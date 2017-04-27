<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Administracion de Usuarios y Agentes</title>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var busquedaUsuariosStore;
var windowLoader;
var recargaGridUsuarios;

var _UrlBusquedaUsuarios        = '<s:url namespace="/catalogos"    action="busquedaUsuarios" />';
var _URL_LOADER_NUEVO_USUARIO   = '<s:url namespace="/catalogos"    action="includes/agregaUsuarios" />';
var _URL_LOADER_EDITAR_ROLES    = '<s:url namespace="/catalogos"    action="includes/editarRolesUsuario" />';
var URL_ASIGNAR_IMPRESORA    = '<s:url namespace="/catalogos"    action="includes/asignarImpresorasUsuario" />';
var URL_NUEVA_IMPRESORA    = '<s:url namespace="/catalogos"    action="includes/nuevaImpresora" />';
var _URL_LOADER_EDITAR_PRODUCTOS= '<s:url namespace="/catalogos"    action="includes/editarProductosAgente" />';
var _UrlActivarDesactivarUsuario= '<s:url namespace="/catalogos"    action="activaDesactivaUsuario" />';
var _UrlCambiaPasswordUsuario   = '<s:url namespace="/catalogos"    action="cambiarPasswordUsuario" />';
var _UrlEliminaPasswordUsuario   = '<s:url namespace="/catalogos"    action="eliminaUsuarioLDAP" />';
var _MSG_SIN_DATOS              = 'No hay datos';
var _MSG_BUSQUEDA_SIN_DATOS     = 'No hay datos para la b\u00FAsqueda actual.';

