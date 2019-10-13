package com.example.socket.controller;

import com.example.socket.entity.ChatRecord;
import com.example.socket.entity.ChatRecordVO;
import com.example.socket.entity.PageInfo;
import com.example.socket.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/***
 *
 * @author Gimi
 * @date 2019/10/13 17:13
 *
 ***/
@RestController
public class WebSocketController {

    @Autowired
    MessageWebSocket webSocket;

    @Autowired
    ChatRecordService chatRecordService;

    @GetMapping("list")
    public PageInfo<ChatRecordVO> list(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                       @RequestParam(value = "uid",defaultValue = "9527")String uid){
        return chatRecordService.list(pageNo,pageSize,uid);
    }

    @GetMapping("sendMsg")
    public String sendMsg(String msg,String uid){
        try {
            webSocket.sendMessage(msg,uid);
            chatRecordService.save("系统",msg,"127.0.0.1",uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
