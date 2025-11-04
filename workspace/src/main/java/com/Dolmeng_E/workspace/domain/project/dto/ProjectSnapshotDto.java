package com.Dolmeng_E.workspace.domain.project.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectSnapshotDto {
    private ProjectDto project;
    private ProgressDto progress;
    private TeamDto team;
    private ContextDto context;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 프로젝트 정보
    public static class ProjectDto {
        private String name;
        private LocalDateTime startDate;
        private LocalDateTime dueDate;
        private LocalDateTime currentDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 진행률 정보
    public static class ProgressDto {
        private int totalTasks;
        private int completedTasks;
        private int remainingTasks;
        private double averageTaskCompletionTimeDays;
        // 태스크의 완료 일자
        private RecentCompletionRateDto recentCompletionRate;
        private int delayedTasks;
        private int reopenedTasks;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class RecentCompletionRateDto {
            private int last7Days;
            private int last30Days;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 프로젝트 참여자 정보
    public static class TeamDto {
        private int totalMembers;
        // 활성화 된 멤버가 스톤의 참여자나 담당자로 등록된 멤버인지? 아니면 isDelete가 아닌사람인지?
        private int activeMembers;
        // 멤버별로 할당된 태스크 비율
        private int averageTasksPerMember;
        private double workloadDeviation;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 넣을지 고민
    public static class ContextDto {
        private boolean weekendsExcluded;
        private List<LocalDate> holidays;
        private String velocityChange; // e.g., "slightly increasing"
    }
}
