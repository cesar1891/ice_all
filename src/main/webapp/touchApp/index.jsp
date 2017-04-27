<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/resources/touch-2.3.1/resources/css/sencha-touch.css"></style>
<script src="${ctx}/resources/touch-2.3.1/sencha-touch-all.js"></script>
<script src="${ctx}/resources/touch-2.3.1/overrides.js"></script>
<script src="${ctx}/resources/touch-2.3.1/locale/ext-lang-es.js"></script>
<script>
////// textos //////
var _text_aceptar           = 'Aceptar';
var _text_cancelar          = 'Cancelar';
var _text_enviar            = 'Enviar';
var _text_siguiente         = 'Siguiente';
var _text_anterior          = 'Anterior';
var _text_regresar          = 'Regresar';
var _text_cargando          = 'Cargando...';
var _text_error             = 'Error';
var _text_datosIncompletos  = 'Datos incompletos';
var _text_errorComunicacion = 'Error de comunicaci&oacute;n';

var _contexto = '${ctx}';

var _estadoSesion = '<s:property value="estadoSesion" />';
debug('_estadoSesion:',_estadoSesion);

var _t01 = 'Inicio de sesi&oacute;n';
var _t02 = 'Datos de usuario:';
var _t03 = 'Usuario';
var _t04 = 'Contrase&ntilde;a';
var _t05 = 'Iniciar sesi&oacute;n';
var _t06 = 'Introduzca su nombre de usuario y contrase&ntilde;a';
var _t07 = 'Introduce el nombre de usuario';
var _t08 = 'Introduce la contrase&ntilde;a';
var _t09 = 'Error interno del servidor, consulte a soporte';
var _t10 = 'Seleccionar rol';
var _t11 = 'No puede acceder en este momento';
var _t12 = 'Men&uacute; principal';
var _t13 = 'Salir';
var _t14 = 'No disponible para este dispositivo';
////// textos //////

////// variables globales //////
var _urlLogin              = '<s:url namespace="/seguridad" action="autenticaUsuario"         />';
var _urlLogout             = '<s:url namespace="/seguridad" action="logoutJson"               />';
var _urlPantallaArbolJson  = '<s:url namespace="/seguridad" action="seleccionaRolClienteJson" />';
var _urlCargarArbolRol     = '<s:url namespace="/seguridad" action="ArbolRolCliente"          />';
var _urlRegresaCodigoArbol = '<s:url namespace="/seguridad" action="regresaCodigo"            />';
var _urlObtenerMenus       = '<s:url namespace="/"          action="obtieneMenuPrincipal"     />';
////// variables globales //////

function debug(a,b,c,d)
{
	if(true)
    {
        if(d!=undefined)
            console.log(a,b,c,d);
        else if(c!=undefined)
            console.log(a,b,c);
        else if(b!=undefined)
            console.log(a,b);
        else
            console.log(a);
    }
}

function datosIncompletos(message,callback)
{
	Ext.Msg.alert(_text_datosIncompletos, message, callback);
}

function mensajeError(message,callback)
{
    Ext.Msg.alert(_text_error, message, callback);
}

function errorComunicacion(callback)
{
	mensajeError(_text_errorComunicacion,callback);
}

function maskui()
{
	Ext.Viewport.setMasked(
	{
		xtype    : 'loadmask'
		,message : _text_cargando
	});
}

function unmaskui()
{
    Ext.Viewport.setMasked(false);
}
</script>
<script src="app.js"></script>
</head>
</html>
<body>
</body>
</html>