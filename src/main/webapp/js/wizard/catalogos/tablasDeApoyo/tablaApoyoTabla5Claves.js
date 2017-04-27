   Ext.onReady(function(){  

    Ext.QuickTips.init();
	Ext.QuickTips.enable();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();
    // var para los grids
    
	var storeTabla5Claves;
    
    var selIndexVariables;
    var selIndexValidaciones;          
    var selectedId;
    var afuera;
 	var temporal=-1;
 //*************** grid Tabla5Claves***********   

 	function toggleDetails(btn, pressed){
       	var view = grid.getView();
       	view.showPreview = pressed;
       	view.refresh();
    }

    function Tabla5Claves(){        		        				
 			var urlTabla5Claves='expresiones/ComboTabla.action'; 			 		 			
 			storeTabla5Claves = new Ext.data.Store({
		        proxy: new Ext.data.HttpProxy({
					url: urlTabla5Claves
        		}),
		        reader: new Ext.data.JsonReader({
		            root: 'listaTablaJson',
		            totalProperty: 'totalCount',
        		    id: 'comboAbuelo',
        		    successProperty : '@success'
		        },[
    	    	   {name: 'key', type: 'string',mapping:'key'},
	        	   {name: 'value', type: 'string',mapping:'value'},
	        	   {name: 'nick', type: 'string',mapping:'nick'}      
    		 	]),
    		 	baseParams:{limit:'-1'},
				remoteSort: true
    		});     	
			return storeTabla5Claves;
 	}

		var cm = new Ext.grid.ColumnModel([
		    new Ext.grid.RowNumberer(),
			{header: "Codigo Tabla", 	   dataIndex:'key',	width: 250, sortable:true,id:'key'},		    
		    {header: "Descripcion Tabla",   dataIndex:'value',	width: 250, sortable:true}
		   
		   	                	
        ]);
       
 		
 	
	var gridTabla5Claves= new Ext.grid.GridPanel({
		store: Tabla5Claves(),
		id:'grid-Tabla5Claves',
		border:true,
		//baseCls:' background:white ',
		cm: cm,
		sm: new Ext.grid.RowSelectionModel({
		singleSelect: true
		}),
		tbar:[{
            text:'Agregar',
            tooltip:'Agregar',
            iconCls:'add',
            handler: function() {                    	
				     	TablasDeApoyo(storeTabla5Claves);
				     }
        },'-',{
            text:'Eliminar',
            id:'eliminar-Tabla5Claves',
            tooltip:'Eliminar',
            iconCls:'remove',
            handler: function() {          
            	if(gridTabla5Claves.getSelectionModel().hasSelection()){
            		var selrecord = gridTabla5Claves.getSelectionModel().getSelected();
            		TablasDeApoyoBorrado(storeTabla5Claves,storeTabla5Claves.indexOf(selrecord),tabTabla5Claves);     
            	}else {
        			Ext.Msg.alert("Aviso","Seleccione un registro.");
        		}  
             }
            
            
        },'-',{
            text:'Editar',
            id:"editar-Tabla5Claves",
            tooltip:'Editar',
            iconCls:'option',
            handler: function() {                            		
        		if(gridTabla5Claves.getSelectionModel().hasSelection()){
        			var selrecord = gridTabla5Claves.getSelectionModel().getSelected();
        			TablasDeApoyo(storeTabla5Claves,storeTabla5Claves.indexOf(selrecord));
        		}else {
        			Ext.Msg.alert("Aviso","Seleccione un registro.");
        		}                                                                     
             }
           
        }],      							        	    	    
    	width:600,
        height:290,
    	frame:true,     
		//title:'Tabla de Cinco Claves',
		bodyStyle:'padding:5px',
		//renderTo:document.body,
		
	
		viewConfig: {autoFill: true,forceFit:true}/*,                
		bbar: new Ext.PagingToolbar({
			pageSize:20,
			store: storeTabla5Claves,									            
			displayInfo: true,
			displayMsg: 'Mostrando registros {0} - {1} de {2}',
			emptyMsg: "No hay resultados"
			})*/        							        				        							 
		}); 	
                        
	var FiltroLista = new Ext.form.TextField({
		fieldLabel: 'Filtrar',
		id: 'filtroListaCincoClavesId',
		width:150
	});
	
     var tabTabla5Claves = new Ext.form.FormPanel({
        labelAlign: 'top',
        id: 'tabs-Tabla5Claves',
        title:'Tabla de Cinco Claves',
        bodyStyle:'padding:5px',
        width: 670,
        autoScroll:true,
        heigth:400,
        items: [FiltroLista,gridTabla5Claves]
    });
    tabTabla5Claves.render('tablaApoyoTabla5Claves');
    
    $('#filtroListaCincoClavesId').on('keyup',function(){
		storeTabla5Claves.filterBy(function(record, id){
			
			var key = record.get('key').toUpperCase().replace(/ /g,'');
			var value = record.get('value').toUpperCase().replace(/ /g,'');
			
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