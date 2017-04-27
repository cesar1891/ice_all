/**
 * 
 */
package mx.com.aon.configurador.pantallas.model;

import java.io.Serializable;

/**
 * 
 *  Clase Value Object de una pantalla
 * 
 * @author  aurora.lozada
 * 
 */

public class PantallaVO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 8471114829802796633L;

    private String cdConjunto;
    
    private String cdPantalla;
    
    private String nombrePantalla;
    
    private String descripcion;
    
    private String cdMaster;
    
    private String dsArchivo;
    
    private String dsArchivoSec;    
    
    private String tipoMaster;
    
    private String tipoSituacion;
    
    private String dsCampos;

    private String dsLabels;

    private String cdRol;

    private String dsRol;

    public String getTipoMaster() {
		return tipoMaster;
	}

	public void setTipoMaster(String tipoMaster) {
		this.tipoMaster = tipoMaster;
	}

	/**
     * @return the cdConjunto
     */
    public String getCdConjunto() {
        return cdConjunto;
    }

    /**
     * @param cdConjunto the cdConjunto to set
     */
    public void setCdConjunto(String cdConjunto) {
        this.cdConjunto = cdConjunto;
    }

    /**
     * @return the cdPantalla
     */
    public String getCdPantalla() {
        return cdPantalla;
    }

    /**
     * @param cdPantalla the cdPantalla to set
     */
    public void setCdPantalla(String cdPantalla) {
        this.cdPantalla = cdPantalla;
    }

    /**
     * @return the nombrePantalla
     */
    public String getNombrePantalla() {
        return nombrePantalla;
    }

    /**
     * @param nombrePantalla the nombrePantalla to set
     */
    public void setNombrePantalla(String nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the cdMaster
     */
    public String getCdMaster() {
        return cdMaster;
    }

    /**
     * @param cdMaster the cdMaster to set
     */
    public void setCdMaster(String cdMaster) {
        this.cdMaster = cdMaster;
    }

    /**
     * @return the dsArchivo
     */
    public String getDsArchivo() {
        return dsArchivo;
    }

    /**
     * @param dsArchivo the dsArchivo to set
     */
    public void setDsArchivo(String dsArchivo) {
        this.dsArchivo = dsArchivo;
    }

    /**
     * @return the dsArchivoSec
     */
    public String getDsArchivoSec() {
        return dsArchivoSec;
    }

    /**
     * @param dsArchivoSec the dsArchivoSec to set
     */
    public void setDsArchivoSec(String dsArchivoSec) {
        this.dsArchivoSec = dsArchivoSec;
    }

	/**
	 * @return the tipoSituacion
	 */
	public String getTipoSituacion() {
		return tipoSituacion;
	}

	/**
	 * @param tipoSituacion the tipoSituacion to set
	 */
	public void setTipoSituacion(String tipoSituacion) {
		this.tipoSituacion = tipoSituacion;
	}

	public String getDsCampos() {
		return dsCampos;
	}

	public void setDsCampos(String dsCampos) {
		this.dsCampos = dsCampos;
	}

	public String getDsLabels() {
		return dsLabels;
	}

	public void setDsLabels(String dsLabels) {
		this.dsLabels = dsLabels;
	}

	public String getDsRol() {
		return dsRol;
	}

	public void setDsRol(String dsRol) {
		this.dsRol = dsRol;
	}

	public String getCdRol() {
		return cdRol;
	}

	public void setCdRol(String cdRol) {
		this.cdRol = cdRol;
	}
    
}
