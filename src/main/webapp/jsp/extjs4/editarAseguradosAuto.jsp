<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<script>
////// overrides //////
////// overrides //////

////// variables //////
var _p20_urlCargar    = '<s:url namespace="/"        action="cargarComplementariosAsegurados" />';
//var _p20_urlGuardar   = '<s:url namespace="/emision" action="guardarSituacionesAuto"          />';
var _p20_urlDomicilio = '<s:url namespace="/"        action="pantallaDomicilio"               />';
var _p20_urlBuscarRFC = '<s:url namespace="/"        action="buscarPersonasRepetidas"         />';

var _p20_urlCargarDatosComplementarios = '<s:url namespace="/emision"    action="cargarDatosComplementariosAutoInd"    />';
var _p20_urlPantallaCliente            = '<s:url namespace="/catalogos"  action="includes/personasLoader"              />';

var _p20_urlMovimientoMpoliper         = '<s:url namespace="/emision"    action="movimientoMpoliper"                   />';

var _p20_map1 = <s:property value="%{convertToJSON('map1')}" escapeHtml="false" />;

debug('_p20_map1:',_p20_map1);

var _p22_parentCallback         = false;

var _p20_validaBorrar = function()
{
    mensajeWarning('Falta definir la validaci&oacute;n');
    return true;
};


////// variables //////

Ext.onReady(function()
{
	
	Ext.Ajax.request(
	{
	    url     : _p20_urlCargarDatosComplementarios
	    ,params :
	    {
	        'smap1.cdunieco'  : _p20_map1.cdunieco
	        ,'smap1.cdramo'   : _p20_map1.cdramo
	        ,'smap1.estado'   : _p20_map1.estado
	        ,'smap1.nmpoliza' : _p20_map1.nmpoliza
	    }
	    ,success : function(response)
	    {
	        var json = Ext.decode(response.responseText);
	        debug('### cargar datos complementarios:',json);
	        if(json.exito)
	        {
	        	var nuevaPersona =  ((!Ext.isEmpty(json.smap1.swexiper) && "N" == json.smap1.swexiper) || (Ext.isEmpty(json.smap1.cdperson)) ) ? true:  false;

	        	if(nuevaPersona && !Ext.isEmpty(_p20_map1.cdpercli))
	        	{
	        	    var ck = 'Recuperando cliente de tr\u00e1mite';
	        	    try
	        	    {
	        	        _fieldById('_p20_clientePanel').loader.load(
	                    {
	                        params:
	                        {
	                            'smap1.cdperson' : _p20_map1.cdpercli,
	                            'smap1.cdideper' : json.smap1.cdideper,
	                            'smap1.cdideext' : json.smap1.cdideext,
	                            'smap1.esSaludDanios' : 'D',
	                            'smap1.polizaEnEmision': 'S',
	                            'smap1.esCargaClienteNvo' : 'N', // SE PUEDE CAMBIAR EL CLIENTE //((!Ext.isEmpty(json.smap1.swexiper) && "N" == json.smap1.swexiper) || (Ext.isEmpty(json.smap1.cdperson))? 'S' : 'N' ),
	                            'smap1.cargaCP' : '',//json.smap1.cdpostal,
	                            'smap1.cargaTipoPersona' : '',//json.smap1.otfisjur,
	    //                      'smap1.cargaSucursalEmi' : _p20_map1.cdunieco,
	    //                      'smap1.cargaFenacMin' : _aplicaCobVida?_FechaMinEdad:'',
	    //                      'smap1.cargaFenacMax' : _aplicaCobVida?_FechaMaxEdad:'',
	                            'smap1.tomarUnDomicilio' : 'S',
	                            'smap1.cargaOrdDomicilio' : nuevaPersona? '' : json.smap1.nmorddom
	                        }
	                    });
	        	    }
	        	    catch(e)
	        	    {
	        	        manejaException(e,ck);
	        	    }
	        	}
	        	else
	        	{
	                _fieldById('_p20_clientePanel').loader.load(
	                {
	                    params:
	                    {
	                        'smap1.cdperson' : nuevaPersona? '' : json.smap1.cdperson,
	                        'smap1.cdideper' : json.smap1.cdideper,
	                        'smap1.cdideext' : json.smap1.cdideext,
	                        'smap1.esSaludDanios' : 'D',
	                        'smap1.polizaEnEmision': 'S',
	                        'smap1.esCargaClienteNvo' : 'N', // SE PUEDE CAMBIAR EL CLIENTE //((!Ext.isEmpty(json.smap1.swexiper) && "N" == json.smap1.swexiper) || (Ext.isEmpty(json.smap1.cdperson))? 'S' : 'N' ),
	                        'smap1.cargaCP' : '',//json.smap1.cdpostal,
	                        'smap1.cargaTipoPersona' : '',//json.smap1.otfisjur,
    //	                    'smap1.cargaSucursalEmi' : _p20_map1.cdunieco,
    //	                    'smap1.cargaFenacMin' : _aplicaCobVida?_FechaMinEdad:'',
    //	                    'smap1.cargaFenacMax' : _aplicaCobVida?_FechaMaxEdad:'',
	                        'smap1.tomarUnDomicilio' : 'S',
	                        'smap1.cargaOrdDomicilio' : nuevaPersona? '' : json.smap1.nmorddom
	                    }
	                });
	            }
	            
	            _p22_parentCallback = _p20_personaSaved;
	        }
	        else
	        {
	            mensajeError(json.respuesta);
	        }
	    }
	    ,failure : errorComunicacion
	});
	
	////// contenido //////
	
	
	Ext.create('Ext.panel.Panel',
	{
		itemId      : '_p20_clientePanel'
        ,title      : 'CLIENTE'
		,defaults  :
		{
			style : 'margin : 5px;'
		}
	    ,renderTo : '_p20_divpri'
//	    ,border   : 0
	    ,height     : 600
	            ,autoScroll : true
	            ,loader     :
	            {
	                url       : _p20_urlPantallaCliente
	                ,scripts  : true
	                ,autoLoad : false
	                ,ajaxOptions: {
		                            method: 'POST'
		                     }
	            }
	    
	});
	
	////// contenido //////
	
	////// loaders //////
	////// loaders //////
});

