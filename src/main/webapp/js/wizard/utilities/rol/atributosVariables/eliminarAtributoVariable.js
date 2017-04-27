eliminaAtributoVariableRol = function(store,row,rolesForm) {
	if(row!=null){
		var rec = store.getAt(row);
		var codigoAtributoRol= rec.get('codigoAtributoVariable');
		//alert("codigoAtributo"+codigoAtributoRol);
	}
	
	rolesForm.form.load({	
		url:'rol/EliminaAtributoVariableRol.action'+'?codigoAtributoVariable='+codigoAtributoRol,
		waitTitle:'Espere',
		waitMsg:'Procesando...',
		success: function(form, action) {
			Ext.MessageBox.alert('Status', 'Elemento eliminado');
			store.load();    
		},
		failure: function(form, action) {
			//TODO: Revisar por que siempre entra a este metodo aunque success es true 
			var exito = Ext.util.JSON.decode(action.response.responseText).success;
			if(exito){
				Ext.MessageBox.alert('Status', 'Elemento eliminado');
			}else{
				Ext.MessageBox.alert('Status', 'No se pudo eliminar el Atributo Variable de Rol');
			}
				store.load();
		}
 	});
};