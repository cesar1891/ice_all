package mx.com.gseguros.portal.siniestros.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosGeneralesPolizaVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.service.ConsultasAseguradoManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.model.SolicitudCxPVO;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.CausaSiniestro;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoPago;
import mx.com.gseguros.portal.general.util.TipoPrestadorServicio;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.model.AutorizacionServicioVO;
import mx.com.gseguros.portal.siniestros.model.CoberturaPolizaVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaProveedorVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTTAPVAATVO;
import mx.com.gseguros.portal.siniestros.model.ListaFacturasVO;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.model.PolizaVigenteVO;
import mx.com.gseguros.portal.siniestros.model.SiniestroVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Operacion;

public class SiniestrosAction extends PrincipalCoreAction {
	
	private static final String IMPORTE_WS_IMPORTE = "importe";
	private static final String IMPORTE_WS_IVA     = "iva";
	private static final String IMPORTE_WS_IVR     = "ivr";
	private static final String IMPORTE_WS_ISR     = "isr";
	private static final String IMPORTE_WS_CEDULAR = "cedular";
	private static final long serialVersionUID = -6321288906841302337L;
	static final Logger logger = LoggerFactory.getLogger(SiniestrosAction.class);
	private DateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	private boolean success;
	private SiniestrosManager siniestrosManager;
	private KernelManagerSustituto kernelManagerSustituto;
	private transient CatalogosManager catalogosManager;
	private PantallasManager       pantallasManager;
	private transient Ice2sigsService ice2sigsService;
	private HashMap<String,String> params;
	private HashMap<String,Object> paramsO;
	
	private List<GenericVO> listaAsegurado;
	private List<GenericVO> listaCausaSiniestro;
	private List<CoberturaPolizaVO> listaCoberturaPoliza;
	private List<MesaControlVO> listaMesaControl;
	private List<GenericVO> listaSubcobertura;
	private List<GenericVO> listaCPTICD;
	private List<ConsultaTTAPVAATVO> listaConsultaTTAPVAATV;
	private List<HashMap<String,String>> datosTablas;
	private List<PolizaVigenteVO> listaPoliza;
	private List<PolizaVigenteVO> polizaUnica;
	private List<SolicitudCxPVO> solicitudPago;
	private String msgResult;
	
	private String existePenalizacion;
	private String validacionGeneral;
	private String montoArancel;
	private String usuarioTurnadoSiniestro;
	private String porcentajePenalizacion;
	private List<HashMap<String, String>> loadList;
	private List<HashMap<String, String>> saveList;
	private List<GenericVO> listaTipoAtencion;
	private List<GenericVO> listadoRamosSalud;
	private List<GenericVO> datosValidacionGral;
	private List<ListaFacturasVO> listaFacturas;
	
	private Item                     item;
	private Map<String,Item>         imap;
	private String                   mensaje;
	
	private List<Map<String,String>> slist1;
	private List<Map<String,String>> slist2;
	private List<Map<String,String>> slist3;
	private Map<String,String>       smap;
	private Map<String,String>       smap2;
	private List<Map<String, Object>> facturasxSiniestro;
	private List<Map<String, Object>> aseguradosxSiniestro;
	private List<Map<String, String>> conceptosxSiniestro;
	private Map<String,String>       smap3;
	private List<Map<String,String>> lhosp;
	private List<Map<String,String>> lpdir;
	private List<Map<String,String>> lprem;
	private List<Map<String,String>> listaImportesWS;
	private List<Map<String,String>> facturasxTramite;
	
	private List<Map<String,String>> datosPenalizacion;
	private List<Map<String,String>> datosCoberturaxCal;
	private List<List<Map<String,String>>> llist1;
	private Map<String, String> map1;
	private List<Map<String,String>>  datosInformacionAdicional;
	private List<Map<String,String>>  datosValidacion;
	private Map<String,List<Map<String,String>>> graficas;
	
	private boolean     exito            = false;
	private String      respuesta;
	private String      respuestaOculta  = null;
	
	@Autowired
	@Qualifier("consultasAseguradoManagerImpl")
	private ConsultasAseguradoManager consultasAseguradoManager;
	
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
	* metodo para consultar la poliza en especifico
	* @param unieco, Ramo, Estado, Nmpoliza, cdperson
	* @return PolizaVigenteVO - Informacion de Poliza en especifico
	*/
	public String consultaPolizaUnica(){
		logger.debug("Entra a consultaPolizaUnica params : {}",params);
		try {
			HashMap<String, Object> paramPolUnica = new HashMap<String, Object>();
			paramPolUnica.put("pv_cdunieco_i",params.get("cdunieco"));
			paramPolUnica.put("pv_cdramo_i",params.get("cdramo"));
			paramPolUnica.put("pv_estado_i",params.get("estado"));
			paramPolUnica.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramPolUnica.put("pv_cdperson_i",params.get("cdperson"));
			List<PolizaVigenteVO> polUnica = siniestrosManager.getConsultaPolizaUnica(paramPolUnica);
			if(polUnica!=null && !polUnica.isEmpty())	polizaUnica = polUnica;
		}catch( Exception e){
			logger.error("Error al obtener los datos de la poliza unica : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* metodo para generar un complemento
	* @param ntramiteAnterior
	* @return Nuevo tramite
	*/
	public String solicitarComplemento(){
		logger.debug("Entra a SolicitarComplemento params : {}",params);
		try{
			List<MesaControlVO> listaMesaControl = siniestrosManager.getConsultaListaMesaControl(params.get("pv_ntramite_i"));
			logger.debug("Total Registros MC : {}", listaMesaControl.size());
			// 1.- Guardar en TMESACONTROL 
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			Map<String,String> valores = new LinkedHashMap<String,String>();
			valores.put("otvalor01" , listaMesaControl.get(0).getOtvalor01mc());
			valores.put("otvalor02" , listaMesaControl.get(0).getOtvalor02mc());
			valores.put("otvalor03" , listaMesaControl.get(0).getOtvalor03mc());
			valores.put("otvalor04" , listaMesaControl.get(0).getOtvalor04mc());
			valores.put("otvalor05" , listaMesaControl.get(0).getOtvalor05mc());
			valores.put("otvalor06" , listaMesaControl.get(0).getOtvalor06mc());
			valores.put("otvalor07" , listaMesaControl.get(0).getOtvalor07mc());
			valores.put("otvalor08" , listaMesaControl.get(0).getOtvalor08mc());
			valores.put("otvalor09" , listaMesaControl.get(0).getOtvalor09mc());
			valores.put("otvalor10" , listaMesaControl.get(0).getOtvalor10mc());
			valores.put("otvalor11" , listaMesaControl.get(0).getOtvalor11mc());
			valores.put("otvalor12" , listaMesaControl.get(0).getOtvalor12mc());
			valores.put("otvalor13" , listaMesaControl.get(0).getOtvalor13mc());
			valores.put("otvalor14" , listaMesaControl.get(0).getOtvalor14mc());
			valores.put("otvalor15" , listaMesaControl.get(0).getOtvalor15mc());
			valores.put("otvalor16" , listaMesaControl.get(0).getOtvalor16mc());
			valores.put("otvalor17" , listaMesaControl.get(0).getOtvalor17mc());
			valores.put("otvalor18" , listaMesaControl.get(0).getOtvalor18mc());
			valores.put("otvalor19" , listaMesaControl.get(0).getOtvalor19mc());
			valores.put("otvalor20" , listaMesaControl.get(0).getOtvalor20mc());
			valores.put("otvalor21" , listaMesaControl.get(0).getOtvalor21mc());
			valores.put("otvalor22" , listaMesaControl.get(0).getOtvalor22mc());
			valores.put("otvalor23" , listaMesaControl.get(0).getOtvalor23mc());
			valores.put("otvalor24" , listaMesaControl.get(0).getOtvalor24mc());
			valores.put("otvalor25" , listaMesaControl.get(0).getOtvalor25mc());
			valores.put("otvalor26" , listaMesaControl.get(0).getOtvalor26mc());
			valores.put("otvalor27" , listaMesaControl.get(0).getOtvalor27mc());
			valores.put("otvalor28" , listaMesaControl.get(0).getOtvalor28mc());
			valores.put("otvalor29" , listaMesaControl.get(0).getOtvalor29mc());
			valores.put("otvalor30" , listaMesaControl.get(0).getOtvalor30mc());
			valores.put("otvalor31" , listaMesaControl.get(0).getOtvalor31mc());
			valores.put("otvalor32" , listaMesaControl.get(0).getOtvalor32mc());
			valores.put("otvalor33" , listaMesaControl.get(0).getOtvalor33mc());
			valores.put("otvalor34" , listaMesaControl.get(0).getOtvalor34mc());
			valores.put("otvalor35" , listaMesaControl.get(0).getOtvalor35mc());
			valores.put("otvalor36" , listaMesaControl.get(0).getOtvalor36mc());
			valores.put("otvalor37" , listaMesaControl.get(0).getOtvalor37mc());
			valores.put("otvalor38" , listaMesaControl.get(0).getOtvalor38mc());
			valores.put("otvalor39" , listaMesaControl.get(0).getOtvalor39mc());
			valores.put("otvalor40" , listaMesaControl.get(0).getOtvalor40mc());
			valores.put("otvalor41" , listaMesaControl.get(0).getOtvalor41mc());
			valores.put("otvalor42" , listaMesaControl.get(0).getOtvalor42mc());
			valores.put("otvalor43" , listaMesaControl.get(0).getOtvalor43mc());
			valores.put("otvalor44" , listaMesaControl.get(0).getOtvalor44mc());
			valores.put("otvalor45" , listaMesaControl.get(0).getOtvalor45mc());
			valores.put("otvalor46" , listaMesaControl.get(0).getOtvalor46mc());
			valores.put("otvalor47" , listaMesaControl.get(0).getOtvalor47mc());
			valores.put("otvalor48" , listaMesaControl.get(0).getOtvalor48mc());
			valores.put("otvalor49" , listaMesaControl.get(0).getOtvalor49mc());
			valores.put("otvalor50" , listaMesaControl.get(0).getOtvalor50mc());
			
			String ntramiteGenerado = mesaControlManager.movimientoTramite(
					listaMesaControl.get(0).getCduniecomc()
					,listaMesaControl.get(0).getCdramomc()
					,listaMesaControl.get(0).getEstadomc()
					,listaMesaControl.get(0).getNmpolizamc()
					,listaMesaControl.get(0).getNmsuplemmc()
					,listaMesaControl.get(0).getCdsucadmmc()
					,listaMesaControl.get(0).getCdsucdocmc()
					,listaMesaControl.get(0).getCdtiptramc()
					,getDate(listaMesaControl.get(0).getFerecepcmc())
					,listaMesaControl.get(0).getCdagentemc()
					,listaMesaControl.get(0).getReferenciamc()
					,listaMesaControl.get(0).getNombremc()
					,getDate(listaMesaControl.get(0).getFecstatumc())
					,EstatusTramite.EN_CAPTURA.getCodigo()
					,listaMesaControl.get(0).getCommentsmc()
					,listaMesaControl.get(0).getNmsolicimc()
					,listaMesaControl.get(0).getCdtipsitmc()
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
				logger.debug("valor de nuevo tramite : {}",msgResult);
				//2.- Obtenemos los valores de las facturas anteriores y lo guardamos a TFACMESCTRL
				List<Map<String,String>> facturas = siniestrosManager.obtenerFacturasTramite(params.get("pv_ntramite_i"));
				
				for(int i=0; i< facturas.size();i++){
					String fechaEgreso;
					logger.debug("Valor de Fecha Egreso :{}",facturas.get(i).get("FEEGRESO").toString());
					if(facturas.get(i).get("FEEGRESO").toString().length() > 0 && (!facturas.get(i).get("FEEGRESO").toString().equalsIgnoreCase("null"))){
						fechaEgreso = facturas.get(i).get("FEEGRESO");
					}else{
						fechaEgreso = facturas.get(i).get("FFACTURA");
					}
					siniestrosManager.guardaListaFacturaSiniestro(
						msgResult,							facturas.get(i).get("NFACTURA"),		renderFechas.parse(facturas.get(i).get("FFACTURA")),
						facturas.get(i).get("CDTIPSER"),	facturas.get(i).get("CDPRESTA"),		facturas.get(i).get("PTIMPORT"),
						facturas.get(i).get("CDGARANT"),	facturas.get(i).get("CDCONVAL"),		facturas.get(i).get("DESCPORC"),
						facturas.get(i).get("DESCNUME"),	facturas.get(i).get("CDMONEDA"),		facturas.get(i).get("TASACAMB"),
						facturas.get(i).get("PTIMPORTA"),	facturas.get(i).get("DCTONUEX"),		renderFechas.parse(fechaEgreso),
						facturas.get(i).get("DIASDEDU"),	facturas.get(i).get("NOMBPROV"),		null, null
					);
				}
				
				//3.- Obtenemos los valores de MSINIEST y MSINIPER para llenar la tabla de TWORKSIN
				List<Map<String,String>> msiniper = siniestrosManager.obtenerDatoMsiniper(params.get("pv_ntramite_i"));
				
				for(int i=0;i<msiniper.size();i++) {
					HashMap<String, Object> paramsTworkSin = new HashMap<String, Object>();
					paramsTworkSin.put("pv_nmtramite_i",msgResult);												paramsTworkSin.put("pv_cdunieco_i",	msiniper.get(i).get("CDUNIECO"));
					paramsTworkSin.put("pv_cdramo_i",	msiniper.get(i).get("CDRAMO"));							paramsTworkSin.put("pv_estado_i",	msiniper.get(i).get("ESTADO"));
					paramsTworkSin.put("pv_nmpoliza_i",	msiniper.get(i).get("NMPOLIZA"));						paramsTworkSin.put("pv_nmsolici_i",	msiniper.get(i).get("NMSOLICI"));
					paramsTworkSin.put("pv_nmsuplem_i",	msiniper.get(i).get("NMSUPLEM"));						paramsTworkSin.put("pv_nmsituac_i",	msiniper.get(i).get("NMSITUAC"));
					paramsTworkSin.put("pv_cdtipsit_i",	msiniper.get(i).get("CDTIPSIT"));						paramsTworkSin.put("pv_cdperson_i",	msiniper.get(i).get("CDPERSON"));
					paramsTworkSin.put("pv_feocurre_i",	renderFechas.parse(msiniper.get(i).get("FEOCURRE")));	paramsTworkSin.put("pv_nmautser_i",	msiniper.get(i).get("NMAUTSER"));
					paramsTworkSin.put("pv_nfactura_i",	msiniper.get(i).get("NFACTURA"));						paramsTworkSin.put("pv_reqautes_i",	msiniper.get(i).get("REQAUTES"));
					paramsTworkSin.put("pv_nmordina_i", msiniper.get(i).get("NMORDINA"));
					siniestrosManager.guardaListaTworkSin(paramsTworkSin);
				}
				
				logger.debug("Tipo de Pago : {} ",listaMesaControl.get(0).getOtvalor02mc());
				
				if(TipoPago.DIRECTO.getCodigo().equals(listaMesaControl.get(0).getOtvalor02mc())){
					//PAGO DIRECTO
					for(int i=0; i< facturas.size();i++){
						List<Map<String,String>> siniesxfactura = siniestrosManager.listaSiniestrosTramite2(params.get("pv_ntramite_i"),facturas.get(i).get("NFACTURA"));
						
						for(int a=0; a< siniesxfactura.size();a++){
							String nmsini = siniesxfactura.get(a).get("NMSINIES") + ""; //(EGS)
							if(!nmsini.equalsIgnoreCase("null")){ //(EGS)
								siniestrosManager.getAltaSiniestroSinAutorizacion(msgResult,siniesxfactura.get(a).get("CDUNIECO"),siniesxfactura.get(a).get("CDRAMO"),
									siniesxfactura.get(a).get("ESTADO"),siniesxfactura.get(a).get("NMPOLIZA"),siniesxfactura.get(a).get("NMSUPLEM"),siniesxfactura.get(a).get("NMSITUAC"),
									siniesxfactura.get(a).get("CDTIPSIT"),renderFechas.parse(siniesxfactura.get(a).get("FEOCURRE")),facturas.get(i).get("NFACTURA"), siniesxfactura.get(a).get("SECTWORKSIN"));
							} //(EGS)
						}
					}
					
					for(int ii=0; ii< facturas.size();ii++){
						List<Map<String,String>> siniestrosAnterior = siniestrosManager.listaSiniestrosMsiniesTramite(params.get("pv_ntramite_i"),facturas.get(ii).get("NFACTURA"),null);
						logger.debug("Pago Directo siniestros Anterior ==> :{}",siniestrosAnterior);
						List<Map<String,String>> siniestrosNuevo = siniestrosManager.listaSiniestrosMsiniesTramite(msgResult,facturas.get(ii).get("NFACTURA"),null);
						logger.debug("Pago Directo siniestros Nuevo ==> :{}",siniestrosNuevo);
						
						for(int r= 0; r< siniestrosAnterior.size(); r++)
						{
							for(int q = 0 ; q < siniestrosNuevo.size(); q++){
								if(siniestrosAnterior.get(r).get("CDUNIECO").equalsIgnoreCase(siniestrosNuevo.get(q).get("CDUNIECO")) &&
										siniestrosAnterior.get(r).get("CDRAMO").equalsIgnoreCase(siniestrosNuevo.get(q).get("CDRAMO")) &&
										siniestrosAnterior.get(r).get("ESTADO").equalsIgnoreCase(siniestrosNuevo.get(q).get("ESTADO")) &&
										siniestrosAnterior.get(r).get("NMPOLIZA").equalsIgnoreCase(siniestrosNuevo.get(q).get("NMPOLIZA")) &&
										siniestrosAnterior.get(r).get("NMSITUAC").equalsIgnoreCase(siniestrosNuevo.get(q).get("NMSITUAC")) &&
										siniestrosAnterior.get(r).get("CDTIPSIT").equalsIgnoreCase(siniestrosNuevo.get(q).get("CDTIPSIT")) &&
										siniestrosAnterior.get(r).get("CDPERSON").equalsIgnoreCase(siniestrosNuevo.get(q).get("CDPERSON")) &&
										siniestrosAnterior.get(r).get("FEOCURRE").equalsIgnoreCase(siniestrosNuevo.get(q).get("FEOCURRE")) &&
										siniestrosAnterior.get(r).get("CDUNIECO").equalsIgnoreCase(siniestrosNuevo.get(q).get("CDUNIECO")) &&
										siniestrosAnterior.get(r).get("SECTWORKSIN").equalsIgnoreCase(siniestrosNuevo.get(q).get("SECTWORKSIN")) &&
										siniestrosAnterior.get(r).get("STATUS").equalsIgnoreCase(siniestrosNuevo.get(q).get("STATUS"))
										
								){
									//5.- Actualizamos los valores de MSINIEST
									siniestrosManager.actualizaDatosGeneralesSiniestro(
											siniestrosNuevo.get(q).get("CDUNIECO"),siniestrosNuevo.get(q).get("CDRAMO"),
											siniestrosNuevo.get(q).get("ESTADO"),siniestrosNuevo.get(q).get("NMPOLIZA"),
											siniestrosNuevo.get(q).get("NMSUPLEM"),siniestrosNuevo.get(q).get("AAAPERTU"),
											siniestrosNuevo.get(q).get("NMSINIES"),renderFechas.parse(siniestrosNuevo.get(q).get("FEOCURRE")),
											siniestrosAnterior.get(r).get("NMSINREF"),siniestrosAnterior.get(r).get("CDICD"),
											siniestrosAnterior.get(r).get("CDICD2"),  siniestrosAnterior.get(r).get("CDCAUSA"),
											siniestrosAnterior.get(r).get("CDGARANT"),siniestrosAnterior.get(r).get("CDCONVAL"),
											siniestrosAnterior.get(r).get("NMAUTSER"),siniestrosAnterior.get(r).get("CDPERSON"),"1",
											siniestrosAnterior.get(r).get("NMRECLAMO"),siniestrosAnterior.get(r).get("SWFONSIN"));
									
									List<Map<String,String>>lista = siniestrosManager.P_GET_MSINIVAL(
											siniestrosAnterior.get(r).get("CDUNIECO"), siniestrosAnterior.get(r).get("CDRAMO"), 
											siniestrosAnterior.get(r).get("ESTADO"),siniestrosAnterior.get(r).get("NMPOLIZA"), 
											siniestrosAnterior.get(r).get("NMSUPLEM"), siniestrosAnterior.get(r).get("NMSITUAC"),
											siniestrosAnterior.get(r).get("AAAPERTU"), siniestrosAnterior.get(r).get("STATUS"), 
											siniestrosAnterior.get(r).get("NMSINIES"), facturas.get(ii).get("NFACTURA"));
									
									//Actualizamos la informacion de TCOPASIN
									siniestrosManager.actualizaDatosGeneralesCopago(
											siniestrosNuevo.get(q).get("CDUNIECO"), siniestrosNuevo.get(q).get("CDRAMO"),
											siniestrosNuevo.get(q).get("ESTADO"),   siniestrosNuevo.get(q).get("NMPOLIZA"),
											siniestrosNuevo.get(q).get("NMSUPLEM"), siniestrosNuevo.get(r).get("NMSITUAC"),
											siniestrosNuevo.get(q).get("NMSINIES"), msgResult,
											facturas.get(ii).get("NFACTURA"),       siniestrosAnterior.get(r).get("CDGARANT"),
											siniestrosAnterior.get(r).get("CDCONVAL"), null,
											null, null,null, 
											siniestrosAnterior.get(r).get("FEINGRESO"),siniestrosAnterior.get(r).get("FEEGRESO"),
											siniestrosAnterior.get(r).get("CDTIPEVE"),siniestrosAnterior.get(r).get("CDTIPALT"),
											Constantes.INSERT_MODE);									
									loadList = new ArrayList<HashMap<String,String>>();
									for(Map<String,String>map:lista) {
										loadList.add((HashMap<String,String>)map);
									}
										
									for(int j=0;j<loadList.size();j++){
										Date   dFemovimi = new Date();
										Date   dFeregist = new Date();
										String nmanno    = Calendar.getInstance().get(Calendar.YEAR)+"";
										//6.- Se realiza el guardado en MSINIVAL
										siniestrosManager.P_MOV_MSINIVAL(
											loadList.get(j).get("CDUNIECO"),		loadList.get(j).get("CDRAMO"),			loadList.get(j).get("ESTADO"),				loadList.get(j).get("NMPOLIZA"),
											loadList.get(j).get("NMSUPLEM"),		loadList.get(j).get("NMSITUAC"),		siniestrosNuevo.get(q).get("AAAPERTU"),		siniestrosNuevo.get(q).get("STATUS"),
											siniestrosNuevo.get(q).get("NMSINIES"),	loadList.get(j).get("NFACTURA"),		loadList.get(j).get("CDGARANT"),			loadList.get(j).get("CDCONVAL"),
											loadList.get(j).get("CDCONCEP"),		loadList.get(j).get("IDCONCEP"),		loadList.get(j).get("CDCAPITA"),			loadList.get(j).get("NMORDINA"),
											dFemovimi,								loadList.get(j).get("CDMONEDA"),		loadList.get(j).get("PTPRECIO"),			loadList.get(j).get("CANTIDAD"),
											loadList.get(j).get("DESTOPOR"),		loadList.get(j).get("DESTOIMP"),		loadList.get(j).get("PTIMPORT"),			loadList.get(j).get("PTRECOBR"),
											nmanno,									loadList.get(j).get("NMAPUNTE"),		loadList.get(j).get("USERREGI"),			dFeregist,
											"I",									loadList.get(j).get("PTIMPOEX"),		loadList.get(j).get("DCTOIMEX"),			loadList.get(j).get("PTIMPOEX"),	
											loadList.get(j).get("PTMTOARA"),		loadList.get(j).get("APLICIVA"));
										
										//7.- Obtenemos los valores de TDSINIVAL para ver si tiene Ajuste Medico
										List<Map<String,String>> ajusteMedico = siniestrosManager.P_GET_TDSINIVAL(
												loadList.get(j).get("CDUNIECO"),		loadList.get(j).get("CDRAMO"),			loadList.get(j).get("ESTADO"),				loadList.get(j).get("NMPOLIZA"),
												loadList.get(j).get("NMSUPLEM"),		loadList.get(j).get("NMSITUAC"),		loadList.get(j).get("AAAPERTU"),			loadList.get(j).get("STATUS"),
												loadList.get(j).get("NMSINIES"),		loadList.get(j).get("NFACTURA"),		loadList.get(j).get("CDGARANT"),			loadList.get(j).get("CDCONVAL"),
												loadList.get(j).get("CDCONCEP"),		loadList.get(j).get("IDCONCEP"),		loadList.get(j).get("NMORDINA"));
										
										for(int t=0;t<ajusteMedico.size();t++){
											String userregi  = usuario.getUser();
											Date   fechaRegistro = new Date();
											//8.- Guaramos la informacion de del Ajuste Medico
											siniestrosManager.P_MOV_TDSINIVAL(
													loadList.get(j).get("CDUNIECO"), loadList.get(j).get("CDRAMO"), loadList.get(j).get("ESTADO"), loadList.get(j).get("NMPOLIZA"),
													loadList.get(j).get("NMSUPLEM"), loadList.get(j).get("NMSITUAC"), siniestrosNuevo.get(q).get("AAAPERTU"), siniestrosNuevo.get(q).get("STATUS"),
													siniestrosNuevo.get(q).get("NMSINIES"), loadList.get(j).get("NFACTURA"),loadList.get(j).get("CDGARANT"), loadList.get(j).get("CDCONVAL"),
													loadList.get(j).get("CDCONCEP"), loadList.get(j).get("IDCONCEP"),loadList.get(j).get("NMORDINA"),loadList.get(j).get("NMORDMOV"),
													ajusteMedico.get(t).get("PTIMPORT"), ajusteMedico.get(t).get("COMMENTS"), userregi, fechaRegistro,
													Constantes.INSERT_MODE);
										}
									}
									
									//9.- Obtenemos los valores para realizar el guardado de TVALOSIN
									List<Map<String, String>> validacionFacturas = siniestrosManager.P_GET_FACTURAS_SINIESTRO(siniestrosAnterior.get(r).get("CDUNIECO"), siniestrosAnterior.get(r).get("CDRAMO"),
											siniestrosAnterior.get(r).get("ESTADO"),siniestrosAnterior.get(r).get("NMPOLIZA"),siniestrosAnterior.get(r).get("NMSUPLEM"), siniestrosAnterior.get(r).get("NMSITUAC"),
											siniestrosAnterior.get(r).get("AAAPERTU"), siniestrosAnterior.get(r).get("STATUS"),siniestrosAnterior.get(r).get("NMSINIES"), siniestrosAnterior.get(r).get("CDTIPSIT"));
									for(int k=0; k < validacionFacturas.size();k++){
										//10.- Obtenemos la informacion de Siniestro por tramite
										List<Map<String,String>> asegurados = siniestrosManager.listaSiniestrosTramite2(msgResult, validacionFacturas.get(k).get("NFACTURA"));
										
										for(int h =0; h < asegurados.size();h++){
											String munSiniestro=asegurados.get(h).get("NMSINIES")+"";
											if(!munSiniestro.equalsIgnoreCase("null")){
												//11.- Guardamos la informacion en TVALOSIN
												Map<String,Object>paramsTvalosin = new HashMap<String,Object>();
												paramsTvalosin.put("pv_cdunieco"  , asegurados.get(h).get("CDUNIECO"));
												paramsTvalosin.put("pv_cdramo"    , asegurados.get(h).get("CDRAMO"));
												paramsTvalosin.put("pv_aaapertu"  , asegurados.get(h).get("AAAPERTU"));
												paramsTvalosin.put("pv_status"    , asegurados.get(h).get("STATUS"));
												paramsTvalosin.put("pv_nmsinies"  , asegurados.get(h).get("NMSINIES"));
												paramsTvalosin.put("pv_cdtipsit"  , asegurados.get(h).get("CDTIPSIT"));
												paramsTvalosin.put("pv_nmsuplem"  , asegurados.get(h).get("NMSUPLEM"));
												paramsTvalosin.put("pv_cdusuari"  , null);
												paramsTvalosin.put("pv_feregist"  , null);
												paramsTvalosin.put("pv_otvalor01" , validacionFacturas.get(k).get("APLICA_IVA"));
												paramsTvalosin.put("pv_otvalor02" , validacionFacturas.get(k).get("ANTES_DESPUES"));
												paramsTvalosin.put("pv_otvalor03" , validacionFacturas.get(k).get("IVARETENIDO"));
												paramsTvalosin.put("pv_otvalor04" , msgResult);
												paramsTvalosin.put("pv_otvalor05" , validacionFacturas.get(k).get("NFACTURA"));
												paramsTvalosin.put("pv_accion_i"  , "I");
												kernelManagerSustituto.PMovTvalosin(paramsTvalosin);
												
												//11.- Guardamos la informacion en MAUTSINI
												siniestrosManager.P_MOV_MAUTSINI(asegurados.get(h).get("CDUNIECO"), asegurados.get(h).get("CDRAMO"), asegurados.get(h).get("ESTADO"), 
													asegurados.get(h).get("NMPOLIZA"), asegurados.get(h).get("NMSUPLEM"), asegurados.get(h).get("NMSITUAC"), asegurados.get(h).get("AAAPERTU"), 
													asegurados.get(h).get("STATUS"), asegurados.get(h).get("NMSINIES"),validacionFacturas.get(k).get("NFACTURA"),
													null,null,null,null,null,
													Constantes.MAUTSINI_AREA_RECLAMACIONES,validacionFacturas.get(k).get("AUTRECLA"), Constantes.MAUTSINI_FACTURA, validacionFacturas.get(k).get("COMMENAR"), Constantes.INSERT_MODE);
												
												siniestrosManager.P_MOV_MAUTSINI(asegurados.get(h).get("CDUNIECO"), asegurados.get(h).get("CDRAMO"), asegurados.get(h).get("ESTADO"), 
													asegurados.get(h).get("NMPOLIZA"), asegurados.get(h).get("NMSUPLEM"), asegurados.get(h).get("NMSITUAC"), asegurados.get(h).get("AAAPERTU"), 
													asegurados.get(h).get("STATUS"), asegurados.get(h).get("NMSINIES"), validacionFacturas.get(k).get("NFACTURA"),
													null,null,null,null,null,
													Constantes.MAUTSINI_AREA_MEDICA, validacionFacturas.get(k).get("AUTMEDIC"), Constantes.MAUTSINI_FACTURA, validacionFacturas.get(k).get("COMMENME"), Constantes.INSERT_MODE);
											}
										}	
									}
								}
							}
						}
					}
					
				}else{
					//3.- Se gnera el nuevo Siniestro de dicho tramite.
					siniestrosManager.getAltaSiniestroAltaTramite(msgResult);
					
					//4.- Obtenemos los valores del siniestro Anterior
					List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(params.get("pv_ntramite_i"),null,null);
					logger.debug("Pago Reembolso  siniestros Anterior ==> :{}",siniestros);
					List<Map<String,String>> siniestroNuevo = siniestrosManager.listaSiniestrosMsiniesTramite(msgResult,null,null);
					logger.debug("Pago Reembolso  siniestros Nuevo ==> :{}",siniestroNuevo);
					//5.- Actualizamos los valores de MSINIEST
					siniestrosManager.actualizaDatosGeneralesSiniestro(
							siniestroNuevo.get(0).get("CDUNIECO"),siniestroNuevo.get(0).get("CDRAMO"),
							siniestroNuevo.get(0).get("ESTADO"),siniestroNuevo.get(0).get("NMPOLIZA"),
							siniestroNuevo.get(0).get("NMSUPLEM"),siniestroNuevo.get(0).get("AAAPERTU"),
							siniestroNuevo.get(0).get("NMSINIES"),renderFechas.parse(siniestroNuevo.get(0).get("FEOCURRE")),
							siniestros.get(0).get("NMSINREF"),siniestros.get(0).get("CDICD"),
							siniestros.get(0).get("CDICD2"),siniestros.get(0).get("CDCAUSA"),
							siniestros.get(0).get("CDGARANT"),siniestros.get(0).get("CDCONVAL"),
							siniestros.get(0).get("NMAUTSER"),siniestroNuevo.get(0).get("CDPERSON"),"1",
							siniestroNuevo.get(0).get("NMRECLAMO"), siniestros.get(0).get("SWFONSIN"));
					
					for(int i=0; i< facturas.size();i++){
						//6.- Obtenemos los valores de MSINIVAL por facturas
						List<Map<String,String>>lista = siniestrosManager.P_GET_MSINIVAL(
							msiniper.get(0).get("CDUNIECO"), msiniper.get(0).get("CDRAMO"), 
							msiniper.get(0).get("ESTADO"),msiniper.get(0).get("NMPOLIZA"), 
							msiniper.get(0).get("NMSUPLEM"), msiniper.get(0).get("NMSITUAC"),
							siniestros.get(0).get("AAAPERTU"), siniestros.get(0).get("STATUS"), 
							siniestros.get(0).get("NMSINIES"), facturas.get(i).get("NFACTURA"));
						
						
						//Actualizamos la informacion de TCOPASIN
						siniestrosManager.actualizaDatosGeneralesCopago(
								siniestroNuevo.get(0).get("CDUNIECO"), siniestroNuevo.get(0).get("CDRAMO"),
								siniestroNuevo.get(0).get("ESTADO"),   siniestroNuevo.get(0).get("NMPOLIZA"),
								siniestroNuevo.get(0).get("NMSUPLEM"), siniestroNuevo.get(0).get("NMSITUAC"),
								siniestroNuevo.get(0).get("NMSINIES"), msgResult,
								facturas.get(i).get("NFACTURA"),       siniestros.get(0).get("CDGARANT"),
								siniestros.get(0).get("CDCONVAL"), null,
								null, null,null, 
								siniestros.get(0).get("FEINGRESO"),siniestros.get(0).get("FEEGRESO"),
								siniestros.get(0).get("CDTIPEVE"),siniestros.get(0).get("CDTIPALT"),
								Constantes.INSERT_MODE);
						
						
						loadList = new ArrayList<HashMap<String,String>>();
						for(Map<String,String>map:lista) {
							loadList.add((HashMap<String,String>)map);
						}
						logger.debug("Valor del Siniestro nuevo : {}",siniestroNuevo.get(0).get("NMSINIES"));
						for(int j=0;j<loadList.size();j++){
							Date   dFemovimi = new Date();
							Date   dFeregist = new Date();
							String nmanno    = Calendar.getInstance().get(Calendar.YEAR)+"";
							//6.- Se realiza el guardado en MSINIVAL
							siniestrosManager.P_MOV_MSINIVAL(
								loadList.get(j).get("CDUNIECO"),		loadList.get(j).get("CDRAMO"),			loadList.get(j).get("ESTADO"),				loadList.get(j).get("NMPOLIZA"),
								loadList.get(j).get("NMSUPLEM"),		loadList.get(j).get("NMSITUAC"),		siniestroNuevo.get(0).get("AAAPERTU"),		siniestroNuevo.get(0).get("STATUS"),
								siniestroNuevo.get(0).get("NMSINIES"),	loadList.get(j).get("NFACTURA"),		loadList.get(j).get("CDGARANT"),			loadList.get(j).get("CDCONVAL"),
								loadList.get(j).get("CDCONCEP"),		loadList.get(j).get("IDCONCEP"),		loadList.get(j).get("CDCAPITA"),			loadList.get(j).get("NMORDINA"),
								dFemovimi,								loadList.get(j).get("CDMONEDA"),		loadList.get(j).get("PTPRECIO"),			loadList.get(j).get("CANTIDAD"),
								loadList.get(j).get("DESTOPOR"),		loadList.get(j).get("DESTOIMP"),		loadList.get(j).get("PTIMPORT"),			loadList.get(j).get("PTRECOBR"),
								nmanno,									loadList.get(j).get("NMAPUNTE"),		loadList.get(j).get("USERREGI"),			dFeregist,
								"I",									loadList.get(j).get("PTIMPOEX"),		loadList.get(j).get("DCTOIMEX"),			loadList.get(j).get("PTIMPOEX"),	
								loadList.get(j).get("PTMTOARA"),		loadList.get(j).get("APLICIVA"));
							
							
							
							
							//7.- Obtenemos los valores de TDSINIVAL para ver si tiene Ajuste Medico
							List<Map<String,String>> ajusteMedico = siniestrosManager.P_GET_TDSINIVAL(
									loadList.get(j).get("CDUNIECO"),		loadList.get(j).get("CDRAMO"),			loadList.get(j).get("ESTADO"),				loadList.get(j).get("NMPOLIZA"),
									loadList.get(j).get("NMSUPLEM"),		loadList.get(j).get("NMSITUAC"),		loadList.get(j).get("AAAPERTU"),			loadList.get(j).get("STATUS"),
									loadList.get(j).get("NMSINIES"),		loadList.get(j).get("NFACTURA"),		loadList.get(j).get("CDGARANT"),			loadList.get(j).get("CDCONVAL"),
									loadList.get(j).get("CDCONCEP"),		loadList.get(j).get("IDCONCEP"),		loadList.get(j).get("NMORDINA"));
							
							for(int t=0;t<ajusteMedico.size();t++){
								String userregi  = usuario.getUser();
					    		Date   fechaRegistro = new Date();
					    		//8.- Guaramos la informacion de del Ajuste Medico
					    		siniestrosManager.P_MOV_TDSINIVAL(
					    				loadList.get(j).get("CDUNIECO"), loadList.get(j).get("CDRAMO"), loadList.get(j).get("ESTADO"), loadList.get(j).get("NMPOLIZA"),
					    				loadList.get(j).get("NMSUPLEM"), loadList.get(j).get("NMSITUAC"), siniestroNuevo.get(0).get("AAAPERTU"), siniestroNuevo.get(0).get("STATUS"),
					    				siniestroNuevo.get(0).get("NMSINIES"), loadList.get(j).get("NFACTURA"),loadList.get(j).get("CDGARANT"), loadList.get(j).get("CDCONVAL"),
					    				loadList.get(j).get("CDCONCEP"), loadList.get(j).get("IDCONCEP"),loadList.get(j).get("NMORDINA"),loadList.get(j).get("NMORDMOV"),
					    				ajusteMedico.get(t).get("PTIMPORT"), ajusteMedico.get(t).get("COMMENTS"), userregi, fechaRegistro,
					    				Constantes.INSERT_MODE);
							}
						}
					}
					
					//9.- Obtenemos los valores para realizar el guardado de TVALOSIN
					List<Map<String, String>> validacionFacturas = siniestrosManager.P_GET_FACTURAS_SINIESTRO(msiniper.get(0).get("CDUNIECO"), msiniper.get(0).get("CDRAMO"),
							msiniper.get(0).get("ESTADO"),msiniper.get(0).get("NMPOLIZA"),msiniper.get(0).get("NMSUPLEM"), msiniper.get(0).get("NMSITUAC"),
							siniestros.get(0).get("AAAPERTU"), siniestros.get(0).get("STATUS"),siniestros.get(0).get("NMSINIES"), siniestros.get(0).get("CDTIPSIT"));
					
					for(int k=0; k < validacionFacturas.size();k++){
						//10.- Obtenemos la informacion de Siniestro por tramite
						List<Map<String,String>> asegurados = siniestrosManager.listaSiniestrosTramite2(msgResult, validacionFacturas.get(k).get("NFACTURA"));
						
						for(int h =0; h < asegurados.size();h++){
							String munSiniestro=asegurados.get(h).get("NMSINIES")+"";
							if(!munSiniestro.equalsIgnoreCase("null")){
								//11.- Guardamos la informacion en TVALOSIN
								Map<String,Object>paramsTvalosin = new HashMap<String,Object>();
								paramsTvalosin.put("pv_cdunieco"  , asegurados.get(h).get("CDUNIECO"));
								paramsTvalosin.put("pv_cdramo"    , asegurados.get(h).get("CDRAMO"));
								paramsTvalosin.put("pv_aaapertu"  , asegurados.get(h).get("AAAPERTU"));
								paramsTvalosin.put("pv_status"    , asegurados.get(h).get("STATUS"));
								paramsTvalosin.put("pv_nmsinies"  , asegurados.get(h).get("NMSINIES"));
								paramsTvalosin.put("pv_cdtipsit"  , asegurados.get(h).get("CDTIPSIT"));
								paramsTvalosin.put("pv_nmsuplem"  , asegurados.get(h).get("NMSUPLEM"));
								paramsTvalosin.put("pv_cdusuari"  , null);
								paramsTvalosin.put("pv_feregist"  , null);
								paramsTvalosin.put("pv_otvalor01" , validacionFacturas.get(k).get("APLICA_IVA"));
								paramsTvalosin.put("pv_otvalor02" , validacionFacturas.get(k).get("ANTES_DESPUES"));
								paramsTvalosin.put("pv_otvalor03" , validacionFacturas.get(k).get("IVARETENIDO"));
								paramsTvalosin.put("pv_otvalor04" , msgResult);
								paramsTvalosin.put("pv_otvalor05" , validacionFacturas.get(k).get("NFACTURA"));
								paramsTvalosin.put("pv_accion_i"  , "I");
								kernelManagerSustituto.PMovTvalosin(paramsTvalosin);
								
								//11.- Guardamos la informacion en MAUTSINI
								siniestrosManager.P_MOV_MAUTSINI(asegurados.get(h).get("CDUNIECO"), asegurados.get(h).get("CDRAMO"), asegurados.get(h).get("ESTADO"), 
									asegurados.get(h).get("NMPOLIZA"), asegurados.get(h).get("NMSUPLEM"), asegurados.get(h).get("NMSITUAC"), asegurados.get(h).get("AAAPERTU"), 
									asegurados.get(h).get("STATUS"), asegurados.get(h).get("NMSINIES"),validacionFacturas.get(k).get("NFACTURA"),
									null,null,null,null,null,
									Constantes.MAUTSINI_AREA_RECLAMACIONES,validacionFacturas.get(k).get("AUTRECLA"), Constantes.MAUTSINI_FACTURA, validacionFacturas.get(k).get("COMMENAR"), Constantes.INSERT_MODE);
								
								siniestrosManager.P_MOV_MAUTSINI(asegurados.get(h).get("CDUNIECO"), asegurados.get(h).get("CDRAMO"), asegurados.get(h).get("ESTADO"), 
									asegurados.get(h).get("NMPOLIZA"), asegurados.get(h).get("NMSUPLEM"), asegurados.get(h).get("NMSITUAC"), asegurados.get(h).get("AAAPERTU"), 
									asegurados.get(h).get("STATUS"), asegurados.get(h).get("NMSINIES"), validacionFacturas.get(k).get("NFACTURA"),
									null,null,null,null,null,
									Constantes.MAUTSINI_AREA_MEDICA, validacionFacturas.get(k).get("AUTMEDIC"), Constantes.MAUTSINI_FACTURA, validacionFacturas.get(k).get("COMMENME"), Constantes.INSERT_MODE);
							}
						}	
					}
				}
			}
		}catch( Exception e){
			logger.error("Error al generar el complemento del tramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener las facturas del tramite
	* @param ntramite  
	* @return List<Map<String, String>>  de las facturas del tramite
	*/ 
	public String obtenerFacturasTramite() {
		logger.debug("Entra a obtenerFacturasTramite smap :{}",smap);
		try{
			slist1=siniestrosManager.obtenerFacturasTramite(smap.get("ntramite"));
			mensaje="Facturas obtenidas";
			success=true;
		}
		catch(Exception ex) {
			success=false;
			logger.error("Error al obtener facturas de tramite : {}", ex.getMessage(), ex);
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion que obtiene la lista de las poliza
	* @param cdperson
	* @return PolizaVigenteVO con la informacion de los asegurados
	*/ 
	public String consultaListaPoliza(){
		logger.debug("Entra a consultaListaPoliza params de entrada :{}",params);
		try {
			UserVO usuario  = (UserVO)session.get("USUARIO");
			List<PolizaVigenteVO> lista = siniestrosManager.getConsultaListaPoliza(params.get("cdperson"), params.get("cdramo"), usuario.getRolActivo().getClave());
			if(lista!=null && !lista.isEmpty())	listaPoliza = lista;
			logger.debug("listaPoliza"+listaPoliza);
		}catch( Exception e){
			logger.error("Error al obtener los datos de la poliza : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene la lista del asegurado
	* @param void sin parametros de entrada
	* @return Lista GenericVO con la informacion de los asegurados
	*/    
	public String consultaListaAsegurado(){
		logger.debug("Entra a consultaListaAsegurado params de entrada :{}",params);
		try {
			listaAsegurado= siniestrosManager.getConsultaListaAsegurado(params.get("cdperson"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene la lista del asegurado
	* @param void sin parametros de entrada
	* @return Lista GenericVO con la informacion de los asegurados
	*/    
	public String consultaListaAseguradoPoliza(){
		logger.debug("Entra a consultaListaAseguradoPoliza params de entrada :{}",params);
		try {
			listaAsegurado= siniestrosManager.getConsultaListaAseguradoPoliza(params.get("cdunieco"),params.get("cdramo"),params.get("estado"),params.get("nmpoliza"),params.get("cdperson"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de Ramos de Salud vital
	* @param void sin parametros de entrada
	* @return Lista GenericVO - Informacion de los ramos
	*/    
	public String consultaRamosSalud(){
		logger.debug("Entra a consultaRamosSalud");
		try {
			listadoRamosSalud = siniestrosManager.getConsultaListaRamoSalud();
		}catch( Exception e){
			logger.error("Error al consultar los ramos para Salud : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtener el listado de los motivos de rechazos
	* @param 
	* @return List<HashMap<String, String>> - Motivo de rechazo
	*/
	public String loadListaRechazos(){
		logger.debug("Entra a loadListaRechazos");
		try {
			loadList = new ArrayList<HashMap<String,String>>();
			List<Map<String,String>>lista=siniestrosManager.loadListaRechazos();
			for(Map<String,String>ele:lista){
				HashMap<String,String>map=new HashMap<String,String>();
				map.put("key"   , ele.get("CDMOTIVO"));
				map.put("value" , ele.get("DSMOTIVO"));
				loadList.add(map);
			}
		}catch( Exception e){
			logger.error("Error en loadListaRechazos: {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtener el listado de los incisos de rechazos
	* @param 
	* @return List<HashMap<String, String>> - incisos de rechazo
	*/
	public String loadListaIncisosRechazos(){
		logger.debug("Entra a loadListaIncisosRechazos");
		try {
			loadList = new ArrayList<HashMap<String,String>>();
			List<Map<String,String>>lista= siniestrosManager.loadListaIncisosRechazos(params);
			for(Map<String,String>ele:lista) {
				HashMap<String,String>map=new HashMap<String,String>();
				map.put("key"   , ele.get("CDCAUMOT"));
				map.put("value" , ele.get("DSCAUMOT"));
				loadList.add(map);
			}
		}catch( Exception e){
			logger.error("Error en loadListaIncisosRechazos: {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para generar la carta de rechazo
	* @param params
	* @return PDF - Carta de rechazo
	*/
	public String generaCartaRechazo(){
		logger.debug("Entra a generaCartaRechazo");
		try {
			logger.debug("generaCartaRechazo Siniestros");
			UserVO usuario=(UserVO)session.get("USUARIO");
			String cdrol    = usuario.getRolActivo().getClave();
			String tipoPago = (String) paramsO.get("tipopago");
			String nombreRdf = "";
	
			if(RolSistema.COORDINADOR_MEDICO.getCdsisrol().equals(cdrol)
				|| RolSistema.COORDINADOR_MEDICO_MULTIREGIONAL.getCdsisrol().equals(cdrol)
				|| RolSistema.GERENTE_MEDICO_MULTIREGIONAL.getCdsisrol().equals(cdrol)
				|| RolSistema.MEDICO.getCdsisrol().equals(cdrol)
				|| RolSistema.MEDICO_AJUSTADOR.getCdsisrol().equals(cdrol)
			){
				nombreRdf = getText("rdf.siniestro.cartarechazo.medico.nombre");
			}else {
				if(TipoPago.DIRECTO.getCodigo().equals(tipoPago)){
					nombreRdf = getText("rdf.siniestro.cartarechazo.pagodirecto.nombre");
				} else{
					nombreRdf = getText("rdf.siniestro.cartarechazo.reembolso.nombre");
				}
			}
			
			File carpeta=new File(rutaDocumentosPoliza + "/" + paramsO.get("pv_ntramite_i"));
			if(!carpeta.exists()){
				logger.debug("no existe la carpeta : {}",paramsO.get("pv_ntramite_i"));
				carpeta.mkdir();
				if(carpeta.exists()){
					logger.debug("carpeta creada");
				} else {
					logger.debug("carpeta NO creada");
				}
			} else {
				logger.debug("existe la carpeta :{}",paramsO.get("pv_ntramite_i"));
			}
			String urlContrareciboSiniestro = ""
					+ rutaServidorReports
					+ "?p_usuario=" + usuario.getUser() 
					+ "&p_ntramite=" + paramsO.get("pv_ntramite_i")
					+ "&destype=cache"
					+ "&desformat=PDF"
					+ "&userid="+passServidorReports
					+ "&ACCESSIBLE=YES"
					+ "&report="+ nombreRdf
					+ "&paramform=no"
					;
			String nombreArchivo = getText("pdf.siniestro.cartarechazo.nombre");
			String pathArchivo=""
					+ rutaDocumentosPoliza
					+ "/" + paramsO.get("pv_ntramite_i")
					+ "/" + nombreArchivo
					;
			HttpUtil.generaArchivo(urlContrareciboSiniestro, pathArchivo);
	
			paramsO.put("pv_feinici_i"  , new Date());
			paramsO.put("pv_cddocume_i" , nombreArchivo);
			paramsO.put("pv_dsdocume_i" , "Carta Rechazo");
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
					,"Carta Rechazo"
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
			
		}catch( Exception e){
			logger.error("Error en generaCartaRechazo: {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el usuario al que se le turna el tramite
	* @param ntramite, rolDestino
	* @return usuarioTurnadoSiniestro - Usuario al que se le turno el tramite
	*/    
	public String obtieneUsuarioTurnado(){
		logger.debug("Entra a obtieneUsuarioTurnado params de entrada :{}",params);
		try {
			usuarioTurnadoSiniestro = siniestrosManager.obtieneUsuarioTurnadoSiniestro(params.get("ntramite"),params.get("rolDestino"));
		}catch( Exception e){
			logger.error("Error al consultar el periodo de espera en meses : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que valida si requiere autorizacion de servicio
	* @param Cobertura, Subcobertura, cdramo, cdtipsit
	* @return datosInformacionAdicional informacion adicional
	*/
	public String obtieneRequiereAutServ(){
		logger.debug("Entra a obtieneRequiereAutServ params de entrada :{}",params);
		try {
			datosInformacionAdicional = siniestrosManager.requiereInformacionAdicional(params.get("cobertura"),params.get("subcobertura"),params.get("cdramo"),params.get("cdtipsit"));
		}catch( Exception e){
			logger.error("Error al obtener si requiere autorizacion servicio : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo que obtiene el listado de las coberturas de poliza
	* @param maps [cdunieco,estado,cdramo,nmpoliza,nmsituac,cdgarant]
	* @return Lista CoberturaPolizaVO con la informacion de los asegurados
	*/
	public String consultaListaCoberturaPoliza(){
		logger.debug("Entra a consultaListaCoberturaPoliza params de entrada :{}",params);
		try {
			
			HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
			paramCobertura.put("pv_ntramite_i",params.get("ntramite"));
			paramCobertura.put("pv_tipopago_i",params.get("tipopago"));
			paramCobertura.put("pv_nfactura_i",params.get("tipopago")=="1"?params.get("tipopago"):null);
			paramCobertura.put("pv_cdunieco_i",params.get("cdunieco"));
			paramCobertura.put("pv_estado_i",params.get("estado"));
			paramCobertura.put("pv_cdramo_i",params.get("cdramo"));
			paramCobertura.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramCobertura.put("pv_nmsituac_i",params.get("nmsituac"));
			paramCobertura.put("pv_cdgarant_i",params.get("cdgarant"));
			
			logger.debug("Valor a enviar : {}",paramCobertura);
			List<CoberturaPolizaVO> lista = siniestrosManager.getConsultaListaCoberturaPoliza(paramCobertura);
			if(lista!=null && !lista.isEmpty())	listaCoberturaPoliza = lista;
		}catch( Exception e){
			logger.error("Error al obtener la lista de la cobertura de la poliza : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene la lista de Sbcobertura
	* @param cdgarant
	* @param cdsubcob
	* @return Lista GenericVO con la informacion de los asegurados
	*/    
	public String consultaListaSubcobertura(){
		logger.debug("Entra a consultaListaSubcobertura params de entrada :{}",params);
		try {
			UserVO usuarioR		= (UserVO)session.get("USUARIO");
			String cdrol		= usuarioR.getRolActivo().getClave();
			//cdunieco, cdramo, estado, nmpoliza, nmsituac, cdtipsit, cdgarant, cdsubcob
			listaSubcobertura= siniestrosManager.getConsultaListaSubcobertura(params.get("cdunieco"),params.get("cdramo"), params.get("estado"),
								params.get("nmpoliza"),params.get("nmsituac"),params.get("cdtipsit"),params.get("cdgarant"),params.get("cdsubcob"), cdrol);
		}catch( Exception e){
			logger.error("Error al consultar la Lista de subcoberturas : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el tipo de atencion
	* @param ramo y tipoPago
	* @return Lista GenericVO - Tipo de Atencion
	*/    
	public String consultaListaTipoAtencion(){
		logger.debug("Entra a consultaListaTipoAtencion params de entrada :{}",params);
		try {
			//if(params != null){
				listaTipoAtencion= siniestrosManager.getconsultaListaTipoAtencion(params.get("cdramo"), params.get("cdtipsit"), params.get("tipoPago"));
				logger.debug("listaTipoAtencion : {}",listaTipoAtencion);
			//}
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener la informacion que se encuentra en TMESACONTROL
	* @param ntramite
	* @return MesaControlVO - Informacion del tramite
	*/ 
	public String consultaListadoMesaControl(){
		logger.debug("Entra a consultaListadoMesaControl params de entrada :{}",params);
		try {
			List<MesaControlVO> lista = siniestrosManager.getConsultaListaMesaControl(params.get("ntramite"));
			if(lista!=null && !lista.isEmpty())	listaMesaControl = lista;
		}catch( Exception e){
			logger.error("Error al obtener los registros de la mesa de control : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para validar si ya existen documento cargados
	* @param params
	* @return Success - Exito si se genera el numero de tramite
	*/
	public String validaDocumentosCargados(){
		logger.debug("Entra a validaDocumentosCargados params de entrada :{}",params);
		try {
			msgResult = siniestrosManager.validaDocumentosCargados(params);
			logger.debug("Respuesta ValidaDocumentosCargados:{} ", msgResult);
			if(StringUtils.isBlank(msgResult)){
				msgResult = "Error al realizar validaci\u00f3 de documentos";
				success = false;
			}else if(Constantes.NO.equalsIgnoreCase(msgResult)){
				msgResult = "No se han anexado todos los documentos, favor de subir todos los documentos marcados como entregados en el checklist.";
				success =  false;
			}else if(Constantes.SI.equalsIgnoreCase(msgResult)){
				success =  true;
			}else{
				success =  false;
			}
		}catch( Exception e){
			logger.error("Error en validaDocumentosCargados : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene el listado de las subcobertura
	* @param cdtabla
	* @param otclave
	* @return Lista GenericVO con la informacion de los asegurados
	*/
	public String consultaListaCPTICD(){
		logger.debug("Entra a consultaListaCPTICD params de entrada :{}",params);
		try {
			listaCPTICD= siniestrosManager.getConsultaListaCPTICD(params.get("cdtabla"),params.get("otclave"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de subcoberturas : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	 *  Consulta de ICD's de la nueva tabla de apoyo (EGS)
	 * @return
	 */
	public String consultaListaICD(){
		logger.debug("Entra a consultaListaICD params de entrada :{}",params);
		try {
			listaCPTICD= siniestrosManager.getConsultaListaCPTICD(params.get("otclave1"), params.get("cdramo"),params.get("cdtipsit"),params.get("edad"),params.get("genero")); // (EGS) 
		}catch( Exception e){
			logger.error("Error al consultar la Lista de ICD's : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaListaCPTUnico(){
		logger.debug("Entra a consultaListaCPTUnico params de entrada :{}",params);
		try {
			datosValidacionGral = siniestrosManager.getConsultaListaCPTUnico(params.get("cdicd"));
		}catch( Exception e){
			logger.error("Error al consultaListaCPTUnico la Lista de ICD's : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaListaTipoMedico(){
		logger.debug("Entra a consultaListaTipoMedico params de entrada :{}",params);
		try {
			datosValidacionGral = siniestrosManager.getConsultaListaTipoMedicos(params.get("cdicd"),params.get("cdpts"));
			logger.debug("Valor de respuesta del procedure ==> :{}",datosValidacionGral);
		}catch( Exception e){
			logger.error("Error al consultaListaTipoMedico la Lista de ICD's : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	/**
	* Funcion que nos muestra la informacion de las pantalla principal de facturas y afiliados
	* @param params
	* @return visualizacion de la pantalla
	*/
	public String afiliadosAfectados() {
		logger.info("Entra a afiliadosAfectados params: {}",params);
		try{
			String ntramite = params.get("ntramite");
			if(ntramite==null){
				throw new Exception("No hay tramite");
			}
			params = (HashMap<String, String>) siniestrosManager.obtenerTramiteCompleto(ntramite);
			List<Map<String,String>> facturas = siniestrosManager.obtenerFacturasTramite(ntramite);

			if(facturas==null || facturas.size()==0){
				throw new Exception("No se encuentra la factura");
			}

			Map<String,String> factura = facturas.get(0);
			params.put("OTVALOR12",factura.get("CDGARANT"));
			params.put("OTVALOR14",factura.get("CDCONVAL"));
			params.put("DESCPORC",factura.get("DESCPORC"));
			params.put("DESCNUME",factura.get("DESCNUME"));
			UserVO usuario  = (UserVO)session.get("USUARIO");
			String cdrol    = usuario.getRolActivo().getClave();
			params.put("cdrol", cdrol);
			String pantalla = "AFILIADOS_AGRUPADOS";
			String seccion  = "FORMULARIO";

			List<ComponenteVO> componentes = pantallasManager.obtenerComponentes(
				null, null, null, null, null, cdrol, pantalla, seccion, null);

			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(componentes, true, false, true, false, false, false);

			imap = new HashMap<String,Item>();
			imap.put("itemsForm",gc.getItems());
			String cobertura= factura.get("CDGARANT");
			String subcobertura = factura.get("CDCONVAL");
			String valorComplementario = "0";

			slist2= facturas;//siniestrosManager.obtenerFacturasTramite(ntramite);
			seccion = "COLUMNAS";

			componentes = pantallasManager.obtenerComponentes(
				null, null, null, null, null, cdrol, pantalla, seccion, null);

			for(ComponenteVO com:componentes){
				com.setWidth(100);
			}
			gc.generaComponentes(componentes, true, false, false, true, false, false);

			imap.put("columnas",gc.getColumns());
			pantalla = "RECHAZO_SINIESTRO";
			seccion  = "FORMULARIO";

			componentes = pantallasManager.obtenerComponentes(
				null, null, null, null, null, cdrol, pantalla, seccion, null);
			gc.generaComponentes(componentes, true, false, true, false, false, false);

			imap.put("itemsCancelar",gc.getItems());
			pantalla = "DETALLE_FACTURA";
			seccion = "FORM_EDICION";
			componentes = pantallasManager.obtenerComponentes(
				null, null, null, null, null, cdrol, pantalla, seccion, null);
			gc.generaComponentes(componentes, true, false, true, false, false, false);
			imap.put("itemsEdicion",gc.getItems());
			List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosInfAsegurados(ntramite);
			logger.debug("Valor de siniestros : {}",siniestros);
			List<ComponenteVO>tatrisin=kernelManagerSustituto.obtenerTatrisinPoliza(siniestros.get(0).get("CDUNIECO"),siniestros.get(0).get("CDRAMO"),siniestros.get(0).get("ESTADO"),siniestros.get(0).get("NMPOLIZA"));
			gc.generaComponentes(tatrisin, true, false, true, false, false, false);
			imap.put("tatrisinItems",gc.getItems());
		}
		catch(Exception ex){
			logger.error("error al cargar pantalla de asegurados afectados : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}

	/**
	* Funcion para generar el Siniestro sin Autorizacion de servicio
	* @param params
	* @return Num. de autorizacion
	*/
	public String generarSiniestroSinAutorizacion(){
		logger.debug("Entra a generarSiniestroSinAutorizacion params: {}",params);
		try {
			String fechaOcurrencia = params.get("dateOcurrencia");
			Date   dFeOcurrencia   = renderFechas.parse(fechaOcurrencia);
			
			siniestrosManager.getAltaSiniestroSinAutorizacion(params.get("ntramite"),params.get("cdunieco"),params.get("cdramo"),params.get("estado"),
				  params.get("nmpoliza"),params.get("nmsuplem"),params.get("nmsituac"),params.get("cdtipsit"),dFeOcurrencia, params.get("nfactura"),
				  params.get("secAsegurado"));
			mensaje = "Se ha generado el siniestro";
			success=true;
		}
		catch(Exception ex) {
			logger.error("error al inicar siniestro desde tworksin : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
			success=false;
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para generar el Siniestro Por asegurado
	* @param params
	* @return Num. de autorizacion
	*/
	public String iniciarSiniestroTworksin() {
		logger.debug("Entra a iniciarSiniestroTworksin params de entrada :{}",params);
		try {
			String cdunieco = params.get("cdunieco");
			String cdramo = params.get("cdramo");
			String estado = params.get("estado");
			String cdpresta = params.get("cdpresta");
			
			String nmautser = params.get("nmautser");
			String cdperson = params.get("cdperson");
			String nmpoliza = params.get("nmpoliza");
			String ntramite = params.get("ntramite");
			String nfactura = params.get("nfactura");
			Date feocurrencia = renderFechas.parse(params.get("feocurrencia"));
			String secAsegurado =params.get("secAsegurado");
			//CUANDO SE PIDE EL NUMERO DE AUTORIZACION DE SERVICIO EN PANTALLA
			//INSERTA EL NUMERO DE AUTORIZACION EN TWORKSIN
			siniestrosManager.actualizarAutorizacionTworksin(ntramite,nmpoliza,cdperson,nmautser,nfactura,feocurrencia,secAsegurado);
			//CREA UN MSINIEST A PARTIR DE TWORKSIN
			siniestrosManager.getAltaSiniestroAutServicio(nmautser,nfactura);
			
			
			mensaje = "Se ha asociado el siniestro con la autorizaci\u00f3n";
			success=true;
			siniestrosManager.guardaAutorizacionConceptos(cdunieco,cdramo,estado,nfactura,nmautser,cdpresta,cdperson);
		}
		catch(Exception ex){
			logger.error("error al inicar siniestro desde tworksin : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
			success=false;
		}
		return SUCCESS;
	}

	/**
	* Funcion Para guardar para guardarl el Concepto, Destino de pago y Fecha de Pago
	* @param params
	* @return Num. de autorizacion
	*/
	public String guardarConceptoDestino() {
		logger.debug("Entra a guardarConceptoDestino params de entrada :{}",params);
		try {
			String ntramite = params.get("ntramite");
			String cdtipsit = params.get("cdtipsit");
			String cdDestinoPago = params.get("destinoPago");
			String cdConceptoPago = params.get("concepPago");
			String beneficiario = params.get("beneficiario");
			String cvebeneficiario = params.get("cvebeneficiario");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String tipoPago = params.get("tipoPago");
			String fechaSiniestro   = sdf.format(new Date());
			
			Map<String,Object> otvalor = new HashMap<String,Object>();
			otvalor.put("pv_ntramite_i" , ntramite);
			otvalor.put("pv_cdtipsit_i" , cdtipsit);
			otvalor.put("pv_otvalor18_i"  , cdDestinoPago);
			otvalor.put("pv_otvalor19_i"  , cdConceptoPago);
			otvalor.put("pv_otvalor21_i"  , fechaSiniestro); // Guardaremos la fecha cuando se solicito el pago del siniestro
			
			if(!tipoPago.equalsIgnoreCase("1")){
				otvalor.put("pv_otvalor04_i"  , beneficiario);
				otvalor.put("pv_otvalor27_i"  , cvebeneficiario);
			}
			siniestrosManager.actualizaOTValorMesaControl(otvalor);
			
			success = true;
			mensaje = "Tr\u00e1mite actualizado";
		}
		catch(Exception ex) {
			success=false;
			logger.error("error al seleccionar la cobertura : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion que realiza el proceso de solicitud de pago del Siniestro
	* @param params
	* @return Exito en el pago del Siniestro
	*/
	public String solicitarPago(){
		logger.debug("Entra a solicitarPago Datos de Entrada :{}",params);
		try {
			UserVO usuario  = (UserVO)session.get("USUARIO");
			//RespuestaVO res = ice2sigsService.ejecutaWSreclamosTramite(params.get("pv_ntramite_i"), Operacion.INSERTA, false, usuario);
			//success = res.isSuccess();
			success = true;
			//mensaje = res.getMensaje();
			mensaje = "Se omite temporalmente";
			
			logger.debug("Valor de success ==>: {}",success);
			logger.debug("Valor de mensaje ==>: {}",mensaje);
			if(success){
				List<SiniestroVO> siniestrosTramite = siniestrosManager.solicitudPagoEnviada(params);
				paramsO = new HashMap<String, Object>();
				paramsO.putAll(params);
				
				//String nombreRdf = getText("rdf.siniestro.cartafiniquito.nombre");
				File carpeta=new File(rutaDocumentosPoliza + "/" + paramsO.get("pv_ntramite_i"));
				if(!carpeta.exists()){
					logger.debug("no existe la carpeta: {}",paramsO.get("pv_ntramite_i"));
					carpeta.mkdir();
					if(carpeta.exists()){
						logger.debug("carpeta creada");
					} else {
						logger.debug("carpeta NO creada");
					}
				} else {
					logger.debug("existe la carpeta   : {} ",paramsO.get("pv_ntramite_i"));
				}
				for(SiniestroVO siniestro : siniestrosTramite){
					String nombreRdf = null;
					if(siniestro.getCdramo().equalsIgnoreCase(Ramo.RECUPERA.getCdramo())){
						nombreRdf = getText("rdf.siniestro.cartafiniquitoRecupera.nombre");
					}else if(siniestro.getCdramo().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
						nombreRdf = getText("rdf.siniestro.cartafiniquito.nombreGNP");
					}
					else{
						nombreRdf = getText("rdf.siniestro.cartafiniquito.nombre");
					}
					
					String urlFiniquitoSiniestro = ""
							+ rutaServidorReports
							+ "?p_unieco=" + siniestro.getCdunieco() 
							+ "&p_ramo="   + siniestro.getCdramo()
							+ "&p_estado=" + siniestro.getEstado()
							+ "&p_poliza=" + siniestro.getNmpoliza()
							+ "&p_suplem=" + siniestro.getNmsuplem()
							+ "&p_nmsituac="+ siniestro.getNmsituac()
							+ "&p_aaapertu="+ siniestro.getAapertu()
							+ "&p_status="  + siniestro.getStatusSinies()
							+ "&p_sinies="+ siniestro.getNmsinies()
							+ "&destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+passServidorReports
							+ "&ACCESSIBLE=YES"
							+ "&report="+ nombreRdf
							+ "&paramform=no"
							;
					String nombreArchivo = siniestro.getNmsinies() +"_"+ siniestro.getAapertu() +"_" + getText("pdf.siniestro.finiquito.nombre");
					String pathArchivo=""
							+ rutaDocumentosPoliza
							+ "/" + paramsO.get("pv_ntramite_i")
							+ "/" + nombreArchivo
							;
					HttpUtil.generaArchivo(urlFiniquitoSiniestro, pathArchivo);

					paramsO.put("pv_cdunieco_i"  , siniestro.getCdunieco());
					paramsO.put("pv_cdramo_i"  , siniestro.getCdramo());
					paramsO.put("pv_estado_i"  , siniestro.getEstado());
					paramsO.put("pv_nmpoliza_i"  , siniestro.getNmpoliza());
					paramsO.put("pv_nmsuplem_i"  , siniestro.getNmsuplem());
					paramsO.put("pv_feinici_i"  , new Date());
					paramsO.put("pv_cddocume_i" , nombreArchivo);
					paramsO.put("pv_dsdocume_i" , "Carta Finiquito");
					paramsO.put("pv_nmsolici_i" , siniestro.getNmpoliza());
					paramsO.put("pv_swvisible_i"   , null);
					paramsO.put("pv_codidocu_i"   , null);
					paramsO.put("pv_cdtiptra_i"   , TipoTramite.SINIESTRO.getCdtiptra());
					//kernelManagerSustituto.guardarArchivo(paramsO);
					
					documentosManager.guardarDocumento(
							siniestro.getCdunieco()
							,siniestro.getCdramo()
							,siniestro.getEstado()
							,siniestro.getNmpoliza()
							,siniestro.getNmsuplem()
							,new Date()
							,nombreArchivo
							,"Carta Finiquito"
							,siniestro.getNmpoliza()
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
					
				}
			}
		}catch( Exception e){
			logger.error("Error en solicitarPago generacion de cartas finiquito : {}", e.getMessage(), e);
			success =  false;
			mensaje = "Error al solicitar Pago, generaci\u00f3n de cartas finiquito. Consulte a Soporte T\u00e9cnico.";
			return SUCCESS;
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener los conceptos
	* @param params
	* @return obtenemos el listado de los conceptos
	*/
	public String obtenerMsinival() {
		logger.debug("Entra a obtenerMsinival params de entrada :{}",params);
		try {
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String nmsuplem = params.get("nmsuplem");
			String nmsituac = params.get("nmsituac");
			String aaapertu = params.get("aaapertu");
			String status   = params.get("status");
			String nmsinies = params.get("nmsinies");
			String nfactura = params.get("nfactura");
			List<Map<String,String>>lista = siniestrosManager.P_GET_MSINIVAL(
				cdunieco, cdramo, estado, nmpoliza, nmsuplem,
				nmsituac, aaapertu, status, nmsinies, nfactura);

			loadList = new ArrayList<HashMap<String,String>>();

			for(Map<String,String>map:lista) {
				loadList.add((HashMap<String,String>)map);
			}
			mensaje = "Datos obtenidos";
			success = true;
		}
		catch(Exception ex) {
			logger.error("error al obtener el listado de los conceptos : {}", ex.getMessage(), ex);
			success = false;
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	
	/**
	* Funcion para eliminar los conceptos
	* @param params
	* @return exito cuando eliminamos el concepto
	*/
	public String guardarMsinival() {
		logger.debug("Entra a guardarMsinival params de entrada :{} datosTabla : {}",params,datosTablas);
		try {			
			Date   dFemovimi = new Date();
			Date   dFeregist = new Date();
			String nmanno    = Calendar.getInstance().get(Calendar.YEAR)+"";
			//1.- Eliminamos los registros de la tabla msinival
			try {
				HashMap<String, Object> datosActualizacion = new HashMap<String, Object>();
				datosActualizacion.put("pv_cdunieco_i",datosTablas.get(0).get("cdunieco"));
				datosActualizacion.put("pv_cdramo_i",datosTablas.get(0).get("cdramo"));
				datosActualizacion.put("pv_estado_i",datosTablas.get(0).get("estado"));
				datosActualizacion.put("pv_nmpoliza_i",datosTablas.get(0).get("nmpoliza"));
				datosActualizacion.put("pv_nmsuplem_i",datosTablas.get(0).get("nmsuplem"));
				datosActualizacion.put("pv_nmsituac_i",datosTablas.get(0).get("nmsituac"));
				datosActualizacion.put("pv_aaapertu_i",datosTablas.get(0).get("aaapertu"));
				datosActualizacion.put("pv_status_i",datosTablas.get(0).get("status"));
				datosActualizacion.put("pv_nmsinies_i",datosTablas.get(0).get("nmsinies"));
				datosActualizacion.put("pv_nfactura_i",datosTablas.get(0).get("nfactura"));
				siniestrosManager.getBajaMsinival(datosActualizacion);
			} catch (Exception e) {
				logger.error("error al eliminar en guardarMsinival : {}", e.getMessage(), e);
			}
			//2.- Guardamos los registros de 
			double importeTotal = 0d;
			double importeIVA = 0d;
			double modIVA = 0d;
			double modImporte= 0d;
			for(int i=0;i<datosTablas.size();i++){
				
					logger.debug("Datos : {}",		datosTablas.get(i).get("ptIVA").length());
					logger.debug(" IVA ==> {}",		datosTablas.get(i).get("ptIVA"));
					logger.debug(" ptprecio ==> {}",datosTablas.get(i).get("ptprecio"));
					
					if(StringUtils.isNotBlank(datosTablas.get(i).get("ptIVA"))){
						modIVA     = Double.parseDouble(datosTablas.get(i).get("ptIVA")); 
					}else{
						modIVA     = 0d; 
					}
					if(StringUtils.isNotBlank(datosTablas.get(i).get("ptprecio"))){
						modImporte = Double.parseDouble(datosTablas.get(i).get("ptprecio"));  
					}else{
						modImporte = 0d;  
					}
					
					importeTotal = importeTotal + modImporte;
					importeIVA   = importeIVA   + modIVA;
					
					siniestrosManager.P_MOV_MSINIVAL(
						datosTablas.get(i).get("cdunieco"), 	datosTablas.get(i).get("cdramo"), 		datosTablas.get(i).get("estado"), 		datosTablas.get(i).get("nmpoliza"),
						datosTablas.get(i).get("nmsuplem"),		datosTablas.get(i).get("nmsituac"), 	datosTablas.get(i).get("aaapertu"), 	datosTablas.get(i).get("status"), 
						datosTablas.get(i).get("nmsinies"), 	datosTablas.get(i).get("nfactura"),		datosTablas.get(i).get("cdgarant"), 	datosTablas.get(i).get("cdconval"), 
						datosTablas.get(i).get("cdconcep"), 	datosTablas.get(i).get("idconcep"), 	datosTablas.get(i).get("cdcapita"),		datosTablas.get(i).get("nmordina"), 
						dFemovimi, 								datosTablas.get(i).get("cdmoneda"), 	datosTablas.get(i).get("ptprecio"), 	datosTablas.get(i).get("cantidad"),
						datosTablas.get(i).get("destopor"), 	datosTablas.get(i).get("destoimp"),		datosTablas.get(i).get("ptimport"), 	datosTablas.get(i).get("ptrecobr"),
						nmanno,									datosTablas.get(i).get("nmapunte"),		"", 									dFeregist,
						datosTablas.get(i).get("operacion"),	datosTablas.get(i).get("ptpcioex"),		datosTablas.get(i).get("dctoimex"),		datosTablas.get(i).get("ptimpoex"),
						datosTablas.get(i).get("mtoArancel"),	datosTablas.get(i).get("aplicaIVA"));
			}
			
			
			logger.debug("importeTotal : {} importeIVA:{}",importeTotal,importeIVA);
			
			List<MesaControlVO> listaMesaControl = siniestrosManager.getConsultaListaMesaControl(params.get("params.ntramite"));
			if(listaMesaControl.get(0).getCdtiptramc().equalsIgnoreCase(TipoTramite.PAGO_AUTOMATICO.getCdtiptra()) && 
			   listaMesaControl.get(0).getOtvalor25mc().equalsIgnoreCase("1")){
				//ACTUALIZAR TIMPSINI
				HashMap<String, Object> datosActualizacion = new HashMap<String, Object>();
				datosActualizacion.put("pv_accion_i",Constantes.INSERT_MODE);
				datosActualizacion.put("pv_cdunieco_i",datosTablas.get(0).get("cdunieco"));
				datosActualizacion.put("pv_cdramo_i",datosTablas.get(0).get("cdramo"));
				datosActualizacion.put("pv_estado_i",datosTablas.get(0).get("estado"));
				datosActualizacion.put("pv_nmpoliza_i",datosTablas.get(0).get("nmpoliza"));
				datosActualizacion.put("pv_nmsuplem_i",datosTablas.get(0).get("nmsuplem"));
				datosActualizacion.put("pv_nmsituac_i",datosTablas.get(0).get("nmsituac"));
				datosActualizacion.put("pv_aaapertu_i",datosTablas.get(0).get("aaapertu"));
				datosActualizacion.put("pv_status_i",datosTablas.get(0).get("status"));
				datosActualizacion.put("pv_nmsinies_i",datosTablas.get(0).get("nmsinies"));
				datosActualizacion.put("pv_ptimport_i",importeTotal);
				datosActualizacion.put("pv_ptiva_i",importeIVA);
				datosActualizacion.put("pv_ntramite_i",params.get("params.ntramite"));
				siniestrosManager.actualizarRegistroTimpsini(datosActualizacion);
				//ACTUALIZARTMESACONTROL 
				HashMap<String, Object> paramsPagoDirecto = new HashMap<String, Object>();
				paramsPagoDirecto.put("pv_ntramite_i",params.get("params.ntramite"));
				
				String montoTramite = siniestrosManager.obtieneMontoTramitePagoDirecto(paramsPagoDirecto);
				logger.debug("Valor del Monto del Arancel ===>>>> "+montoTramite);
				Map<String,Object> otvalor = new HashMap<String,Object>();
				otvalor.put("pv_ntramite_i" , params.get("params.ntramite"));
				otvalor.put("pv_otvalor03_i"  , montoTramite);
				siniestrosManager.actualizaOTValorMesaControl(otvalor);
			}
			
			mensaje = "Datos guardados";
			success = true;
		}
		catch(Exception ex){
			logger.debug("error al guardar msinival : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener el monto del arancel
	* @param params
	* @return montoArancel
	*/
	public String obtieneMontoArancel(){
		logger.debug("Entra a obtieneMontoArancel params de entrada :{}",params);
		try {
			String tipoConcepto = null;
			String claveProveedor = null;
			String idConceptoRegis = null;
			if(params.get("tipoConcepto") != null){
				tipoConcepto = params.get("tipoConcepto");
			}
			if(params.get("idProveedor") != null){
				claveProveedor = params.get("idProveedor");
			}
			if(params.get("idConceptoTipo") != null){
				idConceptoRegis = params.get("idConceptoTipo");
			}
			montoArancel = siniestrosManager.obtieneMontoArancelCPT(tipoConcepto ,claveProveedor,idConceptoRegis);
		}catch( Exception e){
			logger.error("Error al obtener el monto del arancel : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion donde obtenemos la informacion adicional por el cdperson
	* @param cdperson
	* @return List<Map<String, String>> datosInformacionAdicional
	*/ 
	public String consultaAutServicioSiniestro(){
		logger.debug("Entra a consultaAutServicioSiniestro params de entrada :{}",params);
		try {
			datosInformacionAdicional = siniestrosManager.getConsultaListaAutServicioSiniestro(params.get("cdperson"));
			logger.debug("Valor de Respuesta : {} Total de registros :{}" ,datosInformacionAdicional,datosInformacionAdicional.size());
		}catch( Exception e){
			logger.error("Error al obtener las autorizaciones : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion donde obtenemos los informacion del siniestro maestro
	* @param params
	* @return List<Map<String, String>> datosInformacionAdicional
	*/ 
	public String consultaSiniestroMaestro(){
		logger.debug("Entra a consultaSiniestroMaestro params de entrada :{}",params);
		try {
			datosInformacionAdicional = siniestrosManager.getConsultaListaMSiniestMaestro( params.get("cdunieco"),		params.get("cdramo"),		params.get("estado"),
																						   params.get("nmpoliza"),		params.get("nmsuplem"),		params.get("nmsituac"),
																						   params.get("status"));
			logger.debug("Siniestros Maestros : {}",datosInformacionAdicional);
		}catch( Exception e){
			logger.error("Error al obtener las consultaSiniestroMaestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion donde obtenemos los datos de las validaciones del siniestro
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/ 
	public String consultaDatosValidacionSiniestro(){
		logger.debug("Entra a consultaDatosValidacionSiniestro params de entrada :{} ",params);
		try {
			datosValidacion = siniestrosManager.getConsultaDatosValidacionSiniestro(params.get("ntramite"),params.get("nfactura"),params.get("tipoPago"));
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener los datos de la validacion del Siniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	/**
	* Funcion donde obtenemos los datos de las validaciones del siniestro
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/ 
	public String consultaDatosAseguradoSiniestro(){
		logger.debug("Entra a consultaDatosAseguradoSiniestro params de entrada :{}",params);
		try {
			datosValidacion = siniestrosManager.obtenerInfAseguradosTramite(params.get("ntramite"));
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener los datos de la validacion del Siniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	/**
	* Funcion donde obtenemos los datos de la validacion del Ajustador Medico
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/
	public String consultaDatosValidacionAjustadorMed(){
		logger.debug("Entra a consultaDatosValidacionAjustadorMed params de entrada :{}",params);
		try {
			datosValidacion = siniestrosManager.getConsultaDatosValidacionAjustadorMed(params.get("ntramite"));
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener las consultaDatosValidacionAjustadorMed : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion para guardar un solo asegurado desde afiliados afectados
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/
	public String guardaaseguradoUnico(){
		logger.debug("Entra a guardaTworksin params de entrada :{}",params);
		try{
			HashMap<String, Object> paramsTworkSin = new HashMap<String, Object>();
			paramsTworkSin.put("pv_nmtramite_i",params.get("nmtramite"));
			paramsTworkSin.put("pv_cdunieco_i",	params.get("cdunieco"));
			paramsTworkSin.put("pv_cdramo_i",	params.get("cdramo"));
			paramsTworkSin.put("pv_estado_i",	params.get("estado"));
			paramsTworkSin.put("pv_nmpoliza_i",	params.get("nmpoliza"));
			paramsTworkSin.put("pv_nmsolici_i",	params.get("nmsolici"));
			paramsTworkSin.put("pv_nmsuplem_i",	params.get("nmsuplem"));
			paramsTworkSin.put("pv_nmsituac_i",	params.get("nmsituac"));
			paramsTworkSin.put("pv_cdtipsit_i",	params.get("cdtipsit"));
			paramsTworkSin.put("pv_cdperson_i",	params.get("cdperson"));
			paramsTworkSin.put("pv_feocurre_i",	renderFechas.parse(params.get("feocurre")));
			paramsTworkSin.put("pv_nmautser_i",	null);
			paramsTworkSin.put("pv_nfactura_i",	params.get("nfactura"));
			paramsTworkSin.put("pv_reqautes_i",	"0");
			paramsTworkSin.put("pv_nmordina_i",  null);
			siniestrosManager.guardaListaTworkSin(paramsTworkSin);
			mensaje = "Asegurado guardado";
			success = true;
		}
		catch(Exception ex){
			logger.debug("error al guardar tworksin : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion para eliminar un asegurado desde afiliados afectados
	* @param params
	* @return exito
	*/
	public String eliminarAsegurado(){
		logger.debug("Entra a eliminarAsegurado params de entrada :{}",params);
		try{
			siniestrosManager.actualizaDatosGeneralesCopago(
					params.get("cdunieco"), params.get("cdramo"),
					params.get("estado"), params.get("nmpoliza"),
					params.get("nmsuplem"),params.get("nmsituac"),
					params.get("nmsinies"), params.get("nmtramite"),
					params.get("nfactura"),null,
					null,null,
					null,null,
					null,null,null,null,null,Constantes.DELETE_MODE);
			
			HashMap<String, Object> paramsTworkSin = new HashMap<String, Object>();
				paramsTworkSin.put("pv_nmtramite_i",params.get("nmtramite"));
				paramsTworkSin.put("pv_nfactura_i",params.get("nfactura"));
				paramsTworkSin.put("pv_cdunieco_i",params.get("cdunieco"));
				paramsTworkSin.put("pv_cdramo_i",params.get("cdramo"));
				paramsTworkSin.put("pv_estado_i",params.get("estado"));
				paramsTworkSin.put("pv_nmpoliza_i",params.get("nmpoliza"));
				paramsTworkSin.put("pv_nmsuplem_i",params.get("nmsuplem"));
				paramsTworkSin.put("pv_nmsituac_i",params.get("nmsituac"));
				paramsTworkSin.put("pv_cdtipsit_i",params.get("cdtipsit"));
				paramsTworkSin.put("pv_cdperson_i",params.get("cdperson"));
				paramsTworkSin.put("pv_feocurre_i",renderFechas.parse(params.get("feocurre")));
				paramsTworkSin.put("pv_nmsinies_i",params.get("nmsinies"));
				paramsTworkSin.put("pv_accion_i",params.get("accion"));
				siniestrosManager.eliminarAsegurado(paramsTworkSin);
				
				
				List<MesaControlVO> listaMesaControl = siniestrosManager.getConsultaListaMesaControl(params.get("nmtramite"));
				if(listaMesaControl.get(0).getCdtiptramc().equalsIgnoreCase(TipoTramite.PAGO_AUTOMATICO.getCdtiptra()) && 
				   listaMesaControl.get(0).getOtvalor25mc().equalsIgnoreCase("1")){					
					HashMap<String, Object> datosActualizacion = new HashMap<String, Object>();
					datosActualizacion.put("pv_accion_i",Constantes.DELETE_MODE);
					datosActualizacion.put("pv_cdunieco_i",params.get("cdunieco"));
					datosActualizacion.put("pv_cdramo_i",params.get("cdramo"));
					datosActualizacion.put("pv_estado_i",params.get("estado"));
					datosActualizacion.put("pv_nmpoliza_i",params.get("nmpoliza"));
					datosActualizacion.put("pv_nmsuplem_i",params.get("nmsuplem"));
					datosActualizacion.put("pv_nmsituac_i",params.get("nmsituac"));
					datosActualizacion.put("pv_aaapertu_i",null);
					datosActualizacion.put("pv_status_i",null);
					datosActualizacion.put("pv_nmsinies_i",params.get("nmsinies"));
					datosActualizacion.put("pv_ptimport_i",null);
					datosActualizacion.put("pv_ptiva_i",null);
					datosActualizacion.put("pv_ntramite_i",params.get("nmtramite"));
					siniestrosManager.actualizarRegistroTimpsini(datosActualizacion);
					
					HashMap<String, Object> paramsPagoDirecto = new HashMap<String, Object>();
					paramsPagoDirecto.put("pv_ntramite_i",params.get("params.ntramite"));
					
					String montoTramite = siniestrosManager.obtieneMontoTramitePagoDirecto(paramsPagoDirecto);
					logger.debug("Valor del Monto del Arancel ===>>>> "+montoTramite);
					Map<String,Object> otvalor = new HashMap<String,Object>();
					otvalor.put("pv_ntramite_i" , params.get("params.ntramite"));
					otvalor.put("pv_otvalor03_i"  , montoTramite);
					siniestrosManager.actualizaOTValorMesaControl(otvalor);
				}	
			mensaje = "Asegurado eliminado";
			success = true;
		}catch(Exception ex){
			logger.debug("error al eliminar registro : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el Siniestro x tramite
	* @param params
	* @return List<Map<String, String>> slist1
	*/
	public String obtenerSiniestrosTramite() {
		logger.debug("Entra a obtenerSiniestrosTramite smap de entrada :{}",smap);
		try {
			slist1 = siniestrosManager.listaSiniestrosTramite2(smap.get("ntramite"),smap.get("nfactura"));
			success=true;
			mensaje="Siniestros obtenidos";
			logger.debug("VALOR DE RESPUESTA ==> :{}",slist1);
		}
		catch(Exception ex) {
			logger.debug("error al obtener siniestros de tramite : {}", ex.getMessage(), ex);
			success = false;
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener el listado de las Suma Asegurada
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/
	public String consultaDatosSumaAsegurada(){
		logger.debug("Entra a consultaDatosSumaAsegurada params de entrada :{}",params);
		try {
			if(params.get("cdramo").equalsIgnoreCase("11")){
				datosValidacion = siniestrosManager.getConsultaDatosSumaAseguradaGNP(params.get("cdunieco"),		params.get("cdramo"),		params.get("estado"),
						  params.get("nmpoliza"),		params.get("cdperson"),		params.get("nmsinref"));
			}else{
				datosValidacion = siniestrosManager.getConsultaDatosSumaAsegurada(params.get("cdunieco"),		params.get("cdramo"),		params.get("estado"),
						  params.get("nmpoliza"),		params.get("cdperson"),		params.get("nmsinref"));
			}
			
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener las autorizaciones : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion para obtener la pantalla de Seleccion Cobertura
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/
	public String pantallaSeleccionCobertura() {
		logger.debug("Entra a pantallaSeleccionCobertura params de entrada :{}",params);
		try {
			UserVO usuario  = (UserVO)session.get("USUARIO");
			String cdrol    = usuario.getRolActivo().getClave();
			params.put("cdrol", cdrol);
			String pantalla = "SELECCION_COBERTURA";
			String seccion  = "FORMULARIO";
			imap = new HashMap<String,Item>();
			
			List<ComponenteVO> componentes = pantallasManager.obtenerComponentes(
						null, null, null, null, null, cdrol, pantalla, seccion, null);

			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(componentes, true, false, true, false, false, false);

			imap.put("item",gc.getItems());
			seccion = "COLUMNAS";
			componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
			gc.generaComponentes(componentes, true, true, false, true, false, false);
			imap.put("columnas",gc.getColumns());
			imap.put("modelFacturas", gc.getFields());
		}
		catch(Exception ex){
			logger.error("error al cargar pantalla de seleccion de cobertura : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener la informacion de los usuarios por rol
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/
	public String obtenerUsuariosPorRol() {
		logger.debug("Entra a obtenerUsuariosPorRol smap de entrada :{}",smap);
		try {
			String cdsisrol = smap.get("cdsisrol");
			slist1  = siniestrosManager.obtenerUsuariosPorRol(cdsisrol);
			success = true;
		}
		catch(Exception ex) {
			logger.error("error al obtener usuarios por rol : {}", ex.getMessage(), ex);
			slist1  = new ArrayList<Map<String,String>>();
			success = false;
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener los documentos
	* @param params
	* @return List<Map<String, String>> loadList
	*/
	public String loadListaDocumentos(){
		logger.debug("Entra a loadListaDocumentos params de entrada :{}",params);
		try {
			loadList = siniestrosManager.loadListaDocumentos(params);
		}catch( Exception e){
			logger.error("Error en loadListaDocumentos : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	/**
	* Funcion para guardar la lista de los documentos
	* @param params Y saveList
	* @return SUCCESS
	*/
	public String guardaListaDocumentos(){
		logger.debug("Entra a guardaListaDocumentos params de entrada :{} SaveList : {}",params, saveList);
		try {
			siniestrosManager.guardaEstatusDocumentos(params, saveList);
		}catch( Exception e){
			logger.error("Error en guardaListaDocumentos : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el total de Penalizacion por cambio de Zona
	* @param existePenalizacion,causaSiniestro,circuloHospAsegurado,zonaTarifiAsegurado,idProveedor,cdRamo
	* @return penalizacionCambioZona
	*/
	private double penalizacionCambioZona(String existePenalizacion, String causaSiniestro, String circuloHospAsegurado,
			String zonaTarifiAsegurado, String idProveedor, String cdRamo) {
		logger.debug("Entra a penalizacionCambioZona existePenalizacion : {}  causaSiniestro :{}  circuloHospAsegurado :{}  zonaTarifiAsegurado :{}  idProveedor :{}  cdRamo :{} ",
					existePenalizacion,causaSiniestro,circuloHospAsegurado,zonaTarifiAsegurado,idProveedor,cdRamo);
		double penalizacionCambioZona = 0;
		if(causaSiniestro!= null){
			if(!causaSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
				if(!existePenalizacion.equalsIgnoreCase("S")){
					try{
						List<ConsultaProveedorVO> medicos = siniestrosManager.getConsultaListaProveedorMedico(TipoPrestadorServicio.MEDICO.getCdtipo(),idProveedor);
						porcentajePenalizacion = siniestrosManager.validaPorcentajePenalizacion(zonaTarifiAsegurado, medicos.get(0).getZonaHospitalaria(), cdRamo);
						penalizacionCambioZona =  Double.parseDouble(porcentajePenalizacion);
					}catch(Exception ex){
						logger.debug("Error en la obtencion de la consulta : {}", ex.getMessage(), ex);
						penalizacionCambioZona =  Double.parseDouble("0");
					}
				}
			}
		}
		return penalizacionCambioZona;
	}
	
	/**
	* Funcion para obtener el total de Penalizacion Por circulo Hospitalario
	* @param circuloHospAsegurado, circuloHospProveedor, causaSiniestro, ramo
	* @return penaliCirculoHosp
	*/
	private double calcularPenalizacionCirculo(String circuloHospAsegurado, String circuloHospProveedor, String causaSiniestro,String ramo){
		logger.debug("Entra a calcularPenalizacionCirculo circuloHospAsegurado : {} circuloHospProveedor : {}  causaSiniestro : {}  ramo : {} ",
					circuloHospAsegurado, circuloHospProveedor, causaSiniestro, ramo);
		double penaliCirculoHosp = 0;
		if(causaSiniestro != null){
			try {
				SimpleDateFormat sdf 	= new SimpleDateFormat("dd/MM/yyyy");
				String feAutorizacion   = sdf.format(new Date());
				HashMap<String, Object> paramPenalizacion = new HashMap<String, Object>();
				paramPenalizacion.put("pv_circuloHosPoliza_i", circuloHospAsegurado);
				paramPenalizacion.put("pv_circuloHosProv_i",circuloHospProveedor);
				paramPenalizacion.put("pv_cdramo_i",ramo);
				paramPenalizacion.put("pv_feautori_i", feAutorizacion);
				porcentajePenalizacion = siniestrosManager.penalizacionCirculoHospitalario(paramPenalizacion);
				penaliCirculoHosp      = Double.parseDouble(porcentajePenalizacion);
			} catch (Exception ex) {
				logger.debug("Error en la obtencion de la consulta : {}", ex.getMessage(), ex);
				penaliCirculoHosp =  Double.parseDouble("0");
			}
		}
		return penaliCirculoHosp;
	}
	
	/**
	* Funcion para obtener el Total de Penalizaciones
	* @param penalizacionCambioZona, penalizacionCirculoHosp, causaSiniestro, copagoOriginal, tipoCopago, proveedor,ramo, fechaOcurrencia
	* @return copagoFinal
	*/
	private String calcularTotalPenalizacion(double penalizacionCambioZona, double penalizacionCirculoHosp, String causaSiniestro, String copagoOriginal, String tipoCopago,String proveedor,String ramo, String fechaOcurrencia) {
		logger.debug("Entra a calcularPenalizacionCirculo penalizacionCambioZona : {} penalizacionCirculoHosp : {} causaSiniestro : {} copagoOriginal : {} tipoCopago : {} proveedor : {} ramo : {} fechaOcurrencia : {} ",
				penalizacionCambioZona, penalizacionCirculoHosp, causaSiniestro, copagoOriginal, tipoCopago, proveedor,ramo, fechaOcurrencia);
		double copagoPenaPorcentaje = 0d;
		double copagoPenaPesos = 0d;
		String copagoFinal= null;
		double copagoOriginalPoliza = 0d;
		String copagoModificado= copagoOriginal.replaceAll(",", "");

		if(copagoOriginal.equalsIgnoreCase("no") || copagoOriginal.equalsIgnoreCase("na")){
			copagoOriginalPoliza = 0d;
		}else{
			if(copagoOriginal.equalsIgnoreCase("#######")){
				copagoOriginalPoliza = 0d;
			}else{
				copagoOriginalPoliza= Double.parseDouble(copagoModificado);
			}
		}
		if(causaSiniestro != null){
			if(!causaSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
				//1.- Verificamos el el Ramo
				if(ramo.equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo()) || ramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())|| ramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){ //SALUD VITAL
					if(tipoCopago.equalsIgnoreCase("%")){
						copagoPenaPorcentaje = penalizacionCambioZona + penalizacionCirculoHosp + Double.parseDouble(""+copagoOriginalPoliza);
						if(copagoPenaPorcentaje <= 0){
							copagoPenaPorcentaje= 0d;
						}
						copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
					}else{
						copagoPenaPorcentaje = penalizacionCambioZona + penalizacionCirculoHosp;
						if(copagoPenaPorcentaje <= 0){
							copagoPenaPorcentaje= 0d;
						}
						copagoPenaPesos		 = Double.parseDouble(""+copagoOriginalPoliza);
						copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
					}
				}else{ // DIFERENTE DE SALUD VITAL
					try {
						datosInformacionAdicional = siniestrosManager.listaConsultaCirculoHospitalarioMultisalud(proveedor,ramo,renderFechas.parse(fechaOcurrencia));
						logger.debug("Total de registros {}",datosInformacionAdicional.size());
						if(datosInformacionAdicional.size() > 0){
							logger.debug("Multi Incremento :{} ",datosInformacionAdicional.get(0).get("MULTINCREMENTO"));
							logger.debug("Hospital Plus : {}   ",datosInformacionAdicional.get(0).get("HOSPITALPLUS"));
							logger.debug("% Incremento : {}    ",datosInformacionAdicional.get(0).get("PORCINCREMENTO"));

							if(datosInformacionAdicional.get(0).get("HOSPITALPLUS").toString().equalsIgnoreCase("0")){
								copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
							}else{
								if(tipoCopago.equalsIgnoreCase("%")){
									copagoPenaPorcentaje =  Double.parseDouble(""+copagoOriginalPoliza) + Double.parseDouble(""+datosInformacionAdicional.get(0).get("PORCINCREMENTO").toString());
									if(copagoPenaPorcentaje <= 0){
										copagoPenaPorcentaje= 0d;
									}
									copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
								}else{
									copagoPenaPorcentaje = Double.parseDouble(""+datosInformacionAdicional.get(0).get("PORCINCREMENTO").toString());
									if(copagoPenaPorcentaje <= 0){
										copagoPenaPorcentaje= 0d;
									}
									copagoPenaPesos		 = Double.parseDouble(""+copagoOriginalPoliza);
									copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
								}
							}
						}else{
							copagoFinal = copagoPenaPorcentaje+"|"+copagoPenaPesos;
						}
					} catch (Exception e) {
						logger.error("error al obtener los datos del proveedor : {}", e.getMessage(), e);
					}
				}
			}else{
				if(tipoCopago.equalsIgnoreCase("%")){
					copagoFinal = copagoOriginalPoliza+"|"+copagoPenaPesos;
				}else{
					copagoFinal = copagoPenaPorcentaje+"|"+copagoOriginalPoliza;
				}
			}
		}else{
			copagoFinal = copagoPenaPorcentaje+"|"+copagoOriginalPoliza;
		}
		return copagoFinal;
	}
	
	/**
	 * Funcion para generar el calculo de siniestros para la nueva version de la pantalla
	 * @param params
	 * @return Exito en el pago del Siniestro
	 */
	public String generarCalculoSiniestros() {
		logger.debug("Entra a generarCalculoSiniestros params de entrada :{}",params);
		try {
			Map<String,String> factura        = null;
			Map<String,String> siniestroIte   = null;
			Map<String,String> proveedor      = null;
			Map<String,String> siniestro      = null;
			List<Map<String,String>>conceptos = null;
			
			slist2                  		  = new ArrayList<Map<String,String>>();
			slist3                  		  = new ArrayList<Map<String,String>>();
			llist1                  		  = new ArrayList<List<Map<String,String>>>();
			lhosp                   		  = new ArrayList<Map<String,String>>();
			lpdir                   		  = new ArrayList<Map<String,String>>();
			lprem                   		  = new ArrayList<Map<String,String>>();
			datosPenalizacion       		  = new ArrayList<Map<String,String>>();
			datosCoberturaxCal      		  = new ArrayList<Map<String,String>>();
			listaImportesWS         		  = new ArrayList<Map<String,String>>();
			
			String ntramite					  = params.get("ntramite");
			UserVO usuario					  = (UserVO)session.get("USUARIO");
			String cdrol					  = usuario.getRolActivo().getClave();
			this.facturasxSiniestro			  = new ArrayList<Map<String,Object>>();
			imap							  = new HashMap<String,Item>();
			
			// Obtenemos el tramite completo de MC
			Map<String,String> tramite     = siniestrosManager.obtenerTramiteCompleto(ntramite);
			logger.debug("Paso 1.- Tramite : {}",tramite);
			smap = tramite;
			// Obtenemos las facturas del tramite
			List<Map<String,String>> facturasAux = siniestrosManager.obtenerFacturasTramite(ntramite);
			logger.debug("Paso 2.- Listado Factura : {}",facturasAux.size());
			if(tramite==null||facturasAux==null){
				throw new Exception("No se encontro tramite/facturas para el tramite");
			}

			siniestrosManager.movTimpsini(Constantes.DELETE_MODE, null, null, null, null,
					null, null, null, null, null,
					ntramite, null, null, null, null, null, false,null);
			logger.debug("Paso 3.- Eliminacion de TIMPSINI");
			boolean esPagoDirecto			  = false;
			// Validamos con el otvalor02 que es el tipo de pago
			if(TipoPago.DIRECTO.getCodigo().equals(tramite.get("OTVALOR02"))){
				esPagoDirecto = true;
			}
			logger.debug("Paso 4.- Es pago Directo : {} ",esPagoDirecto);
			
			
			/***************************** 		P A G O		D I R E C T O  		*************************/
			if(TipoPago.DIRECTO.getCodigo().equals(tramite.get("OTVALOR02"))) {
				logger.debug("Paso 5.- EL PROCESO DE PAGO ES DIRECTO ");
				smap.put("PAGODIRECTO","S");
				smap2     = facturasAux.get(0);
				// Obtenemos los datos del proveedor de acuerdo del CDPRESTA
				/*proveedor = siniestrosManager.obtenerDatosProveedor(facturasAux.get(0).get("CDPRESTA"));
				logger.debug("Paso 6.- Datos del Proveedor : {}",proveedor);
				smap3     = proveedor;
				double ivaprov = Double.parseDouble(proveedor.get("IVA")); 
				double cedprov = Double.parseDouble(proveedor.get("CEDULAR"));
				double isrprov = Double.parseDouble(proveedor.get("ISR"));*/
				
				// Recorremos las facturas
				for(int i = 0; i < facturasAux.size(); i++){
					this.aseguradosxSiniestro = new ArrayList<Map<String,Object>>();
					factura = facturasAux.get(i);
					logger.debug("Paso 7.- Recorremos las Facturas  - El proceso i : {} de la factura : {}",i,factura.get("NFACTURA"));

					Map<String,Object>facturaObj = new HashMap<String,Object>();
					facturaObj.putAll(factura);
					this.facturasxSiniestro.add(facturaObj);
					
					
					proveedor = siniestrosManager.obtenerDatosProveedor(factura.get("CDPRESTA"));
					logger.debug("Paso 6.- Datos del Proveedor : {}",proveedor);
					smap3     = proveedor;
					double ivaprov = Double.parseDouble(proveedor.get("IVA"));
					double cedprov = 0d;
					double isrprov = 0d;
					
					if(factura.get("SWISR").equalsIgnoreCase("S")){
						isrprov = Double.parseDouble(proveedor.get("ISR"));
					}
					
					if(factura.get("SWICE").equalsIgnoreCase("S")){
						cedprov = Double.parseDouble(proveedor.get("CEDULAR"));
					}
					
					// Obtenemos los ASEGURADOS por medio del numero de tramite y factura
					List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,factura.get("NFACTURA"),null);
					logger.debug("Paso 8.- Obtenemos los Asegurados (MSINIEST) : {}",siniestros);
					
					// Obtenemos los conceptos de la factura
					conceptos = siniestrosManager.P_GET_CONCEPTOS_FACTURA(null, tramite.get("CDRAMO"), null, null, null, null, null, 
							null, null, factura.get("NFACTURA"),tramite.get("CDTIPSIT"), tramite.get("NTRAMITE"));
					logger.debug("Paso 9.- Obtenemos la informacion de los conceptos de la factura : {}", conceptos);
					
					// Recorremos el arreglo de asegurados
						for( int j= 0; j < siniestros.size();j++){
							// Asignacion de los variables a ocupar
							String	aplicaPenalCircHosp			= "S";
							String	aplicaPenalZonaHosp			= "S";
							
							logger.debug("Paso 10.- Recorremos los Siniestros - El proceso j : {} Siniestro : {}",j,siniestros.get(j));
							siniestroIte    = siniestros.get(j);
							
							//Validamos si tenemos autorizacion de servicio
							if(StringUtils.isNotBlank(siniestroIte.get("NMAUTSER"))){
								List<AutorizacionServicioVO> lista = siniestrosManager.getConsultaAutorizacionesEsp(siniestroIte.get("NMAUTSER"));
								logger.debug("Paso 10.1.- Verificamos la informacion si tiene Autorizacion de servicio : {} ",lista.size());
								aplicaPenalCircHosp		  = lista.get(0).getAplicaCirHos()+"";
								aplicaPenalZonaHosp		  = lista.get(0).getAplicaZonaHosp()+"";
							}
							
							Map<String,Object>aseguradoObj = new HashMap<String,Object>();
							aseguradoObj.putAll(siniestroIte);
							this.aseguradosxSiniestro.add(aseguradoObj);
							
							String cdunieco 					= siniestroIte.get("CDUNIECO");
							String cdramo   					= siniestroIte.get("CDRAMO");
							String estado   					= siniestroIte.get("ESTADO");
							String nmpoliza 					= siniestroIte.get("NMPOLIZA");
							String nmsuplem 					= siniestroIte.get("NMSUPLEM");
							String nmsituac 					= siniestroIte.get("NMSITUAC");
							String aaapertu 					= siniestroIte.get("AAAPERTU");
							String status   					= siniestroIte.get("STATUS");
							String cdtipsit 					= siniestroIte.get("CDTIPSIT");
							String nmsinies 					= siniestroIte.get("NMSINIES");
							String nfactura 					= factura.get("NFACTURA");
							
							double penalizacionCambioZona		= 0d;
							double penalizacionCirculoHosp		= 0d;
							String aplicaIVA					= "S";
							String seleccionAplica				= "D";
							String ivaRetenido					= "N";
							double deducibleSiniestroIte		= 0d;
							double copagoAplicadoSiniestroIte	= 0d;
							double cantidadCopagoSiniestroIte	= 0d;
							String penalizacionPesos 			= "0";
							String penalizacionPorcentaje		= "0";
							boolean existeCobertura				= false;
							
							Map<String,String>autorizacionesFactura = siniestrosManager.obtenerAutorizacionesFactura(
									cdunieco, cdramo, estado, nmpoliza, nmsuplem,
									nmsituac, aaapertu, status, nmsinies, nfactura);
									facturaObj.put("AUTMEDIC"+nmsinies,autorizacionesFactura.get("AUTMEDIC"));
									facturaObj.put("COMMENME"+nmsinies,autorizacionesFactura.get("COMMENME"));
									facturaObj.put("AUTRECLA"+nmsinies,autorizacionesFactura.get("AUTRECLA"));
									facturaObj.put("COMMENAR"+nmsinies,autorizacionesFactura.get("COMMENAR"));
									logger.debug("Paso 11.- Autorizacion de la informacion de la factura : {}", autorizacionesFactura);
									
									
							// Obtenemos los datos generales del siniestro
							List<Map<String,String>> informacionGral = siniestrosManager.obtieneDatosGeneralesSiniestro(cdunieco, cdramo,
									estado, nmpoliza,nmsituac, nmsuplem, status, aaapertu, nmsinies, factura.get("NTRAMITE"));
							logger.debug("Paso 12.- Datos generales del Siniestro : {} ",informacionGral);
							
							if(informacionGral.size()> 0){
								aseguradoObj.put("CAUSASINIESTRO", informacionGral.get(0).get("CDCAUSA"));
								if(cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) ||cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
									if(informacionGral.get(0).get("CDCAUSA").toString().equalsIgnoreCase(CausaSiniestro.ENFERMEDAD.getCodigo())){
										// Verificamos la Cobertura que tiene el asegurado
										HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
										paramCobertura.put("pv_ntramite_i",ntramite);
										paramCobertura.put("pv_tipopago_i",tramite.get("OTVALOR02"));
										paramCobertura.put("pv_nfactura_i",nfactura);
										paramCobertura.put("pv_cdunieco_i",cdunieco);
										paramCobertura.put("pv_estado_i",estado);
										paramCobertura.put("pv_cdramo_i",cdramo);
										paramCobertura.put("pv_nmpoliza_i",nmpoliza);
										paramCobertura.put("pv_nmsituac_i",nmsituac);
										paramCobertura.put("pv_cdgarant_i",null);
										List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaCoberturaAsegurado(paramCobertura);
										logger.debug("Paso 13.- Listado de Coberturas  : {} ",listaCobertura);
										for(int j1 = 0 ;j1 < listaCobertura.size();j1++){
											if(listaCobertura.get(j1).getCdgarant().toString().equalsIgnoreCase("7EDA")){
												existeCobertura = true;
											}
										}
									}
								}
							}else{
								aseguradoObj.put("CAUSASINIESTRO", CausaSiniestro.ENFERMEDAD.getCodigo());
							}
							// Obtenemos la informacion del Deducible y Copago
							Map<String,String>copagoDeducibleSiniestroIte =siniestrosManager.obtenerCopagoDeducible(
									ntramite, cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies,nfactura,tramite.get("OTVALOR02"), cdtipsit);
							logger.debug("Paso 13.- Informacion Deducible/Copago : {}",copagoDeducibleSiniestroIte);
							
							String tipoFormatoCalculo			= copagoDeducibleSiniestroIte.get("FORMATOCALCULO");
							String calculosPenalizaciones		= copagoDeducibleSiniestroIte.get("PENALIZACIONES");
							//String sonSesiones                  = copagoDeducibleSiniestroIte.get("VALSESIONES");
							facturaObj.put("TIPOFORMATOCALCULO",""+tipoFormatoCalculo);
							facturaObj.put("CALCULOSPENALIZACIONES",""+calculosPenalizaciones);
							logger.debug("Paso 14.- Aplica Penalizacion : {} ",calculosPenalizaciones);
							// Verificamos si aplica penalizaciones
							if(calculosPenalizaciones.equalsIgnoreCase("1")){
								// Generamos el map para pasar la informacion
								HashMap<String, Object> paramExclusion = new HashMap<String, Object>();
								paramExclusion.put("pv_cdunieco_i",cdunieco);
								paramExclusion.put("pv_estado_i",estado);
								paramExclusion.put("pv_cdramo_i",cdramo);
								paramExclusion.put("pv_nmpoliza_i",nmpoliza);
								paramExclusion.put("pv_nmsituac_i",nmsituac);
								
								if(cdramo.equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo()) || cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())||cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
									logger.debug("Valor de aplicaPenalZonaHosp : {} ",aplicaPenalZonaHosp);
									logger.debug("Valor de aplicaPenalCircHosp : {} ",aplicaPenalCircHosp);
									// Validacion por Cambio de Zona
									if(aplicaPenalZonaHosp.equalsIgnoreCase("N")){
										penalizacionCambioZona = 0d;
									}else{
										existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
										penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),informacionGral.get(0).get("CIRHOSPI"),
										informacionGral.get(0).get("DSZONAT"),informacionGral.get(0).get("CDPROVEE"),cdramo);
									}
									
									// Validacion por Circulo Hospitalario
									if(aplicaPenalCircHosp.equalsIgnoreCase("N")){
										penalizacionCirculoHosp = 0d;
									}else{
										// Obtenemos la validacion de Circulo Hospitalario
										penalizacionCirculoHosp = calcularPenalizacionCirculo(informacionGral.get(0).get("CIRHOSPI"), informacionGral.get(0).get("CIRHOPROV"),informacionGral.get(0).get("CDCAUSA"), cdramo);
									}
									aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
									aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
								}else{
									aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
									aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
								}
							}else{
								aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
								aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
							}
							
							logger.debug("Paso 15.- Existe Exclusion de Penalizacion : {} ",existePenalizacion);
							logger.debug("Paso 16.- Penalizacion por Cambio de Zona : {} ",penalizacionCambioZona);
							logger.debug("Paso 17.- Penalizacion por Circulo Hospitalario : {} ",penalizacionCirculoHosp);
							
							String calcularTotalPenalizacion = calcularTotalPenalizacion(penalizacionCambioZona,penalizacionCirculoHosp,informacionGral.get(0).get("CDCAUSA"),
									copagoDeducibleSiniestroIte.get("COPAGO"),copagoDeducibleSiniestroIte.get("TIPOCOPAGO"),
									informacionGral.get(0).get("CDPROVEE"),cdramo, informacionGral.get(0).get("FEOCURRE"));
							logger.debug("Paso 18.- Total Penalizacion : {} ",calcularTotalPenalizacion);
							
							aseguradoObj.put("TOTALPENALIZACIONGLOBAL",""+calcularTotalPenalizacion);
							
							// Obtenemos los valores de los Copago en Porcentaje y Copago en Pesos
							String penalizacionT[] = calcularTotalPenalizacion.split("\\|");
							penalizacionPorcentaje = penalizacionT[0].toString();
							penalizacionPesos = penalizacionT[1].toString();
							aseguradoObj.put("COPAGOPORCENTAJES",penalizacionPorcentaje);
							aseguradoObj.put("COPAGOPESOS",penalizacionPesos);
							
							List<Map<String, String>> listaFactura = siniestrosManager.P_GET_FACTURAS_SINIESTRO(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies, cdtipsit);
							logger.debug("Paso 19.- Informacion de la Factura aplicacion de IVA's : {} ",listaFactura);
							
							if(!listaFactura.isEmpty()){	// (EGS)
								if(listaFactura.get(0).get("APLICA_IVA") != null){
									aplicaIVA       = listaFactura.get(0).get("APLICA_IVA");
									seleccionAplica = listaFactura.get(0).get("ANTES_DESPUES");
									ivaRetenido     = listaFactura.get(0).get("IVARETENIDO");
									if(!StringUtils.isNotBlank(ivaRetenido)){
										ivaRetenido = "N";
									}
								}
							}
							
							String sDeducibleSiniestroIte		= copagoDeducibleSiniestroIte.get("DEDUCIBLE").replace(",","");
							String sCopagoSiniestroIte			= copagoDeducibleSiniestroIte.get("COPAGO").replace(",", "");
							String tipoCopagoSiniestroIte		= copagoDeducibleSiniestroIte.get("TIPOCOPAGO");
							
							if(StringUtils.isNotBlank(sDeducibleSiniestroIte)
									&&(!sDeducibleSiniestroIte.equalsIgnoreCase("na"))
									&&(!sDeducibleSiniestroIte.equalsIgnoreCase("no"))
							){
								try{
									deducibleSiniestroIte = Double.valueOf(sDeducibleSiniestroIte);
								}
								catch(Exception ex){
									logger.debug(""
											+ "\n### ERROR ##################################################"
											+ "\n### no es numero deducible: '"+sDeducibleSiniestroIte+"' ###"
											+ "\n############################################################"
									);
									deducibleSiniestroIte = 0d;
								}
							}
							
							if(cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) ||cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){//GMMI
								if(existeCobertura == true){
									deducibleSiniestroIte = 0d;
								}
							}
							logger.debug("Paso 20.- Valor de deducibleSiniestroIte : {} ",deducibleSiniestroIte);
							
							if(StringUtils.isNotBlank(sCopagoSiniestroIte)
									&&(!sCopagoSiniestroIte.equalsIgnoreCase("na"))
									&&(!sCopagoSiniestroIte.equalsIgnoreCase("no"))
							){
								try {
									cantidadCopagoSiniestroIte = Double.valueOf(sCopagoSiniestroIte);
								}
								catch(Exception ex){
									logger.debug(""
											+ "\n### ERROR ############################################"
											+ "\n### no es numero copago: '"+sCopagoSiniestroIte+"' ###"
											+ "\n######################################################"
									);
									cantidadCopagoSiniestroIte = 0d;
								}
							}
							logger.debug("Paso 21.- Valor de cantidadCopagoSiniestroIte : {} ",cantidadCopagoSiniestroIte);
							
							Map<String,String>importesWSSiniestroIte=new HashMap<String,String>();
							importesWSSiniestroIte.put("cdunieco" , siniestroIte.get("CDUNIECO"));
							importesWSSiniestroIte.put("cdramo"   , siniestroIte.get("CDRAMO"));
							importesWSSiniestroIte.put("estado"   , siniestroIte.get("ESTADO"));
							importesWSSiniestroIte.put("nmpoliza" , siniestroIte.get("NMPOLIZA"));
							importesWSSiniestroIte.put("nmsuplem" , siniestroIte.get("NMSUPLEM"));
							importesWSSiniestroIte.put("nmsituac" , siniestroIte.get("NMSITUAC"));
							importesWSSiniestroIte.put("aaapertu" , siniestroIte.get("AAAPERTU"));
							importesWSSiniestroIte.put("status"   , siniestroIte.get("STATUS"));
							importesWSSiniestroIte.put("nmsinies" , siniestroIte.get("NMSINIES"));
							importesWSSiniestroIte.put("ntramite" , ntramite);
							listaImportesWS.add(importesWSSiniestroIte);
							
							double importeSiniestroIte;
							double ivaSiniestroIte;
							double ivrSiniestroIte;
							double isrSiniestroIte;
							double cedSiniestroIte;
							
							//hospitalizacion
							Map<String,String> hosp = new HashMap<String,String>();
							lhosp.add(hosp);
							hosp.put("PTIMPORT"		, "0");
							hosp.put("DESTO"		, "0");
							hosp.put("IVA"			, "0");
							hosp.put("PRECIO"		, "0");
							hosp.put("DESCPRECIO"	, "0");
							hosp.put("IMPISR"		, "0");
							hosp.put("IMPCED"		, "0");
							
							//reembolso
							Map<String,String>mprem=new HashMap<String,String>();
							mprem.put("dummy","dummy");
							lprem.add(mprem);
							
							//pago directo
							Map<String,String> mpdir = new HashMap<String,String>();
							mpdir.put("total",				"0");
							mpdir.put("totalcedular",		"0");
							mpdir.put("ivaTotalMostrar",	"0");
							mpdir.put("ivaRetenidoMostrar",	"0");
							mpdir.put("iSRMostrar",			"0");
							lpdir.add(mpdir);
							this.conceptosxSiniestro = new ArrayList<Map<String,String>>();
							
							// Recorremos los conceptos de la factura
							for(int k = 0; k < conceptos.size() ; k++){
								Map<String, String> concepto = conceptos.get(k);
								
								if(concepto.get("CDUNIECO").equals(siniestroIte.get("CDUNIECO"))
										&&concepto.get("CDRAMO").equals(siniestroIte.get("CDRAMO"))
										&&concepto.get("ESTADO").equals(siniestroIte.get("ESTADO"))
										&&concepto.get("NMPOLIZA").equals(siniestroIte.get("NMPOLIZA"))
										&&concepto.get("NMSUPLEM").equals(siniestroIte.get("NMSUPLEM"))
										&&concepto.get("NMSITUAC").equals(siniestroIte.get("NMSITUAC"))
										&&concepto.get("AAAPERTU").equals(siniestroIte.get("AAAPERTU"))
										&&concepto.get("STATUS").equals(siniestroIte.get("STATUS"))
										&&concepto.get("NMSINIES").equals(siniestroIte.get("NMSINIES"))
								){
									conceptosxSiniestro.add(concepto);
									logger.debug("VALORES DEL CONCEPTO ================>>>>>>>>>>>>>>> : {} {}",k,concepto);
									
									logger.debug("Datos de los conceptos  contador k: {} ",k);
									logger.debug("Datos de los conceptos  concepto : {}  COPAGO: {} ",concepto,concepto.get("COPAGO"));
									logger.debug("Datos de los clave Concepto : {}",concepto.get("CDCONCEP") );
									
									if(tipoFormatoCalculo.equalsIgnoreCase("1")) {
										logger.debug("--->>>>>>> HOSPITALIZACION");
										double PTIMPORT    = Double.parseDouble(concepto.get("PTIMPORT"));
										double DESTOPOR    = Double.parseDouble(concepto.get("DESTOPOR"));
										double DESTOIMP    = Double.parseDouble(concepto.get("DESTOIMP"));
										double PTPRECIO    = Double.parseDouble(concepto.get("PTPRECIO")) * Double.parseDouble(concepto.get("CANTIDAD"));
										boolean copagoPorc = false;
										String scopago     = concepto.get("COPAGO");
										
										if(scopago.equalsIgnoreCase("no") ||scopago.equalsIgnoreCase("na")){
											scopago="0";
										}
										logger.debug("Valor de respuesta :{}",StringUtils.isNotBlank(scopago));
										if(StringUtils.isNotBlank(scopago)){
											if(scopago.contains("%")){
												copagoPorc = true;
											}
											scopago = scopago.replace("%", "").replace("$", "");
											if(copagoPorc) {
												DESTOPOR = DESTOPOR+Double.valueOf(scopago);
											}
											else{
												/*if(sonSesiones.equalsIgnoreCase("1")){
													DESTOIMP=DESTOIMP+(Double.valueOf(scopago)*Double.parseDouble(concepto.get("CANTIDAD"))) ;
												}else{
													DESTOIMP=DESTOIMP+Double.valueOf(scopago);
												}*/
												DESTOIMP=DESTOIMP+Double.valueOf(scopago);
											}
										}
										
										// valor del copago  scopago
										double hPTIMPORT 	= Double.parseDouble(hosp.get("PTIMPORT"));
										double hDESTO    	= Double.parseDouble(hosp.get("DESTO"));
										double hIVA      	= Double.parseDouble(hosp.get("IVA"));
										double hISR      	= Double.parseDouble(hosp.get("IMPISR"));
										double hICED      	= Double.parseDouble(hosp.get("IMPCED"));
										double hPRECIO      = Double.parseDouble(hosp.get("PRECIO"));
										double hDESCPRECIO  = Double.parseDouble(hosp.get("DESCPRECIO"));
										
										hPTIMPORT 	+= PTIMPORT;
										hDESTO    	+= (PTIMPORT*(DESTOPOR/100d)) + (DESTOIMP);
										
										//Cuando nos den el VoBo. lo tendremos que decomentar
										if(aplicaIVA.equalsIgnoreCase("S")){
											//verificamos si aplica para el concepto 
											if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
												hIVA      	+= PTIMPORT*(ivaprov/100d);
											}
										}else{
											hIVA      	+= PTIMPORT*(ivaprov/100d);
										}
										//hIVA      	+= PTIMPORT*(ivaprov/100d);
										hISR		+= PTIMPORT*(isrprov/100d);
										hICED		+= PTIMPORT*(cedprov/100d);
										hPRECIO 	+= PTPRECIO;
										hDESCPRECIO += (PTPRECIO*(DESTOPOR/100d)) + (DESTOIMP);
										
										hosp.put("PTIMPORT" , hPTIMPORT+"");
										hosp.put("DESTO"    , hDESTO+"");
										hosp.put("IVA"      , hIVA+"");
										hosp.put("IMPISR"   , hISR+"");
										hosp.put("PRECIO"   , hPRECIO+"");
										hosp.put("DESCPRECIO", hDESCPRECIO+"");
										hosp.put("IMPCED"   , hICED+"");
										
										logger.debug("Concepto importe 			    : {} ",PTIMPORT);
										logger.debug("Concepto desto 			    : {} ",DESTOPOR);
										logger.debug("Concepto destoimp 		    : {} ",DESTOIMP);
										logger.debug("Usando iva proveedor          : {} ",ivaprov);
										logger.debug("Concepto copago               : {} ",scopago);
										logger.debug("Concepto desto + copago %     : {} ",DESTOPOR);
										logger.debug("Concepto destoimp + copago $  : {} ",DESTOIMP);
										
										logger.debug("#### VALORES DEL VECTOR #####");
										logger.debug("Concepto hPTIMPORT total      : {} ",hPTIMPORT);
										logger.debug("Concepto hDESTO total         : {} ",hDESTO);
										logger.debug("Concepto hIVA total           : {} ",hIVA);
										logger.debug("Concepto hISR total 		    : {} ",hISR);
										logger.debug("Concepto hPRECIO total        : {} ",hPRECIO);
										logger.debug("Concepto hDESCPRECIO total    : {} ",hDESCPRECIO);
										logger.debug("Concepto hICED total          : {} ",hICED);
										logger.debug("<<<<<<<--- HOSPITALIZACION");
									}else {
										logger.debug("--->>>>>>> PAGO DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD  ---->>>>>>>>>> ");
										double precioArancel		= 0d;
										double descuentoPorc		= 0d;
										double descuentoImpo		= 0d;
										boolean copagoPorc			= false;
										double  copago				= 0d;
										double  copagoAplicado		= 0d;
										
										Map<String,String>row = new HashMap<String,String>();
										row.putAll(concepto);
										double cantidad				= Double.valueOf(row.get("CANTIDAD"));
										
										if(StringUtils.isNotBlank(row.get("IMP_ARANCEL"))) {
											
											if(concepto.get("CDCONCEP").equalsIgnoreCase("-1")){
												String scopago 			 = concepto.get("COPAGO");
												logger.debug("====>>>> VALOR DEL COPAGO "+scopago);
												logger.debug("====>>>> VALOR DEL COPAGO "+concepto.get("COPAGO"));
												scopago=scopago.replace("%", "").replace("$", "").replaceAll(",", "");
												copago=Double.valueOf(scopago);
												precioArancel  = copago;
											}else{
												precioArancel 		= Double.valueOf(row.get("IMP_ARANCEL"));
											}
											
										}
										row.put("IMP_ARANCEL",precioArancel+"");
										
										double subtotalArancel 	= cantidad*precioArancel;
										row.put("SUBTTARANCEL",subtotalArancel+"");
										
										if(StringUtils.isNotBlank(row.get("DESTOPOR"))){
											descuentoPorc 		= Double.parseDouble(row.get("DESTOPOR"));
										}
										
										if(StringUtils.isNotBlank(row.get("DESTOIMP"))){
											descuentoImpo 		= Double.parseDouble(row.get("DESTOIMP"));
										}
										
										double descuentoAplicado = (subtotalArancel*(descuentoPorc/100d))+descuentoImpo;
										row.put("DESTOAPLICA",descuentoAplicado+"");
										
										double subtotalDescuento = subtotalArancel-descuentoAplicado;//++
										row.put("SUBTTDESCUENTO",subtotalDescuento+""); // SUBTOTAL A OCUPAR CUANDO SEA ANTES DE COPAGO
										
										if(aplicaIVA.equalsIgnoreCase("S")){
											if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
												if(seleccionAplica.equalsIgnoreCase("A")){ // ANTES DEL COPAGO
													double iVaaplicaAntes = subtotalDescuento*(ivaprov/100d);
													row.put("IVAAPLICA",iVaaplicaAntes+"");
												}
											}else{
												double iVaaplicaAntes = 0d;
												row.put("IVAAPLICA",iVaaplicaAntes+"");
											}
										}
										
										String scopago 			 = concepto.get("COPAGO");
										String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
										
										if(causadelSiniestro == ""||causadelSiniestro == null){
											causadelSiniestro = CausaSiniestro.ENFERMEDAD.getCodigo();
										}
										if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
											copagoAplicado    = 0d;
										}else{
											if(StringUtils.isNotBlank(scopago)){
												if(scopago.equalsIgnoreCase("na") || scopago.equalsIgnoreCase("no")){
													copagoAplicado = 0d;
												}else{
													if(scopago.contains("%")){
														copagoPorc = true;
													}
													scopago=scopago.replace("%", "").replace("$", "").replaceAll(",", "");
													copago=Double.valueOf(scopago);
													if(copagoPorc) {
														copagoAplicado=(subtotalDescuento*(copago/100d));
													}
													else {
														copagoAplicado=copago * cantidad;
													}
												}
											}
										}
										row.put("COPAGOAPLICA",copagoAplicado+"");
										
										//Realizamos la actualizacion del Concepto
										HashMap<String, Object> actDeducibleCopagoConcepto = new HashMap<String, Object>();
										actDeducibleCopagoConcepto.put("pv_cdunieco_i",concepto.get("CDUNIECO"));
										actDeducibleCopagoConcepto.put("pv_cdramo_i",concepto.get("CDRAMO"));
										actDeducibleCopagoConcepto.put("pv_estado_i",concepto.get("ESTADO"));
										actDeducibleCopagoConcepto.put("pv_nmpoliza_i",concepto.get("NMPOLIZA"));
										actDeducibleCopagoConcepto.put("pv_nmsuplem_i",concepto.get("NMSUPLEM"));
										actDeducibleCopagoConcepto.put("pv_nmsituac_i",concepto.get("NMSITUAC"));
										actDeducibleCopagoConcepto.put("pv_aaapertu_i",concepto.get("AAAPERTU"));
										actDeducibleCopagoConcepto.put("pv_status_i",concepto.get("STATUS"));
										actDeducibleCopagoConcepto.put("pv_msinies_i",concepto.get("NMSINIES"));
										actDeducibleCopagoConcepto.put("pv_nfactura_i",concepto.get("NFACTURA"));
										actDeducibleCopagoConcepto.put("pv_cdgarant_i",concepto.get("CDGARANT"));
										actDeducibleCopagoConcepto.put("pv_cdconval_i",concepto.get("CDCONVAL"));
										actDeducibleCopagoConcepto.put("pv_cdconcep_i",concepto.get("CDCONCEP"));
										actDeducibleCopagoConcepto.put("pv_idconcep_i",concepto.get("IDCONCEP"));
										actDeducibleCopagoConcepto.put("pv_ptprecio_i",concepto.get("PTPRECIO"));
										actDeducibleCopagoConcepto.put("pv_cantidad_i",concepto.get("CANTIDAD"));
										actDeducibleCopagoConcepto.put("pv_deducible_i","0.00");
										actDeducibleCopagoConcepto.put("pv_copago_i",copagoAplicado+"");
										siniestrosManager.actualizarDeducibleCopagoConceptos(actDeducibleCopagoConcepto);
										
										double subtotalCopago    = subtotalDescuento - copagoAplicado;//++
										row.put("SUBTTCOPAGO",subtotalCopago+"");
										
										double israplicado       = subtotalCopago*(isrprov/100d);//++
										row.put("ISRAPLICA",israplicado+"");
										
										double subtotalImpuestos = subtotalCopago-(israplicado+0d);
										
										double totalISRMostrar   = Double.parseDouble(mpdir.get("iSRMostrar"));
										totalISRMostrar 		+= israplicado;
										mpdir.put("iSRMostrar",totalISRMostrar+"");
										
										double cedularaplicado   = subtotalCopago*(cedprov/100d);//++
										row.put("CEDUAPLICA",cedularaplicado+"");
										
										logger.debug("Concepto cantidad 			 : {} ",cantidad);
										logger.debug("Concepto precioArancel 		 : {} ",precioArancel);
										logger.debug("Concepto subtotalArancel 		 : {} ",subtotalArancel);
										logger.debug("Concepto descuentoAplicado 	 : {} ",descuentoAplicado);
										logger.debug("Concepto subtotalDescuento 	 : {} ",subtotalDescuento);
										logger.debug("Concepto copagoAplicado        : {} ",copagoAplicado);
										logger.debug("Concepto subtotalCopago 		 : {} ",subtotalCopago);
										logger.debug("Concepto israplicado 			 : {} ",israplicado);
										logger.debug("Concepto subtotalImpuestos 	 : {} ",subtotalImpuestos);
										logger.debug("Concepto base totalISRMostrar  : {} ",totalISRMostrar);
										logger.debug("Concepto cedularaplicado 		 : {} ",cedularaplicado);
										
										subtotalImpuestos 		 = subtotalImpuestos - cedularaplicado;
										row.put("SUBTTIMPUESTOS",subtotalImpuestos+"");
										
										double ivaaplicado  = 0d;
										double ivaRetenidos = 0d;
										double ptimportauto = 0d;
										
										if(aplicaIVA.equalsIgnoreCase("S")){
											if(seleccionAplica.equalsIgnoreCase("D")){
												if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
													ivaaplicado       = subtotalCopago*(ivaprov/100d);
												}else{
													ivaaplicado= 0d;
												}
												//ivaaplicado       = subtotalCopago*(ivaprov/100d);
												row.put("IVAAPLICA",ivaaplicado+"");
												if(ivaRetenido.equalsIgnoreCase("S")){
													ivaRetenidos      = ((2d * ivaaplicado)/3);
													row.put("IVARETENIDO",ivaRetenidos+"");
												}else{
													ivaRetenidos      = 0d;
													row.put("IVARETENIDO",ivaRetenidos+"");
												}
												ptimportauto      = (subtotalImpuestos - ivaRetenidos)+ivaaplicado;
												row.put("PTIMPORTAUTO",ptimportauto+"");
											}else{
												if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
													ivaaplicado       = subtotalDescuento*(ivaprov/100d);
												}else{
													ivaaplicado= 0d;
												}
												//ivaaplicado       = subtotalDescuento*(ivaprov/100d);
												row.put("IVAAPLICA",ivaaplicado+"");
												if(ivaRetenido.equalsIgnoreCase("S")){
													ivaRetenidos      = ((2d * ivaaplicado)/3);
													row.put("IVARETENIDO",ivaRetenidos+"");
												}else{
													ivaRetenidos      = 0d;
													row.put("IVARETENIDO",ivaRetenidos+"");
												}
												ptimportauto      = (subtotalImpuestos-ivaRetenidos)+ivaaplicado; //++
												row.put("PTIMPORTAUTO",ptimportauto+"");
											}
										}else{
											ivaaplicado       = 0d;
											ivaRetenidos      = 0d;
											row.put("IVAAPLICA",ivaaplicado+"");
											row.put("IVARETENIDO",ivaRetenidos+"");
											ptimportauto      = (subtotalImpuestos-ivaRetenidos)+ivaaplicado;
											row.put("PTIMPORTAUTO",ptimportauto+"");
										}
										
										double totalIVAMostrar = Double.parseDouble(mpdir.get("ivaTotalMostrar"));
										logger.debug("Concepto base totalIVAMostrar : {} ",totalIVAMostrar);
										totalIVAMostrar += ivaaplicado;
										logger.debug("Sumatoria totalIVAMostrar : {} ",totalIVAMostrar);
										mpdir.put("ivaTotalMostrar",totalIVAMostrar+"");
										
										double totalIVARetenidoMostrar = Double.parseDouble(mpdir.get("ivaRetenidoMostrar"));
										logger.debug("Concepto base totalIVARetenidoMostrar : {} ",totalIVARetenidoMostrar);
										
										totalIVARetenidoMostrar += ivaRetenidos;
										logger.debug("Sumatoria totalIVAMostrar      : {} ",totalIVARetenidoMostrar);
										mpdir.put("ivaRetenidoMostrar",totalIVARetenidoMostrar+"");
										
										double ptimport = Double.parseDouble(row.get("PTIMPORT"));
										double valorusado      = ptimportauto;
										
										String autmedic = row.get("AUTMEDIC");
										if(StringUtils.isNotBlank(autmedic)&&autmedic.equalsIgnoreCase("S")){
											valorusado = ptimport;
										}
										row.put("VALORUSADO",valorusado+"");
										
										logger.debug("Concepto ptimport              : {} ",ptimport);
										logger.debug("Concepto valorusado            : {} ",valorusado);
										
										double totalGrupo = Double.parseDouble(mpdir.get("total"));
										logger.debug("Concepto base totalGrupo       : {} ",totalGrupo);
										totalGrupo += valorusado;
										logger.debug("Sumatoria totalGrupo           : {} ",totalGrupo);
										mpdir.put("total",totalGrupo+"");
										
										double totalGrupoCedular = Double.parseDouble(mpdir.get("totalcedular"));
										logger.debug("Concepto base totalGrupoCedular  : {} ",totalGrupoCedular);
										totalGrupoCedular += cedularaplicado;
										logger.debug("Sumatoria totalGrupoCedular      : {} ",totalGrupoCedular);
										mpdir.put("totalcedular",totalGrupoCedular+"");
										
										concepto.putAll(row);
										logger.debug("<<PAGO DIRECTO DIFERENTE A HOSPITALIZACION Y AYUDA DE MATERNIDAD");
									}
								}
							}//FIN DE CONCEPTOS
							aseguradoObj.put("conceptosAsegurado", conceptosxSiniestro);
							
							//hospitalizacion
							// logger.debug("######  HOSPITALIZACI�N Y AYUDA DE MATERNIDAD WS ######");
							if(tipoFormatoCalculo.equalsIgnoreCase("1")){
								logger.debug("--->>>> WS del siniestro iterado Hospitalizacion y Ayuda de Maternidad");
								logger.debug("deducible siniestro iterado     : {} ",sDeducibleSiniestroIte);
								logger.debug("copago siniestro iterado        : {} ",sCopagoSiniestroIte);
								logger.debug("tipo copago siniestro iterado   : {} ",tipoCopagoSiniestroIte);
								
								logger.debug("Hospitalizacion Importe    : {} ",hosp.get("PTIMPORT"));
								logger.debug("Hospitalizacion Descuento  : {} ",hosp.get("DESTO"));
								logger.debug("Hospitalizacion IVA        : {} ",hosp.get("IVA"));
								logger.debug("Hospitalizacion Deducible  : {} ",deducibleSiniestroIte);
								double hPTIMPORT = Double.valueOf(hosp.get("PTIMPORT"));
								double DESTOIMP  = Double.valueOf(hosp.get("DESTO"));
								double hIVA      = Double.valueOf(hosp.get("IVA"));
								
								String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
								double subttDesto =0d;
								
								if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){ // Diferente de Accidente
									subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP + deducibleSiniestroIte );
								}else{
									if(cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) ||cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
										subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP + deducibleSiniestroIte );
									}else{
										subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP);
									}
								}
								
								if(StringUtils.isNotBlank(tipoCopagoSiniestroIte)) {
									if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){ //Diferente de Accidente
										/*if(sonSesiones.equalsIgnoreCase("1")){
											copagoAplicadoSiniestroIte = DESTOIMP;
										}else{
											copagoAplicadoSiniestroIte = Double.parseDouble(penalizacionPesos) + (subttDesto * ( Double.parseDouble(penalizacionPorcentaje) / 100d ));
										}*/
										copagoAplicadoSiniestroIte = Double.parseDouble(penalizacionPesos) + (subttDesto * ( Double.parseDouble(penalizacionPorcentaje) / 100d ));
									}else{
										copagoAplicadoSiniestroIte= 0d;
									}
								}
								
								importeSiniestroIte = subttDesto - copagoAplicadoSiniestroIte;
								//Cambiarlo cuando nos del el Vo.Bo. de Siniestros
								double hIVADesCopago  = Double.valueOf(hosp.get("IVA"));
								//double hIVADesCopago  = importeSiniestroIte*(ivaprov/100d);
								logger.debug("IVA despues de Copago  : {} ",hIVADesCopago);
								
								hosp.put("PTIMPORT_DESCOPAGO" , importeSiniestroIte+"");
								hosp.put("IVA_DESCOPAGO"    , hIVADesCopago+"");
								
								double importeBase = 0d;
								
								importeBase= hPTIMPORT - DESTOIMP;
								if(aplicaIVA.equalsIgnoreCase("S")){
									//SI LOS VALORES SON ANTES DE COPAGO ENTONCES SE QUEDAN IGUALES LOS VALORES DE DESCUENTO, IVA Y PTIMPORT
									if(seleccionAplica.equalsIgnoreCase("D")){ // ANTES DEL COPAGO
										hosp.put("IVA"    , hIVADesCopago+"");
										hosp.put("BASEIVA" , importeSiniestroIte+"");
									}else{
										hosp.put("IVA"    , importeSiniestroIte*(ivaprov/100d)+"");
										hosp.put("BASEIVA" , subttDesto+"");
									}
								}else{
									hosp.put("IVA"    ,0d+"");
									hosp.put("BASEIVA" , subttDesto+"");
								}
								
								//APLICAMOS EL IVA RETENIDO
								if(ivaRetenido.equalsIgnoreCase("S")){
									ivrSiniestroIte = ((2d * Double.parseDouble(hosp.get("IVA")))/3d);
									hosp.put("IVARETENIDO"    , ivrSiniestroIte+"");
								}else{
									ivrSiniestroIte = 0d;
									hosp.put("IVARETENIDO"    , ivrSiniestroIte+"");
								}
								ivaSiniestroIte = Double.parseDouble(hosp.get("IVA"));
								isrSiniestroIte = Double.valueOf(hosp.get("IMPISR"));
								cedSiniestroIte = Double.valueOf(hosp.get("IMPCED"));
								importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroIte)).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroIte)    ).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroIte)    ).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroIte)    ).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_CEDULAR , (new Double(cedSiniestroIte)    ).toString());
								logger.debug("mapa WS siniestro iterado : {} ",importesWSSiniestroIte);
								logger.debug("<<<-- WS del siniestro iterado Hospitalizacion y ayuda de Maternidad");
							}else{
								logger.debug("-->>>> WS del siniestro iterado Diferente de Hospitalizacion y Ayuda de Maternidad ");
								logger.debug("deducible siniestro iterado : {} ",sDeducibleSiniestroIte);
								logger.debug("copago siniestro iterado : {} ",sCopagoSiniestroIte);
								logger.debug("tipo copago siniestro iterado : {} ",tipoCopagoSiniestroIte);
								
								double totalGrupo = Double.valueOf(mpdir.get("total"));
								
								importeSiniestroIte = totalGrupo / ( 1d + ( ivaprov  / 100d ) );
								ivrSiniestroIte = 0d;
								isrSiniestroIte = 0d;
								ivaSiniestroIte = 0d;
								cedSiniestroIte = Double.valueOf(mpdir.get("totalcedular"));
								
								double subttDescuentoSiniestroIte= 0d;
								double subttISRSiniestroIte= 0d;
								double subttcopagototalSiniestroIte=0;
								
								for(Map<String,String>concepto : conceptos) {
									if(concepto.get("CDUNIECO").equals(siniestroIte.get("CDUNIECO"))
											&&concepto.get("CDRAMO").equals(siniestroIte.get("CDRAMO"))
											&&concepto.get("ESTADO").equals(siniestroIte.get("ESTADO"))
											&&concepto.get("NMPOLIZA").equals(siniestroIte.get("NMPOLIZA"))
											&&concepto.get("NMSUPLEM").equals(siniestroIte.get("NMSUPLEM"))
											&&concepto.get("NMSITUAC").equals(siniestroIte.get("NMSITUAC"))
											&&concepto.get("AAAPERTU").equals(siniestroIte.get("AAAPERTU"))
											&&concepto.get("STATUS").equals(siniestroIte.get("STATUS"))
											&&concepto.get("NMSINIES").equals(siniestroIte.get("NMSINIES"))
									){
										subttDescuentoSiniestroIte+= Double.valueOf(concepto.get("SUBTTDESCUENTO"));
										subttcopagototalSiniestroIte+= Double.valueOf(concepto.get("SUBTTCOPAGO"));
										subttISRSiniestroIte+= Double.valueOf(concepto.get("ISRAPLICA"));
										ivaSiniestroIte+= Double.valueOf(concepto.get("IVAAPLICA"));
										ivrSiniestroIte += ((2 * Double.valueOf(concepto.get("IVAAPLICA")))/3);
									}
								}
								
								if(aplicaIVA.equalsIgnoreCase("S")){
									importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(subttcopagototalSiniestroIte)).toString());
									/*if(seleccionAplica.equalsIgnoreCase("D")){
										importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(subttcopagototalSiniestroIte)).toString());
									}else{
										importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(subttDescuentoSiniestroIte)).toString());
									}*/
									if(ivaRetenido.equalsIgnoreCase("S")){
										importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroIte)    ).toString());
									}else{
										importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(0d)    ).toString());
									}
								}else{
									importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE 	  , (new Double(subttcopagototalSiniestroIte)).toString());
									importesWSSiniestroIte.put(IMPORTE_WS_IVR     	  , (new Double(0d)    ).toString());
								}
								
								importesWSSiniestroIte.put(IMPORTE_WS_IVA     		 , (new Double(ivaSiniestroIte)    ).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_ISR     		 , (new Double(subttISRSiniestroIte)    ).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_CEDULAR 		 , (new Double(cedSiniestroIte)    ).toString());
								logger.debug("mapa WS siniestro iterado: {} ",importesWSSiniestroIte);
								logger.debug("<<WS del siniestro iterado Diferente de Hospitalizacion y Ayuda de Maternidad ");
							}
						}
						facturaObj.put("siniestroPD", aseguradosxSiniestro);
				}
			}/***************************** 		P A G O		R E E M B O L S O 		*************************/
			else if(TipoPago.REEMBOLSO.getCodigo().equals(tramite.get("OTVALOR02"))){//TIPO DE PAGO POR REEMBOLSO
				logger.debug("Paso 5.- EL PROCESO DE PAGO REEMBOLSO ");
				double importeSiniestroUnico 	= 0d;
				double ivaSiniestroUnico     	= 0d;
				double ivrSiniestroUnico     	= 0d;
				double isrSiniestroUnico     	= 0d;
				double cedularSiniestroUnico 	= 0d;
				double penalizacionCambioZona 	= 0d;
				double penalizacionCirculoHosp 	= 0d;
				double totalPenalizacion 		= 0d;
				double deducibleFacturaIte      = 0d;
				double cantidadCopagoFacturaIte = 0d;
				double copagoAplicadoFacturaIte = 0d;
				
				Map<String,String> calcxCobe 	= new HashMap<String,String>();
				Map<String,String> penalizacion = new HashMap<String,String>();
				
				List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,null,null);
				logger.debug("Paso 6.- Obtenemos los Siniestros Maestros : {} ",siniestros);
				siniestro  = siniestros.get(0);
				siniestros = null;
				smap2      = siniestro;
				smap3      = new HashMap<String,String>();
				smap3.put("a","a");
				smap.put("PAGODIRECTO","N");
				
				conceptos  = siniestrosManager.P_GET_CONCEPTOS_FACTURA(
						siniestro.get("CDUNIECO"),
						siniestro.get("CDRAMO"),
						siniestro.get("ESTADO"),
						siniestro.get("NMPOLIZA"),
						siniestro.get("NMSUPLEM"),
						siniestro.get("NMSITUAC"),
						siniestro.get("AAAPERTU"),
						siniestro.get("STATUS"),
						siniestro.get("NMSINIES"),
						null,
						siniestro.get("CDTIPSIT"),
						ntramite);
				logger.debug("Paso 7.- Obtenemos los Conceptos de la Factura : {} ",conceptos);
				slist1     = facturasAux;
				
				HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
				paramCobertura.put("pv_ntramite_i",ntramite);
				paramCobertura.put("pv_tipopago_i",tramite.get("OTVALOR02"));
				paramCobertura.put("pv_nfactura_i",null);
				paramCobertura.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
				paramCobertura.put("pv_estado_i",siniestro.get("ESTADO"));
				paramCobertura.put("pv_cdramo_i",siniestro.get("CDRAMO"));
				paramCobertura.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
				paramCobertura.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
				paramCobertura.put("pv_cdgarant_i",null);
				
				List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaListaCoberturaPoliza(paramCobertura);
				logger.debug("Paso 8.- Obtenemos la informacion de la Cobertura : {} ",listaCobertura);
				
				//hospitalizacion
				Map<String,String> hosp = new HashMap<String,String>();
				lhosp.add(hosp);
				hosp.put("PTIMPORT" , "0");
				hosp.put("DESTO"    , "0");
				hosp.put("IVA"      , "0");
				
				//directo
				Map<String,String>mpdir=new HashMap<String,String>();
				mpdir.put("dummy","dummy");
				lpdir.add(mpdir);
				
				Map<String,String>importesWSSiniestroUnico=new HashMap<String,String>();
				importesWSSiniestroUnico.put("cdunieco" , siniestro.get("CDUNIECO"));
				importesWSSiniestroUnico.put("cdramo"   , siniestro.get("CDRAMO"));
				importesWSSiniestroUnico.put("estado"   , siniestro.get("ESTADO"));
				importesWSSiniestroUnico.put("nmpoliza" , siniestro.get("NMPOLIZA"));
				importesWSSiniestroUnico.put("nmsuplem" , siniestro.get("NMSUPLEM"));
				importesWSSiniestroUnico.put("nmsituac" , siniestro.get("NMSITUAC"));
				importesWSSiniestroUnico.put("aaapertu" , siniestro.get("AAAPERTU"));
				importesWSSiniestroUnico.put("status"   , siniestro.get("STATUS"));
				importesWSSiniestroUnico.put("nmsinies" , siniestro.get("NMSINIES"));
				importesWSSiniestroUnico.put("ntramite" , ntramite);
				listaImportesWS.add(importesWSSiniestroUnico);
				
				Map<String,String> facturaIte        = null;
				for(int i = 0; i < facturasAux.size(); i++) {
					deducibleFacturaIte = 0d; //(EGS) Reiniciamos valor en cada iteracion para evitar que se quede con basura
					facturaIte = facturasAux.get(i);
					Map<String,Object>facturaObj=new HashMap<String,Object>();
					facturaObj.putAll(facturaIte);
					this.facturasxSiniestro.add(facturaObj);
					//reembolso
					Map<String,String>mprem=new HashMap<String,String>(0);
					mprem.put("TOTALNETO" , "0");
					mprem.put("SUBTOTAL"  , "0");
					lprem.add(mprem);
					
					String destopor = facturaIte.get("DESCPORC");
					if(StringUtils.isBlank(destopor) || destopor  == null) {
						facturaObj.put("DESCPORC","0");
					}
					String destoimp = facturaIte.get("DESCNUME");
					
					if(StringUtils.isBlank(destoimp)  || destoimp  == null){
						facturaObj.put("DESCNUME","0");
					}
					//Asignacion de las variables principales
					String cdunieco = siniestro.get("CDUNIECO");
					String cdramo   = siniestro.get("CDRAMO");
					String estado   = siniestro.get("ESTADO");
					String nmpoliza = siniestro.get("NMPOLIZA");
					String nmsuplem = siniestro.get("NMSUPLEM");
					String nmsituac = siniestro.get("NMSITUAC");
					String aaapertu = siniestro.get("AAAPERTU");
					String status   = siniestro.get("STATUS");
					String nmsinies = siniestro.get("NMSINIES");
					String cdtipsit = siniestro.get("CDTIPSIT");
					String nfactura = facturaIte.get("NFACTURA");
					
					boolean existeCobertura = false;
					
					Map<String,String>copagoDeducibleFacturaIte =siniestrosManager.obtenerCopagoDeducible(
							ntramite,cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies, nfactura ,tramite.get("OTVALOR02"),cdtipsit);
					
					//1.- Obtenemos la informacion de Autorizacion de Factura
					Map<String,String>autorizacionesFacturaIte = siniestrosManager.obtenerAutorizacionesFactura(
							siniestro.get("CDUNIECO"),
							siniestro.get("CDRAMO"),
							siniestro.get("ESTADO"),
							siniestro.get("NMPOLIZA"),
							siniestro.get("NMSUPLEM"),
							siniestro.get("NMSITUAC"),
							siniestro.get("AAAPERTU"),
							siniestro.get("STATUS"),
							siniestro.get("NMSINIES"),
							facturaIte.get("NFACTURA"));
					facturaObj.put("AUTMEDIC",autorizacionesFacturaIte.get("AUTMEDIC"));
					facturaObj.put("COMMENME",autorizacionesFacturaIte.get("COMMENME"));
					facturaObj.put("AUTRECLA",autorizacionesFacturaIte.get("AUTRECLA"));
					facturaObj.put("COMMENAR",autorizacionesFacturaIte.get("COMMENAR"));
					
					//2.- Obtenemo los datos generales del siniestros
					List<Map<String,String>> informacionGral = siniestrosManager.obtieneDatosGeneralesSiniestro(siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"),
							siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"),siniestro.get("NMSITUAC"), siniestro.get("NMSUPLEM"), siniestro.get("STATUS"), siniestro.get("AAAPERTU"), siniestro.get("NMSINIES") , facturaIte.get("NTRAMITE"));
					
					//3.- Guardamos los valores en calculosPenalizaciones
					Map<String,String>copagoDeducibleSiniestroIte =siniestrosManager.obtenerCopagoDeducible(ntramite, siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"),
							siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSUPLEM"),siniestro.get("NMSITUAC"),
							siniestro.get("AAAPERTU"),siniestro.get("STATUS"),siniestro.get("NMSINIES") ,facturaIte.get("NFACTURA") ,tramite.get("OTVALOR02"),siniestro.get("CDTIPSIT"));
					
					String tipoFormatoCalculo = copagoDeducibleSiniestroIte.get("FORMATOCALCULO");
					String calculosPenalizaciones = copagoDeducibleSiniestroIte.get("PENALIZACIONES");
					calcxCobe.put("tipoFormatoCalculo",""+tipoFormatoCalculo);
					calcxCobe.put("calculosPenalizaciones",""+calculosPenalizaciones);
					datosCoberturaxCal.add(calcxCobe);
					if(informacionGral.size()>0){
						penalizacion.put("causaSiniestro", informacionGral.get(0).get("CDCAUSA"));
						if(informacionGral.get(0).get("CDCAUSA").toString().equalsIgnoreCase(CausaSiniestro.ENFERMEDAD.getCodigo())){
							HashMap<String, Object> paramDatosCo = new HashMap<String, Object>();
							paramDatosCo.put("pv_ntramite_i",ntramite);
							paramDatosCo.put("pv_tipopago_i",tramite.get("OTVALOR02"));
							paramDatosCo.put("pv_nfactura_i",null);
							paramDatosCo.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
							paramDatosCo.put("pv_estado_i",siniestro.get("ESTADO"));
							paramDatosCo.put("pv_cdramo_i",siniestro.get("CDRAMO"));
							paramDatosCo.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
							paramDatosCo.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
							paramDatosCo.put("pv_cdgarant_i",null);
							
							List<CoberturaPolizaVO> listadoCobertura = siniestrosManager.getConsultaCoberturaAsegurado(paramDatosCo);
							//logger.debug("VALOR DE LAS COBERTURAS  P. REEMBOLSO : {} ",listaCobertura);
							for(int j2 = 0 ;j2 < listadoCobertura.size();j2++){
								if(listadoCobertura.get(j2).getCdgarant().toString().equalsIgnoreCase("7EDA")){
									existeCobertura = true;
								}
							}
							logger.debug("existeCobertura : {} ",existeCobertura);
						}
					}else{
						penalizacion.put("causaSiniestro", CausaSiniestro.ENFERMEDAD.getCodigo());
					}
					
					if(tipoFormatoCalculo.equalsIgnoreCase("1")){
						logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						if(calculosPenalizaciones.equalsIgnoreCase("1")){
							HashMap<String, Object> paramExclusion = new HashMap<String, Object>();
							paramExclusion.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
							paramExclusion.put("pv_estado_i",siniestro.get("ESTADO"));
							paramExclusion.put("pv_cdramo_i",siniestro.get("CDRAMO"));
							paramExclusion.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
							paramExclusion.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
							if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo())){
								//--> SALUD VITAL
								//1.- Verificamos si existe exclusion de penalizacion
								existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
								//2.- Obtenemos la penalizaci�n por cambio de Zona
								penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),informacionGral.get(0).get("CIRHOSPI"),
										informacionGral.get(0).get("DSZONAT"),facturaIte.get("CDPRESTA"),siniestro.get("CDRAMO"));
										//penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"), copagoDeducibleFacturaIte.get("LV_PENALIZA"));
								//3.- Obtenemos la penalizaci�n por circulo Hospitalario
								List<ConsultaProveedorVO> medicos = siniestrosManager.getConsultaListaProveedorMedico(TipoPrestadorServicio.MEDICO.getCdtipo(),facturaIte.get("CDPRESTA"));
								penalizacionCirculoHosp = calcularPenalizacionCirculo(informacionGral.get(0).get("CIRHOSPI"), medicos.get(0).getCirculo(),informacionGral.get(0).get("CDCAUSA"),siniestro.get("CDRAMO"));
							}else{
								// --> DIFERENTE DE SALUD VITAL
								penalizacionCambioZona = 0d;
								penalizacionCirculoHosp = 0d;
							}
							
						}
						logger.debug("<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					}
					penalizacion.put("penalizacionCambioZona",""+penalizacionCambioZona);
					penalizacion.put("penalizacionCirculoHosp",""+penalizacionCirculoHosp);
					
					//3.- Obtenemos el total de penalizaci�n
					String calcularTotalPenalizacion = calcularTotalPenalizacion(penalizacionCambioZona,penalizacionCirculoHosp,informacionGral.get(0).get("CDCAUSA"),
							copagoDeducibleFacturaIte.get("COPAGO"),copagoDeducibleFacturaIte.get("TIPOCOPAGO"),
							informacionGral.get(0).get("CDPROVEE"),siniestro.get("CDRAMO"), informacionGral.get(0).get("FEOCURRE"));
					
					penalizacion.put("totalPenalizacionGlobal",""+calcularTotalPenalizacion);
					String penalizacionT[] = calcularTotalPenalizacion.split("\\|");
					penalizacion.put("copagoPorcentajes",penalizacionT[0].toString());
					penalizacion.put("copagoPesos",penalizacionT[1].toString());
					datosPenalizacion.add(penalizacion);
					
					String sDeducibleFacturaIte     = copagoDeducibleFacturaIte.get("DEDUCIBLE").replace(",","");
					String sCopagoFacturaIte        = copagoDeducibleFacturaIte.get("COPAGO").replace(",","");
					String tipoCopagoFacturaIte     = copagoDeducibleFacturaIte.get("TIPOCOPAGO");
					
					//OBTENEMOS LOS VALORES DE PENALIZACION Y COPAGO
					if(StringUtils.isNotBlank(sDeducibleFacturaIte)
							&&(!sDeducibleFacturaIte.equalsIgnoreCase("na"))
							&&(!sDeducibleFacturaIte.equalsIgnoreCase("no"))
					){
						try{
							deducibleFacturaIte = Double.valueOf(sDeducibleFacturaIte);
						}
						catch(Exception ex){
							logger.debug(""
									+ "\n### ERROR ##################################################"
									+ "\n### no es numero deducible: '"+sDeducibleFacturaIte+"' ###"
									+ "\n############################################################"
							);
							deducibleFacturaIte = 0d;
						}
					}
					
					if(StringUtils.isNotBlank(sCopagoFacturaIte)
							&&(!sCopagoFacturaIte.equalsIgnoreCase("na"))
							&&(!sCopagoFacturaIte.equalsIgnoreCase("no"))
					){
						try{
							cantidadCopagoFacturaIte = Double.valueOf(sCopagoFacturaIte);
						}
						catch(Exception ex){
							logger.debug(""
									+ "\n### ERROR ############################################"
									+ "\n### no es numero copago: '"+sCopagoFacturaIte+"' ###"
									+ "\n######################################################"
							);
							cantidadCopagoFacturaIte = 0d;
						}
					}
					
					slist2.add(copagoDeducibleFacturaIte);
					
					Map<String,String>datosProveedor =siniestrosManager.obtenerDatosProveedor(facturaIte.get("CDPRESTA")); 
					slist3.add(datosProveedor);
					double ivaprov=Double.parseDouble(datosProveedor.get("IVA"));
					
					List<Map<String,String>>listaConceptosFactura = new ArrayList<Map<String,String>>();
					llist1.add(listaConceptosFactura);
					for(Map<String,String>concepto : conceptos){
						if(concepto.get("NFACTURA").equals(facturaIte.get("NFACTURA"))){
							listaConceptosFactura.add(concepto);
							logger.debug(">>REEMBOLSO");
							Map<String,String>row=new HashMap<String,String>();
							row.putAll(concepto);
							
							double ptimport = Double.parseDouble(row.get("PTIMPORT"));
							logger.debug("ptimport : {} ",ptimport);
							
							double ajusteaplica = 0d;
							if(StringUtils.isNotBlank(row.get("PTIMPORT_AJUSTADO"))){
								ajusteaplica = Double.parseDouble(row.get("PTIMPORT_AJUSTADO"));
							}
							logger.debug("ajusteaplica : {} ",ajusteaplica);
							
							double subtotal = ptimport-ajusteaplica;
							logger.debug("subtotal : {} ",subtotal);
							row.put("SUBTOTAL",subtotal+"");
							
							double gtotalneto = Double.parseDouble(mprem.get("TOTALNETO"));
							double gsubtotal  = Double.parseDouble(mprem.get("SUBTOTAL"));
							logger.debug("base totalneto : {} ",gtotalneto);
							logger.debug("base subtotal : {} ",gsubtotal);
							gtotalneto += ptimport;
							gsubtotal  += subtotal;
							logger.debug("new totalneto : {} ",gtotalneto);
							logger.debug("new subtotal : {} ",gsubtotal);
							
							mprem.put("TOTALNETO" , gtotalneto + "");
							mprem.put("SUBTOTAL"  , gsubtotal  + "");
							
							concepto.putAll(row);
							logger.debug("<<REEMBOLSO");
							//pago reembolso
						}
					}
					
					//Verificamos la informaci�n del deducible
					if(tipoFormatoCalculo.equalsIgnoreCase("1")){
						logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						//verificamos la causa del siniestro
						String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
						if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
							if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
								//GMMI
								if(existeCobertura == true){
									deducibleFacturaIte = 0d;
								}
							}else{
								deducibleFacturaIte = 0d;
							}
						}
						logger.debug("<<<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					}else{
						logger.debug("--->>>>>>> DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
						if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
							if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
								//GMMI
								if(existeCobertura == true){
									deducibleFacturaIte = 0d;
								}
							}else{
								deducibleFacturaIte = 0d;
							}
						}
						logger.debug("<<<<<<<<--- DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					}
					
					logger.debug(">>Calculando total factura iterada para WS");
					logger.debug("deducible : {} ",deducibleFacturaIte);
					logger.debug("scopago : {} ",sCopagoFacturaIte);
					logger.debug("tipocopago : {} ",tipoCopagoFacturaIte);
					logger.debug("facturaIte.get(DESCPORC) : {} ",facturaIte.get("DESCPORC"));
					double totalFactura  = Double.valueOf(mprem.get("SUBTOTAL"));
					double destoPorFac = 0d;
					double destoImpFac= 0d;
					if(!StringUtils.isBlank(facturaIte.get("DESCPORC")) || !(facturaIte.get("DESCPORC")  == null)){
						destoPorFac = Double.valueOf(facturaIte.get("DESCPORC"));
					}
					if(!StringUtils.isBlank(facturaIte.get("DESCNUME"))  || !(facturaIte.get("DESCNUME")  == null)){
						destoImpFac = Double.valueOf(facturaIte.get("DESCNUME"));
					}
					//double destoPorFac   = Double.valueOf(facturaIte.get("DESCPORC"));
					//double destoImpFac   = Double.valueOf(facturaIte.get("DESCNUME"));
					double destoAplicado = (totalFactura*(destoPorFac/100d)) + destoImpFac;
					logger.debug("subtotal : {} ",totalFactura);
					totalFactura -= destoAplicado;
					logger.debug("subtotal desto : {} ",totalFactura);
					totalFactura -= deducibleFacturaIte;
					logger.debug("subtotal deducible : {} ",totalFactura);
					
					if(StringUtils.isNotBlank(tipoCopagoFacturaIte)){
						String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
						//if(facturaIte.get("CDGARANT").equalsIgnoreCase("18HO")||facturaIte.get("CDGARANT").equalsIgnoreCase("18MA"))
						if(tipoFormatoCalculo.equalsIgnoreCase("1")){
							logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
							//verificamos la causa del siniestro
							if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
								//Diferente de accidente
								
								logger.debug("Valor 1 ==>:{}",Double.parseDouble(penalizacionT[1].toString()));
								logger.debug("Valor 2 ==>:{}",(totalFactura * ( Double.parseDouble(penalizacionT[0].toString()) / 100d )));
								
								
								copagoAplicadoFacturaIte = Double.parseDouble(penalizacionT[1].toString()) + (totalFactura * ( Double.parseDouble(penalizacionT[0].toString()) / 100d ));
							}else{
								copagoAplicadoFacturaIte = 0d;
							}
							logger.debug("<<<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						}else{
							logger.debug("--->>>>>>> DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
							//COBERTURA DIFERENTE HOSPITALIZACI�N Y AYUDA DE MATERNIDAD
							if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
								if(tipoCopagoFacturaIte.equalsIgnoreCase("$")){
									copagoAplicadoFacturaIte = cantidadCopagoFacturaIte;
								}
								if(tipoCopagoFacturaIte.equalsIgnoreCase("%")){
									copagoAplicadoFacturaIte = totalFactura * ( cantidadCopagoFacturaIte / 100d );
								}
							}else{
								copagoAplicadoFacturaIte = 0d;
							}
							logger.debug("<<<<<<<<--- DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						}
					}
					
					logger.debug("Valor copagoAplicadoFacturaIte ==>>>>:{}",copagoAplicadoFacturaIte);
					
					totalFactura -= copagoAplicadoFacturaIte;
					facturaObj.put("TOTALFACTURAIND",totalFactura+"");
					logger.debug("total copago (final) : {} ",totalFactura);
					logger.debug("<<Calculando total factura iterada para WS");
					
					importeSiniestroUnico += totalFactura;
				}
				
				logger.debug(">>WS del siniestro unico");
				importesWSSiniestroUnico.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroUnico)).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_CEDULAR , (new Double(cedularSiniestroUnico)).toString());
				logger.debug("mapa WS siniestro unico : {} ",importesWSSiniestroUnico);
				logger.debug("<<WS del siniestro unico");
				
			}else{
				/***************************** 		P A G O		I N D E M N I Z A C I O N 		*************************/
				logger.debug("Paso 5.- EL PROCESO DE PAGO DE INDEMNIZACION");
				
				List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,null,null);
				logger.debug("Paso 6.- Obtenemos el valor del Siniestro para el pago : {} ",siniestros.get(0));
				
				siniestro  = siniestros.get(0);
				siniestros = null;
				smap2      = siniestro;
				smap3      = new HashMap<String,String>();
				smap3.put("a","a");
				smap.put("PAGODIRECTO","N");
				slist1     = facturasAux;
				logger.debug("FACTURAS : {} ",slist1);
				
				HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
				paramCobertura.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
				paramCobertura.put("pv_estado_i",siniestro.get("ESTADO"));
				paramCobertura.put("pv_cdramo_i",siniestro.get("CDRAMO"));
				paramCobertura.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
				paramCobertura.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
				paramCobertura.put("pv_cdgarant_i",null);
				
				//hospitalizacion
				Map<String,String> hosp = new HashMap<String,String>();
				lhosp.add(hosp);
				hosp.put("PTIMPORT" , "0");
				hosp.put("DESTO"    , "0");
				hosp.put("IVA"      , "0");
				
				//directo
				Map<String,String>mpdir=new HashMap<String,String>();
				mpdir.put("dummy","dummy");
				lpdir.add(mpdir);
				
				Map<String,String>importesWSSiniestroUnico=new HashMap<String,String>();
				importesWSSiniestroUnico.put("cdunieco" , siniestro.get("CDUNIECO"));
				importesWSSiniestroUnico.put("cdramo"   , siniestro.get("CDRAMO"));
				importesWSSiniestroUnico.put("estado"   , siniestro.get("ESTADO"));
				importesWSSiniestroUnico.put("nmpoliza" , siniestro.get("NMPOLIZA"));
				importesWSSiniestroUnico.put("nmsuplem" , siniestro.get("NMSUPLEM"));
				importesWSSiniestroUnico.put("nmsituac" , siniestro.get("NMSITUAC"));
				importesWSSiniestroUnico.put("aaapertu" , siniestro.get("AAAPERTU"));
				importesWSSiniestroUnico.put("status"   , siniestro.get("STATUS"));
				importesWSSiniestroUnico.put("nmsinies" , siniestro.get("NMSINIES"));
				importesWSSiniestroUnico.put("ntramite" , ntramite);
				listaImportesWS.add(importesWSSiniestroUnico);
				double importeSiniestroUnico = 0d;
				double ivaSiniestroUnico     = 0d;
				double ivrSiniestroUnico     = 0d;
				double isrSiniestroUnico     = 0d;
				double cedularSiniestroUnico = 0d;
				
				Map<String,String> facturaIte        = null;
				
				for(int i = 0; i < facturasAux.size(); i++){
					facturaIte = facturasAux.get(i);
					logger.debug("Paso 6.- Factura en proceso : {} Informacion Factura : {} ",i,facturaIte);
					
					Map<String,Object>facturaObj=new HashMap<String,Object>();
					facturaObj.putAll(facturaIte);
					this.facturasxSiniestro.add(facturaObj);
					//reembolso
					Map<String,String>mprem=new HashMap<String,String>(0);
					mprem.put("TOTALNETO" , "0");
					mprem.put("SUBTOTAL"  , "0");
					lprem.add(mprem);
					
					if(siniestro.get("CDRAMO").equalsIgnoreCase(Ramo.RECUPERA.getCdramo())){
						logger.debug("Paso 7.- El Pago a realizar es Recupera");
						
						PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
						datosPol.setCdunieco(siniestro.get("CDUNIECO"));
						datosPol.setCdramo(siniestro.get("CDRAMO"));
						datosPol.setEstado(siniestro.get("ESTADO"));
						datosPol.setNmpoliza(siniestro.get("NMPOLIZA"));
						
						logger.debug("Dato para consulta : {}",datosPol);
						List<ConsultaDatosGeneralesPolizaVO> lista = consultasAseguradoManager.obtieneDatosPoliza(datosPol);
						String feEfecto = lista.get(0).getFeefecto();
						logger.debug("Paso 8.- Obtenemos la fecha Efecto de la Poliza : {} ",feEfecto);
						
						
						List<Map<String,String>>listaRecupera = siniestrosManager.obtieneInformacionRecupera(siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"),
								siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"),siniestro.get("NMSUPLEM"), siniestro.get("NMSITUAC"),
								renderFechas.parse(feEfecto),facturaIte.get("NTRAMITE"), facturaIte.get("NFACTURA"));
						logger.debug("Valor de Respusta listaRecupera --> :{} , {}",listaRecupera, listaRecupera.size());
					
						double totalFacRec = 0d;
						for(int k = 0; k < listaRecupera.size(); k++){
							logger.debug("listaRecupera -->  {}",listaRecupera.get(k).get("PTIMPORT"));
							totalFacRec += Double.parseDouble(listaRecupera.get(k).get("PTIMPORT")+"");
						}
						double totalFactura = totalFacRec;
						facturaObj.put("TOTALFACTURAIND",totalFactura+"");
						importeSiniestroUnico += totalFactura;
					}else{
						Map<String,String>copagoDeducibleFacturaIte =siniestrosManager.obtenerCopagoDeducible(
								ntramite, siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"), siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSUPLEM"), siniestro.get("NMSITUAC"),
								siniestro.get("AAAPERTU"), siniestro.get("STATUS"), siniestro.get("NMSINIES"), facturaIte.get("NFACTURA"),tramite.get("OTVALOR02"),siniestro.get("CDTIPSIT"));
						logger.debug("copagoDeducibleFacturaIte : {} ",copagoDeducibleFacturaIte);
						
						Map<String,String>rentaDiariaxHospitalizacion =siniestrosManager.obtenerRentaDiariaxHospitalizacion(
								siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"), siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSITUAC"), siniestro.get("NMSUPLEM"));
						
						double sumaAsegurada = Double.valueOf(rentaDiariaxHospitalizacion.get("OTVALOR"));
						double beneficioMax = Double.parseDouble(copagoDeducibleFacturaIte.get("BENEFMAX").replace("%",""));
						//	OBTENEMOS EL VALOR Y SE REALIZA UN REEMPLA
						Calendar fechas = Calendar.getInstance();
						
						int diasPantalla = 0;
						String fechaEgreso  = facturaIte.get("FFACTURA");
						if(facturaIte.get("FEEGRESO").length() > 0){
							diasPantalla = Integer.parseInt(facturaIte.get("DIASDEDU"));
							fechaEgreso = facturaIte.get("FEEGRESO");
						}
						//	fecha inicio
						Calendar fechaInicio = new GregorianCalendar();
						fechaInicio.set(Integer.parseInt(facturaIte.get("FFACTURA").substring(6,10)), Integer.parseInt(facturaIte.get("FFACTURA").substring(3,5)), Integer.parseInt(facturaIte.get("FFACTURA").substring(0,2)));
						Calendar fechaFin = new GregorianCalendar();
						fechaFin.set(Integer.parseInt(fechaEgreso.substring(6,10)), Integer.parseInt(fechaEgreso.substring(3,5)), Integer.parseInt(fechaEgreso.substring(0,2)));
						fechas.setTimeInMillis(fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());
						int totalDias = fechas.get(Calendar.DAY_OF_YEAR);
						
						double totalFactura = sumaAsegurada * (beneficioMax/100d) * (totalDias - diasPantalla);
						facturaObj.put("TOTALFACTURAIND",totalFactura+"");
						importeSiniestroUnico += totalFactura;	
					}
				}
				
				logger.debug(">>WS del siniestro unico");
				importesWSSiniestroUnico.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroUnico)).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroUnico)    ).toString());
				importesWSSiniestroUnico.put(IMPORTE_WS_CEDULAR , (new Double(cedularSiniestroUnico)).toString());
				logger.debug("mapa WS siniestro unico : {} ",importesWSSiniestroUnico);
				logger.debug("<<WS del siniestro unico");
			}
			
			if(conceptos!=null&&conceptos.size()>0){
				logger.debug("conceptos[0] :{}",conceptos);
			}
			//	aqui verificamos toda la informaci�n del WS
			logger.debug("###VALOR A IMPRIMIR#####");
			logger.debug("listaImportesWS :{}",listaImportesWS);
			logger.debug("Tipo de pago :{}",esPagoDirecto);
			logger.debug("facturasxSiniestro :{}",facturasxSiniestro);
			try{
				int nmsecsin= 1;
				for(Map<String,String> importe : listaImportesWS){
					logger.debug("Valor de Importe :{}",importe);
					String cduniecoIte = importe.get("cdunieco");
					String cdramoIte   = importe.get("cdramo");
					String estadoIte   = importe.get("estado");
					String nmpolizaIte = importe.get("nmpoliza");
					String nmsuplemIte = importe.get("nmsuplem");
					String nmsituacIte = importe.get("nmsituac");
					String aaapertuIte = importe.get("aaapertu");
					String statusIte   = importe.get("status");
					String nmsiniesIte = importe.get("nmsinies");
					String ntramiteIte = importe.get("ntramite");
					String importeIte  = importe.get("importe");
					String ivaIte      = importe.get("iva");
					String ivrIte      = importe.get("ivr");
					String isrIte      = importe.get("isr");
					String cedularIte  = importe.get("cedular");
					siniestrosManager.movTimpsini(
							Constantes.INSERT_MODE
							,cduniecoIte
							,cdramoIte
							,estadoIte
							,nmpolizaIte
							,nmsuplemIte
							,nmsituacIte
							,aaapertuIte
							,statusIte
							,nmsiniesIte
							,ntramiteIte
							,importeIte
							,ivaIte
							,ivrIte
							,isrIte
							,cedularIte
							,false
							,nmsecsin+"");
						nmsecsin++;
				}
				logger.debug("VALORES DE LAS FACTURAS---->");
				logger.debug("Total de registros :{}",facturasxSiniestro.size());
				logger.debug("facturasxSiniestro :{}",facturasxSiniestro);
				int nmSecSinFac= 1;
				for(Map<String, Object> totalFacturaIte : facturasxSiniestro){
					logger.debug("VALOR DE LAS FACTURAS :{}",totalFacturaIte);
					String ntramiteA     = (String) totalFacturaIte.get("NTRAMITE");
					String nfacturaA     = (String) totalFacturaIte.get("NFACTURA");
					String totalFacturaA = (String) totalFacturaIte.get("TOTALFACTURAIND");
					siniestrosManager.guardarTotalProcedenteFactura(ntramiteA,nfacturaA,totalFacturaA, nmSecSinFac+"");
					nmSecSinFac++;
					try{
						HashMap<String, Object> paramsPagoDirecto = new HashMap<String, Object>();
						paramsPagoDirecto.put("pv_ntramite_i",ntramiteA);
						String montoTramite = siniestrosManager.obtieneMontoTramitePagoDirecto(paramsPagoDirecto);
						
						logger.debug("Valor del Monto del Arancel ===>>>> "+montoTramite);
						Map<String,Object> otvalor = new HashMap<String,Object>();
						otvalor.put("pv_ntramite_i" , ntramiteA);
						otvalor.put("pv_otvalor03_i"  , montoTramite);
						siniestrosManager.actualizaOTValorMesaControl(otvalor);
					}catch(Exception ex){
						logger.debug("error al actualizar el importe de la mesa de control : {}", ex.getMessage(), ex);
					}
					
				}
				success = true; 
				mensaje = "Datos guardados";
			}catch(Exception ex){
				logger.error("Error al guardaar calculos : {}", ex.getMessage(), ex);
				success = false;
				mensaje = ex.getMessage();
			}
		}catch(Exception ex){
			logger.debug("error al cargar pantalla de calculo de siniestros : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}
	
	
	/**
	* Funcion para guardar la cobertura  x factura
	* @param params
	* @return Exito se guarda la cobertura para la factura en especifica
	*/
	public String guardarCoberturaxFactura(){
		logger.debug("Entra a guardarCoberturaxFactura params de entrada :{}",params);
		try{
			String                   ntramite  = params.get("ntramite");
			String                   nfactura  = params.get("nfactura");
			Date                     fefactura = renderFechas.parse(params.get("ffactura"));
			String                   cdtipser  = params.get("cdtipser");
			String                   cdpresta  = params.get("cdpresta");
			String                   ptimport  = params.get("ptimport");
			String                   descporc  = params.get("descporc");
			String                   descnume  = params.get("descnume");
			String                   cdmoneda  = params.get("cdmoneda");
			String                   tasacamb  = params.get("tasacamb");
			String                   ptimporta = params.get("ptimporta");
			String                   dctonuex =  params.get("dctonuex");
			String                   cdgarant =  params.get("cdgarant");
			String                   cdconval =  params.get("cdconval");
			String                   tipoAccion =  params.get("tipoAccion");
			String                   cdramo     =  params.get("cdramo");
			String                   nombProv   =  params.get("nombProv");
			
			logger.debug("VALOR DE tipoAccion --->>>> :{}",tipoAccion);
			siniestrosManager.guardaListaFacturaSiniestro(ntramite, nfactura, fefactura, cdtipser, cdpresta, ptimport, 
					cdgarant, cdconval, descporc, descnume,cdmoneda,tasacamb,ptimporta,dctonuex,null,null,nombProv,tipoAccion, null);
			//Si el ramo es Recupera se inserta en la tabla de MRECUPERA
			if(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo())){
				logger.debug("Entra al recupera --->>>>> ");
				siniestrosManager.guardaListaRecupera(ntramite,nfactura, cdgarant, cdconval, "100","0.00",tipoAccion);
			}
			success = true;
			mensaje = "Cobertura y subcobertura modificada";
		}
		catch(Exception ex){
			success=false;
			logger.error("error al seleccionar la cobertura : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para guardar la cobertura
	* @param params
	* @return Exito se guarda la cobertura
	*/
	public String guardarSeleccionCobertura(){
		logger.debug("Entra a guardarSeleccionCobertura params de entrada :{}",params);
		try{
			String cdramo   = params.get("cdramo");
			String cdtipsit = params.get("cdtipsit");
			String cdgarant = params.get("cdgarant");
			String cdconval = params.get("cdconval");
			String ntramite = params.get("ntramite");
			Map<String,Object> otvalor = new HashMap<String,Object>();
			otvalor.put("pv_ntramite_i" , ntramite);
			otvalor.put("pv_cdramo_i"   , cdramo);
			otvalor.put("pv_cdtipsit_i" , cdtipsit);
			otvalor.put("pv_otvalor12_i"  , cdgarant);
			otvalor.put("pv_otvalor14_i"  , cdconval);
			siniestrosManager.actualizaOTValorMesaControl(otvalor);
			List<Map<String,String>> facturas  = siniestrosManager.obtenerFacturasTramite(ntramite);
			for(Map<String,String>factura:facturas){
				String	nfactura  = factura.get("NFACTURA");
				Date	fefactura = renderFechas.parse(factura.get("FFACTURA"));
				String	cdtipser  = factura.get("CDTIPSER");
				String	cdpresta  = factura.get("CDPRESTA");
				String	ptimport  = factura.get("PTIMPORT");
				String	descporc  = factura.get("DESCPORC");
				String	descnume  = factura.get("DESCNUME");
				String	cdmoneda  = factura.get("CDMONEDA");
				String	tasacamb  = factura.get("TASACAMB");
				String	ptimporta = factura.get("PTIMPORTA");
				String	dctonuex  = factura.get("DCTONUEX");
				Date	feegreso  = renderFechas.parse(factura.get("FEEGRESO"));
				String	diasdedu  = factura.get("DIASDEDU");
				String	nombprov  = factura.get("NOMBPROV");
				siniestrosManager.guardaListaFacturaSiniestro(ntramite, nfactura, fefactura, cdtipser, cdpresta, 
						ptimport, cdgarant, cdconval, descporc, descnume,cdmoneda,tasacamb,ptimporta,dctonuex,
						feegreso,diasdedu,nombprov,null, null);
			}
			success = true;
			mensaje = "Tr\u00e1mite actualizado";
		}
		catch(Exception ex) {
			success=false;
			logger.error("error al seleccionar la cobertura : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion para guardar la cobertura en especifico de acuerdo al tramite
	* @param params
	* @return Exito se guarda la cobertura para la factura en especifica
	*/
	public String guardarSeleccionCoberturaxTramite(){
		logger.debug("Entra a guardarSeleccionCoberturaxTramite params de entrada :{}",params);
		try{
			String cdramo   = params.get("cdramo");
			String cdtipsit = params.get("cdtipsit");
			String cdgarant = params.get("cdgarant");
			String cdconval = params.get("cdconval");
			String ntramite = params.get("ntramite");
			Map<String,Object> otvalor = new HashMap<String,Object>();
			otvalor.put("pv_ntramite_i" , ntramite);
			otvalor.put("pv_cdramo_i"   , cdramo);
			otvalor.put("pv_cdtipsit_i" , cdtipsit);
			otvalor.put("pv_otvalor12_i"  , cdgarant);
			otvalor.put("pv_otvalor14_i"  , cdconval);
			siniestrosManager.actualizaOTValorMesaControl(otvalor);
			success = true;
			mensaje = "Tr\u00e1mite actualizado";
		}
		catch(Exception ex){
			success=false;
			logger.error("error al seleccionar la cobertura : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}

	/**
	* Funcion para obtener los afiliados afectados
	* @param params
	* @return listado de los afiliados afectados
	*/
	public String afiliadosAfectadosConsulta(){
		logger.debug("Entra a afiliadosAfectadosConsulta params de entrada :{}",params);
		try{
			slist1 = siniestrosManager.listaAseguradosTramite(params.get("ntramite"), params.get("nfactura"), params.get("tipoProceso"));
			logger.debug("Total de registros : {}",slist1.size());
		}catch(Exception ex){
			logger.error("error al cargar pantalla de asegurados afectados : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}
	
	/**
	 * Funcion para obtener el periodo de espera
	 * @param params
	 * @return Exito los periodo de espera en dias 
	 */
	public String obtenerPeriodoEspera(){
		logger.debug("Entra a obtenerPeriodoEspera params de entrada :{}",params);
		try{
			
			PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
			datosPol.setCdunieco(params.get("cdunieco"));
			datosPol.setCdramo(params.get("cdramo"));
			datosPol.setEstado(params.get("estado"));
			datosPol.setNmpoliza(params.get("nmpoliza"));
			
			List<ConsultaDatosGeneralesPolizaVO> lista = consultasAseguradoManager.obtieneDatosPoliza(datosPol);
			String feEfecto = lista.get(0).getFeefecto();
			logger.debug("Paso 1.- Obtenemos la fecha Efecto de la Poliza : {}", feEfecto);
			
			List<Map<String,String>> datosAdicionales = siniestrosManager.listaSumaAseguradaPeriodoEsperaRec(params.get("cdramo"),params.get("cdgarant"),params.get("cdconval"),renderFechas.parse(feEfecto));
			double plazoEsperaCobertura = Double.parseDouble(datosAdicionales.get(0).get("PLAZOESPERA"));
			logger.debug("Paso 2.- Obtenemos el plazo de espera : {}", plazoEsperaCobertura);
			
			List<Map<String,String>> periodoEsperaAsegurado = siniestrosManager.listaPeriodoEsperaAsegurado(params.get("cdunieco"), params.get("cdramo"),params.get("estado"),
															params.get("nmpoliza"), params.get("nmsituac"),renderFechas.parse(params.get("feocurre")));
			double diasAsegurado = Double.parseDouble(periodoEsperaAsegurado.get(0).get("DIAS"));
			logger.debug("Paso 3.- Obtenemos el plazo de espera Asegurado : {}", diasAsegurado);
			
			if(diasAsegurado >= plazoEsperaCobertura){
				mensaje = null;
				success = true;
			}else{
				mensaje = "La intervenci\u00f3n quir\u00fargica  no cubre con el periodo de espera : "+datosAdicionales.get(0).get("PLAZOESPERA")+" d\u00edas";
				success = false;
			}
			logger.debug("Paso 4.- Mesaje : {}", mensaje);
			
		}
		catch(Exception ex){
			success=false;
			logger.error("error al obtener el periodo de espera : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener los montos de los pagos del siniestro
	* @param ntramite
	* @return el monto total de las facturas y el monto de los pagos del Siniestro
	*/
	public String obtieneMontoPagoSiniestro(){
		logger.debug("Entra a obtieneMontoPagoSiniestro params de entrada :{}",params);
		try {
			mensaje ="";
			double impFactura = 0d;
			double impxpagar = 0d;
			
			String ntramite = params.get("ntramite");
			String cdramo = params.get("cdramo");
			String tipoPago = params.get("tipoPago");
			if(cdramo.equalsIgnoreCase(Ramo.RECUPERA.getCdramo())){ // Recupera
				mensaje = "";
				success = true;
			}else{
				if(!tipoPago.equalsIgnoreCase(TipoPago.INDEMNIZACION.getCodigo())){
					datosInformacionAdicional = siniestrosManager.obtieneMontoPagoSiniestro(params.get("ntramite"));
					logger.debug("Montos : {}", datosInformacionAdicional);
					if(datosInformacionAdicional.get(0).get("IMPORTEFACTURA")!=null){
						impFactura = Double.parseDouble(datosInformacionAdicional.get(0).get("IMPORTEFACTURA"));
					}
					if(datosInformacionAdicional.get(0).get("MONTOXPAGAR")!=null){
						impxpagar = Double.parseDouble(datosInformacionAdicional.get(0).get("MONTOXPAGAR"));
					}
				}
				if(impFactura >= impxpagar){
					//validamos la informaci�n del cdpresta
					List<Map<String,String>> facturasAux = siniestrosManager.obtenerFacturasTramite(params.get("ntramite"));
					boolean provPendiente = true;
					logger.debug("Valor : {}", provPendiente);
					for(int i = 0; i < facturasAux.size(); i++)
	    			{
						Map<String,String> infProv= siniestrosManager.obtenerDatosProveedor(facturasAux.get(i).get("CDPRESTA"));
						if(infProv.get("CDPRESTA").equalsIgnoreCase("0") || infProv.get("IDPROVEEDOR").equalsIgnoreCase("0")){
							provPendiente = false;
						}
	    			}
					
					if(provPendiente){ //true
						mensaje = "";
						success = true;
					}else{
						if(TipoPago.REEMBOLSO.getCodigo().equalsIgnoreCase(tipoPago)){
							mensaje = "";
							success = true;
						}else{
							mensaje = "Proveedor pendiente o la clave del proveedor es 0 - Favor de configurar la informaci\u00f3n.";
							success = false;
						}
					}
				}else{
					mensaje = "Verifica los C\u00e1lculos - El importe total de las facturas es menor al total a pagar.";
					// (EGS) Si diferencia es menor o igual a 50 centavos, mostrar diferencia
					if (impxpagar - impFactura <= 0.5){
						StringBuilder diferencia = new StringBuilder();
						mensaje = diferencia.append(mensaje).append(" La diferencia es: ").append(impxpagar - impFactura).toString();
					}
					// (EGS) fin
					success = false;
				}
			}
		}catch( Exception e){
			logger.error("Error en el metodo obtieneMontoPagoSiniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		//success = true;
		return SUCCESS;
	}
	

	public String calculoSiniestros(){
		logger.debug("Entra a calculoSiniestros params de entrada :{}",params);
		try{
			GeneradorCampos    gc			= new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			String             pantalla		= "CALCULO_SINIESTRO";
			String             seccion		= null;
			String             ntramite		= params.get("ntramite");
			List<ComponenteVO> componentes	= null;
			UserVO             usuario		= (UserVO)session.get("USUARIO");
			String             cdrol		= usuario.getRolActivo().getClave();
			
			Map<String,String> factura        = null;
			Map<String,String> siniestroIte   = null;
			Map<String,String> proveedor      = null;
			Map<String,String> siniestro      = null;
			List<Map<String,String>>conceptos = null;
			
			slist2                  		  = new ArrayList<Map<String,String>>();
			slist3                  		  = new ArrayList<Map<String,String>>();
			llist1                  		  = new ArrayList<List<Map<String,String>>>();
			lhosp                   		  = new ArrayList<Map<String,String>>();
			lpdir                   		  = new ArrayList<Map<String,String>>();
			lprem                   		  = new ArrayList<Map<String,String>>();
			datosPenalizacion       		  = new ArrayList<Map<String,String>>();
			datosCoberturaxCal      		  = new ArrayList<Map<String,String>>();
			listaImportesWS         		  = new ArrayList<Map<String,String>>();
			
			this.facturasxSiniestro=new ArrayList<Map<String,Object>>();
			imap = new HashMap<String,Item>();
			Map<String,String> tramite     = siniestrosManager.obtenerTramiteCompleto(ntramite);
			logger.debug("Paso 1.- Tramite : {}",tramite);
			smap = tramite;
			List<Map<String,String>> facturasAux = siniestrosManager.obtenerFacturasTramite(ntramite);
			logger.debug("Paso 2.- Listado Factura : {}",facturasAux.size());
			if(tramite==null||facturasAux==null){
				throw new Exception("No se encontro tramite/facturas para el tramite");
			}
			
			/*siniestrosManager.movTimpsini(Constantes.DELETE_MODE, null, null, null, null,
					null, null, null, null, null,
					ntramite, null, null, null, null, null, false);*/
			//logger.debug("Paso 3.- Eliminacion de TIMPSINI");
			boolean esPagoDirecto 			  = false;
			//4.- Verificamos el tipo de pago
			if(TipoPago.DIRECTO.getCodigo().equals(tramite.get("OTVALOR02"))){
				esPagoDirecto = true;
			}
			logger.debug("Paso 4.- Es pago Directo : {} ",esPagoDirecto);
			
			/***************************** 		P A G O		D I R E C T O  		*************************/
			if(TipoPago.DIRECTO.getCodigo().equals(tramite.get("OTVALOR02"))) {
				logger.debug("Paso 5.- EL PROCESO DE PAGO ES DIRECTO ");
				smap.put("PAGODIRECTO","S");
				smap2     = facturasAux.get(0);
				/*proveedor = siniestrosManager.obtenerDatosProveedor(facturasAux.get(0).get("CDPRESTA"));
				logger.debug("Paso 6.- Datos del Proveedor : {}",proveedor);
				smap3     = proveedor;
				double ivaprov = Double.parseDouble(proveedor.get("IVA")); 
				double cedprov = Double.parseDouble(proveedor.get("CEDULAR"));
				double isrprov = Double.parseDouble(proveedor.get("ISR"))*/
				
				for(int i = 0; i < facturasAux.size(); i++){
					this.aseguradosxSiniestro = new ArrayList<Map<String,Object>>();
					factura = facturasAux.get(i);
					logger.debug("Paso 7.- Recorremos las Facturas  - El proceso i : {} de la factura : {}",i,factura.get("NFACTURA"));

					proveedor = siniestrosManager.obtenerDatosProveedor(factura.get("CDPRESTA"));
					logger.debug("Paso 6.- Datos del Proveedor : {}",proveedor);
					smap3     = proveedor;
					double ivaprov = Double.parseDouble(proveedor.get("IVA"));
					double cedprov = 0d;
					double isrprov = 0d;
					
					if(factura.get("SWISR").equalsIgnoreCase("S")){
						isrprov = Double.parseDouble(proveedor.get("ISR"));
					}
					
					if(factura.get("SWICE").equalsIgnoreCase("S")){
						cedprov = Double.parseDouble(proveedor.get("CEDULAR"));
					}
					
					Map<String,Object>facturaObj = new HashMap<String,Object>();
					facturaObj.putAll(factura);
					this.facturasxSiniestro.add(facturaObj);
					
					List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,factura.get("NFACTURA"),null);
					logger.debug("Paso 8.- Obtenemos los Asegurados (MSINIEST) : {}",siniestros);
					
					conceptos = siniestrosManager.P_GET_CONCEPTOS_FACTURA(null, tramite.get("CDRAMO"), null, null, null, null, null, 
										null, null, factura.get("NFACTURA"),tramite.get("CDTIPSIT"),tramite.get("NTRAMITE"));
					logger.debug("Paso 9.- Obtenemos la informacion de los conceptos de la factura : {}", conceptos);

					for( int j= 0; j < siniestros.size();j++){
						String	aplicaPenalCircHosp		  = "S";
						String	aplicaPenalZonaHosp		  = "S";
						
						logger.debug("Paso 10.- Recorremos los Siniestros - El proceso j : {} Siniestro : {}",j,siniestros.get(j));
						siniestroIte    = siniestros.get(j);
						
						if(StringUtils.isNotBlank(siniestroIte.get("NMAUTSER"))){
							List<AutorizacionServicioVO> lista = siniestrosManager.getConsultaAutorizacionesEsp(siniestroIte.get("NMAUTSER"));
							logger.debug("Paso 10.1.- Verificamos la informacion si tiene Autorizacion de servicio : {} ",lista.size());
							aplicaPenalCircHosp		  = lista.get(0).getAplicaCirHos()+"";
							aplicaPenalZonaHosp		  = lista.get(0).getAplicaZonaHosp()+"";
						}
						
						Map<String,Object>aseguradoObj = new HashMap<String,Object>();
						aseguradoObj.putAll(siniestroIte);
						this.aseguradosxSiniestro.add(aseguradoObj);
						String cdunieco 				  = siniestroIte.get("CDUNIECO");
						String cdramo   				  = siniestroIte.get("CDRAMO");
						String estado   				  = siniestroIte.get("ESTADO");
						String nmpoliza 				  = siniestroIte.get("NMPOLIZA");
						String nmsuplem 				  = siniestroIte.get("NMSUPLEM");
						String nmsituac 				  = siniestroIte.get("NMSITUAC");
						String aaapertu 				  = siniestroIte.get("AAAPERTU");
						String status   				  = siniestroIte.get("STATUS");
						String cdtipsit 				  = siniestroIte.get("CDTIPSIT");
						String nmsinies 				  = siniestroIte.get("NMSINIES");
						String nfactura 				  = factura.get("NFACTURA");
						
						double penalizacionCambioZona 	  = 0d;
						double penalizacionCirculoHosp	  = 0d;
						String aplicaIVA				  = "S";
						String seleccionAplica			  = "D";
						String ivaRetenido				  = "N";
						double deducibleSiniestroIte      = 0d;
						double copagoAplicadoSiniestroIte = 0d;
						double cantidadCopagoSiniestroIte = 0d;
						String penalizacionPesos 		  = "0";
						String penalizacionPorcentaje     = "0";
						boolean existeCobertura           = false;
						
						Map<String,String>autorizacionesFactura = siniestrosManager.obtenerAutorizacionesFactura(
						cdunieco, cdramo, estado, nmpoliza, nmsuplem,
						nmsituac, aaapertu, status, nmsinies, nfactura);
						facturaObj.put("AUTMEDIC"+nmsinies,autorizacionesFactura.get("AUTMEDIC"));
						facturaObj.put("COMMENME"+nmsinies,autorizacionesFactura.get("COMMENME"));
						facturaObj.put("AUTRECLA"+nmsinies,autorizacionesFactura.get("AUTRECLA"));
						facturaObj.put("COMMENAR"+nmsinies,autorizacionesFactura.get("COMMENAR"));
						logger.debug("Paso 11.- Autorizacion de la informacion de la factura : {}", autorizacionesFactura);
						
						List<Map<String,String>> informacionGral = siniestrosManager.obtieneDatosGeneralesSiniestro(cdunieco, cdramo,
									estado, nmpoliza,nmsituac, nmsuplem, status, aaapertu, nmsinies, factura.get("NTRAMITE"));
						logger.debug("Paso 12.- Datos generales del Siniestro : {} ",informacionGral);
						
						if(informacionGral.size()> 0){
							aseguradoObj.put("CAUSASINIESTRO", informacionGral.get(0).get("CDCAUSA"));
							if(cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())||cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
								if(informacionGral.get(0).get("CDCAUSA").toString().equalsIgnoreCase(CausaSiniestro.ENFERMEDAD.getCodigo())){
									HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
									paramCobertura.put("pv_ntramite_i",factura.get("NTRAMITE"));
									paramCobertura.put("pv_tipopago_i",tramite.get("OTVALOR02"));
									paramCobertura.put("pv_nfactura_i",nfactura);
									paramCobertura.put("pv_cdunieco_i",cdunieco);
									paramCobertura.put("pv_estado_i",estado);
									paramCobertura.put("pv_cdramo_i",cdramo);
									paramCobertura.put("pv_nmpoliza_i",nmpoliza);
									paramCobertura.put("pv_nmsituac_i",nmsituac);
									paramCobertura.put("pv_cdgarant_i",null);
									List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaCoberturaAsegurado(paramCobertura);
									logger.debug("Paso 13.- Listado de Coberturas  : {} ",listaCobertura);
									for(int j1 = 0 ;j1 < listaCobertura.size();j1++){
										if(listaCobertura.get(j1).getCdgarant().toString().equalsIgnoreCase("7EDA")){
											existeCobertura = true;
										}
									}
								}
							}
						}else{
							aseguradoObj.put("CAUSASINIESTRO", CausaSiniestro.ENFERMEDAD.getCodigo());
						}
						
						Map<String,String>copagoDeducibleSiniestroIte =siniestrosManager.obtenerCopagoDeducible(
									ntramite,cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies, nfactura,tramite.get("OTVALOR02"), cdtipsit);
						logger.debug("Paso 13.- Informacion Deducible/Copago : {}",copagoDeducibleSiniestroIte);
						
						String tipoFormatoCalculo         = copagoDeducibleSiniestroIte.get("FORMATOCALCULO");
						String calculosPenalizaciones     = copagoDeducibleSiniestroIte.get("PENALIZACIONES");
						
						facturaObj.put("TIPOFORMATOCALCULO",""+tipoFormatoCalculo);
						facturaObj.put("CALCULOSPENALIZACIONES",""+calculosPenalizaciones);
						
						logger.debug("Paso 14.- Aplica Penalizacion : {} ",calculosPenalizaciones);
						if(calculosPenalizaciones.equalsIgnoreCase("1")){
							HashMap<String, Object> paramExclusion = new HashMap<String, Object>();
							paramExclusion.put("pv_cdunieco_i",cdunieco);
							paramExclusion.put("pv_estado_i",estado);
							paramExclusion.put("pv_cdramo_i",cdramo);
							paramExclusion.put("pv_nmpoliza_i",nmpoliza);
							paramExclusion.put("pv_nmsituac_i",nmsituac);
							if(cdramo.equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo()) || cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())|| cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
								logger.debug("Valor de aplicaPenalZonaHosp : {} ",aplicaPenalZonaHosp);
								logger.debug("Valor de aplicaPenalCircHosp : {} ",aplicaPenalCircHosp);
								if(aplicaPenalZonaHosp.equalsIgnoreCase("N")){
									penalizacionCambioZona = 0d;
								}else{
									existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
									penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),informacionGral.get(0).get("CIRHOSPI"),
									informacionGral.get(0).get("DSZONAT"),informacionGral.get(0).get("CDPROVEE"),cdramo);
								}
								/*else{
									existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
									penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),copagoDeducibleSiniestroIte.get("LV_PENALIZA"));
								}*/
								if(aplicaPenalCircHosp.equalsIgnoreCase("N")){
									penalizacionCirculoHosp = 0d;
								}else{
									penalizacionCirculoHosp = calcularPenalizacionCirculo(informacionGral.get(0).get("CIRHOSPI"), informacionGral.get(0).get("CIRHOPROV"),informacionGral.get(0).get("CDCAUSA"), cdramo);
								}
								aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
								aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
							}else{
								aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
								aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
							}
						}else{
							aseguradoObj.put("PENALIZACIONCAMBIOZONA",""+penalizacionCambioZona);
							aseguradoObj.put("PENALIZACIONCIRCULOHOSP",""+penalizacionCirculoHosp);
						}
						logger.debug("Paso 15.- Existe Exclusion de Penalizacion : {} ",existePenalizacion);
						logger.debug("Paso 16.- Penalizacion por Cambio de Zona : {} ",penalizacionCambioZona);
						logger.debug("Paso 17.- Penalizacion por Circulo Hospitalario : {} ",penalizacionCirculoHosp);
						
						String calcularTotalPenalizacion = calcularTotalPenalizacion(penalizacionCambioZona,penalizacionCirculoHosp,informacionGral.get(0).get("CDCAUSA"),
																					copagoDeducibleSiniestroIte.get("COPAGO"),copagoDeducibleSiniestroIte.get("TIPOCOPAGO"),
																					informacionGral.get(0).get("CDPROVEE"),cdramo, informacionGral.get(0).get("FEOCURRE"));
						logger.debug("Paso 18.- Total Penalizacion : {} ",calcularTotalPenalizacion);
						
						aseguradoObj.put("TOTALPENALIZACIONGLOBAL",""+calcularTotalPenalizacion);
						String penalizacionT[] = calcularTotalPenalizacion.split("\\|");
						penalizacionPorcentaje = penalizacionT[0].toString();
						penalizacionPesos = penalizacionT[1].toString();
						aseguradoObj.put("COPAGOPORCENTAJES",penalizacionPorcentaje);
						aseguradoObj.put("COPAGOPESOS",penalizacionPesos);
						
						List<Map<String, String>> listaFactura = siniestrosManager.P_GET_FACTURAS_SINIESTRO(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies, cdtipsit);
						logger.debug("Paso 19.- Informacion de la Factura aplicacion de IVA's : {} ",listaFactura);
						
						if(!listaFactura.isEmpty()){	// (EGS)
							if(listaFactura.get(0).get("APLICA_IVA") != null){
								aplicaIVA       = listaFactura.get(0).get("APLICA_IVA");
								seleccionAplica = listaFactura.get(0).get("ANTES_DESPUES");
								ivaRetenido     = listaFactura.get(0).get("IVARETENIDO");
								if(!StringUtils.isNotBlank(ivaRetenido)){
									ivaRetenido = "N";
								}
							}
						}
						
						String sDeducibleSiniestroIte     = copagoDeducibleSiniestroIte.get("DEDUCIBLE").replace(",","");
						String sCopagoSiniestroIte        = copagoDeducibleSiniestroIte.get("COPAGO").replace(",", "");
						String tipoCopagoSiniestroIte     = copagoDeducibleSiniestroIte.get("TIPOCOPAGO");
						
						if(StringUtils.isNotBlank(sDeducibleSiniestroIte)
							&&(!sDeducibleSiniestroIte.equalsIgnoreCase("na"))
							&&(!sDeducibleSiniestroIte.equalsIgnoreCase("no"))
						){
							try{
								deducibleSiniestroIte = Double.valueOf(sDeducibleSiniestroIte);
							}
							catch(Exception ex){
								logger.debug(""
									+ "\n### ERROR ##################################################"
									+ "\n### no es numero deducible: '"+sDeducibleSiniestroIte+"' ###"
									+ "\n############################################################"
								);
								deducibleSiniestroIte = 0d;
							}
						}
						
						if(cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())||cdramo.toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){//GMMI
							if(existeCobertura == true){
								deducibleSiniestroIte = 0d;
							}
						}
						logger.debug("Paso 20.- Valor de deducibleSiniestroIte : {} ",deducibleSiniestroIte);
						
						if(StringUtils.isNotBlank(sCopagoSiniestroIte)
							&&(!sCopagoSiniestroIte.equalsIgnoreCase("na"))
							&&(!sCopagoSiniestroIte.equalsIgnoreCase("no"))
						){
							try {
									cantidadCopagoSiniestroIte = Double.valueOf(sCopagoSiniestroIte);
							}
							catch(Exception ex){
								logger.debug(""
									+ "\n### ERROR ############################################"
									+ "\n### no es numero copago: '"+sCopagoSiniestroIte+"' ###"
									+ "\n######################################################"
								);
								cantidadCopagoSiniestroIte = 0d;
							}
						}
						logger.debug("Paso 21.- Valor de cantidadCopagoSiniestroIte : {} ",cantidadCopagoSiniestroIte);
						
						Map<String,String>importesWSSiniestroIte=new HashMap<String,String>();
						importesWSSiniestroIte.put("cdunieco" , siniestroIte.get("CDUNIECO"));
						importesWSSiniestroIte.put("cdramo"   , siniestroIte.get("CDRAMO"));
						importesWSSiniestroIte.put("estado"   , siniestroIte.get("ESTADO"));
						importesWSSiniestroIte.put("nmpoliza" , siniestroIte.get("NMPOLIZA"));
						importesWSSiniestroIte.put("nmsuplem" , siniestroIte.get("NMSUPLEM"));
						importesWSSiniestroIte.put("nmsituac" , siniestroIte.get("NMSITUAC"));
						importesWSSiniestroIte.put("aaapertu" , siniestroIte.get("AAAPERTU"));
						importesWSSiniestroIte.put("status"   , siniestroIte.get("STATUS"));
						importesWSSiniestroIte.put("nmsinies" , siniestroIte.get("NMSINIES"));
						importesWSSiniestroIte.put("ntramite" , ntramite);
						listaImportesWS.add(importesWSSiniestroIte);
						double importeSiniestroIte;
						double ivaSiniestroIte;
						double ivrSiniestroIte;
						double isrSiniestroIte;
						double cedSiniestroIte;
						
						//hospitalizacion
						Map<String,String> hosp = new HashMap<String,String>();
						lhosp.add(hosp);
						hosp.put("PTIMPORT" 	, "0");
						hosp.put("DESTO"    	, "0");
						hosp.put("IVA"      	, "0");
						hosp.put("PRECIO"   	, "0");
						hosp.put("DESCPRECIO"   , "0");
						hosp.put("IMPISR"   , "0");
						hosp.put("IMPCED"   , "0");
						//hospitalizacion
						
						//reembolso
						Map<String,String>mprem=new HashMap<String,String>();
						mprem.put("dummy","dummy");
						lprem.add(mprem);
						//remmbolso

						//pago directo
						Map<String,String> mpdir = new HashMap<String,String>();
						mpdir.put("total","0");
						mpdir.put("totalcedular","0");
						mpdir.put("ivaTotalMostrar","0");
						mpdir.put("ivaRetenidoMostrar","0");
						mpdir.put("iSRMostrar","0");
						lpdir.add(mpdir);
						//INICIO DE CONCEPTOS
						this.conceptosxSiniestro = new ArrayList<Map<String,String>>();
						for(int k = 0; k < conceptos.size() ; k++){
							Map<String, String> concepto = conceptos.get(k);
							if(concepto.get("CDUNIECO").equals(siniestroIte.get("CDUNIECO"))
								&&concepto.get("CDRAMO").equals(siniestroIte.get("CDRAMO"))
								&&concepto.get("ESTADO").equals(siniestroIte.get("ESTADO"))
								&&concepto.get("NMPOLIZA").equals(siniestroIte.get("NMPOLIZA"))
								&&concepto.get("NMSUPLEM").equals(siniestroIte.get("NMSUPLEM"))
								&&concepto.get("NMSITUAC").equals(siniestroIte.get("NMSITUAC"))
								&&concepto.get("AAAPERTU").equals(siniestroIte.get("AAAPERTU"))
								&&concepto.get("STATUS").equals(siniestroIte.get("STATUS"))
								&&concepto.get("NMSINIES").equals(siniestroIte.get("NMSINIES"))
							){
								conceptosxSiniestro.add(concepto);
								logger.debug("Datos de los conceptos  contador k: {} ",k);
								logger.debug("Datos de los conceptos  concepto : {}  COPAGO: {} ",concepto,concepto.get("COPAGO"));
								
								if(tipoFormatoCalculo.equalsIgnoreCase("1")) {
									logger.debug("--->>>>>>> HOSPITALIZACION");
									double PTIMPORT    = Double.parseDouble(concepto.get("PTIMPORT"));
									double DESTOPOR    = Double.parseDouble(concepto.get("DESTOPOR"));
									double DESTOIMP    = Double.parseDouble(concepto.get("DESTOIMP"));
									double PTPRECIO    = Double.parseDouble(concepto.get("PTPRECIO")) * Double.parseDouble(concepto.get("CANTIDAD"));
									boolean copagoPorc = false;
									String scopago     = concepto.get("COPAGO");
									
									if(scopago.equalsIgnoreCase("no") ||scopago.equalsIgnoreCase("na")){
										scopago="0";
									}
									logger.debug("Valor de respuesta :{}",StringUtils.isNotBlank(scopago));
									/*if(StringUtils.isNotBlank(scopago)){
										if(scopago.contains("%")){
											copagoPorc = true;
										}
										scopago = scopago.replace("%", "").replace("$", "");
										if(copagoPorc) {
											DESTOPOR = DESTOPOR+Double.valueOf(scopago);
										}
										else{
											DESTOIMP=DESTOIMP+Double.valueOf(scopago);
										}
									}*/
									
									double hPTIMPORT 	= Double.parseDouble(hosp.get("PTIMPORT"));
									double hDESTO    	= Double.parseDouble(hosp.get("DESTO"));
									double hIVA      	= Double.parseDouble(hosp.get("IVA"));
									double hISR      	= Double.parseDouble(hosp.get("IMPISR"));
									double hICED      	= Double.parseDouble(hosp.get("IMPCED"));
									double hPRECIO      = Double.parseDouble(hosp.get("PRECIO"));
									double hDESCPRECIO  = Double.parseDouble(hosp.get("DESCPRECIO"));
									
									hPTIMPORT 	+= PTIMPORT;
									hDESTO    	+= (PTIMPORT*(DESTOPOR/100d)) + (DESTOIMP);
									
									
									//Cuando nos den el VoBo. lo tendremos que decomentar
									if(aplicaIVA.equalsIgnoreCase("S")){
										//verificamos si aplica para el concepto 
										if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
											hIVA      	+= PTIMPORT*(ivaprov/100d);
										}
									}else{
										hIVA      	+= PTIMPORT*(ivaprov/100d);
									}
									//hIVA      	+= PTIMPORT*(ivaprov/100d);
									hISR		+= PTIMPORT*(isrprov/100d);
									hICED		+= PTIMPORT*(cedprov/100d);
									hPRECIO 	+= PTPRECIO;
									hDESCPRECIO += (PTPRECIO*(DESTOPOR/100d)) + (DESTOIMP);
									
									hosp.put("PTIMPORT" , hPTIMPORT+"");
									hosp.put("DESTO"    , hDESTO+"");
									hosp.put("IVA"      , hIVA+"");
									hosp.put("IMPISR"   , hISR+"");
									hosp.put("PRECIO"   , hPRECIO+"");
									hosp.put("DESCPRECIO", hDESCPRECIO+"");
									hosp.put("IMPCED"   , hICED+"");
									
									logger.debug("Concepto importe 			    : {} ",PTIMPORT);
									logger.debug("Concepto desto 			    : {} ",DESTOPOR);
									logger.debug("Concepto destoimp 		    : {} ",DESTOIMP);
									logger.debug("Usando iva proveedor          : {} ",ivaprov);
									logger.debug("Concepto copago               : {} ",scopago);
									logger.debug("Concepto desto + copago %     : {} ",DESTOPOR);
									logger.debug("Concepto destoimp + copago $  : {} ",DESTOIMP);
									
									logger.debug("#### VALORES DEL VECTOR #####");
									logger.debug("Concepto hPTIMPORT total      : {} ",hPTIMPORT);
									logger.debug("Concepto hDESTO total         : {} ",hDESTO);
									logger.debug("Concepto hIVA total           : {} ",hIVA);
									logger.debug("Concepto hISR total 		    : {} ",hISR);
									logger.debug("Concepto hPRECIO total        : {} ",hPRECIO);
									logger.debug("Concepto hDESCPRECIO total    : {} ",hDESCPRECIO);
									logger.debug("Concepto hICED total          : {} ",hICED);
									
									logger.debug("<<<<<<<--- HOSPITALIZACION");
								}else {
									logger.debug("--->>>>>>> PAGO DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD  ---->>>>>>>>>> ");
									double precioArancel   	 = 0d;
									double descuentoPorc   	 = 0d;
									double descuentoImpo   	 = 0d;
									boolean copagoPorc 		 = false;
									double  copago     		 = 0d;
									double  copagoAplicado   = 0d;

									Map<String,String>row = new HashMap<String,String>();
									row.putAll(concepto);

									double cantidad 		= Double.valueOf(row.get("CANTIDAD"));

									if(StringUtils.isNotBlank(row.get("IMP_ARANCEL"))) {
										precioArancel 		= Double.valueOf(row.get("IMP_ARANCEL"));
									}
									row.put("IMP_ARANCEL",precioArancel+"");

									double subtotalArancel 	= cantidad*precioArancel;//++
									row.put("SUBTTARANCEL",subtotalArancel+"");

									if(StringUtils.isNotBlank(row.get("DESTOPOR"))){
										descuentoPorc 		= Double.parseDouble(row.get("DESTOPOR"));
									}

									if(StringUtils.isNotBlank(row.get("DESTOIMP"))){
										descuentoImpo 		= Double.parseDouble(row.get("DESTOIMP"));
									}

									double descuentoAplicado = (subtotalArancel*(descuentoPorc/100d))+descuentoImpo;
									row.put("DESTOAPLICA",descuentoAplicado+"");

									double subtotalDescuento = subtotalArancel-descuentoAplicado;//++
									row.put("SUBTTDESCUENTO",subtotalDescuento+""); // SUBTOTAL A OCUPAR CUANDO SEA ANTES DE COPAGO

									if(aplicaIVA.equalsIgnoreCase("S")){
										if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
											if(seleccionAplica.equalsIgnoreCase("A")){ // ANTES DEL COPAGO
												double iVaaplicaAntes = subtotalDescuento*(ivaprov/100d);
												row.put("IVAAPLICA",iVaaplicaAntes+"");
											}
										}else{
											double iVaaplicaAntes = 0d;
											row.put("IVAAPLICA",iVaaplicaAntes+"");
										}
									}

									String scopago 			 = concepto.get("COPAGO");
									String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");

									if(causadelSiniestro == ""||causadelSiniestro == null){
										causadelSiniestro = CausaSiniestro.ENFERMEDAD.getCodigo();
									}
									if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
										copagoAplicado    = 0d;
									}else{
										if(StringUtils.isNotBlank(scopago)){
											if(scopago.equalsIgnoreCase("na") || scopago.equalsIgnoreCase("no")){
												copagoAplicado = 0d;
											}else{
												if(scopago.contains("%")){
													copagoPorc = true;
												}
												scopago=scopago.replace("%", "").replace("$", "").replaceAll(",", "");
												copago=Double.valueOf(scopago);
												if(copagoPorc) {
													copagoAplicado=(subtotalDescuento*(copago/100d));
												}
												else {
													copagoAplicado=copago * cantidad;
												}
											}
										}
									}
									row.put("COPAGOAPLICA",copagoAplicado+"");
									double subtotalCopago    = subtotalDescuento - copagoAplicado;//++
									row.put("SUBTTCOPAGO",subtotalCopago+"");
									
									double israplicado       = subtotalCopago*(isrprov/100d);//++
									row.put("ISRAPLICA",israplicado+"");
									
									double subtotalImpuestos = subtotalCopago-(israplicado+0d);
									
									double totalISRMostrar   = Double.parseDouble(mpdir.get("iSRMostrar"));
									totalISRMostrar 		+= israplicado;
									mpdir.put("iSRMostrar",totalISRMostrar+"");
									
									double cedularaplicado   = subtotalCopago*(cedprov/100d);//++
									row.put("CEDUAPLICA",cedularaplicado+"");
									
									logger.debug("Concepto cantidad 			 : {} ",cantidad);
									logger.debug("Concepto precioArancel 		 : {} ",precioArancel);
									logger.debug("Concepto subtotalArancel 		 : {} ",subtotalArancel);
									logger.debug("Concepto descuentoAplicado 	 : {} ",descuentoAplicado);
									logger.debug("Concepto subtotalDescuento 	 : {} ",subtotalDescuento);
									logger.debug("Concepto copagoAplicado        : {} ",copagoAplicado);
									logger.debug("Concepto subtotalCopago 		 : {} ",subtotalCopago);
									logger.debug("Concepto israplicado 			 : {} ",israplicado);
									logger.debug("Concepto subtotalImpuestos 	 : {} ",subtotalImpuestos);
									logger.debug("Concepto base totalISRMostrar  : {} ",totalISRMostrar);
									logger.debug("Concepto cedularaplicado 		 : {} ",cedularaplicado);
									
									subtotalImpuestos 		 = subtotalImpuestos - cedularaplicado;
									row.put("SUBTTIMPUESTOS",subtotalImpuestos+"");
									
									double ivaaplicado  = 0d;
									double ivaRetenidos = 0d;
									double ptimportauto = 0d;
									
									if(aplicaIVA.equalsIgnoreCase("S")){
										if(seleccionAplica.equalsIgnoreCase("D")){
											if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
												ivaaplicado       = subtotalCopago*(ivaprov/100d);
											}else{
												ivaaplicado= 0d;
											}
											//ivaaplicado       = subtotalCopago*(ivaprov/100d);
											row.put("IVAAPLICA",ivaaplicado+"");
											if(ivaRetenido.equalsIgnoreCase("S")){
												ivaRetenidos      = ((2d * ivaaplicado)/3);
												row.put("IVARETENIDO",ivaRetenidos+"");
											}else{
												ivaRetenidos      = 0d;
												row.put("IVARETENIDO",ivaRetenidos+"");
											}
											ptimportauto      = (subtotalImpuestos - ivaRetenidos)+ivaaplicado;
											row.put("PTIMPORTAUTO",ptimportauto+"");
										}else{
											if(concepto.get("APLICIVA").equalsIgnoreCase("S")){
												ivaaplicado       = subtotalDescuento*(ivaprov/100d);
											}else{
												ivaaplicado= 0d;
											}
											//ivaaplicado       = subtotalDescuento*(ivaprov/100d);
											row.put("IVAAPLICA",ivaaplicado+"");
											if(ivaRetenido.equalsIgnoreCase("S")){
												ivaRetenidos      = ((2d * ivaaplicado)/3);
												row.put("IVARETENIDO",ivaRetenidos+"");
											}else{
												ivaRetenidos      = 0d;
												row.put("IVARETENIDO",ivaRetenidos+"");
											}
											ptimportauto      = (subtotalImpuestos-ivaRetenidos)+ivaaplicado; //++
											row.put("PTIMPORTAUTO",ptimportauto+"");
										}
									}else{
										ivaaplicado       = 0d;
										ivaRetenidos      = 0d;
										row.put("IVAAPLICA",ivaaplicado+"");
										row.put("IVARETENIDO",ivaRetenidos+"");
										ptimportauto      = (subtotalImpuestos-ivaRetenidos)+ivaaplicado;
										row.put("PTIMPORTAUTO",ptimportauto+"");
									}
									
									double totalIVAMostrar = Double.parseDouble(mpdir.get("ivaTotalMostrar"));
									logger.debug("Concepto base totalIVAMostrar : {} ",totalIVAMostrar);
									totalIVAMostrar += ivaaplicado;
									logger.debug("Sumatoria totalIVAMostrar : {} ",totalIVAMostrar);
									mpdir.put("ivaTotalMostrar",totalIVAMostrar+"");
									
									double totalIVARetenidoMostrar = Double.parseDouble(mpdir.get("ivaRetenidoMostrar"));
									logger.debug("Concepto base totalIVARetenidoMostrar : {} ",totalIVARetenidoMostrar);
									
									totalIVARetenidoMostrar += ivaRetenidos;
									logger.debug("Sumatoria totalIVAMostrar      : {} ",totalIVARetenidoMostrar);
									mpdir.put("ivaRetenidoMostrar",totalIVARetenidoMostrar+"");
									
									double ptimport = Double.parseDouble(row.get("PTIMPORT"));
									double valorusado      = ptimportauto;
									
									String autmedic = row.get("AUTMEDIC");
									if(StringUtils.isNotBlank(autmedic)&&autmedic.equalsIgnoreCase("S")){
										valorusado = ptimport;
									}
									row.put("VALORUSADO",valorusado+"");
									
									logger.debug("Concepto ptimport              : {} ",ptimport);
									logger.debug("Concepto valorusado            : {} ",valorusado);
									
									double totalGrupo = Double.parseDouble(mpdir.get("total"));
									logger.debug("Concepto base totalGrupo       : {} ",totalGrupo);
									totalGrupo += valorusado;
									logger.debug("Sumatoria totalGrupo           : {} ",totalGrupo);
									mpdir.put("total",totalGrupo+"");
									
									double totalGrupoCedular = Double.parseDouble(mpdir.get("totalcedular"));
									logger.debug("Concepto base totalGrupoCedular  : {} ",totalGrupoCedular);
									totalGrupoCedular += cedularaplicado;
									logger.debug("Sumatoria totalGrupoCedular      : {} ",totalGrupoCedular);
									mpdir.put("totalcedular",totalGrupoCedular+"");
									
									concepto.putAll(row);
									logger.debug("<<PAGO DIRECTO DIFERENTE A HOSPITALIZACION Y AYUDA DE MATERNIDAD");
								}
							}
						}//FIN DE CONCEPTOS
						aseguradoObj.put("conceptosAsegurado", conceptosxSiniestro);
						
						//hospitalizacion
						//	logger.debug("######  HOSPITALIZACI�N Y AYUDA DE MATERNIDAD WS ######");
						if(tipoFormatoCalculo.equalsIgnoreCase("1")){
							logger.debug("--->>>> WS del siniestro iterado Hospitalizacion y Ayuda de Maternidad");
							logger.debug("deducible siniestro iterado     : {} ",sDeducibleSiniestroIte);
							logger.debug("copago siniestro iterado        : {} ",sCopagoSiniestroIte);
							logger.debug("tipo copago siniestro iterado   : {} ",tipoCopagoSiniestroIte);
							
							logger.debug("Hospitalizacion Importe    : {} ",hosp.get("PTIMPORT"));
							logger.debug("Hospitalizacion Descuento  : {} ",hosp.get("DESTO"));
							logger.debug("Hospitalizacion IVA        : {} ",hosp.get("IVA"));
							logger.debug("Hospitalizacion Deducible  : {} ",deducibleSiniestroIte);
							
							double hPTIMPORT = Double.valueOf(hosp.get("PTIMPORT"));
							double DESTOIMP  = Double.valueOf(hosp.get("DESTO"));
							double hIVA      = Double.valueOf(hosp.get("IVA"));
							String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
							double subttDesto =0d;
							
							if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){ // Diferente de Accidente
								subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP + deducibleSiniestroIte );
							}else{
								if(cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())||cdramo.equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES_PRUEBA.getCdramo())){
									subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP + deducibleSiniestroIte );
								}else{
									subttDesto = (hPTIMPORT + DESTOIMP) - (DESTOIMP);
								}
							}
							
							if(StringUtils.isNotBlank(tipoCopagoSiniestroIte)) {
								if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){ //Diferente de Accidente
									copagoAplicadoSiniestroIte = Double.parseDouble(penalizacionPesos) + (subttDesto * ( Double.parseDouble(penalizacionPorcentaje) / 100d ));
								}else{
									copagoAplicadoSiniestroIte= 0d;
								}
							}
							
							importeSiniestroIte = subttDesto - copagoAplicadoSiniestroIte;
							//Cambiarlo cuando nos del el Vo.Bo. de Siniestros
							double hIVADesCopago  = Double.valueOf(hosp.get("IVA"));
							//double hIVADesCopago  = importeSiniestroIte*(ivaprov/100d);
							logger.debug("IVA despues de Copago  : {} ",hIVADesCopago);
							
							hosp.put("PTIMPORT_DESCOPAGO" , importeSiniestroIte+"");
							hosp.put("IVA_DESCOPAGO"    , hIVADesCopago+"");
							
							double importeBase = 0d;
							
							importeBase= hPTIMPORT - DESTOIMP;
							if(aplicaIVA.equalsIgnoreCase("S")){
								//SI LOS VALORES SON ANTES DE COPAGO ENTONCES SE QUEDAN IGUALES LOS VALORES DE DESCUENTO, IVA Y PTIMPORT
								if(seleccionAplica.equalsIgnoreCase("D")){ // ANTES DEL COPAGO
									hosp.put("IVA"    , hIVADesCopago+"");
									hosp.put("BASEIVA" , importeSiniestroIte+"");
								}else{
									hosp.put("IVA"    , importeSiniestroIte*(ivaprov/100d)+"");
									hosp.put("BASEIVA" , subttDesto+"");
								}
							}else{
								hosp.put("IVA"    ,0d+"");
								hosp.put("BASEIVA" , subttDesto+"");
							}
							
							//APLICAMOS EL IVA RETENIDO
							if(ivaRetenido.equalsIgnoreCase("S")){
								ivrSiniestroIte = ((2d * Double.parseDouble(hosp.get("IVA")))/3d);
								hosp.put("IVARETENIDO"    , ivrSiniestroIte+"");
							}else{
								ivrSiniestroIte = 0d;
								hosp.put("IVARETENIDO"    , ivrSiniestroIte+"");
							}
							ivaSiniestroIte = Double.parseDouble(hosp.get("IVA"));
							isrSiniestroIte = Double.valueOf(hosp.get("IMPISR"));
							cedSiniestroIte = Double.valueOf(hosp.get("IMPCED"));
							importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroIte)).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroIte)    ).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroIte)    ).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroIte)    ).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_CEDULAR , (new Double(cedSiniestroIte)    ).toString());
							logger.debug("mapa WS siniestro iterado : {} ",importesWSSiniestroIte);
							logger.debug("<<<-- WS del siniestro iterado Hospitalizacion y ayuda de Maternidad");
						}else{
							logger.debug("-->>>> WS del siniestro iterado Diferente de Hospitalizacion y Ayuda de Maternidad ");
							logger.debug("deducible siniestro iterado : {} ",sDeducibleSiniestroIte);
							logger.debug("copago siniestro iterado : {} ",sCopagoSiniestroIte);
							logger.debug("tipo copago siniestro iterado : {} ",tipoCopagoSiniestroIte);
							
							double totalGrupo = Double.valueOf(mpdir.get("total"));
							
							importeSiniestroIte = totalGrupo / ( 1d + ( ivaprov  / 100d ) );
							ivrSiniestroIte = 0d;
							isrSiniestroIte = 0d;
							ivaSiniestroIte = 0d;
							cedSiniestroIte = Double.valueOf(mpdir.get("totalcedular"));
							
							double subttDescuentoSiniestroIte= 0d;
							double subttISRSiniestroIte= 0d;
							double subttcopagototalSiniestroIte=0;
							
							for(Map<String,String>concepto : conceptos) {
								if(concepto.get("CDUNIECO").equals(siniestroIte.get("CDUNIECO"))
									&&concepto.get("CDRAMO").equals(siniestroIte.get("CDRAMO"))
									&&concepto.get("ESTADO").equals(siniestroIte.get("ESTADO"))
									&&concepto.get("NMPOLIZA").equals(siniestroIte.get("NMPOLIZA"))
									&&concepto.get("NMSUPLEM").equals(siniestroIte.get("NMSUPLEM"))
									&&concepto.get("NMSITUAC").equals(siniestroIte.get("NMSITUAC"))
									&&concepto.get("AAAPERTU").equals(siniestroIte.get("AAAPERTU"))
									&&concepto.get("STATUS").equals(siniestroIte.get("STATUS"))
									&&concepto.get("NMSINIES").equals(siniestroIte.get("NMSINIES"))
								){
									subttDescuentoSiniestroIte+= Double.valueOf(concepto.get("SUBTTDESCUENTO"));
									subttcopagototalSiniestroIte+= Double.valueOf(concepto.get("SUBTTCOPAGO"));
									subttISRSiniestroIte+= Double.valueOf(concepto.get("ISRAPLICA"));
									ivaSiniestroIte+= Double.valueOf(concepto.get("IVAAPLICA"));
									ivrSiniestroIte += ((2 * Double.valueOf(concepto.get("IVAAPLICA")))/3);
								}
							}
							
							if(aplicaIVA.equalsIgnoreCase("S")){
								if(seleccionAplica.equalsIgnoreCase("D")){
									importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(subttcopagototalSiniestroIte)).toString());
								}else{
									importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE , (new Double(subttDescuentoSiniestroIte)).toString());
								}
								if(ivaRetenido.equalsIgnoreCase("S")){
									importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroIte)    ).toString());
								}else{
									importesWSSiniestroIte.put(IMPORTE_WS_IVR     , (new Double(0d)    ).toString());
								}
							}else{
								importesWSSiniestroIte.put(IMPORTE_WS_IMPORTE 	  , (new Double(subttcopagototalSiniestroIte)).toString());
								importesWSSiniestroIte.put(IMPORTE_WS_IVR     	  , (new Double(0d)    ).toString());
							}
							
							importesWSSiniestroIte.put(IMPORTE_WS_IVA     		 , (new Double(ivaSiniestroIte)    ).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_ISR     		 , (new Double(subttISRSiniestroIte)    ).toString());
							importesWSSiniestroIte.put(IMPORTE_WS_CEDULAR 		 , (new Double(cedSiniestroIte)    ).toString());
							logger.debug("mapa WS siniestro iterado: {} ",importesWSSiniestroIte);
							logger.debug("<<WS del siniestro iterado Diferente de Hospitalizacion y Ayuda de Maternidad ");
						}
					}
					facturaObj.put("siniestroPD", aseguradosxSiniestro);
				}
			}
			/***************************** 		P A G O		R E E M B O L S O 		*************************/
			else if(TipoPago.REEMBOLSO.getCodigo().equals(tramite.get("OTVALOR02"))){//TIPO DE PAGO POR REEMBOLSO
				logger.debug("Paso 5.- EL PROCESO DE PAGO REEMBOLSO ");
				
				double importeSiniestroUnico 	= 0d;
				double ivaSiniestroUnico     	= 0d;
				double ivrSiniestroUnico     	= 0d;
				double isrSiniestroUnico     	= 0d;
				double cedularSiniestroUnico 	= 0d;
				double penalizacionCambioZona 	= 0d;
				double penalizacionCirculoHosp 	= 0d;
				double totalPenalizacion 		= 0d;
				double deducibleFacturaIte      = 0d;
				double cantidadCopagoFacturaIte = 0d;
				double copagoAplicadoFacturaIte = 0d;

				Map<String,String> calcxCobe 	= new HashMap<String,String>();
				Map<String,String> penalizacion = new HashMap<String,String>();
				
				List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,null,null);
				logger.debug("Paso 6.- Obtenemos los Siniestros Maestros : {} ",siniestros);
				siniestro  = siniestros.get(0);
				siniestros = null;
				smap2      = siniestro;
				smap3      = new HashMap<String,String>();
				smap3.put("a","a");
				smap.put("PAGODIRECTO","N");
				
				conceptos  = siniestrosManager.P_GET_CONCEPTOS_FACTURA(
					siniestro.get("CDUNIECO"),
					siniestro.get("CDRAMO"),
					siniestro.get("ESTADO"),
					siniestro.get("NMPOLIZA"),
					siniestro.get("NMSUPLEM"),
					siniestro.get("NMSITUAC"),
					siniestro.get("AAAPERTU"),
					siniestro.get("STATUS"),
					siniestro.get("NMSINIES"),
					null,
					siniestro.get("CDTIPSIT"),
					ntramite
				);
				logger.debug("Paso 7.- Obtenemos los Conceptos de la Factura : {} ",conceptos);
				slist1     = facturasAux;

				HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
				paramCobertura.put("pv_ntramite_i",ntramite);
				paramCobertura.put("pv_tipopago_i",tramite.get("OTVALOR02"));
				paramCobertura.put("pv_nfactura_i",null);
				paramCobertura.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
				paramCobertura.put("pv_estado_i",siniestro.get("ESTADO"));
				paramCobertura.put("pv_cdramo_i",siniestro.get("CDRAMO"));
				paramCobertura.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
				paramCobertura.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
				paramCobertura.put("pv_cdgarant_i",null);

				List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaListaCoberturaPoliza(paramCobertura);
				logger.debug("Paso 8.- Obtenemos la informacion de la Cobertura : {} ",listaCobertura);

				//hospitalizacion
				Map<String,String> hosp = new HashMap<String,String>();
				lhosp.add(hosp);
				hosp.put("PTIMPORT" , "0");
				hosp.put("DESTO"    , "0");
				hosp.put("IVA"      , "0");
				//hospitalizacion

				//directo
				Map<String,String>mpdir=new HashMap<String,String>();
				mpdir.put("dummy","dummy");
				lpdir.add(mpdir);
				//directo

				Map<String,String>importesWSSiniestroUnico=new HashMap<String,String>();
				importesWSSiniestroUnico.put("cdunieco" , siniestro.get("CDUNIECO"));
				importesWSSiniestroUnico.put("cdramo"   , siniestro.get("CDRAMO"));
				importesWSSiniestroUnico.put("estado"   , siniestro.get("ESTADO"));
				importesWSSiniestroUnico.put("nmpoliza" , siniestro.get("NMPOLIZA"));
				importesWSSiniestroUnico.put("nmsuplem" , siniestro.get("NMSUPLEM"));
				importesWSSiniestroUnico.put("nmsituac" , siniestro.get("NMSITUAC"));
				importesWSSiniestroUnico.put("aaapertu" , siniestro.get("AAAPERTU"));
				importesWSSiniestroUnico.put("status"   , siniestro.get("STATUS"));
				importesWSSiniestroUnico.put("nmsinies" , siniestro.get("NMSINIES"));
				importesWSSiniestroUnico.put("ntramite" , ntramite);
				listaImportesWS.add(importesWSSiniestroUnico);
				
				Map<String,String> facturaIte        = null;
				for(int i = 0; i < facturasAux.size(); i++) {
				facturaIte = facturasAux.get(i);
				Map<String,Object>facturaObj=new HashMap<String,Object>();
				facturaObj.putAll(facturaIte);
				this.facturasxSiniestro.add(facturaObj);
				//reembolso
				Map<String,String>mprem=new HashMap<String,String>(0);
				mprem.put("TOTALNETO" , "0");
				mprem.put("SUBTOTAL"  , "0");
				lprem.add(mprem);
				//reembolso
				
				String destopor = facturaIte.get("DESCPORC");
				if(StringUtils.isBlank(destopor) || destopor  == null) {
					facturaObj.put("DESCPORC","0");
				}
				String destoimp = facturaIte.get("DESCNUME");
				if(StringUtils.isBlank(destoimp)  || destoimp  == null){
					facturaObj.put("DESCNUME","0");
				}
				//Asignaci�n de las variables principales
				String cdunieco = siniestro.get("CDUNIECO");
				String cdramo   = siniestro.get("CDRAMO");
				String estado   = siniestro.get("ESTADO");
				String nmpoliza = siniestro.get("NMPOLIZA");
				String nmsuplem = siniestro.get("NMSUPLEM");
				String nmsituac = siniestro.get("NMSITUAC");
				String aaapertu = siniestro.get("AAAPERTU");
				String status   = siniestro.get("STATUS");
				String nmsinies = siniestro.get("NMSINIES");
				String cdtipsit = siniestro.get("CDTIPSIT");
				String nfactura = facturaIte.get("NFACTURA");
				
				boolean existeCobertura = false;
				
				Map<String,String>copagoDeducibleFacturaIte =siniestrosManager.obtenerCopagoDeducible(
					ntramite, cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies, nfactura,tramite.get("OTVALOR02"),cdtipsit);
				
				//1.- Obtenemos la informaci�n de Autorizaci�n de Factura
				Map<String,String>autorizacionesFacturaIte = siniestrosManager.obtenerAutorizacionesFactura(
						siniestro.get("CDUNIECO"),
						siniestro.get("CDRAMO"),
						siniestro.get("ESTADO"),
						siniestro.get("NMPOLIZA"),
						siniestro.get("NMSUPLEM"),
						siniestro.get("NMSITUAC"),
						siniestro.get("AAAPERTU"),
						siniestro.get("STATUS"),
						siniestro.get("NMSINIES"),
						facturaIte.get("NFACTURA"));
					facturaObj.put("AUTMEDIC",autorizacionesFacturaIte.get("AUTMEDIC"));
					facturaObj.put("COMMENME",autorizacionesFacturaIte.get("COMMENME"));
					facturaObj.put("AUTRECLA",autorizacionesFacturaIte.get("AUTRECLA"));
					facturaObj.put("COMMENAR",autorizacionesFacturaIte.get("COMMENAR"));
				
				//2.- Obtenemo los datos generales del siniestros
				List<Map<String,String>> informacionGral = siniestrosManager.obtieneDatosGeneralesSiniestro(siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"),
				siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"),siniestro.get("NMSITUAC"), siniestro.get("NMSUPLEM"), siniestro.get("STATUS"), siniestro.get("AAAPERTU"), siniestro.get("NMSINIES") , facturaIte.get("NTRAMITE"));

				//3.- Guardamos los valores en calculosPenalizaciones
				Map<String,String>copagoDeducibleSiniestroIte =siniestrosManager.obtenerCopagoDeducible(ntramite,siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"),
				siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSUPLEM"),siniestro.get("NMSITUAC"),
				siniestro.get("AAAPERTU"),siniestro.get("STATUS"),siniestro.get("NMSINIES"),facturaIte.get("NFACTURA"),tramite.get("OTVALOR02"),siniestro.get("CDTIPSIT"));

				String tipoFormatoCalculo = copagoDeducibleSiniestroIte.get("FORMATOCALCULO");
				String calculosPenalizaciones = copagoDeducibleSiniestroIte.get("PENALIZACIONES");
				calcxCobe.put("tipoFormatoCalculo",""+tipoFormatoCalculo);
				calcxCobe.put("calculosPenalizaciones",""+calculosPenalizaciones);
				datosCoberturaxCal.add(calcxCobe);
				if(informacionGral.size()>0){
					penalizacion.put("causaSiniestro", informacionGral.get(0).get("CDCAUSA"));
					if(informacionGral.get(0).get("CDCAUSA").toString().equalsIgnoreCase(CausaSiniestro.ENFERMEDAD.getCodigo())){
						HashMap<String, Object> paramDatosCo = new HashMap<String, Object>();
						paramDatosCo.put("pv_ntramite_i",ntramite);
						paramDatosCo.put("pv_tipopago_i",tramite.get("OTVALOR02"));
						paramDatosCo.put("pv_nfactura_i",nfactura);
						paramDatosCo.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
						paramDatosCo.put("pv_estado_i",siniestro.get("ESTADO"));
						paramDatosCo.put("pv_cdramo_i",siniestro.get("CDRAMO"));
						paramDatosCo.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
						paramDatosCo.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
						paramDatosCo.put("pv_cdgarant_i",null);

						List<CoberturaPolizaVO> listadoCobertura = siniestrosManager.getConsultaCoberturaAsegurado(paramDatosCo);
						//logger.debug("VALOR DE LAS COBERTURAS  P. REEMBOLSO : {} ",listaCobertura);
						for(int j2 = 0 ;j2 < listadoCobertura.size();j2++){
							if(listadoCobertura.get(j2).getCdgarant().toString().equalsIgnoreCase("7EDA")){
								existeCobertura = true;
							}
						}
						logger.debug("existeCobertura : {} ",existeCobertura);
					}
				}else{
					penalizacion.put("causaSiniestro", CausaSiniestro.ENFERMEDAD.getCodigo());
				}
				
				//if(facturaIte.get("CDGARANT").equalsIgnoreCase("18HO")||facturaIte.get("CDGARANT").equalsIgnoreCase("18MA"))
				if(tipoFormatoCalculo.equalsIgnoreCase("1")){
					logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					if(calculosPenalizaciones.equalsIgnoreCase("1")){
						HashMap<String, Object> paramExclusion = new HashMap<String, Object>();
						paramExclusion.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
						paramExclusion.put("pv_estado_i",siniestro.get("ESTADO"));
						paramExclusion.put("pv_cdramo_i",siniestro.get("CDRAMO"));
						paramExclusion.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
						paramExclusion.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
						if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.SALUD_VITAL.getCdramo())){
							//--> SALUD VITAL
							//	1.- Verificamos si existe exclusi�n de penalizaci�n
							existePenalizacion = siniestrosManager.validaExclusionPenalizacion(paramExclusion);
							//2.- Obtenemos la penalizaci�n por cambio de Zona
							penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),informacionGral.get(0).get("CIRHOSPI"),
									informacionGral.get(0).get("DSZONAT"),facturaIte.get("CDPRESTA"),siniestro.get("CDRAMO"));
							//penalizacionCambioZona = penalizacionCambioZona(existePenalizacion,informacionGral.get(0).get("CDCAUSA"),copagoDeducibleSiniestroIte.get("LV_PENALIZA"));
							//3.- Obtenemos la penalizaci�n por circulo Hospitalario
							List<ConsultaProveedorVO> medicos = siniestrosManager.getConsultaListaProveedorMedico(TipoPrestadorServicio.MEDICO.getCdtipo(),facturaIte.get("CDPRESTA"));
							penalizacionCirculoHosp = calcularPenalizacionCirculo(informacionGral.get(0).get("CIRHOSPI"), medicos.get(0).getCirculo(),informacionGral.get(0).get("CDCAUSA"),siniestro.get("CDRAMO"));
						}else{
							// --> DIFERENTE DE SALUD VITAL
							penalizacionCambioZona = 0d;
							penalizacionCirculoHosp = 0d;
						}
						
					}
					logger.debug("<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
				}
				penalizacion.put("penalizacionCambioZona",""+penalizacionCambioZona);
				penalizacion.put("penalizacionCirculoHosp",""+penalizacionCirculoHosp);
				
				//3.- Obtenemos el total de penalizaci�n
				String calcularTotalPenalizacion = calcularTotalPenalizacion(penalizacionCambioZona,penalizacionCirculoHosp,informacionGral.get(0).get("CDCAUSA"),
				copagoDeducibleFacturaIte.get("COPAGO"),copagoDeducibleFacturaIte.get("TIPOCOPAGO"),
				informacionGral.get(0).get("CDPROVEE"),siniestro.get("CDRAMO"), informacionGral.get(0).get("FEOCURRE"));
				
				penalizacion.put("totalPenalizacionGlobal",""+calcularTotalPenalizacion);
				String penalizacionT[] = calcularTotalPenalizacion.split("\\|");
				penalizacion.put("copagoPorcentajes",penalizacionT[0].toString());
				penalizacion.put("copagoPesos",penalizacionT[1].toString());
				datosPenalizacion.add(penalizacion);
				
				String sDeducibleFacturaIte     = copagoDeducibleFacturaIte.get("DEDUCIBLE").replace(",","");
				String sCopagoFacturaIte        = copagoDeducibleFacturaIte.get("COPAGO").replace(",","");
				String tipoCopagoFacturaIte     = copagoDeducibleFacturaIte.get("TIPOCOPAGO");
				
				//OBTENEMOS LOS VALORES DE PENALIZACION Y COPAGO
				if(StringUtils.isNotBlank(sDeducibleFacturaIte)
					&&(!sDeducibleFacturaIte.equalsIgnoreCase("na"))
					&&(!sDeducibleFacturaIte.equalsIgnoreCase("no"))
				){
					try{
						deducibleFacturaIte = Double.valueOf(sDeducibleFacturaIte);
					}
					catch(Exception ex){
						logger.debug(""
							+ "\n### ERROR ##################################################"
							+ "\n### no es numero deducible: '"+sDeducibleFacturaIte+"' ###"
							+ "\n############################################################"
						);
						deducibleFacturaIte = 0d;
					}
				}
				
				if(StringUtils.isNotBlank(sCopagoFacturaIte)
					&&(!sCopagoFacturaIte.equalsIgnoreCase("na"))
					&&(!sCopagoFacturaIte.equalsIgnoreCase("no"))
				){
					try{
						cantidadCopagoFacturaIte = Double.valueOf(sCopagoFacturaIte);
					}
					catch(Exception ex){
						logger.debug(""
							+ "\n### ERROR ############################################"
							+ "\n### no es numero copago: '"+sCopagoFacturaIte+"' ###"
							+ "\n######################################################"
						);
						cantidadCopagoFacturaIte = 0d;
					}
				}
				
				slist2.add(copagoDeducibleFacturaIte);
				
				Map<String,String>datosProveedor =siniestrosManager.obtenerDatosProveedor(facturaIte.get("CDPRESTA")); 
				slist3.add(datosProveedor);
				double ivaprov=Double.parseDouble(datosProveedor.get("IVA"));
				
				List<Map<String,String>>listaConceptosFactura = new ArrayList<Map<String,String>>();
				llist1.add(listaConceptosFactura);
				for(Map<String,String>concepto : conceptos){
					if(concepto.get("NFACTURA").equals(facturaIte.get("NFACTURA"))){
						listaConceptosFactura.add(concepto);
						logger.debug(">>REEMBOLSO");
						Map<String,String>row=new HashMap<String,String>();
						row.putAll(concepto);
						
						double ptimport = Double.parseDouble(row.get("PTIMPORT"));
						logger.debug("ptimport : {} ",ptimport);
						
						double ajusteaplica = 0d;
						if(StringUtils.isNotBlank(row.get("PTIMPORT_AJUSTADO"))){
							ajusteaplica = Double.parseDouble(row.get("PTIMPORT_AJUSTADO"));
						}
						logger.debug("ajusteaplica : {} ",ajusteaplica);
						
						double subtotal = ptimport-ajusteaplica;
						logger.debug("subtotal : {} ",subtotal);
						row.put("SUBTOTAL",subtotal+"");
						
						double gtotalneto = Double.parseDouble(mprem.get("TOTALNETO"));
						double gsubtotal  = Double.parseDouble(mprem.get("SUBTOTAL"));
						logger.debug("base totalneto : {} ",gtotalneto);
						logger.debug("base subtotal : {} ",gsubtotal);
						gtotalneto += ptimport;
						gsubtotal  += subtotal;
						logger.debug("new totalneto : {} ",gtotalneto);
						logger.debug("new subtotal : {} ",gsubtotal);
						
						mprem.put("TOTALNETO" , gtotalneto + "");
						mprem.put("SUBTOTAL"  , gsubtotal  + "");
						
						concepto.putAll(row);
						logger.debug("<<REEMBOLSO");
						//pago reembolso
					}
				}
				
				//Verificamos la informaci�n del deducible
				//if(facturaIte.get("CDGARANT").equalsIgnoreCase("18HO")||facturaIte.get("CDGARANT").equalsIgnoreCase("18MA"))
				if(tipoFormatoCalculo.equalsIgnoreCase("1")){
					logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					//verificamos la causa del siniestro
					String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
					if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
						if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
							//GMMI
							if(existeCobertura == true){
								deducibleFacturaIte = 0d;
							}
						}else{
							deducibleFacturaIte = 0d;
						}
					}
					logger.debug("<<<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
				}else{
					logger.debug("--->>>>>>> DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
					if(causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
						if(siniestro.get("CDRAMO").toString().equalsIgnoreCase(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo())){
							//GMMI
							if(existeCobertura == true){
								deducibleFacturaIte = 0d;
							}
						}else{
							deducibleFacturaIte = 0d;
						}
					}
					logger.debug("<<<<<<<<--- DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
				}
				
				logger.debug(">>Calculando total factura iterada para WS");
				logger.debug("deducible : {} ",deducibleFacturaIte);
				logger.debug("scopago : {} ",sCopagoFacturaIte);
				logger.debug("tipocopago : {} ",tipoCopagoFacturaIte);
				logger.debug("facturaIte.get(DESCPORC) : {} ",facturaIte.get("DESCPORC"));
				double totalFactura  = Double.valueOf(mprem.get("SUBTOTAL"));
				double destoPorFac = 0d;
				double destoImpFac= 0d;
				if(!StringUtils.isBlank(facturaIte.get("DESCPORC")) || !(facturaIte.get("DESCPORC")  == null)){
					destoPorFac = Double.valueOf(facturaIte.get("DESCPORC"));
				}
				if(!StringUtils.isBlank(facturaIte.get("DESCNUME"))  || !(facturaIte.get("DESCNUME")  == null)){
					destoImpFac = Double.valueOf(facturaIte.get("DESCNUME"));
				}
				//double destoPorFac   = Double.valueOf(facturaIte.get("DESCPORC"));
				//double destoImpFac   = Double.valueOf(facturaIte.get("DESCNUME"));
				double destoAplicado = (totalFactura*(destoPorFac/100d)) + destoImpFac;
				logger.debug("subtotal : {} ",totalFactura);
				totalFactura -= destoAplicado;
				logger.debug("subtotal desto : {} ",totalFactura);
				totalFactura -= deducibleFacturaIte;
				logger.debug("subtotal deducible : {} ",totalFactura);
				
				if(StringUtils.isNotBlank(tipoCopagoFacturaIte)){
					String causadelSiniestro = informacionGral.get(0).get("CDCAUSA");
					//if(facturaIte.get("CDGARANT").equalsIgnoreCase("18HO")||facturaIte.get("CDGARANT").equalsIgnoreCase("18MA"))
					if(tipoFormatoCalculo.equalsIgnoreCase("1")){
						logger.debug("--->>>>>>> HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						//verificamos la causa del siniestro
						if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
							//Diferente de accidente
							copagoAplicadoFacturaIte = Double.parseDouble(penalizacionT[1].toString()) + (totalFactura * ( Double.parseDouble(penalizacionT[0].toString()) / 100d ));
						}else{
							copagoAplicadoFacturaIte = 0d;
						}
						logger.debug("<<<<<<<<--- HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					}else{
						//COBERTURA DIFERENTE HOSPITALIZACI�N Y AYUDA DE MATERNIDAD
						logger.debug("--->>>>>>> DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
						if(!causadelSiniestro.equalsIgnoreCase(CausaSiniestro.ACCIDENTE.getCodigo())){
							if(tipoCopagoFacturaIte.equalsIgnoreCase("$")){
								copagoAplicadoFacturaIte = cantidadCopagoFacturaIte;
							}
							if(tipoCopagoFacturaIte.equalsIgnoreCase("%")){
								copagoAplicadoFacturaIte = totalFactura * ( cantidadCopagoFacturaIte / 100d );
							}
						}else{
							copagoAplicadoFacturaIte = 0d;
						}
						logger.debug("<<<<<<<<--- DIFERENTE DE HOSPITALIZACION Y AYUDA DE MATERNIDAD");
					}
				}
				totalFactura -= copagoAplicadoFacturaIte;
				facturaObj.put("TOTALFACTURAIND",totalFactura+"");
				logger.debug("total copago (final) : {} ",totalFactura);
				logger.debug("<<Calculando total factura iterada para WS");
				
				importeSiniestroUnico += totalFactura;
			}
			
			logger.debug(">>WS del siniestro unico");
			importesWSSiniestroUnico.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroUnico)).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_CEDULAR , (new Double(cedularSiniestroUnico)).toString());
			logger.debug("mapa WS siniestro unico : {} ",importesWSSiniestroUnico);
			logger.debug("<<WS del siniestro unico");
			
		}else{
			/***************************** 		P A G O		I N D E M N I Z A C I O N 		*************************/
			logger.debug("Paso 5.- EL PROCESO DE PAGO DE INDEMNIZACION");
			
			List<Map<String,String>> siniestros = siniestrosManager.listaSiniestrosMsiniesTramite(ntramite,null,null);
			logger.debug("Paso 6.- Obtenemos el valor del Siniestro para el pago : {} ",siniestros.get(0));
			
			siniestro  = siniestros.get(0);
			siniestros = null;
			smap2      = siniestro;
			smap3      = new HashMap<String,String>();
			smap3.put("a","a");
			smap.put("PAGODIRECTO","N");
			slist1     = facturasAux;
			logger.debug("FACTURAS : {} ",slist1);
			
			HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
			paramCobertura.put("pv_cdunieco_i",siniestro.get("CDUNIECO"));
			paramCobertura.put("pv_estado_i",siniestro.get("ESTADO"));
			paramCobertura.put("pv_cdramo_i",siniestro.get("CDRAMO"));
			paramCobertura.put("pv_nmpoliza_i",siniestro.get("NMPOLIZA"));
			paramCobertura.put("pv_nmsituac_i",siniestro.get("NMSITUAC"));
			paramCobertura.put("pv_cdgarant_i",null);

			//hospitalizacion
			Map<String,String> hosp = new HashMap<String,String>();
			lhosp.add(hosp);
			hosp.put("PTIMPORT" , "0");
			hosp.put("DESTO"    , "0");
			hosp.put("IVA"      , "0");
			//hospitalizacion

			//directo
			Map<String,String>mpdir=new HashMap<String,String>();
			mpdir.put("dummy","dummy");
			lpdir.add(mpdir);
			//directo

			Map<String,String>importesWSSiniestroUnico=new HashMap<String,String>();
			importesWSSiniestroUnico.put("cdunieco" , siniestro.get("CDUNIECO"));
			importesWSSiniestroUnico.put("cdramo"   , siniestro.get("CDRAMO"));
			importesWSSiniestroUnico.put("estado"   , siniestro.get("ESTADO"));
			importesWSSiniestroUnico.put("nmpoliza" , siniestro.get("NMPOLIZA"));
			importesWSSiniestroUnico.put("nmsuplem" , siniestro.get("NMSUPLEM"));
			importesWSSiniestroUnico.put("nmsituac" , siniestro.get("NMSITUAC"));
			importesWSSiniestroUnico.put("aaapertu" , siniestro.get("AAAPERTU"));
			importesWSSiniestroUnico.put("status"   , siniestro.get("STATUS"));
			importesWSSiniestroUnico.put("nmsinies" , siniestro.get("NMSINIES"));
			importesWSSiniestroUnico.put("ntramite" , ntramite);
			listaImportesWS.add(importesWSSiniestroUnico);
			double importeSiniestroUnico = 0d;
			double ivaSiniestroUnico     = 0d;
			double ivrSiniestroUnico     = 0d;
			double isrSiniestroUnico     = 0d;
			double cedularSiniestroUnico = 0d;

			Map<String,String> facturaIte        = null;
			//for(Map<String,String>facturaIte:facturasAux)
			for(int i = 0; i < facturasAux.size(); i++){
				facturaIte = facturasAux.get(i);
				logger.debug("Paso 6.- Factura en proceso : {} Informacion Factura : {} ",i,facturaIte);
				
				Map<String,Object>facturaObj=new HashMap<String,Object>();
				facturaObj.putAll(facturaIte);
				this.facturasxSiniestro.add(facturaObj);
				//reembolso
				Map<String,String>mprem=new HashMap<String,String>(0);
				mprem.put("TOTALNETO" , "0");
				mprem.put("SUBTOTAL"  , "0");
				lprem.add(mprem);
				
				if(siniestro.get("CDRAMO").equalsIgnoreCase(Ramo.RECUPERA.getCdramo())){
					logger.debug("Paso 7.- El Pago a realizar es Recupera");
					
					PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
					datosPol.setCdunieco(siniestro.get("CDUNIECO"));
					datosPol.setCdramo(siniestro.get("CDRAMO"));
					datosPol.setEstado(siniestro.get("ESTADO"));
					datosPol.setNmpoliza(siniestro.get("NMPOLIZA"));
					
					List<ConsultaDatosGeneralesPolizaVO> lista = consultasAseguradoManager.obtieneDatosPoliza(datosPol);
					String feEfecto = lista.get(0).getFeefecto();
					logger.debug("Paso 8.- Obtenemos la fecha Efecto de la Poliza : {} ",feEfecto);
					
					List<Map<String,String>> datosAdicionales = siniestrosManager.listaSumaAseguradaPeriodoEsperaRec(siniestro.get("CDRAMO"),facturaIte.get("CDGARANT"),facturaIte.get("CDCONVAL"),renderFechas.parse(feEfecto));
					double SumaAsegurada = Double.parseDouble(datosAdicionales.get(0).get("SUMAASEG"));
					logger.debug("Paso 9.- Obtenemos la suma Asegurada : {} ",SumaAsegurada);
					
					List<Map<String,String>> esquemaSum = siniestrosManager.listaEsquemaSumaAseguradaRec(siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"), siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSITUAC"));
					double esquemaSumaAseg = Double.parseDouble(esquemaSum.get(0).get("ESQUEMAASEG"));
					logger.debug("Paso 10.- Obtenemos el esquema de suma Asegurada : {} ",esquemaSumaAseg);
					
					double totalFactura = SumaAsegurada * esquemaSumaAseg;
					facturaObj.put("TOTALFACTURAIND",totalFactura+"");
					importeSiniestroUnico += totalFactura;
					
				}else{
					Map<String,String>copagoDeducibleFacturaIte =siniestrosManager.obtenerCopagoDeducible(
							ntramite,siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"), siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSUPLEM"), siniestro.get("NMSITUAC"),
							siniestro.get("AAAPERTU"), siniestro.get("STATUS"), siniestro.get("NMSINIES"), facturaIte.get("NFACTURA"),tramite.get("OTVALOR02"),siniestro.get("CDTIPSIT"));
					logger.debug("copagoDeducibleFacturaIte : {} ",copagoDeducibleFacturaIte);
					
					Map<String,String>rentaDiariaxHospitalizacion =siniestrosManager.obtenerRentaDiariaxHospitalizacion(
						siniestro.get("CDUNIECO"), siniestro.get("CDRAMO"), siniestro.get("ESTADO"), siniestro.get("NMPOLIZA"), siniestro.get("NMSITUAC"), siniestro.get("NMSUPLEM"));
					
					double sumaAsegurada = Double.valueOf(rentaDiariaxHospitalizacion.get("OTVALOR"));
					double beneficioMax = Double.parseDouble(copagoDeducibleFacturaIte.get("BENEFMAX").replace("%",""));
					//OBTENEMOS EL VALOR Y SE REALIZA UN REEMPLA
					Calendar fechas = Calendar.getInstance();
					
					int diasPantalla = 0;
					String fechaEgreso  = facturaIte.get("FFACTURA");
					if(facturaIte.get("FEEGRESO").length() > 0){
						diasPantalla = Integer.parseInt(facturaIte.get("DIASDEDU"));
						fechaEgreso = facturaIte.get("FEEGRESO");
					}
					//fecha inicio
					Calendar fechaInicio = new GregorianCalendar();
					fechaInicio.set(Integer.parseInt(facturaIte.get("FFACTURA").substring(6,10)), Integer.parseInt(facturaIte.get("FFACTURA").substring(3,5)), Integer.parseInt(facturaIte.get("FFACTURA").substring(0,2)));
					Calendar fechaFin = new GregorianCalendar();
					fechaFin.set(Integer.parseInt(fechaEgreso.substring(6,10)), Integer.parseInt(fechaEgreso.substring(3,5)), Integer.parseInt(fechaEgreso.substring(0,2)));
					fechas.setTimeInMillis(fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());
					int totalDias = fechas.get(Calendar.DAY_OF_YEAR);
					
					double totalFactura = sumaAsegurada * (beneficioMax/100d) * (totalDias - diasPantalla);
					facturaObj.put("TOTALFACTURAIND",totalFactura+"");
					importeSiniestroUnico += totalFactura;	
				}
			}

			logger.debug(">>WS del siniestro unico");
			importesWSSiniestroUnico.put(IMPORTE_WS_IMPORTE , (new Double(importeSiniestroUnico)).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_IVA     , (new Double(ivaSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_IVR     , (new Double(ivrSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_ISR     , (new Double(isrSiniestroUnico)    ).toString());
			importesWSSiniestroUnico.put(IMPORTE_WS_CEDULAR , (new Double(cedularSiniestroUnico)).toString());
			logger.debug("mapa WS siniestro unico : {} ",importesWSSiniestroUnico);
			logger.debug("<<WS del siniestro unico");
		}
		
		if(conceptos!=null&&conceptos.size()>0){
			logger.debug("conceptos[0] :{}",conceptos);
		}
		
		seccion     = "FORM_TRAMITE";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, true, true, false, false, false);
		imap.put("tramiteFields" , gc.getFields());
		imap.put("tramiteItems"  , gc.getItems());
		
		seccion     = "FACTURA";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, true, true, true, false, false);
		imap.put("facturaFields"  , gc.getFields());
		imap.put("facturaItems"   , gc.getItems());
		imap.put("facturaColumns" , gc.getColumns());
		
		seccion     = "SINIESTRO";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, true, true, true, false, false);
		imap.put("siniestroFields"  , gc.getFields());
		imap.put("siniestroItems"   , gc.getItems());
		imap.put("siniestroColumns" , gc.getColumns());
		
		seccion     = "PROVEEDOR";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, true, true, false, false, false);
		imap.put("proveedorFields" , gc.getFields());
		imap.put("proveedorItems"  , gc.getItems());
		
		seccion     = "CONCEPTO";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, true, false, true, false, false);
		imap.put("conceptoFields"  , gc.getFields());
		imap.put("conceptoColumns" , gc.getColumns());
	
		seccion     = "FORM_AUTORIZA";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, false, true, false, false, false);
		imap.put("autorizaItems",gc.getItems());
		
		pantalla    = "RECHAZO_SINIESTRO";
		seccion     = "FORMULARIO";
		componentes = pantallasManager.obtenerComponentes(null, null, null, null, null, cdrol, pantalla, seccion, null);
		gc.generaComponentes(componentes, true, false, true, false, false, false);
		imap.put("rechazoitems",gc.getItems());
		
		}
    	catch(Exception ex)
    	{
    		logger.error("error al cargar pantalla de calculo de siniestros : {}", ex.getMessage(), ex);
    	}
    	return SUCCESS;
    }
	
	public String entradaRevisionAdmin(){
		logger.info("Entra a entradaRevisionAdmin params: {}",params);
		try{
			HashMap<String, String> params = new HashMap<String,String>();
			UserVO usuario  = (UserVO)session.get("USUARIO");
			String cdrol    = usuario.getRolActivo().getClave();
			params.put("cdrol", cdrol);
			
			
			String pantalla = "AFILIADOS_AGRUPADOS";
			String seccion  = "FORMULARIO";

			List<ComponenteVO> componentes = pantallasManager.obtenerComponentes(
				null, null, null, null, null, cdrol, pantalla, seccion, null);

			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			imap = new HashMap<String,Item>();
			String valorComplementario = "0";

			List<ComponenteVO>tatrisin=kernelManagerSustituto.obtenerTatrisinPoliza("1000","2","M","20");
			gc.generaComponentes(tatrisin, true, false, true, false, false, false);
			imap.put("tatrisinItems",gc.getItems());
			
			logger.debug("Valores de Respuesta  {}",imap);
		}
		catch(Exception ex){
			logger.error("error al cargar pantalla de asegurados afectados : {}", ex.getMessage(), ex);
		}
		return SUCCESS;
	}
	

	
	/**
	* Funcion para obtener si aplica o no iva en los conceptos
	* @param params
	* @return montoArancel
	*/
	public String obtieneAplicacionIVA(){
		logger.debug("Entra a obtieneAplicacionIVA params de entrada :{}",params);
		try {
			msgResult = siniestrosManager.obtieneAplicaConceptoIVA(params.get("idConcepto"));
			logger.debug("VALOR DE RESPUESTA ===>: {}", msgResult);
			
		}catch( Exception e){
			logger.error("Error al obtener el monto del arancel : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	
	
	/**
	* Funcion para la visualizacion de la autorizacion de servicio 
	* @return params con los valores para hacer las consultas
	*/
	public String configuracionLayoutProveedor(){
		logger.debug("Entra a configuracionLayoutProveedor");
		try {
			logger.debug("Params: {}", params);
		}catch( Exception e){
			logger.error("Error altaFacturasProceso {}", e.getMessage(), e);
		}
		success = true;
		return SUCCESS;
	}
	
	public String guardarHistorialSiniestro(){
		logger.debug("Entra a guardarHistorialSiniestro Params: {}", params);
		try {
			//PAGO DIRECTO
			if(TipoPago.DIRECTO.getCodigo().equals(params.get("tipmov"))){
				//Obtenemos la facturas
				List<Map<String,String>> facturas = siniestrosManager.obtenerFacturasTramite(params.get("ntramite"));
				for(int i=0; i< facturas.size();i++){
					//facturas.get(i).get("FEEGRESO")
					logger.debug("TRAMITE :{}, FACTURA : {}",params.get("ntramite"), facturas.get(i).get("NFACTURA"));
					datosInformacionAdicional = siniestrosManager.guardaHistorialSiniestro(params.get("ntramite"), facturas.get(i).get("NFACTURA"));
					logger.debug("Valores de Salida Directo --> :{}",datosInformacionAdicional);
				}
				
			}else{
				//PAGO POR REEMBOLSO
				datosInformacionAdicional = siniestrosManager.guardaHistorialSiniestro(params.get("ntramite"), null);
				logger.debug("Valores de Salida Reembolso  --> :{}",datosInformacionAdicional);
			}
		}catch( Exception e){
			logger.error("Error guardarHistorialSiniestro: {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String obtenerMRecupera() {
		logger.debug("Entra a obtenerMRecupera params de entrada :{}",params);
		try {
			
			PolizaAseguradoVO datosPoliza = new PolizaAseguradoVO();
			datosPoliza.setCdunieco(params.get("cdunieco"));
			datosPoliza.setCdramo(params.get("cdramo"));
			datosPoliza.setEstado(params.get("estado"));
			datosPoliza.setNmpoliza(params.get("nmpoliza"));
			logger.debug("Dato para consulta : {}",datosPoliza);
			
			List<ConsultaDatosGeneralesPolizaVO> infoPoliza = consultasAseguradoManager.obtieneDatosPoliza(datosPoliza);
			String feEfecto = infoPoliza.get(0).getFeefecto();
			logger.debug("Obtenemos la fecha Efecto de la Poliza : {} ",feEfecto);
			
			List<Map<String,String>>lista = siniestrosManager.obtieneInformacionRecupera(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"),params.get("nmsuplem"), params.get("nmsituac"),
						renderFechas.parse(feEfecto),params.get("ntramite"), params.get("nfactura"));
			logger.debug("Valor de Respusta --> :{}",lista);
			
			loadList = new ArrayList<HashMap<String,String>>();
			for(Map<String,String>map:lista) {
				loadList.add((HashMap<String,String>)map);
			}
			mensaje = "Datos obtenidos";
			success = true;
		}
		catch(Exception ex) {
			logger.error("error al obtener el listado de los conceptos : {}", ex.getMessage(), ex);
			success = false;
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	public String obtieneDatosRecupera() {
		logger.debug("Entra a obtieneDatosRecupera params de entrada :{}",params);
		try {
			
			PolizaAseguradoVO datosPoliza = new PolizaAseguradoVO();
			datosPoliza.setCdunieco(params.get("cdunieco"));
			datosPoliza.setCdramo(params.get("cdramo"));
			datosPoliza.setEstado(params.get("estado"));
			datosPoliza.setNmpoliza(params.get("nmpoliza"));
			logger.debug("Dato para consulta : {}",datosPoliza);
			
			List<ConsultaDatosGeneralesPolizaVO> infoPoliza = consultasAseguradoManager.obtieneDatosPoliza(datosPoliza);
			String feEfecto = infoPoliza.get(0).getFeefecto();
			logger.debug("Obtenemos la fecha Efecto de la Poliza : {} ",feEfecto);
			
			List<Map<String,String>>lista = siniestrosManager.obtieneEsquemaSumAseguradaRecupera(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"),params.get("nmsuplem"), params.get("nmsituac"),
						renderFechas.parse(feEfecto),params.get("cdgarant"), params.get("cdconval"));
			
			loadList = new ArrayList<HashMap<String,String>>();
			for(Map<String,String>map:lista) {
				loadList.add((HashMap<String,String>)map);
			}
			mensaje = "Datos obtenidos";
			success = true;
		}
		catch(Exception ex) {
			logger.error("error al obtener el listado de los conceptos : {}", ex.getMessage(), ex);
			success = false;
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion para eliminar los conceptos
	* @param params
	* @return exito cuando eliminamos el concepto
	*/
	public String guardarMRecupera() {
		logger.debug("Entra a guardarMRecupera params de entrada :{} datosTabla : {}",params,datosTablas);
		try {
			Date   dFemovimi = new Date();
			Date   dFeregist = new Date();
			String nmanno    = Calendar.getInstance().get(Calendar.YEAR)+"";
			//1.- Eliminamos los registros de la tabla msinival
			try {
				siniestrosManager.P_MOV_MRECUPERA(datosTablas.get(0).get("ntramite"),datosTablas.get(0).get("nfactura"), null, null, null, null,"D");
			} catch (Exception e) {
				logger.error("error al eliminar en TfacMesCtrl : {}", e.getMessage(), e);
			}
			
			//2.- Guardamos los registros de 
			for(int i=0;i<datosTablas.size();i++){
				logger.debug("Valores de llegada :{}",datosTablas.get(i));
				siniestrosManager.P_MOV_MRECUPERA(datosTablas.get(i).get("ntramite"),datosTablas.get(i).get("nfactura"), datosTablas.get(i).get("cdgarant"),
						datosTablas.get(i).get("cdconval"),datosTablas.get(i).get("cantporc"),datosTablas.get(i).get("ptimport"),
						datosTablas.get(i).get("operacion"));
			}
			mensaje = "Datos guardados";
			success = true;
		}
		catch(Exception ex){
			logger.debug("error al guardar msinival : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	* Funcion que obtiene la lista del asegurado
	* @param void sin parametros de entrada
	* @return Lista GenericVO con la informacion de los asegurados
	*/    
	public String consultaListaAseguraAutEspecial(){
		logger.debug("Entra a consultaListaAseguraAutEspecial params de entrada :{}",params);
		try {
			listaAsegurado= siniestrosManager.getConsultaListaAseguraAutEspecial(params.get("ntramite"),params.get("nfactura"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaListaContrareciboAutEsp(){
		logger.debug("Entra a consultaListaContrareciboAutEsp params de entrada :{}",params);
		try {
			datosValidacionGral = siniestrosManager.getConsultaListaContrareciboAutEsp(params.get("cdramo"),params.get("ntramite"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	
	public String consultaListaFacturaTramite(){
		logger.debug("Entra a consultaListaFacturaTramite params de entrada :{}",params);
		try {
			datosValidacionGral = siniestrosManager.getConsultaListaFacturaTramite(params.get("ntramite"), params.get("nfactura"));
		}catch( Exception e){
			logger.error("Error al consultar la Lista de los asegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	
	/**
	* metodo que obtiene el listado de las coberturas de poliza
	* @param maps [cdunieco,estado,cdramo,nmpoliza,nmsituac,cdgarant]
	* @return Lista CoberturaPolizaVO con la informacion de los asegurados
	*/
	public String consultaListaCoberturaProducto(){
		logger.debug("Entra a consultaListaCoberturaProducto params de entrada :{}",params);
		try {
			HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
			paramCobertura.put("pv_cdunieco_i",params.get("cdunieco"));
			paramCobertura.put("pv_cdramo_i",params.get("cdramo"));
			paramCobertura.put("pv_estado_i",params.get("estado"));
			paramCobertura.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramCobertura.put("pv_nmsuplem_i",params.get("nmsuplem"));
			paramCobertura.put("pv_nmsituac_i",params.get("nmsituac"));
			paramCobertura.put("pv_cdtipsit_i",params.get("cdtipsit"));
			
			List<CoberturaPolizaVO> lista = siniestrosManager.getConsultaListaCoberturaProducto(paramCobertura);
			if(lista!=null && !lista.isEmpty())	listaCoberturaPoliza = lista;
		}catch( Exception e){
			logger.error("Error al obtener la lista de la cobertura de la poliza : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String guardaAutorizacionEspecial(){
		logger.debug("Entra a guardaAutorizacionEspecial Params: {}", params);
		try{
			Date   fechaProcesamiento = new Date();
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			HashMap<String, Object> paramsAutoriEspecial = new HashMap<String, Object>();
			paramsAutoriEspecial.put("pv_nmautesp_i",params.get("nmautespecial"));
			paramsAutoriEspecial.put("pv_ntramite_i",params.get("txtntramite"));
			paramsAutoriEspecial.put("pv_nfactura_i",params.get("txtnfactura"));
			paramsAutoriEspecial.put("pv_cdunieco_i",params.get("txtcdunieco"));
			paramsAutoriEspecial.put("pv_cdramo_i",  params.get("txtcdramo"));
			paramsAutoriEspecial.put("pv_estado_i",  params.get("txtestado"));
			paramsAutoriEspecial.put("pv_nmpoliza_i",params.get("txtnmpoliza"));
			paramsAutoriEspecial.put("pv_nmsuplem_i",params.get("txtnmsuplem"));
			paramsAutoriEspecial.put("pv_nmsituac_i",params.get("txtnmsituac"));
			paramsAutoriEspecial.put("pv_nmsinies_i",params.get("txtnmsinies"));
			paramsAutoriEspecial.put("pv_valrango_i",params.get("valRango"));
			paramsAutoriEspecial.put("pv_valcober_i",params.get("valGarant"));
			paramsAutoriEspecial.put("pv_cdgarant_i",params.get("cdgarant"));
			paramsAutoriEspecial.put("pv_comments_i",params.get("comments"));
			paramsAutoriEspecial.put("pv_cduser_i", usuario.getUser());
			paramsAutoriEspecial.put("pv_ferecepc_i",fechaProcesamiento);
			paramsAutoriEspecial.put("pv_val_lim_i", params.get("valLimite")); // (EGS)
			msgResult = siniestrosManager.guardaListaAutorizacionEspecial(paramsAutoriEspecial);
			logger.debug("Valor de Respuesta --->"+msgResult);
			
			HashMap<String, Object> paramAutEspecial = new HashMap<String, Object>();
			paramAutEspecial.put("pv_ntramite_i",params.get("txtntramite"));
			paramAutEspecial.put("pv_tipoPago_i",params.get("txttipoPago"));
			paramAutEspecial.put("pv_nfactura_i",params.get("txtnfactura"));
			paramAutEspecial.put("pv_cdunieco_i",params.get("txtcdunieco"));
			paramAutEspecial.put("pv_cdramo_i",  params.get("txtcdramo"));
			paramAutEspecial.put("pv_estado_i",  params.get("txtestado"));
			paramAutEspecial.put("pv_nmpoliza_i",params.get("txtnmpoliza"));
			paramAutEspecial.put("pv_nmsuplem_i",params.get("txtnmsuplem"));
			paramAutEspecial.put("pv_nmsituac_i",params.get("txtnmsituac"));
			paramAutEspecial.put("pv_nmautesp_i",msgResult);
			paramAutEspecial.put("pv_nmsinies_i",params.get("txtnmsinies"));
			validacionGeneral = siniestrosManager.asociarAutorizacionEspecial(paramAutEspecial);
			
		}catch( Exception e){
			logger.error("Error en el guardado de autorizacion especiales : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String obtenerConfiguracionAutEspecial() {
		logger.debug("Entra a obtenerFacturasTramite smap :{}",params);
		try{
			HashMap<String, Object> paramsAutoriEspecial = new HashMap<String, Object>();
			paramsAutoriEspecial.put("pv_nfactura_i",params.get("nfactura"));
			paramsAutoriEspecial.put("pv_ntramite_i",params.get("ntramite"));
			//paramsAutoriEspecial.put("pv_cdperson_i",params.get("cdperson"));
			paramsAutoriEspecial.put("pv_cdunieco_i",params.get("cdunieco"));
			paramsAutoriEspecial.put("pv_cdramo_i",params.get("cdramo"));
			paramsAutoriEspecial.put("pv_estado_i",params.get("estado"));
			paramsAutoriEspecial.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramsAutoriEspecial.put("pv_nmsuplem_i",params.get("nmsuplem"));
			paramsAutoriEspecial.put("pv_nmsituac_i",params.get("nmsituac"));
			paramsAutoriEspecial.put("pv_nmsinies_i",params.get("nmsinies"));
			//paramsAutoriEspecial.put("pv_cdtipsit_i",params.get("cdtipsit"));
			datosValidacion = siniestrosManager.obtenerConfiguracionAutEspecial(paramsAutoriEspecial);
			logger.debug("Valor a enviar ===> :{}",datosValidacion);
			success=true;
		}
		catch(Exception ex) {
			success=false;
			logger.error("Error al obtener facturas de tramite : {}", ex.getMessage(), ex);
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	public String validaAutorizacionEspecial(){
		logger.debug("Entra a validaAutorizacionEspecial  Params: {}", params);
		try {
			HashMap<String, Object> paramAutEspecial = new HashMap<String, Object>();
			paramAutEspecial.put("pv_ntramite_i",params.get("ntramite"));
			paramAutEspecial.put("pv_tipoPago_i",params.get("tipoPago"));
			paramAutEspecial.put("pv_nfactura_i",params.get("nfactura"));
			paramAutEspecial.put("pv_cdunieco_i",params.get("cdunieco"));
			paramAutEspecial.put("pv_cdramo_i",params.get("cdramo"));
			paramAutEspecial.put("pv_estado_i",params.get("estado"));
			paramAutEspecial.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramAutEspecial.put("pv_nmsuplem_i",params.get("nmsuplem"));
			paramAutEspecial.put("pv_nmsituac_i",params.get("nmsituac"));
			paramAutEspecial.put("pv_nmautesp_i",params.get("nmautesp"));
			paramAutEspecial.put("pv_nmsinies_i",params.get("nmsinies"));
			//paramAutEspecial.put("pv_cdperson_i",params.get("cdperson"));
			//paramAutEspecial.put("pv_cdtipsit_i",params.get("cdtipsit"));
			validacionGeneral = siniestrosManager.asociarAutorizacionEspecial(paramAutEspecial);
			logger.debug("validacionGeneral : {}", validacionGeneral);
		}catch( Exception e){
			logger.error("Error validaAutorizacionEspecial : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaDatosAutorizacionEspecial(){
		logger.debug("Entra a consultaDatosAseguradoSiniestro params de entrada :{}",params);
		try {
			datosValidacion = siniestrosManager.obtenerDatosAutorizacionEspecial(params.get("nmautespecial"));
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener los datos de la validacion del Siniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaExisteCoberturaTramite(){
		logger.debug("Entra a consultaExisteCoberturaTramite params de entrada :{}",params);
		try {
			datosValidacion = siniestrosManager.getConsultaExisteCoberturaTramite(params.get("ntramite"),params.get("tipoPago"));
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener las consultaExisteCoberturaTramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String validaLimiteCoberturaAsegurados(){
		logger.debug("Entra a obtieneUsuarioTurnado params de entrada :{}",params);
		
		try {
			Map<String,String> factura        = null;
			Map<String,String> siniestroIte   = null;
			List<Map<String,String>> facturasAux = siniestrosManager.obtenerFacturasTramite(params.get("ntramite"));
			String banderaValidacion = "0";
			String mensaje = "";
			for(int i = 0; i < facturasAux.size(); i++){
				factura = facturasAux.get(i);
				List<Map<String,String>> siniesxfactura = siniestrosManager.listaSiniestrosTramite2(params.get("ntramite"),factura.get("NFACTURA"));
				for( int j= 0; j < siniesxfactura.size();j++){
					siniestroIte    = siniesxfactura.get(j);
					double limite 		 		 ;
					double importePagado 		 ;
					double importePagarAsegurado ;
					
					try{
						limite = Double.valueOf(siniestroIte.get("LIMITE"));
					}catch(Exception ex){
						limite = 0d;
					}
					
					try{
						importePagado = Double.valueOf(siniestroIte.get("IMPPAGCOB"));
					}catch(Exception ex){
						importePagado = 0d;
					}
					
					try{
						importePagarAsegurado = Double.valueOf(siniestroIte.get("IMPORTETOTALPAGO"));
					}catch(Exception ex){
						importePagarAsegurado = 0d;
					}
					
					double importeDisponible		= (limite - importePagado);
					
					//logger.debug("limite :{} importePagado:{} importePagarAsegurado: {} importeDisponible:{}",limite,importePagado,importePagarAsegurado,importeDisponible);
					if(siniestroIte.get("VALTOTALCOB").equalsIgnoreCase("1")){
						logger.debug("Entra a 1");
						if(importeDisponible <=limite && +importePagarAsegurado <= importeDisponible){
							
						}else{
							//(EGS) Para los que sobrepasan el limite permitido, revisamos si tienen autorizacion especial
							StringBuilder msg = new StringBuilder();
							if((siniestroIte.get("REQAUTES").equalsIgnoreCase("1") && Integer.valueOf(siniestroIte.get("NMAUTESP")) > 0)){
								// Revisamos si la autorizacion especial es por excedente en límite de medicamentos
								try{
									String resp = siniestrosManager.validaAutEspLimMedi(siniestroIte.get("NMAUTESP"), params.get("ntramite"), factura.get("NFACTURA"), siniestroIte.get("CDUNIECO"), siniestroIte.get("CDRAMO"), siniestroIte.get("ESTADO"), siniestroIte.get("NMPOLIZA"), siniestroIte.get("NMSUPLEM"), siniestroIte.get("NMSITUAC"), siniestroIte.get("NMSINIES"));
									logger.debug("resp ===> :{}",resp);
									if(!(Integer.valueOf(resp) > 0)){
										banderaValidacion = "1";
										mensaje = msg.append(mensaje).append(" Siniestro ").append(siniestroIte.get("NMSINIES")).append(" sobrepasa l&iacute;mite permitido. Se requiere autorizaci&oacute;n especial por Excedente en L&iacute;mite. <br/>").toString();
									}
								}
								catch(Exception ex) {
									success=false;
									logger.error("Error al obtener facturas de tramite : {}", ex.getMessage(), ex);
									mensaje=ex.getMessage();
								}
							}else{ //(EGS) fin
								banderaValidacion = "1";
								//mensaje = mensaje + "El CR "+factura.get("NTRAMITE")+" la Factura " + factura.get("NFACTURA") + " del siniestro "+ siniestroIte.get("NMSINIES") + " sobrepasa el L&iacute;mite permitido. <br/>";
								mensaje = msg.append(mensaje).append(" Siniestro ").append(siniestroIte.get("NMSINIES")).append(" sobrepasa l&iacute;mite permitido. Se requiere autorizaci&oacute;n especial por Excedente en L&iacute;mite. <br/>").toString();
							}
						}
					}
				}
			}
			if(banderaValidacion.equalsIgnoreCase("1")){
				logger.debug("entra la validacion banderaValidacion" );
				success = false;
				msgResult = mensaje;
			}else{
				success = true;
				msgResult = "";
			}
		}catch( Exception e){
			logger.error("Error al consultar el periodo de espera en meses : {}", e.getMessage(), e);
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	
	public String validarMultiplesCRSISCO(){
		logger.debug("Entra a validarMultiplesCRSISCO params de entrada :{}",params);
		try {
			String tramites = params.get("ntramite");
			logger.debug("Valor del tramite ==>"+tramites);
			String totalTramites[] = tramites.split("\\|");
			logger.debug("Total de Registros"+totalTramites.length);
			loadList = new ArrayList<HashMap<String,String>>();
			String mensajeCRs="";
			String banderaTotal = "0";
			for(int i = 0; i < totalTramites.length; i++){
				logger.debug(totalTramites[i]);
				
				// Realizamos la validacion para cada uno de los conceptos 
				HashMap<String,String> params = new HashMap<String, String>();
				params.put("ntramite",totalTramites[i].toString());
				this.params=params;
				validaLimiteCoberturaAsegurados();
				logger.debug("Valores de respuesta ===> :{}  Mensaje : {}",success,msgResult);
				if(success == false){
					banderaTotal = "1";
					mensajeCRs = mensajeCRs+msgResult;
				}
			}
			
			if(banderaTotal.equalsIgnoreCase("1") ){
				logger.debug("if banderaTotal ===>> "+banderaTotal);
				success = false;
				msgResult = mensajeCRs;
			}else{
				logger.debug("else banderaTotal ===>> "+banderaTotal);
				success = true;
				msgResult = "";
			}
		}catch( Exception e){
			logger.error("Error al obtener las consultaExisteCoberturaTramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		//success = true;
		return SUCCESS;
	}
	
	public String pantallaInicial(){
		success = true;
		return SUCCESS;
	}
	
	
	public String loadListaSolicitudesCxp(){
		logger.debug("===> Entra a loadListaSolicitudesCxp  ===>");
		try {
			solicitudPago = consultasAseguradoManager.obtieneListadoSolicitudesCxp();
			logger.debug("Respuesta loadList : {}", solicitudPago);
		}catch( Exception e){
			logger.error("Error en loadListaDocumentos : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
    public String actualizarMultiSiniestro()
    {
    	this.session = ActionContext.getContext().getSession();
    	logger.debug(""
    			+ "\n######################################"
    			+ "\n######################################"
    			+ "\n###### actualizarMultiSiniestro ######"
    			+ "\n######                          ######"
    			);
    	logger.debug("params: "+params);
    	success = true;
		mensaje = "Siniestro actualizado";
    	logger.debug(""
    			+ "\n######                          ######"
    			+ "\n###### actualizarMultiSiniestro ######"
    			+ "\n######################################"
    			+ "\n######################################"
    			);
    	return SUCCESS;
    }
    
	public String validaArancelesTramitexProveedor(){
		logger.debug("Entra a consultaExisteCoberturaTramite params de entrada :{}",params);
		try {
			String tramites = params.get("ntramite");
			logger.debug("Valor del tramite ==>"+tramites);
			String totalTramites[] = tramites.split("\\|");
			logger.debug("Total de Registros"+totalTramites.length);
			
			loadList = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < totalTramites.length; i++){
				List<Map<String,String>>lista= siniestrosManager.getValidaArancelesTramitexProveedor(totalTramites[i]);
				for(Map<String,String>ele:lista){
					HashMap<String,String>map=new HashMap<String,String>();
					map.put("NTRAMITE"   , ele.get("NTRAMITE"));
					map.put("NFACTURA" , ele.get("NFACTURA"));
					map.put("NMSINIES" , ele.get("NMSINIES"));
					map.put("CDCONCEP" , ele.get("CDCONCEP"));
					loadList.add(map);
				}
			}
			logger.debug("Respuesta datosValidacion : {}", loadList);
		}catch( Exception e){
			logger.error("Error al obtener las consultaExisteCoberturaTramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String validaFacturaMontoPagoAutomatico(){
		logger.debug("Entra a consultaExisteCoberturaTramite params de entrada :{}",params);
		try {
			String tramites = params.get("ntramite");
			logger.debug("Valor del tramite ==>"+tramites);
			String totalTramites[] = tramites.split("\\|");
			logger.debug("Total de Registros"+totalTramites.length);
			
			loadList = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < totalTramites.length; i++){
				List<Map<String,String>>lista= siniestrosManager.getValidaFacturaMontoPagoAutomatico(totalTramites[i]);
				for(Map<String,String>ele:lista){
					HashMap<String,String>map=new HashMap<String,String>();
					map.put("NTRAMITE"   , ele.get("NTRAMITE"));
					map.put("NFACTURA" , ele.get("NFACTURA"));
					map.put("PTIMPORT" , ele.get("PTIMPORT"));
					loadList.add(map);
				}
			}
			logger.debug("Respuesta datosValidacion : {}", loadList);
		}catch( Exception e){
			logger.error("Error al obtener las consultaExisteCoberturaTramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String solicitarPagoAutomatico(){
		logger.debug("Entra a solicitarPagoAutorizacionAutomatica params de entrada :{}",params);
		try {
			String tramites = params.get("ntramite");
			logger.debug("Valor del tramite ==>"+tramites);
			String totalTramites[] = tramites.split("\\|");
			logger.debug("Total de Registros"+totalTramites.length);
			loadList = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < totalTramites.length; i++){
				List<Map<String,String>>lista= siniestrosManager.getValidaFacturaMontoPagoAutomatico(totalTramites[i]);
				logger.debug("Total de Registros ==> "+lista.size());
				
				if(lista.size() > 0){
					logger.debug("No se procesara para su pago");
				}else{
					logger.debug("Se mandar� a llamar al pago del siniestro ==> "+totalTramites[i].toString());
					
					HashMap<String,String> params = new HashMap<String, String>();
					params.put("pv_ntramite_i",totalTramites[i].toString());
					params.put("pv_tipmov_i",TipoPago.DIRECTO.getCodigo());
					this.params=params;
					solicitarPago();
					HashMap<String,String>map=new HashMap<String,String>();
					
					if(success){
						map.put("mensajeRespuesta"   , totalTramites[i].toString()+" El pago se realizo con \u00e9xito.");
					}else{
						map.put("mensajeRespuesta"   , totalTramites[i].toString()+" "+mensaje);
					}
					loadList.add(map);
				}
			}
		}catch( Exception e){
			logger.error("Error al obtener las consultaExisteCoberturaTramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String actualizaTurnadoMesaControl(){
		logger.debug("Entra a actualizaTurnadoMesaControl  Params: {}", params);
		try {
			siniestrosManager.actualizaTurnadoMesaControl(params.get("ntramite"));
		}catch( Exception e){
			logger.error("Error actualizaTurnadoMesaControl : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String procesarTramiteSiniestroSISCO(){
		logger.debug("Entra a procesarTramiteSiniestroSISCO Datos de Entrada");
		success =  true;
		try {
			// Validamos si existe registros en TMOVSISCO
			UserVO usuario=(UserVO)session.get("USUARIO");

			String cdrol    = usuario.getRolActivo().getClave();
			logger.debug("Valor de usuario :{} rol : {} usuarioGetUser :{} ",usuario,cdrol,usuario.getUser());
			String existeRegistros = siniestrosManager.existeRegistrosProcesarSISCO();
			logger.debug("Existe Registros ==> :{}",existeRegistros);
			if(existeRegistros.equalsIgnoreCase("S")){
				String mensajeM = "Se generaron los siguientes tr\u00e1mite : ";
				slist1 = siniestrosManager.procesaPagoAutomaticoSisco(usuario.getUser(), "0");
				logger.debug("Valor de respuesta : {}",slist1);
				for(int i=0;i<slist1.size();i++){
					if(i<slist1.size()-1){
						mensajeM = mensajeM + slist1.get(i).get("NTRAMITE")+",";
					}else{
						mensajeM = mensajeM + slist1.get(i).get("NTRAMITE");
					}
					HashMap<String,String> params = new HashMap<String, String>();
					params.put("ntramite",slist1.get(i).get("NTRAMITE"));
					this.params = params;
					generarCalculoSiniestros();
				}
				mensaje = mensajeM;
			}else{
				mensaje = "No existe registros a procesar.";
				success =  false;
			}
		}catch( Exception e){
			logger.error("Error en procesarTramiteSiniestroSISCO : {}", e.getMessage(), e);
			success =  false;
			return SUCCESS;
		}
		return SUCCESS;
	}
		
	public String procesarTramiteLayout(){
		logger.debug("Entra a procesarTramiteLayout Datos de Entrada :{}",params);
		success = true;
		try {
			String cdpresta =params.get("cdpresta");
			String nmconsult = params.get("layoutConf");
			String tipoproc=params.get("tipoproc");
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			logger.debug("cdpresta: {} nmconsult:{} tipoproc:{} usuario:{}",cdpresta,nmconsult,tipoproc,usuario.getUser());
			slist1 = siniestrosManager.procesaPagoAutomaticoLayout(cdpresta, nmconsult, tipoproc, usuario.getUser());
			logger.debug("VALORES DE RESPUESTA =>>>>>>>>>>>>>>>>>>"+slist1);
			String mensajeM = "Se generaron los siguientes tr\u00e1mite : ";
			for(int i=0;i<slist1.size();i++){
				if(i<slist1.size()-1){
					mensajeM = mensajeM + slist1.get(i).get("NTRAMITE")+",";
				}else{
					mensajeM = mensajeM + slist1.get(i).get("NTRAMITE");
				}
				HashMap<String,String> params = new HashMap<String, String>();
				params.put("ntramite",slist1.get(i).get("NTRAMITE"));
				this.params = params;
				
				if(cdpresta.equalsIgnoreCase("24104") && tipoproc.equalsIgnoreCase("1")){
					logger.debug("No entra a la generaci�n de los calculos ===> cdpresta :{} tipoProceso:{}",cdpresta,tipoproc);
					HashMap<String, Object> paramsPagoDirecto = new HashMap<String, Object>();
					paramsPagoDirecto.put("pv_ntramite_i",slist1.get(i).get("NTRAMITE"));
					String montoTramite = siniestrosManager.obtieneMontoTramitePagoDirecto(paramsPagoDirecto);
					logger.debug("Valor del Monto del Arancel ===>>>> "+montoTramite);
					Map<String,Object> otvalor = new HashMap<String,Object>();
					otvalor.put("pv_ntramite_i" , slist1.get(i).get("NTRAMITE"));
					otvalor.put("pv_otvalor03_i"  , montoTramite);
					siniestrosManager.actualizaOTValorMesaControl(otvalor);
					
				}else{
					logger.debug("entra a la generaci�n de los calculos ===> cdpresta :{} tipoProceso:{}",cdpresta,tipoproc);
					generarCalculoSiniestros();
				}
			}
			mensaje = mensajeM;
		}catch( Exception e){
			logger.error("Error en procesarTramiteLayout : ", e);
			success =  false;
			mensaje = "Error al procesar el tr\u00e1mite al momento de procesar.";
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	public String obtenerInfCoberturaInfonavit(){
		logger.debug("Entra a obtenerInfCoberturaInfonavit params de entrada :{}",params);
		try {
			HashMap<String, Object> paramsInfonavit = new HashMap<String, Object>();
			paramsInfonavit.put("pv_cdunieco_i",params.get("cdunieco"));
			paramsInfonavit.put("pv_cdramo_i",params.get("cdramo"));
			paramsInfonavit.put("pv_estado_i",params.get("estado"));
			paramsInfonavit.put("pv_nmpoliza_i",params.get("nmpoliza"));
			paramsInfonavit.put("pv_nmsuplem_i",params.get("nmsuplem"));
			paramsInfonavit.put("pv_nmsituac_i",params.get("nmsituac"));
			paramsInfonavit.put("pv_cdgarant_i",params.get("cdgarant"));
			paramsInfonavit.put("pv_cdconval_i",params.get("cdconval"));
			paramsInfonavit.put("pv_nmsinies_i",params.get("nmsinies"));
			paramsInfonavit.put("pv_swfonsin_i",params.get("swfonsin"));
			datosInformacionAdicional = siniestrosManager.obtieneInfCoberturaInfonavit(paramsInfonavit);
			logger.debug("Valor de Respuesta ==>>>"+datosInformacionAdicional);
		}catch( Exception e){
			logger.error("Error al obtenerInfCoberturaInfonavit : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaDatosAutEspecial(){
		logger.debug("Entra a consultaDatosAutEspecial params de entrada :{} ",params);
		try {
			datosValidacion = siniestrosManager.getConsultaDatosAutEspecial(params.get("cdramo"),params.get("tipoPago"),params.get("ntramite"),params.get("nfactura"),params.get("cdperson"));
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosAutEspecial : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	public String validaStatusAseguradoSeleccionado(){
		logger.debug("Entra a validaStatusAseguradoSeleccionado params de entrada :{}",params);
		try {
			String feocurre= params.get("feoocurre").substring(8,10)+"/"+params.get("feoocurre").substring(5,7)+"/"+params.get("feoocurre").substring(0,4);
			validacionGeneral = siniestrosManager.obtieneValidacionAsegurado(params.get("cdperson") ,renderFechas.parse(feocurre),params.get("nmpoliza"));
			logger.debug("validacionGeneral ==>: {} ",validacionGeneral);
			
		}catch( Exception e){
			logger.error("Error al obtener el monto del arancel : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	public String validaImporteAsegTramiteAseg(){
		logger.debug("Entra a validaImporteAsegTramiteAseg params de entrada :{}",params);
		try {
			datosValidacion = siniestrosManager.obtenerInfImporteAsegTramiteAseg(params.get("tipopago"),params.get("ntramite"),params.get("nfactura"));
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener los datos de la validacion del Siniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String validaImporteTramiteAsegurados(){
		logger.debug("Entra a validaImporteTramiteAsegurados params de entrada :{}",params);
		
		try {
			Map<String,String> factura        = null;
			Map<String,String> siniestroIte   = null;
			List<Map<String,String>> facturasAux = siniestrosManager.obtenerFacturasTramite(params.get("ntramite"));
			String banderaValidacion = "0";
			String mensaje = "";
			for(int i = 0; i < facturasAux.size(); i++){
				factura = facturasAux.get(i);
				List<Map<String,String>> siniesxfactura = siniestrosManager.obtenerInfImporteAsegTramiteAseg(params.get("tipopago"),params.get("ntramite"),factura.get("NFACTURA"));
				for( int j= 0; j < siniesxfactura.size();j++){
					siniestroIte    = siniesxfactura.get(j);
					if(Double.parseDouble(siniestroIte.get("IMPORTE"+"")) < 0d){
						banderaValidacion = "1";
						mensaje = mensaje + "El Siniestro "+siniestroIte.get("SINIESTRO")+" de la Factura " + siniestroIte.get("FACTURA") + " el importe es negativo. <br/>";
					}
				}
			}
			if(banderaValidacion.equalsIgnoreCase("1")){
				logger.debug("entra la validacion banderaValidacion" );
				success = false;
				msgResult = mensaje+"Favor de corregir el importe para poder continuar.<br/>";
			}else{
				success = true;
				msgResult = "";
			}
		}catch( Exception e){
			logger.error("Error al consultar validaImporteTramiteAsegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	/****************************METODOS DE APOYO PARA SERVIDOR *****************************************/
	
	public String consultaDatosMesaControl(){
		logger.debug("Entra a consultaDatosMesaControl params de entrada :{} ",params);
		try {
			String ntramite = params.get("ntramite");
			map1 = siniestrosManager.obtenerTramiteCompleto(ntramite);
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosMesaControl : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	public String guardaDatosMesaControl() {
		logger.debug("Entra a guardaDatosMesaControl params de entrada :{}",params);
		try {
			
			HashMap<String, Object> datosMC = new HashMap<String, Object>();
			datosMC.put("pv_accion_i" , 	params.get("txtaccion"));
			datosMC.put("pv_ntramite_i" , 	params.get("txtntramite"));
			datosMC.put("pv_cdsubram_i" , 	params.get("txtsubramo"));
			datosMC.put("pv_cdtiptra_i"  , 	params.get("txtproceso"));
			datosMC.put("pv_status_i"  , 	params.get("txtstatus"));
			datosMC.put("pv_cdtipsit_i"  , 	params.get("txttipsit"));
			datosMC.put("pv_otvalor04_i"  , params.get("txtotvalor04"));
			datosMC.put("pv_otvalor05_i"  , params.get("txtotvalor05"));
			datosMC.put("pv_otvalor16_i"  , params.get("txtotvalor16"));
			datosMC.put("pv_otvalor17_i"  , params.get("txtotvalor17"));
			datosMC.put("pv_otvalor20_i"  , params.get("txtotvalor20"));
			datosMC.put("pv_otvalor22_i"  , params.get("txtotvalor22"));
			datosMC.put("pv_otvalor26_i"  , params.get("txtotvalor26"));
			siniestrosManager.actualizaValoresMCSiniestros(datosMC);
			success = true;
			mensaje = "Tr\u00e1mite actualizado";
		}
		catch(Exception ex) {
			success=false;
			logger.error("error guardaDatosMesaControl : {}", ex.getMessage(), ex);
			mensaje = ex.getMessage();
		}
		return SUCCESS;
	}
	
	public String consultaDatosAsegurados(){
		logger.debug("Entra a consultaDatosAsegurados params de entrada :{}",params);
		try {
			String ntramite = params.get("ntramite");
			String nfactura = params.get("nfactura");			
			datosValidacion = siniestrosManager.obtenerAseguradosxTworksin(ntramite,nfactura);
			logger.debug("Respuesta datosValidacion : {}", datosValidacion);
		}catch( Exception e){
			logger.error("Error consultaDatosAsegurados : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String eliminarFaltantesAsegurados(){
		logger.debug("Entra a eliminarFaltantesAsegurados params de entrada :{}",params);
		try{
			siniestrosManager.eliminarFaltantesAsegurados();
			mensaje = "Asegurado eliminado";
			success = true;
		}catch(Exception ex){
			logger.debug("error al eliminar registro : {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
	
	
	public String eliminarAseguradosEspecifico(){
		logger.debug("Entra a eliminarAseguradosEspecifico params de entrada :{}",params);
		try{
			siniestrosManager.getEliminaAseguradoEspecifico(params.get("ntramite"),params.get("nfactura"),params.get("cdperson"),renderFechas.parse(params.get("ntramite")));
			mensaje = "Asegurado eliminado";
			success = true;
		}catch(Exception ex){
			logger.debug("error al eliminarAseguradosEspecifico: {}", ex.getMessage(), ex);
			success=false;
			mensaje=ex.getMessage();
		}
		return SUCCESS;
	}
	
    /**
    * Funcion que obtiene la lista de Sbcobertura
    * @param cdgarant
    * @param cdsubcob
    * @return Lista GenericVO con la informacion de los asegurados
    */    
    public String consultaListaTipoEventoSiniestro(){
        logger.debug("Entra a consultaListaTipoEventoSiniestro params de entrada :{}",params);
        try {
            UserVO usuarioR     = (UserVO)session.get("USUARIO");
            String cdrol        = usuarioR.getRolActivo().getClave();
            //cdunieco, cdramo, estado, nmpoliza, nmsituac, cdtipsit, cdgarant, cdsubcob
            datosValidacionGral= siniestrosManager.getConsultaListaTipoEvento(params.get("cdramo"), params.get("cdtipsit"),params.get("cdgarant"),params.get("cveCatalogo"));
        }catch( Exception e){
            logger.error("Error al consultaListaTipoEventoSiniestro la Lista de subcoberturas : {}", e.getMessage(), e);
            return SUCCESS;
        }
        success = true;
        return SUCCESS;
    }
    
    public String consultaDatosTipoEventoAlta(){
        logger.debug("Entra a consultaDatosTipoEventoAlta params de entrada :{}",params);
        try {
            datosValidacion = siniestrosManager.obtenerDatosValTipoEventoAlta(params.get("cdramo"),params.get("cdtipsit"),params.get("cdgarant"),params.get("cdconval"));
            logger.debug("Respuesta datosValidacion : {}", datosValidacion);
        }catch( Exception e){
            logger.error("Error al consultaDatosTipoEventoAlta del Siniestro : {}", e.getMessage(), e);
            return SUCCESS;
        }
        success = true;
        return SUCCESS;
    }
	
	/**
	 * (EGS) Valida solo un proveedor por reclamo pago directo
	 * @return - cantidad de proveedores involucrados en el reclamo
	 */
	public String validarProveedorPD(){
		logger.debug("Entra a validarProveedorPD Datos de entrada: {}",params);
		try{
			mensaje = siniestrosManager.validaProveedorPD(params.get("ntramite"));
			logger.debug(mensaje);
		}catch(Exception e){
			logger.debug("Error durante la validacion de proveedor, pago directo",e.getMessage(),e);
			success = false;
			mensaje = "";
		}
		return SUCCESS;
	}
	
	// (EGS)
	public String actualizarReqautes(){	
		logger.debug("Entra a actualizarReqautes {}", slist1);
		logger.debug("Valor de mapa {}",smap);
		try{
			for(int i = 0; i < slist1.size(); i++){
				mensaje = siniestrosManager.actualizarReqautes(slist1.get(i).get("REQAUTES"), smap.get("ntramite"), smap.get("nfactura"), slist1.get(i).get("CDUNIECO"), 
													 slist1.get(i).get("CDRAMO"), slist1.get(i).get("ESTADO"), slist1.get(i).get("NMPOLIZA"), 
													 slist1.get(i).get("NMSUPLEM"), slist1.get(i).get("NMSITUAC"), slist1.get(i).get("AAAPERTU"), 
													 slist1.get(i).get("STATUS"), slist1.get(i).get("NMSINIES"));
			}
		}catch(Exception e){
			logger.debug("Error en actualizarReqautes", e.getMessage(),e);
			success = false;
			mensaje = "";
		}
		return SUCCESS;
	}
	
	public String reservaSiniestralidad(){
		logger.info(
				new StringBuilder()
				.append("\n#####################################")
				.append("\n###### reservaSiniestralidad ########")
				.append("\n###### smap1=").append(smap)
				.toString()
				);
		
		success = true;
		
		String cdsisrol = null;
		
		//datos completos
		try
		{
			UserVO usuario = (UserVO)session.get("USUARIO");
			cdsisrol = usuario.getRolActivo().getClave();
			ManagerRespuestaImapVO managerResponse = siniestrosManager.pantallaReservaSiniestralidad(cdsisrol);
			exito           = managerResponse.isExito();
			respuesta       = managerResponse.getRespuesta();
			respuestaOculta = managerResponse.getRespuestaOculta();
			if(exito)
			{
				imap = managerResponse.getImap();
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			success         = false;
			respuesta       = new StringBuilder("Error al obtener atributos de pantalla #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		logger.info(
				new StringBuilder()
				.append("\n###### slist1=").append(slist1)
				.append("\n###### reservaSiniestralidad #########")
				.append("\n#####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String consultaRenovaSiniestralidad(){
		logger.debug("Entra a consultaDatosAutEspecial params de entrada :{} ",params);
		try {
			String pv_CdUniEco_i = params.get("pv_CdUniEco_i")
                  ,pv_CdRamo_i   = params.get("pv_CdRamo_i")
                  ,pv_nmpoliza_i = params.get("pv_nmpoliza_i")
                  ,pv_cdperson   = params.get("pv_cdperson")
                  ,pv_nmsinies   = params.get("pv_nmsinies")
                  ,pv_fecdesde   = params.get("pv_fecdesde")
                  ,pv_fechasta   = params.get("pv_fechasta")
                  ,pv_start_i    = params.get("pv_start_i")
                  ,pv_limit_i    = params.get("pv_limit_i")
                  ,pv_ntramite_i = params.get("pv_ntramite_i")
					,pv_top=null;
			datosValidacion = siniestrosManager.getDatosRenovaSiniestralidad(pv_CdUniEco_i 
																			,pv_CdRamo_i  
																			,pv_nmpoliza_i
																			,pv_cdperson  
																			,pv_nmsinies
																			,pv_fecdesde  
																			,pv_fechasta  
																			,pv_start_i
																			,pv_limit_i 
																			,pv_ntramite_i);
			
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosAutEspecial : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	public String listaProveedores(){
		logger.debug("Entra a listaProveedores params de entrada :{} ",params);
		try {
			String pv_cdpresta_i = params.get("pv_cdpresta_i")
                  ,pv_idespecialidad_i   = params.get("pv_idespecialidad_i")
                  ,pv_tipoProveedor_i = params.get("pv_tipoProveedor_i")
                  ,pv_idZonaHosp_i   = params.get("pv_idZonaHosp_i")
                  ;
			slist1 = siniestrosManager.obtieneListaProveedores(pv_cdpresta_i, pv_idespecialidad_i, pv_tipoProveedor_i, pv_idZonaHosp_i);
			
			logger.debug("Respuesta listaProveedores : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener listaProveedores : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	public String topIcd(){
		
		logger.debug("Entra a topIcd params de entrada :{} ",params);
		try {
			String pv_CdUniEco_i = params.get("pv_CdUniEco_i")
                  ,pv_CdRamo_i   = params.get("pv_CdRamo_i")
                  ,pv_nmpoliza_i = params.get("pv_nmpoliza_i")
                  ,pv_cdperson   = params.get("pv_cdperson")
                  ,pv_nmsinies   = params.get("pv_nmsinies")
                  ,pv_fecdesde   = params.get("pv_fecdesde")
                  ,pv_fechasta   = params.get("pv_fechasta")
                  ,pv_top 		 = params.get("pv_top");
			
		
			slist1=siniestrosManager.obtieneListaTopIcd(pv_CdUniEco_i
																	, pv_CdRamo_i
																	, pv_nmpoliza_i
																	, pv_cdperson
																	, pv_nmsinies
																	, pv_fecdesde
																	, pv_fechasta
																	, pv_top);
			
			
			
			
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosAutEspecial : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
		
	}
	
	public String reservas(){
		
		logger.debug("Entra a reservas params de entrada :{} ",params);
		try {
			String pv_CdUniEco_i = params.get("pv_CdUniEco_i")
                  ,pv_CdRamo_i   = params.get("pv_CdRamo_i")
                  ,pv_nmpoliza_i = params.get("pv_nmpoliza_i")
                  ,pv_cdperson   = params.get("pv_cdperson")
                  ,pv_nmsinies   = params.get("pv_nmsinies")
                  ,pv_fecdesde   = params.get("pv_fecdesde")
                  ,pv_fechasta   = params.get("pv_fechasta")
                  ,pv_top 		 = params.get("pv_top");
			
		
			slist1=siniestrosManager.obtieneListaReservasSolo(pv_CdUniEco_i
					, pv_CdRamo_i
					, pv_nmpoliza_i
					, pv_cdperson
					, pv_nmsinies
					, pv_fecdesde
					, pv_fechasta);
			
			
			
			
			logger.debug("Respuesta reservas : {}",slist1);
		}catch( Exception e){
			logger.error("Error al obtener reservas : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
		
	}
	
public String reservasTipPag(){
		
		logger.debug("Entra a reservasTipPag params de entrada :{} ",params);
		try {
			String pv_CdUniEco_i = params.get("pv_CdUniEco_i")
                  ,pv_CdRamo_i   = params.get("pv_CdRamo_i")
                  ,pv_nmpoliza_i = params.get("pv_nmpoliza_i")
                  ,pv_cdperson   = params.get("pv_cdperson")
                  ,pv_nmsinies   = params.get("pv_nmsinies")
                  ,pv_fecdesde   = params.get("pv_fecdesde")
                  ,pv_fechasta   = params.get("pv_fechasta")
                  ,pv_top 		 = params.get("pv_top");
			
		
			slist1=siniestrosManager.obtieneListaReservas(pv_CdUniEco_i
					, pv_CdRamo_i
					, pv_nmpoliza_i
					, pv_cdperson
					, pv_nmsinies
					, pv_fecdesde
					, pv_fechasta);
			
			
			
			
			logger.debug("Respuesta reservasTipPag : {}",slist1);
		}catch( Exception e){
			logger.error("Error al obtener reservasTipPag : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
		
	}

    public String consultaListaDetalleSiniestro(){
	logger.debug("Entra a consultaListaDetalleSiniestro params de entrada :{}",params);
	try {
		String   pv_CdUniEco_i= params.get("pv_CdUniEco_i")
				,pv_CdRamo_i  = params.get("pv_CdRamo_i")
				,pv_nmpoliza_i= params.get("pv_nmpoliza_i")
				,pv_cdperson  = params.get("pv_cdperson")
				,pv_ntramite_i= params.get("pv_ntramite_i")
				,pv_nmsinies  = params.get("pv_nmsinies")
				,pv_fecdesde  = params.get("pv_fecdesde")
				,pv_fechasta = params.get("pv_fechasta");
		
		datosValidacion  = siniestrosManager.getConsultaListaDetalleSiniestro(   pv_CdUniEco_i
																				,pv_CdRamo_i  
																				,pv_nmpoliza_i
																				,pv_cdperson  
																				,pv_ntramite_i
																				,pv_nmsinies  
																				,pv_fecdesde  
																				,pv_fechasta );
		//if(datosValidacion!=null && !datosValidacion.isEmpty())	datosValidacion = datosValidacion;
	}catch( Exception e){
		logger.error("Error al obtener la lista Detalle Siniestro : {}", e.getMessage(), e);
		return SUCCESS;
	}
	success = true;
	return SUCCESS;
}	
		
    
	/****************************GETTER Y SETTER *****************************************/
	public List<GenericVO> getListaTipoAtencion() {
		return listaTipoAtencion;
	}

	public void setListaTipoAtencion(List<GenericVO> listaTipoAtencion) {
		this.listaTipoAtencion = listaTipoAtencion;
	}

	public String getMsgResult() {
		return msgResult;
	}

	public void setMsgResult(String msgResult) {
		this.msgResult = msgResult;
	}

	public String getExistePenalizacion() {
		return existePenalizacion;
	}

	public void setExistePenalizacion(String existePenalizacion) {
		this.existePenalizacion = existePenalizacion;
	}

	public List<PolizaVigenteVO> getPolizaUnica() {
		return polizaUnica;
	}

	public void setPolizaUnica(List<PolizaVigenteVO> polizaUnica) {
		this.polizaUnica = polizaUnica;
	}

	public void setListaFacturas(List<ListaFacturasVO> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}

	public List<HashMap<String, String>> getDatosTablas() {
		return datosTablas;
	}

	public void setDatosTablas(List<HashMap<String, String>> datosTablas) {
		this.datosTablas = datosTablas;
	}

	public String execute() throws Exception {
		success = true;
		return SUCCESS;
	}

	public List<GenericVO> getListaCPTICD() {
		return listaCPTICD;
	}

	public List<ListaFacturasVO> getListaFacturas() {
		return listaFacturas;
	}

	public void setListaCPTICD(List<GenericVO> listaCPTICD) {
		this.listaCPTICD = listaCPTICD;
	}

	public List<GenericVO> getListaSubcobertura() {
		return listaSubcobertura;
	}

	public void setListaSubcobertura(List<GenericVO> listaSubcobertura) {
		this.listaSubcobertura = listaSubcobertura;
	}

	public List<CoberturaPolizaVO> getListaCoberturaPoliza() {
		return listaCoberturaPoliza;
	}

	public void setListaCoberturaPoliza(List<CoberturaPolizaVO> listaCoberturaPoliza) {
		this.listaCoberturaPoliza = listaCoberturaPoliza;
	}

	public List<GenericVO> getListaCausaSiniestro() {
		return listaCausaSiniestro;
	}

	public void setListaCausaSiniestro(List<GenericVO> listaCausaSiniestro) {
		this.listaCausaSiniestro = listaCausaSiniestro;
	}

	public List<PolizaVigenteVO> getListaPoliza() {
		return listaPoliza;
	}

	public void setListaPoliza(List<PolizaVigenteVO> listaPoliza) {
		this.listaPoliza = listaPoliza;
	}

	public boolean isSuccess() {
		return success;
	}

	public HashMap<String, String> getParams() {
		return params;
	}
	

	public List<GenericVO> getListaAsegurado() {
		return listaAsegurado;
	}

	public Date getDate(String date){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(date);
		} catch (ParseException ex) {
		}
		return null;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public void setListaAsegurado(List<GenericVO> listaAsegurado) {
		this.listaAsegurado = listaAsegurado;
	}

	public List<ConsultaTTAPVAATVO> getListaConsultaTTAPVAATV() {
		return listaConsultaTTAPVAATV;
	}

	public void setListaConsultaTTAPVAATV(
		List<ConsultaTTAPVAATVO> listaConsultaTTAPVAATV) {
		this.listaConsultaTTAPVAATV = listaConsultaTTAPVAATV;
	}



	public List<HashMap<String, String>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<HashMap<String, String>> loadList) {
		this.loadList = loadList;
	}

	public void setSaveList(List<HashMap<String, String>> saveList) {
		this.saveList = saveList;
	}

	public HashMap<String, Object> getParamsO() {
		return paramsO;
	}

	public void setParamsO(HashMap<String, Object> paramsO) {
		this.paramsO = paramsO;
	}

	public void setKernelManagerSustituto(
		KernelManagerSustituto kernelManagerSustituto) {
		this.kernelManagerSustituto = kernelManagerSustituto;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public Map<String, String> getSmap() {
		return smap;
	}

	public void setSmap(Map<String, String> smap) {
		this.smap = smap;
	}

	public String getSmapJson(){
		String r=null;
		try{
			r=JSONUtil.serialize(smap);
		}
		catch (JSONException ex){
			logger.error("error al convertir smap a json",ex);
		}
		return r;
	}

	public Map<String, String> getSmap2() {
		return smap2;
	}

	public void setSmap2(Map<String, String> smap2) {
		this.smap2 = smap2;
	}

	public String getSmap2Json(){
		String r=null;
		try{
			r=JSONUtil.serialize(smap2);
		}
		catch (JSONException ex){
			logger.error("error al convertir smap2 a json",ex);
		}
		return r;
	}

	public Map<String, String> getSmap3() {
		return smap3;
	}

	public void setSmap3(Map<String, String> smap3) {
		this.smap3 = smap3;
	}

	public String getSmap3Json(){
		String r=null;
		try{
			r=JSONUtil.serialize(smap3);
		}
		catch (JSONException ex){
			logger.error("error al convertir smap3 a json",ex);
		}
		return r;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public String getSlist1Json() {
		String r=null;
		try{
			r=JSONUtil.serialize(slist1);
		}
		catch (JSONException ex){
			logger.error("error al convertir slist1 a json",ex);
		}
		return r;
	}

	public String getSlist2Json() {
		String r=null;
		try{
			r=JSONUtil.serialize(slist2);
		}
		catch (JSONException ex){
			logger.error("error al convertir slist2 a json",ex);
		}
		return r;
	}

	public String getLlist1Json() {
		String r=null;
		try{
			r=JSONUtil.serialize(llist1);
		}
		catch (JSONException ex){
			logger.error("error al convertir llist1 a json",ex);
		}
		return r;
	}

	public String getSlist3Json() {
		String r=null;
		try{
			r=JSONUtil.serialize(slist3);
		}
		catch (JSONException ex){
			logger.error("error al convertir slist3 a json",ex);
		}
		return r;
	}

	public String getLhospJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(lhosp);
		}
		catch (JSONException ex){
			logger.error("error al convertir lhosp a json",ex);
		}
		return r;
	}

	public String getLpdirJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(lpdir);
		}
		catch (JSONException ex){
			logger.error("error al convertir lpdir a json",ex);
		}
		return r;
	}

	public String getLpremJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(lprem);
		}
		catch (JSONException ex){
			logger.error("error al convertir lprem a json",ex);
		}
		return r;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public List<List<Map<String, String>>> getLlist1() {
		return llist1;
	}

	public void setLlist1(List<List<Map<String, String>>> llist1) {
		this.llist1 = llist1;
	}

	public List<Map<String, String>> getLhosp() {
		return lhosp;
	}

	public void setLhosp(List<Map<String, String>> lhosp) {
		this.lhosp = lhosp;
	}

	public List<Map<String, String>> getSlist3() {
		return slist3;
	}

	public void setSlist3(List<Map<String, String>> slist3) {
		this.slist3 = slist3;
	}

	public List<Map<String, String>> getLpdir() {
		return lpdir;
	}

	public void setLpdir(List<Map<String, String>> lpdir) {
		this.lpdir = lpdir;
	}

	public List<Map<String, String>> getLprem() {
		return lprem;
	}

	public void setLprem(List<Map<String, String>> lprem) {
		this.lprem = lprem;
	}

	public String getPorcentajePenalizacion() {
		return porcentajePenalizacion;
	}

	public void setPorcentajePenalizacion(String porcentajePenalizacion) {
		this.porcentajePenalizacion = porcentajePenalizacion;
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

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public List<MesaControlVO> getListaMesaControl() {
		return listaMesaControl;
	}

	public void setListaMesaControl(List<MesaControlVO> listaMesaControl) {
		this.listaMesaControl = listaMesaControl;
	}

	public List<Map<String, String>> getListaImportesWS() {
		return listaImportesWS;
	}

	public String getListaImportesWebServiceJson() {
		try {
			return JSONUtil.serialize(listaImportesWS);
		} catch (Exception e) {
			logger.error("Error al generar JSON de listaImportesWS",e);
			return null;
		}
	}

	public void setListaImportesWS(List<Map<String, String>> listaImportesWS) {
		this.listaImportesWS = listaImportesWS;
	}

	public String getIMPORTE_WS_IMPORTE() {
		return IMPORTE_WS_IMPORTE;
	}

	public String getIMPORTE_WS_IVA() {
		return IMPORTE_WS_IVA;
	}

	public String getIMPORTE_WS_IVR() {
		return IMPORTE_WS_IVR;
	}

	public String getIMPORTE_WS_ISR() {
		return IMPORTE_WS_ISR;
	}

	public List<Map<String, String>> getFacturasxTramite() {
		return facturasxTramite;
	}

	public void setFacturasxTramite(List<Map<String, String>> facturasxTramite) {
		this.facturasxTramite = facturasxTramite;
	}

	public List<Map<String, String>> getDatosPenalizacion() {
		return datosPenalizacion;
	}

	public List<Map<String, String>> getDatosCoberturaxCal() {
		return datosCoberturaxCal;
	}

	public void setDatosPenalizacion(List<Map<String, String>> datosPenalizacion) {
		this.datosPenalizacion = datosPenalizacion;
	}

	public void setDatosCoberturaxCal(List<Map<String, String>> datosCoberturaxCal) {
		this.datosCoberturaxCal = datosCoberturaxCal;
	}

	public String getDatosPenalizacionJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(datosPenalizacion);
		}
		catch (JSONException ex){
			logger.error("error al convertir datosPenalizacion a json",ex);
		}
		return r;
	}

	public String getDatosCoberturaxCalJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(datosCoberturaxCal);
		}
		catch (JSONException ex){
			logger.error("error al convertir datosCoberturaxCal a json",ex);
		}
		return r;
	}

	public String getMontoArancel() {
		return montoArancel;
	}

	public void setMontoArancel(String montoArancel) {
		this.montoArancel = montoArancel;
	}

	public List<GenericVO> getListadoRamosSalud() {
		return listadoRamosSalud;
	}

	public void setListadoRamosSalud(List<GenericVO> listadoRamosSalud) {
		this.listadoRamosSalud = listadoRamosSalud;
	}

	public Map<String, String> getMap1() {
		return map1;
	}

	public List<Map<String, String>> getDatosInformacionAdicional() {
		return datosInformacionAdicional;
	}

	public void setDatosInformacionAdicional(List<Map<String, String>> datosInformacionAdicional) {
		this.datosInformacionAdicional = datosInformacionAdicional;
	}

	public void setMap1(Map<String, String> map1) {
		this.map1 = map1;
	}

	public List<Map<String, String>> getDatosValidacion() {
		return datosValidacion;
	}

	public void setDatosValidacion(List<Map<String, String>> datosValidacion) {
		this.datosValidacion = datosValidacion;
	}

	public List<Map<String, Object>> getFacturasxSiniestro() {
		return facturasxSiniestro;
	}

	public void setFacturasxSiniestro(List<Map<String, Object>> facturasxSiniestro) {
		this.facturasxSiniestro = facturasxSiniestro;
	}

	public String getFacturasxSiniestroJson() {
		String r=null;
		try{
			r=JSONUtil.serialize(facturasxSiniestro);
		}
		catch (JSONException ex){
			logger.error("error al convertir slist1 a json",ex);
		}
		return r;
	}

	public String getUsuarioTurnadoSiniestro() {
		return usuarioTurnadoSiniestro;
	}

	public void setUsuarioTurnadoSiniestro(String usuarioTurnadoSiniestro) {
		this.usuarioTurnadoSiniestro = usuarioTurnadoSiniestro;
	}

	public String getValidacionGeneral() {
		return validacionGeneral;
	}

	public void setValidacionGeneral(String validacionGeneral) {
		this.validacionGeneral = validacionGeneral;
	}

	public List<SolicitudCxPVO> getSolicitudPago() {
		return solicitudPago;
	}

	public void setSolicitudPago(List<SolicitudCxPVO> solicitudPago) {
		this.solicitudPago = solicitudPago;
	}

	public List<GenericVO> getDatosValidacionGral() {
		return datosValidacionGral;
	}

	public void setDatosValidacionGral(List<GenericVO> datosValidacionGral) {
		this.datosValidacionGral = datosValidacionGral;
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
	
	public Map<String, List<Map<String, String>>> getGraficas() {
		return graficas;
	}

	public void setGraficas(Map<String, List<Map<String, String>>> graficas) {
		this.graficas = graficas;
	}
}