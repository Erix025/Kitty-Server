package index.kitty.server.threads;

import index.kitty.server.Main;
import index.kitty.server.models.Client;
import index.kitty.server.models.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public class ListenThread extends Thread {
    private Thread thread;
    private Server server;

    public ListenThread(Server server) {
        this.server = server;
    }

    public void run() {
        while (!isInterrupted()) {
            Socket acceptedSocket;
            server.logger.info("Waiting for a client...");
            try {
                acceptedSocket = server.serverSocket.accept();
                // check if this client has connected
                if (server.isClientConnected(acceptedSocket)) {
                    server.logger.info("Client has connected");
                    acceptedSocket.close();
                    continue;
                }
                server.Clients.add(new Client(acceptedSocket));
                server.logger.info("Client connected");
            } catch (IOException e) {
                server.logger.severe("IOException: cannot connect to the client");
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