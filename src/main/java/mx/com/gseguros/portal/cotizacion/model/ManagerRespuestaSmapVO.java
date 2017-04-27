package mx.com.gseguros.portal.cotizacion.model;

import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaSmapVO extends ManagerRespuestaBaseVO
{
	private Map<String,String> smap = null;
	
	public ManagerRespuestaSmapVO()
	{}
	
	public ManagerRespuestaSmapVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaSmapVO(boolean exito,String respuesta,String respuestaOculta,Map<String,String>smap)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.smap            = smap;
	}

	/*
	 * Getters y setters
	 */
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
		);
	}
	
}