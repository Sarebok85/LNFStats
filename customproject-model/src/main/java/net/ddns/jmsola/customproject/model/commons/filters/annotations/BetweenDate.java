package net.ddns.jmsola.customproject.model.commons.filters.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BetweenDate {

	public enum ModeEnum {
		BEFORE, BEFORE_EQUALS, AFTER_EQUALS, AFTER;
	}

	ModeEnum mode();
}
