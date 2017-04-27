package mx.com.gseguros.portal.consultas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosSiniestrosVO;
import mx.com.gseguros.portal.consultas.service.ConsultasSiniestroManager;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;

/**
 *
 * @author Jose Garcia
 */
public class ConsultasSiniestroAction extends PrincipalCoreAction{

	private static final long serialVersionUID = -6321288906841302337L;
	private org.apache.log4j.Logger logger =org.apache.log4j.Logger.getLogger(ConsultasSiniestroAction.class);

	/**
	* Success property
	*/
	private boolean success;

	private SiniestrosManager siniestrosManager;
	private ConsultasSiniestroManager consultaSiniestrosManager;
	private List<ConsultaDatosSiniestrosVO> datosSiniestroAsegurado;
	private List<ConsultaDatosSiniestrosVO> datosFacturaPagoDirecto;
	private HashMap<String, String> params;
	private List<Map<String, String>> siniestro;

	public String detalleSiniestrosInicial() throws Exception{
		logger.debug(""
				+ "\n####################################"
				+ "\n####################################"
				+ "\n###### DetalleSiniestroAction ######"
				+ "\n######                        ######"
				);
		logger.debug("params:"+params);
		try{
			String ntramite = params.get("ntramite");
			Map<String,String> paramsRes = (HashMap<String, String>) siniestrosManager.obtenerLlaveSiniestroReembolso(ntramite);
			
			for(Entry<String,String>en:paramsRes.entrySet()){
				params.put(en.getKey().toLowerCase(),en.getValue());
			}
			
		}catch(Exception ex){
			logger.error("error al obtener clave de siniestro para la pantalla del tabed panel",ex);
		}
		logger.debug("params obtenidos:"+params);
		logger.debug(""
				+ "\n######                        ######"
				+ "\n###### DetalleSiniestroAction ######"
				+ "\n####################################"
				+ "\n####################################"
				);
		success = true;
		return SUCCESS;
	}
	
	
	public String consultaAseguradosPagoReembolso(){
		logger.debug(" **** Entrando a Consulta de Asegurados Pago por reembolso ****");
		try {
				List<ConsultaDatosSiniestrosVO> lista = consultaSiniestrosManager.getConsultaAseguradosPagoReembolso(params.get("cdperson"));
				logger.debug(lista);
				if(lista!=null && !lista.isEmpty())	datosSiniestroAsegurado = lista;
		}catch( Exception e){
			logger.error("Error al obtener los datos de Consulta de Asegurados Pago por reembolso",e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}
	
	public String consultaFacturasPagoDirecto(){
		logger.debug(" **** Entrando a Consulta de Asegurados Pago por reembolso ****");
		try {
			List<ConsultaDatosSiniestrosVO> lista = consultaSiniestrosManager.getConsultaFacturasPagoDirecto(params.get("cdperson"),params.get("cdproveedor"),params.get("cdfactura"));
			logger.debug(lista);
			if(lista!=null && !lista.isEmpty())	datosFacturaPagoDirecto = lista;
		}catch( Exception e){
			logger.error("Error al obtener los datos de Consulta de Asegurados Pago por reembolso",e);
			return SUCCESS;
		}
		success = true;
		return SUCCESS;
	}

	public String execute() throws Exception {
    	return SUCCESS;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	/*public ConsultaDatosSiniestrosVO getDatosSiniestroAsegurado() {
		return datosSiniestroAsegurado;
	}

	public void setDatosSiniestroAsegurado(
			ConsultaDatosSiniestrosVO datosSiniestroAsegurado) {
		this.datosSiniestroAsegurado = datosSiniestroAsegurado;
	}*/

	public List<ConsultaDatosSiniestrosVO> getDatosSiniestroAsegurado() {
		return datosSiniestroAsegurado;
	}

	public void setDatosSiniestroAsegurado(
			List<ConsultaDatosSiniestrosVO> datosSiniestroAsegurado) {
		this.datosSiniestroAsegurado = datosSiniestroAsegurado;
	}

	public List<Map<String, String>> getSiniestro() {
		return siniestro;
	}

	public void setSiniestro(List<Map<String, String>> siniestro) {
		this.siniestro = siniestro;
	}

	public void setConsultaSiniestrosManager(
			ConsultasSiniestroManager consultaSiniestrosManager) {
		this.consultaSiniestrosManager = consultaSiniestrosManager;
	}

	public List<ConsultaDatosSiniestrosVO> getDatosFacturaPagoDirecto() {
		return datosFacturaPagoDirecto;
	}

	public void setDatosFacturaPagoDirecto(
			List<ConsultaDatosSiniestrosVO> datosFacturaPagoDirecto) {
		this.datosFacturaPagoDirecto = datosFacturaPagoDirecto;
	}

}