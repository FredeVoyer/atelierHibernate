package ca.ulaval.glo4002.garage.interfaces.rest.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ca.ulaval.glo4002.garage.domain.appointments.DuplicateAppointmentException;

@Provider
public class DuplicateAppointmentExceptionMapper implements ExceptionMapper<DuplicateAppointmentException> {
    @Override
    public Response toResponse(DuplicateAppointmentException exception) {
        return Response.status(Status.BAD_REQUEST).entity(new ErrorMessage(exception)).build();
    }
}
