package de.ines.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDatabaseStreamService {

    @Autowired
    public AmqpTemplate template;

    @Autowired
    private FanoutExchange fanout;

    /**
     * Method being called whenever the FanoutExchange (connected to the PostDatabaseStreamService class) is triggered.
     * @param message
     */
    public void postDatabaseStreamRoute(Long message){
        template.convertAndSend(fanout.getName(),"", message);
    }

}
