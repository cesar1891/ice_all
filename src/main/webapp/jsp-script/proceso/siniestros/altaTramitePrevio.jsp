<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Consulta Clausulas</title>
		
		<script type="text/javascript">
			var _CONTEXT = '${ctx}';
			
			var _CATALOGO_OFICINA_RECEP= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_ADMIN"/>';
			var _CAT_RAMO_SALUD		   = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@RAMOSALUD"/>';
			//var _CATALOGO_TipoAtencion = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_ATENCION_SINIESTROS"/>';
			var _CATALOGO_TipoPago     = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TTIPOPAGO"/>';
			var _CATALOGO_PROVEEDORES  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';
			var _CATALOGO_TipoMoneda   = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPO_MONEDA"/>';
			var _TIPO_PAGO_DIRECTO     = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo"/>';	
			var _TIPO_PAGO_REEMBOLSO   = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo"/>';
            var _SALUD_VITAL		   = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@SALUD_VITAL.cdramo" />';
            var _MULTISALUD			   = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@MULTISALUD.cdramo" />';
            var _GMMI				   = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@GASTOS_MEDICOS_MAYORES.cdramo" />';
            var _RECUPERA			   = '<s:property value="@mx.com.gseguros.portal.general.util.Ramo@RECUPERA.cdramo" />';
			var _CAT_MODALIDADES 	   = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPSIT"/>';
			debug("TIPO_PAGO");
			debug(_TIPO_PAGO_DIRECTO);
			var _URL_CATALOGOS 						= '<s:url namespace="/catalogos" 		action="obtieneCatalogo" />';
			var _UR_TIPO_ATENCION					= '<s:url namespace="/siniestros"  		action="consultaListaTipoAtencion"/>';
			var _URL_CONSULTA_LISTADO_POLIZA		= '<s:url namespace="/siniestros" 		action="consultaListaPoliza" />';
			var _URL_LISTADO_ASEGURADO          	= '<s:url namespace="/siniestros"       action="consultaListaAsegurado" />';
			var _URL_LISTADO_ASEGURADO_POLIZA		= '<s:url namespace="/siniestros"       action="consultaListaAseguradoPoliza" />';
			//var _URL_LISTADO_ALTATRAMITE          	= '<s:url namespace="/siniestros"       action="consultaListadoAltaTramite" />';
			var _URL_GUARDA_ALTA_TRAMITE     		= '<s:url namespace="/siniestros"       action="guardaAltaTramite" />';
			//var _URL_CONSULTA_ALTA_TRAMITE     		= '<s:url namespace="/siniestros"       action="consultaListadoMesaControl" />';
			//var _URL_CONSULTA_GRID_ALTA_TRAMITE     	= '<s:url namespace="/siniestros"       action="consultaListadoAltaTramite" />';
			var _URL_CONSULTA_FACTURA_PAGADA        = '<s:url namespace="/siniestros"       action="consultaFacturaPagada" />';
			var _p12_urlMesaControl                 = '<s:url namespace="/mesacontrol" 		action="mcdinamica"               />';
			//var _UR_LISTA_RAMO_SALUD					= '<s:url namespace="/siniestros"  		action="consultaRamosSalud"/>';
			var _URL_CONSULTA_BENEFICIARIO          = '<s:url namespace="/siniestros"       action="consultaDatosBeneficiario" />';
			var _URL_VAL_CAUSASINI			        = '<s:url namespace="/siniestros" 	    action="consultaInfCausaSiniestroProducto" />';
			var _URL_VALIDA_STATUSASEG			    = '<s:url namespace="/siniestros" 	   	action="validaStatusAseguradoSeleccionado" />';
			
			var valorAction = <s:property value='paramsJson' escapeHtml='false'/>;
		</script>
		<!-- <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js"></script>-->
		<script type="text/javascript" src="${ctx}/js/proceso/siniestros/altaTramitePrevio.js?${now}"></script>
		
	</head>
	<body>
	<div style="height:200px;">
		<div id="div_clau"></div>
			<!-- <div id="divResultados" style="margin-top:10px;"></div>-->
		</div>
	</body>
</html>