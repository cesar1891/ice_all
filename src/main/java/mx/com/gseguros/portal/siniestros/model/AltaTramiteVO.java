package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class AltaTramiteVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String ntramite;
	private String nfactura;
	private String ffactura;
	private String cdtipser;
	private String dstipser;
	private String cdpresta;
	private String dspresta;
	private String ptimport;
	private String cdgarant;
	private String cdconval;
	private String descporc;
	private String descnume;
	private String cdunieco;
	private String cdramo;
	private String estado;
	private String nmpoliza;
	private String nmpoliex;
	private String nmsolici;
	private String nmsuplem;
	private String nmsituac;
	private String cdtipsit;
	private String nombre;
	private String cdperson;
	private String feocurre;
	private String nmautser;
	private String cdmoneda;
	private String desTipomoneda;
	private String tasacamb;
	private String ptimporta;
	private String nombreAsegurado;
	private String nombProv;
	private String email;
	private String telefono;

	
	public String getNtramite() {
		return ntramite;
	}


	public void setNtramite(String ntramite) {
		this.ntramite = ntramite;
	}


	public String getNfactura() {
		return nfactura;
	}


	public void setNfactura(String nfactura) {
		this.nfactura = nfactura;
	}


	public String getFfactura() {
		return ffactura;
	}


	public void setFfactura(String ffactura) {
		this.ffactura = ffactura;
	}


	public String getCdtipser() {
		return cdtipser;
	}


	public void setCdtipser(String cdtipser) {
		this.cdtipser = cdtipser;
	}


	public String getCdpresta() {
		return cdpresta;
	}


	public void setCdpresta(String cdpresta) {
		this.cdpresta = cdpresta;
	}


	public String getPtimport() {
		return ptimport;
	}


	public void setPtimport(String ptimport) {
		this.ptimport = ptimport;
	}


	public String getCdgarant() {
		return cdgarant;
	}


	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}


	public String getCdconval() {
		return cdconval;
	}


	public void setCdconval(String cdconval) {
		this.cdconval = cdconval;
	}


	public String getDescporc() {
		return descporc;
	}


	public void setDescporc(String descporc) {
		this.descporc = descporc;
	}


	public String getDescnume() {
		return descnume;
	}


	public void setDescnume(String descnume) {
		this.descnume = descnume;
	}


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


	public String getNmsolici() {
		return nmsolici;
	}


	public void setNmsolici(String nmsolici) {
		this.nmsolici = nmsolici;
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


	public String getCdtipsit() {
		return cdtipsit;
	}


	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCdperson() {
		return cdperson;
	}


	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}


	public String getFeocurre() {
		return feocurre;
	}


	public void setFeocurre(String feocurre) {
		this.feocurre = feocurre;
	}


	public String getNmautser() {
		return nmautser;
	}


	public void setNmautser(String nmautser) {
		this.nmautser = nmautser;
	}

	/*public String getNombreProveedor() {
		return nombreProveedor;
	}


	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}*/


	public String getNombreAsegurado() {
		return nombreAsegurado;
	}


	public void setNombreAsegurado(String nombreAsegurado) {
		this.nombreAsegurado = nombreAsegurado;
	}

	
	public String getCdmoneda() {
		return cdmoneda;
	}


	public void setCdmoneda(String cdmoneda) {
		this.cdmoneda = cdmoneda;
	}


	public String getDesTipomoneda() {
		return desTipomoneda;
	}


	public void setDesTipomoneda(String desTipomoneda) {
		this.desTipomoneda = desTipomoneda;
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}


	public String getDstipser() {
		return dstipser;
	}


	public void setDstipser(String dstipser) {
		this.dstipser = dstipser;
	}


	public String getDspresta() {
		return dspresta;
	}


	public void setDspresta(String dspresta) {
		this.dspresta = dspresta;
	}


	public String getNmpoliex() {
		return nmpoliex;
	}


	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}


	public String getTasacamb() {
		return tasacamb;
	}


	public void setTasacamb(String tasacamb) {
		this.tasacamb = tasacamb;
	}


	public String getPtimporta() {
		return ptimporta;
	}


	public void setPtimporta(String ptimporta) {
		this.ptimporta = ptimporta;
	}


	public String getNombProv() {
		return nombProv;
	}


	public void setNombProv(String nombProv) {
		this.nombProv = nombProv;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}	
	
}
