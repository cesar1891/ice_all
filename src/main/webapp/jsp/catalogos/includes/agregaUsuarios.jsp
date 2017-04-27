<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Alta de usuarios</title>
        
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
            var _URL_INSERTA_USUARIO = '<s:url namespace="/catalogos" action="guardaUsuario" />';
            var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
            
            var _URL_ROLES_SISTEMA_X_PRIVILEGIOS = '<s:url namespace="/catalogos" action="obtienerRolesPorPrivilegio" />';
            
            var _CAT_AGENTES = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES"/>';
            var _CAT_COLONIAS= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@COLONIAS"/>';
            var _SEXO        = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SEXO"/>';
            var ROL_AGENTE    = '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@AGENTE.cdsisrol" />';
            
            var _parametros = <s:property value='paramsJson' escapeHtml='false'/>;
            var editMode = (_parametros.edit == 'S');
            
            debug(_parametros);
            
        </script>
        <script type="text/javascript" src="${ctx}/js/catalogos/agregaUsuarios.js?${now}"></script>
        
    </head>
    <body>
	    <div id="div_usuarios" style="height:600px;"></div>
    </body>
</html>