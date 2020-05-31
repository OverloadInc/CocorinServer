package server;

import javax.swing.*;

public class Monitor {
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JPanel logPanel;
    private JPanel statusPanel;
    private JLabel lblName;
    private JTextPane txtConsole;
    private JProgressBar progressBar;

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        JFrame frame = new JFrame("JChat Server Monitor");

        frame.setContentPane(new Monitor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}