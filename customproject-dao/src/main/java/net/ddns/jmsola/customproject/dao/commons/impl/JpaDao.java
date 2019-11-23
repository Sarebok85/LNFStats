package net.ddns.jmsola.customproject.dao.commons.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectDeletedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.dao.commons.api.DaoException;
import net.ddns.jmsola.customproject.dao.commons.api.ValueField;
import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.dao.commons.enums.SortOrderEnum;
import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.BetweenDate;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.EntityFilter;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.FieldWhere;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.FieldWhere.LikeMode;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.InClause;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.InClauses;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.ManualConditionWhere;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.ManualConditionWheres;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.OrderByColumn;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.OrderByMultipleColumns;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.RelationshipJoin;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.RelationshipJoin.LogicalOperatorAssociateField;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.RelationshipJoins;
import net.ddns.jmsola.customproject.model.commons.filters.annotations.SelectConfiguration;

public class JpaDao<T extends Serializable, F extends SearchFilter, ID extends Serializable>
		extends SimpleJpaRepository<T, ID> implements Dao<T, F, ID> {

	private static final String CHECK_METHOD_NAME = "check";

	private static Logger logger = LoggerFactory.getLogger(JpaDao.class);

	private final EntityManager em;
	private final boolean dialectOracleOrPostgreSQL;
	private JpaEntityInformation<T, ?> entityInformation;

	private final boolean exceptionInMergeIfEntityNotExist;

	public JpaDao(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.em = em;
		this.entityInformation = entityInformation;
		// Detect dialect of Database
		this.dialectOracleOrPostgreSQL = isDialectOracleOrPostgreSQL(em);

		Object propertyMergeException = this.em.getEntityManagerFactory().getProperties()
				.get("spring.datajpa.jpadao.merge.exception.ifnotexist");
		if (propertyMergeException != null) {
			this.exceptionInMergeIfEntityNotExist = Boolean.parseBoolean(propertyMergeException.toString());
		} else {
			this.exceptionInMergeIfEntityNotExist = false;
		}
	}

	private boolean isDialectOracleOrPostgreSQL(EntityManager em) {
		boolean isDialectOracleOrPostgreSQLAux = false;
		Object dialectObject = this.em.getEntityManagerFactory().getProperties().get("hibernate.dialect");
		if (dialectObject != null) {
			String dialectString = dialectObject.toString();
			if (!StringUtils.isEmpty(dialectString)) {
				dialectString = dialectString.toLowerCase();
				isDialectOracleOrPostgreSQLAux = dialectString.contains("oracle")
						|| dialectString.contains("postgresql");
			}
		}
		return isDialectOracleOrPostgreSQLAux;
	}

	@Override
	public List<T> findBySearchFilter(F searchFilter, String sortField, SortOrderEnum sortOrder) throws DaoException {
		try {
			TypedQuery<T> query = buildFullJPQLQueryWithParameters(searchFilter, sortField, sortOrder);
			return query.getResultList();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException e) {
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public List<T> findBySearchFilter(F searchFilter) throws DaoException {
		return this.findBySearchFilter(searchFilter, null, null);
	}

	/**
	 * Se sobre escribe el método save() para comprobar si la propiedad
	 * spring.datajpa.jpadao.merge.exception.ifnotexist tiene un valor 'true' En
	 * el caso de que sí que tenga un valor 'true', si en el momento de realizar
	 * el merge se comprueba que la entidad no existe, entonces no se realiza el
	 * merge y se lanza una excepción
	 */
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> S save(S entity) {
		if (this.entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			if (this.exceptionInMergeIfEntityNotExist && entityInformation.getId(entity) != null
					&& this.findOne((ID) entityInformation.getId(entity)) == null) {
				// Si esto se produce, quiere decir que la entidad acaba de ser
				// eliminada,
				// y además me están pidiendo una excepción, por lo tanto, tengo
				// que lanzarla
				throw new ObjectDeletedException("Entity is deleted!", entityInformation.getId(entity),
						entityInformation.getJavaType().getName());
			}
			return em.merge(entity);
		}
	}

	@Override
	public PaginationResult<T> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize,
			String sortField, SortOrderEnum sortOrder) throws DaoException {
		if (pageNumber <= 0 || pageSize <= 0) {
			throw new IllegalArgumentException(
					"El parámetro pageNumber y el parámetro pageSize deben ser mayor que cero");
		}
		try {
			TypedQuery<T> query = buildFullJPQLQueryWithParameters(searchFilter, sortField, sortOrder);
			// Calculo del offset
			PaginationResult<T> paginationResult = new PaginationResult<T>();
			paginationResult.pageNumber(pageNumber);
			paginationResult.offset((paginationResult.getPageNumber() - 1) * pageSize);
			paginationResult.pageSize(pageSize);

			// Se establece la paginación en la consulta JPQL
			query.setFirstResult(paginationResult.getOffset());
			query.setMaxResults(paginationResult.getPageSize());
			// Se ejecuta la consulta
			paginationResult.result(query.getResultList());

			// Vamos a calcular el número total de resultados
			Long numResult = countJPQLQueryWithParameters(searchFilter);
			/*
			 * Se calcula el total de resultados y de forma implicita y
			 * calculada el objeto PaginaResult calcula el numero total de
			 * páginas
			 */
			return paginationResult.totalResult(numResult);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException e) {
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public PaginationResult<T> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize)
			throws DaoException {
		return this.findBySearchFilterPagination(searchFilter, pageNumber, pageSize, null, null);
	}

	private TypedQuery<T> buildFullJPQLQueryWithParameters(F searchFilter, String sortField, SortOrderEnum sortOrder)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
		if (searchFilter == null) {
			throw new IllegalArgumentException("El argumento de entrada searchFilter no puede ser nulo");
		}
		List<ValueField> valuesParameters = new ArrayList<ValueField>();
		EntityFilter entityFilter = searchFilter.getClass().getAnnotation(EntityFilter.class);
		validateEntityFilter(entityFilter);
		// Obtener la abrebiatura de la entidad.
		String abbrEntity = (entityFilter == null ? "e" : entityFilter.abbr());
		SelectConfiguration selectConfiguration = searchFilter.getClass().getAnnotation(SelectConfiguration.class);
		// Build Initial Query
		String queryString = "SELECT "
				+ (selectConfiguration != null && selectConfiguration.distinct() ? "DISTINCT " : new String())
				+ abbrEntity + " FROM " + entityInformation.getJavaType().getName() + " " + abbrEntity;
		// Hay que añadir todos los Joins
		queryString = buildJoinsFromRelationshipJoinAnnotations(searchFilter, queryString, false);

		// Generacion de la clausula WHERE
		Field[] declaredFields = searchFilter.getClass().getDeclaredFields();
		queryString = buildWhereFromDeclaredField(searchFilter, abbrEntity, queryString, valuesParameters,
				declaredFields);
		queryString = buildManualConditionWheres(searchFilter, queryString);
		queryString = buildInClausules(searchFilter, abbrEntity, queryString);
		queryString = buildOrderBy(searchFilter, abbrEntity, queryString, sortField, sortOrder);
		logger.info(queryString); // Imprimimos la query por gusto
		TypedQuery<T> query = this.em.createQuery(queryString, entityInformation.getJavaType());
		// Set parameters
		setWhereParameteresFromQueryString(valuesParameters, query);
		return query;
	}

	@SuppressWarnings("unchecked")
	private Long countJPQLQueryWithParameters(F searchFilter)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
		if (searchFilter == null) {
			throw new IllegalArgumentException("El argumento de entrada searchFilter no puede ser nulo");
		}
		List<ValueField> valuesParameters = new ArrayList<ValueField>();
		EntityFilter entityFilter = searchFilter.getClass().getAnnotation(EntityFilter.class);
		validateEntityFilter(entityFilter);
		// Obtener la abrebiatura de la entidad.
		String abbrEntity = (entityFilter == null ? "e" : entityFilter.abbr());
		// Build Initial Query
		String queryString;
		String selectIdsEntity = buildCountDistinctSentence(abbrEntity);
		if (this.entityInformation.hasCompositeId()) {
			queryString = "SELECT DISTINCT " + selectIdsEntity;
		} else {
			queryString = "SELECT COUNT(DISTINCT " + selectIdsEntity + ")";
		}
		queryString += " FROM " + entityInformation.getJavaType().getName() + " " + abbrEntity;
		// Hay que añadir todos los Joins
		queryString = buildJoinsFromRelationshipJoinAnnotations(searchFilter, queryString, true);

		// Generacion de la clausula WHERE
		Field[] declaredFields = searchFilter.getClass().getDeclaredFields();
		queryString = buildWhereFromDeclaredField(searchFilter, abbrEntity, queryString, valuesParameters,
				declaredFields);
		queryString = buildManualConditionWheres(searchFilter, queryString);
		queryString = buildInClausules(searchFilter, abbrEntity, queryString);
		logger.info(queryString); // Imprimimos la query por gusto

		Query query;
		if (this.entityInformation.hasCompositeId()) {
			query = this.em.createQuery(queryString);

		} else {
			query = this.em.createQuery(queryString, Long.class);

		}
		// Set parameters
		setWhereParameteresFromQueryString(valuesParameters, query);

		if (this.entityInformation.hasCompositeId()) {
			List<Object[]> numResultList = query.getResultList();
			return numResultList == null || numResultList.isEmpty() ? 0L : numResultList.size();
		} else {
			Long numResult = (Long) query.getSingleResult();
			return numResult;
		}
	}

	private String buildCountDistinctSentence(String abbrEntity) {
		String result = new String();
		List<String> idAttributeNames = (List<String>) this.entityInformation.getIdAttributeNames();
		for (int i = 0; i < idAttributeNames.size(); ++i) {
			result += abbrEntity + "." + idAttributeNames.get(0);
			if (i + 1 < idAttributeNames.size()) {
				result += ", ";
			}
		}
		return result;
	}

	private void validateEntityFilter(EntityFilter entityFilter) {
		if (entityFilter == null) {
			throw new IllegalArgumentException(
					"Es necesario que el parametro de entrada SearchFilter esté anotado a nivel de clase con @EntityFilter");
		}
		Class<? extends Serializable> entity = entityFilter.entity();
		if (entity == null) {
			throw new IllegalArgumentException(
					"La anotación @EntityFilter no indica cual es la entidad asociada a través del parámetro entity");
		}
		if (entity.getAnnotation(Entity.class) == null) {
			throw new IllegalArgumentException(
					"En la anotación @EntityFilter, la clase indicada no es una entidad JPA");
		}
	}

	private String buildJoinsFromRelationshipJoinAnnotations(F searchFilter, String queryString,
			boolean isCountingQuery) {
		RelationshipJoin singleJoin = searchFilter.getClass().getAnnotation(RelationshipJoin.class);
		RelationshipJoins multipleJoins = searchFilter.getClass().getAnnotation(RelationshipJoins.class);
		if (singleJoin != null && multipleJoins != null) {
			throw new IllegalStateException(
					"Para agrupar varias @RelationshipJoin " + " utiliza solo la anotación @RelationshipJoins ");
		}
		List<RelationshipJoin> joins = null;
		if (singleJoin != null) {
			joins = Collections.singletonList(singleJoin);
		}
		if (multipleJoins != null) {
			joins = Arrays.asList(multipleJoins.value());
		}
		if (joins != null && !joins.isEmpty()) {
			boolean isNotNullAsociateFields;
			for (RelationshipJoin current : joins) {
				isNotNullAsociateFields = current.asociateField() == null || current.asociateField().length <= 0;
				if (!isNotNullAsociateFields) {
					// Quiere decir que hay elementos, hay que comprobar si
					// todos no son nulos
					Field fieldToValidate;
					String attr;

					int numAssociateFieldsDistinctNull = 0;
					// Vamos a calcular el numero de atributos que es distinto
					// null
					for (int i = 0; i < current.asociateField().length; i++) {
						try {
							attr = current.asociateField()[i];
							fieldToValidate = searchFilter.getClass().getDeclaredField(attr);
							if (fieldToValidate == null) {
								throw new IllegalArgumentException(
										"Existe una anotacion @RelationshipJoin que contiene un campo en associateFields que no es un campo del filtro");
							}
							if (!Modifier.isStatic(fieldToValidate.getModifiers())) {
								// Se obtiene el valor del campo
								fieldToValidate.setAccessible(true);
								Object valueField = fieldToValidate.get(searchFilter);
								fieldToValidate.setAccessible(false);
								if (!isNull(valueField)) {
									numAssociateFieldsDistinctNull++;
								}

							} else {
								throw new IllegalArgumentException(
										"Existe una anotacion @RelationshipJoin que contiene un campo en associateFields que es un atributo estático de clase.");
							}

						} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
								| IllegalAccessException e) {
							throw new IllegalArgumentException(
									"Existe una anotacion @RelationshipJoin que contiene un campo en associateFields que no es un campo de la entidad o que su valor no es accesible mediante reflexion");
						}
					}
					isNotNullAsociateFields = (current.logicalOperator() == LogicalOperatorAssociateField.OR
							&& numAssociateFieldsDistinctNull > 0)
							|| (current.logicalOperator() == LogicalOperatorAssociateField.AND
									&& numAssociateFieldsDistinctNull == current.asociateField().length);
				}
				if (isNotNullAsociateFields) {
					queryString += " " + current.type().value()
							+ (current.fetching() && !isCountingQuery ? " FETCH " : " ") + current.name() + " "
							+ current.abbr() + " ";
				}
			}
		}
		return queryString;
	}

	private String buildWhereFromDeclaredField(F searchFilter, String abbrEntity, String queryString,
			List<ValueField> valuesParameters, Field[] declaredFields) throws IllegalAccessException {
		boolean addFirstWhere = false;
		for (Field field : declaredFields) {
			// Si el campo no es estático entonces se trata
			if (!Modifier.isStatic(field.getModifiers())) {
				// Se obtiene el valor del campo
				field.setAccessible(true);
				Object valueField = field.get(searchFilter);
				field.setAccessible(false);
				// Si el valor del campo no es nulo habrá que añadir
				// condiciones
				if (!isNull(valueField)) {
					Class<?> type = field.getType(); // Tipo del atributo
					FieldWhere singleWhere = field.getAnnotation(FieldWhere.class);
					if (singleWhere == null) {
						throw new IllegalStateException(
								"No puede existir un atributo del filtro sin anotar con @FieldWhere");
					}
					int numParameter = valuesParameters.size() + 1;
					// De esta forma evitamos poner el WHERE 1=1
					if (!addFirstWhere) {
						queryString += " WHERE ";
						addFirstWhere = true;
					} else {
						queryString += " AND ";
					}

					String currentNameColumn, columnName;
					if (singleWhere.columns().length > 1 && declaredFields.length > 1) {
						queryString += "(";
					}
					// Podemos calcular una sola vez el operador y como se
					// construye
					String secondPartOfWhere = getSecondPartOfWhere(field, type, singleWhere, numParameter);
					for (int j = 0; j < singleWhere.columns().length; ++j) {
						currentNameColumn = singleWhere.columns()[j];
						if (currentNameColumn.isEmpty()) {
							columnName = abbrEntity + "." + field.getName();
						} else {
							// Se comprueba si esta bien construido el campo
							// antes de añadirlo a la Query
							if (!isCorrectColumnDefinition(currentNameColumn)) {
								throw new IllegalArgumentException("En la clase " + searchFilter.getClass().getName()
										+ ", en el campo " + field.getName() + " @FielWhere es incorrecto. "
										+ "El campo columns de dicha anotación contiene valores no prefijados por la abreviatura de su entidad JPA.");
							}
							columnName = currentNameColumn;
						}
						// Ya podemos trabajar con columnName
						// Sólo se pueden utilizar las funciones TRANSLATE y
						// UPPER en el caso de que la base de datos sea
						// Oracle o
						// Postgre
						queryString += (singleWhere != null && isLikeClausuleWhere(singleWhere.likeMode())
								&& this.dialectOracleOrPostgreSQL
										? "TRANSLATE(UPPER(" + columnName
												+ "), 'ÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'AEIOUAEIOUAOAEIOOAEIOUC')"
										: columnName)
								+ " " + secondPartOfWhere;
						if ((j + 1) < singleWhere.columns().length) {
							queryString += " " + singleWhere.logicalOperator().name() + " ";
						}
					}
					if (singleWhere.columns().length > 1 && declaredFields.length > 1) {
						queryString += ")";
					}
					valuesParameters.add(new ValueField(valueField,
							singleWhere != null && type.equals(String.class) ? singleWhere.likeMode() : null));
				}
			}
		}
		return queryString;
	}

	private String buildManualConditionWheres(F searchFilter, String queryString)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException {
		ManualConditionWhere singleWhere = searchFilter.getClass().getAnnotation(ManualConditionWhere.class);
		ManualConditionWheres multipleWheres = searchFilter.getClass().getAnnotation(ManualConditionWheres.class);
		if (singleWhere != null && multipleWheres != null) {
			throw new IllegalStateException(
					"Para agrupar varias @ManualConditionWhere " + " utiliza solo la anotación @ManualConditionWheres");
		}
		List<ManualConditionWhere> wheres = null;
		if (singleWhere != null) {
			wheres = Collections.singletonList(singleWhere);
		}
		if (multipleWheres != null) {
			wheres = Arrays.asList(multipleWheres.value());
		}
		if (wheres != null && !wheres.isEmpty()) {
			boolean addFirstWhere = queryString.contains(" WHERE ");

			for (ManualConditionWhere current : wheres) {
				if (current.jpqlWhereCondition() == null || current.jpqlWhereCondition().isEmpty()) {
					throw new IllegalStateException(
							"El atributo jpqlWhereCondition de la anotacion @ManualWhere no puede ser una cadena vacia");
				}
				Method methodCheck = current.condition().getMethod(CHECK_METHOD_NAME, SearchFilter.class);
				if ((boolean) methodCheck.invoke(current.condition().newInstance(), searchFilter)) {
					// De esta forma evitamos poner el WHERE 1=1
					if (!addFirstWhere) {
						queryString += " WHERE ";
						addFirstWhere = true;
					} else {
						queryString += " AND ";
					}
					queryString += "(" + current.jpqlWhereCondition() + ")";
				}
			}
		}
		return queryString;
	}

	private String buildInClausules(F searchFilter, String abbrEntity, String queryString)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException {
		InClause singleIn = searchFilter.getClass().getAnnotation(InClause.class);
		InClauses multipleIns = searchFilter.getClass().getAnnotation(InClauses.class);
		if (singleIn != null && multipleIns != null)
			throw new IllegalStateException(
					"Para agrupar varias @InClausule " + " utiliza solo la anotación @InClausules ");
		List<InClause> ins = null;
		if (singleIn != null) {
			ins = Collections.singletonList(singleIn);
		}
		if (multipleIns != null) {
			ins = Arrays.asList(multipleIns.value());
		}
		if (ins != null && !ins.isEmpty()) {
			String currentNameColumn, columnName;
			for (InClause current : ins) {
				// Si se cumple la condición se aplica el filtro IN
				// hay que ejecutar el metodo correspondiente por reflexión
				Method methodCheck = current.condition().getMethod(CHECK_METHOD_NAME, SearchFilter.class);
				if ((boolean) methodCheck.invoke(current.condition().newInstance(), searchFilter)) {
					queryString += " AND ";
					for (int j = 0; j < current.columns().length; ++j) {
						currentNameColumn = current.columns()[j];
						if (currentNameColumn.isEmpty()) {
							columnName = abbrEntity;
						} else {
							columnName = currentNameColumn;
						}
						queryString += columnName;
						if ((j + 1) < current.columns().length) {
							queryString += ", ";
						}
					}
					queryString += " IN " + "(" + current.inSubJQPLQuery() + ")";
				}
			}
		}
		return queryString;
	}

	private String getSecondPartOfWhere(Field field, Class<?> type, FieldWhere singleWhere, int numParameter) {
		String secondPartOfWhere = new String();
		if (type.equals(String.class)) {
			if (singleWhere != null) {
				if (isLikeClausuleWhere(singleWhere.likeMode())) {
					// Es un LIKE, hay que comprobar si la base de datos es
					// Oracle o
					// si no lo es
					if (this.dialectOracleOrPostgreSQL) {
						// Si lo es, podemos utilizar las funciones TRANSLATE y
						// UPPER
						secondPartOfWhere += "LIKE TRANSLATE(UPPER(?" + numParameter
								+ "), 'ÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'AEIOUAEIOUAOAEIOOAEIOUC')";
					} else {
						// En otro caso no lo utilizamos
						secondPartOfWhere += "LIKE ?" + numParameter;
					}
				} else {
					secondPartOfWhere += " = ?" + numParameter;
				}
			} else {
				throw new IllegalArgumentException(
						"La anotacion @FieldWhere en el campo " + field.getName() + " no existe");
			}
		} else if (type.equals(Date.class)) {
			secondPartOfWhere += " " + getOperatorFromBetweenDateAnnotation(field.getAnnotation(BetweenDate.class))
					+ " ?" + numParameter;
		} else {
			if (singleWhere.operatorIfLikeNone() != null) {
				switch (singleWhere.operatorIfLikeNone()) {
				case EQUALS:
					secondPartOfWhere += " = ?" + numParameter;
					break;
				case DISTINCT:
					secondPartOfWhere += " <> ?" + numParameter;
					break;
				case GREATER_OR_EQUALS_THAN:
					secondPartOfWhere += " >= ?" + numParameter;
					break;
				case GREATER_THAN:
					secondPartOfWhere += " > ?" + numParameter;
					break;
				case LOWER:
					secondPartOfWhere += " < ?" + numParameter;
					break;
				case LOWER_OR_EQUALS_THAN:
					secondPartOfWhere += " <= ?" + numParameter;
					break;
				default:
					secondPartOfWhere += " = ?" + numParameter;
					break;
				}
			} else {
				secondPartOfWhere += " = ?" + numParameter;
			}
		}
		return secondPartOfWhere;
	}

	private boolean isLikeClausuleWhere(LikeMode likeMode) {
		return likeMode != null && !likeMode.equals(LikeMode.NONE);
	}

	private boolean isNull(Object valueField) {
		if (valueField == null) {
			return true;
		} else if ((valueField.getClass().equals(String.class) && ((String) valueField).isEmpty())
				|| (valueField.getClass().equals(Long.class) && ((Long) valueField) < 0)) {
			return true;
		}
		return false;
	}

	private boolean isCorrectColumnDefinition(String currentNameColumn) {
		String[] splitDefinitionColumn = currentNameColumn.split("\\.");
		return (splitDefinitionColumn != null && splitDefinitionColumn.length >= 2);
	}

	private String getOperatorFromBetweenDateAnnotation(BetweenDate betweenDate) {
		if (betweenDate == null)
			return "=";
		switch (betweenDate.mode()) {
		case AFTER:
			return "<";
		case AFTER_EQUALS:
			return "<=";
		case BEFORE:
			return ">";
		case BEFORE_EQUALS:
			return ">=";
		default:
			return "=";
		}
	}

	private void setWhereParameteresFromQueryString(List<ValueField> valuesParameters, Query query) {
		ValueField current;
		for (int i = 0; i < valuesParameters.size(); ++i) {
			// Hay que poner el Like si es necesario
			current = valuesParameters.get(i);
			if (current.getLikeMode() == null) {
				query.setParameter(i + 1, current.getValueField());
			} else {
				switch (current.getLikeMode()) {
				case NONE:
					query.setParameter(i + 1, current.getValueField());
					break;
				case STARTS_WITH:
					query.setParameter(i + 1, "%" + current.getValueField());
					break;
				case ENDS_WITH:
					query.setParameter(i + 1, current.getValueField() + "%");
				case CONTAINS:
					query.setParameter(i + 1, "%" + current.getValueField() + "%");
					break;
				default:
					query.setParameter(i + 1, current.getValueField());
					break;
				}
			}
		}
	}

	private String buildOrderBy(F searchFilter, String abbrEntity, String queryString, String sortField,
			SortOrderEnum sortOrder) {
		if (sortField != null && !sortField.isEmpty()) {
			// Ordenacion personalizada
			sortOrder = sortOrder == null ? SortOrderEnum.ASC : sortOrder;
			queryString += " ORDER BY " + abbrEntity + "." + sortField + " " + sortOrder.toString();
		} else {
			// Ordenacion mediante Search Filter
			OrderByColumn singleOrderBy = searchFilter.getClass().getAnnotation(OrderByColumn.class);
			OrderByMultipleColumns multipleOrderBy = searchFilter.getClass()
					.getAnnotation(OrderByMultipleColumns.class);
			if (singleOrderBy != null && multipleOrderBy != null) {
				throw new IllegalStateException(
						"Para agrupar varias @OrderByColumn utiliza solo la anotación @OrderByMultipleColumns");
			}
			List<OrderByColumn> orders = null;
			if (singleOrderBy != null) {
				orders = Collections.singletonList(singleOrderBy);
			}

			if (multipleOrderBy != null) {
				orders = Arrays.asList(multipleOrderBy.value());
			}

			if (orders != null && !orders.isEmpty()) {
				queryString += " ORDER BY ";
				OrderByColumn current;
				for (int i = 0; i < orders.size(); i++) {
					current = orders.get(i);
					if (current.column() == null || current.column().isEmpty()
							|| !isCorrectColumnDefinition(current.column())) {
						throw new IllegalStateException("La anotacion @" + OrderBy.class.getSimpleName()
								+ " que se ha indicado en la clase " + searchFilter.getClass().getSimpleName()
								+ " tiene un valor en el atributo columns que corresponde con la cadena vacia o que un prefijo de entidad adecuado");
					}
					queryString += current.column() + " " + current.order().toString();
					if ((i + 1) < orders.size()) {
						queryString += ", ";
					}
				}
			}
		}
		return queryString;
	}
}