package mx.com.gseguros.portal.mesacontrol.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.documentos.model.Documento;

public interface MesaControlDAO
{
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String cargarCdagentePorCdusuari(String cdusuari) throws Exception;
	
	/**
	 * 
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cdsucadm
	 * @param cdsucdoc
	 * @param cdtiptra
	 * @param ferecepc
	 * @param cdagente
	 * @param referencia
	 * @param nombre
	 * @param festatus
	 * @param status
	 * @param comments
	 * @param nmsolici
	 * @param cdtipsit
	 * @param swimpres
	 * @param valores
	 * @param cdtipsup TODO
	 * @param renuniext TODO
	 * @param renramo TODO
	 * @param renpoliex TODO
	 * @param origenMesa TODO
	 * @param cdunidspch TODO
	 * @return
	 * @throws Exception
	 */
	public String movimientoMesaControl (
			String cdunieco  , String cdramo   , String estado     , String nmpoliza,
			String nmsuplem , String cdsucadm , String cdsucdoc   , String cdtiptra,
			Date ferecepc   , String cdagente , String referencia , String nombre,
			Date festatus   , String status   , String comments   , String nmsolici,
			String cdtipsit , String cdusuari , String cdsisrol   , String swimpres,
			String cdtipflu , String cdflujomc,
			Map<String, String> valores, String cdtipsup, String renuniext, String renramo, String renpoliex, boolean origenMesa,
			String cdunidspch) throws Exception;
	
	/**
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param cdtiptra
	 * @param renuniext TODO
	 * @param renramo TODO
	 * @param renpoliex TODO
	 * @return
	 * @throws Exception
	 */
	public void actualizaNmpolizaMesaControl(
												  String nmpoliza ,String cdunieco ,String cdramo ,String estado  ,String ntramite
												 ,String cdtiptra ,String cduniext ,String ramo   ,String nmpoliex
												)throws Exception;
	
	/**
	 * GUARDA UN REGISTRO DE TDMESACONTROL.
	 * @param cerrado ¿EL REGISTRO NUEVO YA INSERTA FECHA, USUARIO y ROL FIN?
	 */
	public void movimientoDetalleTramite(
			String ntramite      , Date feinicio   , String cdclausu
			,String comments     , String cdusuari , String cdmotivo
			,String cdsisrol     , String swagente , String cdusuariDest
			,String cdsisrolDest , String status   , boolean cerrado)
			throws Exception;
	
	/**
	 * 
	 * @param ntramite
	 * @param nmsolici
	 * @throws Exception
	 */
	public void actualizarNmsoliciTramite(String ntramite, String nmsolici) throws Exception;
	
	/**
	 * 
	 * @param ntramite
	 * @param cdramo
	 * @param cdtipsit
	 * @param cdsucadm
	 * @param cdsucdoc
	 * @param comments
	 * @param valores
	 * @throws Exception
	 */
	public void actualizaValoresTramite(String ntramite, String cdramo,
			String cdtipsit, String cdsucadm, String cdsucdoc, String comments,
			Map<String, String> valores) throws Exception;
	
	/**
	 * 
	 * @param cdtiptra
	 * @param ntramite
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param nmsolici
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> cargarTramitesPorParametrosVariables(
			String cdtiptra, String ntramite, String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmsolici)
			throws Exception;
	
	/**
	 * 
	 * @param ntramite
	 * @param cdusuari
	 * @throws Exception
	 */
	public void guardarRegistroContrarecibo(String ntramite,String cdusuari) throws Exception;
	
	/**
	 * 
	 * @param ntramite
	 * @param cddocume
	 * @param nuevo
	 * @throws Exception
	 */
	public void actualizarNombreDocumento(String ntramite,String cddocume,String nuevo) throws Exception;
	
	/**
	 * 
	 * @param ntramite
	 * @param cddocume
	 * @throws Exception
	 */
	public void borrarDocumento(String ntramite, String cddocume) throws Exception;

