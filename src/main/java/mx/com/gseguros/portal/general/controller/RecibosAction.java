package mx.com.gseguros.portal.general.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import mx.com.gseguros.portal.general.service.RecibosManager;
import mx.com.gseguros.portal.general.util.TipoArchivo;
import mx.com.gseguros.utils.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RecibosAction extends PrincipalCoreAction {

	@SuppressWarnings("unused")
	private org.apache.log4j.Logger logger =org.apache.log4j.Logger.getLogger(RecibosAction.class);
	
	private static final long serialVersionUID = 1L;

	private RecibosManager recibosManager;

	private Map<String, String> params;

	private List<ReciboVO> recibos;
	
	private List<DetalleReciboVO> detallesRecibo;
	
	private List<Map<String, String>> loadList;
	
    private boolean                  success          = true;
    
    private boolean                  exito            = false;
    
    private Map<String,String>       smap1            = null;
    
    private String                   respuesta;
    
    private String                   respuestaOculta  = null;
    
    private Map<String,Item>         imap             = null;
    
    private List<Map<String,String>> slist1           = null;
    
    protected String                 contentType;
    
    private InputStream              fileInputStream;
    
    private String                   filename;
    
    private String []                arrRec;
	
	/**
	 * Action usado para la carga de la seccion de recibos, propagando parametros
	 */
	public String loadRecibos() throws Exception {
		return SUCCESS;
	}

	public String obtieneRecibos() throws Exception {
		recibos = recibosManager.obtieneRecibos(params.get("cdunieco"),
				params.get("cdramo"), params.get("nmpoliza"),
				params.get("nmsuplem"));
		return SUCCESS;
	}

	public String obtieneDetalleRecibo() throws Exception {
		detallesRecibo = recibosManager.obtieneDetallesRecibo(params.get("cdunieco"),
				params.get("cdramo"), params.get("estado"),
				params.get("nmpoliza"), params.get("nmrecibo"));
		return SUCCESS;
	}

	public String loadRecibosSISA(){
	    logger.info(
	        new StringBuilder()
	            .append("\n#############################")
	            .append("\n###### loadRecibosSISA ######")
	            .append("\n###### smap1=").append(smap1)
	            .toString());
	        
	    success = true;
	    String cdsisrol = null;
	        
	    //datos completos
	    try{
	        UserVO usuario = (UserVO)session.get("USUARIO");
            cdsisrol = usuario.getRolActivo().getClave();
            ManagerRespuestaImapVO managerResponse = recibosManager.pantallaRecibosSISA(cdsisrol);
            imap = managerResponse.getImap();
	    }
	    catch(Exception ex){
	        respuesta = Utils.manejaExcepcion(ex);
	        logger.info(
	                new StringBuilder()
	                    .append("\n###### respuesta=").append(respuesta)
	                    .append("\n###### loadRecibosSISA ######")
	                    .append("\n#############################")
	                    .toString());
//	        long timestamp  = System.currentTimeMillis();
//            success         = false;
//            respuesta       = new StringBuilder("Error al obtener atributos de pantalla #").append(timestamp).toString();
//            respuestaOculta = ex.getMessage();
//            logger.error(respuesta,ex);
	    }
	        logger.info(
	            new StringBuilder()
	                .append("\n###### slist1=").append(slist1)
	                .append("\n###### loadRecibosSISA ######")
	                .append("\n#############################")
	                .toString());
	        return SUCCESS;
	}
	
    public String obtenerDatosRecibosSISA(){
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtenerDatosRecibosSISA ######"
               ,"\n###### params=",params
               ));
        String respuesta = "";
        try{
            Utils.validate(params, "No se recibieron parametros de entrada");
            Utils.validate(params.get("cdunieco"), "No se recibio la oficina",
                           params.get("cdramo")  , "No se recibio el producto",
                           params.get("estado")  , "No se recibio el estado",
                           params.get("nmpoliza"), "No se recibio la poliza");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            loadList = recibosManager.obtenerDatosRecibosSISA(cdunieco, cdramo, estado, nmpoliza);
            respuesta = "Operacion exitosa";
        }
        catch(Exception ex){
            respuesta = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtenerDatosRecibosSISA ######"
               ,"\n###### loadList=",loadList
               ));
        return SUCCESS;
    }
	
    public String consolidarRecibos(){
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### consolidarRecibos ######"
               ,"\n###### params=",params
               ,"\n###### loadList=",loadList
               ));
        try{
            this.session=ActionContext.getContext().getSession();
            Utils.validateSession(session);
            Utils.validate(loadList,               "No se recibio la lista");
            UserVO userVO = (UserVO)session.get("USUARIO");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el ramo",
                           estado,   "No se recibio el estado",
                           nmpoliza,  "No se recibio la poliza");  
            respuesta = recibosManager.consolidarRecibos(cdunieco, cdramo, estado, nmpoliza, userVO, loadList);
            success = true;
        }
        catch(Exception ex){
            Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### consolidarRecibos ######"
               ));
        return SUCCESS;
    }
    
    public String desconsolidarRecibos(){
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### desconsolidarRecibos ######"
               ,"\n###### params=",params
               ,"\n###### loadList=",loadList
               ));
        try{
            this.session=ActionContext.getContext().getSession();
            Utils.validateSession(session);
            Utils.validate(loadList, "No se recibio la lista");
            UserVO userVO = (UserVO)session.get("USUARIO");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el ramo",
                           estado,   "No se recibio el estado",
                           nmpoliza, "No se recibio la poliza");  
            recibosManager.desconsolidarRecibos(cdunieco, cdramo, estado, nmpoliza, userVO, loadList);
            success = true;
        }
        catch(Exception ex){
            Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### desconsolidarRecibos ######"
               ));
        return SUCCESS;
    }
    
    public String obtieneDetalleReciboSISA() throws Exception {
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtieneDetalleReciboSISA ######"
               ,"\n###### params=",params));        
        try{
            Utils.validateSession(session);
            Utils.validate(params.get("cdunieco"), "No se recibio la oficina",
                    params.get("cdramo"),   "No se recibio el producto",
                    params.get("estado"),   "No se recibio el estado",
                    params.get("nmpoliza"), "No se recibio la poliza");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            String nmrecibo = params.get("nmrecibo");
            String nmfolcon = params.get("nmfolcon");
            UserVO userVO = (UserVO)session.get("USUARIO");
            detallesRecibo = recibosManager.obtieneDetallesReciboSISA(
                    cdunieco,
                    cdramo,
                    estado,
                    nmpoliza,
                    nmrecibo,
                    nmfolcon);
        }
        catch(Exception ex){
            Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtieneDetalleReciboSISA ######"
               ));
        return SUCCESS;
    }    
    
    public String procesoObtencionReporte() throws Exception{
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### procesoObtencionReporte ######"
               ,"\n###### params=",params
               ,"\n###### arrRec=",arrRec
               ));    
        try {
            Utils.validate(params,   "No se recibieron parametros");
            Utils.validate(arrRec,   "No se recibieron recibos");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el producto",
                           estado,   "No se recibio el estado",
                           nmpoliza, "No se recibio la poliza");
            contentType     = TipoArchivo.XLS.getContentType();
            filename        = "DesgloseAsegurados"+TipoArchivo.XLS.getExtension();
            fileInputStream = recibosManager.obtenerDatosReporte(cdunieco, cdramo, estado, nmpoliza, arrRec);
        }
        catch (Exception ex) {
            respuesta = Utils.manejaExcepcion(ex);
        }
        return SUCCESS;
    }
    
    public String procesoReporteRecibos() throws Exception{
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### procesoReporteRecibos ######"
               ,"\n###### params=",params
               ));    
        try {
            Utils.validate(params,   "No se recibieron parametros");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el producto",
                           estado,   "No se recibio el estado",
                           nmpoliza, "No se recibio la poliza");
            contentType     = TipoArchivo.XLS.getContentType();
            filename        = "Recibos_"+cdunieco+"_"+cdramo+"_"+nmpoliza+"_"+TipoArchivo.XLS.getExtension();
            fileInputStream = recibosManager.obtenerReporteRecibos(cdunieco, cdramo, estado, nmpoliza);
        }
        catch (Exception ex) {
            respuesta = Utils.manejaExcepcion(ex);
        }
        return SUCCESS;
    }
    
    public String obtenerBitacoraConsolidacion() throws Exception{
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtenerBitacoraConsolidacion ######"
               ,"\n###### params=",params
               ));
        try{
            Utils.validate(params,   "No se recibieron parametros");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el producto",
                           estado,   "No se recibio el estado",
                           nmpoliza, "No se recibio la poliza");        
            slist1 = recibosManager.obtenerBitacoraConsolidacion(cdunieco, cdramo, estado, nmpoliza);
            exito = true;
        }
        catch(Exception ex){
            exito = false;
            Utils.manejaExcepcion(ex);
        }
        return SUCCESS;
    }
    
    public String obtenerLigaRecibo() throws Exception{
        logger.debug(Utils.log(
                "\n###########################################"
               ,"\n###### obtenerLigaRecibo ######"
               ,"\n###### params=",params
               ));
        try{
            Utils.validate(params,   "No se recibieron parametros");
            String cdunieco = params.get("cdunieco");
            String cdramo   = params.get("cdramo");
            String estado   = params.get("estado");
            String nmpoliza = params.get("nmpoliza");
            String folio    = params.get("nmfolcon");
            Utils.validate(cdunieco, "No se recibio la oficina",
                           cdramo,   "No se recibio el producto",
                           estado,   "No se recibio el estado",
                           nmpoliza, "No se recibio la poliza",
                           folio,    "No se recibio el folio de consolidacion");        
            respuesta = recibosManager.obtenerLigaRecibo(cdunieco, cdramo, estado, nmpoliza, folio);
            exito = true;
        }
        catch(Exception ex){
            exito = false;
            Utils.manejaExcepcion(ex);
        }
        return SUCCESS;
    }
    
	// Getters and setters:
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<ReciboVO> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboVO> recibos) {
		this.recibos = recibos;
	}
	
	public List<DetalleReciboVO> getDetallesRecibo() {
		return detallesRecibo;
	}

	public void setDetallesRecibo(List<DetalleReciboVO> detallesRecibo) {
		this.detallesRecibo = detallesRecibo;
	}

	/**
	 * recibosManager setter
	 * 
	 * @param recibosManager
	 */
	public void setRecibosManager(RecibosManager recibosManager) {
		this.recibosManager = recibosManager;
	}

    public List<Map<String, String>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, String>> loadList) {
        this.loadList = loadList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public Map<String, String> getSmap1() {
        return smap1;
    }

    public void setSmap1(Map<String, String> smap1) {
        this.smap1 = smap1;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuestaOculta() {
        return respuestaOculta;
    }

    public void setRespuestaOculta(String respuestaOculta) {
        this.respuestaOculta = respuestaOculta;
    }

    public Map<String, Item> getImap() {
        return imap;
    }

    public void setImap(Map<String, Item> imap) {
        this.imap = imap;
    }

    public List<Map<String, String>> getSlist1() {
        return slist1;
    }

    public void setSlist1(List<Map<String, String>> slist1) {
        this.slist1 = slist1;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String[] getArrRec() {
        return arrRec;
    }

    public void setArrRec(String[] arrRec) {
        this.arrRec = arrRec;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}