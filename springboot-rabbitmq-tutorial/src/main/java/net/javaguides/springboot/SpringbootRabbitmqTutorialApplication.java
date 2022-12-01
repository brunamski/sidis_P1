package net.javaguides.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
public class SpringbootRabbitmqTutorialApplication {

	public static void main(String[] args) {
		System.out.println("Hello");
		SpringApplication.run(SpringbootRabbitmqTutorialApplication.class, args);
	}

}
