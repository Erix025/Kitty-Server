package index.kitty.server.Models.Datas;

import com.alibaba.fastjson2.JSONObject;
import index.kitty.server.Models.User;

import java.util.Date;

public class Message {
    private final JSONObject json;
    private String receiveUserID;
    private String sendUserID;
    private String content;
    private Date sendTime;
    public final static String Head = "Message";

    public Message(Data data) {
        json = data.getJson();
        content = json.getString("Content");
        sendTime = json.getDate("SendTime");
        sendUserID = json.getString("SendUserID");
        receiveUserID = json.getString("ReceiveUserID");
    }

    public Message(String content, Date sendTime, String sendUserID, String receiveUserID) {
        this.content = content;
        this.sendTime = sendTime;
        this.sendUserID = sendUserID;
        this.receiveUserID = receiveUserID;
        json = new JSONObject();
        json.put("Head", Head);
        json.put("Content", content);
        json.put("SendTime", sendTime);
        json.put("SendUserID", sendUserID);
        json.put("ReceiveUserID", receiveUserID);
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    public String getReceiveUserID() {
        return receiveUserID;
    }

    public String getSendUserID() {
        return sendUserID;
    }

    public JSONObject getJson() {
        return json;
    }
}
