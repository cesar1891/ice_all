<%@ page language="java" %>

<%@ include file="/taglibs.jsp"%>

<!-- pagina para decorator "default": -->

<!DOCTYPE html>
<html>
    <head>
    
    	<!-- TENER CUIDADO CON EL ORDEN EN QUE SE INCLUYEN LOS SIGUIENTES FICHEROS -->
    
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        
        <link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs4/resources/my-custom-theme/my-custom-theme-all.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs4/extra-custom-theme.css" />
        <script type="text/javascript" src="${ctx}/resources/jquery/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="${ctx}/resources/extjs4/ext-all.js"></script>
        <!--<script type="text/javascript" src="${ctx}/resources/extjs4/ext-all-debug-w-comments.js"></script>-->
        <script type="text/javascript" src="${ctx}/resources/extjs4/locale/ext-lang-es.js?${now}"></script>
        <%@ include file="/resources/jsp-script/util/variablesGlobales.jsp"%>
        <%@ include file="/resources/jsp-script/util/catalogos.jsp"%>
        <script type="text/javascript" src="${ctx}/resources/extjs4/base_extjs4.js?${now}"></script>
        <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js?${now}"></script>
        <script type="text/javascript" src="${ctx}/resources/scripts/util/rfc.js?${now}"></script>
        <style>
		.green {
		    border-left: 2px solid green;
		    border-right: 2px solid green;
		}       
		.red {
		    border-left: 2px solid red;
		    border-right: 2px solid red;
		}
        </style>
        <decorator:head />
        <!-- EL custom_overrides.js DEBE SER INCLUIDO DESPUES DE LOS SCRIPTS PROPIOS DE CADA JSP -->
        <script type="text/javascript" src="${ctx}/resources/scripts/util/custom_overrides.js?${now}"></script>
        <!-- Manejo de la extension de la sesion: -->
        <script type="text/javascript" src="${ctx}/resources/scripts/util/session_timeout.js?${now}"></script>
        
        <!-- Manejo de sesion unica de usuario: -->
        <script>
        var bloqueoXSession= false;
        setInterval(
        function()
        {
        	Ext.Ajax.request(
                    {
                        url     : '<s:url namespace="/seguridad" action="mantenerSesionUnica"/>'
                        ,params :
                        {
                             'params.cdsisrol'     : '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />'
                            ,'params.cdusuari'     : '<s:property value="%{#session['USUARIO'].user}" />'
                        }
                        ,success : function(response)
                        {
                            var json = Ext.decode(response.responseText);
//                             debug('### Valida session Unica BLOQUEO/DESBLOQUEO:',json);
                            if(json.success==true)
                            {
                            	if(json.params.bloqueo=='S')
                            	{
	                           		if(bloqueoXSession==false)
                           			{   
// 	                           			debug('### Valida session >>> BLOQUEO:',json);
                           			    _maskSession('Ha cambiado su Usuario y/o Rol<br>Si desea continuar no cierre &eacute;sta ventana e inicie una sesi&oacute;n con el Usuario y Rol que &eacute;sta ventana indica');
                           			    		
                           			    bloqueoXSession =true;
                           			}
                           		}
	                            else
	                            {
                           	   		if(bloqueoXSession==true)
                           	   		{
//                            	   		    debug('### Valida session >>> DES-BLOQUEO:',json);
                            			_unmask();
                            			bloqueoXSession=false;
                            		}
	                            }
                            }
                           	else
                           	{
                           		mensajeError('Error en la validacion de session');
                           	}
                        }
                        ,failure : function()
                        {
                            errorComunicacion(null,'Al validar sesion');
                        }
                    });
        },10*1000);</script>
        
    </head>
    <body>
        <s:if test="%{#session != null && #session.containsKey('ES_MOVIL') && #session['ES_MOVIL'] == true }">
            <!-- MOVIL -->
            <a href="${ctx}" onclick="_mask();" ><img src="${ctx}/resources/images/boton_menu_principal_movil.png" /></a>
        </s:if>
        <s:else>
            <!-- DESKTOP -->
        </s:else>
        <decorator:body />
    </body>
</html>