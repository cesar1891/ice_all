Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {

	
	Ext.create('Ext.form.Panel', {
	    width: 400,
	    height: 200,
	    dockedItems: [{
	        xtype: 'toolbar'
	    }],
	    listeners: {
	        click: {
	            element: 'el', //bind to the underlying el property on the panel
	            fn: function(){ console.log('click el'); }
	        },
	        dblclick: {
	            element: 'body', //bind to the underlying body property on the panel
	            fn: function(){ console.log('dblclick body'); }
	        }
	    }
	});
	
	/*
    //////////////////////////////////////
    ////////		MODEL 		/////////
    /////////////////////////////////////
    Ext.define('modelHistoricoReclamaciones',{
        extend: 'Ext.data.Model',
        fields: [
					{type:'string',    name:'key'      						},//	QUITAR AL FINAL
					{type:'string',    name:'value'      					}//	QUITAR AL FINAL
					
        ]
    });
    
    //////////////////////////////////////
    ////////		STORE 		/////////
    /////////////////////////////////////
    var storeGridHistoricoReclamaciones = new Ext.data.Store(
    {
    	pageSize : 5
        ,model      : 'modelHistoricoReclamaciones'
        ,autoLoad  : false
        ,proxy     :
        {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
    });
    
    var storeTipoAutorizacion= Ext.create('Ext.data.JsonStore', {
    	model:'Generic',
        proxy: {
            type: 'ajax',
            url: _URL_TIPO_AUTORIZACION,
            reader: {
                type: 'json',
                root: 'tiposAutorizacion'
            }
        }
    });
    storeTipoAutorizacion.load();
    
    
    codigoAsegurado= Ext.create('Ext.form.ComboBox',
	{
		//colspan:2,
		id:'tipoAutorizacion',				name:'params.tipoAutorizacion',		fieldLabel: 'Tipo autorizaci&oacute;n',		store: storeTipoAutorizacion,
		queryMode:'local',					displayField: 'value',				valueField: 'key',							allowBlank:false,
		blankText:'Es un dato requerido',	editable:false,						labelWidth : 170,
		width: 400,							emptyText:'Seleccione Autorizaci&oacute;n ...',
		listeners : {
			'select' : function(combo, record) {
					closedStatusSelectedID = this.getValue();
					if(closedStatusSelectedID !=1){
						Ext.getCmp('panelbusqueda').show();
						Ext.getCmp('clausulasGridId').show();						
					}else{
						Ext.getCmp('panelbusqueda').hide();
						Ext.getCmp('clausulasGridId').hide();
					}    					
				}
			}
	});
    
    panelbusquedas= Ext.create('Ext.panel.Panel',
	{
		border  : 0,		id     : 'panelbusqueda',				width	: 600,		style  : 'margin:5px'
		,layout :
		{
			type  : 'table',			columns : 2
		}
		,defaults : 
		{
			style : 'margin:5px;'
		}
		,items :
		[
			{
				xtype : 'textfield',		labelWidth: 168,	fieldLabel : 'Nombre/C&oacute;digo asegurado',	id : 'idAsegurado',		width: 500,
				allowBlank:false
				
			}
		]
		,buttonAlign: 'center'
		,buttons : [{
		text: "Buscar"
		,icon:_CONTEXT+'/resources/fam3icons/icons/magnifier.png'
		,handler: function(){
	
		storeGridHistoricoReclamaciones.removeAll();
	
		var params = {
		'params.cdclausu' : Ext.getCmp('idComboClau').getValue(),
		'params.dsclausu' : Ext.getCmp('idFiltroDes').getValue()
		};
		cargaStorePaginadoLocal(storeGridHistoricoReclamaciones, _URL_CONSULTA_CLAUSU, 'listaGenerica', params, function(options, success, response){
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
		}] 
	});
    

    
    
    gridDatos=            Ext.create('Ext.grid.Panel',
            {
        id             : 'clausulasGridId'
        //,title         : 'Hist&oacute;rico de Reclamaciones'
        ,store         :  storeGridHistoricoReclamaciones
        ,collapsible   : true
        ,titleCollapse : true
        ,style         : 'margin:5px'
        ,width   : 600
        ,height: 200
        ,columns       :
        [
            {
                header     : 'Clave'
                ,dataIndex : 'key'
            	,width	   : 200
                //,flex      : 1
            }
            ,
            {
                header     : 'Descripcion de la Cl&aacute;usula'
                ,dataIndex : 'value'
                ,width	   : 200	
                //,flex      : 1
            }
        ],
        bbar     :
        {
            displayInfo : true,
            store       : storeGridHistoricoReclamaciones,
            xtype       : 'pagingtoolbar'
        }
    });

    
    
    Ext.getCmp('panelbusqueda').hide();
	Ext.getCmp('clausulasGridId').hide();
	
    panelClausula= Ext.create('Ext.form.Panel', {
        id: 'panelClausula',
        //width: 650,
        //url: _URL_INSERTA_CLAUSU,
        bodyPadding: 5,
        renderTo: Ext.getBody(),
        defaults 	:
		{
				style : 'margin:5px;'
		}
		,
        items: [
            	codigoAsegurado//,
            	,panelbusquedas
            	,gridDatos
    	       ],
	        buttonAlign:'center',
	        buttons: [{
    		text: 'Aceptar'
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
    	}
    	]
    });

	modificacionClausula = Ext.create('Ext.window.Window',
	        {
	            title        : 'Modo de Autorizaci&oacute;n'
	            ,modal       : true
	            ,buttonAlign : 'center'
	            ,closable : false
	            ,width		 : 650
	            ,minHeight 	 : 100 
	            ,maxheight      : 400
	            ,items       :
	            [
	             	panelClausula
	        ]
	        }).show();
	
	*/
});