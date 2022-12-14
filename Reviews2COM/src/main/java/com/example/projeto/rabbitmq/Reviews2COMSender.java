package com.example.projeto.rabbitmq;


import com.example.projeto.domain.models.Review;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Reviews2COMSender {

    @Autowired
    private AmqpTemplate template;

    private String fanout = "reviews2";

    private String fanoutdel = "reviews2del";

    private String fanoutup = "reviewsup";

    public void send(Review p) {
        template.convertAndSend(fanout,"",p);
        System.out.println(" [x] Sent '" + p + "'");
    }

    public void sendId(Long id) {
        template.convertAndSend(fanoutdel,"",id);
        System.out.println(" [x] Sent '" + id + "'");
    }

    public void sendUpdate(Review r) {
        template.convertAndSend(fanoutup,"",r);
        System.out.println(" [x] Sent '" + r + "'");
    }
}
