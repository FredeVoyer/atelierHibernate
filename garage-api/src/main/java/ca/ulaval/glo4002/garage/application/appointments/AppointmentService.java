package ca.ulaval.glo4002.garage.application.appointments;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentDetails;
import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentMapper;
import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentRequest;
import ca.ulaval.glo4002.garage.domain.DomainTransaction;
import ca.ulaval.glo4002.garage.domain.appointments.Appointment;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentFactory;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNotFoundException;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentRepository;
import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.OrderFactory;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;

public class AppointmentService {
    private final AppointmentFactory appointmentFactory;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;
	private final DomainTransaction domainTransaction;

	@Inject
    public AppointmentService(
            AppointmentFactory appointmentFactory,
            AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            OrderFactory orderFactory,
            OrderRepository orderRepository,
		    DomainTransaction domainTransaction) {
        this.appointmentFactory = appointmentFactory;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.orderFactory = orderFactory;
        this.orderRepository = orderRepository;
		this.domainTransaction = domainTransaction;
	}

    public AppointmentNumber makeAppointment(AppointmentRequest request) {
		return domainTransaction.wrapInTransaction(() -> {
			Appointment appointment = appointmentFactory.create(request.clientName, request.clientPhone);
			appointmentRepository.save(appointment);

			Order order = orderFactory.create(appointment.getAppointmentNumber(), request.partToChange);
			orderRepository.save(order);

			return appointment.getAppointmentNumber();
		});
    }

	public List<AppointmentDetails> listAppointments() {
        Collection<Appointment> appointments = appointmentRepository.findAll();
        return appointmentMapper.toDto(appointments);
    }

    public AppointmentDetails findAppointment(AppointmentNumber appointmentNumber) {
        Appointment appointment = appointmentRepository.findByNumber(appointmentNumber)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentNumber));
        return appointmentMapper.toDto(appointment);
    }
}
