package com.ecom.notification.consumer;

import com.ecom.notification.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@EnableKafka
public class ConsumerService {

    @KafkaListener(groupId = "notification-group", topics = "order-created-topic")
    public void consumeMessage(OrderCreatedEvent event){
        System.out.println(event.getStatus());
    }

    @KafkaListener(groupId = "notification-group", topics = "order-created-topic-dlt", containerFactory = "dltKafkaListenerContainerFactory")
    public void consumeDLT( @Payload(required = false) byte[] data,
                            @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String error) {
        String readable = new String(data, StandardCharsets.UTF_8);
        System.out.println(readable);
        System.out.println("Error: " + error);
    }
}
