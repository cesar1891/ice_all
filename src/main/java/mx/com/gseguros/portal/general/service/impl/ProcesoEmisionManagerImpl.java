package mx.com.gseguros.portal.general.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.WSEmisionAutoException;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.emision.model.EmisionVO;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.general.dao.AseguradoDAO;
import mx.com.gseguros.portal.general.service.ProcesoEmisionManager;
import mx.com.gseguros.portal.general.service.ServiciosManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.emision.model.EmisionAutosVO;
import mx.com.gseguros.ws.autosgs.service.EmisionAutosService;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

@Service
public class ProcesoEmisionManagerImpl implements ProcesoEmisionManager {
	
	private static Logger logger = LoggerFactory.getLogger(ProcesoEmisionManagerImpl.class);
	
	@Value("${caratula.impresion.autos.url}")
	private String caratulaImpresionAutosURL;
	
	@Value("${caratula.impresion.autos.serviciopublico.url}")
	private String caratulaImpresionAutosServicioPublicoURL;
	
	@Value("${caratula.impresion.autos.flotillas.url}")
	private String caratulaImpresionAutosFlotillasURL;
	
	@Value("${recibo.impresion.autos.url}")
	private String reciboImpresionAutosURL;
	
	@Value("${caic.impresion.autos.url}")
	private String caicImpresionAutosURL;

	@Value("${aeua.impresion.autos.url}")
	private String aeuaImpresionAutosURL;
	
	@Value("${ap.impresion.autos.url}")
	private String apImpresionAutosURL;
	
	@Value("${incisos.flotillas.impresion.autos.url}")
	private String incisosFlotillasImpresionAutosURL;
	
	@Value("${incisos.flotillas.excel.impresion.autos.url}")
	private String incisosFlotillasExcelImpresionAutosUrl;		
	
	@Value("${tarjeta.iden.impresion.autos.url}")
	private String tarjetaIdenImpresionAutosURL;

	@Value("${numero.incisos.reporte}")
	private String numIncisosReporte;
	
	@Value("${manual.agente.txtinfocobredgs}")
	private String urlImpresionCobReduceGS;
	
	@Value("${manual.agente.txtinfocobgesgs}")
	private String urlImpresionCobGestoriaGS;
	
	@Value("${manual.agente.condgralescobsegvida}")
	private String urlImpresionCondicionesSegVida;
	
	@Value("${rdf.emision.nombre.esp.cobvida}")
	private String rdfEspecSeguroVida;

	//Datos para el servidor de Reportes
	@Value("${ruta.servidor.reports}")
	private String rutaServidorReportes;

	@Value("${pass.servidor.reports}")
	private String passServidorReportes;

	@Value("${ruta.documentos.poliza}")
	private String rutaDocumentosPoliza;
	
	@Value("${caratula.impresion.autos.docextra.url}")
    private String caratulaImpresionAutosDocExtra;
	
	@Autowired
	private ServiciosManager serviciosManager;
	
	@Autowired
	private Ice2sigsService ice2sigsService;
	
	@Autowired
	@Qualifier("emisionAutosServiceImpl")
	private EmisionAutosService emisionAutosService;
	
	@Autowired
	private RecibosSigsService recibosSigsService;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private AseguradoDAO aseguradoDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
	private ConsultasPolizaDAO consultasPolizaDAO;
	
	@Autowired
	private EmisionDAO emisionDAO;
	
