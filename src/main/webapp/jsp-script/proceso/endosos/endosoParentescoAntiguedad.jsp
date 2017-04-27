<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/

var pantallaValositUrlLoad       = '<s:url namespace="/"           action="pantallaValositLoad" />';
var pantallaValositUrlSave       = '<s:url namespace="/endosos"    action="guardarEndosoValositBasico" />';
var pantallaValositUrlSaveSimple = '<s:url namespace="/endosos"    action="guardarEndosoValositBasicoSimple" />';
var _urlConfirmaEndosoParentesco = '<s:url namespace="/endosos"    action="guardarEndosoParentescoAntiguedad" />';
var pantallaValositUrlDoc        = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza" />';

var _cargaAseguradosPol = '<s:url namespace="/endosos" action="cargaAsegurados" />';
var _URL_CARGA_CATALOGO ='<s:url namespace="/catalogos"       action="obtieneCatalogo" />';
var _catalogoParentesco = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PARENTESCO"/>';

var pantallaValositInput                 = [];
    pantallaValositInput['cdunieco']     = '<s:property value="smap1.CDUNIECO" />';
    pantallaValositInput['cdramo']       = '<s:property value="smap1.CDRAMO" />';
    pantallaValositInput['estado']       = '<s:property value="smap1.ESTADO" />';
    pantallaValositInput['nmpoliza']     = '<s:property value="smap1.NMPOLIZA" />';
    pantallaValositInput['cdtipsit']     = '<s:property value="smap1.CDTIPSIT" />';
    pantallaValositInput['nmsituac']     = '';
    pantallaValositInput['ntramite']     = '<s:property value="smap1.NTRAMITE" />';
    pantallaValositInput['nmsuplem']     = '<s:property value="smap1.NMSUPLEM" />';
    pantallaValositInput['feinival']     = '<s:property value="smap1.FEINIVAL" />';
    pantallaValositInput['endososimple'] = <s:property value="endosoSimple" />;
    pantallaValositInput['fechainicio']  = pantallaValositInput['endososimple'] ? '<s:property value="mensaje" />' : new Date();
    
var pantallaValositEsTitular        = false;
var pantallaValositIndiceParentesco = 'parametros.pv_otvalor16';
if(pantallaValositInput['cdtipsit']=='MS')
{
	pantallaValositIndiceParentesco = 'parametros.pv_otvalor03';
}


var _storeAsegurados;
var _storeParentescos;
var _gridAsegurados;

debug('input',pantallaValositInput);

