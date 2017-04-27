package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.SumaAseguradaManager;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaIncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaVO;
import mx.com.gseguros.wizard.dao.WizardDAO;

import org.apache.log4j.Logger;

@Deprecated
public class SumaAseguradaManagerImpl implements SumaAseguradaManager {

	private static Logger logger = Logger
			.getLogger(SumaAseguradaManagerImpl.class);

	
	private WizardDAO wizardDAO;

	public void setWizardDAO(WizardDAO wizardDAO) {
		this.wizardDAO = wizardDAO;
	}


	public List<LlaveValorVO> catalogoTipoSumaAsegurada()
			throws ApplicationException {
		List<LlaveValorVO> catalogoTipoSumaAsegurada = null;
		try {
			
			

			if (catalogoTipoSumaAsegurada == null) {
				throw new ApplicationException("No exite ningun tipo de suma asegurada");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_TIPO_SUMA_ASEGURADA'",bae);
			throw new ApplicationException(
					"Error al cargar el catalogo de tipo suma asegurada desde el sistema");
		}
		return catalogoTipoSumaAsegurada;

	}

	public List<LlaveValorVO> catalogoSumaAsegurada(String codigoRamo)
			throws ApplicationException {
		List<LlaveValorVO> catalogoSumaAsegurada = null;
		try {
			

			if (catalogoSumaAsegurada == null) {
				throw new ApplicationException("No exite suma asegurada");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_SUMA_ASEGURADA'", bae);
			throw new ApplicationException(
					"Error al cargar el catalogo de suma asegurada desde el sistema");
		}
		return catalogoSumaAsegurada;
	}

	public List<LlaveValorVO> catalogoMonedaSumaASegurada()
			throws ApplicationException {
		List<LlaveValorVO> catalogoMoneda = null;
		try {
			

			if (catalogoMoneda == null) {
				throw new ApplicationException("No exite ninguna moneda de suma asegurada");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'CATALOGO_MONEDA_SUMA_ASEGURADA'",bae);
			throw new ApplicationException(
					"Error al cargar el catalogo de moneda suma asegurada desde el sistema");
		}
		return catalogoMoneda;
	}

	public List<SumaAseguradaVO> listaSumaAsegurada(String codigoRamo,
			String codigoCapital) throws ApplicationException {
		List<SumaAseguradaVO> listaSumasAseguradas = null;
		try {
			
			Map params = new HashMap<String, String>();
			params.put("codigoRamo", codigoRamo);
			params.put("codigoCapital", codigoCapital);

			

			if (listaSumasAseguradas == null) {
				throw new ApplicationException("No exite suma asegurada");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'LISTA_SUMAS_ASEGURADAS'", bae);
			throw new ApplicationException(
					"Error al cargar la lista de sumas aseguradas desde el sistema");
		}
		return listaSumasAseguradas;
	}

	public void agregaSumaAseguradaProducto(SumaAseguradaVO sumaAsegurada)
			throws ApplicationException {
		
		try {
		
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_SUMA_ASEGURADA", bae);
			throw new ApplicationException("Error al insertar suma asegurada");
		}

	}

	public void eliminaSumaAseguradaProducto(String codigoCapital,
			String codigoRamo) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDCAPITA_I", codigoCapital);
		try {
			
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_SUMA_ASEGURADA_PRODUCTO",bae);
			throw new ApplicationException(
					"Error al eliminar suma asegurada del producto");
		}

	}

	public List<SumaAseguradaIncisoVO> listaSumaAseguradaInciso(
			String codigoRamo, String codigoCobertura, String codigoCapital, String codigoTipoSituacion)
			throws ApplicationException {
		List<SumaAseguradaIncisoVO> listaSumasAseguradasInciso = null;
		try {
			
			Map params = new HashMap<String, String>();
			params.put("codigoRamo", codigoRamo);
			params.put("codigoCapital", codigoCapital);
			params.put("codigoCobertura", codigoCobertura);
			params.put("codigoTipoSituacion", codigoTipoSituacion);
			

			if (listaSumasAseguradasInciso == null) {
				throw new ApplicationException("No exite suma asegurada");
			}
		} catch (Exception bae) {
			logger.error("Exception in invoke 'LISTA_SUMAS_ASEGURADAS_INCISO'", bae);
			throw new ApplicationException(
					"Error al cargar la lista de sumas aseguradas nivel inciso desde el sistema");
		}
		return listaSumasAseguradasInciso;
	}

	/*
	 * (non-Javadoc)
	 * @see com.biosnet.ice.wizard.productos.service.SumaAseguradaManager#agregaSumaAseguradaInciso(com.biosnet.ice.wizard.productos.sumaAsegurada.model.SumaAseguradaIncisoVO)
	 */
	public void agregaSumaAseguradaInciso(SumaAseguradaIncisoVO sumaAseguradaInciso)
			throws ApplicationException {
		
		try {
		
		} catch (Exception bae) {
			logger.error("Exception in invoke AGREGAR_SUMA_ASEGURADA_INCISO", bae);
			throw new ApplicationException("Error al insertar suma asegurada nivel inciso");
		}
		
	}

	public void eliminaSumaAseguradaInciso(String codigoCapital,
			String codigoRamo, String codigoTipoSituacion)
	throws ApplicationException {
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDCAPITA_I", codigoCapital);
		params.put("PV_CDTIPSIT_I",codigoTipoSituacion);
		try {
			
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_SUMA_ASEGURADA_INCISO",bae);
			throw new ApplicationException(
					"Error al eliminar suma asegurada del inciso");
		}
		
	}


}
