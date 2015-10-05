import client.SocketClient;
import server.SocketServer;

import java.io.IOException;
import java.net.Socket;

public class Main {
    //MACHESTER IS IEEE 802.3
    //NRZ IS TWO, NRZ-L NRZ-I
    public static void main(String[] args) throws IOException {

        if (args.length == 0)
            SocketServer.start();
        else
        {
         String ip = args[0];
            int port = Integer.parseInt(args[1]);

            Socket socket = new Socket (ip,port);
            SocketClient client = new SocketClient(socket);

        }
    }
}
