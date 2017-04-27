<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Alta de usuarios</title>
        
        <script type="text/javascript">
 ////// urls //////
         _AGENTE    = '<s:property value="@mx.com.gseguros.portal.general.util.RolSistema@AGENTE.cdsisrol" />';
            
            var _parametros = <s:property value='paramsJson' escapeHtml='false'/>;
            
            debug(_parametros);
            
            var _url_obtiene_perfil_medico = '<s:url namespace="/perfilMedico" action="consultarIcds"              />';
     ////// urls //////
        ////// variables //////
            
        </script>
        
        <script type="text/javascript" src="${ctx}/js/consultas/consultaECD.js?${now}"></script>
        
    </head>
    <body>
	    <div id="_p999_divpri" style="height:800px;"></div>
    </body>
</html>