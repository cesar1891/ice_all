package mx.com.gseguros.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
/**
 * Clase para Invocar los servicios del Anexo4F
 * @version 1.0.0
 */
public class WebServicesUtil {
	
	private final static Log logger = LogFactory.getLog(WebServicesUtil.class);
    
	/**
	 * Metodo para invocar un Web Service empleando Axis2 con Soap 1.1
	 * @param  direccionWS URL del servicio web
	 * @param  actionWS    Solo informativo, Metodo del servicio web a invocar 
	 * @param  mensaje     PayLoad del servicio web para invocar que incluye los niveles de SoapEnvelope su Body y Contenido
	 * @param  timeout     Tiempo de espera para la llamada
	 * @param  Options     Opciones del Service Client
	 * @param  asincrono true = sincrono, false = asincrono
	 * 
	 * @return respuesta OMElement con el mensaje de respuesta contenida en el Body
	 * @throws Exception Si ocurre error en la invocacion del servicio
	 */
	public static OMElement invocaWebServiceAxis2Soap11(String direccionWS, String actionWS, OMElement mensaje, Long timeout, Options options, boolean asincrono) throws Exception {
		
		logger.debug("Inicia Invoca Servicio Axis2 ["+direccionWS+" Metodo: "+actionWS+"]");
		logger.debug(" ***** Parametros Servicio ***** ");
		logger.debug(" Direccion = " + direccionWS);
		logger.debug(" Action    = " + actionWS);
		logger.debug(" Mensaje   = " + mensaje);
		logger.debug(" Timeout   = " + timeout);
		logger.debug(" Asincrono = " + asincrono);
		
		OMElement respuesta = null;
		ServiceClient serviceClient = null;
		
		try{
			/**
			 * Si las opciones que envian son nulas se les asigna unas por default
			 */
			if(options == null ){
				options =  new Options();
			}
			if(timeout != null){
				options.setTimeOutInMilliSeconds(timeout);
			}
			options.setTo(new EndpointReference(direccionWS));
			
			serviceClient = new ServiceClient();
			serviceClient.setOptions(options);
			
			/**
			 * Se rea un SOAPEnvelope con Soap 11 el cual se le fija el body con el xml que viene de la BD, el xml de la BD es un SOAPEnvelope por lo que 
			 * siempre debe de traer un header y body, por ellos se obtiene doble getFirstElement el cual da como resultado el contenido del Body
			 */
			SOAPEnvelope messageEnvelop = OMAbstractFactory.getSOAP11Factory().getDefaultEnvelope();
			messageEnvelop.getBody().addChild(mensaje.getFirstElement().getFirstElement());
			//logger.debug("MESSAGE ENVELOPE GENERADO: " + messageEnvelop.toString());
			MessageContext messageCtx = new MessageContext(); 
			messageCtx.setEnvelope(messageEnvelop);
			
			OperationClient callerOp = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
			callerOp.addMessageContext(messageCtx);
			
			callerOp.execute(!asincrono);
			
			/**invoke service*/
			if(!asincrono) {
				MessageContext resultMessage = callerOp.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
				respuesta = resultMessage.getEnvelope().getBody().getFirstElement().cloneOMElement();
				
				logger.debug("Respuesta Inmediata antes cleanUp de WS: " + respuesta);
			} 
		}catch(Exception e){
			throw e;
		}finally{
			logger.debug("Haciendo cleanupTransport para serviceClient: " + serviceClient);
			if(serviceClient != null ){
				serviceClient.cleanupTransport();
			}
		}
		
		logger.debug(" Respuesta invocaServicio " +  actionWS + " = " + respuesta);
		
		return respuesta;
	}
	
	
	/**
	 * Metodo que formatea un xml en linea a un xml con formato identado
	 * @param xml
	 * @return
	 */
	public static String formatXml(String xml){
		String xmlString = null;
        try{
        	
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            
            Transformer serializer= SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            StringWriter stringWtr = new StringWriter();
            Source source = new DOMSource(document);
            StreamResult result = new StreamResult(stringWtr);
            
            serializer.transform(source, result);
            xmlString = stringWtr.toString();
            
        }catch(Exception e){
            logger.error(e);
        }
		return xmlString;
    }
	
	
	public static void main(String[] args) {
		System.out.println(formatXml("<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns1:clienteSaludGS xmlns:ns1=\"http://ws.ice2sigs.gs.com/\"><arg0>1</arg0><arg1><agrupaCli>0</agrupaCli><apellidomCli>BARRUETA</apellidomCli><apellidopCli>ALONSO</apellidopCli><calleCli>PRUEBAS</calleCli><canconCli>0</canconCli><celularCli> </celularCli><cheqdevCli>0</cheqdevCli><claveCli>517990</claveCli><codcarCli>0</codcarCli><codposCli>3610</codposCli><coloniaCli>AMERICAS UNIDAS</coloniaCli><edocarCli>0</edocarCli><edocivilCli>2</edocivilCli><estadoCli>9</estadoCli><faxCli> </faxCli><fecaltaCli>2014-04-23T16:19:06.059-05:00</fecaltaCli><fecnacCli>1969-08-26T16:19:06.060-06:00</fecnacCli><fismorCli>1</fismorCli><giroCli>0</giroCli><municipioCli>BENITO JUAREZ</municipioCli><nombreCli>ALEJANDRO</nombreCli><numeroCli>44 INT </numeroCli><poblacionCli>BENITO JUAREZ</poblacionCli><rfcCli>AOBA690826</rfcCli><rmdbRn>0</rmdbRn><sexoCli>1</sexoCli><sinocurCli>0</sinocurCli><sucursalCli>1002</sucursalCli></arg1></ns1:clienteSaludGS></soapenv:Body></soapenv:Envelope>"));
	}
	
    
}