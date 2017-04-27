package mx.com.gseguros.portal.endosos.service.impl;

import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.endosos.dao.EndososGrupoDAO;
import mx.com.gseguros.portal.endosos.service.EndososGrupoManager;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class EndososGrupoManagerImpl implements EndososGrupoManager
{
	private static final Logger logger = Logger.getLogger(EndososGrupoManagerImpl.class);
	
	private EndososGrupoDAO endososGrupoDAO;
	private PantallasDAO    pantallasDAO;
	
	@Override
	public ManagerRespuestaImapVO endososGrupo()
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ endososGrupo @@@@@@")
				.toString()
				);
		
		ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
		resp.setImap(new HashMap<String,Item>());
		
		try
		{
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			List<ComponenteVO>componentesHistorico=pantallasDAO.obtenerComponentes(
					null             , null           , null
					,null            , null           , null
					,"ENDOSOS_GRUPO" , "GRID_POLIZAS" , null);
			gc.generaComponentes(componentesHistorico, true, true, false, true, true, false);
			
			resp.getImap().put("historicoFields"  , gc.getFields());
			resp.getImap().put("historicoColumns" , gc.getColumns());
			
			List<ComponenteVO>componentesFamilia=pantallasDAO.obtenerComponentes(
					null             , null           , null
					,null            , null           , null
					,"ENDOSOS_GRUPO" , "GRID_FAMILIAS" , null);
			gc.generaComponentes(componentesFamilia, true, true, false, true, true, false);
			
			resp.getImap().put("familiaFields"  , gc.getFields());
			resp.getImap().put("familiaColumns" , gc.getColumns());
			
			List<ComponenteVO>componentesIntegrante=pantallasDAO.obtenerComponentes(
					null             , null           , null
					,null            , null           , null
					,"ENDOSOS_GRUPO" , "GRID_INTEGRANTES" , null);
			gc.generaComponentes(componentesIntegrante, true, true, false, true, true, false);
			
			resp.getImap().put("integranteFields"  , gc.getFields());
			resp.getImap().put("integranteColumns" , gc.getColumns());
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener componentes #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ endososGrupo @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO buscarHistoricoPolizas(String nmpoliex,String rfc,String cdperson,String nombre,String cdsisrol)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ buscarHistoricoPolizas @@@@@@")
				.append("\n@@@@@@ nmpoliex=").append(nmpoliex)
				.append("\n@@@@@@ rfc=")     .append(rfc)
				.append("\n@@@@@@ cdperson=").append(cdperson)
				.append("\n@@@@@@ nombre=")  .append(nombre)
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			resp.setSlist(endososGrupoDAO.buscarHistoricoPolizas(nmpoliex,rfc,cdperson,nombre,cdsisrol));
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener movimientos #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ buscarHistoricoPolizas @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO cargarFamiliasPoliza(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarFamiliasPoliza @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			resp.setSlist(endososGrupoDAO.cargarFamiliasPoliza(cdunieco,cdramo,estado,nmpoliza,nmsuplem));
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener familias #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarFamiliasPoliza @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSlistVO cargarIntegrantesFamilia(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,String nmsitaux)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarIntegrantesFamilia @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ nmsitaux=").append(nmsitaux)
				.toString()
				);
		
		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		
		try
		{
			resp.setSlist(endososGrupoDAO.cargarIntegrantesFamilia(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsitaux));
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener integrantes #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarIntegrantesFamilia @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	//getters y setters
	public void setEndososGrupoDAO(EndososGrupoDAO endososGrupoDAO)
	{
		this.endososGrupoDAO = endososGrupoDAO;
	}
	
	public void setPantallasDAO(PantallasDAO pantallasDAO)
	{
		this.pantallasDAO = pantallasDAO;
	}
}