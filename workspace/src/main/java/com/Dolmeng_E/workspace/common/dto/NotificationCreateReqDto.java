package com.Dolmeng_E.workspace.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationCreateReqDto {
    private String title; // 알림 제목
    private String content; // 알림 내용
    private String type; // 알림 타입
    private List<UUID> userIdList = new ArrayList<>(); // 알림 받은 유저 아이디
    private LocalDateTime sendAt; // 예약알림이라면 알림이 뜰 날짜/시간 (즉시알림은 Null)
}