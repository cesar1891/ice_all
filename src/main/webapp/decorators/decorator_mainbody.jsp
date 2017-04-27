<%@ page language="java" %>

<%@ include file="/taglibs_deprecated.jsp"%>

<!-- pagina para decorator "decmainbody": -->
    
<!DOCTYPE html>
<html>
<head> 
	<link href="${ctx}/resources/css/header_light.css?yyyyMMdd" rel="stylesheet" type="text/css" />
	<link href="${ctx}/resources/css/toolbar_magenta.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/resources/css/footer_magenta.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/extjs/resources/css/xtheme-gray.css" />
	<link href="${ctx}/resources/css/template_css.css?yyyyMMdd" rel="stylesheet" type="text/css" />
    <decorator:head />
</head>
<body>
    <decorator:body />
</body>
</html>