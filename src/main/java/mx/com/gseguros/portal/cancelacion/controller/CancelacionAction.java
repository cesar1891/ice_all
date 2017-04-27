package mx.com.gseguros.portal.cancelacion.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cancelacion.service.CancelacionManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelacionAction extends PrincipalCoreAction
{
	private static final long              serialVersionUID   = 3337342608259982346L;
	private static org.apache.log4j.Logger log                = org.apache.log4j.Logger.getLogger(CancelacionAction.class);
	private boolean                        success;
	private SimpleDateFormat               renderFechas       = new SimpleDateFormat("dd/MM/yyyy");
	
	private CancelacionManager       cancelacionManager;
	private PantallasManager         pantallasManager;
	private transient Ice2sigsService ice2sigsService;
	private Map<String,String>       smap1;
	private List<Map<String,String>> slist1;
	private Map<String,Item>         imap;
	private String                   error;
	private String                   respuesta;
	
	
	@Autowired
	private DocumentosManager documentosManager;
	

	//////////////////////////////////
	////// marco de cancelacion //////
	/*//////////////////////////////*/
	public String marcoCancelacion()
	{
		log.debug(""
				+ "\n##############################"
				+ "\n##############################"
				+ "\n###### marcoCancelacion ######"
				+ "\n######                  ######"
				);
		try
		{
			imap=new HashMap<String,Item>(0);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"MARCOCANCELACION", "FILTRO", null));
			
			imap.put("itemsFiltro",gc.getItems());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"MARCOCANCELACION", "MODELOCANDIDATA", null));
			
			imap.put("fieldsCandidata",gc.getFields());
			imap.put("columnsCandidata",gc.getColumns());
			
		}
		catch(Exception ex)
		{
			log.error("error al generar el marco de cancelacion",ex);
		}
		log.debug(""
				+ "\n######                  ######"
				+ "\n###### marcoCancelacion ######"
				+ "\n##############################"
				+ "\n##############################"
				);
		return SUCCESS;
	}
	/*//////////////////////////////*/
	////// marco de cancelacion //////
	//////////////////////////////////
	
	/////////////////////////////////////////////
	////// manda a la pantalla de cancelar //////
	/*/////////////////////////////////////////*/
	public String pantallaCancelar()
	{
		log.debug(""
				+ "\n##############################"
				+ "\n##############################"
				+ "\n###### pantallaCancelar ######"
				+ "\n######                  ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			imap=new HashMap<String,Item>(0);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"PANTALLACANCELARUNICA", "FORM", null));
			
			imap.put("itemsMarcocancelacionModelocandidata",gc.getItems());
			
		}
		catch(Exception ex)
		{
			log.error("error al crear la pantalla de cancelacion unica",ex);
		}
		log.debug(""
				+ "\n######                  ######"
				+ "\n###### pantallaCancelar ######"
				+ "\n##############################"
				+ "\n##############################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////////////////*/
	////// manda a la pantalla de cancelar //////
	/////////////////////////////////////////////

	////////////////////////////
	////// buscar polizas //////
	/*////////////////////////*/
	public String buscarPolizas()
	{
		log.debug(""
				+ "\n###########################"
				+ "\n###########################"
				+ "\n###### buscarPolizas ######"
				+ "\n######               ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			//////////////////////
			////// completa //////
			smap1.put("pv_cdunieco_i",smap1.get("pv_dsuniage_i"));
			smap1.put("pv_cdramo_i",smap1.get("pv_dsramo_i"));
			////// completa //////
			//////////////////////
			
			/////////////////////////////////
			////// transforma a object //////
			Map<String,Object>omap=new HashMap<String,Object>(0);
			for(Entry en:smap1.entrySet())
			{
				if(((String)en.getKey()).substring(0,5).equalsIgnoreCase("pv_fe"))
				{
					String fecha=(String)en.getValue();
					if(StringUtils.isNotBlank(fecha))
					{
						omap.put((String)en.getKey(),renderFechas.parse(fecha));
					}
					else
					{
						omap.put((String)en.getKey(),null);
					}
				}
				else
				{
					omap.put((String)en.getKey(),(String)en.getValue());
				}
			}
			////// transforma a object //////
			/////////////////////////////////
			
			if(smap1.get("pv_nmpoliza_i")!=null&&smap1.get("pv_nmpoliza_i").length()>0)
			{
				cancelacionManager.seleccionaPolizaUnica(omap);
			}
			else
			{
				cancelacionManager.seleccionaPolizas(omap);
			}
			slist1=cancelacionManager.obtenerPolizasCandidatas(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			log.error("error al obtener polizas",ex);
			slist1=null;
			success=false;
			error=ex.getMessage();
		}
		log.debug(""
				+ "\n######               ######"
				+ "\n###### buscarPolizas ######"
				+ "\n###########################"
				+ "\n###########################"
				);
		return SUCCESS;
	}
	/*////////////////////////*/
	////// buscar polizas //////
	////////////////////////////
	
	/////////////////////////////////////////
	////// cancelacion unica de poliza //////
	/*
	pv_fevencim_i=03/02/2015,
	pv_estado_i=M,
	pv_nmsituac_i=1,
	pv_fecancel_i=23/02/2014,
	pv_comenta_i=asd,
	pv_cdunieco_i=1006,
	pv_nmpoliza_i=28,
	pv_cduniage_i=,
	pv_cdrazon_i=2,
	pv_usuario_i=,
	pv_feefecto_i=03/02/2014,
	pv_cdramo_i=2
	*/
	/*/////////////////////////////////////*/
	public String cancelacionUnica()
	{
		log.debug(""
				+ "\n##############################"
				+ "\n##############################"
				+ "\n###### cancelacionUnica ######"
				+ "\n######                  ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			String cdtipsup = TipoEndoso.CANCELACION_UNICA.getCdTipSup().toString();
			
			String nmsuplem = null;
			String ntramite = null;
			String nmsolici = null;
			
			UserVO usuario=(UserVO)session.get("USUARIO");
			smap1.put("pv_usuario_i"  , usuario.getUser());
			smap1.put("pv_cdtipsup_i" , cdtipsup);
			smap1.put("pv_cdsisrol_i" , usuario.getRolActivo().getClave());
			nmsuplem = cancelacionManager.cancelaPoliza(smap1);	
			
			String cdunieco = smap1.get("pv_cdunieco_i");
			String cdramo   = smap1.get("pv_cdramo_i");
			String estado   = smap1.get("pv_estado_i");
			String nmpoliza = smap1.get("pv_nmpoliza_i");
			
			Map<String,String> result = documentosManager.generarDocumentosParametrizados(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0" //nmsituac
					,nmsuplem
					,DocumentosManager.PROCESO_ENDOSO
					,ntramite
					,nmsolici
					,null
					);
			
			ntramite = result.get("ntramite");
			nmsolici = result.get("nmsolici");
			
			String sucursal = cdunieco;
			if(StringUtils.isNotBlank(sucursal) && "1".equals(sucursal)) sucursal = "1000";
			
			// Ejecutamos el Web Service de Recibos:
			ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
					estado, nmpoliza, 
					nmsuplem, null, 
					sucursal, "", ntramite, 
					true, cdtipsup, 
					(UserVO) session.get("USUARIO"));
			
			success=true;			
		}
		catch(Exception ex)
		{
			log.error("error al cancelar poliza unica",ex);
			respuesta = ex.getMessage();
			success=false;
		}
		log.debug(""
				+ "\n######                  ######"
				+ "\n###### cancelacionUnica ######"
				+ "\n##############################"
				+ "\n##############################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////////////*/
	////// cancelacion unica de poliza //////
	/////////////////////////////////////////
	
	////////////////////////////////////////////////
	////// pantalla de cancelacion automatica //////
	/*////////////////////////////////////////////*/
	public String pantallaCancelarAuto()
	{
		log.debug(""
				+ "\n##################################"
				+ "\n##################################"
				+ "\n###### pantallaCancelarAuto ######"
				+ "\n######                      ######"
				);
		log.debug("slist1 :"+slist1);
		try
		{
			imap=new HashMap<String,Item>(0);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"MARCOCANCELACION", "MODELOCANDIDATA", null));
			
			imap.put("fieldsMarcocancelacionModelocandidata",gc.getFields());
			imap.put("columnsMarcocancelacionModelocandidata",gc.getColumns());
			
			Map<String,String>todasN=new HashMap<String,String>(0);
			todasN.put("pv_cdunieco_i" , null);
			todasN.put("pv_cdramo_i"   , null);
			todasN.put("pv_estado_i"   , null);
			todasN.put("pv_nmpoliza_i" , null);
			todasN.put("pv_swcancel_i" , "N");
			cancelacionManager.actualizarTagrucan(todasN);
			
			for(Map<String,String>selec:slist1)
			{
				Map<String,String>iterada=new HashMap<String,String>(0);
				iterada.put("pv_cdunieco_i" , selec.get("CDUNIAGE"));
				iterada.put("pv_cdramo_i"   , selec.get("CDRAMO"));
				iterada.put("pv_estado_i"   , null);
				iterada.put("pv_nmpoliza_i" , selec.get("NMPOLIZA"));
				iterada.put("pv_swcancel_i" , "S");
				cancelacionManager.actualizarTagrucan(iterada);
			}
			
		}
		catch(Exception ex)
		{
			log.error("error al cargar pantalla de cancelacion automatica",ex);
		}
		log.debug(""
				+ "\n######                      ######"
				+ "\n###### pantallaCancelarAuto ######"
				+ "\n##################################"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	/*////////////////////////////////////////////*/
	////// pantalla de cancelacion automatica //////
	////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////
	////// proceso de cancelacion automatica manual //////
	/*//////////////////////////////////////////////////*/
	public String cancelacionAutoManual()
	{
		log.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### cancelacionAutoManual ######"
				+ "\n######                       ######"
				);
		
		logger.debug("params: "+ smap1);
		try
		{
			String cdtipsup = TipoEndoso.CANCELACION_MASIVA.getCdTipSup().toString();
			
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			Map<String,String>params=new HashMap<String,String>(0);
			params.put("pv_id_proceso_i"  , "1");
			params.put("pv_fecha_carga_i" , null);
			params.put("pv_usuario_i"     , usuario.getUser());
			params.put("pv_cdtipsup_i"    , cdtipsup);
			params.put("pv_cdsisrol_i"    , usuario.getRolActivo().getClave());
			cancelacionManager.cancelacionMasiva(params);
			success=true;
			
			params=new HashMap<String,String>(0);
			params.put("pv_feproces_i", smap1.get("feproces"));
			ArrayList<PolizaVO> polizasCanceladas = cancelacionManager.obtienePolizasCancelacionMasiva(params);
			
			logger.debug("Datos de polizas caceladas para WS: "+ polizasCanceladas);
			for(PolizaVO polizaC : polizasCanceladas){
				
				String sucursal = polizaC.getCdunieco();
				if(StringUtils.isNotBlank(sucursal) && "1".equals(sucursal)) sucursal = "1000";
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(polizaC.getCdunieco(), polizaC.getCdramo(), 
						polizaC.getEstado(), polizaC.getNmpoliza(), 
						polizaC.getNmsuplem(), null, 
						sucursal, "", "", 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
				
			}
			
		}
		catch(Exception ex)
		{
			log.error("error al cancelar las polizas",ex);
			success=false;
		}
		log.debug(""
				+ "\n######                       ######"
				+ "\n###### cancelacionAutoManual ######"
				+ "\n###################################"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	/*//////////////////////////////////////////////////*/
	////// proceso de cancelacion automatica manual //////
	//////////////////////////////////////////////////////
	
	/////////////////////////////////////////////
	////// obtener detalles de cancelacion //////
	/*/////////////////////////////////////////*/
	public String obtenerDetalleCancelacion()
	{
		log.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### obtenerDetalleCancelacion ######"
				+ "\n######                           ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			smap1.putAll(cancelacionManager.obtenerDetalleCancelacion(smap1)); 
			success=true;
		}
		catch(Exception ex)
		{
			log.error("error al obtener detalle de una cancelacion",ex);
			success=false;
		}
		log.debug(""
				+ "\n######                           ######"
				+ "\n###### obtenerDetalleCancelacion ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////////////////*/
	////// obtener detalles de cancelacion //////
	/////////////////////////////////////////////
	
	public String validaCancelacionAProrrata()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### validaCancelacionAProrrata ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			
			Utils.validate(
					cdunieco  , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de la poliza"
					,nmpoliza , "No se recibio el numero de poliza"
					);
			
			cancelacionManager.validaCancelacionAProrrata(cdunieco,cdramo,estado,nmpoliza);
			
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta=",respuesta
				,"\n###### validaCancelacionAProrrata ######"
				,"\n########################################"
				));
		return SUCCESS;
	}

	public String validaRazonCancelacion()
	{
		logger.debug(Utils.log(
				"\n########################################"
				,"\n###### validaRazonCancelacion ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			Utils.validate(smap1 , "No se recibieron datos");
			
			String cdunieco = smap1.get("cdunieco");
			String cdramo   = smap1.get("cdramo");
			String estado   = smap1.get("estado");
			String nmpoliza = smap1.get("nmpoliza");
			String cdrazon  = smap1.get("cdrazon");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00F3 la sucursal"
					,cdramo   , "No se recibi\u00F3 el producto"
					,estado   , "No se recibi\u00F3 el estado de la p\u00F3liza"
					,nmpoliza , "No se recibi\u00F3 el numero de p\u00F3liza"
					,cdrazon  , "No se recibi\u00F3 el motivo"
					);
			
			cancelacionManager.validaRazonCancelacion(cdunieco,cdramo,estado,nmpoliza,cdrazon);
			
			success = true;
		}
		catch(Exception ex)
		{
			success = false;
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				"\n###### respuesta=",respuesta
				,"\n###### validaRazonCancelacion ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	
	/////////////////////////////////
	////// getters and setters //////
	/*/////////////////////////////*/
	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setCancelacionManager(CancelacionManager cancelacionManager) {
		this.cancelacionManager = cancelacionManager;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}
	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
}