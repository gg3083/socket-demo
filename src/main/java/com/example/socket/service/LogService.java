package com.example.socket.service;

import com.example.socket.dao.LogMapper;
import com.example.socket.entity.AccessLog;
import com.example.socket.entity.ChatRecord;
import com.example.socket.entity.ChatRecordVO;
import com.example.socket.entity.PageInfo;
import com.example.socket.utils.BaiduPushUtil;
import com.example.socket.utils.IpAddrUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 *
 * @author Gimi
 * @date 2019/10/15 20:29
 *
 ***/
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    public void save(String ip,String page){
        String address = IpAddrUtil.getAddressByIp(ip);
        AccessLog log = new AccessLog();
        log.setIpAddr(ip);
        log.setCity(address);
        log.setContent(page);
        logMapper.insert(log);
    }

    public PageInfo<AccessLog> list(int pageNo, int pageSize){
        PageHelper.startPage(pageNo,pageSize);
        List<AccessLog> records = logMapper.list();
        return new PageInfo<>(new com.github.pagehelper.PageInfo(records));
    }


}
