package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.ReviewDTO;
import com.example.projeto.domain.models.ReviewDTOStatus;
import com.example.projeto.domain.models.Vote;
import com.example.projeto.domain.models.VoteDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotesCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "votes_create";

    private String fanoutRev = "reviews_create";

    public void send(VoteDTO v) {
        template.convertAndSend(fanout, "", v);
        System.out.println(" [x] Sent '" + v + "'");
    }

    public void sendReview(ReviewDTO r) {
        template.convertAndSend(fanoutRev, "", r);
        System.out.println(" [x] Sent '" + r + "'");
    }
}
