package mx.com.gseguros.test;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.utils.Host;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.utils.cmd.SystemCommandExecutor;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/test")
public class TestImpresionesAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -3861435458381281429L;
	
	final static Logger logger = LoggerFactory.getLogger(TestImpresionesAction.class);
	
	private List<PrintService> printServices;
	
	private Map<String, String> params;
	
	private String respuesta;
	
	/**
	 * Lista las impresoras disponibles desde el servidor
	 * @return
	 * @throws Exception
	 */
	@Action(value="listaImpresoras",
			results={@Result(name="success", type="json")}
	)
	public String listaImpresoras() throws Exception {
		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		
        logger.debug("Printer Services found:");
        printService(services);
        
        printServices = Arrays.asList(services);
		
		return SUCCESS;
	}
	
	
	/**
	 * Imprime un documento
	 * @return
	 * @throws Exception
	 */
	@Action(value="imprimeDocumento",
			results={@Result(name="success", type="json")}
	)
	public String imprimeDocumento() throws Exception {
		
		// Discover the printers that can print the format according to the instructions in the attribute set
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        logger.debug("Printer Services found:");
        printService(services);
        
        int iPrinter = Integer.parseInt(params.get("iPrinter"));
        
		// Input the file
		FileInputStream textStream = null; 
		try {
			textStream = new FileInputStream(params.get("filename")); 
		} catch (FileNotFoundException ffne) {
			logger.error(ffne.getMessage(), ffne);
		}
		if (textStream == null) {
			logger.info("No existe el documento: {}", params.get("filename"));
		    return SUCCESS;
		} else {
			logger.info("Si existe el documento: {}", params.get("filename"));
		}
		
		
		String formatoArchivo = params.get("formato");
		Integer numCopias = 1;
		
		if(StringUtils.isNotBlank(params.get("numCopias"))){
			numCopias  = Integer.parseInt(params.get("numCopias"));
		}else{
			numCopias  = 1;
		}
		
		Doc myDoc = null;
		if(formatoArchivo.equalsIgnoreCase("PDF")) {
			DocFlavor myFormat = DocFlavor.INPUT_STREAM.PDF;
			myDoc = new SimpleDoc(textStream, myFormat, null);
			logger.debug("Flavor encontrado: {}", myFormat);
		} else if(formatoArchivo.equalsIgnoreCase("JPEG")) {
			DocFlavor myFormat = DocFlavor.INPUT_STREAM.JPEG;
			myDoc = new SimpleDoc(textStream, myFormat, null);
			logger.debug("Flavor encontrado: {}", myFormat);
		} else if(formatoArchivo.equalsIgnoreCase("PNG")) {
			DocFlavor myFormat = DocFlavor.INPUT_STREAM.PNG;
			myDoc = new SimpleDoc(textStream, myFormat, null);
			logger.debug("Flavor encontrado: {}", myFormat);
		} else {
			logger.error("Formato inexistente: {}. No hay un Flavor definido", formatoArchivo);
		}
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		
		// Configuracion de numero de copias:
		aset.add(new Copies(numCopias)); 
		
		// Configuracion de tipo de hoja:
		if(params != null && params.get("mediaSize") != null) {
			if( "A0".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.ISO.A0.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "A1".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.ISO.A1.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "A2".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.ISO.A2.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "A3".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.ISO.A3.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "A4".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.ISO.A4.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "LETTER".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.NA.LETTER.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			} else if( "LEGAL".equals(params.get("mediaSize")) ) {
				aset.add(MediaSize.NA.LEGAL.getMediaSizeName());
				logger.debug("Se configura tipo de hoja a {}", params.get("mediaSize"));
			}
		}
		//aset.add(MediaSize.NA.LETTER.getMediaSizeName());
		
		//aset.add(Sides.DUPLEX);
		/*
		float printableX = someSize;
		float printableY = someSize;
		printSet = new HashPrintRequestAttributeSet();
		mediaSizeName = MediaSize.findMedia(printableX,printableY,MediaPrintableArea.INCH);
		printSet.add(mediaSizeName);
		*/
		
		////////////////////////////////////////
		if(params.get("mediaId") != null) {
			
			int mediaId = Integer.parseInt(params.get("mediaId"));
			// we store all the tray in a hashmap
	        Map<Integer, Media> trayMap = new HashMap<Integer, Media>(10);

	        // we chose something compatible with the printable interface
	        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
	        logger.debug("Service: {}", services[iPrinter]);
	        
	        // we retrieve all the supported attributes of type Media
	        // we can receive MediaTray, MediaSizeName, ...
	        Object o = services[iPrinter].getSupportedAttributeValues(Media.class, flavor, null);
	        if (o != null && o.getClass().isArray()) {
	            for (Media media : (Media[]) o) {
	                // we collect the MediaTray available
	                if (media instanceof MediaTray) {
	                    logger.debug("{} : {} - {}", media.getValue(), media, media.getClass().getName());
	                    trayMap.put(media.getValue(), media);
	                }
	            }
	        }

	        // Tray target id:
	        MediaTray selectedTray = (MediaTray) trayMap.get(Integer.valueOf(mediaId));
	        logger.debug("Selected tray : {}", selectedTray.toString());
	        
	        // we have to add the MediaTray selected as attribute
	        aset.add(selectedTray);
		}
		////////////////////////////////////////
		
		DocPrintJob job = services[iPrinter].createPrintJob(); 
		try { 
        	logger.info("Antes de imprimir");
        	logger.debug("configuracion de impresion:");
        	Attribute[] attrs = aset.toArray();
            for (int i = 0; i < attrs.length; i++) {
            	logger.info("\t\t{}: {}", attrs[i].getName(), attrs[i]);
            }
            job.print(myDoc, aset);
            logger.info("Despu\u00E9s de imprimir");
        } catch (PrintException pe) {
        	logger.error(pe.getMessage(), pe);
        }
		return SUCCESS;
	}
	
	
	private static void printService(PrintService[] services) {
        if (services!=null && services.length>0) {
            for (int i = 0; i < services.length; i++) {
                logger.debug("{}", services[i]);
            }
        }
    }
	
	
	/**
	 * Elige una bandeja para imprmir
	 * @return
	 * @throws Exception
	 */
	@Action(value="eligeBandeja",
			results={@Result(name="success", type="json")}
	)
	public String eligeBandeja() throws Exception {

		// Params:
		String printerName = params.get("printerName");
        String mediaId     = params.get("mediaId");
        String printPage   = params.get("printTestPage");
		
        /*
        // get default printer
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        // suggest the use of the default printer
        logger.debug("Default Print Service: {}", defaultPrintService.getName());
        

        // if there is no input, use the default printer
        if (printerName == null || printerName.equals("")) {
        	logger.debug("Se toma la impresora por default: {}", defaultPrintService.getName());
            printerName = defaultPrintService.getName();
        }
        */

        // the printer is selected
        AttributeSet aset = new HashAttributeSet();
        aset.add(new PrinterName(printerName, null));

        // selection of all print services
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);
        // we store all the tray in a hashmap
        Map<Integer, Media> trayMap = new HashMap<Integer, Media>(10);

        // we chose something compatible with the printable interface
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;

        for (PrintService service : services) {
            logger.debug("Service: {}", service);

            // we retrieve all the supported attributes of type Media
            // we can receive MediaTray, MediaSizeName, ...
            Object o = service.getSupportedAttributeValues(Media.class, flavor, null);
            if (o != null && o.getClass().isArray()) {
                for (Media media : (Media[]) o) {
                    // we collect the MediaTray available
                    if (media instanceof MediaTray) {
                        logger.debug("{} : {} - {}", media.getValue(), media, media.getClass().getName());
                        trayMap.put(media.getValue(), media);
                    }
                }
            }
        }

        // Tray target id:
        MediaTray selectedTray = (MediaTray) trayMap.get(Integer.valueOf(mediaId));
        logger.debug("Selected tray : {}", selectedTray.toString());

        if (printPage.equalsIgnoreCase("Y")) {

            // we have to add the MediaTray selected as attribute
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(selectedTray);

            // we create the printer job, it print a specified document with a set of job attributes
            DocPrintJob job = services[0].createPrintJob();

            try {
                logger.debug("Trying to print an empty page on : {}", selectedTray.toString());
                // we create a document that implements the printable interface
                Doc doc = new SimpleDoc(new PrintableDemo(), DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);

                // we print using the selected attributes (paper tray)
                job.print(doc, attributes);

            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
            }
        }
        return SUCCESS;
    }


    static class PrintableDemo implements Printable {

        @Override
        public int print(Graphics pg, PageFormat pf, int pageNum) {
            // we print an empty page
            if (pageNum >= 1)
                return Printable.NO_SUCH_PAGE;
            pg.drawString("", 10, 10);
            return Printable.PAGE_EXISTS;
        }
    }
    
    
	@Action(value="testComando",
			results={@Result(name="success", type="json")}
	)
	public String ejecutaComando() throws Exception {
		
		logger.debug("params={}", params);
		logger.debug("Antes de ejecutar comando {}", params.get("comando"));
		Host.executeCommand(params.get("comando"));
		logger.debug("Despues de ejecutar comando {}", params.get("comando"));
		
		return SUCCESS;
	}
	
	
	@Action(value="testComando2",
			results={@Result(name="success", type="json")}
	)
	public String ejecutaComando2() throws Exception {
	    
	    try {
		
    		List<String> commands = new ArrayList<String>();
    	    commands.add("/bin/sh");
    	    commands.add("-c");
    	    commands.add(params.get("comando"));
    	    //commands.add("ls -l /var/tmp | grep tmp");
    
    	    // execute the command
    	    SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
    	    logger.debug("Antes de ejecutar comando {}", params.get("comando"));
    	    int result = commandExecutor.executeCommand();
    		logger.debug("Despues de ejecutar comando {}", params.get("comando"));
    		logger.debug("The numeric result of the command wasss: " + result);
    
    	    // get the stdout and stderr from the command that was run
    	    StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
    	    StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
    	    
    	    // print the stdout and stderr
    	    logger.debug("STDOUT:");
    	    logger.debug(stdout.toString());
    	    logger.debug("STDERR:");
    	    logger.debug(stderr.toString());
    	    
    	    params.put("result", String.valueOf(result));
    	    params.put("stdout", stdout.toString());
    	    params.put("stderr", stderr.toString());
            
        } catch(Exception ex) {
            respuesta = Utils.manejaExcepcion(ex);
        }
		
		return SUCCESS;
	}

	
	/**
	 * Prueba de sleep
	 * @return
	 * @throws Exception
	 */
	@Action(value="sleep",
			results={@Result(name="success", type="json")}
	)
	public String sleep() throws Exception {
		int min = Integer.parseInt(params.get("m"));
		logger.debug("Iniciando sleep de {} minuto(s)", min);
		Thread.sleep(min*60*1000);
        logger.debug("Finalizando sleep de {} minuto(s)", min);
		return SUCCESS;
	}
    
    
	/*
	public static void main(String[] args) {
		
		PrintService[] services2 = PrintServiceLookup.lookupPrintServices(null, null);
        logger.debug("Printer Services found:");
        printService(services2);
		
		// Input the file
		FileInputStream textStream = null; 
		try {
			textStream = new FileInputStream("E:\\R\\Pictures\\Beatles-Hard-Days-Night-cover.jpg"); 
		} catch (FileNotFoundException ffne) {
			ffne.printStackTrace();
		} 
		if (textStream == null) { 
			System.out.println("no existe el documento");
		    return; 
		} else {
			System.out.println("SI existe el documento");
		}
		// Set the document type
		DocFlavor myFormat = DocFlavor.INPUT_STREAM.JPEG;
		// Create a Doc
		Doc myDoc = new SimpleDoc(textStream, myFormat, null); 
		// Build a set of attributes
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet(); 
		aset.add(new Copies(5)); 
		aset.add(MediaSize.ISO.A4.getMediaSizeName());
		aset.add(Sides.DUPLEX); 
		// discover the printers that can print the format according to the
		// instructions in the attribute set
		PrintService [] //services = PrintServiceLookup.lookupDefaultPrintService();//PrintServiceLookup.lookupPrintServices(myFormat, aset);
		services = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PDF, null);
		System.out.println("***");
		printService(services);
		System.out.println("***");
		
		DocPrintJob job = services2[1].createPrintJob(); 
		try { 
        	System.out.println("Antes de print");
            job.print(myDoc, aset);
            System.out.println("Despues de print");
        } catch (PrintException pe) {
        	pe.printStackTrace();
        } 
		//System.out.println("services=" + services);
		//System.out.println("services.length=" + services.length);
		
//		// Create a print job from one of the print services
//		if (services.length > 0) { 
//		        DocPrintJob job = services[0].createPrintJob(); 
//		        try { 
//		        	System.out.println("Antes de print");
//		            job.print(myDoc, aset);
//		            System.out.println("Despues de print");
//		        } catch (PrintException pe) {
//		        	pe.printStackTrace();
//		        } 
//		}
		
	}
	*/
	
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public List<PrintService> getPrintServices() {
		return printServices;
	}

	public void setPrintServices(List<PrintService> printServices) {
		this.printServices = printServices;
	}
	
}