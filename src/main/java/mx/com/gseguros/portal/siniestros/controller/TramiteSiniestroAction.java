package mx.com.gseguros.portal.siniestros.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.TipoPago;
import mx.com.gseguros.portal.general.util.TipoPrestadorServicio;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.model.AltaTramiteVO;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

public class TramiteSiniestroAction extends PrincipalCoreAction {
	private static final long serialVersionUID = 1292525260646267925L;
	private DateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	static final Logger logger = LoggerFactory.getLogger(TramiteSiniestroAction.class);
	private boolean success;
	private SiniestrosManager siniestrosManager;
	private KernelManagerSustituto kernelManagerSustituto;
	private transient CatalogosManager catalogosManager;
	private PantallasManager       pantallasManager;
	private transient Ice2sigsService ice2sigsService;
	private Map<String,String> params;
	private List<AltaTramiteVO> listaAltaTramite;
	private List<HashMap<String,String>> datosTablas;
	private String msgResult;
	private List<Map<String,String>> slist1;
	private String	mensaje;
	private String factPagada;
	private Map<String,String> smap;
	private List<HashMap<String, String>> loadList;
	private HashMap<String,Object> paramsO;
	private String validaCdTipsitTramite;
	
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
    
	/**
	* Funcion para cargar la pantalla principal del alta de tramite
	* @param params
	* @return params - Params con los valores de unieco, tramite y rol
	*/
	public String altaTramite(){
		logger.debug("Entra a altaTramite Params: {}", params);
		try {
			String ntramite = null;
			String cdunieco = null;
			UserVO usuario  = (UserVO)session.get("USUARIO");
			String cdUnieco = usuario.getCdUnieco();
			String rolUsuario = usuario.getRolActivo().getClave();
			String caseIdRstn = null;
			cdunieco = usuario.getCdUnieco().toString();
			if(params != null){
				cdunieco  = params.get("cdunieco");
				ntramite  = params.get("ntramite");
				caseIdRstn = params.get("caseIdRstn");
			}
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("cdunieco",cdunieco);
			params.put("ntramite",ntramite);
			params.put("RolSiniestro", rolUsuario);
			params.put("caseIdRstn", caseIdRstn);
			setParamsJson(params);
			logger.debug("Params : {}", params);
		}catch( Exception e){
			logger.error("Error en el alta de Tramite : {}", e.getMessage(), e);
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el listado del alta del tramite
	* @param ntramite
	* @return Lista AltaTramiteVO - tramite Alta Tramite
	*/
	public String consultaListadoAltaTramite(){
		logger.debug("Entra a consultaListadoAltaTramite Params: {}", params);
		try {
			List<AltaTramiteVO> lista = siniestrosManager.getConsultaListaAltaTramite(params.get("ntramite"));
			if(lista!=null && !lista.isEmpty())	listaAltaTramite = lista;
		}catch( Exception e){
			logger.error("Error consultaListadoAltaTramite: {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	/**
	* metodo para el guardado del alta del tramite
	* @param Json con todos los valores del formulario y los grid
	* @return Lista AutorizaServiciosVO con la informacion de los asegurados
	*/
	public String guardaAltaTramite(){
		logger.debug("Entra a guardaAltaTramite Params: {} datosTablas {}", params,datosTablas);
		try{
			// 1.- Guardar en TMESACONTROL 
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			String caseIdRstn = null;
			if (params != null && params.containsKey("caseIdRstn") && StringUtils.isNotBlank(params.get("caseIdRstn"))) {
			    caseIdRstn = params.get("caseIdRstn");
			}
			
			//Si el tr\u00e1mite es nuevo
			if(params.get("idNumTramite").toString().length() <= 0){
				
				Map<String,String> valores = new LinkedHashMap<String,String>();
				valores.put("otvalor02" , params.get("cmbTipoPago"));
				valores.put("otvalor03" , params.get("ImporteIndFactura"));
				valores.put("otvalor04" , params.get("cmbBeneficiario"));
				valores.put("otvalor05" , usuario.getUser());
				valores.put("otvalor06" , params.get("fechaIndFactura"));
				valores.put("otvalor07" , params.get("cmbTipoAtencion"));
				valores.put("otvalor08" , params.get("numIndFactura"));
				valores.put("otvalor09" , params.get("cmbAseguradoAfectado"));
				valores.put("otvalor10" , params.get("dtFechaOcurrencia"));
				valores.put("otvalor11" , params.get("cmbProveedor"));
				valores.put("otvalor15" , params.get("idnombreBeneficiarioProv"));
				valores.put("otvalor20" , params.get("cmbRamos"));
				valores.put("otvalor22" , params.get("txtAutEspecial"));
				valores.put("otvalor26" , params.get("pv_cdtipsit_i"));
				valores.put("otvalor27" , params.get("idCveBeneficiario"));
				
				if(params.get("cmbProveedor").toString().length() > 0){
					valores.put("otvalor13",TipoPrestadorServicio.CLINICA.getCdtipo());
				}
				
				if(params.get("cmbTipoPago").toString().equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo()) && (params.get("cmbRamos").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) || params.get("cmbRamos").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo()))){
					valores.put("otvalor12","7RDH");
					valores.put("otvalor14","7RDH001");
				}
				
				//WrapperResultados res = kernelManagerSustituto.PMovMesacontrol(parMesCon);
				String ntramiteGenerado = mesaControlManager.movimientoTramite(
						params.get("cdunieco")
						,params.get("cmbRamos")
						,params.get("estado")
						,params.get("polizaAfectada")
						,params.get("idNmsuplem")
						,params.get("cmbOficEmisora")
						,params.get("cmbOficReceptora")
						,TipoTramite.SINIESTRO.getCdtiptra()
						,getDate(params.get("dtFechaRecepcion"))
						,null
						,null
						,params.get("idnombreAsegurado")
						,getDate(params.get("dtFechaRecepcion"))
						,EstatusTramite.PENDIENTE.getCodigo()
						,null
						,params.get("idNmsolici")
						,params.get("idCdtipsit")
						,usuario.getUser()
						,usuario.getRolActivo().getClave()
						,null //swimpres
						,null //cdtipflu
						,null //cdflujomc
						,valores, null, null, null, null
						);
				//if(res.getItemMap() == null){
				if(ntramiteGenerado==null){
					logger.error("Sin mensaje respuesta de nmtramite!!");
				}else{
					//msgResult = (String) res.getItemMap().get("ntramite");
					msgResult = ntramiteGenerado;
					logger.debug("Entra a proceso 1");
					ProcesoAltaTramite(msgResult, params.get("cmbRamos"));
				}
			}else{
				//Existe el tr�mite y solo lo vamos a actualizar
				HashMap<String, Object> modMesaControl = new HashMap<String, Object>();
				//1.- Verificamos si cambio el tipo de atenci�n
				List<MesaControlVO> lista = siniestrosManager.getConsultaListaMesaControl(params.get("idNumTramite").toString());
				String valorTipoAtencion = lista.get(0).getOtvalor07mc();
				if(!valorTipoAtencion.equalsIgnoreCase(params.get("cmbTipoAtencion"))){
					siniestrosManager.eliminaDocumentosxTramite(params.get("idNumTramite").toString());
					modMesaControl.put("pv_otvalor01_i",null);
				}else{
					modMesaControl.put("pv_otvalor01_i",lista.get(0).getOtvalor01mc());
				}
				modMesaControl.put("pv_ntramite_i",params.get("idNumTramite"));
				modMesaControl.put("pv_cdunieco_i",params.get("cdunieco"));
				modMesaControl.put("pv_cdramo_i",params.get("cmbRamos"));
				modMesaControl.put("pv_estado_i",params.get("estado"));
				modMesaControl.put("pv_nmpoliza_i",params.get("polizaAfectada"));
				modMesaControl.put("pv_nmsuplem_i",params.get("idNmsuplem"));
				modMesaControl.put("pv_nmsolici_i",params.get("idNmsolici"));
				modMesaControl.put("pv_cdtipsit_i",params.get("idCdtipsit"));
				modMesaControl.put("pv_cdsucadm_i",params.get("cmbOficEmisora"));
				modMesaControl.put("pv_cdsucdoc_i",params.get("cmbOficReceptora"));
				modMesaControl.put("pv_cdtiptra_i",TipoTramite.SINIESTRO.getCdtiptra());
				modMesaControl.put("pv_ferecepc_i",renderFechas.parse(params.get("dtFechaRecepcion")));
				modMesaControl.put("pv_nombre_i",params.get("idnombreAsegurado"));
				modMesaControl.put("pv_festatus_i",renderFechas.parse(params.get("dtFechaRecepcion")));
				modMesaControl.put("pv_status_i",EstatusTramite.PENDIENTE.getCodigo());
				modMesaControl.put("pv_otvalor02_i",params.get("cmbTipoPago"));
				modMesaControl.put("pv_otvalor03_i",params.get("ImporteIndFactura"));
				modMesaControl.put("pv_otvalor04_i",params.get("cmbBeneficiario"));
				modMesaControl.put("pv_otvalor05_i",usuario.getUser());
				modMesaControl.put("pv_otvalor06_i",params.get("fechaIndFactura"));
				modMesaControl.put("pv_otvalor07_i",params.get("cmbTipoAtencion"));
				modMesaControl.put("pv_otvalor08_i",params.get("numIndFactura"));
				modMesaControl.put("pv_otvalor09_i",params.get("cmbAseguradoAfectado"));
				modMesaControl.put("pv_otvalor10_i",params.get("dtFechaOcurrencia"));
				modMesaControl.put("pv_otvalor11_i",params.get("cmbProveedor"));
				modMesaControl.put("pv_otvalor15_i",params.get("idnombreBeneficiarioProv"));
				modMesaControl.put("pv_otvalor20_i",params.get("cmbRamos"));
				modMesaControl.put("pv_otvalor22_i",params.get("txtAutEspecial"));
				modMesaControl.put("pv_otvalor26_i",params.get("pv_cdtipsit_i"));
				modMesaControl.put("pv_otvalor27_i",params.get("idCveBeneficiario"));
				
				if(params.get("cmbTipoPago").toString().equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo()) && (params.get("cmbRamos").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) || params.get("cmbRamos").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo()))){
					modMesaControl.put("pv_otvalor12","7RDH");
					modMesaControl.put("pv_otvalor14","7RDH001");
				}
				siniestrosManager.actualizaValorMC(modMesaControl);
				//2.- Verificamos Si el tipo de pago es: 1.- Reembolso y  2.- Indemnizacion
				if(params.get("cmbTipoPago").trim().equalsIgnoreCase(TipoPago.REEMBOLSO.getCodigo())||params.get("cmbTipoPago").trim().equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo())){
					logger.debug("Entra a proceso 2 : REEMBOLSO E INDEMIZACION"); 
					ProcesoAltaTramite(params.get("idNumTramite"), params.get("cmbRamos"));
				}
			}
		}catch( Exception e){
			logger.error("Error en el guardado de alta de tramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* metodo para el guardado del tramite (TFACMESCTRL Y TWORKSIN) 
	* @param Json con todos los valores del formulario y los grid
	* @return Lista AutorizaServiciosVO con la informacion de los asegurados
	*/
	public String ProcesoAltaTramite(String msgResult, String cdramo) throws Exception {
		logger.debug("Entra a ProcesoAltaTramite");
		// si tipo de pago es Directo
		if(params.get("cmbTipoPago").trim().equalsIgnoreCase(TipoPago.DIRECTO.getCodigo())){
			for(int i=0;i<datosTablas.size();i++) {
				siniestrosManager.guardaListaFacturaSiniestro(
					msgResult, 
					datosTablas.get(i).get("nfactura"),
					renderFechas.parse(datosTablas.get(i).get("ffactura").substring(8,10)+"/"+datosTablas.get(i).get("ffactura").substring(5,7)+"/"+datosTablas.get(i).get("ffactura").substring(0,4)),
					datosTablas.get(i).get("cdtipser"),
					datosTablas.get(i).get("cdpresta"),
					datosTablas.get(i).get("ptimport"),
					null,
					null,
					null,
					null,
					datosTablas.get(i).get("cdmoneda"),
					datosTablas.get(i).get("tasacamb"),
					datosTablas.get(i).get("ptimporta"),
					null,
					null,
					null,
					null,
					null,
					null
				);
			}
		}else{
			/*Se agrega la informaci�n de las facturas*/
			if(params.get("idNumTramite").toString().length() > 0){
				try {
					// Se realiza la eliminacion de las facturas
					//siniestrosManager.getEliminacionFacturaTramite(msgResult, null, "0"); (EGS)
					logger.debug("1 - Antes de eliminar factura: null 0   ", msgResult);
					siniestrosManager.getEliminacionFacturaTramite(msgResult, null, "0",cdramo); // (EGS)
					logger.debug("2 - Despues de eliminar factura");
				} catch (Exception e) {
					logger.error("error al eliminar en TfacMesCtrl : {}", e.getMessage(), e);
				}
			}
			String cobertura = null;
			String subcobertura = null;
			if(params.get("cmbTipoPago").toString().equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo()) && params.get("cmbRamos").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
				cobertura 	 = "7RDH";
				subcobertura = "7RDH001";
			}
			
			for(int i=0;i<datosTablas.size();i++) {
				String nfactura= "0";
				if(params.get("cmbTipoPago").trim().equalsIgnoreCase(TipoPago.REEMBOLSO.getCodigo())){
					nfactura = datosTablas.get(i).get("nfactura");
				}else{
					nfactura= msgResult+""+i;
				}
				siniestrosManager.guardaListaFacturaSiniestro(
					msgResult, 
					nfactura,
					renderFechas.parse(datosTablas.get(i).get("ffactura").substring(8,10)+"/"+datosTablas.get(i).get("ffactura").substring(5,7)+"/"+datosTablas.get(i).get("ffactura").substring(0,4)),
					datosTablas.get(i).get("cdtipser"),
					(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo()))?"0":datosTablas.get(i).get("cdpresta"),
					datosTablas.get(i).get("ptimport"),
					cobertura,
					subcobertura,
					null,
					null,
					datosTablas.get(i).get("cdmoneda"),
					datosTablas.get(i).get("tasacamb"),
					datosTablas.get(i).get("ptimporta"),
					null,
					null,
					null,
					(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo()))?datosTablas.get(i).get("nombprov"):null ,
					null,
					null
				);
			}
	
			if(params.get("idNumTramite").toString().length() > 0){
				try {
					// Eliminacion de tworksin por numero de tramite, 
					siniestrosManager.getEliminacionAsegurado(msgResult, null, "0");
				} catch (Exception e) {
					logger.error("Error al eliminar en TworkSin : {}", e.getMessage(), e);
				}
			}
	
			HashMap<String, Object> paramsTworkSinPagRem = new HashMap<String, Object>();
			paramsTworkSinPagRem.put("pv_nmtramite_i",msgResult);
			paramsTworkSinPagRem.put("pv_cdunieco_i",params.get("cdunieco"));
			paramsTworkSinPagRem.put("pv_cdramo_i",params.get("cmbRamos"));
			paramsTworkSinPagRem.put("pv_estado_i",params.get("estado"));
			paramsTworkSinPagRem.put("pv_nmpoliza_i",params.get("polizaAfectada"));
			paramsTworkSinPagRem.put("pv_nmsolici_i",params.get("idNmsolici"));
			paramsTworkSinPagRem.put("pv_nmsuplem_i",params.get("idNmsuplem"));
			paramsTworkSinPagRem.put("pv_nmsituac_i",params.get("nmsituac"));
			paramsTworkSinPagRem.put("pv_cdtipsit_i",params.get("idCdtipsit"));
			paramsTworkSinPagRem.put("pv_cdperson_i",params.get("cmbAseguradoAfectado"));
			paramsTworkSinPagRem.put("pv_feocurre_i",renderFechas.parse(params.get("dtFechaOcurrencia")));
			paramsTworkSinPagRem.put("pv_nfactura_i",null);
			paramsTworkSinPagRem.put("pv_nmautser_i",null);
			paramsTworkSinPagRem.put("pv_reqautes_i",params.get("txtAutEspecial"));
			paramsTworkSinPagRem.put("pv_nmordina_i",  null);
			siniestrosManager.guardaListaTworkSin(paramsTworkSinPagRem);
			
			//Guardamos la informacion del asegurado
			HashMap<String, Object> paramsAsegurado = new HashMap<String, Object>();
			paramsAsegurado.put("pv_cdunieco_i",params.get("cdunieco"));
			paramsAsegurado.put("pv_cdramo_i",params.get("cmbRamos"));
			paramsAsegurado.put("pv_estado_i",params.get("estado"));
			paramsAsegurado.put("pv_nmpoliza_i",params.get("polizaAfectada"));
			paramsAsegurado.put("pv_nmsuplem_i",params.get("idNmsuplem"));
			paramsAsegurado.put("pv_nmsituac_i",params.get("nmsituac"));
			paramsAsegurado.put("pv_cdperson_i",params.get("cmbAseguradoAfectado"));
			paramsAsegurado.put("pv_nmtelefo_i",params.get("txtTelefono"));
			paramsAsegurado.put("pv_dsemail_i",params.get("txtEmail"));
			siniestrosManager.actualizaTelefonoEmailAsegurado(paramsAsegurado);
			
		}
		success = true;
		return SUCCESS;
	}
	
	public String guardaFacturasTramite(){
		logger.debug("Entra guardaFacturasTramite Params: {} datosTablas: {} ", params,datosTablas);
		try{
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			slist1 = siniestrosManager.obtenerFacturasTramite(params.get("nmtramite"));
			logger.debug("listado : {}", slist1.size());
			
			String valorFactura ="";
			String cdramo = params.get("cmbRamos");
			String tipoPago = params.get("cmbTipoPago");
			for(int i=0;i<datosTablas.size();i++) {
				
				if((cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo()) || cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())|| cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())) && tipoPago.equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo())) {
					valorFactura = params.get("nmtramite")+""+(slist1.size());
				}else{
					valorFactura = datosTablas.get(i).get("nfactura");
				}
				
				siniestrosManager.guardaListaFacturaSiniestro(
					params.get("nmtramite"),
					valorFactura,
					renderFechas.parse(datosTablas.get(i).get("ffactura").substring(8,10)+"/"+datosTablas.get(i).get("ffactura").substring(5,7)+"/"+datosTablas.get(i).get("ffactura").substring(0,4)),
					datosTablas.get(i).get("cdtipser"),
					(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo()))?"0":datosTablas.get(i).get("cdpresta"),
					datosTablas.get(i).get("ptimport"),
					(((cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) || cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())) && tipoPago.equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo()))?"7RDH":null),
					(((cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) || cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo()))&& tipoPago.equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo()))?"7RDH001":null),
					null,
					null,
					datosTablas.get(i).get("cdmoneda"),
					datosTablas.get(i).get("tasacamb"),
					datosTablas.get(i).get("ptimporta"),
					null,
					null,
					null,
					(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo()))?datosTablas.get(i).get("nombprov"):null ,
					null,
					null
				);
				actualizaMesaControlSiniestro(params.get("nmtramite"));
			}
		}catch( Exception e){
			logger.error("Error guardaFacturasTramite {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para el guardado del alta del tramite
	* @param Json con todos los valores del formulario y los grid
	* @return Lista AutorizaServiciosVO con la informacion de los asegurados
	*/
	public void actualizaMesaControlSiniestro (String ntramiteProceso){
		logger.debug("Entra a actualizaMesaControlSiniestro ntramiteProceso: {}", ntramiteProceso);
		try{
			slist1 = siniestrosManager.obtenerFacturasTramite(ntramiteProceso);
			double SumaTotal = 0d;
			String nfacturaInd = null;
			for(int i=0; i< slist1.size();i++){
				SumaTotal += Double.parseDouble(slist1.get(i).get("PTIMPORT"));
				nfacturaInd = slist1.get(i).get("NFACTURA");
			}
			logger.debug("SumaTotal: {}", SumaTotal);
			logger.debug("Total Registros : {}", slist1.size());
			//modificamos la mesa de control valores ot
			List<MesaControlVO> lista = siniestrosManager.getConsultaListaMesaControl(ntramiteProceso);
			logger.debug("Total Registros Mesa Control : {}", lista.size());
			
			HashMap<String, Object> modMesaControl = new HashMap<String, Object>();
			modMesaControl.put("pv_ntramite_i",ntramiteProceso);
			modMesaControl.put("pv_cdunieco_i",lista.get(0).getCduniecomc());
			modMesaControl.put("pv_cdramo_i",lista.get(0).getCdramomc());
			modMesaControl.put("pv_estado_i",lista.get(0).getEstadomc());
			modMesaControl.put("pv_nmpoliza_i",lista.get(0).getNmpolizamc());
			modMesaControl.put("pv_nmsuplem_i",lista.get(0).getNmsuplemmc());
			modMesaControl.put("pv_nmsolici_i",lista.get(0).getNmsolicimc());
			modMesaControl.put("pv_cdtipsit_i",lista.get(0).getCdtipsitmc());
			modMesaControl.put("pv_cdsucadm_i",lista.get(0).getCdsucadmmc());
			modMesaControl.put("pv_cdsucdoc_i",lista.get(0).getCdsucdocmc());
			modMesaControl.put("pv_cdtiptra_i",lista.get(0).getCdtiptramc());
			modMesaControl.put("pv_ferecepc_i",renderFechas.parse(lista.get(0).getFerecepcmc()));
			modMesaControl.put("pv_nombre_i",lista.get(0).getNombremc());
			modMesaControl.put("pv_festatus_i",renderFechas.parse(lista.get(0).getFecstatumc()));
			modMesaControl.put("pv_status_i",lista.get(0).getStatusmc());
			modMesaControl.put("pv_otvalor01_i",lista.get(0).getOtvalor01mc());
			modMesaControl.put("pv_otvalor02_i",lista.get(0).getOtvalor02mc());
			modMesaControl.put("pv_otvalor03_i",SumaTotal+"");
			modMesaControl.put("pv_otvalor04_i",lista.get(0).getOtvalor04mc());
			modMesaControl.put("pv_otvalor05_i",lista.get(0).getOtvalor05mc());
			modMesaControl.put("pv_otvalor06_i",lista.get(0).getOtvalor06mc());
			modMesaControl.put("pv_otvalor07_i",lista.get(0).getOtvalor07mc());
			modMesaControl.put("pv_otvalor08_i",(slist1.size()>1)?null:nfacturaInd);
			modMesaControl.put("pv_otvalor09_i",lista.get(0).getOtvalor09mc());
			modMesaControl.put("pv_otvalor10_i",lista.get(0).getOtvalor10mc());
			modMesaControl.put("pv_otvalor11_i",lista.get(0).getOtvalor11mc());
			modMesaControl.put("pv_otvalor15_i",lista.get(0).getOtvalor15mc());
			modMesaControl.put("pv_otvalor20_i",lista.get(0).getOtvalor20mc());
			modMesaControl.put("pv_otvalor22_i",lista.get(0).getOtvalor22mc());
			modMesaControl.put("pv_otvalor26_i",lista.get(0).getOtvalor26mc());
			modMesaControl.put("pv_otvalor27_i",lista.get(0).getOtvalor20mc());
			siniestrosManager.actualizaValorMC(modMesaControl);
		}catch( Exception e){
			logger.error("Error actualizaMesaControlSiniestro : {}", e.getMessage(), e);
		}
	}
	
	/**
	* Funcion para eliminar las facturas y sus asegurados
	* @param params ntramite,  nfactura
	* @return success - Si el guardado fue correcto
	*/
	public String eliminarFactAsegurado(){
		logger.debug("Entra actualizaMesaControlSiniestro Params: {}", params);
		try{
			String tipoPagoTramite = params.get("tipoPago").toString();
			String procedencia = params.get("procedencia").toString();
			if(procedencia.equalsIgnoreCase("ALTA_TRAMITE")){
				//siniestrosManager.getEliminacionFacturaTramite(params.get("ntramite"),params.get("nfactura"),params.get("valorAccion")); (EGS)
				logger.debug("3 - Antes de eliminar factura: tramite", params.get("ntramite"),params.get("nfactura"), params.get("valorAccion"));
				siniestrosManager.getEliminacionFacturaTramite(params.get("ntramite"), params.get("nfactura"), params.get("valorAccion"),params.get("cdramo")); // (EGS)
				logger.debug("4 - Despues de eliminar factura");
				siniestrosManager.getEliminacionAsegurado(params.get("ntramite"),params.get("nfactura"),params.get("valorAccion"));
				success=true;
			}else{
				List<Map<String,String>> aseguradosFactura = siniestrosManager.listaSiniestrosTramite2(params.get("ntramite"),params.get("nfactura"));
				logger.debug("Paso 8.- Obtenemos los Asegurados (MSINIEST) : {}",aseguradosFactura);
				
				//siniestrosManager.getEliminacionFacturaTramite(params.get("ntramite"),params.get("nfactura"),params.get("valorAccion")); (EGS)
				logger.debug("5 - Antes de eliminar factura: {} ", params.get("ntramite"), params.get("nfactura"), params.get("valorAccion"));
				siniestrosManager.getEliminacionFacturaTramite(params.get("ntramite"), params.get("nfactura"), params.get("valorAccion"),params.get("cdramo")); // (EGS)
				logger.debug("6 - Despues de eliminar factura");
				if(TipoPago.DIRECTO.getCodigo().equals(tipoPagoTramite)){
					//siniestrosManager.getEliminacionAsegurado(params.get("ntramite"),params.get("nfactura"),params.get("valorAccion"));
					for(int a=0; a< aseguradosFactura.size();a++){
						HashMap<String, Object> paramsTworkSin = new HashMap<String, Object>();
						paramsTworkSin.put("pv_nmtramite_i",params.get("ntramite"));
						paramsTworkSin.put("pv_nfactura_i",params.get("nfactura"));
						paramsTworkSin.put("pv_cdunieco_i",aseguradosFactura.get(a).get("CDUNIECO"));
						paramsTworkSin.put("pv_cdramo_i", aseguradosFactura.get(a).get("CDRAMO"));
						paramsTworkSin.put("pv_estado_i", aseguradosFactura.get(a).get("ESTADO"));
						paramsTworkSin.put("pv_nmpoliza_i", aseguradosFactura.get(a).get("NMPOLIZA"));
						paramsTworkSin.put("pv_nmsuplem_i", aseguradosFactura.get(a).get("NMSUPLEM"));
						paramsTworkSin.put("pv_nmsituac_i", aseguradosFactura.get(a).get("NMSITUAC"));
						paramsTworkSin.put("pv_cdtipsit_i", aseguradosFactura.get(a).get("CDTIPSIT"));
						paramsTworkSin.put("pv_cdperson_i", aseguradosFactura.get(a).get("CDPERSON"));
						paramsTworkSin.put("pv_feocurre_i",renderFechas.parse(aseguradosFactura.get(a).get("FEOCURRE")));
						paramsTworkSin.put("pv_nmsinies_i",aseguradosFactura.get(a).get("NMSINIES"));
						paramsTworkSin.put("pv_accion_i","1");
						siniestrosManager.eliminarAsegurado(paramsTworkSin);
						
						siniestrosManager.actualizaDatosGeneralesCopago(
							aseguradosFactura.get(a).get("CDUNIECO"), aseguradosFactura.get(a).get("CDRAMO"),
							aseguradosFactura.get(a).get("ESTADO"),   aseguradosFactura.get(a).get("NMPOLIZA"),
							aseguradosFactura.get(a).get("NMSUPLEM"), aseguradosFactura.get(a).get("NMSITUAC"),
							aseguradosFactura.get(a).get("NMSINIES"), params.get("ntramite"),
							params.get("nfactura"),null,
							null,null,
							null,null,
							null,null,
                            null,null,
                            null,Constantes.DELETE_MODE
						);
					}
					actualizaMesaControlSiniestro(params.get("ntramite"));
					success=true;
				}else{
					actualizaMesaControlSiniestro(params.get("ntramite"));
					success=true;
				}
			}
		}
		catch(Exception ex){
			success = false;
			logger.error("Error eliminarFactAsegurado : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de la factura en proceso
	* @param nfactura, cdpresta, ptimport
	* @return factPagada - ntramite en donde se encuentra la factura
	*/ 
	public String consultaFacturaPagada(){
		logger.debug("Entra a consultaFacturaPagada Params: {}", params);
		try {
			factPagada = siniestrosManager.obtieneTramiteEnProceso(params.get("nfactura"), params.get("cdpresta"), params.get("ptimport"));
			logger.debug("factPagada: {}", factPagada);
		}catch( Exception e){
			logger.error("Error consultaFacturaPagada : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de la factura en proceso
	* @param nfactura, cdpresta, ptimport
	* @return factPagada - ntramite en donde se encuentra la factura
	*/ 
	public String consultaDatosBeneficiario(){
		logger.debug("Entra a consultaDatosBeneficiario Params: {}", params);
		try {
			List<Map<String,String>> datosAsegurado = siniestrosManager.obtieneDatosBeneficiario(params.get("cdunieco"), params.get("cdramo"), params.get("estado"),
									params.get("nmpoliza"), params.get("cdperson"));
			
			if(datosAsegurado.size()> 0){
				logger.debug("VALORES DE EDAD: {}", datosAsegurado.get(0).get("EDAD"));
				int edadMinima = Integer.parseInt("18");
				if(Integer.parseInt(datosAsegurado.get(0).get("EDAD")) >= edadMinima){
					mensaje = "";
					success = true;
				}else{
					mensaje = "El Beneficiario es un menor de edad."+"\n"+"Elegir otro.";
					success = false;
				}
			}else{
				mensaje = "";
				success = true;
			}

		}catch( Exception e){
			logger.error("Error consultaDatosBeneficiario {}", e.getMessage(), e);
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para guardar la factura, pero eliminando primero los anteriores
	* @param params y datosTablas  
	* @return success si es exito
	*/ 
	public String guardaFacturaAltaTramite(){
		logger.debug("Entra a guardaFacturaAltaTramite Params: {} datosTabla{}", params,datosTablas);
		try {
			//Realizamos la eliminaci�n de las facturas
			//siniestrosManager.getEliminacionFacturaTramite(params.get("idNumTramite"), null, "0"); (EGS)
			logger.debug("7 - Antes de eliminar factura: null 0 tramite:", params.get("idNumTramite"));
			siniestrosManager.getEliminacionFacturaTramite(params.get("idNumTramite"), null, "0",params.get("cdramo")); // (EGS)
			logger.debug("8 - Despues de eliminar factura");
			for(int i=0;i<datosTablas.size();i++) {
				siniestrosManager.guardaListaFacturaSiniestro(
					params.get("idNumTramite"), 
					datosTablas.get(i).get("nfactura"),
					renderFechas.parse(datosTablas.get(i).get("ffactura").substring(8,10)+"/"+datosTablas.get(i).get("ffactura").substring(5,7)+"/"+datosTablas.get(i).get("ffactura").substring(0,4)),
					datosTablas.get(i).get("cdtipser"),
					datosTablas.get(i).get("cdpresta"),
					datosTablas.get(i).get("ptimport"),
					null,
					null,
					null,
					null,
					datosTablas.get(i).get("cdmoneda"),
					datosTablas.get(i).get("tasacamb"),
					datosTablas.get(i).get("ptimporta"),
					null,
					null,
					null,
					null,
					null,
					null
				);
			}
		}catch( Exception e){
			logger.error("Error guardaFacturaAltaTramite : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener las facturas por el asegurado
	* @param smap  
	* @return List<Map<String, String>>  de las facturas del tramite
	*/ 
	public String validarFacturaAsegurado() {
		logger.debug("Entra a validarFacturaAsegurado smap: {}", smap);
		try {
			String tipoPagoTramite = smap.get("tipoPago");
			String faltaAsegurados="";
			String validacionAseg=null;
			boolean faltaFacturas=true;
			boolean esReembolso = tipoPagoTramite.equalsIgnoreCase(TipoPago.REEMBOLSO.getCodigo()) ||tipoPagoTramite.equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo());
			if(esReembolso){
				validacionAseg = "0";
			}else{
				List<Map<String,String>> facturas=siniestrosManager.obtenerFacturasTramite(smap.get("ntramite"));
				for(Map<String,String>factura:facturas){
					List<Map<String,String>> asegurados =siniestrosManager.obtenerAseguradosTramite(factura.get("NTRAMITE"), factura.get("NFACTURA"));
					if(asegurados.size() <= 0){
						faltaFacturas= false;
						faltaAsegurados = faltaAsegurados +" "+factura.get("NFACTURA");
					}
				}
				if(faltaFacturas){
					validacionAseg = "0";
				}else{
					validacionAseg ="1";
				}
			}
	
			loadList = new ArrayList<HashMap<String,String>>();
			HashMap<String,String>map=new HashMap<String,String>();
			map.put("faltaAsegurados"   ,validacionAseg );
			map.put("facturasFaltantes" , faltaAsegurados);
			loadList.add(map);
			success=true;
		}
		catch(Exception ex) {
			success=false;
			logger.error("Error validarFacturaAsegurado : {}", ex.getMessage(), ex);
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener los asegurados de la factura en el tramite
	* @param params  
	* @return List<Map<String, String>> - Listado de los asegurados
	*/ 
	public String obtenerAseguradosTramite(){
		logger.debug("entra a obtenerAsegurados TramiteParams: {}", params);
		try {
			slist1=siniestrosManager.obtenerAseguradosTramite(params.get("ntramite"), params.get("nfactura"));
			mensaje="Asegurados obtenidos";
			success=true;
		}
		catch(Exception ex) {
			success=false;
			logger.error("Error obtenerAseguradosTramite : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para guardar la informacion de los asegurados
	* @param params y datosTablas
	* @return success - Si el guardado fue correcto
	*/
	public String guardaTworksin(){
		logger.debug("Entra a guardaTworksin Params: {}  datosTablas: {}", params,datosTablas);
		try{
			try {
				// Eliminacion de los datos en tworksin
				siniestrosManager.getEliminacionAsegurado(params.get("idNumTramite"),datosTablas.get(0).get("modFactura"), "1");
				//siniestrosManager.getEliminacionTworksin(msgResult,msgResult);
			} catch (Exception e) {
				logger.error("Error al eliminar en TworkSin : {}", e.getMessage(), e);
			}
			for(int i=0;i<datosTablas.size();i++) {
				HashMap<String, Object> paramsTworkSin = new HashMap<String, Object>();
				paramsTworkSin.put("pv_nmtramite_i",params.get("idNumTramite"));
				paramsTworkSin.put("pv_cdunieco_i",	datosTablas.get(i).get("modUnieco"));
				paramsTworkSin.put("pv_cdramo_i",	datosTablas.get(i).get("modRamo"));
				paramsTworkSin.put("pv_estado_i",	datosTablas.get(i).get("modEstado"));
				paramsTworkSin.put("pv_nmpoliza_i",	datosTablas.get(i).get("modPolizaAfectada"));
				paramsTworkSin.put("pv_nmsolici_i",	datosTablas.get(i).get("modNmsolici"));
				paramsTworkSin.put("pv_nmsuplem_i",	datosTablas.get(i).get("modNmsuplem"));
				paramsTworkSin.put("pv_nmsituac_i",	datosTablas.get(i).get("modNmsituac"));
				paramsTworkSin.put("pv_cdtipsit_i",	datosTablas.get(i).get("modCdtipsit"));
				paramsTworkSin.put("pv_cdperson_i",	datosTablas.get(i).get("modCdperson"));
				paramsTworkSin.put("pv_feocurre_i",	renderFechas.parse(datosTablas.get(i).get("modFechaOcurrencia").substring(8,10)+"/"+datosTablas.get(i).get("modFechaOcurrencia").substring(5,7)+"/"+datosTablas.get(i).get("modFechaOcurrencia").substring(0,4)));
				paramsTworkSin.put("pv_nmautser_i",	null);
				paramsTworkSin.put("pv_nfactura_i",	datosTablas.get(i).get("modFactura"));
				paramsTworkSin.put("pv_reqautes_i", datosTablas.get(i).get("modtxtAutEspecial"));
				paramsTworkSin.put("pv_nmordina_i",  null);
				siniestrosManager.guardaListaTworkSin(paramsTworkSin);
				
				//Guardamos la informacion del asegurado
				HashMap<String, Object> paramsAsegurado = new HashMap<String, Object>();
				paramsAsegurado.put("pv_cdunieco_i",datosTablas.get(i).get("modUnieco"));
				paramsAsegurado.put("pv_cdramo_i",datosTablas.get(i).get("modRamo"));
				paramsAsegurado.put("pv_estado_i",datosTablas.get(i).get("modEstado"));
				paramsAsegurado.put("pv_nmpoliza_i",datosTablas.get(i).get("modPolizaAfectada"));
				paramsAsegurado.put("pv_nmsuplem_i",datosTablas.get(i).get("modNmsuplem"));
				paramsAsegurado.put("pv_nmsituac_i",datosTablas.get(i).get("modNmsituac"));
				paramsAsegurado.put("pv_cdperson_i",datosTablas.get(i).get("modCdperson"));
				paramsAsegurado.put("pv_nmtelefo_i",datosTablas.get(i).get("modTelefono"));
				paramsAsegurado.put("pv_dsemail_i",datosTablas.get(i).get("modEmail"));
				siniestrosManager.actualizaTelefonoEmailAsegurado(paramsAsegurado);
				
				
			}
			mensaje = "Asegurado guardado";
			success = true;
		}
		catch(Exception ex){
			logger.error("Error al guardar tworksin : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para generar Contrarecibo
	* @param paramsO
	* @return Success - Exito se genera el contrarecibo
	*/ 
	public String generarContrarecibo(){
		logger.debug("Entra a generarContrarecibo");
		try {
			
			String tipoPago = (String) paramsO.get("pv_pagoAut_i");
			if(tipoPago.equalsIgnoreCase("0")){
					params =  new HashMap<String, String>();
					params.put("pv_nmtramite_i", (String) paramsO.get("pv_ntramite_i"));
					params.put("pv_cdtippag_i", (String) paramsO.get("pv_cdtippag_i"));
					params.put("pv_cdtipate_i", (String) paramsO.get("pv_cdtipate_i"));
					loadList = siniestrosManager.loadListaDocumentos(params);
					if(loadList == null || loadList.isEmpty()){
						msgResult = "No se puede Generar el Contra Recibo. No hay documentos";
						success = false;
						return SUCCESS;
					}
					for(HashMap<String, String> doc: loadList){
						if( "Si".equalsIgnoreCase((String)doc.get("obligatorio")) && !(doc.get("listo")!= null && "true".equalsIgnoreCase((String)doc.get("listo")))){
							msgResult = "No se puede Generar el Contra Recibo ya que en Revision de Documentos no se han marcado como entregados todos los documentos obligatorios (checklist).";
							success = false;
							return SUCCESS;
						}
					}
			}
		}catch( Exception e){
			logger.error("Error en loadListaDocumentos {}", e.getMessage(), e);
			msgResult = "Error al cargar documentos obligatorios";
			success =  false;
			return SUCCESS;
		}
		try {
			logger.debug("generarContrarecibo Siniestros {}", paramsO);
			if(Constantes.MSG_TITLE_ERROR.equals(siniestrosManager.generaContraRecibo(paramsO))){
				msgResult = "Error al generar el n\u00fa de Contra Recibo";
				success =  false;
				return SUCCESS;
			}
			File carpeta=new File(rutaDocumentosPoliza + "/" + paramsO.get("pv_ntramite_i"));
			if(!carpeta.exists()){
				logger.debug("no existe la carpeta::: {}", paramsO.get("pv_ntramite_i"));
				carpeta.mkdir();
				if(carpeta.exists()){
					logger.debug("carpeta creada");
				} else {
					logger.debug("carpeta NO creada");
				}
			} else {
				logger.debug("existe la carpeta   ::: {}", paramsO.get("pv_ntramite_i"));
			}
			UserVO usuario=(UserVO)session.get("USUARIO");
	
			String nombreArchivoReporte= null;
			if (Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals((String)paramsO.get("pv_cdramo_i"))) {
				nombreArchivoReporte = getText("rdf.siniestro.contrarecibo.nombreGNP");
			}else{
				nombreArchivoReporte= getText("rdf.siniestro.contrarecibo.nombre");
			}
			
			logger.debug("Alberto ==> "+nombreArchivoReporte);
			String urlContrareciboSiniestro = ""
					+ rutaServidorReports
					+ "?p_usuario=" + usuario.getUser() 
					+ "&p_TRAMITE=" + paramsO.get("pv_ntramite_i")
					+ "&destype=cache"
					+ "&desformat=PDF"
					+ "&userid="+passServidorReports
					+ "&ACCESSIBLE=YES"
					+ "&report="+nombreArchivoReporte
					+ "&paramform=no"
					;
					String nombreArchivo = getText("siniestro.contrarecibo.nombre");
					String pathArchivo=""
					+ rutaDocumentosPoliza
					+ "/" + paramsO.get("pv_ntramite_i")
					+ "/" + nombreArchivo
			;
			HttpUtil.generaArchivo(urlContrareciboSiniestro, pathArchivo);
	
			paramsO.put("pv_feinici_i"  , new Date());
			paramsO.put("pv_cddocume_i" , nombreArchivo);
			paramsO.put("pv_dsdocume_i" , "Contra Recibo");
			paramsO.put("pv_swvisible_i"   , null);
			paramsO.put("pv_codidocu_i"   , null);
			paramsO.put("pv_cdtiptra_i"   , TipoTramite.SINIESTRO.getCdtiptra());
			//kernelManagerSustituto.guardarArchivo(paramsO);
			
			documentosManager.guardarDocumento(
					(String)paramsO.get("pv_cdunieco_i")
					,(String)paramsO.get("pv_cdramo_i")
					,(String)paramsO.get("pv_estado_i")
					,(String)paramsO.get("pv_nmpoliza_i")
					,(String)paramsO.get("pv_nmsuplem_i")
					,new Date()
					,nombreArchivo
					,"Contra Recibo"
					,(String)paramsO.get("pv_nmsolici_i")
					,(String)paramsO.get("pv_ntramite_i")
					,(String)paramsO.get("pv_tipmov_i")
					,null
					,null
					,TipoTramite.SINIESTRO.getCdtiptra()
					,null
					,null
					,null
					,null, false
					);
			
			if (Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo().equals((String)paramsO.get("pv_cdramo_i"))) {
                HttpUtil.enviarArchivoRSTN(
                        HttpUtil.RSTN_SINIESTROS_PATH + (String)paramsO.get("caseIdRstn"),
                        pathArchivo, 
                        "Contra Recibo",
                        HttpUtil.RSTN_DOC_CLASS_SINIESTROS);
            }
			
		}catch( Exception e){
			logger.error("Error en generarContrarecibo : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para generar el siniestro unicamente por el numero de tramite
	* @param params
	* @return Success - Exito si se genera el numero de tramite
	*/
	public String generaSiniestroTramite(){
		logger.debug("Entra a generaSiniestroTramite Params: {}", params);
		try {
			siniestrosManager.getAltaSiniestroAltaTramite(params.get("pv_ntramite_i"));
		}catch( Exception e){
			logger.error("Error en generaSiniestroTramite : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para validar el cdtipsit del tramite 
	* @param ntramite
	* @return Success - Exito
	*/
	public String validaCdTipsitTramite(){
		logger.debug("Entra a validaCdTipsitTramite Params: {}", params);
		try {
			HashMap<String, Object> paramTramite= new HashMap<String, Object>();
			paramTramite.put("pv_ntramite_i",params.get("ntramite"));
			validaCdTipsitTramite = siniestrosManager.validaCdTipsitAltaTramite(paramTramite);
		}catch( Exception e){
			logger.error("Error al obtener el cdtipsit : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion para la visualizacion de la autorizacion de servicio 
	* @return params con los valores para hacer las consultas
	*/
	public String altaFacturasProceso(){
		logger.debug("Entra a ltaFacturasProceso");
		try {
			logger.debug("Params: {}", params);
		}catch( Exception e){
			logger.error("Error altaFacturasProceso {}", e.getMessage(), e);
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

	public void setParamsJson(HashMap<String, String> params2) {
		this.params = params2;
	}

	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params {}", e.getMessage(), e);
			return null;
		}
	}
	
	public List<AltaTramiteVO> getListaAltaTramite() {
		return listaAltaTramite;
	}

	public void setListaAltaTramite(List<AltaTramiteVO> listaAltaTramite) {
		this.listaAltaTramite = listaAltaTramite;
	}
	
	public List<HashMap<String, String>> getDatosTablas() {
		return datosTablas;
	}

	public void setDatosTablas(List<HashMap<String, String>> datosTablas) {
		this.datosTablas = datosTablas;
	}
	
	public Date getDate(String date){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(date);
		} catch (ParseException ex) {
		}
		return null;
	}
	
	public String getMsgResult() {
		return msgResult;
	}

	public void setMsgResult(String msgResult) {
		this.msgResult = msgResult;
	}
	
	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getFactPagada() {
		return factPagada;
	}

	public void setFactPagada(String factPagada) {
		this.factPagada = factPagada;
	}
	
	public Map<String, String> getSmap() {
		return smap;
	}

	public void setSmap(Map<String, String> smap) {
		this.smap = smap;
	}
	
	public List<HashMap<String, String>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<HashMap<String, String>> loadList) {
		this.loadList = loadList;
	}
	
	public HashMap<String, Object> getParamsO() {
		return paramsO;
	}

	public void setParamsO(HashMap<String, Object> paramsO) {
		this.paramsO = paramsO;
	}
	
	public String getValidaCdTipsitTramite() {
		return validaCdTipsitTramite;
	}

	public void setValidaCdTipsitTramite(String validaCdTipsitTramite) {
		this.validaCdTipsitTramite = validaCdTipsitTramite;
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