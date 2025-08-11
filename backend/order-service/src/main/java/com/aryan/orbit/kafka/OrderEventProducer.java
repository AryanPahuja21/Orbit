package com.aryan.orbit.events;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC = "order-events";

    public void publish(OrderEvent event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event)
                .addCallback(result -> { /* success logging */ },
                        ex -> { /* failure handling/backpressure */});
    }
}
