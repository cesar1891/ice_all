/**
 * 
 */
package mx.com.aon.portal.model;

/**
 * @author CIMA_USR
 *
 */
public abstract class AbstractEntidadVO {
	 private String nombre;
	 private String idEntidad;
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }
}
