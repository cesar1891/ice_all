package mx.com.gseguros.portal.general.dao;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;

public interface CatalogosDAO {
	
	public List<GenericVO> obtieneTmanteni(String cdTabla) throws Exception;
	
	public List<GenericVO> obtieneAgentes(String claveONombre) throws Exception;
	
	/**
	 * Obtiene los atributos de situacion restringidos por rol del sistema (EGS)
	 * @param cdAtribu  Numero o posicion del atributo
	 * @param cdTipSit  Tipo de situaci&oacute;n
	 * @param otValor   Identificador del elemento padre (si es un elemento dependiente)
	 * @param cdSisRol  C&oacute;digo del Rol en el sistema
	 * @return Atributos de situacion
	 * @throws Exception
	 */
	public List<GenericVO> obtieneAtributosSituacion(String cdAtribu, String cdTipSit, String otValor, String cdSisRol) throws Exception; //utilizado para atributos situacion x rol (EGS)
	
	public List<GenericVO> obtieneAtributosSiniestro(String cdAtribu, String cdTipSit ,String otValor) throws Exception;
	
	public List<GenericVO> obtieneAtributosPoliza(String cdAtribu, String cdRamo, String otValor) throws Exception;

	public List<GenericVO> obtieneAtributosGarantia(String cdAtribu, String cdTipSit, String cdRamo, String valAnt, String cdGarant, String cdSisrol) throws Exception; // se agrega par�metro cdSisrol para considerar restricciones por rol (EGS)
	
	public List<GenericVO> obtieneAtributosRol(String cdAtribu, String cdTipSit ,String cdRamo, String valAnt, String cdRol) throws Exception;
	
	public List<GenericVO> obtieneColonias(String codigoPostal) throws Exception;

	public List<GenericVO> obtieneMunicipios(String cdEstado) throws Exception;

	public List<GenericVO> obtieneZonasPorModalidad(String cdtipsit) throws Exception;
	
	public List<GenericVO> obtieneRolesSistema(String dsRol) throws Exception;
	
	/**
	 * Obtiene las sucursales
	 * @param cdunieco Sucursal administradora por la que se va a filtrar
	 * @return Listado de sucursales
	 * @throws Exception
	 */
	public List<GenericVO> obtieneSucursales(String cdunieco,String cdusuari) throws Exception;
	
	public List<GenericVO> obtieneStatusTramite(Map<String,String> params) throws Exception;
	
	public String obtieneCantidadMaxima(String cdramo, String cdtipsit, TipoTramite tipoTramite, Rango rango, Validacion validacion) throws Exception;
	
	public List<GenericVO> cargarAgentesPorPromotor(Map<String,String> params) throws Exception;
	
	public List<GenericVO> cargarServicioPublicoAutos(Map<String,String> params) throws Exception;
	
	public String agregaCodigoPostal(Map<String,String>params)throws Exception;

	public String asociaZonaCodigoPostal(Map<String,String>params)throws Exception;
	
	public List<GenericVO> cargarDescuentosPorAgente(
    		String tipoUnidad
    		,String uso
    		,String zona
    		,String promotoria
    		,String cdagente
    		,String cdtipsit
    		,String cdatribu)throws Exception;
	
	public List<GenericVO>cargarListaNegocioServicioPublico(
			String cdtipsit
			,String cdatribu
			,String tipoUnidad
			,String cdagente)throws Exception;
	
	public List<GenericVO>cargarModelosPorSubmarcaRamo5(String submarca,String cdtipsit)throws Exception;
	
	public List<GenericVO>cargarVersionesPorModeloSubmarcaRamo5(String submarca,String modelo,String cdtipsit)throws Exception;
	
	public List<GenericVO>cargarAutosPorCadenaRamo5(
			String cadena
			,String cdtipsit
			,String servicio
			,String uso
			)throws Exception;
	
