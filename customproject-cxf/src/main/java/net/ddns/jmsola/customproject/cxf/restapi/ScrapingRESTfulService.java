package net.ddns.jmsola.customproject.cxf.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import net.ddns.jmsola.customproject.cxf.commons.UserWebServiceException;
import net.ddns.jmsola.customproject.services.api.ServicioScraping;
import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.dto.ResponseDto;

@Path("/Scraping")
public class ScrapingRESTfulService {

	@Autowired
	private ServicioScraping srvScraping;

	@GET
	@Path("/getPlantillas")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPlantillas() throws UserWebServiceException {
		
		try {
			
			srvScraping.getPlantillas();
			return Response.ok().entity(new ResponseDto("Ok")).build();
		
		} catch (ServicioException e) {
			return Response.status(Response.Status.SEE_OTHER).entity(new ResponseDto(e.getMessage())).build();
		}
	
	}
	
	@GET
	@Path("/getResultados")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getResultados() throws UserWebServiceException {
		
		try {
		
			srvScraping.getResultados();
			return Response.ok().entity(new ResponseDto("Ok")).build();
		
		} catch (ServicioException e) {
			return Response.status(Response.Status.SEE_OTHER).entity(new ResponseDto(e.getMessage())).build();
		}
	
	}
	
	@GET
	@Path("/validarJsons")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response validarJsons() throws UserWebServiceException {
		
		try {
		
			srvScraping.validarJsons();
			return Response.ok().entity(new ResponseDto("Ok")).build();
		
		} catch (ServicioException e) {
			return Response.status(Response.Status.SEE_OTHER).entity(new ResponseDto(e.getMessage())).build();
		}
	
	}
	

	
}
