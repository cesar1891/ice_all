Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {
	
	asignarUsuario= Ext.create('Ext.form.ComboBox',
    {
        id:'usuarioAsignacion',
        name:'params.usuarioAsignacion',
        fieldLabel: 'Usuario',
        //store: storeCirculoHospitalario,
        queryMode:'local',
        displayField: 'value',
        valueField: 'key',
        allowBlank:false,
        blankText:'El usuario es un dato requerido',
        editable:false,
        labelWidth : 100,
        width: 400,
        emptyText:'Seleccione un usuario...'
    });  
    
	
    panelAsignarUsuario= Ext.create('Ext.form.Panel', {
        id: 'panelAsignarUsuario',
        width: 450,
        //url: _URL_INSERTA_CLAUSU,
        bodyPadding: 5,
        renderTo: Ext.getBody(),
        items: [
            		asignarUsuario
               ],
	        buttonAlign:'center',
	        buttons: [{
    		text: 'Aceptar'
    		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
    		,buttonAlign : 'center',
    		handler: function() {
    	    	if (panelAsignarUsuario.form.isValid()) {
    	    		panelAsignarUsuario.form.submit({
    		        	waitMsg:'Procesando...',			        	
    		        	failure: function(form, action) {
    		        		Ext.Msg.show({
    	   	                    title: 'ERROR',
    	   	                    msg: action.result.errorMessage,
    	   	                    buttons: Ext.Msg.OK,
    	   	                    icon: Ext.Msg.ERROR
    	   	                });
    					},
    					success: function(form, action) {
    						Ext.Msg.show({
    	   	                    title: '&Eacute;XITO',
    	   	                    msg: "La cl&aacute;usula se guardo correctamente",
    	   	                    buttons: Ext.Msg.OK
    	   	                });
    						panelAsignarUsuario.form.reset();
    						
    					}
    				});
    			} else {
    				Ext.Msg.show({
    	                   title: 'Aviso',
    	                   msg: 'Complete la informaci&oacute;n requerida',
    	                   buttons: Ext.Msg.OK,
    	                   icon: Ext.Msg.WARNING
    	               });
    			}
    		}
    	},{
    	    text: 'Cancelar',
    	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
    	    buttonAlign : 'center',
    	    handler: function() {
    	    	asginarUsuario.close();
    	    }
    	}
    	]
    });
    
    
    
	asginarUsuario = Ext.create('Ext.window.Window',
    {
        title        : 'Asignar Reclamaci&oacute;n'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width		 : 450
        ,height      : 115
        ,items       :
        [
         	panelAsignarUsuario
    ]
    }).show();

});