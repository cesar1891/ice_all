package mx.com.gseguros.portal.siniestros.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Alberto
 *
 */
public class ConsultaManteniVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;
	private String cdtabla;
	private String codigo;
	private String descripc;
	private String descripl;
	
	
	
	public String getCdtabla() {
		return cdtabla;
	}



	public void setCdtabla(String cdtabla) {
		this.cdtabla = cdtabla;
	}



	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getDescripc() {
		return descripc;
	}



	public void setDescripc(String descripc) {
		this.descripc = descripc;
	}



	public String getDescripl() {
		return descripl;
	}



	public void setDescripl(String descripl) {
		this.descripl = descripl;
	}



	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
}
