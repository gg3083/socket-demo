package com.example.socket.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/***
 *
 * @author Gimi
 * @date 2019/10/15 20:28
 *
 ***/
@Data
public class AccessLog {
    private Integer id;
    private String ipAddr;
    private String city;
    private String content;
    private Date createTime;
}