	@Autowired
	private PersonasDAO personasDAO;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private EmisionManager emisionManager;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Override
	public Map<String, String> emitir(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String cdtipsit, String ntramite, String cdusuari, String cdsisrol, String cdelemento,
            String cveusuariocaptura, boolean esFlotilla, String tipoGrupoInciso, String polizaremota)
            throws Exception {
		
		Map<String, String> result = new HashMap<String, String>();
		
		String paso = null;
		
		try {
		    Date fechaHoy = new Date();
		    
			// Datos de la sesion del usuario:
			paso = "Obteniendo datos del usuario";
			DatosUsuario datUs = cotizacionDAO.cargarInformacionUsuario(cdusuari, cdtipsit);
			String cdpersonSesion = datUs.getCdperson();
			
			UserVO us = new UserVO();
			us.setUser(cdusuari);
			us.setClaveUsuarioCaptura(cveusuariocaptura);
			
			String nmpolAlt   = null; //this.nmpolAlt
			String sucursalGS = null; //this.sucursalGS
			String cdRamoGS   = null; //this.cdRamoGS
			
		    ////// validar edad asegurados
			paso = "Validando la edad de los asegurados";
			
			boolean necesitaAutorizacion = false;
			List<Map<String,String>> listaAseguradosEdadInvalida = aseguradoDAO.validaEdadAsegurados(cdunieco, cdramo, estado, nmpoliza, "0");
			
			if(listaAseguradosEdadInvalida != null && listaAseguradosEdadInvalida.size() > 0) {
				
				necesitaAutorizacion = true;
				
				result.put("necesitaAutorizacion" , "S");
				
				String msjeEnvio = "La p\u00f3liza se envi\u00f3 a autorizaci\u00f3n debido a que:<br/>";
				for(Map<String, String> iAseguradoEdadInvalida : listaAseguradosEdadInvalida) {
					msjeEnvio = msjeEnvio + iAseguradoEdadInvalida.get("NOMBRE");
					if(iAseguradoEdadInvalida.get("SUPERAMINI").substring(0, 1).equals("-")) {
						msjeEnvio = msjeEnvio + " no llega a la edad de "+iAseguradoEdadInvalida.get("EDADMINI")+" a&ntilde;os<br/>";
					} else {
						msjeEnvio = msjeEnvio + " supera la edad de "+iAseguradoEdadInvalida.get("EDADMAXI")+" a&ntilde;os<br/>";
					}
				}
	        	
	        	RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
	        	        cdusuari,
	        	        cdsisrol,
	        	        ntramite,
	        	        EstatusTramite.EN_ESPERA_DE_AUTORIZACION.getCodigo(),
	        	        msjeEnvio,
	        	        null,  // cdrazrecha
	        	        null,  // cdusuariDes
	        	        null,  // cdsisrolDes
	        	        false, // permisoAgente
	        	        false, // porEscalamiento
	        	        fechaHoy,
	        	        false  // sinGrabarDetalle
	        	        );
	        	
	        	msjeEnvio = Utils.join(msjeEnvio, ". ", despacho.getMessage());
	        	
				throw new ApplicationException(msjeEnvio, "Error " + paso);
			}
			
			
			if(!necesitaAutorizacion) {
				paso = "Validando cuadro de comisi\u00f3n natural";
				boolean cuadroNatural = cotizacionDAO.validarCuadroComisionNatural(cdunieco, cdramo, estado, nmpoliza);
				
				if(!cuadroNatural) {
					necesitaAutorizacion = true;
					result.put("necesitaAutorizacion", "S");
					String msjeAutorizacion = "La p\u00f3liza se envi\u00f3 a autorizaci\u00f3n debido a que se cambio el cuadro de comisiones";
					
					RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
	                        cdusuari,
	                        cdsisrol,
	                        ntramite,
	                        EstatusTramite.EN_ESPERA_DE_AUTORIZACION.getCodigo(),
	                        msjeAutorizacion,
	                        null,  // cdrazrecha
	                        null,  // cdusuariDes
	                        null,  // cdsisrolDes
	                        false, // permisoAgente
	                        false, // porEscalamiento
	                        fechaHoy,
	                        false  // sinGrabarDetalle
	                        );
	                
					msjeAutorizacion = Utils.join(msjeAutorizacion, ". ", despacho.getMessage());
					
					throw new ApplicationException(msjeAutorizacion, "Error " + paso);
				}
			}
			
			////// obtener forma de pago
			paso = "Obteniendo forma de pago";
			String cdperpag;
			Map<String, String> infoPoliza = consultasDAO.cargarInformacionPoliza(cdunieco, cdramo, estado, nmpoliza, cdusuari);
			logger.debug("poliza a emitir: " + infoPoliza);
			cdperpag = infoPoliza.get("cdperpag");
			
			
			////// emision
			paso = "Emitiendo poliza";
			
			EmisionVO emision = null;
			if(cdramo.equals(Ramo.RECUPERA.getCdramo()) && cdtipsit.equals(TipoSituacion.RECUPERA_INDIVIDUAL.getCdtipsit())) {
			    // Caso especial para Recupera Individual:
			    emision = emisionDAO.emitirPolizaRemotaRecupera(cdusuari, cdunieco, cdramo, estado, nmpoliza, 
                    "1", "0", cdelemento, cdunieco, null, cdperpag, 
                    cdpersonSesion, new Date(), ntramite, polizaremota);
			} else {
			    
			    emision = emisionDAO.emitir(cdusuari, cdunieco, cdramo, estado, nmpoliza, 
                    "1", "0", cdelemento, cdunieco, null, cdperpag, 
                    cdpersonSesion, new Date(), ntramite);
			}
			logger.debug("Emision obtenida: " + emision);
			
