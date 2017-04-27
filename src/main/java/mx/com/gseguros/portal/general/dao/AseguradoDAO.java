package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;

public interface AseguradoDAO {
	
	/**
	 * Valida la edad de los asegurados en una poliza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return Lista de los asegurados de edad invalida
	 * @throws Exception
	 */
	public List<Map<String,String>> validaEdadAsegurados(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception;
	
	/**
	 * Agrega un asegurado y lo asigna a una poliza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param feefecto
	 * @param dsnombre
	 * @param dsnombre1
	 * @param paterno
	 * @param materno
	 * @param cdrfc
	 * @param sexo
	 * @param fenacimi
	 * @param cdestciv
	 * @param dsocupacion
	 * @param cdtipsit
	 * @param cdplan
	 * @param nmorddom
	 * @param cdagrupa
	 * @param otvalor01
	 * @param otvalor02
	 * @param otvalor03
	 * @param otvalor04
	 * @param otvalor05
	 * @param otvalor06
	 * @param otvalor07
	 * @param otvalor08
	 * @param otvalor09
	 * @param otvalor10
	 * @throws Exception
	 */
    public void agregarAsegurado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
            String feefecto, String dsnombre, String dsnombre1, String paterno, String materno, String cdrfc,
            String sexo, String fenacimi, String cdestciv, String dsocupacion, String cdtipsit, String cdplan,
            String nmorddom, String cdagrupa, String otvalor01, String otvalor02, String otvalor03, String otvalor04,
            String otvalor05, String otvalor06, String otvalor07, String otvalor08, String otvalor09, String otvalor10)
            throws Exception;
    
}