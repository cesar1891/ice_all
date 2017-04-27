package mx.com.aon.tmp;

import java.io.File;

public class EmailVO {

	private String mensaje;
	
	private String[] to;
	
	private String[] cc;
	
	private String asunto;
	
	private File file;
	
	private String fileName;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTo() {
		return to[0];
	}
	
	/**
	 * @return the to
	 */
	public String[] getAllTo() {
		return to;
	}
	
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = new String[1];
		this.to[0] = to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String[] to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc[0];
	}

	/**
	 * @return the cc
	 */
	public String[] getAllCc() {
		return cc;
	}
	
	
	/**
	 * @param cc the cc to set
	 */
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	
	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = new String[1];
		this.cc[0] = cc;
	}

	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * @param asunto the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
}