/*
Write a networked application that converts between Morse code, and it's English-language equivalent.
The client should send either Morse code or an English sentence.
The server should then convert the message to its counterpart and send it back to the client.

Use one blank between each Morse-coded letter and three blanks between each Morse-coded word.
 */
import javax.swing.*;

public class ServerDriver {

    public static void main(String[] args) {
        Server linkedServer = new Server();
        linkedServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        linkedServer.runServer();
    }
}
