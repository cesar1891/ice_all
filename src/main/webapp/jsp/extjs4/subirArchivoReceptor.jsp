<%@ include file="/taglibs.jsp"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <script>
            $(document).ready(function(){
            	/*
            	Se va a invocar el callback que se recibe desde SubirArchivoAction.subirArchivo
            	*/
            	try
            	{
                    parent.<s:property value="smap1.callbackFn" />();
                }
                catch(e)
                {
                    alert('Error al mostrar el estatus del archivo subido. Consulte a soporte');
                    /*
                    SE DEBE ENVIAR EL PARAMETRO smap1.callbackFn AL ACTION QUE RECIBE LOS ARCHIVOS
                    Y DESDE EL JSP DONDE SE SUBA EL ARCHIVO DEBE EXISTIR UNA FUNCION CONO ESE NOMBRE
                    EJEMPLO:
                    funcion:
                    
                       function juanita(){...}
                    
                    submit de documento:
                       ...submit(
                       {
                           params:
                           {
                               'smap1.callbackFn' : 'juanita'
                               ....
                    */
                }
            });
        </script>
    </head>
    <body>
    </body>
</html>
