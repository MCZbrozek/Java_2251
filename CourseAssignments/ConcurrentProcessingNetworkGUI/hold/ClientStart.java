// Adapted from clientSide assignment 
public class ClientStart {
    public static void main(String[] args) {
        // Client application
        Client client;

        // if no command line args are passed then just connect to the local host
        if (args.length == 0) {
            client = new Client("127.0.0.1");
        } else {
            // or use args to connect
            client = new Client(args[0]);
        }
        // once we know where are connecting, run that client!
        client.runClient();
    }

}
