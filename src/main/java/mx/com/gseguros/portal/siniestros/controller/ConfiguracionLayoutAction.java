package mx.com.gseguros.portal.siniestros.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.service.CotizacionManager;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.TipoFecha;
import mx.com.gseguros.portal.general.validacionformato.CampoVO;
import mx.com.gseguros.portal.general.validacionformato.ValidadorFormatoContext;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

public class ConfiguracionLayoutAction extends PrincipalCoreAction {
	private static final long serialVersionUID = -1637743812712245272L;
	static final Logger logger = LoggerFactory.getLogger(ConfiguracionLayoutAction.class);
	private DateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	private HashMap<String,String> 	params;
	private Map<String,String>      smap1;
	private String                  error;
	private String                  respuesta;
	private String                  respuestaOculta = null;
	private boolean                 exito           = false;
	private File                    censo;
	private List<Map<String,Object>> olist1;
	private SiniestrosManager 		 siniestrosManager;
	private KernelManagerSustituto kernelManagerSustituto;
	private PantallasManager       pantallasManager;
	private transient CatalogosManager catalogosManager;
	private transient Ice2sigsService ice2sigsService;
	private List<HashMap<String,String>> datosTablas;
	private List<Map<String,String>> datosValidacion;
	private List<Map<String,String>>  datosInformacionAdicional;
	private boolean success;
	private File layoutGral;
	private String tipo; 
	private String minimo = null;
	private String maximo = null;
	private boolean requerido = false;
	private String formatofecha = null;
	private GenericVO resultado;
	private String fileName;
    private String fileNameError;
    private String censoFileName;
	private String censoContentType;
	private List<Map<String,String>> slist1;
	private String mensaje;
	private String validacionGeneral;
	
	@Autowired
	private ValidadorFormatoContext validadorFormatoContext;
    
	@Autowired
	private CotizacionManager cotizacionManager;

	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	@Value("${user.server.layouts}")
    private String userServerLayouts;	

	@Value("${pass.server.layouts}")
    private String passServerLayouts;	

	@Value("${directorio.server.layouts}")
    private String directorioServerLayouts;	

	@Value("${dominio.server.layouts}")
    private String dominioServerLayouts;

