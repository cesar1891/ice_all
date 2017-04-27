package mx.com.gseguros.portal.general.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.service.ServiciosManager;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="json-default")
@Namespace("/rstn")
public class RstnAction extends PrincipalCoreAction {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String message;
    private Map<String, String> params;
    private List<Map<String, String>> list;
    
    @Autowired
    private ServiciosManager serviciosManager;
    
    @Action(value   = "recuperarPersonasPorRFCoPorNombre",
            results = { @Result(name="success", type="json") }
            )
    public String recuperarPersonasPorRFCoPorNombre () {
        logger.debug(Utils.log("\n###############################################",
                               "\n###### recuperarPersonasPorRFCoPorNombre ######",
                               "\n###### params = ", params));
        try {
            Utils.validate(params, "Faltan params");
            String rfc    = params.get("rfc"),
                   nombre = params.get("nombre");
            if (StringUtils.isBlank(rfc) && StringUtils.isBlank(nombre)) {
                throw new ApplicationException("rfc y nombre vacios");
            }
            list = serviciosManager.recuperarPersonasPorRFCoPorNombre(rfc, nombre);
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = ", success,
                               "\n###### message = ", message,
                               "\n###### recuperarPersonasPorRFCoPorNombre ######",
                               "\n###############################################"));
        return SUCCESS;
    }
    
    @Action(value   = "recuperarPolizasPorCdpersonYcdramo",
            results = { @Result(name="success", type="json") }
            )
    public String recuperarPolizasPorCdpersonYcdramo () {
        logger.debug(Utils.log("\n################################################",
                               "\n###### recuperarPolizasPorCdpersonYcdramo ######",
                               "\n###### params = ", params));
        try {
            Utils.validate(params, "Faltan params");
            String cdperson = params.get("cdperson"),
                   cdramo   = params.get("cdramo");
            Utils.validate(cdperson , "Falta cdperson",
                           cdramo   , "Falta cdramo");
            list = serviciosManager.recuperarPolizasPorCdpersonYcdramo(cdperson, cdramo);
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = ", success,
                               "\n###### message = ", message,
                               "\n###### recuperarPolizasPorCdpersonYcdramo ######",
                               "\n################################################"));
        return SUCCESS;
    }
    
    @Action(value   = "recuperarCoberturasAmparadasPorPolizaYasegurado",
            results = { @Result(name="success", type="json") }
            )
    public String recuperarCoberturasAmparadasPorPolizaYasegurado () {
        logger.debug(Utils.log("\n#############################################################",
                               "\n###### recuperarCoberturasAmparadasPorPolizaYasegurado ######",
                               "\n###### params = ", params));
        try {
            Utils.validate(params, "Faltan params");
            String cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo"),
                   estado   = params.get("estado"),
                   nmpoliza = params.get("nmpoliza"),
                   nmsuplem = params.get("nmsuplem"),
                   nmsituac = params.get("nmsituac");
            Utils.validate(cdunieco , "Falta cdunieco",
                           cdramo   , "Falta cdramo",
                           estado   , "Falta estado",
                           nmpoliza , "Falta nmpoliza",
                           nmsuplem , "Falta nmsuplem",
                           nmsituac , "Falta nmsituac");
            list = serviciosManager.recuperarCoberturasAmparadasPorPolizaYasegurado(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac);
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = ", success,
                               "\n###### message = ", message,
                               "\n###### recuperarCoberturasAmparadasPorPolizaYasegurado ######",
                               "\n#############################################################"));
        return SUCCESS;
    }
    
    @Action(value   = "recuperarDetallePoliza",
            results = { @Result(name="success", type="json") }
            )
    public String recuperarDetallePoliza () {
        logger.debug(Utils.log("\n####################################",
                               "\n###### recuperarDetallePoliza ######",
                               "\n###### params = ", params));
        try {
            Utils.validate(params, "Faltan params");
            String cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo"),
                   estado   = params.get("estado"),
                   nmpoliza = params.get("nmpoliza");
            Utils.validate(cdunieco , "Falta cdunieco",
                           cdramo   , "Falta cdramo",
                           estado   , "Falta estado",
                           nmpoliza , "Falta nmpoliza");
            params =  serviciosManager.recuperarDetallePoliza(cdunieco, cdramo, estado, nmpoliza);
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = ", success,
                               "\n###### message = ", message,
                               "\n###### recuperarDetallePoliza ######",
                               "\n####################################"));
        return SUCCESS;
    }
    
    @Action(value   = "recuperarDetalleAseguradosPoliza",
            results = { @Result(name="success", type="json") }
            )
    public String recuperarDetalleAseguradosPoliza () {
        logger.debug(Utils.log("\n##############################################",
                               "\n###### recuperarDetalleAseguradosPoliza ######",
                               "\n###### params = ", params));
        try {
            Utils.validate(params, "Faltan params");
            String cdunieco = params.get("cdunieco"),
                   cdramo   = params.get("cdramo"),
                   estado   = params.get("estado"),
                   nmpoliza = params.get("nmpoliza"),
                   nmsuplem = params.get("nmsuplem");
            Utils.validate(cdunieco , "Falta cdunieco",
                           cdramo   , "Falta cdramo",
                           estado   , "Falta estado",
                           nmpoliza , "Falta nmpoliza",
                           nmsuplem , "Falta nmsuplem");
            list = serviciosManager.recuperarDetalleAseguradosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
            success = true;
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        logger.debug(Utils.log("\n###### success = ", success,
                               "\n###### message = ", message,
                               "\n###### recuperarDetalleAseguradosPoliza ######",
                               "\n##############################################"));
        return SUCCESS;
    }
    
    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// GETTERS Y SETTERS /////////////////////////////
    
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
    
    ///////////////////////////// GETTERS Y SETTERS /////////////////////////////
    /////////////////////////////////////////////////////////////////////////////    
}