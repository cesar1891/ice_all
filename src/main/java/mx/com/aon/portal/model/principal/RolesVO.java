package mx.com.aon.portal.model.principal;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class RolesVO implements Serializable {

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = 4396142393028438388L;
	
	private String cdRol;
	private String dsRol;
	/**
	 * 
	 * @return
	 */
	public String getCdRol() {
		return cdRol;
	}
	/**
	 * 
	 * @param cdRol
	 */
	public void setCdRol(String cdRol) {
		this.cdRol = cdRol;
	}
	/**
	 * 
	 * @return
	 */
	public String getDsRol() {
		return dsRol;
	}
	/**
	 * 
	 * @param dsRol
	 */
	public void setDsRol(String dsRol) {
		this.dsRol = dsRol;
	}
	/**
	 * 
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("cdRol", cdRol)
				.append("dsRol", dsRol).toString();

	}

}
