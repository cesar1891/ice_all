/**
 * 
 */
package mx.com.aon.flujos.cotizacion.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 *  Clase Value Object correspondiente a un registro obtenido en resultados de cotizacion
 * 
 * @author  aurora.lozada
 * 
 */

public class ResultadoCotizacionVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 150094115711007710L;
    
    /**
     *
     *IDENTIFICADOR UNICO
     */
    private String cdIdentifica;
    /**
     *
     *CLAVE DE AON
     */
    private String cdUnieco;
    /**
     *
     *CLAVE DEL PRODUCTO
     */
    private String cdRamo;
    /**
     *
     *ESTADO
     */
    private String estado;
    /**
     *NUMERO POLIZA 
     */
    private String nmPoliza;
    /**
     *NUMERO DEL SUPLEMENTO
     */
    private String nmSuplem;
    /**
     *ESTADO DE LA POLIZA 
     */
    private String status;
    /**
     *CLAVE DEL PLAN 
     */
    private String cdPlan;
    /**
     *DESCRIPCION DEL PLAN 
     */
    private String dsPlan;
    /**
     *PRIMA ANUAL
     */
    private String mnPrima;
    /**
     *CLAVE DE LA ASEGURADORA 
     */
    private String cdCiaaseg;
    /**
     *DESCRIPCION DE LA ASEGURADORA 
     */
    private String dsUnieco;
    /**
     *CLAVE FORMA DE PAGO 
     */
    private String cdPerpag;
    /**
     *DESCRIPCION FORMA DE PAGO 
     */
    private String dsPerpag;
    /**
     *CLAVE TIPO SITUAC 
     */
    private String cdTipsit;
    /**
     *DESCRIPCION TIPO DE SITUAC 
     */
    private String dsTipsit;
    /**
     *NUMERO DE SITUACION 
     */
    private String numeroSituacion;
    /**
     *CLAVE DE LA COBERTURA 
     */
    private String cdGarant;
    /**
     *DESCRIPCION DE LA COBERTURA 
     */
    private String dsGarant;
    /**
     *OBLIGATORIO 
     */
    private String swOblig;
    /**
     *SUMA ASEGURADA 
     */
    private String sumaAseg;
    /**
     *PRIMA POR FORMA DE PAGO 
     */
    private String nMimpfpg;

    private String primaFormap;
    /**
     *FECHA DE EMISION
     */
    private String feEmisio;
    /**
     *FECHA DE VENCIMIENTO 
     */
    private String feVencim;
    
    
    //Getters && Setters
    /**
     * @return the cdIdentifica
     */
    public String getCdIdentifica() {
        return cdIdentifica;
    }

    /**
     * @param cdIdentifica the cdIdentifica to set
     */
    public void setCdIdentifica(String cdIdentifica) {
        this.cdIdentifica = cdIdentifica;
    }

    /**
     * @return the cdUnieco
     */
    public String getCdUnieco() {
        return cdUnieco;
    }

    /**
     * @param cdUnieco the cdUnieco to set
     */
    public void setCdUnieco(String cdUnieco) {
        this.cdUnieco = cdUnieco;
    }

    /**
     * @return the cdRamo
     */
    public String getCdRamo() {
        return cdRamo;
    }

    /**
     * @param cdRamo the cdRamo to set
     */
    public void setCdRamo(String cdRamo) {
        this.cdRamo = cdRamo;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the nmPoliza
     */
    public String getNmPoliza() {
        return nmPoliza;
    }

    /**
     * @param nmPoliza the nmPoliza to set
     */
    public void setNmPoliza(String nmPoliza) {
        this.nmPoliza = nmPoliza;
    }

    /**
     * @return the nmSuplem
     */
    public String getNmSuplem() {
        return nmSuplem;
    }

    /**
     * @param nmSuplem the nmSuplem to set
     */
    public void setNmSuplem(String nmSuplem) {
        this.nmSuplem = nmSuplem;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the cdPlan
     */
    public String getCdPlan() {
        return cdPlan;
    }

    /**
     * @param cdPlan the cdPlan to set
     */
    public void setCdPlan(String cdPlan) {
        this.cdPlan = cdPlan;
    }

    /**
     * @return the dsPlan
     */
    public String getDsPlan() {
        return dsPlan;
    }

    /**
     * @param dsPlan the dsPlan to set
     */
    public void setDsPlan(String dsPlan) {
        this.dsPlan = dsPlan;
    }

    /**
     * @return the mnPrima
     */
    public String getMnPrima() {
        return mnPrima;
    }

    /**
     * @param mnPrima the mnPrima to set
     */
    public void setMnPrima(String mnPrima) {
        this.mnPrima = mnPrima;
    }

    /**
     * @return the cdCiaaseg
     */
    public String getCdCiaaseg() {
        return cdCiaaseg;
    }

    /**
     * @param cdCiaaseg the cdCiaaseg to set
     */
    public void setCdCiaaseg(String cdCiaaseg) {
        this.cdCiaaseg = cdCiaaseg;
    }

    /**
     * @return the dsUnieco
     */
    public String getDsUnieco() {
        return dsUnieco;
    }

    /**
     * @param dsUnieco the dsUnieco to set
     */
    public void setDsUnieco(String dsUnieco) {
        this.dsUnieco = dsUnieco;
    }

    /**
     * @return the cdPerpag
     */
    public String getCdPerpag() {
        return cdPerpag;
    }

    /**
     * @param cdPerpag the cdPerpag to set
     */
    public void setCdPerpag(String cdPerpag) {
        this.cdPerpag = cdPerpag;
    }

  

    /**
     * @return the cdTipsit
     */
    public String getCdTipsit() {
        return cdTipsit;
    }

    /**
     * @param cdTipsit the cdTipsit to set
     */
    public void setCdTipsit(String cdTipsit) {
        this.cdTipsit = cdTipsit;
    }

    /**
     * @return the dsTipsit
     */
    public String getDsTipsit() {
        return dsTipsit;
    }

    /**
     * @param dsTipsit the dsTipsit to set
     */
    public void setDsTipsit(String dsTipsit) {
        this.dsTipsit = dsTipsit;
    }

    /**
     * @return the dsPerpag
     */
    public String getDsPerpag() {
        return dsPerpag;
    }

    /**
     * @param dsPerpag the dsPerpag to set
     */
    public void setDsPerpag(String dsPerpag) {
        this.dsPerpag = dsPerpag;
    }

	/**
	 * 
	 * @return
	 */
	public String getCdGarant() {
		return cdGarant;
	}
	/**
	 * 
	 * @param cdGarant
	 */
	public void setCdGarant(String cdGarant) {
		this.cdGarant = cdGarant;
	}
	/**
	 * 
	 * @return
	 */
	public String getDsGarant() {
		return dsGarant;
	}
	/**
	 * 
	 * @param dsGarant
	 */
	public void setDsGarant(String dsGarant) {
		this.dsGarant = dsGarant;
	}
	/**
	 * 
	 * @return
	 */
	public String getSwOblig() {
		return swOblig;
	}
	/**
	 * 
	 * @param swOblig
	 */
	public void setSwOblig(String swOblig) {
		this.swOblig = swOblig;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSumaAseg() {
		return sumaAseg;
	}
	/**
	 * 
	 * @param sumaAseg
	 */
	public void setSumaAseg(String sumaAseg) {
		this.sumaAseg = sumaAseg;
	}

	/**
	 * 
	 * @return
	 */
	public String getNMimpfpg() {
		return nMimpfpg;
	}
	/**
	 * 
	 * @param mimpfpg
	 */
	public void setNMimpfpg(String mimpfpg) {
		nMimpfpg = mimpfpg;
	}

	public String getPrimaFormap() {
		return primaFormap;
	}

	public void setPrimaFormap(String primaFormap) {
		this.primaFormap = primaFormap;
	}

	/**
	 * @return the feEmisio
	 */
	public String getFeEmisio() {
		return feEmisio;
	}

	/**
	 * @param feEmisio the feEmisio to set
	 */
	public void setFeEmisio(String feEmisio) {
		this.feEmisio = feEmisio;
	}

	/**
	 * @return the feVencim
	 */
	public String getFeVencim() {
		return feVencim;
	}

	/**
	 * @param feVencim the feVencim to set
	 */
	public void setFeVencim(String feVencim) {
		this.feVencim = feVencim;
	}

	public String getNumeroSituacion() {
		return numeroSituacion;
	}

	public void setNumeroSituacion(String numeroSituacion) {
		this.numeroSituacion = numeroSituacion;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
