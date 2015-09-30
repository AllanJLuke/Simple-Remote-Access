package client;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by micho on 9/29/15.
 */
public class Client {
    public int n = 1;
    public static Socket sock;


    public static void main(String[] args) throws IOException {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        sock = new Socket(ip, port);
        Scanner scan = new Scanner(System.in);
        String input;
        String serverOut = "";
        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();
        PrintWriter pw = new PrintWriter(out, true);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        while (scan.hasNext())

        {
            input = scan.nextLine();
            pw.println(input);

            while((serverOut = br.readLine())!= null){
                System.out.println(serverOut);
            }
        }
    }
}
