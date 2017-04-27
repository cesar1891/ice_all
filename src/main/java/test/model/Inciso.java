/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model;

import java.util.Date;

import mx.com.aon.portal2.web.GenericVO;

/**
 *
 * @author Jair
 */
public class Inciso {
    
    private GenericVO rol;
    private Date fechaNacimiento;
    private GenericVO genero;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Inciso(GenericVO rol, Date fechaNacimiento, GenericVO genero, String nombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno) {
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.nombre = nombre;
        this.segundoNombre = segundoNombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
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

    public GenericVO getGenero() {
        return genero;
    }

    public void setGenero(GenericVO genero) {
        this.genero = genero;
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
