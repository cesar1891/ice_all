/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.aon.portal.web.model;

import java.math.BigDecimal;
import java.util.List;

import mx.com.aon.portal2.web.GenericVO;

/**
 *
 * @author Jair
 */
public class CotizacionSaludVO {
    
    private Long id;
    private GenericVO estado;
    private GenericVO ciudad;
    private BigDecimal deducible;
    private GenericVO copago;
    private GenericVO sumaSegurada;
    private GenericVO circuloHospitalario;
    private GenericVO coberturaVacunas;
    private GenericVO coberturaPrevencionEnfermedadesAdultos;
    private GenericVO maternidad;
    private GenericVO sumaAseguradaMaternidad;
    private GenericVO baseTabuladorReembolso;
    private GenericVO costoEmergenciaExtranjero;
    private GenericVO coberturaEliminacionPenalizacionCambioZona;
    
    private List<IncisoSaludVO> incisos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenericVO getCiudad() {
        return ciudad;
    }

    public void setCiudad(GenericVO ciudad) {
        this.ciudad = ciudad;
    }

    public BigDecimal getDeducible() {
        return deducible;
    }

    public void setDeducible(BigDecimal deducible) {
        this.deducible = deducible;
    }

    public GenericVO getCopago() {
        return copago;
    }

    public void setCopago(GenericVO copago) {
        this.copago = copago;
    }

    public GenericVO getSumaSegurada() {
        return sumaSegurada;
    }

    public void setSumaSegurada(GenericVO sumaSegurada) {
        this.sumaSegurada = sumaSegurada;
    }

    public GenericVO getCirculoHospitalario() {
        return circuloHospitalario;
    }

    public void setCirculoHospitalario(GenericVO circuloHospitalario) {
        this.circuloHospitalario = circuloHospitalario;
    }

    public GenericVO getCoberturaVacunas() {
        return coberturaVacunas;
    }

    public void setCoberturaVacunas(GenericVO coberturaVacunas) {
        this.coberturaVacunas = coberturaVacunas;
    }

    public GenericVO getCoberturaPrevencionEnfermedadesAdultos() {
        return coberturaPrevencionEnfermedadesAdultos;
    }

    public void setCoberturaPrevencionEnfermedadesAdultos(GenericVO coberturaPrevencionEnfermedadesAdultos) {
        this.coberturaPrevencionEnfermedadesAdultos = coberturaPrevencionEnfermedadesAdultos;
    }

    public GenericVO getMaternidad() {
        return maternidad;
    }

    public void setMaternidad(GenericVO maternidad) {
        this.maternidad = maternidad;
    }

    public GenericVO getSumaAseguradaMaternidad() {
        return sumaAseguradaMaternidad;
    }

    public void setSumaAseguradaMaternidad(GenericVO sumaAseguradaMaternidad) {
        this.sumaAseguradaMaternidad = sumaAseguradaMaternidad;
    }

    public GenericVO getBaseTabuladorReembolso() {
        return baseTabuladorReembolso;
    }

    public void setBaseTabuladorReembolso(GenericVO baseTabuladorReembolso) {
        this.baseTabuladorReembolso = baseTabuladorReembolso;
    }

    public GenericVO getCostoEmergenciaExtranjero() {
        return costoEmergenciaExtranjero;
    }

    public void setCostoEmergenciaExtranjero(GenericVO costoEmergenciaExtranjero) {
        this.costoEmergenciaExtranjero = costoEmergenciaExtranjero;
    }

    public List<IncisoSaludVO> getIncisos() {
        return incisos;
    }

    public void setIncisos(List<IncisoSaludVO> incisos) {
        this.incisos = incisos;
    }

    public GenericVO getEstado() {
        return estado;
    }

    public void setEstado(GenericVO estado) {
        this.estado = estado;
    }

    public GenericVO getCoberturaEliminacionPenalizacionCambioZona() {
        return coberturaEliminacionPenalizacionCambioZona;
    }

    public void setCoberturaEliminacionPenalizacionCambioZona(GenericVO coberturaEliminacionPenalizacionCambioZona) {
        this.coberturaEliminacionPenalizacionCambioZona = coberturaEliminacionPenalizacionCambioZona;
    }
    
}
