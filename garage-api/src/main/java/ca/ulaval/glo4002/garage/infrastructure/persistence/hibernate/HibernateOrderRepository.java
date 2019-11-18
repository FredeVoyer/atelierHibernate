package ca.ulaval.glo4002.garage.infrastructure.persistence.hibernate;

import java.util.List;

import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;

public class HibernateOrderRepository implements OrderRepository {
    @Override
    public void save(Order order) {
        // TODO lab
    }

    @Override
    public List<Order> findAll() {
        // TODO lab
        return null;
    }
}
