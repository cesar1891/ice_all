<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p48_urlRecuperacion             = '<s:url namespace="/recuperacion" action="recuperar"                         />';
var _p48_urlRecuperarItemsFormulario = '<s:url namespace="/endosos"      action="recuperarComponentesAltaAsegurado" />';
var _p48_urlConfirmarEndoso          = '<s:url namespace="/endosos"      action="confirmarEndosoFamilias"           />';
var _p48_urlMovimientos              = '<s:url namespace="/movimientos"  action="ejecutar"                          />';
var _p48_urlMovimientosSMD           = '<s:url namespace="/movimientos"  action="ejecutarSMD"                       />';
var _p48_urlAgregarFamilia           = '<s:url namespace="/emision"  	 action="includes/cotizacionGrupo"          />';
var _p48_urlInforFamiliaEndoso       = '<s:url namespace="/endosos"      action="obtieneInfoFamiliaEndoso"          />';
var _p48_urlObtieneCatalogos         = '<s:url namespace="/catalogos"    action="obtieneCatalogo"                   />';
var _p21_urlBorrarRespaldoCenso      = '<s:url namespace="/emision"      action="borrarRespaldoCenso"              />';
var _p21_urlRestaurarRespaldoCenso   = '<s:url namespace="/emision"      action="restaurarRespaldoCenso"           />';
////// urls //////

////// variables //////
var _p48_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p48_params:',_p48_params);

var _p48_store;
var _p48_storeRespaldo;
var _p48_storeMov;
var _p48_nfamilia = 0;
var _p48_storeFamilia;

var _p48_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p48_flujo:',_p48_flujo);
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var _p48_itemsPoliza  = [ <s:property value="items.itemsPoliza"  escapeHtml="false" /> ];
var _p48_fieldsInciso = [ <s:property value="items.fieldsInciso" escapeHtml="false" /> ];
var _p48_colsInciso   = [ <s:property value="items.colsInciso"   escapeHtml="false" /> ];
var _p48_colsMovimi   = [
                            {
                                xtype    : 'actioncolumn'
                                ,width   : 50
                                ,hidden  : _p48_params.operacion!='baja'
                                ,items   :
                                [
                                    {
                                        icon     : '${icons}arrow_undo.png'
                                        ,tooltip : 'Deshacer'
                                        ,handler : _p48_deshacerMov
                                    }
                                    ,{
                                        icon     : '${icons}pencil.png'
                                        ,tooltip : 'Editar'
                                        ,handler : _p48_editarAsegurado
                                    }
                                ]
                            }
                            ,{
                                text       : 'MOV.'
                                ,width     : 70
                                ,dataIndex : 'MOV'
                               	,hidden    : _p48_params.operacion!='baja'
                                ,renderer  : function(v)
                                {
                                    if(v=='-')
                                    {
                                        return '<span style="font-size:10px;">Quitar</span>';
                                    }
                                    if(v=='+')
                                    {
                                        return '<span style="font-size:10px;">Agregar</span>';
                                    }
                                    return v;
                                }
                            }
                            ,<s:property value="items.colsInciso"   escapeHtml="false" />
                        ];
var _p48_itemsEndoso = [ <s:property value="items.itemsEndoso" escapeHtml="false" /> ];
var _p48_comboGrupos = <s:property value="items.comboGrupos" escapeHtml="false" />;
////// componentes dinamicos //////

