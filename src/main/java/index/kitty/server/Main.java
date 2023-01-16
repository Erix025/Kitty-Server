package index.kitty.server;

import index.kitty.server.Models.Client;
import index.kitty.server.Models.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static Server mainServer;
    public static void main(String[] args) {
        mainServer = new Server(8808);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true)
        {
            String order = "";
            try {
                order = bufferedReader.readLine();
            }catch (IOException e)
            {
                System.out.println(e);
            }
            switch (order)
            {
                case "exit":
                    mainServer.Close();
                    System.exit(0);
                case "ConnectInfo":
                    for(Client client : mainServer.Clients)
                    {
                        System.out.println(client.getClientAddressInfo());
                    }
                    if(mainServer.Clients.size() == 0)
                    {
                        System.out.println("No Connections");
                    }
                    break;
            }
        }
    }
}