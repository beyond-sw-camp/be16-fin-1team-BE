package com.Dolmeng_E.workspace.domain.chatbot.service;

import com.Dolmeng_E.workspace.common.service.RestTemplateClient;
import com.Dolmeng_E.workspace.domain.chatbot.dto.ChatbotMessageUserReqDto;
import com.Dolmeng_E.workspace.domain.chatbot.dto.N8nAgentReqDto;
import com.Dolmeng_E.workspace.domain.chatbot.entity.ChatbotMessage;
import com.Dolmeng_E.workspace.domain.chatbot.entity.ChatbotMessageType;
import com.Dolmeng_E.workspace.domain.chatbot.repository.ChatbotMessageRepository;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceParticipant;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceParticipantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatbotMessageService {
    static final String AGENT_URL = "http://localhost:5678/webhook-test/chatbot-agent";

    private final ChatbotMessageRepository chatbotMessageRepository;
    private final WorkspaceParticipantRepository workspaceParticipantRepository;
    private final RestTemplateClient restTemplateClient;

    public String sendMessage(String userId, ChatbotMessageUserReqDto reqDto) {
        // 워크스페이스 참여자 검증
        WorkspaceParticipant participant = workspaceParticipantRepository
                .findByWorkspaceIdAndUserId(reqDto.getWorkspaceId(), UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스 참여자가 아닙니다."));

        // 사용자가 보낸 메시지 저장
        ChatbotMessage chatbotUserMessage = ChatbotMessage.builder()
                .content(reqDto.getContent())
                .type(ChatbotMessageType.USER)
                .workspaceParticipant(participant)
                .build();
        chatbotMessageRepository.save(chatbotUserMessage);

        // 사용자 메시지를 agent에게 보내기전에 필요한 데이터 담기
        N8nAgentReqDto n8nAgentReqDto = N8nAgentReqDto.builder()
                .workspaceId(reqDto.getWorkspaceId())
                .content(reqDto.getContent())
                .userId(userId)
                .userName(participant.getUserName())
                .build();

        // agent에게 요청 및 응답 받아오기
        ResponseEntity<String> response = restTemplateClient.post(AGENT_URL, n8nAgentReqDto, String.class);

        // agent의 응답을 답장으로 저장
        ChatbotMessage chatbotBotMessage = ChatbotMessage.builder()
                .content(response.getBody())
                .type(ChatbotMessageType.BOT)
                .workspaceParticipant(participant)
                .build();
        chatbotMessageRepository.save(chatbotBotMessage);

        return response.getBody();
    }
}
