<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// overrides //////
////// overrides //////
////// urls //////
var _p30_urlCargarSumaAseguradaRamo5          = '<s:url namespace="/emision"         action="cargarSumaAseguradaRamo5"            />';
var _p30_urlCargarCduniecoAgenteAuto          = '<s:url namespace="/emision"         action="cargarCduniecoAgenteAuto"            />';
var _p30_urlCargarRetroactividadSuplemento    = '<s:url namespace="/emision"         action="cargarRetroactividadSuplemento"      />';
var _p30_urlCargarParametros                  = '<s:url namespace="/emision"         action="obtenerParametrosCotizacion"         />';
var _p30_urlRecuperarCliente                  = '<s:url namespace="/"                action="buscarPersonasRepetidas"             />';
var _p30_urlCargarCatalogo                    = '<s:url namespace="/catalogos"       action="obtieneCatalogo"                     />';
var _p30_urlCotizar                           = '<s:url namespace="/emision"         action="cotizarAutosFlotilla"                />';
var _p30_urlCargaMasiva                       = '<s:url namespace="/emision"         action="procesarCargaMasivaFlotilla"         />';
var _p30_urlCargar                            = '<s:url namespace="/emision"         action="cargarCotizacionAutoFlotilla"        />';
var _p30_urlRecuperacionSimple                = '<s:url namespace="/emision"         action="recuperacionSimple"                  />';
var _p30_urlRecuperacionSimpleLista           = '<s:url namespace="/emision"         action="recuperacionSimpleLista"             />';
var _p30_urlComprar                           = '<s:url namespace="/flujocotizacion" action="comprarCotizacion4"                  />';
var _p30_urlDatosComplementarios              = '<s:url namespace="/emision"         action="emisionAutoFlotilla"                 />';
var _p30_urlCargarTipoCambioWS                = '<s:url namespace="/emision"         action="cargarTipoCambioWS"                  />';
var _p30_urlNada                              = '<s:url namespace="/emision"         action="webServiceNada"                      />';
var _p30_urlCargarObligatorioCamionRamo5      = '<s:url namespace="/emision"         action="cargarObligatorioTractocamionRamo5"  />';
var _p30_urlViewDoc                           = '<s:url namespace="/documentos"      action="descargaDocInline"                   />';
var _p30_urlEnviarCorreo                      = '<s:url namespace="/general"         action="enviaCorreo"                         />';
var _p30_urlCargarDatosEndoso                 = '<s:url namespace="/emision"         action="recuperarDatosEndosoAltaIncisoAuto"  />';
var _p30_urlObtencionReporteExcel             = '<s:url namespace="/reportes"        action="procesoObtencionReporte"             />';
var _p30_urlObtencionReporteExcel2            = '<s:url namespace="/reportes"        action="procesoObtencionReporte2"            />';
var _p30_urlDetalleTramite                    = '<s:url namespace="/mesacontrol"     action="movimientoDetalleTramite"            />';
var _p30_urlActualizarOtvalorTramiteXDsatribu = '<s:url namespace="/emision"         action="actualizarOtvalorTramitePorDsatribu" />';
var _p30_urlRecuperarOtvalorTramiteXDsatribu  = '<s:url namespace="/emision"         action="recuperarOtvalorTramitePorDsatribu"  />';
var url_PantallaPreview                       = '<s:url namespace="/endosos"         action="includes/previewEndosos"             />';
var _p48_urlMovimientos                       = '<s:url namespace="/movimientos"     action="ejecutar"                            />';
var _p30_urlConfirmarEndoso                   = '<s:url namespace="/endosos"         action="confirmarEndosoAltaIncisoAuto"       />';
var _p30_urlCargarPolizaSIGS                  = '<s:url namespace="/emision"         action="cargarPoliza"                        />';

var _p30_urlRecuperarDatosTramiteValidacion = '<s:url namespace="/flujomesacontrol" action="recuperarDatosTramiteValidacionCliente" />';
var _p29_urlObtieneValNumeroSerie           = '<s:url namespace="/emision"          action="obtieneValNumeroSerie"                  />';

var MontoMaximo = 0;
var MontoMinimo = 0;

var _p30_urlImprimirCotiza = '<s:property value="rutaServidorReports" />';
var _p30_reportsServerUser = '<s:property value="passServidorReports" />';
var _p30_urlRecuperacion = '<s:url namespace="/recuperacion" action="recuperar"/>';
var _RUTA_DOCUMENTOS_TEMPORAL = '<s:text name="ruta.documentos.temporal" />';
var _p30_descuento = false;

var cargarXpoliza = false;
////// urls //////

////// variables //////

        Ext.util.Format.thousandSeparator=",";
        Ext.define("Ext.override.ThousandSeparatorNumberField", {
        override: "Ext.form.field.Number",
            
            /**
            * @cfg {Boolean} useThousandSeparator
            */
            useThousandSeparator: false,
            
            /**
             * @inheritdoc
             */
            toRawNumber: function (value) {
                
                var cadena = String(value).replace(this.decimalSeparator,'.').replace(new RegExp(Ext.util.Format.thousandSeparator, "g"), '');

                return cadena;
            },
            
            /**
             * @inheritdoc
             */
            getErrors: function (value) {
                if (!this.useThousandSeparator) return this.callParent(arguments);
                
                var me = this,
                    errors = Ext.form.field.Text.prototype.getErrors.apply(me, arguments),
                    format = Ext.String.format,
                    num;

                value = Ext.isDefined(value) ? value : this.processRawValue(this.getRawValue());

                if (value.length < 1) { // if it's blank and textfield didn't flag it then it's valid
                    return errors;
                }

                value = me.toRawNumber(value);

                if (isNaN(value.replace(Ext.util.Format.thousandSeparator,''))) {
                    errors.push(format(me.nanText, value));
                }

                num = me.parseValue(value);

                if (me.minValue === 0 && num < 0) {
                    errors.push(this.negativeText);
                }
                else if (num < me.minValue) {
                    errors.push(format(me.minText, me.minValue));
                }

                if (num > me.maxValue) {
                    errors.push(format(me.maxText, me.maxValue));
                }

                return errors;
            },
            
            /**
             * @inheritdoc
             */
             valueToRaw: function (value) {
                if (!this.useThousandSeparator) return this.callParent(arguments);
                
                var me = this;
                var format = "000,000";
                
                for (var i = 0; i < me.decimalPrecision; i++) {
                    if (i == 0) format += ".";
                    format += "0";
                }
                
                value = me.parseValue(Ext.util.Format.number(value, format));
                value = me.fixPrecision(value);
                value = Ext.isNumber(value) ? value : parseFloat(me.toRawNumber(value));
                value = isNaN(value) ? '' : String(Ext.util.Format.number(value, format)).replace('.', me.decimalSeparator);

                return value;
            },
            
            /**
             * @inheritdoc
             */
            getSubmitValue: function () {
                if (!this.useThousandSeparator)
                    return this.callParent(arguments);
                var me = this,
                    value = me.callParent();

                if (true||!me.submitLocaleSeparator) {
                    value = me.toRawNumber(value);
                }
                debug("Cadena(value) 2: " + value);
//                 alert(value);
                return value;
            },
            
            /**
             * @inheritdoc
             */
            setMinValue: function (value) {
                if (!this.useThousandSeparator)
                    return this.callParent(arguments);
                var me = this,
                    allowed;

                me.minValue = Ext.Number.from(value, Number.NEGATIVE_INFINITY);
                me.toggleSpinners();

                // Build regexes for masking and stripping based on the configured options
                if (me.disableKeyFilter !== true) {
                    allowed = me.baseChars + '';

                    if (me.allowExponential) {
                        allowed += me.decimalSeparator + 'e+-';
                    }
                    else {
                        allowed += Ext.util.Format.thousandSeparator;
                        if (me.allowDecimals) {
                            allowed += me.decimalSeparator;
                        }
                        if (me.minValue < 0) {
                            allowed += '-';
                        }
                    }

                    allowed = Ext.String.escapeRegex(allowed);
                    me.maskRe = new RegExp('[' + allowed + ']');
                    if (me.autoStripChars) {
                        me.stripCharsRe = new RegExp('[^' + allowed + ']', 'gi');
                    }
                }
            },
            
            /**
             * @private
             */
            parseValue: function (value) {
                if (!this.useThousandSeparator)
                    return this.callParent(arguments);
                value = parseFloat(this.toRawNumber(value));
                return isNaN(value) ? null : value;
            }
        });

var _p30_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p30_smap1:',_p30_smap1);
///Parche para TL
if(_p30_smap1.cdtipsit=='ARTL'){
	_p30_smap1.cdtipsit='AR';
	_p30_smap1.cdtipsit2='TL';
}
/////////////////
var _p28_smap1 =
{
    cdtipsit : _p30_smap1.cdtipsit
};
debug('_p28_smap1:',_p28_smap1);

var _p30_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p30_flujo:',_p30_flujo);

var _p30_flujoAux = {};
try {
    if (!Ext.isEmpty(_p30_flujo.aux)) {
        _p30_flujoAux = Ext.decode(_p30_flujo.aux);
    }
} catch (e) {
    debugError('WARNING al decodificar _p30_flujo.aux como JSON:', _p30_flujo, e);
}
debug('_p30_flujoAux:', _p30_flujoAux);

var _p30_store                   = null;
var _p30_selectedRecord          = null;
var _p30_recordClienteRecuperado = null;
var _p30_selectedTarifa          = null;
var _p30_ventanaCdtipsit         = null;
var _p30_semaforoBorrar          = false;
var _p30_precioDolarDia          = null;

var _p30_storeCdtipsit       = null;
var _p30_storeMarcasRamo5    = null;
var _p30_storeSubmarcasRamo5 = null;
var _p30_storeVersionesRamo5 = null;
var _p30_storeUsosRamo5      = null;
var _p30_storePlanesRamo5    = null;
var _p30_storeCargasRamo5    = null;

var _p30_reporteCotizacion = '<s:text name='%{"rdf.cotizacion.flot.nombre."+smap1.cdtipsit.toUpperCase()}' />';

var _p30_endoso = _p30_smap1.endoso+'x'=='Sx';

var _p30_bufferAutos = [];

// var rolesSuscriptores ='|SUSCRIAUTO|TECNISUSCRI|EMISUSCRI|JEFESUSCRI|GERENSUSCRI|SUBDIRSUSCRI|';
////// variables //////

////// dinamicos //////
var _p30_gridColsConf =
[
    <s:if test='%{getImap().get("gridCols")!=null}'>
        <s:property value="imap.gridCols" />
    </s:if>
];
var _p30_gridCols =
[
    {
        dataIndex     : 'nmsituac'
        ,width        : 50
        ,menuDisabled : true
    }
    ,{
        sortable      : false
        ,menuDisabled : true
        ,dataIndex    : 'personalizado'
        ,width        : 30
        ,renderer     : function(v)
        {
            var r='';
            if(v+'x'=='six')
            {
                r='<img src="${ctx}/resources/fam3icons/icons/tag_blue_edit.png" />';
            }
            return r;
        }
    }
];
for(var i=0;i<_p30_gridColsConf.length;i++)
{
    _p30_gridCols.push(_p30_gridColsConf[i]);
}
for(var i=0;i<_p30_gridCols.length;i++)
{
    if(!Ext.isEmpty(_p30_gridCols[i].editor)&&_p30_gridCols[i].editor.readOnly)
    {
        _p30_gridCols[i].editor='';
    }
}
_p30_gridCols.push(
{
    xtype         : 'actioncolumn'
    ,menuDisabled : true
    ,sortable     : false
    ,width        : 50
    ,items        :
    [
        {
            tooltip  : 'Configurar paquete'
            ,icon    : '${ctx}/resources/fam3icons/icons/cog.png'
            ,iconBkp : '${ctx}/resources/fam3icons/icons/cog.png'
            ,handler : _p30_gridBotonConfigClic
            ,disabled	 : _p30_smap1.turistas=='S'
        }
        ,{
            tooltip  : 'Eliminar'
            ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
            ,iconBkp : '${ctx}/resources/fam3icons/icons/delete.png'
            ,handler : _p30_gridBotonEliminarClic
        }
    ]
}
);

// Inserta los cuadros para sucursal, ramo y poliza que cargaran del sigs al ice para renovacion
var _p30_panel7Items =
[
    {
        layout  :
        {
           type    : 'table'
          ,columns : 1
          ,style   : 'width: 20px !important;'
          ,align   : 'right'
       }
       ,border : 0
       ,items  :
       [
        {
                 xtype       : 'numberfield'
                ,itemId      : '_p30_numsuc'
                ,fieldLabel  : 'SUCURSAL'
                ,name        : 'sucursal'               
                ,sinOverride : true
                ,labelWidth  : 100
                ,style       : 'margin:0px;margin-left:5px;'//'margin:5px;margin-left:15px;width:20px !important;'
                ,value       : !Ext.isEmpty(_p30_smap1.renuniext) ? _p30_smap1.renuniext : ''
                ,listeners   :
                {
                    change : _p30_nmpolizaChange
                }
                ,readOnly    :  true 
            }
           ,{
                    xtype       : 'numberfield'
                   ,itemId      : '_p30_numram'
                   ,fieldLabel  : 'RAMO'
                   ,name        : 'ramo'                   
                   ,sinOverride : true                   
                   ,labelWidth  : 100
                   ,style       : 'margin:0px;margin-left:5px;'//'width : 30px !important;'
                   ,value       : !Ext.isEmpty(_p30_smap1.renramo) ? _p30_smap1.renramo : ''
                   ,listeners   :
                   {
                       change : _p30_nmpolizaChange
                   }
                   ,readOnly    :  true 
              }
             ,{
                  xtype       : 'numberfield'
                 ,itemId      : '_p30_numpol'
                 ,fieldLabel  : 'POLIZA'
                 ,name        : 'poliza'
                 ,sinOverride : true                 
                 ,labelWidth  : 100
                 ,style       : 'margin:0px;margin-left:5px;'//'width : 50px !important;'
                 ,value       : !Ext.isEmpty(_p30_smap1.renpoliex) ? _p30_smap1.renpoliex : ''
                 ,listeners   :
                 {
                     change : _p30_nmpolizaChange
                 }
                 ,readOnly    :  true 
           }
//           ,{
//                  xtype   : 'button'
//                 ,itemId  : '_p30_botonCargarPoliza'
//                 ,text    : 'BUSCAR'
//                 ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
//                 ,style   : 'margin-right'
//                 //,handler : _p30_cargarPoliza
//           }
       ]
    }
 ];

var _p30_panel1ItemsConf =
[
    <s:if test='%{getImap().get("panel1Items")!=null}'>
        <s:property value="imap.panel1Items" />
    </s:if>
];

var _p30_panel3ItemsConf =
[
    <s:if test='%{getImap().get("panel3Items")!=null}'>
        <s:property value="imap.panel3Items" />
    </s:if>
];

var _p30_panel5ItemsConf =
[
    <s:if test='%{getImap().get("panel5Items")!=null}'>
        <s:property value="imap.panel5Items" />
    </s:if>
];

var _p30_panel6ItemsConf =
[
    <s:if test='%{getImap().get("panel6Items")!=null}'>
        <s:property value="imap.panel6Items" />
    </s:if>
];

var _p30_paneles  = [];
<s:iterator value="imap"> 
    <s:if test='%{key.substring(0,"paneldin_".length()).equals("paneldin_")}'>
        _p30_paneles['<s:property value='%{key.substring("paneldin_".length())}' />']=Ext.create('Ext.window.Window',
        {
            title        : ''
            ,modal       : true
            ,closeAction : 'hide'
            ,maxHeight   : 600
            ,width       : 850
            ,autoScroll  : true
            ,callback    : false
            ,valores     : false
            ,items       :
            [
                Ext.create('Ext.form.Panel',
                {
                    itemId    : '_p30_form_panel_<s:property value='%{key.substring("paneldin_".length())}' />'
                    ,defaults : { style : 'margin:5px;' }
                    ,border   : 0
                    ,layout   :
                    {
                        type     : 'table'
                        ,columns : 3
                    }
                    ,items       : [ <s:property value="value" /> ]
                    ,buttonAlign : 'center'
                    ,buttons     :
                    [
                        {
                            text     : 'Aceptar'
                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                            ,handler : function(me){ me.up('window').callback(); }
                        }
                        ,{
                            text     : 'Cancelar'
                            ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                            ,handler : function(me){ me.up('window').hide();}
                        }
                    ]
                })
            ]
        });
    </s:if>
    <s:set var="contador" value="#contador+1" />
</s:iterator>

var _p30_gridTbarItems =
[
    {
        text     : 'Agregar'
        ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
        ,handler : function(){_p30_agregarAuto();}
    }
    ,'->'
];

var _f1_botones =[];
for(var i in _p30_smap1)
{
    if(i.slice(0,6)=='boton_')
    {
    	var text=i.split('_')[1];
        _f1_botones.push(
        {
            text      : text
            ,icon     : '${ctx}/resources/fam3icons/icons/cog.png'
            ,cdtipsit : _p30_smap1[i]
            ,handler  : function(me){_p30_configuracionPanelDinClic(me.cdtipsit,me.text);}
            ,hidden	  : (_p30_smap1.turistas!='S' && (text=='AUTO TURISTA' || text=='TURISTA LICENCIA')) || (_p30_smap1.turistas=='S' && text!='AUTO TURISTA' && text!='TURISTA LICENCIA')
        });
    }
}
if(_f1_botones.length>1)
{
    for(var i=0;i<_f1_botones.length-1;i++)
    {
        for(var j=i+1;j<_f1_botones.length;j++)
        {
            if(_f1_botones[j].text<_f1_botones[i].text)
            {
                var _f1_aux=_f1_botones[j];
                _f1_botones[j]=_f1_botones[i];
                _f1_botones[i]=_f1_aux;
            }
        }
    }
    for(var i=0;i<_f1_botones.length;i++)
    {
        _p30_gridTbarItems.push(_f1_botones[i]);
    }
}
else if(_f1_botones.length==1)
{
    _p30_gridTbarItems.push(_f1_botones[0]);
}

var _p30_editorCdtipsit=<s:property value="imap.cdtipsitItem" />;

var _p30_situaciones    = _p30_smap1.situacionesCSV.split(',');
debug('_p30_situaciones:',_p30_situaciones);

var _p30_tatrisitFullForms    = [];
var _p30_tatrisitParcialForms = [];
var _p30_tatrisitAutoWindows  = [];
<s:iterator value="imap">
    <s:if test='%{key.substring(0,"tatrisit_full_items_".length()).equals("tatrisit_full_items_")}'>
        _p30_tatrisitFullForms['<s:property value='%{key.substring("tatrisit_full_items_".length())}' />']=
        Ext.create('Ext.form.Panel',
        {
            items : [ <s:property value="value" /> ]
        });
    </s:if>
    <s:if test='%{key.substring(0,"tatrisit_parcial_items_".length()).equals("tatrisit_parcial_items_")}'>
        _p30_tatrisitParcialForms['<s:property value='%{key.substring("tatrisit_parcial_items_".length())}' />']=
        {
            xtype        : 'form'
            ,itemId      : '_p30_tatrisitParcialForm<s:property value='%{key.substring("tatrisit_parcial_items_".length())}' />'
            ,layout      : 'hbox'
            ,autoScroll  : true
            ,border      : 0
            ,hidden      : true
            ,items       : [ <s:property value="value" /> ]
            ,buttonAlign : 'center'
            ,buttons     :
            [
                {
                    text     : 'Aceptar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                    ,handler : function(bot) { _p30_editarAutoAceptar(bot); }
                }
                ,{
                    text     : 'B&uacute;squeda de veh&iacute;culo'
                    ,icon    : '${ctx}/resources/fam3icons/icons/car.png'
                    ,handler : function(bot) { _p30_editarAutoAceptar(bot,_p30_editarAutoBuscar); }
                    ,hidden	 : _p30_smap1.turistas=='S'
                }
            ]
        };
    </s:if>
    <s:if test='%{key.substring(0,"tatrisit_auto_items_".length()).equals("tatrisit_auto_items_")}'>
        _p30_tatrisitAutoWindows['<s:property value='%{key.substring("tatrisit_auto_items_".length())}' />']=
        Ext.create('Ext.window.Window',
        {
            modal        : true
            ,itemId      : '_p30_tatrisitAutoWindow<s:property value='%{key.substring("tatrisit_auto_items_".length())}' />'
            ,closeAction : 'hide'
            ,noMostrar   : false
            ,title       : 'B&Uacute;SQUEDA VEH&Iacute;CULO'
            ,items :
            [
                Ext.create('Ext.form.Panel',
                {
                    itemId      : '_p30_tatrisitAutoForm<s:property value='%{key.substring("tatrisit_auto_items_".length())}' />'
                    ,defaults   : { style:'margin:5px;' }
                    ,items      : [ <s:property value="value" /> ]
                    ,border     : 0
                    ,micallback : function(form)
                    {
                        mensajeWarning('Sin funci&oacute;n de retorno');
                    }
                    ,buttonAlign : 'center'
                    ,buttons     :
                    [
                        {
                            text     : 'Aceptar'
                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                            ,handler : _p30_tatrisitWindowAutoAceptarClic
                        }
                    ]
                })
            ]
        });
    </s:if>
</s:iterator>
debug('_p30_tatrisitFullForms:'    , _p30_tatrisitFullForms);
debug('_p30_tatrisitParcialForms:' , _p30_tatrisitParcialForms);
debug('_p30_tatrisitAutoWindows:'  , _p30_tatrisitAutoWindows);
////// dinamicos //////

