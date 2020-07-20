package over.server;

import over.config.Configurator;

import javax.swing.*;
import java.awt.*;

/**
 * <code>Monitor</code> class encapsulates a mechanism to record all historical data regarding connected clients interactions.
 *
 * @author Overload Inc.
 * @version %I%, %G%
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

    /**
     * The default port to use for the chat application.
     */
    private final String DEFAULT_PORT="10101";

    /**
     * The server instance.
     */
    private final Server server;

    /**
     * <code>Monitor</code> class constructor.
     */
    public Monitor() {
        initComponents();

        String port = getPort();
        server = new Server(port, this);
    }

    /**
     * Initializes the <code>Monitor</code> class' components.
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
        setTitle(Configurator.getConfigurator().getProperty("serverName"));
        setMinimumSize(new Dimension(600, 600));
        setName("frmMonitor");
        setJMenuBar(menuBar);

        optExit.setText(Configurator.getConfigurator().getProperty("exitOption"));
        optExit.setName("optExit");

        fileMenu.setText(Configurator.getConfigurator().getProperty("fileMenu"));
        fileMenu.setName("fileMenu");
        fileMenu.add(optExit);

        optAbout.setText(Configurator.getConfigurator().getProperty("aboutOption"));
        optAbout.setName("optAbout");

        helpMenu.setText(Configurator.getConfigurator().getProperty("helpMenu"));
        helpMenu.setName("helpMenu");
        helpMenu.add(optAbout);

        menuBar.setName("menuBar");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        txtConsole.setName("txtConsole");
        txtConsole.setEditable(false);

        scrollConsole.setName("scrollConsole");
        scrollConsole.setViewportView(txtConsole);

        logPanel.setName("logPanel");
        logPanel.setLayout(new BorderLayout());
        logPanel.add(scrollConsole, BorderLayout.CENTER);

        lblStatus.setText(Configurator.getConfigurator().getProperty("lblOverload"));
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
     * @param text the new message line.
     * @param style the font style. <code>1</code> to <code>Simple</code> and <code>2</code> to <code>Bold</code>.
     */
    void addLog(String text, int style) {
        switch (style) {
            case 1: fontEditor.setSimple(txtConsole, text); break;
            case 2: fontEditor.setBold(txtConsole, text); break;
        }
    }

    /**
     * Builds a new window to configure the server's port.
     * @return the port number established to the server.
     */
    private String getPort() {
        String defaultPort = DEFAULT_PORT;

        JTextField txtPort = new JTextField(20);
        txtPort.setText(defaultPort);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel(Configurator.getConfigurator().getProperty("connectionPort")));
        panel.add(txtPort);

        int result = JOptionPane.showConfirmDialog(this.mainPanel, panel, Configurator.getConfigurator().getProperty("communication"), JOptionPane.OK_CANCEL_OPTION);

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
        addLog(Configurator.getConfigurator().getProperty("serverInitialized"), 2);
    }

    /**
     * Starts the <code>Monitor</code> execution.
     * @param args initial input parameters.
     */
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Configurator.getConfigurator().initConfigurator();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Monitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new Monitor().setVisible(true));
    }
}