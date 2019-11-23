package net.ddns.jmsola.customproject.services.config;

import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.ddns.jmsola.customproject.dao.commons.impl.JpaDao;

@Configuration
@ComponentScan(basePackages = { "net.ddns.jmsola.customproject.services.*", "net.ddns.jmsola.customproject.dao.*" })
@EnableTransactionManagement
/*
 * OJO: En los packageToScan del EntityManager ni en el basePackages del
 * EnableJpaRepositories se puede poner los paquetes con .*, no funciona y lo
 * sobreentiende ya.
 */
@EnableJpaRepositories(basePackages = "net.ddns.jmsola.customproject.dao.repositories", entityManagerFactoryRef = "emf", transactionManagerRef = "transactionManager", repositoryBaseClass = JpaDao.class)
public class CustomProjectServicesConfig {

	/* Model Mapper que nos permitirá realizar un mapeo directo entidad-DTO */
	@Bean
	public ModelMapper modelMapper() {
		/*
		 * Si queremos personalizar los mapeos, podemos hacerlo en este punto, para los
		 * DTOs que consideremos http://modelmapper.org/user-manual/property-mapping/
		 */
		return new ModelMapper();
	}

	@Bean(name = "packageModelJPACustomProject")
	public List<String> packageModelJPACustomProject() {
		return Collections.singletonList("net.ddns.jmsola.customproject.model.entities");
	}
}
