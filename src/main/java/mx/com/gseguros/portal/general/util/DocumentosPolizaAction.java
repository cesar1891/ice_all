package mx.com.gseguros.portal.general.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.DocumentosUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

public class DocumentosPolizaAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 5866297387639852014L;

	private static final Logger logger = Logger.getLogger(DocumentosPolizaAction.class);
	
	private InputStream fileInputStream;
	private String path;
	private String subfolder;
	private String filename;
	
	private Map<String,String>smap1;
	private List<Map<String,String>>slist1;

	protected boolean success;
	protected String contentType;
	
	private String url;
	
	private String  respuesta;
	private String  respuestaOculta = null;
	private boolean exito           = false;
	private int reintentoRegeneraRepore = 1;// variable para solo reintentar la regeneracion de reporte una vez 
	
	private ConsultasManager consultasManager;
	
	private Map<String,Item> items;
	
	private Map<String,Item> imap;
	
	private Map<String, String> params;
	
	@Autowired
	private PantallasManager pantallasManager;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
    @Autowired
    private SiniestrosManager siniestrosManager;
    
    @Autowired
    private ConsultasPolizaManager consultasPolizaManager;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
    
    @Value("${pass.servidor.reports}")
    private String passServidorReports;
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
    @Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
    
	/**
	 * Metodo para la descarga de los archivos de los Movimientos en los casos
	 * de BO
	 * 
	 * @return
	 */
	public String descargaDocumento() {
		
		logger.debug("Parametros de entrada para la descarga del archivo");
		logger.debug("path: " + path);
		logger.debug("subfolder: " + subfolder);
		logger.debug("filename: " + filename);
		logger.debug("contentType: " + contentType);
		logger.debug("url: " + url);
		
		try {
			if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(contentType)) {
				fileInputStream = HttpUtil.obtenInputStream(url);
			} else {
				// Se asigna el fileInputStream:
				String rutaArchivo = generaRutaArchivo(path, subfolder, filename);
				logger.info("Se va a descargar el archivo: " + rutaArchivo);
				fileInputStream = new FileInputStream(new File(rutaArchivo));
				
				// Se asigna el contentType:
				contentType = obtieneContentType(filename);
			}
		} catch (Exception e) {
			boolean reintentoRealizado = false; 
			if(fileInputStream == null){
				reintentoRealizado = reintentaRegeneraReporte();
			}
			
			// para mantener el flujo como estaba y si no se realiza el reintento pone el mensaje de error al Action
			if(!reintentoRealizado){
				addActionError(e.getMessage());
			}
		}
		
		if(fileInputStream == null){
			reintentaRegeneraReporte();
		}
		
		success = true;
		return SUCCESS;
	}
	
	
    public static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);

        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;  
        }

        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());

        return newUri;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(appendUri("http://example.com", "name=John"));
        System.out.println(appendUri("http://example.com#fragment", "name=John"));
        System.out.println(appendUri("http://example.com?email=john.doe@email.com", "name=John"));
        System.out.println(appendUri("http://example.com?email=john.doe@email.com#fragment", "name=John"));
    }
	
	public String descargaReporte() {
        
        logger.debug("Parametros de entrada para la descarga del reporte");
        logger.debug("params: " + params);
//        logger.debug("filename: " + filename);
//        logger.debug("contentType: " + contentType);
//        logger.debug("url: " + url);
        
        String getParams = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String separador = StringUtils.isBlank(getParams) ? "?" : "&";
            getParams += new StringBuilder(separador).append(entry.getKey()).append("=").append(entry.getValue()).toString();
        }
        
        url = rutaServidorReports + getParams;
        
        //"destype=cache&desformat=PDF&userid=ice/ice@10.1.1.170:1521/GSEGUROS&ACCESSIBLE=YES&report=CONTRA_RECIBO_RECLAMACION.rdf&paramform=no";
        
        try {
            if(StringUtils.isNotBlank(url)) {
                fileInputStream = HttpUtil.obtenInputStream(url);
            }
        } catch (Exception e) {
            addActionError(e.getMessage());
        }
        
        success = true;
        return SUCCESS;
    }
	
	/**
	 * Regenera un reporte que se ha generado con errores 
	 */
	public String regeneraReporte() {
		
		logger.debug("**********  Parametros de entrada para regenerar reporte ***********");
		logger.debug("smap1: " + smap1);
		
		try {
		    if(consultasManager.esProductoSalud(smap1.get("pv_cdramo_i"))){
		        success = mesaControlManager.regeneraReporte(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), 
	                    smap1.get("pv_nmpoliza_i"), smap1.get("pv_nmsuplem_i"), smap1.get("pv_cddocume_i"), smap1.get("pv_nmsituac_i"),
	                    smap1.get("pv_nmcertif_i"), smap1.get("pv_cdmoddoc_i"));
		    } else {
		        String paso = null;
	            
	            String cdunieco      = smap1.get("pv_cdunieco_i"), 
	                   cdramo        = smap1.get("pv_cdramo_i"), 
	                   estado        = smap1.get("pv_estado_i"),
	                   nmpoliza      = smap1.get("pv_nmpoliza_i"), 
	                   nmsuplem      = "999999999999999999",//smap1.get("pv_nmsuplem_i"),
	                   nmtramite     = smap1.get("pv_nmtramite_i"),
	                   nombreReporte = smap1.get("pv_cddocume_i").substring(0 , smap1.get("pv_cddocume_i").length()-3);
	                       
	            paso = "Generando URL para SOL_VIDA_AUTO.pdf";
	            String rutaReports    = rutaServidorReports;
	            String passReports    = passServidorReports;
	            String rutaDocumentos = rutaDocumentosPoliza;
	            String url = rutaReports
	                    + "?destype=cache"
	                    + "&desformat=PDF"
	                    + "&userid="+passReports
	                    + "&report="+nombreReporte+"rdf"
	                    + "&paramform=no"
	                    + "&ACCESSIBLE=YES" //parametro que habilita salida en PDF
	                    + "&p_unieco="+cdunieco
	                    + "&p_ramo="+cdramo
	                    + "&p_estado="+estado
	                    + "&p_poliza="+nmpoliza
	                    + "&p_suplem="+nmsuplem
	                    + "&desname="+rutaDocumentos+"/"+nmtramite+"/"+nombreReporte+"pdf";
	            
	            paso = "Guardando PDF de Vista Previa de Autos en Temporal";
	            logger.debug(paso);
	            HttpUtil.generaArchivo(url,rutaDocumentos+"/"+nmtramite+"/"+nombreReporte+"pdf");
	            success = true;
		        
		    }
			
			
		} catch (Exception e) {
			logger.error("Error al regenerar el reporte "+ smap1, e);
			addActionError(e.getMessage());
			success = false;
		}

		return SUCCESS;
	}

	/**
	 * Regenera documentos desde consulta de polizas a nivel suplemento  
	 */
	public String regeneraDocumentosEndoso() {
		
		logger.debug("**********  Parametros de entrada para regenerar documentos ***********");
		logger.debug("smap1: " + smap1);
		
		try {
			success = mesaControlManager.regeneraDocumentosEndoso(smap1.get("cdunieco"), smap1.get("cdramo"), smap1.get("estado"), 
					smap1.get("nmpoliza"), smap1.get("nmsuplem"));
			
		} catch (Exception e) {
			logger.error("Error al regenerar el reporte "+ smap1, e);
			success = false;
			respuesta = Utils.manejaExcepcion(e);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Reintenta la regeneracion de un reporte regresa false si no hace el reintento. (se maneja un solo reintento para evitar recursividad)
	 * @return
	 */
	private boolean reintentaRegeneraReporte(){
		
		if(reintentoRegeneraRepore <= 0) return false; 
		
		reintentoRegeneraRepore = reintentoRegeneraRepore - 1;
		
		try{
			
			if(smap1 == null){
				logger.warn("Error sin Impacto, no hay parametros a regenerar reporte.");
			}
			
			logger.debug("<<<<<>>>>>  Volviendo a regenerar reporte...");
			regeneraReporte();
			
			logger.debug("<<<<<>>>>>  Volviendo a descargar reporte...");
			descargaDocumento();
		}catch(Exception ex){
			logger.error("Error al reintentar reporte", ex);
		}
		
		return true;
	}
	
	public String ventanaDocumentosPoliza()
	{
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### ventanaDocumentosPoliza ######"
				,"\n###### smap1=",smap1
				));
		logger.debug("smap1: "+smap1);
		if(smap1==null)
		{
			smap1=new HashMap<String,String>();
		}
		if(!smap1.containsKey("cdtiptra"))
		{
			smap1.put("cdtiptra","1");
		}
		
		smap1.put("random", String.format("%.0f", Math.random()*10000d));
		
		if(smap1.containsKey("ntramite")
				&&"1".equals(smap1.get("cdtiptra"))
				&&session!=null
				&&session.get("USUARIO")!=null
				&&!smap1.containsKey("readOnly")
				)
		{
			UserVO usuario = (UserVO)session.get("USUARIO");
			boolean bloqueoPermisoVentana = false;
			try
			{
				bloqueoPermisoVentana = consultasManager.validarVentanaDocumentosBloqueada(
					smap1.get("ntramite")
					,smap1.get("cdtiptra")
					,usuario.getUser()
					,usuario.getRolActivo().getClave()
					);
			}
			catch(Exception ex)
			{
				logger.error("error al validar ventana de documentos bloqueada",ex);
			}
			
			if(bloqueoPermisoVentana&&false)
			{
				smap1.put("readOnly" , "");
			}
		}
		
		if(smap1.containsKey("ntramite")){
		    try {
		        String ntramite = smap1.get("ntramite");
	            List<MesaControlVO> listaMesaControl;
	            listaMesaControl = siniestrosManager.getConsultaListaMesaControl(ntramite);
	            if(TipoEndoso.RENOVACION.getCdTipSup().toString().equalsIgnoreCase(listaMesaControl.get(0).getCdtipsup())){
	                PolizaAseguradoVO polizaAseguradoVO = new PolizaAseguradoVO();
                    polizaAseguradoVO.setCdunieco(listaMesaControl.get(0).getCduniecomc());
                    polizaAseguradoVO.setCdramo(listaMesaControl.get(0).getCdramomc());
                    polizaAseguradoVO.setEstado(listaMesaControl.get(0).getEstadomc());
                    polizaAseguradoVO.setNmpoliza(listaMesaControl.get(0).getNmsolicimc());
                    List<PolizaDTO> lista = consultasPolizaManager.obtieneDatosPoliza(polizaAseguradoVO);
                    if (lista != null && !lista.isEmpty()) {
                        consultasManager.copiarArchivosRenovacionColectivo(listaMesaControl.get(0).getCduniecomc(),listaMesaControl.get(0).getCdramomc(),"M",
                                        Integer.parseInt(lista.get(0).getNmpolant().substring(7,13))+"",ntramite, this.rutaDocumentosPoliza);
                    } 
	            }
	            
            } catch (Exception e) {
                logger.error("error al validar ventana de documentos bloqueada",e);
            }
           
		}
		
		
		String proceso = smap1.get("lista");
		if(StringUtils.isNotBlank(proceso))
		{
			items = new HashMap<String,Item>();
			
			List<ComponenteVO> comboDocs = pantallasManager.recuperarComboDocs(proceso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			try
			{
				gc.generaComponentes(comboDocs, true, false, true, false, false, false);
				items.put("comboDocs",gc.getItems());
			}
			catch(Exception ex)
			{
				logger.error(Utils.join("Error al generar combo de docs #",System.currentTimeMillis()),ex);
			}
			
		}

		logger.debug(Utils.log(
				 "\n###### ventanaDocumentosPoliza ######",smap1
				,"\n#####################################"
				));
		return SUCCESS;
	}
	
	
	public String fusionarDocumentos()
	{
		logger.info(
				new StringBuilder()
				.append("\n################################")
				.append("\n###### fusionarDocumentos ######")
				.append("\n###### smap1=").append(smap1)
				.toString()
				);
		
		exito   = true;
		success = true;
		
		String archivosAux = null;
		String ntramite    = null;
		
		//datos completos
		try
		{
			archivosAux = smap1.get("lista");
			ntramite    = smap1.get("ntramite");
			if(StringUtils.isBlank(archivosAux))
			{
				throw new Exception("No hay lista de archivos");
			}
			if(StringUtils.isBlank(ntramite))
			{
				throw new Exception("No hay tramite");
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			respuesta       = new StringBuilder("No se recibieron los datos necesarios #").append(timestamp).toString();
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		if(exito)
		{
			try
			{
				String[]archivos = archivosAux.split("#");
				List<File>files  = new ArrayList<File>();
				for(String iArchivo:archivos)
				{
					File file = new File(
							new StringBuilder().
							append(this.rutaDocumentosPoliza)
							.append(Constantes.SEPARADOR_ARCHIVO).append(ntramite)
							.append(Constantes.SEPARADOR_ARCHIVO).append(iArchivo)
							.toString()
							);
					logger.debug(new StringBuilder().append("archivo iterado=").append(file).toString());
					files.add(file);
				}
		
				File fusionado = DocumentosUtils.fusionarDocumentosPDF(files,new File(
						new StringBuilder()
						.append(this.rutaDocumentosTemporal).append(Constantes.SEPARADOR_ARCHIVO)
						.append(System.currentTimeMillis()).append("_fusion_").append(ntramite).append(".pdf")
						.toString()
						));
				
				if(fusionado==null || !fusionado.exists())
				{
					throw new Exception("El archivo no fue creado");
				}
				
				fileInputStream=new FileInputStream(fusionado);
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = new StringBuilder().append("Error al crear el archivo #").append(timestamp).toString();
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		logger.info(
				new StringBuilder()
				.append("\n###### fusionarDocumentos ######")
				.append("\n################################")
				.toString()
				);
		
		String result = SUCCESS;
		if(!exito)
		{
			result = ERROR;
		}
		return result;
	}

	
	/**
	 * Genera la ruta del archivo a descargar en base a los parametros recibidos
	 * 
	 * @param ruta Ruta a utilizar, si es null se usar� una ruta default
	 * @param subcarpeta Subcarpeta del archivo, si es null se omite
	 * @param filename Nombre del archivo a descargar
	 * @return Ruta absoluta del archivo a descargar 
	 */
	private String generaRutaArchivo(String ruta, String subcarpeta, String filename) {
		
		StringBuilder sbRutaArchivo = new StringBuilder();
		// Agregamos la ruta:
		sbRutaArchivo.append(StringUtils.isNotBlank(ruta) ? ruta : this.rutaDocumentosPoliza);
		sbRutaArchivo.append(Constantes.SEPARADOR_ARCHIVO);
		// Agregamos la subcarpeta si existe:
		if(StringUtils.isNotBlank(subcarpeta)) {
			sbRutaArchivo.append(subcarpeta).append(Constantes.SEPARADOR_ARCHIVO);
		}
		sbRutaArchivo.append(filename);
		
		return sbRutaArchivo.toString();
	}
	
	
	/**
	 * Obtiene el contentType a partir del nombre de un archivo
	 * @param filename Nombre del archivo
	 * @return contentType del archivo, o uno contentType por default
	 */
	private String obtieneContentType(String filename) {

		String contentType = null;
		String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		fileType = fileType.trim();
		
		// Se asigna el contentType asociado al tipo de archivo, si no existe le asignamos uno por default:
		for (TipoArchivo tipoArch : TipoArchivo.values()) {
			if(tipoArch.toString().equalsIgnoreCase(fileType)) {
				contentType = tipoArch.getContentType();
				break;
			}
	    }
		if(contentType == null) {
			contentType = TipoArchivo.DEFAULT.getContentType();
		}
		return contentType;
	}
	
	public String iniciarPantallaTrafudoc(){
	    logger.info(
                new StringBuilder()
                .append("\n###### iniciarPantallaTrafudoc ######")
                .append("\n################################")
                .toString()
                );
	    String respuesta = "";
	    String cdsisrol  = "";
	    try{
	        UserVO usuario = (UserVO)session.get("USUARIO");
            if(usuario == null){
                throw new ApplicationException("No hay usuario en sesion");
            }
            cdsisrol = usuario.getRolActivo().getClave();
            ManagerRespuestaImapVO managerResponse = consultasManager.pantallaTrafudoc(cdsisrol);
            exito           = managerResponse.isExito();
            respuesta       = managerResponse.getRespuesta();
            respuestaOculta = managerResponse.getRespuestaOculta();
            if(exito){
                setImap(managerResponse.getImap());
            }
	    }
	    catch(Exception ex){
	        respuesta = Utils.manejaExcepcion(ex);
	    }
	    logger.info(
                new StringBuilder()
                .append("\n###### iniciarPantallaTrafudoc ######")
                .append("\n################################")
                .toString()
                );
	    return SUCCESS;
	}
	
	public String obtenerCursorTrafudoc(){
	    logger.info(
                new StringBuilder()
                .append("\n###### obtenerCursorTrafudoc ######")
                .append("\n###### params = ").append(params)
                .append("\n################################")
                .toString()
                );
	    String respuesta = "";
	    try{
	        String cdfunci  = params.get("cdfunci");
	        String cdramo   = params.get("cdramo");
	        String cdtipsit = params.get("cdtipsit");
	        slist1 = consultasManager.obtenerCursorTrafudoc(cdfunci, cdramo, cdtipsit);
	        success = true;
	        respuesta = "Operacion realizada con exito";
	    }
	    catch(Exception ex){
	        respuesta = Utils.manejaExcepcion(ex);
	    }
	    logger.info(
                new StringBuilder()
                .append("\n###### obtenerCursorTrafudoc ######")
                .append("\n###### slist1").append(slist1)
                .append("\n################################")
                .toString()
                );
	    return SUCCESS;
	}
	
	public String documentosXFamilia(){
	    logger.debug(
                new StringBuilder()
                .append("\n##################################")
                .append("\n###### documentosXFamilia ######")
                .append("\n###### smap1=").append(smap1)
                .toString()
                );
        
        success = true;
        exito   = true;
        
       
        try
        {
            UserVO usuario = (UserVO)session.get("USUARIO");
            String pv_cdunieco_i  = smap1.get("cdunieco");
            String pv_cdramo_i    = smap1.get("cdramo");
            String pv_estado_i    = smap1.get("estado");
            String pv_nmpoliza_i  = smap1.get("nmpoliza"); 
            String pv_nmsuplem_i  = smap1.get("nmsuplem");
            String pv_cdusuari    = usuario.getUser();
            
            Utils.validate( pv_cdunieco_i    ,"No se recibi\u00F3 pv_cdunieco_i ",
                            pv_cdramo_i      ,"No se recibi\u00F3 pv_cdramo_i   ",
                            pv_estado_i      ,"No se recibi\u00F3 pv_estado_i   ",
                            pv_nmpoliza_i    ,"No se recibi\u00F3 pv_nmpoliza_i ",
                            pv_nmsuplem_i    ,"No se recibi\u00F3 pv_nmsuplem_i ",
                            pv_cdusuari      ,"No se recibi\u00F3 pv_cdusuari   ");
            
           respuesta = consultasManager.documentosXFamilia(pv_cdunieco_i,
                                                pv_cdramo_i,
                                                pv_estado_i, 
                                                pv_nmpoliza_i, 
                                                pv_nmsuplem_i, 
                                                pv_cdusuari);
           smap1.put("userEmail", usuario.getEmail());
            
            
            
            
        }
        catch(Exception ex)
        {
            long timestamp  = System.currentTimeMillis();
            exito           = false;
            respuesta       = "Datos incompletos #"+timestamp;
            respuestaOculta = ex.getMessage();
            logger.error(respuesta,ex);
        }
        
        
        
        logger.debug(
                new StringBuilder()
                .append("\n###### smap1=").append(smap1)
                .append("\n###### documentosXFamilia   ######")
                .append("\n##################################")
                .toString()
                );
        return SUCCESS;
	}
	
	public String ejecutaFusionFam(){
        logger.debug(
                new StringBuilder()
                .append("\n##################################")
                .append("\n###### ejecutaFusionFam ######")
                .append("\n###### smap1=").append(smap1)
                .toString()
                );
        
        success = true;
        exito   = true;
        
       
        try
        {
            UserVO usuario = (UserVO)session.get("USUARIO");
            String pv_cdunieco_i  = smap1.get("cdunieco");
            String pv_cdramo_i    = smap1.get("cdramo");
            String pv_estado_i    = smap1.get("estado");
            String pv_nmpoliza_i  = smap1.get("nmpoliza"); 
            String pv_nmsuplem_i  = smap1.get("nmsuplem");
            String pv_tipoMov_i   = smap1.get("tipoMov");
            String pv_cdtiptra_i  = smap1.get("cdTipTra");
            String pv_nmsituac_i  = smap1.get("nmsituac");
            
            Utils.validate( pv_cdunieco_i    ,"No se recibi\u00F3 pv_cdunieco_i ",
                            pv_cdramo_i      ,"No se recibi\u00F3 pv_cdramo_i   ",
                            pv_estado_i      ,"No se recibi\u00F3 pv_estado_i   ",
                            pv_nmpoliza_i    ,"No se recibi\u00F3 pv_nmpoliza_i ",
                            pv_nmsuplem_i    ,"No se recibi\u00F3 pv_nmsuplem_i ");
            
            usuario.setEmail(smap1.get("email"));
            
            consultasManager.ejecutaFusionFam  ( pv_cdunieco_i,
                                                            pv_cdramo_i,
                                                            pv_estado_i, 
                                                            pv_nmpoliza_i, 
                                                            pv_nmsuplem_i,
                                                            pv_tipoMov_i,
                                                            pv_cdtiptra_i,
                                                            usuario);
            
            
            
            
        }
        catch(Exception ex)
        {
            long timestamp  = System.currentTimeMillis();
            exito           = false;
            respuesta       = "Datos incompletos #"+timestamp;
            respuestaOculta = ex.getMessage();
            logger.error(respuesta,ex);
        }
        
        
        
        logger.debug(
                new StringBuilder()
                .append("\n###### smap1=").append(smap1)
                .append("\n###### ejecutaFusionFam     ######")
                .append("\n##################################")
                .toString()
                );
        return SUCCESS;
    }
	 
	/**
	 * Crea una carpeta en el servidor de aplicaciones
	 * 
	 * @param path ruta donde se desea crear la carpeta 
	 * @return 
	 */
	public void creaCarpeta(){
		try {
	        success = true;	        
	        Utils.validate(path, "No se recibio el parametro path");
	        logger.debug("path recibido: "+path);	
	        File   carpeta        = new File(path);
	        boolean creado = false;
			
			if(!carpeta.exists())
            {
            	logger.debug("No existe la carpeta: "+carpeta);
            	creado = carpeta.mkdir();
            	if (creado) {
            		logger.info("###### Carpeta: " + carpeta.toString() + " creada con exito ######");
            	} else {
            		throw new Exception("Error al crear la carpeta") ;
            	}            	
            } else {
            	logger.info("La carpeta: "+carpeta+" ya existe");
            }
			
		} catch (Exception e) {
			respuesta = Utils.manejaExcepcion(e);
		}
	}
	
	/**
	 * Lista los archivos de un directorio especificado
	 * @param path ruta del directorio a explorar
	 * @return 
	 */
	public String  listarDirectorio(){
		try {
			success = true;	        

			UserVO usuario = (UserVO)session.get("USUARIO");
            if(usuario == null){
                throw new ApplicationException("No hay usuario en sesion");
            }

			Utils.validate(path, "No se recibio el parametro path");
	        File f = new File(path);
	        
	        slist1= new ArrayList<Map<String, String>>();
	        
	        if (f.exists()){
	        	File[] ficheros = f.listFiles();
	        	File fichero;
	        	
	        	for (int i=0;i<ficheros.length;i++){
	        		smap1 = new HashMap<String, String>();
	        		fichero = ficheros[i];
		        	logger.debug(fichero.getName());
		        	
		        	String permisoLectura = (fichero.canRead())?"r":"";
		        	String permisoEscritura= (fichero.canWrite())?"w":"";
		        	String permisoEjecucion = (fichero .canExecute())?"x":"";
		        	String permisos=permisoLectura+permisoEscritura+permisoEjecucion;
		
		        	smap1.put("nombreDoc", fichero.getName());
		        	
		        	long unidad = 1048576;
		        	long bytesFichero = fichero.length();
		        	long megasFichero = bytesFichero / unidad;
		        	smap1.put("tamDoc", (String.valueOf(megasFichero)+" MB"));//tam en bytes para convertir a MB debe dividir entre 1048576
		    		
		        	long fechaFichero = fichero.lastModified(); //fecha en milisegundos
		        	String sFechaFichero = Utils.formateaFechaMilisegundos(fechaFichero); 

		        	smap1.put("modificado", sFechaFichero);
		        	smap1.put("permisos", permisos);
		        	smap1.put("propietario", "--");
		        	slist1.add(smap1);	
		        	exito=true;
	        	}
	        	
	        } else { 
	        	throw new Exception("No existe el directorio: "+path) ;
	        }
		} catch (Exception e) {
			respuesta = Utils.manejaExcepcion(e);
		}
		return SUCCESS;
	}
	
	//Getters and setters:
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSubfolder() {
		return subfolder;
	}

	public void setSubfolder(String subfolder) {
		this.subfolder = subfolder;
	}


	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}


	public void setConsultasManager(ConsultasManager consultasManager) {
		this.consultasManager = consultasManager;
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

    public Map<String,Item> getImap() {
        return imap;
    }

    public void setImap(Map<String,Item> imap) {
        this.imap = imap;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}

	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}
	
}
