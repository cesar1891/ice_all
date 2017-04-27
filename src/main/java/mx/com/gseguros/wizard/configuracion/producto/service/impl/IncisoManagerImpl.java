package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import static mx.com.gseguros.wizard.configuracion.producto.dao.IncisoDAO.AGREGAR_INCISO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.dao.IncisoDAO;
import mx.com.gseguros.wizard.configuracion.producto.incisos.model.IncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.service.IncisoManager;

import org.apache.log4j.Logger;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que implementa los metodos para agregar, asociar, editar y obtener 
 * las listas de incisos asociados a un producto
 *
 */
public class IncisoManagerImpl extends AbstractManagerJdbcTemplateInvoke implements IncisoManager {

	private static Logger logger = Logger.getLogger(IncisoManagerImpl.class);
	
	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */
	
	//private Map endpoints;
	
	/**
	 * Implementacion que extrae todos los incisos asociados a un producto.
	 * 
	 * 
	 * @return List<IncisoVO> - Lista con la informacion de todos los
	 *         incisos asociados al producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<IncisoVO> incisosDelProducto(String codigoRamo, String codigoTipo) throws ApplicationException {

		HashMap params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDTIPSIT_I", codigoTipo);   
		 
		 List<IncisoVO> listaIncisosDelProducto = null;
		 
		 WrapperResultados result = this.returnBackBoneInvoke(params, IncisoDAO.OBTIENE_INCISOS_DEL_PRODUCTO);
			
		 listaIncisosDelProducto = (List<IncisoVO>) result.getItemList();
		 
        return listaIncisosDelProducto;
	}

	
	/**
	 * Implementacion que extrae una lista de  incisos para asociar a un producto.
	 * 
	 * 
	 * @return List<IncisoVO> - Lista con la informacion de los
	 *         incisos que se pueden asociar al producto solicitado.
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<IncisoVO> incisosJson() throws ApplicationException {
        	List<IncisoVO> listaIncisos = null;
	        HashMap<String, Object> params = new HashMap<String, Object>();
			
			WrapperResultados result = this.returnBackBoneInvoke(params, IncisoDAO.OBTIENE_INCISOS);
			
			listaIncisos = (List<IncisoVO>) result.getItemList();
	        
	        return listaIncisos;
	}
	
	/**
	 * Implementacion que agrega un inciso al catalogo.
	 * 
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD. 
	 */
	
	//*NOTA: Retorna un valor number de la bd para el MSG_ID pero en este momento no se esta manipulando
	
	@SuppressWarnings("unchecked")
	public WrapperResultados agregarInciso(IncisoVO inciso) throws ApplicationException{
        
        /*
        	logger.debug("claveIMPL-------->"+inciso.getCdtipsit());
			logger.debug("descripcionIMPL-->"+inciso.getDstipsit());
			logger.debug("subincisoIMPL---->"+inciso.getSwsitdec());
		*/
		//try {
        //    endpoint.invoke(inciso);
        //} catch (Exception e) {
        //    throw new ApplicationException("Error intentando insertar un nuevo inciso");
        //}
		
		//Implementacion usando JDBCTemplates
		Map params = new HashMap();
		params.put("P_CDTIPSIT", inciso.getCdtipsit());
		params.put("P_DSTIPSIT", inciso.getDstipsit());
		params.put("P_SWSITDEC", inciso.getSwsitdec());
		
		WrapperResultados resultado = returnBackBoneInvoke(params, AGREGAR_INCISO);
		
		return resultado;
    }

	/**
	 * Implementacion que asocia un inciso al producto.
	 * 
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.  
	 */
	//*NOTA: Retorna un valor number de la bd para el MSG_ID pero en este momento no se esta manipulando
	public void asociarInciso(IncisoVO incisoAsociado) throws ApplicationException {
		
		HashMap params = new HashMap<String, String>();
		params.put("pv_cdramo_i", incisoAsociado.getCdramo());
		params.put("pv_nmsituac_i", incisoAsociado.getNmsituac());
		params.put("pv_cdtipsit_i", incisoAsociado.getCdtipsit());
		params.put("pv_swobliga_i", incisoAsociado.getSwobliga());
		params.put("pv_swinsert_i", incisoAsociado.getSwinsert());
		params.put("pv_cdagrupa_i", "0");
		params.put("pv_dsriesgo_i", incisoAsociado.getDsriesgo());
		 
		 this.returnBackBoneInvoke(params, IncisoDAO.ASOCIAR_INCISO);
			
	}
	
	public void eliminarInciso(IncisoVO inciso)throws ApplicationException {
		
		HashMap params = new HashMap<String, String>();
		params.put("pv_cdramo_i", inciso.getCdramo());
		params.put("pv_cdtipsit_i", inciso.getCdtipsit());   
		 
		this.returnBackBoneInvoke(params, IncisoDAO.ELIMINAR_INCISO);
			
	}

}
