package com.Dolmeng_E.workspace.domain.project.dto;

import com.Dolmeng_E.workspace.domain.project.entity.Project;
import com.Dolmeng_E.workspace.domain.project.entity.ProjectStatus;
import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectCreateDto {
    private String workspaceId;
    private String projectName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String projectObjective;
    private String projectDescription;

    public Project toEntity(Workspace workspace) {
        return Project.builder()
                .workspace(workspace)
                .projectName(this.projectName)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .projectObjective(this.projectObjective)
                .projectDescription(this.projectDescription)
                .build();
    }
}
