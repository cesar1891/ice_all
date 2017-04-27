<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p31_urlPantallaCliente                = '<s:url namespace="/catalogos"  action="includes/personasLoader"            />';
var _p31_urlCotizacionAutoFlotilla         = '<s:url namespace="/emision"    action="cotizacionAutoFlotilla"             />';
var _p31_urlCargarDatosComplementarios     = '<s:url namespace="/emision"    action="cargarDatosComplementariosAutoInd"  />';
var _p31_urlCargarRetroactividadSuplemento = '<s:url namespace="/emision"    action="cargarRetroactividadSuplemento"     />';
var _p31_urlMovimientoMpoliper             = '<s:url namespace="/emision"    action="movimientoMpoliper"                 />';
var _p31_urlGuardar                        = '<s:url namespace="/emision"    action="guardarComplementariosAutoFlotilla" />';
var _p31_urlRecotizar                      = '<s:url namespace="/emision"    action="recotizarAutoFlotilla"              />';
var _p31_urlEmitir                         = '<s:url namespace="/"           action="emitir"                             />';
var _p31_urlDocumentosPoliza               = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza"            />';
var _p31_urlDocumentosPolizaClon           = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon"        />';
var _p31_urlRecuperacionSimple             = '<s:url namespace="/emision"    action="recuperacionSimple"                 />';
var _p31_urlRecuperacionSimpleLista        = '<s:url namespace="/emision"    action="recuperacionSimpleLista"            />';
var urlReintentarWS                        = '<s:url namespace="/"           action="reintentaWSautos"                   />';
var _urlEnviarCorreo                       = '<s:url namespace="/general"    action="enviaCorreo"                        />';
var _p31_urlCargarCatalogo                 = '<s:url namespace="/catalogos"  action="obtieneCatalogo"                    />';
var _p29_urlObtieneValNumeroSerie          = '<s:url namespace="/emision"    action="obtieneValNumeroSerie"              />';
var _p31_urlCargaMasiva                    = '<s:url namespace="/emision"    action="cargaMasivaFlotillaEmision"         />';
var _p31_urlProcesaCargaMasiva             = '<s:url namespace="/emision"    action="procesarCargaMasivaFlotillaEmision" />';
////// urls //////

////// variables //////
var _p31_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p31_smap1:',_p31_smap1);

var _p31_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p31_flujo:',_p31_flujo);

var _p22_parentCallback         = false;
var _p22_parentCallbackCallback = false;

var _p31_polizaAdicionalesItems = null;
var _p31_incisoColumns          = null;
var _p31_storeIncisos           = null;
var _p31_selectedRecord         = null;
var _p31_ventanaDocs            = null;

var _SWexiper = _p31_smap1.swexiper;
var _paramsRetryWS;
var _mensajeEmail;

var _p31_storeCdtipsit       = null;
var _p31_storeVersionesRamo5 = null;

var _aplicaCobVida = !Ext.isEmpty(_p31_smap1.AplicaCobVida) && _p31_smap1.AplicaCobVida == "S" ? true : false ;
var _FechaMinEdad  = _p31_smap1.FechaMinEdad;
var _FechaMaxEdad  = _p31_smap1.FechaMaxEdad;
////// variables //////

////// dinamicos //////
var _p31_tatrisitFullForms    = [];
var _p31_tatrisitParcialForms = [];
<s:iterator value="imap">
    <s:if test='%{key.substring(0,"tatrisit_full_items_".length()).equals("tatrisit_full_items_")}'>
        _p31_tatrisitFullForms['<s:property value='%{key.substring("tatrisit_full_items_".length())}' />']=
        Ext.create('Ext.form.Panel',
        {
            items : [ <s:property value="value" /> ]
        });
    </s:if>
    <s:if test='%{key.substring(0,"tatrisit_parcial_items_".length()).equals("tatrisit_parcial_items_")}'>
        _p31_tatrisitParcialForms['<s:property value='%{key.substring("tatrisit_parcial_items_".length())}' />']=
        Ext.create('Ext.form.Panel',
        {
            itemId       : '_p31_tatrisitParcialForm<s:property value='%{key.substring("tatrisit_parcial_items_".length())}' />'
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
                    ,handler : function(bot) { _p31_editarAutoAceptar(bot); }
                }
            ]
        });
    </s:if>
</s:iterator>
////// dinamicos //////

