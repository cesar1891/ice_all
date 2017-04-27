package mx.com.gseguros.portal.reexpedicionDocumentos.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.utils.HttpUtil;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ReexpedicionDocumentosAction extends PrincipalCoreAction
{
	private static final long serialVersionUID = 5205183585188359309L;

	private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ReexpedicionDocumentosAction.class);
	
	private ConsultasManager              consultasManager;
	private PantallasManager              pantallasManager;
	@Autowired
	private EndososManager                endososManager;
	
	private Map<String,Item>              itemMap;
	private LinkedHashMap<String,Object>  linkedObjectMap;
	private String                        mensaje;
	private List<Map<String,String>>      stringList;
	private Map<String,String>            stringMap;
	private boolean                       success;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
    
    @Value("${pass.servidor.reports}")
    private String passServidorReports;	
    
    @Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
    
	public String principal()
	{
		logger.info(""
				+ "\n#########################################"
				+ "\n###### marcoReexpedicionDocumentos ######"
				);
		try
		{
			UserVO usuario     = (UserVO)session.get("USUARIO");
			String cdsisrol    = usuario.getRolActivo().getClave();
			String pantalla    = "MARCO_REEXPEDICION_DOCUMENTOS";
			String seccion     = "FORMULARIO";
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			List<ComponenteVO> listaComponentesFormulario = pantallasManager.obtenerComponentes(null, null, null, null, null, null, pantalla, seccion, null);
			gc.generaComponentes(listaComponentesFormulario, true, false, true, false, false, false);
			itemMap = new HashMap<String,Item>();
			itemMap.put("itemsDeFormulario",gc.getItems());
			seccion = "MODELO_POLIZA";
			List<ComponenteVO> listaComponentesModeloPoliza = pantallasManager.obtenerComponentes(null, null, null, null, null, null, pantalla, seccion, null);
			gc.generaComponentes(listaComponentesModeloPoliza, true, true, false, true, false, false);
			itemMap.put("fieldsModeloPoliza",gc.getFields());
			itemMap.put("columnasGridPolizas",gc.getColumns());
			seccion = "BOTONES_GRID";
			List<ComponenteVO> listaBotonesGrid = pantallasManager.obtenerComponentes(null, null, null, null, null, cdsisrol, pantalla, seccion, null);
			gc.generaComponentes(listaBotonesGrid, true, false, false, false, false, true);
			itemMap.put("botonesGridPolizas",gc.getButtons());
		}
		catch(Exception ex)
		{
			logger.error("error al cargar marco de reexpedicion de documentos",ex);
		}
		logger.info(""
				+ "\n###### marcoReexpedicionDocumentos ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	public String regeneraMedicinaPreventiva()
	{
		logger.info(""
				+ "\n########################################"
				+ "\n###### regeneraMedicinaPreventiva ######"
				);
		logger.info("stringMap: "+stringMap);
		try
		{
			String ntramite = stringMap.get("NTRAMITE");
			String cdunieco = stringMap.get("CDUNIECO");
			String cdramo   = stringMap.get("CDRAMO");
			String estado   = stringMap.get("ESTADO");
			String nmpoliza = stringMap.get("NMPOLIZA");
			String cdmoddoc = "1314";
			//String nmsuplem = stringMap.get("NMSUPLEM");

			// Reimprimimos todos los Documentos de Endosos de Medicina preventiva de una poliza:
			String [] paginas = {"P_1_INDIS", "P_2_INDIS"};
			List<Map<String, String>> listaDocsEndosos = endososManager.obtenerListaDocumentosEndosos(
					new PolizaVO(cdunieco,cdramo, estado, nmpoliza),cdmoddoc);
			logger.debug("listaDocsEndosos=" + listaDocsEndosos);
			for(Map<String,String> sMapDoc : listaDocsEndosos) {
				
				String nmsuplem = sMapDoc.get("NMSUPLEM");
				String cdtipsup = sMapDoc.get("CDTIPSUP");
				
				for (String pag : paginas) {
					String nombrePDF = pag;
					// Si el documento no es un endoso de EMISION, agregamos el nmsumplem al nombre:
					if( cdtipsup != null
							&&Integer.parseInt(cdtipsup)!=TipoEndoso.EMISION_POLIZA.getCdTipSup()
							&&Integer.parseInt(cdtipsup)!=TipoEndoso.RENOVACION.getCdTipSup()
							)
					{
						nombrePDF = nombrePDF += "_"+ nmsuplem;
					}
					String url = new StringBuilder()
						.append(rutaServidorReports)
						.append("?destype=cache")
						.append("&desformat=PDF")
						.append("&userid=").append(this.passServidorReports)
						.append("&report=").append(pag).append(".rdf")
						.append("&paramform=no")
						.append("&ACCESSIBLE=YES")
						.append("&p_unieco=").append(cdunieco)
						.append("&p_ramo=").append(cdramo)
						.append("&p_estado='M'")
						.append("&p_poliza=").append(nmpoliza)
						.append("&p_suplem=").append(nmsuplem)
						.toString();
					HttpUtil.generaArchivo(url, new StringBuilder()
							.append(this.rutaDocumentosPoliza).append("/").append(ntramite).append("/")
							.append(nombrePDF).append(".pdf").toString());
				}
			}
			
			mensaje = "Documento de medicina preventiva reexpedido";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al regenerar medicina preventiva",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### regeneraMedicinaPreventiva ######"
				+ "\n########################################"
				);
		return SUCCESS;
	}
	
	public String regeneraMedicinaPreventivaEspecialista()
	{
		logger.info(""
				+ "\n####################################################"
				+ "\n###### regeneraMedicinaPreventivaEspecialista ######"
				);
		logger.info("stringMap: "+stringMap);
		try
		{
			String ntramite = stringMap.get("NTRAMITE");
			String cdunieco = stringMap.get("CDUNIECO");
			String cdramo   = stringMap.get("CDRAMO");
			String estado   = stringMap.get("ESTADO");
			String nmpoliza = stringMap.get("NMPOLIZA");
			String cdmoddoc = "1516";
			// Reimprimimos todos los Documentos de Endosos de Medicina preventiva de Especialista de una poliza:
			String [] paginas = {"P_1_ESPE", "P_2_ESPE"};
			List<Map<String, String>> listaDocsEndosos = endososManager.obtenerListaDocumentosEndosos(
					new PolizaVO(cdunieco,cdramo, estado, nmpoliza),cdmoddoc);
			logger.debug("listaDocsEndosos=" + listaDocsEndosos);
			for(Map<String,String> sMapDoc : listaDocsEndosos) {
				
				String nmsuplem = sMapDoc.get("NMSUPLEM");
				String cdtipsup = sMapDoc.get("CDTIPSUP");
				
				for (String pag : paginas) {
					String nombrePDF = pag;
					// Si el documento no es un endoso de EMISION, agregamos el nmsumplem al nombre:
					if( cdtipsup != null
							&&Integer.parseInt(cdtipsup)!=TipoEndoso.EMISION_POLIZA.getCdTipSup()
							&&Integer.parseInt(cdtipsup)!=TipoEndoso.RENOVACION.getCdTipSup()
							)
					{
						nombrePDF = nombrePDF += "_"+ nmsuplem;
					}
					String url = new StringBuilder()
						.append(rutaServidorReports)
						.append("?destype=cache")
						.append("&desformat=PDF")
						.append("&userid=").append(this.passServidorReports)
						.append("&report=").append(pag).append(".rdf")
						.append("&paramform=no")
						.append("&ACCESSIBLE=YES")
						.append("&p_unieco=").append(cdunieco)
						.append("&p_ramo=").append(cdramo)
						.append("&p_estado='M'")
						.append("&p_poliza=").append(nmpoliza)
						.append("&p_suplem=").append(nmsuplem)
						.toString();
					HttpUtil.generaArchivo(url, new StringBuilder()
							.append(this.rutaDocumentosPoliza).append("/").append(ntramite).append("/")
							.append(nombrePDF).append(".pdf").toString());
				}
			}
			
			mensaje = "Documento de medicina preventiva expecialista reexpedido";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al regenerar medicina preventiva",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### regeneraMedicinaPreventivaEspecialista ######"
				+ "\n####################################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDatosEmail()
	{
		logger.info(""
				+ "\n###############################"
				+ "\n###### obtenerDatosEmail ######"
				);
		logger.info("stringMap: "+stringMap);
		try
		{
			String direccionIPLocal  = ServletActionContext.getRequest().getLocalAddr();
			int    puertoLocal       = ServletActionContext.getRequest().getLocalPort();
			String contexto          = ServletActionContext.getServletContext().getServletContextName();
			String urlReporte        = "http://"+direccionIPLocal+":"+puertoLocal+"/"+contexto+"/reportes/procesoObtencionReporte.action";
			//stringMap=new HashMap<String,String>();
			stringMap.put("url",urlReporte);
			
			LinkedHashMap<String,Object> parametros = new LinkedHashMap<String,Object>();
			parametros.put("param1","1");//el id del proceso que recibe el PL para regresar los correos a cuales se debe enviar
			parametros.put("param2",stringMap.get("cdunieco"));
			List<Map<String,String>> listaEmails = consultasManager.consultaDinamica(ObjetoBD.OBTIENE_EMAIL, parametros);
			String correos = "";
			if(listaEmails!=null)
			{
				boolean primero=true;
				for(Map<String,String>email:listaEmails)
				{
					if(primero)
					{
						correos=email.get("DESCRIPL");
						primero=false;
					}	
					else
					{
						correos=correos+";"+email.get("DESCRIPL");
					}
				}
			}
			stringMap.put("correos",correos);
			success = true;
			mensaje = "Correo(s) obtenido(s)";
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los datos de email de recibos subsecuentes",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDatosEmail ######"
				+ "\n###############################"
				);
		return SUCCESS;
	}

	public Map<String, Item> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Item> itemMap) {
		this.itemMap = itemMap;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<Map<String, String>> getStringList() {
		return stringList;
	}

	public void setStringList(List<Map<String, String>> stringList) {
		this.stringList = stringList;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public void setConsultasManager(ConsultasManager consultasManager) {
		this.consultasManager = consultasManager;
	}

	public LinkedHashMap<String, Object> getLinkedObjectMap() {
		return linkedObjectMap;
	}

	public void setLinkedObjectMap(LinkedHashMap<String, Object> linkedObjectMap) {
		this.linkedObjectMap = linkedObjectMap;
	}
	
    public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getRutaDocumentosPoliza() {
		return rutaDocumentosPoliza;
	}

}