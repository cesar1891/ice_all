package mx.com.aon.portal.model.principal;

import java.io.Serializable;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class SeccionVO implements Serializable {

	/**
	 * Serial Version 
	 */
	private static final long serialVersionUID = 1742647712441506291L;
	
	private String cdSeccion;
	private String dsSeccion;
	/**
	 * 
	 * @return
	 */
	public String getCdSeccion() {
		return cdSeccion;
	}
	/**
	 * 
	 * @param cdSeccion
	 */
	public void setCdSeccion(String cdSeccion) {
		this.cdSeccion = cdSeccion;
	}
	/**
	 * 
	 * @return
	 */
	public String getDsSeccion() {
		return dsSeccion;
	}
	/**
	 * 
	 * @param dsSeccion
	 */
	public void setDsSeccion(String dsSeccion) {
		this.dsSeccion = dsSeccion;
	}
	
	
}
