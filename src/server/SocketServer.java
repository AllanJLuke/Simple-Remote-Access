package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by allan on 2015-09-20.
 */
public class SocketServer {

    private static ServerSocket socket;
    private static boolean running;


    static {

        running = false;
        try {
            socket = new ServerSocket(0);
            System.out.println("Server started on " + socket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-69);
        }
    }

    private SocketServer() {
    }

    public static void start() {
        running = true;
        while (running) {
            try {
               new Thread(new ServerHandler(socket.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
