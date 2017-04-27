<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
var _p30_urlRecuperacion        = '<s:url namespace="/recuperacion" action="recuperar"/>';
var _p30_urlMovimientoExclusion = '<s:url namespace="/mesacontrol" action="exclusionTurnados"/>';
////// urls //////

////// variables //////
var ventana;
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var comboUsuario = <s:property value="items.comboUsuario" escapeHtml='false' />;

////// componentes dinamicos //////

Ext.onReady(function()
{
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////
    _p83_store = Ext.create('Ext.data.Store',
    {
    	fields :
            [
                'dsusuari','cdusuari'
            ]
    });
    ////// stores //////
    
    ////// componentes //////
    ventana =
    Ext.create('Ext.window.Window',{
        title        : 'Agregar a Excluidos'
        ,modal       : true
        ,width       : 350
        ,buttonAlign : 'center'
        ,resizable   : false
        ,closeAction : 'hide'
        ,listeners   : 
        {
        
        	close  : function()
        	{
        		comboUsuario.reset();
        	}
        
        }
        ,items       :[
             Ext.create('Ext.form.Panel',
            {
               items    :
                       [
                            comboUsuario
                       ]
            })
        ],
        buttonAlign:'center',
        buttons: 
        [
            {
                text: 'Aceptar',
                icon: '${ctx}/resources/fam3icons/icons/accept.png',
                buttonAlign : 'center',
                handler: function(button) {
                    
                    var ventana = button.up('window');
                    var form = button.up('window').down('form');
                    var valores= form.getValues();
                    var usu =  valores.cdusuari;
                    
                    if(!form.isValid())
                    {
                        return datosIncompletos();
                    }
                    
                    debug('Usuario: ',usu);
                    _p83_AgregarClic(usu);
                                                            
                    ventana.close();             
                }
            }
        ]
    });
    ////// componentes //////
    
    ////// contenido //////
  Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p83_panelpri'
        ,border   : 0
        ,renderTo : '_p83_divpri'
        ,defaults : { style : 'margin : 5px;' }
        ,items    :
        [
         Ext.create
         (
                 'Ext.grid.Panel',
                 {
                	  store      :  _p83_store
                     ,itemId     : '_p83_grid'
                     ,title      : 'Usuarios Excluidos'
                     ,height     : 470
                   	 ,tbar     :
                            [{
                                xtype : 'textfield',
                                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Nombre:</span>',
                                labelWidth : 100,
                                width: 260,
                                maxLength : 50,
                                listeners:{
                                	change: function(elem,newValue,oldValue){
                                		newValue = Ext.util.Format.uppercase(newValue);
                                		
                	            		//Validacion de valor anterior ya que la pantalla hace lowercase en automatico y manda doble change
                						if( newValue == Ext.util.Format.uppercase(oldValue)){
                							return false;
                						}
                						
                						try{
                							_p83_store.removeFilter('filtroUsuari');
                							_p83_store.filter(Ext.create('Ext.util.Filter', {property: 'dsusuari', anyMatch: true, value: newValue, root: 'data', id:'filtroUsuari'}));
                						}catch(e){
                							error('Error al filtrar por usuario',e);
                						}
                                	}
                                }
                            },
                            '->',//Espaciador
                            {
                            	 itemId        : '_p83_botonEditar'
                                ,text          : 'Agregar'
                                ,icon          : '${ctx}/resources/fam3icons/icons/add.png'
                                ,handler       : _p83_ventanaAgregar
                            }
                           ]
                   	 ,columns: 
                   		 [
	                   	    {
	                   	         text          : 'USUARIO'
	                   	        ,dataIndex     : 'dsusuari'
	                   	        ,flex          : 1
	                   	        ,menuDisabled  : true
	                   	    }
	                   	    ,{
	                   	        xtype         : 'actioncolumn'
	                   	            ,menuDisabled : true
	                   	            ,sortable     : false
	                   	            ,width        : 50
	                   	            ,items        :
	                   	            [
	                   	                {
	                   	                    tooltip  : 'Eliminar'
	                   	                    ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
	                  	                    ,handler : _p83_gridBotonEliminarClic
	                   	                }
                   	            ]
                   	      }
                   	]
                 }
                 
         )
         
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _p83_cargar();
    ////// loaders //////
});

////// funciones //////

  
function _p83_cargar()
{
	_mask("RECUPERANDO EXCLUSIONES"); 

     Ext.Ajax.request({ 
                       url     : _p30_urlRecuperacion
                      ,params :
                           {
                             'params.consulta' :'RECUPERAR_EXCLUSION_TURNADOS'
                           }
                      ,success : function(response)
                      {  
                        _unmask();  
                       var ck = 'RECUPERANDO EXCLUIDOS';
                       try
                         {
                          var json = Ext.decode(response.responseText);
                          debug('### Lista de Excluidos: ',json);
                          if(json.success==true)
                             {
                               lista = json.list;
                               _p83_store.removeAll();
                               _p83_store.add(lista);
                             }
                            else
                              {
                               mensajeError(json.message);
                              }
                           }
                        catch(e)
                           {
                             manejaException(e,ck);
                           }
                      }
                  ,failure : function()
                     {
                       _unmask();
                       errorComunicacion(null,'Error al obtener lista de excluidos');
                     }
          });
}

function _p83_gridBotonEliminarClic(view,row,col,item,e,record)
{
	var usuario = record.get('cdusuari');
    debug('>_p83_gridBotonEliminarClic:',usuario);
       
    Ext.Msg.show({
        title: 'Confirmar acci&oacute;n',
        msg: '&iquest;Est\u00e1 seguro que desea eliminar este usuario del turnado?',
        buttons: Ext.Msg.YESNO,
        fn: function(buttonId, text, opt) {
            if(buttonId == 'yes') {
            	_p83_UsuarioExcluido(usuario,'D');
            }
        },
        icon: Ext.Msg.QUESTION
    });
    
    debug('<_p83_gridBotonEliminarClic');
}

function _p83_AgregarClic(cdusuario)
{
    debug('>_p83_AgregarClic:',cdusuario);
       _p83_UsuarioExcluido(cdusuario,'I');
    debug('<_p83_AgregarClic');
}

function _p83_UsuarioExcluido(cdusuari,accion)
{
	_mask("Procesando");
	 Ext.Ajax.request({ 
         url     : _p30_urlMovimientoExclusion
        ,params :
             {
                'params.usuario' : cdusuari
               ,'params.accion'  : accion
             }
        ,success : function(response)
        {            
         _unmask();	
         var ck = 'USUARIO DE EXCLUIDOS';
		         try
		           {
		            var json = Ext.decode(response.responseText);
		            debug('### Lista de Excluidos: ',json);
		            if(json.success==true)
		               {
		            	_p83_cargar();
		               }
		              else
		                {
		                 mensajeError(json.message);
		                }
		             }
		          catch(e)
		             {
		               manejaException(e,ck);
		             }
		        }
		    ,failure : function()
		       {
		    	 _unmask();
		         errorComunicacion(null,'Error al procesar Exclusi\u00f3n');
		       }
		});
	
}

function _p83_ventanaAgregar()
{
	debug('_p83_ventanaAgregar');
   ventana.show();
}

////// funciones //////
</script>
</head>
<body>
<div id="_p83_divpri" style="height:500px;border:1px solid #CCCCCC"></div>
</body>
</html>