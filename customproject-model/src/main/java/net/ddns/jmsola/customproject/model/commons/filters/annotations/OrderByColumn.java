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
public @interface OrderByColumn {
	
	public enum OrderType {

		ASC("ASC"), DESC("DESC");
		private final String value;

		OrderType(String v) {
			value = v;
		}

		public String value() {
			return value;
		}

		public static OrderType fromValue(String v) {
			for (OrderType c : OrderType.values()) {
				if (c.value.equals(v)) {
					return c;
				}
			}
			throw new IllegalArgumentException(v);
		}

		@Override
		public String toString() {
			return this.value;
		}
	}
	
	/**
	 * Columna del OrderBy
	 * @return
	 */
	String column();
	
	/**
	 * Orden ASC o DESC. Por defecto, ASC
	 * @return
	 */
	OrderType order() default OrderType.ASC;
}
