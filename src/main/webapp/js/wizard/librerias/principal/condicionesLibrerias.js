Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var storeCondiciones;

    

    var selectedId;
    var afuera;
 	var temporal=-1;
 
 //********************************************************   
 
 //********************grid condiciones********************
     function condiciones(){        		        				
 			url='librerias/CargaReglaNegocio.action'+'?numeroGrid='+'3' 			 		 			
 			storeCondiciones = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioCondiciones',   
            	totalProperty: 'totalCount',
            	id: 'nombreCabecera',
            	successProperty : '@success'
            	         	
	        	},[
	        		{name: 'nombreCabecera',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcionCabecera',  type: 'string',  mapping:'descripcion'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		            
				]),
			//autoLoad:true,
			baseParams:{limit:'-1'},
			remoteSort: true
    	});
    	storeCondiciones.setDefaultSort('descripcionCabecera', 'desc');
    	//storeCondiciones.load();
		return storeCondiciones;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cmCondiciones = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Nombre", 	   dataIndex:'nombreCabecera',	width: 120, sortable:true,id:'nombreCabecera'},		    
		    {header: "Descripci�n",   dataIndex:'descripcionCabecera',	width: 120, sortable:true}
		   
		   	                	
        ]);
        var grid3;
        var selectedId;
 		
 	function createGridCondiciones(){
	grid3= new Ext.grid.GridPanel({
		store:condiciones(),
		id:'grid-condiciones',
		border:true,
		//baseCls:' background:white ',
		cm: cmCondiciones,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		/*listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeCondiciones.data.items[row].id;
	        	 			//var sel = Ext.getCmp('grid-lista3').getSelectionModel().getSelected();
	        	 			//selIndexValidaciones = storeCondiciones.indexOf(sel);
	        	 			Ext.getCmp('eliminar-condiciones').on('click',function(){                            		
                            		//DeleteDemouser(storeCondiciones,selectedId,sel,listaValoresForm);
                                 });
                            
                            afuera=row;     
                            Ext.getCmp('editar-condiciones').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;
                            			//alert("temporal"+temporal);
                            			var codigoExpresion = storeCondiciones.getAt(afuera).get('codigoExpresion');
                            			if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!="undefined"){ 
                            				
                            				
                            				ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', storeCondiciones, '3', afuera, function(){
            									storeCondiciones.load();
            								});
					            		}else{
	            								var connect = new Ext.data.Connection();
											    connect.request ({
													url:'atributosVariables/ObtenerCodigoExpresion.action',
													callback: function (options, success, response) {				   
														codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
														ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', storeCondiciones, '3', afuera, function(){
															storeCondiciones.load();
														});
													}
										   		});
					            		}
                            			//ExpresionesVentanaCondiciones(storeCondiciones,rec,temporal);
                            		}                                                                     
                                 });
                                 temporal=-1;    
                            /*     
                            Ext.getCmp('editar-grid').on('click',function(){                            		
                            		 ExpresionesVentanaCondiciones(storeCondiciones,rec,row);
                                                                                                             
                                 });**     
	                   	 }
	               	}*/
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar condici&oacute;n',
            iconCls:'add',
            handler: function() {                    	
				     	var connect = new Ext.data.Connection();
					    connect.request ({
							url:'atributosVariables/ObtenerCodigoExpresion.action',
							callback: function (options, success, response) {				   
								codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
								
								ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', storeCondiciones, '3' ,null, function(){
									storeCondiciones.load();
								});
							}
				   		});
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-condiciones',
            tooltip:'Eliminar condici�n',
            iconCls:'remove'
        },'-',{
            text:'Editar',
            id:"editar-condiciones",
            tooltip:'Editar condici&oacute;n',
            iconCls:'option',
            handler:function()
            {
            	var gridTmp  = grid3;
            	var storeTmp = storeCondiciones;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		var codigoExpresion = storeCondiciones.getAt(indexTmp).get('codigoExpresion');
            		if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!=undefined)
            		{
            			ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', storeCondiciones, '3', indexTmp, function()
            			{
            				storeCondiciones.load();
            			});
            		}
            		else
            		{
            			var connect = new Ext.data.Connection();
            			connect.request (
            			{
            				url:'atributosVariables/ObtenerCodigoExpresion.action',
            				callback: function (options, success, response)
            				{
            					codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
            					ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONDICIONES', storeCondiciones, '3', indexTmp, function()
            					{
            						storeCondiciones.load();
            					});
            				}
            			});
            		}
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        }],      							        	    	    
    	width:600,
        height:370,
    	frame:true,     
		//title:'Condiciones',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true}/*,                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeCondiciones,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})*/        							        				        							 
		}); 	
	}
	
	createGridCondiciones();
	//storeCondiciones.load();
	
	var FiltroLista = new Ext.form.TextField({
		fieldLabel: 'Filtrar',
		id: 'filtroListaCondicionesId',
		width:150
	});
	
 //********************************************************     
    var tab2 = new Ext.FormPanel({
        labelAlign: 'top',
        id: 'tab2-form3',
        title: 'Condiciones',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [FiltroLista,grid3]
    });

    tab2.render('libreriasCondiciones');
    
    $('#filtroListaCondicionesId').on('keyup',function(){
		storeCondiciones.filterBy(function(record, id){
			
			var key = record.get('nombreCabecera').toUpperCase().replace(/ /g,'');
			var value = record.get('descripcionCabecera').toUpperCase().replace(/ /g,'');
			
			var filtro = FiltroLista.getValue().toUpperCase().replace(/ /g,'');
    		var posK = key.lastIndexOf(filtro);
    		var posV = value.lastIndexOf(filtro);
    		
    		if(posK > -1 || posV > -1){
    			return true;
    		}else{
    			return false;
    		}
		});
	});
});