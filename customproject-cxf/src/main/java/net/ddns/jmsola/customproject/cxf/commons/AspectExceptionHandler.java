package net.ddns.jmsola.customproject.cxf.commons;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(AspectExceptionHandler.class);

	@AfterThrowing(pointcut = "@within(javax.jws.WebService) || @within(javax.ws.rs.Path)", throwing = "ex")
	public void afterThrowingExceptionInRESTfulService(JoinPoint jointPoint, 
			Exception ex) throws UserWebServiceException {
		logger.error("Capturando exception. JoinPoint --> " + jointPoint.toString());
		logger.error("Excepción capturada --> ", ex );
		throw new UserWebServiceException(ex.getMessage());
	}

	@PostConstruct
	public void init() {
		logger.info("Iniciando " + this.getClass().getSimpleName());
	}

	@PreDestroy
	public void destroy() {
		logger.info("Destruyendo " + this.getClass().getSimpleName());
	}

}
