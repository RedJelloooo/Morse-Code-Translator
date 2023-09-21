import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This code is based off of Fig-28_03_06 along with my own changed for worse code
 */
public class Server extends JFrame {

    private final JTextField userEntry;
    private final JTextArea displayToUser;
    private ServerSocket server; // server socket
    private ObjectOutputStream outputStream; // output stream to client
    private ObjectInputStream inputStream; // input stream from client
    private Socket connection; // connection to client
    private int connectionCounter = 1; // counter of number of connections

    /**
     * sets up the GUI interface on the server side
     */
    public Server(){
        super("Server");

        userEntry = new JTextField();
        userEntry.setEditable(false);
        userEntry.addActionListener(
                new ActionListener(){
                    //this is sent to the client
                    public void actionPerformed(ActionEvent event){
                        sendData(event.getActionCommand());
                        userEntry.setText("");
                    }
                }
        );

        add(userEntry, BorderLayout.NORTH);

        displayToUser = new JTextArea();
        add(new JScrollPane(displayToUser), BorderLayout.CENTER);

        setSize(500, 350);

        setVisible(true);
    }

    /**
     * this function runs the server and throws IO and EOF exceptions
     */
    public void runServer(){
        try {
            server = new ServerSocket(23735, 100);

            while (true) {
                try {
                    waitForConnection();
                    getStreams();
                    processConnection();


                } catch (EOFException eofException) {
                    messageDisplay("\n server terminated connection.");
                } finally {
                    closeConnection();
                    connectionCounter++;
                }
            }
        }
        catch(IOException ioException){
            ioException.getStackTrace();
        }
    }

    /**
     * waits for the connection and displays the info
     * @throws IOException if the connection to the client fails
     */
    public void waitForConnection() throws IOException {
        messageDisplay("Waiting to connect...\n");
        connection = server.accept();
        messageDisplay("Connection: " + connectionCounter + " received from: " + connection.getInetAddress().getHostName());
    }

    /**
     * gets the input and output streams for data
     * @throws IOException if fetching the data streams fails
     */
    public void getStreams() throws IOException{
        // gets the output streams and flushes
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        outputStream.flush(); // flush buffer

        //gets the input streams
        inputStream = new ObjectInputStream(connection.getInputStream());

        messageDisplay("\nGot input and output Streams!\n");
    }

    /**
     * processes the connection with the client
     * @throws IOException if at some point processing the connection fails
     */
    public void processConnection() throws IOException{

        String message = "Connection Success";
        sendData(message);

        setTextFieldEditable();
        //process messages from client and display it
        do{
            try{
                message = (String) inputStream.readObject();
                messageDisplay("\nCLIENT>>> " + message);
                sendData(convert(message));
            }
            catch (ClassNotFoundException cNFE){
                messageDisplay("\nUnknown object type received");
            }

        }while(!message.equals("CLIENT>>> TERMINATE"));
    }

    /**
     * closes the connection with the user and throws exception if it fails
     */
    public void closeConnection(){
        messageDisplay("\nTerminating the Connection\n");
        setTextFieldEditable();

        try{
            outputStream.close();
            inputStream.close();
            connection.close(); //closes the streams and socket
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    /**
     * This method does the work of converting the given message into either morse code or roman letters
     * if the first character is a period or a dash it is assumed that it is morse code
     * @param message message to be converted
     */
    private String convert(String message){

        StringBuilder returnStr = new StringBuilder(); // string builder to make sentences

        message = message.trim();

        if(message.charAt(0) == '.' || message.charAt(0) == '-'){ // checks if first character is a period or dash for morse code
            String[] splitMessage = message.split("   ");
            for(String x: splitMessage){
                returnStr.append(Encoder.translateToLetters(x).append(" "));
            }
        }
        else{ //if no period or dash in first char then it converts to roman letters
            String[] split = message.split(" ");
            for(String x: split){
                returnStr.append(Encoder.translateToMorse(x).append("   "));
            }
        }

        return returnStr.toString();
    }

    /**
     * sends data to the client as long as it is a valid object
     * @param message message to be sent to teh server
     */
    private void sendData(String message){

        //send message and then flush the output
        try{
            outputStream.writeObject("SERVER>>> " + message);
            outputStream.flush();

            messageDisplay("\nSERVER>>> " + message);

        }
        catch (IOException ioException){
            displayToUser.append("\nSorry, error writing in the object.");
        }
    }

    /**
     * sets the text field to editable
     */
    private void setTextFieldEditable(){

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() { // changes if the text field is editable or not
                        userEntry.setEditable(false);
                    }
                }
        );

    }

    /**
     * displays the message on the GUI
     */
    private void messageDisplay(final String message)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run() // updates the area
                    {
                        displayToUser.append(message); //adds the message
                    }
                }
        );
    }


}
