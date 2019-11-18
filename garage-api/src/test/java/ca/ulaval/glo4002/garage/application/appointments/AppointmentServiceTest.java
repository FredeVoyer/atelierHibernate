package ca.ulaval.glo4002.garage.application.appointments;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.garage.application.appointments.dto.*;
import ca.ulaval.glo4002.garage.domain.ImplicitDomainTransaction;
import ca.ulaval.glo4002.garage.domain.appointments.*;
import ca.ulaval.glo4002.garage.domain.orders.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    static final AppointmentNumber NEW_APPOINTMENT_NUMBER = AppointmentNumber.create("test 1");
    static final AppointmentNumber AN_APPOINTMENT_NUMBER = AppointmentNumber.create("test 2");

    @Mock
    AppointmentFactory appointmentFactory;
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    AppointmentMapper appointmentMapper;
    @Mock
    OrderFactory orderFactory;
    @Mock
    OrderRepository orderRepository;

    AppointmentRequest request = new AppointmentRequest() {{
        clientName = "name";
        clientPhone = "123-123-1233";
        partToChange = "motor";
    }};

	AppointmentService appointmentService;

	@BeforeEach
	void setUp() {
		appointmentService = new AppointmentService(
				appointmentFactory,
				appointmentRepository,
				appointmentMapper,
				orderFactory,
				orderRepository,
				new ImplicitDomainTransaction()
		);
	}

	@Test
    void makeAnAppointment_createsANewAppointment() {
        givenNewAppointmentCanBeCreated();

        appointmentService.makeAppointment(request);

        verify(appointmentFactory).create(request.clientName, request.clientPhone);
    }

    @Test
    void makeAnAppointment_savesTheNewAppointment() {
        Appointment newAppointment = givenNewAppointmentCanBeCreated();

        appointmentService.makeAppointment(request);

        verify(appointmentRepository).save(newAppointment);
    }

    @Test
    void makeAnAppointment_createsAOrderForThePart() {
        givenNewAppointmentCanBeCreated();

        appointmentService.makeAppointment(request);

        verify(orderFactory).create(NEW_APPOINTMENT_NUMBER, request.partToChange);
    }

    @Test
    void makeAnAppointment_savesThePartOrder() {
        givenNewAppointmentCanBeCreated();
        Order newOrder = mock(Order.class);
        willReturn(newOrder).given(orderFactory).create(any(), anyString());

        appointmentService.makeAppointment(request);

        verify(orderRepository).save(newOrder);
    }

    @Test
    void makeAnAppointment_generatesAnAppointmentNumber() {
        givenNewAppointmentCanBeCreated();

        AppointmentNumber generatedNumber = appointmentService.makeAppointment(request);

        assertThat(generatedNumber).isSameAs(NEW_APPOINTMENT_NUMBER);
    }

    @Test
    void listAppointments_fetchesAllAppointmentsInRepository() {
        appointmentService.listAppointments();

        verify(appointmentRepository).findAll();
    }

    @Test
    void listAppointments_transformsAllAppointmentsToAppointmentDetails() {
        List<Appointment> savedAppointments = givenAppointmentsExist();
        ArrayList<AppointmentDetails> expectedAppointmentDetails = new ArrayList<>();
        willReturn(expectedAppointmentDetails).given(appointmentMapper).toDto(savedAppointments);

        List<AppointmentDetails> actualDetails = appointmentService.listAppointments();

        assertThat(actualDetails).isSameAs(expectedAppointmentDetails);
    }

    @Test
    void findAppointment_searchesForTheAppointmentInTheRepository() {
        givenAnAppointmentCanBeFound();

        appointmentService.findAppointment(AN_APPOINTMENT_NUMBER);

        verify(appointmentRepository).findByNumber(AN_APPOINTMENT_NUMBER);
    }

    @Test
    void findAppointment_transformsTheAppointmentToAppointmentDetails() {
        Appointment appointment = givenAnAppointmentCanBeFound();
        AppointmentDetails expectedDetails = new AppointmentDetails();
        willReturn(expectedDetails).given(appointmentMapper).toDto(appointment);

        AppointmentDetails actualDetails = appointmentService.findAppointment(AN_APPOINTMENT_NUMBER);

        assertThat(actualDetails).isSameAs(expectedDetails);
    }

    @Test
    void findAppointment_createsAnErrorIfTheAppointmentDoesNotExist() {
        willReturn(Optional.empty()).given(appointmentRepository).findByNumber(any());

        assertThrows(
                AppointmentNotFoundException.class,
                () -> appointmentService.findAppointment(AN_APPOINTMENT_NUMBER)
        );
    }

    private Appointment givenAnAppointmentCanBeFound() {
        Appointment appointment = mock(Appointment.class);
        willReturn(Optional.of(appointment)).given(appointmentRepository).findByNumber(any());
        return appointment;
    }

    private List<Appointment> givenAppointmentsExist() {
        List<Appointment> savedAppointments = new ArrayList<>();
        willReturn(savedAppointments).given(appointmentRepository).findAll();
        return savedAppointments;
    }

    private Appointment givenNewAppointmentCanBeCreated() {
        Appointment newAppointment = mock(Appointment.class);
        willReturn(newAppointment).given(appointmentFactory).create(anyString(), anyString());
        willReturn(NEW_APPOINTMENT_NUMBER).given(newAppointment).getAppointmentNumber();
        return newAppointment;
    }
}
