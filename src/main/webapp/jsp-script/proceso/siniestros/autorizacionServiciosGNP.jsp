<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
    <title>Autorizaci&oacute;n de servicio</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}'; 
            
            var _CAT_AUTORIZACION				    = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
            var _CAT_CAUSASINIESTRO				    = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CAUSA_SINIESTRO"/>';
            var _CAT_TRATAMIENTO				    = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TRATAMIENTOS"/>';
            var _CAT_TIPOEVENTOGNP                  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOEVENTOGNP"/>';
            var _CAT_TPENALIZACIONES				= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PENALIZACIONES"/>';
            var _CAT_MEDICOS                        = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MEDICOS"/>';
            var _CAT_MEDICOS_ESPECIFICO             = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MEDICOESPECIFICO"/>';
            var _CAT_PROVEEDORES                    = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
            var _SINO								= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SINO" />';
            
            var _SALUD_VITAL						= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SALUD_VITAL.cdramo" />';
            var _MULTISALUD							= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@MULTISALUD.cdramo" />';
            var _GMMI								= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES.cdramo" />';
            var _GMMPRUEBA                          = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES_PRUEBA.cdramo" />';
            var _RECUPERA							= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@RECUPERA.cdramo" />';
            
            var _CODIGO_CAUSA_ENFERMEDAD		    = '<s:property value="@mx.com.gseguros.portal.general.util.CausaSiniestro@ENFERMEDAD.codigo"/>';
            var _CODIGO_CAUSA_ACCIDENTE			    = '<s:property value="@mx.com.gseguros.portal.general.util.CausaSiniestro@ACCIDENTE.codigo"/>';
            var _CODIGO_CAUSA_MATERNIDAD		    = '<s:property value="@mx.com.gseguros.portal.general.util.CausaSiniestro@MATERNIDAD.codigo"/>';
            var _AUTORIZACION_SERVICIO			    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@AUTORIZACION_SERVICIOS.cdtiptra"/>';	
            var _CAT_RAMOS                          = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOS"/>';
            var _0_urlRutaReporte                   = '<s:property value="rutaServidorReports" />';
            var _0_reportsServerUser                = '<s:property value="passServidorReports" />';
            var _urlViewReport                      = '<s:property value="rutaServidorReports" />';
            var _reportsServerUser                  = '<s:property value="passServidorReports" />';
            
            var _URL_TIPO_AUTORIZACION				= _CONTEXT + '/js/proceso/siniestros/tiposAutorizacion.json';
            
            var _URL_CATALOGOS                      = '<s:url namespace="/catalogos"   action="obtieneCatalogo" />';
            var _VER_COBERTURAS		    			= '<s:url namespace="/consultasPoliza"  action="includes/verCoberturasPoliza" />';
            var _URL_LISTADO_ASEGURADO          	= '<s:url namespace="/siniestros"  action="consultaListaAsegurado" />';
            var _URL_CONSULTA_LISTADO_AUTORIZACION 	= '<s:url namespace="/siniestros"  action="consultaListaAutorizacion" />';
            var _URL_CONSULTA_AUTORIZACION_ESP 		= '<s:url namespace="/siniestros"  action="consultaAutorizacionServicio" />';
            var _URL_LISTA_COBERTURA 				= '<s:url namespace="/siniestros"  action="consultaListaCoberturaPoliza" />';
            var _URL_LISTA_SUBCOBERTURA				= '<s:url namespace="/siniestros"  action="consultaListaSubcobertura" />';
            var _URL_LISTA_CPTICD					= '<s:url namespace="/siniestros"  action="consultaListaCPTICD" />';
            var _URL_LISTA_ICD						= '<s:url namespace="/siniestros"  action="consultaListaICD" />'; // (EGS)
            var _URL_LISTA_CDPUNIECO                = '<s:url namespace="/siniestros"  action="consultaListaCPTUnico" />'; // (EGS)
            _URL_LISTA_TIPOMEDICO                   = '<s:url namespace="/siniestros"  action="consultaListaTipoMedico" />'; 
            var _URL_CONSULTA_DEDUCIBLE_COPAGO		= '<s:url namespace="/siniestros"  action="consultaListaDatSubGeneral" />';
            var _URL_CONSULTA_LISTADO_POLIZA		= '<s:url namespace="/siniestros"  action="consultaListaPoliza" />';
            var _URL_LISTA_TABULADOR				= '<s:url namespace="/siniestros"  action="consultaListaPorcentaje" />';
            var _URL_TIPO_MEDICO					= '<s:url namespace="/siniestros"  action="consultaTipoMedico" />';
            var _URL_LISTADO_CONCEP_EQUIP    		= '<s:url namespace="/siniestros"  action="consultaListaTDeTauts" />';
            var _URL_GUARDA_AUTORIZACION			= '<s:url namespace="/siniestros"  action="guardaAutorizacionServicio" />';
            var _VER_AUTORIZACION_SERVICIO 			= '<s:url namespace="/siniestros"  action="includes/verAutorizacionServicio" />';
            var _UR_LISTA_PLAZAS 					= '<s:url namespace="/siniestros"  action="consultaListaPlazas"/>';
            var _URL_NUMERO_DIAS					= '<s:url namespace="/siniestros"  action="consultaNumeroDias"/>';
            var _URL_POLIZA_UNICA					= '<s:url namespace="/siniestros"  action="consultaPolizaUnica"/>';
            var _URL_EXCLUSION_PENALIZACION			= '<s:url namespace="/siniestros"  action="validaExclusionPenalizacion"/>';
            var _URL_PORCENTAJE_PENALIZACION		= '<s:url namespace="/siniestros"  action="validaPorcentajePenalizacion"/>';
            var panDocUrlViewDoc                    = '<s:url namespace ="/documentos" action="descargaDocInline" />';
            var venDocUrlImpConrec                  = '<s:url namespace ="/documentos" action="generarContrarecibo" />';
            var _p12_urlMesaControl              	= '<s:url namespace="/mesacontrol" action="mcdinamica"               />';
            var _URL_MONTO_MAXIMO					= '<s:url namespace="/siniestros"  action="consultaMontoMaximo"/>';
            var _URL_MESES_MAXIMO_MAT				= '<s:url namespace="/siniestros"  action="consultaMesesMaximoMaternidad"/>';
            var _URL_Existe_Documentos				= '<s:url namespace="/siniestros"  action="validaDocumentosAutoServ" />';
            var _URL_NUM_MESES_TIEMPO_ESPERA   		= '<s:url namespace="/siniestros"  action="obtieneMesesTiempoEspera" />';
            var _UR_LISTA_RAMO_SALUD				= '<s:url namespace="/siniestros"  action="consultaRamosSalud"/>';
            var _URL_MONTO_DISP_PROVEEDOR           = '<s:url namespace="/siniestros"  action="obtieneRequiereAutServ"/>';
            var _URL_LOADER_DATOS_POLIZA            = '<s:url namespace="/consultasPoliza" action="includes/ventanaDatosPoliza" />';
            var _URL_LOADER_ASEGURADOS_POLIZA       = '<s:url namespace="/consultasPoliza" action="includes/ventanaAseguradosPoliza" />';
            var _URL_LOADER_RECIBOS                 = '<s:url namespace="/general"         action="includes/loadRecibos" />';
            var _URL_LOADER_VER_EXCLUSIONES         = '<s:url namespace="/consultasPoliza" action="includes/verClausulas" />';
            var _URL_CIRCULO_HOSP_MULTISALUD        = '<s:url namespace="/siniestros" 	   action="consultaCirculoHospitalarioMultisalud" />';
            var _URL_CIRCULO_HOSPITALARIO        	= '<s:url namespace="/siniestros" 	   action="consultaCirculoHospitalario" />';
            var _URL_PORCENTAJE_QUIRURGICO        	= '<s:url namespace="/siniestros"      action="consultaPorcentajeQuirurgico" />';
            var _URL_LOADER_HISTORIAL_RECLAMACIONES = '<s:url namespace="/siniestros"	   action="includes/historialReclamaciones" />';
            var _URL_LOADER_EXLUSION_ICD_ASEGURADOS = '<s:url namespace="/siniestros"      action="includes/exclusionICDAsegurado" />';
            var _URL_VAL_CAUSASINI			        = '<s:url namespace="/siniestros" 	   action="consultaInfCausaSiniestroProducto" />';
            var _URL_LISTA_TIPOEVENTO               = '<s:url namespace="/siniestros"      action="consultaListaTipoEventoSiniestro" />';
            var _urlViewDoc                         = '<s:url namespace ="/documentos"     action="descargaDocInline"              />';
            var _URL_CONSULTA_DOCUMENTOS            = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            var _URL_OBTIENE_IMPORTEARANCEL         = '<s:url namespace="/siniestros"      action="obtieneImporteArancelGNP" />';
            var _URL_CONSULTA_CLAUSULAS_POLIZA      = '<s:url namespace="/consultasPoliza" action="consultaClausulasPoliza" />';
            var _URL_cargaHistorialSinies           = '<s:url namespace="/siniestros" action="cargaICDExcluidosAsegurado" />';
            var _0_reporteVistaPreviaGral           = '<s:text name="rdf.siniestro.autorizacion.servicio.nombre.GNP" />';
            var _0_reporteVistaPreviaProv           = '<s:text name="rdf.siniestro.autorizacion.servicio.nombre.GNPEXP" />';
            var _reporteEdoCtaSin                   = '<s:text name="rdf.siniestro.estadocuenta.nombre" />';
            var _URL_DATOS_COMPLEMENTO_ICD          = '<s:url namespace="/siniestros"  action="obtieneDatosGeneralesICD" />';
            var _URL_VALIDA_EXCLUSION               = '<s:url namespace="/siniestros"  action="obtenerValidacionExclusionICD" />';
            var _UrlRechazarTramiteWindwow          = '<s:url namespace="/siniestros"   action="includes/rechazoReclamaciones" />';
            var _URL_CONSULTA_CLAUSU_DETALLE        = '<s:url namespace="/catalogos"    action="consultaClausulaDetalle" />';
            var _URL_CONSULTA_CLAUSU                = '<s:url namespace="/catalogos"    action="consultaClausulas" />';
            var _URL_ActualizaStatusTramite         = '<s:url namespace="/mesacontrol"  action="actualizarStatusTramite" />';
            var mesConUrlFinDetalleMC               = '<s:url namespace="/mesacontrol"  action="finalizarDetalleTramiteMC" />';
            var compleUrlGuardarCartoRechazo        = '<s:url namespace="/siniestros"   action="guardarCartaRechazoAutServ" />';
            var _STATUS_TRAMITE_RECHAZADO           = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
            var _URL_ActualizaStatusMAUTSERV        = '<s:url namespace="/siniestros"   action="cambiarEstatusMAUTSERV" />';
            var valorAction                         = <s:property value='paramsJson' escapeHtml='false'/>;
            
            debug("valor d_SALUD_VITAL --->",_SALUD_VITAL);
        </script>
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/autorizacionServiciosGNP.js?${now}"></script>
        <!-- <script type="text/javascript" src="${ctx}/js/proceso/siniestros/verAutorizacionServicios.js"></script> -->
        
    </head>
    <body>
    <!-- <div style="height:500px;">
            <div id="div_clau"></div>
   </div>-->
    	<!-- <div style="height:1500px;">-->
    	<div style="height:2500px;">
            <div id="div_clau"></div>
            <!-- <div id="divResultados" style="margin-top:10px;"></div>-->
        </div> 
   </body>
</html>