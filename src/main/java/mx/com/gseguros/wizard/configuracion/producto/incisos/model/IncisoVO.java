/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.incisos.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * Clase que contiene atributos de la pantalla incisos, getters y setters 
 */
public class IncisoVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String cdramo;
    private String dstipsit;
    private String cdtipsit;
    private String swsitdec;
    private String success;
    private String failure;
    private String swinsert;
    private String swobliga;
    private String nmsituac;
    private String cdagrupa;
    private String mode;
    private String ttiposit;
    
    private String dsriesgo;

    public String getTtiposit() {
		return ttiposit;
	}

	public void setTtiposit(String ttiposit) {
		this.ttiposit = ttiposit;
	}

	public String toString() {
        return new ToStringBuilder(this).append("cdramo", cdramo).append("dstipsit", dstipsit).append("cdtipsit", cdtipsit)
                .append("swsitdec", swsitdec).append("success", success).append("failure", failure).append("swinsert", swinsert)
                .append("swobliga", swobliga).append("nmsituac", nmsituac).append("cdagrupa", cdagrupa).append("mode", mode)
                .toString();
    }

    /**
     * @return the cdramo
     */
    public String getCdramo() {
        return cdramo;
    }

    /**
     * @param cdramo the cdramo to set
     */
    public void setCdramo(String cdramo) {
        this.cdramo = cdramo;
    }

    /**
     * @return the cdtipsit
     */
    public String getCdtipsit() {
        return cdtipsit;
    }

    /**
     * @param cdtipsit the cdtipsit to set
     */
    public void setCdtipsit(String cdtipsit) {
        this.cdtipsit = cdtipsit.toUpperCase();
    }

    /**
     * @return the dstipsit
     */
    public String getDstipsit() {
        return dstipsit;
    }

    /**
     * @param dstipsit the dstipsit to set
     */
    public void setDstipsit(String dstipsit) {
        this.dstipsit = dstipsit.toUpperCase();
    }

    /**
     * @return the failure
     */
    public String getFailure() {
        return failure;
    }

    /**
     * @param failure the failure to set
     */
    public void setFailure(String failure) {
        this.failure = failure;
    }

    /**
     * @return the success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     * @return the swsitdec
     */
    public String getSwsitdec() {
        return swsitdec;
    }

    /**
     * @param swsitdec the swsitdec to set
     */
    public void setSwsitdec(String swsitdec) {
        this.swsitdec = swsitdec;
    }

    /**
     * @return the nmsituac
     */
    public String getNmsituac() {
        return nmsituac;
    }

    /**
     * @param nmsituac the nmsituac to set
     */
    public void setNmsituac(String nmsituac) {
        this.nmsituac = nmsituac;
    }

    /**
     * @return the swinsert
     */
    public String getSwinsert() {
        return swinsert;
    }

    /**
     * @param swinsert the swinsert to set
     */
    public void setSwinsert(String swinsert) {
        this.swinsert = swinsert;
    }

    /**
     * @return the swobliga
     */
    public String getSwobliga() {
        return swobliga;
    }

    /**
     * @param swobliga the swobliga to set
     */
    public void setSwobliga(String swobliga) {
        this.swobliga = swobliga;
    }

    /**
     * @return the cdagrupa
     */
    public String getCdagrupa() {
        return cdagrupa;
    }

    /**
     * @param cdagrupa the cdagrupa to set
     */
    public void setCdagrupa(String cdagrupa) {
        this.cdagrupa = cdagrupa;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

	public String getDsriesgo() {
		return dsriesgo;
	}

	public void setDsriesgo(String dsriesgo) {
		this.dsriesgo = dsriesgo;
	}

}
