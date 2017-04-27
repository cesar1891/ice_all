package mx.com.gseguros.confpantallas.model;

public class ViewBean implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	private String nombreVar;
	private String xtype;
	private String codigo;
	
	public String getNombreVar() {
		return nombreVar;
	}
	public void setNombreVar(String nombreVar) {
		this.nombreVar = nombreVar;
	}
	public String getXtype() {
		return xtype;
	}
	public void setXtype(String xtype) {
		this.xtype = xtype;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
