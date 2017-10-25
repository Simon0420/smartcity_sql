package de.ines.Configuration;

import de.ines.services.PostDatabaseStreamService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class RabbitPostDatabaseStreamConfig {

    @Bean
    public ConnectionFactory connectionFactory1() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer container1(PostDatabaseStreamService postDatabaseStreamService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory1());
        container.setQueueNames("PostDatabaseStreamQueue");
        MessageListenerAdapter messageListenerAdapter = postDatabaseStreamMessageAdapter(postDatabaseStreamService);
        messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }


    @Bean
    MessageListenerAdapter postDatabaseStreamMessageAdapter(PostDatabaseStreamService receiver){
        return new MessageListenerAdapter(receiver, "postDatabaseStreamMessage");
    }



}
