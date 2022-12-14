package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.models.Review;
import com.example.projeto.domain.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;

@Component
public class Products2GETReceiver {

    @Autowired
    private ProductService productService;


    @RabbitListener(queues = "products2GET")
    public void receiver(Product p) throws IOException {
        productService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }

    @RabbitListener(queues = "products2GETrev")
    public void receiverCreateRev(Review r) throws IOException {
        productService.createRev(r);
        System.out.println(" [x] Received '" + r + "'");
    }

    @RabbitListener(queues = "products2GETrevdel")
    public void receiverDeleteRev(Long reviewId) throws IOException {
        productService.deleteRev(reviewId);
        System.out.println(" [x] Deleted '" + reviewId + "'");
    }

    @RabbitListener(queues = "products2GETrevup")
    public void receiverUpdate(Review r) throws IOException {
        productService.partialUpdate(r);
        System.out.println(" [x] Updated review '" + r + "'");
    }
}
