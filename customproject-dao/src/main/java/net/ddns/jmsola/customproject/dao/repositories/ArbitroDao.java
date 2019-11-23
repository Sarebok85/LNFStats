package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.Arbitro;
import net.ddns.jmsola.customproject.model.filters.ArbitroSearchFilter;

public interface ArbitroDao extends Dao<Arbitro, ArbitroSearchFilter, Integer> {
		
	Arbitro findByArbitro(String arbitro);

	
}
