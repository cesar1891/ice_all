<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.x-action-col-icon {
    height: 16px;
    width: 16px;
    margin-right: 8px;
}
</style>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var _CONTEXT = '${ctx}';
    var datosgrid;
	var storeIncisos;
	var gridIncisos;
	var mesConUrlDocu          = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza" />';
	
    
    /* DATOS X VERIFICAR */
    var mesConUrlLoadTareas    = '<s:url namespace="/mesacontrol"     action="loadTareas" />';
    var mesConStoreTareas;
    var mesConGridTareas;
    var mesConStoreUniAdmin;
    var mesconInput            = [];
    /*mesconInput['cdunieco']    = '<s:property value="smap1.pv_cdunieco_i" />';
    mesconInput['ntramite']    = '<s:property value="smap1.pv_ntramite_i" />';
    mesconInput['cdramo']      = '<s:property value="smap1.pv_cdramo_i"   />';
    mesconInput['nmpoliza']    = '<s:property value="smap1.pv_nmpoliza_i" />';
    mesconInput['estado']      = '<s:property value="smap1.pv_estado_i"   />';
    mesconInput['cdagente']    = '<s:property value="smap1.pv_cdagente_i" />';
    mesconInput['status']      = '<s:property value="smap1.pv_status_i"   />';
    mesconInput['cdtipsit']    = '<s:property value="smap1.pv_cdtipsit_i" />';
    mesconInput['fedesde']     = '<s:property value="smap1.pv_fedesde_i"  />';
    mesconInput['fehasta']     = '<s:property value="smap1.pv_fehasta_i"  />';*/
    
    //debug('mesConInput',mesconInput);
    
    
   
	
	
