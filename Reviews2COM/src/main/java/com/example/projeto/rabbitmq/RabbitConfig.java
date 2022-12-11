package com.example.projeto.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private String myQueue = "reviews2COM";

    private String myQueue2 = "reviews2COMdel";

    private String Queue1 = "reviewsGET";

    private String Queue2 = "reviews2GET";

    private String Queue3 = "reviewsCOM";

    private String Queue4 = "reviewsGETdel";

    private String Queue5 = "reviews2GETdel";

    private String Queue6 = "reviewsCOMdel";

    private String Queue7 = "reviewsGETup";

    private String Queue8 = "reviews2GETup";

    private String Queue9 = "reviewsCOMup";

    private String Queue10 = "productsGETrev";

    private String Queue11 = "products2GETrev";

    private String Queue12 = "productsGETrevdel";

    private String Queue13 = "products2GETrevdel";

    private String exchange = "reviews2";

    private String exchangedel = "reviews2del";


    @Bean
    @Qualifier("create")
    public FanoutExchange fanout() {
        return new FanoutExchange(exchange);
    }

    @Bean
    @Qualifier("delete")
    public FanoutExchange fanoutDel() {
        return new FanoutExchange(exchangedel);
    }

    @Bean
    public Queue myQueue() {
        return new Queue(myQueue, false);
    }

    @Bean
    public Queue myQueue2() {
        return new Queue(myQueue2, false);
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
    public Queue queue6() {
        return new Queue(Queue6, false);
    }

    @Bean
    public Queue queue10() {
        return new Queue(Queue10, false);
    }

    @Bean
    public Queue queue11() {
        return new Queue(Queue11, false);
    }

    @Bean
    public Queue queue12() {
        return new Queue(Queue12, false);
    }

    @Bean
    public Queue queue13() {
        return new Queue(Queue13, false);
    }

    @Bean
    public Binding binding1(@Qualifier("create") FanoutExchange exchange,
                            Queue queue1) {
        return BindingBuilder.bind(queue1).to(exchange);
    }

    @Bean
    public Binding binding2(@Qualifier("create") FanoutExchange exchange,
                            Queue queue2) {
        return BindingBuilder.bind(queue2).to(exchange);
    }

    @Bean
    public Binding binding3(@Qualifier("create") FanoutExchange exchange,
                            Queue queue3) {
        return BindingBuilder.bind(queue3).to(exchange);
    }

    @Bean
    public Binding binding4(@Qualifier("delete") FanoutExchange exchangedel,
                            Queue queue4) {
        return BindingBuilder.bind(queue4).to(exchangedel);
    }

    @Bean
    public Binding binding5(@Qualifier("delete") FanoutExchange exchangedel,
                            Queue queue5) {
        return BindingBuilder.bind(queue5).to(exchangedel);
    }

    @Bean
    public Binding binding6(@Qualifier("delete") FanoutExchange exchangedel,
                            Queue queue6) {
        return BindingBuilder.bind(queue6).to(exchangedel);
    }

    @Bean
    public Binding binding10(@Qualifier("create") FanoutExchange exchange,
                            Queue queue10) {
        return BindingBuilder.bind(queue10).to(exchange);
    }

    @Bean
    public Binding binding11(@Qualifier("create") FanoutExchange exchange,
                            Queue queue11) {
        return BindingBuilder.bind(queue11).to(exchange);
    }

    @Bean
    public Binding binding12(@Qualifier("delete") FanoutExchange exchangedel,
                            Queue queue12) {
        return BindingBuilder.bind(queue12).to(exchangedel);
    }

    @Bean
    public Binding binding13(@Qualifier("delete") FanoutExchange exchangedel,
                            Queue queue13) {
        return BindingBuilder.bind(queue13).to(exchangedel);
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
    public Reviews2COMReceiver receiver() {
        return new Reviews2COMReceiver();
    }

    @Bean
    public Reviews2COMSender sender() {
        return new Reviews2COMSender();
    }
}
