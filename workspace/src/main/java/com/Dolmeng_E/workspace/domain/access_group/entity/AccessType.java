package com.Dolmeng_E.workspace.domain.access_group.entity;

public enum AccessType {
    INVITE_USER,         // ws_acc_list_1. 유저 초대
    PROJECT_CREATE,      // ws_acc_list_2. 프로젝트 생성
    STONE_CREATE,        // ws_acc_list_3. 스톤 생성
    USER_GROUP_CREATE,   // ws_acc_list_4. 사용자 그룹 생성
    PROJECT_FILE_VIEW,   // ws_acc_list_5. 프로젝트별 파일 조회
    STONE_FILE_VIEW,     // ws_acc_list_6. 스톤별 파일 조회
    WORKSPACE_FILE_VIEW  // ws_acc_list_7. 워크스페이스별 파일 조회
}

