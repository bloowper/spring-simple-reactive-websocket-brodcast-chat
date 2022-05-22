package com.orchowski.seminariumreactivechat.chatmessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

@Configuration
class ChatConfiguration {

    @Bean
    HandlerMapping chatHandlerMapping() {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(2);
        handlerMapping.setUrlMap(Map.of(
                "/ws/chat", ChatWebSocketHandler.class
        ));
        return handlerMapping;
    }
}
