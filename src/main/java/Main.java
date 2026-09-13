import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import shell.Shell;
import shell.autocomplete.AutoCompleter;
import shell.terminal.Termios;;

public class Main {
    private static final BufferedReader WINDOWS_INPUT = new BufferedReader(new InputStreamReader(System.in));
    private static String lastCommand = "";

    public static void main(String[] args) {
        Shell shell = new Shell();

        while (true) {
            final String line = read(shell);

            if (line == null) {
                break;
            } else if (line.isBlank()) {
                continue;
            } else {
                lastCommand = line;
                shell.run(line);
            }
        }
    }

    public static void bell() {
        System.out.print((char) 0x7);
    }

    private static String read(Shell shell) {
        if (Shell.IS_WINDOWS) {
            return readWindowsLine();
        }

        final var autocompleter = new AutoCompleter();

        try (final var rawModeSession = Termios.enableRawMode()) {
            System.out.print("$ ");

            boolean bellRang = false;
            final var line = new StringBuilder();

            while (true) {
                int input = System.in.read();

                if (input == -1) {
                    return null;
                }

                final char character = (char) input;

                switch (character) {
                    case 0x4: {
                        if (!line.isEmpty())
                            continue;
                        return null;
                    }
                    case '\r': {
                        break;
                    }
                    case '\n': {
                        System.out.print('\n');
                        return line.toString();
                    }
                    case '\t': {
                        switch (autocompleter.autocomplete(shell, line, bellRang)) {
                            case NONE -> {
                                bellRang = false;
                                bell();
                            }
                            case FOUND -> {
                                bellRang = false;
                            }
                            case MORE -> {
                                bellRang = true;
                                bell();
                            }
                        }
                        ;
                        break;
                    }
                    case 0x1b: {
                        int next1 = System.in.read();
                        int next2 = System.in.read();
                        if (next1 == '[' && next2 == 'A') { // Up arrow
                            if (!lastCommand.isEmpty()) {
                                // Clear current line visually
                                while (line.length() > 0) {
                                    System.out.print("\b \b");
                                    line.setLength(line.length() - 1);
                                }
                                line.append(lastCommand);
                                System.out.print(lastCommand);
                            }
                        }
                        break;
                    }
                    case 0x7f: {
                        if (line.isEmpty())
                            continue;
                        line.setLength(line.length() - 1);
                        System.out.print("\b \b");
                        break;
                    }
                    default: {
                        line.append(character);
                        System.out.print(character);
                        break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String readWindowsLine() {
        System.out.print("$ ");

        try {
            return WINDOWS_INPUT.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
