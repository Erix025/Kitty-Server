package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;
import index.kitty.server.Models.Client;

public class LoginData {
    private JSONObject json;
    private String userID;
    private String password;
    private String clientType;
    private Client loginClient;
    public final static String Head = "LoginData";

    public LoginData(Data data, Client client) // Receive the LoginData
    {
        json = data.getJson();
        userID = json.getString("UserID");
        password = json.getString("Password");
        clientType = json.getString("clientType");
        loginClient = client;
        loginClient.setClientType(clientType);
    }

    public LoginData(String userID, String password, String clientType)// Create the LoginData
    {
        this.userID = userID;
        this.password = password;
        this.clientType = clientType;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("UserID", userID);
        json.put("Password", password);
        json.put("ClientType", clientType);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getClientType() {
        return clientType;
    }

    public String getPassword() {
        return password;
    }

    public String getUserID() {
        return userID;
    }

    public Client getLoginClient() {
        return loginClient;
    }
}
