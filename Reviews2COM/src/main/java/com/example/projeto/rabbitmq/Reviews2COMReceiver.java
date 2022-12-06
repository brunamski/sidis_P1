package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Reviews2COMReceiver {

    @Autowired
    private ReviewService reviewService;

    @RabbitListener(queues = "reviews2COM")
    public void receiver(Review p){
        reviewService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }
}
