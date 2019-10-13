package com.example.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/***
 *
 * @author Gimi
 * @date 2019/10/13 17:14
 *
 ***/
@Configuration
public class WebSocketConfig {
    @Bean(name="serverEndpointExporter")
    public ServerEndpointExporter getServerEndpointExporterBean(){
        return new ServerEndpointExporter();
    }
}
