package mx.com.gseguros.portal.general.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.JaimeErickManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JaimeErickManagerImpl implements JaimeErickManager
{
	private Logger logger = LoggerFactory.getLogger(JaimeErickManagerImpl.class);
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	public Map<String,Item> recuperarElementosPantalla() throws Exception
	{
		String paso = null;
		Map<String,Item> elementos = new HashMap<String,Item>();
		
		try
		{
			paso = "Probando paso";
			
			if(Math.random()>0.5d)
			{
				throw new Exception("Error random");
			}
			
			paso = "Recuperando elementos";
			
			List<ComponenteVO> itemsForm = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_ERICK_JAIME"
					,"FORMULARIO"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsForm, true, false, true, false, false, false);
			
			elementos.put("itemsFormulario" , gc.getItems());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		return elementos;
	}
	
	@Override
	public void guardarEnBase(String nombre, String edad, String fecha) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarEnBase @@@@@@"
				,"\n@@@@@@ nombre=",nombre
				,"\n@@@@@@ edad=",edad
				,"\n@@@@@@ fecha=",fecha
				));
		String paso = null;
		
		try
		{
			paso = "Probando paso";
			
			if(Math.random()>0.5d)
			{
				throw new Exception("Error random");
			}
			
			paso = "guardando datos";
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
}