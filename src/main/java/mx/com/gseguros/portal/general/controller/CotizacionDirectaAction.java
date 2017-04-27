package mx.com.gseguros.portal.general.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.service.LoginManager;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.service.CotizacionAutoManager;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@ParentPackage(value="seguridad")
@Namespace("/cotizacionDirecta")
public class CotizacionDirectaAction extends PrincipalCoreAction {
	
	private static final long serialVersionUID = 7996363816495572103L;
	
	private static Logger logger = LoggerFactory.getLogger(CotizacionDirectaAction.class);
	
	private boolean success;
	
	private String respuesta;
	
	private Map<String,String> params;
	
	private List<Map<String,String>> list;
	
	private String detalleRespuesta;
	
	private static final String PORREDAU_DEFAULT = "0";
	
	private static final String PORPARTI_DEFAULT = "100";
	
	/**
	 * Lista que recibe los valores del grid de vehículos para flotillas
	 */
	private List<Map<String,String>> listaValoresSituac;
	
	/**
	 * Lista que recibe los valores de los formularios de configuracion por situacion (botones)
	 */
	private List<Map<String,String>> listaConfigSituac;
	
	private List<Map<String,String>> tarifa;
	
	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Autowired
	private CotizacionAutoManager cotizacionAutoManager;
	
