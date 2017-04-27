<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var _URL_CONSULTA_CLAUSULAS_POLIZA = '<s:url namespace="/consultasPoliza" action="consultaClausulasPoliza" />';
    var venExcluUrlCargarDisp   = '<s:url namespace="/" action="obtenerExclusionesPorTipo" />';
    var venExcluUrlGuardar      = '<s:url namespace="/" action="guardarExclusiones" />';
    var venExcluUrlLoadHtml     = '<s:url namespace="/" action="cargarHtmlExclusion" />';
    var venExcluUrlAddExclu     = '<s:url namespace="/endosos" action="guardarEndosoClausulaPaso" />';
    var venExcluUrlAddExcluDetalle   = '<s:url namespace="/" action="agregarExclusionDetalle" />';
    var venExcluUrlSaveHtml     = '<s:url namespace="/" action="guardarHtmlExclusion" />';
    var venExcluUrlCargarTipos  = '<s:url namespace="/" action="cargarTiposClausulasExclusion" />';
    var _endpnx_urlAgregarIcd      = '<s:url namespace="/emision" action="agregarClausulaICD"            />';
    var _endpnx_urlCargarIcdClausu = '<s:url namespace="/emision" action="cargarClausulaICD"             />';
    var _endpnx_urlBorrarIcd       = '<s:url namespace="/emision" action="borrarClausulaICD"             />';
    var venExcluContexto        = '${ctx}';
    var inputCduniecopx         = '<s:property value="smap1.cdunieco" />';
    var inputCdramopx           = '<s:property value="smap1.cdramo" />';
    var inputEstadopx           = '<s:property value="smap1.estado" />';
    var inputNmpolizapx         = '<s:property value="smap1.nmpoliza" />';
    var inputNmsituacpx         = '<s:property value="smap1.nmsituac" />';
    var panendExInput           = [];
    var endnomUrlDoc            = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon" />';
    panendExInput['cdtipsit']   = '<s:property value="smap1.cdtipsit" />';
    panendExInput['ntramite']   = '<s:property value="smap1.ntramite" />';
    panendExInput['nmsuplem']   = '<s:property value="smap1.nmsuplem" />';
    debug('panendExInput',panendExInput);
    var venExcluStoreDisp;
    var venExcluStoreUsa;
    var venExcluStoreTipos;
    var loadExcluTimeoutVar;
    var _2_form;
    
    var _endCla_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
    
    debug('_endCla_flujo:',_endCla_flujo);
    /*///////////////////*/
    ////// variables //////
    ///////////////////////
    
///////////////////////
////// funciones //////
/*///////////////////*/
function _2_documentos()
{
	Ext.create('Ext.window.Window',
    {
        title        : 'Documentos del tr&aacute;mite '+panendExInput['ntramite']
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 600
        ,height      : 400
        ,autoScroll  : true
        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
        ,loader      :
        {
            url       : endnomUrlDoc
            ,params   :
            {
                 'smap1.cdunieco' : inputCduniecopx
                ,'smap1.cdramo'   : inputCdramopx
                ,'smap1.estado'   : inputEstadopx
                ,'smap1.nmpoliza' : inputNmpolizapx
                ,'smap1.nmsuplem' : '0'
                ,'smap1.ntramite' : panendExInput['ntramite']
                ,'smap1.nmsolici' : ''
                ,'smap1.tipomov'  : '0'
            }
            ,scripts  : true
            ,autoLoad : true
        }
    }).show();
}
    
