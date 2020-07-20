package over.server;

import over.config.Configurator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * <code>ClientThread</code> class provides a set of methods to control the chat operations for each connected client.
 *
 * @author Overload Inc.
 * @version %I%, %G%
 */
public class ClientThread extends Thread {

    /**
     * Client communication socket.
     */
    private final Socket socket;

    /**
     * Stream to send objects to the server.
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * Stream to receive objects from the server.
     */
    private ObjectInputStream objectInputStream;

    /**
     * Server's thread.
     */
    private final Server server;

    /**
     * Unique client <code>Id</code>.
     */
    private String idClient;

    /**
     * Indicates if a thread is listening a specific client.
     */
    private boolean isListening;

    /**
     * <code>ClientThread</code> class constructor.
     * @param socket the socket to establish communication with the server.
     * @param server the target chat server.
     */
    public ClientThread(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException ex) {
            System.err.println(Configurator.getConfigurator().getProperty("message01"));
        }
    }

    /**
     * Closes the current socket communication.
     */
    public void disconnect() {
        try {
            socket.close();

            isListening = false;
        }
        catch (IOException ex) {
            System.err.println(Configurator.getConfigurator().getProperty("message02"));
        }
    }

    /**
     * Executes the thread.
     */
    @Override
    public void run() {
        try{
            listen();
        }
        catch (Exception ex) {
            System.err.println(Configurator.getConfigurator().getProperty("message03"));
        }

        disconnect();
    }

    /**
     * Listen what the current client sends.
     */
    public void listen(){
        isListening = true;

        while(isListening){
            try {
                Object object = objectInputStream.readObject();

                if(object instanceof LinkedList)
                    execute((LinkedList<String>)object);
            }
            catch (Exception e) {
                System.err.println(Configurator.getConfigurator().getProperty("message04"));
            }
        }
    }

    /**
     * Performs specific tasks depending on the socket's input and output parameters.
     * <p>
     * <code>1</code> indicates the transmitter client.
     * <p>
     * <code>2</code> indicates the receiver client.
     * <p>
     * <code>3</code> indicates a specific client message.
     * @param list the client list.
     */
    public void execute(LinkedList<String> list){
        String type = list.get(0);

        switch (type) {
            case "CONNECTION_REQUEST":
                acceptConnection(list.get(1));
                break;
            case "DISCONNECTION_REQUEST":
                acceptDisconnection();
                break;
            case "MESSAGE":
                String receiver = list.get(2);
                server.clientList.stream().filter(h -> (receiver.equals(h.getIdClient()))).forEach((h) -> h.sendMessage(list));
                break;
            default:
                break;
        }
    }

    /**
     * Sends a specific message through the socket.
     * @param list the object list.
     */
    private void sendMessage(LinkedList<String> list){
        try {
            objectOutputStream.writeObject(list);
        }
        catch (Exception e) {
            System.err.println(Configurator.getConfigurator().getProperty("message05"));
        }
    }

    /**
     * Notifies to each connected client the existence of a new incoming client.
     * @param id the target client <code>Id</code> for establishing communication.
     */
    private void acceptConnection(String id) {
        Server.incrementCorrelative();

        this.idClient = Server.getCorrelative() + " - " + id;

        LinkedList<String> incomingData = new LinkedList<>();
        incomingData.add("ACCEPTED_CONNECTION");
        incomingData.add(this.idClient);
        incomingData.addAll(server.getConnectedUsers());

        sendMessage(incomingData);

        server.addLog(Configurator.getConfigurator().getProperty("userConnected") + " ", 2);
        server.addLog("" + this.idClient, 1);

        LinkedList<String> newConnected = new LinkedList<>();
        newConnected.add("NEW_USER_CONNECTED");
        newConnected.add(this.idClient);

        server.clientList.stream().forEach(client -> client.sendMessage(newConnected));
        server.clientList.add(this);
    }

    /**
     * Returns the unique client <code>Id</code> in the chat.
     * @return the unique client <code>Id</code>.
     */
    public String getIdClient() {
        return idClient;
    }

    /**
     * Notifies to each connected client whenever a client is leaving the chat.
     */
    private void acceptDisconnection() {
        LinkedList<String> disconnectedClient = new LinkedList<>();
        disconnectedClient.add("USER_DISCONNECTED");
        disconnectedClient.add(this.idClient);

        server.addLog(Configurator.getConfigurator().getProperty("userDisconnected") + " ", 2);
        server.addLog("" + this.idClient, 1);

        this.disconnect();

        for(int i = 0; i < server.clientList.size(); i++) {
            if(server.clientList.get(i).equals(this)) {
                server.clientList.remove(i);
                break;
            }
        }

        server.clientList.stream().forEach(h -> h.sendMessage(disconnectedClient));
    }
}