Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var storeVariables;

    

    var selectedId;
    var afuera;
 	var temporal=-1;
 
 //********************************************************  
 //********************grid autorizaciones********************
    function variables(){        		        				
 			url='librerias/CargaReglaNegocio.action'+'?numeroGrid='+'1' 			 		 			
 			storeVariables = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioVariables',   
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
    	storeVariables.setDefaultSort('descripcionCabecera', 'desc');
    	//storeVariables.load();
		return storeVariables;
 	}
 

 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cmVariables = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Nombre", 	   dataIndex:'nombreCabecera',	width: 250, sortable:true,id:'nombreCabecera'},		    
		    {header: "Descripción",   dataIndex:'descripcionCabecera',	width: 250, sortable:true}
		   
		   	                	
        ]);
     
 
	var grid1= new Ext.grid.GridPanel({
		store:variables(),
		id:'grid-variables-temporales',
		border:true,
		//baseCls:' background:white ',
		cm: cmVariables,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar variable temporal',
            iconCls:'add',
            handler: function() {                    	
				     	var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									var codigoDeExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
									ExpresionesVentana2(codigoDeExpresion, 'EXPRESION_VARIABLES_TEMPORALES', storeVariables, '5', null, function(){
                    					storeVariables.load();
    								});
								}
					   		});
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-variables',
            tooltip:'Eliminar variable temporal',
            iconCls:'remove',
            handler: function(){
            				if(grid1.getSelectionModel().hasSelection()){
            					Ext.MessageBox.confirm('Eliminar', 'Se eliminar&aacute; el registro seleccionado', function(btn) {
				                   if (btn == "yes") {
				                   		var conn = new Ext.data.Connection();
				               			conn.request({
										    url: 'librerias/borraVariableTemporal.action',
										    method: 'POST',
										    params: {
										    	descripcionVariableProducto: grid1.getSelectionModel().getSelected().get('nombreCabecera') 
										    },			    		 			 
										    callback: function(options, success, response) {
												if (Ext.util.JSON.decode(response.responseText).success != false) {
													Ext.Msg.alert("Aviso", Ext.util.JSON.decode(response.responseText).mensajeDelAction, function (){ grid1.getStore().load();	});
													
												}else {
													Ext.Msg.alert("Error", Ext.util.JSON.decode(response.responseText).mensajeDelAction);
												}
											}
										});						
				             		}
				              	});
            				}
                			 else{Ext.MessageBox.alert('Aviso','Debe seleccionar un registro para realizar esta operaci&oacute;n');}
	                		}
            
            
        },'-',{
            text:'Editar',
            id:"editar-variables",
            tooltip:'Editar variable temporal',
            iconCls:'option',
            handler: function() {                            		
        		if(grid1.getSelectionModel().hasSelection()){
        			var selrecord = grid1.getSelectionModel().getSelected();
        			var recordIndex = storeVariables.indexOf(selrecord);

        			var codigoExpresion = selrecord.get('codigoExpresion');
        			if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!="undefined"){ 
        				ExpresionesVentana2(codigoExpresion, 'EXPRESION_VARIABLES_TEMPORALES', storeVariables, '5', recordIndex, function(){
        					storeVariables.load();
						});
            		}else{
							var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
									ExpresionesVentana2(codigoExpresion, 'EXPRESION_VARIABLES_TEMPORALES', storeVariables, '5', recordIndex, function(){
                    					storeVariables.load();
    								});
								}
					   		});
            		}
        			//ExpresionesVentanaVariablesTemporales(storeVariables,rec,temporal);
        		
        		}else {
        			Ext.Msg.alert("Aviso","Seleccione un registro.");
        		}                                                                     
             }
           
        }],      							        	    	    
    	width:600,
        height:290,
    	frame:true,     
		//title:'Variables Temporales',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true}/*,                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeVariables,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})*/        							        				        							 
		}); 	

	
	var FiltroLista = new Ext.form.TextField({
		fieldLabel: 'Filtrar',
		id: 'filtroListaVarTempId',
		width:150
	});
	

 //********************************************************     
    var tab2 = new Ext.FormPanel({
        labelAlign: 'top',
        id: 'tab2-form1',
        title: 'Variables Temporales',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [FiltroLista,grid1]
    });

    tab2.render('libreriasVariables');
    
    $('#filtroListaVarTempId').on('keyup',function(){
		storeVariables.filterBy(function(record, id){
			
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