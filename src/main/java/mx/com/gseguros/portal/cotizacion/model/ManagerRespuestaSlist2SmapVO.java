package mx.com.gseguros.portal.cotizacion.model;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Utils;

public class ManagerRespuestaSlist2SmapVO extends ManagerRespuestaBaseVO
{
	private Map<String,String>       smap   = null;
	private List<Map<String,String>> slist1 = null;
	private List<Map<String,String>> slist2 = null;
	
	public ManagerRespuestaSlist2SmapVO()
	{}
	
	public ManagerRespuestaSlist2SmapVO(boolean exito)
	{
		this.exito = exito;
	}
	
	public ManagerRespuestaSlist2SmapVO(
			boolean exito
			,String respuesta
			,String respuestaOculta
			,Map<String,String>smap
			,List<Map<String,String>>slist1
			,List<Map<String,String>>slist2
			)
	{
		this.exito           = exito;
		this.respuesta       = respuesta;
		this.respuestaOculta = respuestaOculta;
		this.smap            = smap;
		this.slist1          = slist1;
		this.slist2          = slist2;
	}

	/*
	 * Getters y setters
	 */
	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
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
				,"\nSlist1="          , slist1
				,"\nSlist2="          , slist2
		);
	}
	
}