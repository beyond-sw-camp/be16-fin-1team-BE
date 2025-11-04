package com.Dolmeng_E.search.domain.search.service;

import com.Dolmeng_E.search.domain.search.controller.WorkspaceServiceClient;
import com.Dolmeng_E.search.domain.search.dto.DriveEventDto;
import com.Dolmeng_E.search.domain.search.dto.StoneTaskResDto;
import com.Dolmeng_E.search.domain.search.dto.SubProjectResDto;
import com.Dolmeng_E.search.domain.search.dto.SubTaskResDto;
import com.Dolmeng_E.search.domain.search.repository.DocumentDocumentRepository;
import com.Dolmeng_E.search.domain.search.repository.FileDocumentRepository;
import com.Dolmeng_E.search.domain.search.repository.StoneDocumentRepository;
import com.Dolmeng_E.search.domain.search.repository.TaskDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DriveDeleteEventConsumer {
    private final ObjectMapper objectMapper;
    private final DocumentDocumentRepository documentDocumentRepository;
    private final FileDocumentRepository fileDocumentRepository;
    private final WorkspaceServiceClient workspaceServiceClient;
    private final StoneDocumentRepository stoneDocumentRepository;
    private final TaskDocumentRepository taskDocumentRepository;

    @KafkaListener(topics = "drive-delete-topic", groupId = "search-consumer-group")
    public void handleFile(String eventMessage, Acknowledgment ack) {
        try {
            // 1. Kafka 메시지(JSON)를 DTO로 파싱
            DriveEventDto driveEventDto = objectMapper.readValue(eventMessage, DriveEventDto.class);
            String rootType = driveEventDto.getRootType();
            String rootId = driveEventDto.getRootId();

            if(rootType.equals("WORKSPACE")){
                documentDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                fileDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                List<SubProjectResDto> subProjectResDtos = workspaceServiceClient.getSubProjectsByWorkspace(rootId);
                for(SubProjectResDto subProjectResDto : subProjectResDtos){
                    documentDocumentRepository.deleteByRootTypeAndRootId("PROJECT", subProjectResDto.getProjectId());
                    fileDocumentRepository.deleteByRootTypeAndRootId("PROJECT", subProjectResDto.getProjectId());
                    List<StoneTaskResDto.StoneInfo> stoneInfos = workspaceServiceClient.getSubStonesAndTasks(subProjectResDto.getProjectId()).getStones();
                    List<StoneTaskResDto.TaskInfo> taskInfos = workspaceServiceClient.getSubStonesAndTasks(subProjectResDto.getProjectId()).getTasks();
                    for(StoneTaskResDto.StoneInfo stoneInfo : stoneInfos){
                        documentDocumentRepository.deleteByRootTypeAndRootId("STONE", stoneInfo.getStoneId());
                        fileDocumentRepository.deleteByRootTypeAndRootId("STONE", stoneInfo.getStoneId());
                        stoneDocumentRepository.deleteByDocTypeAndId("STONE", stoneInfo.getStoneId());
                    }
                    for(StoneTaskResDto.TaskInfo taskInfo : taskInfos){
                        taskDocumentRepository.deleteByDocTypeAndId("TASK", taskInfo.getTaskId());
                    }
                }
            }else if(rootType.equals("STONE")){
                documentDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                fileDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                stoneDocumentRepository.deleteByDocTypeAndId(rootType, rootId);
                List<SubTaskResDto> tasks = workspaceServiceClient.getSubTasks(rootId);
                for(SubTaskResDto subTaskResDto : tasks){
                    taskDocumentRepository.deleteByDocTypeAndId("TASK", subTaskResDto.getTaskId());
                }
            }else if(rootType.equals("PROJECT")){
                documentDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                fileDocumentRepository.deleteByRootTypeAndRootId(rootType, rootId);
                List<StoneTaskResDto.StoneInfo> stoneInfos = workspaceServiceClient.getSubStonesAndTasks(rootId).getStones();
                List<StoneTaskResDto.TaskInfo> taskInfos = workspaceServiceClient.getSubStonesAndTasks(rootId).getTasks();
                for(StoneTaskResDto.StoneInfo stoneInfo : stoneInfos){
                    documentDocumentRepository.deleteByRootTypeAndRootId("STONE", stoneInfo.getStoneId());
                    fileDocumentRepository.deleteByRootTypeAndRootId("STONE", stoneInfo.getStoneId());
                    stoneDocumentRepository.deleteByDocTypeAndId("STONE", stoneInfo.getStoneId());
                }
                for(StoneTaskResDto.TaskInfo taskInfo : taskInfos){
                    documentDocumentRepository.deleteByRootTypeAndRootId("TASK", taskInfo.getTaskId());
                    fileDocumentRepository.deleteByRootTypeAndRootId("TASK", taskInfo.getTaskId());
                    stoneDocumentRepository.deleteByDocTypeAndId("TASK", taskInfo.getTaskId());
                }
            }else if(rootType.equals("TASK")){
                taskDocumentRepository.deleteByDocTypeAndId("TASK", rootId);
            }
            System.out.println("ES 처리 성공");
            ack.acknowledge();
        } catch (Exception e) {
            System.err.println("ES 처리 실패: " + e.getMessage());
            ack.acknowledge();
        }
    }
}
