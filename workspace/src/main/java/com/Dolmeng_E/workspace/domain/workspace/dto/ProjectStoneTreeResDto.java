package com.Dolmeng_E.workspace.domain.workspace.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProjectStoneTreeResDto {
    private String projectId;
    private String projectName;
    private List<StoneMilestoneTreeDto> stones;
}