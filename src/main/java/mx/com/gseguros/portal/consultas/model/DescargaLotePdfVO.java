package mx.com.gseguros.portal.consultas.model;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public class DescargaLotePdfVO implements Serializable{
	
	private InputStream fileInput;
	private File errores;
	
	public InputStream getFileInput() {
		return fileInput;
	}
	public void setFileInput(InputStream fileInput) {
		this.fileInput = fileInput;
	}
	public File getErrores() {
		return errores;
	}
	public void setErrores(File errores) {
		this.errores = errores;
	}
	
	
}
