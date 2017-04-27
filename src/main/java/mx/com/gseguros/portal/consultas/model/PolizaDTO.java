package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class PolizaDTO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	
	private String nmpoliex;
	private String cdramoext;
	private String cduniext;
	private String nmsolici;
	private String feemisio;
	private String feefecto;
	private String cdmoneda;
	private String dsmoneda;
	private String ottempot;
	private String dstempot;
	private String feproren;
	private String fevencim;
	private String nmrenova;
	private String cdperpag;
	private String dsperpag;
	private String swtarifi;
	private String dstarifi;
	private String cdtipcoa;
	private String dstipcoa;
	private String nmcuadro;
	private String dscuadro;
	private String porredau;
	private String ptpritot;
	private String cdmotanu;
	private String dsmotanu;
	private String cdperson;
	private String titular;
	private String cdrfc;
	private String cdagente;
	private String statuspoliza;
	private String cdplan;
	private String dsplan;
	private String cdramo;
	private String dsramo;
	private String cdtipsit;
	private String cdunieco;
	private String dsunieco;
	private String nmpolant;
	private String tipopol;
	
	/**
	 * TODO: Datos Temporales, quitar cuando las caratulas de autos ya tengan la informacion completa
	 */
	private String reduceGS;
	private String gestoria;
	private String cobvida;
	
	
	public String getCobvida() {
		return cobvida;
	}


	public void setCobvida(String cobvida) {
		this.cobvida = cobvida;
	}


	/**
	 * Modalidad
	 */
	private String dstipsit;
	/**
	 * C&oacute;digo de la p&oacute;liza usado en SISA 
	 */
	private String icodpoliza;
	
	/**
	 * Fecha de pago
	 */
	private String fepag;
	/**
	 * Status de pago
	 */
	private String statuspago;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	
	// Getters and setters:

	public String getNmpoliex() {
		return nmpoliex;
	}


	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}


	public String getNmsolici() {
		return nmsolici;
	}


	public void setNmsolici(String nmsolici) {
		this.nmsolici = nmsolici;
	}


	public String getFeemisio() {
		return feemisio;
	}


	public void setFeemisio(String feemisio) {
		this.feemisio = feemisio;
	}


	public String getFeefecto() {
		return feefecto;
	}


	public void setFeefecto(String feefecto) {
		this.feefecto = feefecto;
	}


	public String getCdmoneda() {
		return cdmoneda;
	}


	public void setCdmoneda(String cdmoneda) {
		this.cdmoneda = cdmoneda;
	}


	public String getDsmoneda() {
		return dsmoneda;
	}


	public void setDsmoneda(String dsmoneda) {
		this.dsmoneda = dsmoneda;
	}


	public String getOttempot() {
		return ottempot;
	}


	public void setOttempot(String ottempot) {
		this.ottempot = ottempot;
	}


	public String getDstempot() {
		return dstempot;
	}


	public void setDstempot(String dstempot) {
		this.dstempot = dstempot;
	}


	public String getFeproren() {
		return feproren;
	}


	public void setFeproren(String feproren) {
		this.feproren = feproren;
	}


	public String getFevencim() {
		return fevencim;
	}


	public void setFevencim(String fevencim) {
		this.fevencim = fevencim;
	}


	public String getNmrenova() {
		return nmrenova;
	}


	public void setNmrenova(String nmrenova) {
		this.nmrenova = nmrenova;
	}


	public String getCdperpag() {
		return cdperpag;
	}


	public void setCdperpag(String cdperpag) {
		this.cdperpag = cdperpag;
	}


	public String getDsperpag() {
		return dsperpag;
	}


	public void setDsperpag(String dsperpag) {
		this.dsperpag = dsperpag;
	}


	public String getSwtarifi() {
		return swtarifi;
	}


	public void setSwtarifi(String swtarifi) {
		this.swtarifi = swtarifi;
	}


	public String getDstarifi() {
		return dstarifi;
	}


	public void setDstarifi(String dstarifi) {
		this.dstarifi = dstarifi;
	}


	public String getCdtipcoa() {
		return cdtipcoa;
	}


	public void setCdtipcoa(String cdtipcoa) {
		this.cdtipcoa = cdtipcoa;
	}


	public String getDstipcoa() {
		return dstipcoa;
	}


	public void setDstipcoa(String dstipcoa) {
		this.dstipcoa = dstipcoa;
	}


	public String getNmcuadro() {
		return nmcuadro;
	}


	public void setNmcuadro(String nmcuadro) {
		this.nmcuadro = nmcuadro;
	}


	public String getDscuadro() {
		return dscuadro;
	}


	public void setDscuadro(String dscuadro) {
		this.dscuadro = dscuadro;
	}


	public String getPorredau() {
		return porredau;
	}


	public void setPorredau(String porredau) {
		this.porredau = porredau;
	}


	public String getPtpritot() {
		return ptpritot;
	}


	public void setPtpritot(String ptpritot) {
		this.ptpritot = ptpritot;
	}


	public String getCdmotanu() {
		return cdmotanu;
	}


	public void setCdmotanu(String cdmotanu) {
		this.cdmotanu = cdmotanu;
	}


	public String getDsmotanu() {
		return dsmotanu;
	}


	public void setDsmotanu(String dsmotanu) {
		this.dsmotanu = dsmotanu;
	}


	public String getCdperson() {
		return cdperson;
	}


	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}


	public String getTitular() {
		return titular;
	}


	public void setTitular(String titular) {
		this.titular = titular;
	}


	public String getCdrfc() {
		return cdrfc;
	}


	public void setCdrfc(String cdrfc) {
		this.cdrfc = cdrfc;
	}


	public String getCdagente() {
		return cdagente;
	}


	public void setCdagente(String cdagente) {
		this.cdagente = cdagente;
	}


	public String getStatuspoliza() {
		return statuspoliza;
	}


	public void setStatuspoliza(String statuspoliza) {
		this.statuspoliza = statuspoliza;
	}


	public String getCdplan() {
		return cdplan;
	}


	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}


	public String getDsplan() {
		return dsplan;
	}


	public void setDsplan(String dsplan) {
		this.dsplan = dsplan;
	}


	public String getCdramo() {
		return cdramo;
	}


	public void setCdramo(String cdramo) {
		this.cdramo = cdramo;
	}


	public String getDsramo() {
		return dsramo;
	}


	public void setDsramo(String dsramo) {
		this.dsramo = dsramo;
	}


	public String getCdtipsit() {
		return cdtipsit;
	}


	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}


	public String getCdunieco() {
		return cdunieco;
	}


	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}


	public String getDsunieco() {
		return dsunieco;
	}


	public void setDsunieco(String dsunieco) {
		this.dsunieco = dsunieco;
	}


	public String getNmpolant() {
		return nmpolant;
	}


	public void setNmpolant(String nmpolant) {
		this.nmpolant = nmpolant;
	}


	public String getDstipsit() {
		return dstipsit;
	}


	public void setDstipsit(String dstipsit) {
		this.dstipsit = dstipsit;
	}


	public String getIcodpoliza() {
		return icodpoliza;
	}


	public void setIcodpoliza(String icodpoliza) {
		this.icodpoliza = icodpoliza;
	}


	public String getFepag() {
		return fepag;
	}


	public void setFepag(String fepag) {
		this.fepag = fepag;
	}


	public String getStatuspago() {
		return statuspago;
	}


	public void setStatuspago(String statuspago) {
		this.statuspago = statuspago;
	}


	public String getCdramoext() {
		return cdramoext;
	}


	public void setCdramoext(String cdramoext) {
		this.cdramoext = cdramoext;
	}


	public String getCduniext() {
		return cduniext;
	}


	public void setCduniext(String cduniext) {
		this.cduniext = cduniext;
	}


	public String getReduceGS() {
		return reduceGS;
	}


	public void setReduceGS(String reduceGS) {
		this.reduceGS = reduceGS;
	}


	public String getGestoria() {
		return gestoria;
	}


	public void setGestoria(String gestoria) {
		this.gestoria = gestoria;
	}


	public String getTipopol() {
		return tipopol;
	}


	public void setTipopol(String tipopol) {
		this.tipopol = tipopol;
	}
	
	
	
}