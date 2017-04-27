<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<body>
<!-- TODO: quitar este archivo, no se utiliza actualmente -->
<h3>Indice</h3>
<ul>
	<s:iterator value="%{#session['USUARIO'].listaMenuVertical}" >
		  <li>
		     <a href="<s:property value='href'/>"  > <s:property value='text'/> </a>
		  </li>
	</s:iterator>	
</ul>
</body>
</html>






