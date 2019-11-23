package net.ddns.jmsola.customproject.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.dao.repositories.EquipoDao;
import net.ddns.jmsola.customproject.dao.repositories.JugadorDao;
import net.ddns.jmsola.customproject.dao.repositories.PlantillaDao;
import net.ddns.jmsola.customproject.dao.repositories.PosicionDao;
import net.ddns.jmsola.customproject.dao.repositories.TemporadaDao;
import net.ddns.jmsola.customproject.model.entities.Plantilla;
import net.ddns.jmsola.customproject.model.filters.PlantillaSearchFilter;
import net.ddns.jmsola.customproject.services.api.ServicioPlantilla;
import net.ddns.jmsola.customproject.services.commons.ServicioException;

@Service
public class ServicioPlantillaImpl extends ServicioPlantilla {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EquipoDao equipoDao;

	@Autowired
	private PosicionDao posicionDao;

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private TemporadaDao temporadaDao;

	@Autowired
	private PlantillaDao plantillaDao;

	

	@Override
	public Plantilla findById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plantilla> findBySearchFilter(PlantillaSearchFilter searchFilter) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaginationResult<Plantilla> findBySearchFilterPagination(PlantillaSearchFilter searchFilter, int pageNumber,
			int pageSize) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plantilla> findAll() throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Plantilla entity) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Plantilla save(Plantilla entity) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(Plantilla entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Plantilla entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRange(List<Plantilla> entity) throws ServicioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub

	}

}
