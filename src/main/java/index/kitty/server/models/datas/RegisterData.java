package index.kitty.server.models.datas;

import com.alibaba.fastjson2.JSONObject;

public class RegisterData {
    private final JSONObject json;
    private final String userID;
    private final String password;

    public final static String Head = "RegisterData";

    public RegisterData(Data data) {
        json = data.getJson();
        userID = json.getString("UserID");
        password = json.getString("Password");
    }

    public RegisterData(String userID, String password, String clientType) {
        this.userID = userID;
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

}
