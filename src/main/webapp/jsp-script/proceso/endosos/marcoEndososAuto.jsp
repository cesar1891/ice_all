<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- Paging Persistence library -->
<script type="text/javascript" src="${ctx}/resources/extjs4/plugins/pagingpersistence/pagingselectionpersistence2.js?${now}"></script>

<script>
////// urls //////
var _p34_urlRecuperacionSimple            = '<s:url namespace="/emision"      action="recuperacionSimple"           />';
var _p34_urlRecuperacionSimpleLista       = '<s:url namespace="/emision"      action="recuperacionSimpleLista"      />';
var _p34_urlVentanaDocumentos             = '<s:url namespace="/documentos"   action="ventanaDocumentosPoliza"      />';
var _p34_urlObtenerColumnasIncisosPorRamo = '<s:url namespace="/endosos"      action="recuperarColumnasIncisoRamo"  />';
var _p34_urlRecuperarEndososClasificados  = '<s:url namespace="/endosos"      action="recuperarEndososClasificados" />';
var _p34_urlObtieneCatalogos              = '<s:url namespace="/catalogos"    action="obtieneCatalogo"              />';
var _p34_urlRecuperacion                  = '<s:url namespace="/recuperacion" action="recuperar"                    />';

var _p34_urlActualizarOtvalorTramiteXDsatribu = '<s:url namespace="/emision" action="actualizarOtvalorTramitePorDsatribu" />';
var _p34_urlRecuperarOtvalorTramiteXDsatribu  = '<s:url namespace="/emision" action="recuperarOtvalorTramitePorDsatribu"  />';
////// urls //////

////// variables //////
var _p34_contexto = '${ctx}';
var _p34_smap1    = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p34_smap1:',_p34_smap1);

var _p34_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p34_flujo:',_p34_flujo);

var _p34_flujoAux = {};

if(!Ext.isEmpty(_p34_flujo)
    &&!Ext.isEmpty(_p34_flujo.aux)
)
{
    try
    {
        _p34_flujoAux = Ext.decode(_p34_flujo.aux);
    }
    catch(e)
    {
        manejaException(e);
    }
}

debug('_p34_flujoAux:',_p34_flujoAux,'.');

var _p34_storePolizas;
var _p34_storeGrupos;
var _p34_storeFamilias;
var _p34_storeIncisos;
var _p34_storeEndosos;

var _p34_soloNivelPoliza = false;
var _p34_soloNivelInciso = false;
////// variables //////

////// overrides //////
////// overrides //////

////// dinamicos //////
var _p34_formBusqItems       = [<s:property value="imap.formBusqItems"        escapeHtml="false" />];
var _p34_gridPolizasColumns  = [<s:property value="imap.gridPolizasColumns"   escapeHtml="false" />];
var _p34_historicoColumns    = [<s:property value="imap.gridHistoricoColumns" escapeHtml="false" />];
var _p34_gridGruposColumns   = [<s:property value="imap.gridGruposColumns"    escapeHtml="false" />];
var _p34_gridFamiliasColumns = [<s:property value="imap.gridFamiliasColumns"  escapeHtml="false" />];

_p34_gridPolizasColumns.push
(
    {
        xtype         : 'actioncolumn'
        ,sortable     : false
        ,menuDisabled : true
        ,width        : 80
        ,items        :
        [
            {
                icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
                ,tooltip : 'Ver incisos'
                ,handler : function(view,row,col,item,e,record){ _p34_gridPolizasIncisosClic(record,view.up('grid')); }
            }
            ,{
                icon     : '${ctx}/resources/fam3icons/icons/clock.png'
                ,tooltip : 'Ver historial'
                ,handler : function(view,row,col,item,e,record){ _p34_gridPolizasHistorialClic(record); }
            }
            ,{
                icon     : '${ctx}/resources/fam3icons/icons/printer.png'
                ,tooltip : 'Ver documentaci&oacute;n'
                ,handler : function(view,row,col,item,e,record){ _p34_gridPolizasDocumentosClic(record); }
            }
        ]
    }
);

_p34_gridFamiliasColumns.push
(
    {
        xtype         : 'actioncolumn'
        ,sortable     : false
        ,menuDisabled : true
        ,width        : 30
        ,items        :
        [
            {
                icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
                ,tooltip : 'Ver incisos'
                ,handler : function(view,row,col,item,e,record){ _p34_gridFamiliasIncisosClic(record,view.up('window')); }
            }
        ]
    }
);

_p34_gridGruposColumns.push
(
    {
        xtype         : 'actioncolumn'
        ,sortable     : false
        ,menuDisabled : true
        ,width        : 30
        ,items        :
        [
            {
                icon     : '${ctx}/resources/fam3icons/icons/text_list_numbers.png'
                ,tooltip : 'Ver incisos'
                ,handler : function(view,row,col,item,e,record){ _p34_gridGruposIncisosClic(record,view.up('window')); }
            }
        ]
    }
);
////// dinamicos //////

