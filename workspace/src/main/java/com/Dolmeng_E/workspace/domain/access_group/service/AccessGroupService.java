package com.Dolmeng_E.workspace.domain.access_group.service;

import com.Dolmeng_E.workspace.domain.access_group.dto.CustomAccessGroupDto;
import com.Dolmeng_E.workspace.domain.access_group.entity.AccessDetail;
import com.Dolmeng_E.workspace.domain.access_group.entity.AccessGroup;
import com.Dolmeng_E.workspace.domain.access_group.entity.AccessList;
import com.Dolmeng_E.workspace.domain.access_group.entity.AccessType;
import com.Dolmeng_E.workspace.domain.access_group.repository.AccessDetailRepository;
import com.Dolmeng_E.workspace.domain.access_group.repository.AccessGroupRepository;
import com.Dolmeng_E.workspace.domain.access_group.repository.AccessListRepository;
import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessGroupService {

    private final AccessGroupRepository accessGroupRepository;
    private final AccessDetailRepository accessDetailRepository;
    private final AccessListRepository accessListRepository;
    private final WorkspaceRepository workspaceRepository;


//    To-Do : 모든 로직 구현 후 try-catch 작업 해야함

    // 관리자 권한 그룹 생성 (워크스페이스 ID 기반, 워크스페이스 생성시 자동생성)
    public void createAdminGroupForWorkspace(String workspaceId) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다. ID=" + workspaceId));

        AccessGroup adminGroup = AccessGroup.builder()
                .workspace(workspace)
                .accessGroupName("관리자 그룹")
                .build();
        accessGroupRepository.save(adminGroup);

        for (AccessType type : AccessType.values()) {
            AccessList accessList = accessListRepository.findByAccessType(type)
                    .orElseThrow(() -> new IllegalStateException("AccessList 정의 없음: " + type));

            AccessDetail detail = AccessDetail.builder()
                    .accessGroup(adminGroup)
                    .accessList(accessList)
                    .isAccess(true)
                    .build();

            accessDetailRepository.save(detail);
        }
    }


    //    일반유저 권한그룹 생성(워크스페이스 생성시 자동생성)
    public void createDefaultUserAccessGroup(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다. ID=" + workspaceId));

        AccessGroup defaultUserGroup = AccessGroup.builder()
                .workspace(workspace)
                .accessGroupName("일반 유저 그룹")
                .build();
        accessGroupRepository.save(defaultUserGroup);

        // 스톤의 파일 조회만 허용(임시)
        Set<AccessType> defaultTruePermissions = Set.of(AccessType.STONE_FILE_VIEW);

        for (AccessType type : AccessType.values()) {
            AccessList accessList = accessListRepository.findByAccessType(type)
                    .orElseThrow(() -> new IllegalStateException("AccessList 정의 없음: " + type));

            boolean isAccess = defaultTruePermissions.contains(type);

            AccessDetail detail = AccessDetail.builder()
                    .accessGroup(defaultUserGroup)
                    .accessList(accessList)
                    .isAccess(isAccess)
                    .build();

            accessDetailRepository.save(detail);
        }


    }

    //    커스터마이징 권한그룹 생성

    public void createCustomAccessGroup(CustomAccessGroupDto dto) {

        // 1. 워크스페이스 조회
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다. ID=" + dto.getWorkspaceId()));

        // 2. AccessGroup 생성
        AccessGroup newGroup = AccessGroup.builder()
                .workspace(workspace)
                .accessGroupName(dto.getAccessGroupName())
                .build();

        accessGroupRepository.save(newGroup);

        // 3️. AccessType 순서대로 accessList 값 매핑
        AccessType[] types = AccessType.values();
        List<Boolean> accessValues = dto.getAccessList();

        for (int i = 0; i < types.length; i++) {
            AccessType type = types[i];
            boolean isAccess = i < accessValues.size() && Boolean.TRUE.equals(accessValues.get(i));

            // AccessList (권한 정의 테이블) 조회
            AccessList accessList = accessListRepository.findByAccessType(type)
                    .orElseThrow(() -> new IllegalStateException("AccessList 정의 없음: " + type));

            // AccessDetail 생성 및 저장
            AccessDetail detail = AccessDetail.builder()
                    .accessGroup(newGroup)
                    .accessList(accessList)
                    .isAccess(isAccess)
                    .build();

            accessDetailRepository.save(detail);
        }
    }

    //    권한그룹 수정

    //    권한그룹 리스트 조회

    //    권한그룹 상세 조회

    //    권한그룹 사용자 추가

    //    권한그룹 사용자 수정

    //    권한그룹 사용자 제거

    //    권한그룹 삭제
}
