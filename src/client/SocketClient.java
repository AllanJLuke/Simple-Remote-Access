package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Created by micho on 9/29/15.
 */
public class SocketClient extends CustomProtocolClient {


    private File outputFile;
    public SocketClient(final Socket clientSocket) throws IOException {

        super(new DataInputStream(clientSocket.getInputStream()), new DataOutputStream(clientSocket.getOutputStream()));

        processUserInput();
    }

    private void processUserInput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        while (scanner.hasNext()) {
            userInput = scanner.nextLine();


            if (userInput.startsWith("put")) {

                String[] split = userInput.split(" ");

                if (split.length < 2) {
                    System.out.println("MISSING PATH");
                    System.out.println(showHelp());
                } else {
                    File file = new File(split[1]);
                    if (file.exists()) {
                        send(userInput);
                        send(file);


                    } else {
                        System.out.println("FILE DOES NOT EXIST, USE AN ABSOLUTE PATH");
                    }
                }

            }
            else if (userInput.startsWith("get"))
            {
                String [] commandChunks = userInput.split(" ");
                if (commandChunks.length < 2)
                {
                    System.out.println("MISSING PATH");
                    System.out.println(showHelp());
                }
                else {
                    send(userInput);
                    File temp = new File(commandChunks[1]);
                    outputFile = new File(System.getProperty("user.home") + File.separator + temp.getName());
                    try {
                        outputFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {

                send(userInput);
            }

        }

    }

    public static String showHelp() {
        return "\nls: display contents of current directory.\n" +
                "cd <path>: change directory by providing the absolute path or path relative from current directory.\n" +
                "mkdir <directory-name>: create a directory in current directory by providing directory name.\n" +
                "get <remote-file>: Download a file from server by providing the absolute path or" +
                "path relative from current directory.\n" +
                "put <local-file>: Upload a file by providing the absolute path on local machine.\n";

    }

    @Override
    public void handleServerOutput(String command) {
        System.out.println(command);
    }

    @Override
    public void handleDownload(byte[] bytes) {
        try {
            Files.write(outputFile.toPath(),bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
