package mx.com.gseguros.portal.siniestros.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.siniestros.model.AltaTramiteVO;
import mx.com.gseguros.portal.siniestros.model.AutorizaServiciosVO;
import mx.com.gseguros.portal.siniestros.model.AutorizacionServicioVO;
import mx.com.gseguros.portal.siniestros.model.CoberturaPolizaVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaManteniVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaPorcentajeVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaProveedorVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTDETAUTSVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaTTAPVAATVO;
import mx.com.gseguros.portal.siniestros.model.DatosSiniestroVO;
import mx.com.gseguros.portal.siniestros.model.ListaFacturasVO;
import mx.com.gseguros.portal.siniestros.model.MesaControlVO;
import mx.com.gseguros.portal.siniestros.model.PolizaVigenteVO;
import mx.com.gseguros.portal.siniestros.model.SiniestroVO;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Reclamo;

public interface SiniestrosDAO {

	public List<AutorizacionServicioVO> obtieneDatosAutorizacionEsp(String nmautser) throws Exception;

	public List<GenericVO> obtieneListadoAsegurado(String cdperson) throws Exception;

	public List<AutorizaServiciosVO> obtieneListadoAutorizaciones(String tipoAut,String cdperson) throws Exception;
	
	public List<ConsultaProveedorVO> obtieneListadoProvMedico(String tipoprov,String cdpresta) throws Exception;
	
	public List<CoberturaPolizaVO> obtieneListadoCoberturaPoliza(HashMap<String, Object> paramCobertura) throws Exception;

	public List<CoberturaPolizaVO> obtieneListadoCoberturaAsegurado(HashMap<String, Object> paramCobertura) throws Exception;
	
	public List<DatosSiniestroVO> obtieneListadoDatSubGeneral(HashMap<String, Object> paramDatSubGral) throws Exception;
	
	public List<GenericVO> obtieneListadoSubcobertura(String cdunieco, String cdramo, String estado, String nmpoliza,
						String nmsituac, String cdtipsit, String cdgarant, String cdsubcob,String cdrol) throws Exception;
	
	public List<GenericVO> obtieneListadoSubcoberturaTotales() throws Exception;
	
	public List<GenericVO> obtieneListadoSubcoberturaTotalesMultisalud(String cdtipsit) throws Exception;
	
	public List<GenericVO> obtieneListadoSubcoberturaRecupera() throws Exception;
	
	public List<GenericVO> obtieneListadoCPTICD(String cdtabla, String otclave) throws Exception;
	
	/**
	 * Metodo sobrecargado para considerar edad y genero del asegurado en la consulta de ICD's (EGS)
	 * @param cdicd
	 * @param cdramo
	 * @param cdtipsit
	 * @param edad
	 * @param genero
	 * @return
	 * @throws Exception
	 */
	public List<GenericVO> obtieneListadoCPTICD(String cdicd, String cdramo, String cdtipsit, String edad, String genero) throws Exception;
	
	public List<GenericVO> obtieneListadoTipoPago(String cdramo) throws Exception;

	public List<HashMap<String, String>> loadListaDocumentos(Map<String, String> params) throws Exception;

	public String generaContraRecibo(HashMap<String, Object> params) throws Exception;

	public List<Map<String, String>> loadListaIncisosRechazos(Map<String, String> params) throws Exception;

	public List<Map<String, String>> loadListaRechazos() throws Exception;
	
	public String guardaEstatusDocumento(HashMap<String, String> params) throws Exception;

	public String rechazarTramite(HashMap<String, String> params) throws Exception;
	
	public List<ConsultaTDETAUTSVO> obtieneListadoTDeTauts(String nmautser) throws Exception;
	
	public String guardarListaTDeTauts(HashMap<String, Object> paramsTDeTauts) throws Exception;
	
	public List<PolizaVigenteVO> obtieneListadoPoliza(String cdperson,String cdramo, String rolUsuario) throws Exception;
	
