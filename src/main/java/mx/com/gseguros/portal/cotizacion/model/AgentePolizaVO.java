package mx.com.gseguros.portal.cotizacion.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representa al agente asociado a una poliza
 * @author hector.lopez
 *
 */
public class AgentePolizaVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo del agente
	 */
	private String cdagente;
	/**
	 * Nombre completo del agente
	 */
	private String nombre;
	private String cdtipoAg;
	private String descripl;
	private String porparti;
	private String porredau;
	private String nmsuplem;
	private String nmcuadro;
	private String cdsucurs;
	private String comision;
	private String nvacomision;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
	
	//Getters and setters:

	public String getCdagente() {
		return cdagente;
	}

	public void setCdagente(String cdagente) {
		this.cdagente = cdagente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCdtipoAg() {
		return cdtipoAg;
	}

	public void setCdtipoAg(String cdtipoAg) {
		this.cdtipoAg = cdtipoAg;
	}

	public String getDescripl() {
		return descripl;
	}

	public void setDescripl(String descripl) {
		this.descripl = descripl;
	}

	public String getPorparti() {
		return porparti;
	}

	public void setPorparti(String porparti) {
		this.porparti = porparti;
	}

	public String getPorredau() {
		return porredau;
	}

	public void setPorredau(String porredau) {
		this.porredau = porredau;
	}

	public String getNmsuplem() {
		return nmsuplem;
	}

	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}

	public String getNmcuadro() {
		return nmcuadro;
	}

	public void setNmcuadro(String nmcuadro) {
		this.nmcuadro = nmcuadro;
	}

	public String getCdsucurs() {
		return cdsucurs;
	}

	public void setCdsucurs(String cdsucurs) {
		this.cdsucurs = cdsucurs;
	}


	public String getComision() {
		return comision;
	}


	public void setComision(String comision) {
		this.comision = comision;
	}


	public String getNvacomision() {
		return nvacomision;
	}


	public void setNvacomision(String nvacomision) {
		this.nvacomision = nvacomision;
	}
	
}
