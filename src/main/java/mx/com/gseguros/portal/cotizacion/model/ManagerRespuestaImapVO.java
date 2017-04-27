package mx.com.gseguros.portal.cotizacion.model;

import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaImapVO extends ManagerRespuestaBaseVO
{
	private Map<String,Item> imap   = null;
	
	public ManagerRespuestaImapVO()
	{}
	
	public ManagerRespuestaImapVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaImapVO(boolean exito,String respuesta,String respuestaOculta,Map<String,Item>imap)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.imap            = imap;
	}

	/*
	 * Getters y setters
	 */
	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}
	
	@Override
	public String toString()
	{
		return Utils.log(
				"Exito="              , exito
				,"\nRespuesta="       , respuesta
				,"\nRespuestaOculta=" , respuestaOculta
				,"\nImap="            , imap
		);
	}
	
}