Ext.onReady(function()
{
	// Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    Ext.tip.QuickTipManager.init();
    
    ////// modelos //////
    Ext.define('_p34_modeloPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            //MPOLIZAS
             'CDUNIECO'  , 'CDRAMO'   , 'ESTADO'   , 'NMPOLIZA'
            ,'NMSUPLEM'  , 'STATUS'   , 'SWESTADO' , 'NMSOLICI'
            ,'CDMOTANU'  , 'SWAUTORI' , 'CDMONEDA' , 'OTTEMPOT'
            ,'HHEFECTO'  , 'NMRENOVA' , 'NMNUMSIN' , 'CDTIPCOA'
            ,'SWTARIFI'  , 'SWABRIDO' , 'CDPERPAG' , 'NMPOLIEX'
            ,'NMCUADRO'  , 'PORREDAU' , 'SWCONSOL' , 'NMPOLANT'
            ,'NMPOLNVA'  , 'CDRAMANT' , 'CDMEJRED' , 'NMPOLDOC'
            ,'NMPOLIZA2' , 'NMRENOVE' , 'NMSUPLEE' , 'TTIPCAMC'
            ,'TTIPCAMV'  , 'SWPATENT' , 'NMPOLMST' , 'PCPGOCTE'
            ,'TIPOFLOT'
            ,{ name        : 'FEANULAC' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEAUTORI' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEINISUS' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEFINSUS' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEEFECTO' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEPROREN' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEVENCIM' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FERECIBO' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEULTSIN' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEEMISIO' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FESOLICI' , type       : 'date' , dateFormat : 'd/m/Y' }
            //MPERSONA
            ,'CDPERSON'    , 'CDTIPIDE'  , 'CDIDEPER'   , 'DSNOMBRE'
            ,'CDTIPPER'    , 'OTFISJUR'  , 'OTSEXO'     , 'CDRFC'
            ,'FOTO'        , 'DSEMAIL'   , 'DSNOMBRE1'  , 'DSAPELLIDO'
            ,'DSAPELLIDO1' , 'CDNACION'  , 'DSCOMNOM'   , 'DSRAZSOC'
            ,'DSNOMUSU'    , 'CDESTCIV'  , 'CDGRUECO'   , 'CDSTIPPE'
            ,'NMNUMNOM'    , 'CURP'      , 'CANALING'   , 'CONDUCTO'
            ,'PTCUMUPR'    , 'STATUSPER' , 'RESIDENCIA' , 'NONGRATA'
            ,'CDIDEEXT'    , 'CDSUCEMI'
            ,{ name        : 'FENACIMI'  , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEINGRESO' , type       : 'date' , dateFormat : 'd/m/Y' }
            ,{ name        : 'FEACTUAL'  , type       : 'date' , dateFormat : 'd/m/Y' }
            //TVALOPOL
            ,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05"
            ,"OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
            ,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15"
            ,"OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
            ,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25"
            ,"OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
            ,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35"
            ,"OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
            ,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45"
            ,"OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
            ,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05"
            ,"DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
            ,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15"
            ,"DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
            ,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25"
            ,"DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
            ,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35"
            ,"DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
            ,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45"
            ,"DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
            //OTROS
            ,"RAMO"
            //CUSTOM
            ,'NOMBRECOMPLETO'
            ,'NTRAMITE'
            ,'NMSITAUX','CDTIPSIT'
        ]
    });
    
    Ext.define('_p34_modeloHistorico',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'NSUPLOGI'
            ,'DSTIPSUP'
            ,{ name : 'FEEMISIO' , type : 'date' , dateFormat : 'd/m/Y' }
            ,{ name : 'FEINIVAL' , type : 'date' , dateFormat : 'd/m/Y' }
            ,{ name : 'FEFINVAL' , type : 'date' , dateFormat : 'd/m/Y' }
        ]
    });
    
    Ext.define('_p34_modeloInciso',
    {
        extend     : 'Ext.data.Model'
        ,idProperty: 'NMSITUAC'
        ,fields    : []
    });
    
    Ext.define('_p34_modeloEndoso',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ 'CDTIPSUP' , 'DSTIPSUP' , 'LIGA', 'TIPO_VALIDACION' ]
    });
    
    Ext.define('_p34_modeloGrupo',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ 'CDGRUPO' , 'DSGRUPO' ]
    });
    
    Ext.define('_p34_modeloFamilia',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ 'NMSITAUX' , 'TITULAR' ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p34_storePolizas=Ext.create('Ext.data.Store',
    {
        model     : '_p34_modeloPoliza'
        ,autoLoad : false
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p34_urlRecuperacionSimpleLista
            ,extraParams :
            {
                'smap1.procedimiento' : 'RECUPERAR_POLIZAS_ENDOSABLES'
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,successProperty : 'success'
                ,messageProperty : 'respuesta'
            }
        }
    });
    
    _p34_storeGrupos=Ext.create('Ext.data.Store',
    {
        model     : '_p34_modeloGrupo'
        ,autoLoad : false
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p34_urlRecuperacionSimpleLista
            ,extraParams :
            {
                'smap1.procedimiento' : 'RECUPERAR_GRUPOS_POLIZA'
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,successProperty : 'success'
                ,messageProperty : 'respuesta'
            }
        }
    });
    
    _p34_storeFamilias=Ext.create('Ext.data.Store',
    {
        model     : '_p34_modeloFamilia'
        ,autoLoad : false
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p34_urlRecuperacionSimpleLista
            ,extraParams :
            {
                'smap1.procedimiento' : 'RECUPERAR_FAMILIAS_POLIZA'
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,successProperty : 'success'
                ,messageProperty : 'respuesta'
            }
        }
    });
    
    _p34_storeIncisos=Ext.create('Ext.data.Store',
    {
        model     : '_p34_modeloInciso'
        ,autoLoad : false
        ,pageSize : 25
        ,proxy    :
        {
            type         : 'ajax' 
            /* type         : 'jsonp' */
            ,url         : _p34_urlRecuperacionSimpleLista
            ,callbackKey : 'callback'
            ,extraParams :
            {
                'smap1.procedimiento' : 'RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA'
            }
            ,reader      :
            {
                type             : 'json'
                ,root            : 'slist1'
                ,successProperty : 'success'
                ,messageProperty : 'respuesta'
                ,totalProperty   : 'total'
            }
            ,call: function()
            {
               debug('call');
            }
            ,simpleSortMode: true
        }
        ,listeners : {
			beforeload:function(){
				try{
					_fieldById('_p34_gridIncisos',null,true).setLoading(true);
				}catch(e){
					Ext.ComponentQuery.query('#_p34_gridIncisos').forEach(function(it){ it.setLoading(false)})
					debugError(e);
				}
			},        
		  	load: function(me, records, success){
			 	debug('### incisos load',records,success);
	            if(success)
	            {
	                for(var i in records)
	                {
	                    var record    = records[i];
	                    var atributos = record.get('CDTIPSIT');
	                    for(var i=1;i<=99;i++)
	                    {
	                        var valor=record.get('OTVALOR'+(('x00'+i).slice(-2)));
	                        if(valor+'x'=='nullx')
	                        {
	                            valor='';
	                        }
	                        var display=record.get('DSVALOR'+(('x00'+i).slice(-2)));
	                        if(display+'x'=='nullx')
	                        {
	                            display='';
	                        }
	                        atributos=atributos+'|'+valor+'~'+display;
	                    }
	                    record.set('ATRIBUTOS',atributos);
	                    
	                    record.set('NOMBRECOMPLETO',
	                        (Ext.isEmpty(record.get('DSNOMBRE'))   ?'':record.get('DSNOMBRE'))   + ' ' +
	                        (Ext.isEmpty(record.get('DSNOMBRE1'))  ?'':record.get('DSNOMBRE1'))  + ' ' +
	                        (Ext.isEmpty(record.get('DSAPELLIDO')) ?'':record.get('DSAPELLIDO')) + ' ' +
	                        (Ext.isEmpty(record.get('DSAPELLIDO1'))?'':record.get('DSAPELLIDO1'))
	                    );
	                    
	                    debug('record customizado:',record.data);
	                }
	                
	                _p34_storeIncisos.commitChanges();
	            } 
	            try{
					_fieldById('_p34_gridIncisos',null,true).setLoading(false);
				}catch(e){
					Ext.ComponentQuery.query('#_p34_gridIncisos').forEach(function(it){ it.setLoading(false)})
					debugError(e);
				}
		    }		        
	     }
    });
    
    _p34_storeEndosos=Ext.create('Ext.data.Store',
    {
        model     : '_p34_modeloEndoso'
        ,autoLoad : false
        ,proxy    :
        {
            type  : 'memory'
            ,data : []
        }
        ,listeners :
        {
            add : function(me,records)
            {
                debug('_p34_storeEndosos add arguments:',arguments,'.');
                if(!Ext.isEmpty(_p34_flujoAux.endosoSeleccionado))
                {
                    for(var i=0 ; i<records.length ; i++)
                    {
                        var cdtipsup = Number(records[i].get('CDTIPSUP'));
                        if(cdtipsup !== Number(_p34_flujoAux.endosoSeleccionado))
                        {
                            debug('se quita el record porque no coincide ',cdtipsup,' con ',_p34_flujoAux.endosoSeleccionado,'.');
                            me.remove(records[i]);
                        }
                    }
                }
            }
        }
    });
    ////// stores //////
    
    ////// componentes //////
    _p34_formBusqItems.forEach(function(elemento){
    	elemento.width = 235;
        elemento.labelWidth = 80;
        
    });
    ////// componentes //////
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        defaults  : { style : 'margin:5px;' }
        ,renderTo : '_p34_divpri'
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p34_panelFlujo'
                ,title       : 'ACCIONES'
                ,hidden      : Ext.isEmpty(_p34_flujo)
                ,buttonAlign : 'left'
                ,buttons     : []
                ,listeners   :
                {
                    afterrender : function(me)
                    {
                        if(!Ext.isEmpty(_p34_flujo))
                        {
                            _cargarBotonesEntidad(
                                _p34_flujo.cdtipflu
                                ,_p34_flujo.cdflujomc
                                ,_p34_flujo.tipoent
                                ,_p34_flujo.claveent
                                ,_p34_flujo.webid
                                ,me.itemId//callback
                                ,_p34_flujo.ntramite
                                ,_p34_flujo.status
                                ,_p34_flujo.cdunieco
                                ,_p34_flujo.cdramo
                                ,_p34_flujo.estado
                                ,_p34_flujo.nmpoliza
                                ,_p34_flujo.nmsituac
                                ,_p34_flujo.nmsuplem
                                ,null//callbackDespuesProceso
                            );
                        }
                    }
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p34_formBusq'
                ,title    : 'B&uacute;squeda de p&oacute;lizas'
                ,border   : 0
                ,items    : _p34_formBusqItems
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 4
                }
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Buscar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function(){ _p34_buscarClic(); }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                        ,handler : function(me){ me.up('form').getForm().reset(); }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId     : '_p34_gridPolizas'
                ,columns   : _p34_gridPolizasColumns
                ,maxHeight : 400
                ,selModel  :
                {
                    selType        : 'checkboxmodel'
                    ,allowDeselect : true
                    ,mode          : 'SINGLE'
                    ,listeners     :
                    {
                        selectionchange : function(me,selected)
                        {
                            if(_p34_soloNivelInciso === true)
                            {
                                mensajeWarning('Solo se permiten endosos a nivel inciso');
                            }
                            else
                            {
                                _fieldById('_p34_botonEndososPoliza').setDisabled(selected.length==0);
                            }
                        }
                    }
                }
                ,store   : _p34_storePolizas
                ,tbar    :
                [
                    {
                        text      : 'Endosos...'
                        ,itemId   : '_p34_botonEndososPoliza'
                        ,icon     : '${ctx}/resources/fam3icons/icons/book_addresses.png'
                        ,disabled : true
                        ,handler  : function(){ _p34_botonEndososPolizaClic(); }
                    }
                    ,'->'
                    ,{
                        xtype      : 'textfield'
                        ,listeners :
                        {
                            afterrender : function()
                            {
                                _p34_filtrarStore('',_p34_storePolizas);
                            }
                            ,change : function(me,val)
                            {
                                _p34_filtrarStore(val,_p34_storePolizas);
                            }
                        }
                    }
                ]
            })
        ]
    })
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    _p34_recuperarTipoEndosoFlujo();
    _p34_recuperarEndosoAltaAseguradosSaludCol();
    ////// loaders //////
});

////// funciones //////
function _p34_buscarClic()
{
    debug('>_p34_buscarClic');
    var form            = _fieldById('_p34_formBusq');
    var nItemsNoVacios  = 0;
    var nItemsNoValidos = 0;
    var formItems       = Ext.ComponentQuery.query('[fieldLabel][hidden=false]',form);
    for(var i in formItems)
    {
        var item = formItems[i];
        if(!Ext.isEmpty(item.getValue()))
        {
            nItemsNoVacios=nItemsNoVacios+1;
        }
        if(!item.isValid())
        {
            nItemsNoValidos=nItemsNoValidos+1;
        }
    }
    if(nItemsNoVacios==0)
    {
        mensajeWarning('Favor de introducir alg&uacute;n criterio de b&uacute;squeda');
    }
    else if(nItemsNoValidos>0)
    {
        datosIncompletos();
    }
    else
    {
        _p34_polizas();
    }
    debug('<_p34_buscarClic');
}

function _p34_polizas(callback)
{
    debug('>_p34_polizas');
    
    var form   = _fieldById('_p34_formBusq');
    var values = form.getValues();
    var params = {};
    for(var att in values)
    {
        params['smap1.'+att]=values[att];
    }
    debug('valores a enviar:',params);
    
    form.setLoading(true);
    _p34_storePolizas.load(
    {
        params    : params
        ,callback : function(records,operation,success)
        {
            form.setLoading(false);
            debug('### polizas load',records,operation,success);
            if(success&&records.length>0)
            {
                for(var i in records)
                {
                    var record=records[i];
                    record.set('NOMBRECOMPLETO',
                        (Ext.isEmpty(record.get('DSNOMBRE'))   ?'':record.get('DSNOMBRE'))   + ' ' +
                        (Ext.isEmpty(record.get('DSNOMBRE1'))  ?'':record.get('DSNOMBRE1'))  + ' ' +
                        (Ext.isEmpty(record.get('DSAPELLIDO')) ?'':record.get('DSAPELLIDO')) + ' ' +
                        (Ext.isEmpty(record.get('DSAPELLIDO1'))?'':record.get('DSAPELLIDO1'))
                    );
                }
                _p34_storePolizas.commitChanges();
                
                if(!Ext.isEmpty(callback) && typeof callback === 'function')
                {
                    callback();
                }
            }
            else
            {
                if(success)
                {
                    /*centrarVentanaInterna(Ext.create('Ext.window.Window',
                    {
                        title        : 'Aviso'
                        ,width       : 300
                        ,html        : '<div style="padding:5px;text-align:center;">Sin resultados</div>'
                        ,buttonAlign : 'center'
                        ,buttons     :
                        [
                            {
                                text : 'Aceptar'
                                ,handler : function(me)
                                {
                                    me.up('window').destroy();
                                }
                            }
                        ]
                    }).show());*/
                }
                else
                {
                    mensajeError(operation.getError());
                }
            }
        }
    });
    
    debug('<_p34_polizas');
}

