Ext.onReady(function(){

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();
    // var para los grids
    var storeConceptosCobertura;
    var selectedId;
    var afuera;
 	var temporal=-1;
 //*************** grid conceptos por cobertura***********   
    function ConceptosCobertura(){        		       
    		url='conceptosCobertura/CargaListasConceptosPorCobertura.action?lista=conceptosPorCobertura'; 			 		 			
 			storeConceptosCobertura = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaConceptosCobertura',   
            	totalProperty: 'totalCount',
            	id: 'readerlistaConceptosCobertura'
            	         	
	        	},[
	        		{name: 'codigoPeriodo',  type: 'string',  mapping:'codigoPeriodo'},
	        		{name: 'descripcionPeriodo',  type: 'string',  mapping:'descripcionPeriodo'},
	        		{name: 'codigoCobertura',  type: 'string',  mapping:'codigoCobertura'},
	        		{name: 'descripcionCobertura',  type: 'string',  mapping:'descripcionCobertura'},
	        		{name: 'codigoConcepto',  type: 'string',  mapping:'codigoConcepto'},
	        		{name: 'descripcionConcepto',  type: 'string',  mapping:'descripcionConcepto'},
	        		{name: 'codigoComportamiento',  type: 'string',  mapping:'codigoComportamiento'},
	        		{name: 'descripcionComportamiento',  type: 'string',  mapping:'descripcionComportamiento'},
	        		{name: 'codigoCondicion',  type: 'string',  mapping:'codigoCondicion'},
	        		{name: 'descripcionCondicion',  type: 'string',  mapping:'descripcionCondicion'},
	        		{name: 'orden'   , type:'string' , mapping:'orden'},
	        		{name: 'cdtipcon', type:'string' , mapping:'cdtipcon'},
	        		{name: 'dstipcon', type:'string' , mapping:'dstipcon'},
	        		{name: 'cdexpres', type:'string' , mapping:'cdexpres'}
	        		            
				]),
			//autoLoad:true,
			remoteSort: true
			
    	});
    	storeConceptosCobertura.setDefaultSort('descripcionPeriodo', 'desc');
    	
		return storeConceptosCobertura;
 	}
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Periodo", 	   dataIndex:'descripcionPeriodo',	width: 100, sortable:true,id:'descripcionPeriodo'},		    
		    {header: "Orden",   dataIndex:'orden',	width: 100, sortable:true},
		    //{header: "Cobertura",   dataIndex:'codigoCobertura',	width: 100, sortable:true},
		    //{header: "Nombre de Cobertura",   dataIndex:'descripcionCobertura',	width: 150, sortable:true},
		    {header: "Concepto",   dataIndex:'codigoConcepto',	width: 100, sortable:true},
		    {header: "Nombre de Concepto",   dataIndex:'descripcionConcepto',	width: 200, sortable:true},
		    {header: "Comportamiento",   dataIndex:'descripcionComportamiento',	width: 150, sortable:true},
		    {header: "Condici&oacute;n",   dataIndex:'descripcionCondicion',	width: 100, sortable:true}
		   
		   	                	
        ]);
        var grid1;
        var selectedId;
 		//var afuera;
 		//var temporal=-1;
 		
 	
	gridConceptoCobertura= new Ext.grid.GridPanel({
		store:ConceptosCobertura(),
		id:'grid-conceptoCobertura',
		border:true,
		//baseCls:' background:white ',
		cm: cm,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		/*listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeConceptosCobertura.data.items[row].id;
	        	 			//var sel = Ext.getCmp('grid-lista1').getSelectionModel().getSelected();
	        	 			//selIndexVariables = storeVariables.indexOf(rec);
                            		 //alert("row"+row);
                            		 
                            afuera=row;     
	        	 			Ext.getCmp('eliminar-ConceptosCobertura').on('click',function(){                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;                           			
                            			var rec = storeConceptosCobertura.getAt(temporal);
    									var eliminaCodigoPeriodo= rec.get('codigoPeriodo');                          
    									var eliminaOrden=rec.get('orden');
                            			Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
           									if(btn == 'yes'){ 
           			     
			     									var conn = new Ext.data.Connection();
                 									conn.request ({	
				    				 					url:'conceptosCobertura/EliminarConceptosPorCobertura.action?codigoPeriodo='+eliminaCodigoPeriodo+'&orden='+eliminaOrden,
				     									waitTitle:'Espere',
					            						waitMsg:'Procesando...',
			     	 									callback: function (options, success, response) {			     	 		
                            	 							if (Ext.util.JSON.decode(response.responseText).success == false) {
                              	 								Ext.Msg.alert('Status', 'Error al eliminar el elemento');                               
                             								} else {
                                 								Ext.Msg.alert('Status', 'Elemento eliminado');
                                 								storeConceptosCobertura.load();
                               								}
                           								}
        	 										});         	 
        									}
       									});
                                    }                                                               
                                 });
                                 temporal=-1;
                            	 Ext.getCmp('editar-ConceptosCobertura').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			//alert("AFUERA: " + afuera);
                            			//alert("hidden: " + Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue());
                            			//alert("TEMPORAL: " + temporal);
                            			//alert("STORE: " + storeConceptosCobertura);
                            			temporal=afuera;
                            			//alert("TEMPORAL2: " + temporal);
                            			//alert("temporal"+temporal);
                            			configuraConceptoCobertura (storeConceptosCobertura,Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue(),afuera);
                            		}                                                                     
                                 });
                                 temporal=-1;
                                 Ext.getCmp('editar-ConceptosCoberturaExpresion').on('click',function(){
                             		
                             		if(afuera!=temporal){
                             			//alert("AFUERA: " + afuera);
                             			//alert("hidden: " + Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue());
                             			//alert("TEMPORAL: " + temporal);
                             			//alert("STORE: " + storeConceptosCobertura);
                             			temporal=afuera;
                             			var rec = storeConceptosCobertura.getAt(temporal);
                             			//console.log('record:',rec);
                             			//alert("TEMPORAL2: " + temporal);
                             			//alert("temporal"+temporal);
                             			//configuraConceptoCobertura (storeConceptosCobertura,Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue(),afuera);
                             			//ExpresionesVentana2(codigoDeExpresion, "EXPRESION_VARIABLES_TEMPORALES", null, '5');
                             			var storeAux = new Ext.data.SimpleStore({
										    fields: [{name: 'codigoExpresion'},{name: 'descripcionCabecera'},{name:'descripcionTipo'},{name:'nombreCabecera'}],
										    data: [[rec.get('cdexpres'),rec.get('descripcionConcepto'),rec.get('dstipcon'),rec.get('codigoConcepto')]]
										});
										
										ExpresionesVentana2(rec.get('cdexpres'), 'EXPRESION_CONCEPTO_TARIFICACION', storeAux, '2', 0);
                             		}                                                                     
                                  });
                                  temporal=-1;
	                   	 }
	               	}*/
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar concepto por cobertura',
            iconCls:'add',
            handler: function() {                    	
				     	configuraConceptoCobertura(storeConceptosCobertura,Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue());
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-ConceptosCobertura',
            tooltip:'Eliminar concepto por cobertura',
            iconCls:'remove',
            handler : function()
            {
            	var gridTmp  = gridConceptoCobertura;
            	var storeTmp = storeConceptosCobertura;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		var rec = storeConceptosCobertura.getAt(indexTmp);
					var eliminaCodigoPeriodo= rec.get('codigoPeriodo');                          
					var eliminaOrden=rec.get('orden');
        			Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este elemento?', function(btn) {
							if(btn == 'yes'){ 
	     
									var conn = new Ext.data.Connection();
									conn.request ({	
				 					url:'conceptosCobertura/EliminarConceptosPorCobertura.action?codigoPeriodo='+eliminaCodigoPeriodo+'&orden='+eliminaOrden,
 									waitTitle:'Espere',
            						waitMsg:'Procesando...',
	 									callback: function (options, success, response) {			     	 		
        	 							if (Ext.util.JSON.decode(response.responseText).success == false) {
          	 								Ext.Msg.alert('Status', 'Error al eliminar el elemento');                               
         								} else {
             								Ext.Msg.alert('Status', 'Elemento eliminado');
             								storeConceptosCobertura.load();
           								}
       								}
									});         	 
						}
						});
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        },'-',{
            text:'Editar',
            id:'editar-ConceptosCobertura',
            tooltip:'Editar concepto por cobertura',
            iconCls:'option',
            handler : function()
            {
            	var gridTmp  = gridConceptoCobertura;
            	var storeTmp = storeConceptosCobertura;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		configuraConceptoCobertura (storeConceptosCobertura,Ext.getCmp('hidden-codigo-combo-conceptos-cobertura').getValue(),indexTmp);
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        },'-',{
            text:'Editar expresi&oacute;n',
            id:'editar-ConceptosCoberturaExpresion',
            tooltip:'Editar expresi&oacute;n del concepto',
            iconCls:'option',
            handler : function()
            {
            	var gridTmp  = gridConceptoCobertura;
            	var storeTmp = storeConceptosCobertura;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		var rec = storeConceptosCobertura.getAt(indexTmp);
         			var storeAux = new Ext.data.SimpleStore({
					    fields: [{name: 'codigoExpresion'},{name: 'descripcionCabecera'},{name:'descripcionTipo'},{name:'nombreCabecera'}],
					    data: [[rec.get('cdexpres'),rec.get('descripcionConcepto'),rec.get('dstipcon'),rec.get('codigoConcepto')]]
					});
					
					ExpresionesVentana2(rec.get('cdexpres'), 'EXPRESION_CONCEPTO_TARIFICACION', storeAux, '2', 0);
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        }],      							        	    	    
    	width:650,
        height:290,
    	frame:true,     
		title:'Conceptos Por Cobertura',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true/*,forceFit:true*/},                
		bbar: new Ext.PagingToolbar({
			pageSize:25,
			store: storeConceptosCobertura,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {1}',
			emptyMsg: "No existen resultados para mostrar"      		    	
			})        							        				        							 
		}); 	
	

	//storeTarificacion.load(); 
 //********************************************************     
    var tabConceptosCobertura = new Ext.FormPanel({
        //labelAlign: 'top',
        id: 'tabConceptosCobertura-form',        
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [{xtype:'hidden',id:'hidden-codigo-combo-conceptos-cobertura',name:'codigoCobertura2'},        		
        		gridConceptoCobertura]
    });
	
    tabConceptosCobertura.render('centerConceptosCobertura');
});



