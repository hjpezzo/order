package br.com.harley.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.harley.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

}
