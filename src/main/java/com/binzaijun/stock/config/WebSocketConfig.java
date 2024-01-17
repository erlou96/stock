package com.binzaijun.stock.config;

import com.binzaijun.stock.handle.MyWebSocketHandler;
import com.binzaijun.stock.handle.StockInfoSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/api/my-websocket-endpoint")
                .setAllowedOrigins("*");
        registry.addHandler(new StockInfoSocketHandler(), "/api/stock-realtime")
                .setAllowedOrigins("*");
    }
}