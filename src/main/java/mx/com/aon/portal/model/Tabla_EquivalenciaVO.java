package mx.com.aon.portal.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Clase VO representa la estructura de datos a usar en Tabla de Equivalencia.
 * 
 */
public class Tabla_EquivalenciaVO {

	private String country_code;
	private String country_name;
	private String cdsistema;
	private String otclave01acw;
	private String otclave01ext;
	
	private String nmtabla;
	private String nmcolumna;
	private String otclave02acw;
	private String otclave02ext;
	private String otclave03acw;
	private String otclave03ext;
	
	private String otclave04acw;
	private String otclave04ext;
	private String otclave05acw;
	private String otclave05ext;
	private String cdtablaacw;
	private String numCode;
	private String regionId;
	private String otclave01;
	private String otclave02;
	private String otclave03;
	private String otclave04;
	private String otclave05;
	private String otvalor;
	private String cdTablaExt;
	private String nmcolumnatcataext;
	private String otvalorExt;
	
	/**
     * @return String
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
	
	public String getNmcolumnatcataext() {
		return nmcolumnatcataext;
	}
	public void setNmcolumnatcataext(String nmcolumnatcataext) {
		this.nmcolumnatcataext = nmcolumnatcataext;
	}
	public String getOtvalor() {
		return otvalor;
	}
	public void setOtvalor(String otvalor) {
		this.otvalor = otvalor;
	}
	public String getOtclave01() {
		return otclave01;
	}
	public void setOtclave01(String otclave01) {
		this.otclave01 = otclave01;
	}
	public String getOtclave02() {
		return otclave02;
	}
	public void setOtclave02(String otclave02) {
		this.otclave02 = otclave02;
	}
	public String getOtclave03() {
		return otclave03;
	}
	public void setOtclave03(String otclave03) {
		this.otclave03 = otclave03;
	}
	public String getOtclave04() {
		return otclave04;
	}
	public void setOtclave04(String otclave04) {
		this.otclave04 = otclave04;
	}
	public String getOtclave05() {
		return otclave05;
	}
	public void setOtclave05(String otclave05) {
		this.otclave05 = otclave05;
	}
	public String getNumCode() {
		return numCode;
	}
	public void setNumCode(String numCode) {
		this.numCode = numCode;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCdsistema() {
		return cdsistema;
	}
	public void setCdsistema(String cdsistema) {
		this.cdsistema = cdsistema;
	}
	public String getOtclave01acw() {
		return otclave01acw;
	}
	public void setOtclave01acw(String otclave01acw) {
		this.otclave01acw = otclave01acw;
	}
	public String getOtclave01ext() {
		return otclave01ext;
	}
	public void setOtclave01ext(String otclave01ext) {
		this.otclave01ext = otclave01ext;
	}
	public String getNmtabla() {
		return nmtabla;
	}
	public void setNmtabla(String nmtabla) {
		this.nmtabla = nmtabla;
	}
	public String getOtclave02acw() {
		return otclave02acw;
	}
	public void setOtclave02acw(String otclave02acw) {
		this.otclave02acw = otclave02acw;
	}
	public String getOtclave02ext() {
		return otclave02ext;
	}
	public void setOtclave02ext(String otclave02ext) {
		this.otclave02ext = otclave02ext;
	}
	public String getOtclave03acw() {
		return otclave03acw;
	}
	public void setOtclave03acw(String otclave03acw) {
		this.otclave03acw = otclave03acw;
	}
	public String getOtclave03ext() {
		return otclave03ext;
	}
	public void setOtclave03ext(String otclave03ext) {
		this.otclave03ext = otclave03ext;
	}
	public String getOtclave04acw() {
		return otclave04acw;
	}
	public void setOtclave04acw(String otclave04acw) {
		this.otclave04acw = otclave04acw;
	}
	public String getOtclave04ext() {
		return otclave04ext;
	}
	public void setOtclave04ext(String otclave04ext) {
		this.otclave04ext = otclave04ext;
	}
	public String getOtclave05acw() {
		return otclave05acw;
	}
	public void setOtclave05acw(String otclave05acw) {
		this.otclave05acw = otclave05acw;
	}
	public String getOtclave05ext() {
		return otclave05ext;
	}
	public void setOtclave05ext(String otclave05ext) {
		this.otclave05ext = otclave05ext;
	}
	public String getCdtablaacw() {
		return cdtablaacw;
	}
	public void setCdtablaacw(String cdtablaacw) {
		this.cdtablaacw = cdtablaacw;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getCdTablaExt() {
		return cdTablaExt;
	}
	public void setCdTablaExt(String cdTablaExt) {
		this.cdTablaExt = cdTablaExt;
	}
	public String getNmcolumna() {
		return nmcolumna;
	}
	public void setNmcolumna(String nmcolumna) {
		this.nmcolumna = nmcolumna;
	}


	public String getOtvalorExt() {
		return otvalorExt;
	}


	public void setOtvalorExt(String otvalorExt) {
		this.otvalorExt = otvalorExt;
	}
	
}
