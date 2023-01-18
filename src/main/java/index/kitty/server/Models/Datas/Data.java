package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class Data {
    private JSONObject json;

    public String getHead() {
        return json.getString("Head");
    }

    public JSONObject getJson() {
        return json;
    }

    public Data(String string) {
        json = JSON.parseObject(string);
    }

    public Data(byte[] bytes) {
        json = JSON.parseObject(bytes);
    }
}
