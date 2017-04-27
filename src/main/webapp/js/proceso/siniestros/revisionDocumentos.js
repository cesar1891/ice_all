
Ext.onReady(function() {

	Ext.define('modeloDocumentos',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'id'},
                 {type:'boolean',   name:'listo'},
                 {type:'string',    name:'nombre'},
                 {type:'string',    name:'obligatorio'}
				]
    });
    
    var storeDocumentos = Ext.create('Ext.data.JsonStore', {
    	model:'modeloDocumentos',
        proxy: {
            type: 'ajax',
            url: _URL_ListaDocumentos,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    storeDocumentos.load({
    	params: {
    		'params.pv_cdtippag_i' : _tipoPago,
    		'params.pv_cdtipate_i' : _tipoAtencion,
    		'params.pv_nmtramite_i' : _nmTramite
    	}
    });
    
    var gridDocumentos = Ext.create('Ext.grid.Panel',{
		title: 'Checklist para los Documentos Entregados del Siniestro',
		renderTo : 'maindiv',
		autoScroll: true,
		store:  storeDocumentos,
		collapsible: true,
		titleCollapse: true,
		style: 'margin:0px',
		height: 250,
		columns       :[ { header     : 'Id' , dataIndex : 'id', hidden: true},
		                 { header     : 'Entregado' ,dataIndex : 'listo' ,flex: 1, xtype: 'checkcolumn', menuDisabled : true },
		                 { header     : 'Nombre del Documento' ,dataIndex : 'nombre' ,flex: 1 },
		                 { header     : 'Obligatorio' ,dataIndex : 'obligatorio' ,flex: 1 }
		 ],
		buttonAlign: 'center', 
		buttons:[{
				text: 'Guardar Estatus de Entrega',
				icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				disabled: _tieneCR,
				handler: function() {
					var saveList = [];
					storeDocumentos.getUpdatedRecords().forEach(function(record,index,arr){
						//para convertir boolean a string ya que el server no lo convierte bien
						record.data['listo'] = record.get('listo')==true?'I':'D';
						saveList.push(record.data);
					});
					
					if(saveList.length == 0){
						mensajeWarning('No se modificaron datos.');
						return;
					}
					
					gridDocumentos.setLoading(true);
					Ext.Ajax.request({
						url: _GuardaDocumentos,
						jsonData: {
							params: {
					    		'pv_ntramite_i' : _nmTramite,
					    		'pv_cdtippag_i' : _tipoPago,
					    		'pv_cdtipate_i' : _tipoAtencion
					    	},
							saveList : 	saveList
						},
						success: function(response) {
							var res = Ext.decode(response.responseText);
							gridDocumentos.setLoading(false);
							
							if(res.success){
								mensajeCorrecto('Aviso','Se ha guardado con exito.');
								storeDocumentos.reload();
								windowLoader.close();
							}else{
								mensajeError('No se pudo guardar.');
							}
							
						},
						failure: function(){
							gridDocumentos.setLoading(false);
							mensajeError('No se pudo guardar.');
						}
					});
				}
		}]
	});
	
});
