package net.ddns.jmsola.customproject.services.commons.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.model.commons.GenericEntity;
import net.ddns.jmsola.customproject.model.commons.filters.SearchFilter;
import net.ddns.jmsola.customproject.services.commons.ServicioException;

public abstract class ServicioCrudDto<DTO extends GenericDto, TEntity extends GenericEntity, F extends SearchFilter, ID extends Serializable>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract DTO findById(ID id) throws ServicioException;

	public abstract List<DTO> findBySearchFilter(F searchFilter) throws ServicioException;

	public abstract PaginationResult<DTO> findBySearchFilterPagination(F searchFilter, int pageNumber, int pageSize)
			throws ServicioException;

	public abstract List<DTO> findAll() throws ServicioException;

	public abstract boolean exists(DTO dto) throws ServicioException;

	public abstract boolean existsById(ID id) throws ServicioException;

	public DTO saveOrUpdate(DTO entity, boolean validate) throws ServicioException {
		if (validate) {
			validate(entity);
		}
		return save(entity);
	}

	public DTO saveOrUpdate(DTO entity) throws ServicioException {
		return this.saveOrUpdate(entity, true);
	}

	protected abstract DTO save(DTO entity) throws ServicioException;

	public abstract void validate(DTO dto) throws ServicioException;

	public abstract void delete(DTO dto) throws ServicioException;

	public abstract void deleteRange(List<DTO> dtos) throws ServicioException;

	public abstract void deleteById(ID id) throws ServicioException;

	protected DTO entityToDto(TEntity entity) throws ServicioException {
		if (entity == null) {
			return null;
		}
		return this.parseEntityToDto(entity);
	}

	protected List<DTO> entitiesToDtos(List<TEntity> entities) throws ServicioException {
		// JDK >= 1.8
		return entities == null ? new ArrayList<DTO>()
				: entities.stream().map(this::entityToDto).collect(Collectors.toList());
		// JDK <= 1.7
		/*
		 * List<DTO> resultDTOs = new ArrayList<DTO>(); if (entities != null &&
		 * !entities.isEmpty()) { for (TEntity entity : entities) {
		 * resultDTOs.add(this.parseEntityToDTO(entity)); } } return resultDTOs;
		 */
	}

	protected TEntity dtoToEntity(DTO dto) throws ServicioException {
		if (dto == null) {
			return null;
		}
		return this.parseDtoToEntity(dto);
	}

	protected List<TEntity> dtosToEntities(List<DTO> dtos) throws ServicioException {
		// JDK >= 1.8
		return dtos == null ? new ArrayList<TEntity>()
				: dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());

		// JDK <= 1.7
		/*
		 * List<TEntity> resultEntities = new ArrayList<TEntity>(); if (dtos != null &&
		 * !dtos.isEmpty()) { for (DTO dto : dtos) {
		 * resultEntities.add(this.parseDtoToEntity(dto)); } } return resultEntities;
		 */
	}

	protected PaginationResult<DTO> paginationResultDtosToEntities(PaginationResult<TEntity> paginationResultEntities)
			throws ServicioException {
		PaginationResult<DTO> paginationResultDto = new PaginationResult<DTO>();
		return paginationResultDto.pageNumber(paginationResultEntities.getPageNumber())
				.pageSize(paginationResultEntities.getPageSize()).totalResult(paginationResultEntities.getTotalResult())
				.offset(paginationResultEntities.getOffset())
				.result(this.entitiesToDtos(paginationResultEntities.getResult()));

	}

	// Parsers de entitades a DTO y viceversa (Método plantilla)
	protected abstract DTO parseEntityToDto(TEntity entity) throws ServicioException;

	protected abstract TEntity parseDtoToEntity(DTO dto) throws ServicioException;
}