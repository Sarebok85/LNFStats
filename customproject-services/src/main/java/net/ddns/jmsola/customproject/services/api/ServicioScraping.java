package net.ddns.jmsola.customproject.services.api;

import net.ddns.jmsola.customproject.services.commons.ServicioException;

public abstract class ServicioScraping {

	public abstract void getPlantillas() throws ServicioException;
	public abstract void getResultados() throws ServicioException;
	public abstract void validarJsons() throws ServicioException;
}
