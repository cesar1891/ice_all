<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script>
////// urls //////
var _p32_urlRecuperacionSimple      = '<s:url namespace="/emision" action="recuperacionSimple"           />';
var _p32_urlRecuperacionSimpleLista = '<s:url namespace="/emision" action="recuperacionSimpleLista"      />';
var _p32_urlGenerarCdperson         = '<s:url namespace="/"        action="generarCdperson"              />';
var _p32_urlGuardar                 = '<s:url namespace="/emision" action="guardarPantallaBeneficiarios" />';
var _p32_urlConfirmarEndoso         = '<s:url namespace="/endosos" action="guardarEndosoBeneficiarios"   />';
////// urls //////

////// variables //////
var _p32_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p32_smap1:',_p32_smap1);

var _p32_store = null;

var _p32_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p32_flujo:',_p32_flujo);
////// variables //////

//////

var objetoColumnas  = 
            [
                {
                    dataIndex : 'mov'
                    ,text     : 'OPERACI&Oacute;N'
                    ,width    : 100
                    ,renderer : function(mov)
                    {
                         if(mov+'x'=='-x')
                        {
                            return '<span style="font-style:italic;">(eliminar)</span>';
                        }
                        else if(mov+'x'=='+x')
                        {
                            return '<span style="font-style:italic;">(agregar)</span>';
                        }
                    }
                }
                ,<s:property value="imap.mpoliperColumns" />
            ]


if(_p32_smap1.cdramo+'x'=='5x' || _p32_smap1.cdramo+'x'=='16x') // Y 16
{
	for(var i=0;i<objetoColumnas.length;i++)
	{
	    //DESHABILITO LOS CAMPOS DE FECHA DE NACIMIENTO PARA LOS TIPOS TL Y TV
		if(objetoColumnas[i].text === 'FECHA NACIMIENTO')
		{
			objetoColumnas[i].hidden=true;
// 			objetoColumnas[i].setHidden(true);
			
			i=objetoColumnas.length;
		}
	}
// 	alert(objetoColumnas);
    debug('>columnas<',objetoColumnas);
} 

