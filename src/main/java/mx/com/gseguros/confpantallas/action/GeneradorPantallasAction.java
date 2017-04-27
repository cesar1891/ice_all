package mx.com.gseguros.confpantallas.action;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.confpantallas.service.impl.GeneradorPantallasManager;

import org.springframework.beans.factory.annotation.Value;

import org.apache.log4j.Logger;

public class GeneradorPantallasAction extends PrincipalCoreAction {
	
	private static final long serialVersionUID = 7775762215191857883L;
	
	private Logger logger = Logger.getLogger(GeneradorPantallasAction.class);
	
	private GeneradorPantallasManager generadorPantallasManager;
	
	private String cdpantalla;
	
	private String datos;
	
	private String componentes;
	
	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;

	@Value("${pass.servidor.reports}")
    private String passServidorReports; 
	
	
	public String guardaPantalla() throws Exception {
		generadorPantallasManager.insertaPantalla(cdpantalla, datos, componentes);
		return SUCCESS;
	}
	
	
	
	
	
	// Getters and setters:
	
	/**
	 * setter for generadorPantallasManager
	 * @param generadorPantallasManager
	 */
	public void setGeneradorPantallasManager(GeneradorPantallasManager generadorPantallasManager) {
		this.generadorPantallasManager = generadorPantallasManager;
	}

	public String getCdpantalla() {
		return cdpantalla;
	}

	public void setCdpantalla(String cdpantalla) {
		this.cdpantalla = cdpantalla;
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public String getComponentes() {
		return componentes;
	}

	public void setComponentes(String componentes) {
		this.componentes = componentes;
	}
	
	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}
	
}