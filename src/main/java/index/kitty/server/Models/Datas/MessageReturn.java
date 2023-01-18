package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;

public class MessageReturn {
    private JSONObject json;
    private boolean isMessageValid;
    private String information;
    public final static String Head = "MessageReturn";

    public MessageReturn(Data data) {
        json = data.getJson();
        isMessageValid = json.getBooleanValue("Valid");
        information = json.getString("Information");
    }

    public MessageReturn(boolean isMessageValid, String information) {
        this.isMessageValid = isMessageValid;
        this.information = information;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("Valid", isMessageValid);
        json.put("Information", information);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getInformation() {
        return information;
    }

    public boolean isMessageValid() {
        return isMessageValid;
    }
}
