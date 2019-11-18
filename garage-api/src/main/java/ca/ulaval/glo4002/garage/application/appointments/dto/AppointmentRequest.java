package ca.ulaval.glo4002.garage.application.appointments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentRequest {
    @JsonProperty("clientName")
    public String clientName;

    @JsonProperty("clientPhone")
    public String clientPhone;

    @JsonProperty("partToChange")
    public String partToChange;
}
