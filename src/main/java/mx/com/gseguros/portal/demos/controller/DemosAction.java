package mx.com.gseguros.portal.demos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
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
@Namespace("/demo")
public class DemosAction extends PrincipalCoreAction
{
	
	private static Logger logger = LoggerFactory.getLogger(DemosAction.class);
	
	private boolean success;
	
	private FlujoVO flujo;
	
	private Map<String,String> params;
	
	private Map<String,Item> items;
	
	private String message;
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	public DemosAction()
	{
		this.session=ActionContext.getContext().getSession();
	}
	
	@Action(value   = "template",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/proceso/demo/demoTemplate.jsp")
		    })
	public String template()
	{
		logger.debug(Utils.log(""
				,"\n######################"
				,"\n###### template ######"
				,"\n###### flujo="  , flujo
				));
		
		String result = ERROR
		       ,paso  = null;
		try
		{
			try
			{
				paso = "Validando datos";
				logger.debug(Utils.log("","paso=",paso));
				
				UserVO usuario = Utils.validateSession(session);
				
				String cdsisrol = usuario.getRolActivo().getClave();
				
				if(flujo==null)
				{
					throw new ApplicationException("No hay datos de flujo");
				}
				
				String pantalla = flujo.getAux();
				
				Utils.validate(
						pantalla , "No se recibi\u00f3 la pantalla (aux)"
						);
				
				paso = "Recuperando componentes";
				logger.debug(Utils.log("","paso=",paso));
				
				List<ComponenteVO> uno = pantallasDAO.obtenerComponentes(
						null//cdtiptra
						,null//cdunieco
						,null//cdramo
						,null//cdtipsit
						,null//estado
						,cdsisrol
						,Utils.join("DEMO_",pantalla)
						,"SECCION_A"
						,null//orden
						);
				
				List<ComponenteVO> dos = pantallasDAO.obtenerComponentes(
						null//cdtiptra
						,null//cdunieco
						,null//cdramo
						,null//cdtipsit
						,null//estado
						,cdsisrol
						,Utils.join("DEMO_",pantalla)
						,"SECCION_B"
						,null//orden
						);
				
				List<ComponenteVO> tres = pantallasDAO.obtenerComponentes(
						null//cdtiptra
						,null//cdunieco
						,null//cdramo
						,null//cdtipsit
						,null//estado
						,cdsisrol
						,Utils.join("DEMO_",pantalla)
						,"SECCION_C"
						,null//orden
						);
				
				paso = "Generando componentes";
				logger.debug(Utils.log("","paso=",paso));
				
				GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				
				items = new HashMap<String,Item>();
				
				params = new HashMap<String,String>();
				
				if(uno.size()>0)
				{
					int unoSize = uno.size();
					ComponenteVO ultimo = uno.remove(unoSize-1);
					params.put("uno" , ultimo.getLabel());
					
					gc.generaComponentes(uno, true, false, true, false, false, false);
					items.put("uno" , gc.getItems());
				}
				else
				{
					params.put("uno" , "");
				}
				
				if(dos.size()>0)
				{
					int dosSize = dos.size();
					ComponenteVO ultimo = dos.remove(dosSize-1);
					params.put("dos" , ultimo.getLabel());
					
					gc.generaComponentes(dos, true, false, true, false, false, false);
					items.put("dos" , gc.getItems());
				}
				else
				{
					params.put("dos" , "");
				}
				
				if(tres.size()>0)
				{
					int tresSize = tres.size();
					ComponenteVO ultimo = tres.remove(tresSize-1);
					params.put("tres" , ultimo.getLabel());
					
					gc.generaComponentes(tres, true, false, true, false, false, false);
					items.put("tres" , gc.getItems());
				}
				else
				{
					params.put("tres" , "");
				}
				
				result = SUCCESS;				
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}

		logger.debug(Utils.log(""
				,"\n###### result=" , result
				,"\n###### template ######"
				,"\n######################"
				));
		return result;
	}
	
	@Action(value   = "guardarDatosDemo",
			results = { @Result(name="success", type="json") }
			)
	public String guardarDatosDemo()
	{
		logger.debug(Utils.log(""
				,"\n##############################"
				,"\n###### guardarDatosDemo ######"
				,"\n###### params=", params
				));
		
		try
		{
			String paso = null;
			try
			{
				paso = "Verificando datos";
				logger.debug(Utils.log("", "paso=", paso));
				
				Utils.validate(params, "No se recibieron datos");
				
				String ntramite = params.get("ntramite");
				
				paso = "Guardando datos";
				logger.debug(Utils.log("", "paso=", paso));
				
				consultasDAO.guardarDatosDemo(ntramite,params);
				
				success = true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### success=", success
				,"\n###### message=", message
				,"\n###### guardarDatosDemo ######"
				,"\n##############################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "cargarDatosDemo",
			results = { @Result(name="success", type="json") }
			)
	public String cargarDatosDemo()
	{
		logger.debug(Utils.log(""
				,"\n#############################"
				,"\n###### cargarDatosDemo ######"
				,"\n###### params=", params
				));
		
		try
		{
			String paso = null;
			try
			{
				paso = "Verificando datos";
				logger.debug(Utils.log("", "paso=", paso));
				
				Utils.validate(params, "No se recibieron datos");
				
				String ntramite = params.get("ntramite");
				
				paso = "Recuperando datos";
				logger.debug(Utils.log("", "paso=", paso));
				
				params = consultasDAO.cargarDatosDemo(ntramite);
				
				success = true;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### success=", success
				,"\n###### message=", message
				,"\n###### cargarDatosDemo ######"
				,"\n#############################"
				));
		return SUCCESS;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public FlujoVO getFlujo() {
		return flujo;
	}

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}