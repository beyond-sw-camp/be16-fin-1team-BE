package com.Dolmeng_E.chat.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, String message) {
        System.out.println(message);

//        chatService.saveMessage(roomId, dto);

        messageTemplate.convertAndSend("/topic/"+roomId, message);
    }
}
