package ca.ulaval.glo4002.garage.domain.appointments;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(AppointmentNumber appointmentNumber) {
        super("Could not find appointment for number " + appointmentNumber);
    }
}
