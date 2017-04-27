/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.gseguros.portal.general.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

/**
 *
 * @author Jair
 */
public class SubirArchivoAction extends PrincipalCoreAction implements ServletRequestAware{

	private static final long serialVersionUID = 8283714363561378583L;
	
	private HttpServletRequest servletRequest;
    private File file;
    private String fileFileName;
    private String fileContentType;
    private String targetId;
    private final static Logger logger = LoggerFactory.getLogger(SubirArchivoAction.class);
    private String uploadKey;
    private float progreso;
    private String progresoTexto;
    private Map<String,String>smap1;
    private KernelManagerSustituto kernelManager;
    SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
    private List<Map<String,String>>slist1;
    private boolean success;
    private String mensajeRespuesta;
    private MesaControlManager mesaControlManager;
    private boolean exito;
    private String respuesta;
    private String respuestaOculta;
    
    private long start;
    private long limit;
    private long totalCount;
    
    @Autowired
    private DocumentosManager documentosManager;
    
    @Autowired
    private ConsultasManager consultasManager;
    
	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
    
    @Value("${pass.servidor.reports}")
    private String passServidorReports;	
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
    @Value("${ruta.documentos.persona}")
    private String rutaDocumentosPersona;
    
    @Value("${user.server.layouts}")
    private String userServerLayouts;	

    @Value("${pass.server.layouts}")
    private String passServerLayouts;	

    @Value("${directorio.server.layouts}")
    private String directorioServerLayouts;	

    @Value("${dominio.server.layouts}")
    private String dominioServerLayouts;

    @Value("${dominio.server.layouts2}")
    private String dominioServerLayouts2;
    
    public String mostrarPanel()
    {
        return SUCCESS;
    }
    
    public String jsonObtenerProgreso()
    {
        CustomProgressListener listener=(CustomProgressListener) session.get("SK_PROGRESS_LISTENER");
        if(listener.getPercentDone()==100)
        {
            progresoTexto="Terminado";
        }
        else
        {
            progresoTexto="Cargando ("+listener.getPercentDone()+"%)...";
        }
        progreso=(Float.parseFloat(listener.getPercentDone()+""))/100f;
        
        // Se asigna en sesion "SK_ERROR" cuando fallo el subir el archivo:
        if(session != null && session.get("SK_ERROR") != null) {
        	logger.debug("session.SK_ERROR={}", session.get("SK_ERROR"));
        	mensajeRespuesta = (String)session.get("SK_ERROR");
        }
        
        return SUCCESS;
    }
    
    public String mostrarBarra()
    {
        return SUCCESS;
    }
    
    public String ponerLlaveSesion()
    {
        session.put("SK_LLAVE_ULTIMO_ARCHIVO",uploadKey);
        logger.debug("Se puso la ultima llave en sesion: "+session.get("SK_LLAVE_ULTIMO_ARCHIVO"));
        return SUCCESS;
    }
    
