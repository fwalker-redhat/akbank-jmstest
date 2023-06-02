package org.example.jmspool.jms;

import org.springframework.jms.JmsException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class DestinationProducer {
    private static final Logger log = Logger.getLogger(DestinationProducer.class.getName());
    private final Map<String, JmsTemplate> templateDestinationTuples = new HashMap<>();

    private final ConnectionFactory cf;

    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService executorService;

    public DestinationProducer() throws NamingException, JMSException {
        log.info("Constructing DestinationProducer");
        Context context = new InitialContext();
        log.info("Got Initial Context");
        CachingConnectionFactory ccf = new CachingConnectionFactory((ConnectionFactory) context.lookup("java:jboss/jms/CF"));
        ccf.setSessionCacheSize(200);
        cf = ccf;
        templateDestinationTuples.put("DEV.QUEUE.1", createJmsTemplate());
        templateDestinationTuples.put("DEV.QUEUE.2", createJmsTemplate());
    }

    @Async
    public void produceMessage(String destination, String message) throws NamingException, JMSException {
        log.info("Submitting message: " + message);
        try {
            JmsTemplate jmsTemplate = templateDestinationTuples.get(destination);
            log.info("Really Sending message : " + message + ", with JMSTemplate : " + jmsTemplate.toString() + " and connectionFactory : " + jmsTemplate.getConnectionFactory().toString());
            jmsTemplate.send(destination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(message);
                }
            });
            log.info("Message Sent: " + message);
        } catch (JmsException je) {
            log.info("Whoops, something went wrong:" + je.getMessage());
        }
    }
//
//    public void produceMessage(String destination, String message) throws NamingException, JMSException {
//        log.info("Submitting message: " + message);
//        executorService.execute(new Produce(templateDestinationTuples.get(destination), destination, message));
//    }

    private JmsTemplate createJmsTemplate() throws NamingException {
        return new JmsTemplate(cf);
    }

    static class Produce implements Runnable {

        private final JmsTemplate jmsTemplate;

        private final String destination;
        private final String message;

        public Produce(JmsTemplate jmsTemplate, String destination, String message) {
            this.jmsTemplate = jmsTemplate;
            this.destination = destination;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                log.info("Really Sending message : " + this.message + ", with JMSTemplate : " + jmsTemplate.toString() + " and connectionFactory : " + jmsTemplate.getConnectionFactory().toString());
                jmsTemplate.send(this.destination, session -> session.createTextMessage(message));
                log.info("Message Sent: " + message);
            } catch (JmsException je) {
                log.info("Whoops, something went wrong:" + je.getMessage());
            }
        }

    }

}
