package com.Dolmeng_E.drive.common.controller;

import com.Dolmeng_E.drive.common.dto.EditorBatchMessageDto;
import com.Dolmeng_E.drive.domain.drive.service.DocumentLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EditorController {

    // Redis에 메시지를 발행하기 위한 RedisTemplate
    private final RedisTemplate<String, Object> redisTemplate;
    private final DocumentLineService documentLineService;

    @MessageMapping("/editor/cursor")
    public void handleCursorUpdate(@Payload EditorBatchMessageDto message) {
        // 커서 위치는 DB에 저장하지 않고 바로 브로드캐스트
        redisTemplate.convertAndSend("document-updates", message);
    }

    @MessageMapping("/editor/batch-update")
    public void handleBatchUpdate(@Payload EditorBatchMessageDto message) {
        documentLineService.batchUpdateDocumentLine(message);
        // 3. (다음 단계) 웹소켓을 통해 다른 사용자들에게 이 블록 생성 정보를 브로드캐스트합니다.
        redisTemplate.convertAndSend("document-updates", message);
    }
}