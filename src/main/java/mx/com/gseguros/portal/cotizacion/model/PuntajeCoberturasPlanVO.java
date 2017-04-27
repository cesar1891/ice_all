package mx.com.gseguros.portal.cotizacion.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representa la sumatoria de coberturas distintas a las seleccionadas en pantalla
 * @author hector.lopez
 *
 */
public class PuntajeCoberturasPlanVO implements Serializable{

	
	private static final long serialVersionUID = -8828078148107907868L;

	private String cdplan;
	private String dsplan;
	private String dsplanLargo;
	private int coberturasAagregar;
	private int coberturasAquitar;
	private int totalCoberturasDif;

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
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

	public String getDsplanLargo() {
		return dsplanLargo;
	}

	public void setDsplanLargo(String dsplanLargo) {
		this.dsplanLargo = dsplanLargo;
	}

	public int getCoberturasAagregar() {
		return coberturasAagregar;
	}

	public void setCoberturasAagregar(int coberturasMas) {
		this.coberturasAagregar = coberturasMas;
	}

	public int getCoberturasAquitar() {
		return coberturasAquitar;
	}

	public void setCoberturasAquitar(int coberturasMenos) {
		this.coberturasAquitar = coberturasMenos;
	}

	public int getTotalCoberturasDif() {
		return totalCoberturasDif;
	}

	public void setTotalCoberturasDif(int totalCoberturasDif) {
		this.totalCoberturasDif = totalCoberturasDif;
	}
	
}
