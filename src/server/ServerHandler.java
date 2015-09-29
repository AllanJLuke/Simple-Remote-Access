package server;

import utils.ShellEmu;

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
    private final ShellEmu shellEmu;
    public ServerHandler(final Socket socket,final boolean isLinux) {
        this.clientSocket = socket;
        shellEmu = new ShellEmu(isLinux);
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
            String commandRoot;
            while ((inputLine = in.readLine()) != null) {
                commandRoot = inputLine.split(" ")[0];
              switch (commandRoot){
                  case "put":
                       handleUpload();
                  case "get":
                     //sendFile();
                      break;
                  default:
                      out.println(shellEmu.executeCommand(inputLine));


              }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUpload() {
        boolean recieving = true;
        while(recieving)
        {
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(clientSocket.getInputStream()));
        }
    }
}
