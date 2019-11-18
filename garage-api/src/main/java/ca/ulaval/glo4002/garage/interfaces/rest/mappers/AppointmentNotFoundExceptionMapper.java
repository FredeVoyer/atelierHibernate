package ca.ulaval.glo4002.garage.interfaces.rest.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ca.ulaval.glo4002.garage.domain.appointments.AppointmentNotFoundException;

@Provider
public class AppointmentNotFoundExceptionMapper implements ExceptionMapper<AppointmentNotFoundException> {
    @Override
    public Response toResponse(AppointmentNotFoundException exception) {
        return Response.status(Status.NOT_FOUND).entity(new ErrorMessage(exception)).build();
    }
}