	/**
	* Funcion donde obtenemos los datos de las validaciones del siniestro
	* @param params
	* @return List<Map<String, String>> datosValidacion
	*/ 
	public String consultaDatosConfiguracionProveedor(){
		logger.debug("Entra a consultaDatosProveedorSiniestro params de entrada :{} ",params);
		try {
			datosValidacion = siniestrosManager.getConsultaConfiguracionProveedor(params.get("cdpresta"));
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosProveedorSiniestro : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
	
	public String  guardarConfiguracionProveedor(){
		logger.debug("Entra a guardarConfiguracionProveedor params de entrada :{}",params);
		try {
			Date   fechaProcesamiento = new Date();
			this.session=ActionContext.getContext().getSession();
			UserVO usuario=(UserVO) session.get("USUARIO");
			
			
			String respuesta = siniestrosManager.guardaConfiguracionProveedor(params.get("cmbProveedorMod"),params.get("tipoLayout"),params.get("idaplicaIVA"),
					params.get("secuenciaIVA"),params.get("idaplicaIVARET"),usuario.getUser(), fechaProcesamiento, params.get("proceso"));
		}catch( Exception e){
			logger.error("Error al obtener el monto del arancel : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* metodo para el guardado del alta del tramite
	* @param Json con todos los valores del formulario y los grid
	* @return Lista AutorizaServiciosVO con la informacion de los asegurados
	*/
	public String guardaConfiguracionLayout(){
		logger.debug("Entra a guardaAltaTramite Params: {} datosTablas {}", params,datosTablas);
		try{
			//Realizamos la insercion de los guardados
			siniestrosManager.guardaLayoutProveedor(params.get("cmbProveedor"),params.get("tipoLayout"), null,null,null,null,null,null,null,null,"D");
			for(int i=0;i<datosTablas.size();i++) {
				siniestrosManager.guardaLayoutProveedor(
					params.get("cmbProveedor"), 
					params.get("tipoLayout"),
					datosTablas.get(i).get("claveAtributo"),
					datosTablas.get(i).get("claveFormatoAtributo"),
					datosTablas.get(i).get("valorMinimo"),
					datosTablas.get(i).get("valorMaximo"),
					datosTablas.get(i).get("columnaExcel"),
					datosTablas.get(i).get("claveFormatoFecha"),
					datosTablas.get(i).get("atributoRequerido"),
					i+"",
					null
				);
			}
		}catch( Exception e){
			logger.error("Error en el guardado de alta de tramite : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String validaExisteConfiguracionProv(){
		logger.debug("Entra a validaExisteConfiguracionProv  Params: {}", params);
		try {
			validacionGeneral = siniestrosManager.validaExisteConfiguracionProv(params.get("cdpresta"), params.get("tipoLayout"));
			logger.debug("validacionGeneral : {}", validacionGeneral);
		}catch( Exception e){
			logger.error("Error validaExisteConfiguracionProv : {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	/**
	* Funcion para obtener el listado del alta del tramite
	* @param ntramite
	* @return Lista AltaTramiteVO - tramite Alta Tramite
	*/
	public String consultaConfiguracionLayout(){
		logger.debug("Entra a consultaConfiguracionLayout Params: {}", params);
		try {
			datosInformacionAdicional = siniestrosManager.consultaConfiguracionLayout(params.get("cdpresta"));
		}catch( Exception e){
			logger.error("Error consultaListadoAltaTramite: {}", e.getMessage(), e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String subirLayoutGeneral()
	{
		logger.debug(""
				+ "\n#############################"
				+ "\n###### subirLayoutGeneral ######"
				+ "\n censo "+censo+""
				+ "\n censoFileName "+censoFileName+""
				+ "\n censoContentType "+censoContentType+""
				+ "\n smap1 "+smap1
				);
		
		success = true;
		exito   = true;
		
		String ntramite=smap1.get("ntramite");
		if(StringUtils.isBlank(ntramite))
		{
			String timestamp = smap1.get("timestamp");
			//censo.renameTo(new File(this.rutaDocumentosTemporal+"/censo_"+timestamp));
			try {
            	FileUtils.copyFile(censo, new File(this.rutaDocumentosTemporal+"/layoutGral_"+timestamp));
            	logger.debug("archivo movido");
			} catch (Exception e) {
				logger.error("archivo NO movido", e);
			}
			
			logger.debug("censo renamed to: "+this.rutaDocumentosTemporal+"/layoutGral_"+timestamp);
		}
		
		logger.debug(""
				+ "\n###### subirLayoutGeneral ######"
				+ "\n################################"
				);
		return SUCCESS;
	}
	
	public String validaLayoutFormatoExcel(){
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### validaLayoutFormatoExcel ######"
				,"\n###### smap1="  , smap1
				,"\n###### olist1=" , olist1
				));
		success = true;
		exito   = true;
		try {
			//Recibimos el parametro de timestamp para validar el nombre del archivo
			String layoutTimestamp = smap1.get("timestamp");
			String cdpresta        = smap1.get("cdpresta");
			String layoutConf      = smap1.get("tipoLayout");
			
			layoutGral = new File(this.rutaDocumentosTemporal+"/layoutGral_"+layoutTimestamp);
			logger.debug("============== VALOR DEL NOMBRE DEL LAYOUT GENERAL ==========:  {}",layoutGral);
			List<CampoVO> campos = new ArrayList<CampoVO>();
			List<Map<String,String>>  datosInformacionLayout;
			List<Map<String,String>>  confLayoutExcel;
			FileInputStream input       = null;
			Workbook        workbook    = null;
			
			datosInformacionLayout = siniestrosManager.requiereConfiguracionLayoutProveedor(cdpresta, layoutConf);
			input       = new FileInputStream(layoutGral);
			workbook    = WorkbookFactory.create(input);
			
			if(exito&&workbook.getNumberOfSheets()!=1)
			{
				long etimestamp = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Favor de revisar el n\u00famero de hojas del layout #"+etimestamp;
				logger.error(respuesta);
			}else{
				for(int i = 0; i < Integer.parseInt(datosInformacionLayout.get(0).get("MAXREGISTRO"));i++){
					tipo = null;
					minimo = null;
					maximo = null;
					requerido = false;
					formatofecha = null;
					//Obtenemos la configuracion del layout configurado
					confLayoutExcel = siniestrosManager.obtieneConfiguracionLayoutExcel(cdpresta, layoutConf, String.valueOf(i+1));
					//logger.debug("Valor de confLayoutExcel =====> "+confLayoutExcel);
					try{
						tipo = confLayoutExcel.get(0).get("CVEFORMATO").toString();
					}
					catch(Exception ex){
						tipo = null;
					}
					
					try{
						minimo = confLayoutExcel.get(0).get("VALORMIN").toString();
					}
					catch(Exception ex){
						minimo = null;
					}
					
					try{
						maximo = confLayoutExcel.get(0).get("VALORMAX").toString();
					}
					catch(Exception ex){
						maximo = null;
					}
					
					try{
						requerido = Boolean.parseBoolean(confLayoutExcel.get(0).get("SWOBLIGA").toString());
					}
					catch(Exception ex){
						requerido =false;
					}
				
					try{
						formatofecha = confLayoutExcel.get(0).get("FORMATDATE").toString();
					}
					catch(Exception ex){
						formatofecha =null;
					}
					
					if(tipo.equalsIgnoreCase("F")){
						campos.add(new CampoVO(tipo, null, null, requerido,formatofecha));
					}else{
						campos.add(new CampoVO(tipo, Integer.parseInt(minimo), Integer.parseInt(maximo), requerido));
					}
				}
				
				String fullNameArchErrValida = rutaDocumentosTemporal + Constantes.SEPARADOR_ARCHIVO+"conversion_" + System.currentTimeMillis() + "_err.txt";
				
				File archErrVal = validadorFormatoContext.ejecutaValidacionesFormato(layoutGral, campos, fullNameArchErrValida, ValidadorFormatoContext.Strategy.VALIDACION_EXCEL);
				
				if(archErrVal != null && archErrVal.length() > 0) {
					String msjeError = "Archivo tiene errores de formato";
					String cadena;
					FileReader f = new FileReader(fullNameArchErrValida);
				    BufferedReader b = new BufferedReader(f);
				    StringBuilder bufferErroresLayout = new StringBuilder();
				    while((cadena = b.readLine())!=null) {
				    	respuesta= cadena+"\n";
				    	bufferErroresLayout.append(respuesta);
			    	}
				    b.close();
				    
					StringBuilder errorLayout      = new StringBuilder();
					respuesta       = errorLayout.append(bufferErroresLayout.toString()).append("\nError #").append(System.currentTimeMillis()).toString();
					exito   = false;
					success = false;
					
					if (archErrVal.delete()){
						logger.debug("El fichero archErrVal ha sido borrado satisfactoriamente");
					}else{
						logger.debug("El fichero archErrVal no puede ser borrado");
					}
					
					throw new ApplicationException(msjeError);
					
				}
			}
		} catch (Exception e) {
			Utils.manejaExcepcion(e);
		}
		logger.debug(""
				+ "\n###### validaLayoutFormatoExcel ######"
				+ "\n######################################"
				);
		
		
		return SUCCESS;
	}
	
	public String validaLayoutConfiguracionExcel()
	{
		this.session=ActionContext.getContext().getSession();
		logger.debug(Utils.log(
				 "\n#####################################"
				,"\n###### subirexcelConfiguracion ######"
				,"\n###### smap1="  , smap1
				,"\n###### olist1=" , olist1
				));
		success = true;
		exito   = true;
		try {
			//Recibimos el parametro de timestamp para validar el nombre del archivo
			String layoutTimestamp = smap1.get("timestamp");
			String cdpresta        = smap1.get("cdpresta");
			String layoutConf      = smap1.get("tipoLayout");
			
			layoutGral = new File(this.rutaDocumentosTemporal+"/layoutGral_"+layoutTimestamp);
			
			List<Map<String,String>>  datosInformacionLayout = siniestrosManager.requiereConfiguracionLayoutProveedor(cdpresta, layoutConf);
			String nombreLayout = null;
			String nombreLayoutConfirmado = smap1.get("nombreLayoutConfirmado");
				
			if(exito&&StringUtils.isBlank(nombreLayoutConfirmado)){
				FileInputStream input       = null;
				Workbook        workbook    = null;
				Sheet           sheet       = null;
				Long            inTimestamp = null;
				File            archivoTxt  = null;
				PrintStream     output      = null;
				
				try{
					input       = new FileInputStream(layoutGral);
					workbook    = WorkbookFactory.create(input);
					sheet       = workbook.getSheetAt(0);
					inTimestamp = System.currentTimeMillis();
					nombreLayout = "layout_"+inTimestamp+".txt";
					archivoTxt  = new File(this.rutaDocumentosTemporal +"/"+nombreLayout);
					output      = new PrintStream(archivoTxt);
				}
				catch(Exception ex){
					long etimestamp = System.currentTimeMillis();
					exito           = false;
					respuesta       = "Error al procesar el layout #"+etimestamp;
					respuestaOculta = ex.getMessage();
					logger.error(respuesta,ex);
				}
				
				if(exito)
				{
					//Iterate through each rows one by one
					logger.debug(""
							+ "\n##############################################"
							+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
							);
					
		            Iterator<Row> rowIterator        = sheet.iterator();
		            int           fila               = 0;
		            int           nConsulta          = 0;
		            StringBuilder bufferErroresCenso = new StringBuilder();
		            int           filasLeidas        = 0;
		            int           filasProcesadas    = 0;
		            int           filasError         = 0;
		            
		            Map<Integer,String>  totalConsultasAseg = new LinkedHashMap<Integer,String>();
					Map<Integer,Boolean> estadoConsultas = new LinkedHashMap<Integer,Boolean>();
					Map<Integer,Integer> errorConsultas   = new LinkedHashMap<Integer,Integer>();
					
					boolean[] gruposValidos = new boolean[olist1.size()];
					while (rowIterator.hasNext()&&exito) 
		            {
						Row           row            = rowIterator.next();
		                Date          auxDate        = null;
		                Cell          auxCell        = null;
		                StringBuilder bufferLinea    = new StringBuilder();
		                StringBuilder bufferLineaStr = new StringBuilder();
		                boolean       filaBuena      = true;
		                
		                if(Utils.isRowEmpty(row))
		                {
		                	break;
		                }
		                
		                fila       		= fila + 1;
		                filasLeidas 	= filasLeidas + 1;
		                double cdgrupo	= -1d;

		                // Empezamos a leer los campos del archivo de Excel
		                String fechaOcurrencia 	 		= null;
		                String aseguradoAfectado 		= null;
		                String coberturaAfectada 		= null;
		                String subcoberturaAfectada		= null;
		                String tipoConcepto 	 		= null;
		                String codigoConcepto 	 		= null;
		                String conversionTipoID  		= null;
		                int total 				 		= 1;
		                int totalVal 			 		= 1;
		                int errorOcurrencia 	 		= 0;
		                int errorGeneralCobSub      	= 0;
		                int errorCobertura	 	 		= 0;
		                int errorSubcobertura 	 		= 0;
		                int errorConcepto 		 		= 0;
		                String leyendaConcepto 			= null;
		                String campoFechaOcurrencia 	= null;
		                String campocodigoConcepto  	= null;
		                String campoCPTs 				= null;
		                String campocodigoCobertura 	= null;
		                String campocodigoSubCobertura  = null;
		                
		                logger.debug("Valor de datosInformacionLayout :{} datosInformacionLayout.size:{}",datosInformacionLayout,datosInformacionLayout.size());
		                for(int i = 0; i < datosInformacionLayout.size();i++){
		                	int celdaPrincipal = Integer.parseInt(datosInformacionLayout.get(i).get("CVEEXCEL").toString())-1;
		                	auxCell = row.getCell(celdaPrincipal);
		                	
		                	try{
		                		//1.- Validamos si la clave del Formato es Fecha
		                		if(datosInformacionLayout.get(i).get("CVEFORMATO").toString().equalsIgnoreCase("F")){
		                			String conversionFechaString = obtieneValor(auxCell, CampoVO.FECHA,datosInformacionLayout.get(i).get("DESCFECHA").toString(),  "dd/MM/yyyy");
		                			
		                			//Guardamos la informacion de la Fecha de Ocurrencia y el campo por si hay algun error
		                			if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("FECHA OCURRENCIA")){
		                				campoFechaOcurrencia =  datosInformacionLayout.get(i).get("DESCRIPC").toString();
		                				fechaOcurrencia      = conversionFechaString;
		                			}
		                			//Si alguna de las fecha es null o vacia le asignaremos una por default
		                			if(datosInformacionLayout.get(i).get("SWOBLIGA").toString().equalsIgnoreCase("N") && StringUtils.isBlank(conversionFechaString)){
	                					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	                					Date date = formatter.parse("01/01/1900");
	                					bufferLinea.append(
			                				auxCell!=null?renderFechas.format(date)+"|":"|"
				                		);
	                				}else{
	                					bufferLinea.append(conversionFechaString+"|");
	                				}
		                		}else if(datosInformacionLayout.get(i).get("CVEFORMATO").toString().equalsIgnoreCase("A")){
		                			//2.- Validamos si la clave del Formato es Alfanumerico
		                			String conversionString = obtieneValor(auxCell, CampoVO.ALFANUMERICO, null, null);
		                			String cadenaModificada = conversionString.trim().
	                						replaceAll("\u00E1","a").replaceAll("\u00E9","e").replaceAll("\u00ED","i").replaceAll("\u00F3","o").
	                						replaceAll("\u00FA","u").replaceAll("\u00C1","A").replaceAll("\u00C9","E").replaceAll("\u00CD","I").
	                						replaceAll("\u00D3","O").replaceAll("\u00DA","U").replaceAll("\\*","");
		                			
		                			if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CVE. ASEGURADO")){
	                					HashMap<String, Object> paramPersona = new HashMap<String, Object>();
	                					paramPersona.put("pv_cdideext_i",cadenaModificada);
	                					String existePersona = siniestrosManager.validaPersonaSisaSicaps(paramPersona);
	                					if(Integer.parseInt(existePersona) > 0){
	                						aseguradoAfectado = "G"+existePersona;
	                						bufferLinea.append(
				                				auxCell!=null?"G"+existePersona+"|":"|"
					                		);
	                					}else{
	                						throw new Exception("El asegurado no se encuentra en SICAPS.");
	                					}
	                				}else if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CLAVE ASEGURADO")){
	                					HashMap<String, Object> paramPersona = new HashMap<String, Object>();
	                					paramPersona.put("pv_cdideext_i",cadenaModificada.replaceAll("G", "") );
	                					String existePersona = siniestrosManager.validaPersonaSisaSicaps(paramPersona);
	                					if(Integer.parseInt(existePersona) > 0){
	                						aseguradoAfectado = "G"+existePersona;
	                						bufferLinea.append(
				                				auxCell!=null?"G"+existePersona+"|":"|"
					                		);
	                					}else{
	                						throw new Exception("El asegurado no se encuentra en SICAPS.");
	                					}
	                				}
		                			else{
	                					if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("COBERTURA")){
	                						campocodigoCobertura =  datosInformacionLayout.get(i).get("DESCRIPC").toString();
	                						if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("COBERTURA") &&
	                    							datosInformacionLayout.get(i).get("SWOBLIGA").toString().equalsIgnoreCase("N") && 
	                    							StringUtils.isBlank(cadenaModificada.trim())){
	    	                						coberturaAfectada ="Medicamentos";
	    	                						bufferLinea.append("Medicamentos"+"|");
	    		                				}else{
	    		                					coberturaAfectada =cadenaModificada.trim();
	    		                					bufferLinea.append(auxCell!=null?cadenaModificada.trim()+"|":"|");
	    		                				}
	                					}else if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("SUBCOBERTURA")){
	                						campocodigoSubCobertura =  datosInformacionLayout.get(i).get("DESCRIPC").toString();
	                						if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("SUBCOBERTURA") &&
	                    							datosInformacionLayout.get(i).get("SWOBLIGA").toString().equalsIgnoreCase("N") && 
	                    							StringUtils.isBlank(cadenaModificada.trim())){
	    		                					//Si la SUBCOBERTURA es opcional o no viene le asignamos la de MEDICAMENTOS (FARMACIAS)
	    		                					subcoberturaAfectada ="Medicamentos - Todos Los Medicamentos Recetados Por Los Medicos De La Red";
	    		                					bufferLinea.append("Medicamentos - Todos Los Medicamentos Recetados Por Los Medicos De La Red"+"|");
	                						}else{
    		                					subcoberturaAfectada =cadenaModificada.trim();
    		                					bufferLinea.append(auxCell!=null?cadenaModificada.trim()+"|":"|");
    		                				}
	                					}else{
	                						bufferLinea.append(auxCell!=null?cadenaModificada.trim()+"|":"|");
	                					}
	                				}
	                				
		                			//Hacemos la asignacion para cada una de ellas para validar que existe
	                				if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CLAVE ASEGURADO")){
	                					aseguradoAfectado = cadenaModificada.trim();
	                				}
	                				
		                			
	                				if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("ID. CONCEPTO")){ //CPT  UB HCPC
	                					tipoConcepto = cadenaModificada.trim();
	                				}
		                			
	                				if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CVE. CONCEPTO")){
	                					codigoConcepto = cadenaModificada.trim();
	                					campocodigoConcepto =  datosInformacionLayout.get(i).get("DESCRIPC").toString();
	                				}
		                		}else{
		                			//3.- Validamos si la clave del Formato es Numerico o decimal
		                			String conversionString = obtieneValor(auxCell, CampoVO.ALFANUMERICO, null, null);
		                			logger.debug("Valor ==>>>: {}",datosInformacionLayout.get(i).get("DESCEXCEL").toString());
		                			if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("IVA")){
		                				 DecimalFormat df = new DecimalFormat("####################.###");
		                				 bufferLinea.append(df.format(Double.parseDouble(conversionString))+"|");
		                			}else{
		                				if(Double.parseDouble(conversionString) > 0){
			                				logger.debug("Valor de conversion ==> :{}",conversionString);
			                				DecimalFormat df = new DecimalFormat("####################.###");
			                				bufferLinea.append(df.format(Double.parseDouble(conversionString))+"|");
				                			//bufferLinea.append(Double.parseDouble(conversionString)+"|");
			                			}else{
			                				throw new Exception("Error en los importes y cantidades");
			                			}
		                			}
		                		}
		                		
		                		//Validacion de la fecha de ocurrencia
		                		if(fechaOcurrencia!= null && aseguradoAfectado != null && coberturaAfectada != null && subcoberturaAfectada != null && total <= 1){
		                			total = total+1;
		                			HashMap<String, Object> paramPersona = new HashMap<String, Object>();
                					paramPersona.put("pv_feocurre_i",renderFechas.parse(fechaOcurrencia));
                					paramPersona.put("pv_cdperson_i",aseguradoAfectado.replaceAll("G", "")); 
                					String feocurreAsegurado = siniestrosManager.validaFeocurreAsegurado(paramPersona);
                					if(Integer.parseInt(feocurreAsegurado) == 0){
                						errorOcurrencia = 1;
                						throw new Exception("El rango de la fecha no se encuentra en la p&oacute;liza");
                					}else{
                						HashMap<String, Object> paramsCausaSini = new HashMap<String, Object>();
                						paramsCausaSini.put("pv_cdperson_i",aseguradoAfectado.replaceAll("G", ""));
                						paramsCausaSini.put("pv_feocurre_i",renderFechas.parse(fechaOcurrencia));
                						paramsCausaSini.put("pv_cobertura_i",coberturaAfectada);
                						paramsCausaSini.put("pv_subcobertura_i",subcoberturaAfectada);
                						
                						datosInformacionAdicional = siniestrosManager.listaConsultaInfAseguradoCobSubCoberturas(paramsCausaSini);
                						
                						
                						logger.debug("CDTIPSIT:{} ",datosInformacionAdicional.get(0).get("CDTIPSIT"));
                						logger.debug("CDRAMO:{} ",datosInformacionAdicional.get(0).get("CDRAMO"));
                						logger.debug("COBERTURA:{} ",datosInformacionAdicional.get(0).get("COBERTURA"));
                						logger.debug("SUBCOBERTURA:{} ",datosInformacionAdicional.get(0).get("SUBCOBERTURA"));
                						
                						if(datosInformacionAdicional.get(0).get("COBERTURA") ==null || datosInformacionAdicional.get(0).get("COBERTURA")==""){
                							errorGeneralCobSub = 1;
                							errorCobertura     = 1;
                							throw new Exception("Error en la Cobertura");
                						}else if(datosInformacionAdicional.get(0).get("SUBCOBERTURA") ==null || datosInformacionAdicional.get(0).get("SUBCOBERTURA")==""){
                							errorGeneralCobSub = 1;
                							errorSubcobertura  = 1;
                							throw new Exception("Error en la SubCobertura");
                						}else{
                							bufferLinea.append(datosInformacionAdicional.get(0).get("CDRAMO")+"|");
                						}
                					}
		                		}
		                		//Validacion de los CPT, UB y HCPC
		                		if(tipoConcepto!= null && codigoConcepto != null && totalVal <= 1){
		                			totalVal = totalVal+1;
		                			if(tipoConcepto.equalsIgnoreCase("CPT")){
		                				conversionTipoID = "1";
		                			}else if(tipoConcepto.equalsIgnoreCase("UB")){
		                				conversionTipoID = "2";
		                			}else{
		                				conversionTipoID = "3";
		                			}
		                			HashMap<String, Object> paramExiste = new HashMap<String, Object>();
		                			paramExiste.put("pv_idconcep_i",conversionTipoID);
		                			paramExiste.put("pv_descripc_i",codigoConcepto);									
		                			String existeCodigoConcepto = siniestrosManager.validaExisteCodigoConcepto(paramExiste);
		                			if(existeCodigoConcepto.equalsIgnoreCase("N")){
		                				errorConcepto = 1;
		                				leyendaConcepto = tipoConcepto+" "+codigoConcepto;
		                				throw new Exception("Error en el concepto");
		                			}
		                		}
		                	}
		                	catch(Exception ex){
		                		filaBuena = false;
		                		logger.debug("Valor de la excepcion ==>:{}",ex);
		                		if(errorOcurrencia == 1){
		                			bufferErroresCenso.append(Utils.join("La Fecha de ocurrencia no se encuentra dentro de las fechas de la p\u00F3liza .\nError en el campo FECHA OCURRENCIA "+campoFechaOcurrencia+" de la fila ",fila," "));
		                		}
		                		if(errorConcepto == 1){
		                			bufferErroresCenso.append(Utils.join("El "+leyendaConcepto+" no se encuentra dado de alta.\nError en el campo CVE. CONCEPTO "+campocodigoConcepto+" de la fila ",fila," "));
		                		}
		                		if(errorGeneralCobSub ==1){
		                			if(errorCobertura == 1){
		                				bufferErroresCenso.append(Utils.join("La Cobertura no es correcta.\nError en el campo COBERTURA "+campocodigoCobertura+" de la fila ",fila," "));
		                			}else{
		                				bufferErroresCenso.append(Utils.join("La Subcobertura no es correcta.\nError en el campo SUBCOBERTURA "+campocodigoCobertura+" de la fila ",fila," "));
		                			}
		                		}
		                		else{
		                			if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CLAVE ASEGURADO")){
			                			bufferErroresCenso.append(Utils.join("El asegurado no existe o no se encuentra vigente.\nError en el campo "+datosInformacionLayout.get(i).get("DESCEXCEL").toString()+" "+datosInformacionLayout.get(i).get("DESCRIPC").toString()+" de la fila ",fila," "));
			                		}else if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("CANTIDAD CONCEPTO")){
			                			bufferErroresCenso.append(Utils.join("La catidad tienen que ser mayor que cero.\nError en el campo "+datosInformacionLayout.get(i).get("DESCEXCEL").toString()+" "+datosInformacionLayout.get(i).get("DESCRIPC").toString()+" de la fila ",fila," "));
			                		}else if(datosInformacionLayout.get(i).get("DESCEXCEL").toString().equalsIgnoreCase("PRECIO CONCEPTO")){
			                			bufferErroresCenso.append(Utils.join("El precio tienen que ser mayor que cero.\nError en el campo "+datosInformacionLayout.get(i).get("DESCEXCEL").toString()+" "+datosInformacionLayout.get(i).get("DESCRIPC").toString()+" de la fila ",fila," "));
			                		}
			                		else{
			                			bufferErroresCenso.append(Utils.join("Error en el campo "+datosInformacionLayout.get(i).get("DESCEXCEL").toString()+" "+datosInformacionLayout.get(i).get("DESCRIPC").toString()+" de la fila ",fila," "));
			                		}
		                		}
			                }
			                finally{
			                	bufferLineaStr.append(Utils.join(extraerStringDeCelda(row.getCell(celdaPrincipal)),"-"));
			                }
		                }
		                
		                if(datosInformacionLayout.size() == 15){
		                	bufferLinea.append(cdpresta+"|"+layoutTimestamp+"|");
		                }else{
		                	bufferLinea.append("|"+cdpresta+"|"+layoutTimestamp+"|");
		                }
		                
		                nConsulta++;
	                	totalConsultasAseg.put(nConsulta,"");
	                	estadoConsultas.put(nConsulta,true);
	                	
	                	if(filaBuena) {
		                	totalConsultasAseg.put(nConsulta,Utils.join(totalConsultasAseg.get(nConsulta),bufferLinea.toString(),"\n"));
		                	filasProcesadas = filasProcesadas + 1;
		                }
		                else {
		                	filasError = filasError + 1;
		                	bufferErroresCenso.append(Utils.join(": ",bufferLineaStr.toString(),"\n"));
		                	estadoConsultas.put(nConsulta,false);
		                	if(!errorConsultas.containsKey(nConsulta)) {
		                		errorConsultas.put(nConsulta,fila);
		                	}
		                }		                	
		            } // fin del while
					
					logger.debug("VALOR DEL EXITO ====> "+exito);
					logger.debug("filasError ====>"+filasError);

					if(exito) {
		            	logger.debug("total Consultas: {}\nEstado Consultas: {}\nError Consultas: {}"
			            		,totalConsultasAseg,estadoConsultas,errorConsultas);
			            
			            for(Entry<Integer,Boolean>en:estadoConsultas.entrySet()){
			            	int     n = en.getKey();
			            	boolean v = en.getValue();
			            	if(v){
			            		output.print(totalConsultasAseg.get(n));
			            	}
			            }
			            
						smap1.put("erroresCenso"    , bufferErroresCenso.toString());
						smap1.put("filasLeidas"     , Integer.toString(filasLeidas));
						smap1.put("filasProcesadas" , Integer.toString(filasProcesadas));
						smap1.put("filasErrores"    , Integer.toString(filasError));
					}
					
					if(exito)
		            {
		            	try
		            	{
		            		input.close();
		            		output.close();
		            	}
		            	catch(Exception ex)
		            	{
		            		long etimestamp = System.currentTimeMillis();
		            		exito           = false;
		            		respuesta       = "Error al transformar el archivo #"+etimestamp;
		            		respuestaOculta = ex.getMessage();
		            		logger.error(respuesta,ex);
		            	}
		            }
					
					logger.debug(""
							+ "\n###### "+archivoTxt.getAbsolutePath()+" ######"
							+ "\n##############################################"
					);
					
					if(exito){
						if(filasError > 0){
							exito = false;
						}else{
							exito = FTPSUtils.upload
									(
										this.dominioServerLayouts,
										this.userServerLayouts,
										this.passServerLayouts,
										archivoTxt.getAbsolutePath(),
										this.directorioServerLayouts+"/"+nombreLayout
								    )
									&&FTPSUtils.upload
									(
										this.dominioServerLayouts,
										this.userServerLayouts,
										this.passServerLayouts,
										archivoTxt.getAbsolutePath(),
										this.directorioServerLayouts+"/"+nombreLayout
									);
							
							if(!exito)
							{
								long etimestamp = System.currentTimeMillis();
								exito           = false;
								respuesta       = "Error al transferir archivo al servidor #"+etimestamp;
								respuestaOculta = respuesta;
								logger.error(respuesta);
							}
							
							if(exito)
							{
								try
								{
									logger.debug("Entra a la opcion del guardado ===> ");
									cotizacionManager.guardarLayoutGenerico(nombreLayout);
									logger.debug("<=== Sale a la opcion del guardado");
								}
								catch(Exception ex)
								{
									long etimestamp = System.currentTimeMillis();
									exito           = false;
									respuesta       = "Error al guardar los datos #"+etimestamp;
									respuestaOculta = ex.getMessage();
									logger.error(respuesta,ex);
									
								}
							}
						}
					}
				}// Fin del iterator
			}
			
