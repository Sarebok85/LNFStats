package net.ddns.jmsola.customproject.model.filters;

import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.EntityFilter;
import net.ddns.jmsola.customproject.model.entities.Fase_Competicion;

@EntityFilter(entity = Fase_Competicion.class, abbr = "fs")
public class Fase_CompeticionSearchFilter  implements SearchFilter {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isEmpty() {
		return false;
	}
}




