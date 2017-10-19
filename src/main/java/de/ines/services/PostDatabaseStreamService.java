package de.ines.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDatabaseStreamService {

    @Autowired
    public AmqpTemplate template;

    public void postDatabaseStreamRoute(String message){
        template.convertAndSend("SmartCity-Exchange1","PostDatabaseStreamQueue", message);
    }

    public void postDatabaseStreamMessage(String message){
        System.out.println(message);
    }

}
