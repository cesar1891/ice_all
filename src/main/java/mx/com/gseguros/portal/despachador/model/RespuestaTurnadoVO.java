package mx.com.gseguros.portal.despachador.model;

import mx.com.gseguros.utils.Utils;

public class RespuestaTurnadoVO {
    private boolean encolado;
    private String message;
    
    public boolean isEncolado() {
        return encolado;
    }
    public void setEncolado(boolean encolado) {
        this.encolado = encolado;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString () {
        return Utils.join("{ message: ", message, ", encolado: ", encolado, " }");
    }
}