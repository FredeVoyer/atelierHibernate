package ca.ulaval.glo4002.garage.domain.appointments;

import java.util.Collection;
import java.util.Optional;

public interface AppointmentRepository {
    void save(Appointment appointment);

    Collection<Appointment> findAll();

    Optional<Appointment> findByNumber(AppointmentNumber appointmentNumber);
}
