package net.ddns.jmsola.customproject.cxf.commons;

import javax.xml.ws.WebFault;

import org.apache.commons.lang3.exception.ExceptionUtils;

import net.ddns.jmsola.customproject.cxf.dto.responseerror.ResponseError;

@WebFault
public class UserWebServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserWebServiceException() {
		super();
	}

	public UserWebServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserWebServiceException(String message) {
		super(message);
	}

	public ResponseError getFaultInfo() {
		ResponseError responseError = new ResponseError();
		responseError.setMessage(this.getMessage());
		responseError.setStackTrace(ExceptionUtils.getStackTrace(this));
		return responseError;

	}
}
