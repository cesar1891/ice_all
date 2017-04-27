Ext.require(['Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);
Ext.onReady(function() {
    Ext.tip.QuickTipManager.init();
    var bandera='';
    var validaTipoAgente = '';
    var cdsucurs='';
    var nmcuadro='';
    var nmsuplem='';
    var guardarRegistros=null;
    var resultado=0;
    var valorIndex= 0;
    var contadorGral = 0;
    var datosInternos = [];
    //var datoRequerido= 1;

    /*GENERACION DE MODELOS Y STORE*/
    Ext.define('modeloGridClau',{
        extend:'Ext.data.Model',
        fields:[
            {type:'string',        name:'cdagente'      },               {type:'string',        name:'cdagenteA'     },
            {type:'string',        name:'cdsucurs'      },               {type:'string',        name:'cdtipoAg'      },
            {type:'string',        name:'descripl'      },               {type:'string',        name:'nmcuadro'      },
            {type:'string',        name:'nmsuplem'      },               {type:'string',        name:'porparti'      },
            {type:'string',        name:'porredau'      },               {type:'string',        name:'comision'      },
            {type:'string',        name:'nvacomision'   }
        ]
    });
    
    var storGridClau= new Ext.data.Store({
        model      : 'modeloGridClau'
        ,autoLoad  : false
        ,proxy     : {
            type: 'ajax',
            url : _URL_OBTIENE_DATOS_AGENTE_X_PUNTOS,
            reader: {
                type: 'json',
                root: 'datosPolizaAgente'
            }
        }
    });
    
    var storeDatosAgente = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            url : _URL_CATALOGOS,
            extraParams : {catalogo:_CATALOGO_AGENTES},
            reader: {
                type: 'json',
                root: 'lista'
            }
        }
    });
    storeDatosAgente.load();
    
    var storeTiposAgente = Ext.create('Ext.data.Store', {
        model:'Generic',
        autoLoad:true,
        proxy: {
            type: 'ajax',
            url : _URL_GENERAL_AGENTES,
            reader: {
                type: 'json',
                root: 'datosGeneralesAgente'
            }
        }
    });

    /* INFORMACION PARA EL WINDOWS */
    var panelModificacionInsercion= Ext.create('Ext.form.Panel',{
        border  : 0
        ,bodyStyle:'padding:5px;'
        ,items :
        [   
            totalClaveAgentes= Ext.create('Ext.form.field.ComboBox',
			{
            	fieldLabel : 'Agente',			displayField : 'value',		name : 'pv_cdagente_i',		width : 500,							valueField : 'key',
            	forceSelection : true,			matchFieldWidth : false,	queryMode :'remote',		queryParam : 'params.agente',			minChars : 3,
            	store : storeDatosAgente,		triggerAction : 'all',		hideTrigger : true,			allowBlank : false,						id : 'pv_cdagente_i',
            	labelWidth : 170
			}),
			//INFORMACION PARA EL TIPO DE AGENTE
			tiposAgente = Ext.create('Ext.form.field.ComboBox',
			{
				fieldLabel :'Tipo de Agente',	allowBlank : false,			displayField : 'value',		id : 'tipoAgente',						name:'tipoAgente',
				labelWidth : 170,				width : 500,				valueField : 'key',			editable : false,						forceSelection : false,
				queryMode :'local',				store : storeTiposAgente,	triggerAction : 'all'
			}),
			{
				xtype : 'numberfield',			fieldLabel : 'Comisi&oacute;n',id : 'comisionCuadro',	name : 'comisionCuadro',				allowNegative : false,
				labelWidth : 170,				allowBlank : false,			maxValue : 100,				allowDecimals : true,					decimalSeparator : '.',
				minValue : 0,					width : 500,				readOnly : true
			},
			{
				xtype : 'numberfield',			fieldLabel : 'Nva. Comisi&oacute;n',id : 'nvacomision',	name : 'nvacomision',					allowNegative : false,
				labelWidth : 170,				allowBlank : true,			maxValue : 100,				allowDecimals : true,					decimalSeparator : '.',
				minValue : 0,					width : 500,
				listeners : {
					'blur':function(field){
						var recuperado = field.getValue()+"";
						if(recuperado != "null"){
							if(+field.getValue() <= 0 || +field.getValue() == +Ext.getCmp('comisionCuadro').getValue()){
								centrarVentanaInterna(Ext.Msg.show({
	                                title:'Warning',
	                                msg: 'El valor debe ser diferente a la comisi&oacute;n y mayor que 0.',
	                                buttons: Ext.Msg.OK,
	                                icon: Ext.Msg.WARNING
	                            }));
								Ext.getCmp('nvacomision').setValue();
							}
						}
					}
				}
			},
			//INFORMACION PARA LA MODIFICACION DE PARTICIPACION
			{
            	xtype : 'numberfield',			id : 'participacion',		name : 'participacion',		fieldLabel : 'Participaci&oacute;n',	labelWidth : 170,
            	width : 500,					allowBlank : false,			maxValue : 100,				minValue : 0,							allowDecimals : true,
            	decimalSeparator:'.',			allowBlank:false
			},
			//INFORMACION PARA LA MODIFICACION DE CESION DE COMISION
			{
				xtype : 'numberfield',			fieldLabel : 'Cesi&oacute;n de Comisi&oacute;n',		id : 'sesionComision',					name : 'sesionComision',
				allowNegative : false,			labelWidth : 170,			allowBlank : false,			allowDecimals : true,
				decimalSeparator : '.',			minValue : 0,				width : 500
			}
        ]
    });
            
    /*PANTALLA EMERGENTE PARA LA INSERCION Y MODIFICACION DE LOS DATOS DEL GRID*/
    ventanaGrid= Ext.create('Ext.window.Window', {
           title: 'P&Oacute;LIZA - AGENTE',
           modal:true,
           height: 290,
           closable: false ,
           closeAction: 'hide',
           items:[panelModificacionInsercion],
           buttonAlign : 'center',
           buttons:[{
                  text: 'Aceptar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/accept.png',
                  handler: function() {
                        if (panelModificacionInsercion.form.isValid()) {
                            var datos=panelModificacionInsercion.form.getValues();
                            
                            if((parseFloat(datos.sesionComision) > parseFloat(datos.comisionCuadro))){
                            	centrarVentanaInterna(Ext.Msg.show({
                                    title:'Error',
                                    msg: 'La cesi&oacute;n de comisi&oacute;n debe ser menor que la comisi&oacute;n.',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.WARNING
                                }));
                            }else if(parseFloat(datos.sesionComision) > parseFloat(datos.participacion)){
                            	centrarVentanaInterna(Ext.Msg.show({
                                    title:'Error',
                                    msg: 'El valor de la partici&oacute;n debe ser mayor a Cesi&oacute;n de comisi&oacute;n',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.WARNING
                                }));
                            }
                            else{
                            	if(bandera==0){
                                	if(storGridClau.data.items[0]!=undefined){           
                                        cdsucurs=storGridClau.data.items[0].data.cdsucurs;    
                                        nmcuadro=storGridClau.data.items[0].data.nmcuadro;
                                        nmsuplem=storGridClau.data.items[0].data.nmsuplem;
                                    }
                                    
                                    guardaEliminaPorcentajeAgentes(datos.pv_cdagente_i,datos.pv_cdagente_i, nmsuplem, datos.tipoAgente, datos.sesionComision, nmcuadro, cdsucurs,datos.participacion,datos.nvacomision, "I", datos.comisionCuadro);
                                    Ext.getCmp('btnGuardaRegistro').enable();
                                    ventanaGrid.close();
                                }
                                else{    //valores del windows
                                    var obtener = [];
                                    storGridClau.each(function(record) {
                                        obtener.push(record.data);
                                    });
                                    
                                    if(contadorGral == 0){
                                        for(var i=0;i < obtener.length;i++){
                                            datosInternos[i]=obtener[i].cdagente;
                                        }
                                        contadorGral=1;
                                    }
                                    var respuesta='';
                                    if(datosInternos[valorIndex] !=undefined){
                                        respuesta=datosInternos[valorIndex];
                                    }else{
                                        respuesta=datos.pv_cdagente_i;
                                    }
                                    
                                    guardaEliminaPorcentajeAgentes(datos.pv_cdagente_i,respuesta, storGridClau.data.items[valorIndex].get('nmsuplem'), datos.tipoAgente,datos.sesionComision, storGridClau.data.items[valorIndex].get('nmcuadro'), storGridClau.data.items[valorIndex].get('cdsucurs'),datos.participacion,datos.nvacomision, "U", datos.comisionCuadro);
                                    Ext.getCmp('btnGuardaRegistro').enable();
                                    ventanaGrid.close();
                                }
                            }
                        } else {
                        	centrarVentanaInterna(Ext.Msg.show({
                               title: 'Aviso',
                               msg: 'Complete la informaci&oacute;n requerida',
                               buttons: Ext.Msg.OK,
                               icon: Ext.Msg.WARNING
                           }));
                        }
                    }
              },
            {
                  text: 'Cancelar',
                  id  :'btnCancelar',
                  icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                  handler: function() {
					ventanaGrid.close();
                  }
            }
           ]
           });
    
    
    /**
     * Para evitar duplicidad de componentes se  destruye la ventana desde un nivel superior
     */
    _ventanaGridAgentesSuperior = ventanaGrid;
    
    /*MENU PRINCIPAL */
    menuPrincipal= Ext.create('Ext.form.Panel',{
        border    : 0
        ,renderTo : 'div_clau'
        ,url: _URL_GUARDA_PORCENTAJE_X_PUNTOS
        ,items    :
        [
            /*ELIMINAR EL PANEL DE LAS BUSQUEDAS*/            
            Ext.create('Ext.panel.Panel',{
                border  : 0
                ,items :
                [
                    {
                        xtype : 'textfield',           id:'unieco',             name:'params.cdunieco',     fieldLabel: 'C&oacute;digo de Unidad Econ&oacute;mica',
                        value: inputCdunieco,          allowBlank:false,        hidden:true
                    },
                    
                    {
                        xtype : 'textfield',           fieldLabel: 'Producto',  name:'params.ramo',         id:'ramo',
                        value: inputCdramo,            hidden:true
                    },
                    {
                        xtype : 'textfield',            fieldLabel: 'Estado del la p&oacute;liza',          id:'estado',
                        name:'params.estado',           value: inputEstado,     hidden:true
                    },
                    {
                        xtype : 'textfield',            fieldLabel: 'Numero de P&oacute;liza',              id:'poliza',
                        name:'params.nmpoliza',         value: inputNmpoliza,   hidden:true
                    }
                ]
            }),
            Ext.define('datosGridC', {
				extend: 'Ext.grid.Panel',
				collapsible   : true,
				titleCollapse : true,
				requires: [
				           'Ext.selection.CellModel',
				           'Ext.grid.*',
				           'Ext.data.*',
				           'Ext.util.*',
				           'Ext.form.*'
			           ],
		       xtype: 'cell-editing',
		       title: 'DATOS GENERALES',
		       frame: false,
		       initComponent: function(){
		    	   this.cellEditing = new Ext.grid.plugin.CellEditing({
	    		   		clicksToEdit: 1
	    	   		});
				    Ext.apply(this, {
					   height: 200,
					   plugins: [this.cellEditing],
					   store: storGridClau,
					   columns: 
					   [
							{
					    		xtype		: 'actioncolumn',		width: 60,		 sortable: false,				menuDisabled: true,
					    		items: [{
                                        icon:_CONTEXT+'/resources/fam3icons/icons/delete.png',
                                        tooltip: 'Quitar Agente',
                                        scope: this,
                                        handler: this.onRemoveClick
                                    },
                                    {
                                        icon:_CONTEXT+ '/resources/fam3icons/icons/pencil.png',
                                        tooltip: 'Modificar Agente',
                                        scope: this,
                                        handler: this.onAddClick
                                    }
			    			    ]
					    	},
					    	{    header     : 'Clave Agente',                        dataIndex : 'cdagente',        flex      : 1    },
		                	{    header     : 'Agente',                              dataIndex : 'cdtipoAg',        flex      : 1    },
		                	{    header     : 'Tipo de Agente',                      dataIndex : 'descripl',        flex      : 1    },
		                	{    header     : 'Comisi&oacute;n',                     dataIndex : 'comision',        flex      : 1    },
		                	{    header     : 'Nva. Comisi&oacute;n',                dataIndex : 'nvacomision',     flex      : 1    },
		                	{    header     : 'Participaci&oacute;n',                dataIndex : 'porparti',        flex      : 1    },
		                	{    header     : 'Cesi&oacute;n de comisi&oacute;n',    dataIndex : 'porredau',        flex      : 1    }
							
				    	],
				    	tbar: [{
			                    icon:_CONTEXT+'/resources/fam3icons/icons/add.png',
			                    text: 'Agregar agente',
			                    id:'btnAgregarAgente',
			                    scope: this,
			                    handler: function() {
			                        bandera= 0;
			                        valorIndex=0;
			                        actualizacionInsercion(null,null,null,null);
			                    }
			                }]
					   });
				   		this.callParent();
			       },
			       onRemoveClick: function(grid, rowIndex){
			    	   if(+storGridClau.data.length > 1){
			    		   var record=this.getStore().getAt(rowIndex);
			    		   
			    		   guardaEliminaPorcentajeAgentes(record.data.cdagente, record.data.cdagente, record.data.nmsuplem, record.data.cdtipoAg, record.data.porredau, record.data.nmcuadro, record.data.cdsucurs,record.data.porparti,record.data.nvacomision, 'D' , record.data.comision);
				    	   this.getStore().removeAt(rowIndex);
			    	   }else{
			    		   centrarVentanaInterna(mensajeWarning('Antes de eliminar el agente, ingrese el nuevo agente.'));
		    		   }
			       },
			       onAddClick: function(grid, rowIndex){
			       	 var record=this.getStore().getAt(rowIndex);
			       	   	claveAgente= Ext.getCmp('pv_cdagente_i').setValue(record.data.cdagente);
			       	   	tipoAgente= Ext.getCmp('tipoAgente').setValue(record.data.cdtipoAg);
				   	    valorparticipacion= Ext.getCmp('participacion').setValue(record.data.porparti);
                        valorsesionComision= Ext.getCmp('sesionComision').setValue(record.data.porredau);
                        Ext.getCmp('comisionCuadro').setValue(record.data.comision);
                        Ext.getCmp('nvacomision').setValue(record.data.nvacomision);
                        Ext.getCmp('nvacomision').setMaxValue(record.data.comision);
                        bandera= 1;
                        valorIndex= rowIndex;
                        actualizacionInsercion(record.data.cdagente ,record.data.cdtipoAg ,record.data.porparti ,record.data.porredau);
				   }
				})
        ],
        buttonAlign: 'center'
            ,buttons : [{
                text: "Guardar"
                ,id:'btnGuardaRegistro'    
                ,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
                ,
                handler: function() {
                    var arr = [];
                    guardarRegistros= [];
                    
                    storGridClau.each(function(record) {
                        arr.push(record.data);
                    });
                   
                    resultado = 0;
                    validaTipoAgente=0;
                    contadorAgente=0;
                    /* veificar que el Id del Agente no se repita */
                    for(var i = 0; i < arr.length; i++){
        	        	for (var j = i + 1; j < arr.length; j++){
        	        		if(arr[i].cdagente == arr[j].cdagente){
        	        			contadorAgente++;
        	        		}
        	        	}
        	        }
                    
                    for(var i=0;i < arr.length;i++){
                    	guardarRegistros.push({cdagente: arr[i].cdagente,
                                               cdagenteA: arr[i].cdagenteA,
                                               cdsucurs: arr[i].cdsucurs,
                                               cdtipoAg: arr[i].cdtipoAg, descripl: arr[i].descripl,
                                               nmcuadro: arr[i].nmcuadro, nmsuplem: arr[i].nmsuplem,
                                               porparti: arr[i].porparti, porredau:arr[i].porredau,
                                               comision: arr[i].comision,
                                               nvacomision: arr[i].nvacomision});
                        resultado=parseFloat(resultado) + parseFloat(arr[i].porparti);
                        storGridClau.data.items[i].set('cdagenteA',arr[i].cdagente);
                        if(parseFloat(arr[i].cdtipoAg)==1){
                            validaTipoAgente++;
                        }
                    }
                    if (resultado == 100 && validaTipoAgente==1 && contadorAgente == 0) {
                        var submitValues={};
                        var formulario=menuPrincipal.form.getValues(); // Obtiene el valor del formulario
                        formulario['cdunieco']=formulario['params.cdunieco'];
                        formulario['nmpoliza']=formulario['params.nmpoliza'];
                        formulario['ramo']=formulario['params.ramo'];
                        formulario['estado']=formulario['params.estado'];
                        submitValues['params']=formulario;
                        submitValues['datosPorcentajeAgente']=guardarRegistros; // guardamos la informacion del grid, dependiendo del numero
                        menuPrincipal.setLoading(true);
                        Ext.Ajax.request({
                            url: _URL_GUARDA_PORCENTAJE_X_PUNTOS,
                            jsonData:Ext.encode(submitValues), // convierte a estructura JSON
                            
                            success:function(response,opts){
                                menuPrincipal.setLoading(false);
                                var jsonResp = Ext.decode(response.responseText);
                                if(jsonResp.success==true){
                                    contadorGral=0;
                                    datosInternos = [];
                                    Ext.getCmp('btnGuardaRegistro').disable();
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title:'Guardado',
                                        msg: 'Se modificaron los datos de los agentes',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.OK
                                    }));
                                    expande(1);
                                }
                                else{
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title:'Error',
                                        msg: 'Error al modificar los registros del Agente',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    }));
                                }
                            },
                            failure:function(response,opts){
                                menuPrincipal.setLoading(false);
                                centrarVentanaInterna(Ext.Msg.show({
                                    title:'Error',
                                    msg: 'Error de comunicaci&oacute;n',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                }));
                            }
                        });
                    } else {
                    	
                    	var mensaje='';
                    	if(resultado < 100 ||resultado > 100){
                    		mensaje= 'La sumatoria de los porcentaje de participaci&oacute;n debe de ser igual a 100';
                		}
                    	if(validaTipoAgente!=1){
                    		mensaje= 'Debe de existir solo un tipo de agente principal';
                		}
                    	if(contadorAgente > 0){
                    		mensaje= 'El agente no se puede repetir';
                		}
                    	centrarVentanaInterna(Ext.Msg.show({
                           title: 'Aviso',
                           msg: mensaje,
                           buttons: Ext.Msg.OK,
                           icon: Ext.Msg.WARNING
                       }));
                    }
                }    
            },
            {
                text: "Recargar"
                ,id:'btnRecargar'
                ,icon:_CONTEXT+'/resources/fam3icons/icons/arrow_refresh.png'
                , handler: function() {
                    storGridClau.removeAll();
                    var params = {
                        'params.cdunieco' : Ext.getCmp('unieco').getValue(),
                        'params.cdramo' : Ext.getCmp('ramo').getValue(),
                        'params.estado' : Ext.getCmp('estado').getValue(),
                        'params.nmpoliza' : Ext.getCmp('poliza').getValue()
                    };
                    contadorGral=0;
                    datosInternos = [];
                    storGridClau.load({
                        params: params,
                        callback: function(records, operation, success){
                            if(success){
                                if(records.length <= 0){
                                    Ext.getCmp('btnAgregarAgente').disable();
                                    Ext.getCmp('btnRecargar').disable();
                                    centrarVentanaInterna(mensajeWarning('No se encontraron datos, se requiere al menos un Agente',function(){
                                    	bandera= 0;
                                        valorIndex=0;
                                        //datoRequerido = 0;
                                        Ext.getCmp('btnCancelar').disable();
                                        actualizacionInsercion(null,null,null,null);
                					}));
                                }else{
                                	Ext.getCmp('btnRecargar').enable();
                                	Ext.getCmp('btnCancelar').enable();
                                }
                            }else{
                                Ext.getCmp('btnAgregarAgente').disable();
                                Ext.getCmp('btnRecargar').disable();
                                centrarVentanaInterna(Ext.Msg.show({
                                     title: 'Error',
                                     msg: 'Error al obtener los datos',
                                     buttons: Ext.Msg.OK,
                                     icon: Ext.Msg.ERROR
                                 }));
                            }
                        }
                    });
                }
            	
            }]
    });

    function actualizacionInsercion(claveAgente,tipoAgente,valorparticipacion,valorsesionComision)
    {
    	var params = {
			'params.cdunieco' : Ext.getCmp('unieco').getValue(),
			'params.cdramo' : Ext.getCmp('ramo').getValue(),
			'params.estado' : Ext.getCmp('estado').getValue(),
			'params.nmpoliza' : Ext.getCmp('poliza').getValue()
		};
		
    	if(bandera == 0){
            panelModificacionInsercion.getForm().reset();
            Ext.getCmp('comisionCuadro').setValue(storGridClau.data.items[0].data.comision);
            Ext.getCmp('nvacomision').setValue(storGridClau.data.items[0].data.nvacomision);
            Ext.getCmp('nvacomision').setMaxValue(storGridClau.data.items[0].data.comision);
            centrarVentanaInterna(ventanaGrid.show());
        }
        else{
            Ext.Ajax.request({
                url     : _URL_CATALOGOS
                ,params : {
                    'params.agente'  : claveAgente,
                    catalogo:_CATALOGO_AGENTES_UNICO
                }
                ,success : function (response){
                    var json=Ext.decode(response.responseText);
                    claveAgente = json.lista[0].key;
                    
                    storeDatosAgente.load({
						params: {
							'params.agente'   : claveAgente
						}
					});
					
					panelModificacionInsercion.query('combo[name=pv_cdagente_i]')[0].setValue(claveAgente);
                    centrarVentanaInterna(ventanaGrid.show());                    
                },
                failure : function (){
                    me.up().up().setLoading(false);
                    centrarVentanaInterna(Ext.Msg.show({
                        title:'Error',
                        msg: 'Error de comunicaci&oacute;n',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    }));
                }
            });
        }
    }
    
    function guardaEliminaPorcentajeAgentes(cdagente,cdagenteA, nmsuplem, cdtipoAg, porredau, nmcuadro, cdsucurs,porparti,nvacomision,accion, comisionCuadro){
            Ext.Ajax.request({
                url     : _URL_GUARDA_ELIMINA_PORC_X_PUNTOS
                ,params : {
                	'params.cdunieco'   : Ext.getCmp('unieco').getValue()
                    ,'params.ramo'      : Ext.getCmp('ramo').getValue()
                    ,'params.estado'    : Ext.getCmp('estado').getValue()
                    ,'params.nmpoliza'  : Ext.getCmp('poliza').getValue()
                    ,'params.cdagente'  : cdagente
                    ,'params.cdagenteA' : cdagenteA
                    ,'params.nmsuplem'   : nmsuplem
                    ,'params.cdtipoAg'   : cdtipoAg
                    ,'params.porredau'   : porredau
                    ,'params.nmcuadro'   : nmcuadro
                    ,'params.cdsucurs'   : cdsucurs
                    ,'params.porparti'   : porparti
                    ,'params.comision'   : comisionCuadro
                    ,'params.nvacomision': nvacomision
                    ,'params.accion'    : accion
                }
                ,success : function (response){
                    var json=Ext.decode(response.responseText);
                    if(json.success){
	                	storGridClau.removeAll();
					    var params = {
					        'params.cdunieco' : Ext.getCmp('unieco').getValue(),
					        'params.cdramo' : Ext.getCmp('ramo').getValue(),
					        'params.estado' : Ext.getCmp('estado').getValue(),
					        'params.nmpoliza' : Ext.getCmp('poliza').getValue()
					    };
					    
					    storGridClau.load({
					        params: params,
					        callback: function(records, operation, success){
					            if(success){
					                if(records.length <= 0){
					                	centrarVentanaInterna(mensajeWarning('No se encontraron datos, se requiere al menos un Agente',function(){
					                    	bandera= 0;
					                        valorIndex=0;
					                        //datoRequerido = 0;
					                        Ext.getCmp('btnCancelar').disable();
					                        actualizacionInsercion(null,null,null,null);
										}));
					                }else{
					                	Ext.getCmp('btnCancelar').enable();
					                }
					            }else{
					            	centrarVentanaInterna(Ext.Msg.show({
					                     title: 'Error',
					                     msg: 'Error al obtener los datos',
					                     buttons: Ext.Msg.OK,
					                     icon: Ext.Msg.ERROR
					                 }));
					            }
					        }
					    });
                    }
                },
                failure : function (){
                    me.up().up().setLoading(false);
                    centrarVentanaInterna(Ext.Msg.show({
                        title:'Error',
                        msg: 'Error de comunicaci&oacute;n',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    }));
                }
            });
    }
    
    //Cargamos los datos de inicio
    storGridClau.removeAll();
    var params = {
        'params.cdunieco' : Ext.getCmp('unieco').getValue(),
        'params.cdramo'   : Ext.getCmp('ramo').getValue(),
        'params.estado'   : Ext.getCmp('estado').getValue(),
        'params.nmpoliza' : Ext.getCmp('poliza').getValue()
    };
    
    storGridClau.load({
        params: params,
        callback: function(records, operation, success){
            if(success){
                if(records.length <= 0){
                    Ext.getCmp('btnAgregarAgente').enable();
                    Ext.getCmp('btnGuardaRegistro').disable();
                    Ext.getCmp('btnRecargar').disable();
                    centrarVentanaInterna(mensajeWarning('No se encontraron datos, se requiere al menos un Agente',function(){
                    	bandera= 0;
                        valorIndex=0;
                        Ext.getCmp('btnCancelar').disable();
                        actualizacionInsercion(null,null,null,null);
					}));
                }else{
                	Ext.getCmp('btnCancelar').enable();
                	Ext.getCmp('btnRecargar').enable();
                }
            }else{
                Ext.getCmp('btnAgregarAgente').disable();
                Ext.getCmp('btnRecargar').disable();
                centrarVentanaInterna(Ext.Msg.show({
                     title: 'Error',
                     msg: 'Error al obtener los datos',
                     buttons: Ext.Msg.OK,
                     icon: Ext.Msg.ERROR
                 }));
            }
        }
    });
});