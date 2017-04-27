/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal2.web.GenericVO;

/**
 *
 * @author Jair
 */
public class Cotizacion {
    
    private GenericVO ciudad;
    private Double deducible;
    private GenericVO copago;
    private GenericVO sumaAsegurada;
    private GenericVO circuloHospitalario;
    private GenericVO coberturaVacunas;
    private GenericVO coberturaPrevencionEnfermedadesAdultos;
    private GenericVO maternidad;
    private GenericVO sumaAseguradaMaternidad;
    private GenericVO baseTabuladorRembolso;
    private GenericVO costoEmergenciaExtranjero;
    private List<test.model.Inciso>incisos=new ArrayList<test.model.Inciso>(0);

    public Cotizacion(GenericVO ciudad, Double deducible, GenericVO copago, GenericVO sumaAsegurada, GenericVO circuloHospitalario, GenericVO coberturaVacunas, GenericVO coberturaPrevencionEnfermedadesAdultos, GenericVO maternidad, GenericVO sumaAseguradaMaternidad, GenericVO baseTabuladorRembolso, GenericVO costoEmergenciaExtranjero) {
        this.ciudad = ciudad;
        this.deducible = deducible;
        this.copago = copago;
        this.sumaAsegurada = sumaAsegurada;
        this.circuloHospitalario = circuloHospitalario;
        this.coberturaVacunas = coberturaVacunas;
        this.coberturaPrevencionEnfermedadesAdultos = coberturaPrevencionEnfermedadesAdultos;
        this.maternidad = maternidad;
        this.sumaAseguradaMaternidad = sumaAseguradaMaternidad;
        this.baseTabuladorRembolso = baseTabuladorRembolso;
        this.costoEmergenciaExtranjero = costoEmergenciaExtranjero;
    }

    public GenericVO getCiudad() {
        return ciudad;
    }

    public void setCiudad(GenericVO ciudad) {
        this.ciudad = ciudad;
    }

    public Double getDeducible() {
        return deducible;
    }

    public void setDeducible(Double deducible) {
        this.deducible = deducible;
    }

    public GenericVO getCopago() {
        return copago;
    }

    public void setCopago(GenericVO copago) {
        this.copago = copago;
    }

    public GenericVO getSumaAsegurada() {
        return sumaAsegurada;
    }

    public void setSumaAsegurada(GenericVO sumaAsegurada) {
        this.sumaAsegurada = sumaAsegurada;
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

    public GenericVO getBaseTabuladorRembolso() {
        return baseTabuladorRembolso;
    }

    public void setBaseTabuladorRembolso(GenericVO baseTabuladorRembolso) {
        this.baseTabuladorRembolso = baseTabuladorRembolso;
    }

    public GenericVO getCostoEmergenciaExtranjero() {
        return costoEmergenciaExtranjero;
    }

    public void setCostoEmergenciaExtranjero(GenericVO costoEmergenciaExtranjero) {
        this.costoEmergenciaExtranjero = costoEmergenciaExtranjero;
    }

    public List<Inciso> getIncisos() {
        return incisos;
    }

    public void setIncisos(List<Inciso> incisos) {
        this.incisos = incisos;
    }
    
}
