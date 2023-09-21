import javax.swing.*;

public class ClientDriver {
    public static void main(String[] args) {

        Client linkedClient;
        //Server linkedServer = new Server();

        if(args.length == 0) {
            linkedClient = new Client("127.0.0.1");
        }
        else{
            linkedClient = new Client(args[0]);
        }

        linkedClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        linkedClient.runClient();
    }

}