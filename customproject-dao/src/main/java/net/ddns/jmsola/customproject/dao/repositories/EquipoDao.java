package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Equipo;
import net.ddns.jmsola.customproject.model.entities.User;
import net.ddns.jmsola.customproject.model.filters.EquipoSearchFilter;

public interface EquipoDao extends Dao<Equipo, EquipoSearchFilter, Integer> {
		
	Equipo findById(Integer Id);
	
	Equipo findByLnfs(Integer Lnfs);
	
	
}
