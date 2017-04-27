package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author JAGC
 *
 */
public class AseguradoVO implements Serializable{

	private static final long serialVersionUID = -8555353864912795413L;

	/**
	 * Codigo de la persona
	 */
	private String cdperson;
	
	private String nmsituac;
	private String cdtipsit;
	private String nombre;
	private String cdrfc;
	private String cdrol;
	private String dsrol;
	private String sexo;
	private String fenacimi;
	private String status;
	private String parentesco;
	private String grupo;
	private String cdgrupo;
	private String familia;
	private String cdfamilia;

	private String cdplan;
	private String dsplan;
	
	private long total;
	
	//Perfil Medico
	private String cantIcd;
	private String maxPerfil;
	private String numPerfil;
	private String perfilFinal;
	private List<Map<String,String>> icds;
	//Perfil Medico
	
	//TODO: Agregar atributos para el Afiliado en SISA o crear un AfiliadoVO con ellos
	/*
	edad
	// Se usa para SISA para PREVEX y/o numasegurado empleado
	identidad 253785-00
	tipo de sangre
	antecedentes
	*/
	

		
	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getCdgrupo() {
		return cdgrupo;
	}

	public void setCdgrupo(String cdgrupo) {
		this.cdgrupo = cdgrupo;
	}

	public String getCdfamilia() {
		return cdfamilia;
	}

	public void setCdfamilia(String cdfamilia) {
		this.cdfamilia = cdfamilia;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getFenacimi() {
		return fenacimi;
	}

	public void setFenacimi(String fenacimi) {
		this.fenacimi = fenacimi;
	}

	public String getCdperson() {
		return cdperson;
	}

	public void setCdperson(String cdperson) {
		this.cdperson = cdperson;
	}

	public String getNmsituac() {
		return nmsituac;
	}

	public void setNmsituac(String nmsituac) {
		this.nmsituac = nmsituac;
	}

	public String getCdtipsit() {
		return cdtipsit;
	}

	public void setCdtipsit(String cdtipsit) {
		this.cdtipsit = cdtipsit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCdrfc() {
		return cdrfc;
	}

	public void setCdrfc(String cdrfc) {
		this.cdrfc = cdrfc;
	}

	public String getCdrol() {
		return cdrol;
	}

	public void setCdrol(String cdrol) {
		this.cdrol = cdrol;
	}

	public String getDsrol() {
		return dsrol;
	}


	public void setDsrol(String dsrol) {
		this.dsrol = dsrol;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}


	public String getCdplan() {
		return cdplan;
	}

	public void setCdplan(String cdplan) {
		this.cdplan = cdplan;
	}

	public String getDsplan() {
		return dsplan;
	}

	public void setDsplan(String dsplan) {
		this.dsplan = dsplan;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
	
	//Perfil Medico
	public String getPerfilFinal() {
		return perfilFinal;
	}
	public void setPerfilFinal(String perfilFinal) {
		this.perfilFinal = perfilFinal;
	}
	public List<Map<String, String>> getIcds() {
		return icds;
	}
	public void setIcds(List<Map<String, String>> icds) {
		this.icds = icds;
	}
	public String getCantIcd() {
		return cantIcd;
	}
	public void setCantIcd(String cantIcd) {
		this.cantIcd = cantIcd;
	}
	public String getMaxPerfil() {
		return maxPerfil;
	}
	public void setMaxPerfil(String maxPerfil) {
		this.maxPerfil = maxPerfil;
	}
	public String getNumPerfil() {
		return numPerfil;
	}
	public void setNumPerfil(String numPerfil) {
		this.numPerfil = numPerfil;
	}
	//Perfil Medico
		
}