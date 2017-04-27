package mx.com.gseguros.confpantallas.model;

public class DinamicPanelAttrVo implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idattr;
	private int idpanel;
	private String attr;
	private String valor;
	private String tipo;
	
	public DinamicPanelAttrVo(int idattr,int idpanel,String attr,String valor,String tipo){
		this.idattr = idattr;
		this.idpanel = idpanel;
		this.attr = attr;
		this.valor = valor;
		this.tipo = tipo;
	}
	
	public DinamicPanelAttrVo() {}

	public int getIdattr() {
		return idattr;
	}
	public void setIdattr(int idattr) {
		this.idattr = idattr;
	}
	public int getIdpanel() {
		return idpanel;
	}
	public void setIdpanel(int idpanel) {
		this.idpanel = idpanel;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
