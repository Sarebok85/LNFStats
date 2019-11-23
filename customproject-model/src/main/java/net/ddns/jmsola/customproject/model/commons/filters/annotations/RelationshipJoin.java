package net.ddns.jmsola.customproject.model.commons.filters.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface RelationshipJoin {
	public enum LogicalOperatorAssociateField {
		AND, OR;
	}

	public enum JoinType {

		JOIN("JOIN"), LEFT_JOIN("LEFT JOIN"), RIGTH_JOIN("RIGTH JOIN");
		private final String value;

		JoinType(String v) {
			value = v;
		}

		public String value() {
			return value;
		}

		public static JoinType fromValue(String v) {
			for (JoinType c : JoinType.values()) {
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

	String name();

	String abbr();

	JoinType type() default JoinType.JOIN;

	boolean fetching() default false;

	/**
	 * Este campo indicará que el JOIN se hará si todos los campos del filtro
	 * indicados en este array no son nulos
	 * 
	 * @return Lista de campos que no deben ser nulos para añadir la condición
	 */

	String[] asociateField() default {};

	LogicalOperatorAssociateField logicalOperator() default LogicalOperatorAssociateField.AND;

}