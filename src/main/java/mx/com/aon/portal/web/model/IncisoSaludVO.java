/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.aon.portal.web.model;

import java.util.Date;

import mx.com.aon.portal2.web.GenericVO;

/**
 *
 * @author Jair
 */
public class IncisoSaludVO {
    
    private Long id;
    private GenericVO rol;
    private Date fechaNacimiento;
    private GenericVO sexo;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenericVO getRol() {
        return rol;
    }

    public void setRol(GenericVO rol) {
        this.rol = rol;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public GenericVO getSexo() {
        return sexo;
    }

    public void setSexo(GenericVO sexo) {
        this.sexo = sexo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
}
