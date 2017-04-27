<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p39_urlRecuperacionSimpleLista = '<s:url namespace="/emision"         action="recuperacionSimpleLista"           />';
var _p39_urlGuardarTvalositEndoso   = '<s:url namespace="/endosos"         action="guardarTvalositEndoso"             />';
var _p39_urlConfirmarEndoso         = '<s:url namespace="/endosos"         action="guardarEndosoAjusteSiniestralidad" />';
var _p39_urlRecuperacionSimple      = '<s:url namespace="/emision"         action="recuperacionSimple"                />';
////// urls //////

////// variables //////
var _p39_smap1  = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
var _p39_slist1 = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
var _p39_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;
debug('_p39_smap1:'  , _p39_smap1);
debug('_p39_slist1:' , _p39_slist1);
debug('_p39_flujo:'  , _p39_flujo);

var _p39_storeIncisos    = null;
var _p39_storeCoberturas = null;
////// variables //////

////// overrides //////
////// overrides //////

////// dinamicos //////
var _p39_incisoColumns    = [ <s:property value="imap.incisoColumns"    escapeHtml="false" /> ];
var _p39_coberturaColumns = [ <s:property value="imap.coberturaColumns" escapeHtml="false" /> ];

////// dinamicos //////
    
Ext.onReady(function()
{
    ////// modelos //////
    Ext.define('_p39_modeloInciso',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            //MPOLISIT
            "CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
            ,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
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
        ]
    });
    
    Ext.define('_p39_modeloCobertura',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            'CDUNIECO'
            ,'CDRAMO'
            ,'ESTADO'
            ,'NMPOLIZA'
            ,'NMSITUAC'
            ,'CDGARANT'
            ,'DSGARANT'
            ,{name :'PRIMA' , type:'float'} 
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    _p39_storeIncisos = Ext.create('Ext.data.Store',
    {
        model : '_p39_modeloInciso'
    });
    
    for(var i in _p39_slist1)
    {
    	var record = new _p39_modeloInciso(_p39_slist1[i]);
        debug('record VILS: ',i,' >>> ',record);
        _p39_storeIncisos.add(record);
        // Llama a funcion que carga en $0.00 cada inciso
        _p39_cargarCoberturasInciso(record,true);
    }
    
    debug('_p39_storeIncisos.data:',_p39_storeIncisos.data);
    
    _p39_storeCoberturas = Ext.create('Ext.data.Store',
    {
        model     : '_p39_modeloCobertura'
        ,autoLoad : false
        ,proxy    :
        {
            type         : 'ajax'
            ,url         : _p39_urlRecuperacionSimpleLista
            ,extraParams :
            {
                'smap1.procedimiento' : 'RECUPERAR_COBERTURAS_PRIMA_NETA'
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
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p39_divpri'
        ,itemId   : '_p39_panelpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.grid.Panel',
            {
                itemId     : '_p39_gridIncisos'
                ,title     : 'Incisos'
                ,minHeight : 150
                ,maxHeight : 200
                ,columns   : _p39_incisoColumns
                ,store     : _p39_storeIncisos
                ,selModel  :
                {
                    selType    : 'checkboxmodel'
                    ,mode      : 'SINGLE'
                    ,listeners :
                    {
                        selectionchange : function(me, selected, eOpts)
                        {
                            if(selected.length>0)
                            {
                                _p39_storeCoberturas.load(
                                {
                                    params    :
                                    {
                                        'smap1.cdunieco'  : selected[0].get('CDUNIECO')
                                        ,'smap1.cdramo'   : selected[0].get('CDRAMO')
                                        ,'smap1.estado'   : selected[0].get('ESTADO')
                                        ,'smap1.nmpoliza' : selected[0].get('NMPOLIZA')
                                        ,'smap1.nmsituac' : selected[0].get('NMSITUAC')
                                        ,'smap1.tstamp'   : _p39_smap1.tstamp 
                                    }
                                    ,callback : function(records,operation,success)
                                    {
                                        debug('### polizasload: ',records,operation,success);
                                        if(success&&records.length>0)
                                        {
                                        	for(i in records)
                                       		{
                                       		   debug('CDGARANT',records[i].get('CDGARANT'));
                                       		   debug('records[i].raw.PRIMA',records[i].raw.PRIMA);
                                       		   debug('records[i].raw.OTVALOR01',records[i].raw.OTVALOR01);
                                       		}
                                        }
                                        else
                                        {
                                            if(success)
                                            {
                                                mensajeWarning('Sin resultados');
                                            }
                                            else
                                            {
                                                mensajeError(operation.getError());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId     : '_p39_gridCoberturas'
                ,title     : 'Coberturas'
                ,minHeight : 150
                ,maxHeight : 300
                ,selType: 'cellmodel'
                ,plugins: [
                       Ext.create('Ext.grid.plugin.CellEditing', {
                           clicksToEdit: 1
                           , listeners: {
                                beforeedit: function(e, editor){
                                    if (editor.colIdx  == 0)
                                        return false;
                                }
                            }
                       })
                   ]
                ,columns   : _p39_coberturaColumns
                ,store     : _p39_storeCoberturas
                ,listeners: { // Para actualizar en BD el valor asigando en prima Neta a cada cobertura por inciso
                	
                	edit: function(editor, e, eOpts ) {
                		debug('Fin de edicion celda coberturas:', e.record, e.field, e.value, e.originalValue,e.rowIdx );
                		// TODO: ir a actualizar a la BD
                		if(e.value != e.originalValue) {
                			debug('se cambio el valor');
                			var valores={
                                CDUNIECO   : _p39_smap1.CDUNIECO
                                ,CDRAMO    : _p39_smap1.CDRAMO
                                ,ESTADO    : _p39_smap1.ESTADO
                                ,NMPOLIZA  : _p39_smap1.NMPOLIZA
                                ,NMSITUAC  : e.record.get('NMSITUAC')
                                ,NMSUPLEM  : '999'
                                ,STATUS    : 'V'
                                ,CDTIPSIT  : e.record.get('CDGARANT')
                                ,OTVALOR01 : e.value
                                ,tstamp    : _p39_smap1.tstamp
                            };
                			Ext.Ajax.request(
                            {
                                url       : _p39_urlGuardarTvalositEndoso
                                ,jsonData :
                                {
                                    smap1 : valores
                                }
                                ,success  : function(response)
                                {
                                    _unmask();
                                    var json=Ext.decode(response.responseText);
                                    debug('### guardar tvalosit endoso:',json);
                                    if(json.success)
                                    {
                                    	centrarVentanaInterna(Ext.MessageBox.confirm('Confirmar', 'Se guardar\u00e1 Prima Neta de la cobertura <br>¿Desea continuar?', function(btn)
                                        {
                                            if(btn === 'yes')
                                            {
                                                e.record.commit();
                                                debug('_p39_storeCoberturas',_p39_storeCoberturas);
                                            }
                                            else {
                                                debug('reverso prima original');
                                            }
                                        }));
                                    }
                                    else
                                    {
                                        mensajeError(json.respuesta);
                                    }
                                }
                                ,failure : function()
                                {
                                    _unmask();
                                    grid.getStore().rejectChanges();
                                    errorComunicacion();
                                }
                            });
                		} else {
                			debug('no hubo cambios');
                		}
                	}
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId     : '_p39_formEndoso'
                ,title     : 'Datos de endoso'
                ,defaults  : { style : 'margin:5px;' }
                ,items     :
                [
                    {
                        xtype       : 'datefield'
                        ,itemId     : '_p39_fechaCmp'
                        ,dateFormat : 'd/m/Y'
                        ,fieldLabel : 'Fecha de efecto'
                        ,value      : new Date()
                        ,allowBlank : false
                        ,name       : 'feefecto'
                    }
                ]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                         text     : 'Emitir'
                        ,itemId  : '_p39_botonConfirmar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                        ,handler : function(me)
                        { //                  
                        	debug('Inicia');
                        	debug(_p39_storeCoberturas.data);
                        	_p39_storeCoberturas.each( function (model) {
                        		debug('Inicia FOR');
                                debug(model.get('PRIMA'));
                                debug('Termina FOR');
                            });
                        	debug(me.up('form').getForm().getValues());
                        	debug('Termina')
                        	if(!me.up('form').getForm().isValid())
                            {
                                datosIncompletos();
                                return;
                            }
                            
                            var lista = [];
                            _p39_storeIncisos.each(function(record)
                            {
                                var inc =
                                {
                                    cdunieco  : record.get('CDUNIECO')
                                    ,cdramo   : record.get('CDRAMO')
                                    ,estado   : record.get('ESTADO')
                                    ,nmpoliza : record.get('NMPOLIZA')
                                    ,nmsituac : record.get('NMSITUAC')
                                    ,nmsuplem : record.get('NMSUPLEM')
                                    ,status   : record.get('STATUS')
                                    ,cdtipsit : record.get('CDTIPSIT')
                                };
                                lista.push(inc);
                            });
                            
                            me.setDisabled(true);
                            me.setText('Cargando...');
                            
                            var jsonData =
                            {
                                smap1   : _p39_smap1
                                ,smap2  : me.up('form').getForm().getValues()
                                ,slist1 : lista
                            };
                            
                            if(!Ext.isEmpty(_p39_flujo))
                            {
                                jsonData.flujo = _p39_flujo;
                            }
                            jsonData.smap1['confirmar'] = 'si';
                            jsonData.smap1['cdperpag'] = _p39_smap1.CDPERPAG;
                            jsonData.smap1['p_plan']   = _p39_smap1.CDMEJRED;
                            Ext.Ajax.request(
                            {
                                url       : _p39_urlConfirmarEndoso
                                ,jsonData : jsonData
                                ,success  : function(response)
                                {
                                    var json=Ext.decode(response.responseText);
                                    debug('### confirmar:',json);
                                    if(json.success)
                                    {
                                        var callbackRemesa = function()
                                        {
                                            marendNavegacion(2);
                                        };
                                        mensajeCorrecto('Endoso generado','Endoso generado',function()
                                        {
                                            _generarRemesaClic(
                                                true
                                                ,_p39_smap1.CDUNIECO
                                                ,_p39_smap1.CDRAMO
                                                ,_p39_smap1.ESTADO
                                                ,_p39_smap1.NMPOLIZA
                                                ,callbackRemesa
                                            );
                                        });
                                    }
                                    else
                                    {
                                        mensajeError(json.respuesta);
                                    }
                                }
                                ,failure  : function()
                                {
                                    me.setDisabled(false);
                                    me.setText('Confirmar');
                                    errorComunicacion();
                                }
                              });
                        }
                    }
                ]
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    ////// custom //////
    
    ////// loaders //////
    
    Ext.Ajax.request(
    {
        url      : _p39_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : _p39_smap1.CDUNIECO
            ,'smap1.cdramo'       : _p39_smap1.CDRAMO
            ,'smap1.estado'       : _p39_smap1.ESTADO
            ,'smap1.nmpoliza'     : _p39_smap1.NMPOLIZA
            ,'smap1.cdtipsup'     : _p39_smap1.cdtipsup
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldById('_p39_fechaCmp').setMinValue(json.smap1.FECHA_MINIMA);
                _fieldById('_p39_fechaCmp').setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldById('_p39_fechaCmp').setReadOnly(json.smap1.EDITABLE=='N');
                _fieldById('_p39_fechaCmp').isValid();
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
    ////// loaders //////
});

////// funciones //////
function _p39_renderer(valor,mapeo,view)
{
    debug('_p39_renderer valor,mapeo,view.id:',valor,mapeo,view.id);
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

//Funcion que carga coberturas y seteando y guardando valores en BD
function _p39_cargarCoberturasInciso(record)
{   //alert('ENTRE V');
    _mask();
    debug('valores a enviar VIL :', record,'.');
	Ext.Ajax.request
	({ 
        url    : _p39_urlRecuperacionSimpleLista
        ,params :
        {
       	    'smap1.tstamp' : _p39_smap1.tstamp
         	 ,'smap1.cdunieco' : record.data['CDUNIECO']
         	 ,'smap1.cdramo' : record.data['CDRAMO']
         	 ,'smap1.estado' : record.data['ESTADO']
         	 ,'smap1.nmpoliza' : record.data['NMPOLIZA']
         	 ,'smap1.nmsituac' : record.data['NMSITUAC']
             ,'smap1.procedimiento' : 'RECUPERAR_COBERTURAS_PRIMA_NETA'
         }
         ,success : function(response)
         {
            var json = Ext.decode(response.responseText);
            if(json.success == true)
            {
         	    debug('valores DEVUELTOS VILS :', json);
         	    for(var i in json.slist1)
         	    {	
          		    _p39_seteaSyGuardar(json.slist1[i]);
         	    }
            }
            else{debugError('error al recuperar coberturas de inciso:',json.respuesta);}
         }
         ,failure  : function()
         {   
        	 _unmask();
             debugError('error de comunicacion al cargar coberturas de iniso');
         }
    });    
}

//Se utiliza una sola vez seteando S en valores inicales
function _p39_seteaSyGuardar(record)
{
	_mask();
    debug('>_p39_seteaSyGuardar ',record,'');
    var valores={
        tstamp : _p39_smap1.tstamp
    };
    for(var key in record)
    {
        var value=record[key];
        debug('value');
        debug(typeof value,key,value);
        if((typeof value=='object')&&value&&value.getDate)
        {
            var fecha='';
            fecha+=value.getDate();
            debug('Fecha',fecha);
            if((fecha+'x').length==2)//1x
            {
                fecha = ('x'+fecha).replace('x','0');//x1=01
            }
            fecha+='/   ';
            fecha+=value.getMonth()+1<10?
                   (('x'+(value.getMonth()+1)).replace('x','0'))
                   :(value.getMonth()+1);
            fecha+='/';
            fecha+=value.getFullYear();
            value=fecha;
        }
        valores[key]=value;
    }
    valores['NMSUPLEM']  = '999';
    valores['STATUS']    = 'V';
    valores['CDTIPSIT']  = record.CDGARANT;
    valores['OTVALOR01'] = '0.00';
    debug('valores a enviar:',valores);
    
    guardar(valores);

    debug('<_p39_seteaSyGuardar');
}

//Prepara valores para guardar
function _p39_guardarCambio(record,callback,i)
{
    debug('>_p39_guardarCambio record.data,!callback?,i',record.data,Ext.isEmpty(callback),i);
    var valores={
        tstamp : _p39_smap1.tstamp
    };
    for(var key in record.data)
    {
        var value=record.data[key];
        debug(typeof value,key,value);
        if((typeof value=='object')&&value&&value.getDate)
        {
            var fecha='';
            fecha+=value.getDate();
            if((fecha+'x').length==2)//1x
            {
                fecha = ('x'+fecha).replace('x','0');//x1=01
            }
            fecha+='/   ';
            fecha+=value.getMonth()+1<10?
                   (('x'+(value.getMonth()+1)).replace('x','0'))
                   :(value.getMonth()+1);
            fecha+='/';
            fecha+=value.getFullYear();
            value=fecha;
        }
        valores[key]=value;
    }
    valores['NMSUPLEM']  = '999';
    valores['STATUS']    = 'V';
    valores['CDTIPSIT']  = record.get('CDGARANT');
    valores['OTVALOR01'] = record.get('PRIMA');
    debug('valores a enviar:',valores);
    
    guardar(valores);

    debug('<_p39_guardarCambio');
}

//Funcion que guarda valores por cobertura
function guardar(valores)
{
	_mask();
	debug('<guardar');
    Ext.Ajax.request(
    {
        url       : _p39_urlGuardarTvalositEndoso
        ,jsonData :
        {
            smap1 : valores
        }
        ,success  : function(response)
        {
        	_unmask();
            var json=Ext.decode(response.responseText);
            debug('### guardar tvalosit endoso:',json);
            if(json.success)
            {
                _p39_storeCoberturas.commitChanges();
                debug(_p39_storeCoberturas);
            }
            else
            {
                _p39_storeCoberturas.rejectChanges();
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
        	_unmask();
            grid.getStore().rejectChanges();
            errorComunicacion();
        }
    });
    debug('>guardar');
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p39_divpri" style="height:500px;border:1px solid #999999;"></div></body>
</html>