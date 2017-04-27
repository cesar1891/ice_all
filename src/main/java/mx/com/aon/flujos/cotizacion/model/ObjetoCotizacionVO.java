package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 
 * @author sergio.ramirez
 *
 */
public class ObjetoCotizacionVO implements Serializable {

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = -1953946065371548065L;
	private String cdUnieco;
	private String cdRamo;
	private String estado;
	private String nmPoliza;
	private String nmSituac;
	private String cdTipobj;
	private String nmSuplem;
	private String status;
	private String nmObjeto;
	private String dsObjeto;
	private String ptObjeto;
	private String dsDescripcion;
	private String cdAgrupa;
	private String nmValor;
	private String accion;
	
	
	
	private String cdGarant;
	private String cdTipcon;
	private String cdContar;
	private String nmImport;
	private String orden;
	private String dsGarant;
	private String dsTipcon;
	private String dsContar;
	
	private String nmEndoso;
	private String dsFormaPag;
	private String totalPagar;
	private String totalPagarF;
	private String nmrecsub;
		
	public String getNmrecsub() {
		return nmrecsub;
	}
	public void setNmrecsub(String nmrecsub) {
		this.nmrecsub = nmrecsub;
	}
	/**
	 * 
	 * @return cdUnieco
	 */
	public String getCdUnieco() {
		return cdUnieco;
	}
	/**
	 * 
	 * @param cdUnieco
	 */
	public void setCdUnieco(String cdUnieco) {
		this.cdUnieco = cdUnieco;
	}
	/**
	 * 
	 * @return cdRamo
	 */
	public String getCdRamo() {
		return cdRamo;
	}
	/**
	 * 
	 * @param cdRamo
	 */
	public void setCdRamo(String cdRamo) {
		this.cdRamo = cdRamo;
	}
	/**
	 * 
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * 
	 * @return nmPoliza
	 */
	public String getNmPoliza() {
		return nmPoliza;
	}
	/**
	 * 
	 * @param nmPoliza
	 */
	public void setNmPoliza(String nmPoliza) {
		this.nmPoliza = nmPoliza;
	}
	/**
	 * 
	 * @return nmSituac
	 */
	public String getNmSituac() {
		return nmSituac;
	}
	/**
	 * 
	 * @param nmSituac
	 */
	public void setNmSituac(String nmSituac) {
		this.nmSituac = nmSituac;
	}
	/**
	 * 
	 * @return cdTipobj
	 */
	public String getCdTipobj() {
		return cdTipobj;
	}
	/**
	 * 
	 * @param cdTipobj
	 */
	public void setCdTipobj(String cdTipobj) {
		this.cdTipobj = cdTipobj;
	}
	/**
	 * 
	 * @return nmSuplem
	 */
	public String getNmSuplem() {
		return nmSuplem;
	}
	/**
	 * 
	 * @param nmSuplem
	 */
	public void setNmSuplem(String nmSuplem) {
		this.nmSuplem = nmSuplem;
	}
	/**
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 
	 * @return nmObjeto
	 */
	public String getNmObjeto() {
		return nmObjeto;
	}
	/**
	 * 
	 * @param nmObjeto
	 */
	public void setNmObjeto(String nmObjeto) {
		this.nmObjeto = nmObjeto;
	}
	/**
	 * 
	 * @return dsObjeto
	 */
	public String getDsObjeto() {
		return dsObjeto;
	}
	/**
	 * 
	 * @param dsObjeto
	 */
	public void setDsObjeto(String dsObjeto) {
		this.dsObjeto = dsObjeto;
	}
	/**
	 * 
	 * @return ptObjeto
	 */
	public String getPtObjeto() {
		return ptObjeto;
	}
	/**
	 * 
	 * @param ptObjeto
	 */
	public void setPtObjeto(String ptObjeto) {
		this.ptObjeto = ptObjeto;
	}
	public void setDsDescripcion(String dsDescripcion) {
		this.dsDescripcion = dsDescripcion;
	}
	public String getDsDescripcion() {
		return dsDescripcion;
	}
	/**
	 * 
	 * @return cdAgrupa
	 */
	public String getCdAgrupa() {
		return cdAgrupa;
	}
	/**
	 * 
	 * @param cdAgrupa
	 */
	public void setCdAgrupa(String cdAgrupa) {
		this.cdAgrupa = cdAgrupa;
	}
	/**
	 * 
	 * @return nmValor
	 */
	public String getNmValor() {
		return nmValor;
	}
	/**
	 * 
	 * @param nmValor
	 */
	public void setNmValor(String nmValor) {
		this.nmValor = nmValor;
	}
	/**
	 * 
	 * @return accion
	 */
	public String getAccion() {
		return accion;
	}
	/**
	 * 
	 * @param accion
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	
	
	public String getCdGarant() {
		return cdGarant;
	}
	public void setCdGarant(String cdGarant) {
		this.cdGarant = cdGarant;
	}
	public String getCdTipcon() {
		return cdTipcon;
	}
	public void setCdTipcon(String cdTipcon) {
		this.cdTipcon = cdTipcon;
	}
	public String getCdContar() {
		return cdContar;
	}
	public void setCdContar(String cdContar) {
		this.cdContar = cdContar;
	}
	public String getNmImport() {
		return nmImport;
	}
	public void setNmImport(String nmImport) {
		this.nmImport = nmImport;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getDsGarant() {
		return dsGarant;
	}
	public void setDsGarant(String dsGarant) {
		this.dsGarant = dsGarant;
	}
	public String getDsTipcon() {
		return dsTipcon;
	}
	public void setDsTipcon(String dsTipcon) {
		this.dsTipcon = dsTipcon;
	}
	public String getDsContar() {
		return dsContar;
	}
	public void setDsContar(String dsContar) {
		this.dsContar = dsContar;
	}
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("cdUnieco",cdUnieco )
            .append("cdRamo",cdRamo)
            .append("estado",estado)
            .append("nmPoliza", nmPoliza)
            .append("nmSituac",nmSituac)
            .append("cdTipobj",cdTipobj)
            .append("nmSuplem",nmSuplem )
            .append("status",status)
            .append("nmObjeto",nmObjeto)
            .append("dsObjeto",dsObjeto )
            .append("dsDescripcion",dsDescripcion )
            .append("ptObjeto",ptObjeto)
            .append("cdAgrupa",cdAgrupa)
            .append("nmValor", nmValor)
            .append("accion",accion)
            .toString();
    }
	public String getNmEndoso() {
		return nmEndoso;
	}
	public void setNmEndoso(String nmEndoso) {
		this.nmEndoso = nmEndoso;
	}
	public String getDsFormaPag() {
		return dsFormaPag;
	}
	public void setDsFormaPag(String dsFormaPag) {
		this.dsFormaPag = dsFormaPag;
	}
	public String getTotalPagar() {
		return totalPagar;
	}
	public void setTotalPagar(String totalPagar) {
		this.totalPagar = totalPagar;
	}
	public String getTotalPagarF() {
		return totalPagarF;
	}
	public void setTotalPagarF(String totalPagarF) {
		this.totalPagarF = totalPagarF;
	}

}





