package mx.com.gseguros.portal.cotizacion.model;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaSlistSmapVO extends ManagerRespuestaBaseVO
{
	private List<Map<String,String>> slist = null;
	private Map<String,String>       smap  = null;
	
	public ManagerRespuestaSlistSmapVO()
	{}
	
	public ManagerRespuestaSlistSmapVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaSlistSmapVO(boolean exito,String respuesta,String respuestaOculta,List<Map<String,String>>slist,Map<String,String>smap)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.slist           = slist;
		this.smap            = smap;
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

	public Map<String, String> getSmap() {
		return smap;
	}

	public void setSmap(Map<String, String> smap) {
		this.smap = smap;
	}

	@Override
	public String toString()
	{
		return Utils.log(
				"Exito="              , exito
				,"\nRespuesta="       , respuesta
				,"\nRespuestaOculta=" , respuestaOculta
				,"\nSmap="            , smap
				,"\nSlist="           , slist
		);
	}
	
}