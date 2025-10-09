package com.Dolmeng_E.chat.domain.controller;

import com.Dolmeng_E.chat.domain.dto.ChatCreateReqDto;
import com.Dolmeng_E.chat.domain.service.ChatService;
import com.example.modulecommon.dto.CommonSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatCreateReqDto dto) {
        chatService.createChatRoom(dto);
        return new ResponseEntity<>(new CommonSuccessDto(dto.getRoomName(), HttpStatus.OK.value(), "채팅방 생성 성공"), HttpStatus.OK);
    }

}
