/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.jmspool.service;

import org.example.jmspool.jms.DestinationProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * A simple CDI service which is able to say hello to someone
 *
 * @author Pete Muir
 */
@ApplicationScoped
public class HelloService {

    private static final Logger log = Logger.getLogger(HelloService.class.getName());
    @Inject
    DestinationProducer producer;

    public void createHelloMessage(String destination, String message) throws Exception {
        log.info("Preparing message to destination " + destination + ": " + message);

        for (int i = 0; i < 100000; i++) {
            producer.produceMessage(destination, message + ": " + (i+1));
        }
    }

}
