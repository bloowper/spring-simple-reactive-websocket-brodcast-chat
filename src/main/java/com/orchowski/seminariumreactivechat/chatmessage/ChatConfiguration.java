package com.orchowski.seminariumreactivechat.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
@Controller
@RequiredArgsConstructor
class ChatConfiguration {

    private final ChatWebSocketHandler webSocketHandler;

    @Bean
    HandlerMapping chatHandlerMapping() {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(2);
        handlerMapping.setUrlMap(Map.of(
                "/chat", webSocketHandler
        ));
        return handlerMapping;
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
