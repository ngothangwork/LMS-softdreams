package dev.thangngo.lmssoftdreams.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BORROW_CREATED_QUEUE = "borrow.created.queue";
    public static final String BORROW_CREATED_EXCHANGE = "borrow.created.exchange";
    public static final String BORROW_CREATED_ROUTING_KEY = "borrow.created.routing.key";


    public static final String BORROW_EXPORT_QUEUE = "borrow.export.pdf.queue";


    @Bean
    public Queue borrowCreatedQueue() {
        return new Queue(BORROW_CREATED_QUEUE, true);
    }

    @Bean
    public DirectExchange  borrowCreatedExchange() {
        return new DirectExchange(BORROW_CREATED_EXCHANGE);
    }

    @Bean
    public Queue borrowExportQueue() {
        return new Queue(BORROW_EXPORT_QUEUE, true);
    }

    @Bean
    public Binding borrowCreatedBinding(Queue borrowCreatedQueue, DirectExchange borrowCreatedExchange) {
        return BindingBuilder.bind(borrowCreatedQueue).to(borrowCreatedExchange).with(BORROW_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}

