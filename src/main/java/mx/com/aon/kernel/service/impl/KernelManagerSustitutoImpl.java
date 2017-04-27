package mx.com.aon.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.flujos.cotizacion.model.AyudaCoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.CoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.SituacionVO;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.dao.ProcesoDAO;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

/**
 *
 * @author Jair
 */
@Deprecated
public class KernelManagerSustitutoImpl extends AbstractManagerJdbcTemplateInvoke implements KernelManagerSustituto
{
    
    protected final transient Logger log = Logger.getLogger(KernelManagerSustitutoImpl.class);
    
    public WrapperResultados calculaNumeroPoliza(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i) throws ApplicationException
    {
        log.debug("### kernel sustituto calculaNumeroPoliza param: "+pv_cdunieco_i+", "+pv_cdramo_i+", "+pv_estado_i);
        Map<String,String> parametros=new HashMap<String,String>(0);
        parametros.put("pv_cdunieco_i", pv_cdunieco_i);
        parametros.put("pv_cdramo_i", pv_cdramo_i);
        parametros.put("pv_estado_i", pv_estado_i);
        logger.debug(
        		new StringBuilder()
        		.append("\n********************************************")
        		.append("\n****** PKG_SATELITES.P_CALC_NUMPOLIZA ******")
        		.append("\n****** params=").append(parametros)
        		.append("\n********************************************")
        		.toString()
        		);
        WrapperResultados res=this.returnBackBoneInvoke(parametros, ProcesoDAO.CALCULA_NUMERO_POLIZA);
        log.debug("### kernel sustituto calculaNumeroPoliza numero de poliza calculado:"+res.getItemMap().get("NUMERO_POLIZA"));
        log.debug("### kernel sustituto calculaNumeroPoliza id:"+res.getMsgId());
        log.debug("### kernel sustituto calculaNumeroPoliza mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados insertaMaestroPolizas(Map<String, String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto insertaMaestroPolizas map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_MPOLIZAS);
        log.debug("### kernel sustituto insertaMaestroPolizas id:"+res.getMsgId());
        log.debug("### kernel sustituto insertaMaestroPolizas mesage:"+res.getMsgText());
        return res;
    }
    
    //PKG_SATELITES.P_MOV_MPOLISIT
    @Override
    public WrapperResultados insertaPolisit(Map<String, Object> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto insertaPolisit map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_MPOLISIT);
        log.debug("### kernel sustituto insertaPolisit id:"+res.getMsgId());
        log.debug("### kernel sustituto insertaPolisit mesage:"+res.getMsgText());
        return res;
    }
    
    //PKG_SATELITES.P_MOV_MPOLISIT
    @Override
    public WrapperResultados insertaPolisit(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsituac
    		,String nmsuplem
    		,String status
    		,String cdtipsit
    		,String swreduci
    		,String cdagrupa
    		,String cdestado
    		,Date   fefecsit
    		,Date   fecharef
    		,String cdgrupo
    		,String nmsituaext
    		,String nmsitaux
    		,String nmsbsitext
    		,String cdplan
    		,String cdasegur
    		,String accion) throws ApplicationException
	{
    	Map<String,Object>params = new HashMap<String,Object>();
    	params.put("pv_cdunieco_i"   , cdunieco);
    	params.put("pv_cdramo_i"     , cdramo);
    	params.put("pv_estado_i"     , estado);
    	params.put("pv_nmpoliza_i"   , nmpoliza);
    	params.put("pv_nmsituac_i"   , nmsituac);
    	params.put("pv_nmsuplem_i"   , nmsuplem);
    	params.put("pv_status_i"     , status);
    	params.put("pv_cdtipsit_i"   , cdtipsit);
    	params.put("pv_swreduci_i"   , swreduci);
    	params.put("pv_cdagrupa_i"   , cdagrupa);
    	params.put("pv_cdestado_i"   , cdestado);
    	params.put("pv_fefecsit_i"   , fefecsit);
    	params.put("pv_fecharef_i"   , fecharef);
    	params.put("pv_cdgrupo_i"    , cdgrupo);
    	params.put("pv_nmsituaext_i" , nmsituaext);
    	params.put("pv_nmsitaux_i"   , nmsitaux);
    	params.put("pv_nmsbsitext_i" , nmsbsitext);
    	params.put("pv_cdplan_i"     , cdplan);
    	params.put("pv_cdasegur_i"   , cdasegur);
    	params.put("pv_accion_i"     , accion);
    	return this.insertaPolisit(params);
	}
    
