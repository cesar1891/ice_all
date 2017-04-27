package mx.com.gseguros.portal.siniestros.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.renovacion.dao.RenovacionDAO;
import mx.com.gseguros.portal.siniestros.dao.SiniestrosDAO;
import mx.com.gseguros.portal.siniestros.model.AltaTramiteVO;
import mx.com.gseguros.portal.siniestros.model.AutorizaServiciosVO;
import mx.com.gseguros.portal.siniestros.model.AutorizacionServicioVO;
import mx.com.gseguros.portal.siniestros.model.CoberturaPolizaVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaManteniVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaPorcentajeVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaProveedorVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTDETAUTSVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTTAPVAATVO;
import mx.com.gseguros.portal.siniestros.model.DatosSiniestroVO;
import mx.com.gseguros.portal.siniestros.model.ListaFacturasVO;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.model.PolizaVigenteVO;
import mx.com.gseguros.portal.siniestros.model.SiniestroVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Reclamo;

public class SiniestrosManagerImpl implements SiniestrosManager
{
	private static Logger logger = Logger.getLogger(SiniestrosManagerImpl.class);
	
	@Autowired
	private PantallasDAO  pantallasDAO;
	
	private SiniestrosDAO siniestrosDAO;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private FlujoMesaControlManager flujoMesaControlManager;
	
	private static org.apache.log4j.Logger log=org.apache.log4j.Logger.getLogger(SiniestrosManagerImpl.class);
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
	private ConsultasPolizaDAO consultasPolizaDAOICE;
	
