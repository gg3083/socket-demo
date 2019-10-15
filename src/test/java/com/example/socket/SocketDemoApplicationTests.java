package com.example.socket;

import com.example.socket.utils.BaiduPushUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocketDemoApplicationTests {

    @Test
    public void contextLoads() {
        String ip = "119.123.134.77";
        String address = BaiduPushUtil.getAddress(ip);
        System.err.println(address);
    }

}
