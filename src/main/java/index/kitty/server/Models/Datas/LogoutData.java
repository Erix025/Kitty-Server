package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;

public class LogoutData {
    private JSONObject json;
    private String userID;
    private String clientType;
    public final static String Head = "LogoutData";

    public LogoutData(Data data) {
        json = data.getJson();
        userID = json.getString("UserID");
        clientType = json.getString("ClientType");
    }

    public LogoutData(String userID, String clientType) {
        this.userID = userID;
        this.clientType = clientType;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("UserID", userID);
        json.put("ClientType", clientType);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getClientType() {
        return clientType;
    }

    public String getUserID() {
        return userID;
    }
}
