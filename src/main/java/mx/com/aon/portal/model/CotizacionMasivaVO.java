
package mx.com.aon.portal.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CotizacionMasivaVO {


	private String cdElemento;
	private String asegura;
	private String cdlayout;
	private String fedesde;
	private String fehasta;
	
	private String cdAsegura;
	private String dsAsegura;
	private String cdRamo;
	private String dsRamo;
	private String cdPerson;
	private String dsNombre;
	private String carga;
	private String nmPoliza;
	private String feInivig;
	private String feFinvig;
	private String prima;
	
	private String dsatribu;
	private String otvalor;
	private String dsnombre;
	private String dsvalor;
	
	private String nmsituac;
	private String cdplan;
	
	private String estado;
	private String cdtipsit;
	private String cdcia;
	private String cdunieco;
	
	public String getCdElemento() {
		return cdElemento;
	}
	public void setCdElemento(String cdElemento) {
		this.cdElemento = cdElemento;
	}
	public String getAsegura() {
		return asegura;
	}
	public void setAsegura(String asegura) {
		this.asegura = asegura;
	}
	public String getCdRamo() {
		return cdRamo;
	}
	public void setCdRamo(String cdRamo) {
		this.cdRamo = cdRamo;
	}
	public String getCdlayout() {
		return cdlayout;
	}
	public void setCdlayout(String cdlayout) {
		this.cdlayout = cdlayout;
	}
	public String getFedesde() {
		return fedesde;
	}
	public void setFedesde(String fedesde) {
		this.fedesde = fedesde;
	}
	public String getFehasta() {
		return fehasta;
	}
	public void setFehasta(String fehasta) {
		this.fehasta = fehasta;
	}
	public String getCdAsegura() {
		return cdAsegura;
	}
	public void setCdAsegura(String cdAsegura) {
		this.cdAsegura = cdAsegura;
	}
	public String getDsAsegura() {
		return dsAsegura;
	}
	public void setDsAsegura(String dsAsegura) {
		this.dsAsegura = dsAsegura;
	}
	public String getDsRamo() {
		return dsRamo;
	}
	public void setDsRamo(String dsRamo) {
		this.dsRamo = dsRamo;
	}
	public String getCdPerson() {
		return cdPerson;
	}
	public void setCdPerson(String cdPerson) {
		this.cdPerson = cdPerson;
	}
	public String getDsNombre() {
		return dsNombre;
	}
	public void setDsNombre(String dsNombre) {
		this.dsNombre = dsNombre;
	}
	public String getCarga() {
		return carga;
	}
	public void setCarga(String carga) {
		this.carga = carga;
	}
	public String getNmPoliza() {
		return nmPoliza;
	}
	public void setNmPoliza(String nmPoliza) {
		this.nmPoliza = nmPoliza;
	}
	public String getFeInivig() {
		return feInivig;
	}
	public void setFeInivig(String feInivig) {
		this.feInivig = feInivig;
	}
	public String getFeFinvig() {
		return feFinvig;
	}
	public void setFeFinvig(String feFinvig) {
		this.feFinvig = feFinvig;
	}
	public String getPrima() {
		return prima;
	}
	public void setPrima(String prima) {
		this.prima = prima;
	}
	public void setDsatribu(String dsatribu) {
		this.dsatribu = dsatribu;
	}
	public String getDsatribu() {
		return dsatribu;
	}
	public void setOtvalor(String otvalor) {
		this.otvalor = otvalor;
	}
	public String getOtvalor() {
		return otvalor;
	}
	public void setDsnombre(String dsnombre) {
		this.dsnombre = dsnombre;
	}
	public String getDsnombre() {
		return dsnombre;
	}
	public void setDsvalor(String dsvalor) {
		this.dsvalor = dsvalor;
	}
	public String getDsvalor() {
		return dsvalor;
	}
	
	public String getNmsituac() {
		return nmsituac;
	}
	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}
	public String getCdplan() {
		return cdplan;
	}
	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCdtipsit() {
		return cdtipsit;
	}
	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}
	public String getCdcia() {
		return cdcia;
	}
	public void setCdcia(String cdcia) {
		this.cdcia = cdcia;
	}
	public String getCdunieco() {
		return cdunieco;
	}
	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}
		
}
