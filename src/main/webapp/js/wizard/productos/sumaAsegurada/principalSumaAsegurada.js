Ext.onReady(function(){
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = "side";

var sumaAseguradaForm;
var storeSumaAsegurada;
var storeSumaAseguradaInciso;
  
   
    var selectedId;
    var afuera;
 	var temporal=-1;
 	var claveCoberturaSuma;
	var descripcionCoberturaSuma;
 //*************** grid suma nivel producto***********   
 /*   function SumaAsegurada(){        		        				
 			url='sumaAsegurada/CatalogoTipoSumaAsegurada.action'+'?catalogo='+'gridSumaAsegurada'; 			 		 			
 			storeSumaAsegurada = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaSumasAseguradas',   
            	totalProperty: 'totalCount',
            	id: 'descripcionCapital'
            	         	
	        	},[
	        		{name: 'codigoCapital',  type: 'string',  mapping:'codigoCapital'},
	        		{name: 'sumaAseguradaProducto',  type: 'string',  mapping:'descripcionCapital'},
	        		{name: 'codigoMoneda',  type: 'string',  mapping:'codigoMoneda'},
	        		{name: 'descripcionMonedaSumaAsegurada',  type: 'string',  mapping:'descripcionMoneda'},
	        		{name: 'codigoTipoCapital',  type: 'string',  mapping:'codigoTipoCapital'},
	        		{name: 'descripcionTipoSumaAsegurada',  type: 'string',  mapping:'descripcionTipoCapital'}
	        		            
				]),
			//autoLoad:true,
			remoteSort: true
    	});
    	storeSumaAsegurada.setDefaultSort('sumaAseguradaProducto', 'desc');
    	
		return storeSumaAsegurada;
 	}
    
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Suma Asegurada", 	   dataIndex:'sumaAseguradaProducto',	width: 200, sortable:true,id:'descripcionCapital'},		    
		    {header: "Tipo Suma Asegurada",   dataIndex:'descripcionTipoSumaAsegurada',	width: 200, sortable:true},
		    {header: "Moneda",   dataIndex:'descripcionMonedaSumaAsegurada',	width: 200, sortable:true}
		   
		   	                	
        ]);
 		
 	
	gridSumaAsegurada= new Ext.grid.GridPanel({
		store:SumaAsegurada(),
		id:'grid-SumaAsegurada-producto',
		border:true,
		baseCls:' background:white ',
		cm: cm,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeSumaAsegurada.data.items[row].id;	        	 			
                            		 
                            afuera=row;     
	        	 			Ext.getCmp('eliminar-sumaAsegurada').on('click',function(){                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;                           			
                            			EliminaSumaAsegurada(storeSumaAsegurada,rec,temporal,sumaAseguradaForm);
                            			
                                    }                                                               
                                 });
                                 temporal=-1;
                            Ext.getCmp('editar-sumaAsegurada').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;
                            			//alert("temporal"+temporal);
                            			AgregaSumaAsegurada (storeSumaAsegurada,temporal);
                            		}                                                                     
                                 });
                                 temporal=-1;
	                   	 }
	               	}
		}),
		tbar:[{
            text:'<s:text name="productos.configSumaAsegurada.btn.agregar"/>',
            tooltip:'Agrega suma asegurada',
            iconCls:'add',
            handler: function() {                    					     	
				     	
				     	AgregaSumaAsegurada(storeSumaAsegurada);
				     	
				     }
        },'-',{
            text:'<s:text name="productos.configSumaAsegurada.btn.eliminar"/>',
            id:'eliminar-sumaAsegurada',
            tooltip:'Elimina suma asegurada',
            iconCls:'remove'
            
            
        },'-',{
            text:'<s:text name="productos.configSumaAsegurada.btn.editar"/>',
            id:"editar-sumaAsegurada",
            tooltip:'Edita suma asegurada',
            iconCls:'option'
           
        }],      							        	    	    
    	width:600,
        height:290,
    	frame:true,     
		//title:'Suma Asegurada',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true},                
		bbar: new Ext.PagingToolbar({
			pageSize:25,
			store: storeSumaAsegurada,									            
			displayInfo: true,
			displayMsg: 'Displaying rows {0} - {1} of {1}',
			emptyMsg: "No rows to display"      		    	
			})        							        				        							 
		}); 	
*/	 
 //*****************************grid suma nivel producto*********************** 

//*************** grid suma nivel inciso***********   
    function SumaAseguradaInciso(){        		        				
 			url='sumaAsegurada/CatalogoTipoSumaAsegurada.action'+'?catalogo='+'gridSumaAseguradaInciso'; 			 		 			
 			storeSumaAseguradaInciso = new Ext.data.Store({
    			proxy: new Ext.data.HttpProxy({
				url: url
        		}),
        		reader: new Ext.data.JsonReader({
            	root:'listaSumasAseguradasInciso',   
            	totalProperty: 'totalCount',
            	id: 'descripcionSumaAsegurada'
            	         	
	        	},[
	        		{name: 'codigoCapital',  type: 'string',  mapping:'codigoCapital'},
	        		{name: 'codigoTipoCapital',  type: 'string',  mapping:'codigoTipoCapital'},
	        		{name: 'descripcionTipoCapital',  type: 'string',  mapping:'descripcionTipoCapital'},
	        		{name: 'codigoListaDeValores',  type: 'string',  mapping:'codigoListaValor'},
	        		{name: 'descripcionListaValor',  type: 'string',  mapping:'descripcionListaValor'},
	        		{name: 'codigoLeyenda',  type: 'string',  mapping:'codigoLeyenda'},
	        		{name: 'descripcionLeyenda',  type: 'string',  mapping:'descripcionLeyenda'},
	        		{name: 'switchReinstalacion',  type: 'string',  mapping:'switchReinstalacion'},
	        		{name: 'codigoCobertura',  type: 'string',  mapping:'codigoCobertura'},
	        		{name: 'descripcionCobertura',  type: 'string',  mapping:'descripcionCobertura'},
	        		{name: 'codigoExpresion',  type: 'string',  mapping:'codigoExpresion'}
	        		
	        		            
				]),
			//autoLoad:true,
			remoteSort: true
    	});
    	storeSumaAseguradaInciso.setDefaultSort('descripcionSumaAsegurada', 'desc');
    	//storeSumaAseguradaInciso.load();
		return storeSumaAseguradaInciso;
 	}
    
 	
 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    	}
		
		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Tipo suma asegurada", 	   dataIndex:'descripcionTipoCapital',	width: 160, sortable:true,id:'descripcionTipoCapital'},		    
		    {header: "Lista de valores",   dataIndex:'descripcionListaValor',	width: 170, sortable:true},
		    {header: "Leyenda",   dataIndex:'descripcionLeyenda',	width: 120, sortable:true},
		    {header: "Reinstalaci�n autom&aacute;tica",   dataIndex:'switchReinstalacion',	width: 100, sortable:true}
		    
		   
		   	                	
        ]);
        
        var selectedId;
 		var afuera;
 		var temporal=-1; 		
 	
	gridSumaAseguradaInciso= new Ext.grid.GridPanel({
		store: SumaAseguradaInciso(),
		id:'grid-SumaAsegurada-inciso',
		border:true,
		baseCls:' background:white ',
		cm: cm,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true,
		/*listeners: {							
        	rowselect: function(sm, row, rec) {	                    		                    	                        	                     	                        
	        	 			selectedId = storeSumaAseguradaInciso.data.items[row].id;	        	 			
                            		 
                            afuera=row;     
	        	 			Ext.getCmp('eliminar-sumaAsegurada-inciso').on('click',function(){                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;                           			
                            			EliminaSumaAsegurada(storeSumaAseguradaInciso,rec,temporal,sumaAseguradaForm);
                            			
                                    }                                                               
                                 });
                                 temporal=-1;
                            Ext.getCmp('editar-sumaAsegurada-inciso').on('click',function(){
                            		
                            		if(afuera!=temporal){
                            			temporal=afuera;
                            			//alert("temporal"+temporal);
                            			claveCoberturaSuma= Ext.getCmp('hidden-clave-cobertura-suma-asegurada').getValue();
										descripcionCoberturaSuma= Ext.getCmp('hidden-descripcion-cobertura-suma-asegurada').getValue();                            			
                            			AgregaSumaAseguradaInciso (claveCoberturaSuma,descripcionCoberturaSuma,storeSumaAseguradaInciso,temporal);
                            		}                                                                     
                                 });
                                 temporal=-1;
	                   	 }
	               	}*/
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agrega suma asegurada',
            iconCls:'add',
            handler: function() {                    					     	
				     	claveCoberturaSuma= Ext.getCmp('hidden-clave-cobertura-suma-asegurada').getValue();
						descripcionCoberturaSuma= Ext.getCmp('hidden-descripcion-cobertura-suma-asegurada').getValue();
				     	AgregaSumaAseguradaInciso(claveCoberturaSuma,descripcionCoberturaSuma,storeSumaAseguradaInciso);
				     	
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-sumaAsegurada-inciso',
            tooltip:'Elimina suma asegurada',
            iconCls:'remove',
            handler : function()
            {
            	var gridTmp  = gridSumaAseguradaInciso;
            	var storeTmp = storeSumaAseguradaInciso;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		EliminaSumaAsegurada(storeSumaAseguradaInciso,null,indexTmp,sumaAseguradaForm);
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        },'-',{
            text:'Editar',
            id:"editar-sumaAsegurada-inciso",
            tooltip:'Edita suma asegurada',
            iconCls:'option',
            handler : function()
            {
            	var gridTmp  = gridSumaAseguradaInciso;
            	var storeTmp = storeSumaAseguradaInciso;
            	debug(gridTmp.getSelectionModel().getSelected());
            	if(gridTmp.getSelectionModel().hasSelection())
            	{
            		var recordTmp = gridTmp.getSelectionModel().getSelected();
            		debug('recordTmp:',recordTmp);
            		var indexTmp = storeTmp.indexOf(recordTmp);
            		debug('indexTmp:',indexTmp);
            		//
            		claveCoberturaSuma= Ext.getCmp('hidden-clave-cobertura-suma-asegurada').getValue();
					descripcionCoberturaSuma= Ext.getCmp('hidden-descripcion-cobertura-suma-asegurada').getValue();                            			
        			AgregaSumaAseguradaInciso (claveCoberturaSuma,descripcionCoberturaSuma,storeSumaAseguradaInciso,indexTmp);
            		//
            	}
            	else
            	{
            		Ext.Msg.alert('Aviso', 'Seleccione un registro');
            	}
            }
        }],      							        	    	    
    	width:600,
        height:290,
    	frame:true,     
		//title:'Suma Asegurada',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true},                
		bbar: new Ext.PagingToolbar({
			pageSize:25,
			store: storeSumaAseguradaInciso,									            
			displayInfo: true,
			displayMsg: 'Displaying rows {0} - {1} of {1}',
			emptyMsg: "No rows to display"      		    	
			})        							        				        							 
		}); 	
	

 
 //*****************************grid suma nivel inciso***********************			
