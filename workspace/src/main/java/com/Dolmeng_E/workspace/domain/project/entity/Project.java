package com.Dolmeng_E.workspace.domain.project.entity;

import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceParticipant;
import com.example.modulecommon.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_generator")
    @GenericGenerator(
            name = "project_seq_generator",
            strategy = "com.Dolmeng_E.workspace.common.domain.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "project_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "valuePrefix", value = "pjt_")
            }
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_participant_id", nullable = false)
    private WorkspaceParticipant workspaceParticipant;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_objective")
    private String projectObjective;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(precision = 3, scale = 1)
    private BigDecimal milestone;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private ProjectStatus projectStatus;

}
