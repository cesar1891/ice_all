<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.valorNoOriginal {
    background: #FFFF99;
}
.valorRenovacionColec {
    background: #FFD299;
}
._p25_editorLectura {
	visibility: hidden;
}
.tatrigarHide {
	display: none;
}
</style>
<!-- Paging Persistence library -->
<script type="text/javascript" src="${ctx}/resources/extjs4/plugins/pagingpersistence/pagingselectionpersistence2.js?${now}"></script>
<script>
////// overrides //////
Ext.override(Ext.form.TextField,
{
    initComponent:function()
    {
        Ext.apply(this,
        {
            labelWidth : 250
        });
        return this.callParent();
    }
});

Ext.override(Ext.form.NumberField,
{
    initComponent:function()
    {
        Ext.apply(this,
        {
            decimalPrecision : 20
        });
        return this.callParent();
    }
});
////// overrides //////

////// variables //////
var _p25_urlSubirCenso                  = '<s:url namespace="/emision"         action="subirCenso"                       />';
var _p25_urlVentanaDocumentos           = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza"          />';
var _p25_urlBuscarPersonas              = '<s:url namespace="/"                action="buscarPersonasRepetidas"          />';
var _p25_urlObtenerCoberturas           = '<s:url namespace="/emision"         action="obtenerCoberturasPlan"            />';
var _p25_urlObtenerCoberturasColec      = '<s:url namespace="/emision"         action="obtenerCoberturasPlanColec"       />';
var _p25_urlObtenerHijosCobertura       = '<s:url namespace="/emision"         action="obtenerTatrigarCoberturas"        />';
var _p25_urlObtenerTarifaEdad           = '<s:url namespace="/emision"         action="cargarTarifasPorEdad"             />';
var _p25_urlObtenerTarifaCobertura      = '<s:url namespace="/emision"         action="cargarTarifasPorCobertura"        />';
var _p25_urlGenerarTramiteGrupo         = '<s:url namespace="/emision"         action="generarTramiteGrupo2"             />';
var _p25_guardarReporteCotizacion       = '<s:url namespace="/emision"         action="guardarReporteCotizacionGrupo"    />';
var _p25_urlActualizarStatus            = '<s:url namespace="/mesacontrol"     action="actualizarStatusTramite"          />';
var _p25_urlCargarParametros            = '<s:url namespace="/emision"         action="obtenerParametrosCotizacion"      />';
var _p25_urlMesaControl                 = '<s:url namespace="/flujomesacontrol" action="mesaControl"                     />';
var _p25_urlViewDoc                     = '<s:url namespace="/documentos"      action="descargaDocInline"                />';
var _p25_urlCargarDatosCotizacion       = '<s:url namespace="/emision"         action="cargarDatosCotizacionGrupo2"      />';
var _p25_urlCargarGrupos                = '<s:url namespace="/emision"         action="cargarGruposCotizacion2"          />';
var _p25_urlObtenerTvalogarsGrupo       = '<s:url namespace="/emision"         action="cargarTvalogarsGrupo"             />';
var _p25_urlCargarAseguradosExtraprimas = '<s:url namespace="/emision"         action="cargarAseguradosExtraprimas2"     />';
var _p25_urlGuardarSituaciones          = '<s:url namespace="/emision"         action="guardarValoresSituaciones"        />';
var _p25_urlGuardarSituacionesTitulares = '<s:url namespace="/emision"         action="guardarValoresSituacionesTitular" />';
var _p25_urlSubirCensoCompleto          = '<s:url namespace="/emision"         action="subirCensoCompleto2"              />';
var _p25_urlCargarAseguradosGrupo       = '<s:url namespace="/emision"         action="cargarAseguradosGrupo"            />';
var _p25_urlCargarAseguradosGrupoPag    = '<s:url namespace="/emision"         action="cargarAseguradosGrupoPag"		  />';
var _p25_urlRecuperarPersona            = '<s:url namespace="/"                action="buscarPersonasRepetidas"          />';
var _p25_urlPantallaPersonas            = '<s:url namespace="/catalogos"       action="includes/personasLoader"          />';
var _p25_urlPantallaDomicilio           = '<s:url namespace="/catalogos"       action="includes/editarDomicilioAsegurado"/>';
var _p25_urlEditarCoberturas            = '<s:url namespace="/"                action="editarCoberturas"                 />';
var _p25_urlEditarExclusiones           = '<s:url namespace="/"                action="pantallaExclusion"                />';
var _p25_urlGuardarAsegurados           = '<s:url namespace="/emision"         action="guardarAseguradosCotizacion"      />';
var _p25_urlCargarConceptosGlobales     = '<s:url namespace="/emision"         action="cargarConceptosGlobalesGrupo"     />';
var _p25_urlEmitir                      = '<s:url namespace="/emision"         action="emitirColectivo"                  />';
var _p25_urlVentanaDocumentosClon       = '<s:url namespace="/documentos"      action="ventanaDocumentosPolizaClon"      />';
var _p25_urlGuardarContratanteColectivo = '<s:url namespace="/emision"         action="guardarContratanteColectivo"      />';
var _p25_urlRecuperarProspecto          = '<s:url namespace="/emision"         action="cargarTramite"                    />';
var _p25_urlPantallaClausulasPoliza     = '<s:url namespace="/emision"         action="includes/pantallaClausulasPoliza" />';
var _p25_urlRecuperacionSimpleLista     = '<s:url namespace="/emision"         action="recuperacionSimpleLista"          />';
var _p25_urlRecuperacionSimple          = '<s:url namespace="/emision"         action="recuperacionSimple"               />';
var _p25_urlPantallaAgentes             = '<s:url namespace="/flujocotizacion" action="principal"                        />';
var _p25_urlComplementoCotizacion       = '<s:url namespace="/emision"         action="complementoSaludGrupo"            />';
var _p25_urlGuardarConfig4TVALAT        = '<s:url namespace="/emision"         action="guardarConfiguracionGarantias"    />';
var _p25_urlRecuperacion                = '<s:url namespace="/recuperacion"    action="recuperar"                        />';
var _p25_urlRestaurarRespaldoCenso      = '<s:url namespace="/emision"         action="restaurarRespaldoCenso"           />';
var _p25_urlBorrarRespaldoCenso         = '<s:url namespace="/emision"         action="borrarRespaldoCenso"              />';
var _p25_urlRefrescarCensoColectivo     = '<s:url namespace="/emision"         action="refrescarCensoColectivo"          />';
var _p25_urlconsultaExtraprimOcup       = '<s:url namespace="/emision"         action="consultaExtraprimaOcup"            />';
var _p25_urlReporte                     = '<s:url namespace="/consultasPoliza" action="consultaIncisosPoliza" />';

var _p21_urlCargarAseguradosFiltroGrupoPag     = '<s:url namespace="/emision"         action="cargarAseguradosFiltroGrupoPag"      />';
var _p21_urlCargarAseguradosFiltroExtraprimas  = '<s:url namespace="/emision"         action="cargarAseguradosFiltroExtraprimas2"   />';
var _p21_filtroGrupoPag     = 'NOMBRE ASEGURADO';

//estas url se declaran con cotcol para ser usadas desde funcionesCotizacionGrupo.js en comun con cotizacionGrupo2.jsp
var _cotcol_urlPantallaEspPersona   = '<s:url namespace="/persona"  action="includes/pantallaEspPersona"  />'
    ,_cotcol_urlPantallaActTvalosit = '<s:url namespace="/tvalosit" action="includes/pantallaActTvalosit" />'
    ,_cotcol_urlBorrarAsegurado     = '<s:url namespace="/emision"  action="borrarIncisoCotizacion"       />';
//estas url se declaran con cotcol para ser usadas desde funcionesCotizacionGrupo.js en comun con cotizacionGrupo2.jsp 

var _p25_urlMarcarTramitePendienteVistaPrevia = '<s:url namespace="/mesacontrol" action="marcarTramiteVistaPrevia" />';

var _p25_nombreReporteCotizacion        = '<s:text name='%{"rdf.cotizacion.nombre."+smap1.cdtipsit.toUpperCase()}' />';
var _p25_nombreReporteCotizacionDetalle = '<s:text name='%{"rdf.cotizacion2.nombre."+smap1.cdtipsit.toUpperCase()}' />';

var _p25_urlImprimirCotiza      = '<s:property value="rutaServidorReports" />';
var _p25_reportsServerUser      = '<s:property value="passServidorReports" />';
var _TIPO_SITUACION_RENOVACION  = '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@RENOVACION.cdTipSup" />';
var _EN_ESPERA_DE_COTIZACION    = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_COTIZACION.codigo" />';
var _p25_smap1 = <s:property value='%{convertToJSON("smap1")}' escapeHtml="false" />;

debug('_p25_smap1:',_p25_smap1);

if ('S' === _p25_smap1.rstn && RolSistema.Agente === _p25_smap1.cdsisrol) {
    _p25_smap1.DETALLE_LINEA = 'S';
    _p25_smap1.LINEA_EXTENDIDA = 'N';
}

_p25_smap1.modificarTodo = false;

if (Number(_p25_smap1.status) === 24
    && [RolSistema.Agente, RolSistema.EjecutivoVenta, RolSistema.MesaControl].indexOf(_p25_smap1.cdsisrol) != -1
    ) {
    _p25_smap1.modificarTodo = true;
}

_p25_smap1.mapaSwitchCoberValosit = {};
try {
    var listaSwitchCoberValosit = _p25_smap1.swtichCoberturaTatrisit.split(',');
    debug('listaSwitchCoberValosit:', listaSwitchCoberValosit);
    var mapaSwitchCoberValosit = {};
    for (var i = 0; i < listaSwitchCoberValosit.length; i++) {
        mapaSwitchCoberValosit[listaSwitchCoberValosit[i]] = 'S';
    }
    debug('mapaSwitchCoberValosit:', mapaSwitchCoberValosit);
    _p25_smap1.mapaSwitchCoberValosit = mapaSwitchCoberValosit;
} catch (e) {
    debugError('WARNING al manejar switch de cobertura tvalosit', e);
}

var posicionExtraprimaOcup;

//se declara el mapa como cotcol para el archivo comun funcionesCotizacionGrupo.js
var _cotcol_smap1 = _p25_smap1;
debug('_cotcol_smap1:',_cotcol_smap1);
//se declara el mapa como cotcol para el archivo comun funcionesCotizacionGrupo.js

//Para la pantalla de agentes
var inputCdunieco = _p25_smap1.cdunieco;
var inputCdramo   = _p25_smap1.cdramo;
var inputEstado   = _p25_smap1.estado;
var inputNmpoliza = _p25_smap1.nmpoliza;
//Para la pantalla de agentes

var _p25_TARIFA_LINEA       = 1;
var _p25_TARIFA_MODIFICADA  = 2;
var _p25_semaforoPlanChange = true;
var _p25_ntramite           = Ext.isEmpty(_p25_smap1.ntramite) ? false      : _p25_smap1.ntramite;
var _p25_ntramiteVacio      = Ext.isEmpty(_p25_smap1.ntramiteVacio) ? false : _p25_smap1.ntramiteVacio;
debug('_p25_ntramite:',_p25_ntramite,'_p25_ntramiteVacio:',_p25_ntramiteVacio);

var inputNtramite = _p25_ntramite;

var _RamoRecupera = ((Ramo.Recupera == _p25_smap1.cdramo) && (TipoSituacion.RecuperaColectivo == _p25_smap1.cdtipsit))? true : false;

var _p25_clasif;
var _p25_storeGrupos;
var _p25_tabGrupos;
var _p25_tabGruposLineal;
var _p25_tabGruposModifi;
var _p25_gridTarifas;
var _p25_valoresFactores = null;
var _p25_resubirCenso    = 'N';

var _p25_filtroCobTimeout;

var _p22_parentCallback = false;
var _callbackContPrincipal = false;
var _contratanteSaved = false;
var _callbackDomicilioAseg = false;

var _ventanaClausulas;
var _ventanaGridAgentesSuperior;

var _callbackAseguradoExclusiones = function (){
    _ventanaClausulas.close();
};

var _p_25_panelPrincipal;

var _ventanaPersonas;

var codpostalDefinitivo;
var cdedoDefinitivo;
var cdmuniciDefinitivo;

_defaultNmordomProspecto = undefined;// valor default del numero de domicilio del prospecto
var nmorddomProspecto = _defaultNmordomProspecto; 

var _p25_editorNombreGrupo=
{
    xtype       : 'textfield'
    ,allowBlank : false
    ,minLength  : 3
};

var _p25_editorPlan = <s:property value="imap.editorPlanesColumn" />.editor;
_p25_editorPlan.on('change',_p25_editorPlanChange);
_p25_editorPlan.on('blur',_p25_editorPlanHijos);
debug('_p25_editorPlan:',_p25_editorPlan);

var _p25_colsExtColumns =
[
    <s:if test='%{getImap().get("colsExtColumns")!=null}'>
        <s:property value="imap.colsExtColumns" escapeHtml="false" />
    </s:if>
];
debug('_p25_colsExtColumns:',_p25_colsExtColumns);

_p25_colsBaseColumns =
[
    <s:if test='%{getImap().get("colsBaseColumns")!=null}'>
        <s:property value="imap.colsBaseColumns" escapeHtml="false" />
    </s:if>
];
debug('_p25_colsBaseColumns:',_p25_colsBaseColumns);

for(var i=0;i<_p25_colsExtColumns.length;i++)
{
    var esExt = true;
    _p25_colsExtColumns[i]['flex']=0;
    _p25_colsExtColumns[i]['width']=150;
    for(var j=0;j<_p25_colsBaseColumns.length;j++)
    {
        if(i==0)
        {
            _p25_colsBaseColumns[j]['flex']=0;
            _p25_colsBaseColumns[j]['width']=150;
        }
        if(_p25_colsExtColumns[i].dataIndex==_p25_colsBaseColumns[j].dataIndex)
        {
            esExt = false;
        }
    }
    if(esExt)
    {
        _p25_colsExtColumns[i]['hidden']=_p25_smap1.LINEA_EXTENDIDA=='N';
    }
}
debug('_p25_colsExtColumns despues de marcar extendidas:',_p25_colsExtColumns);

var _p25_factoresColumns =
[
    <s:if test='%{getImap().get("factoresColumns")!=null}'>
        <s:property value="imap.factoresColumns" escapeHtml="false" />
    </s:if>
];
debug('_p25_factoresColumns:',_p25_factoresColumns);

var _p25_itemsRiesgo =
[
    <s:if test='%{getImap().get("itemsRiesgo")!=null}'>
        <s:property value="imap.itemsRiesgo" escapeHtml="false" />
    </s:if>
];

for(var i=0;i<_p25_itemsRiesgo.length;i++)
{
    _p25_itemsRiesgo[i].name='tvalopol_'+_p25_itemsRiesgo[i].name;
}
debug('_p25_itemsRiesgo:',_p25_itemsRiesgo);

var _p82_callback;
var _p47_callback;

var listaSinPadre = [];

var _cotcol_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_cotcol_flujo:', _cotcol_flujo, '.');
////// variables //////

