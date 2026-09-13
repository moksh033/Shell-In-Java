package shell.command;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import shell.environment.Environment;
import shell.io.FileUtils;
import shell.io.Redirection;

public class EchoCommand implements Command {
    private final Environment environment;

    public EchoCommand(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void execute(List<String> args, Redirection redirection) {
        List<String> arguments = args.subList(1, args.size());
        String output = String.join(" ", arguments);

        if (redirection != null && redirection.isStdout()) {
            try {
                // Resolve file path relative to shell's current directory
                Path resolved = environment.getCurrentDirectory().resolve(redirection.getFile());
                FileUtils.writeToFile(output + "\n", resolved.toString(), redirection.isAppend());
            } catch (IOException e) {
                System.err.println("echo: " + e.getMessage());
            }
        } else {
            System.out.println(output);
        }
    }
}
