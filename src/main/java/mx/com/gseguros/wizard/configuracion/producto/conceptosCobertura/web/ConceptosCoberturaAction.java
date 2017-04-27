package mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.web;

import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model.ConceptosCoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ConceptosCoberturaManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que contiene los metodos de cargar, agregar y editar los conceptos por
 * cobertura.
 * 
 */
public class ConceptosCoberturaAction extends Padre {

	private static final long serialVersionUID = -6800804615826237426L;
	private static final transient Log log = LogFactory
			.getLog(ConceptosCoberturaAction.class);

	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private ConceptosCoberturaManager conceptosCoberturaManager;
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;

	/**
	 * Atributo agregado por struts que contiene el tipo de lista que se cargara
	 * para el concepto por cobertura.
	 */
	private String lista;

	/**
	 * Atributo agregado por struts que contiene el codigo de ramo de concepto
	 * por cobertura.
	 */
	private String codigoRamo;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de conceptos
	 * por cobertura List<ReglaValidacionVO> con los valores de la consulta.
	 */
	private List<ConceptosCoberturaVO> listaConceptosCobertura;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de periodos
	 * para conceptos por cobertura List<LlaveValorVO> con los valores de la
	 * consulta.
	 */
	private List<LlaveValorVO> listaPeriodos;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de coberturas
	 * para conceptos por cobertura List<LlaveValorVO> con los valores de la
	 * consulta.
	 */
	private List<LlaveValorVO> listaCoberturas;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de conceptos
	 * para conceptos por cobertura List<LlaveValorVO> con los valores de la
	 * consulta.
	 */
	private List<LlaveValorVO> listaConceptos;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de conceptos
	 * para conceptos por cobertura List<LlaveValorVO> con los valores de la
	 * consulta.
	 */
	private List<LlaveValorVO> listaComportamientos;

	/**
	 * Atributo de respuesta interpretado por strust con la lista de conceptos
	 * para conceptos por cobertura List<LlaveValorVO> con los valores de la
	 * consulta.
	 */
	private List<LlaveValorVO> listaCondicion;

	/**
	 * Atributo agregado por struts que contiene el codigo de periodo de
	 * concepto por cobertura.
	 */
	private String codigoPeriodo;

	/**
	 * Atributo agregado por struts que contiene el codigo de cobertura de
	 * concepto por cobertura.
	 */
	private String codigoCobertura;

	/**
	 * Atributo agregado por struts que contiene el codigo de concepto de
	 * concepto por cobertura.
	 */
	private String codigoConcepto;

	/**
	 * Atributo agregado por struts que contiene el codigo de ramo de concepto
	 * por cobertura.
	 */
	private String codigoComportamiento;

	/**
	 * Atributo agregado por struts que contiene el codigo de ramo de concepto
	 * por cobertura.
	 */
	private String codigoCondicion;

	/**
	 * Atributo agregado por struts que contiene el orden de concepto por
	 * cobertura.
	 */
	private String orden;

	/**
	 * Atributo agregado por struts que contiene el orden de concepto por
	 * cobertura.
	 */
	private String ordenHidden;
    
