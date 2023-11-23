package com.damoyeo.healthyLife.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*");
        // .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	registry.enableSimpleBroker("/queue","/topic"); //구독자는 topic경로 구독, 발신자는 /topic을 헤더로 넣어서 메세지 브로커를 통해 구독자들에게 송신 가능
    	// /app 서버에서 가공처리가 필요하다면 오는 경로. 끝난걸 /topic경로로 주면 됨. 
        registry.setApplicationDestinationPrefixes("/app"); //메세지를 받을때 경로
    }
}
