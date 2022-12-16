package com.example.projeto.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private String myQueue = "votesCOM";

    private String Queue1 = "reviewsGETvote";

    private String Queue2 = "reviews2GETvote";

    private String Queue3 = "reviewsCOMvote";

    private String Queue4 = "reviews2COMvote";

    private String Queue5 = "votes2COM";

    private String exchange = "votes";


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
    public Queue queue4() {
        return new Queue(Queue4, false);
    }

    @Bean
    public Queue queue5() {
        return new Queue(Queue5, false);
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
    public Binding binding4(FanoutExchange exchange,
                            Queue queue4) {
        return BindingBuilder.bind(queue4).to(exchange);
    }

    @Bean
    public Binding binding5(FanoutExchange exchange,
                            Queue queue5) {
        return BindingBuilder.bind(queue5).to(exchange);
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
    public VotesCOMReceiver receiver() {
        return new VotesCOMReceiver();
    }

    @Bean
    public VotesCOMSender sender() {
        return new VotesCOMSender();
    }
}
