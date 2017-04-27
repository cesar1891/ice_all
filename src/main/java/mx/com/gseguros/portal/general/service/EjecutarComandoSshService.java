package mx.com.gseguros.portal.general.service;

import java.util.List;

public interface EjecutarComandoSshService {
	public String ejectutarCmd(String server,String usuario,String pass, List<String> cmd) throws Exception;
}