function _p34_botonEndososPolizaClic(callback)
{
    debug('>_p34_botonEndososPolizaClic args:',arguments);
    var windowPoliza = _fieldById('_p34_gridPolizas');
    var poliza       = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
    debug('poliza:',poliza.data);
    
    var cdramo   = poliza.get('CDRAMO');
    var nivel    = 'POLIZA';
    var multiple = 'N';
    var tipoflot = 'I';
    var cdtipsit = poliza.get('CDTIPSIT');
    var cdunieco = poliza.get('CDUNIECO');
    var estado   = poliza.get('ESTADO');
    var nmpoliza = poliza.get('NMPOLIZA');
    if(!Ext.isEmpty(poliza.get('TIPOFLOT')))
    {
        tipoflot = poliza.get('TIPOFLOT');
    }
    
    windowPoliza.setLoading(true);
    debug('_p34_storeEndosos:',_p34_storeEndosos);
    Ext.Ajax.request(
    {
        url       : _p34_urlRecuperarEndososClasificados
        ,jsonData :
        {
            smap1 :
            {
                cdramo     : cdramo
                ,nivel     : nivel
                ,multiple  : multiple
                ,tipoflot  : tipoflot
                ,cancelada : !Ext.isEmpty(poliza.get('FEANULAC'))?'S':'N'
                ,cdtipsit  : cdtipsit
                ,cdunieco  : cdunieco
                ,estado	   : estado
                ,nmpoliza  : nmpoliza
            }
        }
        ,success : function(response)
        {
            windowPoliza.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### endosos clasificados polizas:',json);
            if(json.success)
            {
                if(json.slist1.length>0)
                {
                    _p34_storeEndosos.removeAll();
                    for(var i in json.slist1)
                    {
                        _p34_storeEndosos.add(new _p34_modeloEndoso(json.slist1[i]));
                    }
                    _p34_mostrarListaEndosos('POLIZA',json.smap1.stamp);
                }
                else
                {
                    mensajeWarning('No hay endosos disponibles para la selecci&oacute;n');
                }
                
                if (!Ext.isEmpty(callback) && typeof callback === 'function') {
                    callback();
                }
            }
            else
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
            windowPoliza.setLoading(false);
            errorComunicacion();
        }
    });
    
    debug('<_p34_botonEndososPolizaClic');
}

function _p34_gridPolizasDocumentosClic(record)
{
    debug('>_p34_gridPolizasDocumentosClic record:',record.data);
    _p34_mostrarVentanaDocumentos(
    {
        'smap1.cdunieco'       : record.get('CDUNIECO')
        ,'smap1.cdramo'        : record.get('CDRAMO')
        ,'smap1.estado'        : record.get('ESTADO')
        ,'smap1.nmpoliza'      : record.get('NMPOLIZA')
        ,'smap1.nmsuplem'      : '0'
        ,'smap1.nmsolici'      : ''
        ,'smap1.ntramite'      : record.get('NTRAMITE')
        ,'smap1.tipomov'       : '0'
        ,'smap1.ocultarRecibo' : ''
    }
    ,record
    );
    debug('<_p34_gridPolizasDocumentosClic');
}

function _p34_mostrarVentanaDocumentos(params,recordPoliza)
{
    debug('>_p34_mostrarVentanaDocumentos params:',params);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Documentos de la p&oacute;liza - '+recordPoliza.get('NMPOLIEX')
        ,_p34_window : 'si'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 600
        ,height      : 400
        ,autoScroll  : true
        ,closeAction : 'destroy'
        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
        ,loader      :
        {
            url       : _p34_urlVentanaDocumentos
            ,params   : params
            ,scripts  : true
            ,autoLoad : true
        }
    }).show());
    debug('<_p34_mostrarVentanaDocumentos');
}

function _p34_gridPolizasHistorialClic(record)
{
    debug('>_p34_gridPolizasHistorialClic record:',record.data);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Historial de p&oacute;liza - Sucursal '
                                   +record.get('CDUNIECO')+' - producto '
                                   +record.get('CDRAMO')  +' - p&oacuteliza '
                                   +record.get('NMPOLIZA')
        ,_p34_window : 'si'
        ,width       : 700
        ,maxHeight   : 400
        ,autoScroll  : true
        ,modal       : true
        ,closeAction : 'destroy'
        ,items       :
        [
            Ext.create('Ext.grid.Panel',
            {
                columns      : _p34_historicoColumns
                ,autoScroll  : true
                ,store       : Ext.create('Ext.data.Store',
                {
                    model     : '_p34_modeloHistorico'
                    ,autoLoad : true
                    ,proxy    :
                    {
                        type         : 'ajax'
                        ,url         : _p34_urlRecuperacionSimpleLista
                        ,extraParams :
                        {
                            'smap1.procedimiento' : 'RECUPERAR_HISTORICO_POLIZA'
                            ,'smap1.cdunieco'     : record.get('CDUNIECO')
                            ,'smap1.cdramo'       : record.get('CDRAMO')
                            ,'smap1.estado'       : record.get('ESTADO')
                            ,'smap1.nmpoliza'     : record.get('NMPOLIZA')
                        }
                        ,reader :
                        {
                            type  : 'json'
                            ,root : 'slist1'
                        }
                    }
                })
            })
        ]
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Aceptar'
                ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                ,handler : function(me){ me.up('window').destroy(); }
            }
        ]
    }).show());
    debug('<_p34_gridPolizasHistorialClic');
}

