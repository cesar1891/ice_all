package mx.com.gseguros.confpantallas.model;

public class DinamicPanelVo implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int idPadre;
	private int orden;
	
	public DinamicPanelVo (int id, String name, int idPadre,int orden){
		this.id = id;
		this.name = name;
		this.idPadre = idPadre;
		this.orden = orden;
	}
	
	public DinamicPanelVo() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(int idPadre) {
		this.idPadre = idPadre;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

}
