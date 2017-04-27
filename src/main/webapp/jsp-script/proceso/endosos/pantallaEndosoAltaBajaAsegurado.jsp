<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var panendabaseguGridAsegu;
var panendabaseguStoreAsegu;
var panendabaseguPanelLectura;
var panendabaseguPanelPrincipal;
var panEndAltBajAseStoreAltas;
var panEndAltBajAseStoreBajas;
var panEndAltBajAseGridAltas;
var panEndAltBajAseGridBajas;
var panEndAltBajAseWindowAsegu;
var panEndAltBajAseNmsituac;
var panEndAltBajAseValues='cadena';
var _3_form;
var _3_storeClaDisp;
var _3_storeClaTipos;
var loadExcluTimeoutVar;
var _3_panelCla;
var _3_storeClaUsa;
var _TITULAR = 'T';

//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var panendabaseguInputSmap1   = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
var panendabaseguInputSmap2   = <s:property value="%{convertToJSON('smap2')}" escapeHtml="false" />;

var panendabaseguUrlLoadAsegu = '<s:url namespace="/"           action="cargarComplementariosAsegurados" />';
var panendabaseguUrlSave      = '<s:url namespace="/endosos"    action="guardarEndosoAltaBajaAsegurado" />';
var panEndAltBajAseUrlDoc     = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza" />';
var _3_urlCargarClaDisp       = '<s:url namespace="/"           action="obtenerExclusionesPorTipo" />';
var _3_urlCargarClaTipos      = '<s:url namespace="/"           action="cargarTiposClausulasExclusion" />';
var _3_urlLoadHtml            = '<s:url namespace="/"           action="cargarHtmlExclusion" />';

debug('panendabaseguInputSmap1',panendabaseguInputSmap1);
debug('panendabaseguInputSmap2',panendabaseguInputSmap2);

var _endAseg_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_endAseg_flujo:',_endAseg_flujo);
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
function _3_cellclick(grid, td, index)
{
	debug('_3_itemclick');
	var nCols=grid.headerCt.getGridColumns().length;
	debug('columns length: ',nCols,'index: ',index);
	if(index!=nCols-1)
	{
		panEndAltBajAseWindowAsegu.show();
	}
}

function panendabaseguFunAgregar()
{
	debug('panendabaseguFunAgregar');
	if(panEndAltBajAseStoreAltas.getCount()+panEndAltBajAseStoreBajas.getCount()==0)
    {
		panEndAltBajAseNmsituac = panendabaseguStoreAsegu.getCount();
        debug('panEndAltBajAseNmsituac:',panEndAltBajAseNmsituac);
        Ext.getCmp('_3_formNuevo').getForm().reset();
        panEndAltBajAseWindowAsegu.show();
    }
    else
    {
        Ext.Msg.show(
        {
            title    : 'Error'
            ,icon    : Ext.Msg.WARNING
            ,msg     : 'No se pueden quitar/agregar m&aacute;s asegurados'
            ,buttons : Ext.Msg.OK
        });
    }
}

