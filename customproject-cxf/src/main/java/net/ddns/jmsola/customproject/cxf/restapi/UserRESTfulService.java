package net.ddns.jmsola.customproject.cxf.restapi;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import net.ddns.jmsola.customproject.cxf.commons.UserWebServiceException;
import net.ddns.jmsola.customproject.model.entities.User;
import net.ddns.jmsola.customproject.services.api.ServicioUser;
import net.ddns.jmsola.customproject.services.dto.ResponseDto;
import net.ddns.jmsola.customproject.services.dto.UserDto;
import net.ddns.jmsola.customproject.services.dto.api.ServicioUserDto;

@Path("/user")
public class UserRESTfulService {

	@Autowired
	private ServicioUserDto srvUserDto;
	
	@Autowired
	private ServicioUser srvUser;

	@GET
	@Path("/findById/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findUserById(@PathParam("id") Integer id) throws UserWebServiceException {
		UserDto result = srvUserDto.findById(id);
		
		ResponseDto response = new ResponseDto("Error");
		return (result == null ? Response.status(Response.Status.SEE_OTHER).entity(response).build()
				: Response.ok().entity(result).build());
	
	}
	
	@GET
	@Path("/saveUser")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response saveUser() throws UserWebServiceException {
		User usuario = new User();
		
		usuario.setId(0);
		usuario.setUsuario("oo");
		usuario.setPassword("oo");
		srvUser.saveOrUpdate(usuario);
		
		
		return Response.ok().build();
	
	}
	
	@GET
	@Path("/loadBD")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response loadBD() throws UserWebServiceException {
		boolean result = true;
		List<ResponseDto> listaResponse = new ArrayList();	
		
		ResponseDto response = new ResponseDto("Error1");
		listaResponse.add(response);
		
		response = new ResponseDto("Error2");
		listaResponse.add(response);
		
		return (result ? Response.status(Response.Status.SEE_OTHER).entity(listaResponse).build()
				: Response.ok().build());
	
	}
	
}
