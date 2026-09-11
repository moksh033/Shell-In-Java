package shell.command;

import java.util.List;

import shell.io.Redirection;

public class HelpCommand extends BuiltinCommand {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(List<String> args, Redirection redirection) {
        System.out.println("Built-in commands:");
        System.out.println("  cd <directory>  Change the current directory");
        System.out.println("  pwd             Print the current directory");
        System.out.println("  echo <text>     Print text");
        System.out.println("  type <command>  Identify a built-in or external command");
        System.out.println("  help            Show this help message");
        System.out.println("  exit            Exit Shell-In-Java");
        System.out.println();
        System.out.println("External commands are also available through PATH.");
        System.out.println("Redirection: >, >>, 1>, 1>>, 2>, 2>>");
    }
}