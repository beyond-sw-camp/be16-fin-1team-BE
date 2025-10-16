package com.Dolmeng_E.workspace.domain.chatbot.controller;

import com.Dolmeng_E.workspace.domain.chatbot.dto.ChatbotMessageUserReqDto;
import com.Dolmeng_E.workspace.domain.chatbot.service.ChatbotMessageService;
import com.example.modulecommon.dto.CommonSuccessDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotMessageController {
    private final ChatbotMessageService chatbotMessageService;

    // 사용자가 챗봇에게 메시지 전송
    @PostMapping("/user")
    public ResponseEntity<?> sendMessage(@RequestHeader("X-User-Id") String userId, @RequestBody @Valid ChatbotMessageUserReqDto chatbotMessageUserReqDto) {
        String response = chatbotMessageService.sendMessage(userId, chatbotMessageUserReqDto);
        return new ResponseEntity<>(new CommonSuccessDto(response, HttpStatus.OK.value(), "챗봇에게 메시지 전송 성공"),  HttpStatus.OK);
    }
}
