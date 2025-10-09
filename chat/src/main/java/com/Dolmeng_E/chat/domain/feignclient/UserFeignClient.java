package com.Dolmeng_E.chat.domain.feignclient;

import com.Dolmeng_E.chat.common.dto.UserInfoResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// name 부분은 eureka에 등록된 application.name을 의미
@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/user/return")
    UserInfoResDto fetchUserInfo(@RequestHeader("X-User-Email")String userEmail);
}
