<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
#canvasdiv
{
    width            : 5000px;
    height           : 5000px;
    position         : absolute;
    background-image : url('${flujoimg}fondoModelador.png');
}
.catEntidad
{
    border : 0px solid red;
}
.estado .image
{
    width            : 50px;
    height           : 50px;
    background-image : url('${flujoimg}estado.png');
}
.sucursal .image
{
    width            : 50px;
    height           : 50px;
    background-image : url('${flujoimg}estado.png');
}
.estado .CDESTADOMC, .labelE
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 20px;
    width       : 200px;
    text-align  : center;
}
.sucursal .CDUNIECO, .labelS
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 20px;
    width       : 200px;
    text-align  : center;
}
.pantalla .image
{
    width            : 45px;
    height           : 60px;
    background-image : url('${flujoimg}pantalla.png');
}
.pantalla .CDPANTMC, .labelP
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 25px;
    width       : 200px;
    text-align  : center;
}
.componente .image
{
    width            : 60px;
    height           : 40px;
    background-image : url('${flujoimg}componente.png');
}
.componente .CDCOMPMC, .labelC
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 15px;
    width       : 200px;
    text-align  : center;
}
.proceso .image
{
    width            : 65px;
    height           : 35px;
    background-image : url('${flujoimg}proceso.png');
}
.proceso .CDPROCMC, .labelO
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 10px;
    width       : 200px;
    text-align  : center;
}
.validacion .image
{
    width            : 50px;
    height           : 50px;
    background-image : url('${flujoimg}validacion.png');
}
.correo .image
{
    width            : 60px;
    height           : 50px;
    background-image : url('${flujoimg}correo.png');
}
.labelV
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 20px;
    width       : 200px;
    text-align  : center;
}
.revision .image
{
    width            : 45px;
    height           : 50px;
    background-image : url('${flujoimg}revision.png');
}
.labelR
{
    position    : absolute;
    left        : 50%;
    margin-left : -100px;
    border      : 0px solid red;
    top         : 20px;
    width       : 200px;
    text-align  : center;
}
.titulo .image
{
    width            : 50px;
    height           : 20px;
    background-image : url('${flujoimg}titulo.png');
}
.labelT
{
    position       : absolute;
    left           : 0%;
    margin-left    : 10px;
    border         : 0px solid red;
    top            : 3px;
    text-align     : left;
    text-transform : uppercase;
    font-weight    : bold;
}
.labelT0
{
    width : 500px;
}
.labelT1
{
    width : 1000px;
}
.entidad:hover
{
    border : 0px solid blue;
}
.entidadE
{
    position         : absolute;
    width            : 50px;
    height           : 50px;
    border           : 0px solid red;
    background-image : url('${flujoimg}estado.png');
}
.entidadS
{
    position         : absolute;
    width            : 50px;
    height           : 50px;
    border           : 0px solid red;
    background-image : url('${flujoimg}estado.png');
}
.entidadP
{
    position         : absolute;
    width            : 45px;
    height           : 60px;
    border           : 0px solid red;
    background-image : url('${flujoimg}pantalla.png');
}
.entidadC
{
    position         : absolute;
    width            : 60px;
    height           : 40px;
    border           : 0px solid red;
    background-image : url('${flujoimg}componente.png');
}
.entidadO
{
    position         : absolute;
    width            : 65px;
    height           : 35px;
    border           : 0px solid red;
    background-image : url('${flujoimg}proceso.png');
}
.entidadV
{
    position         : absolute;
    width            : 50px;
    height           : 50px;
    border           : 0px solid red;
    background-image : url('${flujoimg}validacion.png');
}
.entidadM
{
    position         : absolute;
    width            : 60px;
    height           : 50px;
    border           : 0px solid red;
    background-image : url('${flujoimg}correo.png');
}
.entidadR
{
    position         : absolute;
    width            : 45px;
    height           : 50px;
    border           : 0px solid red;
    background-image : url('${flujoimg}revision.png');
}
.entidadT0
{
    position         : absolute;
    width            : 500px;
    height           : 20px;
    border           : 0px solid red;
    background-image : url('${flujoimg}titulo0.png');
}
.entidadT1
{
    position         : absolute;
    width            : 1000px;
    height           : 20px;
    border           : 0px solid red;
    background-image : url('${flujoimg}titulo1.png');
}
.plus
{
    position         : absolute;
    top              : 50%;
    left             : 50%;
    margin-top       : -8px;
    margin-left      : -24px;
    width            : 16px;
    height           : 16px;
    border           : 0px solid green;
    background-image : url('${icons}add.png');
    visibility       : hidden;
}
.entidad:hover .plus
{
    visibility : visible;
}
.edit
{
    position         : absolute;
    top              : 50%;
    left             : 50%;
    margin-top       : -8px;
    margin-left      : -8px;
    width            : 16px;
    height           : 16px;
    border           : 0px solid green;
    background-image : url('${icons}pencil.png');
    visibility       : hidden;
}
.entidad:hover .edit
{
    visibility : visible;
}
.remove
{
    position         : absolute;
    top              : 50%;
    left             : 50%;
    margin-top       : -8px;
    margin-left      : 8px;
    width            : 16px;
    height           : 16px;
    border           : 0px solid green;
    background-image : url('${icons}delete.png');
    visibility       : hidden;
}
.entidad:hover .remove
{
    visibility : visible;
}
.catedit
{
    position : absolute;
    top      : 0;
    left     : 0;
}
.entidad:hover .labelE
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelS
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelP
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelC
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelO
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelV
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelR
{
    top : -15px;
    font-weight: bold;
}
.entidad:hover .labelT
{
    top : -15px;
    font-weight: bold;
}
</style>
<script type="text/javascript" src="${ctx}/resources/jsPlumb/jsPlumb-2.0.4.js?${now}"></script>
<script>
////// iframe //////
var stop = false;
if(inIframe())
{
    try
    {
        stop = true;
        window.top.location = window.location;
    }
    catch(e)
    {
        alert('Error');
        window.location='error';
    }
}
////// iframe //////

////// urls //////
var _p52_urlRegistrarEntidad       = '<s:url namespace="/flujomesacontrol" action="registrarEntidad"       />';
var _p52_urlBorrarEntidad          = '<s:url namespace="/flujomesacontrol" action="borrarEntidad"          />';
var _p52_urlRegistrarConnection    = '<s:url namespace="/flujomesacontrol" action="registrarConnection"    />';
var _p52_urlBorrarConnection       = '<s:url namespace="/flujomesacontrol" action="borrarConnection"       />';
var _p52_urlGuardarCoords          = '<s:url namespace="/flujomesacontrol" action="guardarCoordenadas"     />';
var _p52_urlRecuperacion           = '<s:url namespace="/recuperacion"     action="recuperar"              />';
var _p52_urlMovimientoTtipflumc    = '<s:url namespace="/flujomesacontrol" action="movimientoTtipflumc"    />';
var _p52_urlMovimientoTflujomc     = '<s:url namespace="/flujomesacontrol" action="movimientoTflujomc"     />';
var _p52_urlMovimientoCatalogo     = '<s:url namespace="/flujomesacontrol" action="movimientoCatalogo"     />';
var _p52_urlCargarModelado         = '<s:url namespace="/flujomesacontrol" action="cargarModelado"         />';
var _p52_urlCargarDatosEstado      = '<s:url namespace="/flujomesacontrol" action="cargarDatosEstado"      />';
var _p52_urlGuardarDatosStatus     = '<s:url namespace="/flujomesacontrol" action="guardarDatosEstado"     />';
var _p52_urlCargarDatosValidacion  = '<s:url namespace="/flujomesacontrol" action="cargarDatosValidacion"  />';
var _p52_urlCargarDatosCorreo      = '<s:url namespace="/flujomesacontrol" action="cargarDatosCorreo"      />';
var _p52_urlGuardarDatosValidacion = '<s:url namespace="/flujomesacontrol" action="guardarDatosValidacion" />';
var _p52_urlGuardarDatosCorreo     = '<s:url namespace="/flujomesacontrol" action="guardarDatosCorreo"     />';
var _p52_urlCargarDatosRevision    = '<s:url namespace="/flujomesacontrol" action="cargarDatosRevision"    />';
var _p52_urlGuardarDatosRevision   = '<s:url namespace="/flujomesacontrol" action="guardarDatosRevision"   />';
var _p52_urlMovimientoTdocume      = '<s:url namespace="/flujomesacontrol" action="movimientoTdocume"      />';
var _p52_urlMovimientoTrequisi     = '<s:url namespace="/flujomesacontrol" action="movimientoTrequisi"     />';
var _p52_urlCargarDatosAccion      = '<s:url namespace="/flujomesacontrol" action="cargarDatosAccion"      />';
var _p52_urlGuardarDatosAccion     = '<s:url namespace="/flujomesacontrol" action="guardarDatosAccion"     />';
var _p52_urlGuardarTtipflurol      = '<s:url namespace="/flujomesacontrol" action="guardarTtipflurol"      />';
var _p52_urlGuardarTflujorol       = '<s:url namespace="/flujomesacontrol" action="guardarTflujorol"       />';
var _p52_urlCargarDatosTitulo      = '<s:url namespace="/flujomesacontrol" action="cargarDatosTitulo"      />';
var _p52_urlGuardarDatosTitulo     = '<s:url namespace="/flujomesacontrol" action="guardarDatosTitulo"     />';
////// urls //////

////// variables //////
var estadoTpl;
var sucursalTpl;
var pantallaTpl;
var componenteTpl;
var procesoTpl;
var validacionTpl;
var revisionTpl;
var correoTpl;

var _p52_panelGrids;
var _p52_gridTramites;
var _p52_gridProcesos;
var _p52_panelDibujo;
var _p52_panelCanvas;
var _p52_catalogoEstados;
var _p52_catalogoSucursales;
var _p52_panelEstado;
var _p52_catalogoPantallas;
var _p52_catalogoComponentes;
var _p52_catalogoProcesos;
var _p52_catalogoValidaciones;
var _p52_catalogoCorreos;
var _p52_catalogoRevisiones;
var _p52_formValidacion;
var _p52_formCorreos;
var _p52_panelRevision;
var _p52_panelTitulo;
var _p52_panelAccion;
var _p52_catalogoIconos;

var toolkit;

var epProps = [];

var _p52_formTtipflumc;
var _p52_formTflujomc;
var _p52_selectedFlujo;
var _p52_formEstado;
var _p52_formComponente;
var _p52_formPantalla;
var _p52_formProceso;
var _p52_formTdocume;
var _p52_formTrequisi;
var _p52_formCorreos;
var _p52_winVarsCorreo;

var _p52_cargando = false;

var _p52_debug = false;

var _p52_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p52_params:', _p52_params, '.');

var _p52_tituloPrincipal;
////// variables //////

////// overrides //////
extjs_custom_override_mayusculas = false;

Ext.define('App.overrides.view.Table',
{
    override: 'Ext.view.Table',
    getRecord: function (node) {
        node = this.getNode(node);
        if (node) {
            //var recordIndex = node.getAttribute('data-recordIndex');
            //if (recordIndex) {
            //    recordIndex = parseInt(recordIndex, 10);
            //    if (recordIndex > -1) {
            //        // The index is the index in the original Store, not in a GroupStore
            //        // The Grouping Feature increments the index to skip over unrendered records in collapsed groups
            //        return this.store.data.getAt(recordIndex);
            //    }
            //}
            return this.dataSource.data.get(node.getAttribute('data-recordId'));
        }
    },
    indexInStore: function (node) {
        node = this.getNode(node, true);
        if (!node && node !== 0) {
            return -1;
        }
        //var recordIndex = node.getAttribute('data-recordIndex');
        //if (recordIndex) {
        //    return parseInt(recordIndex, 10);
        //}
        return this.dataSource.indexOf(this.getRecord(node));
    }
});
////// overrides //////

////// componentes dinamicos //////
////// componentes dinamicos //////

