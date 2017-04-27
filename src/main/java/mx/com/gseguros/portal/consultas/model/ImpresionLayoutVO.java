package mx.com.gseguros.portal.consultas.model;

import java.util.List;
import java.util.Map;

public class ImpresionLayoutVO {
	private List<Map<String,String>> validacion;
	private Map<String,String> documentos;
	public List<Map<String, String>> getValidacion() {
		return validacion;
	}
	public void setValidacion(List<Map<String, String>> validacion) {
		this.validacion = validacion;
	}
	public Map<String, String> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Map<String, String> documentos) {
		this.documentos = documentos;
	}
	
	
}
