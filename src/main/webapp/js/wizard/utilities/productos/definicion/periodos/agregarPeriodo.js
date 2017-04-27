
  Ext.apply(Ext.form.VTypes, {
  daterange: function(val, field) {
    var date = field.parseDate(val);
    
    // We need to force the picker to update values to recaluate the disabled dates display
    var dispUpd = function(picker) {
      var ad = picker.activeDate;
      picker.activeDate = null;
      picker.update(ad);
    };
    
    if (field.startDateField) {
      var sd = Ext.getCmp(field.startDateField);
      sd.maxValue = date;
      if (sd.menu && sd.menu.picker) {
        sd.menu.picker.maxDate = date;
        dispUpd(sd.menu.picker);
      }
    } else if (field.endDateField) {
      var ed = Ext.getCmp(field.endDateField);
      ed.minValue = date;
      if (ed.menu && ed.menu.picker) {
        ed.menu.picker.minDate = date;
        dispUpd(ed.menu.picker);
      }
    }
    /* Always return true since we're only using this vtype
     * to set the min/max allowed values (these are tested
     * for after the vtype test)
     */
    return true;
  },
  
  password: function(val, field) {
    if (field.initialPassField) {
      var pwd = Ext.getCmp(field.initialPassField);
      return (val == pwd.getValue());
    }
    return true;
  },
  
  passwordText: 'Passwords do not match'
});
 
CreateDemouser = function(dataStore) {

   
    var formPanel = new Ext.form.FormPanel({
  
        baseCls: 'x-plain',
        
        url:'definicion/ABCPeriodos.action',
        defaultType: 'datefield',

        items: [{
                xtype:'datefield',
                fieldLabel: 'Fecha Inicial',
		        name: 'startdt',
		        id: 'startdt',
		        vtype: 'daterange',
		        allowBlank:false,
                endDateField: 'enddt'
		    
        	},{
        		xtype:'datefield',
                allowBlank:false,
                fieldLabel: 'Fecha Final',
		        name: 'enddt',
		        id: 'enddt',
        		vtype: 'daterange',
		        startDateField: 'startdt'
            
        	}
		]
    });
 
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Agregar Periodos de valides del producto',
        width: 400,
        height:250,
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
