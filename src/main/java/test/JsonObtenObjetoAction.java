/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal2.web.GenericVO;
import test.model.Cotizacion;

/**
 *
 * @author Jair
 */
public class JsonObtenObjetoAction extends PrincipalCoreAction {
    
    private boolean success;
    private test.model.Cotizacion data=null;
    
    public String obtenCotizacion()
    {
        success=true;
        data=new Cotizacion(
                new GenericVO("1","A"),
                5000.00d,
                new GenericVO("1","B"),
                new GenericVO("1","C"),
                new GenericVO("1","D"),
                new GenericVO("1","E"),
                new GenericVO("1","F"),
                new GenericVO("1","G"),
                new GenericVO("1","H"),
                new GenericVO("1","I"),
                new GenericVO("1","J"));
        System.out.println("##### SUCCESS");
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Cotizacion getData() {
        return data;
    }

    public void setData(Cotizacion data) {
        this.data = data;
    }
    
}
