package net.ddns.jmsola.customproject.services.commons.dto;

public class IdentifierDto implements GenericDto {

	private static final long serialVersionUID = 1L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IdentifierDto id(String id) {
		this.id = id;
		return this;
	}
	
	public static IdentifierDto newInstance() {
		return new IdentifierDto();
	}
}
