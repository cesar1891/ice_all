<%@ page language="java"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

	/* ********** VARIABLES GLOBALES ************** */
    var _GLOBAL_URL_MANTENER_SESION_UNICA = '<s:url namespace="/seguridad" action="mantenerSesionUnica" />';
	
	var _global_urlConsultaDinamica        = '<s:url namespace="/consultas" action="consultaDinamica" />';
	var _GLOBAL_URL_LOGIN                  = '<s:url namespace="/seguridad" action="login" />';
	var _GLOBAL_URL_LOGOUT                 = '<s:url namespace="/seguridad" action="logout" />';
	var _GLOBAL_URL_MANTENER_SESION_ACTIVA = '<s:url namespace="/seguridad" action="mantenerSesionActiva" />';
	var _GLOBAL_MUESTRA_EXTENSION_TIMEOUT  = '<s:text name="sesion.muestra.extension.timeout"/>';
	var _GLOBAL_OCULTA_EXTENSION_TIMEOUT   = '<s:text name="sesion.oculta.extension.timeout"/>';
	var _GLOBAL_URL_GRABAR_EVENTO          = '<s:url namespace="/servicios" action="grabarEvento" />';
	var _GLOBAL_URL_TEST_SLEEP             = '<s:url namespace="/test" action="sleep" />';
	
	var _GLOBAL_DIRECTORIO_ICONOS         = '${icons}';
	var _GLOBAL_DIRECTORIO_IMAGENES       = '${images}';
	var _GLOBAL_CONTEXTO                  = '${ctx}';
	var _GLOBAL_DIRECTORIO_DEFINES        = '${defines}'
	var _GLOBAL_URL_RECUPERACION          = '<s:url namespace="/recuperacion" action="recuperar"           />';
	var _GLOBAL_URL_IMPRIMIR_LOTE         = '<s:url namespace="/consultas"    action="imprimirLote"        />';
	var _GLOBAL_URL_DESCARGAR_LOTE        = '<s:url namespace="/consultas"    action="descargarLote"       />';
	var _GLOBAL_URL_DESCARGAR_LOTE_DPLX   = '<s:url namespace="/consultas"    action="descargarLoteDplx"   />';
	var _GLOBAL_URL_DESCARGAR_LISTA_ERROR_ARCHIVO   = '<s:url namespace="/consultas"    action="descargaListaError"   />';
	var _GLOBAL_URL_ESPERAR_DESCARGA_LOTE = '<s:url namespace="/consultas"    action="esperarDescargaLote" />';
	
	var _GLOBAL_URL_CARGAR_ACCIONES_ENTIDAD = '<s:url namespace="/flujomesacontrol" action="cargarAccionesEntidad"                  />';
	var _GLOBAL_URL_PANTALLA_EXTERNA        = '<s:url namespace="/flujomesacontrol" action="pantallaExterna"                        />';
	var _GLOBAL_URL_VALIDACION              = '<s:url namespace="/flujomesacontrol" action="ejecutaValidacion"                      />';
	var _GLOBAL_URL_VALIDACION_MONTAR_DATOS = '<s:url namespace="/flujomesacontrol" action="recuperarDatosTramiteValidacionCliente" />';
    var _GLOBAL_URL_REVISION                = '<s:url namespace="/flujomesacontrol" action="ejecutaRevision"                        />';
    var _GLOBAL_URL_TURNAR                  = '<s:url namespace="/flujomesacontrol" action="turnarTramite"                          />';
    var _GLOBAL_URL_ENVIAR_CORREO_FLUJO     = '<s:url namespace="/flujomesacontrol" action="enviaCorreoFlujo"						/>';
    var _GLOBAL_URL_REGRESAR_TRAM_VENCIDO   = '<s:url namespace="/flujomesacontrol" action="regresarTramiteVencido"                 />';
    var _GLOBAL_URL_MARCAR_REQUISITO        = '<s:url namespace="/flujomesacontrol" action="marcarRequisitoRevision"                />';
    var _GLOBAL_URL_CONFIRMAR_REVISION      = '<s:url namespace="/flujomesacontrol" action="marcarRevisionConfirmada"               />';
    var _GLOBAL_URL_ENVIAR_CORREO           = '<s:url namespace="/general"          action="enviaCorreo"                            />';
    var _GLOBAL_URL_MARCAR_IMPRESION        = '<s:url namespace="/consultas"        action="marcarImpresionOperacion"               />';

    var _GLOBAL_COMP_URL_VENTANA_DOCS               = '<s:url namespace="/documentos"       action="ventanaDocumentosPoliza"    />'
        ,_GLOBAL_COMP_URL_GET_HISTORIAL             = '<s:url namespace="/mesacontrol"      action="obtenerDetallesTramite"     />'
        ,_GLOBAL_COMP_URL_FINAL_HIST                = '<s:url namespace="/mesacontrol"      action="finalizarDetalleTramiteMC"  />'
        ,_GLOBAL_COMP_URL_MODIF_HIST                = '<s:url namespace="/flujomesacontrol" action="modificarDetalleTramiteMC"  />'
        ,_GLOBAL_COMP_URL_TURNAR                    = '<s:url namespace="/flujomesacontrol" action="turnarDesdeComp"            />'
        ,_GLOBAL_COMP_URL_CONS_CLAU                 = '<s:url namespace="/catalogos"        action="consultaClausulas"          />'
        ,_GLOBAL_COMP_URL_CONS_CLAU_DET             = '<s:url namespace="/catalogos"        action="consultaClausulaDetalle"    />'
        ,_GLOBAL_COMP_URL_GUARDA_CARTA_RECHAZO      = '<s:url namespace="/"                 action="guardarCartaRechazo"        />'
        ,_GLOBAL_COMP_URL_AUTORIZAR_EMISION         = '<s:url namespace="/"                 action="autorizaEmisionSinSMD"      />'
        ,_GLOBAL_COMP_URL_RECUPERACION_SIMPLE_LISTA = '<s:url namespace="/emision"          action="recuperacionSimpleLista"    />'
        ,_GLOBAL_COMP_URL_ACTUALIZAR_STATUS_TRAMITE = '<s:url namespace="/mesacontrol"      action="actualizarStatusTramite"    />'
        ,_GLOBAL_URL_REASIGNAR_TRAMITE_INDV         = '<s:url namespace="/mesacontrol"      action="reasignarTramiteIndividual" />'
        ,_GLOBAL_COMP_URL_AUTORIZAR_ENDOSO          = '<s:url namespace="/endosos"          action="autorizarEndoso"            />'
        ,_GLOBAL_COMP_RECUPERAR_COTI_COLEC          = '<s:url namespace="/flujomesacontrol" action="recuperarCotiColec"         />'
        ,_GLOBAL_URL_OBTENER_COMPONENTES            = '<s:url namespace="/pantallas"        action="obtenerComponentes"         />'
        ,_GLOBAL_COMP_URL_GUARDAR_DATOS_TRAMITE     = '<s:url namespace="/flujomesacontrol" action="guardarVentanaDatosTramite" />'
        ,_GLOBAL_COMP_URL_CAMBIAR_TIPO_ENDOSO       = '<s:url namespace="/flujomesacontrol" action="cambiarTipoEndosoTramite"   />';
    
    <s:url namespace="/flujomesacontrol" action="mesaControl" var="urlMesaFlujo">
        <s:param name="params.AGRUPAMC" value="%{'PRINCIPAL'}" />
    </s:url>
    
    var _GLOBAL_COMP_URL_MCFLUJO = '<s:property value="urlMesaFlujo" />';
    
    var _GLOBAL_CDSISROL = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';
    
    var _GLOBAL_ES_MOVIL = false;
    
    <s:if test="%{#session != null && #session.containsKey('ES_MOVIL') && #session['ES_MOVIL'] == true }">
        _GLOBAL_ES_MOVIL = true;
    </s:if>
</script>