	@Override
	public List<AutorizacionServicioVO> getConsultaAutorizacionesEsp(String nmautser) throws Exception {
		try {
			return siniestrosDAO.obtieneDatosAutorizacionEsp(nmautser);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaAsegurado(String cdperson) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoAsegurado(cdperson);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<AutorizaServiciosVO> getConsultaListaAutorizaciones(
			String tipoAut, String cdperson) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoAutorizaciones(tipoAut,cdperson);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<ConsultaProveedorVO> getConsultaListaProveedorMedico(String tipoprov,String cdpresta)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoProvMedico(tipoprov,cdpresta);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<CoberturaPolizaVO> getConsultaListaCoberturaPoliza(
			HashMap<String, Object> paramCobertura) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCoberturaPoliza(paramCobertura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<CoberturaPolizaVO> getConsultaCoberturaAsegurado(
			HashMap<String, Object> paramCobertura) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCoberturaAsegurado(paramCobertura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<DatosSiniestroVO> getConsultaListaDatSubGeneral(
			HashMap<String, Object> paramDatSubGral)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoDatSubGeneral(paramDatSubGral);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	
	public List<GenericVO> getConsultaListaSubcobertura(String cdunieco, String cdramo, String estado, String nmpoliza, 
						String nmsituac, String cdtipsit, String cdgarant, String cdsubcob, String cdrol) throws Exception {
		try {
			log.debug("getConsultaListaSubcobertura cdunieco : "+cdunieco+" cdramo : "+cdramo+" estado : "+
						estado+" nmpoliza : "+nmpoliza+" nmsituac : "+nmsituac+" cdtipsit : "+cdtipsit+" cdgarant : "+cdgarant+"cdsubcob : "+cdsubcob);
			List<GenericVO> lista = siniestrosDAO.obtieneListadoSubcobertura(cdunieco, cdramo, estado, nmpoliza, nmsituac, cdtipsit, cdgarant, cdsubcob, cdrol);
			if(lista==null)
			{
				lista= new ArrayList<GenericVO>();
			}
			log.debug("getConsultaListaSubcobertura lista size: "+lista.size());
			return lista;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaSubcoberturaTotales() throws Exception {
		try {
			List<GenericVO> lista = siniestrosDAO.obtieneListadoSubcoberturaTotales();
			if(lista==null)
			{
				lista= new ArrayList<GenericVO>();
			}
			log.debug("getConsultaListaSubcoberturaTotales lista size: "+lista.size());
			return lista;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaSubcoberturaTotalesMultisalud(String cdtipsit) throws Exception {
		try {
			List<GenericVO> lista = siniestrosDAO.obtieneListadoSubcoberturaTotalesMultisalud(cdtipsit);
			if(lista==null)
			{
				lista= new ArrayList<GenericVO>();
			}
			log.debug("getConsultaListaSubcoberturaTotales lista size: "+lista.size());
			return lista;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaSubcoberturaRecupera() throws Exception {
		try {
			List<GenericVO> lista = siniestrosDAO.obtieneListadoSubcoberturaRecupera();
			if(lista==null)
			{
				lista= new ArrayList<GenericVO>();
			}
			log.debug("getConsultaListaSubcoberturaTotales lista size: "+lista.size());
			return lista;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaCPTICD(String cdtabla, String otclave)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCPTICD(cdtabla,otclave);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<GenericVO> getConsultaListaCPTICD(String cdicd, String cdramo, String cdtipsit, String edad, String genero) // (EGS)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCPTICD(cdicd, cdramo, cdtipsit, edad, genero);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaTipoPago(String cdramo) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoTipoPago(cdramo);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	@Override
	public List<HashMap<String, String>> loadListaDocumentos(Map<String, String> params)
			throws Exception {
		try {
			return siniestrosDAO.loadListaDocumentos(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String generaContraRecibo(HashMap<String, Object> params)
			throws Exception {
		try {
			return siniestrosDAO.generaContraRecibo(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> loadListaIncisosRechazos(Map<String, String> params)
			throws Exception {
		try {
			return siniestrosDAO.loadListaIncisosRechazos(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public boolean guardaEstatusDocumentos(HashMap<String, String> params, List<HashMap<String, String>> saveList)
			throws Exception {
		
		boolean allUpdated = true;
		
		for(HashMap<String, String> doc : saveList){
			try {
				params.put("pv_accion_i", doc.get("listo"));
				params.put("pv_cddocume_i", doc.get("id"));
				siniestrosDAO.guardaEstatusDocumento(params);
			} catch (DaoException daoExc) {
				log.error("Error al guardar documento ",daoExc);
				allUpdated = false;
			}
		}
		
		return allUpdated;
	}

	@Override
	public List<Map<String, String>> loadListaRechazos()
			throws Exception {
		try {
			return siniestrosDAO.loadListaRechazos();
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public boolean rechazarTramite(HashMap<String, String> params)
			throws Exception {
		try {
			siniestrosDAO.rechazarTramite(params);
		} catch (DaoException daoExc) {
			log.error("Error al rechazar tramite: " + daoExc.getMessage(), daoExc);	
			return false;
		}
		return true;
	}

	@Override
	public List<SiniestroVO>  solicitudPagoEnviada(Map<String, String> params)
			throws Exception {
		
		List<SiniestroVO> siniestros  = null;
		try {
			siniestros = siniestrosDAO.solicitudPagoEnviada(params);
		} catch (Exception daoExc) {
			log.error("Error en solicitudPagoEnviada PL: " + daoExc.getMessage(), daoExc);	
		}
		return siniestros;
	}

	
	public void setSiniestrosDAO(SiniestrosDAO siniestrosDAO) {
		this.siniestrosDAO = siniestrosDAO;
	}
	
	@Override
	public List<ConsultaTDETAUTSVO> getConsultaListaTDeTauts(String nmautser)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoTDeTauts(nmautser);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public void getEliminacionRegistros(String nmautser)
			throws Exception {
			try {
				siniestrosDAO.eliminacionRegistrosTabla(nmautser);
			} catch (DaoException daoExc) {
				throw new Exception(daoExc.getMessage(), daoExc);
			}
	}
	
	@Override
	public List<AutorizacionServicioVO> guardarAutorizacionServicio(HashMap<String, Object> paramsR)throws Exception {
		try {
			return siniestrosDAO.guardarAutorizacionServicio(paramsR);			
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String guardaListaTDeTauts(HashMap<String, Object> paramsTDeTauts)
			throws Exception {
		try {
			return siniestrosDAO.guardarListaTDeTauts(paramsTDeTauts);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<ConsultaTTAPVAATVO> getConsultaListaTTAPVAAT(
			HashMap<String, Object> paramTTAPVAAT) throws Exception {
		 try {
		        return siniestrosDAO.obtieneListadoTTAPVAAT(paramTTAPVAAT);
		    } catch (DaoException daoExc) {
		        throw new Exception(daoExc.getMessage(), daoExc);
		    }
	}

	@Override
	public List<ConsultaManteniVO> getConsultaListaTipoMedico(String codigo) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoTipoMedico(codigo);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<ConsultaPorcentajeVO> getConsultaListaPorcentaje(String cdcpt, String cdtipmed,String mtobase) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoPorcentaje(cdcpt,cdtipmed,mtobase);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<PolizaVigenteVO> getConsultaListaPoliza(String cdperson,String cdramo, String rolUsuario) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoPoliza(cdperson,cdramo,rolUsuario);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<GenericVO> getConsultaListaPlaza() throws Exception {
		try {
			return siniestrosDAO.obtieneListadoPlaza();
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String guardaListaFacturaSiniestro(String ntramite, String nfactura,
			Date fefactura, String cdtipser, String cdpresta, String ptimport,
			String cdgarant, String cdconval, String descporc, String descnume,
			String cdmoneda, String tasacamb, String ptimporta,
			String dctonuex, Date feegreso, String diasdedu, String nombProv,
			String tipoAccion, String factInicial) throws Exception {
		// TODO Auto-generated method stub
		try {
			String accion = null;
			String facInicial = null;
			log.debug("Entra a esta parte --> : "+feegreso);
			if(tipoAccion == null || tipoAccion == ""){
				accion = Constantes.INSERT_MODE;
			}else{
				accion = Constantes.UPDATE_MODE;
			}
			
			if(factInicial == null || factInicial ==""){
				facInicial = nfactura;
			}else{
				facInicial =  factInicial;
			}
			
			log.debug("Valor de la accion ==>"+accion);
			HashMap<String,Object> paramsFacMesaCtrl=new HashMap<String,Object>();
			paramsFacMesaCtrl.put("pv_accion_i", accion);
			paramsFacMesaCtrl.put("pv_ntramite_i",ntramite);
			paramsFacMesaCtrl.put("pv_nfactura_i",nfactura);
			paramsFacMesaCtrl.put("pv_ffactura_i",fefactura);
			paramsFacMesaCtrl.put("pv_cdtipser_i",cdtipser);
			paramsFacMesaCtrl.put("pv_cdpresta_i",cdpresta);
			paramsFacMesaCtrl.put("pv_ptimport_i",ptimport);
			paramsFacMesaCtrl.put("pv_cdgarant_i",cdgarant);
			paramsFacMesaCtrl.put("pv_cdconval_i",cdconval);
			paramsFacMesaCtrl.put("pv_descporc_i",descporc);
			paramsFacMesaCtrl.put("pv_descnume_i",descnume);
			paramsFacMesaCtrl.put("pv_cdmoneda_i",cdmoneda);
			paramsFacMesaCtrl.put("pv_tasacamb_i",tasacamb);
			paramsFacMesaCtrl.put("pv_ptimporta_i",ptimporta);
			paramsFacMesaCtrl.put("pv_dctonuex_i",dctonuex);
			paramsFacMesaCtrl.put("pv_feegreso_i", feegreso);
			paramsFacMesaCtrl.put("pv_diasdedu_i",diasdedu);
			paramsFacMesaCtrl.put("pv_nombprov_i",nombProv);
			paramsFacMesaCtrl.put("pv_factInicial_i",facInicial);
			log.debug("guardaListaFacMesaControl params: "+paramsFacMesaCtrl);
			return siniestrosDAO.guardaListaFacturaSiniestro(paramsFacMesaCtrl);
		} catch (ParseException parseExc) {
			throw new Exception(parseExc.getMessage(), parseExc);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	/*@Override
	public String movFacMesaControl(
			String ntramite,
			String nfactura,
			String fefactura,
			String cdtipser,
			String cdpresta,
			String ptimport,
			String cdgarant,
			String cdconval,
			String descporc,
			String descnume, 
			String operacion,
			String cdmoneda,
			String tasacamb,
			String ptimporta,
			String dctonuex) throws Exception {
		// TODO Auto-generated method stub
		try {
			HashMap<String,Object> paramsFacMesaCtrl=new HashMap<String,Object>();
			paramsFacMesaCtrl.put("pv_accion_i", operacion);
			paramsFacMesaCtrl.put("pv_ntramite_i",ntramite);
			paramsFacMesaCtrl.put("pv_nfactura_i",nfactura);
				paramsFacMesaCtrl.put("pv_ffactura_i",DateUtils.parseDate(fefactura, Constantes.FORMATO_FECHA));
			paramsFacMesaCtrl.put("pv_cdtipser_i",cdtipser);
			paramsFacMesaCtrl.put("pv_cdpresta_i",cdpresta);
			paramsFacMesaCtrl.put("pv_ptimport_i",ptimport);
			paramsFacMesaCtrl.put("pv_cdgarant_i",cdgarant);
			paramsFacMesaCtrl.put("pv_cdconval_i",cdconval);
			paramsFacMesaCtrl.put("pv_descporc_i",descporc);
			paramsFacMesaCtrl.put("pv_descnume_i",descnume);
			paramsFacMesaCtrl.put("pv_cdmoneda_i",cdmoneda);
			paramsFacMesaCtrl.put("pv_tasacamb_i",tasacamb);
			paramsFacMesaCtrl.put("pv_ptimporta_i",ptimporta);
			paramsFacMesaCtrl.put("pv_dctonuex_i",dctonuex);
			log.debug("guardaListaFacMesaControl params: "+paramsFacMesaCtrl);
			return siniestrosDAO.guardaFacMesaControl(paramsFacMesaCtrl);
		} catch (ParseException parseExc) {
			throw new Exception(parseExc.getMessage(), parseExc);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}*/

	@Override
	public String guardaListaTworkSin(HashMap<String, Object> paramsTworkSin) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.guardaListaTworkSin(paramsTworkSin);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String getAltaSiniestroAutServicio(String nmautser,String nfactura) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.guardaAltaSiniestroAutServicio(nmautser,nfactura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String getAltaSiniestroAltaTramite(String ntramite) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.guardaAltaSiniestroAltaTramite(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String getAltaSiniestroSinAutorizacion(String ntramite,String cdunieco,String cdramo, String estado,String nmpoliza,
												  String nmsuplem,String nmsituac, String cdtipsit, Date fechaOcurrencia,String nfactura,
												  String secAsegurado) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.guardaAltaSiniestroSinAutorizacion(ntramite, cdunieco, cdramo, estado, nmpoliza,
					  												nmsuplem, nmsituac, cdtipsit, fechaOcurrencia, nfactura,secAsegurado);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<ListaFacturasVO> getConsultaListaFacturas(HashMap<String, Object> paramFact) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoFacturas(paramFact);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String getBajaMsinival(HashMap<String, Object> paramBajasinival) throws Exception {
		try {
			return siniestrosDAO.bajaMsinival(paramBajasinival);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	//public List<GenericVO> obtieneListadoCobertura(String cdramo,String cdtipsit) throws DaoException;
	
	public List<GenericVO> obtieneListadoCobertura(String cdramo,String cdtipsit) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCobertura(cdramo,cdtipsit);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	public List<GenericVO> obtieneListadoCoberturaTotales() throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCoberturaTotales();
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String actualizaOTValorMesaControl(Map<String, Object> params) throws Exception {
		try
		{
			String[] keys = new String[]{
					"pv_cdramo_i","pv_cdtipsit_i", "pv_cdsucadm_i", "pv_cdsucdoc_i","pv_comments_i",
					"pv_otvalor01_i","pv_otvalor02_i","pv_otvalor03_i","pv_otvalor04_i","pv_otvalor05_i","pv_otvalor06_i","pv_otvalor07_i","pv_otvalor08_i","pv_otvalor09_i","pv_otvalor10_i",
					"pv_otvalor11_i","pv_otvalor12_i","pv_otvalor13_i","pv_otvalor14_i","pv_otvalor15_i","pv_otvalor16_i","pv_otvalor17_i","pv_otvalor18_i","pv_otvalor19_i","pv_otvalor20_i",
					"pv_otvalor21_i","pv_otvalor22_i","pv_otvalor23_i","pv_otvalor24_i","pv_otvalor25_i","pv_otvalor26_i","pv_otvalor27_i","pv_otvalor28_i","pv_otvalor29_i","pv_otvalor30_i",
					"pv_otvalor31_i","pv_otvalor32_i","pv_otvalor33_i","pv_otvalor34_i","pv_otvalor35_i","pv_otvalor36_i","pv_otvalor37_i","pv_otvalor38_i","pv_otvalor39_i","pv_otvalor40_i",
					"pv_otvalor41_i","pv_otvalor42_i","pv_otvalor43_i","pv_otvalor44_i","pv_otvalor45_i","pv_otvalor46_i","pv_otvalor47_i","pv_otvalor48_i","pv_otvalor49_i","pv_otvalor50_i",
			};
			if(params!=null)
			{
				for(String key : keys)
				{
					if(!params.containsKey(key))
					{
						params.put(key,null);
					}
				}
			}
			return siniestrosDAO.actualizaOTValorMesaControl(params);
		}
		catch (DaoException daoExc)
		{
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	/**
	 * PKG_SINIESTRO.P_LISTA_SINIESTROSXTRAMITE
	 * 6969 NMSINIES,
	 * 500 NMAUTSER,
	 * 510918 CDPERSON,
	 * 'JUAN PEREZ' NOMBRE,
	 * SYSDATE FEOCURRE,
	 * 1009 CDUNIECO,
	 * 'SALUD CAMPECHE' DSUNIECO,
	 * 2 CDRAMO,
	 * 'SALUD VITAL' DSRAMO,
	 * 'SL' CDTIPSIT,
	 * 'SALUD VITAL' DSTIPSIT,
	 * status,
	 * 'M' ESTADO,
	 * 500 NMPOLIZA,
	 * 'S' VOBOAUTO,
	 * '65' CDICD,
	 * 'GRIPE' DSICD,
	 * '66' ICD2,
	 * 'FIEBRE' DSICD2,
	 * 12.5 DESCPORC,
	 * 300 DESCNUME,
	 * 15 COPAGO,
	 * 3500 PTIMPORT,
	 * 'S' AUTRECLA,
	 * 54647 NMRECLAM,
	 * aaapertu
	 */
	@Override
	public List<Map<String,String>> listaSiniestrosMsiniesTramite(String ntramite,String factura, String procesoInterno) throws Exception
	//public List<Map<String,String>> listaSiniestrosMsiniesTramite(String ntramite, String procesoInterno) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , factura);
		params.put("pv_autoServ_i" , procesoInterno);
		log.debug("listaSiniestrosTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.listaSiniestrosMsiniesTramite(params);
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("listaSiniestrosTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String,String>> listaAseguradosTramite(String ntramite, String  nfactura, String tipoProceso) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , nfactura);
		params.put("pv_tipoProceso_i" , tipoProceso);
		List<Map<String,String>> lista = siniestrosDAO.listaAseguradosTramite(params);
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("listaSiniestrosTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String,String>> listaSiniestrosTramite2(String ntramite,String nfactura) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , nfactura);
		log.debug("listaSiniestrosTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.listaSiniestrosTramite2(params);
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("listaSiniestrosTramite lista size: "+lista.size());
		return lista;
	}
	
	/**
	 * PKG_PRESINIESTRO.P_GET_TRAMITE_COMPLETO
	 */
	@Override
	public Map<String,String> obtenerTramiteCompleto(String ntramite) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		log.debug("obtenerTramiteCompleto params: "+params);
		Map<String,String> tramite = siniestrosDAO.obtenerTramiteCompleto(params);
		log.debug("obtenerTramiteCompleto tramite: "+tramite);
		return tramite;
	}
	
	
	/**
	 * PKG_PRESINIESTRO.P_GET_TRAMITE_COMPLETO
	 * 
	 */
	@Override
	public Map<String,String> obtenerTramiteCompletoXNmsolici(String nmpoliza, String cdunieco, String cdramo) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_nmsolici_i" , nmpoliza);
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i" , cdramo);
		params.put("pv_cdtiptra_i" , "21");
		params.put("pv_estado_i"   , "W");
		
		log.debug("obtenerTramiteCompletoXNmpoliza params: "+params);
		Map<String,String> tramite = siniestrosDAO.obtenerTramiteCompletoXNmsolici(params);
		log.debug("obtenerTramiteCompletoXNmpoliza tramite: "+tramite);
		return tramite;
	}
	/**
	 * PKG_SATELITES.P_OBT_TFACMESCTRL
	 * CDCONVAL=null,
	 * CDGARANT=null, 
	 * CDPRESTA=3107,
	 * CDTIPSER=1,
	 * DESCNUME=null,
	 * DESCPORC=null,
	 * DESCSERVICIO=HOSPITALIZACIÓN,
	 * DSGARANT=null,
	 * DSSUBGAR=null
	 * FFACTURA=05/03/2014,
	 * NFACTURA=3829, 
	 * NOMBREPROVEEDOR=null,
	 * NTRAMITE=1592,
	 * PTIMPORT=500,
	 */
	@Override
	public List<Map<String,String>> obtenerFacturasTramite(String ntramite) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		log.debug("obtenerFacturasTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.obtenerFacturasTramite(params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}

	@Override
	public List<HashMap<String,String>> obtenerFacturasTramiteSiniestro(String ntramite, String siniestro) throws Exception
	{
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("" , siniestro);
		
		List<HashMap<String,String>> lista = siniestrosDAO.obtenerFacturasTramiteSiniestro(params);
		if(lista==null)
		{
			lista = new ArrayList<HashMap<String,String>>();
		}
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}
	
	/*@Override
	public void actualizarAutorizacionTworksin(String ntramite, String nmpoliza, String cdperson,String nmautser) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i",ntramite);
		params.put("pv_nmpoliza_i",nmpoliza);
		params.put("pv_cdperson_i",cdperson);
		params.put("pv_nmautser_i",nmautser);
		log.debug("actualizarAutorizacionTworksin params: "+params);
		siniestrosDAO.actualizarAutorizacionTworksin(params);
		log.debug("actualizarAutorizacionTworksin end");
	}*/
	
	@Override
	public void actualizarAutorizacionTworksin(String ntramite, String nmpoliza, String cdperson,String nmautser,
			String nfactura,Date feocurrencia, String secAsegurado) throws Exception
	{
		//Map<String,String> params = new HashMap<String,String>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_ntramite_i",ntramite);
		params.put("pv_nmpoliza_i",nmpoliza);
		params.put("pv_cdperson_i",cdperson);
		params.put("pv_nmautser_i",nmautser);
		params.put("pv_nfactura_i",nfactura);
		params.put("pv_feocurrencia_i",feocurrencia);
		params.put("pv_secAsegurado_i",secAsegurado);
		log.debug("actualizarAutorizacionTworksin params: "+params);
		siniestrosDAO.actualizarAutorizacionTworksin(params);
		log.debug("actualizarAutorizacionTworksin end");
	}
	
	@Override
	public List<PolizaVigenteVO> getConsultaPolizaUnica(HashMap<String, Object> paramPolUnica) throws Exception {
		try {
			log.debug("EN CONSULTA..." + paramPolUnica);
			return siniestrosDAO.consultaPolizaUnica(paramPolUnica);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String validaExclusionPenalizacion(HashMap<String, Object> paramExclusion) throws Exception {
		try {
			return siniestrosDAO.validaExclusionPenalizacion(paramExclusion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String penalizacionCirculoHospitalario(HashMap<String, Object> paramPenalizacion) throws Exception {
		try {
			return siniestrosDAO.obtieneDatosCirculoHospitalario(paramPenalizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public void actualizaMsinies(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem,
			String status,
			String aaapertu,
			String nmsinies,
			Date feocurre,
			String cdicd,
			String cdicd2,
			String nreclamo) throws Exception
	{
		siniestrosDAO.actualizaMsinies(cdunieco,cdramo,estado,nmpoliza,nmsituac,nmsuplem,status,aaapertu,nmsinies,feocurre,cdicd,cdicd2,nreclamo);
	}
	
	@Override
	public void P_MOV_MAUTSINI(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String nmordina,
			String areaauto,
			String swautori,
			String tipautor,
			String comments,
			String accion) throws Exception
	{
		siniestrosDAO.P_MOV_MAUTSINI(
				cdunieco,cdramo,estado,nmpoliza,nmsuplem,
				nmsituac,aaapertu,status,nmsinies,nfactura,
				cdgarant,cdconval,cdconcep,idconcep,nmordina,
				areaauto,swautori,tipautor,comments,accion);
	}
	
	@Override
	public void P_MOV_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String cdcapita,
			String nmordina,
			Date   femovimi,
			String cdmoneda,
			String ptprecio,
			String cantidad,
			String destopor,
			String destoimp,
			String ptimport,
			String ptrecobr,
			String nmanno,
			String nmapunte,
			String userregi,
			Date   feregist,
			String accion,
			String ptpcioex,
			String dctoimex,
			String ptimpoex,
			String mtoArancel,
			String aplicIVA) throws Exception
	{
		siniestrosDAO.P_MOV_MSINIVAL(cdunieco,
				 cdramo,
				 estado,
				 nmpoliza,
				 nmsuplem,
				 nmsituac,
				 aaapertu,
				 status,
				 nmsinies,
				 nfactura,
				 cdgarant,
				 cdconval,
				 cdconcep,
				 idconcep,
				 cdcapita,
				 nmordina,
				 femovimi,
				 cdmoneda,
				 ptprecio,
				 cantidad,
				 destopor,
				 destoimp,
				 ptimport,
				 ptrecobr,
				 nmanno,
				 nmapunte,
				 userregi,
				 feregist,
				 accion,
				 ptpcioex,
				 dctoimex,
				 ptimpoex,
				 mtoArancel,
				 aplicIVA);
	}
	
	@Override
	public List<Map<String,String>>P_GET_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.P_GET_MSINIVAL(
				 cdunieco,
				 cdramo,
				 estado,
				 nmpoliza,
				 nmsuplem,
				 nmsituac,
				 aaapertu,
				 status,
				 nmsinies,
				 nfactura);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("P_GET_MSINIVAL lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public void P_MOV_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina,
			String nmordmov,String ptimport,String comments,String userregi,Date feregist,String accion) throws Exception
	{
		siniestrosDAO.P_MOV_TDSINIVAL(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,
				aaapertu,status,nmsinies,nfactura,cdgarant,
				cdconval,cdconcep,idconcep,nmordina,nmordmov,
				ptimport,comments,userregi,feregist,accion);
	}
	
	@Override
	public List<Map<String,String>>P_GET_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.P_GET_TDSINIVAL(
				cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,
				aaapertu,status,nmsinies,nfactura,cdgarant,
				cdconval,cdconcep,idconcep,nmordina);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("P_GET_TDSINIVAL lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String,String>>P_GET_FACTURAS_SINIESTRO(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String cdtipsit) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.P_GET_FACTURAS_SINIESTRO(
				cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,
				aaapertu,status,nmsinies,cdtipsit);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("P_GET_FACTURAS_SINIES lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public List<GenericVO>obtenerCodigosMedicos(String idconcep, String subcaden) throws Exception
	{
		List<GenericVO>lista=siniestrosDAO.obtenerCodigosMedicos(idconcep,subcaden);
		if(lista==null)
		{
			lista = new ArrayList<GenericVO>();
		}
		log.debug("obtenerCodigosMedicos lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public List<GenericVO>obtenerCodigosMedicosTotales() throws Exception
	{
		List<GenericVO>lista=siniestrosDAO.obtenerCodigosMedicosTotales();
		if(lista==null)
		{
			lista = new ArrayList<GenericVO>();
		}
		log.debug("obtenerCodigosMedicos lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public Map<String,String>obtenerLlaveSiniestroReembolso(String ntramite) throws Exception
	{
		return siniestrosDAO.obtenerLlaveSiniestroReembolso(ntramite);
	}
	
	
	@Override
	public List<Map<String,String>> obtieneDatosGeneralesSiniestro(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsituac, String nmsuplem, String status, String aaapertu, String nmsinies, String ntramite) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_estado_i",   estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_status_i",   status);
		params.put("pv_aaapertu_i", aaapertu);
		params.put("pv_nmsinies_i", nmsinies);
		params.put("pv_ntramite_i", ntramite);
		
		log.debug("obtieneDatosGeneralesSiniestro params: "+params);
		return siniestrosDAO.obtieneDatosGeneralesSiniestro(params);
	}
	
	
	@Override
	public Map<String, Object> actualizaDatosGeneralesSiniestro(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsuplem, String aaapertu, String nmsinies, Date feocurre,
			String nmreclamo, String cdicd, String cdicd2, String cdcausa,String cdgarant, 
			String cdconval, String nmautser, String cdperson, String tipoProceso, String complemento, String aplicFondo) throws Exception {

		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_estado_i",   estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_aaapertu_i", aaapertu);
		params.put("pv_nmsinies_i", nmsinies);
		params.put("pv_feocurre_i", feocurre);
		params.put("pv_nmreclamo_i",nmreclamo);
		params.put("pv_cdicd_i",    cdicd);
		params.put("pv_cdicd2_i",   cdicd2);
		params.put("pv_cdcausa_i",  cdcausa);
		params.put("pv_cdgarant_i",  cdgarant);
		params.put("pv_cdconval_i",  cdconval);
		params.put("pv_nmautser_i", nmautser);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_tipoProceso_i", tipoProceso);
		params.put("pv_complemento_i", complemento);
		params.put("pv_aplicFondo_i", aplicFondo);
		log.debug("actualizaDatosGeneralesSiniestro params: "+params);
		return siniestrosDAO.actualizaDatosGeneralesSiniestro(params);
	}
	
	@Override
	public Map<String, Object> actualizaMsiniestroReferenciado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, String aaapertu, String status, String nmsinies, String nmsinref) throws Exception {
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_estado_i",   estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_aaapertu_i", aaapertu);
		params.put("pv_status_i", status);
		params.put("pv_nmsinies_i", nmsinies);
		params.put("pv_nmsinref_i", nmsinref);
		log.debug("actualizaDatosGeneralesSiniestro params: "+params);
		return siniestrosDAO.actualizaMsiniestroReferenciado(params);
	}

	@Override
	public List<Map<String,String>>P_GET_CONCEPTOS_FACTURA(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdtipsit,
			String ntramite) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.P_GET_CONCEPTOS_FACTURA(
				cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,aaapertu,status,nmsinies,nfactura,cdtipsit, ntramite);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("P_GET_CONCEPTOS_FACTURA lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public Map<String,String> obtenerDatosProveedor(String cdpresta) throws Exception
	{
		log.info(""
				+ "\n###################################"
				+ "\n###### obtenerDatosProveedor ######"
				);
		Map<String,String> proveedor = siniestrosDAO.obtenerDatosProveedor(cdpresta);
		String iva     = proveedor.get("IVA");
		String cedular = proveedor.get("CEDULAR");
		String isr     = proveedor.get("ISR");
		String idprov  = proveedor.get("IDPROVEEDOR");
		if(StringUtils.isBlank(iva))
		{
			proveedor.put("IVA","0");
		}
		if(StringUtils.isBlank(cedular))
		{
			proveedor.put("CEDULAR","0");
		}
		if(StringUtils.isBlank(isr))
		{
			proveedor.put("ISR","0");
		}
		if(StringUtils.isBlank(idprov))
		{
			proveedor.put("IDPROVEEDOR","0");
		}
		log.info("proveedor: "+proveedor);
		log.info(""
				+ "\n###### obtenerDatosProveedor ######"
				+ "\n###################################"
				);
		return proveedor;
	}
	
	@Override
	public Map<String,String>obtenerCopagoDeducible(
			String ntramite,
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String tipopago,
			String cdtipsit) throws Exception
	{
		return siniestrosDAO.obtenerCopagoDeducible(ntramite,cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,aaapertu,status,nmsinies,nfactura,tipopago,cdtipsit);
	}
	
	@Override
	public Map<String,String>obtenerRentaDiariaxHospitalizacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem) throws Exception
	{
		return siniestrosDAO.obtenerRentaDiariaxHospitalizacion(cdunieco,cdramo,estado,nmpoliza,nmsituac,nmsuplem);
	}
	
	@Override
	public String validaPorcentajePenalizacion(String zonaContratada,String zonaAtencion,String cdRamo) throws Exception {
		try {
			return siniestrosDAO.validaPorcentajePenalizacion(zonaContratada,zonaAtencion,cdRamo);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String validaAutorizacionProceso(String nmAutSer) throws Exception {
		try {
			return siniestrosDAO.obtieneAutorizacionProceso(nmAutSer);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String validaDocumentosCargados(HashMap<String, String> params) throws Exception {
		try {
			return siniestrosDAO.validaDocumentosCargados(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Reclamo> obtieneDatosReclamoWS(Map<String, Object> params) throws Exception{
		try {
			return siniestrosDAO.obtieneDatosReclamoWS(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String,String>> cargaHistorialSiniestros(Map<String,String> params) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.cargaHistorialSiniestros(params);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("cargaHistorial lista size:"+lista.size());
		return lista;
	}

	@Override
	public void getCambiarEstatusMAUTSERV(String nmautser,String status) throws Exception {
		// TODO Auto-generated method stub
		siniestrosDAO.cambiarEstatusMAUTSERV(nmautser,status);
	}

	@Override
	public List<AltaTramiteVO> getConsultaListaAltaTramite(String ntramite) throws Exception {
		try {
			return siniestrosDAO.consultaListaAltaTramite(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<MesaControlVO> getConsultaListaMesaControl(String ntramite) throws Exception {
		try {
			return siniestrosDAO.consultaListaMesaControl(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public void getEliminacionAsegurado(String ntramite, String factura, String valorAccion) throws Exception {
		try {
			siniestrosDAO.eliminacionAsegurado(ntramite, factura, valorAccion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//public void getEliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion) throws Exception {  (EGS)
	public void getEliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion, String cdramo) throws Exception {	// (EGS)

		try {
			//siniestrosDAO.eliminacionFacturaTramite(ntramite, nfactura , valorAccion); (EGS)
			siniestrosDAO.eliminacionFacturaTramite(ntramite, nfactura, valorAccion, cdramo); // (EGS)
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public void movTimpsini(String accion
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String ntramite
			,String ptimport
			,String iva
			,String ivr
			,String isr
			,String cedular
			,boolean enviado
			,String nmsecsin) throws Exception
	{
		log.info(""
				+ "\n#########################"
				+ "\n###### movTimpsini ######");
		siniestrosDAO.movTimpsini(accion,cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,aaapertu,status,nmsinies,ntramite,
				ptimport,iva,ivr,isr,cedular,enviado,nmsecsin);
		log.info(""
				+ "\n###### movTimpsini ######"
				+ "\n#########################"
				);
	}
	
	@Override
	public Map<String,String>obtenerAutorizacionesFactura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String nfactura) throws Exception
	{
		log.info(""
				+ "\n##########################################"
				+ "\n###### obtenerAutorizacionesFactura ######"
				);
		Map<String,String>autorizacionesFactura = siniestrosDAO.obtenerAutorizacionesFactura(cdunieco,cdramo
				,estado,nmpoliza,nmsuplem,nmsituac,aaapertu,status,nmsinies,nfactura);
		log.info("autorizaciones: "+autorizacionesFactura);
		log.info(""
				+ "\n###### obtenerAutorizacionesFactura ######"
				+ "\n##########################################"
				);
		return autorizacionesFactura;
	}
	
	@Override
	public void guardarTotalProcedenteFactura(String ntramite,String nfactura,String importe, String nmsecsin)throws Exception
	{
		log.info(""
				+ "\n###########################################"
				+ "\n###### guardarTotalProcedenteFactura ######");
		siniestrosDAO.guardarTotalProcedenteFactura(ntramite,nfactura,importe,nmsecsin);
		log.info(""
				+ "\n###### guardarTotalProcedenteFactura ######"
				+ "\n###########################################"
				);
	}
	
	@Override
	public String validaDocumentosAutServicio(String ntramite) throws Exception {
		try {
			return siniestrosDAO.validaDocumentosAutServicio(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public void turnarTramite(String ntramite,String cdsisrol,String cdusuari) throws Exception
	{
		log.info(""
				+ "\n###########################"
				+ "\n###### turnarTramite ######");
		siniestrosDAO.turnarTramite(ntramite,cdsisrol,cdusuari);
		log.info(""
				+ "\n###### turnarTramite ######"
				+ "\n###########################"
				);
	}
	
	@Override
	public List<Map<String,String>> obtenerUsuariosPorRol(String cdsisrol)throws Exception
	{
		List<Map<String,String>> lista = siniestrosDAO.obtenerUsuariosPorRol(cdsisrol);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.info("lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public Map<String,Object> moverTramite(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			,String swagente
			,Long stamp, boolean enviarCorreos
			) throws Exception
	{
		if(stamp==null)
		{
			stamp = System.currentTimeMillis(); 
		}
		else
		{
			logger.debug(Utils.log(stamp, "Se llama la actualizacion de status despues de esperar tarifa"));
		}
		log.debug(Utils.log(stamp
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ moverTramite @@@@@@"
				,"\n@@@@@@ ntramite="        , ntramite
				,"\n@@@@@@ comments="        , comments
				,"\n@@@@@@ cdusuariSesion="  , cdusuariSesion
				,"\n@@@@@@ cdsisrolSesion="  , cdsisrolSesion
				,"\n@@@@@@ cdusuariDestino=" , cdusuariDestino
				,"\n@@@@@@ cdsisrolDestino=" , cdsisrolDestino
				,"\n@@@@@@ cdmotivo="        , cdmotivo
				,"\n@@@@@@ cdclausu="        , cdclausu
				,"\n@@@@@@ swagente="        , swagente
				,"\n@@@@@@ enviarCorreos="   ,  enviarCorreos
				));
		
		int bloqueos = consultasDAO.recuperarConteoTbloqueoTramite(ntramite);
		logger.debug(Utils.log(stamp,"bloqueos=",bloqueos));
		
		Map<String,Object> res = new HashMap<String,Object>();
		
		/*
		 * si hay bloqueos entonces se manda asincrono y se enciende una bandera en mesa de control, este primer
		 * caso es el normal
		 */
		if(bloqueos==0)
		{
			res = siniestrosDAO.moverTramite(
					ntramite
					,nuevoStatus
					,comments
					,cdusuariSesion
					,cdsisrolSesion
					,cdusuariDestino
					,cdsisrolDestino
					,cdmotivo
					,cdclausu
					,swagente
					);
			if(enviarCorreos){
				flujoMesaControlManager.mandarCorreosStatusTramite(ntramite, cdsisrolSesion, false);
			}
			try
	        {
				cotizacionDAO.grabarEvento(new StringBuilder("\nTurnar tramite")
	        	    ,Constantes.MODULO_GENERAL    //cdmodulo
	        	    ,Constantes.EVENTO_TURNAR     //cdevento
	        	    ,new Date()   //fecha
	        	    ,cdusuariSesion
	        	    ,cdsisrolSesion
	        	    ,ntramite
	        	    ,"-1"
	        	    ,null
	        	    ,null
	        	    ,null
	        	    ,null
	        	    ,null
	        	    ,cdusuariDestino
	        	    ,cdsisrolDestino
	        	    ,nuevoStatus);
	        }
	        catch(Exception ex)
	        {
	        	logger.error("Error al grabar evento, sin impacto",ex);
	        }
		}
		/*
		 * en esta parte vamos a esperar a que los bloqueos terminen para hacer el turnado
		 */
		else
		{
			res.put("ESCALADO" , false);
			res.put("NOMBRE"   , "");
			res.put("ASYNC"    , "S");
			
			new MoverTramiteAsync(
					ntramite
					,nuevoStatus
					,comments
					,cdusuariSesion
					,cdsisrolSesion
					,cdusuariDestino
					,cdsisrolDestino
					,cdmotivo
					,cdclausu
					,swagente
					,stamp
					).start();
		}
		
		log.debug(Utils.log(stamp
				,"\n###### res=", res
				,"\n###### moverTramite ######"
				,"\n##########################"
				));
		return res;
	}
	
	private class MoverTramiteAsync extends Thread
	{
		private String ntramite
		               ,nuevoStatus
		               ,comments
		               ,cdusuariSesion
		               ,cdsisrolSesion
		               ,cdusuariDestino
		               ,cdsisrolDestino
		               ,cdmotivo
		               ,cdclausu
		               ,swagente;
		
		private long stamp;
		
		public MoverTramiteAsync(
				String ntramite
				,String nuevoStatus
				,String comments
				,String cdusuariSesion
				,String cdsisrolSesion
				,String cdusuariDestino
				,String cdsisrolDestino
				,String cdmotivo
				,String cdclausu
				,String swagente
				,long stamp
				)
		{
			this.ntramite        = ntramite;
			this.nuevoStatus     = nuevoStatus;
			this.comments        = comments;
			this.cdusuariSesion  = cdusuariSesion;
			this.cdsisrolSesion  = cdsisrolSesion;
			this.cdusuariDestino = cdusuariDestino;
			this.cdsisrolDestino = cdsisrolDestino;
			this.cdmotivo        = cdmotivo;
			this.cdclausu        = cdclausu;
			this.swagente        = swagente;
			this.stamp           = stamp;
		}
		
		@Override
		public void run()
		{
			try
			{
				/*
				 * en este paso vamos a recuperar el status actua del tramite, y se pondra como
				 * "En tarifa", posteriormente en otro paso le regresamos su status
				 */
				logger.debug(Utils.log(stamp, "Actualizando en status <En Tarifa>"));
				String statusOriginal = mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite,EstatusTramite.EN_TARIFA.getCodigo());
				
				int bloqueos = 0;
				
				do
				{
					logger.debug(Utils.log(stamp, "Recuperando conteo de tbloqueo de primer ciclo"));
					bloqueos = consultasDAO.recuperarConteoTbloqueoTramite(ntramite);
					
					logger.debug(Utils.log(stamp, "Conteo recuperado=",bloqueos));
					
					logger.debug(Utils.log(stamp, "Esperando 30 segundos del primer ciclo..."));
					Thread.sleep(1000l*30l);
					
				}
				while(bloqueos>0);
				
				do
				{
					logger.debug(Utils.log(stamp, "Recuperando conteo de tbloqueo de segundo ciclo"));
					bloqueos = consultasDAO.recuperarConteoTbloqueoTramite(ntramite);
					
					logger.debug(Utils.log(stamp,"Conteo recuperado=",bloqueos));
					
					logger.debug(Utils.log(stamp,"Esperando 30 segundos de segundo ciclo..."));
					Thread.sleep(1000l*30l);
					
				}
				while(bloqueos>0);
				
				/*
				 * despues de esperar que termine de tarificar, le regresamos su status anterior
				 */
				logger.debug(Utils.log(stamp,"Regresando el tramite a su status anterior"));
				mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite,statusOriginal);
				
				logger.debug(Utils.log(stamp,"Se actualiza el status del tramite asincronamente despues de esperar tarifa"));
				moverTramite(
					ntramite
					,nuevoStatus
					,comments
					,cdusuariSesion
					,cdsisrolSesion
					,cdusuariDestino
					,cdsisrolDestino
					,cdmotivo
					,cdclausu
					,swagente
					,stamp, true
					);
			}
			catch(Exception ex)
			{
				logger.error(Utils.log(stamp,"error al mover tramite despues de esperar tarifa"),ex);
			}
		}
	}
	
	@Override
	public void turnarAutServicio(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			) throws Exception
	{
		log.info(""
				+ "\n##########################"
				+ "\n###### moverTramite ######"
				);
		siniestrosDAO.turnarAutServicio(ntramite,nuevoStatus,comments,cdusuariSesion,cdsisrolSesion,cdusuariDestino,cdsisrolDestino,cdmotivo,cdclausu);
		log.info(""
				+ "\n###### moverTramite ######"
				+ "\n##########################"
				);
	}

	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneMontoArancelCPT(String tipoConcepto, String idProveedor, String idConceptoTipo ) throws Exception {
		try {
			return siniestrosDAO.obtieneMontoArancelCPT(tipoConcepto, idProveedor, idConceptoTipo );
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String porcentajeQuirurgico(String tipoMedico, String feAutorizacion) throws Exception {
		try {
			return siniestrosDAO.obtienePorcentajeQuirurgico(tipoMedico, feAutorizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	@Override
	public void eliminaDocumentosxTramite(String ntramite) throws Exception {
		// TODO Auto-generated method stub
		try {
			siniestrosDAO.eliminacionDocumentosxTramite(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String obtieneMesesTiempoEspera(String valorICDCPT, String nomTabla) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.obtieneMesesTiempoEspera(valorICDCPT,nomTabla);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String obtieneMesesTiempoEsperaICD(String cdramo, String cdtipsit, String cdicd, String dsplan) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.obtieneMesesTiempoEsperaICDCPT(cdramo,cdtipsit,cdicd,dsplan);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String obtieneUsuarioTurnadoSiniestro(String ntramite, String rolDestino) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.obtieneUsuarioTurnadoSiniestro(ntramite,rolDestino);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	/*@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String requiereAutorizacionServ(String cobertura, String subcobertura) throws Exception {
		try {
			return siniestrosDAO.requiereAutorizacionServicio(cobertura, subcobertura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}*/
	
	@Override
	public List<GenericVO> getConsultaListaRamoSalud() throws Exception {
		try {
			return siniestrosDAO.obtieneListadoRamoSalud();
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	
	@Override
	public List<Map<String, String>> requiereInformacionAdicional(String cobertura, String subcobertura, String cdramo, String cdtipsit) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cobertura_i", cobertura);
		params.put("pv_subcobertura_i",   subcobertura);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cdtipsit_i",   cdtipsit);
		log.debug("requiereInformacionAdicional params: "+params);
		return siniestrosDAO.obtieneDatosAdicionales(params);
	}
	
	@Override
	public List<Map<String, String>> listaConsultaCirculoHospitalarioMultisalud(String cdpresta, String cdramo, Date feautori) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_feautori_i",   feautori);
		log.debug("listaConsultaCirculoHospitalario params: "+params);
		return siniestrosDAO.obtieneDatosCirculoHospitalarioMultisalud(params);
	}
	
	@Override
	public String eliminarAsegurado(HashMap<String, Object> paramsTworkSin) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.eliminarAsegurado(paramsTworkSin);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> obtenerDatosAdicionalesCobertura(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneDatosAdicionalesCobertura(params);
	}


	@Override
	public String obtieneTramiteEnProceso(String nfactura, String cdpresta, String ptimport) throws Exception {
		return siniestrosDAO.obtieneTramiteEnProceso(nfactura, cdpresta, ptimport);
	}
	
	@Override
	public List<Map<String,String>> obtenerAseguradosTramite(String ntramite,String nfactura) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , nfactura);
		log.debug("obtenerFacturasTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.obtenerAseguradosTramite(params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}

	@Override
	public List<Map<String,String>> obtenerInfAseguradosTramite(String ntramite) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		log.debug("obtenerFacturasTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.obtenerInfAseguradosTramite(params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public String actualizaValorMC(HashMap<String, Object> modMesaControl) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.actualizaValorMC(modMesaControl);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<GenericVO> getconsultaListaTipoAtencion(String cdramo, String modalidad, String tipoPago) throws Exception {
		try {
			return siniestrosDAO.obtieneListaTipoAtencion(cdramo, modalidad, tipoPago);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	
	}

	@Override
	public List<Map<String, String>> getConsultaListaAutServicioSiniestro(String cdperson) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdperson_i", cdperson);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaAutirizacionServicio(params);
	}
	
	@Override
	public List<Map<String, String>> getConsultaListaMSiniestMaestro(String cdunieco,String cdramo, String estado, String nmpoliza, 
																	 String nmsuplem,String nmsituac,String status) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_status_i", status);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaMsiniestMaestro(params);
	}

	@Override
	public List<Map<String, String>> getConsultaDatosValidacionSiniestro(String ntramite, String nfactura,String tipoPago) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_tipoPago_i", tipoPago);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaDatosValidacionSiniestro(params);
	}
	
	/*params.get("cdunieco"),params.get("cdramo"),params.get("estado"),
	params.get("nmpoliza"),params.get("cdperson"),params.get("nmsinref")*/
	@Override
	public List<Map<String, String>> getConsultaDatosSumaAsegurada(String cdunieco, String cdramo,String estado,String nmpoliza, String cdperson, String nmsinref) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nmsinref_i", nmsinref);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaDatosSumaAsegurada(params);
	}
	
	@Override
	public List<Map<String, String>> getConsultaDatosValidacionAjustadorMed(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaDatosValidacionAjustadorMed(params);
	}
	
	@Override
	public String validaCdTipsitAltaTramite(HashMap<String, Object> paramTramite) throws Exception {
		try {
			return siniestrosDAO.validaCdTipsitAltaTramite(paramTramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> listaSumaAseguradaPeriodoEsperaRec(String cdramo, String cobertura, String subcobertura, Date feEfecto) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_cobertura_i", cobertura);
		params.put("pv_subcobertura_i",   subcobertura);
		params.put("pv_feefecto_i",   feEfecto);
		log.debug("Valor Recupera : "+params);
		return siniestrosDAO.obtieneSumaAseguradaPeriodoEsperaRec(params);
	}
	
	@Override
	public List<Map<String, String>> listaEsquemaSumaAseguradaRec(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsituac) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i",   nmpoliza);
		params.put("pv_nmsituac_i",   nmsituac);
		log.debug("Valor Esquema Suma Asegurada : "+params);
		return siniestrosDAO.obtieneEsquemaSumaAseguradaRec(params);
	}

	@Override
	public List<Map<String, String>> listaPeriodoEsperaAsegurado(String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsituac, Date feOcurre) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i",   nmpoliza);
		params.put("pv_nmsituac_i",   nmsituac);
		params.put("pv_feocurre_i",   feOcurre);
		log.debug("Valores de entrada Asegurado Recupera : "+params);
		return siniestrosDAO.obtienePeriodoEsperaAsegurado(params);
	}

	@Override
	public List<Map<String, String>> obtieneMontoPagoSiniestro(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		log.debug("obtieneMontoPagoSiniestro params: "+params);
		return siniestrosDAO.obtieneMontoPagoSiniestro(params);
	}
	
	@Override
	public List<GenericVO> getConsultaListaConceptoPago(String cdramo) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoConceptoPago(cdramo);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaAseguradoPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdperson) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoAseguradoPoliza(cdunieco,cdramo,estado, nmpoliza, cdperson);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneDatosBeneficiario(String cdunieco,String cdramo, 
					String estado, String nmpoliza,String cdperson) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_cdperson_i", cdperson);
		log.debug("obtieneDatosBeneficiario params: "+params);
		return siniestrosDAO.obtieneDatosBeneficiario(params);
	}
	
	@Override
	public List<Map<String, String>> obtenerDatoMsiniper(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		log.debug("obtieneMontoPagoSiniestro params: "+params);
		return siniestrosDAO.obtieneDatoMsiniper(params);
	}

	public void setCotizacionDAO(CotizacionDAO cotizacionDAO) {
		this.cotizacionDAO = cotizacionDAO;
	}
	
	@Override
	public List<Map<String, String>> getConsultaConfiguracionProveedor(String cdpresta) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i", cdpresta);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaConfiguracionProveedor(params);
	}
	
	@Override
	public List<Map<String, String>> getConsultaLayoutConfigurados(String descLayout) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cadena_i", descLayout);
		log.debug("obtenerConsultaLayoutConfigurados params: "+params);
		return siniestrosDAO.obtieneListaLayoutConfigurados(params);
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneAplicaConceptoIVA(String idConcepto) throws Exception {
		try {
			return siniestrosDAO.obtieneAplicaConceptoIVA(idConcepto);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String guardaConfiguracionProveedor(String cdpresta, String tipoLayout, String aplicaIVA,
			String secuenciaIVA, String aplicaIVARET, String cduser, Date fechaProcesamiento, String proceso) throws Exception {
		try {
			return siniestrosDAO.guardaConfiguracionProveedor(cdpresta, tipoLayout, aplicaIVA, secuenciaIVA, aplicaIVARET,cduser,fechaProcesamiento, proceso);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO>obtenerAtributosLayout(String descripcion) throws Exception
	{
		List<GenericVO>lista=siniestrosDAO.obtenerAtributosLayout(descripcion);
		if(lista==null)
		{
			lista = new ArrayList<GenericVO>();
		}
		log.debug("obtenerCodigosMedicos lista size: "+lista.size());
		return lista;
	}

	@Override
	public String guardaLayoutProveedor(String cdpresta, String tipoLayout, String claveAtributo,
			String claveFormatoAtributo, String valorMinimo,
			String valorMaximo, String columnaExcel, String claveFormatoFecha,
			String atributoRequerido, String nmordina, String tipoAccion) throws Exception {
		// TODO Auto-generated method stub
		try {
			String accion = null;
			logger.debug("Valor de tipoAccion -->"+tipoAccion);
			if(tipoAccion == null || tipoAccion == ""){
				accion = Constantes.INSERT_MODE;
			}else{
				accion = tipoAccion;
			}
			
			HashMap<String,Object> paramsConfLayout=new HashMap<String,Object>();
			paramsConfLayout.put("pv_cdpresta_i",cdpresta);
			paramsConfLayout.put("pv_cveconfi_i",tipoLayout);
			paramsConfLayout.put("pv_cveatri_i", claveAtributo);
			paramsConfLayout.put("pv_nmordina_i",nmordina);
			paramsConfLayout.put("pv_cveformato_i",claveFormatoAtributo);
			paramsConfLayout.put("pv_valormax_i",valorMaximo);
			paramsConfLayout.put("pv_valormin_i", valorMinimo);
			paramsConfLayout.put("pv_cveexcel_i", columnaExcel);
			paramsConfLayout.put("pv_formatfech_i",claveFormatoFecha);
			paramsConfLayout.put("pv_swobliga_i",atributoRequerido);
			paramsConfLayout.put("pv_accion_i",accion);
			
			log.debug("paramsConfLayout params: "+paramsConfLayout);
			return siniestrosDAO.guardaLayoutProveedor(paramsConfLayout);
		} catch (ParseException parseExc) {
			throw new Exception(parseExc.getMessage(), parseExc);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> consultaConfiguracionLayout(String cdpresta) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i",   cdpresta);
		log.debug("consultaConfiguracionLayout params: "+params);
		return siniestrosDAO.obtieneConfiguracionLayout(params);
	}
	
	@Override
	public List<Map<String, String>> guardaHistorialSiniestro(String ntramite, String nfactura) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i",   ntramite);
		params.put("pv_nfactura_i",   nfactura);
		log.debug("guardaHistorialSiniestro params: "+params);
		return siniestrosDAO.guardaHistorialSiniestro(params);
	}
	
	@Override
	public String guardaListaRecupera(String ntramite,
			String nfactura,
			String cdgarant, 
			String cdconval, 
			String cantporc, 
			String ptimport, 
			String tipoAccion) throws Exception {
		// TODO Auto-generated method stub
		try {
			
			HashMap<String,Object> paramsMRecupera=new HashMap<String,Object>();
			paramsMRecupera.put("pv_ntramite_i",ntramite);
			paramsMRecupera.put("pv_nfactura_i",nfactura);
			paramsMRecupera.put("pv_cdgarant_i",cdgarant);
			paramsMRecupera.put("pv_cdconval_i",cdconval);
			paramsMRecupera.put("pv_cantporc_i",cantporc);
			paramsMRecupera.put("pv_ptimport_i",ptimport);
			paramsMRecupera.put("pv_accion_i", tipoAccion);
			log.debug("guardaListaMRECUPERA params: "+paramsMRecupera);
			return siniestrosDAO.guardaInfoRecupera(paramsMRecupera);
		} catch (ParseException parseExc) {
			throw new Exception(parseExc.getMessage(), parseExc);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String,String>>obtieneInformacionRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String ntramite, String nfactura) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.obtieneInformacionRecupera(cdunieco,cdramo, estado, nmpoliza, nmsuplem,
				nmsituac, feEfecto, ntramite, nfactura);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("obtieneInformacionRecupera lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String,String>>obtieneEsquemaSumAseguradaRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String cdgarant, String cdconval) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.obtieneEsquemaSumAseguradaRecupera(cdunieco,cdramo, estado, nmpoliza, nmsuplem,
				nmsituac, feEfecto, cdgarant, cdconval);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("obtieneInformacionRecupera lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public void P_MOV_MRECUPERA(String ntramite,String nfactura, String cdgarant, String cdconval,
			String cantporc,String ptimport, String accion) throws Exception
	{
		siniestrosDAO.P_MOV_MRECUPERA(ntramite, nfactura, cdgarant, cdconval, cantporc, ptimport, accion);
	}
	
	@Override
	public String actualizaTelefonoEmailAsegurado(HashMap<String, Object> paramsAsegurado) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.actualizaTelefonoEmailAsegurado(paramsAsegurado);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public void guardaAutorizacionConceptos(String cdunieco, String cdramo, String estado, String nfactura,
			String nmautser, String cdpresta, String cdperson) throws Exception
	{
		//Map<String,String> params = new HashMap<String,String>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i",cdunieco);
		params.put("pv_cdramo_i",cdramo);
		params.put("pv_estado_i",estado);
		params.put("pv_nfactura_i",nfactura);
		params.put("pv_nmautser_i",nmautser);
		params.put("pv_cdpresta_i",cdpresta);
		params.put("pv_cdperson_i",cdperson);
		log.debug("guardaAutorizacionConceptos params: "+params);
		siniestrosDAO.guardaAutorizacionConceptos(params);
		log.debug("guardaAutorizacionConceptos end");
	}
	
	@Override
	public List<Map<String,String>> cargaHistorialCPTPagados(Map<String,String> params) throws Exception
	{
		List<Map<String,String>>lista=siniestrosDAO.cargaHistorialCPTPagados(params);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		log.debug("cargaHistorial lista size:"+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String,String>> listaSiniestrosInfAsegurados(String ntramite) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		log.debug("listaSiniestrosTramite params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.listaSiniestrosInfAsegurados(params);
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("listaSiniestrosTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public List<GenericVO> getConsultaListaAseguraAutEspecial(String ntramite, String nfactura) throws Exception {
		try {
			return siniestrosDAO.obtieneListaAseguraAutEspecial(ntramite,nfactura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<CoberturaPolizaVO> getConsultaListaCoberturaProducto(
			HashMap<String, Object> paramCobertura) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCoberturaProducto(paramCobertura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String guardaListaAutorizacionEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception {
		// TODO Auto-generated method stub
		try {
			return siniestrosDAO.guardaListaAutorizacionEspecial(paramsAutoriEspecial);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerConfiguracionAutEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception
	{
		List<Map<String,String>> lista = siniestrosDAO.obtenerConfiguracionAutEspecial(paramsAutoriEspecial);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public String asociarAutorizacionEspecial(HashMap<String, Object> paramAutEspecial) throws Exception {
		try {
			return siniestrosDAO.asociarAutorizacionEspecial(paramAutEspecial);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerDatosAutorizacionEspecial(String nmautespecial) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_nmautesp_i" , nmautespecial);
		log.debug("obtenerDatosAutorizacionEspecial params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.obtenerDatosAutorizacionEspecial(params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerDatosAutorizacionEspecial lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public List<Map<String, String>> getConsultaExisteCoberturaTramite(String ntramite, String tipoPago) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_formapago_i", tipoPago);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaExisteCoberturaTramite(params);
	}
	
	@Override
	public String validaExisteConfiguracionProv(String cdpresta, String tipoLayout) throws Exception {
		try {
			return siniestrosDAO.validaExisteConfiguracionProv(cdpresta, tipoLayout);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> requiereConfiguracionLayoutProveedor(String cdpresta, String cveLayout) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_cveconfi_i", cveLayout);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneConfiguracionLayoutProveedor(params);
	}
	
	@Override
	public List<GenericVO> getConsultaListaContrareciboAutEsp(String cdramo, String ntramite) throws Exception {
		try {
			return siniestrosDAO.obtieneListaContrareciboAutEsp(cdramo,ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaFacturaTramite(String ntramite, String nfactura) throws Exception {
		try {
			return siniestrosDAO.obtieneListaFacturaTramite(ntramite,nfactura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> procesaPagoAutomaticoSisco(String usuario, String tipoProceso) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdusuari_i", usuario);
		params.put("pv_tipoproc_i", tipoProceso);
		return siniestrosDAO.procesaPagoAutomaticoSisco(params);
	}
	
	@Override
	public List<Map<String, String>> getValidaArancelesTramitexProveedor(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		return siniestrosDAO.obtieneValidaconAranceleTramite(params);
	}
	
	@Override
	public String obtieneMontoTramitePagoDirecto(HashMap<String, Object> paramsPagoDirecto) throws Exception {
		try {
			return siniestrosDAO.obtieneMontoTramitePagoDirecto(paramsPagoDirecto);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> getValidaFacturaMontoPagoAutomatico(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		return siniestrosDAO.obtieneValidaFacturaMontoPagoAutomatico(params);
	}
	
	@Override
	public String guardaListaFacturaPagoAutomatico(String ntramite, String nfactura,String factInicial) throws Exception {
		// TODO Auto-generated method stub
		try {
			HashMap<String,Object> datosFactura=new HashMap<String,Object>();
			datosFactura.put("pv_ntramite_i",ntramite);
			datosFactura.put("pv_nfactura_i",nfactura);
			datosFactura.put("pv_nfacori_i",factInicial);
			return siniestrosDAO.guardaListaFacturaPagoAutomatico(datosFactura);
		} catch (ParseException parseExc) {
			throw new Exception(parseExc.getMessage(), parseExc);
		}
	}
	
	@Override
	public void actualizaTurnadoMesaControl(String ntramite) throws Exception {
		// TODO Auto-generated method stub
		try {
			siniestrosDAO.actualizaTurnadoMesaControl(ntramite);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> listadoFacturasxControl(String ntramite) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_ntramite_i", ntramite);
		return siniestrosDAO.obtieneListadoFacturasxControntrol(params);
	}
	
	@Override
	public List<Map<String, String>> obtieneConfiguracionLayoutExcel(String cdpresta, String cveLayout, String campoExcel ) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_cveexcel_i", campoExcel);
		params.put("pv_cveconfi_i", cveLayout);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneConfiguracionLayoutExcel(params);
	}
	
	@Override
	//cdpresta, nmconsult, tipoproc, usuario.getUser()
	
	public List<Map<String, String>> procesaPagoAutomaticoLayout(String cdpresta, String nmconsult,
			String tipoproc, String usuario) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdpresta_i", cdpresta);
		params.put("pv_nmconsult_i", nmconsult);
		params.put("pv_cdusuari_i", usuario);
		params.put("pv_tipoproc_i", tipoproc);
		log.debug("procesaPagoAutomaticoLayout params: "+params);
		return siniestrosDAO.procesaPagoAutomaticoLayout(params);
	}
	
	@Override
	public String existeRegistrosProcesarSISCO() throws Exception {
		// TODO Auto-generated method stub
		return siniestrosDAO.existeRegistrosProcesarSISCO();
	}
	
	@Override
	public String validaPersonaSisaSicaps(HashMap<String, Object> paramPersona) throws Exception {
		try {
			return siniestrosDAO.validaPersonaSisaSicaps(paramPersona);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String validaFeocurreAsegurado(HashMap<String, Object> paramPersona) throws Exception {
		try {
			return siniestrosDAO.validaFeocurreAsegurado(paramPersona);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneInfCoberturaInfonavit(HashMap<String, Object> paramsInfonavit) throws Exception {
		try {
			return siniestrosDAO.obtieneInfCoberturaInfonavit(paramsInfonavit);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> listaConsultaInfCausaSiniestroProducto(HashMap<String, Object> paramsCausaSini) throws Exception {
		try {
			return siniestrosDAO.obtieneInfCausaSiniestroProducto(paramsCausaSini);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public Map<String, Object> actualizaDatosGeneralesConceptos(String cdunieco, String cdramo, String estado, 
			String nmpoliza, String nmsuplem, String aaapertu, String nmsinies, String cdgarant, 
			String cdconval) throws Exception {

		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_estado_i",   estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_aaapertu_i", aaapertu);
		params.put("pv_nmsinies_i", nmsinies);
		params.put("pv_cdgarant_i",  cdgarant);
		params.put("pv_cdconval_i",  cdconval);
		log.debug("actualizaDatosGeneralesConceptos params: "+params);
		return siniestrosDAO.actualizaDatosGeneralesConceptos(params);
	}
	
	@Override
	public String validaExisteCodigoConcepto(HashMap<String, Object> paramExiste) throws Exception {
		try {
			return siniestrosDAO.validaExisteCodigoConcepto(paramExiste);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> listaConsultaInfAseguradoCobSubCoberturas(HashMap<String, Object> paramsCausaSini) throws Exception {
		try {
			return siniestrosDAO.obtieneInfAseguradoCobSubCoberturas(paramsCausaSini);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String actualizarRegistroTimpsini(HashMap<String, Object> datosActualizacion) throws Exception {
		try {
			return siniestrosDAO.actualizaRegistroTimpsini(datosActualizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public Map<String, Object> actualizaDatosGeneralesCopago(String cdunieco, String cdramo, String estado, String nmpoliza,
			String nmsuplem, String nmsituac, String nmsinies, String ntramite, String nfactura, String cdgarant,
			String cdconval, String deducible, String copago, String nmcallcenter,String aplicaCambio, 
			String feingreso,String feegreso, String cveEvento, String cveAlta,String accion) throws Exception {

		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_estado_i",   estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_nmsituac_i", nmsituac);
		params.put("pv_nmsinies_i", nmsinies);
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_cdgarant_i", cdgarant);
		params.put("pv_cdconval_i", cdconval);
		params.put("pv_deducible_i",deducible);
		params.put("pv_copago_i", 	copago);
		params.put("pv_nmautser_i", nmcallcenter);
		params.put("pv_swautori_i", aplicaCambio);
		logger.debug("pv_feingreso_i=='"+ StringUtils.isBlank(feingreso) +"'");
		logger.debug("pv_feegreso_i=='"+ StringUtils.isBlank(feegreso) +"'");
		
		params.put("pv_feingreso_i", StringUtils.isNotBlank(feingreso) && !feingreso.equals("null")  ? Utils.parse(feingreso) : null);	//(EGS)
		params.put("pv_feegreso_i",  StringUtils.isNotBlank(feegreso) && !feegreso.equals("null")  ? Utils.parse(feegreso) :null);	//(EGS)
		params.put("pv_cveEvento_i", cveEvento);
		params.put("pv_cveAlta_i", cveAlta);		
		params.put("pv_accion_i", accion);
		log.debug("actualizaDatosGeneralesSiniestro params: "+params);
		return siniestrosDAO.actualizaDatosGeneralesCopago(params);
	}
	
	@Override
	public String actualizarDeducibleCopagoConceptos(HashMap<String, Object> datosActualizacion) throws Exception {
		try {
			return siniestrosDAO.actualizaDeducibleCopagoConceptos(datosActualizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
    
	@Override
	public List<Map<String, String>> getConsultaDatosAutEspecial(String cdramo, String tipoPago, String ntramite, 
			String nfactura, String cdperson) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_tipoPago_i", tipoPago);
		params.put("pv_ntramite_i", ntramite);
		params.put("pv_nfactura_i", nfactura);
		params.put("pv_cdperson_i", cdperson);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaDatosAutEspecial(params);
	}
	
	@Override
	public String actualizarValImpuestoProv(HashMap<String, Object> datosActualizacion) throws Exception {
		try {
			return siniestrosDAO.actualizaValImpuestoProv(datosActualizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneValidacionAsegurado(String cdperson, Date feocurre, String nmpoliza ) throws Exception {
		try {
			return siniestrosDAO.obtieneValidacionAsegurado(cdperson, feocurre, nmpoliza );
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerInfImporteAsegTramiteAseg(String tipopago,String ntramite, String nfactura) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_tipopago_i" , tipopago);
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , nfactura);
		log.debug("obtenerInfImporteAsegTramiteAseg params: "+params);
		List<Map<String,String>> lista = siniestrosDAO.obtieneInfImporteAsegTramiteAseg(params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		log.debug("lista: "+lista);
		log.debug("obtenerFacturasTramite lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public String actualizaValoresMCSiniestros(HashMap<String, Object> datosMC) throws Exception {
		try {
			return siniestrosDAO.actualizaValoresMCSiniestros(datosMC);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> obtenerAseguradosxTworksin(String ntramite, String nfactura) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_nfactura_i" , nfactura);
		log.debug("obtenerAseguradosxTworksin params: "+params);
		List<Map<String,String>> tramite = siniestrosDAO.obtenerAseguradosxTworksin(params);
		log.debug("obtenerAseguradosxTworksin tramite: "+tramite);
		return tramite;
	}

	@Override
	public void eliminarFaltantesAsegurados() throws Exception {
		siniestrosDAO.eliminaFaltantesAsegurados();
	}

	@Override
	public void getEliminaAseguradoEspecifico(String ntramite, String nfactura, String cdperson, Date feocurre) throws Exception {
		try {
			siniestrosDAO.eliminacionAseguradoEspecifico(ntramite,nfactura,cdperson,feocurre);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
		
	}
	
   @Override
    public List<Map<String,String>> cargaICDExcluidosAsegurados(Map<String,String> params) throws Exception
    {
        List<Map<String,String>>lista=siniestrosDAO.cargaICDExcluidosAsegurados(params);
        if(lista==null)
        {
            lista=new ArrayList<Map<String,String>>();
        }
        log.debug("cargaHistorial lista size:"+lista.size());
        return lista;
    }

    
    @Override
    public List<GenericVO> getConsultaListaTipoEvento(String cdramo, String cdtipsit, String cdgarant, String reporte) throws Exception {
        try {
            List<GenericVO> lista = siniestrosDAO.obtieneListadoTipoEvento(cdramo, cdtipsit, cdgarant, reporte);
            if(lista==null)
            {
                lista= new ArrayList<GenericVO>();
            }
            log.debug("getConsultaListaSubcobertura lista size: "+lista.size());
            return lista;
        } catch (DaoException daoExc) {
            throw new Exception(daoExc.getMessage(), daoExc);
        }
    }
    
    @Override
    public List<Map<String,String>> obtenerDatosValTipoEventoAlta(String cdramo, String cdtipsit, String cdgarant, String cdconval) throws Exception
    {
        Map<String,String> params = new HashMap<String,String>();
        params.put("pv_cdramo_i"    , cdramo);
        params.put("pv_cdtipsit_i"  , cdtipsit);
        params.put("pv_cdgarant_i"  , cdgarant);
        params.put("pv_cdconval_i"  , cdconval);
        log.debug("obtenerDatosValTipoEventoAlta params: "+params);
        List<Map<String,String>> lista = siniestrosDAO.obtenerDatosValTipoEventoAlta(params);
        if(lista==null)
        {
            lista = new ArrayList<Map<String,String>>();
        }
        log.debug("lista: "+lista);
        log.debug("obtenerDatosAutorizacionEspecial lista size: "+lista.size());
        return lista;
    }
    
    @Override
    public List<GenericVO> getConsultaListaValidacionesGenerales() throws Exception {
        try {
            List<GenericVO> lista = siniestrosDAO.obtieneListadoValidacionesGrales();
            if(lista==null)
            {
                lista= new ArrayList<GenericVO>();
            }
            log.debug("getConsultaListaSubcoberturaTotales lista size: "+lista.size());
            return lista;
        } catch (DaoException daoExc) {
            throw new Exception(daoExc.getMessage(), daoExc);
        }
    }
	
	@Override
	public String validaProveedorPD(String ntramite) throws Exception{ // (EGS)
		try{
			return siniestrosDAO.validaProveedorPD(ntramite);
		} catch (DaoException daoExc){
			throw new Exception(daoExc.getMessage(),daoExc);
		}
	}

	@Override
	public String actualizarReqautes(String reqautes, String ntramite, String nfactura, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac, String aaapertu, String status,
			String nmsinies) throws Exception {	// (EGS)
		try{
			return siniestrosDAO.actualizarReqautes(reqautes, ntramite, nfactura, cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, aaapertu, status, nmsinies);
		}catch (DaoException daoExc){
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String validaAutEspLimMedi(String nmautesp, String ntramite, String nfactura, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsituac, String nmsinies) throws Exception { // (EGS)
		try{
		return siniestrosDAO.validaAutEspLimMedi(nmautesp, ntramite, nfactura, cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, nmsinies);
		}catch (DaoException daoExc){
			throw new Exception(daoExc.getMessage(),daoExc);
		}
	}
	
	@Override
	public List<AseguradoVO> obtenerTramiteCompletoAsegurados(String ntramite, long start, long limit) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		List<AseguradoVO> asegurados;
		
		params.put("pv_ntramite_i" , ntramite);
		log.debug("obtenerTramiteCompleto params: "+params);
		Map<String,String> tramite = siniestrosDAO.obtenerTramiteCompleto(params);
		//logger.debug("tramite: "+tramite);
		PolizaVO poliza = new PolizaVO();
		poliza.setCdunieco(tramite.get("CDUNIECO"));
		poliza.setCdramo(tramite.get("CDRAMO"));
		poliza.setEstado(tramite.get("ESTADO"));
		poliza.setNmpoliza(tramite.get("NMPOLIZA"));
		poliza.setNmsuplem(tramite.get("NMSUPLEM"));
		//logger.debug("poliza: "+ poliza);
		asegurados = consultasPolizaDAOICE.obtieneAsegurados(poliza, start, limit);
		//log.debug("obtenerTramiteCompleto tramite: "+asegurados);
		return asegurados;
	}
	
	@Override
	public ManagerRespuestaImapVO pantallaReservaSiniestralidad(String cdsisrol) throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ pantallaReservaSiniestralidad @@@@@@")
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.toString());
		ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
		String paso = "";
		//obtener componentes
		try
		{
			paso = "antes de obtener componentes busqueda";
			List<ComponenteVO>componentesBusqueda=pantallasDAO.obtenerComponentes(
					null,null,null,null,null,cdsisrol,"PANTALLA_RESERVA_SINIESTRALIDAD","BUSQUEDA",null);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(componentesBusqueda, true, false, true, false, false, false);
			
			Map<String,Item> imap = new HashMap<String,Item>();
			resp.setImap(imap);			
			imap.put("busquedaItems" , gc.getItems());
			
			
				
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, ex.getMessage().toString());
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ pantallaReservaSiniestralidad @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString());
		return resp;
	}
	
	@Override
	public List<Map<String, String>> getDatosRenovaSiniestralidad(String pv_CdUniEco_i 
																,String pv_CdRamo_i  
																,String pv_nmpoliza_i
																,String pv_cdperson  
																,String pv_nmsinies
																,String pv_fecdesde  
																,String pv_fechasta  
																,String pv_start_i
																,String pv_limit_i 
																,String pv_ntramite_i) throws Exception {
		
		
		return siniestrosDAO.obtieneListaDatosRenovaSiniestralidad(pv_CdUniEco_i 
				,pv_CdRamo_i  
				,pv_nmpoliza_i
				,pv_cdperson  
				,pv_nmsinies
				,Utils.formateaFecha(pv_fecdesde)  
				,Utils.formateaFecha(pv_fechasta)  
				,pv_start_i
				,pv_limit_i 
				,pv_ntramite_i);
	}
	
	@Override
	public List<Map<String, String>> obtieneListaTopIcd(String pv_CdUniEco_i 
																,String pv_CdRamo_i  
																,String pv_nmpoliza_i
																,String pv_cdperson  
																,String pv_nmsinies
																,String pv_fecdesde  
																,String pv_fechasta  
																,String pv_top ) throws Exception {
		
		
		return siniestrosDAO.obtieneListaTopIcd(pv_CdUniEco_i 
				,pv_CdRamo_i  
				,pv_nmpoliza_i
				,pv_cdperson  
				,pv_nmsinies
				,Utils.formateaFecha(pv_fecdesde)  
				,Utils.formateaFecha(pv_fechasta)  
				,pv_top );
	}
	
	@Override
	public List<Map<String, String>> obtieneListaReservas(String pv_CdUniEco_i 
																,String pv_CdRamo_i  
																,String pv_nmpoliza_i
																,String pv_cdperson  
																,String pv_nmsinies
																,String pv_fecdesde  
																,String pv_fechasta  ) throws Exception {
		
		
		return siniestrosDAO.obtieneListaReservas(pv_CdUniEco_i 
				,pv_CdRamo_i  
				,pv_nmpoliza_i
				,pv_cdperson  
				,pv_nmsinies
				,Utils.formateaFecha(pv_fecdesde)  
				,Utils.formateaFecha(pv_fechasta)  
				);
	}
	
	@Override
	public List<Map<String, String>> obtieneListaReservasSolo(String pv_CdUniEco_i 
																,String pv_CdRamo_i  
																,String pv_nmpoliza_i
																,String pv_cdperson  
																,String pv_nmsinies
																,String pv_fecdesde  
																,String pv_fechasta  ) throws Exception {
		
		
		return siniestrosDAO.obtieneListaReservasSolo(pv_CdUniEco_i 
				,pv_CdRamo_i  
				,pv_nmpoliza_i
				,pv_cdperson  
				,pv_nmsinies
				,Utils.formateaFecha(pv_fecdesde)  
				,Utils.formateaFecha(pv_fechasta)  
				);
	}
	
	@Override
	public List<Map<String, String>> obtieneListaProveedores(String pv_cdpresta_i 
																,String pv_idespecialidad_i  
																,String pv_tipoProveedor_i
																,String pv_idZonaHosp_i  ) throws Exception {
		
		
		return siniestrosDAO.obtieneListaProveedores( pv_cdpresta_i, 
				 pv_idespecialidad_i, 
				 pv_tipoProveedor_i,
				 pv_idZonaHosp_i);
	}
	@Override
	public List<GenericVO> getConsultaListaCPTUnico(String cdicd) // (EGS)
			throws Exception {
		try {
			return siniestrosDAO.obtieneListadoCPTUnico(cdicd);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaTipoMedicos(String cdicd, String cpts)
			throws Exception {
		String paso = "";
		try {
			paso = "cdicd,cpts ==> "+cdicd+""+cpts;
			logger.debug("Valor de respuesta ==> "+paso);
			return siniestrosDAO.obtieneListadoTipoMedicos(cdicd,cpts);			
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneImporteArancelGNP(String cdpresta,String cpt) throws Exception {
		try {
			return siniestrosDAO.obtieneImporteArancelGNP(cdpresta,cpt);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<GenericVO> getConsultaListaTiposProveedores() throws Exception {
		try {
			List<GenericVO> lista = siniestrosDAO.obtieneListadoTiposProveedores();
			if(lista==null)
			{
				lista= new ArrayList<GenericVO>();
			}
			log.debug("getConsultaListaSubcoberturaTotales lista size: "+lista.size());
			return lista;
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<Map<String, String>> getConsultaListaDetalleSiniestro( String pv_CdUniEco_i
																	,String pv_CdRamo_i  
																	,String pv_nmpoliza_i
																	,String pv_cdperson  
																	,String pv_ntramite_i
																	,String pv_nmsinies  
																	,String pv_fecdesde  
																	,String pv_fechasta ) throws Exception {
		try {
			return siniestrosDAO.obtieneListadoDetalleSiniestro( pv_CdUniEco_i
																, pv_CdRamo_i  
																, pv_nmpoliza_i
																, pv_cdperson  
																, pv_ntramite_i
																, pv_nmsinies  
																,Utils.formateaFecha(pv_fecdesde)  
																,Utils.formateaFecha(pv_fechasta) );
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneMedicoEquipoQuirurgico(String numAutorizacion) throws Exception {
		try {
			return siniestrosDAO.obtieneMedicoEquipoQuirurgico(numAutorizacion);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public List<AutorizacionServicioVO> guardarAutorizacionServicioGNP(HashMap<String, Object> paramsR)throws Exception {
		try {
			return siniestrosDAO.guardarAutorizacionServicioGNP(paramsR);			
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtieneDatosGeneralesICD(String cdunieco, String cdramo, String estado, String nmpoliza, String cdicd, String cdperson) throws Exception {
		try {
			return siniestrosDAO.obtieneDatosGeneralesICD(cdunieco, cdramo, estado, nmpoliza, cdicd, cdperson);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
    
	@Override
	public List<Map<String, String>> getConsultaDatosSumaAseguradaGNP(String cdunieco, String cdramo,String estado,String nmpoliza, String cdperson, String nmsinref) throws Exception {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_nmsinref_i", nmsinref);
		log.debug("obtenerDatosAdicionalesCobertura params: "+params);
		return siniestrosDAO.obtieneListaDatosSumaAseguradaGNP(params);
	}
	
	@Override
	//String tipoConcepto, String idProveedor, String idConceptoTipo
	public String obtenerValidacionExclusionICD(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,  String nmsituac,String cdicd) throws Exception {
		try {
			return siniestrosDAO.obtenerValidacionExclusionICD(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, cdicd);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
}