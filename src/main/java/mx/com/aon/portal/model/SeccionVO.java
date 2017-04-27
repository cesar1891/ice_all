package mx.com.aon.portal.model;


/**
 * 
 * Clase VO usada para obtener una seccion.
 * 
 * @param cdSeccion 
 * @param dsSeccion 
 * @param cdBloque 
 * @param dsBloque 
 * @param isBloqueEditable
 */
public class SeccionVO {
	
	private String cdSeccion;
	private String dsSeccion;
	private String cdBloque;
    private String dsBloque;
    private String isBloqueEditable;


    public String getCdBloque() {
		return cdBloque;
	}

	public void setCdBloque(String cdBloque) {
		this.cdBloque = cdBloque;
	}

	public String getCdSeccion() {
        return cdSeccion;
    }

    public void setCdSeccion(String cdSeccion) {
        this.cdSeccion = cdSeccion;
    }

    public String getDsSeccion() {
		return dsSeccion;
	}
	public void setDsSeccion(String dsSeccion) {
		this.dsSeccion = dsSeccion;
	}


    public String getDsBloque() {
        return dsBloque;
    }

    public void setDsBloque(String dsBloque) {
        this.dsBloque = dsBloque;
    }


    public String getBloqueEditable() {
        return isBloqueEditable;
    }

    public void setBloqueEditable(String bloqueEditable) {
        isBloqueEditable = bloqueEditable;
    }
}
