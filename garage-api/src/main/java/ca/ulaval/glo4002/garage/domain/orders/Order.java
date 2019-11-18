package ca.ulaval.glo4002.garage.domain.orders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;

public class Order {
    private final LocalDateTime date;
    private final AppointmentNumber referenceNumber;
    private final List<Part> parts = new ArrayList<>();

    public Order(LocalDateTime date, AppointmentNumber referenceNumber) {
        this.date = date;
        this.referenceNumber = referenceNumber;
    }

    public void addPart(Part part) {
        parts.add(part);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AppointmentNumber getReferenceNumber() {
        return referenceNumber;
    }

    public List<Part> listParts() {
        return parts;
    }
}
