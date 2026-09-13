package shell.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import shell.Shell;
import shell.io.Redirection;

public class SystemProcessExecutor implements ProcessExecutor {
    private Path workingDirectory;

    public SystemProcessExecutor() {
        this.workingDirectory = Path.of(System.getProperty("user.dir"));
    }

    public void setWorkingDirectory(Path dir) {
        this.workingDirectory = dir;
    }

    @Override
    public void execute(String command, List<String> args, Redirection redirection) {
        // On Windows, wrap in "cmd /c" so shell built-ins (dir, more, whoami, hostname,
        // ping, etc.) are found and work correctly.
        List<String> finalArgs;
        if (Shell.IS_WINDOWS) {
            finalArgs = new ArrayList<>();
            finalArgs.add("cmd");
            finalArgs.add("/c");
            finalArgs.addAll(args);
        } else {
            finalArgs = new ArrayList<>(args);
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(finalArgs);
            processBuilder.directory(workingDirectory.toFile());

            if (redirection != null) {
                Path redirectPath = workingDirectory.resolve(redirection.getFile());
                File file = redirectPath.toFile();
                createParentDirs(file);

                if (redirection.isStderr()) {
                    // stderr → file, stdout → GUI
                    processBuilder.redirectError(getRedirect(file, redirection.isAppend()));
                    processBuilder.redirectErrorStream(false);
                    Process process = processBuilder.start();
                    // Pipe stdout to GUI
                    pipeStreamToOut(process.getInputStream(), false);
                    process.waitFor();
                } else {
                    // stdout → file, stderr → GUI
                    processBuilder.redirectOutput(getRedirect(file, redirection.isAppend()));
                    processBuilder.redirectErrorStream(false);
                    Process process = processBuilder.start();
                    // Pipe stderr to GUI
                    pipeStreamToOut(process.getErrorStream(), true);
                    process.waitFor();
                }
            } else {
                // No redirection: capture both streams and route through System.out/err
                // so output appears in the GUI TextArea
                processBuilder.redirectErrorStream(false);
                Process process = processBuilder.start();

                Thread stdoutThread = pipeStreamToOutAsync(process.getInputStream(), false);
                Thread stderrThread = pipeStreamToOutAsync(process.getErrorStream(), true);

                process.waitFor();
                stdoutThread.join(3000);
                stderrThread.join(3000);
            }
        } catch (IOException e) {
            String errMsg = command + ": command not found";
            if (redirection != null && redirection.isStderr()) {
                try {
                    Path redirectPath = workingDirectory.resolve(redirection.getFile());
                    String msg = errMsg + "\n";
                    if (redirection.isAppend()) {
                        Files.writeString(redirectPath, msg, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } else {
                        Files.writeString(redirectPath, msg, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    }
                } catch (IOException ignored) {}
            } else {
                throw new ProcessExecutionException(errMsg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessExecutionException("Process execution interrupted", e);
        }
    }

    /** Synchronously drain a stream to System.out or System.err */
    private void pipeStreamToOut(InputStream stream, boolean isError) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = stream.read(buffer)) != -1) {
            String chunk = new String(buffer, 0, read, StandardCharsets.UTF_8);
            if (isError) {
                System.err.print(chunk);
            } else {
                System.out.print(chunk);
            }
        }
    }

    /** Asynchronously drain a stream in a background thread */
    private Thread pipeStreamToOutAsync(InputStream stream, boolean isError) {
        Thread t = new Thread(() -> {
            try {
                pipeStreamToOut(stream, isError);
            } catch (IOException ignored) {}
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    private void createParentDirs(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private ProcessBuilder.Redirect getRedirect(File file, boolean isAppend) {
        return isAppend ? ProcessBuilder.Redirect.appendTo(file) : ProcessBuilder.Redirect.to(file);
    }
}
