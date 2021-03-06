package com.orchowski.seminariumreactivechat.chatmessage;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketSession;

@Data
@RequiredArgsConstructor
class Connection {
    private final String id;
    private final WebSocketSession session;
}
