
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoProducto.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoProducto">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ROJO"/>
 *     &lt;enumeration value="VERDE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoProducto")
@XmlEnum
public enum TipoProducto {

    ROJO,
    VERDE;

    public String value() {
        return name();
    }

    public static TipoProducto fromValue(String v) {
        return valueOf(v);
    }

}
