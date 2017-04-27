package mx.com.gseguros.portal.general.controller;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.mesacontrol.service.FlujoMesaControlManager;

/**
 * JTEZVA 1 NOV 2016
 * ACTION PARA LOS SERVICIOS DE MESA DE CONTROL
 */
@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
@Namespace("/serviciosmc")
public class ServiciosMesaControlAction extends PrincipalCoreAction
{
    private static final long serialVersionUID = 7424518550886733034L;
    private boolean success;
    private String message;
    private Map<String, String> params;
    
    @Autowired
    private FlujoMesaControlManager flujoMesaControlManager;
    
    public ServiciosMesaControlAction () {
        this.session = ServletActionContext.getContext().getSession();
    }
    
    // TODO FIXME
}