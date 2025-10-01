package com.Dolmeng_E.workspace.domain.workspace.entity;

import com.Dolmeng_E.workspace.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Workspace extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkspaceTemplates workspaceTemplates;

    @NotNull
    @Column(length = 20)
    private String workspaceName;

    private Integer subscribe;

    @NotNull
    private Long storage;

//    워크스페이스 생성자 ID
    @NotNull
    private UUID userId;

}
