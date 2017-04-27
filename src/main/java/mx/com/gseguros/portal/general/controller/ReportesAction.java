package mx.com.gseguros.portal.general.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.ParamReporteVO;
import mx.com.gseguros.portal.general.model.ReporteVO;
import mx.com.gseguros.portal.general.service.ReportesManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.TipoArchivo;
import mx.com.gseguros.utils.HttpUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class ReportesAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 5866297387639852014L;

	private static Logger logger = Logger.getLogger(ReportesAction.class);

	private ReportesManager reportesManager;
	
	private List<ReporteVO> reportes;
	
	private List<ParamReporteVO> paramsReporte;
	
	private Map<String, String> params;
	
	private InputStream fileInputStream;
	
	private String filename;
	
	protected String contentType;

	protected boolean success;
	
	private String cdreporte;
	private String cdPantalla;
	private String cdSeccion;
	
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	
	public String obtenerListaReportes() throws Exception {
		reportes = reportesManager.obtenerListaReportes();
		success = true;
		return SUCCESS;
	}
	
	
	public String obtenerComponentesReporte() throws Exception {
		
		// Obtenemos los parametros del reporte solicitado:
		List<ComponenteVO> lstCmp = reportesManager.obtenerParametrosReportes(cdreporte, cdPantalla, cdSeccion);
		
		// Generamos los campos/items para presentarlos:
		GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
        gc.generaComponentes(lstCmp, true, false, true, false, false, false);
        params = new HashMap<String, String>();
        params.put("items", gc.getItems().toString());
        logger.debug("items=" + params.get("items"));
		
		success = true;
		return SUCCESS;
	}
 
	
	public String procesoObtencionReporte() throws Exception {
		
		try {
			// Obtenemos el username de la sesion, si no existe generamos uno:
			String username = null;
			if(session != null && session.get("USUARIO") != null) {
				username = ((UserVO)session.get("USUARIO")).getUser();
			} else {
				username = new StringBuffer("user_").append(System.currentTimeMillis()).toString();
			}
			contentType     = TipoArchivo.XLS.getContentType();
			filename        = cdreporte + TipoArchivo.XLS.getExtension();
			fileInputStream = reportesManager.obtenerDatosReporte(cdreporte, username, params);
		} catch (Exception e) {
			logger.error("Error en la obtencion del reporte", e);
		}
		
		success = true;
		return SUCCESS;
	}
	
	public String exportaTablaApoyo() throws Exception {
		
		try {
			logger.debug(" >>>>>> Parametros para exportar tabla de apoyo: " + params);
			params.put("pv_cdreporte_i", "PRUEBA");
			params.put("pv_usuario_i", "ALEX");
			contentType     = TipoArchivo.XLS.getContentType();
			filename        = params.get("pi_cdtabla")+"_"+ params.get("pi_dstabla") + TipoArchivo.XLS.getExtension();
			fileInputStream = reportesManager.exportaTablaApoyo(params);
		} catch (Exception e) {
			logger.error("Error al exportar tabla", e);
		}
		
		success = true;
		return SUCCESS;
	}
	
	public String generaBytesArchivo() throws Exception {
		
		/*
		//Un texto cualquiera guardado en una variable
		contentType = TipoArchivo.TXT.getContentType();
		setFilename("archivoPrueba" + TipoArchivo.TXT.getExtension());
		String saludo="Prueba de generacion de texto desde ICE.";
		try
		{
			//Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
			File archivo = new File("C:\\Users\\Ricardo\\Desktop\\SPs de prueba2.txt");
			//Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
			FileWriter escribir = new FileWriter(archivo, true);
			//Escribimos en el archivo con el metodo write 
			escribir.write(saludo);
			//Cerramos la conexion
			escribir.close();
			setFileInputStream(new FileInputStream(archivo));
		}catch(Exception e){ //Si existe un problema al escribir cae aqui
			logger.error("Error al generar archivo TXT, ", e);
		}
		return SUCCESS;
		*/
		
		String url = "http://docs.oracle.com/javase/specs/jvms/se8/jvms8.pdf";
		if(params != null && params.get("url")!= null) {
			url = params.get("url");
		}
		
		contentType = TipoArchivo.PDF.getContentType();
		filename    = "archivoPrueba" + TipoArchivo.PDF.getExtension();
		// TODO: Reemplazar por el codigo de Jasper Reports que devuelve el inputstream:
		fileInputStream = HttpUtil.obtenInputStream(url);
		return SUCCESS;
		
	}
	
	
	//Getters and setters:

	public void setReportesManager(ReportesManager reportesManager) {
		this.reportesManager = reportesManager;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
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

	public List<ReporteVO> getReportes() {
		return reportes;
	}

	public void setReportes(List<ReporteVO> reportes) {
		this.reportes = reportes;
	}
	
	public List<ParamReporteVO> getParamsReporte() {
		return paramsReporte;
	}

	public void setParamsReporte(List<ParamReporteVO> paramsReporte) {
		this.paramsReporte = paramsReporte;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getCdreporte() {
		return cdreporte;
	}

	public void setCdreporte(String cdreporte) {
		this.cdreporte = cdreporte;
	}


	public String getCdPantalla() {
		return cdPantalla;
	}


	public void setCdPantalla(String cdPantalla) {
		this.cdPantalla = cdPantalla;
	}


	public String getCdSeccion() {
		return cdSeccion;
	}


	public void setCdSeccion(String cdSeccion) {
		this.cdSeccion = cdSeccion;
	}

}