Ext.onReady(function(){
    
	//debug('ready');
	
	
	/*//////////////////////////
	/////////  MODELOS /////////
	//////////////////////////*/
    Ext.define('modelClau',
	{
	    extend:'Ext.data.Model',
	    fields:['noFactura','fechaFactura','tipoServicio','proveedor','importe']
	});
	
    Ext.define('Tarea',
	{
        extend:'Ext.data.Model',
        fields:
        [
			"ntramite","cdunieco","cdramo","estado","nmpoliza",
			"nmsolici","cdsucadm","cdsucdoc","cdsubram","cdtiptra",{name:"ferecepc",type:"date",dateFormat:"d/m/Y"},"cdagente",
			"Nombre_agente","referencia","nombre",{name:"fecstatu",type:"date",dateFormat:"d/m/Y"},"status","comments","cdtipsit"
        ]
    });
    
    /*//////////////////////////
	/////////   STORE  /////////
	//////////////////////////*/
	storeIncisos=new Ext.data.Store(
	{
	    autoDestroy: true,
	    model: 'modelClau'
	});
    
    mesConStoreTareas=Ext.create('Ext.data.Store',
    {
        pageSize : 10,
        autoLoad : true,
        model    : 'Tarea',
        proxy    :
        {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
        ,listeners :
        {
        	load : function (action,records)
        	{
        		//debug("records",records);
        	}
        }
    });
    /*//////////////////////////////////////
    ///////  DECLARACION DE VARIABLES //////
    //////////////////////////////////////*/
	oficinaEmisora= Ext.create('Ext.form.ComboBox',
	{
    	id:'oficinaEmisora',		name:'params.oficinaEmisora',		fieldLabel: 'Oficina emisora',			queryMode:'local',
	    displayField: 'value',	    valueField: 'key',				    allowBlank:false,					    editable:false,
	    labelWidth : 250,		    emptyText:'Seleccione...'			//store: storeCirculoHospitalario,  
	});
    
    oficinaDocumento= Ext.create('Ext.form.ComboBox',
  	{
    	id:'oficinaDocumento',	   name:'params.oficinaDocumento',      fieldLabel: 'Oficina documento',		queryMode:'local',		   
    	displayField: 'value',	   valueField: 'key',					allowBlank:false,				        editable:false,
    	labelWidth : 250,		   emptyText:'Seleccione...'			//store: storeCirculoHospitalario
   	});   
    	    
    tipoPago= Ext.create('Ext.form.ComboBox',
    {
        id:'tipoPago',			   name:'params.tipoPago',		        fieldLabel: 'Tipo pago',				queryMode:'local',
        displayField: 'value',	   valueField: 'key',					allowBlank:false,						editable:false,
        labelWidth : 250,		   emptyText:'Seleccione...'			//store: storeCirculoHospitalario
    });
    
    
    
    
    
    aseguradoAfectado= Ext.create('Ext.form.ComboBox',
    {
        id:'aseguradoAfectado',		name:'params.aseguradoAfectado',	fieldLabel: 'Asegurado afectado',		queryMode:'local',
        displayField: 'value',      valueField: 'key',        			allowBlank:false,        				editable:false,        
        labelWidth : 250,        	emptyText:'Seleccione...'        	//store: storeCirculoHospitalario
    });
    
    beneficiario= Ext.create('Ext.form.ComboBox',
	{
        id:'beneficiario',        name:'params.beneficiario',        	fieldLabel: '*(R) Beneficiario',        queryMode:'local',        
        displayField: 'value',    valueField: 'key',        			allowBlank:false,        				editable:false,        
        labelWidth : 250,         emptyText:'Seleccione...'        		//store: storeCirculoHospitalario
        
    });
    
    proveedor= Ext.create('Ext.form.ComboBox',
    {
        id:'proveedor',        	  name:'params.proveedor',        		fieldLabel: '*(PD) Proveedor',       	queryMode:'local',        
        displayField: 'value',    valueField: 'key',        			allowBlank:false,        				editable:false,        
        labelWidth : 250,        emptyText:'Seleccione...'        		//store: storeCirculoHospitalario
    });
    
    motivoRechazo= Ext.create('Ext.form.ComboBox',
    {
        id:'motivoRechazo',		 name:'params.motivoRechazo',           fieldLabel: 'Motivo',		queryMode:'local',		displayField: 'value',
        valueField: 'key',		 allowBlank:false,						blankText:'El motivo es un dato requerido',	        editable:false,
        labelWidth : 150,		 width: 600,							emptyText:'Seleccione un Motivo ...'		        //store: storeCirculoHospitalario        
    });

    proveedor2= Ext.create('Ext.form.ComboBox',
    	    {
    	        id:'proveedor2',        	  name:'params.proveedor2',        		fieldLabel: 'Proveedor',       	queryMode:'local',        
    	        displayField: 'value',    valueField: 'key',        			allowBlank:false,        				editable:false,        
    	        labelWidth : 170,width:500 ,       emptyText:'Seleccione...'        		//store: storeCirculoHospitalario
    	    });
    
    /*{
    	id:'txtImporte'
        ,xtype      : 'textfield'
    	,fieldLabel : '*(PD) Importe'
    	,readOnly   : true
    	,labelWidth : 250
    	,name       : 'params.importe'
    },
    {
        id: 'fechaFactura',
        name: 'params.fechaFactura',
        fieldLabel: '*(PD) Fecha Factura',
        xtype: 'datefield',
        format: 'd/m/Y',
        editable: true,
        allowBlank:false,
        labelWidth : 250,
        value:new Date()
    }*/
    
    

    var panelModificacionInsercion= Ext.create('Ext.form.Panel',{
        border  : 0
        ,bodyStyle:'padding:5px;'
        ,items :
        [   
             {
		    	id:'noFactInterno'
		        ,xtype      : 'textfield'
		    	,fieldLabel : 'No. Factura'
	    		,labelWidth: 170
	    		,allowBlank:false
		    	,name       : 'noFactInterno'
    		},            
    		{
    	        id: 'fechaFactInterno',
    	        name: 'fechaFactInterno',
    	        fieldLabel: 'Fecha Factura',
    	        xtype: 'datefield',
    	        format: 'd/m/Y',
    	        editable: true,
    	        allowBlank:false,
    	        labelWidth : 170,
    	        value:new Date()
    	    }
            ,
            {
		    	id:'tipoServicioInterno'
		        ,xtype      : 'textfield'
		    	,fieldLabel : 'Tipo de Servicio'
	    		,labelWidth: 170
                ,width:500
                ,allowBlank:false
		    	,name       : 'tipoServicioInterno'
    		}
            ,
            	//proveedor2
           	{
		    	id:'proveedorInterno'
		        ,xtype      : 'textfield'
		    	,fieldLabel : 'Proveedor'
	    		,labelWidth: 170
                ,width:500
                ,allowBlank:false
		    	,name       : 'proveedorInterno'
    		}
            ,
            {
		    	id:'importeInterno'
		        ,xtype      : 'textfield'
		    	,fieldLabel : 'Importe'
	    		,labelWidth: 170
                ,width:500
                ,allowBlank:false
		    	,name       : 'importeInterno'
    		}
        ]
    });
    
    
    /*PANTALLA EMERGENTE PARA LA INSERCION Y MODIFICACION DE LOS DATOS DEL GRID*/
    ventanaGrid= Ext.create('Ext.window.Window', {
         renderTo: document.body,
           title: 'NUEVAS FACTURAS',
           height: 230,
           closeAction: 'hide',
           items:[panelModificacionInsercion],
           
           buttons:[{
                  text: 'Aceptar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                  handler: function() {
                        if (panelModificacionInsercion.form.isValid()) {
                        	
                        	var datos=panelModificacionInsercion.form.getValues();
                        	console.log(datos);
                        	var rec = new modelClau({
        	 				  	noFactura: datos.noFactInterno,
        					 	fechaFactura: datos.fechaFactInterno,
        					 	tipoServicio: datos.tipoServicioInterno,
        					 	proveedor: datos.proveedorInterno,
        					 	importe: datos.importeInterno            	
		        	 		});
                        	storeIncisos.add(rec);
                        	panelModificacionInsercion.getForm().reset();
                        } else {
                            Ext.Msg.show({
                                   title: 'Aviso',
                                   msg: 'Complete la informaci&oacute;n requerida',
                                   buttons: Ext.Msg.OK,
                                   icon: Ext.Msg.WARNING
                               });
                        }
                    }
              },
            {
                  text: 'Cancelar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                  handler: function() {
                       //Ext.getCmp('btnGuardaRegistro').disable();
                      panelModificacionInsercion.getForm().reset();
                      ventanaGrid.close();
                  }
            }
           ]
           });
    
    
    /*////////////////////////////////////////////////////////////////
    ////////////////   DECLARACION DE EDITOR DE INCISOS  ////////////
    ///////////////////////////////////////////////////////////////*/
    Ext.define('EditorIncisos', {
 		extend: 'Ext.grid.Panel',
	 	requires: [
		 	'Ext.selection.CellModel',
		 	'Ext.grid.*',
		 	'Ext.data.*',
		 	'Ext.util.*',
		 	'Ext.form.*'
	 	],
 		xtype: 'cell-editing',

 		title: 'Facturas en Tr&aacute;mite',
 		frame: false,

	 	initComponent: function(){
	 		this.cellEditing = new Ext.grid.plugin.CellEditing({
	 		clicksToEdit: 1
	 		});

	 			Ext.apply(this, {
	 			width: 750,
	 			height: 200,
	 			plugins: [this.cellEditing],
	 			store: storeIncisos,
	 			columns: 
	 			[
	 			 	{	
				 		header: 'No. de Factura',			dataIndex: 'noFactura',			flex:2
				 	},
				 	{
				 		header: 'Fecha de Factura',			dataIndex: 'fechaFactura',		flex:2//,			 	renderer: Ext.util.Format.dateRenderer('d M Y')
				 		
				 	},
				 	{
					 	header: 'Tipo de Servicio',			dataIndex: 'tipoServicio',	 	flex:2	
				 	},
				 	{
					 	header: 'Proveedor (Reembolso)',	dataIndex: 'proveedor',			flex:2
				 	},
				 	{
					 	header: 'Importe', 					dataIndex: 'importe',		 	flex:2,				renderer: Ext.util.Format.usMoney
				 	},
				 	{
					 	xtype: 'actioncolumn',
					 	width: 30,
					 	sortable: false,
					 	menuDisabled: true,
					 	items: [{
					 		icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
					 		tooltip: 'Quitar inciso',
					 		scope: this,
					 		handler: this.onRemoveClick
				 		}]
				 	}
		 		],
		 		selModel: {
			 		selType: 'cellmodel'
			 	},
		 		tbar: [{
				 	icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
				 	text: 'Agregar Factura',
				 	scope: this,
				 	handler: this.onAddClick
		 		}]
		 	});
 			this.callParent();
	 	},
	 	getColumnIndexes: function () {
		 	var me, columnIndexes;
		 	me = this;
		 	columnIndexes = [];
		 	Ext.Array.each(me.columns, function (column)
		 	{
		 		if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
		 			columnIndexes.push(column.dataIndex);
			 	} else {
			 		columnIndexes.push(undefined);
			 	}
		 	});
		 	return columnIndexes;
	 	},
	 	validateRow: function (columnIndexes,record, y)
	 	//hace que una celda de columna con allowblank=false tenga el estilo rojito
	 	{
		 	var view = this.getView();
		 	Ext.each(columnIndexes, function (columnIndex, x)
		 	{
		 		if(columnIndex)
			 	{
			 		var cell=view.getCellByPosition({row: y, column: x});
			 		cellValue=record.get(columnIndex);
				 	if(cell.addCls&&((!cellValue)||(cellValue.lenght==0))){
				 		cell.addCls("custom-x-form-invalid-field");
				 	}
			 	}
		 	});
		 	return false;
	 	},
	 	onAddClick: function(){	 		
	 		ventanaGrid.show();
	 		
	 		/*window.parent.scrollTo(0,600);
	 		// Create a model instance
	 		var rec = new modelClau({
	 				  	noFactura: '1233',
					 	fechaFactura: '12/12/2012',
					 	tipoServicio: 'CONSULTA',
					 	proveedor: 'MATRIZ',
					 	importe: '1245678.00'            	
	 		});
	 		this.getStore().add(rec);
		 	//para acomodarse en la primer celda para editar
		 	this.cellEditing.startEditByPosition({
		 		row: storeIncisos.getRange().length-1, 
		 		column: 0
		 	});*/
	 	},
	 	onRemoveClick: function(grid, rowIndex){
	 		var record=this.getStore().getAt(rowIndex);
	 		console.log(record);        	
	 		this.getStore().removeAt(rowIndex);
	 	}
 	});

 	gridIncisos=new EditorIncisos();
    mesConStoreUniAdmin=[];
    
    Ext.Ajax.request(
    {
    	url      : mesConUrlLoadTareas
    	,params  :
    	{
    		'smap1.pv_cdunieco_i'   : mesconInput['cdunieco']
             ,'smap1.pv_ntramite_i' : mesconInput['ntramite']
             ,'smap1.pv_cdramo_i'   : mesconInput['cdramo']
             ,'smap1.pv_nmpoliza_i' : mesconInput['nmpoliza']
             ,'smap1.pv_estado_i'   : mesconInput['estado']
             ,'smap1.pv_cdagente_i' : mesconInput['cdagente']
             ,'smap1.pv_status_i'   : mesconInput['status']
             ,'smap1.pv_cdtipsit_i' : mesconInput['cdtipsit']
             ,'smap1.pv_fedesde_i'  : mesconInput['fedesde']
             ,'smap1.pv_fehasta_i'  : mesconInput['fehasta']
    	}
    	,success : function(response)
    	{
    		var jsonResponse = Ext.decode(response.responseText);
    		//debug(jsonResponse);
    		mesConStoreTareas.setProxy(
            {
                type         : 'memory',
                enablePaging : true,
                reader       : 'json',
                data         : jsonResponse.slist1
            });
    		mesConStoreTareas.load();
    	}
        ,failure : function()
        {
        	var msg=Ext.Msg.show(
            {
                title   : 'Error',
                icon    : Ext.Msg.ERROR,
                msg     : 'Error de comunicaci&oacute;n',
                buttons : Ext.Msg.OK
            });
        }
    });
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    Ext.define('GridTareas',
    {
    	extend         : 'Ext.grid.Panel'
        ,title         : 'Tareas'
        //,width         : 600
        ,height        : 410
        ,buttonAlign   : 'center'
        ,store         : mesConStoreTareas
        ,bbar     :
        {
            displayInfo : true
            ,store      : mesConStoreTareas
            ,xtype       : 'pagingtoolbar'
        }
        ,initComponent : function()
        {
            //debug('initComponent');
            Ext.apply(this,
            {
                columns     :
                [
                    //No. Tramite Contra Recibo   Emisora     Oficina Documento   Estatus     Fecha de Recepcion  Tipo de Pago    Monto   Beneficiario / Proveedor    Asegurado Afectado  Usuario Acciones
				 	
                    {	 header     : 'No. Tr&aacute;mite'					,dataIndex : 'ntramite'			,width     : 90 		},
                    {	 header     : 'Contra recibo'						,dataIndex : 'ntramite'         ,width     : 90 		},
                    {	 header     : 'Emisora'                    			,dataIndex : "cdsucdoc"         ,width     : 90         },
                    {    header     : 'Oficina Documento'         			,dataIndex : "cdsubram"         ,width     : 120        },
                    {	 header     : 'Estatus'                     		,dataIndex : 'nmpoliza'         ,width     : 90         },
                    {	 header     : 'Fecha<br/>de Recepci&oacute;n'      	,dataIndex : 'ferecepc'         ,width     : 110      	,renderer  : Ext.util.Format.dateRenderer('d M Y')   },
                    {    header     : 'Tipo de Pago'                        ,dataIndex : 'nmpoliza'         ,width     : 100        },
                    {    header     : 'Monto'                        		,dataIndex : 'nmpoliza'         ,width     : 90         },
                    {  	 header     : 'Beneficiario /<br/>Proveedor'        ,dataIndex : 'nmpoliza'         ,width     : 100        },
                    {    header     : 'Asegurado <br/> Afectado'            ,dataIndex : 'nmpoliza'         ,width     : 100        },
                    {    header     : 'Usuario'					            ,dataIndex : 'nmpoliza'         ,width     : 100        },
                    {	 xtype      : 'actioncolumn'						,menuDisabled : true			,header       : 'Acciones'       ,width        : 200,
                    	 items      :
	                        [
	                            {
	                            	/*Revisión de Documentos*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/page.png'
		                            ,tooltip : 'Revisi&oacute;n de Documentos'
	                                ,handler : this.onRevisionDocumentoClick
	                            }
	                            ,{
	                            	/*Rechazar*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/cancel.png'
	                                ,tooltip : 'Rechazar'
	                                ,handler : this.onRechazarClick
	                            }
	                            ,{
	                            	/*Documentacion*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/page_attach.png'
	                            	,tooltip : 'Ver documentaci&oacute;n'
	                            	,handler : this.onFolderClick
	                            },{
	                            	/*Detalle Reclamacion*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/folder.png'
	                                ,tooltip : 'Detalle Reclamaci&oacute;n'
	                                ,handler : this.onDetalleReclamacionClick
	                            },{
	                            	/*Asigar a operador de reclamaciones*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/group_go.png'
	                                ,tooltip : 'Turnar &aacute;rea M&eacute;dica'
	                                ,handler : this.onTurnarReclamacionesClick
	                            },{
	                            	/*Asigar a operador de reclamaciones*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/heart.png'
	                                ,tooltip : 'Turnar &aacute;rea M&eacute;dica'
	                                ,handler : this.onTurnarReclamacionesClick
	                            },{
	                            	/*Asigar a operador de reclamaciones*/
	                            	icon     : '${ctx}/resources/fam3icons/icons/money_dollar.png'
	                                ,tooltip : 'Turnar &aacute;rea M&eacute;dica'
	                                ,handler : this.onTurnarReclamacionesClick
	                            }
	                            
	                            
	                        ]
                    }
                ]
                ,tbar: [{
                    icon    : '${ctx}/resources/fam3icons/icons/add.png',
                    text    : 'Alta de tr&aacute;mite',
                    scope   : this,
                    handler : this.onAddClick
                }]
            });
            this.callParent();
        }
        ,onRevisionDocumentoClick : function(grid,rowIndex)
        {
        	Ext.Msg.show({
            	title:'onRevisionDocumentoClick',
            	msg: 'ENTRA A onRevisionDocumentoClick',
            	buttons: Ext.Msg.OK,
            	icon: Ext.Msg.WARNING
        	});
        }
        ,onRechazarClick : function(grid,rowIndex)
        {
        	var record=grid.getStore().getAt(rowIndex);
        	modificacionClausula= Ext.create('Ext.window.Window',
        	        {
        	            title        : 'Rechazar Reclamaci&oacute;n'
        	            ,modal       : true
        	            ,buttonAlign : 'center'
        	            ,width		 : 650
        	            ,height      : 375
        	            ,items       :
        	            [
        				    Ext.create('Ext.form.Panel', {
        				        id: 'panelClausula',
        				        width: 650,
        				        //url: _URL_INSERTA_CLAUSU,
        				        bodyPadding: 5,
        				        renderTo: Ext.getBody(),
        				        items: [
        			                	motivoRechazo
        				    	        ,{
        				    	            xtype: 'textarea'
        			    	            	,fieldLabel: 'Descripci&oacute;n'
        		    	            		,labelWidth: 150
        		    	            		,width: 600
        		    	            		,name:'params.descripcion'
        	    	            			,height: 250
        	    	            			,allowBlank: false
        	    	            			,blankText:'La descripci&oacute;n es un dato requerido'
        				    	        }],
        			    	        buttonAlign:'center',
        			    	        buttons: [{
        				    		text: 'Guardar'
        				    		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
        				    		,buttonAlign : 'center',
        				    		handler: function() {
        				    	    	if (panelClausula.form.isValid()) {
        				    	    		panelClausula.form.submit({
        				    		        	waitMsg:'Procesando...',			        	
        				    		        	failure: function(form, action) {
        				    		        		Ext.Msg.show({
        				    	   	                    title: 'ERROR',
        				    	   	                    msg: action.result.errorMessage,
        				    	   	                    buttons: Ext.Msg.OK,
        				    	   	                    icon: Ext.Msg.ERROR
        				    	   	                });
        				    					},
        				    					success: function(form, action) {
        				    						Ext.Msg.show({
        				    	   	                    title: '&Eacute;XITO',
        				    	   	                    msg: "La cl&aacute;usula se guardo correctamente",
        				    	   	                    buttons: Ext.Msg.OK
        				    	   	                });
        				    						panelClausula.form.reset();
        				    						
        				    					}
        				    				});
        				    			} else {
        				    				Ext.Msg.show({
        				    	                   title: 'Aviso',
        				    	                   msg: 'Complete la informaci&oacute;n requerida',
        				    	                   buttons: Ext.Msg.OK,
        				    	                   icon: Ext.Msg.WARNING
        				    	               });
        				    			}
        				    		}
        				    	},{
        				    	    text: 'Cancelar',
        				    	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        				    	    buttonAlign : 'center',
        				    	    handler: function() {
        				    	        modificacionClausula.close();
        				    	    }
        				    	}
        				    	]
        				    })
        	        ]
        	        }).show();
        }
        ,onFolderClick : function(grid,rowIndex)
        {
            //debug(rowIndex);
            var record=grid.getStore().getAt(rowIndex);
            //debug(record);
            Ext.create('Ext.window.Window',
            {
            	title        : 'Documentaci&oacute;n'
            	,modal       : true
            	,buttonAlign : 'center'
            	,width       : 600
            	,height      : 400
            	,autoScroll  : true
            	,cls         : 'VENTANA_DOCUMENTOS_CLASS'
            	,loader      :
            	{
            		url       : mesConUrlDocu
            		,params   :
            		{
            			'smap1.nmpoliza'  : record.get('nmpoliza')&&record.get('nmpoliza').length>0?record.get('nmpoliza'):'0'
            			,'smap1.cdunieco' : record.get('cdunieco')
            			,'smap1.cdramo'   : record.get('cdramo')
            			,'smap1.estado'   : record.get('estado')
            			,'smap1.nmsuplem' : '0'
            			,'smap1.ntramite' : record.get('ntramite')
            			,'smap1.nmsolici' : record.get('nmsolici')&&record.get('nmsolici').length>0?record.get('nmsolici'):'0'
            		}
            		,scripts  : true
            		,autoLoad : true
            	}
            }).show();
        }
        ,onDetalleReclamacionClick  : function(grid,rowIndex)
        {
        	Ext.Msg.show({
            	title:'onDetalleReclamacionClick',
            	msg: 'ENTRA A onDetalleReclamacionClick',
            	buttons: Ext.Msg.OK,
            	icon: Ext.Msg.WARNING
        	});
        }
        ,onTurnarReclamacionesClick  : function(grid,rowIndex)
        {
        	Ext.Msg.show({
            	title:'onTurnarReclamacionesClick',
            	msg: 'ENTRA A onTurnarReclamacionesClick',
            	buttons: Ext.Msg.OK,
            	icon: Ext.Msg.WARNING
        	});
        	
        }
        ,onDocumentacionClick  : function(grid,rowIndex)
        {
        	Ext.Msg.show({
            	title:'onDocumentacionClick',
            	msg: 'ENTRA A onDocumentacionClick',
            	buttons: Ext.Msg.OK,
            	icon: Ext.Msg.WARNING
        	});
        }
        ,onAddClick : function(button)
        {

        	var grid=button.up().up();
        	//debug(grid);
        	var menu2= Ext.create('Ext.window.Window',
        	{
        		title        : 'Nuevo tr&aacute;mite'
        		,width       : 800
        		//,maxHeight   : 600
        		,modal       : true
        		,items       :
        		[
        		 	Ext.create('Ext.form.Panel',
        		    {
						id          : 'mesConFormTraManual',
        		    	buttonAlign : 'center'
        		    	//,url        : mesConUrlSaveTra
        		    	,border     : 0
        		    	,defaults   :
        		        {
        		        	style : 'margin:5px;'
        		        }
        		    	,items      :
        		    	[
        		    	 	{
		            			id:'txtContraRecibo'
				                ,xtype      : 'textfield'
				            	,fieldLabel : 'Contra recibo'
				            	,readOnly   : true
				            	,labelWidth : 250
				            	,name       : 'params.contraRecibo'
				            },
				            {
				                id: 'txtEstado',
				                name: 'params.estado',
				                xtype: 'textfield',
				                fieldLabel: 'Estado',
				               labelWidth: 250
				            },				            
				            	oficinaEmisora
			            	,
		                    	oficinaDocumento
		                	,
		                	{
				            	id:'params.fechaRecepcion'
				                ,xtype      : 'textfield'
				            	,fieldLabel : 'Fecha recepci&oacute;n'
				            	,readOnly   : true
				            	,labelWidth : 250
				            	,name       : 'params.fechaRecepcion'
				            },
		            			tipoPago
			            	,
			            		aseguradoAfectado
			            	,
			            		beneficiario
			            	,
			            	    proveedor
		            	    ,
		                	{
				            	id:'txtNoFactura'
				                ,xtype      : 'textfield'
				            	,fieldLabel : '*(PD) No. Factura'
				            	,readOnly   : true
				            	,labelWidth : 250
				            	,name       : 'params.noFactura'
				            }
				            ,
		                	{
				            	id:'txtImporte'
				                ,xtype      : 'textfield'
				            	,fieldLabel : '*(PD) Importe'
				            	,readOnly   : true
				            	,labelWidth : 250
				            	,name       : 'params.importe'
				            },
				            {
		                        id: 'fechaFactura',
		                        name: 'params.fechaFactura',
		                        fieldLabel: '*(PD) Fecha Factura',
		                        xtype: 'datefield',
		                        format: 'd/m/Y',
		                        editable: true,
		                        allowBlank:false,
		                        labelWidth : 250,
		                        value:new Date()
		                    },
		                    	gridIncisos
        		    	],
            	        buttonAlign:'center',
            	        buttons: [
           	                  {
	            	            id:'botonCotizar',
	            	            icon:_CONTEXT+'/resources/fam3icons/icons/calculator.png',
	            	            //text: hayTramiteCargado?'Precaptura':'Cotizar',
	            	            text: 'Generar Tramite',
	                    		handler: function() {
	                    			
	                    			var form = this.up('form').getForm();
	                    			if (form.isValid())
	            	                {
	                    				Ext.Msg.show({
	            	                    	title:'Exito',
	            	                    	msg: 'Se contemplaron todo',
	            	                    	buttons: Ext.Msg.OK,
	            	                    	icon: Ext.Msg.WARNING
	            	                	});
	            	                }
	                    			else
	                				{
	                    				var incisosRecords = storeIncisos.getRange();
	                    				console.log(incisosRecords.length);
	                    				
	                    				var incisosJson = [];
	                    				storeIncisos.each(function(record,index){
	        	                        	if(record.get('nombre')
	        	                        			&&record.get('nombre').length>0)
	                                		{
	        	                        		nombres++;
	                                		}
	        	                            incisosJson.push({
	        	                            	noFactura: record.get('noFactura'),
	        	                            	fechaFactura: record.get('fechaFactura'),
	        	                            	tipoServicio: record.get('tipoServicio'),
	        	                            	proveedor: record.get('proveedor'),
	        	                            	importe: record.get('importe')
	        	                            });
	                                    });
	                    				
	                    				console.log('---- VALOR DE IncisosJson ---- ');
	                    				console.log(incisosJson);
	                    				
	                    				var submitValues=form.getValues();
	                                	submitValues['incisos']=incisosJson;
	                                	console.log('---- VALOR DE submitValues ---- ');
	                    				console.log(submitValues);
	                    				
	                    				Ext.Msg.show({
	            	                    	title:'Datos incompletos',
	            	                    	msg: 'Favor de introducir todos los campos requeridos',
	            	                    	buttons: Ext.Msg.OK,
	            	                    	icon: Ext.Msg.WARNING
	            	                	});
	                				}
	            	            }
	            	        },
	            	        {
	            	        	text: 'Cancelar',
	                            icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
	                            handler: function() {
	                            	menu2.close();
	                            }
	            	        }
                		]
       		    	})
        		]
        	}).show();
        }
    });
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    mesConGridTareas = new GridTareas();
    //mesConGridTareas.render('mesconprimain');
    Ext.create('Ext.panel.Panel',{
   	defaults  :
   	{
   		style : 'margin : 5px;'
   	}
       ,renderTo : 'mesconprimain'
       ,border   : 0
       ,items    : 
       [
	        /*Ext.create('Ext.form.Panel',
	        {
	            title      : 'Filtro'
	            ,frame     : true
	            ,url       : '' // falta URL para ver en que proceso va a ir 
	            ,collapsible : true
	            ,titleCollapse : true
	            ,layout    :
	            {
	                type     : 'table'
	                ,columns : 3
	            }
	            ,defaults    : 
	            {
	                style       : 'margin : 5px;'
                    ,labelWidth : 80
	            }
	            ,buttonAlign : 'center'
	            ,buttons     :
	            [
	                {
	                    text     : 'Limpiar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
	                    ,handler : function()
	                    {
	                    	var recordLimpio={};
	                    	this.up().up().getForm().getFields().each(function(item)
	                    	{
	                    		item.setValue('');
	                    	});
	                    }
	                }
	                ,{
	                    text     : 'Filtrar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
	                    ,handler : function()
	                    {
	                        debug('buscar');
	                        var form =this.up().up();
	                        if(form.isValid())
	                        {
	                            form.submit(
	                            {
	                            	standardSubmit : true
	                            });
	                        }
	                        else
	                        {
	                            Ext.Msg.show(
	                            {
	                                title   : 'Datos imcompletos',
	                                icon    : Ext.Msg.WARNING,
	                                msg     : 'Favor de llenar los campos requeridos',
	                                buttons : Ext.Msg.OK
	                            });
	                        }
	                    }
	                }
	            ]
	            ,items       :
	            [
	                {
	                    xtype           : 'combo'
	                    ,id             : 'marmesconFilUnieco'
	                    ,fieldLabel     : 'Sucursal'
	                    ,name           : 'smap1.pv_cdunieco_i'
	                    ,displayField   : 'value'
	                    ,valueField     : 'key'
	                    ,queryMode      :'local'
	                    ,value          : mesconInput['cdunieco']
	                    ,store          : Ext.create('Ext.data.Store',
	                    {
	                        model     : 'Generic'
	                        ,autoLoad : true
	                        ,proxy    :
	                        {
	                            type         : 'ajax'
	                            ,url         : marmesconurlcata
	                            ,extraParams : {catalogo:'<s:property value="CON_CAT_MESACONTROL_SUCUR_DOCU" />'}
	                            ,reader      :
	                            {
	                                type  : 'json'
	                                ,root : 'lista'
	                            }
	                        }
	                    })
	                    ,listeners      :
	                    {
	                        'change' : function()
	                        {
	                        	debug('change');
	                            Ext.getCmp('marmesconFilRamo').getStore().load(
	                            {
	                                params : {'map1.cdunieco':this.getValue()}
	                            });
	                        }
	                        ,'afterrender' : function()
	                        {
	                        	Ext.getCmp('marmesconFilRamo').getStore().load(
                                {
                                    params : {'map1.cdunieco':this.getValue()}
                                });
	                        }
	                    }
	                }
	                ,{
	                    xtype           : 'combo'
	                    ,id             : 'marmesconFilRamo'
	                    ,fieldLabel     : 'Producto'
	                    ,name           : 'smap1.pv_cdramo_i'
	                    ,valueField     : 'cdramo'
	                    ,displayField   : 'dsramo'
	                    ,forceSelection : false
	                    ,value          : mesconInput['cdramo']
	                    ,queryMode      :'local'
	                    ,store          : Ext.create('Ext.data.Store',
	                    {
	                        model     : 'Ramo'
	                        ,autoLoad : false
	                        ,proxy    :
	                        {
	                            type    : 'ajax'
	                            ,url    : marmesconurlramos
	                            ,reader :
	                            {
	                                type  : 'json'
	                                ,root : 'slist1'
	                            }
	                        }
	                    })
	                    ,listeners      :
	                    {
	                        'change' : function()
	                        {
	                            Ext.getCmp('marmesconFilTipsit').getStore().load(
	                            {
	                                params : {'map1.cdramo':this.getValue()}
	                            });
	                        }
	                        ,'afterrender' : function()
	                        {
	                        	Ext.getCmp('marmesconFilTipsit').getStore().load(
                                {
                                    params : {'map1.cdramo':this.getValue()}
                                });
	                        }
	                    }
	                }
	                ,{
	                    fieldLabel  : 'Modalidad'
	                    ,xtype      : 'combo'
	                    ,id         : 'marmesconFilTipsit'
	                    ,name       : 'smap1.pv_cdtipsit_i'
	                    ,value      : mesconInput['cdtipsit']
	                    ,valueField   : 'CDTIPSIT'
	                    ,displayField : 'DSTIPSIT'
	                    ,forceSelection : false
	                    ,queryMode      :'local'
	                    ,store : Ext.create('Ext.data.Store', {
	                        model:'Tipsit',
	                        autoLoad : false,
	                        proxy:
	                        {
	                            type: 'ajax',
	                            url : marmesconUrlLoadTipsit,
	                            reader:
	                            {
	                                type: 'json',
	                                root: 'slist1'
	                            }
	                        }
	                    })
	                }
	                ,{
	                    xtype           : 'combo'
	                    ,id             : 'marmesconFilEstado'
	                    ,fieldLabel     : 'Estado'
	                    ,name           : 'smap1.pv_estado_i'
	                    ,value          : mesconInput['estado']
	                    ,displayField   : 'value'
	                    ,valueField     : 'key'
	                    ,forceSelection : false
	                    ,queryMode      :'local'
	                    ,store          : Ext.create('Ext.data.Store',
	                    {
	                        model     : 'Generic'
	                        ,autoLoad : true
	                        ,proxy    :
	                        {
	                            type         : 'ajax'
	                            ,url         : marmesconurlcata
	                            ,extraParams : {catalogo:'<s:property value="CON_CAT_POL_ESTADO" />'}
	                            ,reader      :
	                            {
	                                type  : 'json'
	                                ,root : 'lista'
	                            }
	                        }
	                    })
	                }
	                ,{
	                    xtype       : 'textfield'
	                    ,fieldLabel : 'P&oacute;liza'
	                    ,name       : 'smap1.pv_nmpoliza_i'
	                    ,value      : mesconInput['nmpoliza']
	                }
	                ,{
	                    fieldLabel : 'Agente'
	                    ,xtype     : 'combo'
	                    ,name      : 'smap1.pv_cdagente_i'
	                    ,value     : mesconInput['cdagente']
	                    ,displayField : 'value'
	                    ,valueField   : 'key'
	                    ,forceSelection : false
	                    ,matchFieldWidth: false
	                    ,hideTrigger : true
	                    ,minChars  : 3
	                    ,queryMode :'remote'
	                    ,queryParam: 'smap1.pv_cdagente_i'
	                    ,store : Ext.create('Ext.data.Store', {
	                        model:'Generic',
	                        autoLoad:false,
	                        proxy: {
	                            type: 'ajax',
	                            url : marmesconurlAgentes,
	                            reader: {
	                                type: 'json',
	                                root: 'lista'
	                            }
	                        }
	                    })
	                }
	                ,{
	                    xtype       : 'numberfield'
	                    ,fieldLabel : 'Tr&aacute;mite'
	                    ,name       : 'smap1.pv_ntramite_i'
	                    ,value      : mesconInput['ntramite']
	                }
	                ,{
	                    fieldLabel      : 'Estatus'
	                    ,xtype          : 'combo'
	                    ,name           : 'smap1.pv_status_i'
	                    ,value          : mesconInput['status']
	                    ,displayField   : 'value'
	                    ,valueField     : 'key'
	                    ,forceSelection : false
	                    ,queryMode      :'local'
	                    ,store          : Ext.create('Ext.data.Store',
	                    {
	                        model     : 'Generic'
	                        ,autoLoad : true
	                        ,proxy    :
	                        {
	                            type         : 'ajax'
	                            ,url         : marmesconurlcata
	                            ,extraParams : {catalogo:'<s:property value="CON_CAT_MESACONTROL_ESTAT_TRAMI" />'}
	                            ,reader      :
	                            {
	                                type  : 'json'
	                                ,root : 'lista'
	                            }
	                        }
	                    })
	                }
	                ,{
	                	xtype : 'label'
	                }
	                ,{
	                	xtype       : 'datefield'
	                	,fieldLabel : 'Desde'
	                	,format     : 'd/m/Y'
	                	,name       : 'smap1.pv_fedesde_i'
	                	,value      : mesconInput['fedesde']
	                }
	                ,{
                        xtype       : 'datefield'
                        ,fieldLabel : 'Hasta'
                        ,format     : 'd/m/Y'
                        ,name       : 'smap1.pv_fehasta_i'
                        ,value      : mesconInput['fehasta']
                    }
	            ]
	        })*/
            ,mesConGridTareas
    ]
    });    
});

</script>
</head>
<body>
<div id="mesconprimain" style="height:600px;"></div>
</body>
</html>