package com.Dolmeng_E.workspace.domain.project.controller;

import com.Dolmeng_E.workspace.domain.project.dto.ProjectCreateDto;
import com.Dolmeng_E.workspace.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

// 프로젝트 생성
    @PostMapping("")
    public ResponseEntity<?> createProject(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody ProjectCreateDto dto
    ) {
        projectService.createProject(userEmail,dto);
        return null;
    }

// 프로젝트 수정

// 프로젝트 목록 조회

// 프로젝트 상세 조회

// 프로젝트 상태변경

// 프로젝트 삭제

}
