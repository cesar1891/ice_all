/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.gseguros.portal.cotizacion.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Jair
 */
public class DatosUsuario {
    
    private String cdunieco;
    private String cdramo;
    private String cdtipsit;
    private String cdusuari;
    private String cdperson;
    private String cdagente;
    private String nombre;
    private String nmcuadro;
    
    @Override
    public String toString()
    {
    	return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
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

    public String getCdtipsit() {
        return cdtipsit;
    }

    public void setCdtipsit(String cdtipsit) {
        this.cdtipsit = cdtipsit;
    }

    public String getCdusuari() {
        return cdusuari;
    }

    public void setCdusuari(String cdusuari) {
        this.cdusuari = cdusuari;
    }

    public String getCdperson() {
        return cdperson;
    }

    public void setCdperson(String cdperson) {
        this.cdperson = cdperson;
    }

    public String getCdagente() {
        return cdagente;
    }

    public void setCdagente(String cdagente) {
        this.cdagente = cdagente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNmcuadro() {
        return nmcuadro;
    }

    public void setNmcuadro(String nmcuadro) {
        this.nmcuadro = nmcuadro;
    }
    
}