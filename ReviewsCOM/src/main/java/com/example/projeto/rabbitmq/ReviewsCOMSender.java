package com.example.projeto.rabbitmq;


import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.models.ReviewDTO;
import com.example.projeto.domain.models.ReviewDTOStatus;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewsCOMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "reviews_create";

    private String fanoutdel = "reviews_delete";

    private String fanoutup = "reviews_update";

    public void send(ReviewDTO r) {
        template.convertAndSend(fanout,"",r);
        System.out.println(" [x] Sent '" + r + "'");
    }

    public void sendId(Long id) {
        template.convertAndSend(fanoutdel,"",id);
        System.out.println(" [x] Sent '" + id + "'");
    }

    public void sendUpdate(ReviewDTOStatus r) {
        template.convertAndSend(fanoutup,"",r);
        System.out.println(" [x] Sent '" + r + "'");
    }
}
