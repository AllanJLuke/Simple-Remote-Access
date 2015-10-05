package server;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by allan on 2015-10-01.
 */
public abstract class CustomProtocolServer {


    protected final static int TYPE_STRING = 0;
    protected final static int TYPE_BINARY = 1;

    protected final static int ACK = 5;

    private boolean waitingForACK = false;

    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    public CustomProtocolServer(DataInputStream is, DataOutputStream os)
    {
        this.inputStream = is;
        this.outputStream = os;

        processInputStream();

    }



    private void send(byte[] data, int type) throws IOException {

        outputStream.writeInt(data.length);
        outputStream.writeInt(type);
        outputStream.write(data);
        outputStream.writeInt(ACK);
        waitingForACK = true;


    }

    protected void send (String data) throws IOException {
        send(data.getBytes(),TYPE_STRING);
    }


    protected void send (File file) throws IOException {
        //   String output;
        byte[] bytes;
        bytes = Files.readAllBytes(file.toPath());
        send(bytes,TYPE_BINARY);


    }



    private void processInputStream()
    {
        new Thread(new Runnable() {

            byte [] messageBuffer;
            @Override
            public void run() {
                int read;
                try {
                    while((read = inputStream.readInt()) != -1)
                    {
                            if (waitingForACK)
                            {
                                if (read != ACK)
                                {
                                    System.out.println("CLIENT FAILED TO ACKNOWLEDGE TRANSFER");
                                }
                                else waitingForACK = false;
                            }
                            else {

                                messageBuffer = new byte[read];
                                int type = inputStream.readInt();

                                inputStream.readFully(messageBuffer);


                                int ack = inputStream.readInt();

                                if (ack != ACK) {
                                    System.out.println("MALFORMED");
                                } else {
                                    outputStream.writeInt(ACK);
                                }

                                if (type == TYPE_STRING) {
                                    handleInput(new String(messageBuffer, Charset.defaultCharset()));
                                } else if (type == TYPE_BINARY)
                                {
                                    handleUpload(messageBuffer);
                                }
                            }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }}).start();
    }

    public abstract void handleInput(String command);

    public abstract void handleUpload(byte [] bytes);

}
