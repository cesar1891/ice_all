package mx.com.gseguros.portal.endosos.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.endosos.service.EndososGrupoManager;
import mx.com.gseguros.portal.general.service.PantallasManager;

public class EndososGrupoAction extends PrincipalCoreAction
{
	private static final long   serialVersionUID = -6325542558530438412L;
	private static final Logger logger           = Logger.getLogger(EndososGrupoAction.class);
	
	private boolean                  success         = true;
	private boolean                  exito           = false;
	private String                   respuesta;
	private String                   respuestaOculta = null;
	private Map<String,String>       smap1           = null;
	private Map<String,Item>         imap            = null;
	private List<Map<String,String>> slist1          = null;
	
	private EndososGrupoManager endososGrupoManager;
	private PantallasManager    pantallasManager;

	public String endososGrupo()
	{
		logger.info(
				new StringBuilder()
				.append("\n##########################")
				.append("\n###### endososGrupo ######")
				.toString());
		
		//componentes
		ManagerRespuestaImapVO resp = endososGrupoManager.endososGrupo();
		exito           = resp.isExito();
		respuesta       = resp.getRespuesta();
		respuestaOculta = resp.getRespuestaOculta();
		if(exito)
		{
			imap = resp.getImap();
		}
		
		String result = SUCCESS;
		if(!exito)
		{
			result = ERROR;
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### endososGrupo ######")
				.append("\n##########################")
				.toString());
		return result;
	}
	
	public String buscarHistoricoPolizas()
	{
		logger.info(
				new StringBuilder()
				.append("\n####################################")
				.append("\n###### buscarHistoricoPolizas ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String nmpoliex = null;
		String rfc      = null;
		String cdperson = null;
		String nombre   = null;
		String cdsisrol = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			nmpoliex = smap1.get("nmpoliex");
			rfc      = smap1.get("rfc");
			cdperson = smap1.get("cdperson");
			nombre   = smap1.get("nombre");
			cdsisrol = smap1.get("cdsisrol");
			
			if(StringUtils.isBlank(nmpoliex)
					&&StringUtils.isBlank(rfc)
					&&StringUtils.isBlank(cdperson)
					&&StringUtils.isBlank(nombre))
			{
				throw new ApplicationException("No se recibieron datos");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSlistVO resp = endososGrupoManager.buscarHistoricoPolizas(nmpoliex,rfc,cdperson,nombre,cdsisrol);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				slist1 = resp.getSlist();
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### buscarHistoricoPolizas ######")
				.append("\n####################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarFamiliasPoliza()
	{
		logger.info(
				new StringBuilder()
				.append("\n##################################")
				.append("\n###### cargarFamiliasPoliza ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsuplem = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			if(StringUtils.isBlank(cdunieco))
			{
				throw new ApplicationException("No se recibio la sucursal");
			}
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio el producto");
			}
			if(StringUtils.isBlank(estado))
			{
				throw new ApplicationException("No se recibio el estado");
			}
			if(StringUtils.isBlank(nmpoliza))
			{
				throw new ApplicationException("No se recibio el numero de poliza");
			}
			if(StringUtils.isBlank(nmsuplem))
			{
				throw new ApplicationException("No se recibio el suplemento");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = true;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString().toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSlistVO resp = endososGrupoManager.cargarFamiliasPoliza(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				slist1 = resp.getSlist();
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### cargarFamiliasPoliza ######")
				.append("\n##################################")
				.toString()
				);
		return SUCCESS;
	}
	
	public String cargarIntegrantesFamilia()
	{
		logger.info(
				new StringBuilder()
				.append("\n######################################")
				.append("\n###### cargarIntegrantesFamilia ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String cdunieco = null;
		String cdramo   = null;
		String estado   = null;
		String nmpoliza = null;
		String nmsuplem = null;
		String nmsitaux = null;
		
		//datos completos
		try
		{
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron datos");
			}
			cdunieco = smap1.get("cdunieco");
			cdramo   = smap1.get("cdramo");
			estado   = smap1.get("estado");
			nmpoliza = smap1.get("nmpoliza");
			nmsuplem = smap1.get("nmsuplem");
			nmsitaux = smap1.get("nmsitaux");
			if(StringUtils.isBlank(cdunieco))
			{
				throw new ApplicationException("No se recibio sucursal");
			}
			if(StringUtils.isBlank(cdramo))
			{
				throw new ApplicationException("No se recibio producto");
			}
			if(StringUtils.isBlank(estado))
			{
				throw new ApplicationException("No se recibio estado");
			}
			if(StringUtils.isBlank(nmpoliza))
			{
				throw new ApplicationException("No se recibio numero de poliza");
			}
			if(StringUtils.isBlank(nmsuplem))
			{
				throw new ApplicationException("No se recibio suplemento");
			}
			if(StringUtils.isBlank(nmsitaux))
			{
				throw new ApplicationException("No se recibio numero de familia");
			}
		}
		catch(ApplicationException ax)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString();
			respuestaOculta = ax.getMessage();
			logger.error(respuesta,ax);
		}
		
		//proceso
		if(exito)
		{
			ManagerRespuestaSlistVO resp = endososGrupoManager.cargarIntegrantesFamilia(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsitaux);
			exito           = resp.isExito();
			respuesta       = resp.getRespuesta();
			respuestaOculta = resp.getRespuestaOculta();
			if(exito)
			{
				slist1 = resp.getSlist();
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### cargarIntegrantesFamilia ######")
				.append("\n######################################")
				.toString()
				);
		return SUCCESS;
	}
	
	//Getters y setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

	public String getRespuestaOculta() {
		return respuestaOculta;
	}

	public void setRespuestaOculta(String respuestaOculta) {
		this.respuestaOculta = respuestaOculta;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}

	public void setEndososGrupoManager(EndososGrupoManager endososGrupoManager) {
		this.endososGrupoManager = endososGrupoManager;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}
}