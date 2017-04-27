<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// variables //////
var _p24_urlBuscarPolizas  = '<s:url namespace="/renovacion" action="buscarPolizasRenovables" />';
var _p24_urlRenovarPolizas = '<s:url namespace="/renovacion" action="renovarPolizas"          />';

var _p24_storePolizas;
var _p24_ultimosParams;
////// variables //////

Ext.onReady(function()
{
    Ext.Ajax.timeout = 15*60*1000;

    ////// modelos //////
    Ext.define('_p24_modeloPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.gridFields" escapeHtml="false" /> ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p24_storePolizas = Ext.create('Ext.data.Store',
    {
        model     : '_p24_modeloPoliza'
        ,autoLoad : false
        ,proxy    :
        {
            type    : 'ajax'
            ,url    : _p24_urlBuscarPolizas
            ,reader :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,messageProperty : 'respuesta'
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p24_divpri'
        ,defaults : { style : 'margin : 5px;' }
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                title     : 'Buscar p&oacute;lizas a renovar'
                ,itemId   : '_p24_busquedaForm'
                ,defaults : { style : 'margin : 5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 3
                }
                ,items    :
                [
                    <s:property value="imap.busquedaItems" escapeHtml="false" />
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : _p24_buscarClic
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title        : 'Resultados'
                ,itemId      : '_p24_grid'
                ,selType     : 'checkboxmodel'
                ,store       : _p24_storePolizas
                ,minHeight   : 200
                ,maxHeight   : 400
                ,columns     : [ <s:property value="imap.gridColumns" escapeHtml="false" /> ]
                ,viewConfig  : viewConfigAutoSize
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text      : 'Renovar'
                        ,itemId   : '_p24_gridBotonRenovar'
                        ,icon     : '${ctx}/resources/fam3icons/icons/date_add.png'
                        ,handler  : _p24_renovarClic
                        ,disabled : true
                    }
                ]
                ,plugins     :
                [
                    Ext.create('Ext.grid.plugin.CellEditing',
                    {
                        clicksToEdit: 1
                    })
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    var form = _fieldById('_p24_busquedaForm');
    _fieldByName('cdunieco',form).getStore().on(
    {
        load : function(me)
        {
            me.insert(0,new Generic({key:'-1',value:'Todas'}));
            _fieldByName('cdunieco',form).setValue('-1');
        }
    });
    _fieldByName('cdramo',form).getStore().on(
    {
        load : function(me)
        {
            me.insert(0,new Generic({key:'-1',value:'Todos'}));
            _fieldByName('cdramo',form).setValue('-1');
        }
    });
    _fieldByName('mes',form).getStore().on(
    {
        load : function(me)
        {
            _fieldByName('mes',form).setValue(('00'+(new Date().getMonth()+1)).slice(-2));
        }
    });
    _fieldById('_p24_grid').getSelectionModel().on(
    {
        selectionChange : _p24_gridSelectionChange
    });
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p24_buscarClic(button,e)
{
    debug('>_p24_buscarClic');
    var form=button.up('form');
    _p24_ultimosParams =
    {
        'cdunieco' : _fieldByName('cdunieco' , form).getValue()
        ,'cdramo'  : _fieldByName('cdramo'   , form).getValue()
        ,'anio'    : _fieldByName('anio'     , form).getValue()
        ,'mes'     : _fieldByName('mes'      , form).getValue()
    };
    _p24_storePolizas.load(
    {
        params    :
        {
            'smap1.cdunieco' : _fieldByName('cdunieco' , form).getValue()
            ,'smap1.cdramo'  : _fieldByName('cdramo'   , form).getValue()
            ,'smap1.anio'    : _fieldByName('anio'     , form).getValue()
            ,'smap1.mes'     : _fieldByName('mes'      , form).getValue()
        }
        ,callback : _p24_storePolizasLoadCallback
    });
    debug('<_p24_buscarClic');
}

function _p24_storePolizasLoadCallback(records,op,success)
{
    debug('>_p24_storePolizasLoad',records,success,'dummy');
    if(success)
    {
        for(var i=0;i<records.length;i++)
        {
            records[i].set('cducreno',records[i].get('cdunieco'));
        }
    }
    else
    {
        var error=op.getError();
        debug('error:',error);
        if(typeof error=='object')
        {
            errorComunicacion();
        }
        else
        {
            mensajeError(error);
        }
    }
    debug('>_p24_storePolizasLoad');
}

function _p24_renovarClic(button,e)
{
    debug('>_p24_renovarClic');
    var json   = {};
    var slist1 = [];
    var arr    = _fieldById('_p24_grid').getSelectionModel().getSelection();
    Ext.Array.each(arr,function(record)
    {
        var val=record.raw;
        val['cducreno']=record.get('cducreno');
        slist1.push(val);
    });
    json['slist1'] = slist1;
    json['smap1']  = _p24_ultimosParams;
    debug('### renovar json params:',json);
    _fieldById('_p24_grid').setLoading(true);
    Ext.Ajax.request(
    {
        url       : _p24_urlRenovarPolizas
        ,jsonData : json
        ,success  : function(response)
        {
            _fieldById('_p24_grid').setLoading(false);
            var resp = Ext.decode(response.responseText);
            debug('### renovar json response:',resp);
            if(resp.exito)
            {
                _fieldById('_p24_grid').getStore().removeAll();
                mensajeCorrecto('Proceso completo',resp.respuesta);
            }
            else
            {
                mensajeError(resp.respuesta);
            }
        }
        ,failure  : function()
        {
            _fieldById('_p24_grid').setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p24_renovarClic');
}

function _p24_gridSelectionChange(selModel,selected,e)
{
    debug('>_p24_gridSelectionChange selected.length:',selected.length);
    _fieldById('_p24_gridBotonRenovar').setDisabled(selected.length==0);
}
////// funciones //////
</script>
</head>
<body><div id="_p24_divpri" style="height:600px;"></div></body>
</html>