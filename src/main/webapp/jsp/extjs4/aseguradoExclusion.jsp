<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var _URL_CONSULTA_CLAUSULAS_POLIZA          = '<s:url namespace="/consultasPoliza" action="consultaClausulasPoliza" />';
    var venExcluUrlCargarDisp      = '<s:url namespace="/"                action="obtenerExclusionesPorTipo"     />';
    var venExcluUrlGuardar         = '<s:url namespace="/"                action="guardarExclusiones"            />';
    var venExcluUrlLoadHtml        = '<s:url namespace="/"                action="cargarHtmlExclusion"           />';
    var venExcluUrlAddExclu        = '<s:url namespace="/"                action="agregarExclusion"              />';
    var venExcluUrlAddExcluDetalle = '<s:url namespace="/"                action="agregarExclusionDetalle"       />';
    var venExcluUrlSaveHtml        = '<s:url namespace="/"                action="guardarHtmlExclusion"          />';
    var venExcluUrlCargarTipos     = '<s:url namespace="/"                action="cargarTiposClausulasExclusion" />';
    var _pnx_urlAgregarIcd         = '<s:url namespace="/emision"         action="agregarClausulaICD"            />';    
    var _pnx_urlCargarIcdClausu    = '<s:url namespace="/emision"         action="cargarClausulaICD"             />';
    var _pnx_urlBorrarIcd          = '<s:url namespace="/emision"         action="borrarClausulaICD"             />';
    
    var _pnx_smap1 = <s:property value='%{convertToJSON("smap1")}' escapeHtml="false" />;
    _pnx_smap1.cdunieco = _pnx_smap1.pv_cdunieco;
    _pnx_smap1.cdramo   = _pnx_smap1.pv_cdramo;
    _pnx_smap1.estado   = _pnx_smap1.pv_estado;
    _pnx_smap1.nmpoliza = _pnx_smap1.pv_nmpoliza;
    _pnx_smap1.nmsituac = _pnx_smap1.pv_nmsituac;
    _pnx_smap1.nmsuplem = _pnx_smap1.pv_nmsuplem;
    debug('_pnx_smap1:',_pnx_smap1);
    
    var inputCdpersonpx         = '<s:property value="smap1.pv_cdperson" />';
    var inputCdrolpx            = '<s:property value="smap1.pv_cdrol" />';
    var inputNombreaseguradopx  = '<s:property value="smap1.nombreAsegurado" escapeHtml="false" />';
    var inputCdrfcpx            = '<s:property value="smap1.cdrfc" escapeHtml="false" />';
    var venExcluStoreDisp;
    var venExcluStoreUsa;
    var venExcluStoreTipos;
    var loadExcluTimeoutVar;
    /*///////////////////*/
    ////// variables //////
    ///////////////////////

