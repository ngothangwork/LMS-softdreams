package dev.thangngo.lmssoftdreams.publisher;

import dev.thangngo.lmssoftdreams.config.RabbitMQConfig;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BorrowCreatePublisher {
    private final RabbitTemplate rabbitTemplate;

    public BorrowCreatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateRequest(BorrowCreateRequest request) {
        System.out.println("Gửi yêu cầu tạo sách");
        rabbitTemplate.convertAndSend(RabbitMQConfig.BORROW_CREATED_EXCHANGE, RabbitMQConfig.BORROW_CREATED_ROUTING_KEY,  request);
    }
}
