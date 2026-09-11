import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import shell.Shell;

public class RainShower {
    private static final Color BLACK = new Color(8, 8, 8);
    private static final Color RED = new Color(235, 45, 55);
    private static final Color TEXT = new Color(235, 235, 235);

    private final JTextArea output = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton runButton = new JButton("RUN");
    private final Shell shell = new Shell();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RainShower().show());
    }

    private void show() {
        JFrame frame = new JFrame("Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 500));
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(BLACK);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("<html><pre>  #   #  #######  ###  #   #  #######  #######  ######   #######  ######   #   #  #######  ######<br>  ## ##  #        #   # ## ##  #        #        #     #  #        #     #  #   #  #        #     #<br>  # # #  #####    #   # # # #  #####    #####    ######   #####    ######   #   #  #####    ######<br>  #   #  #        #   # #   #  #        #        #   #    #        #   #    #   #  #        #   #<br>  #   #  #######   ###  #   #  #######  #######  #    #   #######  #    #   #######  #######  #    #</pre></html>");
        title.setForeground(new Color(55, 220, 100));
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        root.add(title, BorderLayout.NORTH);

        output.setEditable(false);
        output.setBackground(BLACK);
        output.setForeground(TEXT);
        output.setCaretColor(RED);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        output.setMargin(new Insets(12, 12, 12, 12));
        output.setText("RainShower terminal\nType help to see available commands.\n\n");

        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 55)));
        scrollPane.getViewport().setBackground(BLACK);
        root.add(scrollPane, BorderLayout.CENTER);

        JPanel commandBar = new JPanel(new BorderLayout(10, 0));
        commandBar.setBackground(BLACK);
        input.setBackground(new Color(24, 24, 24));
        input.setForeground(TEXT);
        input.setCaretColor(RED);
        input.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        input.addActionListener(this::runCommand);

        runButton.setBackground(RED);
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.addActionListener(this::runCommand);
        commandBar.add(input, BorderLayout.CENTER);
        commandBar.add(runButton, BorderLayout.EAST);
        root.add(commandBar, BorderLayout.SOUTH);

        System.setOut(new PrintStream(new TextAreaOutputStream(), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new TextAreaOutputStream(), true, StandardCharsets.UTF_8));

        frame.setContentPane(root);
        frame.setVisible(true);
        input.requestFocusInWindow();
    }

    private void runCommand(ActionEvent event) {
        String command = input.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        input.setText("");
        output.append("$ " + command + "\n");
        input.setEnabled(false);
        runButton.setEnabled(false);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                shell.run(command);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException exception) {
                    output.append(exception.getCause().getMessage() + "\n");
                }
                input.setEnabled(true);
                runButton.setEnabled(true);
                input.requestFocusInWindow();
            }
        }.execute();
    }

    private final class TextAreaOutputStream extends OutputStream {
        @Override
        public void write(int value) throws IOException {
            write(new byte[] {(byte) value}, 0, 1);
        }

        @Override
        public void write(byte[] bytes, int offset, int length) {
            String text = new String(bytes, offset, length, StandardCharsets.UTF_8);
            SwingUtilities.invokeLater(() -> {
                output.append(text);
                output.setCaretPosition(output.getDocument().getLength());
            });
        }
    }
}