package mx.com.gseguros.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class DocumentosUtils
{
	private final static Logger logger = LoggerFactory.getLogger(DocumentosUtils.class);
	
	public static File fusionarDocumentosPDF(List<File>origen,File destino)
	{
		return DocumentosUtils.fusionarDocumentosPDF(origen,destino,false);
	}
	
	public static File fusionarDocumentosPDF(List<File>origen,File destino,boolean credencial)
	{
		File resp=null;
		try
		{
			List<InputStream>listaInput=new ArrayList<InputStream>();
			for(File iOrigen:origen)
			{
				listaInput.add(new FileInputStream(iOrigen));
			}
			DocumentosUtils.concatPDFs(listaInput,new FileOutputStream(destino),false,credencial);
			resp=new File(destino.getCanonicalPath());
		}catch(Exception ex)
		{
			logger.error("Error al fusionarDocumentosPDF: ", ex);
		}
		return resp;
	}
	
	public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate)
	{
		DocumentosUtils.concatPDFs(streamOfPDFFiles, outputStream, paginate, false);
	}
	
	public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate, boolean credencial) {

	    Document document = null;
	    
	    /*
	     * Si es credencial, se configura el tamanio ID_1 que
	     * mide 85.60 x 53.98 mm
	     */
	    if(credencial)
	    {
	    	document = new Document(PageSize.ID_1);
	    }
	    else
	    {
	    	document = new Document(new RectangleReadOnly(625,842));
	    }
	    
	    try {
	      List<InputStream> pdfs = streamOfPDFFiles;
	      List<PdfReader> readers = new ArrayList<PdfReader>();
	      int totalPages = 0;
	      Iterator<InputStream> iteratorPDFs = pdfs.iterator();

	      // Create Readers for the pdfs.
	      while (iteratorPDFs.hasNext()) {
	        InputStream pdf = iteratorPDFs.next();
	        PdfReader pdfReader = new PdfReader(pdf);
	        readers.add(pdfReader);
	        totalPages += pdfReader.getNumberOfPages();
	      }
	      // Create a writer for the outputstream
	      PdfWriter writer = PdfWriter.getInstance(document, outputStream);

	      document.open();
	      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
	      // data

	      PdfImportedPage page;
	      int currentPageNumber = 0;
	      int pageOfCurrentReaderPDF = 0;
	      Iterator<PdfReader> iteratorPDFReader = readers.iterator();

	      // Loop through the PDF files and add to the output.
	      while (iteratorPDFReader.hasNext()) {
	        PdfReader pdfReader = iteratorPDFReader.next();

	        // Create a new page in the target for each source page.
	        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
	          document.newPage();
	          pageOfCurrentReaderPDF++;
	          currentPageNumber++;
	          page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
	          cb.addTemplate(page, 0, 0);

	          // Code for pagination.
	          if (paginate) {
	            cb.beginText();
	            cb.setFontAndSize(bf, 9);
	            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
	            cb.endText();
	          }
	        }
	        pageOfCurrentReaderPDF = 0;
	      }
	      outputStream.flush();
	      document.close();
	      outputStream.close();
	    } catch (Exception e) {
	      logger.error("Error en concatPDFs: ", e);
	    } finally {
	      if (document.isOpen())
	        document.close();
	      try {
	        if (outputStream != null)
	          outputStream.close();
	      } catch (IOException ioe) {
	        logger.error("Error en concatPDFs:: ", ioe);
	      }
	    }
	  }
	
	public static File pdfBlanco(File output){
		
		try {
			FileOutputStream outputStream=new FileOutputStream(output);;
			Document doc=new Document();
			doc.open();
			doc.newPage();
			PdfWriter writer = PdfWriter.getInstance(doc,outputStream);
			writer.open();
			doc.open();
			doc.add(new Paragraph(" "));
			writer.flush();
			doc.close();
			writer.close();
			outputStream.flush();
			outputStream.close();
			
		} catch (Exception e) {
			logger.error("Error en pdfBlanco: ", e);
		}
		return output;

	}
	
	
	
	/**
	 * Si es un archivo con numero impar de hojas le agrega una hoja blanca para la impresion duplex
	 * 
	 * @param input archivo a analizar 
	 * @param blanco ruta para guardar temporalmente el pdf blanco que se le agrega al archivo
	 * @return el mismo input pero con una hoja blanca si es el caso
	 */
	public static File blancoParaDuplex(File input,File blanco){
		try {
			PdfReader reader=new PdfReader(input.getAbsolutePath());
			int nPaginas=reader.getNumberOfPages();
			List<File> fus=new ArrayList<File>();
			fus.add(input);
			if(nPaginas%2!=0){
				fus.add(pdfBlanco(blanco));
			}
			
			long timestamp = System.currentTimeMillis();
			long rand = new Double(1000d*Math.random()).longValue();
			
			File ot=new File(blanco
        			        .getAbsoluteFile()
        			        .getParent()
        			        +Constantes.SEPARADOR_ARCHIVO
        			        +input.getName()
        			        .substring(
        			                0, input.getName().indexOf(".")
        			                )
        			        +"_dplx_"
        			        +timestamp+rand
        			        +".pdf");
			

			return mixPdf(fus,ot); 
			
		} catch (Exception e) {
			logger.error("Error en blancoParaDuplex: ", e);
		}
		return null;
	}
	
	public static File mixPdf(List<File> pdfs,File salida) {
		
		Document document= new Document();
		PdfCopy copy;
		try {
		    logger.debug("lista de archivos: {}",pdfs);
			copy = new PdfCopy(document, new FileOutputStream(salida));
		
			document.open();
			PdfReader reader;
			int n;
			for(File doc: pdfs){
			    logger.debug("Uniendo archivo: {}",doc.getAbsolutePath());
				reader=new PdfReader(doc.getAbsolutePath());
				n=reader.getNumberOfPages();
				for(int page=0;page<n;){
					copy.addPage(copy.getImportedPage(reader, ++page));
				}
				copy.freeReader(reader);
				reader.close();
			}
			document.close();
		
		} catch (Exception e) {
			logger.error("Error en mixPdf: ",e);
		}
		logger.debug("Archivo fusionado: {}",salida);
		return salida;
		
	}
	
	
	/**
	 * Genera un documento de Excel en base a los parametros de la lista
	 * @param lista					
	 * @param nombreArchivo   Nombre del archivo a generar
	 * @param incluyeEncabezado true si se requiere agregar un registro con el nombre de los encabezados, false si no
	 * @return
	 */
	public static boolean generaExcel(List<Map<String, String>> lista, String nombreArchivo, boolean incluyeEncabezado) {
		
		boolean exito = false;
		
		try {
		 // Generar archivo en Excel en ruta temporal
	        if(lista != null && lista.size() > 0) {
	                            
	            SXSSFWorkbook wb = new SXSSFWorkbook(); 
	            wb.setCompressTempFiles(true);
	            SXSSFSheet sh = (SXSSFSheet) wb.createSheet();
	            sh.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk
	            
	            int rownum = 0;
	            // Incluimos el encabezado de las columnas:
	            if(incluyeEncabezado && lista != null && lista.size() > 0) {
	                Map<String, String> map = lista.get(0);
	                Row row = sh.createRow(rownum);
	                int cellnum = 0;
	                for (Map.Entry<String, String> entry : map.entrySet()){
	                    Cell cell = row.createCell(cellnum);
	                    cell.setCellValue(entry.getKey());
	                    cellnum++;
	                }
	                rownum ++;
	            }
	            // Generamos los datos en el archivo:
	            for (Map<String, String> map : lista) {
	                Row row = sh.createRow(rownum);
	                int cellnum = 0;
	                for (Map.Entry<String, String> entry : map.entrySet()){
	                    Cell cell = row.createCell(cellnum);
	                    cell.setCellValue(entry.getValue());
	                    cellnum++;
	                }
	                rownum ++;
	            }
	            FileOutputStream out = new FileOutputStream(nombreArchivo);
	            wb.write(out);
	            out.close();
	        } else {
	            logger.warn("No hay datos para exportar el archivo {}", nombreArchivo);
	        }
	        exito = true;
        } catch (Exception e) {
            logger.error("Error al exportar datos al archivo {}", nombreArchivo, e);
        }
		return exito;
	}
	
	public static boolean verificaPDF(File pdf){
	    
	    try{
	        logger.debug("Verificando arcivo {}:"+pdf.getAbsolutePath());
	        PdfReader reader=new PdfReader(pdf.getAbsolutePath());
	        boolean res=!reader.isRebuilt(); 
	        logger.debug("Resultado Verificando arcivo {} :"+res,pdf.getAbsolutePath());
	        return res;
	        
	    }catch(Exception e){
	        logger.debug("Error al verificar pdf: {}",e);
	        return false;
	    }
	    
	    
	}
	
	
//	public static void main(String []args)throws Exception{
//		
//		List<File> l=new ArrayList();
//		l.add(new File("C:\\Users\\Biosnet.16\\Desktop\\basura\\lote_4529_remesa_440457_tramite_394544_t_1480534101908_135.pdf"));//malo
//		l.add(new File("C:\\Users\\Biosnet.16\\Desktop\\basura\\lote_4529_remesa_440458_tramite_300059_t_1480533657476_545.pdf"));
//		mixPdf(l, new File("c:\\Users\\Biosnet.16\\Desktop\\basura\\slm.pdf"));
//		System.out.println("yaaa");
//		System.out.println(verificaPDF(new File("c:\\Users\\Biosnet.16\\Desktop\\basura\\lotesdsd_4529_remesa_440458_tramite_300059_t_1480533657476_545.pdf")));
//		
//	}
}