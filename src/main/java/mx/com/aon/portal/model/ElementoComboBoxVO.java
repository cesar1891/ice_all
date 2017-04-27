package mx.com.aon.portal.model;

/**
 * Clase VO que modela una estructura de datos reusable 
 * para setear valores que contengan un codigo y una descripcion.
 * 
 * @vars codigo,descripcion
 *
 */
public class ElementoComboBoxVO {
    private String codigo;
    private String codigoAux;
    private String descripcion;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

	public String getCodigoAux() {
		return codigoAux;
	}

	public void setCodigoAux(String codigoAux) {
		this.codigoAux = codigoAux;
	}
}
