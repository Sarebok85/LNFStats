package net.ddns.jmsola.customproject.services.commons.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.commons.ServicioMensajesI18n;

@Service
public class ServicioMensajesI18nImpl implements ServicioMensajesI18n {

	private static final long serialVersionUID = 1L;
	private String baseNameResourceBundle;

	@PostConstruct
	private void init() {
		// Calculate by reflexion the package of i18n resources
		List<String> packageTokenizer = new ArrayList<String>(
				Arrays.asList(this.getClass().getPackage().getName().split("\\.")));
		int i = packageTokenizer.indexOf("services");
		packageTokenizer = packageTokenizer.subList(0, i + 1);
		packageTokenizer.add("resources");

		// JDK 1.8
		this.baseNameResourceBundle = String.join(".", packageTokenizer) + ".ServicesMessages";

		// JDK 1.7
		/*
		 * this.baseNameResourceBundle = new String(); String partPackage; for (i = 0; i
		 * < packageTokenizer.size(); ++i) { partPackage = packageTokenizer.get(i);
		 * baseNameResourceBundle += partPackage; if (i+1 < packageTokenizer.size()) {
		 * partPackage += "."; }
		 * 
		 * } this.baseNameResourceBundle += ".ServicesMessages";
		 */
	}

	@PreDestroy
	private void destroy() {
	}

	@Override
	public String getMensajeI18n(String propertyName) throws ServicioException {
		return getMensajeI18n(propertyName, new String[] {});
	}

	@Override
	public String getMensajeI18n(String propertyName, String... parameters) throws ServicioException {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(this.baseNameResourceBundle,
				LocaleContextHolder.getLocale());
		try {
			String value = resourceBundle.getString(propertyName);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; ++i) {
					value = value.replace("{" + i + "}", parameters[i]);
				}
			}
			return value;
		} catch (MissingResourceException e) {
			return propertyName;
		}
	}
}
