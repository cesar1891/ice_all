package mx.com.gseguros.portal.catalogos.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/persona")
public class PersonaAction extends PrincipalCoreAction
{

	private static final long serialVersionUID = 2211193078664526450L;

	private static final Logger logger = LoggerFactory.getLogger(PersonaAction.class);
	
	private boolean                  success;
	private String                   message;
	private Map<String,String>       params;
	private List<Map<String,String>> list;
	private Map<String,Item>         items;
	private List<GenericVO> 		genericVO;
	@Autowired
	private PersonasManager personasManager;
	
	@Actions({
			@Action(value   = "pantallaEspPersona",
		            results = {
		            @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		            @Result(name="success" , location="/jsp-script/catalogos/pantallaEspPersona.jsp")
		        }),
		    @Action(value   = "includes/pantallaEspPersona",
				    results = {
			        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
			        @Result(name="success" , location="/jsp-script/catalogos/pantallaEspPersona.jsp")
			})
	})
	public String pantallaPersona()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### pantallaPersona ######"
				,"\n###### params=",params
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params                 , "No se recibieron datos");
			Utils.validate(
					params.get("cdperson") , "No se recibi\u00F3 la clave de persona"
					,params.get("origen")  , "No se recibi\u00F3 la clave de origen"
					);
			
			items = personasManager.pantallaPersona(
					params.get("origen")
					,usuario.getRolActivo().getClave()
					,ServletActionContext.getServletContext().getServletContextName()
					);
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=",result
				,"\n###### pantallaPersona ######"
				,"\n#############################"
				));
		return result;
	}
	
	@Action(value   = "guardarPantallaEspPersona",
			results = { @Result(name="success", type="json") }
			)
	public String guardarPantallaEspPersona()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### guardarPantallaEspPersona ######"
				,"\n###### params=",params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String[] keys =  new String[]{
					"cdperson"
					,"cdtipide"
					,"cdideper"
					,"dsnombre"
					,"cdtipper"
					,"otfisjur"
					,"otsexo"
					,"fenacimi"
					,"cdrfc"
					,"dsemail"
					,"dsnombre1"
					,"dsapellido"
					,"dsapellido1"
					,"cdnacion"
					,"dscomnom"
					,"dsrazsoc"
					,"feingreso"
					,"feactual"
					,"dsnomusu"
					,"cdestciv"
					,"cdgrueco"
					,"cdstippe"
					,"nmnumnom"
					,"curp"
					,"canaling"
					,"conducto"
					,"ptcumupr"
					,"status"
					,"residencia"
					,"nongrata"
					,"cdideext"
					,"cdsucemi"
			};
			StringBuilder faltantes = new StringBuilder("Faltan los siguientes valores:<br/>");
			boolean       faltan    = false;
			for(String c:keys)
			{
				if(!params.containsKey(c))
				{
					faltantes.append(c).append("<br/>");
					faltan = true;
				}
			}
			if(faltan)
			{
				throw new ApplicationException(faltantes.toString());
			}
			
			message = personasManager.guardarPantallaEspPersona(params, usuario);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardarPantallaEspPersona ######"
				,"\n#######################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarEspPersona",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarEspPersona()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### recuperarEspPersona ######"
				,"\n###### params=",params
				));
		try
		{
			Utils.validateSession(session);
			Utils.validate(params                 , "No se recibieron datos");
			Utils.validate(params.get("cdperson") , "No se recibi\u00F3 la clave de persona");
			
			params.putAll(personasManager.recuperarEspPersona(params.get("cdperson")));
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### params=",params
				,"\n###### recuperarEspPersona ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "guardarConfiguracionClientes",
		results = { @Result(name="success", type="json") }
	)
	public String guardarConfiguracionClientes()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### guardaClienteNonGratos ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			Utils.validate(params                 , "No se recibieron datos");
			String cduser = ((UserVO)session.get("USUARIO")).getUser();
			Date   fechaProcesamiento = new Date();
			//message = personasManager.guardarClienteNonGratos(paramsCliente, params.get("cdrfc"));
			message = personasManager.guardarConfiguracionClientes(params.get("cdrfc"),params.get("status"),params.get("cdtipper"),params.get("agente"),
					params.get("dsnombre"),params.get("dsdomicil"), params.get("obsermot"),params.get("proceso"), cduser, fechaProcesamiento, params.get("accion"));
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardaClienteNonGratos ######"
				,"\n#######################################"
				));
		return SUCCESS;
	}
	
	
	@Action(value   = "obtieneInformacionCliente",
		results = { @Result(name="success", type="json") }
	)
	public String obtieneInformacionCliente()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### obtieneInformacionCliente ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			Utils.validate(params, "No se recibieron datos");
			
			message = personasManager.obtieneInformacionCliente(params.get("sucursal"),params.get("ramo"),params.get("poliza"));
			String respuesta[] = message.split("\\|");
			
			if(respuesta[0].toString().equalsIgnoreCase("1")){
				Formatter fmt = new Formatter();
				String claveExterna = "D"+String.format("%04d",Integer.parseInt(params.get("sucursal")))+""+String.format("%010d",Integer.parseInt(respuesta[1].toString()));
				List<Map<String, String>> loadList = new ArrayList<Map<String,String>>();
				HashMap<String,String>map=new HashMap<String,String>();
				map.put("CVECLIENSIGS",respuesta[1].toString());
				map.put("SUCEMISORA",respuesta[2].toString());
				map.put("TIPERSONA",respuesta[3].toString());
				map.put("NOMCLIENTE",respuesta[4].toString());
				map.put("APPATERNO",respuesta[5].toString());
				map.put("APMATERNO",respuesta[6].toString());
				map.put("RAZONSOCIAL",respuesta[7].toString());
				map.put("ANECLI",respuesta[8].toString());
				map.put("RFCCLIENTE",respuesta[9].toString());
				map.put("CVEIFE",respuesta[10].toString());
				map.put("CURP",respuesta[11].toString());
				map.put("SEXO",respuesta[12].toString());
				map.put("CVEEDOCIVIL",respuesta[13].toString());
				map.put("CALLECLIENTE",respuesta[14].toString());
				map.put("NUMCLIENTE",respuesta[15].toString());
				map.put("CPCLIENTE",respuesta[16].toString());
				map.put("COLCLIENTE",respuesta[17].toString());
				map.put("CVEMUNSIGS",String.format("%03d",Integer.parseInt(respuesta[18].toString())));
				map.put("POBLACION",respuesta[19].toString());
				map.put("CVEEDOSIGS",respuesta[20].toString());
				map.put("FECNACIMIENTO",respuesta[21].toString());
				map.put("NACEXT",respuesta[22].toString());
				map.put("CVEOCUPACION",respuesta[23].toString());
				map.put("CVEGIRO",respuesta[24].toString());
				map.put("TELEFONO1",respuesta[25].toString());
				map.put("TELEFONO2",respuesta[26].toString());
				map.put("TELEFONO3",respuesta[27].toString());
				map.put("EMAIL",respuesta[28].toString());
				map.put("PAGINAWEB",respuesta[29].toString());
				map.put("CANCON",respuesta[30].toString());
				map.put("FUENTEINGRESO",respuesta[31].toString());
				map.put("TIPADMINISTRACION",respuesta[32].toString());
				map.put("CARGOPUBL",respuesta[33].toString());
				map.put("NOMCAR",respuesta[34].toString());
				map.put("PERCAR",respuesta[35].toString());
				map.put("APOCLI",respuesta[36].toString());
				map.put("DOMORI",respuesta[37].toString());
				map.put("NUMPAS",respuesta[38].toString());
				map.put("CVEUSERCAPTURA",respuesta[39].toString());
				map.put("CVEUSERAUTORIZA",respuesta[40].toString());
				map.put("FECALTA",respuesta[41].toString());
				map.put("FECHACTUALIZACION",respuesta[42].toString());
				map.put("ESTATUCLIENTE",respuesta[43].toString());
				map.put("CVEEXTERNA", claveExterna);
				loadList.add(map);
			list =loadList; 	
			success = true;
			}else{
				message = respuesta[1].toString();
				success = false;
			}
			
			//obtieneInformacionCliente
			

			
			
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### guardaClienteNonGratos ######"
				,"\n#######################################"
				));
		return SUCCESS;
	}
	
	@Action(value = "obtieneListaClientesxTipo", results = { @Result(name = "success", type = "json", params = {
			"ignoreHierarchy", "false", "includeProperties",
			"list.*,success" }) })
		public String obtieneListaClientesxTipo()
		{
			logger.debug(Utils.log(
					 "\n###########################################"
					,"\n###### obtieneListaClientesNonGratos ######"
					,"\n###### params=",params
					));
			
			try
			{
				//Utils.validateSession(session);
				//Utils.validate(params                 , "No se recibieron datos");
				list = personasManager.obtieneListaClientesxTipo(params.get("cdrfc"), params.get("proceso"));
				success = true;
			}
			catch(Exception ex)
			{
				message = Utils.manejaExcepcion(ex);
			}
			
			logger.debug(Utils.log(
					 "\n###### obtieneListaClientesNonGratos ######"
					,"\n###########################################"
					));
			return SUCCESS;
		}
	
	@Action(value = "consultaClientes", results = { @Result(name = "success", type = "json", params = {
			"ignoreHierarchy", "false", "includeProperties",
			"genericVO.*,success" }) })
		public String consultaClientes()
		{
			logger.debug(Utils.log(
					 "\n##############################"
					,"\n###### consultaClientes ######"
					,"\n###### params=",params
					));
			
			try
			{
				genericVO = personasManager.consultaClientes(params.get("cdperson"));
				success = true;
			}
			catch(Exception ex)
			{
				message = Utils.manejaExcepcion(ex);
			}
			
			logger.debug(Utils.log(
					 "\n###### consultaClientes ######"
					,"\n##############################"
					));
			return SUCCESS;
		}
	
	@Actions({
		@Action(value   = "administracionClientes",
	            results = {
	            @Result(name="success" , location="/jsp-script/proceso/siniestros/administracionClientes.jsp")
	        })
	})
	public String administracionClientes()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### administracionClientes ######"
				,"\n###### params=",params
				));
		
		String result = ERROR;
		
		try
		{
			/* Proceso Cliente
			 * 1.- Clientes Non Gratos
			 * 2.- Politicamente Expuesto
			 * 3.- VIP
			 */
			UserVO usuario = Utils.validateSession(session);
			result = SUCCESS;
			String proceso  = params.get("tipoProceso");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("procesoCliente",proceso);
			setParamsJson(params);
			logger.debug("Params : {}", params);
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=",result
				,"\n###### administracionClientes ######"
				,"\n####################################"
				));
		return result;
	}

	public void setParamsJson(HashMap<String, String> params) {
		this.params = params;
	}

	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params {}", e.getMessage(), e);
			return null;
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

	public List<GenericVO> getGenericVO() {
		return genericVO;
	}

	public void setGenericVO(List<GenericVO> genericVO) {
		this.genericVO = genericVO;
	}
	
}