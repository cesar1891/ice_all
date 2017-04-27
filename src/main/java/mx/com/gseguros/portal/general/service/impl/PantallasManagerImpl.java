package mx.com.gseguros.portal.general.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;

public class PantallasManagerImpl implements PantallasManager
{
	private final static Logger logger = Logger.getLogger(PantallasManagerImpl.class);
	
	private PantallasDAO  pantallasDAO;
	
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	@Override
	public List<ComponenteVO> obtenerComponentes(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtenerComponentes @@@@@@"
				,"\n@@@@@@ cdtiptra=" , cdtiptra
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ pantalla=" , pantalla
				,"\n@@@@@@ seccion="  , seccion
				,"\n@@@@@@ orden="    , orden
				));
		List<ComponenteVO> lista=pantallasDAO.obtenerComponentes(cdtiptra, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, orden);
		lista=lista!=null?lista:new ArrayList<ComponenteVO>(0);
		logger.debug(Utils.log(
				 "\n@@@@@@ lista size=",lista.size()
				,"\n@@@@@@ obtenerComponentes @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	/**
	 * PKG_CONF_PANTALLAS.P_GET_TCONFCMP
	 */
	@Override
	public List<Map<String, String>> obtenerParametros(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtenerParametros @@@@@@"
				,"\n@@@@@@ cdtiptra=" , cdtiptra
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ pantalla=" , pantalla
				,"\n@@@@@@ seccion="  , seccion
				,"\n@@@@@@ orden="    , orden
				));
		List<Map<String,String>> lista=pantallasDAO.obtenerParametros(cdtiptra, cdunieco, cdramo, cdtipsit, estado, cdsisrol, pantalla, seccion, orden);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug(Utils.log(
				 "\n@@@@@@ lista size=",lista.size()
				,"\n@@@@@@ obtenerParametros @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	/**
	 * PKG_CONF_PANTALLAS.P_MOV_TCONFCMP
	 */
	@Override
	public void movParametros(
			 String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			,String accion
			,String idproceso
			) throws Exception
	{
		Map<String,String>params = new LinkedHashMap<String,String>(0);
		
		params.put("PV_CDUNIECO_I" , cdunieco);
		params.put("PV_CDRAMO_I"   , cdramo);
		params.put("PV_CDTIPSIT_I" , cdtipsit);
		params.put("PV_ESTADO_I"   , estado);
		params.put("PV_PANTALLA_I" , pantalla);
		params.put("PV_CDSISROL_I" , cdsisrol);
		params.put("PV_CDTIPTRA_I" , cdtiptra);
		params.put("PV_ORDEN_I"    , orden);
		params.put("PV_SECCION_I"  , seccion);
		params.put("PV_ACCION_I"   , accion);
		params.put("PV_IDPRO_I"    , idproceso);
		logger.debug("EndososManager movParametros params: "+params);
		pantallasDAO.movParametros(params);
		logger.debug("EndososManager movParametros end");
    }
	
	/**
	 * PKG_CONF_PANTALLAS.P_INSERTA_TCONFCMP
	 */
	@Override
	public void insertarParametros(Map<String,String> params) throws Exception
	{
		logger.debug("EndososManager insertarParametros params: "+params);
		pantallasDAO.insertarParametros(params);
		logger.debug("EndososManager insertarParametros end");
	}
	
	/**
	 * PKG_CONF_PANTALLAS.P_OBT_ARBOL_TCONFCMP
	 */
	@Override
	public Item obtenerArbol() throws Exception
    {
		logger.debug("EndososManager obtenerArbol inicio");
		
		//obtener registros
		List<Map<String,String>> lista=pantallasDAO.obtenerArbol();
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerArbol lista size: "+lista.size());
		
		String pantallaKey   = "PANTALLA";
		String componenteKey = "SECCION";
		
		//poner llaves (pantalla) y hojas (componentes)
		Map<String,List<String>>arbol=new LinkedHashMap<String,List<String>>();
		for(Map<String,String>registroIte:lista)
		{
			String pantallaIte   = registroIte.get(pantallaKey);
			String componenteIte = registroIte.get(componenteKey);
			
			//poner llave
			if(!arbol.containsKey(pantallaIte))
			{
				arbol.put(pantallaIte,new ArrayList<String>());
			}
			
			//poner hoja
			arbol.get(pantallaIte).add(componenteIte);
		}
		
		logger.debug("EndososManager obtenerArbol arbol map: "+arbol);
		
		Item iArbol=new Item("root",null,Item.OBJ);
		iArbol.add("expanded",true);
		
		Item iArbolChildren=new Item("children",null,Item.ARR);
		
		iArbol.add(iArbolChildren);
		
		for(Entry<String,List<String>>pantallaIte:arbol.entrySet())
		{
			logger.debug("pantallaIte: "+pantallaIte);
			
			String      nombrePantallaIte      = pantallaIte.getKey();
			List<String>pantallaItecomponentes = pantallaIte.getValue();
			
			logger.debug("nombrePantallaIte: "      + nombrePantallaIte);
			logger.debug("pantallaItecomponentes: " + pantallaItecomponentes);
			
			Item iPantallaIte=new Item(null,null,Item.OBJ);
			
			iArbolChildren.add(iPantallaIte);
			
			iPantallaIte.add("expanded" , false);
			iPantallaIte.add("text"     , nombrePantallaIte);
			
			if(pantallaItecomponentes!=null&&pantallaItecomponentes.size()>0)
			{
				Item iPantallaIteChildren=new Item("children",null,Item.ARR);
				
				iPantallaIte.add(iPantallaIteChildren);
				
				for(String pantallaItecomponenteIte:pantallaItecomponentes)
				{
					logger.debug("pantallaItecomponenteIte: "+pantallaItecomponenteIte);
					
					if(pantallaItecomponenteIte!=null)
					{
						Item iPantallaIteComponenteIte=new Item(null,null,Item.OBJ);
						
						iPantallaIteChildren.add(iPantallaIteComponenteIte);
						
						iPantallaIteComponenteIte.add("text" , pantallaItecomponenteIte);
						iPantallaIteComponenteIte.add("leaf" , true);
					}
				}
			}			
		}
		
		logger.debug("EndososManager obtenerArbol arbol item: "+iArbol);
		
		return iArbol;
    }
	
	public Map<String,String> obtienePantalla(Map<String,String> params) throws Exception{
		
		List<Map<String, String>>  res = pantallasDAO.obtienePantalla(params);
		return res.get(0); 
    }
	
	@Override
	public List<ComponenteVO> recuperarComboDocs(String proceso)
	{
		long stamp = System.currentTimeMillis();
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarComboDocs @@@@@@"
				,"\n@@@@@@ stamp="   , stamp
				,"\n@@@@@@ proceso=" , proceso
				));
		
		List<ComponenteVO> lista = new ArrayList<ComponenteVO>();
		
		try
		{
			lista = pantallasDAO.obtenerComponentes(
					null//cdtiptra
					,null//cdunieco
					,null//cdramo
					,null//cdtipsit
					,null//estado
					,proceso//cdsisrol
					,"_C2_VENTANA_DOCS"//pantalla
					,"COMBO_DOCS"//seccion
					,null//orden
					);
		}
		catch(Exception ex)
		{
			logger.error(Utils.join("Error al recuperar combo de docs #",stamp),ex);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ stamp=" , stamp
				,"\n@@@@@@ lista=" , lista
				,"\n@@@@@@ recuperarComboDocs @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}

	///////////////////////////////
	////// getters y setters //////
	/*///////////////////////////*/
	public PantallasDAO getPantallasDAO() {
		return pantallasDAO;
	}

	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}
	/*///////////////////////////*/
	////// getters y setters //////
	///////////////////////////////
}