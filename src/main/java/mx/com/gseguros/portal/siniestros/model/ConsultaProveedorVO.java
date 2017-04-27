package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ConsultaProveedorVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String cdpresta;
	private String nombre;
	private String cdespeci;
	private String descesp;
	private String circulo;
	private String codpos;
	private String zonaHospitalaria;
	
	public String getCirculo() {
		return circulo;
	}

	public void setCirculo(String circulo) {
		this.circulo = circulo;
	}

	public String getCodpos() {
		return codpos;
	}

	public void setCodpos(String codpos) {
		this.codpos = codpos;
	}

	public String getZonaHospitalaria() {
		return zonaHospitalaria;
	}

	public void setZonaHospitalaria(String zonaHospitalaria) {
		this.zonaHospitalaria = zonaHospitalaria;
	}

	public String getCdpresta() {
		return cdpresta;
	}

	public void setCdpresta(String cdpresta) {
		this.cdpresta = cdpresta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCdespeci() {
		return cdespeci;
	}

	public void setCdespeci(String cdespeci) {
		this.cdespeci = cdespeci;
	}

	public String getDescesp() {
		return descesp;
	}

	public void setDescesp(String descesp) {
		this.descesp = descesp;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