Ext.onReady(function()
{
    if(stop)
    {
        return;
    }
    ////// requires //////
    Ext.Loader.setConfig({enabled: true});
    Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES+'SelectorMultiple');
	/* Ext.require(['Ext.form.Panel',
				 'Ext.ux.form.MultiSelect',
				 'Ext.ux.form.ItemSelector',
				 'Ext.tip.QuickTipManager',
				 'Ext.ux.ajax.JsonSimlet',
				 'Ext.ux.ajax.SimManager']); */
    ////// requires //////
    
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    estadoTpl = new Ext.Template(
    [
         '<div id="E{CDESTADOMC}" class="catEntidad estado" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{DSESTADOMC}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div><div class="CDESTADOMC">{CDESTADOMC}</div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center"><a class="catedit" href="#" onclick="_p52_editCatClic(\'E\',\'E{CDESTADOMC}\'); return false;" ><img src="${icons}pencil.png" /></a>{DSESTADOMC}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    sucursalTpl = new Ext.Template(
    [
         '<div id="S{CDUNIECO}" class="catEntidad sucursal" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{DSUNIECO}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div><div class="CDUNIECO">{CDUNIECO}</div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center">{DSUNIECO}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    pantallaTpl = new Ext.Template(
    [
         '<div id="P{CDPANTMC}" class="catEntidad pantalla" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{DSPANTMC}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div><div class="CDPANTMC">{CDPANTMC}</div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center"><a class="catedit" href="#" onclick="_p52_editCatClic(\'P\',\'P{CDPANTMC}\'); return false;" ><img src="${icons}pencil.png" /></a>{DSPANTMC}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    componenteTpl = new Ext.Template(
    [
         '<div id="C{CDCOMPMC}" class="catEntidad componente" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{DSCOMPMC}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div><div class="CDCOMPMC">{CDCOMPMC}</div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center"><a class="catedit" href="#" onclick="_p52_editCatClic(\'C\',\'C{CDCOMPMC}\'); return false;" ><img src="${icons}pencil.png" /></a>{DSCOMPMC}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    procesoTpl = new Ext.Template(
    [
         '<div id="O{CDPROCMC}" class="catEntidad proceso" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{DSPROCMC}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div><div class="CDPROCMC">{CDPROCMC}</div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center"><a class="catedit" href="#" onclick="_p52_editCatClic(\'O\',\'O{CDPROCMC}\'); return false;" ><img src="${icons}pencil.png" /></a>{DSPROCMC}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    validacionTpl = new Ext.Template(
    [
         '<div id="V0" class="catEntidad validacion" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{dsvalidacion}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center">{dsvalidacion}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    revisionTpl = new Ext.Template(
    [
         '<div id="R0" class="catEntidad revision" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{dsrevision}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center">{dsrevision}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    correoTpl = new Ext.Template(
    	    [
    	         '<div id="M0" class="catEntidad correo" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{dsmail}">'
    	        ,'    <table width="90" border="0">'
    	        ,'        <tr>'
    	        ,'            <td align="center"><div class="image"></div></td>'
    	        ,'        </tr>'
    	        ,'        <tr>'
    	        ,'            <td align="center">{dsmail}</td>'
    	        ,'        </tr>'
    	        ,'    </table>'
    	        ,'</div>'
    	    ]);
    
    tituloTpl = new Ext.Template(
    [
         '<div id="T{cdtitulo}" class="catEntidad titulo" draggable="true" ondragstart="_p52_dragstart(event);" descrip="{dstitulo}">'
        ,'    <table width="90" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><div class="image"></div></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center">{dstitulo}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    iconoTpl = new Ext.Template(
    [
         '<div class="radioicono">'
        ,'    <table width="80" border="0">'
        ,'        <tr>'
        ,'            <td align="center"><img src="${icons}{CDICONO}.png" /></td>'
        ,'        </tr>'
        ,'        <tr>'
        ,'            <td align="center"><input type="radio" name="iconoaccion" value="{CDICONO}"/>{DSICONO}</td>'
        ,'        </tr>'
        ,'    </table>'
        ,'</div>'
    ]);
    
    epProps['E'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Circle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['S'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Circle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['P'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['C'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['O'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['V'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Diamond' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['R'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : true
        ,isTarget  : true
    };
    
    epProps['T'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : false
        ,isTarget  : false
    };
    
    epProps['M'] =
    {
        anchor     : [ 'Perimeter' , { shape : 'Rectangle' } ]
        ,isSource  : true
        ,isTarget  : true
    };    
   
    _p52_formTtipflumc = Ext.create('Ext.window.Window',
    {
        title        : 'TR\u00C1MITE'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    <s:property value="items.ttipfluFormItems" escapeHtml="false" />
                    ,{
                        xtype       : 'fieldcontainer'
                        ,fieldLabel : 'PROPIEDADES'
                        ,items      :
                        [
                            {
                                xtype       : 'checkbox'
                                ,boxLabel   : 'REQUIERE P\u00d3LIZA'
                                ,name       : 'SWREQPOL'
                                ,inputValue : 'S'
                            }
                            ,{
                                xtype       : 'checkbox'
                                ,boxLabel   : 'M\u00DALTIPLES P\u00d3LIZAS'
                                ,name       : 'SWMULTIPOL'
                                ,inputValue : 'S'
                                ,align      : 'right'
                            }
                            ,{
                                xtype       : 'checkbox'
                                ,boxLabel   : 'EXTERNO'
                                ,name       : 'SWEXTERNO'
                                ,inputValue : 'S'
                                ,align      : 'right'
                            }
                        ]
                    }
                ]
            })//zxc
            ,Ext.create('Ext.grid.Panel',
            {
                title        : 'Permisos'
                ,hideHeaders : true
                ,width       : 300
                ,height      : 200
                ,border      : 0
                ,columns     :
                [
                    {
                        dataIndex : 'DSSISROL'
                        ,flex     : 1
                    }
                    ,{
                        xtype      : 'checkcolumn'
                        ,dataIndex : 'SWACTIVO'
                        ,width     : 30
                    }
                ]
                ,store : Ext.create('Ext.data.Store',
                {
                    autoLoad : true
                    ,fields  :
                    [
                        'CDSISROL'
                        ,'DSSISROL'
                        ,{ name : 'SWACTIVO' , type : 'boolean' }
                    ]
                    ,proxy   :
                    {
                        type         : 'ajax'
                        ,url         : _p52_urlRecuperacion
                        ,extraParams :
                        {
                            'params.consulta' : 'RECUPERAR_ROLES'
                        }
                        ,reader      :
                        {
                            type  : 'json'
                            ,root : 'list'
                        }
                    }
                })
            })
        ]
        ,showNew : function()
        {
            var me = this;
            me.down('form').getForm().reset();
            me.down('[name=ACCION]').setValue('I');
            me.down('[name=CDTIPMOD]').setValue(_p52_params.cdtipmod);
            me.down('grid').getStore().each(function(r)
            {
                r.set('SWACTIVO',false);
            });
            
            me.down('grid').getStore().commitChanges();
            
            centrarVentanaInterna(me.show());
        }
        ,showEdit : function(record)
        {
            var me = this;
            me.down('form').getForm().loadRecord(record);
            me.down('[name=ACCION]').setValue('U');
            
            var store = me.down('grid').getStore();
            
            store.each(function(r)
            {
                r.set('SWACTIVO',false);
            });
            
            store.commitChanges();
            
            var ck = 'Recuperando permisos por tr\u00e1mite';
            try
            {
                _mask(ck);
                Ext.Ajax.request(
                {
                    url      : _p52_urlRecuperacion
                    ,params  :
                    {
                        'params.consulta'  : 'RECUPERAR_TTIPFLUROL'
                        ,'params.cdtipflu' : record.get('CDTIPFLU')
                    }
                    ,success : function(response)
                    {
                        _unmask();
                        var ck = 'Decodificando respuesta al recuperar permisos por tr\u00e1mite';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### ttipflurol:',json,'.');
                            if(json.success === true)
                            {
                                ck = 'Iterando permisos';
                                
                                for(var i = 0 ; i < json.list.length ; i++)
                                {
                                    store.getAt(store.find('CDSISROL',json.list[i].CDSISROL)).set('SWACTIVO',json.list[i].SWACTIVO === 'S');
                                }
                                
                                store.commitChanges();
                                
                                centrarVentanaInterna(me.show());
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
                        errorComunicacion(null,'Error al recuperar permisos por tr\u00e1mite');
                    }
                });
            }
            catch(e)
            {
                _unmask();
                manejaException(e,ck);
            }
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Guardar'
                ,icon    : '${icons}disk.png'
                ,handler : function(me)
                {
                    var ck = 'Guardando';
                    try
                    {
                        var win    = me.up('window')
                            ,form  = win.down('form').getForm()
                            ,store = win.down('grid').getStore();
                        
                        if(!form.isValid())
                        {
                            throw 'Favor de revisar los datos';
                        }
                        
                        _setLoading(true,win);
                        Ext.Ajax.request(
                        {
                            url      : _p52_urlMovimientoTtipflumc
                            ,params  : _formValuesToParams(form.getValues())
                            ,success : function(response)
                            {
                                _setLoading(false,win);
                                var ck = 'Decodificando respuesta al guardar tr\u00E1mite';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### mov ttipflumc:',json);
                                    if(json.success==true)
                                    {
                                        ck = 'Guardando permisos';
                                        
                                        var jsonPerm =
                                        {
                                            params :
                                            {
                                                cdtipflu : json.params.CDTIPFLU
                                            }
                                            ,list : []
                                        };
                                        
                                        store.each(function(r)
                                        {
                                            if(r.get('SWACTIVO') === true)
                                            {
                                                jsonPerm.list.push(
                                                {
                                                    CDTIPFLU  : json.params.CDTIPFLU
                                                    ,CDSISROL : r.get('CDSISROL')
                                                    ,SWACTIVO : 'S'
                                                });
                                            }
                                        });
                                        
                                        debug('jsonPerm:',jsonPerm,'.');
                                        
                                        _mask(ck);
                                        Ext.Ajax.request(
                                        {
                                            url       : _p52_urlGuardarTtipflurol
                                            ,jsonData : jsonPerm
                                            ,success  : function(response)
                                            {
                                                _unmask();
                                                var ck = 'Decodificando respuesta al guardar permisos de rol';
                                                try
                                                {
                                                    var jsonRespPerm = Ext.decode(response.responseText);
                                                    debug('### guardar ttipflurol:',jsonRespPerm,'.');
                                                    if(jsonRespPerm.success === true)
                                                    {
                                                        win.hide();
                                                        _p52_gridTramites.store.reload();
                                                    }
                                                    else
                                                    {
                                                        mensajeError(jsonRespPerm.message);
                                                    }
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck);
                                                }
                                            }
                                            ,failure  : function()
                                            {
                                                _unmask();
                                                errorComunicacion(null,'Error al guardar permisos de tr\u00e1mite');
                                            }
                                        });
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
                                _setLoading(false,win);
                                errorComunicacion(null,'Error guardando tr\u00E1mite');
                            }
                        });
                    }
                    catch(e)
                    {
                        _setLoading(false,win);
                        manejaException(e,ck);
                    }
                }
            }
        ]
    });
       
    _p52_formTflujomc = Ext.create('Ext.window.Window',
    {
        title        : 'PROCESO'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '_ACCION'
                        ,name       : 'ACCION'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDTIPFLU'
                        ,name       : 'CDTIPFLU'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDFLUJOMC'
                        ,name       : 'CDFLUJOMC'
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'NOMBRE'
                        ,name       : 'DSFLUJOMC'
                        ,allowBlank : false
                    }
                    ,{
                        xtype       : 'fieldcontainer'
                        ,fieldLabel : 'PROPIEDADES'
                        ,items      :
                        [
                            {
                                xtype       : 'checkbox'
                                ,boxLabel   : 'VISIBLE'
                                ,name       : 'SWFINAL'
                                ,inputValue : 'S'
                            }
                            ,{
                                xtype       : 'radio'
                                ,boxLabel   : 'INDIVIDUAL'
                                ,name       : 'SWGRUPO'
                                ,inputValue : 'I'
                                ,align      : 'right'
                            }
                            ,{
                                xtype       : 'radio'
                                ,boxLabel   : 'COLECTIVO/GRUPO'
                                ,name       : 'SWGRUPO'
                                ,inputValue : 'C'
                                ,align      : 'right'
                            }
                            ,{
                                xtype       : 'radio'
                                ,boxLabel   : 'AMBOS'
                                ,name       : 'SWGRUPO'
                                ,inputValue : ''
                                ,align      : 'right'
                                ,itemId     : 'swgrupoInd'
                            }
                        ]
                    }
                    ,<s:property value="items.comboCdtipram" escapeHtml="false" />
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title        : 'Permisos'
                ,hideHeaders : true
                ,width       : 300
                ,height      : 200
                ,border      : 0
                ,columns     :
                [
                    {
                        dataIndex : 'DSSISROL'
                        ,flex     : 1
                    }
                    ,{
                        xtype      : 'checkcolumn'
                        ,dataIndex : 'SWACTIVO'
                        ,width     : 30
                    }
                ]
                ,store : Ext.create('Ext.data.Store',
                {
                    autoLoad : true
                    ,fields  :
                    [
                        'CDSISROL'
                        ,'DSSISROL'
                        ,{ name : 'SWACTIVO' , type : 'boolean' }
                    ]
                    ,proxy   :
                    {
                        type         : 'ajax'
                        ,url         : _p52_urlRecuperacion
                        ,extraParams :
                        {
                            'params.consulta' : 'RECUPERAR_ROLES'
                        }
                        ,reader      :
                        {
                            type  : 'json'
                            ,root : 'list'
                        }
                    }
                })
            })
        ]
        ,showNew : function()
        {
            var ck = 'Mostrando formulario';
            try
            {
                var sel = _p52_gridTramites.getSelectionModel().getSelection();
                if(sel.length==0)
                {
                    throw 'Debe seleccionar un tr\u00e1mite';
                }
                
                var me = this;
                me.down('form').getForm().reset();
                me.down('[name=ACCION]').setValue('I');
                me.down('[name=CDTIPFLU]').setValue(sel[0].get('CDTIPFLU'));
                me.down('#swgrupoInd').setValue(true);
                
                me.down('grid').getStore().each(function(r)
                {
                    r.set('SWACTIVO',false);
                });
                
                me.down('grid').getStore().commitChanges();
                
                centrarVentanaInterna(me.show());
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,showEdit : function(record)
        {
            var me = this;
            debug('record:',record);
            me.down('form').getForm().loadRecord(record);
            me.down('[name=ACCION]').setValue('U');
            
            me.down('#swgrupoInd').setValue(true);
            
            if(!Ext.isEmpty(record.get('SWGRUPO')))
            {
                debug('se marcara radio:',record.get('SWGRUPO'),',');
                me.down('[name=SWGRUPO][inputValue='+record.get('SWGRUPO')+']').setValue(true);
            }
            
            var store = me.down('grid').getStore();
            
            store.each(function(r)
            {
                r.set('SWACTIVO',false);
            });
            
            store.commitChanges();
            
            var ck = 'Recuperando permisos por proceso';
            try
            {
                _mask(ck);
                Ext.Ajax.request(
                {
                    url      : _p52_urlRecuperacion
                    ,params  :
                    {
                        'params.consulta'   : 'RECUPERAR_TFLUJOROL'
                        ,'params.cdtipflu'  : record.get('CDTIPFLU')
                        ,'params.cdflujomc' : record.get('CDFLUJOMC')
                    }
                    ,success : function(response)
                    {
                        _unmask();
                        var ck = 'Decodificando respuesta al recuperar permisos por proceso';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### tflujorol:',json,'.');
                            if(json.success === true)
                            {
                                ck = 'Iterando permisos';
                                
                                for(var i = 0 ; i < json.list.length ; i++)
                                {
                                    store.getAt(store.find('CDSISROL',json.list[i].CDSISROL)).set('SWACTIVO',json.list[i].SWACTIVO === 'S');
                                }
                                
                                store.commitChanges();
                                
                                centrarVentanaInterna(me.show());
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
                        errorComunicacion(null,'Error al recuperar permisos por proceso');
                    }
                });
            }
            catch(e)
            {
                _unmask();
                manejaException(e,ck);
            }
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Guardar'
                ,icon    : '${icons}disk.png'
                ,handler : function(me)
                {
                    var ck = 'Guardando';
                    try
                    {
                        var win      = me.up('window')
                            ,formCmp = win.down('form')
                            ,form    = win.down('form').getForm()
                            ,store   = win.down('grid').getStore();
                        
                        if(!form.isValid())
                        {
                            throw 'Favor de revisar los datos';
                        }
                        
                        var formValues = form.getValues();
                        
                        _setLoading(true,win);
                        Ext.Ajax.request(
                        {
                            url      : _p52_urlMovimientoTflujomc
                            ,params  : _formValuesToParams(formValues)
                            ,success : function(response)
                            {
                                _setLoading(false,win);
                                var ck = 'Decodificando respuesta al guardar proceso';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### mov tflujomc:',json);
                                    if(json.success==true)
                                    {
                                        ck = 'Guardando permisos';
                                        
                                        var jsonPerm =
                                        {
                                            params :
                                            {
                                                cdtipflu   : json.params.CDTIPFLU
                                                ,cdflujomc : json.params.CDFLUJOMC
                                            }
                                            ,list : []
                                        };
                                        
                                        store.each(function(r)
                                        {
                                            if(r.get('SWACTIVO') === true)
                                            {
                                                jsonPerm.list.push(
                                                {
                                                    CDTIPFLU   : json.params.CDTIPFLU
                                                    ,CDFLUJOMC : json.params.CDFLUJOMC
                                                    ,CDSISROL  : r.get('CDSISROL')
                                                    ,SWACTIVO  : 'S'
                                                });
                                            }
                                        });
                                        
                                        debug('jsonPerm:',jsonPerm,'.');
                                        
                                        _mask(ck);
                                        Ext.Ajax.request(
                                        {
                                            url       : _p52_urlGuardarTflujorol
                                            ,jsonData : jsonPerm
                                            ,success  : function(response)
                                            {
                                                _unmask();
                                                var ck = 'Decodificando respuesta al guardar permisos de rol';
                                                try
                                                {
                                                    var jsonRespPerm = Ext.decode(response.responseText);
                                                    debug('### guardar ttipflurol:',jsonRespPerm,'.');
                                                    if(jsonRespPerm.success === true)
                                                    {
                                                        win.hide();
                                                        _p52_gridProcesos.store.reload();
                                                        
                                                        if(!_p52_panelDibujo.isHidden())
                                                        {
                                                            _p52_panelDibujo.setTitle(json.params.DSFLUJOMC);
                                                            _p52_selectedFlujo.set('DSFLUJOMC' , json.params.DSFLUJOMC);
                                                            _p52_selectedFlujo.set('SWFINAL'   , json.params.SWFINAL);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        mensajeError(jsonRespPerm.message);
                                                    }
                                                }
                                                catch(e)
                                                {
                                                    manejaException(e,ck);
                                                }
                                            }
                                            ,failure  : function()
                                            {
                                                _unmask();
                                                errorComunicacion(null,'Error al guardar permisos de proceso');
                                            }
                                        });
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
                                _setLoading(false,win);
                                errorComunicacion(null,'Error guardando proceso');
                            }
                        });
                    }
                    catch(e)
                    {
                        _setLoading(false,win);
                        manejaException(e,ck);
                    }
                }
            }
        ]
    });
    
    _p52_formEstado = Ext.create('Ext.window.Window',
    {
        title        : 'STATUS'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '_ACCION'
                        ,name       : 'ACCION'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDESTADOMC'
                        ,name       : 'CDESTADOMC'
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'NOMBRE'
                        ,name       : 'DSESTADOMC'
                        ,allowBlank : false
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p52_guardarCatalogo(me,'E');
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var ck = 'Mostrando formulario';
            try
            {
                var me = this;
                me.down('form').getForm().reset();
                me.down('[name=ACCION]').setValue('I');
                centrarVentanaInterna(me.show());
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,showEdit : function(data)
        {
            var me = this;
            me.down('form').getForm().loadRecord(data);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
    _p52_formComponente = Ext.create('Ext.window.Window',
    {
        title        : 'COMPONENTE'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '_ACCION'
                        ,name       : 'ACCION'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDCOMPMC'
                        ,name       : 'CDCOMPMC'
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'NOMBRE'
                        ,name       : 'DSCOMPMC'
                        ,allowBlank : false
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'CLASE'
                        ,name       : 'NOMCOMP'
                        ,allowBlank : false
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p52_guardarCatalogo(me,'C');
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var ck = 'Mostrando formulario';
            try
            {
                var me = this;
                me.down('form').getForm().reset();
                me.down('[name=ACCION]').setValue('I');
                centrarVentanaInterna(me.show());
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,showEdit : function(data)
        {
            var me = this;
            me.down('form').getForm().loadRecord(data);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
    _p52_formPantalla = Ext.create('Ext.window.Window',
    {
        title        : 'PANTALLA'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '_ACCION'
                        ,name       : 'ACCION'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDPANTMC'
                        ,name       : 'CDPANTMC'
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'NOMBRE'
                        ,name       : 'DSPANTMC'
                        ,width      : 500
                        ,allowBlank : false
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'URL'
                        ,name       : 'URLPANTMC'
                        ,width      : 500
                        ,allowBlank : false
                    }
                    ,{
                        xtype       : 'fieldcontainer'
                        ,fieldLabel : 'PROPIEDADES'
                        ,items      :
                        [
                            {
                                xtype       : 'checkbox'
                                ,boxLabel   : 'EXTERNA'
                                ,name       : 'SWEXTERNA'
                                ,inputValue : 'S'
                            }
                        ]
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                           _p52_guardarCatalogo(me,'P');
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var ck = 'Mostrando formulario';
            try
            {
                var me = this;
                me.down('form').getForm().reset();
                me.down('[name=ACCION]').setValue('I');
                centrarVentanaInterna(me.show());
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,showEdit : function(data)
        {
            var me = this;
            me.down('form').getForm().loadRecord(data);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
    _p52_formProceso = Ext.create('Ext.window.Window',
    {
        title        : 'PROCESO'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    {
                        xtype       : 'textfield'
                        ,fieldLabel : '_ACCION'
                        ,name       : 'ACCION'
                        ,allowBlank : false
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : '_CDPROCMC'
                        ,name       : 'CDPROCMC'
                        ,hidden     : !_p52_debug
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'NOMBRE'
                        ,name       : 'DSPROCMC'
                        ,width      : 500
                        ,allowBlank : false
                    }
                    ,{
                        xtype       : 'textfield'
                        ,fieldLabel : 'URL'
                        ,name       : 'URLPROCMC'
                        ,width      : 500
                        ,allowBlank : false
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p52_guardarCatalogo(me,'O');
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var ck = 'Mostrando formulario';
            try
            {
                var me = this;
                me.down('form').getForm().reset();
                me.down('[name=ACCION]').setValue('I');
                centrarVentanaInterna(me.show());
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,showEdit : function(data)
        {
            var me = this;
            me.down('form').getForm().loadRecord(data);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
    _p52_formTdocume = Ext.create('Ext.window.Window',
    {
        title        : 'DOCUMENTO'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    <s:property value="items.tdocumeFormItems" escapeHtml="false" />
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            var ck = 'Guardando';
                            try
                            {
                                var win  = me.up('window');
                                var form = me.up('form').getForm();
                                if(!form.isValid())
                                {
                                    throw 'Favor de revisar los datos';
                                }
                                
                                _setLoading(true,win);
                                Ext.Ajax.request(
                                {
                                    url      : _p52_urlMovimientoTdocume
                                    ,params  : _formValuesToParams(form.getValues())
                                    ,success : function(response)
                                    {
                                        _setLoading(false,win);
                                        var ck = 'Decodificando respuesta al guardar documento';
                                        try
                                        {
                                            var json = Ext.decode(response.responseText);
                                            debug('### mov tdocume:',json);
                                            if(json.success==true)
                                            {
                                                win.hide();
                                                _p52_panelRevision.hide();
                                                _p52_panelCanvas.enable();
                                                _fieldById('_p52_gridRevDoc').store.reload();
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
                                        _setLoading(false,win);
                                        errorComunicacion(null,'Error guardando documento');
                                    }
                                });
                            }
                            catch(e)
                            {
                                _setLoading(false,win);
                                manejaException(e,ck);
                            }
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var me = this;
            me.down('form').getForm().reset();
            me.down('[name=ACCION]').setValue('I');
            centrarVentanaInterna(me.show());
        }
        ,showEdit : function(record)
        {
            var me = this;
            me.down('form').getForm().loadRecord(record);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
    _p52_formTrequisi = Ext.create('Ext.window.Window',
    {
        title        : 'REQUISITO'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,border  : 0
                ,items   :
                [
                    <s:property value="items.trequisiFormItems" escapeHtml="false" />
                    ,{
                        xtype       : 'fieldcontainer'
                        ,fieldLabel : 'PROPIEDADES'
                        ,items      :
                        [
                            {
                                xtype       : 'checkbox'
                                ,boxLabel   : 'CAPTURAR DATO'
                                ,name       : 'SWPIDEDATO'
                                ,inputValue : 'S'
                            }
                        ]
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            var ck = 'Guardando';
                            try
                            {
                                var win  = me.up('window');
                                var form = me.up('form').getForm();
                                if(!form.isValid())
                                {
                                    throw 'Favor de revisar los datos';
                                }
                                
                                _setLoading(true,win);
                                Ext.Ajax.request(
                                {
                                    url      : _p52_urlMovimientoTrequisi
                                    ,params  : _formValuesToParams(form.getValues())
                                    ,success : function(response)
                                    {
                                        _setLoading(false,win);
                                        var ck = 'Decodificando respuesta al guardar requisito';
                                        try
                                        {
                                            var json = Ext.decode(response.responseText);
                                            debug('### mov trequisi:',json);
                                            if(json.success==true)
                                            {
                                                win.hide();
                                                _p52_panelRevision.hide();
                                                _p52_panelCanvas.enable();
                                                _fieldById('_p52_gridRevReq').store.reload();
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
                                        _setLoading(false,win);
                                        errorComunicacion(null,'Error guardando requisito');
                                    }
                                });
                            }
                            catch(e)
                            {
                                _setLoading(false,win);
                                manejaException(e,ck);
                            }
                        }
                    }
                ]
            })
        ]
        ,showNew : function()
        {
            var me = this;
            me.down('form').getForm().reset();
            me.down('[name=ACCION]').setValue('I');
            centrarVentanaInterna(me.show());
        }
        ,showEdit : function(record)
        {
            var me = this;
            me.down('form').getForm().loadRecord(record);
            me.down('[name=ACCION]').setValue('U');
            centrarVentanaInterna(me.show());
        }
    });
    
 	_p52_winVarsCorreo = Ext.create('Ext.window.Window',{
		title        : 'VARIABLES'
		,modal       : true
		,closeAction : 'hide'
  		,items       : [
		                new SelectorMultiple()
		                ]
		,buttonAlign : 'center'		                
 		,buttons     : [
 		                {
 		                	text  : 'Guardar',
 		                	icon  : _GLOBAL_DIRECTORIO_ICONOS+'disk.png',
 		                	handler : function(me){
 		                		var win = me.up('window');
 		                		var nameVar   = win.nameCmpVar;
 		                		var name      = win.nameCmp;
 		                		var vars      = win.down('[itemId=panVars]').getValue().split(',');
 		                		var numvars   = _p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name='+name+']').getValue());
 		                		if (numvars != vars.length){
 		                			mensajeError('El numero de variables seleccionadas no coinciden con las llaves del texto');
 		                			return;
 		                		}
 		                		_p52_formCorreos.down('[name='+nameVar+']').setValue(win.down('[itemId=panVars]').getValue());
 		                		win.close();
 		                	}
 		                }
 		                ]
	}); 
	
	if (Number(_p52_params.cdtipmod) === 1) {
	    _p52_tituloPrincipal = 'C O N F I G U R A D O R&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D E&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;P R O C E S O S';
	} else if (Number(_p52_params.cdtipmod) === 2) {
        _p52_tituloPrincipal = 'C O N F I G U R A D O R&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D E&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;S U C U R S A L E S';
    } else {
        _p52_tituloPrincipal = '- E R R O R -';
    }
	
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo : '_p52_divpri'
        ,itemId  : '_p52_panelpri'
        ,title   : _p52_tituloPrincipal
        ,height  : 730
        ,border  : 0
        ,layout  : 'border'
        ,header  :
        {
            titleAlign     : 'center'
            ,titlePosition : 1
            ,items         :
            [
                {
                    xtype    : 'button'
                    ,text    : 'Regresar'
                    ,icon    : '${icons}arrow_left.png'
                    ,handler : function(){ history.back(); }
                }
                ,{
                    xtype    : 'button'
                    ,text    : 'Recargar'
                    ,icon    : '${icons}control_repeat_blue.png'
                    ,handler : function(){ _mask('Cargando...'); location.reload(); }
                }
            ]
        }
        ,items      :
        [
            Ext.create('Ext.panel.Panel',
            {
                itemId  : '_p52_panelGrids'
                ,region : 'north'
                ,layout : 'border'
                ,style  : 'margin-top : 5px;'
                ,height : 200
                ,split  : true
                ,items  :
                [
                    Ext.create('Ext.grid.Panel',
                    {
                        itemId       : '_p52_gridTramites'
                        ,title       : 'TR\u00C1MITES'
                        ,region      : 'west'
                        ,width       : 400
                        ,split       : true
                        ,header      :
                        {
                            titlePosition : 0
                            ,items        :
                            [{
                                xtype    : 'button'
                                ,text    : 'Agregar'
                                ,icon    : '${icons}add.png'
                                ,hidden  : Number(_p52_params.cdtipmod) !== 1
                                ,handler : function(){ _p52_formTtipflumc.showNew(); }
                            }]
                        }
                        ,hideHeaders : true
                        ,columns     :
                        [
                            {
                                dataIndex : 'DSTIPFLU'
                                ,flex     : 1
                            }
                            ,{
                                xtype    : 'actioncolumn'
                                ,width   : 30
                                ,icon    : '${icons}pencil.png'
                                ,tooltip : 'Editar'
                                ,hidden  : Number(_p52_params.cdtipmod) !== 1
                                ,handler : function(me,row,col,item,e,record)
                                {
                                    _p52_formTtipflumc.showEdit(record);
                                }
                            }
                        ]
                        ,store : Ext.create('Ext.data.Store',
                        {
                            autoLoad : true
                            ,fields  :
                            [
                                'CDTIPFLU'
                                ,'DSTIPFLU'
                                ,'CDTIPTRA'
                                ,'CDTIPSUP'
                                ,'SWMULTIPOL'
                                ,'SWREQPOL'
                                ,'CDTIPMOD'
                                ,'SWEXTERNO'
                            ]
                            ,proxy   :
                            {
                                url          : _p52_urlRecuperacion
                                ,type        : 'ajax'
                                ,extraParams :
                                {
                                    'params.consulta' : 'RECUPERAR_TTIPFLUMC',
                                    'params.cdtipmod' : _p52_params.cdtipmod
                                }
                                ,reader      :
                                {
                                    type             : 'json'
                                    ,root            : 'list'
                                    ,messageProperty : 'message'
                                }
                            }
                        })
                        ,listeners :
                        {
                            select : function(me,record)
                            {
                                _p52_gridProcesos.store.load(
                                {
                                    params :
                                    {
                                        'params.cdtipflu' : record.get('CDTIPFLU')
                                    }
                                });
                            }
                        }
                    })
                    ,Ext.create('Ext.grid.Panel',
                    {
                        itemId       : '_p52_gridProcesos'
                        ,title       : 'PROCESOS'
                        ,region      : 'center'
                        ,header      :
                        {
                            titlePosition : 0
                            ,items        :
                            [{
                                xtype    : 'button'
                                ,text    : 'Agregar'
                                ,icon    : '${icons}add.png'
                                ,hidden  : Number(_p52_params.cdtipmod) !== 1
                                ,handler : function(me){ _p52_formTflujomc.showNew(); }
                            }]
                        }
                        ,hideHeaders : true
                        ,columns     :
                        [
                            {
                                dataIndex : 'DSFLUJOMC'
                                ,flex     : 1
                            }
                            ,{
                                xtype   : 'actioncolumn'
                                ,width  : 30
                                ,hidden : Number(_p52_params.cdtipmod) !== 1
                                ,items  :
                                [
                                    {
                                        icon    : '${icons}pencil.png'
                                        ,tooltip : 'Editar'
                                        ,handler : function(view,row,col,item,e,record)
                                        {
                                            _p52_formTflujomc.showEdit(record);
                                        }
                                    }
                                ]
                            }
                            ,{
                                xtype  : 'actioncolumn'
                                ,width : 30
                                ,items :
                                [
                                    {
                                        icon     : '${icons}chart_line.png'
                                        ,tooltip : 'Modelar'
                                        ,handler : function(view,row,col,item,e,record)
                                        {
                                            _p52_selectedFlujo = record;
                                            _p52_panelDibujo.setTitle(record.get('DSFLUJOMC'));
                                            _p52_navega(2);
                                            _p52_cargarModelado();
                                        }
                                    }
                                ]
                            }
                        ]
                        ,store : Ext.create('Ext.data.Store',
                        {
                            autoLoad : false
                            ,fields  :
                            [
                                'CDTIPFLU'
                                ,'CDFLUJOMC'
                                ,'DSFLUJOMC'
                                ,'SWFINAL'
                                ,'CDTIPRAM'
                                ,'SWGRUPO'
                            ]
                            ,proxy   :
                            {
                                type         : 'ajax'
                                ,url         : _p52_urlRecuperacion
                                ,extraParams :
                                {
                                    'params.consulta' : 'RECUPERAR_TFLUJOMC'
                                }
                                ,reader      :
                                {
                                    type             : 'json'
                                    ,root            : 'list'
                                    ,messageProperty : 'message'
                                }
                            }
                        })
                    })
                ]
            })
            ,Ext.create('Ext.panel.Panel',
            {
                itemId      : '_p52_panelDibujo'
                ,title      : ''
                ,titleAlign : 'center'
                ,region     : 'center'
                ,hidden     : true
                ,layout     : 'border'
                ,border     : 0
                ,tools      :
                [{
                    type     : 'gear'
                    ,tooltip : 'Editar'
                    ,handler : function()
                    {
                        var ck = 'Editando proceso';
                        try
                        {
                            _p52_formTflujomc.showEdit(_p52_selectedFlujo);
                        }
                        catch(e)
                        {
                            manejaException(e,ck);
                        }
                    }
                }]
                ,items :
                [
                    Ext.create('Ext.panel.Panel',
                    {
                        itemId  : '_p52_accordion'
                        ,width  : 250
                        ,region : 'west'
                        ,style  : 'margin-right:5px;'
                        ,layout :
                        {
                            type           : 'accordion'
                            ,titleCollapse : true
                            ,animate       : true
                        }
                        ,items :
                        [
                            {
                                title       : 'ESTATUS'
                                ,itemId     : '_p52_catalogoEstados'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [
                                    {
                                        type      : 'collapse'
                                        ,tooltip  : 'Agregar'
                                        ,callback : function(panel)
                                        {
                                            _p52_editCatClic('E');
                                        }
                                    }
                                    ,{
                                        type     : 'help'
                                        ,tooltip : 'Tips'
                                        ,handler : function()
                                        {
                                            _p52_ventanaTips([
                                                'Restricci\u00f3n al agregar: solo se puede agregar una vez.'
                                            ]);
                                        }
                                    }
                                ]
                            }
                            ,{
                                title       : 'PANTALLAS'
                                ,itemId     : '_p52_catalogoPantallas'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [{
                                    type      : 'collapse'
                                    ,tooltip  : 'Agregar'
                                    ,callback : function(panel)
                                    {
                                        _p52_editCatClic('P');
                                    }
                                }]
                            }
                            ,{
                                title       : 'COMPONENTES'
                                ,itemId     : '_p52_catalogoComponentes'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [{
                                    type      : 'collapse'
                                    ,tooltip  : 'Agregar'
                                    ,callback : function(panel)
                                    {
                                        _p52_editCatClic('C');
                                    }
                                }]
                            }
                            ,{
                                title       : 'PROCESOS'
                                ,itemId     : '_p52_catalogoProcesos'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [
                                    {
                                        type      : 'collapse'
                                        ,tooltip  : 'Agregar'
                                        ,callback : function(panel)
                                        {
                                            _p52_editCatClic('O');
                                        }
                                    }
                                    ,{
                                        type     : 'help'
                                        ,tooltip : 'Tips'
                                        ,handler : function()
                                        {
                                            _p52_ventanaTips([
                                                'Conexiones de salida:'
                                                ,'- Puede no tener conexiones,'
                                                ,'- Puede tener una sola conexi\u00f3n sin valor,'
                                                ,'- Puede tener dos conexiones, una con valor EXITO y otra con valor ERROR.'
                                            ]);
                                        }
                                    }
                                ]
                            }
                            ,{
                                title       : 'VALIDACIONES'
                                ,itemId     : '_p52_catalogoValidaciones'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [{
                                    type     : 'help'
                                    ,tooltip : 'Tips'
                                    ,handler : function()
                                    {
                                        _p52_ventanaTips([
                                            'Conexiones de salida:'
                                            ,'- Debe tener al menos una conexi\u00f3n,'
                                            ,'- Todas sus conexiones deben tener valor.'
                                        ]);
                                    }
                                }]
                            }
                            ,{
                                title       : 'REVISIONES'
                                ,itemId     : '_p52_catalogoRevisiones'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [{
                                    type     : 'help'
                                    ,tooltip : 'Tips'
                                    ,handler : function()
                                    {
                                        _p52_ventanaTips([
                                            'Conexiones de salida:'
                                            ,'- Debe tener al menos una conexi\u00f3n,'
                                            ,'- Puede tener una sola conexi\u00f3n sin valor,'
                                            ,'- Puede tener dos conexiones, una con valor EXITO y otra con valor ERROR.'
                                        ]);
                                    }
                                }]
                            }
                            ,{
                                title       : 'CORREOS'
                                ,itemId     : '_p52_catalogoCorreos'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 1
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : {valign:'top'}
                                }
                            }
                            ,{
                                title       : 'SUCURSALES'
                                ,itemId     : '_p52_catalogoSucursales'
                                ,hidden     : Number(_p52_params.cdtipmod) !== 2
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                            }
                            ,{
                                title       : 'T\u00CDTULOS'
                                ,itemId     : '_p52_catalogoTitulos'
                                ,defaults   : { style : 'margin : 5px;' }
                                ,autoScroll : true
                                ,layout     :
                                {
                                    type     : 'table'
                                    ,columns : 2
                                    ,tdAttrs : { valign : 'top' }
                                }
                                ,tools :
                                [{
                                    type     : 'help'
                                    ,tooltip : 'Tips'
                                    ,handler : function()
                                    {
                                        _p52_ventanaTips([
                                            'Se utilizan para dividir o seccionar el flujo'
                                        ]);
                                    }
                                }]
                            }
                        ]
                    })
                    ,Ext.create('Ext.panel.Panel',
                    {
                        itemId      : '_p52_propForm'
                        ,width      : 300
                        ,style      : 'margin-left:5px;'
                        ,region     : 'east'
                        ,autoScroll : true
                        ,items      :
                        [
                            Ext.create('Ext.panel.Panel',
                            {
                                itemId       : '_p52_panelEstado'
                                ,title       : 'STATUS'
                                ,hidden      : true
                                ,border      : 0
                                ,buttonAlign : 'center'
                                ,tools       : [
                                    {
                                        type    : 'unpin',
                                        tooltip : 'Copiar datos',
                                        handler : function (event, toolEl, owner, tool) {
                                            var ck = 'Copiando propiedades de estatus';
                                            try {
                                                var panel = tool.up('panel');
                                                debug('panel:', panel);
                                                var store = panel.down('grid').getStore();
                                                var roles = {};
                                                store.each(function (record) {
                                                    roles[record.get('CDSISROL')] = {
                                                        SWVER     : record.get('SWVER'),
                                                        SWVERDEF  : record.get('SWVERDEF'),
                                                        SWTRABAJO : record.get('SWTRABAJO'),
                                                        SWCOMPRA  : record.get('SWCOMPRA'),
                                                        SWREASIG  : record.get('SWREASIG')
                                                    };
                                                });
                                                var props = {
                                                    roles : roles
                                                };
                                                var names = [
                                                    'TIMEWRN1H',
                                                    'TIMEWRN1M',
                                                    'TIMEWRN2H',
                                                    'TIMEWRN2M',
                                                    'TIMEMAXH',
                                                    'TIMEMAXM',
                                                    'CDTIPASIG',
                                                    'STATUSOUT',
                                                    'CDETAPA',
                                                    'SWFINNODE'
                                                ];
                                                var values = panel.down('form').getValues();
                                                debug('values:', values);
                                                for (var i = 0; i < names.length; i++) {
                                                    var name = names[i];
                                                    props[name] = values[name];
                                                }
                                                debug('props:', props);
                                                var encode = Ext.encode(props);
                                                debug('encode:', encode);
                                                executeCopy(encode);
                                            } catch (e) {
                                                manejaException(e, ck);
                                            }
                                        }
                                    }, {
                                        type    : 'pin',
                                        tooltip : 'Pegar datos',
                                        handler : function (event, toolEl, owner, tool) {
                                            var ck = 'Pegando propiedades de estatus';
                                            try {
                                                var encode = window.prompt("Pegar: Ctrl+V, Enter");
                                                if (Ext.isEmpty(encode)) {
                                                    return;
                                                }
                                                debug('encode:', encode);
                                                var decode = Ext.decode(encode);
                                                debug('decode:', decode);
                                                var panel = tool.up('panel');
                                                debug('panel:', panel);
                                                var names = [
                                                    'TIMEWRN1H',
                                                    'TIMEWRN1M',
                                                    'TIMEWRN2H',
                                                    'TIMEWRN2M',
                                                    'TIMEMAXH',
                                                    'TIMEMAXM',
                                                    'STATUSOUT',
                                                    'CDETAPA',
                                                    'SWFINNODE'
                                                ];
                                                for (var i = 0; i < names.length; i++) {
                                                    var name = names[i];
                                                    panel.down('[name=' + name + ']').setValue(decode[name]);
                                                }
                                                if (!Ext.isEmpty(decode.CDTIPASIG)) {
                                                    Ext.ComponentQuery.query('[name=CDTIPASIG][inputValue=1]')[0].setValue(decode.CDTIPASIG);
                                                }
                                                var store = panel.down('grid').getStore();
                                                store.each(function (record) {
                                                    record.set('SWVER'     , decode.roles[record.get('CDSISROL')].SWVER     || false);
                                                    record.set('SWVERDEF'  , decode.roles[record.get('CDSISROL')].SWVERDEF  || false);
                                                    record.set('SWTRABAJO' , decode.roles[record.get('CDSISROL')].SWTRABAJO || false);
                                                    record.set('SWCOMPRA'  , decode.roles[record.get('CDSISROL')].SWCOMPRA  || false);
                                                    record.set('SWREASIG'  , decode.roles[record.get('CDSISROL')].SWREASIG  || false);
                                                });
                                            } catch (e) {
                                                manejaException(e, ck);
                                            }
                                        }
                                    }
                                ]
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosEstado(me,function(me)
                                            {
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items       :
                                [
                                    {
                                        xtype     : 'form'
                                        ,defaults : { style : 'margin:5px;' }
                                        ,border   : 0
                                        ,layout   : {
                                            type     : 'table'
                                            ,columns : 2
                                        }
                                        ,items    :
                                        [
                                            {
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_ACCION'
                                                ,name       : 'ACCION'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDTIPFLU'
                                                ,name       : 'CDTIPFLU'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDFLUJOMC'
                                                ,name       : 'CDFLUJOMC'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDESTADOMC'
                                                ,name       : 'CDESTADOMC'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_WEBID'
                                                ,name       : 'WEBID'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_XPOS'
                                                ,name       : 'XPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_YPOS'
                                                ,name       : 'YPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Alerta 1 horas'
                                                ,minValue   : 0
                                                ,maxValue   : 40
                                                ,increment  : 1
                                                ,name       : 'TIMEWRN1H'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}flag_yellow.png'
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Alerta 1 minutos'
                                                ,minValue   : 0
                                                ,maxValue   : 55
                                                ,increment  : 5
                                                ,name       : 'TIMEWRN1M'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}flag_yellow.png'
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Alerta 2 horas'
                                                ,minValue   : 0
                                                ,maxValue   : 40
                                                ,increment  : 1
                                                ,name       : 'TIMEWRN2H'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}flag_red.png'
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Alerta 2 minutos'
                                                ,minValue   : 00
                                                ,maxValue   : 55
                                                ,increment  : 5
                                                ,name       : 'TIMEWRN2M'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}flag_red.png'
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Tiempo m\u00e1ximo horas'
                                                ,minValue   : 0
                                                ,maxValue   : 40
                                                ,increment  : 1
                                                ,name       : 'TIMEMAXH'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}clock_red.png'
                                            }
                                            ,{
                                                xtype       : 'numberfield'
                                                ,fieldLabel : 'Tiempo m\u00e1ximo minutos'
                                                ,minValue   : 0
                                                ,maxValue   : 55
                                                ,increment  : 5
                                                ,name       : 'TIMEMAXM'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype : 'image'
                                                ,src  : '${icons}clock_red.png'
                                            }
                                            ,{
                                                xtype        : 'fieldcontainer'
                                                ,fieldLabel  : 'Tipo de asignaci\u00f3n'
                                                ,labelAlign  : 'top'
                                                ,colspan     : 2
                                                ,width       : 200
                                                ,defaultType : 'radiofield'
                                                ,defaults    : { flex : 1 }
                                                ,layout      : 'hbox'
                                                ,items       :
                                                [
                                                    {
                                                        boxLabel    : 'Simple'
                                                        ,name       : 'CDTIPASIG'
                                                        ,inputValue : 1
                                                    }
                                                    ,{
                                                        boxLabel    : 'Propio'
                                                        ,name       : 'CDTIPASIG'
                                                        ,inputValue : 2
                                                    }
                                                    ,{
                                                        boxLabel    : 'Carrusel'
                                                        ,name       : 'CDTIPASIG'
                                                        ,inputValue : 3
                                                    }
                                                    ,{
                                                        boxLabel    : 'Carga'
                                                        ,name       : 'CDTIPASIG'
                                                        ,inputValue : '4'
                                                    }
                                                ]
                                            },
                                            {
                                                xtype            : 'combo'
                                                ,fieldLabel      : 'Estatus al vencer'
                                                ,allowBlank      : false
                                                ,colspan         : 2
                                                ,valueField      : 'key'
                                                ,name            : 'STATUSOUT'
                                                ,displayField    : 'value'
                                                ,matchFieldWidth : false
                                                ,forceSelection  : true
                                                ,listConfig      : {
                                                    maxHeight : 150
                                                    ,minWidth : 120
                                                }
                                                ,queryMode       : 'local'
                                                ,store           : Ext.create('Ext.data.Store', {
                                                    model : 'Generic',
                                                    data  : [
                                                        {
                                                            key   : '-1',
                                                            value : 'NINGUNO'
                                                        }, {
                                                            key   : '999',
                                                            value : 'VENCIDO'
                                                        }, {
                                                            key   : '4',
                                                            value : 'RECHAZADO'
                                                        }
                                                    ]
                                                })
                                            }
                                            ,<s:property value="items.comboEtapa" escapeHtml="false" />
                                            ,{border : 0}
                                            ,{
                                                xtype       : 'fieldcontainer'
                                                ,fieldLabel : 'Propiedades'
                                                ,colspan    : 2
                                                ,items      :
                                                [
                                                    {
                                                        xtype       : 'checkbox'
                                                        ,boxLabel   : 'Estado final'
                                                        ,name       : 'SWFINNODE'
                                                        ,inputValue : 'S'
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    ,Ext.create('Ext.grid.Panel',
                                    {
                                        itemId   : '_p52_gridEstRol'
                                        ,title   : 'PERMISOS'
                                        ,height  : 220
                                        ,columns :
                                        [
                                            {
                                                text       : 'ROL'
                                                ,dataIndex : 'DSSISROL'
                                                ,width     : 170
                                            }
                                            ,{
                                                text       : 'VER'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWVER'
                                                ,width     : 55
                                            }
                                            ,{
                                                text       : 'INICIO'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWVERDEF'
                                                ,width     : 55
                                            }
                                            ,{
                                                text       : 'TRAB.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWTRABAJO'
                                                ,width     : 55
                                            }
                                            ,{
                                                text       : 'COMPR.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWCOMPRA'
                                                ,width     : 55
                                            }
                                            ,{
                                                text       : 'REASI.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWREASIG'
                                                ,width     : 55
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            autoLoad : true
                                            ,fields  :
                                            [
                                                'CDSISROL'
                                                ,'DSSISROL'
                                                ,{ name : 'SWVER'     , type : 'boolean' }
                                                ,{ name : 'SWTRABAJO' , type : 'boolean' }
                                                ,{ name : 'SWVERDEF'  , type : 'boolean' }
                                                ,{ name : 'SWCOMPRA'  , type : 'boolean' }
                                                ,{ name : 'SWREASIG'  , type : 'boolean' }
                                                ,'CDROLASIG'
                                            ]
                                            ,proxy   :
                                            {
                                                type         : 'ajax'
                                                ,url         : _p52_urlRecuperacion
                                                ,extraParams :
                                                {
                                                    'params.consulta' : 'RECUPERAR_ROLES'
                                                }
                                                ,reader      :
                                                {
                                                    type  : 'json'
                                                    ,root : 'list'
                                                }
                                            }
                                        })
                                    })
                                    ,Ext.create('Ext.grid.Panel',
                                    {
                                        itemId   : '_p52_gridEstAvi'
                                        ,title   : 'AVISOS'
                                        ,height  : 180
                                        ,hidden  : true
                                        ,plugins :
                                        [
                                            Ext.create('Ext.grid.plugin.CellEditing',
                                            {
                                                clicksToEdit: 1
                                            })
                                        ]
                                        ,tools :
                                        [{
                                            type      : 'plus'
                                            ,tooltip  : 'Agregar'
                                            ,callback : function(panel)
                                            {
                                                debug('panel.',panel);
                                                panel.store.add({});
                                            }
                                        }]
                                        ,columns :
                                        [
                                            {
                                                text       : 'CORREO'
                                                ,dataIndex : 'DSMAILAVI'
                                                ,flex      : 1
                                                ,editor    : 'textfield'
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            autoLoad : true
                                            ,fields  :
                                            [
                                                'CDAVISO'
                                                ,'CDTIPAVI'
                                                ,'DSCOMENT'
                                                ,'SWAUTOAVI'
                                                ,'DSMAILAVI'
                                                ,'CDUSUARIAVI'
                                                ,'CDSISROLAVI'
                                            ]
                                            ,proxy   :
                                            {
                                                type    : 'memory'
                                                ,reader : 'json'
                                                ,data   :
                                                [
                                                ]
                                            }
                                        })
                                    })
                                ]
                            })
                            ,Ext.create('Ext.form.Panel',
                            {
                                itemId       : '_p52_formValidacion'
                                ,title       : 'VALIDACI\u00D3N'
                                ,defaults    : { style : 'margin:5px;' }
                                ,buttonAlign : 'center'
                                ,hidden      : true
                                ,tools       :
                                [{
                                    type     : 'help'
                                    ,tooltip : 'Estructura de DATOS para validaci\u00f3n cliente'
                                    ,handler : function()
                                    {
                                        _p52_ventanaTips(
                                        [
                                            'La estructura de los DATOS para validaci\u00f3n cliente es:'
                                            ,'CDFLUJOMC: "12"'
                                            ,'CDRAMO: "2"'
                                            ,'CDSISROL: "MESADECONTROL"'
                                            ,'CDTIPFLU: "1"'
                                            ,'CDUNIECO: "1000"'
                                            ,'CDUSUARI: "A100"'
                                            ,'CLAVEENT: "27"'
                                            ,'ESTADO: "W"'
                                            ,'NMPOLIZA: ""'
                                            ,'NMSITUAC: ""'
                                            ,'NMSUPLEM: ""'
                                            ,'NTRAMITE: "13892"'
                                            ,'STATUS: "2"'
                                            ,'TIPOENT: "V"'
                                            ,'TRAMITE.CDAGENTE: "11000"'
                                            ,'TRAMITE.CDFLUJOMC: "12"'
                                            ,'TRAMITE.CDRAMO: "2"'
                                            ,'TRAMITE.CDSUBRAM: "213"'
                                            ,'TRAMITE.CDSUCADM: "1000"'
                                            ,'TRAMITE.CDSUCDOC: "1000"'
                                            ,'TRAMITE.CDTIPFLU: "1"'
                                            ,'TRAMITE.CDTIPSIT: "SL"'
                                            ,'TRAMITE.CDTIPSUP: "1"'
                                            ,'TRAMITE.CDTIPTRA: "1"'
                                            ,'TRAMITE.CDUNIECO: "1000"'
                                            ,'TRAMITE.CDUSUARI: "A100"'
                                            ,'TRAMITE.COMMENTS: "observaciones"'
                                            ,'TRAMITE.ESTADO: "W"'
                                            ,'TRAMITE.FECSTATU: "17/12/2015"'
                                            ,'TRAMITE.FERECEPC: "17/12/2015"'
                                            ,'TRAMITE.NMPOLIZA: null'
                                            ,'TRAMITE.NMSOLICI: null'
                                            ,'TRAMITE.NMSUPLEM: null'
                                            ,'TRAMITE.NOMBRE: "PROSPECTO"'
                                            ,'TRAMITE.NTRAMITE: "13892"'
                                            ,'TRAMITE.OTVALOR01: null'
                                            ,'TRAMITE.OTVALOR02: null'
                                            ,'TRAMITE.OTVALOR03: null'
                                            ,'TRAMITE.OTVALOR04: null'
                                            ,'TRAMITE.OTVALOR05: null'
                                            ,'TRAMITE.OTVALOR06: null'
                                            ,'TRAMITE.OTVALOR07: null'
                                            ,'TRAMITE.OTVALOR08: null'
                                            ,'TRAMITE.OTVALOR09: null'
                                            ,'TRAMITE.OTVALOR10: null'
                                            ,'TRAMITE.OTVALOR11: null'
                                            ,'TRAMITE.OTVALOR12: null'
                                            ,'TRAMITE.OTVALOR13: null'
                                            ,'TRAMITE.OTVALOR14: null'
                                            ,'TRAMITE.OTVALOR15: null'
                                            ,'TRAMITE.OTVALOR16: null'
                                            ,'TRAMITE.OTVALOR17: null'
                                            ,'TRAMITE.OTVALOR18: null'
                                            ,'TRAMITE.OTVALOR19: null'
                                            ,'TRAMITE.OTVALOR20: null'
                                            ,'TRAMITE.OTVALOR21: null'
                                            ,'TRAMITE.OTVALOR22: null'
                                            ,'TRAMITE.OTVALOR23: null'
                                            ,'TRAMITE.OTVALOR24: null'
                                            ,'TRAMITE.OTVALOR25: null'
                                            ,'TRAMITE.OTVALOR26: null'
                                            ,'TRAMITE.OTVALOR27: null'
                                            ,'TRAMITE.OTVALOR28: null'
                                            ,'TRAMITE.OTVALOR29: null'
                                            ,'TRAMITE.OTVALOR30: null'
                                            ,'TRAMITE.OTVALOR31: null'
                                            ,'TRAMITE.OTVALOR32: null'
                                            ,'TRAMITE.OTVALOR33: null'
                                            ,'TRAMITE.OTVALOR34: null'
                                            ,'TRAMITE.OTVALOR35: null'
                                            ,'TRAMITE.OTVALOR36: null'
                                            ,'TRAMITE.OTVALOR37: null'
                                            ,'TRAMITE.OTVALOR38: null'
                                            ,'TRAMITE.OTVALOR39: null'
                                            ,'TRAMITE.OTVALOR40: null'
                                            ,'TRAMITE.OTVALOR41: null'
                                            ,'TRAMITE.OTVALOR42: null'
                                            ,'TRAMITE.OTVALOR43: null'
                                            ,'TRAMITE.OTVALOR44: null'
                                            ,'TRAMITE.OTVALOR45: null'
                                            ,'TRAMITE.OTVALOR46: null'
                                            ,'TRAMITE.OTVALOR47: null'
                                            ,'TRAMITE.OTVALOR48: null'
                                            ,'TRAMITE.OTVALOR49: null'
                                            ,'TRAMITE.OTVALOR50: null'
                                            ,'TRAMITE.REFERENCIA: "1"'
                                            ,'TRAMITE.STATUS: "2"'
                                            ,'TRAMITE.SWIMPRES: null'
                                            ,'WEBID: "1450300336059_9079"'
                                        ]);
                                    }
                                }]
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosValidacion(me,function(me)
                                            {
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                                _p52_actualizaLabel(
                                                    'V'
                                                    ,_p52_formValidacion.down('[name=WEBID]').getValue()
                                                    ,_p52_formValidacion.down('[name=DSVALIDA]').getValue()
                                                );
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items :
                                [
                                    {
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_ACCION'
                                        ,name       : 'ACCION'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDTIPFLU'
                                        ,name       : 'CDTIPFLU'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDFLUJOMC'
                                        ,name       : 'CDFLUJOMC'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDVALIDA'
                                        ,name       : 'CDVALIDA'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_WEBID'
                                        ,name       : 'WEBID'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_XPOS'
                                        ,name       : 'XPOS'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_YPOS'
                                        ,name       : 'YPOS'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : 'NOMBRE'
                                        ,labelAlign : 'top'
                                        ,name       : 'DSVALIDA'
                                        ,allowBlank : false
                                    }
                                    ,{
                                        xtype          : 'textfield'
                                        ,fieldLabel    : 'VALIDACI\u00D3N'
                                        ,labelAlign    : 'top'
                                        ,name          : 'CDVALIDAFK'
                                        ,allowBlank    : false
                                        ,minLength     : 1
                                        ,maxLength     : 8
                                    }
                                    ,{
                                        xtype       : 'textarea'
                                        ,fieldLabel : 'VALIDACI\u00D3N CLIENTE'
                                        ,labelAlign : 'top'
                                        ,name       : 'JSVALIDA'
                                        ,width      : 270
                                        ,height     : 200
                                    }
                                ]
                            })
                            ,Ext.create('Ext.panel.Panel',
                            {
                                itemId       : '_p52_panelRevision'
                                ,title       : 'REVISI\u00D3N'
                                ,hidden      : true
                                ,buttonAlign : 'center'
                                ,border      : 0
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosRevision(me,function(me)
                                            {
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                                _p52_actualizaLabel(
                                                    'R'
                                                    ,_p52_panelRevision.down('[name=WEBID]').getValue()
                                                    ,_p52_panelRevision.down('[name=DSREVISI]').getValue()
                                                );
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items :
                                [
                                    {
                                        xtype     : 'form'
                                        ,border   : 0
                                        ,defaults : { style : 'margin:5px;' }
                                        ,items    :
                                        [
                                            {
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_ACCION'
                                                ,name       : 'ACCION'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDTIPFLU'
                                                ,name       : 'CDTIPFLU'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDFLUJOMC'
                                                ,name       : 'CDFLUJOMC'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDREVISI'
                                                ,name       : 'CDREVISI'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_WEBID'
                                                ,name       : 'WEBID'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_XPOS'
                                                ,name       : 'XPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_YPOS'
                                                ,name       : 'YPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : 'Nombre'
                                                //,labelAlign : 'top'
                                                ,name       : 'DSREVISI'
                                                ,allowBlank : false
                                            }
                                        ]
                                    }
                                    ,Ext.create('Ext.grid.Panel',
                                    {
                                        itemId    : '_p52_gridRevReq'
                                        ,title    : 'REQUISITOS'
                                        ,height   : 250
                                        ,tools    :
                                        [{
                                            type     : 'plus'
                                            ,tooltip : 'Agregar'
                                            ,handler : function()
                                            {
                                                centrarVentanaInterna(
                                                    Ext.MessageBox.confirm(
                                                        'Confirmar'
                                                        ,'Se perder\u00e1n los cambios no guardados en la revisi\u00f3n<br>¿Desea continuar?'
                                                        ,function(btn)
                                                        {
                                                            if(btn === 'yes')
                                                            {
                                                                _p52_formTrequisi.showNew();
                                                            }
                                                        }
                                                    )
                                                );
                                            }
                                        }]
                                        ,features :
                                        [{
                                            ftype           : 'groupingsummary'
                                            ,startCollapsed : true
                                            ,groupHeaderTpl :
                                            [
                                                '{name}'
                                            ]
                                        }]
                                        ,columns :
                                        [
                                            {
                                                text       : 'REQUISITO'
                                                ,dataIndex : 'DSREQUISI'
                                                ,flex      : 1
                                            }
                                            ,{
                                                xtype    : 'actioncolumn'
                                                ,tooltip : 'Editar'
                                                ,icon    : '${icons}pencil.png'
                                                ,width   : 30
                                                ,handler : function(me,row,col,item,e,record)
                                                {
                                                    centrarVentanaInterna(
                                                        Ext.MessageBox.confirm(
                                                            'Confirmar'
                                                            ,'Se perder\u00e1n los cambios no guardados en la revisi\u00f3n<br>¿Desea continuar?'
                                                            ,function(btn)
                                                            {
                                                                if(btn === 'yes')
                                                                {
                                                                    _p52_formTrequisi.showEdit(record);
                                                                }
                                                            }
                                                        )
                                                    );
                                                }
                                            }
                                            ,{
                                                text       : 'INC.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWLISTA'
                                                ,width     : 50
                                            }
                                            ,{
                                                text       : 'REQ.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWOBLIGA'
                                                ,width     : 50
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            autoLoad    : true
                                            ,groupField : 'DSTIPTRA'
                                            ,fields     :
                                            [
                                                'CDREQUISI'
                                                ,'DSREQUISI'
                                                ,'CDTIPTRA'
                                                ,'DSTIPTRA'
                                                ,{ name : 'SWLISTA'   , type : 'boolean' }
                                                ,{ name : 'SWOBLIGA'  , type : 'boolean' }
                                                ,'SWPIDEDATO'
                                            ]
                                            ,proxy   :
                                            {
                                                type         : 'ajax'
                                                ,url         : _p52_urlRecuperacion
                                                ,extraParams :
                                                {
                                                    'params.consulta' : 'RECUPERAR_TREQUISI'
                                                }
                                                ,reader      :
                                                {
                                                    type  : 'json'
                                                    ,root : 'list'
                                                }
                                            }
                                        })
                                    })
                                    ,Ext.create('Ext.grid.Panel',
                                    {
                                        itemId    : '_p52_gridRevDoc'
                                        ,title    : 'DOCUMENTOS'
                                        ,height   : 250
                                        ,border   : 0
                                        ,tools    :
                                        [{
                                            type     : 'plus'
                                            ,tooltip : 'Agregar'
                                            ,handler : function()
                                            {
                                                centrarVentanaInterna(
                                                    Ext.MessageBox.confirm(
                                                        'Confirmar'
                                                        ,'Se perder\u00e1n los cambios no guardados en la revisi\u00f3n<br>¿Desea continuar?'
                                                        ,function(btn)
                                                        {
                                                            if(btn === 'yes')
                                                            {
                                                                _p52_formTdocume.showNew();
                                                            }
                                                        }
                                                    )
                                                );
                                            }
                                        }]
                                        ,features :
                                        [{
                                            ftype           : 'groupingsummary'
                                            ,startCollapsed : true
                                            ,groupHeaderTpl :
                                            [
                                                '{name}'
                                            ]
                                        }]
                                        ,columns :
                                        [
                                            {
                                                text       : 'DOCUMENTO'
                                                ,dataIndex : 'DSDOCUME'
                                                ,flex      : 1
                                            }
                                            ,{
                                                xtype    : 'actioncolumn'
                                                ,tooltip : 'Editar'
                                                ,icon    : '${icons}pencil.png'
                                                ,width   : 30
                                                ,handler : function(me,row,col,item,e,record)
                                                {
                                                    centrarVentanaInterna(
                                                        Ext.MessageBox.confirm(
                                                            'Confirmar'
                                                            ,'Se perder\u00e1n los cambios no guardados en la revisi\u00f3n<br>¿Desea continuar?'
                                                            ,function(btn)
                                                            {
                                                                if(btn === 'yes')
                                                                {
                                                                    _p52_formTdocume.showEdit(record);
                                                                }
                                                            }
                                                        )
                                                    );
                                                }
                                            }
                                            ,{
                                                text       : 'INC.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWLISTA'
                                                ,width     : 50
                                            }
                                            ,{
                                                text       : 'REQ.'
                                                ,xtype     : 'checkcolumn'
                                                ,dataIndex : 'SWOBLIGA'
                                                ,width     : 50
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            autoLoad    : true
                                            ,groupField : 'DSTIPTRA'
                                            ,fields     :
                                            [
                                                'CDDOCUME'
                                                ,'DSDOCUME'
                                                ,'CDTIPTRA'
                                                ,'DSTIPTRA'
                                                ,{ name : 'SWLISTA'   , type : 'boolean' }
                                                ,{ name : 'SWOBLIGA'  , type : 'boolean' }
                                            ]
                                            ,proxy   :
                                            {
                                                type         : 'ajax'
                                                ,url         : _p52_urlRecuperacion
                                                ,extraParams :
                                                {
                                                    'params.consulta' : 'RECUPERAR_TDOCUME'
                                                }
                                                ,reader      :
                                                {
                                                    type  : 'json'
                                                    ,root : 'list'
                                                }
                                            }
                                        })
                                    })
                                ]
                            })
                            ,Ext.create('Ext.form.Panel',
                            {
                                itemId       : '_p52_formCorreos'
                                ,title       : 'Correos'
                                ,defaults    : { style : 'margin:5px;' }
                                ,buttonAlign : 'center'
                                ,hidden      : true
                                ,layout		 : {
                                	type     : 'table'
                                	,columns : 2
                                	,tdAttrs : {valign:'middle'}
                                	}
                                ,tools       :
                                [{
                                    type     : 'help'
                                    ,tooltip : 'Envi\u00f3 de correos'
                                    ,handler : function()
                                    {
                                        _p52_ventanaTips(
                                        [
                                            'La estructura de los DATOS para validaci\u00f3n cliente es:'
                                            ,'CDFLUJOMC: "12"'
                                            ,'CDRAMO: "2"'
                                            ,'CDSISROL: "MESADECONTROL"'
                                            ,'CDTIPFLU: "1"'
                                            ,'CDUNIECO: "1000"'
                                            ,'CDUSUARI: "A100"'
                                            ,'CLAVEENT: "27"'
                                            ,'ESTADO: "W"'
                                            ,'NMPOLIZA: ""'
                                            ,'NMSITUAC: ""'
                                            ,'NMSUPLEM: ""'
                                            ,'NTRAMITE: "13892"'
                                            ,'STATUS: "2"'
                                            ,'TIPOENT: "V"'
                                        ]);
                                    }
                                }]
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosCorreo
                                            (me,function(me)
                                            {
                                            	////////////INICIA VALIDACION VARIABLES////////////
                                            	//var win            = me.up('window');
                                            	//Para obtener la longitud de variables
                                            	var vdestino    = _p52_formCorreos.down('[name=VARDESTINO]').getValue();
                                            	var vasunto     = _p52_formCorreos.down('[name=VARASUNTO]').getValue();
                                            	var vmensaje    = _p52_formCorreos.down('[name=VARMENSAJE]').getValue();
                                            	
                                            	//Se genera esta condicion para evitar que se almacene longitud 1 cuando la cadena esta vacia
                                            	var varsdestino = 0;
                                            	if(vdestino.indexOf(',') == -1 && vdestino.length == 0){
                                            		varsdestino = 0;
                                            	}else{
                                            		varsdestino = vdestino.split(',').length;
                                            	}
                                            	
                                            	var varsasunto     = 0;
                                            	if(vasunto.indexOf(',') == -1 && vasunto.length == 0){
                                            		varsasunto = 0;
                                            	}else{
                                            		varsasunto = vasunto.split(',').length;
                                            	}
                                            	
                                            	var varsmensaje    = 0;
												if(vmensaje.indexOf(',') == -1 && vmensaje.length == 0){
                                            		varsmensaje = 0;
                                            	}else{
                                            		varsmensaje = vmensaje.split(',').length;
                                            	}
                                            	
                                            	//Obtiene las coincidencias de {} en las variables
 		                						var numvarsdestino = _p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSDESTINO]').getValue());
 		                						var numvarsasunto  = _p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSASUNTO]').getValue());
 		                						var numvarsmensaje = _p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSMENSAJE]').getValue());
 		                						
 		                						if(varsdestino != numvarsdestino){
 		                							mensajeError('El numero de variables seleccionadas en el destinatario no coincide con las llaves del texto');
 		                							return;
 		                						}
 		                						
 		                						if(varsasunto != numvarsasunto){
 		                							mensajeError('El numero de variables seleccionadas en el asunto no coincide con las llaves del texto');
 		                							return;
 		                						}
 		                						
 		                						if(varsmensaje != numvarsmensaje){
 		                							mensajeError('El numero de variables seleccionadas en el mensaje no coincide con las llaves del texto');
 		                							return;
 		                						}
 		                						////////////TERMINA VALIDACION VARIABLES////////////
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                                _p52_actualizaLabel(
                                                    'V'
                                                    ,_p52_formCorreos.down('[name=WEBID]').getValue()
                                                    ,_p52_formCorreos.down('[name=DSMAIL]').getValue()
                                                );
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items :
                                [
                                    {
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_ACCION'
                                        ,name       : 'ACCION'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDTIPFLU'
                                        ,name       : 'CDTIPFLU'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDFLUJOMC'
                                        ,name       : 'CDFLUJOMC'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_CDMAIL'
                                        ,name       : 'CDMAIL'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_WEBID'
                                        ,name       : 'WEBID'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_XPOS'
                                        ,name       : 'XPOS'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_YPOS'
                                        ,name       : 'YPOS'
                                        ,allowBlank : false
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_VARDESTINO'
                                        ,name       : 'VARDESTINO'
                                        ,itemId		: 'VARDESTINO'
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_VARASUNTO'
                                        ,name       : 'VARASUNTO'
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : '_VARMENSAJE'
                                        ,name       : 'VARMENSAJE'
                                        ,hidden     : !_p52_debug
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : 'ETIQUETA'
                                        ,labelAlign : 'top'
                                        ,name       : 'DSMAIL'
                                        ,allowBlank : false
                                    }
                                    ,{
                                        border : 0
                                    }
                                    ,{
                                        xtype       : 'textfield'
                                        ,fieldLabel : 'PARA'
                                        ,labelAlign : 'top'
                                        ,name       : 'DSDESTINO'
                                        ,allowBlank : false
                                    }
                                    ,{
                                    	xtype: 'button'
                                        ,text: 'Variables'
                                        ,icon : '${icons}table_relationship.png'
                                        ,handler: function() {
                                        	if(_p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSDESTINO]').getValue()) == 0){
                                        		mensajeError('El texto no contiene sintaxis para agregar variables');
                                        	}else{
                                        		_p52_mostrarVentanaVariablesCorreo('VARDESTINO','DSDESTINO');
                                        	}
                                        }
                                    }
                                    ,{
                                        xtype          : 'textfield'
                                        ,fieldLabel    : 'ASUNTO'
                                        ,labelAlign    : 'top'
                                        ,name          : 'DSASUNTO'
                                        ,allowBlank    : false
                                        ,minLength     : 1
                                        ,maxLength     : 300
                                    }
                                    ,{
                                    	xtype: 'button'
                                        ,text: 'Variables'
                                        ,icon : '${icons}table_relationship.png'
                                        ,handler: function() {
                                        	if(_p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSASUNTO]').getValue()) == 0){
                                        		mensajeError('El texto no contiene sintaxis para agregar variables');
                                        	}else{
                                        		_p52_mostrarVentanaVariablesCorreo('VARASUNTO','DSASUNTO');
                                        	}
                                        }
                                    }
                                    ,{
                                        xtype          : 'textarea'
                                        ,fieldLabel    : 'MENSAJE'
                                        ,labelAlign    : 'top'
                                        ,name          : 'DSMENSAJE'
                                    }
                                    ,{
                                    	xtype: 'button'
                                        ,text: 'Variables'
                                        ,icon : '${icons}table_relationship.png'
                                        ,handler: function() {
                                        	if(_p52_numberContainsSubstrInStr('{}',_p52_formCorreos.down('[name=DSMENSAJE]').getValue()) == 0){
                                        		mensajeError('El texto no contiene sintaxis para agregar variables');
                                        	}else{
                                        		_p52_mostrarVentanaVariablesCorreo('VARMENSAJE','DSMENSAJE');
                                        	}
                                        }
                                    }
                                    /* , new SelectorMultiple() */
                                    /*,{
                                        xtype: 'itemselector',
                                        name : 'variables'
                                        ,imagePath: '../ux/images/'
                                        ,store: 
                                        ,displayField: 'DSVARMAIL',
                                        ,valueField  : 'CDVARMAIL'
                                        ,allowBlank  : false
                                        ,msgTarget   : 'side'
                                        ,fromTitle   : 'Disponibles'
                                        ,toTitle     : 'Seleccionadas'
                                    }*/
                                ]
                            })
                            ,Ext.create('Ext.panel.Panel',
                            {
                                itemId       : '_p52_panelTitulo'
                                ,title       : 'T\u00CDTULO'
                                ,hidden      : true
                                ,buttonAlign : 'center'
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosTitulo(me,function(me)
                                            {
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                                _p52_actualizaLabel(
                                                    'T'
                                                    ,_p52_panelTitulo.down('[name=WEBID]').getValue()
                                                    ,_p52_panelTitulo.down('[name=DSTITULO]').getValue()
                                                );
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items :
                                [
                                    {
                                        xtype     : 'form'
                                        ,border   : 0
                                        ,defaults : { style : 'margin:5px;' }
                                        ,items    :
                                        [
                                            {
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_ACCION'
                                                ,name       : 'ACCION'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDTIPFLU'
                                                ,name       : 'CDTIPFLU'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDFLUJOMC'
                                                ,name       : 'CDFLUJOMC'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_CDTITULO'
                                                ,name       : 'CDTITULO'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_WEBID'
                                                ,name       : 'WEBID'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_XPOS'
                                                ,name       : 'XPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_YPOS'
                                                ,name       : 'YPOS'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : 'Nombre'
                                                ,labelAlign : 'top'
                                                ,name       : 'DSTITULO'
                                                ,allowBlank : false
                                            }
                                        ]
                                    }
                                ]
                            })
                            ,Ext.create('Ext.panel.Panel',
                            {
                                itemId       : '_p52_panelAccion'
                                ,title       : 'ACCI\u00D3N'
                                ,hidden      : true
                                ,buttonAlign : 'center'
                                ,tools       : [
                                    {
                                        type    : 'unpin',
                                        tooltip : 'Copiar datos',
                                        handler : function (event, toolEl, owner, tool) {
                                            var ck = 'Copiando propiedades de conector';
                                            try {
                                                var panel = tool.up('panel');
                                                debug('panel:', panel);
                                                var iconos = $('[name=iconoaccion]:checked');
                                                var icono = '';
                                                if (iconos.length > 0) {
                                                    icono = iconos[0].value;
                                                }
                                                var store = panel.down('grid').getStore();
                                                var roles = {};
                                                store.each(function (record) {
                                                    roles[record.get('CDSISROL')] = record.get('SWPERMISO');
                                                });
                                                var props = {
                                                    DSACCION : panel.down('[name=DSACCION]').getValue(),
                                                    CDVALOR  : panel.down('[name=CDVALOR]').getValue(),
                                                    AUX      : panel.down('[name=AUX]').getValue(),
                                                    SWESCALA : panel.down('[name=SWESCALA]').getValue(),
                                                    ICONO    : icono,
                                                    roles    : roles
                                                };
                                                debug('props:', props);
                                                var encode = Ext.encode(props);
                                                debug('encode:', encode);
                                                executeCopy(encode);
                                            } catch (e) {
                                                manejaException(e, ck);
                                            }
                                        }
                                    }, {
                                        type    : 'pin',
                                        tooltip : 'Pegar datos',
                                        handler : function (event, toolEl, owner, tool) {
                                            var ck = 'Pegando propiedades de conector';
                                            try {
                                                var encode = window.prompt("Pegar: Ctrl+V, Enter");
                                                if (Ext.isEmpty(encode)) {
                                                    return;
                                                }
                                                debug('encode:', encode);
                                                var decode = Ext.decode(encode);
                                                debug('decode:', decode);
                                                var panel = tool.up('panel');
                                                debug('panel:', panel);
                                                panel.down('[name=DSACCION]').setValue(decode.DSACCION);
                                                panel.down('[name=CDVALOR]').setValue(decode.CDVALOR);
                                                panel.down('[name=AUX]').setValue(decode.AUX);
                                                panel.down('[name=SWESCALA]').setValue(decode.SWESCALA);
                                                var store = panel.down('grid').getStore();
                                                store.each(function (record) {
                                                    record.set('SWPERMISO', decode.roles[record.get('CDSISROL')] || false);
                                                });
                                                if (!Ext.isEmpty(decode.ICONO)) {
                                                    var iconos = $('[name=iconoaccion][value=' + decode.ICONO + ']');
                                                    if (iconos.length > 0) {
                                                        iconos[0].focus();
                                                        iconos[0].checked = true;
                                                    }
                                                }
                                            } catch (e) {
                                                manejaException(e, ck);
                                            }
                                        }
                                    }
                                ]
                                ,buttons     :
                                [
                                    {
                                        text     : 'Guardar'
                                        ,icon    : '${icons}disk.png'
                                        ,handler : function(me)
                                        {
                                            _p52_guardarDatosAccion(me,function(me)
                                            {
                                                _p52_panelCanvas.enable();
                                                me.up('panel').hide();
                                            });
                                        }
                                    }
                                    ,{
                                        text     : 'Cancelar'
                                        ,icon    : '${icons}cancel.png'
                                        ,handler : function(me)
                                        {
                                            _p52_panelCanvas.enable();
                                            me.up('panel').hide();
                                        }
                                    }
                                ]
                                ,items :
                                [
                                    {
                                        xtype     : 'form'
                                        ,border   : 0
                                        ,defaults : { style : 'margin:5px;' }
                                        ,items    :
                                        [
                                            {
		                                        xtype       : 'textfield'
		                                        ,fieldLabel : '_ACCION'
		                                        ,name       : 'ACCION'
		                                        ,allowBlank : false
		                                        ,hidden     : !_p52_debug
		                                    }
		                                    ,{
		                                        xtype       : 'textfield'
		                                        ,fieldLabel : '_CDTIPFLU'
		                                        ,name       : 'CDTIPFLU'
		                                        ,allowBlank : false
		                                        ,hidden     : !_p52_debug
		                                    }
		                                    ,{
		                                        xtype       : 'textfield'
		                                        ,fieldLabel : '_CDFLUJOMC'
		                                        ,name       : 'CDFLUJOMC'
		                                        ,allowBlank : false
		                                        ,hidden     : !_p52_debug
		                                    }
		                                    ,{
		                                        xtype       : 'textfield'
		                                        ,fieldLabel : '_CDACCION'
		                                        ,name       : 'CDACCION'
		                                        ,allowBlank : false
		                                        ,hidden     : !_p52_debug
		                                    }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_IDORIGEN'
                                                ,name       : 'IDORIGEN'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : '_IDDESTIN'
                                                ,name       : 'IDDESTIN'
                                                ,allowBlank : false
                                                ,hidden     : !_p52_debug
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : 'NOMBRE'
                                                ,labelAlign : 'top'
                                                ,name       : 'DSACCION'
                                                ,allowBlank : false
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : 'VALOR'
                                                ,labelAlign : 'top'
                                                ,name       : 'CDVALOR'
                                            }
                                            ,{
                                                xtype       : 'textfield'
                                                ,fieldLabel : 'PAR\u00c1METRO'
                                                ,labelAlign : 'top'
                                                ,name       : 'AUX'
                                            }
                                            ,{
                                                xtype       : 'checkbox'
                                                ,boxLabel   : 'Disparado por escalamiento'
                                                ,name       : 'SWESCALA'
                                                ,inputValue : 'S'
                                            }
                                        ]
                                    }
                                    ,Ext.create('Ext.grid.Panel',
                                    {
                                        itemId   : '_p52_gridAccRol'
                                        ,title   : 'PERMISOS'
                                        ,height  : 220
                                        ,tools   :
                                        [
                                            {
                                                type     : 'collapse'
                                                ,tooltip : 'Ninguno'
                                                ,handler : function()
                                                {
                                                    _fieldById('_p52_gridAccRol').store.each(function(record)
                                                    {
                                                        record.set('SWPERMISO',false);
                                                    });
                                                }
                                            }
                                            ,{
                                                type     : 'plus'
                                                ,tooltip : 'Todos'
                                                ,handler : function()
                                                {
                                                    _fieldById('_p52_gridAccRol').store.each(function(record)
                                                    {
                                                        record.set('SWPERMISO',true);
                                                    });
                                                }
                                            }
                                        ]
                                        ,columns :
                                        [
                                            {
                                                text       : 'ROL'
                                                ,dataIndex : 'DSSISROL'
                                                ,width     : 200
                                            }
                                            ,{
                                                xtype      : 'checkcolumn'
                                                ,dataIndex : 'SWPERMISO'
                                                ,flex      : 1
                                            }
                                        ]
                                        ,store : Ext.create('Ext.data.Store',
                                        {
                                            autoLoad : true
                                            ,fields  :
                                            [
                                                'CDSISROL'
                                                ,'DSSISROL'
                                                ,{ name : 'SWPERMISO' , type : 'boolean' }
                                            ]
                                            ,proxy   :
                                            {
                                                type         : 'ajax'
                                                ,url         : _p52_urlRecuperacion
                                                ,extraParams :
                                                {
                                                    'params.consulta' : 'RECUPERAR_ROLES'
                                                }
                                                ,reader      :
                                                {
                                                    type  : 'json'
                                                    ,root : 'list'
                                                }
                                            }
                                        })
                                    })
                                    ,Ext.create('Ext.panel.Panel',
                                    {
                                        itemId      : '_p52_catalogoIconos'
                                        ,title      : 'ICONO'
                                        ,height     : 250
                                        ,defaults   : { style : 'margin : 5px;' }
                                        ,autoScroll : true
                                        ,border     : 0
                                        ,layout     :
                                        {
                                            type     : 'table'
                                            ,columns : 3
                                            ,tdAttrs : { valign : 'top' }
                                        }
                                        ,header :
                                        {
                                            titlePosition : 0
                                            ,items        :
                                            [
                                                {
                                                    xtype    : 'button'
                                                    ,icon    : '${icons}cancel.png'
                                                    ,handler : function()
                                                    {
                                                        _fieldById('enfocadorIconos').setValue('');
                                                        _fieldById('enfocadorIconos').focus();
                                                    }
                                                },
                                                {
                                                    xtype      : 'textfield'
                                                    ,itemId    : 'enfocadorIconos'
                                                    ,width     : 100
                                                    ,indice    : 0
                                                    ,listeners :
                                                    {
                                                        change : function(me,val)
                                                        {
                                                            me.indice = 0;
                                                            debug('indice 0');
                                                        }
                                                    }
                                                    ,buscar : function()
                                                    {
                                                        var me = this, val = me.getValue();
                                                        debug('buscar indice:',me.indice,',val:',val,'.');
                                                        if(!Ext.isEmpty(val))
                                                        {
                                                            var iconos = $('[name=iconoaccion][value*='+val.toLowerCase().split(' ').join('_')+']');
                                                            debug('iconos:',iconos,'.');
                                                            if(iconos.length>0)
                                                            {
                                                                iconos[me.indice%iconos.length].focus();
                                                                iconos[me.indice%iconos.length].checked = true;
                                                                me.indice++;
                                                            }
                                                        }
                                                        me.focus();
                                                    }
                                                }
                                                ,{
                                                    xtype    : 'button'
                                                    ,icon    : '${icons}tick.png'
                                                    ,handler : function(me)
                                                    {
                                                        _fieldById('enfocadorIconos').buscar();
                                                    }
                                                }
                                            ]
                                        }
                                        ,tools  :
                                        [{
                                            type     : 'pin'
                                            ,tooltip : 'Ir al seleccionado'
                                            ,handler : function(me)
                                            {
                                                var iconos = $('[name=iconoaccion]:checked');
                                                debug('iconos:',iconos,'.');
                                                if(iconos.length>0)
                                                {
                                                    iconos[0].focus();
                                                }
                                            }
                                        }]
                                    })
                                ]
                            })
                        ]
                    })
                    ,Ext.create('Ext.panel.Panel',
                    {
                        itemId      : '_p52_panelCanvas'
                        ,region     : 'center'
                        ,autoScroll : true
                        ,html       : '<div id="canvasdiv" ondragover="event.preventDefault();" ondrop="_p52_drop(event);"></div>'
                    })
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Guardar coordenadas'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p52_guardarCoords();
                        }
                    }
                    ,{
                        text     : 'Guardar coordenadas y regresar'
                        ,icon    : '${icons}disk.png'
                        ,handler : function(me)
                        {
                            _p52_guardarCoords(function()
                            {
                                _p52_navega(1);
                            });
                        }
                    }
                    ,{
                        text  : 'Salir sin guardar coordenadas'
                        ,icon : '${icons}cancel.png'
                        ,handler : function(me)
                        {
                            _p52_navega(1);
                        }
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    _p52_panelGrids           = _fieldById('_p52_panelGrids');
    _p52_gridTramites         = _fieldById('_p52_gridTramites');
    _p52_gridProcesos         = _fieldById('_p52_gridProcesos');
    _p52_panelDibujo          = _fieldById('_p52_panelDibujo');
    _p52_catalogoEstados      = _fieldById('_p52_catalogoEstados');
    _p52_catalogoSucursales   = _fieldById('_p52_catalogoSucursales');
    _p52_panelCanvas          = _fieldById('_p52_panelCanvas');
    _p52_panelEstado          = _fieldById('_p52_panelEstado');
    _p52_catalogoPantallas    = _fieldById('_p52_catalogoPantallas');
    _p52_catalogoComponentes  = _fieldById('_p52_catalogoComponentes');
    _p52_catalogoProcesos     = _fieldById('_p52_catalogoProcesos');
    _p52_catalogoValidaciones = _fieldById('_p52_catalogoValidaciones');
    _p52_catalogoCorreos      = _fieldById('_p52_catalogoCorreos');
    _p52_catalogoRevisiones   = _fieldById('_p52_catalogoRevisiones');
    _p52_catalogoTitulos      = _fieldById('_p52_catalogoTitulos');
    _p52_formValidacion       = _fieldById('_p52_formValidacion');
    _p52_formCorreos          = _fieldById('_p52_formCorreos');
    _p52_panelRevision        = _fieldById('_p52_panelRevision');
    _p52_panelTitulo          = _fieldById('_p52_panelTitulo');
    _p52_panelAccion          = _fieldById('_p52_panelAccion');
    _p52_catalogoIconos       = _fieldById('_p52_catalogoIconos');
    ////// custom //////
    
    ////// loaders //////
    if (1 === Number(_p52_params.cdtipmod)) {
        _p52_cargarEstados();
        _p52_cargarPantallas();
        _p52_cargarComponentes();
        _p52_cargarProcesos();
        _p52_cargarValidaciones();
        _p52_cargarRevisiones();
        _p52_cargarCorreos();
        _p52_cargarIconos();
    } else if (2 === Number(_p52_params.cdtipmod)) {
        _p52_cargarSucursales();
    }
    _p52_cargarTitulos();
    
    jsPlumb.ready(function()
    {
        toolkit = jsPlumb.getInstance(
        {
            Container           : 'canvasdiv',
            Endpoint            : ['Dot',{radius:7}],
            ConnectionOverlays  : [ [ 'PlainArrow' , { location : 1, width : 15, length : 15 } ] ],
            Connector           : 'StateMachine',
            //Connector         : 'Flowchart',
            ReattachConnections : false,
            PaintStyle          : {
                strokeStyle : 'rgba(67, 85, 135, 0.4)',
                lineWidth   : 4
            },
            HoverPaintStyle    : {
                strokeStyle : 'rgba(255, 0, 0, 0.8)',
                lineWidth   : 5
            },
            EndpointStyle       : {
                fillStyle     : 'rgba(67, 85, 135, 0.4)',
                outlineColor  : 'rgba(67, 85, 135, 0.4)'
            },
            EndpointHoverStyle : {
                fillStyle    : 'rgba(255, 0, 0, 0.8)',
                outlineColor : 'rgba(255, 0, 0, 0.8)'
            }
        });
        
        toolkit.bind('click',function(con)
        {
            debug('toolkit.bind.click con:',con);
            if (con.type === 'Dot') {
                return alert('Debe seleccionar la flecha, no el punto');
            }
            try {
                document.getElementById('spanUltimoEditado').remove();
            } catch (e) {}
            con.addOverlay(['Label', {
                label    : '<span id="spanUltimoEditado" style="background:green;color:white;font-size:8px;">EDITADO</span>',
                location : 0.5
            }]);
            _p52_editEndpoint(con,'A',con.cdaccion);
        });
        
        toolkit.bind('connection',function(con)
        {
            debug('connection con:',con,'.');
            if(_p52_cargando==false)
            {
                _p52_registrarConnection(con);
            }
        });
        
        toolkit.bind('connectionDetached',function(con)
        {
            debug('connectionDetached con:',con,'.');
            if(_p52_cargando==false)
            {
                _p52_borrarConnection(con.connection.cdaccion);
            }
        });
        
        toolkit.bind('connectionMoved',function(con)
        {
            debug('connectionMoved con:',con,'.');
            _p52_borrarConnection(con.connection.cdaccion);
        });
    });
    ////// loaders //////
});

////// funciones //////

function _p52_navega(nivel)
{
    if(nivel==1)
    {
        _p52_panelGrids.show();
        _p52_panelDibujo.hide();
    }
    else if(nivel==2)
    {
        _p52_panelGrids.hide();
        _p52_panelDibujo.show();
        
        _p52_panelCanvas.enable();
        _p52_panelEstado.hide();
        _p52_formValidacion.hide();
        _p52_panelRevision.hide();
        _p52_panelAccion.hide();
    }
}

function _p52_cargarEstados()
{
    debug('_p52_cargarEstados');
    
    var ck = 'Cargando status';
    try
    {
        _setLoading(true,_p52_catalogoEstados);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TESTADOMC'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoEstados);
                var ck = 'Decodificando respuesta al cargar los status';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load status:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoEstados.removeAll();
                        var estados = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            estados.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : estadoTpl
                                ,border : 0
                                ,itemId : 'E'+json.list[i].CDESTADOMC
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoEstados.add(estados);
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
                _setLoading(false,_p52_catalogoEstados);
                errorComunicacion(null,'Error al cargar los status');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoEstados);
        manejaException(e,ck);
    }
}

function _p52_cargarSucursales()
{
    debug('_p52_cargarSucursales');
    
    var ck = 'Cargando sucursales';
    try
    {
        _setLoading(true,_p52_catalogoSucursales);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TODAS_SUCURSALES'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoSucursales);
                var ck = 'Decodificando respuesta al cargar las sucursales';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load sucursales:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoSucursales.removeAll();
                        var sucursales = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            sucursales.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : sucursalTpl
                                ,border : 0
                                ,itemId : 'S'+json.list[i].CDUNIECO
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoSucursales.add(sucursales);
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
                _setLoading(false,_p52_catalogoSucursales);
                errorComunicacion(null,'Error al cargar las sucursales');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoSucursales);
        manejaException(e,ck);
    }
}

function _p52_cargarPantallas()
{
    debug('_p52_cargarPantallas');
    
    var ck = 'Cargando pantallas';
    try
    {
        _setLoading(true,_p52_catalogoPantallas);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TPANTMC'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoPantallas);
                var ck = 'Decodificando respuesta al cargar las pantallas';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load pantallas:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoPantallas.removeAll();
                        var pantallas = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            pantallas.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : pantallaTpl
                                ,border : 0
                                ,itemId : 'P'+json.list[i].CDPANTMC
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoPantallas.add(pantallas);
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
                _setLoading(false,_p52_catalogoPantallas);
                errorComunicacion(null,'Error al cargar las pantallas');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoPantallas);
        manejaException(e,ck);
    }
}

function _p52_cargarComponentes()
{
    debug('_p52_cargarComponentes');
    
    var ck = 'Cargando componentes';
    try
    {
        _setLoading(true,_p52_catalogoComponentes);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TCOMPMC'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoComponentes);
                var ck = 'Decodificando respuesta al cargar los componentes';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load componentes:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoComponentes.removeAll();
                        var comps = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            comps.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : componenteTpl
                                ,border : 0
                                ,itemId : 'C'+json.list[i].CDCOMPMC
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoComponentes.add(comps);
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
                _setLoading(false,_p52_catalogoComponentes);
                errorComunicacion(null,'Error al cargar los componentes');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoComponentes);
        manejaException(e,ck);
    }
}

function _p52_cargarProcesos()
{
    debug('_p52_cargarProcesos');
    
    var ck = 'Cargando Procesos';
    try
    {
        _setLoading(true,_p52_catalogoProcesos);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TPROCMC'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoProcesos);
                var ck = 'Decodificando respuesta al cargar los procesos';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load procesos:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoProcesos.removeAll();
                        var procs = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            procs.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : procesoTpl
                                ,border : 0
                                ,itemId : 'O'+json.list[i].CDPROCMC
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoProcesos.add(procs);
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
                _setLoading(false,_p52_catalogoProcesos);
                errorComunicacion(null,'Error al cargar los procesos');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoProcesos);
        manejaException(e,ck);
    }
}

function _p52_cargarValidaciones()
{
    debug('_p52_cargarValidaciones');
    _p52_catalogoValidaciones.removeAll();
    _p52_catalogoValidaciones.add(
    {
        xtype   : 'panel'
        ,tpl    : validacionTpl
        ,border : 0
        ,data   :
        {
            cdvalidacion  : 0
            ,dsvalidacion : 'Nueva validaci\u00f3n'
        }
    });
}

function _p52_cargarRevisiones()
{
    debug('_p52_cargarRevisiones');
    _p52_catalogoRevisiones.removeAll();
    _p52_catalogoRevisiones.add(
    {
        xtype   : 'panel'
        ,tpl    : revisionTpl
        ,border : 0
        ,data   :
        {
            cdrevision  : 0
            ,dsrevision : 'Nueva revisi\u00f3n'
        }
    });
}

function _p52_cargarCorreos()
{
    debug('_p52_cargarCorreos');
    _p52_catalogoCorreos.removeAll();
    _p52_catalogoCorreos.add(
    {
        xtype   : 'panel'
        ,tpl    : correoTpl
        ,border : 0
        ,data   :
        {
            cdmail  : 0
            ,dsmail : 'Nuevo correo'
        }
    });
}


function _p52_cargarTitulos()
{
    debug('_p52_cargarTitulos');
    _p52_catalogoTitulos.removeAll();
    _p52_catalogoTitulos.add(
    [
	    {
	        xtype   : 'panel'
	        ,tpl    : tituloTpl
	        ,border : 0
	        ,data   :
            {
                cdtitulo  : 0
                ,dstitulo : 'T\u00edtulo 500'
            }
	    }
	    ,{
	        xtype   : 'panel'
            ,tpl    : tituloTpl
            ,border : 0
            ,data   :
            {
                cdtitulo  : 1
                ,dstitulo : 'T\u00edtulo 1000'
            }
        }
    ]);
}

function _p52_cargarIconos()
{
    debug('_p52_cargarIconos');
    
    var ck = 'Cargando iconos';
    try
    {
        _setLoading(true,_p52_catalogoIconos);
        Ext.Ajax.request(
        {
            url      : _p52_urlRecuperacion
            ,params  :
            {
                'params.consulta' : 'RECUPERAR_TICONOS'
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_catalogoIconos);
                var ck = 'Decodificando respuesta al cargar los iconos';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### load iconos:',json);
                    if(json.success==true)
                    {
                        _p52_catalogoIconos.removeAll();
                        var iconos = [];
                        for(var i=0;i<json.list.length;i++)
                        {
                            iconos.push(
                            {
                                xtype   : 'panel'
                                ,tpl    : iconoTpl
                                ,border : 0
                                ,data   : json.list[i]
                            });
                        }
                        _p52_catalogoIconos.add(iconos);
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
                _setLoading(false,_p52_catalogoIconos);
                errorComunicacion(null,'Error al cargar los iconos');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_catalogoIconos);
        manejaException(e,ck);
    }
}

function _p52_dragstart(event)
{
    debug('_p52_dragstart event:',event);
    event.dataTransfer.setData('clave'   , event.target.id);
    event.dataTransfer.setData('descrip' , $('#'+event.target.id).attr('descrip'));
    debug('getData:',event.dataTransfer.getData("clave"),event.dataTransfer.getData("descrip"));
}

function _p52_drop(event)
{
    debug('_p52_drop event:',event);
    
    var catId   = event.dataTransfer.getData("clave");
    var descrip = event.dataTransfer.getData("descrip");
    var tipo    = catId.substr(0,1);
    var clave   = catId.substr(1);
    var id      = _p52_generaId();
    var x       = event.layerX;
    var y       = event.layerY;
    
    if(tipo=='E')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,descrip,x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='S')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,descrip,x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='P')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,descrip,x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='C')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,descrip,x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='O')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,descrip,x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='V')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,json.params.cdentidad,'',x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='R')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,json.params.cdentidad,'',x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
    else if(tipo=='T')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,clave,'',x,y);
        });
    }
    else if(tipo=='M')
    {
        _p52_registrarEntidad(tipo,clave,id,x,y,function(json)
        {
            _p52_addDiv(id,tipo,json.params.cdentidad,'',x,y);
            _p52_addEndpoint(id,tipo);
        });
    }
}

function _p52_generaId()
{
    return (new Date()).getTime()+'_'+(Math.floor(Math.random()*10000));
}

function _p52_addEndpoint(id,tipo)
{
    debug('_p52_addEndpoint id,tipo:',id,tipo);
    var ep = toolkit.addEndpoint(id,epProps[tipo]);
    return ep;
}

function _p52_editEndpoint(id,tipo,clave)
{
    debug('_p52_editEndpoint id,tipo,clave:',id,tipo,clave,'.');
    if(tipo=='E')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.show();
        _p52_formValidacion.hide();
        _p52_panelRevision.hide();
        _p52_panelTitulo.hide();
        _p52_panelAccion.hide();
        _p52_formCorreos.hide();
        _p52_cargarDatosEstado(clave);
    }
    else if(tipo=='V')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.hide();
        _p52_formValidacion.show();
        _p52_panelRevision.hide();
        _p52_panelTitulo.hide();
        _p52_panelAccion.hide();
        _p52_formCorreos.hide();
        _p52_cargarDatosValidacion(clave);
    }
    else if(tipo=='R')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.hide();
        _p52_formValidacion.hide();
        _p52_panelRevision.show();
        _p52_panelTitulo.hide();
        _p52_panelAccion.hide();
        _p52_formCorreos.hide();
        _p52_cargarDatosRevision(clave);
    }
    else if(tipo=='T')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.hide();
        _p52_formValidacion.hide();
        _p52_panelRevision.hide();
        _p52_panelTitulo.show();
        _p52_panelAccion.hide();
        _p52_formCorreos.hide();
        _p52_cargarDatosTitulo(id);
    }
    else if(tipo=='M')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.hide();
        _p52_formValidacion.hide();
        _p52_panelRevision.hide();
        _p52_panelTitulo.hide();
        _p52_panelAccion.hide();
        _p52_formCorreos.show();
        _p52_cargarDatosCorreo(clave);
    }
    else if(tipo=='A')
    {
        _p52_panelCanvas.disable();
        _p52_panelEstado.hide();
        _p52_formValidacion.hide();
        _p52_panelRevision.hide();
        _p52_panelTitulo.hide();
        _p52_panelAccion.show();
        _p52_formCorreos.hide();
        _p52_cargarDatosAccion(clave);
    }
}

function _p52_removeEndpoint(id,tipo,clave)
{
    debug('_p52_removeEndpoint id,tipo,clave:',id,tipo,clave,'.');
    var ck = 'Borrando entidad';
    try
    {
        if (confirm('¿Borrar?') !== true) {
            return;
        }
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url      : _p52_urlBorrarEntidad
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.tipo'      : tipo
                ,'params.clave'     : clave
                ,'params.webid'     : id
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al borrar entidad';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### -entidad:',json);
                    if(json.success==true)
                    {
                        toolkit.remove(id);
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
                _setLoading(false,_p52_panelDibujo);
                errorComunicacion(null,'Error al borrar entidad');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelDibujo);
        manejaException(e,ck);
    }
}

function _p52_editCatClic(tipo,id)
{
    debug('_p52_editCatClic tipo,id:',tipo,id,'.');
    
    if(tipo=='E')
    {
        if(Ext.isEmpty(id))
        {
            _p52_formEstado.showNew();
        }
        else
        {
            debug('recuperando:',_fieldById(id).initialConfig.data,'.');
            var rec =
            {
                datos    : _fieldById(id).initialConfig.data
                ,getData : function()
                {
                    return this.datos;
                }
            };
            _p52_formEstado.showEdit(rec);
        }
    }
    else if(tipo=='P')
    {
        if(Ext.isEmpty(id))
        {
            _p52_formPantalla.showNew();
        }
        else
        {
            debug('recuperando:',_fieldById(id).initialConfig.data,'.');
            var rec =
            {
                datos    : _fieldById(id).initialConfig.data
                ,getData : function()
                {
                    return this.datos;
                }
            };
            _p52_formPantalla.showEdit(rec);
        }
    }
    else if(tipo=='C')
    {
        if(Ext.isEmpty(id))
        {
            _p52_formComponente.showNew();
        }
        else
        {
            debug('recuperando:',_fieldById(id).initialConfig.data,'.');
            var rec =
            {
                datos    : _fieldById(id).initialConfig.data
                ,getData : function()
                {
                    return this.datos;
                }
            };
            _p52_formComponente.showEdit(rec);
        }
    }
    else if(tipo=='O')
    {
        if(Ext.isEmpty(id))
        {
            _p52_formProceso.showNew();
        }
        else
        {
            debug('recuperando:',_fieldById(id).initialConfig.data,'.');
            var rec =
            {
                datos    : _fieldById(id).initialConfig.data
                ,getData : function()
                {
                    return this.datos;
                }
            };
            _p52_formProceso.showEdit(rec);
        }
    }
    
}

function _p52_registrarEntidad(tipo,clave,id,x,y,callback)
{
    debug('_p52_registrarEntidad :',tipo,clave,id,'.');
    debug(x,y,'.');
    var ck = 'Registrando entidad';
    try
    {
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url     : _p52_urlRegistrarEntidad
            ,params :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.tipo'      : tipo
                ,'params.clave'     : clave
                ,'params.webid'     : id
                ,'params.xpos'      : x
                ,'params.ypos'      : y
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al registrar entidad';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### +entidad:',json);
                    if(json.success==true)
                    {
                        callback(json);
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
                _setLoading(false,_p52_panelDibujo);
                errorComunicacion(null,'Error al registrar entidad');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelDibujo);
        manejaException(e,ck);
    }
}

function _p52_registrarConnection(con)
{
    debug('_p52_registrarConnection con:',con,'.');
    var ck = 'Registrando conexi\u00f3n';
    try
    {
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url      : _p52_urlRegistrarConnection
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.idorigen'  : con.sourceId
                ,'params.iddestin'  : con.targetId
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al registrar conexi\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### +conex:',json);
                    if(json.success==true)
                    {
                        con.connection.cdaccion=json.params.cdaccion;
                        debug('con:',con);
                    }
                    else
                    {
                        toolkit.detach(con);
                        mensajeError(json.message);
                    }
                }
                catch(e)
                {
                    toolkit.detach(con);
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                toolkit.detach(con);
                _setLoading(false,_p52_panelDibujo);
                errorComunicacion(null,'Error al registrar conexi\u00f3n');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p52_borrarConnection(cdaccion)
{
    debug('_p52_borrarConnection cdaccion:',cdaccion,'.');
    var ck = 'Borrando conexi\u00f3n';
    try
    {
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url      : _p52_urlBorrarConnection
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdaccion'  : cdaccion
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al borrar conexi\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### +conex:',json);
                    if(json.success==true)
                    {
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
                _setLoading(false,_p52_panelDibujo);
                errorComunicacion(null,'Error al borrar conexi\u00f3n');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p52_guardarCoords(callback)
{
    debug('_p52_guardarCoords callback?',!Ext.isEmpty(callback));
    var ck = 'Guardando coordenadas';
    try
    {
        var jsonData =
        {
            params :
            {
                cdtipflu   : _p52_selectedFlujo.get('CDTIPFLU')
                ,cdflujomc : _p52_selectedFlujo.get('CDFLUJOMC')
            }
            ,list : []
        };
        
        ck       = 'Recopilando coordenadas';
        var divs = $('.entidad');
        debug('divs:',divs);
        
        for(var i=0;i<divs.length;i++)
        {
            var divi = divs[i];
            jsonData.list.push(
            {
                clave  : $(divi).attr('clave')
                ,webid : $(divi).attr('id')
                ,xpos  : divi.offsetLeft
                ,ypos  : divi.offsetTop
                ,tipo  : $(divi).attr('tipo')
            });
        }
        
        debug('jsonData:',jsonData);
        
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url       : _p52_urlGuardarCoords
            ,jsonData : jsonData
            ,success  : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al guardar coordenadas';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar coords:',json);
                    if(json.success==true)
                    {
                        mensajeCorrecto('Coordenadas guardadas','Coordenadas guardadas',callback);
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
            ,failure  : function()
            {
                _setLoading(false,_p52_panelDibujo);
                errorComunicacion(null,'Error al guardar coordenadas');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelDibujo);
        manejaException(e,ck);
    }
}

function _p52_guardarCatalogo(boton,tipo)
{
    debug('_p52_guardarCatalogo boton,tipo:',boton,tipo,'.');
    var ck   = 'Guardando cat\u00e1logo '+tipo;
    var win  = boton.up('window');
    var form = boton.up('form').getForm();
    try
    {
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var datos = _formValuesToParams(form.getValues());
        datos['params.tipo'] = tipo;
        
        _setLoading(true,win);
        Ext.Ajax.request(
        {
            url      : _p52_urlMovimientoCatalogo
            ,params  : datos
            ,success : function(response)
            {
                _setLoading(false,win);
                var ck = 'Decodificando respuesta al guardar cat\u00e1logo tipo'+tipo;
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar cat,json:',tipo,json,'.');
                    if(json.success==true)
                    {
                        win.hide();
                        if(tipo=='E')
                        {
                            _p52_cargarEstados();
                        }
                        else if(tipo=='P')
                        {
                            _p52_cargarPantallas();
                        }
                        else if(tipo=='C')
                        {
                            _p52_cargarComponentes();
                        }
                        else if(tipo=='O')
                        {
                            _p52_cargarProcesos();
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
                _setLoading(false,win);
                errorComunicacion(null,'Error guardando cat\u00e1logo '+tipo);
            }
        });
    }
    catch(e)
    {
        _setLoading(false,win);
        manejaException(e,ck);
    }
}

function _p52_cargarModelado()
{
    debug('_p52_cargarModelado');
    var ck = 'Borrando modelado';
    try
    {
        var divs = $('.entidad');
        debug('divs:',divs);
        
        for(var i=0;i<divs.length;i++)
        {
            _p52_cargando = true;
            toolkit.remove(divs[i].id);
            _p52_cargando = false;
        }
    
        ck = 'Recuperando modelado';
    
        _setLoading(true,_p52_panelDibujo);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarModelado
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelDibujo);
                var ck = 'Decodificando respuesta al cargar modelado';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### modelado:',json);
                    if(json.success==true)
                    {
                        if(json.list.length>0)
                        {
                            for(var i=0;i<json.list.length;i++)
                            {
                                var ite = json.list[i];
                                
                                var id      = ite.WEBID;
                                var tipo    = ite.TIPO;
                                var y       = ite.YPOS;
                                var x       = ite.XPOS;
                                
                                if(tipo=='E')
                                {
                                    clave   = ite.CDESTADOMC;
                                    descrip = ite.DSESTADOMC;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='P')
                                {
                                    clave   = ite.CDPANTMC;
                                    descrip = ite.DSPANTMC;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='C')
                                {
                                    clave   = ite.CDCOMPMC;
                                    descrip = ite.DSCOMPMC;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='O')
                                {
                                    clave   = ite.CDPROCMC;
                                    descrip = ite.DSPROCMC;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='V')
                                {
                                    clave   = ite.CDVALIDA;
                                    descrip = ite.DSVALIDA;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='R')
                                {
                                    clave   = ite.CDREVISI;
                                    descrip = ite.DSREVISI;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='T')
                                {
                                    clave   = ite.CDTITULO;
                                    descrip = ite.DSTITULO;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y,0);
                                }
                                else if(tipo=='M')
                                {
                                    clave   = ite.CDMAIL;
                                    descrip = ite.DSMAIL;
                                    _p52_addDiv(id,tipo,clave,descrip,x,y);
                                }
                                else if(tipo=='A')
                                {
                                    _p52_cargando = true;
                                    var con = toolkit.connect(
                                    {
                                        source   : _p52_addEndpoint(ite.IDORIGEN  , $('#'+ite.IDORIGEN).attr('tipo'))
                                        , target :_p52_addEndpoint(ite.IDDESTIN , $('#'+ite.IDDESTIN).attr('tipo'))
                                    });
                                    con.cdaccion = ite.CDACCION;
                                    _p52_cargando = false;
                                    
                                    // JTEZVA 2016 12 05
                                    con.dsaccion = ite.DSACCION;
                                    con.bind('mouseover', function (conn) {
                                        debug('mouseover args:', arguments);
                                        conn.addOverlay(['Label',
                                            {
                                                label    : '<span style="background:white;font-size:8px;">' + (conn.dsaccion || '(vacio)') + '</span>',
                                                location : 0.1,
                                                id       : "connLabel"
                                            }
                                        ]);
                                        conn.addOverlay(['Label',
                                            {
                                                label    : '<span style="background:white;font-size:8px;">' + (conn.dsaccion || '(vacio)') + '</span>',
                                                location : 0.9,
                                                id       : "connLabel2"
                                            }
                                        ]);
                                    });
                                    con.bind('mouseout', function (conn) {
                                        debug('mouseout args:', arguments);
                                        conn.removeOverlay("connLabel");
                                        conn.removeOverlay("connLabel2");
                                    });
                                }
                            }
                        }
                    }
                    else
                    {
                        mensajeError(json.message,function()
                        {
                            _p52_navega(1);
                        });
                    }
                }
                catch(e)
                {
                    _p52_navega(1);
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                _setLoading(false,_p52_panelDibujo);
                _p52_navega(1);
                errorComunicacion(null,'Error al cargar modelado');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelDibujo);
        _p52_navega(1);
        manejaException(e,ck);
    }
}

function _p52_addDiv(id,tipo,clave,descrip,x,y)
{
    debug('_p52_addDiv arguments:',arguments,'.');
    
    if(Ext.isEmpty(descrip))
    {
        descrip = '';
    }
    
    if(tipo=='E')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelE">'+clave+' - '+descrip+'</div></div>');
    }
    else if(tipo=='S')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelS">'+clave+' - '+descrip+'</div></div>');
    }
    else if(tipo=='P')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelP">'+clave+' - '+descrip+'</div></div>');
    }
    else if(tipo=='C')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelC">'+clave+' - '+descrip+'</div></div>');
    }
    else if(tipo=='O')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelO">'+clave+' - '+descrip+'</div></div>');
    }
    else if(tipo=='V')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelV">'+descrip+'</div></div>');
    }
    else if(tipo=='R')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelR">'+descrip+'</div></div>');
    }
    else if(tipo=='T')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+clave+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelT labelT'+clave+'">'+descrip+'</div></div>');
    }
    else if(tipo=='M')
    {
        $('#canvasdiv').append('<div id="'+id+'" tipo="'+tipo+'" clave="'+clave+'" class="entidad entidad'+tipo+'" style="top:'+y+'px;left:'+x+'px;" title="'+descrip+'"><a href="#" onclick="_p52_addEndpoint(\''+id+'\',\''+tipo+'\');return false;" class="plus"></a><a href="#" onclick="_p52_editEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;" class="edit"></a><a class="remove" href="#" onclick="_p52_removeEndpoint(\''+id+'\',\''+tipo+'\',\''+clave+'\');return false;"></a><div class="labelV">'+descrip+'</div></div>');
    }
    toolkit.draggable(id,
    {
        snapThreshold : 20
        ,grid         : [20,20]
    });
}

function _p52_cargarDatosEstado(cdestadomc)
{
    debug('_p52_cargarDatosEstado cdestadomc:',cdestadomc,'.');
    var ck = 'Borrando datos de status';
    try
    {
    
        var grid = _fieldById('_p52_gridEstRol');
        grid.store.each(function(record)
        {
            record.set('SWVER'     , false);
            record.set('SWTRABAJO' , false);
            record.set('SWVERDEF'  , false);
            record.set('SWCOMPRA'  , false);
            record.set('SWREASIG'  , false);
        });
        
        grid = _fieldById('_p52_gridEstAvi');
        grid.store.removeAll();
        
        _p52_panelEstado.down('form').getForm().reset();
    
        ck = 'Recuperando datos de status';
    
        _setLoading(true,_p52_panelEstado);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosEstado
            ,params  :
            {
                'params.cdtipflu'    : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc'  : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdestadomc' : cdestadomc
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelEstado);
                var ck = 'Decodificando respuesta al recuperar datos de status';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos estado:',json);
                    if(json.success==true)
                    {
                        _p52_panelEstado.down('form').loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_panelEstado.down('[name=ACCION]').setValue('U');
                        
                        _p52_panelEstado.setTitle('Status '+cdestadomc);
                        
                        var grid = _fieldById('_p52_gridEstRol');
                        grid.store.each(function(record)
                        {
                            for(var i=0;i<json.list.length;i++)
                            {
                                var ite = json.list[i];
                                if(ite.TIPO=='P'&&ite.CDSISROL==record.get('CDSISROL'))
                                {
                                    if('S'==ite.SWVER)
                                    {
                                        record.set('SWVER',true);
                                    }
                                    if('S'==ite.SWTRABAJO)
                                    {
                                        record.set('SWTRABAJO',true);
                                    }
                                    if('S'==ite.SWVERDEF)
                                    {
                                        record.set('SWVERDEF',true);
                                    }
                                    if('S'==ite.SWCOMPRA)
                                    {
                                        record.set('SWCOMPRA',true);
                                    }
                                    if('S'==ite.SWREASIG)
                                    {
                                        record.set('SWREASIG',true);
                                    }
                                }
                            }
                        });
                        
                        grid = _fieldById('_p52_gridEstAvi');
                        for(var i=0;i<json.list.length;i++)
                        {
                            var ite = json.list[i];
                            if(ite.TIPO=='A')
                            {
                                grid.store.add(ite);
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
                _setLoading(false,_p52_panelEstado);
                errorComunicacion(null,'Error al recuperar datos de status');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelEstado);
        manejaException(e,ck);
    }
}

function _p52_cargarDatosRevision(cdrevisi)
{
    debug('_p52_cargarDatosRevision cdrevisi:',cdrevisi,'.');
    var ck = 'Borrando datos de revisi\u00f3n';
    try
    {
        var grid = _fieldById('_p52_gridRevDoc');
        grid.getView().features[0].collapseAll();
        grid.store.each(function(record)
        {
            record.set('SWLISTA'  , false);
            record.set('SWOBLIGA' , false);
        });
        
        grid = _fieldById('_p52_gridRevReq');
        grid.getView().features[0].collapseAll();
        grid.store.each(function(record)
        {
            record.set('SWLISTA'  , false);
            record.set('SWOBLIGA' , false);
        });
        
        _p52_panelRevision.down('form').getForm().reset();
    
        ck = 'Recuperando datos de revisi\u00f3n';
    
        _setLoading(true,_p52_panelRevision);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosRevision
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdrevisi'  : cdrevisi
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelRevision);
                var ck = 'Decodificando respuesta al recuperar datos de revisi\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos revision:',json);
                    if(json.success==true)
                    {
                        _p52_panelRevision.down('form').loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_panelRevision.down('[name=ACCION]').setValue('U');
                        
                        var grid = _fieldById('_p52_gridRevDoc');
                        grid.store.each(function(record)
                        {
                            for(var i=0;i<json.list.length;i++)
                            {
                                var ite = json.list[i];
                                
                                if (ite.TIPO === 'DOC' && ite.CDDOCUME == record.get('CDDOCUME') && 'S' == ite.SWOBLIGA)
                                {
                                    record.set('SWOBLIGA',true);
                                }
                                
                                if (ite.TIPO === 'DOC' && ite.CDDOCUME == record.get('CDDOCUME') && 'S' == ite.SWLISTA)
                                {
                                    record.set('SWLISTA',true);
                                }
                            }
                        });
                        
                        grid = _fieldById('_p52_gridRevReq');
                        grid.store.each(function(record)
                        {
                            for(var i=0;i<json.list.length;i++)
                            {
                                var ite = json.list[i];
                                
                                if (ite.TIPO === 'REQ' && ite.CDREQUISI == record.get('CDREQUISI') && 'S' == ite.SWOBLIGA)
                                {
                                    record.set('SWOBLIGA',true);
                                }
                                
                                if (ite.TIPO === 'REQ' && ite.CDREQUISI == record.get('CDREQUISI') && 'S' == ite.SWLISTA)
                                {
                                    record.set('SWLISTA',true);
                                }
                            }
                        });
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
                _setLoading(false,_p52_panelRevision);
                errorComunicacion(null,'Error al recuperar datos de revisi\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelRevision);
        manejaException(e,ck);
    }
}

function _p52_cargarDatosTitulo(webid)
{
    debug('_p52_cargarDatosTitulo webid:',webid,'.');
    var ck = 'Borrando datos de revisi\u00f3n';
    try
    {
        _p52_panelTitulo.down('form').getForm().reset();
    
        ck = 'Recuperando datos de t\u00edtulo';
    
        _setLoading(true,_p52_panelTitulo);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosTitulo
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.webid'     : webid
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelTitulo);
                var ck = 'Decodificando respuesta al recuperar datos de t\u00edtulo';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos titulo:',json);
                    if(json.success==true)
                    {
                        _p52_panelTitulo.down('form').loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_panelTitulo.down('[name=ACCION]').setValue('U');
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
                _setLoading(false,_p52_panelTitulo);
                errorComunicacion(null,'Error al recuperar datos de t\u00edtulo');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelTitulo);
        manejaException(e,ck);
    }
}

function _p52_cargarDatosValidacion(cdvalida)
{
    debug('_p52_cargarDatosValidacion cdvalida:',cdvalida,'.');
    var ck = 'Borrando datos de validaci\u00f3n';
    try
    {
        _p52_formValidacion.getForm().reset();
    
        ck = 'Recuperando datos de validaci\u00f3n';
    
        _setLoading(true,_p52_formValidacion);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosValidacion
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdvalida'  : cdvalida
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_formValidacion);
                var ck = 'Decodificando respuesta al recuperar datos de validaci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos validacion:',json);
                    if(json.success==true)
                    {
                        _p52_formValidacion.loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_formValidacion.down('[name=ACCION]').setValue('U');
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
                _setLoading(false,_p52_formValidacion);
                errorComunicacion(null,'Error al recuperar datos de validaci\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_formValidacion);
        manejaException(e,ck);
    }
}

function _p52_cargarDatosCorreo(cdmail)
{
	debug('_p52_cargarDatosCorreo cdmail:',cdmail,'.');
    var ck = 'Borrando datos de validaci\u00f3n';
    try
    {
        _p52_formCorreos.getForm().reset();
    
        ck = 'Recuperando datos de validaci\u00f3n';
    
        _setLoading(true,_p52_formCorreos);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosCorreo
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdmail'    : cdmail
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_formCorreos);
                var ck = 'Decodificando respuesta al recuperar datos de validaci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos validacion:',json);
                    if(json.success==true)
                    {
                        _p52_formCorreos.loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_formCorreos.down('[name=ACCION]').setValue('U');
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
                _setLoading(false,_p52_formCorreos);
                errorComunicacion(null,'Error al recuperar datos de validaci\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_formCorreos);
        manejaException(e,ck);
    }
}

function _p52_cargarDatosAccion(cdaccion)
{
    debug('_p52_cargarDatosAccion cdaccion:',cdaccion,'.');
    var ck = 'Borrando datos de acci\u00f3n';
    try
    {
        var grid = _fieldById('_p52_gridAccRol');
        grid.store.each(function(record)
        {
            record.set('SWPERMISO' , false);
        });
        
        _p52_panelAccion.down('form').getForm().reset();
        
        $('[name=iconoaccion]:checked').prop('checked',false);
        
        ck = 'Recuperando datos de acci\u00f3n';
    
        _setLoading(true,_p52_panelAccion);
        Ext.Ajax.request(
        {
            url      : _p52_urlCargarDatosAccion
            ,params  :
            {
                'params.cdtipflu'   : _p52_selectedFlujo.get('CDTIPFLU')
                ,'params.cdflujomc' : _p52_selectedFlujo.get('CDFLUJOMC')
                ,'params.cdaccion'  : cdaccion
            }
            ,success : function(response)
            {
                _setLoading(false,_p52_panelAccion);
                var ck = 'Decodificando respuesta al recuperar datos de acci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos accion:',json);
                    if(json.success==true)
                    {
                        _p52_panelAccion.down('form').loadRecord(
                        {
                            getData : function()
                            {
                                return json.params;
                            }
                        });
                        _p52_panelAccion.down('[name=ACCION]').setValue('U');
                        
                        var grid = _fieldById('_p52_gridAccRol');
                        grid.store.each(function(record)
                        {
                            for(var i=0;i<json.list.length;i++)
                            {
                                var ite = json.list[i];
                                if(ite.CDSISROL==record.get('CDSISROL')&&'S'==ite.SWPERMISO)
                                {
                                    record.set('SWPERMISO',true);
                                }
                            }
                        });
                        
                        $('[name=iconoaccion][value='+json.params.CDICONO+']').prop('checked',true);
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
                _setLoading(false,_p52_panelAccion);
                errorComunicacion(null,'Error al recuperar datos de acci\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelAccion);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosEstado(bot,callback)
{
    debug('_p52_guardarDatosEstado');
    var ck = 'Guardando datos de status';
    try
    {
        var form  = _p52_panelEstado.down('form').getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var jsonData =
        {
            params : form.getValues()
            ,list  : []
        };
        
        var grid = _fieldById('_p52_gridEstRol');
        grid.store.each(function(record)
        {
            var datos       = record.getData();
            datos.TIPO      = 'P';
            datos.SWVER     = record.get("SWVER")     ? "S" : "N";
            datos.SWTRABAJO = record.get("SWTRABAJO") ? "S" : "N";
            datos.SWVERDEF  = record.get("SWVERDEF")  ? "S" : "N";
            datos.SWCOMPRA  = record.get("SWCOMPRA")  ? "S" : "N";
            datos.SWREASIG  = record.get("SWREASIG")  ? "S" : "N";
            jsonData.list.push(datos);
        });
        
        grid = _fieldById('_p52_gridEstAvi');
        grid.store.each(function(record)
        {
            var datos  = record.getData();
            datos.TIPO = 'A';
            jsonData.list.push(datos);
        });
        
        debug('jsonData:',jsonData);
        
        _setLoading(true,_p52_panelEstado);
        Ext.Ajax.request(
        {
            url       : _p52_urlGuardarDatosStatus
            ,jsonData : jsonData
            ,success  : function(response)
            {
                _setLoading(false,_p52_panelEstado);
                var ck = 'Decodificando respuesta al guardar datos de status';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_panelEstado);
                errorComunicacion(null,'Error al guardar datos de status');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelEstado);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosRevision(bot,callback)
{
    debug('_p52_guardarDatosRevision');
    var ck = 'Guardando datos de revisi\u00f3n';
    try
    {
        var form  = _p52_panelRevision.down('form').getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var jsonData =
        {
            params : form.getValues()
            ,list  : []
        };
        
        var grid = _fieldById('_p52_gridRevDoc');
        grid.store.each(function(record)
        {
            var datos      = record.getData();
            datos.TIPO     = 'DOC';
            datos.SWLISTA  = record.get("SWLISTA")  ? "S" : "N";
            datos.SWOBLIGA = record.get("SWOBLIGA") ? "S" : "N";
            jsonData.list.push(datos);
        });
        
        grid = _fieldById('_p52_gridRevReq');
        grid.store.each(function(record)
        {
            var datos      = record.getData();
            datos.TIPO     = 'REQ';
            datos.SWLISTA  = record.get("SWLISTA")  ? "S" : "N";
            datos.SWOBLIGA = record.get("SWOBLIGA") ? "S" : "N";
            jsonData.list.push(datos);
        });
        
        debug('jsonData:',jsonData);
        
        _setLoading(true,_p52_panelRevision);
        Ext.Ajax.request(
        {
            url       : _p52_urlGuardarDatosRevision
            ,jsonData : jsonData
            ,success  : function(response)
            {
                _setLoading(false,_p52_panelRevision);
                var ck = 'Decodificando respuesta al guardar datos de revisi\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_panelRevision);
                errorComunicacion(null,'Error al guardar datos de revisi\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelRevision);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosTitulo(bot,callback)
{
    debug('_p52_guardarDatosTitulo');
    var ck = 'Guardando datos de t\u00edtulo';
    try
    {
        var form  = _p52_panelTitulo.down('form').getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        _setLoading(true,_p52_panelTitulo);
        Ext.Ajax.request(
        {
            url       : _p52_urlGuardarDatosTitulo
            ,params   : _formValuesToParams(form.getValues())
            ,success  : function(response)
            {
                _setLoading(false,_p52_panelTitulo);
                var ck = 'Decodificando respuesta al guardar datos de t\u00edtulo';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_panelTitulo);
                errorComunicacion(null,'Error al guardar datos de t\u00edtulo');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelTitulo);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosAccion(bot,callback)
{
    debug('_p52_guardarDatosAccion');
    var ck = 'Guardando datos de acci\u00f3n';
    try
    {
        var form  = _p52_panelAccion.down('form').getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        var jsonData =
        {
            params : form.getValues()
            ,list  : []
        };
        
        var grid = _fieldById('_p52_gridAccRol');
        grid.store.each(function(record)
        {
            var datos       = record.getData();
            datos.SWPERMISO = record.get("SWPERMISO") ? "S" : "N";
            jsonData.list.push(datos);
        });
        
        jsonData.params.CDICONO = $('[name=iconoaccion]:checked').val();
        
        debug('jsonData:',jsonData);
        
        _setLoading(true,_p52_panelAccion);
        Ext.Ajax.request(
        {
            url       : _p52_urlGuardarDatosAccion
            ,jsonData : jsonData
            ,success  : function(response)
            {
                _setLoading(false,_p52_panelAccion);
                var ck = 'Decodificando respuesta al guardar datos de acci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_panelAccion);
                errorComunicacion(null,'Error al guardar datos de revisi\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_panelAccion);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosValidacion(bot,callback)
{
    debug('_p52_guardarDatosValidacion');
    var ck = 'Guardando datos de validaci\u00f3n';
    try
    {
        var form  = _p52_formValidacion.getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        _setLoading(true,_p52_formValidacion);
        Ext.Ajax.request(
        {
            url      : _p52_urlGuardarDatosValidacion
            ,params  : _formValuesToParams(form.getValues())
            ,success : function(response)
            {
                _setLoading(false,_p52_formValidacion);
                var ck = 'Decodificando respuesta al guardar datos de validaci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_formValidacion);
                errorComunicacion(null,'Error al guardar datos de validaci\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_formValidacion);
        manejaException(e,ck);
    }
}

function _p52_guardarDatosCorreo(bot,callback)
{
    debug('_p52_guardarDatosCorreo');
    var ck = 'Guardando datos de correo';
    try
    {
        var form  = _p52_formCorreos.getForm();
        if(!form.isValid())
        {
            throw 'Favor de revisar los datos';
        }
        
        _setLoading(true,_p52_formCorreos);
        Ext.Ajax.request(
        {
            url      : _p52_urlGuardarDatosCorreo
            ,params  : _formValuesToParams(form.getValues())
            ,success : function(response)
            {
                _setLoading(false,_p52_formCorreos);
                var ck = 'Decodificando respuesta al guardar datos de validaci\u00f3n';
                try
                {
                    var json = Ext.decode(response.responseText);
                    if(json.success==true)
                    {
                        callback(bot);
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
                _setLoading(false,_p52_formCorreos);
                errorComunicacion(null,'Error al guardar datos de validaci\u00f3n');
            }
        });
    }
    catch(e)
    {
        _setLoading(false,_p52_formCorreos);
        manejaException(e,ck);
    }
}

function _p52_actualizaLabel(tipo,webid,label)
{
    debug('_p52_actualizaLabel tipo,webid,label:',tipo,webid,label,'.');
    $('#'+webid+'>.label'+tipo).html(label);
}

//_p52_ejecutaValidacion(7,5,'M',32,1,245736709260000000,'ALVARO');
function _p52_ejecutaValidacion(cdunieco,cdramo,estado,nmpoliza,nmsituac,nmsuplem,cdvalidafk)
{
    Ext.Ajax.request(
    {
        url : '<s:url namespace="/flujomesacontrol" action="ejecutaValidacion" />'
        ,params :
        {
            'params.cdunieco'    : cdunieco
            ,'params.cdramo'     : cdramo
            ,'params.estado'     : estado
            ,'params.nmpoliza'   : nmpoliza
            ,'params.nmsituac'   : nmsituac
            ,'params.nmsuplem'   : nmsuplem
            ,'params.cdvalidafk' : cdvalidafk
        }
        ,success : function(response)
        {
            alert();
            debug(Ext.decode(response.responseText));
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
}

function _p52_ventanaTips(tips)
{
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        modal        : true
        ,title       : 'Tips'
        ,width       : 450
        ,height      : 230
        ,style       : 'padding:5px;'
        ,border      : 0
        ,html        : '<p>'+(tips.join('<p>'))
        ,closeAction : 'destroy'
        ,autoScroll  : true
        ,buttonAlign : 'center'
        ,buttons     :
        [{
            text     : 'Continuar'
            ,icon    : '${icons}accept.png'
            ,handler : function(me)
            {
                me.up('window').destroy();
            }
        }]
    }).show());
}

function _p52_mostrarVentanaVariablesCorreo(nameVar, name){	
	debug('_p52_mostrarVentanaVariablesCorreo',name);
	_p52_winVarsCorreo.down('[itemId=panVars]').setValue(_p52_formCorreos.down('[name='+nameVar+']').getValue());
	_p52_winVarsCorreo.nameCmpVar = nameVar;
	_p52_winVarsCorreo.nameCmp    = name;
	_p52_winVarsCorreo.show();
	
}

function _p52_numberContainsSubstrInStr(subStr, str){
	var lastIndex = 0,
 		count     = 0;
 	while(lastIndex != -1){
 		lastIndex = str.indexOf(subStr,lastIndex);
 		if(lastIndex != -1){
        	count ++;
        	lastIndex += subStr.length;
    	}
 	}
 	return count;
}
////// funciones //////

</script>
</head>
<body>
<div id="_p52_divpri" style="height:740px;border:1px solid #CCCCCC;"></div>
</body>
</html>
// MAXIMO DE UN PARAMETRO VARCHAR PARA UNA EXPRESION: #PL['F_CONSULTA_DINAMICA';&VNUMTRA;'12345678901234567890123456789012345678901234567890123456X']