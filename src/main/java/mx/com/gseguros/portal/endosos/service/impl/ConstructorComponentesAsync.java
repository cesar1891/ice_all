package mx.com.gseguros.portal.endosos.service.impl;

import java.util.List;

import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.ThreadCounter;
import mx.com.gseguros.portal.general.util.GeneradorCampos;

import org.apache.log4j.Logger;

public class ConstructorComponentesAsync extends Thread
{
	private static Logger logger = Logger.getLogger(ConstructorComponentesAsync.class);
	private PantallasDAO  pantallasDAO;
	private ThreadCounter tc;
	private String servletContextName
	               ,cdtiptra
	               ,cdunieco
	               ,cdramo
	               ,cdtipsit
	               ,estado
	               ,cdsisrol
	               ,pantalla
	               ,seccion
	               ,orden
	               ,llaveGenerador;
	private boolean esParcial
	                ,conField
	                ,conItem
	                ,conColumn
	                ,conEditor
	                ,conButton;
	
	public ConstructorComponentesAsync(
			String llaveGenerador
			,String cdtiptra
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String cdsisrol
			,String pantalla
			,String seccion
			,String orden
			,boolean esParcial
            ,boolean conField
            ,boolean conItem
            ,boolean conColumn
            ,boolean conEditor
            ,boolean conButton
			)
	{
		logger.debug("async ConstructorComponentesAsync");
		this.llaveGenerador = llaveGenerador;
		this.cdtiptra       = cdtiptra;
		this.cdunieco       = cdunieco;
		this.cdramo         = cdramo;
		this.cdtipsit       = cdtipsit;
		this.estado         = estado;
		this.cdsisrol       = cdsisrol;
		this.pantalla       = pantalla;
		this.seccion        = seccion;
		this.orden          = orden;
		this.esParcial      = esParcial;
		this.conField       = conField;
		this.conItem        = conItem;
		this.conColumn      = conColumn;
		this.conEditor      = conEditor;
		this.conButton      = conButton;
	}
	
	public void setPropiedadesPadre(ThreadCounter tc,String servletContextName,PantallasDAO pantallasDAO)
	{
		logger.debug("async setPropiedadesPadre");
		this.tc                 = tc;
		this.servletContextName = servletContextName;
		this.pantallasDAO       = pantallasDAO;
	}
	
	@Override
	public void run()
	{
		try
		{
			List<ComponenteVO> lista = pantallasDAO.obtenerComponentes(
					cdtiptra
					,cdunieco
					,cdramo
					,cdtipsit
					,estado
					,cdsisrol
					,pantalla
					,seccion
					,orden
					);
			
			GeneradorCampos gc = new GeneradorCampos(servletContextName);
			gc.generaComponentes(lista, esParcial, conField, conItem, conColumn, conEditor, conButton);
			
			tc.ciclo(llaveGenerador, gc);
		}
		catch(Exception ex)
		{
			logger.error("Logueando error desde constructor asincrono",ex);
			tc.setException(ex);
		}
	}
}