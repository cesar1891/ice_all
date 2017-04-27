
ClausulasIE = function(dataStore, selectedId) {

   var clave= new Ext.form.TextField({
                    fieldLabel: 'clave',
                    name: 'eclave',
                    anchor:'40%'   
   });
   var descripcion= new Ext.form.TextField({
                    fieldLabel: 'descripcion',
                    name: 'edescripcion',
                    anchor:'98%'   
   });
   var htmledit= new Ext.form.HtmlEditor({
    		id:'bio',
            fieldLabel:'Biography',
            height:200,
            anchor:'98%'
   });
    var formPanel = new Ext.FormPanel({
        labelAlign: 'top',
        frame:true,
        
        bodyStyle:'padding:5px 5px 0',
        width: 600,
        url:'definicion/ABCClausulas.action',
        items: [
        	clave,
        	descripcion,
        	htmledit
        ]
    });
 	if(selectedId!=null){
		 formPanel.form.load({url:'definicion/editClausulas?action=loadData&id='+selectedId, waitMsg:'Loading'});  
    }
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Alta al catalogo de Clausulas',
        width: 700,
        height:600,
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
			            waitMsg:'Salvando datos...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Error Message', action.result.errorInfo);
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Confirm', action.result.info);
						    window.hide();
						    //dataStore.load({params:{start:0, limit:10}});
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Errors', 'Favor de llenar todos los datos requeridos');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){window.hide();}
        }]
    });

    window.show();
};
