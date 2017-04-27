package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.ATRIBUTO_PADRE_COBERTURA;
import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.ATRIBUTO_PADRE_OBJETO;
import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.ATRIBUTO_PADRE_PRODUCTO;
import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.ATRIBUTO_PADRE_ROL;
import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.GUARDA_ATRIBUTOS_VARIABLES;
import static mx.com.gseguros.wizard.configuracion.producto.dao.AtributoDAO.OBTIENE_PADRES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.ConvertUtil;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.atributosVariables.model.AtributosVariablesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.AtributosVariablesManager;
import mx.com.gseguros.wizard.model.MensajesVO;

import org.apache.log4j.Logger;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que implementa los metodos para agregar y obtener las listas de
 * atributos variables asociados a un producto
 * 
 */
public class AtributosVariablesManagerImpl extends
		AbstractManagerJdbcTemplateInvoke implements AtributosVariablesManager {

	private static Logger logger = Logger.getLogger(AtributosVariablesManagerImpl.class);

	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */

	
	/**
	 * Metodo que se utiliza para obtener las claves de los atributos variables.
	 * 
	 * @param point
	 *            Parametro que contiene el VM que sera invocado.
	 * @param params
	 *            Contiene los valores de los parametros que se van a buscar en
	 *            la base de datos.
	 * @return Retorna la clave de los atributos.
	 * @throws ApplicationException
	 *             Si existe algun error en la consulta.
	 */
//	@SuppressWarnings("unused")
//	private int obtenerClaveAtributo(String point, Map<String, String> params)
//			throws ApplicationException {
//		int codigo = 0;
//		if (logger.isInfoEnabled()) {
//			logger.info("Entro al metodo de obtener Clave");
//		}
//		
//		try {
//			codigo = (Integer) endpoint.invoke(params);
//			if (logger.isDebugEnabled()) {
//				logger.debug("Codigo nuevo de Atributo: " + codigo);
//			}
//		} catch (Exception e) {
//			logger.error("Se origino un error: " + e.getMessage());
//			throw new ApplicationException(
//					"Error intentando obtener la clave del nuevo atributo variable");
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("Esta saliendo del metodo de obtener Clave");
//		}
//		return codigo;
//	}

	/**
	 * Implementacion que extrae todas las tablas asociadas al los atributos
	 * variables de un producto.
	 * 
	 * 
	 * @return List<AtributosVariablesVO> - Lista con la informacion de todas
	 *         las tablas asociadas al los atributos variables de un producto
	 *         solicitado.
	 * @throws ApplicationException
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */

	@SuppressWarnings("unchecked")
	public PagedList listaDeValoresAtributosVariables(String valor, int start, int limit) throws ApplicationException {

		Map<String,String> map = new HashMap<String,String>();
        map.put("pv_ottipotb_i", valor);
        
        String endpointName = "OBTIENE_VALORES_ATRIBUTOS";
        return pagedBackBoneInvoke(map, endpointName, start, limit);
		
	}

	/**
	 * Implementacion que guarda todos los atributos variables de un producto.
	 * 
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	@SuppressWarnings("unchecked")
	public String guardarAtributosVariables(AtributosVariablesVO atributos)
			throws ApplicationException {

		logger.info("Entro metodo: guardarAtributosVariables");
		
		

		Map params = new HashMap();

		params.put("pv_cdramo_i", atributos.getCdRamo());
		params.put("pv_clavecampo_i", ConvertUtil.nvl(atributos.getClaveCampo()));
		params.put("pv_descripcion_i", atributos.getDescripcion());
		params.put("pv_cdformato_i", atributos.getCdFormato());
		params.put("pv_obligatorio_i", atributos.getObligatorio());
		params.put("pv_maximo_i", atributos.getMaximo());
		params.put("pv_minimo_i", atributos.getMinimo());
		params.put("pv_cdtabla_i", atributos.getCdTabla());
		params.put("pv_emision_i", atributos.getEmision());
		params.put("pv_endosos_i", atributos.getEndosos());
		params.put("pv_retarificacion_i", atributos.getRetarificacion());
		params.put("pv_cotizador_i", atributos.getCotizador());
		params.put("pv_swdatcom_i", atributos.getDatoComplementario());
		params.put("pv_swcomobl_i", atributos.getObligatorioComplementario());
		params.put("pv_swcomupd_i", atributos.getModificableComplementario());
		params.put("pv_swendoso_i", atributos.getApareceEndoso());
		params.put("pv_swendobl_i", atributos.getObligatorioEndoso());
		params.put("pv_cdatribu_padre_i", atributos.getCodigoPadre());
		params.put("pv_nmagrupa_i", atributos.getAgrupador());
		params.put("pv_cdcondicvis_i", atributos.getCodigoCondicion());
		params.put("pv_swinsert_i", atributos.getInserta());
		params.put("pv_cdexpres_i", atributos.getCodigoExpresion());
		params.put("pv_nmorden_i", atributos.getOrden());

		if (logger.isDebugEnabled())
			logger.debug("Return"); 
		try {
			WrapperResultados res = returnBackBoneInvoke(params, GUARDA_ATRIBUTOS_VARIABLES);
			logger.info("Salida metodo: guardarAtributosVariables");
			return res.getMsgText();
		} catch (ApplicationException ap) {
			logger.error("Error: " + ap, ap);
			ap.printStackTrace();
			throw new ApplicationException(ap.getMessage());
		} catch (Exception e){
			logger.error("Error: " + e, e);
			throw new ApplicationException(e.getMessage());
		}
		
		

	}

	public MensajesVO guardarAtributosVariablesInciso(AtributosVariablesVO atributos)
			throws ApplicationException {
		MensajesVO mensaje = new MensajesVO();

		
		Map params = new HashMap();

		params.put("PV_CDTIPSIT_I", atributos.getCodigoSituacion());
		params.put("PV_CDATRIBU_I", atributos.getClaveCampo());
		params.put("PV_DSATRIBU_I", atributos.getDescripcion());
		params.put("PV_CDFORMATO_I", atributos.getCdFormato());
		params.put("PV_MINIMO_I", atributos.getMinimo());
		params.put("PV_MAXIMO_I", atributos.getMaximo());
		params.put("PV_SWOBLIGA_I", atributos.getObligatorio());
		params.put("PV_SWPRODUC_I", atributos.getEmision());
		params.put("PV_SWSUPLEM_I", atributos.getEndosos());
		params.put("PV_SWTARIFI_I", atributos.getRetarificacion());
		params.put("PV_SWPRESEN_I", atributos.getCotizador());
		params.put("PV_CDTABLA_I", atributos.getCdTabla());
		params.put("PV_SWDATCOM_I", atributos.getDatoComplementario());
		params.put("PV_SWCOMOBL_I", atributos.getObligatorioComplementario());
		params.put("PV_SWCOMUPD_I", atributos.getModificableComplementario());
		params.put("PV_SWENDOSO_I", atributos.getApareceEndoso());
		params.put("PV_SWENDOBL_I", atributos.getObligatorioEndoso());
		params.put("PV_CDEXPRES_I", atributos.getCodigoExpresion());
		params.put("PV_CDRAMO_I", atributos.getCdRamo());
		params.put("PV_SWINSERT_I", atributos.getInserta());
		params.put("PV_CDATRIBU_PADRE_I", atributos.getCodigoPadre());
		params.put("PV_NMORDEN_I", atributos.getOrden());
		params.put("PV_NMAGRUPA_I", atributos.getAgrupador());
		params.put("PV_CDCONDICVIS_I", atributos.getCodigoCondicion());
		params.put("PV_SWSUSCRI_I",atributos.getAtributoParaTodos());
		
		try {
			if (atributos.getClaveCampo() == null) {
				if (atributos.getClaveCampo().equals(""))
					atributos.setClaveCampo(null);
			}
			WrapperResultados res = returnBackBoneInvoke(params, "GUARDA_ATRIBUTOS_VARIABLES_INCISO");
			mensaje.setMsgId(res.getMsgId());
			mensaje.setMsgText(res.getMsgText());
			mensaje.setTitle(res.getMsgTitle());
		} catch (Exception e) {
			throw new ApplicationException(
					"Error intentando guardar los atributos variables");
		}
		
		return mensaje;
	}

	public MensajesVO guardarAtributosVariablesCobertura(AtributosVariablesVO atributos) throws ApplicationException {
		MensajesVO mensaje = new MensajesVO();
		
		HashMap<String, Object> params= new HashMap<String, Object>(); 
		params.put("PV_CDRAMO_I", atributos.getCdRamo());
		params.put("PV_CDTIPSIT_I", atributos.getCodigoSituacion());
		params.put("PV_CDGARANT_I", atributos.getCodigoGarantia());
		params.put("PV_CDATRIBU_I", atributos.getClaveCampo());
		params.put("PV_CDTABLA_I", atributos.getCdTabla());
		params.put("PV_CDFORMATO_I", atributos.getCdFormato());
		params.put("PV_ENDOSOS_I", atributos.getEndosos());
		params.put("PV_EMISION_I", atributos.getEmision());
		params.put("PV_MINIMO_I", atributos.getMinimo());
		params.put("PV_MAXIMO_I", atributos.getMaximo());
		params.put("PV_SWOBLIGA_I", atributos.getObligatorio());
		params.put("PV_SWCOTIZA_I", atributos.getCotizador());
		params.put("PV_SWDATCOM_I", atributos.getDatoComplementario());
		params.put("PV_SWCOMOBL_I", atributos.getObligatorioComplementario());
		params.put("PV_SWCOMUPD_I", atributos.getModificableComplementario());
		params.put("PV_SWENDOSO_I", atributos.getApareceEndoso());
		params.put("PV_SWENDOBL_I", atributos.getObligatorioEndoso());
		params.put("PV_SWINSERT_I", atributos.getInserta());
		params.put("PV_DSATRIBU_I", atributos.getDescripcion());
		params.put("PV_CDEXPRES_I", atributos.getCodigoExpresion());
		params.put("pv_cdatribu_padre_i", atributos.getCodigoPadre());
		params.put("pv_nmorden_i", atributos.getOrden());
		params.put("pv_nmagrupa_i", atributos.getAgrupador());
		params.put("pv_cdcondicvis_i", atributos.getCodigoCondicion());
		params.put("pv_SWTARIFI_i", atributos.getRetarificacion());
		
		try {
			if (atributos.getClaveCampo() == null) {
				if (atributos.getClaveCampo().equals(""))
					atributos.setClaveCampo(null);
			}
			WrapperResultados res = returnBackBoneInvoke(params, "GUARDA_ATRIBUTOS_VARIABLES_GARANTIA");
			mensaje.setMsgId(res.getMsgId());
			mensaje.setMsgText(res.getMsgText());
			mensaje.setTitle(res.getMsgTitle());
			
		} catch (Exception e) {
			throw new ApplicationException("Error intentando guardar los atributos variables");
		}
		return mensaje;
	}
	
	public MensajesVO eliminarAtributosVariables(AtributosVariablesVO atributos, int nivel) throws ApplicationException {
		MensajesVO mensaje = new MensajesVO();
		WrapperResultados res =  null;
		try {
		
		Map<String,String> params = new HashMap<String,String>();
	    params.put("pv_cdramo_i", atributos.getCdRamo());
	    params.put("pv_cdatribu_i", atributos.getClaveCampo());
	    params.put("pv_cdtipsit_i", atributos.getCodigoSituacion());
	    params.put("PV_CDRAMO_I", atributos.getCdRamo());
	    params.put("PV_CDTIPSIT_I", atributos.getCodigoSituacion());
	    params.put("PV_CDGARANT_I", atributos.getCodigoGarantia());
	    params.put("PV_CDATRIBU_I", atributos.getClaveCampo());
			
		switch (nivel) {
		case 1:
			res = returnBackBoneInvoke(params, "ELIMINA_ATRIBUTOS_VARIABLES_PRODUCTO");
			break;
		case 2:
			res = returnBackBoneInvoke(params, "ELIMINA_ATRIBUTOS_VARIABLES_INCISO");
			break;
		case 3:
			res = returnBackBoneInvoke(params, "ELIMINA_ATRIBUTOS_VARIABLES_GARANTIA");
			break;
		}
		
		mensaje.setMsgId(res.getMsgId());
		mensaje.setMsgText(res.getMsgText());
		mensaje.setTitle(res.getMsgTitle());
		
			if(logger.isDebugEnabled()) {
				logger.debug("MsgId=" + mensaje.getMsgId());
				logger.debug("MsgText=" + mensaje.getMsgText());
				logger.debug("Title=" + mensaje.getTitle());
			}
		} catch (Exception e) {
			mensaje.setMsgText(e.getMessage());
			logger.error("Error intentando eliminar los atributos variables",e);
		}
		
		return mensaje;
	}

	public AtributosVariablesVO obtenerAtributoVariablePorNivel(int nivel,
			AtributosVariablesVO atributoVariable) throws ApplicationException {
		String vm = null;
		AtributosVariablesVO resultado = null;

		switch (nivel) {
		case 1:
			vm = "OBTIENE_ATRIBUTO_VARIABLE_PRODUCTO";
			break;
		case 2:
			vm = "OBTIENE_ATRIBUTO_VARIABLE_RIESGO";
			break;
		case 3:
			vm = "OBTIENE_ATRIBUTO_VARIABLE_COBERTURA";
			break;
		}
		if (vm != null) {
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("PV_CDRAMO_I", atributoVariable.getCdRamo());
				params.put("PV_CDTIPSIT_I", atributoVariable.getCodigoSituacion());
				params.put("PV_CDGARANT_I", atributoVariable.getCodigoGarantia());
				params.put("PV_CDATRIBU_I", atributoVariable.getCodigoAtributo());
				
				List<AtributosVariablesVO> lista = (List<AtributosVariablesVO>) getAllBackBoneInvoke(params, vm);
				resultado = lista.get(0);
				
			} catch (Exception bae) {
				logger.error("Exception in invoke", bae);
				throw new ApplicationException(
						"Error al cargar los Atributos Variables");
			}
		}

		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getPadre(String cdTipSituacion,
			String codigoAtributoP) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("PV_CDTIPSIT_I", cdTipSituacion);
		params.put("PV_CDATRIBU_I", codigoAtributoP);
		ArrayList<LlaveValorVO> padres = null;
		try {
			WrapperResultados res = returnBackBoneInvoke(params, OBTIENE_PADRES);
			padres = (ArrayList<LlaveValorVO>) res.getItemList();
		} catch (Exception bae) {
			logger.error(bae.getMessage());
			throw new ApplicationException("Error al recuperar datos de padre");
		}
		return (ArrayList<LlaveValorVO>) padres;
	}

	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getPadreObjeto(String cdTipSituacion,
			String codigoAtributoP) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("pv_cdtipobj_i", cdTipSituacion);
		params.put("pv_cdatribu_i", ConvertUtil.nvl(codigoAtributoP));
		
		return getAllBackBoneInvoke(params, ATRIBUTO_PADRE_OBJETO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.gseguros.wizard.configuracion.producto.service.AtributosVariablesManager#getPadrePoliza(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getPadrePoliza(String cdRamo,
			String codigoAtributoP) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("pv_cdramo_i", cdRamo);
		params.put("pv_cdatribu_i", ConvertUtil.nvl(codigoAtributoP));

		return getAllBackBoneInvoke(params, ATRIBUTO_PADRE_PRODUCTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.gseguros.wizard.configuracion.producto.service.AtributosVariablesManager#getPadreGarantia(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getPadreGarantia(String codigoRamo,
			String codigoGarantia, String cdTipSituacion, String codigoAtributoP)
			throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("pv_cdramo_i", codigoRamo);
		params.put("pv_cdgarant_i", codigoGarantia);
		params.put("pv_cdtipsit_i", cdTipSituacion);
		params.put("pv_cdatribu_i", ConvertUtil.nvl(codigoAtributoP));

		return getAllBackBoneInvoke(params, ATRIBUTO_PADRE_COBERTURA);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.gseguros.wizard.configuracion.producto.service.AtributosVariablesManager#getPadreRol(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getPadreRol(String codigoRamo, String codigoRol,
			String codigoAtributoP) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("pv_cdrol_i", codigoRol);
		params.put("pv_cdramo_i", codigoRamo);
		params.put("pv_cdatribu_i", ConvertUtil.nvl(codigoAtributoP));

		return getAllBackBoneInvoke(params, ATRIBUTO_PADRE_ROL);
	}
	
	
	public boolean tieneHijosAtributoVariableProducto(AtributosVariablesVO atributo) throws ApplicationException {
		boolean tieneHijos = false;
		String mensaje = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pv_cdramo_i", atributo.getCdRamo());
			params.put("pv_cdatribu_i", atributo.getCodigoSituacion());
			
			WrapperResultados res = returnBackBoneInvoke(params, "VALIDA_HIJOS_ATRIB_VAR_PRODUCTO");
			mensaje = (String)(res.getItemMap().get("pv_existe_o"));
			
		} catch (Exception bae) {
			logger.error("Se origino un error: " + bae.getMessage(), bae);
			throw new ApplicationException("Error intentando validar hijos de atributos variables");
		}
		
		
		if("1".equals(mensaje)){
			tieneHijos = true;
		}else{
			tieneHijos = false;
		}
		return tieneHijos;
	}

	public boolean tieneHijosAtributoVariableIncisoRiesgo(AtributosVariablesVO atributo) throws ApplicationException {
		String mensaje = null;
		boolean tieneHijos = false;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pv_cdtipsit_i", atributo.getCodigoSituacion());
			params.put("pv_cdatribu_i", atributo.getCodigoAtributo());
			
			WrapperResultados res = returnBackBoneInvoke(params, "VALIDA_HIJOS_ATRIB_VAR_INCISO_RIESGO");
			mensaje = (String)(res.getItemMap().get("pv_existe_o"));
			
		} catch (Exception bae) {
			logger.error("Se origino un error: " + bae.getMessage(), bae);
			throw new ApplicationException("Error intentando validar hijos de atributos variables");
		}
		if("1".equals(mensaje)){
			tieneHijos = true;
		}else{
			tieneHijos = false;
		}
		return tieneHijos;
	}

	public boolean tieneHijosAtributoVariableCobertura(AtributosVariablesVO atributo) throws ApplicationException {
		String mensaje = null;
		boolean tieneHijos = false;
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pv_cdramo_i", atributo.getCdRamo());
			params.put("pv_cdtipsit_i", atributo.getCodigoSituacion());
			params.put("pv_cdgarant_i", atributo.getCodigoGarantia());
			params.put("pv_cdatribu_i", atributo.getCodigoAtributo());
			
			WrapperResultados res = returnBackBoneInvoke(params, "VALIDA_HIJOS_ATRIB_VAR_COBERTURA");
			mensaje = (String)(res.getItemMap().get("pv_existe_o"));
			
		} catch (Exception bae) {
			logger.error("Se origino un error: " + bae.getMessage(), bae);
			throw new ApplicationException("Error intentando validar hijos de atributos variables");
		}
		if("1".equals(mensaje)){
			tieneHijos = true;
		}else{
			tieneHijos = false;
		}
		return tieneHijos;
	}
	
	
}