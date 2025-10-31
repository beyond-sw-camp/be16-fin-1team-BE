package com.Dolmeng_E.workspace.domain.workspace.entity;

import com.example.modulecommon.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "workspace_invite")
public class WorkspaceInvite extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String email;  // 초대 이메일

    @Column(nullable = false)
    private String inviteToken;  // 초대코드(UUID 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    private WorkspaceParticipant inviter;

    private LocalDateTime expiredAt;   // 만료 시간
    private boolean isUsed;            // 사용 여부
}

