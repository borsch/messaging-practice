package com.github.borsch.messagingpractice.activemq.producer.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AmqConfig {

    public static final String TOPIC_JMS_TEMPLATE = "AmqConfig.topicJmsTemplate";
    public static final String QUEUE_JMS_TEMPLATE = "AmqConfig.queueJmsTemplate";

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

    @Bean(TOPIC_JMS_TEMPLATE)
    JmsTemplate topicJmsTemplate(final ConnectionFactory connectionFactory) {
        final JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setConnectionFactory(connectionFactory);

        return jmsTemplate;
    }

    @Bean(QUEUE_JMS_TEMPLATE)
    JmsTemplate queueJmsTemplate(final ConnectionFactory connectionFactory) {
        final JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setConnectionFactory(connectionFactory);

        return jmsTemplate;
    }

}
