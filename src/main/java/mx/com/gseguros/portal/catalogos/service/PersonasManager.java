package mx.com.gseguros.portal.catalogos.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.Item;

public interface PersonasManager
{
	/**
	 * Carga los componentes de la pantalla de personas
	 * @return exito,respuesta,respuestaOculta,itemMap
	 */
	public Map<String,Object> pantallaPersonas(String cdsisrol,long timestamp) throws Exception;
	
	/**
	 * Obtiene el Domicilio del contratante de una poliza
	 * @param params
	 * @param timestamp
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> obtenerDomicilioContratante(Map<String, String> params,long timestamp) throws Exception;

	public Map<String,String> obtieneMunicipioYcolonia(Map<String, String> params) throws Exception;

	public void actualizaCodigoExterno(Map<String, String> params) throws Exception;
	
	/**
	 * Buscar personas por RFC de PKG_CONSULTA.P_GET_MPERSONA
	 * @return exito,respuesta,respuestaOculta,listaPersonas
	 */
	public Map<String,Object> obtenerPersonasPorRFC(String rfc,String nombre,String snombre,String apat,String amat, String validaTienePoliza, long timestamp) throws Exception;
	
	public Map<String,Object> obtenerPersonaPorCdperson(String cdperson,long timestamp) throws Exception;
	
	/**
	 * Guardar pantalla de personas
	 * @return exito,respuesta,respuestaOculta,cdpersonNuevo
	 */
	public Map<String,Object> guardarPantallaPersonas(String cdperson
			,String cdtipide
			,String cdideper
			,String dsnombre
			,String cdtipper
			,String otfisjur
			,String otsexo
			,Date   fenacimi
			,String cdrfc
			,String dsemail
			,String dsnombre1
			,String dsapellido
			,String dsapellido1
			,Date   feingreso
			,String cdnacion
			,String canaling
			,String conducto
			,String ptcumupr
			,String residencia
			,String nongrata
			,String cdideext
			,String cdestcivil
			,String cdsucemi
			,String nmtelefo
			,List<Map<String,String>> saveList
			,List<Map<String,String>> updateList
			,List<Map<String,String>> deleteList
			,boolean autosave
			,UserVO usuario
			,long   timestamp) throws Exception;

	/**
	 * Guardar pantalla de domicilio asegurados
	 * @return 
	 */
	public void guardarPantallaDomicilio(String cdperson
			,String nmorddom
			,String dsdomici
			,String nmtelefo
			,String cdpostal
			,String cdedo
			,String cdmunici
			,String cdcoloni
			,String nmnumero
			,String nmnumint
			,UserVO usuario
			,String swactivo
			,String accion
			,long   timestamp) throws Exception;
	/**
	 * Obtener el domicilio de una persona por su cdperson de PKG_CONSULTA.P_GET_MDOMICIL
	 * @return exito,respuesta,respuestaOculta,domicilio
	 */
	public Map<String,Object> obtenerDomicilioPorCdperson(String cdperson,String nmorddom,long timestamp) throws Exception;

	public Map<String,Object> obtenerDomiciliosPorCdperson(String cdperson,long timestamp) throws Exception;
	/**
	 * Obtener los items de tatriper y los valores de tvaloper para un cdperson de PKG_LISTA.P_GET_ATRI_PER y PKG_CONSULTA.P_GET_TVALOPER
	 * @return exito,respuesta,respuestaOculta,itemsTatriper,fieldsTatriper,tvaloper
	 */
	public Map<String,Object> obtenerTatriperTvaloperPorCdperson(String cdperson,String cdrol,long timestamp) throws Exception;
	/**
	 * Guardar los datos de tvaloper por cdperson con PKG_CONSULTA.P_MOV_TVALOPER
	 * @return exito,respuesta,respuestaOculta
	 */
	public Map<String,Object> guardarDatosTvaloper(String cdperson, String cdrol
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05
			,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15
			,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25
			,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35
			,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45
			,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50
			,String otvalor51, String otvalor52
			,long timestamp
			) throws Exception;
	public List<Map<String,String>>cargarDocumentosPersona(String cdperson)throws Exception;
	
	public String cargarNombreDocumentoPersona(String cdperson,String codidocu)throws Exception;

	public List<Map<String,String>> obtieneAccionistas(Map<String, String> params)throws Exception;
	
	public String guardaAccionista(Map<String, String> params)throws Exception;

	public String eliminaAccionistas(Map<String, String> params)throws Exception;

	public String actualizaStatusPersona(Map<String, String> params)throws Exception;
	
	public Map<String,Item> pantallaBeneficiarios(String cdunieco,String cdramo,String estado,String cdsisrol,String cdtipsup)throws Exception;
	
	public void setSession(Map<String,Object>session);
	
	public Map<String,Item> pantallaPersona(String origen, String cdsisrol, String context) throws Exception;
	
	public String guardarPantallaEspPersona(Map<String,String>params, UserVO usuario) throws Exception;
	
	public Map<String,String>recuperarEspPersona(String cdperson) throws Exception;
	
	public String guardarConfiguracionClientes(String rfc, String status, String tipoPersona, String cveAgente,
			String nombreCompleto, String domicilio, String observaciones,String proceso, String cduser, Date fechaProcesamiento,
			String accion) throws Exception;

	public List<Map<String, String>> obtieneListaClientesxTipo(String rfc, String proceso) throws Exception;

	public List<GenericVO> consultaClientes(String cdperson) throws Exception;
	
	public String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception;
	
	public String validaExisteAseguradoSicaps(String cdideper)throws Exception;

	public Integer obtieneTipoCliWS(String codigoExterno, String compania) throws Exception;

	public List<Map<String, String>> obtieneConfPatallaCli(String cdperson, String usuario, String rol, String tipoCliente) throws Exception;
	
	public void guardarBeneficiarios(String cdunieco, String cdramo, String estado, String nmpoliza,
            String usuarioCaptura, List<Map<String, String>> beneficiarios) throws Exception;

    public String obtieneAseguradoSICAPS(String nombres, String apellidoP, String apellidoM, Date fechaNac) throws Exception;
}