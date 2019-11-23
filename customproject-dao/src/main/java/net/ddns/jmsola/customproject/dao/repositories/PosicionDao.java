package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Equipo;
import net.ddns.jmsola.customproject.model.entities.Posicion;
import net.ddns.jmsola.customproject.model.filters.PosicionSearchFilter;

public interface PosicionDao extends Dao<Posicion, PosicionSearchFilter, Integer> {
		
	Posicion findByPosicion(String posicion);	
	
}
