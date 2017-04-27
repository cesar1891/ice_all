<%-- 
    Document   : subirArchivoBarra
    Created on : 5/09/2013, 09:50:09 AM
    Author     : Jair
--%>
<%@ include file="/taglibs.jsp"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <script>
            var urlObtenerProgreso='<s:url namespace="/" action="jsonObtenerProgreso" />';
            var uploadKey='<s:property value="uploadKey" />';
            var p;
            function checarProgreso(){
                Ext.Ajax.request({
                    url: urlObtenerProgreso,
                    params:{uploadKey:uploadKey},
                    success:function(response,opts)
                    {
                        var jsonResp = Ext.decode(response.responseText);
                        debug('jsonResp.respuestaOculta=', jsonResp.mensajeRespuesta);
                        // Si ocurre un error al subir el archivo terminamos la ejecucion:
                        if( jsonResp && !Ext.isEmpty(jsonResp.mensajeRespuesta) ) {
                        	parent.callbackDocumentoSubidoPoliza(jsonResp.mensajeRespuesta);
                    		return;
                    	}
                        p.updateProgress(jsonResp.progreso,jsonResp.progresoTexto,true);
                        if(jsonResp.progreso==1)
                        {
                        	//no hago nada, el otro iframe redirige
                        }
                        else
                        {
                            setTimeout(checarProgreso,500);
                        }
                    },
                    failure:checarProgreso
                });
            }
            Ext.onReady(function(){
                p=Ext.create('Ext.ProgressBar',{
                    renderTo:'maindiv',
                    width:450
                });
                if(uploadKey&&uploadKey.length>0) {
                	checarProgreso();
                }
            });
        </script>
    </head>
    <body style="margin:0;padding: 0;">
        <div id="maindiv"></div>
    </body>
</html>
