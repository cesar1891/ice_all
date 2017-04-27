<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" isErrorPage="true" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- <meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/> 
<meta http-equiv="Expires" content="Tue, 20 Aug 2010 14:25:27 GMT">
-->
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"/>

<link href="resources/css/template_css.css?yyyyMMdd" rel="stylesheet" type="text/css" />
<link href="resources/css/header_light.css?yyyyMMdd" rel="stylesheet" type="text/css" />
<link href="resources/css/toolbar_magenta.css" rel="stylesheet" type="text/css" />
<link href="resources/css/footer_magenta.css" rel="stylesheet" type="text/css" />

<script language="javascript">
    function muestraError(){
        document.getElementById("muestraError").style.display="none";
        document.getElementById("exception").style.display="block";
    }
</script>
<!--[if lte IE 6]>
<link href="resources/css/template_ie.css" rel="stylesheet" type="text/css" />
<![endif]-->
<link rel="shortcut icon" href="resources/images/favicon.ico" />
</head>
    <body id="page_bg" class="w-wide f-default header-light toolbar-magenta footer-magenta">
        <div id="header">
            <div class="wrapper">
                <div id="access">
                </div>
                <a href="#" title="home" onclick="location.href='${ctx}/'"><span id="logo_left">&nbsp;</span></a>
                <div>
                    <div id="top">
                        
                    </div>
                </div>
                <a href="#" title="home"><span id="logo_right">&nbsp;</span></a>
            </div>
        </div>
        <div id="toolbar">
            <div class="wrapper">
                <div id="nav">
                </div>
            </div>
        </div>
        <div id="showcase">
            <div class="wrapper">
                <div class="padding">
                    <table class="showcase" cellspacing="0">
                    </table>
                </div>
            </div>
        </div>
        <div id="mainbody">
            <div class="wrapper">
                <div id="mainbody-2">
                    <div id="mainbody-3">
                        <div id="mainbody-4">
                            <div id="mainbody-5">
                                <div id="mainbody-padding">
                                    <table class="mainbody" cellspacing="0">
                                        <tr valign="top">
                                            <td class="mainbody">
                                                <div><div class="padding" id="others">
                                                    <br/><br/><br/>
                                                    <h3>Ha ocurrido un error en la aplicaci&oacute;n</h3>
                                                    Regresar a la <a href="<c:url value="/"/>">p&aacute;gina inicial</a><br/>
                                                    <% if (exception != null) { %>
                                                        <div id="muestraError"><a href="#" onCLick="muestraError();"> Mostrar error</a></div>
                                                        <div id="exception" style="display:none">
                                                        <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre>
                                                        </div>
                                                    <% } else { %>
                                                        Please check your log files for further information.
                                                    <% } %>
                                                    <br/><br/><br/>
                                                </div></div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>                          
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="footer">
            <div class="wrapper">
                <div class="padding">
                    <table class="footer" cellspacing="0">
                        <tr valign="top"> 
                            <td class="footer">
                            <div class="moduletable">
                            </div>
                            </td>
                        </tr>
                    </table>
                    <div id="the-footer">
                        Copyright &#169; 2013
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>