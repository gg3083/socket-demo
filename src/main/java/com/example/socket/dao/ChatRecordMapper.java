package com.example.socket.dao;

import com.example.socket.entity.ChatRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 *
 * @author Gimi
 * @date 2019/10/13 19:14
 *
 ***/
@Mapper
public interface ChatRecordMapper {

        List<ChatRecord> list();

        void insert(@Param("record") ChatRecord record);

}
