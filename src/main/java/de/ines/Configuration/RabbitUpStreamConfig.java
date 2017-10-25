package de.ines.Configuration;

import de.ines.domain.GpsPoint;
import de.ines.services.GpsPointService;
import de.ines.services.PostDatabaseStreamService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class RabbitUpStreamConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        //The routing key is set to the name of the queue by the broker for the default exchange.
        template.setRoutingKey("UpStreamQueue");
        //Where we will synchronously receive messages from
        template.setQueue("UpStreamQueue");
        return template;
    }




    @Bean
    public SimpleMessageListenerContainer container(GpsPointService gpsPointService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("UpStreamQueue");
        MessageListenerAdapter messageListenerAdapter = upStreamListenerAdapter(gpsPointService);
        messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter upStreamListenerAdapter(GpsPointService receiver){
        return new MessageListenerAdapter(receiver, "upStreamMessage");
    }


}
