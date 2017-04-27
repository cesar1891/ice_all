eliminaAtributoVariableObjeto = function(store,selectedId,objetosForm, rec) {
	objetosForm.form.load({	
		url:'tipoObjeto/EliminaDatoVariableObjeto.action',
		method: 'POST',
		params: {
			descripcion: selectedId,
			codigoObjeto: rec.get('claveObjeto'),
			claveCampo: rec.get('claveCampo')
		},
		waitTitle:'Espere',
		waitMsg:'Procesando...',
		failure: function(form, action) {
			Ext.MessageBox.alert('Status', 'Elemento eliminado');
			store.load();
		},
		success: function(form, action) {
			Ext.MessageBox.alert('Status', 'Elemento eliminado');						    
		}
	});
};