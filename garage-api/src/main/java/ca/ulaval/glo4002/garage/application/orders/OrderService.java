package ca.ulaval.glo4002.garage.application.orders;

import java.util.List;

import javax.inject.Inject;

import ca.ulaval.glo4002.garage.application.orders.dto.OrderMapper;
import ca.ulaval.glo4002.garage.application.orders.dto.OrderSummary;
import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;

public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Inject
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderSummary> listOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDto(orders);
    }
}
