package com.example.projeto.rabbitmq;


import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Reviews2GETReceiver {

    @Autowired
    private ReviewService reviewService;


    @RabbitListener(queues = "reviews2GET")
    public void receiver(Review p) throws IOException {
        reviewService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }
}
