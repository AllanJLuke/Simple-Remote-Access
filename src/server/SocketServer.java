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
    public final static String BASE_STORAGE = System.getProperty("user.home");
    public final static String END_OF_UPLOAD = "EOF";

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

    public static String showHelp() {
        return "\nls: display contents of current directory.\n"+
                "cd <path>: change directory by providing the absolute path or path relative from current directory.\n"+
                "mkdir <directory-name>: create a directory in current directory by providing directory name.\n"+
                "get <remote-file>: Download a file from server by providing the absolute path or" +
                "path relative from current directory.\n"+
                "put <local-file>: Upload a file by providing the absolute path on local machine.\n";

    }
}
