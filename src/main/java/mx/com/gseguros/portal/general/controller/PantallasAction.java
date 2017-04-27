package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/pantallas")
public class PantallasAction extends PrincipalCoreAction {
    private final static Logger logger = LoggerFactory.getLogger(PantallasAction.class);
    private boolean success;
    private String message;
    private Map<String, String> params;
    
    @Autowired
    private PantallasManager pantallasManager;
    
    @Action(value   = "obtenerComponentes",
            results = { @Result(name="success", type="json") }
            )
    public String obtenerComponentes () {
        logger.debug(Utils.log(
                "\n################################",
                "\n###### obtenerComponentes ######",
                "\n###### params = ", params));
        try {
            UserVO usuario = Utils.validateSession(session);
            String cdsisrol = usuario.getRolActivo().getClave();
            String paso = null;
            Utils.validate(params, "No se recibieron datos para recuperar componentes");
            String cdtiptra = params.get("cdtiptra"),
                   cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo"),
                   cdtipsit = params.get("cdtipsit"),
                   estado   = params.get("estado"),
                   pantalla = params.get("pantalla"),
                   seccion  = params.get("seccion"),
                   orden    = params.get("orden");
            Utils.validate(pantalla , "Falta pantalla",
                           seccion  , "Falta secci\u00f3n");
            boolean fields  = "S".equals(params.get("fields")),
                    items   = "S".equals(params.get("items")),
                    columns = "S".equals(params.get("columns")),
                    buttons = "S".equals(params.get("buttons"));
            if (fields == false && items == false && columns == false && buttons == false) {
                throw new ApplicationException("Debe enviar al menos un tipo de componente (fields, items, columns, buttons)");
            }
            try {
                paso = "Recuperando componentes";
                logger.debug(paso);
                List<ComponenteVO> lista = pantallasManager.obtenerComponentes(cdtiptra, cdunieco, cdramo, cdtipsit, estado, cdsisrol,
                        pantalla, seccion, orden);
                if (lista == null || lista.size() == 0) {
                    throw new ApplicationException("No hay componentes configurados para la pantalla y secci\u00f3n");
                }
                
                paso = "Generando componentes";
                logger.debug(paso);
                GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
                gc.generaComponentes(lista, true, fields, items, columns, false, buttons);
                
                paso = "Extrayendo componentes";
                logger.debug(paso);
                if (fields) {
                    params.put("fields", gc.getFields().toString());
                }
                if (items) {
                    params.put("items", gc.getItems().toString());
                }
                if (columns) {
                    params.put("columns", gc.getColumns().toString());
                }
                if (buttons) {
                    params.put("buttons", gc.getButtons().toString());
                }
            } catch (Exception ex) {
                Utils.generaExcepcion(ex, paso);
            }
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log(
                "\n###### success = " , success,
                "\n###### message = " , message,
                "\n###### obtenerComponentes ######",
                "\n################################"));
        return SUCCESS;
    }
    
    /////////////////////////////////////////////////////////////////////
    ///////////////////////// GETTERS Y SETTERS /////////////////////////
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, String> getParams() {
        return params;
    }
    
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    ///////////////////////// GETTERS Y SETTERS /////////////////////////
    /////////////////////////////////////////////////////////////////////
}