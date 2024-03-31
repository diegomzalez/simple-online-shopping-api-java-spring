package com.diegomzalez.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.diegomzalez.orderservice.dto.OrderLineItemsDto;
import com.diegomzalez.orderservice.dto.OrderRequest;
import com.diegomzalez.orderservice.model.Order;
import com.diegomzalez.orderservice.model.OrderLineItems;
import com.diegomzalez.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItemds = orderRequest.getOrderLineItems().stream().map(this::mapToDto).toList();
        Order order = Order.builder().number(UUID.randomUUID().toString()).build();
        order.setOrderLineItemsList(orderLineItemds);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = OrderLineItems.builder().price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity()).skuCode(orderLineItemsDto.getSkuCode()).build();
        return null;
    }
}
