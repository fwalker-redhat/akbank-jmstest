package org.example.jmspool.jms;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class DestinationProducer {
    private static final Logger log = Logger.getLogger(DestinationProducer.class.getName());

    private final Map<String, String> QUEUE_JNDI;
    Map<String, TemplateDestinationTuple> templateDestinationTuples = new HashMap<String, TemplateDestinationTuple>();

    public DestinationProducer() {
        this.QUEUE_JNDI = new HashMap<>();
        this.QUEUE_JNDI.put("DEV.QUEUE.1", "java:jboss/jms/queue1");
        this.QUEUE_JNDI.put("DEV.QUEUE.2", "java:jboss/jms/queue2");
    }

    public void produceMessage(String destination, String message) throws NamingException, JMSException {
        getTemplateDestinationTuples(destination).send(message);
    }

    private TemplateDestinationTuple getTemplateDestinationTuples(String destination) throws NamingException, JMSException {
        log.info("Getting Destination JMSTemplate");
        if (templateDestinationTuples.get(destination) == null) {
            log.info("Destination JMSTemplate not Found, Creating New");
            templateDestinationTuples.put(destination, new TemplateDestinationTuple(createJmsTemplate(), getDestination(destination)));
        }
        return templateDestinationTuples.get(destination);
    }

    private JmsTemplate createJmsTemplate() throws NamingException {
        Context context = new InitialContext();
        log.info("Got Initial Context");
        ConnectionFactory cf = new CachingConnectionFactory((ConnectionFactory) context.lookup("java:jboss/jms/CF"));
        log.info("Created Caching ConnectionFactory");
        return new JmsTemplate(cf);
    }

    private Destination getDestination(String queueName) throws NamingException, IllegalArgumentException {
        Context context = new InitialContext();
        log.info("Got Initial Context");
        String jndiLookup = this.QUEUE_JNDI.get(queueName);
        if (jndiLookup == null) throw new IllegalArgumentException("Queue JNDI Not Defined");
        return (Destination) context.lookup(jndiLookup);
    }

}