	/* ############################################################################## 
	 * ##################################### VERIFICAR ##############################*/
	public List<AutorizacionServicioVO> guardarAutorizacionServicio(Map<String, Object> paramsR) throws Exception;

	public List<ConsultaTTAPVAATVO> obtieneListadoTTAPVAAT(HashMap<String, Object> paramTTAPVAAT) throws Exception;

	public List<ConsultaManteniVO> obtieneListadoTipoMedico(String codigo) throws Exception;

	public List<ConsultaPorcentajeVO> obtieneListadoPorcentaje(String cdcpt, String cdtipmed,String mtobase) throws Exception;

	public void eliminacionRegistrosTabla(String nmautser) throws Exception;

	public List<GenericVO> obtieneListadoPlaza() throws Exception;

	public String guardaListaFacturaSiniestro(HashMap<String, Object> paramsFacMesaCtrl) throws Exception;

	public String guardaListaTworkSin(HashMap<String, Object> paramsTworkSin) throws Exception;

	public String guardaAltaSiniestroAutServicio(String nmautser,String nfactura) throws Exception;

	public String guardaAltaSiniestroAltaTramite(String ntramite) throws Exception;
	
	public String guardaAltaSiniestroSinAutorizacion(String ntramite,String cdunieco,String cdramo, String estado,String nmpoliza,
			  String nmsuplem,String nmsituac, String cdtipsit, Date fechaOcurrencia,String nfactura, String secAsegurado) throws Exception;
	
	public List<ListaFacturasVO> obtieneListadoFacturas(HashMap<String, Object> paramFact) throws Exception;

	public String bajaMsinival(HashMap<String, Object> paramBajasinival) throws Exception;
	
	public List<GenericVO> obtieneListadoCobertura(String cdramo,String cdtipsit) throws Exception;

	public List<GenericVO> obtieneListadoCoberturaTotales() throws Exception;
	
	public String actualizaOTValorMesaControl(Map<String, Object> params) throws Exception;
	
	/**
	 * Obtiene lista de siniestros por tramite 
	 * 6969 NMSINIES,
	 * 500 NMAUTSER,
	 * 510918 CDPERSON,
	 * 'JUAN PEREZ' NOMBRE,
	 * SYSDATE FEOCURRE,
	 * 1009 CDUNIECO,
	 * 'SALUD CAMPECHE' DSUNIECO,
	 * 2 CDRAMO,
	 * 'SALUD VITAL' DSRAMO,
	 * 'SL' CDTIPSIT,
	 * 'SALUD VITAL' DSTIPSIT,
	 * status,
	 * 'M' ESTADO,
	 * 500 NMPOLIZA,
	 * 'S' VOBOAUTO,
	 * '65' CDICD,
	 * 'GRIPE' DSICD,
	 * '66' ICD2,
	 * 'FIEBRE' DSICD2,
	 * 12.5 DESCPORC,
	 * 300 DESCNUME,
	 * 15 COPAGO,
	 * 3500 PTIMPORT,
	 * 'S' AUTRECLA,
	 * 54647 NMRECLAM,
	 * aaapertu
	 */
	public List<Map<String,String>> listaSiniestrosMsiniesTramite(Map<String, String> params) throws Exception;
	
	public List<Map<String,String>> listaSiniestrosTramite2(Map<String, String> params) throws Exception;
	
	public List<Map<String,String>> listaAseguradosTramite(Map<String, String> params) throws Exception;
	
	/**
	 * Obtiene el tramite completo
	 */
	@Deprecated
	public Map<String,String> obtenerTramiteCompleto(Map<String, String> params) throws Exception;
	
	public Map<String,String> obtenerTramiteCompletoXNmsolici(Map<String, String> params) throws Exception;
	
	public Map<String,String> obtenerTramiteCompleto(String ntramite) throws Exception;
	
	/**
	 * Obtiene las facturas de un tramite
	 * ntramite,
		nfactura,
		ffactura,
		cdtipser,
		DescServicio,
		cdpresta,
		NombreProveedor,
		ptimport,
		cdgarant,
		DSGARANT,
		DESCPORC,
		DESCNUME
	 */
	public List<Map<String,String>> obtenerFacturasTramite(Map<String, String> params) throws Exception;

