package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.portal.consultas.model.PagedMapList;

public interface IndicadoresDAO {
	
	/**
	 * Obtiene los indicadores principales del dashboard de la Mesa de Control
	 * @param idcierre TODO
	 * @param cdunieco
	 * @param lineanegocio
	 * @param cdramo
	 * @param tipotramite
	 * @param cdagente
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> obtieneDashInicial(String idcierre, String cdunieco, String lineanegocio,
			String cdramo, String tipotramite, String cdagente) throws Exception;
	
	
	/**
	 * Obtiene el detalle de los tramites pendientes
	 * @param idcierre TODO
	 * @param cdunieco
	 * @param lineanegocio
	 * @param cdramo
	 * @param tipotramite
	 * @param cdagente
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> obtieneDashPendientes(String idcierre, String cdunieco, String lineanegocio,
			String cdramo, String tipotramite, String cdagente) throws Exception;


	/**
	 * Obtiene los tramites por linea de negocio
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtieneTramitesPorLineaNegocio(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente) throws Exception;

	
	/**
	 * Obtiene los tramites por linea de negocio y ramo de acuerdo a los criterios enviados
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @param start TODO
	 * @param limit TODO
	 * @return
	 * @throws Exception
	 */
	public PagedMapList obtieneTramitesLineaNegocioPorRamo(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente, String start, String limit) throws Exception;
	
	
	/**
	 * Obtiene el detalle de las lineas de negocio de acuerdo a los criterios enviados
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @param start TODO
	 * @param limit TODO
	 * @return
	 * @throws Exception
	 */
	public PagedMapList obtieneDetalleLineaNegocio(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente, String start, String limit) throws Exception;
	
	
	/**
	 * Obtiene las lineas de negocio por sucursal de acuerdo a los criterios enviados
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> obtieneLineaNegocioPorSucursal(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente) throws Exception;
	
	
	/**
	 * Obtiene las lineas de negocio por usuario de acuerdo a los criterios enviados
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @param start TODO
	 * @param limit TODO
	 * @return
	 * @throws Exception
	 */
	public PagedMapList obtieneLineaNegocioPorUsuario(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente, String start, String limit) throws Exception;
	
	
	/**
	 * Obtiene los tramites por tipo de tramite de acuerdo a los criterios enviados
	 * @param idcierre TODO
	 * @param cdetapa
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @param start TODO
	 * @param limit TODO
	 * @return
	 * @throws Exception
	 */
	public PagedMapList obtieneTramitesPorTipo(String idcierre, String cdetapa, String cdunieco,
			String lineaNegocio, String cdramo, String tipoTramite, String cdagente, String start, String limit) throws Exception;
	
	
	/**
	 * Obtiene los tramites pendientes por el numero de dias atrasados
	 * @param idCierre TODO
	 * @param cdunieco
	 * @param lineaNegocio
	 * @param cdramo
	 * @param tipoTramite
	 * @param cdagente
	 * @param numDias Numero de dias atrasados
	 * @param start TODO
	 * @param limit TODO
	 * @return tramites obtenidos
	 * @throws Exception
	 */
	public PagedMapList obtieneTramitesPendientesPorDia(String idCierre,
			String cdunieco, String lineaNegocio, String cdramo, String tipoTramite, String cdagente, String numDias, String start, String limit) throws Exception;
	
}