function _2_confirmar()
{
	var boton=this;
	
	var valido=true;
	
	if(valido)
	{
		valido=_2_form.isValid();
		if(!valido)
		{
			datosIncompletos();
		}
	}
	
    if(valido)
    {
    	_setLoading(true,boton);
    	
    	var submitParams =
    	{
            'smap1.pv_cdunieco_i'  : inputCduniecopx
            ,'smap1.pv_cdramo_i'   : inputCdramopx
            ,'smap1.pv_estado_i'   : inputEstadopx
            ,'smap1.pv_nmpoliza_i' : inputNmpolizapx
            ,'smap1.pv_nmsituac_i' : inputNmsituacpx
            ,'smap1.pv_nmsuplem_i' : panendExInput['nmsuplem']
            ,'smap1.pv_ntramite_i' : panendExInput['ntramite']
            ,'smap1.pv_cdtipsit_i' : panendExInput['cdtipsit']
            ,'smap1.confirmar'     : 'si'
            ,'smap1.fecha_endoso'  : Ext.Date.format(Ext.getCmp('_2_fieldFechaId').getValue(),'d/m/Y')
        };
    	
    	if(!Ext.isEmpty(_endCla_flujo))
    	{
    	    submitParams = _flujoToParams(_endCla_flujo,submitParams);
    	}
    	
	    Ext.Ajax.request(
	    {
	        url      : venExcluUrlAddExclu
	        ,timeout : 180000
	        ,params  : submitParams
	        ,success : function (response)
	        {
	            debug('success');
	            _setLoading(false,boton);
	            var json=Ext.decode(response.responseText);
	            if(json.success==true)
	            {
	                var callbackRemesa = function()
	                {
	                    //////////////////////////////////
                        ////// usa codigo del padre //////
                        /*//////////////////////////////*/
                        marendNavegacion(2);
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
	                };
	                
	                mensajeCorrecto('Confirmar endoso',json.mensaje,function()
	                {
	                    _generarRemesaClic(
	                        true
	                        ,inputCduniecopx
	                        ,inputCdramopx
	                        ,inputEstadopx
	                        ,inputNmpolizapx
	                        ,callbackRemesa
	                    );
	                });
	            }
	            else
	            {
	                mensajeError(json.error);
	            }
	        }
	        ,failure : function ()
	        {
	            _setLoading(false,boton);
	            _setLoading(false,me.up().up());
	            Ext.Msg.show({
	                title:'Error',
	                msg: 'Error de comunicaci&oacute;n',
	                buttons: Ext.Msg.OK,
	                icon: Ext.Msg.ERROR
	            });
	        }
	    });
    }
}
/*///////////////////*/
////// funciones //////
///////////////////////
    
