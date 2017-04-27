package mx.com.gseguros.portal.cotizacion.model;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaSlist2VO extends ManagerRespuestaBaseVO
{
	private List<Map<String,String>> slist  = null;
	private List<Map<String,String>> slist2 = null;
	
	public ManagerRespuestaSlist2VO()
	{}
	
	public ManagerRespuestaSlist2VO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaSlist2VO(
			boolean exito
			,String respuesta
			,String respuestaOculta
			,List<Map<String,String>>slist
			,List<Map<String,String>>slist2
			)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.slist           = slist;
		this.slist2          = slist2;
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

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	@Override
	public String toString()
	{
		return Utils.log(
				"Exito="              , exito
				,"\nRespuesta="       , respuesta
				,"\nRespuestaOculta=" , respuestaOculta
				,"\nSlist="           , slist
				,"\nSlist2="          , slist2
		);
	}
	
}