			String nmpolizaEmitida = emision.getNmpoliza();
			String nmpoliexEmitida = emision.getNmpoliex();
			String nmsuplemEmitida = emision.getNmsuplem();
			String esDxN           = emision.getEsDxN();
			String cdIdeperRes     = emision.getCdideper();
			String tipoMov         = TipoTramite.POLIZA_NUEVA.getCdtiptra();
			
			result.put("nmpoliza", nmpolizaEmitida);
			result.put("nmsuplem", nmsuplemEmitida);
			result.put("cdIdeper", cdIdeperRes);
			result.put("nmpoliza", nmpolizaEmitida);
			result.put("nmpoliex", nmpoliexEmitida);
			
			try {
            	serviciosManager.grabarEvento(new StringBuilder("\nEmision")
            	    ,Constantes.MODULO_EMISION  //cdmodulo
            	    ,Constantes.EVENTO_EMISION  //cdevento
            	    ,new Date() //fecha
            	    ,cdusuari
            	    ,cdsisrol
            	    ,ntramite
            	    ,cdunieco
            	    ,cdramo
            	    ,"M"
            	    ,nmpolizaEmitida
            	    ,nmpoliza
            	    ,null
            	    ,null
            	    ,null);
            } catch(Exception ex) {
            	logger.error("Error al grabar evento, sin impacto funcional", ex);
            }
			
			String rutaCarpeta = rutaDocumentosPoliza+"/"+ntramite;
			
			// Lanzando WS de cliente y recibos
			paso = "Lanzando WS de cliente y recibos";
			
