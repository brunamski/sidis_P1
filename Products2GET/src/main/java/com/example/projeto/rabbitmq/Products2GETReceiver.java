package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
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
}
