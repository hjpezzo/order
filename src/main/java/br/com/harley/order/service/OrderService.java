package br.com.harley.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.harley.order.amqp.RabbitConfig;
import br.com.harley.order.entity.BarItem;
import br.com.harley.order.entity.KitchenItem;
import br.com.harley.order.entity.Order;
import br.com.harley.order.enums.Status;
import br.com.harley.order.repository.OrderRepository;
import br.com.harley.order.request.BarItemMessage;
import br.com.harley.order.request.KitchenItemMessage;

@Service
public class OrderService {

	OrderRepository orderRepository;
	RabbitTemplate rabbitTemplate;

	public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
		this.orderRepository = orderRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	@CacheEvict(cacheNames = "orders", allEntries = true)
	public String startOrder(Order order) {
		String id = UUID.randomUUID().toString();
		order.setId(id);
		List<BarItem> barItens = order.getBarItens();
		for (BarItem barItem : barItens) {
			barItem.setOrder(order);
		}

		List<KitchenItem> kitchenItens = order.getKitchenItens();
		for (KitchenItem kitchenItem : kitchenItens) {
			kitchenItem.setOrder(order);
		}

		if (order.getBarItens() == null || order.getBarItens().size() == 0) {
			order.setStatusBar(Status.DONE);
		} else {
			order.setStatusBar(Status.PREPARING);
		}
		if (order.getKitchenItens() == null || order.getKitchenItens().size() == 0) {
			order.setStatusKitchen(Status.DONE);
		} else {
			order.setStatusKitchen(Status.PREPARING);
		}

		order = saveOrder(order);

		if (order.getStatusBar().equals(Status.PREPARING)) {
			rabbitTemplate.convertAndSend(RabbitConfig.ROUTING_KEY_ORDER_TO_BAR,
					new BarItemMessage(order.getId(), order.getBarItens()));
		}
		if (order.getStatusKitchen().equals(Status.PREPARING)) {
			rabbitTemplate.convertAndSend(RabbitConfig.ROUTING_KEY_ORDER_TO_KITCHEN,
					new KitchenItemMessage(order.getId(), order.getKitchenItens()));
		}

		return id;
	}

	private Order saveOrder(Order order) {
		if (order.getStatusBar().equals(Status.DONE) && order.getStatusKitchen().equals(Status.DONE)) {
			rabbitTemplate.convertAndSend(RabbitConfig.ROUTING_KEY_ORDER_TO_NOTIFICATION, order);
		}

		return orderRepository.save(order);
	}

	@Cacheable(cacheNames = "orders", key = "#root.method.name")
	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

	public Order getOrder(String orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}

	public void updateStatusBar(String orderId) {
		Order order = getOrder(orderId);
        if (order != null) {
			order.setStatusBar(Status.DONE);
	        saveOrder(order);
        }
	}

	public void updateStatusKitchen(String orderId) {
		Order order = getOrder(orderId);
        if (order != null) {
	        order.setStatusKitchen(Status.DONE);
	        saveOrder(order);
        }
	}
}
