package mx.com.gseguros.portal.general.util;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.general.service.MailService;
import mx.com.gseguros.utils.HttpUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

public class MailAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 3608545898806750390L;
	
	static final Logger logger = LoggerFactory.getLogger(MailAction.class);
	
	private MailService mailService;
	
	private boolean success;
	
	private List<String> to;
	
	private List<String> cc;
	
	private List<String> bcc;
	
	private String asunto;
	
	private String mensaje;
	
	private List<String> archivos;
	
	/**
	 * Nombre que tendr&aacute; el archivo adjunto obtenido de urlArchivo
	 */
	private String nombreArchivo;
	
	/**
	 * URL del archivo adjunto
	 */
	private String urlArchivo;
	
	/**
	 * Indica si el content-type del mensaje es HTML 
	 */
	private boolean html;

	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	/**
	 * Envia un e-mail. Se pueden adjuntar archivos de 2 formas: <br/>
	 * 1.- Enviando una lista de rutas completas del servidor, usando el atributo "archivos". <br/> 
	 * 2.- Si el contenido de un archivo proviene de una URL, enviamos los parametros 
	 *     urlArchivo (contenido) y nombreArchivo (nombre que tendr&aacute; el archivo adjunto).  
	 * @return
	 * @throws Exception
	 */
	public String enviaCorreo() throws Exception {
		
		try{
			// Si viene la url de un archivo lo agrega a la lista de archivos adjuntos:
			if(StringUtils.isNotBlank(urlArchivo) && StringUtils.isNotBlank(nombreArchivo) ) {
				String nombreCompletoArchivo = this.rutaDocumentosTemporal + File.separator + nombreArchivo;
				if(HttpUtil.generaArchivo(urlArchivo, nombreCompletoArchivo)) {
					if(archivos == null) {
						archivos = new ArrayList<String>();
					}
					archivos.add(nombreCompletoArchivo);
				} else {
					String mensaje = new StringBuffer("El archivo ").append(nombreCompletoArchivo).append(" no existe, no se adjuntar\u00E1").toString(); 
					throw new Exception(mensaje);
				}
			}
			
			success = mailService.enviaCorreo(obtieneEMails(to),obtieneEMails(cc), obtieneEMails(bcc),
					asunto, mensaje, obtieneRutasAdjuntos(archivos), html);
			
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * 
	 * @param rutasArchivos
	 * @return
	 */
	private String[] obtieneRutasAdjuntos(List<String> rutasArchivos) {
		
		String[] rutas = null;
		if(rutasArchivos != null && !rutasArchivos.isEmpty()) {
			rutas = rutasArchivos.toArray(new String[rutasArchivos.size()]);
		}
		return rutas;
	}
	
	
	/**
	 * Busca todos los emails separados por ";" dentro de cada elemento de una lista y los devuelve en un arreglo
	 * @param lstMails lista 
	 * @return Arreglo de Strings con todos los mails obtenidos
	 */
	private String[] obtieneEMails(List<String> lstMails) {
		
		String[] arrEmails = null;
		if(lstMails != null && !lstMails.isEmpty()) {
			List<String> emails = new ArrayList<String>();
			for(String strMails : lstMails) {
				// Se agregan todos los e-mails separados por ";":
				emails.addAll( Arrays.asList( StringUtils.split(strMails, ";") ) );
			}
			arrEmails = emails.toArray(new String[lstMails.size()]);
		}
		return arrEmails;
	}


	//Getters and setters
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<String> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<String> archivos) {
		this.archivos = archivos;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getUrlArchivo() {
		return urlArchivo;
	}

	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}
	
	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}


	/**
	 * mailService setter
	 * @param mailService
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
	}


	
}