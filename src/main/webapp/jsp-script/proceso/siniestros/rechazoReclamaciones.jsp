<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Documento</title>
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            
            var _nmTramite = '<s:property value="params.nmTramite" />';
            var _TIPOPAGO= '<s:property value="params.tipopago" />';
            var _cdunieco= '<s:property value="params.cdunieco" />';
            var _cdramo= '<s:property value="params.cdramo" />';
            var _estado= '<s:property value="params.estado" />';
            var _nmpoliza= '<s:property value="params.nmpoliza" />';
            var _nmsuplem= '<s:property value="params.nmsuplem" />';
            var _nmsolici= '<s:property value="params.nmsolici" />';
            var _cdtipsit= '<s:property value="params.cdtipsit" />';
            
            var _URL_ListaRechazos =      '<s:url namespace="/siniestros" action="loadListaRechazos" />';
            var _URL_ListaIncisosRechazos =      '<s:url namespace="/siniestros" action="loadListaIncisosRechazos" />';
            var _URL_ActualizaStatusTramite =      '<s:url namespace="/mesacontrol" action="actualizarStatusTramite" />';
            var _UrlGeneraCartaRechazo =      '<s:url namespace="/siniestros" action="generaCartaRechazo" />';
            
            var panDocUrlViewDoc     = '<s:url namespace ="/documentos" action="descargaDocInline" />';
            var nombreReporteRechazo = '<s:text name="pdf.siniestro.cartarechazo.nombre"/>';
            
          //Catalogo Tipos de pago a utilizar:
        	var _PAGO_DIRECTO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
        	var _REEMBOLSO    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
        </script>
        <!-- <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js"></script>-->
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/rechazoReclamaciones.js"></script>
   </head>
    <body>
            <div id="maindiv"></div>
    </body>
</html>