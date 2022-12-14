package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReviewsCOMReceiver {

    @Autowired
    private ReviewService reviewService;

    @RabbitListener(queues = "reviewsCOM")
    public void receiver(Review r){
        reviewService.create(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "reviewsCOMdel")
    public void receiverDelete(Long id) {
        reviewService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "reviewsCOMup")
    public void receiverUpdate(Review r) throws IOException {
        reviewService.updateReviewStatus(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }

    @RabbitListener(queues = "reviewsCOMprod")
    public void receiverProduct(Product p){
        Product product = reviewService.createProduct(p);
        System.out.println(" [x] Received '" + product + "'");
    }
}
