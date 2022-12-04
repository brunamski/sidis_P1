package com.example.projeto.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ErrorHandler;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private String myQueue = "productsCOM";

    private String Queue1 = "productsGET";

    private String Queue2 = "products2GET";

    private String Queue3 = "products2COM";

    private String exchange = "products";


    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(exchange);
    }

    @Bean
    public Queue myQueue() {
        return new Queue(myQueue, false);
    }

    @Bean
    public Queue queue1() {
        return new Queue(Queue1, false);
    }

    @Bean
    public Queue queue2() {
        return new Queue(Queue2, false);
    }

    @Bean
    public Queue queue3() {
        return new Queue(Queue3, false);
    }

    @Bean
    public Binding binding1(FanoutExchange exchange,
                            Queue queue1) {
        return BindingBuilder.bind(queue1).to(exchange);
    }

    @Bean
    public Binding binding2(FanoutExchange exchange,
                            Queue queue2) {
        return BindingBuilder.bind(queue2).to(exchange);
    }

    @Bean
    public Binding binding3(FanoutExchange exchange,
                            Queue queue3) {
        return BindingBuilder.bind(queue3).to(exchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ProductsCOMReceiver receiver() {
        return new ProductsCOMReceiver();
    }

    @Bean
    public ProductsCOMSender sender() {
        return new ProductsCOMSender();
    }
}
