package net.ddns.jmsola.customproject.dao.commons.api;

import java.io.Serializable;

import net.ddns.jmsola.customproject.model.commons.filters.annotations.FieldWhere.LikeMode;

public class ValueField implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Object valueField;
	private LikeMode likeMode;
	
	public ValueField(Object valueField, LikeMode likeMode) {
		super();
		this.valueField = valueField;
		this.likeMode = likeMode;
	}

	public Object getValueField() {
		return valueField;
	}
	
	public ValueField valueField(Object valueField){
		this.valueField = valueField;
		return this;
	}

	public void setValueField(Object valueField) {
		this.valueField = valueField;
	}

	public LikeMode getLikeMode() {
		return likeMode;
	}
	
	public ValueField likeMode(LikeMode likeMode){
		this.likeMode = likeMode;
		return this;
	}

	public void setLikeMode(LikeMode likeMode) {
		this.likeMode = likeMode;
	}
}
