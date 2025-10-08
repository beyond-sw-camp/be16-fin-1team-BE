package com.Dolmeng_E.chat.domain.entity;

import com.example.modulecommon.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String workspaceId;

    @Column(nullable = false)
    private String projectId;

    @Column(nullable = false)
    private String stoneId;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private String isDelete = "N";
}
