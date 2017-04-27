
package mx.com.gseguros.ws.folioserviciopublico.client.jaxws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mx.com.gseguros.ws.folioserviciopublico.client.jaxws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidarFolioResponse_QNAME = new QName("http://com.gs.folioserviciopublico.soap.folio", "validarFolioResponse");
    private final static QName _ValidarFolio_QNAME = new QName("http://com.gs.folioserviciopublico.soap.folio", "validarFolio");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mx.com.gseguros.ws.folioserviciopublico.client.jaxws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ValidarFolio }
     * 
     */
    public ValidarFolio createValidarFolio() {
        return new ValidarFolio();
    }

    /**
     * Create an instance of {@link ValidarFolioResponse }
     * 
     */
    public ValidarFolioResponse createValidarFolioResponse() {
        return new ValidarFolioResponse();
    }

    /**
     * Create an instance of {@link ResponseFolio }
     * 
     */
    public ResponseFolio createResponseFolio() {
        return new ResponseFolio();
    }

    /**
     * Create an instance of {@link EmAdmfolId }
     * 
     */
    public EmAdmfolId createEmAdmfolId() {
        return new EmAdmfolId();
    }

    /**
     * Create an instance of {@link RequestFolio }
     * 
     */
    public RequestFolio createRequestFolio() {
        return new RequestFolio();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarFolioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.folioserviciopublico.soap.folio", name = "validarFolioResponse")
    public JAXBElement<ValidarFolioResponse> createValidarFolioResponse(ValidarFolioResponse value) {
        return new JAXBElement<ValidarFolioResponse>(_ValidarFolioResponse_QNAME, ValidarFolioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarFolio }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.gs.folioserviciopublico.soap.folio", name = "validarFolio")
    public JAXBElement<ValidarFolio> createValidarFolio(ValidarFolio value) {
        return new JAXBElement<ValidarFolio>(_ValidarFolio_QNAME, ValidarFolio.class, null, value);
    }

}
