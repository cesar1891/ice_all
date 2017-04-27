package mx.com.gseguros.portal.renovacion.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.SlistSmapVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.renovacion.dao.RenovacionDAO;
import mx.com.gseguros.portal.renovacion.service.RenovacionManager;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.recibossigs.service.RecibosSigsService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class RenovacionManagerImpl implements RenovacionManager
{
	private static final Logger           logger       = Logger.getLogger(RenovacionManagerImpl.class);
	private static final SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy"); 
	
	//Dependencias inyectadas
	private RenovacionDAO renovacionDAO;
	private PantallasDAO  pantallasDAO;
	private CotizacionDAO cotizacionDAO;	
	
	private transient Ice2sigsService ice2sigsService;
	private transient RecibosSigsService recibosSigsService;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private KernelManagerSustituto kernelManager;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private StoredProceduresManager storedProceduresManager;
	
	@Autowired
	private EndososDAO endososDAO;
	
	@Autowired
	private ConsultasDAO  consultasDAO;
	
	@Autowired
	private ConsultasManager consultasManager;
	
	@Override
	public ManagerRespuestaImapVO pantallaRenovacion(String cdsisrol)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ pantallaRenovacion @@@@@@")
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.toString());
		ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
		
		//obtener componentes
		try
		{
			List<ComponenteVO>componentesBusqueda=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION","BUSQUEDA",null);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(componentesBusqueda, true, false, true, false, false, false);
			
			Map<String,Item> imap = new HashMap<String,Item>();
			resp.setImap(imap);
			
			imap.put("busquedaItems" , gc.getItems());
			
			List<ComponenteVO>componentesGrid=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION","GRID",null);
			
			gc.generaComponentes(componentesGrid, true, true, false, true, true, false);
			
			imap.put("gridFields"  , gc.getFields());
			imap.put("gridColumns" , gc.getColumns());
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al obtener componentes de busqueda #")
					.append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ pantallaRenovacion @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString());
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO buscarPolizasRenovables(String cdunieco,String cdramo,String anio,String mes)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ buscarPolizasRenovables @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ anio=")    .append(anio)
				.append("\n@@@@@@ mes=")     .append(mes)
				.toString()
				);
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		//obtener polizas
		try
		{
			List<Map<String,String>>listaPolizasRenovables = renovacionDAO.buscarPolizasRenovables(cdunieco,cdramo,anio,mes);
			resp.setSlist(listaPolizasRenovables);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al buscar las polizas renovables #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ buscarPolizasRenovables @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO renovarPolizas(
			List<Map<String,String>>polizas
			,String cdusuari
			,String anio
			,String mes
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ renovarPolizas @@@@@@")
				.append("\n@@@@@@ cdusuari=").append(cdusuari)
				.append("\n@@@@@@ anio=").append(anio)
				.append("\n@@@@@@ mes=").append(mes)
				.append("\n@@@@@@ polizas=").append(polizas)
				.append("\n@@@@@@ rutaDocumentosPoliza=").append(rutaDocumentosPoliza)
				.append("\n@@@@@@ rutaServidorReports=").append(rutaServidorReports)
				.append("\n@@@@@@ passServidorReports=").append(passServidorReports)
				.toString()
				);
		
		ManagerRespuestaVoidVO resp=new ManagerRespuestaVoidVO(true);
		
		//marcar polizas
		for(Map<String,String>poliza:polizas){
			try
				{
					
						renovacionDAO.marcarPoliza(
								poliza.get("anio")
								,poliza.get("mes")
								,poliza.get("cdtipopc")
								,"1"//cdtipacc
								,poliza.get("cdunieco")
								,poliza.get("cdramo")
								,poliza.get("nmpoliza")
								,new Date()//feemisio
								,"S"//swrenova
								,"S"//swaproba
								,"1"//nmsituac
								,poliza.get("cducreno")
								);
					
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al marcar polizas #").append(timestamp).append(". Detalle: ").append(ex.getMessage()).append(" para la poliza ").append(poliza.get("nmpoliza")).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
		}
		
		List<Map<String,String>>polizasRenovadas=null;
		
		//ejecutar renovacion
		if(resp.isExito())
		{
			try
			{
			    polizasRenovadas = renovacionDAO.renovarPolizas(
			    		cdusuari
			    		,anio
			    		,mes
			    		,"2"//cdtipopc
			    		,usuario.getRolActivo().getClave()
			    		);
			}
			catch(ApplicationException ax)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
				resp.setRespuestaOculta(ax.getMessage());
				logger.error(resp.getRespuesta(),ax);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(
						new StringBuilder()
						.append("Error al renovar polizas #")
						.append(timestamp)
						.toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		StringBuilder respBuilder = new StringBuilder("P\u00f3lizas renovadas:");
		
		//crear documentacion
		if(resp.isExito())
		{
			try
			{
				for(Map<String,String>iPoliza:polizasRenovadas)
				{
					String esDxN     = iPoliza.get("SWDXN");
					String cdunieco  = iPoliza.get("cdunieco");
					String cdramo    = iPoliza.get("cdramo");
					String estado    = iPoliza.get("estado");
					String nmpoliza  = iPoliza.get("nmpoliza");
					String nmsuplem  = iPoliza.get("nmsuplem");
					String ntramite  = iPoliza.get("ntramite");
					String cdtipopc  = iPoliza.get("cdtipopc");
					String nmpolant  = iPoliza.get("nmpolant");
					String uniecoant = iPoliza.get("uniecoant");
					
					try
					{
						if(StringUtils.isNotBlank(esDxN) && "S".equalsIgnoreCase(esDxN))
						{	
							// Ejecutamos el Web Service de Recibos:
							ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo,
									estado, nmpoliza, 
									nmsuplem, rutaDocumentosPoliza,
									cdunieco, nmpoliza, ntramite, 
									false, "33", //tipMov 33 para Renovacion
									usuario);
							// Ejecutamos el Web Service de Recibos DxN:
							recibosSigsService.generaRecibosDxN(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdunieco, nmpoliza, ntramite, usuario);
						}
						else
						{
							// Ejecutamos el Web Service de Recibos:
							ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo,
									estado, nmpoliza, 
									nmsuplem, rutaDocumentosPoliza,
									cdunieco, nmpoliza,ntramite, 
									true, "33",//tipMov 33 para Renovacion
									usuario);
						}
					}
					catch(Exception ex)
					{
						logger.error("Error al lanzar ws recibos en Renovacion",ex);
					}
					

					logger.debug(new StringBuilder("\n@@@@@@ Documentacion para poliza=").append(iPoliza));
					
					documentosManager.generarDocumentosParametrizados(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,"0" //nmsituac
							,nmsuplem
							,DocumentosManager.PROCESO_EMISION
							,ntramite
							,null //nmsolici
							,null
							);
					
					/*
					List<Map<String,String>>iPolizaDocs =
							cotizacionDAO.impresionDocumentosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite);
					
					String iPolizaRutaCarpeta = new StringBuilder(rutaDocumentosPoliza).append("/").append(ntramite).toString();
					File   iPolizaCarpeta     = new File(iPolizaRutaCarpeta);
					
					if(!iPolizaCarpeta.exists())
					{
						if(!iPolizaCarpeta.mkdir())
						{
							throw new ApplicationException("Sin permisos para crear carpeta de documentos");
						}
					}
					
					for(Map<String,String>iDoc:iPolizaDocs)
					{
						String descripc          = iDoc.get("descripc");
						String descripl          = iDoc.get("descripl");
						StringBuilder urlBuilder = new StringBuilder()
						        .append(rutaServidorReports)
						        .append("?destype=cache")
								.append("&desformat=PDF")
								.append("&paramform=no")
								.append("&ACCESSIBLE=YES")
								.append("&userid=")  .append(passServidorReports)
								.append("&report=")  .append(descripl)
								.append("&p_unieco=").append(cdunieco)
								.append("&p_ramo=")  .append(cdramo)
								.append("&p_estado=").append(estado)
								.append("&p_poliza=").append(nmpoliza)
								.append("&p_suplem=").append(nmsuplem)
								.append("&desname=") .append(iPolizaRutaCarpeta).append("/").append(descripc);
						if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN"))
						{
							// C R E D E N C I A L _ X X X X X X . P D F
							//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
							
							urlBuilder.append("&p_cdperson=").append(descripc.substring(11, descripc.lastIndexOf(".")));
						}
						String url = urlBuilder.toString();
						HttpUtil.generaArchivo(url,iPolizaRutaCarpeta+"/"+descripc);
					}
					*/
					
					//copiar documentos de usuario
					List<Map<String,String>>iPolizaUserDocs=renovacionDAO.cargarDocumentosSubidosPorUsuario(uniecoant,cdramo,estado,nmpolant);
					for(Map<String,String>iUserDoc:iPolizaUserDocs)
					{
						String ntramiteAnt = iUserDoc.get("ntramite");
						String cddocume    = iUserDoc.get("cddocume");
						
						File doc = new File(
								new StringBuilder(rutaDocumentosPoliza)
								.append("/")
								.append(ntramiteAnt)
								.append("/")
								.append(cddocume)
								.toString()
								);
						
						File newDoc = new File(
								new StringBuilder(rutaDocumentosPoliza)
								.append("/")
								.append(ntramite)
								.append("/")
								.append(cddocume)
								.toString()
								);
						
						if(doc!=null&&doc.exists())
						{
							try
							{
								FileUtils.copyFile(doc, newDoc);
							}
							catch(Exception ex)
							{
								logger.error("Error copiando archivo de usuario",ex);
							}
						}
						else
						{
							logger.error(new StringBuilder("No existe el documento").append(doc).toString());
						}
					}
					
					renovacionDAO.actualizaRenovacionDocumentos(anio,mes,cdtipopc,uniecoant,cdramo,nmpolant);
					
					respBuilder
					    .append("<br/>Antes ").append(uniecoant).append(" - ").append(nmpolant)
					    .append(", ahora ")   .append(cdunieco) .append(" - ").append(nmpoliza)
					    .append(" con tr\u00e1mite ").append(ntramite);
				}
			}
			catch(ApplicationException ax)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
				resp.setRespuestaOculta(ax.getMessage());
				logger.error(resp.getRespuesta(),ax);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(
						new StringBuilder()
						.append("Error al generar documentos de polizas #")
						.append(timestamp)
						.toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		if(resp.isExito())
		{
			resp.setRespuesta(respBuilder.toString());
			resp.setRespuestaOculta("Todo OK");
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ renovarPolizas @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaImapVO pantallaRenovacionIndividual(String cdsisrol) throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ pantallaRenovacionIndividual @@@@@@")
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.toString());
		ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
		String paso = "";
		//obtener componentes
		try
		{
			paso = "antes de obtener componentes busqueda";
			List<ComponenteVO>componentesBusqueda=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","BUSQUEDA",null);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(componentesBusqueda, true, false, true, false, false, false);
			
			Map<String,Item> imap = new HashMap<String,Item>();
			resp.setImap(imap);			
			imap.put("busquedaItems" , gc.getItems());
			paso = "antes de obtener componentes modelo polizas";
			List<ComponenteVO> componentesGrid=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","MODELO_POLIZAS_RENOVAR",null);			
			gc.generaComponentes(componentesGrid, true, true, false, true, true, false);			
			imap.put("gridFields"  , gc.getFields());
			imap.put("gridColumns" , gc.getColumns());
			paso = "antes de obtener modelo contratante";
			List<ComponenteVO> itemsFormularioContratante=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","MODELO_CONTRATANTE",null);			
			gc.generaComponentes(itemsFormularioContratante, true, true, true, false, false, false);		
			imap.put("itemsFormularioContratante"  , gc.getItems());
			imap.put("fieldsFormularioContratante"  , gc.getFields());
			paso = "antes de obtener modelo polizas";
			List<ComponenteVO> itemsFormularioPoliza = pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","MODELO_POLIZAS",null);			
			gc.generaComponentes(itemsFormularioPoliza, true, true, false, true, true, false);			
			imap.put("itemsFormularioPolizaFields"  , gc.getFields());
			imap.put("itemsFormularioPolizaColumns" , gc.getColumns());
			paso = "antes de obtener componentes editar forma pago";
			List<ComponenteVO> componentesEditarPago=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","EDITAR_FORMA_PAGO",null);			
			gc.generaComponentes(componentesEditarPago, true, false, true, false, false, false);
			imap.put("itemsEditarPago" , gc.getItems());
			paso = "antes de obtener componentes domicilio";
			List<ComponenteVO> componentesEditarDomicilio = pantallasDAO.obtenerComponentes(
					null,null,"4",null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","EDITAR_DOMICILIO",null);				
			gc.generaComponentes(componentesEditarDomicilio, true, false, true, false, false, false);
			imap.put("itemsEditarDomicilio" , gc.getItems());
			
			//TODO
			//Quitar esta parte para obtenerla cuando se dispare la renovacion
			List<ComponenteVO> listaTatrisit = kernelManager.obtenerTatripol(new String[]{"2","SL","I"});
			gc.generaComponentes(listaTatrisit, true, false, true, false, false, false);
			imap.put("itemsTatrisit", gc.getItems());
			
			paso = "antes de obtener componentes calendario";
			List<ComponenteVO> componentesCalendarioRenovacion = pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","PROGRAMADO_CALENDARIO",null);				
			gc.generaComponentes(componentesCalendarioRenovacion, true, false, true, false, false, false);
			imap.put("itemsCalendarioRenovacion" , gc.getItems());
			
			paso = "antes de obtener componentes calendario grid";
			List<ComponenteVO> itemsCalendario = pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","PROGRAMADO_CALENDARIO_GRID",null);			
			gc.generaComponentes(itemsCalendario, true, true, false, true, true, false);
			imap.put("itemsCalendarioFields"  , gc.getFields());
			imap.put("itemsCalendarioColumns" , gc.getColumns());
			
			paso = "antes de obtener componentes exclusiones";
			List<ComponenteVO> componentesCondicionesRenovacion = pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","PROGRAMADO_EXCLUSIONES",null);				
			gc.generaComponentes(componentesCondicionesRenovacion, true, false, true, false, false, false);
			imap.put("itemsCondicionesRenovacion" , gc.getItems());
			
			paso = "antes de obtener componentes exclusiones grid";
			List<ComponenteVO> itemsCondiciones = pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RENOVACION_INDIVIDUAL","PROGRAMADO_EXCLUSIONES_GRID",null);			
			gc.generaComponentes(itemsCondiciones, true, true, false, true, true, false);			
			imap.put("itemsCondicionesFields"  , gc.getFields());
			imap.put("itemsCondicionesColumns" , gc.getColumns());
				
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ pantallaRenovacionIndividual @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString());
		return resp;
	}

	@Override
	public ManagerRespuestaSlistVO buscarPolizasRenovacionIndividual(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdusuari,
			String cdsisrol
			) throws Exception{
		logger.debug(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ buscarPolizasRenovacionIndividual @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=").append(cdramo)
				.append("\n@@@@@@ estado=").append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ cdusuari=").append(cdusuari)
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.toString()
				);
		String paso = "";
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		String cdagente = null;
		try{
		    if(null != cdusuari){
		        paso = "obteniendo agente "+cdusuari;
		        cdagente = consultasDAO.recuperaAgentePoliza(cdunieco, cdramo, estado, nmpoliza, cdusuari);
		    }
			paso = "antes de obtener componentes de poliza";
			List<Map<String,String>> listaPolizasRenovables = renovacionDAO.busquedaRenovacionIndividual(cdunieco, cdramo, estado, nmpoliza, cdagente);
			paso = "despues de obtener componentes de poliza";
			resp.setSlist(listaPolizasRenovables);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ buscarPolizasRenovacionIndividual @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString());
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO buscarPolizasRenovacionIndividualMasiva(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdtipsit,
			String fecini,
			String fecfin,
			String status,
			String cdperson,
			String retenedora,
			String cdusuari,
			String cdsisrol
			) throws Exception{
		String paso = "";
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		try{
			paso = "antes de obtener polizas masivas";
//			fecini = Utils.formateaFechaConHora(fecini);
//			fecfin = Utils.formateaFechaConHora(fecfin);
			fecini = Utils.formateaFecha(fecini);
			fecfin = Utils.formateaFecha(fecfin);
			List<Map<String,String>> listaPolizasRenovables = renovacionDAO.busquedaRenovacionIndividualMasiva(
					cdunieco, 
					cdramo, 
					estado, 
					nmpoliza, 
					cdtipsit, 
					fecini, 
					fecfin, 
					status,
					cdperson,
					retenedora,
					cdusuari);
			paso = "despues de obtener polizas masivas";
			resp.setSlist(listaPolizasRenovables);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO renuevaPolizaIndividual(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String usuario,
			String feefecto,
			String feproren,
			String estadoNew,
			String cdmoneda) throws Exception
	{
		//obtener componentes
		ManagerRespuestaSlistVO lista = new ManagerRespuestaSlistVO();
		String paso  = "";
		String ntramite = ""; 
		try
		{	
			paso     = "enviando para renovar poliza";			
//			generaTcartera(cdunieco, cdramo, nmpoliza, feefecto, feproren, nmsuplem, cdagente, cdperpag, cdcontra, cdmoneda);
			Map<String,String> map = renovacionDAO.renuevaPolizaIndividual(cdunieco, cdramo, estado, nmpoliza, estadoNew, usuario);
			//Map<String,String> flujomc = consultasDAO.recuperarDatosFlujoRenovacion(cdramo, "I");
			paso = "enviando a recuperar datos flujo emision";
			Map<String,String> flujomc = consultasDAO.recuperarDatosFlujoEmision(cdramo, "I");
			logger.info(
	                new StringBuilder()
	                .append("\n###### flujomc=").append(flujomc));
			paso = "enviando a actualizar flujo tramite";
			consultasManager.actualizaFlujoTramite(map.get("ntramite"), flujomc.get("cdflujomc"), flujomc.get("cdtipflu"));
//			logger.info(new StringBuilder().append("\n@@@@@@ flujomc=").append(flujomc).toString());
			paso = "enviando a obtener poliza cdperson tramite";
			List<Map<String, String>> slist = renovacionDAO.obtenerPolizaCdpersonTramite(map.get("ntramite"));
			paso = "obteniendo cdtipsit inciso1";
			String cdtipsit = endososDAO.recuperarCdtipsitInciso1(cdunieco, cdramo, estado, nmpoliza);
			slist.get(0).put("cdtipsit",  cdtipsit);
			slist.get(0).put("ntramite",  map.get("ntramite"));
			slist.get(0).put("cdtipflu",  flujomc.get("cdtipflu"));
			slist.get(0).put("cdflujomc", flujomc.get("cdflujomc"));
			slist.get(0).put("estadomc",  map.get("status"));
			slist.get(0).put("nmsuplem",  map.get("nmsuplem"));
			paso = "asignando valores de cdtipsit";
			cdunieco = slist.get(0).get("cdunieco");
			cdramo 	 = slist.get(0).get("cdramo");
			estado 	 = slist.get(0).get("estado");
			nmpoliza = slist.get(0).get("nmpoliza");
			/**
			 * Obtiene atributos tvalopol
			 */
			Map<String,String> paramGetValopol = new HashMap<String,String>(0);
			paramGetValopol.put("pv_cdunieco",cdunieco);
			paramGetValopol.put("pv_cdramo"  ,cdramo);
			paramGetValopol.put("pv_estado"  ,estado);
			paramGetValopol.put("pv_nmpoliza",nmpoliza);
			paso = "antes de obtener atributos variables poliza";
			Map<String,Object> parametrosCargados = kernelManager.pGetTvalopol(paramGetValopol);
			if(parametrosCargados.isEmpty()){
				throw new Exception("No se obtuvo ningun atributo variable a nivel de poliza");
			}
			Iterator it = parametrosCargados.entrySet().iterator();			
			while(it.hasNext()){
				Entry<String,Object> entry = (Map.Entry<String, Object>) it.next();
				if(entry.getKey().startsWith("otvalor")){
					slist.get(0).put("parametros.pv_"+entry.getKey(), (String)entry.getValue());
				}else{
					slist.get(0).put(entry.getKey(), (String)entry.getValue());
				}
			}
			
			if(slist.size() > 0){
				lista.setSlist(slist);
				lista.setExito(true);
			}
			
			/**
			 * Obtiene atributos datos generales
			 */
			Map<String,Object> params = new HashMap<String,Object>(0);
			params.put("pv_cdusuari", usuario);
			params.put("pv_cdunieco", cdunieco);
			params.put("pv_cdramo"  , cdramo);
			params.put("pv_estado"  , estado);
			params.put("pv_nmpoliza", nmpoliza);
			logger.info(new StringBuilder().append("\n@@@@@@ params=").append(params).toString());
			paso = "antes de obtener informacion de poliza";
			Map<String, Object> select = kernelManager.getInfoMpolizas(params);
			Iterator ite = select.entrySet().iterator();			
			while(ite.hasNext()){
				Entry<String,Object> entry = (Map.Entry<String, Object>) ite.next();
				if(entry.getKey().startsWith("fe")){
					String fecha = Utils.format((Date)entry.getValue());
					entry.setValue(fecha);
				}
				slist.get(0).put(entry.getKey(), (String)entry.getValue());
			}
			paso = "antes de cargar bandera cambio cuadro";
			slist.get(0).put("cambioCuadro", cotizacionManager.cargarBanderaCambioCuadroPorProducto(cdramo)?"S":"N");
			LinkedHashMap<String,Object> paramsRetroactividad = new LinkedHashMap<String,Object>();
			paramsRetroactividad.put("param1" , cdunieco);
			paramsRetroactividad.put("param2" , cdramo);
			paramsRetroactividad.put("param3" , TipoEndoso.EMISION_POLIZA.getCdTipSup()+"");
			paramsRetroactividad.put("param4" , usuario);
			paramsRetroactividad.put("param5" , slist.get(0).get("cdtipsit"));
			logger.info(new StringBuilder().append("\n@@@@@@ paramsRetroactividad=").append(paramsRetroactividad).toString());
			paso = "antes de obtener retroactividad ";
			Map<String,String> retroactividad = storedProceduresManager.procedureMapCall(ObjetoBD.OBTIENE_RETROACTIVIDAD_TIPSUP.getNombre(), paramsRetroactividad, null);
			
			int retroac = Integer.valueOf(retroactividad.get("RETROAC"));
			int diferi  = Integer.valueOf(retroactividad.get("DIFERI"));
			
			Calendar calendarMin = Calendar.getInstance();
			Calendar calendarMax = Calendar.getInstance();
			
			calendarMin.add(Calendar.DAY_OF_YEAR, retroac * -1);
			calendarMax.add(Calendar.DAY_OF_YEAR, diferi);
			
			slist.get(0).put("fechaMinEmi" , renderFechas.format(calendarMin.getTime()));
			slist.get(0).put("fechaMaxEmi" , renderFechas.format(calendarMax.getTime()));
			
			if(slist.size() > 0){
				lista.setSlist(slist);
				lista.setExito(true);
			}
			logger.info(
					new StringBuilder()
					.append("\n@@@@@@ slitst=").append(lista.getSlist())
					.toString());
		}
		catch(Exception ex){
			logger.info(
					new StringBuilder()
					.append("\n@@@@@@ paso=").append(paso)
					.toString());
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return lista;
	}
	
	@Override
	public void actualizaValoresCotizacion(Map<String, String> valores, String cdelemen, String cdusuari, String cdtipsup) throws Exception{
		String paso = "";
		try{
			paso     = "actualizando informacion de cotizacion";
			String cdunieco 	= valores.get("cdunieco");
			String cdramo 		= valores.get("cdramo");
			String estado 		= valores.get("estado");
			String nmpoliza 	= valores.get("nmpoliza");
			String nmsuplem 	= valores.get("nmsuplem");
			String feefecto 	= Utils.formateaFecha(valores.get("feefecto"));
			String feefecto_ant = Utils.formateaFecha(valores.get("feefecto_ant"));
			String cdagente 	= valores.get("cdagente");
			String cdperpag 	= valores.get("cdperpag");
			String cdcontra 	= valores.get("cdcontra");
			String cdmoneda		= valores.get("cdmoneda");			
			Map<String,String> atributos = new HashMap<String,String>();
			Map<String,String> campos	 = new HashMap<String,String>();
			paso = "antes de armar valores";
			for(Entry<String,String> atrib : valores.entrySet())
			{
				String key = atrib.getKey();
				if(StringUtils.isNotBlank(key) && key.length() >= "pv_otvalor".length()){
					if(key.substring(0, "pv_otvalor".length()).equals("pv_otvalor")){
						atributos.put(key.substring("pv_".length(), key.length()), atrib.getValue());
					}
				}
				if(!key.startsWith("pv_otvalor")){
					campos.put(key, atrib.getValue());
				}
			}
			paso = "antes de actualizar tvalopol";
			cotizacionDAO.movimientoTvalopol(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplem,
					"V", //status
					atributos
					);
			paso = "antes de actualizar contratante";
			renovacionDAO.actualizaContratanteFormaPago(cdunieco, cdramo, estado, nmpoliza, cdperpag, cdcontra);
			paso = "antes de lanzar tarifa";
			endososDAO.sigsvalipolEnd(
					cdusuari,
					cdelemen,
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					"0",
					nmsuplem,
					cdtipsup
					);
		}catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
	}
	
	@Override
	public Map<String, String> confirmarCotizacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String ntramite,
			String cdperpag,
			String feefecto,
			UserVO usuario,
			String rutaDocumentosPoliza) throws Exception{
		String paso = "";
		String esDxN = "";
		Map<String, String> smap1 = new HashMap<String, String>();
		try{
			paso     = "confirmando cotizacion";
			List<Map<String,String>> polizas = renovacionDAO.confirmarPolizaIndividual(cdunieco, cdramo, estado, nmpoliza, nmsuplem, ntramite);
			paso    = "termino de confirmar";
			esDxN = polizas.get(0).get("SWDXN");
			paso    = "obtiene DXN";
			smap1 = renovacionDAO.confirmarTramite(cdunieco, cdramo, estado, nmpoliza, cdperpag, feefecto);
			logger.info(new StringBuilder().append("\n@@@@@@ smap1=").append(smap1).toString());
			String nmpolizaNew = smap1.get("nmpolizaNew");
			String nmsuplemNew = smap1.get("nmsuplemNew");
			if(StringUtils.isNotBlank(esDxN) && "S".equalsIgnoreCase(esDxN)){
				// Ejecutamos el Web Service de Recibos:
				paso    = "generando recibos DXN";
				ice2sigsService.ejecutaWSrecibos(
						cdunieco, 
						cdramo,
						"M",//estado, 
						nmpolizaNew, 
						nmsuplemNew, 
						rutaDocumentosPoliza,
						cdunieco, 
						nmpoliza, 
						ntramite, 
						false, 
						"33", //tipMov 33 para Renovacion
						usuario);
				// Ejecutamos el Web Service de Recibos DxN:
				paso    = "generando recibos DXN";
				recibosSigsService.generaRecibosDxN(
						cdunieco, 
						cdramo, 
						"M",//estado, 
						nmpolizaNew, 
						nmsuplemNew, 
						cdunieco, 
						nmpoliza, 
						ntramite, 
						usuario);
			}
			else
			{
				// Ejecutamos el Web Service de Recibos:
				paso    = "generando recibos diferente DXN";
				ice2sigsService.ejecutaWSrecibos(
						cdunieco, 
						cdramo,
						"M",//estado, 
						nmpolizaNew, 
						nmsuplemNew, 
						rutaDocumentosPoliza,
						cdunieco, 
						nmpoliza,
						ntramite, 
						true, 
						"33",//tipMov 33 para Renovacion
						usuario);
			}
//			paso     = "antes de generar documentos";	
			/**
			 * Se comenta porque la generacion la hace desde el procedure
			 */
//			documentosManager.generarDocumentosParametrizados(
//					cdunieco,
//					cdramo,
//					"M",//estado,
//					nmpolizaNew,
//					"0", //nmsituac
//					nmsuplemNew,
//					DocumentosManager.PROCESO_EMISION,
//					ntramite,
//					null, //nmsolici
//					null
//					);
//			paso  = "antes de generar documentos";
//			smap1 = renovacionDAO.confirmarTramite(cdunieco, cdramo, estado, nmpoliza, cdperpag, feefecto);
//			logger.info(new StringBuilder().append("\n@@@@@@ smap1=").append(smap1).toString());
		}catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return smap1;
	}
	
	public void generaTcartera(
			String cdunieco,
			String cdramo,
			String nmpoliza,
			String feefecto,
			String feefecto_ant,
			String nmsuplem,
			String cdagente,
			String cdperpag,
			String cdcontra,
			String cdmoneda) throws Exception{		
		String paso = "";
		try{
			paso     = "antes de generar tcartera";
			renovacionDAO.generaTcartera(cdunieco, cdramo, nmpoliza, feefecto, feefecto_ant, nmsuplem, cdagente, cdperpag, cdcontra, cdmoneda);
			paso     = "despues de generar tcartera";
		}catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
	}
	
	@Override
	public String obtenerItemsTatripol(
			String cdramo,
			String cdtipsit) throws Exception{
		String respuesta = "";
		String paso = "";
		try{
			paso = "antes de obtener tatripol";
			List<ComponenteVO> listaTatrisit = kernelManager.obtenerTatripol(new String[]{cdramo,cdtipsit,"I"});
			paso = "despues de obtener tatripol";
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			Map<String,Item> imap = new HashMap<String,Item>();
			gc.setCdramo(cdramo);
			gc.setCdtipsit(cdtipsit);
			gc.generaComponentes(listaTatrisit, true, false, true, false, false, false);
			respuesta = "["+gc.getItems()+"]";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return respuesta;
	}
	
	@Override
	public void renovarPolizasMasivasIndividuales(
			List<Map<String,String>> slist) throws Exception{
		String paso = "";
		try{
			paso = "antes de renovar polizas masivas";
			renovacionDAO.renovarPolizasMasivasIndividuales(slist);
			paso = "despues de renovar polizas masivas";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
	}
		
	@Override
	public List<Map<String, String>> obtenerCondicionesRenovacionIndividual(
			String nmperiod,
			String cdunieco,
			String cdramo,
			String anio,
			String mes) throws Exception{
		String paso = "";
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		try{
			paso = "Antes de obtener condiciones de renovacion programada";
			result = renovacionDAO.obtenerCondicionesRenovacionprogramada(nmperiod, cdunieco, cdramo, anio, mes);
			paso = "Despues de obtener conciones de renovacion programada";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return result;
	}
	
	@Override
	public void movimientoCondicionesRenovacionIndividual(
			String nmperiod,
			String cdunieco,
			String cdramo,
			String anio,
			String mes,
			String criterio,
			String campo,
			String valor,
			String valor2,
			String operacion) throws Exception{
		String paso = "";
		try{
			paso = "Antes de entrar a mov condiciones de renovacion programada";
			renovacionDAO.movimientoCondicionesRenovacionProgramada(nmperiod, cdunieco, cdramo, anio, mes, criterio, campo, valor, valor2, operacion);
			paso = "Despues de mov condiciones de renovacion programada";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
	}
	
	@Override
	public List<Map<String, String>> obtenerCalendarizacionRenovacionIndividual(
			String anio,
			String mes) throws Exception{
		String paso = "";
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		try{
			paso = "Antes de obtener calendarizacion de renovacion programada";
			result = renovacionDAO.obtenerCalendarizacionProgramada(anio, mes);
			paso = "Despues de obtener calendarizacion de renovacion programada";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		return result;
	}
	
	@Override
	public void movimientoCalendarizacionRenovacionIndividual(
			String nmperiod,
			String anio,
			String mes,
			String cdunieco,
			String cdramo,
			String feinicio,
			String fefinal,
			String feaplica,
			String operacion) throws Exception{
		String paso = "";
		try{
			paso = "Antes de entrar a mov calendarizacion de renovacion programada";
//			Date fecha = renderFechas.parse(feinicio);
			feinicio = Utils.formateaFecha(feinicio);
			fefinal  = Utils.formateaFecha(fefinal);
			feaplica = Utils.formateaFecha(feaplica);
			renovacionDAO.movimientoCalendarizacionProgramada(nmperiod, anio, mes, cdunieco, cdramo, feinicio, fefinal, feaplica, operacion);
			paso = "Despues de mov calendarizacion de renovacion programada";
		}
		catch(Exception ex){
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
	}
	
	@Override
	public String validaValorExclusion(String criterio, String valor) throws Exception{
	    String paso = "";
	    String result = "";
	    try{
	        paso = "Validando valor de exclusion";
	        result = renovacionDAO.validaValorExclusion(criterio, valor);
	    }
	    catch(Exception ex){
	        Utils.generaExcepcion(ex, paso);
	    }
	    return result;
	}
	
	@Override
	public String renovacionColectivo(String cdusuari,String cdunieco,String cdramo,String nmpoliza,String fecdesde,String fechasta,String procedimiento)throws Exception{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ renovacionColectivo @@@@@@")
				.toString());
		String paso = "";
		String result = "";
		    try{
		        paso = "Validando valor de exclusion";
		        if(procedimiento.equals("COLECTIVO")){
					paso = "VALIDA_RENOVACION_COLECTIVO";
					logger.debug(paso);
					
					result = renovacionDAO.validaRenovacionColectivo(cdusuari, cdunieco, cdramo, nmpoliza);
					
				}
                if(procedimiento.equals("RENOVAR_X_FECHAS")){
					paso = "RENOVAR_X_FECHAS_COLECTIVOS";
					logger.debug(paso);
					logger.debug("Entro a paso 1");
					result = renovacionDAO.renovaXFechasColectivo(cdusuari, Utils.formateaFecha(fecdesde), Utils.formateaFecha(fechasta)); 
					logger.debug("Entro a paso 2");
				}
		    }
		    catch(Exception ex){
		        Utils.generaExcepcion(ex, paso);
		    }
		    logger.info(
					new StringBuilder()
					.append("\n@@@@@@ renovacionColectivo @@@@@@")
					.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
					.toString());
		    return result;
	}
	
	//Getters y setters
	public void setRenovacionDAO(RenovacionDAO renovacionDAO) {
		this.renovacionDAO = renovacionDAO;
	}

	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}

	public void setCotizacionDAO(CotizacionDAO cotizacionDAO) {
		this.cotizacionDAO = cotizacionDAO;
	}

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public void setRecibosSigsService(RecibosSigsService recibosSigsService) {
		this.recibosSigsService = recibosSigsService;
	}
}