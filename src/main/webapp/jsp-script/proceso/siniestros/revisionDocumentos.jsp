<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Documento</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';            
            var _URL_CARGA_CLAVES_CLAU	 		= '<s:url namespace="/catalogos" action="cargaClausulas" />';
            var _URL_CONSULTA_CLAUSU 			= '<s:url namespace="/catalogos" action="consultaClausulas" />';
            var _URL_CONSULTA_CLAUSU_DETALLE 	= '<s:url namespace="/catalogos" action="consultaClausulaDetalle" />';
            var _URL_INSERTA_CLAUSU 			= '<s:url namespace="/catalogos" action="insertaClausula" />';
            var _URL_ACTUALIZA_CLAUSU 			= '<s:url namespace="/catalogos" action="actualizaClausula" />';

            var _URL_ListaDocumentos 			= '<s:url namespace="/siniestros" action="loadListaDocumentos" />';
            var _GuardaDocumentos 				= '<s:url namespace="/siniestros" action="guardaListaDocumentos" />';
            
            var _nmTramite = '<s:property value="params.nmTramite" />';
            var _tipoPago = '<s:property value="params.cdTipoPago" />';
            var _tipoAtencion = '<s:property value="params.cdTipoAtencion" />';
            var _tieneCR = <s:property value="params.tieneCR" />;
            
        </script>
        <script type="text/javascript" src="${ctx}/js/proceso/siniestros/revisionDocumentos.js"></script>
        
    </head>
    <body>
            <div id="maindiv"></div>
    </body>
</html>