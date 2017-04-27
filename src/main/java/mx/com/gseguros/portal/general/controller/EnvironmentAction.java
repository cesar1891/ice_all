package mx.com.gseguros.portal.general.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.utils.Utils;

public class EnvironmentAction extends PrincipalCoreAction {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EnvironmentAction.class);
	
	private static final long serialVersionUID = 1L;

	private Map<String, Object> props;
	
	private Map<String, String> params;
	
	/**
	 * Obtiene los datos del ambiente en el que se ejecuta la aplicaci&oacute;n 
	 * @return
	 * @throws Exception
	 */
	public String obtieneDatosAmbiente() throws Exception {
		
		try {
			Calendar calendar = Calendar.getInstance();
			TimeZone timeZone = calendar.getTimeZone();
			
			props = new HashMap<String, Object>();
			props.put("DisplayName", Locale.getDefault().getDisplayName());
			props.put("user.country", System.getProperty("user.country"));
			props.put("Country", Locale.getDefault().getCountry());
			props.put("DisplayCountry", Locale.getDefault().getDisplayCountry());
			props.put("ISO3Country", Locale.getDefault().getISO3Country());
			props.put("user.language", System.getProperty("user.language"));
			props.put("Language", Locale.getDefault().getLanguage());
			props.put("DisplayLanguage", Locale.getDefault().getDisplayLanguage());
			props.put("ISO3Language", Locale.getDefault().getISO3Language());
			props.put("user.variant", System.getProperty("user.variant"));
			props.put("Variant", Locale.getDefault().getVariant());
			props.put("DisplayVariant", Locale.getDefault().getDisplayVariant());
			props.put("TimeZone Name", timeZone.getDisplayName());
			props.put("TimeZone Id", timeZone.getID());
		} catch(Exception e) {
			logger.error(new StringBuilder("Error: ").append(e.getMessage()), e);
		}
		return SUCCESS;
	}

	
	/**
	 * Test de fechas para verificar que el parseo de una fecha de String a Date se haga correctamente segun el Timezone
	 * @return
	 * @throws Exception
	 */
	public String testFechasTimezone() throws Exception {
		
		try {
			String fechaStr = "18/07/2014";
			if(params != null && params.get("fecha") != null) {
				fechaStr = params.get("fecha");
			}

			SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
			logger.debug("Timezone por default : " + renderFechas.getTimeZone());
			logger.debug("Fecha Parseada a Date: " + renderFechas.parse(fechaStr));

			logger.debug("\n");
			renderFechas = new SimpleDateFormat("dd/MM/yyyy");
			renderFechas.setTimeZone(TimeZone.getTimeZone("Mexico/General"));
			logger.debug("Fecha Parseada a Date Con TimeZone Mexico/General: " + renderFechas.parse(fechaStr));
			
			logger.debug("\n");
			renderFechas = new SimpleDateFormat("dd/MM/yyyy");
			renderFechas.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
			logger.debug("Fecha Parseada a Date Con TimeZone America/Mexico_City: " + renderFechas.parse(fechaStr));

			logger.debug("\n");
			renderFechas = new SimpleDateFormat("dd/MM/yyyy");
			renderFechas.setTimeZone(TimeZone.getTimeZone("GMT"));
			logger.debug("Timezone nuevo para GMT: " + renderFechas.getTimeZone());
			logger.debug("Fecha Parseada a Date Con TimeZone GMT: " + renderFechas.parse(fechaStr));
			
			logger.debug("\n");
			renderFechas = new SimpleDateFormat("dd/MM/yyyy");
			renderFechas.setTimeZone(TimeZone.getTimeZone("UTC"));
			logger.debug("Timezone nuevo para UTC: " + renderFechas.getTimeZone());
			logger.debug("Fecha Parseada a Date Con TimeZone UTC: " + renderFechas.parse(fechaStr));

			Calendar  cal = null;

			String [] fechaArr = fechaStr.split("/");   
			    int dia  =  Integer.parseInt(fechaArr[0]);
			    int mes  =  Integer.parseInt(fechaArr[1])-1;
			    int anio =  Integer.parseInt(fechaArr[2]);
			    cal = Calendar.getInstance();
			    cal.set(anio, mes, dia);

			logger.debug("\n");
			logger.debug("Date con Calendar: " + cal.getTime());
			
			logger.debug("\n");
			cal = Calendar.getInstance();
			cal.set(anio, mes, dia, 0, 0, 0);
			logger.debug("Date con Calendar Hora Cero : " + cal.getTime());
			
			logger.debug("\n");
			cal = Calendar.getInstance(TimeZone.getTimeZone("Mexico/General"),new Locale("es", "MX"));
			cal.set(anio, mes, dia);
			logger.debug("Date con Calendar TimeZone y Locale Mexico/General: " + cal.getTime());
			
			logger.debug("\n");
			cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"),new Locale("es", "MX"));
			cal.set(anio, mes, dia);
			logger.debug("Date con Calendar TimeZone y Locale America/Mexico_City: " + cal.getTime());
			
		} catch(Exception e) {
			logger.error(new StringBuilder("Error: ").append(e.getMessage()).toString(), e);
		}
		return SUCCESS;
	}
	
	public String ponFechas() throws Exception {
    	
    	String fechaStr = "18/07/2014";
		if(params != null && params.get("fecha") != null) {
			fechaStr = params.get("fecha");
		}
    	
		logger.debug("Entrando a ponFechas");
        params = new HashMap<String, String>();
        params.put("pv_cdelemen_i", "6442");
        params.put("pv_fecha_i"   , fechaStr);
        params.put("pv_estado_i"  , "M");
        params.put("pv_cdusuari_i", "SUSCRIPTOR");
        params.put("pv_cdunieco_i", "1403");
        params.put("pv_nmpoliza_i", "93");
        params.put("pv_cdtipsup_i", "19");
        params.put("pv_proceso_i" , "END");
        params.put("pv_cdramo_i"  , "2");
        logger.debug("map IN="+ params);
        props = Utils.ponFechas(params);
        logger.debug("map OUT="+ props);
        return SUCCESS;
    }
	
	//Getters and setters:

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}


	public Map<String, String> getParams() {
		return params;
	}


	public void setParams(Map<String, String> params) {
		this.params = params;
	}


}