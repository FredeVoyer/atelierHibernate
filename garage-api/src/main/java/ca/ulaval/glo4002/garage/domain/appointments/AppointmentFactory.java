package ca.ulaval.glo4002.garage.domain.appointments;

import java.time.Clock;
import java.time.LocalDate;

public class AppointmentFactory {
    private final Clock clock;

    public AppointmentFactory() {
        this(Clock.systemDefaultZone());
    }

    public AppointmentFactory(Clock clock) {
        this.clock = clock;
    }

    public Appointment create(String clientName, String clientPhone) {
        AppointmentNumber number = AppointmentNumber.create(LocalDate.now(clock), clientName);
        return new Appointment(number, clientName, clientPhone);
    }
}
