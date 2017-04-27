package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

/**
 * @author Alberto
 *
 */
public class SiniestroVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String cdunieco;
	private String cdramo;
	private String estado;
	private String nmpoliza;
	private String nmsuplem;
	private String nmsituac;
	private String aapertu;
	private String statusSinies;
	private String nmsinies;
	
	public String getCdunieco() {
		return cdunieco;
	}
	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}
	public String getCdramo() {
		return cdramo;
	}
	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNmpoliza() {
		return nmpoliza;
	}
	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}
	public String getNmsuplem() {
		return nmsuplem;
	}
	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}
	public String getNmsituac() {
		return nmsituac;
	}
	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}
	public String getAapertu() {
		return aapertu;
	}
	public void setAapertu(String aapertu) {
		this.aapertu = aapertu;
	}
	public String getStatusSinies() {
		return statusSinies;
	}
	public void setStatusSinies(String statusSinies) {
		this.statusSinies = statusSinies;
	}
	public String getNmsinies() {
		return nmsinies;
	}
	public void setNmsinies(String nmsinies) {
		this.nmsinies = nmsinies;
	}
		
}
