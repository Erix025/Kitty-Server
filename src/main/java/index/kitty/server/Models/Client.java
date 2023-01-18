package index.kitty.server.Models;

import index.kitty.server.Threads.ReadThread;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private ReadThread readThread;
    private String clientType;
    public Client(Socket socket) {
        this.socket = socket;
        try {
            //set DataStream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            readThread = new ReadThread(this);
            readThread.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInputStream() {
        return in;
    }

    public BufferedWriter getOutStream() {
        return out;
    }

    // get Socket Address
    public String getClientAddressInfo() {
        return socket.getInetAddress().getHostAddress();
    }
    // disconnect client
    public void disconnect() {
        try {
            socket.close();
            //remove logged client and user
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // get a line from the DataInputStream
    public String getData() throws EOFException {
        String line = "";
        try {
            line = in.readLine();
        } catch (EOFException e) {
            throw e;
        } catch (IOException e) {
            System.out.println(e);
        }
        return line;
    }
    public void putData(String data)
    {
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
