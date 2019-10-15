package com.example.socket.controller;

import com.example.socket.entity.AccessLog;
import com.example.socket.entity.ChatRecord;
import com.example.socket.entity.ChatRecordVO;
import com.example.socket.entity.PageInfo;
import com.example.socket.service.ChatRecordService;
import com.example.socket.service.LogService;
import com.example.socket.utils.IpAddrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    LogService logService;

    @GetMapping("list")
    public PageInfo<ChatRecordVO> list(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                       @RequestParam(value = "uid",defaultValue = "9527")String uid, HttpServletRequest request){
        logService.save(IpAddrUtil.getRealIp(request),"首页");
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

    @GetMapping("/log/list")
    public PageInfo<AccessLog> logList(@RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize, HttpServletRequest request){
        logService.save(IpAddrUtil.getRealIp(request),"查看日志");
        return logService.list(pageNo,pageSize);
    }
}
