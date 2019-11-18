package ca.ulaval.glo4002.garage.application.orders.dto;

import java.util.Collection;
import java.util.List;

import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.Part;

import static java.util.stream.Collectors.toList;

public class OrderMapper {
    public List<OrderSummary> toDto(Collection<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(toList());
    }

    public OrderSummary toDto(Order order) {
        List<Part> parts = order.listParts();
        OrderSummary summary = new OrderSummary();
        summary.orderDate = order.getDate();
        summary.referenceNumber = order.getReferenceNumber().toString();
        summary.numberOfParts = parts.size();
        summary.partNames = parts.stream()
                .map(Part::getName)
                .collect(toList());

        return summary;
    }
}
