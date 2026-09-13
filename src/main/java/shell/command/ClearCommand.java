package shell.command;

import java.util.List;
import shell.io.Redirection;

public class ClearCommand extends BuiltinCommand {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public void execute(List<String> args, Redirection redirection) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