Ext.onReady(function()
{
	debug('si entra a la vista');
    /*_grabarEvento('COTIZACION','ACCCOTIZA'
                  ,_p25_ntramiteVacio?_p25_ntramiteVacio:(_p25_ntramite?_p25_ntramite:''),_p25_smap1.cdunieco,_p25_smap1.cdramo);*/

    Ext.Ajax.timeout = 60*60*1000;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

    ////// modelos //////
    var _p25_colsBaseFields =
    [
        <s:if test='%{getImap().get("colsBaseFields")!=null}'>
            <s:property value="imap.colsBaseFields" escapeHtml="false" />
        </s:if>
    ];
    debug('_p25_colsBaseFields:',_p25_colsBaseFields);
    
    var _p25_colsExtFields =
    [
        <s:if test='%{getImap().get("colsExtFields")!=null}'>
            <s:property value="imap.colsExtFields" escapeHtml="false" />
        </s:if>
    ];
    debug('_p25_colsExtFields:',_p25_colsExtFields);
    
    var _p25_factoresFields =
    [
        <s:if test='%{getImap().get("factoresFields")!=null}'>
            <s:property value="imap.factoresFields" escapeHtml="false" />
        </s:if>
    ];
    debug('_p25_factoresFields:',_p25_factoresFields);
    
    var _p25_modeloGrupoFields =
    [
        'letra'
        ,'nombre'
        ,'cdplan'
    ];
    
    for(var i=0;i<_p25_factoresFields.length;i++)
    {
        _p25_modeloGrupoFields.push(_p25_factoresFields[i]);
    }
    
    for(var i=0;i<_p25_colsBaseFields.length;i++)
    {
        _p25_modeloGrupoFields.push(_p25_colsBaseFields[i]);
    }
    
    for(var i=0;i<_p25_colsExtFields.length;i++)
    {
        var esBase = false;
        for(var j=0;j<_p25_colsBaseFields.length;j++)
        {
            if(_p25_colsBaseFields[j].name==_p25_colsExtFields[i].name)
            {
                esBase = true;
            }
        }
        if(!esBase)
        {
            _p25_modeloGrupoFields.push(_p25_colsExtFields[j]);
        }
    }
    
    debug('_p25_modeloGrupoFields:',_p25_modeloGrupoFields);
    
    Ext.define('_p25_modeloGrupo',
    {
        extend  : 'Ext.data.Model'
        ,fields : _p25_modeloGrupoFields
    });
    
    Ext.define('RFCPersona',
    {
        extend  : 'Ext.data.Model'
        ,fields : ["RFCCLI","NOMBRECLI","FENACIMICLI","DIRECCIONCLI","CLAVECLI","DISPLAY", "CDIDEPER"
        ,'CODPOSTAL','CDEDO','CDMUNICI','DSDOMICIL','NMNUMERO','NMNUMINT','CDIDEEXT','NMORDDOM']
    });
    
    Ext.define('_p25_modeloTarifaEdad',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'EDAD'
            ,'HOMBRES'
            ,'MUJERES'
            ,'TARIFA_UNICA_HOMBRES'
            ,'TARIFA_UNICA_MUJERES'
            ,'TARIFA_TOTAL_HOMBRES'
            ,'TARIFA_TOTAL_MUJERES'
            ,'DERPOL_TOTAL_GENERAL'
            ,'RECARGOS_TOTAL_GENERAL'
            ,'IVA_TOTAL_GENERAL'
        ]
    });
    
    Ext.define('_p25_modeloTarifaCobertura',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'DSGARANT'
            ,{type:'float',name:'PRIMA_PROMEDIO'}
        ]
    });
    
    var _p25_extraprimaFields =
    [
        { name : 'NMSITUAC' , type : 'int' }
        ,'nombre'
        ,'familia'
        ,'titular'
        ,'parentesco'
        ,'agrupador'
        ,'cdatexoc'
        <s:if test='%{getImap().containsKey("extraprimasFields")&&getImap().get("extraprimasFields")!=null}'>
            ,<s:property value="imap.extraprimasFields" escapeHtml="false" />
        </s:if>
    ];
    debug('_p25_extraprimaFields:',_p25_extraprimaFields);
    Ext.define('_p25_modeloExtraprima',
    {
        extend  : 'Ext.data.Model'
        ,idProperty : 'nmsituac'
        ,fields     : _p25_extraprimaFields
        ,mode       : 'MULTI'
    });
    
    Ext.define('_p25_modeloAsegurados',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value='%{getImap().containsKey("aseguradosFields")?getImap().get("aseguradosFields").toString():""}' /> ]
    });
    
    Ext.define('_p25_modeloRecuperados',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value='%{getImap().containsKey("recuperadosFields")?getImap().get("recuperadosFields").toString():""}' /> ]
    });
    
    Ext.define('_p25_vpModelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'concepto'
            ,{ name : 'importe'   , type :'float' }
            ,{ name : 'subgrupo1' , type :'float' }
            ,{ name : 'subgrupo2' , type :'float' }
            ,{ name : 'subgrupo3' , type :'float' }
            ,{ name : 'subgrupo4' , type :'float' }
            ,{ name : 'subgrupo5' , type :'float' }
            ,{ name : 'subgrupo6' , type :'float' }
            ,{ name : 'subgrupo7' , type :'float' }
            ,{ name : 'subgrupo8' , type :'float' }
            ,{ name : 'subgrupo9' , type :'float' }
        ]
    });
    
    Ext.define('_p25_modeloRevisionAsegurado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'CDUNIECO'
            ,'CDRAMO'
            ,'ESTADO'
            ,'NMPOLIZA'
            ,'CDGRUPO'
            ,'NMSITUAC'
            ,'PARENTESCO'
            ,'NOMBRE'
            ,'EDAD'
            ,'SEXO'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p25_storeGrupos = Ext.create('Ext.data.Store',
    {
        model : '_p25_modeloGrupo'
    });
    ////// stores //////
    
    ////// componentes //////
    var botoneslinea = [];
    if(_p25_smap1.DETALLE_LINEA=='S')
    {
        botoneslinea.push(
        {
            tooltip   : 'Editar'
            ,icon     : '${ctx}/resources/fam3icons/icons/pencil.png'
            ,handler  : _p25_editarGrupoClic
        });
        if ([
            RolSistema.SuscriptorTecnico,
            RolSistema.SupervisorTecnico,
            RolSistema.SubdirectorSalud,
            RolSistema.DirectorSalud
        ].indexOf(_cotcol_smap1.cdsisrol) !== -1) {
            botoneslinea.push(
            {
                tooltip   : 'Borrar detalle'
                ,icon     : '${ctx}/resources/fam3icons/icons/error_delete.png'
                ,handler  : _p25_borrarDetalleGrupoClic
            });
        }
    }
    if(!_p25_ntramite || _cotcol_smap1.modificarTodo === true)
    {
        botoneslinea.push(
        {
            tooltip  : 'Borrar subgrupo'
            ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
            ,handler : _p25_borrarGrupoClic
        });
    }
    if(_p25_smap1.ASEGURADOS=='S')
    {
        botoneslinea.push(
        {
            tooltip  : 'Asegurados'
            ,icon    : '${ctx}/resources/fam3icons/icons/group.png'
            ,handler : _cotcol_aseguradosClic
        });
    }
    if(_p25_smap1.EXTRAPRIMAS=='S')
    {
        botoneslinea.push(
        {
            tooltip  : 'Revisar extraprimas'
            ,icon    : '${ctx}/resources/fam3icons/icons/group_error.png'
            ,handler : _p25_revisarAseguradosClic
        });
    }
    
    _p25_tabGruposLinealCols =
    [
        {
            header     : 'ID'
            ,dataIndex : 'letra'
            ,width     : 40
        }
        ,{
            header     : 'NOMBRE'
            ,dataIndex : 'nombre'
            ,width     : 150
            ,editor    : _p25_editorNombreGrupo
        }
        ,{
            header     : 'PLAN'
            ,dataIndex : 'cdplan'
            ,width     : 100
            ,editor    : _p25_editorPlan
            ,renderer  : function(v)
            {
                return rendererColumnasDinamico(v,'cdplan');
            }
        }
    ];
    
    Ext.Array.each(_p25_colsExtColumns,function(col)
    {
        _p25_tabGruposLinealCols.push(col);
    });
    
    _p25_tabGruposLinealCols.push(
    {
        xtype         : 'actioncolumn'
        ,sortable     : false
        ,menuDisabled : true
        ,width        : (botoneslinea.length*20) + 20
        ,items        : botoneslinea
    });
    
    _p25_tabGruposLineal =
    {
        title     : 'RESUMEN SUBGRUPOS'
        ,itemId   : '_p25_tabGruposLineal'
        ,defaults : { style : 'margin:5px' }
        ,border   : 0
        ,items    : Ext.create('Ext.grid.Panel',
        {
            tbar     :
            [
                {
                    text     : 'Agregar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                    ,handler : _p25_agregarGrupoClic
                    ,hidden  : _p25_ntramite && _cotcol_smap1.modificarTodo === false ? true : false
                }
            ]
            ,columns : _p25_tabGruposLinealCols
            ,store   : _p25_storeGrupos
            ,height  : 250
            ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
            {
                clicksToEdit  : 1
                ,errorSummary : false
                ,listeners    :
                {
                    beforeedit : function(editor,context)
                    {
                        _p25quitarFiltroSumaAsegRamo11();
                    	debug( 'VEditor: ', editor);
                    	debug( 'VContext: ', context);
                        if(!(
                            !_p25_ntramite||_p25_ntramiteVacio||(!Ext.isEmpty(_p25_smap1.sincenso)&&_p25_smap1.sincenso=='S')
                        ))
                        {
                            if (_cotcol_smap1.modificarTodo === false) {
                                if ([
                                        RolSistema.SuscriptorTecnico,
                                        RolSistema.SupervisorTecnico,
                                        RolSistema.SubdirectorSalud,
                                        RolSistema.DirectorSalud
                                    ].indexOf(_cotcol_smap1.cdsisrol) == -1) {
                                    return false;
                                }
                            }
                        }
                       // Iteramos sobre cada  columna					
					   for(var i in _p25_tabGruposLinealCols)
                        {
                            // Checamos que existan datos en el store y el listener que desencadena el evento
                            if(!Ext.isEmpty(_p25_tabGruposLinealCols[i].editor) && !Ext.isEmpty(_p25_tabGruposLinealCols[i].editor.store) && _p25_tabGruposLinealCols[i].editor.hasListener('blur'))
                            {
                                 // Si la siguiente columna es hija y tiene las caracteristocas del padre
                                 if(!Ext.isEmpty(_p25_tabGruposLinealCols[Number(i)+1].editor) && !Ext.isEmpty(_p25_tabGruposLinealCols[Number(i)+1].editor.store))
                                 {
                                     //dataIndex contiene los valores que apuntamos dentro del hijo
                                     var editorComboHijo = _p25_tabGruposLinealCols[Number(i)+1].editor;
                                     editorComboHijo.store.respaldoValor = context.record.get(_p25_tabGruposLinealCols[Number(i)+1].dataIndex);
                                     editorComboHijo.store.padre = _p25_tabGruposLinealCols[Number(i)+1].editor;
                                     editorComboHijo.store.beforeedit = true;
                                     //se comenta el loading porque no siempre carga a la derecha
                                     //editorComboHijo.setLoading(true);
                                     editorComboHijo.store.on(
                                     {
                                         // Si existe la funcion load, seteamos el valor directamente 
                                         load: function(store, records, successful, eOpts)
                                         {
                                             if(store.beforeedit===true)
                                             {
                                                 store.beforeedit=false;
                                                 debug('load '+store.respaldoValor);
                                                 store.padre.setValue(store.respaldoValor);
                                                 //se comenta el loading porque no siempre carga a la derecha
                                                 //store.padre.setLoading(false);
                                             }
                                         }
                                     });
                                 }

                                 setTimeout(
                                     function(j)
                                     {   
                                         // blur que espera 1s a que las listas esten cargadas
                                         debug('se dispara blur '+j);
                                         _p25_tabGruposLinealCols[j].editor.fireEvent('blur',_p25_tabGruposLinealCols[j].editor);
                                     }
                                     ,1000
                                     ,i
                                 );
                            }
                        }
                    },
                    edit : function () {
                        _p25quitarFiltroSumaAsegRamo11();
                    },
                    canceledit : function () {
                        _p25quitarFiltroSumaAsegRamo11();
                    }
                }
            })
        })
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Continuar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function()
                {
                    _p25_setActiveConcepto();
                }
            }
        ]
    };
    
    _p25_tabGruposModifiCols =
    [
        {
            header     : 'ID'
            ,dataIndex : 'letra'
            ,width     : 40
        }
        ,{
            header     : 'NOMBRE'
            ,dataIndex : 'nombre'
            ,width     : 150
            ,editor    : _p25_editorNombreGrupo
        }
        ,{
            header     : 'PLAN'
            ,dataIndex : 'cdplan'
            ,width     : 100
            ,editor    : _p25_editorPlan
            ,renderer  : function(v)
            {
                return rendererColumnasDinamico(v,'cdplan');
            }
        }
    ];
    
    Ext.Array.each(_p25_colsBaseColumns,function(col)
    {
        _p25_tabGruposModifiCols.push(col);
    });
    
    //Se quita agregar botón modificar sin ninguna restricción (EGS)
    var botonesModificada =
    [
    ];
    //Restringe por cdsisrol y cdtipsit, agregar botón actualizar (EGS)
    if(!((_p25_smap1.cdsisrol=='EJECUTIVOCUENTA' || _p25_smap1.cdsisrol=='EJECUTIVOINTERNO' || _p25_smap1.cdsisrol=='MESADECONTROL')&&_p25_smap1.cdtipsit =='RC'))
    {
        botonesModificada.push(
        {
            tooltip   : 'Editar'
            ,icon     : '${ctx}/resources/fam3icons/icons/pencil.png'
            ,handler  : _p25_editarGrupoClic
        });
        if ([
            RolSistema.SuscriptorTecnico,
            RolSistema.SupervisorTecnico,
            RolSistema.SubdirectorSalud,
            RolSistema.DirectorSalud
        ].indexOf(_cotcol_smap1.cdsisrol) !== -1) {
            botonesModificada.push(
            {
                tooltip   : 'Borrar detalle'
                ,icon     : '${ctx}/resources/fam3icons/icons/error_delete.png'
                ,handler  : _p25_borrarDetalleGrupoClic
            });
        }
    }
    
    if(!_p25_ntramite || _cotcol_smap1.modificarTodo === true)
    {
        botonesModificada.push(
        {
            tooltip   : 'Borrar subgrupo'
            ,icon     : '${ctx}/resources/fam3icons/icons/delete.png'
            ,handler  : _p25_borrarGrupoClic
        });
    }
    if(_p25_smap1.ASEGURADOS=='S')
    {
        botonesModificada.push(
        {
            tooltip  : 'Asegurados'
            ,icon    : '${ctx}/resources/fam3icons/icons/group.png'
            ,handler : _cotcol_aseguradosClic
        });
    }
    if(_p25_smap1.EXTRAPRIMAS=='S')
    {
        botonesModificada.push(
        {
            tooltip  : 'Revisar extraprimas'
            ,icon    : '${ctx}/resources/fam3icons/icons/group_error.png'
            ,handler : _p25_revisarAseguradosClic
        });
    }
    
    _p25_tabGruposModifiCols.push(
    {
        xtype         : 'actioncolumn'
        ,sortable     : false
        ,menuDisabled : true
        ,width        : (botonesModificada.length*20) + 20
        ,items        : botonesModificada
    });
    
    _p25_tabGruposModifi =
    {
        title     : 'RESUMEN SUBGRUPOS'
        ,itemId   : '_p25_tabGruposModifi'
        ,defaults : { style : 'margin:5px' }
        ,border   : 0
        ,items    : Ext.create('Ext.grid.Panel',
        {
            tbar     :
            [
                {
                    text     : 'Agregar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                    ,handler : _p25_agregarGrupoClic
                    ,hidden  : _p25_ntramite && _cotcol_smap1.modificarTodo === false ? true : false
                }
            ]
            ,columns : _p25_tabGruposModifiCols
            ,store   : _p25_storeGrupos
            ,height  : 250
            ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
            {
                clicksToEdit  : 1
                ,errorSummary : true
                ,listeners    :
                {
                    beforeedit : function(editor,context)
                    {
                        _p25quitarFiltroSumaAsegRamo11();
                    	debug( 'VEditor: ', editor);
                        debug( 'VContext: ', context);
                        if(!(
                            !_p25_ntramite||_p25_ntramiteVacio||(!Ext.isEmpty(_p25_smap1.sincenso)&&_p25_smap1.sincenso=='S')
                        ))
                        {
                            if (_cotcol_smap1.modificarTodo === false) {
                                if ([
                                        RolSistema.SuscriptorTecnico,
                                        RolSistema.SupervisorTecnico,
                                        RolSistema.SubdirectorSalud,
                                        RolSistema.DirectorSalud
                                    ].indexOf(_cotcol_smap1.cdsisrol) == -1) {
                                    return false;
                                }
                            }
                        }
                        // Iteramos sobre cada columna:                      
    					for(var i in _p25_tabGruposModifiCols)
						{
                            // Checamos que existan datos en el store y el listener que desencadena el evento
						    if(!Ext.isEmpty(_p25_tabGruposModifiCols[i].editor) && !Ext.isEmpty(_p25_tabGruposModifiCols[i].editor.store) && _p25_tabGruposModifiCols[i].editor.hasListener('blur'))
						    {
                                 // Si la siguiente columna es hija y tiene las caracteristocas del padre
						    	 if(!Ext.isEmpty(_p25_tabGruposModifiCols[Number(i)+1].editor) && !Ext.isEmpty(_p25_tabGruposModifiCols[Number(i)+1].editor.store))
						    	 {
						    	     _p25_tabGruposModifiCols[Number(i)+1].editor.store.respaldoValor = context.record.get(_p25_tabGruposModifiCols[Number(i)+1].dataIndex);
						    	     _p25_tabGruposModifiCols[Number(i)+1].editor.store.padre = _p25_tabGruposModifiCols[Number(i)+1].editor;
						    	     _p25_tabGruposModifiCols[Number(i)+1].editor.store.beforeedit = true;
						    	     //se comenta el loading porque no siempre hereda el de la derecha
						    	     //_p25_tabGruposModifiCols[Number(i)+1].editor.setLoading(true);
						    	     _p25_tabGruposModifiCols[Number(i)+1].editor.store.on(
						    	     {
                                         // Si existe la funcion load, seteamos el valor directamente 
						    	         load: function(store, records, successful, eOpts)
						    	         {
						    	             if(store.beforeedit===true)
						    	             {
						    	                 store.beforeedit=false;
						    	                 debug('load '+store.respaldoValor);
						    	                 store.padre.setValue(store.respaldoValor);
						    	                 //se comenta el loading porque no siempre hereda el de la derecha
						    	                 //store.padre.setLoading(false);
						    	             }
						    	         }
						    	     });
						    	 }

						         setTimeout(
						             function(j)
						             {
                                         // blur que espera 1s a que las listas esten cargadas:
						                 debug('se dispara blur '+j);
						                 _p25_tabGruposModifiCols[j].editor.fireEvent('blur',_p25_tabGruposModifiCols[j].editor);
						             }
						             ,1000
						             ,i
						         );
						    }
						}                      
                    },
                    edit : function () {
                        _p25quitarFiltroSumaAsegRamo11();
                    },
                    canceledit : function () {
                        _p25quitarFiltroSumaAsegRamo11();
                    }
                }
            })
        })
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Continuar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function()
                {
                	_p25_setActiveConcepto();
				}
            }
        ]
    };
    ////// componentes //////
    
    ////// contenido //////
    _p_25_panelPrincipal = Ext.create('Ext.tab.Panel',
    {
        itemId     : '_p25_tabpanel'
        ,border    : 0
        ,items     :
        [
            {
                title       : 'CONCEPTO'
                ,itemId     : '_p25_tabConcepto'
                ,border     : 0
                ,defaults   : { style : 'margin:5px' }
                ,items      :
                [
                    Ext.create('Ext.form.Panel',
                    {
                        border : 0
                        ,xtype : 'form'
                        ,url   : _p25_urlSubirCenso
                        ,items :
                        [
                            {
                                xtype   : 'fieldset'
                                ,hidden: (_p25_smap1.cdsisrol=='SUSCRIPTOR'&& (_p25_smap1.status-0==19 || _p25_smap1.status-0==21 || _p25_smap1.status-0==23) ) ? true :false
                                ,title  : '<span style="font:bold 14px Calibri;">DATOS DEL CONTRATANTE</span>'
                                ,disabled : ( !Ext.isEmpty(_p25_smap1.estado) && _p25_smap1.estado == 'M' )
                                ,layout :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                }
                                ,items  : [ <s:property value="imap.itemsContratante" /> ]
                            },
                            Ext.create('Ext.panel.Panel',
					        {
					            itemId      : '_p29_clientePanel'
					            ,title      : 'DATOS DEL CONTRATANTE'
					            ,hidden: (_p25_smap1.cdsisrol=='SUSCRIPTOR'&& (_p25_smap1.status-0==19 || _p25_smap1.status-0==21 || _p25_smap1.status-0==23) ) ? false :true
					            ,height     : 400
					            ,autoScroll : true
					            ,tbar     :
					            [
					                {
					                    text     : 'Cambiar Contratante'
					                    ,icon    : '${ctx}/resources/fam3icons/icons/user_go.png'
					                    ,handler : function(){
											destruirContLoaderPersona();	 
											
				                            _fieldById('_p29_clientePanel').getLoader().destroy();
				                            
				                            _fieldById('_p29_clientePanel').loader = new Ext.ComponentLoader({
							                    url       : _p25_urlPantallaPersonas
							                    ,scripts  : true
							                    ,autoLoad : false
							                    ,ajaxOptions: {
							                            method: 'POST'
							                     }
							                });
							                
				                            _fieldById('_p29_clientePanel').getLoader().load({
										            params: {
										                'smap1.cdperson' : '',
										                'smap1.cdideper' : '',
										                'smap1.cdideext' : '',
										                'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
										                'smap1.polizaEnEmision': 'S',
										                'smap1.esCargaClienteNvo' : 'N' ,
										                'smap1.ocultaBusqueda' : 'S' ,
										                'smap1.cargaCP' : '',
										                'smap1.cargaTipoPersona' : '',
										                'smap1.cargaSucursalEmi' : _p25_smap1.cdunieco,
										                'smap1.activaCveFamiliar': 'N',
										                'smap1.modoRecuperaDanios': 'N',
										                'smap1.modoSoloEdicion': 'N',
										                'smap1.contrantantePrincipal': 'S',
										                'smap1.tomarUnDomicilio' : 'S'
										            }
										     });
										     
										     _fieldByName('cdperson').setValue('');
										     
										     _contratanteSaved = false;
										     
										     _callbackContPrincipal = function(json){
    	
										    	debug( 'Datos a cargar', json.smap1);
										    	
										    	_fieldByName('cdrfc')    .setValue(json.smap1.CDRFC);
										        _fieldByName('cdperson') .setValue(json.smap1.CDPERSON);
										        
												codpostalDefinitivo = json.smap1.CDPOSTAL;
												cdedoDefinitivo = json.smap1.CDEDO;
												cdmuniciDefinitivo = json.smap1.CDMUNICI;
												
												var params = {
												    smap1 :
												    {
												        cdunieco : _p25_smap1.cdunieco
												        ,cdramo  : _p25_smap1.cdramo
												        ,estado  : _p25_smap1.estado
												        ,nmpoliza: _p25_smap1.nmpoliza
												        ,nmsuplem: Ext.isEmpty(_p25_smap1.nmsuplem)?'0':_p25_smap1.nmsuplem
												        ,cdperson: json.smap1.CDPERSON
												        ,codpostal: json.smap1.CDPOSTAL
												        ,cdedo: json.smap1.CDEDO
												        ,cdmunici: json.smap1.CDMUNICI
												        ,nmorddom: json.smap1.NMORDDOM
												        ,confirmaEmision: 'S'
												    }
												};
											
												Ext.Ajax.request(
											        {
										            url       : _p25_urlGuardarContratanteColectivo
										            ,jsonData : params
										            ,success  : function(response)
										            {
										                var jsonCont = Ext.decode(response.responseText);
										                if(jsonCont.exito){
										                	mensajeCorrecto('Aviso','Contratante Guardado Correctamente.');
										                	_contratanteSaved = true;
										                }
										                else
										                {
										                    mensajeWarning(jsonCont.respuesta);
										                }
										            }
										            ,failure  : function()
										            {
										                errorComunicacion();
										            }
										        });
										        
											};
										     
							            }
					                    
					                }
					            ]
					            ,loader     :
					            {
					                url       : _p25_urlPantallaPersonas
				                    ,scripts  : true
				                    ,autoLoad : false
				                    ,ajaxOptions: {
				                            method: 'POST'
				                     }
				                }
					        })
                            ,{
                                xtype     : 'fieldset'
                                ,title    : '<span style="font:bold 14px Calibri;">INFORMACI&Oacute;N DEL RIESGO</span>'
                                ,itemId   : '_p25_fieldsetRiesgo'
                                ,layout   :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                }
                                ,defaults : {  style : 'margin : 5px;' }
                                ,items    : _p25_itemsRiesgo
                            }
                            ,{
                                xtype     : 'fieldset'
                                ,title    : '<span style="font:bold 14px Calibri;">INFORMACI&Oacute;N DE LA P&Oacute;LIZA</span>'
                                ,defaults : { style : 'margin:5px;' }
                                ,layout   :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                }
                                ,items    :
                                [
                                    Ext.create('Ext.form.field.Display',
                                    {
                                        fieldLabel  : 'PRODUCTO'
                                        ,labelWidth : 250
                                        ,value      : 'Cargando...'
                                    })
                                    ,Ext.create('Ext.form.field.Number',
                                    {
                                        fieldLabel : 'TR&Aacute;MITE'
                                        ,itemId    : '_p25_fieldNtramite'
                                        ,readOnly  : true
                                        ,name      : 'ntramite'
                                    })
                                    ,Ext.create('Ext.form.field.Number',
                                    {
                                        fieldLabel : 'COTIZACI&Oacute;N'
                                        ,itemId    : '_p25_fieldNmpoliza'
                                        ,readOnly  : true
                                        ,name      : 'nmpoliza'
                                    })
                                    ,Ext.create('Ext.form.field.Date',
                                    {
                                        format      : 'd/m/Y'
                                        ,fieldLabel : 'FECHA INICIO VIGENCIA'
                                        ,name       : 'feini'
                                        ,allowBlank : false
                                        ,value      : new Date()
                                        ,listeners  :
                                        {
                                             change : function(field,value)
                                             {
                                                 try
                                                 {
                                                     Ext.ComponentQuery.query('#fechaFinVigencia')[0].setValue(Ext.Date.add(value,Ext.Date.YEAR,1));
                                                 }
                                                 catch (e) {}
                                             }
                                        }
                                    })
                                    ,Ext.create('Ext.form.field.Date',
                                    {
                                        format      : 'd/m/Y'
                                        ,fieldLabel : 'FECHA FIN VIGENCIA'
                                        ,itemId     : 'fechaFinVigencia'
                                        ,name       : 'fefin'
                                        ,allowBlank : false
                                        ,readOnly   : true
                                        ,value      : Ext.Date.add(new Date(),Ext.Date.YEAR,1)
                                    })
                                    ,<s:property value="imap.comboFormaPago" />
                                    ,<s:property value="imap.comboRepartoPago" />
                                    ,{
                                        xtype       : 'numberfield'
                                        ,fieldLabel : 'PORCENTAJE A CARGO DEL CLIENTE'
                                        ,name       : 'pcpgocte'
                                        ,allowBlank : false
                                        ,minValue   : 0
                                        ,maxValue   : 100
                                        ,listeners  :
                                        {
                                            change : function(comp,val)
                                            {
                                                _fieldByName('pcpgotit').setValue(100-val);
                                            }
                                        }
                                    },{
                                        xtype       : 'numberfield'
                                        ,fieldLabel : 'PORCENTAJE A CARGO DEL TITULAR'
                                        ,name       : 'pcpgotit'
                                        ,readOnly   : true
                                    }
                                    ,<s:property value="imap.comboPool" />
                                    ,<s:property value="imap.datosPoliza" />
                                    ,{
                                        xtype    : 'button'
                                        ,text    : 'Exclusiones/Extraprimas (Cl&aacute;usulas)'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/lock.png'
                                        ,hidden  : Ext.isEmpty(_p25_smap1.nmpoliza)
                                        ,handler : function(){ _p25_crearVentanaClausulas(); }
                                    }
                                ]
                            }
                            ,{
                                xtype   : 'fieldset'
                                ,title  : '<span style="font:bold 14px Calibri;">DATOS DEL AGENTE</span>'
                                ,itemId : '_p25_fieldsetDatosAgente'
                                ,items  : [ <s:property value="imap.itemsAgente" /> ]
                                ,layout :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                }
                            }                        
                            ,{
                                xtype     : 'fieldset'
                                ,title    : '<span style="font:bold 14px Calibri;">NÚMERO DE ASEGURADOS</span>'
                                ,defaults : { style : 'margin:5px;' }
                                ,hidden   : _p25_ntramite ? true : false
                                ,items    :
                                [
                                    {
                                        xtype    : 'button'
                                        ,text    : 'De 10 a 49 asegurados'
                                        ,scale   : 'medium'
                                        ,handler : _p25_construirLinea
                                    }
                                    ,{
                                        xtype    : 'button'
                                        ,text    : '50 o más asegurados'
                                        ,scale   : 'medium'
                                        ,handler : _p25_construirModificada
                                    }
                                ]
                            }
                            ,{
                                xtype     : 'fieldset'
                                ,title    : '<span style="font:bold 14px Calibri;">CENSO</span>'
                                ,defaults : { style : 'margin:5px;' }
                                ,hidden   : _p25_ntramite&&_p25_smap1.sincenso!='S' && _cotcol_smap1.modificarTodo === false ? true : false
                                ,items    :
                                [
                                    {
                                        xtype        : 'fieldcontainer'
                                        ,fieldLabel  : 'Tipo de censo'
                                        ,defaultType : 'radiofield'
                                        ,defaults    : { style : 'margin : 5px;' }
                                        ,layout      : 'hbox'
                                        ,hidden      : _p25_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba
                                        ,items       :
                                        [
                                            {
                                                boxLabel    : 'Por asegurado'
                                                ,name       : 'tipoCenso'
                                                ,inputValue : 'solo'
                                                ,checked    : true
                                            }
                                            ,{
                                                boxLabel    : 'Agrupado por edad'
                                                ,name       : 'tipoCenso'
                                                ,inputValue : 'grupo'
                                            }
                                        ]
                                    }
                                    ,{
                                        xtype       : 'filefield'
                                        ,fieldLabel : 'Censo de asegurados'
                                        ,name       : 'censo'
                                        ,buttonText : 'Examinar...'
                                        ,allowBlank : _p25_ntramite&&_p25_smap1.sincenso!='S' && _cotcol_smap1.modificarTodo === false ? true : false
                                        ,buttonOnly : false
                                        ,width      : 450
                                        ,cAccept    : ['xls','xlsx']
                                        ,msgTarget  : 'side'
                                        ,listeners  :
                                        {
                                            change : function(me)
                                            {
                                                //alert('1');
                                                var indexofPeriod = me.getValue().lastIndexOf("."),
                                                uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                                if (!Ext.Array.contains(this.cAccept, uploadedExtension))
                                                {
                                                    centrarVentanaInterna(Ext.MessageBox.show(
                                                    {
                                                        title   : 'Error de tipo de archivo',
                                                        msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                                        buttons : Ext.Msg.OK,
                                                        icon    : Ext.Msg.WARNING
                                                    }));
                                                    me.reset();
                                                }
                                            }
                                        }
                                    }
                                ]
                            }
                        ]
                        ,buttonAlign : 'center'
                        ,buttons:[{
                            xtype :'panel',
                            defaults : { style : 'margin:5px' },
                            border   : 0,
                            width    : 950,
                            ui       :'footer',
                            items    : [
                                <s:if test='%{getImap().get("botones")!=null}'>
                                    <s:property value="imap.botones" />,
                                </s:if>
                                {
                                    text     : 'Limpiar'
                                    ,xtype   : 'button'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                    ,handler : _p25_cotizarNueva
                                    ,hidden  : _p25_ntramite ? true : false
                                }
                            ],
                            listeners : {
                                afterrender : function (me) {
                                    if (!Ext.isEmpty(_cotcol_flujo) && _cotcol_flujo.aux === 'RSTN') {
                                        var ck = 'Quitando botones rstn';
                                        try {
                                            var botones = Ext.ComponentQuery.query('button', me);
                                            debug('botones rstn:', botones);
                                            for (var i = 0; i < botones.length; i++) {
                                                var boton = botones[i];
                                                debug(boton.text, boton.handler.toString());
                                                if (boton.handler.toString().indexOf('turnar') != -1) {
                                                    debug('boton hide');
                                                    boton.hide();
                                                }
                                            }
                                        } catch (e) {
                                            debugError(e, 'WARNING al quitar botones rstn. ' + ck);
                                        }
                                    }
                                }
                            }
                        }]
                        /*,buttons     :
                        [
                            <s:if test='%{getImap().get("botones")!=null}'>
                                <s:property value="imap.botones" />,
                            </s:if>
                            {
                                text     : 'Limpiar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                ,handler : _p25_cotizarNueva
                                ,hidden  : _p25_ntramite ? true : false
                            }
                        ]*/
                    })
                ]
            }
        ]
        ,listeners :
        {
            tabchange : function(panel,newtab,oldtab,e)
            {
                var grids=Ext.ComponentQuery.query('[xtype=grid]',oldtab);
                debug('grids:',grids);
                if(grids.length>0)
                {
                    var conCambios = false;
                    $.each(grids,function(i,grid)
                    {
                        var records = grid.getStore().getModifiedRecords();
                        debug('modificados:',records.length);
                        if(
                            grid.up('panel').title!='RESUMEN SUBGRUPOS'
                            &&records.length>0
                            &&grid.title!='FACTORES DEL SUBGRUPO'
                            )
                        {
                            conCambios = true;
                            debug('cambios:',records);
                        }
                    });
                    if(conCambios)
                    {
                        mensajeWarning('Hay cambios pendientes sin guardar');
                    }
                }
            }
        }
    });
    
    Ext.create('Ext.panel.Panel',{
        renderTo : '_p25_divpri',
        itemId   : '_cotcol_paneltop',
        border   : 0,
        items    : [
            Ext.create('Ext.panel.Panel',
            {
                itemId       : '_cotcol_panelFlujo'
                ,title       : 'ACCIONES'
                ,hidden      : Ext.isEmpty(_cotcol_flujo) || _cotcol_flujo.aux === 'RSTN'
                ,buttonAlign : 'left'
                ,buttons     : []
                ,style       : 'margin-bottom: 5px;'
                ,listeners   :
                {
                    afterrender : function(me)
                    {
                        if(!Ext.isEmpty(_cotcol_flujo))
                        {
                            _cargarBotonesEntidad(
                                _cotcol_flujo.cdtipflu
                                ,_cotcol_flujo.cdflujomc
                                ,_cotcol_flujo.tipoent
                                ,_cotcol_flujo.claveent
                                ,_cotcol_flujo.webid
                                ,me.itemId//callback
                                ,_cotcol_flujo.ntramite
                                ,_cotcol_flujo.status
                                ,_cotcol_flujo.cdunieco
                                ,_cotcol_flujo.cdramo
                                ,_cotcol_flujo.estado
                                ,_cotcol_flujo.nmpoliza
                                ,_cotcol_flujo.nmsituac
                                ,_cotcol_flujo.nmsuplem
                                ,null//callbackDespuesProceso
                            );
                        }
                    }
                }
            }),
            _p_25_panelPrincipal
        ]
    });
    
    _callbackContPrincipal = function(json){
    	
    	debug( 'Datos a cargar', json.smap1);
    	
    	_fieldByName('cdrfc')    .setValue(json.smap1.CDRFC);
        _fieldByName('cdperson') .setValue(json.smap1.CDPERSON);

        codpostalDefinitivo = json.smap1.CDPOSTAL;
		cdedoDefinitivo = json.smap1.CDEDO;
		cdmuniciDefinitivo = json.smap1.CDMUNICI;
												
		var params = {
		    smap1 :
		    {
		        cdunieco : _p25_smap1.cdunieco
		        ,cdramo  : _p25_smap1.cdramo
		        ,estado  : _p25_smap1.estado
		        ,nmpoliza: _p25_smap1.nmpoliza
		        ,nmsuplem: Ext.isEmpty(_p25_smap1.nmsuplem)?'0':_p25_smap1.nmsuplem
		        ,cdperson: json.smap1.CDPERSON
		        ,codpostal: json.smap1.CDPOSTAL
				,cdedo: json.smap1.CDEDO
				,cdmunici: json.smap1.CDMUNICI
				,nmorddom: json.smap1.NMORDDOM
		        ,confirmaEmision: 'S'
		    }
		};
	
		Ext.Ajax.request(
	        {
            url       : _p25_urlGuardarContratanteColectivo
            ,jsonData : params
            ,success  : function(response)
            {
                var jsonCont = Ext.decode(response.responseText);
                if(jsonCont.exito){
                	mensajeCorrecto('Aviso','Contratante Guardado Correctamente.');
                	_contratanteSaved = true;
                }
                else
                {
                    mensajeWarning(jsonCont.respuesta);
                }
            }
            ,failure  : function()
            {
                errorComunicacion();
            }
        });
        
	};
    
    if(_p25_smap1.VENTANA_DOCUMENTOS=='S'&&(_p25_ntramite||_p25_ntramiteVacio) && (Ext.isEmpty(_cotcol_flujo) || _cotcol_flujo.aux !== 'RSTN'))
    {
        Ext.create('Ext.window.Window',
        {
            title           : 'Documentos del tr&aacute;mite ' + (_p25_ntramite||_p25_ntramiteVacio)
            ,ventanaDocu    : true
            ,closable       : false
            ,width          : 500
            ,height         : 300
            ,autoScroll     : true
            ,collapsible    : true
            ,titleCollapse  : true
            ,startCollapsed : true
            ,resizable      : false
            ,cls            : 'VENTANA_DOCUMENTOS_CLASS'
            ,loader         :
            {
                scripts   : true
                ,autoLoad : true
                ,url      : _p25_urlVentanaDocumentosClon
                ,params   :
                {
                    'smap1.cdunieco'  : _p25_smap1.cdunieco
                    ,'smap1.cdramo'   : _p25_smap1.cdramo
                    ,'smap1.estado'   : 'W'
                    ,'smap1.nmpoliza' : '0'
                    ,'smap1.nmsuplem' : '0'
                    ,'smap1.nmsolici' : '0'
                    ,'smap1.ntramite' : (_p25_ntramite||_p25_ntramiteVacio)
                    ,'smap1.tipomov'  : '0'
                }
            }
        }).showAt(500, 0);
    }
    ////// contenido //////
    
    ////// custom //////
    _fieldByName('cdrfc',_p_25_panelPrincipal).on(
    {
        'blur'    : _p25_rfcBlur
        ,'change' : function()
        {
            _fieldByName('cdperson',_p_25_panelPrincipal).reset();
            nmorddomProspecto = _defaultNmordomProspecto;
        }
    });
    
    _fieldByName('nombre',_p_25_panelPrincipal).on(
    {
        'select' : function()
        {
        	_fieldByName('cdperson',_p_25_panelPrincipal).setValue('');
            nmorddomProspecto = _defaultNmordomProspecto;
        }
    });
    
    _fieldByName('codpostal',_p_25_panelPrincipal).on(
    {
        'select' : function()
        {
        	_fieldByName('cdperson',_p_25_panelPrincipal).setValue('');
            nmorddomProspecto = _defaultNmordomProspecto;
        }
    });
    
    _fieldByName('cdedo',_p_25_panelPrincipal).on(
    {
        'select' : function()
        {
        	_fieldByName('cdperson',_p_25_panelPrincipal).setValue('');
            nmorddomProspecto = _defaultNmordomProspecto;
        }
    });
    
    _fieldByName('cdmunici',_p_25_panelPrincipal).on(
    {
        'select' : function()
        {
        	_fieldByName('cdperson',_p_25_panelPrincipal).setValue('');
            nmorddomProspecto = _defaultNmordomProspecto;
        }
    });
    
    
    if(_p25_smap1.BLOQUEO_CONCEPTO=='S')
    {
        var items=Ext.ComponentQuery.query('[name]',_p25_tabConcepto());
        if(_p25_smap1.cdsisrol=='SUSCRIPTOR'&&_p25_smap1.status-0==18)
        {
            var items2 = [];
            for(var i in items)
            {
                if(items[i].name!='cdrfc'
                    &&items[i].name!='nombre'
//                  &&items[i].name!='codpostal' //Tampoco se permite editar el codigo postal para ser homologo con estado y municipio
                    &&items[i].name!='dsdomici'
                    &&items[i].name!='nmnumero'
                    &&items[i].name!='nmnumint'
                    )
                {
                    items2.push(items[i]);
                }
            }
            items = items2;
        }
        $.each(items,function(i,item)
        {
            item.setReadOnly(true);
        });
    }
    
    _fieldByName('cdreppag').on(
    {
        select : function()
        {
            var val=_fieldByName('cdreppag').getValue();
            debug('reparto pago select valor:',val);
            if(val==1)
            {
                _hide(_fieldByName('pcpgocte'));
                _fieldByName('pcpgocte').setValue(100);
                _hide(_fieldByName('pcpgotit'));
                _fieldByName('pcpgotit').setValue(0);
            }
            else if(val==2)
            {
                _show(_fieldByName('pcpgocte'));
                _fieldByName('pcpgocte').setValue(50);
                _show(_fieldByName('pcpgotit'));
                _fieldByName('pcpgotit').setValue(50);
            }
            else
            {
                _hide(_fieldByName('pcpgocte'));
                _fieldByName('pcpgocte').setValue(0);
                _hide(_fieldByName('pcpgotit'));
                _fieldByName('pcpgotit').setValue(100);
            }
        }
    });
    
    var formAsegCmp=_fieldByLabel('FORMA DE ASEGURAMIENTO',false,true);
    if(!Ext.isEmpty(formAsegCmp))
    {
        formAsegCmp.on(
        {
            select : function()
            {
                var me  =_fieldByLabel('FORMA DE ASEGURAMIENTO');
                debug('formAsegCmp select me:',me);
                var val = me.getValue();
                var record = me.findRecordByValue(val);
                debug('formAsegCmp select record:',record);
                var label  = record.get('value');
                
                var repartoCmp = _fieldByName('cdreppag');
                if(repartoCmp.store.getCount()==0)
                {
                    repartoCmp.store.padre = repartoCmp;
                    repartoCmp.igualar     = record.get('value')+'';
                    repartoCmp.store.on(
                    {
                        load : function(me)
                        {
                            var repartoCmp=me.padre;
                            repartoCmp.store.each(function(record)
                            {
                                var display = record.get('value');
                                if(display.lastIndexOf(repartoCmp.igualar)!=-1)
                                {
                                    debug('repartoCmp vacio select:',repartoCmp);
                                    repartoCmp.select(record);
                                    repartoCmp.fireEvent('select',repartoCmp,[record]);
                                }
                            });
                        }
                    });
                }
                else
                {
                    repartoCmp.store.each(function(record)
                    {
                        var display = record.get('value');
                        if(display.lastIndexOf(label)!=-1)
                        {
                            debug('repartoCmp select:',repartoCmp);
                            repartoCmp.select(record);
                            repartoCmp.fireEvent('select',repartoCmp,[record]);
                        }
                    });
                }
            }
        });
    }
    
    try
    {
        var recCombo    = _fieldLikeLabel('PERSONALIZA', null, true);
        var recNumber   = _fieldLikeLabel('PAGO FRACCIONADO', null, true);
        var cdperpagCmp = _fieldByName('cdperpag', null, true);
        
        if (!Ext.isEmpty(recCombo) && !Ext.isEmpty(recNumber) && !Ext.isEmpty(cdperpagCmp)) {
	        recNumber.bloqueo = false;
	        recNumber.heredar = function()
	        {
	            var recCombo    = _fieldLikeLabel('PERSONALIZA');
	            var recNumber   = _fieldLikeLabel('PAGO FRACCIONADO');
	            var cdperpagCmp = _fieldByName('cdperpag');
	            if(recCombo.getValue()+'x'=='Nx')
	            {
	                recNumber.hide();
	                recNumber.setValue(0);
	            }
	            else
	            {
	                recNumber.show();
	                if(!Ext.isEmpty(cdperpagCmp.getValue())&&recNumber.bloqueo==false)
		            {
		                recNumber.setLoading(true);
		                Ext.Ajax.request(
		                {
		                    url     : _p25_urlRecuperacionSimple
		                    ,params :
		                    {
		                        'smap1.procedimiento' : 'RECUPERAR_PORCENTAJE_RECARGO_POR_PRODUCTO'
		                        ,'smap1.cdramo'       : _p25_smap1.cdramo
		                        ,'smap1.cdperpag'     : cdperpagCmp.getValue()
		                    }
		                    ,success : function(response)
		                    {
		                        var ck = 'Recuperando porcentaje de recargo por producto';
		                        try
		                        {
		                            recNumber.setLoading(false);
		                            ck = 'Decodificando porcentaje de recargo por producto'; 
		                            var json=Ext.decode(response.responseText);
		                            debug('### pago frac:',json);
		                            if(json.exito)
		                            {
	    	                            recNumber.setValue(json.smap1.recargo);
		                            }
		                            else
		                            {
		                                mensajeError(json.respuesta);
		                            }
		                        }
		                        catch(e)
		                        {
		                            manejaException(e,ck);
		                        }
		                    }
		                    ,failure : function()
		                    {
		                        recNumber.setLoading(false);
		                        errorComunicacion(null,'Recuperando porcentaje de recargo por producto');
		                    }
		                });
		            }
	            }
	        };
	        
	        //triggers
	        recCombo.on(
	        {
	           change : function()
	           {
	               _fieldLikeLabel('PAGO FRACCIONADO').heredar();
	           }
	        });
	        if(recCombo.store.getCount()>0)
	        {
	            recCombo.setValue('N');
	        }
	        else
	        {
	            recCombo.store.padre=recCombo;
	            recCombo.store.on(
	            {
	                load : function(me)
	                {
	                    me.padre.setValue('N');
	                }
	            });
	        }
	        
	        cdperpagCmp.on(
	        {
	           change : function()
	           {
	               _fieldLikeLabel('PAGO FRACCIONADO').heredar();
	           }
	        });
	    }
    }
    catch(e)
    {
        debugError('Error al customizar recargo personalizado',e);
    }
    
    try
    {
        var botonAgentes = Ext.ComponentQuery.query('button[text=Agentes]')[0];
        botonAgentes.up().remove(botonAgentes,false);
        _fieldById('_p25_fieldsetDatosAgente').add(Ext.create('Ext.button.Button',
        {
            text     : botonAgentes.text
            ,icon    : botonAgentes.icon
            ,handler : botonAgentes.handler
            ,style   : 'margin:5px;'
        }));
        _fieldById('_p25_fieldsetDatosAgente').doLayout();
    }
    catch(e)
    {
        debugError('error inofensivo al querer mover boton de agentes',e);
    }
    
    try
    {
        if(_p25_smap1.cdtipsup  == _TIPO_SITUACION_RENOVACION){
            Ext.ComponentQuery.query('button[text=Exportar Censo]')[0].show();
            Ext.ComponentQuery.query('button[text=Actualizar Censo]')[0].show();
            Ext.ComponentQuery.query('button[text=Refrescar Censo]')[0].show();
            Ext.ComponentQuery.query('button[text=Complementar]')[0].hide();
        }else{
            Ext.ComponentQuery.query('button[text=Exportar Censo]')[0].hide();
            Ext.ComponentQuery.query('button[text=Actualizar Censo]')[0].hide();
            Ext.ComponentQuery.query('button[text=Refrescar Censo]')[0].hide();
            Ext.ComponentQuery.query('button[text=Complementar]')[0].show();
        }
    }
    catch(e)
    {
        debugError('error para la renovacion de la Poliza',e);
    }
    //para ramo 1 quieren heredar derechos de poliza según paquete
    if(Number(_p25_smap1.cdramo)==1)
    {
        var paqueteCmp;
        var paqueteCmp2;
        var derechosCmp;
        var derechosCmp2;
        
        for(var i in _p25_colsExtColumns)
        {
            var col = _p25_colsExtColumns[i];
            if(col.text=='PAQUETE')
            {
                paqueteCmp = col.editor;
            }
            else if(col.text=='DERECHOS DE POLIZA')
            {
                derechosCmp = col.editor;
            }
        }
        derechosCmp.setReadOnly(_p25_smap1.cdsisrol!='COTIZADOR');
        derechosCmp.heredar = function(paqueteVal)
        {
            derechosCmp.setLoading(true);
            Ext.Ajax.request(
            {
                url      : _p25_urlRecuperacionSimple
                ,params  :
                {
                    'smap1.procedimiento' : 'RECUPERAR_DERECHOS_POLIZA_POR_PAQUETE_RAMO_1'
                    ,'smap1.paquete'      : paqueteVal
                }
                ,success : function(response)
                {
                    derechosCmp.setLoading(false);
                    var ck = 'Decodificando derechos de poliza';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### derechos de poliza:',json);
                        if(json.exito)
                        {
                            derechosCmp.setValue(json.smap1.DERECHOS);
                        }
                        else
                        {
                            mensajeError(json.respuesta);
                        }
                    }
                    catch(e)
                    {
                        manejaException(e,ck);
                    }
                }
                ,failure : function()
                {
                    derechosCmp.setLoading(false);
                    errorComunicacion(null,'Recuperando derechos de poliza');
                }
            });
        };
        paqueteCmp.on(
        {
            select : function(me,rec)
            {
                derechosCmp.heredar(rec[0].get('key'));
            }
        });
        
        ////
        
        for(var i in _p25_colsBaseColumns)
        {
            var col = _p25_colsBaseColumns[i];
            if(col.text=='PAQUETE')
            {
                paqueteCmp2 = col.editor;
            }
            else if(col.text=='DERECHOS DE POLIZA')
            {
                derechosCmp2 = col.editor;
            }
        }
        derechosCmp2.setReadOnly(_p25_smap1.cdsisrol!='COTIZADOR');
        derechosCmp2.heredar = function(paqueteVal)
        {
            derechosCmp2.setLoading(true);
            Ext.Ajax.request(
            {
                url      : _p25_urlRecuperacionSimple
                ,params  :
                {
                    'smap1.procedimiento' : 'RECUPERAR_DERECHOS_POLIZA_POR_PAQUETE_RAMO_1'
                    ,'smap1.paquete'      : paqueteVal
                }
                ,success : function(response)
                {
                    derechosCmp2.setLoading(false);
                    var ck = 'Decodificando derechos de poliza';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### derechos de poliza:',json);
                        if(json.exito)
                        {
                            derechosCmp2.setValue(json.smap1.DERECHOS);
                        }
                        else
                        {
                            mensajeError(json.respuesta);
                        }
                    }
                    catch(e)
                    {
                        manejaException(e,ck);
                    }
                }
                ,failure : function()
                {
                    derechosCmp2.setLoading(false);
                    errorComunicacion(null,'Recuperando derechos de poliza');
                }
            });
        };
        paqueteCmp2.on(
        {
            select : function(me,rec)
            {
                derechosCmp2.heredar(rec[0].get('key'));
            }
        });
    }
    //para ramo 1 quieren heredar derechos de poliza según paquete
    
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />
    
    /////// SE VAN A CARGAR LAS LISTAS DE TODOS LOS COMBOS QUE SEAN TATRISIT    
     for(var i in _p25_colsExtColumns)
     {
	    if(!Ext.isEmpty(_p25_colsExtColumns[i].editor) && !Ext.isEmpty(_p25_colsExtColumns[i].editor.store) && !Ext.isEmpty(_p25_colsExtColumns[i].editor.cdatribu))
	     {
	        recupeListaTatrisitSinPadre(_p25_colsExtColumns[i].editor);
	     }
     }   
     
     for(var i in _p25_colsBaseColumns)
     {
        if(!Ext.isEmpty(_p25_colsBaseColumns[i].editor) && !Ext.isEmpty(_p25_colsBaseColumns[i].editor.store) && !Ext.isEmpty(_p25_colsBaseColumns[i].editor.cdatribu))
         {
            recupeListaTatrisitSinPadre(_p25_colsExtColumns[i].editor);
         }
     }
    
    if (_p25_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba) {
        // Participacion - contratacion - poblacion
        var ck = 'Configurando participacion - contratacion - poblacion';
        try {
            var r11partComp = _fieldLikeLabel('PARTICIPACI', '_p25_fieldsetRiesgo', true),
                r11contComp = _fieldLikeLabel('CONTRATACI', '_p25_fieldsetRiesgo', true),
                r11poblComp = _fieldLikeLabel('POBLACI', '_p25_fieldsetRiesgo', true);
            if (!Ext.isEmpty(r11partComp) && !Ext.isEmpty(r11contComp) && !Ext.isEmpty(r11poblComp)) {
                r11partComp.heredar = function () {
                    debug('r11partComp heredar!');
                    var ck = 'Heredando participacion de contratacion';
                    try {
                        var desc = r11contComp.findRecordByValue(r11contComp.getValue()).get('value');
                        debug('desc:', desc);
                        if (desc.indexOf('CONTRIBUTORIO') != -1) {
                            _show(r11partComp);
                            r11partComp.setValue(0);
                        } else {
                            _hide(r11partComp);
                            r11partComp.setValue(0);
                        }
                    } catch (e) {
                        manejaException(e, ck);
                    }
                };
                r11contComp.on({
                    select : r11partComp.heredar
                });
                
                r11poblComp.heredar = function () {
                    debug('r11poblComp heredar!');
                    var ck = 'Heredando poblacion de contratacion';
                    try {
                        var desc = r11contComp.findRecordByValue(r11contComp.getValue()).get('value');
                        debug('desc:', desc);
                        if (desc.indexOf('VOLUNTAR') != -1) {
                            _show(r11poblComp);
                            r11poblComp.setValue(0);
                        } else {
                            _hide(r11poblComp);
                            r11poblComp.setValue(0);
                        }
                    } catch (e) {
                        manejaException(e, ck);
                    }
                };
                r11contComp.on({
                    select : r11poblComp.heredar
                });
            }
        } catch (e) {
            manejaException(e, ck);
        }
        
        // ocultar cdreppag y cdpool
        ck = 'Ocultando cdreppag y cdpool';
        try {
            var cdreppagCmp = _fieldByName('cdreppag', null, true),
                cdpoolCmp   = _fieldByName('cdpool', null, true);
            _hide(cdreppagCmp);
            _hide(cdpoolCmp);
        } catch (e) {
            manejaException(e, ck);
        }
        
        // tarifa fija - monto
        ck = 'Configurando monto de tarifa fija';
        try {
            var tarifaCmp = _fieldByLabel('TARIFA FIJA', '_p25_fieldsetRiesgo', true),
                montoCmp  = _fieldByLabel('MONTO DE TARIFA FIJA', '_p25_fieldsetRiesgo', true);
            if (!Ext.isEmpty(tarifaCmp) && !Ext.isEmpty(montoCmp)) {
                montoCmp.heredar = function () {
                    montoCmp.setValue(0);
                    var tarifaVal = tarifaCmp.getValue();
                    if (tarifaVal === 'S') {
                        _show(montoCmp);
                    } else {
                        _hide(montoCmp);
                    }
                };
                tarifaCmp.on({
                    select : montoCmp.heredar
                });
                montoCmp.heredar();
            }
        } catch (e) {
            manejaException(e, ck);
        }
    }
    
    //_iceMostrar();
    ////// custom //////
    
    ////// loaders //////
    Ext.Ajax.request(
    {
        url       : _p25_urlconsultaExtraprimOcup
        ,params   : 
        {
        	'smap1.cdtipsit' : _p25_smap1.cdtipsit
        }
        ,success  : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('respuesta del guardado de extraprimas:',json);
            if(json.exito)
            {
                posicionExtraprimaOcup = json.params.otvalor;
                debug('posicionExtraprimaOcup',posicionExtraprimaOcup);

            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure  : function()
        {
            errorComunicacion();
        }
    });
     
    if(_p25_ntramiteVacio)
    {
        _fieldByName('ntramite').setValue(_p25_ntramiteVacio);
        _p25_tabpanel().setLoading(true);
        Ext.Ajax.request(
        {
            url     : _p25_urlRecuperarProspecto
            ,params :
            {
                'smap1.ntramite' : _p25_ntramiteVacio
            }
            ,success : function(response)
            {
                _p25_tabpanel().setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('prospecto:',json);
                _fieldByName('nombre').setValue(json.smap1.NOMBRE);
            }
            ,failure : function()
            {
                _p25_tabpanel().setLoading(false);
                errorComunicacion();
            }
        });
    }
    else if(_p25_ntramite)
    {
        _p25_tabpanel().setLoading(true);
        try
        {
            _fieldLikeLabel('PAGO FRACCIONADO', null, true).bloqueo=true;
        }
        catch(e)
        {
            debugError('Error al bloquear campo de recargo personalizado',e);
        }
        Ext.Ajax.request(
        {
            url     : _p25_urlCargarDatosCotizacion
            ,params :
            {
                'smap1.cdunieco'  : _p25_smap1.cdunieco
                ,'smap1.cdramo'   : _p25_smap1.cdramo
                ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
                ,'smap1.estado'   : _p25_smap1.estado
                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                ,'smap1.ntramite' : _p25_smap1.ntramite
            }
            ,success : function(response)
            {
                _p25_tabpanel().setLoading(false);
                var json=Ext.decode(response.responseText);
                debug('### cargar cotizacion response:',json);
                if(json.exito)
                {
                    for(var prop in json.params)
                    {
                        if(prop!='cdedo'&&prop!='cdmunici'&&prop!='clasif'&&prop!='swexiper'&&prop!='nmorddom')
                        {
                            if(prop=='pcpgocte')
                            {
                                var porc = json.params[prop];
                                debug('porcentaje pago cliente:',porc);
                                if(porc-0==100)
                                {
                                    _fieldByName('cdreppag').setValue('1');
                                    _fieldByName('pcpgocte').setValue('100');
                                    _fieldByName('pcpgocte').hide();
                                    _fieldByName('pcpgotit').hide();
                                    _fieldByName('cdreppag').getStore().on(
                                    {
                                        'load' : function()
                                        {
                                            _fieldByName('cdreppag').setValue('1');
                                            _fieldByName('pcpgocte').setValue('100');
                                            _fieldByName('pcpgocte').hide();
                                            _fieldByName('pcpgotit').hide();
                                            _fieldByName('pcpgocte').allowBlank=true;
                                            _fieldByName('pcpgocte').isValid();
                                        }
                                    });
                                }
                                else if(porc-0==0)
                                {
                                    _fieldByName('cdreppag').setValue('3');
                                    _fieldByName('pcpgocte').setValue('0');
                                    _fieldByName('pcpgocte').hide();
                                    _fieldByName('pcpgotit').hide();
                                    _fieldByName('cdreppag').getStore().on(
                                    {
                                        'load' : function()
                                        {
                                            _fieldByName('cdreppag').setValue('3');
                                            _fieldByName('pcpgocte').setValue('0');
                                            _fieldByName('pcpgocte').hide();
                                            _fieldByName('pcpgotit').hide();
                                        }
                                    });
                                }
                                else
                                {
                                    _fieldByName('cdreppag').setValue('2');
                                    _fieldByName('pcpgocte').setValue(porc);
                                    _fieldByName('cdreppag').getStore().on(
                                    {
                                        'load' : function()
                                        {
                                            _fieldByName('cdreppag').setValue('2');
                                            _fieldByName('pcpgocte').setValue(porc);
                                        }
                                    });
                                }
                            }
                            else
                            {
                                var item=_fieldByName(prop,null,true);
                                if(!Ext.isEmpty(item))
                                {
                                    if(Ext.isEmpty(item.store))
                                    {
                                        item.setValue(json.params[prop]);
                                    }
                                    else
                                    {
                                        if(item.store.getCount()==0)
                                        {
                                            item.store.comboName = prop+'';
                                            item.forceSelection  = false;
                                            item.store.on(
                                            {
                                                load : function(store)
                                                {
                                                    _fieldByName(store.comboName).forceSelection=true;
                                                }
                                            });
                                        }
                                        item.setValue(json.params[prop]);
                                    }
                                }
                            }
                        }
                    }
                    _fieldByName('cdedo').heredar(true,function()
                    {
                        _fieldByName('cdedo').setValue(json.params['cdedo']);
                        _fieldByName('cdmunici').heredar(true,function()
                        {
                            _fieldByName('cdmunici').setValue(json.params['cdmunici']);
                        });
                    });
                    
                    
                    if(_p25_smap1.cdsisrol=='SUSCRIPTOR'&& (_p25_smap1.status-0==19 || _p25_smap1.status-0==21 || _p25_smap1.status-0==23)){
                    	var cargacdperson = _fieldByName('cdperson').getValue();
                    	
                    	_fieldById('_p29_clientePanel').loader.load(
			            {
			                params:
			                {
				                'smap1.cdperson' : (!Ext.isEmpty(json.params['swexiper']) && (json.params['swexiper'] == 'S' || json.params['swexiper'] == 's') && !Ext.isEmpty(cargacdperson))? cargacdperson : '',
				                'smap1.cdideper' : '',
				                'smap1.cdideext' : '',
				                'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
				                'smap1.polizaEnEmision': 'S',
				                'smap1.esCargaClienteNvo' : 'N' ,
				                'smap1.ocultaBusqueda' : 'S' ,
				                'smap1.cargaCP' : '',
				                'smap1.cargaTipoPersona' : '',
				                'smap1.cargaSucursalEmi' : _p25_smap1.cdunieco,
				                'smap1.activaCveFamiliar': 'N',
				                'smap1.modoRecuperaDanios': 'N',
				                'smap1.modoSoloEdicion': 'N',
				                'smap1.contrantantePrincipal': 'S',
				                'smap1.tomarUnDomicilio' : 'S',
	                    		'smap1.cargaOrdDomicilio' : (!Ext.isEmpty(json.params['swexiper']) && (json.params['swexiper'] == 'S' || json.params['swexiper'] == 's') && !Ext.isEmpty(cargacdperson))? json.params['nmorddom'] : ''
				            }
			            });
                    }
                    
                    
                    _p25_clasif = json.params['clasif'];
                    debug('_p25_clasif:',_p25_clasif);
                    var auxCargarGrupos=function(callback)
                    {
                        _p25_tabpanel().setLoading(true);
                        Ext.Ajax.request(
                        {
                            url      : _p25_urlCargarGrupos
                            ,params  :
                            {
                                'smap1.cdunieco'  : _p25_smap1.cdunieco
                                ,'smap1.cdramo'   : _p25_smap1.cdramo
                                ,'smap1.estado'   : _p25_smap1.estado
                                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                            }
                            ,callback : function()
                            {
                                try
						        {
						            _fieldLikeLabel('PAGO FRACCIONADO', null, true).bloqueo=false;
						        }
						        catch(e)
						        {
						            debugError('Error al desbloquear campo de recargo personalizado',e);
						        }
                            }
                            ,success : function(response)
                            {
                                _p25_tabpanel().setLoading(false);
                                var json2=Ext.decode(response.responseText);
                                debug('### cargar grupos response:',json2);
                                if(json2.exito)
                                {
                                    callback(json2);
                                }
                                else
                                {
                                    mensajeError(json2.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                _p25_tabpanel().setLoading(false);
                                errorComunicacion();
                            }
                        });
                    };
                    if(_p25_clasif==_p25_TARIFA_LINEA&&_p25_smap1.LINEA_EXTENDIDA=='S')
                    {
                        debug('>cargar linea');
                        auxCargarGrupos(function(resp)
                        {
                            debug('>callback linea');
                            _p25_construirLinea(0,0,true);
                            _p25_setActiveConcepto();
                            var aux=resp.slist1.length;
                            var aux2=0;
                            for(var i=0;i<aux;i++)
                            {
                                _p25_storeGrupos.add(new _p25_modeloGrupo(resp.slist1[i]));
                                _p25_storeGrupos.sort('letra','ASC');
                                _p25_storeGrupos.commitChanges();
                            }
                        });
                        debug('<cargar linea');
                    }
                    else
                    {
                        debug('>cargar modificada');
                        auxCargarGrupos(function(resp)
                        {
                            debug('>callback modificada');
                            if(_p25_clasif==_p25_TARIFA_LINEA)
                            {
                                _p25_construirLinea(0,0,true);
                                _p25_setActiveConcepto();
                            }
                            else
                            {
                                _p25_construirModificada(0,0,true);
                                _p25_setActiveConcepto();
                            }
                            var aux=resp.slist1.length;
                            var aux2=0;
                            debug('cargar:',aux);
                            _p25_tabpanel().setLoading(true);
                            if(aux==0)
                            {
                                mensajeError('No hay grupos para cargar');
                            }
                            for(var i=0;i<aux;i++)
                            {
                                Ext.Ajax.request(
                                {
                                    url      : _p25_urlObtenerTvalogarsGrupo
                                    ,params  :
                                    {
                                        'smap1.cdunieco'  : _p25_smap1.cdunieco
                                        ,'smap1.cdramo'   : _p25_smap1.cdramo
                                        ,'smap1.estado'   : _p25_smap1.estado
                                        ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                                        ,'smap1.letra'    : resp.slist1[i].letra
                                        ,'smap1.i'        : i
                                    }
                                    ,success : function(response)
                                    {
                                        var tvalogars=Ext.decode(response.responseText);
                                        debug('### tvalogars:',tvalogars);
                                        if(tvalogars.exito)
                                        {
                                            aux2=aux2+1;
                                            debug('cargadas:',aux2);
                                            var grupo=new _p25_modeloGrupo(resp.slist1[tvalogars.smap1.i]);
                                            grupo.tvalogars=tvalogars.slist1;
                                            grupo.valido=true;
                                            _p25_storeGrupos.add(grupo);
                                            _p25_storeGrupos.sort('letra','ASC');
                                            _p25_storeGrupos.commitChanges();
                                            if(aux2==aux)//tenemos todas las respuestas
                                            {
                                                _p25_tabpanel().setLoading(false);
                                            }
                                        }
                                        else
                                        {
                                            _p25_tabpanel().setLoading(false);
                                            mensajeError(tvalogars.respuesta);
                                        }
                                    }
                                    ,failure : function()
                                    {
                                        _p25_tabpanel().setLoading(false);
                                        errorComunicacion();
                                    }
                                });
                            }
                            debug('<callback modificada');
                        });
                        debug('<cargar modificada');
                    }
                }
                else
                {
                     mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                _p25_tabpanel().setLoading(false);
                errorComunicacion();
            }
        });
        
        _p25_tbloqueo(false);
    }
    
    _fieldByName('nmnumero').regex = undefined;
    _fieldByName('nmnumint').regex = undefined;
    
    if(!_p25_ntramite)
    {
        Ext.Ajax.request(
        {
            url     : _p25_urlRecuperacionSimpleLista
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_VALORES_PANTALLA'
                ,'smap1.pantalla'     : 'COTIZACION_COLECTIVO'
                ,'smap1.cdramo'       : _p25_smap1.cdramo
                ,'smap1.cdtipsit'     : _p25_smap1.cdtipsit
            }
            ,success : function(response)
            {
                var json=Ext.decode(response.responseText);
                debug('### valores pantalla:',json);
                if(json.exito)
                {
                    for(var i in json.slist1)
                    {
                        var comp=_fieldByName(json.slist1[i].NAME,_fieldById('_p25_tabConcepto'));
                        if(comp)
                        {
                            if(Ext.isEmpty(comp.store))
                            {
                                comp.setValue(json.slist1[i].VALOR);
                            }
                            else
                            {
                                if(comp.store.getCount()==0)
                                {
                                    comp.valorInicial = json.slist1[i].VALOR;
                                    comp.store.padre  = comp;
                                    comp.store.on(
                                    {
                                        load : function(me)
                                        {
                                            me.padre.select(me.padre.findRecordByValue(me.padre.valorInicial));
                                            me.padre.fireEvent('select');
                                        }
                                    });
                                }
                                else
                                {
                                    comp.select(comp.findRecordByValue(json.slist1[i].VALOR));
                                    comp.fireEvent('select');
                                }
                            }
                        }
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                errorComunicacion();
            }
        });
    }
    
    try
    {
        if(_p25_ntramite&&_p25_smap1.sincenso!='S')
        {
            Ext.ComponentQuery.query('[text=Guardar sin censo]')[0].hide();
        }
    }
    catch(e)
    {}
    
    Ext.Ajax.request(
    {
        url      : _p25_urlCargarParametros
        ,params  :
        {
            'smap1.parametro' : 'TITULO_COTIZACION'
            ,'smap1.cdramo'   : _p25_smap1.cdramo
            ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
        }
        ,success : function(response)
        {
            try
            {
                var json=Ext.decode(response.responseText);
                debug('### obtener rango años response:',json);
                if(json.exito)
                {
                    _fieldByLabel('PRODUCTO').setValue(json.smap1.P1VALOR);
                }
                else
                {
                    _fieldByLabel('PRODUCTO').hide();
                    debugError('No se encontro titulo de la cotizacion:',json.respuesta);
                }
            }
            catch(e)
            {
                debugError('error al obtener titulo de cotizacion:',e);
            }
        }
        ,failure : function()
        {
            _fieldByLabel('PRODUCTO').hide();
            errorComunicacion();
        }
    });
    
    Ext.Ajax.request(
    {
        url     : _p25_urlCargarParametros
        ,params :
        {
            'smap1.parametro' : 'COMP_LECT_RIESGO_COT_GRUP'
            ,'smap1.cdramo'   : _p25_smap1.cdramo
            ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
            ,'smap1.clave4'   : _p25_smap1.cdsisrol
        }
        ,success : function(response)
        {
            var ck = 'Decodificando permisos de componentes de riesgo';
            try
            {
                var json = Ext.decode(response.responseText);
                debug('### permisos:',json);
                if(json.exito)
                {
                    for(var i=1;i<=13;i++)
                    {
                        var indice = 'P'+i+'VALOR';
                        var label  = json.smap1[indice];
                        if(!Ext.isEmpty(label))
                        {
                            //_fieldLikeLabel(json.smap1[indice],_fieldById('_p25_fieldsetRiesgo')).readOnly = true;
                            _fieldLikeLabel(json.smap1[indice],_fieldById('_p25_fieldsetRiesgo')).hide();
                        }
                    }
                }
                else
                {
                    debugError('Error inofensivo al obtener permisos de componentes de riesgo',json.respuesta);
                }
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,failure : function()
        {
            errorComunicacion(null,'Error al obtener permisos de componentes de riesgo');
        }
    });
    
    _fieldByName('cdpool').on(
    {
        focus : function(me)
        {
            me.forceSelection = false;
        }
    });
    ////// loaders //////
});

////// funciones //////
function _p25_construirLinea(button,event,confirmado)
{
    debug('>_p25_construirLinea confirmado:',confirmado,'-dummy');
    var valido = true;
    
    //validar confirmacion
    if(valido)
    {
        valido=confirmado==true;
        if(!valido)
        {
            centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se borrarán los subgrupos existentes<br>¿Desea continuar?', function(btn)
            {
                if(btn === 'yes')
                {
                    _p25_construirLinea(button,event,true);
                }
            }));
        }
    }
    
    //agregar tab grupos
    if(valido)
    {
        _p25_quitarTabsDetalleGrupo();
        _p25_clasif = _p25_TARIFA_LINEA;
        _p25_agregarTabGrupos();
    }
    debug('<_p25_construirLinea');
}

function _p25_quitarTabsDetalleGrupo(letraGrupo)
{
    debug('>_p25_quitarTabsDetalleGrupo:',letraGrupo,'dummy');
    if(!letraGrupo)
    {
        var tabsDetalleGrupo=Ext.ComponentQuery.query('[tipo=tabDetalleGrupo]');
    }
    else
    {
        var tabsDetalleGrupo=Ext.ComponentQuery.query('[letraGrupo='+letraGrupo+']');
    }
    if(tabsDetalleGrupo.length>0)
    {
        for(var i=0;i<tabsDetalleGrupo.length;i++)
        {
            tabsDetalleGrupo[i].destroy();
        }
    }
    debug('<_p25_quitarTabsDetalleGrupo');
}

function _p25_agregarTabGrupos()
{
    debug('>_p25_agregarTabGrupos');
    _p25_storeGrupos.removeAll();
    
    if(_p25_tabGrupos)
    {
        debug('remove:',Ext.ComponentQuery.query('#'+_p25_tabGrupos.itemId)[0]);
        _p25_tabpanel().remove(Ext.ComponentQuery.query('#'+_p25_tabGrupos.itemId)[0],false);
    }
    
    if(_p25_clasif==_p25_TARIFA_LINEA)
    {
        _p25_tabGrupos = _p25_tabGruposLineal;
    }
    else
    {
        _p25_tabGrupos = _p25_tabGruposModifi;
    }
    
    _p25_agregarTab(_p25_tabGrupos);
    window.parent.scrollTo(0, 0);
    debug('<_p25_agregarTabGrupos');
}

function _p25_agregarTab(tab)
{
    debug('>_p25_agregarTab:',tab);
    _p25_tabpanel().add(tab);
    debug('active:',Ext.ComponentQuery.query('#'+tab.itemId)[0]);
    _p25_setActiveTab(tab.itemId);
    window.parent.scrollTo(0, 0);
    debug('<_p25_agregarTab');
}

function _p25_tabpanel()
{
    return Ext.ComponentQuery.query('#_p25_tabpanel')[0];
}

function _p25_editorPlanHijos(combo)
{
	debug('>_p25_editorPlanHijos');

	if(Number(_p25_smap1.cdramo)==4)
		{
		
			Ext.Ajax.request(
	        {
	            url      : _p25_urlRecuperacion
	            ,params  :
	            {
	            	 'params.consulta' :'RECUPERAR_CLAVES_PLAN_RAMO4'
	                ,'params.cdramo'        : _p25_smap1.cdramo
	                ,'params.cdtipsit'      : _p25_smap1.cdtipsit
	                ,'params.cdplan'        : combo.getValue()
	                
	            }
	        ,success : function(response)
            {   
	        	var ck = 'Recuperando dependientes de combo';                          
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### json resulatnte: _p25_editorPlanHijos',json);
                    if(json.success==true)
                    {
                        var cols = _p25_query('#'+_p25_tabGrupos.itemId)[0].items.items[0].columns;
                        for(var i=0;i<cols.length;i++)
                        	{
                        	 if(cols[i].text=='CLAVE DE PLAN')
                        		 {
                        		   var comboClave = cols[i].field;
                        		   debug(comboClave);
                        		   comboClave.store.removeAll();
                        		   
                        		   for(var j=0; j<json.list.length ;j++)
                        			   {
                        			   
	                        			   comboClave.store.add( {
	                        				   key   : json.list[j].otclave
	                        				  ,value : json.list[j].otvalor
	                        			   
	                        			   } );
                        			   
                        			   }
	                        			   comboClave.store.fireEvent('load',comboClave.store);
                        		 }
                        	}
                    }
                    else
                    {
                        mensajeError(json.message);
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                errorComunicacion(null,'Error al cargar dependientes de plan');
            }
			
			});
		}

    if (_p25_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba && !Ext.isEmpty(combo.getValue())) {
        var ck = 'Heredando suma asegurada';
        debug(ck);
        try {
            var cols = _p25_query('#'+_p25_tabGrupos.itemId)[0].items.items[0].columns;
            debug('cols:', cols);
            for (var i = 0; i < cols.length; i++) {
                if (cols[i].text === 'SUMA ASEGURADA') {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                    editor.getStore().filter('key', combo.getValue());
                } else if (cols[i].text === 'DEDUCIBLE') {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                    editor.getStore().filter('key', combo.getValue());
                } else if (cols[i].text.indexOf('ULO HOSPITAL') !== -1) {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                    editor.getStore().filter('key', combo.getValue());
                }/* else if (cols[i].text.indexOf('VEL HOSPITAL') !== -1) {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                    editor.getStore().filter('key', combo.getValue());
                }*/
            }
        } catch (e) {
            manejaException(e, ck);
        }
    }
}

function _p25_editorPlanChange(combo,newValue,oldValue,eOpts)
{
    debug('>_p25_editorPlanChange new',newValue,'old',oldValue+'x');
    if(!Ext.isEmpty(oldValue)&&(_p25_clasif==_p25_TARIFA_MODIFICADA||_p25_smap1.LINEA_EXTENDIDA=='N')&&_p25_semaforoPlanChange)
    {
        centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Al cambiar el plan se borrar&aacute; el detalle del subgrupo<br/>¿Desea continuar?', function(btn)
        {
            if(btn == 'yes')
            {
                var record = _p25_query('#'+_p25_tabGrupos.itemId)[0].items.items[0].getSelectionModel().getSelection()[0];
                debug('record:',record);
                var letra  = record.get('letra');
                debug('letra:',letra);
                record.valido = false;
                _p25_quitarTabsDetalleGrupo(letra);
            }
            else
            {
                _p25_semaforoPlanChange = false;
                combo.setValue(oldValue);
                _p25_semaforoPlanChange = true;
            }
        }));
    }
    debug('<_p25_editorPlanChange');
}

function _p25_query(regex)
{
    debug('>_p25_query<',regex);
    return Ext.ComponentQuery.query(regex);
}

function _p25_setActiveTab(itemId)
{
    debug('>_p25_setActiveTab:',itemId);
    _p25_tabpanel().setActiveTab(Ext.ComponentQuery.query('#'+itemId)[0]);
    window.parent.scrollTo(0, 0);
    debug('<_p25_setActiveTab');
}

function _p25_cotizarNueva()
{
    debug('>_p25_cotizarNueva');
    _p25_quitarTabsDetalleGrupo();
    _p25_storeGrupos.removeAll();
    if(_p25_tabGrupos)
    {
        debug('remove:',Ext.ComponentQuery.query('#'+_p25_tabGrupos.itemId)[0]);
        _p25_tabpanel().remove(Ext.ComponentQuery.query('#'+_p25_tabGrupos.itemId)[0],false);
    }
    _p25_tabConcepto().down('[xtype=form]').getForm().reset();
    _p25_editarClic();
    debug('<_p25_cotizarNueva');
}

function _p25_tabConcepto()
{
    debug('>_p25_tabConcepto<');
    return _p25_query('#_p25_tabConcepto')[0];
}

function _p25_editarClic()
{
    debug('>_p25_editarClic');
    _p25_tabConcepto().down('[xtype=form]').setDisabled(false);
    _p25_tabConcepto().remove(_p25_gridTarifas,true);
    window.parent.scrollTo(0, 0);
    debug('<_p25_editarClic');
}

function _p25_construirModificada(button,event,confirmado)
{
    debug('>_p25_construirModificada');
    var valido = true;
    
    //validar confirmacion
    if(valido)
    {
        valido=confirmado==true;
        if(!valido)
        {
            centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se borrarán los subgrupos existentes<br>¿Desea continuar?', function(btn)
            {
                if(btn === 'yes')
                {
                    _p25_construirModificada(button,event,true);
                }
            }));
        }
    }
    debug('<_p25_construirModificada');

    if(valido)
    {
        _p25_quitarTabsDetalleGrupo();
		//se condiciona por rol el valor de la variable, para que guarde las coberturas sin necesidad de editar subgrupo, para roles restringidos (EGS)
		_p25_clasif = (!([RolSistema.Agente,RolSistema.EjecutivoVenta,RolSistema.MesaControl].indexOf(_p25_smap1.cdsisrol) == -1)&&_p25_smap1.cdtipsit == 'RC') ? _p25_TARIFA_LINEA : _p25_TARIFA_MODIFICADA;
		//_p25_clasif = _p25_TARIFA_MODIFICADA; //(EGS)
		_p25_agregarTabGrupos();
    }
}

