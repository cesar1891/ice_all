package mx.com.gseguros.wizard.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.procesoarchivo.ProcesadorArchivosContext;
import mx.com.gseguros.portal.general.procesoarchivo.Tabla5ClavesProcesamientoArchivoStrategyImpl.TipoTabla;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.validacionformato.CampoVO;
import mx.com.gseguros.portal.general.validacionformato.ValidadorFormatoContext;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/cargamasiva")
public class CargaMasivaAction extends PrincipalCoreAction {
	
	private List<Map<String,String>>         slist1;
	private Map<String,String>               smap1;
	private String                           mensaje;
	private static final long serialVersionUID = -3861435458381281429L;
	
	private static Logger logger = LoggerFactory.getLogger(CargaMasivaAction.class);

	@Value("${directorio.server.layouts}")
    private String directorioServerLayouts;
	
	public CargaMasivaAction()
	{
		this.session=ActionContext.getContext().getSession();
	}
	
	@Autowired
	private transient CatalogosManager catalogosManager; 
	
	@Autowired
	private EmisionManager emisionManager;
	
	private Map<String, String> params;
	
	private GenericVO resultado;
	
	private File file;
    private String fileFileName;
    private String fileContentType;
    private String respuesta;
    
    private boolean success;
	
	@Autowired
	private ValidadorFormatoContext validadorFormatoContext;
	
	@Autowired
	private ProcesadorArchivosContext procesadorArchivosContext;
	
	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	@Action(value="cargaTablaApoyo",
	results={@Result(name="success", type="json")}
	)
	public String cargaTablaApoyo() throws Exception {
		
		try {
			logger.info(">>>>>  Parametros para la carga masiva de Tablas de Apoyo: {}", params);
			
			// Tipo de Tabla (1 o 5 claves):
			Integer tipoTabla = Integer.parseInt(params.get("tipotabla"));
			
			// Formato del archivo (para tabla de 1 o 5 claves):		
			List<CampoVO> campos = new ArrayList<CampoVO>();
			
			try {
				logger.info("Se agregan campos, claves y atributos...");
				
				// Dependiendo el tipo de tabla creamos el numero de campos llave por default :
				for (int i = 0, totalClaves = tipoTabla; i < totalClaves; i++) {
					campos.add(new CampoVO(CampoVO.ALFANUMERICO, null, null, true)); 
				}
				
				//Agregamos las claves:
				List<Map<String, String>> claves = catalogosManager.obtieneClavesTablaApoyo(params);
				for (Map<String, String> cve : claves) {
					campos.set(new Integer(cve.get("NUMCLAVE"))-1, 
							new CampoVO(
									cve.get("SWFORMA1"),
									NumberUtils.createInteger(cve.get("NMLMIN1")),
									NumberUtils.createInteger(cve.get("NMLMAX1")),
									false));
				}
				
				// Agregamos 2 campos de fecha si es tipo tabla de 5 claves:
				if(tipoTabla == TipoTabla.CINCO.getCodigo()) {
					for (int i = 0, totalClaves = 2; i < totalClaves; i++) {
						campos.add(new CampoVO(CampoVO.FECHA, null, null, true));
					}
				}
				
				// Agregamos los atributos de la tabla:
				List<Map<String, String>> atributos = catalogosManager.obtieneAtributosTablaApoyo(params);
				for (Map<String, String> atr : atributos) {
					campos.add( 
							new CampoVO(
									atr.get("SWFORMAT"),
									NumberUtils.createInteger(atr.get("NMLMIN")), 
									NumberUtils.createInteger(atr.get("NMLMAX")), 
									true));
				}
				
			} catch (Exception e) {
				logger.error("Error en cargar tabla de apoyo, ", e);
				throw new ApplicationException("Las claves y atributos de la tabla de apoyo no est\u00E1n correctamente parametrizados");
			}
			
			
			// VALIDACION DE FORMATO:
			logger.info("Se valida el formato de los campos: {}", campos);
			
			String fullNameArchErrValida = rutaDocumentosTemporal + Constantes.SEPARADOR_ARCHIVO+"conversion_" + System.currentTimeMillis() + "_err.txt";
			File archErrVal = validadorFormatoContext.ejecutaValidacionesFormato(file, campos, fullNameArchErrValida, mx.com.gseguros.portal.general.validacionformato.ValidadorFormatoContext.Strategy.VALIDACION_EXCEL);
			if(archErrVal != null && archErrVal.length() > 0) {
				String msjeError = "Archivo tiene errores de formato";
				resultado = new GenericVO("1", msjeError);
				fileFileName = archErrVal.getName();
				throw new ApplicationException(msjeError);
			}
			
			
			// PROCESAMIENTO DEL ARCHIVO:
			//logger.info("Se ejecuta proceso de archivo: " + this.directorioServerLayouts+Constantes.SEPARADOR_ARCHIVO+fileFileName);
			logger.info("Se ejecuta proceso de archivo: {}", this.directorioServerLayouts,Constantes.SEPARADOR_ARCHIVO,fileFileName);
			
			mx.com.gseguros.portal.general.procesoarchivo.ProcesadorArchivosContext.Strategy estrategiaProcesaArchivo = null;
			if(tipoTabla == TipoTabla.UNA.getCodigo()) {
				estrategiaProcesaArchivo = mx.com.gseguros.portal.general.procesoarchivo.ProcesadorArchivosContext.Strategy.ARCHIVO_TABLA_1_CLAVE;
			} else if(tipoTabla == TipoTabla.CINCO.getCodigo()) {
				estrategiaProcesaArchivo = mx.com.gseguros.portal.general.procesoarchivo.ProcesadorArchivosContext.Strategy.ARCHIVO_TABLA_5_CLAVES;
			}
			RespuestaVO resp = procesadorArchivosContext.ejecutaProcesamientoArchivo(
					file,
					campos,
					Integer.parseInt(params.get("pi_nmtabla")),
					Integer.parseInt(params.get("tipoproceso")),
					params.get("feCierre"),
					estrategiaProcesaArchivo);
			if(!resp.isSuccess()) {
				String msjeError = "Error en el guardado";
				resultado = new GenericVO("2", resp.getMensaje());
				throw new ApplicationException(msjeError);
			}
			
			logger.info("Termina carga masiva de {} exitosamente" ,fileFileName);
			success = true;
			
		} catch (ApplicationException appExc) {
			logger.error("Error en la carga masiva ", appExc);
		}
		
		return SUCCESS;
	}
	
	
	