			if(exito)
			{
				respuesta       = "Se ha complementado el guardado del layout";
				success = true;
				exito   = true;
				respuestaOculta = "Todo OK";
			}
		} catch (Exception e) {
			Utils.manejaExcepcion(e);
		}
		logger.debug(""
				+ "\n###### subirexcelConfiguracion ######"
				+ "\n#####################################"
				);
		
		
		return SUCCESS;
	}

	public String consultaLayoutConfigurados(){
		logger.debug("Entra a consultaDatosConfiguracionLayout params de entrada :{} ",params);
		try {
			datosValidacion = siniestrosManager.getConsultaLayoutConfigurados(params.get("descLayout"));
			logger.debug("Respuesta datosValidacion : {}",datosValidacion);
		}catch( Exception e){
			logger.error("Error al obtener consultaDatosConfiguracionLayout : {}", e.getMessage(), e);
			return SUCCESS;
		}
		setSuccess(true);
		return SUCCESS;
	}
    
    
	/**
	  * Obtiene el valor de una celda
	  * @param celda Celda que contiene el valor
	  * @param tipoDato tipo de dato de la celda "A" Alfanumerico, "N" Numerico, "F" Fecha, "P" Porcentaje
	  * @param dateFormat Cadena con el formato de fecha, nulo si la celda no es tipo "F"
	  * @return valor de la celda en String
	  * @throws Exception  si falla la obtencion del valor
	  */
	private String obtieneValor(Cell celda, String tipoDato, String dateFormatIn, String dateFormatOut) throws Exception {
		String strValor = null;
		if(celda==null) {
			return "";
		}
		if (tipoDato.equals(CampoVO.FECHA)) {
			logger.debug("Cell type:" + celda.getCellType());
			if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
				strValor = celda.getStringCellValue();
				SimpleDateFormat df;
				if (dateFormatIn == null) {
					df = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					df = new SimpleDateFormat(dateFormatIn);
				}
				if (dateFormatOut == null) {
					df = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					df = new SimpleDateFormat(dateFormatOut);
				}
				DateFormat formatter = new SimpleDateFormat(dateFormatIn, Locale.US);
				Date date = formatter.parse(strValor);
				strValor = df.format(date);
				
			} else if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				SimpleDateFormat df;
				if (dateFormatOut == null) {
					df = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					df = new SimpleDateFormat(dateFormatOut);
				}
				strValor = df.format(celda.getDateCellValue());
			}
		} else {
			celda.setCellType(Cell.CELL_TYPE_STRING);
			strValor = celda.getStringCellValue();
		}
		return strValor;
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
	
	/////////////////////////////////////////////////////7
	public Map<String, String> getSmap1() {
		return smap1;
	}
	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}
	
	public String getSmap1Json(){
		String r=null;
		try{
			r=JSONUtil.serialize(smap1);
		}
		catch (JSONException ex){
			logger.error("error al convertir smap a json",ex);
		}
		return r;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
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
	public File getCenso() {
		return censo;
	}
	public void setCenso(File censo) {
		this.censo = censo;
	}
	public List<Map<String, Object>> getOlist1() {
		return olist1;
	}
	public void setOlist1(List<Map<String, Object>> olist1) {
		this.olist1 = olist1;
	}
	public List<Map<String, String>> getDatosValidacion() {
		return datosValidacion;
	}
	
	public void setDatosValidacion(List<Map<String, String>> datosValidacion) {
		this.datosValidacion = datosValidacion;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}


	public HashMap<String, String> getParams() {
		return params;
	}


	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}


	public KernelManagerSustituto getKernelManagerSustituto() {
		return kernelManagerSustituto;
	}


	public void setKernelManagerSustituto(KernelManagerSustituto kernelManagerSustituto) {
		this.kernelManagerSustituto = kernelManagerSustituto;
	}


	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}


	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}


	public Ice2sigsService getIce2sigsService() {
		return ice2sigsService;
	}


	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public List<HashMap<String, String>> getDatosTablas() {
		return datosTablas;
	}

	public void setDatosTablas(List<HashMap<String, String>> datosTablas) {
		this.datosTablas = datosTablas;
	}

	public List<Map<String, String>> getDatosInformacionAdicional() {
		return datosInformacionAdicional;
	}

	public void setDatosInformacionAdicional(List<Map<String, String>> datosInformacionAdicional) {
		this.datosInformacionAdicional = datosInformacionAdicional;
	}
	
    public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getValidacionGeneral() {
		return validacionGeneral;
	}

	public void setValidacionGeneral(String validacionGeneral) {
		this.validacionGeneral = validacionGeneral;
	}
	
	public String getRutaDocumentosTemporal() {
		return rutaDocumentosTemporal;
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

}
