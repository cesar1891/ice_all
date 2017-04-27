<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Consulta de asegurados</title>
        <script type="text/javascript">
        Ext.onReady(function() {
        	Ext.Msg.alert('Aviso', 'No posee rol con el que intenta acceder al sistema, por favor p&oacute;ngase en contacto con el Administrador.');
        });
        
        </script>
        </head>
    <body>
        <div id="dvConsultasAsegurados" style="height:710px">
        </div>
    </body>
</html>