package mx.com.gseguros.portal.consultas.controller;


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
import mx.com.gseguros.portal.consultas.model.PerfilAseguradoVO;
import mx.com.gseguros.portal.consultas.service.ConsultasPerfilMedicoManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.utils.Utils;

@Controller
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/perfilMedico")
public class ConsultasPerfilMedicoAction extends PrincipalCoreAction {
    
	private static final Logger logger = LoggerFactory.getLogger(ConsultasPerfilMedicoAction.class);
    private boolean success;
    private String message;
    private Map<String, String> params;
    private List<Map<String, String>> list;
    
    
    @Autowired
    private ConsultasPerfilMedicoManager consultasManager;
    
    //consulta el perfil medico de uno o mas asegurados
    @Action(value   = "consultarPerfil",
    	results = { @Result(name="success", type="json") 
    	})
    public String consultarPerfil () {
        String result = ERROR;
        try {
        	// validar sesion
            UserVO usuarioSesion = Utils.validateSession(session);
            Utils.validate(params, "No hay datos");
            
            String listaPersonas = params.get("listaPersonas");
            Utils.validate(listaPersonas, "Falta listaPersonas");
            
            params.put("pv_lsperson_i", listaPersonas);
            this.list = consultasManager.consultaPerfilAsegurados(params);
                       
            message = "Correcto";
            success= true;
            result = SUCCESS;
            
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        return result;
    }

    
    @Action(value   = "consultarIcds",
    	results = { @Result(name="success", type="json") 
    	})
    public String consultarIcds () {
        String result = ERROR;
        try {
            logger.debug("entrando al action");
        	// validar sesion
            UserVO usuarioSesion = Utils.validateSession(session);
            Utils.validate(params, "No hay datos");
            
            String cdperson = params.get("cdperson");//pv_cdperson_i
            Utils.validate(cdperson, "Falta cdperson");
            
            params.put("pv_cdperson_i", cdperson);
            
            PerfilAseguradoVO icds = new PerfilAseguradoVO();
            icds= consultasManager.consultaICDSAsegurado(params);
            list = icds.getIcds();
            
            message = "Correcto";
            success= true;
            result = SUCCESS;
            
        } catch (Exception ex) {
            message = Utils.manejaExcepcion(ex);
        }
        return result;
    }
    
    
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

    
}