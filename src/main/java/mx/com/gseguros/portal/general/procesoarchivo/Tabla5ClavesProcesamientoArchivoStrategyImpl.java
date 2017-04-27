package mx.com.gseguros.portal.general.procesoarchivo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.general.model.RespuestaVO;
import mx.com.gseguros.portal.general.validacionformato.CampoVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.wizard.dao.TablasApoyoDAO;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@Service("procesaArchivoTabla5Claves")
public class Tabla5ClavesProcesamientoArchivoStrategyImpl implements ProcesamientoArchivoStrategy {
	
	private static Logger logger = Logger.getLogger(Tabla5ClavesProcesamientoArchivoStrategyImpl.class);
	
	private final static int TOTAL_COLUMNAS = 33;
	
	private final static String CARACTER_SEPARADOR_CAMPOS = "|";
	
	private static String NEW_LINE = System.getProperty("line.separator");
	
	@Autowired
	@Value("${dominio.server.layouts}")
	private String hostName;
	
	@Autowired
	@Value("${user.server.layouts}")
	private String username;
	
	@Autowired
	@Value("${pass.server.layouts}")
	private String password;
	
	@Autowired
	@Value("${ruta.documentos.temporal}")
	private String temporalPath;
	
	@Autowired
	@Value("${directorio.server.layouts}")
	private String remoteFilePath;
	
	
	public enum TipoTabla {
		
		UNA(1),
		CINCO(5);

		private Integer codigo;

		private TipoTabla(Integer codigo) {
			this.codigo = codigo;
		}

		public Integer getCodigo() {
			return codigo;
		}
	}
	

	@Autowired
	TablasApoyoDAO tablasApoyoDAO;
	
	
	public Tabla5ClavesProcesamientoArchivoStrategyImpl() {
		super();
	}
	
	public Tabla5ClavesProcesamientoArchivoStrategyImpl(String localFilePath, Integer nmtabla) {
		super();
		//this.nmtabla = nmtabla;
	}

	
	@Override
	public RespuestaVO ejecutaProcesamiento(File archivoOrigen, List<CampoVO> campos, Integer nmtabla, Integer tipoproceso, String feCierre) throws Exception {
		
		RespuestaVO respVO = null;
		File fileCSVLocal = null;
		try {
			
			//Convertir Excel a CSV:
			fileCSVLocal = new File(temporalPath + Constantes.SEPARADOR_ARCHIVO + "conversion_" + System.currentTimeMillis() + ".csv");
			logger.debug("Creando archivo fileCSV=" + temporalPath + " name=" + fileCSVLocal.getName());
			
			BufferedWriter writerCSV = new BufferedWriter( new FileWriter(fileCSVLocal));
			//FileInputStream input    = new FileInputStream(archivoOrigen);
			//XSSFWorkbook workbook = new XSSFWorkbook(input);
			//XSSFSheet       sheet    = workbook.getSheetAt(0);
			Workbook workbook = WorkbookFactory.create(archivoOrigen);
			Sheet sheet = workbook.getSheetAt(0);
			
			//Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext()) {
	        	
	            Row row   = rowIterator.next();
	            
	            if(Utils.isRowEmpty(row))
	            {
	            	break;
	            }
	            
	            for (int colIndex = 0; colIndex < TOTAL_COLUMNAS; colIndex++) {
	            	
	            	Cell celdaActual = row.getCell(colIndex);
	            	String strValor = null;
	            	
	            	if(celdaActual != null) {
	            		
	            		if(campos.get(colIndex).getType().equals(CampoVO.FECHA)) {
	                    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	                    	strValor  = df.format(celdaActual.getDateCellValue());
	                	} else {
	                		celdaActual.setCellType(Cell.CELL_TYPE_STRING);
	                		strValor = celdaActual.getStringCellValue();
	                	}
	            	}
	            		
	            	if(strValor != null) {
	            		writerCSV.write(strValor);
	            	}
	            	//logger.debug("***** colIndex=" + colIndex + "***** total=" + TOTAL_COLUMNAS);
	            	
	            	//Para no agregar el separador al final del archivo CSV:
	            	if(colIndex < TOTAL_COLUMNAS-1) {
	            		writerCSV.write(CARACTER_SEPARADOR_CAMPOS);
	            	}
				}
	            
	            //Se agrega caracter de nueva linea:
	            if(rowIterator.hasNext()) {
	            	writerCSV.write(NEW_LINE);
	            }
	        }
	        writerCSV.close();
	        
			//Subir archivo CSV a servidor de BD via FTP
	        boolean fileUploaded = FTPSUtils.upload(hostName, username, password, fileCSVLocal.getAbsolutePath(), remoteFilePath + Constantes.SEPARADOR_ARCHIVO + fileCSVLocal.getName());
	        		
			if(fileUploaded) {
				
				//Ejecutar SP que realice la carga del archivo CSV:
				String resp = tablasApoyoDAO.cargaMasiva(nmtabla, TipoTabla.CINCO.getCodigo(), fileCSVLocal.getName(), CARACTER_SEPARADOR_CAMPOS, tipoproceso, feCierre);
				respVO = new RespuestaVO(true, resp);
				
			} else {
				respVO = new RespuestaVO(false, "Error en subir el Secure FTP");
			}
			
		} catch (DaoException e) {
			logger.error("Error en la carga masiva", e);
			respVO = new RespuestaVO(false, e.getMessage());
		} catch (Exception e) {
			logger.error("Error en la carga masiva", e);
			respVO = new RespuestaVO(false, "Error en el guardado");
		} finally {
			// Borrar archivo CSV intermedio en servidor de aplicaciones:
			if(fileCSVLocal != null) {
				fileCSVLocal.delete();
			}
			// Borrar archivo CSV intermedio en servidor de BD:
			FTPSUtils.delete(hostName, username, password, remoteFilePath + Constantes.SEPARADOR_ARCHIVO + fileCSVLocal.getName());
		}
		
		return respVO;
	}
	
}