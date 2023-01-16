package com.orange.chatroom.service;

import com.orange.chatroom.domain.IPInfo;
import com.orange.chatroom.domain.Message;
import com.orange.chatroom.encoder.ServerEncoder;
import com.orange.chatroom.utils.WebSocketUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : yilantingfeng
 * @version : v1.0
 * @projectName : chatroom
 * @package : com.orange.chatroom.service
 * @className : ChatRoomWebSocket
 * @description:
 * @date : 2023/1/10 13:30
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chatroom",encoders = {ServerEncoder.class})
public class ChatRoomWebSocket {
    private static final String TAG = "ChatRoomWebSocket：";
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private static CopyOnWriteArraySet<ChatRoomWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    private static final String HEARTBEAT = "heartbeat";

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info(TAG + "有新连接加入！当前在线人数为" + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info(TAG + "有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error(TAG + "发生错误");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(TAG + "来自客户端的消息:" + message);
        if (HEARTBEAT.equals(message)) {
            return;
        }
        IPInfo newMessageInfo = getHostInfo(session);
        String host = newMessageInfo.getHost();
        // 群发消息
        for (ChatRoomWebSocket item : webSocketSet) {
            try {
                IPInfo hostInfo = getHostInfo(item.session);
                Message sendMessage = new Message();
                sendMessage.setHost(host);
                sendMessage.setMessage(message);
                sendMessage.setIsOwner(host.equals(hostInfo.getHost()));
                item.sendObject(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static IPInfo getHostInfo(Session session) {
        SocketAddress remoteAddress = WebSocketUtil.getRemoteAddress(session);
        String hostAndPort = remoteAddress.toString().replace("/", "");
        String[] split = hostAndPort.split(":");
        String host = split[0];
        String port = split[1];
        IPInfo ipInfo = new IPInfo();
        ipInfo.setHost(host);
        ipInfo.setPort(port);
        return ipInfo;
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendObject(Object message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getOnlineCount() {
        return onlineCount.get();
    }

    private void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    private void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

}