function _p34_gridPolizasIncisosClic(record, padre, callback)
{
    debug('>_p34_gridPolizasIncisosClic args:', arguments);
    _fieldById('_p34_gridPolizas').getSelectionModel().deselectAll();
    padre.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p34_urlObtenerColumnasIncisosPorRamo
        ,params :
        {
            'smap1.cdramo' : record.get('CDRAMO')
        }
        ,success : function(response)
        {
            padre.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### columnas ramo:',json);
            if(json.success)
            {
            _fieldById('_p34_gridPolizas').getSelectionModel().select(record);
                var cols=Ext.decode('['+json.smap1.columnas+']');
                debug('columnas:',cols);
                _p34_incisos('POLIZA',record,cols,padre,callback);
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            padre.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p34_gridPolizasIncisosClic');
}

function _p34_incisos(nivel,recordNivel,cols,padre,callback)
{
    debug('>_p34_incisos args:', arguments);
    
    var recordPoliza=_fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
    debug('recordPoliza:',recordPoliza.data);
    
    padre.setLoading(true);
    
    Ext.Ajax.request(
    {
        url      : _p34_urlRecuperacionSimple
        ,params  :
        {
            'smap1.cdramo'         : recordPoliza.get('CDRAMO')
            ,'smap1.procedimiento' : 'RECUPERAR_DSATRIBUS_TATRIPOL'
        }
        ,success : function(responsePol)
        {
            var jsonPol=Ext.decode(responsePol.responseText);
            debug('### leer descripcion atributos de Poliza:',jsonPol);
            if(jsonPol.exito)
            {            	
            	var arrayAtributosPol = [];                
                if(!Ext.isEmpty(recordPoliza.get('CDRAMO')) && new String(recordPoliza.get('CDRAMO')) == "6"){					
                	var atributosPol = jsonPol.smap1.listaNombres.split('@#@');
	                for(var atr in atributosPol)
	                {
	                    arrayAtributosPol.push(atributosPol[atr]);
	                }
                }
                
                debug('atributos para arrayAtributosPol:',arrayAtributosPol);
                Ext.ModelManager.getModel('_p34_modeloInciso').setFields(arrayAtributosPol);
                debug(new _p34_modeloInciso().data);
                
                Ext.Ajax.request(
			    {
			        url      : _p34_urlRecuperacionSimple
			        ,params  :
			        {
			            'smap1.cdramo'         : recordPoliza.get('CDRAMO')
			            ,'smap1.procedimiento' : 'RECUPERAR_DSATRIBUS_TATRISIT'
			        }
			        ,success : function(response)
			        {
			            var json=Ext.decode(response.responseText);
			            debug('### leer descripcion atributos:',json);
			            if(json.exito)
			            {
			                var arrayAtributos = [
			                    //MPOLISIT
			                    "CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
			                    ,{name:'NMSITUAC',type:'int'}
			                    , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
			                    ,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "CDGRUPO"
			                    ,"NMSITUAEXT" , "NMSITAUX" , "NMSBSITEXT" , "CDPLAN"
			                    ,"CDASEGUR"   , "DSGRUPO"
			                    ,{ name : 'FEFECSIT' , type : 'date' , dateFormat : 'd/m/Y' }
			                    ,{ name : 'FECHAREF' , type : 'date' , dateFormat : 'd/m/Y' }
			                    //TVALOSIT
			                    ,'NMSUPLEM_TVAL'
			                    ,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
			                    ,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
			                    ,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
			                    ,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
			                    ,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
			                    ,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
			                    ,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
			                    ,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
			                    ,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
			                    ,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
			                    ,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
			                    ,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
			                    ,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
			                    ,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
			                    ,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
			                    ,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
			                    ,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
			                    ,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
			                    ,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
			                    ,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
			                    //MPERSONA
			                    ,"CDPERSON"    , "CDTIPIDE"  , "CDIDEPER"   , "DSNOMBRE"
			                    ,"CDTIPPER"    , "OTFISJUR"  , "OTSEXO"     , "CDRFC"
			                    ,"FOTO"        , "DSEMAIL"   , "DSNOMBRE1"  , "DSAPELLIDO"
			                    ,"DSAPELLIDO1" , "CDNACION"  , "DSCOMNOM"   , "DSRAZSOC"
			                    ,"DSNOMUSU"    , "CDESTCIV"  , "CDGRUECO"   , "CDSTIPPE"
			                    ,"NMNUMNOM"    , "CURP"      , "CANALING"   , "CONDUCTO"
			                    ,"PTCUMUPR"    , "STATUSPER" , "RESIDENCIA" , "NONGRATA"
			                    ,"CDIDEEXT"    , "CDSUCEMI"
			                    ,{ name : 'FENACIMI'  , type : 'date' , dateFormat : 'd/m/Y' }
			                    ,{ name : 'FEINGRESO' , type : 'date' , dateFormat : 'd/m/Y' }
			                    ,{ name : 'FEACTUAL'  , type : 'date' , dateFormat : 'd/m/Y' }
			                    //MPOLIPER
			                    ,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
			                    //CUSTOM
			                    ,'ATRIBUTOS','NOMBRECOMPLETO'
			                    //MPLANES
			                    ,'DSPLAN'
			                ];
			                var splited = json.smap1.listaNombres.split('@#@');
			                /* debug('json.smap1.listaNombre',json.smap1.listaNombres); */
			                arrayAtributos.push('swselect');
			                for(var i in splited)
			                {
			                    arrayAtributos.push(splited[i]);
			                }

			                for(var atrP in arrayAtributosPol)
			                {
			                    arrayAtributos.push(arrayAtributosPol[atrP]);
			                }
			                
			                debug('atributos para el modelo:',arrayAtributos);
			                Ext.ModelManager.getModel('_p34_modeloInciso').setFields(arrayAtributos);
			                debug(new _p34_modeloInciso().data);
			                padre.setLoading(false);
							_p34_storeIncisos.getProxy().setExtraParam('smap1.dsatribu', null);
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.otvalor', null);
			                debug('******* no se setea smap1.nmfamili');
			                //_p34_storeIncisos.getProxy().setExtraParam('smap1.nmfamili', null);			                
			                centrarVentanaInterna(Ext.create('Ext.window.Window',
			                {
			                    itemId       : '_p34_windowIncisos'
			                    ,title       : 'Incisos de la p&oacute;liza - '
			                                   +recordPoliza.get('NMPOLIEX')
			                                   +(nivel=='GRUPO'  ?(' - grupo ' +recordNivel.get('CDGRUPO' )):'')
			                                   +(nivel=='FAMILIA'?(' - familia'+recordNivel.get('NMSITAUX')):'')
			                    ,_p34_window : 'si'
			                    ,closeAction : 'destroy'
			                    ,modal       : true
			                    ,width       : 950
			                    ,maxHeight   : 400
			                    ,autoScroll  : true
			                    ,items       :
			                    [
			                        Ext.create('Ext.grid.Panel',
			                        {
			                            itemId      : '_p34_gridIncisos'
			                            ,columns    : cols
			                            ,width      : 910
			                            ,height     : 330
			                            ,autoScroll : true
			                            /* ,selModel   : Ext.create('Ext.selection.CheckboxModel', {mode: 'MULTI'}) */			                            
			                            ,selModel   : 
  			                             {			                              
			                                selType    : 'checkboxmodel'
			                                 ,mode      : 'MULTI' 
			                                 ,listeners :
			                                {
			                                    selectionchange : function(me,selected)
			                                    {
													if(_p34_soloNivelPoliza === true)
			                                        {
			                                            mensajeWarning('Solo se permiten endosos a nivel p\u00f3liza');
			                                        }
			                                        else
			                                        {
			                                            _fieldById('_p34_botonEndososIncisos').setDisabled(selected.length==0);
			                                        }
			                                    }
			                                }
			                            }
				                        ,viewConfig: {
				                        	id: 'gv'
				                            ,trackOver: true
				                            ,stripeRows: true
				                            ,loadMask : false
				                        }
			                        	,plugins: [
			                        	           {
			                        	        	   ptype       : 'pagingselectpersist'
			                        	        	   ,pluginId   : 'pagingselect'
			                        	        	   }
			                        	           ] 
			                            ,store   : _p34_storeIncisos
			                            ,tbar    :
			                            [
			                                {
			                                    text      : 'Endosos...'
			                                    ,itemId   : '_p34_botonEndososIncisos'
			                                    ,icon     : '${ctx}/resources/fam3icons/icons/book_addresses.png'
			                                    ,disabled : true
			                                    ,handler  : function(){ _p34_botonEndososIncisosClic(); }
			                                }
			                                ,'->'
					                        ,{
				                            	 xtype    : 'button'
				                            	 ,text    : 'Limpiar todo'
				                            	 ,icon  : '${ctx}/resources/fam3icons/icons/delete.png'
				                            	 ,handler : function(btn) {
				                            		 _fieldById('_p34_gridIncisos').getPlugin('pagingselect').clearPersistedSelection();
				                            	 }
				                             }
			                                ,Ext.create('Ext.form.ComboBox',
			                                {
			                                    name        : 'dsatribu'
			                                    ,allowBlank : false
			                                    ,style:'margin:5px'
			                                    ,typeAhead:true
			                                    ,anyMatch:true
			                                    ,displayField:'value'
			                                    ,valueField:'key'
			                                    ,matchFieldWidth:false
			                                    ,listConfig:
			                                    {
			                                        maxHeight:150
			                                        ,minWidth:120
			                                    }
			                                    ,forceSelection:true
			                                    ,editable:true
			                                    ,queryMode:'local'
			                                    ,store:Ext.create('Ext.data.Store',
			                                    {
			                                    	model:'Generic'
			                                    	,autoLoad:true
			                                    	,proxy:
			                                    	{
			                                    	    type:'ajax'
			                                    	    ,url:_p34_urlObtieneCatalogos
			                                    	    ,reader:
			                                    	    {
			                                    	        type:'json'
			                                    	        ,root:'lista'
			                                    	        ,rootProperty:'lista'
			                                    	    }
			                                    	    ,extraParams:
			                                    	    {
			                                    	        catalogo           : 'RECUPERAR_LISTA_FILTRO_PROPIEDAD_INCISO'
                                                            ,'params.cdramo'   :  recordPoliza.get('CDRAMO')
                                                            ,'params.cdtipsit' :  recordPoliza.get('CDTIPSIT')
                                                            ,'params.nivel'    :  "I"
			                                    	    }
			                                    	}
				                                    ,listeners :
					                                {
					                                    load: function(st) {
					                                    	_fieldByName('dsatribu').setValue('NOMBRE ASEGURADO'); // Se setea un valor del combo al inicio
					                                    }
					                                }
			                                    })
			                                })
			                                ,{
			                                    xtype      : 'textfield',
			                                    name       : 'txtBuscar'
			                                }
				                            ,{
				                                xtype    : 'button'
				                                ,text    : 'Buscar'
				                                //,icon  : panDocContexto+'/resources/fam3icons/icons/add.png'
				                                ,handler : function(btn) {
				                                	debug('Buscando...');				                                	
				                                	if(Ext.isEmpty(btn.up('toolbar').down('textfield[name=txtBuscar]').getValue())){
				                                    	debug('valor nulo...');
				                                    	btn.up('toolbar').down('combo[name=dsatribu]').setValue('NOMBRE ASEGURADO');
				                                    }				                                	
				                                    _p34_storeIncisos.getProxy().setExtraParam('smap1.dsatribu',btn.up('toolbar').down('combo[name=dsatribu]').getValue());
				                                    _p34_storeIncisos.getProxy().setExtraParam('smap1.otvalor', btn.up('toolbar').down('textfield[name=txtBuscar]').getValue());
				                                    _p34_storeIncisos.loadPage(1);
				                                }
                							}
			                            ],
					                    bbar: Ext.create('Ext.PagingToolbar', {
								            store: _p34_storeIncisos,
								            displayInfo: true
								        })
								        //,renderTo: '_p34_gridIncisos-grid'
			                        })
			                    ]
			                    ,listeners:{
			                    	close:function(me){
			                    		/* debug('grid incisos '    ,_fieldById('_p34_gridIncisos').getPlugin('pagingselect'));
			                    		debug('grid incisos plug',_fieldById('_p34_gridIncisos').getPlugin('pagingselect')); */
			                    		_fieldById('_p34_gridIncisos').getPlugin('pagingselect').clearPersistedSelection();
			                    		debug('seleccionados',_fieldById('_p34_gridIncisos').getPlugin('pagingselect').selected);
			                    	}
			                    }
			                }).show());
			                
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.cdunieco',recordPoliza.get('CDUNIECO'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.cdramo',  recordPoliza.get('CDRAMO'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.estado',  recordPoliza.get('ESTADO'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.nmpoliza',recordPoliza.get('NMPOLIZA'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.cdgrupo', recordPoliza.get('CDGRUPO'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.nmfamili',nivel == 'POLIZA' ? null : recordNivel.get('NMSITAUX'));
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.nivel',   nivel);
			                _p34_storeIncisos.getProxy().setExtraParam('smap1.atrPol',  (!Ext.isEmpty(recordPoliza.get('CDRAMO')) && new String(recordPoliza.get('CDRAMO')) == "6")? 'S':'N');
			                if (!Ext.isEmpty(callback) && typeof callback === 'function') {
			                    _p34_storeIncisos.loadPage(1, {
			                        callback : callback
			                    });
			                } else {
			                    _p34_storeIncisos.loadPage(1);
			                }
					    }
					    else
					    {
					        mensajeError(json.respuesta);
					    }
			        }
			    });
                
		    }
		    else
		    {
		        mensajeError(jsonPol.respuesta);
		    }
        }
    });
    
    debug('<_p34_incisos');
}

function _p34_botonEndososIncisosClic(callback)
{
    debug('>_p34_botonEndososIncisosClic args:', arguments);
    
    var windowIncisos = _fieldById('_p34_windowIncisos');
    var poliza        = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
    var incisos       = _fieldById('_p34_gridIncisos').getSelectionModel().getSelection();
    debug('poliza:'  , poliza.data);
    debug('incisos:' , incisos);
    
    var cdramo   = poliza.get('CDRAMO');
    var nivel    = 'INCISO';
    var multiple = incisos.length>1?'S':'N';
    var tipoflot = 'I';
    debug('Creando CDTIPSIT',poliza.get('CDTIPSIT'))
    var cdtipsit = poliza.get('CDTIPSIT');
    if(!Ext.isEmpty(poliza.get('TIPOFLOT')))
    {
        tipoflot = poliza.get('TIPOFLOT');
    }
    
    var incisosRaw=[];
    for(var i in incisos)
    {
        var incisoIte=incisos[i].raw;
        incisoIte['ATRIBUTOS']=incisos[i].get('ATRIBUTOS');
        incisosRaw.push(incisoIte);
    }
    
    windowIncisos.setLoading(true);
    debug('_p34_storeEndosos:',_p34_storeEndosos);
    Ext.Ajax.request(
    {
        url       : _p34_urlRecuperarEndososClasificados
        ,jsonData :
        {
            smap1 :
            {
                cdramo     : cdramo
                ,nivel     : nivel
                ,multiple  : multiple
                ,tipoflot  : tipoflot
                ,cancelada : !Ext.isEmpty(poliza.get('FEANULAC'))?'S':'N'
                ,cdtipsit  : cdtipsit
            }
            ,slist1 : incisosRaw
        }
        ,success : function(response)
        {
            windowIncisos.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### endosos clasificados inciso:',json);
            if(json.success)
            {
                if(json.slist1.length>0)
                {
                    _p34_storeEndosos.removeAll();
                    for(var i in json.slist1)
                    {
                        _p34_storeEndosos.add(new _p34_modeloEndoso(json.slist1[i]));
                    }
                    _p34_mostrarListaEndosos('INCISO',json.smap1.stamp);
                }
                else
                {
                    mensajeWarning('No hay endosos disponibles para la selecci&oacute;n');
                }
                
                if (!Ext.isEmpty(callback) && typeof callback === 'function') {
                    callback();
                }
            }
            else
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
            windowIncisos.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p34_botonEndososIncisosClic');
}

function _p34_renderer(valor,mapeo,view)
{
    debug('_p34_renderer valor,mapeo,view.id:',valor,mapeo,view.id);
    var mapeos=mapeo.split('@');
    debug('mapeos:',mapeos);
    var valores  = valor.split('|');
    debug('valores:',valores);
    var cdtipsit = valores[0];
    debug('cdtipsit:',cdtipsit);
    var mapeado=false;
    for(var i in mapeos)
    {
        var bloques   = mapeos[i].split('#');
        var cdtipsits = bloques[0];
        var cdatribu  = bloques[1];
        var tipo      = bloques[2];
        if(('|'+cdtipsits+'|').lastIndexOf('|'+cdtipsit+'|')!=-1)
        {
            debug('tipo:'     , tipo);
            debug('cdatribu:' , cdatribu);
            if(tipo+'x'=='VALORx')
            {
                mapeado=true;
                valor=valores[cdatribu].split('~')[0];
                debug('tipo VALOR cdatribu,valor:',cdatribu,valor,'.');
                break;
            }
            else if(tipo+'x'=='DISPLAYx')
            {
                mapeado=true;
                valor=valores[cdatribu].split('~')[1];
                debug('tipo DISPLAY cdatribu,valor:',cdatribu,valor,'.');
                break;
            }
        } 
    }
    if(!mapeado)
    {
        valor='N/A';
    }
    return valor;
}

function _p34_mostrarListaEndosos(nivel,stamp)
{
    debug('>_p34_mostrarListaEndosos nivel:',nivel,'stamp:',stamp);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Endosos disponibles'
        ,_p34_window : 'si'
        ,itemId      : '_p34_windowEndosos'
        ,closeAction : 'destroy'
        //,maxHeight   : 400
        ,autoScroll  : true
        ,modal       : true
        ,items       :
        [
            Ext.create('Ext.grid.Panel',
            {
                itemId       : '_p34_gridEndosos'
                ,width       : 700
                ,store       : _p34_storeEndosos
                ,nivel       : nivel
                ,stamp       : stamp
                //,hideHeaders : true
                ,columns     :
                [
                    {
                        text      : 'Clave'
                        ,dataIndex: 'CDTIPSUP'
                        ,flex     : 1
                        ,hidden   : true
                    },
                    {
                        text      : 'Descripci\u00F3n'
                        ,dataIndex : 'DSTIPSUP'
                        ,flex     : 9
                    }
                ]
                ,listeners :
                {
                    cellclick : function(view, td, cellIndex, record)
                    {
                        var nivel = view.up('grid').nivel;
                        var stamp = view.up('grid').stamp;
                        debug('nivel:',nivel,'stamp:',stamp);
                        /*
                         * Si viene esta bandera encendida significa que esta pantalla solo se utiliza para seleccionar un endoso
                         * y guardar el cdtipsup en el otvalor de mesa de control correspondiente
                         */
                        if(_p34_flujoAux.seleccionarEndoso === 'S' && !Ext.isEmpty(_p34_flujoAux.onSeleccionar) )
                        {
                            var ck = 'Guardando clave de endoso';
                            try
                            {
                                centrarVentanaInterna(
                                    Ext.MessageBox.confirm(
                                        'Confirmar'
                                        ,'¿Desea marcar el tr\u00e1mite para endoso '+record.get('DSTIPSUP')+'?<br>Esta acci\u00f3n no podr\u00e1 deshacerse'
                                        ,function(btn)
                                        {
                                            if(btn === 'yes')
                                            {
				                                _p34_guardarAtributoTramitePorDescripcion(
				                                    _p34_flujo.ntramite
				                                    ,'CDTIPSUP'
				                                    ,record.get('CDTIPSUP')
				                                    ,function()
				                                    {
				                                        _p34_guardarAtributoTramitePorDescripcion(
						                                    _p34_flujo.ntramite
						                                    ,'DSTIPSUP'
						                                    ,record.get('DSTIPSUP')
						                                    ,function()
						                                    {
						                                        var mask, ck = 'Cambiando estatus de tr\u00e1mite';
						                                        try
						                                        {
						                                    	    mask = _maskLocal(ck);
											                        Ext.Ajax.request(
											                        {
											                            url      : _GLOBAL_COMP_URL_TURNAR
											                            ,params  :
											                            {
											                                'params.CDTIPFLU'   : _p34_flujo.cdtipflu
											                                ,'params.CDFLUJOMC' : _p34_flujo.cdflujomc
											                                ,'params.NTRAMITE'  : _p34_flujo.ntramite
											                                ,'params.STATUSOLD' : _p34_flujo.status
											                                ,'params.STATUSNEW' : _p34_flujoAux.onSeleccionar
											                                ,'params.COMMENTS'  : 'Endoso seleccionado: '+record.get('DSTIPSUP')
											                                ,'params.SWAGENTE'  : 'S'
											                            }
											                            ,success : function(response)
											                            {
											                                mask.close();
											                                var ck = 'Decodificando respuesta al cambiar status de tr\u00e1mite';
											                                try
											                                {
											                                    var json = Ext.decode(response.responseText);
											                                    debug('### cambiar estatus:',json);
											                                    if(json.success)
											                                    {
											                                        mensajeCorrecto
											                                        (
											                                            'Estatus actualizado'
											                                            //,json.message
											                                            ,'El estatus de tr\u00e1mite fue actualizado'
											                                            ,function()
											                                            {
											                                                _mask('Redireccionando');
											                                                Ext.create('Ext.form.Panel').submit(
											                                                {
											                                                    url             : _GLOBAL_COMP_URL_MCFLUJO
											                                                    ,standardSubmit : true
											                                                });
											                                            }
											                                        );
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
											                                mask.close();
											                                errorComunicacion(null,'Error al cambiar estatus de tr\u00e1mite');
											                            }
											                        });
											                    }
											                    catch(e)
											                    {
											                        manejaException(e,ck,mask);
											                    }
						                                    }
						                                );
				                                    }
				                                );
				                            }
				                        }
				                    )
				                );
                            }
                            catch(e)
                            {
                                manejaException(e,ck);
                            }
                        }
                        else if(nivel=='POLIZA')
                        {
                            var poliza        = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
                            var smap1         = poliza.raw;
                            smap1['tstamp']   = stamp;
                            smap1['cdtipsup'] = record.get('CDTIPSUP');
                            smap1['TIPO_VALIDACION'] = record.get('TIPO_VALIDACION');
                            smap1['pantallaOrigen'] = 'MARCO_ENDOSOS_GENERAL';
                            debug('smap1:',smap1);
                            
                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                            {
                                itemId       : '_p34_endosoWindow'
                                ,modal       : true
                                ,title       : record.get('DSTIPSUP')+' - P\u00D3LIZA '+poliza.get('NMPOLIEX')
                                ,_p34_window : 'si'
                                ,width       : 950
                                ,height      : 500
                                ,closeAction : 'destroy'
                                ,maximizable  : true
                                ,autoScroll  : true
                                ,loader      :
                                {
                                    url       : _p34_contexto+record.get('LIGA')
                                    ,autoLoad : false
                                    ,scripts  : true
                                }
                            }).show());
                            
                            var jsonData =
                            {
                                smap1   : smap1
                                ,params : smap1
                            };
                            
                            if(!Ext.isEmpty(_p34_flujo))
                            {
                                jsonData.flujo = _p34_flujo;
                            }
                            
                            _fieldById('_p34_endosoWindow').getLoader().load(
                            {
                                jsonData : jsonData
                            });
                        }
                        else if(nivel=='INCISO')
                        {
                            var poliza        = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
                            var smap1         = poliza.raw;
                            smap1['tstamp']   = stamp;
                            smap1['cdtipsup'] = record.get('CDTIPSUP');
                            smap1['TIPO_VALIDACION'] = record.get('TIPO_VALIDACION');
                            smap1['pantallaOrigen']   = 'MARCO_ENDOSOS_GENERAL';
                            debug('smap1:',smap1);
                            
                            var incisos = _fieldById('_p34_gridIncisos').getSelectionModel().getSelection();
                            debug('incisos:',incisos);
                            
                            var incisosRaw=[];
                            for(var i in incisos)
                            {
                                var incisoIte=incisos[i].raw;
                                incisoIte['ATRIBUTOS']=incisos[i].get('ATRIBUTOS');
                                incisosRaw.push(incisoIte);
                            }
                            debug('incisosRaw:',incisosRaw);
                            
                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                            {
                                itemId       : '_p34_endosoWindow'
                                ,modal       : true
                                ,title       : record.get('DSTIPSUP')+' - P\u00D3LIZA '+poliza.get('NMPOLIEX')
                                ,_p34_window : 'si'
                                ,width       : 950
                                ,height      : 500
                                ,closeAction : 'destroy'
                                ,maximizable  : true
                                ,autoScroll  : true
                                ,loader      :
                                {
                                    url       : _p34_contexto+record.get('LIGA')
                                    ,autoLoad : false
                                    ,scripts  : true
                                }
                            }).show());
                            
                            var jsonData =
                            {
                                smap1   : smap1
                                ,params : smap1
                                ,slist1 : incisosRaw
                                ,list   : incisosRaw
                            };
                            
                            if(!Ext.isEmpty(_p34_flujo))
                            {
                                jsonData.flujo = _p34_flujo;
                            }
                            
                            _fieldById('_p34_endosoWindow').getLoader().load(
                            {
                                jsonData : jsonData
                            });
                        }
                    }
                }
            })
        ]
    }).show());
    debug('<_p34_mostrarListaEndosos');
}

function _p34_gridPolizasGruposClic(row)
{
    var record=_p34_storePolizas.getAt(row);
    debug('>_p34_gridPolizasGruposClic record:',record.data);
    _fieldById('_p34_gridPolizas').getSelectionModel().deselectAll();
    var windowPolizas=_fieldById('_p34_gridPolizas');
    windowPolizas.setLoading(true);
    _p34_storeGrupos.load(
    {
        params :
        {
            'smap1.cdunieco'  : record.get('CDUNIECO')
            ,'smap1.cdramo'   : record.get('CDRAMO')
            ,'smap1.estado'   : record.get('ESTADO')
            ,'smap1.nmpoliza' : record.get('NMPOLIZA')
        }
        ,callback : function(records,operation,success)
        {
            _fieldById('_p34_gridPolizas').getSelectionModel().select(record);
            windowPolizas.setLoading(false);
            if(success)
            {
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    itemId       : '_p34_windowGrupos'
                    ,title       : 'Grupos de la p&oacute;liza - Sucursal '
                                   +record.get('CDUNIECO')+' - producto '
                                   +record.get('CDRAMO')  +' - p&oacuteliza '
                                   +record.get('NMPOLIZA')
                    ,_p34_window : 'si'
                    ,modal       : true
                    ,closeAction : 'destroy'
                    ,width       : 500
                    ,maxHheight  : 400
                    ,autoScroll  : true
                    ,items       :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            itemId    : '_p34_gridGrupos'
                            ,columns  : _p34_gridGruposColumns
                            ,width    : 450
                            ,selModel :
                            {
                                selType    : 'checkboxmodel'
                                ,mode      : 'SIMPLE'
                                ,listeners :
                                {
                                    selectionchange : function(me,selected)
                                    {
                                        _fieldById('_p34_botonEndososGrupos').setDisabled(selected.length==0);
                                    }
                                }
                            }
                            ,store   : _p34_storeGrupos
                            ,tbar    :
                            [
                                {
                                    text      : 'Endosos...'
                                    ,itemId   : '_p34_botonEndososGrupos'
                                    ,icon     : '${ctx}/resources/fam3icons/icons/book_addresses.png'
                                    ,disabled : true
                                    ,handler  : function(){ _p34_botonEndososGruposClic(); }
                                }
                                ,'->'
                                ,{
                                    xtype      : 'textfield'
                                    ,listeners :
                                    {
                                        afterrender : function()
                                        {
                                            _p34_filtrarStore('',_p34_storeGrupos);
                                        }
                                        ,change : function(me,val)
                                        {
                                            _p34_filtrarStore(val,_p34_storeGrupos);
                                        }
                                    }
                                }
                            ]
                        })
                    ]
                }).show());
            }
            else
            {
                mensajeError(operation.getError());
            }
        }
    });
    debug('<_p34_gridPolizasGruposClic');
}

