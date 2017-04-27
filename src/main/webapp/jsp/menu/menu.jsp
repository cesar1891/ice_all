<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${ctx}</title>
        <script type="text/javascript">
            var _CONTEXT = '${ctx}';
        </script>
        <script type="text/javascript" src="${ctx}/js/menu/menu.js?${now}"></script>
    </head>
    <body>
        <div id="header">
            <div class="wrapper">
                <div style="padding: 5px;">
                    <font style="font-size: 17px;font-family:calibri,tahoma,arial,helvetica;">
                    Usuario: 
                    <s:if test="%{#session.containsKey('USUARIO')}">
                        <strong><s:property value="%{#session['USUARIO'].user}" /> - <s:property value="%{#session['USUARIO'].name}" /></strong>
                        Rol: <strong><s:property value="%{#session['USUARIO'].rolActivo.descripcion}" /></strong>
                        Sucursal: <strong><s:property value="%{#session['USUARIO'].cdUnieco}" /></strong>
                    </s:if>
                </font>
                </div>
                <div>                   
                    <div>
                        <table class="showcase" cellspacing="1" height="120">
                            <tr valign="top">
                                <td class="showcase">
                                    <div id="top_lefta">
                                        <s:if test="%{#session.containsKey('CONTENIDO_TOPLEFT')}">
                                            <s:property value="%{#session['CONTENIDO_TOPLEFT']}" escapeHtml="false" />
                                        </s:if>
                                        <s:elseif test="%{#session.containsKey('CONTENIDO_TOPLEFT_IMAGE')}">
                                            <img src="<s:url value="%{#session['CONTENIDO_TOPLEFT_IMAGE']}"/>" width="300" height="100" />
                                        </s:elseif>                                                 
                                        <s:else>
                                            <a href="#" title="home"><span id="logo_left">&nbsp;</span></a>                                 
                                        </s:else>
                                    </div>
                                </td>
                                <td class="showcase">
                                    <div id="top_centera">
                                        <s:if test="%{#session.containsKey('CONTENIDO_TOPCENTER')}">
                                            <s:property value="%{#session['CONTENIDO_TOPCENTER']}" escapeHtml="false" />
                                        </s:if>
                                        <s:elseif test="%{#session.containsKey('CONTENIDO_TOPCENTER_IMAGE')}">
                                            <img src="<s:url value="%{#session['CONTENIDO_TOPCENTER_IMAGE']}"/>" width="300" height="100" />
                                        </s:elseif>                                     
                                    </div>
                                </td>
                                <td class="showcase">
                                    <div id="top_righta">
                                        <s:if test="%{#session.containsKey('CONTENIDO_TOPRIGHT')}">
                                            <s:property value="%{#session['CONTENIDO_TOPRIGHT']}" escapeHtml="false" />
                                        </s:if>
                                        <s:elseif test="%{#session.containsKey('CONTENIDO_TOPRIGHT_IMAGE')}">
                                            <img src="<s:url value="%{#session['CONTENIDO_TOPRIGHT_IMAGE']}"/>" width="300" height="100" />
                                        </s:elseif>                         
                                    </div>
                                </td>
                            </tr>
                        </table>        
                    </div>
                    <div id="top"></div>
                </div>
            </div>
        </div>
        <div id="toolbar">
            <div class="wrapper">
                <div id="nav"></div>
            </div>
        </div>
        <div id="nav-toolbar"></div>
    </body>
</html>