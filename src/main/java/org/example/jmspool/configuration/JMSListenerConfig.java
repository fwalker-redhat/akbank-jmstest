package org.example.jmspool.configuration;

import org.example.jmspool.jms.JMSConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;

@EnableJms
public class JMSListenerConfig {

    private static final Logger log = Logger.getLogger(JMSListenerConfig.class.getName());

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws NamingException {
        Context context = new InitialContext();
        log.info("Got Initial Context");
        ConnectionFactory cf = new SingleConnectionFactory((QueueConnectionFactory) context.lookup("java:jboss/jms/ffmQueueCF2"));
        log.info("Created Caching ConnectionFactory");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public DefaultMessageListenerContainer queue1MessageListenerContainer(DefaultJmsListenerContainerFactory dlcf) throws NamingException {
        SimpleJmsListenerEndpoint endpoint =
                new SimpleJmsListenerEndpoint();
        endpoint.setMessageListener(new JMSConsumer());
        endpoint.setDestination("DEV.QUEUE.1");
        log.info("Created new endpoint: DEV.QUEUE.1");
        return dlcf.createListenerContainer(endpoint);
    }

    @Bean
    public DefaultMessageListenerContainer queue2MessageListenerContainer(DefaultJmsListenerContainerFactory dlcf) throws NamingException {
        SimpleJmsListenerEndpoint endpoint =
                new SimpleJmsListenerEndpoint();
        endpoint.setMessageListener(new JMSConsumer());
        endpoint.setDestination("DEV.QUEUE.2");
        log.info("Created new endpoint: DEV.QUEUE.2");
        return dlcf.createListenerContainer(endpoint);
    }

}
