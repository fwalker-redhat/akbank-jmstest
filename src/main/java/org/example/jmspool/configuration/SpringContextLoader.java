package org.example.jmspool.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.util.logging.Logger;

@ApplicationScoped
public class SpringContextLoader {

    private static final Logger log = Logger.getLogger(SpringContextLoader.class.getName());

    private ApplicationContext applicationContext;

    @PostConstruct
    public void populateApplicationContext() {
        log.info("Populating Application Context");
        this.applicationContext = new AnnotationConfigApplicationContext(JMSListenerConfig.class);
    }

    @Singleton
    public ApplicationContext getApplicationContext() {
        log.info("Application Context Loading");
        if (this.applicationContext != null) {
            log.info("Application Context Started at: " + this.applicationContext.getStartupDate());
            return this.applicationContext;
        }
        throw new RuntimeException("Spring Application Context not Created");
    }
}