    //PKG_SATELITES2.P_MOV_TVALOSIT
    @Override
    public WrapperResultados insertaValoresSituaciones(Map<String, String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto insertaValoresSituaciones map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_TVALOSIT);
        log.debug("### kernel sustituto insertaValoresSituaciones id:"+res.getMsgId());
        log.debug("### kernel sustituto insertaValoresSituaciones mesage:"+res.getMsgText());
        return res;
    }
    
    //PKG_SATELITES2.P_MOV_TVALOSIT
    @Override
    public WrapperResultados insertaValoresSituaciones(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsituac
    		,String nmsuplem
    		,String status
    		,String cdtipsit
    		,String accion
    		,Map<String, String> otvalorValosit) throws ApplicationException
    {
    	Map<String,String>params=new HashMap<String,String>();
    	params.put("pv_cdunieco" , cdunieco);
		params.put("pv_cdramo"   , cdramo);
		params.put("pv_estado"   , estado);
		params.put("pv_nmpoliza" , nmpoliza);
		params.put("pv_nmsituac" , nmsituac);
		params.put("pv_nmsuplem" , nmsuplem);
		params.put("pv_status"   , status);
		params.put("pv_cdtipsit" , cdtipsit);
    	params.put("pv_accion_i" , accion);
    	params.putAll(otvalorValosit);
    	return this.insertaValoresSituaciones(params);
    }
    
    public WrapperResultados actualizaValoresSituaciones(Map<String, String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto actualizaValoresSituaciones map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_UPD_TVALOSIT);
        log.debug("### kernel sustituto actualizaValoresSituaciones id:"+res.getMsgId());
        log.debug("### kernel sustituto actualizaValoresSituaciones mesage:"+res.getMsgText());
        return res;
    }
    
    public List<SituacionVO> clonaSituaciones(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto clonaSituaciones map: "+parameters);
        List<SituacionVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.CLONAR_SITUACION);
        lista=lista!=null?lista:new ArrayList<SituacionVO>(0);
        log.debug("### kernel sustituto clonaSituaciones lista size: "+lista.size());
        return lista;
    }
    
    @Override
    public WrapperResultados coberturas(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto coberturas map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.EJECUTA_P_EXEC_SIGSVDEF);
        log.debug("### kernel sustituto coberturas id:"+res.getMsgId());
        log.debug("### kernel sustituto coberturas mesage:"+res.getMsgText());
        return res;
    }
    
    @Override
    public WrapperResultados coberturasEnd(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto coberturasEnd map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.EJECUTA_P_EXEC_SIGSVDEFEND);
        log.debug("### kernel sustituto coberturasEnd id:"+res.getMsgId());
        log.debug("### kernel sustituto coberturasEnd mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados ejecutaASIGSVALIPOL_EMI(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto ejecuta asigsvalipol_emi map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.EJECUTA_SIGSVALIPOL_EMI);
        log.debug("### kernel sustituto ejecuta asigsvalipol_emi id:"+res.getMsgId());
        log.debug("### kernel sustituto ejecuta asigsvalipol_emi mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados clonaPersonas(Map<String,Object> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto clonaPersonas map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_CLONAR_PERSONAS);
        log.debug("### kernel sustituto clonaPersonas id:"+res.getMsgId());
        log.debug("### kernel sustituto clonaPersonas mesage:"+res.getMsgText());
        return res;
    }
    
    public List<ResultadoCotizacionVO> obtenerResultadosCotizacion(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto obtenerResultadosCotizacion map: "+parameters);
        List<ResultadoCotizacionVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_RESULTADOS_COTIZACION);
        lista=lista!=null?lista:new ArrayList<ResultadoCotizacionVO>(0);
        log.debug("### kernel sustituto obtenerResultadosCotizacion lista="+lista);
        log.debug("### kernel sustituto obtenerResultadosCotizacion lista size: "+lista.size());
        return lista;
    }
    
    public List<Map<String,String>> obtenerResultadosCotizacion2(Map<String,String> params) throws ApplicationException
    {
        log.debug("### kernel sustituto obtenerResultadosCotizacion2 map: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.OBTENER_RESULTADOS_COTIZACION2);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerResultadosCotizacion2 lista size: "+lista.size());
        return lista;
    }
    
    public List<CoberturaCotizacionVO> obtenerCoberturas(Map<String,String> parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto obtenerCoberturas map: "+parameters);
        List<CoberturaCotizacionVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_COBERTURAS);
        lista=lista!=null?lista:new ArrayList<CoberturaCotizacionVO>(0);
        log.debug("### kernel sustituto obtenerCoberturas lista size: "+lista.size());
        return lista;
    }
    
    public AyudaCoberturaCotizacionVO obtenerAyudaCobertura(String idCobertura,String idRamo,String idCiaAsegurador) throws ApplicationException
    {
        Map<String,Object>parameters=new HashMap<String,Object>(0);
        parameters.put("pv_garant_i",idCobertura);
        parameters.put("pv_cdramo_i",idRamo);
        parameters.put("pv_ciaaseg_i",idCiaAsegurador);
        log.debug("### kernel sustituto obtenerAyudaCobertura map: "+parameters);
        AyudaCoberturaCotizacionVO res=(AyudaCoberturaCotizacionVO) this.getBackBoneInvoke(parameters, ProcesoDAO.OBTENER_AYUDA_COBERTURA);
        log.debug("### kernel sustituto obtenerAyudaCobertura return: "+res);
        return res;
    }
    
    @Deprecated
    public DatosUsuario obtenerDatosUsuario(String cdusuario,String cdtipsit) throws ApplicationException
    {
        Map<String,Object>parameters=new HashMap<String,Object>(0);
        parameters.put("pv_cdusuari_i",cdusuario);
        parameters.put("pv_cdtipsit_i",cdtipsit);
        log.debug("### kernel sustituto obtenerDatosUsuario map: "+parameters);
        log.debug(
        		new StringBuilder()
        		.append("\n**********************************************")
        		.append("\n****** pkg_satelites.p_get_info_usuario ******")
        		.append("\n****** params=").append(parameters)
        		.append("\n**********************************************")
        		.toString()
        		);
        DatosUsuario res=(DatosUsuario) this.getBackBoneInvoke(parameters, ProcesoDAO.OBTENER_DATOS_USUARIO);
        log.debug("### kernel sustituto obtenerDatosUsuario return: "+res);
        return res;
    }
    
    @Deprecated
    public List<ComponenteVO> obtenerTatrisit(String cdtipsit,String cdusuari) throws ApplicationException
    {
        Map<String,Object> parameters=new HashMap<String,Object>(0);
        parameters.put("pv_cdtipsit_i",cdtipsit);
        parameters.put("pv_cdusuari_i",cdusuari);
        log.debug("### kernel sustituto obtenerTatrisit map: "+parameters);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRISIT);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
        log.debug("### kernel sustituto obtenerTatrisit lista size: "+lista.size());
        return lista;
    }
    
    public List<ComponenteVO> obtenerTatrisin(String cdramo,String cdtipsit) throws ApplicationException
    {
        Map<String,Object> parameters=new HashMap<String,Object>(0);
        parameters.put("pv_cdramo_i"   , cdramo);
        parameters.put("pv_cdtipsit_i" , cdtipsit);
        log.debug("### kernel sustituto obtenerTatrisin map: "+parameters);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRISIN);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
        log.debug("### kernel sustituto obtenerTatrisin lista size: "+lista.size());
        return lista;
    }
    public List<ComponenteVO> obtenerTatrisinPoliza(String cdunieco,String cdramo,String estado,String nmpoliza) throws ApplicationException
    {
    	Map<String,Object> parameters=new HashMap<String,Object>(0);
    	parameters.put("cdunieco" , cdunieco);
        parameters.put("cdramo"   , cdramo);
        parameters.put("estado"   , estado);
        parameters.put("nmpoliza" , nmpoliza);
        log.debug("### kernel sustituto obtenerTatrisinPoliza map: "+parameters);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRISIN_POLIZA);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
        log.debug("### kernel sustituto obtenerTatrisinPoliza lista size: "+lista.size());
        return lista;
    }
    
    public List<ComponenteVO> obtenerTatripol(String args[]) throws ApplicationException
    {
        Map<String,Object> parameters=new HashMap<String,Object>(0);
        parameters.put("pv_cdramo"   , args[0]);
        parameters.put("pv_cdtipsit" , args[1]);
        parameters.put("pv_cdtippol" , args[2]);
        log.debug("### kernel sustituto obtenerTatripol map: "+parameters);
		log.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRI_POLIZA ******")
				.append("\n****** params=").append(parameters)
				.append("\n******************************************")
				.toString()
				);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRIPOL);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
		log.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRI_POLIZA ******")
				.append("\n****** params=").append(parameters)
				.append("\n****** registro=").append(lista)
				.append("\n******************************************")
				.toString()
				);
        return lista;
    }
    
    @Deprecated
    public List<ComponenteVO> obtenerTatrigar(Map<String,String>parameters) throws ApplicationException
    {
    	if(!parameters.containsKey("pv_cdatrivar_i"))
    	{
    		parameters.put("pv_atrivar_i" , null);
    	}
        log.debug(
        		new StringBuilder()
        		.append("\n********************************************")
        		.append("\n****** PKG_LISTAS.P_GET_ATRI_GARANTIA ******")
        		.append("\n****** params=").append(parameters)
        		.append("\n********************************************")
        		.toString()
        		);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRIGAR);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
        log.debug(
        		new StringBuilder()
        		.append("\n********************************************")
        		.append("\n****** PKG_LISTAS.P_GET_ATRI_GARANTIA ******")
        		.append("\n****** params=")  .append(parameters)
        		.append("\n****** registro=").append(lista)
        		.append("\n********************************************")
        		.toString()
        		);
        return lista;
    }
    
    public List<ComponenteVO> obtenerTatriper(Map<String,String>parameters) throws ApplicationException
    {
        log.debug("### kernel sustituto obtenerTatriper map: "+parameters);
        List<ComponenteVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TATRIPER);
        lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
        log.debug("### kernel sustituto obtenerTatriper lista size: "+lista.size());
        return lista;
    }
    
    public WrapperResultados comprarCotizacion(Map<String,String> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto comprarCotizacion map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.COMPRAR_COTIZACION);
        log.debug("### kernel sustituto comprarCotizacion id:"+res.getMsgId());
        log.debug("### kernel sustituto comprarCotizacion mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados movDetalleSuplemento(Map<String,Object> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto movDetalleSuplemento map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.INSERTAR_DETALLE_SUPLEMEN);
        log.debug("### kernel sustituto movDetalleSuplemento id:"+res.getMsgId());
        log.debug("### kernel sustituto movDetalleSuplemento mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados movBitacobro(String cdunieco,String cdramo,String estado,String poliza,String nmsuplem,String codigo,String mensaje, String usuario, String ntramite, String cdurlws, String metodows, String xmlin, String cderrws) throws ApplicationException
    {
    	Map<String,String>parameters=new LinkedHashMap<String,String>(0);
    	parameters.put("pv_cdunieco_i" , cdunieco);
    	parameters.put("pv_cdramo_i"   , cdramo);
    	parameters.put("pv_estado_i"   , estado);
    	parameters.put("pv_nmpoliza_i" , poliza);
    	parameters.put("pv_nmsuplem_i" , nmsuplem);
    	parameters.put("pv_cdcodigo_i" , codigo);
    	parameters.put("pv_mensaje_i"  , mensaje);
    	parameters.put("pv_usuario_i"  , usuario);
    	parameters.put("pv_ntramite_i" , ntramite);
    	parameters.put("pv_cdurlws_i"  , cdurlws);
    	parameters.put("pv_metodows_i" , metodows);
    	parameters.put("pv_xmlin_i"    , xmlin);
    	parameters.put("pv_cderrws_i"  , cderrws);
    	log.debug("### kernel sustituto movBitacobro map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_TBITACOBROS);
        log.debug("### kernel sustituto movBitacobro id:"+res.getMsgId());
        log.debug("### kernel sustituto movBitacobro mesage:"+res.getMsgText());
        return res;
    }

    @Override
    public Map<String,Object> getInfoMpolizas(Map<String, Object> parameters) throws ApplicationException {
        log.debug("### kernel sustituto getInfoMpolizas map: "+parameters);
        Map<String,Object> map=(Map<String,Object>) this.getBackBoneInvoke(parameters, ProcesoDAO.GET_INFO_MPOLIZAS);
        log.debug("### kernel sustituto response map: "+map);
        return map;
    }
    
    @Override
    public Map<String,Object> getInfoMpolizas(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String cdusuari) throws ApplicationException {
    	Map<String, Object> params = new HashMap<String, Object>(0);
		params.put("pv_cdunieco" , cdunieco);
		params.put("pv_cdramo"   , cdramo);
		params.put("pv_estado"   , estado);
		params.put("pv_nmpoliza" , nmpoliza);
		params.put("pv_cdusuari" , cdusuari);
		return this.getInfoMpolizas(params);
    }
    
    public List<GenericVO> getTmanteni(String tabla) throws ApplicationException
    {
        log.debug("### kernel sustituto getTmanteni tabla: "+tabla);
        Map<String,String> parameters=new HashMap<String,String>(0);
        parameters.put("pv_cdtabla",tabla);
        log.debug(
        		new StringBuilder()
        		.append("\n***************************************")
        		.append("\n****** PKG_LISTAS.P_GET_TMANTENI ******")
        		.append("\n****** params=").append(parameters)
        		.append("\n***************************************")
        		.toString()
        		);
        List<GenericVO> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_TMANTENI);
        lista=lista!=null?lista:new ArrayList<GenericVO>(0);
        log.debug("### kernel sustituto obtenerTatrisit lista size: "+lista.size());
        return lista;
    }
    
    //PKG_SATELITES.P_OBT_DATOS_MPOLIPER
    @Override
    public List<Map<String, Object>> obtenerAsegurados(Map<String, String> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto obtenerAsegurados parameters: "+parameters);
        List<Map<String,Object>> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_ASEGURADOS);
        lista=lista!=null?lista:new ArrayList<Map<String,Object>>(0);
        log.debug("### kernel sustituto obtenerAsegurados lista size: "+lista.size());
        return lista;
    }
    
    //PKG_SATELITES.P_OBT_DATOS_MPOLIPER
    /*
    nmsituac
	,cdrol
	,fenacimi
	,sexo
	,cdperson
	,nombre
	,segundo_nombre
	,Apellido_Paterno
	,Apellido_Materno
	,cdrfc
	,Parentesco
	,tpersona
	,nacional
	,swexiper
	*/
    @Override
    public List<Map<String, Object>> obtenerAsegurados(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsuplem) throws ApplicationException
    {
    	Map<String,String>parameters=new HashMap<String,String>();
    	parameters.put("pv_cdunieco" , cdunieco);
    	parameters.put("pv_cdramo"   , cdramo);
    	parameters.put("pv_estado"   , estado);
    	parameters.put("pv_nmpoliza" , nmpoliza);
    	parameters.put("pv_nmsuplem" , nmsuplem);
        return this.obtenerAsegurados(parameters);
    }
    
    public Map<String,Object> getInfoMpolizasCompleta(Map<String,String> parameters) throws ApplicationException {
        log.debug("### kernel sustituto getInfoMpolizasCompleta map: "+parameters);
        Map<String,Object> map=(Map<String,Object>) this.getBackBoneInvoke(parameters, ProcesoDAO.OBTENER_POLIZA_COMPLETA);
        log.debug("### kernel sustituto getInfoMpolizasCompleta response map: "+map);
        return map;
    }
    
    public WrapperResultados pMovTvalopol(Map<String, String> parameters) throws ApplicationException
    {
    	String[] inputKeys=new String[]{
    			"pv_cdunieco",
        		"pv_cdramo",
        		"pv_estado",
        		"pv_nmpoliza",
        		"pv_nmsuplem",
        		"pv_status",
        		"pv_otvalor01",
        		"pv_otvalor02",
        		"pv_otvalor03",
        		"pv_otvalor04",
        		"pv_otvalor05",
        		"pv_otvalor06",
        		"pv_otvalor07",
        		"pv_otvalor08",
        		"pv_otvalor09",
        		"pv_otvalor10",
        		"pv_otvalor11",
        		"pv_otvalor12",
        		"pv_otvalor13",
        		"pv_otvalor14",
        		"pv_otvalor15",
        		"pv_otvalor16",
        		"pv_otvalor17",
        		"pv_otvalor18",
        		"pv_otvalor19",
        		"pv_otvalor20",
        		"pv_otvalor21",
        		"pv_otvalor22",
        		"pv_otvalor23",
        		"pv_otvalor24",
        		"pv_otvalor25",
        		"pv_otvalor26",
        		"pv_otvalor27",
        		"pv_otvalor28",
        		"pv_otvalor29",
        		"pv_otvalor30",
        		"pv_otvalor31",
        		"pv_otvalor32",
        		"pv_otvalor33",
        		"pv_otvalor34",
        		"pv_otvalor35",
        		"pv_otvalor36",
        		"pv_otvalor37",
        		"pv_otvalor38",
        		"pv_otvalor39",
        		"pv_otvalor40",
        		"pv_otvalor41",
        		"pv_otvalor42",
        		"pv_otvalor43",
        		"pv_otvalor44",
        		"pv_otvalor45",
        		"pv_otvalor46",
        		"pv_otvalor47",
        		"pv_otvalor48",
        		"pv_otvalor49",
        		"pv_otvalor50",
    	};
    	for(String key:inputKeys)
    	{
    		if(!parameters.containsKey(key))
    		{
    			parameters.put(key, null);
    		}
    	}
    	log.debug("### kernel sustituto pMovTvalopol map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_TVALOPOL);
        log.debug("### kernel sustituto pMovTvalopol id:"+res.getMsgId());
        log.debug("### kernel sustituto pMovTvalopol mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados pMovTvalogar(Map<String, String> parameters) throws ApplicationException
    {
    	List<String>inputKeys=new ArrayList<String>();
    	inputKeys.add("pv_cdunieco");
    	inputKeys.add("pv_cdramo");
    	inputKeys.add("pv_estado");
    	inputKeys.add("pv_nmpoliza");
    	inputKeys.add("pv_nmsituac");
    	inputKeys.add("pv_cdgarant");
    	inputKeys.add("pv_nmsuplem");
    	inputKeys.add("pv_status");
    	for(int i=1;i<=640;i++)
    	{
    		inputKeys.add(new StringBuilder().append("pv_otvalor").append(StringUtils.leftPad(String.valueOf(i),3,"0")).toString());
    	}
    	for(String key:inputKeys)
    	{
    		if(!parameters.containsKey(key))
    		{
    			parameters.put(key, null);
    		}
    	}
    	log.debug("### kernel sustituto pMovTvalogar map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_TVALOGAR);
        log.debug("### kernel sustituto pMovTvalogar id:"+res.getMsgId());
        log.debug("### kernel sustituto pMovTvalogar mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados pMovTvaloper(Map<String, String> parameters) throws ApplicationException
    {
    	String[] inputKeys=new String[]{
				"pv_cdunieco","pv_cdramo","pv_estado","pv_nmpoliza","pv_nmsituac","pv_nmsuplem","pv_status","pv_cdrol","pv_cdperson","pv_cdatribu","pv_cdtipsit",
                "pv_otvalor01","pv_otvalor02","pv_otvalor03","pv_otvalor04","pv_otvalor05","pv_otvalor06","pv_otvalor07","pv_otvalor08","pv_otvalor09","pv_otvalor10",
                "pv_otvalor11","pv_otvalor12","pv_otvalor13","pv_otvalor14","pv_otvalor15","pv_otvalor16","pv_otvalor17","pv_otvalor18","pv_otvalor19","pv_otvalor20",
                "pv_otvalor21","pv_otvalor22","pv_otvalor23","pv_otvalor24","pv_otvalor25","pv_otvalor26","pv_otvalor27","pv_otvalor28","pv_otvalor29","pv_otvalor30",
                "pv_otvalor31","pv_otvalor32","pv_otvalor33","pv_otvalor34","pv_otvalor35","pv_otvalor36","pv_otvalor37","pv_otvalor38","pv_otvalor39","pv_otvalor40",
                "pv_otvalor41","pv_otvalor42","pv_otvalor43","pv_otvalor44","pv_otvalor45","pv_otvalor46","pv_otvalor47","pv_otvalor48","pv_otvalor49","pv_otvalor50",
                "pv_otvalor51","pv_otvalor52","pv_otvalor53","pv_otvalor54","pv_otvalor55","pv_otvalor56","pv_otvalor57","pv_otvalor58","pv_otvalor59","pv_otvalor60",
                "pv_otvalor61","pv_otvalor62","pv_otvalor63","pv_otvalor64","pv_otvalor65","pv_otvalor66","pv_otvalor67","pv_otvalor68","pv_otvalor69","pv_otvalor70",
                "pv_otvalor71","pv_otvalor72","pv_otvalor73","pv_otvalor74","pv_otvalor75","pv_otvalor76","pv_otvalor77","pv_otvalor78","pv_otvalor79","pv_otvalor80",
                "pv_otvalor81","pv_otvalor82","pv_otvalor83","pv_otvalor84","pv_otvalor85","pv_otvalor86","pv_otvalor87","pv_otvalor88","pv_otvalor89","pv_otvalor90",
                "pv_otvalor91","pv_otvalor92","pv_otvalor93","pv_otvalor94","pv_otvalor95","pv_otvalor96","pv_otvalor97","pv_otvalor98","pv_otvalor99"
		};
    	for(String key:inputKeys)
    	{
    		if(!parameters.containsKey(key))
    		{
    			parameters.put(key, null);
    		}
    	}
    	log.debug("### kernel sustituto pMovTvaloper map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_TVALOPER);
        log.debug("### kernel sustituto pMovTvaloper id:"+res.getMsgId());
        log.debug("### kernel sustituto pMovTvaloper mesage:"+res.getMsgText());
        return res;
    }
    
    //requiere de su propio catch si no hay datos:
    public Map<String,Object> pGetTvalopol(Map<String,String> parameters) throws ApplicationException {
        log.debug("### kernel sustituto pGetTvalopol map: "+parameters);
        Map<String,Object>map=(Map<String,Object>) this.getBackBoneInvoke(parameters, ProcesoDAO.P_GET_TVALOPOL);
        log.debug("### kernel sustituto pGetTvalopol response map: "+map);
        return map;
    }
    
    public String generaCdperson() throws ApplicationException
    {
        log.debug("### kernel sustituto generaCdperson");
        WrapperResultados res=this.returnBackBoneInvoke(new HashMap<String,String>(0), ProcesoDAO.GENERA_MPERSON);
        log.debug("### kernel sustituto generaCdperson cdperson generado:"+res.getItemMap().get("CDPERSON"));
        log.debug("### kernel sustituto generaCdperson id:"+res.getMsgId());
        log.debug("### kernel sustituto generaCdperson mesage:"+res.getMsgText());
        return (String)res.getItemMap().get("CDPERSON");
    }
    
    public WrapperResultados movMpersona(Map<String,Object> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto movMpersona map: "+parameters);
    	if (parameters != null) {
    	    if (!parameters.containsKey("pv_dsocupacion_i")) {
    	        parameters.put("pv_dsocupacion_i", null);
    	    }
    	}
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_MPERSONA);
        log.debug("### kernel sustituto movMpersona id:"+res.getMsgId());
        log.debug("### kernel sustituto movMpersona mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados movMpoliper(Map<String,Object> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto movMpoliper map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_MOV_MPOLIPER);
        log.debug("### kernel sustituto movMpoliper id:"+res.getMsgId());
        log.debug("### kernel sustituto movMpoliper mesage:"+res.getMsgText());
        return res;
    }
    
    public WrapperResultados borraMpoliper(Map<String,Object> parameters) throws ApplicationException
    {
    	log.debug("### kernel sustituto borraMpoliper map: "+parameters);
        WrapperResultados res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_BORRA_MPOLIPER);
        log.debug("### kernel sustituto borraMpoliper id:"+res.getMsgId());
        log.debug("### kernel sustituto borraMpoliper mesage:"+res.getMsgText());
        return res;
    }

    public WrapperResultados existeDomicilioContratante(String cdideper, String cdperson){
    	log.debug("buscando datos Domicilio para cdideper: " + cdideper);
    	WrapperResultados res = null;
    	try{
    		HashMap<String, String> parameters =  new HashMap<String, String>();
    		parameters.put("pv_cdideper_i", cdideper);
    		parameters.put("pv_cdperson_i", cdperson);
    		res=this.returnBackBoneInvoke(parameters, ProcesoDAO.P_EXISTE_DOMICILIO);
    		
    		log.debug("REsultados de Buscar Domicilio: " + res.getItemMap());
    	}catch(Exception e){
    		log.error(e);
    	}
    	return res;
    }
    
    public List<Map<String,String>> obtenerCoberturasUsuario(Map<String,String> parametros) throws ApplicationException
    {
    	log.debug("### kernel sustituto obtenerCoberturasUsuario parametros: "+parametros);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(parametros, ProcesoDAO.OBTENER_COBERTURAS_USUARIO);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerCoberturasUsuario lista size: "+lista.size());
        return lista;
    }
    
    public WrapperResultados movPoligar(Map<String, String> param) throws ApplicationException
    {
    	log.debug("### kernel sustituto movPoligar map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.P_MOV_MPOLIGAR);
        log.debug("### kernel sustituto movPoligar id:"+res.getMsgId());
        log.debug("### kernel sustituto movPoligar mesage:"+res.getMsgText());
        return res;
    }
    
	public WrapperResultados movPolicap(Map<String, String> param) throws ApplicationException
	{
		log.debug("### kernel sustituto movPolicap map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.P_MOV_MPOLICAP);
        log.debug("### kernel sustituto movPolicap id:"+res.getMsgId());
        log.debug("### kernel sustituto movPolicap mesage:"+res.getMsgText());
        return res;
	}
	
	public List<Map<String, String>> obtenerDetallesCotizacion(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerDetallesCotizacion parametros: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.OBTENER_DETALLES_COTIZACION);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerDetallesCotizacion lista size: "+lista.size());
        return lista;
	}
    
	public Map<String, Object> obtenerValoresTatrigar(Map<String, String> parameters) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerValoresTatrigar map: "+parameters);
        Map<String,Object>map=(Map<String,Object>) this.getBackBoneInvoke(parameters, ProcesoDAO.P_GET_TVALOGAR);
        log.debug("### kernel sustituto obtenerValoresTatrigar response map: "+map);
        return map;
	}
	
	public Map<String, Object> obtenerValoresTatriper(Map<String, String> parameters) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerValoresTatriper map: "+parameters);
        Map<String,Object>map=(Map<String,Object>) this.getBackBoneInvoke(parameters, ProcesoDAO.P_GET_TVALOPER);
        log.debug("### kernel sustituto obtenerValoresTatriper response map: "+map);
        return map;
	}
	
	public Map<String, String> obtenerDomicilio(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerDomicilio map: "+params);
        Map<String,String>map=(Map<String,String>) this.getBackBoneInvoke(params, ProcesoDAO.P_GET_DOMICIL);
        log.debug("### kernel sustituto obtenerDomicilio response map: "+map);
        return map;
	}

	public Map<String, String> obtenerDomicilioGeneral(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerDomicilio map: "+params);
		Map<String,String>map=(Map<String,String>) this.getBackBoneInvoke(params, ProcesoDAO.P_GET_DOMICIL_GENERAL);
		log.debug("### kernel sustituto obtenerDomicilio response map: "+map);
		return map;
	}
	
	public WrapperResultados pMovMdomicil(Map<String, String> param) throws ApplicationException
	{
		/*
		pv_cdperson_i smap1.pv_cdperson
		pv_nmorddom_i smap1.NMORDDOM
		pv_msdomici_i smap1.DSDOMICI qwe
		pv_nmtelefo_i smap1.NMTELEFO
		pv_cdpostal_i smap1.CODPOSTAL
		pv_cdedo_i    smap1.CDEDO
		pv_cdmunici_i smap1.CDMUNICI
		pv_cdcoloni_i smap1.CDCOLONI
		pv_nmnumero_i smap1.NMNUMERO
		pv_nmnumint_i smap1.NMNUMINT
		pv_accion_i   #U
		pv_msg_id_o   -
		pv_title_o    -
		*/
		log.debug("### kernel sustituto pMovMdomicil map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.P_MOV_MDOMICIL);
        log.debug("### kernel sustituto pMovMdomicil id:"+res.getMsgId());
        log.debug("### kernel sustituto pMovMdomicil mesage:"+res.getMsgText());
        return res;
	}
	
	public WrapperResultados emitir(Map<String, Object> param) throws ApplicationException
	{
		log.debug("### kernel sustituto emitir map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.EMITIR);
        log.debug("### kernel sustituto emitir id:"+res.getMsgId());
        log.debug("### kernel sustituto emitir mesage:"+res.getMsgText());
        return res;
	}

	/*@Deprecated
	public WrapperResultados guardarArchivo(Map<String, Object> params) throws ApplicationException
	{
		if(params!=null)
		{
			if(!params.containsKey("pv_codidocu_i"))
			{
				params.put("pv_codidocu_i",null);
			}
			if(!params.containsKey("pv_cdtiptra_i"))
			{
				params.put("pv_cdtiptra_i","1");
			}
			if(!params.containsKey("cdorddoc"))
			{
				params.put("cdorddoc" , null);
			}
			if(!params.containsKey("cdmoddoc"))
			{
				params.put("cdmoddoc" , null);
			}
		}
		log.debug("### kernel sustituto guardarArchivo map: "+params);
        WrapperResultados res=this.returnBackBoneInvoke(params, ProcesoDAO.GUARDAR_ARCHIVO_POLIZA);
        log.debug("### kernel sustituto guardarArchivo id:"+res.getMsgId());
        log.debug("### kernel sustituto guardarArchivo mesage:"+res.getMsgText());
        return res;
	}
	*/
	
	public WrapperResultados guardarArchivoPersona(String cdperson, Date fecha, String cddocume, String dsdocume,String codidocu) throws ApplicationException
	{
		Map<String,Object>param=new HashMap<String,Object>();
		param.put("cdperson" , cdperson);
		param.put("feinici"  , fecha);
		param.put("cddocume" , cddocume);
		param.put("dsdocume" , dsdocume);
		param.put("codidocu" , codidocu);
		log.debug("### kernel sustituto guardarArchivo map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.GUARDAR_ARCHIVO_PERSONA);
        log.debug("### kernel sustituto guardarArchivo id:"+res.getMsgId());
        log.debug("### kernel sustituto guardarArchivo mesage:"+res.getMsgText());
        return res;
	}
	
	public List<Map<String,String>>obtenerDocumentosPoliza(Map<String,Object>parameters) throws ApplicationException
	{
		if(!parameters.containsKey("pv_cdsisrol_i"))
		{
			parameters.put("pv_cdsisrol_i" , ((UserVO)ActionContext.getContext().getSession().get("USUARIO")).getRolActivo().getClave());
		}
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(parameters, ProcesoDAO.OBTENER_DOCUMENTOS_POLIZA);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug(Utils.log("### kernel sustituto obtenerDocumentosPoliza lista : ", lista));
        return lista;
	}
	
	public WrapperResultados insertaMaestroHistoricoPoliza(Map<String, Object> param) throws ApplicationException
	{
		log.debug("### kernel sustituto insertaMaestroHistoricoPoliza map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.INSERTA_MAESTRO_HISTORICO_POLIZAS);
        log.debug("### kernel sustituto insertaMaestroHistoricoPoliza id:"+res.getMsgId());
        log.debug("### kernel sustituto insertaMaestroHistoricoPoliza mesage:"+res.getMsgText());
        return res;
	}
	
	public WrapperResultados movMPoliage(Map<String, Object> param) throws ApplicationException
	{
		log.debug("### kernel Mov Poliage map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.MOV_MPOLIAGE);
        log.debug("### kernel sustituto guardMov PoliagearArchivo id:"+res.getMsgId());
        log.debug("### kernel sustituto Mov Poliage mesage:"+res.getMsgText());
        return res;
	}
	
	public List<Map<String, String>> cargarTiposClausulasExclusion() throws ApplicationException
	{
		log.debug("### kernel sustituto cargarTiposClausulasExclusion sin parametros");
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(new HashMap<String,String>(0), ProcesoDAO.OBTENER_TIPOS_CLAUSULAS_EXCLUSION);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto cargarTiposClausulasExclusion lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> obtenerExclusionesPorTipo(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerExclusionesPorTipo params: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.OBTENER_EXCLUSIONES_X_TIPO);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerExclusionesPorTipo lista size: "+lista.size());
        return lista;
	}
	
	public Map<String, String> obtenerHtmlClausula(Map<String, String> params) throws ApplicationException
	{
        log.debug("### kernel sustituto obtenerHtmlClausula map: "+params);
        Map<String,String>map=(Map<String,String>) this.getBackBoneInvoke(params, ProcesoDAO.OBTENER_HTML_CLAUSULA);
        log.debug("### kernel sustituto obtenerHtmlClausula response map: "+map);
        return map;
	}
	
	/*
	public WrapperResultados PMovMesacontrol(Map<String, Object> param) throws ApplicationException
	{
		if(param!=null)
		{
			for(int i=1;i<=50;i++)
			{
				String cdatribu=i+"";
				if(cdatribu.length()<2)
				{
					cdatribu = "0" + cdatribu;
				}
				String key = "pv_otvalor"+cdatribu;
				if((!param.containsKey(key))&&(!param.containsKey(key.toLowerCase())))
				{
					param.put(key,null);
				}
			}
			if(!param.containsKey("swimpres"))
			{
				param.put("swimpres",null);
			}
		}
		log.debug("### kernel PMovMesacontrol map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.P_MOV_MESACONTROL);
        log.debug("### kernel sustituto PMovMesacontrol id:"+res.getMsgId());
        log.debug("### kernel sustituto PMovMesacontrol mesage:"+res.getMsgText());
        return res;
	}
	*/
	
	public WrapperResultados PMovTvalosin(Map<String, Object> param) throws ApplicationException
	{
		if(param!=null)
		{
			for(int i=1;i<=50;i++)
			{
				String cdatribu=i+"";
				if(cdatribu.length()<2)
				{
					cdatribu = "0" + cdatribu;
				}
				String key = "pv_otvalor"+cdatribu;
				if((!param.containsKey(key))&&(!param.containsKey(key.toLowerCase())))
				{
					param.put(key,null);
				}
			}
		}
		log.debug("### kernel PMovTvalosin map: "+param);
        WrapperResultados res=this.returnBackBoneInvoke(param, ProcesoDAO.P_MOV_TVALOSIN);
        log.debug("### kernel sustituto PMovTvalosin id:"+res.getMsgId());
        log.debug("### kernel sustituto PMovTvalosin mesage:"+res.getMsgText());
        return res;
	}
	
	/*public WrapperResultados movDmesacontrol(Map<String, Object> params) throws ApplicationException
	{
		log.debug("### kernel movDmesacontrol map: "+params);
        WrapperResultados res=this.returnBackBoneInvoke(params, ProcesoDAO.P_MOV_DMESACONTROL);
        log.debug("### kernel sustituto movDmesacontrol id:"+res.getMsgId());
        log.debug("### kernel sustituto movDmesacontrol mesage:"+res.getMsgText());
        return res;
	}*/
	
	public List<Map<String, String>> loadMesaControl(Map<String,String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto loadMesaControl map: "+params);
		/////////////////////////////////////////////////////////////////
		////// transformacion de mapa de strings a mapa de objetos //////
		/*/////////////////////////////////////////////////////////////*
		Map<String,Object>omap=new LinkedHashMap<String,Object>(0);
		SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
		Iterator it=param.entrySet().iterator();
		while(it.hasNext())
		{
			Entry en=(Entry)it.next();
			if(en.getKey().equals("pv_fedesde_i")||en.getKey().equals("pv_fehasta_i"))
			{
				if(en.getValue()!=null&&((String)en.getValue()).length()>0)
				{
					try
					{
						omap.put(en.getKey()+"",renderFechas.parse((String)en.getValue()));//poner fecha
					}
					catch(Exception ex)
					{
						log.error("error al convertir cadena a fecha",ex);
						omap.put(en.getKey()+"",null);
					}
				}
				else
				{
					omap.put(en.getKey()+"",null);//poner el nulo o cadena vacia
				}
			}
			else
			{
				omap.put(en.getKey()+"",en.getValue());//poner cadena
			}
		}
		/*/////////////////////////////////////////////////////////////*/
		////// transformacion de mapa de strings a mapa de objetos //////
		/////////////////////////////////////////////////////////////////
		
		if(params!=null)
		{
			if(!params.containsKey("cdtipram"))
			{
				params.put("cdtipram" , null);
			}
			if(!params.containsKey("lote"))
			{
				params.put("lote" , null);
			}
			if(!params.containsKey("tipolote"))
			{
				params.put("tipolote" , null);
			}
			if(!params.containsKey("tipoimpr"))
			{
				params.put("tipoimpr" , null);
			}
			if(!params.containsKey("cdusuari_busq"))
			{
				params.put("cdusuari_busq" , null);
			}
			if(!params.containsKey("dscontra")){
                params.put("dscontra" , null);
            }
		}
		
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.LOAD_MESA_CONTROL);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto loadMesaControl lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> loadMesaControlUsuario(Map<String,String> param) throws ApplicationException
	{
		log.debug("### kernel sustituto loadMesaControlUsuario map: "+param);
		/////////////////////////////////////////////////////////////////
		////// transformacion de mapa de strings a mapa de objetos //////
		/*/////////////////////////////////////////////////////////////*
		Map<String,Object>omap=new LinkedHashMap<String,Object>(0);
		SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
		Iterator it=param.entrySet().iterator();
		while(it.hasNext())
		{
			Entry en=(Entry)it.next();
			if(en.getKey().equals("pv_fedesde_i")||en.getKey().equals("pv_fehasta_i"))
			{
				if(en.getValue()!=null&&((String)en.getValue()).length()>0)
				{
					try
					{
						omap.put(en.getKey()+"",renderFechas.parse((String)en.getValue()));//poner fecha
					}
					catch(Exception ex)
					{
						log.error("error al convertir cadena a fecha",ex);
						omap.put(en.getKey()+"",null);
					}
				}
				else
				{
					omap.put(en.getKey()+"",null);//poner el nulo o cadena vacia
				}
			}
			else
			{
				omap.put(en.getKey()+"",en.getValue());//poner cadena
			}
		}
		/*/////////////////////////////////////////////////////////////*/
		////// transformacion de mapa de strings a mapa de objetos //////
		/////////////////////////////////////////////////////////////////
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(param, ProcesoDAO.LOAD_MESA_CONTROL_USUARIO);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto loadMesaControlusuario lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> loadMesaControlSuper(Map<String,String>params) throws ApplicationException
	{
		log.debug("### kernel sustituto loadMesaControlSuper map: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.P_OBTIENE_MESACONTROL_SUPER);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto loadMesaControlSuper lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> obtenerDetalleMC(Map<String, String> param) throws ApplicationException
	{
		log.debug("### kernel sustituto obtenerDetalleMC map: "+param);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(param, ProcesoDAO.LOAD_DETALLE_MESA_CONTROL);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerDetalleMC lista size: "+lista.size());
        return lista;
	}

	public WrapperResultados obtenDatosRecibos(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_RECIBOS);
		return result;
	}
	
	public WrapperResultados obtenDatosComisiones(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_COMISIONES);
		return result;
	}

	public WrapperResultados obtenDatosRecibosDxN(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_RECIBOS_DxN);
		return result;
	}

	public WrapperResultados obtenDatosClienteWS(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_CLIENTE);
		return result;
	}

	public WrapperResultados obtenDatosClienteGeneralWS(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_CLIENTE_GENERAL);
		return result;
	}
	public WrapperResultados obtenDatosClienteGeneralWSporCdperson(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_CLIENTE_GENERAL_X_CDPERSON);
		return result;
	}
	public WrapperResultados obtenDatosDomicilioGeneralWSporCdperson(HashMap<String,Object> params) throws ApplicationException
	{
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_DOMICILIO_GENERAL_X_CDPERSON);
		return result;
	}
	
	public WrapperResultados cargaColonias(String codigoPostal) throws ApplicationException
	{
		HashMap<String,Object> params =  new HashMap<String, Object>();
		params.put("pv_codpostal_i", codigoPostal);
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_CATALOGO_COLONIAS);
		return result;
	}

	public WrapperResultados mesaControlUpdateSolici(String ntramite,String nmsolici) throws ApplicationException
	{
		HashMap<String,Object> params =  new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nmsolici_i", nmsolici);
		log.debug("### kernel mesaControlUpdateSolici map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.MESACONTROL_UPDATE_SOLICI);
        log.debug("### kernel sustituto mesaControlUpdateSolici id:"+res.getMsgId());
        log.debug("### kernel sustituto mesaControlUpdateSolici mesage:"+res.getMsgText());
		return res;
		
	}
	
	public WrapperResultados mesaControlUpdateStatus(String ntramite,String status) throws ApplicationException
	{
		HashMap<String,Object> params =  new HashMap<String, Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_status_i", status);
		log.debug("### kernel mesaControlUpdateStatus map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.MESACONTROL_UPDATE_STATUS);
        log.debug("### kernel sustituto mesaControlUpdateStatus id:"+res.getMsgId());
        log.debug("### kernel sustituto mesaControlUpdateStatus mesage:"+res.getMsgText());
		return res;
	}
	
	public WrapperResultados mesaControlFinalizarDetalle(Map<String, String> smap1) throws ApplicationException
	{
		Iterator it=smap1.entrySet().iterator();
		HashMap<String,Object> params =  new HashMap<String, Object>();
		while(it.hasNext())
		{
			Entry en=(Entry) it.next();
			params.put((String)en.getKey(),(String)en.getValue());
		}
		params.put("pv_fechafin_i",new Date());
		log.debug("### kernel mesaControlFinalizarDetalle map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.MESACONTROL_FINALIZAR_DETALLE);
        log.debug("### kernel sustituto mesaControlFinalizarDetalle id:"+res.getMsgId());
        log.debug("### kernel sustituto mesaControlFinalizarDetalle mesage:"+res.getMsgText());
		return res;
	}
	
	public WrapperResultados preparaContrarecibo(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel preparaContrarecibo map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.DOCUMENTOS_PREPARAR_CONTRARECIBO);
        log.debug("### kernel sustituto preparaContrarecibo id:"+res.getMsgId());
        log.debug("### kernel sustituto preparaContrarecibo mesage:"+res.getMsgText());
		return res;
	}
	
	//PKG_COTIZA.P_OBTIENE_TVALOSIT
	public Map<String, Object> obtieneValositSituac(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto obtieneValositSituac map: "+params);
        Map<String,Object> map=(Map<String,Object>) this.getBackBoneInvoke(params, ProcesoDAO.OBTIENE_VALOSIT_SITUAC);
        log.debug("### kernel sustituto obtieneValositSituac response map: "+map);
        return map;
	}
	
	//PKG_COTIZA.P_OBTIENE_TVALOSIT
	public Map<String, Object> obtieneValositSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac) throws ApplicationException
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
        params.put("pv_cdramo_i"   , cdramo);
        params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsituac_i" , nmsituac);
		return this.obtieneValositSituac(params);
	}
	
	public List<Map<String, String>> buscarRFC(Map<String,String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto buscarRFC map: "+params);
		
		if(!params.containsKey("pv_rfc_i"))
		{
			params.put("pv_rfc_i", "");
		}
		if(!params.containsKey("pv_cdperson_i"))
		{
			params.put("pv_cdperson_i", "");
		}
		
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.BUSCAR_RFC);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto buscarRFC lista size: "+lista.size());
        return lista;
	}
	
	public WrapperResultados borrarMpoliper(Map<String, String> param) throws ApplicationException
	{
		log.debug("### kernel borrarMpersona map: "+param);
		WrapperResultados res = this.returnBackBoneInvoke(param,ProcesoDAO.BORRAR_MPERSONA);
        log.debug("### kernel borrarMpersona id:"+res.getMsgId());
        log.debug("### kernel borrarMpersona mesage:"+res.getMsgText());
		return res;
	}
	
	public List<Map<String, String>> obtenerRamos(String cdunieco) throws ApplicationException
	{
		Map<String,String>params=new LinkedHashMap<String,String>(0);
		params.put("pv_cdunieco_i",cdunieco);
		log.debug("### kernel sustituto obtenerRamos map: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.OBTENER_RAMOS);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerRamos lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> obtenerTipsit(String cdramo) throws ApplicationException
	{
		Map<String,String>params=new LinkedHashMap<String,String>(0);
		params.put("pv_cdramo_i",cdramo);
		log.debug("### kernel sustituto obtenerTipsit map: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.OBTENER_TIPSIT);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto obtenerTipsit lista size: "+lista.size());
        return lista;
	}
	
	public List<Map<String, String>> PValInfoPersonas(Map<String,String> params) throws ApplicationException
	{
		log.debug("### kernel sustituto PValInfoPersonas map: "+params);
        List<Map<String,String>> lista= this.getAllBackBoneInvoke(params, ProcesoDAO.P_VAL_INFO_PERSONAS);
        lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
        log.debug("### kernel sustituto PValInfoPersonas lista size: "+lista.size());
        return lista;
	}
	public String validaTitularMenorEdad(Map<String,String> params) throws ApplicationException
	{
		String titularMenor = Constantes.NO;
		log.debug("### kernel sustituto validaTitularMenorEdad map: "+params);
		WrapperResultados res=this.returnBackBoneInvoke(params, ProcesoDAO.VALIDA_TITULAR_MENOR_EDAD);
		titularMenor = (String) res.getItemMap().get("EXISTE_TITULAR_MENOR");
		log.debug("### kernel sustituto validaTitularMenorEdad: "+titularMenor);
		return titularMenor;
	}


	public WrapperResultados obtenerAgentePoliza(String cdunieco,
			String cdramo, String estado, String nmpoliza)
			throws ApplicationException {		
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pi_CDUNIECO", cdunieco);
			params.put("pi_CDRAMO", cdramo);
			params.put("pi_ESTADO", estado);
			params.put("pi_NMPOLIZA", nmpoliza);
	
			WrapperResultados result = this.returnBackBoneInvoke(params,
					ProcesoDAO.OBTIENE_DATOS_POLIZA_AGENTE);
			return result;
	}

	public WrapperResultados obtenerAgentePolizaPorPuntos(String cdunieco,
			String cdramo, String estado, String nmpoliza)
					throws ApplicationException {		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pi_CDUNIECO", cdunieco);
		params.put("pi_CDRAMO", cdramo);
		params.put("pi_ESTADO", estado);
		params.put("pi_NMPOLIZA", nmpoliza);
		
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_POLIZA_AGENTE_X_PUNTOS);
		return result;
	}

	public WrapperResultados obtenerTiposAgente() throws ApplicationException {
		HashMap<String, Object> params = new HashMap<String, Object>();		
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.OBTIENE_DATOS_GENERAL_AGENTE);
		return result;
	}

	/**
	 * PKG_SATELITES.P_MOV_MPOLIAGE_PORCENTAJES
	 */
	public WrapperResultados guardarPorcentajeAgentes(Map<String, Object> params)
			throws ApplicationException {
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.GUARDA_PORCENTAJE_POLIZA);
		return result;
	}

	/**
	 * PKG_SATELITES.P_MOV_MPOLIAGE_PORCENTAJES
	 */
	public WrapperResultados guardaPorcentajeAgentesPorPuntos(Map<String, Object> params)
			throws ApplicationException {
		
		/**
		 * Se convierte a porcentaje de puntos porcentuales
		 */
		String comision = (String) params.get("COMISION");
		String porcComPP  = (String) params.get("pi_PORREDAU");
		
		try{
			logger.debug("<<<<<<>>>>>> Antes de convertir el punto porcentual a valor porcentual <<<<<<>>>>>>");
			logger.debug("::::::Comision: " + comision);
			logger.debug("::::::Porcentaje Cesion de Comision PP: " + porcComPP);
			
    		if(StringUtils.isNotBlank(comision) && StringUtils.isNotBlank(porcComPP)){
    			float fComision = Float.parseFloat(comision);
    			float fporcCom  = 0;
    			float fporcComPP = Float.parseFloat(porcComPP);
    			
    			fporcCom = (fporcComPP * 100) / fComision;
    			porcComPP = Float.toString(fporcCom);
    			
    			logger.debug("::::: Despues de convertir  la comision a comision total de punto porcentual: " + porcComPP);
    		}
		}catch (Exception e){
			logger.debug("Error al convertir punto porcentual a valor porcentual de la cesion de comision");
		}
		
		params.put("pi_PORREDAU", porcComPP);
		
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.GUARDA_PORCENTAJE_POLIZA_X_PUNTOS);
		return result;
	}
	
	public WrapperResultados guardarEliminarPorcentajeAgentes(Map<String, Object> params)
			throws ApplicationException {
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.GUARDA_ELIMINA_PORCENTAJE_POLIZA);
		return result;
	}

	public WrapperResultados guardaEliminaPorcentajeAgentesPorPuntos(Map<String, Object> params)
			throws ApplicationException {
		
		/**
		 * Se convierte a porcentaje de puntos porcentuales
		 */
		String comision = (String) params.get("COMISION");
		String porcComPP  = (String) params.get("pi_PORREDAU");
		
		try{
			logger.debug("<<<<<<>>>>>> Antes de convertir el punto porcentual a valor porcentual <<<<<<>>>>>>");
			logger.debug("::::::Comision: " + comision);
			logger.debug("::::::Porcentaje Cesion de Comision PP: " + porcComPP);
			
    		if(StringUtils.isNotBlank(comision) && StringUtils.isNotBlank(porcComPP)){
    			float fComision = Float.parseFloat(comision);
    			float fporcCom  = 0;
    			float fporcComPP = Float.parseFloat(porcComPP);
    			
    			fporcCom = (fporcComPP * 100) / fComision;
    			porcComPP = Float.toString(fporcCom);
    			
    			logger.debug("::::: Despues de convertir  la comision a comision total de punto porcentual: " + porcComPP);
    		}
		}catch (Exception e){
			logger.debug("Error al convertir punto porcentual a valor porcentual de la cesion de comision");
		}
		
		params.put("pi_PORREDAU", porcComPP);
		
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.GUARDA_ELIMINA_PORCENTAJE_POLIZA_X_PUNTOS);
		return result;
	}

	
	public WrapperResultados guardaPeriodosDxN(Map<String, Object> params)
			throws ApplicationException {
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.GUARDA_PERIODOS_DXN);
		return result;
	}

	public WrapperResultados lanzaProcesoDxN(Map<String, Object> params)
			throws ApplicationException {
		WrapperResultados result = this.returnBackBoneInvoke(params,
				ProcesoDAO.LANZA_PROCESO_DXN);
		return result;
	}
	
	public WrapperResultados validarExtraprima(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel validarExtraprima map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.VALIDAR_EXTRAPRIMA);
		log.debug("### kernel sustituto validarExtraprima status:"+res.getItemList());
        log.debug("### kernel sustituto validarExtraprima id:"+res.getMsgId());
        log.debug("### kernel sustituto validarExtraprima mesage:"+res.getMsgText());
		return res;
	}
	
	@Override
	public WrapperResultados validarExtraprimaSituac(Map<String, String> params) throws ApplicationException
	{
		log.debug("### kernel validarExtraprimaSituac map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.VALIDAR_EXTRAPRIMA_SITUAC);
		log.debug("### kernel sustituto validarExtraprimaSituac status:"+res.getItemMap().get("status"));
        log.debug("### kernel sustituto validarExtraprimaSituac id:"+res.getMsgId());
        log.debug("### kernel sustituto validarExtraprimaSituac mesage:"+res.getMsgText());
		return res;
	}
	
	@Override
	public WrapperResultados validarExtraprimaSituacRead(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem) throws ApplicationException
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsituac_i" , nmsituac);
		params.put("pv_nmsuplem_i" , nmsuplem);
		
		log.debug("### kernel validarExtraprimaSituacRead map: "+params);
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.VALIDAR_EXTRAPRIMA_SITUAC_READ);
		log.debug("### kernel sustituto validarExtraprimaSituacRead status:"+res.getItemMap().get("status"));
        log.debug("### kernel sustituto validarExtraprimaSituacRead id:"+res.getMsgId());
        log.debug("### kernel sustituto validarExtraprimaSituacRead mesage:"+res.getMsgText());
        return res;
	}

	public String habilitaSigRecibo(Map<String, String> params) throws ApplicationException{
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.HABILITA_SIG_RECIBO);
		return res.getMsgTitle();
	}

	public String obtenCdtipsitGS(Map<String, Object> params) throws ApplicationException{
		String cdtipsitGS = null;
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.OBTEN_CDTIPSIT_GS);
		cdtipsitGS = (String) res.getItemMap().get("cdtipsitGS");
		logger.debug(">>>> cdTipsitGS obtenido: " + cdtipsitGS);
		return cdtipsitGS;
	}
	
	public String obtenSubramoGS(Map<String, Object> params) throws ApplicationException{
		String subramoGS = null;
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.OBTEN_SUBRAMO_GS);
		subramoGS = (String) res.getItemMap().get("subramoGS");
		logger.debug(">>>> subramoGS obtenido: " + subramoGS);
		return subramoGS;
	}
	
	public String obtenCdtipsit(Map<String, Object> params) throws ApplicationException{
		String cdtipsit = null;
		WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.OBTEN_CDTIPSIT_GS);
		cdtipsit = (String) res.getItemMap().get("cdtipsit");
		logger.debug(">>>> cdTipsit obtenido: " + cdtipsit);
		return cdtipsit;
	}
	
	public WrapperResultados validaUsuarioSucursal(String cdunieco, String cdramo, String cdtipsit, String username) throws ApplicationException {
		
		try {
			Map<String,String>params = new HashMap<String,String>();
			params.put("pv_cdunieco_i" , cdunieco);
			params.put("pv_cdramo_i"   , cdramo);
			params.put("pv_cdtipsit"   , cdtipsit);
			params.put("pv_cdusuari_i" , username);
			
			log.debug("### kernel validaUsuarioSucursal map: "+params);
			WrapperResultados res = this.returnBackBoneInvoke(params,ProcesoDAO.VALIDA_USUARIO_SUCURSAL);
	        log.debug("### validaUsuarioSucursal id:"+res.getMsgId());
	        log.debug("### validaUsuarioSucursal message:"+res.getMsgText());
	        return res;
		} catch(Exception e) {
			throw new ApplicationException(new StringBuilder("El usuario ").append(username).append(" no est� asociado a la sucursal de documento elegida, debe elegir otra.").toString(), e);
		}
	}

	public boolean actualizaCdIdeper(Map<String,String> params){
		try{
			this.returnBackBoneInvoke(params,ProcesoDAO.ACTUALIZA_CDIDEPER);
		}catch(Exception e){
			logger.error("Error al insertar el cdIdeper. ", e);
			return false;
		}
		return true;
	}
	
	public void validaDatosAutos(Map<String,String> params)throws Exception{
		this.returnBackBoneInvoke(params,ProcesoDAO.VALIDA_DATOS_AUTOS);
	}
	
	public void actualizaPolizaExterna(Map<String,String> params)throws Exception{
		this.returnBackBoneInvoke(params,ProcesoDAO.ACTUALIZA_POLALT);
	}

	public void cargaCobranzaMasiva(Map<String,String> params)throws Exception{
		this.returnBackBoneInvoke(params,ProcesoDAO.CARGA_COBRANZA_MASIVA);
	}
	
	public List<Map<String, String>> obtieneCobranzaAplicada(Map<String,String> params)throws Exception{
		return this.getAllBackBoneInvoke(params, ProcesoDAO.OBTIENE_COBRANZA_APLICADA);
	}
	
	public List<Map<String, String>> obtieneRemesaAplicada(Map<String,String> params)throws Exception{
		return this.getAllBackBoneInvoke(params, ProcesoDAO.OBTIENE_REMESA_APLICADA);
	}
	
}