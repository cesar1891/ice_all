<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Consulta de p&oacute;lizas</title>
        
        <script type="text/javascript" src="${ctx}/resources/extjs4/plugins/pagingpersistence/pagingselectionpersistence.js?${now}"></script>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            
            var _URL_TIPOS_CONSULTA               = _CONTEXT + '/resources/scripts/consultaPolizas/tiposConsulta.json';
            var _URL_CONSULTA_DATOS_POLIZA        = '<s:url namespace="/consultasPoliza" action="consultaDatosPoliza" />';
            var _URL_CONSULTA_DATOS_POLIZA_TVALOPOL='<s:url namespace="/consultasPoliza" action="consultaDatosPolizaTvalopol" />';
            var _URL_CONSULTA_DATOS_SUPLEMENTO    = '<s:url namespace="/consultasPoliza" action="consultaDatosSuplemento" />';
            var _URL_CONSULTA_DATOS_MENSAJES      = '<s:url namespace="/consultasPoliza" action="consultaAvisos" />';
            var _URL_CONSULTA_DATOS_TARIFA_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaDatosTarifaPoliza" />';
            
            //perfil medico
            //var _URL_CONSULTA_DATOS_ASEGURADO   = '<s:url namespace="/consultasPoliza" action="consultaDatosAsegurados"/>';
            var _URL_CONSULTA_DATOS_ASEGURADO     = '<s:url namespace="/consultasPoliza" action="consultaPerfiles"/>';
            var _URL_CONSULTA_ICDS 				  = '<s:url namespace="/consultasAsegurado" action="includes/consultaECD"/>';
            var _URL_CONSULTA_PERFIL 			  = '<s:url namespace="/perfilMedico" action="consultarPerfil"/>';
            //perfil medico
            
            var _URL_CONSULTA_POLIZAS_ASEGURADO   = '<s:url namespace="/consultasPoliza" action="consultaPolizasAsegurado" />';
            var _URL_CONSULTA_AGENTES_POLIZA      = '<s:url namespace="/consultasPoliza" action="consultaAgentesPoliza" />';
            var _URL_CONSULTA_RECIBOS_AGENTE      = '<s:url namespace="/consultasPoliza" action="consultaRecibosAgente" />';
            var _URL_CONSULTA_COPAGOS_POLIZA      = '<s:url namespace="/consultasPoliza" action="consultaCopagosPoliza" />';
            var _URL_CONSULTA_DOCUMENTOS          = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
            //var _URL_LOADER_RECIBOS               = '<s:url namespace="/general"         action="includes/loadRecibos" />';
            var _URL_LOADER_RECIBOS               = '<s:url namespace="/general"         action="includes/loadRecibosSISA" />';
            var _URL_LOADER_VER_EXCLUSIONES       = '<s:url namespace="/consultasPoliza" action="includes/verClausulas" />';
            var _URL_LOADER_VER_TATRISIT          = '<s:url namespace="/consultasPoliza" action="includes/verDatosTatrisit" />';
            var _URL_LOADER_HISTORIAL_RECLAMACIONES= '<s:url namespace="/siniestros"	 action="includes/historialReclamaciones" />';
            
            var _URL_REGENERA_DOCUMENTOS_ENDOSO   = '<s:url namespace="/documentos"      action="regeneraDocumentosEndoso" />';
            var _URL_RECIBOS_ENDOSO               = '<s:url namespace="/"      action="manualWSrecibos" />';

            var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
            
            var _MSG_ERROR                       = 'Error';
            var _MSG_INFO                        = 'Info';
            var _MSG_ERROR_HISTORICO_MOVIMIENTOS = 'Ocurri\u00F3 un error al obtener el hist\u00F3rico de movimientos';
            var _MSG_SIN_DATOS                   = 'No hay datos';
            var _MSG_BUSQUEDA_SIN_DATOS          = 'No se encontraron datos. Intente cambiar los filtros de b\u00FAsqueda.';
            var _MSG_RFC_INVALIDO                = 'Ingrese un RFC v\u00E1lido';
            var _MSG_NMPOLIEX_INVALIDO           = 'Ingrese un numero de poliza';
            var _MSG_CDPERSON_INVALIDO           = 'Ingrese un c\u00F3digo de persona';
            var _MSG_NOMBRE_INVALIDO             = 'Ingrese el nombre';
            
            //Variable para conocer el rol activo del sistema.
            var cdSisRolActivo = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';
            
            
            //Variables para la generacion de Reportes
            var _urlViewReport      = '<s:property value="rutaServidorReports" />';
            var _reportsServerUser  = '<s:property value="passServidorReports" />';
            var _reporteEdoCtaSin   = '<s:text name="rdf.siniestro.estadocuenta.nombre" />';
            var _urlViewDoc         = '<s:url namespace ="/documentos"     action="descargaDocInline"              />';
            
            var _botonesGrid = [
            		<s:property value="itemMap.gridbuttons" />
            	];
            function regeneraDocsConsulta (){}
            function reenviaRecibosSuplemento (){}
            
        </script>
        <script type="text/javascript" src="${ctx}/resources/scripts/consultaPolizas/consultasPolizas.js?${now}"></script>
        
    </head>
    <body>
        <div id="dvConsultasPolizas" style="height:650px"></div>
    </body>
</html>
