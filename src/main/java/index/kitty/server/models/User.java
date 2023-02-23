package index.kitty.server.models;

import index.kitty.server.Main;

import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.logging.Level;

public class User {
    private final ArrayList<Client> clients = new ArrayList<>();
    private String ID;
    private String Password;

    public User() {

    }

    public User(String ID, Client client) {
        this.ID = ID;
        if(client != null)
        {
            clients.add(client);
        }
    }
    public User(String ID, String password)
    {
        this.ID = ID;
        this.Password = password;
    }
    public String getID() {
        return ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void putClient(Client client) throws AlreadyConnectedException {
        //To avoid repeated login
        for(var existClient : clients)
        {
            if(existClient.getClientAddressInfo().equals(client.getClientAddressInfo()))
            {
                throw new AlreadyConnectedException();
            }
        }
        clients.add(client);
        Main.mainServer.logger.info("A new client added to User: "+ID);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
}
