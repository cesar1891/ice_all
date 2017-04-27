Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {

    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });

    Ext.define('modelClau',
    {
        extend:'Ext.data.Model',
        fields:['key','value']
    });
    
    var storeClavesClausulas = new Ext.data.Store(
    {
        model      : 'modelClau'
        ,autoLoad  : true
        ,proxy     :
        {
            url     : _URL_CARGA_CLAVES_CLAU
            ,type   : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'listaGenerica'
            }
        }
    });
    
    Ext.define('modeloGridClau',{
        extend:'Ext.data.Model',
        fields:['key','value']
    });
    
    var storGridClau = new Ext.data.Store(
    {
    	pageSize : 10
        ,model      : 'modeloGridClau'
        ,autoLoad  : false
        ,proxy     :
        {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
    });
    
    Ext.create('Ext.panel.Panel',
    	    {
    	        border    : 0
    	        ,renderTo : 'div_clau'
    	        ,items    :
    	        [
    	            Ext.create('Ext.panel.Panel',
    	            {
    	                border  : 0
    	                ,title: 'Busqueda de Cl&aacute;usulas'
    	                ,style         : 'margin:5px'
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
    	                        id              : 'idComboClau'
    	                        ,store          : storeClavesClausulas
    	                        ,displayField   : 'key'
    	                        ,valueField     : 'key'
    	                        ,labelWidth: 150
    	                        ,editable       : true
    	                        ,forceSelection : false
    	                        ,style          : 'margin:5px'
    	                        ,fieldLabel     : 'Clave de la cl&aacute;usula'
    	                        ,width          :  250
    	                    })
    	                    ,{
    	                        xtype : 'textfield'
    	                        ,labelWidth: 150
    	                        ,fieldLabel : 'Filtro Descripci&oacute;n'
    	                        ,id         : 'idFiltroDes'
    	                    }
    	                ]
    	                ,buttonAlign: 'center'
    	                ,buttons : [{
    	                	text: "Buscar"
	                		,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
    	                	,handler: function(){

    	                		storGridClau.removeAll();
    	                		
    	                		var params = {
	                				'params.cdclausu' : Ext.getCmp('idComboClau').getValue(),
	                				'params.dsclausu' : Ext.getCmp('idFiltroDes').getValue()
	                			};
    	                		cargaStorePaginadoLocal(storGridClau, _URL_CONSULTA_CLAUSU, 'listaGenerica', params, function(options, success, response){
    	                			if(success){
										var jsonResponse = Ext.decode(response.responseText);
										if(jsonResponse.listaGenerica && jsonResponse.listaGenerica.length == 0) {
											showMessage("Aviso", "No se encontraron datos.", Ext.Msg.OK, Ext.Msg.INFO);
											return;
										}
									}else{
										showMessage('Error', 'Error al obtener los datos', 
											Ext.Msg.OK, Ext.Msg.ERROR);
									}
    	                		});
    	                	}	
    	                },{
    	                	text: "Limpiar"
    	                		,icon:_CONTEXT+'/resources/fam3icons/icons/arrow_refresh.png'
        	                	,handler: function(){
        	                			Ext.getCmp('idComboClau').reset();
        	                			Ext.getCmp('idFiltroDes').reset();
        	                	}	
        	             }] 
    	            })
    	            ,Ext.create('Ext.grid.Panel',
    	            {
    	                id             : 'clausulasGridId'
    	                ,title         : 'Cl&aacute;usulas'
    	                ,store         :  storGridClau
    	                ,collapsible   : true
    	                ,titleCollapse : true
    	                ,style         : 'margin:5px'
    	                ,height        : 400
    	                ,columns       :
    	                [
    	                    {
    	                        header     : 'Clave'
    	                        ,dataIndex : 'key'
    	                        ,flex      : 1
    	                    }
    	                    ,
    	                    {
    	                        header     : 'Descripcion de la Cl&aacute;usula'
        	                    ,dataIndex : 'value'
        	                    ,flex      : 1
        	                }
    	                ],
    	                bbar     :
    	                {
    	                    displayInfo : true,
    	                    store       : storGridClau,
    	                    xtype       : 'pagingtoolbar'
    	                }
    	            	,buttonAlign: 'center'
		                ,buttons : [{
		                	text: "Agregar"
		                	,icon:_CONTEXT+'/resources/fam3icons/icons/application_add.png'
		                	,handler: function(){
		                		edicionActualizacionClausulas(null,null,0);
		                	}	
		                },{
		                	text: "Editar"
		                		,icon:_CONTEXT+'/resources/fam3icons/icons/application_edit.png'
		                		,handler: function(){
	    	                		if(Ext.getCmp('clausulasGridId').getSelectionModel().hasSelection()){
	    	                			var rowSelected = Ext.getCmp('clausulasGridId').getSelectionModel().getSelection()[0];
	    	                			edicionActualizacionClausulas(rowSelected.get('key'),rowSelected.get('value'),1);
	    	                		}else {
	    	                			Ext.Msg.alert('Aviso', 'Debe de seleccionar una cl&aacute;usula para realizar la edici&oacute;n');
	    	                		}
	    	                	}	
	    	             }]
    	            })
    	        ]
    	    });
    
    /*Funcion para la actualizacion y modificación de cláusulas*/
    function edicionActualizacionClausulas(idClausula,descripcion,bandera)
    {
    	//AgregarNuevo --> 0
    	//Editar  	   --> 1
    	if(bandera==0)
		{
        	modificacionClausula = Ext.create('Ext.window.Window',
        	        {
        	            title        : 'CL&Aacute;USULA'
        	            ,modal       : true
        	            ,buttonAlign : 'center'
        	            ,width		 : 810
        	            ,height      : 410
        	            ,items       :
        	            [
        				    panelClausula= Ext.create('Ext.form.Panel', {
        				        id: 'panelClausula',
        				        width: 800,
        				        url: _URL_INSERTA_CLAUSU,
        				        bodyPadding: 5,
        				        renderTo: Ext.getBody(),
        				        items: [
        				               {
        				                	 xtype:'textfield'
        			                		,name:'params.descripcion'
        		                			,fieldLabel: 'Nombre de la Cl&aacute;usula'
        	                				,labelWidth: 150
        	                				,width: 750
        	                				//,value: descripcion
        	                				,allowBlank: false
        	                				,blankText:'El nombre de la cl&aacute;usula es un dato requerido'
        				            	}
        				    	        ,{
        				    	            xtype: 'textarea'
        			    	            	,fieldLabel: 'Descripci&oacute;n'
        		    	            		,labelWidth: 150
        		    	            		,width: 750
        		    	            		,name:'params.contenido'
        	    	            			,height: 250
        	    	            			,allowBlank: false
        	    	            			,blankText:'La descripci&oacute;n es un dato requerido'
        				    	        }],
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
        	        });
        	    	modificacionClausula.show();
		}
    	else
		{
    		var txtContenido="";
    		Ext.Ajax.request(
    				{
    				    url     : _URL_CONSULTA_CLAUSU_DETALLE
    				    ,params : 
    				    {
    				        'params.cdclausu'  : idClausula
    				    }
    				    ,success : function (response)
    				    {
    				    	var json=Ext.decode(response.responseText);
    				    	txtContenido =json.msgResult;
    				    	modificacionClausula = Ext.create('Ext.window.Window',
    			    		        {
    			    		            title        : 'CL&Aacute;USULA'
    			    		            ,modal       : true
    			    		            ,buttonAlign : 'center'
    			    		            ,width		 : 810
    			    		            ,height      : 410
    			    		            ,items       :
    			    		            [
    			    					    panelClausula= Ext.create('Ext.form.Panel', {
    			    					        id: 'panelClausula',
    			    					        width: 800,
    			    					        url: _URL_ACTUALIZA_CLAUSU,
    			    					        bodyPadding: 5,
    			    					        renderTo: Ext.getBody(),
    			    					        items: [
    			    					                {
    			    					                	 xtype:'hiddenfield'
    			    				                		,name:'params.cdclausu'
    			    			                			,fieldLabel: 'Id Clave'
    			    		                				,labelWidth: 150
    			    		                				,width: 750
    			    		                				,value: idClausula
    			    					            	}
    			    					            	,{
    			    					                	 xtype:'textfield'
    			    				                		,name:'params.descripcion'
    			    			                			,fieldLabel: 'Nombre de la Cl&aacute;usula'
    			    		                				,labelWidth: 150
    			    		                				,width: 750
    			    		                				,value: descripcion
    			    		                				,allowBlank: false
    			    		                				,blankText:'El nombre de la cl&aacute;usula es un dato requerido'
    			    					            	}
    			    					    	        ,{
    			    					    	            xtype: 'textarea'
    			    					    	            ,id:"idContenido"	
    			    				    	            	,fieldLabel: 'Descripci&oacute;n'
    			    			    	            		,labelWidth: 150
    			    			    	            		,width: 750
    			    			    	            		,name:'params.contenido'
    			    		    	            			,height: 250
    			    		    	            			,allowBlank: false
    			    		    	            			,blankText:'La descripci&oacute;n es un dato requerido'
    			    		    	            			,value: txtContenido
    			    					    	        }],
    			    					    	buttons: [{
    			    					    		text: 'Actualizar',
    			    					    		icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
    			    					    		buttonAlign : 'center',
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
    			    					    	   	                    msg: "La cl&aacute;usula se modific&oacute; correctamente",
    			    					    	   	                    buttons: Ext.Msg.OK
    			    					    	   	                });    			    					    						
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
    			    					    	},
    			    					    	{
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
    			    		        });
    			    		    	modificacionClausula.show(); 
    				    	
    				    },
    				    failure : function ()
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
   
    
});