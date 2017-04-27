package mx.com.gseguros.portal.general.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.endosos.service.impl.ConstructorComponentesAsync;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;

public class ThreadCounter
{
	private static Logger logger = Logger.getLogger(ThreadCounter.class);
	
	private int                               contador;
	private Exception                         ex;
	private Map<String,GeneradorCampos>       generadores;
	private String                            servletContextName;
	private PantallasDAO                      pantallasDAO;
	private List<ConstructorComponentesAsync> constructores;
	
	public ThreadCounter(String servletContextName, PantallasDAO pantallasDAO)
	{
		this.servletContextName = servletContextName;
		this.pantallasDAO       = pantallasDAO;
		this.generadores        = new HashMap<String,GeneradorCampos>();
		this.constructores      = new ArrayList<ConstructorComponentesAsync>();
	}
	
	public void agregarConstructor(ConstructorComponentesAsync con)
	{
		con.setPropiedadesPadre(this, servletContextName, pantallasDAO);
		this.constructores.add(con);
		this.contador = this.contador+1;
	}
	
	public Map<String,GeneradorCampos> run() throws Exception
	{
		for(ConstructorComponentesAsync con : constructores)
		{
			con.start();
		}
		
		while(contador>0 && ex==null)
		{
			Thread.sleep(1);
		}
		
		if(ex!=null)
		{
			throw ex;
		}
		
		return generadores;
	}
	
	public void ciclo(String key,GeneradorCampos gc)
	{
		if(this.ex==null)
		{
			generadores.put(key,gc);
			contador--;
			logger.debug(Utils.log("async ciclo ",contador));
		}
	}
	
	public void setException(Exception ex)
	{
		if(this.ex==null)
		{
			logger.error("Excepcion que rompe los hilos",ex);
			this.ex = ex;
		}
		else
		{
			logger.error("Excepcion secundaria despues de rotos los hilos",ex);
		}
	}
}