package shell.process;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

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
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.directory(workingDirectory.toFile());

            if (redirection != null) {
                // Resolve redirect file relative to shell cwd
                Path redirectPath = workingDirectory.resolve(redirection.getFile());
                File file = redirectPath.toFile();
                createParentDirs(file);

                if (redirection.isStderr()) {
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    processBuilder.redirectError(getRedirect(file, redirection.isAppend()));
                } else {
                    processBuilder.redirectOutput(getRedirect(file, redirection.isAppend()));
                    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                }
            } else {
                processBuilder.inheritIO();
            }

            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            String errMsg = command + ": command not found\n";
            // If stderr is redirected, write the error to the redirect file
            if (redirection != null && redirection.isStderr()) {
                try {
                    Path redirectPath = workingDirectory.resolve(redirection.getFile());
                    if (redirection.isAppend()) {
                        Files.writeString(redirectPath, errMsg, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } else {
                        Files.writeString(redirectPath, errMsg, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    }
                } catch (IOException ignored) {}
            } else {
                throw new ProcessExecutionException(command + ": command not found");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessExecutionException("Process execution interrupted", e);
        }
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
