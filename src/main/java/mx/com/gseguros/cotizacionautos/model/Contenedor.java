package mx.com.gseguros.cotizacionautos.model;

import java.io.Serializable;

public class Contenedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	TipoMoneda colMoneda;
	TipoImporte colImporte;
	TipoClickCob colClickCob;
	String cdaseg;
	String cdplan;
	String nmsituac;
	
	public TipoMoneda getColMoneda() {
		return colMoneda;
	}
	public void setColMoneda(TipoMoneda colMoneda) {
		this.colMoneda = colMoneda;
	}
	public TipoImporte getColImporte() {
		return colImporte;
	}
	public void setColImporte(TipoImporte colImporte) {
		this.colImporte = colImporte;
	}
	public TipoClickCob getColClickCob() {
		return colClickCob;
	}
	public void setColClickCob(TipoClickCob colClickCob) {
		this.colClickCob = colClickCob;
	}
	public String getNmsituac() {
		return nmsituac;
	}
	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}
	public String getCdaseg() {
		return cdaseg;
	}
	public void setCdaseg(String cdaseg) {
		this.cdaseg = cdaseg;
	}
	public String getCdplan() {
		return cdplan;
	}
	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}
	public Contenedor(TipoMoneda colMoneda, TipoImporte colImporte,
			TipoClickCob colClickCob, String cdaseg, String cdplan,
			String nmsituac) {
		this.colMoneda = colMoneda;
		this.colImporte = colImporte;
		this.colClickCob = colClickCob;
		this.cdaseg = cdaseg;
		this.cdplan = cdplan;
		this.nmsituac = nmsituac;
	}
	public Contenedor() {
	}
	
}
