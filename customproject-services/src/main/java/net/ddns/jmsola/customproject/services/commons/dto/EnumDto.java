package net.ddns.jmsola.customproject.services.commons.dto;

public class EnumDto<E extends Enum<E>> extends IdentifierDto {
	
	private static final long serialVersionUID = 1L;
	
	private E value;
	private String description;
	
	public E getValue() {
		return value;
	}
	
	public void setValue(E value) {
		this.value = value;
	}
	
	public EnumDto<E> value(E value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public EnumDto<E> description(String description) {
		this.description = description;
		return this;
	}
	
	

}
