package com.Dolmeng_E.workspace.domain.project.service;

import com.Dolmeng_E.workspace.domain.project.dto.ProjectCreateDto;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectCalendarRepository;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectParticipantRepository;
import com.Dolmeng_E.workspace.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectCalendarRepository projectCalendarRepository;
    private final ProjectParticipantRepository projectParticipantRepository;

// 프로젝트 생성

    public String createProject(String userEmail, ProjectCreateDto dto) {
        // 1. 프로젝트 생성 권한 확인
        // 2. 스톤 생성
        // 3. 프로젝트 생성
        return null;
    }


// 프로젝트 수정

// 프로젝트 목록 조회

// 프로젝트 상세 조회

// 프로젝트 상태변경

// 프로젝트 삭제

}
