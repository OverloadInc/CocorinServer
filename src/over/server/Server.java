package over.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Server extends Thread {

    /**
     * Listen new client connections to include them in all available chats.
     */
    private ServerSocket serverSocket;

    /**
     * List all client communication threads.
     * Each client is related to one thread which continuously listen what the client sends to the server.
     */
    LinkedList<ClientThread> clientList;

    /**
     * Server's monitor instance.
     */
    private final Monitor monitor;

    /**
     * Server's port.
     */
    private final String port;

    /**
     * Correlative to make unique each connected client even when they have the same name.
     */
    private static int correlative;

    /**
     * Server class constructor.
     * @param port the target port.
     * @param monitor the target component to display the resulting messages.
     */
    public Server(String port, Monitor monitor) {
        correlative = 0;
        this.port = port;
        this.monitor = monitor;
        clientList = new LinkedList<>();

        this.start();
    }

    /**
     * Returns the correlative.
     * @return the unique client ID.
     */
    public static int getCorrelative() {
        return correlative;
    }

    /**
     * Increments the correlative index to make two or more users with the same name different.
     */
    public static void incrementCorrelative() {
        correlative++;
    }

    /**
     * Executes the thread to listen to new user connections.
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.valueOf(port));

            monitor.addInitServer();

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("New incoming connection: " + socket);

                ClientThread clientThread = new ClientThread(socket, this);
                clientThread.start();
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Server failed to start.\n" +
                    "Please, verify the port number used.\nThe application will be closed.");

            System.exit(0);
        }
    }

    /**
     * Returns a list of connected client IDs.
     * @return the list of connected users.
     */
    LinkedList<String> getConnectedUsers() {
        LinkedList<String> connectedUsers = new LinkedList<>();

        clientList.stream().forEach(c -> connectedUsers.add(c.getIdClient()));

        return connectedUsers;
    }

    /**
     * Adds a new line to the monitor's console.
     * @param text the message to display in the monitor's console.
     */
    void addLog(String text) {
        monitor.addLog(text);
    }
}