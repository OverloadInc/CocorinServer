package server;

import javax.swing.*;
import java.awt.*;

public class Monitor {
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JPanel logPanel;
    private JPanel statusPanel;
    private JLabel lblName;
    private JTextPane txtConsole;
    private JProgressBar progressBar;
    private final String DEFAULT_PORT="10101";
    private final Server server;
    private FontEditor fontEditor = new FontEditor();

    public Monitor() {
        String port = getPort();
        server = new Server(port, this);
    }

    /**
     * Método que agrega una línea de texto al log.
     * @param text
     */
    void addLog(String text) {
        fontEditor.setSimple(txtConsole, text);
    }

    /**
     * Método que abre una ventana para que el usuario ingrese el puerto que
     * desea utilizar para que el servidor escuche.
     * @return
     */
    private String getPort() {
        String defaultPort = DEFAULT_PORT;

        JTextField txtPort = new JTextField(20);
        txtPort.setText(defaultPort);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(2, 1));
        myPanel.add(new JLabel("Connection port:"));
        myPanel.add(txtPort);

        int result = JOptionPane.showConfirmDialog(null, myPanel,"Communication configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            defaultPort = txtPort.getText();
        }
        else{
            System.exit(0);
        }

        return defaultPort;
    }

    /**
     * Método que agrega un mensaje de confirmación al log cuando el servidor está
     * corriendo correctamente.
     */
    void addInitServer() {
        fontEditor.setSimple(txtConsole, "Server initialized.");
    }

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        JFrame frame = new JFrame();
        frame.setContentPane(new Monitor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}