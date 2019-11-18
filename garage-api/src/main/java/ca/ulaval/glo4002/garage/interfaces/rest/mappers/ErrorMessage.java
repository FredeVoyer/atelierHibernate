package ca.ulaval.glo4002.garage.interfaces.rest.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
    @JsonProperty("message")
    public final String message;

    public ErrorMessage(Throwable exception) {
        this.message = exception.getMessage();
    }
}
