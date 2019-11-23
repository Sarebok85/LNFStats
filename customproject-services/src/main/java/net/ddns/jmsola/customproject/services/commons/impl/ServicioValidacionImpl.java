package net.ddns.jmsola.customproject.services.commons.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.commons.ServicioMensajesI18n;
import net.ddns.jmsola.customproject.services.commons.ServicioValidacion;

@Service
public class ServicioValidacionImpl implements ServicioValidacion {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ServicioMensajesI18n srvMensajes;

	@Override
	public void isNull(String fieldLabel, Object valueField) throws ServicioException {
		if (valueField == null || (valueField.getClass().equals(String.class) && ((String) valueField).isEmpty())) {
			throw new ServicioException(srvMensajes.getMensajeI18n("nulo", fieldLabel));
		}
	}
}