	@Action(value="procesarCargaMasivaRecuperaInd",
			results={
					@Result(name="exito", type="json"),
					@Result(name="error", type="json")
					}
	)
	public String procesarCargaMasivaRecuperaInd() {
		int tamano = 0;
		int contar = 0;
		int contar2 = 0;
		String result = null;
		smap1 = new HashMap<String, String>();
		try {
			logger.debug("Validando datos de entrada");
			Utils.validate(file, "No se recibi\u00f3 el archivo");

			logger.debug("ANTES DE HACER LA IMPLEMENTACION {}",fileFileName);

			List<CampoVO> campos = new ArrayList<CampoVO>();
			campos.add(new CampoVO(CampoVO.NUMERICO, 1, 100, false)); // 1
			campos.add(new CampoVO(CampoVO.NUMERICO, 1, 100, true)); // 2
			campos.add(new CampoVO(CampoVO.NUMERICO, 1, 100, false)); // 3
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 4
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 5
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 6
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 7
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 8
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 9
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 10
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 11
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 12
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 13
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 14
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 15
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 16
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, true)); // 17
			campos.add(new CampoVO(CampoVO.ALFANUMERICO, 1, 100, false)); // 18
			// Nombre del archivo de errores (si los hay):
			String fullNameArchErrValida = rutaDocumentosTemporal + Constantes.SEPARADOR_ARCHIVO
					+ "conversion_" + System.currentTimeMillis() + "_err.txt";
			File archErrVal = validadorFormatoContext.ejecutaValidacionesFormato(file, campos, fullNameArchErrValida,
					ValidadorFormatoContext.Strategy.VALIDACION_EXCEL);
			if (archErrVal != null && archErrVal.length() > 0) {
				String msjeError = "Archivo tiene errores de formato";
				resultado = new GenericVO("1", msjeError);
				fileFileName = archErrVal.getName();
				throw new ApplicationException(msjeError);
			} else {
				logger.debug("TERMINA PROCESO {}", campos);
				success = true;
				ManagerRespuestaSlistVO resp = emisionManager.procesarCargaMasivaRecupera(file);// ,tipoflot
				logger.debug(resp.getRespuesta(), "{} #### {}", resp.getRespuestaOculta());
				tamano = resp.getSlist().size();
				if (resp.getRespuesta() == "" || resp.getRespuesta() == null) {
					int tam = resp.getSlist().size();
					for (int i = 0; i <= tam - 1; i++) {
						for (Entry<String, String> en : resp.getSlist().get(i).entrySet()) {
							if("MEMBRESIA".equals(en.getKey())) {
								contar += 1;
								slist1 = resp.getSlist();
							}
						}
					}
				} else {

					String delimita[] = resp.getRespuesta().split("\n");
					logger.debug("la respuesta {}",resp.getRespuesta());
					contar2 = delimita.length;
					smap1.put("ERROR", resp.getRespuesta());
					slist1 = resp.getSlist();
					result = "errores_validacion";
				}
			}
			result = "exito";
			success = true;
		} catch (Exception ex) {
			result = "error";
			mensaje = Utils.manejaExcepcion(ex);
			logger.error("Error en al carga de Archivo de Emision Recupera Individual:", ex);
		}
		logger.debug("NOMBRE DE ARCHIVO : ",fileFileName);
		smap1.put("filasLeidas", String.valueOf(tamano));
		if (contar2 > 0) {
			smap1.put("filasErrores", String.valueOf(contar2));
		} else {
			smap1.put("filasErrores", String.valueOf(contar - tamano));
		}
		smap1.put("filasProcesadas", String.valueOf(contar));
		smap1.put("nombreArchivo", fileFileName);
		return result;
	}
	
	
	@Action(value           = "generarPolizasRecuperaInd",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			}
	)
	public String generarPolizasRecuperaInd() {
		try {
			String mensaje = "";
			String cdperson = "";
			String sucursal = "";
			String poliza = "";
			String nombre1 = "";
			String nombre2 = "";
			String apePat = "";
			String apeMat = "";
			String producto = "";
			String cve_plan = "";
			String esq_suma_ase = "";
			String parentesco = "";
			String f_nacimiento = "";
			String RFC = "";
			String sexo = "";
			String peso = "";
			String estatura = "";
			String fecinivig = "";
			String membresia = "";
			String nombreArchivo = smap1.get("nombreArchivo");
			logger.debug(Utils.log("\n###############################", "\n###### params = ", params,
					"\n###### list   = ", slist1, "\n###### smap   = ", smap1));

			logger.debug("TAMA\u00D1O: {}" , slist1.size());
			for (Map<String, String> ite : slist1) {
				membresia = null;
				cdperson = ite.get("CDUNIECO");
				sucursal = ite.get("SUCURSAL");
				poliza = ite.get("POLIZA");
				nombre1 = ite.get("NOMBRE1");
				nombre2 = ite.get("NOMBRE2");
				apePat = ite.get("APEPAT");
				apeMat = ite.get("APEMAT");
				producto = ite.get("PRODUCTO");
				cve_plan = ite.get("PLAN");
				esq_suma_ase = ite.get("ESQUEMA");
				parentesco = ite.get("PARENTESCO");
				f_nacimiento = ite.get("FECNAC");
				RFC = ite.get("RFC");
				sexo = ite.get("SEXO");
				peso = ite.get("PESO");
				estatura = ite.get("ESTATURA");
				fecinivig = ite.get("FECINIVIG");
				membresia = ite.get("MEMBRESIA");
				if (membresia != null) {
					mensaje = emisionManager.generarPoliza(cdperson, sucursal, poliza, nombre1, nombre2, apePat, apeMat,
							producto, cve_plan, esq_suma_ase, parentesco, f_nacimiento, RFC, sexo, peso, estatura,
							fecinivig, membresia);
					logger.debug("mensaje {} ", mensaje);
				}

			}
			logger.debug("MENSAJE DE TEXTO {}",mensaje);
			if (StringUtils.isNotBlank(mensaje)) {
				Utils.validateSession(session);
				emisionManager.insertaBitacora(new Date(), nombreArchivo, slist1.size(),
						slist1.size() + "-" + slist1.size(), "web");
				respuesta = "success";
			}else{
				respuesta = "failure";
			}
		} catch (Exception ex) {
			respuesta = "failure";
			mensaje = Utils.manejaExcepcion(ex);
		}
		logger.debug("ES EL RESULTADO DE LA RESPUESTA {}",respuesta);
		return respuesta;
	}
	//Getters and setters:
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public File getFile() {
		return file;
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public GenericVO getResultado() {
		return resultado;
	}

	public void setResultado(GenericVO resultado) {
		this.resultado = resultado;
	}
	
	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
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
	
	public String getDirectorioServerLayouts() {
		return directorioServerLayouts;
	}
}