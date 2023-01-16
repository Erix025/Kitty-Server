package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;
import index.kitty.server.Models.Client;

public class RegisterData {
    private JSONObject json;
    private String userID;
    private String password;
    private Client registerClient;

    public final static String Head = "RegisterData";

    public RegisterData(Data data, Client client) {
        json = data.getJson();
        userID = json.getString("UserID");
        password = json.getString("Password");
        registerClient = client;
    }
    public RegisterData(String userID, String password, String clientType){
        this.userID =userID;
        this.password = password;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("UserID", userID);
        json.put("Password", password);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getPassword() {
        return password;
    }

    public String getUserID() {
        return userID;
    }

    public Client getRegisterClient() {
        return registerClient;
    }
}
