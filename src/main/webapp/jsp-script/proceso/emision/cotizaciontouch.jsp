<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Cotizar</title>
<link rel="stylesheet" href="${ctx}/resources/touch-2.3.1/resources/css/sencha-touch.css"></style>
<script src="${ctx}/resources/jquery/jquery-1.10.2.min.js"></script>
<script src="${ctx}/resources/touch-2.3.1/sencha-touch-all.js"></script>
<script src="${ctx}/resources/touch-2.3.1/overrides.js"></script>
<script src="${ctx}/resources/touch-2.3.1/locale/ext-lang-es.js"></script>
<script>
////// overrides //////
Ext.util.Format.usMoney = function (v)
{
    v = (Math.round((v - 0) * 100)) / 100;
    v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10)) ? v + "0" : v);
    return ('$' + v);//.replace(/\./, ',');
};

Ext.override(Ext.field.Select,
{
    initialize : function()
    {
        this.callParent();
        this.setDefaultPhonePickerConfig(
        {
            cancelButton : 'Cancelar'
            ,doneButton  : 'Aceptar'
        });
        this.setDefaultTabletPickerConfig(
        {
            cancelButton : 'Cancelar'
            ,doneButton  : 'Aceptar'
        });
    }
});
Ext.override(Ext.Picker,
{
    initialize : function()
    {
        this.callParent();
        this.setCancelButton('Cancelar');
        this.setDoneButton('Aceptar');
    }
});
////// overrides //////

////// variables //////
var _text_cargando = 'Cargando...';

var _mcotiza_navView;
var _mcotiza_storeIncisos;
var _mcotiza_selectedCdperpag;
var _mcotiza_selectedCdplan;
var _mcotiza_selectedDsplan;
var _mcotiza_selectedNmsituac;

var _mcotiza_urlCotizar              = '<s:url namespace="/emision"         action="cotizar"              />';
var _mcotiza_urlCotizarExterno       = '<s:url namespace="/externo"         action="cotizar"              />';
var _mcotiza_urlViewDoc              = '<s:url namespace="/documentos"      action="descargaDocInline"    />';
var _mcotiza_urlComprar              = '<s:url namespace="/flujocotizacion" action="comprarCotizacion4"   />';
var _mcotiza_urlLoad                 = '<s:url namespace="/emision"         action="cargarCotizacion"     />';
var _mcotiza_urlNada                 = '<s:url namespace="/emision"         action="webServiceNada"       />';
var _mcotiza_urlDatosComplementarios = '<s:url namespace="/"                action="datosComplementarios" />';

var _mcotiza_necesitoIncisos = true;
<s:if test='%{getImap().get("fieldsIndividuales")==null}'>
    _mcotiza_necesitoIncisos = false;
</s:if>
debug('_mcotiza_necesitoIncisos:',_mcotiza_necesitoIncisos);

var _mcotiza_smap1         = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
 _mcotiza_smap1.conincisos = _mcotiza_necesitoIncisos?'si':'no';

var _mcotiza_urlImprimirCotiza = '<s:property value="rutaServidorReports" />';
var _mcotiza_reportsServerUser = '<s:property value="passServidorReports" />';
var _mcotiza_reporteCotizacion = '<s:text name='%{"rdf.cotizacion.nombre."+smap1.cdtipsit.toUpperCase()}' />';
var _mcotiza_urlEnviarCorreo   = '<s:url namespace="/general" action="enviaCorreo" />';

var _mcotiza_modeloExtraFields = [
<s:if test='%{getImap().get("modeloExtraFields")!=null}'>
    <s:property value="imap.modeloExtraFields" />
</s:if>
];

var _mcotiza_incisoTpl;
var _mcotiza_validacion_custom;

var _g_storeSino;

debug('_mcotiza_smap1:',_mcotiza_smap1);
////// variables //////

////// funciones //////
function maskui()
{
    Ext.Viewport.setMasked(
    {
        xtype    : 'loadmask'
        ,message : _text_cargando
    });
}

function unmaskui()
{
    Ext.Viewport.setMasked(false);
}

