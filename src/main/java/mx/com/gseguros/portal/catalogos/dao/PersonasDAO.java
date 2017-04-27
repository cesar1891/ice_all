package mx.com.gseguros.portal.catalogos.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.general.model.ComponenteVO;

public interface PersonasDAO
{
	/**
	 * Obtiene personas por RFC de PKG_CONSULTA.P_GET_MPERSONA
	 */
	public List<Map<String,String>>obtenerPersonasPorRFC(String rfc, String nombre, String nombre2, String apat, String amat,
	        String validaTienePoliza) throws Exception;
	
	@Deprecated
	public Map<String,String>obtenerPersonaPorCdperson(Map<String,String>params) throws Exception;
	
	/**
	 * Movimiento mpersona
	 * @param cdperson
	 * @param cdtipide
	 * @param cdideper
	 * @param dsnombre
	 * @param cdtipper
	 * @param otfisjur
	 * @param otsexo
	 * @param fenacimi
	 * @param cdrfc
	 * @param dsemail
	 * @param dsnombre1
	 * @param dsapellido
	 * @param dsapellido1
	 * @param feingreso
	 * @param cdnacion
	 * @param canaling
	 * @param conducto
	 * @param ptcumupr
	 * @param residencia
	 * @param nongrata
	 * @param cdideext
	 * @param cdestcivil
	 * @param cdsucemi
	 * @param usuarioExternoInterno Numero Entero
	 * @param accion
	 * @throws Exception
	 */
	public void movimientosMpersona(String cdperson
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
			,String usuarioExternoInterno
			,String accion) throws Exception;

	/**
	 * Actualiza factores para determinar campos de Personas en Articulo 140
	 */
	public void actualizaFactoresArt140(String cdperson, String cdnacion, String otfisjur,String residencia, String ptcumupr) throws Exception;
	
	/**
	 * Obtiene el Domicilio del contratante de una poliza
	 * @param params
	 * @param timestamp
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> obtenerDomicilioContratante(Map<String, String> params) throws Exception;
	
	/**
	 * obtener domicilio por cdperson desde PKG_CONSULTA.P_GET_MDOMICIL
	 */
	public Map<String,String> obtenerDomicilioPorCdperson(String cdperson, String nmorddom) throws Exception;

	public List<Map<String,String>> obtenerDomiciliosPorCdperson(String cdperson) throws Exception;
	
	/**
	 * Obtener nuevo cdperson permanente
	 */
	public String obtenerNuevoCdperson() throws Exception;

	public String validaExisteRFC(String cdrfc) throws Exception;
	
	/**
	 * Guarda mdomicil
	 * @param cdperson
	 * @param nmorddom
	 * @param dsdomici
	 * @param nmtelefo
	 * @param cdpostal
	 * @param cdedo
	 * @param cdmunici
	 * @param cdcoloni
	 * @param nmnumero
	 * @param nmnumint
	 * @param cdtipdom
	 * @param usuarioExternoInterno Numero Entero
	 * @param swactivo
	 * @param accion
	 * @throws Exception
	 */
	public void movimientosMdomicil(String cdperson
			,String nmorddom
			,String dsdomici
			,String nmtelefo
			,String cdpostal
			,String cdedo
			,String cdmunici
			,String cdcoloni
			,String nmnumero
			,String nmnumint
			,String cdtipdom
			,String usuarioExternoInterno
			,String swactivo
			,String accion) throws Exception;


	/**
	 * Obtener los componentes de tatriper por cdrol y cdperson de PKG_LISTAS.P_GET_ATRI_PER
	 */
	public List<ComponenteVO> obtenerAtributosPersona(String cdperson, String cdrol) throws Exception;
	/**
	 * Obtiene los valores de tvaloper para un cdperson y un cdrol de PKG_CONSULTA.P_GET_TVALOPER
	 */
	public Map<String,String> obtenerTvaloper(String cdrol,String cdperson) throws Exception;
	/**
	 * Movimientos de tvaloper por cdperson de PKG_SATELITES.P_MOV_TVALOPER_NUEVO
	 */
	public void movimientosTvaloper(String cdrol,String cdperson
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05
			,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15
			,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25
			,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35
			,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45
			,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50,
			String otvalor51, String otvalor52
			)throws Exception;
	
	/**
	 * Movimientos de tvaloper por cdperson de PKG_SATELITES.P_MOV_TVALOPER
	 */
	public void insertaTvaloper(
			String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
			String nmsuplem, String status, String cdrol, String cdperson, String cdatribu, String cdtipsit,
			String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05,
			String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10,
			String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15,
			String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20,
			String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25,
			String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30,
			String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35,
			String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40,
			String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45,
			String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50,
			String otvalor51, String otvalor52
			) throws Exception;
	
	public List<Map<String,String>>cargarDocumentosPersona(Map<String,String> params)throws Exception;
	
	public void validarDocumentosPersona(Map<String,String> params)throws Exception;
	
	public String cargarNombreDocumentoPersona(Map<String,String>params)throws Exception;

	public String guardaAccionista(Map<String,String>params)throws Exception;

	public String eliminaAccionistas(Map<String,String>params)throws Exception;

	public String actualizaStatusPersona(Map<String,String>params)throws Exception;
	
	public List<Map<String,String>> obtieneAccionistas(Map<String, String> params)throws Exception;
	
	public Map<String,String> obtieneMunicipioYcolonia(Map<String, String> params) throws Exception;

	public void actualizaCodigoExterno(Map<String, String> params) throws Exception;
	
	public Map<String,String>cargarPersonaPorCdperson(String cdperson)throws Exception;
	
	public Map<String,String> recuperarEspPersona(String cdperson) throws Exception;
	
	/**
	 * Obtiene un nuevo cdperson temporal
	 * @return
	 * @throws Exception
	 */
	public String obtieneCdperson() throws Exception;
	
	public void actualizaCdideper(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsuplem, String cdideper) throws Exception;
	
	public String guardarConfiguracionClientes(Map<String, Object> paramsCliente) throws Exception;
	
	public String actualizaClienteClientexTipo(String rfc, String activaCliente, String tipCliente) throws Exception;

	public List<Map<String, String>> obtieneListaClientesxTipo(String rfc, String proceso) throws Exception;

	public List<GenericVO> consultaClientes(String cdperson) throws Exception;

	public String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception;
	
	public String validaExisteAseguradoSicaps(String cdideper)throws Exception;
	
	public List<Map<String, String>> obtieneConfPatallaCli(String cdperson, String usuario, String rol, String tipoCliente) throws Exception;
	
	/**
	 * ACTUALIZA TVALOSIT SI EXISTE FECHA DE NACIMIENTO, EDAD Y/O SEXO EN SU ATRIBUTOS
	 */
	public void sincronizaPersonaToValosit(
			String sexo
			,Date fenacimi
			,String cdtipsit
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception;

    public String obtieneAseguradoSICAPS(String nombres, String apellidoP, String apellidoM, Date fechaNac) throws Exception;
}