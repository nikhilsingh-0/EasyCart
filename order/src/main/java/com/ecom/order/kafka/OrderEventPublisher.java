package com.ecom.order.kafka;

import com.ecom.order.dto.OrderCreatedEvent;
import com.ecom.order.dto.OrderItemDTO;
import com.ecom.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String,OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreated(Order order) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(i -> new OrderItemDTO(
                                i.getId(),
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPrice(),
                                i.getPrice().multiply(
                                        BigDecimal.valueOf(i.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );

        CompletableFuture<SendResult<String, OrderCreatedEvent>> result = kafkaTemplate.send("order-created-topic", event);
        result.whenComplete((res,ex)->{
            if (ex!=null){
                System.out.println(ex.getMessage());
            }
            System.out.println(res.getProducerRecord().topic());
        });
    }
}

