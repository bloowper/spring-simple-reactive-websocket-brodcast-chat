package com.orchowski.seminariumreactivechat.chatmessage;

import com.orchowski.seminariumreactivechat.chatmessage.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
final class MessageProcessor {
    // private final Sinks.Many<ChatMessageDto> sink = Sinks.many()
    //         .replay()
    //         .limit(Duration.of(20, ChronoUnit.SECONDS)); //Dodatkowa retencja wiadomości
    private final Sinks.Many<ChatMessageDto> sink = Sinks.many()
            .multicast().directBestEffort();

    void publish(ChatMessageDto chatMessageDto) {
        sink.emitNext(chatMessageDto, (signalType, emitResult) -> {
            log.warn("Message publication failed type [{}] result [{}]", signalType, emitResult);
            return false;// We never want to retray publication of message
        });
    }

    Flux<ChatMessageDto> getFlux() {
        return sink.asFlux();
    }
}