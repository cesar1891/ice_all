package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TipoObjetoManager;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.DatoVariableObjetoVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.TipoObjetoVO;
import mx.com.gseguros.wizard.dao.WizardDAO;
import mx.com.gseguros.wizard.model.MensajesVO;

import org.apache.log4j.Logger;


public class TipoObjetoManagerImpl implements TipoObjetoManager {

	private static Logger logger = Logger.getLogger(TipoObjetoManagerImpl.class);
	
	private WizardDAO wizardDAO;
	
	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}

	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */
//	private Map<String, Endpoint> endpoints;
	
	public List<LlaveValorVO> catalogoTipoObjetoJson(String tipoObjeto)
			throws ApplicationException {
		List<LlaveValorVO> catalogo = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDTIPOBJ_I", tipoObjeto);
			catalogo = wizardDAO.obtieneCatalogoRoles(params);

			if (catalogo == null) {
				throw new ApplicationException(
						"No exite ningun atributo variable");
			} /*
				 * else { logger .debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				 * catalogotipo de objeto size" + catalogo.size()); }
				 */

		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_TIPO_OBJETO'",
					bae);
			throw new ApplicationException(
					"Error al cargar el catalogo de tipo objeto desde el sistema");
		}
		return catalogo;

	}

	public void agregaObjetoAlCatalogo(LlaveValorVO objeto)
	throws ApplicationException {
		 
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_DSTIPOBJ_I", objeto.getValue());
			wizardDAO.agregarObjCatalogo(params);
					
		}catch(Exception bae){
			logger.error("Exception in invoke AGREGAR_TIPO_OBJETO_CATALOGO", bae);
			throw new ApplicationException(
				"Error al insertar nuevo tipo de objeto al catalogo");
		}
		
	}

	public List<DatoVariableObjetoVO> listaDatosVariablesObjetos(String codigoObjeto)
	throws ApplicationException {
		 List<DatoVariableObjetoVO> listaAtributos = null;
	        try {
	        	Map<String, String> params = new HashMap<String, String>();
	        	params.put("PV_CDTIPOBJ_I", codigoObjeto);
	        	listaAtributos = wizardDAO.listaDatosVariablesObjetos(params);
	        
	        } catch (Exception e) {
	            throw new ApplicationException("Error regresando lista de atributos variables de objeto");
	        }
	        return listaAtributos;
	}
	
	public void agregaDatosVariablesObjeto(List<DatoVariableObjetoVO> atributosObjeto)
		throws ApplicationException {
			
			for(DatoVariableObjetoVO datVarObj : atributosObjeto){
				try{			
					
					Map<String, Object> params = new HashMap<String, Object>();
		        	
		        	params.put("PV_CDTIPOBJ_I", datVarObj.getCodigoObjeto());
		    		params.put("PV_CDATRIBU_I", datVarObj.getCodigoAtributoVariable());
		    		params.put("PV_SWFORMAT_I", datVarObj.getCodigoFormato());
		    		params.put("PV_NMLMAX_I", datVarObj.getMaximo());
		    		params.put("PV_NMLMIN_I", datVarObj.getMinimo());
		    		params.put("PV_SWOBLIGA_I", datVarObj.getSwitchObligatorio());
		    		params.put("PV_DSATRIBU_I", datVarObj.getDescripcionAtributoVariable());
		    		params.put("PV_OTTABVAL_I", datVarObj.getCodigoTabla());
		    		params.put("PV_SWPRODUC_I", datVarObj.getEmision());
		    		params.put("PV_SWSUPLEM_I", datVarObj.getEndoso());
		    		params.put("PV_SWACTUAL_I", datVarObj.getRetarificacion());
		    		params.put("PV_GB_SWFORMAT_I", null);
		    		params.put("PV_CDATRIBU_PADRE_I", datVarObj.getCodigoPadre());
		    		params.put("PV_NMORDEN_I", datVarObj.getOrden());
		    		params.put("PV_NMAGRUPA_I", datVarObj.getAgrupador());
		    		params.put("PV_CDCONDICVIS_I", datVarObj.getCodigoCondicion());
		    		params.put("PV_SWCOTIZA_I", datVarObj.getApareceCotizador());		
		    		params.put("PV_SWDATCOM_I", datVarObj.getDatoComplementario());
		    		params.put("PV_SWCOMOBL_I", datVarObj.getObligatorioComplementario());
		    		params.put("PV_SWCOMUPD_I", datVarObj.getModificableComplementario());
		    		params.put("PV_SWENDOSO_I", datVarObj.getApareceEndoso());
		    		params.put("PV_SWENDOBL_I", datVarObj.getObligatorioEndoso());

		        	wizardDAO.agregaDatosVarObj(params);
		        	
				}catch(Exception bae){
					logger.error("Exception in invoke AGREGAR_DATOS_VARIABLES_OBJETO", bae);
						throw new ApplicationException("Error al insertar atributoS variableS al objeto");
				}
			}
		
	}
	
	public void agregarTipoObjetoInciso(TipoObjetoVO objeto)throws ApplicationException {
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDTIPSIT_I", objeto.getCodigoTipoSituacion());
			params.put("PV_CDTIPOBJ_I", objeto.getCodigoTipoObjeto());
			wizardDAO.agregaTipoObjInciso(params);
		}catch(Exception bae){
			logger.error("Exception in invoke AGREGAR_TIPO_OBJETO_INCISO", bae);
			throw new ApplicationException(
				"Error al insertar tipo de objeto al inciso");
		}
		
	}
	
	public MensajesVO borrarTipoObjetoInciso(TipoObjetoVO objeto)throws ApplicationException {
		
		MensajesVO mensajeVO = null;
		try{
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("pv_cdtipsit_i", objeto.getCodigoTipoSituacion());
			params.put("pv_cdtipobj_i", objeto.getCodigoTipoObjeto());
			
			mensajeVO = wizardDAO.borraTipoObjInciso(params);			
		}catch(Exception bae){
			logger.error("Exception in invoke BORRAR_TIPO_OBJETO_INCISO", bae);
			throw new ApplicationException(
				"Error al borrar tipo de objeto al inciso");
		}
		return mensajeVO;
	}
	
	public void borraTatriObjeto(TipoObjetoVO objeto)throws ApplicationException {
		
		try{
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("pv_cdtipobj_i", objeto.getCodigoTipoObjeto());
			params.put("pv_cdatribu_i", objeto.getCodigoAtributoVariable());
			wizardDAO.borraTatriObjeto(params);
		}catch (Exception bae) {
			logger.error("Exception in invoke BORRA_TATRI_OBJETO", bae);
			throw new ApplicationException("Error al eliminar objeto");
		}
	}
	
	public boolean tieneHijosAtributoVariableObjeto(TipoObjetoVO tipoObjeto)
			throws ApplicationException {
		MensajesVO mensaje = null;
		boolean tieneHijos = false;
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
        	params.put("pv_cdtipobj_i", tipoObjeto.getCodigoTipoObjeto());
        	params.put("pv_cdatribu_i", tipoObjeto.getCodigoAtributoVariable());
			mensaje = wizardDAO.tieneHijosAtributoVariableObjeto(params);
		} catch (Exception bae) {
			logger.error("Se origino un error: " + bae.getMessage(), bae);
			throw new ApplicationException("Error intentando validar hijos de atributos variables de Objeto");
		}
		if(mensaje.getMsgText().equals("1")){
			tieneHijos = true;
		}else{
			tieneHijos = false;
		}
		return tieneHijos;
	}


}