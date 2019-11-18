package ca.ulaval.glo4002.garage.domain.appointments;

public class DuplicateAppointmentException extends RuntimeException {
    public DuplicateAppointmentException() {
        super("This appointment already exists");
    }
}
