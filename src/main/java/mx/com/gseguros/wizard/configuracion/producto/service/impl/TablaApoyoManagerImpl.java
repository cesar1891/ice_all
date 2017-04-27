package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TablaApoyoManager;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;

/**
 * Clase que implementa los metodos de la interface {@link TablaApoyoManager}
 * para obtener los valores de la tabla de apoyo.
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 * @see TablaApoyoManager
 */
public class TablaApoyoManagerImpl implements TablaApoyoManager {

	/**
	 * Logger de la clase
	 */
	private static Logger logger = Logger
			.getLogger(TablaApoyoManagerImpl.class);
	
	private WizardDAO wizardDAO;

	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}


	/**
	 * Endpoints para acceder a los VM
	 */
//	private Map<String, Endpoint> endpoints;

	/**
	 * Atributo que contiene las tablas de apoyo que se pueden consultar.
	 */
	private Map<String, String> tablaApoyo;

	/**
	 * Retorna Map de objetos de tipo String con los nombres de las tablas de
	 * apoyo.
	 * 
	 * @return Map de objetos de tipo String con los nombres de las tablas de
	 *         apoyo.
	 */
	private Map<String, String> getTablaApoyo() {
		return tablaApoyo;
	}

	/**
	 * Asigna al Map de objetos de tipo String con los nombres de las tablas de
	 * apoyo.
	 * 
	 * @param tablaApoyo
	 */
	public void setTablaApoyo(Map<String, String> tablaApoyo) {
		this.tablaApoyo = tablaApoyo;
	}

	/**
	 * Retorna Map de objetos de tipo {@link Endpoint} con los vm que se pueden
	 * invocar.
	 * 
	 * @return Map de {@link Endpoint}
	 */
//	public Map<String, Endpoint> getEndpoints() {
//		return endpoints;
//	}

	/**
	 * Asigna al Map de objetos de tipo {@link Endpoint} con los vm que se
	 * pueden invocar.
	 * 
	 * @param endpoints
	 */
//	public void setEndpoints(Map<String, Endpoint> endpoints) {
//		this.endpoints = endpoints;
//	}

	/**
	 * Metodo que se utiliza para la busqueda de la lista para las tablas de
	 * apoyo que utiliza la aplicacion.
	 * 
	 * @param valor
	 *            Nombre clave de la tabla de apoyo.
	 * @return Lista que contiene objetos de tipo {@link LlaveValorVO}.
	 * @throws ApplicationException
	 */
	private List<LlaveValorVO> obtenerLista(String valor)
			throws ApplicationException {
		List<LlaveValorVO> resultado = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("PV_CDTABLA_I", valor);
		params.put("PV_CDIDIOMA_I", null);
		params.put("PV_CDREGION_I", null);
		try {
			resultado = wizardDAO.obtenerListaTablasApoyo(params);
			if (resultado == null) {
				resultado = new ArrayList<LlaveValorVO>();
			} else {
				logger
						.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!listaCatalogoTipoSegurp size"
								+ resultado.size());
			}

		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_TABLA_APOYO'", bae);
			throw new ApplicationException(
					"Error al cargar los tipos de seguro desde el sistema");
		}
		return resultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.AplicacionManager#obtenerListaNaturales()
	 */
	public List<LlaveValorVO> obtenerListaNaturales()
			throws ApplicationException {
		return obtenerLista(getTablaApoyo().get("NATURALEZA"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.AplicacionManager#obtenerTipoSeguros()
	 */
	public List<LlaveValorVO> obtenerTipoSeguros() throws ApplicationException {
		return obtenerLista(getTablaApoyo().get("TIPO_SEGURO"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.AplicacionManager#obtenerObligatoriedadRol()
	 */
	public List<LlaveValorVO> obtenerObligatoriedadRol()
			throws ApplicationException {
		return obtenerLista(getTablaApoyo().get("OBLIGATORIEDAD_ROL"));

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.AplicacionManager#obtenerLeyendaSumaAsegurada()
	 */
	public List<LlaveValorVO> obtenerLeyendaSumaAsegurada()
			throws ApplicationException {
		return obtenerLista(getTablaApoyo().get("LEYENDA_SUMA_ASEGURADA"));

	}		
}