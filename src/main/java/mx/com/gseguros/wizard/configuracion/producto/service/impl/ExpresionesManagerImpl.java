package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import static mx.com.gseguros.wizard.dao.ExpresionesDAO.OBTIENE_TABLA_CINCO_CLAVES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.HojaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ExpresionesManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ExpresionesManagerImpl extends AbstractManagerJdbcTemplateInvoke implements ExpresionesManager {

	private static Logger logger = Logger
			.getLogger(ExpresionesManagerImpl.class);
	/**
	 * Mapa en el cual se introducen los Manager's para ser extraidos y
	 * utilizados como servicios.
	 */
	//private Map<String, Endpoint> endpoints;

	@SuppressWarnings("unchecked")
	public boolean agregarVariables(List<VariableVO> listaVariables,
			int codigoExpresion, String ottiporg) throws ApplicationException {
		
		for(VariableVO variable : listaVariables){
			
			WrapperResultados mensaje = null;
			try {
				HashMap<String, Object> params =  new HashMap<String, Object>();
				params.put("PV_CDEXPRES_I", codigoExpresion);
				params.put("PV_CDVARIAB_I", variable.getCodigoVariable());
				params.put("PV_OTTABLA_I", variable.getTabla());
				params.put("PV_OTSELECT_I", variable.getDescripcionColumna());
				params.put("PV_SWFORMAT_I", variable.getSwitchFormato());
				
				mensaje = returnBackBoneInvoke(params, "INSERTAR_LISTA_VARIABLES");
				
			} catch (Exception bae) {
				logger.error("Exception in invoke 'INSERTAR_LISTA_VARIABLES'", bae);
			}
			
			if( mensaje!= null && mensaje.getMsgTitle() != null){
				logger.debug("HHH MSGSS: "+mensaje.getMsgTitle());
					if("1".equals(mensaje.getMsgTitle()))return false;
			}
			
		}
		
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public int insertarExpresion(ExpresionVO expresionVO, boolean success)
			throws ApplicationException {
		int id = 0;
		try {
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("PV_CDEXPRES_I", expresionVO.getCodigoExpresion());
			params.put("PV_OTEXPRES_I", expresionVO.getOtExpresion());
			params.put("PV_SWRECALC_I", expresionVO.getSwitchRecalcular());
			params.put("pv_OTTIPORG_i", expresionVO.getOttiporg());
			params.put("pv_OTTIPEXP_i", expresionVO.getOttipexp());
			WrapperResultados res = returnBackBoneInvoke(params, "INSERTAR_EXPRESION");
			
			id = Integer.parseInt((String)res.getItemMap().get("PV_SEC_CDEXPRES"));
			logger.debug("Este es el id que regresa del endpoint" + id);
			if (id == 0 && expresionVO.getCodigoExpresion() !=0 ) {
				id = expresionVO.getCodigoExpresion();
			}
		} catch (Exception bae) {
			success = false;
			logger.error("Exception in invoke 'INSERTAR_EXPRESION'", bae);
			throw new ApplicationException(
					"Error al insertar los datos de la expresion en el sistema");
		}
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public int obtieneSecuenciaExpresion() throws ApplicationException {
		int id = 0;
		try {

			HashMap<String, Object> params =  new HashMap<String, Object>();
			
			WrapperResultados res = returnBackBoneInvoke(params,"OBTIENE_SECUENCIA_EXPRESION");
			id= Integer.parseInt((String)res.getItemMap().get("PV_SEC_CDEXPRES"));
			logger.debug("Este es el id que regresa del endpoint" + id);
			
		} catch (Exception bae) {
			logger.error("Exception in invoke 'OBTIENE_SECUENCIA_EXPRESION'", bae);
			throw new ApplicationException("Error al obtener la secuencia de la expresion del sistema");
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public boolean agregarClave(List<ClaveVO> listaClaves, int codigoExpresion,
			String codigoVariable, String ottiporg) throws ApplicationException {
		boolean success = true;
		for(ClaveVO clave : listaClaves){
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDEXPRES_I", codigoExpresion);
			params.put("PV_CDVARIAB_I", codigoVariable);
			params.put("PV_OTEXPRES_I", clave.getExpresion());
			params.put("PV_SWRECALC_I", clave.getRecalcular());
			params.put("PV_OTSECUEN_I", clave.getCodigoSecuencia());
			params.put("PV_OTTIPORG_I", ottiporg);
			params.put("PV_OTTIPEXP_I", "K");
			params.put("pv_cdexpres_key_i", clave.getCodigoExpresionKey());
			
			WrapperResultados mensaje = null;
			logger.debug(" HHHH Los parametros para mandar a agregarClave() son :"+ params);
			try {
				mensaje = returnBackBoneInvoke(params, "INSERTAR_LISTA_CLAVES");
				logger.debug("agrego bien las claves");
	
			} catch (Exception bae) {
				success = false;
				logger.error("Exception in invoke 'INSERTAR_LISTA_CLAVES'", bae);
			}
			//logger.debug("MESAJE para: "+clave.getCodigoSecuencia()+" es: "+mensaje.getMsgTitle());
			if( mensaje!= null && mensaje.getMsgTitle() != null){
				if("1".equals(mensaje.getMsgTitle())){
					success = false;
					break;
				}
			}
		}
		return success;
	}

	@SuppressWarnings("unchecked")
	public void eliminarVariableLocalExpresion(Map<String, String> params) throws ApplicationException {
		logger.debug("fuera del try");
		try {
			logger.debug("dentro del manager");
			returnBackBoneInvoke(params, "ELIMINAR_VARIABLE_LOCAL_EXPRESION");
			
			logger.debug("elimino bien");
		} catch (Exception bae) {
			logger.error("Exception in invoke 'ELIMINAR_VARIABLE_LOCAL_EXPRESION'", bae);
			throw new ApplicationException("Error al eliminar la variable local de la expresion del sistema");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HojaVO> funcionesArbol() throws ApplicationException {
		List<HojaVO> listaFuncionesJson = new ArrayList<HojaVO>();

		try {
			HashMap<String, Object> params =  new HashMap<String, Object>();
			WrapperResultados res =  returnBackBoneInvoke(params, "LISTA_FUNCIONES");
			listaFuncionesJson = (ArrayList<HojaVO>)res.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaFuncionesJson;
	}

	@SuppressWarnings("unchecked")
	public List<HojaVO> variablesTemporalesArbol() throws ApplicationException {
		List<HojaVO> listaVariablesTemporalesJson = new ArrayList<HojaVO>();

		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			WrapperResultados res = returnBackBoneInvoke(params, "LISTA_VARIABLES_TEMPORALES");
			listaVariablesTemporalesJson = (ArrayList<HojaVO>) res.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaVariablesTemporalesJson;
	}

	@SuppressWarnings("unchecked")
	public List<RamaVO> camposDelProductoArbol() throws ApplicationException {
		List<RamaVO> ramas = new ArrayList<RamaVO>();
		RamaVO rama = null;
		List<HojaVO> listaCamposDelProductoJson = null;
		for (int j = 0; j < 5; j++) {
			listaCamposDelProductoJson = new ArrayList<HojaVO>();
			rama = new RamaVO();
			rama.setAllowDelete(false);
			rama.setCls("file");
			rama.setExpanded(true);
			rama.setHref("../definicion/PrincipalProductos.action");
			rama.setHrefTarget("target");
			rama.setId("id"+j);
			rama.setQtip("hey"+j);
			rama.setQtipTitle("tile"+j);
			rama.setText("text"+j);
			
			HojaVO hojaVO = null;
			for (int i = 0; i < 10; i++) {
				hojaVO = new HojaVO();
				hojaVO.setCls("file");
				hojaVO.setId("id-" + Integer.toString(i));
				hojaVO.setLeaf(true);
				hojaVO.setText("Menu campos " + Integer.toString(i));
				hojaVO.setDescripcion("Descripcion de " + hojaVO.getText());
				hojaVO.setFuncion("campos" + Integer.toString(i));
				listaCamposDelProductoJson.add(hojaVO);
			}
			rama.setChildren(listaCamposDelProductoJson.toArray());
			ramas.add(rama);
		}
		return ramas;
	}

	/**
	 * M�todo para obtener los valores de las tablas en la pantalla Wizard de Parametrizaci�n, 
	 * pesta�a Cat�logos, opci�n Tablas de Apoyo, opci�n Tabla Cinco Valores. 
	 * @param start
	 * @param limit
	 * @return pagedBackBoneInvoke(map, endpointName, start, limit)
	 * @author augusto.perez
	 */
	@SuppressWarnings("unchecked")
	public PagedList listaTabla(int start, int limit) throws ApplicationException {
		Map map = new HashMap();
		
        String endpointName = OBTIENE_TABLA_CINCO_CLAVES;
        return pagedBackBoneInvoke(map, endpointName, start, limit);
	}

	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> listaColumna(String idTabla)
			throws ApplicationException {
		List<LlaveValorVO> listaColumnaJson = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDTABLA_I", idTabla);
			WrapperResultados res = returnBackBoneInvoke(params, "LISTA_COLUMNA");
			listaColumnaJson = (List<LlaveValorVO>) res.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaColumnaJson;
	}

	@SuppressWarnings("unchecked")
	public List<ClaveVO> listaClave(String idTabla) throws ApplicationException {
		logger.debug("Entro a expresionesManagerImpl ==>lista clave + idTabla="
				+ idTabla);
		List<ClaveVO> listaClaveJson = null;
		try {
			HashMap<String, Object> params =  new HashMap<String, Object>(); 
			params.put("PV_CDTABLA_I", idTabla);
			WrapperResultados res = returnBackBoneInvoke(params, "LISTA_CLAVE");
			listaClaveJson = (List<ClaveVO>) res.getItemList();
			if(listaClaveJson != null && !listaClaveJson.isEmpty() ){
				//List<Iteger> clavesARemover = new ArrayList<Integer>();
				List<ClaveVO> claves = new ArrayList<ClaveVO>();
				for(ClaveVO cvo : listaClaveJson){
					if( !( cvo.getClave() != null && StringUtils.isNotBlank( cvo.getClave() ) ) ){
						claves.add(cvo);
						//clavesARemover.add(Integer.parseInt(cvo.getCodigoSecuencia()));
					}					
				}
				if(!claves.isEmpty())
					listaClaveJson.removeAll(claves);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("listaClaveJson="+listaClaveJson);
		logger.debug("listaClaveJson.isEmpty()="+listaClaveJson.isEmpty());
		return listaClaveJson;
	}

	@SuppressWarnings("unchecked")
	public ClaveVO obtieneClave(String dsclave)
			throws Exception {
		ClaveVO cVO = null;

		if (cVO == null) {
			cVO = new ClaveVO();
			cVO.setClave(dsclave);
			cVO.setExpresion("");
			cVO.setRecalcular("N");
		}
		logger.debug("obtieneClave + cVO.getClave" + cVO.getClave());
		return cVO;
	}

	@SuppressWarnings("unchecked")
	public List<ClaveVO> obtieneClaves(VariableVO variableLocal)throws ApplicationException{
		List<ClaveVO> result =null;
		try{
			HashMap<String, Object> params = new HashMap<String, Object>(); 
			params.put("PV_CDEXPRES_I", variableLocal.getCodigoExpresion());
			params.put("PV_CDVARIAB_I", variableLocal.getCodigoVariable());
			params.put("PV_OTTABLA_I", variableLocal.getTabla());
			params.put("PV_OTSELECT_I", variableLocal.getColumna());
			WrapperResultados res = returnBackBoneInvoke(params, "CLAVES_VARIABLE_LOCAL");
		 	result= (List<ClaveVO>)res.getItemList();
		}catch(Exception bea){
			
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<VariableVO> listaComboVariables() throws ApplicationException {
		List<VariableVO> listaComboVariables = new ArrayList<VariableVO>();
		return listaComboVariables;
	}

	@SuppressWarnings("unchecked")
	public List<ClaveVO> listaPropertyGrid() throws ApplicationException {
		List<ClaveVO> listaPropertyGridJson = new ArrayList<ClaveVO>();

		ClaveVO variableVO = new ClaveVO();
		variableVO.setClave("1");
		variableVO.setExpresion("2");
		variableVO.setRecalcular("S");

		listaPropertyGridJson.add(variableVO);
		return listaPropertyGridJson;
	}

	@SuppressWarnings("unchecked")
	public List<ExpresionVO> obtieneExpresion(int codigoExpresion) {
		List<ExpresionVO> listaExpresion = new ArrayList<ExpresionVO>();
		
		try {
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("PV_CDEXPRES_I", codigoExpresion);
			WrapperResultados res =  returnBackBoneInvoke(params, "OBTIENE_EXPRESION");
			listaExpresion = (ArrayList<ExpresionVO>) res.getItemList();
			if(listaExpresion!=null && !listaExpresion.isEmpty()){
				if(listaExpresion.get(0).getSwitchRecalcular()==null)
					listaExpresion.get(0).setSwitchRecalcular("N");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaExpresion;
	}
	
	@SuppressWarnings("unchecked")
	public List<VariableVO> obtieneVariableExpresion(int codigoExpresion) throws ApplicationException {
		List<VariableVO> listaVariableExpresion = new ArrayList<VariableVO>();
		
		try {
			HashMap<String, Object> params =  new HashMap<String, Object>();
			params.put("PV_CDEXPRES_I", codigoExpresion);
			
			WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_VARIABLE_EXPRESION");
			listaVariableExpresion = res.getItemList();
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaVariableExpresion;
	}

	/**
	 * 
	 * @param codigoExpresion
	 * @param expresion
	 * @param tipoExpresion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public WrapperResultados validarExpresion(int codigoExpresion, String expresion, String tipoExpresion , String cdVariable) throws Exception
	{	
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("pv_expresion_i"  , expresion);
		params.put("pv_cdexpres_i"   , codigoExpresion + "");
		params.put("pv_ottipexp_i"   , tipoExpresion);
		params.put("pv_ottiporg_i"   , "0");
		params.put("pv_otlength_i"   , "0");
		params.put("pv_otdepth_i"    , "0");
		params.put("pv_lang_code_o"  , null);
		params.put("pv_cdvariable_i" , cdVariable);
		
		WrapperResultados mensaje = null;
		try
		{
			mensaje =  returnBackBoneInvoke(params, "VALIDA_EXPRESION");
		}
		catch (Exception e)
		{
			logger.error("Error al validar la expresion");
			throw new Exception("Validacion de expresiones", e);
		}
		return mensaje;
	}
	
	@SuppressWarnings("unchecked")
	public boolean existeExpresion(String codigoExpresion) throws Exception {
		List<ExpresionVO> listaExpresion = new ArrayList<ExpresionVO>();
		boolean existeExpresion = false;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDEXPRES_I", codigoExpresion);
			
			WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_EXPRESION");
			
			listaExpresion = (ArrayList<ExpresionVO>) res.getItemList();
			if(listaExpresion!=null && !listaExpresion.isEmpty()){
				existeExpresion = true;
				logger.debug("La expresion con codigo " + codigoExpresion + " SI existe en BD");
			}else{
				logger.debug("La expresion con codigo " + codigoExpresion + " NO existe en BD");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return existeExpresion;
	}
	
	/**
	 * @param endpoints
	 *            the endpoints to set
	 */
//	public void setEndpoints(Map<String, Endpoint> endpoints) {
//		this.endpoints = endpoints;
//	}

}