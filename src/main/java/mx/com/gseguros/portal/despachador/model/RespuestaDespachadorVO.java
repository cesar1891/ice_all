package mx.com.gseguros.portal.despachador.model;

import mx.com.gseguros.utils.Utils;

public class RespuestaDespachadorVO {
	
    private String cdunieco,
                   cdusuari,
                   cdsisrol,
                   cdtipasig,
                   status;
	private boolean encolado;
	
	public String getCdunieco() {
		return cdunieco;
	}
	
	public void setCdunieco(String cdunieco) {
		this.cdunieco = cdunieco;
	}
	
	public String getCdusuari() {
		return cdusuari;
	}
	
	public void setCdusuari(String cdusuari) {
		this.cdusuari = cdusuari;
	}
	
	public String getCdsisrol() {
        return cdsisrol;
    }

    public void setCdsisrol(String cdsisrol) {
        this.cdsisrol = cdsisrol;
    }

    public String getCdtipasig() {
        return cdtipasig;
    }

    public void setCdtipasig(String cdtipasig) {
        this.cdtipasig = cdtipasig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEncolado() {
        return encolado;
    }

    public void setEncolado(boolean encolado) {
        this.encolado = encolado;
    }

    @Override
	public String toString() {
		return Utils.log("\n************************************",
		                 "\n****** RespuestaDespachadorVO ******",
		                 "\n****** cdunieco  = " , cdunieco,
		                 "\n****** cdusuari  = " , cdusuari,
		                 "\n****** cdsisrol  = " , cdsisrol,
                         "\n****** cdtipasig = " , cdtipasig,
                         "\n****** status    = " , status,
		                 "\n****** encolado  = " , encolado,
		                 "\n************************************");
	}
}