package mx.com.gseguros.portal.general.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.service.ConveniosManager;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/convenios")
public class ConveniosAction extends PrincipalCoreAction{
	
	private Logger logger = LoggerFactory.getLogger(ConveniosAction.class);
	
	private boolean success;
	
	private String message;
	
	private Map<String,String> params;
	
	private Map<String,Item> items;

	private List<Map<String,String>> list;
	
	@Autowired
	private ConveniosManager conveniosManager;
	
	@Action(value   = "consultaConvenios",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/proceso/convenios/endosoConvenio.jsp")
		    })
	public String consultaConvenios()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### consultaConvenios ######"
				));
		
		String result = ERROR;
		
		try
		{
			items = conveniosManager.recuperarElementosPantalla();
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=" , result
				,"\n###### Consulta convenios ######"
				,"\n###########################"
				));
		return result;
	}
	
	@Action(value   = "guardarEnBase",
			results = { @Result(name="success", type="json") }
			)
	public String guardarEnBase()
	{		
		UserVO usuVO = null;
		try
		{
			Utils.validate(params,"No se recibieron datos");		
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String cdtipsit   = params.get("cdtipsit");
			String nmpoliza = params.get("nmpoliza");
			String diasgrac = params.get("diasgrac");
			String cdconven = params.get("cdconven");
			String status   = params.get("estatus");
			Date fecregis = new Date();
			usuVO = new UserVO();
			String cdusureg = Utils.validateSession(session).getUser();
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### guardarEnBase ######"
					,"\n###### cdunieco ",cdunieco
					,"\n###### cdramo ",cdramo
					,"\n###### estado ",estado
					,"\n###### cdtipsit",cdtipsit
					,"\n###### nmpoliza ",nmpoliza
					,"\n###### diasgrac ",diasgrac
					,"\n###### cdconven ",cdconven
					,"\n###### status ",status	
					,"\n###### fecha ",fecregis									
					,"\n###### usuario ",cdusureg
					));
			conveniosManager.guardarEnBase(cdunieco,cdramo,estado,cdtipsit,nmpoliza,diasgrac,cdconven,status,fecregis,cdusureg,fecregis,cdusureg,"I");
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### termina guardarEnBase ######"
					));
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### guardarEnBase ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "editarEnBase",
			results = { @Result(name="success", type="json") }
			)
	public String editarEnBase()
	{		
		UserVO usuVO = null;
		try
		{
			Utils.validate(params,"No se recibieron datos");		
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String cdtipsit   = params.get("cdtipsit");
			String estado   = "M";
			String nmpoliza = params.get("nmpoliza");
			String diasgrac = params.get("diasgrac");
			String cdconven = params.get("cdconven");
			String status   = params.get("estatus");			
			Date fecregis = new Date();
			usuVO = new UserVO();
			String cdusureg = Utils.validateSession(session).getUser();
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### editandoBase ######"
					,"\n###### cdunieco ",cdunieco
					,"\n###### cdramo ",cdramo
					,"\n###### estado ",estado
					,"\n###### nmpoliza ",nmpoliza
					,"\n###### diasgrac ",diasgrac
					,"\n###### cdconven ",cdconven
					,"\n###### status ",status	
					,"\n###### fecha ",fecregis									
					,"\n###### usuario ",cdusureg
					));
			conveniosManager.guardarEnBase(cdunieco,cdramo,estado,cdtipsit,nmpoliza,diasgrac,cdconven,status,null,null,fecregis,cdusureg,"U");
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### termina editandoBase ######"
					));
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### guardarEnBase ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "buscarPorPoliza",
			results = { @Result(name="success", type="json") }
			)
	public String buscarPorPoliza()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### buscarPorPoliza ######"
				));		
		try
		{
			Utils.validate(params,"No se recibieron datos");
			String cdunieco  = params.get("cdunieco");
			String cdramo  = params.get("cdramo");
			String cdtipsit  = params.get("cdtipsit");
			String estado  = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String cdcontra = params.get("cdperson");
			list = conveniosManager.buscarPoliza(cdunieco, cdramo, cdtipsit, estado, nmpoliza, cdcontra);
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### buscarPorPoliza ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "consultaCancelacioneConvenios",
		    results = {
		        @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
		        @Result(name="success" , location="/jsp-script/proceso/convenios/exclusionConvenios.jsp")
		    })
	public String consultaCancelacioneConvenios()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### consultaCancelacioneConvenios ######"
				));
		
		String result = ERROR;
		
		try
		{
			items = conveniosManager.recuperarCancelacionesElementosPantalla();
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### result=" , result
				,"\n###### Consulta convenios de cancelacion ######"
				,"\n###########################"
				));
		return result;
	}
	
	@Action(value   = "guardarCancelacionesEnBase",
			results = { @Result(name="success", type="json") }
			)
	public String guardarCancelacionesEnBase()
	{		
		UserVO usuVO = null;
		try
		{
			Utils.validate(params,"No se recibieron datos");		
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String status   = params.get("estatus");
			Date fecregis = new Date();
			usuVO = new UserVO();
			String cdusureg = Utils.validateSession(session).getUser();
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### guardarCancelacionesEnBase ######"
					,"\n###### cdunieco ",cdunieco
					,"\n###### cdramo ",cdramo
					,"\n###### estado ",estado
					,"\n###### nmpoliza ",nmpoliza
					,"\n###### status ",status	
					,"\n###### fecha ",fecregis									
					,"\n###### usuario ",cdusureg
					));
			conveniosManager.guardarCancelacionesEnBase(cdunieco,cdramo,estado,nmpoliza,status,fecregis,cdusureg,fecregis,cdusureg,"I");
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### termina guardarCancelacionesEnBase ######"
					));
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### guardarEnBase ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "editarCancelacionesEnBase",
			results = { @Result(name="success", type="json") }
			)
	public String editarCancelacionesEnBase()
	{		
		UserVO usuVO = null;
		try
		{
			Utils.validate(params,"No se recibieron datos");		
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String cdtipsit   = params.get("cdtipsit");
			String estado   = "M";
			String nmpoliza = params.get("nmpoliza");
			String diasgrac = params.get("diasgrac");
			String cdconven = params.get("cdconven");
			String status   = params.get("estatus");			
			Date fecregis = new Date();
			usuVO = new UserVO();
			String cdusureg = Utils.validateSession(session).getUser();
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### editarCancelacionesEnBase ######"
					,"\n###### cdunieco ",cdunieco
					,"\n###### cdramo ",cdramo
					,"\n###### estado ",estado
					,"\n###### nmpoliza ",nmpoliza
					,"\n###### diasgrac ",diasgrac
					,"\n###### cdconven ",cdconven
					,"\n###### status ",status	
					,"\n###### fecha ",fecregis									
					,"\n###### usuario ",cdusureg
					));
			conveniosManager.guardarCancelacionesEnBase(cdunieco,cdramo,estado,nmpoliza,status,null,null,fecregis,cdusureg,"U");
			logger.debug(Utils.log(
					 "\n###########################"
					,"\n###### termina editarCancelacionesEnBase ######"
					));
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### guardarEnBase ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "buscarCancelacionesPorPoliza",
			results = { @Result(name="success", type="json") }
			)
	public String buscarCancelacionesPorPoliza()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### buscarCancelacionesPorPoliza ######"
				));		
		try
		{
			Utils.validate(params,"No se recibieron datos");
			String cdunieco  = params.get("cdunieco");
			String cdramo  = params.get("cdramo");
			String cdtipsit  = params.get("cdtipsit");
			String estado  = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			list = conveniosManager.buscarCancelacionesPoliza(cdunieco, cdramo, cdtipsit, estado, nmpoliza);
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### buscarCancelacionesPorPoliza ######"
				,"\n###########################"
				));
		return SUCCESS;
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
