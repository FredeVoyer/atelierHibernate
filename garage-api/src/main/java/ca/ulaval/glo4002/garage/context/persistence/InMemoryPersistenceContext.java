package ca.ulaval.glo4002.garage.context.persistence;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ca.ulaval.glo4002.garage.domain.DomainTransaction;
import ca.ulaval.glo4002.garage.domain.ImplicitDomainTransaction;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentRepository;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;
import ca.ulaval.glo4002.garage.infrastructure.persistence.memory.InMemoryAppointmentRepository;
import ca.ulaval.glo4002.garage.infrastructure.persistence.memory.InMemoryOrderRepository;

public class InMemoryPersistenceContext extends AbstractBinder {
    @Override
    protected void configure() {
        bind(InMemoryAppointmentRepository.class).to(AppointmentRepository.class).in(Singleton.class);
        bind(InMemoryOrderRepository.class).to(OrderRepository.class).in(Singleton.class);
	    bind(ImplicitDomainTransaction.class).to(DomainTransaction.class);
    }
}
