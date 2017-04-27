package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;

public interface TemplateDAO {
    public void guardarDatosPoliza (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
    public List<Map<String, String>> recuperarAsegurados(String cdunieco, String cdramo, String estado,
            String nmpoliza, String nmsuplem) throws Exception;
}