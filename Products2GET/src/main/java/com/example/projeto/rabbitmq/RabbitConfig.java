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
import org.springframework.context.annotation.Profile;

@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("products_create");
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange reviewsFanout() {
        return new FanoutExchange("reviews_create");
    }

    @Bean
    public Queue autoDeleteQueue3() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange reviewsUpFanout() {
        return new FanoutExchange("reviews_update");
    }

    @Bean
    public Queue autoDeleteQueue4() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange reviewsDelFanout() {
        return new FanoutExchange("reviews_delete");
    }

    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    @Bean
    public Binding binding2(FanoutExchange reviewsFanout,
                            Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(reviewsFanout);
    }

    @Bean
    public Binding binding3(FanoutExchange reviewsUpFanout,
                            Queue autoDeleteQueue3) {
        return BindingBuilder.bind(autoDeleteQueue3).to(reviewsUpFanout);
    }

    @Bean
    public Binding binding4(FanoutExchange reviewsDelFanout,
                            Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(reviewsDelFanout);
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
    public Products2GETReceiver receiver() {
        return new Products2GETReceiver();
    }

    @Bean
    public Queue queueReceiver(){
        return new Queue("rpc_prod_receiver");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("rpc_prod");
    }

    @Bean
    public Binding bindingReceiver(DirectExchange directExchange, Queue queueReceiver){
        return BindingBuilder.bind(queueReceiver).to(directExchange).with("key");
    }


    @Bean
    public Queue queueReceiver2(){
        return new Queue("rpc_rev_receiver");
    }

    @Bean
    public DirectExchange directExchange2(){
        return new DirectExchange("rpc_rev");
    }

    @Bean
    public Binding bindingReceiver2(DirectExchange directExchange2, Queue queueReceiver2){
        return BindingBuilder.bind(queueReceiver2).to(directExchange2).with("key");
    }
}


