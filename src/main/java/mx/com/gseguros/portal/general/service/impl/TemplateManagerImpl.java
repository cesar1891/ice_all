package mx.com.gseguros.portal.general.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.service.TemplateManager;
import mx.com.gseguros.utils.Utils;

@Service
public class TemplateManagerImpl implements TemplateManager {
    private final static Logger logger = LoggerFactory.getLogger(TemplateManagerImpl.class);
    
    @Override
    public String managerRegresaString(Object... params) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void guardarDatosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception {
        String paso = null;
        try {
            paso = "Guardando datos de p\u00f3liza";
            logger.debug(paso);
            // templateDAO.guardarDatosPoliza(...
            
            paso = "Actualizando suplemento";
            logger.debug(paso);
            // templateDAO.actualizaSuplemento(...
        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso);
        }
    }

    @Override
    public List<Map<String, String>> recuperarAsegurados(String cdunieco, String cdramo, String estado,
            String nmpoliza, String nmsuplem) throws Exception {
        List<Map<String, String>> listaAsegurados = null;
        String paso = null;
        try {
            paso = "Recuperando asegurados";
            logger.debug(paso);
            // listaAsegurados = templateDAO.recuperarAsegurados(....
            if (listaAsegurados == null || listaAsegurados.size() == 0) {
                throw new ApplicationException("No hay asegurados");
            }

            paso = "Actualizando asegurados";
            logger.debug(paso);
            for (Map<String, String> aseguradoIterado : listaAsegurados) {
                aseguradoIterado.put("fechaConsulta", Utils.format(new Date()));
            }
        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso);
        }
        return listaAsegurados;
    }
    
}