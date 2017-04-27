package mx.com.gseguros.portal.general.service;

import java.util.List;
import java.util.Map;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.general.util.Catalogos;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;

public interface CatalogosManager {
	
	public List<GenericVO> getTmanteni(Catalogos catalogo) throws Exception;
	
	public List<GenericVO> obtieneColonias(String codigoPostal) throws Exception;

	public List<GenericVO> obtieneMunicipios(String cdEstado) throws Exception;

	public List<GenericVO> obtieneZonasPorModalidad(String cdtipsit) throws Exception;
	
	public List<GenericVO> obtieneAgentes(String claveONombre) throws Exception;
	
	/**
	 * Obtiene los atributos de situacion restringidos por rol del sistema (EGS)
	 * @param cdAtribu  Numero o posicion del atributo
	 * @param cdTipSit  Tipo de situaci&oacute;n
	 * @param idPadre   Identificador del elemento padre (si es un elemento dependiente)
	 * @param cdSisRol  C&oacute;digo del Rol en el sistema
	 * @return Atributos de situacion
	 * @throws Exception
	 */
	public List<GenericVO> obtieneAtributosSituacion(String cdAtribu, String cdTipSit, String idPadre, String cdSisRol) throws Exception; // se utiliza para atributos situacion x rol (EGS)
	
	public List<GenericVO> obtieneAtributosSiniestro(String cdAtribu, String cdTipSit, String idPadre) throws Exception;
	
	public List<GenericVO> obtieneAtributosPoliza(String cdAtribu, String cdRamo, String idPadre) throws Exception;

	public List<GenericVO> obtieneAtributosGarantia(String cdAtribu, String cdTipSit, String cdRamo, String idPadre, String cdGarant, String cdSisrol) throws Exception; // se agrega par�metro cdSisrol para considerar restricciones por rol (EGS)
	
	public List<GenericVO> obtieneAtributosRol(String cdAtribu, String cdTipSit ,String cdRamo, String valAnt, String cdRol) throws Exception;
	
	public List<GenericVO> obtieneRolesSistema(String dsRol) throws Exception;
	
	/**
	 * Obtiene las sucursales
	 * @param cdunieco Sucursal administradora por la que se filtra
	 * @return
	 * @throws Exception
	 */
	public List<GenericVO> obtieneSucursales(String cdunieco,String cdusuari) throws Exception;
	
	public List<GenericVO> obtieneStatusTramite(Map<String,String> params) throws Exception;
	
	/**
     * Obtiene la cantidad m&aacute;xima de acuerdo a un tipo de rango (unidad de medida) solicitado
     * @param cdramo      cdramo
     * @param cdtipsit    cdtipsit
     * @param tipoTramite tipo de tramite
     * @param rango       tipo de rango solicitado
     * @return Cantidad m&aacute;xima solicitada
     * @throws Exception
     */
    public String obtieneCantidadMaxima(String cdramo, String cdtipsit, TipoTramite tipoTramite, Rango rango, Validacion validacion) throws Exception;
    
    public List<GenericVO> cargarAgentesPorPromotor(String cdusuari)throws Exception;
    
    public List<GenericVO> cargarServicioPublicoAutos(String substring,String cdramo,String cdtipsit)throws Exception;
    
    public String agregaCodigoPostal(Map<String, String> params)throws Exception;

    public String asociaZonaCodigoPostal(Map<String, String> params)throws Exception;
    
    public List<Map<String, String>> obtieneTablasApoyo(Map<String,String> params) throws Exception;

    public String guardaTablaApoyo(Map<String,String> params) throws Exception;

    public boolean guardaClavesTablaApoyo(Map<String, String> params, List<Map<String, String>> saveList) throws Exception;

    public boolean guardaAtributosTablaApoyo(Map<String, String> params, List<Map<String, String>> saveList) throws Exception;
    
    public List<Map<String, String>> obtieneClavesTablaApoyo(Map<String,String> params) throws Exception;

    public List<Map<String, String>> obtieneAtributosTablaApoyo(Map<String,String> params) throws Exception;
    
    public List<GenericVO> cargarDescuentosPorAgente(
    		String tipoUnidad
    		,String uso
    		,String zona
    		,String promotoria
    		,String cdagente
    		,String cdtipsit
    		,String cdatribu)throws Exception;
    
    public List<GenericVO> cargarListaNegocioServicioPublico(
    		String cdtipsit
    		,String cdatribu
    		,String tipoUnidad
    		,String cdagente);
    
    public List<GenericVO>cargarModelosPorSubmarcaRamo5(String submarca,String cdtipsit);
    
    public List<GenericVO>cargarVersionesPorModeloSubmarcaRamo5(String submarca,String modelo,String cdtipsit);
    
    public List<GenericVO>cargarAutosPorCadenaRamo5(
    		String cadena
    		,String cdtipsit
    		,String servicio
    		,String uso
    		);
    
    public List<GenericVO>cargarTtapvat1(String cdtabla);
    public List<GenericVO>cargarNegocioPorCdtipsitRamo5(String cdtipsit);
    public List<GenericVO>cargarUsosPorNegocioRamo5(String cdnegocio,String cdtipsit,String servicio,String tipocot);
    public List<GenericVO>cargarMarcasPorNegocioRamo5(String cdnegocio,String cdtipsit);
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
    public List<GenericVO>cargarSumaAseguradaRamo4(String cdsisrol,String cdplan)throws Exception;
    
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

