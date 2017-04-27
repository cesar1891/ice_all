package mx.com.gseguros.portal.general.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.service.ImpresionService;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.utils.cmd.SystemCommandExecutor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImpresionServiceUNIXImpl implements ImpresionService {
	
	final static Logger logger = LoggerFactory.getLogger(ImpresionServiceUNIXImpl.class);
	
	public static void main(String[] args) {
		
		ImpresionServiceUNIXImpl imprSrvImpl = new ImpresionServiceUNIXImpl();
		try {
			imprSrvImpl.imprimeDocumento("/biosnet logo.png", "Microsoft XPS Document Writer", 3, null, false);
		} catch (Exception e) {
			logger.error("Error en la impresion", e);
		}
	}
	
	
	@Override
	public void imprimeDocumento(String documento, String nombreImpresora, int numCopias, String bandeja, boolean esDuplex) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ imprimeDocumento @@@@@@"
				,"\n@@@@@@ documento="       , documento
				,"\n@@@@@@ nombreImpresora=" , nombreImpresora
				,"\n@@@@@@ numCopias="       , numCopias
				,"\n@@@@@@ bandeja="         , bandeja
				,"\n@@@@@@ esDuplex="        , esDuplex
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
        
		// Se valida la existencia / instalacion de la impresora a utilizar:
		//validaExistenciaImpresora(nombreImpresora); // Se comenta, ya que no reconocia algunas impresoras en Produccion
		
		// Se valida la existencia del documento a imprimir:
		validaExistenciaDocumento(documento);
		
		// Generamos el comando a invocar:
	    List<String> commands = new ArrayList<String>();
	    commands.add("/bin/sh");
	    commands.add("-c");
	    // Se forma el comando de impresion:
		StringBuilder sbComando = new StringBuilder("lpr")
				.append(" -P ").append(nombreImpresora)
				.append(" '").append(documento).append("'")
				.append(" -#").append(numCopias);
	    // Si se especifica la bandeja de impresion, se agrega al comando:
	    if(StringUtils.isNotBlank(bandeja)) {
	    	sbComando.append(" -o InputSlot=").append(bandeja);
	    }
	    // Si se especifica impresion de ambos lados de la hoja, se agrega al comando:
	    if(esDuplex) {
	    	sbComando.append(" -o sides=two-sided-long-edge");
	    }
	    commands.add(sbComando.toString());

	    // execute the command
	    SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
	    int result = commandExecutor.executeCommand();
	    logger.debug("Resultado numerico del comando {} : {}", sbComando, result);

	    // get the stdout and stderr from the command that was run
	    StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
	    StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
	    
	    // print the stdout and stderr:
	    logger.debug("STDOUT:");
	    logger.debug(stdout.toString());
	    logger.debug("STDERR:");
	    logger.debug(stderr.toString());
	    
	    // Si el resultado es distinto de cero o hay contenido en la salida de error, lanzamos una excepcion:
	    if(result != 0 || StringUtils.isNotBlank(stderr)) {
	    	throw new Exception(stderr.toString());
	    }
		
	}
	
	
	/**
	 * Valida la existencia de una impresora
	 * @param nombreImpresora Nombre de la impresora a validar
	 * @throws Exception Excepcion lanzada cuando la impresora indicada no existe
	 */
	private void validaExistenciaImpresora(String nombreImpresora) throws ApplicationException {
		
		PrintService printSrv = null;
		
		// selection of all print services by printer name the printer is selected
        AttributeSet attSet = new HashAttributeSet();
        attSet.add(new PrinterName(nombreImpresora, null));
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, attSet);
                
        for (PrintService printService : services) {
        	if(nombreImpresora.trim().equalsIgnoreCase(printService.getName())) {
        		printSrv = printService;
        		break;
        	}
        }
        
        if(printSrv == null) {
        	throw new ApplicationException("No existe la impresora indicada: " + nombreImpresora);
        }
	}
	
	
	/**
	 * Valida la existencia de un documento
	 * @param documento Nombre y ruta completa del documento a validar
	 * @throws ApplicationException Excepcion lanzada cuando el documento indicado no existe 
	 */
	private void validaExistenciaDocumento(String documento) throws ApplicationException {
		
		try {
			new FileInputStream(documento);
		} catch (FileNotFoundException ffne) {
			throw new ApplicationException("El archivo no existe: " + documento, ffne);
		}
	}
	
}