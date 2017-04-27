/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.aon.portal.web;

import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.web.model.CotizacionSaludVO;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.CookiesAware;

/**
 *
 * @author Jair
 */
public class DinosaurioAction extends PrincipalCoreAction implements CookiesAware{

    private Map<String, String> cookiesMap;
    private Dinosaurio dino;
    private String llave;
    private CotizacionSaludVO cotizacion;
    public boolean success;
    private Logger log=Logger.getLogger(DinosaurioAction.class);
    
    public String jsoo()
    {
    	log.debug(""
    			+ "\n########################"
    			+ "\n########################"
    			+ "\n######            ######"
    			+ "\n###### dinosaurio ######"
    			+ "\n######            ######");
        try
        {
        	success=true;
        }
        catch(Exception ex)
        {
        	log.debug("error en el dinosaurio",ex);
        	success=false;
        }
        log.debug(""
        		+ "\n######            ######"
    			+ "\n######            ######"
    			+ "\n###### dinosaurio ######"
    			+ "\n########################"
    			+ "\n########################");
        return SUCCESS;
    }
    
    public Map<String, String> getCookiesMap() {
        return cookiesMap;
    }
    
    public void setCookiesMap(Map<String, String> map) {
        this.cookiesMap=map;
    }

    public Dinosaurio getDino() {
        return dino;
    }

    public void setDino(Dinosaurio dino) {
        this.dino = dino;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public CotizacionSaludVO getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(CotizacionSaludVO cotizacion) {
        this.cotizacion = cotizacion;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
    
}
