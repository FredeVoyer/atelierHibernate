package ca.ulaval.glo4002.garage.domain.orders;

public class Part {
    private final int quantity;
    private final String name;

    public Part(int quantity, String name) {
        this.quantity = quantity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
