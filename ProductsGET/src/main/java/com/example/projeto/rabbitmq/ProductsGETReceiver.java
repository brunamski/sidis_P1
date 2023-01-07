package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

@Component
public class ProductsGETReceiver {

    @Autowired
    private ProductService productService;


    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiver(ProductDTO p) throws IOException {
        Product product = productService.create(p);
        System.out.println(" [x] Received '" + product + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiverCreateRev(ReviewDTO r) throws IOException {
        productService.createRev(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    public void receiverDeleteRev(Long reviewId) throws IOException {
        productService.deleteRev(reviewId);
        System.out.println(" [x] Deleted '" + reviewId + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receiverUpdate(ReviewDTOStatus r) {
        productService.partialUpdate(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }
}
