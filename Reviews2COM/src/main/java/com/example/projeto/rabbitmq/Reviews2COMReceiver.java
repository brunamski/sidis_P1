package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
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
    public void receiver(Review r){
        Review review = reviewService.create(r);
        System.out.println(" [x] Received '" + review + "'");
    }

    @RabbitListener(queues = "reviews2COMdel")
    public void receiverDelete(Long id) {
        reviewService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "reviews2COMprod")
    public void receiverProduct(Product p){
        Product product = reviewService.createProduct(p);
        System.out.println(" [x] Received '" + product + "'");
    }
}
