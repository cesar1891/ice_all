package mx.com.gseguros.portal.cotizacion.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.model.AseguradosFiltroVO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.ConfiguracionCoberturaDTO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.general.dao.AseguradoDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.MailService;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

public class CotizacionManagerImpl implements CotizacionManager 
{

	private static final Logger           logger       = LoggerFactory.getLogger(CotizacionManagerImpl.class);
	private static final SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	private CotizacionDAO  cotizacionDAO;
	private PantallasDAO   pantallasDAO;
	private PersonasDAO    personasDAO;
	private MesaControlDAO mesaControlDAO;
	private ConsultasDAO   consultasDAO;
	
	private boolean lanzatarAsincrono = true;
	
	@Value("${ruta.documentos.poliza}")
	private String rutaDocumentosPoliza;
	
	@Value("${ruta.servidor.reports}")
	private String rutaServidorReports;
	
	@Value("${pass.servidor.reports}")
	private String passServidorReports;
	
	@Value("${dominio.server.layouts2}")
	private String dominioServerLayouts2;
	
	@Autowired
	private transient Ice2sigsService ice2sigsService;
	
	@Autowired
    private MailService mailService;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private EmisionDAO emisionDAO;
	
	@Autowired
	private SiniestrosManager siniestrosManager;
	    
	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	@Autowired
	private AseguradoDAO aseguradoDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Autowired
    private PersonasManager personasManager;
	
	@Autowired
	private ConsultasManager consultasManager;
	
	@Autowired
	private AutosSIGSDAO autosSIGSDAO;
	
	@Override
	public void movimientoTvalogarGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdatribu
			,String valor)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ movimientoTvalogarGrupo @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ cdgrupo=") .append(cdgrupo)
				.append("\n@@@@@@ cdgarant=").append(cdgarant)
				.append("\n@@@@@@ status=")  .append(status)
				.append("\n@@@@@@ cdatribu=").append(cdatribu)
				.append("\n@@@@@@ valor=")   .append(valor)
				.toString()
				);
		cotizacionDAO.movimientoTvalogarGrupo(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsuplem
				,cdtipsit
				,cdgrupo
				,cdgarant
				,status
				,cdatribu
				,valor
				);
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ movimientoTvalogarGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
	}
	
	@Override
	public void movimientoMpolisitTvalositGrupo(
			String  cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo
			,String otvalor06
			,String otvalor07
			,String otvalor08
			,String otvalor09
			,String otvalor10
			,String otvalor11
			,String otvalor12
			,String otvalor22
			,String otvalor23
			,String otvalor24
			,String otvalor25
			,String otvalor26
			,String otvalor13
			,String otvalor16)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoMpolisitTvalositGrupo @@@@@@"
				,"\n@@@@@@ cdunieco " , cdunieco
				,"\n@@@@@@ cdramo " , cdramo
				,"\n@@@@@@ estado " , estado
				,"\n@@@@@@ nmpoliza " , nmpoliza
				,"\n@@@@@@ cdgrupo " , cdgrupo
				,"\n@@@@@@ otvalor06 " , otvalor06
				,"\n@@@@@@ otvalor07 " , otvalor07
				,"\n@@@@@@ otvalor08 " , otvalor08
				,"\n@@@@@@ otvalor09 " , otvalor09
				,"\n@@@@@@ otvalor10 " , otvalor10
				,"\n@@@@@@ otvalor11 " , otvalor11
				,"\n@@@@@@ otvalor12 " , otvalor12
				,"\n@@@@@@ otvalor22 " , otvalor22
				,"\n@@@@@@ otvalor23 " , otvalor23
				,"\n@@@@@@ otvalor24 " , otvalor24
				,"\n@@@@@@ otvalor25 " , otvalor25
				,"\n@@@@@@ otvalor26 " , otvalor26
				,"\n@@@@@@ otvalor13 " , otvalor13
				,"\n@@@@@@ otvalor16 " , otvalor16
				));
		
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco"  , cdunieco);
		params.put("cdramo"    , cdramo);
		params.put("estado"    , estado);
		params.put("nmpoliza"  , nmpoliza);
		params.put("cdgrupo"   , cdgrupo);
		params.put("otvalor06" , otvalor06);
		params.put("otvalor07" , otvalor07);
		params.put("otvalor08" , otvalor08);
		params.put("otvalor09" , otvalor09);
		params.put("otvalor10" , otvalor10);
		params.put("otvalor11" , otvalor11);
		params.put("otvalor12" , otvalor12);
		params.put("otvalor22" , otvalor22);
		params.put("otvalor23" , otvalor23);
		params.put("otvalor24" , otvalor24);
		params.put("otvalor25" , otvalor25);
		params.put("otvalor26" , otvalor26);
		params.put("otvalor13" , otvalor13);
		params.put("otvalor16" , otvalor16);
		cotizacionDAO.movimientoMpolisitTvalositGrupo(params);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ movimientoMpolisitTvalositGrupo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}

	@Override
	public void movimientoMpoligarGrupo(
			String  cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdmoneda
			,String accion
			,String respvalogar)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ movimientoMpoligarGrupo @@@@@@")
				.append("\n@@@@@@ cdunieco=")   .append(cdunieco)
				.append("\n@@@@@@ cdramo=")     .append(cdramo)
				.append("\n@@@@@@ estado=")     .append(estado)
				.append("\n@@@@@@ nmpoliza=")   .append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=")   .append(nmsuplem)
				.append("\n@@@@@@ cdtipsit=")   .append(cdtipsit)
				.append("\n@@@@@@ cdgrupo=")    .append(cdgrupo)
				.append("\n@@@@@@ cdgarant=")   .append(cdgarant)
				.append("\n@@@@@@ status=")     .append(status)
				.append("\n@@@@@@ cdmoneda=")   .append(cdmoneda)
				.append("\n@@@@@@ accion=")     .append(accion)
				.append("\n@@@@@@ respvalogar=").append(respvalogar)
				.toString()
				);
		cotizacionDAO.movimientoMpoligarGrupo(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsuplem
				,cdtipsit
				,cdgrupo
				,cdgarant
				,status
				,cdmoneda
				,accion
				,respvalogar
				);
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ movimientoMpoligarGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
	}
	
	@Override
	public Map<String,String> cargarDatosCotizacionGrupo(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite) throws Exception
	{
		logger.info(""
				+ "\n########################################"
				+ "\n###### cargarDatosCotizacionGrupo ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n cdtipsit "+cdtipsit
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n ntramite "+ntramite
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("ntramite" , ntramite);
		Map<String,String>datos=cotizacionDAO.cargarDatosCotizacionGrupo(params);
		if(datos==null)
		{
			datos=new HashMap<String,String>();
		}
		logger.info(""
				+ "\n###### cargarDatosCotizacionGrupo ######"
				+ "\n########################################"
				);
		return datos;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarDatosCotizacionGrupo2(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarDatosCotizacionGrupo2 @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ ntramite=").append(ntramite)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			Map<String,String>mapaDatos=cotizacionDAO.cargarDatosCotizacionGrupo2(
					cdunieco
					,cdramo
					,cdtipsit
					,estado
					,nmpoliza
					,ntramite
					);
			
			Map<String,String>mapaDatosAux = new HashMap<String,String>();
			for(Entry<String,String>en:mapaDatos.entrySet())
			{
				String key = en.getKey();
				if(StringUtils.isNotBlank(key)
						&&key.length()>"otvalor".length()
						&&key.substring(0, "otvalor".length()).equals("otvalor")
						)
				{
					mapaDatosAux.put(new StringBuilder("tvalopol_parametros.pv_").append(key).toString(),en.getValue());
				}
				else
				{
					mapaDatosAux.put(key,en.getValue());
				}
			}
			resp.setSmap(mapaDatosAux);
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
			resp.setRespuesta(new StringBuilder("Error al cargar cotizacion #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarDatosCotizacionGrupo2 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public Map<String,String> cargarDatosCotizacionGrupoEndoso(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite) throws Exception
	{
		logger.info(""
				+ "\n##############################################"
				+ "\n###### cargarDatosCotizacionGrupoEndoso ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n cdtipsit "+cdtipsit
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n ntramite "+ntramite
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("ntramite" , ntramite);
		Map<String,String>datos=cotizacionDAO.cargarDatosCotizacionGrupoEndoso(params);
		
		logger.info("datos"+datos);
		
		if(datos==null)
		{
			datos=new HashMap<String,String>();
		}
		logger.info(""
				+ "\n###### cargarDatosCotizacionGrupoEndoso ######"
				+ "\n##############################################"
				);
		return datos;
	}
	
	@Override
	public ManagerRespuestaSlistVO cargarGruposCotizacion2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarGruposCotizacion2 @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			List<Map<String,String>>listaAux    =cotizacionDAO.cargarGruposCotizacion2(cdunieco,cdramo,estado,nmpoliza);
			List<Map<String,String>>listaGrupos = new ArrayList<Map<String,String>>();
			
			for(Map<String,String>grupo:listaAux)
			{
				Map<String,String>grupoEditado=new HashMap<String,String>();
				
				for(Entry<String,String>en:grupo.entrySet())
				{
					String key = en.getKey();
					if(StringUtils.isNotBlank(key)
							&&key.length()>"otvalor".length()
							&&key.substring(0, "otvalor".length()).equals("otvalor")
							)
					{
						grupoEditado.put(new StringBuilder("parametros.pv_").append(key).toString(),en.getValue());
					}
					else
					{
						grupoEditado.put(key,en.getValue());
					}
				}
				listaGrupos.add(grupoEditado);
			}
			
			resp.setSlist(listaGrupos);
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
			resp.setRespuesta(new StringBuilder("Error al cargar grupos #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarGruposCotizacion2 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}

	@Override
	public ManagerRespuestaSlistVO cargarGruposCotizacionReexpedicion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarGruposCotizacionReexpedicion @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			List<Map<String,String>>listaAux    =cotizacionDAO.cargarGruposCotizacion2(cdunieco,cdramo,estado,nmpoliza);
			List<Map<String,String>>listaGrupos = new ArrayList<Map<String,String>>();
			
			for(Map<String,String>grupo:listaAux)
			{
				Map<String,String>grupoEditado=new HashMap<String,String>();
				
				for(Entry<String,String>en:grupo.entrySet())
				{
					String key = en.getKey();
					if(StringUtils.isNotBlank(key)
							&&key.length()>"otvalor".length()
							&&key.substring(0, "otvalor".length()).equals("otvalor")
							)
					{
						grupoEditado.put(new StringBuilder("pv_").append(key).toString(),en.getValue());
					}
					else
					{
						grupoEditado.put(key,en.getValue());
					}
				}
				listaGrupos.add(grupoEditado);
			}
			
			resp.setSlist(listaGrupos);
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
			resp.setRespuesta(new StringBuilder("Error al cargar grupos #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarGruposCotizacionReexpedicion @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public List<Map<String,String>>cargarGruposCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception
	{
		logger.info(""
				+ "\n####################################"
				+ "\n###### cargarGruposCotizacion ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		List<Map<String,String>>listaGrupos=cotizacionDAO.cargarGruposCotizacion(params);
		if(listaGrupos==null)
		{
			listaGrupos=new ArrayList<Map<String,String>>();
		}
		logger.info("lista size: "+listaGrupos.size());
		logger.info(""
				+ "\n###### cargarGruposCotizacion ######"
				+ "\n####################################"
				);
		return listaGrupos;
	}
	
	@Override
	public Map<String,String>cargarDatosGrupoLinea(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### cargarDatosGrupoLinea ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n cdgrupo "+cdgrupo
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdgrupo"  , cdgrupo);
		Map<String,String>datos=cotizacionDAO.cargarDatosGrupoLinea(params);
		if(datos==null)
		{
			datos=new HashMap<String,String>();
		}
		logger.info(""
				+ "\n###### cargarDatosGrupoLinea ######"
				+ "\n###################################"
				);
		return datos;
	}
	
	@Override
	public List<Map<String,String>>cargarTvalogarsGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### cargarTvalogarsGrupo ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n cdgrupo "+cdgrupo
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdgrupo"  , cdgrupo);
		List<Map<String,String>>listaTvalogars=cotizacionDAO.cargarTvalogarsGrupo(params);
		if(listaTvalogars==null)
		{
			listaTvalogars=new ArrayList<Map<String,String>>();
		}
		logger.debug("lista size: "+listaTvalogars.size());
		logger.info(""
				+ "\n###### cargarTvalogarsGrupo ######"
				+ "\n##################################"
				);
		return listaTvalogars;
	}
	
	@Override
	public List<Map<String,String>>cargarTarifasPorEdad(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdplan
			,String cdgrupo
			,String cdperpag)throws Exception
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### cargarTarifasPorEdad ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n nmsuplem "+nmsuplem
				+ "\n cdplan "+cdplan
				+ "\n cdgrupo "+cdgrupo
				+ "\n cdperpag "+cdperpag
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdplan"   , cdplan);
		params.put("cdgrupo"  , cdgrupo);
		params.put("cdperpag" , cdperpag);
		List<Map<String,String>>lista=cotizacionDAO.cargarTarifasPorEdad(params);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.info("cargarTarifasPorEdad lista size: "+lista.size());
		logger.info(""
				+ "\n###### cargarTarifasPorEdad ######"
				+ "\n##################################"
				);
		return lista;
	}
	
	@Override
	public List<Map<String,String>>cargarTarifasPorCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdplan
			,String cdgrupo
			,String cdperpag)throws Exception
	{
		logger.info(""
				+ "\n#######################################"
				+ "\n###### cargarTarifasPorCobertura ######"
				+ "\n cdunieco "+cdunieco
				+ "\n cdramo "+cdramo
				+ "\n estado "+estado
				+ "\n nmpoliza "+nmpoliza
				+ "\n nmsuplem "+nmsuplem
				+ "\n cdplan "+cdplan
				+ "\n cdgrupo "+cdgrupo
				+ "\n cdperpag "+cdperpag
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdplan"   , cdplan);
		params.put("cdgrupo"  , cdgrupo);
		params.put("cdperpag"  , cdperpag);
		List<Map<String,String>>lista=cotizacionDAO.cargarTarifasPorCobertura(params);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.info("cargarTarifasPorCobertura lista size: "+lista.size());
		logger.info(""
				+ "\n###### cargarTarifasPorCobertura ######"
				+ "\n#######################################"
				);
		return lista;
	}
	
	@Override
	public String cargarNombreAgenteTramite(String ntramite)throws Exception
	{
		logger.info(""
				+ "\n#######################################"
				+ "\n###### cargarNombreAgenteTramite ######"
				+ "\nntramite "+ntramite
				);
		String nombre=cotizacionDAO.cargarNombreAgenteTramite(ntramite);
		logger.info("cargarNombreAgenteTramite nombre: "+nombre);
		logger.info(""
				+ "\n###### cargarNombreAgenteTramite ######"
				+ "\n#######################################"
				);
		return nombre;
	}
	
	@Override
	public Map<String,String>cargarPermisosPantallaGrupo(String cdsisrol,String status)throws Exception
	{
		logger.info(""
				+ "\n#########################################"
				+ "\n###### cargarPermisosPantallaGrupo ######"
				+ "\ncdsisrol "+cdsisrol
				+ "\nstatus "+status
				);
		Map<String,String>res=cotizacionDAO.cargarPermisosPantallaGrupo(cdsisrol,status);
		logger.info(""
				+ "\nresponse "+res
				+ "\n###### cargarPermisosPantallaGrupo ######"
				+ "\n#########################################"
				);
		return res;
	}
	
	@Override
	public void guardarCensoCompletoMultisalud(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdedo
			,String cdmunici
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarCensoCompleto @@@@@@"
				,"\n@@@@@@ nombreArchivo=" , nombreArchivo
				,"\n@@@@@@ cdunieco="      , cdunieco
				,"\n@@@@@@ cdramo="        , cdramo
				,"\n@@@@@@ estado="        , estado
				,"\n@@@@@@ mpoliza="       , nmpoliza
				,"\n@@@@@@ cdedo="         , cdedo
				,"\n@@@@@@ cdmunici="      , cdmunici
				,"\n@@@@@@ cdplan1="       , cdplan1
				,"\n@@@@@@ cdplan2="       , cdplan2
				,"\n@@@@@@ cdplan3="       , cdplan3
				,"\n@@@@@@ cdplan4="       , cdplan4
				,"\n@@@@@@ cdplan5="       , cdplan5
				,"\n@@@@@@ complemento="   , complemento
				));
		
		cotizacionDAO.guardarCensoCompletoMultisalud(
				nombreArchivo
				,cdunieco
				,cdramo
				,estado
				,nmpoliza
				,cdedo
				,cdmunici
				,cdplan1
				,cdplan2
				,cdplan3
				,cdplan4
				,cdplan5
				,complemento
				);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarCensoCompleto @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	
	public int obtieneTipoValorAutomovil(String codigoPostal, String tipoVehiculo)throws Exception{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdpostal_i"     , codigoPostal);
		params.put("pv_cdtipveh_i"  , tipoVehiculo);
		Map<String,String>res = cotizacionDAO.obtieneTipoValorAutomovil(params);
		return Integer.parseInt(res.get("pv_etiqueta_o"));
	}

	public String obtieneCodigoPostalAutomovil(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac, String nmsuplem) throws Exception{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"  , cdunieco);
		params.put("pv_cdramo_i"    , cdramo);
		params.put("pv_estado_i"    , estado);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsituac_i"  , nmsituac);
		params.put("pv_nmsuplem_i"  , nmsuplem);
		
		return cotizacionDAO.obtieneCodigoPostalAutomovil(params);
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				+ "\ncdgrupo "+cdgrupo
				+ "\nstart "+start
				+ "\nlimit "+limit
				);
		
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("start"    , start);
		params.put("limit"    , limit);
		List<Map<String,String>> lista = cotizacionDAO.cargarAseguradosExtraprimas(cdunieco, 
				cdramo, 
				estado, 
				nmpoliza,
				nmsuplem,
				cdgrupo, 
				start, 
				limit);		
		logger.debug(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\n#########################################"
				);
		return lista;
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo)throws Exception
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				+ "\ncdgrupo "+cdgrupo
				);
		
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		List<Map<String,String>> lista = cotizacionDAO.cargarAseguradosExtraprimas(params);		
		logger.debug(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAseguradosExtraprimas ######"
				+ "\n#########################################"
				);
		return lista;
	}
	
	@Override
	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdtipsit
			,String ocupacion
			,String extraprimaOcupacion
			,String peso
			,String estatura
			,String extraprimaSobrepeso
			,String cdgrupo
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarExtraprimaAsegurado @@@@@@"
				,"\n@@@@@@ cdunieco="            , cdunieco
				,"\n@@@@@@ cdramo="              , cdramo
				,"\n@@@@@@ estado="              , estado
				,"\n@@@@@@ nmpoliza="            , nmpoliza
				,"\n@@@@@@ nmsuplem="            , nmsuplem
				,"\n@@@@@@ nmsituac="            , nmsituac
				,"\n@@@@@@ cdtipsit="            , cdtipsit
				,"\n@@@@@@ ocupacion="           , ocupacion
				,"\n@@@@@@ extraprimaOcupacion=" , extraprimaOcupacion
				,"\n@@@@@@ peso="                , peso
				,"\n@@@@@@ estatura="            , estatura
				,"\n@@@@@@ extraprimaSobrepeso=" , extraprimaSobrepeso
				,"\n@@@@@@ cdgrupo="             , cdgrupo
				));
		
		String paso = null;
		
		try
		{
			paso = "Actualizando valores adicionales de situaci\u00f3n";
			logger.debug(paso);
			
			cotizacionDAO.guardarExtraprimaAsegurado(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nmsituac
					,ocupacion
					,extraprimaOcupacion
					,peso
					,estatura
					,extraprimaSobrepeso
					);
			
			paso = "Recuperando coberturas de extraprimas";
			logger.debug(paso);
			
			List<Map<String,String>> coberturasDeExtraprimas = consultasDAO.recuperaCoberturasExtraprima(cdramo,cdtipsit);
			
			for(Map<String,String> coberturaExtraprima : coberturasDeExtraprimas)
			{
				String cdgarant = coberturaExtraprima.get("CDGARANT");
				
				paso = Utils.join("Ejecutando valores por defecto para la cobertura ",cdgarant);
				logger.debug(paso);
				
				cotizacionDAO.valoresPorDefecto(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplem
						,cdgarant
						,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
						);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarExtraprimaAsegurado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,List<Map<String,String>> slist1
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarExtraprimaAsegurado @@@@@@"
				,"\n@@@@@@ cdunieco="            , cdunieco
				,"\n@@@@@@ cdramo="              , cdramo
				,"\n@@@@@@ estado="              , estado
				,"\n@@@@@@ nmpoliza="            , nmpoliza
				,"\n@@@@@@ nmsuplem="            , nmsuplem
				,"\n@@@@@@ cdtipsit="            , cdtipsit
				,"\n@@@@@@ slist1="              , slist1
				));
		
		String paso = null;
		
		try
		{
			paso = "Actualizando valores adicionales de situaci\u00f3n";
			logger.debug(paso);
			
			cotizacionDAO.guardarExtraprimaAsegurado(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,slist1
					);
			
			paso = "Recuperando coberturas de extraprimas";
			logger.debug(paso);
			
			List<Map<String,String>> coberturasDeExtraprimas = consultasDAO.recuperaCoberturasExtraprima(cdramo,cdtipsit);
			
			for(Map<String,String> situacion:slist1){
				String nmsituac = situacion.get("nmsituac");
				for(Map<String,String> coberturaExtraprima : coberturasDeExtraprimas){
					String cdgarant = coberturaExtraprima.get("CDGARANT");
					cotizacionDAO.valoresPorDefecto(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,nmsuplem
							,cdgarant
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
							);
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarExtraprimaAsegurado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo)throws Exception
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				+ "\ncdgrupo "+cdgrupo
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		List<Map<String,String>>lista=cotizacionDAO.cargarAseguradosGrupo(params);
		logger.info(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\n###################################"
				);
		return lista;
	}

	@Override
	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				+ "\ncdgrupo "+cdgrupo
				+ "\nstart "+start
				+ "\nlimit "+limit
				);
		List<Map<String,String>>lista=cotizacionDAO.cargarAseguradosGrupo(
				cdunieco,
				cdramo,
				estado,
				nmpoliza,
				nmsuplem,
				cdgrupo,
				start,
				limit);
		logger.info(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAseguradosGrupo ######"
				+ "\n###################################"
				);
		return lista;
	}
	
	@Override
	public void borrarMpoliperGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo)throws Exception
	{
		logger.info(""
				+ "\n#################################"
				+ "\n###### borrarMpoliperGrupo ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\ncdgrupo "+cdgrupo
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdgrupo"  , cdgrupo);
		cotizacionDAO.borrarMpoliperGrupo(params);
		logger.info(""
				+ "\n###### borrarMpoliperGrupo ######"
				+ "\n#################################"
				);
	}
	
	@Deprecated
	@Override
	public Map<String,String>cargarTipoSituacion(String cdramo,String cdtipsit)throws Exception
	{
		logger.info(""
				+ "\n#################################"
				+ "\n###### cargarTipoSituacion ######"
				+ "\ncdramo "+cdramo
				+ "\ncdtipsit "+cdtipsit
				);
		Map<String,String>respuesta=cotizacionDAO.cargarTipoSituacion(cdramo,cdtipsit);
		logger.info(""
				+ "\nrespuesta "+respuesta
				+ "\n###### cargarTipoSituacion ######"
				+ "\n#################################"
				);
		return respuesta;
	}
	
	@Override
	public String cargarCduniecoAgenteAuto(String cdagente, String cdtipram)throws Exception
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ cargarCduniecoAgenteAuto @@@@@@",
				"\n@@@@@@ cdagente = ", cdagente,
				"\n@@@@@@ cdtipram = ", cdtipram
				));
		
		String cdunieco = cotizacionDAO.cargarCduniecoAgenteAuto(cdagente, cdtipram);
		
		logger.debug(Utils.log(
				"\n@@@@@@ cdunieco = ", cdunieco,
				"\n@@@@@@ cargarCduniecoAgenteAuto @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return cdunieco;
	}
	
	@Override
	public Map<String,String>obtenerDatosAgente(String cdagente,String cdramo)throws Exception
	{
		logger.info(new StringBuilder()
		.append("\n################################")
		.append("\n###### obtenerDatosAgente ######")
		.append("\ncdagente=").append(cdagente)
		.append("\ncdramo=").append(cdramo)
		.toString()
				);
		Map<String,String>datos=cotizacionDAO.obtenerDatosAgente(cdagente,cdramo);
		logger.info(new StringBuilder()
		.append("\ndatos=").append(datos)
		.append("\n###### obtenerDatosAgente ######")
		.append("\n################################")
		.toString()
				);
		return datos;
	}
	
	@Override
	public ManagerRespuestaSmapVO obtenerParametrosCotizacion(
			ParametroCotizacion parametro
			,String cdramo
			,String cdtipsit
			,String clave4
			,String clave5)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ obtenerParametrosCotizacion @@@@@@")
				.append("\n@@@@@@ parametro=").append(parametro.getParametro())
				.append("\n@@@@@@ cdramo=")   .append(cdramo)
				.append("\n@@@@@@ cdtipsit=") .append(cdtipsit)
				.append("\n@@@@@@ clave4=")   .append(clave4)
				.append("\n@@@@@@ clave5=")   .append(clave5)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			Map<String,String>valores=cotizacionDAO.obtenerParametrosCotizacion(parametro,cdramo,cdtipsit,clave4,clave5);
			resp.setSmap(valores);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("No existe el par\u00e1metro #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ obtenerParametrosCotizacion @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarAutoPorClaveGS(String cdramo,String clavegs,String cdtipsit,String cdsisrol, String tipoUnidad) throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarAutoPorClaveGS @@@@@@")
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ clave gs=").append(clavegs)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ cdsisrol=").append(cdtipsit)
				.append("\n@@@@@@ tipoUnidad=").append(tipoUnidad)
				.toString()
				);
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			Map<String,String>valores=cotizacionDAO.cargarAutoPorClaveGS(cdramo,clavegs,cdtipsit,cdsisrol,tipoUnidad);
			resp.setSmap(valores);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al recuperar datos del auto #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarAutoPorClaveGS @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarClaveGSPorAuto(String cdramo,String modelo) throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarClaveGSPorAuto @@@@@@")
				.append("\n@@@@@@ cdramo=").append(cdramo)
				.append("\n@@@@@@ modelo=").append(modelo)
				.toString()
				);
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			Map<String,String>params=new HashMap<String,String>();
			params.put("cdramo" , cdramo);
			params.put("modelo" , modelo);
			Map<String,String>valores=cotizacionDAO.cargarClaveGSPorAuto(params);
			resp.setSmap(valores);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al recuperar clave gs del auto #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarClaveGSPorAuto @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarSumaAseguradaAuto(String cdsisrol,String modelo,String version,String cdramo,String cdtipsit)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarSumaAseguradaAuto @@@@@@")
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.append("\n@@@@@@ modelo=")  .append(modelo)
				.append("\n@@@@@@ version=") .append(version)
				.toString()
				);
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			Map<String,String>params=new HashMap<String,String>();
			params.put("cdsisrol" , cdsisrol);
			params.put("modelo"   , modelo);
			params.put("version"  , version);
			Map<String,String>valores=cotizacionDAO.cargarSumaAseguradaAuto(params);
			logger.debug(
					new StringBuilder()
					.append("suma asegurada=")
					.append(valores)
					.toString());
			
			String claveSuma = "SUMASEG";
			String suma      = valores.get(claveSuma);
			Double dSuma     = Double.valueOf(suma);
			
			Map<String,String>valoresDeprecio=cotizacionDAO.obtenerParametrosCotizacion(
					ParametroCotizacion.DEPRECIACION
					,cdramo
					,cdtipsit
					,cdsisrol
					,null
					);
			
			logger.debug(
					new StringBuilder()
					.append("depreciacion=")
					.append(valoresDeprecio)
					.toString());
			
			Double deprecio=Double.valueOf(valoresDeprecio.get("P1VALOR"));
			dSuma = dSuma*(1d-deprecio);
			
			valores.put(claveSuma,String.format("%.2f", dSuma));
			
			resp.setSmap(valores);

			logger.debug(
					new StringBuilder()
					.append("suma asegurada nueva=")
					.append(valores)
					.toString());
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al cargar valor comercial del auto #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarSumaAseguradaAuto @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO agregarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem
			,String icd)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ agregarClausulaICD @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsituac=").append(nmsituac)
				.append("\n@@@@@@ cdclausu=").append(cdclausu)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ icd=")     .append(icd)
				.toString()
				);
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		try
		{
			Map<String,String>params=new HashMap<String,String>();
			params.put("cdunieco" , cdunieco);
			params.put("cdramo"   , cdramo);
			params.put("estado"   , estado);
			params.put("nmpoliza" , nmpoliza);
			params.put("nmsituac" , nmsituac);
			params.put("cdclausu" , cdclausu);
			params.put("nmsuplem" , nmsuplem);
			params.put("icd"      , icd);
			params.put("accion"   , Constantes.INSERT_MODE);
			cotizacionDAO.movimientoMpolicotICD(params);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al relacionar ICD #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ agregarClausulaICD @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO cargarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarClausulaICD @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsituac=").append(nmsituac)
				.append("\n@@@@@@ cdclausu=").append(cdclausu)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.toString()
				);
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			Map<String,String>params=new HashMap<String,String>();
			params.put("cdunieco" , cdunieco);
			params.put("cdramo"   , cdramo);
			params.put("estado"   , estado);
			params.put("nmpoliza" , nmpoliza);
			params.put("nmsituac" , nmsituac);
			params.put("cdclausu" , cdclausu);
			params.put("nmsuplem" , nmsuplem);
			resp.setSlist(cotizacionDAO.cargarMpolicotICD(params));
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al obtener los ICD #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
			
			resp.setSlist(new ArrayList<Map<String,String>>());
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarClausulaICD @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO borrarClausulaICD(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdclausu
			,String nmsuplem
			,String icd)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ borrarClausulaICD @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsituac=").append(nmsituac)
				.append("\n@@@@@@ cdclausu=").append(cdclausu)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ icd=")     .append(icd)
				.toString()
				);
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		try
		{
			Map<String,String>params=new HashMap<String,String>();
			params.put("cdunieco" , cdunieco);
			params.put("cdramo"   , cdramo);
			params.put("estado"   , estado);
			params.put("nmpoliza" , nmpoliza);
			params.put("nmsituac" , nmsituac);
			params.put("cdclausu" , cdclausu);
			params.put("nmsuplem" , nmsuplem);
			params.put("icd"      , icd);
			params.put("accion"   , Constantes.DELETE_MODE);
			cotizacionDAO.movimientoMpolicotICD(params);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(
					new StringBuilder()
					.append("Error al borrar ICD #").append(timestamp)
					.toString()
					);
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ borrarClausulaICD @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public List<Map<String,String>>cargarConfiguracionGrupo(String cdramo,String cdtipsit)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n######################################")
				.append("\n###### cargarConfiguracionGrupo ######")
				.append("\n###### cdramo=")  .append(cdramo)
				.append("\n###### cdtipsit=").append(cdtipsit)
				.toString()
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		List<Map<String,String>>lista=cotizacionDAO.cargarConfiguracionGrupo(params);
		logger.info(
				new StringBuilder()
				.append("\n###### lista=").append(lista)
				.append("\n###### cargarConfiguracionGrupo ######")
				.append("\n######################################")
				.toString()
				);
		return lista;
	}
	
	@Override
	public ComponenteVO cargarComponenteTatrisit(String cdtipsit,String cdusuari,String cdatribu)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n######################################")
				.append("\n###### cargarComponenteTatrisit ######")
				.append("\n###### cdtipsit=").append(cdtipsit)
				.append("\n###### cdusuari=").append(cdusuari)
				.append("\n###### cdatribu=").append(cdatribu)
				.toString()
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdusuari" , cdusuari);
		params.put("cdatribu" , cdatribu);
		ComponenteVO comp = cotizacionDAO.cargarComponenteTatrisit(params);
		logger.info(
				new StringBuilder()
				.append("\n###### componente=").append(comp)
				.append("\n###### cargarComponenteTatrisit ######")
				.append("\n######################################")
				.toString()
				);
		return comp;
	}
	
	@Override
	public ComponenteVO cargarComponenteTatrigar(String cdramo,String cdtipsit,String cdgarant,String cdatribu)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n######################################")
				.append("\n###### cargarComponenteTatrigar ######")
				.append("\n###### cdramo=")  .append(cdramo)
				.append("\n###### cdtipsit=").append(cdtipsit)
				.append("\n###### cdgarant=").append(cdgarant)
				.append("\n###### cdatribu=").append(cdatribu)
				.toString()
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdgarant" , cdgarant);
		params.put("cdatribu" , cdatribu);
		ComponenteVO comp = cotizacionDAO.cargarComponenteTatrigar(params);
		logger.info(
				new StringBuilder()
				.append("\n###### componente=").append(comp)
				.append("\n###### cargarComponenteTatrigar ######")
				.append("\n######################################")
				.toString()
				);
		return comp;
	}
	
	@Override
	public ManagerRespuestaVoidVO validarDescuentoAgente(
			String  tipoUnidad
			,String uso
			,String zona
			,String promotoria
			,String cdagente
			,String descuento)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ validarDescuentoAgente @@@@@@")
				.append("\n@@@@@@ tipoUnidad=").append(tipoUnidad)
				.append("\n@@@@@@ uso=").append(uso)
				.append("\n@@@@@@ zona=").append(zona)
				.append("\n@@@@@@ promotoria=").append(promotoria)
				.append("\n@@@@@@ cdagente=").append(cdagente)
				.append("\n@@@@@@ descuento=").append(descuento)
				.toString()
				);
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		//procedure
		try
		{
			cotizacionDAO.validarDescuentoAgente(tipoUnidad,uso,zona,promotoria,cdagente,descuento);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder(ex.getMessage()).append(" #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ validarDescuentoAgente @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public void movimientoTdescsup(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nsuplogi
			,String cdtipsup
			,Date feemisio
			,String nmsolici
			,Date fesolici
			,Date ferefere
			,String cdseqpol
			,String cdusuari
			,String nusuasus
			,String nlogisus
			,String cdperson
			,String accion)throws Exception
	{
		cotizacionDAO.movimientoTdescsup(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nsuplogi
				,cdtipsup
				,feemisio
				,nmsolici
				,fesolici
				,ferefere
				,cdseqpol
				,cdusuari
				,nusuasus
				,nlogisus
				,cdperson
				,accion);
	}
	
	@Override
	public ManagerRespuestaImapSmapVO pantallaCotizacionGrupo(
			String cdramo
			,String cdtipsit
			,String ntramite
			,String ntramiteVacio
			,String status
			,String cdusuari
			,String cdsisrol
			,String nombreUsuario
			,String cdagente
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ pantallaCotizacionGrupo @@@@@@")
				.append("\n@@@@@@ cdramo=")       .append(cdramo)
				.append("\n@@@@@@ cdtipsit=")     .append(cdtipsit)
				.append("\n@@@@@@ ntramite=")     .append(ntramite)
				.append("\n@@@@@@ ntramiteVacio=").append(ntramiteVacio)
				.append("\n@@@@@@ status=")       .append(status)
				.append("\n@@@@@@ cdusuari=")     .append(cdusuari)
				.append("\n@@@@@@ cdsisrol=")     .append(cdsisrol)
				.append("\n@@@@@@ nombreUsuario=").append(nombreUsuario)
				.append("\n@@@@@@ cdagente=")     .append(cdagente)
				.toString()
				);
		
		ManagerRespuestaImapSmapVO resp = new ManagerRespuestaImapSmapVO(true);

		//retocar datos de entrada
		try
		{
			resp.setSmap(new HashMap<String,String>());
			if(StringUtils.isBlank(status))
			{
				status = "0";
			}
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al procesar datos #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		//retocar datos de entrada
		
		String nombreAgente = null;
		
		//datos del agente
		if(resp.isExito())
		{
			try
			{
				//si entran como agente
				if(StringUtils.isBlank(ntramite)&&StringUtils.isBlank(ntramiteVacio))
				{
				    DatosUsuario datUsu = cotizacionDAO.cargarInformacionUsuario(cdusuari,cdtipsit);
				    String cdunieco     = datUsu.getCdunieco();
	        		
				    resp.getSmap().put("cdunieco",cdunieco);
				    
	        		cdagente     = datUsu.getCdagente();
	        		nombreAgente = nombreUsuario;
				}
				//si entran por tramite o tramite vacio
				else if(StringUtils.isNotBlank(ntramite)||StringUtils.isNotBlank(ntramiteVacio))
				{
					nombreAgente = cotizacionDAO.cargarNombreAgenteTramite(StringUtils.isNotBlank(ntramite)?ntramite:ntramiteVacio);
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener datos del agente #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//datos del agente
		
		GeneradorCampos gcTatri            = null;
		GeneradorCampos gcGral             = null;
		List<ComponenteVO>tatrisitOriginal = null;
		
		//componentes
		if(resp.isExito())
		{
			try
			{
				gcTatri = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gcTatri.setCdramo(cdramo);
				gcTatri.setCdtipsit(cdtipsit);
				resp.setImap(new HashMap<String,Item>());
				
				//TATRISIT ORIGINAL
				tatrisitOriginal = cotizacionDAO.cargarTatrisit(cdtipsit, cdusuari);
				
				//columnas base
				List<ComponenteVO>tatrisitColsBase = new ArrayList<ComponenteVO>();
				for(ComponenteVO iTatri:tatrisitOriginal)
				{
					if(StringUtils.isNotBlank(iTatri.getSwsuscri())
							&&iTatri.getSwsuscri().equals("N")
							&&iTatri.getSwGrupo().equals("S")
							&&iTatri.getSwGrupoLinea().equals("N")
							)
					{
						logger.debug(new StringBuilder("SE AGREGA PARA COLUMNA BASE ").append(iTatri).toString());
						iTatri.setColumna("S");
						//Se oculta para cdsisrol indicados, la columna DERECHOS DE POLIZA, para cdtipsit RC (EGS)
						if((RolSistema.AGENTE.getCdsisrol().equals(cdsisrol) || RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol) || RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
							&&TipoSituacion.RECUPERA_COLECTIVO.getCdtipsit().equals(cdtipsit)&&iTatri.getLabel().equals("DERECHOS DE POLIZA")){
							iTatri.setColumna(iTatri.COLUMNA_OCULTA);
						}
						tatrisitColsBase.add(iTatri);
					}
				}
				
				if(tatrisitColsBase.size()>0)
				{
					gcTatri.generaComponentes(tatrisitColsBase, true, true, false, true, true, false);
					resp.getImap().put("colsBaseFields"  , gcTatri.getFields());
					resp.getImap().put("colsBaseColumns" , gcTatri.getColumns());
				}
				else
				{
					resp.getImap().put("colsBaseFields"  , null);
					resp.getImap().put("colsBaseColumns" , null);
				}
				//columnas base
				
				//columnas extendidas (de coberturas)
				List<ComponenteVO> tatrisitColsCober = new ArrayList<ComponenteVO>();
				for(ComponenteVO iTatri:tatrisitOriginal)
				{
					if(StringUtils.isNotBlank(iTatri.getSwsuscri())
							&&iTatri.getSwsuscri().equals("N")
							&&iTatri.getSwGrupo().equals("S")
							)
					{
						logger.debug(new StringBuilder("SE AGREGA PARA COLUMNA DE COBERTURA ").append(iTatri).toString());
						iTatri.setColumna("S");
						//Se oculta para cdsisrol indicados, la columna DERECHOS DE POLIZA, para cdtipsit RC (EGS)
						if((RolSistema.AGENTE.getCdsisrol().equals(cdsisrol) || RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol) || RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
							&&TipoSituacion.RECUPERA_COLECTIVO.getCdtipsit().equals(cdtipsit)&&iTatri.getLabel().equals("DERECHOS DE POLIZA")){
							iTatri.setColumna(iTatri.COLUMNA_OCULTA); //EGS
						}
						tatrisitColsCober.add(iTatri);
					}
				}
				if(tatrisitColsCober.size()>0)
				{
					gcTatri.generaComponentes(tatrisitColsCober, true, true, false, true, true, false);
					resp.getImap().put("colsExtFields"  , gcTatri.getFields());
					resp.getImap().put("colsExtColumns" , gcTatri.getColumns());
				}
				else
				{
					resp.getImap().put("colsExtFields"  , null);
					resp.getImap().put("colsExtColumns" , null);
				}
				//columnas extendidas (de coberturas)
				
				//factores
				List<ComponenteVO>factores = new ArrayList<ComponenteVO>();
				for(ComponenteVO iTatri:tatrisitOriginal)
				{
					if(StringUtils.isNotBlank(iTatri.getSwsuscri())
							&&iTatri.getSwsuscri().equals("N")
							&&iTatri.getSwGrupo().equals("N")
							&&iTatri.getSwGrupoFact().equals("S")
							)
					{
						logger.debug(new StringBuilder("SE AGREGA PARA FACTOR ").append(iTatri).toString());
						iTatri.setColumna("S");
						factores.add(iTatri);
						iTatri.setMenorCero(true);
					}
				}
				if(factores.size()>0)
				{
					gcTatri.generaComponentes(factores, true, true, false, true, true, false);
					resp.getImap().put("factoresFields"  , gcTatri.getFields());
					resp.getImap().put("factoresColumns" , gcTatri.getColumns());
				}
				else
				{
					resp.getImap().put("factoresFields"  , null);
					resp.getImap().put("factoresColumns" , null);
				}
				//factores
				
				gcGral = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gcGral.setCdramo(cdramo);
				
				List<ComponenteVO>tatripol=cotizacionDAO.cargarTatripol(cdramo,cdtipsit,"C");
				if(tatripol!=null&&tatripol.size()>0)
				{
					logger.debug("Si hay tatripol");
					
					/*
					 * se recuperan componentes que sirven para omitir elementos de tatripol
					 */
					List<ComponenteVO> listaTatripolOmitidos = pantallasDAO.obtenerComponentes(
							null               , null                , cdramo
							,cdtipsit          , null                , cdsisrol
							,"COTIZACION_GRUPO", "TATRIPOL_OMITIDOS" , null
							);
					
					if(listaTatripolOmitidos!=null && listaTatripolOmitidos.size()>0)
					{
						logger.debug("Si hay tatripol omitidos desde editor pantallas");
						
						List<ComponenteVO> tatripolAux = new ArrayList<ComponenteVO>();
						for(ComponenteVO tatripolItem : tatripol)
						{
							boolean omitido = false;
							for(ComponenteVO tatripolOmitido : listaTatripolOmitidos)
							{
								if(tatripolOmitido.getNameCdatribu().equals(tatripolItem.getNameCdatribu()))
								{
									logger.debug(Utils.log(
											"tatripol omitido original: <"
											,tatripolItem.getNameCdatribu()
											,"> editor pantallas: <"
											,tatripolOmitido.getNameCdatribu()
											,">"
											));
									omitido = true;
									break;
								}
							}
							if(!omitido)
							{
								logger.debug(Utils.log("No se omite <",tatripolItem.getNameCdatribu(),">"));
								tatripolAux.add(tatripolItem);
							}
						}
						tatripol = tatripolAux;
					}
					else
					{
						logger.debug("No hay tatripol omitidos desde editor de pantallas");
					}
					
					gcGral.generaComponentes(tatripol,true,false,true,false,false,false);
					resp.getImap().put("itemsRiesgo",gcGral.getItems());
				}
				else
				{
					resp.getImap().put("itemsRiesgo" , null);
				}
				gcGral.setCdramo(null);
				
				List<ComponenteVO>componentesContratante=pantallasDAO.obtenerComponentes(
						null               , "|"+cdramo+"|" , "|"+status+"|" ,
						null               , null           , cdsisrol       ,
						"COTIZACION_GRUPO" , "CONTRATANTE"  , null);
				gcGral.generaComponentes(componentesContratante, true,false,true,false,false,false);
				resp.getImap().put("itemsContratante"  , gcGral.getItems());
				
				List<ComponenteVO>componentesAgente=pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "AGENTE", null);
				componentesAgente.get(0).setDefaultValue(nombreAgente);
				componentesAgente.get(1).setDefaultValue(cdagente);
				gcGral.generaComponentes(componentesAgente, true,false,true,false,false,false);
				resp.getImap().put("itemsAgente"  , gcGral.getItems());
				
				List<ComponenteVO>columnaEditorPlan=pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "EDITOR_PLANES2", null);
				gcGral.generaComponentes(columnaEditorPlan, true, false, false, true, true, false);
				resp.getImap().put("editorPlanesColumn",gcGral.getColumns());
				
				List<ComponenteVO>comboFormaPago=pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_FORMA_PAGO", null);
				gcGral.generaComponentes(comboFormaPago, true,false,true,false,false,false);
				resp.getImap().put("comboFormaPago"  , gcGral.getItems());
				
				List<ComponenteVO>comboRepartoPago=pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, null,
						"COTIZACION_GRUPO", "COMBO_REPARTO_PAGO", null);
				gcGral.generaComponentes(comboRepartoPago, true,false,true,false,false,false);
				resp.getImap().put("comboRepartoPago"  , gcGral.getItems());
				
				List<ComponenteVO>comboPool = pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "COMBO_POOL", null);
				gcGral.generaComponentes(comboPool, true,false,true,false,false,false);
				resp.getImap().put("comboPool"  , gcGral.getItems());
				
				List<ComponenteVO>datosPoliza = pantallasDAO.obtenerComponentes(
						null, null, null,
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "DATOS_POLIZA", null);
				gcGral.generaComponentes(datosPoliza, true,false,true,false,false,false);
				resp.getImap().put("datosPoliza"  , gcGral.getItems());
				
				List<ComponenteVO>botones=pantallasDAO.obtenerComponentes(
						null, null, "|"+status+"|",
						null, null, cdsisrol,
						"COTIZACION_GRUPO", "BOTO2NES", null);
				if(botones!=null&&botones.size()>0)
				{
					gcGral.generaComponentes(botones, true, false, false, false, false, true);
					resp.getImap().put("botones" , gcGral.getButtons());
				}
				else
				{
					resp.getImap().put("botones" , null);
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener componentes #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//componentes
		
		//permisos
		if(resp.isExito())
		{
			try
			{
				resp.getSmap().put("status" , status);
				resp.getSmap().putAll(cotizacionDAO.cargarPermisosPantallaGrupo(cdsisrol,status));
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener permisos #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//permisos
		
		//campos para extraprimas
		if(resp.isExito() && resp.getSmap().containsKey("EXTRAPRIMAS")
				 && StringUtils.isNotBlank(resp.getSmap().get("EXTRAPRIMAS"))
				 && resp.getSmap().get("EXTRAPRIMAS").equals("S"))
		{
			try
			{
				List<ComponenteVO>tatrisitExtraprima = new ArrayList<ComponenteVO>();
				for(ComponenteVO iTatri:tatrisitOriginal)
				{
					if(StringUtils.isNotBlank(iTatri.getSwsuscri())
							&&iTatri.getSwsuscri().equals("S")
							&&iTatri.getSwGrupoExtr().equals("S")
							)
					{
						logger.debug(new StringBuilder("SE AGREGA PARA EXTRAPRIMA ").append(iTatri).toString());
						iTatri.setColumna("S");
						tatrisitExtraprima.add(iTatri);
					}
				}
				
				if(tatrisitExtraprima.size()>0)
				{
					gcTatri.generaComponentes(tatrisitExtraprima, true, true, false, true, true, false);
					resp.getImap().put("extraprimasFields"  , gcTatri.getFields());
					resp.getImap().put("extraprimasColumns" , gcTatri.getColumns());
				}
				else
				{
					resp.getImap().put("extraprimasFields"  , null);
					resp.getImap().put("extraprimasColumns" , null);
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener componentes de extraprimas #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//campos para extraprimas
		
		//campos de asegurados
		if(resp.isExito() && resp.getSmap().containsKey("ASEGURADOS")
				 && StringUtils.isNotBlank(resp.getSmap().get("ASEGURADOS"))
				 && resp.getSmap().get("ASEGURADOS").equals("S"))
		{
			try
			{
				
				List<ComponenteVO>componentesExtraprimas=pantallasDAO.obtenerComponentes(
						null  , null , null
						,cdtipsit , null , cdsisrol
						,"COTIZACION_GRUPO", "ASEGURADOS", null);
				gcGral.generaComponentes(componentesExtraprimas, true, true, false, true, false, false);
				resp.getImap().put("aseguradosColumns" , gcGral.getColumns());
				resp.getImap().put("aseguradosFields"  , gcGral.getFields());
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener componentes de asegurados #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//campos de asegurados
		
		//campos para recuperados
		if(resp.isExito() && resp.getSmap().containsKey("ASEGURADOS_EDITAR")
				 && StringUtils.isNotBlank(resp.getSmap().get("ASEGURADOS_EDITAR"))
				 && resp.getSmap().get("ASEGURADOS_EDITAR").equals("S"))
		{
			try
			{
				List<ComponenteVO>componentesRecuperados=pantallasDAO.obtenerComponentes(
						null  , null , null
						,null , null , cdsisrol
						,"COTIZACION_GRUPO", "RECUPERADOS", null);
				gcGral.generaComponentes(componentesRecuperados, true, true, false, true, true, false);
				resp.getImap().put("recuperadosColumns" , gcGral.getColumns());
				resp.getImap().put("recuperadosFields"  , gcGral.getFields());
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener componentes de recuperados #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		//campos para recuperados
		
		//atributo variable para recuperar tatrigar
		if(resp.isExito())
		{
			try
			{
				Map<String,String>atrivarTatrigar=cotizacionDAO.obtenerParametrosCotizacion(
						ParametroCotizacion.ATRIBUTO_VARIABLE_TATRIGAR
						,cdramo
						,cdtipsit
						,null
						,null);
				resp.getSmap().put("ATRIVAR_TATRIGAR" , atrivarTatrigar.get("P1VALOR"));
			}
			catch(Exception ex)
			{
				resp.getSmap().put("ATRIVAR_TATRIGAR" , "XX");
			}
		}
		//atributo variable para recuperar tatrigar
		
		if (resp.isExito()) {
		    try {
		        resp.getSmap().put("customCode", consultasDAO.recuperarCodigoCustom("25", cdsisrol));
		    } catch (Exception ex) {
		        resp.getSmap().put("customCode", "/* error */");
		        logger.error("Error sin impacto al recuperar codigo custom",ex);
		    }
		}
		
		if (resp.isExito()) {
            try {
                StringBuilder switchCoberturaTatrisitString = new StringBuilder("_x");
                List<ComponenteVO> switchCoberturaTatrisitListaCmp = pantallasDAO.obtenerComponentes(
                        null, //cdtiptra, 
                        null, //cdunieco, 
                        "|" + cdramo + "|", 
                        "|" + cdtipsit + "|", 
                        null, //estado, 
                        cdsisrol, 
                        "COTIZACION_GRUPO", 
                        "SWITCH_COBER_VALOSIT", 
                        null
                        );
                for (ComponenteVO switchCoberturaTatrisitListaCmpIte : switchCoberturaTatrisitListaCmp) {
                    switchCoberturaTatrisitString.append(",_").append(switchCoberturaTatrisitListaCmpIte.getLabel());
                }
                resp.getSmap().put("swtichCoberturaTatrisit", switchCoberturaTatrisitString.toString());
            } catch (Exception ex) {
                resp.getSmap().put("swtichCoberturaTatrisit", "_x");
                logger.error("Error sin impacto al recuperar codigo custom",ex);
            }
        }
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ pantallaCotizacionGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarClienteCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarClienteCotizacion @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		try
		{
			resp.setSmap(cotizacionDAO.cargarClienteCotizacion(cdunieco,cdramo,estado,nmpoliza));
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener cliente de cotizacion #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarClienteCotizacion @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarConceptosGlobalesGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdperpag)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarConceptosGlobalesGrupo @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ cdperpag=").append(cdperpag)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp=new ManagerRespuestaSmapVO(true);
		
		//procedure
		try
		{
			resp.setSmap(cotizacionDAO.cargarConceptosGlobalesGrupo(cdunieco,cdramo,estado,nmpoliza,nmsuplem,cdperpag));
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
			resp.setRespuesta(new StringBuilder("Error al obtener conceptos globales #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarConceptosGlobalesGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO generarTramiteGrupo(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,Map<String,String> tvalopol
			,String ntramite
			,String ntramiteVacio
			,String miTimestamp
			,String rutaDocumentosTemporal
			,String tipoCenso
			,String dominioServerLayouts
			,String userServerLayouts
			,String passServerLayouts
			,String directorioServerLayouts
			,String cdtipsit
			,List<Map<String,Object>>grupos
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,String cdagente
			,String cdusuari
			,String cdsisrol
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String cdelemen
			,boolean sincenso
			,boolean censoAtrasado
			,boolean resubirCenso
			,boolean complemento
			,String cdpool
			,String nombreCensoConfirmado
			,boolean asincrono
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			,boolean duplicar
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ generarTramiteGrupo @@@@@@")
				.append("\n@@@@@@ cdunieco=")               .append(cdunieco)
				.append("\n@@@@@@ cdramo=")                 .append(cdramo)
				.append("\n@@@@@@ nmpoliza=")               .append(nmpoliza)
				.append("\n@@@@@@ feini=")                  .append(feini)
				.append("\n@@@@@@ fefin=")                  .append(fefin)
				.append("\n@@@@@@ cdperpag=")               .append(cdperpag)
				.append("\n@@@@@@ pcpgocte=")               .append(pcpgocte)
				.append("\n@@@@@@ smap=")                   .append(tvalopol)
				.append("\n@@@@@@ ntramite=")               .append(ntramite)
				.append("\n@@@@@@ ntramiteVacio=")          .append(ntramiteVacio)
				.append("\n@@@@@@ miTimestamp=")            .append(miTimestamp)
				.append("\n@@@@@@ rutaDocumentosTemporal=") .append(rutaDocumentosTemporal)
				.append("\n@@@@@@ tipoCenso=")              .append(tipoCenso)
				.append("\n@@@@@@ dominioServerLayouts=")   .append(dominioServerLayouts)
				.append("\n@@@@@@ userServerLayouts=")      .append(userServerLayouts)
				.append("\n@@@@@@ passServerLayouts=")      .append(passServerLayouts)
				.append("\n@@@@@@ directorioServerLayouts=").append(directorioServerLayouts)
				.append("\n@@@@@@ cdtipsit=")               .append(cdtipsit)
				.append("\n@@@@@@ grupos=")                 .append(grupos)
				.append("\n@@@@@@ codpostalCli=")           .append(codpostalCli)
				.append("\n@@@@@@ cdedoCli=")               .append(cdedoCli)
				.append("\n@@@@@@ cdmuniciCli=")            .append(cdmuniciCli)
				.append("\n@@@@@@ cdagente=")               .append(cdagente)
				.append("\n@@@@@@ cdusuari=")               .append(cdusuari)
				.append("\n@@@@@@ cdsisrol=")               .append(cdsisrol)
				.append("\n@@@@@@ clasif=")                 .append(clasif)
				.append("\n@@@@@@ LINEA_EXTENDIDA=")        .append(LINEA_EXTENDIDA)
				.append("\n@@@@@@ cdpersonCli=")            .append(cdpersonCli)
				.append("\n@@@@@@ nombreCli=")              .append(nombreCli)
				.append("\n@@@@@@ rfcCli=")                 .append(rfcCli)
				.append("\n@@@@@@ dsdomiciCli=")            .append(dsdomiciCli)
				.append("\n@@@@@@ nmnumeroCli=")            .append(nmnumeroCli)
				.append("\n@@@@@@ nmnumintCli=")            .append(nmnumintCli)
				.append("\n@@@@@@ cdelemen=")               .append(cdelemen)
				.append("\n@@@@@@ sincenso=")               .append(sincenso)
				.append("\n@@@@@@ censoAtrasado=")          .append(censoAtrasado)
				.append("\n@@@@@@ resubirCenso=")           .append(resubirCenso)
				.append("\n@@@@@@ complemento=")            .append(complemento)
				.append("\n@@@@@@ cdpool=")                 .append(cdpool)
				.append("\n@@@@@@ nombreCensoConfirmado=")  .append(nombreCensoConfirmado)
				.append("\n@@@@@@ asincrono=")              .append(asincrono)
				.append("\n@@@@@@ cdideper_=")              .append(cdideper_)
				.append("\n@@@@@@ cdideext_=")              .append(cdideext_)
				.append("\n@@@@@@ nmpolant=")               .append(nmpolant)
				.append("\n@@@@@@ nmrenova=")               .append(nmrenova)
				.append("\n@@@@@@ duplicar=")               .append(duplicar)
				.append("\n@@@@@@ usuarioSesion=")          .append(usuarioSesion.toString())
				.toString()
				);
		
		ManagerRespuestaSmapVO resp=new ManagerRespuestaSmapVO(true);
		resp.setSmap(new HashMap<String,String>());
		
		String swexiperCli = Constantes.NO;
		
		if(tvalopol !=null && !tvalopol.isEmpty() && tvalopol.containsKey("swexiper") && StringUtils.isNotBlank(tvalopol.get("swexiper"))){
			if(Constantes.SI.equalsIgnoreCase(tvalopol.get("swexiper"))){
				swexiperCli =  Constantes.SI;
			}
		}
		
		//nmpoliza
		if(resp.isExito()&&(StringUtils.isBlank(nmpoliza) || duplicar))
		{
			try
			{
				nmpoliza = cotizacionDAO.calculaNumeroPoliza(cdunieco,cdramo,"W");
				resp.getSmap().put("nmpoliza",nmpoliza);
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
				resp.setRespuesta(new StringBuilder("Error al calcular numero de poliza #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		Date fechaHoy = new Date();
		
		//mpolizas
		if(resp.isExito())
		{
			try
			{
				cotizacionDAO.movimientoPoliza(
						cdunieco
						,cdramo
						,"W"      //estado
						,nmpoliza
						,"0"      //nmsuplem
						,"V"      //status
						,"0"      //swestado
						,null     //nmsolici
			            ,null     //feautori
			            ,null     //cdmotanu
			            ,null     //feanulac
			            ,"N"      //swautori
			            ,"001"    //cdmoneda
			            ,null     //feinisus
			            ,null     //fefinsus
			            ,"R"      //ottempot
			            ,feini
			            ,"12:00"  //hhefecto
			            ,fefin
			            ,null     //fevencim
			            ,StringUtils.isBlank(nmrenova) ? "0" : nmrenova
			            ,null     //ferecibo
			            ,null     //feultsin
			            ,"0"      //nmnumsin
			            ,"N"      //cdtipcoa
			            ,"A"      //swtarifi
			            ,null     //swabrido
			            ,renderFechas.format(fechaHoy) //feemisio
			            ,cdperpag
			            ,null     //nmpoliex
			            ,"P1"     //nmcuadro
			            ,"100"    //porredau
			            ,"S"      //swconsol
			            ,nmpolant //nmpolant
			            ,null     //nmpolnva
			            ,renderFechas.format(fechaHoy) //fesolici
			            ,null     //cdramant
			            ,null     //cdmejred
			            ,null     //nmpoldoc
			            ,null     //nmpoliza2
			            ,null     //nmrenove
			            ,null     //nmsuplee
			            ,null     //ttipcamc
			            ,null     //ttipcamv
			            ,null     //swpatent
			            ,pcpgocte
			            ,"F"      //tipoflot
			            ,cdpool   //agrupador
			            ,"U"      //accion
						);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al guardar poliza #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//fechas mpolisit
		if(resp.isExito())
		{
			try
			{
				cotizacionDAO.actualizarFefecsitMpolisit(cdunieco, cdramo, "W", nmpoliza, "0");
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al guardar fechas de situaciones #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//tvalopol
		if(resp.isExito())
		{
			try
			{
				Map<String,String>valores = new HashMap<String,String>();
				for(Entry<String,String>atrib:tvalopol.entrySet())
				{
					String key = atrib.getKey();
					if(StringUtils.isNotBlank(key)
							&&key.length()>="tvalopol_".length())
					{
						if(key.substring(0, "tvalopol_".length()).equals("tvalopol_"))
						{
							valores.put(key.substring("tvalopol_parametros.pv_".length(), key.length()),atrib.getValue());
						}
					}
				}
				if(valores.size()>0)
				{
					logger.debug(new StringBuilder("SE GUARDAN VALORES EN TVALOPOL=").append(valores).toString());
					cotizacionDAO.movimientoTvalopol(
							cdunieco
							,cdramo
							,"W"
							,nmpoliza
							,"0" //nmsuplem
							,"V" //status
							,valores
							);
				}
				else
				{
					logger.debug("NO SE GUARDAN VALORES EN TVALOPOL");
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al guardar datos adicionales de poliza #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//contratante
		if(resp.isExito())
		{
			try
			{
				if(StringUtils.isBlank(cdpersonCli)){
					cdpersonCli = this.guardarContratanteColectivo(cdunieco, cdramo, "W", nmpoliza, "0", rfcCli, cdpersonCli, nombreCli,
							codpostalCli, cdedoCli, cdmuniciCli, dsdomiciCli, nmnumeroCli, nmnumintCli, "1", false, usuarioSesion);
					swexiperCli = Constantes.NO;
				}
				
				logger.debug("Guardar Tramite Grupo 2, Guardar contratante, swexiper: "+swexiperCli);
				cotizacionDAO.movimientoMpoliper(
						cdunieco
						,cdramo
						,"W"
						,nmpoliza
						,"0"       //nmsituac
						,"1"       //cdrol
						,cdpersonCli
						,"0"       //nmsuplem
						,"V"       //status
						,"1"       //nmorddom
						,null      //swreclam
						,Constantes.INSERT_MODE
						,swexiperCli
						);
				
				logger.debug("VALOR DE SWEXIPER : "+ swexiperCli);
				
			}
            catch(Exception ex)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al guardar el contratante #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
		}
		
		boolean hayTramite      = StringUtils.isNotBlank(ntramite);
		boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
		boolean esCensoSolo     = StringUtils.isNotBlank(tipoCenso)&&tipoCenso.equalsIgnoreCase("solo");
		String  nombreCenso     = nombreCensoConfirmado;
		
		//enviar censo
		if(resp.isExito()&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar)&&!sincenso&&!complemento&&StringUtils.isBlank(nombreCensoConfirmado))
		{
			FileInputStream input       = null;
			Workbook        workbook    = null;
			Sheet           sheet       = null;
			File            archivoTxt  = null;
			PrintStream     output      = null;
			
			StringBuilder bufferErroresCenso = new StringBuilder("");
			int filasLeidas     = 0;
			int filasProcesadas = 0;
			int filasErrores    = 0;
			
			int nGrupos       = grupos.size();
			boolean[] bGrupos = new boolean[nGrupos];
			
			//instanciar
			try
			{
				File censo  = new File(new StringBuilder(rutaDocumentosTemporal).append("/censo_").append(miTimestamp).toString());
				input       = new FileInputStream(censo);
				workbook    = WorkbookFactory.create(input);
				sheet       = workbook.getSheetAt(0);
				nombreCenso = new StringBuilder("censo_").append(miTimestamp).append("_").append(nmpoliza).append(".txt").toString();
				archivoTxt  = new File(new StringBuilder(rutaDocumentosTemporal).append("/").append(nombreCenso).toString());
				output      = new PrintStream(archivoTxt);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al procesar censo #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
			
			if(resp.isExito()&&workbook.getNumberOfSheets()!=1)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Favor de revisar el n\u00famero de hojas del censo #").append(timestamp).toString());
				logger.error(resp.getRespuesta());
			}
			
			//crear pipes y ejecutar procedure
			if(resp.isExito())
			{
				int nSituac = 0;
				
				//pipes para censo solo
				if(esCensoSolo)
				{
					
					Map<Integer,String>  familias       = new LinkedHashMap<Integer,String>();
					Map<Integer,Boolean> estadoFamilias = new LinkedHashMap<Integer,Boolean>();
					Map<Integer,Integer> errorFamilia   = new LinkedHashMap<Integer,Integer>();
					Map<Integer,String>  titulares      = new LinkedHashMap<Integer,String>();
					
					//Iterate through each rows one by one
					logger.debug(
							new StringBuilder()
							.append("\n----------------------------------------------")
							.append("\n------ ").append(archivoTxt.getAbsolutePath())
							.toString()
							);
		            Iterator<Row> rowIterator = sheet.iterator();
		            int           fila        = 0;
		            int           nFamilia    = 0;
		            while (rowIterator.hasNext()&&resp.isExito()) 
		            {
		            	boolean       filaBuena      = true;
		            	StringBuilder bufferLinea    = new StringBuilder();
		            	StringBuilder bufferLineaStr = new StringBuilder();
		            	
		                Row  row     = rowIterator.next();
		                Date auxDate = null;
		                Cell auxCell = null;
		                
		                if(Utils.isRowEmpty(row))
		                {
		                	break;
		                }
		                
		                fila        = fila + 1;
		                nSituac     = nSituac + 1;
		                filasLeidas = filasLeidas + 1;
		                
		                String nombre = "";
		                
		                try
		                {	
			                auxCell=row.getCell(0);
			                logger.debug(
			                		new StringBuilder("NOMBRE: ")
			                		.append(
			                				auxCell!=null?
			                					new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                					:"|"
			                				)
			                		.toString()
			                		);
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                				:"|"
			                );
			                
			                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():""," ");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Nombre' (A) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(0)),"-"));
	                    }
		                
		                try
		                {
			                auxCell=row.getCell(1);
			                logger.debug(
			                		new StringBuilder("APELLIDO: ")
			                		.append(
			                				auxCell!=null?
			                					new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                					:"|"
			                		).toString()
			                );
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                				:"|"
			                );
			                
			                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():""," ");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido paterno' (B) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(1)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(2);
			                logger.debug(
			                		new StringBuilder("APELLIDO 2: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                						:"|"
			                		).toString()
			                		);
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                				:"|"
			                		);
			                
			                nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido materno' (C) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(2)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(3);
			                logger.debug(
			                		new StringBuilder("EDAD: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(String.format("%.0f",auxCell.getNumericCellValue())).append("|").toString()
			                						:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(String.format("%.0f",auxCell.getNumericCellValue())).append("|").toString()
			                				:"|"
			                		);
			                
			                if(row.getCell(4)!=null) {
				                auxDate=row.getCell(4).getDateCellValue();
				                if(auxDate!=null)
				                {
				                	Calendar cal = Calendar.getInstance();
				                	cal.setTime(auxDate);
				                	if(cal.get(Calendar.YEAR)>2100
				                			||cal.get(Calendar.YEAR)<1900
				                			)
				                	{
				                		throw new ApplicationException("El anio de la fecha no es valido");
				                	}
				                }
				                logger.debug(new StringBuilder("FENACIMI: ").append(
				                		auxDate!=null?new StringBuilder(renderFechas.format(auxDate)).append("|").toString():"|").toString());
				                bufferLinea.append(
				                	auxDate!=null?new StringBuilder(renderFechas.format(auxDate)).append("|").toString():"|");
			                } else {
			                	logger.debug(new StringBuilder("FENACIMI: ").append("|").toString());
			                	bufferLinea.append("|");
			                }
			                
			                if(
			                		(
			                				row.getCell(3) == null
			                				|| row.getCell(3).getNumericCellValue() == 0d
			                		)
			                		&&
			                		(
			                				row.getCell(4) == null
			                				|| row.getCell(4).getDateCellValue() == null
			                		)
			                )
			                {
			                	logger.error(Utils.join("No hay edad ni fecha de nacimiento para la fila ",fila));
			                	throw new ApplicationException(Utils.join("No hay edad ni fecha de nacimiento para la fila ",fila));
			                }
			                
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Edad' o 'Fecha de nacimiento' (D, E) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(3)),"-"));
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(4)),"-"));
	                    }
		                
		                try
	                	{
		                	String sexo = row.getCell(5).getStringCellValue();
		                	if(StringUtils.isBlank(sexo)
	                				||(!sexo.equals("H")&&!sexo.equals("M")))
	                		{
	                			throw new ApplicationException("El sexo no se reconoce [H,M]");
	                		}
			                logger.debug(
			                		new StringBuilder("SEXO: ")
			                		.append(sexo)
			                		.append("|")
			                		.toString());
			                bufferLinea.append(
			                		new StringBuilder(sexo).append("|").toString());
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (F) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(5)),"-"));
	                    }
		                
		                try
	                	{
		                	String parentesco = row.getCell(6).getStringCellValue();
		                	if(StringUtils.isBlank(parentesco)
	                				||(!parentesco.equals("T")
	                						&&!parentesco.equals("H")
	                						&&!parentesco.equals("P")
	                						&&!parentesco.equals("C")
	                						&&!parentesco.equals("D")
	                						)
	                						)
	                		{
	                			throw new ApplicationException("El parentesco no se reconoce [T,C,P,H,D]");
	                		}
			                logger.debug(Utils.log("PARENTESCO: ",parentesco,"|"));
			                bufferLinea.append(Utils.join(parentesco,"|"));
			                
			                if("T".equals(parentesco))
			                {
			                	nFamilia++;
			                	familias.put(nFamilia,"");
			                	estadoFamilias.put(nFamilia,true);
			                	titulares.put(nFamilia,nombre);
			                }
			                else if(fila==1)
			                {
			                	throw new ApplicationException("La primer fila debe ser titular");
			                }
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	if(fila==1)
		                	{
		                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (G) de la fila ",fila," la primer fila debe ser titular, se excluir\u00e1n las filas hasta el siguiente titular "));
		                	}
		                	else
		                	{
		                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (G) de la fila ",fila," "));
		                	}
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(6)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(7);
			                logger.debug(
			                		new StringBuilder("OCUPACION: ")
			                		.append(
			                				auxCell!=null?
			                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                				:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
			                				:"|");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ocupacion' (H) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(7)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(8);
			                logger.debug(
			                		new StringBuilder("EXTRAPRIMA OCUPACION: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                						:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                				:"|");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de ocupacion' (I) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(8)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(9);
			                logger.debug(
			                		new StringBuilder("PESO: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                						:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                				:"|");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (J) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(9)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(10);
			                logger.debug(
			                		new StringBuilder("ESTATURA: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                						:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                				:"|");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (K) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(10)),"-"));
	                    }
		                
		                try
	                	{
			                auxCell=row.getCell(11);
			                logger.debug(
			                		new StringBuilder("EXTRAPRIMA SOBREPESO: ")
			                		.append(
			                				auxCell!=null?
			                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                						:"|"
			                		).toString());
			                bufferLinea.append(
			                		auxCell!=null?
			                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
			                				:"|");
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de sobrepeso' (L) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(11)),"-"));
	                    }
		                
		                try
	                	{
			                logger.debug(
			                		new StringBuilder("GRUPO: ")
			                		.append(
			                				String.format("%.0f",row.getCell(12).getNumericCellValue()))
			                		.append("|")
			                		.toString());
			                bufferLinea.append(
			                		new StringBuilder(String.format("%.0f",row.getCell(12).getNumericCellValue())).append("|").toString());
			                
			                double cdgrupo=row.getCell(12).getNumericCellValue();
			                if(cdgrupo>nGrupos||cdgrupo<1d)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("No existe el grupo (M) de la fila ",fila," "));
			                }
			                else
			                {
			                	bGrupos[new Double(cdgrupo).intValue()-1]=true;
			                }
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (M) de la fila ",fila," "));
		                }
	                    finally
	                    {
	                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(12)),"-"));
	                    }
                        
                        try {
                            auxCell = row.getCell(13);
                            String valor = "";
                            if (auxCell != null && auxCell.getStringCellValue() !=null && StringUtils.isNotBlank(auxCell.getStringCellValue())) {
                                valor = auxCell.getStringCellValue();
                                if (!"S".equals(valor) && !"C".equals(valor)) {
                                    throw new ApplicationException(Utils.join("Solo se permite S o C en estado civil N", fila));
                                }
                            }
                            logger.debug(Utils.join("ESTADO CIVIL: ", valor, "|"));
                            bufferLinea.append(Utils.join(valor, "|"));
                        } catch (Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Estado civil' (N) de la fila ",fila," "));
                        } finally {
                            bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(13)),"-"));
                        }
                        
                        try {
                            auxCell = row.getCell(14);
                            String valor = "";
                            if (auxCell !=null) {
                                valor = String.format("%.0f",auxCell.getNumericCellValue());
                            }
                            logger.debug(Utils.join("NUMERO DE DEPENDIENTES: ", valor, "|"));
                            bufferLinea.append(Utils.join(valor, "|"));
                        } catch (Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Numero de dependientes' (O) de la fila ",fila," "));
                        } finally {
                            bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(14)),"-"));
                        }
		                
		                bufferLinea.append("\n");
		                logger.debug("** NUEVA_FILA **");
		                
		                if(filaBuena)
		                {
		                	familias.put(nFamilia,Utils.join(familias.get(nFamilia),bufferLinea.toString()));
		                	filasProcesadas = filasProcesadas + 1;
		                }
		                else
		                {
		                	filasErrores = filasErrores + 1;
		                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
		                	estadoFamilias.put(nFamilia,false);
		                	
		                	if(!errorFamilia.containsKey(nFamilia))
		                	{
		                		errorFamilia.put(nFamilia,fila);
		                	}
		                }
		            }
		            
		            try
		            {
			            logger.debug("\nFamilias: {}\nEstado familias: {}\nErrorFamilia: {}\nTitulares: {}"
			            		,familias,estadoFamilias,errorFamilia,titulares);
			            
			            for(Entry<Integer,Boolean>en:estadoFamilias.entrySet())
			            {
			            	int     n = en.getKey();
			            	boolean v = en.getValue();
			            	if(v)
			            	{
			            		output.print(familias.get(n));
			            	}
			            	else
			            	{
			            		bufferErroresCenso.append(Utils.join("La familia ",n," del titular '",titulares.get(n),"' no fue incluida por error en la fila ",errorFamilia.get(n),"\n"));
			            	}
			            }
			            
		                input.close();
		            }
		            catch(IOException ex)
		            {
		            	long timestamp = System.currentTimeMillis();
		            	resp.setExito(false);
		            	resp.setRespuesta(new StringBuilder("Error en el buffer #").append(timestamp).toString());
		            	resp.setRespuestaOculta(ex.getMessage());
		            	logger.error(resp.getRespuesta(),ex);
		            }
		            
		            if(resp.isExito())
		            {
			            Map<String,String> familiasMinimas = null;
			            try
			            {
			            	familiasMinimas = cotizacionDAO.obtenerParametrosCotizacion(
			            		ParametroCotizacion.NUMERO_FAMILIAS_COTI_COLECTIVO
			            		,cdramo
			            		,cdtipsit
			            		,null
			            		,null
			            		);
			            }
			            catch(Exception ex)
			            {
			            	resp.setExito(false);
			            	resp.setRespuesta(Utils.join("Error al recuperar n\u00FAmero m\u00EDnimo de titulares #",System.currentTimeMillis()));
			            	logger.error(resp.getRespuesta(),ex);
			            }
			            
			            //if(resp.isExito()&&!clasif.equals("1")) // Si el censo es distinto a Linea (menor a 50 asegurados), validamos el num. de titulares
			            if(resp.isExito())
			            {
				            int nMin = 0;
				            try
				            {
				            	nMin = Integer.parseInt(familiasMinimas.get("P1VALOR"));
				            }
				            catch(Exception ex)
				            {
				            	resp.setExito(false);
				            	resp.setRespuesta(Utils.join("Error al validar el n\u00FAmero de titulares #",System.currentTimeMillis()));
				            	logger.error(resp.getRespuesta(),ex);
				            }
				            
				            if(resp.isExito()&&nFamilia<nMin)
				            {
				            	resp.setExito(false);
				            	resp.setRespuesta(Utils.join("El n\u00FAmero de titulares debe ser por lo menos "
				            			,nMin
				            			,", se encontraron "
				            			,nFamilia
				            			," #",System.currentTimeMillis()));
				            	logger.error(resp.getRespuesta());
				            }
			            }
		            }
		            
		            output.close();
		            logger.debug(
		            		new StringBuilder()
		            		.append("\n------ ").append(archivoTxt.getAbsolutePath())
							.append("\n----------------------------------------------")
							.toString()
							);
				}
				//pipes para censo agrupado
				else
				{
					//Iterate through each rows one by one
					logger.debug(
							new StringBuilder()
							.append("\n----------------------------------------------")
							.append("\n------ ").append(archivoTxt.getAbsolutePath())
							.toString()
							);
		            Iterator<Row> rowIterator = sheet.iterator();
		            int fila = 0;
		            while (rowIterator.hasNext()&&resp.isExito()) 
		            {
		                Row row = rowIterator.next();
		                
		                if(Utils.isRowEmpty(row))
		                {
		                	break;
		                }
		                
		                boolean       filaBuena      = true;
		                StringBuilder bufferLinea    = new StringBuilder("");
		                StringBuilder bufferLineaStr = new StringBuilder("");
		                
		                fila        = fila + 1;
		                filasLeidas = filasLeidas + 1;
		                
		                try
	                	{
			                logger.debug(
			                		new StringBuilder("EDAD: ")
			                		.append(String.format("%.0f",row.getCell(0).getNumericCellValue())).append("|")
			                		.toString());
			                bufferLinea.append(
			                		new StringBuilder(String.format("%.0f",row.getCell(0).getNumericCellValue())).append("|").toString());
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Edad' (A) de la fila ",fila," "));
		                }
		                finally
		                {
		                	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(0)),"-"));
		                }
		                
		                try
	                	{
		                	String sexo = row.getCell(1).getStringCellValue();
		                	if(!"H".equals(sexo)
		                			&&!"M".equals(sexo))
		                	{
		                		throw new ApplicationException("Genero (sexo) incorrecto");
		                	}
			                logger.debug(
			                		new StringBuilder("SEXO: ").append(sexo).append("|").toString());
			                bufferLinea.append(
			                		new StringBuilder(sexo).append("|").toString());
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (B) de la fila ",fila," "));
		                }
		                finally
		                {
		                	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(1)),"-"));
		                }
		                
		                try
	                	{
			                logger.debug(
			                		new StringBuilder("CUANTOS: ")
			                		.append(String.format("%.0f",row.getCell(2).getNumericCellValue()))
			                		.append("|")
			                		.toString());
			                bufferLinea.append(
			                		new StringBuilder(String.format("%.0f",row.getCell(2).getNumericCellValue())).append("|").toString());
			                
			                nSituac = nSituac + (int)row.getCell(2).getNumericCellValue();
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Cantidad' (C) de la fila ",fila," "));
		                }
		                finally
		                {
		                	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(2)),"-"));
		                }
		                
		                try
	                	{
			                logger.debug(
			                		new StringBuilder("GRUPO: ")
			                		.append(String.format("%.0f",row.getCell(3).getNumericCellValue()))
			                		.append("|")
			                		.toString());
			                bufferLinea.append(
			                		new StringBuilder(String.format("%.0f",row.getCell(3).getNumericCellValue())).append("|").toString());
			                
			                double cdgrupo=row.getCell(3).getNumericCellValue();
			                if(cdgrupo>nGrupos||cdgrupo<1d)
			                {
			                	filaBuena = false;
			                	bufferErroresCenso.append(Utils.join("No existe el grupo (D) de la fila ",fila," "));
			                }
			                else
			                {
			                	bGrupos[new Double(cdgrupo).intValue()-1]=true;
			                }
		                }
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (D) de la fila ",fila," "));
		                }
		                finally
		                {
		                	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(3)),"-"));
		                }
		                
		                bufferLinea.append("\n");
		                logger.debug("** NUEVA_FILA **");
		                
		                if(filaBuena)
		                {
		                	output.print(bufferLinea.toString());
		                	filasProcesadas = filasProcesadas + 1;
		                }
		                else
		                {
		                	filasErrores = filasErrores + 1;
		                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
		                }
		            }
		            
		            try
		            {
		            	input.close();
		            }
		            catch(IOException ex)
		            {
		            	long timestamp = System.currentTimeMillis();
		            	resp.setExito(false);
		            	resp.setRespuesta(new StringBuilder("Error en el buffer #").append(timestamp).toString());
		            	resp.setRespuestaOculta(ex.getMessage());
		            	logger.error(resp.getRespuesta(),ex);
		            }
		            
		            output.close();
		            logger.debug(
		            		new StringBuilder()
		            		.append("\n------ ").append(archivoTxt.getAbsolutePath())
							.append("\n----------------------------------------------")
		            		.toString()
							);
				}
				
				if(resp.isExito())
				{
					resp.getSmap().put("erroresCenso"    , bufferErroresCenso.toString());
					resp.getSmap().put("filasLeidas"     , Integer.toString(filasLeidas));
					resp.getSmap().put("filasProcesadas" , Integer.toString(filasProcesadas));
					resp.getSmap().put("filasErrores"    , Integer.toString(filasErrores));
				}
				
				if(resp.isExito())
				{
					if(clasif.equals("1")&&nSituac>49)
					{
						long timestamp  = System.currentTimeMillis();
						//se condiciona por rol la asignaci�n de valores, para que guarde las coberturas sin necesidad de editar subgrupo, para RC y roles restringidos (EGS)
						if(!(RolSistema.AGENTE.getCdsisrol().equals(cdsisrol) || RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol) || RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol))
								&&TipoSituacion.RECUPERA_COLECTIVO.getCdtipsit().equals(cdtipsit)){
							resp.setExito(false);
							resp.setRespuesta(Utils.join("No se permiten mas de 49 asegurados #",timestamp));
						}
						//resp.setExito(false); //(EGS)
						//resp.setRespuesta(Utils.join("No se permiten mas de 49 asegurados #",timestamp)); //(EGS)
						resp.setRespuestaOculta(resp.getRespuesta());
						logger.error(resp.getRespuesta());
					}
					else if(!clasif.equals("1")&&nSituac<50)
					{
						long timestamp  = System.currentTimeMillis();
						resp.setExito(false);
						resp.setRespuesta(Utils.join("No se permiten menos de 50 asegurados #",timestamp));
						resp.setRespuestaOculta(resp.getRespuesta());
						logger.error(resp.getRespuesta());
					}
				}
				
				if(resp.isExito())
				{
					int cdgrupoVacio=0;
					for(int i=0;i<nGrupos;i++)
					{
						if(!bGrupos[i])
						{
							cdgrupoVacio=i+1;
							break;
						}
					}
					if(cdgrupoVacio>0)
					{	                	
	                	long timestamp = System.currentTimeMillis();
						resp.setExito(false);
						resp.setRespuesta(Utils.join("No hay asegurados para el grupo ",cdgrupoVacio," #"+timestamp));
						resp.setRespuestaOculta(resp.getRespuesta());
						logger.error(resp.getRespuesta());
					}
				}
				
				//enviar archivo
				if(resp.isExito())
				{
					resp.setExito(
						FTPSUtils.upload
						(
							dominioServerLayouts,
							userServerLayouts,
							passServerLayouts,
							archivoTxt.getAbsolutePath(),
							new StringBuilder(directorioServerLayouts).append("/").append(nombreCenso).toString()
						)
						&&FTPSUtils.upload
						(
							dominioServerLayouts2,
							userServerLayouts,
							passServerLayouts,
							archivoTxt.getAbsolutePath(),
							new StringBuilder(directorioServerLayouts).append("/").append(nombreCenso).toString()
						)
					);
					
					if(!resp.isExito())
					{
						long timestamp = System.currentTimeMillis();
						resp.setExito(false);
						resp.setRespuesta(
								new StringBuilder("Error al transferir archivo al servidor #").append(timestamp).toString());
						resp.setRespuestaOculta(resp.getRespuesta());
						logger.error(resp.getRespuesta());
					}
				}
			}
		}
		
		//pl censo
		if(resp.isExito()&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar))
		{
			String nombreProcedureCenso = null;
			String tipoCensoParam       = "AGRUPADO";
			if(esCensoSolo||sincenso)
			{
				tipoCensoParam = "INDIVIDUAL";
			}
			
			//obtener el PL
			try
			{
				Map<String,String>mapaAux=cotizacionDAO.obtenerParametrosCotizacion(
						ParametroCotizacion.PROCEDURE_CENSO
						,cdramo
						,cdtipsit
						,tipoCensoParam
						,null
						);
				nombreProcedureCenso = mapaAux.get("P1VALOR");
				if(StringUtils.isBlank(nombreProcedureCenso))
				{
					throw new ApplicationException("No se encontraron datos");
				}
			}
            catch(ApplicationException ax)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(
            			new StringBuilder("Error al obtener el nombre del procedimiento del censo: ")
            			.append(ax.getMessage())
            			.append(" #")
            			.append(timestamp)
            			.toString());
            	resp.setRespuestaOculta(ax.getMessage());
            	logger.error(resp.getRespuesta(),ax);
            }
            catch(Exception ex)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al obtener el nombre del procedimiento para el censo #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
			
			//ejecutar el PL
			if(resp.isExito())
			{
				//contar grupos
				int nGru=grupos.size();
				
				try
				{
					cotizacionDAO.procesarCenso(
							nombreProcedureCenso
							,cdusuari
							,cdsisrol
							,sincenso?"layout_censo"+nGru+".txt":nombreCenso
							,cdunieco
							,cdramo
							,"W"
							,nmpoliza
							,cdtipsit
							,cdagente
							,codpostalCli
							,cdedoCli
							,cdmuniciCli
							,"N"
							);
				}
	            catch(Exception ex)
	            {
	            	long timestamp = System.currentTimeMillis();
	            	resp.setExito(false);
	            	resp.setRespuesta(new StringBuilder("Error al ejecutar procedimiento del censo #").append(timestamp).toString());
	            	resp.setRespuestaOculta(ex.getMessage());
	            	logger.error(resp.getRespuesta(),ex);
	            }
			}
		}
		
		if((resp.isExito()&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||duplicar))
				&&StringUtils.isBlank(nombreCensoConfirmado)
		)
		{
			resp.getSmap().put("nombreCensoParaConfirmar" , nombreCenso);
			resp.setExito(true);
			resp.setRespuesta(Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]"));
			logger.info(resp.getRespuesta());
			return resp;
		}
		
		if(resp.isExito())
		{
			ManagerRespuestaSmapVO respInterna = procesoColectivoInterno(
					grupos
					,cdunieco
					,cdramo
					,nmpoliza
					,hayTramite
					,hayTramiteVacio
					,clasif
					,LINEA_EXTENDIDA
					,cdtipsit
					,cdpersonCli
					,nombreCli
					,rfcCli
					,dsdomiciCli
					,codpostalCli
					,cdedoCli
					,cdmuniciCli
					,nmnumeroCli
					,nmnumintCli
					,ntramite
					,ntramiteVacio
					,cdagente
					,cdusuari
					,cdelemen
					,false //reinsertarContratante
					,sincenso
					,censoAtrasado
					,cdperpag
					,resubirCenso
					,cdsisrol
					,complemento
					,asincrono
					,cdideper_
					,cdideext_
					,usuarioSesion
					,false
					,duplicar
					);
			
			resp.setExito(respInterna.isExito());
			resp.setRespuesta(respInterna.getRespuesta());
			resp.setRespuestaOculta(respInterna.getRespuestaOculta());
			if(resp.isExito())
			{
				resp.getSmap().putAll(respInterna.getSmap());
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ generarTramiteGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	private ManagerRespuestaSmapVO procesoColectivoInterno(
			List<Map<String,Object>>grupos
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,boolean hayTramite
			,boolean hayTramiteVacio
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdtipsit
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String ntramite
			,String ntramiteVacio
			,String cdagente
			,String cdusuari
			,String cdelemen
			,boolean reinsertaContratante
			,boolean sincenso
			,boolean censoAtrasado
			,String cdperpag
			,boolean resubirCenso
			,String cdsisrol
			,boolean complemento
			,boolean asincrono
			,String cdideper_
			,String cdideext_
			,UserVO usuarioSesion
			,boolean censoCompleto
			,boolean duplicar
			)
	{
		logger.info(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ procesoColectivoInterno @@@@@@"
				,"\n@@@@@@ grupos="               , grupos
				,"\n@@@@@@ cdunieco="             , cdunieco
				,"\n@@@@@@ cdramo="               , cdramo
				,"\n@@@@@@ nmpoliza="             , nmpoliza
				,"\n@@@@@@ hayTramite="           , hayTramite
				,"\n@@@@@@ hayTramiteVacio="      , hayTramiteVacio
				,"\n@@@@@@ clasif="               , clasif
				,"\n@@@@@@ LINEA_EXTENDIDA="      , LINEA_EXTENDIDA
				,"\n@@@@@@ cdtipsit="             , cdtipsit
				,"\n@@@@@@ cdpersonCli="          , cdpersonCli
				,"\n@@@@@@ nombreCli="            , nombreCli
				,"\n@@@@@@ rfcCli="               , rfcCli
				,"\n@@@@@@ dsdomiciCli="          , dsdomiciCli
				,"\n@@@@@@ codpostalCli="         , codpostalCli
				,"\n@@@@@@ cdedoCli="             , cdedoCli
				,"\n@@@@@@ cdmuniciCli="          , cdmuniciCli
				,"\n@@@@@@ nmnumeroCli="          , nmnumeroCli
				,"\n@@@@@@ nmnumintCli="          , nmnumintCli
				,"\n@@@@@@ ntramite="             , ntramite
				,"\n@@@@@@ ntramiteVacio="        , ntramiteVacio
				,"\n@@@@@@ cdagente="             , cdagente
				,"\n@@@@@@ cdusuari="             , cdusuari
				,"\n@@@@@@ cdelemen="             , cdelemen
				,"\n@@@@@@ reinsertaContratante=" , reinsertaContratante
				,"\n@@@@@@ sincenso="             , sincenso
				,"\n@@@@@@ censoAtrasado="        , censoAtrasado
				,"\n@@@@@@ cdperpag="             , cdperpag
				,"\n@@@@@@ resubirCenso="         , resubirCenso
				,"\n@@@@@@ cdsisrol="             , cdsisrol
				,"\n@@@@@@ complemento="          , complemento
				,"\n@@@@@@ asincrono="            , asincrono
				,"\n@@@@@@ cdideper_="            , cdideper_
				,"\n@@@@@@ cdideext_="            , cdideext_
				,"\n@@@@@@ censoCompleto="        , censoCompleto
				,"\n@@@@@@ duplicar="             , duplicar
				));
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		resp.setSmap(new HashMap<String,String>());
		
		final String LINEA = "1";
		
		Date fechaHoy = new Date();
		
		//mpolisit y tvalosit
		if(resp.isExito())
		{
			try
			{
				for(Map<String,Object>grupoIte:grupos)
				{
					String grupoIteCdgrupo = (String)grupoIte.get("letra");
					String grupoIteCdplan  = (String)grupoIte.get("cdplan");
					String grupoIteNombre  = (String)grupoIte.get("nombre");
					
					Map<String,String>grupoIteValoresSit = new HashMap<String,String>();
					for(Entry<String,Object>grupoIteAtribIte:grupoIte.entrySet())
					{
						String key = grupoIteAtribIte.getKey();
						if(StringUtils.isNotBlank(key)
								&&key.length()>="parametros.pv_".length()
								&&key.substring(0, "parametros.pv_".length()).equals("parametros.pv_")
								)
						{
							grupoIteValoresSit.put(key.substring("parametros.pv_".length(), key.length()),String.valueOf(grupoIteAtribIte.getValue()));
						}
					}
					
					cotizacionDAO.actualizaMpolisitTvalositGrupo(
							cdunieco
							,cdramo
							,"W"
							,nmpoliza
							,grupoIteCdgrupo
							,grupoIteNombre
							,grupoIteCdplan
							,grupoIteValoresSit
							);
					
					if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
					{
						cotizacionDAO.actualizaValoresDefectoSituacion(cdunieco, cdramo, "W", nmpoliza, "0");
					}
				}
			}
            catch(Exception ex)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al actualizar grupos #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
		}
		
		
		if(resp.isExito() && StringUtils.isNotBlank(codpostalCli)){
			try {
				cotizacionDAO.actualizaDomicilioAseguradosColectivo(cdunieco, cdramo, "W", nmpoliza, "0", codpostalCli, cdedoCli, cdmuniciCli);
			} catch (Exception ex) {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al actualizar tvalopol domicilio #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
		}
		
		if(resp.isExito()
				&&(!hayTramite||hayTramiteVacio||duplicar)
				&&
				(
					RolSistema.AGENTE.getCdsisrol().equals(cdsisrol)
					||RolSistema.EJECUTIVO_INTERNO.getCdsisrol().equals(cdsisrol)
					||RolSistema.MESA_DE_CONTROL.getCdsisrol().equals(cdsisrol)
					||RolSistema.SUSCRIPTOR.getCdsisrol().equals(cdsisrol)
				)
		)
		{
			asincrono = true;
		}
		
		//sigsvdef
		if(resp.isExito()&&(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||complemento||censoCompleto||duplicar)&&asincrono==false)
		{
			try
			{
				/*
				cotizacionDAO.valoresPorDefecto(
						cdunieco
						,cdramo
						,"W"
						,nmpoliza
						,"0"    //nmsituac
						,"0"    //nmsuplem
						,"TODO" //cdgarant
						,"1"    //cdtipsup
						);
				*/
				cotizacionDAO.ejecutaValoresDefectoConcurrente(
						cdunieco
						,cdramo
						,"W" //estado
						,nmpoliza
						,"0" //nmsuplem
						,"0" //nmsituac
						,"1" //tipotari
						,cdperpag
						);
				
				try
	            {
	            	cotizacionDAO.grabarEvento(new StringBuilder("\nCotizacion grupo")
	            	    ,Constantes.MODULO_COTIZACION //cdmodulo
	            	    ,Constantes.EVENTO_COTIZAR    //cdevento
	            	    ,fechaHoy   //fecha
	            	    ,cdusuari
	            	    ,((UserVO)ActionContext.getContext().getSession().get("USUARIO")).getRolActivo().getClave()
	            	    ,ntramite
	            	    ,cdunieco
	            	    ,cdramo
	            	    ,"W"
	            	    ,nmpoliza
	            	    ,nmpoliza
	            	    ,cdagente
	            	    ,null
	            	    ,null, null);
	            }
	            catch(Exception ex)
	            {
	            	logger.error("Error al grabar evento, sin impacto",ex);
	            }
			}
            catch(Exception ex)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al insertar valores por defecto #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
		}
		
		//tvalogar
		if(resp.isExito()&&(
				(!clasif.equals(LINEA))
				||LINEA_EXTENDIDA.equals("N")
				)
				&&asincrono==false
				)
		{
			try
			{
				for(Map<String,Object>grupoIte:grupos)
				{
					String grupoIteCdgrupo                     = (String)grupoIte.get("letra");
					List<Map<String,String>>grupoIteCoberturas = (List<Map<String,String>>)grupoIte.get("tvalogars");
					for(Map<String,String>grupoIteCoberturaIte:grupoIteCoberturas)
					{
						String grupoIteCoberturaIteCdgarant = grupoIteCoberturaIte.get("cdgarant");
						boolean grupoIteCoberturaIteAmparada = StringUtils.isNotBlank(grupoIteCoberturaIte.get("amparada"))
								&&grupoIteCoberturaIte.get("amparada").equalsIgnoreCase("S");
						if(grupoIteCoberturaIteAmparada)
						{
							cotizacionDAO.movimientoMpoligarGrupo(
									cdunieco
									,cdramo
									,"W"      //estado
									,nmpoliza
									,"0"      //nmsuplem
									,cdtipsit
									,grupoIteCdgrupo
									,grupoIteCoberturaIteCdgarant
									,"V"      //status
									,"001"    //cdmoneda
									,Constantes.INSERT_MODE, null
									);
							boolean grupoIteCoberturaIteTieneAtrib          = false;
							Map<String,String>grupoIteCoberturaIteTvalogars = new HashMap<String,String>();
							for(Entry<String,String>grupoIteCoberturaIteAtribIte:grupoIteCoberturaIte.entrySet())
							{
								String grupoIteCoberturaIteAtribIteKey=grupoIteCoberturaIteAtribIte.getKey();
								if(StringUtils.isNotBlank(grupoIteCoberturaIteAtribIteKey)
										&&grupoIteCoberturaIteAtribIteKey.length()>"parametros.pv_otvalor".length()
										&&grupoIteCoberturaIteAtribIteKey.substring(0, "parametros.pv_otvalor".length()).equalsIgnoreCase("parametros.pv_otvalor")
										&&grupoIteCoberturaIteAtribIte.getValue()!=null
										)
								{
									grupoIteCoberturaIteTieneAtrib=true;
									grupoIteCoberturaIteTvalogars.put(
											grupoIteCoberturaIteAtribIteKey.substring("parametros.pv_".length()
													,grupoIteCoberturaIteAtribIteKey.length()),String.valueOf(grupoIteCoberturaIteAtribIte.getValue()));
								}
							}
							if(grupoIteCoberturaIteTieneAtrib)
							{
								cotizacionDAO.movimientoTvalogarGrupoCompleto(
										cdunieco
										,cdramo
										,"W"      //estado
										,nmpoliza
										,"0"      //nmsuplem
										,cdtipsit
										,grupoIteCdgrupo
										,grupoIteCoberturaIteCdgarant
										,"V"      //status
										,grupoIteCoberturaIteTvalogars
										);
							}
						}
					}
				}
			}
            catch(Exception ex)
            {
            	long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al guardar atributos de coberturas #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
            }
		}
		
		// Se inserta el maestro y detalle de los grupos:
		try {
		    
		    try {
			    cotizacionDAO.eliminarGrupos(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit);
			} catch (Exception e) {
				logger.warn("No se eliminaron los grupos de la poliza: {}", e);
			}
			for(Map<String,Object> grupoIte : grupos) {
				logger.debug("grupoIte=={}", grupoIte);
				// Guardar el maestro de grupos mpoligrup:
				cotizacionDAO.insertaMpoligrup(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("nombre"), (String)grupoIte.get("cdplan"),(String)grupoIte.get("dsplanl"), null, "0", "0", Constantes.NO, Constantes.NO, Constantes.NO);
				// Guardar el detalle de grupos mgrupogar:
				cotizacionDAO.insertaMgrupogar(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("cdplan"), "0");
			}
		} catch(Exception ex) {
			long timestamp = System.currentTimeMillis();
        	resp.setExito(false);
        	resp.setRespuesta(new StringBuilder("Error al guardar grupos de la poliza #").append(timestamp).toString());
        	resp.setRespuestaOculta(ex.getMessage());
        	logger.error(resp.getRespuesta(),ex);
		}
		
		//tramite
		if(resp.isExito()&&(!hayTramite||hayTramiteVacio||censoAtrasado||duplicar))
		{
			try
			{
				if(!hayTramite&&!hayTramiteVacio)//es agente
				{
					Map<String,String>otvalorMesaControl=new HashMap<String,String>();
					otvalorMesaControl.put("otvalor01" , clasif);
					otvalorMesaControl.put("otvalor02" , sincenso ? "S" : "N");
					
					Map<String,String> datosFlujo = consultasDAO.recuperarDatosFlujoEmision(cdramo,"C");
					
					String estatus = EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo();
					try {
            			estatus = flujoMesaControlDAO.recuperarEstatusDefectoRol(cdsisrol);
            		} catch (Exception ex) {
            			logger.warn("Error sin impacto al querer recuperar estatus por defecto de un rol", ex);
            		}
					
					ntramite = mesaControlDAO.movimientoMesaControl(
							cdunieco
							,cdramo
							,"W"       //estado
							,"0"
							,"0"       //nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.POLIZA_NUEVA.getCdtiptra()
							,fechaHoy
							,cdagente
							,null      //referencia
							,null      //nombre
							,fechaHoy
							,estatus
							,"Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente"
							,nmpoliza
							,cdtipsit
							,cdusuari
							,cdsisrol
							,null //swimpres
							,datosFlujo.get("cdtipflu")
	            			,datosFlujo.get("cdflujomc")
							,otvalorMesaControl
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString(), null, null, null, false, null
							);
					resp.getSmap().put("ntramite" , ntramite);
					
					RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        ntramite,
					        estatus,
					        "Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente",
					        null,  // cdrazrecha
					        cdusuari,
					        cdsisrol,
					        true,  // permisoAgente
					        false, //porEscalamiento
					        fechaHoy,
					        false  // sinGrabarDetalle
					        );
					
					resp.setRespuesta(despacho.getMessage());
					
					/* JTEZVA 7 sep 2016
					 * el tramite no se turna por lo que no lleva doble detalle
					mesaControlDAO.movimientoDetalleTramite(
							ntramite
							,new Date()
							,null       //cdclausu
							,"Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente"
							,cdusuari
							,null       //cdmotivo
							,cdsisrol
							,"S"
							,null
							,null
							,estatus
							,false
							);
					*/
					
					/* ya no turna, solo lo crea y ya JTEZVA 2016 09 02
					 * resp.getSmap().put("nombreUsuarioDestino"
							,mesaControlDAO.turnaPorCargaTrabajo(ntramite,"COTIZADOR",EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo())
					);*/
					
					try
		            {
						cotizacionDAO.grabarEvento(new StringBuilder("\nNuevo tramite grupo")
		            	    ,Constantes.MODULO_EMISION    //cdmodulo
		            	    ,Constantes.EVENTO_GENERAR_TRAMITE_GRUPO //cdevento
		            	    ,new Date()   //fecha
		            	    ,cdusuari
		            	    ,((UserVO)ActionContext.getContext().getSession().get("USUARIO")).getRolActivo().getClave()
		            	    ,ntramite
		            	    ,cdunieco
		            	    ,cdramo
		            	    ,"W"
		            	    ,nmpoliza
		            	    ,nmpoliza
		            	    ,cdagente
		            	    ,null
		            	    ,null, null);
		            }
		            catch(Exception ex)
		            {
		            	logger.error("Error al grabar evento, sin impacto",ex);
		            }
				}
				else
				{
					String ntramiteActualiza = ntramite;
					if(hayTramiteVacio)
					{
						ntramiteActualiza = ntramiteVacio;
					}
					
					mesaControlDAO.actualizarNmsoliciTramite(ntramiteActualiza, nmpoliza);
					
					Map<String,String>valoresTramite=new HashMap<String,String>();
					valoresTramite.put("otvalor01" , clasif);
					valoresTramite.put("otvalor02" , sincenso ? "S" : "N");
					mesaControlDAO.actualizaValoresTramite(
							ntramiteActualiza
							,null    //cdramo
							,null    //cdtipsit
							,null    //cdsucadm
							,null    //cdsucdoc
							,null    //comments
							,valoresTramite);
					
					if (duplicar) {
					    RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
					            cdusuari,
					            cdsisrol,
					            ntramiteActualiza,
					            EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo(),
					            Utils.join("Se duplica la cotizaci\u00f3n para generar la nueva solicitud ", nmpoliza),
					            null,  // cdrazrecha,
					            null,  // cdusuariDes,
					            null,  // cdsisrolDes,
					            true,  // permisoAgente,
					            false, // porEscalamiento,
					            fechaHoy,
					            false  // sinGrabarDetalle
					            );
					    resp.setRespuesta(despacho.getMessage());
					}
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al guardar tramite #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//mpoliage
		if(resp.isExito()&&(!hayTramite||hayTramiteVacio||duplicar))
		{
			try
			{
				Map<String,String>datosAgenteExterno=cotizacionDAO.obtenerDatosAgente(cdagente,cdramo);
    			String nmcuadro=datosAgenteExterno.get("NMCUADRO");
    			
    			String paramNtramite = ntramite;
    			if(hayTramiteVacio)
    			{
    				paramNtramite = ntramiteVacio;
    			}
				
    			cotizacionDAO.movimientoMpoliage(
    					cdunieco
    					,cdramo
    					,"W"      //estado
    					,nmpoliza
    					,cdagente
    					,"0"      //nmsuplem
    					,"V"      //status
    					,"1"      //cdtipoag
    					,"0"      //porredau
    					,nmcuadro
    					,null     //cdsucurs
    					,Constantes.INSERT_MODE
    					,paramNtramite
    					,"100"    //porparti
    					);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al guardar datos del agente #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//sigsvalipol
		if(resp.isExito()&&asincrono==false)
		{
			try
			{
				logger.debug("@@@@@@@@@@lanzatarAsincrono1 ",lanzatarAsincrono);
				if(lanzatarAsincrono)
				{
					new EjecutaTarificacionConcurrente(
							cdunieco
							,cdramo
							,"W" //estado
							,nmpoliza
							,"0" //nmsuplem
							,"0" //nmsituac
							,"1" //tipotari
							,cdperpag
							,cdusuari
							,cdsisrol
							).start();
				}
				else
				{
					cotizacionDAO.eliminarMpolirec(
							cdunieco
			        		,cdramo
			        		,"W"
			        		,nmpoliza
			        		,"0"
							);
					
					cotizacionDAO.insertaMorbilidad(cdunieco,cdramo,"W",nmpoliza,"0");
					
					cotizacionDAO.ejecutaTarificacionConcurrente(
							cdunieco
							,cdramo
							,"W" //estado
							,nmpoliza
							,"0" //nmsuplem
							,"0" //nmsituac
							,"1" //tipotari
							,cdperpag
							);
					logger.debug("##############GRABANDO EVENTO DE COTIZACION 1##############");
					cotizacionDAO.grabarEvento(new StringBuilder(),
					        Constantes.MODULO_COTIZACION,
					        Constantes.EVENTO_COTIZAR,
					        new Date(), cdusuari, cdsisrol, ntramite, cdunieco, cdramo, "W", nmpoliza, nmpoliza, cdagente, "", "", null);
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
            	resp.setExito(false);
            	resp.setRespuesta(new StringBuilder("Error al tarificar #").append(timestamp).toString());
            	resp.setRespuestaOculta(ex.getMessage());
            	logger.error(resp.getRespuesta(),ex);
			}
		}
		
		if(resp.isExito()&&asincrono==true)
		{
			procesoColectivoAsincrono2(
					hayTramite
					,hayTramiteVacio
					,censoAtrasado
					,complemento
					,cdunieco
					,cdramo
					,nmpoliza
					,cdperpag
					,clasif
					,LINEA
					,LINEA_EXTENDIDA
					,grupos
					,cdtipsit
					,cdusuari
					,cdsisrol
					,duplicar
					);
		}
		
		if(resp.isExito())
		{
		    String mensaje = resp.getRespuesta();
			resp.setRespuesta(Utils.join("Se gener\u00f3 el tr\u00e1mite ", ntramite,
			        StringUtils.isBlank(mensaje)
			            ? ""
			            : (Utils.join(". ", mensaje))
			));
			resp.setRespuestaOculta("Todo OK");
		}
		

		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ procesoColectivoInterno @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	public ManagerRespuestaSlistVO obtenerTiposSituacion()
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ obtenerTiposSituacion @@@@@@")
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			resp.setSlist(cotizacionDAO.obtenerTiposSituacion());
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener tipos de situacion #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ obtenerTiposSituacion @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public List<Map<String,String>> cargarAseguradosExtraprimas2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			)throws Exception
	{
		logger.info(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarAseguradosExtraprimas2 @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ cdgrupo="  , cdgrupo
				));
		
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		
		String paso = "Recuperando asegurados extraprima";
		
		//cargar situaciones grupo
		try
		{
			List<Map<String,String>>situaciones=cotizacionDAO.cargarSituacionesGrupo(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdgrupo);
			
			for(Map<String,String>situacion:situaciones)
		    {
		    	String tpl = null;
		    	if(StringUtils.isBlank(situacion.get("titular")))
		    	{
		    		tpl = "Asegurados";
		    	}
		    	else
		    	{
		    		tpl = Utils.join(
		    				"Familia (" , situacion.get("familia") , ") de " , situacion.get("titular")
		    				);
		    	}
		    	situacion.put("agrupador",
		    			Utils.join(StringUtils.leftPad(situacion.get("familia"),3,"0") , "_" , tpl)
		    			);
		    	
		    	Map<String,String>editada=new HashMap<String,String>();
		    	for(Entry<String,String>en:situacion.entrySet())
		    	{
		    		String key = en.getKey();
		    		if(StringUtils.isNotBlank(key)
		    				&&key.length()>"otvalor".length()
		    				&&key.substring(0, "otvalor".length()).equals("otvalor")
		    				)
		    		{
		    			editada.put(new StringBuilder("parametros.pv_").append(key).toString(),en.getValue());
		    		}
		    		else
		    		{
		    			editada.put(key,en.getValue());
		    		}
		    	}
		    	lista.add(editada);
		    }
		}
		catch(ApplicationException ax)
		{
			Utils.generaExcepcion(ax, paso);
		}
		
		logger.info(Utils.log(
				 "\n@@@@@@ lista=" , lista
				,"\n@@@@@@ cargarAseguradosExtraprimas2 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return lista;
	}
	
	public ManagerRespuestaVoidVO guardarValoresSituaciones(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>> situaciones,
			String cdtipsit,
			Boolean guardarExt)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ guardarValoresSituaciones @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=").append(cdramo)
				.append("\n@@@@@@ estado=").append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ situaciones=").append(situaciones)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ guardarExt=").append(guardarExt)
				.toString()
				);
		
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		List<Map<String,String>> listaCobExt = null;
		
		//actualizar situaciones
		try{
			//listaCobExt = ...
			String paso = Utils.join("antes de entrar a recuperaCoberturasExtraprima");
		    listaCobExt = consultasDAO.recuperaCoberturasExtraprima(cdramo,cdtipsit);
		    cotizacionDAO.actualizaValoresSituacion(cdunieco, cdramo, estado, nmpoliza, nmsuplem, situaciones);
		    paso = Utils.join("despues de pasar a recuperaCoberturasExtraprima");
		    for(Map<String,String>situacion:situaciones){
		    	for(Map<String,String> coberturaExtraprima : listaCobExt){
					String cdgarant = coberturaExtraprima.get("CDGARANT");					
					paso = Utils.join("Ejecutando valores por defecto para la cobertura ",cdgarant);
					logger.debug(paso);					
					cotizacionDAO.valoresPorDefecto(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,situacion.get("nmsituac")
							,nmsuplem
							,cdgarant
							,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
							);
		    	}
		    }
			resp.setRespuesta("Se guardaron todos los datos");
		}
		catch(Exception dx)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al guardar situacion #").append(timestamp).toString());
			resp.setRespuestaOculta(dx.getMessage());
			logger.error(resp.getRespuesta(),dx);
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ guardarValoresSituaciones @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	public ManagerRespuestaVoidVO guardarValoresSituacionesTitular(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdtipsit,
			String valor,
			String cdgrupo)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ guardarValoresSituacionesTitular @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=").append(cdramo)
				.append("\n@@@@@@ estado=").append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ valor=").append(valor)
				.append("\n@@@@@@ cdgrupo=").append(cdgrupo)
				.toString()
				);
		
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);		
		//actualizar situaciones
		try{
			//listaCobExt = ...
			String paso = Utils.join("antes de entrar a actualizaValoresSituacionTitulares");
		    List<Map<String, String>> situaciones = cotizacionDAO.actualizaValoresSituacionTitulares(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsit, valor, cdgrupo);
		    paso = Utils.join("despues de pasar a actualizaValoresSituacionTitulares");
			resp.setRespuesta("Se guardaron todos los datos");
		}
		catch(Exception dx)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al guardar situacion #").append(timestamp).toString());
			resp.setRespuestaOculta(dx.getMessage());
			logger.error(resp.getRespuesta(),dx);
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ guardarValoresSituacionesTitular @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO subirCensoCompleto(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,String rutaDocsTemp
			,String censoTimestamp
			,String dominioServerLayout
			,String usuarioServerLayout
			,String passwordServerLayout
			,String direcServerLayout
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,List<Map<String,Object>>grupos
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String ntramite
			,String ntramiteVacio
			,String cdelemen
			,String nombreCensoConfirmado
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ subirCensoCompleto @@@@@@")
				.append("\n@@@@@@ cdunieco=")            .append(cdunieco)
				.append("\n@@@@@@ cdramo=")              .append(cdramo)
				.append("\n@@@@@@ nmpoliza=")            .append(nmpoliza)
				.append("\n@@@@@@ feini=")               .append(feini)
				.append("\n@@@@@@ fefin=")               .append(fefin)
				.append("\n@@@@@@ cdperpag=")            .append(cdperpag)
				.append("\n@@@@@@ pcpgocte=")            .append(pcpgocte)
				.append("\n@@@@@@ rutaDocsTemp=")        .append(rutaDocsTemp)
				.append("\n@@@@@@ censoTimestamp=")      .append(censoTimestamp)
				.append("\n@@@@@@ dominioServerLayout=") .append(dominioServerLayout)
				.append("\n@@@@@@ usuarioServerLayout=") .append(usuarioServerLayout)
				.append("\n@@@@@@ passwordServerLayout=").append(passwordServerLayout)
				.append("\n@@@@@@ direcServerLayout=")   .append(direcServerLayout)
				.append("\n@@@@@@ cdtipsit=")            .append(cdtipsit)
				.append("\n@@@@@@ cdusuari=")            .append(cdusuari)
				.append("\n@@@@@@ cdsisrol=")            .append(cdsisrol)
				.append("\n@@@@@@ cdagente=")            .append(cdagente)
				.append("\n@@@@@@ codpostalCli=")        .append(codpostalCli)
				.append("\n@@@@@@ cdedoCli=")            .append(cdedoCli)
				.append("\n@@@@@@ cdmuniciCli=")         .append(cdmuniciCli)
				.append("\n@@@@@@ grupos=")              .append(grupos)
				.append("\n@@@@@@ clasif=")              .append(clasif)
				.append("\n@@@@@@ LINEA_EXTENDIDA=")     .append(LINEA_EXTENDIDA)
				.append("\n@@@@@@ cdpersonCli=")         .append(cdpersonCli)
				.append("\n@@@@@@ nombreCli=")           .append(nombreCli)
				.append("\n@@@@@@ dsdomiciCli=")         .append(dsdomiciCli)
				.append("\n@@@@@@ nmnumeroCli=")         .append(nmnumeroCli)
				.append("\n@@@@@@ nmnumintCli=")         .append(nmnumintCli)
				.append("\n@@@@@@ ntramite=")            .append(ntramite)
				.append("\n@@@@@@ ntramiteVacio=")       .append(ntramiteVacio)
				.append("\n@@@@@@ cdelemen=")            .append(cdelemen)
				.append("\n@@@@@@ nombreCensoConfirmado=").append(nombreCensoConfirmado)
				.append("\n@@@@@@ cdideper_=")            .append(cdideper_)
				.append("\n@@@@@@ cdideext_=")            .append(cdideext_)
				.append("\n@@@@@@ nmpolant=")             .append(nmpolant)
				.append("\n@@@@@@ nmrenova=")             .append(nmrenova)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		resp.setSmap(new HashMap<String,String>());
		
		Date fechaHoy = new Date();
		
		//mpolizas
		try
		{
			cotizacionDAO.movimientoPoliza(
					cdunieco
					,cdramo
					,"W"      //estado
					,nmpoliza
					,"0"      //nmsuplem
					,"V"      //status
					,"0"      //swestado
					,null     //nmsolici
		            ,null     //feautori
		            ,null     //cdmotanu
		            ,null     //feanulac
		            ,"N"      //swautori
		            ,"001"    //cdmoneda
		            ,null     //feinisus
		            ,null     //fefinsus
		            ,"R"      //ottempot
		            ,feini
		            ,"12:00"  //hhefecto
		            ,fefin
		            ,null     //fevencim
		            ,StringUtils.isBlank(nmrenova) ? "0" : nmrenova
		            ,null     //ferecibo
		            ,null     //feultsin
		            ,"0"      //nmnumsin
		            ,"N"      //cdtipcoa
		            ,"A"      //swtarifi
		            ,null     //swabrido
		            ,renderFechas.format(fechaHoy) //feemisio
		            ,cdperpag
		            ,null     //nmpoliex
		            ,"P1"     //nmcuadro
		            ,"100"    //porredau
		            ,"S"      //swconsol
		            ,nmpolant
		            ,null     //nmpolnva
		            ,renderFechas.format(fechaHoy) //fesolici
		            ,null     //cdramant
		            ,null     //cdmejred
		            ,null     //nmpoldoc
		            ,null     //nmpoliza2
		            ,null     //nmrenove
		            ,null     //nmsuplee
		            ,null     //ttipcamc
		            ,null     //ttipcamv
		            ,null     //swpatent
		            ,pcpgocte
		            ,"F"      //tipoflot
		            ,null     //agrupador
		            ,"U"      //accion
					);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al guardar poliza #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		String  nombreCenso   = null;
		boolean pagoRepartido = false;
		
		int     filasError    = 0;
		
		if(resp.isExito())
		{
			try
			{
				pagoRepartido = consultasDAO.validaPagoPolizaRepartido(cdunieco, cdramo, "W", nmpoliza);
			}
			catch(Exception ex)
			{
				resp.setRespuesta(Utils.join("Error al recuperar reparto de pago #",System.currentTimeMillis()));
				resp.setRespuestaOculta(ex.getMessage());
				resp.setExito(false);
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		boolean pideNumCliemte = false;
		if(resp.isExito())
		{
			try
			{
				pideNumCliemte = consultasDAO.validaClientePideNumeroEmpleado(cdunieco,cdramo,"W",nmpoliza);
			}
			catch(Exception ex)
			{
				resp.setRespuesta(Utils.join("Error al recuperar parametrizacion de numero de empleado por cliente"));
				resp.setRespuestaOculta(ex.getMessage());
				resp.setExito(false);
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//crear pipes
		if(resp.isExito()&&StringUtils.isBlank(nombreCensoConfirmado))
		{
			
			FileInputStream input       = null;
			Workbook        workbook    = null;
			Sheet           sheet       = null;
			File            archivoTxt  = null;
			PrintStream     output      = null;
			
			try
			{	
				File censo       = new File(new StringBuilder(rutaDocsTemp).append("/censo_").append(censoTimestamp).toString());
				input            = new FileInputStream(censo);
				workbook         = WorkbookFactory.create(input);
				sheet            = workbook.getSheetAt(0);
				Long inTimestamp = System.currentTimeMillis();
				nombreCenso      = "censo_"+inTimestamp+"_"+nmpoliza+".txt";
				archivoTxt       = new File(new StringBuilder(rutaDocsTemp).append("/").append(nombreCenso).toString());
				output           = new PrintStream(archivoTxt);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al procesar censo #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
			
			if(resp.isExito()&&workbook.getNumberOfSheets()!=1)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Favor de revisar el n\u00famero de hojas del censo #").append(timestamp).toString());
				logger.error(resp.getRespuesta());
			}
			
			if(resp.isExito())
			{
				logger.debug(
						new StringBuilder()
						.append("\n-------------------------------------")
						.append("\n------ PROCESAR CENSO COMPLETO ------")
						.append("\n------ NOMBRE CENSO=").append(archivoTxt.getAbsolutePath())
						.toString()
						);
				Iterator<Row> rowIterator        = sheet.iterator();
	            int           fila               = 0;
	            int           nFamilia           = 0;
	            StringBuilder bufferErroresCenso = new StringBuilder();
	            int           filasLeidas        = 0;
	            int           filasProcesadas    = 0;
	            filasError         = 0;
	            
	            Map<Integer,List<Map<String,String>>> listaFamilias = new HashMap<Integer, List<Map<String,String>>>(); 
	            List<Map<String,String>>             filasFamilia = new ArrayList<Map<String,String>>(); 
	            
	            Map<Integer,String>  familias       = new LinkedHashMap<Integer,String>();
				Map<Integer,Boolean> estadoFamilias = new LinkedHashMap<Integer,Boolean>();
				Map<Integer,Integer> errorFamilia   = new LinkedHashMap<Integer,Integer>();
				Map<Integer,String>  titulares      = new LinkedHashMap<Integer,String>();
	            
				boolean[] gruposValidos = new boolean[grupos.size()];
				
				HashMap<String,String> codigosPostales = null;
				
				try
				{
					codigosPostales = cotizacionDAO.obtieneCodigosPostalesProductos();
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al validar Codigos Postales del Producto, sin datos. #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
				
				if(codigosPostales == null){
					codigosPostales = new HashMap<String, String>();
				}
				
	            while (rowIterator.hasNext()&&resp.isExito()) 
	            {
	                Row           row            = rowIterator.next();
	                Date          auxDate        = null;
	                Cell          auxCell        = null;
	                StringBuilder bufferLinea    = new StringBuilder();
	                StringBuilder bufferLineaStr = new StringBuilder();
	                boolean       filaBuena      = true;
	                
	                String nombre1         =  null;
	                String nombre2         =  null;
	                String apellidoP       =  null;
	                String apellidoM       =  null;
	                String fechaNac        =  null;
	                String cveAsegurado    =  null;
	                String fecanti         =  null;
	                String feingreso       =  null;
	                
	                if(Utils.isRowEmpty(row))
	                {
	                	break;
	                }
	                
	                fila        = fila + 1;
	                filasLeidas = filasLeidas + 1;
	                
	                String parentesco = null;
	                String dependiente = null;
	                String nombre     = "";
	                double cdgrupo    = -1d;
	              //GRUPO
	                try
                	{
	                	cdgrupo = row.getCell(0).getNumericCellValue();
		                logger.debug(
		                		new StringBuilder("GRUPO: ")
		                        .append(
		                        		String.format("%.0f",row.getCell(0).getNumericCellValue())
		                        ).append("|").toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(
		                		    String.format("%.0f",row.getCell(0).getNumericCellValue())
		                		    ).append("|").toString()
		                		);
		                
		                if(cdgrupo>grupos.size())
		                {
		                	bufferErroresCenso.append(Utils.join("Grupo no permitido: ",cdgrupo," (grupos: ",grupos.size(),") en la fila ",fila," "));
		                	throw new ApplicationException("El grupo de excel no existe");
		                }
                	}
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (A) de la fila ",fila," "));
	                    try{
                            cdgrupo = Double.parseDouble(row.getCell(0).getStringCellValue());
                            logger.debug("GRUPO: "+(row.getCell(0).getStringCellValue()+"|"));
                            bufferLinea.append(row.getCell(0).getStringCellValue()+"|");
                            if(cdgrupo>grupos.size())
                            {
                                bufferErroresCenso.append(Utils.join("Grupo no permitido: ",cdgrupo," (grupos: ",grupos.size(),") en la fila ",fila," "));
                                throw new ApplicationException("El grupo de excel no existe");
                            }
                        }catch (Exception e) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Grupo' (A) de la fila ",fila," "));
                        }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(0)),"-"));
	                }
	                
	              //CERTIFICADO
	                try {
		                auxCell=row.getCell(1);
		                dependiente = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"0|";
		                logger.debug("CERTIFICADO: "+dependiente);
		                bufferLinea.append(dependiente);
	                } catch(Exception ex) {
	                	logger.error("error al leer dependiente como numero, se intentara como string:",ex);
	                	try {
	                		dependiente = row.getCell(1).getStringCellValue()+"|";
	                		if("|".equals(dependiente)) {
	                			dependiente = "0|";
	                		}
	                		logger.debug("CERTIFICADO: "+dependiente);
			                bufferLinea.append(dependiente);
	                	} catch(Exception ex2) {
		                	logger.error("error dependiente:",ex2);
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Certificado' (B) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(1)),"-"));
	                }
	                
	              //PARENTESCO
	                try
                	{
	                	parentesco = row.getCell(2).getStringCellValue();
	                	if(StringUtils.isBlank(parentesco)
                				||(!parentesco.equals("T")
                						&&!parentesco.equals("H")
                						&&!parentesco.equals("P")
                						&&!parentesco.equals("C")
                						&&!parentesco.equals("D")
                						)
                						)
                		{
                			throw new ApplicationException("El parentesco no se reconoce [T,C,P,H,D]");
                		}
		                logger.debug(
		                		new StringBuilder("PARENTESCO: ")
		                		.append(parentesco)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(parentesco)
		                		.append("|")
		                		.toString()
		                		);
		                
		                if(!"T".equals(parentesco)&&fila==1)
		                {
		                	throw new ApplicationException("La primer fila debe ser titular");
		                }
		                
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	if(fila==1)
	                	{
	                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," la primer fila debe ser titular, se excluir\u00e1n las filas hasta el siguiente titular "));
	                	}
	                	else
	                	{
	                		bufferErroresCenso.append(Utils.join("Error en el campo 'Parentesco' (C) de la fila ",fila," "));
	                	}
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(2)),"-"));
	                }
	              //PATERNO
	                try
                	{
		                logger.debug(
		                		new StringBuilder("PATERNO: ")
		                		.append(row.getCell(3).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
	                    apellidoP = row.getCell(3).getStringCellValue();
                        bufferLinea.append(
		                		new StringBuilder(row.getCell(3).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                
		                nombre = Utils.join(nombre,row.getCell(3).getStringCellValue()," ");
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido paterno' (D) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(3)),"-"));
	                }
	              //MATERNO
	                try
                	{
		                logger.debug(
		                		new StringBuilder("MATERNO: ")
		                		.append(row.getCell(4).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
	                    apellidoM = row.getCell(4).getStringCellValue();
	                    bufferLinea.append(
		                		new StringBuilder(row.getCell(4).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                
		                nombre = Utils.join(nombre,row.getCell(4).getStringCellValue()," ");
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Apellido materno' (E) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(4)),"-"));
	                }
	              //PRIMER NOMBRE
	                try
                	{
		                logger.debug(
		                		new StringBuilder("NOMBRE: ")
		                		.append(row.getCell(5).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                nombre1 = row.getCell(5).getStringCellValue();
		                bufferLinea.append(
		                		new StringBuilder(row.getCell(5).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                
		                nombre = Utils.join(nombre,row.getCell(5).getStringCellValue()," ");
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Nombre' (F) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(5)),"-"));
	                }
	              //SEGUNDO NOMBRE
                    try
                    {
                        auxCell=row.getCell(6);
                        logger.debug("SEGUNDO NOMBRE: "+(
                                auxCell!=null?auxCell.getStringCellValue()+"|":"|"
                                ));
                        bufferLinea.append(
                                auxCell!=null?auxCell.getStringCellValue()+"|":"|"
                                );
                        nombre2 = auxCell!=null?auxCell.getStringCellValue():"";
                        nombre = Utils.join(nombre,auxCell!=null?auxCell.getStringCellValue():"");
                        
                        if("T".equals(parentesco))
                        {
                            if(nFamilia > 0){
                                listaFamilias.put(nFamilia, filasFamilia);
                                filasFamilia = new ArrayList<Map<String,String>>(); 
                            }
                            
                            nFamilia++;
                            familias.put(nFamilia,"");
                            estadoFamilias.put(nFamilia,true);
                            titulares.put(nFamilia,nombre);
                            
                        }
                    }
                    catch(Exception ex)
                    {
                        filaBuena = false;
                        bufferErroresCenso.append(Utils.join("Error en el campo 'Segundo nombre' (G) de la fila ",fila," "));
                    }
                    finally
                    {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(6)),"-"));
                    }
	                
	              //SEXO
	                try
                	{
	                	String sexo = row.getCell(7).getStringCellValue();
	                	if(StringUtils.isBlank(sexo)
                				||(!sexo.equals("H")&&!sexo.equals("M")))
                		{
                			throw new ApplicationException("El sexo no se reconoce [H,M]");
                		}
		                logger.debug(
		                		new StringBuilder("SEXO: ")
		                		.append(sexo)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(sexo)
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Sexo' (H) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(7)),"-"));
	                }
	              //FECHA NACIMIENTO
	                try
                	{
		                auxDate=row.getCell(8).getDateCellValue();
		                if(auxDate!=null)
		                {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100
		                			||cal.get(Calendar.YEAR)<1900
		                			)
		                	{
		                		throw new ApplicationException("El anio de la fecha no es valido");
		                	}
		                }
		                
		                fechaNac = auxDate!=null?
        						renderFechas.format(auxDate)
        						:"";
        						
		                logger.debug(
		                		new StringBuilder("FECHA NACIMIENTO: ")
		                		.append(
		                				auxDate!=null?
		                						renderFechas.format(auxDate)
		                						:""
		                		)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxDate!=null?
		                				new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de nacimiento' (I) de la fila ",fila," "));
	                    try
	                    {
	                        //auxDate=row.getCell(8).getDateCellValue();
	                        auxDate= renderFechas.parse(row.getCell(8).getStringCellValue());
	                        if(auxDate!=null)
	                        {
	                            Calendar cal = Calendar.getInstance();
	                            cal.setTime(auxDate);
	                            if(cal.get(Calendar.YEAR)>2100
	                                    ||cal.get(Calendar.YEAR)<1900
	                                    )
	                            {
	                                throw new ApplicationException("El anio de la fecha no es valido");
	                            }
	                        }
	                        
	                        fechaNac = auxDate!=null?
	                                renderFechas.format(auxDate)
	                                :"";
	                                
	                        logger.debug(
	                                new StringBuilder("FECHA NACIMIENTO: ")
	                                .append(
	                                        auxDate!=null?
	                                                renderFechas.format(auxDate)
	                                                :""
	                                )
	                                .append("|")
	                                .toString()
	                                );
	                        bufferLinea.append(
	                                auxDate!=null?
	                                        new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
	                                        :"|"
	                                );
	                    }
	                    catch(Exception e)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de nacimiento' (I) de la fila ",fila," "));
	                    }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(8)),"-"));
	                }
	                
	                String codpostal = null;
	              //CODIGO POSTAL
	                try
                	{
		                logger.debug(
		                		new StringBuilder("COD POSTAL: ")
		                		.append(String.format("%.0f",row.getCell(9).getNumericCellValue()))
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(String.format("%.0f",row.getCell(9).getNumericCellValue()))
		                		.append("|")
		                		.toString()
		                		);
		                
		                codpostal = String.format("%.0f",row.getCell(9).getNumericCellValue());
		                
                	}
	                catch(Exception ex2)
	                {
	                	logger.error("error al leer codigo postal como numero, se intentara como string:",ex2);
	                	try
	                	{
	                		logger.debug(Utils.log("COD POSTAL: "
	                				,row.getCell(9).getStringCellValue()
			                		,"|"
			                		));
			                bufferLinea.append(Utils.join(
			                		row.getCell(9).getStringCellValue()
			                		,"|"
			                		));
			                
			                codpostal = row.getCell(9).getStringCellValue();
	                	}
		                catch(Exception ex)
		                {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' (J) de la fila ",fila," "));
		                }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(9)),"-"));
	                }
	                
	                logger.debug(">>> Validando codigo postal existente en el producto");
	                if(StringUtils.isBlank(codpostal) || !codigosPostales.containsKey(codpostal)){
	                	logger.error("Codigo Postal inexistente en la fila: " + fila);
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Codigo postal' No v\u00e1lido (J) de la fila ",fila," "));
	                }else{
	                	logger.debug("<<< Codigo postal correcto..");
	                }
	                
	              //ESTADO
	                try
                	{
		                logger.debug(
		                		new StringBuilder("ESTADO: ")
		                		.append(row.getCell(10).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(row.getCell(10).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado' (K) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(10)),"-"));
	                }
	              //MUNICIPIO
	                try
                	{
		                logger.debug(
		                		new StringBuilder("MUNICIPIO: ")
		                		.append(row.getCell(11).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(row.getCell(11).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Municipio' (L) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(11)),"-"));
	                }
	              //COLONIA
	                try
                	{
		                logger.debug(
		                		new StringBuilder("COLONIA: ")
		                		.append(row.getCell(12).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(row.getCell(12).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Colonia' (M) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(12)),"-"));
	                }
	              //CALLE
	                try
                	{
		                logger.debug(
		                		new StringBuilder("CALLE: ")
		                		.append(row.getCell(13).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(row.getCell(13).getStringCellValue())
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Calle' (N) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(13)),"-"));
	                }
	              //NUM. EXTERIOR
	                try
                	{
	                	String numExt = extraerStringDeCelda(row.getCell(14));
	                	if(StringUtils.isBlank(numExt))
	                	{
	                		throw new ApplicationException("Falta numero exterior");
	                	}
		                logger.debug(Utils.log("NUM EXT: ",numExt,"|"));
		                bufferLinea.append(Utils.join(numExt,"|"));
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Numero exterior' (O) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(14)),"-"));
	                }
	              //NUM. INTERIOR
	                try
                	{
		                String numInt = extraerStringDeCelda(row.getCell(15));
		                logger.debug(Utils.log("NUM INT: ",numInt,"|"));
		                bufferLinea.append(Utils.join(numInt,"|"));
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Numero interior' (P) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(15)),"-"));
	                }
	              //RFC
	                try
                	{
	                	auxCell = row.getCell(16);
		                logger.debug(Utils.log("RFC: ",auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(Utils.join(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                if(
		                		(auxCell==null||StringUtils.isBlank(auxCell.getStringCellValue()))
		                		&&pagoRepartido
		                		&&"T".equals(parentesco)
		                )
		                {
		                	throw new Exception("Sin rfc para un titular en pago repartido");
		                }
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'RFC' (Q) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(16)),"-"));
	                }
	              //CORREO
	                try
                	{
		                auxCell=row.getCell(17);
		                logger.debug(
		                		new StringBuilder("CORREO: ")
		                		.append(
		                				auxCell!=null?
		                						auxCell.getStringCellValue()
		                						:""
		                		).append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Correo' (R) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(17)),"-"));
	                }
	              //TELEFONO
	                try
                	{
	                    logger.debug("Se intenta como Entero ==> ");
	                    auxCell=row.getCell(18);
		                logger.debug(
		                		new StringBuilder("TELEFONO: ")
		                		.append(
		                				auxCell!=null?
		                						String.format("%.0f",auxCell.getNumericCellValue())
		                						:""
		                		)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(String.format("%.0f",auxCell.getNumericCellValue())).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	try
	                    {
	                	    logger.debug("Se intenta como String ==> ");
	                        auxCell=row.getCell(18);
	                        logger.debug(
	                                new StringBuilder("TELEFONO: ")
	                                .append(
	                                        auxCell!=null?
	                                                auxCell.getStringCellValue()
	                                                :""
	                                )
	                                .append("|")
	                                .toString()
	                                );
	                        bufferLinea.append(
	                                auxCell!=null?
	                                        new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                        :"|"
	                                );
	                    }
	                    catch(Exception ex2)
	                    {
	                        logger.debug("Se intenta como ==> "+ex2);
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Telefono :/' (S) de la fila ",fila," "));
	                    }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(18)),"-"));
	                }
	              //IDENTIDAD NO.DE EMPLEADO
	                try
                	{
		                auxCell=row.getCell(19);
		                if(pideNumCliemte&&
		                		(auxCell==null||auxCell.getStringCellValue()==null||StringUtils.isBlank(auxCell.getStringCellValue()))
		                )
		                {
		                	throw new ApplicationException("Necesito el numero de empleado");
		                }
		                logger.debug("IDENTIDAD: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                
		                if(cdunieco.equalsIgnoreCase("1403")){
		                	if(auxCell!=null){
		                		//Validamos que en verdad
		                		String identidad = auxCell.getStringCellValue();
		                		String identidadModificada[] = identidad.split("\\-");
		                		String seccion1 = StringUtils.leftPad(identidadModificada[0].toString(), 6, "0");
		                		logger.debug("Seccion 1 IDENTIDAD : {}",seccion1);
		                		String seccion2 = StringUtils.leftPad(identidadModificada[1].toString(), 2, "0");
		                		logger.debug("Seccion 2 IDENTIDAD : {}",seccion2);
		                		
		                		if(StringUtils.isNumeric(seccion1) && StringUtils.isNumeric(seccion2)){
		                			bufferLinea.append(seccion1.toString()+"-"+seccion2.toString()+"|");
		                		}else{
		                			//mandamos excepcion
			                		throw new ApplicationException("No es numero");
		                		}		                		
		                	}else{
		                		//mandamos excepcion
		                		throw new ApplicationException("La identidad no puede ser null");
		                	}
		                }else{
		                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
		                }
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Identidad' (T) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(19)),"-"));
	                }
	                
	              //FECHA DE RECONOCIMIENTO DE ANTIGUEDAD
	                try
                	{
		                auxDate=row.getCell(20)!=null?row.getCell(20).getDateCellValue():null;
		                if(auxDate!=null)
		                {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100
		                			||cal.get(Calendar.YEAR)<1900
		                			)
		                	{
		                		throw new ApplicationException("El anio de la fecha de reconocimiento de antiguedad no es valido");
		                	}
		                }
		                fecanti = auxDate!=null?
        						renderFechas.format(auxDate)
        						:"";
        						
		                logger.debug(
		                		new StringBuilder("FECHA RECONOCIMIENTO ANTIGUEDAD: ")
		                		.append(
		                				auxDate!=null?
		                						renderFechas.format(auxDate)
		                						:""
		                		)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxDate!=null?
		                				new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de reconocimiento antiguedad' (U) de la fila ",fila," "));
	                    try
	                    {
	                        auxDate=row.getCell(20)!=null?renderFechas.parse(row.getCell(20).getStringCellValue()):null;
	                        if(auxDate!=null)
	                        {
	                            Calendar cal = Calendar.getInstance();
	                            cal.setTime(auxDate);
	                            if(cal.get(Calendar.YEAR)>2100
	                                    ||cal.get(Calendar.YEAR)<1900
	                                    )
	                            {
	                                throw new ApplicationException("El anio de la fecha de reconocimiento de antiguedad no es valido");
	                            }
	                        }
	                        fecanti = auxDate!=null?
	                                renderFechas.format(auxDate)
	                                :"";
	                                
	                        logger.debug(
	                                new StringBuilder("FECHA RECONOCIMIENTO ANTIGUEDAD: ")
	                                .append(
	                                        auxDate!=null?
	                                                renderFechas.format(auxDate)
	                                                :""
	                                )
	                                .append("|")
	                                .toString()
	                                );
	                        bufferLinea.append(
	                                auxDate!=null?
	                                        new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
	                                        :"|"
	                                );
	                    }
	                    catch(Exception ex3)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de reconocimiento antiguedad' (U) de la fila ",fila," "));
	                    }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(20)),"-"));
	                }
	              //OCUPACION
	                try {
		                auxCell=row.getCell(21);
		                logger.debug("OCUPACION: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
		                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                	} catch(Exception ex) {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Ocupaci&oacute;n' (V) de la fila ",fila," "));
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(21)),"-"));
	                }
	              //EXT. OCUPACIONAL
	                try
                	{
		                auxCell=row.getCell(22);
		                logger.debug(
		                		new StringBuilder("EXTRAPRIMA OCUPACION: ")
		                		.append(
		                				auxCell!=null?
		                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                						:"|"
		                		).toString());
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                				:"|");
	                }
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de ocupacion' (W) de la fila ",fila," "));
	                    try
	                    {
	                        auxCell=row.getCell(22);
	                        logger.debug(
	                                new StringBuilder("EXTRAPRIMA OCUPACION: ")
	                                .append(
	                                        auxCell!=null?
	                                                new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                                :"|"
	                                ).toString());
	                        bufferLinea.append(
	                                auxCell!=null?
	                                        new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                        :"|");
	                    }
	                    catch(Exception e)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de ocupacion' (W) de la fila ",fila," "));
	                    }
	                }
                    finally
                    {
                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(22)),"-"));
                    }
	              //PESO
	                try
                	{
		                auxCell=row.getCell(23);
		                logger.debug(
		                		new StringBuilder("PESO: ")
		                		.append(
		                				auxCell!=null?
		                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                						:"|"
		                		).toString());
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                				:"|");
	                }
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (X) de la fila ",fila," "));
	                    try
	                    {
	                        auxCell=row.getCell(23);
	                        logger.debug(
	                                new StringBuilder("PESO: ")
	                                .append(
	                                        auxCell!=null?
	                                                new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                                :"|"
	                                ).toString());
	                        bufferLinea.append(
	                                auxCell!=null?
	                                        new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                        :"|");
	                    }
	                    catch(Exception ex2)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Peso' (X) de la fila ",fila," "));
	                    }
	                }
                    finally
                    {
                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(23)),"-"));
                    }
	              //ESTATURA
	                try
                	{
		                auxCell=row.getCell(24);
		                logger.debug(
		                		new StringBuilder("ESTATURA: ")
		                		.append(
		                				auxCell!=null?
		                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                						:"|"
		                		).toString());
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                				:"|");
	                }
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (Y) de la fila ",fila," "));
	                    try
	                    {
	                        auxCell=row.getCell(24);
	                        logger.debug(
	                                new StringBuilder("ESTATURA: ")
	                                .append(
	                                        auxCell!=null?
	                                                new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                                :"|"
	                                ).toString());
	                        bufferLinea.append(
	                                auxCell!=null?
	                                        new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                        :"|");
	                    }
	                    catch(Exception e)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Estatura' (Y) de la fila ",fila," "));
	                    }
	                }
                    finally
                    {
                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(24)),"-"));
                    }
	              //EXT. SOBREPESO
	                try
                	{
		                auxCell=row.getCell(25);
		                logger.debug(
		                		new StringBuilder("EXTRAPRIMA SOBREPESO: ")
		                		.append(
		                				auxCell!=null?
		                						new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                						:"|"
		                		).toString());
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(String.format("%.2f",auxCell.getNumericCellValue())).append("|").toString()
		                				:"|");
	                }
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de sobrepeso' (Z) de la fila ",fila," "));
	                    try
	                    {
	                        auxCell=row.getCell(25);
	                        logger.debug(
	                                new StringBuilder("EXTRAPRIMA SOBREPESO: ")
	                                .append(
	                                        auxCell!=null?
	                                                new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                                :"|"
	                                ).toString());
	                        bufferLinea.append(
	                                auxCell!=null?
	                                        new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
	                                        :"|");
	                    }
	                    catch(Exception e)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Extraprima de sobrepeso' (Z) de la fila ",fila," "));
	                    }
	                }
                    finally
                    {
                    	bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(25)),"-"));
                    }
	                
	              //ESTADO CIVIL
	                try
                	{
	                	String estadoCivil = row.getCell(26).getStringCellValue();
	                	
	                	//TODO: quitar cdtipsit estatico y ponerlo por subramo
	                	if("SSI".equals(cdtipsit)&&StringUtils.isBlank(estadoCivil))
	                	{
	                		throw new ApplicationException("El estado civil es obligatorio");
	                	}
	                	
	                	if(StringUtils.isNotBlank(estadoCivil))
	                	{
		                	if(
        						!estadoCivil.equals("C")
        						&&!estadoCivil.equals("S")
        						&&!estadoCivil.equals("D")
        						&&!estadoCivil.equals("V")
        						&&!estadoCivil.equals("O")
	                		)
	                		{
	                			throw new ApplicationException("El estado civil no se reconoce [C, S, D, V, O]");
	                		}
	                	}
	                	
		                logger.debug(
		                		new StringBuilder("EDO CIVIL: ")
		                		.append(estadoCivil)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		new StringBuilder(estadoCivil)
		                		.append("|")
		                		.toString()
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Estado civil' (AA) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(26)),"-"));
	                }
	              //FECHA INGRESO
	                try
                	{
		                auxDate=row.getCell(27)!=null?row.getCell(27).getDateCellValue():null;
		                if(auxDate!=null)
		                {
		                	Calendar cal = Calendar.getInstance();
		                	cal.setTime(auxDate);
		                	if(cal.get(Calendar.YEAR)>2100
		                			||cal.get(Calendar.YEAR)<1900
		                			)
		                	{
		                		throw new ApplicationException("El anio de la fecha de ingreso no es valido");
		                	}
		                }
		                
		                feingreso = auxDate!=null?
        						renderFechas.format(auxDate)
        						:"";
        						
		                logger.debug(
		                		new StringBuilder("FECHA INGRESO EMPLEADO: ")
		                		.append(
		                				auxDate!=null?
		                						renderFechas.format(auxDate)
		                						:""
		                		)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxDate!=null?
		                				new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	//filaBuena = false;
	                	//bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de ingreso empleado' (AB) de la fila ",fila," "));
	                    try
	                    {
	                        //auxDate=row.getCell(27)!=null?row.getCell(27).getDateCellValue():null;
	                        auxDate= ((extraerStringDeCelda(row.getCell(27))!=null) && 
                                    StringUtils.isNotBlank(extraerStringDeCelda(row.getCell(27)))) ?
                                            renderFechas.parse(extraerStringDeCelda(row.getCell(27))):
                                            null;
	                        if(auxDate!=null)
	                        {
	                            Calendar cal = Calendar.getInstance();
	                            cal.setTime(auxDate);
	                            if(cal.get(Calendar.YEAR)>2100
	                                    ||cal.get(Calendar.YEAR)<1900
	                                    )
	                            {
	                                throw new ApplicationException("El anio de la fecha de ingreso no es valido");
	                            }
	                        }
	                        
	                        feingreso = auxDate!=null?
	                                renderFechas.format(auxDate)
	                                :"";
	                                
	                        logger.debug(
	                                new StringBuilder("FECHA INGRESO EMPLEADO: ")
	                                .append(
	                                        auxDate!=null?
	                                                renderFechas.format(auxDate)
	                                                :""
	                                )
	                                .append("|")
	                                .toString()
	                                );
	                        bufferLinea.append(
	                                auxDate!=null?
	                                        new StringBuilder(renderFechas.format(auxDate)).append("|").toString()
	                                        :"|"
	                                );
	                    }
	                    catch(Exception e)
	                    {
	                        filaBuena = false;
	                        bufferErroresCenso.append(Utils.join("Error en el campo 'Fecha de ingreso empleado' (AB) de la fila ",fila," "));
	                    }
	                    finally
	                    {
	                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(27)),"-"));
	                    }
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(27)),"-"));
	                }
	              //ID SISA
	                try {
		                logger.debug("ID SISA: "+(String.format("%.0f",row.getCell(28).getNumericCellValue())+"|"));
		                bufferLinea.append(String.format("%.0f",row.getCell(28).getNumericCellValue())+"|");
                	} catch(Exception ex2) {
	                	logger.warn("error al leer el peso, se intentara como string:",ex2);
	                	try {
	                		auxCell=row.getCell(28);
			                bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
	                	} catch(Exception ex) {
		                	filaBuena = false;
		                	bufferErroresCenso.append(Utils.join("Error en el campo 'Id. SISA' (AC) de la fila ",fila," "));
		                }
	                } finally {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(28)),"-"));
	                }
	              //PLAZA
	                try
                	{
		                auxCell=row.getCell(29);
		                logger.debug(
		                		new StringBuilder("PLAZA: ")
		                		.append(
		                				auxCell!=null?
		                						auxCell.getStringCellValue()
		                						:""
		                		)
		                		.append("|")
		                		.toString()
		                		);
		                bufferLinea.append(
		                		auxCell!=null?
		                				new StringBuilder(auxCell.getStringCellValue()).append("|").toString()
		                				:"|"
		                		);
                	}
	                catch(Exception ex)
	                {
	                	filaBuena = false;
	                	bufferErroresCenso.append(Utils.join("Error en el campo 'Plaza' (AD) de la fila ",fila," "));
	                }
	                finally
	                {
	                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(29)),"-"));
	                }
	              //ID. ASEGURADO
	                try {
                        logger.debug("ID. ASEGURADO: "+(String.format("%.0f",row.getCell(30).getNumericCellValue())+"|"));
                        auxCell=row.getCell(30);
                        cveAsegurado = auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue()):"";
                        bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                        /*boolean exitoValidacion;
                        if(StringUtils.isNotBlank(cveAsegurado) && (Integer.parseInt(cveAsegurado) > 0)){
                            long timestamp=System.currentTimeMillis();
                            Map<String,Object>managerResult=personasManager.obtenerPersonaPorCdperson(cveAsegurado,timestamp);
                            exitoValidacion = (Boolean)managerResult.get("exito");
                           if(exitoValidacion){
                        	   logger.debug("ID ASEGURADO: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                        	   bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                           }else{
                               filaBuena = false;
                               bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' :"+cveAsegurado+" ,no se encuentra en SICAPS (AE) de la fila ",fila," "));
                           }                            
                        }else{
                            String respuesta  = personasManager.obtieneAseguradoSICAPS((nombre1+" "+nombre2),apellidoP,apellidoM,renderFechas.parse(fechaNac));
                            if(Integer.parseInt(respuesta) > 0){
                                filaBuena = false;
                                bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado'. El asegurado ya se encuentra en SICAPS clave: "+respuesta +" (AE) de la fila ",fila," "));
                            }else{
                            	logger.debug("ID ASEGURADO: "+(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|"));
                            	bufferLinea.append(auxCell!=null?String.format("%.0f",auxCell.getNumericCellValue())+"|":"|");
                            }                            
                        }*/
                    } catch(Exception ex2) { 
                        logger.warn("error al leer el campo 'Id. Asegurado', se intentara como string ==>");
                        try {
                            auxCell=row.getCell(30);
                            cveAsegurado = auxCell!=null?auxCell.getStringCellValue():"";
                            bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                            /*boolean exitoValidacion;
                            if(StringUtils.isNotBlank(cveAsegurado)){
                                long timestamp=System.currentTimeMillis();
                                Map<String,Object>managerResult=personasManager.obtenerPersonaPorCdperson(cveAsegurado,timestamp);
                                exitoValidacion = (Boolean)managerResult.get("exito");
                               if(exitoValidacion){
                            	   logger.debug("ID ASEGURADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
                            	   bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                               }else{
                                   filaBuena = false;
                                   bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' :"+cveAsegurado+" ,no se encuentra en SICAPS (AE) de la fila ",fila," "));
                               }                            
                            }else{
                                String respuesta  = personasManager.obtieneAseguradoSICAPS((nombre1+" "+nombre2),apellidoP,apellidoM,renderFechas.parse(fechaNac));
                                if(Integer.parseInt(respuesta) > 0){
                                    filaBuena = false;
                                    bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado'. El asegurado ya se encuentra en SICAPS clave: "+respuesta +" (AE) de la fila ",fila," "));
                                }else{
                                	logger.debug("ID ASEGURADO: "+(auxCell!=null?auxCell.getStringCellValue()+"|":"|"));
                                	bufferLinea.append(auxCell!=null?auxCell.getStringCellValue()+"|":"|");
                                }                                
                            }*/
                        } catch(Exception ex) {
                            filaBuena = false;
                            bufferErroresCenso.append(Utils.join("Error en el campo 'Id. Asegurado' (AE) de la fila ",fila," "));
                        }
                    } finally {
                        bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(30)),"-"));
                    }
	                /* nuevos para SSI fin */

                    try {
                        auxCell = row.getCell(31);
                        String valor = "";
                        if (auxCell !=null) {
                            valor = String.format("%.0f",auxCell.getNumericCellValue());
                        }
                        logger.debug(Utils.join("NUMERO DE DEPENDIENTES: ", valor, "|"));
                        bufferLinea.append(Utils.join(valor, "|"));
                    } catch (Exception ex) {
                        filaBuena = false;
                        bufferErroresCenso.append(Utils.join("Error en el campo 'Numero de dependientes' (AF) de la fila ",fila," "));
                    } finally {
                        bufferLineaStr.append(Utils.join("",this.extraerStringDeCelda(row.getCell(31)),"-"));
                    }
	                
	                logger.debug(Utils.log("** NUEVA_FILA (filaBuena=",filaBuena,",cdgrupo=",cdgrupo,") **"));
	                
	                if(filaBuena)
	                {
	                	familias.put(nFamilia,Utils.join(familias.get(nFamilia),bufferLinea.toString(),"\n"));
	                	filasProcesadas = filasProcesadas + 1;
	                	gruposValidos[((int)cdgrupo)-1]=true;
	                	
                		Map<String,String> params =  new HashMap<String, String>();
    					
    					params.put("pv_cdunieco_i", cdunieco);
    					params.put("pv_cdramo_i", cdramo);
    					params.put("pv_estado_i", "W");
    					params.put("pv_nmpoliza_i", nmpoliza);
    					params.put("pv_cdgrupo_i", extraerStringDeCelda(row.getCell(0)));
    					params.put("pv_parentesco_i", extraerStringDeCelda(row.getCell(2)));
    					params.put("pv_dsapellido_i", extraerStringDeCelda(row.getCell(3)));
    					params.put("pv_dsapellido1_i", extraerStringDeCelda(row.getCell(4)));
    					params.put("pv_dsnombre_i", extraerStringDeCelda(row.getCell(5)));
    					params.put("pv_dsnombre1_i", extraerStringDeCelda(row.getCell(6)));
    					params.put("pv_otsexo_i", extraerStringDeCelda(row.getCell(7)));
    					
    					params.put("pv_fenacimi_i", fechaNac);
    					params.put("pv_cdpostal_i", extraerStringDeCelda(row.getCell(9)));
    					params.put("pv_dsestado_i", extraerStringDeCelda(row.getCell(10)));
    					params.put("pv_dsmunicipio_i", extraerStringDeCelda(row.getCell(11)));
    					params.put("pv_dscolonia_i", extraerStringDeCelda(row.getCell(12)));
    					params.put("pv_dsdomici_i", extraerStringDeCelda(row.getCell(13)));
    					params.put("pv_nmnumero_i", extraerStringDeCelda(row.getCell(14)));
    					params.put("pv_nmnumint_i", extraerStringDeCelda(row.getCell(15)));
    					params.put("pv_cdrfc_i",    extraerStringDeCelda(row.getCell(16)));
    					params.put("pv_dsemail_i",  extraerStringDeCelda(row.getCell(17)));
    					params.put("pv_nmtelefo_i", extraerStringDeCelda(row.getCell(18)));
    					params.put("pv_identidad_i",extraerStringDeCelda(row.getCell(19)));
    					params.put("pv_fecantig_i", fecanti);
    					params.put("pv_expocupacion_i", extraerStringDeCelda(row.getCell(22)));
    					params.put("pv_peso_i",     extraerStringDeCelda(row.getCell(23)));
    					params.put("pv_estatura_i", extraerStringDeCelda(row.getCell(24)));
    					params.put("pv_expsobrepeso_i", extraerStringDeCelda(row.getCell(25)));
    					params.put("pv_edocivil_i", extraerStringDeCelda(row.getCell(26)));
    					params.put("pv_feingresoempleo_i", feingreso);
    					params.put("pv_plaza_i", extraerStringDeCelda(row.getCell(29)));
    					params.put("pv_idasegurado_i", extraerStringDeCelda(row.getCell(30)));
    					
    					filasFamilia.add(params);
	                	
	                }
	                else
	                {
	                	filasError = filasError + 1;
	                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
	                	estadoFamilias.put(nFamilia,false);
	                	
	                	if(!errorFamilia.containsKey(nFamilia))
	                	{
	                		errorFamilia.put(nFamilia,fila);
	                	}
	                }
	                
	                if(cdgrupo>0d && cdgrupo<=grupos.size())
	                {
	                	logger.debug(Utils.log("cdgrupo=",cdgrupo,", valido=",gruposValidos[((int)cdgrupo)-1]));
	                }
	                else
	                {
	                	logger.debug(Utils.log("cdgrupo=",cdgrupo,", !no se puede imprimir valido"));
	                }
	            }
	            
	            if(resp.isExito())
	            {
	            	for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		logger.debug(Utils.log("gruposValidos[i]=",gruposValidos[i]));
	            	}
	            }
	            
	            if(resp.isExito())
	            {
	            	boolean       sonGruposValidos = true;
	            	StringBuilder errorGrupos      = new StringBuilder();
	            	for(int i=0;i<gruposValidos.length;i++)
	            	{
	            		if(!gruposValidos[i])
	            		{
	            			sonGruposValidos = false;
	            			errorGrupos.append("Debe haber al menos un asegurado v\u00E1lido para el grupo ").append(i+1).append("\n");
	            		}
	            	}
	            	if(!sonGruposValidos)
	            	{
	            		resp.setExito(false);
	            		resp.setRespuesta(
	            				errorGrupos.append("\n")
	            				.append(bufferErroresCenso.toString())
	            				.append("\nError #").append(System.currentTimeMillis()).toString());
	            		resp.setRespuestaOculta(resp.getRespuesta());
	            		logger.error(bufferErroresCenso.toString());
	            		logger.error(resp.getRespuesta());
	            	}
	            }
	            
	            if(resp.isExito())
	            {
	            	logger.debug("\nFamilias: {}\nEstado familias: {}\nErrorFamilia: {}\nTitulares: {}"
		            		,familias,estadoFamilias,errorFamilia,titulares);
		            
		            for(Entry<Integer,Boolean>en:estadoFamilias.entrySet())
		            {
		            	int     n = en.getKey();
		            	boolean v = en.getValue();
		            	if(v)
		            	{
		            		output.print(familias.get(n));
		            	}
		            	else
		            	{
		            		bufferErroresCenso.append(Utils.join("La familia ",n," del titular '",titulares.get(n),"' no fue incluida por error en la fila ",errorFamilia.get(n),"\n"));
		            	}
		            }
		            
	            	resp.getSmap().put("erroresCenso"    , bufferErroresCenso.toString());
	            	resp.getSmap().put("filasLeidas"     , Integer.toString(filasLeidas));
	            	resp.getSmap().put("filasProcesadas" , Integer.toString(filasProcesadas));
	            	resp.getSmap().put("filasErrores"    , Integer.toString(filasError));
	            	
	            	
	            	if(nFamilia > 0 && !filasFamilia.isEmpty()){
	            		listaFamilias.put(nFamilia, filasFamilia);
	            		
	            		try
	    				{
	            			for(Entry<Integer, List<Map<String,String>>> entry : listaFamilias.entrySet())
	            			{
	            				Integer numFam = entry.getKey();
	            				
	            				if(estadoFamilias.containsKey(numFam) && estadoFamilias.get(numFam)){
	            				    List<Map<String,String>> listaZwcenso = new ArrayList<Map<String,String>> ();
	            					for(Map<String,String> paramsElemFam: listaFamilias.get(numFam)){
	            					    listaZwcenso.add(paramsElemFam);
	            					}
	            					cotizacionDAO.insertaRegistroInfoCenso(listaZwcenso);
	            				}
	            			}
	    				}
	    	            catch(Exception ex)
	    	            {
	    	            	logger.error("Error al insetar registro de censo", ex);
	    	            }
	            		
	            	}
	            }
	            
	            if(resp.isExito())
	            {
	            	try
	            	{
	            		input.close();
	            		output.close();
	            	}
	            	catch(Exception ex)
	            	{
	            		long timestamp = System.currentTimeMillis();
	            		resp.setExito(false);
	            		resp.setRespuesta(new StringBuilder("Error al transformar el archivo #").append(timestamp).toString());
	            		resp.setRespuestaOculta(ex.getMessage());
	            		logger.error(resp.getRespuesta(),ex);
	            	}
	            }
	            
	            logger.debug(
						new StringBuilder()
						.append("\n------ PROCESAR CENSO COMPLETO ------")
						.append("\n-------------------------------------")
						.toString()
						);
	            

	            if(resp.isExito())
	            {
					resp.setExito(
						FTPSUtils.upload
						(
							dominioServerLayout,
							usuarioServerLayout,
							passwordServerLayout,
							archivoTxt.getAbsolutePath(),
							new StringBuilder(direcServerLayout).append("/").append(nombreCenso).toString()
						)
						&&FTPSUtils.upload
						(
							dominioServerLayouts2,
							usuarioServerLayout,
							passwordServerLayout,
							archivoTxt.getAbsolutePath(),
							new StringBuilder(direcServerLayout).append("/").append(nombreCenso).toString()
						)
					);
					
					if(!resp.isExito())
					{
						long timestamp = System.currentTimeMillis();
						resp.setExito(false);
						resp.setRespuesta(new StringBuilder("Error al transferir archivo al servidor #").append(timestamp).toString());
						resp.setRespuestaOculta(resp.getRespuesta());
						logger.error(resp.getRespuesta());
					}
	            }
			}
			
			if(resp.isExito()&&StringUtils.isBlank(nombreCensoConfirmado))
			{
				resp.getSmap().put("nombreCensoParaConfirmar", nombreCenso);
				resp.setRespuesta(Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]"));
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ subirCensoCompleto @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}

	@Override
	public void insertaRegistroInfoCenso (List<Map<String,String>> lista) throws Exception{
		cotizacionDAO.insertaRegistroInfoCenso(lista);
	}
	
	@Override
	public ManagerRespuestaSmapVO confirmarCensoCompleto(
			String cdunieco
			,String cdramo
			,String nmpoliza
			,String feini
			,String fefin
			,String cdperpag
			,String pcpgocte
			,String rutaDocsTemp
			,String censoTimestamp
			,String dominioServerLayout
			,String usuarioServerLayout
			,String passwordServerLayout
			,String direcServerLayout
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdedoCli
			,String cdmuniciCli
			,List<Map<String,Object>>grupos
			,String clasif
			,String LINEA_EXTENDIDA
			,String cdpersonCli
			,String nombreCli
			,String rfcCli
			,String dsdomiciCli
			,String nmnumeroCli
			,String nmnumintCli
			,String ntramite
			,String ntramiteVacio
			,String cdelemen
			,String nombreCensoConfirmado
			,String cdideper_
			,String cdideext_
			,String nmpolant
			,String nmrenova
			,UserVO usuarioSesion
			,String estatuRenovacion
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ confirmarCensoCompleto @@@@@@")
				.append("\n@@@@@@ cdunieco=")            .append(cdunieco)
				.append("\n@@@@@@ cdramo=")              .append(cdramo)
				.append("\n@@@@@@ nmpoliza=")            .append(nmpoliza)
				.append("\n@@@@@@ feini=")               .append(feini)
				.append("\n@@@@@@ fefin=")               .append(fefin)
				.append("\n@@@@@@ cdperpag=")            .append(cdperpag)
				.append("\n@@@@@@ pcpgocte=")            .append(pcpgocte)
				.append("\n@@@@@@ rutaDocsTemp=")        .append(rutaDocsTemp)
				.append("\n@@@@@@ censoTimestamp=")      .append(censoTimestamp)
				.append("\n@@@@@@ dominioServerLayout=") .append(dominioServerLayout)
				.append("\n@@@@@@ usuarioServerLayout=") .append(usuarioServerLayout)
				.append("\n@@@@@@ passwordServerLayout=").append(passwordServerLayout)
				.append("\n@@@@@@ direcServerLayout=")   .append(direcServerLayout)
				.append("\n@@@@@@ cdtipsit=")            .append(cdtipsit)
				.append("\n@@@@@@ cdusuari=")            .append(cdusuari)
				.append("\n@@@@@@ cdsisrol=")            .append(cdsisrol)
				.append("\n@@@@@@ cdagente=")            .append(cdagente)
				.append("\n@@@@@@ codpostalCli=")        .append(codpostalCli)
				.append("\n@@@@@@ cdedoCli=")            .append(cdedoCli)
				.append("\n@@@@@@ cdmuniciCli=")         .append(cdmuniciCli)
				.append("\n@@@@@@ grupos=")              .append(grupos)
				.append("\n@@@@@@ clasif=")              .append(clasif)
				.append("\n@@@@@@ LINEA_EXTENDIDA=")     .append(LINEA_EXTENDIDA)
				.append("\n@@@@@@ cdpersonCli=")         .append(cdpersonCli)
				.append("\n@@@@@@ nombreCli=")           .append(nombreCli)
				.append("\n@@@@@@ dsdomiciCli=")         .append(dsdomiciCli)
				.append("\n@@@@@@ nmnumeroCli=")         .append(nmnumeroCli)
				.append("\n@@@@@@ nmnumintCli=")         .append(nmnumintCli)
				.append("\n@@@@@@ ntramite=")            .append(ntramite)
				.append("\n@@@@@@ ntramiteVacio=")       .append(ntramiteVacio)
				.append("\n@@@@@@ cdelemen=")            .append(cdelemen)
				.append("\n@@@@@@ nombreCensoConfirmado=").append(nombreCensoConfirmado)
				.append("\n@@@@@@ cdideper_=")            .append(cdideper_)
				.append("\n@@@@@@ cdideext_=")            .append(cdideext_)
				.append("\n@@@@@@ nmpolant=")             .append(nmpolant)
				.append("\n@@@@@@ nmrenova=")             .append(nmrenova)
				.append("\n@@@@@@ estatuRenovacion=")     .append(estatuRenovacion)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		resp.setSmap(new HashMap<String,String>());
		
		try
		{
			new ConfirmaCensoConcurrente(cdunieco, cdramo, nmpoliza, cdtipsit,
					nombreCensoConfirmado, cdusuari, cdsisrol, nombreCensoConfirmado, cdagente,
					codpostalCli, cdedoCli, cdmuniciCli, ntramite, ntramiteVacio,
					clasif, LINEA_EXTENDIDA, cdpersonCli, nombreCli, rfcCli,
					dsdomiciCli, nmnumeroCli, nmnumintCli, cdelemen, cdideper_,
					cdideext_, cdperpag, usuarioSesion, grupos, estatuRenovacion
					).start();
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al confirmar el censo concurrente. #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ confirmarCensoCompleto @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO validarCambioZonaGMI(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String codpostal)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ validarCambioZonaGMI @@@@@@")
				.append("\n@@@@@@ cdunieco=") .append(cdunieco)
				.append("\n@@@@@@ cdramo=")   .append(cdramo)
				.append("\n@@@@@@ cdtipsit=") .append(cdtipsit)
				.append("\n@@@@@@ estado=")   .append(estado)
				.append("\n@@@@@@ nmpoliza=") .append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=") .append(nmsuplem)
				.append("\n@@@@@@ nmsituac=") .append(nmsituac)
				.append("\n@@@@@@ codpostal=").append(codpostal)
				.toString()
				);
		
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		//procedimiento
		try
		{
			cotizacionDAO.validarCambioZonaGMI(cdunieco,cdramo,cdtipsit,estado,nmpoliza,nmsuplem,nmsituac,codpostal);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al validar cambio de zona #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ validarCambioZonaGMI @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO validarEnfermedadCatastGMI(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String circHosp)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ validarEnfermedadCatastGMI @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ nmsituac=").append(nmsituac)
				.append("\n@@@@@@ circHosp=").append(circHosp)
				.toString()
				);
		
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		//procedimiento
		try
		{
			cotizacionDAO.validarEnfermedadCatastGMI(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,circHosp);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al validar circulo hospitalario #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ validarEnfermedadCatastGMI @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public String cargarTabuladoresGMIParche(
			String circulo
			,String cdatribu)throws Exception
	{
		return cotizacionDAO.cargarTabuladoresGMIParche(circulo, cdatribu);
	}
	
	@Override
	public String guardarContratanteColectivo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String rfc
			,String cdperson
			,String nombre
			,String cdpostal
			,String cdedo
			,String cdmunici
			,String dsdomici
			,String nmnumero
			,String nmnumint
			,String nmorddom
			,boolean esConfirmaEmision
			,UserVO usuarioSesion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarContratanteColectivo @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ rfc="      , rfc
				,"\n@@@@@@ cdperson=" , cdperson
				,"\n@@@@@@ nombre="   , nombre
				,"\n@@@@@@ cdpostal=" , cdpostal
				,"\n@@@@@@ cdedo="    , cdedo
				,"\n@@@@@@ cdmunici=" , cdmunici
				,"\n@@@@@@ dsdomici=" , dsdomici
				,"\n@@@@@@ nmnumero=" , nmnumero
				,"\n@@@@@@ nmnumint=" , nmnumint
				,"\n@@@@@@ nmorddom=" , nmorddom
				,"\n@@@@@@ esConfirmaEmision=" , esConfirmaEmision
				));
		
		String paso = "Se guarda contratante colectivo";
		
		try
		{
			String swexiper = Constantes.SI;
			if(!esConfirmaEmision && StringUtils.isBlank(cdperson)){
				cdperson = personasDAO.obtieneCdperson();
				
				logger.debug("<<<>>> Nuevo cdperson generado <<<>>> " + cdperson);
				swexiper = Constantes.NO; // para generar un cdperson de prospecto
				
				String usuarioCaptura =  null;
				
				if(usuarioSesion!=null){
					if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
						usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
					}else{
						usuarioCaptura = usuarioSesion.getCodigoPersona();
					}
					
				}
				
				/**
				 * GURADA UN PROSPECTO PERO COMO NO SE INDICA UN TIPO DE PERSONA SE GUARDA POR DEFAULT COMO MORAL
				 */
				personasDAO.movimientosMpersona(
					cdperson
					,"1"         //cdtipide
					,null        //cdideper
					,nombre
					,"99"        //cdtipper 99 PARA PROSPECTO TEMPORAL, se borra despues de la emision.
					,"M"         //otfisjur
					,"H"         //otsexo
					,new Date()  //fenacimi
					,rfc
					,""          //dsemail
					,null        //dsnombre1
					,null        //dsapellido
					,null        //dsapellido1
					,new Date()  //feingreso
					,null        //cdnacion
					,null        //canaling
					,null        //conducto
					,null        //ptcumupr
					,null        //residencia
					,null		 //nongrata
					,null		 //cdideext
					,null		 //cdestcivil
					,null		 //cdsucemi
					,usuarioCaptura
					,Constantes.INSERT_MODE
				);
				

				nmorddom = "1";
				personasDAO.movimientosMdomicil(
					cdperson
					,nmorddom   //nmorddom
					,dsdomici
					,null       //nmtelefo
					,cdpostal
					,cdedo
					,cdmunici
					,null       //cdcoloni
					,nmnumero
					,nmnumint
					,"1" // domicilio personal default
					,usuarioCaptura
					,Constantes.SI  //domicilio activo
					,Constantes.INSERT_MODE
				);
				
			}else if(!esConfirmaEmision && StringUtils.isNotBlank(cdperson)){
				swexiper = Constantes.NO;
			}if(esConfirmaEmision && StringUtils.isBlank(cdperson)){
				logger.error("El cdperson no puede ser nulo al confirmar la emision");
				return cdperson;
			}
			
			logger.debug("Asignando contratante para poliza colectiva, cdperson:"+ cdperson+" numero de domicilio: "+nmorddom);
			
			cotizacionDAO.borrarMpoliperSituac0(cdunieco, cdramo, estado, nmpoliza, "0", "1");
			
			cotizacionDAO.movimientoMpoliper(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0"       //nmsituac
					,"1"       //cdrol
					,cdperson
					,"0"       //nmsuplem
					,"V"       //status
					,nmorddom       //nmorddom
					,null      //swreclam
					,Constantes.INSERT_MODE
					,swexiper
					);
			
			cotizacionDAO.actualizaDomicilioAseguradosColectivo(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdpostal, cdedo, cdmunici);
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ Contratante guardado cdperson:" ,cdperson 
				,"\n@@@@@@ guardarContratanteColectivo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return cdperson;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarTramite(String ntramite)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarTramite @@@@@@"
				,"\n@@@@@@ ntramite=" , ntramite
				));
		
		ManagerRespuestaSmapVO resp = new ManagerRespuestaSmapVO(true);
		
		String paso = null;
		
		try
		{
			paso = "Recuperando tramite";
			
			List<Map<String,String>>lista=mesaControlDAO.cargarTramitesPorParametrosVariables(
					TipoTramite.POLIZA_NUEVA.getCdtiptra()
					,ntramite
					,null //cdunieco
					,null //cdramo
					,null //estado
					,null //nmpoliza
					,null //nmsuplem
					,null //nmsolici
					);
			
			if(lista==null||lista.size()==0)
			{
				throw new ApplicationException("No hay tramite");
			}
			if(lista.size()>1)
			{
				throw new ApplicationException("Tramite duplicado");
			}
			resp.setSmap(lista.get(0));
			
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ " , resp
				,"\n@@@@@@ cargarTramite @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
    @Override
    public boolean cargarBanderaCambioCuadroPorProducto(String cdramo)
    {
    	logger.debug(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ cargarBanderaCambioCuadroPorProducto @@@@@@"
    			,"\n@@@@@@ cdramo=",cdramo
    			));
    	
    	boolean bandera=false;
    	
    	try
    	{
    		bandera=cotizacionDAO.cargarBanderaCambioCuadroPorProducto(cdramo);
    	}
    	catch(Exception ex)
    	{
    		logger.error("Error al obtener bandera de cambio de cuadro por producto",ex);
    		bandera=false;
    	}
    	
    	logger.debug(Utils.log(
    		 "\n@@@@@@ bandera=",bandera
   			,"\n@@@@@@ cargarBanderaCambioCuadroPorProducto @@@@@@"
   			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
   			));
    	return bandera;
    }
    
    @Override
    public ManagerRespuestaSlistSmapVO cotizar(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String cdusuari
			,String cdelemen
			,String nmpoliza
			,String feini
			,String fefin
			,String fesolici
			,String cdpersonCli
			,String nmorddomCli
			,String cdideperCli
			,boolean noTarificar
			,boolean conIncisos
			,List<Map<String,String>>incisos
			,boolean flagMovil
			,Map<String,String>tvalopol
			,String cdagente
			,UserVO usuarioSesion
			,String fromSigs
			,String cduniext
			,String renramo
			,String nmpoliex
			,String ntramite
			)throws Exception
    {
    	logger.debug(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ cotizar @@@@@@"
    			,"\n@@@@@@ cdunieco="    , cdunieco
    			,"\n@@@@@@ cdramo="      , cdramo
    			,"\n@@@@@@ cdtipsit="    , cdtipsit
    			,"\n@@@@@@ cdusuari="    , cdusuari
    			,"\n@@@@@@ cdelemen="    , cdelemen
    			,"\n@@@@@@ nmpoliza="    , nmpoliza
    			,"\n@@@@@@ feini="       , feini
    			,"\n@@@@@@ fefin="       , fefin
    			,"\n@@@@@@ fesolici="    , fesolici
    			,"\n@@@@@@ cdpersonCli=" , cdpersonCli
    			,"\n@@@@@@ cdideperCli=" , cdideperCli
    			,"\n@@@@@@ nmorddomCli=" , nmorddomCli
    			,"\n@@@@@@ noTarificar=" , noTarificar
    			,"\n@@@@@@ conIncisos="  , conIncisos
    			,"\n@@@@@@ incisos="     , incisos
    			,"\n@@@@@@ flagMovil="   , flagMovil
    			,"\n@@@@@@ tvalopol="    , tvalopol
    			,"\n@@@@@@ cdagente="    , cdagente
    			,"\n@@@@@@ fromSigs="    , fromSigs
    			));
    	
    	ManagerRespuestaSlistSmapVO resp=new ManagerRespuestaSlistSmapVO(true);
    	resp.setSmap(new HashMap<String,String>());
    	resp.getSmap().put("nmpoliza" , nmpoliza);
    	
    	String paso = "Cotizando";
    	
    	try
    	{
    		Date fechaHoy = new Date();
    		
    		String llaveRol       = "";
            String llaveSexo      = "";
            String llaveFenacimi  = "DATE";
            String llaveCodPostal = "";
    		
    		if(noTarificar==false)
			{
				////////////////////////////////
				////// si no hay nmpoliza //////
				if(StringUtils.isBlank(nmpoliza) || nmpoliza.equals("0"))
				{
					try
					{
						paso = "Recuperando consecutivo de p\u00F3liza";
						nmpoliza = cotizacionDAO.calculaNumeroPoliza(cdunieco,cdramo,"W");
						resp.getSmap().put("nmpoliza" , nmpoliza);
						if(nmpoliex != null && !nmpoliex.isEmpty() && ("|5|6|16|").lastIndexOf("|"+cdramo+"|")!=-1 && fromSigs.equals("S"))
						{
							try{
								flujoMesaControlManager.actualizaTramiteMC(
								         ntramite 
										,cdunieco
										,cdramo
										,"W"//estado
										,nmpoliza
										,"21"//cdtiptra 
										,cduniext
										,renramo
										,nmpoliex
										);
							}
							catch (Exception e) {
								logger.error("error al actualizar el valor otvalor01 en tmesacontrol ",e);
							}
						}
					}
					catch(Exception ex)
					{
						throw new ApplicationException("Falta parametrizar la numeraci\u00f3n de p\u00f3liza");
					}
				}
				////// si no hay nmpoliza //////
				////////////////////////////////
				
				////// para incluir fesolici //////
				if(StringUtils.isBlank(fesolici))
				{
					fesolici = renderFechas.format(fechaHoy);
				}
				////// para incluir fesolici //////
				
				//////////////////////
	            ////// mpolizas //////
				paso = "Insertando maestro de p\u00F3liza";
				cotizacionDAO.movimientoPoliza(
						cdunieco
						,cdramo
						,"W"      //estado
						,nmpoliza
						,"0"      //nmsuplem
						,"V"      //status
						,"0"      //estado
						,null     //nmsolici
						,null     //feautori
						,null     //cdmotanu
						,null     //feanulac
						,"N"      //swautori
						,"001"    //cdmoneda
						,null     //feinisus
						,null     //fefinsus
						,"R"      //ottempot
						,feini    //feefecto
						,"12:00"  //hhefecto
						,fefin    //feproren
						,null     //fevencim
						,"0"      //nmrenova
						,null     //ferecibo
						,null     //feultsin
						,"0"      //nmnumsin
						,"N"      //cdtipcoa
						,"A"      //swtarifi
						,null     //swabrido
						,renderFechas.format(fechaHoy) //feemisio
						,"12"     //cdperpag
						,null //nmpoliex
						,"P1"     //nmcuadro
						,"100"    //porredau
						,"S"      //swconsol
						,null     //nmpolant
						,null     //nmpolnva
						,fesolici
						,StringUtils.isNotBlank(cdagente)?cdagente:cdusuari//cdramant
						,null     //cdmejred
						,null     //nmpoldoc
						,null     //nmpoliza2
						,null     //nmrenove
						,null     //nmsuplee
						,null     //ttipcamc
						,null     //ttipcamv
						,null     //swpatent
						,"100"    //pcpgocte
						,null     //tipoflot
						,null     //agrupador
						,"U"      //accion
						);
	            ////// mpolizas //////
	            //////////////////////
				
				paso = "Insertando valores adicionales de p\u00F3liza";
				cotizacionDAO.movimientoTvalopol(
						cdunieco
						,cdramo
						,"W"      //estado
						,nmpoliza
						,"0"      //nmsuplem
						,"V"      //status
						,tvalopol
						);
			}
	            
            if(conIncisos)
            {
	            ////////////////////////////////
	            ////// ordenar al titular //////
	            
	            ////// 1. indicar para la situacion el indice //////
	            try {
	            	LinkedHashMap<String,Object>p=new LinkedHashMap<String,Object>();
	            	p.put("cdtipsit",cdtipsit);
	            	
	            	paso = "Recuperando atributos base de situaci\u00F3n";
	            	Map<String,String>atributos=consultasDAO.cargarAtributosBaseCotizacion(cdtipsit);
	            	if(atributos.get("PARENTESCO") != null) {
	            		llaveRol=atributos.get("PARENTESCO");
	                	if(llaveRol.length()==1) {
	                		llaveRol="0"+llaveRol;
	                	}
	                	llaveRol="parametros.pv_otvalor"+llaveRol;
	            	}
	            	if(atributos.get("SEXO") != null) {
	            		llaveSexo=atributos.get("SEXO");
	            		if(llaveSexo.length()==1) {
	                		llaveSexo="0"+llaveSexo;
	                	}
	                	llaveSexo="parametros.pv_otvalor"+llaveSexo;
	            	}
	            	if(atributos.get("FENACIMI") != null) {
	            		llaveFenacimi=atributos.get("FENACIMI");
	                	if(llaveFenacimi.length()==1) {
	                		llaveFenacimi="0"+llaveFenacimi;
	                	}
	                	llaveFenacimi="parametros.pv_otvalor"+llaveFenacimi;
	            	}
	            	if(atributos.get("CODPOSTAL") != null) {
	            		llaveCodPostal=atributos.get("CODPOSTAL");
	                	if(llaveCodPostal.length()==1) {
	                		llaveCodPostal="0"+llaveCodPostal;
	                	}
	                	llaveCodPostal="parametros.pv_otvalor"+llaveCodPostal;
	            	}
	            } catch(Exception ex){
	            	logger.error("error al obtener atributos", ex);
	            }
	            ////// 1. indicar para la situacion el indice //////
	            
	            ////// parche. Validar codigo postal //////
	            if(StringUtils.isNotBlank(llaveCodPostal)&&StringUtils.isNotBlank(incisos.get(0).get(llaveCodPostal)))
	            {
	            	paso = "Validando c\u00F3digo postal";
	            	cotizacionDAO.validarCodpostalTarifa(incisos.get(0).get(llaveCodPostal),cdtipsit);
	            }
	            //// parche. Validar codigo postal //////
	            
	            ////// 2. ordenar //////
	            int indiceTitular=-1;
	            for(int i=0;i<incisos.size();i++)
	            {
	            	if(incisos.get(i).get(llaveRol).equalsIgnoreCase("T"))
	            	{
	            		indiceTitular=i;
	            	}
	            }
	            List<Map<String,String>> temp    = new ArrayList<Map<String,String>>(0);
	            Map<String,String>       titular = incisos.get(indiceTitular);
	            temp.add(titular);
	            incisos.remove(indiceTitular);
	            temp.addAll(incisos);
	            incisos=temp;
	            ////// 2. ordenar //////
	            
	            ////// ordenar al titular //////
	            ////////////////////////////////
            }
	            
            //////////////////////////////////////////
            ////// mpolisit y tvalosit iterados //////
            int contador=1;
            for(Map<String,String>inciso:incisos)
            {
            	if(noTarificar==false)
            	{
		        	//////////////////////////////
		        	////// mpolisit iterado //////
            		paso = "Insertando relaci\u00F3n p\u00F3liza situaci\u00F3n";
		        	cotizacionDAO.movimientoMpolisit(
		        			cdunieco
		        			,cdramo
		        			,"W"          //estado
		        			,nmpoliza
		        			,contador+"" //nmsituac
		        			,"0"         //mnsuplem
		        			,"V"         //status
		        			,cdtipsit
		        			,null        //swreduci
		        			,"1"         //cdagrupa
		        			,"0"         //cdestado
		        			,renderFechas.parse(feini) //fefecsit
		        			,renderFechas.parse(feini) //fecharef
		        			,null        //cdgrupo
		        			,null        //nmsituaext
		        			,null        //nmsitaux
		        			,null        //nmsbsitext
		        			,"1"         //cdplan
		        			,"30"        //cdasegur
		        			,"I"         //accion
		        			);
		        	////// mpolisit iterado //////
		        	//////////////////////////////
            	}
                
                //////////////////////////////
                ////// tvalosit iterado //////
                
                ////// 1. tvalosit base //////
                Map<String,String>mapaValositIterado=new HashMap<String,String>(0);
                ////// 1. tvalosit base //////
                
                ////// 2. tvalosit desde form //////
                for(Entry<String,String>en:inciso.entrySet())
                {
                	// p a r a m e t r o s . p v _ o t v a l o r 
                	//0 1 2 3 4 5 6 7 8 9 0 1
                	String key=en.getKey();
                	String value=en.getValue();
                	if(key.length()>"parametros.pv_".length()
                			&&key.substring(0,"parametros.pv_".length()).equalsIgnoreCase("parametros.pv_"))
                	{
                		mapaValositIterado.put(key.substring("parametros.pv_".length()),value);
                	}
                }
                ////// 2. tvalosit desde form //////
                
                ////// 3. completar faltantes //////
                for(int i=1;i<=99;i++)
                {
                	String key="otvalor"+i;
                	if(i<10)
                	{
                		key="otvalor0"+i;
                	}
                	if(!mapaValositIterado.containsKey(key))
                	{
                		mapaValositIterado.put(key,null);
                	}
                }
                ////// 3. completar faltantes //////
                
                ////// 4. custom //////
            	try
            	{
            		paso = "Recuperando valores constantes de situaci\u00F3n";
            		Map<String,String>tvalositConst=cotizacionDAO.obtenerParametrosCotizacion(
            			ParametroCotizacion.TVALOSIT_CONSTANTE
            			,cdramo
            			,cdtipsit
            			,null
            			,null);
            		
            		if(tvalositConst!=null)
                	{
                		for(int i=1;i<=13;i++)
                		{
                			String key = tvalositConst.get(Utils.join("P",i,"CLAVE"));
                			String val = tvalositConst.get(Utils.join("P",i,"VALOR"));
                			if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(val))
                			{
	                			mapaValositIterado.put
	                			(
	                					Utils.join
	                					(
	                							"otvalor"
	                							,StringUtils.leftPad(key,2,"0")
	                					)
	                					,val
	                			);
                			}
                		}
                	}
            	}
            	catch(Exception ex)
            	{
            		logger.warn("Error sin impacto funcional al cotizar");
            	}
                	
            	if(cdtipsit.equals(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit()))
            	{
            		paso = "Recuperando tabuladores de gastos m\u00E9dicos";
            		mapaValositIterado.put("otvalor22",
            				cotizacionDAO.cargarTabuladoresGMIParche(mapaValositIterado.get("otvalor16"), "22")
            		);
            		mapaValositIterado.put("otvalor23",
            				cotizacionDAO.cargarTabuladoresGMIParche(mapaValositIterado.get("otvalor16"), "23")
            		);
            	}
                ////// 4. custom //////
                
            	paso = "Insertando valores adicionales de situaci\u00F3n";
            	cotizacionDAO.movimientoTvalosit(
            			cdunieco
            			,cdramo
            			,"W"
            			,nmpoliza
            			,contador+"" //nmsituac
            			,"0"         //nmsuplem
            			,"V"         //status
            			,cdtipsit
            			,mapaValositIterado
            			,"I"         //accion
            			);
                ////// tvalosit iterado //////
                //////////////////////////////
                
                contador++;
            }
            ////// mpolisit y tvalosit iterados //////
            //////////////////////////////////////////
		        
            if(noTarificar==false)
			{
	            /////////////////////////////
	            ////// clonar personas //////
	            contador=1;
	            for(Map<String,String> inciso : incisos)
	            {
	            	paso = "Clonando incisos";
	                cotizacionDAO.clonarPersonas(
	                		cdelemen
	                		,cdunieco
	                		,cdramo
	                		,"W"
	                		,nmpoliza
	                		,contador+""
	                		,cdtipsit
	                		,fechaHoy
	                		,cdusuari
	                		,inciso.get("nombre")
	                		,inciso.get("nombre2")
	                		,inciso.get("apat")
	                		,inciso.get("amat")
	                		,inciso.containsKey(llaveSexo)?inciso.get(llaveSexo):llaveSexo
	                		,inciso.containsKey(llaveFenacimi)?
	                		renderFechas.parse(inciso.get(llaveFenacimi)):(
	                				llaveFenacimi.equalsIgnoreCase("DATE")?
	                						fechaHoy :
	                							renderFechas.parse(llaveFenacimi))
	                		,inciso.containsKey(llaveRol)?inciso.get(llaveRol):llaveRol
	                );
	                contador++;
	            }
	            ////// clonar personas //////
	            /////////////////////////////
	
				/**
				 * TODO: EVALUAR E IMPLEMENTAR CDPERSON TEMPORAL
				 */

				if (!consultasDAO.esProductoSalud(cdramo) && StringUtils.isBlank(cdpersonCli) && StringUtils.isNotBlank(cdideperCli)) {
					logger.debug("Persona proveniente de WS, Se importar�, Valor de cdperson en blanco, valor de cdIdeper: " + cdideperCli);
					
					
					/**
					 * TODO: EVALUAR E IMPLEMENTAR CDPERSON TEMPORAL
					 * PARA GUARDAR CLIENTE EN BASE DE DATOS DEL WS, se traslada codigo de comprar cotizacion a cotizacion pues pierde el mpersona cuando se recuperan cotizaciones
					 */
						if (Ramo.AUTOS_FRONTERIZOS.getCdramo()
								.equalsIgnoreCase(cdramo)
								|| Ramo.SERVICIO_PUBLICO.getCdramo()
										.equalsIgnoreCase(cdramo)
								|| Ramo.AUTOS_RESIDENTES.getCdramo()
										.equalsIgnoreCase(cdramo)) {

							logger.debug("<<<>>> Verificando que no se haya insertado el cliente anteriormente... ");
			    			boolean personaNueva =  true;
			    			String  newCdPerson   = null;
			    			
			    			try {
			    				String validarAsegurado = personasManager.validaExisteAseguradoSicaps(StringUtils.trim(cdideperCli));
			    				if(Integer.parseInt(validarAsegurado) > 0){
			    					personaNueva =  false;
			    					logger.debug("<<<>>> Se detecto persona importada anteriormente...");
			    					newCdPerson = validarAsegurado;
			    				}
			    				
							} catch (Exception e) {
								logger.error("Error al verificar existencia de Cliente externo");
							}
			    			
			    			if(personaNueva){
								String cdtipsitGS = consultasDAO.obtieneSubramoGS(cdramo, cdtipsit);
	
								ClienteGeneral clienteGeneral = new ClienteGeneral();
								// clienteGeneral.setRfcCli((String)aseg.get("cdrfc"));
								clienteGeneral.setRamoCli(Integer
										.parseInt(cdtipsitGS));
								clienteGeneral.setNumeroExterno(cdideperCli);
	
								ClienteGeneralRespuesta clientesRes = ice2sigsService
										.ejecutaWSclienteGeneral(
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												Ice2sigsService.Operacion.CONSULTA_GENERAL,
												clienteGeneral, null, false);
	
								if (clientesRes != null
										&& ArrayUtils.isNotEmpty(clientesRes
												.getClientesGeneral())) {
									ClienteGeneral cli = null;
	
									if (clientesRes.getClientesGeneral().length == 1) {
										logger.debug("Cliente unico encontrado en WS, guardando informacion del WS...");
										cli = clientesRes.getClientesGeneral()[0];
									} else {
										logger.error("Error, No se pudo obtener el cliente del WS. Se ha encontrado mas de Un elemento!");
									}
	
									if (cli != null) {
	
										/**
										 * TODO: EVALUAR E IMPLEMENTAR CDPERSON TEMPORAL
										 */
										
										// IR POR NUEVO CDPERSON:
										newCdPerson = personasDAO.obtenerNuevoCdperson();
	
										logger.debug("Insertando nueva persona, cdperson generado: " +newCdPerson);
										
										String usuarioCaptura =  null;
										
										if(usuarioSesion!=null){
											if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
												usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
											}else{
												usuarioCaptura = usuarioSesion.getCodigoPersona();
											}
										}
							    		
							    		String apellidoPat = "";
								    	if(StringUtils.isNotBlank(cli.getApellidopCli()) && !cli.getApellidopCli().trim().equalsIgnoreCase("null")){
								    		apellidoPat = cli.getApellidopCli();
								    	}
								    	
								    	String apellidoMat = "";
								    	if(StringUtils.isNotBlank(cli.getApellidomCli()) && !cli.getApellidomCli().trim().equalsIgnoreCase("null")){
								    		apellidoMat = cli.getApellidomCli();
								    	}
								    	
							    		Calendar calendar =  Calendar.getInstance();
							    		
							    		String sexo = "H"; //Hombre
								    	if(cli.getSexoCli() > 0){
								    		if(cli.getSexoCli() == 2) sexo = "M";
								    	}
								    	
								    	String tipoPersona = "F"; //Fisica
								    	if(cli.getFismorCli() > 0){
								    		if(cli.getFismorCli() == 2){
								    			tipoPersona = "M";
								    		}else if(cli.getFismorCli() == 3){
								    			tipoPersona = "S";
								    		}
								    	}
								    	
								    	if(cli.getFecnacCli()!= null){
								    		calendar.set(cli.getFecnacCli().get(Calendar.YEAR), cli.getFecnacCli().get(Calendar.MONTH), cli.getFecnacCli().get(Calendar.DAY_OF_MONTH));
								    	}
								    	
								    	
								    	Calendar calendarIngreso =  Calendar.getInstance();
								    	if(cli.getFecaltaCli() != null){
								    		calendarIngreso.set(cli.getFecaltaCli().get(Calendar.YEAR), cli.getFecaltaCli().get(Calendar.MONTH), cli.getFecaltaCli().get(Calendar.DAY_OF_MONTH));
								    	}
								    	
								    	String nacionalidad = "001";// Nacional
								    	if(StringUtils.isNotBlank(cli.getNacCli()) && !cli.getNacCli().equalsIgnoreCase("1")){
								    		nacionalidad = "002";
								    	}
								    	
								    	String codPosImp = cli.getCodposCli();
		                                if(StringUtils.isNotBlank(codPosImp) && codPosImp.length() == 4){
		                                    codPosImp = "0"+codPosImp;//Se agrega un cero a la izquierda del codigo postal en caso de que falte
		                                }
								    	
							    		//GUARDAR MPERSONA
							    		
										personasDAO.movimientosMpersona(newCdPerson, "1", cli.getNumeroExterno(), (cli.getFismorCli() == 1) ? cli.getNombreCli() : cli.getRazSoc()
												, "1", tipoPersona, sexo, calendar.getTime(), cli.getRfcCli(), cli.getMailCli(), null
												, (cli.getFismorCli() == 1) ? apellidoPat : "", (cli.getFismorCli() == 1) ? apellidoMat: "", calendarIngreso.getTime(), nacionalidad, cli.getCanconCli() <= 0 ? "0" : (Integer.toString(cli.getCanconCli()))
												, null, null, null, null, null, null, Integer.toString(cli.getSucursalCli()), usuarioCaptura, Constantes.INSERT_MODE);
										
										String edoAdosPos2 = Integer.toString(cli.getEstadoCli());
						    			if(edoAdosPos2.length() ==  1){
						    				edoAdosPos2 = "0"+edoAdosPos2;
						    			}
						    			
						    			HashMap<String,String> paramsMunCol = new HashMap<String, String>();
		                                paramsMunCol.put("pv_cdpostal_i", codPosImp);
		                                paramsMunCol.put("pv_cdedo_i",    edoAdosPos2);
		                                paramsMunCol.put("pv_dsmunici_i", cli.getMunicipioCli());
		                                paramsMunCol.put("pv_dscoloni_i", cli.getColoniaCli());
		                                
		                                Map<String,String> munycol= personasManager.obtieneMunicipioYcolonia(paramsMunCol);
							    		
							    		//GUARDAR DOMICILIO
						    			
						    			personasDAO.movimientosMdomicil(newCdPerson, "1", cli.getCalleCli(), cli.getTelefonoCli()
							    				, codPosImp, codPosImp+edoAdosPos2, munycol.get("CDMUNICI"), munycol.get("CDCOLONI")
							    				, cli.getNumeroCli(), null
							    				,"1" // domicilio personal default
												,usuarioCaptura
												,Constantes.SI  //domicilio activo
												,Constantes.INSERT_MODE);
	
						    			//GUARDAR TVALOPER
						    			
						    			personasDAO.movimientosTvaloper("1", newCdPerson, cli.getCveEle(), cli.getPasaporteCli(), null, null, null,
						    				null, null, cli.getOrirecCli(), null, null,
						    				cli.getNacCli(), null, null, null, null, 
						    				null, null, null, null, (cli.getOcuPro() > 0) ? Integer.toString(cli.getOcuPro()) : "0", 
						    				null, null, null, null, cli.getCurpCli(), 
						    				null, null, null, null, null, 
						    				null, null, null, null, null, 
						    				null, null, cli.getTelefonoCli(), cli.getMailCli(), null, 
						    				null, null, null, null, null, 
						    				null, null, null, null, null,
		    		    					cli.getFaxCli(), cli.getCelularCli());
						    			
									}
								}
			    			}
			    			cdpersonCli = newCdPerson;
			    			nmorddomCli = "1";
						}
				}
	            
	            ////// mpoliper contratante recuperado //////
	            if(StringUtils.isNotBlank(cdpersonCli))
	            {
	            	paso = "Insertando relaci\u00F3n p\u00F3liza persona";
	            	cotizacionDAO.movimientoMpoliper(
	            			cdunieco
	            			,cdramo
	            			,"W"
	            			,nmpoliza
	            			,"0"         //nmsituac
	            			,"1"         //cdrol
	            			,cdpersonCli //cdperson
	            			,"0"         //nmsuplem
	            			,"V"         //status
	            			,nmorddomCli //nmorddom
	            			,null        //swreclam
	            			,"I"         //accion
	            			,"S"         //swexiper
	            			);
	            }
	            ////// mpoliper contratante recuperado //////
	            
	            paso = "Aplicando ajustes de producto";
	            cotizacionDAO.aplicarAjustesCotizacionPorProducto(
	            		cdunieco
	            		,cdramo
	            		,"W"
	            		,nmpoliza
	            		,cdtipsit
	            		,"I"
	            		);
	            
	            ////////////////////////
	            ////// coberturas //////
	            /*////////////////////*/
	            paso = "Ejecutando tarificaci\u00F3n";
	            cotizacionDAO.valoresPorDefecto(
	            		cdunieco
	            		,cdramo
	            		,"W"
	            		,nmpoliza
	            		,"0"
	            		,"0"
	            		,"TODO"
	            		,"1"
	            		);
	            /*////////////////////*/
	            ////// coberturas //////
	            ////////////////////////
		    }
            
            ///////////////////////////////////
            ////// Generacion cotizacion //////
            /*///////////////////////////////*/
            
            ////// 0.1 verificamos que el plan tenga las coberturas que le corresponden //////
            paso = "Validando coberturas del plan";
//            for(int i=0;i<incisos.size();i++)
//            {
//            	if(StringUtils.isNotBlank(incisos.get(i).get("estado")) && StringUtils.isNotBlank(incisos.get(i).get("nmsuplem")))
            	if(StringUtils.isNotBlank(cdramo) && cdramo.equals("5"))
            	{
                    String planValido = cotizacionDAO.validandoCoberturasPLan(
                  		  cdunieco
                  		 ,cdramo
                  		 ,"W"//estado
                  		 ,nmpoliza
                  		 ,"0"//nmsuplem
                  		);
                    	logger.debug("Amis Y Modelo con Irregularidades en coberturas: "+planValido);
                  
      	            if(StringUtils.isNotBlank(planValido))
      	            {
      	            	String mensajeAPantalla = "Por el momento no es posible cotizar para esta unidad, el paquete de cobertura Prestigio, Amplio  y Limitado, le pedimos por favor ponerse en contacto con su ejecutivo de ventas.";
      	            	resp.getSmap().put("msnPantalla" , mensajeAPantalla);
      	            	String mensajeACorreo= "Se le notifica que no ha sido posible cotizar la solicitud "+nmpoliza+" del producto de autom&oacute;viles en el paquete de cobertura prestigio, amplia y limitada:\n" + planValido;
      	            	
      	            	String [] listamails = cotizacionDAO.obtenerCorreosReportarIncidenciasPorTipoSituacion(cdramo);
      	            	//{"XXXX@XXX.com.mx","YYYYY@YYYY.com.mx"};";
      	            	String [] adjuntos = new String[0];
      	            	boolean mailSend = mailService.enviaCorreo(listamails, null, null, "Reporte de Tarifa incompleta - SICAPS", mensajeACorreo, adjuntos, true);
      	        		if(!mailSend)
      	        		{
      	        			throw new ApplicationException("4");
      	        		}
      	        		else
      	        		{
      	        			//correo envio exitosamente
      	    			}
      	            }
            	}
//            }
            
            /*///////////////////////////////*/
            ////// Generacion cotizacion //////
            ///////////////////////////////////
            
        	return resp;
    	}
    	catch(Exception ex)
    	{
    		Utils.generaExcepcion(ex, paso);
    	}
    	
    	logger.debug(Utils.log(
    		 "\n@@@@@@ ",resp
    		,"\n@@@@@@ cotizar @@@@@@"
   			,"\n@@@@@@@@@@@@@@@@@@@@@"
   			));
    	
    	resp.setRespuesta("El n\u00FAmero de p\u00F3liza es : "+nmpoliza);
    	return resp;
    }

    @Override
    public ManagerRespuestaSlistSmapVO cotizarContinuacion(String cdusuari,String cdunieco,String cdramo,String cdelemen,String cdtipsit,String nmpoliza,  boolean flagMovil)throws Exception
    {
	logger.debug(Utils.log(
			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
			,"\n@@@@@@ cotizarContinuacion @@@@@@"
			));
	ManagerRespuestaSlistSmapVO resp=new ManagerRespuestaSlistSmapVO(true);
	resp.setSmap(new HashMap<String,String>());
	resp.getSmap().put("nmpoliza" , nmpoliza);
	String paso = "cotizarContinuacion";
	try
	{
		 paso = "Recuperando resultados de cotizaci\u00F3n";
	     List<Map<String,String>> listaResultados=cotizacionDAO.cargarResultadosCotizacion(
	     		 cdusuari
	     		,cdunieco
	     		,cdramo
	     		,"W"
	     		,nmpoliza
	     		,cdelemen
	     		,cdtipsit
	     		);
	     logger.debug("listaResultados: "+listaResultados);
	     resp.setSlist(listaResultados);
		
		paso = "Cotizando";
        ////////////////////////////////
        ////// Agrupar resultados //////
        /*
        NMSUPLEM=0,
		FEFECSIT=13/01/2014,
		NMPOLIZA=3853,
		MNPRIMA=4571.92,           <--2
		CDPERPAG=7,                <--1
		DSPLAN=Plus 500,           <--3
		FEVENCIM=13/01/2015,
		STATUS=V,
		NMSITUAC=3,
		ESTADO=W,
		DSPERPAG=DXN Catorcenal,   <--(1)
		CDCIAASEG=20,
		CDIDENTIFICA=2,
		CDTIPSIT=SL,
		FEEMISIO=13/01/2014,
		CDUNIECO=1,
		CDRAMO=2,
		CDPLAN=M,                  <--(3)
		DSUNIECO=PUEBLA
         */
        
        paso = "Agrupando tarifa";
        
        ////// 1. encontrar planes, formas de pago y algun nmsituac//////
        Map<String,String>formasPago = new LinkedHashMap<String,String>();
        Map<String,String>planes     = new LinkedHashMap<String,String>();
        String nmsituac="";
        for(Map<String,String>res:listaResultados)
        {
        	String cdperpag = res.get("CDPERPAG");
        	String dsperpag = res.get("DSPERPAG");
        	String cdplan   = res.get("CDPLAN");
        	String dsplan   = res.get("DSPLAN");
        	if(!formasPago.containsKey(cdperpag))
        	{
        		formasPago.put(cdperpag,dsperpag);
        	}
        	if(!planes.containsKey(cdplan))
        	{
        		planes.put(cdplan,dsplan);
        	}
        	nmsituac=res.get("NMSITUAC");
        }
        logger.debug("formas de pago: "+formasPago);
        logger.debug("planes: "+planes);
        ////// 1. encontrar planes y formas de pago //////
        
        ////// 2. crear formas de pago //////
        List<Map<String,String>>tarifas=new ArrayList<Map<String,String>>();
        for(Entry<String,String>formaPago:formasPago.entrySet())
        {
        	Map<String,String>tarifa=new HashMap<String,String>();
        	tarifa.put("CDPERPAG",formaPago.getKey());
        	tarifa.put("DSPERPAG",formaPago.getValue());
        	tarifa.put("NMSITUAC",nmsituac);
        	tarifas.add(tarifa);
        }
        logger.debug("tarifas despues de formas de pago: "+tarifas);
        ////// 2. crear formas de pago //////
        
        ////// 3. crear planes //////
        for(Map<String,String>tarifa:tarifas)
        {
        	for(Entry<String,String>plan:planes.entrySet())
            {
            	tarifa.put("CDPLAN"+plan.getKey(),plan.getKey());
            	tarifa.put("DSPLAN"+plan.getKey(),plan.getValue());
            }
        }
        logger.debug("tarifas despues de planes: "+tarifas);
        ////// 3. crear planes //////
        
        ////// 4. crear primas //////
        for(Map<String,String>res:listaResultados)
        {
        	String cdperpag = res.get("CDPERPAG");
        	String mnprima  = res.get("MNPRIMA");
        	String cdplan   = res.get("CDPLAN");
        	for(Map<String,String>tarifa:tarifas)
            {
        		if(tarifa.get("CDPERPAG").equals(cdperpag))
        		{
        			if(tarifa.containsKey("MNPRIMA"+cdplan))
        			{
        				logger.debug("ya hay prima para "+cdplan+" en "+cdperpag+": "+tarifa.get("MNPRIMA"+cdplan));
        				tarifa.put("MNPRIMA"+cdplan,((Double)Double.parseDouble(tarifa.get("MNPRIMA"+cdplan))+(Double)Double.parseDouble(mnprima))+"");
        				logger.debug("nueva: "+tarifa.get("MNPRIMA"+cdplan));
        			}
        			else
        			{
        				logger.debug("primer prima para "+cdplan+" en "+cdperpag+": "+mnprima);
        				tarifa.put("MNPRIMA"+cdplan,mnprima);
        			}
        		}
            }
        }
        logger.debug("tarifas despues de primas: "+tarifas);
        
        resp.setSlist(tarifas);
        ////// 4. crear primas //////
        
        ////// Agrupar resultados //////
        ////////////////////////////////
        
        ///////////////////////////////////
        ////// columnas para el grid //////
        List<ComponenteVO>tatriPlanes=new ArrayList<ComponenteVO>();
        
        ////// 1. forma de pago //////
        ComponenteVO tatriCdperpag=new ComponenteVO();
    	tatriCdperpag.setType(ComponenteVO.TIPO_GENERICO);
    	tatriCdperpag.setLabel("CDPERPAG");
    	tatriCdperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
    	tatriCdperpag.setNameCdatribu("CDPERPAG");
    	
    	/*Map<String,String>mapaCdperpag=new HashMap<String,String>();
    	mapaCdperpag.put("OTVALOR10","CDPERPAG");
    	tatriCdperpag.setMapa(mapaCdperpag);*/
    	tatriPlanes.add(tatriCdperpag);
    	
    	ComponenteVO tatriDsperpag=new ComponenteVO();
    	tatriDsperpag.setType(ComponenteVO.TIPO_GENERICO);
    	tatriDsperpag.setLabel("Forma de pago");
    	tatriDsperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
    	tatriDsperpag.setNameCdatribu("DSPERPAG");
    	tatriDsperpag.setColumna(Constantes.SI);
    	
    	/*Map<String,String>mapaDsperpag=new HashMap<String,String>();
    	mapaDsperpag.put("OTVALOR08","S");
    	mapaDsperpag.put("OTVALOR10","DSPERPAG");
    	tatriDsperpag.setMapa(mapaDsperpag);*/
    	tatriPlanes.add(tatriDsperpag);
    	////// 1. forma de pago //////
    	
    	////// 2. nmsituac //////
    	ComponenteVO tatriNmsituac=new ComponenteVO();
    	tatriNmsituac.setType(ComponenteVO.TIPO_GENERICO);
    	tatriNmsituac.setLabel("NMSITUAC");
    	tatriNmsituac.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
    	tatriNmsituac.setNameCdatribu("NMSITUAC");
    	
    	/*Map<String,String>mapaNmsituac=new HashMap<String,String>();
    	mapaNmsituac.put("OTVALOR10","NMSITUAC");
    	tatriNmsituac.setMapa(mapaNmsituac);*/
    	tatriPlanes.add(tatriNmsituac);
    	////// 2. nmsituac //////
    	
    	////// 2. planes //////
        for(Entry<String,String>plan:planes.entrySet())
        {
        	////// prima
        	ComponenteVO tatriPrima=new ComponenteVO();
        	tatriPrima.setType(ComponenteVO.TIPO_GENERICO);
        	tatriPrima.setLabel(plan.getValue());
        	tatriPrima.setTipoCampo(ComponenteVO.TIPOCAMPO_PORCENTAJE);
        	tatriPrima.setColumna(Constantes.SI);
        	tatriPrima.setNameCdatribu("MNPRIMA"+plan.getKey());
        	tatriPrima.setRenderer("function(v)"
        			+ "{"
        			+ "    debug('valor:',v);"
        			+ "    v=v.toFixed(2);"
        			+ "    debug('valor fixed:',v);"
        			+ "    var v2='';"
        			+ "    var ultimoPunto=-3;"
        			+ "    for(var i=(v+'').length-1;i>=0;i--)"
        			+ "    {"
        			+ "        var digito=(v+'').charAt(i);"
        			+ "        if(digito=='.')"
        			+ "        {"
        			+ "            ultimoPunto=-2;"
        			+ "        }"
        			+ "        if(ultimoPunto>-3)"
        			+ "        {"
        			+ "            ultimoPunto=ultimoPunto+1;"
        			+ "        }"
        			+ "        if(ultimoPunto%3==0&&ultimoPunto>0)"
        			+ "        {"
        			+ "            digito=digito+',';"
        			+ "        }"
        			+ "        v2=digito+v2;"
        			+ "        if(i==0)"
        			+ "        {"
        			+ "            v2='$ '+v2;"
        			+ "        }"
        			+ "    }"
        			+ "    return v2;"
        			+ "}");
        	
        	/*Map<String,String>mapaPlan=new HashMap<String,String>();
        	mapaPlan.put("OTVALOR08","S");
        	mapaPlan.put("OTVALOR09","MONEY");
        	mapaPlan.put("OTVALOR10","MNPRIMA"+plan.getKey());
        	tatriPrima.setMapa(mapaPlan);*/
        	tatriPlanes.add(tatriPrima);
        	
        	////// cdplan
        	ComponenteVO tatriCdplan=new ComponenteVO();
         	tatriCdplan.setType(ComponenteVO.TIPO_GENERICO);
         	tatriCdplan.setLabel("CDPLAN"+plan.getKey());
         	tatriCdplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
         	tatriCdplan.setNameCdatribu("CDPLAN"+plan.getKey());
         	tatriCdplan.setColumna(ComponenteVO.COLUMNA_OCULTA);
         	
         	/*Map<String,String>mapaCdplan=new HashMap<String,String>();
         	//mapaCdplan.put("OTVALOR08","H");
         	mapaCdplan.put("OTVALOR10","CDPLAN"+plan.getKey());
         	tatriCdplan.setMapa(mapaCdplan);*/
         	tatriPlanes.add(tatriCdplan);
         	
         	////// dsplan
         	ComponenteVO tatriDsplan=new ComponenteVO();
         	tatriDsplan.setType(ComponenteVO.TIPO_GENERICO);
         	tatriDsplan.setLabel("DSPLAN"+plan.getKey());
         	tatriDsplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
         	tatriDsplan.setNameCdatribu("DSPLAN"+plan.getKey());
         	
         	/*Map<String,String>mapaDsplan=new HashMap<String,String>();
         	//mapaDsplan.put("OTVALOR08","H");
         	mapaDsplan.put("OTVALOR10","DSPLAN"+plan.getKey());
         	tatriDsplan.setMapa(mapaDsplan);*/
         	tatriPlanes.add(tatriDsplan);
        }
        ////// 2. planes //////
        
        GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
        try
        {
        	Map<String,Object> session = ActionContext.getContext().getSession();
        	gc.setEsMovil(session!=null&&session.containsKey("ES_MOVIL")&&((Boolean)session.get("ES_MOVIL"))==true);
        }
        catch(Exception ex)
        {
        	logger.warn("Warning! error manejado al intentar acceder a sesion",ExceptionUtils.getStackTrace(ex));
        }
        if(!gc.isEsMovil()&&flagMovil)
        {
        	gc.setEsMovil(true);
        }
        gc.genera(tatriPlanes);
        
        String columnas = gc.getColumns().toString();
        // c o l u m n s : [
        //0 1 2 3 4 5 6 7 8
        resp.getSmap().put("columnas",columnas.substring(8));
        
        String fields = gc.getFields().toString();
        // f i e l d s : [
        //0 1 2 3 4 5 6 7
        resp.getSmap().put("fields",fields.substring(7));
	}
	catch(Exception ex)
	{
		Utils.generaExcepcion(ex, paso);
	}
	
	logger.debug(Utils.log(
		 "\n@@@@@@ ",resp
		,"\n@@@@@@ cotizar @@@@@@"
			,"\n@@@@@@@@@@@@@@@@@@@@@"
			));
	
	resp.setRespuesta("El n\u00FAmero de p\u00F3liza es : "+nmpoliza);
	return resp;
}
    
    
    @Override
    public String procesoComprarCotizacion(String cdunieco, String cdramo, String nmpoliza, String cdtipsit,
			String fechaInicio, String fechaFin, String ntramite, String cdagenteExt, String cdciaaguradora,
			String cdplan, String cdperpag, String cdusuari, String cdsisrol, String cdelemen,
			boolean esFlotilla, String tipoflot, String cdpersonCli, String cdideperCli,
			String nombreReporteCotizacion, String nombreReporteCotizacionFlot, UserVO usuarioSesion,
			String swrenovacion, String sucursal, String ramo, String poliza, String porparti, String porredau) throws Exception {
    	
    	String paso = null;
    	
    	Date fechaHoy = new Date();
    	
    	try {
    		
        	//datos de usuario
        	paso = "Recuperando datos de usuario";
        	DatosUsuario userData = cotizacionDAO.cargarInformacionUsuario(cdusuari, cdtipsit);
        	String cdperson              = userData.getCdperson();
    		String cdagente              = userData.getCdagente();
    		String nmcuadro              = userData.getNmcuadro();
        	
    		//datos de agente
    		paso = "Recuperando datos de agente";
    		Map<String,String> datosAgenteExterno = cotizacionDAO.obtenerDatosAgente(cdagenteExt, cdramo);
    		cdagente = datosAgenteExterno.get("CDAGENTE");
    		nmcuadro = datosAgenteExterno.get("NMCUADRO");
        	
        	//detalle suplemento
    		paso = "Insertando suplemento";
            Calendar calendarHoy = Calendar.getInstance();
            
            cotizacionDAO.movimientoTdescsup(cdunieco, cdramo, "W", nmpoliza
        			,"0", "1", calendarHoy.getTime(), null,calendarHoy.getTime(), 
        			null, null, cdusuari, null, null,cdperson, Constantes.INSERT_MODE);
            
        	
        	//maestro historico poliza
            paso = "Insertando hist�rico de p�liza";
            
            SimpleDateFormat renderHora = new SimpleDateFormat("HH:mm");
            cotizacionDAO.movimientoMsupleme(
            		cdunieco, cdramo, "W", nmpoliza, "0", 
            		renderFechas.parse(fechaInicio), renderHora.format(calendarHoy.getTime()), 
            		renderFechas.parse(fechaFin), renderHora.format(calendarHoy.getTime()), 
            		null, "0", null, null, null, null, null, null, null, null, null, null, null, "I");
        	
        	//mpoliage
            paso = "Ligando la poliza al agente";
        		String cesionComision = porredau;
        		Map<String,String> tipoSituacion = cotizacionDAO.cargarTipoSituacion(cdramo, cdtipsit);
        		
        		if(tipoSituacion.get("SITUACION").equals("AUTO")) {
        			cesionComision = cotizacionDAO.cargarPorcentajeCesionComisionAutos(cdunieco,cdramo,"W",nmpoliza);
        		}
                
                cotizacionDAO.movimientoMpoliage(cdunieco, cdramo, "W", nmpoliza, 
            			cdagente, "0", "V", "1", cesionComision, nmcuadro, null, 
            			"I", StringUtils.isNotBlank(ntramite) ? ntramite : null, porparti);
        	
            // Actualizando la cesion de comision:
            paso = "Actualizando la cesion de comision";
            try {
            	cotizacionDAO.actualizaCesionComision(cdunieco, cdramo, "W", nmpoliza);
                logger.info("El procedimiento de actualizacion de cesion de comision fue invocado exitosamente");
            } catch(Exception ex) {
    			logger.warn(Utils.join("Hubo un error al actualizar cesion de comision:", ExceptionUtils.getStackTrace(ex)));
    		}
            
        	
        	//comprar
            paso = "Generando poliza - Insertando inciso definitivo (comprar cotizacion)";
            cotizacionDAO.procesaIncisoDefinitivo(cdunieco, cdramo, "W", nmpoliza, "0", 
            		cdelemen, cdperson, cdciaaguradora, cdplan, cdperpag);
        	
            //acutalizar/generar tramite
            paso = "Actualizando y generando tr�mite " + ntramite;
            
    		//actualizar tramite
            
    		if(StringUtils.isNotBlank(ntramite)) {
    			
    			paso = "Actualizando el tr�mite";
    			
    			mesaControlDAO.actualizarNmsoliciTramite(ntramite, nmpoliza);
            	logger.debug("se inserta detalle nuevo");
            	
            	mesaControlDAO.movimientoDetalleTramite(
            			ntramite, new Date(), null
            			,Utils.join("Se guard\u00f3 una cotizaci\u00f3n nueva para el tr\u00e1mite: ",nmpoliza)
            			,cdusuari, null, cdsisrol,"S", null, null
            			,EstatusTramite.PENDIENTE.getCodigo(),false
            			);
        		
    		} else { //se genera un tramite
    			
    			paso = "Recuperando datos de flujo de emisi\u00f3n";
    			logger.debug(paso);
    			
    			String tipoProcesoParaRecuperarFlujo = "I";
        		if (StringUtils.isNotBlank(tipoflot)) {
        			tipoProcesoParaRecuperarFlujo = tipoflot;
        		}
        		
        		Map<String,String> datosFlujo = null;
        		String cdtiptra = null;
        		
        		if ("S".equals(swrenovacion)) {
        			datosFlujo = consultasDAO.recuperarDatosFlujoRenovacion(cdramo,tipoProcesoParaRecuperarFlujo);
        			cdtiptra   = TipoTramite.RENOVACION.getCdtiptra();
        		} else { // es emision
        			datosFlujo = consultasDAO.recuperarDatosFlujoEmision(cdramo,tipoProcesoParaRecuperarFlujo);
        			cdtiptra   = TipoTramite.POLIZA_NUEVA.getCdtiptra();
        		}
    			
    			paso = "Generando el tr\u00e1mite";
    			logger.debug(paso);
    			
            	ntramite = mesaControlDAO.movimientoMesaControl(
            			cdunieco,
            			cdramo,
            			"W",
            			"0",
            			"0", 
            			cdunieco,
            			cdunieco,
            			cdtiptra,
            			fechaHoy,
            			cdagente,
            			"",
            			"", 
            			fechaHoy,
            			EstatusTramite.PENDIENTE.getCodigo(),
            			"Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente",
            			nmpoliza,
            			cdtipsit,
            			cdusuari,
            			cdsisrol,
            			null,
            			datosFlujo.get("cdtipflu"),
            			datosFlujo.get("cdflujomc"),
            			null,
            			TipoEndoso.EMISION_POLIZA.getCdTipSup().toString(),
            			sucursal,
            			ramo,
            			poliza,
            			false,
            			null
            	);
            	
            	RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
            	        cdusuari,
            	        cdsisrol,
            	        ntramite,
            	        EstatusTramite.PENDIENTE.getCodigo(),
            	        "Se guard\u00f3 un nuevo tr\u00e1mite en mesa de control desde cotizaci\u00f3n de agente",
            	        null,  // cdrazrecha
            	        null,  // cdusuariDes
            	        null,  // cdsisrolDes
            	        true,  // permisoAgente
            	        false, // porEscalamiento
            	        fechaHoy,
            	        false  // sinGrabarDetalle
            	        );
            	logger.debug(despacho.getMessage());
            	try {
	            	cotizacionDAO.grabarEvento(new StringBuilder("\nCotizar tramite grupo"), 
	            			Constantes.MODULO_EMISION, Constantes.EVENTO_COMPRAR_TRAMITE_MC, new Date(), cdusuari, cdsisrol, ntramite, 
	            			cdunieco, cdramo, "W", nmpoliza, nmpoliza, 
	            			cdagente, null, null, null);
	            } catch(Exception ex) {
	            	logger.error("Error al grabar evento, sin impacto funcional", ex);
	            }
            }
        	
        	//generar cotizacion
        	if(!cdramo.equals(Ramo.SERVICIO_PUBLICO.getCdramo()) && (!esFlotilla||"P".equals(tipoflot))) {
        		
        		paso = "Generando la cotizaci�n";
        		
                File carpeta=new File(rutaDocumentosPoliza+"/"+ntramite);
                if(!carpeta.exists()) {
                	if(!carpeta.mkdir()) {
                		throw new Exception("Error al crear la carpeta");
                	}
                }
                
                StringBuilder urlReporteCotizacion=new StringBuilder()
                       .append(rutaServidorReports)
                       .append("?p_unieco=")  .append(cdunieco)
                       .append("&p_ramo=")    .append(cdramo)
                       .append("&p_subramo=") .append(cdtipsit)
                       .append("&p_estado=")  .append("'W'")
                       .append("&p_poliza=")  .append(nmpoliza)
                       .append("&p_suplem=")  .append("0")
                       .append("&p_cdplan=")  .append(cdplan)
                       .append("&p_plan=")    .append(cdplan)
                       .append("&p_perpag=")  .append(cdperpag)
                       .append("&p_ntramite=").append(ntramite)
                       .append("&p_cdusuari=").append(cdusuari)
                       .append("&userid=")    .append(passServidorReports);
                
                if(!esFlotilla) {
                	urlReporteCotizacion.append("&report=").append(nombreReporteCotizacion);
                } else {
                	urlReporteCotizacion.append("&report=").append(nombreReporteCotizacionFlot);
                }
                
                urlReporteCotizacion.append("&destype=cache").append("&desformat=PDF").append("&ACCESSIBLE=YES").append("&paramform=no");
                
                String nombreArchivoCotizacion="cotizacion.pdf";
                String pathArchivoCotizacion=new StringBuilder()
            					.append(rutaDocumentosPoliza)
            					.append("/").append(ntramite)
            					.append("/").append(nombreArchivoCotizacion)
            					.toString();
                HttpUtil.generaArchivo(urlReporteCotizacion.toString(), pathArchivoCotizacion);
                
                mesaControlDAO.guardarDocumento(cdunieco, cdramo, "W",
            			"0", "0", new Date(), nombreArchivoCotizacion,
            			"COTIZACI\u00f3N", nmpoliza, ntramite, "1",
            			null, null, "1", null, null, null, null, false);
            }
        	
    		if(StringUtils.isNotBlank(cdpersonCli)){
    			
    			paso = "Guardando el cliente";
    			
    			cotizacionDAO.borrarMpoliperTodos(cdunieco, cdramo, "W", nmpoliza);
    			
    			cotizacionDAO.movimientoMpoliper(cdunieco, cdramo, "W", nmpoliza,
    					"0", "1", cdpersonCli, "0", "V", "1", null, "I", "S");
    			
    		} else if(StringUtils.isNotBlank(cdideperCli)){
    			
    			paso = "Guardando el cliente en Base de Datos del WS";
    			
    			logger.debug("Persona proveniente de WS, Valor de cdperson en blanco, valor de cdIdeper: " + cdideperCli);
    			
    			/**
    	    	 * PARA GUARDAR CLIENTE EN BASE DE DATOS DEL WS 
    	    	 */
    	    	if(Ramo.AUTOS_FRONTERIZOS.getCdramo().equalsIgnoreCase(cdramo) 
    		    		|| Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo)
    		    		|| Ramo.AUTOS_RESIDENTES.getCdramo().equalsIgnoreCase(cdramo)) {
    	    		
    	    		
    	    		logger.debug("<<<>>> Verificando que no se haya insertado el cliente anteriormente... ");
	    			boolean personaNueva =  true;
	    			String  newCdPerson   = null;
	    			
	    			try {
	    				String validarAsegurado = personasManager.validaExisteAseguradoSicaps(StringUtils.trim(cdideperCli));
	    				if(Integer.parseInt(validarAsegurado) > 0){
	    					personaNueva =  false;
	    					logger.debug("<<<>>> Se detecto persona importada anteriormente...");
	    					newCdPerson = validarAsegurado;
	    				}
	    				
					} catch (Exception e) {
						logger.error("Error al verificar existencia de Cliente externo");
					}
	    			
	    			if(personaNueva){
	    		    	String cdtipsitGS = consultasDAO.obtieneSubramoGS(cdramo, cdtipsit);
	    		    	
	    		    	ClienteGeneral clienteGeneral = new ClienteGeneral();
	    		    	//clienteGeneral.setRfcCli((String)aseg.get("cdrfc"));
	    		    	clienteGeneral.setRamoCli(Integer.parseInt(cdtipsitGS));
	    		    	clienteGeneral.setNumeroExterno(cdideperCli);
	    		    	
	    		    	ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
	    		    	
	    		    	if(clientesRes !=null && ArrayUtils.isNotEmpty(clientesRes.getClientesGeneral())){
	    		    		ClienteGeneral cli = null;
	    		    		
	    		    		if(clientesRes.getClientesGeneral().length == 1){
	    		    			logger.debug("Cliente unico encontrado en WS, guardando informacion del WS...");
	    		    			cli = clientesRes.getClientesGeneral()[0];
	    		    		}else {
	    		    			logger.error("Error, No se pudo obtener el cliente del WS. Se ha encontrado mas de Un elemento!");
	    		    		}
	    		    		
	    		    		if(cli != null){
	    		    			
	    		    			//IR POR NUEVO CDPERSON:
	    			    		newCdPerson = personasDAO.obtieneCdperson();
	    			    		
	    			    		logger.debug("Insertando nueva persona, cdperson generado: " + newCdPerson);
	    			    		
	    			    		String usuarioCaptura =  null;
								
								if(usuarioSesion!=null){
									if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
										usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
									}else{
										usuarioCaptura = usuarioSesion.getCodigoPersona();
									}
									
								}
	    			    		
	    			    		String apellidoPat = "";
	    				    	if(StringUtils.isNotBlank(cli.getApellidopCli()) && !cli.getApellidopCli().trim().equalsIgnoreCase("null")){
	    				    		apellidoPat = cli.getApellidopCli();
	    				    	}
	    				    	
	    				    	String apellidoMat = "";
	    				    	if(StringUtils.isNotBlank(cli.getApellidomCli()) && !cli.getApellidomCli().trim().equalsIgnoreCase("null")){
	    				    		apellidoMat = cli.getApellidomCli();
	    				    	}
	    				    	
	    			    		Calendar calendar =  Calendar.getInstance();
	    			    		
	    			    		String sexo = "H"; //Hombre
	    				    	if(cli.getSexoCli() > 0){
	    				    		if(cli.getSexoCli() == 2) sexo = "M";
	    				    	}
	    				    	
	    				    	String tipoPersona = "F"; //Fisica
	    				    	if(cli.getFismorCli() > 0){
	    				    		if(cli.getFismorCli() == 2){
	    				    			tipoPersona = "M";
	    				    		}else if(cli.getFismorCli() == 3){
	    				    			tipoPersona = "S";
	    				    		}
	    				    	}
	    				    	/*
	    				    	String nacionalidad = "001";// Nacional
	    				    	if(StringUtils.isNotBlank(cli.getNacCli()) && !cli.getNacCli().equalsIgnoreCase("1")){
	    				    		nacionalidad = "002";
	    				    	}
	    				    	*/
	    				    	
	    				    	if(cli.getFecnacCli()!= null){
	    				    		calendar.set(cli.getFecnacCli().get(Calendar.YEAR), cli.getFecnacCli().get(Calendar.MONTH), cli.getFecnacCli().get(Calendar.DAY_OF_MONTH));
	    				    	}
	    				    	
	    				    	
	    				    	Calendar calendarIngreso =  Calendar.getInstance();
	    				    	if(cli.getFecaltaCli() != null){
	    				    		calendarIngreso.set(cli.getFecaltaCli().get(Calendar.YEAR), cli.getFecaltaCli().get(Calendar.MONTH), cli.getFecaltaCli().get(Calendar.DAY_OF_MONTH));
	    				    	}
	    				    	
	    				    	String nacionalidad = "001";// Nacional
	    				    	if(StringUtils.isNotBlank(cli.getNacCli()) && !cli.getNacCli().equalsIgnoreCase("1")){
	    				    		nacionalidad = "002";
	    				    	}
	    				    	
	    			    		//GUARDAR MPERSONA
	    						personasDAO.movimientosMpersona(newCdPerson, "1", cli.getNumeroExterno(),
	    								(cli.getFismorCli() == 1) ? cli.getNombreCli() : cli.getRazSoc(),
	    								"1", tipoPersona, sexo, calendar.getTime(), cli.getRfcCli(), cli.getMailCli(),
	    								null, (cli.getFismorCli() == 1) ? apellidoPat : "", (cli.getFismorCli() == 1) ? apellidoMat : "", calendarIngreso.getTime(), nacionalidad,
	    								cli.getCanconCli() <= 0 ? "0" : (Integer.toString(cli.getCanconCli())),
	    								null, null, null, null, null, null, String.valueOf(cli.getSucursalCli()), usuarioCaptura, Constantes.INSERT_MODE);
	    			    		
	    			    		//GUARDAR DOMICILIO
	    			    		String edoAdosPos2 = Integer.toString(cli.getEstadoCli());
	    		    			if(edoAdosPos2.length() ==  1){
	    		    				edoAdosPos2 = "0"+edoAdosPos2;
	    		    			}
	    		    			
	    		    			String codPosImp = cli.getCodposCli();
	                            if(StringUtils.isNotBlank(codPosImp) && codPosImp.length() == 4){
	                                codPosImp = "0"+codPosImp;//Se agrega un cero a la izquierda del codigo postal en caso de que falte
	                            }
	                            
	                            HashMap<String,String> paramsMunCol = new HashMap<String, String>();
	                            paramsMunCol.put("pv_cdpostal_i", codPosImp);
	                            paramsMunCol.put("pv_cdedo_i",    edoAdosPos2);
	                            paramsMunCol.put("pv_dsmunici_i", cli.getMunicipioCli());
	                            paramsMunCol.put("pv_dscoloni_i", cli.getColoniaCli());
	                            
	                            Map<String,String> munycol= personasManager.obtieneMunicipioYcolonia(paramsMunCol);
	    		    			
	    		    			personasDAO.movimientosMdomicil(newCdPerson,"1", cli.getCalleCli() +" "+ cli.getNumeroCli()
	    		    					,cli.getTelefonoCli(), codPosImp, codPosImp+edoAdosPos2
	    		    					,munycol.get("CDMUNICI"), munycol.get("CDCOLONI"), cli.getNumeroCli(), null
	    		    					,"1" // domicilio personal default
										,usuarioCaptura
										,Constantes.SI  //domicilio activo
										,Constantes.INSERT_MODE);
	    		    			
	    		    			
	    		    			personasDAO.insertaTvaloper("0", "0", null, "0", null,
	    		    					null, null, "1", newCdPerson, null, null,
	    		    					cli.getCveEle(), cli.getPasaporteCli(), null, null, null,
	    		    					null, null, cli.getOrirecCli(), null, null,
	    		    					cli.getNacCli(), null, null, null, null,
	    		    					null, null, null, null, (cli.getOcuPro() > 0) ? Integer.toString(cli.getOcuPro()) : "0",
	    		    					null, null, null, null, cli.getCurpCli(),
	    		    					null, null, null, null, null,
	    		    					null, null, null, null, null,
	    		    					null, null, cli.getTelefonoCli(), cli.getMailCli(), null,
	    		    					null, null, null, null, null,
	    		    					null, null, null, null, null,
	    		    					cli.getFaxCli(), cli.getCelularCli());
	    	    				
	    	    				
	    		    		}
	    		    	}
    		    	}
	    			cotizacionDAO.borrarMpoliperTodos(cdunieco, cdramo, "W", nmpoliza);
					
					cotizacionDAO.movimientoMpoliper(cdunieco, cdramo, "W", nmpoliza,
							"0", "1", newCdPerson, "0", "V", "1", null, "I",
							"S");
    	    	}
    		}
    		
		} catch (Exception e) {
			Utils.generaExcepcion(e, paso);
		}
    	
    	return ntramite;
    }
    
	
    @Override
    public boolean validaDomicilioCotizacionTitular(Map<String,String> params)throws Exception{
    	return cotizacionDAO.validaDomicilioCotizacionTitular(params);
    }
    
    @Deprecated
    @Override
    public boolean validarCuadroComisionNatural(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
    	return cotizacionDAO.validarCuadroComisionNatural(cdunieco,cdramo,estado,nmpoliza);
	}
    
    @Override
    @Deprecated
	public Map<String,String>cargarTvalopol(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
    	return cotizacionDAO.cargarTvalopol(cdunieco,cdramo,estado,nmpoliza);
	}
    
    @Deprecated
    @Override
    public String cargarPorcentajeCesionComisionAutos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
    	return cotizacionDAO.cargarPorcentajeCesionComisionAutos(cdunieco,cdramo,estado,nmpoliza);
	}
    
    @Override
    public void ejecutaValoresDefectoConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception
	{
    	logger.debug(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ ejecutaValoresDefectoConcurrente @@@@@@"
    			,"\n@@@@@@ cdunieco=" , cdunieco
    			,"\n@@@@@@ cdramo="   , cdramo
    			,"\n@@@@@@ estado="   , estado
    			,"\n@@@@@@ nmpoliza=" , nmpoliza
    			,"\n@@@@@@ nmsuplem=" , nmsuplem
    			,"\n@@@@@@ nmsituac=" , nmsituac
    			,"\n@@@@@@ tipotari=" , tipotari
    			,"\n@@@@@@ cdperpag=" , cdperpag
    			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			));
    	cotizacionDAO.ejecutaValoresDefectoConcurrente(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, tipotari, cdperpag);
	}
    
    @Deprecated
    @Override
    public void ejecutaTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
    	logger.debug(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ ejecutaTarificacionConcurrente @@@@@@"
    			,"\n@@@@@@ cdunieco=" , cdunieco
    			,"\n@@@@@@ cdramo="   , cdramo
    			,"\n@@@@@@ estado="   , estado
    			,"\n@@@@@@ nmpoliza=" , nmpoliza
    			,"\n@@@@@@ nmsuplem=" , nmsuplem
    			,"\n@@@@@@ nmsituac=" , nmsituac
    			,"\n@@@@@@ tipotari=" , tipotari
    			,"\n@@@@@@ cdperpag=" , cdperpag
    			,"\n@@@@@@ cdusuari=" , cdusuari
    			,"\n@@@@@@ cdsisrol=" , cdsisrol
    			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			));
    	logger.debug("@@@@@@@@@@@@@@@ lanzatarAsincrono2 ",lanzatarAsincrono);
    	if(lanzatarAsincrono)
    	{
	    	new EjecutaTarificacionConcurrente(
	    			cdunieco
	    			,cdramo
	    			,estado
	    			,nmpoliza
	    			,nmsuplem
	    			,nmsituac
	    			,tipotari
	    			,cdperpag
	    			,cdusuari
	    			,cdsisrol
	    			).start();
    	}
    	else
    	{
    		cotizacionDAO.eliminarMpolirec(
					cdunieco
	        		,cdramo
	        		,estado
	        		,nmpoliza
	        		,nmsuplem
					);
    		
    		cotizacionDAO.insertaMorbilidad(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
    		
    		cotizacionDAO.ejecutaTarificacionConcurrente(
	    			cdunieco
	    			,cdramo
	    			,estado
	    			,nmpoliza
	    			,nmsuplem
	    			,nmsituac
	    			,tipotari
	    			,cdperpag
	    			);
    		logger.debug("@@@@@@@@@@@@@@@@@GRABANDO EVENTO DE COTIZACION 2@@@@@@@@@@@@@@@@@");
    		cotizacionDAO.grabarEvento(new StringBuilder(), Constantes.MODULO_COTIZACION, Constantes.EVENTO_COTIZAR,
    		        new Date(), cdusuari, cdsisrol, "", cdunieco, cdramo, "W", nmpoliza, nmpoliza, "", "", "", null);
    	}
	}
    
    
    private class ConfirmaCensoConcurrente extends Thread
    {
    	private String cdunieco
    	               ,cdramo
    	               ,nmpoliza
    	               ,cdtipsit
    	               ,nombreCensoConfirmado
    	               ,cdusuari
    	               ,cdsisrol
    	               ,nombreCenso
    	               ,cdagente
    	               ,codpostalCli
    	               ,cdedoCli
    	               ,cdmuniciCli
    	               ,ntramite
    	               ,ntramiteVacio
    	               ,clasif
    	               ,LINEA_EXTENDIDA
    	               ,cdpersonCli
    	               ,nombreCli
					   ,rfcCli
					   ,dsdomiciCli
					   ,nmnumeroCli
					   ,nmnumintCli
					   ,cdelemen
					   ,cdideper_
					   ,cdideext_
					   ,cdperpag
					   ,estatuRenovacion;
    	
    	private UserVO usuarioSesion;
    	List<Map<String,Object>>grupos;
    	               
    	public ConfirmaCensoConcurrente(String cdunieco, String cdramo, String nmpoliza, String cdtipsit,
				String nombreCensoConfirmado, String cdusuari, String cdsisrol, String nombreCenso, String cdagente,
				String codpostalCli, String cdedoCli, String cdmuniciCli, String ntramite, String ntramiteVacio,
				String clasif, String lINEA_EXTENDIDA, String cdpersonCli, String nombreCli, String rfcCli,
				String dsdomiciCli, String nmnumeroCli, String nmnumintCli, String cdelemen, String cdideper_,
				String cdideext_, String cdperpag, UserVO usuarioSesion, List<Map<String, Object>> grupos, String estatuRenovacion) {
			this.cdunieco = cdunieco;
			this.cdramo = cdramo;
			this.nmpoliza = nmpoliza;
			this.cdtipsit = cdtipsit;
			this.nombreCensoConfirmado = nombreCensoConfirmado;
			this.cdusuari = cdusuari;
			this.cdsisrol = cdsisrol;
			this.nombreCenso = nombreCenso;
			this.cdagente = cdagente;
			this.codpostalCli = codpostalCli;
			this.cdedoCli = cdedoCli;
			this.cdmuniciCli = cdmuniciCli;
			this.ntramite = ntramite;
			this.ntramiteVacio = ntramiteVacio;
			this.clasif = clasif;
			LINEA_EXTENDIDA = lINEA_EXTENDIDA;
			this.cdpersonCli = cdpersonCli;
			this.nombreCli = nombreCli;
			this.rfcCli = rfcCli;
			this.dsdomiciCli = dsdomiciCli;
			this.nmnumeroCli = nmnumeroCli;
			this.nmnumintCli = nmnumintCli;
			this.cdelemen = cdelemen;
			this.cdideper_ = cdideper_;
			this.cdideext_ = cdideext_;
			this.cdperpag = cdperpag;
			this.usuarioSesion = usuarioSesion;
			this.grupos = grupos;
			this.estatuRenovacion = estatuRenovacion;
		}

		@Override
    	public void run()
    	{
    		try
    		{
    			
    			mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.EN_TARIFA.getCodigo());
    			
				String nombreProcedureCenso = null;
				String tipoCensoParam       = "COMPLETO";
				
				//obtener el PL
				try
				{
					Map<String,String>mapaAux=cotizacionDAO.obtenerParametrosCotizacion(
							ParametroCotizacion.PROCEDURE_CENSO
							,cdramo
							,cdtipsit
							,tipoCensoParam
							,null
							);
					nombreProcedureCenso = mapaAux.get("P1VALOR");
					if(StringUtils.isBlank(nombreProcedureCenso))
					{
						throw new ApplicationException("No se encontraron datos");
					}
				}
	            catch(Exception ax)
	            {
	            	long timestamp = System.currentTimeMillis();
	            	throw new ApplicationException(
	            			new StringBuilder("Error al obtener el nombre del procedimiento del censo: ")
	            			.append(ax.getMessage())
	            			.append(" #")
	            			.append(timestamp)
	            			.toString());
	            }
				
				try
				{
					cotizacionDAO.procesarCenso(
							nombreProcedureCenso
							,cdusuari
							,cdsisrol
							,nombreCenso
							,cdunieco
							,cdramo
							,"W"
							,nmpoliza
							,cdtipsit
							,cdagente
							,codpostalCli
							,cdedoCli
							,cdmuniciCli
							,"N"
							);
				}
	            catch(Exception ex)
	            {
	            	long timestamp = System.currentTimeMillis();
	            	throw new ApplicationException(new StringBuilder("Error al ejecutar procedimiento del censo #").append(timestamp).toString());
	            }
    				
    			logger.info(Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]"));
    			
    			boolean hayTramite      = StringUtils.isNotBlank(ntramite);
    			boolean hayTramiteVacio = StringUtils.isNotBlank(ntramiteVacio);
    			
//    			if(resp.isExito()&&StringUtils.isBlank(nombreCensoConfirmado))
//    			{
//    				resp.getSmap().put("nombreCensoParaConfirmar", nombreCenso);
//    				resp.setExito(true);
//    				resp.setRespuesta(Utils.join("Se ha revisado el censo [REV. ",System.currentTimeMillis(),"]"));
//    				logger.info(resp.getRespuesta());
//    				return resp;
//    			}
    			
    			try
				{
    				ManagerRespuestaSmapVO respInterna = procesoColectivoInterno(
    						grupos
    						,cdunieco
    						,cdramo
    						,nmpoliza
    						,hayTramite
    						,hayTramiteVacio
    						,clasif
    						,LINEA_EXTENDIDA
    						,cdtipsit
    						,cdpersonCli
    						,nombreCli
    						,rfcCli
    						,dsdomiciCli
    						,codpostalCli
    						,cdedoCli
    						,cdmuniciCli
    						,nmnumeroCli
    						,nmnumintCli
    						,ntramite
    						,ntramiteVacio
    						,cdagente
    						,cdusuari
    						,cdelemen
    						,true
    						,false
    						,false
    						,cdperpag
    						,false //resubirCenso
    						,cdsisrol
    						,false
    						,false //asincrono
    						,cdideper_
    						,cdideext_
    						,usuarioSesion
    						,true
    						,false // duplicar
    						);
    			}
	            catch(Exception ex)
	            {
	            	long timestamp = System.currentTimeMillis();
	            	throw new ApplicationException(new StringBuilder("Error al ejecutar procesoColectivoInterno al confirmar censo concurrente  2 #").append(timestamp).toString());
	            }
    			
    			long stamp = System.currentTimeMillis();
    			logger.debug(Utils.log(stamp,"Mandando el tramite a estatus completo despues de subir censo cocurrente 2 y proceso colectivo interno"));

    			if(estatuRenovacion.equalsIgnoreCase(EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo())){
    			    mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.EN_ESPERA_DE_COTIZACION.getCodigo());
    			}else{
    			    mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.TRAMITE_COMPLETO.getCodigo());
    			}
    			
    			
    		}
    		catch(Exception ex)
    		{
    			logger.error("Error en confirmar censo concurrente 2", ex);
    		}
    	}
    }
    
    
    private class EjecutaTarificacionConcurrente extends Thread
    {
    	private String cdunieco
    	               ,cdramo
    	               ,estado
    	               ,nmpoliza
    	               ,nmsuplem
    	               ,nmsituac
    	               ,tipotari
    	               ,cdperpag
    	               ,cdusuari
    	               ,cdsisrol;
    	
    	public EjecutaTarificacionConcurrente(
    			String cdunieco
    			,String cdramo
    			,String estado
    			,String nmpoliza
    			,String nmsuplem
    			,String nmsituac
    			,String tipotari
    			,String cdperpag
    			,String cdusuari
    			,String cdsisrol
    			)
    	{
			this.cdunieco = cdunieco;
			this.cdramo   = cdramo;
			this.estado   = estado;
			this.nmpoliza = nmpoliza;
			this.nmsuplem = nmsuplem;
			this.nmsituac = nmsituac;
			this.tipotari = tipotari;
			this.cdperpag = cdperpag;
			this.cdusuari = cdusuari;
			this.cdsisrol = cdsisrol;
    	}
    	
    	@Override
    	public void run()
    	{
    		try
    		{
    			cotizacionDAO.eliminarMpolirec(
						cdunieco
		        		,cdramo
		        		,estado
		        		,nmpoliza
		        		,nmsuplem
						);
    			
    			cotizacionDAO.insertaMorbilidad(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
    			
    		    cotizacionDAO.ejecutaTarificacionConcurrente(
        			cdunieco
        			,cdramo
        			,estado
        			,nmpoliza
        			,nmsuplem
        			,nmsituac
        			,tipotari
        			,cdperpag
        			);
    		    logger.debug("@@@@@@@@@@@@@@@@@GRABANDO EVENTO DE COTIZACION 3@@@@@@@@@@@@@@@@@");
    		    cotizacionDAO.grabarEvento(new StringBuilder(), Constantes.MODULO_COTIZACION, Constantes.EVENTO_COTIZAR,
    		            new Date(), cdusuari, cdsisrol, "", cdunieco, cdramo, "W", nmpoliza, nmpoliza, "", "", "", null);
    		}
    		catch(Exception ex)
    		{
    			logger.error("Error en tarificacion concurrente", ex);
    		}
    	}
    }
    
    @Override
    public void ejecutaValoresDefectoTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception
	{
    	logger.debug(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ ejecutaValoresDefectoTarificacionConcurrente @@@@@@"
    			,"\n@@@@@@ cdunieco=" , cdunieco
    			,"\n@@@@@@ cdramo="   , cdramo
    			,"\n@@@@@@ estado="   , estado
    			,"\n@@@@@@ nmpoliza=" , nmpoliza
    			,"\n@@@@@@ nmsuplem=" , nmsuplem
    			,"\n@@@@@@ nmsituac=" , nmsituac
    			,"\n@@@@@@ tipotari=" , tipotari
    			,"\n@@@@@@ cdperpag=" , cdperpag
    			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			));
    	cotizacionDAO.ejecutaValoresDefectoTarificacionConcurrente(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, tipotari, cdperpag);
	}
    
    @Override
    public void actualizaValoresDefectoSituacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
    	logger.debug(Utils.log(
   			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
   			,"\n@@@@@@ actualizaValoresDefectoSituacion @@@@@@"
   			,"\n@@@@@@ cdunieco=" , cdunieco
   			,"\n@@@@@@ cdramo="   , cdramo
   			,"\n@@@@@@ estado="   , estado
   			,"\n@@@@@@ nmpoliza=" , nmpoliza
   			,"\n@@@@@@ nmsuplem=" , nmsuplem
   			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
   			));
    	cotizacionDAO.actualizaValoresDefectoSituacion(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
	}
	
    private String extraerStringDeCelda(Cell cell, String tipo)
	{
		try
		{
			if("date".equals(tipo)&&cell.getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				return renderFechas.format(cell.getDateCellValue());
			}
			else
			{
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue().toString();
			}
		}
		catch(Exception ex)
		{
			return "";
		}
	}
    
	private String extraerStringDeCelda(Cell cell)
	{
		try
		{
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String cadena = cell.getStringCellValue();
			return cadena==null?"":cadena;
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	@Override
	public Map<String,Object> complementoSaludGrupo(
			String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String complemento
			,File censo
			,String rutaDocumentosTemporal
			,String dominioServerLayouts
			,String userServerLayouts
			,String passServerLayouts
			,String rootServerLayouts
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,String cdagente
			,String codpostalCli
			,String cdestadoCli
			,String cdmuniciCli
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ complementoSaludGrupo @@@@@@"
				,"\n@@@@@@ ntramite="               , ntramite
				,"\n@@@@@@ cdunieco="               , cdunieco
				,"\n@@@@@@ cdramo="                 , cdramo
				,"\n@@@@@@ estado="                 , estado
				,"\n@@@@@@ nmpoliza="               , nmpoliza
				,"\n@@@@@@ complemento="            , complemento
				,"\n@@@@@@ censo="                  , censo
				,"\n@@@@@@ rutaDocumentosTemporal=" , rutaDocumentosTemporal
				,"\n@@@@@@ dominioServerLayouts="   , dominioServerLayouts
				,"\n@@@@@@ userServerLayouts="      , userServerLayouts
				,"\n@@@@@@ passServerLayouts="      , passServerLayouts
				,"\n@@@@@@ rootServerLayouts="      , rootServerLayouts
				,"\n@@@@@@ cdtipsit="               , cdtipsit
				,"\n@@@@@@ cdusuari="               , cdusuari
				,"\n@@@@@@ cdsisrol="               , cdsisrol
				,"\n@@@@@@ cdagente="               , cdagente
				,"\n@@@@@@ codpostalCli="           , codpostalCli
				,"\n@@@@@@ cdestadoCli="            , cdestadoCli
				,"\n@@@@@@ cdmuniciCli="            , cdmuniciCli
				,"\n@@@@@@ cdplan1="                , cdplan1
				,"\n@@@@@@ cdplan2="                , cdplan2
				,"\n@@@@@@ cdplan3="                , cdplan3
				,"\n@@@@@@ cdplan4="                , cdplan4
				,"\n@@@@@@ cdplan5="                , cdplan5
				));
		
		Map<String,Object> resp = new HashMap<String,Object>();
		
		String paso = "Complementando asegurados";
		try
		{
			paso = "Recuperando configuracion de complemento";
			logger.debug("\nPaso: {}",paso);
			List<Map<String,String>>configs=cotizacionDAO.cargarParametrizacionExcel("COMPGRUP",cdramo,complemento);
			logger.debug("\nConfigs: {}",configs);
			
			paso = "Filtrando filas con errores";
			logger.debug("\nPaso: {}",paso);
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream(censo));
			if(workbook.getNumberOfSheets()!=1)
			{
				throw new ApplicationException("Favor de revisar el n\u00famero de hojas del censo");
			}
			
			Iterator<Row>            rowIterator = workbook.getSheetAt(0).iterator();
			List<Map<String,String>> registros   = new ArrayList<Map<String,String>>();
			List<Map<String,String>> recordsDTO  = new ArrayList<Map<String,String>>();
			int                      nTotal      = 0;
			int                      nBuenas     = 0;
			int                      nError      = 0;
			StringBuilder            errores     = new StringBuilder();
			
			int fila = 0;
			String[] columnas=new String[]{
					  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
					,"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"
					,"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"
			};
			while (rowIterator.hasNext()) 
            {
				fila++;
				nTotal++;
				
				logger.debug("\nIterando fila {}",fila);
				
				Row                row         = rowIterator.next();
				boolean            filaBuena   = true;
				StringBuilder      bufferLinea = new StringBuilder();
				Map<String,String> registro    = new HashMap<String,String>();
				Map<String,String> recordDTO   = new LinkedHashMap<String,String>();
				
				if(Utils.isRowEmpty(row))
				{
					break;
				}
				
				for(Map<String,String>config : configs)
				{
					try
					{
						logger.debug("\nIterando config {}",config);
						int    indice = Integer.parseInt(config.get("COLUMNA"));
						String letra  = columnas[indice];
						Cell   celda  = row.getCell(indice);
						
						String tipo   = config.get("TIPO");
						String valor  = extraerStringDeCelda(row.getCell(indice),tipo);
						String substr = config.get("CDTIPSIT");
						logger.debug("\nValor {} Tipo {} Substr {}",valor,tipo,substr);
						
						boolean obligatorio = "S".equals(config.get("REQUERIDO"));
						
						//validar obligatorio
						if(obligatorio&&StringUtils.isBlank(valor))
						{
							filaBuena = false;
							errores.append(Utils.join("Se requiere ",letra,", "));
						}
						
						//validar tipo
						if(StringUtils.isNotBlank(valor))
						{
							if("int".equals(tipo))
							{
								try
								{
									Integer.parseInt(valor);
								}
								catch(Exception ex)
								{
									filaBuena = false;
									errores.append(Utils.join("No es numerico ",letra,", "));
								}
							}
							else if("double".equals(tipo))
							{
								try
								{
									Double.parseDouble(valor);
								}
								catch(Exception ex)
								{
									filaBuena = false;
									errores.append(Utils.join("No es decimal ",letra,", "));
								}
							}
							else if("date".equals(tipo))
							{
								try
								{
									logger.debug("\nAntes leer fecha");
									celda.setCellType(Cell.CELL_TYPE_NUMERIC);
									Date fecha = celda.getDateCellValue();
									logger.debug("\nFecha leida: {}",fecha);
									Calendar cal = Calendar.getInstance();
				                	cal.setTime(fecha);
				                	if(cal.get(Calendar.YEAR)>2100
				                			||cal.get(Calendar.YEAR)<1900
				                			)
				                	{
				                		throw new ApplicationException("El anio de la fecha no es valido");
				                	}
				                	valor = renderFechas.format(fecha);
								}
								catch(Exception ex)
								{
									logger.error("Erro al leer fecha",ex);
									filaBuena = false;
									errores.append(Utils.join("Fecha incorrecta ",letra,", "));
								}
							}
						}
						
						//validar 
						if(StringUtils.isNotBlank(valor)&&StringUtils.isNotBlank(substr))
						{
							if(substr.indexOf(Utils.join("|",valor,"|"))==-1)
							{
								filaBuena = false;
								errores.append(Utils.join("Valor incorrecto ",letra,", "));
							}
						}

						bufferLinea.append(Utils.join(valor,"-"));
						recordDTO.put(config.get("PROPIEDAD"),valor);
						registro.put(config.get("PROPIEDAD"),valor);
						registro.put(Utils.join("_",letra,"_",config.get("PROPIEDAD")),config.get("DESCRIPCION"));
					}
					catch(Exception ex)
					{
						filaBuena = false;
					}
				}
				
				if(filaBuena)
				{
					nBuenas++;
					registros.add(registro);
					recordsDTO.add(recordDTO);
				}
				else
				{
					nError++;
					errores.append(Utils.join("en la fila ",fila,": ",bufferLinea.toString(),"\n"));
				}
            }
			
			resp.put("erroresCenso"    , errores.toString());
			resp.put("filasLeidas"     , Integer.toString(nTotal));
			resp.put("filasProcesadas" , Integer.toString(nBuenas));
			resp.put("filasErrores"    , Integer.toString(nError));
			resp.put("registros"       , registros);
			
			paso = "Generando archivo de transferencia";
			logger.debug("\nPaso: {}",paso);
			String nombreCenso = null;
			if(nBuenas>0)
			{
				nombreCenso = Utils.join("censo_",System.currentTimeMillis(),"_",nmpoliza,".txt");
				
				File        archivoTxt = new File(Utils.join(rutaDocumentosTemporal,"/",nombreCenso));
				PrintStream output     = new PrintStream(archivoTxt);
				for(Map<String,String>recordDTO:recordsDTO)
				{
					for(Entry<String,String>en:recordDTO.entrySet())
					{
						output.print(Utils.join(en.getValue(),"|"));
					}
					output.println();
				}
				output.close();
				
				paso = "Transfiriendo archivo";
				logger.debug("\nPaso: {}",paso);
				
				boolean transferido = FTPSUtils.upload
				(
					dominioServerLayouts,
					userServerLayouts,
					passServerLayouts,
					archivoTxt.getAbsolutePath(),
					Utils.join(rootServerLayouts,"/",nombreCenso)
				)
				&&FTPSUtils.upload
				(
					dominioServerLayouts2,
					userServerLayouts,
					passServerLayouts,
					archivoTxt.getAbsolutePath(),
					Utils.join(rootServerLayouts,"/",nombreCenso)
				);
				if(!transferido)
				{
					throw new ApplicationException("No se pudo transferir el archivo");
				}
				
				paso = "Recuperando procedimiento";
				logger.debug("\nPaso: {}",paso);
				Map<String,String> mapaProc = cotizacionDAO.obtenerParametrosCotizacion(
						ParametroCotizacion.PROCEDURE_CENSO
						,cdramo
						,cdtipsit
						,complemento == "C" ? "INDIVIDUAL" : "COMPLETO"
						,null
						);
				String nombreProc = mapaProc.get("P1VALOR");
				logger.debug("\ncenso: {}",nombreProc);
				
				paso = "Procesar censo";
				logger.debug("\nPaso: {}",paso);
				if(TipoSituacion.MULTISALUD_COLECTIVO.getCdtipsit().equals(cdtipsit))
				{
					if("C".equals(complemento))
					{
						cotizacionDAO.procesaLayoutCensoMultisalud(
								nombreCenso
								,cdunieco
								,cdramo
								,"W"
								,nmpoliza
								,cdestadoCli
								,cdmuniciCli
								,cdplan1
								,cdplan2
								,cdplan3
								,cdplan4
								,cdplan5
								,"S"
								);
					}
					else
					{
						cotizacionDAO.guardarCensoCompletoMultisalud(
								nombreCenso
								,cdunieco
								,cdramo
								,"W"
								,nmpoliza
								,cdestadoCli
								,cdmuniciCli
								,cdplan1
								,cdplan2
								,cdplan3
								,cdplan4
								,cdplan5
								,"S"
								);
					}
				}
				else
				{
					cotizacionDAO.procesarCenso(
							nombreProc
							,cdusuari
							,cdsisrol
							,nombreCenso
							,cdunieco
							,cdramo
							,"W"
							,nmpoliza
							,cdtipsit
							,cdagente
							,codpostalCli
							,cdestadoCli
							,cdmuniciCli
							,"S"
							);
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ complementoSaludGrupo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	@Override
	@Deprecated
	public boolean validaPagoPolizaRepartido(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ validaPagoPolizaRepartido @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		boolean pagoRepartido = consultasDAO.validaPagoPolizaRepartido(cdunieco,cdramo,estado,nmpoliza);
		logger.debug(Utils.log(
				 "\n@@@@@@ pagoRepartido=" , pagoRepartido
				,"\n@@@@@@ validaPagoPolizaRepartido @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return pagoRepartido;
	}
	
	/*
	@Deprecated
	@Override
	public String turnaPorCargaTrabajo(
			String ntramite
			,String cdsisrol
			,String status
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ turnaPorCargaTrabajo @@@@@@"
				,"\n@@@@@@ ntramite=" , ntramite
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ status="   , status
				));
		
		String nombre = mesaControlDAO.turnaPorCargaTrabajo(ntramite,cdsisrol,status);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ nombre=",nombre
				,"\n@@@@@@ turnaPorCargaTrabajo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return nombre;
	}
	*/
	
	@Override
	public String guardarConfiguracionGarantias(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdpaq
			,String dspaq
			,String derpol
			,List<Map<String,String>>tvalogars
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarConfiguracionGarantias @@@@@@"
				,"\n@@@@@@ cdramo="    , cdramo
				,"\n@@@@@@ cdtipsit="  , cdtipsit
				,"\n@@@@@@ cdplan="    , cdplan
				,"\n@@@@@@ cdpaq="     , cdpaq
				,"\n@@@@@@ dspaq="     , dspaq
				,"\n@@@@@@ derpol="    , derpol
				,"\n@@@@@@ tvalogars=" , tvalogars.size()
				));
		
		String cdPaqueteNuevo = null;
		String paso           = null;
		try
		{
			boolean nuevo = !"0".equals(dspaq);
			
			paso = "Construyendo objetos de transferencia";
			logger.debug("\nPaso: {}",paso);
			
			List<ConfiguracionCoberturaDTO> lista = new ArrayList<ConfiguracionCoberturaDTO>();
			for(Map<String,String>tvalogar : tvalogars)
			{
				ConfiguracionCoberturaDTO c = new ConfiguracionCoberturaDTO(tvalogar.get("cdgarant"));
				c.setOtvalor001(tvalogar.get("parametros.pv_otvalor001"));
				c.setOtvalor002(tvalogar.get("parametros.pv_otvalor002"));
				c.setOtvalor003(tvalogar.get("parametros.pv_otvalor003"));
				c.setOtvalor004(tvalogar.get("parametros.pv_otvalor004"));
				c.setOtvalor005(tvalogar.get("parametros.pv_otvalor005"));
				c.setOtvalor006(tvalogar.get("parametros.pv_otvalor006"));
				c.setOtvalor007(tvalogar.get("parametros.pv_otvalor007"));
				c.setOtvalor008(tvalogar.get("parametros.pv_otvalor008"));
				c.setOtvalor009(tvalogar.get("parametros.pv_otvalor009"));
				c.setOtvalor010(tvalogar.get("parametros.pv_otvalor010"));
				c.setOtvalor011(tvalogar.get("parametros.pv_otvalor011"));
				c.setOtvalor012(tvalogar.get("parametros.pv_otvalor012"));
				c.setOtvalor013(tvalogar.get("parametros.pv_otvalor013"));
				c.setOtvalor014(tvalogar.get("parametros.pv_otvalor014"));
				c.setOtvalor015(tvalogar.get("parametros.pv_otvalor015"));
				c.setOtvalor016(tvalogar.get("parametros.pv_otvalor016"));
				c.setOtvalor017(tvalogar.get("parametros.pv_otvalor017"));
				c.setOtvalor018(tvalogar.get("parametros.pv_otvalor018"));
				c.setOtvalor019(tvalogar.get("parametros.pv_otvalor019"));
				c.setOtvalor020(tvalogar.get("parametros.pv_otvalor020"));
				c.setOtvalor021(tvalogar.get("parametros.pv_otvalor021"));
				c.setOtvalor022(tvalogar.get("parametros.pv_otvalor022"));
				c.setOtvalor023(tvalogar.get("parametros.pv_otvalor023"));
				c.setOtvalor024(tvalogar.get("parametros.pv_otvalor024"));
				c.setOtvalor025(tvalogar.get("parametros.pv_otvalor025"));
				c.setOtvalor026(tvalogar.get("parametros.pv_otvalor026"));
				c.setOtvalor027(tvalogar.get("parametros.pv_otvalor027"));
				c.setOtvalor028(tvalogar.get("parametros.pv_otvalor028"));
				c.setOtvalor029(tvalogar.get("parametros.pv_otvalor029"));
				c.setOtvalor030(tvalogar.get("parametros.pv_otvalor030"));
				c.setOtvalor031(tvalogar.get("parametros.pv_otvalor031"));
				c.setOtvalor032(tvalogar.get("parametros.pv_otvalor032"));
				c.setOtvalor033(tvalogar.get("parametros.pv_otvalor033"));
				c.setOtvalor034(tvalogar.get("parametros.pv_otvalor034"));
				c.setOtvalor035(tvalogar.get("parametros.pv_otvalor035"));
				c.setOtvalor036(tvalogar.get("parametros.pv_otvalor036"));
				c.setOtvalor037(tvalogar.get("parametros.pv_otvalor037"));
				c.setOtvalor038(tvalogar.get("parametros.pv_otvalor038"));
				c.setOtvalor039(tvalogar.get("parametros.pv_otvalor039"));
				c.setOtvalor040(tvalogar.get("parametros.pv_otvalor040"));
				c.setOtvalor041(tvalogar.get("parametros.pv_otvalor041"));
				c.setOtvalor042(tvalogar.get("parametros.pv_otvalor042"));
				c.setOtvalor043(tvalogar.get("parametros.pv_otvalor043"));
				c.setOtvalor044(tvalogar.get("parametros.pv_otvalor044"));
				c.setOtvalor045(tvalogar.get("parametros.pv_otvalor045"));
				c.setOtvalor046(tvalogar.get("parametros.pv_otvalor046"));
				c.setOtvalor047(tvalogar.get("parametros.pv_otvalor047"));
				c.setOtvalor048(tvalogar.get("parametros.pv_otvalor048"));
				c.setOtvalor049(tvalogar.get("parametros.pv_otvalor049"));
				c.setOtvalor050(tvalogar.get("parametros.pv_otvalor050"));
				c.setOtvalor051(tvalogar.get("parametros.pv_otvalor051"));
				c.setOtvalor052(tvalogar.get("parametros.pv_otvalor052"));
				c.setOtvalor053(tvalogar.get("parametros.pv_otvalor053"));
				c.setOtvalor054(tvalogar.get("parametros.pv_otvalor054"));
				c.setOtvalor055(tvalogar.get("parametros.pv_otvalor055"));
				c.setOtvalor056(tvalogar.get("parametros.pv_otvalor056"));
				c.setOtvalor057(tvalogar.get("parametros.pv_otvalor057"));
				c.setOtvalor058(tvalogar.get("parametros.pv_otvalor058"));
				c.setOtvalor059(tvalogar.get("parametros.pv_otvalor059"));
				c.setOtvalor060(tvalogar.get("parametros.pv_otvalor060"));
				c.setOtvalor061(tvalogar.get("parametros.pv_otvalor061"));
				c.setOtvalor062(tvalogar.get("parametros.pv_otvalor062"));
				c.setOtvalor063(tvalogar.get("parametros.pv_otvalor063"));
				c.setOtvalor064(tvalogar.get("parametros.pv_otvalor064"));
				c.setOtvalor065(tvalogar.get("parametros.pv_otvalor065"));
				c.setOtvalor066(tvalogar.get("parametros.pv_otvalor066"));
				c.setOtvalor067(tvalogar.get("parametros.pv_otvalor067"));
				c.setOtvalor068(tvalogar.get("parametros.pv_otvalor068"));
				c.setOtvalor069(tvalogar.get("parametros.pv_otvalor069"));
				c.setOtvalor070(tvalogar.get("parametros.pv_otvalor070"));
				c.setOtvalor071(tvalogar.get("parametros.pv_otvalor071"));
				c.setOtvalor072(tvalogar.get("parametros.pv_otvalor072"));
				c.setOtvalor073(tvalogar.get("parametros.pv_otvalor073"));
				c.setOtvalor074(tvalogar.get("parametros.pv_otvalor074"));
				c.setOtvalor075(tvalogar.get("parametros.pv_otvalor075"));
				c.setOtvalor076(tvalogar.get("parametros.pv_otvalor076"));
				c.setOtvalor077(tvalogar.get("parametros.pv_otvalor077"));
				c.setOtvalor078(tvalogar.get("parametros.pv_otvalor078"));
				c.setOtvalor079(tvalogar.get("parametros.pv_otvalor079"));
				c.setOtvalor080(tvalogar.get("parametros.pv_otvalor080"));
				c.setOtvalor081(tvalogar.get("parametros.pv_otvalor081"));
				c.setOtvalor082(tvalogar.get("parametros.pv_otvalor082"));
				c.setOtvalor083(tvalogar.get("parametros.pv_otvalor083"));
				c.setOtvalor084(tvalogar.get("parametros.pv_otvalor084"));
				c.setOtvalor085(tvalogar.get("parametros.pv_otvalor085"));
				c.setOtvalor086(tvalogar.get("parametros.pv_otvalor086"));
				c.setOtvalor087(tvalogar.get("parametros.pv_otvalor087"));
				c.setOtvalor088(tvalogar.get("parametros.pv_otvalor088"));
				c.setOtvalor089(tvalogar.get("parametros.pv_otvalor089"));
				c.setOtvalor090(tvalogar.get("parametros.pv_otvalor090"));
				c.setOtvalor091(tvalogar.get("parametros.pv_otvalor091"));
				c.setOtvalor092(tvalogar.get("parametros.pv_otvalor092"));
				c.setOtvalor093(tvalogar.get("parametros.pv_otvalor093"));
				c.setOtvalor094(tvalogar.get("parametros.pv_otvalor094"));
				c.setOtvalor095(tvalogar.get("parametros.pv_otvalor095"));
				c.setOtvalor096(tvalogar.get("parametros.pv_otvalor096"));
				c.setOtvalor097(tvalogar.get("parametros.pv_otvalor097"));
				c.setOtvalor098(tvalogar.get("parametros.pv_otvalor098"));
				c.setOtvalor099(tvalogar.get("parametros.pv_otvalor099"));
				c.setOtvalor100(tvalogar.get("parametros.pv_otvalor100"));
				c.setOtvalor101(tvalogar.get("parametros.pv_otvalor101"));
				c.setOtvalor102(tvalogar.get("parametros.pv_otvalor102"));
				c.setOtvalor103(tvalogar.get("parametros.pv_otvalor103"));
				c.setOtvalor104(tvalogar.get("parametros.pv_otvalor104"));
				c.setOtvalor105(tvalogar.get("parametros.pv_otvalor105"));
				c.setOtvalor106(tvalogar.get("parametros.pv_otvalor106"));
				c.setOtvalor107(tvalogar.get("parametros.pv_otvalor107"));
				c.setOtvalor108(tvalogar.get("parametros.pv_otvalor108"));
				c.setOtvalor109(tvalogar.get("parametros.pv_otvalor109"));
				c.setOtvalor110(tvalogar.get("parametros.pv_otvalor110"));
				c.setOtvalor111(tvalogar.get("parametros.pv_otvalor111"));
				c.setOtvalor112(tvalogar.get("parametros.pv_otvalor112"));
				c.setOtvalor113(tvalogar.get("parametros.pv_otvalor113"));
				c.setOtvalor114(tvalogar.get("parametros.pv_otvalor114"));
				c.setOtvalor115(tvalogar.get("parametros.pv_otvalor115"));
				c.setOtvalor116(tvalogar.get("parametros.pv_otvalor116"));
				c.setOtvalor117(tvalogar.get("parametros.pv_otvalor117"));
				c.setOtvalor118(tvalogar.get("parametros.pv_otvalor118"));
				c.setOtvalor119(tvalogar.get("parametros.pv_otvalor119"));
				c.setOtvalor120(tvalogar.get("parametros.pv_otvalor120"));
				c.setOtvalor121(tvalogar.get("parametros.pv_otvalor121"));
				c.setOtvalor122(tvalogar.get("parametros.pv_otvalor122"));
				c.setOtvalor123(tvalogar.get("parametros.pv_otvalor123"));
				c.setOtvalor124(tvalogar.get("parametros.pv_otvalor124"));
				c.setOtvalor125(tvalogar.get("parametros.pv_otvalor125"));
				c.setOtvalor126(tvalogar.get("parametros.pv_otvalor126"));
				c.setOtvalor127(tvalogar.get("parametros.pv_otvalor127"));
				c.setOtvalor128(tvalogar.get("parametros.pv_otvalor128"));
				c.setOtvalor129(tvalogar.get("parametros.pv_otvalor129"));
				c.setOtvalor130(tvalogar.get("parametros.pv_otvalor130"));
				c.setOtvalor131(tvalogar.get("parametros.pv_otvalor131"));
				c.setOtvalor132(tvalogar.get("parametros.pv_otvalor132"));
				c.setOtvalor133(tvalogar.get("parametros.pv_otvalor133"));
				c.setOtvalor134(tvalogar.get("parametros.pv_otvalor134"));
				c.setOtvalor135(tvalogar.get("parametros.pv_otvalor135"));
				c.setOtvalor136(tvalogar.get("parametros.pv_otvalor136"));
				c.setOtvalor137(tvalogar.get("parametros.pv_otvalor137"));
				c.setOtvalor138(tvalogar.get("parametros.pv_otvalor138"));
				c.setOtvalor139(tvalogar.get("parametros.pv_otvalor139"));
				c.setOtvalor140(tvalogar.get("parametros.pv_otvalor140"));
				c.setOtvalor141(tvalogar.get("parametros.pv_otvalor141"));
				c.setOtvalor142(tvalogar.get("parametros.pv_otvalor142"));
				c.setOtvalor143(tvalogar.get("parametros.pv_otvalor143"));
				c.setOtvalor144(tvalogar.get("parametros.pv_otvalor144"));
				c.setOtvalor145(tvalogar.get("parametros.pv_otvalor145"));
				c.setOtvalor146(tvalogar.get("parametros.pv_otvalor146"));
				c.setOtvalor147(tvalogar.get("parametros.pv_otvalor147"));
				c.setOtvalor148(tvalogar.get("parametros.pv_otvalor148"));
				c.setOtvalor149(tvalogar.get("parametros.pv_otvalor149"));
				c.setOtvalor150(tvalogar.get("parametros.pv_otvalor150"));
				c.setOtvalor151(tvalogar.get("parametros.pv_otvalor151"));
				c.setOtvalor152(tvalogar.get("parametros.pv_otvalor152"));
				c.setOtvalor153(tvalogar.get("parametros.pv_otvalor153"));
				c.setOtvalor154(tvalogar.get("parametros.pv_otvalor154"));
				c.setOtvalor155(tvalogar.get("parametros.pv_otvalor155"));
				c.setOtvalor156(tvalogar.get("parametros.pv_otvalor156"));
				c.setOtvalor157(tvalogar.get("parametros.pv_otvalor157"));
				c.setOtvalor158(tvalogar.get("parametros.pv_otvalor158"));
				c.setOtvalor159(tvalogar.get("parametros.pv_otvalor159"));
				c.setOtvalor160(tvalogar.get("parametros.pv_otvalor160"));
				c.setOtvalor161(tvalogar.get("parametros.pv_otvalor161"));
				c.setOtvalor162(tvalogar.get("parametros.pv_otvalor162"));
				c.setOtvalor163(tvalogar.get("parametros.pv_otvalor163"));
				c.setOtvalor164(tvalogar.get("parametros.pv_otvalor164"));
				c.setOtvalor165(tvalogar.get("parametros.pv_otvalor165"));
				c.setOtvalor166(tvalogar.get("parametros.pv_otvalor166"));
				c.setOtvalor167(tvalogar.get("parametros.pv_otvalor167"));
				c.setOtvalor168(tvalogar.get("parametros.pv_otvalor168"));
				c.setOtvalor169(tvalogar.get("parametros.pv_otvalor169"));
				c.setOtvalor170(tvalogar.get("parametros.pv_otvalor170"));
				c.setOtvalor171(tvalogar.get("parametros.pv_otvalor171"));
				c.setOtvalor172(tvalogar.get("parametros.pv_otvalor172"));
				c.setOtvalor173(tvalogar.get("parametros.pv_otvalor173"));
				c.setOtvalor174(tvalogar.get("parametros.pv_otvalor174"));
				c.setOtvalor175(tvalogar.get("parametros.pv_otvalor175"));
				c.setOtvalor176(tvalogar.get("parametros.pv_otvalor176"));
				c.setOtvalor177(tvalogar.get("parametros.pv_otvalor177"));
				c.setOtvalor178(tvalogar.get("parametros.pv_otvalor178"));
				c.setOtvalor179(tvalogar.get("parametros.pv_otvalor179"));
				c.setOtvalor180(tvalogar.get("parametros.pv_otvalor180"));
				c.setOtvalor181(tvalogar.get("parametros.pv_otvalor181"));
				c.setOtvalor182(tvalogar.get("parametros.pv_otvalor182"));
				c.setOtvalor183(tvalogar.get("parametros.pv_otvalor183"));
				c.setOtvalor184(tvalogar.get("parametros.pv_otvalor184"));
				c.setOtvalor185(tvalogar.get("parametros.pv_otvalor185"));
				c.setOtvalor186(tvalogar.get("parametros.pv_otvalor186"));
				c.setOtvalor187(tvalogar.get("parametros.pv_otvalor187"));
				c.setOtvalor188(tvalogar.get("parametros.pv_otvalor188"));
				c.setOtvalor189(tvalogar.get("parametros.pv_otvalor189"));
				c.setOtvalor190(tvalogar.get("parametros.pv_otvalor190"));
				c.setOtvalor191(tvalogar.get("parametros.pv_otvalor191"));
				c.setOtvalor192(tvalogar.get("parametros.pv_otvalor192"));
				c.setOtvalor193(tvalogar.get("parametros.pv_otvalor193"));
				c.setOtvalor194(tvalogar.get("parametros.pv_otvalor194"));
				c.setOtvalor195(tvalogar.get("parametros.pv_otvalor195"));
				c.setOtvalor196(tvalogar.get("parametros.pv_otvalor196"));
				c.setOtvalor197(tvalogar.get("parametros.pv_otvalor197"));
				c.setOtvalor198(tvalogar.get("parametros.pv_otvalor198"));
				c.setOtvalor199(tvalogar.get("parametros.pv_otvalor199"));
				c.setOtvalor200(tvalogar.get("parametros.pv_otvalor200"));
				c.setOtvalor201(tvalogar.get("parametros.pv_otvalor201"));
				c.setOtvalor202(tvalogar.get("parametros.pv_otvalor202"));
				c.setOtvalor203(tvalogar.get("parametros.pv_otvalor203"));
				c.setOtvalor204(tvalogar.get("parametros.pv_otvalor204"));
				c.setOtvalor205(tvalogar.get("parametros.pv_otvalor205"));
				c.setOtvalor206(tvalogar.get("parametros.pv_otvalor206"));
				c.setOtvalor207(tvalogar.get("parametros.pv_otvalor207"));
				c.setOtvalor208(tvalogar.get("parametros.pv_otvalor208"));
				c.setOtvalor209(tvalogar.get("parametros.pv_otvalor209"));
				c.setOtvalor210(tvalogar.get("parametros.pv_otvalor210"));
				c.setOtvalor211(tvalogar.get("parametros.pv_otvalor211"));
				c.setOtvalor212(tvalogar.get("parametros.pv_otvalor212"));
				c.setOtvalor213(tvalogar.get("parametros.pv_otvalor213"));
				c.setOtvalor214(tvalogar.get("parametros.pv_otvalor214"));
				c.setOtvalor215(tvalogar.get("parametros.pv_otvalor215"));
				c.setOtvalor216(tvalogar.get("parametros.pv_otvalor216"));
				c.setOtvalor217(tvalogar.get("parametros.pv_otvalor217"));
				c.setOtvalor218(tvalogar.get("parametros.pv_otvalor218"));
				c.setOtvalor219(tvalogar.get("parametros.pv_otvalor219"));
				c.setOtvalor220(tvalogar.get("parametros.pv_otvalor220"));
				c.setOtvalor221(tvalogar.get("parametros.pv_otvalor221"));
				c.setOtvalor222(tvalogar.get("parametros.pv_otvalor222"));
				c.setOtvalor223(tvalogar.get("parametros.pv_otvalor223"));
				c.setOtvalor224(tvalogar.get("parametros.pv_otvalor224"));
				c.setOtvalor225(tvalogar.get("parametros.pv_otvalor225"));
				c.setOtvalor226(tvalogar.get("parametros.pv_otvalor226"));
				c.setOtvalor227(tvalogar.get("parametros.pv_otvalor227"));
				c.setOtvalor228(tvalogar.get("parametros.pv_otvalor228"));
				c.setOtvalor229(tvalogar.get("parametros.pv_otvalor229"));
				c.setOtvalor230(tvalogar.get("parametros.pv_otvalor230"));
				c.setOtvalor231(tvalogar.get("parametros.pv_otvalor231"));
				c.setOtvalor232(tvalogar.get("parametros.pv_otvalor232"));
				c.setOtvalor233(tvalogar.get("parametros.pv_otvalor233"));
				c.setOtvalor234(tvalogar.get("parametros.pv_otvalor234"));
				c.setOtvalor235(tvalogar.get("parametros.pv_otvalor235"));
				c.setOtvalor236(tvalogar.get("parametros.pv_otvalor236"));
				c.setOtvalor237(tvalogar.get("parametros.pv_otvalor237"));
				c.setOtvalor238(tvalogar.get("parametros.pv_otvalor238"));
				c.setOtvalor239(tvalogar.get("parametros.pv_otvalor239"));
				c.setOtvalor240(tvalogar.get("parametros.pv_otvalor240"));
				c.setOtvalor241(tvalogar.get("parametros.pv_otvalor241"));
				c.setOtvalor242(tvalogar.get("parametros.pv_otvalor242"));
				c.setOtvalor243(tvalogar.get("parametros.pv_otvalor243"));
				c.setOtvalor244(tvalogar.get("parametros.pv_otvalor244"));
				c.setOtvalor245(tvalogar.get("parametros.pv_otvalor245"));
				c.setOtvalor246(tvalogar.get("parametros.pv_otvalor246"));
				c.setOtvalor247(tvalogar.get("parametros.pv_otvalor247"));
				c.setOtvalor248(tvalogar.get("parametros.pv_otvalor248"));
				c.setOtvalor249(tvalogar.get("parametros.pv_otvalor249"));
				c.setOtvalor250(tvalogar.get("parametros.pv_otvalor250"));
				c.setOtvalor251(tvalogar.get("parametros.pv_otvalor251"));
				c.setOtvalor252(tvalogar.get("parametros.pv_otvalor252"));
				c.setOtvalor253(tvalogar.get("parametros.pv_otvalor253"));
				c.setOtvalor254(tvalogar.get("parametros.pv_otvalor254"));
				c.setOtvalor255(tvalogar.get("parametros.pv_otvalor255"));
				c.setOtvalor256(tvalogar.get("parametros.pv_otvalor256"));
				c.setOtvalor257(tvalogar.get("parametros.pv_otvalor257"));
				c.setOtvalor258(tvalogar.get("parametros.pv_otvalor258"));
				c.setOtvalor259(tvalogar.get("parametros.pv_otvalor259"));
				c.setOtvalor260(tvalogar.get("parametros.pv_otvalor260"));
				c.setOtvalor261(tvalogar.get("parametros.pv_otvalor261"));
				c.setOtvalor262(tvalogar.get("parametros.pv_otvalor262"));
				c.setOtvalor263(tvalogar.get("parametros.pv_otvalor263"));
				c.setOtvalor264(tvalogar.get("parametros.pv_otvalor264"));
				c.setOtvalor265(tvalogar.get("parametros.pv_otvalor265"));
				c.setOtvalor266(tvalogar.get("parametros.pv_otvalor266"));
				c.setOtvalor267(tvalogar.get("parametros.pv_otvalor267"));
				c.setOtvalor268(tvalogar.get("parametros.pv_otvalor268"));
				c.setOtvalor269(tvalogar.get("parametros.pv_otvalor269"));
				c.setOtvalor270(tvalogar.get("parametros.pv_otvalor270"));
				c.setOtvalor271(tvalogar.get("parametros.pv_otvalor271"));
				c.setOtvalor272(tvalogar.get("parametros.pv_otvalor272"));
				c.setOtvalor273(tvalogar.get("parametros.pv_otvalor273"));
				c.setOtvalor274(tvalogar.get("parametros.pv_otvalor274"));
				c.setOtvalor275(tvalogar.get("parametros.pv_otvalor275"));
				c.setOtvalor276(tvalogar.get("parametros.pv_otvalor276"));
				c.setOtvalor277(tvalogar.get("parametros.pv_otvalor277"));
				c.setOtvalor278(tvalogar.get("parametros.pv_otvalor278"));
				c.setOtvalor279(tvalogar.get("parametros.pv_otvalor279"));
				c.setOtvalor280(tvalogar.get("parametros.pv_otvalor280"));
				c.setOtvalor281(tvalogar.get("parametros.pv_otvalor281"));
				c.setOtvalor282(tvalogar.get("parametros.pv_otvalor282"));
				c.setOtvalor283(tvalogar.get("parametros.pv_otvalor283"));
				c.setOtvalor284(tvalogar.get("parametros.pv_otvalor284"));
				c.setOtvalor285(tvalogar.get("parametros.pv_otvalor285"));
				c.setOtvalor286(tvalogar.get("parametros.pv_otvalor286"));
				c.setOtvalor287(tvalogar.get("parametros.pv_otvalor287"));
				c.setOtvalor288(tvalogar.get("parametros.pv_otvalor288"));
				c.setOtvalor289(tvalogar.get("parametros.pv_otvalor289"));
				c.setOtvalor290(tvalogar.get("parametros.pv_otvalor290"));
				c.setOtvalor291(tvalogar.get("parametros.pv_otvalor291"));
				c.setOtvalor292(tvalogar.get("parametros.pv_otvalor292"));
				c.setOtvalor293(tvalogar.get("parametros.pv_otvalor293"));
				c.setOtvalor294(tvalogar.get("parametros.pv_otvalor294"));
				c.setOtvalor295(tvalogar.get("parametros.pv_otvalor295"));
				c.setOtvalor296(tvalogar.get("parametros.pv_otvalor296"));
				c.setOtvalor297(tvalogar.get("parametros.pv_otvalor297"));
				c.setOtvalor298(tvalogar.get("parametros.pv_otvalor298"));
				c.setOtvalor299(tvalogar.get("parametros.pv_otvalor299"));
				c.setOtvalor300(tvalogar.get("parametros.pv_otvalor300"));
				c.setOtvalor301(tvalogar.get("parametros.pv_otvalor301"));
				c.setOtvalor302(tvalogar.get("parametros.pv_otvalor302"));
				c.setOtvalor303(tvalogar.get("parametros.pv_otvalor303"));
				c.setOtvalor304(tvalogar.get("parametros.pv_otvalor304"));
				c.setOtvalor305(tvalogar.get("parametros.pv_otvalor305"));
				c.setOtvalor306(tvalogar.get("parametros.pv_otvalor306"));
				c.setOtvalor307(tvalogar.get("parametros.pv_otvalor307"));
				c.setOtvalor308(tvalogar.get("parametros.pv_otvalor308"));
				c.setOtvalor309(tvalogar.get("parametros.pv_otvalor309"));
				c.setOtvalor310(tvalogar.get("parametros.pv_otvalor310"));
				c.setOtvalor311(tvalogar.get("parametros.pv_otvalor311"));
				c.setOtvalor312(tvalogar.get("parametros.pv_otvalor312"));
				c.setOtvalor313(tvalogar.get("parametros.pv_otvalor313"));
				c.setOtvalor314(tvalogar.get("parametros.pv_otvalor314"));
				c.setOtvalor315(tvalogar.get("parametros.pv_otvalor315"));
				c.setOtvalor316(tvalogar.get("parametros.pv_otvalor316"));
				c.setOtvalor317(tvalogar.get("parametros.pv_otvalor317"));
				c.setOtvalor318(tvalogar.get("parametros.pv_otvalor318"));
				c.setOtvalor319(tvalogar.get("parametros.pv_otvalor319"));
				c.setOtvalor320(tvalogar.get("parametros.pv_otvalor320"));
				c.setOtvalor321(tvalogar.get("parametros.pv_otvalor321"));
				c.setOtvalor322(tvalogar.get("parametros.pv_otvalor322"));
				c.setOtvalor323(tvalogar.get("parametros.pv_otvalor323"));
				c.setOtvalor324(tvalogar.get("parametros.pv_otvalor324"));
				c.setOtvalor325(tvalogar.get("parametros.pv_otvalor325"));
				c.setOtvalor326(tvalogar.get("parametros.pv_otvalor326"));
				c.setOtvalor327(tvalogar.get("parametros.pv_otvalor327"));
				c.setOtvalor328(tvalogar.get("parametros.pv_otvalor328"));
				c.setOtvalor329(tvalogar.get("parametros.pv_otvalor329"));
				c.setOtvalor330(tvalogar.get("parametros.pv_otvalor330"));
				c.setOtvalor331(tvalogar.get("parametros.pv_otvalor331"));
				c.setOtvalor332(tvalogar.get("parametros.pv_otvalor332"));
				c.setOtvalor333(tvalogar.get("parametros.pv_otvalor333"));
				c.setOtvalor334(tvalogar.get("parametros.pv_otvalor334"));
				c.setOtvalor335(tvalogar.get("parametros.pv_otvalor335"));
				c.setOtvalor336(tvalogar.get("parametros.pv_otvalor336"));
				c.setOtvalor337(tvalogar.get("parametros.pv_otvalor337"));
				c.setOtvalor338(tvalogar.get("parametros.pv_otvalor338"));
				c.setOtvalor339(tvalogar.get("parametros.pv_otvalor339"));
				c.setOtvalor340(tvalogar.get("parametros.pv_otvalor340"));
				c.setOtvalor341(tvalogar.get("parametros.pv_otvalor341"));
				c.setOtvalor342(tvalogar.get("parametros.pv_otvalor342"));
				c.setOtvalor343(tvalogar.get("parametros.pv_otvalor343"));
				c.setOtvalor344(tvalogar.get("parametros.pv_otvalor344"));
				c.setOtvalor345(tvalogar.get("parametros.pv_otvalor345"));
				c.setOtvalor346(tvalogar.get("parametros.pv_otvalor346"));
				c.setOtvalor347(tvalogar.get("parametros.pv_otvalor347"));
				c.setOtvalor348(tvalogar.get("parametros.pv_otvalor348"));
				c.setOtvalor349(tvalogar.get("parametros.pv_otvalor349"));
				c.setOtvalor350(tvalogar.get("parametros.pv_otvalor350"));
				c.setOtvalor351(tvalogar.get("parametros.pv_otvalor351"));
				c.setOtvalor352(tvalogar.get("parametros.pv_otvalor352"));
				c.setOtvalor353(tvalogar.get("parametros.pv_otvalor353"));
				c.setOtvalor354(tvalogar.get("parametros.pv_otvalor354"));
				c.setOtvalor355(tvalogar.get("parametros.pv_otvalor355"));
				c.setOtvalor356(tvalogar.get("parametros.pv_otvalor356"));
				c.setOtvalor357(tvalogar.get("parametros.pv_otvalor357"));
				c.setOtvalor358(tvalogar.get("parametros.pv_otvalor358"));
				c.setOtvalor359(tvalogar.get("parametros.pv_otvalor359"));
				c.setOtvalor360(tvalogar.get("parametros.pv_otvalor360"));
				c.setOtvalor361(tvalogar.get("parametros.pv_otvalor361"));
				c.setOtvalor362(tvalogar.get("parametros.pv_otvalor362"));
				c.setOtvalor363(tvalogar.get("parametros.pv_otvalor363"));
				c.setOtvalor364(tvalogar.get("parametros.pv_otvalor364"));
				c.setOtvalor365(tvalogar.get("parametros.pv_otvalor365"));
				c.setOtvalor366(tvalogar.get("parametros.pv_otvalor366"));
				c.setOtvalor367(tvalogar.get("parametros.pv_otvalor367"));
				c.setOtvalor368(tvalogar.get("parametros.pv_otvalor368"));
				c.setOtvalor369(tvalogar.get("parametros.pv_otvalor369"));
				c.setOtvalor370(tvalogar.get("parametros.pv_otvalor370"));
				c.setOtvalor371(tvalogar.get("parametros.pv_otvalor371"));
				c.setOtvalor372(tvalogar.get("parametros.pv_otvalor372"));
				c.setOtvalor373(tvalogar.get("parametros.pv_otvalor373"));
				c.setOtvalor374(tvalogar.get("parametros.pv_otvalor374"));
				c.setOtvalor375(tvalogar.get("parametros.pv_otvalor375"));
				c.setOtvalor376(tvalogar.get("parametros.pv_otvalor376"));
				c.setOtvalor377(tvalogar.get("parametros.pv_otvalor377"));
				c.setOtvalor378(tvalogar.get("parametros.pv_otvalor378"));
				c.setOtvalor379(tvalogar.get("parametros.pv_otvalor379"));
				c.setOtvalor380(tvalogar.get("parametros.pv_otvalor380"));
				c.setOtvalor381(tvalogar.get("parametros.pv_otvalor381"));
				c.setOtvalor382(tvalogar.get("parametros.pv_otvalor382"));
				c.setOtvalor383(tvalogar.get("parametros.pv_otvalor383"));
				c.setOtvalor384(tvalogar.get("parametros.pv_otvalor384"));
				c.setOtvalor385(tvalogar.get("parametros.pv_otvalor385"));
				c.setOtvalor386(tvalogar.get("parametros.pv_otvalor386"));
				c.setOtvalor387(tvalogar.get("parametros.pv_otvalor387"));
				c.setOtvalor388(tvalogar.get("parametros.pv_otvalor388"));
				c.setOtvalor389(tvalogar.get("parametros.pv_otvalor389"));
				c.setOtvalor390(tvalogar.get("parametros.pv_otvalor390"));
				c.setOtvalor391(tvalogar.get("parametros.pv_otvalor391"));
				c.setOtvalor392(tvalogar.get("parametros.pv_otvalor392"));
				c.setOtvalor393(tvalogar.get("parametros.pv_otvalor393"));
				c.setOtvalor394(tvalogar.get("parametros.pv_otvalor394"));
				c.setOtvalor395(tvalogar.get("parametros.pv_otvalor395"));
				c.setOtvalor396(tvalogar.get("parametros.pv_otvalor396"));
				c.setOtvalor397(tvalogar.get("parametros.pv_otvalor397"));
				c.setOtvalor398(tvalogar.get("parametros.pv_otvalor398"));
				c.setOtvalor399(tvalogar.get("parametros.pv_otvalor399"));
				c.setOtvalor400(tvalogar.get("parametros.pv_otvalor400"));
				c.setOtvalor401(tvalogar.get("parametros.pv_otvalor401"));
				c.setOtvalor402(tvalogar.get("parametros.pv_otvalor402"));
				c.setOtvalor403(tvalogar.get("parametros.pv_otvalor403"));
				c.setOtvalor404(tvalogar.get("parametros.pv_otvalor404"));
				c.setOtvalor405(tvalogar.get("parametros.pv_otvalor405"));
				c.setOtvalor406(tvalogar.get("parametros.pv_otvalor406"));
				c.setOtvalor407(tvalogar.get("parametros.pv_otvalor407"));
				c.setOtvalor408(tvalogar.get("parametros.pv_otvalor408"));
				c.setOtvalor409(tvalogar.get("parametros.pv_otvalor409"));
				c.setOtvalor410(tvalogar.get("parametros.pv_otvalor410"));
				c.setOtvalor411(tvalogar.get("parametros.pv_otvalor411"));
				c.setOtvalor412(tvalogar.get("parametros.pv_otvalor412"));
				c.setOtvalor413(tvalogar.get("parametros.pv_otvalor413"));
				c.setOtvalor414(tvalogar.get("parametros.pv_otvalor414"));
				c.setOtvalor415(tvalogar.get("parametros.pv_otvalor415"));
				c.setOtvalor416(tvalogar.get("parametros.pv_otvalor416"));
				c.setOtvalor417(tvalogar.get("parametros.pv_otvalor417"));
				c.setOtvalor418(tvalogar.get("parametros.pv_otvalor418"));
				c.setOtvalor419(tvalogar.get("parametros.pv_otvalor419"));
				c.setOtvalor420(tvalogar.get("parametros.pv_otvalor420"));
				c.setOtvalor421(tvalogar.get("parametros.pv_otvalor421"));
				c.setOtvalor422(tvalogar.get("parametros.pv_otvalor422"));
				c.setOtvalor423(tvalogar.get("parametros.pv_otvalor423"));
				c.setOtvalor424(tvalogar.get("parametros.pv_otvalor424"));
				c.setOtvalor425(tvalogar.get("parametros.pv_otvalor425"));
				c.setOtvalor426(tvalogar.get("parametros.pv_otvalor426"));
				c.setOtvalor427(tvalogar.get("parametros.pv_otvalor427"));
				c.setOtvalor428(tvalogar.get("parametros.pv_otvalor428"));
				c.setOtvalor429(tvalogar.get("parametros.pv_otvalor429"));
				c.setOtvalor430(tvalogar.get("parametros.pv_otvalor430"));
				c.setOtvalor431(tvalogar.get("parametros.pv_otvalor431"));
				c.setOtvalor432(tvalogar.get("parametros.pv_otvalor432"));
				c.setOtvalor433(tvalogar.get("parametros.pv_otvalor433"));
				c.setOtvalor434(tvalogar.get("parametros.pv_otvalor434"));
				c.setOtvalor435(tvalogar.get("parametros.pv_otvalor435"));
				c.setOtvalor436(tvalogar.get("parametros.pv_otvalor436"));
				c.setOtvalor437(tvalogar.get("parametros.pv_otvalor437"));
				c.setOtvalor438(tvalogar.get("parametros.pv_otvalor438"));
				c.setOtvalor439(tvalogar.get("parametros.pv_otvalor439"));
				c.setOtvalor440(tvalogar.get("parametros.pv_otvalor440"));
				c.setOtvalor441(tvalogar.get("parametros.pv_otvalor441"));
				c.setOtvalor442(tvalogar.get("parametros.pv_otvalor442"));
				c.setOtvalor443(tvalogar.get("parametros.pv_otvalor443"));
				c.setOtvalor444(tvalogar.get("parametros.pv_otvalor444"));
				c.setOtvalor445(tvalogar.get("parametros.pv_otvalor445"));
				c.setOtvalor446(tvalogar.get("parametros.pv_otvalor446"));
				c.setOtvalor447(tvalogar.get("parametros.pv_otvalor447"));
				c.setOtvalor448(tvalogar.get("parametros.pv_otvalor448"));
				c.setOtvalor449(tvalogar.get("parametros.pv_otvalor449"));
				c.setOtvalor450(tvalogar.get("parametros.pv_otvalor450"));
				c.setOtvalor451(tvalogar.get("parametros.pv_otvalor451"));
				c.setOtvalor452(tvalogar.get("parametros.pv_otvalor452"));
				c.setOtvalor453(tvalogar.get("parametros.pv_otvalor453"));
				c.setOtvalor454(tvalogar.get("parametros.pv_otvalor454"));
				c.setOtvalor455(tvalogar.get("parametros.pv_otvalor455"));
				c.setOtvalor456(tvalogar.get("parametros.pv_otvalor456"));
				c.setOtvalor457(tvalogar.get("parametros.pv_otvalor457"));
				c.setOtvalor458(tvalogar.get("parametros.pv_otvalor458"));
				c.setOtvalor459(tvalogar.get("parametros.pv_otvalor459"));
				c.setOtvalor460(tvalogar.get("parametros.pv_otvalor460"));
				c.setOtvalor461(tvalogar.get("parametros.pv_otvalor461"));
				c.setOtvalor462(tvalogar.get("parametros.pv_otvalor462"));
				c.setOtvalor463(tvalogar.get("parametros.pv_otvalor463"));
				c.setOtvalor464(tvalogar.get("parametros.pv_otvalor464"));
				c.setOtvalor465(tvalogar.get("parametros.pv_otvalor465"));
				c.setOtvalor466(tvalogar.get("parametros.pv_otvalor466"));
				c.setOtvalor467(tvalogar.get("parametros.pv_otvalor467"));
				c.setOtvalor468(tvalogar.get("parametros.pv_otvalor468"));
				c.setOtvalor469(tvalogar.get("parametros.pv_otvalor469"));
				c.setOtvalor470(tvalogar.get("parametros.pv_otvalor470"));
				c.setOtvalor471(tvalogar.get("parametros.pv_otvalor471"));
				c.setOtvalor472(tvalogar.get("parametros.pv_otvalor472"));
				c.setOtvalor473(tvalogar.get("parametros.pv_otvalor473"));
				c.setOtvalor474(tvalogar.get("parametros.pv_otvalor474"));
				c.setOtvalor475(tvalogar.get("parametros.pv_otvalor475"));
				c.setOtvalor476(tvalogar.get("parametros.pv_otvalor476"));
				c.setOtvalor477(tvalogar.get("parametros.pv_otvalor477"));
				c.setOtvalor478(tvalogar.get("parametros.pv_otvalor478"));
				c.setOtvalor479(tvalogar.get("parametros.pv_otvalor479"));
				c.setOtvalor480(tvalogar.get("parametros.pv_otvalor480"));
				c.setOtvalor481(tvalogar.get("parametros.pv_otvalor481"));
				c.setOtvalor482(tvalogar.get("parametros.pv_otvalor482"));
				c.setOtvalor483(tvalogar.get("parametros.pv_otvalor483"));
				c.setOtvalor484(tvalogar.get("parametros.pv_otvalor484"));
				c.setOtvalor485(tvalogar.get("parametros.pv_otvalor485"));
				c.setOtvalor486(tvalogar.get("parametros.pv_otvalor486"));
				c.setOtvalor487(tvalogar.get("parametros.pv_otvalor487"));
				c.setOtvalor488(tvalogar.get("parametros.pv_otvalor488"));
				c.setOtvalor489(tvalogar.get("parametros.pv_otvalor489"));
				c.setOtvalor490(tvalogar.get("parametros.pv_otvalor490"));
				c.setOtvalor491(tvalogar.get("parametros.pv_otvalor491"));
				c.setOtvalor492(tvalogar.get("parametros.pv_otvalor492"));
				c.setOtvalor493(tvalogar.get("parametros.pv_otvalor493"));
				c.setOtvalor494(tvalogar.get("parametros.pv_otvalor494"));
				c.setOtvalor495(tvalogar.get("parametros.pv_otvalor495"));
				c.setOtvalor496(tvalogar.get("parametros.pv_otvalor496"));
				c.setOtvalor497(tvalogar.get("parametros.pv_otvalor497"));
				c.setOtvalor498(tvalogar.get("parametros.pv_otvalor498"));
				c.setOtvalor499(tvalogar.get("parametros.pv_otvalor499"));
				c.setOtvalor500(tvalogar.get("parametros.pv_otvalor500"));
				c.setOtvalor501(tvalogar.get("parametros.pv_otvalor501"));
				c.setOtvalor502(tvalogar.get("parametros.pv_otvalor502"));
				c.setOtvalor503(tvalogar.get("parametros.pv_otvalor503"));
				c.setOtvalor504(tvalogar.get("parametros.pv_otvalor504"));
				c.setOtvalor505(tvalogar.get("parametros.pv_otvalor505"));
				c.setOtvalor506(tvalogar.get("parametros.pv_otvalor506"));
				c.setOtvalor507(tvalogar.get("parametros.pv_otvalor507"));
				c.setOtvalor508(tvalogar.get("parametros.pv_otvalor508"));
				c.setOtvalor509(tvalogar.get("parametros.pv_otvalor509"));
				c.setOtvalor510(tvalogar.get("parametros.pv_otvalor510"));
				c.setOtvalor511(tvalogar.get("parametros.pv_otvalor511"));
				c.setOtvalor512(tvalogar.get("parametros.pv_otvalor512"));
				c.setOtvalor513(tvalogar.get("parametros.pv_otvalor513"));
				c.setOtvalor514(tvalogar.get("parametros.pv_otvalor514"));
				c.setOtvalor515(tvalogar.get("parametros.pv_otvalor515"));
				c.setOtvalor516(tvalogar.get("parametros.pv_otvalor516"));
				c.setOtvalor517(tvalogar.get("parametros.pv_otvalor517"));
				c.setOtvalor518(tvalogar.get("parametros.pv_otvalor518"));
				c.setOtvalor519(tvalogar.get("parametros.pv_otvalor519"));
				c.setOtvalor520(tvalogar.get("parametros.pv_otvalor520"));
				c.setOtvalor521(tvalogar.get("parametros.pv_otvalor521"));
				c.setOtvalor522(tvalogar.get("parametros.pv_otvalor522"));
				c.setOtvalor523(tvalogar.get("parametros.pv_otvalor523"));
				c.setOtvalor524(tvalogar.get("parametros.pv_otvalor524"));
				c.setOtvalor525(tvalogar.get("parametros.pv_otvalor525"));
				c.setOtvalor526(tvalogar.get("parametros.pv_otvalor526"));
				c.setOtvalor527(tvalogar.get("parametros.pv_otvalor527"));
				c.setOtvalor528(tvalogar.get("parametros.pv_otvalor528"));
				c.setOtvalor529(tvalogar.get("parametros.pv_otvalor529"));
				c.setOtvalor530(tvalogar.get("parametros.pv_otvalor530"));
				c.setOtvalor531(tvalogar.get("parametros.pv_otvalor531"));
				c.setOtvalor532(tvalogar.get("parametros.pv_otvalor532"));
				c.setOtvalor533(tvalogar.get("parametros.pv_otvalor533"));
				c.setOtvalor534(tvalogar.get("parametros.pv_otvalor534"));
				c.setOtvalor535(tvalogar.get("parametros.pv_otvalor535"));
				c.setOtvalor536(tvalogar.get("parametros.pv_otvalor536"));
				c.setOtvalor537(tvalogar.get("parametros.pv_otvalor537"));
				c.setOtvalor538(tvalogar.get("parametros.pv_otvalor538"));
				c.setOtvalor539(tvalogar.get("parametros.pv_otvalor539"));
				c.setOtvalor540(tvalogar.get("parametros.pv_otvalor540"));
				c.setOtvalor541(tvalogar.get("parametros.pv_otvalor541"));
				c.setOtvalor542(tvalogar.get("parametros.pv_otvalor542"));
				c.setOtvalor543(tvalogar.get("parametros.pv_otvalor543"));
				c.setOtvalor544(tvalogar.get("parametros.pv_otvalor544"));
				c.setOtvalor545(tvalogar.get("parametros.pv_otvalor545"));
				c.setOtvalor546(tvalogar.get("parametros.pv_otvalor546"));
				c.setOtvalor547(tvalogar.get("parametros.pv_otvalor547"));
				c.setOtvalor548(tvalogar.get("parametros.pv_otvalor548"));
				c.setOtvalor549(tvalogar.get("parametros.pv_otvalor549"));
				c.setOtvalor550(tvalogar.get("parametros.pv_otvalor550"));
				c.setOtvalor551(tvalogar.get("parametros.pv_otvalor551"));
				c.setOtvalor552(tvalogar.get("parametros.pv_otvalor552"));
				c.setOtvalor553(tvalogar.get("parametros.pv_otvalor553"));
				c.setOtvalor554(tvalogar.get("parametros.pv_otvalor554"));
				c.setOtvalor555(tvalogar.get("parametros.pv_otvalor555"));
				c.setOtvalor556(tvalogar.get("parametros.pv_otvalor556"));
				c.setOtvalor557(tvalogar.get("parametros.pv_otvalor557"));
				c.setOtvalor558(tvalogar.get("parametros.pv_otvalor558"));
				c.setOtvalor559(tvalogar.get("parametros.pv_otvalor559"));
				c.setOtvalor560(tvalogar.get("parametros.pv_otvalor560"));
				c.setOtvalor561(tvalogar.get("parametros.pv_otvalor561"));
				c.setOtvalor562(tvalogar.get("parametros.pv_otvalor562"));
				c.setOtvalor563(tvalogar.get("parametros.pv_otvalor563"));
				c.setOtvalor564(tvalogar.get("parametros.pv_otvalor564"));
				c.setOtvalor565(tvalogar.get("parametros.pv_otvalor565"));
				c.setOtvalor566(tvalogar.get("parametros.pv_otvalor566"));
				c.setOtvalor567(tvalogar.get("parametros.pv_otvalor567"));
				c.setOtvalor568(tvalogar.get("parametros.pv_otvalor568"));
				c.setOtvalor569(tvalogar.get("parametros.pv_otvalor569"));
				c.setOtvalor570(tvalogar.get("parametros.pv_otvalor570"));
				c.setOtvalor571(tvalogar.get("parametros.pv_otvalor571"));
				c.setOtvalor572(tvalogar.get("parametros.pv_otvalor572"));
				c.setOtvalor573(tvalogar.get("parametros.pv_otvalor573"));
				c.setOtvalor574(tvalogar.get("parametros.pv_otvalor574"));
				c.setOtvalor575(tvalogar.get("parametros.pv_otvalor575"));
				c.setOtvalor576(tvalogar.get("parametros.pv_otvalor576"));
				c.setOtvalor577(tvalogar.get("parametros.pv_otvalor577"));
				c.setOtvalor578(tvalogar.get("parametros.pv_otvalor578"));
				c.setOtvalor579(tvalogar.get("parametros.pv_otvalor579"));
				c.setOtvalor580(tvalogar.get("parametros.pv_otvalor580"));
				c.setOtvalor581(tvalogar.get("parametros.pv_otvalor581"));
				c.setOtvalor582(tvalogar.get("parametros.pv_otvalor582"));
				c.setOtvalor583(tvalogar.get("parametros.pv_otvalor583"));
				c.setOtvalor584(tvalogar.get("parametros.pv_otvalor584"));
				c.setOtvalor585(tvalogar.get("parametros.pv_otvalor585"));
				c.setOtvalor586(tvalogar.get("parametros.pv_otvalor586"));
				c.setOtvalor587(tvalogar.get("parametros.pv_otvalor587"));
				c.setOtvalor588(tvalogar.get("parametros.pv_otvalor588"));
				c.setOtvalor589(tvalogar.get("parametros.pv_otvalor589"));
				c.setOtvalor590(tvalogar.get("parametros.pv_otvalor590"));
				c.setOtvalor591(tvalogar.get("parametros.pv_otvalor591"));
				c.setOtvalor592(tvalogar.get("parametros.pv_otvalor592"));
				c.setOtvalor593(tvalogar.get("parametros.pv_otvalor593"));
				c.setOtvalor594(tvalogar.get("parametros.pv_otvalor594"));
				c.setOtvalor595(tvalogar.get("parametros.pv_otvalor595"));
				c.setOtvalor596(tvalogar.get("parametros.pv_otvalor596"));
				c.setOtvalor597(tvalogar.get("parametros.pv_otvalor597"));
				c.setOtvalor598(tvalogar.get("parametros.pv_otvalor598"));
				c.setOtvalor599(tvalogar.get("parametros.pv_otvalor599"));
				c.setOtvalor600(tvalogar.get("parametros.pv_otvalor600"));
				c.setOtvalor601(tvalogar.get("parametros.pv_otvalor601"));
				c.setOtvalor602(tvalogar.get("parametros.pv_otvalor602"));
				c.setOtvalor603(tvalogar.get("parametros.pv_otvalor603"));
				c.setOtvalor604(tvalogar.get("parametros.pv_otvalor604"));
				c.setOtvalor605(tvalogar.get("parametros.pv_otvalor605"));
				c.setOtvalor606(tvalogar.get("parametros.pv_otvalor606"));
				c.setOtvalor607(tvalogar.get("parametros.pv_otvalor607"));
				c.setOtvalor608(tvalogar.get("parametros.pv_otvalor608"));
				c.setOtvalor609(tvalogar.get("parametros.pv_otvalor609"));
				c.setOtvalor610(tvalogar.get("parametros.pv_otvalor610"));
				c.setOtvalor611(tvalogar.get("parametros.pv_otvalor611"));
				c.setOtvalor612(tvalogar.get("parametros.pv_otvalor612"));
				c.setOtvalor613(tvalogar.get("parametros.pv_otvalor613"));
				c.setOtvalor614(tvalogar.get("parametros.pv_otvalor614"));
				c.setOtvalor615(tvalogar.get("parametros.pv_otvalor615"));
				c.setOtvalor616(tvalogar.get("parametros.pv_otvalor616"));
				c.setOtvalor617(tvalogar.get("parametros.pv_otvalor617"));
				c.setOtvalor618(tvalogar.get("parametros.pv_otvalor618"));
				c.setOtvalor619(tvalogar.get("parametros.pv_otvalor619"));
				c.setOtvalor620(tvalogar.get("parametros.pv_otvalor620"));
				c.setOtvalor621(tvalogar.get("parametros.pv_otvalor621"));
				c.setOtvalor622(tvalogar.get("parametros.pv_otvalor622"));
				c.setOtvalor623(tvalogar.get("parametros.pv_otvalor623"));
				c.setOtvalor624(tvalogar.get("parametros.pv_otvalor624"));
				c.setOtvalor625(tvalogar.get("parametros.pv_otvalor625"));
				c.setOtvalor626(tvalogar.get("parametros.pv_otvalor626"));
				c.setOtvalor627(tvalogar.get("parametros.pv_otvalor627"));
				c.setOtvalor628(tvalogar.get("parametros.pv_otvalor628"));
				c.setOtvalor629(tvalogar.get("parametros.pv_otvalor629"));
				c.setOtvalor630(tvalogar.get("parametros.pv_otvalor630"));
				c.setOtvalor631(tvalogar.get("parametros.pv_otvalor631"));
				c.setOtvalor632(tvalogar.get("parametros.pv_otvalor632"));
				c.setOtvalor633(tvalogar.get("parametros.pv_otvalor633"));
				c.setOtvalor634(tvalogar.get("parametros.pv_otvalor634"));
				c.setOtvalor635(tvalogar.get("parametros.pv_otvalor635"));
				c.setOtvalor636(tvalogar.get("parametros.pv_otvalor636"));
				c.setOtvalor637(tvalogar.get("parametros.pv_otvalor637"));
				c.setOtvalor638(tvalogar.get("parametros.pv_otvalor638"));
				c.setOtvalor639(tvalogar.get("parametros.pv_otvalor639"));
				c.setOtvalor640(tvalogar.get("parametros.pv_otvalor640"));
				lista.add(c);
			}
			
			paso = "Guardando configuraci\u00F3n";
			logger.debug("\nPaso: {}",paso);
			
			cdPaqueteNuevo = cotizacionDAO.guardarConfiguracionGarantias(
					cdramo
					,cdtipsit
					,cdplan
					,cdpaq
					,nuevo
					,dspaq
					,derpol
					,lista
					);
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdPaqueteNuevo=",cdPaqueteNuevo
				,"\n@@@@@@ guardarConfiguracionGarantias @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return cdPaqueteNuevo;
	}
	
	@Override
	public List<Map<String,String>> obtenerCoberturasPlanColec(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdsisrol
			)throws Exception
	{
		return cotizacionDAO.obtenerCoberturasPlanColec(cdramo,cdtipsit,cdplan,cdsisrol);
	}

	@Override
	public List<Map<String,String>> obtieneCobeturasNombrePlan(
			String cdramo
			,String cdtipsit
			,String cdplan
			)throws Exception
	{
		return cotizacionDAO.obtieneCobeturasNombrePlan(cdramo,cdtipsit,cdplan);
	}

	@Override
	public boolean ejectutaBloqueoProcesoTramite(
            String ntramite
           ,String claveProceso
           ,String cdrol
           ,String descripcion
           ,String valor
           ,String operacion
           )throws Exception
	{
	    return cotizacionDAO.ejectutaBloqueoProcesoTramite(
	            ntramite
	            ,claveProceso
	            ,cdrol
	            ,descripcion
	            ,valor
	            ,operacion
	            );
	}

	@Override
	public Map<String,String> consultaBloqueoProcesoTramite(String ntramite, String claveProceso)throws Exception
	{
	    return cotizacionDAO.consultaBloqueoProcesoTramite(ntramite,claveProceso);
	}

	@Override
	public Map<String,String> obtieneDatosContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		List<Map<String,String>> res  = cotizacionDAO.obtieneDatosContratantePoliza(cdunieco,cdramo,estado,nmpoliza, nmsuplem);
		
		Map<String,String> datosCont = null;
		if(res!= null && !res.isEmpty()){
			datosCont = res.get(0);
		}
		
		return datosCont;
	}
	
	@Override
	public void actualizaCesionComision(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		cotizacionDAO.actualizaCesionComision(cdunieco,cdramo,estado,nmpoliza);
	}

	@Override
	public void actualizaDomicilioAseguradosColectivo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdpostal
			,String cdedo
			,String cdmunici
			)throws Exception
	{
		cotizacionDAO.actualizaDomicilioAseguradosColectivo(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdpostal, cdedo, cdmunici);
	}
	
	@Override
	public void procesoColectivoAsincrono(
			boolean hayTramite
			,boolean hayTramiteVacio
			,boolean censoAtrasado
			,boolean complemento
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String cdperpag
			,String clasif
			,String LINEA
			,String LINEA_EXTENDIDA
			,List<Map<String,Object>> olist1
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,boolean duplicar
			)
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ procesoColectivoAsincrono @@@@@@"
				,"\n@@@@@@ hayTramite="      , hayTramite
				,"\n@@@@@@ hayTramiteVacio=" , hayTramiteVacio
				,"\n@@@@@@ censoAtrasado="   , censoAtrasado
				,"\n@@@@@@ complemento="     , complemento
				,"\n@@@@@@ cdunieco="        , cdunieco
				,"\n@@@@@@ cdramo="          , cdramo
				,"\n@@@@@@ nmpoliza="        , nmpoliza
				,"\n@@@@@@ cdperpag="        , cdperpag
				,"\n@@@@@@ clasif="          , clasif
				,"\n@@@@@@ LINEA="           , LINEA
				,"\n@@@@@@ LINEA_EXTENDIDA=" , LINEA_EXTENDIDA
				,"\n@@@@@@ olist1="          , olist1
				,"\n@@@@@@ cdtipsit="        , cdtipsit
				,"\n@@@@@@ cdusuari="        , cdusuari
				,"\n@@@@@@ cdsisrol="        , cdsisrol
				,"\n@@@@@@ duplicar="        , duplicar
				));
		new ProcesoColectivoAsincrono(
				hayTramite
				,hayTramiteVacio
				,censoAtrasado
				,complemento
				,cdunieco
				,cdramo
				,nmpoliza
				,cdperpag
				,clasif
				,LINEA
				,LINEA_EXTENDIDA
				,olist1
				,cdtipsit
				,cdusuari
				,cdsisrol
				,duplicar
				).start();
	}
	
	private class ProcesoColectivoAsincrono extends Thread
	{
		private boolean hayTramite
		                ,hayTramiteVacio
		                ,censoAtrasado
		                ,resubirCenso
		                ,complemento
		                ,duplicar
		                ;
		
		private String cdunieco
		               ,cdramo
		               ,nmpoliza
		               ,cdperpag
		               ,clasif
		               ,LINEA
		               ,LINEA_EXTENDIDA
		               ,cdtipsit
		               ,cdusuari
		               ,cdsisrol
		               ;
		
		private List<Map<String,Object>> olist1;
		
		public ProcesoColectivoAsincrono(
				boolean hayTramite
				,boolean hayTramiteVacio
				,boolean censoAtrasado
				,boolean complemento
				,String cdunieco
				,String cdramo
				,String nmpoliza
				,String cdperpag
				,String clasif
				,String LINEA
				,String LINEA_EXTENDIDA
				,List<Map<String,Object>> olist1
				,String cdtipsit
				,String cdusuari
				,String cdsisrol
				,boolean duplicar
				)
		{
			this.hayTramite      = hayTramite;
			this.hayTramiteVacio = hayTramiteVacio;
			this.censoAtrasado   = censoAtrasado;
			this.complemento     = complemento;
			this.cdunieco        = cdunieco;
			this.cdramo          = cdramo;
			this.nmpoliza        = nmpoliza;
			this.cdperpag        = cdperpag;
			this.clasif          = clasif;
			this.LINEA           = LINEA;
			this.LINEA_EXTENDIDA = LINEA_EXTENDIDA;
			this.olist1          = olist1;
			this.cdtipsit        = cdtipsit;
			this.cdusuari        = cdusuari;
			this.cdsisrol        = cdsisrol;
			this.duplicar        = duplicar;
		}
		
		@Override
		public void run()
		{
			long timestamp = System.currentTimeMillis();
			
			logger.debug(Utils.log(
					 "\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"
					,"\n&&&&&& procesoColectivoAsincrono.RUN &&&&&&"
					,"\n&&&&&& [id="             , timestamp ,"]"
					,"\n&&&&&& hayTramite="      , hayTramite
					,"\n&&&&&& hayTramiteVacio=" , hayTramiteVacio
					,"\n&&&&&& censoAtrasado="   , censoAtrasado
					,"\n&&&&&& complemento="     , complemento
					,"\n&&&&&& cdunieco="        , cdunieco
					,"\n&&&&&& cdramo="          , cdramo
					,"\n&&&&&& nmpoliza="        , nmpoliza
					,"\n&&&&&& cdperpag="        , cdperpag
					,"\n&&&&&& clasif="          , clasif
					,"\n&&&&&& LINEA="           , LINEA
					,"\n&&&&&& LINEA_EXTENDIDA=" , LINEA_EXTENDIDA
					,"\n&&&&&& olist1="          , olist1
					,"\n&&&&&& cdtipsit="        , cdtipsit
					,"\n&&&&&& cdusuari="        , cdusuari
					,"\n&&&&&& cdsisrol="        , cdsisrol
					,"\n&&&&&& duplicar="        , duplicar
					));
			try
			{
				if(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||complemento||duplicar)
				{
					logger.debug(Utils.log("\n&&&&&& ejecutaValoresDefectoConcurrente [id=",timestamp,"] &&&&&&"));		
					cotizacionDAO.ejecutaValoresDefectoConcurrente(
		            		cdunieco
		            		,cdramo
		            		,"W"
		            		,nmpoliza
		            		,"0"
		            		,"0"
		            		,"1"
		            		,cdperpag
		            		);
				}
				
				if(clasif.equals(LINEA)&&LINEA_EXTENDIDA.equals("S"))
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						//HOSPITALIZACION (DEDUCIBLE)
						String cdgarant = "4HOS";
						String cdatribu = "001";
						String valor    = (String)iGrupo.get("deducible");
						logger.debug(Utils.log("\n&&&&&& movimientoTvalogarGrupo [id=",timestamp,"] &&&&&&"));
						movimientoTvalogarGrupo(cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", cdatribu, valor);
						
						//ASISTENCIA INTERNACIONAL VIAJES --AHORA MEDICAMENTOS
						String asisinte = (String)iGrupo.get("asisinte");
						if (StringUtils.isBlank(asisinte)) {
							asisinte = "0";
						}
						cdgarant = "4MED";
						if(Integer.parseInt(asisinte)>0)
						{
							logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
							movimientoMpoligarGrupo(
									cdunieco
									,cdramo
									,"W"
									,nmpoliza
									,"0"
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V"
									,"001"
									,Constantes.INSERT_MODE
									,"S"
									);
							
							logger.debug(Utils.log("\n&&&&&& movimientoTvalogarGrupo [id=",timestamp,"] &&&&&&"));
							movimientoTvalogarGrupo(
									cdunieco
									,cdramo
									,"W" //estado
									,nmpoliza
									,"0" //nmsuplem
									,cdtipsit
									,cdgrupo
									,cdgarant
									,"V" //status
									,"001"
									,asisinte
									);
						}
						else
						{
							logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
							movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
						
						//EMERGENCIA EXTRANJERO
						String emerextr = (String)iGrupo.get("emerextr");
						cdgarant = "4EE";
						if(emerextr.equalsIgnoreCase("S"))
						{
							logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
							movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
						}
						else
						{
							logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
							movimientoMpoligarGrupo(
									cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
						}
					}
				}
				else
				{
					for(Map<String,Object>iGrupo:olist1)
					{
						String cdgrupo = (String)iGrupo.get("letra");
						
						List<Map<String,String>>tvalogars=(List<Map<String,String>>)iGrupo.get("tvalogars");
						for(Map<String,String>iTvalogar:tvalogars)
						{
							String cdgarant  = iTvalogar.get("cdgarant");
							boolean amparada = StringUtils.isNotBlank(iTvalogar.get("amparada"))
									&&iTvalogar.get("amparada").equalsIgnoreCase("S");
							
							//if(!cdgarant.equalsIgnoreCase("4AYM"))
							//{
								if(amparada)
								{
									logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
									movimientoMpoligarGrupo(
											cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.INSERT_MODE, null);
									//buscar cdatribus
									boolean hayAtributos=false;
									Map<String,String>listaCdatribu=new HashMap<String,String>();
									for(Entry<String,String>iAtribTvalogar:iTvalogar.entrySet())
									{
										String key=iAtribTvalogar.getKey();
										if(key!=null
												&&key.length()>"parametros.pv_otvalor".length()
												&&key.substring(0, "parametros.pv_otvalor".length()).equalsIgnoreCase("parametros.pv_otvalor"))
										{
											hayAtributos=true;
											listaCdatribu.put(key.substring("parametros.pv_otvalor".length(), key.length()),iAtribTvalogar.getValue());
										}
									}
									if(hayAtributos)
									{
										for(Entry<String,String>atributo:listaCdatribu.entrySet())
										{
											if(StringUtils.isNotBlank(atributo.getValue()))
											{
												logger.debug(Utils.log("\n&&&&&& movimientoTvalogarGrupo [id=",timestamp,"] &&&&&&"));
											    movimientoTvalogarGrupo(
													cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V",
													atributo.getKey(), atributo.getValue());
											}
										}
									}
								}
								else
								{
									logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
									movimientoMpoligarGrupo(
											cdunieco, cdramo, "W", nmpoliza, "0", cdtipsit, cdgrupo, cdgarant, "V", "001", Constantes.DELETE_MODE, null);
								}
							//}
						}
					}
				}
				
				//cotizacionDAO.movimientoTbloqueo(cdunieco, cdramo, "W", nmpoliza, "-8", Constantes.DELETE_MODE);
				
				logger.debug(Utils.log("\n&&&&&& ejecutaTarificacionConcurrente [id=",timestamp,"] &&&&&&"));
				ejecutaTarificacionConcurrente(
	            		cdunieco
	            		,cdramo
	            		,"W"
	            		,nmpoliza
	            		,"0"
	            		,"0"
	            		,"1"
	            		,cdperpag
	            		,cdusuari
	            		,cdsisrol
	            		);
			}
			catch(Exception ex)
			{
				logger.error(Utils.log("Error en el proceso colectivo asincrono [id=",timestamp,"]"),ex);
			}
			
			logger.debug(Utils.log(
					 "\n&&&&&& [id=",timestamp,"]"
					,"\n&&&&&& procesoColectivoAsincrono.END &&&&&&"
					,"\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"
					));
		}
	}
	
	public void procesoColectivoAsincrono2(
			boolean hayTramite
			,boolean hayTramiteVacio
			,boolean censoAtrasado
			,boolean complemento
			,String cdunieco
			,String cdramo
			,String nmpoliza
			,String cdperpag
			,String clasif
			,String LINEA
			,String LINEA_EXTENDIDA
			,List<Map<String,Object>> grupos
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,boolean duplicar
			)
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ procesoColectivoAsincrono2 @@@@@@"
				,"\n@@@@@@ hayTramite="      , hayTramite
				,"\n@@@@@@ hayTramiteVacio=" , hayTramiteVacio
				,"\n@@@@@@ censoAtrasado="   , censoAtrasado
				,"\n@@@@@@ complemento="     , complemento
				,"\n@@@@@@ cdunieco="        , cdunieco
				,"\n@@@@@@ cdramo="          , cdramo
				,"\n@@@@@@ nmpoliza="        , nmpoliza
				,"\n@@@@@@ cdperpag="        , cdperpag
				,"\n@@@@@@ clasif="          , clasif
				,"\n@@@@@@ LINEA="           , LINEA
				,"\n@@@@@@ LINEA_EXTENDIDA=" , LINEA_EXTENDIDA
				,"\n@@@@@@ grupos="          , grupos
				,"\n@@@@@@ cdusuari="        , cdusuari
				,"\n@@@@@@ cdsisrol="        , cdsisrol
				,"\n@@@@@@ duplicar="        , duplicar
				));
		new ProcesoColectivoAsincrono2(
				hayTramite
				,hayTramiteVacio
				,censoAtrasado
				,complemento
				,cdunieco
				,cdramo
				,nmpoliza
				,cdperpag
				,clasif
				,LINEA
				,LINEA_EXTENDIDA
				,grupos
				,cdtipsit
				,cdusuari
				,cdsisrol
				,duplicar
				).start();
	}
	
	private class ProcesoColectivoAsincrono2 extends Thread
	{
		private boolean hayTramite
		                ,hayTramiteVacio
		                ,censoAtrasado
		                ,resubirCenso
		                ,complemento
		                ,duplicar
		                ;
		
		private String cdunieco
		               ,cdramo
		               ,nmpoliza
		               ,cdperpag
		               ,clasif
		               ,LINEA
		               ,LINEA_EXTENDIDA
		               ,cdtipsit
		               ,cdusuari
		               ,cdsisrol
		               ;
		
		private List<Map<String,Object>> grupos;
		
		public ProcesoColectivoAsincrono2(
				boolean hayTramite
				,boolean hayTramiteVacio
				,boolean censoAtrasado
				,boolean complemento
				,String cdunieco
				,String cdramo
				,String nmpoliza
				,String cdperpag
				,String clasif
				,String LINEA
				,String LINEA_EXTENDIDA
				,List<Map<String,Object>> grupos
				,String cdtipsit
				,String cdusuari
				,String cdsisrol
				,boolean duplicar
				)
		{
			this.hayTramite      = hayTramite;
			this.hayTramiteVacio = hayTramiteVacio;
			this.censoAtrasado   = censoAtrasado;
			this.complemento     = complemento;
			this.cdunieco        = cdunieco;
			this.cdramo          = cdramo;
			this.nmpoliza        = nmpoliza;
			this.cdperpag        = cdperpag;
			this.clasif          = clasif;
			this.LINEA           = LINEA;
			this.LINEA_EXTENDIDA = LINEA_EXTENDIDA;
			this.grupos          = grupos;
			this.cdtipsit        = cdtipsit;
			this.cdusuari        = cdusuari;
			this.cdsisrol        = cdsisrol;
			this.duplicar        = duplicar;
		}
		
		@Override
		public void run()
		{
			long timestamp = System.currentTimeMillis();
			
			logger.debug(Utils.log(
					 "\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"
					,"\n&&&&&& procesoColectivoAsincrono2.RUN &&&&&&"
					,"\n&&&&&& [id="             , timestamp ,"]"
					,"\n&&&&&& hayTramite="      , hayTramite
					,"\n&&&&&& hayTramiteVacio=" , hayTramiteVacio
					,"\n&&&&&& censoAtrasado="   , censoAtrasado
					,"\n&&&&&& complemento="     , complemento
					,"\n&&&&&& cdunieco="        , cdunieco
					,"\n&&&&&& cdramo="          , cdramo
					,"\n&&&&&& nmpoliza="        , nmpoliza
					,"\n&&&&&& cdperpag="        , cdperpag
					,"\n&&&&&& clasif="          , clasif
					,"\n&&&&&& LINEA="           , LINEA
					,"\n&&&&&& LINEA_EXTENDIDA=" , LINEA_EXTENDIDA
					,"\n&&&&&& grupos="          , grupos
					,"\n&&&&&& cdtipsit="        , cdtipsit
					,"\n&&&&&& cdusuari="        , cdusuari
					,"\n&&&&&& cdsisrol="        , cdsisrol
					,"\n&&&&&& duplicar="        , duplicar
					));
			try
			{
				if(!hayTramite||hayTramiteVacio||censoAtrasado||resubirCenso||complemento||duplicar)
				{
					logger.debug(Utils.log("\n&&&&&& ejecutaValoresDefectoConcurrente [id=",timestamp,"] &&&&&&"));
					cotizacionDAO.ejecutaValoresDefectoConcurrente(
							cdunieco
							,cdramo
							,"W" //estado
							,nmpoliza
							,"0" //nmsuplem
							,"0" //nmsituac
							,"1" //tipotari
							,cdperpag
							);
				}
				
				if((!clasif.equals(LINEA))||LINEA_EXTENDIDA.equals("N"))
				{
					for(Map<String,Object>grupoIte:grupos)
					{
						String grupoIteCdgrupo                     = (String)grupoIte.get("letra");
						List<Map<String,String>>grupoIteCoberturas = (List<Map<String,String>>)grupoIte.get("tvalogars");
						for(Map<String,String>grupoIteCoberturaIte:grupoIteCoberturas)
						{
							String grupoIteCoberturaIteCdgarant = grupoIteCoberturaIte.get("cdgarant");
							boolean grupoIteCoberturaIteAmparada = StringUtils.isNotBlank(grupoIteCoberturaIte.get("amparada"))
									&&grupoIteCoberturaIte.get("amparada").equalsIgnoreCase("S");
							if(grupoIteCoberturaIteAmparada)
							{
								logger.debug(Utils.log("\n&&&&&& movimientoMpoligarGrupo [id=",timestamp,"] &&&&&&"));
								cotizacionDAO.movimientoMpoligarGrupo(
										cdunieco
										,cdramo
										,"W"      //estado
										,nmpoliza
										,"0"      //nmsuplem
										,cdtipsit
										,grupoIteCdgrupo
										,grupoIteCoberturaIteCdgarant
										,"V"      //status
										,"001"    //cdmoneda
										,Constantes.INSERT_MODE, null
										);
								boolean grupoIteCoberturaIteTieneAtrib          = false;
								Map<String,String>grupoIteCoberturaIteTvalogars = new HashMap<String,String>();
								for(Entry<String,String>grupoIteCoberturaIteAtribIte:grupoIteCoberturaIte.entrySet())
								{
									String grupoIteCoberturaIteAtribIteKey=grupoIteCoberturaIteAtribIte.getKey();
									if(StringUtils.isNotBlank(grupoIteCoberturaIteAtribIteKey)
											&&grupoIteCoberturaIteAtribIteKey.length()>"parametros.pv_otvalor".length()
											&&grupoIteCoberturaIteAtribIteKey.substring(0, "parametros.pv_otvalor".length()).equalsIgnoreCase("parametros.pv_otvalor")
											&&grupoIteCoberturaIteAtribIte.getValue()!=null
											)
									{
										grupoIteCoberturaIteTieneAtrib=true;
										grupoIteCoberturaIteTvalogars.put(
												grupoIteCoberturaIteAtribIteKey.substring("parametros.pv_".length()
														,grupoIteCoberturaIteAtribIteKey.length()),String.valueOf(grupoIteCoberturaIteAtribIte.getValue()));
									}
								}
								if(grupoIteCoberturaIteTieneAtrib)
								{
									logger.debug(Utils.log("\n&&&&&& movimientoTvalogarGrupoCompleto [id=",timestamp,"] &&&&&&"));
									cotizacionDAO.movimientoTvalogarGrupoCompleto(
											cdunieco
											,cdramo
											,"W"      //estado
											,nmpoliza
											,"0"      //nmsuplem
											,cdtipsit
											,grupoIteCdgrupo
											,grupoIteCoberturaIteCdgarant
											,"V"      //status
											,grupoIteCoberturaIteTvalogars
											);
								}
							}
						}
					}
				}
				
				
				// Se inserta el maestro y detalle de los grupos:
				try {
	                cotizacionDAO.eliminarGrupos(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit);
	            } catch (Exception e) {
	                logger.warn("No se eliminaron los grupos de la poliza: {}", e);
	            }
				for(Map<String,Object> grupoIte : grupos) {
					logger.debug("grupoIte=={}", grupoIte);
					// Guardar el maestro de grupos mpoligrup:
					logger.debug(Utils.log("\n&&&&&& insertaMpoligrup [id=",timestamp,"] &&&&&&"));
					cotizacionDAO.insertaMpoligrup(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("nombre"), (String)grupoIte.get("cdplan"), (String)grupoIte.get("dsplanl"), null, "0", "0", Constantes.NO, Constantes.NO, Constantes.NO);
					// Guardar el detalle de grupos mgrupogar:
					logger.debug(Utils.log("\n&&&&&& insertaMgrupogar [id=",timestamp,"] &&&&&&"));
					cotizacionDAO.insertaMgrupogar(cdunieco, cdramo, Constantes.POLIZA_WORKING, nmpoliza, cdtipsit, (String)grupoIte.get("letra"), (String)grupoIte.get("cdplan"), "0");
				}
				
				
				logger.debug(Utils.log("\n&&&&&& eliminarMpolirec [id=",timestamp,"] &&&&&&"));
				cotizacionDAO.eliminarMpolirec(
						cdunieco
		        		,cdramo
		        		,"W"
		        		,nmpoliza
		        		,"0"
						);
				
				logger.debug(Utils.log("\n&&&&&& insertaMorbilidad [id=",timestamp,"] &&&&&&"));
				cotizacionDAO.insertaMorbilidad(cdunieco,cdramo,"W",nmpoliza,"0");
				
				logger.debug(Utils.log("\n&&&&&& ejecutaTarificacionConcurrente [id=",timestamp,"] &&&&&&"));
				cotizacionDAO.ejecutaTarificacionConcurrente(
						cdunieco
						,cdramo
						,"W" //estado
						,nmpoliza
						,"0" //nmsuplem
						,"0" //nmsituac
						,"1" //tipotari
						,cdperpag
						);
				logger.debug("@@@@@@@@@@@@@@@@@GRABANDO EVENTO DE COTIZACION 4@@@@@@@@@@@@@@@@@");
				cotizacionDAO.grabarEvento(new StringBuilder(), Constantes.MODULO_COTIZACION, Constantes.EVENTO_COTIZAR,
				        new Date(), cdusuari, cdsisrol, "", cdunieco, cdramo, "W", nmpoliza, nmpoliza, "", "", "", null);
			}
			catch(Exception ex)
			{
				logger.error(Utils.log("Error en el proceso colectivo asincrono 2 [id=",timestamp,"]"),ex);
			}
			
			logger.debug(Utils.log(
					 "\n&&&&&& [id=",timestamp,"]"
					,"\n&&&&&& procesoColectivoAsincrono2.END &&&&&&"
					,"\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"
					));
		}
	}
	
	@Deprecated
	@Override
	public List<Map<String,String>> recuperarListaDocumentosParametrizados(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception
	{
		return cotizacionDAO.recuperarListaDocumentosParametrizados(cdorddoc,nmsolici,ntramite);
	}
	
	/*
	@Deprecated
	@Override
	public List<Map<String,String>> generarDocumentosBaseDatos(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception
	{
		return cotizacionDAO.generarDocumentosBaseDatos(cdorddoc,nmsolici,ntramite);
	}
	*/
	
	@Override
	@Deprecated
	public void actualizarFefecsitMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		cotizacionDAO.actualizarFefecsitMpolisit(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
	}
	
	@Override
	public void borrarIncisoCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ borrarIncisoCotizacion @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				));
		
		String paso = null;
		try
		{
			paso = "Borrando asegurado";
			logger.debug(paso);
			
			cotizacionDAO.borrarIncisoCotizacion(cdunieco,cdramo,estado,nmpoliza,nmsituac);
			
			paso = "Reenumerando p\u00f3liza";
			logger.debug(paso);
			
			cotizacionDAO.reenumerarSituaciones(cdunieco,cdramo,estado,nmpoliza,"0",nmsituac);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ borrarIncisoCotizacion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	@Deprecated
	public boolean isEstatusGeneraDocumentosCotizacion(String status) throws Exception
	{
		return cotizacionDAO.isEstatusGeneraDocumentosCotizacion(status);
	}
    
	@Override
	public void guardarCensoCompletoMultisaludEndoso(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdedo
			,String cdmunici
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			,String nmsuplem
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarCensoCompleto @@@@@@"
				,"\n@@@@@@ nombreArchivo=" , nombreArchivo
				,"\n@@@@@@ cdunieco="      , cdunieco
				,"\n@@@@@@ cdramo="        , cdramo
				,"\n@@@@@@ estado="        , estado
				,"\n@@@@@@ mpoliza="       , nmpoliza
				,"\n@@@@@@ cdedo="         , cdedo
				,"\n@@@@@@ cdmunici="      , cdmunici
				,"\n@@@@@@ cdplan1="       , cdplan1
				,"\n@@@@@@ cdplan2="       , cdplan2
				,"\n@@@@@@ cdplan3="       , cdplan3
				,"\n@@@@@@ cdplan4="       , cdplan4
				,"\n@@@@@@ cdplan5="       , cdplan5
				,"\n@@@@@@ complemento="   , complemento
				,"\n@@@@@@ nmsuplem="      , nmsuplem
				));
		
		cotizacionDAO.guardarCensoCompletoMultisaludEndoso(
				nombreArchivo
				,cdunieco
				,cdramo
				,estado
				,nmpoliza
				,cdedo
				,cdmunici
				,cdplan1
				,cdplan2
				,cdplan3
				,cdplan4
				,cdplan5
				,complemento
				,nmsuplem
				);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarCensoCompleto @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void ejecutasigsvdefEnd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdgarant
			,String cdtipsup
			)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ ejecutasigsvdefEnd @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ cdgarant=" , cdgarant
				,"\n@@@@@@ cdtipsup=" , cdtipsup
				));
		
		String paso = null;
		try
		{
			cotizacionDAO.sigsvdefEnd(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsituac
				,nmsuplem
				,cdgarant
				,cdtipsup
			);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ ejecutasigsvdefEnd @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	@Deprecated
	public void restaurarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		cotizacionDAO.restaurarRespaldoCenso(cdunieco,cdramo,estado,nmpoliza);
	}
	
	@Override
	@Deprecated
	public void borrarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String nmpoliza
			)throws Exception
	{
		cotizacionDAO.borrarRespaldoCenso(cdunieco,cdramo,nmpoliza);
	}

	@Override
	public void borrarMpoliperSituac0(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
			String cdrol)throws Exception{
		cotizacionDAO.borrarMpoliperSituac0(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdrol);
	}
	
	@Override
	public void guardarLayoutGenerico(
			String nombreArchivo
			)throws Exception
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarLayoutGenerico @@@@@@"
				,"\n@@@@@@ nombreArchivo=" , nombreArchivo
				));
		
		cotizacionDAO.guardarLayoutGenerico(
				nombreArchivo
				);
		
		logger.debug(Utils.log(
				"\n@@@@@@ guardarLayoutGenerico @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	@Deprecated
	public void movimientoTbloqueo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String accion
			)throws Exception
	{
		cotizacionDAO.movimientoTbloqueo(cdunieco,cdramo,estado,nmpoliza,nmsituac,accion);
	}
	
	@Override
	public String consultaExtraprimOcup(
			String cdtipsit
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ consultaExtraprimOcup@@@@@@"
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				));

		String paso = null;
		String otvalor = null;

		try{
			paso = "Recuperando coberturas de extraprimas";
			logger.debug(paso);
			
			otvalor = cotizacionDAO.consultaExtraprimOcup(cdtipsit);
			
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ consultaExtraprimOcup @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return otvalor;
	}
	
	@Override
	public void actualizarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			,String otvalor
			,String accion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ actualizarOtvalorTramitePorDsatribu @@@@@@"
				,"\n@@@@@@ ntramite=", ntramite
				,"\n@@@@@@ dsatribu=", dsatribu
				,"\n@@@@@@ otvalor=" , otvalor
				,"\n@@@@@@ accion="  , accion
				));
		
		String paso = "Ejecutando actualizaci\u00f3n de valor din\u00e1mico de tr\u00e1mite";
		
		try
		{
			mesaControlDAO.actualizarOtvalorTramitePorDsatribu(ntramite,dsatribu,otvalor,accion);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ actualizarOtvalorTramitePorDsatribu @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public String recuperarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarOtvalorTramitePorDsatribu @@@@@@"
				,"\n@@@@@@ ntramite=", ntramite
				,"\n@@@@@@ dsatribu=", dsatribu
				));
		
		String paso     = "Ejecutando actualizaci\u00f3n de valor din\u00e1mico de tr\u00e1mite"
		       ,otvalor = null;
		
		try
		{
			otvalor = mesaControlDAO.recuperarOtvalorTramitePorDsatribu(ntramite,dsatribu);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ otvalor=", otvalor
				,"\n@@@@@@ recuperarOtvalorTramitePorDsatribu @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return otvalor;
	}
	
	@Override
	public List<Map<String,String>> cargarAseguradosExtraprimas2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit
			)throws Exception
	{
		logger.info(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarAseguradosExtraprimas2 @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ cdgrupo="  , cdgrupo
				,"\n@@@@@@ start="    , start
				,"\n@@@@@@ limit="    , limit
				));
		
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		
		String paso = "Recuperando asegurados extraprima";
		
		//cargar situaciones grupo
		try
		{
			List<Map<String,String>>situaciones=cotizacionDAO.cargarSituacionesGrupo(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdgrupo
					,start
					,limit);
			
			for(Map<String,String>situacion:situaciones)
		    {
		    	String tpl = null;
		    	if(StringUtils.isBlank(situacion.get("titular")))
		    	{
		    		tpl = "Asegurados";
		    	}
		    	else
		    	{
		    		tpl = Utils.join(
		    				"Familia (" , situacion.get("familia") , ") de " , situacion.get("titular")
		    				);
		    	}
		    	situacion.put("agrupador",
		    			Utils.join(StringUtils.leftPad(situacion.get("familia"),3,"0") , "_" , tpl)
		    			);
		    	
		    	Map<String,String>editada=new HashMap<String,String>();
		    	for(Entry<String,String>en:situacion.entrySet())
		    	{
		    		String key = en.getKey();
		    		if(StringUtils.isNotBlank(key)
		    				&&key.length()>"otvalor".length()
		    				&&key.substring(0, "otvalor".length()).equals("otvalor")
		    				)
		    		{
		    			editada.put(new StringBuilder("parametros.pv_").append(key).toString(),en.getValue());
		    		}
		    		else
		    		{
		    			editada.put(key,en.getValue());
		    		}
		    	}
		    	lista.add(editada);
		    }
			Map<String,String>	total = lista.remove(lista.size()-1);
			Map<String,String> tot = situaciones.remove(situaciones.size()-1);
			tot.put("total", tot.get("total"));
	    	lista.add(tot);
		}		
		catch(ApplicationException ax)
		{
			Utils.generaExcepcion(ax, paso);
		}
		
		logger.info(Utils.log(
				 "\n@@@@@@ lista=" , lista
				,"\n@@@@@@ cargarAseguradosExtraprimas2 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return lista;
	}
	
	@Override
	public String obtenSumaAseguradosMedicamentos(
			String cdramo,
			String cdtipsit,
			String cdgarant
			)throws Exception
	{
		logger.info(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtenSumaAseguradosMedicamentos @@@@@@"
				,"\n@@@@@@ cdramo   ="   , cdramo
				,"\n@@@@@@ cdtipsit ="   , cdtipsit
				,"\n@@@@@@ cdgarant ="   , cdgarant
				));
		
		String sumaAseguradosMed = cotizacionDAO.obtenSumaAseguradosMedicamentos(cdramo, cdtipsit, cdgarant);
		
		
		
		logger.info(Utils.log(
				 "\n@@@@@@ sumaAseguradosMed=" , sumaAseguradosMed
				,"\n@@@@@@ obtenSumaAseguradosMedicamentos @@@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return sumaAseguradosMed;
		
		
	}
	
	@Override
	public String recuperarDescripcionEstatusTramite (String status) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ recuperarDescripcionEstatusTramite @@@@@@",
				"\n@@@@@@ status = ", status));
		String dsstatus = null;
		String paso = "Recuperando descripci\u00f3n de status";
		try {
			List<Map<String, String>> lista = flujoMesaControlDAO.recuperaTestadomc(status);
			if (lista == null || lista.size() == 0) {
				throw new ApplicationException(Utils.join("No hay descripciones para el estatus '", status,"'"));
			}
			for (Map<String, String> estado : lista) {
				if (status.equals(estado.get("CDESTADOMC"))) {
					dsstatus = estado.get("DSESTADOMC");
					break;
				}
			}
			if (StringUtils.isBlank(dsstatus)) {
				throw new ApplicationException(Utils.join("No hay descripci\u00f3n para el estatus '", status,"'"));
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				"\n@@@@@@ dsstatus = ", dsstatus,
				"\n@@@@@@ recuperarDescripcionEstatusTramite @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return dsstatus;
	}
	
	@Override
	@Deprecated // LOS METODOS DE MANAGER SIN LOGICA QUE SOLO INVOCAN AL DAO DEBEN DEJAR DE USARSE
	public void actualizarCdplanGrupo(String cdunieco, String cdramo, String estado, String nmpoliza,
		String nmsuplem, String cdgrupo, String cdplan) throws Exception {
		emisionDAO.actualizarCdplanGrupo(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdgrupo, cdplan);
	}
	
	@Override
	public Map<String,String> obtieneFormapago(String administradora,String retenedora) throws Exception{
		
		List<Map<String,String>> map=consultasDAO.formasPagoRetenedora(administradora, retenedora);
		if(map.get(0)!=null){
			return map.get(0);
		}
		return null;
	}
	
	@Override
    public List<Map<String,String>> obtieneRetAdmin(String administradora,String retenedora) throws Exception{
        
        List<Map<String,String>> list=consultasDAO.obtieneRetAdmin(administradora, retenedora);
        
        return list;
    }
	
	@Override
	public List<Map<String,String>> buescaEmpleado(
	         String pv_administradora_i
	        ,String pv_retenedora_i
	        ,String pv_clave_i
			,String pv_nombre_i 
			,String pv_apellido_paterno_i 
			,String pv_apellido_materno_i 
			,String pv_rfc_i 
			) throws Exception{
		
	    List<Map<String,String>> res=null;
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ buescaEmpleado @@@@@@"
				));
		
			String paso="";
			
			try{
				
				paso="busca datos";
				res=new ArrayList<Map<String,String>>();
				
				res=cotizacionDAO.buscaEmpleado(pv_administradora_i,pv_retenedora_i,pv_clave_i, pv_nombre_i, pv_apellido_paterno_i, pv_apellido_materno_i, pv_rfc_i);
			}catch(Exception ex) {
				Utils.generaExcepcion(ex, paso);
			}
			
			logger.debug(Utils.log(
					"\n@@@@@@ ",
					"\n@@@@@@ buescaEmpleado @@@@@@",
					"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return res;
		
	}
	
	
	@Override
    public String guardaEmpleado(String pv_numsuc_i
            ,String pv_cveent_i
            ,String pv_cveemp_i
            ,String pv_nomemp_i
            ,String pv_apaterno_i
            ,String pv_amaterno_i
            ,String pv_rfc_i
            ,String pv_curp_i
            ,String pv_usuario_i
            ,String pv_feregist_i
            ,String pv_accion_i
            ) throws Exception{
        
        logger.debug(Utils.log(
                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ,"\n@@@@@@ guardaEmpleado @@@@@@"
                ,"\n@@@@@@ "
                ));
        
            String paso="";
            
            try{
                
                paso="guardar enpleado";
                cotizacionDAO.guardaEmpleado(pv_numsuc_i, pv_cveent_i, pv_cveemp_i, pv_nomemp_i, pv_apaterno_i, pv_amaterno_i, pv_rfc_i, pv_curp_i, pv_usuario_i, pv_feregist_i, pv_accion_i);
            }catch(Exception ex) {
                Utils.generaExcepcion(ex, paso);
            }
            
            logger.debug(Utils.log(
                    "\n@@@@@@ ",
                    "\n@@@@@@ guardaEmpleado @@@@@@",
                    "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
        return "";
        
    }

    @Override
    public Map<String, String> obtieneValidacionDescuentoR6(String tipoUnidad, String uso, String zona,
            String promotoria, String cdagente, String cdtipsit, String cdatribu) throws Exception {
        return cotizacionDAO.obtieneValidacionDescuentoR6(tipoUnidad, uso, zona,
                promotoria, cdagente, cdtipsit, cdatribu);
    }

    @Override
    public List<ComponenteVO> obtenerAtributosPolizaOriginal(Map<String, String> params) throws Exception {
        if(!params.containsKey("pv_cdatrivar_i")) {
            params.put("pv_atrivar_i" , null);
        }
        return cotizacionDAO.obtenerAtributosPolizaOriginal(params);
    }

    @Override
    public String obtenerAseguradoDuplicado(HashMap<String, Object> paramPersona) throws Exception {
        try {
            return cotizacionDAO.obtenerAseguradoDuplicado(paramPersona);
        } catch (Exception daoExc) {
            throw new Exception(daoExc.getMessage(), daoExc);
        }
    }
    
    @Override
    public void agregarAsegurado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
            String feefecto, String dsnombre, String dsnombre1, String paterno, String materno, String cdrfc,
            String sexo, String fenacimi, String cdestciv, String dsocupacion, String cdtipsit, String cdplan,
            String nmorddom, String cdagrupa, String otvalor01, String otvalor02, String otvalor03, String otvalor04,
            String otvalor05, String otvalor06, String otvalor07, String otvalor08, String otvalor09, String otvalor10)
            throws Exception {
        
        aseguradoDAO.agregarAsegurado(cdunieco, cdramo, estado, nmpoliza, nmsuplem, feefecto, dsnombre, dsnombre1,
                paterno, materno, cdrfc, sexo, fenacimi, cdestciv, dsocupacion, cdtipsit, cdplan, nmorddom, cdagrupa, otvalor01, otvalor02,
                otvalor03, otvalor04, otvalor05, otvalor06, otvalor07, otvalor08, otvalor09, otvalor10);
    }

    @Override
    public String validaCertificadoGrupo(HashMap<String, Object> paramCertificado) throws Exception {
        return cotizacionDAO.validaCertificadoGrupo(paramCertificado);
    }
    
    public void guardaDatosAgenteSecundarioSigs(String ntramite,String agt_sec, String porc_part) throws Exception {
        String paso = "Actualizando Otvalor mesa de control";
        try {
            paso = "Insertando Agente Secundario";
            mesaControlDAO.actualizarOtvalorTramitePorDsatribu(ntramite, "AUX%CLAVE%AGENTE%SECUNDARIO", agt_sec, "I");
            paso = "Insertando Porcentaje Participacion Agente Secundario";
            mesaControlDAO.actualizarOtvalorTramitePorDsatribu(ntramite, "AUX%PARTICIPACI%N%AGENTE%SECUNDARIO", porc_part, "I");
        } 
        catch(Exception ex)
        {
            Utils.generaExcepcion(ex, paso);
        }
    }
	
    @Override
    public void refrescarCensoColectivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsolici)throws Exception
    {
        cotizacionDAO.refrescarCensoColectivo(cdunieco,cdramo,estado,nmpoliza,nmsolici);
    }
    
	public AseguradosFiltroVO cargarAseguradosFiltroGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			//,String nmsitaux
			,String start
			,String limit
			,String filtro
			,String valorFiltro)throws Exception
	{
		String paso = null;
    	AseguradosFiltroVO asegurados = new AseguradosFiltroVO();
		
        try {
    		
    		logger.info(""
				+ "\n#######################################"
				+ "\n###### cargarAseguradosGrupoImpl ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				//+ "\ncdgrupo "+cdgrupo
				//+ "\nnmsitaux "+nmsitaux
				+ "\nfiltro "+filtro
				+ "\nvalorFiltro "+valorFiltro
				+ "\nstart "+start
				+ "\nlimit "+limit
				);

        	HashMap<String,String>params=new HashMap<String,String>();
			paso = "Seteando parametros antes de llamar al metodo cargarAseguradosFiltroGrupo";
			logger.debug(paso);
			
			params.put("pv_cdunieco_i"   , cdunieco);
			params.put("pv_cdramo_i"   , cdramo);
			params.put("pv_estado_i"   , estado);
			params.put("pv_nmpoliza_i" , nmpoliza);
			params.put("pv_nmsuplem_i" , nmsuplem);
			params.put("pv_cdgrupo_i"  , cdgrupo);
			//params.put("pv_nmsitaux_i"  , nmsitaux);
			params.put("pv_start_i"  , start);
			params.put("pv_limit_i"  , limit);
			params.put("pv_dsatribu_i"  , filtro);
			params.put("pv_otvalor_i"  , valorFiltro);
			
			//asegurados = consultasAseguradoFiltroDAO.cargarAseguradosFiltroGrupo(params);
			
			asegurados = cotizacionDAO.cargarAseguradosFiltroGrupo(params);
			paso = "Despues de llamar al metodo cargarAseguradosFiltroGrupo";
			logger.debug(paso);
			
			logger.info(""
					+ "\nlista size "+asegurados.getTotal()
					+ "\n###### cargarAseguradosFiltroGrupo ######"
					+ "\n#########################################"
					);
        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso);
        }
		return asegurados;
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosFiltroExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit
			,String filtro
			,String valorFiltro)throws Exception
	{
		logger.debug(""
				+ "\n#########################################"
				+ "\n###### cargarAseguradosFiltroExtraprimas ######"
				+ "\ncdunieco "+cdunieco
				+ "\ncdramo "+cdramo
				+ "\nestado "+estado
				+ "\nnmpoliza "+nmpoliza
				+ "\nnmsuplem "+nmsuplem
				+ "\ncdgrupo "+cdgrupo
				+ "\nstart "+start
				+ "\nlimit "+limit
				+ "\nfiltro "+filtro
				+ "\nvalorFiltro "+valorFiltro
				);
		
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("start"    , start);
		params.put("limit"    , limit);
		params.put("pv_dsatribu_i"    , filtro);
		params.put("pv_otvalor_i"    , valorFiltro);
		List<Map<String,String>> lista = cotizacionDAO.cargarAseguradosFiltroExtraprimas(params);		
		logger.debug(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAseguradosFiltroExtraprimas ######"
				+ "\n#########################################"
				);
		return lista;
	}
	
	@Override
	public List<Map<String,String>> cargarAseguradosFiltroExtraprimas2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit
			,String filtro
			,String valorFiltro
			) throws Exception
	{
		logger.info(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarAseguradosFiltroExtraprimas2 @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ cdgrupo="  , cdgrupo
				,"\n@@@@@@ start="  , start
				,"\n@@@@@@ limit="  , limit
				,"\n@@@@@@ filtro="  ,filtro
				,"\n@@@@@@ valorFiltro="  , valorFiltro
				));
		
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		
		String paso = "Recuperando asegurados extraprima con filtro";
		
		//cargar situaciones grupo
		try
		{
			List<Map<String,String>>situaciones=cotizacionDAO.cargarSituacionesFiltroGrupo(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdgrupo
					,start
					,limit
					,filtro
					,valorFiltro);
			
			for(Map<String,String>situacion:situaciones)
		    {
		    	String tpl = null;
		    	if(StringUtils.isBlank(situacion.get("titular")))
		    	{
		    		tpl = "Asegurados";
		    	}
		    	else
		    	{
		    		tpl = Utils.join(
		    				"Familia (" , situacion.get("familia") , ") de " , situacion.get("titular")
		    				);
		    	}
		    	situacion.put("agrupador",
		    			Utils.join(StringUtils.leftPad(situacion.get("familia"),3,"0") , "_" , tpl)
		    			);
		    	
		    	Map<String,String>editada=new HashMap<String,String>();
		    	for(Entry<String,String>en:situacion.entrySet())
		    	{
		    		String key = en.getKey();
		    		if(StringUtils.isNotBlank(key)
		    				&&key.length()>"otvalor".length()
		    				&&key.substring(0, "otvalor".length()).equals("otvalor")
		    				)
		    		{
		    			editada.put(new StringBuilder("parametros.pv_").append(key).toString(),en.getValue());
		    		}
		    		else
		    		{
		    			editada.put(key,en.getValue());
		    		}
		    	}
		    	lista.add(editada);
		    }
		}
		catch(ApplicationException ax)
		{
			Utils.generaExcepcion(ax, paso);
		}
		
		return lista;
	}
	
	@Override
	public String validaAgenteActivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception{
	    logger.info(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ validaAgenteActivo @@@@@@"
               ,"\n@@@@@@ cdunieco=" , cdunieco
               ,"\n@@@@@@ cdramo="   , cdramo
               ,"\n@@@@@@ estado="   , estado
               ,"\n@@@@@@ nmpoliza=" , nmpoliza
               ,"\n@@@@@@ nmsuplem=" , nmsuplem
               ));
	    String mensaje = "";
	    try{
            String ntramite = consultasManager.recuperarTramitePorNmsuplem(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
            if(ntramite != null){
                Map<String, String> mapTramite = siniestrosManager.obtenerTramiteCompleto(ntramite);
                logger.debug("validarAgenteParaNuevoTramite tramite = {}", mapTramite.get("NTRAMITE"));
                logger.debug("validarAgenteParaNuevoTramite tiptra = {}", mapTramite.get("CDTIPTRA"));
                logger.debug("validarAgenteParaNuevoTramite agente = {}", mapTramite.get("CDAGENTE"));
                logger.debug("validarAgenteParaNuevoTramite ramo = {}", mapTramite.get("CDSUBRAM"));
                if(TipoTramite.RENOVACION.getCdtiptra().equals(mapTramite.get("CDTIPTRA"))){
                    try{
                        autosSIGSDAO.validarAgenteParaNuevoTramite(mapTramite.get("CDAGENTE"), mapTramite.get("CDSUBRAM"), "P");
                    }
                    catch(Exception ex){
                        mensaje = ex.getMessage();
                    }
                }
            }
        }
        catch(Exception ex){
            logger.error("No se pudo obtener el tramite de salud", ex);
        }
	    return mensaje;
	}
	
	@Override
	public HashMap<String, String> obtieneCodigosPostalesProductos() throws Exception {
		return cotizacionDAO.obtieneCodigosPostalesProductos();
	}
	
	@Override
    public String aplicaDescAutos(
             String cdunieco
            ,String cdramo
            ,String nmpoliza
            ,String modPrim
            ,String cdtipsit
            )throws Exception
    {
        //////////////////////////////////////////
        ////// alterar mpolirec sin recotizar/////
        String paso = "Modificacion de primas al aplicar descuento";
        try
        {
             return cotizacionDAO.aplicaDescAutos(cdunieco, cdramo, nmpoliza, modPrim,cdtipsit.isEmpty()?"1":"0");
        }
        catch(Exception ex)
        {
            throw new ApplicationException(paso);
        }
        ////// alterar mpolirec sin recotizar/////
        //////////////////////////////////////////
    }
	
	@Override
    public String validaDatosAutoSigs(List<Map<String,String>> incisosSigs) throws Exception
    {
        return  cotizacionDAO.validaDatosAutoSigs(incisosSigs);
        
    }
    
	@Override
	public List<Map<String, String>> cargarResultadosCotizacion(
	            String cdusuari, String cdunieco, String cdramo, String estado,
	            String nmpoliza, String cdelemen, String cdtipsit) throws Exception
	{
	      return cotizacionDAO.cargarResultadosCotizacion(cdusuari, cdunieco, cdramo, estado, nmpoliza, cdelemen, cdtipsit);
	}
	
    @Override
    public List<Map<String, String>> cargarResultadosCotizacionAutoFlotilla(
            String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception
    {
        return cotizacionDAO.cargarResultadosCotizacionAutoFlotilla(cdunieco, cdramo, estado, nmpoliza);
        
    }
    
    @Override
    public List<Map<String, String>> cargarResultadosCotizacionLicenciaFlotilla(
            String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception
    {
        return cotizacionDAO.cargarResultadosCotizacionLicenciaFlotilla(cdunieco, cdramo, estado, nmpoliza);
        
    }
    
    @Override
	public void eliminaDocsCotiza(String cdunieco, String cdramo, String estado, String nmpoliza,
			String ntramite) throws Exception{
		this.cotizacionDAO.eliminaDocsCotiza(cdunieco, cdramo, estado, nmpoliza, ntramite);
		
	}

	@Override
	public void insertaMpoligrup(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String dsgrupo, String cdplan, String dsplanVariable, String cdcveplan, String nmsumaaseg, String nmdeducible,
			String swmat, String swmed, String swee) throws Exception{
		this.cotizacionDAO.insertaMpoligrup(cdunieco, cdramo, estado, nmpoliza, cdtipsit, cdgrupo, dsgrupo, cdplan, dsplanVariable, cdcveplan, nmsumaaseg, nmdeducible, swmat, swmed, swee);
	}

	@Override
	public void insertaMgrupogar(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String cdplan, String nmsumaaseg) throws Exception{
		this.cotizacionDAO.insertaMgrupogar(cdunieco, cdramo, estado, nmpoliza, cdtipsit, cdgrupo, cdplan, nmsumaaseg);
	}

	@Override
	public void eliminarGrupos(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit) throws Exception{
		this.cotizacionDAO.eliminarGrupos(cdunieco, cdramo, estado, nmpoliza, cdtipsit);
	}
	
	@Override
	public String aplicaRecargoPagoFraccionado(String cdunieco, String cdramo, String nmpoliza, String recargoPF, String flotilla) throws Exception{
		return this.cotizacionDAO.aplicaRecargoPagoFraccionado( cdunieco,  cdramo,  nmpoliza,  recargoPF,  flotilla);
	}
	
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	////////////////  GETTERS Y SETTERS  ////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	
	public void setCotizacionDAO(CotizacionDAO cotizacionDAO) {
		this.cotizacionDAO = cotizacionDAO;
	}

	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}

	public void setPersonasDAO(PersonasDAO personasDAO) {
		this.personasDAO = personasDAO;
	}

	public void setMesaControlDAO(MesaControlDAO mesaControlDAO) {
		this.mesaControlDAO = mesaControlDAO;
	}

	public void setConsultasDAO(ConsultasDAO consultasDAO) {
		this.consultasDAO = consultasDAO;
	}


    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    ////////////////  GETTERS Y SETTERS  ////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////

}