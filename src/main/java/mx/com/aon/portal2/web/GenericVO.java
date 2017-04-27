package mx.com.aon.portal2.web;

import java.io.Serializable;

import mx.com.gseguros.utils.Utils;

/**
 *
 * @author Jair
 */
public class GenericVO implements Serializable {
    
	private static final long serialVersionUID = -5060990997960225913L;
	
	private String key
	               ,value
	               ,aux
                   ,aux2
                   ,aux3;
    
    public GenericVO(){}
    
    public GenericVO(String key,String value)
    {
        this.key   = key;
        this.value = value;
    }
    
    public GenericVO(String key,String value,String aux)
    {
        this.key   = key;
        this.value = value;
        this.aux   = aux;
    }
    
    public GenericVO(String key,String value,String aux,String aux2)
    {
        this.key   = key;
        this.value = value;
        this.aux   = aux;
        this.aux2  = aux2;
    }
    
    public GenericVO(String key,String value,String aux,String aux2,String aux3)
    {
        this.key   = key;
        this.value = value;
        this.aux   = aux;
        this.aux2  = aux2;
        this.aux3  = aux3;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString()
    {
    	return Utils.log("key=",key,", value=",value,", aux=",aux);
    }

	public String getAux() {
		return aux;
	}

	public void setAux(String aux) {
		this.aux = aux;
	}

	public String getAux2() {
		return aux2;
	}

	public void setAux2(String aux2) {
		this.aux2 = aux2;
	}

	public String getAux3() {
		return aux3;
	}

	public void setAux3(String aux3) {
		this.aux3 = aux3;
	}
    
}
