package index.kitty.server.Methods;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import index.kitty.server.Main;
import index.kitty.server.Models.Client;
import index.kitty.server.Models.Datas.*;
import index.kitty.server.Models.User;

import java.io.IOException;
import java.nio.channels.AlreadyConnectedException;
import java.util.NoSuchElementException;

public class DataFactory {
    private static void DataAnalysis(Data source) {
        switch (source.getHead()) {
            case Message.Head:
                Main.mainServer.putTask(new SendMessage(new Message(source)));
                break;
            case LoginReturnData.Head:
                break;
            case RegisterReturnData.Head:
                break;
        }
    }

    public static void DataAnalysis(Data source, Client client) {
        DataAnalysis(source);
        switch (source.getHead()) {
            case LoginData.Head:
                Main.mainServer.putTask(new UserLogin(new LoginData(source, client)));
                break;
            case RegisterData.Head:
                Main.mainServer.putTask(new UserRegister(new RegisterData(source, client)));
                break;
        }
    }
}

class SendMessage implements Runnable {
    private Message message;

    SendMessage(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        User object;
        try {
            /*
            //get receiver
            object = Main.mainServer.getUser(message.getReceiveUserID());
            for(Client client : object.getClients())
            {
                try {
                    client.getOutStream().writeUTF(JSON.toJSONString(message.getJson()));
                }
                catch(IOException e)
                {
                    //TODO: log ERR
                }
            }*/
            System.out.println(JSON.toJSONString(message.getJson()));
            System.out.println(message.getContent());
            System.out.println(message.getSendTime());
            System.out.println(message.getSendUserID());
            System.out.println(message.getReceiveUserID());
        } catch (NoSuchElementException e) {
            //TODO: Cache Message
        }

    }
}

class UserLogin implements Runnable {
    private LoginData loginData;

    UserLogin(LoginData loginData) {
        this.loginData = loginData;
    }

    @Override
    public void run() {
        //check the username and password
        var userList = Main.mainServer.getDataBase().UserDataBase;
        String loginInformation = "";
        boolean loginValid = false;
        for (User user : userList) {
            if (user.getID().equals(loginData.getUserID()) && user.getPassword().equals(loginData.getPassword())) {
                loginValid = true;
                break;
            }
        }
        //login or not login
        if (loginValid) {
            //TODO login log
            //Add the user in AliveUsers
            try {
                //if the user has already been alive
                User user = Main.mainServer.getAliveUser(loginData.getUserID());
                try {
                    user.putClient(loginData.getLoginClient());
                    loginInformation = "Login successfully";
                } catch (AlreadyConnectedException e) {
                    loginValid = false;
                    loginInformation = "This client has already logged in";
                }
            } catch (NoSuchElementException e) {
                //if the user has not been alive
                Main.mainServer.putAliveUser(new User(loginData.getUserID(),loginData.getLoginClient()));
                loginInformation = "Login successfully";
            }
        } else {
            loginInformation = "The user isn't existed or the password is not correct";
        }
        //send return data
        var returnData = new LoginReturnData(loginValid, loginInformation);
        loginData.getLoginClient().putData(JSON.toJSONString(returnData.getJson()));
    }
}

class UserRegister implements Runnable {
    private RegisterData registerData;

    UserRegister(RegisterData registerData) {
        this.registerData = registerData;
    }

    @Override
    public void run() {
        var registeredUsers = Main.mainServer.getDataBase().UserDataBase;
        boolean registerValid = true;
        String registerReturnInformation = "";
        for (User user : registeredUsers) {
            if (registerData.getUserID().equals(user.getID())) {
                registerValid = false;
                registerReturnInformation = "The user has already existed";
                break;
            }
        }
        if (registerValid) {
            registerReturnInformation = "Register Successfully";
            registeredUsers.add(new User(registerData.getUserID(), registerData.getPassword()));
        }
        System.out.println(registerReturnInformation);
        RegisterReturnData returnData = new RegisterReturnData(registerValid, registerReturnInformation);
        registerData.getRegisterClient().putData(JSON.toJSONString(returnData.getJson()));
    }
}