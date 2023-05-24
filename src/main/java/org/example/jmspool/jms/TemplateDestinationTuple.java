package org.example.jmspool.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class TemplateDestinationTuple {
    private JmsTemplate jmsTemplate;
    private Destination destination;

    public TemplateDestinationTuple(JmsTemplate jmsTemplate, Destination destination) throws JMSException {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public Destination getDestination() {
        return destination;
    }

    public void send(final String message) {
        if (jmsTemplate == null || destination == null)
            throw new IllegalStateException("JMSTemplate or Destination not set");
        jmsTemplate.send(this.destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }


}
