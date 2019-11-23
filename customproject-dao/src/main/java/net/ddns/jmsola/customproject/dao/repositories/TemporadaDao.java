package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Equipo;
import net.ddns.jmsola.customproject.model.entities.Temporada;
import net.ddns.jmsola.customproject.model.filters.TemporadaSearchFilter;

public interface TemporadaDao extends Dao<Temporada, TemporadaSearchFilter, Integer> {
	Temporada findByAnho(Integer Id);	
}
