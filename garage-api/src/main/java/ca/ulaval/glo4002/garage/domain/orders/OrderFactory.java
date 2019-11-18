package ca.ulaval.glo4002.garage.domain.orders;

import java.time.Clock;
import java.time.LocalDateTime;

import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;

public class OrderFactory {
    private final Clock clock;

    public OrderFactory() {
        this(Clock.systemDefaultZone());
    }

    public OrderFactory(Clock clock) {
        this.clock = clock;
    }

    public Order create(AppointmentNumber referenceNumber, String partName) {
        Part part = new Part(1, partName);
        Order order = new Order(LocalDateTime.now(clock), referenceNumber);
        order.addPart(part);

        return order;
    }
}
