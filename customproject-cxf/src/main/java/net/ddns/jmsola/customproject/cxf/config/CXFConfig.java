package net.ddns.jmsola.customproject.cxf.config;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;

import net.ddns.jmsola.customproject.services.config.CustomProjectServicesConfig;

@Configuration
@PropertySources({ @PropertySource(value = "file:${catalina.home}/conf/db.properties", ignoreResourceNotFound = false),
		@PropertySource(value = "file:${catalina.home}/conf/customproject-app.properties", ignoreResourceNotFound = false) })
@ComponentScan(basePackages = { "net.ddns.jmsola.customproject.cxf.*" })
@EnableAsync
@EnableAspectJAutoProxy
// Configuracion para Transaccionalidad a nivel @Service y la configuracion de
// los servicios Spring a utilizar
@ImportResource(value = { "WEB-INF/cxf-config.xml", "WEB-INF/aop-config.xml" })
@Import({ CustomProjectServicesConfig.class })
public class CXFConfig {

	@Autowired
	private Environment env;

	@Resource(name = "packageModelJPACustomProject")
	private List<String> packagesToScanJPACustomProject;

	@Bean(name = "emf")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		// Hay que añadir todos los paquetes que hay que escanear a partir de
		// los contextos de los @Service
		emf.setPackagesToScan(packagesToScanJPACustomProject.stream().toArray(String[]::new));

		emf.setDataSource(dataSource());
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		emf.setJpaProperties(jpaProperties());
		return emf;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + env.getProperty("db.host") + ":" + env.getProperty("db.port") + "/"
				+ env.getProperty("db.schema"));
		dataSource.setUsername(env.getProperty("db.user"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}
	
	/*
	 * Permite obtener un DataSource a partir del contexto JNDI (por ejemplo, en
	 * Tomcat, que haya sido definido en el fichero context.xml del servidor
	 */
	// @Bean
	// public DataSource dataSource() {
	// JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
	// jndiDataSourceLookup.setResourceRef(true);
	// return jndiDataSourceLookup.getDataSource("jdbc/oracle");
	// }

	/**
	 * Permite utilizar las anotaciones @Value con propiedades del sistema o
	 * propiedades que han sigo cargadas a partir de un fichero properties
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	Properties jpaProperties() {
		Properties jpaProps = new Properties();
		jpaProps.setProperty("hibernate.show_sql", "true");
		jpaProps.setProperty("hibernate.bytecode.use_reflection_optimizer", "true");
		jpaProps.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		jpaProps.setProperty("hibernate.hbm2ddl.auto", "validate");

		// Se añade esta propiedad, que es utilizada en JpaDao.java, para
		// modificar el comportamiento del merge.
		// Si la entidad a la que hacer merge no existe, entonces lanzará una
		// excepción si es 'true' esta propiedad
		// En otro caso, el comportamiento será el de por defecto, es decir,
		// almacenará de nuevo la entidad en base de datos
		jpaProps.setProperty("spring.datajpa.jpadao.merge.exception.ifnotexist", "false");
		return jpaProps;
	}
}
