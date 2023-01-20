package index.kitty.server.threads;

import index.kitty.server.Main;
import index.kitty.server.methods.DataFactory;
import index.kitty.server.models.Client;
import index.kitty.server.models.datas.Data;

import java.net.SocketException;

public class ReadThread extends Thread {
    private Thread thread;
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
                    //todo log
                    Main.mainServer.removeClient(client);
                    client.disconnect();
                    return;
                }
                DataFactory.DataAnalysis(new Data(dataSource), client);// Analysis Data
            } catch (SocketException e) {
                // when the socket is closed
                //todo log
                Main.mainServer.removeClient(client);
                client.disconnect();
                return;
            }
        }
    }

    @Override
    public synchronized void start() {
        if (thread == null) {
            String threadName = "KittySocketReadThread";
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
