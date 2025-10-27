package com.Dolmeng_E.search.domain.search.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(indexName = "tasks")
public class TaskDocument {
    // --- 공통 식별 필드 ---
    // 검색 대상 ID
    @Id
    private String id;

    // 생성자
    @Field(type = FieldType.Keyword, index = false)
    private String createdBy;

    // 검색할 대상 타입
    @Builder.Default
    @Field(type = FieldType.Keyword)
    private String docType = "TASK";

    // --- 검색 대상 필드 ---
    // 제목
    @Field(type = FieldType.Text, analyzer = "nori")
    private String searchTitle;

    // 설명
    @Field(type = FieldType.Text, analyzer = "nori")
    private String searchContent;

    // --- 권한/정렬 필드 ---
    // 권한 필드 stone 참여자 목록 + 프로젝트 관리자 + 워크스페이스 관리자
    @Field(type = FieldType.Keyword)
    private List<String> viewableUserIds;

    // --- URL 생성 및 UI 표시용 필드 ---
    // 완료 여부
    @Field(type = FieldType.Boolean)
    private Boolean isDelete;

    // 생성일
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime createdAt;

    // 생성자 프로필 이미지
    @Field(type = FieldType.Keyword, index = false)
    private String profileImage;

    // 생성자 이름
    @Field(type = FieldType.Keyword, index = false)
    private String creatorName;
}
