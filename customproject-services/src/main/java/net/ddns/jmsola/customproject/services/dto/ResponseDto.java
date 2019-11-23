package net.ddns.jmsola.customproject.services.dto;

import net.ddns.jmsola.customproject.services.commons.dto.GenericDto;

public class ResponseDto implements GenericDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mensaje;

	public ResponseDto(String mensaje) {
		super();
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
