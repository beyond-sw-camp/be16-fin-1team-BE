package com.Dolmeng_E.chat.domain.dto;

import com.Dolmeng_E.chat.domain.entity.ChatMessage;
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
    private String senderEmail;
    private String senderName;
    private LocalDateTime lastSendTime;
    private String userProfileImageUrl;
}
