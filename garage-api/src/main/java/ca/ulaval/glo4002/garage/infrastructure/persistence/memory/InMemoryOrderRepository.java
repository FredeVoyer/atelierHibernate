package ca.ulaval.glo4002.garage.infrastructure.persistence.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> store = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Order order) {
        store.add(order);
    }

    @Override
    public List<Order> findAll() {
        return store;
    }
}
