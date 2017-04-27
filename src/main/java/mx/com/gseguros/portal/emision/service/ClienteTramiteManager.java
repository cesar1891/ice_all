package mx.com.gseguros.portal.emision.service;

import java.util.Map;

public interface ClienteTramiteManager
{
	public Map<String,String> pantallaContratanteTramite(String ntramite) throws Exception;
	
	public void movimientoClienteTramite(String ntramite, String cdperson, String accion) throws Exception;
	
	public String recuperarNmsoliciTramite (String ntramite) throws Exception;
}