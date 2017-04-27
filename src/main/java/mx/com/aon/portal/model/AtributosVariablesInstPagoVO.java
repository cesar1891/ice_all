package mx.com.aon.portal.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AtributosVariablesInstPagoVO implements Serializable{
	
	private static final long serialVersionUID = 7588500634327948238L;
	
	private String cdInsCte;
	private String cdAtribu;
	private String dsAtribu;
	private String swformat;
	private String dsFormat;
	private String nmMin;
	private String nmMax;
	private String cdTabla;
	private String dsTabla;
	private String nmTabla;
	private String swemisi;
	private String swemiobl;
	private String swemiupd;
	private String swendoso;
	private String swendobl;
	private String swendupd;
	private String cdatribu_padre;
	private String nmorden;
	private String nmagrupa;
	private String cdexpress;
	private String cdcondicvis;
	private String swdatcom;
	private String swlegend;
	private String dslegend;
	private String swcomobl;
	private String swcomupd;
	private ArrayList <String> children;
	private ArrayList <String> parent;
	
	
	
	
	
	
	public String getCdTabla() {
		return cdTabla;
	}
	public void setCdTabla(String cdTabla) {
		this.cdTabla = cdTabla;
	}
	public String getDsTabla() {
		return dsTabla;
	}
	public void setDsTabla(String dsTabla) {
		this.dsTabla = dsTabla;
	}
	public String getNmTabla() {
		return nmTabla;
	}
	public void setNmTabla(String nmTabla) {
		this.nmTabla = nmTabla;
	}
	public String getCdInsCte() {
		return cdInsCte;
	}
	public void setCdInsCte(String cdInsCte) {
		this.cdInsCte = cdInsCte;
	}
	public String getCdAtribu() {
		return cdAtribu;
	}
	public void setCdAtribu(String cdAtribu) {
		this.cdAtribu = cdAtribu;
	}
	public String getDsAtribu() {
		return dsAtribu;
	}
	public void setDsAtribu(String dsAtribu) {
		this.dsAtribu = dsAtribu;
	}
	public String getSwformat() {
		return swformat;
	}
	public void setSwformat(String swformat) {
		this.swformat = swformat;
	}
	public String getDsFormat() {
		return dsFormat;
	}
	public void setDsFormat(String dsFormat) {
		this.dsFormat = dsFormat;
	}
	public String getNmMin() {
		return nmMin;
	}
	public void setNmMin(String nmMin) {
		this.nmMin = nmMin;
	}
	public String getNmMax() {
		return nmMax;
	}
	public void setNmMax(String nmMax) {
		this.nmMax = nmMax;
	}
	public String getSwemisi() {
		return swemisi;
	}
	public void setSwemisi(String swemisi) {
		this.swemisi = swemisi;
	}
	public String getSwemiobl() {
		return swemiobl;
	}
	public void setSwemiobl(String swemiobl) {
		this.swemiobl = swemiobl;
	}
	public String getSwemiupd() {
		return swemiupd;
	}
	public void setSwemiupd(String swemiupd) {
		this.swemiupd = swemiupd;
	}
	public String getSwendoso() {
		return swendoso;
	}
	public void setSwendoso(String swendoso) {
		this.swendoso = swendoso;
	}
	public String getSwendobl() {
		return swendobl;
	}
	public void setSwendobl(String swendobl) {
		this.swendobl = swendobl;
	}
	public String getSwendupd() {
		return swendupd;
	}
	public void setSwendupd(String swendupd) {
		this.swendupd = swendupd;
	}
	public String getCdatribu_padre() {
		return cdatribu_padre;
	}
	public void setCdatribu_padre(String cdatribu_padre) {
		this.cdatribu_padre = cdatribu_padre;
	}
	public String getNmorden() {
		return nmorden;
	}
	public void setNmorden(String nmorden) {
		this.nmorden = nmorden;
	}
	public String getNmagrupa() {
		return nmagrupa;
	}
	public void setNmagrupa(String nmagrupa) {
		this.nmagrupa = nmagrupa;
	}
	public String getCdexpress() {
		return cdexpress;
	}
	public void setCdexpress(String cdexpress) {
		this.cdexpress = cdexpress;
	}
	public String getCdcondicvis() {
		return cdcondicvis;
	}
	public void setCdcondicvis(String cdcondicvis) {
		this.cdcondicvis = cdcondicvis;
	}
	public String getSwdatcom() {
		return swdatcom;
	}
	public void setSwdatcom(String swdatcom) {
		this.swdatcom = swdatcom;
	}
	public String getSwlegend() {
		return swlegend;
	}
	public void setSwlegend(String swlegend) {
		this.swlegend = swlegend;
	}
	public String getSwcomupd() {
		return swcomupd;
	}
	public void setSwcomupd(String swcomupd) {
		this.swcomupd = swcomupd;
	}
	public String getDslegend() {
		return dslegend;
	}
	public void setDslegend(String dslegend) {
		this.dslegend = dslegend;
	}
	public String getSwcomobl() {
		return swcomobl;
	}
	public void setSwcomobl(String swcomobl) {
		this.swcomobl = swcomobl;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	public ArrayList<String> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}
	public ArrayList<String> getParent() {
		return parent;
	}
	public void setParent(ArrayList<String> parent) {
		this.parent = parent;
	}	
}
