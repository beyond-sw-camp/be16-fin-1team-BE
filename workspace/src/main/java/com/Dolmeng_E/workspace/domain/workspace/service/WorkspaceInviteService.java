package com.Dolmeng_E.workspace.domain.workspace.service;

import com.Dolmeng_E.workspace.common.dto.UserEmailListDto;
import com.Dolmeng_E.workspace.common.dto.UserInfoListResDto;
import com.Dolmeng_E.workspace.common.dto.UserInfoResDto;
import com.Dolmeng_E.workspace.common.service.UserFeign;
import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceInvite;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceParticipant;
import com.Dolmeng_E.workspace.domain.workspace.entity.WorkspaceRole;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceInviteRepository;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceParticipantRepository;
import com.Dolmeng_E.workspace.domain.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceInviteService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceInviteRepository inviteRepository;
    private final WorkspaceParticipantRepository participantRepository;
    private final JavaMailSender mailSender;
    private final UserFeign userFeignClient;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    // 1. 여러 명 초대 메일 발송
    @Transactional
    public void sendInviteList(String userId, String workspaceId, List<String> emailList) {

        // 1. 워크스페이스 조회
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스를 찾을 수 없습니다."));

        // 2. 초대자 검증
        WorkspaceParticipant inviter = participantRepository
                .findByWorkspaceIdAndUserId(workspaceId, UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스 참여자가 아닙니다."));

        if (!inviter.getWorkspaceRole().equals(WorkspaceRole.ADMIN)) {
            throw new IllegalArgumentException("워크스페이스 관리자만 초대할 수 있습니다.");
        }

        // 3. user-service 에서 이메일 리스트로 사용자 정보 조회
        UserEmailListDto dto = new UserEmailListDto();
        dto.setUserEmailList(emailList);
        UserInfoListResDto userInfoListResDto = userFeignClient.fetchUserInfoByEmail(dto);

        List<UserInfoResDto> userInfoList = userInfoListResDto.getUserInfoList();

        // 4. 초대 처리 반복
        for (UserInfoResDto userInfo : userInfoList) {
            String email = userInfo.getUserEmail();

            // 이미 초대된 이메일은 스킵
            if (inviteRepository.existsByWorkspaceAndEmail(workspace, email)) continue;

            // 이미 참여 중인 사용자라면 스킵 (삭제된 유저는 재초대 가능)
            boolean alreadyActive = participantRepository.findByWorkspaceId(workspace.getId()).stream()
                    .anyMatch(p -> !p.isDelete() && p.getUserId().equals(userInfo.getUserId()));
            if (alreadyActive) continue;

            // 초대 토큰 생성
            String token = UUID.randomUUID().toString();

            // 초대 엔티티 생성
            WorkspaceInvite invite = WorkspaceInvite.builder()
                    .email(email)
                    .inviteToken(token)
                    .workspace(workspace)
                    .inviter(inviter)
                    .expiredAt(LocalDateTime.now().plusHours(24))
                    .isUsed(false)
                    .build();

            inviteRepository.save(invite);

            // 초대 메일 전송
            sendInviteEmail(userInfo.getUserName(), email, workspace.getWorkspaceName(), token);
        }
    }

    // 5. 초대 메일 전송 메서드
    private void sendInviteEmail(String userName, String email, String workspaceName, String token) {
        String inviteLink = frontendUrl + "/invite/accept?token=" + token;

        String subject = "[Dolmeng_E] 워크스페이스 초대: " + workspaceName;
        String body = """
                안녕하세요, %s님!

                %s 워크스페이스로 초대되었습니다.
                아래 링크를 클릭해 참여를 완료해주세요 👇

                %s

                초대코드는 24시간 동안 유효합니다.
                """.formatted(userName, workspaceName, inviteLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    // 6. 초대 수락 (기존 그대로 유지)
    @Transactional
    public void acceptInvite(String userId, String token) {
        WorkspaceInvite invite = inviteRepository.findByInviteToken(token)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 초대코드입니다."));

        if (invite.isUsed()) throw new IllegalArgumentException("이미 사용된 초대코드입니다.");
        if (invite.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("초대코드가 만료되었습니다.");

        Workspace workspace = invite.getWorkspace();
        UUID uuid = UUID.fromString(userId);

        Optional<WorkspaceParticipant> participantOpt =
                participantRepository.findByWorkspaceIdAndUserId(workspace.getId(), uuid);

        if (participantOpt.isPresent()) {
            WorkspaceParticipant participant = participantOpt.get();
            if (participant.isDelete()) {
                participant.restoreParticipant();
            } else {
                throw new IllegalArgumentException("이미 워크스페이스에 참여 중입니다.");
            }
        } else {
            WorkspaceParticipant newParticipant = WorkspaceParticipant.builder()
                    .workspace(workspace)
                    .userId(uuid)
                    .userName("초대된 사용자")
                    .workspaceRole(WorkspaceRole.COMMON)
                    .isDelete(false)
                    .build();
            participantRepository.save(newParticipant);
        }

        invite.setUsed(true);
    }
}

