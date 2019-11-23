package net.ddns.jmsola.customproject.model.commons.filters.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permitirá anotar una clase que herede de SearchFilter para indicar por que columnas se realizará la operación ORDER BY SQL 
 * @author José María Sola Durán
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderByMultipleColumns {
	
	OrderByColumn[] value();
}
