creaCatalogoTipoObjeto= function(dsCatalogoObjeto) {

	var descripcion = new Ext.form.TextField({
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        maxLength : 120,
        blankText : 'Descripci\u00F3n requerida.',
        name: 'descripcionObjeto',
        anchor: '90%'  
    });  
    
        
    var formPanel = new Ext.form.FormPanel({
  
        baseCls: 'x-plain',
        labelWidth: 75,
        url:'tipoObjeto/AgregaTipoObjetoCatalogo.action',
        defaultType: 'textfield',
        //collapsed : false,

        items: [
            descripcion
    	]
    });

    // define window and show it in desktop
    var objetosWindow = new Ext.Window({
        title: 'Cat\u00E1logo De Tipo De Objetos',
        width: 300,
        height:110,
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
						    Ext.MessageBox.alert('Status', 'Tipo de objeto no agregado al catalogo');
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Tipo de objeto agregado al catalogo');
						    objetosWindow.close();
						    dsCatalogoObjeto.load();
						    
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){
            				objetosWindow.close();
            		 }
        }]
    });

	objetosWindow.show();
};