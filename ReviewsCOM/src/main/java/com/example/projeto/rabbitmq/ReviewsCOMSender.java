package com.example.projeto.rabbitmq;


import com.example.projeto.domain.models.Review;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewsCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "reviews";

    public void send(Review p) {
        template.convertAndSend(fanout, "", p);
        System.out.println(" [x] Sent '" + p + "'");
    }
}
