package ca.ulaval.glo4002.garage.domain.appointments;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentFactoryTest {
    private static final String FROZEN_DATE = "2019-05-09";
    private static final String CLIENT_NAME = "name";
    private static final String CLIENT_PHONE = "123-123-1233";

    private AppointmentFactory factory;

    @BeforeEach
    void setUp() {
        ZoneOffset utc = ZoneOffset.UTC;
        Clock frozenClock = Clock.fixed(LocalDate.parse(FROZEN_DATE).atStartOfDay().toInstant(utc), utc.normalized());
        factory = new AppointmentFactory(frozenClock);
    }

    @Test
    void createAppointment_assignsAnAppointmentNumberBasedOnDate() {
        Appointment appointment = factory.create(CLIENT_NAME, CLIENT_PHONE);

        AppointmentNumber generatedNumber = appointment.getAppointmentNumber();
        assertThat(generatedNumber.getNumber()).contains(FROZEN_DATE);
    }

    @Test
    void createAppointment_assignsAnAppointmentNumberBasedOnName() {
        Appointment appointment = factory.create(CLIENT_NAME, CLIENT_PHONE);

        AppointmentNumber generatedNumber = appointment.getAppointmentNumber();
        assertThat(generatedNumber.getNumber()).contains(CLIENT_NAME);
    }

    @Test
    void createAppointment_addsClientToTheAppointment() {
        Appointment appointment = factory.create(CLIENT_NAME, CLIENT_PHONE);

        assertThat(appointment.getClientName()).isEqualTo(CLIENT_NAME);
        assertThat(appointment.getClientPhone()).isEqualTo(CLIENT_PHONE);
    }
}