	public void borraDomicilioAsegSiCodposCambia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdpos)throws Exception;
	
	/**
	 * Guarda un documento asociado a una poliza
	 * @param cdunieco Unidad economica de la poliza
	 * @param cdramo   Codigo de Ramo de la poliza
	 * @param estado   Estado de la poliza
	 * @param nmpoliza Numero de poliza
	 * @param nmsuplem Numero de suplemento / imagen de la poliza
	 * @param feinici  Fecha de inicio
	 * @param cddocume Codigo del documento
	 * @param dsdocume Descripcion del documento
	 * @param nmsolici  Numero de solicitud
	 * @param ntramite  Numero de tramite
	 * @param tipmov    Tipo de Movimiento / Tipo de Suplemento
	 * @param swvisible Indica si sera visible en la lista de documentos
	 * @param codidocu Codigo de documento
	 * @param cdtiptra Tipo de tramite
	 * @param cdorddoc TODO
	 * @param cdusuari TODO
	 * @param cdsisrol TODO
	 * @param sustituir TODO
	 * @param cdmoddoc TODO
	 * @throws Exception
	 */
	public void guardarDocumento(String cdunieco, String cdramo, String estado,
			String nmpoliza, String nmsuplem, Date feinici, String cddocume,
			String dsdocume, String nmsolici, String ntramite, String tipmov,
			String swvisible, String codidocu, String cdtiptra, String cdorddoc, Documento documento, String cdusuari, String cdsisrol, boolean sustituir) throws Exception;
	
	/*
	public String turnaPorCargaTrabajo(
			String ntramite
			,String cdsisrol
			,String status
			)throws Exception;
	*/
	
	public void validarAntesDeTurnar(
    		String ntramite
    		,String status
    		,String cdusuari
    		,String cdsisrol
    		)throws Exception;
	
	
	/**
	 * Actualiza estatus de un tramite en la Mesa de Control
	 * @param ntramite
	 * @param status
	 * @throws Exception
	 */
	public void actualizaStatusMesaControl(String ntramite, String status) throws Exception;
	
	public void actualizarStatusRemesa(
			String ntramite
			,String status
			)throws Exception;
	
	public List<Map<String,String>> recuperarTramites(
			String cdunieco
			,String ntramite
			,String cdramo
			,String nmpoliza
			,String estado
			,String cdagente
			,String status
			,String cdtipsit
			,Date fedesde
			,Date fehasta
			,String cdsisrol
			,String cdtiptra
			,String contrarecibo
			,String tipoPago
			,String nfactura
			,String cdpresta
			,String cdusuari
			,String cdtipram
			,String lote
			,String tipolote
			,String tipoimpr
			,String cdusuari_busq
			)throws Exception;
	
	public void actualizarHijosRemesa(
			String lote
			,String ntramite
			,String status
			)throws Exception;
	
	public Map<String,Boolean> marcarImpresionOperacion(
			String cdsisrol
			,String ntramite
			,String marcar
			)throws Exception;
	
	public void marcarTramiteVistaPrevia(String ntramite) throws Exception;
	
	public String recuperarSwvispreTramite(String ntramite) throws Exception;
	
	/**
	 * Se pone un status al tramite y se retorna el actual, no se registra en los historicos
	 * @param ntramite
	 * @param statusTemporal
	 * @return
	 * @throws Exception
	 */
	public String marcarTramiteComoStatusTemporal(String ntramite, String statusTemporal) throws Exception;
	/**
	 * Este metodo guada o borra una exclusion de usuario
	 * @param usuario
	 * @param accion puede ser I insertar D borrar
	 * @throws Exception
	 */
	public void movimientoExclusionUsuario(String usuario, String accion) throws Exception;

	/**
	 * Regenera un reporte erroneo
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param cddocume
	 * @param nmsituac
	 * @param nmcertif
	 * @param cdmoddoc
	 * @return
	 * @throws Exception
	 */
	public boolean regeneraReporte(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String cddocume, String nmsituac, String nmcertif, String cdmoddoc) throws Exception;

	/**
	 * Regenera documentos a nivel suplemento
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @return
	 * @throws Exception
	 */
	public boolean regeneraDocumentosEndoso(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception;
	
	/**
	 * Realiza el reverso de impresion para que dado un tramite que ya haya sido impreso,
     * actualizar el mismo como "pendiente por imprimir"
     * 
	 * @param ntramite
	 * @param cdsisrol
	 * @param cdusuari
	 * @throws Exception
	 */
	public void regeneraReverso(String ntramite, String cdsisrol,String cdusuari) throws Exception;
	
	/**
	 * ACTUALIZA UN OTVALOR USANDO UN LIKE %+dsatribu+%
	 * @param ntramite
	 * @param dsatribu
	 * @param otvalor
	 * @param accion (I,U,D) Insert, Update, Delete
	 * @throws Exception
	 */
	public void actualizarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			,String otvalor
			,String accion
			)throws Exception;
	
	/**
	 * RECUPERA UN OTVALOR USANDO UN LIKE %+dsatribu+%
	 * @param ntramite
	 * @param dsatribu
	 * @return otvalor
	 * @throws Exception
	 */
	public String recuperarOtvalorTramitePorDsatribu(
			String ntramite
			,String dsatribu
			)throws Exception;
	
	public void actualizarNmsuplemTramite(String ntramite, String nmsuplem) throws Exception;
	
	public void borrarNmsoliciTramite(String ntramite) throws Exception;
	
	public void concatenarAlInicioDelUltimoDetalle(String ntramite, String comentario, String cdmodulo, String cdevento) throws Exception;
	
	public String regeneraRemesaReport(String ntramite, String cddocume) throws Exception;
	
	public List<Map<String, String>> obtenerMesaControl(String cdunieco, String ntramite, String cdramo, String nmpoliza, String estado, String cdagente, String status, String cdtipsit, String fedesde, String fehasta, 
	        String cdrol, String cdtiptra, String contrarecibo, String tipoPago, String nfactura, String cdpresta, String cdusuari, String cdtipram, String lote, String tipolote,     
	        String tipoimpr, String cdusuari_busq, String dscontra) throws Exception;
	
	/**
     * Si ya existe un tramite cdtiptra 1 o 21 con esa cdunieco, cdramo, estado, nmsolici arroja excepcion
     */
    public void validaDuplicidadTramiteEmisionPorNmsolici (String cdunieco, String cdramo, String estado, String nmsolici) throws Exception;
}