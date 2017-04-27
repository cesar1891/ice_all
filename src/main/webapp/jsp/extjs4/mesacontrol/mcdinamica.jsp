<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.x-action-col-icon
{
    margin-right: 4px;
}
</style>
<script type="text/javascript">
/*
//Se repara que combos con 'forceSelection' dejen pasar cadena mientras se carga su lista'
Ext.define('ComboBox', {
    override: 'Ext.form.ComboBox',
    
    validator: function(val) {

        var me = this;
        var valido= true;
            
        if (me.forceSelection === true && !Ext.isEmpty(val)) {
        	valido = (me.findRecord('value',val)!== false);
        }
        debug(me.name+' - '+me.forceSelection+' - '+val+ '-' +valido)
        return valido || 'No se encuentra el registro';
    }
});
*/
///////////////////////
////// variables //////
/*///////////////////*/
var mcdinInput                    = [];
var mcdinSesion                   = [];
var mcdinUrlNuevo                 = '<s:url namespace="/mesacontrol" action="guardarTramiteDinamico"  />';
var mcdinUrlCargar                = '<s:url namespace="/mesacontrol" action="loadTareasDinamico"      />';
var _4_urlReload                  = '<s:url namespace="/mesacontrol" action="mcdinamica"              />';
var _4_urlReporte                 = '<s:url namespace="/reportes"    action="procesoObtencionReporte" />';
var _4_urlRecuperacionSimpleLista = '<s:url namespace="/emision"     action="recuperacionSimpleLista" />';
var _4_urlActualizarStatusTramite = '<s:url namespace="/mesacontrol" action="actualizarStatusTramite" />';

//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _4_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_4_smap1:',_4_smap1);

mcdinInput['cdunieco'] = '<s:property value="smap2.pv_cdunieco_i" />';
mcdinInput['ntramite'] = '<s:property value="smap2.pv_ntramite_i" />';
mcdinInput['cdramo']   = '<s:property value="smap2.pv_cdramo_i"   />';
mcdinInput['nmpoliza'] = '<s:property value="smap2.pv_nmpoliza_i" />';
mcdinInput['estado']   = '<s:property value="smap2.pv_estado_i"   />';
mcdinInput['cdagente'] = '<s:property value="smap2.pv_cdagente_i" />';
mcdinInput['status']   = '<s:property value="smap2.pv_status_i"   />';
mcdinInput['cdtipsit'] = '<s:property value="smap2.pv_cdtipsit_i" />';
mcdinInput['fedesde']  = '<s:property value="smap2.pv_fedesde_i"  />';
mcdinInput['fehasta']  = '<s:property value="smap2.pv_fehasta_i"  />';
mcdinInput['tiptra']   = '<s:property value="smap2.pv_cdtiptra_i" />';
mcdinInput['contrarecibo']   = '<s:property value="smap2.pv_contrarecibo_i" />';
mcdinInput['tipoPago']   = '<s:property value="smap2.pv_tipoPago_i" />';
mcdinInput['nfactura']   = '<s:property value="smap2.pv_nfactura_i" />';
mcdinInput['cdpresta']   = '<s:property value="smap2.pv_cdpresta_i" />';
/* mcdinInput['dscontra']   = '<s:property value="smap2.pv_dscontra_i" />'; */
debug('mcdinInput: ',mcdinInput);

mcdinSesion['username'] = '<s:property value="username" />';
mcdinSesion['rol'] = '<s:property value="rol" />';
debug('mcdinSesion: ',mcdinSesion);

var mcdinGrid;
var mcdinStore;
var mcdinFiltro;
var mcdinFormNuevo;
var loadMcdinStore;
var loadMcdinStoreFiltro;

var _4_botones=
{
	xtype         : 'actioncolumn'
	,hidden       : false
	,menuDisabled : true
	,sortable     : false
	,items:
	[
	    <s:property value="imap1.actionColumns" />
	]
};

var _4_statusColumns = [<s:property value="imap1.statusColumns" escapeHtml="false" />];

