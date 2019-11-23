package net.ddns.jmsola.customproject.model.filters;

import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.EntityFilter;
import net.ddns.jmsola.customproject.model.entities.Arbitro;

@EntityFilter(entity = Arbitro.class, abbr = "a")
public class ArbitroSearchFilter  implements SearchFilter {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isEmpty() {
		return false;
	}
}