function _p25_setActiveConcepto()
{
    debug('>_p25_setActiveConcepto');
    _p25_setActiveTab('_p25_tabConcepto');
    window.parent.scrollTo(0, 0);
    debug('<_p25_setActiveConcepto');
}

function _p25_agregarGrupoClic()
{
    debug('>_p25_agregarGrupoClic');
    _p25_storeGrupos.add(new _p25_modeloGrupo({letra:'99'}));
    _p25_storeGrupos.sort('letra','ASC');
    _p25_renombrarGrupos(true);
    _p25_storeGrupos.commitChanges();
    debug('<_p25_agregarGrupoClic');
}

function _p25_renombrarGrupos(sinBorrarPestanias)
{
    debug('>_p25_renombrarGrupos');
    if(!sinBorrarPestanias)
    {
        _p25_quitarTabsDetalleGrupo();
    }
    var letras=['1','2','3','4','5','6','7','8','9','91','92','93','94','95','96','97','98','99'];
    var i=0;
    _p25_storeGrupos.each(function(record)
    {
        record.set('letra',letras[i]);
        i=i+1;
    });
    debug('<_p25_renombrarGrupos');
}

function _p25_rfcBlur(field)
{
    debug('>_p25_rfcBlur:',field);
    var value=field.getValue();
    debug('value:',value);
    
    var valido = true;
    
    if(valido)
    {
        valido = _p25_smap1.BLOQUEO_CONCEPTO=='N';
        if(!valido)
        {
            valido = _p25_smap1.cdsisrol=='SUSCRIPTOR'&&_p25_smap1.status-0==18;
        }
    }
    
    if(valido)
    {
        valido = value&&value.length>8&&value.length<14;
    }
    
    if(valido)
    {
        _p25_tabpanel().setLoading(true);
        Ext.Ajax.request
        ({
            url     : _p25_urlBuscarPersonas
            ,timeout: 240000
            ,params :
            {
                'map1.pv_rfc_i'       : value
                ,'map1.cdtipsit'      : _p25_smap1.cdtipsit
                ,'map1.pv_cdunieco_i' : _p25_smap1.cdunieco
                ,'map1.pv_cdramo_i'   : _p25_smap1.cdramo
                ,'map1.pv_estado_i'   : 'W'
                ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
                ,'map1.esContratante' : 'S'
            }
            ,success:function(response)
            {
                _p25_tabpanel().setLoading(false);
                var json=Ext.decode(response.responseText);
                debug('json response:',json);
                if(json&&json.slist1&&json.slist1.length>0)
                {
                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                    {
                        width        : 600
                        ,height      : 400
                        ,modal       : true
                        ,autoScroll  : true
                        ,title       : 'Coincidencias'
                        ,items       : Ext.create('Ext.grid.Panel',
                        {
                            store    : Ext.create('Ext.data.Store',
                            {
                                model     : 'RFCPersona'
                                ,autoLoad : true
                                ,proxy :
                                {
                                    type    : 'memory'
                                    ,reader : 'json'
                                    ,data   : json['slist1']
                                }
                            })
                            ,columns :
                            [
                                {
                                    xtype         : 'actioncolumn'
                                    ,menuDisabled : true
                                    ,width        : 30
                                    ,items        :
                                    [
                                        {
                                            icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                            ,tooltip : 'Seleccionar usuario'
                                            ,handler : function(grid, rowIndex, colIndex)
                                            {
                                                var record = grid.getStore().getAt(rowIndex);
                                                debug('record:',record);
                                                
                                                _fieldByName('cdrfc',_p_25_panelPrincipal).setValue(record.get('RFCCLI'));
                                                
//                                                _fieldByName('cdperson',_p_25_panelPrincipal).setValue(record.get('CLAVECLI'));
                                                //se obtiene cdperson temporal para prospecto
                                                _fieldByName('cdperson',_p_25_panelPrincipal).setValue('');
                                                
                                                _fieldByName('cdideper_',_p_25_panelPrincipal).setValue(record.get('CDIDEPER'));
                                                _fieldByName('cdideext_',_p_25_panelPrincipal).setValue(record.get('CDIDEEXT'));
                                                
                                                _fieldByName('nombre',_p_25_panelPrincipal).setValue(record.get('NOMBRECLI'));
                                                _fieldByName('codpostal',_p_25_panelPrincipal).setValue(record.get('CODPOSTAL'));
                                                
                                                _fieldByName('cdedo',_p_25_panelPrincipal).heredar(true,function()
                                                {
                                                    _fieldByName('cdedo',_p_25_panelPrincipal).setValue(record.get('CDEDO'));
                                                    _fieldByName('cdmunici',_p_25_panelPrincipal).heredar(true,function()
                                                    {
                                                        _fieldByName('cdmunici',_p_25_panelPrincipal).setValue(record.get('CDMUNICI'));
                                                    });
                                                });
                                                
                                                _fieldByName('dsdomici',_p_25_panelPrincipal).setValue(record.get('DSDOMICIL'));
//                                                _fieldByName('nmnumero',_p_25_panelPrincipal).setValue(record.get('NMNUMERO'));
//                                                _fieldByName('nmnumint',_p_25_panelPrincipal).setValue(record.get('NMNUMINT'));
//                                                
                                                nmorddomProspecto = record.get('NMORDOM');
                                                
                                                /*debug('cliente obtenido de WS? ', json.clienteWS);
                                                gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdrfc",record.get("RFCCLI"));
                                                if(json.clienteWS)
                                                {
                                                    gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdideper",record.get("CDIDEPER"));
                                                }
                                                else
                                                {
                                                    gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdperson",record.get("CLAVECLI"));
                                                    gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdideper",record.get("CDIDEPER"));
                                                    gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("swexiper",'S');
                                                }
                                                */
                                                grid.up().up().destroy();
                                            }
                                        }
                                    ]
                                }
                                ,{
                                    header     : 'ID. Asegurado'
                                    ,dataIndex : 'CLAVECLI'
                                    ,flex      : 1
                                },{
                                    header     : 'RFC'
                                    ,dataIndex : 'RFCCLI'
                                    ,flex      : 1
                                }
                                ,{
                                    header     : 'Nombre'
                                    ,dataIndex : 'NOMBRECLI'
                                    ,flex      : 1
                                }
                                ,{
                                    header     : 'Direcci&oacute;n'
                                    ,dataIndex : 'DIRECCIONCLI'
                                    ,flex      : 3
                                }
                            ]
                        })
                    }).show());
                }
                else
                {
                    mensajeWarning('No hay coincidencias de RFC');
                }
            }
            ,failure:function()
            {
                _p25_tabpanel().setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p25_rfcBlur');
}

function _p25_borrarGrupoClic(grid,rowIndex)
{
    var record = grid.getStore().getAt(rowIndex);
    debug('>_p25_borrarGrupoClic:',record.data);
    centrarVentanaInterna(Ext.Msg.confirm('Borrar grupo','¿Desea borrar el grupo '+record.get('letra')+' y renombrar los grupos en orden alfabético?<br>Esta acci&oacute;n cerrará las pestañas de detalle de subgrupos y podría perder los cambios no guardados.',
    function(button){
        debug('clicked:',button);
        if(button=='yes')
        {
            _p25_storeGrupos.remove(record);
            _p25_renombrarGrupos();
        }
    }));
    debug('<_p25_borrarGrupoClic');
}

function _p25_editarGrupoClic(grid,rowIndex)
{
    var record = grid.getStore().getAt(rowIndex);
    debug('>_p25_editarGrupoClic:',record);
    
    var valido = true;
    
    if(valido)
    {
        valido = !Ext.isEmpty(record.get('cdplan'));
        if(!valido)
        {
            mensajeWarning('Favor de seleccionar un plan para el subgrupo');
        }
    }
    
    if(valido)
    {
        _p25_quitarTabsDetalleGrupo(record.get('letra'));
        _p25_tabpanel().setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p25_urlObtenerCoberturasColec
            ,params  :
            {
                'smap1.cdramo'    : _p25_smap1.cdramo
                ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
                ,'smap1.cdplan'   : record.get('cdplan')
                , 'smap1.cdsisrol': _p25_smap1.cdsisrol
            }
            ,success : function(response)
            {
                _p25_tabpanel().setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('json response:',json);
                if(json.exito)
                {
                    var numCoberturas=json.slist1.length;
                    debug('numCoberturas:',numCoberturas);
                    var contadorCoberturas=0;
                    _p25_tabpanel().setLoading(true);
                    for(var i=0;i<numCoberturas;i++)
                    {
                        Ext.Ajax.request(
                        {
                            url      : _p25_urlObtenerHijosCobertura
                            ,params  :
                            {
                                'smap1.cdramo'     : _p25_smap1.cdramo
                                ,'smap1.cdtipsit'  : _p25_smap1.cdtipsit
                                ,'smap1.cdplan'    : record.get('cdplan')
                                ,'smap1.cdgarant'  : json.slist1[i].CDGARANT
                                ,'smap1.indice'    : i
                                ,'smap1.cdatrivar' : record.get('parametros.pv_otvalor'+_p25_smap1.ATRIVAR_TATRIGAR)
                                ,'smap1.cdtipsup'  : _p25_smap1.cdtipsup
                                ,'smap1.nmpolant'  : _p25_smap1.nmpolant
                                ,'smap1.cdgrupo'   : record.get('letra')
                            }
                            ,success : function(response)
                            {
                                var json2=Ext.decode(response.responseText);
                                debug('json response ['+json2.smap1.indice+']:',json2);
                                if(json2.exito)
                                {
                                    contadorCoberturas=contadorCoberturas+1;
                                    json.slist1[json2.smap1.indice]['hijos']       = Ext.decode(json2.respuesta.substr("items:".length));
                                    json.slist1[json2.smap1.indice]['modeloHijos'] = Ext.decode(json2.respuestaOculta.substr("fields:".length));
                                    if(contadorCoberturas==numCoberturas)
                                    {
                                        var items = [];
                                        for(var j=0;j<numCoberturas;j++)
                                        {
                                            var hijos = json.slist1[j].hijos;
                                            for(var k=0;k<hijos.length;k++)
                                            {
                                                var hijo = hijos[k];
                                                hijo.fieldLabel='['+hijo.cdatribu+'] '+hijo.fieldLabel;
                                                if(hijo.maxValue&&hijo.maxValue<999999999)
                                                {
                                                    var compara = function(me)
                                                    {
                                                        if(me.isDisabled())
                                                        {
                                                            if(_p25_smap1.cdtipsup == _TIPO_SITUACION_RENOVACION){
                                                                me.removeCls('valorRenovacionColec');
                                                            }else{
                                                                me.removeCls('valorNoOriginal');
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if(me.getValue()!=me.valorInicial)
                                                            {
                                                                if(_p25_smap1.cdtipsup == _TIPO_SITUACION_RENOVACION){
                                                                    me.addCls('valorRenovacionColec');
                                                                }else{
                                                                    me.addCls('valorNoOriginal');
                                                                }
                                                            }
                                                            else
                                                            {
                                                                if(_p25_smap1.cdtipsup == _TIPO_SITUACION_RENOVACION){
                                                                    me.removeCls('valorRenovacionColec');
                                                                }else{
                                                                    me.removeCls('valorNoOriginal');
                                                                }
                                                            }
                                                        }
                                                    };
                                                    hijo.on(
                                                    {
                                                        change   : compara
                                                        ,disable : compara
                                                        ,enable  : compara
                                                    });
                                                }
                                            } 
                                            var item = Ext.create('Ext.form.Panel',
                                            {
                                                width       : 470
                                                ,frame      : true
                                                ,height     : 140
                                                ,autoScroll : true
                                                ,defaults   : { style : 'margin:5px;', labelWidth : 100 }
                                                ,items      : hijos
                                                ,cdgarant   : json.slist1[j].CDGARANT
                                                ,tbar       :
                                                [
                                                    {
                                                        xtype       : 'displayfield'
                                                        ,fieldLabel : '<span style="color:white;">'+json.slist1[j].DSGARANT+'</span>'
                                                        ,labelWidth : 220
                                                    }
                                                    ,{
                                                        xtype       : 'checkbox' 
                                                        ,boxLabel   : 'Amparada' + (_p25_smap1.mapaSwitchCoberValosit['_' + json.slist1[j].CDGARANT] === 'S' ? '**' : '')
                                                        ,name       : 'amparada'
                                                        ,inputValue : 'S'
	                                                    ,checked    : json.slist1[j].SWOBLIGA == 'S' // && (json.slist1[j].SWSELECCIONADO == 'S')
	                                                    ,readOnly   : json.slist1[j].SWEDITABLE == 'N'
                                                        ,style      : 'color:white;'
                                                        ,listeners  :
                                                        {
                                                            boxready : function(checkbox)
                                                            {
                                                                var value=checkbox.getValue();
                                                                debug('checkbox boxready:',value);
                                                                var form = checkbox.up().up();
                                                                for(var l=0;l<form.items.items.length;l++)
                                                                {
                                                                    form.items.items[l].setDisabled(!value);
                                                                }
                                                            }
                                                            ,change : function(checkbox,value)
                                                            {
                                                                debug('checkbox change:',value);
                                                                var form       = checkbox.up('form');
                                                                var miCdgarant = form.cdgarant;
                                                                for(var l=0;l<form.items.items.length;l++)
                                                                {
                                                                    form.items.items[l].setDisabled(!value);
                                                                    if(value
                                                                        &&(Ext.isEmpty(checkbox.flagPuedesBorrar) || checkbox.flagPuedesBorrar==true)
                                                                    )
                                                                    {
                                                                        try
                                                                        {
                                                                            form.items.items[l].reset();
                                                                        }
                                                                        catch(e)
                                                                        {
                                                                            debugError('Error cachado:',e);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    ,{
                                                        xtype       : 'displayfield'
                                                        ,fieldLabel : '<span style="color:white;">Buscar</span>'
                                                        ,labelWidth : 35
                                                    }
                                                    ,{
                                                        xtype       : 'textfield'
                                                        ,width      : 85
                                                        ,listeners  :
                                                        {
                                                            change : function(me)
                                                            {
                                                                var form=me.up('form');
                                                                debug('form:',form);
                                                                clearTimeout(_p25_filtroCobTimeout);
                                                                _p25_filtroCobTimeout = setTimeout(function()
                                                                {
                                                               Ext.Array.each(form.items.items,function(item)
                                                               {
                                                                   if(me.getValue()+'x'!='x')
                                                                   {
                                                                   	   //alert('2');
                                                                       var val=me.getValue().toLowerCase().replace(/s/g,'');
                                                                       //alert('3');
                                                                       if(item.fieldLabel.toLowerCase().replace(/s/g,'').lastIndexOf(val)!=-1)
                                                                       {
                                                                           //item.show();
                                                                           item.removeCls('tatrigarHide');
                                                                       }
                                                                       else
                                                                       {
                                                                           //item.hide();
                                                                           item.addCls('tatrigarHide');
                                                                       }
                                                                   }
                                                                   else
                                                                   {
                                                                       //item.show();
                                                                       item.removeCls('tatrigarHide');
                                                                   }
                                                               });
                                                               },1000); 
                                                            }
                                                        }
                                                    }
                                                    ,{
                                                        xtype  : 'hidden'
                                                        ,name  : 'swobliga'
                                                        ,value : json.slist1[j].SWOBLIGA
                                                    }
                                                    ,{
                                                        xtype  : 'hidden'
                                                        ,name  : 'cdgarant'
                                                        ,value : json.slist1[j].CDGARANT
                                                    }
                                                ]
                                            });
                                            if(record.valido)
                                            {
                                                debug('GRUPO VALIDO');
                                                Ext.define('_p25_modelo'+json.slist1[j].CDGARANT,
                                                {
                                                    extend  : 'Ext.data.Model'
                                                    ,fields : json.slist1[j].modeloHijos
                                                });
                                                tvalogars = record.tvalogars;
                                                tvalogar  = null;
                                                for(var k=0;k<tvalogars.length;k++)
                                                {
                                                    if(tvalogars[k].cdgarant==json.slist1[j].CDGARANT)
                                                    {
                                                        tvalogar = tvalogars[k];
                                                    }
                                                }
                                                var datosAnteriores = Ext.create('_p25_modelo'+json.slist1[j].CDGARANT,tvalogar);
                                                debug('Datos a cargar para '+json.slist1[j].CDGARANT+':',datosAnteriores,'con:',tvalogar);
                                                item.valido          = true;
                                                item.datosAnteriores = datosAnteriores;
                                            }
                                            else
                                            {
                                                debug('GRUPO NO VALIDO');
                                                item.valido = false;
                                            }
                                            items.push(item);
                                        }
                                        _p25_tabpanel().setLoading(false);
                                        grid.editingPlugin.cancelEdit();
                                        _p25_agregarTab(
                                        {
                                            title       : 'DETALLE DE SUBGRUPO '+record.get('letra')
                                            ,itemId     : 'id'+(new Date().getTime())
                                            ,letraGrupo : record.get('letra')
                                            ,tipo       : 'tabDetalleGrupo'
                                            ,defaults   : { style : 'margin:5px;' }
                                            ,items      :
                                            [
                                                Ext.create('Ext.panel.Panel',
                                                {
                                                    title        : 'COBERTURAS DEL SUBGRUPO'
                                                    ,maxHeight   : 490
                                                    ,hidden      : _p25_smap1.COBERTURAS=='N'
                                                    ,autoScroll  : true
                                                    ,collapsible : true
                                                    ,layout      :
                                                    {
                                                        type     : 'table'
                                                        ,columns : 2
                                                    }
                                                    ,defaults    : { style : 'margin:5px' }
                                                    ,items       : items
                                                    ,tbar        :
                                                    [
                                                        '->'
                                                        ,{
                                                            text     : 'Guardar configuraci&oacute;n'
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                                                            ,hidden  : _p25_smap1.cdsisrol!='COTIZADOR'||_p25_smap1.cdtipsit!='RC'
                                                            ,handler : function(me)
                                                            {
                                                                var ck = 'Guardando config.';
                                                                try
                                                                {
                                                                    var errores = [];
                                                                    var forms   = Ext.ComponentQuery.query('form',me.up().up());
                                                                    debug('forms:',forms)
                                                                    for(var i in forms)
                                                                    {
                                                                        if(!forms[i].isValid())
                                                                        {
                                                                            errores.push('Revisar la cobertura '+forms[i].down('displayfield').fieldLabel);
                                                                        }
                                                                    }
                                                                    if(errores.length>0)
                                                                    {
                                                                        throw errores.join('<br/>').replace(/white/g,'black');
                                                                    }
                                                                    
                                                                    var cdgrupo = me.up('[tipo=tabDetalleGrupo]').letraGrupo;
                                                                    debug('cdgrupo:',cdgrupo);
                                                                    
                                                                    var grupo = _p25_storeGrupos.findRecord('letra',cdgrupo);
                                                                    debug('grupo:',grupo);
                                                                    
                                                                    var nombrePlan = _p25_tabGruposModifiCols[2].editor
                                                                        .findRecord('key',grupo.get('cdplan')).get('value');
                                                                    debug('nombrePlan:',nombrePlan);
                                                                    
                                                                    var nombrePaq = _p25_tabGruposModifiCols[3].editor
                                                                        .findRecord('key',grupo.get('parametros.pv_otvalor01')).get('value');
                                                                    debug('nombrePaq:',nombrePaq);
                                                                    
                                                                    var derpol = grupo.get('parametros.pv_otvalor08');
                                                                    debug('derpol:',derpol);
                                                                    
                                                                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                                                                    {
                                                                        modal  : true
                                                                        ,title : '¿Desea actualizar?'
                                                                        ,html  : '<div style="padding:5px;">'
                                                                                     +'¿Desea actualizar la configuraci&oacute;n para el plan '
                                                                                     +nombrePlan+' y el<br/>paquete '+nombrePaq+' o desea guardar como un nuevo paquete'
                                                                                     +'?</div>'
                                                                        ,buttonAlign : 'center'
                                                                        ,buttons     :
                                                                        [
                                                                            {
                                                                                text     : 'Actualizar'
                                                                                ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                                                                                ,handler : function(me)
                                                                                {
                                                                                    var ck = 'Actualizando configuraci&oacute;n';
                                                                                    try
                                                                                    {
                                                                                        var tvalogars = [];
                                                                                        for(var i in forms)
                                                                                        {
                                                                                            tvalogars.push(forms[i].getValues());
                                                                                        }
                                                                                        debug('tvalogars:',tvalogars);
                                                                                        
                                                                                        var params =
                                                                                        {
                                                                                            smap1 :
                                                                                            {
                                                                                                cdramo    : _p25_smap1.cdramo
                                                                                                ,cdtipsit : _p25_smap1.cdtipsit
                                                                                                ,cdplan   : grupo.get('cdplan')
                                                                                                ,cdpaq    : grupo.get('parametros.pv_otvalor01')
                                                                                                ,dspaq    : '0'
                                                                                                ,derpol   : derpol
                                                                                            }
                                                                                            ,slist1 : tvalogars
                                                                                        };
                                                                                        debug('params:',params);
                                                                                        
                                                                                        var window = me.up('window');
                                                                                        window.setLoading(true);
                                                                                        Ext.Ajax.request(
                                                                                        {
                                                                                            url       : _p25_urlGuardarConfig4TVALAT
                                                                                            ,jsonData : params
                                                                                            ,success  : function(response)
                                                                                            {
                                                                                                window.setLoading(false);
                                                                                                var ck = 'Decodificando respuesta al guardar configuraci&oacute;n';
                                                                                                try
                                                                                                {
                                                                                                    var json = Ext.decode(response.responseText);
                                                                                                    debug('### guardar config:',json);
                                                                                                    if(json.success)
                                                                                                    {
                                                                                                        me.up('window').destroy();
                                                                                                        mensajeCorrecto('Datos guardados','Se actualiz\u00F3 la configuraci\u00F3n del paquete');
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                        mensajeError(json.respuesta);
                                                                                                    }
                                                                                                }
                                                                                                catch(e)
                                                                                                {
                                                                                                    manejaException(e,ck);
                                                                                                }
                                                                                            }
                                                                                            ,failure  : function()
                                                                                            {
                                                                                                window.setLoading(false);
                                                                                                errorComunicacion(null,'Error guardando configuraci&oacute;n');
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    catch(e)
                                                                                    {
                                                                                        manejaException(e,ck);
                                                                                    }
                                                                                }
                                                                            }
                                                                            ,{
                                                                                text     : 'Agregar nuevo paquete'
                                                                                ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                                                                                ,handler : function(me)
                                                                                {
                                                                                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                                                                                    {
                                                                                        modal  : true
                                                                                        ,title : 'Guardar nuevo paquete'
                                                                                        ,items :
                                                                                        [
                                                                                            {
                                                                                                xtype     : 'form'
                                                                                                ,defaults : { style : 'margin:5px;' }
                                                                                                ,items    :
                                                                                                [
                                                                                                    {
                                                                                                        xtype       : 'textfield'
                                                                                                        ,fieldLabel : 'Nombre'
                                                                                                        ,allowBlank : false
                                                                                                    }
                                                                                                ]
                                                                                                ,buttonAlign : 'center'
                                                                                                ,buttons     :
                                                                                                [
                                                                                                    {
                                                                                                        text     : 'Agregar nuevo paquete'
                                                                                                        ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                                                                                                        ,handler : function(me2)
                                                                                                        {
                                                                                                            if(!me2.up('form').isValid())
                                                                                                            {
                                                                                                                datosIncompletos();
                                                                                                            }
                                                                                                            else
                                                                                                            {
                                                                                                                var ck = 'Guardando nuevo paquete';
							                                                                                    try
							                                                                                    {
							                                                                                        var tvalogars = [];
							                                                                                        for(var i in forms)
							                                                                                        {
							                                                                                            tvalogars.push(forms[i].getValues());
							                                                                                        }
							                                                                                        debug('tvalogars:',tvalogars);
							                                                                                        
							                                                                                        var params =
							                                                                                        {
							                                                                                            smap1 :
							                                                                                            {
							                                                                                                cdramo    : _p25_smap1.cdramo
							                                                                                                ,cdtipsit : _p25_smap1.cdtipsit
							                                                                                                ,cdplan   : grupo.get('cdplan')
							                                                                                                ,cdpaq    : grupo.get('parametros.pv_otvalor01')
							                                                                                                ,dspaq    : me2.up('form').down('textfield').getValue()
							                                                                                                ,derpol   : derpol
							                                                                                            }
							                                                                                            ,slist1 : tvalogars
							                                                                                        };
							                                                                                        debug('params:',params);
							                                                                                        
							                                                                                        var window2 = me2.up('window');
							                                                                                        window2.setLoading(true);
							                                                                                        Ext.Ajax.request(
							                                                                                        {
							                                                                                            url       : _p25_urlGuardarConfig4TVALAT
							                                                                                            ,jsonData : params
							                                                                                            ,success  : function(response)
							                                                                                            {
							                                                                                                window2.setLoading(false);
							                                                                                                var ck = 'Decodificando respuesta al guardar nuevo paquete';
							                                                                                                try
							                                                                                                {
							                                                                                                    var json = Ext.decode(response.responseText);
							                                                                                                    debug('### nuevo paquete:',json);
							                                                                                                    if(json.success)
							                                                                                                    {
							                                                                                                        grupo.set('parametros.pv_otvalor01',json.smap1.cdPaqueteNuevo);
							                                                                                                        if(_p25_clasif==_p25_TARIFA_LINEA)
							                                                                                                        {
							                                                                                                            _p25_tabGruposModifiCols[3].editor.store.reload();
							                                                                                                            _p25_tabGruposLinealCols[3].editor.store.reload(
							                                                                                                            {
							                                                                                                                callback : function()
							                                                                                                                {
							                                                                                                                    _fieldById('_p25_tabGruposLineal').down('grid').getView().refresh();
							                                                                                                                }
							                                                                                                            });
							                                                                                                            
							                                                                                                        }
							                                                                                                        else
							                                                                                                        {
                                                                                                                                        _p25_tabGruposLinealCols[3].editor.store.reload();
							                                                                                                            _p25_tabGruposModifiCols[3].editor.store.reload(
							                                                                                                            {
							                                                                                                                callback : function()
							                                                                                                                {
							                                                                                                                    _fieldById('_p25_tabGruposModifi').down('grid').getView().refresh();
							                                                                                                                }
							                                                                                                            });
							                                                                                                            
							                                                                                                        }
							                                                                                                        debug('grupo:',grupo);
							                                                                                                        me.up('window').destroy();
							                                                                                                        window2.destroy();
							                                                                                                        mensajeCorrecto('Datos guardados','Se guard\u00F3 el nuevo paquete');
							                                                                                                    }
							                                                                                                    else
							                                                                                                    {
							                                                                                                        mensajeError(json.respuesta);
							                                                                                                    }
							                                                                                                }
							                                                                                                catch(e)
							                                                                                                {
							                                                                                                    manejaException(e,ck);
							                                                                                                }
							                                                                                            }
							                                                                                            ,failure  : function()
							                                                                                            {
							                                                                                                window2.setLoading(false);
							                                                                                                errorComunicacion(null,'Error guardando nuevo paquete');
							                                                                                            }
							                                                                                        });
							                                                                                    }
							                                                                                    catch(e)
							                                                                                    {
							                                                                                        manejaException(e,ck);
							                                                                                    }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                ]
                                                                                            }
                                                                                        ]
                                                                                    }).show());
                                                                                }
                                                                            }
                                                                        ]
                                                                    }).show());
                                                                }
                                                                catch(e)
                                                                {
                                                                    manejaException(e,ck);
                                                                }
                                                            }
                                                        }
                                                    ]
                                                })
                                                ,Ext.create('Ext.grid.Panel',
                                                {
                                                    title    : 'FACTORES DEL SUBGRUPO'
                                                    ,height  : 150
                                                    ,hidden  : _p25_smap1.FACTORES=='N'
                                                    ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
                                                    {
                                                        clicksToEdit  : 1
                                                        ,errorSummary : false
                                                        /*
                                                        ,listeners    :
                                                        {
                                                            beforeedit : function(editor)
                                                            {
                                                                centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Al cambiar el factor de incremento de inflaci&oacute;n<br/>y/o el factor de extraprima o renovaci&oacute;n entonces<br/>se cambiar&aacute; para todas las coberturas<br/>¿Desea continuar?', function(btn)
                                                                {
                                                                    if(btn=='yes')
                                                                    {
                                                                        _p25_incrinflAux = record.get('incrinfl');
                                                                        _p25_extrrenoAux = record.get('extrreno');
                                                                    }
                                                                    else
                                                                    {
                                                                        editor.cancelEdit();
                                                                    }
                                                                }));
                                                            }
                                                            ,edit : function(editor,e)
                                                            {
                                                                var letraGrupo  = e.record.get('letra');
                                                                var incrinfl    = e.record.get('incrinfl');
                                                                var extrreno    = e.record.get('extrreno');
                                                                debug('letraGrupo:',letraGrupo);
                                                                debug('_p25_incrinflAux:',_p25_incrinflAux,'incrinfl:',incrinfl);
                                                                debug('_p25_extrrenoAux:',_p25_extrrenoAux,'extrreno:',extrreno);
                                                                if(_p25_incrinflAux!=incrinfl)
                                                                {
                                                                    var pestania=Ext.ComponentQuery.query('[letraGrupo='+letraGrupo+']')[0];
                                                                    debug('pestania:',pestania);
                                                                    $.each(_p25_arrayNombresIncrinfl,function(i,nombre)
                                                                    {
                                                                        var componentes=Ext.ComponentQuery.query('[fieldLabel='+nombre+']',pestania);
                                                                        debug('componentes para poner factor incrinfl:',componentes);
                                                                        $.each(componentes,function(i,comp)
                                                                        {
                                                                            debug('poniendo valor en:',comp);
                                                                            comp.setValue(incrinfl);
                                                                        });
                                                                    });
                                                                }
                                                                if(_p25_extrrenoAux!=extrreno)
                                                                {
                                                                    var pestania=Ext.ComponentQuery.query('[letraGrupo='+letraGrupo+']')[0];
                                                                    debug('pestania:',pestania);
                                                                    $.each(_p25_arrayNombresExtrreno,function(i,nombre)
                                                                    {
                                                                        var componentes=Ext.ComponentQuery.query('[fieldLabel='+nombre+']',pestania);
                                                                        debug('componentes para poner factor extrreno:',componentes);
                                                                        $.each(componentes,function(i,comp)
                                                                        {
                                                                            debug('poniendo valor en:',comp);
                                                                            comp.setValue(extrreno);
                                                                        });
                                                                    });
                                                                }
                                                            }
                                                        }
                                                        */
                                                    })
                                                    ,columns : _p25_factoresColumns
                                                    ,store   : Ext.create('Ext.data.Store',
                                                    {
                                                        model : '_p25_modeloGrupo'
                                                        ,data : record
                                                    })
                                                    ,listeners :
                                                    {
                                                        afterrender : function()
                                                        {
                                                            if(_p25_factoresColumns.length>0
                                                                //&&_p25_smap1.FACTORES=='S'
                                                                //&&Ext.isEmpty(record.get(_p25_factoresColumns[0].editor.name))
                                                                && (!_p25_ntramite||_p25_ntramiteVacio)
                                                            )
                                                            {
                                                                var ponerValoresFactores=function()
                                                                {
                                                                    debug('_p25_valoresFactores:',_p25_valoresFactores);
                                                                    var letraGrupo = record.get('letra');
                                                                    var pestania   = Ext.ComponentQuery.query('[letraGrupo='+letraGrupo+']')[0];
                                                                    for(var i in _p25_valoresFactores)
                                                                    {
                                                                        var elem  = _p25_valoresFactores[i];
                                                                        var name  = 'parametros.pv_'+elem.NAME;
                                                                        var valor = elem.VALOR;
                                                                        record.set(name,valor);
                                                                    }
                                                                };
                                                                if(Ext.isEmpty(_p25_valoresFactores))
                                                                {
                                                                    Ext.Ajax.request(
                                                                    {
                                                                        url     : _p25_urlRecuperacionSimpleLista
                                                                        ,params :
                                                                        {
                                                                            'smap1.procedimiento' : 'RECUPERAR_VALORES_ATRIBUTOS_FACTORES'
                                                                            ,'smap1.cdramo'       : _p25_smap1.cdramo
                                                                            ,'smap1.cdtipsit'     : _p25_smap1.cdtipsit
                                                                        }
                                                                        ,success : function(response)
                                                                        {
                                                                            var jsonValfac=Ext.decode(response.responseText);
                                                                            debug('### valores factores:',jsonValfac);
                                                                            if(jsonValfac.exito)
                                                                            {
                                                                                _p25_valoresFactores=jsonValfac.slist1;
                                                                                ponerValoresFactores();
                                                                            }
                                                                            else
                                                                            {
                                                                                mensajeError(jsonValfac.respuesta);
                                                                            }
                                                                        }
                                                                        ,failure : function()
                                                                        {
                                                                            errorComunicacion();
                                                                        }
                                                                    });
                                                                }
                                                                else
                                                                {
                                                                    ponerValoresFactores();
                                                                }
                                                            }
                                                            else
                                                            {
                                                                debug('sin factores');
                                                            }
                                                        }
                                                    }
                                                })
                                                ,Ext.create('Ext.grid.Panel',
                                                {
                                                    title      : 'TARIFA POR EDADES'
                                                    ,minHeight : 100
                                                    ,hidden    : (_p25_ntramite && _cotcol_smap1.modificarTodo === false ? false : true)
                                                        || _p25_smap1.cdsisrol === RolSistema.Agente
                                                    ,maxHeight : 250
                                                    ,tbar      :
                                                    [
                                                        {
                                                            text     : 'Ver conceptos globales'
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/money_dollar.png'
                                                            ,hidden  : '|COTIZADOR|SUPTECSALUD|SUBDIRSALUD|DIRECSALUD|'.indexOf('|'+_p25_smap1.cdsisrol+'|')==-1
                                                            ,handler : function(){ _p25_generarVentanaVistaPrevia(true); }
                                                        }
                                                    ]
                                                    ,store     : Ext.create('Ext.data.Store',
                                                    {
                                                        model     : '_p25_modeloTarifaEdad'
                                                        ,autoLoad : _p25_ntramite ? true : false
                                                        ,proxy    :
                                                        {
                                                            type         : 'ajax'
                                                            ,timeout     : 1000*60*2
                                                            ,extraParams :
                                                            {
                                                                'smap1.cdunieco'  : _p25_smap1.cdunieco
                                                                ,'smap1.cdramo'   : _p25_smap1.cdramo
                                                                ,'smap1.estado'   : _p25_smap1.estado
                                                                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                                                                ,'smap1.nmsuplem' : '0'
                                                                ,'smap1.cdplan'   : record.get('cdplan')
                                                                ,'smap1.cdgrupo'  : record.get('letra')
                                                                ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
                                                            }
                                                            ,url         : _p25_urlObtenerTarifaEdad
                                                            ,reader      :
                                                            {
                                                                type  : 'json'
                                                                ,root : 'slist1'
                                                            }
                                                        }
                                                    })
                                                    ,columns   :
                                                    [
                                                        {
                                                            header     : 'Edad'
                                                            ,width     : 60
                                                            ,dataIndex : 'EDAD'
                                                        }
                                                        ,{
                                                            header     : 'No. Hombres'
                                                            ,width     : 100
                                                            ,dataIndex : 'HOMBRES'
                                                        }
                                                        ,{
                                                            header     : 'No. Mujeres'
                                                            ,width     : 100
                                                            ,dataIndex : 'MUJERES'
                                                        }
                                                        ,{
                                                            header     : 'Tarifa por hombre'
                                                            ,flex      : 1
                                                            ,dataIndex : 'TARIFA_UNICA_HOMBRES'
                                                            ,renderer  : Ext.util.Format.usMoney
                                                        }
                                                        ,{
                                                            header     : 'Tarifa por mujer'
                                                            ,flex      : 1
                                                            ,dataIndex : 'TARIFA_UNICA_MUJERES'
                                                            ,renderer  : Ext.util.Format.usMoney
                                                        }
                                                        ,{
                                                            header     : 'Total hombres'
                                                            ,flex      : 1
                                                            ,dataIndex : 'TARIFA_TOTAL_HOMBRES'
                                                            ,renderer  : Ext.util.Format.usMoney
                                                        }
                                                        ,{
                                                            header     : 'Total mujeres'
                                                            ,flex      : 1
                                                            ,dataIndex : 'TARIFA_TOTAL_MUJERES'
                                                            ,renderer  : Ext.util.Format.usMoney
                                                        }
                                                    ]
                                                })
                                                ,Ext.create('Ext.grid.Panel',
                                                {
                                                    title      : 'PRIMA PROMEDIO'
                                                    ,minHeight : 100
                                                    ,hidden    : (_p25_ntramite && _cotcol_smap1.modificarTodo === false ? false : true)
                                                        || _p25_smap1.cdsisrol === RolSistema.Agente
                                                    ,maxHeight : 250
                                                    ,store     : Ext.create('Ext.data.Store',
                                                    {
                                                        model     : '_p25_modeloTarifaCobertura'
                                                        ,autoLoad : _p25_ntramite ? true : false
                                                        ,proxy    :
                                                        {
                                                            type         : 'ajax'
                                                            ,extraParams :
                                                            {
                                                                'smap1.cdunieco'  : _p25_smap1.cdunieco
                                                                ,'smap1.cdramo'   : _p25_smap1.cdramo
                                                                ,'smap1.estado'   : _p25_smap1.estado
                                                                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                                                                ,'smap1.nmsuplem' : '0'
                                                                ,'smap1.cdplan'   : record.get('cdplan')
                                                                ,'smap1.cdgrupo'  : record.get('letra')
                                                                ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
                                                            }
                                                            ,url         : _p25_urlObtenerTarifaCobertura
                                                            ,reader      :
                                                            {
                                                                type  : 'json'
                                                                ,root : 'slist1'
                                                            }
                                                        }
                                                    })
                                                    ,columns   :
                                                    [
                                                        {
                                                            header           : 'Cobertura'
                                                            ,dataIndex       : 'DSGARANT'
                                                            ,flex            : 3
                                                            ,summaryType     : 'count'
                                                            ,summaryRenderer : function(value, summaryData, dataIndex) 
                                                            {
                                                                return Ext.String.format('Total de {0} cobertura{1}', value, value !== 1 ? 's' : '');
                                                            }
                                                        }
                                                        ,{
                                                            header       : 'Prima'
                                                            ,flex        : 1
                                                            ,dataIndex   : 'PRIMA_PROMEDIO'
                                                            ,renderer    : Ext.util.Format.usMoney
                                                            ,summaryType : 'sum'
                                                        }
                                                    ]
                                                    ,features  :
                                                    [
                                                        {
                                                            ftype: 'summary'
                                                        }
                                                    ]
                                                })
                                            ]
                                            ,buttonAlign : 'center'
                                            ,buttons     :
                                            [
                                                {
                                                    text     : 'Guardar'
                                                    ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                                                    ,handler : function(button){_p25_guardarGrupo(button.up().up());}
                                                    ,hidden  : _p25_smap1.COBERTURAS=='N'||_p25_smap1.COBERTURAS_BOTON=='N'
                                                }
                                            ]
                                        });
                                        var formsValidos = Ext.ComponentQuery.query('[valido=true]');
                                        debug('formsValidos:',formsValidos);
                                        for(var k=0;k<formsValidos.length;k++)
                                        {
                                            var form = formsValidos[k];
                                            debug('intento cargar:',form,'con:',form.datosAnteriores);
                                            //form.loadRecord(form.datosAnteriores);
                                            
                                            for(var attCampo in form.datosAnteriores.data)
                                            {
                                                var attCampoCampo = form.down('[name='+attCampo+']');
                                                if(!Ext.isEmpty(attCampoCampo))
                                                {
                                                    attCampoCampo.setValue(form.datosAnteriores.get(attCampo));
                                                    if(attCampoCampo.xtype=='combobox')
                                                    {
                                                        attCampoCampo.store.valorParaCargar = ''+form.datosAnteriores.get(attCampo);
                                                        attCampoCampo.store.padre = attCampoCampo;
                                                        attCampoCampo.store.on(
                                                        {
                                                            load : function(me)
                                                            {
                                                                me.padre.setValue(me.valorParaCargar);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            
                                            if(form.datosAnteriores.raw&&form.datosAnteriores.raw.amparada=='S')
                                            {
                                                form.down('[name=amparada]').flagPuedesBorrar = false;
                                                form.down('[name=amparada]').setValue(true);
                                                debug('se "checkeo" el box');
                                                form.down('[name=amparada]').flagPuedesBorrar = true;
                                            }
                                            else
                                            {
                                                form.down('[name=amparada]').setValue(false);
                                                debug('se "descheckeo" el box');
                                            }
                                            debug('cargado:',form);
                                        }
                                    }
                                }
                                else
                                {
                                    _p25_tabpanel().setLoading(false);
                                    mensajeError(json2.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                _p25_tabpanel().setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                _p25_tabpanel().setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p25_editarGrupoClic');
}

function _p25_guardarGrupo(panelGrupo)
{
     debug('>_p25_guardarGrupo:',panelGrupo);
     
     var letraGrupo  = panelGrupo.letraGrupo;
     debug('letraGrupo:',letraGrupo);
     
     var formsTatrigar = panelGrupo.down('[title=COBERTURAS DEL SUBGRUPO]').items.items;
     debug('formsTatrigar:',formsTatrigar);
     
     var tvalogars = [];
     var valido    = true;
     if(_p25_clasif==_p25_TARIFA_MODIFICADA||_p25_smap1.LINEA_EXTENDIDA=='N')
     {
         for(var i=0;i<formsTatrigar.length;i++)
         {
             var iFormTatrigar = formsTatrigar[i];
             valido            = valido && iFormTatrigar.isValid();
             var tvalogar      = iFormTatrigar.getValues();
             if(false)//tvalogar.swobliga=='S'&&_p25_smap1.cdsisrol!='COTIZADOR')
             {
                 tvalogar['amparada']='S';
             }
             else
             {
                 if(!tvalogar.amparada)
                 {
                     tvalogar['amparada']='N';
                 }
             }
             tvalogars.push(tvalogar);
         }
         if(!valido)
         {
             datosIncompletos();
         }
     }
     
     if(valido)
     {
         debug('tvalogars:',tvalogars);
         var recordGrupo=_p25_obtenerGrupoPorLetra(letraGrupo);
         recordGrupo['tvalogars'] = tvalogars;
         recordGrupo['valido']    = true;
         debug('recordGrupo:',recordGrupo);
         if(_p25_smap1.FACTORES=='S')
         {
             var storeFactores = panelGrupo.down('grid[title=FACTORES DEL SUBGRUPO]').getStore();
             debug('storeFactores:',storeFactores);
             storeFactores.commitChanges();
         }
       	mensajeCorrecto('Se han guardado los datos','Se han guardado los datos',_p25_setActiveResumen);
     }
     
     debug('<_p25_guardarGrupo');
}

function _p25_obtenerGrupoPorLetra(letra)
{
    debug('>_p25_obtenerGrupoPorLetra:',letra);
    var recordGrupo = null;
    _p25_storeGrupos.each(function(record)
    {
        if(record.get('letra')==letra)
        {
            recordGrupo = record;
        }
    });
    debug('<_p25_obtenerGrupoPorLetra:',recordGrupo);
    return recordGrupo;
}

function _p25_setActiveResumen()
{
    debug('>_p25_setActiveResumen');
    _p25_setActiveTab(_p25_tabGrupos.itemId);
    window.parent.scrollTo(0, 0);
    debug('<_p25_setActiveResumen');
}

function _p25_generarTramiteClic(callback,sincenso,revision,complemento,nombreCensoParaConfirmar,asincrono)
{
    debug('>_p25_generarTramiteClic callback?' , !Ext.isEmpty(callback)   , ',sincenso:'    , sincenso    , '.');
    debug('revision:'                          , revision                 , ',complemento:' , complemento , '.');
    debug('nombreCensoParaConfirmar:'          , nombreCensoParaConfirmar , ',asincrono:'   , asincrono   , '.');
    var valido = true;
    
    if(valido){
		 if((_p25_smap1.cdsisrol=='SUSCRIPTOR'&& (_p25_smap1.status-0==19 || _p25_smap1.status-0==21 || _p25_smap1.status-0==23) ) && !_contratanteSaved){
		 	valido = false;
		 	mensajeWarning('Debe Guardar el Contratante.');
		 }
    }
    
    if(valido)
    {
        //parche para sin censo>
        if(
            (!Ext.isEmpty(sincenso)&&sincenso==true)
            ||!Ext.isEmpty(nombreCensoParaConfirmar)
        )
        {
            _fieldByName('censo').allowBlank=true;
        }
        //<parche para sin censo
        
        valido = _p25_tabConcepto().down('[xtype=form]').isValid();
        
        //parche para sin censo>
        if(
            (!Ext.isEmpty(sincenso)&&sincenso==true)
            ||!Ext.isEmpty(nombreCensoParaConfirmar)
        )
        {
            _fieldByName('censo').allowBlank=_p25_ntramite&&_p25_smap1.sincenso!='S' ? true : false;
        }
        //<parche para sin censo
        if(!valido)
        {
            mensajeWarning('Verificar los datos del concepto y el censo de asegurados',_p25_setActiveConcepto);
        }
        
        //debug("numcontrato",_fieldByName('tvalopol_parametros.pv_otvalor20').getValue() );
      	//parche para numcontrato>
      	var campoNumContrato = _fieldByName('tvalopol_parametros.pv_otvalor20', null, true);
      	if (!Ext.isEmpty(campoNumContrato)
      	    && campoNumContrato.fieldLabel.toUpperCase().indexOf('CONTRATO') != -1 ) {
      	    if (Ext.isEmpty(campoNumContrato.getValue())) {
      		    campoNumContrato.setValue('0');
            }
            if (_p21_smap1.cdunieco == 1403 && campoNumContrato.getValue() == 0) {
	        	valido=false;
	        	mensajeWarning('Verificar los datos del numero de contrato', _p25_setActiveConcepto);
        	}
      	}
    }
    
    if(valido)
    {    
        valido = _p25_storeGrupos.getCount()>0;
        debug('_p25_storeGrupos.getCount()>0:',_p25_storeGrupos.getCount()>0);
        if(!valido)
        {
            if(!_p25_tabGrupos)
            {
                mensajeWarning('Debe introducir el n&uacute;mero de asegurados',_p25_setActiveConcepto);
            }
            else
            {
                mensajeWarning('Debe introducir al menos un grupo',_p25_setActiveResumen);
            }
        }
    }
    
    if(valido&&_p25_clasif==_p25_TARIFA_LINEA)
    {
        var mensajeDeError = 'Falta definir los datos para el(los) grupo(s): ';
        _p25_storeGrupos.each(function(record)
        {
            if(Ext.isEmpty(record.get('cdplan')))
            {
                valido         = false;
                mensajeDeError = mensajeDeError + record.get('letra') + ' ';
            }
        });
        if(!valido)	
        {
            mensajeWarning(mensajeDeError,_p25_setActiveResumen);
        }
    }
    
    if(valido&&(_p25_clasif==_p25_TARIFA_MODIFICADA||_p25_smap1.LINEA_EXTENDIDA=='N'))
    {
        var mensajeDeError = 'Falta definir o guardar el detalle para el(los) grupo(s): ';
        _p25_storeGrupos.each(function(record)
        {
            if(!record.valido)
            {
                valido         = false;
                mensajeDeError = mensajeDeError + record.get('letra') + ' ';
            }
        });
        if(!valido)
        {
            mensajeWarning(mensajeDeError,_p25_setActiveResumen);
        }
    }
    
    if(valido)
    {
        var form=_p25_tabConcepto().down('[xtype=form]');
        form.setLoading(true);
        var timestamp = new Date().getTime();
        
        var micallback = function()
        {
            var form=_p25_tabConcepto().down('[xtype=form]');
            var disabled = _p25_tabpanel().isDisabled();
            if(disabled)
            {
                _p25_tabpanel().setDisabled(false);
            }
            var conceptos = form.getValues();
            if(disabled)
            {
                _p25_tabpanel().setDisabled(true);
            }
            conceptos['timestamp']             = timestamp;
            conceptos['clasif']                = _p25_clasif;
            conceptos['LINEA_EXTENDIDA']       = _p25_smap1.LINEA_EXTENDIDA;
            conceptos['cdunieco']              = _p25_smap1.cdunieco;
            conceptos['cdramo']                = _p25_smap1.cdramo;
            conceptos['cdtipsit']              = _p25_smap1.cdtipsit;
            conceptos['ntramiteVacio']         = _p25_ntramiteVacio ? _p25_ntramiteVacio : ''
            conceptos['sincenso']              = !Ext.isEmpty(sincenso)&&sincenso==true?'S':'N';
            conceptos['censoAtrasado']         = !Ext.isEmpty(_p25_smap1.sincenso)&&_p25_smap1.sincenso=='S'?'S':'N';
            conceptos['resubirCenso']          = _p25_resubirCenso;
            conceptos['complemento']           = true==complemento?'S':'N';
            conceptos['nombreCensoConfirmado'] = nombreCensoParaConfirmar;
            conceptos['asincrono']             = asincrono;
            conceptos['duplicar']              = _cotcol_smap1.modificarTodo === true ? 'S' : 'N';
            
            if(_p25_smap1.cdsisrol=='SUSCRIPTOR'&& (_p25_smap1.status-0==19 || _p25_smap1.status-0==21 || _p25_smap1.status-0==23) ){
            		conceptos.swexiper = 'S';
            		
                	conceptos.codpostal = codpostalDefinitivo; 
                	conceptos.cdedo     = cdedoDefinitivo; 
                	conceptos.cdmunici  = cdmuniciDefinitivo; 
            }else{
                	conceptos.swexiper = 'N';
            }
            
            var grupos = [];
            _p25_storeGrupos.each(function(record)
            {
                var grupo = record.data;
                grupo['tvalogars']=record.tvalogars;
                grupos.push(grupo);
            });
            Ext.Ajax.request(
            {
                url       : _p25_urlGenerarTramiteGrupo
                ,jsonData :
                {
                    smap1   : conceptos
                    ,olist1 : grupos
                }
                ,success  : function(response)
                {
                    form.setLoading(false);
                    var json=Ext.decode(response.responseText);
                    debug('### generar tramite:',json);
                    if(json.exito)
                    {
                        if((_p25_ntramite||_p25_ntramiteVacio) && _cotcol_smap1.modificarTodo === false)
                        {
                            if(callback)
                            {
                                if(!_p25_ntramite)
                                {
                                    _p25_smap1.ntramite = _p25_ntramiteVacio;
                                    _p25_smap1.nmpoliza = json.smap1.nmpoliza;
                                }
                                if((true==revision||_p25_ntramiteVacio)
                                    &&!(_p25_ntramite&&_p25_smap1.sincenso!='S')
                                    &&Ext.isEmpty(nombreCensoParaConfirmar)
                                )
                                {
                                    _p25_tabpanel().setDisabled(true);
                                    var ck = 'Recuperando asegurados para revision';
                                    try
                                    {
                                        _p25_tabpanel().setLoading(true);
                                        Ext.Ajax.request(
                                        {
                                            url      : _p25_urlRecuperacionSimpleLista
                                            ,params  :
                                            {
                                                'smap1.procedimiento' : 'RECUPERAR_REVISION_COLECTIVOS'
                                                ,'smap1.cdunieco'     : _p25_smap1.cdunieco
                                                ,'smap1.cdramo'       : _p25_smap1.cdramo
                                                ,'smap1.estado'       : 'W'
                                                ,'smap1.nmpoliza'     : json.smap1.nmpoliza
                                            }
                                            ,success : function(response)
                                            {
                                                var ck = 'Decodificando datos de asegurados para revision';
                                                try
                                                {
                                                    _p25_tabpanel().setLoading(false);
                                                    var json2 = Ext.decode(response.responseText);
                                                    debug('### asegurados:',json2);
                                                    var store = Ext.create('Ext.data.Store',
                                                    {
                                                        model : '_p25_modeloRevisionAsegurado'
                                                        ,data : json2.slist1
                                                    });
                                                    debug('store.getRange():',store.getRange());
                                                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                                                    {
                                                        width   : 600
                                                        ,height : 500
                                                        ,title  : 'Revisar asegurados del censo'
                                                        ,closable : false
                                                        ,listeners :
                                                        {
                                                            afterrender : function()
                                                            {
                                                                if(json2.slist1.length==0)
                                                                {
                                                                    setTimeout(function(){mensajeError('No se registraron asegurados, favor de revisar a detalle los errores');},1000);
                                                                }
                                                            }
                                                        }
                                                        ,items  :
                                                        [
                                                            Ext.create('Ext.panel.Panel',
                                                            {
                                                                layout    : 'hbox'
                                                                ,border   : 0
                                                                ,defaults : { style : 'margin:5px;' }
                                                                ,height   : 40
                                                                ,items    :
                                                                [
                                                                    {
                                                                        xtype       : 'displayfield'
                                                                        ,fieldLabel : 'Filas leidas'
                                                                        ,value      : json.smap1.filasLeidas
                                                                    }
                                                                    ,{
                                                                        xtype       : 'displayfield'
                                                                        ,fieldLabel : 'Filas procesadas'
                                                                        ,value      : json.smap1.filasProcesadas
                                                                    }
                                                                    ,{
                                                                        xtype       : 'displayfield'
                                                                        ,fieldLabel : 'Filas con error'
                                                                        ,value      : json.smap1.filasErrores
                                                                    }
                                                                    ,{
                                                                        xtype    : 'button'
                                                                        ,text    : 'Ver errores'
                                                                        ,hidden  : Number(json.smap1.filasErrores)==0
                                                                        ,handler : function()
                                                                        {
                                                                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                                                                            {
                                                                                modal        : true
                                                                                ,closeAction : 'destroy'
                                                                                ,title       : 'Errores al procesar censo'
                                                                                ,width       : 800
                                                                                ,height      : 500
                                                                                ,items       :
                                                                                [
                                                                                    {
                                                                                        xtype       : 'textarea'
                                                                                        ,fieldStyle : 'font-family: monospace'
                                                                                        ,value      : json.smap1.erroresCenso
                                                                                        ,readOnly   : true
                                                                                        ,width      : 780
                                                                                        ,height     : 440
                                                                                    }
                                                                                ]
                                                                            }).show());
                                                                        }
                                                                    }
                                                                ]
                                                            })
                                                            ,Ext.create('Ext.grid.Panel',
                                                            {
                                                                height   : 350
                                                                ,columns :
                                                                [
                                                                    {
                                                                        text       : 'Grupo'
                                                                        ,dataIndex : 'CDGRUPO'
                                                                        ,width     : 60
                                                                    }
                                                                    ,{
                                                                        text       : 'No.'
                                                                        ,dataIndex : 'NMSITUAC'
                                                                        ,width     : 40
                                                                    }
                                                                    ,{
                                                                        text       : 'Parentesco'
                                                                        ,dataIndex : 'PARENTESCO'
                                                                        ,width     : 120
                                                                    }
                                                                    ,{
                                                                        text       : 'Nombre'
                                                                        ,dataIndex : 'NOMBRE'
                                                                        ,width     : 200
                                                                    }
                                                                    ,{
                                                                        text       : 'Sexo'
                                                                        ,dataIndex : 'SEXO'
                                                                        ,width     : 80
                                                                    }
                                                                    ,{
                                                                        text       : 'Edad'
                                                                        ,dataIndex : 'EDAD'
                                                                        ,width     : 60
                                                                    }
                                                                ]
                                                                ,store : store
                                                            })
                                                        ]
                                                        ,buttonAlign : 'center'
                                                        ,buttons     :
                                                        [
                                                            {
                                                                text      : 'Aceptar y continuar'
                                                                ,icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                                                ,disabled : Number(json.smap1.filasErrores)>0
                                                                ,handler  : function(me)
                                                                {
                                                                    me.up('window').destroy();
                                                                    _p25_generarTramiteClic(
                                                                        callback
                                                                        ,sincenso
                                                                        ,false //revision
                                                                        ,complemento
                                                                        ,json.smap1.nombreCensoParaConfirmar
                                                                    );
                                                                }
                                                                ,listeners :
                                                                {
                                                                    afterrender : function(me)
                                                                    {
                                                                        if(Number(json.smap1.filasErrores)>0)
                                                                        {
                                                                            _p25_desbloqueoBotonRol(me);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            ,{
                                                                text     : 'Modificar datos'
                                                                ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
                                                                ,handler : function(me)
                                                                {
                                                                    me.up('window').destroy();
                                                                    _p25_tabpanel().setDisabled(false);
                                                                    _p25_resubirCenso = 'S';
                                                                }
                                                            }
                                                        ]
                                                    }).show());
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck);
                                                }
                                            }
                                            ,failure : function()
                                            {
                                                _p25_tabpanel().setLoading(false);
                                                errorComunicacion(ck);
                                            }
                                        });
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }
                                }
                                else
                                {
                                    callback(json);
                                }
                            }
                            else
                            {
                                mensajeError(json.respuesta+'<br/>Se guard&oacute; la informaci&oacute;n pero no hay callback');
                            }
                        }
                        else
                        {
                            _p25_fieldNmpoliza().setValue(json.smap1.nmpoliza);
                            _p25_fieldNtramite().setValue(json.smap1.ntramite);
                            _p25_tabpanel().setDisabled(true);
                            
                            if(Ext.isEmpty(nombreCensoParaConfirmar))
                            {
	                            mensajeCorrecto('Tr&aacute;mite generado',json.respuesta+'<br/>Para revisar los datos presiona aceptar',function()
	                            {
	                                var ck = 'Recuperando asegurados para revision';
	                                try
	                                {
	                                    _p25_tabpanel().setLoading(true);
	                                    Ext.Ajax.request(
	                                    {
	                                        url      : _p25_urlRecuperacionSimpleLista
	                                        ,params  :
	                                        {
	                                            'smap1.procedimiento' : 'RECUPERAR_REVISION_COLECTIVOS'
	                                            ,'smap1.cdunieco'     : _p25_smap1.cdunieco
	                                            ,'smap1.cdramo'       : _p25_smap1.cdramo
	                                            ,'smap1.estado'       : 'W'
	                                            ,'smap1.nmpoliza'     : json.smap1.nmpoliza
	                                        }
	                                        ,success : function(response)
	                                        {
	                                            var ck = 'Decodificando datos de asegurados para revision';
	                                            try
	                                            {
	                                                _p25_tabpanel().setLoading(false);
	                                                var json2 = Ext.decode(response.responseText);
	                                                debug('### asegurados:',json2);
	                                                var store = Ext.create('Ext.data.Store',
	                                                {
	                                                    model : '_p25_modeloRevisionAsegurado'
	                                                    ,data : json2.slist1
	                                                });
	                                                debug('store.getRange():',store.getRange());
	                                                centrarVentanaInterna(Ext.create('Ext.window.Window',
	                                                {
	                                                    width   : 600
	                                                    ,height : 500
	                                                    ,title  : 'Revisar asegurados del censo'
	                                                    ,closable : false
                                                        ,listeners :
                                                        {
                                                            afterrender : function()
                                                            {
                                                                if(json2.slist1.length==0)
                                                                {
                                                                    setTimeout(function(){mensajeError('No se registraron asegurados, favor de revisar a detalle los errores');},1000);
                                                                }
                                                            }
                                                        }
	                                                    ,items  :
	                                                    [
	                                                        Ext.create('Ext.panel.Panel',
	                                                        {
	                                                            layout    : 'hbox'
	                                                            ,border   : 0
	                                                            ,defaults : { style : 'margin:5px;' }
	                                                            ,height   : 40
	                                                            ,items    :
	                                                            [
	                                                                {
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas leidas'
	                                                                    ,value      : json.smap1.filasLeidas
	                                                                }
	                                                                ,{
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas procesadas'
	                                                                    ,value      : json.smap1.filasProcesadas
	                                                                }
	                                                                ,{
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas con error'
	                                                                    ,value      : json.smap1.filasErrores
	                                                                }
	                                                                ,{
	                                                                    xtype    : 'button'
	                                                                    ,text    : 'Ver errores'
	                                                                    ,hidden  : Number(json.smap1.filasErrores)==0
	                                                                    ,handler : function()
	                                                                    {
	                                                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
	                                                                        {
	                                                                            modal        : true
	                                                                            ,closeAction : 'destroy'
	                                                                            ,title       : 'Errores al procesar censo'
	                                                                            ,width       : 800
	                                                                            ,height      : 500
	                                                                            ,items       :
	                                                                            [
	                                                                                {
	                                                                                    xtype       : 'textarea'
	                                                                                    ,fieldStyle : 'font-family: monospace'
	                                                                                    ,value      : json.smap1.erroresCenso
	                                                                                    ,readOnly   : true
	                                                                                    ,width      : 780
	                                                                                    ,height     : 440
	                                                                                }
	                                                                            ]
	                                                                        }).show());
	                                                                    }
	                                                                }
	                                                            ]
	                                                        })
	                                                        ,Ext.create('Ext.grid.Panel',
	                                                        {
	                                                            height   : 350
	                                                            ,columns :
	                                                            [
	                                                                {
	                                                                    text       : 'Grupo'
	                                                                    ,dataIndex : 'CDGRUPO'
	                                                                    ,width     : 60
	                                                                }
	                                                                ,{
	                                                                    text       : 'No.'
	                                                                    ,dataIndex : 'NMSITUAC'
	                                                                    ,width     : 40
	                                                                }
	                                                                ,{
	                                                                    text       : 'Parentesco'
	                                                                    ,dataIndex : 'PARENTESCO'
	                                                                    ,width     : 120
	                                                                }
	                                                                ,{
	                                                                    text       : 'Nombre'
	                                                                    ,dataIndex : 'NOMBRE'
	                                                                    ,width     : 200
	                                                                }
	                                                                ,{
	                                                                    text       : 'Sexo'
	                                                                    ,dataIndex : 'SEXO'
	                                                                    ,width     : 80
	                                                                }
	                                                                ,{
	                                                                    text       : 'Edad'
	                                                                    ,dataIndex : 'EDAD'
	                                                                    ,width     : 60
	                                                                }
	                                                            ]
	                                                            ,store : store
	                                                        })
	                                                    ]
	                                                    ,buttonAlign : 'center'
	                                                    ,buttons     :
	                                                    [
	                                                        {
	                                                            text      : 'Aceptar y subir documentos'
	                                                            ,icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                                                ,disabled : Number(json.smap1.filasErrores)>0
	                                                            ,handler  : function(me)
	                                                            {
	                                                                me.up('window').destroy();
                                                                    _p25_generarTramiteClic(
                                                                        null
                                                                        ,sincenso
                                                                        ,false //revision
                                                                        ,complemento
                                                                        ,json.smap1.nombreCensoParaConfirmar
                                                                    );
	                                                            }
                                                                ,listeners :
                                                                {
                                                                    afterrender : function(me)
                                                                    {
                                                                        if(Number(json.smap1.filasErrores)>0)
                                                                        {
                                                                            _p25_desbloqueoBotonRol(me);
                                                                        }
                                                                    }
                                                                }
	                                                        }
	                                                        ,{
	                                                            text     : 'Modificar datos'
	                                                            ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
	                                                            ,handler : function(me)
	                                                            {
	                                                                me.up('window').destroy();
	                                                                _p25_tabpanel().setDisabled(false);
	                                                                _p25_resubirCenso = 'S';
	                                                            }
	                                                        }
	                                                    ]
	                                                }).show());
	                                            }
	                                            catch(e)
	                                            {
	                                                manejaException(e,ck);
	                                            }
	                                        }
	                                        ,failure : function()
	                                        {
	                                            _p25_tabpanel().setLoading(false);
	                                            errorComunicacion(null,'Recuperando asegurados para revision');
	                                        }
	                                    });
	                                }
	                                catch(e)
	                                {
	                                    manejaException(e,ck);
	                                }
	                            });
	                        }
	                        else
	                        {
	                            if ('S' === _p25_smap1.rstn) {
	                                try {
	                                    Ext.Ajax.request({
	                                        url    : _p25_guardarReporteCotizacion,
	                                        params : {
	                                            'smap1.cdunieco' : json.smap1.cdunieco,
	                                            'smap1.cdramo'   : json.smap1.cdramo,
	                                            'smap1.estado'   : 'W',
	                                            'smap1.nmpoliza' : json.smap1.nmpoliza,
	                                            'smap1.cdperpag' : _fieldByName('cdperpag').getValue(),
	                                            'smap1.cdtipsit' : _p25_smap1.cdtipsit,
	                                            'smap1.ntramite' : json.smap1.ntramite,
	                                            'smap1.nGrupos'  : _p25_storeGrupos.getCount(),
	                                            'smap1.status'   : '14',
	                                            'smap1.caseIdRstn' : _NVL(_p25_smap1.caseIdRstn)
	                                        }
	                                    });
	                                } catch (e) {
	                                    manejaException(e, 'Generando y enviando documentos de cotizacion');
	                                }
	                                mensajeCorrecto(
	                                    'Tr\u00e1mite generado',
	                                    'Se ha generado el tr\u00e1mite ' + json.smap1.ntramite,
	                                    function () {
	                                        _mask();
	                                        location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?ntramite=' + json.smap1.ntramite;
	                                    }
	                                );
	                                return;
	                            }
	                            
	                            var callbackConfirmado = function (callback)
                                {
                                    centrarVentanaInterna(Ext.create('Ext.window.Window',
		                            {
		                                width        : 600
		                                ,height      : 440
		                                ,title       : 'Subir documentos de tu tr&aacute;mite ('+json.smap1.ntramite+')'
		                                ,closable    : false
		                                ,modal       : true
		                                ,loadingMask : true
		                                ,loader      :
		                                {
		                                    url       : _p25_urlVentanaDocumentos
		                                    ,scripts  : true
		                                    ,autoLoad : true
		                                    ,params   :
		                                    {
		                                        'smap1.cdunieco'  : json.smap1.cdunieco
		                                        ,'smap1.cdramo'   : json.smap1.cdramo
		                                        ,'smap1.estado'   : 'W'
		                                        ,'smap1.nmpoliza' : '0'
		                                        ,'smap1.nmsolici' : '0'
		                                        ,'smap1.nmsuplem' : '0'
		                                        ,'smap1.ntramite' : json.smap1.ntramite
		                                        ,'smap1.tipomov'  : '0'
		                                    }
		                                }
		                                ,buttonAlign : 'center'
		                                ,buttons     :
		                                [
		                                    {
		                                        text     : 'Continuar'
		                                        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
		                                        ,handler : function(me)
		                                        {
		                                            location.reload();
		                                        }
		                                    }
		                                ]
		                            }).show());
		                            if(!Ext.isEmpty(json.smap1.nombreUsuarioDestino))
		                            {
		                                mensajeCorrecto(
		                                    'Tr&aacute;mite asignado',
		                                    'El tr&aacute;mite '+json.smap1.ntramite+' fue asignado a '+json.smap1.nombreUsuarioDestino,
		                                    function () {
                                                if (_cotcol_smap1.modificarTodo === true) {
                                                    _p25_mesacontrol();
                                                }
                                            }
		                                );
		                            } else {
		                                mensajeCorrecto(
		                                    'Tr\u00e1mite generado',
                                            'Se ha generado el tr\u00e1mite ' + json.smap1.ntramite +
                                                ', favor de revisar los requisitos y subir sus documentos antes de turnar ' +
                                                'al \u00e1rea t\u00e9cnica',
                                            callback
                                        );
		                            }
		                        };
		                        var callbackNormal = callbackConfirmado;
		                        var mask, ck = 'Recuperando lista de requisitos';
                                try {
                                    var ntramite = json.smap1.ntramite;
                                    ck = 'Recuperando validaci\u00f3n ligada a requisitos';
                                    mask = _maskLocal(ck);
                                    Ext.Ajax.request({
                                        url     : _GLOBAL_URL_RECUPERACION,
                                        params  : {
                                            'params.consulta' : 'RECUPERAR_VALIDACION_POR_CDVALIDAFK',
                                            'params.ntramite' : ntramite,
                                            'params.clave'    : '_CONFCOT'
                                        },
                                        success : function (response) {
                                            mask.close();
                                            var ck = 'Decodificando respuesta al recuperar validaci\u00f3n ligada a requisitos';
                                            try {
                                                var valida = Ext.decode(response.responseText);
                                                debug('### validacion ligada a checklist:', valida);
                                                if (valida.success === true) {
                                                    if (valida.list.length > 0) {
                                                        _cargarAccionesEntidad(
                                                            valida.list[0].CDTIPFLU,
                                                            valida.list[0].CDFLUJOMC,
                                                            valida.list[0].TIPOENT,
                                                            valida.list[0].CDENTIDAD,
                                                            valida.list[0].WEBID,
                                                            function (acciones) {
                                                                if (acciones.length > 0) {
                                                                    debug('acciones:', acciones);
                                                                    callbackNormal(function () {
                                                                        _procesaAccion(
                                                                            acciones[0].CDTIPFLU,
                                                                            acciones[0].CDFLUJOMC,
                                                                            acciones[0].TIPODEST,
                                                                            acciones[0].CLAVEDEST,
                                                                            acciones[0].WEBIDDEST,
                                                                            acciones[0].AUX,
                                                                            valida.params.ntramite,
                                                                            valida.list[0].STATUS,
                                                                            null, //cdunieco
                                                                            null, //cdramo
                                                                            null, //estado
                                                                            null, //nmpoliza
                                                                            null, //nmsituac
                                                                            null, //nmsuplem
                                                                            valida.list[0].cdusuari,
                                                                            valida.list[0].cdsisrol,
                                                                            null // callback
                                                                        );
                                                                    });
                                                                } else {
                                                                    callbackNormal();
                                                                }
                                                            }
                                                        );
                                                    } else {
                                                        callbackNormal();
                                                    }
                                                } else {
                                                    mensajeError(json.message);
                                                }
                                            } catch (e) {
                                                manejaException(e, ck);
                                            }
                                        },
                                        failure : function () {
                                            mask.close();
                                            errorComunicacion(null, 'Error al recuperar validaci\u00f3n ligada a requisitos');
                                        }
                                    });
                                } catch (e) {
                                    manejaException(e, ck, mask);
                                    callbackNormal();
                                }
	                        }
                        }
                    }
                    else
                    {
                        mensajeError(json.respuesta,function(){});
                    }
                }
                ,failure  : function()
                {
                    form.setLoading(false);
                    errorComunicacion();
                }
            });
        }
        
        if(
            (!Ext.isEmpty(sincenso)&&sincenso==true)
            ||!Ext.isEmpty(nombreCensoParaConfirmar)
        )
        {
            micallback();
        }
        else
        {
            form.submit(
            {
                params   :
                {
                    'smap1.timestamp' : timestamp
                    ,'smap1.ntramite' : _p25_ntramite && _p25_smap1.sincenso != 'S' && _cotcol_smap1.modificarTodo !== true
                        ? _p25_ntramite
                        : ''
                }
                ,success : function()
                {
                    micallback();
                }
                ,failure : function()
                {
                    form.setLoading(false);
                    errorComunicacion();
                }
            });
        }
    }
    
    debug('<_p25_generarTramiteClic');
}

function _p25_fieldNtramite()
{
    debug('>_p25_fieldNtramite<');
    return _p25_query('#_p25_fieldNtramite')[0];
}

function _p25_fieldNmpoliza()
{
    debug('>_p25_fieldNmpoliza<');
    return _p25_query('#_p25_fieldNmpoliza')[0];
}

function _p25_turnar(status,titulo,closable)
{
    debug('>_p25_turnar:',status);
    var ventana=Ext.create('Ext.window.Window',
    {
        title        : !Ext.isEmpty(titulo) ? titulo : 'Turnar tr&aacute;mite'
        ,width       : 500
        ,height      : 330
        ,modal       : true
        ,closable    : closable==undefined ? true : closable
        ,items       :
        [
            {
                xtype       : 'textarea'
                ,labelAlign : 'top'
                ,fieldLabel : 'Comentarios'
                ,itemId     : '_p25_turnarCommentsItem'
                ,width      : 480
                ,height     : 200
            }
            ,{
                xtype       : 'radiogroup'
                ,fieldLabel : 'Mostrar al agente'
                ,columns    : 2
                ,width      : 250
                ,style      : 'margin:5px;'
                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                ,items      :
                [
                    {
                        boxLabel    : 'Si'
                        ,itemId     : 'SWAGENTE'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'S'
                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                    }
                    ,{
                        boxLabel    : 'No'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'N'
                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                    }
                ]
            }
        ]
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Aceptar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function(button)
                {
                    ventana.setLoading(true);
                    Ext.Ajax.request(
                    {
                        url     : _p25_urlActualizarStatus
                        ,params :
                        {
                            'smap1.status'    : status
                            ,'smap1.ntramite' : _p25_ntramite ? _p25_ntramite : _p25_ntramiteVacio
                            ,'smap1.comments' : Ext.ComponentQuery.query('#_p25_turnarCommentsItem')[0].getValue()
                            ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
                        }
                        ,success : function(response)
                        {
                            ventana.setLoading(false);
                            var json=Ext.decode(response.responseText);
                            debug('json response:',json);
                            if(json.success)
                            {
                                status = json.smap1.status;
                                ventana.setLoading(true);
                                
                                //se llaman los documentos, en Java se condiciona si se generan o no
                                Ext.Ajax.request(
			                    {
			                        url     : _p25_guardarReporteCotizacion
			                        ,params :
			                        {
			                            'smap1.cdunieco'  : _p25_smap1.cdunieco
			                            ,'smap1.cdramo'   : _p25_smap1.cdramo
			                            ,'smap1.estado'   : _p25_smap1.estado
			                            ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
			                            ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
			                            ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
			                            ,'smap1.ntramite' : _p25_smap1.ntramite
                                        ,'smap1.nGrupos'  : _p25_storeGrupos.getCount()
			                            ,'smap1.status'   : status
			                        }
			                    });
			                    
                                Ext.Ajax.request(
                                {
                                    url      : _p25_urlCargarParametros
                                    ,params  :
                                    {
                                        'smap1.parametro' : 'MENSAJE_TURNAR'
                                        ,'smap1.cdramo'   : _p25_smap1.cdramo
                                        ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
                                        ,'smap1.clave4'   : status
                                    }
                                    ,success : function(response)
                                    {
                                        ventana.setLoading(false);
                                        var json2=Ext.decode(response.responseText);
                                        debug('### json response parametro mensaje turnar:',json2);
                                        if(json2.exito)
                                        {
                                        
                                            var mensajeTurnado = json2.smap1.P1VALOR
                                                    +(!Ext.isEmpty(json.smap1.nombreUsuarioDestino)?
                                                        '<br/>El tr&aacute;mite '+_p25_smap1.ntramite+' fue asignado a '+json.smap1.nombreUsuarioDestino:
                                                        ''
                                                    );
                                            
                                            if(json.smap1.ASYNC=='S')
                                            {
                                                mensajeTurnado = 'El tr\u00e1mite '+_p25_smap1.ntramite+' qued\u00f3 en espera y ser\u00e1 procesado posteriormente';
                                            }
                                        
                                            mensajeCorrecto('Tr&aacute;mite guardado'
                                                ,mensajeTurnado
                                                ,function()
                                            {
                                                button.up().up().destroy();
                                                if(status+'x'=='19x')
                                                {
                                                    _p25_reload(null,19,_p25_smap1.nmpoliza);
                                                }
                                                else
                                                {
                                                    _p25_mesacontrol();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            mensajeWarning(json.respuesta);
                                        }
                                    }
                                    ,failure : function()
                                    {
                                        ventana.setLoading(false);
                                        errorComunicacion();
                                    }
                                });
                            }
                            else
                            {
                                mensajeError(json.mensaje);
                            }
                        }
                        ,failure : function()
                        {
                            ventana.setLoading(false);
                            errorComunicacion();
                        }
                    });
                }
            }
            ,{
                text     : 'Cancelar'
                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                ,handler : function(me)
                {
                    me.up('window').destroy();
                }
            }
        ]
    }).show();
    centrarVentanaInterna(ventana);
    debug('<_p25_turnar');
}

function _p25_reload(json,status,nmpoliza)
{
    debug('>_p25_reload params:');
    debug(json,status,nmpoliza,'dummy');
    _p25_tabpanel().setLoading(true);
    Ext.create('Ext.form.Panel').submit(
    {
        standardSubmit : true
        ,params        :
        {
            'smap1.cdunieco'  : _p25_smap1.cdunieco
            ,'smap1.cdramo'   : _p25_smap1.cdramo
            ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
            ,'smap1.estado'   : _p25_smap1.estado
            ,'smap1.nmpoliza' : Ext.isEmpty(nmpoliza) ? json.smap1.nmpoliza : nmpoliza
            ,'smap1.ntramite' : _p25_ntramite ? _p25_ntramite : _p25_ntramiteVacio
            ,'smap1.cdagente' : _fieldByName('cdagente').getValue()
            ,'smap1.status'   : Ext.isEmpty(status) ? _p25_smap1.status : status
            ,'smap1.cdtipsup' : null
            ,'smap1.nmpolant' : null
        }
    });
    debug('<_p25_reload');
}

function _p25_mesacontrol(json)
{
    _mask('Redireccionando...');
    Ext.create('Ext.form.Panel').submit(
    {
        standardSubmit : true
        ,url           : _p25_urlMesaControl
        ,params        :
        {
            'params.AGRUPAMC' : 'PRINCIPAL'
        }
    });
}

function _p25_imprimir()
{
    debug('>_p25_imprimir');
    var urlRequestImpCotiza = _p25_urlImprimirCotiza
            + '?p_unieco='      + _p25_smap1.cdunieco
            + '&p_ramo='        + _p25_smap1.cdramo
            + '&p_estado=W'
            + '&p_poliza='      + _p25_smap1.nmpoliza
            + '&p_suplem=0'
            + '&p_cdplan='
            + '&p_cdperpag='    + _fieldByName('cdperpag').getValue()
            + '&p_perpag='      + _fieldByName('cdperpag').getValue()
            + '&p_usuari='      + _p25_smap1.cdusuari
            + '&destype=cache'
            + "&desformat=PDF"
            + "&userid="        + _p25_reportsServerUser
            + "&ACCESSIBLE=YES"
            + "&report="        + _p25_nombreReporteCotizacion
            + "&paramform=no";
    debug('urlRequestImpCotiza:',urlRequestImpCotiza);
    var numRand = Math.floor((Math.random() * 100000) + 1);
    debug(numRand);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title          : 'Cotizaci&oacute;n'
        ,width         : 700
        ,height        : 500
        ,collapsible   : true
        ,titleCollapse : true
        ,html : '<iframe innerframe="'
                + numRand
                + '" frameborder="0" width="100" height="100"'
                + 'src="'
                + _p25_urlViewDoc
                + "?contentType=application/pdf&url="
                + encodeURIComponent(urlRequestImpCotiza)
                + "\">"
                + '</iframe>'
        ,listeners :
        {
            resize : function(win,width,height,opt)
            {
                debug(width,height);
                $('[innerframe="'+ numRand+ '"]').attr(
                {
                    'width'   : width - 20
                    ,'height' : height - 60
                });
            }
        }
    }).show());
    debug('<_p25_imprimir');
}

function _p25_imprimir2()
{
    debug('>_p25_imprimir2');
    var urlRequestImpCotiza = _p25_urlImprimirCotiza
            + '?p_unieco='      + _p25_smap1.cdunieco
            + '&p_ramo='        + _p25_smap1.cdramo
            + '&p_estado=W'
            + '&p_poliza='      + _p25_smap1.nmpoliza
            + '&p_suplem=0'
            + '&p_cdplan='
            + '&p_cdperpag='    + _fieldByName('cdperpag').getValue()
            + '&p_perpag='      + _fieldByName('cdperpag').getValue()
            + '&p_usuari='      + _p25_smap1.cdusuari
            + '&destype=cache'
            + "&desformat=PDF"
            + "&userid="        + _p25_reportsServerUser
            + "&ACCESSIBLE=YES"
            + "&report="        + _p25_nombreReporteCotizacionDetalle
            + "&paramform=no";
    debug('urlRequestImpCotiza:',urlRequestImpCotiza);
    var numRand = Math.floor((Math.random() * 100000) + 1);
    debug(numRand);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title          : 'Cotizaci&oacute;n'
        ,width         : 700
        ,height        : 500
        ,collapsible   : true
        ,titleCollapse : true
        ,html : '<iframe innerframe="'
                + numRand
                + '" frameborder="0" width="100" height="100"'
                + 'src="'
                + _p25_urlViewDoc
                + "?contentType=application/pdf&url="
                + encodeURIComponent(urlRequestImpCotiza)
                + "\">"
                + '</iframe>'
        ,listeners :
        {
            resize : function(win,width,height,opt)
            {
                debug(width,height);
                $('[innerframe="'+ numRand+ '"]').attr(
                {
                    'width'   : width - 20
                    ,'height' : height - 60
                });
            }
        }
    }).show());
    debug('<_p25_imprimir2');
}

function _p25_revisarAseguradosClic(grid,rowIndex)
{
	debug('>_p25_revisarAseguradosClic');
	var record = grid.getStore().getAt(rowIndex);
    _p25_quitarTabExtraprima(record.get('letra'));
    var timestamp = new Date().getTime();
    var cdgrupo   = record.get('letra');
    
    var pluginTabExtraprimas = [
        {
            ptype    : 'pagingselectpersist',
            pluginId : 'pagingselect'+cdgrupo
        }
    ];
    
    if (_p25_smap1.EXTRAPRIMAS_EDITAR === 'S') {
        pluginTabExtraprimas.push(Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit : 1,
            errorSummary : true,
            pluginId     : 'rowedit'+cdgrupo,
            cdgrupo      : cdgrupo,
            listeners    : {
                edit: checkEdit
            }
        }));
    }
    
    _p25_agregarTab(
    {
        title                 : 'EXTRAPRIMAS DE SUBGRUPO '+record.get('letra')
        ,itemId               : 'id'+(new Date().getTime())
        ,extraprimaLetraGrupo : record.get('letra')
        ,defaults             : { style : 'margin:5px;' }
        ,border               : 0
        ,cdgrupo              : record.get('letra')
        ,items                :
        [
            Ext.create('Ext.grid.Panel',
            {
                itemId		  :	'gridAseg'+record.get('letra')
                ,cdgrupo      : record.get('letra')
                ,selModel     : Ext.create('Ext.selection.CheckboxModel', 
                		{
                	mode              : 'MULTI',
                	showHeaderCheckbox: false,
                	cdgrupo           : cdgrupo
                	})
            	,columns     :
                [
                    {
                        text       : 'NO.'
                        ,dataIndex : 'nmsituac'
                    }
                    ,{
                        text       : 'PARENTESCO'
                        ,dataIndex : 'parentesco'
                    }
                    ,{
                        text       : 'NOMBRE'
                        ,dataIndex : 'nombre'
                    }
                    <s:if test='%{getImap().containsKey("extraprimasColumns")&&getImap().get("extraprimasColumns")!=null}'>
                        ,<s:property value="imap.extraprimasColumns" escapeHtml='false' />
                    </s:if>
                ]
                ,width      : 980
                ,height     : 500                
                ,plugins    : pluginTabExtraprimas
                ,tbar       :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '<span style="color:white;">Buscar:</span>'
                        ,timeoutFn  : ''
                        ,itemId     : 'textFieldBuscar'+record.get('letra')
                    }
                    ,{
                    	//agregar boton buscar extraprimas
                    	xtype : 'button',
                        text: 'Buscar',
                        handler : function() {
                        	var store = this.up('grid').getStore();
                        	debug('grid',store);
						    
                        	store.proxy.url = _p21_urlCargarAseguradosFiltroExtraprimas;
						    var nombreField = 'textFieldBuscar'+record.get('letra');
						    debug('nombreField', nombreField);
						    var valorFiltro = this.up('grid').down().getComponent(nombreField).getValue();
						    debug('_p21_urlCargarAseguradosFiltroExtraprimas',_p21_urlCargarAseguradosFiltroExtraprimas);
						    debug('valorFiltro',valorFiltro);
						    
						    //recargo el store
						    store.load({
								proxy:
								{
								    url         : _p21_urlCargarAseguradosFiltroExtraprimas
									
								}
								,params   :
								{
									'smap1.filtro'         :  _p21_filtroGrupoPag
									,'smap1.valorFiltro'   :  valorFiltro
								}	
								,reader      :
    	   						{
    	   							type             : 'json'
    	   							,root            : 'slist1'
    	   							,successProperty : 'success'
    	   							,messageProperty : 'respuesta'
    	   							,totalProperty   : 'total'
    	   						}
							});
                        }
                    }
                    <s:if test='%{getImap().containsKey("extraprimasColumns")&&getImap().get("extraprimasColumns")!=null}'>
                    ,{
                        xtype             : 'numberfield'
                        ,fieldLabel       : '<span style="color:white;">Extraprima ocupaci&oacute;n titulares:</span>'
                        ,name             : 'extrtitu'
                        ,grupo            : record.get('letra')
                        ,allowBlank       : false
                        ,allowDecimals    : true
                        ,decimalSeparator : '.'
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Aplicar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/bell_link.png'
                        ,grupo   : record.get('letra')
                        ,handler : _p25_guardarExtraprimasTitulares                        
                    }
                    </s:if>
                ]
                ,store      : Ext.create('Ext.data.Store',
                		{
                	model       : '_p25_modeloExtraprima'
              		,groupField : 'agrupador'
                	,autoLoad   : false
                    ,pageSize   : 10
                    ,storeId    : '_p25_storeExtraprimas'+record.get('letra')
                    ,cdgrupo    : record.get('letra')
                    ,proxy      :
                    {
                    	type         : 'ajax'
   						,url         : _p25_urlCargarAseguradosExtraprimas
   						,callbackKey : 'callback'
   						,extraParams :
   						{
   							'smap1.cdunieco'  : _p25_smap1.cdunieco
   							,'smap1.cdramo'   : _p25_smap1.cdramo
   							,'smap1.estado'   : _p25_smap1.estado
   							,'smap1.nmpoliza' : _p25_smap1.nmpoliza
   							,'smap1.nmsuplem' : '0'
   							,'smap1.cdgrupo'  : record.get('letra')
   						}
   						,reader      :
   						{
   							type             : 'json'
   							,root            : 'slist1'
   							,successProperty : 'success'
   							,messageProperty : 'respuesta'
   							,totalProperty   : 'total'
   						}
   						,simpleSortMode: true
                  },
                  listeners : {
                    	   'load' :  {
                    		   fn : function(store,records,successful) {
                    			   debug('reseteando los datos');
                    			   var selection = _fieldById('gridAseg'+store.cdgrupo).getPlugin('pagingselect'+store.cdgrupo).selection;
                    			   var mapselection = {};
                     			   for (var i = 0; i < selection.length; i++){
                     				debug('metiendo llave ',selection[i].data['nmsituac']);
                     			   	mapselection[selection[i].data['nmsituac']] = selection[i].data; 
                     			   }
                     			   debug('mapselection',mapselection);
                    			   for(var s in _fieldById('gridAseg'+store.cdgrupo).store.data.items){
                    				   var rec = _fieldById('gridAseg'+store.cdgrupo).store.getAt(s);
                    				   debug('nmsituac',rec.data['nmsituac']);
                					   if (rec.data['nmsituac'] in mapselection){
                						   debug('entro al map');
                						   var obj = mapselection[rec.data['nmsituac']];
                    					   for(var y in rec.data){
	                    					   rec.set(y, obj[y]);
                    					   }
                					   }else{
                						   for(var y in rec.data){
	                    					   debug('entro al set normal ',rec.raw[y]);
	                    					   rec.set(y,rec.raw[y]);
                    					   }
                    				   	   rec.commit();
                					   }
                    			   }
                    		  }
                  		}
                  }
                 })
                ,bbar :
                {
                    displayInfo : true
                    ,store      : Ext.getStore('_p25_storeExtraprimas'+record.get('letra'))
                    ,xtype      : 'pagingtoolbar'
                }
                ,viewConfig : viewConfigAutoSize
                ,features   :
                [
                    {
                        groupHeaderTpl :
                        [
                            '{name:this.formatName}'
                            ,{
                                formatName : function(name)
                                {
                                    return name.split("_")[1];
                                }
                            }
                        ]
                        ,ftype          : 'groupingsummary'
                        ,startCollapsed : false
                    }
                ]
                ,listeners   :
                {
                    afterrender : function(me)
                    {                       
                        debug('despues de editar registro');
                        Ext.getStore('_p25_storeExtraprimas'+record.get('letra')).sort('nmsituac','ASC');
                    }
                	,beforedeselect: beforedesel
                	,beforeedit	   : beforeed
                	,canceledit		: function(me){
                		_fieldById('btnguardar'+record.get('letra')).enable();
                	}
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,itemId  : 'btnguardar'+cdgrupo
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,hidden  : _p25_smap1.EXTRAPRIMAS_EDITAR=='N'
                        ,handler : function()
                        {
                            _p25_guardarExtraprimas(cdgrupo);
                            _p25_setActiveResumen();
                            _fieldById('gridAseg'+cdgrupo).store.commitChanges();
                        }
                    }
                ]
            })
        ]
    });
    _fieldById('gridAseg'+record.get('letra')).store.loadPage(1);
    debug('sale de <_p25_revisarAseguradosClic');
}

function _p25_quitarTabExtraprima(letra)
{
    debug('>_p25_quitarTabExtraprima letra:',letra);
    var tabsExtraprima = Ext.ComponentQuery.query('[extraprimaLetraGrupo='+letra+']');
    if(tabsExtraprima.length>0)
    {
        for(var i=0;i<tabsExtraprima.length;i++)
        {
            tabsExtraprima[i].destroy();
        }
    }
    debug('<_p25_quitarTabExtraprima');
}



function _p25_subirDetallePersonas()
{
    debug('>_p25_subirDetallePersonas');
    
    var form = _p25_tabConcepto().down('[xtype=form]');
    
    var valido=form.isValid();
    if(!valido)
    {
        mensajeWarning('Verificar datos del contratante');
    }
    
    if(valido)
    {
        var data =
        {
            smap1 : form.getValues()
        };
        
        debug(">>>>>>>>> DATA <<<<<<<<<",data);
        
        data.smap1['cdunieco'] = _p25_smap1.cdunieco;
        data.smap1['cdramo']   = _p25_smap1.cdramo;
        data.smap1['estado']   = _p25_smap1.estado;
        data.smap1['nmpoliza'] = _p25_smap1.nmpoliza;
        data.smap1['nmsuplem'] = Ext.isEmpty(_p25_smap1.nmsuplem)?'0':_p25_smap1.nmsuplem;
        
        data.smap1['confirmaEmision'] = 'N';
        data.smap1['nmorddom'] = nmorddomProspecto;
        
        debug(">>>>>>>>> DATA final<<<<<<<<<",data);
        
        form.setLoading(true);
        Ext.Ajax.request(
        {
            url       : _p25_urlGuardarContratanteColectivo
            ,jsonData : data
            ,success  : function(response)
            {
                form.setLoading(false);
                var json = Ext.decode(response.responseText);
                if(json.exito)
                {
                	if(Ext.isEmpty(_fieldByName('cdperson',_p_25_panelPrincipal).getValue())){
	            		_fieldByName('cdperson',_p_25_panelPrincipal).setValue(json.smap1.cdperson);
	            	}
	            	
                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                    {
                        title   : 'Cargar archivo de personas'
                        ,width  : 400
                        ,modal  : true
                        ,items  :
                        [
                            Ext.create('Ext.form.Panel',
                            {
                                url          : _p25_urlSubirCenso
                                ,items       :
                                [
                                    {
                                        xtype       : 'filefield'
                                        ,fieldLabel : 'Archivo'
                                        ,buttonText : 'Examinar...'
                                        ,buttonOnly : false
                                        ,name       : 'censo'
                                        ,labelAlign : 'top'
                                        ,width      : 330
                                        ,style      : 'margin:5px;'
                                        ,allowBlank : false
                                        ,msgTarget  : 'side'
                                        ,cAccept    : ['xls','xlsx']
                                        ,listeners  :
                                        {
                                            change : function(me)
                                            {
                                                //alert('4');
                                                var indexofPeriod = me.getValue().lastIndexOf("."),
                                                uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                                if (!Ext.Array.contains(this.cAccept, uploadedExtension))
                                                {
                                                    centrarVentanaInterna(Ext.MessageBox.show(
                                                    {
                                                        title   : 'Error de tipo de archivo',
                                                        msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                                        buttons : Ext.Msg.OK,
                                                        icon    : Ext.Msg.WARNING
                                                    }));
                                                    me.reset();
                                                }
                                            }
                                        }
                                    }
                                ]
                                ,buttonAlign : 'center'
                                ,buttons     :
                                [
                                    {
                                        text     : 'Cargar archivo'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/group_edit.png'
                                        ,handler : function(me) { _p25_subirArchivoCompleto(me); }
                                    }
                                ]
                            })
                        ]
                    }).show());
                }
                else
                {
                    mensajeWarning(json.respuesta);
                }
            }
            ,failure  : function()
            {
                form.setLoading(false);
                errorComunicacion();
            }
        });
    }
    debug('<_p25_subirDetallePersonas');
}

function _p25_emitir()
{
    _p25_generarTramiteClic(_p25_generarVentanaVistaPrevia);
}

function _p25_tbloqueo(closable,callback,retry)
{
    Ext.Ajax.request(
    {
        url     : _p25_urlRecuperacionSimple
        ,params :
        {
            'smap1.procedimiento' : 'RECUPERAR_CONTEO_BLOQUEO'
            ,'smap1.cdunieco'     : _p25_smap1.cdunieco
            ,'smap1.cdramo'       : _p25_smap1.cdramo
            ,'smap1.estado'       : _p25_smap1.estado
            ,'smap1.nmpoliza'     : _p25_smap1.nmpoliza
        }
        ,success : function(response)
        {
            var ck = 'Decodificando conteo de bloqueos';
            try
            {
                var json=Ext.decode(response.responseText);
                debug('### conteo bloqueo:',json);
                if(json.exito)
                {
                    if(Number(json.smap1.CONTEO)>0)
                    {
                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                        {
                            title     : 'Cotizaci\u00F3n en proceso...'
                            ,width    : 400
                            ,height   : 150
                            ,modal    : true
                            ,closable : false
                            ,html     : '<div style="padding:5px;border:0px solid black;">'
                                        +'Su cotizaci\u00F3n a\u00FAn se encuentra generando tarifa ('
                                        +json.smap1.CONTEO
                                        +').<br/>Intente m\u00E1s tarde</div>'
                            ,buttonAlign : 'center'
                            ,buttons     :
                            [
                                {
                                    text     : 'Regresar'
                                    ,icon    : '${icons}arrow_undo.png'
                                    ,handler : function () {
                                        if (!Ext.isEmpty(_cotcol_flujo) && _cotcol_flujo.aux === 'RSTN') {
                                            _mask();
                                            location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?empty';
                                            return;
                                        }
                                        _p25_mesacontrol();
                                    }
                                    ,hidden  : closable
                                }
                                ,{
                                    text     : 'Recargar'
                                    ,icon    : '${icons}arrow_refresh.png'
                                    ,handler : function(me){ me.up('window').setLoading(true); location.reload(); }
                                    ,hidden  : closable
                                }
                                ,{
                                    text     : 'Reintentar'
                                    ,icon    : '${icons}control_repeat_blue.png'
                                    ,handler : function(me){ me.up('window').destroy(); retry(); }
                                    ,hidden  : !(closable&&!Ext.isEmpty(retry))
                                }
                                ,{
                                    text     : 'Consultar m\u00e1s tarde'
                                    ,icon    : '${icons}accept.png'
                                    ,hidden  : !closable
                                    ,handler : function(me)
                                    {
                                        var ck = 'Marcando como pendiente de vista previa';
                                        try
                                        {
                                            _mask(ck);
                                            Ext.Ajax.request(
                                            {
                                                url      : _p25_urlMarcarTramitePendienteVistaPrevia
                                                ,params  :
                                                {
                                                    'params.ntramite' : _p25_ntramite
                                                }
                                                ,success : function(response)
                                                {
                                                    _unmask();
                                                    var ck = 'Decodificando respuesta al marcar como pendiente de vista previa';
                                                    try
                                                    {
                                                        var json = Ext.decode(response.responseText);
                                                        debug('### marcar swvispre:',json);
                                                        if(json.success==true)
                                                        {
                                                            if (!Ext.isEmpty(_cotcol_flujo) && _cotcol_flujo.aux === 'RSTN') {
                                                                mensajeCorrecto(
                                                                    'Tr\u00e1mite en espera',
                                                                    'El tr\u00e1imte podr\u00e1 ser consultado posteriormente',
                                                                    function () {
                                                                        _mask();
                                                                        location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?empty';
                                                                    }
                                                                );
                                                                return;
                                                            }
                                                            _p25_mesacontrol();
                                                        }
                                                        else
                                                        {
                                                            mensajeError(json.message);
                                                        }
                                                    }
                                                    catch(e)
                                                    {
                                                        manejaException(e,ck);
                                                    }
                                                }
                                                ,failure : function()
                                                {
                                                    _unmask();
                                                    errorComunicacion(null,'Error al marcar como pendiente de vista previa');
                                                }
                                            });
                                        }
                                        catch(e)
                                        {
                                            manejaException(e,ck);
                                        }
                                    }
                                }
                            ]
                        }).show());
                    }
                    else
                    {
                        if(!Ext.isEmpty(callback))
                        {
                            callback();
                        }
                        else
                        {
                            _mask('Verificando vista previa');
                            Ext.Ajax.request(
                            {
                                url     : _p25_urlRecuperacion
                                ,params :
                                {
                                    'params.consulta'  : 'RECUPERAR_SWVISPRE_TRAMITE'
                                    ,'params.ntramite' : false == _p25_ntramite ? '' : _p25_ntramite
                                }
                                ,success : function(response)
                                {
                                    _unmask();
                                    var ck = 'Decodificando respuesta al verificar vista previa';
                                    try
                                    {
                                        var json = Ext.decode(response.responseText);
                                        debug('### swvispre:',json);
                                        if(json.success==true)
                                        {
                                            if(json.params.SWVISPRE=='S')
                                            {
                                                _p25_generarVentanaVistaPrevia(false);
                                            }
                                        }
                                        else
                                        {
                                            mensajeError(json.message);
                                        }
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }
                                }
                                ,failure : function()
                                {
                                    _unmask();
                                    errorComunicacion(null,'Error al verificar vista previa');
                                }
                            });
                        }
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,failure : function()
        {
            me.setLoading(false);
            errorComunicacion(null,'Error al contar bloqueos');
        }
    });
}

function _p25_generarVentanaVistaPrevia(sinBotones)
{
    _mask('Cargando...');
    setTimeout(
        function()
        {
            _unmask();
            _p25_tbloqueo(
                true
                ,function()
                {
                    _p25_generarVentanaVistaPrevia2(sinBotones);
                }
                ,function()
                {
                    _p25_generarVentanaVistaPrevia(sinBotones);
                }
            );
        }
        ,2000
    );
}

function _p25_generarVentanaVistaPrevia2(sinBotones)
{
    var itemsVistaPrevia=[];
    
    var mostrarVentana= function()
    {
        var ventana = Ext.create('Ext.window.Window',
        {
            title     : 'Vista previa'
            ,width    : 800
            ,height   : 400
            ,closable : !Ext.isEmpty(sinBotones)&&sinBotones==true
            ,modal    : true
            ,items    :
            [
                Ext.create('Ext.tab.Panel',
                {
                    height : 300
                    ,items : itemsVistaPrevia
                })
            ]
            ,buttonAlign : 'center'
            ,buttons     :
            [
                {
                    text     : 'Vista previa'
                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                    ,hidden  : !Ext.isEmpty(sinBotones)&&sinBotones==true
                    ,handler : function(){_p25_imprimir2();}
                }
                ,{
                    text     : 'Emitir'
                    ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                    ,hidden  : !Ext.isEmpty(sinBotones)&&sinBotones==true
                    ,handler : function(){_p25_emitir2(ventana,this);}
                }
                ,{
                    text     : 'Cancelar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                    ,hidden  : !Ext.isEmpty(sinBotones)&&sinBotones==true
                    ,handler : function(){ventana.destroy();}
                }
            ]
        });
        centrarVentanaInterna(ventana.show());
    }
    
    var rendererVP = function(val,md,rec)
    {
        if(rec.get('concepto')=='TOTAL DE HOMBRES'
           ||rec.get('concepto')=='TOTAL DE MUJERES')
        {
            return val;
        }
        else
        {
            return Ext.util.Format.usMoney(val);
        }
    };
    
    itemsVistaPrevia.push(
    Ext.create('Ext.grid.Panel',
    {
        title    : 'CONCEPTOS GLOBALES'
        ,itemId  : '_p25_gridConceptosGlobales'
        ,stores  : Ext.create('Ext.data.Store',
        {
            model : '_p25_vpModelo'
        })
        ,columns :
        [
            {
                text       : 'CONCEPTO'
                ,dataIndex : 'concepto'
                ,sortable  : false
                ,width     : 200
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 1'
                ,dataIndex : 'subgrupo1'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<1
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 2'
                ,dataIndex : 'subgrupo2'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<2
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 3'
                ,dataIndex : 'subgrupo3'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<3
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 4'
                ,dataIndex : 'subgrupo4'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<4
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 5'
                ,dataIndex : 'subgrupo5'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<5
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 6'
                ,dataIndex : 'subgrupo6'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<6
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 7'
                ,dataIndex : 'subgrupo7'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<7
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 8'
                ,dataIndex : 'subgrupo8'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<8
            }
            ,{
                text       : 'IMPORTE<br/>SUBGRUPO 9'
                ,dataIndex : 'subgrupo9'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
                ,hidden    : _p25_storeGrupos.getCount()<9
            }
            ,{
                text       : 'IMPORTE<br/>P&Oacute;LIZA'
                ,dataIndex : 'importe'
                ,sortable  : false
                ,renderer  : rendererVP
                ,width     : 140
            }
        ]
        ,listeners :
        {
            afterrender : function(me)
            {
                Ext.Ajax.request(
                {
                    url     : _p25_urlCargarConceptosGlobales
                    ,params :
                    {
                        'smap1.cdunieco'  : _p25_smap1.cdunieco
                        ,'smap1.cdramo'   : _p25_smap1.cdramo
                        ,'smap1.estado'   : _p25_smap1.estado
                        ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                        ,'smap1.nmsuplem' : '0'
                        ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### obtener conceptos globales:',json);
                        if(json.exito)
                        {
                            me.getStore().removeAll();
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'PRIMA NETA'
                                ,importe : json.smap1.PRIMA_NETA
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'DERECHOS DE POLIZA'
                                ,importe : json.smap1.DERPOL
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'RECARGOS'
                                ,importe : json.smap1.RECARGOS
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'IVA'
                                ,importe : json.smap1.IVA
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'TOTAL DE 4 CONCEPTOS'
                                ,importe : 0
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'TOTAL DE HOMBRES'
                                ,importe : 0
                            }));
                            me.getStore().add(new _p25_vpModelo(
                            {
                                concepto : 'TOTAL DE MUJERES'
                                ,importe : 0
                            }));
                            
                            _p25_storeGrupos.each(function(record)
						    {
						        itemsVistaPrevia.push(
						        Ext.create('Ext.grid.Panel',
						        {
						            title      : 'TARIFA SUBGRUPO '+record.get('letra')
						            ,minHeight : 100
						            ,maxHeight : 250
						            ,store     : Ext.create('Ext.data.Store',
						            {
						                model     : '_p25_modeloTarifaEdad'
						                ,grupo    : record.get('letra')
						                ,autoLoad : true
						                ,proxy    :
						                {
						                    type         : 'ajax'
						                    ,timeout     : 1000*60*2
						                    ,extraParams :
						                    {
						                        'smap1.cdunieco'  : _p25_smap1.cdunieco
						                        ,'smap1.cdramo'   : _p25_smap1.cdramo
						                        ,'smap1.estado'   : _p25_smap1.estado
						                        ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
						                        ,'smap1.nmsuplem' : '0'
						                        ,'smap1.cdplan'   : record.get('cdplan')
						                        ,'smap1.cdgrupo'  : record.get('letra')
						                        ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
						                    }
						                    ,url         : _p25_urlObtenerTarifaEdad
						                    ,reader      :
						                    {
						                        type  : 'json'
						                        ,root : 'slist1'
						                    }
						                }
						                ,listeners :
						                {
						                    load : function(me,records,success)
						                    {
						                        if(success)
						                        {
						                            var prima  = 0;
						                            var derpol = 0;
						                            var recar  = 0;
						                            var iva    = 0;
						                            var hom    = 0;
						                            var muj    = 0;
						                            var pTot   = 0;
						                            
						                            for(var ij in records)
						                            {
						                                var primaPaso  = Number(records[ij].get('TARIFA_TOTAL_HOMBRES')) + Number(records[ij].get('TARIFA_TOTAL_MUJERES'));
						                                var derpolPaso = Number(records[ij].get('DERPOL_TOTAL_GENERAL'));
						                                var recarPaso  = Number(records[ij].get('RECARGOS_TOTAL_GENERAL'));
						                                var ivaPaso    = Number(records[ij].get('IVA_TOTAL_GENERAL'));
						                                
						                                prima  += primaPaso;
						                                derpol += derpolPaso;
						                                recar  += recarPaso;
						                                iva    += ivaPaso;
						                                
						                                pTot   += primaPaso+derpolPaso+recarPaso+ivaPaso;
						                                
						                                hom    += Number(records[ij].get('HOMBRES'));
						                                muj    += Number(records[ij].get('MUJERES'));
						                            }
						                            
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(0).set('subgrupo'+me.grupo , prima);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(1).set('subgrupo'+me.grupo , derpol);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(2).set('subgrupo'+me.grupo , recar);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(3).set('subgrupo'+me.grupo , iva);
						                            
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(4).set('subgrupo'+me.grupo , pTot);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(4).set('importe',
						                                Number(_fieldById('_p25_gridConceptosGlobales').store.getAt(4).get('importe'))+pTot);
						                                
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(5).set('subgrupo'+me.grupo , hom);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(5).set('importe',
						                                Number(_fieldById('_p25_gridConceptosGlobales').store.getAt(5).get('importe'))+hom);
						                            
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(6).set('subgrupo'+me.grupo , muj);
						                            _fieldById('_p25_gridConceptosGlobales').store.getAt(6).set('importe',
						                                Number(_fieldById('_p25_gridConceptosGlobales').store.getAt(6).get('importe'))+muj);
						                            
						                            _fieldById('_p25_gridConceptosGlobales').store.commitChanges();
						                        }
						                    }
						                }
						            })
						            ,columns   :
						            [
						                {
						                    header     : 'Edad'
						                    ,width     : 60
						                    ,dataIndex : 'EDAD'
						                }
						                ,{
						                    header     : 'No. Hombres'
						                    ,width     : 100
						                    ,dataIndex : 'HOMBRES'
						                }
						                ,{
						                    header     : 'No. Mujeres'
						                    ,width     : 100
						                    ,dataIndex : 'MUJERES'
						                }
						                ,{
						                    header     : 'Tarifa por hombre'
						                    ,flex      : 1
						                    ,dataIndex : 'TARIFA_UNICA_HOMBRES'
						                    ,renderer  : Ext.util.Format.usMoney
						                }
						                ,{
						                    header     : 'Tarifa por mujer'
						                    ,flex      : 1
						                    ,dataIndex : 'TARIFA_UNICA_MUJERES'
						                    ,renderer  : Ext.util.Format.usMoney
						                }
						                ,{
						                    header     : 'Total hombres'
						                    ,flex      : 1
						                    ,dataIndex : 'TARIFA_TOTAL_HOMBRES'
						                    ,renderer  : Ext.util.Format.usMoney
						                }
						                ,{
						                    header     : 'Total mujeres'
						                    ,flex      : 1
						                    ,dataIndex : 'TARIFA_TOTAL_MUJERES'
						                    ,renderer  : Ext.util.Format.usMoney
						                }
						            ]
						        })
						        );
						    });
                            
                        }
                        else
                        {
                            mensajeError(json.respuesta);
                        }
                    }
                    ,failure : errorComunicacion
                });
            }
        }
    }));
    
    mostrarVentana();
}

function _cotcol_aseguradosClic(gridSubgrupo,rowIndexSubgrupo)
{
    var record = gridSubgrupo.getStore().getAt(rowIndexSubgrupo);
    debug('>_p25_aseguradosClic record:',record);
    _p25_quitarTabAsegurados(record.get('letra'));
    var columnas =
    [
        <s:property value='%{getImap().containsKey("aseguradosColumns")?getImap().get("aseguradosColumns").toString():""}' escapeHtml='false' />
    ];
    if(_p25_smap1.ASEGURADOS_EDITAR=='S')
    {
        columnas.push(
        {
            xtype         : 'actioncolumn'
            ,sortable     : false
            ,menuDisabled : true
            ,items        :
            [
                {
                    tooltip  : 'Quitar asegurado'
                    ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                    ,handler : _cotcol_quitarAsegurado
                }
                ,{
                    tooltip  : 'Recuperar por RFC'
                    ,icon    : '${ctx}/resources/fam3icons/icons/vcard_edit.png'
                    ,handler : _p25_recuperarAsegurado
                }
                ,{
                    tooltip  : 'Editar datos b\u00E1sicos de persona'
                    ,icon    : '${icons}user_edit.png'
                    ,handler : _cotcol_editarDatosBaseAsegurado
                }
                ,{
                    tooltip  : 'Editar datos b\u00E1sicos de inciso'
                    ,icon    : '${icons}tag_red.png'
                    ,handler : _cotcol_mostrarVentanaActTvalosit
                }
                ,{
                    tooltip  : 'Editar persona/domicilio'
                    ,icon    : '${icons}report_key.png'
                    ,handler : _p25_editarAsegurado
                }
                ,{
                    tooltip  : 'Editar coberturas'
                    ,icon    : '${ctx}/resources/fam3icons/icons/text_list_bullets.png'
                    ,handler : _p25_editarCoberturas
                }
                ,{
                    tooltip  : 'Editar exclusiones'
                    ,icon    : '${ctx}/resources/fam3icons/icons/lock.png'
                    ,handler : _p25_editarExclusiones
                }
            ]
        });
    }
    
    var pluginsTabAsegurados = [
        {
            ptype    : 'pagingselectpersist',
            pluginId : 'pagingselectasegurados'+record.get('letra')
        }
    ];
    
    if (_p25_smap1.ASEGURADOS_EDITAR === 'S') {
        pluginsTabAsegurados.push(Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit : 1,
            errorSummary : false
        }));
    }
    
    _p25_agregarTab(
    {
        title                 : 'ASEGURADOS DE SUBGRUPO '+record.get('letra')
        ,itemId               : 'id'+(new Date().getTime())
        ,aseguradosLetraGrupo : record.get('letra')
        ,defaults             : { style : 'margin:5px;' }
        ,border               : 0
        ,items                :
        [
            Ext.create('Ext.grid.Panel',
            {
            	itemId			: 'gridAsegurados'+record.get('letra')
            	,columns    : columnas
                ,width      : 980
                ,height     : 500
                ,plugins    : pluginsTabAsegurados
                ,tbar       :
                [
                    {
                        xtype       : 'textfield'
	                    ,itemId     : 'textFieldBuscar'+record.get('letra') 
                        ,fieldLabel : '<span style="color:white;">Buscar:</span>'
                        ,timeoutFn  : ''
                    },
                    {
                    	//agregar boton buscar
                    	xtype : 'button',
                        text: 'Buscar',
                        handler : function() {
                        	var store = this.up('grid').getStore();
                        	debug('grid',store);
						    
                        	store.proxy.url = _p21_urlCargarAseguradosFiltroGrupoPag;
						    var nombreField = 'textFieldBuscar'+record.get('letra');
						    debug('nombreField', nombreField);
						    var valorFiltro = this.up('grid').down().getComponent(nombreField).getValue();
						    
						    debug('_p21_filtroGrupoPag',_p21_filtroGrupoPag);
						    debug('valorFiltro',valorFiltro);
						    
						    //recargo el store
						    store.load({
								proxy:
								{
								    url         : _p21_urlCargarAseguradosFiltroGrupoPag
									
								}
								,params   :
								{
									'smap1.filtro'         :  _p21_filtroGrupoPag
									,'smap1.valorFiltro'   :  valorFiltro
								}	
								,reader      :
    	   						{
    	   							type             : 'json'
    	   							,root            : 'slist1'
    	   							,successProperty : 'success'
    	   							,messageProperty : 'respuesta'
    	   							,totalProperty   : 'total'
    	   						}
							});
                        }
                    }
                ]
                ,store      : Ext.create('Ext.data.Store',
                {
                    model             : '_p25_modeloAsegurados'
                    ,groupField       : 'AGRUPADOR'
                    ,autoLoad         : false
                    ,pageSize         : 10
                    ,storeId          : '_p25_storeAsegurados'+record.get('letra')
                    ,gridSubgrupo     : gridSubgrupo
                    ,rowIndexSubgrupo : rowIndexSubgrupo
                    ,proxy            :
                    {
                        type         : 'ajax'
	   						/* ,url         : _p21_urlCargarAseguradosGrupo */
	   						,url         : _p25_urlCargarAseguradosGrupoPag
	   						,callbackKey : 'callback'
	   						,extraParams :
	   						{
	   							'smap1.cdunieco'  : _p25_smap1.cdunieco
	   							,'smap1.cdramo'   : _p25_smap1.cdramo
	   							,'smap1.estado'   : _p25_smap1.estado
	   							,'smap1.nmpoliza' : _p25_smap1.nmpoliza
	   							,'smap1.nmsuplem' : '0'
	   							,'smap1.cdgrupo'  : record.get('letra')
	   						}
	   						,reader      :
	   						{
	   							type             : 'json'
	   							,root            : 'slist1'
	   							,successProperty : 'success'
	   							,messageProperty : 'respuesta'
	   							,totalProperty   : 'total'
	   						}
	   						,simpleSortMode: true
                    }
                })
                ,bbar :
                {
                    displayInfo : true
                    ,store      : Ext.getStore('_p25_storeAsegurados'+record.get('letra'))
                    ,xtype      : 'pagingtoolbar'
                }
                ,viewConfig : viewConfigAutoSize
                ,features   :
                [
                    {
                        groupHeaderTpl :
                        [
                            '{name:this.formatName}'
                            ,{
                                formatName : function(name)
                                {
                                    return name.split("_")[1];
                                }
                            }
                        ]
                        ,ftype          : 'groupingsummary'
                        ,startCollapsed : false
                    }
                ]
                ,listeners :
                {
                    afterrender : function(me)
                    {
/*                         cargaStorePaginadoLocalFiltro(
                            Ext.getStore('_p25_storeAsegurados'+record.get('letra'))
                            ,_p25_urlCargarAseguradosGrupo
                            ,'slist1'
                            ,{
                                'smap1.cdunieco'  : _p25_smap1.cdunieco
                                ,'smap1.cdramo'   : _p25_smap1.cdramo
                                ,'smap1.estado'   : _p25_smap1.estado
                                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                                ,'smap1.nmsuplem' : '0'
                                ,'smap1.cdgrupo'  : record.get('letra')
                            }
                            ,null
                            ,me
                            ,null
                            ,null
                        ); */
                        
                        Ext.getStore('_p25_storeAsegurados'+record.get('letra')).sort('NMSITUAC','ASC');
                    }
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,hidden  : _p25_smap1.ASEGURADOS_EDITAR!='S'
                        ,handler : function()
                        {
                            _p25_guardarAsegurados(this.up().up());
                        }
                    }
                ]
            })
        ]
    });
    _fieldById('gridAsegurados'+record.get('letra')).store.loadPage(1);
    debug('<_cotcol_aseguradosClic');
}

function _p25_quitarTabAsegurados(letra)
{
    debug('>_p25_quitarTabAsegurados letra:',letra);
    var tabsAsegurados = Ext.ComponentQuery.query('[aseguradosLetraGrupo='+letra+']');
    if(tabsAsegurados.length>0)
    {
        for(var i=0;i<tabsAsegurados.length;i++)
        {
            tabsAsegurados[i].destroy();
        }
    }
    debug('<_p25_quitarTabAsegurados');
}

function _p25_recuperarAsegurado(grid,rowIndex)
{
    var record=grid.getStore().getAt(rowIndex);
    debug('>_p25_recuperarAsegurado:',record.data);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title     : 'Recuperar asegurado'
        ,width    : 600
        ,height   : 370
        ,modal    : true
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                border    : 0
                ,layout   : 'hbox'
                ,defaults : { style : 'margin:5px;' }
                ,items    :
                [
                    {
                        xtype       : 'textfield'
                        ,name       : 'rfc'
                        ,fieldLabel : 'RFC'
                        ,allowBlank : false
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function(button)
                        {
                            var form=button.up('[xtype=form]');
                            var valido = true;
                            
                            if(valido)
                            {
                                valido = form.isValid();
                                if(!valido)
                                {
                                    datosIncompletos();
                                }
                            }
                            
                            if(valido)
                            {
                                cargaStorePaginadoLocal(form.up('[xtype=window]').down('[xtype=grid]').getStore()
                                    ,_p25_urlRecuperarPersona
                                    ,'slist1'
                                    ,{
                                        'map1.pv_rfc_i' : form.down('[name=rfc]').getValue()
                                    });
                            }
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                height      : 250
                ,viewConfig : viewConfigAutoSize
                ,columns    :
                [
                    {
                        xtype         : 'actioncolumn'
                        ,width        : 20
                        ,menuDisabled : true
                        ,sortable     : false
                        ,items        :
                        [
                            {
                                tooltip  : 'Recuperar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                ,handler : function(grid2,row)
                                {
                                    grid2.up('[xtype=window]').setLoading(true);
                                    var record2=grid2.getStore().getAt(row);
                                    debug('recuperar a:',record2.data);
                                    record.set('RFC',record2.get('RFCCLI'));
                                    record.set('NOMBRE',record2.get('NOMBRE'));
                                    record.set('SEGUNDO_NOMBRE',record2.get('SNOMBRE'));
                                    record.set('APELLIDO_PATERNO',record2.get('APPAT'));
                                    record.set('APELLIDO_MATERNO',record2.get('APMAT'));
                                    record.set('CDIDEPER',record2.get('CDIDEPER'));
                                    record.set('CDPERSON',record2.get('CLAVECLI'));
                                    record.set('SWEXIPER','S');
                                    grid.refresh();
                                    grid2.up('[xtype=window]').destroy();
                                }
                            }
                        ]
                    }
                    <s:property value='%{getImap().containsKey("recuperadosColumns")?","+getImap().get("recuperadosColumns").toString():""}' />
                ]
                ,store      : Ext.create('Ext.data.Store',
                {
                    model : '_p25_modeloRecuperados'
                })
            })
        ]
    }).show());
    debug('<_p25_recuperarAsegurado');
}

/*
se mueve funcion a /js/proceso/emision/funcionesCotizacionGrupo.js
function _cotcol_editarDatosBaseAsegurado;
se mueve funcion a /js/proceso/emision/funcionesCotizacionGrupo.js
*/

function _p25_editarAsegurado(grid,rowIndex)
{
    var record=grid.getStore().getAt(rowIndex);
    debug('>_p25_editarAsegurado:',record.data);
    
    try{
    	_p22_parentCallback = false;
    	_callbackDomicilioAseg = false;
    	destruirLoaderContratante();
    	_ventanaPersonas.destroy();
    }catch(e){
    	debug('No se elimina ventana de Persona');
    }
    
    var titularComoContratante = false;
    
    if(_fieldByName('cdreppag').getValue() == '2' || _fieldByName('cdreppag').getValue() == '3'){
    	if(!Ext.isEmpty(record.get('PARENTESCO')) && 'T' == record.get('PARENTESCO')){
			titularComoContratante = true;
    	}
    }
    
    
    if(titularComoContratante){
    	_ventanaPersonas = Ext.create('Ext.window.Window',
					        {
					            title      : 'Editar persona '+record.get('NOMBRE') + ' ' +record.get('APELLIDO_PATERNO')
					            ,modal     : true
					            ,autoScroll: true
					            ,width     : 900
					            ,height    : 900
					            ,loader    : 
					            {
				                    url       : _p25_urlPantallaPersonas
				                    ,scripts  : true
				                    ,autoLoad : true
				                    ,ajaxOptions: {
				                            method: 'POST'
				                     },
				                     params: {
									                'smap1.cdperson' : record.get('CDPERSON'),
									                'smap1.cdideper' : '',
									                'smap1.cdideext' : '',
									                'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
									                'smap1.esCargaClienteNvo' : 'N' ,
									                'smap1.ocultaBusqueda' : 'S' ,
									                'smap1.cargaCP' : '',
									                'smap1.cargaTipoPersona' : '',
									                'smap1.cargaSucursalEmi' : _p25_smap1.cdunieco,
									                'smap1.activaCveFamiliar': 'N',
									                'smap1.modoRecuperaDanios': 'N',
									                'smap1.modoSoloEdicion': 'S'
									            }
				                }
					        }).show();
					        centrarVentanaInterna(_ventanaPersonas);
					        
					        _p22_parentCallback = function(json){
							        record.set('RFC'              , json.smap1.CDRFC);
							        record.set('NOMBRE'           , json.smap1.DSNOMBRE);
							        record.set('SEGUNDO_NOMBRE'   , json.smap1.DSNOMBRE1);
							        record.set('APELLIDO_PATERNO' , json.smap1.DSAPELLIDO);
							        record.set('APELLIDO_MATERNO' , json.smap1.DSAPELLIDO1);
							        record.set('APELLIDO_MATERNO' , json.smap1.DSAPELLIDO1);
							        record.set('FECHA_NACIMIENTO' , json.smap1.FENACIMI);
							        record.set('NACIONALIDAD'     , json.smap1.CDNACION);
							        
							        _ventanaPersonas.close();
							};
    }else{
    	_ventanaPersonas = Ext.create('Ext.window.Window',
			{
		        title   : 'Editar persona '+record.get('NOMBRE') + ' ' +record.get('APELLIDO_PATERNO')
		        ,width  : 450
		        ,height : 400
		        ,modal  : true
		        ,loader : {
	            url       : _p25_urlPantallaDomicilio
	            ,params   :
	            {
	                'params.cdperson' : record.get('CDPERSON')
	            }
	            ,scripts  : true
	            ,autoLoad : true
        	}
		    }).show();
		    
		centrarVentanaInterna(_ventanaPersonas);
		
		_callbackDomicilioAseg = function(){
		        _ventanaPersonas.close();
		};
    }
    
    debug('<_p25_editarAsegurado');
}

function _p25_editarCoberturas(grid,row)
{
    var record=grid.getStore().getAt(row);
    debug('>_p25_editarCoberturas record:',record.data);
    _p25_guardarAsegurados(grid,function()
    {
        centrarVentanaInterna(Ext.create('Ext.window.Window',
        {
            title   : 'Editar coberturas de '+record.get('NOMBRE')
            ,width  : 900
            ,height : 500
            ,modal  : true
            ,loader :
            {
                url       : _p25_urlEditarCoberturas
                ,params   :
                {
                    'smap1.pv_cdunieco'  : _p25_smap1.cdunieco
                    ,'smap1.pv_cdramo'   : _p25_smap1.cdramo
                    ,'smap1.pv_estado'   : _p25_smap1.estado
                    ,'smap1.pv_nmpoliza' : _p25_smap1.nmpoliza
                    ,'smap1.pv_nmsituac' : record.get('NMSITUAC')
                    ,'smap1.pv_cdperson' : record.get('CDPERSON')
                    ,'smap1.pv_cdtipsit' : _p25_smap1.cdtipsit
                }
                ,scripts  : true
                ,autoLoad : true
            }
        }).show());
    });
    debug('<_p25_editarCoberturas');
}

function _p25_editarExclusiones(grid,row)
{
    var record=grid.getStore().getAt(row);
    debug('>_p25_editarExclusiones record:',record.data);
    _p25_guardarAsegurados(grid,function()
    {
        var ventana=Ext.create('Ext.window.Window',
        {
            title   : 'Editar exclusiones de '+(record.get('NOMBRE')+' '+(record.get('SEGUNDO_NOMBRE')?record.get('SEGUNDO_NOMBRE')+' ':' ')+record.get('APELLIDO_PATERNO')+' '+record.get('APELLIDO_MATERNO'))
            ,width  : 900
            ,height : 500
            ,modal  : true
            ,loader :
            {
                url       : _p25_urlEditarExclusiones
                ,params   :
                {
                    'smap1.pv_cdunieco'      : _p25_smap1.cdunieco
                    ,'smap1.pv_cdramo'       : _p25_smap1.cdramo
                    ,'smap1.pv_estado'       : _p25_smap1.estado
                    ,'smap1.pv_nmpoliza'     : _p25_smap1.nmpoliza
                    ,'smap1.pv_nmsituac'     : record.get('NMSITUAC')
                    ,'smap1.pv_nmsuplem'     : '0'
                    ,'smap1.pv_cdperson'     : record.get('CDPERSON')
                    ,'smap1.pv_cdrol'        : record.get('CDROL')
                    ,'smap1.nombreAsegurado' : record.get('NOMBRE')+' '+(record.get('SEGUNDO_NOMBRE')?record.get('SEGUNDO_NOMBRE')+' ':' ')+record.get('APELLIDO_PATERNO')+' '+record.get('APELLIDO_MATERNO')
                    ,'smap1.cdrfc'           : record.get('RFC')
                }
                ,scripts  : true
                ,autoLoad : true
            }
        }).show()
        centrarVentanaInterna(ventana);
        expande = function()
        {
            ventana.destroy();
        }
    });
    debug('<_p25_editarExclusiones');
}

function _p25_guardarAsegurados(grid,callback)
{
    debug('>_p25_guardarAsegurados grid:',grid);
    debug('selection model', grid.getSelectionModel());
    var store=grid.getStore();
    
    var valido = true;
    
    /*
    if(valido)
    {
        var error = '';
        $.each(store.datos,function(i,record)
        {
            if(Ext.isEmpty(record.get('NOMBRE')))
            {
                valido = false;
                error = error + 'Faltan los datos de la situaci&oacute;n '+record.get('NMSITUAC')+'<BR/>';
            }
        });
        if(!valido)
        {
            mensajeError(error);
        }
    }
    */
    
    var asegurados = [];
    
    if(valido)
    {
        for(var i in store.datos)
        {
            var record = store.datos[i];
            if(record.dirty)
            {
                asegurados.push(record);
            }
        }
        
        valido     = asegurados.length>0||callback!=undefined;
        if(!valido)
        {
            mensajeWarning('No hay cambios');
            _p25_setActiveResumen();
        }
    }
    
    debug('asegurados:',asegurados);
    
    if(valido&&asegurados.length>0)
    {
        debug('guardar:',asegurados);
        var slist1 = [];
        $.each(store.datos,function(i,irecord)
        {
            debug('iterando para guardar:',irecord);
            slist1.push(
            {
                nmsituac  : irecord.get('NMSITUAC')
                ,cdrol    : irecord.get('CDROL')
                ,cdperson : irecord.get('CDPERSON')
                ,swexiper : Ext.isEmpty(irecord.get('SWEXIPER'))?'N':irecord.get('SWEXIPER')
                ,cdideper : irecord.get('CDIDEPER')
            });
        });
        debug('slist1:',slist1);
        grid.setLoading(true);
        
/*         Ext.Ajax.request(
        {
            url       : _p25_urlGuardarAsegurados
            ,jsonData :
            {
                slist1 : slist1
                ,smap1 :
                {
                    cdunieco  : _p25_smap1.cdunieco
                    ,cdramo   : _p25_smap1.cdramo
                    ,cdtipsit : _p25_smap1.cdtipsit
                    ,estado   : _p25_smap1.estado
                    ,nmpoliza : _p25_smap1.nmpoliza
                    ,cdgrupo  : grid.up('[aseguradosLetraGrupo]').aseguradosLetraGrupo
                }
            }
            ,success : function(response)
            {
                grid.setLoading(false);
                var json=Ext.decode(response.responseText);
                debug('json response guardar asegurados:',json);
                if(json.exito)
                {
                    for(var i=0;i<asegurados.length;i++)
                    {
                        asegurados[i].commit();
                    }
                    if(callback!=undefined)
                    {
                        callback();
                    }
                    else
                    {
                        mensajeCorrecto('Datos guardados',json.respuesta);
                        _p25_setActiveResumen();
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                grid.setLoading(false);
            }
        }); */
    }
    
    if(valido&&asegurados.length==0)
    {
        callback();
    }
    
    debug('<_p25_guardarAsegurados');
}

function _p25_emitir2(ventana,button)
{
    var params =
    {
        'smap1.cdunieco'  : _p25_smap1.cdunieco
        ,'smap1.cdramo'   : _p25_smap1.cdramo
        ,'smap1.cdtipsit' : _p25_smap1.cdtipsit
        ,'smap1.estado'   : _p25_smap1.estado
        ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
        ,'smap1.cdperpag' : _fieldByName('cdperpag').getValue()
        ,'smap1.feini'    : Ext.Date.format(_fieldByName('feini').getValue(),'d/m/Y')
        ,'smap1.fefin'    : Ext.Date.format(_fieldByName('fefin').getValue(),'d/m/Y')
        ,'smap1.ntramite' : _p25_ntramite
        ,'smap1.caseIdRstn' : _NVL(_p25_smap1.caseIdRstn)
    };
    debug('parametros para emitir:',params);
    ventana.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p25_urlEmitir
        ,params  : params
        ,success : function(response)
        {
            ventana.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('json response emitir:',json);
            if(json.exito)
            {
                if ((!Ext.isEmpty(_cotcol_flujo)) && 'RSTN' === _cotcol_flujo.aux) {
                    mensajeCorrecto(
                        'P\u00f3liza emitida',
                        'Se ha emitido la p\u00f3liza ' + json.smap1.nmpoliexEmi,
                        function () {
                            _mask();
                            location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?nmpoliex=' + json.smap1.nmpoliexEmi;
                        }
                    );
                    return;
                }
                
                button.hide();
                button.up().down('[text=Cancelar]').hide();
                button.up().add(
                [
                    {
                        xtype  : 'displayfield'
                        ,value : 'P&Oacute;LIZA '+json.smap1.nmpoliexEmi
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Documentos'
                        ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                        ,handler : function(me)
                        {
                            mensajeWarning('Los documentos se est\u00e1n generando, pueden consultarse m\u00e1s tarde desde mesa de control cuando el tr\u00e1mite se encuentre en estatus Confirmado');
                            /*
                            var callbackRemesa = function()
                            {
	                            centrarVentanaInterna(Ext.create('Ext.window.Window',
	                            {
	                                title       : 'Documentos de la p&oacute;liza ' + json.smap1.nmpolizaEmi + ' [' + json.smap1.nmpoliexEmi + ']'
	                                ,width      : 500
	                                ,height     : 400
	                                ,autoScroll : true
	                                ,modal      : true
	                                ,cls        : 'VENTANA_DOCUMENTOS_CLASS'
	                                ,loader     :
	                                {
	                                    scripts   : true
	                                    ,autoLoad : true
	                                    ,url      : _p25_urlVentanaDocumentos
	                                    ,params   :
	                                    {
	                                        'smap1.cdunieco'  : _p25_smap1.cdunieco
	                                        ,'smap1.cdramo'   : _p25_smap1.cdramo
	                                        ,'smap1.estado'   : 'M'
	                                        ,'smap1.nmpoliza' : json.smap1.nmpolizaEmi
	                                        ,'smap1.nmsuplem' : json.smap1.nmsuplemEmi
	                                        ,'smap1.nmsolici' : json.smap1.nmpoliza
	                                        ,'smap1.ntramite' : _p25_ntramite
	                                        ,'smap1.tipomov'  : '0'
	                                    }
	                                }
	                            }).show());
                            };
                            _generarRemesaClic(
                                false
                                ,_p25_smap1.cdunieco
                                ,_p25_smap1.cdramo
                                ,'M'
                                ,json.smap1.nmpolizaEmi
                                ,callbackRemesa
                            );
                            */
                        }
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Mesa de control'
                        ,icon    : '${ctx}/resources/fam3icons/icons/house.png'
                        ,handler : _p25_mesacontrol
                    }
                ]);
                
                if(_p25_smap1.VENTANA_DOCUMENTOS=='S')
                {
                    Ext.ComponentQuery.query('[ventanaDocu]')[0].destroy();
                    debug('ventana de documentos destruida');
                }
                mensajeCorrecto(
                    'P&oacute;liza emitida'
                    ,json.respuesta+'<br/>Los documentos se est\u00e1n generando, pueden consultarse m\u00e1s tarde desde mesa de control cuando el tr\u00e1mite se encuentre en estatus Confirmado'
                );
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure:function()
        {
            ventana.setLoading(false);
            errorComunicacion();
        }
    });
}

function _p25_crearVentanaClausulas()
{
    debug('>_p25_crearVentanaClausulas<');
    
    _ventanaClausulas = Ext.create('Ext.window.Window',
    {
        title   : 'Exclusiones/Extraprimas (Cl&aacute;usulas)'
        ,width  : 800
        ,height : 500
        ,modal  : true
        ,loader : {
                url       : _p25_urlEditarExclusiones
                ,params   :
                {
                    'smap1.pv_cdunieco'      : _p25_smap1.cdunieco
                    ,'smap1.pv_cdramo'       : _p25_smap1.cdramo
                    ,'smap1.pv_estado'       : _p25_smap1.estado
                    ,'smap1.pv_nmpoliza'     : _p25_smap1.nmpoliza
                    ,'smap1.pv_nmsituac'     : '0'
                    ,'smap1.pv_nmsuplem'     : Ext.isEmpty(_p25_smap1.nmsuplem)?'0':_p25_smap1.nmsuplem
                    ,'smap1.pv_cdperson'     : ''
                    ,'smap1.pv_cdrol'        : ''
                    ,'smap1.nombreAsegurado' : 'POLIZA'
                    ,'smap1.cdrfc'           : ''
                }
                ,scripts  : true
                ,autoLoad : true
        }
        /*,loader :
        {
            scripts   : true
            ,autoLoad : true
            ,url      : _p25_urlPantallaClausulasPoliza
            ,params   :
            {
                'smap1.cdunieco'  : _p25_smap1.cdunieco
                ,'smap1.cdramo'   : _p25_smap1.cdramo
                ,'smap1.estado'   : _p25_smap1.estado
                ,'smap1.nmpoliza' : _p25_smap1.nmpoliza
                ,'smap1.nmsuplem' : Ext.isEmpty(_p25_smap1.nmsuplem)?'0':_p25_smap1.nmsuplem
            }
        }*/
    }).show();
    
    centrarVentanaInterna(_ventanaClausulas);
}

function _p25_agentes()
{
    debug('>_p25_agentes');
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'AGENTES'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 800
        ,height      : 500
        ,autoScroll  : true
        ,closeAction : 'destroy'
        ,loader      :
        {
            url       : _p25_urlPantallaAgentes
            ,scripts  : true
            ,autoLoad : true
        },
        listeners: {
        	close: function (){
        		try{
        			_ventanaGridAgentesSuperior.destroy();
        		}catch (e){
        			debugError('Error al destruir ventana de agentes.',e);
        		}
        	}
        }
    }).show());
    debug('<_p25_agentes');
}

function _p25_mostrarVentanaComplementoCotizacion(complemento,callback)
{
    debug('>_p25_mostrarVentanaComplementoCotizacion');
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title           : 'Complemento de '+(complemento=='C'?'cotizaci&oacute;n':'emisi&oacute;n')
        ,width          : 500
        ,minHeight      : 100
        ,maxHeight      : 400
        ,modal          : true
        ,closeOperation : 'destroy'
        ,items          :
        [
            {
                xtype     : 'form'
                ,url      : _p25_urlComplementoCotizacion
                ,border   : 0
                ,defaults : { style : 'margin:5px;' }
                ,items    :
                [
                    {
                        xtype  : 'displayfield'
                        ,value : 'Puede subir un complemento para agregar asegurados a la '+(complemento=='C'?'cotizaci&oacute;n':'emisi&oacute;n')
                    }
                    ,{
                        xtype       : 'filefield'
                        ,fieldLabel : 'Censo de asegurados'
                        ,name       : 'censo'
                        ,buttonText : 'Examinar...'
                        ,allowBlank : false
                        ,buttonOnly : false
                        ,width      : 450
                        ,cAccept    : ['xls','xlsx']
                        ,msgTarget  : 'side'
                        ,listeners  :
                        {
                            change : function(me)
                            {
                                //alert('5');
                                var indexofPeriod = me.getValue().lastIndexOf("."),
                                uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                if (!Ext.Array.contains(this.cAccept, uploadedExtension))
                                {
                                    centrarVentanaInterna(Ext.MessageBox.show(
                                    {
                                        title   : 'Error de tipo de archivo',
                                        msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                        buttons : Ext.Msg.OK,
                                        icon    : Ext.Msg.WARNING
                                    }));
                                    me.reset();
                                }
                            }
                        }
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Complementar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,handler : function(me)
                        {
                            debug('>complemento cotizacion button click');
                            var form = me.up('form');
                            
                            var params =
                            {
                                'smap1.cdunieco'     : _p25_smap1.cdunieco
                                ,'smap1.cdramo'      : _p25_smap1.cdramo
                                ,'smap1.cdtipsit'    : _p25_smap1.cdtipsit
                                ,'smap1.estado'      : _p25_smap1.estado
                                ,'smap1.nmpoliza'    : _p25_smap1.nmpoliza
                                ,'smap1.complemento' : complemento
                                ,'smap1.ntramite'    : _p25_smap1.ntramite
                                ,'smap1.cdagente'    : _fieldByName('cdagente').getValue()
                                ,'smap1.codpostal'   : _fieldByName('codpostal').getValue()
                                ,'smap1.cdestado'    : _fieldByName('cdedo').getValue()
                                ,'smap1.cdmunici'    : _fieldByName('cdmunici').getValue()
                            };
                            for(var i=0;i<5;i++)
                            {
                                try
                                {
                                    params['smap1.cdplan'+(i+1)] = _p25_storeGrupos.getAt(i).get('cdplan');
                                }
                                catch(e)
                                {
                                    params['smap1.cdplan'+(i+1)] = '';
                                    debug('Error inofensivo','No hay grupo '+(i+1));
                                }
                            }
                            
                            if(form.isValid())
                            {
                                form.setLoading(true);
                                form.submit(
                                {
                                    params   : params
                                    ,success : function(form2,action)
                                    {
                                        form.setLoading(false);
                                        var ck = 'Procesando respuesta al subir complemento';
                                        try
                                        {
                                            var json = Ext.decode(action.response.responseText);
                                            debug('### submit:',json);
                                            if(json.exito)
                                            {
                                                form.up('window').destroy();
                                                var despues = function()
                                                {
	                                                var numRand      = Math.floor((Math.random() * 100000) + 1);
	                                                var nombreModelo = '_modelo'+numRand;
	                                                var fields  = [];
	                                                var columns = [];
	                                                
	                                                if(Number(json.smap1.filasProcesadas)>0)
	                                                {
	                                                    var record = json.slist1[0];
	                                                    debug('record:',record);
	                                                    for(var att in record)
	                                                    {
	                                                        if(att.substring(0,1)=='_')
	                                                        {
	                                                            var col =
	                                                            {
	                                                                dataIndex : att.substring(att.lastIndexOf('_')+1)
	                                                                ,text     : record[att]
	                                                                ,orden    : ''+att
	                                                            };
	                                                            columns.push(col);
	                                                        }
	                                                        else
	                                                        {
	                                                            fields.push(att);
	                                                        }
	                                                    }
	                                                }
	                                                
	                                                for(var i=0;i<columns.length-1;i++)
	                                                {
	                                                    for(var j=i+1;j<columns.length;j++)
	                                                    {
	                                                        if(columns[i].orden>columns[j].orden)
	                                                        {
	                                                            var aux    = columns[i];
	                                                            columns[i] = columns[j];
	                                                            columns[j] = aux;
	                                                        }
	                                                    }
	                                                }
	                                                
	                                                debug('fields:',fields,'columns:',columns);
	                                                
	                                                Ext.define(nombreModelo,
	                                                {
	                                                    extend  : 'Ext.data.Model'
	                                                    ,fields : fields
	                                                });
	                                                
	                                                var store = Ext.create('Ext.data.Store',
	                                                {
	                                                    model : nombreModelo
	                                                    ,data : json.slist1
	                                                });
	                                                
	                                                debug('store.getRange():',store.getRange());
	                                                
	                                                centrarVentanaInterna(Ext.create('Ext.window.Window',
	                                                {
	                                                    width     : 600
	                                                    ,height   : 500
	                                                    ,title    : 'Revisar asegurados del complemento'
	                                                    ,closable : false
	                                                    ,items    :
	                                                    [
	                                                        Ext.create('Ext.panel.Panel',
	                                                        {
	                                                            layout    : 'hbox'
	                                                            ,border   : 0
	                                                            ,defaults : { style : 'margin:5px;' }
	                                                            ,height   : 40
	                                                            ,items    :
	                                                            [
	                                                                {
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas leidas'
	                                                                    ,value      : json.smap1.filasLeidas
	                                                                }
	                                                                ,{
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas procesadas'
	                                                                    ,value      : json.smap1.filasProcesadas
	                                                                }
	                                                                ,{
	                                                                    xtype       : 'displayfield'
	                                                                    ,fieldLabel : 'Filas con error'
	                                                                    ,value      : json.smap1.filasErrores
	                                                                }
	                                                                ,{
	                                                                    xtype    : 'button'
	                                                                    ,text    : 'Ver errores'
	                                                                    ,hidden  : Number(json.smap1.filasErrores)==0
	                                                                    ,handler : function()
	                                                                    {
	                                                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
	                                                                        {
	                                                                            modal        : true
	                                                                            ,closeAction : 'destroy'
	                                                                            ,title       : 'Errores al procesar censo'
	                                                                            ,width       : 800
	                                                                            ,height      : 500
	                                                                            ,items       :
	                                                                            [
	                                                                                {
	                                                                                    xtype       : 'textarea'
	                                                                                    ,fieldStyle : 'font-family: monospace'
	                                                                                    ,value      : json.smap1.erroresCenso
	                                                                                    ,readOnly   : true
	                                                                                    ,width      : 780
	                                                                                    ,height     : 440
	                                                                                }
	                                                                            ]
	                                                                        }).show());
	                                                                    }
	                                                                }
	                                                            ]
	                                                        })
	                                                        ,Ext.create('Ext.grid.Panel',
	                                                        {
	                                                            height      : 350
	                                                            ,columns    : columns
	                                                            ,store      : store
	                                                            ,viewConfig : viewConfigAutoSize
	                                                        })
	                                                    ]
	                                                    ,buttonAlign : 'center'
	                                                    ,buttons     :
	                                                    [
	                                                        {
	                                                            text     : 'Aceptar y continuar'
	                                                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
	                                                            ,handler : function(){ callback(); }
	                                                        }
	                                                        ,{
	                                                            text     : 'Agregar m&aacute;s'
	                                                            ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
	                                                            ,handler : function(me){ me.up('window').destroy(); }
	                                                        }
	                                                    ]
	                                                }).show());
	                                            };
	                                            
	                                            _p25_generarTramiteClic(despues,false,false,true);
                                            }
                                            else
                                            {
                                                mensajeError(json.respuesta);
                                            }
                                        }
                                        catch(e)
                                        {
                                            manejaException(e,ck);
                                        }
                                    }
                                    ,failure : function()
                                    {
                                        form.setLoading(false);
                                        errorComunicacion(null,'Error al subir archivo de complemento');
                                    }
                                })
                            }
                            else
                            {
                                datosIncompletos();
                            }
                        }
                    }
                ]
            }
        ]
    }).show());
    
}

