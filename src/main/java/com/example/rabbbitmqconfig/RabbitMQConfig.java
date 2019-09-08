package com.example.rabbbitmqconfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	private static final String MY_QUEUE = "MyQueue";
	
	/*when application is launched automatically 
	below queue will be created programatically. --due to @Bean.
	We can also use @Queue annotation below instead of @Bean
	*/
	@Bean 
	Queue myQueue() {
		return new Queue(MY_QUEUE, true);
	}
	
	/*Exchange creation*/
	@Bean
	Exchange myExchange() {
		return ExchangeBuilder.topicExchange("MyTopicExchnage")
				.durable(true)
				.build();
	}
	
	/*create a binding between exchange and queue - from exchange to Q*/
	@Bean
	Binding myBinding() {
		return BindingBuilder
				.bind(myQueue())
				.to(myExchange())
				.with("topicRouting")
				.noargs();
	}
	//connection to rabbitMQ broker
	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = 
				new CachingConnectionFactory("localhost");
		cachingConnectionFactory.setUsername("guest");
		cachingConnectionFactory.setPassword("guest");
		return cachingConnectionFactory;
	}
	
	//connect everything
	@Bean
	MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer simpleMessageListenerContainer = 
				new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		simpleMessageListenerContainer.setQueues(myQueue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQMesageListener());
		return simpleMessageListenerContainer;
	}
}
