package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Competicion;
import net.ddns.jmsola.customproject.model.entities.Temporada;
import net.ddns.jmsola.customproject.model.filters.CompeticionSearchFilter;

public interface CompeticionDao extends Dao<Competicion, CompeticionSearchFilter, Integer> {
	Competicion findById(Integer Id);	
}
