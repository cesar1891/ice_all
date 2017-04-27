package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.ConvertUtil;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.SumaAseguradaManagerJdbcTemplate;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaIncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaVO;

public class SumaAseguradaManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements SumaAseguradaManagerJdbcTemplate {

	@SuppressWarnings("unchecked")
	public void agregaSumaAseguradaInciso(SumaAseguradaIncisoVO sumaAseguradaInciso) throws ApplicationException {
		
		HashMap map = new HashMap();
		map.put("PV_CDRAMO_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoRamo()));
		map.put("PV_CDCAPITA_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoCapital()));
		map.put("PV_CDTIPCAP_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoTipoCapital()));
		map.put("PV_DSCAPITA_I", ConvertUtil.nvl(sumaAseguradaInciso.getDescripcionCapital()));
		map.put("PV_CDGARANT_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoCobertura()));
		map.put("PV_CDPRESEN_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoLeyenda()));
		map.put("PV_CDTIPSIT_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoTipoSituacion()));
		map.put("PV_OTTABVAL_I", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoListaValor()));
		map.put("PV_SWREAUTO_I", ConvertUtil.nvl(sumaAseguradaInciso.getSwitchReinstalacion()));
		map.put("pv_cdexpdef_i", ConvertUtil.nvl(sumaAseguradaInciso.getCodigoExpresion()));

		WrapperResultados res = returnBackBoneInvoke(map, "AGREGAR_SUMA_ASEGURADA_INCISO_JDBC_TMPL");
		//return res.getMsgText();
	}
	
	public List<LlaveValorVO> catalogoTipoSumaAsegurada()
			throws ApplicationException {
		List<LlaveValorVO> catalogoTipoSumaAsegurada = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			WrapperResultados res = returnBackBoneInvoke(params, "CATALOGO_TIPO_SUMA_ASEGURADA");
			catalogoTipoSumaAsegurada = (List<LlaveValorVO>) res.getItemList();

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
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", codigoRamo);
			WrapperResultados res = returnBackBoneInvoke(params, "CATALOGO_SUMA_ASEGURADA");
			catalogoSumaAsegurada = (List<LlaveValorVO>) res.getItemList();
			
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
			HashMap<String, Object> params = new HashMap<String, Object>();
			WrapperResultados res = returnBackBoneInvoke(params, "CATALOGO_MONEDA_SUMA_ASEGURADA");
			catalogoMoneda = (List<LlaveValorVO>) res.getItemList();
			
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
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", codigoRamo);
			params.put("PV_CDCAPITA_I", codigoCapital);
			WrapperResultados res = returnBackBoneInvoke(params, "LISTA_SUMAS_ASEGURADAS");
			listaSumasAseguradas = (List<SumaAseguradaVO>) res.getItemList();

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
	
	public List<SumaAseguradaIncisoVO> listaSumaAseguradaInciso(
			String codigoRamo, String codigoCobertura, String codigoCapital, String codigoTipoSituacion)
			throws ApplicationException {
		List<SumaAseguradaIncisoVO> listaSumasAseguradasInciso = null;
		try {
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PV_CDRAMO_I", codigoRamo);
			params.put("PV_CDCAPITA_I", codigoCapital);
			params.put("PV_CDGARANT_I", codigoCobertura);
			params.put("PV_CDTIPSIT_I", codigoTipoSituacion);
			WrapperResultados res = returnBackBoneInvoke(params, "LISTA_SUMAS_ASEGURADAS_INCISO");
			listaSumasAseguradasInciso = (List<SumaAseguradaIncisoVO>) res.getItemList();
			
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
	
	public void eliminaSumaAseguradaProducto(String codigoCapital,
			String codigoRamo) throws ApplicationException {
		
		Map params = new HashMap<String, String>();
		params.put("PV_CDRAMO_I", codigoRamo);
		params.put("PV_CDCAPITA_I", codigoCapital);
		try {
			returnBackBoneInvoke(params, "ELIMINAR_SUMA_ASEGURADA_PRODUCTO");
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_SUMA_ASEGURADA_PRODUCTO",bae);
			throw new ApplicationException(
					"Error al eliminar suma asegurada del producto");
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
			returnBackBoneInvoke(params, "ELIMINAR_SUMA_ASEGURADA_INCISO");
		} catch (Exception bae) {
			logger.error("Exception in invoke ELIMINAR_SUMA_ASEGURADA_INCISO",bae);
			throw new ApplicationException(
					"Error al eliminar suma asegurada del inciso");
		}
		
	}


}
