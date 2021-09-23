package br.com.harley.order.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

	public static String EXCHANGE_NAME = "order-process-exchange";
	public static final String ROUTING_KEY_ORDER_TO_BAR = "order-to-bar";
	public static final String ROUTING_KEY_ORDER_TO_KITCHEN = "order-to-kitchen";
	public static final String ROUTING_KEY_ORDER_TO_NOTIFICATION = "order-to-notification";
	public static final String ROUTING_KEY_BAR_TO_ORDER = "bar-to-order";
	public static final String ROUTING_KEY_KITCHEN_TO_ORDER = "kitchen-to-order";
	public static final String QUEUE_TO_BAR = "order-to-bar-queue";
	public static final String QUEUE_TO_KITCHEN = "order-to-kitchen-queue";
	public static final String QUEUE_TO_NOTIFICATION = "order-to-notification-queue";
	public static final String QUEUE_FROM_BAR = "bar-to-order-queue";
	public static final String QUEUE_FROM_KITCHEN = "kitchen-to-order-queue";

	@Bean
	public Exchange declareExchange() {
		return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
	}

	@Bean
	public Queue declareQueueNotification() {
		return QueueBuilder.durable(QUEUE_TO_NOTIFICATION).build();
	}

	@Bean
	public Queue declareQueueBar() {
		return QueueBuilder.durable(QUEUE_TO_BAR).build();
	}

	@Bean
	public Queue declareQueueKitchen() {
		return QueueBuilder.durable(QUEUE_TO_KITCHEN).build();
	}

	@Bean
	public Queue declareQueueFromBar() {
		return QueueBuilder.durable(QUEUE_FROM_BAR).build();
	}

	@Bean
	public Queue declareQueueFromKitchen() {
		return QueueBuilder.durable(QUEUE_FROM_KITCHEN).build();
	}

	@Bean
	public Binding declareBindingOrderToBar(Exchange exchange) {
		return BindingBuilder.bind(declareQueueBar()).to(exchange).with(ROUTING_KEY_ORDER_TO_BAR).noargs();
	}

	@Bean
	public Binding declareBindingOrderToKitchen(Exchange exchange) {
		return BindingBuilder.bind(declareQueueKitchen()).to(exchange).with(ROUTING_KEY_ORDER_TO_KITCHEN).noargs();
	}

	@Bean
	public Binding declareBindingOrderToNotification(Exchange exchange) {
		return BindingBuilder.bind(declareQueueNotification()).to(exchange).with(ROUTING_KEY_ORDER_TO_NOTIFICATION)
				.noargs();
	}

	@Bean
	public Binding declareBindingBarToOrder(Exchange exchange) {
		return BindingBuilder.bind(declareQueueFromBar()).to(exchange).with(ROUTING_KEY_BAR_TO_ORDER).noargs();
	}

	@Bean
	public Binding declareBindingKitchenToOrder(Exchange exchange) {
		return BindingBuilder.bind(declareQueueFromKitchen()).to(exchange).with(ROUTING_KEY_KITCHEN_TO_ORDER).noargs();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final var rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(RabbitConfig.EXCHANGE_NAME);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}
