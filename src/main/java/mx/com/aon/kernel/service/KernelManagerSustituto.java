/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.aon.kernel.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.flujos.cotizacion.model.AyudaCoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.CoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.SituacionVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.general.model.ComponenteVO;

/**
 *
 * @author Jair
 */
public interface KernelManagerSustituto {
    public WrapperResultados calculaNumeroPoliza(String pv_cdunieco_i, String pv_cdramo_i, String pv_estado_i) throws ApplicationException;
    public WrapperResultados insertaMaestroPolizas(Map<String, String> parameters) throws ApplicationException;
    public WrapperResultados insertaPolisit(Map<String, Object> parameters) throws ApplicationException;
    public WrapperResultados insertaPolisit(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsituac
    		,String nmsuplem
    		,String status
    		,String cdtipsit
    		,String swreduci
    		,String cdagrupa
    		,String cdestado
    		,Date   fefecsit
    		,Date   fecharef
    		,String cdgrupo
    		,String nmsituaext
    		,String nmsitaux
    		,String nmsbsitext
    		,String cdplan
    		,String cdasegur
    		,String accion) throws ApplicationException;
    public WrapperResultados insertaValoresSituaciones(Map<String, String> parameters) throws ApplicationException;
    public WrapperResultados insertaValoresSituaciones(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsituac
    		,String nmsuplem
    		,String status
    		,String cdtipsit
    		,String accion
    		,Map<String, String> otvalorValosit) throws ApplicationException;
    public WrapperResultados actualizaValoresSituaciones(Map<String, String> parameters) throws ApplicationException;
    public List<SituacionVO> clonaSituaciones(Map<String,String> parameters) throws ApplicationException;
    public WrapperResultados coberturas(Map<String,String> parameters) throws ApplicationException;
    public WrapperResultados coberturasEnd(Map<String,String> parameters) throws ApplicationException;
    public WrapperResultados ejecutaASIGSVALIPOL_EMI(Map<String,String> parameters) throws ApplicationException;
    public WrapperResultados clonaPersonas(Map<String,Object> parameters) throws ApplicationException;
    public List<ResultadoCotizacionVO> obtenerResultadosCotizacion(Map<String,String> parameters) throws ApplicationException;
    public List<Map<String,String>>    obtenerResultadosCotizacion2(Map<String,String> params) throws ApplicationException;
    public List<CoberturaCotizacionVO> obtenerCoberturas(Map<String,String> parameters) throws ApplicationException;
    public AyudaCoberturaCotizacionVO obtenerAyudaCobertura(String idCobertura,String idRamo,String idCiaAsegurador) throws ApplicationException;
    public List<ComponenteVO> obtenerTatrisit(String cdtipsit,String cdusuari) throws ApplicationException;
    public List<ComponenteVO> obtenerTatrisin(String cdramo,String cdtipsit) throws ApplicationException;
    public List<ComponenteVO> obtenerTatrisinPoliza(String cdunieco,String cdramo,String estado,String nmpoliza) throws ApplicationException;
    public List<ComponenteVO> obtenerTatripol(String[] args) throws ApplicationException;
    public DatosUsuario obtenerDatosUsuario(String cdusuario,String cdtipsit) throws ApplicationException;
    public WrapperResultados movDetalleSuplemento(Map<String,Object> parameters) throws ApplicationException;
    public WrapperResultados comprarCotizacion(Map<String,String> parameters) throws ApplicationException;
    /**
     * PKG_SATELITES.P_GET_INFO_MPOLIZAS
     * @return cdusuari,cdunieco,dsunieco,cdperson,cdagente,
     * nombre,cdramo,dsramo,nmcuadro,cdtipsit,fesolici,nmsolici,feefecto,feproren,ottempot,cdperpag
     */
    public Map<String,Object> getInfoMpolizas(Map<String,Object>parameters) throws ApplicationException;
    /**
     * PKG_SATELITES.P_GET_INFO_MPOLIZAS
     * @return cdusuari,cdunieco,dsunieco,cdperson,cdagente,
     * nombre,cdramo,dsramo,nmcuadro,cdtipsit,fesolici,nmsolici,feefecto,feproren,ottempot,cdperpag
     */
    public Map<String,Object> getInfoMpolizas(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String cdusuari) throws ApplicationException;
    public List<GenericVO> getTmanteni(String tabla) throws ApplicationException;
	public List<Map<String, Object>> obtenerAsegurados(Map<String, String> map1) throws ApplicationException;
	public Map<String,Object> getInfoMpolizasCompleta(Map<String,String> parameters) throws ApplicationException;
	public WrapperResultados pMovTvalopol(Map<String, String> parameters) throws ApplicationException;
	public WrapperResultados pMovTvalogar(Map<String, String> parameters) throws ApplicationException;
	public WrapperResultados pMovTvaloper(Map<String, String> parameters) throws ApplicationException;
	//requiere de su propio catch si no hay datos:
	public Map<String,Object> pGetTvalopol(Map<String,String> parameters) throws ApplicationException;
	public String generaCdperson() throws ApplicationException;
	public WrapperResultados movMpersona(Map<String,Object> parameters) throws ApplicationException;
	public WrapperResultados movMpoliper(Map<String,Object> parameters) throws ApplicationException;
	public WrapperResultados borraMpoliper(Map<String,Object> parameters) throws ApplicationException;
	public List<Map<String,String>> obtenerCoberturasUsuario(Map<String,String> parametros) throws ApplicationException;
	public WrapperResultados movPoligar(Map<String, String> param) throws ApplicationException;
	public WrapperResultados movPolicap(Map<String, String> param) throws ApplicationException;
	public List<Map<String, String>> obtenerDetallesCotizacion(Map<String, String> params) throws ApplicationException;
    @Deprecated
	public List<ComponenteVO> obtenerTatrigar(Map<String, String> smap1) throws ApplicationException;
	//requiere de su propio catch si no hay datos:
	public Map<String, Object> obtenerValoresTatrigar(Map<String, String> param) throws ApplicationException;
	public List<ComponenteVO> obtenerTatriper(Map<String, String> smap1) throws ApplicationException;
	//requiere de su propio catch si no hay datos:
	public Map<String, Object> obtenerValoresTatriper(Map<String, String> smap1) throws ApplicationException;
	public Map<String, String> obtenerDomicilio(Map<String, String> params) throws ApplicationException;
	public Map<String, String> obtenerDomicilioGeneral(Map<String, String> params) throws ApplicationException;
	/**
	 * PKG_SATELITES.P_MOV_MDOMICIL
	 */
	public WrapperResultados pMovMdomicil(Map<String, String> paramDomicil) throws ApplicationException;
	public WrapperResultados emitir(Map<String, Object> paramEmi) throws ApplicationException;
	