	public List<GenericVO>cargarTtapvat1(String cdtabla)throws ApplicationException,Exception;
	public List<GenericVO>cargarNegocioPorCdtipsitRamo5(String cdtipsit)throws Exception;
	public List<GenericVO>cargarUsosPorNegocioRamo5(String cdnegocio,String cdtipsit,String servicio,String tipocot)throws Exception;
	public List<GenericVO>cargarMarcasPorNegocioRamo5(String cdnegocio,String cdtipsit)throws Exception;
	public List<GenericVO>cargarNegociosPorAgenteRamo5(
			String cdagente
			,String cdsisrol
			,String tipoflot
			)throws Exception;
	public List<GenericVO>cargarCargasPorNegocioRamo5(String cdsisrol,String negocio)throws Exception;
	public List<GenericVO>cargarPlanesPorNegocioModeloClavegsRamo5(
			String cdtipsit
			,String modelo
			,String negocio
			,String clavegs
			,String servicio
			,String tipoflot
			)throws Exception;
	public List<GenericVO>cargarNegociosPorTipoSituacionAgenteRamo5(
			String cdtipsit
			,String cdagente
			,String producto
			,String cdsisrol
			)throws Exception;
	public List<GenericVO>cargarTiposSituacionPorNegocioRamo5(String negocio,String producto)throws Exception;
	public List<GenericVO>cargarCuadrosPorSituacion(String cdtipsit)throws Exception;
	public List<GenericVO>recuperarSumaAseguradaRamo4(String cdsisrol,String cdplan)throws Exception;

	public List<GenericVO>recuperarTiposServicioPorAuto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception;
	
	public List<GenericVO> recuperarListaTiposValorRamo5PorRol(String cdtipsit,String cdsisrol,String cdusuari) throws Exception;
	
	public List<GenericVO> recuperarModulosEstadisticas() throws Exception;
	
	public List<GenericVO> recuperarTareasEstadisticas(String cdmodulo) throws Exception;
	
	public List<GenericVO> obtieneAgenteEspecifico(String cdagente) throws Exception;
	
	public List<GenericVO> recuperarListaPools() throws Exception;
	
	public List<GenericVO> obtieneAtributosExcel(String cdTabla) throws Exception;
	
	public List<GenericVO> obtieneCatalogoParentescoAutos() throws Exception;
	
	public List<GenericVO> recuperarListaConvenios() throws Exception;
	
	public List<GenericVO> recuperarContratantesSalud(String nombre) throws Exception;
	
	public List<GenericVO> recuperaRamosColectivoTipoRamo(String tipoRamo) throws Exception;
	
	public List<GenericVO> recuperarTiposRamoSituacionColectivo(String tipoRamo, String cdramo) throws Exception;
	
	public List<GenericVO> recuperarListaFiltroPropiedadesInciso(String cdunieco, String cdramo,String estado,String nmpoliza) throws Exception;
	
	public List<GenericVO> recuperarRamosPorSucursalPorTipogrupo(String cdunieco, String tipogrupo) throws Exception;
	
	public List<GenericVO> recuperarTipsitPorRamoPorTipogrupo(String cdramo, String tipogrupo) throws Exception;
	
	public List<GenericVO> recuperarTiposDeEndosoPorCdramoPorCdtipsit(String cdramo, String cdtipsit, String vigente) throws Exception;
	
	public List<GenericVO> recuperarMotivosRechazo (String ntramite) throws Exception;
	
	/**
	 * Obtiene los valores de ttapvat1
	 * @param cdtabla
	 * @return Lista de otvalor de ttapvat1
	 * @throws Exception
	 */
	public List<Map<String,String>> obtenerTablaApoyo1(String cdtabla)throws Exception;
	
	/**
	 * Obtiene los valores de la tabla de apoyo CAMPEXTREN
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> obtenerCamposExclusionRenovacion()throws Exception;
	
	public List<GenericVO> obtieneCatalogoDescAtrib(String cdRamo, String dsAtribu, String otValor) throws Exception;

    public List<GenericVO> obtieneCatalogoRetAdminAgente(String pv_numsuc_i, String pv_cdagente_i) throws Exception;
	
	public boolean guardaDescripcionCortaCobertura(String cdgarant, String descCorta) throws Exception;

	public List<GenericVO> obtieneClaveDescuentoSubRamo(String pv_numsuc_i, String pv_cveent_i,String pv_cdramo_i, String pv_cdtipsit_i)throws Exception;

	public List<GenericVO> obtieneIdsCierres() throws Exception;

    public List<GenericVO> obtieneAdminXAgente(String pv_cdagente_i) throws Exception;
    
    public List<GenericVO> recuperarListaFiltroPropiedadInciso(String cdramo,String cdtipsit,String nivel) throws Exception;
    
    public List<Map<String, String>> recuperarTiposEndosoPorTramite (String ntramite) throws Exception;
	
}