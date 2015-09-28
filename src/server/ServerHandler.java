package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by allan on 2015-09-21.
 */
public class ServerHandler implements Runnable {

    private final Socket clientSocket;

    public ServerHandler(final Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (


                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            String outputLine;

            // Initiate conversation with client

//            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
              System.out.println(inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
