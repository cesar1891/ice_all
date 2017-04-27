package mx.com.gseguros.portal.cotizacion.model;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaVoidVO extends ManagerRespuestaBaseVO
{
	
	public ManagerRespuestaVoidVO()
	{}
	
	public ManagerRespuestaVoidVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaVoidVO(boolean exito,String respuesta,String respuestaOculta)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
	}
	
	@Override
	public String toString()
	{
		return Utils.log(
				"Exito="              , exito
				,"\nRespuesta="       , respuesta
				,"\nRespuestaOculta=" , respuestaOculta
		);
	}
	
}