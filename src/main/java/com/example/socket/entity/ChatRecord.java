package com.example.socket.entity;

import lombok.Data;

import java.util.Date;

/***
 *
 * @author Gimi
 * @date 2019/10/13 19:12
 *
 ***/
@Data
public class ChatRecord {
    private Integer id;
    private String uid;
    private String sendUser;
    private String sendMsg;
    private String ipAddr;
    private String city;
    private Date createTime;

}