    public String subirArchivo () {
    	logger.debug(Utils.log(
    			"\n##########################",
    			"\n###### subirArchivo ######",
    			"\n###### smap1           = ", smap1,
    			"\n###### file            = ", file,
    			"\n###### fileFileName    = ", fileFileName,
    			"\n###### fileContentType = ", fileContentType
    			));
        try
        {
        	this.session = ActionContext.getContext().getSession();
        	
            UserVO usuario = Utils.validateSession(session);
            
            Utils.validate(smap1, "No se recibieron datos");
            
        	String nombreArchivo = Utils.join(System.currentTimeMillis(), "_",
        			((long)(Math.random()*10000l)), ".", fileFileName.substring(fileFileName.indexOf(".")+1)
        			);
        	
        	String nuevaRuta = Utils.join(this.rutaDocumentosPoliza, Constantes.SEPARADOR_ARCHIVO,
        			smap1.get("ntramite"), Constantes.SEPARADOR_ARCHIVO, nombreArchivo);
        	
        	String antiguaRuta = file.getAbsolutePath();
        	
        	logger.debug("se movera desde {}", antiguaRuta);
            logger.debug("se movera a {}", nuevaRuta);
            
            String rutaCarpeta = Utils.join(this.rutaDocumentosPoliza, Constantes.SEPARADOR_ARCHIVO, smap1.get("ntramite"));
            File carpeta = new File(rutaCarpeta);
            if (!carpeta.exists()) {
            	logger.info("no existe la carpeta::: "+rutaCarpeta);
            	carpeta.mkdir();
            	if (carpeta.exists()) {
            		logger.info("carpeta creada");
            	} else {
            		logger.info("carpeta NO creada");
            	}
            } else {
            	logger.debug("existe la carpeta   ::: "+rutaCarpeta);
            }
            
            try {
            	FileUtils.copyFile(file, new File(nuevaRuta));
            	logger.info("archivo movido");
			} catch (Exception e) {
				logger.error("archivo NO movido", e);
			}
            
            String codidocu = smap1.get("codidocu");             //combo original de siniestros
            String cddocume = smap1.get("cddocumeFlujo");        //combo de mesa de control de flujos
            String cddocumeRevisi = smap1.get("cddocumeRevisi"); //sin combo, cuando se sube desde revision uno particular
            
            boolean sustituir = false;
            
            if (StringUtils.isBlank(codidocu)) {
            	if (StringUtils.isNotBlank(cddocume)) {
            		codidocu = cddocume;
            	} else if (StringUtils.isNotBlank(cddocumeRevisi)) {
            		codidocu = cddocumeRevisi;
            		sustituir = true;
            	}
            }
            
            documentosManager.guardarDocumento(
            		smap1.get("cdunieco")
            		,smap1.get("cdramo")
            		,smap1.get("estado")
            		,smap1.get("nmpoliza")
            		,smap1.get("nmsuplem")
            		,renderFechas.parse(smap1.get("fecha"))
            		,nombreArchivo
            		,smap1.get("descripcion")
            		,smap1.get("nmsolici")
            		,smap1.get("ntramite")
            		,smap1.get("tipomov")
            		,null
            		,codidocu
            		,smap1.get("cdtiptra")
            		,null
            		,null
            		,usuario.getUser()
            		,usuario.getRolActivo().getClave()
            		,sustituir
            		);
            
            exito = true;
            
        } catch(Exception ex) {
        	logger.error("error al mover el archivo",ex);
        }
        logger.debug(Utils.log(
        		"\n###### exito = ", exito,
        		"\n###### subirArchivo ######",
        		"\n##########################"
        		));
        return SUCCESS;
    }
    
    public String subirArchivoPersona()
    {
    	logger.debug("smap1 "+smap1);
        logger.debug("file "+file);
        logger.debug("fileFileName "+fileFileName);
        logger.debug("fileContentType "+fileContentType);
        
        try
        {
        	String nombreArchivo=System.currentTimeMillis()+"_"+((long)(Math.random()*10000l))+"."+fileFileName.substring(fileFileName.indexOf(".")+1);
        	String nuevaRuta=this.rutaDocumentosPersona+Constantes.SEPARADOR_ARCHIVO+smap1.get("cdperson")+Constantes.SEPARADOR_ARCHIVO
                +nombreArchivo;
        	String antiguaRuta=file.getAbsolutePath();
        	logger.debug("se movera desde::: "+antiguaRuta);
            logger.debug("se movera a    ::: "+nuevaRuta);
            
            String rutaCarpeta=this.rutaDocumentosPersona+Constantes.SEPARADOR_ARCHIVO+smap1.get("cdperson");
            File carpeta = new File(rutaCarpeta);
            if(!carpeta.exists())
            {
            	logger.debug("no existe la carpeta::: "+rutaCarpeta);
            	carpeta.mkdir();
            	if(carpeta.exists())
            	{
            		logger.debug("carpeta creada");
            	}
            	else
            	{
            		logger.debug("carpeta NO creada");
            	}
            }
            else
            {
            	logger.debug("existe la carpeta   ::: "+rutaCarpeta);
            }
            
            try {
            	FileUtils.copyFile(file, new File(nuevaRuta));
            	logger.info("archivo movido");
			} catch (Exception e) {
				logger.error("archivo NO movido", e);
			}
            /*
            if(file.renameTo(new File(nuevaRuta)))
    		{
    			logger.debug("archivo movido");	
    		}
    		else
    		{
    			logger.debug("archivo NO movido");
    		}
            */
            kernelManager.guardarArchivoPersona(smap1.get("cdperson"),renderFechas.parse(smap1.get("fecha")),nombreArchivo,smap1.get("descripcion"),smap1.get("codidocu"));
        }
        
        catch(Exception ex)
        {
        	logger.error("error al mover el archivo",ex);
        }
        return SUCCESS;
    }

