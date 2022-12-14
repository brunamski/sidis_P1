package com.example.projeto.rabbitmq;


import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReviewsGETReceiver {

    @Autowired
    private ReviewService reviewService;


    @RabbitListener(queues = "reviewsGET")
    public void receiver(Review p) {
        reviewService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }

    @RabbitListener(queues = "reviewsGETdel")
    public void receiverDelete(Long id) {
        reviewService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "reviewsGETup")
    public void receiverUpdate(Review r) throws IOException {
        reviewService.updateReviewStatus(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }
}