Ext.onReady(function()
{
	debug('TIMEOUT ANTES DE SETEAR: ', Ext.Ajax.timeout);
	
	// Se aumenta el timeout para todas las peticiones:
	Ext.Ajax.timeout = 30*60*1000; // 30 min
//	Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
//	Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
//	Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	debug('TIMEOUT DESPUES DE SETEAR: ', Ext.Ajax.timeout);
    ////// modelos //////
    Ext.define('_p31_polizaModelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {
                type        : 'date'
                ,dateFormat : 'd/m/Y'
                ,name       : 'feini'
            }
            ,{
                type        : 'date'
                ,dateFormat : 'd/m/Y'
                ,name       : 'fefin'
            }
            ,<s:property value="imap.polizaFields" />
            ,<s:property value="imap.agenteFields" />
            <s:if test='%{getImap().get("polizaAdicionalesFields")!=null}'>
                ,<s:property value="imap.polizaAdicionalesFields" />
            </s:if>
        ]
    });
    
    Ext.define('_p31_modeloDetalleCotizacion',
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
        ]
    });
    
    Ext.define('_p31_modelo',
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
            ,'cdtipsit','nmsituac'
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p31_storeIncisos = Ext.create('Ext.data.Store',
    {
        model : '_p31_modelo'
    });
    
    _p31_storeCdtipsit = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p31_storeCdtipsit'
        ,cargado  : false
        ,autoLoad : true
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p31_urlCargarCatalogo
            ,extraParams :
            {
                'catalogo'         : 'TIPSIT'
                ,'params.idPadre'  : _p31_smap1.cdramo
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
                    _fieldById('_p31_gridIncisos').getView().refresh();
                    _fieldById('_p31_panelStoreCdtipsit').destroy();
                    debug('### cdtipsit:',records);
                }
                else
                {
                    _fieldById('_p31_panelStoreCdtipsit').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    
    _p31_storeVersionesRamo5 = Ext.create('Ext.data.Store',
    {
        model     : 'Generic'
        ,storeId  : '_p31_storeVersionesRamo5'
        ,cargado  : false
        ,autoLoad : _p31_smap1.cdramo+'x'=='5x'
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p31_urlCargarCatalogo
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
	                _fieldById('_p31_gridIncisos').getView().refresh();
	                _fieldById('_p31_panelStoreVersiones').destroy();
                }
                else
                {
                    _fieldById('_p31_panelStoreVersiones').setBodyStyle('border:2px solid red;');
                }
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    _p31_polizaAdicionalesItems =
    [
        <s:property value="imap.agenteItems" />
        ,{
            xtype       : 'datefield'
            ,format     : 'd/m/Y'
            ,fieldLabel : 'INICIO DE VIGENCIA'
            ,name       : 'feini'
            ,style      : 'margin:5px;'
            ,listeners  :
            {
                change : _p31_feiniChange
            }
        }
        ,{
            xtype       : 'datefield'
            ,format     : 'd/m/Y'
            ,fieldLabel : 'FIN DE VIGENCIA'
            ,name       : 'fefin'
            ,style      : 'margin:5px;'
        }
    ];
    <s:if test='%{getImap().get("polizaAdicionalesItems")!=null}'>
        var aux=[<s:property value="imap.polizaAdicionalesItems" />];
        for(var i=0;i<_p31_polizaAdicionalesItems.length;i++)
        {
            aux.push(_p31_polizaAdicionalesItems[i]);
        }
        _p31_polizaAdicionalesItems = aux;
    </s:if>
    for(var i=0;i<_p31_polizaAdicionalesItems.length;i++)
    {
        _p31_polizaAdicionalesItems[i].labelWidth=295;
    }
    
    _p31_incisoColumns =
    [
        {
            dataIndex     : 'nmsituac'
            ,menuDisabled : true
            ,sortable     : false
            ,width        : 50
        }
    ];
    var _p31_incisoColumnsConf = [];
    <s:if test='%{getImap().get("gridColumns")!=null}'>
        _p31_incisoColumnsConf = [<s:property value="imap.gridColumns" />];
    </s:if>
    for(var i in _p31_incisoColumnsConf)
    {
        _p31_incisoColumns.push(_p31_incisoColumnsConf[i]);
    }
    
    var _p31_datosGeneralesItems = [<s:property value="imap.polizaItems" />];
    for(var i=0;i<_p31_datosGeneralesItems.length;i++)
    {
        _p31_datosGeneralesItems[i].labelWidth=295;
    }
    
    var panelesPrincipales =
    [
            Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p31_panelFlujo'
                ,title       : 'ACCIONES'
                ,hidden      : Ext.isEmpty(_p31_flujo)
                ,buttonAlign : 'left'
                ,buttons     : []
                ,listeners   :
                {
                    afterrender : function(me)
                    {
                        if(!Ext.isEmpty(_p31_flujo))
                        {
                            _cargarBotonesEntidad(
                                _p31_flujo.cdtipflu
                                ,_p31_flujo.cdflujomc
                                ,_p31_flujo.tipoent
                                ,_p31_flujo.claveent
                                ,_p31_flujo.webid
                                ,me.itemId//callback
                                ,_p31_flujo.ntramite
                                ,_p31_flujo.status
                                ,_p31_flujo.cdunieco
                                ,_p31_flujo.cdramo
                                ,_p31_flujo.estado
                                ,_p31_flujo.nmpoliza
                                ,_p31_flujo.nmsituac
                                ,_p31_flujo.nmsuplem
                                ,null//callbackDespuesProceso
                            );
                        }
                    }
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p31_polizaForm'
                ,title    : 'DATOS DE P&Oacute;LIZA'
                ,defaults : { style : 'margin:5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                    ,tdAttrs : { valign : 'top' }
                }
                ,items    :
                [
                    {
                        xtype  : 'fieldset'
                        ,title : '<span style="font:bold 14px Calibri;">DATOS GENERALES</span>'
                        ,items : _p31_datosGeneralesItems
                    }
                    ,{
                        xtype  : 'fieldset'
                        ,title : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES</span>'
                        ,items : _p31_polizaAdicionalesItems
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId    : '_p31_gridIncisos'
                ,title    : 'DATOS ADICIONALES DE INCISO'
                ,columns  : _p31_incisoColumns
                ,store    : _p31_storeIncisos
                ,height   : 200
                ,bbar     :
                    [
						'->'
					   ,{
						    xtype  : 'form'
						   ,hidden : _p31_smap1.tipoflot+'x'!='Fx' && _p31_smap1.tipoflot+'x'!='Px'
						   ,layout : 'hbox'
						   ,items  :
						   [
							   {
								    xtype         : 'filefield'
								    ,buttonOnly   : true
								    ,style        : 'margin:0px;'
								    ,name         : 'excel'
								    ,style        : 'background:#223772;'
								    ,buttonConfig :
								    {
								        text   : 'Cargar layout complementario...'
								        ,icon  : '${ctx}/resources/fam3icons/icons/book_next.png'
								        ,hidden: _p31_smap1.cdtipsit2==TipoSituacion.TuristaLicencia
								    }
								    ,listeners :
								    {
								        change : function(me)
		                                { 
								            var indexofPeriod = me.getValue().lastIndexOf("."),
		                                    uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
		                                    debug('uploadedExtension:',uploadedExtension);
		                                    var valido=Ext.Array.contains(['xls','xlsx'], uploadedExtension);
		                                    if(!valido)
		                                    {
		                                        mensajeWarning('Solo se permiten hojas de c&aacute;lculo');
		                                        me.reset();
		                                    }
		                                    if(valido&&_p31_smap1.cdramo+'x'!='5x')
		                                    {
	                                            mensajeWarning('Solo aplica para negocio de Autos');
	                                            me.reset();
		                                    }
		                                    
								            var timestamp = new Date().getTime();
		                                    me.up('form').submit(
                                            {
                                                 url      : _p31_urlCargaMasiva
                                                ,params   :
                                                {
                                                     'smap1.timestamp' : timestamp
                                                    ,'smap1.accion'  : 'guardar'
                                                },success : function()
                                                {
                                                    var json =
                                                    {
                                                        slist1 : []
                                                    };        
                                                                                            
                                                    for(var itera in _p31_storeIncisos.data.items)
                                                    {
                                                        json.slist1.push(_p31_storeIncisos.data.items[itera].raw);
                                                    }
                                                    
                                                    var conceptos = {};
                                                    
                                                    conceptos['timestamp'] = timestamp;
                                                    conceptos['accion']    = 'guardar';
                                                    
                                                    _fieldById('_p31_panelpri').setLoading(true);
                                                    Ext.Ajax.request(
                                                    {
                                                        url       : _p31_urlProcesaCargaMasiva
                                                        ,jsonData :
                                                        {
                                                             smap1  : conceptos
                                                            ,olist1 : json.slist1
                                                        }
                                                        ,success  : function(response)
                                                        {
                                                        	_fieldById('_p31_panelpri').setLoading(false);
                                                            var json=Ext.decode(response.responseText);
                                                            debug('respuesta al cargar el archivo para complementar',json);
                                                            if(json.exito)
                                                            {
                                                                for(var itera in json.olist1)//_p31_storeIncisos.data.items)
                                                                {
                                                                    _p31_storeIncisos.data.items[itera].raw = json.olist1[itera];
                                                                } 
                                                    
                                                                var iterando = 0;
                                                                _p31_storeIncisos.each(function(record)
                                                                {
                                                                    record.data = _p31_storeIncisos.data.items[iterando].raw;
                                                                    iterando++;
                                                                });
                                                                _p31_guardar(_fieldById('_p31_gridIncisos').getView().refresh(true));
                                                            }
                                                            else
                                                            {
                                                                mensajeError(json.respuesta);
                                                            }
                                                        }
                                                        ,failure  : function()
                                                        {
                                                            _fieldById('_p31_panelpri').setLoading(false);
                                                            errorComunicacion();
                                                        }
                                                    });
                                                }
                                                ,failure : function()
                                                {
                                                    _fieldById('_p31_panelpri').setLoading(false);
                                                    errorComunicacion();
                                                }
                                            });
		                                }
								    }
							   }
						   ]
						}
					]
                ,selModel :
                {
                    selType        : 'checkboxmodel'
                    ,allowDeselect : true
                    ,mode          : 'SINGLE'
                    ,listeners     :
                    {
                        selectionchange : function(me,selected,eOpts)
                        {
                            debug('selectionchange:',selected,_p31_tatrisitParcialForms);
                            for(var i in _p31_tatrisitParcialForms)
                            {
                                _p31_tatrisitParcialForms[i].hide();
                            }
                            if(selected.length==1)
                            {
                                _p31_selectedRecord = selected[0];
                                var cdtipsit = _p31_selectedRecord.get('cdtipsit');
                                _p31_tatrisitParcialForms[cdtipsit].getForm().loadRecord(_p31_selectedRecord);
                                _p31_tatrisitParcialForms[cdtipsit].show();
                                _p31_tatrisitParcialForms[cdtipsit].items.items[0].focus();
                            }
                        }
                    }
                }
            })
    ];
    
    for(var i in _p31_tatrisitParcialForms)
    {
        panelesPrincipales.push(_p31_tatrisitParcialForms[i]);
    }
    
    panelesPrincipales.push
    (
        Ext.create('Ext.panel.Panel',
        {
            itemId      : '_p31_clientePanel'
            ,title      : 'CLIENTE'
            ,height     : 400
            ,autoScroll : true
            ,loader     :
            {
                url       : _p31_urlPantallaCliente
                ,scripts  : true
                ,autoLoad : false
                ,ajaxOptions: {
		                            method: 'POST'
		                     }
            }
        })
        ,Ext.create('Ext.panel.Panel',
        {
            itemId       : '_p31_panelBotones'
            ,border      : 0
            ,buttonAlign : 'center'
            ,buttons     :
            [
                {
                    itemId   : '_p31_botonEmitir'
                    ,text    : 'Emitir'
                    ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                    ,handler : _p31_emitirClic
                }
                ,{
                    itemId   : '_p31_botonGuardar'
                    ,text    : 'Guardar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                    ,handler : function(){ _p31_guardar(); }
                }
                ,{
                    text     : 'Nueva'
                    ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                    ,hidden  : !Ext.isEmpty(_p31_flujo)
                    ,handler : _p31_nuevaClic
                }
            ]
        })
    );
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p31_panelpri'
        ,renderTo : '_p31_divpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    : panelesPrincipales
    });
    
    _p31_ventanaDocs=Ext.create('Ext.window.Window',
    {
        title           : 'Documentaci&oacute;n'
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
            ,url      : _p31_urlDocumentosPolizaClon
            ,params   :
            {
                'smap1.cdunieco'       : _p31_smap1.cdunieco
                ,'smap1.cdramo'        : _p31_smap1.cdramo
                ,'smap1.estado'        : _p31_smap1.estado
                ,'smap1.nmpoliza'      : ''
                ,'smap1.nmsuplem'      : '0'
                ,'smap1.nmsolici'      : ''
                ,'smap1.ntramite'      : _p31_smap1.ntramite
                ,'smap1.tipomov'       : '0'
                ,'smap1.ocultarRecibo' : ''
            }
        }
    }).showAt(500,0);
    _p31_ventanaDocs.collapse();
    ////// contenido //////
    
    ////// custom //////
    try{
    	var feini=_fieldByName("feini",null,true);
    	var fefin=_fieldByName("fefin",null,true);
    	
	    feini.on(
	    {
	        change : function(me,val,oldVal)
	        {
	            try
	            {
	            	
	            	
	            	var date1 = oldVal;
	            	var date2 = fefin.getValue();
	            	var timeDiff = Math.abs(date2.getTime() - date1.getTime());
	            	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
	                fefin.setValue(Ext.Date.add(val,Ext.Date.DAY,diffDays))
	            }
	            catch(e)
	            {
	                debug(e);
	            }
	        }
	    });
	    fefin.setReadOnly(true);
    }catch(e){
    	debugError(e)
    }
    /*_fieldByLabel('AGENTE').hide();*/
    _fieldByName('porparti').setMaxValue(99);
    
    //ramo 5
    if(_p31_smap1.cdramo+'x'=='5x')
    {
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p31_panelStoreCdtipsit'
            ,floating : true
            ,html     : 'Cargando situaciones...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,330);
    
        Ext.create('Ext.panel.Panel',
        {
            itemId    : '_p31_panelStoreVersiones'
            ,floating : true
            ,html     : 'Cargando versiones...'
            ,width    : 200
            ,height   : 30
            ,frame    : true
        }).showAt(770,370);
        
        //pai
        if(_p31_smap1.tipoflot+'x'!='Fx')
        {
            _fieldLikeLabel('PARA PAI').allowBlank=true;
            _fieldLikeLabel('PARA PAI').hide();
        }
        //pai
        
        // y o
        if(_p31_smap1.tipoflot+'x'=='Px')
        {
            var yos = Ext.ComponentQuery.query('[fieldLabel=Y/O]');
            for(var i in yos)
            {
                yos[i].hide();
            }
        }
        // y o
        
        //RPF
        var campoRPF = _fieldByName("parametros.pv_otvalor17",_p31_polizaAdicionalesItems,true);
        if (!Ext.isEmpty(campoRPF))
    	{
    	   campoRPF.hide();
    	   campoRPF.setMinValue(1);
    	   if(campoRPF.getValue() == 0);
    	    {
    	    	campoRPF.setValue(null);
    	    }
    	    campoRPF.on(
    	    	    {
    	    	        change : function(me,val,oldVal)
    	    	        {
    	    	            try
    	    	            {
    	    	            	if(val == 0)
    	    	            		{
    	    	            		 me.setValue(null);
    	    	            		}
    	    	            }
    	    	            catch(e)
    	    	            {
    	    	                debug(e);
    	    	            }
    	    	        }
    	    	    });
    	}//RPF
    	
    }//ramo 5
    
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />
    ////// custom //////
    
    ////// loaders //////
    Ext.Ajax.request(
    {
        url     : _p31_urlCargarDatosComplementarios
        ,params :
        {
            'smap1.cdunieco'  : _p31_smap1.cdunieco
            ,'smap1.cdramo'   : _p31_smap1.cdramo
            ,'smap1.estado'   : _p31_smap1.estado
            ,'smap1.nmpoliza' : _p31_smap1.nmpoliza
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### cargar datos complementarios:',json);
            if(json.exito)
            {
                var form   = _fieldById('_p31_polizaForm');
                var record = new _p31_polizaModelo(json.smap1);
                debug('record:',record);
                form.loadRecord(record);
                
                if(json.smap1.agente_sec+'x'!='x')
                {
                    var age2 = _fieldByName('agente_sec');
                    age2.getStore().load(
                    {
                        params :
                        {
                            'params.agente' : json.smap1.agente_sec
                        }
                        ,callback : function()
                        {
                            age2.setValue(age2.findRecord('key',json.smap1.agente_sec));
                        }
                    });
                }
                
                _fieldById('_p31_clientePanel').loader.load(
                {
                    params:
                    {
                        'smap1.cdperson' : json.smap1.cdperson,
                        'smap1.cdideper' : json.smap1.cdideper,
                        'smap1.cdideext' : json.smap1.cdideext,
                        'smap1.esSaludDanios' : 'D',
                        'smap1.polizaEnEmision': 'S',
                        'smap1.esCargaClienteNvo' :(Ext.isEmpty(json.smap1.cdperson)? 'S' : 'N' ),
                        'smap1.cargaCP' : json.smap1.cdpostal,
                        'smap1.cargaTipoPersona' : json.smap1.otfisjur,
                        'smap1.cargaSucursalEmi' : _p31_smap1.cdunieco,
	                    'smap1.cargaFenacMin' : _aplicaCobVida?_FechaMinEdad:'',
	                    'smap1.cargaFenacMax' : _aplicaCobVida?_FechaMaxEdad:'',
	                    'smap1.tomarUnDomicilio' : 'S',
	                    'smap1.cargaOrdDomicilio' : json.smap1.nmorddom,
	                    'smap1.muestraBusqueda' : _p31_smap1.cdtipsit2 == "TL" || _p31_smap1.tipoflot=="F"?'S':'N' 
                    }
                });
                
                _p22_parentCallback = _p31_personaSaved;
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    
    Ext.Ajax.request(
    {
        url     : _p31_urlRecuperacionSimpleLista
        ,params :
        {
            'smap1.procedimiento' : 'RECUPERAR_TVALOSIT'
            ,'smap1.cdunieco'     : _p31_smap1.cdunieco
            ,'smap1.cdramo'       : _p31_smap1.cdramo
            ,'smap1.estado'       : _p31_smap1.estado
            ,'smap1.nmpoliza'     : _p31_smap1.nmpoliza
            ,'smap1.nmsuplem'     : '0'
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### tvalosit:',json);
            if(json.exito)
            {
                for(var i in json.slist1)
                {
                    json.slist1[i]['cdtipsit']=json.slist1[i].CDTIPSIT;
                    json.slist1[i]['nmsituac']=json.slist1[i].NMSITUAC;
                    _p31_storeIncisos.add(new _p31_modelo(json.slist1[i]));
                }
                _p31_loadCallback();
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    ////// loaders //////
});

////// funciones //////
function _p31_nuevaClic()
{
    _fieldById('_p31_panelpri').setLoading(true);
    Ext.create('Ext.form.Panel').submit(
    {
        url     : _p31_urlCotizacionAutoFlotilla
        ,params :
        {
            'smap1.cdramo'    : _p31_smap1.cdramo
            ,'smap1.cdtipsit' : _p31_smap1.cdtipsit2!='TL'?_p31_smap1.cdtipsit:"ARTL"
            ,'smap1.tipoflot' : _p31_smap1.tipoflot
        }
        ,standardSubmit : true
    });
}

function _p31_loadCallback()
{
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    /*var vigen = _fieldByLabel('VIGENCIA');
    vigen.hide();
    feini.on(
    {
        change : function(me,val)
        {
            try
            {
                fefin.setValue(Ext.Date.add(val,Ext.Date.DAY,vigen.getValue()))
            }
            catch(e)
            {
                debug(e);
            }
        }
    });*/
    
    Ext.Ajax.request(
    {
        url     : _p31_urlCargarRetroactividadSuplemento
        ,params :
        {
            'smap1.cdunieco'  : _p31_smap1.cdunieco
            ,'smap1.cdramo'   : _p31_smap1.cdramo
            ,'smap1.cdtipsup' : 1
            ,'smap1.cdusuari' : _p31_smap1.cdusuari
            ,'smap1.cdtipsit' : _p31_smap1.cdtipsit
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### obtener retroactividad:',json);
            if(json.exito)
            {
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

function _p31_personaSaved(json)
{
    debug('>_p31_personaSaved');
    Ext.Ajax.request(
    {
        url     : _p31_urlMovimientoMpoliper
        ,params :
        {
            'smap1.cdunieco'  : _p31_smap1.cdunieco
            ,'smap1.cdramo'   : _p31_smap1.cdramo
            ,'smap1.estado'   : _p31_smap1.estado
            ,'smap1.nmpoliza' : _p31_smap1.nmpoliza
            ,'smap1.nmsituac' : '0'
            ,'smap1.cdrol'    : '1'
            ,'smap1.cdperson' : json.smap1.CDPERSON
            ,'smap1.nmsuplem' : '0'
            ,'smap1.status'   : 'V'
            ,'smap1.nmorddom' : json.smap1.NMORDDOM
            ,'smap1.accion'   : 'I'
            ,'smap1.swexiper' : _SWexiper
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### mpoliper:',json);
            if(json.exito)
            {
                if(!Ext.isEmpty(_p22_parentCallbackCallback))
                {
                    _p22_parentCallbackCallback();
                    _p22_parentCallbackCallback=null;
                }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    debug('<_p31_personaSaved');
}

function _p31_emitirClic()
{
    debug('>_p31_emitirClic');
    
    _p31_guardar(_p31_mostrarVistaPrevia);
    
    debug('<_p31_emitirClic');
}

function _p31_guardar(callback)
{
    debug('>_p31_guardar');
    _p22_parentCallbackCallback = null;
    var form1  = _fieldById('_p31_polizaForm');
    var valido = form1.isValid();
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        var error        = '';
        var agregarError = function(cadena,nmsituac)
        {
            valido = false;
            error  = error + 'Inciso ' + nmsituac + ': ' + cadena +'</br>';
        };
        _p31_storeIncisos.each(function(record)
        {
            var cdtipsit = record.get('cdtipsit');
            var nmsituac = record.get('nmsituac');
            
            if(!Ext.isEmpty(cdtipsit))
            {
                var itemsObliga = Ext.ComponentQuery.query('[swobligaemiflot=true]',_p31_tatrisitFullForms[cdtipsit]);
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
        valido = _fieldByName('_p22_formBusqueda').isValid();
        if(!valido)
        {
            mensajeWarning('Falta registrar un cliente');
        }
    }
    
    if(valido)
    {
        _p22_parentCallbackCallback = function()
        {
            var json =
            {
                smap1   : form1.getValues()
                ,slist1 : []
            };
        
            json.smap1['cdunieco'] = _p31_smap1.cdunieco;
            json.smap1['cdramo']   = _p31_smap1.cdramo;
            json.smap1['estado']   = _p31_smap1.estado;
            json.smap1['ntramite'] = _p31_smap1.ntramite;
            
            _p31_storeIncisos.each(function(record)
            {
                json.slist1.push(record.data);
            });
            
            debug('json para guardar:',json);
            var panelPri = _fieldById('_p31_panelpri');
    
            panelPri.setLoading(true);
            Ext.Ajax.request(
            {
                url       : _p31_urlGuardar
                ,jsonData : json
                ,success  : function(response)
                {
                    panelPri.setLoading(false);
                    var json = Ext.decode(response.responseText);
                    debug('### guardar:',json);
                    if(json.exito)
                    {
                        if(callback)
                        {
                            callback();
                        }
                        else
                        {
                            mensajeCorrecto('Datos guardados',json.respuesta);
                        }
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure  : function()
                {
                    panelPri.setLoading(false);
                    errorComunicacion();
                }
            });
        };
        _fieldByName('_p22_botonGuardar').handler();
    }
    
    debug('<_p31_guardar');
}

function _p31_mostrarVistaPrevia()
{
    debug('>_p31_mostrarVistaPrevia');
    var panelpri = _fieldById('_p31_panelpri');
    var numSerie = '';
    
    debug('TIMEOUT ANTES DE RECOTIZAR: ', Ext.Ajax.timeout);
    
     //se agrega para validar numero de serie
    _p31_storeIncisos.each(function(record) {
    	debug( record.get('parametros.pv_otvalor37'));
    	numSerie+=''+(record.get('parametros.pv_otvalor37'))+'|';
    	//console.log( record.get('parametros.pv_otvalor37') );
    });
    
    panelpri.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p31_urlRecotizar
        ,params :
        {
            'smap1.cdunieco'    : _p31_smap1.cdunieco
            ,'smap1.cdramo'     : _p31_smap1.cdramo
            ,'smap1.cdtipsit'   : _p31_smap1.cdtipsit
            ,'smap1.estado'     : 'W'
            ,'smap1.nmpoliza'   : _p31_smap1.nmpoliza
            ,'smap1.notarifica' : 'si'
            ,'smap1.cdperpag'   : _fieldByName('cdperpag').getValue()
        }
        ,success : function(response)
        {
            panelpri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### retarificar:',json);
            if(json.exito)
            {
            	var tarifaFinal = Ext.create('Ext.window.Window',{
                title       : 'Tarifa final'
                ,width      : 660
                ,maxHeight  : 500
                ,autoScroll : true
                ,modal      : true
                ,closable   : false
                ,items      :
                [
                    Ext.create('Ext.grid.Panel',
                    {
                        store    : Ext.create('Ext.data.Store',
                        {
                            model       : '_p31_modeloDetalleCotizacion'
                            ,groupField : 'TITULO'
                            ,sorters    :
                            [
                                {
                                    sorterFn : function(o1,o2)
                                    {
                                        debug('sorting:',o1,o2);
                                        try{
                                        	if(_p31_smap1.cdtipsit2=='TL'){
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
                                        	}
                                        }catch(e){
                                        	debugError(e)
                                        }
                                        if (o1.get('COBERTURA') == o2.get('COBERTURA'))
                                        {
                                            return 0;
                                        }
                                        return o1.get('COBERTURA') < o2.get('COBERTURA') ? -1 : 1;
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
                                ,width           : 480
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
                                ,ftype          : 'groupingsummary'
                                ,startCollapsed :!RolSistema.puedeSuscribirAutos(_p31_smap1.cdsisrol)// _p31_smap1.cdsisrol!='SUSCRIAUTO'
                                ,listeners      :
                                {
                                    groupexpand : function(view,node,group)
                                    {
                                        if(!RolSistema.puedeSuscribirAutos(_p31_smap1.cdsisrol))
                                        {
                                            this.collapseAll();
                                        }
                                    }
                                }                                    
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
                                        sum += parseFloat(json.slist1[i].PRIMA);
                                    }
                                    this.setText('Total: '+ Ext.util.Format.usMoney(sum));
                                    this.callParent();
                                }
                            })
                        ]
                    })
                    ,Ext.create('Ext.form.Panel',
                    {
                        layout :
                        {
                            type     : 'table'
                            ,columns : 5
                        }
                        ,defaults : { style : 'margin:5px;' }
                        ,items    :
                        [
                            {
                                xtype       : 'textfield'
                                ,itemId     : '_p31_numerofinalpoliza'
                                ,fieldLabel : 'N&uacute;mero de poliza'
                                ,readOnly   : true
                            }
                            ,{
                                itemId   : '_p31_botonEmitirPolizaFinal'
                                ,xtype   : 'button'
                                ,text    : 'Emitir'
                                ,icon    : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
                                ,handler : _p31_emitirFinal
                            },{
                                itemId : 'botonEnvioEmail'
                                ,xtype : 'button'
                                ,text  : 'Enviar Email'
                                ,icon  : '${ctx}/resources/fam3icons/icons/email.png'
                                ,disabled: true
                                ,handler:function()
                                {
                                    Ext.Msg.prompt('Envio de Email', 'Escriba los correos que recibir&aacute;n la documentaci&oacute;n (separados por ;)', 
                                    function(buttonId, text){
                                        if(buttonId == "ok" && !Ext.isEmpty(text)){
                                            
                                            if(Ext.isEmpty(_mensajeEmail)){
                                                mensajeError('Mensaje de Email sin contenido. Consulte a Soporte T&eacute;cnico');
                                                return;
                                            }
                                            
                                            Ext.Ajax.request(
                                                    {
                                                        url : _urlEnviarCorreo,
                                                        params :
                                                        {
                                                            to     : text,
                                                            asunto : 'Documentación de póliza de Autos',
                                                            mensaje: _mensajeEmail,
                                                            html   : true
                                                        },
                                                        callback : function(options,success,response)
                                                        {
                                                            if (success)
                                                            {
                                                                var json = Ext.decode(response.responseText);
                                                                if (json.success == true)
                                                                {
                                                                    Ext.Msg.show(
                                                                    {
                                                                        title    : 'Correo enviado'
                                                                        ,msg     : 'El correo ha sido enviado'
                                                                        ,buttons : Ext.Msg.OK
                                                                        ,icon    : 'x-message-box-ok'
                                                                        ,fn      : function()
                                                                        {
                                                                            _generarRemesaClic(
                                                                                false
                                                                                ,_p31_smap1.cdunieco
                                                                                ,_p31_smap1.cdramo
                                                                                ,'M'
                                                                                ,_p31_smap1.nmpolizaEmitida
                                                                                ,function(){}
                                                                                ,'S'
                                                                            );
                                                                        }
                                                                    });
                                                                }
                                                                else
                                                                {
                                                                    mensajeError('Error al enviar el correo');
                                                                }
                                                            }
                                                            else
                                                            {
                                                                errorComunicacion();
                                                            }
                                                        }
                                                    });
                                        
                                        }else {
                                            mensajeWarning('Introduzca al menos una direcci&oacute;n de email');    
                                        }
                                    })
                                }
                            }
                            ,{
                                itemId : 'botonReenvioWS'
                                ,xtype : 'button'
                                ,text  : 'Reintentar Emisi&oacute;n'
                                ,icon  : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
                                ,disabled: true
                                ,handler:function()
                                {
                                    var me=this;
                                    reintentarWSAuto(me.up().up(), _paramsRetryWS);
                                }
                            }
                            ,{
                                xtype     : 'button'
                                ,itemId   : '_p31_botonDocumentosPolizaEmitida'
                                ,text     : 'Imprimir'
                                ,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
                                ,disabled : true
                                ,handler  : function(me)
                                {
                                    var callbackRemesa = function()
                                    {
                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                                        {
                                            title        : 'Documentos del tr&aacute;mite'
                                            ,modal       : true
                                            ,buttonAlign : 'center'
                                            ,width       : 600
                                            ,height      : 400
                                            ,autoScroll  : true
                                            ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                            ,loader      :
                                            {
                                                url       : _p31_urlDocumentosPoliza
                                                ,params   :
                                                {
                                                    'smap1.nmpoliza'  : _p31_smap1.nmpolizaEmitida
                                                    ,'smap1.cdunieco' : _p31_smap1.cdunieco
                                                    ,'smap1.cdramo'   : _p31_smap1.cdramo
                                                    ,'smap1.estado'   : 'M'
                                                    ,'smap1.nmsuplem' : '0'
                                                    ,'smap1.ntramite' : ''
                                                    ,'smap1.nmsolici' : _p31_smap1.nmpoliza
                                                    ,'smap1.tipomov'  : '0'
                                                }
                                                ,scripts  : true
                                                ,autoLoad : true
                                            }
                                        }).show());
                                    };
                                    _generarRemesaClic(
                                        false
                                        ,_p31_smap1.cdunieco
                                        ,_p31_smap1.cdramo
                                        ,'M'
                                        ,_p31_smap1.nmpolizaEmitida
                                        ,callbackRemesa
                                    );
                                }
                            }
                            ,{
                                xtype    : 'button'
                                ,itemId  : '_p31_botonCancelarEmision'
                                ,text    : 'Cancelar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                ,handler : function()
                                {
                                    var me=this;
                                    me.up().up().destroy();
                                }
                            }
                            ,{
                                xtype     : 'button'
                                ,itemId   : '_p31_botonNueva'
                                ,text     : 'Nueva'
                                ,icon     : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                ,disabled : true
                                ,hidden   : !Ext.isEmpty(_p31_flujo)
                                ,handler  : function(){ _p31_nuevaClic(); }
                            }
							,{
                                itemId   : '_p31_botonMesaControl'
                                ,xtype   : 'button'
                                ,text    : 'Mesa de control'
                                ,icon    : '${ctx}/resources/fam3icons/icons/house.png'
                                ,hidden  : Ext.isEmpty(_p31_flujo)
                                ,handler : function(){
                                	_mask('Redireccionando...');
                                	Ext.create('Ext.form.Panel').submit({
                                		standardSubmit : true
                                		,url           : _GLOBAL_COMP_URL_MCFLUJO
                                		}); 	
                                	} 
                            }
                        ]
                    })
                ]
            });
        	if(!Ext.isEmpty(numSerie)){
        		Ext.Ajax.request(
					{
						url     : _p29_urlObtieneValNumeroSerie
						,params :
						{
							'smap1.numSerie'  : numSerie
							,'smap1.feini'   : _fieldByName('feini').getValue()
						}
						,success : function(response)
						{
							var jsonNumSerie=Ext.decode(response.responseText);
			    	    	if(jsonNumSerie.exito!=true)
				        	    	{
				        	    		 if(!RolSistema.puedeSuscribirAutos(_p31_smap1.cdsisrol))
				        	    		 {
				        	    		 		mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", jsonNumSerie.respuesta);
					        	    			
					        				}else{
					        					centrarVentanaInterna(tarifaFinal.show());
					        					mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", jsonNumSerie.respuesta);
    
					        				}
					        	    		
				        	    	} else {
				        	    		centrarVentanaInterna(tarifaFinal.show());
				        	    	}
			    	    	
						}
						,failure : errorComunicacion
					});
        		
        	}else{
            		centrarVentanaInterna(tarifaFinal.show());
            		
            	}
            	
            		
            
            }
            else
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    
   
	

    
    debug('<_p31_mostrarVistaPrevia');
}

function _p31_emitirFinal(me)
{
    debug('>_p31_emitirFinal');
    var panelpri = me.up().up();
    panelpri.setLoading(true);
    
    debug('TIMEOUT ANTES DE EMITIR: ', Ext.Ajax.timeout);
    Ext.Ajax.request(
    {
        url      : _p31_urlEmitir
        ,params  :
        {
            'panel1.pv_ntramite'      : _p31_smap1.ntramite
            ,'panel2.pv_cdunieco'     : _p31_smap1.cdunieco
            ,'panel2.pv_cdramo'       : _p31_smap1.cdramo
            ,'panel2.pv_estado'       : _p31_smap1.estado
            ,'panel1.pv_nmpoliza'     : _p31_smap1.nmpoliza
            ,'panel2.pv_nmpoliza'     : _p31_smap1.nmpoliza
            ,'panel2.pv_cdtipsit'     : _p31_smap1.cdtipsit
            ,'panel1.flotilla'        : _p31_smap1.cdtipsit2=='TL'?null:'si'
            ,'panel1.tipoGrupoInciso' : _p31_smap1.cdtipsit2=='TL'?null:'C'
        }
        ,success:function(response)
        {
            panelpri.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### emitir:',json);
            if(json.success==true)
            {
            	_p31_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                debug("_p31_smap1.nmpolizaEmitida:",_p31_smap1.nmpolizaEmitida);
                
            	if(json.retryRec){
                    _fieldById('_p31_botonEmitirPolizaFinal').hide();
                    _fieldById('_p31_botonCancelarEmision').setDisabled(true);
                    _fieldById('_p31_botonNueva').setDisabled(false);
                    
                    Ext.Msg.show(
	                {
	                    title    :'Aviso'
	                    ,msg     : 'Error en la emisi&oacute;n. No se pudo emitir la p&oacute;liza.'
	                    ,buttons : Ext.Msg.OK
	                    ,icon    : Ext.Msg.WARNING
	                    ,fn      : function()
	                    {
	                            var paramsWS = {
	                                 'panel1.pv_nmpoliza'  : _p31_smap1.nmpoliza
	                                ,'panel1.pv_ntramite' : _p31_smap1.ntramite
	                                ,'panel2.pv_cdramo'   : _p31_smap1.cdramo
	                                ,'panel2.pv_cdunieco' : _p31_smap1.cdunieco
	                                ,'panel2.pv_estado'   : _p31_smap1.estado
	                                ,'panel2.pv_nmpoliza' : _p31_smap1.nmpoliza
	                                ,'panel2.pv_cdtipsit' : _p31_smap1.cdtipsit
	                                ,'nmpoliza'           : json.nmpoliza
	                                ,'nmsuplem'           : json.nmsuplem                                                                       
	                                ,'cdIdeper'           : json.cdIdeper
	                                ,'panel1.flotilla'    : 'si'
	                                ,'panel1.tipoGrupoInciso' : 'C'
	                                ,'nmpolAlt'           : json.nmpolAlt
	                                ,'sucursalGS'         : json.sucursalGS
	                                ,'cdRamoGS'           : json.cdRamoGS
	                                ,'retryRec'           : json.retryRec
	                            };
	                            reintentarWSAuto(me.up().up(), paramsWS);
	                    }
	                });
	                return;
                }
                
                _fieldById('_p31_numerofinalpoliza').setValue(json.panel2.nmpoliex);
                _fieldById('_p31_botonEmitirPolizaFinal').setDisabled(true);
                _fieldById('_p31_botonDocumentosPolizaEmitida').setDisabled(false);
                _p31_ventanaDocs.destroy();
                
                _fieldById('botonReenvioWS').hide();
                _mensajeEmail = json.mensajeEmail;
                _fieldById('botonEnvioEmail').enable();
                
                _fieldById('_p31_botonCancelarEmision').setDisabled(true);
                _fieldById('_p31_botonNueva').setDisabled(false);
                
                if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
                {
                    var ventanaTmp = Ext.Msg.show(
                    {
                        title    : 'Aviso del sistema'
                        ,msg     : json.mensajeRespuesta
                        ,buttons : Ext.Msg.OK
                        ,icon    : Ext.Msg.WARNING
                        ,fn      : function()
                        {
                            if(!Ext.isEmpty(json.nmpolAlt))
                            {
                                mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
                            }
                        }
                    });
                    centrarVentanaInterna(ventanaTmp);
                }
                else
                {
                    if(!Ext.isEmpty(json.nmpolAlt))
                    {
                        mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
                    }
                }
            }
            else
            {
                if(json.retryWS){
                	
                	_p31_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                	debug("_p31_smap1.nmpolizaEmitida, en reintento WS:" , _p31_smap1.nmpolizaEmitida);
                	
                    _fieldById('_p31_botonEmitirPolizaFinal').hide();
                    _fieldById('_p31_botonCancelarEmision').setDisabled(true);
                    _fieldById('_p31_botonNueva').setDisabled(false);
                }
                Ext.Msg.show(
                {
                    title    :'Error'
                    ,msg     : Ext.isEmpty(json.mensajeRespuesta)?"Error al emitir":json.mensajeRespuesta
                    ,buttons : Ext.Msg.OK
                    ,icon    : Ext.Msg.ERROR
                    ,fn      : function()
                    {
                        if(json.retryWS)
                        {
                            var paramsWS =
                            {
                                'panel1.pv_nmpoliza'  : _p31_smap1.nmpoliza
                                ,'panel1.pv_ntramite' : _p31_smap1.ntramite
                                ,'panel2.pv_cdramo'   : _p31_smap1.cdramo
                                ,'panel2.pv_cdunieco' : _p31_smap1.cdunieco
                                ,'panel2.pv_estado'   : _p31_smap1.estado
                                ,'panel2.pv_nmpoliza' : _p31_smap1.nmpoliza
                                ,'panel2.pv_cdtipsit' : _p31_smap1.cdtipsit
                                ,'nmpoliza'           : json.nmpoliza
                                ,'nmsuplem'           : json.nmsuplem                                                                       
                                ,'cdIdeper'           : json.cdIdeper
                                ,'panel1.flotilla'    : 'si'
                                ,'panel1.tipoGrupoInciso' : 'C'
                            };
                            reintentarWSAuto(me.up().up(), paramsWS);
                        }
                    }
                });
            }
        }
        ,failure:function()
        {
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p31_emitirFinal');
}

//funcion para reintentar WS auto
                
function reintentarWSAuto(loading, params){

    Ext.Msg.show({
       title    :'Confirmaci&oacute;n'
       ,msg     : '&iquest;Desea Reenviar los Web Services de Autos?'
       ,buttons : Ext.Msg.YESNO
       ,icon    : Ext.Msg.QUESTION
       ,fn      : function(boton, text, opt){
        if(boton == 'yes'){
            
            loading.setLoading(true);
            
            Ext.Ajax.request(
                    {
                        url     : urlReintentarWS
                        ,params :params
                        ,success:function(response)
                        {
                            loading.setLoading(false);
                            var json=Ext.decode(response.responseText);
                            debug(json);
                            if(json.success==true)
                            {
                                mensajeCorrecto('Aviso', 'Ejecuci&oacute;n Correcta de Reintento. P&oacute;liza Emitida: ' + json.nmpolAlt);
                                
                                _p31_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                                
                                _fieldById('_p31_numerofinalpoliza').setValue(json.nmpolAlt);
                                _fieldById('_p31_botonDocumentosPolizaEmitida').setDisabled(false);
                                _p31_ventanaDocs.destroy();
                                _fieldById('botonReenvioWS').setDisabled(true);
                                _fieldById('botonReenvioWS').hide();
                                
                                _mensajeEmail = json.mensajeEmail;
                                _fieldById('botonEnvioEmail').enable();
                            }
                            else
                            {
                                Ext.Msg.show({
                                    title    :'Aviso'
                                    ,msg     : json.mensajeRespuesta
                                    ,buttons : Ext.Msg.OK
                                    ,icon    : Ext.Msg.WARNING
                                    ,fn      : function(){
                                        reintentarWSAuto(loading, params);
                                    }
                                });
                            }
                        }
                        ,failure:function()
                        {
                            loading.setLoading(false);
                            Ext.Msg.show({
                                title:'Error',
                                msg: 'Error de comunicaci&oacute;n',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.ERROR
                                ,fn      : function(){
                                    reintentarWSAuto(loading, params);
                                }
                            });
                        }
                    });
        }else{
            _paramsRetryWS = params;
            debug("Habilitando Boton Reenvio WS");
            _fieldById('botonReenvioWS').setDisabled(false);
        }
       }
    });
                        
}

function _p31_feiniChange(comp,val)
{
    debug('_p31_feiniChange:',val);
    var fefin = _fieldByName('fefin');
   // fefin.setMinValue(Ext.Date.add(val,Ext.Date.DAY,1));
    fefin.isValid();
}

function _p31_renderer(record,mapeo)
{
    debug('>_p31_renderer',mapeo,record.data);
    
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
                        var store=_fieldById('_p31_tatrisitParcialForm'+cdtipsit).down('[name='+name+']').getStore();
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
                        label='Error...';//'-sin store-';
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

function _p31_editarAutoAceptar(bot)
{
    debug('>_p31_editarAutoAceptar');
    var form = bot.up('form');
    
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
        var record = _p31_selectedRecord;
        for(var prop in form.getValues())
        {
            record.set(prop,form.getValues()[prop]);
        }
        _fieldById('_p31_gridIncisos').getSelectionModel().deselectAll();
    }
}

////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p31_divpri" style="height:1000px;"></div></body>
</html>