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
import org.springframework.security.core.parameters.P;
import org.springframework.util.ErrorHandler;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private String myQueue = "productsGET";

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("products");
    }

    @Bean
    public Queue myQueue() {
        return new Queue("productsGET");
    }

    @Bean
    public Queue queue1() {
        return new Queue("productsCOM");
    }

    @Bean
    public Queue queue2() {
        return new Queue("products2COM");
    }

    @Bean
    public Queue queue3() {
        return new Queue("products2GET");
    }

    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanout);
    }

    @Bean
    public Binding binding2(FanoutExchange fanout,
                            Queue queue2) {
        return BindingBuilder.bind(queue2).to(fanout);
    }

    @Bean
    public Binding binding3(FanoutExchange fanout,
                            Queue queue3) {
        return BindingBuilder.bind(queue3).to(fanout);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setDefaultReceiveQueue(myQueue);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyAddress(myQueue().getName());
        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }

    @Bean
    public ProductsGETReceiver receiver() {
        return new ProductsGETReceiver();
    }

    @Bean
    public ProductsGETSender sender() {
        return new ProductsGETSender();
    }
}
