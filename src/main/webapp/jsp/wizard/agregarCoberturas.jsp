<%@ include file="/taglibs.jsp"%>
<!-- LIBRARIES -->
<!-- SOURCE CODE -->
<script type="text/javascript">

   creaAltaCatalogoCoberturas = function(store) {
	
    // pre-define fields in the form
	var claveCatalogo = new Ext.form.TextField({
        fieldLabel: 'Clave',
        allowBlank: false,
        maxLength : '4',
   		maxLengthText : 'El codigo de cobertura maximo de 4 caracteres',
        name: 'claveCobertura',
        anchor: '90%' 
    });
    
	var descripcionCatalogo = new Ext.form.TextField({
        fieldLabel: 'Descripci&oacute;n',
        allowBlank: false,
        maxLength : '50',
   		maxLengthText : 'Cincuenta caracteres maximo',
        name: 'descripcionCobertura',
        anchor: '90%'  
    });  
    
    
	var dsTipoCobertura = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: _ACTION_COBERTURAS +'?combo='+'tipo'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaTipoCobertura',
            id: 'TipoCobertura'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsTipoCobertura.load();
  	
var comboTipoCobertura =new Ext.form.ComboBox({
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsTipoCobertura,
							allowBlank:false,
						    displayField:'value',
						    valueField: 'key',
					    	typeAhead: true,
					    	labelAlign: 'left',
					    	anchor: '90%', 
						    mode: 'local',
					    	triggerAction: 'all',
					    	selectOnFocus:true,
						    fieldLabel: 'Tipo',
					    	name:"tipoCobertura",
					    	selectFirst : function() {
         						this.focusAndSelect(this.store.data.first());
       						},
          					focusAndSelect : function(record) {
         						var index = typeof record === 'number' ? record : this.store.indexOf(record);
         						this.select(index, this.isExpanded());
         						this.onSelect(this.store.getAt(record), index, this.isExpanded());
       						},
          					onSelect : function(record, index, skipCollapse){
         						if(this.fireEvent('beforeselect', this, record, index) !== false){
           							this.setValue(record.data[this.valueField || this.displayField]);
           							if( !skipCollapse ) {
            							this.collapse();
           							}
           							this.lastSelectedIndex = index + 1;
           							this.fireEvent('select', this, record, index);
         						}
								var valor=record.get('key');
								Ext.getCmp('hidden-combo-tipo-cobertura').setValue(valor);
							}	    	
			});
			
    
	var dsRamoCobertura = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
			url: _ACTION_COBERTURAS +'?combo='+'ramo'
        }),
        reader: new Ext.data.JsonReader({
            root: 'listaRamoCobertura',
            id: 'RamoCobertura'
	        },[
           {name: 'key', type: 'string',mapping:'key'},
           {name: 'value', type: 'string',mapping:'value'}      
        ]),
		remoteSort: true
    });
  	dsRamoCobertura.load();
  	
var comboRamoCobertura =new Ext.form.ComboBox({
    	                    tpl: '<tpl for="."><div ext:qtip="{key}" class="x-combo-list-item">{value}</div></tpl>',
							store: dsRamoCobertura,
							allowBlank:false,
						    displayField:'value',
						    valueField: 'key',
					    	typeAhead: true,
					    	labelAlign: 'left',
					    	anchor: '90%',
						    mode: 'local',
					    	triggerAction: 'all',
					    	selectOnFocus:true,
						    fieldLabel: 'Ramo',
					    	name:"ramoCobertura",
					    	selectFirst : function() {
         						this.focusAndSelect(this.store.data.first());
       						},
          					focusAndSelect : function(record) {
         						var index = typeof record === 'number' ? record : this.store.indexOf(record);
         						this.select(index, this.isExpanded());
         						this.onSelect(this.store.getAt(record), index, this.isExpanded());
       						},
          					onSelect : function(record, index, skipCollapse){
         						if(this.fireEvent('beforeselect', this, record, index) !== false){
           							this.setValue(record.data[this.valueField || this.displayField]);
           							if( !skipCollapse ) {
            							this.collapse();
           							}
           							this.lastSelectedIndex = index + 1;
           							this.fireEvent('select', this, record, index);
         						}
								var valor=record.get('key');
								Ext.getCmp('hidden-combo-ramo-cobertura').setValue(valor);
							}
			});			
    
    var reinstalacion = new Ext.form.Checkbox({
        fieldLabel: 'Reinstalaci&oacute;n',
        id:'reinstalacion-check',
        name: 'reinstalacion',            
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
    });  
    
    var indiceInflacionario = new Ext.form.Checkbox({
        fieldLabel: '&Iacute;ndice inflacionario',
        id:'indice-inflacionario-check',
        name: 'indiceInflacionario',                
                onClick:function(){
				            	if(this.getValue()){				            	
				            		this.setRawValue("S");				            	
				            	}
				            }
          
    });  
        
    var formPanel = new Ext.form.FormPanel({
        baseCls: 'x-plain',
        labelWidth: 120,
        url:_ACTION_AGREGA_COBERTURAS,
        defaultType: 'textfield',

        items: [{xtype:'hidden',id:'hidden-combo-ramo-cobertura',name:'claveRamoCobertura'},
        		{xtype:'hidden',id:'hidden-combo-tipo-cobertura',name:'claveTipoCobertura'},
            claveCatalogo,
            descripcionCatalogo,
            comboTipoCobertura,
            comboRamoCobertura
            //reinstalacion,
            //indiceInflacionario
    	]
    });

    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'T&iacute;tulo',
        width: 400,
        height:195,
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
	 		        formPanel.form.submit({			      
			            waitTitle:'Cargando',
					    waitMsg:'Cargando',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Elemento ya existe');
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Status', 'Cobertura Agregada');
						    window.close();
						    store.load();
						    
						    
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Error', 'Favor de llenar datos requeridos');
				}             
	        }
        },{
            text: 'Limpiar',
            handler: function(){
            	formPanel.form.reset();            	
            	//reinstalacion.reset();
            	//Ext.getCmp('indice-inflacionario-check').reset();
            }
        },{
            text: 'Cancelar',
            handler: function(){
            			window.close();
            		 }
        }]
    });

	window.show();
	//dsComboCobertura.load();
};	
</script>