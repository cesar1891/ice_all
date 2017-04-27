Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {


	Ext.define('modeloRechazos',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'key'},
                 {type:'string',    name:'value'}
				]
    });
	
	 var storeRechazos = Ext.create('Ext.data.JsonStore', {
	    	model:'modeloRechazos',
	        proxy: {
	            type: 'ajax',
	            url: _URL_ListaRechazos,
	            reader: {
	                type: 'json',
	                root: 'loadList'
	            }
	        }
	    });
	 storeRechazos.load();

	 Ext.define('modeloIncisosRechazos',{
		 extend: 'Ext.data.Model',
		 fields: [{type:'string',    name:'key'},
		          {type:'string',    name:'value'}
		          ]
	 });
	 
	 var storeIncisosRechazos = Ext.create('Ext.data.JsonStore', {
		 model:'modeloIncisosRechazos',
		 proxy: {
			 type: 'ajax',
			 url: _URL_ListaIncisosRechazos,
			 reader: {
				 type: 'json',
				 root: 'loadList'
			 }
		 }
	 });
	
	
	
    motivoRechazo= Ext.create('Ext.form.ComboBox',
    	    {
    	        id:'motivoRechazo',
    	        name:'smap1.cdmotivo',
    	        fieldLabel: 'Motivo',
    	        store: storeRechazos,
    	        queryMode:'local',
    	        displayField: 'value',
    	        valueField: 'key',
    	        allowBlank:false,
    	        blankText:'El motivo es un dato requerido',
    	        editable:false,
    	        labelWidth : 150,
    	        width: 600,
    	        emptyText:'Seleccione ...',
    	        listeners: {
    	        	select: function(combo, records, eOpts){
    	        		//Ext.getCmp('incisosRechazo').setValue('');
    	        		panelRechazarReclamaciones.down('[name=smap1.incisosRechazo]').setValue('');
    	        		panelRechazarReclamaciones.down('[name=smap1.comments]').setValue('');
    	        		storeIncisosRechazos.removeAll();
    	        		storeIncisosRechazos.load({
    	        			 params: {
    	        				'params.pv_cdmotivo_i' : records[0].get('key'),
    	        				'params.pv_cdramo_i'   : _cdramo,
    	        				'params.pv_cdtipsit_i' : _cdtipsit
    	        			 }
    	        		 });
    	        	}
    	        }
    	    });

    
    textoRechazo = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Descripci&oacute;n modificado'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
			,allowBlank: false
			,blankText:'La descripci&oacute;n es un dato requerido'
        });
    
    incisosRechazo= Ext.create('Ext.form.ComboBox',
    		{
    	id:'incisosRechazo',
    	name:'smap1.incisosRechazo',
    	fieldLabel: 'Incisos Rechazo',
    	store: storeIncisosRechazos,
    	queryMode:'local',
    	displayField: 'value',
    	valueField: 'key',
//    	allowBlank:false,
    	blankText:'El motivo es un dato requerido',
    	editable:false,
    	labelWidth : 150,
    	width: 600,
    	emptyText:'Seleccione ...',
    		listeners: {
	        	select: function(combo, records, eOpts){
	        		
	        		textoRechazo.setValue(records[0].get('value'));
	        	}
	        }
    });
 
    
    panelRechazarReclamaciones= Ext.create('Ext.form.Panel', {
        title: 'Rechazar Reclamaci&oacute;n',
    	id: 'panelRechazarReclamaciones',
        width: 650,
        url: _URL_ActualizaStatusTramite,
        bodyPadding: 5,
        renderTo: 'maindiv',
        items: [
            	motivoRechazo,incisosRechazo,textoRechazo,{
                    xtype       : 'radiogroup'
                    ,fieldLabel : 'Mostrar al agente'
                    ,columns    : 2
                    ,width      : 250
                    ,style      : 'margin:5px;'
                    ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                    ,items      :
                    [
                        {
                            boxLabel    : 'Si'
                            ,itemId     : 'SWAGENTE2'
                            ,name       : 'SWAGENTE2'
                            ,inputValue : 'S'
                            ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                        }
                        ,{
                            boxLabel    : 'No'
                            ,name       : 'SWAGENTE2'
                            ,inputValue : 'N'
                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                        }
                    ]
                }
    	        ],
	        buttonAlign:'center',
	        buttons: [{
    		text: 'Rechazar'
    		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
    		,buttonAlign : 'center',
    		handler: function() {
    	    	if (panelRechazarReclamaciones.form.isValid()) {
    	    		panelRechazarReclamaciones.form.submit({
    		        	waitMsg:'Procesando...',			
    		        	params: {
    		        		'smap1.ntramite' : _nmTramite, 
    		        		'smap1.status'   : 4
    		        		,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
    		        	},
    		        	failure: function(form, action) {
    		        		Ext.Msg.show({
    	   	                    title: 'ERROR',
    	   	                    msg: 'Error al Rechazar.',
    	   	                    buttons: Ext.Msg.OK,
    	   	                    icon: Ext.Msg.ERROR
    	   	                });
    					},
    					success: function(form, action) {
    						
    						var respuesta = Ext.decode(action.response.responseText);
    						if(respuesta.success==true){
    							Ext.Msg.show({
        	   	                    title: '&Eacute;XITO',
        	   	                    msg: "Se ha rechazado correctamente.",
        	   	                    buttons: Ext.Msg.OK
        	   	                });
        						loadMcdinStore();
        						windowLoader.close();
        						
        						Ext.Ajax.request({
        							url: _UrlGeneraCartaRechazo,
        							params: {
        								'paramsO.pv_cdunieco_i' : _cdunieco,
        					    		'paramsO.pv_cdramo_i'   : _cdramo,
        					    		'paramsO.pv_estado_i'   : _estado,
        					    		'paramsO.pv_nmpoliza_i' : _nmpoliza,
        					    		'paramsO.pv_nmsuplem_i' : _nmsuplem,
        					    		'paramsO.pv_nmsolici_i' : _nmsolici,
        					    		'paramsO.pv_tipmov_i'   : _TIPOPAGO,
        								'paramsO.pv_ntramite_i' : _nmTramite,
        								'paramsO.tipopago' : _TIPOPAGO
        							},
        							success: function(response, opt) {
        								var jsonRes=Ext.decode(response.responseText);

        								if(jsonRes.success == true){
        									var numRand=Math.floor((Math.random()*100000)+1);
        						        	debug('numRand a: ',numRand);
        						        	var windowVerDocu=Ext.create('Ext.window.Window',
        						        	{
        						        		title          : 'Carta de Rechazo del Siniestro'
        						        		,width         : 700
        						        		,height        : 500
        						        		,collapsible   : true
        						        		,titleCollapse : true
        						        		,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
        						        		                 +'src="'+panDocUrlViewDoc+'?subfolder=' + _nmTramite + '&filename=' + nombreReporteRechazo +'">'
        						        		                 +'</iframe>'
        						        		,listeners     :
        						        		{
        						        			resize : function(win,width,height,opt){
        						                        debug(width,height);
        						                        $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
        						                    }
        						        		}
        						        	}).show();
        						        	windowVerDocu.center();
        								}else {
        									mensajeError('Error al generar la carta de rechazo.');
        								}
        							},
        							failure: function(){
        								mensajeError('Error al generar la carta de rechazo.');
        							}
        						});
        						
    						}else {
    							Ext.Msg.show({
        	   	                    title: 'ERROR',
        	   	                    msg: 'Error al Rechazar.',
        	   	                    buttons: Ext.Msg.OK,
        	   	                    icon: Ext.Msg.ERROR
        	   	                });
    						}
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
    	        windowLoader.close();
    	        //asginarUsuario.show();
    	    }
    	}
    	]
    });
	
});