function _p34_gridPolizasFamiliasClic(row)
{
    var record=_p34_storePolizas.getAt(row);
    debug('>_p34_gridPolizasFamiliasClic record:',record.data);
    _fieldById('_p34_gridPolizas').getSelectionModel().deselectAll();
    var windowPolizas=_fieldById('_p34_gridPolizas');
    windowPolizas.setLoading(true);
    _p34_storeFamilias.load(
    {
        params :
        {
            'smap1.cdunieco'  : record.get('CDUNIECO')
            ,'smap1.cdramo'   : record.get('CDRAMO')
            ,'smap1.estado'   : record.get('ESTADO')
            ,'smap1.nmpoliza' : record.get('NMPOLIZA')
        }
        ,callback : function(records,operation,success)
        {
            _fieldById('_p34_gridPolizas').getSelectionModel().select(record);
            windowPolizas.setLoading(false);
            if(success)
            {
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    itemId       : '_p34_windowFamilias'
                    ,title       : 'Familias de la p&oacute;liza - Sucursal '
                                   +record.get('CDUNIECO')+' - producto '
                                   +record.get('CDRAMO')  +' - p&oacuteliza '
                                   +record.get('NMPOLIZA')
                    ,_p34_window : 'si'
                    ,modal       : true
                    ,closeAction : 'destroy'
                    ,width       : 500
                    ,items       :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            itemId    : '_p34_gridFamilias'
                            ,columns  : _p34_gridFamiliasColumns
                            ,autoScroll : true 
                            ,width    : 490
                            ,height  : 400
                            ,selModel :
                            {
                                selType    : 'checkboxmodel'
                                ,mode      : 'SIMPLE'
                                ,listeners :
                                {
                                    selectionchange : function(me,selected)
                                    {
                                        _fieldById('_p34_botonEndososFamilias').setDisabled(selected.length==0);
                                    }
                                }
                            }
                            ,store   : _p34_storeFamilias
                            ,tbar    :
                            [
                                {
                                    text      : 'Endosos...'
                                    ,itemId   : '_p34_botonEndososFamilias'
                                    ,icon     : '${ctx}/resources/fam3icons/icons/book_addresses.png'
                                    ,disabled : true
                                    ,handler  : function(){ _p34_botonEndososFamiliasClic(); }
                                }
                                ,'->'
                                ,{
                                    xtype      : 'textfield'
                                    ,listeners :
                                    {
                                        afterrender : function()
                                        {
                                            _p34_filtrarStore('',_p34_storeFamilias);
                                        }
                                        ,change : function(me,val)
                                        {
                                            _p34_filtrarStore(val,_p34_storeFamilias);
                                        }
                                    }
                                }
                            ]
                        })
                    ]
                }).show());
            }
            else
            {
                mensajeError(operation.getError());
            }
        }
    });
    debug('<_p34_gridPolizasFamiliasClic');
}

