package index.kitty.server.threads;

import index.kitty.server.models.Client;
import index.kitty.server.models.Server;

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
            Socket acceptedSocket;
            //todo log
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
                //todo log
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