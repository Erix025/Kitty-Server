package index.kitty.server.Models;

import index.kitty.server.Threads.ReadThread;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ReadThread readThread;
    private String clientType;
    public Client(Socket socket) {
        this.socket = socket;
        try {
            //set DataStream
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            readThread = new ReadThread(this);
            readThread.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getInputStream() {
        return in;
    }

    public DataOutputStream getOutStream() {
        return out;
    }

    // get Socket Address
    public String getClientAddressInfo() {
        return socket.getInetAddress().getHostAddress();
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // get a line from the DataInputStream
    public String getData() throws EOFException {
        String line = "";
        try {
            line = in.readUTF();
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
            out.writeUTF(data);
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
