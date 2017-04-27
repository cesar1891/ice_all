package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.controller.RecuperacionDatosAction;
import mx.com.gseguros.portal.general.model.Movimiento;
import mx.com.gseguros.portal.general.service.MovimientosManager;
import mx.com.gseguros.utils.Utils;

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

import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/movimientos")
public class MovimientosAction extends PrincipalCoreAction
{
	private final static Logger logger = LoggerFactory.getLogger(RecuperacionDatosAction.class);
	
	private boolean                  success;
	private String                   message;
	private Map<String,String>       params;
	private List<Map<String,String>> list;
	
	@Autowired
	private MovimientosManager movimientosManager;
	
	public MovimientosAction()
	{
		this.session = ActionContext.getContext().getSession();
	}
	
	@Action(value   = "ejecutar",
			results = { @Result(name="success", type="json") }
	)
	public String ejecutar()
	{
		logger.debug(Utils.log(
				 "\n######################"
				,"\n###### ejecutar ######"
				,"\n###### params=",params
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			String movimiento = params.get("movimiento");
			Utils.validate(movimiento, "No se recibi\u00F3 el movimiento");
			
			Movimiento mov = null;
			try
			{
				mov = Movimiento.valueOf(movimiento);
			}
			catch(Exception ex)
			{
				throw new ApplicationException("El movimiento no existe");
			}
			
			if("V".equals(mov.getTipo()))
			{
				movimientosManager.ejecutar(usuario,mov,params,list);
			}
			else if("M".equals(mov.getTipo()))
			{
				params.putAll(movimientosManager.ejecutarRecuperandoMapa(usuario,mov,params,list));
			}
			else if("L".equals(mov.getTipo()))
			{
				list = movimientosManager.ejecutarRecuperandoLista(usuario,mov,params,list);
			}
			else if("ML".equals(mov.getTipo()))
			{
				Map<String,Object> res = movimientosManager.ejecutarRecuperandoMapaLista(usuario,mov,params,list);
				if(res==null
						 ||!res.containsKey("mapa")
						 ||!res.containsKey("lista")
				)
				{
					throw new ApplicationException("El movimiento no retorna todos los par\u00E1metros");
				}
				params.putAll((Map<String,String>)res.get("mapa"));
				list = (List<Map<String,String>>) res.get("lista");
			}
			else
			{
				throw new ApplicationException("Tipo de movimiento mal definido");
			}
			
			success = true;
		}
		catch(Exception ex)
		{
			logger.error(Utils.join("Error al ejecutar movimiento {",params,"}"),ex);
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				 "\n###### ejecutar ######"
				,"\n######################"
				));
		return SUCCESS;
	}
	
	@Action(value           = "ejecutarSMD",
			results         = { @Result(name="success", type="json") },
			interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
		    }
	)
	public String ejecutarSMD()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### ejecutarSMD ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params, "No se recibieron datos");
			String movimiento = params.get("movimiento");
			Utils.validate(movimiento, "No se recibi\u00F3 el movimiento");
			
			Movimiento mov = null;
			try
			{
				mov = Movimiento.valueOf(movimiento);
			}
			catch(Exception ex)
			{
				throw new ApplicationException("El movimiento no existe");
			}
			
			if("V".equals(mov.getTipo()))
			{
				movimientosManager.ejecutar(usuario,mov,params,list);
			}
			else if("M".equals(mov.getTipo()))
			{
				params.putAll(movimientosManager.ejecutarRecuperandoMapa(usuario,mov,params,list));
			}
			else if("A".equals(mov.getTipo()))
			{
				params.putAll(movimientosManager.ejecutarRecuperandoMapaAltaFamilia(usuario,mov,params,list));
			}
			else if("L".equals(mov.getTipo()))
			{
				list = movimientosManager.ejecutarRecuperandoLista(usuario,mov,params,list);
			}
			else if("ML".equals(mov.getTipo()))
			{
				Map<String,Object> res = movimientosManager.ejecutarRecuperandoMapaLista(usuario,mov,params,list);
				if(res==null
						 ||!res.containsKey("mapa")
						 ||!res.containsKey("lista")
				)
				{
					throw new ApplicationException("El movimiento no retorna todos los par\u00E1metros");
				}
				params.putAll((Map<String,String>)res.get("mapa"));
				list = (List<Map<String,String>>) res.get("lista");
			}
			else
			{
				throw new ApplicationException("Tipo de movimiento mal definido");
			}
			
			success = true;
		}
		catch(Exception ex)
		{
			logger.error(Utils.join("Error al ejecutar movimiento {",params,"}"),ex);
			message = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
				 "\n###### ejecutarSMD ######"
				,"\n#########################"
				));
		return SUCCESS;
	}
	
	////////////////////////////////////////////////////////
	// GETTERS Y SETTERS ///////////////////////////////////
	public boolean isSuccess() {                          //
		return success;                                   //
	}                                                     //
                                                          //
	public void setSuccess(boolean success) {             //
		this.success = success;                           //
	}                                                     //
                                                          //
	public String getMessage() {                          //
		return message;                                   //
	}                                                     //
                                                          //
	public void setMessage(String message) {              //
		this.message = message;                           //
	}                                                     //
                                                          //
	public Map<String, String> getParams() {              //
		return params;                                    //
	}                                                     //
                                                          //
	public void setParams(Map<String, String> params) {   //
		this.params = params;                             //
	}                                                     //
                                                          //
	public List<Map<String, String>> getList() {          //
		return list;                                      //
	}                                                     //
                                                          //
	public void setList(List<Map<String, String>> list) { //
		this.list = list;                                 //
	}                                                     //
	////////////////////////////////////////////////////////
	
}