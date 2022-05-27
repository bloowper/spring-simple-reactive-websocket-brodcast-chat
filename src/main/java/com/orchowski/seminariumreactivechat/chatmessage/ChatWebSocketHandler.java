package com.orchowski.seminariumreactivechat.chatmessage;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchowski.seminariumreactivechat.chatmessage.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
class ChatWebSocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;
    private final MessageProcessor messageProcessor;

    private final Map<String, Connection> sessions = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var sessionId = session.getId();

        var inputChanel = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(payload -> log.info("Session [{}] recived message  [{}]",sessionId, payload))
                .map(this::stringToChatMessage)
                .doOnError(JacksonException.class, e -> log.error("Session [{}] message unmarshalling failed", sessionId))
                .doOnNext(messageProcessor::publish)
                .doFinally(signalType -> {
                    if (signalType.equals(SignalType.ON_COMPLETE)) {
                        sessions.remove(sessionId);
                    }
                });

        var outputChanel = messageProcessor
                .getFlux()
                .map(this::chatMessageTostring)
                .map(session::textMessage);

        return session.send(outputChanel).and(inputChanel);
    }

    @SneakyThrows
    private ChatMessageDto stringToChatMessage(String json) {
        return this.objectMapper.readValue(json, ChatMessageDto.class);
    }

    @SneakyThrows
    private String chatMessageTostring(ChatMessageDto chatMessageDto) {
        return this.objectMapper.writeValueAsString(chatMessageDto);
    }
}
