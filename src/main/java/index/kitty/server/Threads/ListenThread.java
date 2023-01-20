package index.kitty.server.Threads;

import index.kitty.server.Models.Client;
import index.kitty.server.Models.Server;

import java.io.IOException;
import java.net.Socket;

public class ListenThread extends Thread {
    private Thread thread;
    private final Server server;

    public ListenThread(Server server) {
        this.server = server;
    }

    public void run() {
        while (!isInterrupted()) {
            Socket acceptedSocket = null;
            System.out.println("Waiting for a client ...");
            try {
                acceptedSocket = server.serverSocket.accept();
                // check if this client has connected
                if (server.isClientConnected(acceptedSocket)) {
                    // TODO log
                    System.out.println("Client has connected");
                    acceptedSocket.close();
                    continue;
                }
                server.Clients.add(new Client(acceptedSocket));
                System.out.println("Client accepted");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void start() {
        if (thread == null) {
            String threadName = "KittyServerListenThread";
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}