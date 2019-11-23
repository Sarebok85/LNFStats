package net.ddns.jmsola.customproject.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import net.ddns.jmsola.customproject.model.commons.GenericEntity;

@Entity
@Table(name = "FASE_COMPETICION")
public class Fase_Competicion implements GenericEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fase_competicion")
	private Integer id;
	
	@Column(name = "fase_competicion", nullable = false)
	private String fase_competicion;

	@OneToMany(mappedBy="fase_competicion")
	private List<Partido> partidos;
	
	public List<Partido> getPartidos() {
		return partidos;
	}

	public void setPartidos(List<Partido> partidos) {
		this.partidos = partidos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFase_competicion() {
		return fase_competicion;
	}

	public void setFase_competicion(String fase_competicion) {
		this.fase_competicion = fase_competicion;
	}




}