    private String msgRespuesta;

	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici&oacute;n web.
	 * 
	 * @return INPUT
	 * @throws Exception
	 */
	public String execute() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entro a conceptos por cobertura");
		}
		return INPUT;
	}

	/**
	 * Metodo <code>cargaListasConceptosPorCobertura</code> con el que son
	 * cargadas las listas de la pantalla conceptos por cobertura.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String cargaListasConceptosPorCobertura() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a listas de conceptos por cobertura");
		}
		String codigoSituacion = "";
		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
		}
		if (session.containsKey("CODIGO_NIVEL2")) {
			codigoSituacion = (String) session.get("CODIGO_NIVEL2");

		}
		if (session.containsKey("CODIGO_NIVEL6")) {
			codigoCobertura = (String) session.get("CODIGO_NIVEL6");
			
		}
		if (isDebugEnabled) {
			log.debug("CODIGORAMO-->>" + codigoRamo);
		}
		log.debug("NIVEL6: "  + session.get("CODIGO_NIVEL6"));
		if (lista.equals("conceptosPorCobertura") && codigoRamo != null) {
			// codigoRamo="100";//prueba

			if (codigoCobertura != null)
				listaConceptosCobertura = conceptosCoberturaManager
						.obtieneConceptosPorCobertura(codigoRamo,
								codigoSituacion, codigoCobertura);
			else
				listaConceptosCobertura = conceptosCoberturaManager
						.obtieneConceptosPorCobertura(codigoRamo);

		}

		if (lista.equals("periodos") && codigoRamo != null) {
			listaPeriodos = conceptosCoberturaManager
					.obtieneListaPeriodos(codigoRamo);
		}

		if (lista.equals("coberturas") && codigoRamo != null) {
			// codigoRamo="100";//prueba
			listaCoberturas = conceptosCoberturaManager
					.obtieneListaCoberturas(codigoRamo);
		}
		if (lista.equals("conceptos")) {
			
			if (StringUtils.isBlank(((String) session.get("CODIGO_NIVEL6"))))
				listaConceptos = conceptosCoberturaManager.obtieneListaConceptos("G");
			else
				listaConceptos = conceptosCoberturaManager.obtieneListaConceptos(null);
		}
		if (lista.equals("comportamiento")) {
			listaComportamientos = conceptosCoberturaManager
					.obtieneListaComportamientos();
		}
		if (lista.equals("condiciones")) {
			listaCondicion = conceptosCoberturaManager
					.obtieneListaCondiciones();
		}
		success = true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>agregarConceptosCobertura</code> con el que es agregado o
	 * editado un concepto por cobertura.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String agregarConceptosCobertura() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("-> Entro a agregar concepto por cobertura");
            log.debug(":: codigoCobertura : " + codigoCobertura);
		}

        if (StringUtils.isBlank(codigoCobertura)) {
            msgRespuesta = "Concepto global no asociado";    
        } else {
            msgRespuesta = "Concepto por cobertura no asociado";
        }
        		
        if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
			if (isDebugEnabled) {
				log.debug("CODIGORAMO-->" + codigoRamo);
			}
		}

		if (codigoRamo != null) {

			ConceptosCoberturaVO conceptoCobertura = new ConceptosCoberturaVO();

			conceptoCobertura.setCodigoPeriodo(codigoPeriodo);
			conceptoCobertura.setOrden(orden);
			conceptoCobertura.setCodigoCobertura((String)session.get("CODIGO_NIVEL6")== null ? "" : (String)session.get("CODIGO_NIVEL6"));
			conceptoCobertura.setCodigoConcepto(codigoConcepto);
			conceptoCobertura.setCodigoComportamiento(codigoComportamiento);
			if (codigoComportamiento.equals("C"))
				conceptoCobertura.setCodigoCondicion(codigoCondicion);
			else
				conceptoCobertura.setCodigoCondicion("");

			if (orden == null) {
				conceptoCobertura.setOrden(ordenHidden);
			}
			conceptosCoberturaManager.agregarConceptoPorCobertura(
					conceptoCobertura, codigoRamo);

		}

		success = true;
        
        if (StringUtils.isBlank(codigoCobertura)) {
            msgRespuesta = "Concepto global asociado";    
        } else {
            msgRespuesta = "Concepto por cobertura asociado";
        }
        
        if (isDebugEnabled) {
            log.debug(":: msgRespuesta : " + msgRespuesta);
        }
		
        return SUCCESS;
	}

	/**
	 * Metodo <code>eliminarReglaValidacion</code> con el que es eliminada la
	 * regla de validacion de un producto.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String eliminarConceptosPorCobertura() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("Entro a eliminar conceptos por cobertura");
		}

		if (session.containsKey("CODIGO_NIVEL0")) {
			codigoRamo = (String) session.get("CODIGO_NIVEL0");
			if (isDebugEnabled) {
				log.debug("CODIGORAMO-->" + codigoRamo);
			}
		}
		if (codigoRamo != null) {
			conceptosCoberturaManager.eliminaConceptosPorCobertura(codigoRamo,
					codigoPeriodo, orden);
		}
		success = true;
		return SUCCESS;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setConceptosCoberturaManager(
			ConceptosCoberturaManager conceptosCoberturaManager) {
		this.conceptosCoberturaManager = conceptosCoberturaManager;
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}

	public String getCodigoRamo() {
		return codigoRamo;
	}

	public void setCodigoRamo(String codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public List<ConceptosCoberturaVO> getListaConceptosCobertura() {
		return listaConceptosCobertura;
	}

	public void setListaConceptosCobertura(
			List<ConceptosCoberturaVO> listaConceptosCobertura) {
		this.listaConceptosCobertura = listaConceptosCobertura;
	}

	public List<LlaveValorVO> getListaPeriodos() {
		return listaPeriodos;
	}

	public void setListaPeriodos(List<LlaveValorVO> listaPeriodos) {
		this.listaPeriodos = listaPeriodos;
	}

	public List<LlaveValorVO> getListaCoberturas() {
		return listaCoberturas;
	}

	public void setListaCoberturas(List<LlaveValorVO> listaCoberturas) {
		this.listaCoberturas = listaCoberturas;
	}

	public List<LlaveValorVO> getListaConceptos() {
		return listaConceptos;
	}

	public void setListaConceptos(List<LlaveValorVO> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	public List<LlaveValorVO> getListaComportamientos() {
		return listaComportamientos;
	}

	public void setListaComportamientos(List<LlaveValorVO> listaComportamientos) {
		this.listaComportamientos = listaComportamientos;
	}

	public List<LlaveValorVO> getListaCondicion() {
		return listaCondicion;
	}

	public void setListaCondicion(List<LlaveValorVO> listaCondicion) {
		this.listaCondicion = listaCondicion;
	}

	public String getCodigoPeriodo() {
		return codigoPeriodo;
	}

	public void setCodigoPeriodo(String codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	public String getCodigoCobertura() {
		return codigoCobertura;
	}

	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getCodigoComportamiento() {
		return codigoComportamiento;
	}

	public void setCodigoComportamiento(String codigoComportamiento) {
		this.codigoComportamiento = codigoComportamiento;
	}

	public String getCodigoCondicion() {
		return codigoCondicion;
	}

	public void setCodigoCondicion(String codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getOrdenHidden() {
		return ordenHidden;
	}

	public void setOrdenHidden(String ordenHidden) {
		this.ordenHidden = ordenHidden;
	}

    /**
     * @return the msgRespuesta
     */
    public String getMsgRespuesta() {
        return msgRespuesta;
    }

    /**
     * @param msgRespuesta the msgRespuesta to set
     */
    public void setMsgRespuesta(String msgRespuesta) {
        this.msgRespuesta = msgRespuesta;
    }

}
