package server;

import javax.swing.*;
import java.awt.*;

/**
 * Monitor class encapsulates a mechanism to record all historical data regarding connected clients interactions.
 */
public class Monitor extends JFrame {
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JLabel lblStatus;
    private JPanel logPanel;
    private JPanel mainPanel;
    private JPanel statusPanel;
    private JMenuBar menuBar;
    private JMenuItem optAbout;
    private JMenuItem optExit;
    private JScrollPane scrollConsole;
    private JTextPane txtConsole;
    private FontEditor fontEditor;
    private final String DEFAULT_PORT="10101";
    private final Server server;

    /**
     * Monitor class constructor
     */
    public Monitor() {
        initComponents();

        String port = getPort();
        server = new Server(port, this);
    }

    /**
     * Initializes the Monitor class' components
     */
    public void initComponents() {
        fontEditor = new FontEditor();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        optExit = new JMenuItem();
        helpMenu = new JMenu();
        optAbout = new JMenuItem();
        mainPanel = new JPanel();
        logPanel = new JPanel();
        statusPanel = new JPanel();
        scrollConsole = new JScrollPane();
        txtConsole = new JTextPane();
        lblStatus = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("JChat Client v0.1");
        setMinimumSize(new Dimension(600, 600));
        setName("frmMonitor");
        setJMenuBar(menuBar);

        optExit.setText("Exit");
        optExit.setName("optExit");

        fileMenu.setText("File");
        fileMenu.setName("fileMenu");
        fileMenu.add(optExit);

        optAbout.setText("About");
        optAbout.setName("optAbout");

        helpMenu.setText("Help");
        helpMenu.setName("helpMenu");
        helpMenu.add(optAbout);

        menuBar.setName("menuBar");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        txtConsole.setName("txtConsole");

        scrollConsole.setName("scrollConsole");
        scrollConsole.setViewportView(txtConsole);

        logPanel.setName("logPanel");
        logPanel.setLayout(new BorderLayout());
        logPanel.add(scrollConsole, BorderLayout.CENTER);

        lblStatus.setText("Powered by Overload Inc.");
        lblStatus.setName("lblStatus");

        statusPanel.setName("statusPanel");
        statusPanel.add(lblStatus);

        mainPanel.setName("mainPanel");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(logPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        pack();

        setLocationRelativeTo(null);
    }

    /**
     * Adds a new message line to the monitor's console.
     * @param text the new message line
     */
    void addLog(String text) {
        fontEditor.setSimple(txtConsole, text);
    }

    /**
     * Builds a new window to configure the server's port
     * @return the port number established to the server
     */
    private String getPort() {
        String defaultPort = DEFAULT_PORT;

        JTextField txtPort = new JTextField(20);
        txtPort.setText(defaultPort);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel("Connection port:"));
        panel.add(txtPort);

        int result = JOptionPane.showConfirmDialog(this.mainPanel, panel,"Communication configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION)
            defaultPort = txtPort.getText();
        else
            System.exit(0);

        return defaultPort;
    }

    /**
     * Displays a message when the server is running successfully.
     */
    void addInitServer() {
        fontEditor.setSimple(txtConsole, "Server initialized.");
    }

    /**
     * Starts the Monitor execution.
     * @param args initial input parameters.
     */
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Monitor().setVisible(true);
            }
        });
    }
}