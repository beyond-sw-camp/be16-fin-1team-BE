package com.Dolmeng_E.chat.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

}
