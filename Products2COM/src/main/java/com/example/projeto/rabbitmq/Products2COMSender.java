package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Products2COMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "products";

    public void send(Product p) {
        template.convertAndSend(fanout, "", p);
        System.out.println(" [x] Sent '" + p + "'");
    }
}
