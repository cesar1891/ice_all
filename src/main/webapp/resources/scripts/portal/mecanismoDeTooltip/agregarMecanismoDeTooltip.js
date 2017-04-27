
/**
	INSERTA REGISTROS EN LA BASE DE DATOS
*/

function agregar (record) {

	idioma_actual=record.get('langCode');
	
	var mtooltip_agr = new Ext.data.JsonReader({
						root: 'MEstructuraList',
						totalProperty: 'totalCount',
						successProperty: '@success'
				      },[
				        {name: 'langCode', type: 'string', mapping: 'langCode'},
				        {name: 'Etiqueta',  type: 'string',  mapping:'nbEtiqueta'},
				        {name: 'dsTitulo',  type: 'string',  mapping:'dsTitulo'},
				        {name: 'idTitulo',  type: 'string',  mapping:'idTitulo'},
				        {name: 'idObjeto',  type: 'string',  mapping:'idObjeto'},
				        {name: 'nbObjeto',  type: 'string',  mapping:'nbObjeto'},
				        {name: 'Objeto',  type: 'string',  mapping:'nbObjeto'},
				        {name: 'fgTipoObjeto',  type: 'string',  mapping:'fgTipoObjeto'},
				        {name: 'Tooltip',  type: 'string',  mapping:'dsTooltip'},
				        {name: 'fgAyuda',  type: 'string',  mapping:'fgAyuda'},
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
	    hiddenName: 'lang_Code',
	    typeAhead: true,
	    allowBlank : false,
	    mode: 'local',
	    triggerAction: 'all',
	    //fieldLabel: "Idioma",
        id:'comboIdioAgre',
        fieldLabel: getLabelFromMap('comboIdioAgre',helpMap,'Idioma'),
        tooltip:getLabelFromMap('comboIdioAgre',helpMap, 'Idioma'),		
        hasHelpIcon:getHelpIconFromMap('comboIdioAgre',helpMap),								 
        Ayuda: getHelpTextFromMap('comboIdioAgre',helpMap),
	    width:120,
	    labelAlign:'right',
	    anchor:'80%',
	    selectOnFocus:true,
	    forceSelection:true,
	    emptyText:'...'
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
	    store: tipoObjeto,
	    displayField:'descripcion',
	    valueField:'codigo',
	    hiddenName: 'fgTipoObjeto',
	    typeAhead: true,
	    allowBlank : false,
	    mode: 'local',
	    triggerAction: 'all',
	    id:'ComboAgrMecTooltip',
        fieldLabel: getLabelFromMap('ComboAgrMecTooltip',helpMap,'Tipo de objeto'),
        tooltip: getToolTipFromMap('ComboAgrMecTooltip',helpMap,'Tipo de Objeto'),
        hasHelpIcon:getHelpIconFromMap('ComboAgrMecTooltip',helpMap),								 
        Ayuda: getHelpTextFromMap('ComboAgrMecTooltip',helpMap),
        width:130,
	    labelAlign:'right',
	    bodyStyle:'padding:15px',
	    selectOnFocus:true,
	    forceSelection:true,
	    emptyText:'...'
	   });	

	
	var cajaAyuda = new Ext.form.Checkbox({
        id:'fgAyuda',
        fieldLabel: getLabelFromMap('fgAyuda',helpMap,'Ayuda Adicional'),
	    tooltip:getLabelFromMap('fgAyuda',helpMap, 'Ayuda Adicional'),		
        hasHelpIcon:getHelpIconFromMap('fgAyuda',helpMap),								 
        Ayuda: getHelpTextFromMap('fgAyuda',helpMap),
        name:'fgAyuda',
        readOnly:false,
        labelAlign:'right',
        checked :false
       });
	
	
	var table = new Ext.FormPanel({
		//title:'Definici&oacute;n de Propiedades',
		id:'MecDeTooltip',
        title: '<span style="color:black;font-size:12px;">'+getLabelFromMap('MecDeTooltip', helpMap,'Definici&oacute;n de Propiedades')+'</span>',		
		bodyStyle:'background: white',
		layout:'table',
		frame:true,
		width:650,
		labelAlign:'right',
		url: _ACTION_OBTENER_REG_MECANISMO_DE_TOOLTIP,
		reader:mtooltip_agr,
       	successProperty: 'success',
		layoutConfig:{
						columns:3
		},
		items:[{		
				layout:"form",
				items:[{
						xtype: 'textfield',
						anchor: '95%',
                        fieldLabel: getLabelFromMap('dsTituloTxt',helpMap,'Pantalla'),
	                    tooltip:getLabelFromMap('dsTituloTxt',helpMap, 'Pantalla'),		
                        hasHelpIcon:getHelpIconFromMap('dsTituloTxt',helpMap),								 
                        Ayuda: getHelpTextFromMap('dsTituloTxt',helpMap),
                        width:130,
                        labelAlign:'right',
						id: 'dsTituloTxt', 
						name: 'dsTitulo',
						value: record.get('dsTitulo'),
						disabled: 'true',
						readOnly: 'true'
						},
						{
						xtype:'hidden',
						name: 'idTitulo',
						value: record.get('idTitulo')
						}
						],
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
				},/*{
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
						html:'<p>Traducci&oacute;n</p>'
						}]
				},*/
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
				},
				{
				layout:"form",
				items:[{
						xtype: 'textfield',
						anchor:'95%',
						id:'texIdObj',
                        fieldLabel: getLabelFromMap('texIdObj',helpMap,'Objeto'),
	                    tooltip:getLabelFromMap('texIdObj',helpMap, 'Objeto'),		
                        hasHelpIcon:getHelpIconFromMap('texIdObj',helpMap),								 
                        Ayuda: getHelpTextFromMap('texIdObj',helpMap),
						width:130,
						labelAlign:'right',
						name: 'Objeto',
						disabled: 'true',
						readOnly: 'true'
						},
						{
						xtype:'hidden',
						name: 'nbObjeto'//,
						//value:Ext.getCmp('texIdObj').getValue()
						}/*,
						{
						xtype:'hidden',
						name: 'idObjeto',
						value:record.get('idObjeto')
						}*/
						],
				colspan: 3
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						anchor:'95%',
                        id:'textEtiquId',
                        fieldLabel: getLabelFromMap('textEtiquId',helpMap,'Etiqueta'),
	                    tooltip:getLabelFromMap('textEtiquId',helpMap, 'Etiqueta'),		
                        hasHelpIcon:getHelpIconFromMap('textEtiquId',helpMap),								 
                        Ayuda: getHelpTextFromMap('textEtiquId',helpMap),
						width:130,
						name:'Etiqueta',
						labelAlign:'right',
						disabled: 'true',
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
						anchor:'95%',
                        id:'textToolId',
                        fieldLabel: getLabelFromMap('textToolId',helpMap,'Tooltip'),
	                    tooltip:getLabelFromMap('textToolId',helpMap, 'Tooltip'),		
                        hasHelpIcon:getHelpIconFromMap('textToolId',helpMap),								 
                        Ayuda: getHelpTextFromMap('textToolId',helpMap),
						width:130,
						name:'Tooltip',
						labelAlign:'right',
						disabled: 'true',
						readOnly: 'true'
						}],
				colspan: 2
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						id:'dsTooltipIdioma',
						name:'dsTooltip',
						labelSeparator:'',
						//allowBlank: false, 
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
                        id:'textAyuId',
                        fieldLabel: getLabelFromMap('textAyuId',helpMap,'Texto Ayuda'),
	                    tooltip:getLabelFromMap('textAyuId',helpMap, 'Texto Ayuda'),		
                        hasHelpIcon:getHelpIconFromMap('textAyuId',helpMap),								 
                        Ayuda: getHelpTextFromMap('textAyuId',helpMap),
						width:130,
						labelAlign:'right',	
						name:'Ayuda',
						disabled: 'true',
						readOnly: 'true'
						}],
				colspan:2
				},{
				layout:"form",
				items:[{
						xtype:'textfield',
						id:'dsAyudaIdioma',
						name:'dsAyuda',
						labelSeparator:'',
						anchor:'95%'
						}]
				}
			  ]
	});


	var window = new Ext.Window ({
		
			//title: '<span style="color:black;font-size:14px;">'+(record)?getLabelFromMap('95',helpMap,'Agregar Mecanismos de Tooltip'):getLabelFromMap('94',helpMap,'Agregar Mecanismos de Tooltip')+'</span>',
           
			id:'wndwMecToolAgrId',
            title: getLabelFromMap('wndwMecToolAgrId', helpMap,'Agregar Mecanismos de Tooltip'),			
			width: 665,
			Height:500,
        	buttonAlign:'center',
        	modal: true,
        	items:  table,	
            buttons : [ {
				     text: getLabelFromMap('AgregarIdButtonsGuardarId', helpMap,'Guardar'),                
				     tooltip: getToolTipFromMap('AgregarIdButtonsGuardarId', helpMap,'Guardar'), 
                     disabled : false,
                     handler : function() {

                     if (table.form.isValid()) {
                     	/*if(idioma_actual!=Ext.getCmp('comboIdioAgre').getValue())
                     	{*/
                        table.form.submit( {

                            //action a invocar cuando el formulario haga submit
                            url : _ACTION_AGREGAR_GUARDAR_MECANISMO_DE_TOOLTIP,

                            //funcion a ejecutar cuando la llamada a la action retorna satisfactoriamente
                            success : function(form, action) {
                                Ext.Msg.alert('Aviso', action.result.actionMessages[0], function(){reloadGrid();})
                                window.close()
                            },

                            //funcion a ejecutar cuando la llamada a la action NO se ejecuta satisfactoriamente
                            failure : function(form, action) {
                                Ext.Msg.alert('Error', action.result.errorMessages[0])
                           },

                            //mensaje a mostrar mientras se guardan los datos
                            waitTitle: 'Espere',
                            waitMsg : 'guardando datos ...'

                        });
                     	/*}else {Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400100', helpMap,'Fijarse el idioma'));
                           }*/
                     	
                    } else {Ext.MessageBox.alert(getLabelFromMap('400000', helpMap,'Aviso'),getLabelFromMap('400013', helpMap,'Complete la informaci&oacuten requerida'));
                           }
                }	
            }, {
				text: getLabelFromMap('AgregarIdButtonsCancelarId', helpMap,'Cancelar'),                
				tooltip: getToolTipFromMap('AgregarIdButtonsCancelarId', helpMap,'Cancelar'), 
                handler : function() {
                    window.close()
                }

            }]

	});
    window.show();
	idiomas.load({
					callback:function(){
						cmbIdiomas.setValue(record.get("langCode"));
					}
				});
	tipoObjeto.load(
		{
			callback:function(){
								Ext.getCmp('ComboAgrMecTooltip').setDisabled(true);
							}
		});
	table.load({
		waitTitle : getLabelFromMap('400021', helpMap,'Espere...'),
        waitMsg : getLabelFromMap('400028', helpMap,'Leyendo datos...'),
		params:{
			idObjeto: record.get("idObjeto"), 
			lang_Code: record.get("langCode")
			},
		failure: function () {
				Ext.Msg.alert('', '');
		}
		});
}