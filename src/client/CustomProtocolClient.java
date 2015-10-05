package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Created by allan on 2015-10-04.
 */
public abstract class CustomProtocolClient {


    protected final static int TYPE_STRING = 0;
    protected final static int TYPE_BINARY = 1;

    protected final static int ACK = 5;

    private boolean waitingForACK = false;

    private final DataOutputStream outputStream;
    private final DataInputStream inputStream;

    public CustomProtocolClient(DataInputStream is, DataOutputStream os) {
        this.outputStream = os;
        this.inputStream = is;
        processServerOutput();

    }


    private void send(byte[] data, int type) throws IOException {

        outputStream.writeInt(data.length);
        outputStream.writeInt(type);
        outputStream.write(data);
        outputStream.writeInt(ACK);
        waitingForACK = true;

    }


    protected void send (String data) throws IOException {
        send(data.getBytes(), TYPE_STRING);
    }

    protected void send (File file) throws IOException {
        //   String output;
            byte[] bytes;
            bytes = Files.readAllBytes(file.toPath());
            send(bytes,TYPE_BINARY);


    }



    private void processServerOutput() {

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
                                    System.out.println("SERVER DID NOT ACKNOWLEDGE");
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
                                    handleServerOutput(new String(messageBuffer, Charset.defaultCharset()));
                                }
                                else if (type == TYPE_BINARY)
                                {
                                    handleDownload(messageBuffer);
                                }
                            }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }}).start();
    }


    public abstract void handleServerOutput(String command);

    public abstract void handleDownload(byte [] bytes);
}


