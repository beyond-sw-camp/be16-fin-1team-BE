package com.Dolmeng_E.workspace.common.service;

import com.Dolmeng_E.workspace.common.dto.UserIdListDto;
import com.Dolmeng_E.workspace.common.dto.UserInfoListResDto;
import com.Dolmeng_E.workspace.common.dto.UserInfoResDto;
import com.example.modulecommon.dto.CommonSuccessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "chat-service")
public interface ChatFeign {
    @GetMapping("/chat/room/{roomId}/unread-messages")
    ResponseEntity<CommonSuccessDto> getUnreadMessageListByRoom (@PathVariable("roomId") Long roomId, @RequestHeader("X-User-Id")String userId);

}
