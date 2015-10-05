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
                        System.out.println("FILE DOES NOT EXIST");
                    }
                }

            } else {

                send(userInput);
            }

        }

    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.56.1", 49857);
        SocketClient client = new SocketClient(socket);
//        String ip = "192.168.56.1";
//        int port = 55626;
//        clientSocket= new Socket(ip, port);
//        String userInput;
//        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
//        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
//
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    while ((serverOut[0] = br.readLine()) != null) {
////                        System.out.println(serverOut[0]);
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }).start();
//
//        while (scan.hasNext())
//
//        {
//            userInput = scan.nextLine();
//
//            if (userInput.startsWith("put"))
//            {
//
//                String[] split = userInput.split(" ");
//
//                if (split.length < 2)
//                {
//                    System.out.println("MISSING PATH");
//                    System.out.println(showHelp());
//                }
//                else
//                {
//                    File file = new File (split[1]);
//                    if (file.exists())
//                    {
//                      //  pw.println(userInput);
//                        OutputStream os = clientSocket.getOutputStream();
//                        DataOutputStream out = new DataOutputStream(os);
//                   //     BufferedReader reader = new BufferedReader(new FileReader(file));
//                     //   String output;
//                        byte[] bytes = Files.readAllBytes(file.toPath());
//
//
//                   //     outputStream.println(userInput);
//                        //.flush();
//                        os.flush();
//                    //    os.write(bytes);
//                        System.out.println(bytes.length);
//                        out.write(bytes.length);
//                        out.write(bytes);
//
//
//                      //  System.out.println("SENDING  "+ bytes.length+ ". HASH: " + Arrays.hashCode(bytes));
//                      //  os.write(bytes);
//                      //  pw.println(END_OF_UPLOAD);
//                    }
//                    else {
//                        System.out.println("FILE DOES NOT EXIST");
//                    }
//                }
//
//            }else{
//                byte [] bytes = userInput.getBytes();
//
//          //      pw.println(userInput);
//            }
//
//        }
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
}
