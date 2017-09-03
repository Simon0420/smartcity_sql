package de.ines.queue;

/**
 * Created by simon on 03.09.2017.
 */
public class QueueTest {
    public static void main(String[] args){
        QueueProducer producer = new QueueProducer();
        producer.producingTest();
        QueueConsumer consumer = new QueueConsumer();
        consumer.consuming();
    }
}
