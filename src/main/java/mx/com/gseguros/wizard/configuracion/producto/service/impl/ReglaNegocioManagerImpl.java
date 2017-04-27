package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ReglaNegocioManager;
import mx.com.gseguros.wizard.configuracion.producto.util.ReglaNegocio;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;
import org.jfree.util.Log;


/**
 * Clase concreta que implementa los metodos de la interface
 * {@link ReglaNegocioManager} para insertar, modificar y eliminar las reglas de
 * negocio del producto; tales como:
 * <p>
 * <ul>
 * <li>Validaciones</li>
 * <li>Condiciones</li>
 * <li>Variables Temporales</li>
 * <li>Conceptos de Tarificacion</li>
 * <li>Autorizaciones</li>
 * </ul>
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 * @see ReglaNegocioManager
 */
public class ReglaNegocioManagerImpl implements ReglaNegocioManager {

	
	private WizardDAO wizardDAO;
	
	/**
	 * Logger de la clase.
	 */
	private static Logger logger = Logger
			.getLogger(ReglaNegocioManagerImpl.class);

	
	/**
	 * 
	 * @param tipo
	 * @return
	 * @throws ApplicationException
	 */
	private List<ReglaNegocioVO> obtenerReglasNegocio(String tipo) throws ApplicationException {
		List<ReglaNegocioVO> resultado = new ArrayList<ReglaNegocioVO>();
		WrapperResultados wrapperResult = null;
		logger.debug("Entrando a obtenerReglasNegocio(String) Endpoint:" + tipo);
		try {
			
			resultado = (ArrayList<ReglaNegocioVO>)wrapperResult.getItemList();

		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_TABLA_APOYO'", bae);
			throw new ApplicationException(
					"Error al cargar los tipos de seguro desde el sistema");
		}

		return resultado;
	}
	
	
	public PagedList obtenerReglasNegocioPagedList(ReglaNegocio reglaNegocio, int start, int limit) 
		throws ApplicationException {
		logger.debug(">>>>>>> obtenerReglasNegocioPagedList");
		Map<String, String> map = new HashMap<String, String>();
		List result = null;
		PagedList pagedList = null;
		
		try{
		switch (reglaNegocio) {
		case Validacion:
			result = wizardDAO.obtenerValidaciones();
			break;
		case ConceptoTarificacion:
			result = wizardDAO.obtenerConceptoTarificacion();
			break;
		case Condicion:
			result = wizardDAO.obtenerCondicion();
			break;
		case Autorizacion:
			result = wizardDAO.obtenerAutorizacion();
			break;
		case VariableTemporal:
			result = wizardDAO.obtenerVarTemp();
			break;
		}
		}catch(Exception e){
			Log.error("Error al ejecutar PL para obtener regla de negocio",e);
		}
		
		if(result!=null){
			
			if(limit != -1){
		        if (result.size() < (start + limit)){
		          limit =  result.size();
		        } else {
		          limit = start + limit;
		        }
		        result = result.subList(start, limit);
			}
			pagedList = new PagedList();
			pagedList.setItemsRangeList(result);
	        pagedList.setTotalItems(result.size());
		}
		
		return pagedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#insertarReglaNegocio(com.biosnet.ice.wizard.productos.reglanegocio.model.ReglaNegocioVO)
	 */
	public void insertarReglaNegocio(ReglaNegocioVO reglaNegocio)
			throws ApplicationException, Exception {

		if (reglaNegocio == null)
			throw new Exception("Parametro reglaNegocio null");

		if (reglaNegocio.getCodigo() == null)
			throw new Exception(
					"Atributo codigo del parametro reglaNegocio null");

		if (reglaNegocio.getNombre() == null
				|| reglaNegocio.getNombre().equals(""))
			throw new Exception(
					"Atributo nombre del parametro reglaNegocio null");

		if (reglaNegocio.getDescripcion() == null
				|| reglaNegocio.getDescripcion().equals(""))
			throw new Exception(
					"Atributo descripcion del parametro reglaNegocio null");

		Map<String, Object> params = new HashMap<String, Object>();

		switch (reglaNegocio.getCodigo()) {
		case Validacion:
			params.put("pv_cdvalida_i", reglaNegocio.getNombre());
			params.put("pv_dsvalida_i", reglaNegocio.getDescripcion());
			params.put("pv_cdexpres_i", reglaNegocio.getCodigoExpresion());
			params.put("pv_cdtipo_i", reglaNegocio.getTipo());
			params.put("pv_dsmsgerr_i", reglaNegocio.getMensaje());
			
			wizardDAO.insertarValidacion(params);
			
			break;
		case ConceptoTarificacion:
			params.put("pv_cdcontar_i", reglaNegocio.getNombre());
			params.put("pv_dscontar_i", reglaNegocio.getDescripcion());
			params.put("pv_cdtipcon_i", reglaNegocio.getTipo());
			params.put("pv_cdexpres_i", reglaNegocio.getCodigoExpresion());
			
			wizardDAO.insertarConceptoTarif(params);
			break;
		case VariableTemporal:
			params.put("pv_cdvariant_i", reglaNegocio.getNombre());
			params.put("pv_dsvariat_i", reglaNegocio.getDescripcion());
			params.put("pv_cdexpres_i", reglaNegocio.getCodigoExpresion());
			
			wizardDAO.insertarVariableTemp(params);
			break;
		case Autorizacion:
			params.put("pv_cdmotivo_i", reglaNegocio.getNombre());
			params.put("pv_dsmotivo_i", reglaNegocio.getDescripcion());
			params.put("pv_otnivel_i", reglaNegocio.getNivel());
			params.put("pv_cdexpres_i", reglaNegocio.getCodigoExpresion());
			
			wizardDAO.insertarAutorizacion(params);
			break;
		case Condicion:
			params.put("pv_cdcondic_i", reglaNegocio.getNombre());
			params.put("pv_dscondic_i", reglaNegocio.getDescripcion());
			params.put("pv_cdexpres_i", reglaNegocio.getCodigoExpresion());
			
			wizardDAO.insertarCondicion(params);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#obtenerReglasNegocio(com.biosnet.ice.wizard.util.ReglaNegocio)
	 */
	public List<ReglaNegocioVO> obtenerReglasNegocio(ReglaNegocio reglaNegocio)
			throws ApplicationException {

		List<ReglaNegocioVO> reglasNegocio = new ArrayList<ReglaNegocioVO>();
	try{	
		switch (reglaNegocio) {
		case Validacion:
			reglasNegocio = wizardDAO.obtenerValidaciones();
			break;
		case ConceptoTarificacion:
			reglasNegocio = wizardDAO.obtenerConceptoTarificacion();
			break;
		case Condicion:
			reglasNegocio = wizardDAO.obtenerCondicion();
			break;
		case Autorizacion:
			reglasNegocio = wizardDAO.obtenerAutorizacion();
			break;
		case VariableTemporal:
			reglasNegocio = wizardDAO.obtenerVarTemp();
			break;
		}
	}catch(Exception e){
		Log.error("Error al ejecutar PL para obtener regla de negocio",e);
	}
		return reglasNegocio;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#obtenerTiposConceptosTarificacion()
	 */
	public List<LlaveValorVO> obtenerTiposConceptosTarificacion() throws ApplicationException {
		List<LlaveValorVO> lista = new ArrayList<LlaveValorVO>();
		
		try {
			HashMap<String, Object>params =  new HashMap<String, Object>();
			lista = wizardDAO.obtieneTipoConceptos(params);

		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_TABLA_APOYO'", bae);
			throw new ApplicationException(
					"Error al cargar los tipos de seguro desde el sistema");
		}
		
		return lista;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#obtenerTiposValidaciones()
	 */
	public List<LlaveValorVO> obtenerTiposValidaciones() {
		List<LlaveValorVO> lista = new ArrayList<LlaveValorVO>();
		
		LlaveValorVO valor = new LlaveValorVO();
		
		valor.setKey("A");
		valor.setValue("Aviso");
		lista.add(valor);
		
		valor = new LlaveValorVO();
		valor.setKey("E");
		valor.setValue("Error");
		lista.add(valor);
		
		return lista;
	}

	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#obtenerVariablesTemporalesAsociadasAlProducto()
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public List<ReglaNegocioVO> obtenerVariablesTemporalesAsociadasAlProducto(
			String cdramo) throws ApplicationException {
		List<ReglaNegocioVO> listaVariablesTemporales = new ArrayList<ReglaNegocioVO>();
		WrapperResultados res;
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDRAMO_I", cdramo);
			listaVariablesTemporales = wizardDAO.obtenerVariablesTemporalesAsociadasAlProducto(params);
			
			
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_VARIABLES_TEMPORALES_PRODUCTO'", bae);
			throw new ApplicationException(
					"Error al cargar las variables temporales asociadas al producto desde el sistema");
		}		
		return listaVariablesTemporales;
	}

	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.ReglaNegocioManager#asociarVariablesDelProducto()
	 */
	public void asociarVariablesDelProducto(
			List<ReglaNegocioVO> listaReglaNegocioVariables, String codigoRamo)
			throws ApplicationException {
			
			for(ReglaNegocioVO regla : listaReglaNegocioVariables){ 
				try{
					Map params = new HashMap<String, String>();
					params.put("PV_CDVARIAT_I", regla.getNombre());
					params.put("PV_CDRAMO_I", codigoRamo);
					
					wizardDAO.asociarVariablesDelProducto(params);
				}catch(Exception bae){
					logger.error("Exception in invoke ASOCIAR_VARIABLES_TEMPORALES_PRODUCTO", bae);
					throw new ApplicationException(
						"Error al insertar las variables temporales del producto");
				}
			}
		
	}

	public void desasociarVariablesDelProducto(List<ReglaNegocioVO> list,
			String codigoRamo) throws ApplicationException {
		
		for(ReglaNegocioVO regla : list){
			try{
				Map params = new HashMap<String, String>();
				params.put("PV_CDVARIAT_I", regla.getNombre());
				params.put("PV_CDRAMO_I", codigoRamo);
				
				wizardDAO.desasociarVariablesDelProducto(params);
			}catch(Exception bae){
				logger.error("Exception in invoke DESASOCIAR_VARIABLES_TEMPORALES_PRODUCTO", bae);
				throw new ApplicationException(
					"Error al eliminar las variables temporales del producto");
			}
		}
		
	}
	
	public String borraVarTmp(String cdVariat) throws ApplicationException {
		String res = "Variable temporal borrada con \u00e9xito";
		Map params = new HashMap<String, String>();
		params.put("pv_cdvariat_i", cdVariat);
		 
		try{
			wizardDAO.borraVarTmp(params);
		}catch(Exception bae){
			logger.error("Exception in invoke ELIMINA_VAR_TMP", bae);
			throw new ApplicationException(
				"Error al eliminar las variables temporales del producto");
		}
		
		return res;
	
		
	}
	
	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}

	
}