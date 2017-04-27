<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Consulta de asegurados</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
           
            var _URL_TIPOS_CONSULTA               = _CONTEXT + '/resources/scripts/consultaAsegurados/tiposConsulta.json';            
            var _URL_CONSULTA_RESULTADOS_ASEGURADO   = '<s:url namespace="/consultasAsegurado" action="consultaResultadosAsegurado" />';
            var _URL_CONSULTA_POLIZA_ACTUAL       = '<s:url namespace="/consultasAsegurado" action="consultaPolizaActual" />';
            var _URL_CONSULTA_DATOS_COMPLEMENTARIOS = '<s:url namespace="/consultasAsegurado" action="consultaDatosComplementarios" />';            
            var _URL_CONSULTA_DATOS_POLIZA        = '<s:url namespace="/consultasAsegurado" action="consultaDatosPoliza" />';
            var _URL_CONSULTA_DATOS_ASEGURADO_DETALLE = '<s:url namespace="/consultasAsegurado" action="consultaAseguradoDetalle" />';
            var _URL_CONSULTA_DATOS_TITULAR       = '<s:url namespace="/consultasAsegurado" action="consultaDatosTitular" />';
            var _URL_CONSULTA_DATOS_CONTRATANTE   = '<s:url namespace="/consultasAsegurado" action="consultaDatosContratante" />';
            var _URL_CONSULTA_DATOS_ASEGURADO     = '<s:url namespace="/consultasAsegurado" action="consultaDatosAsegurado" />';    //FAMILIA
            var _URL_CONSULTA_DATOS_ENDOSOS       = '<s:url namespace="/consultasAsegurado" action="consultaDatosEndosos" />';
            var _URL_CONSULTA_DATOS_ENFERMEDADES  = '<s:url namespace="/consultasAsegurado" action="consultaDatosEnfermedades" />';
            var _URL_CONSULTA_DATOS_PLAN          = '<s:url namespace="/consultasAsegurado" action="consultaDatosPlan" />';
            var _URL_CONSULTA_COPAGOS_POLIZA      = '<s:url namespace="/consultasAsegurado" action="consultaCopagosPoliza" />';
            var _URL_CONSULTA_COBERTURAS_POLIZA   = '<s:url namespace="/consultasAsegurado" action="consultaCoberturasPoliza" />';
            var _URL_CONSULTA_COBERTURAS_BASICAS  = '<s:url namespace="/consultasAsegurado" action="consultaCoberturasBasicas" />';
            var _URL_LOADER_HISTORICO             = '<s:url namespace="/consultasAsegurado" action="consultaDatosHistorico" />';
            var _URL_LOADER_HISTORICO_FARMACIA    = '<s:url namespace="/consultasAsegurado" action="consultaDatosHistoricoFarmacia" />';
            var _URL_LOADER_VIGENCIA              = '<s:url namespace="/consultasAsegurado" action="consultaPeriodosVigencia" />';
            var _URL_CONSULTA_HOSPITALES 		  = '<s:url namespace="/consultasAsegurado" action="consultaHospitales" />';
            var _URL_LOADER_AVISOS_ANTERIORES     = '<s:url namespace="/consultasAsegurado" action="includes/avisosAnteriores" />';
            var _URL_ENVIAR_AVISO_HOSPITALIZACION = '<s:url namespace="/consultasAsegurado" action="includes/enviarAvisoHospitalizacion" />';
          
            var _URL_CONSULTA_DATOS_TARIFA_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaDatosTarifaPoliza" />';
            var _URL_CONSULTA_DOCUMENTOS          = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            var _URL_LOADER_RECIBOS               = '<s:url namespace="/general"         action="includes/loadRecibos" />';
            var _URL_CONSULTA_ECD 				  = '<s:url namespace="/consultasAsegurado" action="includes/consultaECD"                    />';
            var _URL_CONSULTA_PERFIL 				  = '<s:url namespace="/perfilMedico" action="consultarPerfil"                    />';
            
            //Mensajes
            var _MSG_ERROR                       = 'Error';
            var _MSG_INFO                        = 'Info';
            var _MSG_ERROR_HISTORICO_MOVIMIENTOS = 'Ocurri\u00F3 un error al obtener el hist\u00F3rico de movimientos';
            var _MSG_SIN_DATOS                   = 'No hay datos';
            var _MSG_BUSQUEDA_SIN_DATOS          = 'No se encontraron datos. Intente cambiar los filtros de b\u00FAsqueda.';
            var _MSG_RFC_INVALIDO                = 'Ingrese un RFC v\u00E1lido';            
            var _MSG_NMPOLIEX_INVALIDO           = 'Ingrese un numero de poliza';
            var _MSG_CDPERSON_INVALIDO           = 'Ingrese un c\u00F3digo de persona';
            var _MSG_NOMBRE_INVALIDO             = 'Ingrese el nombre';
            
            //Variable que determina si es usuario de CallCenter
            var _IS_USUARIO_CALL_CENTER          = <s:property value="usuarioCallCenter"/>
          	//Variables que determinan si se preconsulta la pantalla
            var _NMPOLIEX         = '<s:property value="params.nmpoliex"/>'
          	var _ICODPOLIZA     = '<s:property value="params.icodpoliza"/>'
          	var _CDPERSON         = '<s:property value="params.cdperson"/>'
          	var _NMSITUAC         = '<s:property value="params.nmsituac"/>'
            
        </script>
        <script type="text/javascript" src="${ctx}/resources/scripts/consultaAsegurados/consultasAsegurados.js?${now}"></script>
        
    </head>
    <body>
        <div id="dvConsultasAsegurados" style="height:710px"></div>
    </body>
</html>