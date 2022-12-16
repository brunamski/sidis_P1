package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.services.VoteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void receiver(Review r){
        Review review = voteService.create(r);
        System.out.println(" [x] Received '" + r + "'");
    }
}
