package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Reviews2COMReceiver {

    @Autowired
    private ReviewService reviewService;

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiver(Review r){
        Review review = reviewService.create(r);
        System.out.println(" [x] Received '" + review + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receiverDelete(Long id) {
        reviewService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiverUpdate(Review r) throws IOException {
        reviewService.updateReviewStatus(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiverProduct(ProductDTO p) throws IOException {
        reviewService.createProduct(p);
        System.out.println(" [x] Received '" + p + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receiverVote(VoteDTO v){
        reviewService.create(v);
        System.out.println(" [x] Received '" + v + "'");
    }
}