var _4_botonesGrid =
[
	<s:property value="imap1.gridbuttons" />
];
_4_botonesGrid.push('->',
{
    xtype       : 'textfield'
    ,fieldLabel : '<span style="color:white;">Filtro:</span>'
    ,labelWidth : 60
    ,espera     : ''
    ,listeners  :
    {
        change : function(me,val)
        {
            clearTimeout(me.espera);
            me.espera=setTimeout(function()
            {
                loadMcdinStoreFiltro = val;
                Ext.ComponentQuery.query('[xtype=button][text=Buscar]')[0].handler();
            },1500);
        }
    }
});
/*///////////////////*/
////// variables //////
///////////////////////

<s:if test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("1")}'>
    <jsp:include page="/jsp-script/proceso/endosos/scriptMesaEmision.jsp" />
</s:if>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("14")}'>
    <jsp:include page="/jsp-script/proceso/endosos/scriptMesaAutorizacionServicios.jsp" />
</s:elseif>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("15")}'>
    <jsp:include page="/jsp-script/proceso/endosos/scriptMesaAutorizacionEndosos.jsp" />
</s:elseif>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("16")}'>
	<jsp:include page="/jsp-script/proceso/siniestros/scriptMesaSiniestros.jsp" />
</s:elseif>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("17")}'>
    <jsp:include page="/jsp-script/proceso/emision/scriptMesaAutorizacionEmisiones.jsp" />
</s:elseif>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("19")}'>
    <jsp:include page="/jsp-script/proceso/siniestros/scriptMesaSISCO.jsp" />
</s:elseif>
<s:elseif test='%{getSmap2().get("pv_cdtiptra_i").equalsIgnoreCase("20")}'>
    <jsp:include page="/jsp-script/proceso/emision/scriptMesaImpresion.jsp" />
</s:elseif>
_4_botones.width = (_4_botones.items.length*20)+20;

///////////////////////
////// funciones //////
/*///////////////////*/
function _4_imagenStatus(record,imagen,texto,estados,funcion,row)
{
	debug('record:'  , record);
	debug('imagen:'  , imagen);
	debug('texto:'   , texto);
	debug('estados:' , estados);
	debug('funcion:' , funcion);
	debug('row:'     , row);
	var indice   = $.inArray(Number(record.get('status')),estados);
	debug('indice: ',indice);
	if(indice>=0)
	{
	    return '<a href="#" onclick="'+funcion+'('+row+'); return false;"><img src="${ctx}/resources/fam3icons/icons/'+imagen+'.png" data-qtip="'+texto+'" /></a>';
	}
	else
	{
		return 'a';
	}
}

function _4_cambiarTiptra(cdtiptra)
{
	var editable = '';
	var titulo   = '';
	
	if(cdtiptra == TipoTramite.PolizaNueva)
	{
		editable = 'si';
		titulo   = 'Tareas';
	}
	else if(cdtiptra == TipoTramite.AutorizacionServicios)
    {
        editable = '';
        titulo   = 'Autorizaciones de servicios';
    }
	else if(cdtiptra == TipoTramite.EndosoParadoPorAutorizacion)
    {
        editable = '';
        titulo   = 'Endosos en espera';
    }
	else if(cdtiptra == TipoTramite.Siniestro)
    {
        editable = '';
        titulo   = 'Reclamaciones en proceso';
    }
	else if(cdtiptra == TipoTramite.EmisionEnEspera)
    {
        editable = '';
        titulo   = 'Emisiones en espera';
    }
	
	Ext.create('Ext.form.Panel').submit(
	{
		url     : _4_urlReload
		,params :
		{
			'smap1.cdramo'         : _4_smap1.cdramo
			,'smap1.cdtipsit'      : _4_smap1.cdtipsit
			,'smap1.gridTitle'     : titulo
			,'smap2.pv_cdtiptra_i' : cdtiptra
			,'smap1.editable'      : editable
		}
	    ,standardSubmit : true
	});
}

