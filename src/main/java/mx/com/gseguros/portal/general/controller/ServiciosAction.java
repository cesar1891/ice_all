package mx.com.gseguros.portal.general.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.consultas.service.RecuperacionSimpleManager;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.general.service.ServiciosManager;
import mx.com.gseguros.portal.general.util.TipoRamo;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
@Namespace("/servicios")
public class ServiciosAction extends PrincipalCoreAction
{
	
	private static final long serialVersionUID = 7996363816495572103L;
	private static Logger     logger           = Logger.getLogger(ServiciosAction.class);
	
	private Map<String,String> params;
	private boolean            success;
	private String             respuesta;
	
	@Autowired
	private ServiciosManager serviciosManager;

	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	@Autowired
	private RecuperacionSimpleManager recuperacionSimpleManager;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private DespachadorManager despachadorManager;

	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	@Action(value   = "reemplazarDocumentoCotizacion",
		    results = {
		        @Result(name="input"   , location="/jsp-script/servicios/input.jsp"),
		        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
		    })
	public String reemplazarDocumentoCotizacion()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.join(
				 "\n###########################################"
				,"\n###### reemplazarDocumentoCotizacion ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validate(sb, params , "No se recibieron datos");
			
			String cdunieco  = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza");
			
			Utils.validate(sb
					,cdunieco , "No se recibio la sucursal"
					,cdramo   , "No se recibio el producto"
					,estado   , "No se recibio el estado de poliza"
					,nmpoliza , "No se recibio el numero de poliza");
			
			respuesta = serviciosManager.reemplazarDocumentoCotizacion(sb,cdunieco, cdramo, estado, nmpoliza);
			
			logger.debug(Utils.log(
					 "\n###########################################"
					,"\n@@@*** reemplazarDocumentoCotizacion ***@@@"
					,"\n###########################################"
					));
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "grabarEvento",
		    results = {
		        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
		    })
	public String grabarEvento()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.join(
				 "\n##########################"
				,"\n###### grabarEvento ######"
				,"\n###### params=" , params
				));
		try
		{
			Utils.validate(sb,params,"No se recibieron datos");
			
			String cdmodulo    = params.get("cdmodulo");
			String cdevento    = params.get("cdevento");
			String ntramite    = params.get("ntramite");
			String cdunieco    = params.get("cdunieco");
			String cdramo      = params.get("cdramo");
			String estado      = params.get("estado");
			String nmpoliza    = params.get("nmpoliza");
			String nmsolici    = params.get("nmsolici");
			String cdagente    = params.get("cdagente");
			String cdusuariDes = params.get("cdusuariDes");
			String cdsisrolDes = params.get("cdsisrolDes");
			
			Utils.validate(sb
					,cdmodulo , "No se recibio el modulo"
					,cdevento , "No se recibio el evento"
					);
			
			String cdusuari = null;
			String cdsisrol = null;
			if(session!=null && session.get("USUARIO")!=null)
			{
				try
				{
					UserVO user = (UserVO)session.get("USUARIO");
					cdusuari = user.getUser();
					cdsisrol = user.getRolActivo().getClave();
				}
				catch(Exception ex)
				{
					sb.append("\nException al obtener usuario, no afecta el flujo");
				}
			}
			
			Date fecha = new Date();
			
			serviciosManager.grabarEvento(
					sb
					,cdmodulo
					,cdevento
					,fecha
					,cdusuari
					,cdsisrol
					,ntramite
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsolici
					,cdagente
					,cdusuariDes
					,cdsisrolDes
					);
			
			logger.debug(Utils.log(
					 "\n##########################"
					,"\n@@@*** grabarEvento ***@@@"
					,"\n@@@*** cdmodulo=" , cdmodulo , ", cdevento=" , cdevento , ", fecha="    , fecha
					,"\n@@@*** cdusuari=" , cdusuari , ", cdsisrol=" , cdsisrol , ", ntramite=" , ntramite
					,"\n@@@*** cdunieco=" , cdunieco , ", cdramo="   , cdramo   , ", estado="   , estado
					,"\n@@@*** nmpoliza=" , nmpoliza , ", nmsolici=" , nmsolici , ", cdagente=" , cdagente
					,"\n##########################"
					));
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		return SUCCESS;
	}
	
	@Action(value   = "recibosSubsecuentes",
		    results = {
	        @Result(name="input"   , location="/jsp-script/servicios/input.jsp"),
	        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
	    })
	public String recibosSubsecuentes()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### recibosSubsecuentes ######"
				));
		
		try
		{
			serviciosManager.recibosSubsecuentes(rutaDocumentosTemporal,false);
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recibosSubsecuentes ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "recibosSubsecuentesTest",
		    results = {
	        @Result(name="input"   , location="/jsp-script/servicios/input.jsp"),
	        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
	    })
	public String recibosSubsecuentesTest()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### recibosSubsecuentesTest ######"
				));
		
		try
		{
			serviciosManager.recibosSubsecuentes(rutaDocumentosTemporal,true);
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recibosSubsecuentesTest ######"
				,"\n#####################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "registrarTramiteRenovacion",
		    results = {
	        @Result(name="input"   , location="/jsp-script/servicios/input.jsp"),
	        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
	    })
	public String registrarTramiteRenovacion()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### registrarTramiteRenovacion ######"
				,"\n###### params=" , params
				));
		
		try
		{
			Utils.validate(params , "No se recibieron datos");
			
			String cdtiptra     = "21" //RENOVACION
					,cdtipsup   = "1"  //emision
					,nmpoliza   = "0"
					,referencia = "1"
					,nombre     = null
					,status     = "43" // SOLICITUD DE RENOVACION
					,estado     = "W"
					//recuperados de BD:
					,cdtipflu   = null //recuperar
					,cdflujomc  = null //recuperar
					,cdsucadm   = null //recuperar del agente
					,cdsucdoc   = null //recuperar del agente
					//recibidos:
					,cdagente   = params.get("cdagente")
					,cduniext   = params.get("sucursal")
					,ramo       = params.get("ramo")
					,nmpoliex   = params.get("poliza")
					,cdramo     = params.get("cdramo")
					,cdtipsit   = params.get("cdtipsit")
					,tipoflot   = params.get("tipoflot")
					,cdusuari   = params.get("cdusuari")
					,cdsisrol   = params.get("cdsisrol")
					,commentsIn = params.get("comments")
					,comments   = Utils.join(
							"Registrado desde m\u00f3dulo de renovaci\u00f3n autom\u00e1tica: ",
							StringUtils.isBlank(commentsIn)
								? "(sin observaciones)"
								: commentsIn
					);
			
			Date fechaRecep    = new Date()
			     ,fechaEstatus = new Date();
			
			Utils.validate(
					cdagente  , "No se recibi\u00f3 el agente"
					,cduniext , "No se recibi\u00f3 la sucursal"
					,ramo     , "No se recibi\u00f3 el ramo"
					,nmpoliex , "No se recibi\u00f3 la p\u00f3liza"
					,cdramo   , "No se recibi\u00f3 el cdramo"
					,cdtipsit , "No se recibi\u00f3 el cdtipsit"
					,tipoflot , "No se recibi\u00f3 el tipo de flotilla"
					,cdusuari , "No se recibi\u00f3 el usuario"
					,cdsisrol , "No se recibi\u00f3 el rol"
					);
			
			if("IPF".indexOf(tipoflot) == -1)
			{
				throw new ApplicationException("El tipo de flotilla debe ser I, P, o F");
			}
			
			String cadena = "INDIVIDUAL";
			
			if("P".equals(tipoflot))
			{
				cadena = "PYME";
			}
			else if("F".equals(tipoflot))
			{
				cadena = "FLOTILLA";
			}
			
			Map<String,String> paramsFlujo = new LinkedHashMap<String,String>();
			paramsFlujo.put("descripcion" , Utils.join("RENOVACI%N%AUTO%",cadena));
			
			Map<String,String> flujo = recuperacionSimpleManager.recuperarMapa(
					cdusuari
					,cdsisrol
					,RecuperacionSimple.RECUPERAR_FLUJO_POR_DESCRIPCION
					,paramsFlujo
					,null //usuario
					);
			
			cdtipflu  = flujo.get("CDTIPFLU");
			cdflujomc = flujo.get("CDFLUJOMC");
			
			logger.debug(Utils.log(
					"cdtipflu recuperado = "     , cdtipflu
					,", cdflujomc recuperado = " , cdflujomc
					));
			
			cdsucadm = cotizacionManager.cargarCduniecoAgenteAuto(cdagente, TipoRamo.AUTOS.getCdtipram());
			
			logger.debug(Utils.log("sucursal recuperada para el agente '",cdagente,"' = ",cdsucadm));
			
			Utils.validate(cdsucadm , "No se pudo recuperar la sucursal del agente");
			
			cdsucdoc = cdsucadm;
			
			String ntramite = flujoMesaControlManager.registrarTramite(
					cdsucdoc
					,cdramo
					,estado
					,nmpoliza
					,null //nmsuplem
					,cdsucadm
					,cdsucdoc
					,cdtiptra
					,fechaRecep
					,cdagente
					,referencia
					,nombre
					,fechaEstatus
					,status
					,comments
					,null //nmsolici
					,cdtipsit
					,cdusuari
					,cdsisrol
					,null //swimpres
					,cdtipflu
					,cdflujomc
					,null
					,cdtipsup
					,cduniext
					,ramo
					,nmpoliex
					,false
					,true
					);
			
			respuesta = Utils.join("OK,",ntramite);
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### respuesta = " , respuesta
				,"\n###### registrarTramiteRenovacion ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "generarFlagsTramites",
		    results = {
		        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
		    })
	public String generarFlagsTramites () {
		logger.debug(Utils.join(
			"\n##################################",
			"\n###### generarFlagsTramites ######"
		));
		try
		{
			respuesta = Utils.join("Flags generadas: ",serviciosManager.generarFlagsTramites());
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
			"\n###### respuesta = ", respuesta,
			"\n###### generarFlagsTramites ######",
			"\n##################################"
		));
		return SUCCESS;
	}
	
	@Action(value   = "actualizarFlagsTramites",
		    results = {
		        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
		    })
	public String actualizarFlagsTramites () {
		logger.debug(Utils.join(
			"\n#####################################",
			"\n###### actualizarFlagsTramites ######"
		));
		try
		{
			respuesta = Utils.join("Flags actualizadas: ",serviciosManager.actualizarFlagsTramites());
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
			"\n###### respuesta = ", respuesta,
			"\n###### actualizarFlagsTramites ######",
			"\n#####################################"
		));
		return SUCCESS;
	}
	
	@Action(value   = "procesarFlagsTramites",
		    results = {
		        @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
		    })
	public String procesarFlagsTramites () {
		logger.debug(Utils.join(
			"\n###################################",
			"\n###### procesarFlagsTramites ######"
		));
		
		/*
		 * bandera que nos dice si esta peticion bloqueo el proceso de FLAGS
		 */
		boolean seBloqueaFLAGS = false;
		
		try
		{
			/*
			 * Si no da exception, es porque se bloquea correctamente, marcamos
			 * la bandera como true porque esta peticion hizo el bloqueo.
			 * Si da exception es porque ya estaba bloqueado anteriormente y no llega al = true
			 */
			serviciosManager.bloquearProceso(Constantes.PROCESO_FLAGS, true, Constantes.USUARIO_SISTEMA, Constantes.ROL_SISTEMA);
			seBloqueaFLAGS = true;
			
			respuesta = Utils.join(
					"Flags generadas: "    , serviciosManager.generarFlagsTramites(),
					", flags actualizadas: " , serviciosManager.actualizarFlagsTramites()
					);
		} catch(Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		} finally {
			/*
			 * Solo si esta peticion hizo el bloqueo del proceso, mandamos
			 * desbloquear al final
			 */
			if (seBloqueaFLAGS) {
				try {
					serviciosManager.bloquearProceso(Constantes.PROCESO_FLAGS, false, Constantes.USUARIO_SISTEMA, Constantes.ROL_SISTEMA);
				} catch (Exception ex) {
					logger.debug("Error al desbloquear proceso FLAGS", ex);
				}
			}
		}
		logger.debug(Utils.log(
			"\n###### respuesta = ", respuesta,
			"\n###### procesarFlagsTramites ######",
			"\n###################################"
		));
		return SUCCESS;
	}
	
	@Action(value   = "enviarDocRstn",
            results = {
                @Result(name="success" , location="/jsp-script/servicios/respuesta.jsp")
            })
	public String enviarDocRstn () {
	    logger.debug(Utils.log("\n###########################",
	                           "\n###### enviarDocRstn ######",
	                           "\n###### params = ", params));
	    try {
	        HttpUtil.enviarArchivoRSTN(
	                HttpUtil.RSTN_DEFAULT_PATH + params.get("caseIdRstn"),
	                params.get("fullPath"),
	                params.get("fileDesc"),
	                "Cotizacion".equals(params.get("docClass"))
	                    ? HttpUtil.RSTN_DOC_CLASS_COTIZACION
	                    : HttpUtil.RSTN_DOC_CLASS_EMISION);
	    } catch (Exception ex) {
	        respuesta = Utils.manejaExcepcion(ex);
	    }
        logger.debug(Utils.log("\n###### respuesta = ", respuesta,
                               "\n###### enviarDocRstn ######",
                               "\n###########################"));
	    return SUCCESS;
	}
	
	/*
	 * Getters y setters
	 */
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public void setServiciosManager(ServiciosManager serviciosManager) {
		this.serviciosManager = serviciosManager;
	}
	
	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}

}