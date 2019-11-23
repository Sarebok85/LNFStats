package net.ddns.jmsola.customproject.dao.commons.api;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.dao.commons.enums.SortOrderEnum;
import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;

@NoRepositoryBean
public interface Dao<T extends Serializable, F extends SearchFilter, ID extends Serializable>
		extends CrudRepository<T, ID>, PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

	// Se añaden nuestro métodos compartidos por todos los Repository
	
	List<T> findBySearchFilter(F searchFilter, String sortField, SortOrderEnum sortOrder) throws DaoException;
	
	List<T> findBySearchFilter(F searchFilter) throws DaoException;
	
	//Metodo con paginacion  y ordenacion personalizada, fuera de las indicadas en la anotacion @OrderByColumn
	PaginationResult<T> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize,
			String sortField, SortOrderEnum sortOrder) throws DaoException;

	//Metodo para paginacion
	PaginationResult<T> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize)
			throws DaoException;
}
