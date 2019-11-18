package ca.ulaval.glo4002.garage.infrastructure.persistence.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ca.ulaval.glo4002.garage.domain.appointments.Appointment;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentRepository;
import ca.ulaval.glo4002.garage.domain.appointments.DuplicateAppointmentException;

public class InMemoryAppointmentRepository implements AppointmentRepository {
    private final Map<AppointmentNumber, Appointment> store = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void save(Appointment appointment) {
        if (store.containsKey(appointment.getAppointmentNumber())) {
            throw new DuplicateAppointmentException();
        }

        store.put(appointment.getAppointmentNumber(), appointment);
    }

    @Override
    public Collection<Appointment> findAll() {
        return store.values();
    }

    @Override
    public Optional<Appointment> findByNumber(AppointmentNumber appointmentNumber) {
        return Optional.ofNullable(store.get(appointmentNumber));
    }
}