//////

Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p32_modeloMpoliper',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'mov'
            ,<s:property value="imap.mpoliperFields" />
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p32_store=Ext.create('Ext.data.Store',
    {
        model     : '_p32_modeloMpoliper'
        ,autoLoad : false
        ,proxy    :
        {
            type    : 'ajax'
            ,reader :
            {
                type  : 'json'
                ,root : 'slist1'
            }
            ,url : _p32_urlRecuperacionSimpleLista
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p32_divpri'
        ,itemId   : '_p32_panelpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.grid.Panel',
            {
                itemId     : '_p32_grid'
                ,title     : 'BENEFICIARIOS'
                ,minHeight : 250
                ,maxHeight : 400
                ,plugins   :
                [
                    Ext.create('Ext.grid.plugin.RowEditing',
                    {
                        clicksToEdit  : 2
                        ,errorSummary : false
                    })
                ]
                ,selModel  :
                {
                    selType        : 'checkboxmodel'
                    ,allowDeselect : true
                    ,mode          : 'SINGLE'
                }
                ,store     : _p32_store
                ,columns   : objetoColumnas 
                ,tbar :
                [
                    {
                        text     : 'Agregar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
                        ,handler : function(){ _p32_agregar(); }
                    }
                    ,{
                        text     : 'Borrar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,handler : function(){ _p32_borrar(); }
                    }
                    ,{
                        text     : 'Deshacer'
                        ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                        ,handler : function(){ _p32_deshacer(); }
                    }
                    ,{
                        xtype  : 'displayfield'
                        ,value : '<span style="color:white;">*Para editar un beneficiario haga doble clic sobre la fila</span>'
                    }
                ]
                ,bbar    :
                [
                    '->'
                    ,{
                        xtype     : 'form'
                        ,itemId   : '_p32_formEndoso'
                        ,layout   : 'hbox'
                        ,defaults : { style : 'margin:5px;' }
                        ,ui       :'footer'
                        ,items    :
                        [
                            {
                                xtype       : 'datefield'
                                ,name       : 'fechaEndoso'
                                ,fieldLabel : 'Fecha de efecto'
                                ,value      : new Date()
                                ,style      : 'margin:5px;margin-left:15px;'
                                ,allowBlank : false
                                ,hidden     : Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                                    ? true
                                    : false
                            }
                            ,{
                                xtype    : 'button'
                                ,text     : Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                                    ? 'Guardar'
                                    : 'Confirmar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/' + (
                                    Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                                        ? 'disk.png'
                                        : 'key.png'
                                )
                                ,handler : function(){ _p32_guardarClic(); }
                            }
                        ]
                    }
                    ,'->'
                ]
                /*,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        xtype    : 'button'
                        text     : Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                            ? 'Guardar'
                            : 'Confirmar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/' + (
                            Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                                ? 'disk.png'
                                : 'key.png'
                        )
                        ,handler : function(){ _p32_guardarClic(); }
                    }
                ]*/
//                 ,listeners :
//                 {
//                     afterrender : function()
//                     {
 
//                     }
//                 }
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////

    if(_p32_smap1.ultimaImagen+'x'=='Sx')
    {
        Ext.Ajax.request(
        {
            url     : _p32_urlRecuperacionSimple
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_ULTIMO_NMSUPLEM'
                ,'smap1.cdunieco'     : _p32_smap1.cdunieco
                ,'smap1.cdramo'       : _p32_smap1.cdramo
                ,'smap1.estado'       : _p32_smap1.estado
                ,'smap1.nmpoliza'     : _p32_smap1.nmpoliza
            }
            ,success : function(response)
            {
                var json=Ext.decode(response.responseText);
                debug('### nmsuplem:',json);
                if(json.exito)
                {
                    debug('_p32_smap1:',_p32_smap1);
                    _p32_smap1.nmsuplem=json.smap1.nmsuplem;
                    debug('_p32_smap1:',_p32_smap1);
                    
                    _p32_cargarStore();
                }
                else
                {
                    mensajeWarning(json.respuesta);
                }
            }
            ,failure : function(){ errorComunicacion(); }
        });
    }
    else
    {
        _p32_cargarStore();
    }
    ////// loaders //////
});

////// funciones //////
function _p32_cargarStore()
{
    debug('>_p32_cargarStore<');
    _p32_store.load(
    {
        params :
        {
            'smap1.procedimiento' : 'RECUPERAR_MPOLIPER_OTROS_ROLES_POR_NMSITUAC'
            ,'smap1.cdunieco'     : _p32_smap1.cdunieco
            ,'smap1.cdramo'       : _p32_smap1.cdramo
            ,'smap1.estado'       : _p32_smap1.estado
            ,'smap1.nmpoliza'     : _p32_smap1.nmpoliza
            ,'smap1.nmsuplem'     : _p32_smap1.nmsuplem
            ,'smap1.nmsituac'     : _p32_smap1.nmsituac
            ,'smap1.roles'        : _p32_smap1.cdrolPipes
        }
    });
}

function _p32_borrar()
{
    var valido=true;
    
    if(valido)
    {
        valido = _fieldById('_p32_grid').getSelectionModel().getSelection().length>0;
        if(!valido)
        {
            mensajeWarning('Debe seleccionar un beneficiario');
        }
    }
    
    var record;
    
    if(valido)
    {
        record = _fieldById('_p32_grid').getSelectionModel().getSelection()[0];
        valido = record.get('mov')+'x'!='-x';
        if(!valido)
        {
            mensajeWarning('Este beneficiario ya se marc&oacute; como eliminado');
        }
    }
    
    if(valido)
    {
        record = _fieldById('_p32_grid').getSelectionModel().getSelection()[0];
        valido = record.get('mov')+'x'!='+x';
        if(!valido)
        {
            mensajeWarning('Este beneficiario es nuevo y no puede eliminarse');
        }
    }
    
    if(valido)
    {
        record.set('mov','-');
    }
}

function _p32_deshacer()
{
    var valido=true;
    
    if(valido)
    {
        valido = _fieldById('_p32_grid').getSelectionModel().getSelection().length>0;
        if(!valido)
        {
            mensajeWarning('Debe seleccionar un beneficiario');
        }
    }
    
    var record;
    if(valido)
    {
        record = _fieldById('_p32_grid').getSelectionModel().getSelection()[0];
        valido = record.get('mov')+'x'!='x';
        if(!valido)
        {
            mensajeWarning('No hay operaciones que deshacer para el beneficiario seleccionado');
        }
    }
    
    if(valido)
    {
        if(record.get('mov')+'x'=='+x')
        {
            _p32_store.remove(record);
        }
        else
        {
            record.set('mov','');
        }
    }
}

function _p32_agregar()
{
    var valido=true;
    
    if(valido)
    {
        _setLoading(true,_fieldById('_p32_grid'));
        Ext.Ajax.request(
        {
            url      : _p32_urlGenerarCdperson
            ,success : function(response)
            {
                _setLoading(false,_fieldById('_p32_grid'));
                var json=Ext.decode(response.responseText);
                debug('### cdperson:',json);
                if(json.success)
                {
                    var record=new _p32_modeloMpoliper(
                    {
                        CDPERSON : json.cdperson
                        ,mov     : '+'
                    });
                    _p32_store.add(record);
                }
                else
                {
                    mensajeError('Error al generar clave de persona');
                }
            }
            ,failure : function()
            {
                _setLoading(false,_fieldById('_p32_grid'));
                errorComunicacion();
            }
        });
    }
}

function _p32_guardarClic(callback)
{
    debug('>_p32_guardarClic');
    
    var ck = 'Validando beneficiarios';
    try
    {
        if(_p32_store.getCount()==0)
        {
            throw 'No hay beneficiarios';
        }
        
        var buenos=0;
        _p32_store.each(function(record)
        {
            if(record.get('mov')+'x'!='-x')
            {
                buenos=buenos+1;
            }
        });
        if(buenos==0)
        {
            throw 'Todos los beneficiarios se han marcado para borrar';
        }
        
        var malos=0;
        _p32_store.each(function(record)
        {
            if(Ext.isEmpty(record.get('DSNOMBRE')))
            {
                malos=malos+1;
            }
        });
        if(malos>0)
        {
            throw 'El nombre es obligatorio para todos los beneficiarios';
        }
        
        malos=0;
        _p32_store.each(function(record)
        {
            if(record.get('PORBENEF')-0==0)
            {
                malos=malos+1;
            }
        });
        if(malos>0)
        {
            throw 'El porcentaje no puede ser cero para los beneficiarios';
        }
        
        var suma=0;
        _p32_store.each(function(record)
        {
            if(record.get('mov')+'x'!='-x')
            {
                suma=suma+(record.get('PORBENEF')-0);
            }
        });
        if(suma!=100)
        {
            throw 'La suma de porcentajes de beneficiarios activos es '+suma+', en lugar de 100';
        }
        
        var json=
        {
            smap1 :
            {
                cdunieco  : _p32_smap1.cdunieco
                ,cdramo   : _p32_smap1.cdramo
                ,estado   : _p32_smap1.estado
                ,nmpoliza : _p32_smap1.nmpoliza
                ,nmsuplem : _p32_smap1.nmsuplem
                ,nmsituac : _p32_smap1.nmsituac
                ,cdtipsup : _p32_smap1.cdtipsup
                ,ntramite : _p32_smap1.ntramite
                ,feefecto : Ext.Date.format(_fieldByName('fechaEndoso').getValue(),'d/m/Y')
            }
            ,slist1 : []
        };
        
        _p32_store.each(function(record)
        {
            json.slist1.push(parseaFechas(record.data));
        });
        
        if(!Ext.isEmpty(_p32_flujo))
        {
            json.flujo = _p32_flujo;
        }
        
        debug('json a enviar:',json);
        
        ck='Enviando peticion';
        _setLoading(true,_fieldById('_p32_grid'));
        Ext.Ajax.request(
        {
            url       : Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S'
                ? _p32_urlGuardar
                : _p32_urlConfirmarEndoso
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_fieldById('_p32_grid'));
                var json2 = Ext.decode(response.responseText);
                debug('### guardar:',json2);
                if(json2.exito)
                {
                    if (Number(_p32_smap1.cdtipsup) === 1 || _p32_smap1.sinConfirmar === 'S') {
                        mensajeCorrecto('Datos guardados','Datos guardados',function()
                        {
                            try
                            {
                                //////////////////////////////////
                                ////// usa codigo del padre //////
                                /*//////////////////////////////*/
                                try{
                                	expande(2);
                                }catch(e)
                                {
                                    //manejaException(e,'Manejando navegacion');
                                }
                                /*//////////////////////////////*/
                                ////// usa codigo del padre //////
                                //////////////////////////////////
                            }
                            catch(e)
                            {
                                manejaException(e,'Manejando navegacion');
                            }
                        });
                        _p32_cargarStore();
                        
                        if(!Ext.isEmpty(callback))
                        {
                        	callback();
                        }
                        
                    }
                    else
                    {
                        var callbackRemesa = function()
                        {
                            try
                            {
                                //////////////////////////////////
                                ////// usa codigo del padre //////
                                /*//////////////////////////////*/
                                marendNavegacion(2);
                                /*//////////////////////////////*/
                                ////// usa codigo del padre //////
                                //////////////////////////////////
                            }
                            catch(e)
                            {
                                manejaException(e,'Reiniciando navegacion');
                            }
                        };
                        
                        mensajeCorrecto('Datos guardados','Endoso generado',function()
                        {
                            _generarRemesaClic(
                                true
                                ,_p32_smap1.cdunieco
                                ,_p32_smap1.cdramo
                                ,_p32_smap1.estado
                                ,_p32_smap1.nmpoliza
                                ,callbackRemesa
                            );
                        });
                    }
                }
                else
                {
                    mensajeError(json2.error);
                }
            }
            ,failure  : function()
            {
                _setLoading(false,_fieldById('_p32_grid'));
                errorComunicacion();
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,_fieldById('_p32_grid'));
    }
    
    debug('<_p32_guardarClic');
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body>
<div id="_p32_divpri" style="height:400px;"></div>
</body>
</html>