Ext.onReady(function()
{
	Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    ////// modelos //////
    Ext.define('_p48_modelo',
    {
        extend  : 'Ext.data.Model'
        ,fields : _p48_fieldsInciso
    });
    ////// modelos //////
    
    ////// stores //////
    debug("VALOR DEL CDTIPSUP ===> ",+_p48_params.cdtipsup);
   	if(+_p48_params.cdtipsup > 9){
	    _p48_store = Ext.create('Ext.data.Store',
	    {
	        autoLoad  : false
	        ,model    : '_p48_modelo'
	        ,pageSize : 25
	        ,proxy    :
	        {
	        	startParam   : 'params.start'
	        	,limitParam  : 'params.limit'
	        	,pageParam   : 'params.page'
	            ,type        : 'ajax'
	            ,url         : _p48_urlRecuperacion
	            ,callbackKey : 'callback'
	            ,extraParams :
	            {
	                'params.consulta'  : 'RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA'
	                ,'params.cdunieco' : _p48_params.CDUNIECO
	                ,'params.cdramo'   : _p48_params.CDRAMO
	                ,'params.estado'   : _p48_params.ESTADO
	                ,'params.nmpoliza' : _p48_params.NMPOLIZA
	            }
	            ,reader :
	            {
	                type             : 'json'
	                ,root            : 'list'
	                ,successProperty : 'success'
	                ,messageProperty : 'message'
	            }
	            ,simpleSortMode: true
	        }
	    });
	    _p48_cargarStore();	
   	}
    
    _p48_storeMov = Ext.create('Ext.data.Store',
    {
        autoLoad  : false
        ,model    : '_p48_modelo'
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p48_urlRecuperacion
            ,callbackKey : 'callback'
            ,extraParams :
            {
                'params.consulta'  : 'RECUPERAR_MOVIMIENTOS_ENDOSO_ALTA_BAJA_ASEGURADO'
                ,'params.cdunieco' : _p48_params.CDUNIECO
                ,'params.cdramo'   : _p48_params.CDRAMO
                ,'params.estado'   : _p48_params.ESTADO
                ,'params.nmpoliza' : _p48_params.NMPOLIZA
                ,'params.nmsuplem' : _p48_params.nmsuplem_endoso
            }
            ,reader :
            {
                type             : 'json'
                ,root            : 'list'
                ,successProperty : 'success'
                ,messageProperty : 'message'
            }
            ,call: function()
            {
               debug('call');
            }
            ,simpleSortMode: true
        }
    });
    if(!Ext.isEmpty(_p48_params.nmsuplem_endoso))
    {
        _p48_cargarStoreMov();
    }
    
     _p48_storeFamilia = Ext.create('Ext.data.Store',
	    {
	        autoLoad  : false
	        ,model    : '_p48_modelo'
	        ,proxy    :
	        {
	            type         : 'ajax'
	            ,url         : _p48_urlRecuperacion
	            ,callbackKey : 'callback'
	            ,extraParams :
	            {
	                'params.consulta'  : 'RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA'
	                ,'params.cdunieco' : _p48_params.CDUNIECO
	                ,'params.cdramo'   : _p48_params.CDRAMO
	                ,'params.estado'   : _p48_params.ESTADO
	                ,'params.nmpoliza' : _p48_params.NMPOLIZA
//	                ,'params.nmfamili' : _p48_params.NMSITAUX
	            }
	            ,reader :
	            {
	                type             : 'json'
	                ,root            : 'list'
	                ,successProperty : 'success'
	                ,messageProperty : 'message'
	            }
	            ,call: function()
	            {
	               debug('call');
	            }
	            ,simpleSortMode: true
	        }
	    });
	   
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
	    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p48_divpri'
        ,itemId   : '_p48_panelpri'
        ,defaults : { style : 'margin:5px;' }
        ,border   : 0
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                title     : 'DATOS DE P\u00D3LIZA'
                ,defaults : { style : 'margin:5px;' } 
                ,layout   : 'hbox'
                ,items    : _p48_itemsPoliza
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title     : 'ASEGURADOS'
                ,itemId   : '_p48_gridAsegurados'
                ,columns  : _p48_colsInciso
                ,store    : _p48_store
                ,height   : 265
                ,selModel :
                {
                    selType        : 'checkboxmodel'
                    ,allowDeselect : true
                    ,mode          : 'SINGLE'
                }
                ,tbar     :
                [
                    {
                        text     : 'Agregar asegurados'
                        ,icon    : '${icons}add.png'
                        ,hidden  : _p48_params.operacion!='alta'||Ext.isEmpty(_p48_params.TIPOFLOT)||_p48_params.TIPOFLOT=='I'
                        ,handler : _p48_agregarFamClic
                    }
                    ,{
                        text     : 'Quitar'
                        ,icon    : '${icons}delete.png'
                        ,hidden  : _p48_params.operacion!='baja'
                        ,handler : _p48_quitarAseguradoClic
                    }
                    ,'->'
                    ,Ext.create('Ext.form.ComboBox',
                    {
                        name        : 'dsatribu'
                        ,allowBlank : false
                        ,style:'margin:5px'
                        ,typeAhead:true
                        ,anyMatch:true
                        ,displayField:'value'
                        ,hidden  : _p48_params.operacion!='baja'
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
                        	    ,url:_p48_urlObtieneCatalogos
                        	    ,reader:
                        	    {
                        	        type:'json'
                        	        ,root:'lista'
                        	        ,rootProperty:'lista'
                        	    }
                        	    ,extraParams:
                        	    {
                        	        catalogo           : 'RECUPERAR_LISTA_FILTRO_PROPIEDAD_INCISO'
                        	        ,'params.cdramo'   :  _p48_params.CDRAMO
                        	        ,'params.cdtipsit' :  _p48_params.CDTIPSIT
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
                        name       : 'txtBuscar',
                        hidden     : _p48_params.operacion!='baja'
                    }
                    ,{
                        xtype    : 'button'
                        ,text    : 'Buscar'
                        ,hidden  : _p48_params.operacion!='baja'
                        //,icon  : panDocContexto+'/resources/fam3icons/icons/add.png'
                        ,handler : function(btn) {
                        	debug('Buscando...');				                                	
                        	if(Ext.isEmpty(btn.up('toolbar').down('textfield[name=txtBuscar]').getValue())){
                            	debug('valor nulo...');
                            	btn.up('toolbar').down('combo[name=dsatribu]').setValue('NOMBRE ASEGURADO');
                            }				                                	
                            _p48_store.getProxy().setExtraParam('params.dsatribu',btn.up('toolbar').down('combo[name=dsatribu]').getValue());
                            _p48_store.getProxy().setExtraParam('params.otvalor', btn.up('toolbar').down('textfield[name=txtBuscar]').getValue());
                            _p48_store.loadPage(1);
                        }
					}
                ]
                ,bbar: Ext.create('Ext.PagingToolbar', {
		            store: _p48_store, //Funcionalidad en la barra de paginado.
		            displayInfo: true
		        })
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title    : 'MOVIMIENTOS'
                ,itemId  : '_p48_gridMovimientos'
                ,columns : _p48_colsMovimi
                ,store   : _p48_storeMov
                ,height  : 200
            })
            ,Ext.create('Ext.form.Panel',
            {
                title        : 'DATOS DEL ENDOSO'
                ,itemId      : '_p48_formEndoso'
                ,defaults    : { style : 'margin : 5px;' }
                ,items       : _p48_itemsEndoso
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Confirmar'
                        ,icon    : '${icons}key.png'
                        ,handler : function(me)
                        {
                            Ext.MessageBox.confirm('Confirmar', '¿Desea confirmar el endoso?', function(btn)
                            {
                                if(btn === 'yes')
                                {
                                    var ck = 'Confirmando endoso';
                                    try
                                    {
                                        if(_p48_storeMov.getCount()==0)
                                        {
                                           
                                        	throw 'No hay cambios';
                                        }
                                        
                                        if(!_fieldById('_p48_formEndoso').isValid())
                                        {
                                            throw 'Revisar datos del endoso';
                                        }
                                       	
                                        if(+_p48_params.cdtipsup > 9){
                                        	var cdtipsitPrimerInc = _p48_store.getAt(0).get('CDTIPSIT');
                                        }else{
                                        	var cdtipsitPrimerInc = _p48_params.CDTIPSIT;
                                        }
                                        var datos =
                                        {
                                            params : 
                                            {
                                                cdunieco              : _p48_params.CDUNIECO
                                                ,cdramo               : _p48_params.CDRAMO
                                                ,estado               : _p48_params.ESTADO
                                                ,nmpoliza             : _p48_params.NMPOLIZA
                                                ,cdtipsup             : _p48_params.cdtipsup
                                                ,nmsuplem             : _p48_params.nmsuplem_endoso
                                                ,nsuplogi             : _p48_params.nsuplogi
                                                ,fecha                : Ext.Date.format(_fieldByName('FEFECHA').getValue(),'d/m/Y')
                                                ,cdtipsitPrimerInciso : cdtipsitPrimerInc
                                                ,nmsolici             : _p48_params.NMSOLICI
                                            }
                                            ,list : []
                                        };
                                        
                                        _p48_storeMov.each(function(record)
                                        {
                                            datos.list.push({nmsituac:record.get('NMSITUAC')});
                                        });
                                        
                                        if(!Ext.isEmpty(_p48_flujo))
                                        {
                                            datos.flujo = _p48_flujo;
                                        }
                                        
                                        debug('datos para confirmar:',datos);
                                        
                                        _setLoading(true,_fieldById('_p48_panelpri'));
                                        
                                        Ext.Ajax.request(
                                        {
                                            url       : _p48_urlConfirmarEndoso
                                            ,jsonData : datos
                                            ,success  : function(response)
                                            {
                                                _setLoading(false,_fieldById('_p48_panelpri'));
                                                var ck = 'Decodificando respuesta al confirmar endoso';
                                                try
                                                {
                                                    var json = Ext.decode(response.responseText);
                                                    debug('### confirmar:',json);
                                                    if(json.success)
                                                    {
                                                        var callbackRemesa = function()
                                                        {
                                                            marendNavegacion(2);
                                                        };
                                                        mensajeCorrecto('Endoso generado',json.message,function()
                                                        {
                                                        	var cadena= json.message;
                                                        	var palabra="guardado";
                                                        	if (cadena.indexOf(palabra)==-1){
                                                        		_generarRemesaClic2(
                                                                        false
                                                                        ,_p48_params.CDUNIECO
                                                                        ,_p48_params.CDRAMO
                                                                        ,_p48_params.ESTADO
                                                                        ,_p48_params.NMPOLIZA
                                                                        ,callbackRemesa
                                                                    );
                                                        	}else{
                                                        		_generarRemesaClic(
                                                                        true
                                                                        ,_p48_params.CDUNIECO
                                                                        ,_p48_params.CDRAMO
                                                                        ,_p48_params.ESTADO
                                                                        ,_p48_params.NMPOLIZA
                                                                        ,callbackRemesa
                                                                    );
                                                        	}
                                                        });
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
                                                _setLoading(false,_fieldById('_p48_panelpri'));
                                                errorComunicacion();
                                            }
                                        });
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }
                                }
                            });
                        } 
                    }
                    ,{
                        text     : 'Cancelar y borrar endoso'
                        ,itemId  : '_p48_botonCancelar'
                        ,icon    : '${icons}cancel.png'
                        ,hidden  : true
                        ,handler : _p48_cancelarEndosoClic
                    }, {
                        text    : 'Imprimir',
                        icon    : '${ctx}/resources/fam3icons/icons/printer.png',
                        handler : function () {
                            centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                title       : 'Documentos',
                                modal       : true,
                                buttonAlign : 'center',
                                width       : 600,
                                height      : 400,
                                autoScroll  : true,
                                cls         : 'VENTANA_DOCUMENTOS_CLASS',
                                loader      : {
                                    url    : _GLOBAL_COMP_URL_VENTANA_DOCS,
                                    params : {
                                        'smap1.cdunieco' : _p48_params.CDUNIECO,
                                        'smap1.cdramo'   : _p48_params.CDRAMO,
                                        'smap1.estado'   : _p48_params.ESTADO,
                                        'smap1.nmpoliza' : _p48_params.NMPOLIZA,
                                        'smap1.nmsuplem' : '0',
                                        'smap1.ntramite' : _p48_params.NTRAMITE || (!Ext.isEmpty(_p48_flujo) ? _p48_flujo.ntramite : ''),
                                        'smap1.nmsolici' : '',
                                        'smap1.tipomov'  : '0'
                                    },
                                    scripts  : true,
                                    autoLoad : true
                                }
                            }).show());
                        }
                    }, {
                        text    : 'Regresar a mesa de control',
                        icon    : '${icons}house.png',
                        handler : function (me) {
                            _iceMesaControl();
                        }
                    }
                ]
                ,listeners :
                {
                    afterrender : function(me)
                    {
                        _p48_validarEstadoBotonCancelar();
                       	if(+_p48_params.cdtipsup > 9){
                        	_p48_store.loadPage(1);
                        }
                        var ck = 'Limitando fecha de endoso';
                        try
                        {
                            me.down('[name=FEFECHA]').minValue = Ext.Date.parse(_p48_params.FEEFECTO,'d/m/Y');
                            me.down('[name=FEFECHA]').maxValue = Ext.Date.parse(_p48_params.FEPROREN,'d/m/Y');
                        }
                        catch(e)
                        {
                            manejaException(e);
                        }
                    }
                }
            })
        ]
    });
    
    
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    ////// loaders //////
});

