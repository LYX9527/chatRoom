package com.orange.chatroom.encoder;

import com.alibaba.fastjson.JSONObject;
import com.orange.chatroom.domain.Message;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

/**
 * @author : yilantingfeng
 * @version : v1.0
 * @projectName : chatroom
 * @package : com.orange.chatroom.encoder
 * @className : ServerEncoder
 * @description:
 * @date : 2023/1/11 13:22
 */
public class ServerEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message object) throws EncodeException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("host", object.getHost());
        jsonObject.put("message", object.getMessage());
        jsonObject.put("isOwner", object.getIsOwner());
        return jsonObject.toJSONString();
    }
}
