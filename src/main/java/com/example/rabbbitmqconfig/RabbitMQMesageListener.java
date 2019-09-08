package com.example.rabbbitmqconfig;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class RabbitMQMesageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		System.out.println("Message : "+new String(message.getBody()));
	}

}
