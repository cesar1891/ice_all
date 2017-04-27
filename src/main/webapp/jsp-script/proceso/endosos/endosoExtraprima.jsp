<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////

/*
smap1:
    cdrfc:'MAVA900817001',
    cdperson:'511965',
    masextraprima:'si',
    fenacimi:'1990-08-17T00:00:00',
    sexo:'H',
    Apellido_Materno:'MAT',
    nombre:'NOMBRE1',
    nombrecompleto:'NOMBRE1  PAT MAT',
    nmsituac:'1',
    segundo_nombre:'null',
    Parentesco:'T',
    CDTIPSIT:'SL',
    NTRAMITE:'615',
    CDUNIECO:'1006',
    CDRAMO:'2',
    extraprima:'0',
    NMSUPLEM:'245668512050000001',
    NMPOLIZA:'14',
    swexiper:'S',
    NMPOLIEX:'1006213000014000000',
    nacional:'001',
    activo:'true',
    NSUPLOGI:'10',
    ESTADO:'M',
    cdrol:'2',
    tpersona:'F',
    Apellido_Paterno:'PAT'
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _8_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _8_formLectura;
var _8_formExtraprima;
var _8_panelPri;
var _8_panelEndoso;
var _8_fieldFechaEndoso;

var _8_urlLoadHtml       = '<s:url namespace="/" action="cargarHtmlExclusion" />';
var _8_urlCargarClaTipos = '<s:url namespace="/" action="cargarTiposClausulasExclusion" />';
var _8_urlCargarClaDisp  = '<s:url namespace="/" action="obtenerExclusionesPorTipo" />';

var _8_panelCla;
var _8_storeClaTipos;
var _8_storeClaDisp;
var _8_storeClaUsa;
var loadExcluTimeoutVar;
var booleanPeso = "0"; 
var booleanOcup = "0";

var _8_urlGuardar = '<s:url namespace="/endosos" action="guardarEndosoExtraprima" />';

debug('_8_smap1:',_8_smap1);

var _p8_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p8_flujo:',_p8_flujo);
////// variables //////
///////////////////////

Ext.onReady(function()
{
    /////////////////////
    ////// modelos //////
    Ext.define('ModeloTipoClausula',
    {
        extend:'Ext.data.Model',
        fields:['cdtipcla','dstipcla','swgrapol']
    });
    
    Ext.define('ClausulaModel',{
        extend:'Ext.data.Model',
        fields:['cdclausu','dsclausu','linea_usuario','cdtipcla','linea_general']
    });
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    _8_storeClaTipos = new Ext.data.Store(
    {
        model      : 'ModeloTipoClausula'
        ,autoLoad  : true
        ,proxy     :
        {
            url     : _8_urlCargarClaTipos
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
    
    _8_storeClaDisp = new Ext.data.Store(
    {
        model      : 'ClausulaModel'
        ,autoLoad  : false
        ,proxy     :
        {
            url     : _8_urlCargarClaDisp
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
    
    _8_storeClaUsa = new Ext.data.Store(
    {
        model  : 'ClausulaModel'
        ,proxy :
        {
            type    : 'memory'
            ,reader : 'json'
            ,data   : []
        }
    });
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    Ext.define('_8_PanelCla',
    {
        extend    : 'Ext.panel.Panel'
        //,hidden   : _8_smap1.masextraprima!='si'
        ,border   : 0
        ,defaults :
        {
            style : 'margin : 5px;'
        }
        ,layout   :
        {
            type     : 'table'
            ,columns : 2
        }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                border   : 0
                ,colspan : 2
                ,layout  :
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
                        ,store          : _8_storeClaTipos
                        ,displayField   : 'dstipcla'
                        ,valueField     : 'cdtipcla'
                        ,editable       : false
                        ,forceSelection : true
                        ,style          : 'margin:5px'
                        ,fieldLabel     : 'Tipo de cl&aacute;usula'
                        ,width          : 350
                        ,queryMode      : 'local'
                        ,listeners      :
                        {
                            change : function(me,value)
                            {
                                debug(value);
                                if(Ext.getCmp('idComboTipCla').getValue()&&Ext.getCmp('idComboTipCla').getValue().length>0)
                                {
                                    _8_storeClaDisp.load(
                                    {
                                        params :
                                        {
                                            'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()+''
                                            ,'smap1.pv_descrip_i' : Ext.getCmp('idfiltrocoberinput').getValue()+''
                                        }
                                        ,callback: function(records, operation, success)
                                        {
                                            debug('records cargados:',records);
                                            for(var i=0;i<records.length;i++)
                                            {
                                                records[i].set('cdtipcla',Ext.getCmp('idComboTipCla').getValue());
                                            }
                                            debug('records cargados editados:',records);
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
                        ,width      : 350
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
                                        _8_storeClaDisp.load(
                                        {
                                            params :
                                            {
                                                'smap1.pv_cdtipcla_i' : Ext.getCmp('idComboTipCla').getValue()+''
                                                ,'smap1.pv_descrip_i' : Ext.getCmp('idfiltrocoberinput').getValue()+''
                                            }
                                            ,callback: function(records, operation, success)
                                            {
                                                debug('records cargados:',records);
                                                for(var i=0;i<records.length;i++)
                                                {
                                                    records[i].set('cdtipcla',Ext.getCmp('idComboTipCla').getValue());
                                                }
                                                debug('records cargados editados:',records);
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
                ,store         : _8_storeClaDisp
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
                                    var record=_8_storeClaDisp.getAt(rowIndex);
                                    debug('clausula a agregar:',record);
                                    Ext.Ajax.request(
                                    {
                                        url     : _8_urlLoadHtml
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
                                                                //me.up().up().setLoading(true);
                                                                debug(Ext.getCmp("idComboTipCla").getValue());
                                                                var swgrapol;
                                                                Ext.getCmp("idComboTipCla").getStore().each(function(record){
                                                                    if (record.get("cdtipcla") == Ext.getCmp("idComboTipCla").getValue())
                                                                        {swgrapol=record.get("swgrapol");}
                                                                    });
                                                                debug(swgrapol);
                                                                
                                                                if(swgrapol == "S")
                                                                {
                                                                    record.set('linea_usuario',Ext.getCmp('venExcluHidenInputCopy').getValue()=='1'?
                                                                            Ext.getCmp('venExcluHtmlInputCopy').getValue():'');
                                                                    record.set('linea_general',Ext.getCmp('venExcluHidenInputCopy').getValue()=='0'?
                                                                            Ext.getCmp('venExcluHtmlInputCopy').getValue():'');
                                                                    _8_storeClaUsa.add(record);
                                                                    _8_storeClaDisp.remove(record);
                                                                    me.up().up().destroy();
                                                                    debug('clausula agregada:',record);
                                                                }                                                               
                                                            }
                                                        }
                                                    ]
                                                }).show();
                                            }
                                        }
                                        ,failure : errorComunicacion
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
                ,store         : _8_storeClaUsa
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
                                var record=_8_storeClaUsa.getAt(rowIndex);
                                debug('clausula a editar:',record);
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
                                                    record.set('linea_usuario',Ext.getCmp('venExcluHtmlInputEdit').getValue());
                                                    debug('clausula editada:',record);
                                                    me.up().up().destroy();
                                                }
                                                else
                                                {
                                                    debug('sin cambios');
                                                    me.up().up().destroy();
                                                }
                                            }
                                        }
                                    ]
                                }).show();
                            }//end if cell index = 1
                            else if(cellIndex==2)
                            {
                                var record=_8_storeClaUsa.getAt(rowIndex);
                                debug('clausula eliminada:',record);
                                _8_storeClaUsa.remove(record);
                            }//end if cell index = 2
                        }//end if find img
                    }
                }
            })
        ]
    });
    
    Ext.define('_8_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_8_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    _8_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_8_FormExtraprima',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_8_FormExtraprima initComponent');
            Ext.apply(this,
            {
                title      : 'Extraprima'
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items     :
                [
                     <s:property value="imap1.itemExtraprimaLectura" />
                    ,<s:property value="imap1.itemExtraprima" />
                   	,<s:property value="imap1.itemExtraprimaLecturaOcu" />
                    ,<s:property value="imap1.itemExtraprimaOcu" />
                ]
                ,listeners :
                {
                    afterrender : function(me)
                    {
                        var comboOriginal  		= me.items.items[0];
                        var comboNuevo     		= me.items.items[1];
                        var txtOcupacional 		= me.items.items[2];
                        var txtNuevoOcupacional = me.items.items[3];
                        
                        var extraOrigin   = _8_smap1.extraprima;	//_8_smap1.extraprima		_8_smap1.masextraprima	
                        var extraOrigOcu  = _8_smap1.extraprimaOcu;	//_8_smap1.extraprimaOcu	_8_smap1.masextraprimaOcu

                        
                        comboOriginal.setValue(extraOrigin);
                        comboNuevo.setValue(extraOrigin);
                        
                        txtOcupacional.setValue(extraOrigOcu);
                        txtNuevoOcupacional.setValue(extraOrigOcu);
                        
                        //COMBO PARA LA EXTRAPRIMA DE SOBREPESO
                        comboNuevo.on('change',function(combo,newVal,oldVal)
                        {
                        	var extraOrigin = _8_smap1.extraprima;
                            var incremento  = _8_smap1.masextraprima=='si';
                            if(incremento==true) {
                            	booleanPeso = "1";
                                if(newVal*1<extraOrigin*1) {
                                	booleanPeso = "0";
                                    combo.setValue(oldVal);
                                    mensajeError('No se puede decrementar la extraprima de sobrepeso');
                                }
                            }
                            else{
                            	booleanPeso = "1";
                                if(newVal*1>extraOrigin*1){
                                	booleanPeso = "0";
                                    combo.setValue(oldVal);
                                    mensajeError('No se puede incrementar la extraprima de sobrepeso');
                                }
                            }
                        });
                        
                        txtNuevoOcupacional.on('blur',function(textfield,newVal,oldVal){
                        	var extraOrigin = _8_smap1.extraprimaOcu;
                        	var incremento  = _8_smap1.masextraprimaOcu=='si';
                        	var valornuevo = me.items.items[3].getValue();
                        	var valorOriginal = me.items.items[2].getValue();
                        	
                        	if(incremento==true){
                            	booleanOcup = "1";
                            	if(valornuevo*1<extraOrigin*1){
                            		booleanOcup = "0";
                            		me.items.items[3].setValue(valorOriginal);
                                    mensajeError('No se puede decrementar la extraprima ocupacional');
                                }
                            }
                            else{
                            	booleanOcup = "1";
                                if(valornuevo*1>extraOrigin*1){
                                	booleanOcup = "0";
                                	me.items.items[3].setValue(valorOriginal);
                                    mensajeError('No se puede incrementar la extraprima ocupacional');
                                }
                            }
                        });
                    }
                }
            });
            this.callParent();
        }
    });
    
    Ext.define('_8_FormLectura',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_8_FormLectura initComponent');
            Ext.apply(this,
            {
                title      : 'Datos de la p&oacute;liza'
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,listeners :
                {
                    afterrender : function(me){
                    	debug("Valor de ME ==>",me);
                    	//heredarPanel(me);
                   	}
                }
                ,items     : [ <s:property value="imap1.itemsLectura" /> ]
            });
            this.callParent();
        }
    });
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    _8_panelCla=new _8_PanelCla();
    _8_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha de efecto'
        ,allowBlank : false
        ,value      : new Date()
        ,name       : 'fecha_endoso'
    });
    _8_panelEndoso    = new _8_PanelEndoso();
    _8_formExtraprima = new _8_FormExtraprima();
    _8_formLectura    = new _8_FormLectura();
    
    _8_panelPri=Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_8_divPri'
        ,defaults    :
        {
            style : 'margin : 5px;'
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Confirmar endoso'
                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                ,handler : _8_confirmar
            }
        ]
        ,items       :
        [
            _8_formLectura
            ,_8_formExtraprima
            ,_8_panelCla
            ,_8_panelEndoso
        ]
    });
    ////// contenido //////
    ///////////////////////
});

///////////////////////
////// funciones //////
function _8_confirmar()
{
    debug('_8_confirmar');
    
    var valido=true;
    
    if(valido)
    {
        valido=_8_formExtraprima.isValid()&&_8_panelEndoso.isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    if(valido)
    {
    	var json=
        {
            smap1  : _8_smap1
            ,smap2 :
            {
                fecha_endoso 	: Ext.Date.format(_8_fieldFechaEndoso.getValue(),'d/m/Y')
                ,extraprima  	: _8_formExtraprima.items.items[1].getValue()
                ,extraprimaOcu  : _8_formExtraprima.items.items[3].getValue()
                ,banderapeso    : booleanPeso
               	,banderaocup    : booleanOcup
            }
        }
        
        json['slist1']=[];
        _8_storeClaUsa.each(function(record)
        {
            json['slist1'].push(record.getData());
        });
        
        if(!Ext.isEmpty(_p8_flujo))
        {
            json.flujo = _p8_flujo;
        }
        
        debug('datos que se enviaran:',json);
        _setLoading(true,_8_panelPri);
        Ext.Ajax.request(
        {
            url       : _8_urlGuardar
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_8_panelPri);
                json=Ext.decode(response.responseText);
                debug('datos recibidos:',json);
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
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                    	var cadena= json.mensaje;
                        var palabra="guardado";
                    	if (cadena.indexOf(palabra)==-1){
                    		_generarRemesaClic2(
                                    false
                                    ,_8_smap1.CDUNIECO
                                    ,_8_smap1.CDRAMO
                                    ,_8_smap1.ESTADO
                                    ,_8_smap1.NMPOLIZA
                                    ,callbackRemesa
                                );
                    	}else{
                    		_generarRemesaClic(
                                    true
                                    ,_8_smap1.CDUNIECO
                                    ,_8_smap1.CDRAMO
                                    ,_8_smap1.ESTADO
                                    ,_8_smap1.NMPOLIZA
                                    ,callbackRemesa
                                );
                    	}
                    });
                }
                else
                {
                    mensajeError(json.error);
                }
            }
            ,failure  : function()
            {
                _setLoading(false,_8_panelPri);
                errorComunicacion();
            }
        });
    }
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_8_divPri" style="height:1000px;"></div>