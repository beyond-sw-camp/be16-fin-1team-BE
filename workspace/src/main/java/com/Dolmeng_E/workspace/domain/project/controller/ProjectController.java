package com.Dolmeng_E.workspace.domain.project.controller;

import com.Dolmeng_E.workspace.domain.project.dto.ProjectCreateDto;
import com.Dolmeng_E.workspace.domain.project.service.ProjectService;
import com.example.modulecommon.dto.CommonSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            @RequestHeader("X-User-Id") String userId,
            @RequestBody ProjectCreateDto dto
    ) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .statusMessage("프로젝트 생성 완료")
                .result(projectService.createProject(userId, dto))
                .statusCode(HttpStatus.CREATED.value())
                .build()
                ,HttpStatus.CREATED);
    }

// 프로젝트 수정

// 프로젝트 목록 조회

// 프로젝트 상세 조회

// 프로젝트 상태변경

// 프로젝트 삭제

}
