package com.orange.chatroom.utils;

import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author : yilantingfeng
 * @version : v1.0
 * @projectName : chatroom
 * @package : com.orange.chatroom.utils
 * @className : WebSocketUtil
 * @description:
 * @date : 2023/1/11 8:34
 */
@Slf4j
public class WebSocketUtil {
    public static SocketAddress getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();
        String fieldPath = "base#socketWrapper#socket#sc";
        SocketChannel fieldInstance = (SocketChannel) FieldInstanceUtil.getFieldInstance(async, fieldPath);
        try {
            SocketAddress remoteAddress = fieldInstance.getRemoteAddress();
            return remoteAddress;
        } catch (IOException e) {
            log.warn("getRemoteAddress error{}",
                    e.getMessage());
            return null;
        }
    }
}