Ext.onReady(function()
{
    //_grabarEvento('COTIZACION','ACCCOTIZA',null,null,_p30_smap1.cdramo);
    
	///Parche para TL
	if(_p30_smap1.cdtipsit=='ARTL'){
		_p30_smap1.cdtipsit='AR';
		_p30_smap1.cdtipsit2='TL';
	}
	/////////////////
	
    Ext.Ajax.timeout = 3*60*60*1000; // 1 hora
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    ////// modelos //////
    Ext.define('_p30_modelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
             'parametros.pv_otvalor01','parametros.pv_otvalor02','parametros.pv_otvalor03','parametros.pv_otvalor04','parametros.pv_otvalor05'
            ,'parametros.pv_otvalor06','parametros.pv_otvalor07','parametros.pv_otvalor08','parametros.pv_otvalor09','parametros.pv_otvalor10'
            ,'parametros.pv_otvalor11','parametros.pv_otvalor12','parametros.pv_otvalor13','parametros.pv_otvalor14','parametros.pv_otvalor15'
            ,'parametros.pv_otvalor16','parametros.pv_otvalor17','parametros.pv_otvalor18','parametros.pv_otvalor19','parametros.pv_otvalor20'
            ,'parametros.pv_otvalor21','parametros.pv_otvalor22','parametros.pv_otvalor23','parametros.pv_otvalor24','parametros.pv_otvalor25'
            ,'parametros.pv_otvalor26','parametros.pv_otvalor27','parametros.pv_otvalor28','parametros.pv_otvalor29','parametros.pv_otvalor30'
            ,'parametros.pv_otvalor31','parametros.pv_otvalor32','parametros.pv_otvalor33','parametros.pv_otvalor34','parametros.pv_otvalor35'
            ,'parametros.pv_otvalor36','parametros.pv_otvalor37','parametros.pv_otvalor38','parametros.pv_otvalor39','parametros.pv_otvalor40'
            ,'parametros.pv_otvalor41','parametros.pv_otvalor42','parametros.pv_otvalor43','parametros.pv_otvalor44','parametros.pv_otvalor45'
            ,'parametros.pv_otvalor46','parametros.pv_otvalor47','parametros.pv_otvalor48','parametros.pv_otvalor49','parametros.pv_otvalor50'
            ,'parametros.pv_otvalor51','parametros.pv_otvalor52','parametros.pv_otvalor53','parametros.pv_otvalor54','parametros.pv_otvalor55'
            ,'parametros.pv_otvalor56','parametros.pv_otvalor57','parametros.pv_otvalor58','parametros.pv_otvalor59','parametros.pv_otvalor60'
            ,'parametros.pv_otvalor61','parametros.pv_otvalor62','parametros.pv_otvalor63','parametros.pv_otvalor64','parametros.pv_otvalor65'
            ,'parametros.pv_otvalor66','parametros.pv_otvalor67','parametros.pv_otvalor68','parametros.pv_otvalor69','parametros.pv_otvalor70'
            ,'parametros.pv_otvalor71','parametros.pv_otvalor72','parametros.pv_otvalor73','parametros.pv_otvalor74','parametros.pv_otvalor75'
            ,'parametros.pv_otvalor76','parametros.pv_otvalor77','parametros.pv_otvalor78','parametros.pv_otvalor79','parametros.pv_otvalor80'
            ,'parametros.pv_otvalor81','parametros.pv_otvalor82','parametros.pv_otvalor83','parametros.pv_otvalor84','parametros.pv_otvalor85'
            ,'parametros.pv_otvalor86','parametros.pv_otvalor87','parametros.pv_otvalor88','parametros.pv_otvalor89','parametros.pv_otvalor90'
            ,'parametros.pv_otvalor91','parametros.pv_otvalor92','parametros.pv_otvalor93','parametros.pv_otvalor94','parametros.pv_otvalor95'
            ,'parametros.pv_otvalor96','parametros.pv_otvalor97','parametros.pv_otvalor98','parametros.pv_otvalor99'
            ,'cdplan'                 ,'cdtipsit'               ,'personalizado'          ,{name:'nmsituac',type:'int'}
        ]
    });
    
    Ext.define('_p30_modeloRecuperado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'NOMBRECLI'
            ,'DIRECCIONCLI'
        ]
    });
    
    Ext.define('_p30_modeloTarifa',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'CDPERPAG'
            ,'DSPERPAG'
            ,'PRIMA'
        ]
    });
    
    Ext.define('_p30_modeloDetalleCotizacion',
    {
        extend : 'Ext.data.Model'
        ,fields :
        [
            'COBERTURA'
            ,{
                name : 'PRIMA'
                ,type : 'float'
            }
            ,'TITULO'
            ,'ORDEN'
        ]
    });
    
    Ext.define('_p30_modeloCoberturaCotizacion',
    {
        extend : 'Ext.data.Model'
        ,fields :
        [
            'COBERTURA'
            ,'SUMASEG'
            ,'TITULO'
            ,'ORDEN'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p30_store = Ext.create('Ext.data.Store',
    {
        model : '_p30_modelo'
    });
    
    _p30_storeSubmarcasRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeSubmarcasRamo5'
        ,cargado  : false
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo' : 'RAMO_5_SUBMARCAS'
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,rec,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreSubmarcas').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreSubmarcas').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storeVersionesRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeVersionesRamo5'
        ,cargado  : false
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p30_urlCargarCatalogo
            ,timeout     : 1000*60*2
            ,extraParams :
            {
                'catalogo' : 'RAMO_5_VERSIONES'
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,rec,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreVersiones').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreVersiones').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storeUsosRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeUsosRamo5'
        ,cargado  : false
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo' : 'RAMO_5_TIPOS_USO'
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,rec,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreUsos').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreUsos').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storeMarcasRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeMarcasRamo5'
        ,cargado  : false
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo' : 'RAMO_5_MARCAS'
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,records,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreMarcas').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreMarcas').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storePlanesRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storePlanesRamo5'
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,cargado  : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo'         : 'PLANES_X_PRODUCTO'
                ,'params.cdramo'   : '5'
                ,'params.cdtipsit' : ''
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,rec,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStorePlanes').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStorePlanes').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storeCargasRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeCargasRamo5'
        ,autoLoad : _p30_smap1.cdramo+'x'=='5x'
        ,cargado  : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo'         : 'RAMO_5_TIPOS_CARGA'
                ,'params.cdramo'   : '5'
                ,'params.cdtipsit' : 'AR'
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(me,rec,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreCargas').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreCargas').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p30_storeCdtipsit = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p30_storeCdtipsit'
        ,cargado  : false
        ,autoLoad : true
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p30_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo'         : 'TIPSIT'
                ,'params.idPadre'  : _p30_smap1.cdramo
            }
            ,reader :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
        ,listeners :
        {
            load : function(store,records,success)
            {
                if(success)
                {
                    this.cargado=true;
                    _fieldById('_p30_grid').getView().refresh();
                    _fieldById('_p30_panelStoreCdtipsit').destroy();
                }
                else
                {
                    _fieldById('_p30_panelStoreCdtipsit').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    var _p30_panel1Items =
    [
        {
            xtype   : 'fieldset'
            ,itemId : 'fsPanel1'
            ,border : 0
            ,items  :
            [
                {
                    layout  :
                    {
                        type     : 'table'
                        ,columns : 2
                    }
                    ,border : 0
                    ,items  :
                    [
                        {
                            xtype        : 'numberfield'
                            ,fieldLabel  : 'FOLIO'
                            ,name        : 'nmpoliza'
                            ,style       : 'margin:5px;margin-left:15px;'
                            ,hidden      : _p30_endoso
                            ,listeners   :
                            {
                                change : _p30_nmpolizaChange
                            }
                        }
                        ,{
                            xtype    : 'button'
                            ,itemId  : '_p30_botonCargar'
                            ,text    : 'BUSCAR'
                            ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                            ,handler : function(){_p30_cargarClic();}
                            ,hidden  : _p30_endoso
                        }
                    ]
                }
            ]
        }
        ,{
            xtype   : 'fieldset'
            ,itemId : '_p30_panel3Fieldset'
            ,title  : '<span style="font:bold 14px Calibri;">CLIENTE</span>'
            ,items  : _p30_panel3ItemsConf
        }
    ];
    for(var i=0;i<_p30_panel1ItemsConf.length;i++)
    {
        _p30_panel1ItemsConf[i].style='margin:5px;margin-left:15px;';
        _p30_panel1Items[0].items.push(_p30_panel1ItemsConf[i]);
    }
    _p30_panel1Items[0].items.push(
    {
        xtype       : 'datefield'
        ,name       : 'feini'
        ,fieldLabel : 'INICIO DE VIGENCIA'
        ,value      : new Date()
        ,style      : 'margin:5px;margin-left:15px;'
        //,readOnly   : _p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol!='SUSCRIAUTO'
        ,hidden     : _p30_endoso
    }
    ,{
        xtype       : 'datefield'
        ,name       : 'fefin'
        ,fieldLabel : 'FIN DE VIGENCIA'
        ,value      : Ext.Date.add(new Date(),Ext.Date.YEAR,1)
        ,minValue   : Ext.Date.add(new Date(),Ext.Date.DAY,1)
        ,style      : 'margin:5px;margin-left:15px;'
        ,hidden     : _p30_endoso
    }
    ,{
        xtype       : 'datefield'
        ,name       : 'fechaEndoso'
        ,fieldLabel : 'Fecha de efecto'
        ,value      : new Date()
        ,style      : 'margin:5px;margin-left:15px;'
        ,allowBlank : !_p30_endoso
        ,hidden     : !_p30_endoso
    }
    ,{
        xtype   : 'fieldset'
        ,itemId : '_p30_fieldBusquedaPoliza'
        ,width  : 290
        ,title  : '<span style="font:bold 14px Calibri;">RENOVAR POR POLIZA</span>'
        ,items  : _p30_panel7Items
        ,hidden : !Ext.isEmpty(_p30_flujo) ? (_p30_flujo.cdflujomc != 240 && _p30_flujo.cdtipflu != 103) : true
      }
    );
    
    var itemsTatripol=
    [
        <s:if test='%{!"0".equals(getSmap1().get("tatripolItemsLength"))}' >
            <s:property value="imap.tatripolItems" />
        </s:if>
    ];
    
    _p30_inicializarTatripol(itemsTatripol);
    
    if(_p30_smap1.tatripolItemsLength-0>0)
    {
        _p30_panel1Items[0].items.push(
        {
            xtype   : 'fieldset'
            ,itemId : '_p30_fieldsetTatripol'
            ,title  : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES DE P&Oacute;LIZA</span>'
            ,width  : 290
            ,hidden : !RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)//(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|'))==-1
            ,items  : itemsTatripol
        });
    }
    
    var _p30_formOcultoItems = [];
    <s:if test='%{getImap().get("panel5Items")!=null}'>
        var items = [<s:property value="imap.panel5Items" />];
        for(var i=0;i<items.length;i++)
        {
            _p30_formOcultoItems.push(items[i]);
        }
    </s:if>
    <s:if test='%{getImap().get("panel6Items")!=null}'>
        var items = [<s:property value="imap.panel6Items" />];
        for(var i=0;i<items.length;i++)
        {
            _p30_formOcultoItems.push(items[i]);
        }
    </s:if>
    debug('_p30_formOcultoItems:',_p30_formOcultoItems);
    ////// componentes //////
    
    ////// contenido //////
    var itemspri=
    [
        Ext.create('Ext.panel.Panel',
        {
            itemId       : '_p30_panelFlujo'
            ,title       : 'ACCIONES'
            ,hidden      : Ext.isEmpty(_p30_flujo)
            ,buttonAlign : 'left'
            ,buttons     : []
            ,listeners   :
            {
                afterrender : function(me)
                {
                    if(!Ext.isEmpty(_p30_flujo))
                    {
                        _cargarBotonesEntidad(
                            _p30_flujo.cdtipflu
                            ,_p30_flujo.cdflujomc
                            ,_p30_flujo.tipoent
                            ,_p30_flujo.claveent
                            ,_p30_flujo.webid
                            ,me.itemId//callback
                            ,_p30_flujo.ntramite
                            ,_p30_flujo.status
                            ,_p30_flujo.cdunieco
                            ,_p30_flujo.cdramo
                            ,_p30_flujo.estado
                            ,_p30_flujo.nmpoliza
                            ,_p30_flujo.nmsituac
                            ,_p30_flujo.nmsuplem
                            ,null//callbackDespuesProceso
                        );
                    }
                }
            }
        })
        ,Ext.create('Ext.form.Panel',
        {
            itemId      : '_p30_form'
            ,title      : 'DATOS GENERALES'
            ,formOculto : Ext.create('Ext.form.Panel',{ items : _p30_formOcultoItems })
            ,defaults   : { style : 'margin:5px;' }
            ,layout     :
            {
                type     : 'table'
                ,columns : 2
                ,tdAttrs : {valign:'top'}
            }
            ,items    : _p30_panel1Items
        })
        ,Ext.create('Ext.grid.Panel',
        {
            itemId      : '_p30_grid'
            ,title      : 'INCISOS'
            ,tbar       : {
                layout: 'anchor',
                defaults: { margin:'2 2 0 0' },
                items: _p30_gridTbarItems
            }
            ,bbar       :
            [
                {
                    xtype    : 'button'
                    ,hidden  : _p30_smap1.tipoflot+'x'!='Fx' && _p30_smap1.tipoflot+'x'!='Px'
                    ,text    : 'Limpiar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                    ,hidden  : true
                    ,handler : function()
                    {
                        _p30_store.removeAll();
                    }
                }
                ,'->'
                ,{
                     xtype  : 'form'
                    ,hidden : _p30_smap1.tipoflot+'x'!='Fx' && _p30_smap1.tipoflot+'x'!='Px'
                    ,layout : 'hbox'
                    ,items  :
                    [
                        {
                            xtype       : 'checkbox'
                            ,boxLabel   : '<span style="color:white;">Tomar configuraci&oacute;n de carga masiva</span>'
                            ,name       : 'smap1.tomarMasiva'
                            ,inputValue : 'S'
                            ,style      : 'background:#223772;'
                            ,checked    : _p30_smap1.tipoflot+'x'!='Px'
                            ,hidden     : _p30_smap1.tipoflot+'x'=='Px'
                        }
                        ,{
                            xtype         : 'filefield'
                            ,buttonOnly   : true
                            ,style        : 'margin:0px;'
                            ,name         : 'excel'
                            ,style        : 'background:#223772;'
                            ,buttonConfig :
                            {
                                text   : 'Carga masiva...'
                                ,icon  : '${ctx}/resources/fam3icons/icons/book_next.png'
                                ,hidden: _p30_smap1.turistas == 'S' 
                            }
                            ,listeners :
                            {
                                change : function(me)
                                {   var descripcion ='';
                                    var msnIncInv='';
                                    var indexofPeriod = me.getValue().lastIndexOf("."),
                                    uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                    debug('uploadedExtension:',uploadedExtension);
                                    var valido=Ext.Array.contains(['xls','xlsx'], uploadedExtension);
                                    if(!valido)
                                    {
                                        mensajeWarning('Solo se permiten hojas de c&aacute;lculo');
                                        me.reset();
                                    }
                                    if(valido&&_p30_smap1.cdramo+'x'=='5x')
                                    {
                                        valido = !Ext.isEmpty(_fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue());
                                        if(!valido)
                                        {
                                            mensajeWarning('Seleccione el negocio');
                                            me.reset();
                                        }
                                    }
                                    if(valido&&_p30_smap1.cdramo+'x'=='5x')
                                    {
                                        valido = _fieldLikeLabel('CIRCULACI',_fieldById('_p30_panel3Fieldset')).isValid();
                                        if(!valido)
                                        {
                                            mensajeWarning('Seleccione el c&oacute;digo postal');
                                            me.reset();
                                        }
                                    }
                                    
                                    if(valido)
                                    {
                                               Ext.Ajax.request(
                                                 {
                                                     url      : _p30_urlCargarTipoCambioWS
                                                     ,success : function(response)
                                                     {
                                                         serieCmp.setLoading(false);
                                                         var json=Ext.decode(response.responseText);
                                                         debug('### dolar:',json);
                                                         _p30_precioDolarDia=json.smap1.dolar;
                                                    }
                                                     ,failure : function()
                                                     {
                                                         errorComunicacion();
                                                     }
                                                 });
                                        
                                        var panelpri = _fieldById('_p30_panelpri');
                                        var postalVal   = _fieldLikeLabel('CIRCULACI',_fieldById('_p30_form')).getValue();
                                        panelpri.setLoading(true);
                                        me.up('form').submit(
                                        {
                                            url     : _p30_urlCargaMasiva
                                            ,params :
                                            {
                                                'smap1.cdramo'    : _p30_smap1.cdramo
                                                ,'smap1.cdtipsit' : _p30_smap1.cdtipsit
                                                ,'smap1.tipoflot' : _p30_smap1.tipoflot
                                                ,'smap1.codpos'   : postalVal
                                                ,'smap1.cambio'   : _p30_precioDolarDia
                                                ,'smap1.negocio'  : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                                            }
                                            ,success : function(form,action)
                                            {
                                                panelpri.setLoading(false);
                                                var json = Ext.decode(action.response.responseText);
                                                debug('### excel:',json);
                                                if(json.exito)
                                                {
                                                    var mrecords = [];
                                                    var msnIncInv = json.respuestaOculta;
                                                    
//                                                     if(Ext.isEmpty(json.slist1))
//                                                     {
//                                                     	mensajeWarning(msnIncInv);
//                                                     	panelpri.setLoading(false);
//                                                     }
//                                                     else
//                                                     {
                                                    
	                                                    for(var i in json.slist1)
	                                                    {
	                                                        var record=new _p30_modelo(json.slist1[i]);
	                                                        mrecords.push(record);
	                                                        debug('record.data:',record.data);
	                                                    }
	                                                    
	                                                    _p30_store.removeAll();
	                                                    _p30_numerarIncisos(mrecords);
	                                                    
	                                                    if(_p30_smap1.cdramo+'x'=='5x')
	                                                    {
	                                                        //recuperar
	                                                        var len = json.slist1.length;
	                                                        panelpri.setLoading(true);
	                                                        var errores    = [];
	                                                        var procesados = 0;
	                                                        var recupera = function(i)
	                                                        {
	                                                            var record       = mrecords[i];
	                                                            var cdtipsit     = record.get('cdtipsit');
	                                                            debug('recuperar:',record.data);
	                                                            if(cdtipsit+'x'=='ARx'
	                                                                ||cdtipsit+'x'=='CRx'
	                                                                ||cdtipsit+'x'=='PCx'
	                                                                ||cdtipsit+'x'=='PPx'
	                                                            )
	                                                            {
	                                                                var form         = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
	                                                                var claveCmp     = form.down('[fieldLabel*=CLAVE]');
	                                                                var claveName    = claveCmp.name;
	                                                                var marcaName    = form.down('[fieldLabel=MARCA]').name;
	                                                                var submarcaName = form.down('[fieldLabel=SUBMARCA]').name;
	                                                                var modeloName   = form.down('[fieldLabel=MODELO]').name;
	                                                                var versionName  = form.down('[fieldLabel*=VERSI]').name;
	                                                                
	                                                                var llaveAuto = '@'+record.get(claveName)+','+record.get(modeloName);
	                                                                debug('llaveAuto a recuperar:',llaveAuto);
	                                                                
	                                                                if(Ext.isEmpty(_p30_bufferAutos[llaveAuto]))
	                                                                {
	                                                                    debug('llaveAuto en base de datos');
	                                                                    var params = claveCmp.getStore().proxy.extraParams;
	                                                                    params['params.cadena']       = llaveAuto;
	                                                                    params['params.cdtipsit']     = cdtipsit;
	                                                                    params['params.indice']       = i-0;
	                                                                    params['params.claveName']    = claveName;
	                                                                    params['params.marcaName']    = marcaName;
	                                                                    params['params.submarcaName'] = submarcaName;
	                                                                    params['params.modeloName']   = modeloName;
	                                                                    params['params.versionName']  = versionName;
	                                                                    Ext.Ajax.request(
	                                                                    {
	                                                                        url      : claveCmp.getStore().proxy.url
	                                                                        ,params  : params
	                                                                        ,success : function(response)
	                                                                        {
	                                                                            var json = Ext.decode(response.responseText);
	                                                                            var indice = json.params.indice-0;
	                                                                            debug('### recuperar:',indice,json);
	                                                                            var record = mrecords[indice];
	                                                                            if(json.success==true&&json.lista.length>0)
	                                                                            {
	                                                                                var indiceRecuperado = -1;
	                                                                                for(var j in json.lista)
	                                                                                {
	                                                                                    var jsplit = json.lista[j].value.split(' - ');
	                                                                                    if(jsplit[0]==record.get(json.params.claveName)
	                                                                                        &&jsplit[3]==record.get(json.params.modeloName)
	                                                                                    )
	                                                                                    {
	                                                                                        indiceRecuperado = j-0;
	                                                                                        break;
	                                                                                    }
	                                                                                }
	                                                                                if(indiceRecuperado!=-1)
	                                                                                {
	                                                                                    var encontrado = new Generic(json.lista[indiceRecuperado]);
	                                                                                    var splited    = encontrado.get('value').split(' - ');
	
	                                                                                    var marcaTemp = _p30_storeMarcasRamo5.find('value' , splited[1],0,false,false,true);
	                                                                                    var submarcaTemp = _p30_storeSubmarcasRamo5.find('aux'   , splited[1]+'@'+splited[2],0,false,false,true);
	                                                                                    var versionTemp = _p30_storeVersionesRamo5.find('value' , splited[4],0,false,false,true);
	                                                                                    if(marcaTemp != -1 && submarcaTemp != 1 && versionTemp != -1)
	                                                                                    {
			                                                                                  var marca     = _p30_storeMarcasRamo5.getAt(marcaTemp).get('key');
			                                                                                  var submarca  = _p30_storeSubmarcasRamo5.getAt(submarcaTemp).get('key');
			                                                                                  var version   = _p30_storeVersionesRamo5.getAt(versionTemp).get('key');
			                                                                                  record.set(json.params.marcaName    , marca);
			                                                                                  record.set(json.params.submarcaName , submarca);
			                                                                                  record.set(json.params.versionName  , version);
			                                                                                  
			                                                                                  if('x'+record.get('cdtipsit')=='xCR')
			                                                                                  {
			                                                                                      var tipoVehiName = _p30_tatrisitFullForms['CR'].down('[fieldLabel*=TIPO DE VEH]').name;
			                                                                                      record.set(tipoVehiName,json.lista[j].aux);
			                                                                                      //alert(record.get(tipoVehiName));
			                                                                                  }
			                                                                                  _p30_bufferAutos[llaveAuto]=encontrado;
	                                                                                    }
	                                                                                    else
	                                                                                    {
	                                                                                    	var inc = (Number(json.params.indice)+1)+'';
	                                                                                    	if (marcaTemp == -1)         {var texto ='Favor de Introducir manualmente la descripcion de marca del inciso '+(inc.toString())+"<br/>";}
	                                                                                    	else if (submarcaTemp == -1) {var texto ='Favor de Introducir manualmente la submarca del inciso '+(inc.toString())+"<br/>";}
	                                                                                    	else if (versionTemp == -1)  {var texto ='Favor de Introducir manualmente la version del inciso '+(inc.toString())+"<br/>";}
	                                                                                    	descripcion = texto + descripcion; debug('### texto descriptivo:',descripcion);
	                                                                                    }
	                                                                                }
	                                                                                else
	                                                                                {
	                                                                                    errores.push('No se pudo encontrar el auto '+record.get(json.params.claveName)+' en modelo '+record.get(json.params.modeloName)+' (inciso '+record.get('nmsituac')+')');
	                                                                                }
	                                                                            }
	                                                                            else
	                                                                            {
	                                                                                errores.push('No se pudo recuperar el auto '+record.get(json.params.claveName)+' en modelo '+record.get(json.params.modeloName)+' (inciso '+record.get('nmsituac')+')');
	                                                                            }
	                                                                            
	                                                                            procesados=procesados+1;
	                                                                            if(procesados==len)
	                                                                            {
	                                                                                _p30_store.add(mrecords);
	                                                                                var errorLargo = '';
	                                                                                if(errores.length>0 && (!Ext.isEmpty(descripcion)||!Ext.isEmpty(msnIncInv)))
	                                                                                {
	                                                                                	 errorLargo = errores.join('<BR/>');
	                                                                                	 if(!Ext.isEmpty(descripcion)||!Ext.isEmpty(msnIncInv))
	                                                                                     {   
	                                                                                		 errorLargo = descripcion+'<BR/>'+errores.join('<BR/>');
	                                                                                         if(msnIncInv != null)
	                                                                                         {
	                                                                                        	 errorLargo = descripcion +'<BR/>'+ msnIncInv+'<BR/>'+errores.join('<BR/>');
	                                                                                         }
	                                                                                     }
	                                                                                     if(msnIncInv != null && descripcion.length<5)
	                                                                                     {
	                                                                                    	 errorLargo = msnIncInv+'<BR/>'+errores.join('<BR/>');
	                                                                                     }
	                                                                                }
	                                                                                if(errores.length<=0 && (!Ext.isEmpty(descripcion)||!Ext.isEmpty(msnIncInv)))
                                                                                    {
                                                                                         if(!Ext.isEmpty(descripcion)||!Ext.isEmpty(msnIncInv))
                                                                                         {   
                                                                                        	 errorLargo = descripcion;
                                                                                             if(msnIncInv != null)
                                                                                             {
                                                                                            	 errorLargo = descripcion +'<BR/>'+ msnIncInv;
                                                                                             }
                                                                                         }
                                                                                         if(msnIncInv != null && descripcion.length<5)
                                                                                         {
                                                                                        	 errorLargo = msnIncInv;
                                                                                         }
                                                                                    }
	                                                                                if(!Ext.isEmpty(errorLargo))
                                                                                    {
                                                                                        mensajeWarning(errorLargo);
                                                                                    }
                                                                                    panelpri.setLoading(false);
	                                                                            }
	                                                                        }
	                                                                        ,failure : function()
	                                                                        {
	                                                                            errores.push('Error de comunicaci&oacute;n');
	                                                                            procesados=procesados+1;
	                                                                            if(procesados==len)
	                                                                            {
	                                                                                panelpri.setLoading(false);
	                                                                                _p30_store.add(mrecords);
	                                                                                if(errores.length>0)
	                                                                                {
	                                                                                    mensajeWarning(errores.join('<BR/>'));
	                                                                                }
	                                                                            }
	                                                                        }
	                                                                    });
		                                                             }
		                                                             else
		                                                             {
		                                                                 debug('llaveAuto en buffer:',_p30_bufferAutos[llaveAuto]);
		                                                                 var encontrado = _p30_bufferAutos[llaveAuto];
		                                                                 var splited    = encontrado.get('value').split(' - ');
		                                                                 debug('splited:',splited);
		                                                                 var marca      = _p30_storeMarcasRamo5   .getAt(_p30_storeMarcasRamo5   .find('value',splited[1],0,false,false,true)).get('key');
		                                                                 var submarca   = _p30_storeSubmarcasRamo5.getAt(_p30_storeSubmarcasRamo5.find('aux'  ,splited[1]+'@'+splited[2],0,false,false,true)).get('key');
		                                                                 var version    = _p30_storeVersionesRamo5.getAt(_p30_storeVersionesRamo5.find('value',splited[4],0,false,false,true)).get('key');
		                                                                 record.set(marcaName    , marca);
		                                                                 record.set(submarcaName , submarca);
		                                                                 record.set(versionName  , version);
		                                                                 procesados=procesados+1;
			                                                             if(procesados==len)
			                                                             {
			                                                                 panelpri.setLoading(false);
			                                                                 _p30_store.add(mrecords);
			                                                                 if(errores.length>0)
			                                                                 {
			                                                                     mensajeWarning(errores.join('<BR/>'));
			                                                                 }
			                                                             }
		                                                             }
	                                                            }
	                                                            else
	                                                            {
	                                                                procesados=procesados+1;
		                                                            if(procesados==len)
		                                                            {
		                                                                panelpri.setLoading(false);
		                                                                _p30_store.add(mrecords);
		                                                                if(errores.length>0)
		                                                                {
		                                                                    mensajeWarning(errores.join('<BR/>'));
		                                                                }
		                                                            }
	                                                            }
	                                                        };
	                                                        for(var i=0;i<len;i++)
	                                                        {
	                                                            recupera(i);
	                                                        }
	                                                    }
	                                                    else
	                                                    {
	                                                        _p30_store.add(mrecords);
	                                                    }
//                                                     }
                                                }
                                                else
                                                {
                                                    mensajeError(json.respuesta);
                                                }
                                            }
                                            ,failure : function()
                                            {
                                                panelpri.setLoading(false);
                                                errorComunicacion();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
            ,columns    : _p30_gridCols
            ,height     : 330
            ,store      : _p30_store
            ,selModel   :
            {
                selType        : 'checkboxmodel'
                ,allowDeselect : true
                ,mode          : 'SINGLE'
                ,listeners     :
                {
                    selectionchange : function(me,selected,eOpts)
                    {
                        if(selected.length==1)
                        {
                            _p30_selectedRecord=selected[0];
                            _p30_editarAuto();
                        }
                        else
                        {
                            for(var i in _p30_situaciones)
                            {
                                _fieldById('_p30_tatrisitParcialForm'+_p30_situaciones[i]).hide();
                            }
                        }
                    }
                }
            }
        })
    ];
        
    for(var i in _p30_tatrisitParcialForms)
    {
        itemspri.push(_p30_tatrisitParcialForms[i]);
    }
        
    itemspri.push(
        Ext.create('Ext.panel.Panel',
        {
            itemId       : '_p30_botonera'
            ,buttonAlign : 'center'
            ,border      : 0
            ,buttons     :
            [
                {
                    itemId   : '_p30_cotizarButton'
                    ,text    : 'Cotizar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/calculator.png'
                    ,handler : function(){_p30_cotizar();}
                    ,hidden  : _p30_endoso
                }
                ,{
                    itemId   : '_p30_limpiarButton'
                    ,text    : 'Limpiar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                    ,handler : function(){_p30_limpiar();}
                    ,hidden  : _p30_endoso
                }
                ,{
                    itemId   : '_p30_endosoButton'
                    ,text    : 'Emitir'
                    ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                    ,handler : function(){_p30_confirmarEndoso();}
                    ,hidden  : !_p30_endoso
                }
            ]
        })
    );
    
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p30_divpri'
        ,itemId   : '_p30_panelpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    : itemspri
    });
    
    _p30_ventanaCdtipsit = Ext.create('Ext.window.Window',
    {
        title        : _p30_smap1.turistas!='S'?'ELEGIR TIPO DE VEH&Iacute;CULO':'ELEGIR TIPO DE LICENCIA'
        ,modal       : true
        ,closeAction : 'hide'
        ,items       : _p30_editorCdtipsit
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Aceptar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function(bot)
                {
                	if(!Ext.isEmpty(_p30_editorCdtipsit.getValue()))
                    {
                        _p30_selectedRecord.set('cdtipsit',_p30_editorCdtipsit.getValue());
                        bot.up('window').hide();
                        _p30_editarAuto();
                    }
                    else
                    {
                        mensajeWarning('Seleccione un tipo de veh&iacute;culo');
                    }
                }
            }
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    
    try{
    	if(_p30_smap1.turistas=='S'){
	    	_fieldByName("parametros.pv_otvalor17",_fieldById('_p30_panel3Fieldset',null,true),true).allowBlank=true;
	    	_fieldByName("parametros.pv_otvalor17",_fieldById('_p30_panel3Fieldset',null,true),true).isValid();
	    	_fieldByLabel("MONEDA").setValue("2");
	    	_fieldByLabel("MONEDA").getStore().filter([{filterFn: function(item) {
	            
	            return item.get("key") == '2'; }}])
	    	_fieldByName('aux.otvalor18',_fieldById("_p30_fieldsetTatripol"),true).setMargin('0 0 0 15');
	    	_fieldById("fsPanel1").insert(Ext.ComponentQuery.query("#fsPanel1>[xtype]").length-2,_fieldByName('aux.otvalor18',_fieldById("_p30_fieldsetTatripol"),true));
	    	
	    	_fieldByLabel('AGENTE',_fieldById("fsPanel1")).on({
	    		select:function(me){
	    			
	    			var extraparams={
	   	    			 catalogo:'RAMO_5_NEGOCIO_X_CDTIPSIT_AGENTE'
	   	    			,'params.cdtipsit':'TL'
	   	    			,'params.producto':'I'
	   	    			,'params.cdsisrol': _GLOBAL_CDSISROL
	   	    			,'params.cdagente':me.getValue() 
	   	    	}
	   	    	_fieldByLabel('NEGOCIO',_fieldById("fsPanel1")).store.proxy.extraParams=extraparams;
	   	    	_fieldByLabel('NEGOCIO',_fieldById("fsPanel1")).store.load();
	    		}
	    	})
	    	
	    	
    	}
    }catch(e){
    	debugError(e)
    }
    
    //fechas
    _fieldByName('feini').on(
    {
        change : function(me,val)
        {
            debug('val:',val);
            var fefin = _fieldByName('fefin');
            fefin.setValue(Ext.Date.add(val,Ext.Date.YEAR,1));
            fefin.setMinValue(Ext.Date.add(val,Ext.Date.DAY,1));
            fefin.isValid();
        }
    });
    //fechas
    
    //ramo 5
    if(_p30_smap1.cdramo+'x'=='5x')
    {
    	
    	//RPF
    	_fieldByName("aux.otvalor17",null,true).hide();
    	
        //fechas
        _fieldByName('feini').on(
        {
            change : function(){_p30_calculaVigencia();}
        });
    
        _fieldByName('fefin').on(
        {
            change : function(){_p30_calculaVigencia();}
        });
        
        _p30_calculaVigencia();
        
        //agente
        if(_p30_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            _fieldByLabel('AGENTE',_fieldById('_p30_form')).setValue(_p30_smap1.cdagente);
            _fieldByLabel('AGENTE',_fieldById('_p30_form')).setReadOnly(true);
            _p30_ramo5AgenteSelect(_fieldByLabel('AGENTE',_fieldById('_p30_form')),_p30_smap1.cdagente);
        }
        else if('PROMOTORAUTO'.lastIndexOf(_p30_smap1.cdsisrol)!=-1 && RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol))
        {
            _fieldByLabel('AGENTE',_fieldById('_p30_form')).on(
            {
                'select' : _p30_ramo5AgenteSelect
            });
        }
        //agente
        
        //negocio
        if(_p30_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getStore().load(
            {
                params :
                {
                    'params.cdagente' : _p30_smap1.cdagente
                }
            });
        }
        else
        {
            debug('@CUSTOM negocio:',_fieldByLabel('NEGOCIO',_fieldById('_p30_form')),'.');
            _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).anidado = true;
            _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).heredar = function(remoto,callback)
            {
                _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getStore().load(
                {
                    params :
                    {
                        'params.cdagente' : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
                    }
                    ,callback : function()
                    {
                        if(!Ext.isEmpty(callback))
                        {
                            callback(_fieldByLabel('NEGOCIO',_fieldById('_p30_form')));
                        }
                    }
                });
            }
            _fieldByLabel('AGENTE',_fieldById('_p30_form')).on(
            {
                select : function(){ _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).heredar(); }
            });
        }
        //negocio
        
        //grid
        debug('@CUSTOM grid:_p30_grid removeall on negocio selecet');
        _fieldById('_p30_grid').heredar = function()
        {
            _fieldById('_p30_grid').getStore().removeAll();
        }
        
        _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).on(
        {
            select : function(){ _fieldById('_p30_grid').heredar(); }
        });
        //grid
        
        //paneles tconvalsit
        debug('@CUSTOM tconvalsit:tconvalsit load on negocio select');
        _fieldById('_p30_grid').heredarPanelesTconvalsit = function()
        {
            for(var cdtipsit in _p30_paneles)
            {
                Ext.Ajax.request(
                {
                    url     : _p30_urlRecuperacionSimpleLista
                    ,params :
                    {
                        'smap1.procedimiento' : 'RECUPERAR_CONFIGURACION_VALOSIT_FLOTILLAS'
                        ,'smap1.cdramo'       : _p30_smap1.cdramo
                        ,'smap1.cdtipsit'     : cdtipsit
                        ,'smap1.negocio'      : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### config:',json);
                        if(json.exito)
                        {
                            _p30_paneles[json.smap1.cdtipsit].valores    = {};
                            _p30_paneles[json.smap1.cdtipsit].valoresBkp = {};
                            for(var i in json.slist1)
                            {   
                                _p30_paneles[json.smap1.cdtipsit].valores['parametros.pv_otvalor'+(('00'+json.slist1[i].CDATRIBU).slice(-2))]    = json.slist1[i].VALOR;
                                _p30_paneles[json.smap1.cdtipsit].valoresBkp['parametros.pv_otvalor'+(('00'+json.slist1[i].CDATRIBU).slice(-2))] = json.slist1[i].VALOR;
                            }
                            debug('valores:',_p30_paneles[json.smap1.cdtipsit].valores);
                            
                            if(json.slist2.length>0)
                            {
                                for(var i in json.slist2)
                                {
                                    var minimo = json.slist2[i].MINIMO;
                                    var maximo = json.slist2[i].MAXIMO;
                                    var item   = _p30_paneles[json.smap1.cdtipsit].down('[name=parametros.pv_otvalor'+(('x00'+json.slist2[i].CDATRIBU).slice(-2))+']');
                                    debug('cdtipsit:',json.smap1.cdtipsit,'item.fieldLabel:',item.fieldLabel);
                                    debug('min:',minimo,'max:',maximo);
                                    if(!Ext.isEmpty(minimo)&&!Ext.isEmpty(maximo))
		                            {
		                                item.minValue = minimo;
		                                item.maxValue = maximo;
		                                if(item.xtype=='combobox')
		                                {
		                                    item.validator=function(value)
		                                    {
		                                        var valido=true;
		                                        if(value+'x'!='x')
		                                        {
		                                            var record = this.getStore().findRecord('value',value);
		                                            if(record)
		                                            {
		                                                var value=record.get('key');
		                                                if(Number(value)<Number(this.minValue))
		                                                {
		                                                    valido = 'El valor m&iacute;nimo es '+this.minValue;
		                                                }
		                                                else if(Number(value)>Number(this.maxValue))
		                                                {
		                                                    valido = 'El valor m&aacute;ximo es '+this.maxValue;
		                                                }
		                                            }
		                                            else
		                                            {
		                                                valido = 'El valor debe estar en el rango '+this.minValue+' - '+this.maxValue;
		                                            }
		                                        }
		                                        return valido;
		                                    }
		                                    if(!item.isValid())
		                                    {
		                                        item.reset();
		                                    }
		                                    debug('item=',item.fieldLabel);
		                                    debug('minimo=',minimo,'maximo=',maximo);
		                                    item.store.filterBy(function(record)
		                                    {
		                                        debug('filtrando record=',record);
		                                        var key=record.get('key')-0;
		                                        debug('quitando key=',key,key>=minimo&&key<=maximo,'.');
		                                        return key>=minimo&&key<=maximo;
		                                    });
		                                    item.on(
		                                    {
		                                        expand : function(me)
		                                        {
		                                            var minimo = me.minValue;
		                                            var maximo = me.maxValue;
		                                            me.store.filterBy(function(record)
		                                            {
		                                                debug('filtrando record=',record);
		                                                var key=record.get('key')-0;
		                                                debug('quitando key=',key,key>=minimo&&key<=maximo,'.');
		                                                return key>=minimo&&key<=maximo;
		                                            });
		                                        }
		                                    });
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
                    ,failure : errorComunicacion
                });
            }
        }
        
        _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).on(
        {
            select : function(){ _fieldById('_p30_grid').heredarPanelesTconvalsit(); }
        });
        //paneles tconvalsit
        
        //cliente nuevo
        _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_form')).on(
        {
            change : _p30_ramo5ClienteChange
        });
        _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_form')).select('S');
        
        //tipo situacion
        debug('@CUSTOM cdtipsit:',_p30_editorCdtipsit,'.');
        _p30_editorCdtipsit.anidado = true;
        _p30_editorCdtipsit.heredar = function()
        {
            _p30_editorCdtipsit.getStore().load(
            {
                params :
                {
                    'params.negocio' : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                }
            });
        };
        _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).on(
        {
            change : function(me,val)
            {
                if(me.findRecord('key',val)!=false)
                {
                    _p30_editorCdtipsit.heredar();
                }
            }
        });
        //tipo situacion
        
        //loaders panels
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreMarcas'
            ,floating : true
            ,html     : 'Cargando marcas...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,50);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreSubmarcas'
            ,floating : true
            ,html     : 'Cargando submarcas...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,90);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreUsos'
            ,floating : true
            ,html     : 'Cargando usos...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,130);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreVersiones'
            ,floating : true
            ,html     : 'Cargando versiones...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,170);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStorePlanes'
            ,floating : true
            ,html     : 'Cargando planes...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,210);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreCdtipsit'
            ,floating : true
            ,html     : 'Cargando situaciones...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,250);
        
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p30_panelStoreCargas'
            ,floating : true
            ,html     : 'Cargando tipos de carga...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,290);
        //loaders panels
        
        //herencia situaciones
        for(var i in _p30_situaciones)
        {
            var cdtipsit = _p30_situaciones[i];
            
            //uso
            if('|AR|CR|PC|PP|MO|TC|RQ|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var tipoUsoCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO USO]');
                debug('@CUSTOM tipouso:',tipoUsoCmp,'.');
                tipoUsoCmp.anidado = true;
                tipoUsoCmp.heredar = function(remoto,callback)
                {
                    var record      = _p30_selectedRecord;
                    var cdtipsit    = record.get('cdtipsit');
                    var me          = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO USO]');
                    var servicioVal = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO SERVICIO]').getValue();
                    var negocioVal  = _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue();
                    if(!Ext.isEmpty(negocioVal)&&!Ext.isEmpty(servicioVal))
                    {
                        me.getStore().load(
                        {
                            params :
                            {
                                'params.cdtipsit'   : cdtipsit
                                ,'params.cdnegocio' : negocioVal
                                ,'params.servicio'  : servicioVal
                            }
                            ,callback : function()
                            {
                                var val = me.getValue();
                                var den = false;
                                me.getStore().each(function(record)
                                {
                                    if(record.get('key')==val)
                                    {
                                        den = true;
                                    }
                                });
                                if(!den)
                                {
                                    me.clearValue();
                                }
                                var claveCmps = Ext.ComponentQuery.query('[fieldLabel*=CLAVE GS]',_fieldById('_p30_tatrisitAutoForm'+cdtipsit));
                                debug('claveCmps:',claveCmps);
                                for(var i in claveCmps)
                                {
                                    claveCmps[i].store.proxy.extraParams['params.uso'] = me.getValue();
                                    claveCmps[i].store.proxy.extraParams['params.servicio'] = servicioVal;
                                }
                                if(!Ext.isEmpty(callback))
                                {
                                    callback
                                    (
                                        _fieldById('_p30_tatrisitParcialForm'+_p30_selectedRecord.get('cdtipsit'))
                                            .down('[fieldLabel=TIPO USO]')
                                    );
                                }
                            } 
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
                
                _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO SERVICIO]').on(
                {
                    select : function(me)
                    {
                        var record      = _p30_selectedRecord;
                        var cdtipsit    = record.get('cdtipsit');
                        _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO USO]').heredar(true);
                        var claveCmps = Ext.ComponentQuery.query('[fieldLabel*=CLAVE GS]',_fieldById('_p30_tatrisitAutoForm'+cdtipsit));
                        debug('claveCmps:',claveCmps);
                        for(var i in claveCmps)
                        {
                            claveCmps[i].store.proxy.extraParams['params.servicio'] = me.getValue();
                        }
                    }
                });
                
                tipoUsoCmp.on(
                {
                    change : function(me,val,old)
                    {
                        var record     = _p30_selectedRecord;
                        var cdtipsit   = record.get('cdtipsit');
                        var claveCmp   = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=CLAVE GS]');
                        var modeloName = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').name;
                        if(!Ext.isEmpty(claveCmp))
                        {
                            claveCmp.store.proxy.extraParams['params.uso'] = val;
                            if(!Ext.isEmpty(record.get(claveCmp.name))
                                &&!Ext.isEmpty(record.get(modeloName))
                                &&!Ext.isEmpty(old)
                            )
                            {
                                record.set(claveCmp.name  , '');
                                record.set(modeloCmp.name , '');
                                mensajeWarning('Debe actualizar el veh&iacute;culo');
                            }
                        }
                    }
                });
            }
            //uso
            
            //plan
            if('|AR|CR|PC|PP|MO|TC|RQ|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var planCmp=_fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=PAQUETE]');
                debug('@CUSTOM plan:',planCmp,'.');
                planCmp.anidado = true;
                planCmp.heredar = function(remoto,callback)
                {
                    var record     = _p30_selectedRecord;
                    var cdtipsit   = record.get('cdtipsit');
                    var me         = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=PAQUETE]');
                    if(Ext.isEmpty(me))
                    {
                        me = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=PAQUETE (Seleccionar veh&iacute;culo primero)]');
                    }
                    var modeloName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=MODELO]').getName();
                    var claveName  = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*=CLAVE]').getName();
                    var marcaName  = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=MARCA]').getName();
                    debug('heredar:'    , me);
                    debug('modeloName:' , modeloName);
                    debug('claveName:'  , claveName);
                    debug('marcaName:'  , marcaName);
                    
                    var modeloVal   = record.get(modeloName);
                    var claveVal    = record.get(claveName);
                    var marcaVal    = record.get(marcaName);
                    var negocioVal  = _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue();
                    var servicioVal = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO SERVICIO]').getValue();
                    debug('modeloVal:'   , modeloVal);
                    debug('claveVal:'    , claveVal);
                    debug('marcaVal:'    , marcaVal);
                    debug('negocioVal:'  , negocioVal);
                    debug('servicioVal:' , servicioVal);
                    
                    if(
                        (
                            '|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1
                            &&
                            (
                                Ext.isEmpty(modeloVal)||Ext.isEmpty(marcaVal)||Ext.isEmpty(negocioVal)||Ext.isEmpty(servicioVal)
                            )
                        )
                        ||
                        (
                            '|MO|TC|RQ|'.lastIndexOf('|'+cdtipsit+'|')!=-1
                            &&
                            (
                                Ext.isEmpty(modeloVal)||Ext.isEmpty(negocioVal)||Ext.isEmpty(servicioVal)
                            )
                        )
                    )
                    {
                        me.allowBlank=true;
                        me.setFieldLabel('PAQUETE (Seleccionar veh&iacute;culo primero)');
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                    else
                    {
                        me.allowBlank=false;
                        me.setFieldLabel('PAQUETE');
                        me.getStore().load(
                        {
                            params :
                            {
                                'params.cdtipsit'  : cdtipsit
                                ,'params.negocio'  : negocioVal
                                ,'params.modelo'   : modeloVal
                                ,'params.clavegs'  : claveVal
                                ,'params.servicio' : servicioVal
                            }
                            ,callback : function()
                            {
                                if(!Ext.isEmpty(callback))
                                {
                                    var me = _fieldById('_p30_tatrisitParcialForm'+_p30_selectedRecord.get('cdtipsit'))
                                        .down('[fieldLabel=PAQUETE]');
                                    if(Ext.isEmpty(me))
                                    {
                                        me = _fieldById('_p30_tatrisitParcialForm'+_p30_selectedRecord.get('cdtipsit'))
                                            .down('[fieldLabel=PAQUETE (Seleccionar veh&iacute;culo primero)]');
                                    }
                                    callback(me);
                                }
                            } 
                        });
                    }
                }
                
                _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO SERVICIO]').on(
                {
                    select : function(me)
                    {
                        var record   = _p30_selectedRecord;
                        var cdtipsit = record.get('cdtipsit');
                        
                        var planCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=PAQUETE]');
                        if(Ext.isEmpty(planCmp))
                        {
                            planCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit)
                                .down('[fieldLabel=PAQUETE (Seleccionar veh&iacute;culo primero)]');
                        }
                        planCmp.heredar(true);
                    }
                });
            }
            //plan
            
            //marca
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var marcaCmp=_fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MARCA]');
                debug('@CUSTOM marca:',marcaCmp,'.');
                marcaCmp.anidado = true;
                marcaCmp.heredar = function(remoto,callback)
                {
                    var record     = _p30_selectedRecord;
                    var cdtipsit   = record.get('cdtipsit');
                    var me         = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MARCA]');
                    var negocioVal = _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue();
                    if(!Ext.isEmpty(negocioVal))
                    {
                        me.getStore().load(
                        {
                            params    :
                            {
                                'params.cdtipsit'   : cdtipsit
                                ,'params.cdnegocio' : negocioVal
                            }
                            ,callback : function()
                            {
                                if(!Ext.isEmpty(callback))
                                {
                                    callback
                                    (
                                        _fieldById('_p30_tatrisitAutoForm'+cdtipsit)
                                            .down('[fieldLabel=MARCA]')
                                    );
                                }
                            }
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
            }
            //marca
            
            //modelo
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var modeloCmp=_fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]');
                debug('@CUSTOM modelo:',modeloCmp,'.');
                modeloCmp.anidado = true;
                modeloCmp.heredar = function(remoto,callback)
                {
                    var record      = _p30_selectedRecord;
                    var cdtipsit    = record.get('cdtipsit');
                    var me          = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]');
                    var submarcaVal = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=SUBMARCA]').getValue();
                    if(!Ext.isEmpty(submarcaVal))
                    {
                        me.getStore().load(
                        {
                            params    :
                            {
                                'params.cdtipsit' : cdtipsit
                                ,'params.idPadre' : submarcaVal
                            }
                            ,callback : function()
                            {
                                if(!Ext.isEmpty(callback))
                                {
                                    callback
                                    (
                                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit'))
                                            .down('[fieldLabel=MODELO]')
                                    );
                                }
                            }
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
                
                modeloCmp.on(
                {
                    select : function(me)
                    {
                        var record   = _p30_selectedRecord;
                        var cdtipsit = record.get('cdtipsit');
                        
                        var tipovalorName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=TIPO VALOR]').name;
                        var tipovalorVal  = record.get(tipovalorName)-0;
                        var anioAc        = new Date().getFullYear()-0;
                        var modeloVal     = me.getValue()-0;
                        
                        debug('tipovalorVal:' , tipovalorVal);
                        debug('modeloVal:'    , modeloVal);
                        
                        if(tipovalorVal==3&&anioAc-modeloVal>1)
                        {
                            mensajeWarning('Para valor de factura solo se permiten modelos del a&ntilde;o actual o anterior');
                            me.setValue('');
                        }
                    }
                });
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=SUBMARCA]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel=MODELO]').heredar(true);
                    }
                });
            }
            else if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                Ext.Ajax.request(
                {
                    url : _p30_urlCargarParametros
                    ,params  :
                    {
                        'smap1.parametro' : 'RANGO_ANIO_MODELO'
                        ,'smap1.cdramo'   : _p30_smap1.cdramo
                        ,'smap1.cdtipsit' : cdtipsit
                        ,'smap1.clave4'   : _p30_smap1.cdsisrol
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### obtener rango años response:',json);
                        if(json.exito)
                        {
                            var modeloCmp = _fieldById('_p30_tatrisitAutoForm'+json.smap1.cdtipsit).down('[fieldLabel=MODELO]');
                            modeloCmp.limiteInferior = json.smap1.P1VALOR-0;
                            modeloCmp.limiteSuperior = json.smap1.P2VALOR-0;
                            
                            modeloCmp.validator=function(value)
                            {
                                var record   = _p30_selectedRecord;
                                var cdtipsit = record.get('cdtipsit');
                                var me       = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]');
                                
                                var r = true;
                                var anioActual = new Date().getFullYear()-0;
                                var max = anioActual+me.limiteSuperior;
                                var min = anioActual+me.limiteInferior;
                                debug('anioActual:',anioActual);
                                debug('max:',max);
                                debug('min:',min);
                                debug('value:',value);
                                if(value<min||value>max)
                                {
                                    r='El modelo debe estar en el rango '+min+'-'+max;
                                }
                                return r;
                           };
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
            //modelo
            
            //version
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var versionCmp=_fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VERSI]');
                debug('@CUSTOM version:',versionCmp,'.');
                versionCmp.anidado = true;
                versionCmp.heredar = function(remoto,callback)
                {
                    var record      = _p30_selectedRecord;
                    var cdtipsit    = record.get('cdtipsit');
                    var me          = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VERSI]');
                    var submarcaVal = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=SUBMARCA]').getValue();
                    var modeloVal   = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').getValue();
                    if(!Ext.isEmpty(submarcaVal)
                        &&!Ext.isEmpty(modeloVal))
                    {
                        me.getStore().load(
                        {
                            params    :
                            {
                                'params.cdtipsit'  : cdtipsit
                                ,'params.submarca' : submarcaVal
                                ,'params.modelo'   : modeloVal
                            }
                            ,callback : function()
                            {
                                if(!Ext.isEmpty(callback))
                                {
                                    callback
                                    (
                                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit'))
                                            .down('[fieldLabel*=VERSI]')
                                    );
                                }
                            }
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=SUBMARCA]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=VERSI]').heredar(true);
                    }
                });
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=VERSI]').heredar(true);
                    }
                });
            }
            //version
            
            //clave
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var claveCmp=_fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=CLAVE]');
                debug('@CUSTOM clave:',claveCmp,'.');
                
                claveCmp.getStore().getProxy().extraParams['params.cdtipsit']=cdtipsit;
                
                claveCmp.anidado = true;
                claveCmp.heredar = function(remoto,callback)
                {
                    var record     = _p30_selectedRecord;
                    var cdtipsit   = record.get('cdtipsit');
                    var me         = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=CLAVE]');
                    var modeloVal  = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').getValue();
                    var versionVal = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VERSI]').getValue();
                    if(!Ext.isEmpty(modeloVal)
                        &&!Ext.isEmpty(versionVal))
                    {
                        me.getStore().load(
                        {
                            params    :
                            {
                                'params.cadena'    : versionVal
                                ,'params.cdtipsit' : cdtipsit
                            }
                            ,callback : function()
                            {
                                var record      = _p30_selectedRecord;
                                var cdtipsit    = record.get('cdtipsit');
                                var form        = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                                var me          = form.down('[fieldLabel*=CLAVE]');
                                var marcaCmp    = form.down('[fieldLabel=MARCA]');
                                var submarcaCmp = form.down('[fieldLabel=SUBMARCA]');
                                var modeloCmp   = form.down('[fieldLabel=MODELO]');
                                var versionCmp  = form.down('[fieldLabel*=VERSI]');
                                
                                var marcaDisplay    = marcaCmp.findRecord('key'    , marcaCmp.getValue()).get('value');
                                var submarcaDisplay = submarcaCmp.findRecord('key' , submarcaCmp.getValue()).get('value');
                                var modeloDisplay   = modeloCmp.findRecord('key'   , modeloCmp.getValue()).get('value');
                                var versionDisplay  = versionCmp.findRecord('key'  , versionCmp.getValue()).get('value');
                                
                                var meDisplay = versionCmp.getValue()+' - '+marcaDisplay+' - '+submarcaDisplay+' - '
                                    +modeloDisplay+' - '+versionDisplay;
                                debug('meDisplay:',meDisplay);
                                me.setValue(me.findRecordByDisplay(meDisplay));
                                
                                if(!Ext.isEmpty(callback))
                                {
                                    callback(me);
                                }
                            }
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=CLAVE]').heredar(true);
                    }
                });
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VERSI]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=CLAVE]').heredar(true);
                    }
                });
            }
            //clave
            
            //valor
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var valorCmp = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VALOR VEH]');
               
                Ext.apply(valorCmp,
                        {
                         useThousandSeparator: true
                        });
                
                debug('@CUSTOM valor:',valorCmp);
                valorCmp.heredar = function(conservar)
                {
                    debug('>recuperar conservar:',conservar,'.');
                    var record     = _p30_selectedRecord;
                    var cdtipsit   = record.get('cdtipsit');
                    var form       = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                    var me         = form.down('[fieldLabel*=VALOR VEH]');
                    var versionVal = form.down('[fieldLabel*=VERSI]').getValue();
                    var modeloVal  = form.down('[fieldLabel=MODELO]').getValue();
                    
                    var tipovalorName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=TIPO VALOR]').name;
                    var tipovalorVal  = record.get(tipovalorName);
                    
                    debug('versionVal:'    , versionVal);
                    debug('modeloVal:'     , modeloVal);
                    debug('tipovalorName:' , tipovalorName);
                    debug('tipovalorVal:'  , tipovalorVal);
                    
                    if(!Ext.isEmpty(versionVal)&&!Ext.isEmpty(modeloVal)&&!Ext.isEmpty(tipovalorVal))
                    {
                        me.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url      : _p30_urlCargarSumaAseguradaRamo5
                            ,params  :
                            {
                                 'smap1.cdtipsit' : cdtipsit
                                ,'smap1.clave'    : versionVal
                                ,'smap1.modelo'   : modeloVal
                                ,'smap1.cdsisrol' : _p30_smap1.cdsisrol
                            }
                            ,success : function(response)
                            {
                                me.setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### cargar suma asegurada:',json);
                                if(json.exito)
                                {
                                    if(!Ext.isEmpty(json.respuesta))
                                    {
                                        //comentado porque rafa no quiere avisos
                                        //mensajeWarning(json.respuesta);
                                    }
                                    if(!conservar||Ext.isEmpty(me.getValue()))
                                    {
                                        me.setValue(json.smap1.sumaseg);
                                    }
                                    me.valorCargado=json.smap1.sumaseg;
                                    
                                    me.setLoading(true);
                                    Ext.Ajax.request(
                                    {
                                        url      : _p30_urlCargarParametros
                                        ,params  :
                                        {
                                            'smap1.parametro' : 'RANGO_VALOR'
                                            ,'smap1.cdramo'   : _p30_smap1.cdramo
                                            ,'smap1.cdtipsit' : cdtipsit
                                            ,'smap1.clave4'   : tipovalorVal
                                            ,'smap1.clave5'   : _p30_smap1.cdsisrol
                                        }
                                        ,success : function(response)
                                        {
                                            me.setLoading(false);
                                            var json = Ext.decode(response.responseText);
                                            debug('### obtener rango valor:',json);
                                            if(json.exito)
                                            {
                                            	try {
                                            		debug('json.smap1.P1VALOR=', Number(json.smap1.P1VALOR));
                                            		debug('json.smap1.P2VALOR=', Number(json.smap1.P2VALOR));
                                            		// http://stackoverflow.com/questions/11832914/round-to-at-most-2-decimal-places-in-javascript
                                            		var valormin = Math.round((Number(me.valorCargado) * (1 + Number(json.smap1.P1VALOR))) * 100) / 100;
                                                    var valormax = Math.round((Number(me.valorCargado) * (1 + Number(json.smap1.P2VALOR))) * 100) / 100;
                                                    MontoMaximo = valormax;
													MontoMinimo = valormin;
													
                                                    
                                                    if(_p30_smap1.cdsisrol =='EJECUTIVOCUENTA'){
                                                    	if((Number(valormin)>= Number(me.maximoTotal)) && (Number(valormax)>= Number(me.maximoTotal))){
                                                    		MontoMaximo = me.maximoTotal;
    														MontoMinimo = me.maximoTotal;
                                                    		me.setMinValue(me.maximoTotal);
                                                   			me.setMaxValue(me.maximoTotal);
                                   	                    }else{
                                   	                    	me.setMaxValue(valormax);
                                   	                    	me.setMinValue(valormin);
                                   	                    }
                                                	}else{
														me.setMinValue(valormin);
														me.setMaxValue(valormax);
                                                	}
                                                    me.isValid();
                                                    debug('valor:',me.valorCargado);
                                                    debug('valormin:',valormin);
                                                    debug('valormax:',valormax);
                                            	} catch(e) {
                                            		debug('No se asignaron valores minimo ni maximo', e);
                                            	}
                                            }
                                            else
                                            {
                                                mensajeError(json.respuesta);
                                            }
                                        }
                                        ,failure : function()
                                        {
                                            me.setLoading(false);
                                            errorComunicacion();
                                        }
                                    });
                                }
                                else
                                {
                                    mensajeError(json.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                me.setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                };
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=MODELO]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=VALOR VEH]').heredar(false);
                    }
                });
                
                _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VERSI]').on(
                {
                    select : function()
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).down('[fieldLabel*=VALOR VEH]').heredar(false);
                    }
                });
                
                //maximo total
                Ext.Ajax.request(
                {
                    url      : _p30_urlRecuperacionSimple
                    ,params  :
                    {
                        'smap1.procedimiento' : 'RECUPERAR_VALOR_MAXIMO_SITUACION_POR_ROL'
                        ,'smap1.cdtipsit'     : cdtipsit
                    }
                    ,success : function(response)
                    {
                        var jsonValmax = Ext.decode(response.responseText);
                        debug('### valor maximo total por rol:',jsonValmax);
                        if(jsonValmax.exito)
                        {
                            var valorValmaxCmp         = _fieldById('_p30_tatrisitAutoForm'+jsonValmax.smap1.cdtipsit).down('[fieldLabel*=VALOR VEH]');
                            valorValmaxCmp.maximoTotal = jsonValmax.smap1.VALOR;
                            valorValmaxCmp.validator   = function(val)
                            {
                                var me = this;
                                if(Number(val)>Number(me.maximoTotal))
                                {
                                	if(_p30_smap1.cdsisrol =='EJECUTIVOCUENTA'){
                                		me.setMinValue(false);
                            			me.setMaxValue(false);
                                		return 'Favor de acudir a Mesa de Control para realizar la cotización.';
                                	}else{
                                		return 'El valor m&aacute;ximo es '+me.maximoTotal;
                                	}
                                }else{
                                	if(_p30_smap1.cdsisrol =='EJECUTIVOCUENTA'){
                                		me.setMinValue(MontoMinimo);
                                		me.setMaxValue(MontoMaximo);
                                	}
                                }
                                return true;
                            };
                        }
                        else
                        {
                            mensajeWarning(jsonValmax.respuesta);
                        }
                    }
                    ,failure : function()
                    {
                        errorComunicacion();
                    }
                });
                //maximo total
            }
            else if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var sumaCmp = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VALOR VEH]');
                debug('@CUSTOM suma asegurada front.:',sumaCmp);
                sumaCmp.anidado = true;
                sumaCmp.heredar = function()
                {
                    var record        = _p30_selectedRecord;
                    var cdtipsit      = record.get('cdtipsit');
                    var me            = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=VALOR VEH]');
                    var tipovalorName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=TIPO VALOR]').name;
                    var tipovalorVal  = record.get(tipovalorName)-0;
                    
                    debug('tipovalorName:' , tipovalorName,record.data);
                    debug('tipovalorVal:'  , tipovalorVal);
                    
                    me.setReadOnly(tipovalorVal==2);
                }
                
                //trigger tipo valor
                var tipovalorCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO VALOR]');
                debug('@CUSTOM valor a cotizar front. trigger suma aseg.:',tipovalorCmp);
                tipovalorCmp.on(
                {
                    select : function(me)
                    {
                        var record   = _p30_selectedRecord;
                        var cdtipsit = record.get('cdtipsit');
                        var sumaName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*=VALOR VEH]').name;
                        var sumaVal  = record.get(sumaName);
                        
                        debug('sumaName:',sumaName,record.data); 
                        debug('sumaVal:',sumaVal);
                        
                        if(!Ext.isEmpty(sumaVal))
                        {
                            record.set(sumaName,'');
                            mensajeWarning('Debe actualizar la suma asegurada del veh&iacute;culo');
                        }
                    }
                });
                //trigger tipo valor
                
                //trigger tipo vehiculo
                var tipovehCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel*=TIPO VEH]');
                debug('@CUSTOM tipo veh. front. trigger suma aseg.:',tipovehCmp);
                tipovehCmp.on(
                {
                    select : function(me)
                    {
                        var record   = _p30_selectedRecord;
                        var cdtipsit = record.get('cdtipsit');
                        var sumaName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*=VALOR VEH]').name;
                        var sumaVal  = record.get(sumaName);
                        
                        debug('sumaName:',sumaName,record.data); 
                        debug('sumaVal:',sumaVal);
                        
                        if(!Ext.isEmpty(sumaVal))
                        {
                            record.set(sumaName,'');
                            mensajeWarning('Debe actualizar la suma asegurada del veh&iacute;culo');
                        }
                    }
                });
                //trigger tipo vehiculo
                
                //trigger cd postal
                var postalCmp = _fieldLikeLabel('CIRCULACI',_fieldById('_p30_form'));
                if(Ext.isEmpty(postalCmp.triggerSumaAseg)||postalCmp.triggerSumaAseg!=true)
                {
                    debug('@CUSTOM cd postal trigger suma aseg.:',postalCmp);
                    postalCmp.triggerSumaAseg=true;
                    postalCmp.on(
                    {
                        change : function()
                        {
                            var sumas = [];
                            var sumaName = _p30_tatrisitFullForms['AF'].down('[fieldLabel*=VALOR VEH]').name;
                            debug('sumaName:',sumaName);
                            _p30_store.each(function(record)
                            {
                                var cdtipsit=record.get('cdtipsit');
                                if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
                                {
                                    if(!Ext.isEmpty(record.get(sumaName)))
                                    {
                                        record.set(sumaName,'');
                                        sumas.push(record.get('nmsituac'));
                                    }
                                }
                            });
                            if(sumas.length>0)
                            {
                                mensajeWarning('Debe actualizar la suma asegurada de los '
                                    +'veh&iacute;culos fronterizos en los siguientes incisos: '+sumas.join(','));
                            }
                        }
                    });
                }
                //trigger cd postal
            }
            //valor
            
            //seleccionar auto
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var form = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                debug('@CUSTOM form buscarAutoDesc:',form);
                
                form.buscarAutoDesc = function(recordClave)
                {
                    debug('>buscarAutoDesc recordClave.data:',recordClave.data);
                    
                    var record   = _p30_selectedRecord;
                    var cdtipsit = record.get('cdtipsit');
                    var form     = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                    
                    var claveCmp    = form.down('[fieldLabel*=CLAVE]');
                    var marcaCmp    = form.down('[fieldLabel=MARCA]');
                    var submarcaCmp = form.down('[fieldLabel=SUBMARCA]');
                    var modeloCmp   = form.down('[fieldLabel=MODELO]');
                    var versionCmp  = form.down('[fieldLabel*=VERSI]');
                    debug('form:'        , form);
                    debug('claveCmp:'    , claveCmp);
                    debug('marcaCmp:'    , marcaCmp);
                    debug('submarcaCmp:' , submarcaCmp);
                    debug('modeloCmp:'   , modeloCmp);
                    debug('versionCmp:'  , versionCmp);
                    
                    var claveVal = claveCmp.getValue();
                    if(!Ext.isEmpty(claveVal))
                    {
                        var record=recordClave;
                        debug('claveVal:' , claveVal);
                        debug('record:'   , record);
                        if(record!=false)
                        {
                            var claveDisplay      = record.get('value');
                            var claveDisplaySplit = claveDisplay.split(' - ');
                            var marcaDisplay      = claveDisplaySplit[1];
                            var submarcaDisplay   = claveDisplaySplit[2];
                            var modeloDisplay     = claveDisplaySplit[3];
                            var versionDisplay    = claveDisplaySplit[4];
                            debug('claveDisplay:',claveDisplay);
                            
                            var tipovalorName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=TIPO VALOR]').name;
                            var tipovalorVal  = _p30_selectedRecord.get(tipovalorName)-0;
                            var anioAc        = new Date().getFullYear()-0;
                            debug('tipovalorVal:'  , tipovalorVal);
                            debug('modeloDisplay:' , modeloDisplay);
                            
                            if(tipovalorVal==3&&anioAc-(modeloDisplay-0)>1)
                            {
                                mensajeWarning('Para valor de factura solo se permiten modelos del a&ntilde;o actual o anterior');
                            }
                            else
                            {
                                form.setLoading(true);
                            
                                marcaCmp.setValue(marcaCmp.findRecordByDisplay(marcaDisplay));
                            
                                submarcaCmp.auxDisplay = submarcaDisplay;
                                modeloCmp.auxDisplay   = modeloDisplay;
                                versionCmp.auxDisplay  = versionDisplay;
                            
                                submarcaCmp.heredar(true,function(cmp)
                                {
                                    cmp.setValue(cmp.findRecordByDisplay(cmp.auxDisplay));
                                    _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit'))
                                        .down('[fieldLabel=MODELO]').heredar(true,function(cmp)
                                    {
                                        cmp.setValue(cmp.auxDisplay);
                                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit'))
                                            .down('[fieldLabel*=VERSI]').heredar(true,function(cmp)
                                        {
                                            cmp.setValue(cmp.findRecordByDisplay(cmp.auxDisplay));
                                            _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).setLoading(false);
                                            _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit'))
                                                .down('[fieldLabel*=VALOR VEH]').heredar(false);
                                        });
                                    });
                                });
                            }
                        }
                    }
                };
                
                form.down('[fieldLabel*=CLAVE]').on(
                {
                    select : function(me,records)
                    {
                        _fieldById('_p30_tatrisitAutoForm'+_p30_selectedRecord.get('cdtipsit')).buscarAutoDesc(records[0]);
                    }
                });
            }
            //seleccionar auto
            
            //tipo valor
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var tipovalorCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=TIPO VALOR]');
                debug('@CUSTOM tipovalor:',tipovalorCmp);
                
                tipovalorCmp.store.load(
                {
                    params :
                    {
                        'params.cdtipsit' : cdtipsit
                    }
                });
                
                tipovalorCmp.cdtipsit=cdtipsit+'';
                tipovalorCmp.on(
                {
                    select : function(me)
                    {
                        var record   = _p30_selectedRecord;
                        var cdtipsit = record.get('cdtipsit');
                        var form     = _p30_tatrisitParcialForms[cdtipsit];
                        var meVal    = me.getValue()-0;
                        
                        var modeloName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel=MODELO]').name;
                        var modeloVal  = record.get(modeloName);
                        if(!Ext.isEmpty(modeloVal))
                        {
                            var valorName  = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*=VALOR VEH]').name;
                            var valorVal   = record.get(valorName);
                            var valorReset = false;
                            if(!Ext.isEmpty(valorVal))
                            {
                                valorReset=true;
                                record.set(valorName,'');
                            }
                            
                            var anioAc      = new Date().getFullYear()-0;
                            var modeloReset = false;
                            
                            debug('meVal'     , meVal);
                            debug('anioAc'    , anioAc);
                            debug('modeloVal' , modeloVal);
                            
                            if(meVal==3&&anioAc-(modeloVal-0)>1)
                            {
                                modeloReset=true;
                                record.set(modeloName , '');
                                record.set('cdplan'   , '');
                                //form.down('[name=cdplan]').heredar();
                            }
                            
                            if(valorReset&&modeloReset)
                            {
                                mensajeWarning('Para valor de factura solo se permiten modelos del a&ntilde;o '
                                    +'actual o anterior, favor de actualizar el modelo y el valor del veh&iacute;culo');
                            }
                            else if(valorReset)
                            {
                                mensajeWarning('Debe actualizar el valor del veh&iacute;culo');
                            }
                            else if(modeloReset)
                            {
                                mensajeWarning('Para valor de factura solo se permiten modelos del a&ntilde;o '
                                    +'actual o anterior, favor de actualizar el modelo');
                            }
                        }
                        
                        if(_p30_smap1.tipoflot=='P' && meVal==3)
                        {
                           var numeroDiasFechaFacturacion;
                            Ext.Ajax.request
                            ({ 
                                url     : _p30_urlRecuperacion
                                ,params :
                                     {
                                       'params.consulta' :'RECUPERAR_DIAS_FECHA_FACTURACION'
                                      ,'params.cdtipsit' : me.cdtipsit
                    }
                            ,success : function(response)
                            {                                   
                             try
                               {
                                var json = Ext.decode(response.responseText);
                                debug('### load status:',json);
                                if(json.success==true)
                                   {
                                     var hoy = new  Date();
                                     numeroDiasFechaFacturacion = json.params.dias;
                                     var limite = Ext.Date.add(hoy, Ext.Date.DAY,-1*(numeroDiasFechaFacturacion));
                                     
                                     var campoFecha = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=FECHA DE FACTURA]');
                                     
                                     campoFecha.setMinValue(limite);
                                     campoFecha.setMaxValue(hoy);
                                     campoFecha.show();
                                     campoFecha.allowBlank=false;
                                     campoFecha.up('form').doLayout();
                                     
                                     debug('campoFecha:',campoFecha);
                                     debug('campoFecha.up form:',campoFecha.up('form'));
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
                             errorComunicacion(null,'Error al obtener dias para factura');
                           }
                });
                        }
                        else if(_p30_smap1.tipoflot=='P' && meVal!=3)
                          {  var campo= _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=FECHA DE FACTURA]');
                             campo.reset();
                             campo.hide();
                             campo.allowBlank=true;
                             campo.up('form').doLayout();
                          }  
                        else if(_p30_smap1.tipoflot=='F')
                        {  var campo= _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=FECHA DE FACTURA]');
                           campo.reset();
                           campo.show();
                           campo.allowBlank=true;
                           campo.up('form').doLayout();
                        }  
                        }
           });
            //tipo valor
        }
            //tipo factura
            if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1 && _p30_smap1.tipoflot=='P')
            {  /* ------------------------------- VALOR FACTURA ------------------- */
                var fechaFacturaCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=FECHA DE FACTURA]');
                fechaFacturaCmp.cdtipsit = cdtipsit+'';
                fechaFacturaCmp.hide();
                debug('@CUSTOM fecha fact hide:',fechaFacturaCmp);
            }
            //tipo factura
            //tipo cambio
            if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var dolarCmp = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=TIPO DE CAMBIO]');
                debug('@CUSTOM dolarCmp:',dolarCmp,'.');
                dolarCmp.anidado = true;
                dolarCmp.heredar = function(remoto,callback)
                {
                    var record   = _p30_selectedRecord;
                    var cdtipsit = record.get('cdtipsit');
                    var form     = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                    var me       = form.down('[fieldLabel*=TIPO DE CAMBIO]');
                    var serieCmp = form.down('[fieldLabel*=MERO DE SERIE]');
                    
                    me.hide();
                    
                    if(Ext.isEmpty(_p30_precioDolarDia))
                    {
                        serieCmp.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url      : _p30_urlCargarTipoCambioWS
                            ,success : function(response)
                            {
                                serieCmp.setLoading(false);
                                var json=Ext.decode(response.responseText);
                                debug('### dolar:',json);
                                _p30_precioDolarDia=json.smap1.dolar;
                                me.setValue(_p30_precioDolarDia);
                            }
                            ,failure : function()
                            {
                                serieCmp.setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                    else
                    {
                        me.setValue(_p30_precioDolarDia);
                    }
                };
            }
            //tipo cambio
            
            //serie
            if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var serieCmp = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel*=MERO DE SERIE]');
                debug('@CUSTOM serie:',serieCmp);
                serieCmp.anidado = true;
                serieCmp.heredar = function()
                {
                    var record    = _p30_selectedRecord;
                    var cdtipsit  = record.get('cdtipsit');
                    var window    = _p30_tatrisitAutoWindows[cdtipsit];
                    var postalCmp = _fieldLikeLabel('CIRCULACI',_fieldById('_p30_form'));
                    
                    if(!postalCmp.isValid())
                    {
                        window.noMostrar=true;
                        mensajeWarning('Debe seleccionar el c&oacute;digo postal primero',function()
                        {
                            _fieldLikeLabel('CIRCULACI',_fieldById('_p30_form')).focus();
                        });
                    }
                };
                
                serieCmp.recuperar = function()
                {
                    var record     = _p30_selectedRecord;
                    var cdtipsit   = record.get('cdtipsit');
                    var form       = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                    var me         = form.down('[fieldLabel*=MERO DE SERIE]');
                    var meVal      = me.getValue();
                    var marcaCmp   = form.down('[fieldLabel=MARCA]');
                    var modeloCmp  = form.down('[fieldLabel=MODELO]');
                    var versionCmp = form.down('[fieldLabel*=DESCRIPCI]');
                    var sumaCmp    = form.down('[fieldLabel*=VALOR VEH]');
                    var suma2Cmp   = form.down('[fieldLabel=RESPALDO VALOR NADA]');
                    
                    var precioDolar = form.down('[fieldLabel*=TIPO DE CAMBIO]').getValue()-0;
                    var postalVal   = _fieldLikeLabel('CIRCULACI',_fieldById('_p30_form')).getValue();
                    
                    var tipovehName = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*=TIPO VEH]').name;
                    var tipovehVal  = record.get(tipovehName);
                    debug('tipovehName:' , tipovehName,record.data);
                    debug('tipovehVal:'  , tipovehVal);
                    
                    if(!Ext.isEmpty(meVal)&&meVal.length==17)
                    {
                        me.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url : _p30_urlNada
                            ,params :
                            {
                                 'smap1.vim'      : meVal
                                ,'smap1.cdramo'   : _p30_smap1.cdramo
                                ,'smap1.cdtipsit' : cdtipsit
                                ,'smap1.tipoveh'  : tipovehVal
                                ,'smap1.codpos'   : postalVal
                            }
                            ,success : function(response)
                            {
                                me.setLoading(false);
                                var json=Ext.decode(response.responseText);
                                debug('### nada:',json);
                                if(json.success)
                                {
                                    marcaCmp.setValue(json.smap1.AUTO_MARCA);
                                    modeloCmp.setValue(json.smap1.AUTO_ANIO);
                                    versionCmp.setValue(json.smap1.AUTO_DESCRIPCION);
                                    sumaCmp.setMinValue
                                    (
                                        (
                                            (
                                                (
                                                    json.smap1.AUTO_PRECIO-0
                                                )*precioDolar
                                            )
                                            *
                                            (
                                                1-
                                                (
                                                    json.smap1.FACTOR_MIN-0
                                                )
                                            )
                                        ).toFixed(2)
                                    );
                                    sumaCmp.setMaxValue
                                    (
                                        (
                                            (
                                                (
                                                    json.smap1.AUTO_PRECIO-0
                                                )*precioDolar
                                            )
                                            *
                                            (
                                                1+
                                                (
                                                    json.smap1.FACTOR_MAX-0
                                                )
                                            )
                                        ).toFixed(2)
                                    );
                                    sumaCmp.setValue
                                    (
                                        (
                                            (
                                                json.smap1.AUTO_PRECIO-0
                                            )*precioDolar
                                        ).toFixed(2)
                                    );
                                    suma2Cmp.setValue
                                    (
                                        (
                                            (
                                                json.smap1.AUTO_PRECIO-0
                                            )*precioDolar
                                        ).toFixed(2)
                                    );
                                }
                                else
                                {
                                	var formaAuto = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);

                                	if(
                                	    ('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1) 
                                         && 
//                                         (rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
                                           RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
                                      ){
                                		    _fieldByName('parametros.pv_otvalor04',formaAuto).setReadOnly(false);
                                            _fieldByName('parametros.pv_otvalor05',formaAuto).setReadOnly(false);
                                            _fieldByName('parametros.pv_otvalor06',formaAuto).setReadOnly(false);
                                            _fieldByName('parametros.pv_otvalor07',formaAuto).setReadOnly(false);
                                            _fieldByName('parametros.pv_otvalor26',formaAuto).setReadOnly(false);
                                       }
                                	else
                                		{
                                			_fieldByName('parametros.pv_otvalor04',formaAuto).setReadOnly(true);
	                                        _fieldByName('parametros.pv_otvalor05',formaAuto).setReadOnly(true);
	                                        _fieldByName('parametros.pv_otvalor06',formaAuto).setReadOnly(true);
	                                        _fieldByName('parametros.pv_otvalor07',formaAuto).setReadOnly(true);
	                                        _fieldByName('parametros.pv_otvalor26',formaAuto).setReadOnly(true);
                                		}
                                	
                                	mensajeError(json.error);
                                }
                            }
                            ,failure : function()
                            {
                                me.setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                    else
                    {
                        marcaCmp.setValue('');
                        modeloCmp.setValue('');
                        versionCmp.setValue('');
                        sumaCmp.setValue('');
                        suma2Cmp.setValue('');
                    }
                };
                
                serieCmp.on(
                {
                    change : function(me)
                    {
                        me.recuperar();
                    }
                });
            }
            //serie
            
            //respaldo nada
            if('|AF|PU|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var respNadaCmp = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=RESPALDO VALOR NADA]');
                debug('@CUSTOM respaldo nada:',respNadaCmp);
                respNadaCmp.anidado = true;
                respNadaCmp.heredar = function()
                {
                    var record    = _p30_selectedRecord;
                    var cdtipsit  = record.get('cdtipsit');
                    var me        = _fieldById('_p30_tatrisitAutoForm'+cdtipsit).down('[fieldLabel=RESPALDO VALOR NADA]');
                    me.hide();
                };
            }
            //respaldo nada
            
            //carga
            if('|CR|PC|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var cargaCmp = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=CARGA]');
                debug('@CUSTOM carga:',cargaCmp,'.');
                cargaCmp.anidado = true;
                cargaCmp.heredar = function(remoto,callback)
                {
                    var record      = _p30_selectedRecord;
                    var cdtipsit    = record.get('cdtipsit');
                    var me          = _fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel=CARGA]');
                    var negocioVal  = _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue();
                    if(!Ext.isEmpty(negocioVal))
                    {
                        me.getStore().load(
                        {
                            params :
                            {
                                'params.negocio' : negocioVal
                            }
                            ,callback : function()
                            {
                                if(!Ext.isEmpty(callback))
                                {
                                    try
                                    {
                                        callback
                                        (
                                            _fieldById('_p30_tatrisitParcialForm'+_p30_selectedRecord.get('cdtipsit'),true)
                                                .down('[fieldLabel=CARGA]')
                                        );
                                    }
                                    catch(ex)
                                    {
                                        debugError(ex);
                                    }
                                }
                            } 
                        });
                    }
                    else
                    {
                        me.getStore().removeAll();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(me);
                        }
                    }
                };
            }
            //carga
            
            //cilindraje
            if('|MO|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
            {
                var form     = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
                var marcaCmp = form.down('[fieldLabel=MARCA]');
                debug('@CUSTOM marcaCmp cilindraje:',marcaCmp,'.');
                marcaCmp.on(
                {
                    change : function(me)
                    {
                        me.up('form').down('[fieldLabel=CILINDRAJE]').reset();
                    }
                });
            }
            //cilindraje
        }
        //herencia situaciones
    }
    //ramo 5
    
    Ext.Ajax.request(
    {
        url : _p30_urlCargarParametros
        ,params  :
        {
            'smap1.parametro' : 'REMOLQUES_POR_TRACTOCAMION'
            ,'smap1.cdramo'   : _p30_smap1.cdramo
            ,'smap1.cdtipsit' : '*'
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### remolques por tracto:',json);
            if(json.exito)
            {
                //5100
                //6001
                _p30_smap1.remolquesPorTracto=json.smap1.P1VALOR; // Remolques x tractocamion
                _p30_smap1.totalTractocamion =json.smap1.P2VALOR; // Total de Tractocamiones
                _p30_smap1.totalCamiones =json.smap1.P3VALOR; // total de camiones
            }
            else
            {
                _p30_smap1.remolquesPorTracto=0;
                _p30_smap1.totalTractocamion = 0;
                _p30_smap1.totalCamiones = 0;
                mensajeError(json.respuesta);
            }
            debug('_p30_smap1:',_p30_smap1);
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
    
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />
    ////// custom //////
    
    ////// loaders //////
    if(_p30_endoso)
    {
        var panel=_fieldById('_p30_panelpri');
        panel.setLoading(true);
        Ext.Ajax.request(
        {
            url      : _p30_urlCargarDatosEndoso
            ,params  :
            {
                'smap1.cdunieco'  : _p30_smap1.CDUNIECO
                ,'smap1.cdramo'   : _p30_smap1.CDRAMO
                ,'smap1.estado'   : _p30_smap1.ESTADO
                ,'smap1.nmpoliza' : _p30_smap1.NMPOLIZA
                ,'smap1.nmsuplem' : _p30_smap1.NMSUPLEM
            }
            ,success : function(response)
            {
                panel.setLoading(false);
                var json=Ext.decode(response.responseText);
                debug('### datos endoso:',json);
                if(json.success)
                {
                    _p30_cargarIncisoXpolxTvalopolTconvalsit(json);
                    var formItems = Ext.ComponentQuery.query('[name]',_fieldById('_p30_form'));
                    for(var i in formItems)
                    {
                        if(formItems[i].name!='fechaEndoso')
                        {
                            formItems[i].allowBlank = true;
                            formItems[i].setReadOnly(true);
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
                panel.setLoading(false);
                errorComunicacion();
            }
        });
        
        Ext.Ajax.request(
	    {
	        url      : _p30_urlRecuperacionSimple
	        ,params  :
	        {
	            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
	            ,'smap1.cdunieco'     : _p30_smap1.CDUNIECO
	            ,'smap1.cdramo'       : _p30_smap1.CDRAMO
	            ,'smap1.estado'       : _p30_smap1.ESTADO
	            ,'smap1.nmpoliza'     : _p30_smap1.NMPOLIZA
	            ,'smap1.cdtipsup'     : _p30_smap1.cdtipsup
	        }
	        ,success : function(response)
	        {
	            var json = Ext.decode(response.responseText);
	            debug('### fechas:',json);
	            if(json.exito)
	            {
	                _fieldByName('fechaEndoso').setMinValue(json.smap1.FECHA_MINIMA);
	                _fieldByName('fechaEndoso').setMaxValue(json.smap1.FECHA_MAXIMA);
	                _fieldByName('fechaEndoso').setReadOnly(json.smap1.EDITABLE=='N');
	                _fieldByName('fechaEndoso').isValid();
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
    
    _p30_recuperarClienteTramite();
    
    _p30_recuperarCotizacionDeTramite();
    
    try {
        _p30_recuperarPolizaSIGS();
    } catch (e) {
        debugError('error al invocar funcion _p30_recuperarPolizaSIGS');
    }
    
    ////// loaders //////
});

////// funciones //////
function _p30_calculaVigencia()
{
    debug('>_p30_calculaVigencia');
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    
    var itemVigencia=_fieldByLabel('VIGENCIA',_fieldById('_p30_form'));
    itemVigencia.hide();
    
    if(feini.isValid()&&fefin.isValid())
    {
        var milisDif = Ext.Date.getElapsed(feini.getValue(),fefin.getValue());
        var diasDif  = (milisDif/1000/60/60/24).toFixed(0);
        debug('milisDif:',milisDif,'diasDif:',diasDif);
        itemVigencia.setValue(diasDif);
    }
    debug('<_p30_calculaVigencia');
}

function _p30_configuracionPanelDinClic(cdtipsit,titulo)
{
    debug('>_p30_configuracionPanelDinClic:',cdtipsit,titulo);
    if(Ext.isEmpty(_fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()))
    {
        mensajeWarning('Seleccione el negocio');
    }
    else
    {
        var panel    = _p30_paneles[cdtipsit];
        var itemDesc = panel.down('[fieldLabel*=DESCUENTO]');
        if( RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
//         	(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
            &&!Ext.isEmpty(itemDesc)
            && 'TL'.lastIndexOf(cdtipsit)==-1
          )
        {
            Ext.Ajax.request(
            {
                url     : _p30_urlRecuperacionSimple
                ,params :
                {
                    'smap1.procedimiento' : 'RECUPERAR_DESCUENTO_RECARGO_RAMO_5'
                    ,'smap1.cdtipsit'     : _p30_smap1.cdtipsit
                    ,'smap1.cdagente'     : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
                    ,'smap1.negocio'      : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                    ,'smap1.tipocot'      : _p30_smap1.tipoflot
                    ,'smap1.cdsisrol'     : _p30_smap1.cdsisrol
                    ,'smap1.cdusuari'     : _p30_smap1.cdusuari
                }
                ,success : function(response)
                {
                    var json = Ext.decode(response.responseText);
                    debug('### cargar rango descuento ramo 5:',json);
                    if(json.exito)
                    {
                        itemDesc.minValue=100*(json.smap1.min-0);
                        itemDesc.maxValue=100*(json.smap1.max-0);
                        itemDesc.isValid();
                        debug('min:',itemDesc.minValue);
                        debug('max:',itemDesc.maxValue);
                        itemDesc.setReadOnly(false);
                    }
                    else
                    {
                        itemDesc.minValue=0;
                        itemDesc.maxValue=0;
                        itemDesc.setValue(0);
                        itemDesc.isValid();
                        itemDesc.setReadOnly(true);
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    itemDesc.minValue=0;
                    itemDesc.maxValue=0;
                    itemDesc.setValue(0);
                    itemDesc.isValid();
                    itemDesc.setReadOnly(true);
                    errorComunicacion();
                }
            });
        }
        panel.setTitle(titulo);
        if(panel.valores!=false)
        {
            panel.down('form').loadRecord(new _p30_modelo(panel.valores));
        }
        else
        {
            panel.down('form').getForm().reset();
        }
        panel.callback=function()
        {
            var form = panel.down('form');
            var valido = form.isValid();
            if(!valido)
            {
                datosIncompletos();
            }
        
            if(valido)
            {
                panel.valores=form.getValues();
                panel.hide();
                debug('panel:',panel);
            }
        }
        centrarVentanaInterna(panel.show());
    }
    debug('<_p30_configuracionPanelDinClic');
}

function _p30_agregarAuto()
{
    debug('>_p30_agregarAuto');
    
    try{
    	if(_p30_smap1.turistas=='S' && _p30_store.count()>=3){
    		mensajeWarning('Solo puedes agregar 3 incisos');
    		return;
    	}
    	
    }catch(e){
    	debugError(e);
    }
    var valido=true;
    if(valido&&_p30_smap1.cdramo+'x'=='5x')
    {
        valido=!Ext.isEmpty(_fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue());
        if(!valido)
        {
            mensajeWarning('Seleccione el negocio');
        }
    }
    
    if(valido)
    {
        var record=new _p30_modelo();
        _p30_store.add(record);
        _p30_numerarIncisos();
        _fieldById('_p30_grid').getSelectionModel().select(record);
    }
    
    debug('<_p30_agregarAuto');
}

function _p30_gridBotonConfigClic(view,row,col,item,e,record)
{
    debug('>_p30_gridBotonConfigClic:',record);
    var cdtipsit = record.get('cdtipsit');
    
    var valido = !Ext.isEmpty(cdtipsit);
    if(!valido)
    {
        mensajeWarning('Debe seleccionar el tipo de veh&iacute;culo');
    }
    
    if(valido)
    {
        var cdtipsitPanel = _p30_smap1['destino_'+cdtipsit];
        debug('cdtipsit:',cdtipsit,'cdtipsitPanel:',cdtipsitPanel);
        var panel    = _p30_paneles[cdtipsitPanel];
        var itemDesc = panel.down('[fieldLabel*=DESCUENTO]');
        if( RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
//         	(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
            &&!Ext.isEmpty(itemDesc))
        {
            Ext.Ajax.request(
            {
                url     : _p30_urlRecuperacionSimple
                ,params :
                {
                    'smap1.procedimiento' : 'RECUPERAR_DESCUENTO_RECARGO_RAMO_5'
                    ,'smap1.cdtipsit'     : _p30_smap1.cdtipsit
                    ,'smap1.cdagente'     : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
                    ,'smap1.negocio'      : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                    ,'smap1.tipocot'      : _p30_smap1.tipoflot
                    ,'smap1.cdsisrol'     : _p30_smap1.cdsisrol
                    ,'smap1.cdusuari'     : _p30_smap1.cdusuari
                }
                ,success : function(response)
                {
                    var json = Ext.decode(response.responseText);
                    debug('### cargar rango descuento ramo 5:',json);
                    if(json.exito)
                    {
                        itemDesc.minValue=100*(json.smap1.min-0);
                        itemDesc.maxValue=100*(json.smap1.max-0);
                        itemDesc.isValid();
                        debug('min:',itemDesc.minValue);
                        debug('max:',itemDesc.maxValue);
                        itemDesc.setReadOnly(false);
                    }
                    else
                    {
                        itemDesc.minValue=0;
                        itemDesc.maxValue=0;
                        itemDesc.setValue(0);
                        itemDesc.isValid();
                        itemDesc.setReadOnly(true);
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    itemDesc.minValue=0;
                    itemDesc.maxValue=0;
                    itemDesc.setValue(0);
                    itemDesc.isValid();
                    itemDesc.setReadOnly(true);
                    errorComunicacion();
                }
            });
        }
        
        var form  = panel.down('form');
        debug('form values:',form.getValues(),'record.data:',record.data);
        var valoresExtras = false;
        for(var prop in form.getValues())
        {
            if(cdtipsitPanel==cdtipsit)
            {
                if(!Ext.isEmpty(record.get(prop)))
                {
                    debug('el valor ',record.get(prop),' encendio la carga del record en el formulario');
                    valoresExtras = true;
                    break;
                }
            }
            else
            {
                var cmpPanel = panel.down('[name='+prop+']');
                debug('cmpPanel:',cmpPanel);
                var fieldLabel = cmpPanel.fieldLabel;
                debug('fieldLabel:',fieldLabel);
                if(cmpPanel.auxiliar=='adicional'&&!Ext.isEmpty(record.get(prop)))
                {
                    //alert('ADIC!-'+fieldLabel+'-'+prop);
                    valoresExtras = true;
                    break;
                }
                else
                {
                    var cmpByLabel = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*='+_substringComa(fieldLabel)+']');
                    if(!Ext.isEmpty(cmpByLabel))
                    {
                        var nameByLabel = cmpByLabel.name;
                        debug('buscando valores en nameByLabel:',nameByLabel,'.');
                        if(!Ext.isEmpty(record.get(nameByLabel)))
                        {
                            //alert('SI!-'+fieldLabel+'-'+nameByLabel);
                            valoresExtras = true;
                            break;
                        }
                    }
                    else
                    {
                        //alert('NO!-'+fieldLabel+'-'+cdtipsit);
                        debug('No existe el dsatribu en el cdtipsit:',fieldLabel,cdtipsit,'.');
                    }
                }
            }
        }
        if(record.get('personalizado')=='si'||valoresExtras)
        {
            panel.setTitle('CONFIGURACI&Oacute;N DE PAQUETE (PERSONALIZADA)');
            if(cdtipsitPanel==cdtipsit)
            {
                form.loadRecord(record);
            }
            else
            {
                form.getForm().reset();
                var values = form.getValues();
	            for(var prop in values)
	            {	            
	                var cmpPanel = panel.down('[name='+prop+']');
	                debug('cmpPanel:',cmpPanel);
	                var fieldLabel = cmpPanel.fieldLabel;
	                debug('fieldLabel:',fieldLabel);
	                if(cmpPanel.auxiliar=='adicional')
                    {
                        debug('set normal, porque es adicional');
                        //alert('ADIC!-'+fieldLabel+'-'+prop);
                        cmpPanel.setValue(record.get(prop));
                    }
                    else
                    {
                        var cmpByLabel = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*='+_substringComa(fieldLabel)+']');
                        if(!Ext.isEmpty(cmpByLabel))
                        {
                            var nameByLabel = cmpByLabel.name;
                            debug('setValue en nameByLabel:',nameByLabel,'.');
                            cmpPanel.setValue(record.get(nameByLabel));
                            //alert('SI!-'+fieldLabel+'-'+nameByLabel);
                        }
                        else
                        {
                            //alert('NO!-'+fieldLabel+'-'+cdtipsit);
                            debug('No existe el dsatribu en el cdtipsit:',fieldLabel,cdtipsit,'.');
                        }
                    }
	            }
            }
        }
        else if(panel.valores!=false)
        {
            panel.setTitle('CONFIGURACI&Oacute;N DE PAQUETE (VALORES DE SITUACI&Oacute;N)');
            form.loadRecord(new _p30_modelo(panel.valores));
        }
        else
        {
            panel.setTitle('CONFIGURACI&Oacute;N DE PAQUETE (VAC&Iacute;A)');
            form.getForm().reset();
        }
        panel.callback=function()
        {
            var values = form.getValues();
            for(var prop in values)
            {
                if(cdtipsitPanel==cdtipsit)
                {
                    record.set(prop,values[prop]);
                }
                else
                {
                    var cmpOriginal = _p30_paneles[cdtipsitPanel].down('[name='+prop+']');
                    debug('cmpOriginal:',cmpOriginal);
                    debug('cmpOriginal.auxiliar:',cmpOriginal.auxiliar,'.');
                    var fieldLabel = cmpOriginal.fieldLabel;
                    debug('fieldLabel:',fieldLabel);
                    if(cmpOriginal.auxiliar=='adicional')
                    {
                        debug('set normal, porque es adicional');
                        //alert('ADIC!-'+fieldLabel+'-'+prop);
                        record.set(prop,values[prop]);
                    }
                    else
                    {
                        var cmpByLabel  = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*='+_substringComa(fieldLabel)+']');
                        if(!Ext.isEmpty(cmpByLabel))
                        {
                            var nameByLabel = cmpByLabel.name;
                            debug('set en nameByLabel para cdtipsit:',nameByLabel,cdtipsit,'.');
                            record.set(nameByLabel,values[prop]);
                            //alert('SI!-'+fieldLabel+'-'+nameByLabel);
                        }
                        else
                        {
                            //alert('NO!-'+fieldLabel+'-'+cdtipsit);
                            debug('No existe el dsatribu en el cdtipsit:',fieldLabel,cdtipsit,'.');
                        }
                    }
                }
            }
            record.set('personalizado','si');
            debug('record:',record);
            panel.hide();
        }
        centrarVentanaInterna(panel.show());
    }
    debug('<_p30_gridBotonConfigClic');
}

function _p30_gridBotonEliminarClic(view,row,col,item,e,record)
{
    debug('>_p30_gridBotonEliminarClic:',record);
    _p30_store.remove(record);
    _p30_numerarIncisos();
    _p30_semaforoBorrar=true;
    debug('<_p30_gridBotonEliminarClic');
}

function _p30_ramo5AgenteSelect(comp,records)
{
    var cdagente = typeof records == 'string' ? records : records[0].get('key');
    debug('>_p30_ramo5AgenteSelect cdagente:',cdagente);
    Ext.Ajax.request(
    {
        url     : _p30_urlCargarCduniecoAgenteAuto
        ,params :
        {
            'smap1.cdagente' : cdagente
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('#### obtener cdunieco agente response:',json);
            if(json.exito)
            {
                if(Ext.isEmpty(_p30_smap1.ntramite))
                {
                    _p30_smap1.cdunieco=json.smap1.cdunieco;
                }
                debug('_p30_smap1:',_p30_smap1);
                Ext.Ajax.request(
                {
                    url     : _p30_urlCargarRetroactividadSuplemento
                    ,params :
                    {
                        'smap1.cdunieco'  : _p30_smap1.cdunieco
                        ,'smap1.cdramo'   : _p30_smap1.cdramo
                        ,'smap1.cdtipsup' : 1
                        ,'smap1.cdusuari' : _p30_smap1.cdusuari
                        ,'smap1.cdtipsit' : _p30_smap1.cdtipsit
                    }
                    ,success : function(response)
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### obtener retroactividad:',json);
                        if(json.exito)
                        {
                            var feini = _fieldByName('feini');
                            var fefin = _fieldByName('fefin');
                            
                            feini.setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,(json.smap1.retroac-0)*-1));
                            feini.setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi-0));
                            feini.isValid();
                        }
                        else
                        {
                            mensajeError(json.respuesta);
                        }
                    }
                    ,failure : errorComunicacion
                });
            }
            else
            {
                _fieldByLabel('AGENTE',_fieldById('_p30_form')).reset();
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _fieldByLabel('AGENTE',_fieldById('_p30_form'))
            errorComunicacion();
        }
    });
    debug('<_p30_ramo5AgenteSelect');
}

function _p30_ramo5ClienteChange()
{
    var combcl  = _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_form'));
    
    debug('>_p30_ramo5ClienteChange value:',combcl.getValue());
    
    var nombre  = _fieldLikeLabel('NOMBRE CLIENTE' , _fieldById('_p30_form'));
    var tipoper = _fieldByLabel('TIPO PERSONA'     , _fieldById('_p30_form'));
    var codpos  = _fieldLikeLabel('CP CIRCULACI'   , _fieldById('_p30_form'));
    
    //cliente nuevo
    if(combcl.getValue()=='S')
    {
        nombre.reset();
        tipoper.reset();
        codpos.reset();
        
        nombre.setReadOnly(false);
        tipoper.setReadOnly(false);
        codpos.setReadOnly(false);
        
        _p30_recordClienteRecuperado=null;
    }
    //recuperar cliente
    else if(combcl.getValue()=='N' && ( Ext.isEmpty(combcl.semaforo)||combcl.semaforo==false ) )
    {
        nombre.reset();
        tipoper.reset();
        codpos.reset();
        
        nombre.setReadOnly(true);
        tipoper.setReadOnly(true);
        codpos.setReadOnly(true);
        
        var ventana=Ext.create('Ext.window.Window',
        {
            title      : 'Recuperar cliente'
            ,modal     : true
            ,width     : 600
            ,height    : 400
            ,items     :
            [
                {
                    layout    : 'hbox'
                    ,defaults : { style : 'margin : 5px;' }
                    ,items    :
                    [
                        {
                            xtype       : 'textfield'
                            ,name       : '_p30_recuperaRfc'
                            ,fieldLabel : 'RFC'
                            ,minLength  : 9
                            ,maxLength  : 13
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Buscar'
                            ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                            ,handler : function(button)
                            {
                                debug('recuperar cliente buscar');
                                var rfc=_fieldByName('_p30_recuperaRfc').getValue();
                                var valido=true;
                                if(valido)
                                {
                                    valido = !Ext.isEmpty(rfc)
                                             &&rfc.length>8
                                             &&rfc.length<14;
                                    if(!valido)
                                    {
                                        mensajeWarning('Introduza un RFC v&aacute;lido');
                                    }
                                }
                                
                                if(valido)
                                {
                                    button.up('window').down('grid').getStore().load(
                                    {
                                        params :
                                        {
                                            'map1.pv_rfc_i'       : rfc
                                            ,'map1.cdtipsit'      : _p30_smap1.cdtipsit
                                            ,'map1.pv_cdtipsit_i' : _p30_smap1.cdtipsit
                                            ,'map1.pv_cdunieco_i' : _p30_smap1.cdunieco
                                            ,'map1.pv_cdramo_i'   : _p30_smap1.cdramo
                                            ,'map1.pv_estado_i'   : 'W'
                                            ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                                        }
                                    });
                                }
                            }
                        }
                    ]
                }
                ,Ext.create('Ext.grid.Panel',
                {
                    title    : 'Resultados'
                    ,columns :
                    [
                        {
                            xtype    : 'actioncolumn'
                            ,width   : 30
                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                            ,handler : function(view,row,col,item,e,record)
                            {
                                debug('recuperar cliente handler record:',record);
                                _p30_recordClienteRecuperado=record;
                                nombre.setValue(record.raw.NOMBRECLI);
                                tipoper.setValue(record.raw.TIPOPERSONA);
                                codpos.setValue(record.raw.CODPOSTAL);
                                ventana.destroy();
                            }
                        }
                        ,{
                            text       : 'Nombre'
                            ,dataIndex : 'NOMBRECLI'
                            ,width     : 200
                        }
                        ,{
                            text       : 'Direcci&oacute;n'
                            ,dataIndex : 'DIRECCIONCLI'
                            ,flex      : 1
                        }
                    ]
                    ,store : Ext.create('Ext.data.Store',
                    {
                        model     : '_p30_modeloRecuperado'
                        ,autoLoad : false
                        ,proxy    :
                        {
                            type    : 'ajax'
                            ,url    : _p30_urlRecuperarCliente
                            ,timeout: 240000
                            ,reader :
                            {
                                type  : 'json'
                                ,root : 'slist1'
                            }
                        }
                    })
                })
            ]
            ,listeners :
            {
                close : function()
                {
                    combcl.setValue('S');
                }
            }
        }).show();
        centrarVentanaInterna(ventana);
    }
    debug('<_p30_ramo5ClienteChange');
}

function _p30_cotizar(sinTarificar)
{
    debug('>_p30_cotizar sinTarificar:',sinTarificar,'.');
    
    var valido = _fieldById('_p30_form').isValid();
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        var error  = '';
        for(var i=0;i<_f1_botones.length;i++)
        {
            var boton    = _f1_botones[i];
            var cdtipsit = boton.cdtipsit;
            var panel    = _p30_paneles[cdtipsit];
            debug('buscando en panel:',panel);
            if(panel.valores==false)
            {
                valido = false;
                error  = error+'FALTA DEFINIR: '+boton.text+'<br/>';
            }
        }
        if(!valido)
        {
            mensajeWarning(error);
        }
    }
    
    if(valido)
    {
    	if(RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol))
//     	(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
        {
            valido = _p30_store.getCount()>=1;
            if(!valido)
            {
                mensajeWarning('Debe capturar al menos un inciso');
            }
        }
        else
        {
        	if(_p30_smap1.turistas!='S'){ 
	            valido = _p30_store.getCount()>=5;
	            if(!valido)
	            {
	                mensajeWarning('Debe capturar al menos cinco incisos');
	            }
        	}
        }
    }
    
    if(valido)
    {
        var error        = '';
        var agregarError = function(cadena,nmsituac)
        {
            valido = false;
            error  = error + 'Inciso ' + nmsituac + ': ' + cadena +'</br>';
        };
        _p30_store.each(function(record)
        {
            var cdtipsit = record.get('cdtipsit');
            var nmsituac = record.get('nmsituac');
            
            if(!Ext.isEmpty(cdtipsit))
            {
                if(Ext.isEmpty(record.get('cdplan')))
                {
                    agregarError('Debe seleccionar el paquete',nmsituac);
                }
                var itemsObliga = Ext.ComponentQuery.query('[swobligaflot=true]',_p30_tatrisitFullForms[cdtipsit]);
                for(var i in itemsObliga)
                {
                    if(Ext.isEmpty(record.get(itemsObliga[i].getName())))
                    {
                        agregarError('Falta definir "'+itemsObliga[i].getFieldLabel()+'"',nmsituac);
                    }
                }
            }
            else
            {
                agregarError('Debe seleccionar el tipo de veh&iacute;culo',nmsituac);
            }
        });
        if(!valido)
        {
            mensajeWarning(error);
        }
    }
    
    if(valido)
    {
    	if(_p30_smap1.tipoflot=='P' 
    	   &&
    	   !RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
//     	   (rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')==-1)
    	   )
    	{	
    		var ncamiones = 0;
            var ntractocamiones = 0;
            var nsemiremolques = 0;
        	
        	var nTipo2  = 0;
        	var nTipo4  = 0;
            var nTipo13 = 0;
            _p30_store.each(function(record)
            {
                if(record.get('cdtipsit')+'x'=='CRx')
                {
            		var tipoVehiName = _p30_tatrisitFullForms['CR'].down('[fieldLabel*=TIPO DE VEH]').name;
            		if(record.get(tipoVehiName)-0==2){
            			ncamiones = ncamiones+1;
            		}
            		else if(record.get(tipoVehiName)-0==4)
                    {
                        ncamiones = ncamiones+1;
                        ntractocamiones = ntractocamiones+1;
                    }else if(record.get(tipoVehiName)-0==13)
                    {
                    	nsemiremolques = nsemiremolques+1;
                    }
                    
                }else if(record.get('cdtipsit')+'x'=='TCx'){
                	ncamiones = ncamiones+1;
                	ntractocamiones = ntractocamiones+1;
                	
                }else if(record.get('cdtipsit')+'x'=='RQx'){
                	nsemiremolques = nsemiremolques+1;
                }
            });
            
            if(ncamiones > _p30_smap1.totalCamiones){
            	valido = false;
            	mensajeWarning("&Uacute;nicamente se puede amparar "+_p30_smap1.totalCamiones+" Camiones en la autorización. <br/>Intente nuevamente.");
            }else{
            	if(ntractocamiones > _p30_smap1.totalTractocamion){
                	valido = false;
                	mensajeWarning("&Uacute;nicamente se puede amparar "+_p30_smap1.totalTractocamion+" Tractocamiones en la autorización. <br/>Intente nuevamente.");
                }else{
                	if(nsemiremolques > (+ntractocamiones * +_p30_smap1.remolquesPorTracto)){
                		valido = false;
                		mensajeWarning('Se permiten '+_p30_smap1.remolquesPorTracto+' remolques por cada tractocami&oacute;n<br/>'
                                +'Y en la cotizaci&oacute;n hay '+nsemiremolques+' remolques y '+ntractocamiones+' tractocamiones');
                	}
                }
            }
    	}
    	
   	     var ck = 'Cambiando tipo de situaci\u00f3n para camiones';
   	     try 
   	     {
   	         _p30_store.each
   	         (
   	          function(record)
   	             {
   	                if( ',CR,'.lastIndexOf(','+record.get('cdtipsit')+',')!=-1)
   	                {
   	                    var tipoVehiName = _p30_tatrisitFullForms['CR'].down('[fieldLabel*=TIPO DE VEH]').name;
   	                    if(record.get(tipoVehiName)-0==2){
   	                        record.cdtipsit_panel = 'PC';
   	                    }
   	                    else if(record.get(tipoVehiName)-0==4)
   	                    {
   	                        record.cdtipsit_panel = 'TC';
   	                    }else if(record.get(tipoVehiName)-0==13)
   	                    {
   	                        record.cdtipsit_panel = 'RQ';
   	                    }
   	                }
   	             }
   	         );
   	     }catch (e) 
   	     {
   	        debug(e);//debugError , ck
   	     }
    }
    
    if(valido)
    {
        valido = _fieldByName('nmpoliza',_fieldById('_p30_form')).sucio==false;
        if(!valido)
        {
            _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo = true;
            _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue('');
            _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo = false;
            valido = true;
        }
    }
    
    debug('_p30_paneles:',_p30_paneles,'valido:',valido);
    if(valido)
    {
        var form = _fieldById('_p30_form');
        
        //copiar paneles a oculto
        var arr = Ext.ComponentQuery.query('#_p30_gridTarifas');
        if(arr.length>0)
        {
            var formDescuentoActual = _fieldById('_p30_formDescuento');
            var formCesion          = _fieldById('_p30_formCesion');
            var recordPaneles       = new _p30_modelo(formDescuentoActual.getValues());
            var itemsCesion         = Ext.ComponentQuery.query('[fieldLabel]',formCesion);
            for(var i=0;i<itemsCesion.length;i++)
            {
                recordPaneles.set(itemsCesion[i].getName(),itemsCesion[i].getValue());
            }
            form.formOculto.loadRecord(recordPaneles);
            debug('form.formOculto.getValues():',form.formOculto.getValues());
        }
    
        debug('length:',_p30_paneles.length,'type:',typeof _p30_paneles);
        var recordsCdtipsit = [];
        for(var cdtipsitPanel in _p30_paneles)
        {
            var panel      = _p30_paneles[cdtipsitPanel];
            var recordBase = new _p30_modelo(panel.valores);
            recordBase.set('cdtipsit',cdtipsitPanel);
            debug('cdtipsitPanel:',cdtipsitPanel,'recordBase:',recordBase);
            recordsCdtipsit[cdtipsitPanel] = recordBase;
        }
        debug('recordsCdtipsit:',recordsCdtipsit);
        var storeTvalosit = Ext.create('Ext.data.Store',
        {
            model : '_p30_modelo'
        });
        var formValuesAux = form.getValues();
        var formValues    = {};
        for(var prop in formValuesAux)
        {
            if(prop+'x'!='x'
                &&prop.slice(0,5)=='param')
            {
                formValues[prop]=formValuesAux[prop];
            }
        }
        debug('formValues:',formValues);
        var valuesFormOculto = form.formOculto.getValues();
        debug('valuesFormOculto:',valuesFormOculto);
        _p30_store.each(function(record)
        {
            var cdtipsit       = record.cdtipsit_panel || record.get('cdtipsit');
            var cdtipsitPanel  = _p30_smap1['destino_'+cdtipsit];
            var recordBase     = recordsCdtipsit[cdtipsitPanel];
            var recordTvalosit = new _p30_modelo(record.data);

            if(cdtipsitPanel==cdtipsit)
            {
                for(var prop in recordTvalosit.getData())
                {
                	var propReal= !Ext.isEmpty(record.cdtipsit_panel)
          				    && prop.slice(0,prop.length-2)=='parametros.pv_otvalor'
          				    &&!Ext.isEmpty(recordBase.raw[prop])
          				    ? prop.slice(0,prop.length-2)+valorOtCorrespondiente(prop.slice(prop.length-2,prop.length),cdtipsit,recordBase)
          				    : prop;
          			var valor = recordTvalosit.get(propReal);
                    var base  = recordBase.get(prop);
                    if(Ext.isEmpty(valor)&&!Ext.isEmpty(base)&& propReal!='parametros.pv_otvalorX')
                    {
                    	recordTvalosit.set(propReal,base);
                    }
                }
            }
            else
            {
                for(var prop in recordTvalosit.getData())
                {
                    var base = recordBase.get(prop);
                    debug('base:',base);
                    
                    var valorValosit = recordTvalosit.get(prop);
                    debug('valorValosit:',valorValosit);
                    
                    var cmpPanel = _p30_paneles[cdtipsitPanel].down('[name='+prop+']');
                    debug('cmpPanel:',cmpPanel,'.');
                    if(!Ext.isEmpty(cmpPanel))
                    {
                        if(cmpPanel.auxiliar=='adicional'
                            &&Ext.isEmpty(valorValosit)
                            &&!Ext.isEmpty(base)
                        )
                        {
                            debug('set normal, porque es adicional');
                            recordTvalosit.set(prop,base);
                        }
                        else
                        {
                            var cmpPanelLabel = cmpPanel.fieldLabel;
                            debug('cmpPanelLabel:',cmpPanelLabel,'.');
                            var cmpByLabel = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*='+_substringComa(cmpPanelLabel)+']');
                            if(!Ext.isEmpty(cmpByLabel))
                            {
                                var nameByLabel = cmpByLabel.name;
                                var valor       = recordTvalosit.get(nameByLabel);
                                debug('valor:',valor);
                                if(Ext.isEmpty(valor)&&!Ext.isEmpty(base))
                                {
                                    recordTvalosit.set(nameByLabel,base);
                                }
                            }
                        }
                    }
                }
            }
            
            if(_p30_smap1.mapeo=='DIRECTO')
            {
                for(var prop in formValues)
                {
                    recordTvalosit.set(prop,formValues[prop]);
                }
                for(var att in valuesFormOculto)
                {
                    var valorOculto  = valuesFormOculto[att];
                    var valorValosit = recordTvalosit.get(att);
                    if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                    {
                        recordTvalosit.set(att,valorOculto);
                    }
                }
            }
            else
            {
                var mapeos = _p30_smap1.mapeo.split('#');
                debug('mapeos:',mapeos);
                for(var i in mapeos)
                {
                    var cdtipsitsMapeo = mapeos[i].split('|')[0];
                    var mapeo          = mapeos[i].split('|')[1];
                    debug('cdtipsit:',cdtipsit,'cdtipsitsMapeo:',cdtipsitsMapeo);
                    if((','+cdtipsitsMapeo+',').lastIndexOf(','+cdtipsit+',')!=-1)
                    {
                        debug('coincidente:',cdtipsitsMapeo,'cdtipsit:',cdtipsit)
                        debug('mapeo:',mapeo);
                        if(mapeo=='DIRECTO')
                        {
                            debug('directo');
                            for(var prop in formValues)
                            {
                                recordTvalosit.set(prop,formValues[prop]);
                            }
                            for(var att in valuesFormOculto)
                            {
                                var valorOculto  = valuesFormOculto[att];
                                var valorValosit = recordTvalosit.get(att);
                                if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                                {
                                    recordTvalosit.set(att,valorOculto);
                                }
                            }
                        }
                        else
                        {
                            var atributos = mapeo.split('@');
                            debug('atributos:',atributos);
                            for(var i in atributos)
                            {
                                var atributoIte = atributos[i];
                                var modelo      = atributoIte.split(',')[0];
                                var origen      = atributoIte.split(',')[1];
                                var pantalla    = atributoIte.split(',')[2];
                                
                                modelo   = 'parametros.pv_otvalor'+(('x00'+modelo)  .slice(-2));
                                pantalla = 'parametros.pv_otvalor'+(('x00'+pantalla).slice(-2));
                                
                                debug('modelo:'   , modelo   , '.');
                                debug('origen:'   , origen   , '.');
                                debug('pantalla:' , pantalla , '.');
                                
                                if(origen=='F')
                                {
                                    recordTvalosit.set(modelo,formValues[pantalla]);
                                }
                                else if(origen=='O')
                                {
                                    var valorOculto  = valuesFormOculto[pantalla];
                                    var valorValosit = recordTvalosit.get(modelo);
                                    if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                                    {
                                        recordTvalosit.set(modelo,valorOculto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            storeTvalosit.add(recordTvalosit);
            debug('record:',record.data,'tvalosit:',recordTvalosit.data);
        });
        debug('_p30_store:',_p30_store);
        debug('storeTvalosit:',storeTvalosit);
        
        var json =
        {
            smap1 :
            {
                cdunieco     : _p30_smap1.cdunieco
                ,cdramo      : _p30_smap1.cdramo
                ,estado      : 'W'
                ,nmpoliza    : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                ,cdtipsit    : _p30_smap1.cdtipsit
                ,cdpersonCli : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.CLAVECLI
                ,nmorddomCli : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.NMORDDOM
                ,cdideperCli : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.CDIDEPER
                ,feini       : Ext.Date.format(_fieldByName('feini').getValue(),'d/m/Y')
                ,fefin       : Ext.Date.format(_fieldByName('fefin').getValue(),'d/m/Y')
                ,cdagente    : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
                ,notarificar : !Ext.isEmpty(sinTarificar)&&sinTarificar==true? 'si':'no'
                ,tipoflot    : _p30_smap1.tipoflot
                ,modPrim     : sinTarificar == false || sinTarificar== true ? "" : sinTarificar
                ,licencias	 : _p30_smap1.cdtipsit2 ? 'S':'N'
            }
            ,slist1 : []
            ,slist2 : []
            ,slist3 : []
            ,flujo  : !Ext.isEmpty(_p30_flujo) ?_p30_flujo :null
        };
        
        for(var cdtipsitPanel in recordsCdtipsit)
        {
        	try{
        		if(_p30_smap1.turistas=='S'){
		        	if(recordsCdtipsit[cdtipsitPanel].data['parametros.pv_otvalor25'] instanceof Array ){
		        		recordsCdtipsit[cdtipsitPanel].data['parametros.pv_otvalor25']=recordsCdtipsit[cdtipsitPanel].data['parametros.pv_otvalor25'][0]
		        	}
        		}
        	}catch(e){
        		debugError(e)
        	}
            json.slist3.push(recordsCdtipsit[cdtipsitPanel].data);
        }
        
        var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
        try{
	        if(_p30_smap1.turistas=='S'){
	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
	    	}
        }catch(e){
        	debugError(e);
        }
        debug('itemsTatripol:',itemsTatripol);
        for(var i in itemsTatripol)
        {
            var tatri=itemsTatripol[i];
            json.smap1['tvalopol_'+tatri.cdatribu]=tatri.getValue();
        }
        
        _p30_store.each(function(record)
        {
        	try{
        		if(_p30_smap1.turistas=='S'){
        			if(record.data['parametros.pv_otvalor25'] instanceof Array ){
                		record.data['parametros.pv_otvalor25']=record.data['parametros.pv_otvalor25'][0]
                	}
        		}
        	}catch(e){
        		debugError(e)
        	}
        	
            json.slist2.push(record.data);
        });
        
        storeTvalosit.each(function(record)
        {
        	try{
        		if(_p30_smap1.turistas=='S'){
        			if(record.data['parametros.pv_otvalor25'] instanceof Array ){
                		record.data['parametros.pv_otvalor25']=record.data['parametros.pv_otvalor25'][0]
                	}
        		}
        	}catch(e){
        		debugError(e)
        	}
            json.slist1.push(record.data);
        });
        
        //crear record con los valores del formulario y el formulario oculto
        debug('storeTvalosit.getAt(0).data:',storeTvalosit.getAt(0).data);
        var recordTvalositPoliza=new _p30_modelo(storeTvalosit.getAt(0).data);
        debug('recordTvalositPoliza:',recordTvalositPoliza.data);
        recordTvalositPoliza.set('cdtipsit','XPOLX');
        recordTvalositPoliza.set('nmsituac',-1);
        for(var prop in formValues)
        {
            recordTvalositPoliza.set(prop,formValues[prop]);
        }
        for(var att in valuesFormOculto)
        {
            recordTvalositPoliza.set(att,valuesFormOculto[att]);
        }
        debug('recordTvalositPoliza final:',recordTvalositPoliza.data);
        json.slist2.push(recordTvalositPoliza.data);
        //crear record con los valores del formulario y el formulario oculto
        
        debug('>>> json a enviar:',json);

        var panelpri = _fieldById('_p30_panelpri');
        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url       : _p30_urlCotizar
            ,jsonData : json
            ,success  : function(response)
            {
                panelpri.setLoading(false);
                
                _p30_bloquear(true);
                
                json = Ext.decode(response.responseText);
                debug('### cotizar:',json);
                if(json.exito)
                {
                	
                	if(!Ext.isEmpty(json.smap1.msnPantalla))
                    {
                           mensajeError(json.smap1.msnPantalla);
                    }
                	
                	if(!Ext.isEmpty(json.respuesta))
                    {
                		mensajeWarning(json.respuesta);
                    }
                	
                    _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=true;
                    _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue(json.smap1.nmpoliza);
                    _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=false;
                    
                    if(Ext.isEmpty(sinTarificar)||false==sinTarificar)
                    {
                        _grabarEvento('COTIZACION'
                                      ,'COTIZA'
                                      ,_p30_smap1.ntramite
                                      ,_p30_smap1.cdunieco
                                      ,_p30_smap1.cdramo
                                      ,'W'
                                      ,json.smap1.nmpoliza
                                      ,json.smap1.nmpoliza
                                      ,'buscar'
                                      ,_fieldById('_p30_form')
                                      );
                    }
                    
                    var itemsDescuento =
                    [
                        {
                            xtype  : 'displayfield'
                            ,value : '¿Desea usar su descuento de agente?'
                        }
                        ,{
                            xtype  : 'displayfield'
                            ,value : 'Si desea aplicar un DESCUENTO seleccione el porcentaje mayor a 0%'
                        }
                        ,{
                            xtype  : 'displayfield'
                            ,value : 'Si desea aplicar un RECARGO seleccione el porcentaje menor a 0%'
                        }
                    ];
                    
                    var itemAntRefPol =
                        [
                            {
                                xtype  : 'displayfield'
                                ,value : 'Forma de pago de p\u00f3liza a renovar: '+json.smap1.fila
                            }
                        ];
                    
                    <s:if test='%{getImap().get("panel5Items")!=null}'>
                        var itemsaux = [<s:property value="imap.panel5Items" />];
                        for(var ii=0;ii<itemsaux.length;ii++)
                        {
                            debug('itemsaux[ii]:',itemsaux[ii]);
                            itemsDescuento.push(itemsaux[ii]);
                        }
                    </s:if>
                    debug('itemsDescuento:',itemsDescuento);
                    
                    var itemsComision =
                    [
                        {
                            xtype  : 'displayfield'
                            ,value : 'Indique el porcentaje de comisi&oacute;n que desea ceder'
                        }
                    ];
                    
                    var itemsRecargoPF =
                        [
                            {
                                xtype  : 'displayfield'
                                ,value : 'Indique el porcentaje de recargo por pago fraccionado que desea aplicar'
                            },
                            Ext.create('Ext.form.NumberField',{
                            	fieldLabel:'PORCENTAJE RECARGO POR PAGO FRACCIONADO',
                            	label:'PORCENTAJE RECARGO POR PAGO FRACCIONADO',
                            	allowBlank:true,
                            	name:'otvalor.recargoPF',
                            	readOnly:false,
                            	swobligaflot:false,
                            	swobligaemiflot:false,
                            	hidden:false,
                            	style:'margin:5px',
                            	allowDecimals:true,
                            	decimalSeparator:'.',
                            	minValue:0
                            	})
                        ];
                    
                    <s:if test='%{getImap().get("panel6Items")!=null}'>
                        var itemsaux = [<s:property value="imap.panel6Items" />];
                        for(var ii=0;ii<itemsaux.length;ii++)
                        {
                            itemsaux[ii].minValue=0;
                            itemsaux[ii].maxValue=100;
                            itemsComision.push(itemsaux[ii]);
                        }
                    </s:if>
                    debug('itemsComision:',itemsComision);
                    
                    var arr = Ext.ComponentQuery.query('#_p30_gridTarifas');
                    if(arr.length>0)
                    {
                        _fieldById('_p30_formCesion').destroy();
                        panelpri.remove(arr[arr.length-1],true);
                    }
                    
                    var _p30_formDescuento = Ext.create('Ext.form.Panel',
                    {
                        itemId        : '_p30_formDescuento'
                        ,border       : 0
                        ,defaults     : { style : 'margin:5px;' }
                        ,style        : 'margin-left:535px;'
                        ,width        : 450
                        ,windowCesion : Ext.create('Ext.window.Window',
                        {
                            title        : 'CESI&Oacute;N DE COMISI&Oacute;N'
                            ,autoScroll  : true
                            ,closeAction : 'hide'
                            ,modal       : true
                            ,items       :
                            [
                                Ext.create('Ext.form.Panel',
                                {
                                    itemId       : '_p30_formCesion'
                                    ,border      : 0
                                    ,defaults    : { style : 'margin:5px;' }
                                    ,items       : itemsComision
                                    ,buttonAlign : 'center'
                                    ,buttons     :
                                    [
                                        {
                                            itemId   : '_p30_botonAplicarCesion'
                                            ,text    : 'Aplicar'
                                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                            ,handler : function(me)
                                            {
                                                if(me.up('form').getForm().isValid())
                                                {
                                                    me.up('window').hide();
                                                    _p30_cotizar(true);
                                                }
                                                else
                                                {
                                                    mensajeWarning('Favor de verificar los datos');
                                                }
                                            }
                                        }
                                    ]
                                })
                            ]
                        })
                        ,items       :
                        [
                            {
                                xtype  : 'fieldset'
                                ,itemId:"descuentoAgente"
                                ,title : '<span style="font:bold 14px Calibri;">DESCUENTO DE AGENTE</span>'
                                ,items : itemsDescuento
                            }
                            ,{
                                xtype  : 'fieldset'
                                ,title : '<span style="font:bold 14px Calibri;">Datos de Renovacion</span>'
                                ,items : itemAntRefPol
                                ,hidden:  !Ext.isEmpty(_p30_flujo) ? (_p30_flujo.cdflujomc != 240 && _p30_flujo.cdtipflu != 103) : true
                            }
                        ]
                        ,buttonAlign : 'right'
                        ,buttons     :
                        [
                            {
                                itemId   : '_p30_botonAplicarDescuento'
                                ,text    : 'Aplicar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                ,disabled:_p30_descuento
                                ,handler : function(me)
                                {
                                    if(me.up('form').getForm().isValid())
                                    {
                                        var modPrim = false;
                                        if(_p30_smap1.tipoflot == TipoFlotilla.Flotilla)
                                        {
                                          modPrim = Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p30_formDescuento'))[0].lastValue;
                                        }    
                                        _p30_cotizar(modPrim);
                                    }
                                    else
                                    {
                                        mensajeWarning('Favor de verificar los datos');
                                    }
                                    _p30_descuento=true;
                                    
                                }
                                ,listeners: {
                                	afterrender:function(me){
                                		try{
                                		if(_fieldById("descuentoAgente",null,true).down("[xtype=numberfield]").getValue()!=0 && _fieldById("descuentoAgente",null,true).down("[xtype=numberfield]").getValue()!=null){
                                			me.setDisabled(true);
                                		}
                                		}catch(e){
                                			debugError(e)
                                		}
                                	}
                                }
                            }
                        ]
                    });
                    var _p30_PanelRecargoPF = Ext.create('Ext.form.Panel',
                            {
                                itemId        : '_p30_PanelRecargoPF'
                                ,border       : 0
                                ,defaults     : { style : 'margin:5px;' }
                                ,style        : 'margin-left:535px;'
                                ,width        : 450
                                ,windowCesion : Ext.create('Ext.window.Window'
                                ,{
                                    title        : 'RECARGO POR PAGO FRACCIONADO'
                                    ,autoScroll  : true
                                    ,closeAction : 'hide'
                                    ,modal       : true
                                    ,items       :
                                    [   
                                        Ext.create('Ext.form.Panel',
                                        {
                                            itemId       : '_p30_formRecargoPF'
                                            ,border      : 0
                                            ,defaults    : { style : 'margin:5px;' }
                                            ,items       : itemsRecargoPF
                                            ,buttonAlign : 'center'
                                            ,buttons     :
                                            [
                                                {
                                                    itemId   : '_p30_botonAplicarRecargoPF'
                                                    ,text    : 'Aplicar'
                                                    ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                                    ,handler : function(me)
                                                    {
                                                        if(me.up('form').getForm().isValid())
                                                        {
                                                        	me.up('window').hide();
                                                        }
                                                        else
                                                        {
                                                            mensajeWarning('Favor de verificar los datos');
                                                        }
                                                    }
                                                }
                                            ]
                                        })
                                    ]
                                })
                            });
                    
                    _p30_formDescuento.loadRecord(new _p30_modelo(form.formOculto.getValues()));
                    _fieldById('_p30_formCesion').loadRecord(new _p30_modelo(form.formOculto.getValues()));
                    
                    //bloquear descuento
                    var arrDesc = Ext.ComponentQuery.query('[fieldLabel]',_p30_formDescuento);
                    var disabledDesc = false;
                    for(var i=0;i<arrDesc.length;i++)
                    {
                        if(arrDesc[i].getValue()-0!=0)
                        {
                            arrDesc[i].setReadOnly(true);
                            disabledDesc = true;
                        }
                    }
                    
                    if (disabledDesc === true && true === _p30_flujoAux.multiDesc) {
                        disabledDesc = false;
                    }
                    
                    if(_p30_smap1.tipoflot != TipoFlotilla.Flotilla)
                    {
                      _fieldById('_p30_botonAplicarDescuento').setDisabled(disabledDesc);
                    }
                    
                    //bloquear comision
                    var arrComi      = Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p30_formCesion'));
                    var disabledComi = false;
                    for(var i=0;i<arrComi.length;i++)
                    {
                        if(arrComi[i].getValue()-0!=0)
                        {
                            arrComi[i].setReadOnly(true);
                            disabledComi = true;
                        }
                    }
                    
                    if (disabledComi === true && true === _p30_flujoAux.multiDesc) {
                        disabledComi = false;
                    }
                    
                    _fieldById('_p30_botonAplicarCesion').setDisabled(disabledComi);
                    var formasPago=[]
                    try{
                    	if(_p30_smap1.turistas=='S'){
	                    	json.slist1.forEach(function(it){
	                    		if(!FormaPago.esDxN(it.CDPERPAG))
	                    			formasPago.push(it);
	                    	});
	                    	
	                    	formasPago=turistasFormaPago(_fieldByName('feini').getValue()
	                    								,_fieldByName('fefin').getValue()
	                    								,formasPago)
                    	}else{
                    		formasPago=json.slist1;
                    	}
                    	
                    }catch(e){
                    	debugError(e)
                    }
                    var gridTarifas=Ext.create('Ext.panel.Panel',
                    {
                        itemId : '_p30_gridTarifas'
                        ,items :
                        [
                            Ext.create('Ext.grid.Panel',
                            {
                                title             : 'Resultados'
                                ,border           : 0
                                ,store            : Ext.create('Ext.data.Store',
                                {
                                    model : '_p30_modeloTarifa'
                                    ,data : formasPago
                                })
                                ,columns          :
                                [
                                    {
                                        text       : 'FORMA DE PAGO'
                                        ,dataIndex : 'DSPERPAG'
                                        ,flex      : 1
                                    }
                                    ,{
                                        text       : 'PRIMA'
                                        ,dataIndex : 'PRIMA'
                                        ,renderer  : Ext.util.Format.usMoney
                                        ,flex      : 1
                                    }
                                ]
                                ,selType          : 'cellmodel'
                                ,minHeight        : 100
                                ,enableColumnMove : false
                                ,listeners        :
                                {
                                    select       : _p30_tarifaSelect
                                    ,afterrender : function(me)
                                    {
                                        if(!Ext.isEmpty(json.smap1.columna) && !Ext.isEmpty(json.smap1.fila))
                                        {
                                            var gridTarifas = _fieldById('_p30_gridTarifas').down('grid');
                                            var sm = gridTarifas.getSelectionModel();
                                            try
                                            {
                                                var columna=1 ,fila=999; 
                                                
                                                for(var IteGriTar=0;IteGriTar<17;IteGriTar++)
                                                {
                                                    sm.select({row:IteGriTar,column:columna});
                                                    var texto = (sm.getSelection({row:IteGriTar,column:columna})[0].data.DSPERPAG).toLowerCase()
                                                    if(json.smap1.fila.toLowerCase() === texto)
                                                    {
                                                          fila = IteGriTar;
                                                          IteGriTar = 18;
                                                    }
                                                }
                                                
                                                sm.select({row:fila,column:columna});
                                             }catch(e) {
                                               debug("Excede rango fuera de la cuadricula de tarifas");
                                             }
                                        }
                                        
                                        if(!Ext.isEmpty(_p30_flujo)) // && !sinTarificar===true)
                                        {
                                            _p30_actualizarCotizacionTramite(_p30_actualizarSwexiperTramite);
                                        }
                                    }
                                }
                            })
                            ,_p30_formDescuento
                            ,Ext.create('Ext.panel.Panel',
                            {
                                defaults : { style : 'margin:5px;' }
                                ,border  : 0
                                ,tbar    :
                                [
                                    '->'
                                    ,{
                                        itemId    : '_p30_botonDetalles'
                                        ,text     : 'Detalles'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
                                        ,disabled : true
                                        ,handler  : _p30_detalles
                                       ,hidden    : !RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)//(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')==-1)
                                    }
                                    ,{
                                        itemId    : '_p30_botonCoberturas'
                                        ,text     : 'Coberturas'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/table.png'
                                        ,disabled : true
                                        ,handler  : _p30_coberturas
                                    }
                                    ,{
                                        itemId   : '_p30_botonEditar'
                                        ,text    : 'Editar'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
                                        ,handler : _p30_editar
                                    }
                                    ,{
                                        itemId   : '_p30_botonClonar'
                                        ,text    : 'Duplicar'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                                        ,handler : _p30_clonar
                                    }
                                    ,{
                                        itemId   : '_p30_botonNueva'
                                        ,text    : 'Nueva'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                        ,handler : _p30_nueva
                                    }
                                ]
                                ,bbar    :
                                [
                                    '->'
                                    ,{
                                        itemId    : '_p30_botonEnviar'
                                        ,xtype    : 'button'
                                        ,text     : 'Enviar'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/email.png'
                                        ,disabled : true
                                        ,handler  : _p30_enviar
                                        ,hidden	  : _p30_smap1.turistas=='S'
                                    }
                                    ,{
                                        itemId    : '_p30_botonImprimir'
                                        ,xtype    : 'button'
                                        ,text     : 'Imprimir'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
                                        ,disabled : true
                                        ,handler  : _p30_imprimir
                                        ,hidden	  : _p30_smap1.turistas=='S'
                                    }
                                    ,{
                                        itemId   : '_p30_botonCesion'
                                        ,xtype   : 'button'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/page_white_star.png'
                                        ,text    : 'Cesi&oacute;n de comisi&oacute;n'
                                        ,handler : _p30_cesionClic
                                        ,hidden	  : _p30_smap1.turistas=='S'
                                    }
                                    ,{
                                        itemId   : '_p30_botonRecargoPF'
                                        ,xtype   : 'button'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/page_white_star.png'
                                        ,text    : 'Recargo Pago Fraccionado'
                                        ,tooltip : 'Aplicable solo para Flotillas'
                                        ,disabled: true
                                        ,handler : _p30_aplicaRecargoPF
                                    }
                                    ,{
                                        itemId    : '_p30_botonComprar'
                                        ,xtype    : 'button'
                                        ,text     : 'Confirmar cotizaci&oacute;n'
                                        ,icon     : '${ctx}/resources/fam3icons/icons/book_next.png'
                                        ,disabled : true
                                        ,handler  : _p30_comprar
                                    }, {
                                        itemId  : '_p30_botonOnCotizar',
                                        xtype   : 'button',
                                        text    : 'Enviar',
                                        icon    : '${icons}user_go.png',
                                        hidden  : true,
                                        handler : _p30_botonOnCotizarClic
                                    }
                                ]
                            })
                        ]
                    });
                    
                    panelpri.add(gridTarifas);
                    panelpri.doLayout();
                    
                    // Cuando se turna al cotizar
                    if (typeof _p30_flujoAux.onCotizar === 'number') {
                        if (_p30_flujoAux.onCotizarHide === 'S') {
                            gridTarifas.down('grid').hide();
                        }
                        _fieldById('_p30_botonComprar').hide();
                        _fieldById('_p30_botonOnCotizar').show();
                    }
                    
                    if(_p30_smap1.cdramo+'x'=='5x'&&arrDesc.length>0)
                    {
                        _p30_formDescuento.setLoading(true);
                        Ext.Ajax.request(
                        {
                            url     : _p30_urlRecuperacionSimple
                            ,params :
                            {
                                'smap1.procedimiento' : 'RECUPERAR_DESCUENTO_RECARGO_RAMO_5'
                                ,'smap1.cdtipsit'     : _p30_smap1.cdtipsit
                                ,'smap1.cdagente'     : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
                                ,'smap1.negocio'      : _fieldByLabel('NEGOCIO',_fieldById('_p30_form')).getValue()
                                ,'smap1.tipocot'      : _p30_smap1.tipoflot
                                ,'smap1.cdsisrol'     : _p30_smap1.cdsisrol
                                ,'smap1.cdusuari'     : _p30_smap1.cdusuari
                            }
                            ,success : function(response)
                            {
                                _p30_formDescuento.setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### cargar rango descuento ramo 5:',json);
                                if(json.exito)
                                {
                                    for(var i=0;i<arrDesc.length;i++)
                                    {
                                        arrDesc[i].minValue=100*(json.smap1.min-0);
                                        arrDesc[i].maxValue=100*(json.smap1.max-0);
                                        arrDesc[i].isValid();
                                        debug('min:',arrDesc[i].minValue);
                                        debug('max:',arrDesc[i].maxValue);
                                        arrDesc[i].setReadOnly(false);
                                    }
                                }
                                else
                                {
                                    for(var i=0;i<arrDesc.length;i++)
                                    {
                                        arrDesc[i].minValue=0;
                                        arrDesc[i].maxValue=0;
                                        arrDesc[i].setValue(0);
                                        arrDesc[i].isValid();
                                        arrDesc[i].setReadOnly(true);
                                    }
                                    mensajeError(json.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                _p30_formDescuento.setLoading(false);
                                for(var i=0;i<arrDesc.length;i++)
                                {
                                    arrDesc[i].minValue=0;
                                    arrDesc[i].maxValue=0;
                                    arrDesc[i].setValue(0);
                                    arrDesc[i].isValid();
                                    arrDesc[i].setReadOnly(true);
                                }
                                errorComunicacion();
                            }
                        });
                    }
                    
                    try {
                       gridTarifas.down('button[disabled=false]').focus(false, 1000);
                    } catch(e) {
                        debug(e);
                    }
                }
                else
                {
                    _p30_bloquear(false);
                    mensajeError(json.respuesta);
                }
            }
            ,failure  : function()
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    
    debug('<_p30_cotizar');
}

function _p30_nmpolizaChange(me)
{
    var sem = me.semaforo;
    if(Ext.isEmpty(sem)||sem==false)
    {
        me.sucio = true;
    }
    else
    {
        me.sucio = false;
    }
}

function _p30_limpiar()
{
    debug('>_p30_limpiar');
    
    _p30_store.removeAll();
    
    _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=true;
    _fieldById('_p30_form').getForm().reset();
    _fieldById('_p30_form').formOculto.getForm().reset();
    _fieldByName('nmpoliza',_fieldById('_p30_form')).semaforo=false;
    
    for(var i in _p30_paneles)
    {
        _p30_paneles[i].valores=_p30_paneles[i].valoresBkp;
    }
    
    if(_p30_smap1.cdramo+'x'=='5x')
    {
        _p30_calculaVigencia();
        _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_form')).setValue('S');    
        
        if(_p30_smap1.cdsisrol=='EJECUTIVOCUENTA')
        {
            var agente = _fieldByLabel('AGENTE',_fieldById('_p30_form'));
            agente.setValue(_p30_smap1.cdagente);
            agente.setReadOnly(true);
            _p30_ramo5AgenteSelect(agente,_p30_smap1.cdagente);
        }
    }
    
    _p30_inicializarTatripol();
    
    _p30_recuperarClienteTramite();
    
    debug('<_p30_limpiar');
}

function _p30_cargarClic()
{
    debug('>_p30_cargarClic');
    
    var panelpri = _fieldById('_p30_panelpri');
    var ck       = '';
    try
    {
        ck='Validando folio';
        var nmpoliza = _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue();
        checkEmpty(nmpoliza,'Introduce un n&uacute;mero v&aacute;lido');
        
        ck='Invocando servicio de recuperacion de cotizacion';
        panelpri.setLoading(true);
        Ext.Ajax.request(
        {
            url     : _p30_urlCargar
            ,params :
            {
                'smap1.cdramo'      : _p30_smap1.cdramo
                ,'smap1.nmpoliza'   : nmpoliza
                ,'smap1.ntramiteIn' : _p30_smap1.ntramite
            }
            ,success : function(response)
            {
                panelpri.setLoading(false);
                var ck = '';
                try
                {
                    ck = 'Descodificando respuesta';
                    var json = Ext.decode(response.responseText);
                    debug('### cargar:',json);
                    checkBool(json.exito,json.respuesta);
                    
                    llenandoCampos(json, nmpoliza, false);
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                panelpri.setLoading(false);
                errorComunicacion();
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,panelpri);
    }
    
    debug('<_p30_cargarClic');
}

function _p30_tarifaSelect(selModel, record, row, column, eOpts)
{
    var gridTarifas = _fieldById('_p30_gridTarifas').down('grid');
    debug('column:',column);
    if(column>0)
    {
        column = (column * 2) -1;
    }
    debug('( column * 2 )-1:',column);
    var columnName=gridTarifas.columns[column].dataIndex;
    debug('record',record);
    debug('columnName',columnName);
    if(columnName=='DSPERPAG')
    {
        _fieldById('_p30_botonImprimir').setDisabled(true);
        _fieldById('_p30_botonEnviar').setDisabled(true);
        _fieldById('_p30_botonDetalles').setDisabled(true);
        _fieldById('_p30_botonCoberturas').setDisabled(true);
        _fieldById('_p30_botonComprar').setDisabled(true);
        _fieldById('_p30_botonRecargoPF').setDisabled(true);
    }
    else
    {
        // M N P R I M A X
        //0 1 2 3 4 5 6 7
        _p30_selectedTarifa = record;
        debug('_p30_selectedTarifa:',_p30_selectedTarifa);
        
        _fieldById('_p30_botonImprimir').setDisabled(false);
        _fieldById('_p30_botonEnviar').setDisabled(false);
        _fieldById('_p30_botonDetalles').setDisabled(false);
        _fieldById('_p30_botonCoberturas').setDisabled(false);
        _fieldById('_p30_botonComprar').setDisabled(false);
        _fieldById('_p30_botonRecargoPF').setDisabled(_p30_smap1.tipoflot=='F'?false:true);
    }
}

function _p30_cesionClic()
{
    debug('>_p30_cesionClic');
    _fieldById('_p30_formDescuento').windowCesion.show();
    centrarVentanaInterna(_fieldById('_p30_formDescuento').windowCesion);
    debug('<_p30_cesionClic');
}

function _p30_aplicaRecargoPF()
{
    debug('>_p30_PanelRecargoPFClic');
    _fieldById('_p30_PanelRecargoPF').windowCesion.show();
    centrarVentanaInterna(_fieldById('_p30_PanelRecargoPF').windowCesion);
    debug('<_p30_PanelRecargoPFClic');
}

function _p30_editar()
{
    debug('>_p30_editar');
    var panelPri    = _fieldById('_p30_panelpri');
    var gridTarifas = _fieldById('_p30_gridTarifas');
    
    _fieldById('_p30_formCesion').destroy();
    panelPri.remove(gridTarifas,true);
    panelPri.doLayout();
    
    _p30_bloquear(false);
    debug('<_p30_editar');
}

function _p30_bloquear(b)
{
    debug('>_p30_bloquear:',b);
    var comps=Ext.ComponentQuery.query('[fieldLabel]',_fieldById('_p30_form'));
    
    for(var i=0;i<comps.length;i++)
    {
        comps[i].setReadOnly(b);
    }
    
    _fieldById('_p30_botonera').setDisabled(b);
    _fieldById('_p30_botonCargar').setDisabled(b);
    var toolbars = Ext.ComponentQuery.query('toolbar',_fieldById('_p30_grid'));
    for(var i in toolbars)
    {
        toolbars[i].setDisabled(b);
    }
    
    if(b)
    {
    }
    else
    {
        try {
            _fieldByName('nmpoliza',_fieldById('_p30_form')).focus();
        } catch(e) {
            debug(e);
        }
    }
    
    if(_p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol=='EJECUTIVOCUENTA')
    {
        var agente = _fieldByLabel('AGENTE',_fieldById('_p30_form'));
        agente.setValue(_p30_smap1.cdagente);
        agente.setReadOnly(true);
        _p30_ramo5AgenteSelect(agente,_p30_smap1.cdagente);
    }
    
    _p30_recuperarClienteTramite();
    
    debug('<_p30_bloquear');
}

function _p30_clonar()
{
    debug('>_p30_clonar');
    _fieldById('_p30_form').formOculto.getForm().reset();
    
    var ck = 'Borrando descuentos';
    try {
        for (var cdtipsit in _p30_paneles) {
            var panel  = _p30_paneles[cdtipsit];
            var items = Ext.ComponentQuery.query('[fieldLabel*=DESCUENTO / RECARGO]', panel);
            for (var j = 0; j < items.length; j++) {
                var item = items[j];
                var name = item.name;
                if (!Ext.isEmpty(panel.valores)) {
                    var descAnte = panel.valores[name];
                    panel.valores[name] = '';
                    debug('El descuento para el panel', cdtipsit, 'en el campo', name, 'se cambio de', descAnte, 'a', panel.valores[name], '.');
                }
            }
        }
        _p30_store.each(function(record) {
            var cdtipsit = record.get('cdtipsit'),
                nmsituac = record.get('nmsituac');
            var items = Ext.ComponentQuery.query('[fieldLabel*=DESCUENTO / RECARGO]', _p30_tatrisitFullForms[cdtipsit]);
            for (var i = 0; i < items.length; i++) {
                var name = items[i].name;
                var descAnte = record.get(name);
                record.set(name, '');
                debug('El descuento para el inciso', nmsituac, cdtipsit, name, 'se cambio de', descAnte, 'a', record.get(name), '.');
            }
        });
    } catch (e) {
        manejaException(e, ck);
    }
    
    _p30_editar();
    _fieldByName('nmpoliza',_fieldById('_p30_form')).setValue('');
    debug('<_p30_clonar');
}

function _p30_nueva()
{
    debug('>_p30_nueva');
    _p30_editar();
    _p30_limpiar();
    debug('<_p30_nueva');
}

function _p30_detalles()
{
    debug('>_p30_detalles');
    var panelpri = _fieldById('_p30_panelpri');
    panelpri.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p30_urlRecuperacionSimpleLista
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_DETALLES_COTIZACION_AUTOS_FLOTILLA'
            ,'smap1.cdunieco'     : _p30_smap1.cdunieco
            ,'smap1.cdramo'       : _p30_smap1.cdramo
            ,'smap1.estado'       : 'W'
            ,'smap1.nmpoliza'     : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
            ,'smap1.cdperpag'     : _p30_selectedTarifa.get('CDPERPAG')
        }
        ,success : function(response)
        {
            panelpri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### detalles:',json);
            if(json.exito)
            {
            	//Editor de incisos de arbol en detalles de cotizacion Colectivos
            	var totalCoberturas = 0;
            	var totalGlobales = 0;
            	
            	var nuevoElementoCoberturas;
                var nuevoElementoGlobales;
                var nuevoElementoTotal;

                var ordenRecargos = 700;
                var ordenDerechos = 800;
                var ordenIva = 900;
            	
               for(var i in json.slist1)
               	{	 
            	   var totalIncisos = json.slist1.length;
            	   var j= Number(i);
            	   var cobertura = json.slist1[j].COBERTURA;
            	   try{
            		   if(_p30_smap1.turistas == 'S' ){
            			   if(!json.slist1[j+1]){
            				   break;
            			   }
            		   }
            	   }catch(e){
            		   debugError(e)
            	   }
            	   debug('### Cobertura: ',j,'/',totalIncisos,' - ',cobertura);
            	   
            	    	 if(cobertura.indexOf('Recargo')!=-1)
                			  {
            	    		   totalGlobales += Number(json.slist1[j].PRIMA);
                			   ordenRecargos = Number(ordenRecargos) - 5 ;
                			   json.slist1[j].ORDEN = ordenRecargos+"";
                			  }
            	    	 else if(cobertura.indexOf('Derechos')!=-1)
                			  {
            	    		   totalGlobales += Number(json.slist1[j].PRIMA);
                			   ordenDerechos = Number(ordenDerechos) - 5 ;
                			   json.slist1[j].ORDEN = ordenDerechos+"";	  
                			  }
            	    	 else if(cobertura.indexOf('Iva')!=-1)
                			  {
            	    		  totalGlobales += Number(json.slist1[j].PRIMA);
                			   ordenIva = Number(ordenIva) - 5 ;
                			   json.slist1[j].ORDEN = ordenIva+"";
                			  }
                		
                	 else
                	 {totalCoberturas += Number(json.slist1[j].PRIMA);}
               	
                     var sigEle = j+1;
                     var tituloAct =  json.slist1[j].TITULO;
                     var tituloSig = json.slist1[sigEle].TITULO;
                     
                     debug('Titulo Actual: ',tituloAct,' Siguiente ',tituloSig,' Sig elemento ',sigEle);
                     
                     if( sigEle<=totalIncisos && (tituloAct != tituloSig))
                     {
			               	nuevoElementoCoberturas = {
			                        COBERTURA  :  'Total de Coberturas'
			                       ,PRIMA      :  totalCoberturas+""
			                       ,ORDEN      :  _p30_smap1.turistas=='S'?'580':'500'
			                       ,TITULO     :  json.slist1[j].TITULO
			                       ,CDRAMO     :  json.slist1[j].CDRAMO
			                       ,CDUNIECO   :  json.slist1[j].CDUNIECO
			                       ,ESTADO     :  json.slist1[j].ESTADO
			                       ,NMPOLIZA   :  json.slist1[j].NMPOLIZA
			                       ,NMSITUAC   :  json.slist1[j].NMSITUAC
			                }
			                nuevoElementoGlobales = {
			                        COBERTURA  : 'Total de Conceptos Globales'
			                       ,PRIMA      :  totalGlobales+""
			                       ,ORDEN      :  _p30_smap1.turistas=='S'?'998':'995'//999
			                       ,TITULO     :  json.slist1[j].TITULO
			                       ,CDRAMO     :  json.slist1[j].CDRAMO
			                       ,CDUNIECO   :  json.slist1[j].CDUNIECO
			                       ,ESTADO     :  json.slist1[j].ESTADO
			                       ,NMPOLIZA   :  json.slist1[j].NMPOLIZA
			                       ,NMSITUAC   :  json.slist1[j].NMSITUAC
			                }
			               	
			                nuevoElementoTotal = {
			                        COBERTURA: 'Total por Inciso'
			                       ,PRIMA      :  (totalGlobales+totalCoberturas)+""
			                       ,ORDEN      :  '999'
			                       ,TITULO     :  json.slist1[j].TITULO
			                       ,CDRAMO     :  json.slist1[j].CDRAMO
			                       ,CDUNIECO   :  json.slist1[j].CDUNIECO
			                       ,ESTADO     :  json.slist1[j].ESTADO
			                       ,NMPOLIZA   :  json.slist1[j].NMPOLIZA
			                       ,NMSITUAC   :  json.slist1[j].NMSITUAC
			                       }
			               	
			               	json.slist1.push(nuevoElementoCoberturas);
			                json.slist1.push(nuevoElementoGlobales);
			                json.slist1.push(nuevoElementoTotal);
			               	
			               	totalCoberturas = 0;
			                totalGlobales = 0;
			                ordenRecargos = 700;
			                ordenDerechos = 800;
			                ordenIva = 900;
                     }
	            }
               debug('### antes:',json.slist1);
               if(_p30_smap1.turistas=='S'){
	               json.slist1.forEach(function(it,idx,arr){
	            	   if(Number(it.PRIMA)==0){
	            		   arr.splice(idx, 1)
	            	   }
	               })
               }
               debug('### despues:',json.slist1);
               debug('### detalles:',json);
                              
               centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    title       : 'Detalles de cotizaci&oacute;n'
                    ,width      : 600
                    ,maxHeight  : 600
                    ,autoScroll : true
                    ,modal      : true  
                    ,items      :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            store    : Ext.create('Ext.data.Store',
                            {
                                model       : '_p30_modeloDetalleCotizacion'
                               // ,groupField : 'TITULO'
                                //,groupDir	: 'DESC'
                              /*  ,sorters    :
                                [
                                    {
                                        sorterFn : function(o1,o2)
                                        {
                                            debug('sorting:',o1,o2);
                                            if (Number(o1.get('ORDEN')) == Number(o2.get('ORDEN')))
                                            {
                                                return 0;
                                            }
                                            return Number(o1.get('ORDEN')) < Number(o2.get('ORDEN')) ? -1 : 1;
                                        }
                                    }
                                ]*/
                                ,groupers: [{
                                    property: 'TITULO',
                                    sorterFn: function(o1, o2){
                                    	debug(o1," # ",o2)
                                    	try{
                                        	
                                        		var a=o1.get('TITULO'),
                                        	 	b=o2.get('TITULO')
                                        	 	
                                            	a=Number(a.split("_")[0]);
                                            	b=Number(b.split("_")[0]);
                                            	if(a==0)
                                            		a=99999;
                                            	if(b==0)
                                            		b=99999;
                                            	
                                            	if(a>b){
                                            		return 1;
                                            	}else if(a<b)
                                            		return -1
                                            		
                                            	
                                        	
                                        }catch(e){
                                        	debugError(e)
                                        }
                                        debug('sorting:',o1,o2);
                                        if (Number(o1.get('ORDEN')) == Number(o2.get('ORDEN')))
                                        {
                                            return 0;
                                        }
                                        return Number(o1.get('ORDEN')) < Number(o2.get('ORDEN')) ? -1 : 1;
                                    }
                                }]
                                ,proxy      :
                                {
                                    type    : 'memory'
                                    ,reader : 'json'
                                }
                                ,data : json.slist1
                            })
                            ,columns :
                            [
                                {
                                    header           : 'Nombre de la cobertura'
                                    ,dataIndex       : 'COBERTURA'
                                    ,width           : 400
                                    ,summaryType     : 'count'
                                    ,summaryRenderer : function(value)
                                    {
                                        return Ext.String.format('Total de {0} cobertura{1}',value,value !== 1 ? 's': '');
                                    }
                                }
                                ,{
                                    header       : 'Importe por cobertura'
                                    ,dataIndex   : 'PRIMA'
                                    ,width       : 150
                                    ,renderer    : Ext.util.Format.usMoney
                                    ,align       : 'right'
                                    ,summaryType : 'sum'
                                } 
                            ]
                            ,features :
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
                                 ,ftype          : 'grouping'//'groupingsummary'
                                 ,startCollapsed : false
                                }
                            ]
                        })
                        ,Ext.create('Ext.toolbar.Toolbar',
                        {
                            buttonAlign : 'right'
                            ,items      :
                            [
                                '->'
                                ,Ext.create('Ext.form.Label',
                                {
                                    style          : 'color:white;'
                                    ,initComponent : function()
                                    {
                                        var sum = 0;
                                        for ( var i = 0; i < json.slist1.length; i++)
                                        {
                                            if(json.slist1[i].COBERTURA == 'Total por Inciso' && _p30_smap1.turistas!='S'  )
                                            	{
                                            	   sum += parseFloat(json.slist1[i].PRIMA);
                                                }
                                            else if(json.slist1[i].COBERTURA =='Total de Coberturas' && _p30_smap1.turistas=='S'){
                                            	sum += parseFloat(json.slist1[i].PRIMA);
                                            }
                                        }
                                        try{
                                        	if(_p30_smap1.turistas=='S')
	                                        	json.slist1.forEach(function(it){
	                                        		
	                                        		if(Number(it.TITULO.split("_")[0])==0 && it.COBERTURA=='Total de Conceptos Globales'){
	                                        			sum += parseFloat(it.PRIMA);
	                                        		}
	                                        	})
                                        }catch(e){
                                        	debugError(e)
                                        }
                                        this.setText('Total: '+ Ext.util.Format.usMoney(sum));
                                        this.callParent();
                                    }
                                })
                            ]
                        })
                    ]
                }).show());
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p30_detalles');
}

function _p30_coberturas()
{
    debug('>_p30_coberturas');
    var panelpri = _fieldById('_p30_panelpri');
    panelpri.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p30_urlRecuperacionSimpleLista
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_DETALLES_COBERTURAS_COTIZACION_AUTOS_FLOTILLA'
            ,'smap1.cdunieco'     : _p30_smap1.cdunieco
            ,'smap1.cdramo'       : _p30_smap1.cdramo
            ,'smap1.estado'       : 'W'
            ,'smap1.nmpoliza'     : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
            ,'smap1.cdperpag'     : _p30_selectedTarifa.get('CDPERPAG')
        }
        ,success : function(response)
        {
            panelpri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### detalles:',json);
            if(json.exito)
            {
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    title       : 'Coberturas'
                    ,width      : 600
                    ,maxHeight  : 600
                    ,autoScroll : true
                    ,modal      : true
                    ,items      :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            store    : Ext.create('Ext.data.Store',
                            {
                                model       : '_p30_modeloCoberturaCotizacion'
                                ,groupField : 'TITULO'
                                ,sorters    :
                                [
                                    {
                                        sorterFn : function(o1,o2)
                                        {
                                            debug('sorting:',o1,o2);
                                            if (Number(o1.get('ORDEN')) == Number(o2.get('ORDEN')))
                                            {
                                                return 0;
                                            }
                                            return Number(o1.get('ORDEN')) < Number(o2.get('ORDEN')) ? -1 : 1;
                                        }
                                    }
                                ]
                                ,proxy      :
                                {
                                    type    : 'memory'
                                    ,reader : 'json'
                                }
                                ,data : json.slist1
                            })
                            ,columns :
                            [
                                {
                                    header           : 'Nombre de la cobertura'
                                    ,dataIndex       : 'COBERTURA'
                                    ,width           : 400
                                }
                                ,{
                                    header       : 'Suma asegurada'
                                    ,dataIndex   : 'SUMASEG'
                                    ,width       : 150
                                }
                            ]
                            ,features :
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
                        })
                    ]
                }).show());
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p30_coberturas');
}

function _p30_comprar()
{
    debug('_p30_comprar');
    var panelPri = _fieldById('_p30_panelpri');
    panelPri.setLoading(true);
    var nombreTitular = '';
    
    var paramsComprar =
    {
        comprarNmpoliza        : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
        ,comprarCdplan         : '*'
        ,comprarCdperpag       : _p30_selectedTarifa.get('CDPERPAG')
        ,comprarCdramo         : _p30_smap1.cdramo
        ,comprarCdciaaguradora : '20'
        ,comprarCdunieco       : Ext.isEmpty(_p30_smap1.cdunieco) ? _p30_flujo.cdunieco : _p30_smap1.cdunieco
        ,cdtipsit              : _p30_smap1.cdtipsit
        ,'smap1.fechaInicio'   : Ext.Date.format(_fieldByName('feini').getValue(),'d/m/Y')
        ,'smap1.fechaFin'      : Ext.Date.format(_fieldByName('fefin').getValue(),'d/m/Y')
        ,'smap1.ntramite'      : _p30_smap1.ntramite
        ,'smap1.cdpersonCli'   : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.CLAVECLI
        ,'smap1.nmorddomCli'   : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.NMORDDOM
        ,'smap1.cdideperCli'   : Ext.isEmpty(_p30_recordClienteRecuperado) ? '' : _p30_recordClienteRecuperado.raw.CDIDEPER
        ,'smap1.cdagenteExt'   : _p30_smap1.cdramo+'x'=='5x' ? _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue() : ''
        ,'smap1.flotilla'      : 'si'
        ,'smap1.tipoflot'      : _p30_smap1.tipoflot
        ,'smap1.recargoPF'     : Ext.isEmpty(_fieldByName('otvalor.recargoPF',_fieldById('_p30_formRecargoPF')).getValue())? null:_fieldByName('otvalor.recargoPF',_fieldById('_p30_formRecargoPF')).getValue()
    };
    
    if(!Ext.isEmpty(_p30_flujo))
    {
        paramsComprar['flujo.cdtipflu']  = _p30_flujo.cdtipflu;
        paramsComprar['flujo.cdflujomc'] = _p30_flujo.cdflujomc;
        paramsComprar['flujo.tipoent']   = _p30_flujo.tipoent;
        paramsComprar['flujo.claveent']  = _p30_flujo.claveent;
        paramsComprar['flujo.webid']     = _p30_flujo.webid;
        paramsComprar['flujo.ntramite']  = _p30_flujo.ntramite;
        paramsComprar['flujo.status']    = _p30_flujo.status;
        paramsComprar['flujo.cdunieco']  = _p30_flujo.cdunieco;
        paramsComprar['flujo.cdramo']    = _p30_flujo.cdramo;
        paramsComprar['flujo.estado']    = _p30_flujo.estado;
        paramsComprar['flujo.nmpoliza']  = _p30_flujo.nmpoliza;
        paramsComprar['flujo.nmsituac']  = _p30_flujo.nmsituac;
        paramsComprar['flujo.nmsuplem']  = _p30_flujo.nmsuplem;
    }
    debug('### Comprar:',paramsComprar);

    Ext.Ajax.request(
    {
        url      : _p30_urlComprar
        ,params  : paramsComprar
        ,success : function(response,opts)
        {
            panelPri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### Comprar:',json);
            if (json.exito)
            {
                if(Ext.isEmpty(_p30_flujo)
                    ||Ext.isEmpty(_p30_flujo.aux)
                    ||_p30_flujo.aux.indexOf('onComprar')==-1
                    ) //si no hay flujo, o no hay auxiliar en flujo, o el auxiliar no contiene la palabra onComprar
                {
	                centrarVentanaInterna(Ext.Msg.show(
	               {
	                   title    : 'Tr&aacute;mite generado'
	                   ,msg     : 'La cotizaci&oacute;n '+_fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()+' se guard&oacute; y no podr&aacute; ser modificada posteriormente'
	                   ,buttons : Ext.Msg.OK
	                   ,fn      : function()
	                   {
	                       var swExiper = (!Ext.isEmpty(_p30_recordClienteRecuperado) && Ext.isEmpty(_p30_recordClienteRecuperado.raw.CLAVECLI) && !Ext.isEmpty(_p30_recordClienteRecuperado.raw.CDIDEPER))? 'N' : 'S' ;
	                       
	                       var paramsDatCom =
	                       {
	                           'smap1.cdunieco'  : _p30_smap1.cdunieco
	                           ,'smap1.cdramo'   : _p30_smap1.cdramo
	                           ,'smap1.cdtipsit' : _p30_smap1.cdtipsit
	                           ,'smap1.estado'   : 'W'
	                           ,'smap1.nmpoliza' : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
	                           ,'smap1.ntramite' : json.smap1.ntramite
	                           ,'smap1.swexiper' : swExiper
	                           ,'smap1.tipoflot' : _p30_smap1.tipoflot
	                           ,'smap1.cdtipsit2': _p30_smap1.cdtipsit2
	                       };
	                       
	                       if(!Ext.isEmpty(_p30_flujo))
	                       {
	                           paramsDatCom['flujo.cdtipflu']  = _p30_flujo.cdtipflu;
	                           paramsDatCom['flujo.cdflujomc'] = _p30_flujo.cdflujomc;
	                           paramsDatCom['flujo.tipoent']   = _p30_flujo.tipoent;  //ACTUAL QUE SE RECUPERARA
	                           paramsDatCom['flujo.claveent']  = _p30_flujo.claveent; //ACTUAL QUE SE RECUPERARA
	                           paramsDatCom['flujo.webid']     = _p30_flujo.webid;    //ACTUAL QUE SE RECUPERARA
	                           paramsDatCom['flujo.ntramite']  = _p30_flujo.ntramite;
	                           paramsDatCom['flujo.status']    = _p30_flujo.status;
	                           paramsDatCom['flujo.cdunieco']  = _p30_flujo.cdunieco;
	                           paramsDatCom['flujo.cdramo']    = _p30_flujo.cdramo;
	                           paramsDatCom['flujo.estado']    = _p30_flujo.estado;
	                           paramsDatCom['flujo.nmpoliza']  = _p30_flujo.nmpoliza;
	                           paramsDatCom['flujo.nmsituac']  = _p30_flujo.nmsituac;
	                           paramsDatCom['flujo.nmsuplem']  = _p30_flujo.nmsuplem;
	                           paramsDatCom['flujo.aux']       = 'RECUPERAR';
	                       }
	                       
	                       Ext.create('Ext.form.Panel').submit(
	                       {
	                           url             : _p30_urlDatosComplementarios
	                           ,standardSubmit : true
	                           ,params         : paramsDatCom
	                       });
	                   }
	                }));
	            }   
	            else //flujo tiene la palabra onComprar
                {
                    //si el flujo tiene este comodin ejecutaremos un turnado con el status indicado
                    var ck = 'Turnando tr\u00e1mite';
                    try
                    {
                        var status = _p30_flujo.aux.split(',')[1].split(':')[1];//primer split= tipoflot:P onComprar:160, segundo= onComprar 160
                        debug('status para turnar onComprar:',status,'.');
                        
                        _mask(ck);
                        Ext.Ajax.request(
                        {
                            url      : _GLOBAL_COMP_URL_TURNAR
                            ,params  :
                            {
                                'params.CDTIPFLU'   : _p30_flujo.cdtipflu
                                ,'params.CDFLUJOMC' : _p30_flujo.cdflujomc
                                ,'params.NTRAMITE'  : _p30_flujo.ntramite
                                ,'params.STATUSOLD' : _p30_flujo.status
                                ,'params.STATUSNEW' : status
                                ,'params.COMMENTS'  : 'Tr\u00e1mite cotizado'
                                ,'params.SWAGENTE'  : 'S'
                            }
                            ,success : function(response)
                            {
                                _unmask();
                                var ck = '';
                                try
                                {
                                    var json = Ext.decode(response.responseText);
                                    debug('### turnar:',json);
                                    if(json.success)
                                    {
                                        mensajeCorrecto
                                        (
                                            'Tr\u00e1mite turnado'
                                            //,json.message
                                            ,'El tr\u00e1mite fue turnado para aprobaci\u00f3n del agente/promotor'
                                            ,function()
                                            {
                                                _mask('Redireccionando');
                                                Ext.create('Ext.form.Panel').submit(
                                                {
                                                    url             : _GLOBAL_COMP_URL_MCFLUJO
                                                    ,standardSubmit : true
                                                });
                                            }
                                        );
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
                                errorComunicacion(null,'Error al turnar tr\u00e1mite');
                            }
                        });
                    }
                    catch(e)
                    {
                        manejaException(e,ck);
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
            panelPri.setLoading(false);
            errorComunicacion();
        }
    });
}

function _p30_numerarIncisos(arreglo)
{
    var i=1;
    if(Ext.isEmpty(arreglo))
    {
        _p30_store.each(function(record)
        {
            record.set('nmsituac',i++);
        });
    }
    else
    {
        for(var j in arreglo)
        {
            arreglo[j].set('nmsituac',i++);
        }
    }
}

function _p30_editarAuto()
{
    debug('>_p30_editarAuto');
    var record = _p30_selectedRecord;
    debug('_p30_editarAuto:',record.data);
    if(Ext.isEmpty(record.get('cdtipsit')))
    {
        _p30_pedirCdtipsit();
    }
    else
    {
        for(var i in _p30_situaciones)
        {
            _fieldById('_p30_tatrisitParcialForm'+_p30_situaciones[i]).hide();
        }
        if(!_p30_semaforoBorrar)
        {
            var cdtipsit = record.get('cdtipsit');
            var form     = _fieldById('_p30_tatrisitParcialForm'+cdtipsit);
            form.show();
            var combos=Ext.ComponentQuery.query('combo[heredar]',form);
            debug('combos anidados:',combos);
            for(var i in combos)
            {
                combos[i].forceSelection=false;
            }
            form.loadRecord(record);
            heredarPanel(form,true);
            form.items.items[0].focus();
        }
        else
        {
            _p30_semaforoBorrar=false;
        }
    }
}

function _p30_pedirCdtipsit()
{
    debug('>_p30_pedirCdtipsit');
    _p30_editorCdtipsit.reset();
    centrarVentanaInterna(_p30_ventanaCdtipsit.show());
}

function _p30_editarAutoAceptar(bot,callback)
{
    debug('>_p30_editarAutoAceptar');
    var form   = bot.up('form');
    var record = _p30_selectedRecord;
   myMask = _maskLocal('Cargando...');
    myMask.show();
    var valido=true;
    
    if(valido)
    {
        valido=form.isValid();
        if(!valido) {
        	myMask.close();
            datosIncompletos();
        }
    }
    
    if(valido)
    {
        var values=form.getValues();
        for(var prop in values)
        {
            record.set(prop,values[prop]);
        }
        if(_p30_endoso){
        	if((record.get('cdtipsit'))==TipoSituacion.AutosResidentes
         	||(record.get('cdtipsit'))==TipoSituacion.CamionesCarga
         	||(record.get('cdtipsit'))==TipoSituacion.PickUpCarga
         	||(record.get('cdtipsit'))==TipoSituacion.TractoCamionesArmados
         	||(record.get('cdtipsit'))==TipoSituacion.Motos
	         	||(record.get('cdtipsit'))==TipoSituacion.PickUpParticular){
	         		var numSerie='';
					numSerie = record.get('parametros.pv_otvalor37');
					if(!Ext.isEmpty(numSerie)){
						Ext.Ajax.request({
							url     : _p29_urlObtieneValNumeroSerie,
							params : {
								'smap1.numSerie'  : numSerie
								,'smap1.feini'    : _fieldByName('feini').getValue()
							},
							success : function(response) {
								var jsonNumSerie=Ext.decode(response.responseText);
								if(jsonNumSerie.exito!=true) {
									myMask.close();
									numSerie='';
									if(!RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)) {
										myMask.close();
										mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", jsonNumSerie.respuesta);
										
									} else {
										if(Ext.isEmpty(callback)){
								        	centrarVentanaInterna(Ext.MessageBox.confirm(
												'Confirmar'
												,'¿Desea Continuar?'
												,function(btn){
													if(btn === 'yes') {
														myMask.close();
														_fieldById('_p30_grid').getSelectionModel().deselectAll();
													} else {
														debug('no quiso Continuar');
													}
												}
											));
											mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", jsonNumSerie.respuesta);
								        }
									}
								} else {
									myMask.close();
									_fieldById('_p30_grid').getSelectionModel().deselectAll();
								}
							},
							failure : errorComunicacion
						});
					} else {
						myMask.close();
						mensajeError("No se recibio el número de serie");
					}
				
	        } else {
	        	myMask.close();
	        	_fieldById('_p30_grid').getSelectionModel().deselectAll();
	        }
        } else {
        	myMask.close();
        	_fieldById('_p30_grid').getSelectionModel().deselectAll();
        	}
        if(!Ext.isEmpty(callback)){
        	_fieldById('_p30_grid').getSelectionModel().deselectAll();
        	callback();
        }
    }
    
   
    debug('<_p30_editarAutoAceptar');
}

function _p30_renderer(record,mapeo)
{
    debug('>_p30_renderer',mapeo,record.data);
    
    var label='-situacion-';
    
    if(!Ext.isEmpty(record.get('cdtipsit')))
    {
        label='N/A';
        
        var cdtipsit = record.get('cdtipsit');
        var mapeos   = mapeo.split('#');
        for(var i in mapeos)
        {
            var mapeoIte  = mapeos[i];
            var cdtipsits = mapeoIte.split('|')[0];
            
            if((','+cdtipsits+',').lastIndexOf(','+cdtipsit+',')!=-1)//mapeo correcto
            {
                var name      = mapeoIte.split('|')[1];
                if(!isNaN(name))
                {
                    name='parametros.pv_otvalor'+(('x00'+name).slice(-2));
                }
                var origen = mapeoIte.split('|')[2];
                var valor  = record.get(name);
                debug('name:'   , name   , '.');
                debug('origen:' , origen , '.');
                debug('valor:'  , valor  , '.');
                
                label='';//'-vacio-';
                if(!Ext.isEmpty(valor))
                {
                    if(origen+'x'=='valorx')
                    {
                        label=valor;
                    }
                    else if(origen+'x'=='atributox')
                    {
                        label='';//'-atributo-';
                        var store=_p30_tatrisitFullForms[cdtipsit].down('[name='+name+']').getStore();
                        if(Ext.isEmpty(store))
                        {
                            debugError('No hay store de atributo:',cdtipsit,name,'.');
                            mensajeError('El atributo mapeado no contiene lista de valores');
                        }
                        else
                        {
                            var index = store.find('key',valor,0,false,false,true);
                            if(index==-1)
                            {
                                label='No encontrado...';
                            }
                            else
                            {
                                label=store.getAt(index).get('value');
                            }
                        }
                    }
                    else
                    {
                        label = 'Error...';//'-sin store-';
                        var fuentes      = '';
                        var valorFuentes = false;
                        if(origen.lastIndexOf('!')!=-1)
                        {
                            fuentes = origen.split('!')[1];
                            origen  = origen.split('!')[0];
                        }
                        
                        if(!Ext.isEmpty(fuentes))
                        {
                            var pares = fuentes.split('-');
                            for (var paresI in pares)
                            {
                                var parIzq = pares[paresI].split(':')[0];
                                var parDer = pares[paresI].split(':')[1];
                                if(parIzq==cdtipsit)
                                {
                                    var parDerFull = 'parametros.pv_otvalor'+(('00'+parDer).slice(-2));
                                    if('x'+record.get(parDerFull)!='x')
                                    {
                                        label        = record.get(parDerFull);
                                        valorFuentes = true;
                                    }
                                }
                            }
                        }
                        
                        if(!valorFuentes)
                        {
	                        var store=Ext.getStore(origen);
	                        if(Ext.isEmpty(store))
	                        {
	                            debugError('No hay store:',origen);
	                            mensajeError('No se encuentra la colecci&oacute;n con id "'+origen+'"');
	                        }
	                        if(!Ext.isEmpty(store)&&store.cargado)
	                        {
	                            var index = store.find('key',valor,0,false,false,true);
	                            if(index==-1)
	                            {
	                                label='No encontrado...';
	                            }
	                            else
	                            {
	                                label=store.getAt(index).get('value');
	                            }
	                        }
	                        else
	                        {
	                            label = 'Cargando...';
	                        }
                        }   
                    }
                }
            }
        }
    }
    return label;
}

function _p30_editarAutoBuscar()
{
    debug('>_p30_editarAutoBuscar');
    var cdtipsit = _p30_selectedRecord.get('cdtipsit');
    var window   = _p30_tatrisitAutoWindows[cdtipsit];
    var form     = _fieldById('_p30_tatrisitAutoForm'+cdtipsit);
    var record   = _p30_selectedRecord;
    var combos   = Ext.ComponentQuery.query('combo[heredar]',form);
    debug('combos con herencia:',combos);
    for(var i in combos)
    {
        combos[i].forceSelection=false;
    }
    form.loadRecord(record);
    debug('record cargando en emergente:',record.data);
    heredarPanel(form,true);
    form.micallback = function(me)
    {
        debug('>callback:');
        var record   = _p30_selectedRecord;
        var cdtipsit = record.get('cdtipsit');
        var data     = me.getValues();
        for(var prop in data)
        {
            record.set(prop,data[prop]);
        }
        
        if('|AR|CR|PC|PP|'.lastIndexOf('|'+cdtipsit+'|')!=-1)
        {
            debug('dentro de ','|AR|CR|PC|PP|');
            var planVal = record.get('cdplan');
            debug('planVal:',planVal);
            if(!Ext.isEmpty(planVal))
            {
                debug('planVal no empty');
                var planCmp=_fieldById('_p30_tatrisitParcialForm'+cdtipsit).down('[fieldLabel*=PAQUETE]');
                debug('planCmp:',planCmp);
                planCmp.heredar(true,function(planCmp)
                {
                    var valido = false;
                    planCmp.getStore().each(function(plan)
                    {
                        if(plan.get('key')==planVal)
                        {
                            valido = true;
                        }
                    });
                    if(!valido)
                    {
                        record.set('cdplan','');
                        mensajeWarning('Debe actualizar el paquete para el inciso');
                        _fieldById('_p30_grid').getSelectionModel().select(record);
                    }
                });
            }
        }
        
        me.up('window').hide();
        _fieldById('_p30_grid').getSelectionModel().select(record);
    };
    if(window.noMostrar==false)
    {
        centrarVentanaInterna(window.show());
    }
    else
    {
        window.noMostrar=false;
    }
    debug('<_p30_editarAutoBuscar');
}

function _p30_tatrisitWindowAutoAceptarClic(bot)
{
    var form = bot.up('form');
    debug('>_p30_tatrisitWindowAutoAceptarClic:',form);
    
    var ck='Validando registro';
    try
    {
        if(!form.isValid())
        {
            throw 'Favor de verificar los datos';
        }
        
        if(_p30_selectedRecord.get('cdtipsit')+'x'=='CRx')
        {
            Ext.Ajax.request(
            {
                url     : _p30_urlCargarObligatorioCamionRamo5
                ,params :
                {
                    'smap1.clave' : form.down('[fieldLabel*=VERSI]').getValue()
                }
                ,success : function(response)
                {
                    var json=Ext.decode(response.responseText);
                    debug('### tipo vehi:',json);
                    if(json.exito)
                    {
                        var tipoVehiName = _p30_tatrisitFullForms['CR'].down('[fieldLabel*=TIPO DE VEH]').name;
                        _p30_selectedRecord.set(tipoVehiName,json.smap1.tipo);
                        debug('_p30_selectedRecord:',_p30_selectedRecord.data);
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
         
        form.micallback(form);
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p30_inicializarTatripol(itemsTatripol)
{

    if(Ext.isEmpty(itemsTatripol))
    {
        itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
        try{
	        if(_p30_smap1.turistas=='S'){
	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
	    	}
        }catch(e){
        	debugError(e);
        }
        debug('itemsTatripol:',itemsTatripol);
    }

    for(var i in itemsTatripol)
    {
        var tatriItem = itemsTatripol[i];
        
        if(_p30_smap1.cdramo+'x'=='5x' && tatriItem.fieldLabel=='TIPO CAMBIO')
        {
            if(Ext.isEmpty(_p30_precioDolarDia))
            {
                tatriItem.setLoading(true);
                Ext.Ajax.request(
                {
                    url      : _p30_urlCargarTipoCambioWS
                    ,success : function(response)
                    {
                        var me=_fieldByLabel('TIPO CAMBIO',_fieldById('_p30_fieldsetTatripol'));
                        me.setLoading(false);
                        var json=Ext.decode(response.responseText);
                        debug('### dolar:',json);
                        _p30_precioDolarDia=json.smap1.dolar;
                        me.setValue(_p30_precioDolarDia);
                    }
                    ,failure : function()
                    {
                        _fieldByLabel('TIPO CAMBIO',_fieldById('_p30_fieldsetTatripol')).setLoading(false);
                        errorComunicacion();
                    }
                });
            }
            else
            {
                tatriItem.setValue(_p30_precioDolarDia);
            }
            tatriItem.hide();
        }
        else if(_p30_smap1.cdramo+'x'=='5x' && tatriItem.fieldLabel=='MONEDA')
        {
            tatriItem.select('1');
        }
    }
}

function _p30_imprimir()
{
    debug('>_p30_imprimir');
    
    if(_p30_smap1.tipoflot+'x'=='Px')
    {
        var urlRequestImpCotiza = _p30_urlImprimirCotiza
                + '?p_unieco='      + _p30_smap1.cdunieco
                + '&p_ramo='        + _p30_smap1.cdramo
                + '&p_subramo='     + _p30_smap1.cdtipsit
                + '&p_estado=W'
                + '&p_poliza='      + _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                + '&p_suplem=0'
                + '&p_perpag='      + _p30_selectedTarifa.get('CDPERPAG')
                + '&p_ntramite='    + _p30_smap1.ntramite
                + '&p_cdusuari='    + _p30_smap1.cdusuari
                + '&destype=cache'
                + "&desformat=PDF"
                + "&userid="        + _p30_reportsServerUser
                + "&ACCESSIBLE=YES"
                + "&report="        + _p30_reporteCotizacion
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
                    + _p30_urlViewDoc
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
    }
    else
    {
        Ext.create('Ext.form.Panel').submit(
        {
            url             : _p30_urlObtencionReporteExcel2
            ,standardSubmit : true
            ,target         : '_blank'
            ,params         :
            {
                'params.pv_cdunieco_i'  : _p30_smap1.cdunieco
                ,'params.pv_cdramo_i'   : _p30_smap1.cdramo
                ,'params.pv_estado_i'   : 'W'
                ,'params.pv_nmpoliza_i' : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                ,'params.pv_nmsuplem_i' : '0'
                ,'params.pv_cdperpag_i' : _p30_selectedTarifa.get('CDPERPAG')
                ,'params.pv_cdusuari_i' : _p30_smap1.cdusuari
                ,cdreporte              : 'REPCOT002'
            }
        });
        Ext.create('Ext.form.Panel').submit(
        {
            url             : _p30_urlObtencionReporteExcel
            ,standardSubmit : true
            ,target         : '_blank'
            ,params         :
            {
                'params.pv_cdunieco_i'  : _p30_smap1.cdunieco
                ,'params.pv_cdramo_i'   : _p30_smap1.cdramo
                ,'params.pv_estado_i'   : 'W'
                ,'params.pv_nmpoliza_i' : _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                ,'params.pv_nmsuplem_i' : '0'
                ,'params.pv_cdperpag_i' : _p30_selectedTarifa.get('CDPERPAG')
                ,'params.pv_cdusuari_i' : _p30_smap1.cdusuari
                ,cdreporte              : 'REPCOT001'
            }
        });
    }
    debug('<_p30_imprimir');
}

function _p30_enviar()
{
    debug('>_p30_enviar');
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Enviar cotizaci&oacute;n'
        ,width       : 550
        ,modal       : true
        ,height      : 150
        ,buttonAlign : 'center'
        ,bodyPadding : 5
        ,items       :
        [
            {
                xtype       : 'textfield'
                ,itemId     : '_p30_idInputCorreos'
                ,fieldLabel : 'Correo(s)'
                ,emptyText  : 'Correo(s) separados por ;'
                ,labelWidth : 100
                ,allowBlank : false
                ,blankText  : 'Introducir correo(s) separados por ;'
                ,width      : 500
            }
        ]
        ,buttons :
        [
            {
                text     : 'Enviar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function()
                {
                    var me = this;
                    if (_fieldById('_p30_idInputCorreos').getValue().length > 0
                            &&_fieldById('_p30_idInputCorreos').getValue() != 'Correo(s) separados por ;')
                    {
                        debug('Se va a enviar cotizacion');
                        me.up().up().setLoading(true);
                        Ext.Ajax.request(
                        {
                            url     : _p30_urlEnviarCorreo
                            ,params :
                            {
                                to          : _fieldById('_p30_idInputCorreos').getValue()
                                ,urlArchivo : _p30_urlImprimirCotiza
                                             + '?p_unieco='      + _p30_smap1.cdunieco
                                             + '&p_ramo='        + _p30_smap1.cdramo
                                             + '&p_subramo='     + _p30_smap1.cdtipsit
                                             + '&p_estado=W'
                                             + '&p_poliza='      + _fieldByName('nmpoliza',_fieldById('_p30_form')).getValue()
                                             + '&p_suplem=0'
                                             + '&p_perpag='      + _p30_selectedTarifa.get('CDPERPAG')
                                             + '&p_ntramite='    + _p30_smap1.ntramite
                                             + '&p_cdusuari='    + _p30_smap1.cdusuari
                                             + '&destype=cache'
                                             + "&desformat=PDF"
                                             + "&userid="        + _p30_reportsServerUser
                                             + "&ACCESSIBLE=YES"
                                             + "&report="        + _p30_reporteCotizacion
                                             + "&paramform=no",
                                nombreArchivo : 'cotizacion_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf'
                            },
                            callback : function(options,success,response)
                            {
                                me.up().up().setLoading(false);
                                if (success)
                                {
                                    var json = Ext.decode(response.responseText);
                                    if (json.success == true)
                                    {
                                        me.up().up().destroy();
                                        centrarVentanaInterna(Ext.Msg.show(
                                        {
                                            title : 'Correo enviado'
                                            ,msg : 'El correo ha sido enviado'
                                            ,buttons : Ext.Msg.OK
                                        }));
                                    }
                                    else
                                    {
                                        mensajeError('Error al enviar');
                                    }
                                }
                                else
                                {
                                    errorComunicacion();
                                }
                            }
                        });
                    }
                    else
                    {
                        mensajeWarning('Introduzca al menos un correo');
                    }
                }
            }
            ,{
                text     : 'Cancelar'
                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                ,handler : function()
                {
                    this.up().up().destroy();
                }
            }
        ]
    }).show());
    _fieldById('_p30_idInputCorreos').focus();
    debug('<_p30_enviar');
}

function _p30_confirmarEndoso()
{
    debug('>_p30_confirmarEndoso');
    
    var valido = _fieldById('_p30_form').isValid();
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        var error  = '';
        for(var i=0;i<_f1_botones.length;i++)
        {
            var boton    = _f1_botones[i];
            var cdtipsit = boton.cdtipsit;
            var panel    = _p30_paneles[cdtipsit];
            debug('buscando en panel:',panel);
            if(panel.valores==false)
            {
                valido = false;
                error  = error+'FALTA DEFINIR: '+boton.text+'<br/>';
            }
        }
        if(!valido)
        {
            mensajeWarning(error);
        }
    }
    
    if(valido)
    {
        valido = _p30_store.getCount()>=1;
        if(!valido)
        {
            mensajeWarning('Debe capturar al menos un inciso');
        }
    }
    
    if(valido)
    {
        var error        = '';
        var agregarError = function(cadena,nmsituac)
        {
            valido = false;
            error  = error + 'Inciso ' + nmsituac + ': ' + cadena +'</br>';
        };
        _p30_store.each(function(record)
        {
            var cdtipsit = record.get('cdtipsit');
            var nmsituac = record.get('nmsituac');
            
            if(!Ext.isEmpty(cdtipsit))
            {
                if(Ext.isEmpty(record.get('cdplan')))
                {
                    agregarError('Debe seleccionar el paquete',nmsituac);
                }
                var itemsObliga = Ext.ComponentQuery.query('[swobligaflot=true]',_p30_tatrisitFullForms[cdtipsit]);
                for(var i in itemsObliga)
                {
                    if(Ext.isEmpty(record.get(itemsObliga[i].getName())))
                    {
                        agregarError('Falta definir "'+itemsObliga[i].getFieldLabel()+'"',nmsituac);
                    }
                }
            }
            else
            {
                agregarError('Debe seleccionar el tipo de veh&iacute;culo',nmsituac);
            }
        });
        if(!valido)
        {
            mensajeWarning(error);
        }
    }
    
    debug('_p30_paneles:',_p30_paneles,'valido:',valido);
    if(valido)
    {
        var form = _fieldById('_p30_form');
        
        //copiar paneles a oculto
        var arr = Ext.ComponentQuery.query('#_p30_gridTarifas');
        if(arr.length>0)
        {
            var formDescuentoActual = _fieldById('_p30_formDescuento');
            var formCesion          = _fieldById('_p30_formCesion');
            var recordPaneles       = new _p30_modelo(formDescuentoActual.getValues());
            var itemsCesion         = Ext.ComponentQuery.query('[fieldLabel]',formCesion);
            for(var i=0;i<itemsCesion.length;i++)
            {
                recordPaneles.set(itemsCesion[i].getName(),itemsCesion[i].getValue());
            }
            form.formOculto.loadRecord(recordPaneles);
            debug('form.formOculto.getValues():',form.formOculto.getValues());
        }
    
        debug('length:',_p30_paneles.length,'type:',typeof _p30_paneles);
        var recordsCdtipsit = [];
        for(var cdtipsitPanel in _p30_paneles)
        {
            var panel      = _p30_paneles[cdtipsitPanel];
            var recordBase = new _p30_modelo(panel.valores);
            recordBase.set('cdtipsit',cdtipsitPanel);
            debug('cdtipsitPanel:',cdtipsitPanel,'recordBase:',recordBase);
            recordsCdtipsit[cdtipsitPanel] = recordBase;
        }
        debug('recordsCdtipsit:',recordsCdtipsit);
        var storeTvalosit = Ext.create('Ext.data.Store',
        {
            model : '_p30_modelo'
        });
        var formValuesAux = form.getValues();
        var formValues    = {};
        for(var prop in formValuesAux)
        {
            if(prop+'x'!='x'
                &&prop.slice(0,5)=='param')
            {
                formValues[prop]=formValuesAux[prop];
            }
        }
        debug('formValues:',formValues);
        var valuesFormOculto = form.formOculto.getValues();
        debug('valuesFormOculto:',valuesFormOculto);
        _p30_store.each(function(record)
        {
            var cdtipsit       = record.get('cdtipsit');
            var cdtipsitPanel  = _p30_smap1['destino_'+cdtipsit];
            var recordBase     = recordsCdtipsit[cdtipsitPanel];
            var recordTvalosit = new _p30_modelo(record.data);
            /*
            for(var prop in recordTvalosit.data)
            {
                var valor = recordTvalosit.get(prop);
                var base  = recordBase.get(prop);
                if(valor+'x'=='x'&&base+'x'!='x')
                {
                    recordTvalosit.set(prop,base);
                }
            }
            */
            
            //---
            if(cdtipsitPanel==cdtipsit)
            {
                for(var prop in recordTvalosit.getData())
                {
                    var valor = recordTvalosit.get(prop);
                    var base  = recordBase.get(prop);
                    if(Ext.isEmpty(valor)&&!Ext.isEmpty(base))
                    {
                        recordTvalosit.set(prop,base);
                    }
                }
            }
            else
            {
                for(var prop in recordTvalosit.getData())
                {
                    var base = recordBase.get(prop);
                    debug('base:',base);
                    
                    var valorValosit = recordTvalosit.get(prop);
                    debug('valorValosit:',valorValosit);
                    
                    var cmpPanel = _p30_paneles[cdtipsitPanel].down('[name='+prop+']');
                    debug('cmpPanel:',cmpPanel,'.');
                    if(!Ext.isEmpty(cmpPanel))
                    {
                        if(cmpPanel.auxiliar=='adicional'
                            &&Ext.isEmpty(valorValosit)
                            &&!Ext.isEmpty(base)
                        )
                        {
                            debug('set normal, porque es adicional');
                            //alert('ADIC!-'+fieldLabel+'-'+prop);
                            recordTvalosit.set(prop,base);
                        }
                        else
                        {
                            var cmpPanelLabel = cmpPanel.fieldLabel;
                            debug('cmpPanelLabel:',cmpPanelLabel,'.');
                            var cmpByLabel = _p30_tatrisitFullForms[cdtipsit].down('[fieldLabel*='+_substringComa(cmpPanelLabel)+']');
                            if(!Ext.isEmpty(cmpByLabel))
                            {
                                var nameByLabel = cmpByLabel.name;
                                var valor       = recordTvalosit.get(nameByLabel);
                                debug('valor:',valor);
                                if(Ext.isEmpty(valor)&&!Ext.isEmpty(base))
                                {
                                    recordTvalosit.set(nameByLabel,base);
                                }
                            }
                        }
                    }
                }
            }
            //---
            
            if(_p30_smap1.mapeo=='DIRECTO')
            {
                for(var prop in formValues)
                {
                    recordTvalosit.set(prop,formValues[prop]);
                }
                for(var att in valuesFormOculto)
                {
                    var valorOculto  = valuesFormOculto[att];
                    var valorValosit = recordTvalosit.get(att);
                    if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                    {
                        recordTvalosit.set(att,valorOculto);
                    }
                }
            }
            else
            {
                var mapeos = _p30_smap1.mapeo.split('#');
                debug('mapeos:',mapeos);
                for(var i in mapeos)
                {
                    var cdtipsitsMapeo = mapeos[i].split('|')[0];
                    var mapeo          = mapeos[i].split('|')[1];
                    debug('cdtipsit:',cdtipsit,'cdtipsitsMapeo:',cdtipsitsMapeo);
                    if((','+cdtipsitsMapeo+',').lastIndexOf(','+cdtipsit+',')!=-1)
                    {
                        debug('coincidente:',cdtipsitsMapeo,'cdtipsit:',cdtipsit)
                        debug('mapeo:',mapeo);
                        if(mapeo=='DIRECTO')
                        {
                            debug('directo');
                            for(var prop in formValues)
                            {
                                recordTvalosit.set(prop,formValues[prop]);
                            }
                            for(var att in valuesFormOculto)
                            {
                                var valorOculto  = valuesFormOculto[att];
                                var valorValosit = recordTvalosit.get(att);
                                if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                                {
                                    recordTvalosit.set(att,valorOculto);
                                }
                            }
                        }
                        else
                        {
                            var atributos = mapeo.split('@');
                            debug('atributos:',atributos);
                            for(var i in atributos)
                            {
                                var atributoIte = atributos[i];
                                var modelo      = atributoIte.split(',')[0];
                                var origen      = atributoIte.split(',')[1];
                                var pantalla    = atributoIte.split(',')[2];
                                
                                modelo   = 'parametros.pv_otvalor'+(('x00'+modelo)  .slice(-2));
                                pantalla = 'parametros.pv_otvalor'+(('x00'+pantalla).slice(-2));
                                
                                debug('modelo:'   , modelo   , '.');
                                debug('origen:'   , origen   , '.');
                                debug('pantalla:' , pantalla , '.');
                                
                                if(origen=='F')
                                {
                                    recordTvalosit.set(modelo,formValues[pantalla]);
                                }
                                else if(origen=='O')
                                {
                                    var valorOculto  = valuesFormOculto[pantalla];
                                    var valorValosit = recordTvalosit.get(modelo);
                                    if(valorValosit+'x'=='x'&&valorOculto+'x'!='x')
                                    {
                                        recordTvalosit.set(modelo,valorOculto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            storeTvalosit.add(recordTvalosit);
            debug('record:',record.data,'tvalosit:',recordTvalosit.data);
        });
        debug('_p30_store:',_p30_store);
        debug('storeTvalosit:',storeTvalosit);
        
        var json =
        {
            smap1 :
            {
                cdunieco     : _p30_smap1.CDUNIECO
                ,cdramo      : _p30_smap1.CDRAMO
                ,estado      : _p30_smap1.ESTADO
                ,nmpoliza    : _p30_smap1.NMPOLIZA
                ,cdtipsup    : _p30_smap1.cdtipsup
                ,fechaEndoso : Ext.Date.format(_fieldByName('fechaEndoso').getValue(),'d/m/Y')
                ,confirmar   : 'no'
                ,cdperpag    : _p30_smap1.CDPERPAG
                ,cdagente    : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
            }
            ,slist1 : []
            ,slist2 : []
            ,slist3 : []
        };
        
        for(var cdtipsitPanel in recordsCdtipsit)
        {
            json.slist3.push(recordsCdtipsit[cdtipsitPanel].data);
        }
        
        var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
        try{
	        if(_p30_smap1.turistas=='S'){
	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
	    	}
        }catch(e){
        	debugError(e);
        }
        debug('itemsTatripol:',itemsTatripol);
        for(var i in itemsTatripol)
        {
            var tatri=itemsTatripol[i];
            json.smap1['tvalopol_'+tatri.cdatribu]=tatri.getValue();
        }
        
        _p30_store.each(function(record)
        {
            json.slist2.push(record.data);
        });
        
        storeTvalosit.each(function(record)
        {
            json.slist1.push(record.data);
        });
        
        //crear record con los valores del formulario y el formulario oculto
        debug('storeTvalosit.getAt(0).data:',storeTvalosit.getAt(0).data);
        var recordTvalositPoliza=new _p30_modelo(storeTvalosit.getAt(0).data);
        debug('recordTvalositPoliza:',recordTvalositPoliza.data);
        recordTvalositPoliza.set('cdtipsit','XPOLX');
        for(var prop in formValues)
        {
            recordTvalositPoliza.set(prop,formValues[prop]);
        }
        for(var att in valuesFormOculto)
        {
            recordTvalositPoliza.set(att,valuesFormOculto[att]);
        }
        debug('recordTvalositPoliza final:',recordTvalositPoliza.data);
        json.slist2.push(recordTvalositPoliza.data);
        //crear record con los valores del formulario y el formulario oculto
        
        if(!Ext.isEmpty(_p30_flujo)) {
            json.flujo = _p30_flujo;
        }
        debug('>>> json a enviar:',json);
        var boton=_fieldById('_p30_endosoButton');
        boton.setText('Cargando...');
        boton.setDisabled(true);
        var myMask = _maskLocal();
        
        function ConfirmarEndoso(){
        Ext.Ajax.request(
			        {
			            url       : _p30_urlConfirmarEndoso
			            ,jsonData : json
			            ,success  : function(response) {
			            	myMask.close();
			                boton.setText('Emitir');
			                boton.setDisabled(false);
			                var json2 = Ext.decode(response.responseText);
			                debug('### confirmar endoso:',json2);
			                if(json2.success) {
			                	Ext.create('Ext.window.Window',{
										title        : 'Tarifa final'
										,itemId      : 'tarifa'
										,modal       : true
										,buttonAlign : 'center'
										,width       : 600
										,height      : 550
										//,defaults    : { width: 650 }
										,closable    : false
										,autoScroll  : true
										,loader      :{
												url       : url_PantallaPreview
												,params   : {
														'smap4.nmpoliza'  : _p30_smap1.NMPOLIZA
			                                            ,'smap4.cdunieco' : _p30_smap1.CDUNIECO
			                                            ,'smap4.cdramo'   : _p30_smap1.CDRAMO
			                                            ,'smap4.estado'   : _p30_smap1.ESTADO
			                                            ,'smap4.nmsuplem' : json2.omap1.pv_nmsuplem_o 
			                                            ,'smap4.nsuplogi' : json2.omap1.pv_nsuplogi_o
			                                     }
												,scripts  : true
												,loadMask : true
												,autoLoad : true
												,ajaxOptions : {
			                                    	method: 'POST'
												}
										 }
										,buttons:[{
													text    : 'Confirmar endoso'
													,name    : 'endosoButton'
													,icon    : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
													,handler : 
														function (me){
			                                                boton.setText('Cargando...');
			                                                boton.setDisabled(true);
			                                                
			                                                var json2 =
															        {
															            smap1 : {
															                cdunieco     : _p30_smap1.CDUNIECO
															                ,cdramo      : _p30_smap1.CDRAMO
															                ,estado      : _p30_smap1.ESTADO
															                ,nmpoliza    : _p30_smap1.NMPOLIZA
															                ,cdtipsup    : _p30_smap1.cdtipsup
															                ,fechaEndoso : Ext.Date.format(_fieldByName('fechaEndoso').getValue(),'d/m/Y')
															                ,confirmar   : 'si'
															                ,cdperpag    : _p30_smap1.CDPERPAG
															                ,cdagente    : _fieldByLabel('AGENTE',_fieldById('_p30_form')).getValue()
															            }
															            ,slist1 : []
															            ,slist2 : []
															            ,slist3 : []
															        };
															storeTvalosit.each(function(record) {
															            json2.slist1.push(record.data);
															        });
															        
															if(!Ext.isEmpty(_p30_flujo)) {
                                                                json2.flujo = _p30_flujo;
                                                            }       
                                                            
			                                                Ext.Ajax.request( {
														            url       : _p30_urlConfirmarEndoso
														           ,jsonData : json2
															       ,success  : function(response){
															            	var json3 = Ext.decode(response.responseText);
															                var callbackRemesa = function() { marendNavegacion(2); };
															                
															                mensajeCorrecto('Endoso generado',json3.respuesta,function(){
																				_generarRemesaClic(
																				true
																				,_p30_smap1.CDUNIECO
																				,_p30_smap1.CDRAMO
																				,_p30_smap1.ESTADO
																				,_p30_smap1.NMPOLIZA
																				,callbackRemesa);
																		});
																	}
														            ,failure  : function(){
														                errorComunicacion();
														            }});
			                                                    me.up('window').destroy();
			                                                    
			                                                   }
																						                    
												   },{
													text    : 'Cancelar'
													,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
													,handler : function (me){
																	me.up('window').destroy();
			                                                        marendNavegacion(2);
			                                                        } 
			                                        },{
													text    : 'Documentos'
													,name    : 'documentoButton'
													,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
													,handler  :function(){
														 var numRand=Math.floor((Math.random()*100000)+1);
				                                         debug(numRand);
														 centrarVentanaInterna(Ext.create('Ext.window.Window', {
														 	title          : 'Vista previa'
													        ,width         : 700
													        ,height        : 500
													        ,collapsible   : true
													        ,titleCollapse : true
													        ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
													                         +'src="'+_p30_urlViewDoc+"?&path="+_RUTA_DOCUMENTOS_TEMPORAL+"&filename="+json2.omap1.pdfEndosoNom_o+"\">"
													                         +'</iframe>'
													        ,listeners     : {
													        	resize : function(win,width,height,opt){
													                debug(width,height);
													                $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
													        }
													      }}).show());
													}
													,hidden   : _p30_smap1.TIPOFLOT!= TipoFlotilla.Flotilla? false :true
			                                        } ]
								     }).show();
			                } else {
			                    mensajeError(json2.respuesta);
			                }
			            }
			            ,failure  : function(){
			            	myMask.close();
			                boton.setText('Emitir');
			                boton.setDisabled(false);
			                errorComunicacion();
			            }
			        });
    	}
        var numSerie= '';
        _p30_store.each(function(record) {
	    	debug( record.get('parametros.pv_otvalor37'));
	    	numSerie+=''+(record.get('parametros.pv_otvalor37'))+'|';
	    	
    	});
        
        Ext.Ajax.request(
        	{
				url     : _p29_urlObtieneValNumeroSerie,
				params : {
					'smap1.numSerie'  : numSerie
					,'smap1.feini'    : _fieldByName('feini').getValue()
				},
				success : function(response) {
					var jsonNumSerie=Ext.decode(response.responseText);
					if(jsonNumSerie.exito!=true) {
						myMask.close();
						numSerie='';
						if(!RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)) {
							myMask.close();
							mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", jsonNumSerie.respuesta);
							boton.setText('Emitir');
       			 			boton.setDisabled(false);
						} else {
							centrarVentanaInterna(Ext.MessageBox.confirm(
								'Confirmar'
								,'¿Desea Continuar?'
								,function(btn){
									if(btn === 'yes') {
										myMask.close();
										ConfirmarEndoso();
									} else {
										boton.setText('Emitir');
       			 						boton.setDisabled(false);
										debug('no quiso Continuar');
									}
								}
							));
							mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", jsonNumSerie.respuesta);
					        
						}
					} else {
						myMask.close();
						ConfirmarEndoso();
					}
				},
				failure : errorComunicacion
			});
    }
    
    debug('<_p30_confirmarEndoso');
}

function _p30_cargarIncisoXpolxTvalopolTconvalsit(json, brincarTconvalsit)
{
    debug('>_p30_cargarIncisoXpolxTvalopolTconvalsit args:', arguments);
    var datosGenerales = new _p30_modelo(json.smap1);
    debug('datosGenerales:',datosGenerales);
    var cdtipsitDatos  = datosGenerales.raw['parametros.pv_cdtipsit'];
    debug('cdtipsitDatos:',cdtipsitDatos);
    
    if(_p30_smap1.cdramo+'x'=='5x')
    {
        var clienteNuevoName = _p30_tatrisitFullForms[_p30_smap1.cdtipsit].down('[fieldLabel*=CLIENTE NUEVO]').name;
        debug('clienteNuevoName:',clienteNuevoName);
        datosGenerales.set(clienteNuevoName,'S');
    }
    
    ck='Recuperando datos generales';
    if(_p30_smap1.mapeo=='DIRECTO')
    {
        _fieldById('_p30_form').loadRecord(datosGenerales);
        _fieldById('_p30_form').formOculto.loadRecord(datosGenerales);
        
        var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
        try{
	        if(_p30_smap1.turistas=='S'){
	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
	    	}
        }catch(e){
        	debugError(e);
        }
        debug('itemsTatripol:',itemsTatripol);
        for(var i in itemsTatripol)
        {
            var tatri=itemsTatripol[i];
            tatri.setValue(json.smap1[tatri.name]);
        }
    }
    else
    {
        var mapeos = _p30_smap1.mapeo.split('#');
        debug('mapeos:',mapeos);
        for(var i in mapeos)
        {
            var cdtipsitsMapeo = mapeos[i].split('|')[0];
            var mapeo          = mapeos[i].split('|')[1];
            debug('cdtipsitDatos:',cdtipsitDatos,'cdtipsitsMapeo:',cdtipsitsMapeo);
            if((','+cdtipsitsMapeo+',').lastIndexOf(','+cdtipsitDatos+',')!=-1)
            {
                debug('coincidente:',cdtipsitsMapeo,'cdtipsitDatos:',cdtipsitDatos)
                debug('mapeo:',mapeo);
                if(mapeo=='DIRECTO')
                {
                    debug('directo');
                    if(_p30_smap1.cdramo+'x'=='5x'&& 
//                     		(rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
                       RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
                      )
                    {
                        var agenteCmp  = _fieldByLabel('AGENTE'  , _fieldById('_p30_form'));
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        agenteCmp.forceSelection=false;
                        negocioCmp.forceSelection=false;
                    }
                    else if(_p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                    {
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        negocioCmp.forceSelection=false;
                    }
                    _fieldById('_p30_form').loadRecord(datosGenerales);
                    _fieldById('_p30_form').formOculto.loadRecord(datosGenerales);
                    
                    var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
                    try{
            	        if(_p30_smap1.turistas=='S'){
            	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
            	    	}
                    }catch(e){
                    	debugError(e);
                    }
                    debug('itemsTatripol:',itemsTatripol);
                    for(var i in itemsTatripol)
                    {
                        var tatri=itemsTatripol[i];
                        tatri.setValue(json.smap1[tatri.name]);
                    }
                    
                    if(_p30_smap1.cdramo+'x'=='5x'
                       &&
//                      (rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
                        RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
                       )
                    {
                        var agenteCmp  = _fieldByLabel('AGENTE'  , _fieldById('_p30_form'));
                        agenteCmp.getStore().load(
                        {
                            params :
                            {
                                'params.agente' : agenteCmp.getValue()
                            }
                            ,callback : function()
                            {
                                var agenteCmp  = _fieldByLabel('AGENTE', _fieldById('_p30_form'));
                                //agenteCmp.select(agenteCmp.getValue());
                                agenteCmp.forceSelection=true;
                                var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                                negocioCmp.heredar(true,function(cmp)
                                {
                                    cmp.forceSelection=true;
                                    if(_p30_endoso)
                                    {
                                        negocioCmp.fireEvent('change',negocioCmp,negocioCmp.getValue());
                                    }
                                    else
                                    {
                                        _p30_editorCdtipsit.heredar();
                                    }
                                });
                            }
                        });
                    }
                    else if(_p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                    {
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        negocioCmp.heredar(true,function(cmp)
                        {
                            cmp.forceSelection=true;
                            if(_p30_endoso)
                            {
                                negocioCmp.fireEvent('change',negocioCmp,negocioCmp.getValue());
                            }
                            else
                            {
                                _p30_editorCdtipsit.heredar();
                            }
                        });
                    }
                    else if(_p30_endoso)
                    {
                        negocioCmp.fireEvent('change',negocioCmp,negocioCmp.getValue());
                    }
                    else
                    {
                        _p30_editorCdtipsit.heredar();
                    }
                }
                else
                {
                    var atributos = mapeo.split('@');
                    debug('atributos:',atributos);
                    
                    var recordMapeado = new _p30_modelo();
                    if(_p30_smap1.cdramo=='5')
                    {
                        var clienteNuevoName = _p30_tatrisitFullForms[_p30_smap1.cdtipsit].down('[fieldLabel*=CLIENTE NUEVO]').name;
                        debug('clienteNuevoName:',clienteNuevoName);
                        datosGenerales.set(clienteNuevoName,'S');
                    }
                    
                    for(var i in atributos)
                    {
                        var atributoIte = atributos[i];
                        var modelo      = atributoIte.split(',')[0];
                        var origen      = atributoIte.split(',')[1];
                        var pantalla    = atributoIte.split(',')[2];
                        
                        modelo   = 'parametros.pv_otvalor'+(('x00'+modelo)  .slice(-2));
                        pantalla = 'parametros.pv_otvalor'+(('x00'+pantalla).slice(-2));
                        
                        debug('modelo:'   , modelo   , '.');
                        debug('origen:'   , origen   , '.');
                        debug('pantalla:' , pantalla , '.');
                        
                        recordMapeado.set(pantalla,datosGenerales.get(modelo));
                    }
                    
                    debug('recordMapeado:',recordMapeado.data);
                    if(_p30_smap1.cdramo+'x'=='5x'&& 
//                     (rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
                        RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
                      )
                    {
                        var agenteCmp  = _fieldByLabel('AGENTE' , _fieldById('_p30_form'));
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        agenteCmp.forceSelection=false;
                        negocioCmp.forceSelection=false;
                    }
                    else if(_p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                    {
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        negocioCmp.forceSelection=false;
                    }
                    
                    _fieldById('_p30_form').loadRecord(recordMapeado);
                    _fieldById('_p30_form').formOculto.loadRecord(recordMapeado);
                    
                    var itemsTatripol = Ext.ComponentQuery.query('[name]',_fieldById('_p30_fieldsetTatripol'));
                    try{
            	        if(_p30_smap1.turistas=='S'){
            	        	itemsTatripol.push(_fieldByName('aux.otvalor18',null,true));
            	    	}
                    }catch(e){
                    	debugError(e);
                    }
                    debug('itemsTatripol:',itemsTatripol);
                    for(var i in itemsTatripol)
                    {
                        var tatri=itemsTatripol[i];
                        tatri.setValue(json.smap1[tatri.name]);
                    }
                    
                    if(_p30_smap1.cdramo+'x'=='5x'&& 
//                        (rolesSuscriptores.lastIndexOf('|'+_p30_smap1.cdsisrol+'|')!=-1)
                          RolSistema.puedeSuscribirAutos(_p30_smap1.cdsisrol)
                       )
                    {
                        var agenteCmp  = _fieldByLabel('AGENTE' , _fieldById('_p30_form'));
                        agenteCmp.getStore().load(
                        {
                            params :
                            {
                                'params.agente' : agenteCmp.getValue()
                            }
                            ,callback : function()
                            {
                                var agenteCmp  = _fieldByLabel('AGENTE', _fieldById('_p30_form'));
                                //agenteCmp.select(agenteCmp.getValue());
                                agenteCmp.forceSelection=true;
                                var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                                negocioCmp.heredar(true,function(cmp)
                                {
                                    cmp.forceSelection=true;
                                });
                            }
                        });
                    }
                    else if(_p30_smap1.cdramo+'x'=='5x'&&_p30_smap1.cdsisrol+'x'=='PROMOTORAUTOx')
                    {
                        var negocioCmp = _fieldLikeLabel('NEGOCIO' , _fieldById('_p30_form'));
                        negocioCmp.heredar(true,function(cmp)
                        {
                            cmp.forceSelection=true;
                        });
                    }
                }
            }
        }
    }
    
    if(!Ext.isEmpty(json.smap1.CDPERSON))
    {
        ck='Recuperando cliente';
        /*if(maestra&&false)
        {
            _fieldLikeLabel('NOMBRE CLIENTE').setValue('');
        }
        else
        {*/
            json.smap1['CLAVECLI']       = json.smap1.CDPERSON;
            _p30_recordClienteRecuperado = new _p30_modeloRecuperado(json.smap1);
            
            debug('_p30_recordClienteRecuperado:',_p30_recordClienteRecuperado);
            
            var combcl      = _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_form'));
            combcl.semaforo = true;
            combcl.setValue('N');
            combcl.semaforo = false;
        /*}*/
    }
    
    if (true !== brincarTconvalsit) {
        ck='Recuperando configuracion de incisos';
        for(var i in json.slist1)
        {
            var tconvalsit = json.slist1[i];
            var cdtipsit   = tconvalsit.CDTIPSIT;
            _p30_paneles[cdtipsit].valores=tconvalsit;
        }
    }
}

function _p30_recuperarClienteTramite()
{
    /*
     * Cuando se tiene cdpercli se recupera para ramo 5 cuando exista el combo de cliente nuevo
     * inicio
     */
    if(!Ext.isEmpty(_p30_smap1.cdpercli))
    {
        var ckCdpercli = 'Recuperando cliente de tr\u00e1mite';
        try
        {
            var comboCliente = _fieldLikeLabel('CLIENTE NUEVO',_fieldById('_p30_panel3Fieldset'),true);
            debug('combo cliente nuevo:',comboCliente);
            if(!Ext.isEmpty(comboCliente))
            {
                debug('Entro a recuperar cliente con cdperson:',_p30_smap1.cdpercli);
                _mask(ckCdpercli);
                Ext.Ajax.request(
                {
                    url      : _p30_urlRecuperarCliente
                    ,params  :
                    {
                        'map1.pv_cdperson_i'  : _p30_smap1.cdpercli
                        ,'map1.soloBD'        : 'S'
                        ,'map1.pv_rfc_i'      : ''//rfc
                        ,'map1.cdtipsit'      : _p30_smap1.cdtipsit
                        ,'map1.pv_cdtipsit_i' : _p30_smap1.cdtipsit
                        ,'map1.pv_cdunieco_i' : _p30_smap1.cdunieco
                        ,'map1.pv_cdramo_i'   : _p30_smap1.cdramo
                        ,'map1.pv_estado_i'   : 'W'
                        ,'map1.pv_nmpoliza_i' : _fieldByName('nmpoliza').getValue()
                    }
                    ,success : function(response)
                    {
                        _unmask();
                        var ck = 'Decodificando respuesta al recuperar cliente de tr\u00e1mite';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### recuperacion cliente tramite:',json);
                            
                            comboCliente.semaforo = true;
                            comboCliente.setValue('N');
                            comboCliente.setReadOnly(true);
                            comboCliente.semaforo = false;
                            
                            _p30_recordClienteRecuperado = new _p30_modeloRecuperado(json.slist1[0]);
                            debug('_p30_recordClienteRecuperado:',_p30_recordClienteRecuperado);
                            
                            var nombreCliCmp    = _fieldLikeLabel('NOMBRE CLIENTE'      , _fieldById('_p30_panel3Fieldset'),true)
                                ,tipoPerCmp     = _fieldLikeLabel('TIPO PERSONA'        , _fieldById('_p30_panel3Fieldset'),true)
                                ,codPosCliCmp   = _fieldLikeLabel('CP CIRCULACI'        , _fieldById('_p30_panel3Fieldset'),true)
                                ,feNacimiCliCmp = _fieldLikeLabel('FECHA DE NACIMIENTO' , _fieldById('_p30_panel3Fieldset'),true);
                            
                            if(!Ext.isEmpty(nombreCliCmp))
                            {
                                nombreCliCmp.setValue(_p30_recordClienteRecuperado.get('NOMBRECLI'));
                                nombreCliCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(tipoPerCmp))
                            {
                                tipoPerCmp.setValue(_p30_recordClienteRecuperado.raw.TIPOPERSONA);
                                tipoPerCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(codPosCliCmp))
                            {
                                codPosCliCmp.setValue(_p30_recordClienteRecuperado.raw.CODPOSTAL);
                                codPosCliCmp.setReadOnly(true);
                            }
                            
                            if(!Ext.isEmpty(feNacimiCliCmp))
                            {
                                feNacimiCliCmp.setValue(_p30_recordClienteRecuperado.raw.FENACIMICLI);
                                feNacimiCliCmp.setReadOnly(true);
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
                        errorComunicacion(null,'Error al recuperar cliente de tr\u00e1mite');
                    }
                });
            }
        }
        catch(e)
        {
            manejaException(e,ckCdpercli);
        }
    }
    /*
     * Cuando se tiene cdpercli se recupera para ramo 5 cuando exista el combo de cliente nuevo
     * fin
     */
}

/*
 * Este metodo busca en los otvalor del tramite el numero de la ultima cotizacion
 * realizada para recuperarla automaticamente en pantalla
 */
function _p30_recuperarCotizacionDeTramite()
{
    if(!Ext.isEmpty(_p30_flujo))
    {
        var ck = 'Recuperando cotizaci\u00f3n de tr\u00e1mite';
        try
        {
            _mask(ck);
            Ext.Ajax.request(
            {
                url      : _p30_urlRecuperarOtvalorTramiteXDsatribu
                ,params  :
                {
                    'params.ntramite'  : _p30_flujo.ntramite
                    ,'params.dsatribu' : 'COTIZACI%N%TR%MITE'
                }
                ,success : function(response)
                {
                    _unmask();
                    var ck = 'Decodificando respuesta al recuperar cotizaci\u00f3n de tr\u00e1mite';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### cotizacion de tramite:',json);
                        if(json.success===true)
                        {
                            if(!Ext.isEmpty(json.params.otvalor))
                            {
                                _fieldByName('nmpoliza').setValue(json.params.otvalor);
                                _p30_cargarClic();
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
                ,failure : function(response)
                {
                    _unmask();
                    errorComunicacion(null,'Error al recuperar cotizaci\u00f3n de tr\u00e1mite');
                }
            });
        }
        catch(e)
        {
            _unmask();
            manejaException(e,ck);
        }
    }
}



function valorOtCorrespondiente(valorOtUtil,tipsitfake,recordBase){
	try{
        if(tipsitfake == 'RQ')
        {           //OTVALORRQ        //OTVALORCR
        	     if(valorOtUtil=='28'){valorOtUtil='55';}
        	else if(valorOtUtil=='29'){valorOtUtil='35';}
        	else if(valorOtUtil=='30'){valorOtUtil='27';}
        	else if(valorOtUtil=='33'){valorOtUtil='28';}
        	else if(valorOtUtil=='34'){valorOtUtil='29';}
        	else if(valorOtUtil=='35'){valorOtUtil='30';}
        	else if(valorOtUtil=='36'){valorOtUtil='62';}
        	else if(valorOtUtil=='37'){valorOtUtil='63';}
        	else if(valorOtUtil=='38'){valorOtUtil='64';}
        	else if(valorOtUtil=='39'){valorOtUtil='65';}
        }else if(tipsitfake == 'TC'){
          	     if(valorOtUtil=='29'){valorOtUtil='52';}
          	else if(valorOtUtil=='30'){valorOtUtil='27';}
          	else if(valorOtUtil=='33'){valorOtUtil='28';}
          	else if(valorOtUtil=='34'){valorOtUtil='29';}
          	else if(valorOtUtil=='35'){valorOtUtil='30';}
          	else if(valorOtUtil=='36'){valorOtUtil='41';}
          	else if(valorOtUtil=='41'){valorOtUtil='53';}
          	else if(valorOtUtil=='44'){valorOtUtil='42';}
          	else if(valorOtUtil=='45'){valorOtUtil='44';}
          	else if(valorOtUtil=='46'){valorOtUtil='33';}
          	else if(valorOtUtil=='47'){valorOtUtil='35';}
          	else if(valorOtUtil=='48'){valorOtUtil='47';}
          	else if(valorOtUtil=='53'){valorOtUtil='55';}
          	else if(valorOtUtil=='54'){valorOtUtil='X';}//DEDUCIBLE DE LA COBERTURA RC ECOLOGÍCA
          	else if(valorOtUtil=='55'){valorOtUtil='62';}
          	else if(valorOtUtil=='56'){valorOtUtil='63';}
          	else if(valorOtUtil=='57'){valorOtUtil='64';}
          	else if(valorOtUtil=='58'){valorOtUtil='65';}
        }
		return valorOtUtil;
	}catch(e){
		debugError(e);
	}
	return listFP;
}
////// funciones //////
<%-- include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp" --%>
</script>
<script type="text/javascript" src="${ctx}/js/proceso/emision/cotizacionAutoFlotillaScript.js?now=${now}"></script>
</head>
<body><div id="_p30_divpri" style="height:1500px;"></div></body>
</html>