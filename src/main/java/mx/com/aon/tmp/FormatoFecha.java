package mx.com.aon.tmp;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class FormatoFecha {

	public final static String FORMATO_DEFECTO = "dd/MM/yyyy";
	
	public final static Locale LOCALE = Locale.getDefault();
	
	
	public static String format(java.util.Date fecha, String patron, Locale locale) {
		if (fecha == null)
			throw new NullPointerException();
		if (StringUtils.isBlank(patron))
			throw new IllegalArgumentException("El parametro patron no debe ser blanco o nulo");
		if (locale == null)
			throw new NullPointerException();
		
		return DateFormatUtils.format(fecha, patron, locale);
	}
	
	
	
	public static String format(java.util.Date fecha) {
		if (fecha == null)
			throw new NullPointerException();
		
		return DateFormatUtils.format(fecha, FORMATO_DEFECTO, LOCALE);
	}
	
	
	
	/**
	 * 
	 * 
	 * @param fecha
	 * @param patron
	 * @return
	 */
	public static String format(java.util.Date fecha, String patron) {
		if (fecha == null)
			throw new NullPointerException();
		if (StringUtils.isBlank(patron))
			throw new IllegalArgumentException("El parametro patron no debe ser blanco o nulo");
		
		return DateFormatUtils.format(fecha, patron);
	}
	
	public static String format(java.util.Date fecha, Locale locale) {
		if (fecha == null)
			throw new NullPointerException();
		if (locale == null)
			throw new NullPointerException();
		
		return DateFormatUtils.format(fecha, FORMATO_DEFECTO, locale);
	}
	
	/*
	public static void main(String a[]) {
		try {
			//System.out.println(parse("1990-01-01 00:00:00.0"));
			
			System.out.println(parse(StringUtils.remove("Sun Sep 21 2008 00:00:00 GMT-0500","GMT").substring(4)));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	public static java.util.Date parse(String fecha) throws ParseException {
		if (StringUtils.isBlank(fecha))
			throw new IllegalArgumentException("El parametro fecha no debe ser blanco o nulo");
		
		String[] patron = {FORMATO_DEFECTO, "yyyy-MM-dd HH:mm:ss.S", "MMM dd yyyy HH:mm:ss z"
				,"MMM dd yyyy HH:mm:ss Z", "EEE MMM dd yyyy HH:mm:ss Z"};
		return DateUtils.parseDate(fecha, patron);
	}
}