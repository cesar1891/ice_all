package mx.com.gseguros.ws.autosgs.emision.model;


import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EmisionAutosVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Numero Externo de la Poliza Generada
     */
    private String nmpoliex;
    
    /**
     * Numero de Subramo, (Corresponde al Ramo para GS)
     */
    private String subramo;
    
    private String sucursal;
    
    private String numeroEndoso;

    private String tipoEndoso;
    
    private int numeroIncisos;
    
    private boolean exitoRecibos;
    
    private boolean endosoSinRetarif;//Para usar si se se concluyo un endoso B sin retarificacion
    
    private Integer resRecibos;// respuesta del SP que valida la emision (recibos)
	
	public Integer getResRecibos() {
		return resRecibos;
	}

	public void setResRecibos(Integer resRecibos) {
		this.resRecibos = resRecibos;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getNmpoliex() {
		return nmpoliex;
	}

	public void setNmpoliex(String nmpoliex) {
		this.nmpoliex = nmpoliex;
	}

	public String getSubramo() {
		return subramo;
	}

	public void setSubramo(String subramo) {
		this.subramo = subramo;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public int getNumeroIncisos() {
		return numeroIncisos;
	}

	public void setNumeroIncisos(int numeroIncisos) {
		this.numeroIncisos = numeroIncisos;
	}

	public boolean isExitoRecibos() {
		return exitoRecibos;
	}

	public void setExitoRecibos(boolean exitoRecibos) {
		this.exitoRecibos = exitoRecibos;
	}

	public String getNumeroEndoso() {
		return numeroEndoso;
	}

	public void setNumeroEndoso(String numeroEndoso) {
		this.numeroEndoso = numeroEndoso;
	}

	public String getTipoEndoso() {
		return tipoEndoso;
	}

	public void setTipoEndoso(String tipoEndoso) {
		this.tipoEndoso = tipoEndoso;
	}

	public boolean isEndosoSinRetarif() {
		return endosoSinRetarif;
	}

	public void setEndosoSinRetarif(boolean endosoSinRetarif) {
		this.endosoSinRetarif = endosoSinRetarif;
	}
}
