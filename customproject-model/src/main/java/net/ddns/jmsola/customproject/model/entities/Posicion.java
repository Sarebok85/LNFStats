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
@Table(name = "POSICION")
public class Posicion implements GenericEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_posicion")
	private Integer id;
	
	@Column(name = "posicion", nullable = false)
	private String posicion;

	 @OneToMany(mappedBy="posicion")
	 private List<Jugador> jugadores;
	 
	public Integer getId() {
		return id;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	





}
