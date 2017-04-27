package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model.ConceptosCoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;

public interface ConceptosCoberturaManager {

	List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(String codigoRamo) throws ApplicationException;

	List<LlaveValorVO> obtieneListaPeriodos(String codigoRamo)throws ApplicationException;

	List<LlaveValorVO> obtieneListaCoberturas(String codigoRamo)throws ApplicationException;

	List<LlaveValorVO> obtieneListaConceptos(String parametro)throws ApplicationException;

	List<LlaveValorVO> obtieneListaComportamientos()throws ApplicationException;

	List<LlaveValorVO> obtieneListaCondiciones() throws ApplicationException;

	void agregarConceptoPorCobertura(ConceptosCoberturaVO conceptoCobertura,
			String codigoRamo)throws ApplicationException;

	void eliminaConceptosPorCobertura(String codigoRamo, String codigoPeriodo,
			String orden) throws ApplicationException;
	
	public List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(
			String codigoRamo, String codigoSituacion, String codigoCobertura)
			throws ApplicationException;

}
