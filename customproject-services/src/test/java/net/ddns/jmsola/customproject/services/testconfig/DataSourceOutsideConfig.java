package net.ddns.jmsola.customproject.services.testconfig;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile("outside")
public class DataSourceOutsideConfig {
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://jmsolarasp.ddns.net:3306/CUSTOMPROJECT");
		dataSource.setUsername("customproject");
		dataSource.setPassword("customproject");
		return dataSource;
	}

}
