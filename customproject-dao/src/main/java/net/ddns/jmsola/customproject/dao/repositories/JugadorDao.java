package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Jugador;
import net.ddns.jmsola.customproject.model.entities.Posicion;
import net.ddns.jmsola.customproject.model.filters.JugadorSearchFilter;

public interface JugadorDao extends Dao<Jugador, JugadorSearchFilter, Integer> {
		
	Jugador findByJugador(String jugador);	
	Jugador findByAlias(String jugador);	
}