var _endParen_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_endParen_flujo:',_endParen_flujo);
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
function endvalbasSumit(form,confirmar){
	
		
        var asegurados=[];
        var existeTitular = false;
        var valido = true;
        
        _storeAsegurados.each(function(record){
        	var data = record.getData();
        	
    		if(!Ext.isEmpty(data) && !Ext.isEmpty(data.CDPARENTESCO) && 'T' == data.CDPARENTESCO){
        		if(existeTitular){
        			//doble titular
        			valido = false;
        		}
        		existeTitular = true;
        	}
        });
        
        
        /**
         * SI HAY DOBLE TITULAR 
         */
        if(!valido){
        	debug('Existe mas de un titular.');
        	mensajeWarning('Existe mas de un titular.');
        	return;
        }
        
        if(!existeTitular){
        	debug('Debe de existir un titular.');
        	mensajeWarning('Debe de existir un titular.');
        	return;
        }
        
        var slist1=[];
        _storeAsegurados.getUpdatedRecords().forEach(function(record,index,arr){
        	var data = record.getData();
//    		data.CDAGENTEORIG = record.modified.CDAGENTE;
    		slist1.push(data);
        	
        });
        
        if(slist1.length <= 0){
        	mensajeWarning('No hay cambios en los asegurados para realizar el endoso.');
        	return;
        }
	
	
	var fechaEndoso = _fieldByNameDown('smap1.fecha_endoso',form); 
	
	var json={
            smap1   : {
                    cdunieco   : pantallaValositInput['cdunieco']
                    ,cdramo    : pantallaValositInput['cdramo']
                    ,estado    : pantallaValositInput['estado']
                    ,nmpoliza  : pantallaValositInput['nmpoliza']
                    ,cdtipsit  : pantallaValositInput['cdtipsit']
                    ,fecha_endoso  : fechaEndoso.getRawValue()
//                    ,nmsituac  : pantallaValositInput['nmsituac']
                    ,confirmar : confirmar
                    ,simple    : true == pantallaValositInput['endososimple'] ? 'S' : 'N'
            }
            ,slist1 : slist1
        }
    
    if(!Ext.isEmpty(_endParen_flujo))
    {
        json.flujo = _endParen_flujo;
    }
    
    debug('datos que se enviaran:',json);
        
	_setLoading(true,form);
	
	Ext.Ajax.request(
        {
            url       : _urlConfirmaEndosoParentesco
            ,jsonData : json
            ,timeout  : 8*60*1000
            ,success  : function(response)
            {
                var json=Ext.decode(response.responseText);
                debug('datos recibidos:',json);
                if(json.success==true)
                {
                    _setLoading(false,form);
                    Ext.Msg.show(
                    {
                        title   : 'Endoso generado',
                        msg     : json.mensaje,
                        buttons : Ext.Msg.OK,
                        fn      : function()
                        {
                            if(confirmar=='si')
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
                                _generarRemesaClic(
                                    true
                                    ,pantallaValositInput['cdunieco']
                                    ,pantallaValositInput['cdramo']
                                    ,pantallaValositInput['estado']
                                    ,pantallaValositInput['nmpoliza']
                                    ,callbackRemesa
                                );
                            }
                        }
                    });
                }
                else
                {
                    _setLoading(false,form);
                	var json=Ext.decode(response.responseText);
                    mensajeError(json.error);
                }
            }
            ,failure  : function()
            {
                _setLoading(false,form);
                errorComunicacion();
            }
        });
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
    Ext.define('EndValBasModelo',
    {
        extend:'Ext.data.Model'
        ,<s:property value="item1" />
    });
    
    Ext.define('_modeloAsegurado',
    {
    	extend  : 'Ext.data.Model'
    	,fields : ['CDPERSON','NMSITUAC','CDROL', 'DSROL','PARENTESCO','CDPARENTESCO','FECANTIG','TITULAR']
    });
    /*/////////////////*/   
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    
    _storeAsegurados = Ext.create('Ext.data.Store',
    {
		autoLoad : false
    	,model   : '_modeloAsegurado'
        ,proxy: {
            type: 'ajax',
            url : _cargaAseguradosPol,
            reader: {
                type: 'json',
                root: 'slist1'
            }
        }
    });
    
    _storeParentescos = Ext.create('Ext.data.Store', {
        model     : 'Generic',
        proxy     : {
            type        : 'ajax'
            ,url        : _URL_CARGA_CATALOGO
            ,extraParams: {catalogo:_catalogoParentesco}
            ,reader     :
            {
                type  : 'json'
                ,root : 'lista'
            }
        }
    });
    
    _storeParentescos.load();
    
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    
    Ext.define('_gridAsegurados',
    {
    	extend         : 'Ext.grid.Panel'
    	,initComponent : function()
    	{
    		Ext.apply(this,
    		{
    			title    : 'Asegurados'
    			,store   : _storeAsegurados
    			,columns : [{
		            header     : 'Asegurado'
		            ,dataIndex : 'TITULAR'
		            ,flex      : 1
		        },{
		            header     : 'Parentesco'
		            ,dataIndex : 'PARENTESCO'
		            ,flex      : 1
		        },{
		            header     : 'Antig&#252;edad'
		            ,dataIndex : 'FECANTIG'
		            
		            ,flex      : 1
		        }]
    			,tbar:[{
		                text     : 'Modificar Asegurado'
		                ,icon    : '${ctx}/resources/fam3icons/icons/user_go.png'
		                ,handler : function(){
		                	if(_gridAsegurados.getSelectionModel().hasSelection()){
		                		var recordSel = _gridAsegurados.getSelectionModel().getLastSelected();
		                		
		                		var windowAsegurado = Ext.create('Ext.window.Window', {
								  title : 'Cambiar Informaci&oacute;n del Asegurado: ' + recordSel.get('TITULAR'),
						          modal:true,
						          width : 380,
						          height : 150,
						          bodyStyle:'padding:5px;',
						          items:[{
						                xtype         : 'combobox',
						                fieldLabel    : 'Parentesco',
						                allowBlank    : false,
						                name          : 'PARENTESCO_IN',
						                valueField    : 'key',
						                displayField  : 'value',
						                forceSelection: true,
						                value         : recordSel.get('CDPARENTESCO'),   
						                store         : _storeParentescos
						               
						            },{
						                xtype       : 'datefield',
						                fieldLabel  : 'Antig&#252;edad',
						                name        : 'FECANTIG_IN',
						                format      : 'd/m/Y',
						                value       : recordSel.get('FECANTIG')
						            }],
						          buttons:[{
						        	  text: 'Aceptar',
						                 icon:'${ctx}/resources/fam3icons/icons/accept.png',
							                 handler: function() {
							                 	var comboParentesco = _fieldByNameDown('PARENTESCO_IN',windowAsegurado);
							                 	var campoAntiguedad = _fieldByNameDown('FECANTIG_IN',windowAsegurado);
							                 	
							                 	if(comboParentesco.isValid()){
								                 	recordSel.set('CDPARENTESCO',comboParentesco.getValue());
								                 	recordSel.set('PARENTESCO',comboParentesco.getRawValue());
								                 	recordSel.set('FECANTIG',campoAntiguedad.getRawValue());
								                	
								                	windowAsegurado.close();
							                 	}else{
							                 		comboParentesco.reset();
							                 		showMessage("Aviso","Debe seleccionar un Parentesco y Fecha de Antig&#252;edad.", Ext.Msg.OK, Ext.Msg.INFO);
							                 	}
							                 }
						           		},{
						        	  	 text: 'Cancelar',
						                 icon:'${ctx}/resources/fam3icons/icons/cancel.png',
							                 handler: function() {
							                 	windowAsegurado.close();
							                 }
						           		} ]
						          });
							 	windowAsegurado.show();
		                	}else{
		                		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		                	}
		                	
		                }
		            },'-',{
		                text     : 'Quitar Cambios'
		                ,icon    : '${ctx}/resources/fam3icons/icons/arrow_rotate_anticlockwise.png'
		                ,handler : function(btn,e){
			                	Ext.Msg.show({
		                        title: 'Confirmar acci&oacute;n',
		                        msg   : '&iquest;Esta seguro que desea quitar todos los cambios?',
		                        buttons: Ext.Msg.YESNO,
		                        fn: function(buttonId, text, opt) {
		                            if(buttonId == 'yes') {
		                        		_storeAsegurados.rejectChanges();    
		                            }
			                    },
		                        animateTarget: btn,
		                        icon: Ext.Msg.QUESTION
		                    });
		                }
		            }
    			]
    		});
    		this.callParent();
    	}
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    _gridAsegurados = new _gridAsegurados();
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/

    Ext.create('Ext.form.Panel',
    {
    	defaults     :
    	{
    		style : 'margin : 5px;'
    	}
        ,border      : 0
        ,renderTo    : 'maindivpantallavalosit'
        ,buttonAlign : 'center'
        ,url         : pantallaValositInput['endososimple'] ? pantallaValositUrlSaveSimple : pantallaValositUrlSave
        ,items       :
        [
            _gridAsegurados
            ,Ext.create('Ext.panel.Panel',
            {
            	title     : 'Informaci&oacute;n del endoso'
            	,layout   :
            	{
            		type     : 'table'
            		,columns : 2 
            	}
            	,defaults :
                {
                    style : 'margin : 5px;'
                }
            	,items    :
            	[
            	    {
                        xtype       : 'datefield'
                        ,fieldLabel : 'Fecha de efecto'
                        ,format     : 'd/m/Y'
                        ,value      : pantallaValositInput['feinival']
                        ,allowBlank : false
                        ,name       : 'smap1.fecha_endoso'
                        ,readOnly   : true//pantallaValositInput['endososimple']
                    }
            	]
            })
        ]
        ,buttons     :
        [
            {
                text     : 'Guardar endoso'
                ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                ,handler : function(me)
                {
                    var form=me.up().up();
                    endvalbasSumit(form,'no');
                }
                ,hidden   : true // TODO:Quitar propiedad cuando se arregle duplicidad de PKG_SATELITES.P_MOV_MPOLISIT
            }
            ,{
                text     : 'Confirmar endoso'
                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                ,handler : function(me)
                {
                    var form=me.up().up();
                    endvalbasSumit(form,'si');
                }
            }
            ,{
                text     : 'Documentos'
                ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                ,handler : function()
                {
                    Ext.create('Ext.window.Window',
                    {
                        title        : 'Documentos del tr&aacute;mite '+pantallaValositInput['ntramite']
                        ,modal       : true
                        ,buttonAlign : 'center'
                        ,width       : 600
                        ,height      : 400
                        ,autoScroll  : true
                        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                        ,loader      :
                        {
                            url       : pantallaValositUrlDoc
                            ,params   :
                            {
                                'smap1.nmpoliza'  : pantallaValositInput['nmpoliza']
                                ,'smap1.cdunieco' : pantallaValositInput['cdunieco']
                                ,'smap1.cdramo'   : pantallaValositInput['cdramo']
                                ,'smap1.estado'   : pantallaValositInput['estado']
                                ,'smap1.nmsuplem' : '0'
                                ,'smap1.ntramite' : pantallaValositInput['ntramite']
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
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    Ext.define('LoaderForm',
    {
        extend:'EndValBasModelo',
        proxy:
        {
            extraParams:
            {
                'smap1.pv_cdunieco_i'  : pantallaValositInput['cdunieco']
                ,'smap1.pv_nmpoliza_i' : pantallaValositInput['nmpoliza']
                ,'smap1.pv_cdramo_i'   : pantallaValositInput['cdramo']
                ,'smap1.pv_estado_i'   : pantallaValositInput['estado']
                ,'smap1.pv_nmsituac_i' : pantallaValositInput['nmsituac']
            },
            type:'ajax',
            url : pantallaValositUrlLoad,
            reader:{
                type:'json'
            }
        }
    });

//    var loaderForm=Ext.ModelManager.getModel('LoaderForm');
//    loaderForm.load(123, {
//        success: function(resp) {
//            //console.log(resp);
//            pantallaValositMainContent.loadRecord(resp);
//            if(resp.get(pantallaValositIndiceParentesco)=='T')
//            {
//            	debug('sin cambiar titular');
//            	pantallaValositEsTitular=true;
//            }
//        },
//        failure:function()
//        {
//            Ext.Msg.show({
//                title:'Error',
//                icon: Ext.Msg.ERROR,
//                msg: 'Error al cargar',
//                buttons: Ext.Msg.OK
//            });
//        }
//    });
    
    _setLoading(true,_gridAsegurados);
    _storeAsegurados.load({
		params: {
    		'smap1.cdunieco'  : pantallaValositInput['cdunieco']
    		,'smap1.cdramo'   : pantallaValositInput['cdramo']
    		,'smap1.estado'   : pantallaValositInput['estado']
    		,'smap1.nmpoliza' : pantallaValositInput['nmpoliza']
    		,'smap1.nmsuplem' : pantallaValositInput['nmsuplem']
    	},
		callback: function(records, operation, success){
			_setLoading(false,_gridAsegurados);
    		if(!success){
    			mensajeError('Error al cargar los Asegurados.');
    			//////////////////////////////////
                ////// usa codigo del padre //////
                /*//////////////////////////////*/
                marendNavegacion(4);
                /*//////////////////////////////*/
                ////// usa codigo del padre //////
                //////////////////////////////////
    		}
		}
	});
    /*//////////////////*/    
    ////// cargador //////
    //////////////////////
});
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="maindivpantallavalosit" style="min-height:150px;"></div>