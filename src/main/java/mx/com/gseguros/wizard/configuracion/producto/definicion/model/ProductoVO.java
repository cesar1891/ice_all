package mx.com.gseguros.wizard.configuracion.producto.definicion.model;

import java.io.Serializable;
import java.util.List;

public class ProductoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8072508710080484495L;
	/**
	 * TramosVO
	 */
    private String mode;
    private int codigoRamo;    
    private String descripcionRamo;    
    private String descripcion;    
    private String codigoTipoParametro;    
    private String descripcionTipoParametro;    
    private String codigoTipoRamo;    
    private String descripcionTipoRamo;
    private String codigoTipoPoliza;
    private String descripcionTipoPoliza;
    private String codigoTipoSeguro;
    private String descripcionTipoSeguro;
    private String switchSuscripcion;
    private String switchClausulasNoTipificadas;    
    private int tipoCalculoPolizasTemporales; 
    private String switchRehabilitacion;    
    private int mesesBeneficios;         
    private String switchPrimasPeriodicas;    
    private String switchPermisoPagosOtrasMonedas;       
    private String switchReaseguro;    
    private String switchSiniestros;    
    private String switchTarifa;    
    private String switchReinstalacionAutomatica;    
    private String switchPrimasUnicas;    
    private String switchDistintasPolizasPorAsegurado;    
    private String switchPolizasDeclarativas;       
    private String switchPreavisoCartera;    
    private String switchTipo;        
    private String switchTarifaDireccionalTotal;    
    private int cantidadDiasReclamacion;       
    private String switchSubincisos;     
    
    private String temporal = "N";
    private String renovable = "N";
    private String vidaEntera = "N";
    
    private List<TipoPoliza> listTiposPoliza;
    
    private String switchCancelacion;
    
    private String switchEndosos;
    
    //Getters y Setters

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDescripcionRamo() {
		return descripcionRamo;
	}

	public void setDescripcionRamo(String descripcionRamo) {
		this.descripcionRamo = descripcionRamo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigoTipoParametro() {
		return codigoTipoParametro;
	}

	public void setCodigoTipoParametro(String codigoTipoParametro) {
		this.codigoTipoParametro = codigoTipoParametro;
	}

	public String getDescripcionTipoParametro() {
		return descripcionTipoParametro;
	}

	public void setDescripcionTipoParametro(String descripcionTipoParametro) {
		this.descripcionTipoParametro = descripcionTipoParametro;
	}

	public String getCodigoTipoRamo() {
		return codigoTipoRamo;
	}

	public void setCodigoTipoRamo(String codigoTipoRamo) {
		this.codigoTipoRamo = codigoTipoRamo;
	}

	public String getDescripcionTipoRamo() {
		return descripcionTipoRamo;
	}

	public void setDescripcionTipoRamo(String descripcionTipoRamo) {
		this.descripcionTipoRamo = descripcionTipoRamo;
	}

	public String getSwitchSuscripcion() {
		return switchSuscripcion;
	}

	public void setSwitchSuscripcion(String switchSuscripcion) {
		this.switchSuscripcion = switchSuscripcion;
	}

	public String getSwitchClausulasNoTipificadas() {
		return switchClausulasNoTipificadas;
	}

	public void setSwitchClausulasNoTipificadas(String switchClausulasNoTipificadas) {
		this.switchClausulasNoTipificadas = switchClausulasNoTipificadas;
	}

	public String getSwitchRehabilitacion() {
		return switchRehabilitacion;
	}

	public void setSwitchRehabilitacion(String switchRehabilitacion) {
		this.switchRehabilitacion = switchRehabilitacion;
	}
	
	public String getSwitchPrimasPeriodicas() {
		return switchPrimasPeriodicas;
	}

	public void setSwitchPrimasPeriodicas(String switchPrimasPeriodicas) {
		this.switchPrimasPeriodicas = switchPrimasPeriodicas;
	}

	public String getSwitchPermisoPagosOtrasMonedas() {
		return switchPermisoPagosOtrasMonedas;
	}

	public void setSwitchPermisoPagosOtrasMonedas(
			String switchPermisoPagosOtrasMonedas) {
		this.switchPermisoPagosOtrasMonedas = switchPermisoPagosOtrasMonedas;
	}

	public String getSwitchSiniestros() {
		return switchSiniestros;
	}
	
	public void setSwitchSiniestros(String switchSiniestros) {
		this.switchSiniestros = switchSiniestros;
	}
	
	public String getSwitchTarifa() {
		return switchTarifa;
	}
	
	public void setSwitchTarifa(String switchTarifa) {
		this.switchTarifa = switchTarifa;
	}
	
	public String getSwitchReinstalacionAutomatica() {
		return switchReinstalacionAutomatica;
	}
	
	public void setSwitchReinstalacionAutomatica(
			String switchReinstalacionAutomatica) {
		this.switchReinstalacionAutomatica = switchReinstalacionAutomatica;
	}
	
	public String getSwitchPrimasUnicas() {
		return switchPrimasUnicas;
	}
	
	public void setSwitchPrimasUnicas(String switchPrimasUnicas) {
		this.switchPrimasUnicas = switchPrimasUnicas;
	}
	
	public String getSwitchDistintasPolizasPorAsegurado() {
		return switchDistintasPolizasPorAsegurado;
	}
	
	public void setSwitchDistintasPolizasPorAsegurado(
			String switchDistintasPolizasPorAsegurado) {
		this.switchDistintasPolizasPorAsegurado = switchDistintasPolizasPorAsegurado;
	}
	
	public String getSwitchPolizasDeclarativas() {
		return switchPolizasDeclarativas;
	}
	
	public void setSwitchPolizasDeclarativas(String switchPolizasDeclarativas) {
		this.switchPolizasDeclarativas = switchPolizasDeclarativas;
	}
	
	public String getSwitchTipo() {
		return switchTipo;
	}
	
	public void setSwitchTipo(String switchTipo) {
		this.switchTipo = switchTipo;
	}
	
	public String getSwitchTarifaDireccionalTotal() {
		return switchTarifaDireccionalTotal;
	}
	
	public void setSwitchTarifaDireccionalTotal(String switchTarifaDireccionalTotal) {
		this.switchTarifaDireccionalTotal = switchTarifaDireccionalTotal;
	}
	
	public String getSwitchSubincisos() {
		return switchSubincisos;
	}
	
	public void setSwitchSubincisos(String switchSubincisos) {
		this.switchSubincisos = switchSubincisos;
	}
	
	public List<TipoPoliza> getListTiposPoliza() {
		return listTiposPoliza;
	}
	
	public void setListTiposPoliza(List<TipoPoliza> listTiposPoliza) {
		this.listTiposPoliza = listTiposPoliza;
	}
	
	public String getSwitchReaseguro() {
		return switchReaseguro;
	}
	
	public void setSwitchReaseguro(String switchReaseguro) {
		this.switchReaseguro = switchReaseguro;
	}
	
	public String getSwitchPreavisoCartera() {
		return switchPreavisoCartera;
	}
	
	public void setSwitchPreavisoCartera(String switchPreavisoCartera) {
		this.switchPreavisoCartera = switchPreavisoCartera;
	}
	
	public int getCodigoRamo() {
		return codigoRamo;
	}
	
	public void setCodigoRamo(int codigoRamo) {
		this.codigoRamo = codigoRamo;
	}
	
	public int getTipoCalculoPolizasTemporales() {
		return tipoCalculoPolizasTemporales;
	}
	
	public void setTipoCalculoPolizasTemporales(int tipoCalculoPolizasTemporales) {
		this.tipoCalculoPolizasTemporales = tipoCalculoPolizasTemporales;
	}
	
	public int getMesesBeneficios() {
		return mesesBeneficios;
	}
	
	public void setMesesBeneficios(int mesesBeneficios) {
		this.mesesBeneficios = mesesBeneficios;
	}
	
	public int getCantidadDiasReclamacion() {
		return cantidadDiasReclamacion;
	}
	
	public void setCantidadDiasReclamacion(int cantidadDiasReclamacion) {
		this.cantidadDiasReclamacion = cantidadDiasReclamacion;
	}
	
	public String getCodigoTipoPoliza() {
		return codigoTipoPoliza;
	}
	
	public void setCodigoTipoPoliza(String codigoTipoPoliza) {
		this.codigoTipoPoliza = codigoTipoPoliza;
	}
	
	public String getDescripcionTipoPoliza() {
		return descripcionTipoPoliza;
	}
	
	public void setDescripcionTipoPoliza(String descripcionTipoPoliza) {
		this.descripcionTipoPoliza = descripcionTipoPoliza;
	}
	
	public String getCodigoTipoSeguro() {
		return codigoTipoSeguro;
	}
	
	public void setCodigoTipoSeguro(String codigoTipoSeguro) {
		this.codigoTipoSeguro = codigoTipoSeguro;
	}
	
	public String getDescripcionTipoSeguro() {
		return descripcionTipoSeguro;
	}
	
	public void setDescripcionTipoSeguro(String descripcionTipoSeguro) {
		this.descripcionTipoSeguro = descripcionTipoSeguro;
	}

	/**
	 * @return the temporal
	 */
	public String getTemporal() {
		return temporal;
	}

	/**
	 * @param temporal the temporal to set
	 */
	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}

	/**
	 * @return the renovable
	 */
	public String getRenovable() {
		return renovable;
	}

	/**
	 * @param renovable the renovable to set
	 */
	public void setRenovable(String renovable) {
		this.renovable = renovable;
	}

	/**
	 * @return the vidaEntera
	 */
	public String getVidaEntera() {
		return vidaEntera;
	}

	/**
	 * @param vidaEntera the vidaEntera to set
	 */
	public void setVidaEntera(String vidaEntera) {
		this.vidaEntera = vidaEntera;
	}

	public String getSwitchCancelacion() {
		return switchCancelacion;
	}

	public void setSwitchCancelacion(String switchCancelacion) {
		this.switchCancelacion = switchCancelacion;
	}

	public String getSwitchEndosos() {
		return switchEndosos;
	}

	public void setSwitchEndosos(String switchEndosos) {
		this.switchEndosos = switchEndosos;
	}
}
