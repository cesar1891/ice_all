package mx.com.gseguros.portal.general.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasDAOImpl;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.ConveniosManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;

@Service
public class ConveniosManagerImpl implements ConveniosManager{
	
	private Logger logger = LoggerFactory.getLogger(ConveniosManagerImpl.class);
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	public Map<String,Item> recuperarElementosPantalla() throws Exception
	{
		String paso = null;
		Map<String,Item> elementos = new HashMap<String,Item>();
		
		try
		{
			paso = "Probando paso";
			
			paso = "Recuperando elementos";
			
			List<ComponenteVO> itemsForm = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CONVENIOS"
					,"FORMULARIO"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsForm, true, false, true, false, false, false);
			
			elementos.put("itemsFormulario" , gc.getItems());
			
			List<ComponenteVO> itemsGrid = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CONVENIOS"
					,"MODELO_CONVENIO"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			gc.generaComponentes(itemsGrid, true, true, true, true, true, false);
			
			elementos.put("itemsGrid" , gc.getColumns());
			
			elementos.put("itemsGridModel" , gc.getFields());
			
			List<ComponenteVO> itemsInsert = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CONVENIOS"
					,"FORMULARIO_INSERT"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			gc.generaComponentes(itemsInsert, true, false, true, false, false, false);
			
			elementos.put("itemsInsert" , gc.getItems());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		return elementos;
	}	
	
	@Override
	public void guardarEnBase(String cdunieco, String cdramo, String estado, String cdtipsit, String nmpoliza, String diasgrac, String cdconven, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception
	{
		String paso = null;
		
		try
		{
			paso = "insertando en base";		
			
			consultasDAO.insertarConvenioPoliza(cdunieco, cdramo, estado, cdtipsit, nmpoliza, diasgrac, cdconven, status, fecregis, cdusureg, fecmodif, cdusumod, operacion);
	
			paso = "terminando de insertar en base";
			//dao.guardarDatos
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarEnBase @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}

	@Override
	public List<Map<String, String>> buscarPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza, String cdcontra) throws Exception {
		String paso = null;
		List<Map<String, String>> infoGrid = null;
		try
		{
			paso = "Antes de buscar poliza";
		
			infoGrid = consultasDAO.recuperarConveniosPorPoliza(cdunieco, cdramo, cdtipsit, estado, nmpoliza, cdcontra);
			
			paso = "Despues de buscar poliza";
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		return infoGrid;
	}

	public Map<String,Item> recuperarCancelacionesElementosPantalla() throws Exception
	{
		String paso = null;
		Map<String,Item> elementos = new HashMap<String,Item>();
		
		try
		{
			paso = "Probando paso";
			
			paso = "Recuperando elementos";
			
			List<ComponenteVO> itemsForm = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CANCELACION_CONVENIOS"
					,"FORMULARIO"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsForm, true, false, true, false, false, false);
			
			elementos.put("itemsFormulario" , gc.getItems());
			
			List<ComponenteVO> itemsGrid = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CANCELACION_CONVENIOS"
					,"MODELO_CONVENIO"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			gc.generaComponentes(itemsGrid, true, true, true, true, true, false);
			
			elementos.put("itemsGrid" , gc.getColumns());
			
			elementos.put("itemsGridModel" , gc.getFields());
			
			List<ComponenteVO> itemsInsert = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CANCELACION_CONVENIOS"
					,"FORMULARIO_INSERT"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			gc.generaComponentes(itemsInsert, true, false, true, false, false, false);
			
			elementos.put("itemsInsert" , gc.getItems());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		return elementos;
	}	
	
	@Override
	public void guardarCancelacionesEnBase(String cdunieco, String cdramo, String estado, String nmpoliza, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception
	{
		String paso = null;
		
		try
		{
			paso = "insertando en base";		
			
			consultasDAO.insertarCancelacionesConvenioPoliza(cdunieco, cdramo, estado, nmpoliza, status, fecregis, cdusureg, fecmodif, cdusumod, operacion);
	
			paso = "terminando de insertar en base";
			//dao.guardarDatos
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ guardarEnBase @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}

	@Override
	public List<Map<String, String>> buscarCancelacionesPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza) throws Exception {
		String paso = null;
		List<Map<String, String>> infoGrid = null;
		try
		{
			paso = "Antes de buscar poliza";
		
			infoGrid = consultasDAO.recuperarCancelacionesConveniosPorPoliza(cdunieco, cdramo, cdtipsit, estado, nmpoliza);
			
			paso = "Despues de buscar poliza";
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		return infoGrid;
	}

}