////// funciones //////
function _p20_personaSaved(jsonCli)
{
	
    debug('>_p20_personaSaved');
    debug('Parametros respuestaCli',jsonCli);
    Ext.Ajax.request(
    {
        url     : _p20_urlMovimientoMpoliper
        ,params :
        {
            'smap1.cdunieco'  : _p20_map1.cdunieco
            ,'smap1.cdramo'   : _p20_map1.cdramo
            ,'smap1.estado'   : _p20_map1.estado
            ,'smap1.nmpoliza' : _p20_map1.nmpoliza
            ,'smap1.nmsituac' : '0'
            ,'smap1.cdrol'    : '1'
            ,'smap1.cdperson' : jsonCli.smap1.CDPERSON
            ,'smap1.nmsuplem' : '0'
            ,'smap1.status'   : 'V'
            ,'smap1.nmorddom' : jsonCli.smap1.NMORDDOM
            ,'smap1.accion'   : 'I'
            ,'smap1.swexiper' : 'S'
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### mpoliper:',json);
            if(json.exito){
				expande(1);
            }
            else{
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    debug('<_p20_personaSaved');
}

function _p20_guardar(callback)
{
	debug('>_p20_guardar');
	var valido = true;
	
	if(valido)
	{
	Ext.Ajax.request(
		{
			url       : _p20_urlGuardar
			,jsonData :
			{
				smap1   : _p20_map1
				,slist1 : slist1
			}
		    ,success  : function(response)
		    {
		    	_p20_gridAsegurados.setLoading(false);
		    	var json = Ext.decode(response.responseText);
		    	debug('json response:',json);
		    	if(json.success)
		    	{
		    		_p20_storeAsegurados.removeAll();
		    		for(var i=0;i<json.slist1.length;i++)
		    		{
		    			_p20_storeAsegurados.add(new _p20_modeloAsegurado(json.slist1[i]));
		    		}
		    		if(callback)
		    		{
		    			debug('callback:',callback);
		    			callback();
		    		}
		    		else
		    		{
		    			var ven=Ext.Msg.show({
                            title:'Datos guardados',
                            msg: 'Se han guardado los datos',
                            buttons: Ext.Msg.OK
                        });
		    			centrarVentanaInterna(ven);
                        
		    		}
		    	}
		    	else
		    	{
		    		mensajeError(json.error);
		    	}
		    }
		    ,failure  : function()
		    {
		    	_p20_gridAsegurados.setLoading(false);
		    	errorComunicacion();
		    }
		});
	}
	
	debug('<_p20_guardar');
}

////// funciones //////
</script>
</head>
<body><div id="_p20_divpri" style="height:620px;"></div>
</body>
</html>