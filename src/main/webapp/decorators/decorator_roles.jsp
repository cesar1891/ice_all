<%@ page language="java" %>

<%@ include file="/taglibs.jsp"%>

<!-- pagina para decorator "roles": -->

<!DOCTYPE html>
<html>
	<head>
		<link href="${ctx}/resources/css/header_light.css?yyyyMMdd" rel="stylesheet" type="text/css" />
		<link href="${ctx}/resources/css/toolbar_magenta.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/resources/css/footer_magenta.css" rel="stylesheet" type="text/css" />
		<!-- jtezva: se reemplazo template_css por template_clean_css porque template original tenia copiado el codigo del estilo de extjs2 -->
		<link href="${ctx}/resources/css/template_clean_css.css?yyyyMMdd" rel="stylesheet" type="text/css" />
		<decorator:head/>
		<style type="text/css">
		    #mainbody   {float: none;}
		</style>
	</head>
	<body id="page_bg" class="w-wide f-default header-light toolbar-magenta footer-magenta">
	    <!-- DIV HEADER -->
	    <div id="header">
	        <div class="wrapper">
	            <div id="access">         
	            </div>
	            <a href="#" title="home"><span id="logo_left">&nbsp;</span></a>
	            <div>
	                <div id="top">
	                </div>
	            </div>
	            <!--
	            <a href="#" title="home"><span id="logo_right">&nbsp;</span></a>
	            -->
	        </div>
	    </div>
	    <!-- DIV TOOLBAR 
	     <div id="toolbar">
	        <div class="wrapper">
	            <div id="nav">
	            </div>
	        </div>
	    </div>    -->
	    <!-- DIV SHOW CASE -->
	    <div id="showcase">
	        <div class="wrapper">
	            <div class="padding">
	                <table class="showcase" cellspacing="0">
	                    <tr valign="top">
	                        <td class="showcase">
	                            <div id="top_left">
	                            </div>
	                        </td>
	                        <td class="showcase">
	                            <div id="top_center">
	                                
	                            </div>
	                        </td>
	                        <td class="showcase">
	                            <div id="top_right">
	                            </div>
	                        </td>
	                    </tr>
	                </table>
	            </div>
	        </div>
	    </div>
	<!-- DIV MAIN BODY -->
	     <div id="mainbody">
	        <div class="wrapper">
	            <div id="mainbody-2">
	                <div id="mainbody-3">
	                    <div id="mainbody-4">
	                        <div id="mainbody-5">
	                            <div id="mainbody-padding">
	                                <table class="mainbody" cellspacing="0">
	                                    <tr valign="top">
	                                        <td class="left" id="ContenedorLeft">
	                                            <div><div id="left_1" class="moduletable">
	                                            </div></div>
	                                            <div><div id="left_2" class="moduletable">
	                                            </div></div>
	                                            <div><div id="left_3" class="moduletable">
	                                            </div></div>
	                                            <div><div id="left_4" class="moduletable"></div></div>
	                                            <div><div id="left_5" class="moduletable"></div></div>
	                                        </td>
	                                        <td class="mainbody">
	                                            <table class="headlines" cellspacing="48">
	                                                <tr valign="top">
	                                                    <td class="headlines" colspan="2">
	                                                        <div id="main">
	                                                            <!-- ELEMENTO PRINCIPAL....  -->
	                                                            <decorator:body/>
	                                                        </div>
	                                                    </td>
	                                                </tr>
	                                                <tr>
	                                                    <td class="headlines">
	                                                        <div id="news">
	                                                        </div>
	                                                    </td>
	                                                    <td class="headlines">
	                                                        <div id="knewthat">
	                                                        </div>
	                                                    </td>
	                                                </tr>
	                                                <tr valign="top">
	                                                    <td class="headlines" colspan="2">
	                                                        <div id="mainDown">
	                                                        </div>
	                                                    </td>
	                                                </tr>
	                                                <tr>
	                                                    <td class="headlines">
	                                                        <div id="otherLeft">
	                                                        </div>
	                                                    </td>
	                                                    <td class="headlines">
	                                                        <div id="otherRight">
	                                                        </div>
	                                                    </td>
	                                                </tr>
	                                            </table>
	                                            <div><div class="padding" id="others">
	                                            </div></div>
	                                        </td>
	                                        <td class="right" id="ContenedorRight">
	                                            <div><div id="right_1" class="moduletable">
	                                            </div></div>
	                                            <div><div id="right_2" class="moduletable">
	                                            </div></div>
	                                            <div><div id="right_3" class="moduletable">
	                                            </div></div>
	                                            <div><div id="right_4" class="moduletable"></div></div>
	                                            <div><div id="right_5" class="moduletable"></div></div>
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
	   <!-- Div de Footer --> 
	   <div id="footer">
	        <div class="wrapper">
	            <div class="padding">
	                <table class="footer" cellspacing="30">
	                    <tr valign="top"> 
	                        <td class="footer">
	                        <div class="moduletable" id="footer_left">
	                        </div>
	                        </td>
	                        <td class="footer">
	                        <div class="moduletable" id="footer_center">
	                        </div>
	                        </td>
	                        <td class="footer">
	                        <div class="moduletable" id="footer_right">
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