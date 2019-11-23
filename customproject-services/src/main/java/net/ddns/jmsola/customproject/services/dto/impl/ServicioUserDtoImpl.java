package net.ddns.jmsola.customproject.services.dto.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.dao.commons.dto.paginationresult.PaginationResult;
import net.ddns.jmsola.customproject.model.entities.User;
import net.ddns.jmsola.customproject.model.filters.UserSearchFilter;
import net.ddns.jmsola.customproject.services.api.ServicioUser;
import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.commons.ServicioValidacion;
import net.ddns.jmsola.customproject.services.dto.UserDto;
import net.ddns.jmsola.customproject.services.dto.api.ServicioUserDto;

@Service
public class ServicioUserDtoImpl extends ServicioUserDto {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ServicioUser srvUser;

	@Autowired
	private ServicioValidacion srvValidacion;

	@Override
	public UserDto findById(Integer id) throws ServicioException {
		return entityToDto(srvUser.findById(id));
	}

	@Override
	public List<UserDto> findBySearchFilter(UserSearchFilter searchFilter) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaginationResult<UserDto> findBySearchFilterPagination(UserSearchFilter searchFilter, int pageNumber,
			int pageSize) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDto> findAll() throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(UserDto dto) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected UserDto save(UserDto entity) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(UserDto dto) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UserDto dto) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRange(List<UserDto> dtos) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) throws ServicioException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected UserDto parseEntityToDto(User entity) throws ServicioException {
		UserDto userDto = new UserDto();
		userDto.setId(entity.getId());
		userDto.setUsuario(entity.getUsuario());
		userDto.setPassword(entity.getPassword());
		return userDto;
	}

	@Override
	protected User parseDtoToEntity(UserDto dto) throws ServicioException {
		// TODO Auto-generated method stub
		return null;
	}



}
