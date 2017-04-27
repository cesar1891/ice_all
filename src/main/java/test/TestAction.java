package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;
import mx.com.gseguros.ws.autosgs.emision.model.EmisionAutosVO;
import mx.com.gseguros.ws.folioserviciopublico.client.jaxws.FolioWS;
import mx.com.gseguros.ws.folioserviciopublico.client.jaxws.FolioWSService;
import mx.com.gseguros.ws.folioserviciopublico.client.jaxws.RequestFolio;
import mx.com.gseguros.ws.folioserviciopublico.client.jaxws.ResponseFolio;

import org.apache.log4j.Logger;
//import org.exolab.castor.types.Date;
import org.springframework.beans.factory.annotation.Autowired;


/*
@Namespace("/test")
@ResultPath(value="/")
*/
public class TestAction extends PrincipalCoreAction {

	private static final long serialVersionUID = -3861435458381281429L;
	
	private static Logger logger = Logger.getLogger(TestAction.class);

//	@Autowired
//	private transient EmisionAutosService emisionAutosService;
	
	private Map<String, String> params;
	
	@Autowired
	private KernelManagerSustituto kernelManager;
	
	private EmisionAutosVO emisionAutos;
	
	private ResponseFolio responseFolio;
	
	@Autowired
	private PantallasManager pantallasManager;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private AutosSIGSDAO autosDAOSIGS;
	
	private List<ComponenteVO> items;
	
	/*
	@Action(value="invocaServicioCotizacionAutos",
		results={@Result(name="success", type="json")}
	)
	*/
	public String invocaServicioCotizacionAutos() throws Exception {
		/*
		items = pantallasManager.obtenerComponentes(null, null, null, null, null, null, "ENDOSO_COBERTURA", "GRID_INCISOS", null);
		*/
		/*
		//cargar anterior valosit
		Map<String,String>paramsValositAsegurado=new LinkedHashMap<String,String>(0);
		paramsValositAsegurado.put("pv_cdunieco_i" , params.get("cdunieco"));
		paramsValositAsegurado.put("pv_cdramo_i"   , params.get("cdramo"));
		paramsValositAsegurado.put("pv_estado_i"   , params.get("estado"));
		paramsValositAsegurado.put("pv_nmpoliza_i" , params.get("nmpoliza"));
		paramsValositAsegurado.put("pv_nmsituac_i" , params.get("nmsituac"));
		Map<String,Object> valositAsegurado = kernelManager.obtieneValositSituac(paramsValositAsegurado);
		logger.debug("valosit anterior: " + valositAsegurado);
		*/
		/*
		mesaControlDAO.guardarDocumento(cdunieco, cdramo, estado, nmpoliza, nmsuplem, feinici, cddocume, dsdocume, nmsolici, ntramite, tipmov, swvisible, codidocu, cdtiptra);
		*/
		return SUCCESS;
	}
	
	
	public String invocaServiciosFolioAutos() throws Exception {
		
		logger.debug("params=" + params);
		System.out.println("params==" + params);
		
		FolioWSService service = null;
        try {
            // Creamos el servicio con el WSDL:
            URL wsdlLocation = new URL("http://10.1.1.134:8000/folioserviciopublico-ws/servicios?wsdl");
            String targetNamespace="http://com.gs.folioserviciopublico.soap.folio";
            String name="FolioWSService";
            service = new FolioWSService( wsdlLocation, new QName(targetNamespace, name));
            FolioWS port = service.getFolioWSPort();
            
            RequestFolio req = new RequestFolio();
            req.setNumFolio( Integer.parseInt(params.get("numfolio")) ); // 984452
            req.setSucursalAdmin( Integer.parseInt(params.get("sucursaladmin")) ); //120 
            
            // Invocamos el WS:
            responseFolio = port.validarFolio(req);
            
        } catch (MalformedURLException e ) {
            e.printStackTrace();
        }
		
		return SUCCESS;
	}

	
	//Getters and setters:
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public EmisionAutosVO getEmisionAutos() {
		return emisionAutos;
	}

	public void setEmisionAutos(EmisionAutosVO emisionAutos) {
		this.emisionAutos = emisionAutos;
	}

	public ResponseFolio getResponseFolio() {
		return responseFolio;
	}

	public void setResponseFolio(ResponseFolio responseFolio) {
		this.responseFolio = responseFolio;
	}

	public List<ComponenteVO> getItems() {
		return items;
	}

	public void setItems(List<ComponenteVO> items) {
		this.items = items;
	}
	
}