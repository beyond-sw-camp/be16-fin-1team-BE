package com.Dolmeng_E.user.domain.notification.controller;

import com.Dolmeng_E.user.domain.notification.dto.NotificationCreateReqDto;
import com.Dolmeng_E.user.domain.notification.service.NotificationService;
import com.example.modulecommon.dto.CommonSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final SimpMessageSendingOperations messageTemplate;

    // 알림 생성
    @PostMapping("/new-noti")
    public ResponseEntity<?> createNotification(@RequestBody NotificationCreateReqDto dto) {
        notificationService.createNotification(dto);
        return new ResponseEntity<>(new CommonSuccessDto("CREATED", HttpStatus.CREATED.value(), "알림 생성 성공"), HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        messageTemplate.convertAndSend("/topic/notification/59c720ae-8c62-41d0-abcb-a247113ba2e9", "test");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
