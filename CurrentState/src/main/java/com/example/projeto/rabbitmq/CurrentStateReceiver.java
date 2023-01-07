package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ReviewDTO;
import com.example.projeto.domain.models.ReviewDTOStatus;
import com.example.projeto.domain.models.VoteDTO;
import com.example.projeto.domain.services.CurrentStateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CurrentStateReceiver {

    @Autowired
    private CurrentStateService service;

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiverProduct(ProductDTO p) throws IOException {
        service.createProduct(p);
        System.out.println(" [x] Received '" + p + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiverReview(ReviewDTO r) throws IOException {
        service.createReview(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    public void receiverVote(VoteDTO v) throws IOException {
        service.createVote(v);
        System.out.println(" [x] Received '" + v + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receiverDelete(Long id) {
        service.deleteById(id);
        System.out.println(" [x] Deleted review '" + id + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiverUpdate(ReviewDTOStatus r) throws IOException {
        service.partialUpdate(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }
}
