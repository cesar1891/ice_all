package mx.com.gseguros.portal.general.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.dao.RecibosDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import mx.com.gseguros.portal.general.service.RecibosManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.renovacion.service.impl.RenovacionManagerImpl;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.impl.AutosSIGSDAOImpl.EjecutaVidaPorRecibo;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Recibo;
import mx.com.gseguros.ws.ice2sigs.client.model.ReciboWrapper;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Operacion;
import mx.com.gseguros.ws.ice2sigs.service.impl.Ice2sigsServiceImpl;
import mx.com.gseguros.ws.model.WrapperResultadosWS;

public class RecibosManagerImpl implements RecibosManager {

    private static final Logger           logger       = Logger.getLogger(RenovacionManagerImpl.class);
	
    private RecibosDAO   recibosDAO;
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private Ice2sigsService ice2sigsService;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Value("${recibos.impresion.consolidado.url}")
	private String urlImpresionRecibos;
	
	@Autowired
	private transient KernelManagerSustituto kernelManager;
	
	@Override
	public List<ReciboVO> obtieneRecibos(String cdunieco, String cdramo, String nmpoliza, String nmsuplem) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i",   cdramo);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		return recibosDAO.obtieneRecibos(params);
	}

	@Override
	public List<DetalleReciboVO> obtieneDetallesRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdUnieco_i", cdunieco);
		params.put("pv_cdRamo_i", cdramo);
		params.put("pv_Estado_i", estado);
		params.put("pv_NmPoliza_i", nmpoliza);
		params.put("pv_nmRecibo_i", nmrecibo);
		return recibosDAO.obtieneDetalleRecibo(params);
	}
	
	@Override
    public ManagerRespuestaImapVO pantallaRecibosSISA(String cdsisrol) throws Exception{
        logger.info(
                new StringBuilder()
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .append("\n@@@@@@ pantallaRenovacionIndividual @@@@@@")
                .append("\n@@@@@@ cdsisrol=").append(cdsisrol)
                .toString());
        ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
        String paso = "";
        //obtener componentes
        try{
            paso = "antes de obtener grid de recibos";
            Map<String,Item> imap = new HashMap<String,Item>();
            paso = "despues de declarar imap";
            List<ComponenteVO> itemsRecibo = pantallasDAO.obtenerComponentes(
                    null,null,null,null,null,cdsisrol,"CONSULTA_RECIBOS_SISA","MODELO_RECIBOS",null);          
            GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
            gc.generaComponentes(itemsRecibo, true, true, false, true, true, false);          
            imap.put("itemsReciboFields"  , gc.getFields());
            imap.put("itemsReciboColumns" , gc.getColumns());
            
            List<ComponenteVO> itemsDetalleRecibo = pantallasDAO.obtenerComponentes(
                    null,null,null,null,null,cdsisrol,"CONSULTA_RECIBOS_SISA","DETALLE_RECIBO",null);          
            gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
            gc.generaComponentes(itemsDetalleRecibo, true, true, false, true, true, false);          
            imap.put("itemsDetalleFields"  , gc.getFields());
            imap.put("itemsDetalleColumns" , gc.getColumns());
            
            List<ComponenteVO> itemsBitacoraRecibo = pantallasDAO.obtenerComponentes(
                    null,null,null,null,null,cdsisrol,"CONSULTA_RECIBOS_SISA","MODELO_BITACONS",null);          
            gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
            gc.generaComponentes(itemsBitacoraRecibo, true, true, false, true, true, false);          
            imap.put("itemsBitacoraFields"  , gc.getFields());
            imap.put("itemsBitacoraColumns" , gc.getColumns());            
            
            resp.setImap(imap);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        logger.info(
            new StringBuilder()
                .append("\n@@@@@@ ").append(resp)
                .append("\n@@@@@@ pantallaRenovacionIndividual @@@@@@")
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .toString());
        return resp;
	}

    @Override
    public List<Map<String, String>> obtenerDatosRecibosSISA(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        String paso = "";
        List<Map<String, String>> lista       = new ArrayList<Map<String,String>>();
        List<Map<String, String>> infoRecibos = new ArrayList<Map<String,String>>();
        try{
            paso  = "Antes de obtener datos de recibos";
            lista = recibosDAO.obtenerDatosRecibosSISA(cdunieco, cdramo, estado, nmpoliza);
            paso  = "Despues de obtener datos de recibos";
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return lista;
    }
	
    @Override
    public String consolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String, String>> lista) throws Exception{
        logger.debug(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ consolidarRecibos @@@@@@"
               ,"\n@@@@@@ lista    = " , lista
               ));
        String folio = "";    
        String paso  = "";        
        try{
            String usuario = user.getUser().toString();
            folio = recibosDAO.consolidarRecibos(cdunieco, cdramo, estado, nmpoliza, usuario, lista);
            actualizarFolioSIGS(cdunieco, cdramo, estado, nmpoliza, folio, user, lista);            
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        logger.debug(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ consolidarRecibos @@@@@@"
               ,"\n@@@@@@ folio    = " , folio
               ));
        return folio;
    }
	
    public void desconsolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String, String>> lista) throws Exception{
        logger.debug(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ desconsolidarRecibos @@@@@@"
               ,"\n@@@@@@ lista = " , lista
               ));
        String paso = "";
        try{
            paso = "Antes de desconsolidar recibos";
            String usuario = user.getUser().toString();
            recibosDAO.desconsolidarRecibos(cdunieco, cdramo, estado, nmpoliza, usuario, lista.get(0).get("folio"));
            actualizarReciboSIGS(cdunieco, cdramo, estado, nmpoliza, user, lista);
            recibosDAO.borrarDocumentoReciboConsolidado(cdunieco, cdramo, estado, nmpoliza, lista.get(0).get("folio"));
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        logger.debug(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ desconsolidarRecibos @@@@@@"
               ));
    }
    
    @Override
    public List<DetalleReciboVO> obtieneDetallesReciboSISA(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo, String nmfolcon) throws Exception {
        String paso = "";
        List<DetalleReciboVO> detalle = new ArrayList<DetalleReciboVO>();
        try{
            detalle = recibosDAO.obtieneDetalleReciboSISA(cdunieco, cdramo, estado, nmpoliza, nmrecibo, nmfolcon);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return detalle;
    }
    
    @Override
    public void actualizarFolioSIGS(String cdunieco, String cdramo, String estado, String nmpoliza, String rmdbRn, UserVO user, List<Map<String, String>> lista) throws Exception{
        String paso = "";
        List<Map<String, String>> infoRecibos = new ArrayList<Map<String,String>>();
        try{
            paso = "Obteniendo informacion de recibos";
            String nmrecibo = null;
            String nmsuplem = null;
            String ntramite = null;
            String nmsolici = null;
            String nmimpres = null;
            boolean exitoso = false;
            for(Map<String, String> map : lista){
                nmrecibo = map.get("nmrecibo");
                nmsuplem = map.get("nmsuplem");
                ntramite = map.get("ntramite");
                nmsolici = map.get("nmsolici");
                nmimpres = map.get("nmimpres");
                if(ntramite != null){
                    paso = "Antes de obtener info para SIGS";
                    infoRecibos = recibosDAO.obtenerInfoRecibos(cdunieco, cdramo, estado, nmpoliza, nmrecibo, nmsuplem);
                    logger.debug("Operacion "+infoRecibos.size());
                    paso = "logueando info recibos";
                    logger.debug(infoRecibos);
                    paso = "Antes de recorrer info recibos";
                    Utils.log(paso);
                    for(int i = 0; i < infoRecibos.size(); i++){
                        paso = "Antes de cast ReciboWrapper";
                        Utils.log(paso);
                        ReciboWrapper reciboWrap = (ReciboWrapper) infoRecibos.get(i);
                        reciboWrap.setOperacion(String.valueOf(Operacion.ACTUALIZA));
                        reciboWrap.getRecibo().setRmdbRn(Integer.parseInt(rmdbRn));
                        logger.debug("Operacion "+reciboWrap.getOperacion());
                        logger.debug("rmdbrn "+reciboWrap.getRecibo().getRmdbRn());
                        Utils.log("Operacion",reciboWrap.getOperacion());
                        Utils.log("rmdbrn",reciboWrap.getRecibo().getRmdbRn());                    
                        ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdunieco, ntramite, false, user, reciboWrap);
                        exitoso = true;                    
                    }
                }
                else{
                    throw new Exception("No se pudo actualizar folio de sigs");
                }
            }
            if(exitoso){
                generaDocumentoReciboConsolidado(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsolici, ntramite, nmimpres, rmdbRn);
            }
            paso = "Actualizando informacion de recibos en SIGS";
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
    }

    @Override
    public void actualizarReciboSIGS(String cdunieco, String cdramo, String estado, String nmpoliza, UserVO user, List<Map<String, String>> lista) throws Exception{
        String paso = "";
        List<Map<String, String>> infoRecibos = new ArrayList<Map<String,String>>();
        try{
            paso = "Obteniendo informacion de recibos";
            for(Map<String, String> map : lista){
                String nmrecibo = map.get("nmrecibo");
                String nmsuplem = map.get("nmsuplem");
                String ntramite = map.get("ntramite");
                paso = "Antes de obtener info para SIGS";
                infoRecibos = recibosDAO.obtenerInfoRecibos(cdunieco, cdramo, estado, nmpoliza, nmrecibo, nmsuplem);
                logger.debug("Operacion "+infoRecibos.size());
                paso = "logueando info recibos";
                logger.debug(infoRecibos);
                paso = "Antes de recorrer info recibos";
                Utils.log(paso);
                for(int i = 0; i < infoRecibos.size(); i++){
                    paso = "Antes de cast ReciboWrapper";
                    Utils.log(paso);
                    ReciboWrapper reciboWrap = (ReciboWrapper) infoRecibos.get(i);
                    reciboWrap.setOperacion(String.valueOf(Operacion.ACTUALIZA));
                    reciboWrap.getRecibo().setRmdbRn(Integer.parseInt(nmrecibo));
                    logger.debug("Operacion "+reciboWrap.getOperacion());
                    logger.debug("rmdbrn "+reciboWrap.getRecibo().getRmdbRn());
                    Utils.log("Operacion",reciboWrap.getOperacion());
                    Utils.log("rmdbrn",reciboWrap.getRecibo().getRmdbRn());                    
                    ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, estado, nmpoliza, nmsuplem, cdunieco, ntramite, false, user, reciboWrap);
                }
            }
            paso = "Actualizando informacion de recibos en SIGS";
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
    }
    
    @Override
    public InputStream obtenerDatosReporte(String cdunieco, String cdramo, String estado, String nmpoliza, String[] lista) throws Exception {
        String paso = "";
        InputStream inputStr = null;
        try{
            paso = "Generando reporte";
            inputStr = recibosDAO.obtenerReporte(cdunieco, cdramo, estado, nmpoliza, lista);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return inputStr;
    }
    
    @Override
    public InputStream obtenerReporteRecibos(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
        String paso = "";
        InputStream inputStr = null;
        try{
            paso = "Generando reporte";
            inputStr = recibosDAO.obtenerReporteRecibos(cdunieco, cdramo, estado, nmpoliza);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return inputStr;
    }
    
    @Override
    public List<Map<String, String>> obtenerBitacoraConsolidacion(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        List<Map<String, String>> lista = new ArrayList<Map<String,String>>();
        String paso = "";
        try{
            paso = "Obteniendo bitacora de consolidacion";
            lista = recibosDAO.obtenerBitacoraConsolidacion(cdunieco, cdramo, estado, nmpoliza);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return lista;
    }

    @Override
    public void generaDocumentoReciboConsolidado(
            String cdunieco, 
            String cdramo, 
            String estado, 
            String nmpoliza, 
            String nmsuplem, 
            String nmsolici, 
            String ntramite,
            String nmimpres,
            String folio) throws Exception{
        HashMap<String, Object> params = new HashMap<String, Object>();        
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i",   cdramo);
        params.put("pv_estado_i",   estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("pv_nmsuplem_i", nmsuplem);
        params.put("pv_ntramite_i", ntramite);
        String cdtipsit   = kernelManager.obtenCdtipsit(params);
        params.put("pv_cdtipsit_i", cdtipsit);    
        String cdtipsitGS = kernelManager.obtenCdtipsitGS(params);        
        String parametros = "?"+cdunieco+","+cdtipsitGS+","+nmpoliza+","+nmimpres+","+folio;
        logger.debug("URL Generada para Recibo: "+ urlImpresionRecibos + parametros);
        String nmsuplemEmi = obtenerSuplementoEmision(cdunieco, cdramo, estado, nmpoliza);
        String ntramiteEmi = obtenerTramiteEmision(cdunieco, cdramo, estado, nmpoliza);
        mesaControlDAO.guardarDocumento(
                cdunieco,
                cdramo,
                estado,
                nmpoliza,
                nmsuplemEmi,
                new Date(),
                urlImpresionRecibos + parametros,
                "ReciboConsolidado "+folio,
                nmsolici,
                ntramiteEmi,
                "1",
                Constantes.SI,
                null,
                TipoTramite.POLIZA_NUEVA.getCdtiptra(),
                "0",
                Documento.RECIBO, null, null, false
                );
    }
    
    @Override
    public String obtenerLigaRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String folio) throws Exception{
        String paso = "";
        String liga = "";
        try{
            paso = "Antes de obtener liga de recibo consolidado";
            liga = recibosDAO.obtenerLigaRecibo(cdunieco, cdramo, estado, nmpoliza, folio);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return liga;
    }
    
    @Override
    public String obtenerSuplementoEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        String paso = "";
        String nmsuplem = "";
        try{
            paso = "Antes de obtener suplemento de emision";
            nmsuplem = recibosDAO.obtenerSuplementoEmision(cdunieco, cdramo, estado, nmpoliza);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return nmsuplem;
    }
    
    @Override
    public String obtenerTramiteEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        String paso = "";
        String ntramite = "";
        try{
            paso = "Antes de obtener tramite de emision";
            ntramite = recibosDAO.obtenerTramiteEmision(cdunieco, cdramo, estado, nmpoliza);
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return ntramite;
    }
    
    public void setRecibosDAO(RecibosDAO recibosDAO) {
        this.recibosDAO = recibosDAO;
    }    

}