function _mcdinamica_nuevoTramite(button,turnar)
{
	debug('>_mcdinamica_nuevoTramite');
    var form=button.up().up();
    debug(form.getForm().getValues());
    if(form.isValid())
    {
        form.setLoading(true);
        form.submit(
        {
            success  : function(form2, action)
            {
                form.setLoading(false);
                var ntramite = action.result.msgResult;
                var callback = function()
                {
                    centrarVentanaInterna(Ext.Msg.show(
                    {
                        title    : 'Cambios guardados'
                        ,msg     : 'Se agreg&oacute; un nuevo tr&aacute;mite</br> N&uacute;mero: '+ntramite
                        ,buttons : Ext.Msg.OK
                        ,fn      : function()
                        {
                            button.up('window').hide();
                            button.up('form').getForm().reset();
                            loadMcdinStore();
                        }
                    }));
                };
                if(turnar)
                {
                    form.setLoading(true);
                    Ext.Ajax.request(
                    {
                        url     : mesConUrlUpdateStatus
                        ,params :
                        {
                            'smap1.status'    : 13
                            ,'smap1.ntramite' : ntramite
                            ,'smap1.comments' : 'Trámite nuevo turnado a suscripción'
                        }
                        ,success : function(response)
                        {
                            form.setLoading(false);
                            var json=Ext.decode(response.responseText);
                            debug('json response:',json);
                            if(json.success)
                            {
                                callback();
                            }
                            else
                            {
                                mensajeError(json.mensaje);
                            }
                        }
                        ,failure : function()
                        {
                            form.setLoading(false);
                            errorComunicacion();
                        }
                    });
                }
                else
                {
                    callback();
                }
            }
            ,failure : function(form2, action){
                
                debug('action',action.result);
                form.setLoading(false);
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,msg     :  (!Ext.isEmpty(action.result.errorMessage)) ? action.result.errorMessage :'Error de comunicaci&oacute;n'
                    ,buttons : Ext.Msg.OK
                    ,icon    : Ext.Msg.ERROR
                });
            }
        });
    }
    else
    {
        mensajeWarning('Favor de introducir los campos requeridos');
    }
    debug('>_mcdinamica_nuevoTramite');
}

function _mcdinamica_rendererStatus(val)
{
    var l = 'Cargando...';
    try
    {
        var statusCmp = _fieldByName('smap2.pv_status_i');
        if(statusCmp.store.getCount()>0)
        {
            var rec = statusCmp.findRecordByValue(''+val);
            if(rec)
            {
                l = rec.get('value');
            }
            else
            {
                l = 'No encontrado ('+val+')';
            }
        }
        else
        {            
            statusCmp.store.on(
            {
                load : function(me,records)
                {
                    if(Ext.isEmpty(me.cargado))
                    {
                        mcdinGrid.getView().refresh();
                        me.cargado = true;
                    }
                }
            });
        }
    }
    catch(e)
    {
        debugError('Error al renderizar el status del tramite, val:',val,e);
    }
    return l;
}