function _mcotiza_load()
{
	debug('>_mcotiza_load');
	Ext.Msg.prompt(
		    'Cargar cotizaci&oacute;n',
		    'N&uacute;mero de cotizaci&oacute;n:',
		    function (buttonId, value)
		    {
		    	debug('nmpoliza',value);
		    	var valido=true;
		    	//boton pulsado y valor capturado
		    	if(valido)
		    	{
		    		valido = buttonId=='ok'&&(value+'').length>0;
		    	}
		    	//valor numerico
		    	if(valido)
		    	{
		    		valido = !isNaN(value);
		    		if(!valido)
		    		{
		    			Ext.Msg.alert('Error','Introduce un n&uacute;mero v&aacute;lido');
		    		}
		    	}
		    	//request
		    	if(valido)
		    	{
		    		maskui();
		    		Ext.Ajax.request(
		    		{
		    			url      : _mcotiza_urlLoad
		    			,params  :
		    			{
		    				'smap1.nmpoliza'  : value
		    				,'smap1.cdramo'   : _mcotiza_smap1.cdramo
                            ,'smap1.cdunieco' : _mcotiza_smap1.cdunieco
		    				,'smap1.cdtipsit' : _mcotiza_smap1.cdtipsit
		    			}
		    		    ,success : function(response)
		    		    {
		    		    	unmaskui();
		    		    	var json=Ext.JSON.decode(response.responseText);
		    		    	debug('json response:',json);
		    		    	if(json.success)
		    		    	{
		    		    	    if(!json.smap1.CDUNIECO)
		    		    	    {
		    		    		    _mcotiza_nueva();
		    		    		    for(var i=0;i<json.slist1.length;i++)
		    		    		    {
		    		    		        if(_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')
		    		    		        {
		    		    		            if(json.slist1[i].nombre&&json.slist1[i].nombre+'x'!='x')
                                            {
        		    		    			    _mcotiza_storeIncisos.add(new _mcotiza_Inciso(json.slist1[i]));
    		    		    			    }
    		    		    			}
    		    		    			else
    		    		    			{
    		    		    			    _mcotiza_storeIncisos.add(new _mcotiza_Inciso(json.slist1[i]));
    		    		    			}
		    		    		    }
		    		    		    debug('store:',_mcotiza_storeIncisos);
		    		    		    var primerInciso = new _mcotiza_IncisoAgrupado(json.slist1[0]);
		    		    		    debug('primerInciso:',primerInciso);
		    		    		    //leer elementos anidados
		    		    		    var form      = _mcotiza_getFormDatosGenerales();
		    		    		    var formItems =_mcotiza_getFormDatosGenerales().items.items[0].items.items;
		    		    		    var numBlurs  = 0;
		    		    		    for(var i=0;i<formItems.length;i++)
		    		    		    {
		    		    			    var item=formItems[i];
		    		    			    if(item.hasListener('blur'))
		    		    			    {
		    		    				    var numBlursSeguidos = 1;
		    		    				    debug('contando blur:',item);
		    		    				    for(var j=i+1;j<formItems.length;j++)
		    		    				    {
		    		    					    if(formItems[j].hasListener('blur'))
		    		    					    {
    		    		    						numBlursSeguidos=numBlursSeguidos+1;
		    		    					    }
		    		    				    }
		    		    				    if(numBlursSeguidos>numBlurs)
		    		    				    {
    		    		    					numBlurs=numBlursSeguidos;
		    		    				    }
		    		    			    }
		    		    		    }
		    		    		    debug('numBlurs:',numBlurs);
		    		    		    var i=0;
		    		    		    var renderiza=function()
		    		    		    {
    		    		    			debug('renderiza',i);
		    		    			    form.setRecord(primerInciso);
		    		    			    if(i<numBlurs)
		    		    			    {
    		    		    				i=i+1;
		    		    				    for(var j=0;j<formItems.length;j++)
		    		    				    {
    		    		    				    var iItem  = formItems[j];
		    		    				        var iItem2 = formItems[j+1];
                                                debug('iItem2:',iItem2,'store:',iItem2?iItem2._store:'iItem2 no');
		    		    					    if(iItem.hasListener('blur')&&iItem2&&iItem2._store)
		    		    					    {
    		    		    						debug('tiene blur y lo hacemos heredar',iItem);
		    		    						    _g_heredarCombo(true,iItem2.id,iItem.id);
		    		    					    }
		    		    				    }
		    		    				    setTimeout(renderiza,1000);
		    		    			    }
		    		    			    else
		    		    			    {
    		    		    				_mcotiza_getFieldNmpoliza().setValue(value);
		    		    				    unmaskui();
		    		    			    }
		    		    		    }
		    		    		    maskui();
		    		    		    renderiza();
		    		    		}
		    		    		else
		    		    		{
                                    window.location.href=
                                        _mcotiza_urlDatosComplementarios
                                            +'?cdunieco='      + json.smap1.CDUNIECO
                                            +'&cdramo='        + json.smap1.cdramo
                                            +'&estado=W'
                                            +'&nmpoliza='      + json.smap1.nmpoliza
                                            +'&map1.ntramite=' + json.smap1.NTRAMITE
                                            +'&cdtipsit='      + json.smap1.cdtipsit;
		    		    		}
		    		    	}
		    		    	else
		    		    	{
		    		    		Ext.Msg.alert('Error',json.error);
		    		    	}
		    		    }
		    		    ,failure : function()
		    		    {
		    		    	unmaskui();
		    		    	Ext.Msg.alert('Error','Error de comunicaci&oacute;n');
		    		    }
		    		});
		    	}
		    });
	debug('<_mcotiza_load');
}
function _mcotiza_nueva()
{
	debug('>_mcotiza_nueva');
	_mcotiza_navView.pop(_mcotiza_navView.getItems().length-1);
	_mcotiza_getFormDatosGenerales().reset();
	_mcotiza_storeIncisos.removeAll();
	_mcotiza_getFormInciso().reset();
	_mcotiza_navView.items.items[0].setActiveItem(0);
	debug('<_mcotiza_nueva');
}
function _mcotiza_enviarPorCorreo()
{
	debug('>_mcotiza_enviarPorCorreo');
	Ext.Msg.prompt(
		'Enviar cotizaci&oacute;n',
		'Correo(s) electr&oacute;nico(s):',
		function (buttonId, value)
		{
		    debug(buttonId,value,'c');
			if(buttonId=='ok')
			{
				var valido = true;
				if(valido)
				{
					valido = value.length>0;
					if(!valido)
					{
						Ext.Msg.alert('Error','Introduce un correo');
					}
				}
				if(valido)
				{
					_mcotiza_navView.setMasked(
					{
						xtype    : 'loadmask'
						,message : 'Enviando...'
					});
					Ext.Ajax.request(
	    			{
	    				url     : _mcotiza_urlEnviarCorreo
	    				,params :
	    				{
	    					to         : value,
	    					urlArchivo : _mcotiza_urlImprimirCotiza
                                         + '?p_unieco='      + _mcotiza_smap1.cdunieco
                                         + '&p_ramo='        + _mcotiza_smap1.cdramo
                                         + '&p_subramo='     + _mcotiza_smap1.cdtipsit
                                         + '&p_estado=W'
                                         + '&p_poliza='      + Ext.ComponentQuery.query('#_mcotiza_nmpolizaField')[0].getValue()
                                         + '&p_suplem=0'
                                         + '&p_cdplan='      + _mcotiza_selectedCdplan
                                         + '&p_plan='        + _mcotiza_selectedCdplan
                                         + '&p_perpag='      + _mcotiza_selectedCdperpag
                                         + '&p_ntramite='    + _mcotiza_smap1.ntramite            
                                         + '&p_cdusuari='    + _mcotiza_smap1.cdusuari
                                         + '&destype=cache'
                                         + "&desformat=PDF"
                                         + "&userid="        + _mcotiza_reportsServerUser
                                         + "&ACCESSIBLE=YES"
                                         + "&report="        + _mcotiza_reporteCotizacion
                                         + "&paramform=no",
							nombreArchivo : 'cotizacion_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf'
	    			    },
	    			    callback : function(options,success,response)
	    			    {
							_mcotiza_navView.setMasked(false);
	    			    	if (success)
	    			    	{
	    			    		var json = Ext.decode(response.responseText);
								debug('response:',json);
	    			    		if (json.success == true)
	    			    		{
									Ext.Msg.alert('Aviso','Cotizaci&oacute;n enviada');
	    			    		}
	    			    		else
	    			    		{
	    			    			Ext.Msg.alert('Error','Error al enviar');
	    			    		}
	    			    	}
	    			    	else
	    			    	{
	    			    		Ext.Msg.alert('Error','Error de comunicaci&oacute;n');
	    			    	}
	    			    }
	    			});
				}
			}
		},
		null,
		false,
		null,
		{
		    autoCapitalize: true,
		    placeHolder: 'Separados por ;'
		}
	);
	debug('<_mcotiza_enviarPorCorreo');
}

function _mcotiza_comprar()
{
    debug('>_mcotiza_comprar');
    _mcotiza_navView.setMasked(
    {
        xtype    : 'loadmask'
        ,message : 'Generando tr&aacute;mite...'
    });
    Ext.Ajax.request( 
    {
        url      : _mcotiza_urlComprar
        ,params  :
        {
            comprarNmpoliza        : _mcotiza_getFieldNmpoliza().getValue()
            ,comprarCdplan         : _mcotiza_selectedCdplan
            ,comprarCdperpag       : _mcotiza_selectedCdperpag
            ,comprarCdramo         : _mcotiza_smap1.cdramo
            ,comprarCdciaaguradora : '20'
            ,comprarCdunieco       : _mcotiza_smap1.cdunieco
            ,cdtipsit              : _mcotiza_smap1.cdtipsit
            ,'smap1.fechaInicio'   : Ext.Date.format(_mcotiza_getFieldFechaInicio().getValue(),'d/m/Y')
            ,'smap1.fechaFin'      : Ext.Date.format(_mcotiza_getFieldFechaFin().getValue(),'d/m/Y')
            ,'smap1.nombreTitular' : ''
            ,'smap1.ntramite'      : _mcotiza_smap1.ntramite
        }
        ,success : function(response,opts)
        {
        	_mcotiza_navView.setMasked(false);
            var json = Ext.decode(response.responseText);
            debug('response:',json);
            if (json.success == true)
            {
                if(_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')
                {
                    Ext.Msg.alert('Tr&aacute;mite generado','Se ha generado el tr&aacute;mite '+json.smap1.ntramite,function()
                    {
                        window.location.href=
                            _mcotiza_urlDatosComplementarios
                                +'?cdunieco='      + _mcotiza_smap1.cdunieco
                                +'&cdramo='        + _mcotiza_smap1.cdramo
                                +'&estado=W'
                                +'&nmpoliza='      + _mcotiza_getFieldNmpoliza().getValue()
                                +'&map1.ntramite=' + json.smap1.ntramite
                                +'&cdtipsit='      + _mcotiza_smap1.cdtipsit;
                    });
                }
                else
                {
           		    _mcotiza_getBotonLectura().setText('Tr&aacute;mite '+json.smap1.ntramite+' generado para '+_mcotiza_selectedDsplan);
           		    _mcotiza_getBotonLectura().show();
           		    _mcotiza_getBotonPlan().hide();
                    _mcotiza_getBotonComprar().hide();
				    _mcotiza_getBotonCorreo().hide();
                    _mcotiza_getBotonImprimir().hide();
            	    Ext.Msg.alert('Tr&aacute;mite generado','Se ha generado el tr&aacute;mite '+json.smap1.ntramite);
            	}
            }
            else
            {
            	Ext.Msg.alert('Error','Error al cotizar');
            }
        }
        ,failure : function()
        {
        	_mcotiza_navView.setMasked(false);
        	Ext.Msg.alert('Error','Error de comunicaci&oacute;n');
        }
    });
    debug('<_mcotiza_comprar');
}

function _mcotiza_imprimir()
{
    var me = this;
    var urlRequestImpCotiza = _mcotiza_urlImprimirCotiza
            + '?p_unieco='      + _mcotiza_smap1.cdunieco
            + '&p_ramo='        + _mcotiza_smap1.cdramo
            + '&p_subramo='     + _mcotiza_smap1.cdtipsit
            + '&p_estado=W'
            + '&p_poliza='      + Ext.ComponentQuery.query('#_mcotiza_nmpolizaField')[0].getValue()
            + '&p_suplem=0'
            + '&p_cdplan='      + _mcotiza_selectedCdplan
            + '&p_plan='        + _mcotiza_selectedCdplan
            + '&p_perpag='      + _mcotiza_selectedCdperpag
            + '&p_ntramite='    + _mcotiza_smap1.ntramite            
            + '&p_cdusuari='    + _mcotiza_smap1.cdusuari
            + '&destype=cache'
            + "&desformat=PDF"
            + "&userid="        + _mcotiza_reportsServerUser
            + "&ACCESSIBLE=YES"
            + "&report="        + _mcotiza_reporteCotizacion
            + "&paramform=no";
    debug('urlRequestImpCotiza:',urlRequestImpCotiza);
    
    window.open(
        '<s:url namespace="/documentos" action="descargaDocInline" />?contentType=application/pdf&url='+encodeURIComponent(urlRequestImpCotiza)
        ,'_blank'
        ,'width=800, height=600'
        );
}

function _mcotiza_tarifaSelect(columnName, record, row, column, eOpts)
{
    debug('record',record);
    debug('columnName',columnName);
    if(columnName=='DSPERPAG')
    {
        debug('DSPERPAG');
        _mcotiza_getBotonPlan().setText('Sin plan');
        _mcotiza_getBotonComprar().setDisabled(true);
        _mcotiza_getBotonCorreo().setDisabled(true);
        _mcotiza_getBotonImprimir().setDisabled(true);
    }
    else
    {
        // M N P R I M A X
        //0 1 2 3 4 5 6 7
        _mcotiza_selectedCdperpag = record.get("CDPERPAG");
        _mcotiza_selectedCdplan   = columnName.substr(7);
        _mcotiza_selectedDsplan   = record.get("DSPLAN"+_mcotiza_selectedCdplan);
        _mcotiza_selectedNmsituac = record.get("NMSITUAC");
        debug('_mcotiza_selectedCdperpag',_mcotiza_selectedCdperpag);
        debug('_mcotiza_selectedCdplan',_mcotiza_selectedCdplan);
        debug('_mcotiza_selectedDsplan',_mcotiza_selectedDsplan);
        debug('_mcotiza_selectedNmsituac',_mcotiza_selectedNmsituac);
        _mcotiza_getBotonPlan().setText(_mcotiza_selectedDsplan);
        _mcotiza_getBotonComprar().setDisabled(false);
        _mcotiza_getBotonCorreo().setDisabled(false);
        _mcotiza_getBotonImprimir().setDisabled(false);
    }
}

function _mcotiza_construirGrid(json)
{
	debug('>_mcotiza_construirGrid');
	debug('json:',json);
	
	Ext.define('_mcotiza_modeloTarifa',
    {
        extend  : 'Ext.data.Model'
        ,config :
        {
        	fields : Ext.JSON.decode(json.smap1.fields)
        }
    });
	
	var columns = Ext.JSON.decode(json.smap1.columnas,false);
	debug('columns:',columns);
	Ext.ComponentQuery.query('#_mcotiza_loadButton')[0].hide();
	if(Ext.ComponentQuery.query('#_mcotiza_resetButton').length==0)
	{
		Ext.ComponentQuery.query('navigationview')[0].getNavigationBar().add(
		{
		    xtype    : 'button'
		    ,itemId  : '_mcotiza_resetButton'
		    ,ui      : 'confirm'
		    ,text    : 'Nueva'
		    ,align   : 'right'
		    ,handler : _mcotiza_nueva
		});
	}
	var grid = Ext.create('Ext.grid.Grid',
	{
		title    : 'Cotizaci&oacute;n'
		,itemId  : '_mcotiza_GridTarifas'
		,columns : columns
		,items   :
		[
		    {
		    	xtype   : 'toolbar'
		    	,docked : 'bottom'
	    		,layout :
                {
                    pack : 'center'
                }
		    	,items  :
		    	[
					{
						xtype     : 'button'
						,text     : ''
						,ui       : 'action'
						,itemId   : '_mcotiza_botonLectura'
						,hidden   : true
					}
		    	    ,{
		    	    	xtype     : 'button'
		    	    	,text     : 'Sin plan'
		    	    	,ui       : 'confirm'
		    	    	,itemId   : '_mcotiza_botonPlan'
		    	    }
		    	    ,{
		    	    	xtype     : 'button'
		    	    	,text     : 'Generar'
		    	    	,disabled : true
		    	    	,itemId   : '_mcotiza_botonComprar'
		    	    	,handler  : _mcotiza_comprar
		    	    }
					,{
						xtype     : 'button'
						,icon     : '${ctx}/resources/fam3icons/icons/email.png'
						,disabled : true
						,itemId   : '_mcotiza_botonCorreo'
						,handler  : _mcotiza_enviarPorCorreo
					}
		    	    ,{
		    	    	xtype     : 'button'
		    	    	,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
		    	    	,disabled : true
		    	    	,itemId   : '_mcotiza_botImprimirId'
		    	    	,handler  : _mcotiza_imprimir
		    	    }
		    	]
		    }
		]
		,store   : Ext.create('Ext.data.Store',
        {
            model : '_mcotiza_modeloTarifa'
            ,data : json.slist2
        })
        ,listeners:
        {
        	itemtap: function (dataview, index, target, record, e, eOpts)
        	{
        		var colText      = e.target.$column._text;
        		var colDataIndex = e.target.$column._dataIndex;
        		var colIndex     = -1;
        		debug('dataview:',dataview);
        		debug('event:',e);
        		debug('colText:',colText);
        		debug('colDataIndex:',colDataIndex);
        		for(var i=0;i<dataview._columns.length;i++)
        		{
        			var column = dataview._columns[i];
        			if(column.text==colText)
        			{
        				debug('indice:',i);
        				colIndex = i;
        			}
        		}
        		debug('colIndex:',colIndex);
        		_mcotiza_tarifaSelect(colDataIndex, record, index, colIndex, eOpts);
        	}
        }
	});
	_mcotiza_navView.push(grid);
	debug('<_mcotiza_construirGrid');
}

function _mcotiza_validaAsegurado(values)
{
	debug('>_mcotiza_validaAsegurado',values);
	var valido = true;
	if(valido)
	{
		for(var att in values)
        {
			debug('atrubito a validar:',att,values[att]);
            var estaEnModeloExtra=false;
            for(var i=0;i<_mcotiza_modeloExtraFields.length;i++)
            {
                if(att==_mcotiza_modeloExtraFields[i].name)
                {
                    estaEnModeloExtra=true;
                }
            }
            if(!estaEnModeloExtra&&att!='rowIndex')
            {
                var value=values[att];
                valido=valido&&value;
                if(!valido)
                {
                    debug('falta: ',att);
                }
            }
        }
        if(!valido)
        {
            Ext.Msg.alert('Aviso','Verifica los datos');
        }
	}
	debug('<_mcotiza_validaAsegurado');
	return valido;
}

function _mcotiza_cotiza()
{
	debug('_mcotiza_cotiza');
	var valido = true;
	
	////// validacion de datos generales //////
	if(valido)
	{
		var values = _mcotiza_getFormDatosGenerales().getValues();
		debug('values:',values);
		for(var att in values)
		{
			if(att!='nmpoliza')
			{
				if(values[att]==null || values[att]==undefined || (values[att]+'x')=='x')
				{
				    debug('falta:',att,values[att],'<-valor');
					valido = false;
				}
			}
		}
		if(!valido)
		{
			Ext.Msg.alert('Aviso','Verifica los datos generales',function()
			{
				_mcotiza_navView.items.items[0].setActiveItem(0);
			});
		}
	}
	////// validacion de datos generales //////
	
	////// factor convenido //////
	if((_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')&&valido)
	{
	    debug("_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getValue():",_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getValue());
	    debug("_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMinValue():",_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMinValue());
	    debug("_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMaxValue():",_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMaxValue());
	    var value=_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getValue()-0;
	    var minval=_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMinValue()-0;
	    var maxval=_mcotiza_navView.down('[name=parametros.pv_otvalor07]').getMaxValue()-0;
	    if(valido)
	    {
	        valido=value>=minval;
	        if(!valido)
	        {
    	        Ext.Msg.alert('Aviso','La suma asegurada no puede ser menor a '+minval.toFixed(2),function()
                {
                    _mcotiza_navView.items.items[0].setActiveItem(0);
                });
	        }
	    }
	    if(valido)
	    {
	        valido = value<=maxval;
	        if(!valido)
	        {
	            Ext.Msg.alert('Aviso','La suma asegurada no puede ser mayor a '+maxval.toFixed(2),function()
                {
                    _mcotiza_navView.items.items[0].setActiveItem(0);
                });
	        }
	    }
	}
	////// factor convenido //////
	
	////// al menos un inciso //////
	if(valido&&_mcotiza_necesitoIncisos)
	{
		valido = _mcotiza_storeIncisos.getCount()>0;
		if(!valido)
		{
			Ext.Msg.alert('Aviso','Introduce al menos un inciso',function()
            {
                _mcotiza_navView.items.items[0].setActiveItem(1);
            });
		}
	}
	////// al menos un inciso //////
	
	if(valido)
	{
		valido = _mcotiza_validacion_custom();
	}
	
	////// request //////
	if(valido)
	{
		var json=
        {
            slist1 : []
            ,smap1 : _mcotiza_smap1
        };
		if(_mcotiza_necesitoIncisos)
		{
	        _mcotiza_storeIncisos.each(function(record)
	        {
	            var inciso=_mcotiza_getFormDatosGenerales().getValues();
	            for(var key in inciso)
	            {
	                var value=inciso[key];
	                debug(typeof value,key,value);
	                if((typeof value=='object')&&value&&value.getDate)
	                {
	                    debug('fecha object');
	                    var fecha='';
	                    fecha+=value.getDate();
	                    fecha+='/';
	                    fecha+=value.getMonth()+1<10?
	                            (('x'+(value.getMonth()+1)).replace('x','0'))
	                            :(value.getMonth()+1);
	                    fecha+='/';
	                    fecha+=value.getFullYear();
	                    value=fecha;
	                    debug('pasado a:',value);
	                    inciso[key]=value;
	                }
	            }
	            for(var key in record.data)
	            {
	                var value=record.data[key];
	                debug(typeof value,key,value);
	                if((typeof value=='object')&&value&&value.getDate)
	                {
	                	debug('fecha object');
	                    var fecha='';
	                    fecha+=value.getDate();
	                    fecha+='/';
	                    fecha+=value.getMonth()+1<10?
	                            (('x'+(value.getMonth()+1)).replace('x','0'))
	                            :(value.getMonth()+1);
	                    fecha+='/';
	                    fecha+=value.getFullYear();
	                    value=fecha;
	                    debug('pasado a:',value);
	                }
	                else
	                {
	                	debug('no fecha object');
	                }
	                inciso[key]=value;
	            }
	            json['slist1'].push(inciso);
	        });
		}
		else
		{
		    debug('producto sin incisos');
			var inciso=_mcotiza_getFormDatosGenerales().getValues();
			for(var key in inciso)
			{
			    var value=inciso[key];
                debug(typeof value,key,value);
                if((typeof value=='object')&&value&&value.getDate)
                {
                    debug('fecha object');
                    var fecha='';
                    fecha+=value.getDate();
                    fecha+='/';
                    fecha+=value.getMonth()+1<10?
                            (('x'+(value.getMonth()+1)).replace('x','0'))
                            :(value.getMonth()+1);
                    fecha+='/';
                    fecha+=value.getFullYear();
                    value=fecha;
                    debug('pasado a:',value);
                    inciso[key]=value;
                }
            }
			if(_mcotiza_storeIncisos.getCount()==1)
            {
                var record=_mcotiza_storeIncisos.getAt(0);
                debug('record unico:',record);
                for(var key in record.data)
                {
                    var value=record.data[key];
                    debug(typeof value,key,value);
                    if((typeof value=='object')&&value&&value.getDate)
                    {
                        debug('fecha object');
                        var fecha='';
                        fecha+=value.getDate();
                        fecha+='/';
                        fecha+=value.getMonth()+1<10?
                                (('x'+(value.getMonth()+1)).replace('x','0'))
                                :(value.getMonth()+1);
                        fecha+='/';
                        fecha+=value.getFullYear();
                        value=fecha;
                        debug('pasado a:',value);
                    }
                    else
                    {
                        debug('no fecha object');
                    }
                    inciso[key]=value;
                }
            }
			json['slist1'].push(inciso);
		}
        debug('json para cotizar: ',json);
		_mcotiza_navView.setMasked(
		{
		    xtype    : 'loadmask'
		    ,message : 'Cotizando...'
		});
        Ext.Ajax.request(
        {
            url       : _mcotiza_smap1['externo']=='si'?_mcotiza_urlCotizarExterno:_mcotiza_urlCotizar
            ,jsonData : json
            ,success  : function(response)
            {
				_mcotiza_navView.setMasked(false);
            	json = Ext.decode(response.responseText);
            	debug('response:',json);
            	Ext.ComponentQuery.query('#_mcotiza_nmpolizaField')[0].setValue(json.smap1.nmpoliza);
            	_mcotiza_construirGrid(json);
            }
            ,failure  : function()
            {
				_mcotiza_navView.setMasked(false);
            	Ext.Msg.alert('Aviso','Error al cotizar');
            }
        });
	}
	////// request //////
}

function _g_heredarCombo(remoto,compId,compAnteriorId)
{
	debug('>_g_heredarCombo',remoto,compId,compAnteriorId);
	var thisCmp=Ext.getCmp(compId);
	debug('Heredar:',thisCmp);
	if(!thisCmp.noEsPrimera||remoto==true)
	{
		debug('Hereda por primera vez o porque la invoca el padre');
		thisCmp.noEsPrimera=true;
		thisCmp.getStore().load(
		{
			params    :
			{
				'params.idPadre':Ext.getCmp(compAnteriorId).getValue()
			}
		    ,callback : function()
		    {
		    	
		    	var valorActual=thisCmp.getValue();
		    	var dentro=false;
		    	thisCmp.getStore().each(function(record)
		    	{
		    		if(valorActual==record.get('key'))
		    		{
		    			dentro=true;
		    		}
		    	});
		    	if(!dentro)
		    	{
		    		thisCmp.reset();
		    	}
		    }
		});
	}
	else
	{
		debug('No hereda porque es un change repetitivo');
	}
}

function _mcotiza_incisoItemDisclosure(listPanel,event,rowIndex)
{
	debug('>_mcotiza_incisoItemDisclosure');
	var recordTmp = _mcotiza_storeIncisos.getAt(rowIndex);
	debug('recordTmp:',recordTmp);
	var formIncisoTmp = _mcotiza_getFormInciso();
	debug('formIncisoTmp:',formIncisoTmp);
	formIncisoTmp.setRecord(recordTmp);
	formIncisoTmp.items.items[0].items.items[0].setValue(rowIndex);
	_mcotiza_navView.items.items[0].setActiveItem(1);
	debug('<_mcotiza_incisoItemDisclosure');
}

function _mcotiza_nuevoAsegurado()
{
	debug('>_mcotiza_nuevoAsegurado');
	var valuesTmp = _mcotiza_getFormInciso().getValues();
	debug('valuesTmp:',valuesTmp);
	var valido = true;
	if(valido)
	{
	    if(!_mcotiza_necesitoIncisos)
	    {
	        valido=_mcotiza_storeIncisos.getCount()<1;
	        if(!valido)
	        {
                Ext.Msg.alert('Aviso','Solo se puede introducir un inciso');
            }
        }
	}
	if(valido)
	{
	    valido = _mcotiza_validaAsegurado(valuesTmp);
	}
	if(valido)
	{
		var recordTmp = new _mcotiza_Inciso(valuesTmp);
		debug('recordTmp:',recordTmp);
		_mcotiza_storeIncisos.add(recordTmp);
		_mcotiza_getFormInciso().reset();
		_mcotiza_navView.items.items[0].setActiveItem(2);
	}
	debug('<_mcotiza_nuevoAsegurado');
}

function _mcotiza_borrarAsegurado()
{
    debug('>_mcotiza_borrarAsegurado');
    var valido   = true;
    var rowIndex = _mcotiza_getFormInciso().items.items[0].items.items[0].getValue();
    debug('rowIndex:',rowIndex,(rowIndex+'').length,'a');
    if(valido)
    {
    	valido = rowIndex!=null&&rowIndex!=undefined&&(rowIndex+'').length>0;
    	if(!valido)
    	{
    		Ext.Msg.alert('Aviso','Debes seleccionar un inciso');
    	}
    }
    if(valido)
    {
    	_mcotiza_getFormInciso().items.items[0].items.items[0].setValue('');
    	_mcotiza_storeIncisos.removeAt(rowIndex);
    	_mcotiza_getFormInciso().reset();
    	_mcotiza_navView.items.items[0].setActiveItem(2);
    }
    debug('<_mcotiza_borrarAsegurado');
}

function _mcotiza_editarAsegurado()
{
    debug('>_mcotiza_editarAsegurado');
    var valuesTmp = _mcotiza_getFormInciso().getValues();
    debug('valuesTmp:',valuesTmp);
    var valido   = _mcotiza_validaAsegurado(valuesTmp);
    var rowIndex = _mcotiza_getFormInciso().items.items[0].items.items[0].getValue();
    debug('rowIndex:',rowIndex,(rowIndex+'').length,'a');
    if(valido)
    {
        valido = rowIndex!=null&&rowIndex!=undefined&&(rowIndex+'').length>0;
        if(!valido)
        {
            Ext.Msg.alert('Aviso','Debes seleccionar un inciso');
        }
    }
    if(valido)
    {
        _mcotiza_getFormInciso().items.items[0].items.items[0].setValue('');
        _mcotiza_storeIncisos.removeAt(rowIndex);
        var recordTmp = new _mcotiza_Inciso(valuesTmp);
        debug('recordTmp:',recordTmp);
        _mcotiza_storeIncisos.insert(rowIndex,recordTmp);
        _mcotiza_getFormInciso().reset();
        _mcotiza_navView.items.items[0].setActiveItem(2);
    }
    debug('<_mcotiza_editarAsegurado');
}

function debug(a,b,c,d)
{
	if(false)
	{
		if(d!=undefined)
		{
			console.log(a,b,c,d);
		}
		else if(c!=undefined)
		{
			console.log(a,b,c);
		}
		else if(b!=undefined)
		{
			console.log(a,b);
		}
		else
		{
			console.log(a);
		}
	}
}
////// funciones //////

Ext.setup({onReady:function()
{
	////// modelos //////
	_mcotiza_validacion_custom = function()
    {
        Ext.Msg.alert('Aviso','Falta definir la validaci&oacute;n para el producto');
        return true;
    };
    _mcotiza_incisoTpl = 'Inciso';
    <s:if test='%{getImap().get("validacionCustomButton")!=null}'>
		var botonValidacionCustom = <s:property value="imap.validacionCustomButton" escapeHtml="false"/>;
	    _mcotiza_validacion_custom=botonValidacionCustom.handler;
	    _mcotiza_incisoTpl=botonValidacionCustom.text;
	</s:if>
	debug('_mcotiza_incisoTpl:',_mcotiza_incisoTpl);
	
	Ext.define('MiSelectField',
	{
		extend  : 'Ext.field.Select'
		,config :
		{
			
		}
	    ,initialize : function()
	    {
	    	debug('MiSelectField initialize');
	    	var me = this;
	    	var lista;
	    	me.callParent();
	    	if(me.getUsePicker())
	    	{
	    		debug('has picker');
	            lista = me.getPhonePicker();
	    	}
	    	else
	    	{
	    		debug('has listPanel');
	    		lista = me.getTabletPicker();
	    	}
    		lista.addListener('hide',function()
    		{
    			var thisCmp = Ext.getCmp(me.id);
    			var hasBlur = thisCmp.hasListener('blur');
    			debug('listPanel hide:',me.id,'hasBlur',hasBlur);
    			if(hasBlur)
    			{
    				var lio = me.id.lastIndexOf('_');
    				debug('lio:',lio);
    				var idPrefix = me.id.substring(0,lio+1);
    				var idNum    = me.id.substring(lio+1,me.id.length);
    				debug('idPrefix:',idPrefix,'idNum:',idNum);
    				if(Ext.getCmp(idPrefix+((idNum*1)+1)).heredar)
    				{
    					Ext.getCmp(idPrefix+((idNum*1)+1)).heredar(true);
    				}
    				else
    				{
    					_g_heredarCombo(true,idPrefix+((idNum*1)+1),idPrefix+(idNum*1));
    				}
    			}
    		});
	    }
	});
	
	Ext.define('Generic',
	{
	    extend : 'Ext.data.Model',
	    config :
	    {
	    	fields:
		    [
		        { name : 'key'   , type : 'string' },
		        { name : 'value' , type : 'string' }
		    ]
	    }
	});
	
	var tmp = [];
    <s:if test='%{getImap().get("fieldsIndividuales")!=null}'>
        tmp.push(<s:property value="imap.fieldsIndividuales" />);
    </s:if>
    <s:if test='%{getImap().get("modeloExtraFields")!=null}'>
        tmp.push(<s:property value="imap.modeloExtraFields"  />);
    </s:if>
	Ext.define('_mcotiza_Inciso',
	{
		extend  : 'Ext.data.Model'
		,config :
		{
			fields : tmp
		}
	});
	
	Ext.define('_mcotiza_IncisoAgrupado',
	{
	    extend  : 'Ext.data.Model'
	    ,config :
	    {
	        fields :
	        [
	            <s:property value='imap.fieldsAgrupados' />
	        ]
	    }
	});
	////// modelos //////
	
	////// stores //////
	_mcotiza_storeIncisos = Ext.create('Ext.data.Store',
    {
        model     : '_mcotiza_Inciso'
    });
    
    _g_storeSino = Ext.create('Ext.data.Store',
	{
	    model : 'Generic'
	    ,data :
	    [
	        {
	            key    : 'S'
	            ,value : 'Si'
	        }
	        ,{
	            key    : 'N'
	            ,value : 'No'
	        }
	    ]
	});
	////// stores //////
	
	_mcotiza_navView = Ext.create('Ext.NavigationView',
	{
		defaultBackButtonText : 'Atr&aacute;s'
		,items                :
		[
		    Ext.create('Ext.tab.Panel',
		    {
		    	title           : 'Datos generales'
		    	,tabBarPosition : 'bottom'
		    	,items          :
		    	[
			        {
			        	xtype    : 'formpanel'
			            ,title   : '&nbsp;Datos generales&nbsp;'
			            ,titulo  : 'Datos generales'
			            ,itemId  : '_mcotiza_formDatosGene'
			            ,iconCls : 'info'
			            ,items   :
			            [
			                {
			                    xtype  : 'fieldset'
			                    ,title : 'Datos generales'
			                    ,items :
			                    [
			                        {
			                            xtype       : 'numberfield'
			                            ,itemId     : '_mcotiza_nmpolizaField'
			                            ,label      : 'COTIZACI&Oacute;N'
			                            ,readOnly   : true
			                            ,name       : 'nmpoliza'
			                            ,labelAlign : 'top'
			                        }
			                        ,<s:property value="imap.camposAgrupados"/>
			                        ,{
			                            xtype       : 'datepickerfield'
			                            ,itemId     : '_mcotiza_feinivalField'
			                            ,labelAlign : 'top'
			                            ,label      : 'INICIO DE VIGENCIA'
			                            ,name       : 'feini'
			                            ,value      : new Date()
			                            ,listeners  :
			                            {
			                                change : function(field,value)
			                                {
			                                    debug(value);
			                                    debug(Ext.ComponentQuery.query('#_mcotiza_fefinvalField')[0]);
			                                    try
			                                    {
			                                        Ext.ComponentQuery.query('#_mcotiza_fefinvalField')[0].setValue(Ext.Date.add(value,Ext.Date.YEAR,1));
			                                    }
			                                    catch (e) {}
			                                }
			                            }
			                        }
			                        ,{
			                            xtype       : 'datepickerfield'
			                            ,itemId     : '_mcotiza_fefinvalField'
	                                    ,labelAlign : 'top'
			                            ,label      : 'FIN DE VIGENCIA'
			                            ,name       : 'fefin'
			                            ,readOnly   : true
			                            ,value      : Ext.Date.add(new Date(),Ext.Date.YEAR,1)
			                        }
			                    ]
			                }
			            ]
			        }
			        ,{
			        	xtype    : 'formpanel'
			        	,title   : (_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')?'&nbsp;Contratante&nbsp;':'&nbsp;Inciso&nbsp;'
			        	,titulo  : (_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')?'Edici&oacute;n de contratante':'Edici&oacute;n de incisos'
			        	//,hidden  : !_mcotiza_necesitoIncisos
			        	,iconCls : 'add'
			        	,itemId  : '_mcotiza_formAsegurados'
			        	,items   :
			        	[
				        	{
				        		xtype  : 'fieldset'
				        		,title : (_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')?'Edici&oacute;n de contratante':'Edici&oacute;n de incisos'
				        		,items :
				        		[
				        		    {
				        		    	xtype     : 'numberfield'
				        		    	,label    : 'rowIndex'
				        		    	,name     : 'rowIndex'
				        		    	,readOnly : true
				        		    	,hidden   : true
				        		    }
				        		    <s:if test='%{getImap().get("itemsIndividuales")!=null}' >
					        		    ,<s:property value="imap.itemsIndividuales" />
	                                </s:if>
				        		    <s:if test='%{getImap().get("modeloExtraItems")!=null}' >
				        		        ,<s:property value="imap.modeloExtraItems"  />
				        		    </s:if>
				        		]
				        	}
				        	,{
				        		xtype   : 'toolbar'
				        		,docked : 'bottom'
				        		,layout :
				        		{
				        			pack : 'center'
				        		}
				        		,items  :
				        		[
                                    {
                                        xtype    : 'button'
                                        ,text    : 'Agregar nuevo'
                                        ,ui      : 'confirm'
                                        ,handler : _mcotiza_nuevoAsegurado
                                    }
                                    ,{
                                        xtype    : 'button'
                                        ,text    : 'Actualizar'
                                        ,ui      : 'action'
                                        ,handler : _mcotiza_editarAsegurado
                                    }
                                    ,{
                                        xtype    : 'button'
                                        ,text    : 'Borrar'
                                        ,ui      : 'decline'
                                        ,handler : _mcotiza_borrarAsegurado
                                    }
                                ]
				        	}
				        ]
			        }
			        ,{
                        xtype             : 'list'
                        ,title            : (_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')?'&nbsp;Contratante&nbsp;':'&nbsp;Incisos&nbsp;'
                        ,titulo           : (_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')?'Contratante':'Incisos'
                        ,iconCls          : 'more'
                        //,hidden           : !_mcotiza_necesitoIncisos
                        ,itemId           : '_mcotiza_listAsegurados'
                        ,store            : _mcotiza_storeIncisos
                        ,itemTpl          : _mcotiza_incisoTpl
                        ,onItemDisclosure : _mcotiza_incisoItemDisclosure
                    }
			        ,{
			        	xtype    : 'formpanel'
			        	,title   : 'Cotizar'
			        	,titulo  : 'Cotizar'
			        	,iconCls : 'star'
			        }
			        ,{
			            xtype    : 'formpanel'
			            ,title   : 'Regresar'
			            ,titulo  : 'Regresar'
			            ,iconCls : 'arrow_left'
			        }
		    	]
		    	,listeners      :
	            {
	                activeitemchange : function (tabpanel,value, oldValue)
	                {
	                    debug('_p1_TabbedAjuste activate:',tabpanel.getActiveItem());
	                    debug('value:',value,'oldValue:',oldValue);
	                    debug('index:',tabpanel.indexOf(value));
	                    _mcotiza_navView.getNavigationBar().setTitle(tabpanel.getActiveItem().config.titulo);
	                    var index = tabpanel.indexOf(value);
	                    if(tabpanel.getActiveItem().config.titulo.toLowerCase()=='cotizar')
	                    {
	                    	_mcotiza_cotiza();
	                    }
	                    else if(tabpanel.getActiveItem().config.titulo.toLowerCase()=='regresar')
                        {
	                    	_mcotiza_navView.setMasked(
                   	        {
                   	            xtype    : 'loadmask'
                   	            ,message : 'Cargando...'
                   	        });
                            window.location.replace('${ctx}');
                        }
	                }
	            }
		    })
		]
	});
	
	if(_mcotiza_smap1.cdtipsit=='AF'||_mcotiza_smap1.cdtipsit=='PU')
	{
	    _mcotiza_navView.down('[name=parametros.pv_otvalor03]').addListener('blur',function(comp)
        {
            var vim=comp.getValue();
            debug('>llamando a nada:',vim);
            maskui();
            Ext.Ajax.request(
            {
                url     : _mcotiza_urlNada
                ,params :
                {
                    'smap1.vim'       : vim
                    ,'smap1.cdramo'   : _mcotiza_smap1.cdramo
                    ,'smap1.cdtipsit' : _mcotiza_smap1.cdtipsit
                    ,'smap1.tipoveh'  : _mcotiza_navView.down('[name=parametros.pv_otvalor31]').getValue()
                    ,'smap1.codpos'   : _mcotiza_navView.down('[name=parametros.pv_otvalor08]').getValue()
                }
                ,success : function(response)
                {
                    unmaskui();
                    var json=Ext.JSON.decode(response.responseText);
                    debug('nada response:',json);
                    if(json.success)
                    {
                        var precioDolar = _mcotiza_navView.down('[name=parametros.pv_otvalor24]').getValue()-0;
                        debug('precioDolar:',precioDolar);
                        _mcotiza_navView.down('[name=parametros.pv_otvalor04]').setValue(json.smap1.AUTO_MARCA);
                        _mcotiza_navView.down('[name=parametros.pv_otvalor05]').setValue(json.smap1.AUTO_ANIO);
                        _mcotiza_navView.down('[name=parametros.pv_otvalor06]').setValue(json.smap1.AUTO_DESCRIPCION);
                        _mcotiza_navView.down('[name=parametros.pv_otvalor07]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                        _mcotiza_navView.down('[name=parametros.pv_otvalor26]').setValue((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2));
                        _mcotiza_navView.down('[name=parametros.pv_otvalor07]').setMinValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                        _mcotiza_navView.down('[name=parametros.pv_otvalor07]').setMaxValue(((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                        debug('set min value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1-(json.smap1.FACTOR_MIN-0)));
                        debug('set max value:',((json.smap1.AUTO_PRECIO*precioDolar).toFixed(2))*(1+(json.smap1.FACTOR_MAX-0)));
                    }
                    else
                    {
                        Ext.Msg.alert('Error',json.error);
                    }
                }
                ,failure : function()
                {
                    unmaskui();
                    Ext.Msg.alert('Error','Error de comunicaci&oacute;n');
                }
            });
            debug('<llamando a nada');
        });
        var comboTipoValor =_mcotiza_navView.down('[name=parametros.pv_otvalor02]');
        var itemSumaAsegu  =_mcotiza_navView.down('[name=parametros.pv_otvalor07]');
        var changeFunction = function()
        {
            debug('>comboTipoValor change');
            itemSumaAsegu.setValue('');
            itemSumaAsegu.setReadOnly((comboTipoValor.getValue()+'x')=='2x');
            debug('<comboTipoValor change');
        };
        comboTipoValor.addListener('change',changeFunction);
        changeFunction();
        _mcotiza_navView.down('[name=parametros.pv_otvalor05]').addListener('blur',function()
        {
             var anioActual = new Date().getFullYear();
             var max = anioActual-5;
             var min = anioActual-20;
             var value = _mcotiza_navView.down('[name=parametros.pv_otvalor05]').getValue()-0;
             debug('anioActual:',anioActual);
             debug('max:',max);
             debug('min:',min);
             debug('value:',value);
             if(value<min||value>max)
             {
                 _mcotiza_navView.down('[name=parametros.pv_otvalor05]').setValue('');
                 Ext.Msg.alert('Aviso','El modelo debe estar en el rango '+min+'-'+max);
             }
        });
	}
	
	<s:if test='%{getSmap1().get("CDATRIBU_DERECHO")!=null}'>
	    var form=_mcotiza_getFormDatosGenerales();
	    var items=form.items.items[0].items.items;
        debug('items a reordenar:',items);
        var cdatribus_derechos=_mcotiza_smap1.CDATRIBU_DERECHO.split(',');
        debug('cdatribus_derechos:',cdatribus_derechos);
        var itemsIzq=[];
        var itemsDer=[];
        for(var i=0;i<items.length;i++)
        {
            var iItem=items[i];
            debug('item revisado:',iItem);
            var indexOfIItem=$.inArray(iItem.config.cdatribu,cdatribus_derechos);
            debug('indexOfIItem:',indexOfIItem);
            if(indexOfIItem==-1)
            {
                debug('izquierdo');
                itemsIzq.push(iItem);
            }
            else
            {
                debug('derecho');
                itemsDer.push(iItem);
            }
        }
        form.removeAll(false);
        form.add(
        [
            {
                xtype   : 'fieldset'
                //,title  : 'Datos generales'
                ,itemId : 'mcotizaFieldsetDatosGeneralesIzq'
                ,items  : itemsIzq
            }
            ,{
                xtype   : 'fieldset'
                ,title  : 'Datos de coberturas'
                ,itemId : 'mcotizaFieldsetDatosGeneralesDer'
                ,hidden : true
                ,items  : itemsDer
            }
            ,{
                xtype   : 'toolbar'
                ,docked : 'top'
                ,layout :
                {
                    pack : 'center'
                }
                ,items  :
                [
                    {
                        text     : 'Datos generales'
                        ,ui      : 'decline-round'
                        ,handler : function()
                        {
                            Ext.ComponentQuery.query('#mcotizaFieldsetDatosGeneralesIzq')[0].show();
                            Ext.ComponentQuery.query('#mcotizaFieldsetDatosGeneralesDer')[0].hide();
                        }
                    }
                    ,{
                        text     : 'Datos de coberturas'
                        ,ui      : 'decline-round'
                        ,handler : function()
                        {
                            Ext.ComponentQuery.query('#mcotizaFieldsetDatosGeneralesIzq')[0].hide();
                            Ext.ComponentQuery.query('#mcotizaFieldsetDatosGeneralesDer')[0].show();
                        }
                    }
                ]
            }
        ]);
	</s:if>
	
	Ext.Viewport.add(_mcotiza_navView);
	Ext.ComponentQuery.query('navigationview')[0].getNavigationBar().add(
    {
        xtype    : 'button'
        ,itemId  : '_mcotiza_loadButton'
        ,ui      : 'confirm'
        ,text    : 'Cargar'
        ,align   : 'right'
        ,handler : _mcotiza_load
    });
}});

////// getters //////
function _mcotiza_getFormDatosGenerales()
{
    return Ext.ComponentQuery.query('#_mcotiza_formDatosGene')[0];
}
function _mcotiza_getFormInciso()
{
    return Ext.ComponentQuery.query('#_mcotiza_formAsegurados')[0];
}
function _mcotiza_getBotonComprar()
{
	return Ext.ComponentQuery.query('#_mcotiza_botonComprar')[0];
}
function _mcotiza_getBotonPlan()
{
	return Ext.ComponentQuery.query('#_mcotiza_botonPlan')[0];
}
function _mcotiza_getBotonImprimir()
{
	return Ext.ComponentQuery.query('#_mcotiza_botImprimirId')[0];
}
function _mcotiza_getGridTarifas()
{
	return Ext.ComponentQuery.query('#_mcotiza_GridTarifas')[0];
}
function _mcotiza_getFieldNmpoliza()
{
	return Ext.ComponentQuery.query('#_mcotiza_nmpolizaField')[0];
}
function _mcotiza_getFieldFechaInicio()
{
	return Ext.ComponentQuery.query('#_mcotiza_feinivalField')[0];
}
function _mcotiza_getFieldFechaFin()
{
    return Ext.ComponentQuery.query('#_mcotiza_fefinvalField')[0];
}
function _mcotiza_getBotonLectura()
{
	return Ext.ComponentQuery.query('#_mcotiza_botonLectura')[0];
}
function _mcotiza_getBotonCorreo()
{
	return Ext.ComponentQuery.query('#_mcotiza_botonCorreo')[0];
}
////// getters //////

</script>
</head>
<body>
</body>
</html>
