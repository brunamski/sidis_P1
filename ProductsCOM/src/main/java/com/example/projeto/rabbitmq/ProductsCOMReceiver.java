package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductsCOMReceiver {

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiver(Product p){
        productService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }
}
