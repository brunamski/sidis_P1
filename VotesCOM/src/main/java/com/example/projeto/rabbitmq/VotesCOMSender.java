package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Vote;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotesCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "votes_create";

    public void send(Vote v) {
        template.convertAndSend(fanout, "", v);
        System.out.println(" [x] Sent '" + v + "'");
    }
}
