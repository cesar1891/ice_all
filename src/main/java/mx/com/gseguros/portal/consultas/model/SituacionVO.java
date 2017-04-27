package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hector.lopez
 *
 */
public class SituacionVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8555353864912795413L;

	private String nmsituac;
	private String nmsuplem;
	private String cdtipsit;
	private String cdplan;
	private String dsplan;
	private String fecharef;
	private String fefecsit;
	private String status;
	private String swreduci;
	private String cdagrupa;
	private String cdestado;
	private String cdgrupo;
	private String nmsituaext;
	private String nmsitaux;
	private String nmsbsitext;
	private String inciso;
	private String cduniage;
	private String cdelemento;
	private String dstipsit;
	
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}


	public String getNmsituac() {
		return nmsituac;
	}


	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}


	public String getNmsuplem() {
		return nmsuplem;
	}


	public void setNmsuplem(String nmsuplem) {
		this.nmsuplem = nmsuplem;
	}


	public String getCdtipsit() {
		return cdtipsit;
	}


	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}


	public String getCdplan() {
		return cdplan;
	}


	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}


	public String getFecharef() {
		return fecharef;
	}


	public void setFecharef(String fecharef) {
		this.fecharef = fecharef;
	}


	public String getFefecsit() {
		return fefecsit;
	}


	public void setFefecsit(String fefecsit) {
		this.fefecsit = fefecsit;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getSwreduci() {
		return swreduci;
	}


	public void setSwreduci(String swreduci) {
		this.swreduci = swreduci;
	}


	public String getCdagrupa() {
		return cdagrupa;
	}


	public void setCdagrupa(String cdagrupa) {
		this.cdagrupa = cdagrupa;
	}


	public String getCdestado() {
		return cdestado;
	}


	public void setCdestado(String cdestado) {
		this.cdestado = cdestado;
	}


	public String getCdgrupo() {
		return cdgrupo;
	}


	public void setCdgrupo(String cdgrupo) {
		this.cdgrupo = cdgrupo;
	}


	public String getNmsituaext() {
		return nmsituaext;
	}


	public void setNmsituaext(String nmsituaext) {
		this.nmsituaext = nmsituaext;
	}


	public String getNmsitaux() {
		return nmsitaux;
	}


	public void setNmsitaux(String nmsitaux) {
		this.nmsitaux = nmsitaux;
	}


	public String getNmsbsitext() {
		return nmsbsitext;
	}


	public void setNmsbsitext(String nmsbsitext) {
		this.nmsbsitext = nmsbsitext;
	}


	public String getInciso() {
		return inciso;
	}


	public void setInciso(String inciso) {
		this.inciso = inciso;
	}


	public String getCduniage() {
		return cduniage;
	}


	public void setCduniage(String cduniage) {
		this.cduniage = cduniage;
	}


	public String getCdelemento() {
		return cdelemento;
	}


	public void setCdelemento(String cdelemento) {
		this.cdelemento = cdelemento;
	}


	public String getDstipsit() {
		return dstipsit;
	}


	public void setDstipsit(String dstipsit) {
		this.dstipsit = dstipsit;
	}


	public String getDsplan() {
		return dsplan;
	}


	public void setDsplan(String dsplan) {
		this.dsplan = dsplan;
	}	
	
}
