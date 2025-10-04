package dev.thangngo.lmssoftdreams.consumer;

import dev.thangngo.lmssoftdreams.config.RabbitMQConfig;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class BorrowCreatedConsumer {

    @RabbitListener(queues = RabbitMQConfig.BORROW_CREATED_QUEUE)
    public void handleBorrowCreated(BorrowResponse borrow) {
        System.out.println("Received Borrow Created Event: " + borrow);
    }
}
