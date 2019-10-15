package com.example.socket.dao;

import com.example.socket.entity.AccessLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 *
 * @author Gimi
 * @date 2019/10/15 20:30
 *
 ***/
@Mapper
public interface LogMapper {

    void insert(@Param("log") AccessLog log);

    List<AccessLog> list();
}
