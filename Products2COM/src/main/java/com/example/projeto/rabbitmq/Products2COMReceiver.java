package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.Product;
import com.example.projeto.domain.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@EnableRabbit
@Configuration
public class Products2COMReceiver {

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiver(Product p){
        productService.create(p);
        System.out.println(" [x] Received '" + p + "'");
    }
}
