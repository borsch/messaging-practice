package com.github.borsch.messagingpractice.activemq.consumer.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AmqConfig {

    public static final String VIRTUAL_TOPIC_JMS_LISTENER = "AmqConfig.virtualTopicJmsListener";

    @Value("${spring.activemq.broker-url}")
    private final String brokerUrl;
    @Value("${spring.activemq.user}")
    private final String username;
    @Value("${spring.activemq.password}")
    private final String password;

    @Bean
    ConnectionFactory connectionFactory() {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean(VIRTUAL_TOPIC_JMS_LISTENER)
    DefaultJmsListenerContainerFactory virtualTopicJmsListener(final ConnectionFactory connectionFactory) {
        final DefaultJmsListenerContainerFactory jmsListener = new DefaultJmsListenerContainerFactory();
        jmsListener.setPubSubDomain(false);
        jmsListener.setConnectionFactory(connectionFactory);

        return jmsListener;
    }
}
