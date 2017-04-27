PeriodoIE = function(dataStore, selectedId) {

  
    var formPanel = new Ext.form.FormPanel({
  		id:'load-record',
        baseCls: 'x-plain',
        
        url:'definicion/AsociarPeriodo.action',
        defaultType: 'datefield',
		reader: new Ext.data.JsonReader({		//el edit aun no esta probado
            root: '' 
        }, ['inicio','fin']
        ),
        items: [{xtype:'hidden',id:'id-hidden-codigo-periodo-ventana',name:'codigoPeriodo'},{
                xtype:'datefield',
                fieldLabel: 'Fecha Inicial*',
		        name: 'inicio',
		        id: 'startdt',
		        vtype: 'daterange',
		        allowBlank:false,
		        blankText : 'Fecha de inicio del per\u00EDodo requerida.',
		        endDateField: 'enddt',
                format:'d/m/Y'
		    
        	},{
        		xtype:'datefield',
                allowBlank:false,
                blankText : 'Fecha de final del per\u00EDodo requerida.',
                fieldLabel: 'Fecha Final*',
		        name: 'fin',
		        id: 'enddt',
        		vtype: 'daterange',
		        startDateField: 'startdt',
		        format:'d/m/Y'
            
        	}
		]
    });
    if(selectedId!=null){
    	//alert("El santo y blueDaemon"+selectedId);
	   
		 Ext.getCmp('load-record').getForm().loadRecord(selectedId);  
    }
 
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Agregar Per\u00EDodos de Validez del Producto',
        width: 230,
        height:150,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: formPanel,
		modal:true,
        buttons: [{
            text: 'Guardar', 
            handler: function() {
                // check form value 
                if (formPanel.form.isValid()) {
                //alert(Ext.getCmp('id-hidden-codigo-periodo-ventana').getValue());
	 		        formPanel.form.submit({			      
			            waitTitle:'Espere',
			            waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Error al Guardar el Periodo', "La fecha inicial de un periodo no puede ser anterior <br />a la fecha final del ultimo periodo agregado");
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Confirm', action.result.info);
						   
						    window.close();
						    dataStore.load({params:{start:0, limit:10}});
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Errors', 'Favor de llenar los datos requeridos');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){window.close();}
        }]
    });

    window.show();
};
