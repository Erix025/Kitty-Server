package index.kitty.server.models.datas;

import com.alibaba.fastjson2.JSONObject;

public class LogoutReturnData {
    private final JSONObject json;
    private final boolean isLogoutValid;
    private final String information;
    public final static String Head = "LogoutReturnData";

    public LogoutReturnData(Data data) {
        json = data.getJson();
        isLogoutValid = json.getBoolean("Valid");
        information = json.getString("Information");
    }

    public LogoutReturnData(boolean isLogoutValid, String information) {
        this.isLogoutValid = isLogoutValid;
        this.information = information;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("Valid", isLogoutValid);
        json.put("Information", information);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getInformation() {
        return information;
    }

    public boolean isLogoutValid() {
        return isLogoutValid;
    }
}