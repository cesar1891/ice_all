DeleteDemouser = function(store,selectedId, sel,listaValoresForm) {

    // get the selected items
   Ext.MessageBox.confirm('Mensaje','Esta seguro que desea eliminar este valor?', function(btn) {
           if(btn == 'yes'){ 
           
			     
			     listaValoresForm.form.submit({	
			     url:'atributosVariables/EliminaCargaManual.action'+'?valorClave='+selectedId,
			     waitTitle:'Espere',
			     waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Error al eliminar');
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Valor eliminado');						    
						    store.load();
						    
						}
        	 }); 
        	 
        	}
       } 
      );
};