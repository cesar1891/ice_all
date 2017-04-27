package mx.com.gseguros.confpantallas.model;

public class DinamicControlVo implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idControl;
	private int idPanel;
	private String descripcion;
	private int orden;
	
	public DinamicControlVo(int idControl,int idPanel,String descripcion,int orden){
		this.idControl = idControl;
		this.idPanel = idPanel;
		this.descripcion = descripcion;
		this.orden = orden;
	}
	
	public DinamicControlVo(){
	}
	
	
	public int getIdControl() {
		return idControl;
	}
	public void setIdControl(int idControl) {
		this.idControl = idControl;
	}
	public int getIdPanel() {
		return idPanel;
	}
	public void setIdPanel(int idPanel) {
		this.idPanel = idPanel;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	

}
