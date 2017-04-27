package mx.com.gseguros.mesacontrol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.mesacontrol.dao.impl.FlujoMesaControlDAOImpl;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.model.TvaloperVO;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ParametroGeneral;
import mx.com.gseguros.portal.despachador.dao.DespachadorDAO;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.MailService;
import mx.com.gseguros.portal.general.util.FlujoMC;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.RolSistema;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;

@Service
public class FlujoMesaControlManagerImpl implements FlujoMesaControlManager
{
	private static Logger logger = LoggerFactory.getLogger(FlujoMesaControlManagerImpl.class);
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private AutosSIGSDAO autosSIGSDAO;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private SiniestrosManager  siniestrosManager;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;
	
	@Autowired
	private EndososDAO endososDAO;
	
	@Autowired
	private DespachadorDAO despachadorDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Override
	public Map<String,Item> workflow(String cdsisrol) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ workflow @@@@@@"
				,"\n@@@@@@ cdsisrol=",cdsisrol
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			List<ComponenteVO> ttipfluCmp = pantallasDAO.obtenerComponentes(null, null, null, null, null, cdsisrol, "FLUJOMC", "TTIPFLU", null);
			gc.generaComponentes(ttipfluCmp, true, false, true, false, false, false);
			
			items.put("ttipfluFormItems" , gc.getItems());
			
			List<ComponenteVO> tdocumeCmp = pantallasDAO.obtenerComponentes(null, null, null, null, null, cdsisrol, "FLUJOMC", "TDOCUME", null);
			gc.generaComponentes(tdocumeCmp, true, false, true, false, false, false);
			
			items.put("tdocumeFormItems" , gc.getItems());
			
			List<ComponenteVO> trequisiCmp = pantallasDAO.obtenerComponentes(null, null, null, null, null, cdsisrol, "FLUJOMC", "TREQUISI", null);
			gc.generaComponentes(trequisiCmp, true, false, true, false, false, false);
			
			items.put("trequisiFormItems" , gc.getItems());
			
			List<ComponenteVO> comboCdtipram = pantallasDAO.obtenerComponentes(null, null, null, null, null, cdsisrol, "FLUJOMC", "COMBO_CDTIPRAM", null);
			gc.generaComponentes(comboCdtipram, true, false, true, false, false, false);
			
			items.put("comboCdtipram" , gc.getItems());
			
			List<ComponenteVO> comboEtapa = pantallasDAO.obtenerComponentes(null, null, null, null, null, cdsisrol, "FLUJOMC", "COMBO_ETAPA", null);
			gc.generaComponentes(comboEtapa, true, false, true, false, false, false);
			
