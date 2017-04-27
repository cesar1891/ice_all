<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>

<script type="text/javascript">
	var ACTION_TABLA_EXPRESIONES ="<s:url namespace='/wizard' action='ComboTabla' includeParams='none'/>";
	var ACTION_COLUMNA_EXPRESIONES = "<s:url namespace='/wizard' action='ComboColumna' includeParams='none'/>";
	var ACTION_CLAVE_EXPRESIONES = "<s:url namespace='/wizard' action='ComboClave' includeParams='none'/>";
    var LIMPIA_SESION_CARGA_MANUAL="<s:url action='LimpiarSesionCargaManual' namespace='/wizard'/>";
    var _ACTION_COBERTURAS = "<s:url action='CargaCombosCoberturas' namespace='/wizard'/>";
    var _ACTION_AGREGA_COBERTURAS = "<s:url action='AgregaCobertura' namespace='/wizard'/>";
</script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/vTypes/combo.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/vTypes/date.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/checkColumnPlugin.js"></script>


<script type="text/javascript" src="${ctx}/js/wizard/utilities/coberturas/catalogo/agregarCoberturas.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/productos/tipoObjetos/principalTipoObjetos.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/tipoObjetos/agregarCatalogoObjetos.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/tipoObjetos/agregarDatosVariablesObjeto.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/tipoObjetos/eliminarDatoVariableObjeto.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/tablaApoyo5Claves/AgregarAtributo.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/tablaApoyo5Claves/AgregarClaves.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/tablaApoyo5Claves/tablaApoyo5Claves.js"></script>



<script type="text/javascript" src="${ctx}/js/wizard/utilities/atributosVariables/listasDeValores/agregarValoresCargaManual.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/atributosVariables/listasDeValores/eliminarValoresCargaManual.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/rol/catalogo/atributosVariablesPersona.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/atributosVariables/listasDeValores/listasDeValores.js"></script>


<script type="text/javascript" src="${ctx}/js/wizard/utilities/definicion/clausula/clausulaIE.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/expresiones/expresiones.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/librerias/principal/autorizacionesLibrerias.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/librerias/principal/condicionesLibrerias.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/librerias/principal/tarificacionLibrerias.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/librerias/principal/validacionesLibrerias.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/librerias/principal/variablesLibrerias.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/definicion/periodo/periodoIE.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/definicion/periodo/periodoB.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/incisos/catalogo/agregarIncisos.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/datosFijos/datosFijos.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/conceptosCobertura/configurarConceptoCobertura.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/conceptosCobertura/conceptosPorCobertura.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/utilities/reglasValidacion/configurarReglasValidacion.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/productos/reglasValidacion/reglasDeValidacion.js"></script>


<script type="text/javascript" src="${ctx}/js/wizard/utilities/sumaAsegurada/eliminarSumaAsegurada.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/sumaAsegurada/agregaSumaAseguradaInciso.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/sumaAsegurada/principalSumaAsegurada.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/catalogos/tablasDeApoyo/tablaApoyoTabla5Claves.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/catalogos/tablasDeApoyo/tablaApoyoListaDeValores.js"></script>

<script type="text/javascript" src="${ctx}/js/wizard/layouts/application-layout-browser.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/definicion/principalProductosAON.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/incisos/principalInciso.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/atributosVariables/atributosVariables.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/rol/atributosVariables/eliminarAtributoVariable.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/variablesTemporalesProducto/asociarVariablesTemporalesAlProducto.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/rol/atributosVariables/agregarAtributoVariable.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/utilities/rol/catalogo/agregarCatalogoRoles.js"></script>
<script type="text/javascript" src="${ctx}/js/wizard/productos/rol/roles.js"></script>


<script type="text/javascript" src="${ctx}/js/wizard/productos/planes/planes.js"></script>
<jsp:include page="/jsp/wizard/agregarCoberturas.jsp" flush="true"/>

<!-- jsp que carga el menu-->

<style type="text/css">
        
        #button-grid .x-panel-body {
            border:1px solid #99bbe8;
            border-top:0 none;
        }
        .add {
            background-image:url(resources/extjs4/resources/fam3icons/icons/add.gif) !important;
        }
        .option {
            background-image:url(resources/extjs4/resources/fam3icons/icons/plugin.gif) !important;
        }
        .remove {
            background-image:url(resources/extjs4/resources/fam3icons/icons/delete.gif) !important;
        }
        .save {
            background-image:url(resources/fam3icons/icons/script_save.png) !important;
        }
</style>
    
</head>
<body>
<div id="north" style="width: 100%;">
<table style="width: 98%; border: 0px solid;">
    <tr>
        <td align="left">
            <!-- <img src="${ctx}/resources/images/logo/logo.png">-->
            <div id="nav-toolbar"></div>
        </td>
    </tr>
</table>
</div>


<div id="center" style="height: 100%">
</div>
<div id="center-anidado" style="height: 100%">
</div>
<div id="center-anidado-periodos" style="height: 100%">
</div>

<div id="center4rolls" style="height: 100%">
</div>

<div id="centerIncisos" style="height: 100%">
</div>

<div id="centerAtributos" style="height: 100%">
</div>
<!--div id="texto-obligatorios">
<p> * Datos requeridos</p>
</div-->

<div id="centerDatosFijos" style="height: 100%">
</div>

<div id="centerConceptosGlobales" style="height: 100%">
</div>

<div id="centerCoberturas" style="height: 100%">
</div>

<div id="centerObjetos" style="height: 100%">
</div>

<div id="centerSumaAsegurada" style="height: 100%">
</div>

<div id="centerReglasValidacion" style="height: 100%">
</div>

<div id="centerConceptosCobertura" style="height: 100%">
</div>

<div id="centerVariablesTemporalesProducto" style="height: 100%">
</div>
<div id="centerPlanes" style="height: 100%">
</div>
<div id="libreriasAutorizaciones" style="height: 100%">
</div>
<div id="libreriasCondiciones" style="height: 100%">
</div>
<div id="libreriasTarificacion" style="height: 100%">
</div>
<div id="libreriasValidaciones" style="height: 100%">
</div>
<div id="libreriasVariables" style="height: 100%">
</div>

<div id="tablaApoyoListaDeValores" style="height: 100%">
</div>
<div id="tablaApoyoTabla5Claves" style="height: 100%">
</div>

<div id="empty" style="height: 100%">
</div>
<div id="empty2" style="height: 100%">
</div>

<div id="south">
<table style="width: 99%; border: 0px solid;">
    <tr>
        <td style="width: 33%;"></td>
        <td style="width: 33%;" align="center"></td>
        <td style="width: 33%;" align="right"></td>
    </tr>
</table>
<p></p>
</div>

</body>
</html>