    public String subirArchivoCobranza(){
    	logger.debug("Subiendo Archivo de Cobranza ... ");
        logger.debug("file "+file);
        logger.debug("fileFileName "+fileFileName);
        logger.debug("fileContentType "+fileContentType);
        
        try{
        	success = FTPSUtils.upload(
        			this.dominioServerLayouts, 
        			this.userServerLayouts, 
        			this.passServerLayouts, 
        			file.getAbsolutePath(),
        			this.directorioServerLayouts+Constantes.SEPARADOR_ARCHIVO+fileFileName)
        			&&FTPSUtils.upload(
                			this.dominioServerLayouts2, 
                			this.userServerLayouts, 
                			this.passServerLayouts, 
                			file.getAbsolutePath(),
                			this.directorioServerLayouts+Constantes.SEPARADOR_ARCHIVO+fileFileName);
        	
        	if(!success) {
        		mensajeRespuesta = "Error al subir archivo.";
        		return SUCCESS;
        	}
        }catch(Exception ex) {
        	logger.error("Error al subir el archivo al servidor " + this.dominioServerLayouts2, ex);
        	mensajeRespuesta = "Error al subir archivo.";
        	success= false;
        	return SUCCESS;
        }
        
        try{
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("pv_archivo_i", fileFileName);
        	kernelManager.cargaCobranzaMasiva(params);
        } catch(Exception ex) {
        	logger.error("Error al aplicar la Cobranza",ex);
        	mensajeRespuesta = "Error al aplicar la cobranza.";
        	success= false;
        	return SUCCESS;
        }
        success= true;
        return SUCCESS;
    }
    
    public String consultaCobranza(){
    	logger.debug("Consulta Cobranza Aplicada... ");
        
        try{
        	slist1 = kernelManager.obtieneCobranzaAplicada(new HashMap<String,String>());
        }catch(Exception ex)
        {
        	logger.error("Error al Consultar la Cobranza",ex);
        	mensajeRespuesta = "Error al Consultar la Cobranza.";
        	success= false;
        	return SUCCESS;
        }
        
        success= true;
        return SUCCESS;
    }
    
    
    public String consultaRemesa(){
    	logger.debug("Consulta Remesa Aplicada... ");
        
        try{
        	slist1 = kernelManager.obtieneRemesaAplicada(new HashMap<String,String>());
        }catch(Exception ex)
        {
        	logger.error("Error al Consultar la Remesa",ex);
        	mensajeRespuesta = "Error al Consultar la Remesa.";
        	success= false;
        	return SUCCESS;
        }
        
        success= true;
        return SUCCESS;
    }
    
    
	public String ventanaDocumentosPolizaLoad()
	{
		logger.debug(Utils.log(
				 "\n#########################################"
				,"\n###### ventanaDocumentosPolizaLoad ######"
				,"\n###### start = " , start
				,"\n###### limit = " , limit
				,"\n###### smap1 = " , smap1
				));
		try
		{
			Utils.validate(smap1, "No se recibieron datos");
			
			/*
			pv_cdunieco_i
			pv_cdramo_i
			pv_estado_i
			pv_nmpoliza_i
			pv_registro_o
			pv_msg_id_o
			pv_title_o
			*/
			Map<String,Object>paramGetDocu=new LinkedHashMap<String,Object>(0);
			paramGetDocu.put("pv_start_i", start);
			paramGetDocu.put("pv_limit_i", limit);
			Iterator it=smap1.entrySet().iterator();
			while(it.hasNext())
			{
				Entry e=(Entry) it.next();
				paramGetDocu.put((String)e.getKey(),e.getValue());
			}
			
			slist1=kernelManager.obtenerDocumentosPoliza(paramGetDocu);
			
			//SI VIENE ACTIVO smap1.usuariosPrimero = S entonces se muestran primero los docs de usuario
			boolean usuariosPrimero = "S".equals(smap1.get("usuariosPrimero"));
			
			if(slist1!=null&&slist1.size()>0)
			{
				totalCount = Long.parseLong(slist1.get(0).get("total"));
				
				for(int i=0;i<slist1.size();i++)
				{
					String nombre       = slist1.get(i).get("cddocume")
					       ,descripcion = slist1.get(i).get("dsdocume")
					       ,tipmov      = slist1.get(i).get("tipmov")
					       ,nmsuplem    = slist1.get(i).get("nmsuplem")
					       ,nsuplogi    = slist1.get(i).get("nsuplogi")
					       ,ntramite    = slist1.get(i).get("ntramite");
					
					if(descripcion==null||descripcion.length()==0)
					{
						descripcion="(no especificado)";
					}
					
					String mezcla = Utils.join(nombre,"#_#",descripcion);
					
					slist1.get(i).put("liga",mezcla);
					
					String orden = null;
					
					if ("USUARIO".equals(tipmov)) {
						if (usuariosPrimero) { // Si viene encendido entonces todos los de usuario van arriba
							nsuplogi = ntramite;
							slist1.get(i).put("nsuplogi",nsuplogi);
						} else {
							// Si algun endoso tiene el mismo tramite que nuestro documento de usuario, copiamos su nsuplogi 
							boolean vinculadoConEndoso = false;
							for (Map<String, String> documento : slist1) {
								if (
									ntramite.equals(documento.get("tramite_endoso"))
									&& !"USUARIO".equals(documento.get("tipmov"))
								) {
									nsuplogi = documento.get("nsuplogi");
									slist1.get(i).put("nsuplogi",nsuplogi);
									vinculadoConEndoso = true;
									break;
								}
							}
							
							// Si no encontramos ningun endoso al que copiar nsuplogi, el documento es de emision o es de un tramite nuevo,
							// si encontramos un documento con tramite menor significa que el nuestro es de nuevo, y va hasta arriba
							if (!vinculadoConEndoso) {
								for (Map<String, String> documento : slist1) {
									if (
										!"USUARIO".equals(documento.get("tipmov")) // no es de usuario
										&& StringUtils.isNotBlank(documento.get("ntramite")) // tiene tramite
										&& Double.parseDouble(ntramite) > Double.parseDouble(documento.get("ntramite")) // tiene uno menor
									) {
										nsuplogi = ntramite;
										slist1.get(i).put("nsuplogi",nsuplogi);
										break;
									}
								}
							}
						}
					}
					
					orden = Utils.join(nmsuplem,"#_#",tipmov,"#_#",nsuplogi);
					
					slist1.get(i).put("orden",orden);
				}
			}
			else
			{
				totalCount = 0;
			}
				
			success = true;
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success    = " , success
				,"\n###### respuesta  = " , respuesta
				,"\n###### totalCount = " , totalCount
				,"\n###### ventanaDocumentosPolizaLoad ######"
				,"\n#########################################"
				));
		
