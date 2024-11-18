package com.example.GoQuiz.config;

import com.example.GoQuiz.common.Constant;
import com.example.GoQuiz.handler.QuizChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/ws/*");
//        registry.setApplicationDestinationPrefixes("*");
//    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/java-flutter-websocket").withSockJS();
//    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/quiz").setAllowedOrigins("*");
    }

    @Bean
    public QuizChatWebSocketHandler webSocketHandler() {
        return new QuizChatWebSocketHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {

        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(Constant.WEBSOCKET_MAX_BUFFER_SIZE);
        container.setMaxTextMessageBufferSize(Constant.WEBSOCKET_MAX_BUFFER_SIZE);
        container.setMaxSessionIdleTimeout(Constant.WEBSOCKET_TIMEOUT);
        container.setAsyncSendTimeout(Constant.WEBSOCKET_TIMEOUT);
        return container;
    }
}
