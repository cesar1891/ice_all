<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Ext JS 4 Dashboard</title>
		<script type="text/javascript">
			var _CONTEXT = '${ctx}';
			
			var _MSG_FILTRO_BUSQUEDA   = 'Seleccione un filtro de busqueda';
			var _MSG_BUSQUEDA   	   = 'Ingrese un n&uacute;mero de tr&aacute;mite';
			var _MSG_BUSQUEDA_FACTURA  = 'Ingrese un n&uacute;mero de factura';
			
			var _TIPOPAGO;
			var _CDUNIECO;
			var _CDRAMO;
			var _ESTADO;
			var _NMPOLIZA;
			var _NMSITUAC;
			var _NMSUPLEM;
			var _STATUS;
			var _AAAPERTU;
			var _NMSINIES;
			var _NTRAMITE;
			var _CDTIPSIT;
			
			var _URL_AFILIADOS_AFECTADOS   		    = '<s:url namespace="/consultasSiniestro" action="afiliadosAfectados" />';
			var _URL_LISTADO_ASEGURADO          	= '<s:url namespace="/siniestros"  action="consultaListaAsegurado" />';
			
			
			var _URL_CONSULTA_LIST_ASEG_REEMBOLSO   = '<s:url namespace="/consultasSiniestro" action="consultaAseguradosPagoReembolso" />';
			var _URL_CONSULTA_PROVEEDOR_PDIRECTO    = '<s:url namespace="/consultasSiniestro" action="consultaProveedorPDirecto" />';
			var _URL_BUSQUEDA_DIRECTO				= _CONTEXT + '/resources/scripts/consultaSiniestros/busquedaPagoDirecto.json';
			var _URL_BUSQUEDA_REEMBOLSO				= _CONTEXT + '/resources/scripts/consultaSiniestros/busquedaPagoReembolso.json';
			
			var _URL_CARGA_INFORMACION			    = '<s:url namespace="/consultasSiniestro" action="detalleSiniestrosInicial" />';
			var _URL_INFO_GRAL_SINIESTRO    		= '<s:url namespace="/siniestros"      action="obtieneDatosGeneralesSiniestro" />';
			
			var _URL_LOADER_INFO_GRAL_RECLAMACION 	= '<s:url namespace="/consultasSiniestro" action="includes/loadInfoGeneralReclamacion" />';
			var _URL_LOADER_REV_ADMIN             	= '<s:url namespace="/consultasSiniestro" action="includes/revAdmin" />';
			var _URL_LOADER_CALCULOS             	= '<s:url namespace="/consultasSiniestro" action="includes/calculoSiniestros" />';
			
			var _URL_FACTURA_PDIRECTO   			= '<s:url namespace="/consultasSiniestro" action="consultaFacturasPagoDirecto" />';
			var _URL_MESA_DE_CONTROL     			= '<s:url namespace="/siniestros"       action="consultaListadoMesaControl" />';
			var _CATALOGO_PROVEEDORES  				= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
			var _URL_CATALOGOS 						= '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
			var _URL_CONSULTA_DATOS_SUPLEMENTO 		= '<s:url namespace="/consultasPoliza" action="consultaDatosSuplemento" />';
			var _URLDOCUMENTOSPOLIZA        		= '<s:url namespace="/documentos" action="ventanaDocumentosPoliza"   />';
			
			var _TIPO_TRAMITE_SINIESTRO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra" />';
		</script>
		<script type="text/javascript" src="${ctx}/resources/scripts/consultaSiniestros/consultasSiniestros.js"></script>
		
	</head>
	<body>
		<div id="dvConsultasPolizas" style="height:1010px"></div>
	</body>
</html>