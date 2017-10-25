package de.ines.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class ExchangeAndQueueConfig {

    @Bean
    Queue upStreamQueue(){
        return new Queue("UpStreamQueue", false);
    }

    @Bean
    Queue postDatabaseStreamQueue(){
        return new Queue("PostDatabaseStreamQueue", false);
    }

    @Bean
    Queue postDatabaseStreamQueue1(){
        return new Queue("PostDatabaseStreamQueue1", false);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange("SmartCity-Exchange");
    }

    @Bean
    FanoutExchange exchange1(){
        return new FanoutExchange("SmartCity-Exchange1");
    }

    @Bean
    List<Binding> bindings(){
        return Arrays.asList(BindingBuilder.bind(upStreamQueue()).to(exchange()).with("UpStreamQueue"),
                //BindingBuilder.bind(postDatabaseStreamQueue()).to(exchange1()).with("PostDatabaseStreamQueue"));
                BindingBuilder.bind(postDatabaseStreamQueue()).to(exchange1()),
                BindingBuilder.bind(postDatabaseStreamQueue1()).to(exchange1()));

    }
}
