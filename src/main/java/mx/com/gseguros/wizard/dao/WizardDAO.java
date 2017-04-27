package mx.com.gseguros.wizard.dao;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.conceptosCobertura.model.ConceptosCoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.datosFijos.model.DatoFijoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.reglaValidacion.model.ReglaValidacionVO;
import mx.com.gseguros.wizard.configuracion.producto.reglanegocio.model.ReglaNegocioVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolAtributoVariableVO;
import mx.com.gseguros.wizard.configuracion.producto.rol.model.RolVO;
import mx.com.gseguros.wizard.configuracion.producto.tipoObjeto.model.DatoVariableObjetoVO;
import mx.com.gseguros.wizard.model.MensajesVO;

public interface WizardDAO {
	
	public List<ReglaNegocioVO> obtenerValidaciones() throws Exception;
	public List<LlaveValorVO> obtenerValidacionesLlave()throws Exception;
	public List<ReglaNegocioVO> obtenerConceptoTarificacion() throws Exception;
	public List<ReglaNegocioVO> obtenerCondicion() throws Exception;
	public List<ReglaNegocioVO> obtenerAutorizacion() throws Exception;
	public List<ReglaNegocioVO> obtenerVarTemp() throws Exception;
	public List<ConceptosCoberturaVO> obtieneConceptosPorCobertura(Map<String, String> params) throws Exception;
	public List<LlaveValorVO> obtieneListaPeriodos(Map<String, String> params) throws Exception;
	public List<LlaveValorVO> obtieneListaCoberturas(Map<String, String> params)throws Exception;
	public List<LlaveValorVO> obtieneListaConceptos(Map<String, String> params)throws Exception;
	public List<LlaveValorVO> obtieneTipoConceptos(Map<String, Object> params)throws Exception;
	public List<LlaveValorVO> obtieneListaComportamientos(Map<String, String> params)throws Exception;
	public List<LlaveValorVO> obtieneListaCondiciones(Map<String, String> params) throws Exception;
	public List<LlaveValorVO> obtieneListaCondicionesLlave(Map<String, String> params)throws Exception;
	public void agregarConceptoPorCobertura(Map<String, String> params)throws Exception;
	public void eliminaConceptosPorCobertura(Map<String, String> params)throws Exception;
	
	public List<LlaveValorVO> catalogoAtributosVariablesJson(Map<String, Object> params) throws Exception;
	public List<LlaveValorVO> obtieneCatalogoRoles(Map<String, String> params) throws Exception;
	public List<RolAtributoVariableVO> atributosVariablesRol(Map<String, String> params) throws Exception;
	public RolVO obtieneRolAsociado(Map<String, String> params) throws Exception;
	public void agregaRolCatalogo(Map<String, String> params) throws Exception;
	public void agregaAtributoVariableRol(Map<String, Object> params) throws Exception;
	
	public List<LlaveValorVO> catalogoTipoObjetoJson(Map<String, String> params) throws Exception;
	public List<DatoVariableObjetoVO> listaDatosVariablesObjetos(Map<String, String> params) throws Exception;
	
	public List<DatoFijoVO> listaDatosFijos(Map<String, String> params) throws Exception;

	public List<ReglaNegocioVO> obtenerVariablesTemporalesAsociadasAlProducto(Map<String, String> params) throws Exception;

	public List<ReglaValidacionVO> obtieneReglasDeValidacion(Map<String, String> params) throws Exception;

	public void asociarReglasDeValidacion(Map<String, String> params) throws Exception;
	public void eliminaReglasDeValidacion(Map<String, String> params) throws Exception;
	
	public List<LlaveValorVO> obtieneListaBloques(Map<String, String> params) throws Exception;

	public List<LlaveValorVO> catalogoBloque(Map<String, Object> params) throws Exception;
	public List<LlaveValorVO> catalogoCampo(Map<String, Object> params) throws Exception;

	public List<LlaveValorVO> obtenerListaTablasApoyo(Map<String, String> params) throws Exception;

	public MensajesVO eliminaAtributoVariableRol(Map<String, Object> params) throws Exception;
	public MensajesVO borraTipoObjInciso(Map<String, Object> params) throws Exception;
	
	public void agregarRolInciso(Map<String, Object> params) throws Exception;
	public void agregarAtributoVariableCatalogo(Map<String, Object> params) throws Exception;
	public WrapperResultados eliminaRol(Map<String, Object> params) throws Exception;
	public MensajesVO tieneHijosAtributoVariableRol(Map<String, Object> params) throws Exception;
	public MensajesVO tieneHijosAtributoVariableObjeto(Map<String, Object> params) throws Exception;

	public void borraTatriObjeto(Map<String, Object> params) throws Exception;
	public void insertarDatoFijo(Map<String, Object> params) throws Exception;

	public void insertarValidacion(Map<String, Object> params) throws Exception;
	public void insertarConceptoTarif(Map<String, Object> params) throws Exception;
	public void insertarVariableTemp(Map<String, Object> params) throws Exception;
	public void insertarAutorizacion(Map<String, Object> params) throws Exception;
	public void insertarCondicion(Map<String, Object> params) throws Exception;

	public void agregarObjCatalogo(Map<String, Object> params) throws Exception;
	public void agregaDatosVarObj(Map<String, Object> params) throws Exception;
	public void agregaTipoObjInciso(Map<String, Object> params) throws Exception;

	public void asociarVariablesDelProducto(Map<String, Object> params) throws Exception;
	public void desasociarVariablesDelProducto(Map<String, Object> params) throws Exception;
	public void borraVarTmp(Map<String, Object> params) throws Exception;

	
	public List<Map<String, String>> obtieneTablasApoyo(Map<String,String> params) throws Exception;

	public String guardaTablaApoyo(Map<String,String> params) throws Exception;

	public String guardaClavesTablaApoyo(Map<String,String> params) throws Exception;
	
	public String guardaAtributosTablaApoyo(Map<String,String> params) throws Exception;

	public List<Map<String, String>> obtieneClavesTablaApoyo(Map<String,String> params) throws Exception;

	public List<Map<String, String>> obtieneAtributosTablaApoyo(Map<String,String> params) throws Exception;
}