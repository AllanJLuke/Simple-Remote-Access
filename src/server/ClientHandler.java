package server;

import utils.ShellEmu;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Created by allan on 2015-09-21.
 */
public class ClientHandler extends CustomProtocolServer implements Runnable {

     private final ShellEmu shellEmu;
    File outputFile;
    public ClientHandler(final Socket socket, final boolean isLinux) throws IOException {
        super(new DataInputStream(socket.getInputStream()),new DataOutputStream(socket.getOutputStream()));;
        shellEmu = new ShellEmu(isLinux);
     }

    @Override
    public void run()
    {
    }

    @Override
    public void handleInput(String command)  {
        System.out.println("COMMAND: " + command);
       String [] commandChunks = command.split(" ");

                        switch (commandChunks[0]) {
                            case "put":
                                File temp = new File(commandChunks[1]);
                                outputFile = new File(shellEmu.getWorkingDirectory() + File.separator + temp.getName());
                                try {
                                    outputFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "get":
                                File file = new File(commandChunks[1]);
                                if (!file.isAbsolute())
                                    file = new File(shellEmu.getWorkingDirectory() + File.separator + commandChunks[1]);
                                if (!file.exists())
                                {
                                    try {
                                        send("File does not exist.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    try {
                                        send (file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            default:
                                try {
                                    send(shellEmu.executeCommand(command));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        }
        
    }

    @Override
    public void handleUpload(byte[] bytes) {
        try {
            Files.write(outputFile.toPath(),bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
