package net.ddns.jmsola.customproject.model.filters;

import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.EntityFilter;
import net.ddns.jmsola.customproject.model.entities.Equipo;

@EntityFilter(entity = Equipo.class, abbr = "e")
public class EquipoSearchFilter  implements SearchFilter {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isEmpty() {
		return false;
	}
}




