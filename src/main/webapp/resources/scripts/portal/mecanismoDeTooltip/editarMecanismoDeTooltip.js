
/**
 	EDITAR REGISTROS 
*/

function editar(record) {
    var mtooltip_reg = new Ext.data.JsonReader({
						root: 'MEstructuraList',
						totalProperty: 'totalCount',
						successProperty: '@success'
				      },[
				        {name: 'langCode', type: 'string', mapping: 'langCode'},
				        {name: 'Etiqueta',  type: 'string',  mapping:'nbEtiqueta'},
				        {name: 'nbEtiqueta',  type: 'string',  mapping:'nbEtiqueta'},
				        {name: 'dsTitulo',  type: 'string',  mapping:'dsTitulo'},
				        {name: 'idTitulo',  type: 'string',  mapping:'idTitulo'},
				        {name: 'idObjeto',  type: 'string',  mapping:'idObjeto'},
				        {name: 'nbObjeto',  type: 'string',  mapping:'nbObjeto'},
				        {name: 'Objeto',  type: 'string',  mapping:'nbObjeto'},
				        {name: 'fgTipoObjeto',  type: 'string',  mapping:'fgTipoObjeto'},
				        {name: 'Tooltip',  type: 'string',  mapping:'dsTooltip'},
				        {name: 'dsTooltip',  type: 'string',  mapping:'dsTooltip'},
				        {name: 'fgAyuda',  type: 'string',  mapping:'fgAyuda'},
				        {name: 'dsAyuda',  type: 'string',  mapping:'dsAyuda'},
				        {name: 'Ayuda',  type: 'string',  mapping:'dsAyuda'}
					    ]
	);			

	var idiomas = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: _ACTION_OBTENER_IDIOMAS
	            }),
	        reader: new Ext.data.JsonReader({
	        root: 'idiomasComboBox',
	        id: 'langCode'
	        },[
	       {name: 'langCode', mapping:'langCode', type: 'string'},
	       {name: 'langName', mapping:'langName', type: 'string'}
	    ])
	});			

	var cmbIdiomas = new Ext.form.ComboBox({
	    tpl: '<tpl for="."><div ext:qtip="{langCode}.{langName}" class="x-combo-list-item">{langName}</div></tpl>',
	    store: idiomas,
	    displayField:'langName',
	    valueField:'langCode',
	    hiddenName: 'l_Code',
	    typeAhead: true,
	    allowBlank : false,
	    mode: 'local',
	    triggerAction: 'all',
        id:'comboIdioEdit',
        fieldLabel: getLabelFromMap('comboIdioEdit',helpMap,'Idioma'),
        tooltip:getLabelFromMap('comboIdioEdit',helpMap, 'Idioma'),		
        hasHelpIcon:getHelpIconFromMap('comboIdioEdit',helpMap),								 
        Ayuda: getHelpTextFromMap('comboIdioEdit',helpMap),
	    width:120,
	    labelAlign:'right',
	    anchor:'80%',
	    //width: 150,
	    //bodyStyle:'padding:3px',
	    selectOnFocus:true,
	    forceSelection:true,
	    emptyText:'...',
	    disabled:true
	   });	

	var tipoObjeto = new Ext.data.Store({
	        proxy: new Ext.data.HttpProxy({
	            url: _ACTION_OBTENER_TIPO_OBJETO
	            }),
	        reader: new Ext.data.JsonReader({
	        root: 'comboTipoObjeto',
	        id: 'codigo'
	        },[
	       {name: 'codigo', mapping:'codigo', type: 'string'},
	       {name: 'descripcion', mapping:'descripcion', type: 'string'}
	    ])
	});			
	
	var cmbTipoObjeto = new Ext.form.ComboBox({
	    tpl: '<tpl for="."><div ext:qtip="{codigo}.{descripcion}" class="x-combo-list-item">{descripcion}</div></tpl>',
	    //id:'fgTipoObjeto',
	    //name:'fgTipoObjeto',
	    store: tipoObjeto,
	    displayField:'descripcion',
	    valueField:'codigo',
	    hiddenName: 'fgTipoObjeto',
	    typeAhead: true,
	    allowBlank : false,
	    labelAlign:'right',
	    mode: 'local',
	    triggerAction: 'all',
        id:'comboObjEdit',
        fieldLabel: getLabelFromMap('comboObjEdit',helpMap,'Tipo de objeto'),
        tooltip:getLabelFromMap('comboObjEdit',helpMap, 'Tipo de objeto'),		
        hasHelpIcon:getHelpIconFromMap('comboObjEdit',helpMap),								 
        Ayuda: getHelpTextFromMap('comboObjEdit',helpMap),
	    width:120,
	    //width: 110,
	    bodyStyle:'padding:15px',
	    selectOnFocus:true,
	    forceSelection:true,
	    emptyText:'...'
	   });	

	
	var cajaAyuda = new Ext.form.Checkbox({
        id:'fgAyudaEdit',
        fieldLabel: getLabelFromMap('fgAyudaEdit',helpMap,'Ayuda Adicional'),
        tooltip:getLabelFromMap('fgAyudaEdit',helpMap, 'Caja Ayuda'),		
        hasHelpIcon:getHelpIconFromMap('fgAyudaEdit',helpMap),								 
        Ayuda: getHelpTextFromMap('fgAyudaEdit',helpMap),
	    width:120,
        name:'fgAyuda',
        labelAlign:'right',
        readOnly:false,
        checked :false
       });
	

	var tabla = new Ext.FormPanel({
		url: _ACTION_OBTENER_REG_MECANISMO_DE_TOOLTIP,
		bodyStyle:'background: white',
		//title:'Definici&oacute;n de Propiedades',
        id:'DefPropEditId',
        title: '<span style="color:black;font-size:12px;">'+getLabelFromMap('DefPropEditId', helpMap,'Definici&oacute;n de Propiedades')+'</span>',		
		layout:'table',
		frame:true,
		width:650,
		labelAlign:'right',
		reader:mtooltip_reg,
       	successProperty: 'success',
		layoutConfig:{
						columns:3
		},
		items:[{		
				layout:"form",
				items:[{
						xtype: 'textfield',
						anchor: '95%',
						id:'dsTituloEdit',
                        fieldLabel: getLabelFromMap('dsTituloEdit',helpMap,'Pantalla'),
	                    tooltip:getLabelFromMap('dsTituloEdit',helpMap, 'Pantalla'),		
                        hasHelpIcon:getHelpIconFromMap('dsTituloEdit',helpMap),								 
                        Ayuda: getHelpTextFromMap('dsTituloEdit',helpMap),
	                    width:120,
						labelAlign:'right',
						id: 'dsTituloEdit', 
						name: 'dsTitulo',
						disabled: 'true',
						value: record.get('dsTitulo'),
						readOnly: 'true'
						},
						{
						xtype:'hidden',
						name: 'idObjeto'
						},
						{
						xtype:'hidden',
						name: 'idTitulo',
						value: record.get('idTitulo')
						},{
						xtype:'hidden',
						name:'lang_Code',
						value:IDIOMA_USR
						}],
				colspan: 2
				},
				{
				layout:"form",
				items:[
						cmbIdiomas
				    	]
				 },{
				 layout:"form",
				 items:[{
				 		html:'<p>Objetos</p>'
				 		}],
				 colspan:3
				},
				 /*,{
				 layout:"form",
				 items:[{
				 		html:'<p>Objetos</p>'
				 		}],
				 colspan:3
				},{
				layout:"form",
				items:[{
						html:'<p>Propiedades</p>'
					   }],
				 colspan:1
				},{
				layout:"form",
				items:[{
						html:'<p>Idioma Base</p>'
						}],
				colspan:1	
				},{
				layout:"form",
				items:[{
						html:'<p>Traduccion</p>'
						}]
				}*/
				{
				colspan:3,
				layout:"table",
				layoutConfig: {columns:4},
				items:[
						{	
							width:40,
							html:'&nbsp'
				   		},
						{	
							width:100,
							html:'<p>Propiedades</p>'
				   		},
				   		{	
				   			width:240,
							html:'<p>Idioma Base</p>'
						},
						{
							width:130,
							html:'<p>Traducci&oacute;n</p>'
						}
						]
				},{
				layout:"form",
				items:[{
						xtype: 'textfield',
						anchor:'95%',
                        fieldLabel: getLabelFromMap('nbObjetoEdit',helpMap,'Objeto'),
	                    tooltip:getLabelFromMap('nbObjetoEdit',helpMap, 'Objeto'),		
                        hasHelpIcon:getHelpIconFromMap('nbObjetoEdit',helpMap),								 
                        Ayuda: getHelpTextFromMap('nbObjetoEdit',helpMap),
	                    width:120,
						labelAlign:'right',
						id: 'nbObjetoEdit', 
						name: 'Objeto',
						disabled: 'true',
						value:record.get('nbObjeto'),
						readOnly:'true'
						},
						{
						xtype:'hidden',
						name: 'nbObjeto'//,
						//value:Ext.getCmp('texIdObj').getValue()
						}
						],
				colspan: 3
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						anchor:'95%',
						fieldLabel: getLabelFromMap('EtiquetaEdit',helpMap,'Etiqueta'),
	                    tooltip:getLabelFromMap('EtiquetaEdit',helpMap, 'Etiqueta'),		
                        hasHelpIcon:getHelpIconFromMap('EtiquetaEdit',helpMap),								 
                        Ayuda: getHelpTextFromMap('EtiquetaEdit',helpMap),
					    width:120,
						labelAlign:'right',
						id:'EtiquetaEdit',
						name:'Etiqueta',
						disabled: 'true',
						value:record.get('Etiqueta'),
						readOnly: 'true'
					  }],
				colspan:2
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						id:'nbEtiqueta',
						name:'nbEtiqueta',
						labelSeparator:'',
						allowBlank: false, 
						anchor:'95%'
					  }]
				},{
				layout:"form",
				items:[
						cmbTipoObjeto
						],
				colspan: 3
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						fieldLabel: getLabelFromMap('TooltipEdit',helpMap,'Tooltip'),
	                    tooltip:getLabelFromMap('TooltipEdit',helpMap, 'Tooltip'),		
                        hasHelpIcon:getHelpIconFromMap('TooltipEdit',helpMap),								 
                        Ayuda: getHelpTextFromMap('TooltipEdit',helpMap),
						width:120,
						labelAlign:'right',
						anchor:'95%',
						id:'TooltipEdit',
						name:'Tooltip',
						disabled: 'true',
						value:record.get('Tooltip'),
						readOnly: 'true'
						}],
				colspan: 2
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						id:'dsTooltip',
						name:'dsTooltip',
						labelSeparator:'',
					//	allowBlank: false, 
						anchor:'95%'
						}]
				},
				{
				layout:"form",
				items:[
						cajaAyuda
					    ],
				colspan: 3
				},
				{
				layout:"form",
				items:[{
						xtype:'textfield',
                        fieldLabel: getLabelFromMap('AyudaEdit',helpMap,'Texto Ayuda'),
	                    tooltip:getLabelFromMap('AyudaEdit',helpMap, 'Texto Ayuda'),		
                        hasHelpIcon:getHelpIconFromMap('AyudaEdit',helpMap),								 
                        Ayuda: getHelpTextFromMap('AyudaEdit',helpMap),
					    width:120,
						labelAlign:'right',
						id:'AyudaEdit',
						name:'Ayuda',
						disabled: 'true',
						readOnly: 'true'
						}],
				colspan:2
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						id:'dsAyuda',
						name:'dsAyuda',
						labelSeparator:'',
						anchor:'95%'
						}]
				}
			  ]
	});


	var window = new Ext.Window ({
			//title: '<span style="color:black;font-size:14px;">'+(record)?getLabelFromMap('95',helpMap,'Editar Mecanismos de Tooltip'):getLabelFromMap('94',helpMap,'Agregar Mecanismos de Tooltip')+'</span>',
			
			id:'wndwMecToolEditId',
            title: getLabelFromMap('wndwMecToolEditId', helpMap,'Editar Mecanismos de Tooltip'),			
			width: 665,
			height:500,
			autoHeight: true,
			modal: true,
        	buttonAlign:'center',
        	items:  tabla,
            //se definen los botones del formulario
            buttons : [ {

  		               text: getLabelFromMap('EditarIdButtonsGuardarId', helpMap,'Guardar'),                
				       tooltip: getToolTipFromMap('EditarIdButtonsGuardarId', helpMap,'Guardar'), 
                       disabled : false,
                
                       handler : function() {

                       if (tabla.form.isValid()) {
						
                       	
                        tabla.form.submit( {

                            //action a invocar cuando el formulario haga submit
                            url : _ACTION_AGREGAR_GUARDAR_MECANISMO_DE_TOOLTIP,

                            //funcion a ejecutar cuando la llamada a la action retorna satisfactoriamente
                            success : function(form, action) {
                                Ext.Msg.alert('Aviso', action.result.actionMessages[0], function(){reloadGrid();});
                                window.close();
                            },

                            //funcion a ejecutar cuando la llamada a la action NO se ejecuta satisfactoriamente
                            failure : function(form, action) {
                                Ext.Msg.alert('Error', action.result.errorMessages[0]);
                           },

                            //mensaje a mostrar mientras se guardan los datos
                            waitTitle: 'Espere',
                            waitMsg : 'guardando datos ...'

                        });

                    } else { Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400013', helpMap,'Complete la informaci&oacuten requerida'));
                           }
                }	

             },
             {
			  text: getLabelFromMap('EditarIdButtonsCancelarId', helpMap,'Cancelar'),                
			  tooltip: getToolTipFromMap('EditarIdButtonsCancelarId', helpMap,'Cancelar'), 
              handler : function() {
                    window.close();
             }
            }]
	});

    window.show();
	idiomas.load({
					callback:function(){
						cmbIdiomas.setValue(IDIOMA_USR);
						Ext.getCmp('comboIdioEdit').setDisabled(true);
					}
					
				});
	tipoObjeto.load({
			callback:function(){
								Ext.getCmp('comboObjEdit').setDisabled(true);
							}
		});

	tabla.form.load({
		waitTitle : getLabelFromMap('400021', helpMap,'Espere...'),
        waitMsg : getLabelFromMap('400028', helpMap,'Leyendo datos...'),
		params:{idObjeto: record.get("idObjeto"), lang_Code: record.get('langCode')},
		failure: function () {
				Ext.Msg.alert('', '');
		}
		}
	);
}