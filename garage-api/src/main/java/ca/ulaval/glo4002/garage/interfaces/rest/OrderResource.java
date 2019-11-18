package ca.ulaval.glo4002.garage.interfaces.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca.ulaval.glo4002.garage.application.orders.OrderService;
import ca.ulaval.glo4002.garage.application.orders.dto.OrderSummary;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    private OrderService orderService;

    @Inject
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    public List<OrderSummary> listSummaries() {
        return orderService.listOrders();
    }
}
