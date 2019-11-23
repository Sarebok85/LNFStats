package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Fase_Competicion;
import net.ddns.jmsola.customproject.model.filters.Fase_CompeticionSearchFilter;

public interface Fase_CompeticionDao extends Dao<Fase_Competicion, Fase_CompeticionSearchFilter, Integer> {
		
	Fase_Competicion findById(Integer id_fase_competicion);	
	
}
