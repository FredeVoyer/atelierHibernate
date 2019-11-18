package ca.ulaval.glo4002.garage.domain.orders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderFactoryTest {
    static final AppointmentNumber AN_APPOINTMENT_NUMBER = AppointmentNumber.create("test");
    static final LocalDateTime FROZEN_CLOCK_TIME = LocalDateTime.now();
    static final String A_PART_NAME = "steering";

    OrderFactory factory;

    @BeforeEach
    void setUp() {
        ZoneOffset utc = ZoneOffset.UTC;
        Clock frozenClock = Clock.fixed(FROZEN_CLOCK_TIME.toInstant(utc), utc.normalized());
        factory = new OrderFactory(frozenClock);
    }

    @Test
    void createOrder_addsOneOfThePartToTheOrder() {
        Order order = factory.create(AN_APPOINTMENT_NUMBER, A_PART_NAME);

        assertThat(order.listParts()).hasSize(1);
        assertThat(order.listParts().get(0).getName()).isEqualTo(A_PART_NAME);
        assertThat(order.listParts().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void createOrder_referencesToTheAppointmentNumber() {
        Order order = factory.create(AN_APPOINTMENT_NUMBER, A_PART_NAME);

        assertThat(order.getReferenceNumber()).isSameAs(AN_APPOINTMENT_NUMBER);
    }

    @Test
    void createOrder_savesOrderDate() {
        Order order = factory.create(AN_APPOINTMENT_NUMBER, A_PART_NAME);

        assertThat(order.getDate()).isEqualTo(FROZEN_CLOCK_TIME);
    }
}