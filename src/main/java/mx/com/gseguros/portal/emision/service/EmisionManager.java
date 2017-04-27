package mx.com.gseguros.portal.emision.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;

public interface EmisionManager
{
	public void setSession(Map<String,Object>session);
	
	public ManagerRespuestaImapVO construirPantallaClausulasPoliza() throws Exception;
	
	public ManagerRespuestaVoidVO guardarClausulasPoliza(List<Map<String,String>>clausulas);
	
	/*
	@Deprecated
	public String insercionDocumentosParametrizados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String proceso
			)throws Exception;
	*/

	public void getActualizaCuadroComision(Map<String, Object> paramsPoliage)throws Exception;
	
	@Deprecated
	public void actualizaNmsituaextMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac 
			,String nmsuplem
			,String nmsituaext
			)throws Exception;
	
	@Deprecated
	public void actualizaDatosMpersona(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdestciv
			,String ocup
			)throws Exception;
	
	@Deprecated
	public void validarDocumentoTramite (String ntramite, String cddocume) throws Exception;
	
	@Deprecated
	public String recuperarTramiteCotizacion (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
	
	public boolean revierteEmision(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem);
	
	public String generarLigasDocumentosEmisionLocalesIce (String ntramite) throws Exception;

	/**
	 * 
	 * @param excel nombre del archivo que se va a validar
	 * @return
	 * @throws Exception
	 */
	public ManagerRespuestaSlistVO procesarCargaMasivaRecupera(File excel)throws Exception;

	/**
	 * Invoca servicio que genera una poliza de Recupera Individual
	 * 
	 * @param cdperson codigo de la persona
	 * @param sucursal 
	 * @param poliza poliza proporcionada por el cliente
	 * @param nombre1 primer nombre del asegurado 
	 * @param nombre2 segundo nombre del asegurado
	 * @param apePat apellido paterno del asegurado
	 * @param apeMat apellido materno del asegurado
	 * @param producto identificador del producto
	 * @param cve_plan numero del plan
	 * @param esq_suma_ase esquema de suma asegurada
	 * @param parentesco parentesco con el aasegurado
	 * @param f_nacimiento fecha de nacimiento del asegurado
	 * @param rFC rfc del asegurado
	 * @param sexo sexo del asegurado
	 * @param peso peso del asegurado
	 * @param estatura estatura del asegurado
	 * @param fecinivig  fecha de inicio de vigencia de la poliza
	 * @param membresia 
	 * @return
	 */
	public String generarPoliza(String cdperson, String sucursal, String poliza, String nombre1, String nombre2,
			String apePat, String apeMat, String producto, String cve_plan, String esq_suma_ase, String parentesco,
			String f_nacimiento, String rFC, String sexo, String peso, String estatura, String fecinivig,
			String membresia);

	/**
	 * 
	 * @param fecha
	 * @param nombre
	 * @param polizas
	 * @param rango
	 * @param usuario
	 * @throws Exception
	 */
	public void insertaBitacora(Date fecha, String nombre, int polizas, String rango, String usuario) throws Exception;
	
}