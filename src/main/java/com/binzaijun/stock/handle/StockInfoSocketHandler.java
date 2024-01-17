package com.binzaijun.stock.handle;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StockInfoSocketHandler extends TextWebSocketHandler {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("服务器建立连接 .........");
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        System.out.println("服务器收到消息：" + message.getPayload());
        // 发送消息给客户端
//        session.sendMessage(new TextMessage("Hello World"));
        executorService.scheduleAtFixedRate(() -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage("Server message: " + System.currentTimeMillis()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS); // 每 3 秒向客户端推送一次数据
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        executorService.shutdown();
    }
}
