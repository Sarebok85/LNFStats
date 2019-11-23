package net.ddns.jmsola.customproject.services.commons;

import java.io.Serializable;

public interface ServicioMensajesI18n extends Serializable {

	String getMensajeI18n(String propertyName) throws ServicioException;

	String getMensajeI18n(String propertyName, String... parameters) throws ServicioException;

}
