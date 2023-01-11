package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.VoteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VotesCOMReceiver {

    @Autowired
    private VoteService voteService;

    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receiver(VoteDTO v){
        voteService.create(v);
        System.out.println(" [x] Received '" + v + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiverReview(ReviewDTO r) throws IOException {
        voteService.create(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receiverDelete(Long id) {
        voteService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiverUpdate(ReviewDTOStatus r) {
        voteService.partialUpdate(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiverProd(ProductDTO p) throws IOException {
        voteService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }
}
