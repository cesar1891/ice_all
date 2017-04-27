creaAtributosVariables = function(dsCatalogoAtributos) {  

    // pre-define fields in the form
	var descripcion = new Ext.form.TextField({
        fieldLabel: 'Descripci\u00F3n*',
        allowBlank: false,
        blankText : 'Descripci\u00F3n requerida.',
        name: 'descripcionAtributoVariable',
        anchor: '90%' 
    });
    
    var functionRadioAlfa= new Ext.form.Radio({
        name:'formato',
        fieldLabel: 'Formato*',
        //labelSeparator : "",
        boxLabel: 'Alfanum\u00E9rico',
        checked:true,
  	  	onClick: function(){
  	  				Ext.getCmp('hidden-forma-atributos-variables-persona-validacion-formato').show();
  	  				Ext.getCmp('minimo-clave-atributo').setVisible(true);  
  	  				Ext.getCmp('maximo-clave-atributo').setVisible(true);
       				if(this.getValue()){					            	
            			this.setRawValue("A");
				 	}
			    }
        });   
            
    var functionRadioNumeric= new Ext.form.Radio({
        name:'formato',
        labelSeparator : "",
        boxLabel: 'Num\u00E9rico',
        hideLabel:true,
  	  	onClick:function(){
  	  				Ext.getCmp('hidden-forma-atributos-variables-persona-validacion-formato').show();
  	  				Ext.getCmp('minimo-clave-atributo').setVisible(true);  
  	  				Ext.getCmp('maximo-clave-atributo').setVisible(true);
        			if(this.getValue()){					            	            			
            			this.setRawValue("N");
				    }
				}
        });  
            
    var functionRadioPercent= new Ext.form.Radio({
        name:'formato',
        labelSeparator : "",
        boxLabel: 'Porcentaje',
        //hideLabel:true,
  	  	onClick: function(){         
  	  				Ext.getCmp('hidden-forma-atributos-variables-persona-validacion-formato').hide();
  	  				Ext.getCmp('minimo-clave-atributo').setVisible(false);  
  	  				Ext.getCmp('maximo-clave-atributo').setVisible(false);	 
        			if(this.getValue()){               			
						this.setRawValue("P");
				   	}
			    }
        });    
            
    var functionRadioDate= new Ext.form.Radio({
        name: 'formato',
		boxLabel: 'Fecha',
		hideLabel:true,
  	  	onClick: function(){ 	  		
  	  				Ext.getCmp('hidden-forma-atributos-variables-persona-validacion-formato').hide();
  	  				Ext.getCmp('minimo-clave-atributo').setVisible(false);  
  	  				Ext.getCmp('maximo-clave-atributo').setVisible(false);
        			if(this.getValue()){         
				        this.setRawValue("F");
				        }
			        }
			       
    });
    
    var limiteMaximo = new Ext.form.NumberField({
        fieldLabel: 'M\u00E1ximo',
        //allowBlank: false,
        allowDecimals : false,
		allowNegative : false,
		minValue: 1,
		maxLength: 2, 
		maxLengthText:'Valor m\u00E1ximo es de dos d\u00EDgitos',
		id:'maximo-clave-atributo',
        name: 'limiteMaximo',
        width:40 
    });  
    
    var limiteMinimo = new Ext.form.NumberField({
        fieldLabel: 'M\u00EDnimo',
        //allowBlank: false,
	    allowDecimals : false,
	    allowNegative : false,
	    minValue: 1,
	    maxLength: 2,
	    maxLengthText:'Valor m\u00E1ximo es de dos d\u00EDgitos',
        name: 'limiteMinimo',
        id:'minimo-clave-atributo',
        width:40 
    });
        
    var formPanel = new Ext.form.FormPanel({
  
        baseCls: 'x-plain',
        labelWidth: 70,
        url:'rol/AgregaAtributoVariableCatalogo.action',       
        items: [
            	descripcion,
            	{
        			layout:'column',
        			border: false,
        			baseCls: 'x-plain',
            			items:[{
				    			columnWidth:.65,
                				layout: 'form',
                				border: false,
                				baseCls: 'x-plain',
				            	items: [functionRadioAlfa]
        					},{
               					columnWidth:.35,
			               		layout: 'form',
			               		border: false,
			               		baseCls: 'x-plain',
               					items: [functionRadioNumeric]
				   			}]
        		
		    	},{
				   	layout: 'column',
				   	border: false,
				   	baseCls: 'x-plain',
				   	labelAlign:"right",
                	items: [{
                				columnWidth:.65,
				                layout:'form',
				                border: false,
				                baseCls: 'x-plain',
				                items:[functionRadioPercent]
							},{
                				columnWidth:.35,
                				layout:'form',
                				border: false,
                				baseCls: 'x-plain',
                				items:[functionRadioDate]
				         	}]
				},{
					layout:'form',
					id:'hidden-forma-atributos-variables-persona-validacion-formato',
					border:false,
					baseCls: 'x-plain',
					items:[limiteMinimo, limiteMaximo]          
				}]
    });

    // define window and show it in desktop
    var atributosWindow = new Ext.Window({
        title: 'Atributos Variables De Persona',
        width: 350,
        height:200,
        minWidth: 350,
        minHeight: 200,
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
                	
                  if(validaMaxMinClaveAtributo()){
                  	
	 		        formPanel.form.submit({			      
			            waitTitle:'Espere',
					    waitMsg:'Procesando...',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Atributo variable no agregado');
						},
						success: function(form, action) {
						    //Ext.MessageBox.alert('Status', 'Atributo Variable agregado');
						    atributosWindow.close();
						    dsCatalogoAtributos.load();
						}
			        });
                  }
                  
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar campos requeridos.');
				}             
	        }
        },{
            text: 'Cancelar',
            handler: function(){atributosWindow.close();}
        }]
    });

	atributosWindow.show();
	

function validaMaxMinClaveAtributo(){      
    
	 if( Ext.getCmp('minimo-clave-atributo').isVisible() &&  Ext.getCmp('maximo-clave-atributo').isVisible()  ){
	 
    	if(Ext.getCmp('minimo-clave-atributo').getValue()!= '' && Ext.getCmp('maximo-clave-atributo').getValue()!= ''){
	
    		if(Ext.getCmp('minimo-clave-atributo').getValue() <=  Ext.getCmp('maximo-clave-atributo').getValue()){
    			return true
    		}
    		if(Ext.getCmp('minimo-clave-atributo').getValue()> Ext.getCmp('maximo-clave-atributo').getValue()){
    			Ext.MessageBox.alert('Error', 'Valor maximo es menor que minimo');
    			return false
    		}    		
    	}
    	return true;
	 }else{	
	 	 Ext.getCmp('minimo-clave-atributo').reset();
	 	 Ext.getCmp('maximo-clave-atributo').reset();
    	return true
	 }	  	
 }	    
	
};