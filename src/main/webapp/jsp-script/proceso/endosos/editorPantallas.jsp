<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
///////////////////////
////// variables //////
/*///////////////////*/
extjs_custom_override_mayusculas = false;

var edipanFiltro;
var edipanGrid;
var edipanEditor;
var edipanStore;
var edipanIndexEditado;
var edipanStoreArbol;
var edipanPanelArbol;
var edipanBotonAceptar;

var edipanUrlLoad = '<s:url namespace="/endosos" action="obtenerParametrosPantalla" />';
var edipanUrlSave = '<s:url namespace="/endosos" action="guardarParametrosPantalla" />';
var edipanUrlView = '<s:url namespace="/endosos" action="visorPantallas"            />';
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('EdipanModelo',
	{
		extend  : 'Ext.data.Model'
		,fields :
		[
			<s:property value="imap1.fieldsModelo" />
		]
	});
	
	Ext.define('EdipanModeloFiltro',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            <s:property value="imap1.fieldsFiltro" />
        ]
    });
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	edipanStoreArbol = Ext.create('Ext.data.TreeStore', { <s:property value="imap1.storeArbol" /> });
	
	edipanStore=Ext.create('Ext.data.Store',
	{
		model     : 'EdipanModelo'
		,autoLoad : false
		,proxy    :
        {
            type    : 'ajax'
            ,url    : edipanUrlLoad
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
	});
	/*////////////////*/
	////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	/*/////////////////////*/
	Ext.define('EdipanPanelArbol',
	{
		extend         : 'Ext.tree.Panel'
		,initComponent : function()
		{
			debug('EdipanPanelArbol initComponent');
			Ext.apply(this,
			{
				title        : 'Pantallas'
				,region      : 'west'
		        ,width       : 250
		        ,height      : 500
		        ,store       : edipanStoreArbol
		        ,rootVisible : false
		        ,listeners   :
		        {
		        	itemclick : function( panel, record, item, index, e, eOpts )
		        	{
		        		debug('record',record);
		        		var isLeaf = record.get('leaf');
		        		var text   = record.get('text');
		        		debug('isLeaf',isLeaf,'text',text);
		        		if(isLeaf)
		        		{
		        			edipanFiltro.loadRecord(
	                            Ext.create('EdipanModeloFiltro',
	                            {
	                            	'smap1.pantalla' : record.parentNode.get('text') 
	                                ,'smap1.seccion' : text
	                            })
	                        );
		        		}
		        		else
		        		{
		        			edipanFiltro.loadRecord(
			        			Ext.create('EdipanModeloFiltro',
			        			{
			        				'smap1.pantalla' : text
			        			})
		        			);
		        		}
		        	}
		        }
			});
			this.callParent();
		}
	});
	
	Ext.define('EdipanFiltro',
	{
		extend         : 'Ext.form.Panel'
		,initComponent : function()
		{
			debug('Instancia de EdipanFiltro')
			Ext.apply(this,
			{
				title        : 'Filtro'
				,model       : 'EdipanModeloFiltro'
				,collapsible : true
				,layout      :
				{
					type     : 'table'
					,columns : 3
				}
				,items       :
				[
				    <s:property value="imap1.itemsFiltro" />
				]
				,buttonAlign : 'center'
				,buttons     :
				[
				    {
				    	text     : 'Buscar'
				    	,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
				    	,handler : function()
				    	{
				    		var form=this.up().up();
				    		if(form.isValid())
				    		{
				    			debug(form.getValues());
				    			
				    			edipanStore.load(
				    			{
				    				params : form.getValues()
				    			});
				    			
				    			edipanFiltro.setDisabled(true);
				    			edipanPanelArbol.setDisabled(true);
				    			edipanGrid.setDisabled(false);
				    			edipanEditor.setDisabled(true);
				    			
				    			edipanFiltro.collapse();
				    			edipanGrid.expand();
                                edipanEditor.collapse();
				    		}
				    	}
				    }
				]
			});
			this.callParent();
		}
	});
	
	Ext.define('EdipanGrid',
	{
		extend         : 'Ext.grid.Panel'
		,disabled      : true
		,initComponent : function()
		{
			debug('Instancia de EdipanGrid');
			Ext.apply(this,
			{
				title        : 'Campos'
				,store       : edipanStore
                ,collapsible : true
				,height      : 400
				,columns     :
				[
                    <s:property value="imap1.columnsModelo" />
				]
			    ,buttonAlign : 'center'
			    ,buttons     :
			    [
					{
					    text     : 'Guardar'
					    ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
					    ,handler : function()
					    {
					        
					    	var json={};
					    	
					        edipanFiltro.setDisabled(false);//para poder usar los valores (*)
					        var values=edipanFiltro.getValues();
					        debug('valores del filtro:',values);
					    	edipanFiltro.setDisabled(true);//regresarlo a como estaba (*)
					    	
					        var smap1={};
					        smap1['cdunieco'] = values['smap1.cdunieco'];
					        smap1['cdramo']   = values['smap1.cdramo'];
					        smap1['cdtipsit'] = values['smap1.cdtipsit'];
					        smap1['estado']   = values['smap1.estado'];
					        smap1['pantalla'] = values['smap1.pantalla'];
					        smap1['cdsisrol'] = values['smap1.cdsisrol'];
					        smap1['cdtiptra'] = values['smap1.cdtiptra'];
					        smap1['orden']    = values['smap1.orden'];
					        smap1['seccion']  = values['smap1.seccion'];
					        smap1['rol']      = values['smap1.rol'];
					        json['smap1']     = smap1;
					    	
					    	var slist1=[];
					    	edipanStore.each(function(record)
					    	{
					    		var esFinal = record.get('SWFINAL');
					    		esFinal = esFinal && ( esFinal=='s' || esFinal=='S' );
					    		if(!esFinal)
					    		{
					    			slist1.push(record.getData());					    			
					    		}
					    	});
					    	json['slist1']=slist1;
					    	
					    	debug(json);
					    	
					    	edipanGrid.setLoading(true);
					    	Ext.Ajax.request(
					    	{
					    		url       : edipanUrlSave
					    		,jsonData : json
					    		,success  : function(response)
					    		{
					    			edipanGrid.setLoading(false);
					    			var jsonres=Ext.decode(response.responseText);
					    			if(jsonres.success==true)
					    			{
						    			edipanFiltro.setDisabled(false);
						    			edipanPanelArbol.setDisabled(false);
			                            edipanGrid.setDisabled(true);
			                            edipanEditor.setDisabled(true);
			                            
			                            edipanFiltro.expand();
			                            edipanGrid.collapse();
			                            edipanEditor.collapse();
					    			}
					    			else
					    			{
					    				Ext.Msg.show(
	                                    {
	                                        title    : 'Error'
	                                        ,msg     : 'Error al guardar'
	                                        ,buttons : Ext.Msg.OK
	                                        ,icon    : Ext.Msg.ERROR
	                                    });
					    			}
					    		}
					    	    ,failure  : function()
					    	    {
					    	    	edipanGrid.setLoading(false);
					    	    	
					    	    	Ext.Msg.show(
					    	    	{
	                                    title    : 'Error'
	                                    ,msg     : 'Error de comunicaci&oacute;n'
	                                    ,buttons : Ext.Msg.OK
	                                    ,icon    : Ext.Msg.ERROR
	                                });
					    	    }
					    	});
					    }
					}
					,{
						text     : 'Vista previa'
						,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
						,handler : function()
						{
							edipanFiltro.setDisabled(false);
							var params=edipanFiltro.getValues();
							edipanFiltro.setDisabled(true);
							
							Ext.create('Ext.form.Panel').submit(
							{
								standardSubmit : true
								,url           : edipanUrlView
								,params        : params 
								,target        : '_blank'
							});
						}
					}
			        ,{
			        	text     : 'Cancelar'
			        	,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
			        	,handler : function()
			        	{
			        		edipanFiltro.setDisabled(false);
			        		edipanPanelArbol.setDisabled(false);
                            edipanGrid.setDisabled(true);
                            edipanEditor.setDisabled(true);
                            
                            edipanFiltro.expand();
                            edipanGrid.collapse();
                            edipanEditor.collapse();
			        	}
			        }
			    ]
			    ,listeners   :
			    {
			    	itemclick : function(grid,record,item,index)
			    	{
			    		edipanIndexEditado=index;
			    		debug('edipanIndexEditado',edipanIndexEditado);
			    		
			    		var esFinal = record.get('SWFINAL');
			    		esFinal = esFinal && (esFinal=='S'||esFinal=='s');
			    		
			    		edipanEditor.loadRecord(record);
			    		edipanBotonAceptar.setDisabled(esFinal);
			    		
			    		if(esFinal)
			    		{
			    			edipanEditor.items.items[0].setValue('');
			    			edipanEditor.items.items[1].setValue('');
			    		}
			    		
			    		edipanFiltro.setDisabled(true);
			    		edipanPanelArbol.setDisabled(true);
                        edipanGrid.setDisabled(true);
                        edipanEditor.setDisabled(false);
                        
                        edipanFiltro.collapse();
                        edipanGrid.collapse();
                        edipanEditor.expand();
			    	}
			    }
			    ,viewConfig :
                {
                    listeners :
                    {
                        refresh : function(dataview)
                        {
                            Ext.each(dataview.panel.columns, function(column)
                            {
                                column.autoSize();
                            });
                        }
                    }
                }
			});
			this.callParent();
		}
	});
	
	Ext.define('EdipanEditor',
	{
		extend         : 'Ext.form.Panel'
		,initComponent : function()
		{
			debug('Instancia de EdipanEditor');
			Ext.apply(this,
			{
				title        : 'Editor'
	            ,collapsible : true
	            ,disabled    : true
	            ,layout      :
	            {
	                type     : 'table'
	                ,columns : 3
	            }
		        ,items       :
		        [
		            <s:property value="imap1.itemsModelo" />
		        ]
		        ,buttonAlign : 'center'
                ,buttons     :
                [
                    edipanBotonAceptar
                    ,{
                        text     : 'Duplicar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                        ,handler : function()
                        {
                            debug(this.up().up().getValues());
                            
                            if(this.up().up().isValid())
                            {
	                            edipanStore.add(this.up().up().getValues());
	                            
	                            edipanFiltro.setDisabled(true);
	                            edipanPanelArbol.setDisabled(true);
	                            edipanGrid.setDisabled(false);
	                            edipanEditor.setDisabled(true);
	                            
	                            edipanFiltro.collapse();
	                            edipanGrid.expand();
	                            edipanEditor.collapse();
                            }
                        }
                    }
                    ,{
                        text     : 'Borrar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,handler : function()
                        {
                            debug(this.up().up().getValues());
                            
                            edipanStore.removeAt(edipanIndexEditado);
                            
                            edipanFiltro.setDisabled(true);
                            edipanPanelArbol.setDisabled(true);
                            edipanGrid.setDisabled(false);
                            edipanEditor.setDisabled(true);
                            
                            edipanFiltro.collapse();
                            edipanGrid.expand();
                            edipanEditor.collapse();
                        }
                    }
                    ,{
                        text     : 'Cancelar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                        ,handler : function()
                        {
                            edipanFiltro.setDisabled(true);
                            edipanPanelArbol.setDisabled(true);
                            edipanGrid.setDisabled(false);
                            edipanEditor.setDisabled(true);
                            
                            edipanFiltro.collapse();
                            edipanGrid.expand();
                            edipanEditor.collapse();
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
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
	edipanBotonAceptar = Ext.create('Ext.button.Button',
	{
        text     : 'Actualizar'
        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
        ,handler : function()
        {
        	var form = this.up('form');
            debug(form.getValues());
            
            if(form.isValid()) {
            	
                Ext.MessageBox.confirm('Confirmar', '¿Est&aacute; seguro que desea actualizar este campo?', function(btn){
                	if(btn === 'yes'){
                		edipanStore.removeAt(edipanIndexEditado);
                        edipanStore.insert(edipanIndexEditado, form.getValues());
                        
                        edipanFiltro.setDisabled(true);
                        edipanPanelArbol.setDisabled(true);
                        edipanGrid.setDisabled(false);
                        edipanEditor.setDisabled(true);
                        
                        edipanFiltro.collapse();
                        edipanGrid.expand();
                        edipanEditor.collapse();
                	}
                });
            }
        }
    });
	
	edipanPanelArbol = new EdipanPanelArbol();
	edipanFiltro     = new EdipanFiltro();
	edipanGrid       = new EdipanGrid();
	edipanEditor     = new EdipanEditor();
	
	Ext.create('Ext.panel.Panel',
	{
		renderTo  : 'edipanDivPri'
		,border   : 0
		,defaults :
		{
			style       : 'margin : 5px;'
		}
	    ,layout   : 'border'
	    ,height   : 900
		,items    :
		[
		    edipanPanelArbol
		    ,Ext.create('Ext.panel.Panel',
		    {
		    	region    : 'center'
		    	,defaults :
		    	{
		    		style : 'margin : 5px;'
		    	}
		    	,items    : 
		    	[
					edipanFiltro
					,edipanGrid
					,edipanEditor
		    	]
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
</script>
</head>
<body>
<div id="edipanDivPri"></div>
</body>
</html>