//**************************************************************	


//************FORMA****************
sumaAseguradaForm = new Ext.form.FormPanel({
            	id:'suma-asegurada-form',
            	boder:false,
            	frame:false,
            	labelAlign: 'left',
            	url:'coberturas/AsociaCobertura.action',
            	//labelWidth: 100,
        		title: 'Sumas Aseguradas',
       			bodyStyle:'padding:5px',
        		width:610,
        		//height:250, 
        		buttonAlign: "center",
                items: [{xtype:'hidden',id:'hidden-clave-cobertura-suma-asegurada',name:'claveCobertura'},
                		{xtype:'hidden',id:'hidden-descripcion-cobertura-suma-asegurada',name:'descripcionCobertura'},
                
                	/*{
                		layout:'form',                		
                		id:'id-hidden-form-suma-asegurada-producto',
                		border:false,
                		//labelAlign:'top',
                		items:[{
	        				layout:'column',
		        			border: false,
        	    				items:[
        	    					gridSumaAsegurada
        	    				]        		
		        		}]
		        		},*/{
        				layout:'form',
        				id:'id-hidden-form-suma-asegurada-inciso',        				
        				border: false,
            			items:[{
            				layout:'column',
            				border:false,
            				//labelAlign:'top',
            				items:[            	
				    	 		gridSumaAseguradaInciso
				    	 	]      		
			        	}]
			    }]
    	});
    	    	    	
	sumaAseguradaForm.render('centerSumaAsegurada');
    });