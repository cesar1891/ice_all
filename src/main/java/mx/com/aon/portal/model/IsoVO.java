package mx.com.aon.portal.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class IsoVO implements Serializable{

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -6404719464756924887L;
	private String pais;
	private String languague;
	private String formatoNumerico;
	private String formatoFecha;
	private String cdRegion;
	private String cdIdioma;
	private String clientDateFormat;
	
	public String getCdRegion() {
		return cdRegion;
	}
	public void setCdRegion(String cdRegion) {
		this.cdRegion = cdRegion;
	}
	public String getCdIdioma() {
		return cdIdioma;
	}
	public void setCdIdioma(String cdIdioma) {
		this.cdIdioma = cdIdioma;
	}
	/**
	 * 
	 * @return pais
	 */
	public String getPais() {
		return pais;
	}
	/**
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}
	/**
	 * 
	 * @return languague
	 */
	public String getLanguague() {
		return languague;
	}
	/**
	 * 
	 * @param languague
	 */
	public void setLanguague(String languague) {
		this.languague = languague;
	}
	/**
	 * 
	 * @return formatoNumerico
	 */
	public String getFormatoNumerico() {
		return formatoNumerico;
	}
	/**
	 * 
	 * @param formatoNumerico
	 */
	public void setFormatoNumerico(String formatoNumerico) {
		this.formatoNumerico = formatoNumerico;
	}
	/**
	 * 
	 * @return formatoFecha
	 */
	public String getFormatoFecha() {
		return formatoFecha;
	}
	/**
	 * 
	 * @param formatoFecha
	 */
	public void setFormatoFecha(String formatoFecha) {
		this.formatoFecha = formatoFecha;
	}
	public String getClientDateFormat() {
		return clientDateFormat;
	}
	public void setClientDateFormat(String clientDateFormat) {
		this.clientDateFormat = clientDateFormat;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
