package mx.com.gseguros.portal.general.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.siniestros.model.CoberturaPolizaVO;
import mx.com.gseguros.portal.siniestros.model.ConsultaProveedorVO;
import mx.com.gseguros.portal.siniestros.service.SiniestrosManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

/**
 * 
 * @author Ricardo
 *
 */
public class CatalogosAction extends PrincipalCoreAction {
	
	private static final long serialVersionUID = 384960409053296809L;

	private Logger logger = Logger.getLogger(CatalogosAction.class);
	
	private CatalogosManager        catalogosManager;
	private EndososManager          endososManager;
	private KernelManagerSustituto  kernelManager;
	private SiniestrosManager 	    siniestrosManager;
    private StoredProceduresManager storedProceduresManager; 
	
    private boolean success;
    
    /**
     * true si queremos la propiedad "listaGenerica", 
     * false si queremos la propiedad "lista"
     */
    private boolean catalogoGenerico;
    
    /**
     * Nombre del catalogo a obtener
     */
    private String catalogo;
    
    /**
     * Lista con elementos de tipo "key","value" del cat&aacute;logo solicitado
     */
    private List<GenericVO> lista = new ArrayList<GenericVO>(0);
    
    /**
     * Lista de elementos a cargar en grid por tipo Map
     */
    private List<Map<String, String>> loadList;
    
    /**
     * Lista para guardar varios elementos
     */
    private List<Map<String, String>> saveList;

    private List<Map<String, String>> saveList2;
    
    /**
     * Lista personalizada, puede contener cualquier tipo de objeto 
     */
    private List<?> listaGenerica;
    
    /**
     * Parametros enviados a los catalogos
     */
    private Map<String, String> params;

    /**
     * Mensaje de respuesta del servicio
     */
    private String msgRespuesta;
    
