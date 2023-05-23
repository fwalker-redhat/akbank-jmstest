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
package org.example.jmspool.rs;

import org.example.jmspool.configuration.SpringContextLoader;
import org.example.jmspool.service.HelloService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml where JAX-RS
 * is enabled
 *
 * @author gbrey@redhat.com
 */

@Path("/")
public class HelloWorld {

    private static final Logger log = Logger.getLogger(HelloWorld.class.getName());
    @Inject
    HelloService helloService;

    @Inject
    SpringContextLoader springContextLoader;

    @GET
    @Path("/message/{message}")
    public void sendMessage(@PathParam("message") String message, @QueryParam("destination") String destination) throws Exception {
        springContextLoader.getApplicationContext().getStartupDate();
        log.info("Sending message to destination " + destination + ": " + message);
        helloService.createHelloMessage(destination, message);
    }


}
