package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;

public class LoginData {
    private JSONObject json;
    private String userID;
    private String password;
    private String clientType;
    public final static String Head = "LoginData";

    public LoginData(Data data) // Receive the LoginData
    {
        json = data.getJson();
        userID = json.getString("UserID");
        password = json.getString("Password");
        clientType = json.getString("clientType");
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
}
