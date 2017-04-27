<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

<title>Pantalla Mantenimiento de Atributos Variables de Persona</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="${ctx}/resources/scripts/util/hashMap.js"></script>
<script type="text/javascript" src="${ctx}/resources/scripts/util/ext.js"></script>
<script type="text/javascript" src="${ctx}/resources/scripts/util/AON_utils.js"></script>
<script language="javascript">
    var _CONTEXT = "${ctx}";
    var _ACTION_BUSCAR_ATRIBUTOS_VARIABLES_PERSONA = "<s:url action='buscarAtributosVariablesPersona' namespace='/atributosVariablesPersona'/>";
    var _ACTION_BORRAR_ATRIBUTOS_VARIABLES_PERSONA = "<s:url action='borrarAtributosVariablesPersona' namespace='/atributosVariablesPersona'/>";
    var _ACTION_GUARDAR_ATRIBUTOS_VARIABLES_PERSONA = "<s:url action='guardarAtributosVariablesPersona' namespace='/atributosVariablesPersona'/>";

    var _ACTION_OBTENER_COMBO_FORMATO = "<s:url action='obtenerComboFormatosCampo' namespace='/combos'/>";
    var _ACTION_OBTENER_COMBO_LISTA_VALORES = "<s:url action='obtenerListaValores' namespace='/combos'/>";
    var _ACTION_OBTENER_COMBO_TIPO_PERSONA = "<s:url action='comboTipoPersonaJ' namespace='/combos'/>";
    var _ACTION_OBTENER_COMBO_CATEGORIA_PERSONA = "<s:url action='obtenerCategoriaPersona' namespace='/combos'/>";

    var itemsPerPage=20;
    var helpMap = new Map();
</script>

<script src="${ctx}/resources/scripts/ValidacionGrilla/Ext.ux.plugins.GridValidator.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/scripts/portal/atributosVariablesPersona/atributosVariablesPersona.js"></script>

</head>
<body>
   <table><tr><td><div id="gridAtribuVblesPersona"></div></td></tr></table>
</body>
</html>