function _p25_desbloqueoBotonRol(boton)
{
    var ck = 'Recuperando permisos de bot\u00f3n';
    try
    {
        boton.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p25_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_PERMISO_BOTON_GENERAR_COLECTIVO'
            }
            ,success : function(response)
            {
                boton.setLoading(false);
                var ck = 'Decodificando respuesta al recuperar permisos de bot\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### permisos boton:',json);
                    if(json.success==true)
                    {
                         if('S'==json.params.ACTIVAR_BOTON)
                         {
                             boton.show();
                             boton.enable();
                         }
                         else
                         {
                             mensajeWarning('Favor de revisar los errores de la carga');
                             boton.disable();
                             boton.hide();
                         }
                    }
                    else
                    {
                        mensajeError(json.message);
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                boton.setLoading(false);
                errorComunicacion(null,'Error al recuperar permisos de bot\u00f3n');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function recupeListaTatrisitSinPadre(combo)
{   
       Ext.Ajax.request(
               {
                   url     : _p25_urlRecuperacion
                   ,params :
                   {
                       'params.consulta'     : 'RECUPERAR_LISTA_TATRISIT_SIN_PADRE'
                      ,'params.cdtipsit'     : _p25_smap1.cdtipsit 
                      ,'params.cdatribu'     : combo.cdatribu
                   }
                   ,success : function(response)
                   {
                       _p25_editorPlan.setLoading(false);
                       var json = Ext.decode(response.responseText);
                       debug('Lista Tatrsit sin Padre:',json);
                       listaSinPadre[combo.name] = json.list;
                       debug('listaSinPadre:',listaSinPadre);
                   }
                   ,failure : function()
                   {
                       _p25_editorPlan.setLoading(false);
                       errorComunicacion();
                   }
               });
}

function _p25_exportarExcelCensoFinal(){
    Ext.create('Ext.form.Panel').submit({
        standardSubmit : true,
        url:_p25_urlReporte,
        params: {
            cdreporte : 'REPEXC021'
            ,'params.cdunieco' : _p25_smap1.cdunieco
            ,'params.cdramo'   : _p25_smap1.cdramo
            ,'params.estado'   : _p25_smap1.estado
            ,'params.nmpoliza' : _p25_smap1.nmpoliza
            ,'params.ntramite' : _p25_smap1.ntramite
            ,'params.exportar' : true
        },
        success: function(form, action) {
            
        },
        failure: function(form, action){
            switch (action.failureType){
                case Ext.form.action.Action.CONNECT_FAILURE:
                    Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n');
                    break;
                case Ext.form.action.Action.SERVER_INVALID:
                case Ext.form.action.Action.LOAD_FAILURE:
                    Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte');
                    break;
           }
        }
    });
}

function _hide (comp) {
    debug('_hide comp:', comp, '.');
    if (!Ext.isEmpty(comp) && typeof comp === 'object') {
        //comp.addCls('red');
        //comp.removeCls('green');
        comp.hide();
    }
}

function _show (comp) {
    debug('_show comp:', comp, '.');
    if (!Ext.isEmpty(comp) && typeof comp === 'object') {
        //comp.addCls('green');
        //comp.removeCls('red');
        comp.show();
    }
}

function _p25quitarFiltroSumaAsegRamo11 () {
    debug('_p25quitarFiltroSumaAsegRamo11');
    var ck = 'Quitando filtros de suma asegurada ramo 11';
    try {
        if (_p25_smap1.cdramo == Ramo.GastosMedicosMayoresPrueba) {
            var cols = _p25_query('#'+_p25_tabGrupos.itemId)[0].items.items[0].columns;
            debug('cols:', cols);
            for (var i = 0; i < cols.length; i++) {
                if (cols[i].text === 'SUMA ASEGURADA') {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                } else if (cols[i].text === 'DEDUCIBLE') {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                } else if (cols[i].text.indexOf('ULO HOSPITAL') !== -1) {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                }/* else if (cols[i].text.indexOf('VEL HOSPITAL') !== -1) {
                    var editor = cols[i].field;
                    editor.getStore().clearFilter();
                }*/
            }
        }
    } catch (e) {
        manejaException(e, ck);
    }
};
////// funciones //////
<%-- include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp" --%>
</script>
<script
	src="${ctx}/js/proceso/emision/funcionesCotizacionGrupo.js?now=${now}"></script>
</head>
<body>
	<div id="_p25_divpri" style="height: 1400px;"></div>
</body>
</html>