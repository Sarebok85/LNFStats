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
@Table(name = "ARBITRO")
public class Arbitro implements GenericEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_arbitro")
	private Integer id;
	
	@Column(name = "arbitro", nullable = false)
	private String arbitro;
	
	@OneToMany(mappedBy="arbitro_1")
	private List<Partido> partidos_1;
	
	@OneToMany(mappedBy="arbitro_2")
	private List<Partido> partidos_2;


	public List<Partido> getPartidos_1() {
		return partidos_1;
	}

	public void setPartidos_1(List<Partido> partidos_1) {
		this.partidos_1 = partidos_1;
	}

	public List<Partido> getPartidos_2() {
		return partidos_2;
	}

	public void setPartidos_2(List<Partido> partidos_2) {
		this.partidos_2 = partidos_2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArbitro() {
		return arbitro;
	}

	public void setArbitro(String arbitro) {
		this.arbitro = arbitro;
	}





}
