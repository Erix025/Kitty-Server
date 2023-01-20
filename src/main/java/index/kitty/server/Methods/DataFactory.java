package index.kitty.server.Methods;

import com.alibaba.fastjson2.JSON;
import index.kitty.server.Main;
import index.kitty.server.Models.Client;
import index.kitty.server.Models.Datas.*;
import index.kitty.server.Models.User;

import java.nio.channels.AlreadyConnectedException;
import java.util.NoSuchElementException;

public class DataFactory {
    private static void DataAnalysis(Data source) {
        switch (source.getHead()) {
            case LoginReturnData.Head:
                break;
            case RegisterReturnData.Head:
                break;
        }
    }

    public static void DataAnalysis(Data source, Client client) {
        DataAnalysis(source);
        switch (source.getHead()) {
            case Message.Head:
                Main.mainServer.putTask(new SendMessage(new Message(source), client));
                break;
            case LoginData.Head:
                Main.mainServer.putTask(new UserLogin(new LoginData(source), client));
                break;
            case RegisterData.Head:
                Main.mainServer.putTask(new UserRegister(new RegisterData(source), client));
                break;
            case LogoutData.Head:
                Main.mainServer.putTask(new UserLogout(new LogoutData(source), client));
        }
    }
}

class SendMessage implements Runnable {
    private Message message;
    private Client client;

    SendMessage(Message message, Client client) {
        this.message = message;
        this.client = client;
    }

    @Override
    public void run() {
        User object;
        MessageReturn returnData;
        try {
            // get receiver
            object = Main.mainServer.getAliveUser(message.getReceiveUserID());
            for (Client client : object.getClients()) {
                client.putData(JSON.toJSONString(message.getJson()));
            }
            returnData = new MessageReturn(true, "发送成功");
        } catch (NoSuchElementException e) {
            returnData = new MessageReturn(false, "发送失败，用户未登录或不存在");
            // TODO: Cache Message
        }
        client.putData(JSON.toJSONString(returnData.getJson()));
    }
}

class UserLogin implements Runnable {
    private LoginData loginData;
    private Client client;

    UserLogin(LoginData loginData, Client client) {
        this.loginData = loginData;
        this.client = client;
    }

    @Override
    public void run() {
        // check the username and password
        var userList = Main.mainServer.getDataBase().UserDataBase;
        String loginInformation = "";
        boolean loginValid = false;
        for (User user : userList) {
            if (user.getID().equals(loginData.getUserID()) && user.getPassword().equals(loginData.getPassword())) {
                loginValid = true;
                break;
            }
        }
        // login or not login
        if (loginValid) {
            // TODO login log
            client.setClientType(loginData.getClientType());
            // Add the user in AliveUsers
            try {
                // if the user has already been alive
                User user = Main.mainServer.getAliveUser(loginData.getUserID());
                try {
                    user.putClient(client);
                    loginInformation = "登陆成功";
                } catch (AlreadyConnectedException e) {
                    loginValid = false;
                    loginInformation = "该客户端已登录，请勿重复登录";
                }
            } catch (NoSuchElementException e) {
                // if the user has not been alive
                Main.mainServer.putAliveUser(new User(loginData.getUserID(), client));
                loginInformation = "登陆成功";
            }
        } else {
            loginInformation = "用户名或密码错误";
        }
        // send return data
        var returnData = new LoginReturnData(loginValid, loginInformation);
        client.putData(JSON.toJSONString(returnData.getJson()));
    }
}
class UserLogout implements  Runnable{
    private LogoutData logoutData;
    private Client client;
    UserLogout(LogoutData logoutData, Client client)
    {
        this.logoutData = logoutData;
        this.client = client;
    }
    @Override
    public void run() {
        Main.mainServer.logout(client, logoutData.getUserID());
        //TODO log
        System.out.println("User Logout");
        client.putData(JSON.toJSONString((new LogoutReturnData(true, "注销成功")).getJson()));
    }
}
class UserRegister implements Runnable {
    private RegisterData registerData;
    private Client client;

    UserRegister(RegisterData registerData, Client client) {
        this.registerData = registerData;
        this.client = client;
    }

    @Override
    public void run() {
        var registeredUsers = Main.mainServer.getDataBase().UserDataBase;
        boolean registerValid = true;
        String registerReturnInformation = "";
        for (User user : registeredUsers) {
            if (registerData.getUserID().equals(user.getID())) {
                registerValid = false;
                registerReturnInformation = "用户已存在";
                break;
            }
        }
        if (registerValid) {
            registerReturnInformation = "注册成功";
            registeredUsers.add(new User(registerData.getUserID(), registerData.getPassword()));
        }
        System.out.println(registerReturnInformation);
        RegisterReturnData returnData = new RegisterReturnData(registerValid, registerReturnInformation);
        client.putData(JSON.toJSONString(returnData.getJson()));
    }
}