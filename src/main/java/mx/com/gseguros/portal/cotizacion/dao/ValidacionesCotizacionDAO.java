package mx.com.gseguros.portal.cotizacion.dao;

import java.util.Date;
import java.util.Map;

public interface ValidacionesCotizacionDAO {
	
	public String obtieneValidacionRetroactividad(String numSerie, Date feini)throws Exception;
}