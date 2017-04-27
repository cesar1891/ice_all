creaCatalogoDeRoles= function(dsCatalogoRol) {  

    // pre-define fields in the form
	/*var clave = new Ext.form.TextField({
        fieldLabel: 'Clave',
        allowBlank: false,
        name: 'clave',
        anchor: '90%' 
    });
    */
	var descripcion = new Ext.form.TextField({
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        blankText : 'Descripci\u00F3n requerida.',
        name: 'descripcion',
        anchor: '90%'  
    });  
    
        
    var formPanel = new Ext.form.FormPanel({
  
        baseCls: 'x-plain',
        labelWidth: 75,
        url:'rol/AgregaRolCatalogo.action',
        defaultType: 'textfield',
        //collapsed : false,

        items: [
            //clave,
            descripcion
    	]
    });

    // define window and show it in desktop
    var rolWindow = new Ext.Window({
        title: 'Cat\u00E1logo De Roles',
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
						    Ext.MessageBox.alert('Status', 'Rol no agregado al cat\u00E1logo');
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Rol agregado al cat\u00E1logo');
						    rolWindow.close();
						    dsCatalogoRol.load();
						    
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){
            				rolWindow.close();
            		 }
        }]
    });

	rolWindow.show();
};