function panendabaseguFunQuitar() {
	debug('panendabaseguFunQuitar');
	var aseguSelec=0;
	var aseguActivo;
    panendabaseguStoreAsegu.each(function(record) {
        if(record.get('activo')==true) {
            aseguSelec=aseguSelec+1;
            aseguActivo=record;   
        }
    });
    
    if(aseguSelec==1) {
    	// Si no es cliente ni titular, permitimos la baja:
        if(aseguActivo.get('cdrol')==2 /* se permite quitar asegurado Titular && aseguActivo.get('Parentesco') != _TITULAR*/) {
        	if(panEndAltBajAseStoreAltas.getCount()+panEndAltBajAseStoreBajas.getCount()==0) {
        		panEndAltBajAseStoreBajas.add(aseguActivo);
        		panendabaseguStoreAsegu.remove(aseguActivo);
        	} else {
        		Ext.Msg.show({
   	                title    : 'Error'
   	                ,icon    : Ext.Msg.WARNING
   	                ,msg     : 'No se pueden quitar/agregar m&aacute;s asegurados'
   	                ,buttons : Ext.Msg.OK
   	            });
        	}
        } else {
        	Ext.Msg.show({
   	            title    : 'Error'
   	            ,icon    : Ext.Msg.WARNING
   	            ,msg     : 'No se puede quitar el cliente'
   	            ,buttons : Ext.Msg.OK
   	        });
        }
    } else {
        Ext.Msg.show({
            title    : 'Error'
            ,icon    : Ext.Msg.WARNING
            ,msg     : 'Seleccione un asegurado'
            ,buttons : Ext.Msg.OK
        });
    }
}
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{	
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
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
    
	Ext.define('panendabaseguModelo',
	{
		extend  : 'Ext.data.Model'
		,fields :
		[
		    {
		    	name  : 'activo'
		    	,type : 'boolean'
		    }
		    ,<s:property value="imap1.modelo" />
		]
	});
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	_3_storeClaUsa = new Ext.data.Store(
    {
        model  : 'ClausulaModel'
        ,proxy :
        {
            type    : 'memory'
            ,reader : 'json'
            ,data   : []
        }
    });
	
	_3_storeClaTipos = new Ext.data.Store(
    {
        model      : 'ModeloTipoClausula'
        ,autoLoad  : true
        ,proxy     :
        {
            url     : _3_urlCargarClaTipos
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
    
    _3_storeClaDisp = new Ext.data.Store(
    {
        model      : 'ClausulaModel'
        ,autoLoad  : false
        ,proxy     :
        {
            url     : _3_urlCargarClaDisp
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
	panendabaseguStoreAsegu = Ext.create('Ext.data.Store',
    {
        autoLoad : true
        ,model   : 'panendabaseguModelo'
        ,proxy   :
        {
            type         : 'ajax'
            ,url         : panendabaseguUrlLoadAsegu
            ,extraParams :
            {
            	 'map1.pv_cdunieco' : panendabaseguInputSmap1.CDUNIECO
            	,'map1.pv_cdramo'   : panendabaseguInputSmap1.CDRAMO
            	,'map1.pv_estado'   : panendabaseguInputSmap1.ESTADO
            	,'map1.pv_nmpoliza' : panendabaseguInputSmap1.NMPOLIZA
            	,'map1.pv_nmsuplem' : panendabaseguInputSmap1.NMSUPLEM
            }
            ,reader      :
            {
            	type  : 'json'
            	,root : 'list1'
            }
        }
    });
	
	panEndAltBajAseStoreAltas = Ext.create('Ext.data.Store',
    {
        model  : 'panendabaseguModelo'
        ,proxy :
        {
            type    : 'memory'
            ,reader : 'json'
            ,data   : []
        }
    });
	
	panEndAltBajAseStoreBajas = Ext.create('Ext.data.Store',
    {
        model  : 'panendabaseguModelo'
        ,proxy :
        {
            type    : 'memory'
            ,reader : 'json'
            ,data   : []
        }
    });
	/*////////////////*/
	////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	/*/////////////////////*/
	Ext.define('_3_PanelCla',
    {
		extend    : 'Ext.panel.Panel'
		,hidden   : panendabaseguInputSmap2.alta=='no'
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
                        ,store          : _3_storeClaTipos
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
                                    _3_storeClaDisp.load(
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
                                        _3_storeClaDisp.load(
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
                ,store         : _3_storeClaDisp
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
                                    var record=_3_storeClaDisp.getAt(rowIndex);
                                    debug('clausula a agregar:',record);
                                    Ext.Ajax.request(
                                    {
                                        url     : _3_urlLoadHtml
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
                                                                	_3_storeClaUsa.add(record);
                                                                	_3_storeClaDisp.remove(record);
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
                ,store         : _3_storeClaUsa
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
                                var record=_3_storeClaUsa.getAt(rowIndex);
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
                                var record=_3_storeClaUsa.getAt(rowIndex);
                                debug('clausula eliminada:',record);
                                _3_storeClaUsa.remove(record);
                            }//end if cell index = 2
                        }//end if find img
                    }
                }
            })
        ]
    });
	
	Ext.define('_3_Form',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_3_Form initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,items    :
                [
                    {
                        xtype       : 'datefield'
                        ,id         : '_3_fieldFechaId'
                        ,format     : 'd/m/Y'
                        ,fieldLabel : 'Fecha de efecto'
                        ,allowBlank : false
                        ,value      : new Date()
                        ,name       : 'fecha_endoso'
                    }
                ]
                ,defaults : 
                {
                    style : 'margin : 5px;'
                }
            });
            this.callParent();
        }
    });
	
	Ext.define('PanEndAltBajAseWindowAsegu',
	{
		extend         : 'Ext.window.Window'
		,initComponent : function()
        {
			debug('PanEndAltBajAseWindowAsegu initComponent');
			Ext.apply(this,
			{
				title        : 'Agregar asegurado'
		        //,icon        : '${ctx}/resources/fam3icons/icons/add.png'
		        ,width       : 700
		        ,modal       : true
		        ,height      : 400
		        ,autoScroll  : true
		        ,closeAction : 'hide'
		        ,items       :
		        [
			        Ext.create('Ext.form.Panel',
			        {
			        	id           : '_3_formNuevo'
			        	,items       :
			        	[
			        	    <s:property value="imap1.formulario" />
			        	    ,<s:property value="imap1.formularioAtriper" />
			        	]
				        ,layout      :
				        {
				        	type     : 'table'
				        	,columns : 2
				        }
			            ,buttonAlign : 'center'
			            ,buttons     :
			            [
			                {
			                	text     : 'Aceptar'
			                	,icon    : '${ctx}/resources/fam3icons/icons/add.png'
			                	,handler : function()
			                	{
			                		var form=this.up().up();
			                		var _window=form.up();
			                		if(form.isValid())
			                		{
			                			panEndAltBajAseValues=form.getValues();
			                			debug('panEndAltBajAseValues',panEndAltBajAseValues);
			                			panEndAltBajAseStoreAltas.removeAll();
			                			var parentescoName = _fieldById('_3_formNuevo').down('[fieldLabel=PARENTESCO]').name;
			                			var sexoName       = _fieldById('_3_formNuevo').down('[fieldLabel=SEXO]').name;
			                			var nacimientoName = _fieldById('_3_formNuevo').down('[fieldLabel*=NACIMIENTO]').name;
			                			panEndAltBajAseStoreAltas.add(
			                			{
			                				nmsituac          : panEndAltBajAseNmsituac
			                				,nombre           : panEndAltBajAseValues['nombre']
			                				,segundo_nombre   : panEndAltBajAseValues['nombre2']
			                			    ,Apellido_Paterno : panEndAltBajAseValues['apat']
			                			    ,Apellido_Materno : panEndAltBajAseValues['amat']
			                			    ,cdrol            : 2
			                			    ,Parentesco       : panEndAltBajAseValues[parentescoName]
			                			    ,cdrfc            : panEndAltBajAseValues['rfc']
			                			    ,tpersona         : panEndAltBajAseValues['tpersona']
			                			    ,sexo             : panEndAltBajAseValues[sexoName]
			                			    ,fenacimi         : panEndAltBajAseValues[nacimientoName]
			                			});
			                			_window.hide();
			                		}
			                		else
			                		{
			                			Ext.Msg.show(
	                			        {
	                			            title    : 'Error'
	                			            ,icon    : Ext.Msg.WARNING
	                			            ,msg     : 'Datos incompletos'
	                			            ,buttons : Ext.Msg.OK
	                			        });
			                		}
			                	}
			                }
			            ]
			        })
		        ]
			});
			this.callParent();
        }
	});
	Ext.define('PanEndAltBajAseGridAltas',
    {
        extend         : 'Ext.grid.Panel'
        ,initComponent : function()
        {
            debug('PanEndAltBajAseGridAltas initComponent');
            Ext.apply(this,
            {
                title      : 'Alta de asegurados'
                //,icon      : '${ctx}/resources/fam3icons/icons/add.png'
                ,store     : panEndAltBajAseStoreAltas
                ,minHeight : 100
                ,hidden    : panendabaseguInputSmap2.alta=='no'
                ,columns   :
                [
                    <s:property value="imap1.columnas" />
                    ,{
                    	xtype         : 'actioncolumn'
                    	,width        : 30
                    	,icon         : '${ctx}/resources/fam3icons/icons/delete.png'
                    	,tooltip      : 'No agregar'
                    	,menuDisabled : true
                    	,sortable     : false
                    	,handler      : this.onDeleteClick
                    }
                ]
                ,listeners :
                {
                	cellclick : _3_cellclick
                }
            });
            this.callParent();
        }
	    ,onDeleteClick : function(grid,rowIndex)
	    {
	    	grid.getStore().removeAt(rowIndex);
	    }
    });
	Ext.define('PanEndAltBajAseGridBajas',
    {
        extend         : 'Ext.grid.Panel'
        ,initComponent : function()
        {
            debug('PanEndAltBajAseGridBajas initComponent');
            Ext.apply(this,
            {
                title      : 'Baja de asegurados'
                //,icon      : '${ctx}/resources/fam3icons/icons/delete.png'
                ,store     : panEndAltBajAseStoreBajas
                ,minHeight : 100
                ,hidden    : panendabaseguInputSmap2.alta=='si'
                ,columns   :
                [
                    <s:property value="imap1.columnas" />
                    ,{
                        xtype         : 'actioncolumn'
                        ,width        : 30
                        ,icon         : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,tooltip      : 'No quitar'
                       	,menuDisabled : true
                        ,sortable     : false
                        ,handler      : this.onDeleteClick
                    }
                ]
            });
            this.callParent();
        }
        ,onDeleteClick : function(grid,rowIndex)
        {
            var record = grid.getStore().getAt(rowIndex);
            grid.getStore().removeAt(rowIndex);
            panendabaseguStoreAsegu.add(record);
        }
    });
	Ext.define('PanendabaseguPanelLectura',
    {
        extend         : 'Ext.panel.Panel'
        ,initComponent : function()
        {
            debug('PanendabaseguPanelLectura initComponent');
            Ext.apply(this,
            {
                title   : 'P&oacute;liza afectada'
                ,layout :
                {
                	columns : 3
                	,type   : 'table'
                }
                ,items  :
                [
                    <s:property value="imap1.panelLectura" />
                ]
            });
            this.callParent();
        }
    });
	Ext.define('PanendabaseguGridAsegu',
	{
		extend         : 'Ext.grid.Panel'
		,initComponent : function()
		{
			debug('PanendabaseguGridAsegu initComponent');
			Ext.apply(this,
			{
				title    : 'Asegurados'
		        ,icon    : '${ctx}/resources/fam3icons/icons/user.png'
				,store   : panendabaseguStoreAsegu
				,minHeight : 100
				,maxHeight : 350
				,tbar    :
				[
				    {
				    	text     : 'Agregar'
				    	,icon    : '${ctx}/resources/fam3icons/icons/add.png'
				    	,handler : panendabaseguFunAgregar
				    	,hidden  : panendabaseguInputSmap2.alta=='no'
				    }
				    ,{
                        text     : 'Quitar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,handler : panendabaseguFunQuitar
                        ,hidden  : panendabaseguInputSmap2.alta=='si'
                    }
				]
				,columns :
				[
					{
					    dataIndex     : 'activo'
					    ,xtype        : 'checkcolumn'
					    ,width        : 30
					    ,menuDisabled : true
					    ,sortable     : false
					}
				    ,<s:property value="imap1.columnas" />
				]
			});
			this.callParent();
		}
	});
	Ext.define('PanendabaseguPanelPrincipal',
    {
        extend         : 'Ext.panel.Panel'
        ,initComponent : function()
        {
            debug('PanendabaseguPanelPrincipal initComponent');
            Ext.apply(this,
            {
            	border       : 0
            	,defaults    :
            	{
            		style : 'margin : 5px;'
            	}
                ,items       :
                [
                    panendabaseguPanelLectura
                    ,panendabaseguGridAsegu
                    ,panEndAltBajAseGridAltas
                    ,_3_panelCla
                    ,panEndAltBajAseGridBajas
                    ,_3_form
                ]
                ,renderTo    : 'panendabaseDivPri'
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                    	text     : 'Confirmar endoso'
                    	,id      : 'panEndAltBajAseBotConfirmar'
                    	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                    	,handler : this.onConfirmarClick
                    }
                    ,{
                        text     : 'Documentos'
                        ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                        ,handler : function()
                        {
                            Ext.create('Ext.window.Window',
                            {
                                title        : 'Documentos del tr&aacute;mite '+panendabaseguInputSmap1.NTRAMITE
                                ,modal       : true
                                ,buttonAlign : 'center'
                                ,width       : 600
                                ,height      : 400
                                ,autoScroll  : true
                                ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                ,loader      :
                                {
                                    url       : panEndAltBajAseUrlDoc
                                    ,params   :
                                    {
                                         'smap1.cdunieco' : panendabaseguInputSmap1.CDUNIECO
                                        ,'smap1.cdramo'   : panendabaseguInputSmap1.CDRAMO
                                        ,'smap1.estado'   : panendabaseguInputSmap1.ESTADO
                                        ,'smap1.nmpoliza' : panendabaseguInputSmap1.NMPOLIZA
                                        ,'smap1.nmsuplem' : '0'
                                        ,'smap1.ntramite' : panendabaseguInputSmap1.NTRAMITE
                                        ,'smap1.nmsolici' : ''
                                        ,'smap1.tipomov'  : '0'
                                    }
                                    ,scripts  : true
                                    ,autoLoad : true
                                }
                            }).show();
                        }
                    }
                ]
            });
            this.callParent();
        }
	    ,onConfirmarClick : function()
	    {
	    	debug('onConfirmarClick');
	    	
	    	var valido=true;
	    	
	    	if(valido)
	    	{
	    		valido=panEndAltBajAseStoreAltas.getCount()+panEndAltBajAseStoreBajas.getCount()==1;
	    		if(!valido)
	    		{
	    			mensajeWarning('No hay alta ni baja de asegurados');
	    		}
	    	}
	    	
	    	if(valido)
	    	{
	    		valido=_3_form.isValid();
	    		if(!valido)
	    		{
	    			datosIncompletos();
	    		}
	    	}
	    	
	    	if(valido)
	    	{
	    		var json={};
	    		if(panEndAltBajAseStoreAltas.getCount()==1)
	    		{
	    			json['smap1']=panEndAltBajAseValues;
	    			json['smap1']['nmsituac'] = panEndAltBajAseNmsituac;
	    		}
	    		else
	    		{
	    			json['smap1']=panEndAltBajAseStoreBajas.getAt(0).raw;
	    		}
	    		json['smap2'] = panendabaseguInputSmap1;
	    		json['smap3'] = _3_form.getValues();
	    		
	    		json['slist1'] = [];
	    		_3_storeClaUsa.each(function(record)
	    		{
	    			json['slist1'].push(record.getData());
	    		});
	    		
	    		if(!Ext.isEmpty(_endAseg_flujo))
	    		{
	    		    json.flujo = _endAseg_flujo;
	    		}
	    		
	    		debug('json:',json);
	    		_setLoading(true,panendabaseguPanelPrincipal);
	    		Ext.Ajax.request(
	    		{
	    			url       : panendabaseguUrlSave
	    			,jsonData : json
	    			,success  : function(response)
		            {
	    				_setLoading(false,panendabaseguPanelPrincipal);
	    				json=Ext.decode(response.responseText);
	    				debug('response',json);
	    				if(json.success==true)
	    				{
	    					Ext.getCmp('panEndAltBajAseBotConfirmar').hide();
	    					
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
                                    ,panendabaseguInputSmap1.CDUNIECO
                                    ,panendabaseguInputSmap1.CDRAMO
                                    ,panendabaseguInputSmap1.ESTADO
                                    ,panendabaseguInputSmap1.NMPOLIZA
                                    ,callbackRemesa
                                );
                            });
	    				}
	    				else
	    				{
	    					if(json.error&&json.error.length>0)
                            {
                                mensajeError(json.error);
                            }
                            else
                            {
                            	errorComunicacion();
                            }
	    				}
		            }
	    		    ,failure  : function()
	    		    {
	    		    	_setLoading(false,panendabaseguPanelPrincipal);
	    		    	errorComunicacion();
	    		    }
	    		});
	    	}
	    }
    });
	/*/////////////////////*/
	////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	if(panEndAltBajAseWindowAsegu)
	{
		debug('destroy panEndAltBajAseWindowAsegu anterior');
		panEndAltBajAseWindowAsegu.destroy();
	}
	_3_panelCla                 = new _3_PanelCla();
	panEndAltBajAseWindowAsegu  = new PanEndAltBajAseWindowAsegu();
	_3_form                     = new _3_Form();
	panEndAltBajAseGridAltas    = new PanEndAltBajAseGridAltas();
	panEndAltBajAseGridBajas    = new PanEndAltBajAseGridBajas();
	panendabaseguGridAsegu      = new PanendabaseguGridAsegu();
	panendabaseguPanelLectura   = new PanendabaseguPanelLectura();
	panendabaseguPanelPrincipal = new PanendabaseguPanelPrincipal(); 
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
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="panendabaseDivPri" style="height:1500px;"></div>