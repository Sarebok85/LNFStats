package net.ddns.jmsola.customproject.cxf.restapi;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;

import net.ddns.jmsola.customproject.cxf.commons.UserWebServiceException;
import net.ddns.jmsola.customproject.cxf.dto.responseerror.ResponseError;

@Provider
public class UserExceptionRESTfulHandler implements ExceptionMapper<UserWebServiceException> {

	@Override
	public Response toResponse(UserWebServiceException e) {

		ResponseError responseError = new ResponseError();
		responseError.setMessage(e.getMessage());
		responseError.setStackTrace(ExceptionUtils.getStackTrace(e));

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
				entity(responseError)
				.build();
	}

}
