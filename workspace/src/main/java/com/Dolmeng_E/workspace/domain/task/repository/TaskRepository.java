package com.Dolmeng_E.workspace.domain.task.repository;

import com.Dolmeng_E.workspace.domain.stone.entity.Stone;
import com.Dolmeng_E.workspace.domain.task.entity.Task;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceParticipant;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("""
        SELECT t
        FROM Task t
        WHERE t.taskManager = :participant
          AND t.isDone = false
          AND t.endTime < :endTime
    """)
    List<Task> findUnfinishedTasksBeforeDate(
            @Param("participant") WorkspaceParticipant participant,
            @Param("endTime") LocalDateTime endTime
    );
    List<Task> findAllByStone(Stone stone);

    @Query("SELECT t FROM Task t WHERE t.stone.project.id = :projectId")
    List<Task> findAllByProjectId(@Param("projectId") String projectId);

    // 기존 findAllByTaskManager_Id 대신 명시적 쿼리 작성
    @Query("SELECT t FROM Task t WHERE t.taskManager.Id = :taskManagerId")
    List<Task> findAllByTaskManagerId(@Param("taskManagerId") String taskManagerId);

    long countByStone(Stone stone);

    long countByStoneAndIsDoneTrue(Stone stone);

    // 3) 프로젝트 내 전체 태스크 수 (삭제된 스톤 소속 태스크 제외)
    @Query("""
           select count(t)
           from Task t
           join t.stone s
           where s.project.id = :projectId
             and s.isDelete = false
           """)
    long countTasksByProjectId(@Param("projectId") String projectId);

    // 4) 프로젝트 내 완료 태스크 수
    @Query("""
           select count(t)
           from Task t
           join t.stone s
           where s.project.id = :projectId
             and s.isDelete = false
             and t.isDone = true
           """)
    long countDoneTasksByProjectId(@Param("projectId") String projectId);

    // LLM 조회를 위한 쿼리문

    // 용도: 프로젝트 전체 태스크 수(자식/손자 스톤 포함)
    @Query("""
        select count(t)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
    """)
    long countAllByProjectId(String projectId);

    // 용도: 프로젝트 완료된 태스크 수
    @Query("""
        select count(t)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
          and t.isDone = true
    """)
    long countCompletedByProjectId(String projectId);

    // 용도: 프로젝트 내 "지연 중(미완료 + 마감 초과)" 태스크 수 (리스크 지표)
    @Query("""
        select count(t)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
          and t.isDone = false
          and t.endTime < :now
    """)
    long countDelayedOpenByProjectId(String projectId, LocalDateTime now);

    // 용도: 프로젝트 내 재오픈 누적 합계 (reopenedCount 합)
    @Query("""
        select coalesce(sum(t.reopenedCount), 0)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
    """)
    long sumReopenedByProjectId(String projectId);

    // 용도: 최근 완료량 집계(윈도우 내 완료 태스크 수) - 최근 7/30일 등
    @Query("""
        select count(t)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
          and t.isDone = true
          and t.taskCompletedDate >= :from
          and t.taskCompletedDate <  :to
    """)
    long countCompletedBetween(String projectId, LocalDateTime from, LocalDateTime to);

    // 용도: 평균 완료 시간(초) - startTime ~ taskCompletedDate, 완료된 태스크만
    //  -> 서비스에서 86400.0으로 나눠 '일'로 변환
    @Query("""
        select avg(function('TIMESTAMPDIFF', SECOND, t.startTime, t.taskCompletedDate))
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
          and t.isDone = true
          and t.taskCompletedDate is not null
    """)
    Long avgCompletionSecondsByProjectId(String projectId);

    // 용도: 지연 태스크 TOP3 (지연일수 큰 순). pageable = PageRequest.of(0,3)로 상위만 가져오기
    @Query("""
        select t
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
          and t.isDone = false
          and t.endTime < :now
        order by t.endTime asc  -- 마감일이 오래될수록 지연일수 큼
    """)
    List<Task> findDelayedTasks(String projectId, LocalDateTime now, Pageable pageable);

    // 용도: 멤버별 태스크 수(Workload 분포 → workloadDeviation 계산용)
    @Query("""
        select t.taskManager.id, count(t)
        from Task t
        join t.stone s
        where s.project.id = :projectId
          and s.isDelete = false
        group by t.taskManager.id
    """)
    List<Object[]> countTasksGroupedByManager(String projectId);

    // 용도: 프로젝트 내 '담당자별' 전체 태스크 수와 완료 태스크 수 집계
    @Query("""
    select t.taskManager.id, count(t), sum(case when t.isDone = true then 1 else 0 end)
    from Task t
      join t.stone s
    where s.project.id = :projectId
      and s.isDelete = false
    group by t.taskManager.id
    """)
    List<Object[]> countTotalsAndCompletedByManager(String projectId);


}
