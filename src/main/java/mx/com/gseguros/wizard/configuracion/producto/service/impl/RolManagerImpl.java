package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolAtributoVariableVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolVO;
import mx.com.gseguros.wizard.configuracion.producto.service.RolManager;
import mx.com.gseguros.wizard.dao.WizardDAO;
import mx.com.gseguros.wizard.model.MensajesVO;

import org.apache.log4j.Logger;


public class RolManagerImpl implements RolManager {
	
	private WizardDAO wizardDAO;

	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}

	private static Logger logger = Logger.getLogger(ProductoManagerImpl.class);

	public List<RolAtributoVariableVO> atributosVariablesJson(int codigoRamo,
			String codigoTipoSituacion, String codigoRol, int codigoNivel)
			throws ApplicationException {

		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDTIPSIT_I", codigoTipoSituacion);
		params.put("PV_CDROL_I", codigoRol);
		params.put("PV_CDNIVEL_I", codigoNivel);

		List<RolAtributoVariableVO> listaAtributos = null;
		try {
			listaAtributos = wizardDAO.atributosVariablesRol(params);

		} catch (Exception e) {
			throw new ApplicationException(
					"Error regresando lista de atributos variables");
		}
		return listaAtributos;
	}

	public List<LlaveValorVO> catalogoAtributosVariablesJson()
			throws ApplicationException {
		List<LlaveValorVO> catalogo = null;
		try {
			HashMap<String, Object> params =  new HashMap<String, Object>();
			catalogo = wizardDAO.catalogoAtributosVariablesJson(params);

			// List<ComboVO> item = new ArrayList<ComboVO>();
			// ComboVO cvo = null;
			// for (int i = 0; i < 5; i++) {
			// cvo = new ComboVO();
			// cvo.setKey("1");
			// cvo.setValue("Hallo");
			//	
			// item.add(cvo);
			// }
			// catalogos = item;

			if (catalogo == null) {
				throw new ApplicationException(
						"No exite ningun atributo variable");
			} else {
				logger
						.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! catalogoAtributosVariables size"
								+ catalogo.size());
			}

		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_ATRIBUTOS_VARIABLES'",
					bae);
			throw new ApplicationException(
					"Error al cargar el catalogo de atributos variables desde el sistema");
		}
		return catalogo;
	}

	public List<LlaveValorVO> catalogoObligatorioJson()
			throws ApplicationException {
		
		List<LlaveValorVO> catalogo = null;

		catalogo = new ArrayList<LlaveValorVO>();

		LlaveValorVO obligatoriedad = new LlaveValorVO();
		obligatoriedad.setKey("O");
		obligatoriedad.setValue("OPCIONAL");
		catalogo.add(obligatoriedad);

/*		obligatoriedad = new LlaveValorVO();
		obligatoriedad.setKey("P");
		obligatoriedad.setValue("PROHIBIDO");
		catalogo.add(obligatoriedad);
*/
		obligatoriedad = new LlaveValorVO();
		obligatoriedad.setKey("R");
		obligatoriedad.setValue("REQUERIDO");
		catalogo.add(obligatoriedad);

		if (catalogo == null) {
			throw new ApplicationException(
					"No exite ningun tipo de Obligatoriedad");
		} else {
			logger
					.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! catalogo Obligatorio size"
							+ catalogo.size());
		}

		return catalogo;
	}

	public List<LlaveValorVO> catalogoRolesJson() throws ApplicationException {
		List<LlaveValorVO> catalogo = null;
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			catalogo = wizardDAO.obtieneCatalogoRoles(params);
			
			if (catalogo == null) {
				throw new ApplicationException("No exite ningun Rol");
			} else {
				logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!catalogoRoles size"
						+ catalogo.size());
			}

		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_ROLES'", bae);
			throw new ApplicationException(
					"Error al cargar los roles desde el sistema");
		}
		return catalogo;
	}

	public void agregaRolCatalogo(LlaveValorVO rol) throws ApplicationException {
		
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("PV_CDROL_I", rol.getKey());
			params.put("PV_DSROL_I", rol.getValue());
			
			wizardDAO.agregaRolCatalogo(params);
			
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_ROL_CATALOGO", bae);
			throw new ApplicationException(
					"Error al insertar nuevo rol al catalogo");
		}
	}

	public void agregaAtributoVariableRol( List<RolAtributoVariableVO> listaAtributos)
			throws ApplicationException {
		
		try {
			if (logger.isDebugEnabled()) {
				for (RolAtributoVariableVO atribu : listaAtributos) {

					logger.debug("RAMOLISTA//" + atribu.getCdRamo());
					logger.debug("NIVELLISTA//" + atribu.getCodigoNivel());
					logger.debug("ROLLISTA//" + atribu.getCodigoRol());
					logger.debug("CODATRIBUVARIABLELISTA//"
							+ atribu.getCodigoAtributoVariable());
					logger.debug("CODVALORESLISTA//"
							+ atribu.getCodigoListaDeValores());
					logger.debug("OBLIGATORIOLISTA//"
							+ atribu.getSwitchObligatorio());
					logger.debug("TIPSITLISTA//" + atribu.getCodigoTipsit());
					
					HashMap<String, Object> params = new HashMap<String, Object>();
					
					params.put("PV_CDRAMO_I", atribu.getCdRamo());
					params.put("PV_CDNIVEL_I", atribu.getCodigoNivel());
					params.put("PV_CDROL_I", atribu.getCodigoRol());
					params.put("PV_CDATRIBU_I", atribu.getCodigoAtributoVariable());
					params.put("PV_SWOBLIGA_I", atribu.getSwitchObligatorio());
					params.put("PV_CDTIPSIT_I", atribu.getCodigoTipsit());
					params.put("PV_OTTABVAL_I", atribu.getCodigoListaDeValores());
					params.put("PV_SWCOTIZA_I", atribu.getApareceCotizador());
					params.put("PV_SWCOTUPD_I", atribu.getModificaCotizador());
					params.put("PV_SWDATCOM_I", atribu.getDatoComplementario());
					params.put("PV_SWCOMOBL_I", atribu.getObligatorioComplementario());
					params.put("PV_SWCOMUPD_I", atribu.getModificableComplementario());
					params.put("PV_SWENDOSO_I", atribu.getApareceEndoso());
					params.put("PV_SWENDOBL_I", atribu.getObligatorioEndoso());
					params.put("PV_SWENDUPD_I", atribu.getModificaEndoso());
					params.put("PV_CDATRIBU_PADRE_I", atribu.getCodigoPadre());
					params.put("PV_NMORDEN_I", atribu.getOrden());
					params.put("PV_NMAGRUPA_I", atribu.getAgrupador());
					params.put("PV_CDCONDICVIS_I", atribu.getCodigoCondicion());
					params.put("PV_CDEXPRES_I", atribu.getCodigoExpresion());
					params.put("PV_SWTARIFI_i", atribu.getRetarificacion());
					
					wizardDAO.agregaAtributoVariableRol(params);

				}
			}
			
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_ATRIBUTO_VARIABLE_ROL",
					bae);
			throw new ApplicationException(
					"Error al insertar atributo variable al rol");
		}

	}
	
	public MensajesVO eliminaAtributoVariableRol(RolAtributoVariableVO atributoRol) throws ApplicationException {
		
		MensajesVO mensajeVO = null;
		try {
			HashMap<String,Object> params =  new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", atributoRol.getCdRamo());
			params.put("PV_CDROL_I", atributoRol.getCodigoRol());
			params.put("PV_CDATRIBU_I", atributoRol.getCodigoAtributoVariable());
			mensajeVO = wizardDAO.eliminaAtributoVariableRol(params);
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_ATRIBUTO_VARIABLE_ROL", bae);
			throw new ApplicationException("Error al eliminar rol al inciso");
		}
		return mensajeVO;
	}

	public void agregarRolInciso(RolVO rol) throws ApplicationException {
		
		try {
			HashMap<String,Object> params =  new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", rol.getCodigoRamo());
			params.put("PV_CDROL_I", rol.getCodigoRol());
			params.put("PV_CDNIVEL_I", rol.getCodigoNivel());
			params.put("PV_CDCOMPO_I", rol.getCodigoComposicion());
			params.put("PV_NMAXIMO_I", rol.getNumeroMaximo());
			params.put("PV_CDTIPSIT_I", rol.getCdtipsit());
			params.put("PV_SWDOMICI_I", rol.getSwitchDomicilio());
			
			wizardDAO.agregarRolInciso(params);
		
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_ROL_INCISO", bae);
			throw new ApplicationException("Error al insertar rol al inciso");
		}

	}

	public void agregarAtributoVariableCatalogo(RolAtributoVariableVO atributo)
			throws ApplicationException {
		
		try {
			HashMap<String,Object> params =  new HashMap<String, Object>();
			params.put("PV_CDATRIBU_I", atributo.getCodigoAtributoVariable());
			params.put("PV_DSATRIBU_I", atributo.getDescripcionAtributoVariable());
			params.put("PV_SWFORMAT_I", atributo.getFormato());
			params.put("PV_NMLMIN_I", atributo.getMinimo());
			params.put("PV_NMLMAX_I", atributo.getMaximo());
			params.put("PV_OTTABVAL_I", atributo.getOttabval());
			params.put("PV_GB_SWFORMAT_I", atributo.getFormato());
			/**
			 * TODO: No se encontraron los siguientes dos datos en el VO
			 */
			params.put("PV_CDFISJUR_I", null);
			params.put("PV_CDCATEGO_I", null);
			params.put("PV_SWOBLIGA_I", atributo.getSwitchObligatorio());
			
			wizardDAO.agregarAtributoVariableCatalogo(params);
		} catch (Exception bae) {
			logger.error(
					"Exception in invoke AGREGAR_ATRIBUTO_VARIABLE_CATALOGO",
					bae);
			throw new ApplicationException(
					"Error al insertar atributo variable al catalogo");
		}

	}

	public RolVO obtieneRolAsociado(int codigoRamo, String codigoTipoSituacion,
			String codigoRol, int codigoNivel) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDTIPSIT_I", codigoTipoSituacion);
		params.put("PV_CDROL_I", codigoRol);
		params.put("PV_CDNIVEL_I", codigoNivel);

		RolVO rol = null;
		try {
			rol = wizardDAO.obtieneRolAsociado(params);
		

		} catch (Exception e) {
			throw new ApplicationException("Error regresando rol asociado");
		}
		return rol;
	}

	public WrapperResultados eliminaRol(int codigoRamo, String codigoRol, int codigoNivel)
			throws ApplicationException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDROL_I", codigoRol);
		params.put("PV_CDNIVEL_I", codigoNivel);
		
		WrapperResultados resultado = null;
		try{
			resultado = wizardDAO.eliminaRol(params);
		}catch(Exception e){
			logger.error("ERROR" + e, e);
			throw new ApplicationException("Error al eliminar rol");
		}
		return resultado;
	}

	public boolean tieneHijosAtributoVariableRol(RolAtributoVariableVO atributoRol) throws ApplicationException {
		
		MensajesVO mensaje = null;
		boolean tieneHijos = false;
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("pv_cdramo_i", atributoRol.getCdRamo());
			params.put("pv_cdrol_i", atributoRol.getCodigoRol());
			params.put("pv_cdatribu_i", atributoRol.getCodigoAtributoVariable());
			
			mensaje = wizardDAO.tieneHijosAtributoVariableRol(params);
		} catch (Exception bae) {
			logger.error("Se origino un error: " + bae.getMessage(), bae);
			throw new ApplicationException("Error intentando validar hijos de atributos variables de Rol");
		} 
		if(mensaje.getMsgText().equals("1")){
			tieneHijos = true;
		}else{
			tieneHijos = false;
		}
		return tieneHijos;
	}

}