package mx.com.gseguros.portal.general.util;

/**
 * Enum con informacion de los tipos de archivo mas com&uacute;nes 
 * @author Ricardo
 *
 */
public enum TipoArchivo {
	
	BMP (".bmp" , "image/bmp"),
	CSV (".csv" , "application/octet-stream"),
	DOC (".doc" , "application/msword"),
	DOCX(".docx", "application/msword"),
	GIF (".gif" , "image/gif"),
	HTM (".htm" , "text/html"),
	HTML(".html", "text/html"),
	JPEG(".jpeg", "image/jpeg"),
	JPG (".jpg" , "image/jpeg"),
	PDF (".pdf" , "application/pdf"),
	PNG (".png" , "image/png"),
	PPT (".ppt" , "application/ppt"),
	PPTX(".pptx", "application/ppt"),
	RTF (".rtf" , "application/octet-stream"),
	TIF (".tif" , "image/tiff"),
	TXT (".txt" , "text/plain"),
	XLS (".xls" , "application/vnd.ms-excel"),
	XLSX(".xlsx", "application/vnd.ms-excel"),
	XML (".xml" , "application/octet-stream"),
	DEFAULT (".*","application/octet-stream");
	
	
	private TipoArchivo(String extension, String contentType) {
		this.extension = extension;
		this.contentType = contentType;
	}
	
	private String extension;
	
	private String contentType;

	
	public String getExtension() {
		return extension;
	}

	public String getContentType() {
		return contentType;
	}
	
}