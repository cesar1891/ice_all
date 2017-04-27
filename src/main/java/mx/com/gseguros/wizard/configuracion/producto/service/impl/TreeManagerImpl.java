/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import static mx.com.gseguros.wizard.configuracion.producto.dao.ProductoDAO.BUSCAR_PRODUCTOS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.dao.ProductoDAO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TreeManager;

import org.apache.log4j.Logger;

/**
 * @author Adolfo
 * 
 */
public class TreeManagerImpl extends AbstractManagerJdbcTemplateInvoke implements TreeManager {

	/**
	 * Logger de la clase.
	 */
	private static Logger logger = Logger.getLogger(TreeManagerImpl.class);

	//private Map<String, Endpoint> endpoints;

	/**
	 * @return the endpoints
	 */
//	public Map<String, Endpoint> getEndpoints() {
//		return endpoints;
//	}

	/**
	 * @param endpoints
	 *            the endpoints to set
	 */
//	public void setEndpoints(Map<String, Endpoint> endpoints) {
//		this.endpoints = endpoints;
//	}

	private List<RamaVO> crearObjetos(int nivel, String tipoObjeto,
			List<RamaVO> ramas) {

		List<RamaVO> resultado = new ArrayList<RamaVO>();
		int contador = 0;
		for (RamaVO ramaVO : ramas) {

			ramaVO.setQtip(ramaVO.getCodigoObjeto());
			ramaVO.setQtipTitle(ramaVO.getText());
			ramaVO.setNivel(nivel);
			ramaVO.setTipoObjeto(tipoObjeto);
			ramaVO.setChildren(new RamaVO[0]);
			ramaVO.setPosicion(contador);
			resultado.add(ramaVO);
			contador++;
		}

		return resultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.TreeManager#obtenerProductos()
	 */
	public List<RamaVO> obtenerProductos() throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		resultado = getAllBackBoneInvoke(new HashMap(), BUSCAR_PRODUCTOS);
		
		return crearObjetos(1, "Producto", resultado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.TreeManager#obtenerNiveles(java.lang.String,
	 *      java.util.ArrayList)
	 */
	public List<RamaVO> obtenerNiveles(String tipoObjeto,
			ArrayList<String> parametros) throws ApplicationException {
		int nivel = 3;
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		Map<String, String> params = new HashMap<String, String>();
		params.put("pv_cdramo", parametros.get(0));
		String vm = null;
		try {
			if ("AtributosVariables".equals(tipoObjeto)) {
				if (parametros.size() == 1)
					vm = ProductoDAO.OBTENER_RAMA_ATRIBUTOS_PRODUCTO;
				else if (parametros.size() == 2) {
					vm = ProductoDAO.OBTENER_ATRIBUTOS_SITUACION;
					params.put("pv_cdtipsit_i", parametros.get(1));
					nivel = 5;
				}
				else if (parametros.size() == 4) {
					vm = ProductoDAO.OBTENER_ATRIBUTOS_GARANTIA;
					params.put("pv_cdtipsit_i", parametros.get(1));
//					params.put("codigoPlan", parametros.get(2));
					params.put("pv_cdgarant_i", parametros.get(3));
					
					logger.debug("SITU: " + parametros.get(1));
					logger.debug("GARA: " + parametros.get(3));
					
					
					nivel = 9;
					/*resultado = new ArrayList<RamaVO>();
					RamaVO rama= null;
					for(int i = 0; i<5 ; i++){
						rama= new RamaVO();
						rama.setText("Atributo Variable"+i);
						rama.setCodigoObjeto(Integer.toString(i));
						resultado.add(rama);
					}
					nivel = 9;
					*/
				}
			}else
			if ("ConceptoGlobal".equals(tipoObjeto)) {
				vm = ProductoDAO.OBTENER_RAMA_CONCEPTOS_GLOBALES_PRODUCTO;
			}else
			if ("Inciso".equals(tipoObjeto)) {
				vm = ProductoDAO.OBTENER_RAMA_SITUACION;
			}else
			if ("DatosFijos".equals(tipoObjeto)) {
				vm = ProductoDAO.OBTENER_RAMA_DATOS_FIJOS_PRODUCTO;
				
			}else
			if ("Rol".equals(tipoObjeto)) {

				params.put("pv_cdtipsit_i", "");
				
				if (parametros.size() == 1)
					params.put("pv_cdnivel_i", "0");
				else if (parametros.size() == 2) {
					params.put("pv_cdnivel_i", "1");
					params.put("pv_cdtipsit_i", parametros.get(1));
					nivel = 5;
				} /*else if (parametros.size() == 3)
					params.put("nivel", "2");*/

				vm = ProductoDAO.OBTENER_RAMA_ROLES_PRODUCTO;
			}else
			if ("Cobertura".equals(tipoObjeto)) {
				logger.debug("ENTRA A COBERTURA");
				params.put("pv_cdtipsit_i", parametros.get(1));
				params.put("pv_cdplan_i", parametros.get(2));
				vm = ProductoDAO.OBTENER_GARANTIA;
				//nivel = 5;
				/*resultado = new ArrayList<RamaVO>();
				RamaVO rama= null;
				for(int i = 0; i<5 ; i++){
					rama= new RamaVO();
					rama.setText("cobertura"+i);
					rama.setCodigoObjeto(Integer.toString(i));
					resultado.add(rama);
				}*/
				nivel = 7;
			}else if("Planes".equals(tipoObjeto)){	
				logger.debug("entro al if de planes");
				vm = ProductoDAO.OBTIENE_LISTA_PLANES_RAMAS;
				params.put("pv_cdtipsit_i", parametros.get(1));				
				nivel = 5;
			} else if ("Objeto".equals(tipoObjeto)) {
				vm = ProductoDAO.OBTENER_OBJETOS;
				params.put("pv_cdtipsit_i", parametros.get(1));
				nivel = 5;
			}
			
			
			if (vm != null) {
				WrapperResultados result = this.returnBackBoneInvoke(params, vm);
				resultado = (ArrayList<RamaVO>) result.getItemList();
			}

		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTENER_RAMA_SITUACION'", bae);
			throw new ApplicationException(
					"Error al cargar los tipos de seguro desde el sistema");
		}

		return crearObjetos(nivel, tipoObjeto, resultado);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.biosnet.ice.wizard.productos.service.TreeManager#obtenerEstructuraProducto()
	 */
	public List<RamaVO> obtenerEstructuraProducto() throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		RamaVO ramaVO = new RamaVO();

		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Riesgo");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(0);
		ramaVO.setTipoObjeto("Inciso");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Datos Variables");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(1);
		ramaVO.setTipoObjeto("AtributosVariables");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Datos Fijos");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("DatosFijos");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Conceptos Globales");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(3);
		ramaVO.setTipoObjeto("ConceptoCobertura");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Roles");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(4);
		ramaVO.setTipoObjeto("Rol");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		/*ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Suma Asegurada");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(5);
		ramaVO.setTipoObjeto("SumaAsegurada");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		*/
		
		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Variables Temporales Por Producto");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(6);
		ramaVO.setTipoObjeto("VariableTemporalProducto");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Reglas De Validaci\u00F3n");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(2);
		ramaVO.setPosicion(7);
		ramaVO.setTipoObjeto("ReglaValidacion");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		return resultado;
	}

