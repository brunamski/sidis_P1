package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.services.VoteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VotesCOMReceiver {

    @Autowired
    private VoteService voteService;

    @RabbitListener(queues = "votesCOM")
    public void receiver(Vote v){
        voteService.create(v);
        System.out.println(" [x] Received '" + v + "'");
    }

    @RabbitListener(queues = "votesCOMrev")
    public void receiverReview(Review r){
        Review review = voteService.create(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "votesCOMrevdel")
    public void receiverDelete(Long id) {
        voteService.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "votesCOMrevup")
    public void receiverUpdate(Review r) {
        voteService.partialUpdate(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }
}
