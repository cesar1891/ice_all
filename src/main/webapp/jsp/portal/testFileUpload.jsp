<%@ include file="/taglibs.jsp"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>JSP Page</title>
        <script>
            var targetId='<s:property value="targetId" />';
            var urlPonerLlaveSesion='<s:url namespace="/" action="subirArchivoPonerLlaveSesion" />';
            var URL_SUBIR_ARCHIVO='<s:url namespace="/" action="subirArchivo" />';
            var URL_BARRA_SUBIR_ARCHIVO='<s:url namespace="/" action="subirArchivoMostrarBarra" />';
            function fileUploaded()
            {
                parent.$('#'+targetId+'-inputEl').val(69);
            }
        </script>
        <script src="${ctx}/resources/jsp-script/extjs4/subirArchivo.js"></script>
    </head>
    <body>
        <div id="maindiv"></div>
    </body>
</html>