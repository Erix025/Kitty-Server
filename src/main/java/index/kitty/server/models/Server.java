package index.kitty.server.models;

import index.kitty.server.threads.ListenThread;

import java.io.*;
import java.util.logging.*;
import java.net.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // initialize socket and input stream
    public final ArrayList<Client> Clients = new ArrayList<>();
    public ServerSocket serverSocket = null;
    // listening Thread
    private ListenThread listenThread;
    // Tasks thread pool
    private ExecutorService tasksThreadPool;
    private DataBase dataBase;
    private final ArrayList<User> aliveUsers = new ArrayList<>();
    public Logger logger;

    // constructor with port
    public Server(int port) {
        // start logger
        logger = Logger.getLogger("MainLogger");
        Handler handler = null;
        try {
            handler = new FileHandler("server.log", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (handler != null)
            logger.addHandler(handler);
        // starts server and starts listening thread
        try {
            // create server
            serverSocket = new ServerSocket(port);
            logger.info("Server started");
            // Initialize Database
            dataBase = new DataBase();
            // create tasks thread pool
            tasksThreadPool = Executors.newFixedThreadPool(5);
            // create listening thread
            listenThread = new ListenThread(this);
            listenThread.start();
        } catch (IOException i) {
            logger.severe("IOException: Cannot create the server connection");
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
            logger.severe("IOException: Cannot close the server connection");
        }
        // save database
        dataBase.SaveData();
    }

    public void putTask(Runnable task) {
        logger.fine("Submitted new task:" + task.getClass().getName());
        tasksThreadPool.submit(task);
    }

    public User getAliveUser(String ID) throws NoSuchElementException {
        for (User user : aliveUsers) {
            if (Objects.equals(user.getID(), ID)) {
                return user;
            }
        }
        logger.info("Cannot find User with such ID");
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
        logger.info("Remove alive Client: " + client.getClientAddressInfo());
    }

    public boolean isClientConnected(Socket socket) {
        for (Client client : Clients) {
            if (client.getClientAddressInfo().equals(socket.getInetAddress().getHostAddress())) {
                return true;
            }
        }
        return false;
    }

    public void logout(Client client, String UserID) {
        for (var user : aliveUsers) {
            if (user.getID().equals(UserID)) {
                user.getClients().remove(client);
                break;
            }
        }
        logger.info("User logout: " + UserID);
    }
}