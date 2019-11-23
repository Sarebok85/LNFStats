package net.ddns.jmsola.customproject.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.dao.repositories.EquipoDao;
import net.ddns.jmsola.customproject.dao.repositories.JugadorDao;
import net.ddns.jmsola.customproject.dao.repositories.PlantillaDao;
import net.ddns.jmsola.customproject.model.entities.Equipo;
import net.ddns.jmsola.customproject.model.filters.EquipoSearchFilter;
import net.ddns.jmsola.customproject.services.api.ServicioEquipo;
import net.ddns.jmsola.customproject.services.api.ServicioPlantilla;
import net.ddns.jmsola.customproject.services.commons.ServicioException;

@Service
public class ServicioEquipoImpl extends ServicioEquipo {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EquipoDao equipoDao;

	@Autowired
	private ServicioPlantilla srvPlantilla;

	@Autowired
	private PlantillaDao plantillaDao;

	@Autowired
	private JugadorDao jugadorDao;

	@Override
	public Equipo findById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return equipoDao.findById(id);
	}

	@Override
	public boolean existsById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Equipo save(Equipo entity) throws ServicioException {
		// TODO Auto-generated method stub
		equipoDao.save(entity);
		return entity;
	}

	@Override
	public void deleteById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Equipo> findBySearchFilter(EquipoSearchFilter searchFilter) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaginationResult<Equipo> findBySearchFilterPagination(EquipoSearchFilter searchFilter, int pageNumber,
			int pageSize) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Equipo> findAll() throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Equipo entity) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Equipo entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Equipo entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRange(List<Equipo> entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	
}
