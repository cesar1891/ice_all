package mx.com.gseguros.portal.emision.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.convention.annotation.Action;
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
@ParentPackage(value="default")
@Namespace("/emision")
public class NuevoCotizacionAction extends PrincipalCoreAction
{
	private static Logger logger = LoggerFactory.getLogger(NuevoCotizacionAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String,String> params;
	
	private Map<String,Item> items;
	
	private List<Map<String,String>> list;

	@Autowired
	private CotizacionManager cotizacionManager;
	
	@Action(value   = "borrarIncisoCotizacion",
			results = { @Result(name="success", type="json") }
			)
	public String borrarIncisoCotizacion()
	{
		logger.debug(Utils.log(""
				,"\n####################################"
				,"\n###### borrarIncisoCotizacion ######"
				,"\n###### params=", params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String cdunieco  = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza")
			       ,nmsituac = params.get("nmsituac");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00f3 la sucursal"
					,cdramo   , "No se recibi\u00f3 el producto"
					,estado   , "No se recibi\u00f3 el estado"
					,nmpoliza , "No se recibi\u00f3 la p\u00f3liza"
					,nmsituac , "No se recibi\u00f3 la situaci\u00f3n"
					);
			
			cotizacionManager.borrarIncisoCotizacion(cdunieco,cdramo,estado,nmpoliza,nmsituac);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### borrarIncisoCotizacion ######"
				,"\n####################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "actualizarOtvalorTramitePorDsatribu",
			results = { @Result(name="success", type="json") }
			)
	public String actualizarOtvalorTramitePorDsatribu()
	{
		logger.debug(Utils.log(
				 "\n#################################################"
				,"\n###### actualizarOtvalorTramitePorDsatribu ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite  = params.get("ntramite")
			       ,dsatribu = params.get("dsatribu")
			       ,otvalor  = params.get("otvalor")
			       ,accion   = params.get("accion");
			
			Utils.validate(
					ntramite  , "Falta ntramite"
					,dsatribu , "Falta dsatribu"
					,accion   , "Falta accion"
					);
			
			if(!"I".equals(accion)
					&&!"U".equals(accion)
					&&!"D".equals(accion))
			{
				throw new ApplicationException("Acci\u00f3n no v\u00e1lida");
			}
			
			if("I".equals(accion)||"U".equals(accion))
			{
				Utils.validate(otvalor, "Falta otvalor");
			}
			
			cotizacionManager.actualizarOtvalorTramitePorDsatribu(
					ntramite
					,dsatribu
					,otvalor
					,accion
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=", success
				,"\n###### message=", message
				,"\n###### actualizarOtvalorTramitePorDsatribu ######"
				,"\n#################################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarOtvalorTramitePorDsatribu",
			results = { @Result(name="success", type="json") }
			)
	public String recuperarOtvalorTramitePorDsatribu()
	{
		logger.debug(Utils.log(
				 "\n################################################"
				,"\n###### recuperarOtvalorTramitePorDsatribu ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			
			String ntramite  = params.get("ntramite")
			       ,dsatribu = params.get("dsatribu");
			
			Utils.validate(
					ntramite  , "Falta ntramite"
					,dsatribu , "Falta dsatribu"
					);
			
			params.put(
					"otvalor"
					,cotizacionManager.recuperarOtvalorTramitePorDsatribu(
							ntramite
							,dsatribu
					)
			);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### params="  , params
				,"\n###### recuperarOtvalorTramitePorDsatribu ######"
				,"\n################################################"
				));
		return SUCCESS;
	}
	
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	////////////////  GETTERS Y SETTERS  ////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	
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

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
	
}