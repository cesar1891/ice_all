package mx.com.gseguros.wizard.configuracion.producto.web;

import java.util.Map;

import mx.com.gseguros.wizard.configuracion.producto.service.TablaApoyoManager;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Clase padre de la cual heredan todos los Actions de la aplicacion.
 * <p>
 * Esta clase contiene atributos que contienen la sesion de la aplicacion web,
 * asi como tambien objetos genericos que contiene las especificaciones para el
 * llenado de combos que son "Tablas de Apoyo" dentro del sistema.
 * 
 * @since 1.0
 * @author <a href="mailto:adolfo.gonzalez@biosnettcs.com">Adolfo Gonzalez</a>
 * @version $Id$
 * @see ActionSupport
 * @see SessionAware
 * 
 */
public abstract class Padre extends ActionSupport implements SessionAware {

	/**
	 * Atributo que se utiliza para obtener los valores de los combos que
	 * provienen de tablas de apoyo.
	 */
	private TablaApoyoManager tablasManager;

	/**
	 * Atributo que contiene los atributos de sesion que se utilizan en el
	 * sistema.
	 */
	public Map session;

	/**
	 * Retorna objeto de tipo {@link TablaApoyoManager} que contiene los metodos
	 * para obtener los valores de combos que son tablas de apoyo.
	 * 
	 * @return Objeto de tipo {@link TablaApoyoManager}.
	 */
	public TablaApoyoManager getTablasManager() {
		return tablasManager;
	}

	/**
	 * Asigna objeto de tipo {@link TablaApoyoManager} que contiene los metodos
	 * para obtener los valores de combos que son tablas de apoyo.
	 * 
	 * @param tablasManager
	 *            Objeto de tipo {@link TablaApoyoManager}.
	 */
	public void setTablasManager(TablaApoyoManager tablasManager) {
		this.tablasManager = tablasManager;
	}

	/**
	 * Asigna objeto de tipo {@link java.util.Map} que contien la session de la
	 * aplicacion web.
	 * 
	 * @param session
	 *            Objeto de tipo {@link java.util.Map} que contiene la session.
	 */
	public void setSession(Map session) {
		this.session = session;
	}

}