function _p34_botonEndososGruposClic()
{
    debug('>_p34_botonEndososGruposClic');
    var windowGrupos = _fieldById('_p34_windowGrupos');
    var poliza       = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
    var grupos       = _fieldById('_p34_gridGrupos').getSelectionModel().getSelection();
    debug('poliza:',poliza.data);
    
    var cdramo   = poliza.get('CDRAMO');
    var nivel    = 'GRUPO';
    var multiple = grupos.length>1?'S':'N';
    var tipoflot = 'I';
    if(!Ext.isEmpty(poliza.get('TIPOFLOT')))
    {
        tipoflot = poliza.get('TIPOFLOT');
    }
    
    windowGrupos.setLoading(true);
    debug('_p34_storeEndosos:',_p34_storeEndosos);
    Ext.Ajax.request(
    {
        url       : _p34_urlRecuperarEndososClasificados
        ,jsonData :
        {
            smap1 :
            {
                cdramo    : cdramo
                ,nivel    : nivel
                ,multiple : multiple
                ,tipoflot : tipoflot
            }
        }
        ,success : function(response)
        {
            windowGrupos.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### endosos clasificados grupos:',json);
            if(json.success)
            {
                if(json.slist1.length>0)
                {
                    _p34_storeEndosos.removeAll();
                    for(var i in json.slist1)
                    {
                        _p34_storeEndosos.add(new _p34_modeloEndoso(json.slist1[i]));
                    }
                    _p34_mostrarListaEndosos('GRUPO',json.smap1.stamp);
                }
                else
                {
                    mensajeWarning('No hay endosos disponibles para la selecci&oacute;n');
                }
            }
            else
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
            windowGrupos.setLoading(false);
            errorComunicacion();
        }
    });
    
    debug('<_p34_botonEndososGruposClic');
}

function _p34_botonEndososFamiliasClic()
{
    debug('>_p34_botonEndososFamiliasClic');
    var windowFamilias = _fieldById('_p34_windowFamilias');
    var poliza         = _fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0];
    var familias       = _fieldById('_p34_gridFamilias').getSelectionModel().getSelection();
    debug('poliza:',poliza.data);
    
    var cdramo   = poliza.get('CDRAMO');
    var nivel    = 'FAMILIA';
    var multiple = familias.length>1?'S':'N';
    var tipoflot = 'I';
    if(!Ext.isEmpty(poliza.get('TIPOFLOT')))
    {
        tipoflot = poliza.get('TIPOFLOT');
    }
    
    windowFamilias.setLoading(true);
    debug('_p34_storeEndosos:',_p34_storeEndosos);
    Ext.Ajax.request(
    {
        url       : _p34_urlRecuperarEndososClasificados
        ,jsonData :
        {
            smap1 :
            {
                cdramo    : cdramo
                ,nivel    : nivel
                ,multiple : multiple
                ,tipoflot : tipoflot
            }
        }
        ,success : function(response)
        {
            windowFamilias.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### endosos clasificados familias:',json);
            if(json.success)
            {
                if(json.slist1.length>0)
                {
                    _p34_storeEndosos.removeAll();
                    for(var i in json.slist1)
                    {
                        _p34_storeEndosos.add(new _p34_modeloEndoso(json.slist1[i]));
                    }
                    _p34_mostrarListaEndosos('FAMILIA',json.smap1.stamp);
                }
                else
                {
                    mensajeWarning('No hay endosos disponibles para la selecci&oacute;n');
                }
            }
            else
            {
                mensajeWarning(json.respuesta);
            }
        }
        ,failure : function()
        {
            windowFamilias.setLoading(false);
            errorComunicacion();
        }
    });
    
    debug('<_p34_botonEndososFamiliasClic');
}

