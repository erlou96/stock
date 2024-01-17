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
public class MyWebSocketHandler extends TextWebSocketHandler {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
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
