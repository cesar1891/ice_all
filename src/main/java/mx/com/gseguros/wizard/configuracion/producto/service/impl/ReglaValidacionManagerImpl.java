package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.model.ReglaValidacionVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ReglaValidacionManager;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;

/**
 * Clase concreta que implementa los metodos de la interface
 * {@link ReglaValidacionManager} para insertar, modificar y eliminar las reglas de
 * validacion del producto.
 * 
 * @since 1.0
 * @author <a href="mailto:edgar.perez@biosnettcs.com">Edgar Perez</a>
 * @version $Id$
 * @see ReglaValidacionManager
 */

public class ReglaValidacionManagerImpl implements ReglaValidacionManager {

	private static Logger logger = Logger.getLogger(ReglaValidacionManagerImpl.class);

	private WizardDAO wizardDAO;

	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#obtieneReglasDeValidacion(com.biosnet.ice.wizard.productos.reglaValidacion.model.ReglaValidacionVO)
	 */
	public List<ReglaValidacionVO> obtieneReglasDeValidacion(String codigoRamo)
			throws ApplicationException {
		List<ReglaValidacionVO> listaReglasValidacion = null;
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDRAMO_I", codigoRamo);
			listaReglasValidacion  = wizardDAO.obtieneReglasDeValidacion(params);
			
			if (listaReglasValidacion == null) {
				throw new ApplicationException("No exiten reglas de validacion");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_REGLAS_DE_VALIDACION'", bae);
			throw new ApplicationException(
					"Error al cargar lista de reglas de validacion");
		}
		return listaReglasValidacion;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#obtieneListaBloques()
	 */
	public List<LlaveValorVO> obtieneListaBloques() throws ApplicationException {
		List<LlaveValorVO> listaBloques = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			listaBloques = wizardDAO.obtieneListaBloques(params);
			
			if (listaBloques == null) {
				throw new ApplicationException("No exite ningun bloque");
			} 
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_BLOQUES'", bae);
			throw new ApplicationException(
					"Error al cargar los bloques de reglas de validacion desde el sistema");
		}
		return listaBloques;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#obtieneListaValidaciones()
	 */
	public List<LlaveValorVO> obtieneListaValidaciones()
			throws ApplicationException {
		List<LlaveValorVO> listaValidaciones = null;
		try {
			listaValidaciones = wizardDAO.obtenerValidacionesLlave();
			
			if (listaValidaciones == null) {
				throw new ApplicationException("No exite ninguna validacion");
			} 
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_VALIDACIONES'", bae);
			throw new ApplicationException(
					"Error al cargar las validaciones de reglas de validacion desde el sistema");
		}
		return listaValidaciones;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#obtieneListaCondiciones()
	 */
	public List<LlaveValorVO> obtieneListaCondiciones()
	throws ApplicationException {
		List<LlaveValorVO> listaCondiciones = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			listaCondiciones = wizardDAO.obtieneListaCondicionesLlave(params);
			
			if (listaCondiciones == null) {
				throw new ApplicationException("No exite ninguna condicion");
			} 
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_LISTA_CONDICIONES'", bae);
			throw new ApplicationException(
			"Error al cargar las condiciones de reglas de validacion desde el sistema");
		}
		return listaCondiciones;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#asociarReglaDeValidacion(com.biosnet.ice.wizard.productos.reglaValidacion.model.ReglaValidacionVO)
	 */
	public void asociarReglaDeValidacion(ReglaValidacionVO reglaValidacion,
			String codigoRamo) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDBLOQUE_I", reglaValidacion.getCodigoBloque());
		params.put("PV_OTSECUEN_I", reglaValidacion.getSecuencia());
		params.put("PV_CDVALIDA_I", reglaValidacion.getCodigoValidacion());
		params.put("PV_CDCONDIC_I", reglaValidacion.getCodigoCondicion());

		try {
			wizardDAO.asociarReglasDeValidacion(params);
		} catch (Exception bae) {
			logger.error("Exception in invoke ASOCIAR_REGLA_VALIDACION",bae);
			throw new ApplicationException(
					"Error al asociar regla de validacion del producto");
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaValidacionManager#eliminaReglaDeValidacion(String,String,String)
	 */
	public void eliminaReglaDeValidacion(String codigoRamo,
			String codigoBloque, String secuencia) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDBLOQUE_I", codigoBloque);
		params.put("PV_OTSECUEN_I", secuencia);
		
		try {
			wizardDAO.eliminaReglasDeValidacion(params);
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_REGLA_VALIDACION",bae);
			throw new ApplicationException(
					"Error al eliminar regla de validacion del producto");
		}
		
	}
	
}
