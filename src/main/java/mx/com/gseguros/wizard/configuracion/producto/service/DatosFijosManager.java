package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.datosFijos.model.DatoFijoVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;

public interface DatosFijosManager {
	List<DatoFijoVO> listaDatosFijos(String codigoRamo) throws ApplicationException;
	List<LlaveValorVO> catalogoBloque() throws ApplicationException;
	List<LlaveValorVO> catalogoCampo(String claveBloque) throws ApplicationException;
	void insertarDatoFijo(DatoFijoVO dfvo) throws ApplicationException;
}
