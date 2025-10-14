package com.Dolmeng_E.user.common.service;

import com.Dolmeng_E.user.common.dto.WorkspaceInfoResDto;
import com.Dolmeng_E.user.common.dto.WorkspaceNameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "workspace-service")
public interface WorkspaceFeign {

    // 워크스페이스명 + 회원 id


    @PostMapping("/workspace/return")
    WorkspaceInfoResDto fetchWorkspaceInfo(@RequestHeader("X-User-Id")String userId, @RequestBody WorkspaceNameDto workspaceName);

//    @PostMapping("/user/return/users")
//    WorkspaceInfoResDto fetchUserListInfo(@RequestBody UserIdListDto userIdListDto);
}
