import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private static final Color GREEN = new Color(55, 220, 100);
    private static final Color TEXT = new Color(235, 235, 235);

    private final JTextArea output = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton runButton = new JButton("RUN");
    private final Shell shell = new Shell();
    private String lastCommand = "";

    private static final String STARTUP_TEXT = 
        "$ help\n" +
        "Built-in commands:\n" +
        "  cd <directory>  Change the current directory\n" +
        "  pwd             Print the current directory\n" +
        "  echo <text>     Print text\n" +
        "  type <command>  Identify a built-in or external command\n" +
        "  clear           Clear the terminal screen\n" +
        "  help            Show this help message\n" +
        "  exit            Exit Shell-In-Java\n\n" +
        "Advanced examples:\n" +
        "  dir             List files in the current directory\n" +
        "  more demo.txt   Read a text file\n" +
        "  where java      Find an executable in PATH\n" +
        "  java -version   Show the Java runtime version\n" +
        "  command 2> file Redirect command errors\n\n" +
        "External commands are also available through PATH.\n" +
        "Redirection: >, >>, 1>, 1>>, 2>, 2>>\n\n";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RainShower().show());
    }

    private void show() {
        JFrame frame = new JFrame("Shell");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 500));
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(BLACK);
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

    JTextArea title = new JTextArea(
    "#   #  #####  #      #####  #     #\n" +
    "#   #  #      #        #     #   #  \n" +
    "#####  #####  #        #       #   \n" +
    "#   #  #      #        #     #   #  \n" +
    "#   #  #####  #####  #####  #     #");
        title.setForeground(GREEN);
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        title.setBackground(BLACK);
        title.setEditable(false);
        title.setFocusable(false);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        
        JLabel statusBar = new JLabel("CPU: 0.0%  |  RAM: 0.0 / 0.0 GB  |  Disk Free: 0.0 GB");
        statusBar.setForeground(new Color(200, 200, 200));
        statusBar.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        statusBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BLACK);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(statusBar, BorderLayout.SOUTH);
        root.add(topPanel, BorderLayout.NORTH);

        javax.swing.Timer timer = new javax.swing.Timer(1500, e -> updateStats(statusBar));
        timer.start();

        output.setEditable(false);
        output.setBackground(BLACK);
        output.setForeground(TEXT);
        output.setCaretColor(GREEN);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        output.setMargin(new Insets(12, 12, 12, 12));
        output.setText(STARTUP_TEXT);

        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 55)));
        scrollPane.getViewport().setBackground(BLACK);
        root.add(scrollPane, BorderLayout.CENTER);

        JPanel commandBar = new JPanel(new BorderLayout(10, 0));
        commandBar.setBackground(BLACK);
        input.setBackground(new Color(24, 24, 24));
        input.setForeground(TEXT);
        input.setCaretColor(GREEN);
        input.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        input.addActionListener(this::runCommand);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    input.setText(lastCommand);
                }
            }
        });

        runButton.setBackground(GREEN);
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

        lastCommand = command;
        input.setText("");

        if (command.equalsIgnoreCase("clear")) {
            output.setText(STARTUP_TEXT);
            return;
        }

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

    private void updateStats(JLabel statusBar) {
        try {
            com.sun.management.OperatingSystemMXBean osBean = 
                (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            double cpuLoad = osBean.getCpuLoad() * 100;
            if (cpuLoad < 0) cpuLoad = 0;
            
            long totalRam = osBean.getTotalPhysicalMemorySize();
            long freeRam = osBean.getFreePhysicalMemorySize();
            long usedRam = totalRam - freeRam;
            
            java.io.File rootFile = new java.io.File("C:\\");
            long freeDisk = rootFile.getUsableSpace();
            
            String text = String.format("CPU: %.1f%%  |  RAM: %.1f / %.1f GB  |  Disk Free: %.1f GB", 
                cpuLoad, 
                usedRam / (1024.0*1024*1024), 
                totalRam / (1024.0*1024*1024),
                freeDisk / (1024.0*1024*1024));
            
            SwingUtilities.invokeLater(() -> statusBar.setText(text));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> statusBar.setText("Stats unavailable"));
        }
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
