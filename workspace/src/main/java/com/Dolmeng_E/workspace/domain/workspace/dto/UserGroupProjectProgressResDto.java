package com.Dolmeng_E.workspace.domain.workspace.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGroupProjectProgressResDto {

    private String groupName;

    private int projectCount;

    private double averageProgress;
}
