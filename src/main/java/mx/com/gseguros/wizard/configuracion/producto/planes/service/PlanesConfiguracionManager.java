package mx.com.gseguros.wizard.configuracion.producto.planes.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.model.MensajesVO;
import mx.com.gseguros.wizard.model.PlanesMPlanProVO;
/**
 * 
 * @author sergio.ramirez
 *
 */
public interface PlanesConfiguracionManager {
	/**
	 * 
	 * @param cdRamo
	 * @param cdTipSit 
	 * @return
	 * @throws ApplicationException
	 */
	public List<LlaveValorVO> getListaPlanes(String cdRamo, String cdTipSit)throws ApplicationException;

	/**
	 * 
	 * @param cdRamo
	 * @param cdPlan
	 * @param cdTipSit
	 * @return
	 * @throws ApplicationException
	 */
	public List<LlaveValorVO> getCoberturasPlanes(String cdRamo, String cdPlan,String cdTipSit) throws ApplicationException;
	
	/**
	 * 
	 * @param dsPlan
	 * @param cdRamo
	 * @param cdPlan
	 * @throws ApplicationException
	 */
	public void guardaPlanesConfiguracion(String cdRamo, String cdTipsit, String cdPlan,String dsPlan)throws ApplicationException;
	
	/**
	 * 
	 * @param cdRamo
	 * @param cdPlanTmp
	 * @param cdTipSit
	 * @param codigoCondicion
	 * @throws ApplicationException
	 */
	public MensajesVO guardaCoberturaPlanes(String cdRamo, String cdPlanTmp,String cdTipSit, String codigoCondicion, String obligatorio) throws ApplicationException;
	
	/**
	 * 
	 * @param cdRamo
	 * @param cdPlan
	 * @param dsPlan
	 * @throws ApplicationException
	 */
	public void editaPlan(String cdRamo, String cdPlan,String dsPlan) throws ApplicationException;
	/**
	 * 
	 * @param cdRamo
	 * @return List<RamaVO>
	 */
	public List<RamaVO> getRamaPlanes(String cdRamo, String cdTipSit) throws ApplicationException;
	
	/**
	 * 
	 * @param cdRamo
	 * @param cdTipSit
	 * @param cdPlan
	 * @throws ApplicationException
	 */
	public void eliminaPlan(String cdRamo, String cdTipSit, String cdPlan) throws ApplicationException;

	/**
	 * 
	 * @param planesMPlanProVO
	 * @return
	 * @throws ApplicationException
	 */
	public String borrarCoberturasPlan(PlanesMPlanProVO planesMPlanProVO) throws ApplicationException;
	
	
    public void modificaCoberturaPlan ( String p_cdgarant_i ,String  p_dsgarant_i ) throws ApplicationException;



}
