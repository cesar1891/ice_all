package mx.com.gseguros.portal.endosos.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.cotizacion.model.ParametroEndoso;
import mx.com.gseguros.portal.despachador.model.RespuestaTurnadoVO;
import mx.com.gseguros.portal.despachador.service.DespachadorManager;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

@Service
public class EndososManagerImpl implements EndososManager
{
    private static final Logger logger          = LoggerFactory.getLogger(EndososManagerImpl.class);
    private SimpleDateFormat    renderFechas    = new SimpleDateFormat("dd/MM/yyyy");
    
    @Value("${ruta.servidor.reports}")
	private String rutaServidorReportes;
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
    @Value("${pass.servidor.reports}")
    private String passwordServidorReportes;
    
	@Value("${dominio.server.layouts2}")
	private String dominioServerLayouts2;
    
    @Autowired
	private EndososDAO      endososDAO;
    @Autowired
	private CotizacionDAO   cotizacionDAO;
    @Autowired
	private PantallasDAO    pantallasDAO;
    @Autowired
	private MesaControlDAO  mesaControlDAO;
    @Autowired
	private PersonasDAO     personasDAO;
	
	@Autowired
	private AutosSIGSDAO    autosDAOSIGS;
	
	@Autowired
	private Ice2sigsService ice2sigsService;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private DocumentosManager documentosManager;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private DespachadorManager despachadorManager;
	
	@Override
	public List<Map<String, String>> obtenerEndosos(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager obtenerEndosos params: "+params);
		List<Map<String,String>> lista=endososDAO.obtenerEndosos(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerEndosos lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public Map<String, String> guardarEndosoNombres(Map<String, Object> params) throws Exception
	{
		logger.debug("EndososManager guardarEndosoNombres params: "+params);
		Map<String,String> mapa=endososDAO.guardarEndosoNombres(params);
		logger.debug("EndososManager guardarEndosoNombres response map: "+mapa);
        return mapa;
	}
	
	@Override
	public List<Map<String, String>> retarificarEndosos(Map<String, String> params) throws Exception
	{
		logger.debug("retarificarEndosos params: "+params);
		List<Map<String,String>> lista=endososDAO.retarificarEndosos(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("retarificarEndosos lista size: "+lista.size());
		return lista;
	}
	
	@Deprecated
	@Override
	public Map<String, String> confirmarEndosoB(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager confirmarEndosoB params: "+params);
		Map<String,String> mapa=endososDAO.confirmarEndosoB(params);
		logger.debug("EndososManager confirmarEndosoB response map: "+mapa);
        return mapa;
	}
	
	@Override
	public Map<String, String> guardarEndosoDomicilio(Map<String, Object> params) throws Exception
	{
		logger.debug("EndososManager guardarEndosoDomicilio params: "+params);
		Map<String,String> mapa=endososDAO.guardarEndosoNombres(params);
		logger.debug("EndososManager guardarEndosoDomicilio response map: "+mapa);
        return mapa;
	}
	
	@Override
	public List<Map<String, String>> obtieneCoberturasDisponibles(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager obtieneCoberturasDisponibles params: "+params);
		List<Map<String,String>> lista=endososDAO.obtieneCoberturasDisponibles(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtieneCoberturasDisponibles lista size: "+lista.size());
		return lista;
	}
	
	@Deprecated
	@Override
	public Map<String, String> iniciaEndoso(Map<String, Object> params) throws Exception
	{
		logger.debug("EndososManager iniciaEndoso params: "+params);
		Map<String,String> mapa=endososDAO.iniciaEndoso(params);
		logger.debug("EndososManager iniciaEndoso response map: "+mapa);
        return mapa;
	}
	
	@Override
	public List<Map<String, String>> obtenerAtributosCoberturas(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager obtenerAtributosCoberturas params: "+params);
		List<Map<String,String>> lista=endososDAO.obtenerAtributosCoberturas(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerAtributosCoberturas lista size: "+lista.size());
		return lista;
	}
	
	//PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_END
	@Override
	@Deprecated
	public Map<String,Object> sigsvalipolEnd(
			String cdusuari
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			) throws Exception
	{
		logger.debug("EndososManager sigsvalipolEnd");
		Map<String,Object> mapa=endososDAO.sigsvalipolEnd(
				cdusuari
				,cdelemen
				,cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsituac
				,nmsuplem
				,cdtipsup
				);
		logger.debug("EndososManager sigsvalipolEnd response map: "+mapa);
        return mapa;
	}
	
	@Override
	public Map<String, String> guardarEndosoClausulas(Map<String, Object> params) throws Exception
	{
		logger.debug("EndososManager guardarEndosoClausulas params: "+params);
		Map<String,String> mapa=endososDAO.guardarEndosoClausulas(params);
		logger.debug("EndososManager guardarEndosoClausulas response map: "+mapa);
        return mapa;
	}
	
	//PKG_ENDOSOS.P_CALC_VALOR_ENDOSO
	@Override
	public Map<String,String> calcularValorEndoso(Map<String,Object>params) throws Exception
	{
		logger.debug("EndososManager calcularValorEndoso params: "+params);
		Map<String,String> mapa=endososDAO.calcularValorEndoso(params);
		logger.debug("EndososManager calcularValorEndoso response map: "+mapa);
        return mapa;
	}
	
	@Override
	public Map<String,String> calcularValorEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,Date   feinival
			,String cdtipsup) throws Exception
	{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsituac_i" , nmsituac);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_feinival_i" , feinival);
		params.put("pv_cdtipsup_i" , cdtipsup);
        return this.calcularValorEndoso(params);
	}
	
	//PKG_ENDOSOS.P_ENDOSO_INICIA
	@Override
	@Deprecated
	public Map<String,String> iniciarEndoso(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager iniciarEndoso params: "+params);
		Map<String,String> mapa=endososDAO.iniciarEndoso(params);
		logger.debug("EndososManager iniciarEndoso response map: "+mapa);
        return mapa;
	}
	
	/**
	 * PKG_ENDOSOS.P_ENDOSO_INICIA
	 */
	@Override
	@Deprecated
	public Map<String,String> iniciarEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String fecha
			,String cdelemento
			,String cdusuari
			,String proceso
			,String cdtipsup) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_fecha_i"    , fecha);
		params.put("pv_cdelemen_i" , cdelemento);
		params.put("pv_cdusuari_i" , cdusuari);
		params.put("pv_proceso_i"  , proceso);
		params.put("pv_cdtipsup_i" , cdtipsup);
        return this.iniciarEndoso(params);
	}
	
	@Override
	public void movimientoTworksupEnd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nmsituac
			,String accion) throws Exception
	{
		logger.debug("EndososManager insertarTworksupEnd params {},{},{},{},{},{},{},{}"
				,cdunieco,cdramo,estado,nmpoliza,cdtipsup,nmsuplem,nmsituac,accion);
		endososDAO.movimientoTworksupEnd(cdunieco,cdramo,estado,nmpoliza,cdtipsup,nmsuplem,nmsituac,accion);
		logger.debug("EndososManager insertarTworksupEnd end");
	}
	
	//PKG_SATELITES.P_INSERTA_TWORKSUP_SIT_TODAS
	@Override
	public void insertarTworksupSitTodas(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager insertarTworksupSitTodas params: "+params);
		endososDAO.insertarTworksupSitTodas(params);
		logger.debug("EndososManager insertarTworksupSitTodas end");
	}
	
	//PKG_SATELITES.P_OBTIENE_DATOS_MPOLISIT
	@Override
	public Map<String, String> obtieneDatosMpolisit(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager obtieneDatosMpolisit params: "+params);
		Map<String,String> mapa=endososDAO.obtieneDatosMpolisit(params);
		logger.debug("EndososManager obtieneDatosMpolisit response map: "+mapa);
        return mapa;
	}
	
