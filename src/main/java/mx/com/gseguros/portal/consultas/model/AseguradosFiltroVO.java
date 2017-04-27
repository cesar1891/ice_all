package mx.com.gseguros.portal.consultas.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author DBB
 *
 */

/**
 * Objeto conformado por un mapa que contiene lista de asegurados por grupo y un entero 
 * con el total de asegurados que conforman el grupo.
 * Lista que contiene el detalle de los asegurados
 * total de asegurados que contiene la lista
 */

public class AseguradosFiltroVO implements Serializable{
	
	private List<Map<String,String>> asegurados;
	private int total;

	public List<Map<String,String>> getAsegurados() {
		return asegurados;
	}
	public void setAsegurados(List<Map<String,String>> asegurados) {
		this.asegurados = asegurados;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}