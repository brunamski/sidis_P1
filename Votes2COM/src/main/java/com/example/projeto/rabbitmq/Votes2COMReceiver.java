package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.services.VoteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Votes2COMReceiver {

    @Autowired
    private VoteService voteService;

    @RabbitListener(queues = "votesCOM")
    public void receiver(Vote v){
        voteService.create(v);
        System.out.println(" [x] Received '" + v + "'");
    }

    @RabbitListener(queues = "votes2COMrev")
    public void receiver(Review r){
        voteService.create(r);
        System.out.println(" [x] Received '" + r + "'");
    }
}
