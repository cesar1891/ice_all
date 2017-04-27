package mx.com.gseguros.wizard.configuracion.producto.planes.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.wizard.configuracion.producto.dao.ProductoDAO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.planes.service.PlanesConfiguracionManager;
import mx.com.gseguros.wizard.model.MensajesVO;
import mx.com.gseguros.wizard.model.PlanesMPlanProVO;

/**
 * 
 * @author sergio.ramirez
 *
 */
public class PlanesConfiguracionManagerImpl extends AbstractManagerJdbcTemplateInvoke implements PlanesConfiguracionManager{
	
	/**
	 *@param cdRamo
	 *@return List<LlaveValorVO>lista
	 *@exception DaoException
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getListaPlanes(String cdRamo, String cdTipSit)throws ApplicationException {
		Map params= new HashMap<String, String>();
		params.put("PV_CDRAMO_I", cdRamo);
		params.put("PV_CDTIPSIT_I", cdTipSit);
		List<LlaveValorVO> lista= null;
		try {
			WrapperResultados result = this.returnBackBoneInvoke(params,
					ProductoDAO.OBTIENE_LISTA_PLANES_CONFIGURACION);
			lista=(List<LlaveValorVO>) result.getItemList();
		} catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error retrieving data");
		}	
		return lista;
	}
	/**
	 * @param cdRamo
	 * @param cdPlan
	 * @param cdTipSit
	 * @return List<LlaveValorVO>planes
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<LlaveValorVO> getCoberturasPlanes(String cdRamo, String cdPlan,String cdTipSit) throws ApplicationException {
		Map parameters = new HashMap<String, String>();
		parameters.put("pv_cdramo_i", cdRamo);
		parameters.put("pv_cdplan_i", cdPlan);
		parameters.put("pv_cdtipsit_i", cdTipSit);
		List<LlaveValorVO> planes= null;
		try {
			WrapperResultados result = this.returnBackBoneInvoke(parameters, ProductoDAO.OBTIENE_LISTA_COBERTURAS_PLANES_CONFIGURACION);
			planes= (List<LlaveValorVO>) result.getItemList();
		} catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error retrieving data");
		}
		return planes;
	}
	
	/**
	 *@param cdTipSit
	 *@param cdRamo
	 *@param cdPlan
	 *@throws DaoException
	 *
	 */
	@SuppressWarnings("unchecked")
	public void guardaPlanesConfiguracion(String cdRamo, String cdTipsit, String cdPlan,String dsPlan) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("P_PRODUCTO", cdRamo);
		params.put("P_CVE_PLAN", cdPlan);
		params.put("P_DES_PLAN", dsPlan);
		params.put("p_cve_sit", cdTipsit);
		
		logger.debug("PlanConfiguracionManagerImpl.guardaPlanesConfiguracion()" );
		logger.debug("params=" + params );
		
		try{
			returnBackBoneInvoke(params, "GUARDA_PLAN_CONFIGURACION");			
		}catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error during transaction");
		}
	
	}
	
	public void editaPlan(String cdRamo, String cdPlan,String dsPlan) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("P_PRODUCTO", cdRamo);
		params.put("P_CVE_PLAN", cdPlan);
		params.put("P_DES_PLAN", dsPlan);
		
		logger.debug("PlanConfiguracionManagerImpl.editaPlan()" );
		logger.debug("params=" + params );
		
		try{
			returnBackBoneInvoke(params, "EDITA_PLAN_CONFIGURACION");
		}catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error during transaction");
		}
	
	}
	
	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public MensajesVO guardaCoberturaPlanes(String cdRamo, String cdPlanTmp,String cdTipSit, String codigoCondicion, String obligatorio)throws ApplicationException {
		MensajesVO mensajeVO = new MensajesVO();
		Map parameters = new HashMap<String, String>();
		parameters.put("P_PRODUCTO", cdRamo);
		parameters.put("P_PLAN", cdPlanTmp);
		parameters.put("P_SITUACION", cdTipSit);
		parameters.put("P_GARANTIA", codigoCondicion);
		parameters.put("P_OBLIG", obligatorio);
		
		try {
			WrapperResultados res = returnBackBoneInvoke(parameters, "GUARDA_COBERTURA_PLAN_CONFIGURACION");
			mensajeVO.setMsgId(res.getMsgId());
			mensajeVO.setMsgText(res.getMsgText());
			mensajeVO.setTitle(res.getMsgTitle());
			
		} catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error during transaction");
		}
		return mensajeVO;
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<RamaVO> getRamaPlanes(String cdRamo, String cdTipSit)throws ApplicationException {
		Map params= new HashMap<String, String>();
		params.put("pv_cdramo", cdRamo);
		params.put("pv_cdtipsit_i", cdTipSit);
		List<RamaVO> ramas = null;
		try {
			WrapperResultados result = this.returnBackBoneInvoke(params,
					ProductoDAO.OBTIENE_LISTA_PLANES_RAMAS);
			ramas=(List<RamaVO>) result.getItemList();
		} catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error retrieving data");
		}	
		return (List<RamaVO>)ramas;
	}
	
	
	@SuppressWarnings("unchecked")
	public void eliminaPlan(String cdRamo, String cdTipSit, String cdPlan) throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("pv_cdramo_i", cdRamo);
		params.put("pv_cdplan_i", cdPlan);
		params.put("pv_cdtipsit_i", cdTipSit);
		try{
			returnBackBoneInvoke(params, "ELIMINA_PLAN_CONFIGURACION");
		}catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error during transaction");
		}
	}
	
	/**
	 * Metodo que realiza la eliminacion d un plan seleccionado.
	 * Usa el Store Procedure PKG_CONFG_CUENTA.P_BORRAR_PLANPRO
	 * 
	 * @param PlanesMPlanProVO planesMPlanProVO
	 * 
	 * @return Mensaje asociado en respuesta a la ejecucion del servicio.
	 * 
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	public String borrarCoberturasPlan(PlanesMPlanProVO planesMPlanProVO) throws ApplicationException {

			HashMap map = new HashMap ();
			map.put("p_producto", planesMPlanProVO.getCdRamo());
			map.put("p_plan", planesMPlanProVO.getCdPlan());
			map.put("p_situacion", planesMPlanProVO.getCdTipSit());
			map.put("p_garantia", planesMPlanProVO.getCdGarant());
			map.put("p_oblig", planesMPlanProVO.getSwOblig());

            WrapperResultados res =  returnBackBoneInvoke(map,"BORRAR_PLANPRO");
            return res.getMsgText();
	}
	


    public  void modificaCoberturaPlan(  String p_cdgarant_i ,String  p_dsgarant_i   )throws ApplicationException {
	    
    	Map params = new HashMap<String, String>();
    	params.put("p_cdgarant_i", p_cdgarant_i );
    	params.put("p_dsgarant_i", p_dsgarant_i);
      
		try{
			returnBackBoneInvoke(params, "MODIFICA_COBERTURA_PLAN");
		}catch (Exception e) {
			e.getMessage();
			throw new ApplicationException("Error "  + e.getMessage() );
		}
    }
	
}