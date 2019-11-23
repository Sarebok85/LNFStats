package net.ddns.jmsola.customproject.model.commons.filters;

import java.io.Serializable;

@FunctionalInterface
public interface EvalConditionalSearchFilter extends Serializable {

	boolean check(SearchFilter searchFilter);

}
