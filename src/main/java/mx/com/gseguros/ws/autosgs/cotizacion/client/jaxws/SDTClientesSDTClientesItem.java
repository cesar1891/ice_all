
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para SDTClientes.SDTClientesItem complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SDTClientes.SDTClientesItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="cve_cli" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="suc_emi" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="fis_mor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nom_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ape_pat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ape_mat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="raz_soc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ane_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rfc_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cve_ele" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="curpcli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sexocli" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="edo_civ" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="cal_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="num_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cod_pos" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="colonia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="municip" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="poblaci" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cve_est" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="fec_nac" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="nac_ext" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ocu_pro" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="act_gir" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="telefo1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefo2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefo3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cor_ele" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pag_web" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="can_con" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="fue_ing" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="adm_con" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="car_pub" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nom_car" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="per_car" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="apo_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dom_ori" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="num_pas" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="usu_cap" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="usu_aut" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fec_alt" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="fec_act" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="sta_cli" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descuento" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SDTClientes.SDTClientesItem", namespace = "KB_ObtenerClientes", propOrder = {

})
public class SDTClientesSDTClientesItem {

    @XmlElement(name = "cve_cli", namespace = "KB_ObtenerClientes")
    protected int cveCli;
    @XmlElement(name = "suc_emi", namespace = "KB_ObtenerClientes")
    protected short sucEmi;
    @XmlElement(name = "fis_mor", namespace = "KB_ObtenerClientes", required = true)
    protected String fisMor;
    @XmlElement(name = "nom_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String nomCli;
    @XmlElement(name = "ape_pat", namespace = "KB_ObtenerClientes", required = true)
    protected String apePat;
    @XmlElement(name = "ape_mat", namespace = "KB_ObtenerClientes", required = true)
    protected String apeMat;
    @XmlElement(name = "raz_soc", namespace = "KB_ObtenerClientes", required = true)
    protected String razSoc;
    @XmlElement(name = "ane_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String aneCli;
    @XmlElement(name = "rfc_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String rfcCli;
    @XmlElement(name = "cve_ele", namespace = "KB_ObtenerClientes", required = true)
    protected String cveEle;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String curpcli;
    @XmlElement(namespace = "KB_ObtenerClientes")
    protected short sexocli;
    @XmlElement(name = "edo_civ", namespace = "KB_ObtenerClientes")
    protected short edoCiv;
    @XmlElement(name = "cal_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String calCli;
    @XmlElement(name = "num_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String numCli;
    @XmlElement(name = "cod_pos", namespace = "KB_ObtenerClientes")
    protected int codPos;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String colonia;
    @XmlElement(namespace = "KB_ObtenerClientes")
    protected short municip;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String poblaci;
    @XmlElement(name = "cve_est", namespace = "KB_ObtenerClientes")
    protected short cveEst;
    @XmlElement(name = "fec_nac", namespace = "KB_ObtenerClientes", required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecNac;
    @XmlElement(name = "nac_ext", namespace = "KB_ObtenerClientes", required = true)
    protected String nacExt;
    @XmlElement(name = "ocu_pro", namespace = "KB_ObtenerClientes")
    protected short ocuPro;
    @XmlElement(name = "act_gir", namespace = "KB_ObtenerClientes")
    protected short actGir;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String telefo1;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String telefo2;
    @XmlElement(namespace = "KB_ObtenerClientes", required = true)
    protected String telefo3;
    @XmlElement(name = "cor_ele", namespace = "KB_ObtenerClientes", required = true)
    protected String corEle;
    @XmlElement(name = "pag_web", namespace = "KB_ObtenerClientes", required = true)
    protected String pagWeb;
    @XmlElement(name = "can_con", namespace = "KB_ObtenerClientes")
    protected short canCon;
    @XmlElement(name = "fue_ing", namespace = "KB_ObtenerClientes", required = true)
    protected String fueIng;
    @XmlElement(name = "adm_con", namespace = "KB_ObtenerClientes", required = true)
    protected String admCon;
    @XmlElement(name = "car_pub", namespace = "KB_ObtenerClientes", required = true)
    protected String carPub;
    @XmlElement(name = "nom_car", namespace = "KB_ObtenerClientes", required = true)
    protected String nomCar;
    @XmlElement(name = "per_car", namespace = "KB_ObtenerClientes", required = true)
    protected String perCar;
    @XmlElement(name = "apo_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String apoCli;
    @XmlElement(name = "dom_ori", namespace = "KB_ObtenerClientes", required = true)
    protected String domOri;
    @XmlElement(name = "num_pas", namespace = "KB_ObtenerClientes", required = true)
    protected String numPas;
    @XmlElement(name = "usu_cap", namespace = "KB_ObtenerClientes")
    protected int usuCap;
    @XmlElement(name = "usu_aut", namespace = "KB_ObtenerClientes")
    protected int usuAut;
    @XmlElement(name = "fec_alt", namespace = "KB_ObtenerClientes", required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecAlt;
    @XmlElement(name = "fec_act", namespace = "KB_ObtenerClientes", required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecAct;
    @XmlElement(name = "sta_cli", namespace = "KB_ObtenerClientes", required = true)
    protected String staCli;
    @XmlElement(namespace = "KB_ObtenerClientes")
    protected short descuento;

    /**
     * Obtiene el valor de la propiedad cveCli.
     * 
     */
    public int getCveCli() {
        return cveCli;
    }

    /**
     * Define el valor de la propiedad cveCli.
     * 
     */
    public void setCveCli(int value) {
        this.cveCli = value;
    }

    /**
     * Obtiene el valor de la propiedad sucEmi.
     * 
     */
    public short getSucEmi() {
        return sucEmi;
    }

    /**
     * Define el valor de la propiedad sucEmi.
     * 
     */
    public void setSucEmi(short value) {
        this.sucEmi = value;
    }

    /**
     * Obtiene el valor de la propiedad fisMor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFisMor() {
        return fisMor;
    }

    /**
     * Define el valor de la propiedad fisMor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFisMor(String value) {
        this.fisMor = value;
    }

    /**
     * Obtiene el valor de la propiedad nomCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomCli() {
        return nomCli;
    }

    /**
     * Define el valor de la propiedad nomCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomCli(String value) {
        this.nomCli = value;
    }

    /**
     * Obtiene el valor de la propiedad apePat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApePat() {
        return apePat;
    }

    /**
     * Define el valor de la propiedad apePat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApePat(String value) {
        this.apePat = value;
    }

    /**
     * Obtiene el valor de la propiedad apeMat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApeMat() {
        return apeMat;
    }

    /**
     * Define el valor de la propiedad apeMat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApeMat(String value) {
        this.apeMat = value;
    }

    /**
     * Obtiene el valor de la propiedad razSoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazSoc() {
        return razSoc;
    }

    /**
     * Define el valor de la propiedad razSoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazSoc(String value) {
        this.razSoc = value;
    }

    /**
     * Obtiene el valor de la propiedad aneCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAneCli() {
        return aneCli;
    }

    /**
     * Define el valor de la propiedad aneCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAneCli(String value) {
        this.aneCli = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcCli() {
        return rfcCli;
    }

    /**
     * Define el valor de la propiedad rfcCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcCli(String value) {
        this.rfcCli = value;
    }

    /**
     * Obtiene el valor de la propiedad cveEle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCveEle() {
        return cveEle;
    }

    /**
     * Define el valor de la propiedad cveEle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCveEle(String value) {
        this.cveEle = value;
    }

    /**
     * Obtiene el valor de la propiedad curpcli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurpcli() {
        return curpcli;
    }

    /**
     * Define el valor de la propiedad curpcli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurpcli(String value) {
        this.curpcli = value;
    }

    /**
     * Obtiene el valor de la propiedad sexocli.
     * 
     */
    public short getSexocli() {
        return sexocli;
    }

    /**
     * Define el valor de la propiedad sexocli.
     * 
     */
    public void setSexocli(short value) {
        this.sexocli = value;
    }

    /**
     * Obtiene el valor de la propiedad edoCiv.
     * 
     */
    public short getEdoCiv() {
        return edoCiv;
    }

    /**
     * Define el valor de la propiedad edoCiv.
     * 
     */
    public void setEdoCiv(short value) {
        this.edoCiv = value;
    }

    /**
     * Obtiene el valor de la propiedad calCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalCli() {
        return calCli;
    }

    /**
     * Define el valor de la propiedad calCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalCli(String value) {
        this.calCli = value;
    }

    /**
     * Obtiene el valor de la propiedad numCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumCli() {
        return numCli;
    }

    /**
     * Define el valor de la propiedad numCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumCli(String value) {
        this.numCli = value;
    }

    /**
     * Obtiene el valor de la propiedad codPos.
     * 
     */
    public int getCodPos() {
        return codPos;
    }

    /**
     * Define el valor de la propiedad codPos.
     * 
     */
    public void setCodPos(int value) {
        this.codPos = value;
    }

    /**
     * Obtiene el valor de la propiedad colonia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Define el valor de la propiedad colonia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColonia(String value) {
        this.colonia = value;
    }

    /**
     * Obtiene el valor de la propiedad municip.
     * 
     */
    public short getMunicip() {
        return municip;
    }

    /**
     * Define el valor de la propiedad municip.
     * 
     */
    public void setMunicip(short value) {
        this.municip = value;
    }

    /**
     * Obtiene el valor de la propiedad poblaci.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoblaci() {
        return poblaci;
    }

    /**
     * Define el valor de la propiedad poblaci.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoblaci(String value) {
        this.poblaci = value;
    }

    /**
     * Obtiene el valor de la propiedad cveEst.
     * 
     */
    public short getCveEst() {
        return cveEst;
    }

    /**
     * Define el valor de la propiedad cveEst.
     * 
     */
    public void setCveEst(short value) {
        this.cveEst = value;
    }

    /**
     * Obtiene el valor de la propiedad fecNac.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecNac() {
        return fecNac;
    }

    /**
     * Define el valor de la propiedad fecNac.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecNac(XMLGregorianCalendar value) {
        this.fecNac = value;
    }

    /**
     * Obtiene el valor de la propiedad nacExt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacExt() {
        return nacExt;
    }

    /**
     * Define el valor de la propiedad nacExt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacExt(String value) {
        this.nacExt = value;
    }

    /**
     * Obtiene el valor de la propiedad ocuPro.
     * 
     */
    public short getOcuPro() {
        return ocuPro;
    }

    /**
     * Define el valor de la propiedad ocuPro.
     * 
     */
    public void setOcuPro(short value) {
        this.ocuPro = value;
    }

    /**
     * Obtiene el valor de la propiedad actGir.
     * 
     */
    public short getActGir() {
        return actGir;
    }

    /**
     * Define el valor de la propiedad actGir.
     * 
     */
    public void setActGir(short value) {
        this.actGir = value;
    }

    /**
     * Obtiene el valor de la propiedad telefo1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefo1() {
        return telefo1;
    }

    /**
     * Define el valor de la propiedad telefo1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefo1(String value) {
        this.telefo1 = value;
    }

    /**
     * Obtiene el valor de la propiedad telefo2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefo2() {
        return telefo2;
    }

    /**
     * Define el valor de la propiedad telefo2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefo2(String value) {
        this.telefo2 = value;
    }

    /**
     * Obtiene el valor de la propiedad telefo3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefo3() {
        return telefo3;
    }

    /**
     * Define el valor de la propiedad telefo3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefo3(String value) {
        this.telefo3 = value;
    }

    /**
     * Obtiene el valor de la propiedad corEle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorEle() {
        return corEle;
    }

    /**
     * Define el valor de la propiedad corEle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorEle(String value) {
        this.corEle = value;
    }

    /**
     * Obtiene el valor de la propiedad pagWeb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagWeb() {
        return pagWeb;
    }

    /**
     * Define el valor de la propiedad pagWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagWeb(String value) {
        this.pagWeb = value;
    }

    /**
     * Obtiene el valor de la propiedad canCon.
     * 
     */
    public short getCanCon() {
        return canCon;
    }

    /**
     * Define el valor de la propiedad canCon.
     * 
     */
    public void setCanCon(short value) {
        this.canCon = value;
    }

    /**
     * Obtiene el valor de la propiedad fueIng.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFueIng() {
        return fueIng;
    }

    /**
     * Define el valor de la propiedad fueIng.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFueIng(String value) {
        this.fueIng = value;
    }

    /**
     * Obtiene el valor de la propiedad admCon.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdmCon() {
        return admCon;
    }

    /**
     * Define el valor de la propiedad admCon.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdmCon(String value) {
        this.admCon = value;
    }

    /**
     * Obtiene el valor de la propiedad carPub.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarPub() {
        return carPub;
    }

    /**
     * Define el valor de la propiedad carPub.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarPub(String value) {
        this.carPub = value;
    }

    /**
     * Obtiene el valor de la propiedad nomCar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomCar() {
        return nomCar;
    }

    /**
     * Define el valor de la propiedad nomCar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomCar(String value) {
        this.nomCar = value;
    }

    /**
     * Obtiene el valor de la propiedad perCar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPerCar() {
        return perCar;
    }

    /**
     * Define el valor de la propiedad perCar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPerCar(String value) {
        this.perCar = value;
    }

    /**
     * Obtiene el valor de la propiedad apoCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApoCli() {
        return apoCli;
    }

    /**
     * Define el valor de la propiedad apoCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApoCli(String value) {
        this.apoCli = value;
    }

    /**
     * Obtiene el valor de la propiedad domOri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomOri() {
        return domOri;
    }

    /**
     * Define el valor de la propiedad domOri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomOri(String value) {
        this.domOri = value;
    }

    /**
     * Obtiene el valor de la propiedad numPas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPas() {
        return numPas;
    }

    /**
     * Define el valor de la propiedad numPas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPas(String value) {
        this.numPas = value;
    }

    /**
     * Obtiene el valor de la propiedad usuCap.
     * 
     */
    public int getUsuCap() {
        return usuCap;
    }

    /**
     * Define el valor de la propiedad usuCap.
     * 
     */
    public void setUsuCap(int value) {
        this.usuCap = value;
    }

    /**
     * Obtiene el valor de la propiedad usuAut.
     * 
     */
    public int getUsuAut() {
        return usuAut;
    }

    /**
     * Define el valor de la propiedad usuAut.
     * 
     */
    public void setUsuAut(int value) {
        this.usuAut = value;
    }

    /**
     * Obtiene el valor de la propiedad fecAlt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecAlt() {
        return fecAlt;
    }

    /**
     * Define el valor de la propiedad fecAlt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecAlt(XMLGregorianCalendar value) {
        this.fecAlt = value;
    }

    /**
     * Obtiene el valor de la propiedad fecAct.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecAct() {
        return fecAct;
    }

    /**
     * Define el valor de la propiedad fecAct.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecAct(XMLGregorianCalendar value) {
        this.fecAct = value;
    }

    /**
     * Obtiene el valor de la propiedad staCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaCli() {
        return staCli;
    }

    /**
     * Define el valor de la propiedad staCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaCli(String value) {
        this.staCli = value;
    }

    /**
     * Obtiene el valor de la propiedad descuento.
     * 
     */
    public short getDescuento() {
        return descuento;
    }

    /**
     * Define el valor de la propiedad descuento.
     * 
     */
    public void setDescuento(short value) {
        this.descuento = value;
    }

}
