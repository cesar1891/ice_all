Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var storeTarificacion;

    

    var selectedId;
    var afuera;
 	var temporal=-1;
 
 //********************************************************     
 //********************grid concepto de tarificacion********************
     function tarificacion(){        		        				
 			url='librerias/CargaReglaNegocio.action'+'?numeroGrid='+'5' 			 		 			
 			storeTarificacion = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaReglaNegocioTarificacion',   
            	totalProperty: 'totalCount',
            	id: 'nombreCabecera',
            	successProperty : '@success'
            	         	
	        	},[
	        		{name: 'nombreCabecera',  type: 'string',  mapping:'nombre'},
	        		{name: 'descripcionCabecera',  type: 'string',  mapping:'descripcion'},
	        		{name: 'descripcionTipo',  type: 'string',  mapping:'descripcionTipo'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		            
				]),
			//autoLoad:true,
			baseParams:{limit:'-1'},
			remoteSort: true
    	});
    	storeTarificacion.setDefaultSort('descripcionCabecera', 'desc');
    	//storeTarificacion.load();
		return storeTarificacion;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cmTarificacion = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Nombre", 	   dataIndex:'nombreCabecera',	width: 120, sortable:true,id:'nombreCabecera'},		    
		    {header: "Descripción",   dataIndex:'descripcionCabecera',	width: 250, sortable:true},
		    {header: "Tipo",   dataIndex:'descripcionTipo',	width: 250, sortable:true}
		   
		   	                	
        ]);
        var grid5;
        var selectedId;
 		
 	function createGridTarificacion(){
	grid5= new Ext.grid.GridPanel({
		store:tarificacion(),
		id:'grid-tarificacion',
		border:true,
		//baseCls:' background:white ',
		cm: cmTarificacion,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar tarificación',
            iconCls:'add',
            handler: function() {                    	
				     	var connect = new Ext.data.Connection();
						    connect.request ({
								url:'atributosVariables/ObtenerCodigoExpresion.action',
								callback: function (options, success, response) {				   
									var codigoDeExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
									ExpresionesVentana2(codigoDeExpresion, 'EXPRESION_CONCEPTO_TARIFICACION', storeTarificacion, '2', null, function(){
                    					storeTarificacion.load();
    								});
								}
					   		});
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-tarificacion',
            tooltip:'Eliminar tarificación',
            iconCls:'remove',
            handler: function() {          
            	if(grid5.getSelectionModel().hasSelection()){
            		var selrecord = grid5.getSelectionModel().getSelected();
            		
            		//DeleteDemouser(storeTarificacion,selectedId,sel,listaValoresForm);
            	}else {
        			Ext.Msg.alert("Aviso","Seleccione un registro.");
        		}  
             }
            
        },'-',{
            text:'Editar',
            id:'editar-tarificacion',
            tooltip:'Editar tarificación',
            iconCls:'option',
            handler: function() {                            		
        		if(grid5.getSelectionModel().hasSelection()){
        			var selrecord = grid5.getSelectionModel().getSelected();
        			var recordIndex = storeTarificacion.indexOf(selrecord);

            			var codigoExpresion = selrecord.get('codigoExpresion');
            			if(codigoExpresion!=null && codigoExpresion!="" && codigoExpresion!="0" && codigoExpresion!="undefined"){ 
            				ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONCEPTO_TARIFICACION', storeTarificacion, '2', recordIndex, function(){
            					storeTarificacion.load();
							});
	            		}else{
								var connect = new Ext.data.Connection();
							    connect.request ({
									url:'atributosVariables/ObtenerCodigoExpresion.action',
									callback: function (options, success, response) {				   
										codigoExpresion = Ext.util.JSON.decode(response.responseText).codigoExpresion;
										ExpresionesVentana2(codigoExpresion, 'EXPRESION_CONCEPTO_TARIFICACION', storeTarificacion, '2', recordIndex, function(){
                        					storeTarificacion.load();
        								});
									}
						   		});
	            		}
            			//ExpresionesVentanaTarificaciones(storeTarificacion,rec,temporal);
            		                                                             
                 
        		}else {
        			Ext.Msg.alert("Aviso","Seleccione un registro.");
        		}                                                                     
             }
        }],      							        	    	    
    	width:600,
        height:370,
    	frame:true,     
		//title:'Concepto De Tarificación',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: false,forceFit:false}/*,                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeTarificacion,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})*/        							        				        							 
		}); 	
	}
	
	createGridTarificacion();
	//storeTarificacion.load(); 
	
	var FiltroLista = new Ext.form.TextField({
		fieldLabel: 'Filtrar',
		id: 'filtroListaConcepTarifId',
		width:150
	});
	
 //********************************************************     
    var tab2 = new Ext.FormPanel({
        labelAlign: 'top',
        id: 'tab2-form5',
        title: 'Concepto De Tarificaci\u00F3n',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [FiltroLista,grid5]
    });

    tab2.render('libreriasTarificacion');
    
    $('#filtroListaConcepTarifId').on('keyup',function(){
		storeTarificacion.filterBy(function(record, id){
			
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