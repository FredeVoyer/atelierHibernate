package ca.ulaval.glo4002.garage.domain.orders;

import java.util.List;

public interface OrderRepository {
    void save(Order order);

    List<Order> findAll();
}
