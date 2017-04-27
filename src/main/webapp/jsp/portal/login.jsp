<%@ page language="java" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
	    <title>ICE ${ctx}</title>
	    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
	    <script language="javascript">
	        var _CONTEXT = "${ctx}";
	        var _URL_VALIDA_USUARIO =                   '<s:url namespace="/seguridad" action="autenticaUsuario" />';
	        var _URL_VALIDA_EXISTE_USUARIO =            '<s:url namespace="/seguridad" action="existeUsuarioLDAP" />';
	        //var _GLOBAL_URL_GRABAR_EVENTO = '<s:url namespace="/servicios" action="grabarEvento" />';
	        //funcion para revisar si estas en un iframe
	        //http://stackoverflow.com/questions/326069/how-to-identify-if-a-webpage-is-being-loaded-inside-an-iframe-or-directly-into-t
	        function inIframe() {
	            try {
	                return window.self !== window.top;
	            } catch (e) {
	                return true;
	            }
	        }
	        if (inIframe()) {
	            try {
	                stop = true;
	                window.top.location = window.location;
	            } catch (e) {
	                alert('Error');
	                window.location='error';
	            }
	        }
	    </script>
	    <link href="${ctx}/resources/extjs4/resources/my-custom-theme/my-custom-theme-all.css" rel="stylesheet" type="text/css" />
	    <link href="${ctx}/resources/extjs4/extra-custom-theme.css" rel="stylesheet" type="text/css" />
	    <script type="text/javascript" src="${ctx}/resources/extjs4/ext-all.js"></script>
	    <script type="text/javascript" src="${ctx}/resources/extjs4/locale/ext-lang-es.js?${now}"></script>
	    <script type="text/javascript">
	    
    	    Ext.onReady(function(){
    
                //_grabarEvento('SEGURIDAD','ACCLOGIN');
            
                Ext.apply(Ext.form.field.VTypes, {
                    
                    password: function(val, field) {
                        if (field.initialPassField) {
                            var pwd = field.up('form').down('#' + field.initialPassField);
                            return (val == pwd.getValue());
                        }
                        return true;
                    },
            
                    passwordText: 'Las contrase\u00F1as no coinciden.'
                });
            
                var dsUser = new Ext.form.TextField({
                    id:'user',
                    fieldLabel: 'Usuario',
                    name: 'user',
                    allowBlank: false,
                    blankText:'El nombre del usuario es un dato requerido',
                    listeners:{
                        scope:this,
                        specialkey: function(f,e){
                            if(e.getKey()==e.ENTER){                    
                                validarUsuario();
                            }
                        }
                    }
                });
                
                var dsPassword = new Ext.form.TextField({
                    id:'password',
                    fieldLabel: 'Contrase\u00F1a',
                    inputType: 'password',
                    name: 'password',
                    allowBlank: false,
                    blankText:'La contrase\u00F1a es un dato requerido',
                    listeners:{
                        scope:this,
                        specialkey: function(f,e){
                            if(e.getKey()==e.ENTER){                    
                                validarUsuario();
                            }
                        },
                        change: function(field) {
                            var confirmField = field.up('form').down('[name=passwordConfirm]');
                            confirmField.validate();
                        }
                    }
                });
            
                var confirmPassword = new Ext.form.TextField({
                    id:'confirmPassword',
                    fieldLabel: 'Confirme su Contrase\u00F1a',
                    inputType: 'password',
                    vtype: 'password',
                    name: 'passwordConfirm',
                    allowBlank: false,
                    blankText:'La confirmaci\u00F3n de la contrase\u00F1a es un dato requerido',
                    initialPassField: 'password',
                    hidden: true,
                    disabled: true,
                    listeners:{
                        scope:this,
                        specialkey: function(f,e){
                            if(e.getKey()==e.ENTER){                    
                                validarUsuario();
                            }
                        }
                    }
                });
                  
                var loginForm = new Ext.form.FormPanel({
                    el:'formLogin',
                    id: 'loginForm',
                    title: 'AUTENTICACI\u00D3N DE USUARIO',        
                    labelAlign: 'top',        
                    frame:true,
                    //height: 200,
                    width: 270,
                    bodyPadding: 5,
                    items:[
                        dsUser,
                        dsPassword,
                        confirmPassword
                    ],
                    buttons: [{
                        text: 'Entrar',
                        handler: function(btn, e) {
                            validarUsuario();
                        }
                    },{
                        text: 'Cancelar',
                        handler: function(btn, e) {
                            btn.up('form').getForm().reset();
                            btn.up('form').down('[name=passwordConfirm]').disable();
                            btn.up('form').down('[name=passwordConfirm]').hide();
                        }
                    }]
                });
                        
                loginForm.render();
                
                function validarUsuario() {
                    if (loginForm.form.isValid()) {
                        
                        loginForm.form.submit({
                            url: _URL_VALIDA_EXISTE_USUARIO,
                            waitMsg:'Procesando...',
                            failure: function(form, action) {
                                
                                debug('failure', form, action);
                                
                                var msjError = "Error de comunicaci\u00F3n. Consulte a su administrador o intente m\u00E1s tarde."
                                Ext.Msg.show({
                                    title: 'Error', 
                                    msg: !Ext.isEmpty(action.result.errorMessage) ? action.result.errorMessage : msjError, 
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            },
                            success: function(form, action) {
                                
                                debug('success', form, action);
                                debug('existeUsuarioLDAP=', action.result.params.existeUsuarioLDAP);
                                debug('modoAutenticacionLDAP=', action.result.params.modoAutenticacionLDAP);
                                debug('permiteAgregarUsuariosLdap=', action.result.params.modoAgregarUsuariosLDAP);
                                
                                // Si esta activo el modo autenticacion LDAP y no existe en LDAP:
                                if(action.result.params.modoAutenticacionLDAP == 'true' && action.result.params.existeUsuarioLDAP != 'true') {
                                        
                                    // Si esta activo el modo de agregar usuario LDAP y
                                    // el campo confirmacion de passwd esta oculto:
                                    if(action.result.params.modoAgregarUsuariosLDAP == 'true' && loginForm.down('[name=passwordConfirm]').isHidden()) {
                                            
                                            dsPassword.reset();
                                            loginForm.down('[name=passwordConfirm]').enable();
                                            loginForm.down('[name=passwordConfirm]').show();
                                            Ext.Msg.show({
                                                title   : 'Aviso',
                                                icon    : Ext.Msg.INFO,
                                                msg     : 'Por motivos de seguridad y como \u00FAnica ocasi\u00F3n debe de renovar su contrase\u00F1a.',
                                                buttons : Ext.Msg.OK
                                            });
                                            
                                    } else {
                                        validarPasswordYrol();
                                    }
                                } else {
                                    validarPasswordYrol();
                                }
                            }
                        });
                        
                    } else {
                        Ext.Msg.show({
                            title: 'Aviso',
                            msg: 'Complete y Verifique la informaci\u00F3n requerida',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.WARNING
                        });
                    }
                }
                
                function validarPasswordYrol(){
                    loginForm.form.submit({
                        url: _URL_VALIDA_USUARIO,
                        waitMsg:'Procesando...',
                        failure: function(form, action) {
                            switch (action.failureType) {
                                case Ext.form.action.Action.CONNECT_FAILURE:
                                    Ext.Msg.show({title: 'Error', msg: 'Error de comunicaci\u00F3n', buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
                                    break;
                                case Ext.form.action.Action.SERVER_INVALID:
                                case Ext.form.action.Action.LOAD_FAILURE:
                                    var msgServer = Ext.isEmpty(action.result.errorMessage) ? 'Error interno del servidor, consulte a soporte' : action.result.errorMessage;
                                    Ext.Msg.show({title: 'Error', msg: msgServer, buttons: Ext.Msg.OK, icon: Ext.Msg.ERROR});
                                    break;
                            }
                        },
                        success: function(form, action) {
                            //_grabarEvento('SEGURIDAD','LOGIN');
                            self.location.href = _CONTEXT+'/seleccionaRolCliente.action';
                        }
                    });
                }
            });
	    
	    </script>
	    
	    <script type="text/javascript" src="${ctx}/resources/extjs4/base_extjs4.js?${now}"></script>
        <script type="text/javascript" src="${ctx}/resources/scripts/util/extjs4_utils.js?${now}"></script>
		<script type="text/javascript" src="${ctx}/resources/scripts/util/custom_overrides.js?${now}"></script>

    </head>
    <body>

        <table class="headlines" cellspacing="10">
	        <tr valign="top">
	           <td><img src= "${ctx}/resources/images/aon/login.jpg" width="400"/>&nbsp;</td>
	           <td class="headlines" colspan="1">
	               <div id="formLogin"></div>
	           </td>
	        </tr>
	        <tr> 
			    <td colspan="5">
                    Se recomienda utilizar el navegador Chrome o Firefox para el correcto funcionamiento de la aplicaci&oacute;n.
                    <br/>
                    Haz clic en los iconos para descargar:
                    
					<a href="https://www.google.com/intl/es/chrome/browser/?hl=es" target="_blank" style="text-decoration:none">
						<img src="http://icons.iconarchive.com/icons/google/chrome/128/Google-Chrome-icon.png" height="32" width="32" border="0" alt="Google Chrome" />
					</a>
					&nbsp;&nbsp;
                    <a href="http://www.mozilla.org/es-MX/firefox/new/" target="_blank" style="text-decoration:none">
						<img src="http://icons.iconarchive.com/icons/benjigarner/softdimension/48/Firefox-icon.png" height="32" width="32" border="0" alt="Firefox" />
					</a>
					
			    </td>
			</tr>
	        <tr> 
			    <td colspan="5" class="textologin">
                    <br>INFORMACI&Oacute;N DE GENERAL DE SEGUROS.<br><br><br><br>
			    </td>
			</tr>
	    </table>
	</body>
</html>
