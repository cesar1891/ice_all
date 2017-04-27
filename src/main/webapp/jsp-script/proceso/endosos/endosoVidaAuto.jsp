<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script>
////// urls //////
var _p32_urlRecuperacionSimple      = '<s:url namespace="/emision" action="recuperacionSimple"           />';
//var _p32_urlRecuperacionSimpleLista = '<s:url namespace="/emision" action="recuperacionSimpleLista"      />';
var _p32_urlRecuperacionSimpleLista = '<s:url namespace="/endosos" action="obtieneBeneficiariosVidaAuto" />';
var _p32_urlGenerarCdperson         = '<s:url namespace="/"        action="generarCdperson"              />';
var _p32_urlGuardar                 = '<s:url namespace="/emision" action="guardarPantallaBeneficiarios" />';
var _p32_urlConfirmarEndoso         = '<s:url namespace="/endosos" action="confirmaEndosoBeneficiariosVidaAuto"   />';
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
                    	debug('*****mov: ', mov);
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
//          objetoColumnas[i].setHidden(true);
            
            i=objetoColumnas.length;
        }
    }
//  alert(objetoColumnas);
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
            //,url : _p32_urlRecuperacionSimpleLista
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
                        ,handler : function(){_p32_deshacer(); }
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
                        ,itemId   : '_p36_formEndoso'
                        ,layout   : 'hbox'
                        ,defaults : { style : 'margin:5px;' }
                        ,items    :
                        [
                            {
                                xtype       : 'datefield'
                                ,name       : 'fechaEndoso'
                                ,itemId     : '_p32_fechaCmp'
                                ,fieldLabel : 'Fecha de efecto'
                                ,value      : new Date()
                                ,style      : 'margin:5px;margin-left:15px;'
                                ,allowBlank : false
                            }
                            ,{
                                xtype    : 'button'
                                ,text    : 'Confirmar'
                                ,itemId  : '_p36_botonConfirmar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                                ,handler : function()
                                            {
                                                _p32_guardarClic();
                                            }
                            }
                        ]
                    }
                    ,'->'
                ]
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
    Ext.Ajax.request(
    {
        url      : _p32_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : _p32_smap1.cdunieco
            ,'smap1.cdramo'       : _p32_smap1.cdramo
            ,'smap1.estado'       : _p32_smap1.estado
            ,'smap1.nmpoliza'     : _p32_smap1.nmpoliza
            ,'smap1.cdtipsup'     : _p32_smap1.cdtipsup
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldById('_p32_fechaCmp').setMinValue(json.smap1.FECHA_MINIMA);
                _fieldById('_p32_fechaCmp').setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldById('_p32_fechaCmp').setValue(json.smap1.FECHA_REFERENCIA);
                _fieldById('_p32_fechaCmp').setReadOnly(json.smap1.EDITABLE=='N');
                _fieldById('_p32_fechaCmp').isValid();
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
    
    Ext.Ajax.request({
        url     : _p32_urlRecuperacionSimpleLista
        ,params :{
             'smap1.cdunieco'     : _p32_smap1.cdunieco
            ,'smap1.cdramo'       : _p32_smap1.cdramo
            ,'smap1.estado'       : _p32_smap1.estado
            ,'smap1.nmpoliza'     : _p32_smap1.nmpoliza
        }
        ,success : function(response, options){
            var json = Ext.decode(response.responseText);
            debug('### succes, jsonpreview:', json);
            if(Ext.isEmpty(json.slist1)) {
               centrarVentanaInterna( Ext.create('Ext.window.Window',
                    {
                        title        : 'Aviso'
                        ,modal       : true
                        ,buttonAlign : 'center'
                        ,width       : 520
                        ,resizable   : false
                        ,html        : '<div style="padding:5px;text-align:center;">No existe ningun beneficiario, ya que esta cobertura no esta amparada</div>'
                        ,buttonAlign : 'center'
                        ,closable     : false
                        ,buttons     :
                        [
                            {
                                text : 'Aceptar'
                                ,handler : function(me)
                                {
                                	me.up('window').destroy();
                                    marendNavegacion(2);
                                }
                            }
                        ]
                    }).show());
            } else {
            	_p32_store.loadData(json.slist1);
            	mensajeCorrecto('Aviso', 'En caso de que en la emisión original de la póliza se haya declarado que el contratante ha estado expuesto o padecido una enfermedad crónica o grave en los últimos 12 meses, la cobertura VIDAUTO no se amparará.');
            }
        },
        failure : function(response, options) {
            debug('failure:', response)
            errorComunicacion(null,'Error al cancelar endoso');
        }
    });
   
});

////// funciones //////
function _p32_cargarStore()
{
    debug('>_p32_cargarStore<');
    _p32_store.load(
    {
        params :
        {
             'smap1.cdunieco'     : _p32_smap1.cdunieco
            ,'smap1.cdramo'       : _p32_smap1.cdramo
            ,'smap1.estado'       : _p32_smap1.estado
            ,'smap1.nmpoliza'     : _p32_smap1.nmpoliza
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
                ,tstamp   : _p32_smap1.tstamp
               // ,feefecto : Ext.Date.format(_fieldByName('fechaEndoso').getValue(),'d/m/Y')
                ,'feefecto' : Ext.Date.format(_fieldById('_p32_fechaCmp').getValue(),'d/m/Y')
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
        
        debug('jsonaenviar:',json);
        
        ck='Enviando peticion';
        _setLoading(true,_fieldById('_p32_grid'));
        Ext.Ajax.request(
        {
            url       : _p32_urlConfirmarEndoso
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_fieldById('_p32_grid'));
                var json2 = Ext.decode(response.responseText);
                debug('### guardar:',json2);
                
                if(json2.success){
                	debug('confirmaEndosoBeneficiariosVidaAuto');
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
                
                } else {
                	mensajeError(json2.respuesta);
                
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