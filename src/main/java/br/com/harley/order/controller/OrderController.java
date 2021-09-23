package br.com.harley.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.harley.order.entity.Order;
import br.com.harley.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public String startOrder(@RequestBody Order order) {
        return orderService.startOrder(order);
    }
    
    @GetMapping
    public List<Order> getOrders() {
    	return orderService.getOrders();
    }
}
