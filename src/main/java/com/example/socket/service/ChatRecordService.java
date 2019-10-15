package com.example.socket.service;

import com.example.socket.dao.ChatRecordMapper;
import com.example.socket.entity.ChatRecord;
import com.example.socket.entity.ChatRecordVO;
import com.example.socket.entity.PageInfo;
import com.example.socket.utils.BaiduPushUtil;
import com.example.socket.utils.IpAddrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/***
 *
 * @author Gimi
 * @date 2019/10/13 19:19
 *
 ***/
@Service
public class ChatRecordService {

    @Autowired
    ChatRecordMapper chatRecordMapper;

    public PageInfo<ChatRecordVO> list(int pageNo, int pageSize, String uid){
        PageHelper.startPage(pageNo,pageSize);
        List<ChatRecord> records = chatRecordMapper.list();
        List<ChatRecordVO> recordVOList = new ArrayList<>();
        records.forEach( r ->{
            ChatRecordVO chatRecordVO = new ChatRecordVO();
            BeanUtils.copyProperties( r , chatRecordVO);
            if (Objects.equals(r.getUid(),uid)){
                chatRecordVO.setSelf(true);
            }
            recordVOList.add(chatRecordVO);
        });
        com.github.pagehelper.PageInfo<ChatRecord> pageInfo = new com.github.pagehelper.PageInfo<>(records);

        return new PageInfo<>(pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getTotal(),recordVOList);
    }

    public void save(String sendUser, String msg,String ip,String uid){
        ChatRecord record = new ChatRecord();
        record.setSendUser(sendUser);
        record.setSendMsg(msg);
        record.setIpAddr(ip);
        record.setUid(uid);
        String address = IpAddrUtil.getAddressByIp(ip);
        record.setCity(address);
        chatRecordMapper.insert(record);
    }

}
