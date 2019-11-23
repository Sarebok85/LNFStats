package net.ddns.jmsola.customproject.services.commons;

import java.io.Serializable;

public interface ServicioValidacion extends Serializable {

	void isNull(String fieldLabel, Object valueField) throws ServicioException;

}
