package net.ddns.jmsola.customproject.services.commons.dto;

public class ValueDescriptionDto extends IdentifierDto {

	private static final long serialVersionUID = 1L;

	private String value;
	private String description;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ValueDescriptionDto value(String value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ValueDescriptionDto description(String description) {
		this.description = description;
		return this;
	}
	
	public static ValueDescriptionDto newInstance() {
		return new ValueDescriptionDto();
	}

}