/*///////////////////*/
////// variables //////
///////////////////////
///////////////////////
////// funciones //////
/*///////////////////*/
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{
    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });
    
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
    
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('gridUsuariosModel',
    {
        extend : 'Ext.data.Model'
        ,fields :
        [
            <s:property value="fields" />
        ]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    busquedaUsuariosStore = Ext.create('Ext.data.Store',
    {
        pageSize : 20,
        autoLoad : true
        ,model   : 'gridUsuariosModel'
        ,proxy   :
        {
            type         : 'memory'
            ,enablePaging : true
            ,reader      : 'json'
            ,data        : []
        }
    });
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    var panelUsuarios = Ext.create('Ext.form.Panel',
            {
        title : 'B&uacute;squeda de Usuarios/Agentes'
        ,layout :
            {
            type : 'table'
            ,columns : 2
            }
        ,items :
        [
            <s:property value="items" />
        ]
        ,buttonAlign: 'center'
        ,buttons       :
            [
                {
                    text     : 'Buscar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                    ,handler : function()
                    {
                        var form=this.up().up();
                        if(form.isValid())
                        {
                            recargaGridUsuarios();
                        }
                        else
                        {
                            Ext.Msg.show(
                            {
                                title    : 'Datos imcompletos'
                                ,icon    : Ext.Msg.WARNING
                                ,msg     : 'Favor de llenar los campos requeridos'
                                ,buttons : Ext.Msg.OK
                            });
                        }
                    }
                },
                {
                    text     : 'Limpiar'
                    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                    ,handler : function()
                    {
                        this.up().up().getForm().reset();
                    }
                }
            ]
    });
    
    
    var gridUsuarios = Ext.create('Ext.grid.Panel',
        {
        title : 'Usuarios/Agentes'
        ,height : 350
        ,selType: 'checkboxmodel'
        ,store : busquedaUsuariosStore
        ,columns :
        [
            <s:property value="columns" />
        ]
        ,bbar     :
        {
            displayInfo : true,
            store       : busquedaUsuariosStore,
            xtype       : 'pagingtoolbar',
            id: 'usuariosbbar'
            
        },
        
        //tbar: []
        dockedItems:[
                     {
                         xtype:'toolbar',
                         items:[{
                             icon    : '${ctx}/resources/fam3icons/icons/add.png',
                             text    : 'Agregar usuario',
                             handler : function()
                             {
                                 windowLoader = Ext.create('Ext.window.Window',
                                 {
                                     title        : 'Agregar Usuario'
                                     ,modal       : true
                                     ,buttonAlign : 'center'
                                     ,width       : 510
                                     ,height      : 680
                                     ,autoScroll  : true
                                     ,loader      :
                                     {
                                         url       : _URL_LOADER_NUEVO_USUARIO
                                         ,scripts  : true
                                         ,autoLoad : true
                                         ,loadMask : true
                                         ,ajaxOptions: {
                                             method   : 'POST'
                                         },
                                         params: {
                                             'params.edit' : 'N'
                                         }
                                     }
                                 }).show();
                             }
                         },{
                             icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
                             text    : 'Editar usuario',
                             handler : function()
                             {
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var record = model.getLastSelected();
                                     
                                     var dsusuario = record.get('dsUsuario');
                                     var nombre = record.get('dsNombre');
                                     var snombre = record.get('dsNombre1');
                                     var appat = record.get('dsApellido');
                                     var apmat = record.get('dsApellido1');
                                     
                                     windowLoader = Ext.create('Ext.window.Window',
                                             {
                                                 title        : 'Editar Usuario'
                                                 ,modal       : true
                                                 ,buttonAlign : 'center'
                                                 ,width       : 510
                                                 ,height      : 680
                                                 ,autoScroll  : true
                                                 ,loader      :
                                                 {
                                                     url       : _URL_LOADER_NUEVO_USUARIO
                                                     ,scripts  : true
                                                     ,autoLoad : true
                                                     ,loadMask : true
                                                     ,ajaxOptions: {
                                                         method   : 'POST'
                                                     },
                                                     params: {
                                                         'params.edit' : 'S',
                                                         'params.cdsisrol': record.get('cdrol'),
                                                         'params.cdmodgra': record.get('esAdmin'),
                                                         'params.cdunieco': record.get('cdunieco'),
                                                         'params.cdunisld': record.get('cdunisld'),
                                                         'params.swsusmat': record.get('swsusmat'),
                                                         'params.cdusuario': record.get('cdUsuario'),
                                                         'params.nombre': nombre,
                                                         'params.snombre': snombre,
                                                         'params.appat': appat,
                                                         'params.apmat': apmat,
                                                         'params.sexo': record.get('otSexo'),
                                                         'params.fecnac': record.get('feNacimi'),
                                                         'params.rfc': record.get('cdrfc'),
                                                         'params.curp': record.get('curp'),
                                                         'params.email': record.get('dsEmail'),
                                                         'params.feini': record.get('feini'),
                                                         'params.fefin': record.get('fefinlic'),
                                                         
                                                         //Se agregar'an los par'ametros nuevos que debe mostrar la etiqueta Editar usuario
                                                          'params.statusag': record.get('statusag'),
                                                          'params.claseag': record.get('claseag'),
                                                          'params.cdbroker': record.get('cdbroker'),
                                                          'params.cdoficin': record.get('cdoficin'),
                                                          //'params.cduniecoRuta': record.get('cduniecoRuta')
                                                          'params.cdempresa': record.get('cdempresa')
                                                     }
                                                 }
                                             }).show();
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },{
                             icon    : '${ctx}/resources/fam3icons/icons/flag_yellow.png',
                             text    : 'Alta/Baja de usuario',
                             scope   : this,
                             handler : function (btn, e){
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var usuarioActivo = ("S" == model.getLastSelected().get('swActivo') || "s" == model.getLastSelected().get('swActivo')) ? true:false;
                                     Ext.Msg.show({
                                         title: 'Aviso',
                                         msg: usuarioActivo? '&iquest;Esta seguro que desea dar de Baja este usuario?': '&iquest;Esta seguro que desea dar de Alta este usuario?',
                                         buttons: Ext.Msg.YESNO,
                                         fn: function(buttonId, text, opt) {
                                             if(buttonId == 'yes') {
                                                 
                                                 Ext.Ajax.request({
                                                     url: _UrlActivarDesactivarUsuario,
                                                     params: {
                                                             'params.PV_CDUSUARI_I'  : model.getLastSelected().get('cdUsuario'),
                                                             'params.PV_SWACTIVO_I' : usuarioActivo? 'N':'S'
                                                     },
                                                     success: function(response, opt) {
                                                         var jsonRes=Ext.decode(response.responseText);

                                                         if(jsonRes.success == true){
                                                             mensajeCorrecto('Aviso','Se ha cambiado el estatus correctamente.');
                                                             recargaGridUsuarios();                                  
                                                         }else {
                                                             mensajeError('No se pudo cambiar el estatus del usuario.');
                                                         }
                                                     },
                                                     failure: function(){
                                                         mensajeError('No se pudo cambiar el estatus del usuario.');
                                                     }
                                                 });
                                             }
                                         },
                                         animateTarget: btn,
                                         icon: Ext.Msg.QUESTION
                                     });
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },'-',{
                             icon    : '${ctx}/resources/fam3icons/icons/key_add.png',
                             text    : 'Cambiar Contrase&ntilde;a',
                             hidden  : true,
                             handler : function()
                             {
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var record = model.getLastSelected();
                                     
                                     var nuevoPass = new Ext.form.TextField({
                                         id:'newpassword',
                                         fieldLabel: 'Nueva Contrase&ntilde;a',
                                         inputType: 'password',
                                         name: 'newpassword',
                                         allowBlank  : false,
                                         blankText:'La contrase&ntilde;a es un dato requerido',
                                         minLength: 5,
                                         minLengthText: 'La contrase&ntilde;a debe tener almenos 5 caracteres.',
                                         listeners:{
                                             scope:this,
                                             change: function(field) {
                                                 var confirmField = field.up('form').down('[name=newPasswordConfirm]');
                                                 confirmField.validate();
                                             }
                                         }
                                     });

                                     var confirmNuevoPass = new Ext.form.TextField({
                                         id:'confirmNewpassword',
                                         fieldLabel: 'Confirme la nueva Contrase&ntilde;a',
                                         inputType: 'password',
                                         vtype: 'password',
                                         name: 'newPasswordConfirm',
                                         allowBlank  : false,
                                         blankText:'La confirmaci&oacute;n de la contrase&ntilde;a es un dato requerido',
                                         initialPassField: 'newpassword', // id del campo password inicial
                                         listeners:{
                                             scope:this
                                         }
                                     });
                                     
                                     var windowPass;
                                     
                                     var panelPassword = Ext.create('Ext.form.Panel', {
                                         url: _UrlCambiaPasswordUsuario,
                                         border: false,
                                         bodyStyle:'padding:5px 0px 0px 40px;',
                                         items    : [{
                                                         xtype      : 'hidden',
                                                         name       : 'user',
                                                         value      : record.get('cdUsuario')
                                                     },
                                                     nuevoPass,
                                                     confirmNuevoPass
                                         ],
                                         buttonAlign: 'center',
                                         buttons: [{
                                             text: 'Actualizar',
                                             icon    : '${ctx}/resources/fam3icons/icons/disk.png',
                                             handler: function(btn, e) {
                                                 var form = this.up('form').getForm();
                                                 if (form.isValid()) {
                                                     
                                                     Ext.Msg.show({
                                                         title: 'Confirmar acci&oacute;n',
                                                         msg: '&iquest;Esta seguro que desea actualizar la Contrase&ntilde;a de este usuario?',
                                                         buttons: Ext.Msg.YESNO,
                                                         fn: function(buttonId, text, opt) {
                                                             if(buttonId == 'yes') {
                                                                 form.submit({
                                                                     waitMsg:'Procesando...',                        
                                                                     failure: function(form, action) {
                                                                         showMessage('Error', action.result.errorMessage, Ext.Msg.OK, Ext.Msg.ERROR);
                                                                     },
                                                                     success: function(form, action) {
                                                                         form.reset();
                                                                         windowPass.close();
                                                                         mensajeCorrecto('\u00C9xito', 'La Contrase&ntilde;a se actializ&oacute; correctamente.', Ext.Msg.OK, Ext.Msg.INFO);
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
                                                 icon    : '${ctx}/resources/fam3icons/icons/cancel.png',
                                                 handler: function(btn, e) {
                                                     windowPass.close();
                                                 }
                                         }]
                                     });
                                     
                                     windowPass = Ext.create('Ext.window.Window',
                                             {
                                                 title        : 'Cambiar Contrase&ntilde;a del usuario: ' + record.get('cdUsuario')
                                                 ,modal       : true
                                                 ,buttonAlign : 'center'
                                                 ,width       : 410
                                                 ,height      : 170
                                                 ,autoScroll  : true
                                                 ,items: [panelPassword]
                                             }).show();
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },{
                             icon    : '${ctx}/resources/fam3icons/icons/arrow_rotate_anticlockwise.png',
                             text    : 'Reiniciar Contrase&ntilde;a',
                             handler : function()
                             {
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var record = model.getLastSelected();
                                     
                                     Ext.Msg.show({
                                         title: 'Confirmar acci&oacute;n',
                                         msg: '&iquest;Esta seguro que desea reiniciar la contrase&ntilde;a de este usuario?',
                                         buttons: Ext.Msg.YESNO,
                                         fn: function(buttonId, text, opt) {
                                             if(buttonId == 'yes') {
                                                 
                                                 Ext.Ajax.request({
                                                     url: _UrlEliminaPasswordUsuario,
                                                     params: {
                                                             'user'  : model.getLastSelected().get('cdUsuario')
                                                     },
                                                     success: function(response, opt) {
                                                         var jsonRes=Ext.decode(response.responseText);

                                                         if(jsonRes.success == true){
                                                             mensajeCorrecto('Aviso','Se ha reiniciado la contrase&ntilde;a correctamente.\n ' +
                                                                             'Se solicitar&aacute; una nueva contrase&ntilde;a al usuario al reingresar al sistema.');
                                                         }else {
                                                             mensajeError('No se pudo reiniciar la contrase&ntilde;a del usuario.');
                                                         }
                                                     },
                                                     failure: function(){
                                                         mensajeError('No se pudo reiniciar la contrase&ntilde;a del usuario.');
                                                     }
                                                 });
                                             }
                                         },
                                         icon: Ext.Msg.QUESTION
                                     });
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },'-',{
                             icon    : '${ctx}/resources/fam3icons/icons/user_edit.png',
                             text    : 'Ver/Editar Roles',
                             handler : function()
                             {
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var record = model.getLastSelected();
                                     /*
                                     if('EJECUTIVOCUENTA' == record.get('cdrol')){
                                         mensajeWarning('No se pueden editar los roles para un Agente.');
                                         return;
                                     }*/
                                     windowLoader = Ext.create('Ext.window.Window',
                                             {
                                                 title        : 'Ver/Editar Roles del usuario: ' + record.get('cdUsuario')
                                                 ,modal       : true
                                                 ,buttonAlign : 'center'
                                                 ,width       : 500
                                                 ,height      : 350
                                                 ,autoScroll  : true
                                                 ,loader      :
                                                 {
                                                     url       : _URL_LOADER_EDITAR_ROLES
                                                     ,scripts  : true
                                                     ,autoLoad : true
                                                     ,loadMask : true
                                                     ,ajaxOptions: {
                                                         method   : 'POST'
                                                     },
                                                     params: {
                                                         'params.cdusuario': record.get('cdUsuario'),
                                                         'params.esAgente':  ('EJECUTIVOCUENTA' == record.get('cdrol')) ? 'S':'N'
                                                     }
                                                 }
                                             }).show();
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },'-',{
                             icon    : '${ctx}/resources/fam3icons/icons/database_add.png',
                             text    : 'Productos del Agente',
                             handler : function()
                             {
                                 var model =  gridUsuarios.getSelectionModel();
                                 if(model.hasSelection()){
                                     var record = model.getLastSelected();
                                     
                                     if('EJECUTIVOCUENTA' != record.get('cdrol')){
                                         mensajeWarning('No se pueden editar los productos para un usuario que no sea Agente.');
                                         return;
                                     }
                                     windowLoader = Ext.create('Ext.window.Window',
                                             {
                                                 title        : 'Ver/Editar Productos del usuario: ' + record.get('cdUsuario')
                                                 ,modal       : true
                                                 ,buttonAlign : 'center'
                                                 ,width       : 500
                                                 ,height      : 350
                                                 ,autoScroll  : true
                                                 ,loader      :
                                                 {
                                                     url       : _URL_LOADER_EDITAR_PRODUCTOS
                                                     ,scripts  : true
                                                     ,autoLoad : true
                                                     ,loadMask : true
                                                     ,ajaxOptions: {
                                                         method   : 'POST'
                                                     },
                                                     params: {
                                                         'params.cdagente': record.get('cdUsuario')
                                                     }
                                                 }
                                             }).show();
                                     
                                 }else{
                                     showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 }
                             }
                         },
                         '-',
                         {
                         	icon    : '${ctx}/resources/images/printButton.png',
                             text    : 'Asignar Impresoras',
                             handler : function(){
                             	 var model =  gridUsuarios.getSelectionModel();
                             	 
                                  if(!model.hasSelection()){
                                 	 showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                                 	 return;
                                  }
                             	 var record = model.getLastSelected();
                             	 
                             	windowLoader = Ext.create('Ext.window.Window',
                                         {
                                             title        : 'Asignar impresoras al usuario: ' + record.get('cdUsuario')
                                             ,itemId		 : 'asignaImp'
                                             ,modal       : true
                                             ,buttonAlign : 'center'
                                             ,width       : 500
                                             ,height      : 350
                                             ,autoScroll  : true
                                             ,loader      :
                                             {
                                                 url       : URL_ASIGNAR_IMPRESORA
                                                 ,scripts  : true
                                                 ,autoLoad : true
                                                 ,loadMask : true
                                                 ,ajaxOptions: {
                                                     method   : 'POST'
                                                 },
                                                 params: {
                                                     'params.cdusuario': record.get('cdUsuario')
                                                     
                                                 },
                                                 callback: function(records, operation, success) {
                                     	    		windowLoader.setLoading(false);// Aqui se desactiva el loading de la ventana
                                     	        }
                                             }
                                         }).show();
                             	        windowLoader.setLoading(true);//Aqui se activa el loading de la ventana
                             	
                             }
                         },
                         
                         ,'->',{
                             icon    : '${ctx}/resources/fam3icons/icons/award_star_silver_2.png',
                             text    : 'Creaci&oacute;n de Roles',
                             hidden  : true,
                             handler : function(){
                                         crearEditarRoles();
                                     }
                         }]
                     },
                     {
                         xtype:'toolbar',
                         items:[{
                         	icon    : '${ctx}/resources/images/printButton.png',
                            text    : 'Agregar/Editar Impresoras',
                            handler : function(){
                            	 var model =  gridUsuarios.getSelectionModel();
                            	 
                                 
                            	 var record = model.getLastSelected();
                            	 
                            	windowLoader = Ext.create('Ext.window.Window',
                                        {
                                            title        : 'Agregar/Editar Impresoras' 
                                            ,itemId		 : 'asignaImp'
                                            ,modal       : true
                                            ,buttonAlign : 'center'
                                            ,width       : 500
                                            ,height      : 350
                                            ,autoScroll  : true
                                            ,loader      :
                                            {
                                                url       : URL_NUEVA_IMPRESORA
                                                ,scripts  : true
                                                ,autoLoad : true
                                                ,loadMask : true
                                                ,ajaxOptions: {
                                                    method   : 'POST'
                                                },
                                                params: {
                                                    'params.cdusuario': null
                                                    
                                                },
                                                callback: function(records, operation, success) {
                                    	    		windowLoader.setLoading(false);// Aqui se desactiva el loading de la ventana
                                    	        }
                                            }
                                        }).show();
                            	        windowLoader.setLoading(true);//Aqui se activa el loading de la ventana
                            	
                            }
                        }]
                     }
                 ]
    });
    
    
    
    
    
   
        
        
    Ext.create('Ext.panel.Panel',{
        defaults : {
            style : 'margin : 5px;'
        }
        ,renderTo : 'mainDiv'
        ,items :
        [panelUsuarios,gridUsuarios]
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    recargaGridUsuarios = function(){
        
        gridUsuarios.setLoading(true);
        Ext.getCmp('usuariosbbar').moveFirst();
        
        cargaStorePaginadoLocal(busquedaUsuariosStore, 
                _UrlBusquedaUsuarios, 
                'usuarios', 
                panelUsuarios.getValues(), 
                function (options, success, response){
            
            if(success){
                var jsonResponse = Ext.decode(response.responseText);
                if(jsonResponse.usuarios && jsonResponse.usuarios.length == 0) {
                    //showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                }
                
            }else{
                showMessage('Error', 'Error al obtener los datos.', 
                    Ext.Msg.OK, Ext.Msg.ERROR);
            }
            gridUsuarios.setLoading(false);
        });
    };
    
    
    function crearEditarRoles(){
        
        var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
        var _CAT_ROLES_SISTEMA  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_SISTEMA"/>';
        var _UrlCrearEditarRol  = '<s:url namespace="/catalogos"    action="creaEditaRolSistema" />';
        
        var recargaGridCreaRoles;
        
        var creaRolesStore = Ext.create('Ext.data.Store',
                {
                    pageSize : 20,
                    autoLoad : true
                    ,model   : 'Generic'
                    ,proxy   :
                    {
                        type         : 'memory'
                        ,enablePaging : true
                        ,reader      : 'json'
                        ,data        : []
                    }
                });
        
        var gridCreaRoles = Ext.create('Ext.grid.Panel',
                {
                title : 'Roles existentes en la aplicaci&oacute;n'
                ,height : 300
                ,selType: 'checkboxmodel'
                ,store : creaRolesStore
                ,columns :
                [ { header     : 'C&oacute;digo del Rol' , dataIndex : 'key', flex: 2},
                  { header     : 'Nombre del Rol' , dataIndex : 'value', flex: 5}
                ]
                ,bbar     :
                {
                    displayInfo : true,
                    store       : creaRolesStore,
                    xtype       : 'pagingtoolbar'
                },
                tbar: [{
                    icon    : '${ctx}/resources/fam3icons/icons/add.png',
                    text    : 'Crear Rol',
                    handler : function(){crearEditarRol();}
                },{
                    icon    : '${ctx}/resources/fam3icons/icons/pencil.png',
                    text    : 'Editar Rol',
                    handler : function()
                    {
                        var model =  gridCreaRoles.getSelectionModel();
                        if(model.hasSelection()){
                            var record = model.getLastSelected();
                            crearEditarRol(record);
                            
                        }else{
                            showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                        }
                    }
                }]
            });
        
        
            var windowCrearRoles = Ext.create('Ext.window.Window', {
                  title: 'Creci&oacute;n de Roles',
                  modal:true,
                  height : 450,
                  width  : 800,
                  items:[{
                    layout: {type:'hbox', pack: 'center'},
                    defaults: {
                        style: 'margin: 10px'// para hacer que los componentes se separen 5px
                    },
                    border: false,
                    items: [{
                        xtype: 'textfield',
                        fieldLabel: 'Nombre del Rol:',
                        name: 'dsCreaEditaRol',
                        labelWidth: 120
                    },{
                        xtype: 'button',
                        text: 'Buscar Rol',
                        handler: function(){
                            recargaGridCreaRoles();
                        }
                    }]
                    },gridCreaRoles],
                  bodyStyle:'padding:15px;',
                  buttons:[
                   {
                         text: 'Aceptar',
                         icon:'${ctx}/resources/fam3icons/icons/accept.png',
                         handler: function() {
                             windowCrearRoles.close();
                         }
                   }
                  ]
                  });
                windowCrearRoles.show();
                
                recargaGridCreaRoles = function(){
                    
                    var params = {
                        'params.dsRol' : windowCrearRoles.down('[name=dsCreaEditaRol]').getValue(),
                        'catalogo'     : _CAT_ROLES_SISTEMA
                    };
                    cargaStorePaginadoLocal(creaRolesStore, _URL_CARGA_CATALOGO, 'lista', params, function (options, success, response){
                        if(success){
                            var jsonResponse = Ext.decode(response.responseText);
                            
                            if(!jsonResponse.success) {
                                showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                            }
                        }else{
                            showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
                        }
                    }, gridCreaRoles);
                };
                
                function crearEditarRol(record){

                    var panelCrearEditarRol = Ext.create('Ext.form.Panel',{
                        border  : false
                        ,bodyStyle:'padding:5px;'
                        ,url: _UrlCrearEditarRol
                        ,items :
                        [   {
                                xtype      : 'hidden',
                                name       : 'params.pv_accion_i',
                                value      : record? 'U' : 'I'
                            },{
                                xtype      : 'textfield'
                                ,fieldLabel : 'C&oacute;digo del Rol'
                                ,labelWidth: 120
                                ,width: 350
                                ,allowBlank:false
                                ,readOnly: record?true:false
                                ,name       : 'params.pv_cdsisrol_i'
                                ,value: record? record.get('key') : ''
                            },{
                                xtype      : 'textfield'
                                ,fieldLabel : 'Nombre del Rol'
                                ,labelWidth: 120
                                ,width: 350
                                ,allowBlank:false
                                ,name       : 'params.pv_dssisrol_i'
                                ,value: record? record.get('value') : ''
                            
                            }
                        ]
                    });
                    
                     var windowCrearEditarRol = Ext.create('Ext.window.Window', {
                          title: record?'Editar Rol':'Crear Rol',
                          closeAction: 'hide',
                          modal:true,
                          items:[panelCrearEditarRol],
                          bodyStyle:'padding:15px;',
                          buttons:[{
                                 text: 'Guardar',
                                 icon: '${ctx}/resources/fam3icons/icons/accept.png',
                                 handler: function() {
                                       if (panelCrearEditarRol.form.isValid()) {
                                           
                                           panelCrearEditarRol.form.submit({
                                            waitMsg:'Procesando...',            
                                            failure: function(form, action) {
                                                mensajeError("Error al guardar el Rol");
                                            },
                                            success: function(form, action) {
                                                
                                                recargaGridCreaRoles();
                                                panelCrearEditarRol.getForm().reset();
                                                windowCrearEditarRol.close();
                                                mensajeCorrecto("Aviso","Se ha guardado el Rol.");
                                                
                                                
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
                             },
                           {
                                 text: 'Cancelar',
                                 icon: '${ctx}/resources/fam3icons/icons/cancel.png',
                                 handler: function() {
                                     panelCrearEditarRol.getForm().reset();
                                     windowCrearEditarRol.close();
                                 }
                           }
                          ]
                          });
                     windowCrearEditarRol.show();
                }
        
    }

});
</script>

</head>
<body>
<div id="mainDiv" style="height:600px;"></div>
</body>
</html>