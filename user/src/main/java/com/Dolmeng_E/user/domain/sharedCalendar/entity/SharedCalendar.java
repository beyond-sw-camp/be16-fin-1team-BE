package com.Dolmeng_E.user.domain.sharedCalendar.entity;

import com.Dolmeng_E.user.domain.user.entity.User;
import com.example.modulecommon.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.Dolmeng_E.user.domain.sharedCalendar.entity.CalendarType.SCHEDULE;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCalendar extends BaseTimeEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // TODO 수정 필수
    @Column(length = 255, nullable = false)
    private String workspaceId;

    @Column(name = "calendar_type", nullable = false)
    private CalendarType calendarType = SCHEDULE;

    @Column(name = "calendar_name", length = 20, nullable = false)
    private String calendarName;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    // todo 즐겨찾기 여부
    @Column(nullable = false)
    private Boolean bookmark = false;

    // 일정 공유 여부
    @Column(name = "is_shared", nullable = false)
    private Boolean isShared = false;


}