		return SUCCESS;
	}
    
	////////////////////////////////////////////////
	////// generar contrarecibo de documentos //////
	/*////////////////////////////////////////////*/
	public String generarContrarecibo()
	{
		logger.debug(Utils.log(
				"\n#################################",
				"\n###### generarContrarecibo ######",
				"\n###### smap1 = ", smap1
				));
		
		SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	    Calendar calendarHoy=Calendar.getInstance();
		
		try
		{
			UserVO usu = Utils.validateSession(session);
			
			long timestamp=System.currentTimeMillis();
			String random=((long)(Math.random()*10000l))+"";
			/*String claveReciboTabla="contrarecibo_"+slist1.get(0).get("pv_ntramite_i")+"_"+timestamp+"_"+random;
			logger.debug("la clave para guardar en BD es: "+claveReciboTabla);
			for(Map<String,String> docu:slist1)
			{
				docu.put("pv_cdconrec_i",claveReciboTabla);
				kernelManager.preparaContrarecibo(docu);
			}*/
			String filePath = Utils.join(
				this.rutaDocumentosPoliza,
				Constantes.SEPARADOR_ARCHIVO,
				smap1.get("ntramite"),
				Constantes.SEPARADOR_ARCHIVO,
				"contrarecibo_", timestamp, "_", random, ".pdf"
			);
			
			String nombreRdf = this.getText("rdf.contrarecibo.danios.nombre");
			
			if (consultasManager.esTramiteSalud(smap1.get("ntramite"))) {
				logger.debug("Es salud");
				nombreRdf = this.getText("rdf.contrarecibo.salud.nombre");
			} else {
				logger.debug("No es salud");
			}
			
			String requestUrl = Utils.join(
				this.rutaServidorReports,
				"?destype=cache",
				"&desformat=PDF",
				"&paramform=no",
				"&ACCESSIBLE=YES",
				"&userid="    , passServidorReports,
				"&report="    , nombreRdf,
				"&p_fecha="   , renderFechas.format(calendarHoy.getTime()),
				"&p_tramite=" , smap1.get("ntramite"),
				"&p_usuario=" , usu.getUser(),
				"&desname="   , filePath
			);
			logger.debug("se pide el contrarecibo a: "+requestUrl);
			logger.debug("se guardara el contrarecibo en: "+filePath);
			HttpUtil.generaArchivo(requestUrl, filePath);
			uploadKey="contrarecibo_"+timestamp+"_"+random+".pdf";
			
			mesaControlManager.guardarRegistroContrarecibo(smap1.get("ntramite"),usu.getUser());
			
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al generar contrarecibo",ex);
			success=false;
		}
		logger.debug(Utils.log(
				"\n###### success = ", success,
				"\n###### generarContrarecibo ######",
				"\n#################################"
				));
		return SUCCESS;
	}

	public String habilitaSigRecibo(){
		logger.debug("Entrando a habilitar siguiente recibo");
		try{
			progresoTexto = kernelManager.habilitaSigRecibo(smap1);
			success=false;
			
			logger.debug("Mensaje de habilitar recibos: " + progresoTexto);
			
			if(StringUtils.isBlank(progresoTexto)){
				progresoTexto="Se ha habilitado el recibo subsecuente.";
				success =  true;
			} else if("3".equals(progresoTexto)){
				progresoTexto = "No existen recibos por habilitar.";
			} else if(Constantes.MSG_TITLE_ERROR.equals(progresoTexto)){
				progresoTexto = "Error en la Operaci\u00f3n";
			} else {
				progresoTexto="Se ha habilitado el recibo subsecuente.";
				success=true;
			}
		}catch(Exception ex){
			logger.error("Error al habilitar el recibo subsecuente.",ex);
			progresoTexto = ex.getMessage();
			success=false;
		}
		
		
		return SUCCESS;
	}
	
	public String actualizarNombreDocumento()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### actualizarNombreDocumento ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			exito = true;
			
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron valores");
			}
			String ntramite = smap1.get("ntramite");
			String cddocume = smap1.get("cddocume");
			String nuevo    = smap1.get("nuevo");
			
			if(StringUtils.isBlank(ntramite))
			{
				throw new ApplicationException("No se recibio el numero de tramite");
			}
			if(StringUtils.isBlank(cddocume))
			{
				throw new ApplicationException("No se recibio el codigo del documento");
			}
			
			mesaControlManager.actualizarNombreDocumento(ntramite,cddocume,nuevo);
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			if(ex instanceof ApplicationException)
			{
				respuesta = ex.getMessage()+" #"+timestamp;
			}
			else
			{
				respuesta = "Error al actualizar el nombre del documento #"+timestamp;
			}
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### actualizarNombreDocumento ######"
				,"\n#######################################"
				));
		return SUCCESS;
	}
	

	
	public String borrarDocumento()
	{
		logger.debug(Utils.log(
				 "\n#############################"
				,"\n###### borrarDocumento ######"
				,"\n###### smap1=",smap1
				));
		
		try
		{
			exito = true;
			
			if(smap1==null)
			{
				throw new ApplicationException("No se recibieron valores");
			}
			String ntramite = smap1.get("ntramite");
			String cddocume = smap1.get("cddocume");
			
			if(StringUtils.isBlank(ntramite))
			{
				throw new ApplicationException("No se recibio el numero de tramite");
			}
			if(StringUtils.isBlank(cddocume))
			{
				throw new ApplicationException("No se recibio el codigo del documento");
			}
			
			mesaControlManager.borrarDocumento(ntramite,cddocume);
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false;
			if(ex instanceof ApplicationException)
			{
				respuesta = ex.getMessage()+" #"+timestamp;
			}
			else
			{
				respuesta = "Error al borrar el documento #"+timestamp;
			}
			respuestaOculta = ex.getMessage();
			logger.error(respuesta,ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### borrarDocumento ######"
				,"\n#############################"
				));
		return SUCCESS;
	}
	
	/////////////////////////////////
	////// getters and setters //////
	/*/////////////////////////////*/
    public void setServletRequest(HttpServletRequest hsr) {
        this.servletRequest=hsr;
    }

    public File getFile() {
        return file;
    }

    public String getProgresoTexto() {
        return progresoTexto;
    }

    public void setProgresoTexto(String progresoTexto) {
        this.progresoTexto = progresoTexto;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    public float getProgreso() {
        return progreso;
    }

    public void setProgreso(float progreso) {
        this.progreso = progreso;
    }

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}

	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}

	public void setMesaControlManager(MesaControlManager mesaControlManager) {
		this.mesaControlManager = mesaControlManager;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
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

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
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

	public String getRutaDocumentosPersona() {
		return rutaDocumentosPersona;
	}

	public String getUserServerLayouts() {
		return userServerLayouts;
	}

	public String getPassServerLayouts() {
		return passServerLayouts;
	}

	public String getDirectorioServerLayouts() {
		return directorioServerLayouts;
	}

	public String getDominioServerLayouts() {
		return dominioServerLayouts;
	}

	public String getDominioServerLayouts2() {
		return dominioServerLayouts2;
	}

    
}
