import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This code is based off of Fig-28_03_06 along with my own changed for worse code
 */

public class Client extends JFrame{

    private Socket client; // socket to communicate with server
    private ObjectOutputStream outputStream; // output to server
    private ObjectInputStream inputStream; // input from server
    private final String LinkedListServer; // host server
    private String message = ""; // message from the host server

    private final JTextField userEntry;
    private final JTextArea userDisplay; // get info from user and display it

    /**
     * sets up a GUI for the client
     * @param host - name of the host server
     */
    public Client(String host){
        super("Client");

        LinkedListServer = host;

        userEntry = new JTextField();
        userEntry.setEditable(false);

        userEntry.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        sendData(actionEvent.getActionCommand());
                        userEntry.setText("");
                    }
                }
        );

        add(userEntry, BorderLayout.NORTH);

        userDisplay = new JTextArea();
        add(new JScrollPane(userDisplay), BorderLayout.CENTER);

        setSize(500, 350);
        setVisible(true);
    }

    /**
     * runs the client part by fetching streams and connecting and closes it afterwards
     */
    public void runClient(){
        try{
            connectToServer();
            getStreams();
            processConnection();
        }
        catch (EOFException eofException){
            messageDisplayer("\nClient has terminated the connection.");
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    /**
     * this function connects to the server
     * IOException
     * @throws IOException - if cannot connect to server
     */
    private void connectToServer() throws IOException {
        messageDisplayer("Attempting to connect...\n");

        // creates the socket server connection
        client = new Socket(InetAddress.getByName(LinkedListServer), 23735);

        messageDisplayer( "Connected to " + client.getInetAddress().getHostName() );
    }

    /**
     * this function gets the streams to send and receive data from the server
     * IOException
     * @throws IOException - if getting the streams fails
     */
    private void getStreams() throws IOException{

        //output
        outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.flush(); // flush to send header info

        //input
        inputStream = new ObjectInputStream(client.getInputStream());

        messageDisplayer("\nfetched I/O streams!\n");
    }

    /**
     * method to process the connection to the server
     * @throws  IOException if the connection fails to process
     */
    private void processConnection() throws IOException{
        setTextFieldEditable(true);

        do{
            try{

                message = (String) inputStream.readObject();
                messageDisplayer("\n" + message);
            }
            catch(ClassNotFoundException classNotFoundException){

                messageDisplayer("\n unknown object type received");
            }

        }while(!message.equals("SERVER>>> TERMINATE"));
    }

    /**
     * closes the connection to the server and closes the streams
     */
    private void closeConnection(){
        messageDisplayer("\n closing the connection to server...");
        setTextFieldEditable(false);

        try{
            outputStream.close();
            inputStream.close();
            client.close();
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    /**
     * sends the message to the server
     * @param serverMessage - message to be sent
     */
    private void sendData(String serverMessage){

        try{
            outputStream.writeObject(serverMessage);
            outputStream.flush();
            messageDisplayer("\nCLIENT>>> " + serverMessage);
        }
        catch(IOException ioException){
            messageDisplayer("\nError writing");
        }
    }

    /**
     * changes the state of the text field
     * @param editable - changes the editable state of the text field
     */
    private void setTextFieldEditable(final boolean editable){

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userEntry.setEditable(editable);
                    }
                }
        );
    }

    /**
     * displays the given message in the GUI
     * @param message - message to be displayed on the GUI
     */
    private void messageDisplayer(final String message){
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userDisplay.append(message);
                    }
                }
        );
    }
}
