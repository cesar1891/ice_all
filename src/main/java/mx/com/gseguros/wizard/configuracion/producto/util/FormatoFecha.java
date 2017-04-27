/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Clase que formatea fechas
 * @author Cesar Hernandez
 *
 */
public class FormatoFecha {
	
	/**
	 * Formatea la fecha a un formato espec&iacute;fico
	 * @param fecha Es la fecha que se quiere formatear
	 * @param formatoFecha Es la m&aacute;scara con la que se formateara la fecha
	 * @return La Fecha formateada
	 */
	public static String formatoFecha(String fecha, String formatoFecha)
	{
		if (fecha == null){
			throw new NullPointerException();
		}
		if (formatoFecha == null){
			throw new NullPointerException();
		}
		if (StringUtils.isBlank(fecha)){
			throw new IllegalArgumentException("El parametro fecha no debe ser blanco o nulo");
		}
		if (StringUtils.isBlank(formatoFecha)){
			throw new IllegalArgumentException("El parametro formatoFecha no debe ser blanco o nulo");
		}
		
		SimpleDateFormat sdfOriginal = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		
		Date date = null;
		
		try{
			date = sdfOriginal.parse(fecha);
		}
		catch(ParseException ex){
			System.out.println("ERROR formatoFecha:: " + ex);
		}
	
		return sdf.format(date);
	}
	
	/**
	 * Formatea la fecha dependiendo de un c&oacute;digo de lenguaje y pa&iacute;s
	 * @param fecha Es la fecha que se quiere formatear
	 * @param languageCode Es el c&oacute;digo del lenguaje
	 * @param countryCode Es el c&oacute;digo del pa&iacute;s
	 * @return La fecha formateada dependiendo el c&oacute;digo de lenguaje y pa&iacute;s
	 */
	public static String formatoFecha(String fecha, String languageCode, String countryCode)
	{
		if (fecha == null){
			throw new NullPointerException();
		}
		if (languageCode == null){
			throw new NullPointerException();
		}
		if (countryCode == null){
			throw new NullPointerException();
		}
		
		if(languageCode.toLowerCase().equals("es") && countryCode.toLowerCase().equals("mx")){
			return formatoFecha(fecha, "dd/MM/yyyy");
		}
		if(languageCode.toLowerCase().equals("en") && countryCode.toLowerCase().equals("us")){
			return formatoFecha(fecha, "dd/MM/yyyy");
		}
		if(languageCode.toLowerCase().equals("en") && countryCode.toLowerCase().equals("au")){
			return formatoFecha(fecha, "");
		}
		if(languageCode.toLowerCase().equals("en") && countryCode.toLowerCase().equals("ca")){
			return formatoFecha(fecha, "");
		}
		if(languageCode.toLowerCase().equals("fr") && countryCode.toLowerCase().equals("ca")){
			return formatoFecha(fecha, "");
		}
		if(languageCode.toLowerCase().equals("pt") && countryCode.toLowerCase().equals("br")){
			return formatoFecha(fecha, "");
		}
		
		return "";
	}
}