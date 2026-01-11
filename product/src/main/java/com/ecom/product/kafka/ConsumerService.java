package com.ecom.product.kafka;

import com.ecom.product.dto.OrderCreatedEvent;
import com.ecom.product.exception.StockUpdateFailedException;
import com.ecom.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ProductService productService;

    @KafkaListener(groupId = "product-group", topics = "order-created-topic")
    public void consumeMessage(OrderCreatedEvent event, Acknowledgment acknowledgment) {
        System.out.println(event.getTotalAmount()+""+event.getOrderId());
        boolean result = productService.updateProductQuantity(event);
        if (!result){
            throw new StockUpdateFailedException("Stock update failed for order id "+event.getOrderId());
        }
        acknowledgment.acknowledge();
    }

    @KafkaListener(groupId = "product-group-dlt", topics = "order-created-topic-dlt", containerFactory = "dltKafkaListenerContainerFactory")
    public void consumeDLT(@Payload(required = false) byte[] data,
                           @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String error) {
        String readable = new String(data, StandardCharsets.UTF_8);
        System.out.println(readable);
        System.out.println("Error: " + error);
    }
}
