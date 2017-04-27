package mx.com.gseguros.portal.catalogos.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.service.TvalositManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TvalositManagerImpl implements TvalositManager
{
	private static Logger logger = LoggerFactory.getLogger(TvalositManagerImpl.class);
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Override
	public Map<String,Item> pantallaActTvalosit(
			String origen
			,String cdsisrol
			,String cdtipsit
			,String contexto
			)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaActTvalosit @@@@@@"
				,"\n@@@@@@ origen="   , origen
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ contexto=" , contexto
				));
		
		String paso = null;
		
		Map<String,Item> items = new HashMap<String,Item>();
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(Utils.log("","paso=",paso));
			
			List<ComponenteVO> listaItems = pantallasDAO.obtenerComponentes(
					null                     , null    , null
					,cdtipsit                , origen  , cdsisrol
					,"PANTALLA_ACT_TVALOSIT" , "ITEMS" , null);
			
			paso = "Construyendo componentes";
			logger.debug(Utils.log("","paso=",paso));
			
			GeneradorCampos gc = new GeneradorCampos(contexto);
			
			gc.generaComponentes(listaItems, true, false, true, false, false, false);
			
			items.put("items" , gc.getItems());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ items=", items
				,"\n@@@@@@ pantallaActTvalosit @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return items;
	}
	
	@Override
	public Map<String,String> cargarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarPantallaActTvalosit @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				));

		String paso = null;
		
		Map<String,String> datos = null;
		
		try
		{
			paso = "Recuperando datos de situaci\u00f3n";
			logger.debug(Utils.log("","paso=",paso));
			
			List<Map<String,String>>tvalosits = consultasDAO.cargarTvalosit(cdunieco,cdramo,estado,nmpoliza,"0");
			
			for(Map<String,String> tvalosit : tvalosits)
			{
				if(nmsituac.equals(tvalosit.get("NMSITUAC")))
				{
					datos = tvalosit;
					break;
				}
			}
			
			if(datos==null)
			{
				throw new ApplicationException("No se encuentran los datos de sitauci\u00f3n");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ datos=", datos
				,"\n@@@@@@ cargarPantallaActTvalosit @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return datos;
	}
	
	@Override
	public void guardarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,Map<String,String> otvalores
			)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarPantallaActTvalosit @@@@@@"
				,"\n@@@@@@ cdunieco="  , cdunieco
				,"\n@@@@@@ cdramo="    , cdramo
				,"\n@@@@@@ estado="    , estado
				,"\n@@@@@@ nmpoliza="  , nmpoliza
				,"\n@@@@@@ nmsituac="  , nmsituac
				,"\n@@@@@@ nmsuplem="  , nmsuplem
				,"\n@@@@@@ otvalores=" , otvalores
				));
		
		String paso = null;
		try
		{
			paso = "Recopilando switches y valores";
			logger.debug(Utils.log("","paso=",paso));
			
			String[] valores   = new String[100]
					 ,switches = new String[100];
			
			for(int i=1 ; i<=99 ; i++)
			{
				String key = Utils.join(
						"OTVALOR"
						,StringUtils.leftPad(i+"", 2, "0")
						);
				
				if(otvalores.containsKey(key))
				{
					switches[i] = "S";
					valores[i]  = otvalores.get(key);
				}
				else
				{
					switches[i] = "N";
				}
			}
			
			paso = "Guardando valores";
			logger.debug(Utils.log("","paso=",paso));
			
			cotizacionDAO.guardarPantallaActTvalosit(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsituac
					,nmsuplem
					,valores[1] ,valores[2] ,valores[3] ,valores[4] ,valores[5] ,valores[6] ,valores[7] ,valores[8] ,valores[9] ,valores[10]
					,valores[11],valores[12],valores[13],valores[14],valores[15],valores[16],valores[17],valores[18],valores[19],valores[20]
					,valores[21],valores[22],valores[23],valores[24],valores[25],valores[26],valores[27],valores[28],valores[29],valores[30]
					,valores[31],valores[32],valores[33],valores[34],valores[35],valores[36],valores[37],valores[38],valores[39],valores[40]
					,valores[41],valores[42],valores[43],valores[44],valores[45],valores[46],valores[47],valores[48],valores[49],valores[50]
					,valores[51],valores[52],valores[53],valores[54],valores[55],valores[56],valores[57],valores[58],valores[59],valores[60]
					,valores[61],valores[62],valores[63],valores[64],valores[65],valores[66],valores[67],valores[68],valores[69],valores[70]
					,valores[71],valores[72],valores[73],valores[74],valores[75],valores[76],valores[77],valores[78],valores[79],valores[80]
					,valores[81],valores[82],valores[83],valores[84],valores[85],valores[86],valores[87],valores[88],valores[89],valores[90]
					,valores[91],valores[92],valores[93],valores[94],valores[95],valores[96],valores[97],valores[98],valores[99]
					,switches[1] ,switches[2] ,switches[3] ,switches[4] ,switches[5] ,switches[6] ,switches[7] ,switches[8] ,switches[9] ,switches[10]
					,switches[11],switches[12],switches[13],switches[14],switches[15],switches[16],switches[17],switches[18],switches[19],switches[20]
					,switches[21],switches[22],switches[23],switches[24],switches[25],switches[26],switches[27],switches[28],switches[29],switches[30]
					,switches[31],switches[32],switches[33],switches[34],switches[35],switches[36],switches[37],switches[38],switches[39],switches[40]
					,switches[41],switches[42],switches[43],switches[44],switches[45],switches[46],switches[47],switches[48],switches[49],switches[50]
					,switches[51],switches[52],switches[53],switches[54],switches[55],switches[56],switches[57],switches[58],switches[59],switches[60]
					,switches[61],switches[62],switches[63],switches[64],switches[65],switches[66],switches[67],switches[68],switches[69],switches[70]
					,switches[71],switches[72],switches[73],switches[74],switches[75],switches[76],switches[77],switches[78],switches[79],switches[80]
					,switches[81],switches[82],switches[83],switches[84],switches[85],switches[86],switches[87],switches[88],switches[89],switches[90]
					,switches[91],switches[92],switches[93],switches[94],switches[95],switches[96],switches[97],switches[98],switches[99]
					);
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ guardarPantallaActTvalosit @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
}