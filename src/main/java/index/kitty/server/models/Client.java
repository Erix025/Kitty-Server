package index.kitty.server.models;

import index.kitty.server.threads.ReadThread;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Client {
    private final Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientType;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            // set DataStream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            ReadThread readThread = new ReadThread(this);
            readThread.start();
        } catch (IOException e) {
            //todo log
        }
    }
    // get Socket Address
    public String getClientAddressInfo() {
        return socket.getInetAddress().getHostAddress();
    }

    // disconnect client
    public void disconnect() {
        try {
            socket.close();
            // remove logged client and user
        } catch (IOException e) {
            //todo log
        }
    }

    // get a line from the DataInputStream
    public String getData() throws SocketException {
        String line = "";
        try {
            line = in.readLine();
        } catch (SocketException e) {
            throw e;
        } catch (IOException e) {
            //todo log
        }
        return line;
    }

    public void putData(String data) {
        try {
            out.write(data + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {
        return clientType;
    }
}
