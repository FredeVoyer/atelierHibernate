package ca.ulaval.glo4002.garage.interfaces.rest;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ca.ulaval.glo4002.garage.application.appointments.AppointmentService;
import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentDetails;
import ca.ulaval.glo4002.garage.application.appointments.dto.AppointmentRequest;
import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNumber;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentResource {
    private UriInfo uriInfo;
    private AppointmentService appointmentService;

    @Inject
    public AppointmentResource(@Context UriInfo uriInfo, AppointmentService appointmentService) {
        this.uriInfo = uriInfo;
        this.appointmentService = appointmentService;
    }

    @GET
    public List<AppointmentDetails> getAppointments() {
        return appointmentService.listAppointments();
    }

    @GET
    @Path("/{appointment-number}")
    public AppointmentDetails getAppointment(@PathParam("appointment-number") String appointmentNumber) {
        return appointmentService.findAppointment(AppointmentNumber.create(appointmentNumber));
    }

    @POST
    public Response makeAppointment(AppointmentRequest request) {
        AppointmentNumber appointmentNumber = appointmentService.makeAppointment(request);

        URI path = uriInfo.getAbsolutePathBuilder().path(appointmentNumber.toString()).build();
        return Response.created(path).build();
    }
}
