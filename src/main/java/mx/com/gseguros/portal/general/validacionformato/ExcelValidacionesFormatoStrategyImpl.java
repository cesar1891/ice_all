package mx.com.gseguros.portal.general.validacionformato;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelValidacionesFormatoStrategyImpl implements ValidacionesFormatoStrategy {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ExcelValidacionesFormatoStrategyImpl.class);
	
	private static String NEW_LINE = System.getProperty("line.separator");
	

	@Override
	public File ejecutaValidacionesFormato(File archivo, List<CampoVO> campos, String nombreArchivoErrores) throws Exception {
		
		File fileErrors = new File(nombreArchivoErrores);
		BufferedWriter writerErrors = new BufferedWriter( new FileWriter(fileErrors));
		
		FileInputStream input = new FileInputStream(archivo);
		//XSSFWorkbook workbook = new XSSFWorkbook(input);
		//XSSFSheet       sheet = workbook.getSheetAt(0);
		Workbook workbook = WorkbookFactory.create(input);
		Sheet sheet = workbook.getSheetAt(0);
		
		//Iterate through each rows:
        Iterator<Row> rowIterator = sheet.iterator();
        int rowIndex = 0;
        while (rowIterator.hasNext()) {
            Row row   = rowIterator.next();
            int colIndex = 0;
            
            if(Utils.isRowEmpty(row))
            {
            	break;
            }
            
            for (CampoVO campo: campos) {
            	
            	Cell celdaActual = row.getCell(colIndex);
            	if(celdaActual != null) {
            		
            		// Se obtiene valor de celda:
            		String strValor = null;
            		try {
            			strValor = obtieneValor(celdaActual, campo);
					} catch (Exception e) {
						writerErrors.write(
								new StringBuilder()
									.append("fila ").append((celdaActual.getRowIndex()+1))
									.append(", col ").append((celdaActual.getColumnIndex()+1))
		        					.append("\tEl valor no corresponde al tipo de celda")
		        					.append(NEW_LINE).toString());
						
						// Pasamos a validar el siguiente campo:
						colIndex++;
						continue;
					}
            		
            		// Se valida formato del campo:
            		if(!validaFormato(campo)) {
            			writerErrors.write(
                    			new StringBuilder()
                    				.append("fila ").append((celdaActual.getRowIndex()+1))	
                    				.append(", col ").append((celdaActual.getColumnIndex()+1))
			    					.append("\tFormato inv\u00E1lido")
			    					.append(NEW_LINE).toString());
            		}
            		
                	// Validacion de nulos:
                	if(!validaNulos(strValor, campo)) {
                		writerErrors.write(
                				new StringBuilder()
                					.append("fila ").append((rowIndex+1))
    	            				.append(", col ").append((colIndex+1))
    								.append("\tValor no debe ser nulo, el valor le\u00EDdo es ").append(strValor)
    								.append(NEW_LINE).toString());
                	}
            		
            		// Se valida longitud del valor:
            		if(!campo.isNullable() && !validaLongitud(strValor, campo)) {
        				writerErrors.write(
            					new StringBuilder()
            						.append("fila ").append((celdaActual.getRowIndex()+1))
            						.append(", col ").append((celdaActual.getColumnIndex()+1))
		        					.append("\tLongitud debe ser entre ").append(campo.getMinLength())
		        					.append("\ty ").append(campo.getMaxLength())
		        					.append(", longitud le\u00EDda: ").append(strValor.length())
		        					.append(NEW_LINE).toString());
        			}
                    
            	} else {
            		// Validacion de nulos:
                	if(!campo.isNullable()) {
                		writerErrors.write(
                				new StringBuilder()
                					.append("fila ").append((rowIndex+1))
    	            				.append(", col ").append((colIndex+1))
    								.append("\tValor no debe ser nulo")
    								.append(NEW_LINE).toString());
                	}
            	}
            	colIndex++;
			}
            writerErrors.flush();
            rowIndex++;
        }
        writerErrors.close();
        
        if(fileErrors.length() == 0) {
        	fileErrors.delete();
        	fileErrors = null;
        }
        return fileErrors;
	}
	
	
	/**
	 * Valida si el campo puede ser nulo
	 * @param valor Valor a probar
	 * @param campo Configuracion del campo
	 * @return true si pasa la validacion, false si no
	 */
	private boolean validaNulos(String valor, CampoVO campo) {
		boolean exito = true;
		if(!campo.isNullable() && StringUtils.isBlank(valor)) {
			exito = false;
		} 
		return exito;
	}
    
	
	/**
	 * Obtiene el valor de una celda
	 * @param celda Celda que contiene el valor
	 * @param campo Configuracion del campo
	 * @return Valor
	 * @throws Exception si falla la obtencion del valor
	 */
    private String obtieneValor(Cell celda, CampoVO campo) throws Exception {
    	String strValor = null;
		if(campo.getType().equals(CampoVO.FECHA)) {
        	//SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			logger.debug("Cell type:" + celda.getCellType());
			if(celda.getCellType() == Cell.CELL_TYPE_STRING){
				strValor = celda.getStringCellValue();
			} else if(celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				SimpleDateFormat df;
				if(campo.getDateFormat() == null) {
					df = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					df = new SimpleDateFormat(campo.getDateFormat());
				}
	        	strValor  = df.format(celda.getDateCellValue());
			}
			///////////
			
    	} else {
    		celda.setCellType(Cell.CELL_TYPE_STRING);
    		strValor = celda.getStringCellValue();
    	}
		return strValor;
    }
    
            
    /**
     * Valida el formato de un campo
     * @param campo Configuracion del campo
     * @return true si el formato es correcto, false si no
     */
    private boolean validaFormato(CampoVO campo) {
    	
    	boolean exito = true;
		if (!campo.getType().equals(CampoVO.ALFANUMERICO)
				&& !campo.getType().equals(CampoVO.NUMERICO)
				&& !campo.getType().equals(CampoVO.PORCENTAJE)
				&& !campo.getType().equals(CampoVO.FECHA)) {
			exito = false;
		}
		return exito;
    }
    
    
	/**
	 * Valida la longitud de un valor con respecto a un campo
	 * @param valor Valor a probar
	 * @param campo Configuracion del campo
	 * @return true si la longitud es correcta, false si no
	 */
	private boolean validaLongitud(String valor, CampoVO campo) {
		
		boolean exito = true;
		if (campo.getType().equals(CampoVO.ALFANUMERICO)
				|| campo.getType().equals(CampoVO.NUMERICO)
				|| campo.getType().equals(CampoVO.PORCENTAJE)) {
			
    		if(valor != null 
    				&& (valor.length() < campo.getMinLength() || valor.length() > campo.getMaxLength()) ) {
    			exito = false;
    		}
        } 
		return exito;
	}
	
}