function _4_onReasignarClick(row)
{
    debug('>_4_onReasignarClick row:',row);
    var record = mcdinStore.getAt(row);
    debug('record:',record);
    mcdinGrid.setLoading(true);
    Ext.Ajax.request(
    {
        url : _4_urlRecuperacionSimpleLista
        ,params :
        {
            'smap1.procedimiento' : 'RECUPERAR_USUARIOS_REASIGNACION_TRAMITE'
            ,'smap1.ntramite'     : record.get('ntramite')
        }
        ,success : function(response)
        {
            mcdinGrid.setLoading(false);
            var ck = 'Recuperando usuarios para reasignaci&oacute;n';
            try
            {
                var json = Ext.decode(response.responseText);
                debug('### usuarios:',json);
                if(json.exito)
                {
                    centrarVentanaInterna(Ext.create('Ext.window.Window',
                    {
                         modal      : true
                        ,title      : 'Reasignar tr&aacute;mite'
                        ,items      :
                        [
                            Ext.create('Ext.grid.Panel',
                            {
                                width    : 600
                                ,height  : 300
                                ,columns :
                                [
                                    {
                                        xtype    : 'actioncolumn'
                                        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                        ,tooltip : 'Aceptar'
                                        ,width   : 30
                                        ,handler : function(v,row,col,item,e,rec)
                                        {
                                            var window1  = v.up('window');
                                            var cdusuari = rec.get('CDUSUARI');
                                            var cdsisrol = rec.get('CDSISROL');
                                            var status   = record.get('status');
                                            debug('cdusuari:',cdusuari,'cdsisrol:',cdsisrol);
                                            debug('status:',status,'window1:',window1);
                                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                                            {
                                                title        : 'Comentarios'
                                                ,buttonAlign : 'center'
                                                ,modal       : true
                                                ,buttons     :
                                                [
                                                    {
                                                        text     : 'Reasignar'
                                                        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                                        ,handler : function(me)
                                                        {
                                                            var window = me.up('window');
                                                            var text   = window.down('textarea');
                                                            debug('window:',window,'text:',text);
                                                            if(!text.isValid())
                                                            {
                                                                datosIncompletos();
                                                            }
                                                            else
                                                            {
                                                                window.setLoading(true);
                                                                Ext.Ajax.request(
                                                                {
                                                                    url     : _4_urlActualizarStatusTramite
                                                                    ,params :
                                                                    {
                                                                        'smap1.ntramite'         : record.get('ntramite')
                                                                        ,'smap1.status'          : status
                                                                        ,'smap1.rol_destino'     : cdsisrol
                                                                        ,'smap1.usuario_destino' : cdusuari
                                                                        ,'smap1.comments'        : text.getValue()
                                                                        ,'smap1.swagente'        : _fieldById('SWAGENTE').getGroupValue()
                                                                    }
                                                                    ,success : function(response)
                                                                    {
                                                                        window.setLoading(false);
                                                                        var ck = 'Reasignando tr&aacute;mite';
                                                                        try
                                                                        {
                                                                            var json2 = Ext.decode(response.responseText);
                                                                            debug('### reasignar:',json2);
                                                                            if(json2.success)
                                                                            {
                                                                                window.close();
                                                                                window1.close();
                                                                                Ext.ComponentQuery.query('[xtype=button][text=Buscar]')[0].handler();
                                                                                mensajeCorrecto('Tr&aacute;mite reasignado'
                                                                                    ,'El tr&aacute;mite '+record.get('ntramite')
                                                                                        +' ha sido asignado a '+json2.smap1.nombreUsuarioDestino
                                                                                );
                                                                            }
                                                                            else
                                                                            {
                                                                                mensajeError('Error al reasignar tr&aacute;mite');
                                                                            }
                                                                        }
                                                                        catch(e)
                                                                        {
                                                                            manejaException(e,ck);
                                                                        }
                                                                    }
                                                                    ,failure : function()
                                                                    {
                                                                        window.setLoading(false);
                                                                        errorComunicacion(null,'Error al reasignar tr&aacute;mite');
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                ]
                                                ,items :
                                                [
                                                    {
                                                        xtype       : 'textarea'
                                                        ,width      : 400
                                                        ,height     : 200
                                                        ,allowBlank : false
                                                    }
                                                    ,_4_swagenteRadioGroup()
                                                ]
                                            }).show());
                                        }
                                    }
                                    ,{
                                        text       : 'Clave'
                                        ,dataIndex : 'CDUSUARI'
                                        ,width     : 120
                                    }
                                    ,{
                                        text       : 'Nombre'
                                        ,dataIndex : 'NOMBRE'
                                        ,flex      : 1
                                    }
                                    ,{
                                        text       : 'Num. tr&aacute;mites asignados'
                                        ,dataIndex : 'TOTAL'
                                        ,width     : 160
                                    }
                                ]
                                ,store : Ext.create('Ext.data.Store',
                                {
                                    fields : [ 'CDUSUARI' , 'NOMBRE' , 'CDSISROL' , 'TOTAL']
                                    ,data  : json.slist1
                                })
                            })
                        ]
                    }).show());
                }
                else
                {
                    mensajeError(json.respuesta);
                }
                debug('<_4_onReasignarClick');
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
        ,failure : function()
        {
            mcdinGrid.setLoading(false);
            errorComunicacion(null,'Error recuperando usuarios para reasignaci&oacute;n');
        }
    });
}

function _4_swagenteRadioGroup()
{
    return {
                xtype       : 'radiogroup'
                ,fieldLabel : 'Mostrar al agente'
                ,columns    : 2
                ,width      : 250
                ,style      : 'margin:5px;'
                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                ,items      :
                [
                    {
                        boxLabel    : 'Si'
                        ,itemId     : 'SWAGENTE'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'S'
                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                    }
                    ,{
                        boxLabel    : 'No'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'N'
                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                    }
                ]
            };
}
/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function()
{
    if(Number(mcdinInput['tiptra'])     == 1
        || Number(mcdinInput['tiptra']) == 15
        || Number(mcdinInput['tiptra']) == 17
    )
    {
        alert('Existe una versi\u00f3n nueva de la mesa de control');
    }

    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*60;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('McdinModelo',
    {
    	extend  : 'Ext.data.Model'
    	,fields :
    	[
    	    {
    	    	name  : 'activo'
    	    	,type : 'boolean'
    	    }
    	    ,<s:property value="imap1.modelFields" />
    	]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    mcdinStore = Ext.create('Ext.data.Store',
    {
        pageSize : 10,
        autoLoad : true,
        model    : 'McdinModelo',
        //sorters:[{sorterFn:function(o1,o2){return o1.get('ntramite')<o2.get('ntramite')}}],
        proxy    :
        {
            enablePaging : true,
            reader       : 'json',
            type         : 'memory',
            data         : []
        }
        ,listeners :
        {
            load : function (action,records)
            {
                debug("Records Cargados en mcdinStore: ",records);
            }
        }
    });
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    Ext.define('McdinGrid',
    {
    	extend : 'Ext.grid.Panel'
    	,initComponent : function()
    	{
    		debug('initComponent instance of McdinGrid');
    		var columnas =
    	    [
    	        <s:property value="imap1.gridColumns" />
    	    ];
    		if(_4_statusColumns.length>0)
    		{
    			for(var i=0;i<_4_statusColumns.length;i++)
    			{
    				var col=_4_statusColumns[i];
    				col.flex         = 0;
    				col.width        = 20;
    				col.xtype        = 'actioncolumn';
    				col.text         = '';
    				col.sortable     = false;
    				col.menuDisabled = true;
    				columnas.push(col);
    			}
    		}
    		columnas.push(_4_botones);
    		Ext.apply(this,
    		{
    			title      : '<s:property value="smap1.gridTitle" />'
    			,store     : mcdinStore
    			,height    : 420
    			,selType   : 'checkboxmodel'
    			,columns   : columnas
    			,tbar      : _4_botonesGrid
    			,bbar      :
    	        {
    	            displayInfo : true
    	            ,store      : mcdinStore
    	            ,xtype      : 'pagingtoolbar'
    	        }
    		});
    		this.callParent();
    	}
    });
    
    Ext.define('McdinFiltro',
    {
    	extend         : 'Ext.form.Panel'
    	,initComponent : function()
    	{
    		debug('initComponent instance of McdinFiltro');
    		Ext.apply(this,
    		{
		    	title          : 'Filtro'
		    	,icon          : '${ctx}/resources/fam3icons/icons/zoom.png'
		    	,buttonAlign   : 'center'
		    	,collapsible   : true
		    	,url           : _4_urlReload
		    	,minHeight     : 200
		    	,titleCollapse : true
		    	,layout        :
		    	{
		    		type     : 'table'
		    		,columns : 3
		    	}
		    	,defaults      :
		    	{
		    		style : 'margin : 5px;'
		    	}
		    	,items         :
		    	[
		    	    <s:property value="imap1.itemsFiltro" />
		    	]
		    	,tbar          :
		    	[
		    	    <s:property value="imap1.botonesTramite" />
		    	]
		    	,buttons       :
		    	[
					{
					    text     : 'Reporte'
					    ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
					    ,hidden  : mcdinInput['tiptra']!='1'
					    ,handler : function()
					    {
					        Ext.MessageBox.confirm('Confirmar', '¿Generar reporte con la sucursal y producto seleccionados?', function(btn)
					        {
					            if(btn === 'yes')
					            {   
					                Ext.create('Ext.form.Panel').submit(
					                {
					                    standardSubmit : true,
					                    url:_4_urlReporte,
					                    params:
					                    {
					                        cdreporte : 'REPEXC007'
					                        ,'params.pv_cdunieco_i' : mcdinFiltro.items.items[0].getValue()
					                        ,'params.pv_cdramo_i'   : mcdinFiltro.items.items[1].getValue()
					                    },
					                    success: function(form, action)
					                    {
					                        
					                    },
					                    failure: function(form, action)
					                    {
					                        switch (action.failureType)
					                        {
					                            case Ext.form.action.Action.CONNECT_FAILURE:
					                                Ext.Msg.alert('Error', 'Error de comunicaci&oacute;n');
					                                break;
					                            case Ext.form.action.Action.SERVER_INVALID:
					                            case Ext.form.action.Action.LOAD_FAILURE:
					                                Ext.Msg.alert('Error', 'Error del servidor, consulte a soporte');
					                                break;
					                       }
					                    }
					                });
					            }
					        });
					    }
					}
					,{
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                        ,handler : function()
                        {
                            this.up().up().getForm().reset();
                        }
                    }
		    	    ,{
		    	    	text     : 'Buscar'
		    	    	,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
		    	    	,handler : function()
		    	    	{
		    	    		if(this.up().up().isValid())
		    	    		{
		    	    			var params=
		    	    			{
		    	    				'smap1.pv_cdunieco_i'      : _fieldByName('smap2.pv_cdunieco_i').getValue()
		    	    			    ,'smap1.pv_cdramo_i'       : _fieldByName('smap2.pv_cdramo_i').getValue()
		    	                    ,'smap1.pv_cdtipsit_i'     : _fieldByName('smap2.pv_cdtipsit_i').getValue()
		    	                    ,'smap1.pv_estado_i'       : _fieldByName('smap2.pv_estado_i').getValue()
		    	    			    ,'smap1.pv_nmpoliza_i'     : _fieldByName('smap2.pv_nmpoliza_i').getValue()
		    	                    ,'smap1.pv_cdagente_i'     : _fieldByName('smap2.pv_cdagente_i').getValue()
		    	    			    ,'smap1.pv_ntramite_i'     : _fieldByName('smap2.pv_ntramite_i').getValue()
		    	                    ,'smap1.pv_status_i'       : _fieldByName('smap2.pv_status_i').getValue()
		    	                    ,'smap1.pv_fedesde_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fedesde_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fedesde_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_fehasta_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fehasta_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fehasta_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_cdtiptra_i'     : mcdinInput['tiptra']
		    	    				,'smap1.pv_contrarecibo_i' : Ext.isEmpty(_fieldByName('smap2.pv_contrarecibo_i').getValue())?'':_fieldByName('smap2.pv_contrarecibo_i').getValue()
		    	    				,'smap1.pv_tipoPago_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_tipoPago_i').getValue())?'':_fieldByName('smap2.pv_tipoPago_i').getValue()
		    	    				,'smap1.pv_nfactura_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_nfactura_i').getValue())?'':_fieldByName('smap2.pv_nfactura_i').getValue()
		    	    				,'smap1.pv_cdpresta_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_cdpresta_i').getValue())?'':_fieldByName('smap2.pv_cdpresta_i').getValue()
		    	    				,'smap1.filtro'            : loadMcdinStoreFiltro
		    	    				,'smap1.lote'              : _getValueByName('smap1.lote'          , false)
		    	    				,'smap1.tipolote'          : _getValueByName('smap1.tipolote'      , false)
		    	    				,'smap1.tipoimpr'          : _getValueByName('smap1.tipoimpr'      , false)
		    	    				,'smap1.cdusuari_busq'     : _getValueByName('smap1.cdusuari_busq' , false)
		    	    				,'smap1.dscontra'          : _fieldByName('smap2.pv_dscontra_i').getValue()
		    	    			};
		    	    			cargaStorePaginadoLocal(mcdinStore, mcdinUrlCargar, 'olist1', params, function (options, success, response){
		    	    			    loadMcdinStoreFiltro = '';
		    	    	            if(success){
		    	    	                var jsonResponse = Ext.decode(response.responseText);
		    	    	                
		    	    	                if(!jsonResponse.success) {
		    	    	                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
		    	    	                }
		    	    	            }else{
		    	    	                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
		    	    	            }
		    	    	        }, mcdinGrid);
		    	    		}
		    	    		else
		    	    		{
		    	    			mensajeWarning('Favor de introducir los campos requeridos');
		    	    		}
		    	    	}
		    	    }
		    	]
    		});
    		if(this.items[0])
    		{
    			this.items[0].forceSelection=false;
    		}
    		this.callParent();
    	}
    });
    
    Ext.define('McdinFormNuevo',
    {
    	extend       : 'Ext.window.Window'
        ,title       : 'Agregar tr&aacute;mite'
        ,icon        : '${ctx}/resources/fam3icons/icons/add.png'
        ,width       : 600
        ,modal       : true
        ,height      : 400
        ,closeAction : 'hide'
        ,items       :
        [
            Ext.create('Ext.form.Panel',
            {
                buttonAlign : 'center'
                ,border     : 0
                ,url        : mcdinUrlNuevo
                ,layout     :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,defaults   :
                {
                    style : 'margin : 5px;'
                }
                ,items      :
                [
                    <s:property value="imap1.formItems" />
                ]
                ,buttons    :
                [
                    {
                        text     : 'Guardar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,handler : function(me){_mcdinamica_nuevoTramite(me,false);}
                    }
                    ,{
                        text     : 'Turnar a suscripci&oacute;n'
                        ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                        ,handler : function(me){_mcdinamica_nuevoTramite(me,true);}
                        ,hidden  : true
                    }
                ]
            })
        ]
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    mcdinGrid      = new McdinGrid();
    mcdinFiltro    = new McdinFiltro();
    mcdinFormNuevo = new McdinFormNuevo();
    if(mcdinFormNuevo
    		&&mcdinFormNuevo.items
    		&&mcdinFormNuevo.items.items[0]
            &&mcdinFormNuevo.items.items[0].items
            &&mcdinFormNuevo.items.items[0].items.items[8])
    {
    	var comboTmp =mcdinFormNuevo.items.items[0].items.items[8];
    	debug('combo tooltip',comboTmp);
    	comboTmp.addListener('afterrender',function()
    	{
		    Ext.create('Ext.tip.ToolTip',
		    {
		        target : comboTmp.getEl()
		        ,html  : 'Usar transferencia cuando es cambio de producto'
		    });    		
    	});
    }
    
    Ext.create('Ext.panel.Panel',
    {
    	border    : 0
    	,renderTo : 'mcdinDivPri'
    	,defaults :
    	{
    		style : 'margin : 5px;'
    	}
    	,items    :
    	[
    	    mcdinFiltro,
    	    mcdinGrid
    	]
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    loadMcdinStore = function (){
    	var params = {
	            'smap1.pv_cdunieco_i'     : mcdinInput['cdunieco']
		        ,'smap1.pv_ntramite_i'    : mcdinInput['ntramite']
		        ,'smap1.pv_cdramo_i'      : mcdinInput['cdramo']
		        ,'smap1.pv_nmpoliza_i'    : mcdinInput['nmpoliza']
		        ,'smap1.pv_estado_i'      : mcdinInput['estado']
		        ,'smap1.pv_cdagente_i'    : mcdinInput['cdagente']
		        ,'smap1.pv_status_i'      : mcdinInput['status']
		        ,'smap1.pv_cdtipsit_i'    : mcdinInput['cdtipsit']
		        ,'smap1.pv_fedesde_i'     : mcdinInput['fedesde']
		        ,'smap1.pv_fehasta_i'     : mcdinInput['fehasta']
		        ,'smap1.pv_cdtiptra_i'    : mcdinInput['tiptra']
    			,'smap1.pv_contrarecibo_i': mcdinInput['contrarecibo']
    			,'smap1.pv_tipoPago_i'	  : mcdinInput['tipoPago']
		    	,'smap1.pv_nfactura_i'	  : mcdinInput['nfactura']
		    	,'smap1.pv_cdpresta_i'	  : mcdinInput['cdpresta']
		    	,'smap1.cdtipram'         : mcdinInput['cdtipram']
		    	,'smap1.lote'             : mcdinInput['lote']
                ,'smap1.tipolote'         : mcdinInput['tipolote']
                ,'smap1.tipoimpr'         : mcdinInput['tipoimpr']
                ,'smap1.cdusuari_busq'    : mcdinInput['cdusuari_busq']
    			,'smap1.dscontra'         : mcdinInput['dscontra']
		   };
    	
    	cargaStorePaginadoLocal(mcdinStore, mcdinUrlCargar, 'olist1', params, function (options, success, response){
    		if(success){
                var jsonResponse = Ext.decode(response.responseText);
                
                if(!jsonResponse.success) {
                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
                }
            }else{
                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
            }
    	}, mcdinGrid);
    	
    }
    
    loadMcdinStore();
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
});
</script>
</head>
<body>
<div id="mcdinDivPri" style="height:1000px;"></div>
</body>
</html>