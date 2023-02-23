package index.kitty.server.models;

import index.kitty.server.Main;
import index.kitty.server.threads.ReadThread;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

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
            Main.mainServer.logger.info("Waiting for the message from client");
        } catch (IOException e) {
            Main.mainServer.logger.severe("IOException: can not get input or output stream.");
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
            Main.mainServer.logger.info("Client closed.");
        } catch (IOException e) {
            Main.mainServer.logger.severe("IOException: can not close the server");
        }
    }

    // get a line from the DataInputStream
    public String getData() throws SocketException {
        String line = "";
        try {
            line = in.readLine();
            Main.mainServer.logger.fine("got Data:"+line);
        } catch (SocketException e) {
            throw e;
        } catch (IOException e) {
            Main.mainServer.logger.severe("IOException: can not get Data from the client");
        }
        return line;
    }

    public void putData(String data) {
        try {
            out.write(data + "\n");
            out.flush();
            Main.mainServer.logger.fine("send data: "+data);
        } catch (IOException e) {
            Main.mainServer.logger.severe("IOException: can not put Data to the client");
        }
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {
        return clientType;
    }
}
