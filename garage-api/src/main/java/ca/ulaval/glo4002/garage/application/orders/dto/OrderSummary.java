package ca.ulaval.glo4002.garage.application.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderSummary {
    @JsonProperty("orderDate")
    public LocalDateTime orderDate;

    @JsonProperty("referenceNumber")
    public String referenceNumber;

    @JsonProperty("numberOfParts")
    public int numberOfParts;

    @JsonProperty("partNames")
    public List<String> partNames;
}
