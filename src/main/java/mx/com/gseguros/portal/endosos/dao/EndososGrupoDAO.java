package mx.com.gseguros.portal.endosos.dao;

import java.util.List;
import java.util.Map;

public interface EndososGrupoDAO
{
	public List<Map<String,String>>buscarHistoricoPolizas(String nmpoliex,String rfc,String cdperson,String nombre,String cdsisrol)throws Exception;
	public List<Map<String,String>>cargarFamiliasPoliza(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem)throws Exception;
	public List<Map<String,String>>cargarIntegrantesFamilia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsitaux)throws Exception;
}