Ext.onReady(function(){
    
	Ext.Ajax.timeout = 15*60*1000; // 15 min
	
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('ClausulaModel',{
        extend:'Ext.data.Model',
        fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
    });
    
    Ext.define('ModeloTipoClausula',
    {
        extend:'Ext.data.Model',
        fields:['cdtipcla','dstipcla','swgrapol']
    });
    
    Ext.define('_endpnx_modeloICD',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'ICD','DESCRIPCION'
        ]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    venExcluStoreTipos = new Ext.data.Store(
    {
        model      : 'ModeloTipoClausula'
        ,autoLoad  : true
        ,proxy     :
        {
            url     : venExcluUrlCargarTipos
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
    
    venExcluStoreDisp = new Ext.data.Store(
    {
        model      : 'ClausulaModel'
        ,autoLoad  : false
        ,proxy     :
        {
            url     : venExcluUrlCargarDisp
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
    
    venExcluStoreUsa = new Ext.data.Store(
    {
        model      : 'ClausulaModel'
        ,autoLoad  : true
        ,proxy     :
        {
            url     : _URL_CONSULTA_CLAUSULAS_POLIZA
            ,extraParams :
            {
                'params.cdunieco'  : inputCduniecopx
                ,'params.cdramo'   : inputCdramopx
                ,'params.estado'   : inputEstadopx
                ,'params.nmpoliza' : inputNmpolizapx
                ,'params.nmsituac' : inputNmsituacpx
            }
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'clausulasPoliza'
            }
        }
    });
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    Ext.define('_2_Form',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_2_Form initComponent');
            Ext.apply(this,
            {
                title        : 'Datos del endoso'
                ,items       :
                [
                    {
                        xtype       : 'datefield'
                        ,id         : '_2_fieldFechaId'
                        ,format     : 'd/m/Y'
                        ,fieldLabel : 'Fecha de efecto'
                        ,allowBlank : false
                        //,value      : new Date()
                        ,name       : 'fecha_endoso'
                        ,listeners  :
                        {
                            blur : function()
                            {
                                if(this.isValid())
                                {
                                    this.setReadOnly(true);
                                    mensajeWarning('La fecha ha sido establecida y no se podr&aacute; modificar. Si requiere una fecha diferente cargue de nuevo la pantalla seleccionando el tipo de endoso en el men&uacute; lateral izquierdo');
                                }
                            }
                        }
                    }
                ]
                ,defaults    : 
                {
                    style : 'margin : 5px;'
                }
                ,buttonAlign : 'center'
                ,buttons     :
                              [
                                  {
                                      text     : 'Confirmar'
                                      ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                                      ,handler : _2_confirmar
                                  }
                                  ,{
                                	  text     : 'Documentos'
                                      ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                                      ,handler : _2_documentos
                                  }
                              ]
            });
            this.callParent();
        }
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    _2_form=new _2_Form();
    
    Ext.create('Ext.panel.Panel',
    {
        border    : 0
        ,renderTo : 'maindiv_scr_exclu'
        ,defaults :
        {
        	style : 'margin : 5px;'
        }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                border  : 0
                ,layout :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,defaults : 
                {
                    style : 'margin:5px;'
                }
                ,items :
                [
                    Ext.create('Ext.form.field.ComboBox',
                    {
                        id              : 'idComboTipCla'
                        ,store          : venExcluStoreTipos
                        ,displayField   : 'dstipcla'
                        ,valueField     : 'cdtipcla'
                        ,editable       : false
                        ,forceSelection : true
                        ,style          : 'margin:5px'
                        ,fieldLabel     : 'Tipo de cl&aacute;usula'
                        ,width          : 400
                        ,listeners      :
                        {
                            change : function(me,value)
                            {
                                debug(value);
                                if(Ext.getCmp('idComboTipCla').getValue()&&Ext.getCmp('idComboTipCla').getValue().length>0)
                                {
                                    venExcluStoreDisp.load(
                                    {
                                        params :
                                        {
                                            'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()+''
                                            ,'smap1.pv_descrip_i' : Ext.getCmp('idfiltrocoberinput').getValue()+''
                                        }
                                    });
                                }
                            }
                        }
                    })
                    ,{
                        xtype : 'textfield'
                        ,fieldLabel : 'Filtro'
                        ,id         : 'idfiltrocoberinput'
                        ,listeners  :
                        {
                            change : function(me,value)
                            {
                                debug(value);
                                if(Ext.getCmp('idComboTipCla').getValue()&&Ext.getCmp('idComboTipCla').getValue().length>0)
                                {
                                    clearTimeout(loadExcluTimeoutVar);
                                    loadExcluTimeoutVar=setTimeout(function()
                                    {
                                        venExcluStoreDisp.load(
                                        {
                                            params :
                                            {
                                                'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()+''
                                                ,'smap1.pv_descrip_i' : Ext.getCmp('idfiltrocoberinput').getValue()+''
                                            }
                                        });
                                    },500);
                                }
                            }
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                id             : 'venExcluGridDisp'
                ,title         : 'Cl&aacute;usulas disponibles'
                ,store         : venExcluStoreDisp
                ,collapsible   : true
                ,titleCollapse : true
                ,style         : 'margin:5px'
                ,height        : 200
                ,columns       :
                [
                    {
                        header     : 'Nombre'
                        ,dataIndex : 'dsclausu'
                        ,flex      : 1
                    }
                    ,{
                        menuDisabled : true
                        ,xtype       : 'actioncolumn'
                        ,width       : 30
                        ,items       :
                        [
                            {
                                icon     : venExcluContexto+'/resources/fam3icons/icons/add.png'
                                ,tooltip : 'Agregar cl&aacute;usula'
                                ,handler : function(me,rowIndex)
                                {
                                    debug(rowIndex);
                                    var record=venExcluStoreDisp.getAt(rowIndex);
                                    
                                    var valido=true;
                                    if(valido)
                                    {
                                        valido = Ext.getCmp('_2_fieldFechaId').getValue();
                                        if(!valido)
                                        {
                                            mensajeWarning('Introduzca la fecha de efecto');
                                        }
                                    }
                                    
                                    if(valido)
                                    {
                                    Ext.Ajax.request(
                                    {
                                        url     : venExcluUrlLoadHtml
                                        ,params :
                                        {
                                            'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                        }
                                        ,success : function(response)
                                        {
                                            var json=Ext.decode(response.responseText);
                                            if(json.success==true)
                                            {
                                                var exclu=json.smap1;
                                                debug(exclu);
                                                Ext.create('Ext.window.Window',
                                                {
                                                    title        : 'Detalle de '+exclu.dsclausu
                                                    ,modal       : true
                                                    ,buttonAlign : 'center'
                                                    ,width       : 600
                                                    ,height      : 400
                                                    ,items       :
                                                    [
                                                        Ext.create('Ext.form.field.TextArea', {
                                                            id        : 'venExcluHtmlInputCopy'
                                                            ,width    : 580
                                                            ,height   : 380
                                                            ,value    : exclu.dslinea//viene del lector individual con dslinea
                                                            ,readOnly : true
                                                        })
                                                        ,{
                                                            id      : 'venExcluHidenInputCopy'
                                                            ,xtype  : 'textfield'
                                                            ,hidden : true
                                                            ,value  : '0'
                                                        }
                                                    ]
                                                    ,buttons     :
                                                    [
                                                        {
                                                            id       : 'venExcluHtmlCopyWindowBoton1'
                                                            ,text    : 'Editar'
                                                            ,icon    : venExcluContexto+'/resources/fam3icons/icons/pencil.png'
                                                            ,handler : function(me)
                                                            {
                                                                debug(me);
                                                                me.setDisabled(true);
                                                                Ext.getCmp('venExcluHidenInputCopy').setValue('1');
                                                                Ext.getCmp('venExcluHtmlInputCopy').setReadOnly(false);
                                                            }
                                                        }
                                                        ,{
                                                            id       : 'venExcluHtmlCopyWindowBoton2'
                                                            ,text    : 'Agregar'
                                                            ,icon    : venExcluContexto+'/resources/fam3icons/icons/add.png'
                                                            ,handler : function(me)
                                                            {
                                                                debug(me);
                                                                _setLoading(true,me.up().up());
                                                                debug(Ext.getCmp("idComboTipCla").getValue());
                                                                var swgrapol;
                                                                Ext.getCmp("idComboTipCla").getStore().each(function(record){
                                                                    if (record.get("cdtipcla") == Ext.getCmp("idComboTipCla").getValue())
                                                                        {swgrapol=record.get("swgrapol");}
                                                                    });
                                                                debug(swgrapol);
                                                                
                                                                if(swgrapol == "S")
                                                                {
                                                                    Ext.Ajax.request(
                                                                            {
                                                                                url     : venExcluUrlAddExclu
                                                                                ,params : 
                                                                                {
                                                                                    'smap1.pv_cdunieco_i'  : inputCduniecopx
                                                                                    ,'smap1.pv_cdramo_i'   : inputCdramopx
                                                                                    ,'smap1.pv_estado_i'   : inputEstadopx
                                                                                    ,'smap1.pv_nmpoliza_i' : inputNmpolizapx
                                                                                    ,'smap1.pv_nmsituac_i' : inputNmsituacpx
                                                                                    ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                                                                    ,'smap1.pv_nmsuplem_i' : panendExInput['nmsuplem']
                                                                                    ,'smap1.pv_ntramite_i' : panendExInput['ntramite']
                                                                                    ,'smap1.pv_cdtipsit_i' : panendExInput['cdtipsit']
                                                                                    ,'smap1.pv_status_i'   : 'V'
                                                                                    ,'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()
                                                                                    ,'smap1.pv_swmodi_i'   : ''
                                                                                    ,'smap1.pv_accion_i'   : 'I'
                                                                                    ,'smap1.confirmar'     : ''
                                                                                    ,'smap1.pv_dslinea_i'  :
                                                                                        Ext.getCmp('venExcluHidenInputCopy').getValue()=='1'?
                                                                                                Ext.getCmp('venExcluHtmlInputCopy').getValue():''
                                                                                    ,'smap1.fecha_endoso'  : Ext.Date.format(Ext.getCmp('_2_fieldFechaId').getValue(),'d/m/Y')
                                                                                }
                                                                                ,success : function (response)
                                                                                {
                                                                                    var json=Ext.decode(response.responseText);
                                                                                    if(json.success==true)
                                                                                    {
                                                                                        me.up().up().destroy();
                                                                                        venExcluStoreDisp.remove(record)
                                                                                        venExcluStoreUsa.load();
                                                                                        //venExcluStoreUsa.add(record);
                                                                                        //Ext.getCmp('venExcluGridUsaId').getView().refresh();
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        _setLoading(false,me.up().up());
                                                                                        Ext.Msg.show({
                                                                                            title:'Error',
                                                                                            msg: 'Error al agregar cl&aacute;usula',
                                                                                            buttons: Ext.Msg.OK,
                                                                                            icon: Ext.Msg.ERROR
                                                                                        });
                                                                                    }
                                                                                }
                                                                                ,failure : function ()
                                                                                {
                                                                                    _setLoading(false,me.up().up());
                                                                                    Ext.Msg.show({
                                                                                        title:'Error',
                                                                                        msg: 'Error de comunicaci&oacute;n',
                                                                                        buttons: Ext.Msg.OK,
                                                                                        icon: Ext.Msg.ERROR
                                                                                    });
                                                                                }
                                                                            });
                                                                }
                                                                else
                                                                {
                                                                    Ext.Ajax.request(
                                                                            {
                                                                                url     : venExcluUrlAddExcluDetalle
                                                                                ,params : 
                                                                                {
                                                                                    'smap1.pv_ntramite_i'  : inputNtramite
                                                                                    ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                                                                    ,'smap1.pv_comments_i' : Ext.getCmp('venExcluHtmlInputCopy').getValue()
                                                                                }
                                                                                ,success : function (response)
                                                                                {
                                                                                    var json=Ext.decode(response.responseText);
                                                                                    if(json.success==true)
                                                                                    {
                                                                                        me.up().up().destroy();
                                                                                        venExcluStoreDisp.remove(record);
                                                                                        Ext.Msg.show({
                                                                                            title:'&Eacute;xito',
                                                                                            msg: 'Se guard&oacute; su requerimiento',
                                                                                            buttons: Ext.Msg.OK
                                                                                        });
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        _setLoading(false,me.up().up());
                                                                                        Ext.Msg.show({
                                                                                            title:'Error',
                                                                                            msg: 'Error al agregar cl&aacute;usula',
                                                                                            buttons: Ext.Msg.OK,
                                                                                            icon: Ext.Msg.ERROR
                                                                                        });
                                                                                    }
                                                                                }
                                                                                ,failure : function ()
                                                                                {
                                                                                    _setLoading(false,me.up().up());
                                                                                    Ext.Msg.show({
                                                                                        title:'Error',
                                                                                        msg: 'Error de comunicaci&oacute;n',
                                                                                        buttons: Ext.Msg.OK,
                                                                                        icon: Ext.Msg.ERROR
                                                                                    });
                                                                                }
                                                                            });
                                                                }                                                                
                                                            }
                                                        }
                                                        ,{
                                                            text     : "Relaci&oacute;n de ICD's"
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/pill.png'
                                                            ,handler : function ()
                                                            {
                                                                _endpnx_windowIcd(record.get('cdclausu'));
                                                            }
                                                        }
                                                    ]
                                                }).show();
                                            }
                                            else
                                            {
                                                Ext.Msg.show({
                                                    title:'Error',
                                                    msg: 'Error al cargar',
                                                    buttons: Ext.Msg.OK,
                                                    icon: Ext.Msg.ERROR
                                                });
                                            }
                                        }
                                        ,failure : function()
                                        {
                                            Ext.Msg.show({
                                                title:'Error',
                                                msg: 'Error de comunicaci&oacute;n',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.ERROR
                                            });
                                        }
                                    });
                                    }
                                }
                            }
                        ]
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title          : 'Cl&aacute;usulas indicadas'
                ,id            : 'venExcluGridUsaId' 
                ,store         : venExcluStoreUsa
                ,collapsible   : true
                ,titleCollapse : true
                ,style         : 'margin:5px'
                ,height        : 200
                ,columns       :
                [
                    {
                        header     : 'Nombre'
                        ,dataIndex : 'dsclausu'
                        ,flex      : 1
                    }
                    ,{
                        width         : 30
                        ,menuDisabled : true
                        ,renderer     : function(value)
                        {
                            return '<img src="${ctx}/resources/fam3icons/icons/pencil.png" data-qtip="Editar detalle" style="cursor:pointer;" />';
                        }
                    }
                    ,{
                        width         : 30
                        ,menuDisabled : true
                        ,renderer     : function(value)
                        {
                            return '<img src="${ctx}/resources/fam3icons/icons/delete.png" data-qtip="Quitar cl&aacute;usula" style="cursor:pointer;" />';
                        }
                    }
                ]
                ,listeners :
                {
                    cellclick : function(grid, td,
                            cellIndex, record, tr,
                            rowIndex, e, eOpts)
                    {
                        debug(rowIndex,cellIndex);
                        if($(td).find('img').length>0)//si hay accion
                        {
                            var valido=true;
                            if(valido)
                            {
                                valido = Ext.getCmp('_2_fieldFechaId').getValue();
                                if(!valido)
                                {
                                    mensajeWarning('Introduzca la fecha de efecto');
                                }
                            }
                            if(valido)
                            {
                            if(cellIndex==1)
                            {
                                var record=venExcluStoreUsa.getAt(rowIndex);
                                debug(record);
                                Ext.create('Ext.window.Window',
                                {
                                    title        : 'Detalle de '+record.get('dsclausu')
                                    ,modal       : true
                                    ,buttonAlign : 'center'
                                    ,width       : 600
                                    ,height      : 400
                                    ,items       :
                                    [
                                        Ext.create('Ext.form.field.TextArea', {
                                            id        : 'venExcluHtmlInputEdit'
                                            ,width    : 580
                                            ,height   : 380
                                            ,value    : record.get('linea_usuario')&&record.get('linea_usuario').length>0?
                                                    record.get('linea_usuario'):record.get('linea_general')
                                            ,readOnly : true
                                        })
                                        ,{
                                            id      : 'venExcluHidenInputEdit'
                                            ,xtype  : 'textfield'
                                            ,hidden : true
                                            ,value  : '0'
                                        }
                                    ]
                                    ,buttons     :
                                    [
                                        {
                                            text     : 'Editar'
                                            ,icon    : venExcluContexto+'/resources/fam3icons/icons/pencil.png'
                                            ,handler : function(me)
                                            {
                                                debug(me);
                                                me.setDisabled(true);
                                                Ext.getCmp('venExcluHidenInputEdit').setValue('1');
                                                Ext.getCmp('venExcluHtmlInputEdit').setReadOnly(false);
                                            }
                                        }
                                        ,{
                                            text     : 'Guardar'
                                            ,icon    : venExcluContexto+'/resources/fam3icons/icons/disk.png'
                                            ,handler : function(me)
                                            {
                                                debug(me);
                                                if(Ext.getCmp('venExcluHidenInputEdit').getValue()=='1')
                                                {
                                                    _setLoading(true,me.up().up());
                                                    Ext.Ajax.request(
                                                    {
                                                        url     : venExcluUrlAddExclu
                                                        ,params : 
                                                        {
                                                            'smap1.pv_cdunieco_i'  : inputCduniecopx
                                                            ,'smap1.pv_cdramo_i'   : inputCdramopx
                                                            ,'smap1.pv_estado_i'   : inputEstadopx
                                                            ,'smap1.pv_nmpoliza_i' : inputNmpolizapx
                                                            ,'smap1.pv_nmsituac_i' : inputNmsituacpx
                                                            ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                                            ,'smap1.pv_nmsuplem_i' : panendExInput['nmsuplem']
		                                                    ,'smap1.pv_ntramite_i' : panendExInput['ntramite']
		                                                    ,'smap1.pv_cdtipsit_i' : panendExInput['cdtipsit']
                                                            ,'smap1.pv_status_i'   : ''
                                                            ,'smap1.pv_cdtipcla_i' : record.get('cdtipcla')
                                                            ,'smap1.pv_swmodi_i'   : ''
                                                            ,'smap1.pv_accion_i'   : 'U'
                                                            ,'smap1.confirmar'     : ''
                                                            ,'smap1.pv_dslinea_i'  :
                                                                Ext.getCmp('venExcluHidenInputEdit').getValue()=='1'?
                                                                        Ext.getCmp('venExcluHtmlInputEdit').getValue():''
                                                            ,'smap1.fecha_endoso'  : Ext.Date.format(Ext.getCmp('_2_fieldFechaId').getValue(),'d/m/Y')
                                                        }
                                                        ,success : function (response)
                                                        {
                                                            var json=Ext.decode(response.responseText);
                                                            if(json.success==true)
                                                            {
                                                                me.up().up().destroy();
                                                                venExcluStoreDisp.remove(record)
                                                                venExcluStoreUsa.load();
                                                                //venExcluStoreUsa.add(record);
                                                                //Ext.getCmp('venExcluGridUsaId').getView().refresh();
                                                            }
                                                            else
                                                            {
                                                                _setLoading(false,me.up().up());
                                                                Ext.Msg.show({
                                                                    title:'Error',
                                                                    msg: 'Error al agregar cl&aacute;usula',
                                                                    buttons: Ext.Msg.OK,
                                                                    icon: Ext.Msg.ERROR
                                                                });
                                                            }
                                                        }
                                                        ,failure : function ()
                                                        {
                                                            _setLoading(false,me.up().up());
                                                            Ext.Msg.show({
                                                                title:'Error',
                                                                msg: 'Error de comunicaci&oacute;n',
                                                                buttons: Ext.Msg.OK,
                                                                icon: Ext.Msg.ERROR
                                                            });
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    me.up().up().destroy();
                                                }
                                            }
                                        }
                                        ,{
                                            text     : "Relaci&oacute;n de ICD's"
                                            ,icon    : '${ctx}/resources/fam3icons/icons/pill.png'
                                            ,handler : function ()
                                            {
                                                _endpnx_windowIcd(record.get('cdclausu'));
                                            }
                                        }
                                    ]
                                }).show();
                            }//end if cell index = 1
                            else if(cellIndex==2)
                            {
                                var record=venExcluStoreUsa.getAt(rowIndex);
                                debug(record);
                                _setLoading(true,grid);
                                Ext.Ajax.request(
                                {
                                    url     : venExcluUrlAddExclu
                                    ,params : 
                                    {
                                        'smap1.pv_cdunieco_i'  : inputCduniecopx
                                        ,'smap1.pv_cdramo_i'   : inputCdramopx
                                        ,'smap1.pv_estado_i'   : inputEstadopx
                                        ,'smap1.pv_nmpoliza_i' : inputNmpolizapx
                                        ,'smap1.pv_nmsituac_i' : inputNmsituacpx
                                        ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                        ,'smap1.pv_nmsuplem_i' : panendExInput['nmsuplem']
		                                ,'smap1.pv_ntramite_i' : panendExInput['ntramite']
		                                ,'smap1.pv_cdtipsit_i' : panendExInput['cdtipsit']
                                        ,'smap1.pv_status_i'   : 'V'
                                        ,'smap1.pv_cdtipcla_i' : record.get('cdtipcla')
                                        ,'smap1.pv_swmodi_i'   : ''
                                        ,'smap1.pv_accion_i'   : 'B'
                                        ,'smap1.pv_dslinea_i'  : ''
                                        ,'smap1.confirmar'     : ''
                                        ,'smap1.fecha_endoso'  : Ext.Date.format(Ext.getCmp('_2_fieldFechaId').getValue(),'d/m/Y')
                                    }
                                    ,success : function (response)
                                    {
                                        var json=Ext.decode(response.responseText);
                                        if(json.success==true)
                                        {
                                            _setLoading(false,grid);
                                            venExcluStoreUsa.remove(record);
                                        }
                                        else
                                        {
                                            _setLoading(false,grid);
                                            Ext.Msg.show({
                                                title:'Error',
                                                msg: 'Error al quitar la cl&aacute;usula',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.ERROR
                                            });
                                        }
                                    }
                                    ,failure : function ()
                                    {
                                        _setLoading(false,grid);
                                        Ext.Msg.show({
                                            title:'Error',
                                            msg: 'Error de comunicaci&oacute;n',
                                            buttons: Ext.Msg.OK,
                                            icon: Ext.Msg.ERROR
                                        });
                                    }
                                });
                            }//end if cell index = 2
                            }
                        }//end if find img
                    }
                }
            })
            ,_2_form
        ]
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
});

////// funciones //////
function _endpnx_windowIcd(cdclausu)
{
    debug('>_endpnx_windowIcd:',cdclausu);
    var ventana;
    var combo = <s:property value="item1" />;
    combo.on(
    {
        'select' : function(comp,val)
        {
            debug('combo icd select:',val[0]);
            _setLoading(true,ventana);
            Ext.Ajax.request(
            {
                url      : _endpnx_urlAgregarIcd
                ,params  :
                {
                    'smap1.cdunieco'  : inputCduniecopx
                    ,'smap1.cdramo'   : inputCdramopx
                    ,'smap1.estado'   : inputEstadopx
                    ,'smap1.nmpoliza' : inputNmpolizapx
                    ,'smap1.nmsituac' : inputNmsituacpx
                    ,'smap1.cdclausu' : cdclausu
                    ,'smap1.nmsuplem' : panendExInput['nmsuplem']
                    ,'smap1.icd'      : val[0].get('key')
                }
                ,success : function(response)
                {
                    comp.reset();
                    _setLoading(false,ventana);
                    var json=Ext.decode(response.responseText);
                    debug('### agregar icd response:',json);
                    if(json.exito)
                    {
                        mensajeCorrecto('ICD relacionado','Se ha relacionado correctamente el ICD',function()
                        {
                            combo.up().down('grid').getStore().load();
                        });
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    comp.reset();
                    _setLoading(false,ventana);
                    errorComunicacion();
                }
            });
        }
    });
    ventana = Ext.create('Ext.window.Window',
    {
        title   : "Relaci&oacute;n de ICD's"
        ,width  : 600
        ,height : 400
        ,modal  : true
        ,items  :
        [
            combo
            ,Ext.create('Ext.grid.Panel',
            {
                minHeight   : 100
                ,maxHeight  : 250
                ,autoScroll : true
                ,columns    :
                [
                    {
                        header     : 'ICD'
                        ,dataIndex : 'DESCRIPCION'
                        ,flex      : 1
                    }
                    ,{
                        xtype  : 'actioncolumn'
                        ,width : 20
                        ,items :
                        [
                            {
                                icon     : '${ctx}/resources/fam3icons/icons/delete.png'
                                ,tooltip : 'Quitar'
                                ,handler : function(view,row,col,item,e,record)
                                {
                                    _endpnx_quitarICD(cdclausu,record.get('ICD'),combo.up().down('grid').getStore(),ventana);
                                }
                            }
                        ]
                    }
                ]
                ,store : Ext.create('Ext.data.Store',
                {
                    model     : '_endpnx_modeloICD'
                    ,autoLoad : true
                    ,proxy    :
                    {
                        url          : _endpnx_urlCargarIcdClausu
                        ,type        : 'ajax'
                        ,extraParams :
                        {
                            'smap1.cdunieco'  : inputCduniecopx
                            ,'smap1.cdramo'   : inputCdramopx
                            ,'smap1.estado'   : inputEstadopx
                            ,'smap1.nmpoliza' : inputNmpolizapx
                            ,'smap1.nmsituac' : inputNmsituacpx
                            ,'smap1.cdclausu' : cdclausu
                            ,'smap1.nmsuplem' : panendExInput['nmsuplem']
                        }
                        ,reader      :
                        {
                            type  : 'json'
                            ,root : 'slist1'
                        }
                    }
                })
            })
        ]
    }).show();
    centrarVentanaInterna(ventana);
    debug('<_endpnx_windowIcd');
}

function _endpnx_quitarICD(cdclausu,icd,store,ventana)
{
    debug('>_endpnx_quitarICD:',cdclausu,icd,'dummy');
    debug(store,ventana,'dummy');
    _setLoading(true,ventana);
    Ext.Ajax.request(
    {
        url      : _endpnx_urlBorrarIcd
        ,params  :
        {
            'smap1.cdunieco'  : inputCduniecopx
            ,'smap1.cdramo'   : inputCdramopx
            ,'smap1.estado'   : inputEstadopx
            ,'smap1.nmpoliza' : inputNmpolizapx
            ,'smap1.nmsituac' : inputNmsituacpx
            ,'smap1.cdclausu' : cdclausu
            ,'smap1.nmsuplem' : panendExInput['nmsuplem']
            ,'smap1.icd'      : icd
        }
        ,success : function(response)
        {
            _setLoading(false,ventana);
            var json=Ext.decode(response.responseText);
            debug('### borrar icd response:',json);
            if(json.exito)
            {
                mensajeCorrecto('ICD borrado','El ICD ha sido borrado',function()
                {
                    store.load();
                });
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _setLoading(false,ventana);
            errorComunicacion();
        }
    });
    debug('<_endpnx_quitarICD');
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindiv_scr_exclu" style="height:500px;border:0px solid red;"></div>