function _p34_gridGruposIncisosClic(record,padre)
{
    debug('>_p34_gridGruposIncisosClic record:',record.data,'padre:',padre);
    _fieldById('_p34_gridGrupos').getSelectionModel().deselectAll();
    var cdramoPoliza=_fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0].get('CDRAMO');
    debug('>cdramoPoliza:',cdramoPoliza);
    padre.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p34_urlObtenerColumnasIncisosPorRamo
        ,params :
        {
            'smap1.cdramo' : cdramoPoliza
        }
        ,success : function(response)
        {
            padre.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### columnas ramo:',json);
            if(json.success)
            {
                _fieldById('_p34_gridGrupos').getSelectionModel().select(record);
                var cols=Ext.decode('['+json.smap1.columnas+']');
                debug('columnas:',cols);
                _p34_incisos('GRUPO',record,cols,padre);
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            padre.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p34_gridGruposIncisosClic');
}

function _p34_gridFamiliasIncisosClic(record,padre)
{
    debug('>_p34_gridFamiliasIncisosClic record:',record.data,'padre:',padre);
    _fieldById('_p34_gridFamilias').getSelectionModel().deselectAll();
    var cdramoPoliza=_fieldById('_p34_gridPolizas').getSelectionModel().getSelection()[0].get('CDRAMO');
    debug('>cdramoPoliza:',cdramoPoliza);
    padre.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p34_urlObtenerColumnasIncisosPorRamo
        ,params :
        {
            'smap1.cdramo' : cdramoPoliza
        }
        ,success : function(response)
        {
            padre.setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('### columnas ramo:',json);
            if(json.success)
            {
                _fieldById('_p34_gridFamilias').getSelectionModel().select(record);
                var cols=Ext.decode('['+json.smap1.columnas+']');
                debug('columnas:',cols);
                _p34_incisos('FAMILIA',record,cols,padre);
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            padre.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p34_gridFamiliasIncisosClic');
}

function _p34_filtrarStore(fil,store)
{
    debug('>_p34_filtrarStore fil:',fil);
    if(Ext.isEmpty(fil))
    {
        store.clearFilter();
    }
    else
    {
        fil = fil.toUpperCase().replace(/ /g,'');
        debug('filtro:',fil);
        store.filterBy(function(record)
        {
            var incluido = false;
            for(var clave in record.raw)
            {
                var valor=(String(record.raw[clave])).toUpperCase().replace(/ /g,'');
                if(valor.lastIndexOf(fil)!=-1)
                {
                    incluido=true;
                    break;
                }
            }
            return incluido;
        });
    }
}

function marendNavegacion(nivel)
{
    debug('>marendNavegacion:',nivel);
    if(Number(nivel)==2)
    {
        if(Ext.isEmpty(_p34_flujo))
        {
            var ventanas=Ext.ComponentQuery.query('[_p34_window=si]');
            debug('ventanas:',ventanas);
            for(var i in ventanas)
            {
                ventanas[i].close();
            }
            _p34_storePolizas.reload();
        }
        else
        {
            //cuando es por flujo, despues de confirmar un endoso, se va a mesa de control
            
            var ck = 'Creando componente de documentos';
            try
            {
                Ext.syncRequire(_GLOBAL_DIRECTORIO_DEFINES+'VentanaDocumentos');
                new window['VentanaDocumentos'](
                {
                    cdtipflu   : _p34_flujo.cdtipflu
                    ,cdflujomc : _p34_flujo.cdflujomc
                    ,tipoent   : _p34_flujo.tipodest
                    ,claveent  : _p34_flujo.clavedest
                    ,webid     : _p34_flujo.webiddest
                    ,aux       : _p34_flujo.aux
                    ,ntramite  : _p34_flujo.ntramite
                    ,status    : _p34_flujo.status
                    ,cdunieco  : _p34_flujo.cdunieco
                    ,cdramo    : _p34_flujo.cdramo
                    ,estado    : _p34_flujo.estado
                    ,nmpoliza  : _p34_flujo.nmpoliza
                    ,nmsituac  : _p34_flujo.nmsituac
                    ,nmsuplem  : _p34_flujo.nmsuplem
                    ,cdusuari  : _p34_flujo.cdusuari
                    ,cdsisrol  : _p34_flujo.cdsisrol
                    ,listeners :
                    {
                        close : function()
                        {
                            _mask('Redireccionando...');
                            Ext.create('Ext.form.Panel').submit(
                            {
                                standardSubmit : true
                                ,url           : _GLOBAL_COMP_URL_MCFLUJO
                            });
                        }
                    }
                }).mostrar();
            }
            catch(e)
            {
                manejaException(e,ck);
            }
        }
    }
    debug('<marendNavegacion');
}

/*
Esta funcion se crea para el endoso de alta de asegurados de salud colectivo:
1) Se entra al marco directo o desde mesa de control
2) Se entra por primera vez al endoso de alta de asegurados de salud colectivo
3) En el endoso de alta de asegurados se sube un excel, y despues de subirlo hace un submit a esta pantalla, enviando los siguientes atributos:
    a) smap1.cdunieco
    b) smap1.cdramo
    c) smap1.estado
    d) smap1.nmpoliza
    e) smap1.recuperarEndosoAltaAseguradosSaludCol = 'S'
4) Cuando este marco encuentra esa bandera (recuperarEndosoAltaAseguradosSaludCol) encendida (=== 'S') entonces hace lo siguiente (esta funcion nueva):
    4.1 Manda a cargar la poliza con cdunieco, cdramo, estado y nmpoliza recibidos en smap1
    4.2 Manda a cargar los endosos
    4.3 Vuelve a abrir el endoso de alta de asegurados
*/
function _p34_recuperarEndosoAltaAseguradosSaludCol () {
    debug('_p34_recuperarEndosoAltaAseguradosSaludCol');
    if ('S' === _p34_smap1.recuperarEndosoAltaAseguradosSaludCol && Ext.isEmpty(_p34_flujo)) {
        debug('_p34_recuperarEndosoAltaAseguradosSaludCol entro a recuperar');
        var ck = 'Recuperando datos para continuar endoso de alta de asegurados';
        try {
	        _p34_recuperarDatosPoliza(
	            _p34_smap1.cdunieco,
	            _p34_smap1.cdramo,
	            _p34_smap1.estado,
	            _p34_smap1.nmpoliza,
	            function (poliza) {
	                debug('callback despues de poliza:',poliza,'.');
	                var ck = 'Decodificando respuesta al recuperar p\00f3liza para continuar alta de asegurados';
	                try {
	                    var form = _fieldById('_p34_formBusq');
	                    var hid = Ext.ComponentQuery.query('[hidden=true]',form);
	                    /* jtezva ¿para que?
	                    for (var i = 0; i < hid.length; i++) {
	                        hid[i].show();
	                    }
	                    */
	                    _fieldByName('cdunieco',form).setValue(Ext.isEmpty(poliza.CDUNIEXT) ? _p34_smap1.cdunieco : poliza.CDUNIEXT);
	                    _fieldByName('cdramo',form).setValue(_p34_smap1.cdramo);
	                    _fieldByName('nmpoliza',form).setValue(_p34_smap1.nmpoliza);
	                    _fieldByName('finicio',form).setValue('01/01/1900');
	                    _fieldByName('ffin',form).setValue('01/01/2500');
	                    _fieldByName('ramo',form).forceSelection = false;
	                    _fieldByName('ramo',form).setValue(poliza.RAMO);
	                    /* jtezva ¿para que?
	                    form.hide();
	                    */
	                    _p34_polizas(
	                        function () {
	                            debug('callback despues de cargar poliza desde flujo');
	                            var ck = 'Seleccionando p\u00f3liza';
	                            try {
	                                if (_p34_storePolizas.getCount() === 0) {
	                                    throw 'No se encuentra la p\u00f3liza para continuar el alta de asegurados';
	                                }
	                                _fieldById('_p34_gridPolizas').getSelectionModel().select([_p34_storePolizas.getAt(0)]);
	                                // ^ Seleccionamos una poliza
	                                _p34_botonEndososPolizaClic(function () { // Abrimos los endosos de poliza
	                                    if (_p34_storeEndosos.getCount() === 0) {
	                                        throw 'No se encuentran endosos para continuar el alta de asegurados';
	                                    }
	                                    var recordEndosoAltaAsegurados = -1;
	                                    var records = _p34_storeEndosos.getRange();
	                                    for (var i = 0; i < records.length; i++) {
	                                        var record = records[i];
	                                        if (Number(record.get('CDTIPSUP')) === 9) {
	                                            recordEndosoAltaAsegurados = record;
	                                            break;
	                                        }
	                                    }
	                                    if (recordEndosoAltaAsegurados === -1) {
	                                        throw 'No se encuentra el endoso de alta de asegurados para continuar';
	                                    }
	                                    var grid = _fieldById('_p34_gridEndosos');
	                                    //cellclick : function(view, td, cellIndex, record)
	                                    grid.fireEvent('cellclick', grid.getView(), null, null, recordEndosoAltaAsegurados);
	                                    // ^ Abrimos ese endosos para poliza
	                                });
	                            } catch (e) {
	                                manejaException(e, ck);
	                            }
	                        }
	                    );
	                } catch (e) {
	                    manejaException(e, ck);
	                }
	            }
	        );
	    } catch (e) {
	        manejaException(e, ck);
	    }
    }
}

function _p34_recuperarPolizaIncisosFlujo()
{
    debug('>_p34_recuperarPolizaFlujo');
    if(!Ext.isEmpty(_p34_flujo))
    {
        debug('Si hay flujo');
            _p34_recuperarDatosPoliza(
                _p34_flujo.cdunieco
                ,_p34_flujo.cdramo
                ,_p34_flujo.estado
                ,_p34_flujo.nmpoliza
                ,function(poliza)
                {
                    debug('callback despues de poliza:',poliza,'.');
                    
                    var ck = 'Decodificando auxiliar de flujo';
                    
                    try
                    {
	                    var nivel = '';
	                    
			            /* SE COMENTA EL NIVEL PORQUE YA NO SE USA LA SEPARACION NIVEL P Y NIVEL I (poliza/inciso)
			            var flujoAuxObj = Ext.decode(_p34_flujo.aux);
			            debug('flujoAuxObj:',flujoAuxObj,'.');
			            
			            var nivel = flujoAuxObj.nivel;
			            
			            if(nivel !== 'P' && nivel !== 'I')
			            {
			                throw 'El nivel '+nivel+' no es v\u00e1lido';
			            }
			            */
			            
			            var form = _fieldById('_p34_formBusq');
			            
			            var hid = Ext.ComponentQuery.query('[hidden=true]',form);
			            
			            for(var i=0; i<hid.length ; i++)
			            {
			                hid[i].show();
			            }
			            
			            _fieldByName('cdunieco',form).setValue(Ext.isEmpty(poliza.CDUNIEXT) ? _p34_flujo.cdunieco : poliza.CDUNIEXT );
			            
			            _fieldByName('cdramo',form).setValue(_p34_flujo.cdramo);
			            
			            _fieldByName('nmpoliza',form).setValue(_p34_flujo.nmpoliza);
			            
			            _fieldByName('finicio',form).setValue('01/01/1900');
			            
			            _fieldByName('ffin',form).setValue('01/01/2500');
			            
			            if (Number(_p34_flujoAux.endosoSeleccionado) === 57) { // Para rehabilitacion
			                _fieldByName('statusVig',form).forceSelection = false;
			                _fieldByName('statusVig',form).setValue('C');
			            }
                        
                        _fieldByName('ramo',form).forceSelection = false;
                        _fieldByName('ramo',form).setValue(poliza.RAMO);
			            
			            form.hide();
			            
			            _p34_polizas(
			                function()
			                {
			                    debug('callback despues de cargar poliza desde flujo');
			                    var ck = 'Seleccionando p\u00f3liza';
			                    try
			                    {
			                        /*if(nivel === 'I' && _p34_storePolizas.getCount()==1)
			                        {
			                            _p34_soloNivelInciso = true;
			                            
			                            _p34_gridPolizasIncisosClic(
			                                _p34_storePolizas.getAt(0)
			                                ,_fieldById('_p34_gridPolizas')
			                            );
			                        }
			                        else if(nivel === 'P')
			                        {
			                            _p34_soloNivelPoliza = true;
			                        }*/
			                        
			                        if (_p34_storePolizas.getCount() > 1 && !Ext.isEmpty(poliza.NMPOLIEX)) {
			                            var polizasQuitar = [];
			                            for (var i = _p34_storePolizas.getCount() -1; i >= 0; i--) {
			                                if (_p34_storePolizas.getAt(i).get('NMPOLIEX') != poliza.NMPOLIEX) {
			                                    polizasQuitar.push(i);
			                                }
			                            }
			                            debug('polizasQuitar: ', polizasQuitar);
			                            for (var i = 0; i < polizasQuitar.length; i++) {
			                                _p34_storePolizas.removeAt(polizasQuitar[i]);
			                            }
			                        }
			                        
			                        _fieldById('_p34_gridPolizas').getSelectionModel().select([_p34_storePolizas.getAt(0)]);
			                                // ^ Seleccionamos una poliza
			                        _p34_botonEndososPolizaClic(function () { // Abrimos los endosos de poliza
			                            if (_p34_storeEndosos.getCount() > 0) { // Hay un endoso para poliza
			                                var grid = _fieldById('_p34_gridEndosos');
			                                //cellclick : function(view, td, cellIndex, record)
			                                grid.fireEvent('cellclick', grid.getView(), null, null, _p34_storeEndosos.getAt(0));
			                                        // ^ Abrimos ese endosos para poliza
			                            } else { // No hay endoso para poliza, cargamos asegurados
			                                _fieldById('_p34_windowEndosos').close(); // Cerramos los endosos de poliza
			                                _p34_gridPolizasIncisosClic( // Cargamos asegurados
                                                _p34_storePolizas.getAt(0),
                                                _fieldById('_p34_gridPolizas'),
                                                function () {
                                                    var ck = 'Validando incisos para seleccionar incisos';
                                                    try {
                                                        if (_p34_storeIncisos.getCount() === 1) { // Hay un inciso
                                                            _fieldById('_p34_gridIncisos').getSelectionModel().select([_p34_storeIncisos.getAt(0)]);
                                                                    // ^ Seleccionamos el inciso
                                                            _p34_botonEndososIncisosClic(function () { // Abrimos los endosos de ese inciso
                                                                var ck = 'Buscando endosos para inciso callback';
                                                                try {
                                                                    if (_p34_storeEndosos.getCount() > 0) { // Hay un endoso para inciso
                                                                        var grid = _fieldById('_p34_gridEndosos');
                                                                        //cellclick : function(view, td, cellIndex, record)
                                                                        grid.fireEvent('cellclick', grid.getView(), null, null, _p34_storeEndosos.getAt(0));
                                                                                // ^ Abrimos ese endoso para inciso
                                                                    } else {
                                                                        _fieldById('_p34_windowEndosos').close(); // Cerramos los endosos de inciso
                                                                        mensajeWarning('No se pudo abrir el endoso');
                                                                    }
                                                                } catch(e) {
                                                                    manejaException(e, ck);
                                                                }
                                                            });
                                                        }
                                                    } catch (e) {
                                                        manejaException(e, ck);
                                                    }
                                                }
                                            );
			                            }
			                        });
			                    }
			                    catch(e)
			                    {
			                        manejaException(e,ck);
			                    }
			                }
			            );
		            }
		            catch(e)
		            {
		                manejaException(e,ck);
		            }
                }
            );
    }
    else
    {
        debug('_p34_recuperarPolizaFlujo no hay flujo');
    }
}

//Se recupera lista de polizas
function _p34_recuperarDatosPoliza(cdunieco,cdramo,estado,nmpoliza,callback)
{
    debug('_p34_recuperarDatosPoliza arguments:',arguments,'.');
    
    var ck = 'Recuperando unidad externa';
    try
    {
        if(Ext.isEmpty(cdunieco)
            ||Ext.isEmpty(cdramo)
            ||Ext.isEmpty(estado)
            ||Ext.isEmpty(nmpoliza)
            ||Ext.isEmpty(callback)
            ||typeof callback !== 'function'
        )
        {
            throw 'Faltan par\u00e1metros para recuperar datos de p\u00f3liza';
        }
        
        _mask(ck);
        Ext.Ajax.request(
        {
            url      : _p34_urlRecuperacion
            ,params  :
            {
                'params.consulta'  : 'RECUPERAR_MPOLIZAS_POR_PARAMETROS_VARIABLES'
                ,'params.cdunieco' : cdunieco
                ,'params.cdramo'   : cdramo
                ,'params.estado'   : estado
                ,'params.nmpoliza' : nmpoliza
            }
            ,success : function(response)
            {
                _unmask();
                var ck = 'Decodificando respuesta al recuperar unidad externa';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### datos poliza:',json,'.');
                    if(json.success === true)
                    {
                        if (json.list.length === 0) {
                            throw 'No se encuentran los datos de la p\u00f3liza';
                        }
                        callback(json.list[0]);
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
                errorComunicacion(null,'Error al recuperar datos de p\u00f3liza');
            }
        })
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p34_guardarAtributoTramitePorDescripcion(ntramite,descripcion,valor,callback)
{
    debug('_p34_guardarAtributoTramitePorDescripcion args:',arguments,'.');
    var mask, ck = 'Guardando atributo de tr\u00e1mite';
    try
    {
        mask = _maskLocal(ck);
        Ext.Ajax.request(
        {
            url      : _p34_urlActualizarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : ntramite
                ,'params.dsatribu' : descripcion
                ,'params.otvalor'  : valor
                ,'params.accion'   : 'U'
            }
            ,success : function(response)
            {
                mask.close();
                var ck = 'Decodificando respuesta al guardar atributo de tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### guardar atributo tramite:',json);
                    if(json.success===true)
                    {
                        ck = 'Ejecutando callback al guardar atributo de tr\u00e1mite';
                        
                        callback();
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
                mask.close();
                errorComunicacion(null,'Error al guardar atributo de tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,mask);
    }
}

function _p34_recuperarAtributoTramitePorDescripcion(ntramite,descripcion,callback)
{
    debug('_p34_recuperarAtributoTramitePorDescripcion args:',arguments,'.');
    var mask, ck = 'Recuperando atributo de tr\u00e1mite';
    try
    {
        mask = _maskLocal(ck);
        Ext.Ajax.request(
        {
            url      : _p34_urlRecuperarOtvalorTramiteXDsatribu
            ,params  :
            {
                'params.ntramite'  : ntramite
                ,'params.dsatribu' : descripcion
            }
            ,success : function(response)
            {
                mask.close();
                var ck = 'Decodificando respuesta al recuperar atributo de tr\u00e1mite';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### recuperar atributo tramite:',json);
                    if(json.success===true)
                    {
                        ck = 'Ejecutando callback al recuperar atributo de tr\u00e1mite';
                        
                        callback(json);
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
                mask.close();
                errorComunicacion(null,'Error al recuperar atributo de tr\u00e1mite');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck,mask);
    }
}

function _p34_recuperarTipoEndosoFlujo()
{
    //endosoSeleccionado
    debug('>_p34_recuperarTipoEndosoFlujo');
    if(_p34_flujoAux.endosoSeleccionado === 'Recuperar')
    {
        debug('Voy a recuperar el tipo de endoso');
        _p34_recuperarAtributoTramitePorDescripcion(
            _p34_flujo.ntramite
            ,'CDTIPSUP'
            ,function(json)
            {
                var ck = 'Procesando tipo de endoso recuperado';
                try
                {
                    debug('json de tipo de endoso recuperado:',json);
                    _p34_flujoAux.endosoSeleccionado = json.params.otvalor;
                    
                    debug('_p34_flujoAux.endosoSeleccionado:',_p34_flujoAux.endosoSeleccionado,'.');
                    
                    _p34_recuperarPolizaIncisosFlujo();
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
        );
    }
}
////// funciones //////
</script>
</head>
<body><div id="_p34_divpri" style="height:700px;border:1px solid #CCCCCC;"></div></body>
</html>