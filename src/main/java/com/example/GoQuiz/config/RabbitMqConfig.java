package com.example.GoQuiz.config;

import com.example.GoQuiz.common.CommonResources;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ThreadChannelConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final CommonResources commonResources;
    @Bean
    @SneakyThrows
    public ConnectionFactory connectionFactory() {
        com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        connectionFactory.setHost(commonResources.getRabbitMQHost());
        connectionFactory.setPort(commonResources.getRabbitMQPort());
        connectionFactory.setUsername(commonResources.getRabbitMQUsername());
        connectionFactory.setPassword(commonResources.getRabbitMQPassword());
        if (commonResources.isRabbitMQSSL()) connectionFactory.useSslProtocol();

        // As of version 4.0, the client enables automatic recovery by default.
        // While compatible with this feature, Spring AMQP has its own recovery mechanisms and the client recovery feature generally is not needed.
        // to avoid getting AutoRecoverConnectionNotCurrentlyOpenException instances when the broker is available but the connection has not yet recovered.
        // => disable AutomaticRecoveryEnabled when create new ConnectionFactory
        ThreadChannelConnectionFactory threadConnectionFactory = new ThreadChannelConnectionFactory(connectionFactory);
        threadConnectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(false);
        return threadConnectionFactory;
    }

}
