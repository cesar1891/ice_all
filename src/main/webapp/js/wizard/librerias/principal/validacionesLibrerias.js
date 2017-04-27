Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var storeValidaciones;

    

    var selectedId;
    var afuera;
 	var temporal=-1;
 
//*********************************************************
 
 //********************grid validaciones********************
     function validaciones(){        		        				
 			url='librerias/CargaReglaNegocio.action'+'?numeroGrid='+'2' 			 		 			
 			storeValidaciones = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioValidaciones',   
            	totalProperty: 'totalCount',
            	id: 'nombreCabecera',
            	successProperty : '@success'
            	         	
	        	},[
	        		{name: 'nombreCabecera',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcionCabecera',  type: 'string',  mapping:'descripcion'},
	        		{name: 'descripcionTipo',  type: 'string',  mapping:'descripcionTipo'},
	        		{name: 'mensaje',  type: 'string',  mapping:'mensaje'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		            
				]),
			//autoLoad:true,
			baseParams:{limit:'-1'},
			remoteSort: true
    	});
    	storeValidaciones.setDefaultSort('descripcionCabecera', 'desc');
    	//storeValidaciones.load();
		return storeValidaciones;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cmValidaciones = new Ext.grid.ColumnModel([
		     new Ext.grid.RowNumberer(),
			{header: "Nombre", 	   dataIndex:'nombreCabecera', sortable:true,id:'nombreCabecera', width: 100},		    
		    {header: "Descripci�n",   dataIndex:'descripcionCabecera', sortable:true, width: 300},
		    //{header: "Tipo", 	   dataIndex:'descripcionTipo', sortable:true}		    
		    {header: "Mensaje",   dataIndex:'mensaje', sortable:true, width: 500}
		   
		   	                	
        ]);
        var grid2;
        var selectedId;
 		
 	function createGridValidaciones(){
	grid2= new Ext.grid.GridPanel({
		store:validaciones(),
		id:'grid-validaciones',
		border:true,
		//baseCls:' background:white ',
		cm: cmValidaciones,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		/*listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeValidaciones.data.items[row].id;
                            afuera=row;     
                            Ext.getCmp('editar-validaciones').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;
                            			var codigoExpresion = storeValidaciones.getAt(afuera).get('codigoExpresion');
                            			if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!="undefined"){ 
                            				ExpresionesVentana2(codigoExpresion, 'EXPRESION_VALIDACIONES', storeValidaciones, '4', afuera, function(){
                            					storeValidaciones.load();
            								});
					            		}else{
	            								var connect = new Ext.data.Connection();
											    connect.request ({
													url:'atributosVariables/ObtenerCodigoExpresion.action',
													callback: function (options, success, response) {				   
														codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
														ExpresionesVentana2(codigoExpresion, 'EXPRESION_VALIDACIONES', storeValidaciones, '4', afuera, function(){
															storeValidaciones.load();
			            								});
													}
										   		});
					            		}
                            		}                                                                     
                                 });
                                 temporal=-1;   
	                   	 }
	               	}*/
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar validaci�n',
            iconCls:'add',
            handler: function() {                    	
				     	var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									var codigoDeExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
									ExpresionesVentana2(codigoDeExpresion, 'EXPRESION_VALIDACIONES', storeValidaciones, '4', null, function(){
										storeValidaciones.load();
    								});
								}
					   		});
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-validaciones',
            tooltip:'Eliminar validaci�n',
            iconCls:'remove'
            
        },'-',{
            text:'Editar',
            id:"editar-validaciones",
            tooltip:'Editar validaci&oacute;n',
            iconCls:'option',
            handler : function()
            {
            	var gridTmp  = grid2;
            	var storeTmp = storeValidaciones;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		var codigoExpresion = storeValidaciones.getAt(indexTmp).get('codigoExpresion');
            		if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!=undefined)
            		{ 
            			ExpresionesVentana2(codigoExpresion, 'EXPRESION_VALIDACIONES', storeValidaciones, '4', indexTmp, function()
    					{
            				storeValidaciones.load();
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
    							ExpresionesVentana2(codigoExpresion, 'EXPRESION_VALIDACIONES', storeValidaciones, '4', indexTmp, function()
								{
    								storeValidaciones.load();
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
		//title:'Validaciones',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: false,forceFit:false}/*,                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeValidaciones,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})*/         							        				        							 
		});	
	}
	
	createGridValidaciones();
	//storeValidaciones.load(); 

	var FiltroLista = new Ext.form.TextField({
		fieldLabel: 'Filtrar',
		id: 'filtroListaValidacionesId',
		width:150
	});
	
 //********************************************************     
    var tab2 = new Ext.FormPanel({
        labelAlign: 'top',
        id: 'tab2-form2',
        title: 'Validaciones',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [FiltroLista,grid2]
    });

    tab2.render('libreriasValidaciones');
    
    $('#filtroListaValidacionesId').on('keyup',function(){
		storeValidaciones.filterBy(function(record, id){
			
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