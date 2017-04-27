package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class AutorizacionServicioVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String nmautser;
	private String nmautant;
	private String cdperson;
	private String NombreCliente; 
	private String fesolici;
	private String feautori;
	private String fevencim;
	private String feingres;
	private String cdunieco;
	private String estado;
	private String cdramo;
	private String nmpoliza;
	private String nmsituac;
	private String cduniecs;
	private String cdgarant;
	private String DescGarantia;
	private String cdconval;
	private String DescSubGarantia;
	private String cdprovee;
	private String NombreProveedor;
	private String cdmedico;
	private String NombreMedico;
	private String mtsumadp;
	private String porpenal;
	private String copagofi;
	private String cdicd;
	private String DescICD;
	private String cdcausa;
	private String DescCausa;
	private String aaapertu;
	private String status;
	private String dstratam;
	private String dsobserv;
	private String dsnotas;
	private String fesistem;
	private String cduser;
	private String especialidadMedico;
	private String cveTipoAutorizaG;
	private String cdtipsit;
	private String aplicaZonaHosp;
	private String aplicaCirHos;
	private String descTipsit;
	private String genero; // (EGS)
	private String fenacimi; // (EGS)
	private String idTipoEvento;
	private String ntramite;
	private String aplicaNeg;
	private String tipoServ;
	private String numrecla;
	
	
	public String getCopagofi() {
		return copagofi;
	}

	public void setCopagofi(String copagofi) {
		this.copagofi = copagofi;
	}

	public String getNmautser() {
		return nmautser;
	}

	public void setNmautser(String nmautser) {
		this.nmautser = nmautser;
	}

	public String getNmautant() {
		return nmautant;
	}

	public void setNmautant(String nmautant) {
		this.nmautant = nmautant;
	}

	public String getCdperson() {
		return cdperson;
	}

	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}

	public String getNombreCliente() {
		return NombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		NombreCliente = nombreCliente;
	}

	public String getFesolici() {
		return fesolici;
	}

	public void setFesolici(String fesolici) {
		this.fesolici = fesolici;
	}

	public String getFeautori() {
		return feautori;
	}

	public void setFeautori(String feautori) {
		this.feautori = feautori;
	}

	public String getFevencim() {
		return fevencim;
	}

	public void setFevencim(String fevencim) {
		this.fevencim = fevencim;
	}

	public String getFeingres() {
		return feingres;
	}

	public void setFeingres(String feingres) {
		this.feingres = feingres;
	}

	public String getCdunieco() {
		return cdunieco;
	}


	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCdramo() {
		return cdramo;
	}

	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public String getNmsituac() {
		return nmsituac;
	}

	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}

	public String getCduniecs() {
		return cduniecs;
	}

	public void setCduniecs(String cduniecs) {
		this.cduniecs = cduniecs;
	}

	public String getCdgarant() {
		return cdgarant;
	}

	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}

	public String getDescGarantia() {
		return DescGarantia;
	}

	public void setDescGarantia(String descGarantia) {
		DescGarantia = descGarantia;
	}

	public String getCdconval() {
		return cdconval;
	}

	public void setCdconval(String cdconval) {
		this.cdconval = cdconval;
	}

	public String getDescSubGarantia() {
		return DescSubGarantia;
	}

	public void setDescSubGarantia(String descSubGarantia) {
		DescSubGarantia = descSubGarantia;
	}

	public String getCdprovee() {
		return cdprovee;
	}

	public void setCdprovee(String cdprovee) {
		this.cdprovee = cdprovee;
	}

	public String getNombreProveedor() {
		return NombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		NombreProveedor = nombreProveedor;
	}

	public String getCdmedico() {
		return cdmedico;
	}

	public void setCdmedico(String cdmedico) {
		this.cdmedico = cdmedico;
	}

	public String getNombreMedico() {
		return NombreMedico;
	}

	public void setNombreMedico(String nombreMedico) {
		NombreMedico = nombreMedico;
	}

	public String getMtsumadp() {
		return mtsumadp;
	}

	public void setMtsumadp(String mtsumadp) {
		this.mtsumadp = mtsumadp;
	}

	public String getPorpenal() {
		return porpenal;
	}

	public void setPorpenal(String porpenal) {
		this.porpenal = porpenal;
	}

	public String getCdicd() {
		return cdicd;
	}

	public void setCdicd(String cdicd) {
		this.cdicd = cdicd;
	}

	public String getDescICD() {
		return DescICD;
	}

	public void setDescICD(String descICD) {
		DescICD = descICD;
	}

	public String getCdcausa() {
		return cdcausa;
	}

	public void setCdcausa(String cdcausa) {
		this.cdcausa = cdcausa;
	}

	public String getDescCausa() {
		return DescCausa;
	}

	public void setDescCausa(String descCausa) {
		DescCausa = descCausa;
	}

	public String getAaapertu() {
		return aaapertu;
	}

	public void setAaapertu(String aaapertu) {
		this.aaapertu = aaapertu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDstratam() {
		return dstratam;
	}

	public void setDstratam(String dstratam) {
		this.dstratam = dstratam;
	}

	public String getDsobserv() {
		return dsobserv;
	}

	public void setDsobserv(String dsobserv) {
		this.dsobserv = dsobserv;
	}

	public String getDsnotas() {
		return dsnotas;
	}

	public void setDsnotas(String dsnotas) {
		this.dsnotas = dsnotas;
	}

	public String getFesistem() {
		return fesistem;
	}

	public void setFesistem(String fesistem) {
		this.fesistem = fesistem;
	}

	public String getCduser() {
		return cduser;
	}

	public void setCduser(String cduser) {
		this.cduser = cduser;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getEspecialidadMedico() {
		return especialidadMedico;
	}

	public void setEspecialidadMedico(String especialidadMedico) {
		this.especialidadMedico = especialidadMedico;
	}

	public String getCveTipoAutorizaG() {
		return cveTipoAutorizaG;
	}

	public void setCveTipoAutorizaG(String cveTipoAutorizaG) {
		this.cveTipoAutorizaG = cveTipoAutorizaG;
	}

	public String getCdtipsit() {
		return cdtipsit;
	}

	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public String getAplicaZonaHosp() {
		return aplicaZonaHosp;
	}

	public void setAplicaZonaHosp(String aplicaZonaHosp) {
		this.aplicaZonaHosp = aplicaZonaHosp;
	}

	public String getAplicaCirHos() {
		return aplicaCirHos;
	}

	public void setAplicaCirHos(String aplicaCirHos) {
		this.aplicaCirHos = aplicaCirHos;
	}

	public String getDescTipsit() {
		return descTipsit;
	}

	public void setDescTipsit(String descTipsit) {
		this.descTipsit = descTipsit;
	}	
	
	// metodos agregados (EGS)
	public String getGenero(){
		return genero;
	}
	
	public void setGenero(String genero){
		this.genero = genero;
	}
	
	public String getFenacimi(){
		return fenacimi;
	}
	
	public void setFenacimi(String fenacimi){
		this.fenacimi = fenacimi;
	} // fin (EGS)

    public String getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(String idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

	public String getNtramite() {
		return ntramite;
	}

	public void setNtramite(String ntramite) {
		this.ntramite = ntramite;
	}

	public String getAplicaNeg() {
		return aplicaNeg;
	}

	public void setAplicaNeg(String aplicaNeg) {
		this.aplicaNeg = aplicaNeg;
	}

	public String getTipoServ() {
		return tipoServ;
	}

	public void setTipoServ(String tipoServ) {
		this.tipoServ = tipoServ;
	}

	public String getNumrecla() {
		return numrecla;
	}

	public void setNumrecla(String numrecla) {
		this.numrecla = numrecla;
	}

}
