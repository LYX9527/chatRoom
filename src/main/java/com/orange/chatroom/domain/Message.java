package com.orange.chatroom.domain;

import lombok.Data;

/**
 * @author : yilantingfeng
 * @version : v1.0
 * @projectName : chatroom
 * @package : com.orange.chatroom.domain
 * @className : Message
 * @description:
 * @date : 2023/1/11 13:20
 */
@Data
public class Message {
    private String host;
    private String message;
    private Boolean isOwner;
}