    /**
     * Obtiene el catalogo solicitado en forma de una lista de VOs con llave y valor
     * @return
     * @throws Exception
     */
    public String obtieneCatalogo() throws Exception {
    	logger.debug("catalogo=" + catalogo);
    	logger.debug("params="   + params);
        try {
        	Catalogos cat = Catalogos.valueOf(catalogo);
        	switch(cat) {
        	
        		case AGENTES:
        			lista = catalogosManager.obtieneAgentes(params!=null?params.get("agente"):null);
        			break;
        		case AGENTE_ESPECIFICO:
        			lista = catalogosManager.obtieneAgenteEspecifico(params!=null?params.get("agente"):null);
        			logger.debug("<=== AGENTE_ESPECIFICO  ====>"+lista);
        			break;
        		case AGENTES_POR_PROMOTOR:
        			lista = catalogosManager.cargarAgentesPorPromotor(params.get("cdusuari"));
        			break;
        		case COLONIAS:
					lista = catalogosManager.obtieneColonias(params!=null?params.get("cp"):null);
					break;
        		case MUNICIPIOS:
					lista = catalogosManager.obtieneMunicipios(params.get("cdestado"));
					break;
        		case ZONAS_POR_PRODUCTO:
					lista = catalogosManager.obtieneZonasPorModalidad(params.get("cdtipsit"));
					break;
        		case ANIOS_RENOVACION:
        		case CATCONCEPTO:
        		case CAUSA_SINIESTRO:
        		//case CAUSA_SINIESTROC:
        		//case TIPO_CONSULTA:
        		case DESTINOPAGO:
        		case FORMAS_ASEGURAMIENTO:
        		case GIROS:
				case MC_TIPOS_TRAMITE:
				case MESES:
				case MOTIVOS_CANCELACION:
				case NACIONALIDAD:
				case TIPOS_DOMICILIO:
				case PENALIZACIONES:
				case PLANES:
				case REFERENCIAS_TRAMITE_NUEVO:
				case RELACION_CONT_ASEG:
				case REPARTO_PAGO_GRUPO:
				case ROLES_POLIZA:
				case SEXO:
				case STATUS_POLIZA:
				case TERRORWS:
				case TIPOS_PAGO_POLIZA:
				case TIPOS_PAGO_POLIZA_SIN_DXN:
				case TIPOS_PERSONA:
				case TIPOS_POLIZA:
				case TIPOS_POLIZA_AUTO:
				case TIPO_ATENCION_SINIESTROS:
				case TIPO_CONCEPTO_SINIESTROS:
				case TIPO_MONEDA:
				case TIPO_MENU:
				case TIPO_PAGO_SINIESTROS:
				case TIPO_RESIDENCIA:
				case TRATAMIENTOS:
				case TCUMULOS:
				case TCANALIN:
				case TESTADOS:
				case TZONAS:
				case TEDOCIVIL:
				case OCUPACION:
				case TFORMATOS:
				case STATUSINIESTROS:
				case STATUS_VIGENCIA_POL:
				case TRAZCANAU:
				case FORMATOFECHA:
				case TIPOS_IMP_LOTE:
				case TIPOS_LOTE_IMPR:
				case ORDEN_IMPRESION:
				case CONFLAYOUT:
				case TIPO_RECIBOS_IMPRESION:
				case ETAPAS_ESTADO_FLUJO:
				case TIPO_BUSQUEDA_RENOVACION_INDIVIDUAL:
				case CRITERIOS_RENOVACION_INDIVIDUAL:	
				case CATALOGO_TRAFUDOC_CDFUNCI:
				case TIPOS_PAGO_POLIZA_SIN_DXN_MULTIANUAL:
				case CATALOGO_ESTADOS_RECIBO:
				case ZONAS_SUCURSALES:
				case NIVELES_SUCURSALES:
				case TAPOYO:
				case TESPECIALIDADES:
				case ZONASHOSPITALARIA:
					lista = catalogosManager.getTmanteni(cat);
	                break;
				case TIPOEVENTOGNP:
					lista = catalogosManager.getTmanteni(cat);
					logger.debug("Valor de la lista TIPOEVENTOGNP :"+lista);
	                break;
				case CVECOLUMNA:
					lista = catalogosManager.obtieneAtributosExcel(cat);
	                break;
				case MC_SUCURSALES_ADMIN:
				case MC_SUCURSALES_DOCUMENTO:
					String padre    = null;
					String cdusuari = null;
					if(params!=null)
					{
						padre    = params.get("idPadre");
						cdusuari = params.get("cdusuari");
					}
					lista = catalogosManager.obtieneSucursales(padre,cdusuari);
					break;
				case MC_SUCURSALES_SALUD:
					String padreSalud = "1000";
					if(params!=null)
					{
						padreSalud = params.get("idPadre");
					}
					lista = catalogosManager.obtieneSucursales(padreSalud,null);
					break;
				case MC_ESTATUS_TRAMITE:
					lista = catalogosManager.obtieneStatusTramite(params);
					break;
				case TATRISIT:
					//lista = catalogosManager.obtieneAtributosSituacion(params.get("cdatribu"), params.get("cdtipsit"), params.get("idPadre"));
					//para contemplar atributos situacion por rol (EGS)
					lista = catalogosManager.obtieneAtributosSituacion(params.get("cdatribu"), params.get("cdtipsit"), params.get("idPadre"),((UserVO) session.get("USUARIO")).getRolActivo().getClave());
					break;
				case TATRISIN:
		            lista = catalogosManager.obtieneAtributosSiniestro(params.get("cdatribu"), params.get("cdtipsit"), params.get("idPadre"));
					break;
				case TATRIPOL:
			        lista = catalogosManager.obtieneAtributosPoliza(params.get("cdatribu"), params.get("cdramo"), params.get("idPadre"));
					break;
				case TATRIGAR:
					//lista = catalogosManager.obtieneAtributosGarantia(params.get("cdatribu"), params.get("cdtipsit"), params.get("cdramo"), params.get("idPadre"), params.get("cdgarant"));
					// se agrega par�metro cdSisrol para considerar restricciones por rol (EGS)
					lista = catalogosManager.obtieneAtributosGarantia(params.get("cdatribu"), params.get("cdtipsit"), params.get("cdramo"), params.get("idPadre"), params.get("cdgarant"),((UserVO) session.get("USUARIO")).getRolActivo().getClave());
					break;
				case TATRIPER:
			        lista = catalogosManager.obtieneAtributosRol(params.get("cdatribu"), params.get("cdtipsit"), params.get("cdramo"), params.get("idPadre"), params.get("cdrol"));
					break;
				case RAMOS:
					List<Map<String,String>>ramos=kernelManager.obtenerRamos(params!=null?params.get("idPadre"):null);
					lista=new ArrayList<GenericVO>(0);
					for(Map<String,String> ramo:ramos) {
						lista.add(new GenericVO(ramo.get("cdramo"), ramo.get("dsramo")));
					}
					
					if(!lista.isEmpty() && params != null && params.containsKey("aniadeComodinTodos") && Constantes.SI.equalsIgnoreCase(params.get("aniadeComodinTodos"))){
						lista.add(0,new GenericVO("-1", " ---- Todos ---- "));
					}
					
					break;
				case RAMOSALUD:
					lista =siniestrosManager.getConsultaListaRamoSalud();
					break;
				case TIPSIT:
					List<Map<String,String>>tipsits=kernelManager.obtenerTipsit(params!=null?params.get("idPadre"):null);
					lista=new ArrayList<GenericVO>(0);
					for(Map<String,String> tipsit:tipsits) {
						lista.add(new GenericVO(tipsit.get("CDTIPSIT"), tipsit.get("DSTIPSIT")));
					}
					
					if(!lista.isEmpty() && params != null && params.containsKey("aniadeComodinTodos") && Constantes.SI.equalsIgnoreCase(params.get("aniadeComodinTodos"))){
						lista.add(0,new GenericVO("*", " ---- Todos ---- "));
					}
					
					break;
				case ROLES_SISTEMA:
					String filtro = null;
					if(params!=null){
						filtro = params.get("dsRol");
					}
					lista = catalogosManager.obtieneRolesSistema(filtro);
					break;
				case ENDOSOS:
					UserVO usuario = (UserVO)session.get("USUARIO");
					List<Map<String, String>> nombresEndosos = endososManager.obtenerNombreEndosos(
								usuario.getRolActivo().getClave(),
								Integer.parseInt(params.get("cdramo")), 
								params.get("cdtipsit"));
					lista=new ArrayList<GenericVO>(0);
					for(Map<String,String> nombre:nombresEndosos) {
						lista.add(new GenericVO(nombre.get("CDTIPSUP"), nombre.get("DSTIPSUP")));
					}
					break;
				case SINO:
					lista=new ArrayList<GenericVO>(0);
					lista.add(new GenericVO("N", "NO"));
					lista.add(new GenericVO("S", "SI"));
					break;
				case PLANES_X_PRODUCTO:
					LinkedHashMap<String,Object>param=new LinkedHashMap<String,Object>();
					param.put("param1",params.get("cdramo"));
					param.put("param2",params.get("cdtipsit"));
					List<Map<String,String>>listaPlanes=storedProceduresManager.procedureListCall(
							ObjetoBD.OBTIENE_PLANES_X_PRODUCTO.getNombre(), param, null);
					lista=new ArrayList<GenericVO>();
					for(Map<String,String> plan:listaPlanes)
					{
						lista.add(new GenericVO(plan.get("CDPLAN"), plan.get("DSPLAN")));
					}
					break;
				case COBERTURAS:
					lista = siniestrosManager.obtieneListadoCobertura(params.get("cdramo"), params.get("cdtipsit"));
					break;
				case COBERTURASTOTALES:
					lista = siniestrosManager.obtieneListadoCoberturaTotales();
					break;
				case COBERTURASXVALORES:
					try{
						if(params!=null){
							logger.debug("Valor de params ==>"+params);
							String tipoPago = params.get("tipopago").toString();
							if(TipoPago.DIRECTO.getCodigo().equals(tipoPago)){
								//Verificamos en tworksin y si no en msiniest
								List<Map<String,String>> datosCobertura = siniestrosManager.obtenerDatosAdicionalesCobertura(params.get("ntramite"));
								HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
								paramCobertura.put("pv_ntramite_i",params.get("ntramite"));
								paramCobertura.put("pv_tipopago_i",params.get("tipopago"));
								paramCobertura.put("pv_nfactura_i",null);
								paramCobertura.put("pv_cdunieco_i",datosCobertura.get(0).get("CDUNIECO"));
								paramCobertura.put("pv_estado_i",datosCobertura.get(0).get("ESTADO"));
								paramCobertura.put("pv_cdramo_i",datosCobertura.get(0).get("CDRAMO"));
								paramCobertura.put("pv_nmpoliza_i",datosCobertura.get(0).get("NMPOLIZA"));
								paramCobertura.put("pv_nmsituac_i",datosCobertura.get(0).get("NMSITUAC"));
								paramCobertura.put("pv_cdgarant_i",params.get("cdgarant"));
								
								List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaListaCoberturaPoliza(paramCobertura);
								lista=new ArrayList<GenericVO>(0);
								for(CoberturaPolizaVO nombre:listaCobertura) {
									lista.add(new GenericVO(nombre.getCdgarant(), nombre.getDsgarant()));
								}
								break;
								
							}else{
								String ntramite = params.get("ntramite");
								Map<String,String> paramsRes = (HashMap<String, String>) siniestrosManager.obtenerLlaveSiniestroReembolso(ntramite);
								
								for(Entry<String,String>en:paramsRes.entrySet()){
									params.put(en.getKey().toLowerCase(),en.getValue());
								}
								
								HashMap<String, Object> paramCobertura = new HashMap<String, Object>();
								paramCobertura.put("pv_ntramite_i",params.get("ntramite"));
								paramCobertura.put("pv_tipopago_i",params.get("tipopago"));
								paramCobertura.put("pv_nfactura_i",null);
								paramCobertura.put("pv_cdunieco_i",params.get("cdunieco"));
								paramCobertura.put("pv_estado_i",params.get("estado"));
								paramCobertura.put("pv_cdramo_i",params.get("cdramo"));
								paramCobertura.put("pv_nmpoliza_i",params.get("nmpoliza"));
								paramCobertura.put("pv_nmsituac_i",params.get("nmsituac"));
								paramCobertura.put("pv_cdgarant_i",params.get("cdgarant"));
								
								List<CoberturaPolizaVO> listaCobertura = siniestrosManager.getConsultaListaCoberturaPoliza(paramCobertura);
								lista=new ArrayList<GenericVO>(0);
								for(CoberturaPolizaVO nombre:listaCobertura) {
									lista.add(new GenericVO(nombre.getCdgarant(), nombre.getDsgarant()));
								}
								//lista.add(new GenericVO("18CN", "Cobertura no amparada"));
								break;
							}
						}
					}catch(Exception ex){
						logger.error("error al obtener clave de siniestro para la pantalla del tabed panel",ex);
					}
				
				case SUBCOBERTURAS:
					String cdunieco = null;
					String estado = null;
					String cdramo = null;
					String nmpoliza = null;
					String nmsituac = null;
					String cdgarant = null;
					String cdsubcob = null;
					String cdtipsit = null;
					UserVO usuarioR		= (UserVO)session.get("USUARIO");
					String cdrol		= usuarioR.getRolActivo().getClave();
					
					if(params!=null)
					{
						logger.debug("Valores de entrada --> "+params);
						cdunieco = params.get("cdunieco");
						estado = params.get("estado");
						cdramo = params.get("cdramo");
						nmpoliza = params.get("nmpoliza");
						nmsituac = params.get("nmsituac");
						cdsubcob = params.get("cdsubcob");
						cdtipsit = params.get("cdtipsit");
						
						
						if(params.get("cdgarant")!=null)
						{
							cdgarant = params.get("cdgarant");
						}
						else if(params.get("idPadre")!=null)
						{
							cdgarant = params.get("idPadre");
						}
					}
					lista = siniestrosManager.getConsultaListaSubcobertura(cdunieco, cdramo, estado, nmpoliza, nmsituac, cdtipsit, cdgarant, cdsubcob,cdrol);
					break;
					
				case SUBCOBERTURASTOTALES:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotales();
					break;
				case SUBCOBERTURAS4MS:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotalesMultisalud("MS");
					logger.debug("Valor de lista==>"+lista.size());
					logger.debug(lista);
					break;
				case SUBCOBERTURAS4MSC:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotalesMultisalud("MSC");
					logger.debug("Valor de lista==>"+lista.size());
					logger.debug(lista);
					break;
				case SUBCOBERTURASGMPI:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotalesMultisalud("GMPI");
					logger.debug("Valor de lista==>"+lista.size());
					logger.debug(lista);
					break;
				case SUBCOBERTURASGMPC:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotalesMultisalud("GMPC");
					logger.debug("Valor de lista==>"+lista.size());
					logger.debug(lista);
					break;
				case SUBCOBERTURASINFONAVIT:
					lista = siniestrosManager.getConsultaListaSubcoberturaTotalesMultisalud("SSI");
					logger.debug("Valor de lista==>"+lista.size());
					logger.debug(lista);
					break;
				case SUBCOBERTURASRECUPERA:
					lista = siniestrosManager.getConsultaListaSubcoberturaRecupera();
					break;
				case MEDICOS:
					List<ConsultaProveedorVO> medicos = siniestrosManager.getConsultaListaProveedorMedico(
							TipoPrestadorServicio.MEDICO.getCdtipo(), params != null ? params.get("cdpresta") : null);
					if(catalogoGenerico) {
						listaGenerica = medicos;
					} else {
						lista = new ArrayList<GenericVO>();
						for(ConsultaProveedorVO medico : medicos) {
							lista.add(new GenericVO(medico.getCdpresta(),medico.getNombre()));
						}
					}
					break;
				case MEDICOESPECIFICO:
					List<ConsultaProveedorVO> medicoes = siniestrosManager.getConsultaListaProveedorMedico(
							"ES", params != null ? params.get("cdpresta") : null);
					if(catalogoGenerico) {
						listaGenerica = medicoes;
					} else {
						lista = new ArrayList<GenericVO>();
						for(ConsultaProveedorVO medico : medicoes) {
							lista.add(new GenericVO(medico.getCdpresta(),medico.getNombre()));
						}
					}
					break;
				case PROVEEDORES:
					List<ConsultaProveedorVO> provs = siniestrosManager.getConsultaListaProveedorMedico(
							TipoPrestadorServicio.CLINICA.getCdtipo(), params != null ? params.get("cdpresta") : null);
					if(catalogoGenerico) {
						listaGenerica = provs;
					} else {
						lista = new ArrayList<GenericVO>();
						for(ConsultaProveedorVO prov : provs) {
							lista.add(new GenericVO(prov.getCdpresta(),prov.getNombre()));
						}
					}
					break;
				case TTIPOPAGO:
					lista = siniestrosManager.getConsultaListaTipoPago(params.get("cdramo"));
					break;
				case ICD:
					lista = siniestrosManager.getConsultaListaCPTICD(cat.getCdTabla(),params.get("otclave"));
					break;
				case CODIGOS_MEDICOS:
					String idconcep = null;
					String descripc = null;
					if(params!=null)
					{
						idconcep = params.get("idPadre");
						descripc = params.get("descripc");
					}
					lista = siniestrosManager.obtenerCodigosMedicos(idconcep, descripc);
					break;
				case CODIGOS_MEDICOS_TOTALES:
					lista = siniestrosManager.obtenerCodigosMedicosTotales();
					break;
				case MOTIVOS_RECHAZO_SINIESTRO:
					lista=new ArrayList<GenericVO>();
					List<Map<String,String>>listaMotivos=siniestrosManager.loadListaRechazos();
					for(Map<String,String>ele : listaMotivos)
					{
						lista.add(new GenericVO(ele.get("CDMOTIVO"),ele.get("DSMOTIVO")));
					}
					break;
				case SUBMOTIVOS_RECHAZO_SINIESTRO:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					params.put("pv_cdmotivo_i",params.get("idPadre"));
					lista=new ArrayList<GenericVO>();
					List<Map<String,String>>listaSubMotivos=siniestrosManager.loadListaIncisosRechazos(params);
					for(Map<String,String>ele : listaSubMotivos)
					{
						lista.add(new GenericVO(ele.get("CDCAUMOT"),ele.get("DSCAUMOT")));
					}
					break;
				case ROLES_RAMO:
					LinkedHashMap<String,Object>params2=new LinkedHashMap<String,Object>();
					params2.putAll(params);
					List<Map<String,String>>lista2=storedProceduresManager.procedureListCall("PKG_LISTAS.P_GET_ROLES_X_RAMO", params2, null);
					lista=new ArrayList<GenericVO>();
					for(Map<String,String>iElem : lista2)
					{
						lista.add(new GenericVO(iElem.get("CDROL"),iElem.get("DSROL")));
					}
					break;
				case SERVICIO_PUBLICO_AUTOS:
					lista=catalogosManager.cargarServicioPublicoAutos(params.get("substr"),params.get("cdramo"),params.get("cdtipsit"));
					break;
				case DESCUENTO_POR_AGENTE:
					if(params==null)
					{
						lista=new ArrayList<GenericVO>();
						lista.add(new GenericVO("0","0%"));
					}
					else
					{
						lista=catalogosManager.cargarDescuentosPorAgente(
								params.get("tipoUnidad")
								,params.get("uso")
								,"9"//zona
								,"13"//promotoria
								,params.get("cdagente")
								,params.get("cdtipsit")
								,params.get("cdatribu")
								);
					}
					break;
				case SERVICIO_PUBLICO_NEGOCIO:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarListaNegocioServicioPublico(
							params.get("cdtipsit")
							,params.get("cdatribu")
							,params.get("tipoUnidad")
							,params.get("cdagente")
							);
					break;
				case RAMO_5_MODELOS_X_SUBMARCA:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarModelosPorSubmarcaRamo5(
							params.get("idPadre")
							,params.get("cdtipsit")
							);
					break;
				case RAMO_5_VERSIONES_X_MODELO:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarVersionesPorModeloSubmarcaRamo5(
							params.get("submarca")
							,params.get("modelo")
							,params.get("cdtipsit")
							);
					break;
				case RAMO_5_AUTOS:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					String cadena    = params.get("cadena");
					String cdtipsit2 = params.get("cdtipsit");
					String servicio  = params.get("servicio");
					String uso       = params.get("uso");
					if(
							StringUtils.isBlank(cadena)
							||StringUtils.isBlank(cdtipsit2)
							||(
									!"@".equals(cadena.substring(0, 1))
									&&
									(
											StringUtils.isBlank(servicio)
											||StringUtils.isBlank(uso)
									)
							)
					)
					{
						logger.error("No estan todos los datos");
						lista = new ArrayList<GenericVO>();
					}
					else
					{
						lista = catalogosManager.cargarAutosPorCadenaRamo5(
								cadena
								,cdtipsit2
								,servicio
								,uso
								);
					}
					break;
				case RAMO_5_MARCAS:
				case RAMO_5_SUBMARCAS:
				case RAMO_5_TIPOS_USO:
				case RAMO_5_VERSIONES:
				case RAMO_5_TIPOS_CARGA:
					lista = catalogosManager.cargarTtapvat1(cat.getCdTabla());
					break;
				case RAMO_5_NEGOCIO_X_CDTIPSIT:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarNegocioPorCdtipsitRamo5(params.get("cdtipsit"));
					break;
				case RAMO_5_USOS_X_NEGOCIO:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarUsosPorNegocioRamo5(
							params.get("cdnegocio")
							,params.get("cdtipsit")
							,params.get("servicio")
							,params.get("tipocot")
							);
					break;
				case RAMO_5_MARCAS_X_NEGOCIO:
					if(params==null)
					{
						params=new HashMap<String, String>();
					}
					lista = catalogosManager.cargarMarcasPorNegocioRamo5(params.get("cdnegocio"),params.get("cdtipsit"));
					break;
				case RAMO_5_NEGOCIO_X_AGENTE:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.cargarNegociosPorAgenteRamo5(
							params.get("cdagente")
							,params.get("cdsisrol")
							,params.get("tipoflot")
							);
					break;
				case RAMO_5_CARGAS_X_NEGOCIO:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					UserVO usuarioCargaRamo5 = (UserVO)session.get("USUARIO");
					lista = catalogosManager.cargarCargasPorNegocioRamo5(usuarioCargaRamo5.getRolActivo().getClave(),params.get("negocio"));
					break;
				case RAMO_5_PLAN_X_NEGOCIO_TIPSIT_TIPOVEHI:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.cargarPlanesPorNegocioModeloClavegsRamo5(
							params.get("cdtipsit")
							,params.get("modelo")
							,params.get("negocio")
							,params.get("clavegs")
							,params.get("servicio")
							,params.get("tipoflot")
							);
					break;
				case RAMO_5_NEGOCIO_X_CDTIPSIT_AGENTE:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista=catalogosManager.cargarNegociosPorTipoSituacionAgenteRamo5(
							params.get("cdtipsit")
							,params.get("cdagente")
							,params.get("producto")
							,params.get("cdsisrol")
							);
					break;
				case RAMO_5_TIPOS_SITUACION_X_NEGOCIO:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista=catalogosManager.cargarTiposSituacionPorNegocioRamo5(params.get("negocio"),params.get("producto"));
					break;
				case CUADROS_POR_SITUACION:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista=catalogosManager.cargarCuadrosPorSituacion(params.get("cdtipsit"));
					break;
				case CLAUSULAS_POLIZA:
					lista=new ArrayList<GenericVO>();
					lista.add(new GenericVO("1" , "uno"));
					lista.add(new GenericVO("2" , "dos"));
					lista.add(new GenericVO("3" , "tres"));
					break;
				case RAMO_4_SUMA_ASEG:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista=catalogosManager.cargarSumaAseguradaRamo4(params.get("cdsisrol"),params.get("cdplan"));
					break;
				case TIPO_SERVICIO_X_AUTO:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTiposServicioPorAuto(
							params.get("cdunieco")
							,params.get("cdramo")
							,params.get("estado")
							,params.get("nmpoliza")
							,params.get("nmsituac")
							,params.get("nmsuplem")
							);
					break;
				case RAMO_5_TIPOS_VALOR_X_ROL:
					if(params==null)
					{
						params = new HashMap<String,String>();
					}
					if(session!=null
							&&session.get("USUARIO")!=null)
					{
						lista = catalogosManager.recuperarListaTiposValorRamo5PorRol(
								params.get("cdtipsit")
								,((UserVO)session.get("USUARIO")).getRolActivo().getClave()
								,((UserVO)session.get("USUARIO")).getUser()
								);
					}
					break;
				case CONCEPTOPAGO:
					logger.debug("Entra a concepto de Pago");
					lista = siniestrosManager.getConsultaListaConceptoPago(params.get("cdramo"));
					logger.debug("Salida : "+lista.size() +" : "+lista);
					break;
				case ESTD_MODULOS:
					lista = catalogosManager.recuperarModulosEstadisticas();
					break;
				case ASEGURADOS:
					lista = siniestrosManager.getConsultaListaAsegurado(params != null ? params.get("cdperson") : null);
					break;
				case PARENTESCO:
					lista = catalogosManager.obtieneListaParentesco();
					break;
				case ESTD_TAREAS:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTareasEstadisticas(params.get("idPadre"));
					break;
				case SECUENCIA_IVA:
					lista=new ArrayList<GenericVO>(0);
					lista.add(new GenericVO("A", "IVA ANTES DE COPAGO"));
					lista.add(new GenericVO("D", "IVA DESPUES DE COPAGO"));
					break;
				case ATRIBUTOLAYOUT:
					/*String idconcep = null;
					String descripc = null;
					if(params!=null)
					{
						idconcep = params.get("idPadre");
						descripc = params.get("descripc");
					}*/
					lista = siniestrosManager.obtenerAtributosLayout(null);
					break;
				case AGRUPADOR_POLIZA:
					lista = catalogosManager.recuperarListaPools();
					break;
				case GRUPOS_POLIZA:
					if(params!=null)
					{
						lista = catalogosManager.recuperarGruposPoliza(
								params.get("cdunieco")
								,params.get("cdramo")
								,params.get("estado")
								,params.get("nmpoliza")
								);
					}
					break;
				case SUBRAMOS:
					if(params!=null)
					{
						lista = catalogosManager.recuperarSubramos(
								params.get("idPadre")
								);
					}
					break;
				case TIPOS_RAMO:
					lista = catalogosManager.recuperarTiposRamo();
					break;
				case RAMOS_X_TIPORAMO:
					if(params!=null)
					{
						lista = catalogosManager.recuperarRamosPorTipoRamo(params.get("idPadre"));
					}
					break;
				case SUCURSALES_X_TIPORAMO:
					if(params!=null)
					{
						lista = catalogosManager.recuperarSucursalesPorTipoRamo(params.get("idPadre"));
					}
					break;
				case USUARIOS:
					if(params!=null&&params.containsKey("cadena")&&StringUtils.isNotBlank(params.get("cadena")))
					{
						lista = catalogosManager.recuperarComboUsuarios(params.get("cadena"));
					}
					break;
				case SUCURSALES_PERMISO_IMPRESION:
					if(params!=null)
					{
						UserVO usuario2 = (UserVO)session.get("USUARIO");
						lista = catalogosManager.recuperarSucursalesPermisoImpresion(
								params.get("idPadre")
								,usuario2.getUser()
								,usuario2.getCdUnieco()
								);
					}
					break;
				case TTIPTRAMC:
					lista = catalogosManager.recuperarTtiptramc();
					break;
				case FLUJO_STATUS:
					if(params!=null&&StringUtils.isNotBlank(params.get("agrupamc")))
					{
						lista = catalogosManager.recuperarTestadomcPorAgrupamc(params.get("agrupamc"),params.get("extras"));
					}
					break;
				case TTIPFLUMC:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTtipflumc(params.get("agrupamc"));
					break;
				case TFLUJOMC:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTflujomc(params.get("idPadre"),params.get("swfinal"));
					break;
				case TTIPSUPL:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTtipsupl(params.get("idPadre"),params.get("ninguno"));
					break;
				case PARENTESCOAUTO:
					lista = catalogosManager.obtieneCatalogoParentescoAutos();
					break;
				case RECUPERAR_TDOCUME:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTdocume(params.get("cdtiptra"));
					break;
				case COTIZADORES_ACTIVOS:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.cargarCotizadoresActivos(params.get("cadena"));
					break;
				case MOTIVOS_REEXPEDICION:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					lista = catalogosManager.obtieneMotivosReexp(params.get("cdramo"), params.get("cdtipsit"));
					break;
				case FORMAS_PAGO_POLIZA_POR_RAMO_TIPSIT:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarFormasDePagoPorRamoTipsit(params.get("cdramo"), params.get("cdtipsit"));
					break;
				case CLIENTES:
					if(params!=null)
					{
						lista = catalogosManager.recuperarClientesPorNombreApellido(params.get("cadena"));
					}
					break;
				case CATALOGO_CONVENIOS:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.obtieneCatalogoConvenios();
					break;
				case CATALOGO_CONTRATANES_SALUD:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperaContratantesSalud(params.get("nombre"));
					break;
				case CATALOGO_RAMOS_COLECTIVOS_X_TIPO_RAMO:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					logger.debug(Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
							   "\n@@@@@@ recuperaRamosColectivoTipoRamo @@@@@@"));
					lista = catalogosManager.recuperarTipoRamoColectivo(params.get("cdtipram"));
					break;
				case CATALOGO_TIPOS_SITUACION_COLECTIVOS_X_RAMO:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarTipoRamoSituacionColectivo(params.get("cdtipram"), params.get("idPadre"));
					break;		
				case RECUPERAR_LISTA_FILTRO_PROPIEDADDES_INCISO:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarListaFiltroPropiedadesInciso(params.get("cdunieco"),params.get("cdramo"),params.get("estado"),params.get("nmpoliza"));
					break;
				case RECUPERAR_LISTA_FILTRO_PROPIEDAD_INCISO:
                    if(params == null)
                    {
                        params = new HashMap<String,String>();
                    }
                    lista = catalogosManager.recuperarListaFiltroPropiedadInciso(params.get("cdramo"),params.get("cdtipsit"),params.get("nivel"));
                    break;
				case CATALOGO_CERRADO: //ESTE CATALOGO SOLO REGRESA SUS 5 PARES DE PARAMS COMO 5 RECORDS PARA EL STORE
					if(params == null)
					{
						throw new ApplicationException("Para el catalogo CATALOGO_CERRADO se requieren parametros");
					}
					lista = new ArrayList<GenericVO>();
					for(Entry<String,String> en:params.entrySet())
					{
						if("_".equals(en.getKey().substring(0, 1)))
						{
							lista.add(new GenericVO(en.getKey().substring(1),en.getValue()));
						}
					}
					break;
				case TTIPFLUMC_ROL_USR:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					UserVO user_TTIPFLUMC_ROL_USR = Utils.validateSession(session);
					lista = catalogosManager.recuperarTtipflumcPorRolPorUsuario(params.get("agrupamc"),user_TTIPFLUMC_ROL_USR.getRolActivo().getClave(),user_TTIPFLUMC_ROL_USR.getUser());
					break;
				case TFLUJOMC_X_ROL_USR:
					if(params==null)
					{
						params=new HashMap<String,String>();
					}
					UserVO user_TFLUJOMC_X_ROL_USR = Utils.validateSession(session);
					lista = catalogosManager.recuperarTflujomcPorRolPorUsuario(
							params.get("idPadre")
							,params.get("swfinal")
							,user_TFLUJOMC_X_ROL_USR.getRolActivo().getClave()
							,user_TFLUJOMC_X_ROL_USR.getUser()
							);
					break;
				case SUCURSALES_X_FLUJO:
					if(params==null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperarSucursalesPorFlujo(params.get("idPadre"));
					break;
				case RAMO_X_UNIECO_X_GRUPO:
					if (params != null && StringUtils.isNotBlank(params.get("idPadre")) && StringUtils.isNotBlank(params.get("tipogrupo"))) {
						lista = catalogosManager.recuperarRamosPorSucursalPorTipogrupo(params.get("idPadre"),params.get("tipogrupo"));
					}
					break;
				case TIPSIT_X_RAMO_X_GRUPO:
					if (params != null && StringUtils.isNotBlank(params.get("idPadre")) && StringUtils.isNotBlank(params.get("tipogrupo"))) {
						lista = catalogosManager.recuperarTipsitPorRamoPorTipogrupo(params.get("idPadre"),params.get("tipogrupo"));
					}
					break;
				case TIPOS_ENDOSO_X_CDRAMO_X_CDTIPSIT:
					if(params != null)
					{
						lista = catalogosManager.recuperarTiposDeEndosoPorCdramoPorCdtipsit(
							params.get("cdramo"),
							params.get("cdtipsit"),
							StringUtils.isNotBlank(params.get("vigente"))
							    ? params.get("vigente")
							    : "N"
					    );
					}
					break;
				case MOTIVOS_RECHAZO_TRAMITE:
					String ntramite = null;
					if (params != null) {
						ntramite = params.get("ntramite");
					}
					lista = catalogosManager.recuperarMotivosRechazo(ntramite);
					break;
				case CATALOGO_CONTRATANTES:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperaContratantes(params.get("cdunieco"), params.get("cdramo"), params.get("cadena"));
					break;
				case CATALOGO_CONTRATANTES_RFC:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperaContratantesRfc(params.get("cadena"));
					break;
				case TAPOYO1:
					if(params == null)
					{
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.recuperaTablaApoyo1(params.get("cdtabla"));
					break;
				case CAMPOS_EXCLUSION_RENOVACION:
					lista = catalogosManager.recuperaCamposExclusionRenovacion();
					break;
				case CATALOGO_ATRIPOL_DSATRIBU:
					if(params == null){
						params = new HashMap<String,String>();
					}
					lista = catalogosManager.obtieneCatalogoDescAtrib(params.get("cdramo"), params.get("dsatribu"), params.get("idPadre"));
					break;
				case TRETXADMINAGE:
				    if(params == null){
                        params = new HashMap<String,String>();
                    }
				    lista=catalogosManager.obtieneCatalogoRetAdminAgente(params.get("idPadre"), params.get("cdagente"));
				    break;
				case CLAVE_DESCUENTO_SUBRAMO:
				    if(params == null){
                        params = new HashMap<String,String>();
                    }
                    lista=catalogosManager.obtieneClaveDescuentoSubRamo(params.get("administradora"), params.get("idPadre"),params.get("cdramo"), params.get("cdtipsit"));
				    
				    break;
				case IDCIERRES:
                    lista = catalogosManager.obtieneIdsCierres();
                    break;
				case ADMINISTRADORAXAGENTE:
				    if(params == null){
                        params = new HashMap<String,String>();
                    }
				    lista=catalogosManager.obtieneAdministradoraXAgente(params.get("cdagente"));
				    break;
				case VALIDACIONESGRALES:
                    lista = siniestrosManager.getConsultaListaValidacionesGenerales();
                    break;
				case COMENTARIOS_NEGOCIO:
					if(params == null){
                        params = new HashMap<String,String>();
                    }
					lista = catalogosManager.obtieneComentariosNegocio(params.get("cdramo"), params.get("cdtipsit"), params.get("negocio"));
                	break;
				case TIPOS_ENDOSO_X_TRAMITE:
				    if (params !=null && StringUtils.isNotBlank(params.get("ntramite"))) {
				        lista = catalogosManager.recuperarTiposEndosoPorTramite(params.get("ntramite"));
				    }
				    break;
				case TIPOPROVEEDOR:
					lista = siniestrosManager.getConsultaListaTiposProveedores();
					break;
				default:
					throw new Exception("Catalogo no existente: " + cat);
					//break;
			}
        	success = true;
        } catch(Exception e) {
        	logger.error("No se pudo obtener el catalogo para " + catalogo, e);
            lista=new ArrayList<GenericVO>(0);
        }
        return SUCCESS;
	}
    
    public String agregaCodigoPostal()throws Exception{
		
		try{
			catalogosManager.agregaCodigoPostal(params);
		}catch(Exception ex){
			logger.error("Error al agregaCodigoPostal",ex);
			msgRespuesta = ex.getMessage();
			success = false;
			return SUCCESS;
		}
		
		success = true;
		return SUCCESS;
	}

    public String asociaZonaCodigoPostal()throws Exception{
    	
    	try{
    		catalogosManager.asociaZonaCodigoPostal(params);
    	}catch(Exception ex){
    		logger.error("Error al asociaZonaCodigoPostal",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    public String obtieneTablasApoyo()throws Exception{
    	
    	try{
    		// Obtenemos el rol de sistema del usuario en sesion:
			//String usuario = (String) session.get("USUARIO");
    		UserVO usuario = (UserVO) session.get("USUARIO");
			params.put("PV_CDSISROL_I", usuario.getRolActivo().getClave() );
			params.put("PV_CDUSER_I", usuario.getUser());
    		loadList = catalogosManager.obtieneTablasApoyo(params);
    	}catch(Exception ex){
    		logger.error("Error al obtieneTablasApoyo",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }

    public String guardaTablaApoyo()throws Exception{
    	
    	try{
    		logger.debug("Guardando Tabla de Apoyo...");
    		logger.debug("Parametros: " + params);
    		
    		String naturalezaTabla = params.get("pi_clnatura");
    		if(StringUtils.isNotBlank(naturalezaTabla) && "1".equals(naturalezaTabla)){
    			params.put("pi_ottipotb", "1");
    		}else{
    			params.put("pi_ottipotb", "5");
    		}
    		params.put("pi_swmodifi", "S");
    		params.put("pi_swerror", "S");
    		
    		String nmtabla = catalogosManager.guardaTablaApoyo(params);
    		logger.debug(" >>>>>>>>>  Numero de Tabla Guardada: " + nmtabla);
    		
    		params.put("pi_nmtabla", nmtabla);
    		
    		logger.debug("Guardando Claves... ");
    		catalogosManager.guardaClavesTablaApoyo(params, saveList);
    		logger.debug("Claves Guardadas... ");

    		logger.debug("Guardando Atributos... ");
    		catalogosManager.guardaAtributosTablaApoyo(params, saveList2);
    		logger.debug("Atributos Guardados... ");
    		
    	}catch(Exception ex){
    		logger.error("Error al guardaTablaApoyo",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    public String obtieneClavesTablaApoyo()throws Exception{
    	
    	try{
    		loadList = catalogosManager.obtieneClavesTablaApoyo(params);
    		//logger.debug("Claves obtenidas: " + loadList);
    	}catch(Exception ex){
    		logger.error("Error al obtieneTablasApoyo",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }

    public String obtieneAtributosTablaApoyo()throws Exception{
    	
    	try{
    		loadList = catalogosManager.obtieneAtributosTablaApoyo(params);
    		//logger.debug("Atributos obtenidos: " + loadList);
    	}catch(Exception ex){
    		logger.error("Error al obtieneAtributosTablaApoyo",ex);
    		msgRespuesta = ex.getMessage();
    		success = false;
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    }
    
    public String actualizaDescCoberturas()throws Exception{
        
        logger.debug("Guardando Descripcion Corta Cobertura...");
        logger.debug("Coberturas: " + saveList);
        
        try{
            Utils.validate(saveList, "No hay coberturas a guardar");
            
            for(Map<String, String> cob: saveList){
                catalogosManager.guardaDescripcionCortaCobertura(cob.get("CDGARANT"), cob.get("DSGARANT_CORTA"));
            }
            
        }catch(Exception ex){
            msgRespuesta = Utils.manejaExcepcion(ex);;
            success = false;
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
    
    // Getters and setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isCatalogoGenerico() {
		return catalogoGenerico;
	}

	public void setCatalogoGenerico(boolean catalogoGenerico) {
		this.catalogoGenerico = catalogoGenerico;
	}

	public String getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}

	public List<GenericVO> getLista() {
		return lista;
	}

	public void setLista(List<GenericVO> lista) {
		this.lista = lista;
	}

	public List<?> getListaGenerica() {
		return listaGenerica;
	}

	public void setListaGenerica(List<?> listaGenerica) {
		this.listaGenerica = listaGenerica;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setCatalogosManager(CatalogosManager catalogosManager) {
		this.catalogosManager = catalogosManager;
	}

	public void setKernelManager(KernelManagerSustituto kernelManager) {
		this.kernelManager = kernelManager;
	}

	public void setEndososManager(EndososManager endososManager) {
		this.endososManager = endososManager;
	}


	public void setSiniestrosManager(SiniestrosManager siniestrosManager) {
		this.siniestrosManager = siniestrosManager;
	}

	public void setStoredProceduresManager(
			StoredProceduresManager storedProceduresManager) {
		this.storedProceduresManager = storedProceduresManager;
	}

	public String getMsgRespuesta() {
		return msgRespuesta;
	}

	public void setMsgRespuesta(String msgRespuesta) {
		this.msgRespuesta = msgRespuesta;
	}

	public List<Map<String, String>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<Map<String, String>> loadList) {
		this.loadList = loadList;
	}

	public List<Map<String, String>> getSaveList() {
		return saveList;
	}

	public void setSaveList(List<Map<String, String>> saveList) {
		this.saveList = saveList;
	}

	public List<Map<String, String>> getSaveList2() {
		return saveList2;
	}

	public void setSaveList2(List<Map<String, String>> saveList2) {
		this.saveList2 = saveList2;
	}
	
}