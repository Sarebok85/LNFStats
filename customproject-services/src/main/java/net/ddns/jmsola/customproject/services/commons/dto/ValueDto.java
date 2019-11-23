package net.ddns.jmsola.customproject.services.commons.dto;

public class ValueDto extends IdentifierDto {

	private static final long serialVersionUID = 1L;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ValueDto value(String value) {
		this.value = value;
		return this;
	}

	public static ValueDto newInstance() {
		return new ValueDto();
	}

}
