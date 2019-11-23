package net.ddns.jmsola.customproject.services.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.hibernate.service.spi.ServiceException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.ddns.jmsola.customproject.services.commons.ServicioException;

public class utils {
	
	
	
    /**
     * Con este método devuelvo un objeto de la clase Document con el contenido del
     * HTML de la web que me permitirá parsearlo con los métodos de la librelia JSoup
     * @param url
     * @return Documento con el HTML
     */
    public static Document getHtmlDocument(String url) {

        Document doc = null;
    	try {
    	    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
    	    } catch (IOException ex) {
    		System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
    	    }
        return doc;
    }
    
    /**
     * Con esta método compruebo el Status code de la respuesta que recibo al hacer la petición
     * EJM:
     * 		200 OK			300 Multiple Choices
     * 		301 Moved Permanently	305 Use Proxy
     * 		400 Bad Request		403 Forbidden
     * 		404 Not Found		500 Internal Server Error
     * 		502 Bad Gateway		503 Service Unavailable
     * @param url
     * @return Status Code
     */
    public static int getStatusConnectionCode(String url) {
    		
        Response response = null;
    	
        try {
    	response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
    	System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return response.statusCode();
    }
    
    public static String readUrltoJson(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    
    public static String convertToUTF8 (String cadena) throws ServicioException {
    	
    	byte cadena_Byte[] = cadena.getBytes();
		String cadena_UTF8;
		try {
			cadena_UTF8 = new String(cadena_Byte, "UTF-8");
			return cadena_UTF8;
		} catch (UnsupportedEncodingException e) {
			throw new ServicioException(
					"La conversion de la cadena a UTF-8 no es correcta", e);

		}
    	
		
    }
    
    
}
