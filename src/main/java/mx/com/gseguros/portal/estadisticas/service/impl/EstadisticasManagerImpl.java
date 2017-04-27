package mx.com.gseguros.portal.estadisticas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.estadisticas.service.EstadisticasManager;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasManagerImpl implements EstadisticasManager
{
	private static Logger logger = Logger.getLogger(EstadisticasManagerImpl.class);
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Override
	public Map<String,Item> cotizacionEmision(StringBuilder sb, String cdsisrol) throws Exception
	{
		sb.append(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cotizacionEmision @@@@@@"
				,"\n@@@@@@ cdsisrol=",cdsisrol
				));
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		try
		{
			paso = "Recuperando items del filtro";
			sb.append(Utils.join("\n",paso));
			
			List<ComponenteVO> itemsFiltro = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"ESTADISTICAS"
					,"FILTRO_4"
					,null //orden
					);
			
			paso = "Generando componentes del filtro";
			sb.append(Utils.join("\n",paso));
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(itemsFiltro, true, false, true, false, false, false);
			
			items.put("itemsFiltro" , gc.getItems());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		return items;
	}
	
	@Override
	public Map<String,Object> recuperarCotizacionesEmisiones(
			StringBuilder sb
			,Date fedesde
			,Date fehasta
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdagente
			)throws Exception
	{
		sb.append(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarCotizacionesEmisiones @@@@@@"
				,"\n@@@@@@ fedesde="  , fedesde
				,"\n@@@@@@ fehasta="  , fehasta
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdagente=" , cdagente
				));
		
		Object listas = null;
		String paso   = null;
		
		try
		{
			paso = "Recuperando indicadores";
			sb.append(Utils.join("\n",paso));
			
			listas = consultasDAO.recuperarEstadisticasCotizacionEmision(
					fedesde
					,fehasta
					,cdunieco
					,cdramo
					,cdusuari
					,cdagente
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		return (Map<String,Object>)listas;
	}
	
	@Override
	public Map<String,Item> tareas(StringBuilder sb, String cdsisrol) throws Exception
	{
		sb.append(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ tareas @@@@@@"
				,"\n@@@@@@ cdsisrol=",cdsisrol
				));
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		try
		{
			paso = "Recuperando items del filtro";
			sb.append(Utils.join("\n",paso));
			
			List<ComponenteVO> itemsFiltro = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"ESTADISTICAS"
					,"FILTRO_1"
					,null //orden
					);
			
			paso = "Generando componentes del filtro";
			sb.append(Utils.join("\n",paso));
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(itemsFiltro, true, false, true, false, false, false);
			
			items.put("itemsFiltro" , gc.getItems());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		return items;
	}
	
	@Override
	public Map<String,Object> recuperarTareas(
			StringBuilder sb
			,Date fedesde
			,Date fehasta
			,String cdmodulo
			,String cdtarea
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		sb.append(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTareas @@@@@@"
				,"\n@@@@@@ fedesde="  , fedesde
				,"\n@@@@@@ fehasta="  , fehasta
				,"\n@@@@@@ cdmodulo=" , cdmodulo
				,"\n@@@@@@ cdtarea="  , cdtarea
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));
		
		Object listas = null;
		String paso   = null;
		
		try
		{
			paso = "Recuperando indicadores";
			sb.append(Utils.join("\n",paso));
			
			listas = consultasDAO.recuperarEstadisticasTareas(
					fedesde
					,fehasta
					,cdmodulo
					,cdtarea
					,cdunieco
					,cdramo
					,cdusuari
					,cdsisrol
					);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		return (Map<String,Object>)listas;
	}

	//////////////////////////////////////////////////////////////////////////////////
	//////////// GETTERS Y SETTERS ///////////////////////////////////////////////////
	////////////////////// GETTERS Y SETTERS /////////////////////////////////////////
	//////////////////////////////// GETTERS Y SETTERS ///////////////////////////////
	////////////////////////////////////////// GETTERS Y SETTERS /////////////////////
	//////////////////////////////////////////////////// GETTERS Y SETTERS ///////////
	//////////////////////////////////////////////////////////////////////////////////
	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}

	public void setConsultasDAO(ConsultasDAO consultasDAO) {
		this.consultasDAO = consultasDAO;
	}
}