   agregarValoresGrid = function(store) {

    // pre-define fields in the form
    
    var valorClave= new Ext.form.TextField({
                name:'valorClave',
                fieldLabel: 'Valor Clave*',
                allowBlank: false,
                width: 80
            	});
            
	var ValorDescripcion = new Ext.form.TextField({
        		name: 'valorDescripcion',
        		fieldLabel: 'Valor Descripci\u00F3n*',
        		allowBlank: false,
        		width: 160  
    			});   
	
    
        
    var formPanel = new Ext.form.FormPanel({
  
        baseCls: 'x-plain',
        labelWidth: 115,
        url:'atributosVariables/CargaManual.action',
        defaultType: 'textfield',
        //collapsed : false,

        items: [
            new Ext.form.TextField({
                name:'valorClave',
                fieldLabel: 'Valor Clave*',
                allowBlank: false,
                blankText : 'Valor Clave requerido',
                width: 130
            	}),
            new Ext.form.TextField({
        		name: 'valorDescripcion',
        		fieldLabel: 'Valor Descripci\u00F3n*',
        		allowBlank: false,
        		blankText : 'Valor Descripci\u00F3n requerido',
        		width: 130  
    			})
    	]
    });

    // define window and show it in desktop
    var agregarValoresGridWindow = new Ext.Window({
        title: 'Carga Manual',
        width: 350,
        height:150,
        //minWidth: 300,
        //minHeight: 150,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        modal : true,
  
        items: formPanel,
        

		buttons: [{
            text: 'Guardar', 
            handler: function() {
                // check form value 
                if (formPanel.form.isValid()) {
	 		        formPanel.form.submit({			      
			            waitTitle:'Espere',
			            waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Elemento ya existe');
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Status', 'Elemento agregado');
						    agregarValoresGridWindow.close();
						    store.load();
						    //createGrid();
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){
            	agregarValoresGridWindow.close();
            }
        }]
    });

	agregarValoresGridWindow.show();
};