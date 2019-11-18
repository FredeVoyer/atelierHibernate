package ca.ulaval.glo4002.garage.context.application;

import ca.ulaval.glo4002.garage.application.appointments.AppointmentService;
import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentMapper;
import ca.ulaval.glo4002.garage.application.orders.OrderService;
import ca.ulaval.glo4002.garage.application.orders.dto.OrderMapper;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentFactory;
import ca.ulaval.glo4002.garage.domain.orders.OrderFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationContext extends AbstractBinder {
    @Override
    protected void configure() {
        configureAppointments();
        configureOrders();
    }

    private void configureAppointments() {
        bindAsContract(AppointmentService.class);
        bindAsContract(AppointmentFactory.class);
        bindAsContract(AppointmentMapper.class);
    }

    private void configureOrders() {
        bindAsContract(OrderFactory.class);
        bindAsContract(OrderService.class);
        bindAsContract(OrderMapper.class);
    }
}
