package com.aryan.orbit.kafka;

import com.aryan.orbit.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC = "order-events";

    public void publish(OrderEvent event){
        kafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event)
                .thenAccept(recordMetadata -> System.out.println("Sent order event to Kafka: " + recordMetadata.toString()));
    }
}
