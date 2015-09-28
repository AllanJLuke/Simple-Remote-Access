package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by allan on 2015-09-20.
 */
public class SocketServer {

    private static ServerSocket socket;
    private static boolean running;
    private final static boolean isLinux;

    static {
        isLinux = !System.getProperty("os.name").toLowerCase().startsWith("windows");
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
               new Thread(new ServerHandler(socket.accept(),isLinux)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
