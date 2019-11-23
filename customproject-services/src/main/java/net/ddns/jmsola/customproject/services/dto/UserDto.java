package net.ddns.jmsola.customproject.services.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import net.ddns.jmsola.customproject.services.commons.dto.GenericDto;

public class UserDto implements GenericDto  {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	@NotBlank
	@Size(max = 250)
	private String usuario;
	@NotBlank
	@Size(max = 250)
	private String password;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
