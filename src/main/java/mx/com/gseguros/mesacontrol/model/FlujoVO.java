package mx.com.gseguros.mesacontrol.model;

import org.apache.commons.lang.StringUtils;

import mx.com.gseguros.utils.Utils;

public class FlujoVO
{
	private String ntramite
	               ,status
	               ,cdtipflu
	               ,cdflujomc
	               ,webid
	               ,tipoent
	               ,claveent
	               ,cdunieco
	               ,cdramo
	               ,estado
	               ,nmpoliza
	               ,nmsituac
	               ,nmsuplem
	               ,aux;

	public String getNtramite() {
		return ntramite;
	}

	public void setNtramite(String ntramite) {
		this.ntramite = ntramite;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCdtipflu() {
		return cdtipflu;
	}

	public void setCdtipflu(String cdtipflu) {
		this.cdtipflu = cdtipflu;
	}

	public String getCdflujomc() {
		return cdflujomc;
	}

	public void setCdflujomc(String cdflujomc) {
		this.cdflujomc = cdflujomc;
	}

	public String getWebid() {
		return webid;
	}

	public void setWebid(String webid) {
		this.webid = webid;
	}

	public String getTipoent() {
		return tipoent;
	}

	public void setTipoent(String tipoent) {
		this.tipoent = tipoent;
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

	public String getClaveent() {
		return claveent;
	}

	public void setClaveent(String claveent) {
		this.claveent = claveent;
	}
	
	public String getAux() {
		return aux;
	}

	public void setAux(String aux) {
		this.aux = aux;
	}

	@Override
	public String toString()
	{
		return Utils.log(
				 "\n***************************************"
				,"\n**************** FLUJO ****************"
				,"\n*** ntramite  = " , StringUtils.rightPad(this.ntramite  , 20, " "), "***"
				,"\n*** status    = " , StringUtils.rightPad(this.status    , 20, " "), "***"
				,"\n*** cdtipflu  = " , StringUtils.rightPad(this.cdtipflu  , 20, " "), "***"
				,"\n*** cdflujomc = " , StringUtils.rightPad(this.cdflujomc , 20, " "), "***"
				,"\n*** webid     = " , StringUtils.rightPad(this.webid     , 20, " "), "***"
				,"\n*** tipoent   = " , StringUtils.rightPad(this.tipoent   , 20, " "), "***"
				,"\n*** claveent  = " , StringUtils.rightPad(this.claveent  , 20, " "), "***"
				,"\n*** cdunieco  = " , StringUtils.rightPad(this.cdunieco  , 20, " "), "***"
				,"\n*** cdramo    = " , StringUtils.rightPad(this.cdramo    , 20, " "), "***"
				,"\n*** estado    = " , StringUtils.rightPad(this.estado    , 20, " "), "***"
				,"\n*** nmpoliza  = " , StringUtils.rightPad(this.nmpoliza  , 20, " "), "***"
				,"\n*** nmsituac  = " , StringUtils.rightPad(this.nmsituac  , 20, " "), "***"
				,"\n*** nmsuplem  = " , StringUtils.rightPad(this.nmsuplem  , 20, " "), "***"
				,"\n*** aux       = " , StringUtils.rightPad(this.aux       , 20, " "), "***"
				,"\n***************************************"
				,"\n***************************************"
				);
	}
}