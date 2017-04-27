package mx.com.gseguros.portal.siniestros.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoPrestadorServicio;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.model.AutorizaServiciosVO;
import mx.com.gseguros.portal.siniestros.model.AutorizacionServicioVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaManteniVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaPorcentajeVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTDETAUTSVO;
import mx.com.gseguros.portal.siniestros.model.DatosSiniestroVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

public class AutorizacionServiciosAction extends PrincipalCoreAction {
	private static final long serialVersionUID = -6059133827980738197L;
	static final Logger logger = LoggerFactory.getLogger(AutorizacionServiciosAction.class);
	private boolean success;
	private SiniestrosManager siniestrosManager;
	private KernelManagerSustituto kernelManagerSustituto;
	private transient CatalogosManager catalogosManager;
	private PantallasManager       pantallasManager;
	private transient Ice2sigsService ice2sigsService;
	private Map<String,Item>         imap;
	private Map<String,String> params;
	private List<AutorizaServiciosVO> listaAutorizacion;
	private AutorizacionServicioVO datosAutorizacionEsp;
	private List<DatosSiniestroVO> listaDatosSiniestro;
	private List<ConsultaPorcentajeVO> listaPorcentaje;
	private List<ConsultaManteniVO> listaConsultaManteni;
	private List<ConsultaTDETAUTSVO> listaConsultaTablas;
	private List<HashMap<String,String>> datosTablas;
	private DateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	private AutorizacionServicioVO numeroAutorizacion;
	private List<GenericVO> listaPlazas;
	private String diasMaximos;
	private String existePenalizacion;
	private String porcentajePenalizacion;
	private String montoMaximo;
	private String mesMaximoMaternidad;
	private String existeDocAutServicio;
	private String mesesTiempoEspera;
	private List<Map<String,String>>  datosInformacionAdicional;
	private String msgResult;
	private String autorizarProceso;
	private Map<String, String> map1;
	private String mensaje;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private MesaControlManager mesaControlManager;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
    
