<%@ page language="java" %>

<%@ include file="/taglibs_deprecated.jsp"%>
<%@page import="mx.com.aon.portal.model.PerfilVO"%>
<jsp:useBean id="userVO" class="mx.com.aon.portal.model.UserVO" scope="session" />

<!-- pagina para decorator "default_deprecated" -->

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Expires" content="Tue, 20 Aug 2010 14:25:27 GMT"/> 
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="${ctx}/resources/css/header_light.css?yyyyMMdd" rel="stylesheet" type="text/css" />
		<link href="${ctx}/resources/css/toolbar_magenta.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/resources/css/footer_magenta.css" rel="stylesheet" type="text/css" />
		
		<!-- Estilos para extJs default_deprecated-->
		
		<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/xtheme-gray.css" />
		<link href="${ctx}/resources/css/template_css.css?yyyyMMdd" rel="stylesheet" type="text/css" />
		
		<script language="javascript">
		    var _CONTEXT = "${ctx}";
		
			// Se inicializan las fuentes de la pantalla para la carga
		    var sourceForPanel = new Array();
		
		    // Se asignan las fuentes iniciales
		    <%  PerfilVO perfil = userVO.getFuentesPerfil();  %>
		    
		    sourceForPanel[0]  = '<%=perfil.getTop()!=null?perfil.getTop():""%>';
		    sourceForPanel[1]  = '<%=perfil.getNav()!=null?perfil.getNav():""%>';
		    sourceForPanel[2]  = '<%=perfil.getTopLeft()!=null?perfil.getTopLeft():""%>';
		    sourceForPanel[3]  = '<%=perfil.getTopCenter()!=null?perfil.getTopCenter():""%>';
		    sourceForPanel[4]  = '<%=perfil.getTopRight()!=null?perfil.getTopRight():""%>';
		    sourceForPanel[5]  = '<%=perfil.getLeft_1()!=null?perfil.getLeft_1():""%>';
		    sourceForPanel[6]  = '<%=perfil.getLeft_2()!=null?perfil.getLeft_2():""%>';
		    sourceForPanel[7]  = '<%=perfil.getLeft_3()!=null?perfil.getLeft_3():""%>';
		    sourceForPanel[8]  = '<%=perfil.getLeft_4()!=null?perfil.getLeft_4():""%>';
		    sourceForPanel[9]  = '<%=perfil.getLeft_5()!=null?perfil.getLeft_5():""%>';
		    sourceForPanel[10] = '<%=perfil.getRight_1()!=null?perfil.getRight_1():""%>';
		    sourceForPanel[11] = '<%=perfil.getRight_2()!=null?perfil.getRight_2():""%>';
		    sourceForPanel[12] = '<%=perfil.getRight_3()!=null?perfil.getRight_3():""%>';
		    sourceForPanel[13] = '<%=perfil.getRight_4()!=null?perfil.getRight_4():""%>';
		    sourceForPanel[14] = '<%=perfil.getRight_5()!=null?perfil.getRight_5():""%>';
		    sourceForPanel[15] = '<%=perfil.getMain()!=null?perfil.getMain():""%>';
		    sourceForPanel[16] = '<%=perfil.getNews()!=null?perfil.getNews():""%>';
		    sourceForPanel[17] = '<%=perfil.getKnewthat()!=null?perfil.getKnewthat():""%>';
		    sourceForPanel[18] = '<%=perfil.getMainDown()!=null?perfil.getMainDown():""%>';
		    sourceForPanel[19] = '<%=perfil.getOtherLeft()!=null?perfil.getOtherLeft():""%>';
		    sourceForPanel[20] = '<%=perfil.getOtherRight()!=null?perfil.getOtherRight():""%>';
		    sourceForPanel[21] = '<%=perfil.getOthers()!=null?perfil.getOthers():""%>';
		    sourceForPanel[22] = '<%=perfil.getFooterLeft()!=null?perfil.getFooterLeft():""%>';
		    sourceForPanel[23] = '<%=perfil.getFooterCenter()!=null?perfil.getFooterCenter():""%>';
		    sourceForPanel[24] = '<%=perfil.getFooterRight()!=null?perfil.getFooterRight():""%>';
		
			function cerrar() {
			}
		</script>
		<link rel="shortcut icon" href="${ctx}/resources/images/favicon.ico" />
		<!-- Links para extJs default_deprecated-->
		
		<script type="text/javascript" src="${ctx}/resources/extjs/adapter/ext/ext-base.js"></script>
		
		<script type="text/javascript" src="${ctx}/resources/extjs/ext-all.js"></script>
		<script type="text/javascript" src="${ctx}/resources/scripts/portal/loadPortal.js"></script>
		<jsp:include page="/jsp/menu/menu.jsp" flush="true" />
		
		<decorator:head/>
	</head>
	
	<body id="page_bg" class="w-wide f-default header-light toolbar-magenta footer-magenta" onunload="cerrar()" >		
		<div id="showcase">
			<div class="wrapper">
				<div class="padding">
					<table class="showcase" cellspacing="0">
						<tr valign="top">
							<td class="showcase">
	                            <div id="top_left"></div>
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
	                                            <div>
	                                            	<div id="left_1" class="moduletable" >
	                                            		<s:if test="%{#session.containsKey('CONTENIDO_LEFT_1')}">
															<s:property value="%{#session['CONTENIDO_LEFT_1']}" escapeHtml="false" />
														</s:if>
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_1_IMAGE') && #session.containsKey('CONTENIDO_LEFT_1_FILE')}" >
															<a href="<s:url value="%{#session['CONTENIDO_LEFT_1_FILE']}"/>"><img src="<s:url value="%{#session['CONTENIDO_LEFT_1_IMAGE']}"/>" width="130" height="150" /></a>
														</s:elseif>												
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_1_IMAGE')}">
															<img src="<s:url value="%{#session['CONTENIDO_LEFT_1_IMAGE']}"/>" width="130" height="150" />
														</s:elseif>	                                            	
	                        	                    </div>
	                                            </div>
	                                            <div>
	                                            	<div id="left_2" class="moduletable">
	                                            		<s:if test="%{#session.containsKey('CONTENIDO_LEFT_2')}">                                         
															<s:property value="%{#session['CONTENIDO_LEFT_2']}" escapeHtml="false" />
														</s:if>
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_2_IMAGE') && #session.containsKey('CONTENIDO_LEFT_2_FILE')}" >
															<a href="<s:url value="%{#session['CONTENIDO_LEFT_2_FILE']}"/>"><img src="<s:url value="%{#session['CONTENIDO_LEFT_2_IMAGE']}"/>" width="130" height="150" /></a>
														</s:elseif>	
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_2_IMAGE')}">
															<img src="<s:url value="%{#session['CONTENIDO_LEFT_2_IMAGE']}"/>" width="130" height="150" />
														</s:elseif>                                            	
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="left_3" class="moduletable">
	                                            		<s:if test="%{#session.containsKey('CONTENIDO_LEFT_3')}">
															<s:property value="%{#session['CONTENIDO_LEFT_3']}" escapeHtml="false" />
														</s:if>
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_3_IMAGE') && #session.containsKey('CONTENIDO_LEFT_3_FILE')}" >
															<a href="<s:url value="%{#session['CONTENIDO_LEFT_3_FILE']}"/>"><img src="<s:url value="%{#session['CONTENIDO_LEFT_3_IMAGE']}"/>" width="130" height="150" /></a>
														</s:elseif>	
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_3_IMAGE')}">
															<img src="<s:url value="%{#session['CONTENIDO_LEFT_3_IMAGE']}"/>" width="130" height="150" />
														</s:elseif>                                            	
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="left_4" class="moduletable">
	                                            		<s:if test="%{#session.containsKey('CONTENIDO_LEFT_4')}">
															<s:property value="%{#session['CONTENIDO_LEFT_4']}" escapeHtml="false" />
														</s:if>
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_4_IMAGE') && #session.containsKey('CONTENIDO_LEFT_4_FILE')}" >
															<a href="<s:url value="%{#session['CONTENIDO_LEFT_4_FILE']}"/>"><img src="<s:url value="%{#session['CONTENIDO_LEFT_4_IMAGE']}"/>" width="130" height="150" /></a>
														</s:elseif>	
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_4_IMAGE')}">
															<img src="<s:url value="%{#session['CONTENIDO_LEFT_4_IMAGE']}"/>" width="130" height="150" />
														</s:elseif>                                         
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="left_5" class="moduletable">
	                                            		<s:if test="%{#session.containsKey('CONTENIDO_LEFT_5')}">
															<s:property value="%{#session['CONTENIDO_LEFT_5']}" escapeHtml="false" />
														</s:if>
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_5_IMAGE') && #session.containsKey('CONTENIDO_LEFT_5_FILE')}" >
															<a href="<s:url value="%{#session['CONTENIDO_LEFT_5_FILE']}"/>"><img src="<s:url value="%{#session['CONTENIDO_LEFT_5_IMAGE']}"/>" width="130" height="150" /></a>
														</s:elseif>	
														<s:elseif test="%{#session.containsKey('CONTENIDO_LEFT_5_IMAGE')}">
															<img src="<s:url value="%{#session['CONTENIDO_LEFT_5_IMAGE']}"/>" width="130" height="150" />
														</s:elseif>                                            
	                                            	</div>
	                                            </div>
											</td>
											<td class="mainbody">
												<table class="headlines" cellspacing="10">
													<tr valign="top">
														<td class="headlines" colspan="2">														
	                                                    	<decorator:body/>
														</td>
	                                                </tr>
	                                                <tr>
														<td class="headlines">																												
															<div id="news" style="display: none;"></div>														                                                                                                                
														</td>
														<td class="headlines">													                                                    	
	                                                        <div id="knewthat" style="display: none;"></div>                                               														
														</td>
													</tr>
													<tr valign="top">
														<td class="headlines" colspan="2">                                                        
	                                                    	<div id="mainDown" style="display: none;"></div>
														</td>
	                                                </tr>
	                                                <tr>
														<td class="headlines">                                                    	
	                                                        <div id="otherLeft" style="display: none;"></div>														
														</td>
														<td class="headlines">                                                    	
	                                                        <div id="otherRight" style="display: none;"></div>														
														</td>
													</tr>
												</table>
												<div>
													<div class="padding" id="others">													
	                                            	</div>
	                                            </div>
											</td>
											<td class="right" id="ContenedorRight">
	                                            <div>
	                                            	<div id="right_1" class="moduletable" style="display: none;">                                            		
	                                            	</div>                                                                                       
	                                            </div>
	                                            <div>
	                                            	<div id="right_2" class="moduletable" style="display: none;">                                            		
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="right_3" class="moduletable" style="display: none;">                                            		
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="right_4" class="moduletable" style="display: none;">                                            		
	                                            	</div>
	                                            </div>
	                                            <div>
	                                            	<div id="right_5" class="moduletable" style="display: none;">                                            		
	                                            	</div>
	                                            </div>
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
	                        	<div class="moduletable" id="footer_left" style="display: none;">                        		
	                        	</div>
							</td>
							<td class="footer">
	                        	<div class="moduletable" id="footer_center" style="display: none;">                        
	                        	</div>
							</td>
							<td class="footer">
	                        	<div class="moduletable" id="footer_right" style="display: none;">                       
	                        	</div>
							</td>
	                    </tr>
					</table>
					<!-- Se agrego temporalmente el siguiente div para eliminar el error en Firefox -->
					<div id="main" style="display: none;"></div>
					
					<div id="the-footer">
	                    Copyright &#169; 2013
					</div>
				</div>
			</div>
		</div>
	</body>
</html>