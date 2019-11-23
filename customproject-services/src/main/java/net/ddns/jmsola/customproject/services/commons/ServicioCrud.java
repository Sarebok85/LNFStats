package net.ddns.jmsola.customproject.services.commons;

import java.io.Serializable;
import java.util.List;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.model.commons.GenericEntity;
import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;

public abstract class ServicioCrud<T extends GenericEntity, F extends SearchFilter, ID extends Serializable>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract T findById(ID id) throws ServicioException;

	public abstract List<T> findBySearchFilter(F searchFilter) throws ServicioException;

	public abstract PaginationResult<T> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize)
			throws ServicioException;

	public abstract List<T> findAll() throws ServicioException;

	public abstract boolean exists(T entity) throws ServicioException;

	public abstract boolean existsById(ID id) throws ServicioException;

	public T saveOrUpdate(T entity, boolean validate) throws ServicioException {
		if (validate) {
			validate(entity);
		}
		return save(entity);
	}

	public T saveOrUpdate(T entity) throws ServicioException {
		return this.saveOrUpdate(entity, true);
	}

	protected abstract T save(T entity) throws ServicioException;

	public abstract void validate(T entity) throws ServicioException;

	public abstract void delete(T entity) throws ServicioException;

	public abstract void deleteRange(List<T> entity) throws ServicioException;

	public abstract void deleteById(ID id) throws ServicioException;

}
