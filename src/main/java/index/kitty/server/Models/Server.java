package index.kitty.server.Models;

import index.kitty.server.Threads.ListenThread;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //initialize socket and input stream
    public ArrayList<Client> Clients = new ArrayList<Client>();
    public ServerSocket serverSocket = null;
    //listening Thread
    private ListenThread listenThread;
    //Tasks thread pool
    private ExecutorService tasksThreadPool;
    private DataBase dataBase;
    private ArrayList<User> aliveUsers = new ArrayList<>();

    // constructor with port
    public Server(int port) {
        // starts server and starts listening thread
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            //Initialize Database
            dataBase = new DataBase();
            //create tasks thread pool
            tasksThreadPool = Executors.newFixedThreadPool(5);
            //create listening thread
            listenThread = new ListenThread(this);
            listenThread.start();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void Close() {
        // close Threads
        listenThread.interrupt();
        // close connection
        try {
            for (Client client : Clients) {
                client.disconnect();
            }
            serverSocket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
        //save database
        dataBase.SaveData();
    }

    public void putTask(Runnable task) {
        tasksThreadPool.submit(task);
    }

    public User getAliveUser(String ID) throws NoSuchElementException {
        for (User user : aliveUsers) {
            if (Objects.equals(user.getID(), ID)) {
                return user;
            }
        }
        throw new NoSuchElementException("Cannot find User with such ID");
    }

    public void putAliveUser(User user) {
        aliveUsers.add(user);
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public void removeClient(Client client) {
        for (User user : aliveUsers) {
            if (user.getClients().contains(client)) {
                if (user.getClients().size() == 1) {
                    aliveUsers.remove(user);
                } else {
                    user.getClients().remove(client);
                }
                break;
            }
        }
        Clients.remove(client);
    }
    public boolean isClientConnected(Socket socket) {
        for (Client client : Clients)
        {
            if(client.getClientAddressInfo().equals(socket.getInetAddress().getHostAddress()))
            {
                return true;
            }
        }
        return false;
    }
}