package ca.ulaval.glo4002.garage.application.appointments.dto;

import java.util.Collection;
import java.util.List;

import ca.ulaval.glo4002.garage.domain.appointments.Appointment;

import static java.util.stream.Collectors.toList;

public class AppointmentMapper {
    public List<AppointmentDetails> toDto(Collection<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(toList());
    }

    public AppointmentDetails toDto(Appointment appointment) {
        AppointmentDetails details = new AppointmentDetails();
        details.appointmentNumber = appointment.getAppointmentNumber().toString();
        details.clientName = appointment.getClientName();
        details.clientPhone = appointment.getClientPhone();
        return details;
    }
}
