package mx.com.gseguros.wizard.configuracion.producto.service;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;

public interface TreeManager {

	public List<RamaVO> obtenerNiveles(String tipoObjeto,
			ArrayList<String> parametros) throws ApplicationException;

	List<RamaVO> obtenerProductos() throws ApplicationException;

	List<RamaVO> obtenerEstructuraProducto() throws ApplicationException;

	List<RamaVO> obtenerEstructuraSituacion() throws ApplicationException;

	public List<RamaVO> obtenerEstructuraCobertura()
			throws ApplicationException;

	public List<RamaVO> obtenerEstructuraObjetos() throws ApplicationException;
	
	List<RamaVO> obtenerEstructuraPlanes() throws ApplicationException ;
}
