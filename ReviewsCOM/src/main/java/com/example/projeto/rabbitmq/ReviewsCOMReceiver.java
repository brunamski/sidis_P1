package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReviewsCOMReceiver {

    @Autowired
    private ReviewService reviewService;

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiver(ReviewDTO r) throws IOException {
        reviewService.createDTO(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receiverDelete(Long id) {
        reviewService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiverUpdate(ReviewDTOStatus r) throws IOException {
        reviewService.partialUpdateDTO(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiverProduct(ProductDTO p) throws IOException {
        Product product = reviewService.createProduct(p);
        System.out.println(" [x] Received '" + product + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receiverVote(VoteDTO v){
        reviewService.create(v);
        System.out.println(" [x] Received '" + v + "'");
    }
}
