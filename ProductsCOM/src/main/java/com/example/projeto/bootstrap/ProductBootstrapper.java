package com.example.projeto.bootstrap;

import com.example.projeto.rabbitmq.RPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("bootstrap")
public class ProductBootstrapper implements CommandLineRunner {

    @Autowired
    private RPC rpc;

    @Override
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void run(String... args) throws IOException {
        rpc.helper();
    }
}
