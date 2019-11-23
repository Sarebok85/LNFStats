package net.ddns.jmsola.customproject.model.filters;

import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.EntityFilter;
import net.ddns.jmsola.customproject.model.entities.Temporada;

@EntityFilter(entity = Temporada.class, abbr = "t")
public class TemporadaSearchFilter  implements SearchFilter {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isEmpty() {
		return false;
	}
}