			////// ws cliente y recibos
			String _cdunieco = cdunieco;
			String _cdramo   = cdramo;
			String edoPoliza = "M";
			String _nmpoliza = nmpolizaEmitida;
			String _nmsuplem = nmsuplemEmitida;
			String sucursal = cdunieco;
			if("1".equals(sucursal)) {
				sucursal = "1000";
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> Parametros para WS de cliente, Recibos y Autos: <<<<<<<<<<<<<<<<<<<<<<< ");
			logger.debug(">>>>>>>>>> cdunieco: "   + _cdunieco);
			logger.debug(">>>>>>>>>> cdramo: "     + _cdramo);
			logger.debug(">>>>>>>>>> estado: "     + edoPoliza);
			logger.debug(">>>>>>>>>> nmpoliza: "   + _nmpoliza);
			logger.debug(">>>>>>>>>> suplemento: " + _nmsuplem);
			logger.debug(">>>>>>>>>> sucursal: "   + sucursal);
			logger.debug(">>>>>>>>>> nmsolici: "   + nmpoliza);
			logger.debug(">>>>>>>>>> nmtramite: "  + ntramite);
			
			//ws cliente
			paso = "Creando cliente en WS";
			boolean retryWS  = false;
			boolean retryRec = false;
			if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
		    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)) {
				
				if(StringUtils.isBlank(cdIdeperRes)){
					
					ClienteGeneralRespuesta resCli = ice2sigsService.ejecutaWSclienteGeneral(
							_cdunieco, _cdramo, edoPoliza, _nmpoliza, _nmsuplem, ntramite, null, Ice2sigsService.Operacion.INSERTA, null, us, false);
					
					if(resCli != null && Ice2sigsService.Estatus.EXITO.getCodigo() == resCli.getCodigo() && ArrayUtils.isNotEmpty(resCli.getClientesGeneral())){
						cdIdeperRes = resCli.getClientesGeneral()[0].getNumeroExterno();
						if(StringUtils.isNotBlank(cdIdeperRes) && !cdIdeperRes.equalsIgnoreCase("0") && !cdIdeperRes.equalsIgnoreCase("0L")){
							
							personasDAO.actualizaCdideper(_cdunieco, _cdramo, edoPoliza, _nmpoliza, 
									_nmsuplem, cdIdeperRes);
							
							result.put("cdIdeper", cdIdeperRes);
							
						} else {
							//success = false;
							retryWS = true;
							logger.error("Error al crear Cliente en WS, no se pudo obtener el numero de Cliente, respondio: "+ cdIdeperRes);
							throw new ApplicationException("Error al crear Cliente en WS, no se pudo obtener el numero de Cliente");
						}
					} else {
						//success = false;
						retryWS = true;
						logger.error("Error al Crear el cliente en WS!, Datos Nulos");
						throw new ApplicationException("Error al crear Cliente en WS.");
					}
				}
			}
				
			////// ws de cotizacion y emision para autos
			paso ="Invocando WS de emisi�n";
			if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
				    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
				    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)) {
				
				EmisionAutosVO aux = emisionAutosService.cotizaEmiteAutomovilWS(cdunieco, cdramo,
							edoPoliza, nmpolizaEmitida, nmsuplemEmitida, ntramite,cdtipsit , us);
				
				boolean exitoWSEmision = aux != null && StringUtils.isNotBlank(aux.getNmpoliex()) && !"0".equals(aux.getNmpoliex());
				retryWS = !exitoWSEmision;
				
				if(exitoWSEmision) {
					
					logger.debug("Emision de Auto en WS Exitosa, Numero de Poliza: " + aux.getNmpoliex());
					nmpolAlt = aux.getNmpoliex();
					sucursalGS = aux.getSucursal();
					cdRamoGS = aux.getSubramo();
					
					result.put("nmpolAlt", nmpolAlt);
					result.put("sucursalGS", sucursalGS);
					result.put("nmpoliex", nmpolAlt);
					result.put("cdRamoGS", cdRamoGS);
					result.put("cdIdeper", cdIdeperRes);
					
					//Insertar Poliza Externa WS Auto
					try{
						emisionDAO.actualizaNmpoliexAutos(cdunieco, cdramo, edoPoliza, nmpolizaEmitida, 
								nmsuplemEmitida, nmpolAlt, sucursalGS, cdRamoGS);
						
					} catch(Exception e){
						logger.error("Error al Insertar Poliza Externa: " + e.getMessage(), e);
						throw new WSEmisionAutoException("Error al insertar Poliza Externa: " + nmpolAlt);
					}
					
					if(!aux.isExitoRecibos()){
						//retryRec = true;//Ya no Aplica
						throw new WSEmisionAutoException("Error al generar los recibos, intente de nuevo");
						//return SUCCESS;
					}
					
				} else {
					logger.error("Error en el Web Service de emisi\u00f3n. No se pudo emitir la p\u00f3liza");
					throw new WSEmisionAutoException("Error en el Web Service de emisi\u00f3n. No se pudo emitir la p\u00f3liza");
				}
			}
			
			//ws recibos
			if( cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_VITAL.getCdtipsit())
					||cdtipsit.equalsIgnoreCase(TipoSituacion.SALUD_NOMINA.getCdtipsit())
					||cdtipsit.equalsIgnoreCase(TipoSituacion.MULTISALUD.getCdtipsit())
					||cdtipsit.equalsIgnoreCase(TipoSituacion.RECUPERA_INDIVIDUAL.getCdtipsit())
					||cdtipsit.equalsIgnoreCase(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit()) ) {
				
				paso = "Invocando WS de recibos";
				if(StringUtils.isNotBlank(esDxN) && "S".equalsIgnoreCase(esDxN)) {
					// Ejecutamos el Web Service de Recibos:
					ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo, edoPoliza, _nmpoliza,
							_nmsuplem, rutaCarpeta, sucursal, nmpoliza, ntramite, false, tipoMov, us);
					// Ejecutamos el Web Service de Recibos DxN:
					recibosSigsService.generaRecibosDxN(_cdunieco, _cdramo, edoPoliza, _nmpoliza, 
							_nmsuplem, sucursal, nmpoliza, ntramite, us);
				} else {
					// Ejecutamos el Web Service de Recibos:
					ice2sigsService.ejecutaWSrecibos(_cdunieco, _cdramo, edoPoliza, _nmpoliza,
							_nmsuplem, rutaCarpeta, sucursal, nmpoliza,ntramite, true, tipoMov, us);
				}
			}
			
			
			////// crear carpeta para los documentos
			/*
			paso = "Creando carpeta de documentos";
            File carpeta = new File(rutaCarpeta);
            if(!carpeta.exists()) {
            	logger.debug("no existe la carpeta::: " + rutaCarpeta);
            	carpeta.mkdir();
            	if(carpeta.exists()) {
            		logger.debug("carpeta creada: " + rutaCarpeta);
            	} else {
            		logger.debug("carpeta NO creada: " + rutaCarpeta);
            		throw new ApplicationException("Error al crear la carpeta de documentos");
            	}
            } else {
            	logger.debug("existe la carpeta   ::: "+rutaCarpeta);
            }
            */
			
			////// documentos
            paso = "Generando la documentaci\u00F3n de emisi\u00F3n";
            documentosManager.generarDocumentosParametrizados(
            		cdunieco
            		,cdramo
            		,"M"
            		,nmpolizaEmitida
            		,"0" //nmsituac
            		,nmsuplemEmitida
            		,DocumentosManager.PROCESO_EMISION
            		,ntramite
            		,nmpoliza, null
            		);
            
            /*
            List<Map<String,String>> listaDocu = null;
			try {
				listaDocu = cotizacionDAO.impresionDocumentosPoliza(
						cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida, ntramite);
			} catch (Exception e) {
				logger.warn("Error en P_Imp_documentos", e);
			}
			
			//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
			if(listaDocu != null) {
				for(Map<String, String> docu : listaDocu) {
					
					logger.debug("docu iterado: " + docu);
					String descripc = docu.get("descripc");
					String descripl = docu.get("descripl");
					String url=rutaServidorReportes
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+passServidorReportes
							+ "&report="+descripl
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+cdunieco
							+ "&p_ramo="+cdramo
							+ "&p_estado='M'"
							+ "&p_poliza="+nmpolizaEmitida
							+ "&p_suplem="+nmsuplemEmitida
							+ "&desname="+rutaCarpeta+"/"+descripc;
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						url+="&p_cdperson="+descripc.substring(11, descripc.lastIndexOf("."));
					}
					logger.debug("\n#################################"
								+ "\n###### Se solicita reporte ######"
								+ "\na "+url);
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+descripc);
					logger.debug("\n######                    ######"
								+ "\n###### reporte solicitado ######"
								+ "\n################################"
								+ "");
				}
			}
			*/
			
			/**
			 * Para Guardar URls de Caratula Recibos y documentos de Autos Externas
			 */
			if( Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
		    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
		    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo) ){
				
				String parametros = null;
				
				String urlCaratula = null;
				if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
			    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)){
					urlCaratula = caratulaImpresionAutosURL;
				}else if(Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)){
					urlCaratula = caratulaImpresionAutosServicioPublicoURL;
				}
				
				if("C".equalsIgnoreCase(tipoGrupoInciso)){
					urlCaratula = caratulaImpresionAutosFlotillasURL;
				}
				
				String urlRecibo = reciboImpresionAutosURL;
				String urlCaic = caicImpresionAutosURL;
				String urlAeua = aeuaImpresionAutosURL;
				String urlAp = apImpresionAutosURL;
				
				String urlIncisosFlot = incisosFlotillasImpresionAutosURL;
				String urlIncisosExcelFlot = incisosFlotillasExcelImpresionAutosUrl;
				String urlTarjIdent = tarjetaIdenImpresionAutosURL;
				
				String urlDocsExtra = this.caratulaImpresionAutosDocExtra;
				
				String mensajeEmail = "<span style=\"font-family: Verdana, Geneva, sans-serif;\">"+
									"<br>Estimado(a) cliente,<br/><br/>"+
									"Anexamos a este e-mail la documentaci\u00f3n de la p\u00f3liza de Autom\u00f3viles contratada con GENERAL DE SEGUROS.<br/>"+
									"Para visualizar el documento favor de dar click en el link correspondiente.<br/>";
				
				/**
				 * Para Caratula
				 */
				parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0";
				logger.debug("URL Generada para Caratula: "+ urlCaratula + parametros);
				mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlCaratula + parametros+"\">Car\u00e1tula de p\u00f3liza</a>";
				
				mesaControlDAO.guardarDocumento(
						cdunieco
						,cdramo
						,"M"
						,nmpolizaEmitida
						,nmsuplemEmitida
						,new Date()
						,urlCaratula + parametros
						,"Car\u00e1tula de P\u00f3liza"
						,nmpoliza
						,ntramite
						,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
						,Constantes.SI
						,null
						,"1"
						,"0"
						,Documento.EXTERNO_CARATULA, null, null, false
						);
				
				
				/**
				 * Para Recibos
				 */
				String visible = null;
				HashMap<String,String> imprimir = new HashMap<String, String>(); 
				
				List<Map<String,String>> recibos = consultasPolizaDAO.obtieneRecibosPolizaAuto(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
				
				if(recibos!= null && !recibos.isEmpty()){
					for(Map<String,String> reciboIt : recibos){
						
						/**
						 * Si el Recibo Tiene estatus 1 se guarda en tdocupol como documento de la poliza, excepto algunos endosos como el de forma de pago,
						 * donde se generan recibos negativos para cancelar y esos no deben de guardarse, estos casos el estatus es distinto de 1
						 */
						if(!"1".equals(reciboIt.get("CDESTADO"))) continue;
						
						String llave = reciboIt.get("TIPEND") + reciboIt.get("NUMEND");
						
						if(!imprimir.containsKey(llave)){
							visible = Constantes.SI;
							imprimir.put(llave, reciboIt.get("NUMREC"));
						}else{
							visible = Constantes.NO;
						}
						
						//parametros = "?9999,0,"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",0,0,,"+reciboIt.get("NUMREC"); // PARAMS RECIBO ANTERIORES
						parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,"+reciboIt.get("NUMREC");
						
						logger.debug("URL Generada para Recibo "+reciboIt.get("NUMREC")+": "+ urlRecibo + parametros);
						
						if(Constantes.SI.equalsIgnoreCase(visible)){
							mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlRecibo + parametros+"\">Recibo provisional de primas</a>";
						}
						
						documentosManager.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlRecibo + parametros
								,"Recibo "+reciboIt.get("NUMREC")
								,nmpoliza
								,ntramite
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								,visible
								,null
								,TipoTramite.POLIZA_NUEVA.getCdtiptra()
								,"0"
								,Documento.RECIBO
								,null
								,null, false
								);
					}
				}
				
				
				boolean imprimirCaic = false;
				boolean imprimirAeua = false;
				boolean imprimirAP = false;
				
				List<Map<String,String>> listaEndosos = emisionAutosService.obtieneEndososImprimir(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
				
				if(listaEndosos!= null && !listaEndosos.isEmpty()){
					Map<String,String> emisionIt = listaEndosos.get(0);
					if(emisionIt != null && emisionIt.containsKey("CAIC") && Constantes.SI.equalsIgnoreCase(emisionIt.get("CAIC"))){
						imprimirCaic = true;
					}
					if(emisionIt != null && emisionIt.containsKey("AP") && Constantes.SI.equalsIgnoreCase(emisionIt.get("AP"))){
						imprimirAP = true;
					}
					if(emisionIt != null && emisionIt.containsKey("AEUA") && Constantes.SI.equalsIgnoreCase(emisionIt.get("AEUA"))){
						imprimirAeua = true;
					}
				}
				
				
				/**
				 * Para AP inciso 1
				 */
				if(imprimirAP){
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,0";
					logger.debug("URL Generada para AP Inciso 1: "+ urlAp + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlAp + parametros+"\">Anexo cobertura de AP</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlAp + parametros
							,"AP"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_AP, null, null, false
							);
				}
				
				
				/**
				 * Para CAIC inciso 1
				 */
				if(imprimirCaic){
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,0";
					logger.debug("URL Generada para CAIC Inciso 1: "+ urlCaic + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlCaic + parametros+"\">Anexo de cobertura RC USA</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlCaic + parametros
							,"CAIC"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_CAIC, null, null, false
							);
				}

				/**
				 * Para AEUA inciso 1
				 */
				if(imprimirAeua){
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,0";
					logger.debug("URL Generada para AEUA Inciso 1: "+ urlAeua + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlAeua + parametros+"\">Asistencia en Estados Unidos y Canad\u00E1</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlAeua + parametros
							,"Asistencia en Estados Unidos y Canad\u00E1"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_AEUA, null, null, false
							);
				}
				
				if("C".equalsIgnoreCase(tipoGrupoInciso)){
					/**
					 * Para Incisos Flotillas
					 */
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0";
					logger.debug("URL Generada para urlIncisosFlotillas: "+ urlIncisosFlot + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlIncisosFlot + parametros+"\">Relaci\u00f3n de Incisos Flotillas</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlIncisosFlot + parametros
							,"Incisos Flotillas"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_INCISOS_FLOTILLAS, null, null, false
							);

					/**
					 * Para Incisos Flotillas EXCEL
					 */
					if(StringUtils.isNotBlank(urlIncisosExcelFlot)){
						parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,0";
						logger.debug("URL Generada para urlIncisosExcelFlot: "+ urlIncisosExcelFlot + parametros);
						mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlIncisosExcelFlot + parametros+"\">Relaci\u00f3n de Incisos EXCEL</a>";
						
						mesaControlDAO.guardarDocumento(
								cdunieco
								,cdramo
								,"M"
								,nmpolizaEmitida
								,nmsuplemEmitida
								,new Date()
								,urlIncisosExcelFlot + parametros
								,"Incisos EXCEL"
								,nmpoliza
								,ntramite
								,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
								,Constantes.SI
								,null
								,"1"
								,"0"
								,Documento.EXTERNO_INCISOS_FLOTILLAS, null, null, false
								);
					}
					
					/**
					 * Para Tarjeta Identificacion
					 */
					
					int numeroIncisos = consultasPolizaDAO.obtieneNumeroDeIncisosPoliza(cdunieco, cdramo, "M", nmpolizaEmitida, nmsuplemEmitida);
					
					if(numeroIncisos > 0 ){
						int numeroReportes =  numeroIncisos/Integer.parseInt(numIncisosReporte);
						int reporteSobrante = numeroIncisos % Integer.parseInt(numIncisosReporte);
						
						logger.debug("Tarjeta de Identificacion ::: Numero de Reportes exactos: "+ numeroReportes);
						logger.debug("Tarjeta de Identificacion ::: Numero de incisos sobrantes: "+ reporteSobrante);
						
						if(reporteSobrante > 0 ){
							numeroReportes += 1;
						}
						
						/**
						 * Se divide reporte de tarjeta de identifiacion para flotillas ya que puede ser muy grande el archivo y se divide en una cantidad
						 * de autos por pagina predeterminada.
						 */
						for(int numReporte = 1; numReporte <= numeroReportes; numReporte++){
							
							int desdeInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte))+1;
							int hastaInciso = numReporte * Integer.parseInt(numIncisosReporte);
							
							if(numReporte == numeroReportes && reporteSobrante > 0 ){
								hastaInciso = ((numReporte-1) * Integer.parseInt(numIncisosReporte)) + reporteSobrante;
							}
							
							parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",,0,"+desdeInciso+","+hastaInciso;
							logger.debug("URL Generada para Tarjeta Identificacion: "+ urlTarjIdent + parametros);
							mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlTarjIdent + parametros+"\">Tarjeta de Identificaci\u00f3n. " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos+"</a>";
							
							mesaControlDAO.guardarDocumento(
									cdunieco
									,cdramo
									,"M"
									,nmpolizaEmitida
									,nmsuplemEmitida
									,new Date()
									,urlTarjIdent + parametros
									,"Tarjeta de Identificacion. " +desdeInciso+" - " + hastaInciso + " de "+ numeroIncisos
									,nmpoliza
									,ntramite
									,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
									,Constantes.SI
									,null
									,"1"
									,"0"
									,Documento.EXTERNO_TARJETA_IDENTIFICACION, null, null, false
									);
						}
						
					}
				}
				
				/**
				 * TODO: Datos Temporales, quitar cuando las caratulas de autos ya tengan la informacion completa
				 */
				
				PolizaAseguradoVO datosPol = new PolizaAseguradoVO();
				datosPol.setCdunieco(cdunieco);
				datosPol.setCdramo(cdramo);
				datosPol.setEstado("M");
				datosPol.setNmpoliza(nmpolizaEmitida);
				List<PolizaDTO> listaPolizas = consultasPolizaDAO.obtieneDatosPoliza(datosPol);
				PolizaDTO polRes = listaPolizas.get(0);
				
				boolean reduceGS = (StringUtils.isNotBlank(polRes.getReduceGS()) && Constantes.SI.equalsIgnoreCase(polRes.getReduceGS()))?true:false;
				boolean gestoria = (StringUtils.isNotBlank(polRes.getGestoria()) && Constantes.SI.equalsIgnoreCase(polRes.getGestoria()))?true:false;
				boolean cobVida  = (StringUtils.isNotBlank(polRes.getCobvida()) && Constantes.SI.equalsIgnoreCase(polRes.getCobvida()))?true:false;
				
				if(reduceGS) {
					/**
					 * Para cobertura de reduce GS
					 */
					
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlImpresionCobReduceGS+"\">Reduce GS</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlImpresionCobReduceGS
							,"Reduce GS"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_REDUCE_GS, null, null, false
							);
					
				}
				if(gestoria) {
					/**
					 * Para cobertura de gestoria GS
					 */
					
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlImpresionCobGestoriaGS+"\">Gestoria GS</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlImpresionCobGestoriaGS
							,"Gestoria GS"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_GESTORIA_GS, null, null, false
							);
					
				}
				
				if(cobVida){
					
					String reporteEspVida = rdfEspecSeguroVida;
					String pdfEspVidaNom = "SOL_VIDA_AUTO.pdf";
					
					String url = rutaServidorReportes
							+ "?destype=cache"
							+ "&desformat=PDF"
							+ "&userid="+passServidorReportes
							+ "&report="+reporteEspVida
							+ "&paramform=no"
							+ "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
							+ "&p_unieco="+cdunieco
							+ "&p_ramo="+cdramo
							+ "&p_estado='M'"
							+ "&p_poliza="+nmpolizaEmitida
							+ "&p_suplem="+nmsuplemEmitida
							+ "&desname="+rutaCarpeta+"/"+pdfEspVidaNom;
					
					HttpUtil.generaArchivo(url,rutaCarpeta+"/"+pdfEspVidaNom);
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,pdfEspVidaNom
							,"Especificaciones Seguro de Vida"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,"0"
							,Documento.EXTERNO_ESPECIF_SEGURO_VIDA, null, null, false
							);

					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlImpresionCondicionesSegVida
							,"Condiciones Generales Seguro de Vida"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,"0"
							,Documento.EXTERNO_CONDIC_GRALES_SEGURO_VIDA, null, null, false
							);
				}
				
				if(StringUtils.isNotBlank(urlDocsExtra)){
					/**
					 * Para documento Sanas Practicas
					 */
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",2";
					logger.debug("URL Generada para Sanas Practicas: "+ urlDocsExtra + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlDocsExtra + parametros+"\">Sanas Pr\u00e1cticas</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlDocsExtra + parametros
							,"Sanas Pr\u00e1cticas"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_DOCUMENTO_EXTRA, null, null, false
							);
					
					/**
					 * Para documento Constancia de Recepcion
					 */
					parametros = "?"+sucursalGS+","+cdRamoGS+","+nmpolAlt+",1";
					logger.debug("URL Generada para Constancia de Recepcion de Documentacion Contractual: "+ urlDocsExtra + parametros);
					mensajeEmail += "<br/><br/><a style=\"font-weight: bold\" href=\""+urlDocsExtra + parametros+"\">Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual</a>";
					
					mesaControlDAO.guardarDocumento(
							cdunieco
							,cdramo
							,"M"
							,nmpolizaEmitida
							,nmsuplemEmitida
							,new Date()
							,urlDocsExtra + parametros
							,"Constancia de Recepci\u00f3n de Documentaci\u00f3n Contractual"
							,nmpoliza
							,ntramite
							,String.valueOf(TipoEndoso.EMISION_POLIZA.getCdTipSup())
							,Constantes.SI
							,null
							,"1"
							,"0"
							,Documento.EXTERNO_DOCUMENTO_EXTRA, null, null, false
							);
				}
				
				// JTEZVA 2016 09 08 Se complementan las ligas con los documentos ice
				mensajeEmail += emisionManager.generarLigasDocumentosEmisionLocalesIce(ntramite);
				
				mensajeEmail += "<br/><br/><br/>Agradecemos su preferencia.<br/>"+
									 "General de Seguros<br/>"+
									 "</span>";
				
				flujoMesaControlDAO.guardarMensajeCorreoEmision(
						ntramite,
						Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail)
				);
				
				mensajeEmail = Utils.cambiaGuionesBajosPorAcentosHtml(Utils.cambiaAcentosUnicodePorGuionesBajos(mensajeEmail));
				
			}
			
			////// detalle emision
			paso = "Insertando detalle de emisi�n";
			logger.debug("se inserta detalle nuevo para emision");
	        	
			RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
                    cdusuari,
                    cdsisrol,
                    ntramite,
                    EstatusTramite.CONFIRMADO.getCodigo(),
                    "El tr\u00e1mite se emiti\u00f3",
                    null,  // cdrazrecha
                    null,  // cdusuariDes
                    null,  // cdsisrolDes
                    true,  // permiso agente
                    false, // porEscalamiento
                    fechaHoy,
                    false  // sinGrabarDetalle
                    );
			
		} catch(Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		
		return result;
	}
	
}