Ext.onReady(function(){
    
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('_pnx_modeloICD',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'ICD','DESCRIPCION'
        ]
    });
    
    Ext.define('ClausulaModel',{
        extend:'Ext.data.Model',
        fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
    });
    
    Ext.define('ModeloTipoClausula',
    {
        extend:'Ext.data.Model',
        fields:['cdtipcla','dstipcla','swgrapol']
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
            	'params.cdunieco'  : _pnx_smap1.cdunieco
                ,'params.cdramo'   : _pnx_smap1.cdramo
                ,'params.estado'   : _pnx_smap1.estado
                ,'params.nmpoliza' : _pnx_smap1.nmpoliza
                ,'params.nmsituac' : _pnx_smap1.nmsituac
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
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    Ext.create('Ext.panel.Panel',
    {
        border    : 0
        ,renderTo : 'maindiv_scr_exclu'
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
                                icon     : '${ctx}/resources/fam3icons/icons/add.png'
                                ,tooltip : 'Agregar cl&aacute;usula'
                                ,handler : function(me,rowIndex)
                                {
                                    debug(rowIndex);
                                    var record=venExcluStoreDisp.getAt(rowIndex);
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
                                                centrarVentanaInterna(Ext.create('Ext.window.Window',
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
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
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
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                                                            ,handler : function(me)
                                                            {
                                                                debug(me);
                                                                me.up().up().setLoading(true);
                                                                //console.log(Ext.getCmp("idComboTipCla").getValue());
                                                                var swgrapol;
                                                                Ext.getCmp("idComboTipCla").getStore().each(function(record){
                                                                    if (record.get("cdtipcla") == Ext.getCmp("idComboTipCla").getValue())
                                                                        {swgrapol=record.get("swgrapol");}
                                                                    });
                                                                //console.log(swgrapol);
                                                                
                                                                if(swgrapol == "S")
                                                                {
                                                                    Ext.Ajax.request(
                                                                            {
                                                                                url     : venExcluUrlAddExclu
                                                                                ,params : 
                                                                                {
                                                                                    'smap1.pv_cdunieco_i'  : _pnx_smap1.cdunieco
                                                                                    ,'smap1.pv_cdramo_i'   : _pnx_smap1.cdramo
                                                                                    ,'smap1.pv_estado_i'   : _pnx_smap1.estado
                                                                                    ,'smap1.pv_nmpoliza_i' : _pnx_smap1.nmpoliza
                                                                                    ,'smap1.pv_nmsituac_i' : _pnx_smap1.nmsituac
                                                                                    ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                                                                    ,'smap1.pv_nmsuplem_i' : _pnx_smap1.nmsuplem
                                                                                    ,'smap1.pv_status_i'   : 'V'
                                                                                    ,'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()
                                                                                    ,'smap1.pv_swmodi_i'   : ''
                                                                                    ,'smap1.pv_accion_i'   : 'I'
                                                                                    ,'smap1.pv_dslinea_i'  :
                                                                                        Ext.getCmp('venExcluHidenInputCopy').getValue()=='1'?
                                                                                                Ext.getCmp('venExcluHtmlInputCopy').getValue():''
                                                                                }
                                                                                ,success : function (response)
                                                                                {
                                                                                    var json=Ext.decode(response.responseText);
                                                                                    if(json.success==true)
                                                                                    {
                                                                                        me.up().up().destroy();
                                                                                        venExcluStoreDisp.remove(record);
                                                                                        venExcluStoreUsa.load();
                                                                                        //venExcluStoreUsa.add(record);
                                                                                        //Ext.getCmp('venExcluGridUsaId').getView().refresh();
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        me.up().up().setLoading(false);
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
                                                                                    me.up().up().setLoading(false);
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
                                                                                        //venExcluStoreUsa.load();
                                                                                        //venExcluStoreUsa.add(record);
                                                                                        //Ext.getCmp('venExcluGridUsaId').getView().refresh();
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        me.up().up().setLoading(false);
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
                                                                                    me.up().up().setLoading(false);
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
                                                                _pnx_windowIcd(record.get('cdclausu'));
                                                            }
                                                        }
                                                    ]
                                                }).show());
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
                ,buttonAlign   : 'center'
                ,columns       :
                [
                    {
                        header     : 'Nombre'
                        ,dataIndex : 'dsclausu'
                        ,flex      : 1
                    }
                    ,{
                        width        : 30
                        ,menuDisabled : true
                        ,renderer     : function(value)
                        {
                        	return '<img src="${ctx}/resources/fam3icons/icons/pencil.png" data-qtip="Editar detalle" style="cursor:pointer;" />';
                        }
                    }
                    ,{
                        width        : 30
                        ,menuDisabled : true
                        ,renderer     : function(value)
                        {
                        	return '<img src="${ctx}/resources/fam3icons/icons/delete.png" data-qtip="Quitar cl&aacute;usula" style="cursor:pointer;" />';
                        }
                    }
                    /*
                    ,{
                        menuDisabled : true
                        ,xtype       : 'actioncolumn'
                        ,width       : 30
                        ,items       :
                        [
                            {
                                icon     : '${ctx}/resources/fam3icons/icons/pencil.png'
                                ,tooltip : 'Editar detalle'
                                ,handler : function(me,rowIndex)
                                {
                                    debug(rowIndex);
                                    
                                }
                            }
                        ]
                    }
                    */
                ]
                ,buttons:
                [
                    /*{
                        text     : 'Guardar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,handler : function(but)
                        {
                            var me=but;
                            me.up().up().setLoading(true);
                            var slist1=[];
                            venExcluStoreUsa.each(function(record,index)
                            {
                                slist1.push(
                                {
                                    'cd'    : record.get('cd')
                                    ,'ds'   : record.get('ds')
                                });
                            });
                            var submitValues={};
                            submitValues['slist1']=slist1;
                            var smap1={
                                cdunieco  : _pnx_smap1.cdunieco
                                ,cdramo   : _pnx_smap1.cdramo
                                ,estado   : _pnx_smap1.estado
                                ,nmpoliza : _pnx_smap1.nmpoliza
                                ,nmsituac : _pnx_smap1.nmsituac
                                ,cdperson : inputCdpersonpx
                                ,cdrol    : inputCdrolpx
                            };
                            submitValues['smap1']=smap1;
                            debug('submit',Ext.encode(submitValues));
                            Ext.Ajax.request(
                            {
                                url      : venExcluUrlGuardar
                                ,jsonData : Ext.encode(submitValues)
                                ,success : function()
                                {
                                    me.up().up().setLoading(false);
                                    debug('success');
                                }
                                ,failure : function()
                                {
                                    me.up().up().setLoading(false);
                                    debug('failure');
                                }
                            });
                        }
                    }*/
                    {
                        text     : 'Aceptar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                        ,handler : function()
                        {
                        	try{
                        		expande(2);
                        	}catch(e){
                        		debugError('No hay funcion de Expande para Exclusiones',e);
                        	}
                        	
                            try{
                            	_callbackAseguradoExclusiones();
                            }catch(e){
                            	debugError('No hay funcion de Callback para Exclusiones',e);
                            }
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
                            if(cellIndex==1)
                            {
                                var record=venExcluStoreUsa.getAt(rowIndex);
                                debug(record);
                                centrarVentanaInterna(Ext.create('Ext.window.Window',
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
                                            ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
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
                                            ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                                            ,handler : function(me)
                                            {
                                                debug(me);
                                                if(Ext.getCmp('venExcluHidenInputEdit').getValue()=='1')
                                                {
                                                    me.up().up().setLoading(true);
                                                    Ext.Ajax.request(
                                                    {
                                                        url     : venExcluUrlAddExclu
                                                        ,params : 
                                                        {
                                                            'smap1.pv_cdunieco_i'  : _pnx_smap1.cdunieco
                                                            ,'smap1.pv_cdramo_i'   : _pnx_smap1.cdramo
                                                            ,'smap1.pv_estado_i'   : _pnx_smap1.estado
                                                            ,'smap1.pv_nmpoliza_i' : _pnx_smap1.nmpoliza
                                                            ,'smap1.pv_nmsituac_i' : _pnx_smap1.nmsituac
                                                            ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
                                                            ,'smap1.pv_nmsuplem_i' : _pnx_smap1.nmsuplem
                                                            ,'smap1.pv_status_i'   : 'V'
                                                            ,'smap1.pv_cdtipcla_i' : record.get('cdtipcla')
                                                            ,'smap1.pv_swmodi_i'   : ''
                                                            ,'smap1.pv_accion_i'   : 'U'
                                                            ,'smap1.pv_dslinea_i'  :
                                                                Ext.getCmp('venExcluHidenInputEdit').getValue()=='1'?
                                                                        Ext.getCmp('venExcluHtmlInputEdit').getValue():''
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
                                                                me.up().up().setLoading(false);
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
                                                            me.up().up().setLoading(false);
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
                                            ,handler : function()
                                            {
                                                _pnx_windowIcd(record.get('cdclausu'));
                                            }
                                        }
                                    ]
                                }).show());
                            }//end if cell index = 1
                            else if(cellIndex==2)
                            {
                                var record=venExcluStoreUsa.getAt(rowIndex);
                            	debug(record);
                            	Ext.Ajax.request(
	                            {
	                                url     : venExcluUrlAddExclu
	                                ,params : 
	                                {
	                                    'smap1.pv_cdunieco_i'  : _pnx_smap1.cdunieco
	                                    ,'smap1.pv_cdramo_i'   : _pnx_smap1.cdramo
	                                    ,'smap1.pv_estado_i'   : _pnx_smap1.estado
	                                    ,'smap1.pv_nmpoliza_i' : _pnx_smap1.nmpoliza
	                                    ,'smap1.pv_nmsituac_i' : _pnx_smap1.nmsituac
	                                    ,'smap1.pv_cdclausu_i' : record.get('cdclausu')
	                                    ,'smap1.pv_nmsuplem_i' : _pnx_smap1.nmsuplem
	                                    ,'smap1.pv_status_i'   : ''
	                                    ,'smap1.pv_cdtipcla_i' : ''
	                                    ,'smap1.pv_swmodi_i'   : ''
	                                    ,'smap1.pv_accion_i'   : 'D'
	                                    ,'smap1.pv_dslinea_i'  : ''
	                                }
	                                ,success : function (response)
	                                {
	                                    var json=Ext.decode(response.responseText);
	                                    if(json.success==true)
	                                    {
	                                        venExcluStoreUsa.remove(record);
	                                    }
	                                    else
	                                    {
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
	                                    Ext.Msg.show({
	                                        title:'Error',
	                                        msg: 'Error de comunicaci&oacute;n',
	                                        buttons: Ext.Msg.OK,
	                                        icon: Ext.Msg.ERROR
	                                    });
	                                }
	                            });
                            }//end if cell index = 2
                        }//end if find img
                    }
                }
            })
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

///////////////////////
////// funciones //////
///////////////////////
function _pnx_quitarICD(cdclausu,icd,store,ventana)
{
    debug('>_pnx_quitarICD:',cdclausu,icd,'dummy');
    debug(store,ventana,'dummy');
    ventana.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _pnx_urlBorrarIcd
        ,params  :
        {
            'smap1.cdunieco'  : _pnx_smap1.cdunieco
            ,'smap1.cdramo'   : _pnx_smap1.cdramo
            ,'smap1.estado'   : _pnx_smap1.estado
            ,'smap1.nmpoliza' : _pnx_smap1.nmpoliza
            ,'smap1.nmsituac' : _pnx_smap1.nmsituac
            ,'smap1.cdclausu' : cdclausu
            ,'smap1.nmsuplem' : _pnx_smap1.nmsuplem
            ,'smap1.icd'      : icd
        }
        ,success : function(response)
        {
            ventana.setLoading(false);
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
            ventana.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_pnx_quitarICD');
}

function _pnx_windowIcd(cdclausu)
{
    debug('>_pnx_windowIcd:',cdclausu);
    var ventana;
    var combo = <s:property value="item1" />;
    combo.on(
    {
        'select' : function(comp,val)
        {
            debug('combo icd select:',val[0]);
            ventana.setLoading(true);
            Ext.Ajax.request(
            {
                url      : _pnx_urlAgregarIcd
                ,params  :
                {
                    'smap1.cdunieco'  : _pnx_smap1.cdunieco
                    ,'smap1.cdramo'   : _pnx_smap1.cdramo
                    ,'smap1.estado'   : _pnx_smap1.estado
                    ,'smap1.nmpoliza' : _pnx_smap1.nmpoliza
                    ,'smap1.nmsituac' : _pnx_smap1.nmsituac
                    ,'smap1.cdclausu' : cdclausu
                    ,'smap1.nmsuplem' : _pnx_smap1.nmsuplem
                    ,'smap1.icd'      : val[0].get('key')
                }
                ,success : function(response)
                {
                    comp.reset();
                    ventana.setLoading(false);
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
                    ventana.setLoading(false);
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
                                    _pnx_quitarICD(cdclausu,record.get('ICD'),combo.up().down('grid').getStore(),ventana);
                                }
                            }
                        ]
                    }
                ]
                ,store : Ext.create('Ext.data.Store',
                {
                    model     : '_pnx_modeloICD'
                    ,autoLoad : true
                    ,proxy    :
                    {
                        url          : _pnx_urlCargarIcdClausu
                        ,type        : 'ajax'
                        ,extraParams :
                        {
                            'smap1.cdunieco'  : _pnx_smap1.cdunieco
                            ,'smap1.cdramo'   : _pnx_smap1.cdramo
                            ,'smap1.estado'   : _pnx_smap1.estado
                            ,'smap1.nmpoliza' : _pnx_smap1.nmpoliza
                            ,'smap1.nmsituac' : _pnx_smap1.nmsituac
                            ,'smap1.cdclausu' : cdclausu
                            ,'smap1.nmsuplem' : _pnx_smap1.nmsuplem
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
    debug('<_pnx_windowIcd');
}

///////////////////////
////// funciones //////
///////////////////////

</script>
<div id="maindiv_scr_exclu" style="height:500px;border:0px solid red;"></div>