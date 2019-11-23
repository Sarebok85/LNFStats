package net.ddns.jmsola.customproject.model.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import net.ddns.jmsola.customproject.model.commons.GenericEntity;

@Entity
@Table(name = "PARTIDO")
public class Partido implements GenericEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_partido")
	private Integer id;
	
	@Column(name = "goles_equipo_l")
	private Integer goles_equipo_l;
	
	@Column(name = "goles_equipo_v")
	private Integer goles_equipo_v;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@OneToMany(mappedBy="partido")
	private List<Gol_Partido> goles_partido;
	
	@ManyToOne
	@JoinColumn(name="id_temporada")
	private Temporada temporada;
	
	@ManyToOne
	@JoinColumn(name="id_equipo_v", referencedColumnName="id_equipo")
	private Equipo equipo_v;
	
	@ManyToOne
	@JoinColumn(name="id_equipo_l", referencedColumnName="id_equipo")
	private Equipo equipo_l;
	
	@ManyToOne
	@JoinColumn(name="id_competicion")
	private Competicion competicion;
	
	@ManyToOne
	@JoinColumn(name="id_fase_competicion")
	private Fase_Competicion fase_competicion;
	
	@ManyToOne
	@JoinColumn(name="id_arbitro_1", referencedColumnName="id_arbitro")
	private Arbitro arbitro_1;
	
	@ManyToOne
	@JoinColumn(name="id_arbitro_2", referencedColumnName="id_arbitro")
	private Arbitro arbitro_2;
	
	public List<Gol_Partido> getGoles_partido() {
		return goles_partido;
	}

	public void setGoles_partido(List<Gol_Partido> goles_partido) {
		this.goles_partido = goles_partido;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public Equipo getEquipo_v() {
		return equipo_v;
	}

	public void setEquipo_v(Equipo equipo_v) {
		this.equipo_v = equipo_v;
	}

	public Equipo getEquipo_l() {
		return equipo_l;
	}

	public void setEquipo_l(Equipo equipo_l) {
		this.equipo_l = equipo_l;
	}

	public Competicion getCompeticion() {
		return competicion;
	}

	public void setCompeticion(Competicion competicion) {
		this.competicion = competicion;
	}

	public Fase_Competicion getFase_competicion() {
		return fase_competicion;
	}

	public void setFase_competicion(Fase_Competicion fase_competicion) {
		this.fase_competicion = fase_competicion;
	}

	public Arbitro getArbitro_1() {
		return arbitro_1;
	}

	public void setArbitro_1(Arbitro arbitro_1) {
		this.arbitro_1 = arbitro_1;
	}

	public Arbitro getArbitro_2() {
		return arbitro_2;
	}

	public void setArbitro_2(Arbitro arbitro_2) {
		this.arbitro_2 = arbitro_2;
	}

	public Integer getGoles_equipo_l() {
		return goles_equipo_l;
	}

	public void setGoles_equipo_l(Integer goles_equipo_l) {
		this.goles_equipo_l = goles_equipo_l;
	}

	public Integer getGoles_equipo_v() {
		return goles_equipo_v;
	}

	public void setGoles_equipo_v(Integer goles_equipo_v) {
		this.goles_equipo_v = goles_equipo_v;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
