creaAltaCatalogoIncisos = function(dataStore) {
	
    // pre-define fields in the form
	var claveCatalogo = new Ext.form.TextField({
        fieldLabel: 'Clave*',
        allowBlank: false,
        blankText : 'Clave Requerida',
        maxLength : '6',
   		maxLengthText : 'Seis D\u00EDgitos M\u00E1ximo',
        name: 'claveCatalogo',
        anchor: '90%' 
    });
    
	var descripcionCatalogo = new Ext.form.TextField({
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        maxLength : '30',
   		maxLengthText : 'Treinta Caracteres M\u00E1ximo',
        blankText : 'Descripci\u00F3n Requerida',
        name: 'descripcionCatalogo',
        anchor: '90%'  
    });  
    
    var subIncisos = new Ext.form.Checkbox({
        fieldLabel: 'Subincisos?',
        name: 'subIncisos',
        checked:false,                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
    });  
        
    var formPanel = new Ext.form.FormPanel({
  
        
        baseCls: 'x-plain',
        labelWidth: 75,
        url:'incisos/agregarInciso.action',
        defaultType: 'textfield',

        items: [
            claveCatalogo,
            descripcionCatalogo,
            subIncisos
    	]
    });

    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Agregar Nuevo Riesgo',
        width: 300,
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
						    Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).messageResult);
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Status', Ext.util.JSON.decode(action.response.responseText).messageResult);
						    window.close();
						    dataStore.load();
						}
			        });
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
				}             
	        }
        },{
            text: 'Limpiar',
            handler: function(){formPanel.form.reset();}
        },{
            text: 'Cancelar',
            handler: function(){window.close();}
        }]
    });

	window.show();
};	