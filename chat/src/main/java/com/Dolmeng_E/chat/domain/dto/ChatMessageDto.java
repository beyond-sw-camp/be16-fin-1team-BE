package com.Dolmeng_E.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long roomId;
    private String message;
    private String senderId;
    private String senderName;
    private LocalDateTime lastSendTime;
    private String userProfileImageUrl;

    // 메시지 타입이랑
    // nullable한 파일 목록
}
