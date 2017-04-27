package mx.com.gseguros.wizard.configuracion.producto.atributosVariables.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 *Clase que contiene atributos de la pantalla Atributos Variables, getters y setters 
 */
public class AtributosVariablesVO implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private String numeroTabla;
    private String modo;   
    private String cdRamo;    
    private String claveCampo;    
    private String descripcion;     
    private String minimo; 
    private String maximo;      
    private String cdFormato;      
    private String dsFormato;       
    private String obligatorio;     
    private String emision;      
    private String endosos;    
    private String retarificacion;   
    private String cotizador;   
    private String valorDefecto;    
    private String cdTabla;
    private String dsTabla;
    
    private String codigoSituacion;
    private String codigoGarantia;
    
    private String codigoPadre;
    private String agrupador;
    private String orden;
    private String codigoCondicion;
    private String dsAtributoPadre;
    private String dsCondicion;
    
    private String inserta;
    private String codigoExpresion;
    private String codigoAtributo;
   
    private String datoComplementario;
    private String obligatorioComplementario;
    private String modificableComplementario;
    private String apareceEndoso;
    private String obligatorioEndoso;
    
    private String atributoParaTodos;
    
    /**
	 * @return the inserta
	 */
	public String getInserta() {
		return inserta;
	}

	/**
	 * @param inserta the inserta to set
	 */
	public void setInserta(String inserta) {
		this.inserta = inserta;
	}

	/**
	 * @return the codigoExpresion
	 */
	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	/**
	 * @param codigoExpresion the codigoExpresion to set
	 */
	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	/**
     * @return the cdFormato
     */
    public String getCdFormato() {
        return cdFormato;
    }

    /**
     * @param cdFormato the cdFormato to set
     */
    public void setCdFormato(String cdFormato) {
        this.cdFormato = cdFormato;
    }

    /**
     * @return the claveCampo
     */
    public String getClaveCampo() {
        return claveCampo;
    }

    /**
     * @param claveCampo the claveCampo to set
     */
    public void setClaveCampo(String claveCampo) {
        this.claveCampo = claveCampo;
    }

    /**
     * @return the cotizador
     */
    public String getCotizador() {
        return cotizador;
    }

    /**
     * @param cotizador the cotizador to set
     */
    public void setCotizador(String cotizador) {
        this.cotizador = cotizador;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        if (StringUtils.isNotBlank(descripcion)) {
            this.descripcion = descripcion.toUpperCase();
        } else {
            this.descripcion = "";
        }
    }

    /**
     * @return the dsFormato
     */
    public String getDsFormato() {
        return dsFormato;
    }

    /**
     * @param dsFormato the dsFormato to set
     */
    public void setDsFormato(String dsFormato) {
        this.dsFormato = dsFormato;
    }

    /**
     * @return the emision
     */
    public String getEmision() {
        return emision;
    }

    /**
     * @param emision the emision to set
     */
    public void setEmision(String emision) {
        this.emision = emision;
    }

    /**
     * @return the endosos
     */
    public String getEndosos() {
        return endosos;
    }

    /**
     * @param endosos the endosos to set
     */
    public void setEndosos(String endosos) {
        this.endosos = endosos;
    }

    /**
     * @return the maximo
     */
    public String getMaximo() {
        return maximo;
    }

    /**
     * @param maximo the maximo to set
     */
    public void setMaximo(String maximo) {
        this.maximo = maximo;
    }

    /**
     * @return the minimo
     */
    public String getMinimo() {
        return minimo;
    }

    /**
     * @param minimo the minimo to set
     */
    public void setMinimo(String minimo) {
        this.minimo = minimo;
    }

    /**
     * @return the obligatorio
     */
    public String getObligatorio() {
        return obligatorio;
    }

    /**
     * @param obligatorio the obligatorio to set
     */
    public void setObligatorio(String obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * @return the retarificacion
     */
    public String getRetarificacion() {
        return retarificacion;
    }

    /**
     * @param retarificacion the retarificacion to set
     */
    public void setRetarificacion(String retarificacion) {
        this.retarificacion = retarificacion;
    }

    /**
     * @return the cdRamo
     */
    public String getCdRamo() {
        return cdRamo;
    }

    /**
     * @param cdRamo the cdRamo to set
     */
    public void setCdRamo(String cdRamo) {
        this.cdRamo = cdRamo;
    }

    /**
     * @return the modo
     */
    public String getModo() {
        return modo;
    }

    /**
     * @param modo the modo to set
     */
    public void setModo(String modo) {
        this.modo = modo;
    }

    /**
     * @return the valorDefecto
     */
    public String getValorDefecto() {
        return valorDefecto;
    }

    /**
     * @param valorDefecto the valorDefecto to set
     */
    public void setValorDefecto(String valorDefecto) {
        this.valorDefecto = valorDefecto;
    }

    /**
     * @return the cdTabla
     */
    public String getCdTabla() {
        return cdTabla;
    }

    /**
     * @param cdTabla the cdTabla to set
     */
    public void setCdTabla(String cdTabla) {
        this.cdTabla = cdTabla;
    }

	public String getDsTabla() {
		return dsTabla;
	}

	public void setDsTabla(String dsTabla) {
		this.dsTabla = dsTabla;
	}

	/**
	 * @return the codigoSituacion
	 */
	public String getCodigoSituacion() {
		return codigoSituacion;
	}

	/**
	 * @param codigoSituacion the codigoSituacion to set
	 */
	public void setCodigoSituacion(String codigoSituacion) {
		this.codigoSituacion = codigoSituacion;
	}

	/**
	 * @return the codigoGarantia
	 */
	public String getCodigoGarantia() {
		return codigoGarantia;
	}

	/**
	 * @param codigoGarantia the codigoGarantia to set
	 */
	public void setCodigoGarantia(String codigoGarantia) {
		this.codigoGarantia = codigoGarantia;
	}
	/**
	 * 
	 * @return
	 */
	public String getCodigoAtributo() {
		return codigoAtributo;
	}
	/**
	 * 
	 * @param codigoAtributo
	 */
	public void setCodigoAtributo(String codigoAtributo) {
		this.codigoAtributo = codigoAtributo;
	}
	/**
	 * 
	 * @return
	 */
	public String getCodigoPadre() {
		return codigoPadre;
	}
	/**
	 * 
	 * @param codigoPadre
	 */
	public void setCodigoPadre(String codigoPadre) {
		this.codigoPadre = codigoPadre;
	}
	/**
	 * 
	 * @return
	 */
	public String getAgrupador() {
		return agrupador;
	}
	/**
	 * 
	 * @param agrupador
	 */
	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}
	/**
	 * 
	 * @return
	 */
	public String getOrden() {
		return orden;
	}
	/**
	 * 
	 * @param orden
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}
	/**
	 * 
	 * @return
	 */
	public String getCodigoCondicion() {
		return codigoCondicion;
	}
	/**
	 * 
	 * @param codigoCondicion
	 */
	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}
	/**
	 * 
	 * @return
	 */
	public String getDsAtributoPadre() {
		return dsAtributoPadre;
	}
	/**
	 * 
	 * @param dsAtributoPadre
	 */
	public void setDsAtributoPadre(String dsAtributoPadre) {
		this.dsAtributoPadre = dsAtributoPadre;
	}
	/**
	 * 
	 * @return
	 */
	public String getDsCondicion() {
		return dsCondicion;
	}
	/**
	 * 
	 * @param dsCondicion
	 */
	public void setDsCondicion(String dsCondicion) {
		this.dsCondicion = dsCondicion;
	}

	public String getDatoComplementario() {
		return datoComplementario;
	}

	public void setDatoComplementario(String datoComplementario) {
		this.datoComplementario = datoComplementario;
	}

	public String getObligatorioComplementario() {
		return obligatorioComplementario;
	}

	public void setObligatorioComplementario(String obligatorioComplementario) {
		this.obligatorioComplementario = obligatorioComplementario;
	}

	public String getModificableComplementario() {
		return modificableComplementario;
	}

	public void setModificableComplementario(String modificableComplementario) {
		this.modificableComplementario = modificableComplementario;
	}

	public String getApareceEndoso() {
		return apareceEndoso;
	}

	public void setApareceEndoso(String apareceEndoso) {
		this.apareceEndoso = apareceEndoso;
	}

	public String getObligatorioEndoso() {
		return obligatorioEndoso;
	}

	public void setObligatorioEndoso(String obligatorioEndoso) {
		this.obligatorioEndoso = obligatorioEndoso;
	}

	public String getNumeroTabla() {
		return numeroTabla;
	}

	public void setNumeroTabla(String numeroTabla) {
		this.numeroTabla = numeroTabla;
	}

	public String getAtributoParaTodos() {
		return atributoParaTodos;
	}

	public void setAtributoParaTodos(String atributoParaTodos) {
		this.atributoParaTodos = atributoParaTodos;
	}
	
	
}
