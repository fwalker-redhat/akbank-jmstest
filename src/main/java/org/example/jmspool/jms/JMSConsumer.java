package org.example.jmspool.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

public class JMSConsumer implements MessageListener {

    private static final Logger log = Logger.getLogger(JMSConsumer.class.getName());

    public void onMessage(Message message) {
        log.info("Message Received");
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage) message).getText();
                log.info("Received message: " + msg);
                if (msg == null) {
                    throw new IllegalArgumentException("Null value received...");
                }
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
