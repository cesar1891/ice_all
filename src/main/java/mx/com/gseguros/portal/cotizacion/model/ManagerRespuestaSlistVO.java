package mx.com.gseguros.portal.cotizacion.model;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaSlistVO extends ManagerRespuestaBaseVO
{
	private List<Map<String,String>> slist = null;
	
	public ManagerRespuestaSlistVO()
	{}
	
	public ManagerRespuestaSlistVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaSlistVO(boolean exito,String respuesta,String respuestaOculta,List<Map<String,String>>slist)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.slist           = slist;
	}

	/*
	 * Getters y setters
	 */
	public List<Map<String, String>> getSlist() {
		return slist;
	}

	public void setSlist(List<Map<String, String>> slist) {
		this.slist = slist;
	}

	@Override
	public String toString()
	{
		return Utils.log(
				"Exito="              , exito
				,"\nRespuesta="       , respuesta
				,"\nRespuestaOculta=" , respuestaOculta
				,"\nSlist="           , slist
		);
	}
	
}