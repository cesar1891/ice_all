Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {

	unieco = Ext.create('Ext.form.Textfield',
	{
		label  : 'cdunieco'
		,value : _selCobParams.cdunieco
	});

    cobertura= Ext.create('Ext.form.ComboBox',
    	    {
    	        id:'cmbCobertura',
    	        name:'params.cobertura',
    	        fieldLabel: 'Cobertura',
    	        //store: storeCirculoHospitalario,
    	        queryMode:'local',
    	        displayField: 'value',
    	        valueField: 'key',
    	        allowBlank:false,
    	        blankText:'La cobertura es un dato requerido',
    	        editable:false,
    	        labelWidth : 120,
    	        width: 400,
    	        emptyText:'Seleccione una Cobertura ...'
    	    });
    
    subCobertura= Ext.create('Ext.form.ComboBox',
    	    {
    	        id:'cmbSubCobertura',
    	        name:'params.subCobertura',
    	        fieldLabel: 'Subcobertura',
    	        //store: storeCirculoHospitalario,
    	        queryMode:'local',
    	        displayField: 'value',
    	        valueField: 'key',
    	        allowBlank:false,
    	        blankText:'La subcobertura es un dato requerido',
    	        editable:false,
    	        labelWidth : 120,
    	        width: 400,
    	        emptyText:'Seleccione una Subcobertura ...'
    	    });
    
    panelSeleccionCobertura= Ext.create('Ext.form.Panel', {
        id: 'panelSeleccionCobertura',
        width: 650,
        //url: _URL_INSERTA_CLAUSU,
        bodyPadding: 5,
        renderTo: Ext.getBody(),
        items: [
                	cobertura,
                	subCobertura
    	       ],
	        buttonAlign:'center',
	        buttons: [{
    		text: 'Guardar'
    		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
    		,buttonAlign : 'center',
    		handler: function() {
    	    	if (panelSeleccionCobertura.form.isValid()) {
    	    		panelSeleccionCobertura.form.submit({
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
    						panelSeleccionCobertura.form.reset();
    						
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
    	        modificacionSeleccionCobertura.close();
    	    }
    	}
    	]
    });
    
    modificacionSeleccionCobertura = Ext.create('Ext.window.Window',
	        {
	            title        : 'Detalle Cobertura'
	            ,modal       : true
	            ,buttonAlign : 'center'
	            ,width		 : 450
	            ,height      : 145
	            ,items       :
	            [
	             	panelSeleccionCobertura
	        ]
	        }).show();
});