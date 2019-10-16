package com.example.socket.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.socket.service.ChatRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@ServerEndpoint(value = "/messageSocket/{userId}")
@Slf4j
public class MessageWebSocket {


    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * key: userId value: sessionIds
     */
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userSessionMap =  new ConcurrentHashMap<>();

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, MessageWebSocket> websocketMap = new ConcurrentHashMap<>();

    /**
     * key: sessionId value: userId
     */
    private static ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();

    /**
     * 当前连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    private static ChatRecordService chatRecordService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setChatService(ChatRecordService chatRecordService) {
        MessageWebSocket.chatRecordService = chatRecordService;
    }
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.info("连接成功，当前连接用户 {}",userId);
        try {
            this.session = session;
            String sessionId = session.getId();
            //建立userId和sessionId的关系
            if(userSessionMap.containsKey(userId)) {
                userSessionMap.get(userId).add(sessionId);
            }else{
                ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
                queue.add(sessionId);
                userSessionMap.put(userId, queue);
            }
            sessionUserMap.put(sessionId, userId);
            //建立sessionId和websocket引用的关系
            if(!websocketMap.containsKey(sessionId)){
                websocketMap.put(sessionId, this);
                addOnlineCount();           //在线数加1
            }
        }catch (Exception e){
            log.error("连接失败 {}",e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        String sessionId = this.session.getId();
        //移除userId和sessionId的关系
        String userId = sessionUserMap.get(sessionId);
        sessionUserMap.remove(sessionId);
        if( StringUtils.isEmpty(userId)) {
            return;
        }
        ConcurrentLinkedQueue<String> sessionIds = userSessionMap.get(userId);
        if(StringUtils.isEmpty(sessionIds)) {
            return;
        }
        sessionIds.remove(sessionId);
        if (sessionIds.size() == 0) {
            userSessionMap.remove(userId);
        }
        //移除sessionId和websocket的关系
        if (websocketMap.containsKey(sessionId)) {
            websocketMap.remove(sessionId);
            subOnlineCount();           //在线数减1
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param messageStr 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String messageStr, Session session, @PathParam("userId") String userId) throws IOException {
        log.info("当前接收到的消息为 {}" ,messageStr);
        JSONObject object = JSONObject.parseObject(messageStr);
        Object msg = object.get("msg");
        Object user = object.get("user");
        log.info(" {} 当前发送的消息为 {}",user ,msg);
        chatRecordService.save(String.valueOf(user),String.valueOf(msg),getRemoteAddress(session),userId);
    }

    /**
     *
     * @param session
     * @param error 当连接发生错误时的回调
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(error.getMessage());
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message, String toUserId) throws IOException {
        if(StringUtils.isEmpty(message.trim())){
            return;
        }
        userSessionMap.forEach( (key,value) ->{
            log.info("当前需要发送的用户为 {},session为 {}" ,key,value );
            value.forEach( session -> {
                MessageWebSocket socket = websocketMap.get(session);
                try {
                    socket.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("当前发送消息异常 {} ",e.getMessage());
                }
            });
        });
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

     /**
    *获取在线人数
    */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
     /**
    *在线人数加一
    */
    public static synchronized void addOnlineCount() {
        MessageWebSocket.onlineCount++;
        log.info("有人加入，当前在线人数 {}",onlineCount);
    }
    /**
    *在线人数减一
    */
    public static synchronized void subOnlineCount() {
        MessageWebSocket.onlineCount--;
        log.info("有人退出，当前在线人数 {}",onlineCount);
    }

    public static String getRemoteAddress(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();

        //在Tomcat 8.0.x版本有效
//		InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#sos#socketWrapper#socket#sc#remoteAddress");
        //在Tomcat 8.5以上版本有效
        InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async,"base#socketWrapper#socket#sc#remoteAddress");
        log.info("addr: {}",addr);
        log.info("addr-string: {}",addr.toString());
        log.info("addr-address: {}",addr.getAddress());
        log.info("addr-hostname: {}",addr.getAddress().getHostName());
        return addr == null?"127.0.0.1": addr.getAddress().getHostAddress();
    }

    private static Object getFieldInstance(Object obj, String fieldPath) {
        String fields[] = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }

        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field;
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
            }
        }

        return null;
    }
}