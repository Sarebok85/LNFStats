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
@Table(name = "EQUIPO")
public class Equipo implements GenericEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "id_equipo")
	private Integer id;
	
	@Column(name = "equipo", nullable = false)
	private String equipo;

	@Column(name = "imagen", nullable = false)
	private String imagen;
	
	@Column(name = "ID_LNFS", nullable = false)
	private Integer lnfs;

	public Integer getLnfs() {
		return lnfs;
	}

	public void setLnfs(Integer lnfs) {
		this.lnfs = lnfs;
	}

	@OneToMany(mappedBy="equipo")
	private List<Plantilla> plantillas;
	
	public List<Plantilla> getPlantillas() {
		return plantillas;
	}

	public void setPlantillas(List<Plantilla> plantillas) {
		this.plantillas = plantillas;
	}

	public List<Partido> getPartidos_v() {
		return partidos_v;
	}

	public void setPartidos_v(List<Partido> partidos_v) {
		this.partidos_v = partidos_v;
	}

	public List<Partido> getPartidos_l() {
		return partidos_l;
	}

	public void setPartidos_l(List<Partido> partidos_l) {
		this.partidos_l = partidos_l;
	}

	@OneToMany(mappedBy="equipo_v")
	private List<Partido> partidos_v;
	
	@OneToMany(mappedBy="equipo_l")
	private List<Partido> partidos_l;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}



	


}