	@Autowired
	private LoginManager loginManager;
	
	
	@Action(value="cotizarIndividual",
		results = {
			@Result(name="success", type="json")
	    },
	    interceptorRefs = {
	            @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
	    }
	)
	public String cotizarIndividual() throws Exception {
		
		logger.debug("Inicio de cotizarIndividual Directa params={}", params);
		
		try {
			// Se validan datos:
			Utils.validate(params, "No se recibieron datos para cotizar");
			Utils.validate(params.get("cdusuari"),    "No existe el par\u00E1metro params.cdusuari");
			Utils.validate(params.get("cdelemento"),  "No existe el par\u00E1metro params.cdelemento");
			Utils.validate(params.get("cdunieco"),    "No existe el par\u00E1metro params.cdunieco");
			Utils.validate(params.get("cdramo"),      "No existe el par\u00E1metro params.cdramo");
			Utils.validate(params.get("cdtipsit"),    "No existe el par\u00E1metro params.cdtipsit");
			Utils.validate(list, "No se recibieron datos de incisos (list)");
			
			String nmpoliza = list.get(0).get("nmpoliza");
			String feini    = list.get(0).get("feini");
			String fefin    = list.get(0).get("fefin");
			
			// Se llenan datos:
			boolean noTarificar = StringUtils.isNotBlank(params.get("notarificar")) && params.get("notarificar").equals("si");			
			boolean conIncisos = StringUtils.isNotBlank(params.get("conincisos")) && params.get("conincisos").equals("si");
			UserVO userVO = loginManager.obtenerDatosUsuario(params.get("cdusuari"));
			logger.debug("CDPERSON del user={}", userVO.getCodigoPersona());
			
			// Datos para TVALOPOL:
			Map<String, String> tvalopol = new HashMap<String, String>();
			for(Entry<String, String> en : list.get(0).entrySet()) {
				String key = en.getKey();
				if(key.length() > "aux.".length() && key.substring(0,"aux.".length()).equals("aux.")) {
					tvalopol.put(key.substring("aux.".length()), en.getValue());
				}
			}
			
			//si cdperson es nulo y cdideper no, Importa el cliente y le crea un cdperson y default nmorddom 1
			ManagerRespuestaSlistSmapVO resp = cotizacionManager.cotizar(
					params.get("cdunieco"),
					params.get("cdramo"), 
					params.get("cdtipsit"), 
					params.get("cdusuari"), 
					params.get("cdelemento"),
					nmpoliza, 
					feini, 
					fefin, 
					params.get("fesolici"),
					params.get("cdpersonCli"), 
					params.get("nmorddomCli"), 
					params.get("cdideperCli"),
					noTarificar, 
					conIncisos, 
					list, 
					params.containsKey("movil"),
					tvalopol, 
					params.get("cdagenteAux"), 
					userVO
					,""//"fromSigs"
					,""//"parame.get(\"RENUNIEXT\")"
					,""//"parame.get(\"RENRAMO\")"
					,""//"parame.get(\"RENPOLIEX\")"
					,""//"ntramite"
					);
			resp= cotizacionManager.cotizarContinuacion(params.get("cdusuari"),params.get("cdunieco"),params.get("cdramo"), params.get("cdelemento"),params.get("cdtipsit"),resp.getSmap().get("nmpoliza"),params.containsKey("movil"));
			
			// Se vacian los datos de entrada para no devolverlos de nuevo:
			params = new HashMap<String, String>();
			list   = new ArrayList<Map<String,String>>();
			
			respuesta = resp.getRespuesta();
			
			if(resp.isExito()) {
				params.putAll(resp.getSmap());
				tarifa = resp.getSlist();
			}
			
			success = true;
			
		} catch(Exception e) {
			respuesta = Utils.manejaExcepcion(e);
			detalleRespuesta = ExceptionUtils.getStackTrace(e);
		}
		
		logger.debug("Fin de cotizarIndividual Directa");
		
		return SUCCESS;
	}
	
	
	@Action(value="comprarCotizacion",
		results = {
			@Result(name="success", type="json")
	    }
	)
    public String comprarCotizacion() {
    	
		logger.debug("Inicio de comprarCotizacion Directa params={}", params);
    	
    	try {
        	// Se validan datos:
        	Utils.validate(params, "No hay par\u00E1metros");
        	Utils.validate(params.get("cdusuari"),      "No existe el par\u00E1metro params.cdusuari");
        	Utils.validate(params.get("cdsisrol"),      "No existe el par\u00E1metro params.cdsisrol");
        	Utils.validate(params.get("cdelemento"),    "No existe el par\u00E1metro params.cdelemento");
        	Utils.validate(params.get("cdunieco"),      "No existe el par\u00E1metro params.cdunieco");
        	Utils.validate(params.get("cdramo"),        "No existe el par\u00E1metro params.cdramo");
        	Utils.validate(params.get("cdtipsit"),      "No existe el par\u00E1metro params.cdtipsit");
        	Utils.validate(params.get("nmpoliza"),      "No existe el par\u00E1metro params.nmpoliza");
        	Utils.validate(params.get("cdciaaguradora"),"No existe el par\u00E1metro params.cdciaaguradora");
        	Utils.validate(params.get("cdplan"),        "No existe el par\u00E1metro params.cdplan");
        	Utils.validate(params.get("cdperpag"),      "No existe el par\u00E1metro params.cdperpag");
        	Utils.validate(params.get("fechaInicio"),   "No existe el par\u00E1metro params.fechaInicio");
        	Utils.validate(params.get("fechaFin"),      "No existe el par\u00E1metro params.fechaFin");
        	Utils.validate(params.get("cdagenteExt"),   "No existe el par\u00E1metro params.cdagenteExt");
        	Utils.validate(params.get("sucursal"),      "No exsite el par\u00E1metro params.sucursal");
        	Utils.validate(params.get("ramo"),          "No exsite el par\u00E1metro params.ramo");
        	Utils.validate(params.get("poliza"),        "No exsite el par\u00E1metro params.poliza");
        	
        	String swrenovacion = "S".equals(params.get("swrenovacion")) ? "S" : "N";
        	
        	//DBB
        	String porparti = StringUtils.isNotBlank(params.get("porparti")) ? params.get("porparti") : PORPARTI_DEFAULT;
        	String porredau = StringUtils.isNotBlank(params.get("porredau")) ? params.get("porredau") : PORREDAU_DEFAULT;
        	        	
        	// Se llenan datos:
    		boolean esFlotilla  = StringUtils.isNotBlank(params.get("flotilla"))&&params.get("flotilla").equalsIgnoreCase("si");
    		String tipoflot    = params.get("tipoflot");
    		UserVO userVO = loginManager.obtenerDatosUsuario(params.get("cdusuari"));
    		logger.debug("CDPERSON del user={}", userVO.getCodigoPersona());
    		
    		String ntramite = cotizacionManager.procesoComprarCotizacion(params.get("cdunieco"), params.get("cdramo"), params.get("nmpoliza"), 
    				params.get("cdtipsit"), params.get("fechaInicio"), params.get("fechaFin"), params.get("ntramite"), 
    				params.get("cdagenteExt"),
    				params.get("cdciaaguradora"), params.get("cdplan"), params.get("cdperpag"), 
    				params.get("cdusuari"), params.get("cdsisrol"), params.get("cdelemento"),
    				esFlotilla, tipoflot, params.get("cdpersonCli"), params.get("cdideperCli"),
    				getText("rdf.cotizacion.nombre."+params.get("cdtipsit")),
    				getText("rdf.cotizacion.flot.nombre."+params.get("cdtipsit")), userVO,
    				swrenovacion, params.get("sucursal"), params.get("ramo"), params.get("poliza"), 
    				porparti, porredau
    				);
    		
    		params.put("ntramite", ntramite);
    		
    		respuesta = "Proceso correcto no. tr\u00E1mite : "+ntramite+" no. p\u00F3liza : "+params.get("nmpoliza");
    		success   = true;
    	} catch(Exception e) {
    		respuesta = Utils.manejaExcepcion(e);
    		detalleRespuesta = ExceptionUtils.getStackTrace(e);
    	}
    	
        logger.debug("Fin de comprarCotizacion Directa");
    	
        return SUCCESS;
    }
	
	
	@Action(value="cotizarFlotillas",
			results = {
				@Result(name="success", type="json")
		    },
		    interceptorRefs = {
		            @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
		    }
		)
	public String cotizarFlotillas() throws Exception {
		
		logger.debug(Utils.log(
				 "\n##############################"
				,"\n###### cotizarFlotillas ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				,"\n###### listaValoresSituac=" , listaValoresSituac
				,"\n###### listaConfigSituac="  , listaConfigSituac
				));
		
		try
		{
			Utils.validate(params             , "No se recibieron par\u00E1metros");
			Utils.validate(list               , "No se recibieron los incisos mixtos");
			Utils.validate(listaValoresSituac , "No se recibieron los incisos base");
			Utils.validate(listaConfigSituac  , "No se recibieros las configuraciones de situaci\u00F3n");
			
			String cdusuari    = params.get("cdusuari");
			String cdsisrol    = params.get("cdsisrol");
			String cdelemen    = params.get("cdelemen");
			String cdunieco    = params.get("cdunieco");
			String cdramo      = params.get("cdramo");
			String cdtipsit    = params.get("cdtipsit");
			String estado      = params.get("estado");
			String nmpoliza    = params.get("nmpoliza");
			String feini       = params.get("feini");
			String fefin       = params.get("fefin");
			String cdagente    = params.get("cdagente");
			String cdpersonCli = params.get("cdpersonCli");
			String nmorddomCli = params.get("nmorddomCli");
			String cdideperCli = params.get("cdideperCli");
			String tipoflot    = params.get("tipoflot");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00F3 la sucursal"
					,cdramo   , "No se recibi\u00F3 el producto"
					,cdtipsit , "No se recibi\u00F3 la modalidad"
					,estado   , "No se recibi\u00F3 el estado"
					,feini    , "No se recibi\u00F3 el inicio de vigencia"
					,fefin    , "No se recibi\u00F3 el fin de vigencia"
					,cdagente , "No se recibi\u00F3 el agente"
					,tipoflot , "No se recibi\u00F3 el tipo de cotizaci\u00F3n"
					);
			
			// Se llenan datos:
			boolean noTarificar = StringUtils.isNotBlank(params.get("notarificar")) && params.get("notarificar").equals("si");
			UserVO userVO = loginManager.obtenerDatosUsuario(params.get("cdusuari"));
			logger.debug("CDPERSON del user={}", userVO.getCodigoPersona());
			
			Map<String,String>tvalopol=new HashMap<String,String>();
			for(Entry<String,String>en:params.entrySet()) {
				String key=en.getKey();
				if(key.length()>"tvalopol_".length() && key.substring(0,"tvalopol_".length()).equals("tvalopol_")) {
					tvalopol.put(Utils.join("otvalor",StringUtils.leftPad(key.substring("tvalopol_".length()),2,"0")),en.getValue());
				}
			}
			
			
			//si cdperson es nulo y cdideper no, Importa el cliente y le crea un cdperson y default nmorddom 1
			ManagerRespuestaSlistSmapVO resp = cotizacionAutoManager.cotizarAutosFlotilla(
					cdusuari
					,cdsisrol
					,cdelemen
					,cdunieco
					,cdramo
					,cdtipsit
					,estado
					,nmpoliza
					,feini
					,fefin
					,cdagente
					,cdpersonCli
					,nmorddomCli
					,cdideperCli
					,list
					,listaValoresSituac
					,listaConfigSituac
					,noTarificar
					,tipoflot
					,tvalopol
					,userVO
					,""//cduniext
					,""//renramo
					,""//nmpoliex
					,""//ntramite
					);
			
			// Se vacian los datos de entrada para no devolverlos de nuevo:
			params             = new HashMap<String, String>();
			list               = new ArrayList<Map<String,String>>();
			listaValoresSituac = new ArrayList<Map<String,String>>();
			listaConfigSituac  = new ArrayList<Map<String,String>>();
			
			success         = resp.isExito();
			respuesta       = resp.getRespuesta();
			if(success)
			{
				params.putAll(resp.getSmap());
				tarifa = resp.getSlist();
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### params=" , params
				,"\n###### list="   , list
				,"\n###### cotizarFlotillas ######"
				,"\n##############################"
				));
		return SUCCESS;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="agregarAsegurado",
        results = {
            @Result(name="success", type="json")
        }
    )
	public String agregarAsegurado() throws Exception {
	    
        try {
            
            logger.debug("Inicio agregarAsegurado...");
        
            // Se validan datos:
            Utils.validate(params, "No hay par\u00E1metros");
            Utils.validate(params.get("cdunieco"),   "No existe el par\u00E1metro params.cdunieco");
            Utils.validate(params.get("cdramo"),     "No existe el par\u00E1metro params.cdramo");
            Utils.validate(params.get("estado"),     "No existe el par\u00E1metro params.estado");
            Utils.validate(params.get("nmpoliza"),   "No existe el par\u00E1metro params.nmpoliza");
            Utils.validate(params.get("nmsuplem"),   "No existe el par\u00E1metro params.nmsuplem");
            Utils.validate(params.get("feefecto"),   "No existe el par\u00E1metro params.feefecto");
            Utils.validate(params.get("dsnombre"),   "No existe el par\u00E1metro params.dsnombre");
            Utils.validate(params.get("paterno"),    "No existe el par\u00E1metro params.paterno");
            Utils.validate(params.get("materno"),    "No existe el par\u00E1metro params.materno");
            Utils.validate(params.get("cdrfc"),      "No existe el par\u00E1metro params.cdrfc");
            Utils.validate(params.get("sexo"),       "No existe el par\u00E1metro params.sexo");
            Utils.validate(params.get("fenacimi"),   "No existe el par\u00E1metro params.fenacimi");
            Utils.validate(params.get("cdestciv"),   "No exsite el par\u00E1metro params.cdestciv");
            Utils.validate(params.get("dsocupacion"),"No existe el par\u00E1metro params.dsocupacion");
            Utils.validate(params.get("cdtipsit"),   "No exsite el par\u00E1metro params.cdtipsit");
            Utils.validate(params.get("cdplan"),     "No existe el par\u00E1metro params.cdplan");
            Utils.validate(params.get("nmorddom"),   "No existe el par\u00E1metro params.nmorddom");
            Utils.validate(params.get("cdagrupa"),   "No existe el par\u00E1metro params.cdagrupa");
            
            String dsnombre1 = StringUtils.isNotBlank(params.get("dsnombre1")) ? params.get("dsnombre1") : ""; 
            
            cotizacionManager.agregarAsegurado(params.get("cdunieco"), params.get("cdramo"), params.get("estado"),
                params.get("nmpoliza"), params.get("nmsuplem"), params.get("feefecto"), params.get("dsnombre"),
                dsnombre1, params.get("paterno"), params.get("materno"), params.get("cdrfc"),
                params.get("sexo"), params.get("fenacimi"), params.get("cdestciv"), params.get("dsocupacion"),
                params.get("cdtipsit"), params.get("cdplan"), params.get("nmorddom"), params.get("cdagrupa"), params.get("otvalor01"), params.get("otvalor02"),
                params.get("otvalor03"), params.get("otvalor04"), params.get("otvalor05"), params.get("otvalor06"),
                params.get("otvalor07"), params.get("otvalor08"), params.get("otvalor09"), params.get("otvalor10"));
            
            respuesta = "Asegurado agregado";
            success   = true;
        } catch(Exception e) {
            logger.error("Error:", e);
            respuesta = Utils.manejaExcepcion(e);
            detalleRespuesta = ExceptionUtils.getStackTrace(e);
        }
        
        logger.debug("Fin agregarAsegurado...");
        
	    return SUCCESS;
	}
	
	// Getters y setters

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

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public List<Map<String, String>> getTarifa() {
		return tarifa;
	}

	public void setTarifa(List<Map<String, String>> tarifa) {
		this.tarifa = tarifa;
	}

	public List<Map<String, String>> getListaValoresSituac() {
		return listaValoresSituac;
	}

	public void setListaValoresSituac(List<Map<String, String>> listaValoresSituac) {
		this.listaValoresSituac = listaValoresSituac;
	}

	public List<Map<String, String>> getListaConfigSituac() {
		return listaConfigSituac;
	}

	public void setListaConfigSituac(List<Map<String, String>> listaConfigSituac) {
		this.listaConfigSituac = listaConfigSituac;
	}
	
	public String getDetalleRespuesta() {
		return detalleRespuesta;
	}
	
	public void setDetalleRespuesta(String detalleRespuesta) {
		this.detalleRespuesta = detalleRespuesta;
	}
	
}