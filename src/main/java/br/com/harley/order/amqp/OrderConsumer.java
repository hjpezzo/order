package br.com.harley.order.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.harley.order.request.BarItemMessage;
import br.com.harley.order.request.KitchenItemMessage;
import br.com.harley.order.service.OrderService;

@Component
public class OrderConsumer {

    OrderService orderService;
    RabbitTemplate rabbitTemplate;

    public OrderConsumer(OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_FROM_BAR)
    public void consumerBar(@Payload BarItemMessage barItemMessage) {
        Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
        logger.info("Recebido status de Bar do pedido: ".concat(barItemMessage.getOrderId()));
        orderService.updateStatusBar(barItemMessage.getOrderId());
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_FROM_KITCHEN)
    public void consumerKitchen(@Payload KitchenItemMessage kitchenItemMessage) {
        Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
        logger.info("Recebido status de Kitchen do pedido: ".concat(kitchenItemMessage.getOrderId()));
        orderService.updateStatusKitchen(kitchenItemMessage.getOrderId());
    }

}
