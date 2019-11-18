package ca.ulaval.glo4002.garage.context.persistence;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ca.ulaval.glo4002.garage.domain.DomainTransaction;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentRepository;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;
import ca.ulaval.glo4002.garage.infrastructure.persistence.hibernate.*;

public class HibernatePersistenceContext extends AbstractBinder {
    @Override
    protected void configure() {
        bind(HibernateAppointmentRepository.class).to(AppointmentRepository.class).in(Singleton.class);
        bind(HibernateOrderRepository.class).to(OrderRepository.class).in(Singleton.class);
        bind(HibernateDomainTransaction.class).to(DomainTransaction.class);
    }
}
