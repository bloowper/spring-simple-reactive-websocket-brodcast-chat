package com.orchowski.seminariumreactivechat.chatmessage;

import com.orchowski.seminariumreactivechat.chatmessage.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
final class MessageProcessor {
    private final Sinks.Many<ChatMessage> sink = Sinks.many().replay().all();

    void publish(ChatMessage chatMessage) {
        sink.emitNext(chatMessage, (signalType, emitResult) -> {
            log.warn("Message publication failed type [{}] result [{}]", signalType, emitResult);
            return false;// We never want to retray publication of message
        });
    }

    Flux<ChatMessage> getFlux() {
        return sink.asFlux();
    }
}