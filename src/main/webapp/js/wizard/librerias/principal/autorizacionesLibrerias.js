   Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var storeAutorizaciones;

    

    var selectedId;
    var afuera;
 	var temporal=-1;
 
 //********************************************************  
 //********************grid autorizaciones********************
     function autorizaciones(){        		        				
 			url='librerias/CargaReglaNegocio.action'+'?numeroGrid='+'4' 			 		 			
 			storeAutorizaciones = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioAutorizaciones',   
            	totalProperty: 'totalCount',
            	id: 'nombreCabecera',
            	successProperty : '@success'
            	         	
	        	},[
	        		{name: 'nombreCabecera',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcionCabecera',  type: 'string',  mapping:'descripcion'},
	        		{name: 'nivel',  type: 'string',  mapping:'nivel'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		            
				]),
			//autoLoad:true,
			remoteSort: true
    	});
    	storeAutorizaciones.setDefaultSort('descripcionCabecera', 'desc');
    	//storeAutorizaciones.load();
		return storeAutorizaciones;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cmAutorizaciones = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Nombre", 	   dataIndex:'nombreCabecera',	width: 120, sortable:true,id:'nombreCabecera'},		    
		    {header: "Descripción",   dataIndex:'descripcionCabecera',	width: 120, sortable:true},
		    {header: "Nivel",   dataIndex:'nivel',	width: 120, sortable:true}
		   
		   	                	
        ]);
        var grid4;
        var selectedId;
 		

	grid4= new Ext.grid.GridPanel({
		store:autorizaciones(),
		id:'grid-autorizaciones',
		border:true,
		//baseCls:' background:white ',
		cm: cmAutorizaciones,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeAutorizaciones.data.items[row].id;
	        	 			//var sel = Ext.getCmp('grid-lista4').getSelectionModel().getSelected();
	        	 			//selIndexValidaciones = storeAutorizaciones.indexOf(sel);
	        	 			Ext.getCmp('eliminar-autorizaciones').on('click',function(){                            		
                            		//DeleteDemouser(storeAutorizaciones,selectedId,sel,listaValoresForm);
                                                                                                       
                                 });
                            
                            afuera=row;     
                            Ext.getCmp('editar-autorizaciones').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;
                            			//alert("temporal"+temporal);
                            			var codigoExpresion = storeAutorizaciones.getAt(row).get('codigoExpresion');
                            			if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!="undefined"){ 
                            				ExpresionesVentana2(codigoExpresion, "EXPRESION", storeAutorizaciones, '1', row);
					            		}else{
	            								var connect = new Ext.data.Connection();
											    connect.request ({
													url:'atributosVariables/ObtenerCodigoExpresion.action',
													callback: function (options, success, response) {				   
														codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
														ExpresionesVentana2(codigoExpresion, "EXPRESION", storeAutorizaciones, '1', row);
													}
										   		});
					            		}
                            			//ExpresionesVentanaAutorizaciones(storeAutorizaciones,rec,temporal);
                            		}                                                                     
                                 });
                                 temporal=-1;    
                            /*     
                            Ext.getCmp('editar-grid').on('click',function(){                            		
                            		 ExpresionesVentanaAutorizaciones(storeAutorizaciones,rec,row);
                                                                                                           
                                 });*/     
	                   	 }
	               	}
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar autorización',
            iconCls:'add',
            handler: function() {                    	
				     	var connect = new Ext.data.Connection();
					    connect.request ({
							url:'atributosVariables/ObtenerCodigoExpresion.action',
							callback: function (options, success, response) {				   
								codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
								ExpresionesVentana2(codigoExpresion, "EXPRESION", storeAutorizaciones, '1');
							}
				   		});
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-autorizaciones',
            tooltip:'Eliminar autorización',
            iconCls:'remove'
            
        },'-',{
            text:'Editar',
            id:"editar-autorizaciones",
            tooltip:'Editar autorización',
            iconCls:'option'
        }],      							        	    	    
    	width:600,
        height:370,
    	frame:true,     
		//title:'Autorizaciones',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true},                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeAutorizaciones,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})        							        				        							 
		}); 	


 //********************************************************     
    var tab2 = new Ext.FormPanel({
        labelAlign: 'top',
        id: 'tab2-form4',
        title: 'Autorizaciones',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [{
            layout:'column',
            border:false,
            items:[{
                columnWidth:.5,
                layout: 'form',
                border:false,
                items: [{
                    xtype:'textfield',
                    fieldLabel: '<s:text name="librerias.cabecera.texto"/>',
                    labelSeparator:'',
                    hidden:true,
                    name: 'first',
                    anchor:'95%'
                }]
            }]
        },{
                //title:'Autorizaciones',
                layout:'form',
                width:610,
                items: [grid4]
		}]
    });

    tab2.render('libreriasAutorizaciones');
});