	public List<HashMap<String,String>> obtenerFacturasTramiteSiniestro(HashMap<String, String> params) throws Exception;

	/**
	 * Actualizar autorizacion de servicio de un presiniestro
	 */
	public void actualizarAutorizacionTworksin(Map<String, Object>params) throws Exception;

		public void actualizaMsinies(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem,
			String status,
			String aaapertu,
			String nmsinies,
			Date feocurre,
			String cdicd,
			String cdicd2,
			String nreclamo) throws Exception;
	
	public void P_MOV_MAUTSINI(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String nmordina,
			String areaauto,
			String swautori,
			String tipautor,
			String comments,
			String accion) throws Exception;
	
	public void P_MOV_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdgarant,
			String cdconval,
			String cdconcep,
			String idconcep,
			String cdcapita,
			String nmordina,
			Date   femovimi,
			String cdmoneda,
			String ptprecio,
			String cantidad,
			String destopor,
			String destoimp,
			String ptimport,
			String ptrecobr,
			String nmanno,
			String nmapunte,
			String userregi,
			Date   feregist,
			String accion,
			String ptpcioex,
			String dctoimex,
			String ptimpoex,
			String mtoArancel,
			String aplicIVA) throws Exception;
	
	public List<Map<String,String>>P_GET_MSINIVAL(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura) throws Exception;
	
	public List<PolizaVigenteVO> consultaPolizaUnica(HashMap<String, Object> paramPolUnica) throws Exception;

	public String validaExclusionPenalizacion(HashMap<String, Object> paramExclusion) throws Exception;
	
	public void P_MOV_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina,
			String nmordmov,String ptimport,String comments,String userregi,Date feregist,String accion) throws Exception;
	
	public List<Map<String,String>>P_GET_TDSINIVAL(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String nfactura,
			String cdgarant,String cdconval,String cdconcep,String idconcep,String nmordina) throws Exception;
	
	public List<Map<String,String>>P_GET_FACTURAS_SINIESTRO(
			String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,
			String nmsituac,String aaapertu,String status,String nmsinies,String cdtipsit) throws Exception;

	public List<Map<String,String>>cargaHistorialSiniestros(Map<String,String> params) throws Exception;
	
	public List<GenericVO>obtenerCodigosMedicos(String idconcep, String subcaden) throws Exception;
	
	public List<GenericVO>obtenerCodigosMedicosTotales() throws Exception;
	
	public Map<String,String>obtenerLlaveSiniestroReembolso(String ntramite) throws Exception;
	
	public List<Map<String,String>> obtieneDatosGeneralesSiniestro(Map<String, Object> params) throws Exception;
	
	public Map<String, Object> actualizaDatosGeneralesSiniestro(Map<String, Object> params) throws Exception;
	
	public Map<String, Object> actualizaMsiniestroReferenciado(Map<String, Object> params) throws Exception;
	
	public List<Map<String,String>>P_GET_CONCEPTOS_FACTURA(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String cdtipsit,
			String ntramite) throws Exception;
	
	public Map<String,String>obtenerCopagoDeducible(
			String ntramite,
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String nmsituac,
			String aaapertu,
			String status,
			String nmsinies,
			String nfactura,
			String tipopago,
			String cdtipsit) throws Exception;
	
	public Map<String,String>obtenerRentaDiariaxHospitalizacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsituac,
			String nmsuplem) throws Exception;
	
	public String validaPorcentajePenalizacion(String zonaContratada,String zonaAtencion, String cdRamo) throws Exception;

	public String obtieneAutorizacionProceso(String nmAutSer) throws Exception;
	
	public String validaDocumentosCargados(HashMap<String, String> params) throws Exception;
	
	public List<Reclamo> obtieneDatosReclamoWS(Map<String, Object> params) throws Exception;

	public void cambiarEstatusMAUTSERV(String nmautser,String status) throws Exception;

	public List<AltaTramiteVO> consultaListaAltaTramite(String ntramite) throws Exception;

	public List<MesaControlVO> consultaListaMesaControl(String ntramite) throws Exception;
	
	public void eliminacionAsegurado(String ntramite,String factura, String valorAccion) throws Exception;

	//public void eliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion) throws Exception; (EGS)
	public void eliminacionFacturaTramite(String ntramite, String nfactura, String valorAccion, String cdramo) throws Exception; // (EGS)
	
	public Map<String,String> obtenerDatosProveedor(String cdpresta) throws Exception;
	
	public void movTimpsini(String accion
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String ntramite
			,String ptimport
			,String iva
			,String ivr
			,String isr
			,String cedular
			,boolean enviado
			,String nmsecsin) throws Exception;
	
	public Map<String,String>obtenerAutorizacionesFactura(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String aaapertu
			,String status
			,String nmsinies
			,String nfactura) throws Exception;
	
	public List<SiniestroVO> solicitudPagoEnviada(Map params)throws Exception;
	
	public void guardarTotalProcedenteFactura(String ntramite,String nfactura,String importe,String nmsecsin)throws Exception;
	
	public String validaDocumentosAutServicio(String ntramite)throws Exception;
	
	public void turnarTramite(String ntramite,String cdsisrol,String cdusuari) throws Exception;
	
	public List<Map<String,String>> obtenerUsuariosPorRol(String cdsisrol)throws Exception;
	
	public Map<String,Object> moverTramite(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			,String swagente
			) throws Exception;
	
	public void turnarAutServicio(
			String ntramite
			,String nuevoStatus
			,String comments
			,String cdusuariSesion
			,String cdsisrolSesion
			,String cdusuariDestino
			,String cdsisrolDestino
			,String cdmotivo
			,String cdclausu
			) throws Exception;

	public String obtieneMontoArancelCPT(String tipoConcepto, String idProveedor, String idConceptoTipo) throws Exception;

	public String obtienePorcentajeQuirurgico(String tipoMedico, String feAutorizacion) throws Exception;
	
	public void eliminacionDocumentosxTramite(String ntramite) throws Exception;
	
	public String obtieneMesesTiempoEspera(String valorICDCPT, String nomTabla) throws Exception;
	
	public String obtieneMesesTiempoEsperaICDCPT(String cdramo, String cdtipsit, String cdicd, String dsplan) throws Exception;
	
	public String obtieneUsuarioTurnadoSiniestro(String ntramite, String rolDestino) throws Exception;

	public List<GenericVO> obtieneListadoRamoSalud()  throws Exception;

	public List<Map<String, String>> obtieneDatosAdicionales(Map<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneDatosCirculoHospitalarioMultisalud(Map<String, Object> params) throws Exception;
	
	public String obtieneDatosCirculoHospitalario(HashMap<String, Object> paramPenalizacion) throws Exception;

	public String eliminarAsegurado(HashMap<String, Object> paramsTworkSin) throws Exception;

	public List<Map<String, String>> obtieneDatosAdicionalesCobertura(Map<String, Object> params) throws Exception;

	public String obtieneTramiteEnProceso(String nfactura, String cdpresta, String ptimport) throws Exception;
	
	public List<Map<String,String>> obtenerAseguradosTramite(Map<String, String> params) throws Exception;

	public List<Map<String,String>> obtenerInfAseguradosTramite(Map<String, String> params) throws Exception;
	
	public String actualizaValorMC(HashMap<String, Object> modMesaControl) throws Exception;

	public List<GenericVO> obtieneListaTipoAtencion(String cdramo, String modalidad, String tipoPago) throws Exception;

	public List<Map<String, String>> obtieneListaAutirizacionServicio(Map<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneListaMsiniestMaestro(Map<String, Object> params) throws Exception;

	public List<Map<String, String>> obtieneListaDatosValidacionSiniestro(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneListaDatosValidacionAjustadorMed(HashMap<String, Object> params) throws Exception;
	
	public String validaCdTipsitAltaTramite(HashMap<String, Object> paramTramite) throws Exception;

	public List<Map<String, String>> obtieneListaDatosSumaAsegurada(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneSumaAseguradaPeriodoEsperaRec(HashMap<String, Object> params) throws Exception;

	public List<Map<String, String>> obtieneEsquemaSumaAseguradaRec(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtienePeriodoEsperaAsegurado(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneMontoPagoSiniestro(HashMap<String, Object> params) throws Exception;
	
	public List<GenericVO> obtieneListadoConceptoPago(String cdramo) throws Exception;
	
	public List<GenericVO> obtieneListadoAseguradoPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdperson) throws Exception;
	
	public List<Map<String, String>> obtieneDatosBeneficiario(HashMap<String, Object> params) throws Exception;

	public List<Map<String, String>> obtieneDatoMsiniper(HashMap<String, Object> params) throws Exception;

	public List<Map<String, String>> obtieneListaConfiguracionProveedor(HashMap<String, Object> params) throws Exception;
	
	public String obtieneAplicaConceptoIVA(String idConcepto) throws Exception;
	
	public String guardaConfiguracionProveedor(String cdpresta, String tipoLayout, String aplicaIVA,String secuenciaIVA,
			String aplicaIVARET,String cduser, Date fechaProcesamiento, String proceso) throws Exception;
	
	public List<GenericVO>obtenerAtributosLayout(String descripcion) throws Exception;
	
	public String guardaLayoutProveedor(HashMap<String, Object> paramsConfLayout) throws Exception;
	
	public List<Map<String, String>> obtieneConfiguracionLayout(Map<String, Object> params) throws Exception;
	
	public List<Map<String, String>> guardaHistorialSiniestro(Map<String, Object> params) throws Exception;

	public String guardaInfoRecupera(HashMap<String, Object> paramsMRecupera) throws Exception;
	
	public List<Map<String,String>>obtieneInformacionRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String ntramite, String nfactura) throws Exception;
	
	public List<Map<String,String>>obtieneEsquemaSumAseguradaRecupera(String cdunieco,String cdramo, String estado, String nmpoliza, String nmsuplem,
			String nmsituac, Date feEfecto, String cdgarant, String cdconval) throws Exception;
	
	public void P_MOV_MRECUPERA(String ntramite,String nfactura, String cdgarant, String cdconval,
			String cantporc,String ptimport, String accion) throws Exception;

	public String actualizaTelefonoEmailAsegurado(HashMap<String, Object> paramsAsegurado) throws Exception;
	
	public void guardaAutorizacionConceptos(Map<String, Object>params) throws Exception;
	
	public List<Map<String,String>>cargaHistorialCPTPagados(Map<String,String> params) throws Exception;
	
	public List<Map<String,String>> listaSiniestrosInfAsegurados(Map<String, String> params) throws Exception;
	
	public List<GenericVO> obtieneListaAseguraAutEspecial(String ntramite, String nfactura) throws Exception;
	
	public List<CoberturaPolizaVO> obtieneListadoCoberturaProducto(HashMap<String, Object> paramCobertura) throws Exception;
	
	public String guardaListaAutorizacionEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception;
	
	public List<Map<String,String>> obtenerConfiguracionAutEspecial(HashMap<String, Object> paramsAutoriEspecial) throws Exception;
	
	public String asociarAutorizacionEspecial(HashMap<String, Object> paramAutEspecial) throws Exception;
	
	public List<Map<String,String>> obtenerDatosAutorizacionEspecial(Map<String, String> params) throws Exception;
	
	public List<Map<String, String>> obtieneListaExisteCoberturaTramite(HashMap<String, Object> params) throws Exception;
	
	public String validaExisteConfiguracionProv(String cdpresta,String tipoLayout) throws Exception;
	
	public List<Map<String, String>> obtieneConfiguracionLayoutProveedor(HashMap<String, Object> params) throws Exception;
	
	public List<GenericVO> obtieneListaContrareciboAutEsp(String cdramo, String ntramite) throws Exception;
	
	public List<GenericVO> obtieneListaFacturaTramite(String ntramite, String nfactura) throws Exception;

	public List<Map<String, String>> procesaPagoAutomaticoSisco(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> obtieneValidaconAranceleTramite(HashMap<String, Object> params) throws Exception;

	public String obtieneMontoTramitePagoDirecto(HashMap<String, Object> paramsPagoDirecto) throws Exception;
	
	public List<Map<String, String>> obtieneValidaFacturaMontoPagoAutomatico(HashMap<String, Object> params) throws Exception;
	
	public String guardaListaFacturaPagoAutomatico(HashMap<String, Object> datosFactura) throws Exception;
	
	public void actualizaTurnadoMesaControl(String ntramite) throws Exception;

	public List<Map<String, String>> obtieneListadoFacturasxControntrol(HashMap<String, Object> datosTramite) throws Exception;
	
	public List<Map<String, String>> obtieneConfiguracionLayoutExcel(HashMap<String, Object> params) throws Exception;
	
	public List<Map<String, String>> procesaPagoAutomaticoLayout(HashMap<String, Object> params) throws Exception;
	
	public String existeRegistrosProcesarSISCO() throws Exception;
	
	public String validaPersonaSisaSicaps(HashMap<String, Object> paramPersona) throws Exception;
	
	public List<Map<String, String>> obtieneListaLayoutConfigurados(HashMap<String, Object> params) throws Exception;

	public List<Map<String, String>> obtieneInfCoberturaInfonavit(HashMap<String, Object> paramsInfonavit) throws Exception;
	
	public List<Map<String, String>> obtieneInfCausaSiniestroProducto(HashMap<String, Object> paramsInfonavit) throws Exception;
	
	public String validaFeocurreAsegurado(HashMap<String, Object> paramPersona) throws Exception;
	
	public Map<String, Object> actualizaDatosGeneralesConceptos(Map<String, Object> params) throws Exception;
	
	public String validaExisteCodigoConcepto(HashMap<String, Object> paramExiste) throws Exception;
	
	public List<Map<String, String>> obtieneInfAseguradoCobSubCoberturas(HashMap<String, Object> paramsInfonavit) throws Exception;
	
	public String actualizaRegistroTimpsini(HashMap<String, Object> datosActualizacion) throws Exception;
	
	public Map<String, Object> actualizaDatosGeneralesCopago(Map<String, Object> params) throws Exception;
	
	public String actualizaDeducibleCopagoConceptos(HashMap<String, Object> datosActualizacion) throws Exception;
	
	public List<Map<String, String>> obtieneListaDatosAutEspecial(HashMap<String, Object> params) throws Exception;
	
	public String actualizaValImpuestoProv(HashMap<String, Object> datosActualizacion) throws Exception;
	
	public String obtieneValidacionAsegurado(String cdperson, Date feocurre, String nmpoliza) throws Exception;
	
	public List<Map<String,String>> obtieneInfImporteAsegTramiteAseg(Map<String, String> params) throws Exception;
	
	public String actualizaValoresMCSiniestros(HashMap<String, Object> datosMC) throws Exception;
	
	public List<Map<String, String>> obtenerAseguradosxTworksin(Map<String, String> params) throws Exception;

	public void eliminaFaltantesAsegurados() throws Exception;

	public void eliminacionAseguradoEspecifico(String ntramite, String nfactura, String cdperson, Date feocurre) throws Exception;
	
    public List<Map<String,String>>cargaICDExcluidosAsegurados(Map<String,String> params) throws Exception;

    public List<GenericVO> obtieneListadoTipoEvento(String cdramo, String cdtipsit, String cdgarant, String reporte) throws Exception;
    
    public List<Map<String,String>> obtenerDatosValTipoEventoAlta(Map<String, String> params) throws Exception;
    
    public List<GenericVO> obtieneListadoValidacionesGrales() throws Exception;
    
	public String validaProveedorPD(String ntramite) throws Exception; // (EGS)
	
	/**
	 * Marcar registro para solicitud de autorizacion especial: caso excedente de medicamentos. (Actualiza campo reqautes de tabla msiniest) (EGS)
	 * @param reqautes
	 * @param ntramite
	 * @param nfactura
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param nmsituac
	 * @param aaapertu
	 * @param status
	 * @param nmsinies
	 * @return
	 * @throws Exception
	 */
	public String actualizarReqautes(String reqautes, String ntramite, String nfactura, String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String nmsituac, String aaapertu, String status, String nmsinies) throws Exception;	//(EGS)
	
	/**
	 * Validamos que exista una autorizacion especial por excedente de medicamentos. (EGS)
	 * @param nmautesp
	 * @param ntramite
	 * @param nfactura
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @param nmsuplem
	 * @param nmsituac
	 * @param nmsinies
	 * @return
	 * @throws Exception
	 */
	public String validaAutEspLimMedi(String nmautesp, String ntramite, String nfactura, String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String nmsituac, String nmsinies) throws Exception; //(EGS)
	
	public List<Map<String, String>> obtieneListaDatosRenovaSiniestralidad(String pv_CdUniEco_i 
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_nmsinies
			,String pv_fecdesde  
			,String pv_fechasta  
			,String pv_start_i
			,String pv_limit_i 
			,String pv_ntramite_i) throws Exception;

	public List<Map<String, String>> obtieneListaTopIcd(String pv_CdUniEco_i, String pv_CdRamo_i, String pv_nmpoliza_i,
			String pv_cdperson, String pv_nmsinies, String pv_fecdesde, String pv_fechasta, String pv_top) throws Exception;

	public List<Map<String, String>> obtieneListaReservas(String pv_CdUniEco_i, String pv_CdRamo_i, String pv_nmpoliza_i,
			String pv_cdperson, String pv_nmsinies, String pv_fecdesde, String pv_fechasta) throws Exception;

	public List<Map<String, String>> obtieneListaReservasSolo(String pv_CdUniEco_i, String pv_CdRamo_i, String pv_nmpoliza_i,
			String pv_cdperson, String pv_nmsinies, String pv_fecdesde, String pv_fechasta) throws Exception;
	
	public List<GenericVO> obtieneListadoCPTUnico(String cdicd) throws Exception;
	
	public List<GenericVO> obtieneListadoTipoMedicos(String cdicd, String cpts) throws Exception;
	
	public String obtieneImporteArancelGNP(String cdpresta,String cpt) throws Exception;
	
	public List<GenericVO> obtieneListadoTiposProveedores() throws Exception;

	public List<Map<String, String>> obtieneListaProveedores(String pv_cdpresta_i, String pv_idespecialidad_i, String pv_tipoProveedor_i,
			String pv_idZonaHosp_i) throws Exception;
	
	public List<Map<String, String>> obtieneListadoDetalleSiniestro(   String pv_CdUniEco_i
			,String pv_CdRamo_i  
			,String pv_nmpoliza_i
			,String pv_cdperson  
			,String pv_ntramite_i
			,String pv_nmsinies  
			,String pv_fecdesde  
			,String pv_fechasta) throws Exception;
	
	public String obtieneMedicoEquipoQuirurgico(String numAutorizacion) throws Exception;
	
	public List<AutorizacionServicioVO> guardarAutorizacionServicioGNP(Map<String, Object> paramsR) throws Exception;
	
	public String obtieneDatosGeneralesICD(String cdunieco, String cdramo, String estado, String nmpoliza, String cdicd, String cdperson)throws Exception;
	
	public List<Map<String, String>> obtieneListaDatosSumaAseguradaGNP(HashMap<String, Object> params) throws Exception;
	
	public String obtenerValidacionExclusionICD(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem, String nmsituac, String cdicd)throws Exception;
}