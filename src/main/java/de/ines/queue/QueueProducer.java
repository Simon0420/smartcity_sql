package de.ines.queue;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by simon on 03.09.2017.
 */
public class QueueProducer {

    Properties props;
    Producer<String, String> producer;
    String topicName;

    public QueueProducer(){
        topicName = "inestopic";

        // create instance for properties to access producer configs
        props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);
    }

    public void producingTest(){
        for(int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>(topicName,
                    Integer.toString(i), Integer.toString(i)));
            System.out.println("Message sent successfully");
        }
    }

    public void produceNewRoute(String json){
        producer.send(new ProducerRecord<String, String>(topicName,
                json, json));
    }

    public void close(){
        producer.close();
    }
}
