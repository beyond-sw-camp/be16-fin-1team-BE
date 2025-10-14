package com.Dolmeng_E.workspace.domain.project.service;

import com.Dolmeng_E.workspace.domain.access_group.entity.AccessDetail;
import com.Dolmeng_E.workspace.domain.access_group.entity.AccessGroup;
import com.Dolmeng_E.workspace.domain.access_group.repository.AccessDetailRepository;
import com.Dolmeng_E.workspace.domain.access_group.repository.AccessGroupRepository;
import com.Dolmeng_E.workspace.domain.project.dto.ProjectCreateDto;
import com.Dolmeng_E.workspace.domain.project.entity.Project;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectCalendarRepository;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectParticipantRepository;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectRepository;
import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceParticipant;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceParticipantRepository;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectCalendarRepository projectCalendarRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final WorkspaceParticipantRepository workspaceParticipantRepository;
    private final AccessDetailRepository accessDetailRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final WorkspaceRepository workspaceRepository;

// 프로젝트 생성

    public String createProject(String userId, ProjectCreateDto dto) {
        // 1. 워크스페이스 참여자 객체 생성
        WorkspaceParticipant workspaceParticipant = workspaceParticipantRepository
                .findByWorkspaceIdAndUserId(dto.getWorkspaceId(), UUID.fromString(userId))
                .orElseThrow(()->new EntityNotFoundException("워크스페이스 참여자가 아닙니다."));
        // 2. 프로젝트 생성 권한 확인
        String accessGroupId = workspaceParticipant.getAccessGroup().getId();
        AccessGroup accessGroup = accessGroupRepository.findById(accessGroupId)
                .orElseThrow(()-> new EntityNotFoundException("권한그룹이 존재하지 않습니다."));
        AccessDetail accessDetail = accessDetailRepository.findByAccessGroupAndAccessListId(accessGroup,"ws_acc_list_2")
                        .orElseThrow(()-> new EntityNotFoundException("권한그룹 상세정보가 없습니다."));

        if(!accessDetail.getIsAccess()) {
            throw new IllegalArgumentException("프로젝트 생성 권한이 없습니다.");
        }
        // 3. 스톤 생성

        // 4. 프로젝트 생성
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(()->new EntityNotFoundException("워크스페이스 정보를 찾을 수 없습니다."));
        Project project = dto.toEntity(workspace);
        projectRepository.save(project);
        return project.getId();
    }


// 프로젝트 수정

// 프로젝트 목록 조회

// 프로젝트 상세 조회

// 프로젝트 상태변경

// 프로젝트 삭제

}