	public List<RamaVO> obtenerEstructuraSituacion()
			throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		RamaVO ramaVO = new RamaVO();
/*
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Coberturas");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(0);
		ramaVO.setTipoObjeto("Cobertura");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		*/
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Datos Variables");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(1);
		ramaVO.setTipoObjeto("AtributosVariables");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Planes");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("Planes");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Roles");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("Rol");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Objetos");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("Objeto");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		/*ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Suma Asegurada");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(4);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("SumaAsegurada");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		*/
		
		return resultado;
	}

	public List<RamaVO> obtenerEstructuraObjetos()
			throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		RamaVO ramaVO = new RamaVO();

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Datos Variables");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(6);
		ramaVO.setPosicion(0);
		ramaVO.setTipoObjeto("AtributosVariables");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		return resultado;
	}

	public List<RamaVO> obtenerEstructuraCobertura()
			throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		RamaVO ramaVO = new RamaVO();

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Datos Variables");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(8);
		ramaVO.setPosicion(0);
		ramaVO.setTipoObjeto("AtributosVariables");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Suma Asegurada");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(8);
		ramaVO.setPosicion(2);
		ramaVO.setTipoObjeto("SumaAsegurada");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
				
		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Conceptos Por Cobertura");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(8);
		ramaVO.setPosicion(1);
		ramaVO.setTipoObjeto("ConceptoCobertura");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);
		
		return resultado;
	}

	public List<RamaVO> obtenerEstructuraPlanes()
			throws ApplicationException {
		List<RamaVO> resultado = new ArrayList<RamaVO>();

		RamaVO ramaVO = new RamaVO();

		ramaVO = new RamaVO();
		ramaVO.setId("");
		ramaVO.setCodigoObjeto("");
		ramaVO.setText("Coberturas");
		ramaVO.setQtip(ramaVO.getCodigoObjeto());
		ramaVO.setQtipTitle(ramaVO.getText());
		ramaVO.setNivel(6);
		ramaVO.setPosicion(0);
		ramaVO.setTipoObjeto("Cobertura");
		ramaVO.setChildren(new RamaVO[0]);
		resultado.add(ramaVO);

		return resultado;
	}

}