	//@Deprecated
	//public WrapperResultados guardarArchivo(Map<String, Object> params) throws ApplicationException;
	
	public WrapperResultados guardarArchivoPersona(String cdperson, Date fecha, String cddocume, String dsdocume,String codidocu) throws ApplicationException;
	public List<Map<String,String>>obtenerDocumentosPoliza(Map<String,Object>params) throws ApplicationException;
	/*
	public List<Map<String, String>> obtenerListaDocumentos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite
			) throws ApplicationException;
	*/
	public WrapperResultados insertaMaestroHistoricoPoliza(Map<String, Object> param) throws ApplicationException;
	public WrapperResultados movMPoliage(Map<String, Object> param) throws ApplicationException;
	public List<Map<String, String>> cargarTiposClausulasExclusion() throws ApplicationException;
	public List<Map<String, String>> obtenerExclusionesPorTipo(Map<String, String> smap1) throws ApplicationException;
	public Map<String, String> obtenerHtmlClausula(Map<String, String> paramObtenerHtml) throws ApplicationException;
	//public WrapperResultados PMovMesacontrol(Map<String, Object> parMesCon) throws ApplicationException;
	public WrapperResultados PMovTvalosin(Map<String, Object> parTvalosin) throws ApplicationException;
	public List<Map<String, String>> loadMesaControl(Map<String,String> params) throws ApplicationException;
	public List<Map<String, String>> loadMesaControlUsuario(Map<String,String> params) throws ApplicationException;
	public List<Map<String, String>> loadMesaControlSuper(Map<String,String>params) throws ApplicationException;
	public WrapperResultados obtenDatosRecibos(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados obtenDatosComisiones(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados obtenDatosRecibosDxN(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados cargaColonias(String codigoPostal) throws ApplicationException;
	public WrapperResultados obtenDatosClienteWS(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados obtenDatosClienteGeneralWS(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados obtenDatosClienteGeneralWSporCdperson(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados obtenDatosDomicilioGeneralWSporCdperson(HashMap<String,Object> params) throws ApplicationException;
	public WrapperResultados mesaControlUpdateSolici(String ntramite,String nmsolici) throws ApplicationException;
	public WrapperResultados mesaControlUpdateStatus(String ntramite,String status) throws ApplicationException;
	//public WrapperResultados movDmesacontrol(Map<String, Object> params) throws ApplicationException;
	public List<Map<String, String>> obtenerDetalleMC(Map<String, String> smap1) throws ApplicationException;
	public WrapperResultados mesaControlFinalizarDetalle(Map<String, String> smap1) throws ApplicationException;
	public WrapperResultados preparaContrarecibo(Map<String, String> docu) throws ApplicationException;
	public Map<String, Object> obtieneValositSituac(Map<String, String> params) throws ApplicationException;
	public Map<String, Object> obtieneValositSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac) throws ApplicationException;
	public List<Map<String, String>> buscarRFC(Map<String,String> params) throws ApplicationException;
	public WrapperResultados borrarMpoliper(Map<String, String> param) throws ApplicationException;
	public List<Map<String, String>> obtenerRamos(String cdunieco) throws ApplicationException;
	public List<Map<String, String>> obtenerTipsit(String cdunieco) throws ApplicationException;
	
	/**
	 * Metodo que guarda la bitacora de los WS
	 * @param cdunieco Unidad Economica
	 * @param cdramo   Ramo
	 * @param estado   Estado
	 * @param poliza   Poliza
	 * @param nmsuplem Suplemento
	 * @param codigo   Codigo de error siplificado que indica de que WS se trata y si el tipo de error es de conexion
	 * @param mensaje  Mensaje de errir, ya sea de una Excepcion o el error que responde el WS
	 * @param usuario  Usuario que ejecut� el llamado al WS
	 * @param ntramite Tramite
	 * @param cdurlws  Codigo del properties que contiene la url del WS
	 * @param metodows Metodo que se eject� del WS
	 * @param xmlin    Xml de entrada que se ejecuto para el WS
	 * @param cderrws  Codigo de Error que regreso el WS
	 * @return
	 * @throws ApplicationException
	 */
	public WrapperResultados movBitacobro(String cdunieco,String cdramo,String estado,String poliza,String nmsuplem,String codigo,String mensaje, String usuario, String ntramite, String cdurlws, String metodows, String xmlin, String cderrws) throws ApplicationException;
	
	public List<Map<String, String>> PValInfoPersonas(Map<String,String> params) throws ApplicationException;	
	public String validaTitularMenorEdad(Map<String,String> params) throws ApplicationException;	
	public WrapperResultados obtenerAgentePoliza(String cdunieco, String cdramo, String estado, String nmpoliza)throws ApplicationException;
	public WrapperResultados obtenerAgentePolizaPorPuntos(String cdunieco, String cdramo, String estado, String nmpoliza)throws ApplicationException;
	public WrapperResultados obtenerTiposAgente()throws ApplicationException;
	/**
	 * PKG_SATELITES.P_MOV_MPOLIAGE_PORCENTAJES
	 */
	public WrapperResultados guardarPorcentajeAgentes(Map<String, Object> params) throws ApplicationException;
	public WrapperResultados guardaPorcentajeAgentesPorPuntos(Map<String, Object> params) throws ApplicationException;
	
	public WrapperResultados guardarEliminarPorcentajeAgentes(Map<String, Object> params) throws ApplicationException;
	public WrapperResultados guardaEliminaPorcentajeAgentesPorPuntos(Map<String, Object> params) throws ApplicationException;
	/**
	 * Devuelve una lista con los asegurados extraprimados invalidos
	 * @param params
	 * @return
	 * @throws ApplicationException
	 */
	public WrapperResultados validarExtraprima(Map<String, String> params) throws ApplicationException;
	/**
	 * pkg_satelites.valida_extraprima_situac
	 */
	public WrapperResultados validarExtraprimaSituac(Map<String, String> params) throws ApplicationException;
	/**
	 * pkg_satelites.valida_extraprima_situac_read
	 */
	public WrapperResultados validarExtraprimaSituacRead(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem) throws ApplicationException;
	public WrapperResultados guardaPeriodosDxN(Map<String, Object> params) throws ApplicationException;
	public WrapperResultados lanzaProcesoDxN(Map<String, Object> params) throws ApplicationException;
	public String habilitaSigRecibo(Map<String, String> params) throws ApplicationException;

	public String obtenCdtipsitGS(Map<String, Object> params) throws ApplicationException;
	public String obtenSubramoGS(Map<String, Object> params) throws ApplicationException;
	public String obtenCdtipsit(Map<String, Object> params) throws ApplicationException;
	
	/**
	 * PKG_SATELITES.P_OBT_DATOS_MPOLIPER
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return nmsituac,cdrol,fenacimi,sexo,cdperson,nombre,segundo_nombre,Apellido_Paterno
	 * ,Apellido_Materno,cdrfc,Parentesco,tpersona,nacional,swexiper
	 * @throws ApplicationException
	 */
    public List<Map<String, Object>> obtenerAsegurados(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsuplem) throws ApplicationException;
    
    /**
     * Valida si usuario tiene asociada una sucursal dada
     * @param cdunieco Sucursal a validar
     * @param cdramo   Ramo
     * @param cdtipsit Tipo de situacion
     * @param username Usuario a validar
     * @return
     * @throws ApplicationException
     */
    public WrapperResultados validaUsuarioSucursal(String cdunieco, String cdramo, String cdtipsit, String username) throws ApplicationException;
    
    /**
     * Verifica si los datos del domicilio ya estan guardados y regresa el cdperson
     * @param cdideper
     * @return
     */
    public WrapperResultados existeDomicilioContratante(String cdideper, String cdperson);
    
    public boolean actualizaCdIdeper(Map<String,String> params);
    
    public void validaDatosAutos(Map<String,String> params)throws Exception;
    
    public void actualizaPolizaExterna(Map<String,String> params)throws Exception; 

    public void cargaCobranzaMasiva(Map<String,String> params)throws Exception;
    
    public List<Map<String, String>> obtieneCobranzaAplicada(Map<String,String>params) throws Exception;
 
    public List<Map<String, String>> obtieneRemesaAplicada(Map<String,String>params) throws Exception;
}