			items.put("comboEtapa" , gc.getItems());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ workflow @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	@Override
	public String movimientoTtipflumc(
			String accion
			,String cdtipflu
			,String dstipflu
			,String cdtiptra
			,String swreqpol
			,String swmultipol
			,String cdtipsup
			,String cdtipmod
            ,String swexterno
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoTtipflumc @@@@@@"
				,"\n@@@@@@ accion     = " , accion
				,"\n@@@@@@ cdtipflu   = " , cdtipflu
				,"\n@@@@@@ dstipflu   = " , dstipflu
				,"\n@@@@@@ cdtiptra   = " , cdtiptra
				,"\n@@@@@@ swreqpol   = " , swreqpol
				,"\n@@@@@@ swmultipol = " , swmultipol
				,"\n@@@@@@ cdtipsup   = " , cdtipsup
				,"\n@@@@@@ cdtipmod   = " , cdtipmod
                ,"\n@@@@@@ swexterno  = " , swexterno
				));
		
		String paso = "Guardando tr\u00E1mite";
		logger.debug(paso);
		
		String cdtipfluRes = null;
		
		try
		{
			cdtipfluRes = flujoMesaControlDAO.movimientoTtipflumc(
					cdtipflu
					,dstipflu
					,cdtiptra
					,"S".equals(swmultipol) ? "S" : "N"
					,"S".equals(swreqpol) ? "S" : "N"
					,cdtipsup
					,cdtipmod
					,"S".equals(swexterno) ? "S" : "N"
					,accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ cdtipflu = ", cdtipfluRes
				,"\n@@@@@@ movimientoTtipflumc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return cdtipfluRes;
	}
	
	@Override
	public String movimientoTflujomc(
			String accion
			,String cdtipflu
			,String cdflujomc
			,String dsflujomc
			,String swfinal
			,String cdtipram
			,String swgrupo
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoTflujomc @@@@@@"
				,"\n@@@@@@ accion    = " , accion
				,"\n@@@@@@ cdtipflu  = " , cdtipflu
				,"\n@@@@@@ cdflujomc = " , cdflujomc
				,"\n@@@@@@ dsflujomc = " , dsflujomc
				,"\n@@@@@@ swfinal   = " , swfinal
				,"\n@@@@@@ cdtipram  = " , cdtipram
				,"\n@@@@@@ swgrupo   = " , swgrupo
				));
		
		String paso = "Guardando proceso";
		logger.debug(paso);
		
		String cdflujomcRes = null;
		
		try
		{
			cdflujomcRes = flujoMesaControlDAO.movimientoTflujomc(
					cdtipflu
					,cdflujomc
					,dsflujomc
					,"S".equals(swfinal) ? "S" : "N"
					,cdtipram
					,swgrupo
					,accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdflujomc = ", cdflujomcRes
				,"\n@@@@@@ movimientoTflujomc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return cdflujomcRes;
	}
	
	@Override
	public void movimientoCatalogo(
			String accion
			,String tipo
			,Map<String,String> params
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoCatalogo @@@@@@"
				,"\n@@@@@@ accion=" , accion
				,"\n@@@@@@ tipo="   , tipo
				,"\n@@@@@@ params=" , params
				));
		
		String paso = "Guardando cat\u00e1logo";
		logger.debug(paso);
		
		try
		{
			if("E".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTestadomc(
						accion
						,params.get("CDESTADOMC")
						,params.get("DSESTADOMC")
						);
			}
			else if("P".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTpantmc(
						params.get("CDPANTMC")
						,params.get("DSPANTMC")
						,params.get("URLPANTMC")
						,"S".equals(params.get("SWEXTERNA")) ? "S" : "N"
						,accion
						);
			}
			else if("C".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTcompmc(
						accion
						,params.get("CDCOMPMC")
						,params.get("DSCOMPMC")
						,params.get("NOMCOMP")
						);
			}
			else if("O".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTprocmc(
						params.get("CDPROCMC")
						,params.get("DSPROCMC")
						,params.get("URLPROCMC")
						,accion);
			}
			else
			{
				throw new ApplicationException("Tipo no reconocido");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ movimientoCatalogo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public String registrarEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			,String xpos
			,String ypos
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ registrarEntidad @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ tipo="      , tipo
				,"\n@@@@@@ clave="     , clave
				,"\n@@@@@@ webid="     , webid
				,"\n@@@@@@ xpos="      , xpos
				,"\n@@@@@@ ypos="      , ypos
				));
		
		String cdentidad = null
		       ,paso     = "Registrando entidad";
		logger.debug(paso);
		
		try
		{
			if("E".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTfluest(
						cdtipflu
						,cdflujomc
						,clave //cdestadomc
						,webid
						,xpos
						,ypos
						,null  //timemax
						,null  //timewrn1
						,null  //timewrn2
						,"1"   //cdtipasig
						,"-1"  //statusout
						,"N"   //swfinnode
						,"1"   //EN REGISTRO
						,"I"
						);
			}
			else if("S".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflusuc(
						cdtipflu
						,cdflujomc
						,clave //cdestadomc
						,webid
						,xpos
						,ypos
						,"1"  // nivel: sucursal primaria
						,"100" // capacidad: 10
						,"I"
						);
			}
			else if("P".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflupant(
						cdtipflu
						,cdflujomc
						,clave //cdpantmc
						,webid
						,xpos
						,ypos
						,"I"
						);
			}
			else if("C".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflucomp(
						cdtipflu
						,cdflujomc
						,clave //cdcompmc
						,webid
						,xpos
						,ypos
						,"I"
						);
			}
			else if("O".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTfluproc(
						cdtipflu
						,cdflujomc
						,clave //cdprocmc
						,webid
						,xpos
						,ypos
						,"I"
						);
			}
			else if("V".equals(tipo))
			{
				cdentidad = flujoMesaControlDAO.movimientoTfluval(
						cdtipflu
						,cdflujomc
						,null //cdvalida
						,"" //dsvalida
						,"-1" //cdvalidafk
						,webid
						,xpos
						,ypos
						,""
						,"I"
						);
			}
			else if("R".equals(tipo))
			{
				cdentidad = flujoMesaControlDAO.movimientoTflurev(
						cdtipflu
						,cdflujomc
						,null //cdrevisi
						,""   //dsrevisi
						,webid
						,xpos
						,ypos
						,"I"
						);
			}
			else if("T".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflutit(
						cdtipflu
						,cdflujomc
						,clave
						,""   //dstitulo
						,webid
						,xpos
						,ypos
						,"I"
						);
			}
			else if("M".equals(tipo))
			{
				cdentidad = flujoMesaControlDAO.movimientoTmail(
						cdtipflu,
						cdflujomc,
						null,//cdmail, 
						null,//dsmail, 
						null,//dsdestino, 
						null,//dsasunto, 
						null,//dsmensaje, 
						null,//vardestino, 
						null,//varasunto, 
						null,//varmensaje,
						webid,
						xpos,
						ypos,
						"I"
						);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdentidad=",cdentidad
				,"\n@@@@@@ registrarEntidad @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return cdentidad;
	}
	
	@Override
	public void borrarEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ borrarEntidad @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ tipo="      , tipo
				,"\n@@@@@@ clave="     , clave
				,"\n@@@@@@ webid="     , webid
				));
		
		String paso = "Borrando entidad";
		logger.debug(paso);
		
		try
		{
			if("E".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTfluest(
						cdtipflu
						,cdflujomc
						,clave //cdestadomc
						,webid
						,null //xpos
						,null //ypos
						,null //timemax
						,null //timewrn1
						,null //timewrn2
						,null //cdtipasig
						,null //statusout
						,null //swfinnode
						,null //cdetapa
						,"D"
						);
			}
			else if("P".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflupant(
						cdtipflu
						,cdflujomc
						,clave//cdpantmc
						,webid
						,null//xpos
						,null//ypos
						,"D"
						);
			}
			else if("C".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflucomp(
						cdtipflu
						,cdflujomc
						,clave //cdcompmc
						,webid
						,null //xpos
						,null //ypos
						,"D" //accion
						);
			}
			else if("O".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTfluproc(
						cdtipflu
						,cdflujomc
						,clave //cdprocmc
						,webid
						,null//xpos
						,null//ypos
						,"D"//accion
						);
			}
			else if("V".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTfluval(
						cdtipflu
						,cdflujomc
						,clave //cdvalida
						,null //dsvalida
						,null //cdvalidafk
						,webid
						,null //xpos
						,null //ypoS
						,""//jsvalida
						,"D" //accion
						);
			}
			else if("R".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflurev(
						cdtipflu
						,cdflujomc
						,clave //cdrevisi
						,null //dsrevisi
						,webid
						,null //xpos
						,null //ypos
						,"D" //accion
						);
			}
			else if("T".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTflutit(
						cdtipflu
						,cdflujomc
						,clave //cdrevisi
						,null //dsrevisi
						,webid
						,null //xpos
						,null //ypos
						,"D" //accion
						);
			}
			else if("M".equals(tipo))
			{
				flujoMesaControlDAO.movimientoTmail(
						cdtipflu
						,cdflujomc
						,clave //cdmail
						,null//,dsmail 
						,null//,dsdestino 
						,null//,dsasunto 
						,null//,dsmensaje 
						,null//,vardestino 
						,null//,varasunto 
						,null//,varmensaje,
						,webid
						,null //xpos
						,null //ypoS
						,"D" //accion
						);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ borrarEntidad @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public List<Map<String,String>> cargarModelado(
			String cdtipflu
			,String cdflujomc
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarModelado @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				));
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String                   paso = null;
		
		try
		{
			paso = "Recuperando status";
			logger.debug(paso);
			
			List<Map<String,String>> estados = flujoMesaControlDAO.recuperaTfluest(cdtipflu, cdflujomc, null);
			
			if(estados.size()>0)
			{
				for(Map<String,String>estado:estados)
				{
					estado.put("TIPO" , "E");
					list.add(estado);
				}
				
				paso = "Recuperando pantallas";
				logger.debug(paso);
				
				List<Map<String,String>> pantallas = flujoMesaControlDAO.recuperaTflupant(
						cdtipflu
						,cdflujomc
						,null
						);
				
				for(Map<String,String>pantalla:pantallas)
				{
					pantalla.put("TIPO" , "P");
					list.add(pantalla);
				}
				
				paso = "Recuperando componentes";
				logger.debug(paso);
				
				List<Map<String,String>> componentes = flujoMesaControlDAO.recuperaTflucomp(
						cdtipflu
						,cdflujomc);
				
				for(Map<String,String>componente:componentes)
				{
					componente.put("TIPO" , "C");
					list.add(componente);
				}
				
				List<Map<String,String>> procesos = flujoMesaControlDAO.recuperaTfluproc(cdtipflu, cdflujomc);
				
				for(Map<String,String>proceso:procesos)
				{
					proceso.put("TIPO" , "O");
					list.add(proceso);
				}
				
				List<Map<String,String>> validaciones = flujoMesaControlDAO.recuperaTfluval(cdtipflu, cdflujomc, null);
				
				for(Map<String,String>validacion:validaciones)
				{
					validacion.put("TIPO" , "V");
					list.add(validacion);
				}
				
				List<Map<String,String>> revisiones = flujoMesaControlDAO.recuperaTflurev(cdtipflu, cdflujomc);
				
				for(Map<String,String>revision:revisiones)
				{
					revision.put("TIPO" , "R");
					list.add(revision);
				}
				
				List<Map<String,String>> titulos = flujoMesaControlDAO.recuperaTflutit(cdtipflu, cdflujomc, null);
				
				for(Map<String,String>titulo:titulos)
				{
					titulo.put("TIPO" , "T");
					list.add(titulo);
				}
				
				List<Map<String,String>> correos = flujoMesaControlDAO.recuperaTflumail(cdtipflu, cdflujomc, null);
				
				for(Map<String,String>correo:correos)
				{
					correo.put("TIPO" , "M");
					list.add(correo);
				}
				
				List<Map<String,String>> acciones = flujoMesaControlDAO.recuperaTfluacc(cdtipflu,cdflujomc);
				
				for(Map<String,String>accion:acciones)
				{
					accion.put("TIPO" , "A");
					list.add(accion);
				}
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ list=" , list
				,"\n@@@@@@ cargarModelado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return list;
	}
	
	@Override
	public Map<String,Object> cargarDatosEstado(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosEstado @@@@@@"
				,"\n@@@@@@ cdtipflu="   , cdtipflu
				,"\n@@@@@@ cdflujomc="  , cdflujomc
				,"\n@@@@@@ cdestadomc=" , cdestadomc
				));
		
		Map<String,Object> result = new HashMap<String,Object>();
		String             paso   = null;
		
		try
		{
			paso = "Recuperando status";
			logger.debug(paso);
			
			List<Map<String,String>> lista  = flujoMesaControlDAO.recuperaTfluest(cdtipflu, cdflujomc, cdestadomc);
			Map<String,String>       estado = null;
			
			for(Map<String,String>listaItem:lista)
			{
				if(listaItem.get("CDESTADOMC").equals(cdestadomc))
				{
					estado = listaItem;
					break;
				}
			}
			
			if(estado==null)
			{
				throw new ApplicationException("No se encuentra el status");
			}
			
			if(StringUtils.isBlank(estado.get("TIMEMAX")))
			{
				estado.put("TIMEMAX","0");
			}
			if(StringUtils.isBlank(estado.get("TIMEWRN1")))
			{
				estado.put("TIMEWRN1","0");
			}
			if(StringUtils.isBlank(estado.get("TIMEWRN2")))
			{
				estado.put("TIMEWRN2","0");
			}
			
			Double tmax = Double.parseDouble(estado.get("TIMEMAX"));
			Double wrn1 = Double.parseDouble(estado.get("TIMEWRN1"));
			Double wrn2 = Double.parseDouble(estado.get("TIMEWRN2"));
			
			estado.put("TIMEMAXH" , String.format("%.0f",Math.floor(tmax/60d)));
			estado.put("TIMEMAXM" , String.format("%.0f",Math.floor(tmax%60d)));
			
			estado.put("TIMEWRN1H" , String.format("%.0f",Math.floor(wrn1/60d)));
			estado.put("TIMEWRN1M" , String.format("%.0f",Math.floor(wrn1%60d)));
			
			estado.put("TIMEWRN2H" , String.format("%.0f",Math.floor(wrn2/60d)));
			estado.put("TIMEWRN2M" , String.format("%.0f",Math.floor(wrn2%60d)));
			
			paso = "Recuperando permisos";
			logger.debug(paso);
			
			List<Map<String,String>> permisos = flujoMesaControlDAO.recuperaTfluestrol(cdtipflu, cdflujomc, estado.get("CDESTADOMC"));
			
			paso = "Recuperando avisos";
			logger.debug(paso);
			
			List<Map<String,String>> avisos = flujoMesaControlDAO.recuperaTfluestavi(cdtipflu, cdflujomc, estado.get("CDESTADOMC"));

			List<Map<String,String>> listaProps = new ArrayList<Map<String,String>>();
			
			for(Map<String,String>permiso:permisos)
			{
				permiso.put("TIPO" , "P");
				listaProps.add(permiso);
			}
			
			for(Map<String,String>aviso:avisos)
			{
				aviso.put("TIPO" , "A");
				listaProps.add(aviso);
			}
			
			logger.debug(Utils.log(
					 "\nmapa="  , estado
					,"\nlista=" , listaProps
					));
			
			result.put("mapa"  , estado);
			result.put("lista" , listaProps);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosEstado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public void guardarDatosEstado(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			,String accion
			,String webid
			,String xpos
			,String ypos
			,String timemaxh
			,String timemaxm
			,String timewrn1h
			,String timewrn1m
			,String timewrn2h
			,String timewrn2m
			,String cdtipasig
			,String swescala
			,List<Map<String,String>>list
			,String statusout
			,boolean swfinnode
			,String cdetapa
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarDatosEstado @@@@@@"
				,"\n@@@@@@ cdtipflu="   , cdtipflu
				,"\n@@@@@@ cdflujomc="  , cdflujomc
				,"\n@@@@@@ cdestadomc=" , cdestadomc
				,"\n@@@@@@ accion="     , accion
				,"\n@@@@@@ webid="      , webid
				,"\n@@@@@@ xpos="       , xpos
				,"\n@@@@@@ ypos="       , ypos
				,"\n@@@@@@ timemaxh="   , timemaxh
				,"\n@@@@@@ timemaxm="   , timemaxm
				,"\n@@@@@@ timewrn1h="  , timewrn1h
				,"\n@@@@@@ timewrn1m="  , timewrn1m
				,"\n@@@@@@ timewrn2h="  , timewrn2h
				,"\n@@@@@@ timewrn2m="  , timewrn2m
				,"\n@@@@@@ cdtipasig="  , cdtipasig
				,"\n@@@@@@ swescala="   , swescala
				,"\n@@@@@@ statusout="  , statusout
				,"\n@@@@@@ swfinnode="  , swfinnode
				,"\n@@@@@@ cdetapa="    , cdetapa
				,"\n@@@@@@ list="       , list
				));
		
		String paso = null;
		try
		{
			paso = "Guardando datos de status";
			logger.debug(paso);
			
			flujoMesaControlDAO.movimientoTfluest(
					cdtipflu
					,cdflujomc
					,cdestadomc
					,webid
					,xpos
					,ypos
					,new Long((Long.valueOf(timemaxm))+(Long.valueOf(timemaxh)*60)).toString()   //timemax
					,new Long((Long.valueOf(timewrn1m))+(Long.valueOf(timewrn1h)*60)).toString() //timewrn1
					,new Long((Long.valueOf(timewrn2m))+(Long.valueOf(timewrn2h)*60)).toString() //timewrn2
					,cdtipasig
					,statusout
					,swfinnode ? "S" : "N"
					,cdetapa
					,accion
					);
			
			paso = "Guardando permisos y avisos";
			logger.debug(paso);
			
			for(Map<String,String>ite : list)
			{
				String tipo = ite.get("TIPO");
				if(tipo.equals("P"))
				{
					flujoMesaControlDAO.movimientoTfluestrol(
							cdtipflu
							,cdflujomc
							,cdestadomc
							,ite.get("CDSISROL")
							,"S".equals(ite.get("SWVER")) ? "S" : "N"
							,"S".equals(ite.get("SWTRABAJO")) ? "S" : "N"
							,"S".equals(ite.get("SWCOMPRA")) ? "S" : "N"
							,"S".equals(ite.get("SWREASIG")) ? "S" : "N"
							,"" //cdrolasig
							,"S".equals(ite.get("SWVERDEF")) ? "S" : "N"
							,"I"
							);
				}
				else if(tipo.equals("A"))
				{
					flujoMesaControlDAO.movimientoTfluestavi(
							cdtipflu
							,cdflujomc
							,cdestadomc
							,ite.get("CDAVISO")
							,"1" //cdtipavi
							,""  //dscoment
							,"N" //swautoavi
							,ite.get("DSMAILAVI")
							,null //cdusuariavi
							,null //cdsisrolavi
							,"U"
							);
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarDatosEstado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public String registrarConnection(
			String cdtipflu
			,String cdflujomc
			,String idorigen
			,String iddestin
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ registrarConnection @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ idorigen="  , idorigen
				,"\n@@@@@@ iddestin="  , iddestin
				));
		
		String cdaccion = null
		       ,paso    = null;
		
		try
		{
			paso = "Registrando conexi\u00f3n";
			logger.debug(paso);
			
			cdaccion = flujoMesaControlDAO.movimientoTfluacc(
					cdtipflu
					,cdflujomc
					,null //cdaccion
					,""   //dsaccion
					,""   //cdicono
					,""   //cdvalor
					,idorigen
					,iddestin
					,null
					,null
					,"I"
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdaccion=",cdaccion
				,"\n@@@@@@ registrarConnection @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return cdaccion;
	}
	
	@Override
	public Map<String,String> cargarDatosValidacion(
			String cdtipflu
			,String cdflujomc
			,String cdvalida
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosValidacion @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ cdvalida="  , cdvalida
				));
		
		Map<String,String> validacion = null;
		String             paso       = null;
		
		try
		{
			paso = "Recuperando validaciones";
			logger.debug(paso);
			
			List<Map<String,String>> validaciones = flujoMesaControlDAO.recuperaTfluval(cdtipflu, cdflujomc, cdvalida);
			
			for(Map<String,String>validaIte : validaciones)
			{
				if(validaIte.get("CDVALIDA").equals(cdvalida))
				{
					validacion = validaIte;
					break;
				}
			}
			
			if(validacion==null)
			{
				throw new ApplicationException("No se encuentra la validaci\u00f3n");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosValidacion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return validacion;
	}
	
	@Override
	public void guardarDatosValidacion(
			String cdtipflu
			,String cdflujomc
			,String cdvalida
			,String webid
			,String xpos
			,String ypos
			,String dsvalida
			,String cdvalidafk
			,String jsvalida
			,String accion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarDatosValidacion @@@@@@"
				,"\n@@@@@@ cdtipflu="   , cdtipflu
				,"\n@@@@@@ cdflujomc="  , cdflujomc
				,"\n@@@@@@ cdvalida="   , cdvalida
				,"\n@@@@@@ webid="      , webid
				,"\n@@@@@@ xpos="       , xpos
				,"\n@@@@@@ ypos="       , ypos
				,"\n@@@@@@ dsvalida="   , dsvalida
				,"\n@@@@@@ cdvalidafk=" , cdvalidafk
				,"\n@@@@@@ jsvalida="   , jsvalida
				,"\n@@@@@@ accion="     , accion
				));
		
		String paso = "Guardando datos de validaci\u00f3n";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTfluval(
					cdtipflu
					,cdflujomc
					,cdvalida
					,dsvalida
					,cdvalidafk
					,webid
					,xpos
					,ypos
					,jsvalida
					,accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarDatosValidacion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void guardarCoordenadas (String cdtipflu, String cdflujomc, List<Map<String,String>>list) throws Exception {
		logger.debug(Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				               "\n@@@@@@ guardarCoordenadas @@@@@@",
				               "\n@@@@@@ cdtipflu  = " , cdtipflu,
				               "\n@@@@@@ cdflujomc = " , cdflujomc,
				               "\n@@@@@@ list      = " , list));
		String paso = "Guardando coordenadas";
		try {
		    List<Map<String, String>> lista = new ArrayList<Map<String, String>>();
			for (Map<String, String> entidad : list) {
			    Map<String, String> coor = new HashMap<String, String>();
			    coor.put("cdtipflu"  , cdtipflu);
			    coor.put("cdflujomc" , cdflujomc);
			    coor.put("tipo"      , entidad.get("tipo"));
			    coor.put("clave"     , entidad.get("clave"));
			    coor.put("webid"     , entidad.get("webid"));
			    coor.put("xpos"      , entidad.get("xpos"));
			    coor.put("ypos"      , entidad.get("ypos"));
			    lista.add(coor);
			}
			if (lista.size() > 0) {
			    flujoMesaControlDAO.actualizaCoordenadasLote(lista);
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log("\n@@@@@@ guardarCoordenadas @@@@@@",
		                       "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public String ejecutaValidacion(FlujoVO flujo, String cdvalidafk, String cdusuari, String cdsisrol) throws Exception {
		logger.debug("{}", Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				                     "\n@@@@@@ ejecutaValidacion @@@@@@",
				                     "\n@@@@@@ flujo = "      , flujo,
				                     "\n@@@@@@ cdvalidafk = " , cdvalidafk,
                                     "\n@@@@@@ cdusuari = "   , cdusuari,
                                     "\n@@@@@@ cdsisrol = "   , cdsisrol));
		String salida = null,
		       paso   = null;
		try {
		    paso = "Ejecutando validaci\u00f3n";
		    logger.debug("{}", paso);
		    if ("_LOADAUX".equals(cdvalidafk)) { // PARA RECUPERAR OTVALOR DE TMESACONTROL CON DSATRIBU LIKE '%AUXILIAR%FLUJO%'
		        salida = mesaControlDAO.recuperarOtvalorTramitePorDsatribu(flujo.getNtramite(), "AUXILIAR%FLUJO");
		    } else {
    			logger.debug(paso);
    			salida = flujoMesaControlDAO.ejecutaValidacion(
    					flujo.getNtramite(),
    					"", // flujo.getCdunieco()
    					Ramo.MESA_CONTROL.getCdramo(),
    					"", // flujo.getEstado(),
    					"", // flujo.getNmpoliza(),
    					"", // flujo.getNmsituac(),
    					"", // flujo.getNmsuplem(),
    					cdvalidafk,
    					cdusuari,
    					cdsisrol,
    					flujo.getClaveent());
		    }
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug("{}", Utils.log("\n@@@@@@ salida = ", salida,
				                     "\n@@@@@@ ejecutaValidacion @@@@@@",
				                     "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return salida;
	}
	
	@Override
	public Map<String,Object> cargarDatosRevision(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosRevision @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ cdrevisi="  , cdrevisi
				));
		
		Map<String,Object> result = new HashMap<String,Object>();
		String             paso   = null;
		
		try
		{
			paso = "Recuperando revisi\u00f3n";
			logger.debug(paso);
			
			List<Map<String,String>> lista    = flujoMesaControlDAO.recuperaTflurev(cdtipflu, cdflujomc);
			Map<String,String>       revision = null;
			
			for(Map<String,String>listaItem:lista)
			{
				if(listaItem.get("CDREVISI").equals(cdrevisi))
				{
					revision = listaItem;
					break;
				}
			}
			
			if(revision==null)
			{
				throw new ApplicationException("No se encuentra la revisi\u00f3n");
			}
			
			paso = "Recuperando documentos";
			logger.debug(paso);
			
			List<Map<String,String>> documentos = flujoMesaControlDAO.recuperaTflurevdoc(cdtipflu, cdflujomc, cdrevisi);
			List<Map<String,String>> requisitos = flujoMesaControlDAO.recuperaTflurevreq(cdtipflu, cdflujomc, cdrevisi);
			
			List<Map<String, String>> permisos = new ArrayList<Map<String, String>>();
			
			for (Map<String, String> documento : documentos ) {
				documento.put("TIPO", "DOC");
				permisos.add(documento);
			}
			
			for (Map<String, String> requisito : requisitos) {
				requisito.put("TIPO", "REQ");
				permisos.add(requisito);
			}
			
			logger.debug(Utils.log(
					 "\nmapa="  , revision
					,"\nlista=" , permisos
					));
			
			result.put("mapa"  , revision);
			result.put("lista" , permisos);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosRevision @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public void guardarDatosRevision (String cdtipflu, String cdflujomc, String cdrevisi, String dsrevisi, String accion, String webid,
	        String xpos, String ypos, List<Map<String,String>>list) throws Exception {
		logger.debug(Utils.log(
		        "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ guardarDatosRevision @@@@@@",
				"\n@@@@@@ cdtipflu="  , cdtipflu,
				"\n@@@@@@ cdflujomc=" , cdflujomc,
				"\n@@@@@@ cdrevisi="  , cdrevisi,
				"\n@@@@@@ dsrevisi="  , dsrevisi,
				"\n@@@@@@ accion="    , accion,
				"\n@@@@@@ webid="     , webid,
				"\n@@@@@@ xpos="      , xpos,
				"\n@@@@@@ ypos="      , ypos,
				"\n@@@@@@ list="      , list));
		String paso = null;
		try {
			paso = "Guardando datos de revisi\u00f3n";
			logger.debug(paso);
			flujoMesaControlDAO.movimientoTflurev(
					cdtipflu
					,cdflujomc
					,cdrevisi
					,dsrevisi
					,webid
					,xpos
					,ypos
					,accion
					);
			paso = "Guardando documentos";
			logger.debug(paso);
			List<Map<String, String>> listaReq = new ArrayList<Map<String, String>>(),
			                          listaDoc = new ArrayList<Map<String, String>>();
			for (Map<String, String> ite : list) {
				String tipo = ite.get("TIPO");
				if ("DOC".equals(tipo)) {
				    Map<String, String> doc = new HashMap<String, String>();
				    doc.put("cdtipflu"  , cdtipflu);
				    doc.put("cdflujomc" , cdflujomc);
				    doc.put("cdrevisi"  , cdrevisi);
				    doc.put("cddocume"  , ite.get("CDDOCUME"));
				    doc.put("swobliga"  , "S".equals(ite.get("SWOBLIGA")) ? "S" : "N");
				    doc.put("swlista"   , "S".equals(ite.get("SWLISTA"))  ? "S" : "N");
				    doc.put("accion"    , "I");
					listaDoc.add(doc);
				} else if ("REQ".equals(tipo)) {
				    Map<String, String> req = new HashMap<String, String>();
                    req.put("cdtipflu"  , cdtipflu);
                    req.put("cdflujomc" , cdflujomc);
                    req.put("cdrevisi"  , cdrevisi);
                    req.put("cdrequisi" , ite.get("CDREQUISI"));
                    req.put("swobliga"  , "S".equals(ite.get("SWOBLIGA")) ? "S" : "N");
                    req.put("swlista"   , "S".equals(ite.get("SWLISTA"))  ? "S" : "N");
                    req.put("accion"    , "I");
                    listaReq.add(req);
				} else {
					throw new ApplicationException("El dato en la lista de datos de revisi\u00f3n no tiene un tipo v\u00e1lido");
				}
			}
			if (listaDoc.size() > 0) {
			    flujoMesaControlDAO.movimientoTflurevdocLote(listaDoc);
			}
			if (listaReq.size() > 0) {
                flujoMesaControlDAO.movimientoTflurevreqLote(listaReq);
            }
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
		        "\n@@@@@@ guardarDatosRevision @@@@@@",
		        "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public void movimientoTdocume(
			String accion
			,String cddocume
			,String dsdocume
			,String cdtiptra
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoTdocume @@@@@@"
				,"\n@@@@@@ accion="   , accion
				,"\n@@@@@@ cddocume=" , cddocume
				,"\n@@@@@@ dsdocume=" , dsdocume
				,"\n@@@@@@ cdtiptra=" , cdtiptra
				));
		
		String paso = "Guardando documento";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTdocume(cddocume, dsdocume, cdtiptra, accion);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ movimientoTdocume @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void borrarConnection(
			String cdtipflu
			,String cdflujomc
			,String cdaccion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ borrarConnection @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ cdaccion="  , cdaccion
				));
		
		String paso = "Borrando acci\u00f3n";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTfluacc(
					cdtipflu
					,cdflujomc
					,cdaccion
					,null //dsaccion
					,null //cdicono
					,null //cdvalor
					,null //idorigen
					,null //iddestin
					,null
					,null
					,"D" //accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ borrarConnection @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String,Object> cargarDatosAccion(
			String cdtipflu
			,String cdflujomc
			,String cdaccion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosAccion @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ cdaccion="  , cdaccion
				));
		
		Map<String,Object> result = new HashMap<String,Object>();
		String             paso   = null;
		
		try
		{
			paso = "Recuperando acci\u00f3n";
			logger.debug(paso);
			
			List<Map<String,String>> lista    = flujoMesaControlDAO.recuperaTfluacc(cdtipflu, cdflujomc);
			Map<String,String>       accion = null;
			
			for(Map<String,String>listaItem:lista)
			{
				if(listaItem.get("CDACCION").equals(cdaccion))
				{
					accion = listaItem;
					break;
				}
			}
			
			if(accion==null)
			{
				throw new ApplicationException("No se encuentra la acci\u00f3n");
			}
			
			paso = "Recuperando permisos";
			logger.debug(paso);
			
			List<Map<String,String>> permisos = flujoMesaControlDAO.recuperaTfluaccrol(cdtipflu, cdflujomc, cdaccion);
			
			logger.debug(Utils.log(
					 "\nmapa="  , accion
					,"\nlista=" , permisos
					));
			
			result.put("mapa"  , accion);
			result.put("lista" , permisos);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosAccion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public void guardarDatosAccion (String cdtipflu, String cdflujomc, String cdaccion, String dsaccion,
			String accion, String idorigen, String iddestin, String cdvalor, String cdicono, String swescala,
			String aux, List<Map<String,String>>list) throws Exception {
		logger.debug(Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				               "\n@@@@@@ guardarDatosAccion @@@@@@",
				               "\n@@@@@@ cdtipflu  = " , cdtipflu,
				               "\n@@@@@@ cdflujomc = " , cdflujomc,
				               "\n@@@@@@ cdaccion  = " , cdaccion,
				               "\n@@@@@@ dsaccion  = " , dsaccion,
				               "\n@@@@@@ accion    = " , accion,
				               "\n@@@@@@ idorigen  = " , idorigen,
				               "\n@@@@@@ iddestin  = " , iddestin,
				               "\n@@@@@@ cdvalor   = " , cdvalor,
				               "\n@@@@@@ cdicono   = " , cdicono,
				               "\n@@@@@@ swescala  = " , swescala,
				               "\n@@@@@@ aux       = " , aux,
				               "\n@@@@@@ list      = " , list));
		String paso = null;
		try {
			paso = "Guardando datos de acci\u00f3n";
			logger.debug(paso);
			flujoMesaControlDAO.movimientoTfluacc(
					cdtipflu
					,cdflujomc
					,cdaccion
					,dsaccion
					,cdicono
					,cdvalor
					,idorigen
					,iddestin
					,"S".equals(swescala) ? "S" : "N"
					,aux
					,accion);
			paso = "Guardando permisos";
			logger.debug(paso);
			List<Map<String, String>> lista = new ArrayList<Map<String, String>>();
			for (Map<String,String>ite : list) {
			    Map<String, String> obj = new HashMap<String, String>();
			    obj.put("cdtipflu"  , cdtipflu);
			    obj.put("cdflujomc" , cdflujomc);
			    obj.put("cdaccion"  , cdaccion);
			    obj.put("cdsisrol"  , ite.get("CDSISROL"));
			    obj.put("swpermiso" , "S".equals(ite.get("SWPERMISO")) ? "S" : "N");
			    obj.put("accion"    , "I");
			    lista.add(obj);
			}
			if (lista.size() > 0)
			{
			    flujoMesaControlDAO.movimientoTfluaccrolLote(lista);
			}
		} catch( Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log("\n@@@@@@ guardarDatosAccion @@@@@@",
				               "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public Map<String,Item> debugScreen() throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ debugScreen @@@@@@"
				));
		String           paso  = null;
		Map<String,Item> items = new HashMap<String,Item>();
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			List<ComponenteVO> comps = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"DEBUG_SCREEN"
					,"ITEMS"
					,null //orden
					);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(comps, true, false, true, false, false, false);
			
			items.put("items" , gc.getItems());
			
			logger.debug(Utils.log(
					 "\n@@@@@@ items=",items
					,"\n@@@@@@ debugScreen @@@@@@"
					,"\n@@@@@@@@@@@@@@@@@@@@@@@@@"
					));
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		return items;
	}
	
	@Override
	public Map<String,Object> mesaControl(
			String cdsisrol
			,String agrupamc
			,String cdusuari
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ mesaControl @@@@@@"
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ agrupamc=" , agrupamc
				,"\n@@@@@@ cdusuari=" , cdusuari
				));
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,String> mapa = new HashMap<String,String>();
		
		String             paso   = null;
		try
		{
			paso = "Recuperando componentes de filtro";
			logger.debug(paso);
			
			List<ComponenteVO> filtro = pantallasDAO.obtenerComponentes(
					agrupamc //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"MESA_CONTROL"//pantalla
					,"FILTRO"//seccion
					,null //orden
					);
			
			paso = "Recuperando componentes de grid";
			logger.debug(paso);
			
			List<ComponenteVO> grid = pantallasDAO.obtenerComponentes(
					agrupamc //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"MESA_CONTROL"//pantalla
					,"GRID"//seccion
					,null //orden
					);
			
			List<ComponenteVO> form = pantallasDAO.obtenerComponentes(
					agrupamc //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"MESA_CONTROL"//pantalla
					,"FORMULARIO"//seccion
					,null //orden
					);
            
            List<ComponenteVO> form_base = pantallasDAO.obtenerComponentes(
                    agrupamc //cdtiptra
                    ,null //cdunieco
                    ,null //cdramo
                    ,null //cdtipsit
                    ,null //estado
                    ,cdsisrol
                    ,"MESA_CONTROL"//pantalla
                    ,"FORM_BASE"//seccion
                    ,null //orden
                    );
            
            List<ComponenteVO> form_flujo = pantallasDAO.obtenerComponentes(
                    agrupamc //cdtiptra
                    ,null //cdunieco
                    ,null //cdramo
                    ,null //cdtipsit
                    ,null //estado
                    ,cdsisrol
                    ,"MESA_CONTROL"//pantalla
                    ,"FORM_FLUJO"//seccion
                    ,null //orden
                    );
			
			List<ComponenteVO> botones = pantallasDAO.obtenerComponentes(
					agrupamc //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"MESA_CONTROL"//pantalla
					,"BOTONES"//seccion
					,null //orden
					);
			
			boolean btnReasigna = false;
			if(botones!=null && !botones.isEmpty()){
			   for(ComponenteVO btn : botones){
			       if("REASIGNA".equalsIgnoreCase(btn.getNameCdatribu())){
			           btnReasigna =  true;
			       }
			   }
			}
			mapa.put("BTN_REASIGNAR", btnReasigna?"S":"N");
			
			paso = "Generando componentes";
			logger.debug(paso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(filtro, true, false, true, false, false, false);
			
			Map<String,Item> items = new HashMap<String,Item>();
			items.put("filtroItems" , gc.getItems());
			
			gc.generaComponentes(grid, true, true, false, true, false, false);
			
			items.put("gridFields"  , gc.getFields());
			items.put("gridColumns" , gc.getColumns());
			
			gc.generaComponentes(form, true, false, true, false, false, false);
			
			items.put("formItems" , gc.getItems());
            
            gc.generaComponentes(form_base, true, false, true, false, false, false);
            
            items.put("formBaseItems" , gc.getItems());
            
            gc.generaComponentes(form_flujo, true, false, true, false, false, false);
            
            items.put("formFlujoItems" , gc.getItems());
			
			gc.generaComponentes(botones, true, false, false, false, false, true);
			
			items.put("botonesGrid" , gc.getButtons());
			
			
			if(cdsisrol.equals(RolSistema.AGENTE.getCdsisrol()))
			{
				mapa.put("CDAGENTE" , mesaControlDAO.cargarCdagentePorCdusuari(cdusuari));
			}
			
			result.put("items" , items);
			result.put("mapa"  , mapa);
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ result=",result
				,"\n@@@@@@ mesaControl @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public Map<String,Object> recuperarTramites(
			String agrupamc
			,String status
			,String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String cdagente
			,String ntramite
			,String fedesde
			,String fehasta
			,String cdpersonCliente
			,String filtro
			,String dscontra
			,int start
			,int limit
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTramites @@@@@@"
				,"\n@@@@@@ agrupamc="         , agrupamc
				,"\n@@@@@@ status="           , status
				,"\n@@@@@@ cdusuari="         , cdusuari
				,"\n@@@@@@ cdsisrol="         , cdsisrol
				,"\n@@@@@@ cdunieco="         , cdunieco
				,"\n@@@@@@ cdramo="           , cdramo
				,"\n@@@@@@ cdtipsit="         , cdtipsit
				,"\n@@@@@@ estado="           , estado
				,"\n@@@@@@ nmpoliza="         , nmpoliza
				,"\n@@@@@@ cdagente="         , cdagente
				,"\n@@@@@@ ntramite="         , ntramite
				,"\n@@@@@@ fedesde="          , fedesde
				,"\n@@@@@@ fehasta="          , fehasta
				,"\n@@@@@@ cdpersonCliente="  , cdpersonCliente
				,"\n@@@@@@ filtro="           , filtro
				,"\n@@@@@@ dscontra="         , dscontra
				,"\n@@@@@@ start="            , start
				,"\n@@@@@@ limit="            , limit
				));
		String             paso   = null;
		Map<String,Object> result = null;
		try
		{
			paso = "Recuperando tr\u00e1mites";
			logger.debug(paso);
			
			result = flujoMesaControlDAO.recuperarTramites(
					agrupamc
					,status
					,cdusuari
					,cdsisrol
					,cdunieco
					,cdramo
					,cdtipsit
					,estado
					,nmpoliza
					,cdagente
					,ntramite
					,fedesde
					,fehasta
					,cdpersonCliente
					,dscontra
					,start
					,limit
					);
			
			if(StringUtils.isNotBlank(filtro))
			{
				
				List<Map<String,String>> slist1 = (List<Map<String,String>>)result.get("lista");
				
				List<Map<String,String>> aux = new ArrayList<Map<String,String>>();
				
				for(Map<String,String>rec:slist1)
				{
					for(Entry<String,String>en:rec.entrySet())
					{
						String value = en.getValue();
						if(value==null)
						{
							value = "";
						}
						if(value.toUpperCase().indexOf(filtro.toUpperCase())!=-1)
						{
							aux.add(rec);
							break;
						}
					}
				}
				
				result.put("lista", aux);
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ result=" , result
				,"\n@@@@@@ recuperarTramites @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public Map<String,String> recuperarPolizaUnica(
			String cdunieco
			,String ramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarPolizaUnica @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ ramo="     , ramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		String             paso   = "Recuperando p\u00f3liza";
		Map<String,String> poliza = null;
		try
		{
			poliza = flujoMesaControlDAO.recuperarPolizaUnica(
					cdunieco
					,ramo
					,estado
					,nmpoliza
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarPolizaUnica @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return poliza;
	}
	
	@Override
	public String registrarTramite (
			String cdunieco , String cdramo    , String estado     , String nmpoliza,
			String nmsuplem , String cdsucadm  , String cdsucdoc   , String cdtiptra,
			Date ferecepc   , String cdagente  , String referencia , String nombre,
			Date festatus   , String status    , String comments   , String nmsolici,
			String cdtipsit , String cdusuari  , String cdsisrol   , String swimpres,
			String cdtipflu , String cdflujomc ,
			Map<String, String> valores,
			String cdtipsup , String cduniext  , String ramo       , String nmpoliex,
			boolean origenMesa, boolean inyectadoDesdeSigs) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ registrarTramite @@@@@@",
				"\n@@@@@@ cdunieco           = " , cdunieco,
				"\n@@@@@@ cdramo             = " , cdramo,
				"\n@@@@@@ estado             = " , estado,
				"\n@@@@@@ nmpoliza           = " , nmpoliza,
				"\n@@@@@@ nmsuplem           = " , nmsuplem,
				"\n@@@@@@ cdsucadm           = " , cdsucadm,
				"\n@@@@@@ cdsucdoc           = " , cdsucdoc,
				"\n@@@@@@ cdtiptra           = " , cdtiptra,
				"\n@@@@@@ ferecepc           = " , ferecepc,
				"\n@@@@@@ cdagente           = " , cdagente,
				"\n@@@@@@ referencia         = " , referencia,
				"\n@@@@@@ nombre             = " , nombre,
				"\n@@@@@@ festatus           = " , festatus,
				"\n@@@@@@ status             = " , status,
				"\n@@@@@@ comments           = " , comments,
				"\n@@@@@@ nmsolici           = " , nmsolici,
				"\n@@@@@@ cdtipsit           = " , cdtipsit,
				"\n@@@@@@ cdusuari           = " , cdusuari,
				"\n@@@@@@ cdsisrol           = " , cdsisrol,
				"\n@@@@@@ swimpres           = " , swimpres,
				"\n@@@@@@ cdtipflu           = " , cdtipflu,
				"\n@@@@@@ cdflujomc          = " , cdflujomc,
				"\n@@@@@@ valores            = " , valores,
				"\n@@@@@@ cdtipsup           = " , cdtipsup,
				"\n@@@@@@ cduniext           = " , cduniext,
				"\n@@@@@@ ramo               = " , ramo,
				"\n@@@@@@ nmpoliex           = " , nmpoliex,
				"\n@@@@@@ origenMesa         = " , origenMesa,
                "\n@@@@@@ inyectadoDesdeSigs = " , inyectadoDesdeSigs
		));
		
		String paso = null, ntramite = null;
		try {
			String renuniext  = null,
					renramo   = null,
					renpoliex = null;
			
			if ("21".equals(cdtiptra)) { // Si es una renovacion
				logger.debug(Utils.log("Es una renovacion: ", renuniext, ", ", renramo, ", ", renpoliex));
				renuniext = cduniext;
				renramo   = ramo;
				renpoliex = nmpoliex;
			}
			
			paso = "Verificando si requiere validaci\u00f3n sigs";
			logger.debug(paso);
			
			// Recuperamos de tparagen algo como: |EMISION|ENDOSO|RENOVACION|
			String tramitesValidadosPorSigs = consultasDAO.recuperarTparagen(ParametroGeneral.VALIDACION_SIGS_TRAMITE);
			
			logger.debug("tramitesValidadosPorSigs = {}", tramitesValidadosPorSigs);
			
			// Recuperamos la lista de TTIPTRAMC y buscamos nuestro cdtiptra para recuperar el DSTIPTRA
			List<Map<String,String>> tiposTramites = flujoMesaControlDAO.recuperaTtiptramc();
			String dstiptra = null;
			for (Map<String,String> tramite : tiposTramites) {
				if (tramite.get("CDTIPTRA").equals(cdtiptra)) {
					dstiptra = tramite.get("DSTIPTRA");
					break;
				}
			}
			Utils.validate(dstiptra, "No se encontr\u00f3 el tr\u00e1mite para revisar si requiere validaci\u00f3n");
			
			// Si hay coincidencia de |EMISION|ENDOSO|RENOVACION| que contenga |+EMISION+|
			boolean requiereValidacion = tramitesValidadosPorSigs.indexOf(Utils.join("|", dstiptra, "|")) != -1;
			logger.debug("Coincidencia de {} que contiene {} = {}", tramitesValidadosPorSigs, dstiptra, requiereValidacion);
			
			if (requiereValidacion) {
				paso = "Ejecutando validaci\u00f3n externa";
				logger.debug(paso);
				
				String cdtipend = "P"; // Poliza nueva
				
				if (!TipoTramite.POLIZA_NUEVA.getCdtiptra().equals(cdtiptra)
						&& !TipoTramite.RENOVACION.getCdtiptra().equals(cdtiptra)
				) { // Si no es emision ni renovacion buscamos por cdtipsup
					cdtipend = consultasDAO.recuperarTtipsupl(cdtipsup).get("CDTIPEND");
				}
				
				String ramoGS = null;
				
				// RECUPERAMOS EL RAMO DESDE UN OTVALOR SI EL FLUJO ES EXTERNO
			    paso = "Recuperando tipos de flujo";
			    logger.debug(paso);
			    List<Map<String, String>> tiposFlujo = flujoMesaControlDAO.recuperaTtipflumc("PRINCIPAL", "1");
			    Map<String, String> tipoFlujo = null;
			    for (Map<String, String> ite : tiposFlujo) {
			        if (cdtipflu.equals(ite.get("CDTIPFLU"))) {
			            tipoFlujo = ite;
			            break;
			        }
			    }
			    if (tipoFlujo == null) {
			        throw new ApplicationException("No se encuentra el tipo de flujo");
			    }
			    boolean externo = "S".equals(tipoFlujo.get("SWEXTERNO"));
			    if (externo) {
			        String cdatribu = flujoMesaControlDAO.recuperarCdatribuPorDsatribuTatriflumc(cdtipflu, cdflujomc, "RAMO");
			        if (StringUtils.isNotBlank(cdatribu)) {
			            String key = Utils.join("otvalor", StringUtils.leftPad(cdatribu, 2, "0"));
			            String valor = valores.get(key);
			            if (StringUtils.isNotBlank(valor)) {
			                ramoGS = valor;
			            }
			        }
			    }
				
				if (StringUtils.isBlank(ramoGS)) {
				    ramoGS = consultasDAO.obtieneSubramoGS(cdramo, cdtipsit);
				}
				
				if (!"B".equals(cdtipend)) {
					autosSIGSDAO.validarAgenteParaNuevoTramite(
						cdagente,
						ramoGS,
						cdtipend
					);
				} else {
					logger.debug("Para tipo B no se valida agente");
				}
			}
			
			paso = "Recuperando sucursal del usuario";
            logger.debug(paso);
            cdsucadm = despachadorDAO.recuperarSucursalUsuarioPorTipoTramite(cdusuari, cdflujomc);
			
            // Para flujos de renovacion de autos se recupera la sucursal del agente porque
            // la que se captura (ejemplo 120) puede no existir en sicaps
            if((
                    FlujoMC.AUTOS_RENOVACION_INDIVIDUAL.getCdflujomc().equals(cdflujomc)
                    || FlujoMC.AUTOS_RENOVACION_PYME.getCdflujomc().equals(cdflujomc)
                    || FlujoMC.AUTOS_RENOVACION_FLOTILLA.getCdflujomc().equals(cdflujomc)
                )
                && StringUtils.isNotBlank(cdramo)
                && StringUtils.isNotBlank(cdagente)
                ) {
                try{
                    paso = "Recuperando cdunieco del agente";
                    logger.debug(paso);
                    //Se toma el cdunieco del agente en vez del que esta en la pantalla
                    String cdtipram=consultasDAO.recuperarTipoRamoPorCdramo(cdramo);
                    cdunieco = cotizacionDAO.cargarCduniecoAgenteAuto(cdagente, cdtipram);
                    cdsucdoc = cdunieco; 
                }catch(Exception e){
                    logger.error("Error recuperando cdunieco del agente {}",e);
                }
            }
            
            
			paso = "Registrando tr\u00e1mite";
			logger.debug(paso);
			
			ntramite = mesaControlDAO.movimientoMesaControl(
					cdunieco,
					cdramo,
					estado,
					nmpoliza,
					nmsuplem,
					cdsucadm,
					cdsucdoc,
					cdtiptra,
					ferecepc,
					cdagente,
					referencia,
					nombre,
					festatus,
					status,
					comments,
					nmsolici,
					cdtipsit,
					cdusuari,
					cdsisrol,
					swimpres,
					cdtipflu,
					cdflujomc,
					valores,
					cdtipsup,
					renuniext,
					renramo,
					renpoliex,
					origenMesa,
					cdsucadm
			);
			
			paso = "Registrando movimiento";
			logger.debug(paso);
			
			Date fechaHoy = new Date();
			
			String cdusuariDestino = cdusuari,
			       cdsisrolDestino = cdsisrol;
			
			boolean turnarAOtraPersona = false;
			        //userSinPermisoEndoso = false;
			
			// Si el sistema genera el tramite o el tramite viene de sigs, hay que turnarlo
			if (Constantes.USUARIO_SISTEMA.equals(cdusuari)
			        || Constantes.ROL_SISTEMA.equals(cdsisrol)
			        || inyectadoDesdeSigs
			        ) {
			    turnarAOtraPersona = true;
			}
			
			// Si la persona que registra el endoso de auto no tiene permisos, hay que turnarlo
			/*if ((!turnarAOtraPersona)
			        && origenMesa
			        && FlujoMC.AUTOS_ENDOSO.getCdflujomc().equals(cdflujomc)
			    ) {
			    boolean tienePermiso = false;
			    List<Map<String, String>> endososPermitidos = despachadorDAO.recuperarPermisosEndosos(cdusuari, cdsisrol);
			    for (Map<String, String> elem : endososPermitidos) {
			        if (cdramo.equals(elem.get("CDRAMO"))
			                && cdtipsup.equals(elem.get("CDTIPSUP"))) {
			            tienePermiso = true;
			            break;
			        }
			    }
			    if (!tienePermiso) {
			        turnarAOtraPersona   = true;
			        userSinPermisoEndoso = true;
			    }
			}*/
			
			String commentsCreacion = Utils.join(
                    "Se registra un nuevo tr\u00e1mite desde mesa de control con las siguientes observaciones: ",
                    StringUtils.isBlank(comments)
                        ? "(sin observaciones)"
                        : comments
            );
			
			if (turnarAOtraPersona) {
			    cdusuariDestino = null;
			    cdsisrolDestino = null;
			    
			    /*if (userSinPermisoEndoso) {
    			    paso = "Guardando detalle";
                    logger.debug(paso);
                    mesaControlDAO.movimientoDetalleTramite(
                            ntramite,
                            fechaHoy,
                            null, // cdclausu
                            commentsCreacion,
                            cdusuari,
                            null, // cdmotivo
                            cdsisrol,
                            "S",
                            null, //cdusuariDes,
                            null, //cdsisrolDes,
                            status,
                            false // cerrado
                            );
                    
                    paso = "Abriendo historial";
                    logger.debug(paso);
                    flujoMesaControlDAO.guardarHistoricoTramite(
                            fechaHoy,
                            ntramite,
                            cdusuari,
                            cdsisrol,
                            status,
                            cdunieco,
                            ConstantesDespachador.TIPO_ASIGNACION_REASIGNA);
                    
                    commentsCreacion = "Tr\u00e1mite turnado autom\u00e1ticamente por perfilamiento";
			    }*/
			}
			
		    RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
                    cdusuari,
                    cdsisrol,
                    ntramite,
                    status,
                    commentsCreacion,
                    null,  // cdrazrecha
                    cdusuariDestino,
                    cdsisrolDestino,
                    true,  // permisoAgente
                    false, // porEscalamiento
                    fechaHoy,
                    false,  // sinGrabarDetalle
                    turnarAOtraPersona
                    );
            logger.debug(despacho.getMessage());			
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				"\n@@@@@@ ntramite = ", ntramite,
				"\n@@@@@@ registrarTramite @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
		));
		return ntramite;
	}
	
	@Override
	public void actualizaTramiteMC(
			  String ntramite ,String cdunieco ,String cdramo ,String estado  ,String nmpoliza
			 ,String cdtiptra ,String cduniext ,String renramo   ,String nmpoliex
	) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ actualizaNmpolizaMesaControl @@@@@@",
				"\n@@@@@@ ntramite   = " , ntramite,
				"\n@@@@@@ cdunieco   = " , cdunieco,
				"\n@@@@@@ cdramo     = " , cdramo,
				"\n@@@@@@ estado     = " , estado,
				"\n@@@@@@ nmpoliza   = " , nmpoliza,
				"\n@@@@@@ cdtiptra   = " , cdtiptra,
				"\n@@@@@@ renuniext  = " , cduniext,
				"\n@@@@@@ ramo       = " , renramo,
				"\n@@@@@@ nmpoliex   = " , nmpoliex
		));
		String paso = "Actualizando numero de poliza en MC";
		try {
			mesaControlDAO.actualizaNmpolizaMesaControl(
					ntramite
				   ,cdunieco
				   ,cdramo
				   ,estado
				   ,nmpoliza
				   ,cdtiptra
				   ,cduniext
				   ,renramo
				   ,nmpoliex);

		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				"\n@@@@@@ ntramite = ", ntramite,
				"\n@@@@@@ actualizaNmpolizaMesaControl @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
		));
	}
	
	@Override
	public List<Map<String,String>>cargarAccionesEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String cdentidad
			,String webid
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarAccionesEntidad @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ tipoent="   , tipoent
				,"\n@@@@@@ cdentidad=" , cdentidad
				,"\n@@@@@@ webid="     , webid
				,"\n@@@@@@ cdsisrol="  , cdsisrol
				));
		
		List<Map<String,String>> acciones = new ArrayList<Map<String,String>>();
		String                   paso     = null;
		
		try
		{
			paso = "Recuperando acciones de entidad";
			logger.debug(paso);
			List<Map<String,String>> tmp = flujoMesaControlDAO.cargarAccionesEntidad(
					cdtipflu
					,cdflujomc
					,tipoent
					,cdentidad
					,webid
					,cdsisrol
					);
			
			for(Map<String,String> accion : tmp)
			{
				if(StringUtils.isNotBlank(accion.get("CDESTADOMC")))
				{
					accion.put("TIPODEST"  , "E");
					accion.put("CLAVEDEST" , accion.get("CDESTADOMC"));
					accion.put("WEBIDDEST" , accion.get("WEBIDESTADO"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDPANTMC")))
				{
					accion.put("TIPODEST"  , "P");
					accion.put("CLAVEDEST" , accion.get("CDPANTMC"));
					accion.put("WEBIDDEST" , accion.get("WEBIDPANT"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDCOMPMC")))
				{
					accion.put("TIPODEST"  , "C");
					accion.put("CLAVEDEST" , accion.get("CDCOMPMC"));
					accion.put("WEBIDDEST" , accion.get("WEBIDCOMP"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDPROCMC")))
				{
					accion.put("TIPODEST"  , "O");
					accion.put("CLAVEDEST" , accion.get("CDPROCMC"));
					accion.put("WEBIDDEST" , accion.get("WEBIDPROC"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDVALIDA")))
				{
					accion.put("TIPODEST"  , "V");
					accion.put("CLAVEDEST" , accion.get("CDVALIDA"));
					accion.put("WEBIDDEST" , accion.get("WEBIDVALIDA"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDREVISI")))
				{
					accion.put("TIPODEST"  , "R");
					accion.put("CLAVEDEST" , accion.get("CDREVISI"));
					accion.put("WEBIDDEST" , accion.get("WEBIDREVISI"));
				}
				else if(StringUtils.isNotBlank(accion.get("CDMAIL")))
				{
					accion.put("TIPODEST"  , "M");
					accion.put("CLAVEDEST" , accion.get("CDMAIL"));
					accion.put("WEBIDDEST" , accion.get("WEBIDMAIL"));
				}
				else
				{
					throw new ApplicationException("Acci\u00f3n no conectada");
				}
				if(StringUtils.isBlank(accion.get("TIPODEST"))
						||StringUtils.isBlank(accion.get("CLAVEDEST"))
						||StringUtils.isBlank(accion.get("WEBIDDEST"))
						)
				{
					throw new ApplicationException("Acci\u00f2n mapeada con error");
				}
				acciones.add(accion);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarAccionesEntidad @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return acciones;
	}
	
	@Override
	public void procesoDemo(
			FlujoVO flujo
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ procesoDemo @@@@@@"
				,"\n@@@@@@ flujo="    , flujo
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));

		String paso = "Cambiando fecha";
		logger.debug(paso);
		
		try
		{
			mesaControlDAO.movimientoDetalleTramite(
					flujo.getNtramite()
					,new Date()//feinicio
					,null//cdclausu
					,Utils.join("Se agrega nuevo detalle ",Utils.format(new Date()))
					,cdusuari
					,null//cdmotivo
					,cdsisrol
					,"S"
					,null
					,null
					,"-1"
					,false
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ procesoDemo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String, Object> ejecutaRevision(FlujoVO flujo, String cduser, String cdrol)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ ejecutaRevision @@@@@@"
				,"\n@@@@@@ flujo=",flujo
				));
		
		Map<String, Object> docsFaltan = null;
		String             paso       = null;
		
		try
		{
			paso = "Recuperando lista de documentos faltantes";
			logger.debug(paso);
			
			docsFaltan = flujoMesaControlDAO.recuperarDocumentosRevisionFaltantes(
					flujo.getCdtipflu()
					,flujo.getCdflujomc()
					,flujo.getClaveent()
					,flujo.getNtramite()
					,cduser
					,cdrol
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ docsFaltan=",docsFaltan
				,"\n@@@@@@ ejecutaRevision @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return docsFaltan;
	}
	
	/*
	JTEZVA 1 NOV 2016 SE DEJA DE USAR
	@SuppressWarnings("deprecation")
	@Override
	public String turnarTramite(
			 String ntramite
			,String statusOld
			,String cdtipasigOld
			,String statusNew
			,String cdtipasigNew
			,String cdusuariSes
			,String cdsisrolSes
			,String comments
			,boolean cerrado
			,String swagente
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ turnarTramite @@@@@@"
				,"\n@@@@@@ ntramite="     , ntramite
				,"\n@@@@@@ statusOld="    , statusOld
				,"\n@@@@@@ cdtipasigOld=" , cdtipasigOld
				,"\n@@@@@@ statusNew="    , statusNew
				,"\n@@@@@@ cdtipasigNew=" , cdtipasigNew
				,"\n@@@@@@ cdusuariSes="  , cdusuariSes
				,"\n@@@@@@ cdsisrolSes="  , cdsisrolSes
				,"\n@@@@@@ comments="     , comments
				,"\n@@@@@@ cerrado="      , cerrado
				,"\n@@@@@@ swagente="     , swagente
				));
		String paso    = "Iniciando turnado",
		       message = null;
		try {
			/ *
			Map<String,String> usuarioDestino = new HashMap<String,String>();
			Date               fecstatu       = new Date();
			
			boolean destinoSimple = true;
			
			if(!"1".equals(cdtipasigNew)) //es por carrusel o por carga
			{
				destinoSimple = false;
				
				paso = "Recuperando datos del tr\u00e1mite";
				logger.debug(paso);
				Map<String,Object> datosTramite = flujoMesaControlDAO.recuperarDatosTramiteValidacionCliente(
						null//cdtipflu
						,null//cdflujomc
						,null//tipoent
						,null//claveent
						,null//webid
						,ntramite
						,null//status
						,null//cdunieco
						,null//cdramo
						,null//estado
						,null//nmpoliza
						,null//nmsituac
						,null//nmsuplem
						);
				Map<String,String> tramite = (Map<String,String>)datosTramite.get("TRAMITE");
				String cdtipflu  = tramite.get("CDTIPFLU");
				String cdflujomc = tramite.get("CDFLUJOMC");
				
				paso = "Recuperando rol destino";
				logger.debug(paso);
				
				List<Map<String,String>> roles         = flujoMesaControlDAO.recuperaTfluestrol(cdtipflu, cdflujomc, statusNew);
				logger.debug(Utils.log("\nroles=",roles));
				String                   cdsisrolTurnado = null;
				for(Map<String,String>rol:roles)
				{
					if("S".equals(rol.get("SWTRABAJO")))
					{
						cdsisrolTurnado = rol.get("CDSISROL");
						break;
					}
				}
				if(StringUtils.isBlank(cdsisrolTurnado))
				{
					throw new ApplicationException("No hay rol definido para ver el status nuevo");
				}
				logger.debug(Utils.log("\ncdsisrolNuevo=",cdsisrolTurnado));
				
				String cadenaTipoTurnado = null;
				if("3".equals(cdtipasigNew))
				{
					cadenaTipoTurnado = "CARRUSEL";
				}
				else if("4".equals(cdtipasigNew))
				{
					cadenaTipoTurnado = "TAREAS";
				}
				else
				{
					throw new ApplicationException("El tipo de asignaci\u00f3n no es correcto");
				}
				
				paso = "Verificando si lo tenia ese rol anteriormente";
				logger.debug(paso);
				
				Map<String,String> usuarioMismoRolAnteriormente =
						flujoMesaControlDAO.recuperarUsuarioHistoricoTramitePorRol(ntramite,cdsisrolTurnado);
				
				if(StringUtils.isNotBlank(usuarioMismoRolAnteriormente.get("cdusuari"))
						&&StringUtils.isNotBlank(usuarioMismoRolAnteriormente.get("dsusuari"))
						)
				{
					logger.debug(Utils.log("si lo tenia antes alguien del mismo rol <",cdsisrolTurnado,"> era <",usuarioMismoRolAnteriormente.get("dsusuari"),">"));
					usuarioDestino = usuarioMismoRolAnteriormente;
					usuarioDestino.put("cdsisrol" , cdsisrolTurnado);
				}
				else
				{
					paso = "Recuperando usuario destino del tr\u00e1mite";
					logger.debug(Utils.log("\n",paso,", tipo=",cadenaTipoTurnado));
					usuarioDestino = flujoMesaControlDAO.recuperarUsuarioParaTurnado(cdsisrolTurnado,cadenaTipoTurnado);
					usuarioDestino.put("cdsisrol" , cdsisrolTurnado);
					logger.debug(Utils.log("\nusuario destino=",usuarioDestino));
				}
				
				paso = "Guardando historico de tramite";
				logger.debug(paso);
				
				flujoMesaControlDAO.guardarHistoricoTramite(new Date(),ntramite,usuarioDestino.get("cdusuari"),cdsisrolTurnado,statusOld, null, null);
			}
			
			if(!"1".equals(cdtipasigOld)) //antes lo tenia un usuario especifico
			{
				Map<String,Object> datos = flujoMesaControlDAO.recuperarDatosTramiteValidacionCliente(
						null//cdtipflu
						,null//cdflujomc
						,null//tipoent
						,null//claveent
						,null//webid
						,ntramite
						,null//status
						,null//cdunieco
						,null//cdramo
						,null//estado
						,null//nmpoliza
						,null//nmsituac
						,null//nmsuplem
						);
				Map<String,String> tramite = (Map<String,String>)datos.get("TRAMITE");
				
				String cdusuariActual  = tramite.get("CDUSUARI")
				       ,cdsisrolActual = cdsisrolSes;
				
				if(!cdusuariSes.equals(cdusuariActual))
				{
					/*
					 * el usuario de sesion no es el dueño del tramite,
					 * hay que recuperar al dueño para descontarselo.
					 * Ya sabemos de quien es por TMESACONTROL.CDUSUARI
					 * pero no sabemos el rol, lo necesitamos para descontarlo en la lista,
					 * porque la lista viene en clave doble CDUSUARI-CDSISROL
					 *
					cdsisrolActual = flujoMesaControlDAO.recuperarRolRecienteTramite(ntramite,cdusuariActual);
				}
				
				flujoMesaControlDAO.restarTramiteUsuario(cdusuariActual,cdsisrolActual);
			}
			* /
			
			Date fechaHoy = new Date();
			paso = "Recuperando tipo de estatus";
			logger.debug(paso);
			boolean esFinal = despachadorDAO.esStatusFinal(ntramite, statusNew); // estatus final: CONFIRMADO, RECHAZADO, CANCELADO...
			RespuestaDespachadorVO destino = null;
			if (!esFinal) {
				destino = despachadorManager.despachar(ntramite, statusNew);
			}
			
			paso = "Actualizando status";
			logger.debug(paso);
			
			flujoMesaControlDAO.actualizarStatusTramite(
					ntramite
					,statusNew
					,fechaHoy
					,esFinal
						? null
						: destino.getCdusuari()
					);
			
			if (esFinal) { // hay que borrar el usuario
				paso = "Borrar usuario encargado";
				logger.debug(paso);
				despachadorDAO.borrarUsuarioEncargado(ntramite);
			}
			
			paso = "Cerrando historial anterior";
			logger.debug(paso);
			despachadorDAO.cerrarHistorialTramite(ntramite, fechaHoy, cdusuariSes, cdsisrolSes, esFinal ? statusNew : destino.getStatus());
			
			if (!esFinal) {
			    paso = "Insertar nuevo historial";
			    logger.debug(paso);
			    flujoMesaControlDAO.guardarHistoricoTramite(fechaHoy, ntramite, cdusuariSes, cdsisrolSes, destino.getStatus(),
			            destino.getCdunieco(), destino.getCdtipasig());
			}
			
			paso = "Recuperando descripci\u00f3n de estatus";
			logger.debug(paso);
			
			List<Map<String,String>>estados = flujoMesaControlDAO.recuperaTestadomc(statusNew);
			if(estados==null||estados.size()==0)
			{
				throw new ApplicationException("No se encuentra la descripci\u00f3n del status");
			}
			if(estados.size()>1)
			{
				throw new ApplicationException("Descripci\u00f3n del status duplicada");
			}
			
			paso = "Guardando detalle de tr\u00e1mite";
			logger.debug(paso);
			
			mesaControlDAO.movimientoDetalleTramite(
					ntramite
					,new Date()//feinicio
					,null//cdclausu
					,Utils.join("Tr\u00e1mite turnado a status \"",estados.get(0).get("DSESTADOMC"),"\": ",Utils.NVL(comments,"(sin comentarios)"))
					,cdusuariSes
					,null//cdmotivo
					,cdsisrolSes
					,"S".equals(swagente) ? "S" : "N"
					,esFinal ? destino.getCdusuari() : null
					,esFinal ? destino.getCdsisrol() : null
					,statusNew
					,cerrado
					);
			
			if(destinoSimple)
			{
				message = Utils.join("El tr\u00e1mite ", ntramite, " ha sido turnado con \u00e9xito");
			}
			else
			{
				message = Utils.join("El tr\u00e1mite ", ntramite, " ha sido turnado a ",usuarioDestino.get("dsusuari"));
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		if(statusNew.trim().equals("4"))// 4= rechazado
		{
		    try 
			{	
				paso = "actualizaEstatusTramiteMCsigs";
				Map<String,String>parame = siniestrosManager.obtenerTramiteCompleto(ntramite);
				if(!parame.isEmpty() && parame.size()>0 && parame.get("RENPOLIEX")!=null )
				{
					logger.debug(Utils.log(
							"\n###########################################"
							,"\n###### actualizaEstatusTramiteMCsigs ######"
							,"\nPoliza extraida del sigs: ",parame.get("RENUNIEXT"),"/", parame.get("RENRAMO"),"/", parame.get("RENPOLIEX")
							));
					
					consultasPolizaManager.actualizaTramiteMC(new PolizaVO(	 parame.get("RENUNIEXT")
																			,parame.get("RENRAMO") 
																			,null
																			,parame.get("RENPOLIEX")
																			,ntramite),
																			"0");//0 para que aparezaca como posible a renovar
					
					logger.debug(Utils.log(
							"\n###### actualizaEstatusTramiteMCsigs ######"
							,"\n###########################################"
							));
				}
		    }
		    catch (Exception e) 
	        {
			    logger.error("Error al actulizar estatus de tramite Mc", e);
			    paso = Utils.manejaExcepcion(e);
		    }
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ message=",message
				,"\n@@@@@@ turnarTramite @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return message;
	}
	*/
	
	@Override
	public Map<String,Object> recuperarDatosTramiteValidacionCliente(FlujoVO flujo)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarDatosTramiteValidacionCliente @@@@@@"
				,"\n@@@@@@ flujo=",flujo
				));
		String paso = "Recuperando datos para validaci\u00f3n cliente";
		Map<String,Object> datos = null;
		try
		{
			datos = flujoMesaControlDAO.recuperarDatosTramiteValidacionCliente(
					flujo.getCdtipflu()
					,flujo.getCdflujomc()
					,flujo.getTipoent()
					,flujo.getClaveent()
					,flujo.getWebid()
					,flujo.getNtramite()
					,flujo.getStatus()
					,flujo.getCdunieco()
					,flujo.getCdramo()
					,flujo.getEstado()
					,flujo.getNmpoliza()
					,flujo.getNmsituac()
					,flujo.getNmsuplem()
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ datos=",datos
				,"\n@@@@@@ recuperarDatosTramiteValidacionCliente @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return datos;
	}
	
	/*
	JTEZVA 1 NOV 2016 SE DEJA DE USAR
	@Override
	public String turnarDesdeComp(
			String cdusuari
			,String cdsisrol
			,String cdtipflu
			,String cdflujomc
			,String ntramite
			,String statusOld
			,String statusNew
			,String swagente
			,String comments
			,boolean cerrado
			,String cdrazrecha
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ turnarDesdeComp @@@@@@"
				,"\n@@@@@@ cdusuari   = ", cdusuari
				,"\n@@@@@@ cdsisrol   = ", cdsisrol
				,"\n@@@@@@ cdtipflu   = ", cdtipflu
				,"\n@@@@@@ cdflujomc  = ", cdflujomc
				,"\n@@@@@@ ntramite   = ", ntramite
				,"\n@@@@@@ statusOld  = ", statusOld
				,"\n@@@@@@ statusNew  = ", statusNew
				,"\n@@@@@@ swagente   = ",  swagente
				,"\n@@@@@@ comments   = ", comments
				,"\n@@@@@@ cerrado    = ", cerrado
				,"\n@@@@@@ cdrazrecha = ", cdrazrecha
				));
		
		String message = null
		       ,paso   = null;
		
		try
		{
			if (EstatusTramite.RECHAZADO.getCodigo().equals(statusNew)) {
				paso = "Validando cambios pendientes de un endoso";
				logger.debug(paso);
				endososDAO.validarTramiteSinCambiosEndosoPendiente(ntramite);
			}
			
			paso = "Recuperando status anterior";
			logger.debug(paso);
			
			List<Map<String,String>>statusesOld = flujoMesaControlDAO.recuperaTfluest(cdtipflu, cdflujomc, statusOld);
			if(statusesOld.size()==0)
			{
				throw new ApplicationException("Status anterior no existe");
			}
			if(statusesOld.size()>1)
			{
				throw new ApplicationException("Status anterior repetido");
			}
			Map<String,String> statusAnterior = statusesOld.get(0);
			logger.debug(Utils.log("=",statusAnterior));
			
			paso = "Recuperando status nuevo";
			logger.debug(paso);
			
			List<Map<String,String>>statusesNew = flujoMesaControlDAO.recuperaTfluest(cdtipflu, cdflujomc, statusNew);
			if(statusesNew.size()==0)
			{
				throw new ApplicationException("Status nuevo no existe");
			}
			if(statusesNew.size()>1)
			{
				throw new ApplicationException("Status nuevo repetido");
			}
			Map<String,String> statusNuevo = statusesNew.get(0);
			logger.debug(Utils.log("=",statusNuevo));
			
			paso = "Invocando turnado general";
			logger.debug(paso);
			
			message = this.turnarTramite(
					ntramite
					,statusOld
					,statusAnterior.get("CDTIPASIG")
					,statusNew
					,statusNuevo.get("CDTIPASIG")
					,cdusuari
					,cdsisrol
					,comments
					,cerrado
					,swagente
					);
			
			if (StringUtils.isNotBlank(cdrazrecha)) { // Marcamos el motivo de rechazo en tmesacontrol
				paso = "Marcando motivo de rechazo";
				logger.debug(paso);
				flujoMesaControlDAO.guardarMotivoRechazoTramite(ntramite, cdrazrecha);
			}
			
			paso = "Enviando correos de status nuevo";
			logger.debug(paso);
			mandarCorreosStatusTramite(ntramite, cdsisrol, false);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ message=",message
				,"\n@@@@@@ turnarDesdeComp @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return message;
	}
	*/
	
	@Override
	public void recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(FlujoVO flujo) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos @@@@@@"
				,"\n@@@@@@ flujo=", flujo
				));
		
		String paso = "Recuperando propiedades actuales desde componente anterior";
		
		try
		{
			Map<String,String> conexionFantasma = flujoMesaControlDAO.recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(
					flujo.getCdtipflu()
					,flujo.getCdflujomc()
					,flujo.getTipoent()
					,flujo.getClaveent()
					,flujo.getWebid()
					);
			
			logger.debug("Datos de pantalla/componente actual recuperados: {}",conexionFantasma);
			
			flujo.setTipoent(conexionFantasma.get("TIPOENT"));
			flujo.setClaveent(conexionFantasma.get("CDENTIDAD"));
			flujo.setWebid(conexionFantasma.get("WEBID"));
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ flujo=", flujo
				,"\n@@@@@@ recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String,String> recuperarPolizaUnicaDanios(
			String cduniext
			,String ramo
			,String nmpoliex
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarPolizaUnicaDanios @@@@@@"
				,"\n@@@@@@ cduniext = " , cduniext
				,"\n@@@@@@ ramo     = " , ramo
				,"\n@@@@@@ nmpoliex = " , nmpoliex
				));
		String             paso   = "Recuperando p\u00f3liza";
		Map<String,String> poliza = null;
		try
		{
			poliza = flujoMesaControlDAO.recuperarPolizaUnicaDanios(
					cduniext
					,ramo
					,nmpoliex
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarPolizaUnicaDanios @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return poliza;
	}
	
	@Override
	public void guardarTtipflurol(String cdtipflu, List<Map<String,String>> lista) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarTtipflurol @@@@@@"
				,"\n@@@@@@ cdtipflu = " , cdtipflu
				,"\n@@@@@@ lista    = " , lista
				));
		
		String paso = "Guardando permisos de tr\u00e1mite";
		
		try
		{
			flujoMesaControlDAO.guardarTtipflurol(cdtipflu,lista);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarTtipflurol @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void guardarTflujorol(
			String cdtipflu
			,String cdflujomc
			,List<Map<String,String>> lista
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarTflujorol @@@@@@"
				,"\n@@@@@@ cdtipflu  = " , cdtipflu
				,"\n@@@@@@ cdflujomc = " , cdflujomc
				,"\n@@@@@@ lista     = " , lista
				));
		
		String paso = "Guardando permisos de proceso";
		
		try
		{
			flujoMesaControlDAO.guardarTflujorol(cdtipflu,cdflujomc,lista);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarTflujorol @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String,String> cargarDatosTitulo(
			String cdtipflu
			,String cdflujomc
			,String webid
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosTitulo @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ webid="     , webid
				));
		
		Map<String,String> titulo = null;
		
		String paso = null;
		
		try
		{
			paso = "Recuperando validaciones";
			logger.debug(paso);
			
			List<Map<String,String>> titulos = flujoMesaControlDAO.recuperaTflutit(cdtipflu, cdflujomc, null);
			
			for(Map<String,String>tituloIte : titulos)
			{
				if(tituloIte.get("WEBID").equals(webid))
				{
					titulo = tituloIte;
					break;
				}
			}
			
			if(titulo==null)
			{
				throw new ApplicationException("No se encuentra el t\u00edtulo");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosTitulo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return titulo;
	}
	
	@Override
	public void guardarDatosTitulo(
			String cdtipflu
			,String cdflujomc
			,String cdtitulo
			,String webid
			,String xpos
			,String ypos
			,String dstitulo
			,String accion
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarDatosTitulo @@@@@@"
				,"\n@@@@@@ cdtipflu="   , cdtipflu
				,"\n@@@@@@ cdflujomc="  , cdflujomc
				,"\n@@@@@@ cdtitulo="   , cdtitulo
				,"\n@@@@@@ webid="      , webid
				,"\n@@@@@@ xpos="       , xpos
				,"\n@@@@@@ ypos="       , ypos
				,"\n@@@@@@ dstitulo="   , dstitulo
				,"\n@@@@@@ accion="     , accion
				));
		
		String paso = "Guardando datos de validaci\u00f3n";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTflutit(
					cdtipflu
					,cdflujomc
					,cdtitulo
					,dstitulo
					,webid
					,xpos
					,ypos
					,accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarDatosTitulo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public String modificarDetalleTramiteMC(
			String ntramite,
			String nmordina,
			String comments,
			String cdusuari,
			String cdsisrol
			) throws Exception
	{
		return flujoMesaControlDAO.modificarDetalleTramiteMC(ntramite, nmordina, comments, cdusuari, cdsisrol, new Date());
	}
	
	@Override
	public void guardarDatosCorreo(
			String cdtipflu,
			String cdflujomc,
			String cdmail,
			String dsmail,
			String dsdestino,
			String dsasunto,
			String dsmensaje,
			String vardestino,
			String varasunto,
			String varmensaje,
			String webid,
			String xpos,
			String ypos,
			String accion)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarDatosCorreo @@@@@@@@@@"
				,"\n@@@@@@ cdtipflu="   , cdtipflu
				,"\n@@@@@@ cdflujomc="  , cdflujomc
				,"\n@@@@@@ cdmail="     , cdmail
				,"\n@@@@@@ dsmail="     , dsmail
				,"\n@@@@@@ dsdestino="  , dsdestino
				,"\n@@@@@@ dsasunto="   , dsasunto
				,"\n@@@@@@ dsmensaje="  , dsmensaje
				,"\n@@@@@@ vardestino=" , vardestino
				,"\n@@@@@@ varasunto="  , varasunto
				,"\n@@@@@@ varmensaje=" , varmensaje
				,"\n@@@@@@ webid="      , webid
				,"\n@@@@@@ xpos="       , xpos
				,"\n@@@@@@ ypos="       , ypos
				,"\n@@@@@@ accion="     , accion
				));
		
		String paso = "Guardando datos de validaci\u00f3n";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTmail(
					cdtipflu,
					cdflujomc,
					cdmail,
					dsmail,
					dsdestino,
					dsasunto,
					dsmensaje,
					vardestino,
					varasunto,
					varmensaje,
					webid,
					xpos,
					ypos,
					accion
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@@@ guardarDatosCorreo @@@@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String,String> cargarDatosCorreo(
			String cdtipflu
			,String cdflujomc
			,String cdmail
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarDatosCorreo @@@@@@"
				,"\n@@@@@@ cdtipflu="  , cdtipflu
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				,"\n@@@@@@ cdmail="    , cdmail
				));
		
		Map<String,String> correo = null;
		String             paso       = null;
		
		try
		{
			paso = "Recuperando correo";
			logger.debug(paso);
			
			List<Map<String,String>> correos = flujoMesaControlDAO.recuperaTflumail(cdtipflu, cdflujomc, cdmail);
			
			for(Map<String,String> correoIte : correos)
			{
				if(correoIte.get("CDMAIL").equals(cdmail))
				{
					correo = correoIte;
					break;
				}
			}
			
			if(correo==null)
			{
				throw new ApplicationException("No se encuentra el correo");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarDatosCorreo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return correo;
	}
	
	@Override
	public Map<String, String> recuperarChecklistInicial (String ntramite, String cdsisrol) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ recuperarChecklistInicial @@@@@@",
				"\n@@@@@@ cdtipflu = " , ntramite,
				"\n@@@@@@ cdsisrol = " , cdsisrol));
		String paso = null;
		Map<String, String> mapa = null;
		try {
			paso = "Recuperando lista de checklist";
			List<Map<String, String>> lista = flujoMesaControlDAO.recuperarChecklistInicial(ntramite, cdsisrol);
			if (lista == null || lista.size() == 0) {
				mapa = new LinkedHashMap<String, String>();
			} else {
				mapa = lista.get(0);
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				"\n@@@@@@ mapa = ", mapa,
				"\n@@@@@@ recuperarChecklistInicial @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return mapa;
	}
	
	/**
	 * SOBRECARGADO
	 */
	@Override
	@Deprecated
	public Map<String, String> enviaCorreoFlujo(FlujoVO flujo, Map<String, String> params) throws Exception {
	    return this.enviaCorreoFlujo(flujo, params, false, null);
	}
	
	@Override
	public Map<String, String> enviaCorreoFlujo(FlujoVO flujo, Map<String, String> params, boolean soloCorreosRecibidos,
            String correosRecibidos) throws Exception {		
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ enviaCorreoFlujo @@@@@@",
				"\n@@@@@@ flujo                = " , flujo,
				"\n@@@@@@ params               = " , params,
                "\n@@@@@@ soloCorreosRecibidos = " , soloCorreosRecibidos,
                "\n@@@@@@ correosRecibidos     = " , correosRecibidos
				));
		String paso = null;
		Map<String, String> mapa = null;
		try{
			paso = "Recuperando funciones";
			List<Map<String, String>> funciones = flujoMesaControlDAO.recuperaTvarmailSP();
			Map<Integer, Map<String, String>> mapFunciones   = new HashMap<Integer, Map<String, String>>();
			for(Map<String, String> map:funciones){
				mapFunciones.put(Integer.parseInt(map.get("CDVARMAIL")), map);
			}
			
			if (soloCorreosRecibidos) {
			    params.put("dsdestino", correosRecibidos);
			} else {
			    params.put("dsdestino", cambiarTextoCorreo(flujo.getNtramite(), params.get("dsdestino"), params.get("vardestino"), mapFunciones));
			}
			
			params.put("dsasunto" , cambiarTextoCorreo(flujo.getNtramite(), params.get("dsasunto") , params.get("varasunto") , mapFunciones));
			params.put("dsmensaje", cambiarTextoCorreo(flujo.getNtramite(), params.get("dsmensaje"), params.get("varmensaje"), mapFunciones));
			
			if (StringUtils.isNotBlank(params.get("dsmensaje"))) {
				params.put("dsmensaje", params.get("dsmensaje").replace("\n", "<br/>"));
				params.put("dsmensaje", Utils.cambiaAcentosUnicodePorAcentosHtml(params.get("dsmensaje")));
			}
			
			paso = "Antes de enviar el correo";
			logger.debug(Utils.log(
					"\n|||||||||||||||||||||||||||||||||||||||",
					"\n||||||      ENVIANDO CORREO      ||||||",
					"\n|||||| a       = ", params.get("dsdestino"),
					"\n|||||| asunto  = ", params.get("dsasunto"),
					"\n|||||| mensaje = ", params.get("dsmensaje"),
					"\n||||||      ENVIANDO CORREO      ||||||",
					"\n|||||||||||||||||||||||||||||||||||||||"
			));
			
			if (StringUtils.isNotBlank(params.get("dsdestino")) && !params.get("dsdestino").contains("()")){
				boolean enviado = mailService.enviaCorreo(StringUtils.split(params.get("dsdestino"),";"), 
						  new String[]{}, 
						  new String[]{}, 
						  params.get("dsasunto"), 
						  params.get("dsmensaje"), 
						  new String[]{}, 
						  true);
				if(!enviado){
					throw new ApplicationException("No se pudo enviar el correo a "+params.get("dsdestino"));
				}
			}
			//TODO
			//AGREGAR ELSE PARA REGISTRO DE BITACORA DE ERROR DE CORREO
			
		}catch(Exception ex){
			Utils.generaExcepcion(ex, paso);
		}
		return params;
	}
	/**
	 * Sustituye llaves en texto por variables
	 * @param nmtramite  tramite de mesa de control
	 * @param texto	     texto con llaver
	 * @param variables	 variables separados por coma
	 * @param funciones	 catalogo de funciones para traer valores de BD
	 * @return texto con valores sustituidos
	 */
	private String cambiarTextoCorreo(String nmtramite, String texto, String variables, Map<Integer, Map<String, String>> funciones) throws Exception{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ cambiarTextoCorreo @@@@@@@@@@@@@",
				"\n@@@@@@ nmtramite  = " , nmtramite,
				"\n@@@@@@ texto      = " , texto,
				"\n@@@@@@ variables  = " , variables,
				"\n@@@@@@ funciones  = " , funciones
				));
		String paso    = null;
		String mensaje = null;
		try{
			Utils.validate(nmtramite,"No se recibio el numero de tramite",
						   texto    ,"No se recibio el mensaje a cambiar");
			Utils.validate(funciones,"No se recibieron funciones");
			paso = "empieza a cambiar texto";
			ArrayList<String> resultados = new ArrayList<String>();
			if(StringUtils.isNotBlank(variables)){
				mensaje = texto;
				String[] arrVars = variables.split(",");
				for(String s:arrVars){
					String result = flujoMesaControlDAO.ejecutaProcedureFlujoCorreo(funciones.get(Integer.parseInt(s)).get("BDFUNCTION"),nmtramite);
					if (null == result || !StringUtils.isNotBlank(result)){
						result = "()";
					}
					mensaje = StringUtils.replaceOnce(mensaje, "{}", result);
				}
				logger.debug(Utils.log("\n@@@@@@ mensaje",
						   "\n",mensaje));
			}else{
				mensaje = texto;
			}
		}catch(Exception ex){
			Utils.generaExcepcion(ex, paso);
		}
		return mensaje;
	}
	
	/**
	 * SOBRECARGADO
	 */
	@Deprecated
	@Override
	public void mandarCorreosStatusTramite(String ntramite, String cdsisrol, boolean porEscalamiento) throws Exception {
	    
	}
	
	@Override
	public void mandarCorreosStatusTramite(String ntramite, String cdsisrol, boolean porEscalamiento, boolean soloCorreosRecibidos,
	            String correosRecibidos) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ mandarCorreosStatusTramite @@@@@@",
				"\n@@@@@@ ntramite             = " , ntramite,
				"\n@@@@@@ cdsisrol             = " , cdsisrol,
				"\n@@@@@@ porEscalamiento      = " , porEscalamiento,
                "\n@@@@@@ soloCorreosRecibidos = " , soloCorreosRecibidos,
                "\n@@@@@@ correosRecibidos     = " , correosRecibidos
				));
		String paso = "Recuperando correos de estatus";
		logger.debug(paso);
		try {
			FlujoVO flujo = new FlujoVO();
			flujo.setNtramite(ntramite);
			List<Map<String, String>> correos = flujoMesaControlDAO.obtenerCorreosStatusTramite(ntramite, cdsisrol, porEscalamiento ? "S" : "N");
			
			paso = "Enviando correos de estatus";
			logger.debug(paso);
			
			for (Map<String, String> params:correos) {
				enviaCorreoFlujo(flujo, params, soloCorreosRecibidos, correosRecibidos);
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				"\n@@@@@@ mandarCorreosStatusTramite @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Deprecated
	@Override
	public void guardarMensajeCorreoEmision(String ntramite, String mensajeCorreoEmision) throws Exception {
		flujoMesaControlDAO.guardarMensajeCorreoEmision(ntramite, mensajeCorreoEmision);
	}
	
	@Override
	public Map<String, String> regresarTramiteVencido (String ntramite, boolean soloRevisar, String cdusuari, String cdsisrol) throws Exception {
		logger.debug(Utils.log(
			"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
			"\n@@@@@@ regresarTramiteVencido @@@@@@",
			"\n@@@@@@ ntramite    = " , ntramite,
			"\n@@@@@@ soloRevisar = " , soloRevisar));
		String paso = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
		    Date fechaHoy = new Date();
		    
			paso = "Recuperando estatus anterior al vencimiento";
			logger.debug(paso);
			Map<String, String> estatusAnterior = flujoMesaControlDAO.recuperarEstatusAnteriorVencido(ntramite);
			result.putAll(estatusAnterior);
			String cdstatus  = estatusAnterior.get("CDSTATUS"),
				   dsstatus  = estatusAnterior.get("DSSTATUS"),
				   cdtipflu  = estatusAnterior.get("CDTIPFLU"),
				   cdflujomc = estatusAnterior.get("CDFLUJOMC");
			
			paso = "Recuperando roles que pueden regresar";
			logger.debug(paso);
			List<Map<String, String>> roles = flujoMesaControlDAO.recuperarRolesPermisoRegresarVencido(ntramite, cdstatus);
			String rolesCadena = "No hay roles definidos";
			boolean rolSesionPermiso = false;
			if (roles.size() > 0) {
				StringBuilder rolesBuilder = new StringBuilder();
				for (Map<String, String> rol : roles) {
					rolesBuilder.append(rol.get("DSSISROL")).append("<br/>");
					if(cdsisrol.equals(rol.get("CDSISROL"))) {
						rolSesionPermiso = true;
					}
				}
				rolesCadena = rolesBuilder.toString();
			}
			result.put("rolesCadena" , rolesCadena);
			result.put("permiso"     , rolSesionPermiso ? "S" : "N");
			
			paso = "Recuperando datos del vencimiento";
			logger.debug(paso);
			result.putAll(flujoMesaControlDAO.recuperarDatosVencimiento(ntramite));
			
			if (!soloRevisar) {
				if (!rolSesionPermiso) {
					throw new ApplicationException("No tiene permisos para regresar");
				}
				
				paso = "Recuperando historial";
                logger.debug(paso);
                List<Map<String, String>> historial = despachadorDAO.recuperarHistorialMesa(ntramite);
                if (historial.size() == 0) {
                    throw new ApplicationException("No se puede encontrar el usuario anterior");
                }
                
                String cdusuariAnt = historial.get(0).get("CDUSUARI"),
                       cdsisrolAnt = historial.get(0).get("CDSISROL");
                
                paso = "Turnando tr\u00e1mite";
                logger.debug(paso);
                RespuestaTurnadoVO despacho = despachadorManager.turnarTramite(
                        cdusuari,
                        cdsisrol,
                        ntramite,
                        cdstatus,
                        "Se regresa el tr\u00e1mite desde vencido",
                        null, // cdrazrecha
                        cdusuariAnt,
                        cdsisrolAnt,
                        false, // permisoAgente
                        false, // porEscalamiento
                        fechaHoy, false);
                
                result.put("mensajeDespacho", despacho.getMessage());
				
				try {
					cotizacionDAO.grabarEvento(
						new StringBuilder()
						,Constantes.MODULO_FLAGS
						,Constantes.EVENTO_REGRESAR
						,fechaHoy
						,cdusuari
						,cdsisrol
						,ntramite
						,null //cdunieco
						,null //cdramo
						,null //estado
						,null //nmpoliza
						,null //nmsolici
						,null //cdagente
						,null //cdusuariDes
						,null //cdsisrolDes
						,cdstatus
					);
					Thread.sleep(1000l);
				} catch (Exception ex) {
					logger.debug("Error al grabar evento", ex);
				}
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
			"\n@@@@@@ result = ", result,
			"\n@@@@@@ regresarTramiteVencido @@@@@@",
			"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return result;
	}
	
	@Override
	public void movimientoTrequisi(
			String accion
			,String cdrequisi
			,String dsrequisi
			,String cdtiptra
			,boolean pideDato
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movimientoTrequisi @@@@@@"
				,"\n@@@@@@ accion="    , accion
				,"\n@@@@@@ cdrequisi=" , cdrequisi
				,"\n@@@@@@ dsrequisi=" , dsrequisi
				,"\n@@@@@@ cdtiptra="  , cdtiptra
				,"\n@@@@@@ pideDato="  , pideDato
				));
		
		String paso = "Guardando documento";
		logger.debug(paso);
		
		try
		{
			flujoMesaControlDAO.movimientoTrequisi(cdrequisi, dsrequisi, cdtiptra, pideDato, accion);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ movimientoTrequisi @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public void marcarRequisitoRevision (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrequisi,
			boolean activo,
			String dsdato,
			String cdusuari,
			String cdsisrol) throws Exception {
		logger.debug(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ marcarRequisitoRevision @@@@@@",
				"\n@@@@@@ cdtipflu  = " , cdtipflu,
				"\n@@@@@@ cdflujomc = " , cdflujomc,
				"\n@@@@@@ ntramite  = " , ntramite,
				"\n@@@@@@ cdrequisi = " , cdrequisi,
				"\n@@@@@@ activo    = " , activo,
				"\n@@@@@@ dsdato    = " , dsdato,
				"\n@@@@@@ cdusuari  = " , cdusuari,
				"\n@@@@@@ cdsisrol  = " , cdsisrol);
		String paso = null;
		try {
			paso = "Marcando requisito de revisi\u00f3n";
			logger.debug(paso);
			flujoMesaControlDAO.marcarRequisitoRevision(cdtipflu, cdflujomc, ntramite, cdrequisi, activo, dsdato, cdusuari, cdsisrol);
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(
				"\n@@@@@@ marcarRequisitoRevision @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
	
	@Override
	public void marcarRevisionConfirmada (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrevisi,
			boolean confirmada,
			String cdusuari,
			String cdsisrol) throws Exception {
		logger.debug(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ marcarRevisionConfirmada @@@@@@",
				"\n@@@@@@ cdtipflu  = " , cdtipflu,
				"\n@@@@@@ cdflujomc = " , cdflujomc,
				"\n@@@@@@ ntramite  = " , ntramite,
				"\n@@@@@@ cdrequisi = " , cdrevisi,
				"\n@@@@@@ activo    = " , confirmada,
				"\n@@@@@@ cdusuari  = " , cdusuari,
				"\n@@@@@@ cdsisrol  = " , cdsisrol);
		String paso = null;
		try {
			paso = "Marcando requisito de revisi\u00f3n";
			logger.debug(paso);
			flujoMesaControlDAO.marcarRevisionConfirmada(cdtipflu, cdflujomc, ntramite, cdrevisi, confirmada, cdusuari, cdsisrol);
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(
				"\n@@@@@@ marcarRevisionConfirmada @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
	
	@Deprecated
	@Override
	public List<Map<String, String>> recuperarRequisitosDocumentosObligatoriosFaltantes (String ntramite) throws Exception {
		return flujoMesaControlDAO.recuperarRequisitosDocumentosObligatoriosFaltantes(ntramite);
	}
	
	@Override
	@Deprecated
	public String recuperarEstatusDefectoRol (String cdsisrol) throws Exception {
		return flujoMesaControlDAO.recuperarEstatusDefectoRol(cdsisrol);
	}
	
	@Override
	@Deprecated
	public void actualizaStatusMesaControl(String ntramite, String status) throws Exception {
		mesaControlDAO.actualizaStatusMesaControl(ntramite, status);
	}
	
	@Override
	public void recuperarCotiColec(String cdusuari, String cdsisrol, String ntramite, String nmsolici, String status) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ recuperarCotiColec @@@@@@",
				"\n@@@@@@ cdusuari = ", cdusuari,
				"\n@@@@@@ cdsisrol = ", cdsisrol,
				"\n@@@@@@ ntramite = ", ntramite,
				"\n@@@@@@ nmsolici = ", nmsolici,
				"\n@@@@@@ status   = ", status));
		String paso = null;
		try {
			paso = "Actualizando solicitud";
			logger.debug(paso);
			mesaControlDAO.actualizarNmsoliciTramite(ntramite, nmsolici);
			paso = "Guardando detalle";
			logger.debug(paso);
			mesaControlDAO.movimientoDetalleTramite(
					ntramite,
					new Date(),
					null, //cdclausu
					Utils.join("Se recuper\u00f3 la cotizaci\u00f3n ",nmsolici),
					cdusuari,
					null, //cdmotivo
					cdsisrol,
					"S", // swagente
					null, // cdusuariDest
					null, // cdsisrolDest
					status,
					false //cerrado
					);
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				"\n@@@@@@ recuperarCotiColec @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public Map<String,String> tramiteMC(String ntramite, String nmsolici, String cdunieco, String cdramo, String cdtipsit) throws Exception
	{
	        String mensaje = "Consultando mesa de control para renovacion";
		try
		{
			if(nmsolici!=null && !nmsolici.equals("0") && !nmsolici.isEmpty() && ("|5|6|16|").lastIndexOf("|"+cdramo+"|")!=-1)
			{	
				return siniestrosManager.obtenerTramiteCompletoXNmsolici(nmsolici, cdunieco, cdramo);
				
			}
			else if(ntramite!=null && !ntramite.isEmpty() && ("|5|6|16|").lastIndexOf("|"+cdramo+"|")!=-1)
			{
				return siniestrosManager.obtenerTramiteCompleto(ntramite);
			}
		}
		catch (Exception ex)
		{
			Utils.generaExcepcion(ex, mensaje);
		}
		return new HashMap<String, String>(0);
	}
	
	@Override
	public void guardarVentanaDatosTramite (String ntramite, Map<String, String> datos) throws Exception {
	    logger.debug(Utils.log(
	            "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
	            "\n@@@@@@ guardarVentanaDatosTramite @@@@@@",
	            "\n@@@@@@ ntramite = " , ntramite,
	            "\n@@@@@@ datos    = " , datos));
	    String paso = null;
	    try {
	        paso = "Dividiendo claves concatenadas"; // Si tengo {cdunieco_cdramo : 1} lo convierto en {cdunieco : 1}, {cdramo : 1}
	        logger.debug(paso);
	        Map<String, String> datos2 = new HashMap<String, String>();
	        for (Entry<String, String> en : datos.entrySet()) {
	            String key = en.getKey(),
	                   value = en.getValue();
	            if (key.contains("_")) {
	                String[] keys = key.split("_");
	                for (String keyIte : keys) {
	                    datos2.put(keyIte, value);
	                }
	            } else {
	                datos2.put(key, value);
	            }
	        }
	        
	        paso = "Guardando datos";
	        logger.debug(paso);
	        flujoMesaControlDAO.guardarVentanaDatosTramite(
	                ntramite,
                    datos2.containsKey("cdunieco"),
                    datos2.containsKey("cdramo"),
                    datos2.containsKey("estado"),
                    datos2.containsKey("nmpoliza"),
                    datos2.containsKey("nmsuplem"),
                    datos2.containsKey("nmsolici"),
                    datos2.containsKey("cdsucadm"),
                    datos2.containsKey("cdsucdoc"),
                    datos2.containsKey("cdsubram"),
                    datos2.containsKey("cdtiptra"),
                    datos2.containsKey("ferecepc"),
                    datos2.containsKey("cdagente"),
                    datos2.containsKey("referencia"),
                    datos2.containsKey("nombre"),
                    datos2.containsKey("fecstatu"),
                    datos2.containsKey("status"),
                    datos2.containsKey("comments"),
                    datos2.containsKey("cdtipsit"),
                    datos2.containsKey("otvalor01"),
                    datos2.containsKey("otvalor02"),
                    datos2.containsKey("otvalor03"),
                    datos2.containsKey("otvalor04"),
                    datos2.containsKey("otvalor05"),
                    datos2.containsKey("otvalor06"),
                    datos2.containsKey("otvalor07"),
                    datos2.containsKey("otvalor08"),
                    datos2.containsKey("otvalor09"),
                    datos2.containsKey("otvalor10"),
                    datos2.containsKey("otvalor11"),
                    datos2.containsKey("otvalor12"),
                    datos2.containsKey("otvalor13"),
                    datos2.containsKey("otvalor14"),
                    datos2.containsKey("otvalor15"),
                    datos2.containsKey("otvalor16"),
                    datos2.containsKey("otvalor17"),
                    datos2.containsKey("otvalor18"),
                    datos2.containsKey("otvalor19"),
                    datos2.containsKey("otvalor20"),
                    datos2.containsKey("otvalor21"),
                    datos2.containsKey("otvalor22"),
                    datos2.containsKey("otvalor23"),
                    datos2.containsKey("otvalor24"),
                    datos2.containsKey("otvalor25"),
                    datos2.containsKey("otvalor26"),
                    datos2.containsKey("otvalor27"),
                    datos2.containsKey("otvalor28"),
                    datos2.containsKey("otvalor29"),
                    datos2.containsKey("otvalor30"),
                    datos2.containsKey("otvalor31"),
                    datos2.containsKey("otvalor32"),
                    datos2.containsKey("otvalor33"),
                    datos2.containsKey("otvalor34"),
                    datos2.containsKey("otvalor35"),
                    datos2.containsKey("otvalor36"),
                    datos2.containsKey("otvalor37"),
                    datos2.containsKey("otvalor38"),
                    datos2.containsKey("otvalor39"),
                    datos2.containsKey("otvalor40"),
                    datos2.containsKey("otvalor41"),
                    datos2.containsKey("otvalor42"),
                    datos2.containsKey("otvalor43"),
                    datos2.containsKey("otvalor44"),
                    datos2.containsKey("otvalor45"),
                    datos2.containsKey("otvalor46"),
                    datos2.containsKey("otvalor47"),
                    datos2.containsKey("otvalor48"),
                    datos2.containsKey("otvalor49"),
                    datos2.containsKey("otvalor50"),
                    datos2.containsKey("swimpres"),
                    datos2.containsKey("cdtipflu"),
                    datos2.containsKey("cdflujomc"),
                    datos2.containsKey("cdusuari"),
                    datos2.containsKey("cdtipsup"),
                    datos2.containsKey("swvispre"),
                    datos2.containsKey("cdpercli"),
                    datos2.containsKey("renuniext"),
                    datos2.containsKey("renramo"),
                    datos2.containsKey("renpoliex"),
                    datos2.containsKey("sworigenmesa"),
                    datos2.containsKey("cdrazrecha"),
                    datos2.containsKey("cdunidspch"),
	                datos2.get("cdunieco"),
	                datos2.get("cdramo"),
	                datos2.get("estado"),
	                datos2.get("nmpoliza"),
	                datos2.get("nmsuplem"),
	                datos2.get("nmsolici"),
	                datos2.get("cdsucadm"),
	                datos2.get("cdsucdoc"),
	                datos2.get("cdsubram"),
	                datos2.get("cdtiptra"),
	                datos2.get("ferecepc"),
	                datos2.get("cdagente"),
	                datos2.get("referencia"),
	                datos2.get("nombre"),
	                datos2.get("fecstatu"),
	                datos2.get("status"),
	                datos2.get("comments"),
	                datos2.get("cdtipsit"),
	                datos2.get("otvalor01"),
	                datos2.get("otvalor02"),
	                datos2.get("otvalor03"),
	                datos2.get("otvalor04"),
	                datos2.get("otvalor05"),
	                datos2.get("otvalor06"),
	                datos2.get("otvalor07"),
	                datos2.get("otvalor08"),
	                datos2.get("otvalor09"),
	                datos2.get("otvalor10"),
	                datos2.get("otvalor11"),
	                datos2.get("otvalor12"),
	                datos2.get("otvalor13"),
	                datos2.get("otvalor14"),
	                datos2.get("otvalor15"),
	                datos2.get("otvalor16"),
	                datos2.get("otvalor17"),
	                datos2.get("otvalor18"),
	                datos2.get("otvalor19"),
	                datos2.get("otvalor20"),
	                datos2.get("otvalor21"),
	                datos2.get("otvalor22"),
	                datos2.get("otvalor23"),
	                datos2.get("otvalor24"),
	                datos2.get("otvalor25"),
	                datos2.get("otvalor26"),
	                datos2.get("otvalor27"),
	                datos2.get("otvalor28"),
	                datos2.get("otvalor29"),
	                datos2.get("otvalor30"),
	                datos2.get("otvalor31"),
	                datos2.get("otvalor32"),
	                datos2.get("otvalor33"),
	                datos2.get("otvalor34"),
	                datos2.get("otvalor35"),
	                datos2.get("otvalor36"),
	                datos2.get("otvalor37"),
	                datos2.get("otvalor38"),
	                datos2.get("otvalor39"),
	                datos2.get("otvalor40"),
	                datos2.get("otvalor41"),
	                datos2.get("otvalor42"),
	                datos2.get("otvalor43"),
	                datos2.get("otvalor44"),
	                datos2.get("otvalor45"),
	                datos2.get("otvalor46"),
	                datos2.get("otvalor47"),
	                datos2.get("otvalor48"),
	                datos2.get("otvalor49"),
	                datos2.get("otvalor50"),
	                datos2.get("swimpres"),
	                datos2.get("cdtipflu"),
	                datos2.get("cdflujomc"),
	                datos2.get("cdusuari"),
	                datos2.get("cdtipsup"),
	                datos2.get("swvispre"),
	                datos2.get("cdpercli"),
	                datos2.get("renuniext"),
	                datos2.get("renramo"),
	                datos2.get("renpoliex"),
	                datos2.get("sworigenmesa"),
	                datos2.get("cdrazrecha"),
	                datos2.get("cdunidspch")
	                );
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
        logger.debug(Utils.log(
                "\n@@@@@@ guardarVentanaDatosTramite @@@@@@",
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public void guardarAuxiliarFlujo (String ntramite, String auxiliar) throws Exception {
	    logger.debug("{}", Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
	                                 "\n@@@@@@ guardarAuxiliarFlujo @@@@@@",
	                                 "\n@@@@@@ ntramite = ", ntramite, ", auxiliar = ", auxiliar));
	    String paso = "Guardando auxiliar";
	    logger.debug("{}", paso);
	    try {
	        mesaControlDAO.actualizarOtvalorTramitePorDsatribu(ntramite, "AUXILIAR%FLUJO", auxiliar, "U");
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
	    logger.debug("{}", Utils.log("\n@@@@@@ guardarAuxiliarFlujo @@@@@@",
	                                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	@Deprecated
	public List<Map<String,String>> recuperaTtipflumc(String agrupamc, String cdtipmod) throws Exception {
	    return flujoMesaControlDAO.recuperaTtipflumc(agrupamc, cdtipmod);
	}
    
    @Override
    public void pruebaGuardarObjeto () throws Exception {
        logger.debug("Invocando a pruebaGuardarObjeto() ...");
        String paso = null;
        try {
            paso = "Creando objeto";
            TvaloperVO obj1 = new TvaloperVO();
            obj1.setCdrol("1");
            obj1.setCdperson(1234);
            obj1.setOtvalor01("valor 01");
            obj1.setOtvalor02("valor 02");
            obj1.setOtvalor03("valor 03");
            obj1.setOtvalor04("valor 04");
            obj1.setOtvalor05("valor 05");
            obj1.setOtvalor99("valor 99");
            obj1.setStatus("A");
            obj1.setSwrecupera("S");
            paso = "Enviando objeto";
            flujoMesaControlDAO.pruebaObjetoTvaloper(obj1);
            /*
            paso = "Creando objeto";
            FlujoMesaControlDAOImpl.AlvaroObj obj1 = new FlujoMesaControlDAOImpl.AlvaroObj();
            obj1.setCdunieco(1000);
            obj1.setCdramo(2);
            obj1.setEstado("W");
            obj1.setNmpoliza(123456);
            
            paso = "Enviando objeto";
            flujoMesaControlDAO.pruebaGuardarObjeto(obj1);
            */
        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso);
        }
        logger.debug("Fin pruebaGuardarObjeto().");
    }
	
	@Override
	public void pruebaGuardarLista () throws Exception {
	    String paso = null;
	    try {
	        paso = "Creando lista";
	        List<FlujoMesaControlDAOImpl.AlvaroObj> lista = new ArrayList<FlujoMesaControlDAOImpl.AlvaroObj>();
	        FlujoMesaControlDAOImpl.AlvaroObj obj1 = new FlujoMesaControlDAOImpl.AlvaroObj();
            obj1.setCdunieco(1001);
            obj1.setCdramo(2);
            obj1.setEstado("W");
            obj1.setNmpoliza(123);
            lista.add(obj1);
            
            FlujoMesaControlDAOImpl.AlvaroObj obj2 = new FlujoMesaControlDAOImpl.AlvaroObj();
            obj2.setCdunieco(1002);
            obj2.setCdramo(4);
            obj2.setEstado("M");
            obj2.setNmpoliza(456);
            lista.add(obj2);
            
            FlujoMesaControlDAOImpl.AlvaroObj obj3 = new FlujoMesaControlDAOImpl.AlvaroObj();
            obj3.setCdunieco(1003);
            obj3.setCdramo(7);
            obj3.setEstado("W");
            obj3.setNmpoliza(789);
            lista.add(obj3);
            
            paso = "Enviando lista";
            flujoMesaControlDAO.pruebaGuardarLista(lista);
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
	}
	
	@Override
	public void cambiarTipoEndosoTramite (String ntramite, String status, String cdtipsup, String comments, boolean swagente,
            String cdusuari, String cdsisrol) throws Exception {
	    logger.debug("{}", Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
	                                 "\n@@@@@@ cambiarTipoEndosoTramite @@@@@@",
	                                 "\n@@@@@@ ntramite = " , ntramite,
                                     "\n@@@@@@ status   = " , status,
	                                 "\n@@@@@@ cdtipsup = " , cdtipsup,
	                                 "\n@@@@@@ comments = " , comments,
                                     "\n@@@@@@ swagente = " , swagente,
	                                 "\n@@@@@@ cdusuari = " , cdusuari,
	                                 "\n@@@@@@ cdsisrol = " , cdsisrol));
	    String paso = null;
	    try {
	        paso = "Recuperando descripci\u00f3n de endoso";
	        logger.debug(paso);
	        String dstipsup = consultasDAO.recuperarDstipsupPorCdtipsup(cdtipsup);
	        
	        paso = "Actualizando motivo de endoso";
	        logger.debug(paso);
	        flujoMesaControlDAO.cambiarTipoEndosoTramite(ntramite, cdtipsup);
	        
	        paso = "Registrando detalle";
	        logger.debug(paso);
	        mesaControlDAO.movimientoDetalleTramite(
	                ntramite,
	                new Date(), //feinicio
	                null, //cdclausu
	                Utils.log("Se cambia el motivo de endoso a \"", dstipsup, "\" con las siguientes observaciones: ",
	                        Utils.NVL(comments, "(sin comentarios)")),
	                cdusuari,
	                null, //cdmotivo
	                cdsisrol,
	                swagente ? "S" : "N",
	                null, //cdusuariDest
	                null, //cdsisrolDest
	                status,
	                false
	                );
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
        logger.debug("{}", Utils.log("\n@@@@@@ cambiarTipoEndosoTramite @@@@@@",
                                     "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
	}
	
	@Override
	public FlujoVO generarYRecuperarFlujoRSTN (String ntramite, String cdusuari, String cdsisrol) throws Exception {
	    logger.debug(Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
	                           "\n@@@@@@ generarYRecuperarFlujoRSTN @@@@@@",
	                           "\n@@@@@@ ntramite = ", ntramite,
	                           "\n@@@@@@ cdusuari = ", cdusuari,
	                           "\n@@@@@@ cdsisrol = ", cdsisrol));
	    FlujoVO flujo = null;
	    String paso = "Construyendo flujo RSTN";
	    try {
	        flujo = flujoMesaControlDAO.generarYRecuperarFlujoRSTN(ntramite, cdusuari, cdsisrol);
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
        logger.debug(Utils.log("\n@@@@@@ flujo = ", flujo,
                               "\n@@@@@@ generarYRecuperarFlujoRSTN @@@@@@",
                               "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
        return flujo;
	}
	
	@Deprecated
	@Override
	public Map<String, String> recuperaTflujomc (String cdflujomc) throws Exception {
	    return flujoMesaControlDAO.recuperaTflujomc(cdflujomc);
	}
	
	@Override
	public String obtenerSuplementoTramite(String ntramite) throws Exception{
	    String paso = "";
	    String suplemento = null;
	    try{
	        suplemento = flujoMesaControlDAO.obtenerSuplementoTramite(ntramite);
	    }
	    catch(Exception ex){
	        Utils.generaExcepcion(ex, paso);
	    }
	    return suplemento;
	}
}