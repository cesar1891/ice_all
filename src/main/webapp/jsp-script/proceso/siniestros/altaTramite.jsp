<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
       <title>Consulta Clausulas</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            
            var _CATALOGO_OFICINA_RECEP					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
            var _CAT_RAMO_SALUD		       				= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOSALUD"/>';
            //var _CATALOGO_TipoAtencion 					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_ATENCION_SINIESTROS"/>';
            var _CATALOGO_TipoPago						= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TTIPOPAGO"/>';
            var _CATALOGO_PROVEEDORES  					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
            var _CATALOGO_TipoMoneda   					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MONEDA"/>';
            var _TIPO_PAGO_DIRECTO     					= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo"/>';
            var _TIPO_PAGO_REEMBOLSO   					= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo"/>';
            var _PAGO_DIRECTO 							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
            var _REEMBOLSO    							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
            var _INDEMNIZACION							= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@INDEMNIZACION.codigo" />';
            var _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_ASIGNACION.codigo" />';
            var _STATUS_TRAMITE_EN_CAPTURA          	= '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />';
            var _RECHAZADO								= '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
            var _SALUD_VITAL							= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SALUD_VITAL.cdramo" />';
            var _MULTISALUD								= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@MULTISALUD.cdramo" />';
            var _GMMI									= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES.cdramo" />';
            var _GMP                                   =  '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES_PRUEBA.cdramo" />';
            var _RECUPERA								= '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@RECUPERA.cdramo" />';
            var _TIPO_TRAMITE_SINIESTRO 				= '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra" />';
            var _STATUS_TRAMITE_EN_REVISION_MEDICA      = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_REVISION_MEDICA.codigo" />';
            var nombreReporteRechazo					= '<s:text name="pdf.siniestro.cartarechazo.nombre"/>';
            
            debug("TIPO_PAGO");
            debug(_TIPO_PAGO_DIRECTO);
            var _URL_CATALOGOS 							= '<s:url namespace="/catalogos" 		action="obtieneCatalogo" />';
            var _UrlDocumentosPoliza					= '<s:url namespace="/documentos"		action="ventanaDocumentosPoliza"   />';
            var _p12_urlMesaControl                 	= '<s:url namespace="/mesacontrol" 		action="mcdinamica"               />';
            var panDocUrlViewDoc						= '<s:url namespace ="/documentos"		action="descargaDocInline" />';
            var _URL_ActualizaStatusTramite				= '<s:url namespace="/mesacontrol"		action="actualizarStatusTramite" />';
            var _URL_TurnarAOperadorReclamacion			= '<s:url namespace="/mesacontrol"		action="turnarAOperadorReclamacion" />';
            var _UrlRevisionDocsSiniestro   			= '<s:url namespace="/siniestros" 		action="includes/revisionDocumentos"        />';
            var _UR_TIPO_ATENCION						= '<s:url namespace="/siniestros"  		action="consultaListaTipoAtencion"/>';
            var _URL_CONSULTA_LISTADO_POLIZA			= '<s:url namespace="/siniestros" 		action="consultaListaPoliza" />';
            var _URL_LISTADO_ASEGURADO          		= '<s:url namespace="/siniestros"       action="consultaListaAsegurado" />';
            //var _URL_LISTADO_ALTATRAMITE          		= '<s:url namespace="/siniestros"       action="consultaListadoAltaTramite" />';
            var _URL_GUARDA_ALTA_TRAMITE     			= '<s:url namespace="/siniestros"       action="guardaAltaTramite" />';
            var _URL_CONSULTA_ALTA_TRAMITE     			= '<s:url namespace="/siniestros"       action="consultaListadoMesaControl" />';
            var _URL_CONSULTA_GRID_ALTA_TRAMITE     	= '<s:url namespace="/siniestros"       action="consultaListadoAltaTramite" />';
            var _URL_CONSULTA_FACTURA_PAGADA        	= '<s:url namespace="/siniestros"       action="consultaFacturaPagada" />';
            //var _UR_LISTA_RAMO_SALUD					= '<s:url namespace="/siniestros"  		action="consultaRamosSalud"/>';
            var _URL_GUARDA_FACTURA_TRAMITE				= '<s:url namespace="/siniestros"  		action="guardaFacturaAltaTramite"/>';
            var _URL_CONSULTA_FACTURAS       			= '<s:url namespace="/siniestros"		action="obtenerFacturasTramite" />';
            var _URL_VALIDA_FACTURAASEGURADO  			= '<s:url namespace="/siniestros"		action="validarFacturaAsegurado" />';
            var _URL_ASEGURADO_FACTURA					= '<s:url namespace="/siniestros" 		action="obtenerAseguradosTramite" />';
            var _URL_GUARDA_ASEGURADO					= '<s:url namespace="/siniestros" 		action="guardaTworksin" />';
            var _URL_LOADER_HISTORIAL_RECLAMACIONES 	= '<s:url namespace="/siniestros"		action="includes/historialReclamaciones" />';
            
            var _UrlGenerarContrarecibo					= '<s:url namespace="/siniestros"		action="generarContrarecibo"       />';
            var _UrlGeneraSiniestroTramite				= '<s:url namespace="/siniestros"		action="generaSiniestroTramite" />';
            var _UrlValidaDocumentosCargados			= '<s:url namespace="/siniestros"		action="validaDocumentosCargados" />';
            var _URL_ListaRechazos						= '<s:url namespace="/siniestros"		action="loadListaRechazos" />';
            var _URL_ListaIncisosRechazos				= '<s:url namespace="/siniestros"		action="loadListaIncisosRechazos" />';
            var _UrlGeneraCartaRechazo					= '<s:url namespace="/siniestros"		action="generaCartaRechazo" />';
            var _URL_VALOR_CDTIPSIT						= '<s:url namespace="/siniestros"		action="validaCdTipsitTramite"/>';
            var _URL_NOMBRE_TURNADO   					= '<s:url namespace="/siniestros" 		action="obtieneUsuarioTurnado" />';
            var _URL_ELIMINAR_FACT_ASEG					= '<s:url namespace="/siniestros" 		action="eliminarFactAsegurado" />';
            var _URL_CONSULTA_BENEFICIARIO				= '<s:url namespace="/siniestros"		action="consultaDatosBeneficiario" />';
            var _URL_LISTADO_ASEGURADO_POLIZA			= '<s:url namespace="/siniestros"       action="consultaListaAseguradoPoliza" />';
            var _CAT_MODALIDADES 						= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT"/>';
            var _URL_VAL_CAUSASINI			        	= '<s:url namespace="/siniestros" 	   	action="consultaInfCausaSiniestroProducto" />';
            var _URL_VALIDA_STATUSASEG			        = '<s:url namespace="/siniestros" 	   	action="validaStatusAseguradoSeleccionado" />';	
            var _0_urlRutaReporte                       = '<s:property value="rutaServidorReports" />';
            var _0_reportsServerUser                    = '<s:property value="passServidorReports" />';
            var _0_reporteContraRecibo                  = '<s:text name="rdf.siniestro.contrarecibo.nombre" />';
            var _0_urlEnviarCorreo                      = '<s:url namespace="/general"         action="enviaCorreo"/>';
            
            var valorAction = <s:property value='paramsJson' escapeHtml='false'/>;
            debug("valorAction ====>", valorAction);
        </script>
        <!-- <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js"></script>-->
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/altaTramite.js?${now}"></script>
        
    </head>
    <body>
    <div style="height:1000px;">
            <div id="div_clau"></div>
            <!-- <div id="divResultados" style="margin-top:10px;"></div>-->
        </div>
   </body>
</html>