    @Value("${pass.servidor.reports}")
    private String passServidorReports;	
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
	public String autorizacionServicios() {
		logger.debug("Entra a autorizacionServicios Params: {}", params);
		try {
			UserVO usuario  	= (UserVO)session.get("USUARIO");
			String cdrol    	= usuario.getRolActivo().getClave();
			String pantalla     = "AUTORIZACION_SERVICIOS";
			String seccion      = "PANELBUTTONS";
			List<ComponenteVO>ltFormulario = pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, cdrol,
					pantalla, seccion, null);

			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(ltFormulario, true, false, false, false, false, true);

			imap = new HashMap<String,Item>(0);
			imap.put("panelbuttons",gc.getButtons());
			String numero_aut = null;
			String ntramite = null;
			String caseIdRstn = null;

			if(params != null){
				numero_aut  = params.get("nmAutSer");
				ntramite  =  params.get("ntramite");
				caseIdRstn = params.get("caseIdRstn");
			}

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nmAutSer",numero_aut);
			params.put("ntramite",ntramite);
			params.put("cdrol",cdrol);
			params.put("caseIdRstn",caseIdRstn);
			setParamsJson(params);
		} catch (Exception e) {
			logger.error("Error en la autorizacion de Servicio : {}", e.getMessage(), e);
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	/**
	* Funcion para la visualizacion de la autorizacion de servicio 
	* @return params con los valores para hacer las consultas
	*/
	public String verAutorizacionServicio(){
		logger.debug("Entra a verAutorizacionServicio");
		try {
			logger.debug("Params: {}", params);
		}catch( Exception e){
			logger.error("Error al visualizar la autorizacion de servicio : {}", e.getMessage(), e);
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de  de Autorizacion de Servicio
	* @param String cdperson
	* @return Lista AutorizaServiciosVO con la informacion de los asegurados
	*/
	public String consultaListaAutorizacion(){
		logger.debug("Entra a consultaListaAutorizacion Params: {}", params);
		try {
			List<AutorizaServiciosVO> lista = siniestrosManager.getConsultaListaAutorizaciones(params.get("tipoAut"),params.get("cdperson"));
			if(lista!=null && !lista.isEmpty())	setListaAutorizacion(lista);
		}catch( Exception e){
			logger.error("Error consultaListaAutorizacion : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que realiza la busqueda de la consulta de Autorizacion de servicio en especifico
	* @param String nmautser
	* @return AutorizacionServicioVO
	*/
	public String consultaAutorizacionServicio(){
		logger.debug("Entra a consultaAutorizacionServicio Params: {}", params);
		try {
			List<AutorizacionServicioVO> lista = siniestrosManager.getConsultaAutorizacionesEsp(params.get("nmautser"));
			logger.debug("Total Registros {}", lista.size());
			if(lista!=null && !lista.isEmpty()){
				datosAutorizacionEsp = lista.get(0);
			}
		}catch( Exception e){
			logger.error("Error consultaAutorizacionServicio : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo que obtiene la informacion de deducible y copago
	* @param String params [cdunieco,estado,cdramo,nmpoliza,nmsituac,cdgarant,subcober]
	* @return Lista DatosSiniestroVO con la informacion de los asegurados
	*/
	public String consultaListaDatSubGeneral(){
		logger.debug("Entra consultaListaDatSubGeneral Params: {}", params);
		try {
			HashMap<String, Object> paramDatSubGral = new HashMap<String, Object>();
			paramDatSubGral.put("pv_cdunieco_i",params.get("cdunieco"));
			paramDatSubGral.put("pv_estado_i",params.get("estado"));
			paramDatSubGral.put("pv_cdramo_i",params.get("cdramo"));
			paramDatSubGral.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramDatSubGral.put("pv_nmsituac_i",params.get("nmsituac"));
			paramDatSubGral.put("pv_cdgarant_i",params.get("cdgarant"));
			paramDatSubGral.put("pv_subcober_i",params.get("subcober"));
			paramDatSubGral.put("pv_cdpresta_i",params.get("cdpresta"));
			paramDatSubGral.put("pv_cdtipo_i",TipoPrestadorServicio.CLINICA.getCdtipo());
			paramDatSubGral.put("pv_cdtipsit_i",params.get("cdtipsit"));
			
			List<DatosSiniestroVO> lista = siniestrosManager.getConsultaListaDatSubGeneral(paramDatSubGral);
			logger.debug("Total de registros : {}", lista.size());
			if(lista!=null && !lista.isEmpty())	setListaDatosSiniestro(lista);
		}catch( Exception e){
			logger.error("Error consultaListaDatSubGeneral : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	*Funcion que obtiene la lista de Porcentajes
	* @param cdp, tipoMedico y MontoBase
	* @return Lista ConsultaPorcentajeVO con la informacion del proveedor
	*/ 
	public String consultaListaPorcentaje(){
		logger.debug("Entra a consultaListaPorcentaje Params: {}", params);
		try {
			if(params != null){
				List<ConsultaPorcentajeVO> lista = siniestrosManager.getConsultaListaPorcentaje(params.get("cdcpt"),params.get("cdtipmed"),params.get("mtobase"));
				logger.debug("Total Lista : {}", lista.size());
				if(lista!=null && !lista.isEmpty())	setListaPorcentaje(lista);
			}
		}catch( Exception e){
			logger.error("Error consultaListaPorcentaje : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de tipo Medico
	* @param String codigo del tipo 
	* @return Lista ConsultaTDETAUTSVO con la informacion
	*/
	public String consultaTipoMedico(){
		logger.debug("Entra a consultaTipoMedico Params: {}", params);
		try {
			List<ConsultaManteniVO> lista = siniestrosManager.getConsultaListaTipoMedico(params.get("codigo"));
			logger.debug("Total List<ConsultaManteniVO> lista : {} valores a enviar {}", lista.size(), lista);
			if(lista!=null && !lista.isEmpty())	listaConsultaManteni = lista;
		}catch( Exception e){
			logger.error("Error consultaTipoMedico {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de las listas de los grids
	* @param String nmautser
	* @return Lista ConsultaTDETAUTSVO con la informacion
	*/
	public String consultaListaTDeTauts(){
		logger.debug("Entra a consultaListaTDeTauts Params: {}", params);
		try {
			List<ConsultaTDETAUTSVO> lista = siniestrosManager.getConsultaListaTDeTauts(params.get("nmautser"));
			logger.debug("Total Lista consultaListaTDeTauts : {}", lista.size());
			if(lista!=null && !lista.isEmpty())	listaConsultaTablas = lista;
		}catch( Exception e){
			logger.error("Error consultaListaTDeTauts : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para el guardado de la autorizaci�n de Servicio
	* @param Json con todos los valores del formulario y los grid
	* @return Lista AutorizaServiciosVO con la informaci�n de los asegurados
	*/
	public String guardaAutorizacionServicio(){
		logger.debug("Entra a guardaAutorizacionServicio Params:{} datosTabla: {}", params,datosTablas);
		try {
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			HashMap<String, Object> paramsR = new HashMap<String, Object>();
			paramsR.put("pv_nmautser_i",params.get("nmautser"));
			paramsR.put("pv_nmautant_i",params.get("nmautant"));
			paramsR.put("pv_cdperson_i",params.get("cdperson"));
			paramsR.put("pv_fesolici_i",renderFechas.parse(params.get("fesolici")));
			paramsR.put("pv_feautori_i",renderFechas.parse(params.get("feautori")));
			paramsR.put("pv_fevencim_i",renderFechas.parse(params.get("fevencim")));
			paramsR.put("pv_feingres_i",renderFechas.parse(params.get("feingres")));
			paramsR.put("pv_cdunieco_i",params.get("cdunieco"));
			paramsR.put("pv_estado_i",params.get("estado"));
			paramsR.put("pv_cdramo_i",params.get("cdramo"));
			paramsR.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramsR.put("pv_nmsituac_i",params.get("nmsituac"));
			paramsR.put("pv_cduniecs_i",params.get("cduniecs"));
			paramsR.put("pv_cdgarant_i",params.get("cdgarant"));
			paramsR.put("pv_cdconval_i",params.get("cdconval"));
			paramsR.put("pv_cdprovee_i",params.get("cdprovee"));
			paramsR.put("pv_cdmedico_i",params.get("cdmedico"));
			paramsR.put("pv_mtsumadp_i",params.get("mtsumadp"));
			paramsR.put("pv_copagofi_i",params.get("copagoTotal"));   // Copago Final  copagoTotal
			paramsR.put("pv_porpenal_i",params.get("idPenalCircHospitalario")); // penalizacion por circulo hospitalario
			paramsR.put("pv_cdicd_i",params.get("cdicd"));
			paramsR.put("pv_cdcausa_i",params.get("cdcausa"));
			paramsR.put("pv_aaapertu_i",params.get("aaapertu"));
			paramsR.put("pv_status_i",params.get("status"));
			paramsR.put("pv_dstratam_i",params.get("dstratam"));
			paramsR.put("pv_dsobserv_i",params.get("dsobserv"));
			paramsR.put("pv_dsnotas_i",params.get("dsnotas"));
			paramsR.put("pv_fesistem_i",renderFechas.parse(params.get("fesistem"))); 
			paramsR.put("pv_cduser_i",usuario.getUser());
			paramsR.put("pv_nombmedi_i",params.get("medicoPExt"));
			paramsR.put("pv_especmed_i",params.get("especialidadPExt"));
			paramsR.put("pv_tpautori_i",params.get("cveTipoAutorizaG"));
			paramsR.put("pv_idaplicaCirHosp_i",params.get("idaplicaCirHosp"));
			paramsR.put("pv_idaplicaZona_i",params.get("idaplicaZona"));
			paramsR.put("pv_swnegoci_i",params.get("idaplicaZona"));
			paramsR.put("pv_tiposerv_i",params.get("idTipoEventoGNP"));
			paramsR.put("pv_numrecla_i",params.get("idNumSubsecuente"));
			
			//1.- Eliminacion de la tabla TDETAUTS ---> PKG_PRESINIESTRO.P_BORRA_TDETAUTS
			siniestrosManager.getEliminacionRegistros(params.get("nmautser"));
			//2.- Agregar informacion PKG_PRESINIESTRO.P_GUARDA_MAUTSERV2
			List<AutorizacionServicioVO> lista = siniestrosManager.guardarAutorizacionServicioGNP(paramsR);  
			//List<AutorizacionServicioVO> lista = siniestrosManager.guardarAutorizacionServicio(paramsR);
			if(lista!=null && !lista.isEmpty()) {
				numeroAutorizacion = lista.get(0);
				for(int i=0;i<datosTablas.size();i++) {
					HashMap<String, Object> paramsTDeTauts = new HashMap<String, Object>();
					paramsTDeTauts.put("pv_nmautser_i",lista.get(0).getNmautser());
					paramsTDeTauts.put("pv_cdtipaut_i",datosTablas.get(i).get("cdtipaut"));
					paramsTDeTauts.put("pv_cdmedico_i",datosTablas.get(i).get("cdmedico"));
					paramsTDeTauts.put("pv_cdtipmed_i",datosTablas.get(i).get("cdtipmed"));
					paramsTDeTauts.put("pv_cdctp_i",datosTablas.get(i).get("cdcpt"));
					paramsTDeTauts.put("pv_precio_i",datosTablas.get(i).get("precio"));
					paramsTDeTauts.put("pv_cantporc_i",datosTablas.get(i).get("cantporc"));
					paramsTDeTauts.put("pv_ptimport_i",datosTablas.get(i).get("ptimport"));
					paramsTDeTauts.put("pv_nombprov_i",datosTablas.get(i).get("nombreMedico"));
					//3.- Se guarda la informacion en PKG_PRESINIESTRO.P_GUARDA_TDETAUTS
					siniestrosManager.guardaListaTDeTauts(paramsTDeTauts);
				}
				
				if(params.get("claveTipoAutoriza").trim().equalsIgnoreCase("1") || params.get("claveTipoAutoriza").trim().equalsIgnoreCase("3")){
					String statusParaTramite = null;
					if(params.get("status").trim().equalsIgnoreCase(EstatusTramite.PENDIENTE.getCodigo())){
						statusParaTramite = EstatusTramite.CONFIRMADO.getCodigo();
					}else{
						if(usuario.getRolActivo().getClave().trim().equalsIgnoreCase(RolSistema.COORDINADOR_MEDICO_MULTIREGIONAL.getCdsisrol())) {
							statusParaTramite = EstatusTramite.EN_CAPTURA_CMM.getCodigo();// valor 12
						}else{
							statusParaTramite = EstatusTramite.EN_CAPTURA.getCodigo();// valor 7
						}
					}
					
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , lista.get(0).getNmautser());
					valores.put("otvalor02" , params.get("fesolici"));
					valores.put("otvalor03" , params.get("feautori"));
					valores.put("otvalor04" , params.get("fevencim"));
					valores.put("otvalor05" , params.get("dsNombreAsegurado"));
					valores.put("otvalor06" , params.get("copagoTotal"));
					valores.put("otvalor07" , params.get("idHospitalPlus"));
					valores.put("otvalor08" , params.get("idTipoEvento"));
					valores.put("otvalor16" , usuario.getUser());
					valores.put("otvalor17" , usuario.getUser());
					valores.put("otvalor18" , usuario.getUser());
					
					String ntramiteGenerado = mesaControlManager.movimientoTramite(
							params.get("cdunieco")
							,params.get("cdramo")
							,params.get("estado")
							,params.get("nmpoliza")
							,params.get("nmsuplem")
							,null
							,null
							,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
							,null
							,null
							,null
							,null
							,null
							,statusParaTramite
							,params.get("dsnotas")
							,null
							,params.get("cdtipsit")
							,usuario.getUser()
							,usuario.getRolActivo().getClave()
							,null //swimpres
							,null //cdtipflu
							,null //cdflujomc
							,valores, null, null, null, null
							);
					
					if(params.get("status").trim().equalsIgnoreCase("2")){
						Map<String,Object>paramsO =new HashMap<String,Object>();
						paramsO.put("pv_ntramite_i" , ntramiteGenerado);
						paramsO.put("pv_cdunieco_i" , params.get("cdunieco"));
						paramsO.put("pv_cdramo_i" , params.get("cdramo"));
						paramsO.put("pv_estado_i" , params.get("estado"));
						paramsO.put("pv_nmpoliza_i" , params.get("nmpoliza"));
						paramsO.put("pv_nmAutSer_i" , lista.get(0).getNmautser());
						paramsO.put("pv_cdperson_i" , params.get("cdperson"));
						paramsO.put("pv_nmsuplem_i" , params.get("nmsuplem"));
						//--> Genera Autorizacion de Servicio
						generarAutoriServicio(paramsO);
					}
				}else{
					Map<String,Object> otvalor = new HashMap<String,Object>();
					if(params.get("dsNombreAsegurado").toString().length() > 0){
						otvalor.put("pv_otvalor05_i"  , params.get("dsNombreAsegurado"));
					}
					otvalor.put("pv_ntramite_i" , params.get("idNumtramiteInicial"));
					otvalor.put("pv_cdramo_i"   , params.get("cdramo"));
					otvalor.put("pv_cdtipsit_i" , params.get("cdtipsit"));
					otvalor.put("pv_otvalor02_i"  , params.get("fesolici"));
					otvalor.put("pv_otvalor03_i"  , params.get("feautori"));
					otvalor.put("pv_otvalor04_i"  , params.get("fevencim"));
					otvalor.put("pv_otvalor06_i",params.get("copagoTotal"));
					otvalor.put("pv_otvalor07_i",params.get("idHospitalPlus"));
					otvalor.put("pv_otvalor08_i",params.get("idTipoEvento"));
                    otvalor.put("pv_otvalor16_i",usuario.getUser());
					otvalor.put("pv_otvalor17_i",usuario.getUser());
					siniestrosManager.actualizaOTValorMesaControl(otvalor);
					
					// Tenemos que actualizar el status para el guardado
					if(params.get("status").trim().equalsIgnoreCase("2")){
						String statusNuevo = EstatusTramite.CONFIRMADO.getCodigo();
						String ntramite = params.get("idNumtramiteInicial");
						String comments = null;
						String cdmotivo = null;
						UserVO usu=(UserVO)session.get("USUARIO");
						String cdusuariSesion = usu.getUser();
						String cdsisrolSesion = usu.getRolActivo().getClave();
						String rolDestino     = null;
						String usuarioDestino = null;
						String cdclausu       = null;
						
						siniestrosManager.moverTramite(ntramite, statusNuevo, comments, cdusuariSesion, cdsisrolSesion, usuarioDestino, rolDestino, cdmotivo, cdclausu,null,null, false);
						
						Map<String,Object>paramsO =new HashMap<String,Object>();
						paramsO.put("pv_ntramite_i" , params.get("idNumtramiteInicial"));
						paramsO.put("pv_cdunieco_i" , params.get("cdunieco"));
						paramsO.put("pv_cdramo_i" , params.get("cdramo"));
						paramsO.put("pv_estado_i" , params.get("estado"));
						paramsO.put("pv_nmpoliza_i" , params.get("nmpoliza"));
						paramsO.put("pv_nmAutSer_i" , lista.get(0).getNmautser());
						paramsO.put("pv_cdperson_i" , params.get("cdperson"));
						paramsO.put("pv_nmsuplem_i" , params.get("nmsuplem"));
						generarAutoriServicio(paramsO);	
					}
				}
			}
		}catch( Exception e){
			logger.error("Error guardaAutorizacionServicio : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para generar la autorizacion de servicio
	* @param Map que contiene la informacion del tramite
	* @return Autorizacion de Servicio
	*/
	private String generarAutoriServicio(Map<String, Object> paramsO){
		logger.debug("Entra a generarAutoriServicio Valores para generarAutoriServicio: {}", paramsO);
		try {
			String caseIdRstn = null;
			if (params != null && params.containsKey("caseIdRstn") && StringUtils.isNotBlank(params.get("caseIdRstn"))) {
			    caseIdRstn = params.get("caseIdRstn");
			}
			
			File carpeta=new File(rutaDocumentosPoliza + "/" + paramsO.get("pv_ntramite_i"));
			if(!carpeta.exists()){
				logger.debug("no existe la carpeta:::  {}", paramsO.get("pv_ntramite_i"));
				carpeta.mkdir();
				if(carpeta.exists()){
					logger.debug("carpeta creada");
				} else {
					logger.debug("carpeta NO creada");
				}
			} else {
				logger.debug("existe la carpeta:::  {}", paramsO.get("pv_ntramite_i"));
			}

			UserVO usuario=(UserVO)session.get("USUARIO");
			String reporteSeleccion = null;
			if(paramsO.get("pv_cdramo_i").toString().equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo())){
				reporteSeleccion = getText("rdf.siniestro.autorizacion.servicio.nombre");
			}
			if(paramsO.get("pv_cdramo_i").toString().equalsIgnoreCase(Ramo.MULTISALUD.getCdramo())){
				reporteSeleccion = getText("rdf.siniestro.autorizacion.servicio.nombre.MS");
			}
			if(paramsO.get("pv_cdramo_i").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
				reporteSeleccion = getText("rdf.siniestro.autorizacion.servicio.nombre.GMMI");
			}
			if(paramsO.get("pv_cdramo_i").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
				reporteSeleccion = getText("rdf.siniestro.autorizacion.servicio.nombre.GNP");
			}
			
			String urlAutorizacionServicio = ""
				+ rutaServidorReports
				+ "?p_unieco=" +  paramsO.get("pv_cdunieco_i")
				+ "&p_ramo=" + paramsO.get("pv_cdramo_i")
				+ "&p_estado=" + paramsO.get("pv_estado_i")
				+ "&p_poliza=" + paramsO.get("pv_nmpoliza_i")
				+ "&P_AUTSER=" + paramsO.get("pv_nmAutSer_i")
				+ "&P_CDPERSON=" + paramsO.get("pv_cdperson_i")
				+ "&destype=cache"
				+ "&desformat=PDF"
				+ "&userid="+passServidorReports
				+ "&ACCESSIBLE=YES"
				+ "&report="+reporteSeleccion
				+ "&paramform=no"
				;
			logger.debug("urlAutorizacionServicio: {}", urlAutorizacionServicio);
			//logger.debug(getText("siniestro.autorizacionServicio.nombre").substring(beginIndex));
			String nombreArchivo = getText("siniestro.autorizacionServicio.nombre");
			
			String nombreArchivoModificado = nombreArchivo.substring(nombreArchivo.indexOf(".")+1)+System.currentTimeMillis()+"_"+((long)(Math.random()*10000l))+".pdf";
			String pathArchivo=""
				+ rutaDocumentosPoliza
				+ "/" + paramsO.get("pv_ntramite_i")
				+ "/" + nombreArchivoModificado
				;
			HttpUtil.generaArchivo(urlAutorizacionServicio, pathArchivo);
			
			//paramsO.put("pv_feinici_i"  , new Date());
			//paramsO.put("pv_cddocume_i" , nombreArchivoModificado);
			//paramsO.put("pv_dsdocume_i" , "Autorizacion Servicio "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			//paramsO.put("pv_swvisible_i"   , null);
			//paramsO.put("pv_codidocu_i"   , null);
			//paramsO.put("pv_cdtiptra_i"   , TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra());
			//paramsO.put("pv_nmsolici_i",null);
			//paramsO.put("pv_tipmov_i",TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra());
			//logger.debug("Valor de paramsO {}", paramsO);
			//kernelManagerSustituto.guardarArchivo(paramsO);
			
			documentosManager.guardarDocumento(
					(String)paramsO.get("pv_cdunieco_i")
					,(String)paramsO.get("pv_cdramo_i")
					,(String)paramsO.get("pv_estado_i")
					,(String)paramsO.get("pv_nmpoliza_i")
					,(String)paramsO.get("pv_nmsuplem_i")
					,new Date()
					,nombreArchivoModificado
					,"Autorizacion Servicio "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())
					,null
					,(String)paramsO.get("pv_ntramite_i")
					,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
					,null
					,null
					,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
					,null
					,null
					,null
					,null, false
					);
			
			String totalConcepto = siniestrosManager.obtieneMedicoEquipoQuirurgico((String) paramsO.get("pv_nmAutSer_i"));
			if(Integer.parseInt(totalConcepto)> 0 && paramsO.get("pv_cdramo_i").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
				String urlAutorizacionServicioMed = ""
						+ rutaServidorReports
						+ "?p_unieco=" +  paramsO.get("pv_cdunieco_i")
						+ "&p_ramo=" + paramsO.get("pv_cdramo_i")
						+ "&p_estado=" + paramsO.get("pv_estado_i")
						+ "&p_poliza=" + paramsO.get("pv_nmpoliza_i")
						+ "&P_AUTSER=" + paramsO.get("pv_nmAutSer_i")
						+ "&P_CDPERSON=" + paramsO.get("pv_cdperson_i")
						+ "&destype=cache"
						+ "&desformat=PDF"
						+ "&userid="+passServidorReports
						+ "&ACCESSIBLE=YES"
						+ "&report="+getText("rdf.siniestro.autorizacion.servicio.nombre.GNPEXP")
						+ "&paramform=no"
						;
					logger.debug("urlAutorizacionServicio: {}", urlAutorizacionServicioMed);
					//logger.debug(getText("siniestro.autorizacionServicio.nombre").substring(beginIndex));
					String nombreArchivoMed = getText("siniestro.autorizacionServicio.nombreMed");
					
					String nombreArchivoModificadoMed = nombreArchivoMed.substring(nombreArchivoMed.indexOf(".")+1)+System.currentTimeMillis()+"_"+((long)(Math.random()*10000l))+".pdf";
					String pathArchivoMed=""
						+ rutaDocumentosPoliza
						+ "/" + paramsO.get("pv_ntramite_i")
						+ "/" + nombreArchivoModificadoMed
						;
					HttpUtil.generaArchivo(urlAutorizacionServicioMed, pathArchivoMed);
					
					documentosManager.guardarDocumento(
						(String)paramsO.get("pv_cdunieco_i")
						,(String)paramsO.get("pv_cdramo_i")
						,(String)paramsO.get("pv_estado_i")
						,(String)paramsO.get("pv_nmpoliza_i")
						,(String)paramsO.get("pv_nmsuplem_i")
						,new Date()
						,nombreArchivoModificadoMed
						,"Autorizacion Servicio Medico "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())
						,null
						,(String)paramsO.get("pv_ntramite_i")
						,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
						,null
						,null
						,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
						,null
						,null
						,null
						,null, false
					);
			}
			if (Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals((String)paramsO.get("pv_cdramo_i"))) {
                HttpUtil.enviarArchivoRSTN(
                        HttpUtil.RSTN_AUTORIZACION_PATH + caseIdRstn,
                        pathArchivo, 
                        "Autorizacion Servicio "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()),
                        HttpUtil.RSTN_DOC_CLASS_SINIESTROS);
            }
		}catch( Exception e){
			logger.error("Error generarAutoriServicio {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para obtener el listia de la plazas
	* @param no recibe ningun valor
	* @return Listado de las plazas
	*/
	public String consultaListaPlazas(){
		logger.debug("Entra a consultaListaPlazas");
		try {
			listaPlazas= siniestrosManager.getConsultaListaPlaza();
			logger.debug("total de registros: {}", listaPlazas.size());
		}catch( Exception e){
			logger.error("Error consultaListaPlazas : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para consultar el numero de dias maximo
	* @param Ramo, cdtipsit
	* @return diasMaximos - Numero de dias maximo
	*/
	public String consultaNumeroDias(){
		logger.debug("Entra a consultaNumeroDias Params: {}", params);
		try {
			diasMaximos= catalogosManager.obtieneCantidadMaxima(params.get("cdramo"), params.get("cdtipsit"), 
												TipoTramite.SINIESTRO, Rango.DIAS, Validacion.DIAS_MAX_AUTORIZACION_SERVICIOS);
		}catch( Exception e){
			logger.error("Error consultaAutorizacionServicio : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para validar si existe exclusion de penalizacion
	* @param unieco, Ramo, Estado, Nmpoliza, nmsituac
	* @return existePenalizacion - Exlusion de penalizacion
	*/
	public String validaExclusionPenalizacion(){
		logger.debug("Entra a validaExclusionPenalizacion  Params: {}", params);
		try {
			HashMap<String, Object> paramExclusion = new HashMap<String, Object>();
			paramExclusion.put("pv_cdunieco_i",params.get("cdunieco"));
			paramExclusion.put("pv_estado_i",params.get("estado"));
			paramExclusion.put("pv_cdramo_i",params.get("cdramo"));
			paramExclusion.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramExclusion.put("pv_nmsituac_i",params.get("nmsituac"));
			existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
			logger.debug("existePenalizacion: {}", existePenalizacion);
		}catch( Exception e){
			logger.error("Error validaExclusionPenalizacion : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para obtener el porcentaje de penalizacion por la zona contratada
	* @param zonaContratada, zonaAtencion, cdRamo
	* @return porcentajePenalizacion - Porcentaje de Penalizacion x Zona Contratada
	*/
	public String validaPorcentajePenalizacion(){
		logger.debug("Entra a validaPorcentajePenalizacion Params: {}", params);
		try {
			porcentajePenalizacion = siniestrosManager.validaPorcentajePenalizacion(params.get("zonaContratada"), params.get("zonaAtencion"), params.get("cdRamo"));
			logger.debug("porcentajePenalizacion : {}", porcentajePenalizacion);
		}catch( Exception e){
			logger.error("Error validaPorcentajePenalizacion : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para obtener el monto Maximo
	* @param ccdramo, cdtipsit
	* @return montoMaximo - Monto Maximo
	*/
	public String consultaMontoMaximo(){
		logger.debug("Entra a consultaMontoMaximo Params: {}", params);
		try {
			montoMaximo = catalogosManager.obtieneCantidadMaxima(params.get("cdramo"), params.get("cdtipsit"), 
											TipoTramite.SINIESTRO, Rango.PESOS, Validacion.MONTO_MAXIMO_AUTORIZACION_SERVICIOS);
			logger.debug("montoMaximo : {}", montoMaximo);
		}catch( Exception e){
			logger.error("Error consultaMontoMaximo : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para obtener el numero de meses maximo en maternidad
	* @param ccdramo, cdtipsit
	* @return mesMaximoMaternidad - Numero de meses maximo
	*/
	public String consultaMesesMaximoMaternidad(){
		logger.debug("Entra a consultaMesesMaximoMaternidad Params: {}", params);
		try {
			mesMaximoMaternidad = catalogosManager.obtieneCantidadMaxima(params.get("cdramo"), params.get("cdtipsit"), 
														TipoTramite.SINIESTRO, Rango.MESES, Validacion.MESES_MAX_MATERNIDAD);
			logger.debug("mesMaximoMaternidad : {}", mesMaximoMaternidad);
		}catch( Exception e){
			logger.error("Error consultaMesesMaximoMaternidad : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para validar si tiene todos los documentos cargados
	* @param ntramite
	* @return existeDocAutServicio - Existe Documentos cargados
	*/
	public String validaDocumentosAutoServ(){
		logger.debug("Entra a validaDocumentosAutoServ Params: {}", params);
		try {
			existeDocAutServicio = siniestrosManager.validaDocumentosAutServicio(params.get("ntramite"));
			logger.debug("existeDocAutServicio: {}", existeDocAutServicio);
		}catch( Exception e){
			logger.error("Error validaDocumentosAutoServ : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	
	/**
	* metodo el numero de meses maximo de periodo de espera
	* @param otvalor, cdtabla
	* @return mesesTiempoEspera - Numero maximo de tiempo de espera
	*/
	public String obtieneMesesTiempoEspera(){
		logger.debug("Entra a obtieneMesesTiempoEspera Params: {}", params);
		try {
			if(params.get("cdramo").equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo()) || params.get("cdramo").equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
				mesesTiempoEspera = siniestrosManager.obtieneMesesTiempoEsperaICD(params.get("cdramo"),params.get("cdtipsit"),params.get("cdicd"),params.get("dsplan"));
				mensaje = "Movimiento no procede por padecimiento de periodo de espera de "+(Integer.parseInt(mesesTiempoEspera)/12)+" años";
			}else{
				mesesTiempoEspera = siniestrosManager.obtieneMesesTiempoEspera(params.get("otvalor01"),params.get("cdtabla"));
				mensaje = "Movimiento no procede por padecimiento de periodo de espera de "+(Integer.parseInt(mesesTiempoEspera)/12)+" años";
			}

			logger.debug("mesesTiempoEspera: {} mensaje de respuesta : {}", mesesTiempoEspera,mensaje);
		}catch( Exception e){
			logger.error("Error obtieneMesesTiempoEspera : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que valida el circulo hospitalario para Multisalud
	* @param params (feautori, cdramo, cdpresta)
	* @return datosInformacionAdicional del Multisalud
	*/
	public String consultaCirculoHospitalarioMultisalud(){
		logger.debug("Entra a consultaCirculoHospitalarioMultisalud Params: {}", params);
		try {
			String fechaAutorizacion = params.get("feautori");
			String feAutorizacion= fechaAutorizacion.substring(8,10)+"/"+fechaAutorizacion.substring(5,7)+"/"+fechaAutorizacion.substring(0,4);
			datosInformacionAdicional = siniestrosManager.listaConsultaCirculoHospitalarioMultisalud(params.get("cdpresta"),params.get("cdramo"),renderFechas.parse(feAutorizacion));
			logger.debug("Total datosInformacionAdicional: {}", datosInformacionAdicional.size());
		}catch( Exception e){
			logger.error("Error consultaCirculoHospitalarioMultisalud : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que valida el Circulo Hospitalario
	* @param params (feautori, circuloHosPoliza, circuloHosProv, cdramo)
	* @return porcentajePenalizacion - Porcentaje de penalizacion
	*/
	public String consultaPenalizacionCirculoHospitalario(){
		logger.debug("Entra a consultaPenalizacionCirculoHospitalario Params: {}", params);
		try {
			String fechaAutorizacion = params.get("feautori");
			String feAutorizacion= fechaAutorizacion.substring(8,10)+"/"+fechaAutorizacion.substring(5,7)+"/"+fechaAutorizacion.substring(0,4);
			HashMap<String, Object> paramPenalizacion = new HashMap<String, Object>();
			paramPenalizacion.put("pv_circuloHosPoliza_i",params.get("circuloHosPoliza"));
			paramPenalizacion.put("pv_circuloHosProv_i",params.get("circuloHosProv"));
			paramPenalizacion.put("pv_cdramo_i",params.get("cdramo"));
			paramPenalizacion.put("pv_feautori_i", feAutorizacion);
			porcentajePenalizacion = siniestrosManager.penalizacionCirculoHospitalario(paramPenalizacion);
			logger.debug("porcentajePenalizacion: {}", porcentajePenalizacion);
		}catch( Exception e){
			logger.error("Error consultaPenalizacionCirculoHospitalario : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtener el porcentaje Quirurgico
	* @param params (feautori, tipoMedico)
	* @return msgResult - Porcentaje
	*/
	public String consultaPorcentajeQuirurgico(){
		logger.debug("Entra a consultaPorcentajeQuirurgico Params: {}", params);
		try {
			String fechaAutorizacion = params.get("feautori");
			String feAutorizacion= fechaAutorizacion.substring(8,10)+"/"+fechaAutorizacion.substring(5,7)+"/"+fechaAutorizacion.substring(0,4);
			
			msgResult = siniestrosManager.porcentajeQuirurgico(params.get("tipoMedico"), feAutorizacion);
			logger.debug("msgResult : {}", msgResult);
		}catch( Exception e){
			logger.error("Error consultaPorcentajeQuirurgico :{}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para la validacion de la autorizacion en proceso
	* @param nmAutSer
	* @return autorizarProceso - Autorizacion en proceso
	*/
	public String validaAutorizacionProceso(){
		logger.debug("Entra a validaAutorizacionProceso Params: {}", params);
		try {
			autorizarProceso = siniestrosManager.validaAutorizacionProceso(params.get("nmAutSer"));
			logger.debug("autorizarProceso: {}", autorizarProceso);
		}catch( Exception e){
			logger.error("Error consultaAutorizacionServicio : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion para cambiar el status de la autorizacion de servicio
	* @param nmAutSer, status
	* @return cambioEstatus
	*/
	public String cambiarEstatusMAUTSERV(){
		logger.debug("Entra a cambiarEstatusMAUTSERV Params: {}", params);
		try {
			siniestrosManager.getCambiarEstatusMAUTSERV(params.get("nmautser"), params.get("status"));
		}catch( Exception e){
			logger.error("Error cambiarEstatusMAUTSERV : {}", e.getMessage(), e);
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para guardar la carta de rechazo en autorizacion de servicio
	* @param ntramite, comments
	* @return PDF - Carta de rechazo
	*/
	public String guardarCartaRechazoAutServ() {
		logger.debug("Entra a guardarCartaRechazoAutServ map1: {}", map1);
		String ntramite    = map1.get("ntramite");
		String comments    = map1.get("comments");
		logger.debug("comments: {}", comments); 
		String commentsM   = comments.replaceAll("\n", "%0A").
                replaceAll("\u00E1", "%C3%A1").
                replaceAll("\u00E9", "%C3%A9").
                replaceAll("\u00ED", "%C3%AD").
                replaceAll("\u00F3", "%C3%B3").
                replaceAll("\u00FA", "%C3%BA").
                replaceAll("\u00F1", "%C3%B1").
                replaceAll("\u00C1", "%C3%81").
                replaceAll("\u00C9", "%C3%89").
                replaceAll("\u00CD", "%C3%8D").
                replaceAll("\u00D3", "%C3%93").
                replaceAll("\u00DA", "%C3%9A").
                replaceAll("\u00D1", "%C3%91");
		String cdsisrol    = map1.get("cdsisrol");
		String cdunieco    = map1.get("cdunieco");
		String cdramo      = map1.get("cdramo");
		String estado      = map1.get("estado");
		String nmpoliza    = map1.get("nmpoliza");
		String nmsuplem    = map1.get("nmsuplem");
		String rutaCarpeta = this.rutaDocumentosPoliza+"/"+ntramite;

		File carpeta=new File(this.rutaDocumentosPoliza+"/"+ntramite);
		if(!carpeta.exists()){
			logger.debug("no existe la carpeta: {}", ntramite);
			carpeta.mkdir();
			if(carpeta.exists()){
				logger.debug("carpeta creada");
			} else {
				logger.debug("carpeta NO creada");
			}
		} else {
			logger.debug("existe la carpeta: {}", ntramite);
		}
		String url         = this.rutaServidorReports
						+ "?destype=cache"
						+ "&desformat=PDF"
						+ "&userid="+this.passServidorReports
						+ "&report="+(cdsisrol.equalsIgnoreCase(RolSistema.MEDICO.getCdsisrol())?
						this.getText("rdf.emision.rechazo.medico.nombre"):
						this.getText("rdf.emision.rechazo.admin.nombre"))
						+ "&paramform=no"
						+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
						+ "&p_ntramite="+ntramite
						+ "&p_ramo="+cdramo
						+ "&p_comments="+commentsM;
		logger.debug("Se solicita reporte : {}", url);
		HttpUtil.generaArchivo(url,rutaCarpeta+"/"+this.getText("pdf.emision.rechazo.nombre"));
		try{
			//HashMap<String, Object> paramsR = new HashMap<String, Object>();
			//paramsR.put("pv_cdunieco_i"  , cdunieco);
			//paramsR.put("pv_cdramo_i"    , cdramo);
			//paramsR.put("pv_estado_i"    , estado);
			//paramsR.put("pv_nmpoliza_i"  , nmpoliza);
			//paramsR.put("pv_nmsuplem_i"  , 0);
			//paramsR.put("pv_feinici_i"   , new Date());
			//paramsR.put("pv_cddocume_i"  , this.getText("pdf.emision.rechazo.nombre"));
			//paramsR.put("pv_dsdocume_i"  , "CARTA RECHAZO");
			//paramsR.put("pv_nmsolici_i"  , nmpoliza);
			//paramsR.put("pv_ntramite_i"  , ntramite);
			//paramsR.put("pv_tipmov_i"    , TipoTramite.POLIZA_NUEVA.getCdtiptra());
			//paramsR.put("pv_swvisible_i" , Constantes.SI);
			//kernelManagerSustituto.guardarArchivo(paramsR);
			
			documentosManager.guardarDocumento(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,new Date()
					,this.getText("pdf.emision.rechazo.nombre")
					,"CARTA RECHAZO"
					,nmpoliza
					,ntramite
					,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
					,Constantes.SI
					,null
					,TipoTramite.AUTORIZACION_SERVICIOS.getCdtiptra()
					,null
					,null
					,null
					,null, false
					);
			
		}
		catch(Exception ex){
			logger.error("Error guardarCartaRechazoAutServ : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para validar los datos de la Causa del siniestro de acuerdo al producto
	* @param params (feautori, cdramo, cdpresta)
	* @return datosInformacionAdicional del Multisalud
	*/
	public String consultaInfCausaSiniestroProducto(){
		logger.debug("Entra a consultaInfCausaSiniestroProducto Params: {}", params);
		try {
			
			HashMap<String, Object> paramsCausaSini = new HashMap<String, Object>();
			paramsCausaSini.put("pv_cdramo_i",params.get("cdramo"));
			paramsCausaSini.put("pv_cdtipsit_i",params.get("cdtipsit"));
			paramsCausaSini.put("pv_causa_i",params.get("causaSini"));
			paramsCausaSini.put("pv_codigo_i",params.get("cveCausa"));
			datosInformacionAdicional = siniestrosManager.listaConsultaInfCausaSiniestroProducto(paramsCausaSini);
			
			
			logger.debug("Total datosInformacionAdicional: {}", datosInformacionAdicional.size());
		}catch( Exception e){
			logger.error("Error consultaInfCausaSiniestroProducto : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String obtieneImporteArancelGNP(){
		logger.debug("Entra a obtieneImporteArancelGNP params de entrada :{}",params);
		try {
			msgResult = siniestrosManager.obtieneImporteArancelGNP(params.get("cdpresta"),params.get("cpt"));
			logger.debug("VALOR DE RESPUESTA ===>: {}", msgResult);
			
		}catch( Exception e){
			logger.error("Error al obtieneImporteArancelGNP el monto del arancel : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String obtieneDatosGeneralesICD(){
		logger.debug("Entra a consultaPorcentajeQuirurgico Params: {}", params);
		try {
			msgResult = siniestrosManager.obtieneDatosGeneralesICD(params.get("cdunieco"), params.get("cdramo"),
					params.get("estado"),params.get("nmpoliza"),params.get("cdicd"),params.get("cdperson"));
			logger.debug("msgResult : {}", msgResult);
		}catch( Exception e){
			logger.error("Error consultaPorcentajeQuirurgico :{}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String obtenerValidacionExclusionICD(){
		logger.debug("Entra a obtenerValidacionExclusionICD Params: {}", params);
		try {
			msgResult = siniestrosManager.obtenerValidacionExclusionICD(params.get("cdunieco"), params.get("cdramo"),
					params.get("estado"),params.get("nmpoliza"),params.get("nmsuplem"),params.get("nmsituac"),params.get("cdicd"));
			logger.debug("msgResult : {}", msgResult);
		}catch( Exception e){
			logger.error("Error obtenerValidacionExclusionICD :{}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}
	
	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}
	
	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}
	
	public void setParamsJson(HashMap<String, String> params2) {
		this.params = params2;
	}

	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params",e);
			return null;
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public SiniestrosManager getSiniestrosManager() {
		return siniestrosManager;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

	public KernelManagerSustituto getKernelManagerSustituto() {
		return kernelManagerSustituto;
	}

	public void setKernelManagerSustituto(KernelManagerSustituto kernelManagerSustituto) {
		this.kernelManagerSustituto = kernelManagerSustituto;
	}

	public CatalogosManager getCatalogosManager() {
		return catalogosManager;
	}

	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<AutorizaServiciosVO> getListaAutorizacion() {
		return listaAutorizacion;
	}

	public void setListaAutorizacion(List<AutorizaServiciosVO> listaAutorizacion) {
		this.listaAutorizacion = listaAutorizacion;
	}

	public AutorizacionServicioVO getDatosAutorizacionEsp() {
		return datosAutorizacionEsp;
	}

	public void setDatosAutorizacionEsp(AutorizacionServicioVO datosAutorizacionEsp) {
		this.datosAutorizacionEsp = datosAutorizacionEsp;
	}

	public List<DatosSiniestroVO> getListaDatosSiniestro() {
		return listaDatosSiniestro;
	}

	public void setListaDatosSiniestro(List<DatosSiniestroVO> listaDatosSiniestro) {
		this.listaDatosSiniestro = listaDatosSiniestro;
	}

	public List<ConsultaPorcentajeVO> getListaPorcentaje() {
		return listaPorcentaje;
	}

	public void setListaPorcentaje(List<ConsultaPorcentajeVO> listaPorcentaje) {
		this.listaPorcentaje = listaPorcentaje;
	}
	
	public List<ConsultaManteniVO> getListaConsultaManteni() {
		return listaConsultaManteni;
	}

	public void setListaConsultaManteni(
		List<ConsultaManteniVO> listaConsultaManteni) {
		this.listaConsultaManteni = listaConsultaManteni;
	}
	
	public List<ConsultaTDETAUTSVO> getListaConsultaTablas() {
		return listaConsultaTablas;
	}

	public void setListaConsultaTablas(List<ConsultaTDETAUTSVO> listaConsultaTablas) {
		this.listaConsultaTablas = listaConsultaTablas;
	}
	
	public List<HashMap<String, String>> getDatosTablas() {
		return datosTablas;
	}

	public void setDatosTablas(List<HashMap<String, String>> datosTablas) {
		this.datosTablas = datosTablas;
	}
	
	public AutorizacionServicioVO getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(AutorizacionServicioVO numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
	public List<GenericVO> getListaPlazas() {
		return listaPlazas;
	}

	public void setListaPlazas(List<GenericVO> listaPlazas) {
		this.listaPlazas = listaPlazas;
	}
	
	public String getDiasMaximos() {
		return diasMaximos;
	}

	public void setDiasMaximos(String diasMaximos) {
		this.diasMaximos = diasMaximos;
	}
	
	public String getExistePenalizacion() {
		return existePenalizacion;
	}

	public void setExistePenalizacion(String existePenalizacion) {
		this.existePenalizacion = existePenalizacion;
	}
	
	public String getPorcentajePenalizacion() {
		return porcentajePenalizacion;
	}

	public void setPorcentajePenalizacion(String porcentajePenalizacion) {
		this.porcentajePenalizacion = porcentajePenalizacion;
	}
	
	public String getMontoMaximo() {
		return montoMaximo;
	}

	public void setMontoMaximo(String montoMaximo) {
		this.montoMaximo = montoMaximo;
	}
	
	public String getMesMaximoMaternidad() {
		return mesMaximoMaternidad;
	}

	public void setMesMaximoMaternidad(String mesMaximoMaternidad) {
		this.mesMaximoMaternidad = mesMaximoMaternidad;
	}
	
	public String getExisteDocAutServicio() {
		return existeDocAutServicio;
	}

	public void setExisteDocAutServicio(String existeDocAutServicio) {
		this.existeDocAutServicio = existeDocAutServicio;
	}
	
	public String getMesesTiempoEspera() {
		return mesesTiempoEspera;
	}

	public void setMesesTiempoEspera(String mesesTiempoEspera) {
		this.mesesTiempoEspera = mesesTiempoEspera;
	}
	
	public List<Map<String, String>> getDatosInformacionAdicional() {
		return datosInformacionAdicional;
	}

	public void setDatosInformacionAdicional(List<Map<String, String>> datosInformacionAdicional) {
		this.datosInformacionAdicional = datosInformacionAdicional;
	}
	
	public String getMsgResult() {
		return msgResult;
	}

	public void setMsgResult(String msgResult) {
		this.msgResult = msgResult;
	}
	
	public String getAutorizarProceso() {
		return autorizarProceso;
	}

	public void setAutorizarProceso(String autorizarProceso) {
		this.autorizarProceso = autorizarProceso;
	}
	
	public void setMap1(Map<String, String> map1) {
		this.map1 = map1;
	}
	
	public Map<String, String> getMap1() {
		return map1;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
    public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}
}