    public List<GenericVO> obtieneListaParentesco() throws Exception;
    
    public List<GenericVO> obtieneAgenteEspecifico(String cdagente) throws Exception;
    
    public List<GenericVO> recuperarListaPools() throws Exception;
    
    public List<GenericVO> recuperarGruposPoliza(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;
    
    public List<GenericVO> recuperarSubramos(String cdramo) throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarTiposRamo() throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarRamosPorTipoRamo(String cdtipram) throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarSucursalesPorTipoRamo(String cdtipram) throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarComboUsuarios(String cadena) throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarSucursalesPermisoImpresion(
    		String cdtipram
    		,String cdusuari
    		,String cdunieco
    		) throws Exception;
    
    public List<GenericVO> obtieneAtributosExcel(Catalogos catalogo) throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarTtiptramc() throws Exception;
    
    @Deprecated
    public List<GenericVO> recuperarTestadomcPorAgrupamc(String agrupamc, String extras) throws Exception;
    
    public List<GenericVO> recuperarTtipflumc(String agrupamc) throws Exception;
    
    public List<GenericVO> recuperarTflujomc(String cdtipflu, String swfinal) throws Exception;
    
    public List<GenericVO> recuperarTtipsupl(String cdtiptra,String ninguno) throws Exception;
    
    public List<GenericVO> obtieneCatalogoParentescoAutos() throws Exception;
    
    public List<GenericVO> recuperarTdocume(String cdtiptra) throws Exception;
    
    public List<GenericVO> cargarCotizadoresActivos(String cadena) throws Exception;

    public List<GenericVO> obtieneMotivosReexp(String cdramo, String cdtipsit) throws Exception;
    
    public List<GenericVO> recuperarFormasDePagoPorRamoTipsit(String cdramo, String cdtipsit) throws Exception;
    
    public List<GenericVO> recuperarClientesPorNombreApellido(String cadena) throws Exception;
    
    public List<GenericVO> obtieneCatalogoConvenios() throws Exception;
    
    public List<GenericVO> recuperaContratantesSalud(String nombre) throws Exception;
    
    public List<GenericVO> recuperarTipoRamoColectivo(String nombre) throws Exception;
    
    public List<GenericVO> recuperarTipoRamoSituacionColectivo(String tipoRamo, String cdramo) throws Exception;
    
    public List<GenericVO> recuperarListaFiltroPropiedadesInciso(String cdunieco,String cdramo,String estado, String nmpoliza) throws Exception;
    
    public List<GenericVO> recuperarTtipflumcPorRolPorUsuario(String agrupamc,String cdsisrol,String cdusuari) throws Exception;
    
    public List<GenericVO> recuperarTflujomcPorRolPorUsuario(
    		String cdtipflu
    		,String swfinal
    		,String cdsisrol
    		,String cdusuari
    		)throws Exception;
    
    public List<GenericVO> recuperarSucursalesPorFlujo(String cdflujomc) throws Exception;
    
    public List<GenericVO> recuperarRamosPorSucursalPorTipogrupo(String cdunieco, String tipogrupo) throws Exception;
    
    public List<GenericVO> recuperarTipsitPorRamoPorTipogrupo(String cdramo, String tipogrupo) throws Exception;
    
    public List<GenericVO> recuperarTiposDeEndosoPorCdramoPorCdtipsit(String cdramo, String cdtipsit, String vigente) throws Exception;
    
    public List<GenericVO> recuperarMotivosRechazo (String ntramite) throws Exception;
    
    public List<GenericVO> recuperaContratantes(String cdunieco, String cdramo, String cadena) throws Exception;
    
    public List<GenericVO> recuperaContratantesRfc(String cadena) throws Exception;
    
    public List<GenericVO> recuperaTablaApoyo1(String cdtabla) throws Exception;
    
	public List<GenericVO> recuperaCamposExclusionRenovacion() throws Exception;
	
	public List<GenericVO> obtieneCatalogoDescAtrib(String cdRamo, String dsAtribu, String idPadre) throws Exception;

    public List<GenericVO> obtieneCatalogoRetAdminAgente(String pv_numsuc_i, String pv_cdagente_i) throws Exception;
	
	public boolean guardaDescripcionCortaCobertura(String cdgarant, String descCorta) throws Exception;

    public List<GenericVO> obtieneClaveDescuentoSubRamo(String pv_numsuc_i, 
                                                        String pv_cveent_i, 
                                                        String pv_cdramo_i,
                                                        String pv_cdtipsit_i) throws Exception;
	
	public List<GenericVO> obtieneIdsCierres() throws Exception;

    public List<GenericVO> obtieneAdministradoraXAgente(String pv_cdagente_i) throws Exception;
    
    public List<GenericVO> recuperarListaFiltroPropiedadInciso(String cdramo,String cdtipsit, String nivel) throws Exception;

	public List<GenericVO> obtieneComentariosNegocio(String cdramo, String cdtipsit, String negocio) throws Exception;

    public List<GenericVO> recuperarTiposEndosoPorTramite (String ntramite) throws Exception;
}