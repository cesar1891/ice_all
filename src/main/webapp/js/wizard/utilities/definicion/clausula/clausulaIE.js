ClausulaIE = function(dataStore, selectedId) {

   var clave= new Ext.form.TextField({
				    maxLengthText:'maximo 4 caracteres',
				    maxLength:'4',
				    maxLengthText: 'Cuatro caracteres M\u00E1ximo.',
                    fieldLabel: 'Clave*',
                    name: 'clave',
                    allowBlank:false,
					blankText : 'Clave de la cl\u00E1usula requerida.',
                    anchor:'40%'   
   });
   var descripcion= new Ext.form.TextField({
                    fieldLabel: 'Descripci\u00F3n*',
                    name: 'clausulaDescripcion',
                    maxLength:'60',
                    allowBlank:false,
					blankText : 'Descripci\u00F3n de la cl\u00E1usula requerida.',
                    anchor:'98%'   
   });
   var htmledit= new Ext.form.HtmlEditor({
    		id:'bio',
            fieldLabel:'Texto',
            name:'linea',
            height:100,
            anchor:'98%'
   });
    var formPanel = new Ext.FormPanel({
    	id:'another-id',
        labelAlign: 'top',
        frame:true,
        reader: new Ext.data.JsonReader({			//el edit aun no esta probado
            root: ''
        }, ['clave','descripcion', 'linea']
        ),
        
        bodyStyle:'padding:5px 5px 0',
        width: 600,
        url:'definicion/AgregarClausula.action',
        items: [{xtype:'hidden', id:'hidden-bandera-editar-agregar-clausula',name:'banderaEditar', value:'1'},
        	clave,
        	descripcion,
        	htmledit
        ]
    });
 	if(selectedId!=null){
 	
		 Ext.getCmp('another-id').getForm().loadRecord(selectedId);  
    }
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Alta al Cat\u00E1logo de Cl\u00E1usulas',
        width: 600,
        height:530,
        minWidth: 300,
        minHeight: 150,
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
	 		        formPanel.form.submit({			      
			            waitTitle:'Espere',
			            waitMsg:'Procesando...',
			            failure: function(form, action) {
						    //Ext.MessageBox.alert('Error', "Clausula no agregada");
			            	Ext.MessageBox.alert( 'Error', Ext.util.JSON.decode(action.response.responseText).mensaje );
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Status', "Clausula agregada correctamente");
							Ext.MessageBox.alert( 'Status', Ext.util.JSON.decode(action.response.responseText).mensaje );
						    window.close();
						    dataStore.load({params:{start:0, limit:10}});
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Existen errores al agregar una Clausula, por favor verifiquelos e intente de nuevo!');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){window.close();}
        }]
    });

    window.show();
};