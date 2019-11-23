package net.ddns.jmsola.customproject.model.commons.filters.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldWhere {

	public enum LogicalOperatorBetweenNames {
		AND, OR;
	}

	public enum LikeMode {
		NONE, STARTS_WITH, ENDS_WITH, CONTAINS;
	}

	public enum OperatorLikeNoneEnum {
		LOWER, LOWER_OR_EQUALS_THAN, GREATER_OR_EQUALS_THAN, GREATER_THAN, EQUALS, DISTINCT;
	}

	String[] columns() default {};

	LogicalOperatorBetweenNames logicalOperator() default LogicalOperatorBetweenNames.OR;

	LikeMode likeMode() default LikeMode.NONE;

	OperatorLikeNoneEnum operatorIfLikeNone() default OperatorLikeNoneEnum.EQUALS;

}
