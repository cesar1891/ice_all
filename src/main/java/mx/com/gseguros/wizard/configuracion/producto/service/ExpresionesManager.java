package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.HojaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;

public interface ExpresionesManager {
	public abstract List<HojaVO> funcionesArbol() throws ApplicationException;
	public abstract List<HojaVO> variablesTemporalesArbol() throws ApplicationException;
	public abstract List<RamaVO> camposDelProductoArbol() throws ApplicationException;
	public abstract PagedList listaTabla(int start, int limit) throws ApplicationException;
	public abstract List<LlaveValorVO> listaColumna(String idTabla) throws ApplicationException;
	public abstract List<ClaveVO> listaClave(String idTabla) throws ApplicationException;
	public abstract List<ClaveVO> listaPropertyGrid() throws ApplicationException;
	public abstract List<VariableVO> listaComboVariables() throws ApplicationException;
	public abstract boolean agregarVariables(List<VariableVO> listaVariables, int codigoExpresion, String ottiporg) throws ApplicationException;
	public abstract int insertarExpresion(ExpresionVO expresionVO,boolean success)throws ApplicationException;
	public abstract void eliminarVariableLocalExpresion(Map<String,String> params) throws ApplicationException;
	public abstract int obtieneSecuenciaExpresion()throws ApplicationException;
	public abstract boolean agregarClave(List<ClaveVO> listaClaves, int codigoExpresion,String codigoVariable, String ottiporg) throws ApplicationException;
	public abstract List<ExpresionVO> obtieneExpresion(int codigoExpresion);
	public abstract List<VariableVO> obtieneVariableExpresion(int codigoExpresion) throws ApplicationException;
	public abstract List<ClaveVO> obtieneClaves(VariableVO variableLocal)throws ApplicationException;
	public WrapperResultados validarExpresion(int codigoExpresion, String expresion, String tipoExpresion , String cdVariable) throws Exception;
	public boolean existeExpresion(String codigoExpresion) throws Exception;
}
