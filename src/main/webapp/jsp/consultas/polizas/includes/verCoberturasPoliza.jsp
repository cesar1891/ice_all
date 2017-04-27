<%@ include file="/taglibs.jsp"%>


<script type="text/javascript">
    var _CONTEXT = '${ctx}';
    var _URL_CONSULTA_COPAGOS_POLIZA =      '<s:url namespace="/consultasPoliza" action="consultaCopagosPoliza" />';
    
    // Obtenemos el contenido en formato JSON de la propiedad solicitada:
    var _7_smap1 = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
</script>
        
<script type="text/javascript" src="${ctx}/js/consultas/coberturas/includes/verCoberturas.js"></script>

<div style="height:800px;">
    <div id="div_clau2"></div>
</div>