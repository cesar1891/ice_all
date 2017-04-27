
package mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoVehiculo.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoVehiculo">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AUTO_PICKUP"/>
 *     &lt;enumeration value="FRONTERIZO"/>
 *     &lt;enumeration value="CAMION"/>
 *     &lt;enumeration value="SERVICIO_PUBLICO_AUTOS"/>
 *     &lt;enumeration value="SERVICIO_PUBLICO_MICROS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoVehiculo")
@XmlEnum
public enum TipoVehiculo {

    AUTO_PICKUP,
    FRONTERIZO,
    CAMION,
    SERVICIO_PUBLICO_AUTOS,
    SERVICIO_PUBLICO_MICROS;

    public String value() {
        return name();
    }

    public static TipoVehiculo fromValue(String v) {
        return valueOf(v);
    }

}
