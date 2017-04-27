package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

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
import mx.com.gseguros.portal.general.service.TemplateManager;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/templates")
public class TemplateAction extends PrincipalCoreAction {
    private static final Logger logger = LoggerFactory.getLogger(TemplateAction.class);
    private boolean success;
    private String message;
    private Map<String, String> params;
    private List<Map<String, String>> list;
    
    @Autowired
    private TemplateManager templateManager;

    @Action(value   = "entrarPantalla",
            results = {
                @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/proceso/templates/template.jsp")
            })
    public String entrarPantalla () {
        String result = ERROR;
        try {
            // validar sesion
            UserVO usuarioSesion = Utils.validateSession(session);
            Utils.validate(params, "No hay datos");
            String cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo");
            Utils.validate(cdunieco , "Falta cdunieco",
                           cdramo   , "Falta cdramo");
            String respuestaManager = templateManager.managerRegresaString(cdunieco, cdramo);
            message = "Correcto";
            result = SUCCESS;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        return result;
    }
    
    @Action(value   = "guardaPoliza",
            results = { @Result(name="success", type="json") }
            )
    public String guardaPoliza () {
        try {
            // validar sesion
            UserVO usuarioSesion = Utils.validateSession(session);
            String cdsisrol = usuarioSesion.getRolActivo().getClave();
            Utils.validate(params, "No hay datos");
            String cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo");
            Utils.validate(cdunieco , "Falta cdunieco",
                           cdramo   , "Falta cdramo");
            templateManager.managerRegresaString(cdunieco, cdramo, cdsisrol);
            message = "Correcto";
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        return SUCCESS;
    }
    
    ///////////////////////////////
    ////// Getters y setters //////
    
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

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }
    
    ////// Getters y setters //////
    ///////////////////////////////
    
}