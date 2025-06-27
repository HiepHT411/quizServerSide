package com.hiephoang.platform.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommonResources {

    // RabbitMQ config
    @Value("${spring.rabbitmq.host}")
    private String rabbitMQHost;

    @Value("${spring.rabbitmq.port}")
    private Integer rabbitMQPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitMQUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitMQPassword;

    @Value("${spring.rabbitmq.enabled.ssl}")
    private boolean rabbitMQSSL;
}
