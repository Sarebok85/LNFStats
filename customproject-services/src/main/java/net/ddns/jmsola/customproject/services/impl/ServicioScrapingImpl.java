package net.ddns.jmsola.customproject.services.impl;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ddns.jmsola.customproject.dao.repositories.ArbitroDao;
import net.ddns.jmsola.customproject.dao.repositories.CompeticionDao;
import net.ddns.jmsola.customproject.dao.repositories.EquipoDao;
import net.ddns.jmsola.customproject.dao.repositories.Fase_CompeticionDao;
import net.ddns.jmsola.customproject.dao.repositories.Gol_PartidoDao;
import net.ddns.jmsola.customproject.dao.repositories.JugadorDao;
import net.ddns.jmsola.customproject.dao.repositories.PartidoDao;
import net.ddns.jmsola.customproject.dao.repositories.PlantillaDao;
import net.ddns.jmsola.customproject.dao.repositories.PosicionDao;
import net.ddns.jmsola.customproject.dao.repositories.TemporadaDao;
import net.ddns.jmsola.customproject.model.entities.Arbitro;
import net.ddns.jmsola.customproject.model.entities.Competicion;
import net.ddns.jmsola.customproject.model.entities.Equipo;
import net.ddns.jmsola.customproject.model.entities.Fase_Competicion;
import net.ddns.jmsola.customproject.model.entities.Gol_Partido;
import net.ddns.jmsola.customproject.model.entities.Jugador;
import net.ddns.jmsola.customproject.model.entities.Partido;
import net.ddns.jmsola.customproject.model.entities.Plantilla;
import net.ddns.jmsola.customproject.model.entities.Posicion;
import net.ddns.jmsola.customproject.model.entities.Temporada;
import net.ddns.jmsola.customproject.services.api.ServicioPlantilla;
import net.ddns.jmsola.customproject.services.api.ServicioScraping;
import net.ddns.jmsola.customproject.services.commons.ServicioException;
import net.ddns.jmsola.customproject.services.utils.utils;