////// funciones //////
function _p48_cargarStore()
{
    _p48_store.load(function(records,op,success)
    {
        if(!success)
        {
            mensajeError('Error al recuperar asegurados: '+op.getError());
        }
    });
}

function _p48_cargarStoreMov()
{
    _p48_storeMov.load(function(records,op,success)
    {
        if(!success)
        {
            mensajeError('Error al recuperar movimientos: '+op.getError());
        }
    });
}

function _p48_agregarAseguradoMov(recordsQueSeQuitan, record) {
    //Agrega al asegurado a un grid donde se encuentren todos los asegurados 
	
	try {
	
        _p48_storeMov.each(function(record2) {
            if(Number(record2.get('NMSITUAC'))==Number(record.get('NMSITUAC'))) {
                throw 'Este inciso ya se encuentra en los movimientos';
            }
        });
        
        debug('recordsQueSeQuitan:',recordsQueSeQuitan);
        
        var quitados = 0;
        _setLoading(true,'_p48_gridAsegurados');
        debug('Longitud =>',recordsQueSeQuitan.length);
        for(var i in recordsQueSeQuitan)
        {
            var datos           = parseaFechas(recordsQueSeQuitan[i].data);
            datos['FEPROREN']   = _p48_params.FEPROREN;
            datos['cdtipsup']   = _p48_params.cdtipsup;
            datos['movimiento'] = 'PASO_QUITAR_ASEGURADO';
            datos['sleep']      = i*300;
            debug('datos:',datos);
            Ext.Ajax.request(
            {
                url       : _p48_urlMovimientosSMD 
                ,jsonData : { params : datos }
                ,success  : function(response)
                {
                    var ck = 'Decodificando respuesta al quitar asegurado';
                    try
                    {
                        quitados = quitados + 1;
                        debug('Longitud =>',recordsQueSeQuitan.length);
                        if(quitados==recordsQueSeQuitan.length)
                        {
                            _setLoading(false,'_p48_gridAsegurados');
                        }
                        var json = Ext.decode(response.responseText);
                        debug('### quitar:',json);
                        if(json.success==true)
                        {
                            if(quitados==recordsQueSeQuitan.length)
                            {
                                _p48_params.nmsuplem_endoso = json.params.nmsuplem_endoso;
                                _p48_params.nsuplogi        = json.params.nsuplogi;
                                debug('_p48_params:',_p48_params);
                                _p48_storeMov.proxy.extraParams['params.nmsuplem'] = _p48_params.nmsuplem_endoso;
                                _p48_cargarStoreMov();
                                mensajeCorrecto('Movimiento guardado','Se ha guardado el movimiento');
                                
                                _p48_validarEstadoBotonCancelar();
                            }
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
                    quitados = quitados + 1;
                    if(quitados==recordsQueSeQuitan.length)
                    {
                        _setLoading(false,'_p48_gridAsegurados');
                    }
                    errorComunicacion(null,'Error al quitar asegurado');
                }
            });
        }
        
    } catch(e) {
        manejaException(e);
    }
}

function _p48_quitarAseguradoClic(me)
{
    debug('>_p48_quitarAseguradoClic');
    var ck = 'Quitando asegurado';
    
    try
    {
        var gridAsegurados = _fieldById('_p48_gridAsegurados');
        if(gridAsegurados.getSelectionModel().getSelection().length==0)
        {
            throw 'Seleccione un asegurado';
        }
        
        var recordsQueSeQuitan = [];
        var record = gridAsegurados.getSelectionModel().getSelection()[0];
        
        debug('Parentesco => ',record.get('CVE_PARENTESCO'));

        if(record.get('CVE_PARENTESCO') != 'T' ){
        	//Agrega a un asegurado que no sea Titular
        	recordsQueSeQuitan.push(record);
        	
        	 _p48_agregarAseguradoMov(recordsQueSeQuitan, record);
        }else{
        
		    _p48_storeFamilia.getProxy().setExtraParam('params.nmfamili', record.get('NMSITAUX'));
		    
		    //Agrega a una Familia, cuando el parentesco sea Titular
		    _p48_storeFamilia.load(function(records,op,success)
		    {
		    	var fieldValue
		        
		        debug('Redors: ',_p48_storeFamilia.getCount());
		        
				for(q=0;q < _p48_storeFamilia.getCount();q++){
					fieldValue = _p48_storeFamilia.getAt(q);
					debug('Records: ',_p48_storeFamilia.getAt(q));
					
					recordsQueSeQuitan.push(fieldValue);
					debug('recordsQueSeQuitan:',recordsQueSeQuitan);
				}
		    	debug('record:',record);
			    debug('_p48_storeFamilia ',_p48_storeFamilia);
			    
			    _p48_agregarAseguradoMov(recordsQueSeQuitan, record);
		        
		    });
        }
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p48_deshacerMov(v,row,col,item,e,record)
{
    debug('_p48_deshacerMov record.raw:',record.raw);
    var ck = 'Revirtiendo movimiento';
    try
    {
        if(record.get('MOV')=='-')
        {
            //deshacer "QUITAR"
            //buscamos si hay titular de la famlia en los movimientos
            var elTitularEstaEnMovimientos = false;
            for(var i=0;i<_p48_storeMov.getCount();i++)
            {
                var rec = _p48_storeMov.getAt(i);
                if(Number(rec.get('NMSITAUX'))==Number(record.get('NMSITAUX'))
                    &&rec.get('CVE_PARENTESCO')=='T'
                )
                {
                    elTitularEstaEnMovimientos = true;
                    break;
                }
            }
            debug('elTitularEstaEnMovimientos:',elTitularEstaEnMovimientos);
            var recordsParaDeshacerMov = [];
            if(elTitularEstaEnMovimientos)
            {
                var familia = Number(record.get('NMSITAUX'));
                _p48_storeMov.each(function(rec)
                {
                    if(Number(rec.get('NMSITAUX'))==familia)
                    {
                        recordsParaDeshacerMov.push(rec);
                    }
                });
            }
            else
            {
                recordsParaDeshacerMov.push(record);
            }
            
            debug('recordsParaDeshacerMov:',recordsParaDeshacerMov);
            
            var revertidos = 0;
            _setLoading(true,'_p48_panelpri');
            for(var i=0;i<recordsParaDeshacerMov.length;i++)
            {
                var datos = parseaFechas(recordsParaDeshacerMov[i].data);
                datos['movimiento']      = 'DESHACER_PASO_ASEGURADO';
                datos['nmsuplem_endoso'] = _p48_params.nmsuplem_endoso;
                datos['cdtipsup']        = _p48_params.cdtipsup;
                Ext.Ajax.request(
                {
                    url       : _p48_urlMovimientosSMD
                    ,jsonData : { params : datos }
                    ,success  : function(response)
                    {
                        var ck = 'Decodificando respuesta al revertir movimiento';
                        try
                        {
                            revertidos = revertidos + 1;
                            if(revertidos==recordsParaDeshacerMov.length)
                            {
                                _setLoading(false,'_p48_panelpri');
                            }
                            var json = Ext.decode(response.responseText);
	                        debug('### revertir:',json);
	                        if(json.success==true)
	                        {
	                            if(revertidos==recordsParaDeshacerMov.length)
	                            {
	                                mensajeCorrecto('Movimiento(s) revertido(s)','Se ha(n) revertido el(los) movimiento(s)');
	                                _p48_cargarStoreMov();
	                            }
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
                    ,failure  : function()
                    {
                        revertidos = revertidos + 1;
                        if(revertidos==recordsParaDeshacerMov.length)
                        {
                            _setLoading(false,'_p48_panelpri');
                        }
                        errorComunicacion(null,'Error al revertir movimiento');
                    }
                });
            }
        }
        else if(record.get('MOV')=='+')
        {
            if(record.get('AUX1')=='DEP')
            {
                _p48_storeMov.remove(record);
            }
            else if(record.get('AUX1')=='FAM')
            {
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    title        : 'Quitar asegurado/familia'
                    ,modal       : true
                    ,_p34_window : 'si'
                    ,html        : '<div style="padding:5px;">¿Desea quitar el asegurado o la familia?</div>'
                    ,buttonAlign : 'center'
                    ,buttons     :
                    [
                        {
                            text     : 'Asegurado'
                            ,icon    : '${icons}user_delete.png'
                            ,handler : function(me)
                            {
                                var ck = 'Revirtiendo asegurado';
                                try
                                {
                                    var paren = record.get('CVE_PARENTESCO');
                                    debug('validando parenteso:',paren);
                                    if(paren=='T')
                                    {
                                        throw 'No se puede quitar el titular';
                                    }
                                    _p48_storeMov.remove(record);
                                    me.up('window').destroy();
                                }
                                catch(e)
                                {
                                    manejaException(e,ck);
                                }
                            }
                        }
                        ,{
                            text  : 'Familia'
                            ,icon : '${icons}group_delete.png'
                            ,handler : function(me)
                            {
                                var ck = 'Revirtiendo familia';
                                try
                                {
                                    var familia = record.get('NMSITAUX');
                                    debug('familia a quitar:',familia);
                                    for(var i=_p48_storeMov.getCount()-1;i>=0;i--)
                                    {
                                        if(_p48_storeMov.getAt(i).get('NMSITAUX')==familia)
                                        {
                                            _p48_storeMov.remove(_p48_storeMov.getAt(i));
                                        }
                                    }
                                    me.up('window').destroy();
                                }
                                catch(e)
                                {
                                    manejaException(e,ck);
                                }
                            }
                        }
                    ]
                }).show());
            }
        }
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p48_validarEstadoBotonCancelar()
{
    debug('_p48_validarEstadoBotonCancelar');
    try
    {
        var bot = _fieldById('_p48_botonCancelar');
        if(Ext.isEmpty(_p48_params.nmsuplem_endoso))
        {
            bot.hide();
        }
        else
        {
            bot.show();
        }
    }
    catch(e)
    {
        debugError(e);
        mensajeError('Error al validar estado de endoso');
    }
}

function _p48_agregarFamClic()
{
	debug("_p48_params ==>",_p48_params, _p48_params.cdtipsup,_p48_params.FEPROREN);
    var datos =
    {
        
            CDUNIECO              : _p48_params.CDUNIECO
            ,CDRAMO               : _p48_params.CDRAMO
            ,ESTADO               : _p48_params.ESTADO
            ,NMPOLIZA             : _p48_params.NMPOLIZA
            ,cdtipsup             : _p48_params.cdtipsup
            ,FEPROREN             : _p48_params.FEPROREN
            ,FEEFECTO             : _p48_params.FEEFECTO
            ,movimiento           : 'AGREGAR_FAMILIA'
    };
    
    debug('datos:',datos);
    
    Ext.Ajax.request(
    {
        url       : _p48_urlMovimientosSMD 			//1.- 
        ,jsonData : { params : datos }
        ,success  : function(response)
        {
            var ck = 'Decodificando respuesta al quitar asegurado';
            try
            {
                var json = Ext.decode(response.responseText);
                debug('### quitar:',json);
                if(json.success==true)
                {
                	_p48_validarEstadoBotonCancelar();
                	 _p48_params.nmsuplem_endoso = json.params.nmsuplem_endoso;
                     _p48_params.nsuplogi        = json.params.nsuplogi;
                     debug('_p48_params:',_p48_params);
                     _p48_storeMov.proxy.extraParams['params.nmsuplem'] = _p48_params.nmsuplem_endoso;
                     
                     //Llamar aun procedcer para sabe si ya se subio un documento en 
                    
                    var submitValues={};
							params = {
									'cdunieco'    :  _p48_params.CDUNIECO,
									'cdramo'      :  _p48_params.CDRAMO,
									'estado'      :  _p48_params.ESTADO,
									'nmpoliza'    :  _p48_params.NMPOLIZA,
			                        'nmsuplem'    :  _p48_params.nmsuplem_endoso,
			                        'ntramite'    :  _p48_params.NTRAMITE
							}
							submitValues['smap1']= params;
							
                    Ext.Ajax.request( {
						url    : _p48_urlInforFamiliaEndoso
						,jsonData: Ext.encode(submitValues)
						,success : function (response) {
							var jsonRes = Ext.decode(response.responseText).slist1[0];
							
							var windowHistSinies = Ext.create('Ext.window.Window',{
								name        : 'panelbusqueda',
		                 		modal       : true,
		                 		buttonAlign : 'center',
		                 		_p34_window : 'si',
		                 		width       : 1000,
		                 		height      : 700,
		                 		autoScroll  : true,
		                 		closeAction : 'hide',
		                 		loader      : {
		                 			url     : _p48_urlAgregarFamilia,
		                 			params  : {
		                 				'smap1.cdunieco'  : _p48_params.CDUNIECO
		                                ,'smap1.cdramo'   : _p48_params.CDRAMO
		                                ,'smap1.cdtipsit' : jsonRes.CDTIPSIT
		                                ,'smap1.estado'   : _p48_params.ESTADO
		                                ,'smap1.nmpoliza' : _p48_params.NMPOLIZA
		                                ,'smap1.ntramite' : _p48_params.NTRAMITE
		                                ,'smap1.cdagente' : jsonRes.CDAGENTE
		                                ,'smap1.status'   : jsonRes.EXISTEREGISTRO == 0?"18":"19"
		                                ,'smap1.sincenso' : "N"
		                                ,'smap1.nmsuplem' : _p48_params.nmsuplem_endoso
		                                ,'smap1.prodGenerado' : jsonRes.PRODUCTO
		                 			},
		                 			scripts  : true,
		                 			loadMask : true,
		                 			autoLoad : true,
		                 			ajaxOptions: {
		                 				method: 'POST'
		                 			}
		                 		},
		                 		listeners:{
	        						 close:function(){
	        							 if(true){
	        								windowHistSinies.destroy();
	        							 }
	        						 }
	        					}
		                 	}).show();
							
		                 	centrarVentana(windowHistSinies);
						},
						failure : function (){
							Ext.Msg.show({
								title:'Error',
								msg: 'Error de comunicaci&oacute;n',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR
							});
						}
                    });
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
            quitados = quitados + 1;
            if(quitados==recordsQueSeQuitan.length)
            {
                _setLoading(false,'_p48_gridAsegurados');
            }
            errorComunicacion(null,'Error al quitar asegurado');
        }
    });
}

function _p48_crearFormulario(mpersona,tatrisit,tatrirol)
{
    debug('>_p48_crearFormulario');
    return Ext.create('Ext.form.Panel',
    {
        //title     : 'ASEGURADO',
        defaults : { style : 'margin:5px;' }
        ,width    : 850
        ,items    :
        [
            {
                xtype   : 'fieldset'
                ,title  : '<span style="font:bold 14px Calibri;">DATOS DE PERSONA</span>'
                ,items  : mpersona
                ,layout :
                {
                    type     : 'table'
                    ,columns : 3
                }
            }
            ,{
                xtype   : 'fieldset'
                ,title  : '<span style="font:bold 14px Calibri;">DATOS DE P\u00D3LIZA</span>'
                ,items  : tatrisit
                ,layout :
                {
                    type     : 'table'
                    ,columns : 3
                }
            }/*
            ,{
                xtype   : 'fieldset'
                ,title  : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES</span>'
                ,items  : tatrirol
                ,layout :
                {
                    type     : 'table'
                    ,columns : 3
                }
            }*/
        ]
    });
}

function _p48_obtenerComponentes(depFam,cdtipsit,callback,cmpLoading)
{
    var ck = 'Recuperando componentes';
    try
    {
        if(Ext.isEmpty(cdtipsit))
        {
            throw 'Falta cdtipsit';
        }
        if(Ext.isEmpty(callback))
        {
            throw 'Falta callback';
        }
        
        _setLoading(true,cmpLoading||_fieldById('_p48_gridAsegurados'));
        Ext.Ajax.request(
        {
            url      : _p48_urlRecuperarItemsFormulario
            ,params  :
            {
                'params.cdramo'    : _p48_params.CDRAMO
                ,'params.cdtipsit' : cdtipsit
                ,'params.depFam'   : depFam
            }
            ,success : function(response)
            {
                _setLoading(false,cmpLoading||_fieldById('_p48_gridAsegurados'));
                var ck = 'Decodificando formularios';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### elementos:',json);
                    if(json.success==true)
                    {
                        var mpersona    = Ext.decode(json.params.mpersona);
                        var tatrisit    = Ext.decode(json.params.tatrisit);
                        var tatrirol    = Ext.decode(json.params.tatrirol);
                        var botonValDep = Ext.decode(json.params.validacion);
                        debug('mpersona:'    , mpersona);
                        debug('tatrisit:'    , tatrisit);
                        debug('tatrirol:'    , tatrirol);
                        debug('botonValDep:' , botonValDep.handler);
                        callback(mpersona,tatrisit,tatrirol,botonValDep.handler);
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
                _setLoading(false,cmpLoading||_fieldById('_p48_gridAsegurados'));
                errorComunicacion(null,'Error al construir formularios');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _p48_editarAsegurado(v,row,col,item,e,record)
{
    debug('_p48_editarAsegurado record.data:',record.data);
    var ck = 'Validando registro';
    try
    {
        if(record.get('AUX1')=='DEP'||record.get('AUX1')=='FAM')
        {
            _p48_obtenerComponentes('DEP',record.get('CDTIPSIT'),function(mpersona,tatrisit,tatrirol,validacion)
	        {
	            centrarVentanaInterna(Ext.create('Ext.window.Window',
	            {
	                title  : 'EDITAR ASEGURADO'
	                ,modal : true
	                ,_p34_window : 'si'
	                ,items :
	                [
	                    Ext.create('Ext.form.Panel',
	                    {
	                        defaults : { style : 'margin:5px;' }
	                        ,autoScroll : true
	                        ,border  : 0
	                        ,width   : 600
	                        ,height  : 400
	                        ,items   :
	                        [
	                            {
	                                xtype  : 'fieldset'
	                                ,title : '<span style="font:bold 14px Calibri;">DATOS DE PERSONA</span>'
	                                ,items : mpersona
	                                ,layout  :
	                                {
	                                    type     : 'table'
	                                    ,columns : 2
	                                }
	                            }
	                            ,{
	                                xtype  : 'fieldset'
	                                ,title : '<span style="font:bold 14px Calibri;">DATOS DE P\u00D3LIZA</span>'
	                                ,items : tatrisit
	                                ,layout  :
	                                {
	                                    type     : 'table'
	                                    ,columns : 2
	                                }
	                            }
	                            ,{
	                                xtype  : 'fieldset'
	                                ,title : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES</span>'
	                                ,items : tatrirol
	                                ,layout  :
	                                {
	                                    type     : 'table'
	                                    ,columns : 2
	                                }
	                            }
	                        ]
	                        ,listeners   :
	                        {
	                            afterrender : function(me)
	                            {
	                                me.down('[name=CVE_PARENTESCO]').setReadOnly(true);
	                                me.down('[name=DSGRUPO]').allowBlank = true;
	                                me.down('[name=DSGRUPO]').hide();
	                                me.down('[name=NMSITUAEXT]').allowBlank = true;
                                    me.down('[name=NMSITUAEXT]').hide();
	                                _cargarForm(me,record.data);
	                            }
	                        }
	                        ,buttonAlign : 'center'
	                        ,buttons     :
	                        [
	                            {
	                                text     : 'Aceptar'
	                                ,icon    : '${icons}accept.png'
	                                ,handler : function(me)
	                                {
	                                    var ck = 'Guardando asegurado';
	                                    try
	                                    {
	                                        var form = me.up('form');
	                                        if(!form.isValid())
	                                        {
	                                            throw 'Favor de verificar datos';
	                                        }
	                                        
	                                        var valores = form.getValues();
	                                        debug('valores:',valores);
	                                        
	                                        ck = 'Ejecutando validaci\u00F3n din\u00E1mica';
	                                        validacion(valores,true);
	                                        
	                                        for(var att in valores)
	                                        {
	                                            record.set(att,valores[att]);
	                                        }
	                                        form.up('window').destroy();
	                                    }
	                                    catch(e)
	                                    {
	                                        manejaException(e,ck);
	                                    }
	                                }
	                            }
	                        ]
	                    })
	                ]
	            }).show());
	        });
        }
    }
    catch(e)
    {
        manejaException(e);
    }
}

function _p48_cancelarEndosoClic(button)
{
    Ext.MessageBox.confirm('Confirmar', '¿Desea cancelar y borrar los cambios del endoso?', function(btn)
    {
        if(btn === 'yes')
        {
            _setLoading(true,'_p48_panelpri');
            Ext.Ajax.request(
            {
                url      : _p48_urlMovimientos
                ,params  :
                {
                    'params.movimiento' : 'SACAENDOSO'
                    ,'params.cdunieco'  : _p48_params.CDUNIECO
                    ,'params.cdramo'    : _p48_params.CDRAMO
                    ,'params.estado'    : _p48_params.ESTADO
                    ,'params.nmpoliza'  : _p48_params.NMPOLIZA
                    ,'params.nsuplogi'  : _p48_params.nsuplogi
                    ,'params.nmsuplem'  : _p48_params.nmsuplem_endoso
                }
                ,success : function(response)
                {
                    _setLoading(false,'_p48_panelpri');
                    var ck = 'Decodificando respuesta de cancelar endoso';
                    try
                    {
                        var json = Ext.decode(response.responseText);
                        debug('### cancelar:',json);
                        if(json.success==true)
                        {
                            mensajeCorrecto('Endoso revertido','Endoso revertido');
                            _setLoading(true,'_p48_panelpri');
                            marendNavegacion(2);
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
                    _setLoading(false,'_p48_panelpri');
                    errorComunicacion(null,'Error al cancelar endoso');
                }
            });
        }
    });
}

function _p48_rendererGrupos(val)
{
    //voy a buscar el valor val en el store del combo de grupos
    //cuando no este cargando, regreso "cargando...", y agrego listener, (una sola vez)
    //cuando si este cargado, regreso el valor, o "error"
    if(_p48_comboGrupos.getStore().getCount()>0)
    {
        var recordGrupo = _p48_comboGrupos.findRecordByValue(val);
        if(recordGrupo!=false)
        {
            val = recordGrupo.get('value');
        }
        else
        {
            val = '-Error-';
        }
    }
    else
    {
        debug('_p48_rendererGrupos else');
        val = 'Cargando...';
        if(Ext.isEmpty(_p48_comboGrupos.getStore().tieneListener))
        {
            _p48_comboGrupos.getStore().tieneListener = true;
            _p48_comboGrupos.getStore().on(
            {
                load : function(me,records,success)
                {
                    if(success)
                    {
                        _fieldById('_p48_gridAsegurados').getView().refresh();
                    }
                }
            });
        }
    }
    debug('_p48_rendererGrupos return:',val);
    return val;
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<script src="${ctx}/js/proceso/emision/funcionesCotizacionGrupo.js?now=${now}"></script>
</head>
<body>
<div id="_p48_divpri" style="height:500px;border:1px solid #999999"></div>
</body>
</html>