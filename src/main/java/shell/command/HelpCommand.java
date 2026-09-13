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
        System.out.println("  clear           Clear the terminal screen");
        System.out.println("  help            Show this help message");
        System.out.println("  exit            Exit Shell-In-Java");
        System.out.println();
        System.out.println("Advanced examples:");
        System.out.println("  dir / ls        List files in the current directory");
        System.out.println("  more demo.txt   Read a text file");
        System.out.println("  where java      Find an executable in PATH");
        System.out.println("  java -version   Show the Java runtime version");
        System.out.println("  command 2> file Redirect command errors");
        System.out.println();
        System.out.println("External commands are also available through PATH.");
        System.out.println("Redirection: >, >>, 1>, 1>>, 2>, 2>>");
    }
}