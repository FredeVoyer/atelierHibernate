package ca.ulaval.glo4002.garage.domain.appointments;

public class Appointment {
    private final AppointmentNumber appointmentNumber;
    private final String clientName;
    private final String clientPhone;

    public Appointment(AppointmentNumber appointmentNumber, String clientName, String clientPhone) {
        this.appointmentNumber = appointmentNumber;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public AppointmentNumber getAppointmentNumber() {
        return appointmentNumber;
    }
}