@Service
public class ServicioScrapingImpl extends ServicioScraping {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ServicioScrapingImpl.class);

	@Autowired
	private EquipoDao equipoDao;

	@Autowired
	private ServicioPlantilla srvPlantilla;

	@Autowired
	private PlantillaDao plantillaDao;

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private PosicionDao posicionDao;

	@Autowired
	private TemporadaDao temporadaDao;

	@Autowired
	private CompeticionDao competicionDao;

	@Autowired
	private ArbitroDao arbitroDao;

	@Autowired
	private PartidoDao partidoDao;

	@Autowired
	private Gol_PartidoDao gol_partidoDao;

	@Autowired
	private Fase_CompeticionDao fase_competicionDao;

	@Override
	public void getPlantillas() throws ServicioException {

		boolean bEquipos = false;
		String temporada;

		for (Integer i = 2001; i < 2003; i++) { // TODO - Cambiar, solo de pruebas
			Integer nextSeason = i + 1;
			temporada = i.toString().substring(2, 4) + "-" + nextSeason.toString().substring(2, 4);
			getEquipos(temporada);
		}

	}

	@Override
	public void getResultados() throws ServicioException {

		String temporada;

		for (Integer i = 2001; i < 2003; i++) { // TODO - Cambiar, solo de pruebas
			Integer nextSeason = i + 1;
			temporada = i.toString().substring(2, 4) + "-" + nextSeason.toString().substring(2, 4);
			getPartidos(temporada);
		}

	}
	
	@Override
	public void validarJsons() throws ServicioException {

		String temporada;

		for (Integer i = 2001; i < 2018; i++) { // TODO - Cambiar, solo de pruebas
			Integer nextSeason = i + 1;
			temporada = i.toString().substring(2, 4) + "-" + nextSeason.toString().substring(2, 4);
			System.out.println("<-- INICIO VALIDACION TEMPORADA " + temporada + " -->");
			validarJson(temporada);
		}

	}

	// http://www.lnfs.es/Clubs/temp14-15/25/
	private void getEquipos(String temporada) throws ServicioException {

		String url;

		System.out.println("<-- Inicio Proceso CLUBS -->");

		url = "http://www.lnfs.es/Clubs/temp" + temporada + "/25/";

		System.out.println("- TEMPORADA --> " + temporada);

		// Compruebo si me da un 200 al hacer la petición
		if (utils.getStatusConnectionCode(url) == 200) {
			Document document = utils.getHtmlDocument(url);

			Elements equiposHTML = document.select("div.escudo_equipo a[title]");

			for (Element equipoHTML : equiposHTML) {

				Elements equipoHTML_Img = equipoHTML.select("img");

				Equipo equipo = equipoDao
						.findByLnfs(Integer.parseInt(equipoHTML.attr("href").toString().split("/")[4]));

				if (equipo == null) {
					equipo = new Equipo();

				}

				equipo.setLnfs(Integer.parseInt(equipoHTML.attr("href").toString().split("/")[4]));
				equipo.setEquipo(equipoHTML.attr("title").toString());
				equipo.setImagen(equipoHTML_Img.attr("src").toString());
				equipo = equipoDao.save(equipo);

				getPlantilla("http://www.lnfs.es" + equipoHTML.attr("href"), equipo,
						Integer.parseInt("20" + temporada.substring(0, 2)));

			}

		}

	}

	// http://www.lnfs.es/Clubs/temp14-15/25/
	private void getPlantilla(String urlEquipo, Equipo equipo, Integer AnhoTemporada) throws ServicioException {

		String url;

		System.out.println("<-- Inicio Proceso Plantilla -->");

		url = urlEquipo.substring(0, 38) + urlEquipo.split("/")[6] + "/Plantilla/" + urlEquipo.split("/")[7];

		if (url == "") {
			throw new ServicioException("ServicioScrapingImpl.getPlantilla --> Error al url vacia");
		}

		System.out.println("- Plantilla --> " + url);

		// Compruebo si me da un 200 al hacer la petición
		if (utils.getStatusConnectionCode(url) == 200) {
			Document document = utils.getHtmlDocument(url);

			Elements jugadoresHTML = document.select("div.box a"); // JUGADORES

			for (Element jugadorHTML : jugadoresHTML) {

				String urlJugador = "http://www.lnfs.es" + jugadorHTML.attr("href");

				Temporada temporada = temporadaDao.findByAnho(AnhoTemporada);

				if (temporada == null) {
					throw new ServicioException(
							"ServicioScrapingImpl.getPlantilla --> Temporada no encontrada (" + AnhoTemporada + ")");
				}

				InsertJugadorPlantilla(urlJugador, equipo, temporada);

			}

		}

	}

	private Jugador InsertJugadorPlantilla(String urlJugador, Equipo equipo, Temporada temporada) {

		String jugadorHTML_Nombre = "";
		String jugadorHTML_Posicion = "";
		String jugadorHTML_Alias = "";
		String jugadorHTML_FechaNacimiento = "";
		String jugadorHTML_LugarNacimiento = "";
		Posicion posicion = new Posicion();

		if (utils.getStatusConnectionCode(urlJugador) == 200) {
			Document documentJugador = utils.getHtmlDocument(urlJugador);

			jugadorHTML_Alias = documentJugador.html().split("<span>")[1].split("</span>")[1].split("\\n")[0].trim();
			jugadorHTML_Posicion = documentJugador.html().split("<span>")[2].split("</span>")[1].split("\\n")[0].trim();
			jugadorHTML_Nombre = documentJugador.html().split("<span>")[3].split("</span>")[1].split("\\n")[0].trim();
			jugadorHTML_FechaNacimiento = documentJugador.html().split("<span>")[4].split("</span>")[1].split("\\n")[0]
					.trim();
			jugadorHTML_LugarNacimiento = documentJugador.html().split("<span>")[5].split("</span>")[1].split("\\n")[0]
					.trim();

		}

		if (jugadorHTML_Posicion != "") {
			posicion = posicionDao.findByPosicion(jugadorHTML_Posicion);
		}

		if (posicion == null) {
			throw new ServicioException(
					"ServicioScrapingImpl.getPlantilla --> Posición no encontrada (" + jugadorHTML_Posicion + ")");
		}

		Jugador jugador = jugadorDao.findByAlias(jugadorHTML_Alias);

		if (jugador == null) {
			jugador = new Jugador();
		}

		jugador.setAlias(jugadorHTML_Alias);
		jugador.setPosicion(posicion);
		jugador.setJugador(jugadorHTML_Nombre);
		// TODO - FECHA NACIMIENTO
		jugador.setLugar_nacimiento(jugadorHTML_LugarNacimiento);
		jugadorDao.save(jugador);

		Plantilla plantilla = new Plantilla();
		plantilla.setEquipo(equipo);
		plantilla.setTemporada(temporada);
		plantilla.setJugador(jugador);
		plantillaDao.save(plantilla);

		return jugador;

	}

	private void getPartidos(String temporada) throws ServicioException {

		String urlTablaResultados = "http://www.lnfs.es/Competiciones/temp" + temporada + "/25/Calendarios.html";

		if (utils.getStatusConnectionCode(urlTablaResultados) == 200) {
			Document documentTablaResultados = utils.getHtmlDocument(urlTablaResultados);

			Elements enlacesPartidos = documentTablaResultados.select("tr[class *= row][class != row-head] td a");

			Temporada temporadaBD = temporadaDao.findByAnho(Integer.parseInt("20" + temporada.substring(0, 2)));

			if (temporadaBD == null) {
				throw new ServicioException("ServicioScrapingImpl.getPartidos --> Temporada no encontrada ("
						+ Integer.parseInt("20" + temporada.substring(0, 2)) + ")");
			}

			Competicion competicionBD = competicionDao.findById(1); // TODO - Cambiar por la competicion por parametro

			if (competicionBD == null) {
				throw new ServicioException("ServicioScrapingImpl.getPartidos --> Competicion no encontrada (" + ")");
			}

			Fase_Competicion fase_competicionBD = fase_competicionDao.findById(1);

			if (fase_competicionBD == null) {
				throw new ServicioException(
						"ServicioScrapingImpl.getPartidos --> Fase_Competicion no encontrada (" + ")");
			}

			boolean jsonNoExiste = false;
			boolean errorGuardarPartido = false;

			for (Element enlace : enlacesPartidos) {
				String idPartido = enlace.attr("href").toString().split("/")[5];
				String equiposPartido = enlace.html();
				System.out.println(enlace.attr("href").toString());
				System.out.println("- Añadido --> " + idPartido);

				String urlJson = "http://www.lnfs.es/Public/json/partido_" + idPartido + ".json";

				String json;

				try {
					json = utils.readUrltoJson(urlJson);
				} catch (Exception e) {
					throw new ServicioException(
							"ServicioScrapingImpl.getPartidos --> No se ha podido parsear el JSON - " + urlJson, e);
				}

				JSONArray arrayJson = new JSONArray(json);
				JSONObject gameinfo = arrayJson.getJSONObject(0).getJSONObject("gameinfo");

				System.out.println("<-- Extracción Datos Partido - " + urlJson + " -->");
				System.out.println("-- GameInfo --");
				System.out.println("- date: " + gameinfo.getString("date"));
				System.out.println("- extra: " + gameinfo.getString("extra"));
				System.out.println("- jornadaNombre: " + gameinfo.getString("jornadaNombre"));
				System.out.println("- jornadaNumero: " + gameinfo.getInt("jornadaNumero"));
				System.out.println("- localid: " + gameinfo.getInt("localid"));
				System.out.println("- localscore: " + gameinfo.getInt("localscore"));
				System.out.println("- place: " + gameinfo.getString("place"));
				System.out.println("- reef1: " + gameinfo.getString("reef1"));
				System.out.println("- reef2: " + gameinfo.getString("reef2"));
				System.out.println("- visitid: " + gameinfo.getInt("visitid"));
				System.out.println("- visitscore: " + gameinfo.getInt("visitscore"));

				if (equiposPartido.equals(utils.convertToUTF8(gameinfo.getString("localLongName")) + " - "
						+ utils.convertToUTF8(gameinfo.getString("visitLongName"))) == false) {
					logger.error(
							"ServicioScrapingImpl.getPartidos --> El Json no corresponde con el partido (Temporada "
									+ temporada + " // Partido [" + equiposPartido + "]) -> " + urlJson);
					jsonNoExiste = true;
					errorGuardarPartido = true;
				}

				if (!jsonNoExiste) {
					Equipo equipo_L = equipoDao.findByLnfs(gameinfo.getInt("localid"));

					if (equipo_L == null) {
						// throw new ServicioException("ServicioScrapingImpl.getPartidos --> Equipo_L no
						// encontrado (" + gameinfo.getInt("localid") + ")");
						logger.error("ServicioScrapingImpl.getPartidos --> Partido no introducido por falta Equipo_L ("
								+ gameinfo.getInt("localid") + ") - " + urlJson);
						errorGuardarPartido = true;
					}

					Equipo equipo_V = equipoDao.findByLnfs(gameinfo.getInt("visitid"));

					if (equipo_V == null) {
						// throw new ServicioException("ServicioScrapingImpl.getPartidos --> Equipo_V no
						// encontrado (" + gameinfo.getInt("visitid") + ")");
						logger.error("ServicioScrapingImpl.getPartidos --> Partido no introducido por falta Equipo_V ("
								+ gameinfo.getInt("visitid") + ") - " + urlJson);
						errorGuardarPartido = true;
					}

					Arbitro arbitro_1 = arbitroDao.findByArbitro(utils.convertToUTF8(gameinfo.getString("reef1")));

					if (arbitro_1 == null) {

						arbitro_1 = new Arbitro();
						arbitro_1.setArbitro(utils.convertToUTF8(gameinfo.getString("reef1")));
						arbitroDao.save(arbitro_1);
					}

					Arbitro arbitro_2 = arbitroDao.findByArbitro(utils.convertToUTF8(gameinfo.getString("reef2")));

					if (arbitro_2 == null) {
						arbitro_2 = new Arbitro();
						arbitro_2.setArbitro(utils.convertToUTF8(gameinfo.getString("reef2")));
						arbitroDao.save(arbitro_2);
					}

					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

					Date date;

					try {
						date = formatter.parse(gameinfo.getString("date").substring(0, 16));
					} catch (JSONException e) {
						throw new ServicioException(
								"ServicioScrapingImpl.getPartidos --> Fecha del partido incorrecta ("
										+ gameinfo.getInt("date") + ")",
								e);
					} catch (ParseException e) {
						throw new ServicioException(
								"ServicioScrapingImpl.getPartidos --> Fecha del partido incorrecta ("
										+ gameinfo.getInt("date") + ")",
								e);
					}

					boolean errorGuardarGoles = false;
					boolean errorGuardarPenaltis = false;

					if (!errorGuardarPartido) {

						Partido partido = new Partido();

						partido.setTemporada(temporadaBD);
						partido.setCompeticion(competicionBD);
						partido.setFase_competicion(fase_competicionBD);
						partido.setEquipo_l(equipo_L);
						partido.setEquipo_v(equipo_V);
						partido.setGoles_equipo_l(gameinfo.getInt("localscore"));
						partido.setGoles_equipo_v(gameinfo.getInt("visitscore"));
						partido.setArbitro_1(arbitro_1);
						partido.setArbitro_2(arbitro_2);
						partido.setFecha(date);
						partidoDao.save(partido);

						JSONArray goals = arrayJson.getJSONObject(0).getJSONArray("goals");

						if (goals.length() > 0) {
							for (int i = 0; i < goals.length(); i++) {

								JSONObject goal = goals.getJSONObject(i);

								System.out.println("-- Goal " + (i + 1) + " --");
								System.out.println("- minuto: " + goal.getInt("minuto"));
								System.out.println("- name: " + goal.getString("name"));
								System.out.println("- resultado: " + goal.getString("resultado"));

								Gol_Partido gol_partido = new Gol_Partido();

								Jugador jugador = jugadorDao
										.findByAlias(utils.convertToUTF8(goal.getString("name").replace(" (pp)", "")));

								if (jugador == null) {

									JSONArray teams = arrayJson.getJSONObject(0).getJSONArray("teams");

									for (int t = 0; t < teams.length(); t++) {
										JSONArray players = teams.getJSONObject(t).getJSONArray("playersStats");

										for (int p = 0; p < players.length(); p++) {

											JSONObject player = players.getJSONObject(p);

											if (utils.convertToUTF8(goal.getString("name").replace(" (pp)", ""))
													.equals(utils.convertToUTF8(player.getString("name")))) {

												jugador = InsertJugadorPlantilla(
														"http://www.lnfs.es" + player.getString("link"),
														t == 0 ? equipo_L : equipo_V, temporadaBD);
											}

										}

									}
									if (jugador == null) {
										// throw new ServicioException(
										// "ServicioScrapingImpl.getPartidos --> Jugador no encontrado ("
										// + gameinfo.getString("name") + ")");
										logger.error(
												"ServicioScrapingImpl.getPartidos --> Partido no introducido por falta de Jugador ("
														+ utils.convertToUTF8(
																goal.getString("name").replace(" (pp)", ""))
														+ ") - " + urlJson);
										errorGuardarGoles = true;
									}
								}

								if (!errorGuardarGoles) {
									gol_partido.setJugador(jugador);
									gol_partido.setMinuto(goal.getInt("minuto"));
									gol_partido.setResultado(goal.getString("resultado"));
									gol_partido.setPartido(partido);
									gol_partidoDao.save(gol_partido);
								}

							}
						}

						JSONArray penalties = arrayJson.getJSONObject(0).getJSONArray("penalties");

						if (penalties.length() > 0) {
							for (int i = 0; i < penalties.length(); i++) {
								JSONObject penalti = penalties.getJSONObject(i);
								System.out.println("-- Penalti " + (i + 1) + " --");
								// TODO
								System.out.println("TODO");
							}
						}

					}

				}
			}

		}

	}

	private void validarJson(String temporada) throws ServicioException {

		String urlTablaResultados = "http://www.lnfs.es/Competiciones/temp" + temporada + "/25/Calendarios.html";

		if (utils.getStatusConnectionCode(urlTablaResultados) == 200) {
			Document documentTablaResultados = utils.getHtmlDocument(urlTablaResultados);

			Elements enlacesPartidos = documentTablaResultados.select("tr[class *= row][class != row-head] td a");

			for (Element enlace : enlacesPartidos) {
				String idPartido = enlace.attr("href").toString().split("/")[5];
				String equiposPartido = enlace.html();

				String urlJson = "http://www.lnfs.es/Public/json/partido_" + idPartido + ".json";

				String json;

				try {
					json = utils.readUrltoJson(urlJson);
				} catch (Exception e) {
					throw new ServicioException(
							"ServicioScrapingImpl.getPartidos --> No se ha podido parsear el JSON - " + urlJson, e);
				}

				JSONArray arrayJson = new JSONArray(json);
				JSONObject gameinfo = arrayJson.getJSONObject(0).getJSONObject("gameinfo");

				if (equiposPartido.equals(utils.convertToUTF8(gameinfo.getString("localLongName")) + " - "
						+ utils.convertToUTF8(gameinfo.getString("visitLongName"))) == false) {
					System.out.println("ServicioScrapingImpl.getPartidos --> El Json no corresponde con el partido (Temporada "
							+ temporada + " // Partido [" + equiposPartido + "]) -> " + urlJson);
					logger.error(
							"ServicioScrapingImpl.getPartidos --> El Json no corresponde con el partido (Temporada "
									+ temporada + " // Partido [" + equiposPartido + "]) -> " + urlJson);
				}
			}
		}
	}

}
