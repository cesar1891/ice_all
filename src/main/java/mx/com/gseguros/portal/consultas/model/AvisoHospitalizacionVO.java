package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author GDH
 *
 */

public class AvisoHospitalizacionVO implements Serializable {
	
	private static final long serialVersionUID = -8555353864912795413L;
	
	private String cdperson;
	private String nmpoliza;
	private String cdagente;
	private String cdpresta;
	private String dspresta;
	private String feregistro;
	private String feingreso;
	private String cdusuari;
	private String comentario;
	
	public String getCdperson() {
		return cdperson;
	}
	
	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}
	
	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

	public String getCdagente() {
		return cdagente;
	}

	public void setCdagente(String cdagente) {
		this.cdagente = cdagente;
	}

	public String getCdpresta() {
		return cdpresta;
	}
	
	public void setCdpresta(String cdpresta) {
		this.cdpresta = cdpresta;
	}
	
	public String getDspresta() {
		return dspresta;
	}
	
	public void setDspresta(String dspresta) {
		this.dspresta = dspresta;
	}
	
	public String getFeregistro() {
		return feregistro;
	}
	
	public void setFeregistro(String feregistro) {
		this.feregistro = feregistro;
	}
	
	public String getFeingreso() {
		return feingreso;
	}
	
	public void setFeingreso(String feingreso) {
		this.feingreso = feingreso;
	}
	
	public String getCdusuari() {
		return cdusuari;
	}
	
	public void setCdusuari(String cdusuari) {
		this.cdusuari = cdusuari;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
}
