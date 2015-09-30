package server;

import utils.ShellEmu;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by allan on 2015-09-21.
 */
public class ServerHandler implements Runnable {

    private final Socket clientSocket;
    private final ShellEmu shellEmu;

    public ServerHandler(final Socket socket, final boolean isLinux) {
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
            PrintWriter outputFile = null;
            String[] commandChunks;
            while ((inputLine = in.readLine()) != null) {
                try {
                    if (outputFile == null) {
                        commandChunks = inputLine.split(" ");
                        switch (commandChunks[0]) {
                            case "put":
                                outputFile = new PrintWriter(new FileWriter(SocketServer.BASE_STORAGE + commandChunks[1], true));
                            case "get":
                                File file = new File (commandChunks[1]);
                                if (!file.isAbsolute())
                                    file = new File(shellEmu.getWorkingDirectory() + File.pathSeparator + commandChunks[1]);
                                clientSocket.getOutputStream().write(Files.readAllBytes(file.toPath()));
                                break;
                            default:
                                out.println(shellEmu.executeCommand(inputLine));

                        }
                    } else {
                        if (inputLine.equals(SocketServer.END_OF_UPLOAD))
                            outputFile = null;
                        else {
                            outputFile.println(inputLine);
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException e)
                {
                    out.println("INVALID COMMAND FORMAT");
                    out.println(SocketServer.showHelp());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
