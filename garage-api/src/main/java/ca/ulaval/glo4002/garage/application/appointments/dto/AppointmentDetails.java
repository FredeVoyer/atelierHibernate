package ca.ulaval.glo4002.garage.application.appointments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentDetails {
    @JsonProperty("appointmentNumber")
    public String appointmentNumber;

    @JsonProperty("clientName")
    public String clientName;

    @JsonProperty("clientPhone")
    public String clientPhone;
}
