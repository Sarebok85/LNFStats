package net.ddns.jmsola.customproject.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.dao.repositories.UserDao;
import net.ddns.jmsola.customproject.model.entities.User;
import net.ddns.jmsola.customproject.model.filters.UserSearchFilter;
import net.ddns.jmsola.customproject.services.api.ServicioUser;
import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.commons.ServicioValidacion;

@Service
public class ServicioUserImpl extends ServicioUser {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UserDao userDao;

	@Override
	public User findById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return userDao.findById(id);
	}

	@Override
	public List<User> findBySearchFilter(UserSearchFilter searchFilter) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaginationResult<User> findBySearchFilterPagination(UserSearchFilter searchFilter, int pageNumber,
			int pageSize) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findAll() throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(User entity) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected User save(User entity) throws ServicioException {
		// TODO Auto-generated method stub
		User user = userDao.findById(2);
		user.setUsuario("ss");
		user.setPassword("22");
		userDao.save(entity);
		return null;
	}

	@Override
	public void validate(User entity) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User entity) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRange(List<User> entity) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	
}
