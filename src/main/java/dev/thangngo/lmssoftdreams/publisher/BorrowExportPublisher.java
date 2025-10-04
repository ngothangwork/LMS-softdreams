package dev.thangngo.lmssoftdreams.publisher;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowSearchRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static dev.thangngo.lmssoftdreams.config.RabbitMQConfig.BORROW_EXPORT_QUEUE;

@Component
public class BorrowExportPublisher {

    private final RabbitTemplate rabbitTemplate;

    public BorrowExportPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendExportRequest(BorrowSearchRequest request) {
        System.out.println("Gửi yêu cầu export PDF lên queue...");
        rabbitTemplate.convertAndSend(BORROW_EXPORT_QUEUE, request);
    }
}
