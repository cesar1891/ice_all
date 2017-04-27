<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Reclamaciones Express</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
           
            
            var _URL_NIVEL_RECLAMO                  = _CONTEXT + '/resources/scripts/reclamoExpress/nivelReclamo.json';
            var _URL_RECLAMOS                       = '<s:url namespace="/reclamoExpress" action="consultaReclamos" />';
            var _URL_SECUENCIALES                   = '<s:url namespace="/reclamoExpress" action="consultaSecuenciales" />';
            var _URL_CONSULTA_RECLAMO_EXPRESS       = '<s:url namespace="/reclamoExpress" action="consultaReclamoExpress" />';
            var _URL_LISTA_CPTICD                   = '<s:url namespace="/siniestros"  action="consultaListaCPTICD" />';
            var _URL_LISTA_COBERTURA                = '<s:url namespace="/siniestros"  action="consultaListaCoberturaPoliza" />';
            var _URL_LISTA_SUBCOBERTURA             = '<s:url namespace="/siniestros"  action="consultaListaSubcobertura" />';
            var _CATALOGO_COBERTURASTOTALES         = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@COBERTURASTOTALES"/>';
            var _CATALOGO_SUBCOBERTURASTOTALES      = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SUBCOBERTURASTOTALES"/>';
            var _URL_GUARDA_RECLAMO_EXPRESS         = '<s:url namespace="/reclamoExpress" action="guardaReclamoExpress" />';
            var _URL_GUARDA_DETALLE_EXPRESS         = '<s:url namespace="/reclamoExpress" action="guardaDetalleExpress"/>';
            var _URL_LoadConceptos                  = '<s:url namespace="/siniestros" action="obtenerMsinival" />';
            var _URL_CATALOGOS                      = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
            var _CATALOGO_TipoConcepto              = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_CONCEPTO_SINIESTROS"/>';
            var _CATALOGO_ConceptosMedicos          = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CODIGOS_MEDICOS"/>';
            /*
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
          
            var _URL_CONSULTA_DATOS_TARIFA_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaDatosTarifaPoliza" />';
         
            var _URL_CONSULTA_DOCUMENTOS          = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            var _URL_LOADER_RECIBOS               = '<s:url namespace="/general"         action="includes/loadRecibos" />';
            var _URL_LOADER_AVISO_HOSPITALIZACION = '<s:url namespace="/consultasAsegurado" action="includes/avisoHospitalizacion" />';
            */
            
            //Mensajes
            var _MSG_RECLAMO_INVALIDO       = 'Seleccione una reclamaci\u00F3n.';
            var _MSG_SECUENCIAL_INVALIDO    = 'Seleccione un secuencial.';
            var _MSG_DATO_INVALIDO          = 'Hace falta capturar campos requeridos.';
            var _MSG_RECUPERAR_INFO         = 'Hace falta recuperar la información del reclamo. Presione el bot\u00F3n buscar.';
            var _MSG_ERROR_COMUNICACION     = 'Error de comunicaci\u00F3n.';
            var _MSG_RECLAMO_REGISTRADO     = 'Reclamaci\u00F3n registrada. Por favor capture su detalle.';
            var _MSG_DETALLE_REGISTRADO     = 'Detalle de la reclamaci\u00F3n registrado.';
            /*
            var _MSG_ERROR                       = 'Error';
            var _MSG_INFO                        = 'Info';
            var _MSG_ERROR_HISTORICO_MOVIMIENTOS = 'Ocurri\u00F3 un error al obtener el hist\u00F3rico de movimientos';
            var _MSG_SIN_DATOS                   = 'No hay datos';
            var _MSG_BUSQUEDA_SIN_DATOS          = 'No se encontraron datos. Intente cambiar los filtros de b\u00FAsqueda.';
            var _MSG_RFC_INVALIDO                = 'Ingrese un RFC v\u00E1lido';            
            var _MSG_NMPOLIEX_INVALIDO           = 'Ingrese un numero de poliza';
            
            var _MSG_NOMBRE_INVALIDO             = 'Ingrese el nombre';
            */
            
            //var _IS_USUARIO_CALL_CENTER          = <s:property value="usuarioCallCenter"/>;
                        
        </script>
        <script type="text/javascript" src="${ctx}/resources/scripts/reclamoExpress/reclamoExpress.js?${now}"></script>
        
    </head>
    <body>
        <div id="dvReclamoExpress" style="height:710px"></div>
    </body>
</html>