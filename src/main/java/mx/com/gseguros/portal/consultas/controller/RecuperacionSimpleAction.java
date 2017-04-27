package mx.com.gseguros.portal.consultas.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.consultas.service.RecuperacionSimpleManager;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlist2VO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.utils.Utils;

public class RecuperacionSimpleAction extends PrincipalCoreAction
{
	
	private static Logger logger = Logger.getLogger(RecuperacionSimpleAction.class);
	
	private boolean                   success;
	private boolean                   exito;
	private String                    respuesta;
	private Map<String,String>        smap1;
	private List<Map<String,String>>  slist1;
	private List<Map<String,String>>  slist2;
	private RecuperacionSimpleManager recuperacionSimpleManager;
	private String start;
	private String limit;
	private String total;
	
	public RecuperacionSimpleAction () {
		this.session = ActionContext.getContext().getSession();
	}
	/*
	 * Utilidades
	 */
	
	public String recuperacionSimple()
	{
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### recuperacionSimple ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1                  , "No se recibieron datos");
			String procedimiento = smap1.get("procedimiento");
			Utils.validate(procedimiento          , "No se recibio el procedimiento");
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			
			RecuperacionSimple rec;
			
			try
			{
				rec = RecuperacionSimple.valueOf(procedimiento);
			}
			catch(Exception ex)
			{
				logger.error("Error al intentar obtener el catalogo del enum",ex);
				throw new ApplicationException("El procedimiento no existe");
			}
			
			ManagerRespuestaSmapVO resp = recuperacionSimpleManager.recuperacionSimple(rec,smap1,cdsisrol,cdusuari);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			if(exito)
			{
				smap1.putAll(resp.getSmap());
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recuperacionSimple ######"
				,"\n################################"
				));
		return SUCCESS;
	}
	
	public String recuperacionSimpleLista()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### recuperacionSimpleLista ######"
				,"\n###### smap1=" , smap1
				));
		
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1                  , "No se recibieron datos");
			String procedimiento = smap1.get("procedimiento");
			Utils.validate(procedimiento          , "No se recibio el procedimiento");
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			
			RecuperacionSimple rec;
			
			try
			{
				rec = RecuperacionSimple.valueOf(procedimiento);
			}
			catch(Exception ex)
			{
				logger.error("Error al intentar obtener el catalogo del enum",ex);
				throw new ApplicationException("El procedimiento no existe");
			}
			
			smap1.put("start", start);
			smap1.put("limit",limit);
			
			ManagerRespuestaSlist2VO resp = recuperacionSimpleManager.recuperacionSimpleLista(rec,smap1,cdsisrol,cdusuari);
			exito     = resp.isExito();
			respuesta = resp.getRespuesta();
			if(exito)
			{
				slist1 = resp.getSlist();
				slist2 = resp.getSlist2();
				
				if(rec.equals(RecuperacionSimple.RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA)){
					Map<String,String>total = slist1.remove(slist1.size()-1);
					this.total=total.get("total");
				}
				
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recuperacionSimpleLista ######"
				,"\n#####################################"
				));
		return SUCCESS;
	}
	
	public String recuperacionSimpleListaEndoso()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### recuperacionSimpleListaEndoso ######"
				,"\n###### smap1=" , smap1
				));
		
		try
		{
			logger.debug("Validando datos de entrada");
			Utils.validate(smap1                  , "No se recibieron datos");
			String procedimiento = smap1.get("procedimiento");
			Utils.validate(procedimiento          , "No se recibio el procedimiento");
			Utils.validate(session                , "No hay sesion");
			Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
			String cdsisrol = ((UserVO)session.get("USUARIO")).getRolActivo().getClave();
			String cdusuari = ((UserVO)session.get("USUARIO")).getUser();
			
			RecuperacionSimple rec;
			
			try
			{
				rec = RecuperacionSimple.valueOf(procedimiento);
			}
			catch(Exception ex)
			{
				logger.error("Error al intentar obtener el catalogo del enum",ex);
				throw new ApplicationException("El procedimiento no existe");
			}
			
			ManagerRespuestaSlist2VO resp = recuperacionSimpleManager.recuperacionSimpleLista(rec,smap1,cdsisrol,cdusuari);
			exito     = resp.isExito();
			respuesta = resp.getRespuesta();
			if(exito)
			{
				slist1 = resp.getSlist();
				slist2 = resp.getSlist2();
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### recuperacionSimpleListaEndoso ######"
				,"\n#####################################"
				));
		return SUCCESS;
	}

	/*
	 * Getters y setters
	 */
	public boolean isSuccess()
	{
		return true;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setRecuperacionSimpleManager(RecuperacionSimpleManager recuperacionSimpleManager) {
		this.recuperacionSimpleManager = recuperacionSimpleManager;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}