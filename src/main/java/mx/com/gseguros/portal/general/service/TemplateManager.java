package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;

public interface TemplateManager {
    public String managerRegresaString (Object... params) throws Exception;
    
    public void guardarDatosPoliza (String cdunieco, String cdramo, String estado, String nmpoliza) throws
        Exception;
    
    public List<Map<String, String>> recuperarAsegurados (String cdunieco, String cdramo, String estado,
            String nmpoliza, String nmsuplem) throws Exception;
    
}