	//PKG_SATELITES.P_OBTIENE_DATOS_MPOLISIT
	@Override
	public Map<String, String> obtieneDatosMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
        return this.obtieneDatosMpolisit(params);
	}
	
	@Override
	public List<Map<String, String>> obtenerNombreEndosos(String cdsisrol, Integer cdramo, String cdtipsit) throws Exception
	{
		logger.debug("EndososManager obtenerNombreEndosos");
		List<Map<String,String>> lista=endososDAO.obtenerNombreEndosos(cdsisrol, cdramo, cdtipsit);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerNombreEndosos lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public String obtieneDescripcionEndoso(String cdtipsup) throws Exception {
		logger.debug("EndososManager obtenerNombreEndoso");
		return endososDAO.obtieneDescripcionEndoso(cdtipsup);
	}
	
	@Override
	public void actualizaNombreCliente(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager actualizaNombreCliente params: "+params);
		endososDAO.actualizaNombreCliente(params);
		logger.debug("EndososManager actualizaNombreCliente end");
	}
	
	@Override
	public void actualizaRfcCliente(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager actualizaRfcCliente params: "+params);
		endososDAO.actualizaRfcCliente(params);
		logger.debug("EndososManager actualizaRfcCliente end");
	}

	@Override
	public void actualizarFenacimi(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager actualizarFenacimi params: "+params);
		endososDAO.actualizarFenacimi(params);
		logger.debug("EndososManager actualizarFenacimi end");
	}
	
	@Override
	public void actualizarSexo(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager actualizarSexo params: "+params);
		endososDAO.actualizarSexo(params);
		logger.debug("EndososManager actualizarSexo end");
	}
	
	@Override
	public List<Map<String, String>> obtenerCdpersonMpoliper(Map<String, String> params) throws Exception
	{
		logger.debug("EndososManager obtenerCdpersonMpoliper params: "+params);
		List<Map<String,String>> lista=endososDAO.obtenerCdpersonMpoliper(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerCdpersonMpoliper lista size: "+lista.size());
		return lista;
	}
	
	@Override
	public String obtenerNtramiteEmision(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		logger.debug("EndososManager obtenerNtramiteEmision params: "+params);
		List<Map<String,String>> lista=endososDAO.obtenerNtramiteEmision(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>(0);
		logger.debug("EndososManager obtenerNtramiteEmision lista: "+lista);
		String ntramite=lista.size()>0?lista.get(0).get("NTRAMITE"):"";
		logger.debug("EndososManager obtenerNtramiteEmision ntramite: "+ntramite);
		return ntramite;
	}
	
	
	@Override
	public RespuestaVO validaEndosoAnterior(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsup) {
		
		RespuestaVO resp = new RespuestaVO();
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("pv_cdunieco_i", cdunieco);
			params.put("pv_cdramo_i"  , cdramo);
			params.put("pv_estado_i"  , estado);
			params.put("pv_nmpoliza_i", nmpoliza);
			params.put("pv_cdtipsup_i", cdtipsup);
			logger.debug(new StringBuilder("EndososManager validaEndosoAnterior params: ").append(params).toString());
			endososDAO.validaEndosoAnterior(params);
			resp.setSuccess(true);
		} catch(Exception ex) {
			logger.error(new StringBuilder().append("Error tratando de acceder a pantalla de endoso: ").append(cdtipsup).toString(), ex);
			//resp.setSuccess(false); //No es necesario asignarle valor, un atributo boolean de una clase por default es false
			resp.setMensaje(ex.getMessage());
		}
		return resp;
	}
	
	
	//PKG_ENDOSOS.P_INS_NEW_DEDUCIBLE_TVALOSIT
	@Override
	public void actualizaDeducibleValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String deducible) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"  , cdunieco);
		params.put("pv_cdramo_i"    , cdramo);
		params.put("pv_estado_i"    , estado);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsuplem_i"  , nmsuplem);
		params.put("pv_deducible_i" , deducible);
		logger.debug("EndososManager actualizaDeducibleValosit params: "+params);
		endososDAO.actualizaDeducibleValosit(params);
		logger.debug("EndososManager actualizaDeducibleValosit end");
	}
	
	@Override
	public void actualizaVigenciaPoliza(String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String feefecto
			,String feproren) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"  , cdunieco);
		params.put("pv_cdramo_i"    , cdramo);
		params.put("pv_estado_i"    , estado);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsuplem_i"  , nmsuplem);
		params.put("pv_feefecto_i"  , feefecto);
		params.put("pv_feproren_i"  , feproren);
		logger.debug("EndososManager actualizaDeducibleValosit params: "+params);
		endososDAO.actualizaVigenciaPoliza(params);
		logger.debug("EndososManager actualizaDeducibleValosit end");
	}
	
	//PKG_ENDOSOS.P_INS_NEW_DEDUCIBLE_TVALOSIT
	@Override
	public void actualizaCopagoValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String deducible) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"  , cdunieco);
		params.put("pv_cdramo_i"    , cdramo);
		params.put("pv_estado_i"    , estado);
		params.put("pv_nmpoliza_i"  , nmpoliza);
		params.put("pv_nmsuplem_i"  , nmsuplem);
		params.put("pv_deducible_i" , deducible);
		logger.debug("EndososManager actualizaCopagoValosit params: "+params);
		endososDAO.actualizaCopagoValosit(params);
		logger.debug("EndososManager actualizaCopagoValosit end");
	}
	
	//P_CLONAR_POLIZA_REEXPED
	@Override
	public Map<String,String> pClonarPolizaReexped(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String fecha
			,String cdplan
			,String cdusuario
			,String newcdunieco) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_feinival_i", fecha);
		params.put("pv_cdplan_i"  , cdplan);
		params.put("pv_cduser_i"  , cdusuario);
		params.put("pv_cdunieco_new_i", newcdunieco);
		logger.debug("EndososManager pClonarPolizaReexped params: "+params);
		Map<String,String> mapa=endososDAO.pClonarPolizaReexped(params);
		logger.debug("EndososManager pClonarPolizaReexped response map: "+mapa);
        return mapa;
	}

	@Override
	public boolean clonaGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cduniecoNueva
			,String nmpolizaNueva
			,List<Map<String,String>> grupos) throws Exception
	{
		boolean exito = true;
		for(Map<String,String> grupo: grupos){
			Map<String,String>params=new HashMap<String,String>();
			params.put("pv_cdunieco_i", cdunieco);
			params.put("pv_cdramo_i"  , cdramo);
			params.put("pv_estado_i"  , estado);
			params.put("pv_nmpoliza_i", nmpoliza);
			params.put("pv_cdunieco_new_i", cduniecoNueva);
			params.put("pv_nmpolnew_i", nmpolizaNueva);
			params.put("pv_cdgrupo_i"   , grupo.get("letra"));
			
			logger.debug("EndososManager clonar grupo params: "+params);
			exito = endososDAO.clonaGrupoReexp(params);
			if(!exito){
				logger.error("Error al avtualizar los valores de los grupos para endoso de Reexpedicion en poliza Colectiva");
				break;
			}
		}
		
		return exito;
	}
	//P_CLONAR_POLIZA_REEXPED SIN PLAN EOT 04052016 CLONACION COTIZACIONES SALUD
		@Override
	public Map<String,String> pClonarCotizacionTotal(
				String cdunieco
				,String cdramo
				,String estado
				,String nmpoliza
				,String fecha
				,String cdusuario
				,String newcdunieco
				,String tipoClonacion) throws Exception
		{
			Map<String,String>params = new HashMap<String,String>();
			params.put("pv_cdunieco_i", cdunieco);
			params.put("pv_cdramo_i"  , cdramo);
			params.put("pv_estado_i"  , estado);
			params.put("pv_nmpoliza_i", nmpoliza);
			params.put("pv_feinival_i", fecha);
			params.put("pv_cduser_i"  , cdusuario);
			params.put("pv_cdunieco_new_i", newcdunieco);
			params.put("pv_tipo_clonacion_i", tipoClonacion);
			logger.debug("EndososManager pClonarPolizaReexped params: "+params);
			Map<String,String> mapa = endososDAO.pClonarCotizacionTotal(params);
			logger.debug("EndososManager pClonarPolizaReexped response map: "+mapa);
	        return mapa;
		}
	
	@Override
	public boolean actualizaGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,List<Map<String,String>> grupos) throws Exception
	{
		boolean exito = true;
		for(Map<String,String> grupo: grupos){
			Map<String,String>params=new HashMap<String,String>();
			params.put("pv_cdunieco_i", cdunieco);
			params.put("pv_cdramo_i"  , cdramo);
			params.put("pv_estado_i"  , estado);
			params.put("pv_nmpoliza_i", nmpoliza);
			params.put("pv_cdgrupo_i"   , grupo.get("letra"));
			params.put("pv_cdplan_i"  , grupo.get("cdplan"));
			
			StringBuilder valoresGrupo = new StringBuilder(); 
			
			for (Map.Entry<String, String> entry : grupo.entrySet()) {
				if(entry.getKey().contains("otvalor")){
					String otvalor = new String(entry.getKey());
					otvalor =  otvalor.replace("pv_otvalor", "otvalor");
					valoresGrupo.append(otvalor).append("@").append(entry.getValue()).append("|");
				}
			}
			params.put("pv_cdcadena_i", valoresGrupo.toString());
			
			logger.debug("EndososManager actualizar grupo params: "+params);
			exito = endososDAO.actualizaGrupoReexp(params);
			if(!exito){
				logger.error("Error al avtualizar los valores de los grupos para endoso de Reexpedicion en poliza Colectiva");
				break;
			}
		}
		
		return exito;
	}

	@Override
	public boolean actualizaTodosGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception
	{
		boolean exito = true;
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
			
		StringBuilder valoresGrupo = new StringBuilder();			
		params.put("pv_cdcadena_i", valoresGrupo.toString());
			
		logger.debug("EndososManager actualizar grupo params: "+params);
		exito = endososDAO.actualizaGrupoReexp(params);
		
		return exito;
	}
	
	@Override
	public boolean valoresDefectoGruposReexp(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup
			,List<Map<String,String>> grupos) throws Exception
	{
		boolean exito = true;
		for(Map<String,String> grupo: grupos){
			Map<String,String>params=new HashMap<String,String>();
			params.put("pv_cdunieco_i", cdunieco);
			params.put("pv_cdramo_i"  , cdramo);
			params.put("pv_estado_i"  , estado);
			params.put("pv_nmpoliza_i", nmpoliza);
			params.put("pv_nmsuplem_i", nmsuplem);
			params.put("pv_cdgrupo_i" , grupo.get("letra"));
			params.put("pv_cdtipsup_i", cdtipsup);
			
			logger.debug("EndososManager valores defecto grupo params: "+params);
			exito = endososDAO.valoresDefectoGrupoReexp(params);
			if(!exito){
				logger.error("Error en ejecutar valores por defecto de los grupos para endoso de Reexpedicion en poliza Colectiva");
				break;
			}
		}
		
		return exito;
	}
	
	public boolean valoresDefectoGruposCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup) throws Exception
	{
		boolean exito = true;
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_cdtipsup_i", cdtipsup);			
		logger.debug("EndososManager valores defecto grupo params: "+params);
		exito = endososDAO.valoresDefectoGruposReexp(params);		
		return exito;
	}
	
	@Deprecated
	@Override
	public List<Map<String, String>> obtenerValositUltimaImagen(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception {
		List<Map<String,String>> lista=endososDAO.obtenerValositUltimaImagen(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
		if (lista == null) {
		    lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("obtenerValositUltimaImagen lista {}: ", Utils.log(lista));
		return lista;
	}
	
	//PKG_ENDOSOS.P_INS_NEW_EXTRAPRIMA_TVALOSIT
	@Override
	public void actualizaExtraprimaValosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String extraprima) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"   , cdunieco);
		params.put("pv_cdramo_i"     , cdramo);
		params.put("pv_estado_i"     , estado);
		params.put("pv_nmpoliza_i"   , nmpoliza);
		params.put("pv_nmsituac_i"   , nmsituac);
		params.put("pv_nmsuplem_i"   , nmsuplem);
		params.put("pv_extraprima_i" , extraprima);
		logger.debug("EndososManager actualizaExtraprimaValosit params: "+params);
		endososDAO.actualizaExtraprimaValosit(params);
		logger.debug("EndososManager actualizaExtraprimaValosit end");
	}
	
	@Override
	public void insertarPolizaCdperpag(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdperpag) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i",cdunieco);
		params.put("pv_cdramo_i",cdramo);
		params.put("pv_estado_i",estado);
		params.put("pv_nmpoliza_i",nmpoliza);
		params.put("pv_nmsuplem_i",nmsuplem);
		params.put("pv_cdperpag_i",cdperpag);
		logger.debug("EndososManager insertaPolizaCdperpag params: "+params);
		endososDAO.insertarPolizaCdperpag(params);
		logger.debug("EndososManager insertaPolizaCdperpag end");
	}
	
	/**
	 * PKG_ENDOSOS.P_GET_FEINIVAL_END_FP
	 */
	@Override
	public Date obtenerFechaEndosoFormaPago(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		logger.debug("EndososManager obtenerFechaEndosoFormaPago params: "+params);
		Date fecha=endososDAO.obtenerFechaEndosoFormaPago(params);
		logger.debug("EndososManager obtenerFechaEndosoFormaPago fecha: "+fecha);
		return fecha;
	}
	
	/**
	 * P_CALC_RECIBOS_SUB_ENDOSO_FP
	 */
	@Override
	public void calcularRecibosEndosoFormaPago(String cdunieco,String cdramo,
			String estado,String nmpoliza,String nmsuplem) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco" , cdunieco);
		params.put("pv_cdramo"   , cdramo);
		params.put("pv_estado"   , estado);
		params.put("pv_nmpoliza" , nmpoliza);
		params.put("pv_nmsuplem" , nmsuplem);
		logger.debug("EndososManager calcularRecibosEndosoFormaPago params: "+params);
		endososDAO.calcularRecibosEndosoFormaPago(params);
		logger.debug("EndososManager calcularRecibosEndosoFormaPago fin");
	}

	
	@Override
	public void cancelaRecibosCambioCliente(String cdunieco,String cdramo,
			String estado,String nmpoliza,String nmsuplem) throws Exception
			{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsuplem_i" , nmsuplem);
		logger.debug("EndososManager cancelaRecibosCambioCliente params: "+params);
		endososDAO.cancelaRecibosCambioCliente(params);
		logger.debug("EndososManager cancelaRecibosCambioCliente fin");
			}
	
	/**
	 * P_CALCULA_COMISION_BASE
	 */
	@Override
	public void calcularComisionBase(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsuplem_i" , nmsuplem);
		logger.debug("EndososManager calcularComisionBase params: "+params);
		endososDAO.calcularComisionBase(params);
		logger.debug("EndososManager calcularComisionBase fin");
	}
	
	/**
	 * PKG_CONSULTA.P_GET_AGENTE_POLIZA
	 * @return a.cdunieco,
			a.cdramo,
			a.estado,
			a.nmpoliza,
			a.cdagente,
			a.nmsuplem,
			a.status,
			a.cdtipoag,
			porredau,
			a.porparti,
			nombre,
			cdsucurs,
			nmcuadro
	 */
	@Override
	public List<Map<String,String>> obtenerAgentesEndosoAgente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("PV_CDUNIECO_I" , cdunieco);
		params.put("PV_CDRAMO_I"   , cdramo);
		params.put("PV_ESTADO_I"   , estado);
		params.put("PV_NMPOLIZA_I" , nmpoliza);
		params.put("PV_NMSUPLEM_I" , nmsuplem);
		logger.debug("EndososManager obtenerAgentesEndosoAgente params: "+params);
		List<Map<String,String>>lista=endososDAO.obtenerAgentesEndosoAgente(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>();
		logger.debug("EndososManager obtenerAgentesEndosoAgente lista size: "+lista.size());
		return lista;
	}

	@Override
	public List<Map<String,String>> obtenerAseguradosPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("PV_CDUNIECO_I" , cdunieco);
		params.put("PV_CDRAMO_I"   , cdramo);
		params.put("PV_ESTADO_I"   , estado);
		params.put("PV_NMPOLIZA_I" , nmpoliza);
		params.put("PV_NMSUPLEM_I" , nmsuplem);
		List<Map<String,String>>lista=endososDAO.obtenerAseguradosPoliza(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>();
		
		logger.debug("Resultado de carga asegurados : "+lista);
		
		return lista;
	}
	
	/**
	 * PKG_SATELITES.P_MOV_MPOLIAGE
	 */
	@Override
	public void pMovMpoliage(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdagente
			,String nmsuplem
			,String status
			,String cdtipoag
			,String porredau
			,String nmcuadro
			,String cdsucurs
			,String accion
			,String ntramite
			,String porparti
			) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdagente_i" , cdagente);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_status_i"   , status);
		params.put("pv_cdtipoag_i" , cdtipoag);
		params.put("pv_porredau_i" , porredau);
		params.put("pv_nmcuadro_i" , nmcuadro);
		params.put("pv_cdsucurs_i" , cdsucurs);
		params.put("pv_accion_i"   , accion);
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_porparti_i" , porparti);
		logger.debug("EndososManager pMovMpoliage params: "+params);
		endososDAO.pMovMpoliage(params);
		logger.debug("EndososManager pMovMpoliage fin");
	}
	
	/**
	 * PKG_SATELITES.P_GET_NMSUPLEM_EMISION
	 */
	@Override
	public String pGetSuplemEmision(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		String nmsuplem = "";
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		logger.debug("EndososManager pGetSuplemEmision params: "+params);
		nmsuplem = endososDAO.pGetSuplemEmision(params);
		logger.debug("EndososManager pGetSuplemEmision nmsuplem: "+nmsuplem);
		return nmsuplem;
	}
	
	@Override
	public String obtieneFechaInicioVigenciaPoliza
	(
		String cdunieco,
		String cdramo,
		String estado,
		String nmpoliza
		) throws Exception
	{
		return endososDAO.obtieneFechaInicioVigenciaPoliza(cdunieco,cdramo,estado,nmpoliza);
	}
	
	@Override
	public boolean validaEndosoSimple
	(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza
			) throws Exception
	{
		return endososDAO.validaEndosoSimple(cdunieco,cdramo,estado,nmpoliza);
	}
	
	@Override
	public void validaNuevaCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdgarant
			) throws Exception
	{
		endososDAO.validaNuevaCobertura(cdunieco,cdramo,estado,nmpoliza,nmsituac,cdgarant);
	}
	
	@Override
	public void validaDuplicidadParentesco(String cdunieco, String cdramo, String estado, 
			String nmpoliza, String nmsuplem) throws Exception {
		
		endososDAO.validaDuplicidadParentesco(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
	}
	
	@Override
	public void sacaEndoso(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nsuplogi, String nmsuplem) throws Exception {
		String paso = "Revirtiendo endoso";
		try {
		    endososDAO.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem);
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
	}
	
	@Override
	public void calcularRecibosCambioAgente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception
	{
		logger.info(""
				+ "\n#########################################"
				+ "\n###### calcularRecibosCambioAgente ######"
				);
		endososDAO.calcularRecibosCambioAgente(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
		logger.info(""
				+ "\n###### calcularRecibosCambioAgente ######"
				+ "\n#########################################"
				);
	}

	@Override
	public void calcularRecibosCambioContratante(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem) throws Exception
			{
		logger.info(""
				+ "\n#########################################"
				+ "\n###### calcularRecibosCambioContratante ######"
				);
		endososDAO.calcularRecibosCambioContratante(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
		logger.info(""
				+ "\n###### calcularRecibosCambioAgente ######"
				+ "\n#########################################"
				);
			}
	
	@Override
	public List<Map<String,String>> habilitaRecibosSubsecuentes(
			Date fechaDeInicio
			,Date fechaDeFin
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza) throws Exception
	{
		return endososDAO.habilitaRecibosSubsecuentes(fechaDeInicio,fechaDeFin,cdunieco,cdramo,estado,nmpoliza);
	}
	
	@Override
	public void validaEstadoCodigoPostal(Map<String,String>params) throws Exception{
		endososDAO.validaEstadoCodigoPostal(params);
	}
	
	@Override
	public void actualizaTvalositCoberturasAdicionales(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsup) throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ actualizaTvalositCoberturasAdicionales @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.append("\n@@@@@@ cdtipsup=").append(cdtipsup)
				.toString()
				);
		
		endososDAO.actualizaTvalositCoberturasAdicionales(cdunieco,cdramo,estado,nmpoliza,nmsuplem,cdtipsup);
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ actualizaTvalositCoberturasAdicionales @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
	}
	
	@Override
	public ManagerRespuestaImapSmapVO obtenerComponentesSituacionCobertura(String cdramo,String cdtipsit,String cdtipsup,String cdgarant)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ obtenerComponenteSituacionCobertura @@@@@@")
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ cdtipsup=").append(cdtipsup)
				.append("\n@@@@@@ cdgarant=").append(cdgarant)
				.toString()
				);
		
		ManagerRespuestaImapSmapVO resp = new ManagerRespuestaImapSmapVO(true);
		resp.setSmap(new HashMap<String,String>());
		resp.setImap(new HashMap<String,Item>());
		
		List<ComponenteVO> comp = null;
		
		//obtener componente
		try
		{
			comp = endososDAO.obtenerComponentesSituacionCobertura(cdramo,cdtipsit,cdtipsup,cdgarant);
		}
		catch(ApplicationException ax)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
			resp.setRespuestaOculta(ax.getMessage());
			logger.error(resp.getRespuesta(),ax);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener componente #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		//transformarlo a item
		if(resp.isExito())
		{
			try
			{
				if(comp==null||comp.size()==0)
				{
					resp.getSmap().put("CONITEM" , "false");
				}
				else
				{
					resp.getSmap().put("CONITEM" , "true");
					
					GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
					gc.setCdramo(cdramo);
					gc.setCdtipsit(cdtipsit);
					
					gc.generaComponentes(comp, true, false, true, false, false, false);
					
					resp.getImap().put("item",gc.getItems());
				}
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al construir componente #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ obtenerComponenteSituacionCobertura @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public void actualizaTvalositSituacionCobertura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdatribu
			,String otvalor)
	{
		try
		{
			endososDAO.actualizaTvalositSitaucionCobertura(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,cdatribu,otvalor);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			logger.error(new StringBuilder("Error al actualizar tvalosit situacion cobertura #").append(timestamp).toString(),ex);
		}
	}
	
	@Override
	public ManagerRespuestaImapSmapVO endosoAtributosSituacionGeneral(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String cdusuari
			,String cdtipsup)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ endosoAtributosSituacionGeneral @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ cdusuari=").append(cdusuari)
				.append("\n@@@@@@ cdtipsup=").append(cdtipsup)
				.toString()
				);
		
		ManagerRespuestaImapSmapVO resp = new ManagerRespuestaImapSmapVO(true);
		resp.setImap(new HashMap<String,Item>());
		
		//validar endoso anterior
		try
		{
			endososDAO.validaEndosoAnterior(cdunieco,cdramo,estado,nmpoliza,cdtipsup);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder(ex.getMessage()).append(" #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		List<ComponenteVO>tatrisit=null;
		
		//obtener tatrisit
		if(resp.isExito())
		{
			try
			{
				tatrisit = cotizacionDAO.cargarTatrisit(cdtipsit, cdusuari);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener atributos #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//obtener relacion atributos endoso
		if(resp.isExito())
		{
			try
			{
				resp.setSmap(endososDAO.obtenerParametrosEndoso(
						ParametroEndoso.RELACION_ENDOSO_ATRIBUTO_SITUACION
						,cdramo
						,cdtipsit
						,cdtipsup
						,null));
			}
			catch(ApplicationException ax)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
				resp.setRespuestaOculta(ax.getMessage());
				logger.error(resp.getRespuesta(),ax);
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener relacion endoso - atributos #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		GeneradorCampos gc=null;
		
		//crear componentes de tatrisit
		if(resp.isExito())
		{
			try
			{
				List<ComponenteVO>tatrisitAux = new ArrayList<ComponenteVO>();
				for(ComponenteVO tatri:tatrisit)
				{
					if(resp.getSmap().containsValue(tatri.getNameCdatribu()))
					{
						tatrisitAux.add(tatri);
					}
				}
				tatrisit=tatrisitAux;
				logger.debug(new StringBuilder("Atributos para el endoso=").append(tatrisit).toString());
				
				gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.setCdramo(cdramo);
				gc.setCdtipsit(cdtipsit);
				
				gc.generaComponentes(tatrisit, true, false, true, false, false, false);
				resp.getImap().put("nuevoItems",gc.getItems());

				for(ComponenteVO atributo:tatrisit)
				{
					atributo.setSoloLectura(true);
				}
				gc.generaComponentes(tatrisit, true, true, true, false, false, false);
				resp.getImap().put("actualItems"  , gc.getItems());
				resp.getImap().put("actualFields" , gc.getFields());
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al construir componentes #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		//componentes ajenos a tatrisit
		if(resp.isExito())
		{
			try
			{
				List<ComponenteVO>componentesAux = pantallasDAO.obtenerComponentes(
						null,null,null
						,null,null,null
						,"ENDOSO_ATRI_GRAL","PANEL_LECTURA",null);
				
				gc.generaComponentes(componentesAux, true, false, true, false, false, false);
				resp.getImap().put("lecturaItems",gc.getItems());
				
				componentesAux = pantallasDAO.obtenerComponentes(
						null,null,null
						,null,null,null
						,"ENDOSO_ATRI_GRAL","ITEMS_ENDOSO",null);
				
				gc.generaComponentes(componentesAux, true, false, true, false, false, false);
				resp.getImap().put("endosoItems",gc.getItems());
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al obtener componentes generales #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ endosoAtributosSituacionGeneral @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	@Override
	public ManagerRespuestaSmapVO cargarTvalositTitular(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarTvalositTitular @@@@@@")
				.append("\n@@@@@@ cdunieco=").append(cdunieco)
				.append("\n@@@@@@ cdramo=")  .append(cdramo)
				.append("\n@@@@@@ estado=")  .append(estado)
				.append("\n@@@@@@ nmpoliza=").append(nmpoliza)
				.append("\n@@@@@@ nmsuplem=").append(nmsuplem)
				.toString()
				);
		
		ManagerRespuestaSmapVO resp=new ManagerRespuestaSmapVO(true);
		
		try
		{
			List<Map<String,String>>tvalosits = this.obtenerValositUltimaImagen(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
			for(Map<String,String>tvalosit:tvalosits)
			{
				if(tvalosit.get("NMSITUAC").equals("1"))
				{
					resp.setSmap(tvalosit);
				}
			}
			if(resp.getSmap()==null)
			{
				throw new ApplicationException("No hay valores para el titular");
			}
			Map<String,String>valores=new HashMap<String,String>();
			for(Entry<String,String>en:resp.getSmap().entrySet())
			{
				String key = en.getKey();
				if(StringUtils.isNotBlank(key)
						&&key.length()>"OTVALOR".length()
						&&key.substring(0, "OTVALOR".length()).equals("OTVALOR")
						)
				{
					valores.put(
							new StringBuilder(
									"parametros.pv_otvalor")
							.append(key.substring("OTVALOR".length()))
							.toString()
							,en.getValue()
							);
				}
			}
			resp.getSmap().putAll(valores);
		}
		catch(ApplicationException ax)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
			resp.setRespuestaOculta(ax.getMessage());
			logger.error(resp.getRespuesta(),ax);
		}
		catch(Exception ex)
		{
			long timestamp = System.currentTimeMillis();
			resp.setExito(false);
			resp.setRespuesta(new StringBuilder("Error al obtener valores de atributos de situacion #").append(timestamp).toString());
			resp.setRespuestaOculta(ex.getMessage());
			logger.error(resp.getRespuesta(),ex);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ cargarTvalositTitular @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return resp;
	}
	
	
	@Override
	public ManagerRespuestaVoidVO guardarEndosoAtributosSituacionGeneral(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdtipsup
			,String ntramite
			,String feefecto
			,Map<String,String>tvalosit
			,UserVO usuario
			,String rutaDocsPoliza
			,String rutaServReports
			,String passServReports
			,FlujoVO flujo
			)throws Exception
	{
		String cdelemen = usuario.getEmpresa().getElementoId();
		String cdusuari = usuario.getUser();
		String cdsisrol = usuario.getRolActivo().getClave();
		String nmsolici = null;
		
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarEndosoAtributosSituacionGeneral @@@@@@"
				,"\n@@@@@@ cdunieco        = " , cdunieco
				,"\n@@@@@@ cdramo          = " , cdramo
				,"\n@@@@@@ cdtipsit        = " , cdtipsit
				,"\n@@@@@@ estado          = " , estado
				,"\n@@@@@@ nmpoliza        = " , nmpoliza
				,"\n@@@@@@ nmsuplem        = " , nmsuplem
				,"\n@@@@@@ cdtipsup        = " , cdtipsup
				,"\n@@@@@@ feefecto        = " , feefecto
				,"\n@@@@@@ tvalosit        = " , tvalosit
				,"\n@@@@@@ cdelemen        = " , cdelemen
				,"\n@@@@@@ cdusuari        = " , cdusuari
				,"\n@@@@@@ rutaDocsPoliza  = " , rutaDocsPoliza
				,"\n@@@@@@ rutaServReports = " , rutaDocsPoliza
				,"\n@@@@@@ passServReports = " , rutaDocsPoliza
				,"\n@@@@@@ flujo           = " , flujo
				));
		
		ManagerRespuestaVoidVO resp = new ManagerRespuestaVoidVO(true);
		
		Date fechaEfecto = null;
		
		TipoEndoso enumTipoEndosoElegido = null;
		
		String paso = null;
		
		try
		{
			paso = "Procesando datos";
			logger.debug(paso);
			
			fechaEfecto = Utils.parse(feefecto);
			
			//Creamos un enum en base al tipo de endoso elegido:
			for (TipoEndoso te : TipoEndoso.values()) {
				if( cdtipsup.equals(te.getCdTipSup().toString()) ) {
					enumTipoEndosoElegido = te;
					break;
				}
			}
			
			paso = "Iniciando endoso";
			logger.debug(paso);
			
			Map<String,String>iniciarEndosoResp=endososDAO.iniciarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,fechaEfecto
					,cdelemen
					,cdusuari
					,"END"
					,cdtipsup);
			
			String nmsuplemEndoso = iniciarEndosoResp.get("pv_nmsuplem_o");
			String nsuplogi       = iniciarEndosoResp.get("pv_nsuplogi_o");
			
			paso = "Guardando valores de atributos";
			logger.debug(paso);
			
			Map<String,String>tvalositNuevo=new HashMap<String,String>();
			for(Entry<String,String>en:tvalosit.entrySet())
			{
				String key=en.getKey();
				if(StringUtils.isNotBlank(key)
						&&key.length()>"parametros.pv_".length()
						&&key.substring(0,"parametros.pv_".length()).equals("parametros.pv_")
						)
				{
					tvalositNuevo.put(key.substring("parametros.pv_".length()),en.getValue());
				}
			}
			endososDAO.guardarAtributosSituacionGeneral(cdunieco,cdramo,estado,nmpoliza,nmsuplemEndoso,tvalositNuevo);
			
			/*SE COMENTA LA CONFIRMACION
			Boolean fechaValida = null;
			
			//validar fecha
			try
			{
				long diferenciaFechaActualVSEndoso = new Date().getTime() - fechaEfecto.getTime();
				diferenciaFechaActualVSEndoso = Math.abs(diferenciaFechaActualVSEndoso);
				                          //d   h   m   s   ms
				long maximoDiasPermitidos = 30l*24l*60l*60l*1000l;
				
				fechaValida = diferenciaFechaActualVSEndoso <= maximoDiasPermitidos;
			}
			catch(Exception ex)
			{
				long timestamp = System.currentTimeMillis();
				resp.setExito(false);
				resp.setRespuesta(new StringBuilder("Error al validar fecha de endoso #").append(timestamp).toString());
				resp.setRespuestaOculta(ex.getMessage());
				logger.error(resp.getRespuesta(),ex);
			}
			
			String ntramiteEmision = null;
			
			//obtener tramite de emision
			if(resp.isExito())
			{
				try
				{
					ntramiteEmision = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
				}
				catch(ApplicationException ax)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder(ax.getMessage()).append(" #").append(timestamp).toString());
					resp.setRespuestaOculta(ax.getMessage());
					logger.error(resp.getRespuesta(),ax);
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al obtener tramite de emision #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
			}
			
			String dssuplem = null;
			
			//obtener nombre endoso
			if(resp.isExito())
			{
				try
				{
					dssuplem = "";
					List<Map<String,String>>endosos = endososDAO.obtenerNombreEndosos("", Integer.parseInt(cdramo), cdtipsit);
					for(Map<String,String>endoso:endosos)
					{
						if(endoso.get("CDTIPSUP").equalsIgnoreCase(cdtipsup))
						{
							dssuplem=endoso.get("DSTIPSUP");
						}
					}
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al obtener tramite de emision #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
			}
			
			String ntramiteEndoso = null;
			
			//tramite mesa de control
			if(resp.isExito())
			{
				try
				{
					String statusTramiteEndoso = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
					if(!fechaValida)
					{
						statusTramiteEndoso = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
					}
					
					Map<String,String>valores=new HashMap<String,String>();
					valores.put("otvalor01" , ntramiteEmision);
					valores.put("otvalor02" , cdtipsup);
					valores.put("otvalor03" , dssuplem);
					valores.put("otvalor04" , nsuplogi);
					valores.put("otvalor05" , cdusuari);
					
					ntramiteEndoso = mesaControlDAO.movimientoMesaControl(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplemEndoso
							,cdunieco
							,cdunieco
							,TipoTramite.ENDOSO.getCdtiptra()
							,fechaEfecto
							,null
							,null
							,null
							,fechaEfecto
							,statusTramiteEndoso
							,""
							,null
							,cdtipsit
							,cdusuari
							,cdsisrol, null,null,null
							,valores, null, null, null, null, false
							);
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al guardar tramite #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
			}
			*/
			
			// Acciones particulares por Tipo de Endoso:
			paso = "Ejecutando acciones por tipo de endoso";
			logger.debug(paso);
			
			if(enumTipoEndosoElegido != null) {
				switch(enumTipoEndosoElegido) {
				case SUMA_ASEGURADA_INCREMENTO:
				case SUMA_ASEGURADA_DECREMENTO:
					//Si cdramo es gastos medicos mayores y cd tipsit es gastos medicos individual insertamos en mpolicap:
					if(cdramo.equals(Ramo.GASTOS_MEDICOS_MAYORES.getCdramo()) &&
							cdtipsit.equals(TipoSituacion.GASTOS_MEDICOS_INDIVIDUAL.getCdtipsit())) {
						Map<String,String>mapaMpolicap=new LinkedHashMap<String,String>(0);
						mapaMpolicap.put("pv_cdunieco_i", cdunieco);
						mapaMpolicap.put("pv_cdramo_i"  , cdramo);
						mapaMpolicap.put("pv_estado_i"  , estado);
						mapaMpolicap.put("pv_nmpoliza_i", nmpoliza);
						mapaMpolicap.put("pv_nmsuplem_i", nmsuplemEndoso);
						mapaMpolicap.put("pv_ptcapita_i", tvalosit.get("parametros.pv_otvalor06"));
						endososDAO.insertarMpolicap(mapaMpolicap);
					}
					break;
					
				default:
					break;
				}
			}
			
			paso = "Ejecutando situaciones temporales de endoso";
			logger.debug(paso);
			
			//////////////////////////////
			////// inserta tworksup //////
			// Se insertan en tworksup TODAS LAS SITUACIONES:
			Map<String,String> mapaTworksupEnd = new LinkedHashMap<String,String>(0);
			mapaTworksupEnd.put("pv_cdunieco_i", cdunieco);
			mapaTworksupEnd.put("pv_cdramo_i"  , cdramo);
			mapaTworksupEnd.put("pv_estado_i"  , estado);
			mapaTworksupEnd.put("pv_nmpoliza_i", nmpoliza);
			mapaTworksupEnd.put("pv_cdtipsup_i", cdtipsup);
			mapaTworksupEnd.put("pv_nmsuplem_i", nmsuplemEndoso);
			endososDAO.insertarTworksupSitTodas(mapaTworksupEnd);
			////// inserta tworksup //////
			//////////////////////////////
			
			paso = "Ejecutando tarificaci\u00f3n";
			logger.debug(paso);
			
			//////////////////////////
			////// tarificacion //////
			endososDAO.sigsvalipolEnd(
					cdusuari
					,cdelemen
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0"
					,nmsuplemEndoso
					,cdtipsup
					);
			////// tarificacion //////
			//////////////////////////
			
			paso = "Calculando valor de endoso";
			logger.debug(paso);
			
			//////////////////////////
			////// valor endoso //////
			//Calcula el valor del endoso:
			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
			mapaValorEndoso.put("pv_estado_i"   , estado);
			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
			mapaValorEndoso.put("pv_nmsituac_i" , "0");
			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplemEndoso);
			mapaValorEndoso.put("pv_feinival_i" , Utils.parse(feefecto));
			mapaValorEndoso.put("pv_cdtipsup_i" , cdtipsup);
			logger.debug("mapaValorEndoso=" + mapaValorEndoso);
			endososDAO.calcularValorEndoso(mapaValorEndoso);
			////// valor endoso //////
			//////////////////////////
			
			/*SE COMENTA LA CONFIRMACION
			//confirmar endoso
			if(resp.isExito()&&fechaValida)
			{
				try
				{
					endososDAO.confirmarEndosoB(cdunieco,cdramo,estado,nmpoliza,nmsuplemEndoso,nsuplogi,cdtipsup,"");
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al confirmar endoso #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
			}
			*/
			
			RespuestaConfirmarEndoso confirmacion = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemEndoso
					,nsuplogi
					,cdtipsup
					,""//dscoment
					,fechaEfecto
					,cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			boolean fechaValida = confirmacion.isConfirmado();
			
			String ntramiteEmision = confirmacion.getNtramiteEmision();
			
			String ntramiteEndoso = confirmacion.getNtramite();
			
			//reimprimir documentos
			if(fechaValida)
			{
				paso = "Generando documentos";
				logger.debug(paso);
				
				documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplemEndoso
						,DocumentosManager.PROCESO_ENDOSO //proceso
						,ntramiteEmision
						,nmsolici, null
						);
				
				/*
				List<Map<String,String>>listaDocu=null;
				
				try
				{
					listaDocu=endososDAO.reimprimeDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplemEndoso, cdtipsup);
				}
				catch(Exception ex)
				{
					long timestamp = System.currentTimeMillis();
					resp.setExito(false);
					resp.setRespuesta(new StringBuilder("Error al obtener lista de documentos #").append(timestamp).toString());
					resp.setRespuestaOculta(ex.getMessage());
					logger.error(resp.getRespuesta(),ex);
				}
				
				String rutaCarpeta=new StringBuilder(rutaDocsPoliza).append("/").append(ntramiteEmision).toString();
				
				//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
				for(Map<String,String> docu:listaDocu)
				{
					logger.debug("docu iterado: "+docu);
					
					String descripc  = docu.get("descripc");
					String descripl  = docu.get("descripl");
					nmsolici = docu.get("nmsolici");
					StringBuilder sb = new StringBuilder();
					
					sb.append(rutaServReports)
					  .append("?destype=cache")
					  .append("&paramform=no")
					  .append("&ACCESSIBLE=YES")
					  .append("&desformat=PDF")
					  .append("&userid=")  .append(passServReports)
					  .append("&report=")  .append(descripl)
					  .append("&p_unieco=").append(cdunieco)
					  .append("&p_ramo=")  .append(cdramo)
					  .append("&p_estado=").append(estado)
					  .append("&p_poliza=").append(nmpoliza)
					  .append("&p_suplem=").append(nmsuplemEndoso)
					  .append("&desname=") .append(rutaCarpeta).append("/").append(descripc)
					  ;
					
					if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN"))
					{
						// C R E D E N C I A L _ X X X X X X . P D F
						//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
						sb.append("&p_cdperson=").append(descripc.substring(11, descripc.lastIndexOf("_")));
					}
					
					String url = sb.toString();
					
					logger.debug(
							new StringBuilder()
							.append("\n#################################")
							.append("\n###### Se solicita reporte ######")
							.append("\n###### a ").append(url)
							.toString()
							);
					HttpUtil.generaArchivo(url,new StringBuilder(rutaCarpeta).append("/").append(descripc).toString());
					logger.debug(
							new StringBuilder()
							.append("\n###### a ").append(url)
							.append("\n###### reporte solicitado ######")
							.append("\n################################")
							.toString()
							);
				}
				*/
			}
			
			paso = "Ejecutando servicios web";
			logger.debug(paso);
			
			ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza,
					nmsuplemEndoso, null, cdunieco, nmsolici, ntramite, true, cdtipsup, usuario);
			
			if(fechaValida)
			{
				resp.setRespuesta(Utils.join("Se ha guardado el endoso ", nsuplogi));
			}
			else
			{
				resp.setRespuesta(Utils.join(
						"El endoso ", nsuplogi
						," se guard\u00f3 en mesa de control para autorizaci\u00f3n "
						,"con n\u00famero de tr\u00e1mite ", ntramiteEndoso
						));
			}
			if (confirmacion.getRespuestaTurnado() != null) {
			    resp.setRespuesta(Utils.join(resp.getRespuesta(), ". ", confirmacion.getRespuestaTurnado().getMessage()));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ resp = " , resp
				,"\n@@@@@@ guardarEndosoAtributosSituacionGeneral @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO guardarEndosoBeneficiarios(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,List<Map<String,String>>mpoliperMpersona
			,String cdelemen
			,String cdusuari
			,String cdtipsup
			,String ntramiteEmi
			,String cdsisrol
			,UserVO usuarioSesion
			,FlujoVO flujo
			,String feefecto
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarEndosoBeneficiarios @@@@@@"
				,"\n@@@@@@ cdunieco         = " , cdunieco
				,"\n@@@@@@ cdramo           = " , cdramo
				,"\n@@@@@@ estado           = " , estado
				,"\n@@@@@@ nmpoliza         = " , nmpoliza
				,"\n@@@@@@ nmsituac         = " , nmsituac
				,"\n@@@@@@ mpoliperMpersona = " , mpoliperMpersona
				,"\n@@@@@@ cdelemen         = " , cdelemen
				,"\n@@@@@@ cdusuari         = " , cdusuari
				,"\n@@@@@@ cdtipsup         = " , cdtipsup
				,"\n@@@@@@ ntramiteEmi      = " , ntramiteEmi
				,"\n@@@@@@ cdsisrol         = " , cdsisrol
				,"\n@@@@@@ flujo            = " , flujo
				,"\n@@@@@@ feefecto         = " , feefecto
				));

		ManagerRespuestaVoidVO resp=new ManagerRespuestaVoidVO(true);
		
		String paso = null;
		
		try
		{
			String usuarioCaptura =  null;
			
			if(usuarioSesion!=null){
				if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
					usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuarioSesion.getCodigoPersona();
				}
				
			}
			
			//Date fechaEndoso = new Date();
			
			paso = "Iniciando endoso";
			logger.debug(paso);
			
			Map<String,String>iniciarEndosoResp=endososDAO.iniciarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,renderFechas.parse(feefecto)
					,cdelemen
					,cdusuari
					,"END"
					,cdtipsup);
			
			String nmsuplem = iniciarEndosoResp.get("pv_nmsuplem_o");
			String nsuplogi = iniciarEndosoResp.get("pv_nsuplogi_o");
			
			paso = "Iterando registros";
			logger.debug(paso);
			
			for(Map<String,String>rec:mpoliperMpersona)
			{
				String mov    = rec.get("mov");
				int agregar   = 1;
				int eliminar  = 2;
				int operacion = 0;
				if(StringUtils.isNotBlank(mov))
				{
					if(mov.equals("+"))
					{
						operacion=agregar;
					}
					else if(mov.equals("-"))
					{
						operacion=eliminar;
					}
				}
				
				if(operacion==agregar)
				{
					personasDAO.movimientosMpersona(
							rec.get("CDPERSON")
							,rec.get("CDTIPIDE")
							,rec.get("CDIDEPER")
							,rec.get("DSNOMBRE")
							,rec.get("CDTIPPER")
							,rec.get("OTFISJUR")
							,rec.get("OTSEXO")
							,StringUtils.isNotBlank(rec.get("FENACIMI"))?
									Utils.parse(rec.get("FENACIMI"))
									:null
							,rec.get("CDRFC")
							,rec.get("DSEMAIL")
							,rec.get("DSNOMBRE1")
							,rec.get("DSAPELLIDO")
							,rec.get("DSAPELLIDO1")
							,renderFechas.parse(feefecto)//fechaEndoso
							,rec.get("CDNACION")
							,rec.get("CANALING")
							,rec.get("CONDUCTO")
							,rec.get("PTCUMUPR")
							,rec.get("RESIDENCIA")
							,rec.get("NONGRATA")
							,rec.get("CDIDEEXT")
							,rec.get("CDESTCIV")
							,rec.get("CDSUCEMI")
							,usuarioCaptura
							,Constantes.INSERT_MODE);
					
					endososDAO.movimientoMpoliperBeneficiario(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,"3"
							,rec.get("CDPERSON")
							,nmsuplem
							,"V"
							,rec.get("NMORDDOM")
							,rec.get("SWRECLAM")
							,"N" //swexiper
							,rec.get("CDPARENT")
							,rec.get("PORBENEF")
							,"I"
							);
				}
				else if(operacion==eliminar)
				{
					endososDAO.movimientoMpoliperBeneficiario(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,rec.get("CDROL")
							,rec.get("CDPERSON")
							,nmsuplem
							,rec.get("STATUS")
							,rec.get("NMORDDOM")
							,rec.get("SWRECLAM")
							,rec.get("SWEXIPER")
							,rec.get("CDPARENT")
							,rec.get("PORBENEF")
							,"B"
							);
					
					personasDAO.movimientosMpersona(
							rec.get("CDPERSON")
							,rec.get("CDTIPIDE")
							,rec.get("CDIDEPER")
							,rec.get("DSNOMBRE")
							,rec.get("CDTIPPER")
							,rec.get("OTFISJUR")
							,rec.get("OTSEXO")
							,StringUtils.isNotBlank(rec.get("FENACIMI"))?
									Utils.parse(rec.get("FENACIMI"))
									:null
							,rec.get("CDRFC")
							,rec.get("DSEMAIL")
							,rec.get("DSNOMBRE1")
							,rec.get("DSAPELLIDO")
							,rec.get("DSAPELLIDO1")
							,renderFechas.parse(feefecto)//fechaEndoso
							,rec.get("CDNACION")
							,rec.get("CANALING")
							,rec.get("CONDUCTO")
							,rec.get("PTCUMUPR")
							,rec.get("RESIDENCIA")
							,rec.get("NONGRATA")
							,rec.get("CDIDEEXT")
							,rec.get("CDESTCIV")
							,rec.get("CDSUCEMI")
							,usuarioCaptura
							,"B");
				}
				else
				{
					endososDAO.movimientoMpoliperBeneficiario(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,rec.get("CDROL")
							,rec.get("CDPERSON")
							,nmsuplem
							,"V"
							,rec.get("NMORDDOM")
							,rec.get("SWRECLAM")
							,rec.get("SWEXIPER")
							,rec.get("CDPARENT")
							,rec.get("PORBENEF")
							,"U"
							);
					
					personasDAO.movimientosMpersona(
							rec.get("CDPERSON")
							,rec.get("CDTIPIDE")
							,rec.get("CDIDEPER")
							,rec.get("DSNOMBRE")
							,rec.get("CDTIPPER")
							,rec.get("OTFISJUR")
							,rec.get("OTSEXO")
							,StringUtils.isNotBlank(rec.get("FENACIMI"))?
									Utils.parse(rec.get("FENACIMI"))
									:null
							,rec.get("CDRFC")
							,rec.get("DSEMAIL")
							,rec.get("DSNOMBRE1")
							,rec.get("DSAPELLIDO")
							,rec.get("DSAPELLIDO1")
							,renderFechas.parse(feefecto)//fechaEndoso
							,rec.get("CDNACION")
							,rec.get("CANALING")
							,rec.get("CONDUCTO")
							,rec.get("PTCUMUPR")
							,rec.get("RESIDENCIA")
							,rec.get("NONGRATA")
							,rec.get("CDIDEEXT")
							,rec.get("CDESTCIV")
							,rec.get("CDSUCEMI")
							,usuarioCaptura
							,Constantes.UPDATE_MODE);
				}
			}
			
			RespuestaConfirmarEndoso confirmacion = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,cdtipsup
					,""//dscoment
					,renderFechas.parse(feefecto)//fechaEndoso
					,null //cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			/* COMENTAMOS LA CONFIRMACION
			paso = "Confirmando endoso";
			endososDAO.confirmarEndosoB(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nsuplogi,cdtipsup,"");
			
			paso = "Guardando tr\u00e1mite";
			Map<String,String>valores=new HashMap<String,String>();
			valores.put("otvalor01" , ntramiteEmi);
			valores.put("otvalor02" , cdtipsup);
			valores.put("otvalor03" , consultasDAO.recuperarDstipsupPorCdtipsup(cdtipsup));
			valores.put("otvalor04" , nsuplogi);
			valores.put("otvalor05" , cdusuari);
			
			mesaControlDAO.movimientoMesaControl(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,cdunieco
					,cdunieco
					,TipoTramite.ENDOSO.getCdtiptra()
					,new Date()
					,null
					,null
					,null
					,new Date()
					,EstatusTramite.ENDOSO_CONFIRMADO.getCodigo()
					,""
					,null
					,null //cdtipsit << no lo tengo
					,cdusuari
					,cdsisrol, null,null,null
					,valores, null, null, null, null, false
					);
			*/
			
			paso = "Reimprimiendo documentos";
			
			documentosManager.generarDocumentosParametrizados(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,"0" //nmsituac
					,nmsuplem
					,DocumentosManager.PROCESO_ENDOSO //proceso
					,ntramiteEmi
					,null //nmsolici
					, null
					);
			
			/*
			List<Map<String,String>>listaDocu=null;
			listaDocu=endososDAO.reimprimeDocumentos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdtipsup);	
			String rutaCarpeta=new StringBuilder(rutaDocumentosPoliza).append("/").append(ntramite).toString();
			//listaDocu contiene: nmsolici,nmsituac,descripc,descripl
			for(Map<String,String> docu:listaDocu) {
				logger.debug("docu iterado: "+docu);
				String descripc  = docu.get("descripc");
				String descripl  = docu.get("descripl");
				//String nmsolici =  docu.get("nmsolici");
				StringBuilder sb = new StringBuilder();
				sb.append(rutaServidorReportes)
				  .append("?destype=cache")
				  .append("&paramform=no")
				  .append("&ACCESSIBLE=YES")
				  .append("&desformat=PDF")
				  .append("&userid=")  .append(passwordServidorReportes)
				  .append("&report=")  .append(descripl)
				  .append("&p_unieco=").append(cdunieco)
				  .append("&p_ramo=")  .append(cdramo)
				  .append("&p_estado=").append(estado)
				  .append("&p_poliza=").append(nmpoliza)
				  .append("&p_suplem=").append(nmsuplem)
				  .append("&desname=") .append(rutaCarpeta).append("/").append(descripc);
				
				if(descripc.substring(0, 6).equalsIgnoreCase("CREDEN")) {
					// C R E D E N C I A L _ X X X X X X . P D F
					//0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
					sb.append("&p_cdperson=").append(descripc.substring(11, descripc.lastIndexOf("_")));
				}
				String url = sb.toString();
				logger.debug(new StringBuilder()
						.append("\n#################################").append("\n###### Se solicita reporte ######")
						.append("\n###### a ").append(url).toString());
				HttpUtil.generaArchivo(url,new StringBuilder(rutaCarpeta).append("/").append(descripc).toString());
				logger.debug(new StringBuilder()
						.append("\n###### a ").append(url).append("\n###### reporte solicitado ######")
						.append("\n################################").toString());
			}
			*/
			
			if (confirmacion.getRespuestaTurnado() != null) {
			    if (StringUtils.isBlank(resp.getRespuesta())) {
			        resp.setRespuesta(confirmacion.getRespuestaTurnado().getMessage());
			    } else {
			        resp.setRespuesta(Utils.join(resp.getRespuesta(), ". ", confirmacion.getRespuestaTurnado().getMessage()));
			    }
            }
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex,paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ " , resp
				,"\n@@@@@@ guardarEndosoBeneficiarios @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}

	
	@Override
	public void guardaAseguradoAlterno(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String aseguradoAlterno
			)throws Exception
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardaAseguradoAlterno @@@@@@"
				,"\n@@@@@@ cdunieco="         , cdunieco
				,"\n@@@@@@ cdramo="           , cdramo
				,"\n@@@@@@ estado="           , estado
				,"\n@@@@@@ nmpoliza="         , nmpoliza
				,"\n@@@@@@ nmsuplem="         , nmsuplem
				,"\n@@@@@@ aseguradoAlterno=" , aseguradoAlterno
				));
		
		endososDAO.guardaAseguradoAlterno(cdunieco,cdramo,estado,nmpoliza,nmsuplem,aseguradoAlterno);
		
		logger.debug(Utils.log(
				"\n@@@@@@ guardaAseguradoAlterno @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	public List<Map<String,String>> obtenerListaDocumentosEndosos(PolizaVO poliza, String cdmoddoc) throws Exception {
		return endososDAO.obtenerListaDocumentosEndosos(poliza, cdmoddoc);
	}
	
	public void guardarMpolicot(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsituac, String cdclausu, String nmsuplem,
			String status, String cdtipcla, String swmodi, String dslinea,
			String accion) throws Exception {
		
		endososDAO.guardarMpolicot(cdunieco, cdramo, estado, nmpoliza,
				nmsituac, cdclausu, nmsuplem, status, cdtipcla, swmodi,
				dslinea, accion);
	}
	
	@Override
	public boolean esMismaPersonaContratante(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ esMismaPersonaContratante @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				));
		
		boolean esContrat = endososDAO.esMismaPersonaContratante(cdunieco,cdramo,estado,nmpoliza,nmsituac);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ esContrat=",esContrat
				,"\n@@@@@@ esMismaPersonaContratante @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return esContrat;
	}
	
	@Override
	public String recuperarCdtipsitInciso1(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarCdtipsitInciso1 @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		
		String cdtipsit = endososDAO.recuperarCdtipsitInciso1(cdunieco,cdramo,estado,nmpoliza);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdtipsit=",cdtipsit
				,"\n@@@@@@ recuperarCdtipsitInciso1 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return cdtipsit;
	}

	@Override
	public String obtieneTipoFlot(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneTipoFlot @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		
		String tipoflot = endososDAO.obtieneTipoFlot(cdunieco,cdramo,estado,nmpoliza);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ tipoflot=",tipoflot
				,"\n@@@@@@ obtieneTipoFlot @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return tipoflot;
	}
	
	public int recuperarDiasDiferenciaEndosoValidos(String cdramo,String cdtipsup)throws Exception
	{
		return endososDAO.recuperarDiasDiferenciaEndosoValidos(cdramo,cdtipsup);
	}
	
	public boolean revierteEndosoFallido(String cdunieco,String cdramo,String estado,String nmpoliza,String nsuplogi ,String nmsuplem, Integer codigoError
			,String mensajeError, boolean esEndosoB){
		
		/**
		 * Obtener los endosos a revertir
		 */
		
		logger.debug("<<<<<<<<<>>>>>>>>>>   Entrando a Revierte Endoso, se obtienen los endosos a revertir...   <<<<<<<<<>>>>>>>>>>");
		
		List<Map<String,String>> listaEnd = null;
		
		if(esEndosoB){
			try {
				listaEnd = endososDAO.obtieneDatosEndososB(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
			} catch (Exception e) {
				logger.error("Error al revertir el endoso en SIGS. " ,e);
			}
		}else{
			try {
				listaEnd = endososDAO.obtieneEndososPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
			} catch (Exception e) {
				logger.error("Error al revertir el endoso en SIGS. " ,e);
			}
		}
		
		if(listaEnd != null && !listaEnd.isEmpty()){
			if(esEndosoB){
				try {
					Map<String,String> endIt = listaEnd.get(0);
					
					Map<String,Object>params=new LinkedHashMap<String,Object>();
					params.put("vSucursal", endIt.get("SUCURSAL"));
					params.put("vRamo"    , endIt.get("RAMO"));
					params.put("vPoliza"  , endIt.get("NMPOLIEX"));
					params.put("vEndosoB" , endIt.get("NUMEND"));
					
					autosDAOSIGS.revierteEndosoBFallidoSigs(params);
				} catch (Exception e) {
					logger.error("Error al revertir el endoso en SIGS. " ,e);
				}
			}else {
				for(Map<String,String> endIt : listaEnd){
					try {
						Map<String,Object>params=new LinkedHashMap<String,Object>();
						params.put("vSucursal"  , endIt.get("SUCURSAL"));
						params.put("vRamo"      , endIt.get("RAMO"));
						params.put("vPoliza"    , endIt.get("NMPOLIEX"));
						params.put("vTipoEndoso", endIt.get("TIPOEND"));
						params.put("vNumEndoso" , endIt.get("NUMEND"));
						params.put("vError"     , codigoError);
						params.put("vDesError"  , mensajeError);
						
						autosDAOSIGS.revierteEndosoFallidoSigs(params);
					} catch (Exception e) {
						logger.error("Error al revertir el endoso en SIGS. " ,e);
					}
				}
			}
		}
		
		return endososDAO.revierteEndosoFallido(cdunieco, cdramo, estado, nmpoliza, nsuplogi, nmsuplem);
	}
	
	public boolean revierteDomicilio(Map<String, String> params){
		return endososDAO.revierteDomicilio(params);
	}

	public boolean revierteNombrePersona(Map<String, String> params){
		return endososDAO.revierteNombrePersona(params);
	}

	public void reasignaParentescoTitular(Map<String, String> params)throws Exception{
		endososDAO.reasignaParentescoTitular(params);
	}

	public String obtieneNumeroAtributo(String cdtipsit, String nombreAtributo) throws Exception{
		return endososDAO.obtieneNumeroAtributo(cdtipsit, nombreAtributo);
	}
	
	@Override
	public Map<String,Object> pantallaEndosoAltaBajaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String tipoflot
			,String tipo
			,String cdtipsup
			,String contexto
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaEndosoAltaBajaFamilia @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ tipoflot=" , tipoflot
				,"\n@@@@@@ tipo="     , tipo
				,"\n@@@@@@ cdtipsup=" , cdtipsup
				,"\n@@@@@@ contexto=" , contexto
				));
		String             paso  = "Construyendo pantalla de endoso de asegurados";
		Map<String,Object> mapa  = new HashMap<String,Object>();
		Map<String,Item>   items = new HashMap<String,Item>();
		mapa.put("items" , items);
		try
		{
			paso = "Recuperando componentes de pantalla";
			logger.debug("Paso: {}",paso);
			List<ComponenteVO> itemsPoliza = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,tipoflot+tipoflot //cdtipsit
					,null //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"POLIZA"
					,null //orden
					);
			
			List<ComponenteVO> fieldsInciso = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"MODELOS"
					,"INCISO"
					,null //orden
					);
			
			List<ComponenteVO> colsInciso = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,tipoflot+tipoflot //cdtipsit
					,null //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"INCISO"
					,null //orden
					);
			
			List<ComponenteVO> itemsEndoso = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,tipoflot+tipoflot //cdtipsit
					,null //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"ENDOSO"
					,null //orden
					);
			
			List<ComponenteVO> itemsEsqueleto = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,tipoflot+tipoflot //cdtipsit
					,null //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"ESQUELETO"
					,null //orden
					);
			
			List<ComponenteVO> comboGrupos = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,tipoflot+tipoflot //cdtipsit
					,null //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"COMBO_GRUPOS"
					,null //orden
					);
			
			paso = "Construyendo componentes de pantalla";
			logger.debug("Paso: {}",paso);
			GeneradorCampos gc = new GeneradorCampos(contexto);
			
			gc.generaComponentes(itemsPoliza, true, false, true, false, false, false);
			items.put("itemsPoliza" , gc.getItems());
			
			gc.generaComponentes(fieldsInciso, true, true, false, false, false, false);
			items.put("fieldsInciso" , gc.getFields());
			
			gc.generaComponentes(colsInciso, true, false, false, true, true, false);
			items.put("colsInciso" , gc.getColumns());
			
			gc.generaComponentes(itemsEndoso, true, false, true, false, false, false);
			items.put("itemsEndoso" , gc.getItems());
			
			gc.generaComponentes(itemsEsqueleto, true, false, true, false, false, false);
			items.put("itemsEsqueleto" , gc.getItems());
			
			gc.generaComponentes(comboGrupos, true, false, true, false, false, false);
			items.put("comboGrupos" , gc.getItems());
			
			paso = "Recuperando suplemento de endoso";
			logger.debug("Paso: {}",paso);
			Map<String,String> datosEndoso = endososDAO.recuperarNmsuplemNsuplogiEndosoValidando(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,cdtipsup
					);
			mapa.put("nmsuplem_endoso" , datosEndoso.get("nmsuplem"));
			mapa.put("nsuplogi"        , datosEndoso.get("nsuplogi"));
		}
		catch(Exception ex)
		{
			logger.error("Error al construir pantalla de endoso de alta/baja de familias",ex);
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ mapa=",mapa.keySet()
				,"\n@@@@@@ pantallaEndosoAltaBajaFamilia @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mapa;
	}
	
	@Override
	public Map<String,String> recuperarComponentesAltaAsegurado(
			String cdramo
			,String cdtipsit
			,String depFam
			,String cdsisrol
			,String contexto
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarComponentesAltaAsegurado @@@@@@"
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ depFam="   , depFam
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ contexto=" , contexto
				));
		
		String             items = null;
		String             paso  = "Recuperando componentes";
		Map<String,String> mapa  = new HashMap<String,String>();
		
		try
		{
			List<ComponenteVO> mpersona = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,cdtipsit
					,depFam //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"MPERSONA"
					,null //orden
					);
			
			List<ComponenteVO> tatrisit = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,cdtipsit
					,depFam //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"TATRISIT"
					,null //orden
					);
			
			List<ComponenteVO> tatrirol = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,cdtipsit
					,depFam //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"TATRIROL"
					,null //orden
					);
			
			List<ComponenteVO> validacion = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,cdramo
					,cdtipsit
					,depFam //estado
					,cdsisrol
					,"ENDOSO_FAMILIA"
					,"VALIDACION"
					,null //orden
					);
			if(validacion==null||validacion.size()==0)
			{
				throw new ApplicationException("No hay validaci\u00F3n definida");
			}
			
			paso = "Construyendo componentes";
			logger.debug("Paso: {}",paso);
			
			GeneradorCampos gc = new GeneradorCampos(contexto);
			gc.generaComponentes(mpersona, true, false, true, false, false, false);
			mapa.put("mpersona" , Utils.join("[",gc.getItems().toString(),"]"));
			
			gc.generaComponentes(tatrisit, true, false, true, false, false, false);
			mapa.put("tatrisit" , Utils.join("[",gc.getItems().toString(),"]"));
			
			gc.generaComponentes(tatrirol, true, false, true, false, false, false);
			mapa.put("tatrirol" , Utils.join("[",gc.getItems().toString(),"]"));
			
			gc.generaComponentes(validacion, true,false,false,false,false,true);
			mapa.put("validacion" , gc.getButtons().toString());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarComponentesAltaAsegurado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mapa;
	}
	
	@Override
	public String confirmarEndosoAltaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nsuplogi
			,Date fecha
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			,List<String> incisos
			,String cdtipsitPrimerInciso
			,String nmsolici
			,FlujoVO flujo
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ confirmarEndosoAltaFamilia @@@@@@"
				,"\n@@@@@@ cdusuari             = " , cdusuari
				,"\n@@@@@@ cdsisrol             = " , cdsisrol
				,"\n@@@@@@ cdelemen             = " , cdelemen
				,"\n@@@@@@ cdunieco             = " , cdunieco
				,"\n@@@@@@ cdramo               = " , cdramo
				,"\n@@@@@@ estado               = " , estado
				,"\n@@@@@@ nmpoliza             = " , nmpoliza
				,"\n@@@@@@ cdtipsup             = " , cdtipsup
				,"\n@@@@@@ nmsuplem             = " , nmsuplem
				,"\n@@@@@@ nsuplogi             = " , nsuplogi
				,"\n@@@@@@ fecha                = " , fecha
				,"\n@@@@@@ rutaDocumentosPoliza = " , rutaDocumentosPoliza
				,"\n@@@@@@ rutaServidorReports  = " , rutaServidorReports
				,"\n@@@@@@ passServidorReports  = " , passServidorReports
				,"\n@@@@@@ usuario              = " , usuario
				,"\n@@@@@@ incisos              = " , incisos
				,"\n@@@@@@ cdtipsitPrimerInciso = " , cdtipsitPrimerInciso
				,"\n@@@@@@ nmsolici             = " , nmsolici
				,"\n@@@@@@ flujo                = " , flujo
				));
		
		String respuesta = null;
		String paso      = "Iniciando endoso de baja de asegurados";
		
		try
		{
			paso = "Regenerando suplemento";
			logger.debug(paso);
			
			String nmsuplemRegenerado = endososDAO.regeneraSuplementoFamiliaEndoso(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsuplem
				,nsuplogi
				,fecha
			);
			
			/*for(String nmsituac:incisos){
				paso = "Obtenemos los valores por defecto";
				cotizacionDAO.valoresPorDefecto(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplemRegenerado
						,"TODO"
						,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
				);
			}*/
			
			paso = "Tarificando inciso";
			logger.debug(paso);
			
			endososDAO.sigsvalipolEnd(
				cdusuari
				,cdelemen
				,cdunieco
				,cdramo
				,estado
				,nmpoliza
				,"0"
				,nmsuplemRegenerado
				,cdtipsup
			);
			
			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
			mapaValorEndoso.put("pv_estado_i"   , estado);
			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
			mapaValorEndoso.put("pv_nmsituac_i" , "0");
			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplemRegenerado);
			mapaValorEndoso.put("pv_feinival_i" , fecha);
			mapaValorEndoso.put("pv_cdtipsup_i" , TipoEndoso.ALTA_ASEGURADOS.getCdTipSup().toString());
			this.calcularValorEndoso(mapaValorEndoso);
			
			RespuestaConfirmarEndoso confirmacion = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemRegenerado
					,nsuplogi
					,cdtipsup
					,null //dscoment
					,fecha
					,null//cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			boolean enEspera = !confirmacion.isConfirmado();
			
			String ntramiteEndoso = confirmacion.getNtramite();
			
			String ntramiteEmi = confirmacion.getNtramiteEmision();
			
			/* SE COMENTA LA CONFIRMACION
			paso               = "Recuperando d\u00EDas v\u00E1lidos de cotizaci\u00F3n";
			long maximos       = endososDAO.recuperarDiasDiferenciaEndosoValidos(cdramo, cdtipsup);
			String ntramiteEmi = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
			
			Date fechaHoy                      = new Date();
			long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fecha.getTime();
			diferenciaFechaActualVSEndoso      = Math.abs(diferenciaFechaActualVSEndoso);
			long maximoDiasPermitidos          = maximos*24l*60l*60l*1000l;
			
			String descEndoso = endososDAO.obtieneDescripcionEndoso(cdtipsup); 
			
			logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
			logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
			
			String  estatusTramite = null;
			boolean enEspera       = false;
			if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos)
			{
				estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
				enEspera       = true;
			}
			else
			{
				paso = "Confirmando endoso";
				endososDAO.confirmarEndosoB(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplemRegenerado
						,nsuplogi
						,cdtipsup
						,null //dscoment
						);
				
				estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
			}
			
			paso = "Guardando tr\u00E1mite de endoso";
			Map<String,String> valoresMesaControl = new LinkedHashMap<String,String>();
			valoresMesaControl.put("otvalor01" , ntramiteEmi);
			valoresMesaControl.put("otvalor02" , cdtipsup);
			valoresMesaControl.put("otvalor03" , descEndoso);
			valoresMesaControl.put("otvalor04" , nsuplogi);
			valoresMesaControl.put("otvalor05" , cdusuari);
			
			String ntramiteEndoso = mesaControlDAO.movimientoMesaControl(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemRegenerado
					,cdunieco //cdsucadm
					,cdunieco //cdsucdoc
					,TipoTramite.ENDOSO.getCdtiptra() //cdtiptra
					,fecha //ferecepc
					,null  //cdagente
					,null  //referencia
					,null  //nombre
					,fecha //festatus
					,estatusTramite
					,null  //comments
					,null //nmsolici
					,cdtipsitPrimerInciso
					,cdusuari
					,cdsisrol, null,null,null
					,valoresMesaControl, null, null, null, null, false
					);
			*/
			
			if(!enEspera)
			{
				//DOCUMENTOS
				documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0"
						,nmsuplemRegenerado
						,DocumentosManager.PROCESO_ENDOSO
						,null
						,null
						,null
				);
				
				String sucursal = cdunieco;
				
				String rutaCarpeta=Utils.join(rutaDocumentosPoliza,"/",ntramiteEmi);
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplemRegenerado,
						rutaCarpeta, 
						sucursal, nmsolici, ntramiteEmi, 
						true, cdtipsup, 
						usuario);
				
				respuesta = Utils.join("Se ha guardado el endoso ",nsuplogi);
			}
			else
			{
				String mensajeInvalido = "";
				respuesta = Utils.join("El endoso ",nsuplogi
						," se guard\u00f3 en mesa de control para autorizaci\u00f3n"
						," con n\u00famero de tr\u00e1mite ",ntramiteEndoso
						+mensajeInvalido);
			}
			if (confirmacion.getRespuestaTurnado() != null) {
			    respuesta = Utils.join(respuesta, ". ", confirmacion.getRespuestaTurnado().getMessage());
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ respuesta = " , respuesta
				,"\n@@@@@@ confirmarEndosoAltaFamilia @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return respuesta;
	}
	
	
	@Override
	public String confirmarEndosoBajaFamilia(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsup
			,String nmsuplem
			,String nsuplogi
			,Date fecha
			,String rutaDocumentosPoliza
			,String rutaServidorReports
			,String passServidorReports
			,UserVO usuario
			,List<String> incisos
			,String cdtipsitPrimerInciso
			,String nmsolici
			,FlujoVO flujo
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ confirmarEndosoBajaFamilia @@@@@@"
				,"\n@@@@@@ cdusuari             = " , cdusuari
				,"\n@@@@@@ cdsisrol             = " , cdsisrol
				,"\n@@@@@@ cdelemen             = " , cdelemen
				,"\n@@@@@@ cdunieco             = " , cdunieco
				,"\n@@@@@@ cdramo               = " , cdramo
				,"\n@@@@@@ estado               = " , estado
				,"\n@@@@@@ nmpoliza             = " , nmpoliza
				,"\n@@@@@@ cdtipsup             = " , cdtipsup
				,"\n@@@@@@ nmsuplem             = " , nmsuplem
				,"\n@@@@@@ nsuplogi             = " , nsuplogi
				,"\n@@@@@@ fecha                = " , fecha
				,"\n@@@@@@ rutaDocumentosPoliza = " , rutaDocumentosPoliza
				,"\n@@@@@@ rutaServidorReports  = " , rutaServidorReports
				,"\n@@@@@@ passServidorReports  = " , passServidorReports
				,"\n@@@@@@ usuario              = " , usuario
				,"\n@@@@@@ incisos              = " , incisos
				,"\n@@@@@@ cdtipsitPrimerInciso = " , cdtipsitPrimerInciso
				,"\n@@@@@@ nmsolici             = " , nmsolici
				,"\n@@@@@@ flujo                = " , flujo
				));
		
		String respuesta = null;
		String paso      = "Iniciando endoso de baja de asegurados";
		try
		{
			paso = "Regenerando suplemento";
			String nmsuplemRegenerado = endososDAO.regeneraSuplemento(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,nsuplogi
					,fecha
					);
			
			for(String nmsituac:incisos)
			{
				paso = "Tarificando inciso";
				endososDAO.sigsvalipolEnd(
						cdusuari
						,cdelemen
						,cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplemRegenerado
						,cdtipsup
						);
			}
			
			Map<String,Object>mapaValorEndoso=new LinkedHashMap<String,Object>(0);
			mapaValorEndoso.put("pv_cdunieco_i" , cdunieco);
			mapaValorEndoso.put("pv_cdramo_i"   , cdramo);
			mapaValorEndoso.put("pv_estado_i"   , estado);
			mapaValorEndoso.put("pv_nmpoliza_i" , nmpoliza);
			mapaValorEndoso.put("pv_nmsituac_i" , "0");
			mapaValorEndoso.put("pv_nmsuplem_i" , nmsuplemRegenerado);
			mapaValorEndoso.put("pv_feinival_i" , fecha);
			mapaValorEndoso.put("pv_cdtipsup_i" , TipoEndoso.BAJA_ASEGURADOS.getCdTipSup().toString());
			this.calcularValorEndoso(mapaValorEndoso);
			
			RespuestaConfirmarEndoso confirmacion = this.confirmarEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemRegenerado
					,nsuplogi
					,cdtipsup
					,null//dscoment
					,fecha
					,null//cdtipsit
					,flujo
					,cdusuari
					,cdsisrol
					);
			
			boolean enEspera = !confirmacion.isConfirmado();
			
			String ntramiteEndoso = confirmacion.getNtramite();
			
			String ntramiteEmi = confirmacion.getNtramiteEmision();
			
			/* SE COMENTA LA CONFIRMACION
			paso               = "Recuperando d\u00EDas v\u00E1lidos de cotizaci\u00F3n";
			long maximos       = endososDAO.recuperarDiasDiferenciaEndosoValidos(cdramo, cdtipsup);
			String ntramiteEmi = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
			
			Date fechaHoy                      = new Date();
			long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fecha.getTime();
			diferenciaFechaActualVSEndoso      = Math.abs(diferenciaFechaActualVSEndoso);
			long maximoDiasPermitidos          = maximos*24l*60l*60l*1000l;
			
			String descEndoso = endososDAO.obtieneDescripcionEndoso(cdtipsup); 
			
			logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
			logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
			
			String  estatusTramite = null;
			boolean enEspera       = false;
			if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos)
			{
				estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
				enEspera       = true;
			}
			else
			{
				paso = "Confirmando endoso";
				endososDAO.confirmarEndosoB(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplemRegenerado
						,nsuplogi
						,cdtipsup
						,null //dscoment
						);
				
				estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
			}
			
			paso = "Guardando tr\u00E1mite de endoso";
			Map<String,String> valoresMesaControl = new LinkedHashMap<String,String>();
			valoresMesaControl.put("otvalor01" , ntramiteEmi);
			valoresMesaControl.put("otvalor02" , cdtipsup);
			valoresMesaControl.put("otvalor03" , descEndoso);
			valoresMesaControl.put("otvalor04" , nsuplogi);
			valoresMesaControl.put("otvalor05" , cdusuari);
			
			String ntramiteEndoso = mesaControlDAO.movimientoMesaControl(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplemRegenerado
					,cdunieco //cdsucadm
					,cdunieco //cdsucdoc
					,TipoTramite.ENDOSO.getCdtiptra() //cdtiptra
					,fecha //ferecepc
					,null  //cdagente
					,null  //referencia
					,null  //nombre
					,fecha //festatus
					,estatusTramite
					,null  //comments
					,null //nmsolici
					,cdtipsitPrimerInciso
					,cdusuari
					,cdsisrol, null,null,null
					,valoresMesaControl, null, null, null, null, false
					);
			*/
			
			if(!enEspera)
			{
				//DOCUMENTOS
				documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado //estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplemRegenerado
						,DocumentosManager.PROCESO_ENDOSO //proceso
						,null	
						,null //nmsolici
						, null
				);
				
				String sucursal = cdunieco;
				
				String rutaCarpeta=Utils.join(rutaDocumentosPoliza,"/",ntramiteEmi);
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplemRegenerado, rutaCarpeta, 
						sucursal, nmsolici, ntramiteEmi, 
						true, cdtipsup, 
						usuario);
				
				respuesta = Utils.join("Se ha guardado el endoso ",nsuplogi);
			}
			else
			{
				String mensajeInvalido = "";
				respuesta = Utils.join("El endoso ",nsuplogi
						," se guard\u00f3 en mesa de control para autorizaci\u00f3n"
						," con n\u00famero de tr\u00e1mite ",ntramiteEndoso
						+mensajeInvalido);
			}
			if (confirmacion.getRespuestaTurnado() != null) {
			    respuesta = Utils.join(respuesta, ". ", confirmacion.getRespuestaTurnado().getMessage());
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ respuesta = " , respuesta
				,"\n@@@@@@ confirmarEndosoBajaFamilia @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return respuesta;
	}
	
	@Override
	@Deprecated
	public String recuperarUltimoNmsuplem(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		return consultasDAO.recuperarUltimoNmsuplem(cdunieco,cdramo,estado,nmpoliza);
	}
	@Override
	public List<Map<String,String>> obtenerInfoFamiliaEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_ntramite_i" , ntramite);
		List<Map<String,String>>lista=endososDAO.obtenerInfoFamiliaEndoso(params);
		lista=lista!=null?lista:new ArrayList<Map<String,String>>();
		
		logger.debug("Resultado de carga asegurados : "+lista);
		
		return lista;
	}
	
	@Override
	public void  clonarGarantiaCapitales(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String cdgrupo
			,String cdplan
			,String sexo) throws Exception
	{
		logger.debug("EndososManager clonarGarantiaCapitales params {},{},{},{},{},{},{},{},{}"
				,cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,cdgrupo,cdplan,sexo);
		
		endososDAO.clonarGarantiaCapitales(cdunieco,cdramo,estado,nmpoliza,nmsuplem,nmsituac,cdgrupo,cdplan,sexo);
		logger.debug("EndososManager clonarGarantiaCapitales end");
	}
	
	@Override
	public void actualizaExtraprimaValosit2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String extraprima
			,String extraprimaOcu
			) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_cdunieco_i"   , cdunieco);
		params.put("pv_cdramo_i"     , cdramo);
		params.put("pv_estado_i"     , estado);
		params.put("pv_nmpoliza_i"   , nmpoliza);
		params.put("pv_nmsituac_i"   , nmsituac);
		params.put("pv_nmsuplem_i"   , nmsuplem);
		params.put("pv_extraprima_sob_i" , extraprima);
		params.put("pv_extraprima_ocu_i" , extraprimaOcu);
		logger.debug("EndososManager actualizaExtraprimaValosit params: "+params);
		endososDAO.actualizaExtraprimaValosit2(params);
		logger.debug("EndososManager actualizaExtraprimaValosit end");
	}
	
	public Map<String,Item> cargaInfoPantallaClonacion() throws Exception
	{
		String paso = null;
		Map<String,Item> elementos = new HashMap<String,Item>();
		
		try
		{
			paso = "Probando paso";
			
			paso = "Recuperando elementos";
			
			List<ComponenteVO> itemsForm = pantallasDAO.obtenerComponentes(null //cdtiptra
																		   ,null //cdunieco
																		   ,null //cdramo
																		   ,null //cdtipsit
																		   ,null //estado
																		   ,null //cdsisrol
																		   ,"TRAMITE_CLONACION"
																		   ,"FORMULARIO"
																		   ,null //orden
																			);

			paso = "Generando elementos";
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsForm, true, false, true, false, false, false);
			
			elementos.put("itemsFormulario" , gc.getItems());
			
			List<ComponenteVO> itemsGrid = pantallasDAO.obtenerComponentes(
					null //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"TRAMITE_CLONACION"
					,"MODELO_CLONACION"
					,null //orden
					);
			
			paso = "Generando elementos";
			
			gc.generaComponentes(itemsGrid, true, true, true, true, true, false);
			
			elementos.put("itemsGrid" , gc.getColumns());
			
			elementos.put("itemsGridModel" , gc.getFields());			
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		return elementos;
	}
	
	@Override
	public List<Map<String, String>> buscarCotizaciones(String cdunieco, 
														String cdramo, 
														String cdtipsit, 
														String estado, 
														String nmpoliza, 
														String ntramite, 
														String status, 
														String fecini, 
														String fecfin, 
														String cdsisrol, 
														String cdusuari) throws Exception {
		String paso = null;
		List<Map<String, String>> infoGrid = null;		
		try
		{
			paso = "Antes de buscar cotizacion";
			String cdagente = null;
			
			if (cdsisrol.toUpperCase().equals("EJECUTIVOCUENTA")){
				cdagente = cdusuari;
			}
			
			infoGrid = endososDAO.recuperarCotizaciones(
					cdunieco, 
					cdramo, 
					cdtipsit, 
					estado, 
					nmpoliza, 
					ntramite, 
					status, 
					fecini, 
					fecfin, 
					cdsisrol, 
					cdusuari,
					cdagente);
			
			paso = "Despues de buscar cotizacion";
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		return infoGrid;
	}
	
	@Override
	public Map<String,Object> procesarCensoClonacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cduniecoOrig,
			String cdramoOrig,
			String estadoOrig,
			String nmpolizaOrig,
			File   censo,
			String rutaDocumentosTemporal,
			String dominioServerLayouts,
			String userServerLayouts,
			String passServerLayouts,
			String rootServerLayouts,
			String cdtipsit,
			String cdusuari,
			String cdsisrol)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ complementoSaludGrupo @@@@@@"				
				,"\n@@@@@@ cdunieco="               , cdunieco
				,"\n@@@@@@ cdramo="                 , cdramo
				,"\n@@@@@@ estado="                 , estado
				,"\n@@@@@@ nmpoliza="               , nmpoliza
				,"\n@@@@@@ cduniecoOrig="           , cduniecoOrig
				,"\n@@@@@@ cdramoOrig="             , cdramoOrig
				,"\n@@@@@@ estadoOrig="             , estadoOrig
				,"\n@@@@@@ nmpolizaOrig="           , nmpolizaOrig
				,"\n@@@@@@ censo="                  , censo
				,"\n@@@@@@ rutaDocumentosTemporal=" , rutaDocumentosTemporal
				,"\n@@@@@@ dominioServerLayouts="   , dominioServerLayouts
				,"\n@@@@@@ userServerLayouts="      , userServerLayouts
				,"\n@@@@@@ passServerLayouts="      , passServerLayouts
				,"\n@@@@@@ rootServerLayouts="      , rootServerLayouts
				,"\n@@@@@@ cdtipsit="               , cdtipsit
				,"\n@@@@@@ cdusuari="               , cdusuari
				,"\n@@@@@@ cdsisrol="               , cdsisrol
				));
		
		Map<String,Object> resp = new HashMap<String,Object>();
		
		String paso = "Complementando asegurados";
		try
		{
			paso = "Recuperando configuracion de complemento";
			logger.debug("\nPaso: {}",paso);
			List<Map<String,String>>configs=cotizacionDAO.cargarParametrizacionExcel("COMPGRUP",cdramo, "CLONACION");
			logger.debug("\nConfigs: {}",configs);
			
			paso = "Filtrando filas con errores";
			logger.debug("\nPaso: {}",paso);
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream(censo));
			if(workbook.getNumberOfSheets()!=1)
			{
				throw new ApplicationException("Favor de revisar el n\u00famero de hojas del censo");
			}
			
			Iterator<Row>            rowIterator = workbook.getSheetAt(0).iterator();
			List<Map<String,String>> registros   = new ArrayList<Map<String,String>>();
			List<Map<String,String>> recordsDTO  = new ArrayList<Map<String,String>>();
			int                      nTotal      = 0;
			int                      nBuenas     = 0;
			int                      nError      = 0;
			StringBuilder            errores     = new StringBuilder();
			//se agrega para validar que esten todos los grupos y para que tengan al menos un asegurado valido
			Map<String,String> grupos = new HashMap<String,String>();
			
			int fila = 0;
			String[] columnas=new String[]{
					  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
					,"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"
					,"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ"
			};
			while (rowIterator.hasNext()) 
            {
				fila++;
				nTotal++;
				
				logger.debug("\nIterando fila {}",fila);
				
				Row                row         = rowIterator.next();
				boolean            filaBuena   = true;
				StringBuilder      bufferLinea = new StringBuilder();
				Map<String,String> registro    = new HashMap<String,String>();
				Map<String,String> recordDTO   = new LinkedHashMap<String,String>();
				
				if(Utils.isRowEmpty(row))
				{
					break;
				}
				
				for(Map<String,String>config : configs)
				{
					try
					{
						logger.debug("\nIterando config {}",config);
						int    indice = Integer.parseInt(config.get("COLUMNA"));
						String letra  = columnas[indice];
						Cell   celda  = row.getCell(indice);
						
						String tipo   = config.get("TIPO");
						String valor  = extraerStringDeCelda(row.getCell(indice),tipo);
						String substr = config.get("CDTIPSIT");
						logger.debug("\nValor {} Tipo {} Substr {}",valor,tipo,substr);
						
						boolean obligatorio = "S".equals(config.get("REQUERIDO"));
						
						//validar obligatorio
						if(obligatorio&&StringUtils.isBlank(valor))
						{
							filaBuena = false;
							errores.append(Utils.join("Se requiere ",letra,", "));
						}
						
						//validar tipo
						if(StringUtils.isNotBlank(valor))
						{
							if("int".equals(tipo))
							{
								try
								{
									Integer.parseInt(valor);
								}
								catch(Exception ex)
								{
									filaBuena = false;
									errores.append(Utils.join("No es numerico ",letra,", "));
								}
							}
							else if("double".equals(tipo))
							{
								try
								{
									Double.parseDouble(valor);
								}
								catch(Exception ex)
								{
									filaBuena = false;
									errores.append(Utils.join("No es decimal ",letra,", "));
								}
							}
							else if("date".equals(tipo))
							{
								try
								{
									logger.debug("\nAntes leer fecha");
									celda.setCellType(Cell.CELL_TYPE_NUMERIC);
									Date fecha = celda.getDateCellValue();
									logger.debug("\nFecha leida: {}",fecha);
									Calendar cal = Calendar.getInstance();
				                	cal.setTime(fecha);
				                	if(cal.get(Calendar.YEAR)>2100
				                			||cal.get(Calendar.YEAR)<1900
				                			)
				                	{
				                		throw new ApplicationException("El anio de la fecha no es valido");
				                	}
				                	valor = renderFechas.format(fecha);
								}
								catch(Exception ex)
								{
									logger.error("Erro al leer fecha",ex);
									filaBuena = false;
									errores.append(Utils.join("Fecha incorrecta ",letra,", "));
								}
							}
						}
						
						//validar 
						if(StringUtils.isNotBlank(valor)&&StringUtils.isNotBlank(substr))
						{
							if(substr.indexOf(Utils.join("|",valor,"|"))==-1)
							{
								filaBuena = false;
								errores.append(Utils.join("Valor incorrecto ",letra,", "));
							}
						}

						bufferLinea.append(Utils.join(valor,"-"));
						recordDTO.put(config.get("PROPIEDAD"),valor);
						registro.put(config.get("PROPIEDAD"),valor);
						registro.put(Utils.join("_",letra,"_",config.get("PROPIEDAD")),config.get("DESCRIPCION"));
						//se agrega para validar que esten todos los grupos y para que tengan al menos un asegurado valido
						if("CDGRUPO".equals(config.get("PROPIEDAD")))
						{
							String cdgrupo = valor;
							if(!grupos.containsKey(cdgrupo))
							{
								grupos.put(cdgrupo , "NO VALIDO");
							}
						}
					}
					catch(Exception ex)
					{
						filaBuena = false;
					}
				}
				
				if(filaBuena)
				{
					nBuenas++;
					registros.add(registro);
					recordsDTO.add(recordDTO);
					//se agrega para validar que esten todos los grupos y para que tengan al menos un asegurado valido
					grupos.put(recordDTO.get("CDGRUPO"),"VALIDO");
				}
				else
				{
					nError++;
					errores.append(Utils.join("en la fila ",fila,": ",bufferLinea.toString(),"\n"));
				}
            }
			
			resp.put("erroresCenso"    , errores.toString());
			resp.put("filasLeidas"     , Integer.toString(nTotal));
			resp.put("filasProcesadas" , Integer.toString(nBuenas));
			resp.put("filasErrores"    , Integer.toString(nError));
			resp.put("registros"       , registros);
			/* se agrega para validar que esten todos los grupos y para que tengan al menos un asegurado valido INICIO */
			logger.debug(Utils.log("Grupos de excel:",grupos));
			
			paso = "Recuperando numero de grupos de la poliza original";
			int numGrupOri = endososDAO.recuperarNumeroGruposPoliza(
					cduniecoOrig, 
					cdramoOrig, 
					estadoOrig, 
					nmpolizaOrig);
			if(numGrupOri != grupos.keySet().size())
			{
				throw new ApplicationException(Utils.join("La p\u00f3liza original tiene ",numGrupOri," y en el excel solo se encontraron ",grupos.keySet().size()));
			}
			
			paso = "Verificando grupos v\u00e1lidos";
			StringBuilder gruposInvalidos = new StringBuilder();
			for(Entry<String,String> en : grupos.entrySet())
			{
				String cdgrupo = en.getKey()
				       ,valido = en.getValue();
				
				if(!"VALIDO".equals(valido))
				{
					gruposInvalidos.append("No hay asegurados v\u00e1lidos para el grupo ").append(cdgrupo).append("<br/>");
				}
			}
			if(gruposInvalidos.toString().length()>0)
			{
				throw new ApplicationException(gruposInvalidos.toString());
			}
			/* se agrega para validar que esten todos los grupos y para que tengan al menos un asegurado valido FIN */
			paso = "Generando archivo de transferencia";
			logger.debug("\nPaso: {}",paso);
			String nombreCenso = null;
			if(nBuenas>0)
			{
				nombreCenso = Utils.join("censo_",System.currentTimeMillis(),"_",nmpoliza,".txt");
				
				File        archivoTxt = new File(Utils.join(rutaDocumentosTemporal,"/",nombreCenso));
				PrintStream output     = new PrintStream(archivoTxt);
				for(Map<String,String>recordDTO:recordsDTO)
				{
					for(Entry<String,String>en:recordDTO.entrySet())
					{
						output.print(Utils.join(en.getValue(),"|"));
					}
					output.println();
				}
				output.close();
				
				paso = "Transfiriendo archivo";
				logger.debug("\nPaso: {}",paso);
				
				boolean transferido = FTPSUtils.upload
				(
					dominioServerLayouts,
					userServerLayouts,
					passServerLayouts,
					archivoTxt.getAbsolutePath(),
					Utils.join(rootServerLayouts,"/",nombreCenso)
				)
				&&FTPSUtils.upload
				(
					dominioServerLayouts2,
					userServerLayouts,
					passServerLayouts,
					archivoTxt.getAbsolutePath(),
					Utils.join(rootServerLayouts,"/",nombreCenso)
				);
				if(!transferido)
				{
					throw new ApplicationException("No se pudo transferir el archivo");
				}
				
				paso = "Recuperando procedimiento";
				logger.debug("\nPaso: {}",paso);
				Map<String,String> mapaProc = cotizacionDAO.obtenerParametrosCotizacion(
						ParametroCotizacion.PROCEDURE_CENSO
						,cdramo
						,cdtipsit
						,"CLONACION"
						,null
						);
				String nombreProc = mapaProc.get("P1VALOR");
				logger.debug("\ncenso: {}",nombreProc);
				
				paso = "Procesar censo";
				logger.debug("\nPaso: {}",paso);
				endososDAO.procesarCensoClonacion(
						nombreProc,
						nombreCenso,
						cdunieco,
						cdramo,
						estado,
						nmpoliza,
						cduniecoOrig,
						cdramoOrig,
						estadoOrig,
						nmpolizaOrig);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ complementoSaludGrupo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	public void confirmarClonacionCondiciones(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String cdtipsit,
			int numSituac) throws Exception{		
		String paso = "llamando hilos de extraprima";		
		logger.debug(paso);
		new ConfirmarClonacionCondiciones(cdunieco, cdramo, estado, nmpoliza, cdtipsit, numSituac).start();
	}
	
	private class ConfirmarClonacionCondiciones extends Thread{
		private String cdunieco;
		private String cdramo;
		private String estado;
		private String nmpoliza;
		private String cdtipsit;
		private int    numSituac;
		
		public ConfirmarClonacionCondiciones(
				String cdunieco,
				String cdramo,
				String estado,
				String nmpoliza,
				String cdtipsit,
				int    numSituac){
			this.cdunieco  = cdunieco;
			this.cdramo    = cdramo;
			this.estado    = estado;
			this.nmpoliza  = nmpoliza;
			this.cdtipsit  = cdtipsit;
			this.numSituac = numSituac;
		}
		
		@Override
		public void run(){
			try{
				cotizacionDAO.movimientoTbloqueo(
						cdunieco, 
						cdramo, 
						estado, 
						nmpoliza, 
						"0", 
						"I");
				for(int i = 1; i <= numSituac; i++)
				{
					List<Map<String,String>> coberturasDeExtraprimas = consultasDAO.recuperaCoberturasExtraprima(this.cdramo, this.cdtipsit);
					for(Map<String,String> coberturaExtraprima : coberturasDeExtraprimas)
					{
						String cdgarant = coberturaExtraprima.get("CDGARANT");
						cotizacionDAO.valoresPorDefecto(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,String.valueOf(i)
								,String.valueOf(0)
								,cdgarant
								,TipoEndoso.EMISION_POLIZA.getCdTipSup().toString()
								);
					}
				}
				cotizacionDAO.movimientoTbloqueo(
						cdunieco, 
						cdramo, 
						estado, 
						nmpoliza, 
						"0", 
						"D");
			}catch(Exception ex){
				logger.error("error lanzando valores po defecto extraprima: ",ex);
			}
		}
	}

	private String extraerStringDeCelda(Cell cell, String tipo)
	{
		try
		{
			if("date".equals(tipo)&&cell.getCellType()==Cell.CELL_TYPE_NUMERIC)
			{
				return renderFechas.format(cell.getDateCellValue());
			}
			else
			{
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue().toString();
			}
		}
		catch(Exception ex)
		{
			return "";
		}
	}
    
	
	private String extraerStringDeCelda(Cell cell)
	{
		try
		{
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String cadena = cell.getStringCellValue();
			return cadena==null?"":cadena;
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	@Deprecated
	@Override
	public void confirmarEndosoB(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsuplem
    		,String nsuplogi
    		,String cdtipsup
    		,String dscoment
    		)throws Exception
    {
		endososDAO.confirmarEndosoB(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nsuplogi, cdtipsup, dscoment);
    }
	
	private class RespuestaConfirmarEndoso
	{
		private boolean confirmado;
		
		private String ntramite
		               ,ntramiteEmision;
		
		private RespuestaTurnadoVO respuestaTurnado;
		
		public RespuestaConfirmarEndoso(){}

		public boolean isConfirmado() {
			return confirmado;
		}

		public void setConfirmado(boolean confirmado) {
			this.confirmado = confirmado;
		}

		public String getNtramite() {
			return ntramite;
		}

		public void setNtramite(String ntramite) {
			this.ntramite = ntramite;
		}

		public String getNtramiteEmision() {
			return ntramiteEmision;
		}

		public void setNtramiteEmision(String ntramiteEmision) {
			this.ntramiteEmision = ntramiteEmision;
		}
		
		@Override
		public String toString()
		{
			return Utils.log(
					 "\n**************************************"
					,"\n****** RespuestaConfirmarEndoso ******"
					,"\n****** confirmado      = " , this.confirmado
					,"\n****** ntramite        = " , this.ntramite
					,"\n****** ntramiteEmision = " , this.ntramiteEmision
					,"\n**************************************"
					);
		}

        public RespuestaTurnadoVO getRespuestaTurnado() {
            return respuestaTurnado;
        }

        public void setRespuestaTurnado(RespuestaTurnadoVO respuestaTurnado) {
            this.respuestaTurnado = respuestaTurnado;
        }
	}
	
	private RespuestaConfirmarEndoso confirmarEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nsuplogi
			,String cdtipsup
			,String dscoment
			,Date fechaEndoso
			,String cdtipsit
			,FlujoVO flujo
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ confirmarEndoso @@@@@@"
				,"\n@@@@@@ cdunieco    = " , cdunieco
				,"\n@@@@@@ cdramo      = " , cdramo
				,"\n@@@@@@ estado      = " , estado
				,"\n@@@@@@ nmpoliza    = " , nmpoliza
				,"\n@@@@@@ nmsuplem    = " , nmsuplem
				,"\n@@@@@@ nsuplogi    = " , nsuplogi
				,"\n@@@@@@ cdtipsup    = " , cdtipsup
				,"\n@@@@@@ dscoment    = " , dscoment
				,"\n@@@@@@ fechaEndoso = " , fechaEndoso
				,"\n@@@@@@ cdtipsit    = " , cdtipsit
				,"\n@@@@@@ cdusuari    = " , cdusuari
				,"\n@@@@@@ cdsisrol    = " , cdsisrol
				,"\n@@@@@@ flujo       = " , flujo
				));
		
		RespuestaConfirmarEndoso respuesta = new RespuestaConfirmarEndoso();
		
		String paso = null;
		
		try
		{
		    Date fechaHoy = new Date();
			if(consultasDAO.esProductoSalud(cdramo)) // SALUD
			{
				if(flujo == null) // SALUD SIN FLUJO
				{
					paso = "Recuperando d\u00edas de endoso v\u00e1lido";
					logger.debug(paso);
					
					long numMaximoDias = (long)endososDAO.recuperarDiasDiferenciaEndosoValidos(cdramo,cdtipsup);
					
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se obtiene el numero de tramite de emision de una poliza:
					String ntramiteEmision = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					respuesta.setNtramiteEmision(ntramiteEmision);
					
					// Se almacena la diferencia entre la fecha actual y a fecha que tendra el endoso:
					long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fechaEndoso.getTime();
					diferenciaFechaActualVSEndoso = Math.abs(diferenciaFechaActualVSEndoso);
					// Se almacena el maximo de dias permitidos para realizar un endoso (30 dias):
					long maximoDiasPermitidos = numMaximoDias*24l*60l*60l*1000l;
					
					paso = "Recuperando descripci\u00f3n de endoso";
					logger.debug(paso);
					
					String descEndoso = endososDAO.obtieneDescripcionEndoso(cdtipsup); 
					
					logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
					logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
					
					String estatusTramite = null;
					if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos) {
						logger.debug("************* El Endoso esta en espera, confirmado false");
						estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
						respuesta.setConfirmado(false);
					} else {
						// Se confirma endoso:
						endososDAO.confirmarEndosoB(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,nsuplogi
								,cdtipsup
								,dscoment
								);
						
						estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
						respuesta.setConfirmado(true);
						logger.debug("************* El Endoso fue confirmado, confirmado true");
					}
					
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , ntramiteEmision);
					valores.put("otvalor02" , cdtipsup);
					valores.put("otvalor03" , descEndoso);
					valores.put("otvalor04" , nsuplogi);
					valores.put("otvalor05" , cdusuari);
					
					String cdtipflu    = null
							,cdflujomc = null;
					
					Map<String,String> datosFlujoEndoso = consultasDAO.recuperarDatosFlujoEndoso(cdramo,cdtipsup);
					
					cdtipflu  = datosFlujoEndoso.get("cdtipflu");
					cdflujomc = datosFlujoEndoso.get("cdflujomc");
					
					String ntramiteGenerado = mesaControlDAO.movimientoMesaControl(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.ENDOSO.getCdtiptra()
							,fechaHoy
							,null
							,null
							,null
							,fechaHoy
							,estatusTramite
							,dscoment
							,null
							,cdtipsit
							,cdusuari
							,cdsisrol
							,null //swimpres
							,cdtipflu
							,cdflujomc
							,valores
							,cdtipsup
							,null
							,null
							,null
							,false, null
							);
					
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        ntramiteGenerado,
					        estatusTramite,
					        dscoment,
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        EstatusTramite.ENDOSO_CONFIRMADO.getCodigo().equals(estatusTramite), // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  // sinGrabarDetalle
					        ));
					
					// Si fue confirmado no asignamos numero de tramite:
					respuesta.setNtramite(ntramiteGenerado);
				}
				else // SALUD CON FLUJO
				{
					paso = "Recuperando d\u00edas de endoso v\u00e1lido";
					logger.debug(paso);
					
					long numMaximoDias = (long)endososDAO.recuperarDiasDiferenciaEndosoValidos(cdramo,cdtipsup);
					
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					String ntramiteEmision = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					respuesta.setNtramiteEmision(ntramiteEmision);
					
					// Se almacena la diferencia entre la fecha actual y a fecha que tendra el endoso:
					long diferenciaFechaActualVSEndoso = fechaHoy.getTime() - fechaEndoso.getTime();
					diferenciaFechaActualVSEndoso = Math.abs(diferenciaFechaActualVSEndoso);
					// Se almacena el maximo de dias permitidos para realizar un endoso (30 dias):
					long maximoDiasPermitidos = numMaximoDias*24l*60l*60l*1000l;
					
					logger.debug("************* diferenciaFechaActualVSEndoso=" + diferenciaFechaActualVSEndoso);
					logger.debug("************* maximoDiasPermitidos         =" + maximoDiasPermitidos);
					
					String estatusTramite = null;
					if(diferenciaFechaActualVSEndoso > maximoDiasPermitidos)
					{
						logger.debug("************* El Endoso esta en espera, confirmado false");
						estatusTramite = EstatusTramite.ENDOSO_EN_ESPERA.getCodigo();
						respuesta.setConfirmado(false);
					}
					else
					{
						// Se confirma endoso:
						endososDAO.confirmarEndosoB(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
								,nmsuplem
								,nsuplogi
								,cdtipsup
								,dscoment
								);
						
						estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
						respuesta.setConfirmado(true);
						logger.debug("************* El Endoso fue confirmado, confirmado true");
					}
					
					paso = "Guardando detalle de tr\u00e1mite de endoso";
					logger.debug(paso);
					
					String comments = Utils.join("Endoso confirmado: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment);
					
					if(estatusTramite.equals(EstatusTramite.ENDOSO_EN_ESPERA.getCodigo()))
					{
						comments = Utils.join("Endoso enviado a autorizaci\u00f3n: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment);
					}
					
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        flujo.getNtramite(),
					        estatusTramite,
					        comments,
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        true,  // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  // sinGrabarDetalle
					        ));
					
					paso = "Actualizar suplemento del tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarNmsuplemTramite(flujo.getNtramite(),nmsuplem);
					
					paso = "Actualizando atributos variables de tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"MITE%EMISI"
							,ntramiteEmision
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDTIPSUP"
							,cdtipsup
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"DSTIPSUP"
							,endososDAO.obtieneDescripcionEndoso(cdtipsup)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"NSUPLOGI"
							,nsuplogi
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDUSUARI"
							,cdusuari
							,Constantes.UPDATE_MODE
							);
					
					respuesta.setNtramite(flujo.getNtramite());
				}
			}
			else// AUTOS
			{
				if(flujo == null) // AUTOS SIN FLUJO
				{
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se obtiene el numero de tramite de emision de una poliza:
					String ntramiteEmision = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					respuesta.setNtramiteEmision(ntramiteEmision);
					
					paso = "Recuperando descripci\u00f3n de endoso";
					logger.debug(paso);
					
					String descEndoso = endososDAO.obtieneDescripcionEndoso(cdtipsup); 
					
					// Se confirma endoso:
					endososDAO.confirmarEndosoB(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,nsuplogi
							,cdtipsup
							,dscoment
							);
					
					String estatusTramite = EstatusTramite.ENDOSO_CONFIRMADO.getCodigo();
					respuesta.setConfirmado(true);
					logger.debug("************* El Endoso fue confirmado, confirmado true");
					
					Map<String,String> valores = new LinkedHashMap<String,String>();
					valores.put("otvalor01" , ntramiteEmision);
					valores.put("otvalor02" , cdtipsup);
					valores.put("otvalor03" , descEndoso);
					valores.put("otvalor04" , nsuplogi);
					valores.put("otvalor05" , cdusuari);
					
					String cdtipflu    = null
							,cdflujomc = null;
					
					Map<String,String> datosFlujoEndoso = consultasDAO.recuperarDatosFlujoEndoso(cdramo,cdtipsup);
					
					cdtipflu  = datosFlujoEndoso.get("cdtipflu");
					cdflujomc = datosFlujoEndoso.get("cdflujomc");
					
					String ntramiteGenerado = mesaControlDAO.movimientoMesaControl(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,cdunieco
							,cdunieco
							,TipoTramite.ENDOSO.getCdtiptra()
							,fechaHoy
							,null
							,null
							,null
							,fechaHoy
							,estatusTramite
							,dscoment
							,null
							,cdtipsit
							,cdusuari
							,cdsisrol
							,null //swimpres
							,cdtipflu
							,cdflujomc
							,valores
							,cdtipsup
							,null
							,null
							,null
							,false, null
							);
					
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        ntramiteGenerado,
					        estatusTramite,
					        dscoment,
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        true,  // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  // sinGrabarDetalle
					        ));
					
					// Si fue confirmado no asignamos numero de tramite:
					respuesta.setNtramite(ntramiteGenerado);
				}
				else // AUTOS CON FLUJO
				{
					paso = "Recuperando tr\u00e1mite de emisi\u00f3n";
					logger.debug(paso);
					
					// Se obtiene el numero de tramite de emision de una poliza:
					String ntramiteEmision = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
					
					respuesta.setNtramiteEmision(ntramiteEmision);
					
					paso = "Confirmando endoso de flujo";
					logger.debug(paso);
					
					endososDAO.confirmarEndosoB(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsuplem
							,nsuplogi
							,cdtipsup
							,dscoment
							);
					
					respuesta.setConfirmado(true);
					
					paso = "Actualizando estatus de tr\u00e1mite de endoso";
					logger.debug(paso);
					respuesta.setRespuestaTurnado(despachadorManager.turnarTramite(
					        cdusuari,
					        cdsisrol,
					        flujo.getNtramite(),
					        EstatusTramite.ENDOSO_CONFIRMADO.getCodigo(),
					        Utils.join("Endoso confirmado: ",StringUtils.isBlank(dscoment) ? "(sin comentarios)" : dscoment),
					        null,  // cdrazrecha
					        null,  // cdusuariDes
					        null,  // cdsisrolDes
					        true,  // permisoAgente
					        false, // porEscalamiento
					        fechaHoy,
					        false  //sinGrabarDetalle
					        ));
					
					paso = "Actualizar suplemento del tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarNmsuplemTramite(flujo.getNtramite(),nmsuplem);
					
					paso = "Actualizando atributos variables de tr\u00e1mite";
					logger.debug(paso);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"MITE%EMISI"
							,ntramiteEmision
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDTIPSUP"
							,cdtipsup
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"DSTIPSUP"
							,endososDAO.obtieneDescripcionEndoso(cdtipsup)
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"NSUPLOGI"
							,nsuplogi
							,Constantes.UPDATE_MODE
							);
					
					mesaControlDAO.actualizarOtvalorTramitePorDsatribu(
							flujo.getNtramite()
							,"CDUSUARI"
							,cdusuari
							,Constantes.UPDATE_MODE
							);
					
					respuesta.setNtramite(flujo.getNtramite());
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ respuesta = " , respuesta
				,"\n@@@@@@ confirmarEndoso @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return respuesta;
	}
	
	@Override
    public void BeneficiarioVida_M(
            String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String nmsuplem) throws Exception
    {
        logger.info(
                new StringBuilder()
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .append("\n@@@@@@ BeneficiarioVida_M @@@@@@")
                .append("\n@@@@@@ cdunieco=").append(cdunieco)
                .append("\n@@@@@@ cdramo=")  .append(cdramo)
                .append("\n@@@@@@ estado=")  .append(estado)
                .append("\n@@@@@@ nmpoliza=").append(nmpoliza)
                .append("\n@@@@@@ nmsuplem=").append(nmsuplem)
                .toString()
                );
        
        endososDAO.conviertePuntoMuertoMpoliperBeneficiarioVida(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
        
        logger.info(
                new StringBuilder()
                .append("\n@@@@@@ BeneficiarioVida_M @@@@@@")
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .toString()
                );
    }
}