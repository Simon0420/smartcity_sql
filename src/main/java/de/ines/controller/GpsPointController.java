package de.ines.controller;

import de.ines.domain.GpsPoint;
import de.ines.domain.Route;
import de.ines.services.GpsPointService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@RestController
public class GpsPointController {

    @Autowired
    public GpsPointService gpsPointService;

    @RequestMapping(value = "/saveRoute", method = RequestMethod.POST)
    public String saveRoute(@RequestBody String jsonRoute){
        return gpsPointService.upstreamRoute(jsonRoute);
    }

    @RequestMapping(value="/withinDistanceCall", method = RequestMethod.GET)
    public String withinDistanceCall(@RequestParam(value="latitude")double latitude, @RequestParam(value="longitude")double longitude){
        List<GpsPoint> list = gpsPointService.withinDistanceCall(latitude, longitude);
        for(int i = 0; i < list.size(); i++) {
            GpsPoint pt = list.get(i);
            System.out.print((i+1)+". closest"+"Point: "+pt.latitude+", "+pt.longitude);
        }
        return "successful";
    }

}
