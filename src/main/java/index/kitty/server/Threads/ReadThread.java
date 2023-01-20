package index.kitty.server.Threads;

import index.kitty.server.Main;
import index.kitty.server.Methods.DataFactory;
import index.kitty.server.Models.Client;
import index.kitty.server.Models.Datas.Data;

import java.io.IOException;
import java.net.SocketException;

public class ReadThread extends Thread {
    private Thread thread;
    private final String threadName = "KittySocketReadThread";
    private final Client client;

    public ReadThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String dataSource = client.getData();
                System.out.println(dataSource);
                if (dataSource == null) {
                    // when the socket is closed
                    Main.mainServer.removeClient(client);
                    client.disconnect();
                    return;
                }
                DataFactory.DataAnalysis(new Data(dataSource), client);// Analysis Data
            } catch (SocketException e) {
                // when the socket is closed
                Main.mainServer.removeClient(client);
                client.disconnect();
                return;
            } catch (IOException e) {
                // TODO ERR log
            }
        }
    }

    @Override
    public synchronized void start() {
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
