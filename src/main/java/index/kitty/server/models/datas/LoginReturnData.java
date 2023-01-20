package index.kitty.server.models.datas;

import com.alibaba.fastjson2.JSONObject;

public class LoginReturnData {
    private final JSONObject json;
    private final boolean isLoginValid;
    private final String information;
    public final static String Head = "LoginReturnData";

    public LoginReturnData(Data data) {
        json = data.getJson();
        isLoginValid = json.getBoolean("Valid");
        information = json.getString("Information");
    }

    public LoginReturnData(boolean isLoginValid, String information) {
        this.isLoginValid = isLoginValid;
        this.information = information;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("Valid", isLoginValid);
        json.put("Information", information);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getInformation() {
        return information;
    }

    public boolean isLoginValid() {
        return isLoginValid;
    }
}