package org.example.jmspool.jms;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class DestinationProducer {
    private static final Logger log = Logger.getLogger(DestinationProducer.class.getName());
    Map<String, TemplateDestinationTuple> templateDestinationTuples = new HashMap<String, TemplateDestinationTuple>();

    public void produceMessage(String destination, String message) throws NamingException, JMSException {
        getTemplateDestinationTuples(destination).send(message);
    }

    private TemplateDestinationTuple getTemplateDestinationTuples(String destination) throws NamingException, JMSException {
        log.info("Getting Destination JMSTemplate");
        if (templateDestinationTuples.get(destination) == null) {
            log.info("Destination JMSTemplate not Found, Creating New");
            templateDestinationTuples.put(destination, new TemplateDestinationTuple(createJmsTemplate(), destination));
        }
        return templateDestinationTuples.get(destination);
    }

    private JmsTemplate createJmsTemplate() throws NamingException {
        Context context = new InitialContext();
        log.info("Got Initial Context");
        ConnectionFactory cf = new CachingConnectionFactory((QueueConnectionFactory) context.lookup("java:jboss/jms/ffmQueueCF2"));
        log.info("Created Caching ConnectionFactory");
        return new JmsTemplate(cf);
    }

}
