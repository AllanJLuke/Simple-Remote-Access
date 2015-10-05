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
//    public void run() {
//        try (
//                PrintWriter out =
//                        new PrintWriter(clientSocket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(clientSocket.getInputStream()));
//
//        ) {
//
//            String inputLine;
//            boolean upload = false;
//            String[] commandChunks;
//            FileWriter outputFile = null;
//            out.println("Current Directory: " + shellEmu.getWorkingDirectory());
//            while ((inputLine = in.readLine()) != null) {
//                try {
//                    if(outputFile == null) {
//                        commandChunks = inputLine.split(" ");
//
//                        switch (commandChunks[0]) {
//                            case "put":
//                                File temp = new File(commandChunks[1]);
//                                outputFile = new FileWriter(new File(shellEmu.getWorkingDirectory() + File.separator + temp.getName()), true);
////                                byte [] bytes = new byte [Integer.parseInt(commandChunks[2])];
////                                clientSocket.getInputStream().read(bytes,0,bytes.length);
////                                Files.write(new File(shellEmu.getWorkingDirectory() + File.separator + temp.getName()).toPath(),bytes);
//
//                                new UploadHandler(clientSocket.getInputStream(),
//                                        new File(shellEmu.getWorkingDirectory() + File.separator + temp.getName())).run();
//                                break;
//                            case "get":
//                                File file = new File(commandChunks[1]);
//                                if (!file.isAbsolute())
//                                    file = new File(shellEmu.getWorkingDirectory() + File.separator + commandChunks[1]);
//                                clientSocket.getOutputStream().write(Files.readAllBytes(file.toPath()));
//                                break;
//                            default:
//                                out.println(shellEmu.executeCommand(inputLine));
//
//                        }
//                    }
//                    else
//                    {
//                        if (!inputLine.equals(SocketServer.END_OF_UPLOAD)) {
//                            System.out.println(inputLine);
//                            outputFile.write(inputLine);
//                        }
//                        else {
//                            out.println("DONE");
//                            outputFile.flush();
//                            outputFile.close();
//                            outputFile = null;
//                        }
//                    }
//
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    out.println("INVALID COMMAND FORMAT");
//                    out.println(SocketServer.showHelp());
//                }
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Connection lost from " + clientSocket.getInetAddress());
//        }
//    }


}
