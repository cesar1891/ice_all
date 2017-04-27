Ext.onReady(function() {
    
    Ext.apply(Ext.form.field.VTypes, {
        
        password: function(val, field) {
            if (field.initialPassField) {
                var pwd = field.up('form').down('#' + field.initialPassField);
                return (val == pwd.getValue());
            }
            return true;
        },

        passwordText: 'Las contrase&ntilde;as no coinciden.'
    });
    
    var dsPassword = new Ext.form.TextField({
        id:'password',
        fieldLabel: 'Contrase&ntilde;a',
        inputType: 'password',
        name: 'params.password',
        allowBlank  : editMode,
        hidden      : editMode,
        blankText:'La contrase&ntilde;a es un dato requerido',
        minLength: 5,
        minLengthText: 'La contrase&ntilde;a debe tener almenos 5 caracteres.',
        listeners:{
            scope:this,
            change: function(field) {
                var confirmField = field.up('form').down('[name=passwordConfirm]');
                confirmField.validate();
            }
        }
    });

    var confirmPassword = new Ext.form.TextField({
        id:'confirmPassword',
        fieldLabel: 'Confirme su Contrase&ntilde;a',
        inputType: 'password',
        vtype: 'password',
        name: 'passwordConfirm',
        allowBlank  : editMode,
        hidden      : editMode,
        blankText:'La confirmaci&oacute;n de la contrase&ntilde;a es un dato requerido',
        initialPassField: 'password', // id del campo password inicial
        listeners:{
            scope:this
        }
    });
    
    var panelPersona = Ext.create('Ext.form.Panel', {
        url: _URL_INSERTA_USUARIO,
        border: false,
        defaults: {
            width: 400  
        },
        renderTo : 'div_usuarios',
        bodyStyle:'padding:5px 0px 0px 40px;',
        items    : [
                    {
                        xtype      : 'hidden',
                        name       : 'params.accion',
                        value      : editMode? 'U' : 'I'
                    },{
                        xtype         : 'combo',
                        //labelWidth    : 100,
                        name          : 'params.cdsisrol',
                        fieldLabel    : 'Rol',
                        readOnly      : editMode,
                        allowBlank    : editMode,
                        hidden        : editMode,
                        valueField    : 'key',
                        displayField  : 'value',
                        forceSelection: true,
                        queryMode     :'local',
                        store         : Ext.create('Ext.data.Store', {
                            model     : 'Generic',
                            autoLoad  : true,
                            proxy     : {
                                type        : 'ajax'
                                ,url        : _URL_ROLES_SISTEMA_X_PRIVILEGIOS
                                ,reader     :
                                {
                                    type  : 'json'
                                    ,root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.cdsisrol').setValue(_parametros.cdsisrol);
                                        
                                       var fieldPer = panelPersona.down('#fieldAgente');
                                       panelPersona.getForm().findField('params.cdusuari').clearInvalid();
                                       
                                       if(ROL_AGENTE != _parametros.cdsisrol){
                                           panelPersona.getForm().findField('params.cdusuari').setFieldLabel('Id Usuario');
                                           panelPersona.getForm().findField('params.cdusuari').maxLength = 30;
                                           panelPersona.getForm().findField('params.cdusuari').regex = /^[a-zA-Z0-9]+$/;
                                           panelPersona.getForm().findField('params.cdusuari').regexText = 'La clave del Usuario solo puede contener letras y n&uacute;meros';
                                           
                                           fieldPer.hide();
                                           panelPersona.getForm().findField('params.feini').allowBlank = true;
                                           panelPersona.getForm().findField('params.fefin').allowBlank = true;
                                           panelPersona.getForm().findField('params.dsapellido').allowBlank = false;
                                           panelPersona.getForm().findField('params.dsapellido1').allowBlank = false;
                                           panelPersona.getForm().findField('params.cdrfc').allowBlank = true;
                                           
                                             //se restringe a que  se permita campos sin llenar para no agentes
                                           panelPersona.getForm().findField('params.statusag').allowBlank =true;
                                           panelPersona.getForm().findField('params.claseag').allowBlank =true;
                                           panelPersona.getForm().findField('params.cdoficin').allowBlank =true;
                                           panelPersona.getForm().findField('params.cdbroker').allowBlank =true;
                                   
                                       }else {
                                           panelPersona.getForm().findField('params.cdusuari').setFieldLabel('Id Agente');
                                           panelPersona.getForm().findField('params.cdusuari').maxLength = 15;
                                           panelPersona.getForm().findField('params.cdusuari').regex = /^A[0-9]+$/;
                                           panelPersona.getForm().findField('params.cdusuari').regexText = 'La clave del Agente debe de comenzar con A y seguir de cualquier n&uacute;mero';
                                           
                                           fieldPer.show();
                                           panelPersona.getForm().findField('params.feini').allowBlank = false;
                                           panelPersona.getForm().findField('params.fefin').allowBlank = false;
                                           panelPersona.getForm().findField('params.dsapellido').allowBlank = true;
                                           panelPersona.getForm().findField('params.dsapellido1').allowBlank = true;
                                           panelPersona.getForm().findField('params.cdrfc').allowBlank = false;
                                           
                                              //se restringe a que no se permita campos sin llenar para agentes
                                           panelPersona.getForm().findField('params.statusag').allowBlank =false;
                                           panelPersona.getForm().findField('params.claseag').allowBlank =false;
                                           panelPersona.getForm().findField('params.cdoficin').allowBlank =false;
                                           panelPersona.getForm().findField('params.cdbroker').allowBlank =false;
                                       }
                                    }
                                }   
                            }
                        }),
                       listeners: {
                           select: function ( combo, records, eOpts ){
                               var cdrol = records[0].get('key');
                               
                               var fieldPer = panelPersona.down('#fieldAgente');
                               panelPersona.getForm().findField('params.cdusuari').clearInvalid();
                               
                               if(ROL_AGENTE != cdrol){
                                   panelPersona.getForm().findField('params.cdusuari').setFieldLabel('Id Usuario');
                                   panelPersona.getForm().findField('params.cdusuari').maxLength = 30;
                                   panelPersona.getForm().findField('params.cdusuari').regex = /^[a-zA-Z0-9]+$/;
                                   panelPersona.getForm().findField('params.cdusuari').regexText = 'La clave del Usuario solo puede contener letras y n&uacute;meros';
                                   
                                   fieldPer.hide();
                                   panelPersona.getForm().findField('params.cdramo').allowBlank = true;
                                   panelPersona.getForm().findField('params.feini').allowBlank = true;
                                   panelPersona.getForm().findField('params.fefin').allowBlank = true;
                                   panelPersona.getForm().findField('params.dsapellido').allowBlank = false;
                                   panelPersona.getForm().findField('params.dsapellido1').allowBlank = false;
                                   panelPersona.getForm().findField('params.cdrfc').allowBlank = true;
                                   //TODO: Agregar campos nuevos de agente
                                    
                                     //se restringe a que  se permita campos sin llenar para no agentes
                                   panelPersona.getForm().findField('params.statusag').allowBlank =true;
                                   panelPersona.getForm().findField('params.claseag').allowBlank =true;
                                   panelPersona.getForm().findField('params.cdoficin').allowBlank =true;
                                   panelPersona.getForm().findField('params.cdbroker').allowBlank =true;
                                   
                                   
                               }else {
                                   panelPersona.getForm().findField('params.cdusuari').setFieldLabel('Id Agente');
                                   panelPersona.getForm().findField('params.cdusuari').maxLength = 15;
                                   panelPersona.getForm().findField('params.cdusuari').regex = /^A[0-9]+$/;
                                   panelPersona.getForm().findField('params.cdusuari').regexText = 'La clave del Agente debe de comenzar con A y seguir de cualquier n&uacute;mero';
                                   
                                   fieldPer.show();
                                   panelPersona.getForm().findField('params.cdramo').allowBlank = false;
                                   panelPersona.getForm().findField('params.feini').allowBlank = false;
                                   panelPersona.getForm().findField('params.fefin').allowBlank = false;
                                   panelPersona.getForm().findField('params.dsapellido').allowBlank = true;
                                   panelPersona.getForm().findField('params.dsapellido1').allowBlank = true;
                                   panelPersona.getForm().findField('params.cdrfc').allowBlank = false;
                                   //TODO: Agregar campos nuevos de agente
                                   
                                      //se restringe a que no se permita campos sin llenar para agentes
                                   panelPersona.getForm().findField('params.statusag').allowBlank =false;
                                   panelPersona.getForm().findField('params.claseag').allowBlank =false;
                                   panelPersona.getForm().findField('params.cdoficin').allowBlank =false;
                                   panelPersona.getForm().findField('params.cdbroker').allowBlank =false;
                                   
                               }
                           }
                       }
                    },{
                        xtype      : 'textfield',
                        name       : 'params.cdusuari',
                        fieldLabel : 'Id Usuario',
                        allowBlank : false,
                        //maskRe   : /^[a-zA-Z0-9]+$/,
                        //regex      : /^[a-zA-Z0-9]+$/,
                        //regexText  : 'La clave del Usuario solo puede contener letras y n&uacute;meros',
                        value      : _parametros.cdusuario,
                        readOnly   : editMode
                    },{
                        xtype       : 'combo',
                        name        : 'params.cdmodgra',
                        fieldLabel  : '&iquest;Es Administrador?',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : Cat.Sino
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.cdmodgra').setValue(_parametros.cdmodgra);
                                    }else {
                                        panelPersona.getForm().findField('params.cdmodgra').setValue("N");
                                    }
                                }
                            }
                        })
                    },{
                        xtype       : 'combo',
                        name        : 'params.cdunieco',
                        fieldLabel  : 'Sucursal Da&ntilde;os',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'SUCURSALES_X_TIPORAMO',
                                    'params.idPadre'   : '2' 
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.cdunieco').setValue(_parametros.cdunieco);
                                    }
                                }
                            }
                        }),
                        listeners : {
                            /*select: function(combo, records){
                                // Recargamos el grid de Productos:
                                panelPersona.down('[name=params.cdramo]').getStore().load({
                                    params: {
                                        'params.idPadre': records[0].get('key')
                                    }
                                });
                                
                                // Asignamos valor a sucursal (onlyRead):
                                panelPersona.down('[name=params.cduniecoRuta]').setValue(records[0].get('key'));
                            },*/
                            change: function( field, newValue, oldValue, eOpts ) {
                                
                                debug('EVENTO CHANGE', newValue, oldValue);
                                
                                // Recargamos el grid de Productos:
                                panelPersona.down('[name=params.cdramo]').getStore().load({
                                    params: {
                                        'params.idPadre': newValue
                                    }
                                });
                                
                                // Asignamos valor a sucursal (onlyRead):
                                panelPersona.down('[name=params.cduniecoRuta]').setValue(newValue);
                                
                            }
                        }
                    },
                    
                    {
                        xtype       : 'combo',
                        name        : 'params.cdunisld',
                        fieldLabel  : 'Sucursal Salud',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : 'SUCURSALES_X_TIPORAMO',
                                    'params.idPadre'   : '10' 
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.cdunisld').setValue(_parametros.cdunisld);
                                    }
                                }
                            }
                        }),
                        listeners : {
                            /*select: function(combo, records){
                                // Recargamos el grid de Productos:
                                panelPersona.down('[name=params.cdramo]').getStore().load({
                                    params: {
                                        'params.idPadre': records[0].get('key')
                                    }
                                });
                                
                                // Asignamos valor a sucursal (onlyRead):
                                panelPersona.down('[name=params.cduniecoRuta]').setValue(records[0].get('key'));
                            },*/
                            change: function( field, newValue, oldValue, eOpts ) {
                                
                                debug('EVENTO CHANGE', newValue, oldValue);
                                
                                // Recargamos el grid de Productos:
                                panelPersona.down('[name=params.cdramo]').getStore().load({
                                    params: {
                                        'params.idPadre': newValue
                                    }
                                });
                                
                                // Asignamos valor a sucursal (onlyRead):
                                panelPersona.down('[name=params.cduniecoRuta]').setValue(newValue);
                                
                            }
                        }
                    },{
                        xtype       : 'combo',
                        name        : 'params.swsusmat',
                        fieldLabel  : '&iquest;Es suscriptor matriz?',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : Cat.Sino
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.swsusmat').setValue(_parametros.swsusmat);
                                    }else {
                                        panelPersona.getForm().findField('params.swsusmat').setValue("N");
                                    }
                                }
                            }
                        })
                    }
                    
                    ,{
                    	//Se agrega el combo para elegir Empresa:Todas (1) General de Salud (2),General de Seguros (3)
                    	xtype       : 'combo',
                        name        : 'params.cdempresa',
                        value       : editMode?_parametros.cdempresa : '',
                        fieldLabel  : 'Empresa',
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        anyMatch    : true,
                        store       : Ext.create('Ext.data.Store', {
                                       fields : [ 'key', 'value' ],
                                       data : [ 
                                       	        {'key':'' , 'value':'Todas' },
                                                {'key':'1', 'value':'General de Salud'}, 
                                                {'key':'2', 'value':'General de Seguros'}
                                               ]
                                    })
                                    /*,                                                       
                        listeners: {
                                afterrender:function (cmb){
                                        if(editMode){
                                            cmb.setValue(_parametros.cdempresa);
                                        }else{
                                            cmb.setValue('');
                                        }
                                 }
                        }*/
                    },{
                        xtype      : 'textfield',
                        name       : 'params.dsnombre',
                        fieldLabel : 'Nombre',
                        value      : _parametros.nombre,
                        allowBlank : false
                    },{
                        xtype      : 'textfield',
                        name       : 'params.dsnombre1',
                        fieldLabel : 'Segundo Nombre',
                        value      : _parametros.snombre
                    },{
                        xtype      : 'textfield',
                        name       : 'params.dsapellido',
                        fieldLabel : 'Apellido paterno',
                        allowBlank : false,
                        value      : _parametros.appat
                    },{
                        xtype      : 'textfield',
                        name       : 'params.dsapellido1',
                        fieldLabel : 'Apellido materno',
                        allowBlank : false,
                        value      : _parametros.apmat
                    },{
                        xtype       : 'combo',
                        name        : 'params.otsexo',
                        fieldLabel  : 'Sexo',
                        allowBlank  : false,
                        valueField  : 'key',
                        displayField: 'value',
                        forceSelection: true,
                        queryMode   :'local',
                        store       : Ext.create('Ext.data.Store', {
                            model : 'Generic',
                            autoLoad : true,
                            proxy : {
                                type : 'ajax',
                                url : _URL_CARGA_CATALOGO,
                                extraParams : {
                                    catalogo : _SEXO
                                },
                                reader : {
                                    type : 'json',
                                    root : 'lista'
                                }
                            },
                            listeners: {
                                load: function (){
                                    if(editMode){
                                        panelPersona.getForm().findField('params.otsexo').setValue(_parametros.sexo);
                                    }
                                }
                            }
                        })
                    },{
                        xtype      : 'datefield',
                        name       : 'params.fenacimi',
                        fieldLabel : 'Fecha de nacimiento',
                        format     : 'd/m/Y',
                        //value    : '01/01/1970',
                        maxValue   :  new Date(),
                        value      : _parametros.fecnac
                    },{
                        xtype      : 'textfield',
                        name       : 'params.cdrfc',
                        fieldLabel : 'RFC',
                        regex      : /^[a-zA-Z0-9]+$/,
                        regexText  : 'El RFC solo puede contener letras y n&uacute;meros',
                        value      : _parametros.rfc
                    },{
                        xtype      : 'textfield',
                        name       : 'params.curp',
                        fieldLabel : 'CURP',
                        regex      : /^[a-zA-Z0-9]+$/,
                        regexText  : 'El CURP solo puede contener letras y n&uacute;meros',
                        value      : _parametros.curp
                    },{
                        xtype      : 'textfield',
                        name       : 'params.dsemail',
                        allowBlank  : false,
                        fieldLabel : 'Email',
                        vtype      : 'email',
                        value      : _parametros.email
                    },{
                        xtype      : 'fieldset',
                        itemId     : 'fieldAgente',
                        padding    :  0,
                        border     : false,
                        defaults: {
                            width: 400
                        },
                        items      : [{
                                        xtype       : 'combo',
                                        name        : 'params.cdramo',
                                        fieldLabel  : 'Producto',
                                        allowBlank  : editMode,
                                        hidden      : editMode,
                                        valueField  : 'key',
                                        displayField: 'value',
                                        forceSelection: true,
                                        queryMode   :'local',
                                        store       : Ext.create('Ext.data.Store', {
                                            model : 'Generic',
                                            autoLoad : true,
                                            proxy : {
                                                type : 'ajax',
                                                url : _URL_CARGA_CATALOGO,
                                                extraParams : {
                                                    catalogo : Cat.Ramos
                                                },
                                                reader : {
                                                    type : 'json',
                                                    root : 'lista'
                                                }
                                            },
                                            listeners: {
                                                load: function (){
                                                	debug("_parametros.cdramo=",_parametros.cdramo);
                                                    if(editMode){                                                    	
                                                        panelPersona.getForm().findField('params.cdramo').setValue(_parametros.cdramo);
                                                    }
                                                }
                                            }
                                        })
                                    },{
                                        xtype      : 'datefield',
                                        name       : 'params.feini',
                                        fieldLabel : 'Inicio de Vigencia',
                                        value      : editMode ?  _parametros.feini : new Date()
                                    },{
                                        xtype      : 'datefield',
                                        name       : 'params.fefin',
                                        fieldLabel : 'Fin de Licencia',
                                        value      : _parametros.fefin
                                    },
                                           //////////////////////////////////////////////////////////////
                                            /////////////   Modificaciones para agregar Usuario/Agente
                                            ///////////     //////////////////////////////////////////////
                                                           //Se agregan los campos de tipo numberfield para Sucursal, Oficina y Promotoria.
                                    { 
                                        layout : 'hbox',
                                                   align: 'stretch',
                                                   border: false,
                                                   //title: 'Ruta:',
                                                   //width: 500,
                                                   height: 30,
                                                   
                                        
                                        items : [
                                                    {xtype: 'text',text: 'Ruta:',degrees:0,width:105},
                                                    {xtype: 'displayfield', name: 'params.cduniecoRuta', fieldLabel: 'Suc',readOnly:true,labelWidth: 25, width: 70},
                                                    {xtype: 'numberfield', name: 'params.cdoficin',   fieldLabel: 'Ofic',/*allowBlank:false*/labelWidth:30,  width: 110,maxLength:5,minValue: 0,value: _parametros.cdoficin},
                                                    {xtype: 'numberfield', name: 'params.cdbroker', fieldLabel: 'Prom',/*allowBlank:false*/labelWidth: 30, width: 115,maxLength:5,minValue: 0,value: _parametros.cdbroker}
                                                ]
                                    },{                                                     
                                                  // Se agrega el Combo para elegir Clase, 
                                            xtype       : 'combo',
                                            name        : 'params.claseag',
                                            value      : _parametros.claseag,
                                            //readOnly      : editMode,
                                            //allowBlank    : editMode,
                                            //hidden        : editMode,
                                            fieldLabel  : 'Clase',
                                            //allowBlank  : false,
                                            valueField  : 'key',
                                            displayField: 'value',
                                            forceSelection: false,
                                            queryMode   :'local',
                                            anyMatch    : true,
                                            store       : Ext.create('Ext.data.Store', {
                                                           model : 'Generic',
                                                           autoLoad : true,                         
                                                           fields : [ 'id', 'genre_name' ],
                                                            data : [ 
                                                                     [ '1', 'Agente dependiente de promotor\u00eda o sucursal' ],
                                                                     [ '2', 'Promotor con agentes' ], 
                                                                     [ '3', 'Agente libre, no depende de promotor\u00eda ni sucursal' ], 
                                                                     [ '4', 'Promotor sin agentes' ]
                                                                   ]
                                                                    
                                                
                                                        })
                                                        
                                                        ,                                                       
                                                        listeners: {
                                                                load: function (){
                                                                    if(editMode){
                                                                        panelPersona.getForm().findField('params.claseag').setValue(_parametros.claseag);
                                                                            }
                                                                        }
                                                                    }
                                                                    
                                            
                                        },{      // Se agrega el Combo para elegir Estatus, 
                                            xtype       : 'combo',
                                            name        : 'params.statusag',
                                            fieldLabel  : 'Estatus',
                                            value      : _parametros.statusag,
                                            //allowBlank  : false,
                                            valueField  : 'key',
                                            displayField: 'value',
                                            forceSelection: false,
                                            queryMode   :'local',
                                            anyMatch    : true,
                                            store       : Ext.create('Ext.data.Store', {
                                                           model : 'Generic',
                                                           autoLoad : true,                         
                                                           fields : [ 'id', 'genre_name' ],
                                                           data : [ 
                                                                     [ 'C', 'Cancelado' ],
                                                                     [ 'D', 'Definitivo (agente activo con todos los permisos)' ], 
                                                                     [ 'F', 'Finado (cancelado)' ], 
                                                                     [ 'I', 'Inhabilitado (Activo con algunos permisos como consultas y pagos, pero no para emitir)' ],
                                                                     [ 'R', 'Tratar como cancelado' ]
                                                                  ] 
                                                                    
                                                
                                                        })
                                                        
                                                        ,
                                            listeners: {
                                                    load: function (){
                                                        if(editMode){
                                                            panelPersona.getForm().findField('params.statusag').setValue(_parametros.statusag);
                                                                }
                                                            }
                                                        }
                                                        
                                            
                                        }]
                    }/*,
                    dsPassword,
                    confirmPassword*/

        ],
        buttonAlign: 'center',
        buttons: [{
            text: 'Guardar',
            icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
            handler: function(btn, e) {
                var form = this.up('form').getForm();
                
                if (form.isValid()) {
                    
                    var msjeConfirmaGuardadoUsuario;
                    if(editMode){
                        msjeConfirmaGuardadoUsuario = '&iquest;Esta seguro que desea actualizar este usuario?';
                    }else{
                        msjeConfirmaGuardadoUsuario = '&iquest;Esta seguro que desea crear este usuario?';
                    }
                    
                    Ext.Msg.show({
                        title: 'Confirmar acci&oacute;n',
                        msg: msjeConfirmaGuardadoUsuario,
                        buttons: Ext.Msg.YESNO,
                        fn: function(buttonId, text, opt) {
                            if(buttonId == 'yes') {
                                
                                //debug('VALORES DEL GUARDADO: ', form.getValues());
                                
                                form.submit({
                                    waitMsg:'Procesando...',                        
                                    failure: function(form, action) {
                                        switch (action.failureType) {
                                            case Ext.form.action.Action.CONNECT_FAILURE:
                                                Ext.Msg.show({title: 'Error', msg: 'Error de comunicaci&oacute;n', buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
                                                break;
                                            case Ext.form.action.Action.SERVER_INVALID:
                                            case Ext.form.action.Action.LOAD_FAILURE:
                                                 var msgServer = Ext.isEmpty(action.result.errorMessage) ? 'Error interno del servidor, verifique su sesi&oacute;n' : action.result.errorMessage;
                                                 Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
                                                break;
                                        }
                                    },
                                    success: function(form, action) {
                                        recargaGridUsuarios();
                                        windowLoader.close();
                                        mensajeCorrecto('\u00C9xito', 'El usuario se guard\u00F3 correctamente', Ext.Msg.OK, Ext.Msg.INFO);
                                        form.reset();
                                    }
                                });
                                
                            }
                        },
                        animateTarget: btn,
                        icon: Ext.Msg.QUESTION
                    });
                } else {
                    Ext.Msg.show({
                        title: 'Aviso',
                        msg: 'Complete la informaci&oacute;n requerida',
                        buttons: Ext.Msg.OK,
                        animateTarget: btn,
                        icon: Ext.Msg.WARNING
                    });
                }
            }
        },
        {
                text: 'Cancelar',
                icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
                handler: function(btn, e) {
                    windowLoader.close();
                }
        }]
    });
    
      
});                