package ca.ulaval.glo4002.garage.infrastructure.persistence.hibernate;

import java.util.Collection;
import java.util.Optional;

import ca.ulaval.glo4002.garage.domain.appointments.Appointment;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentRepository;

public class HibernateAppointmentRepository implements AppointmentRepository {
    @Override
    public void save(Appointment appointment) {
        // TODO lab. Ne pas oublier de lancer DuplicateAppointment si le numéro existe déjà.
    }

    @Override
    public Collection<Appointment> findAll() {
        // TODO lab
        return null;
    }

    @Override
    public Optional<Appointment> findByNumber(AppointmentNumber appointmentNumber) {
        // TODO lab
        return null;
    }
}
