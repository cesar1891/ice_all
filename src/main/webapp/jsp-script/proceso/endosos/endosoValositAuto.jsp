<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p36_urlConfirmarEndoso        = '<s:url namespace="/endosos" action="confirmarEndosoTvalositAuto" />';
var _p36_previewConfirmarEndoso    = '<s:url namespace="/endosos" action="previewEndosoTvalositAuto" />';
var _p36_urlRecuperacionSimple     = '<s:url namespace="/emision" action="recuperacionSimple"          />';
var _p29_urlObtieneValNumeroSerie  = '<s:url namespace="/emision"    action="obtieneValNumeroSerie"    />';
////// urls //////

////// variables //////
var _p36_smap1  = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
var _p36_slist1 = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
var _p36_flujo  = <s:property value="%{convertToJSON('flujo')}"  escapeHtml="false" />;

debug('_p36_smap1:'  , _p36_smap1);
debug('_p36_slist1:' , _p36_slist1);
debug('_p36_flujo:'  , _p36_flujo);

var _p36_store;
var numSerie = '';
////// variables //////

////// overrides //////
////// overrides //////

////// dinamicos //////
var _p39_gridColumnsEditables =
[
    <s:property value="imap.gridColumnsEditables" escapeHtml="false" />
];
var _p36_gridColumns =
[
     <s:property value="imap.gridColumnsLectura"   escapeHtml="false" />
];
for(var i in _p39_gridColumnsEditables)
{
	_p36_gridColumns.push(_p39_gridColumnsEditables[i]);
}

var _p36_itemsEdicion=[];
for(var i in _p36_gridColumns)
{
    var col=_p36_gridColumns[i];
    if(!Ext.isEmpty(col.editor)
        &&col.hidden==true
    )
    {
        col.editor.fieldLabel = col.text;
        //col.editor.width      = 300;
        col.editor.allowBlank = false;
        _p36_itemsEdicion.push(col.editor);
    }
}
////// dinamicos //////

Ext.onReady(function()
{
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
    ////// modelos //////
    Ext.define('_p36_modeloInciso',
    {
        extend  : 'Ext.data.Model'
        ,fields : []
    });
    
    var arrAtributos = [
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
    ];
    
    debug('keys:', Ext.Object.getKeys(_p36_slist1[0]));
    Ext.each(Ext.Object.getKeys(_p36_slist1[0]), function(name) {
        arrAtributos.push(name);
    });
    Ext.ModelManager.getModel('_p36_modeloInciso').setFields(arrAtributos);
    ////// modelos //////
    
    ////// stores //////
    _p36_store=Ext.create('Ext.data.Store',
    {
        model : '_p36_modeloInciso'
        ,data : []
    });
    for(var i in _p36_slist1)
    {
    	var modeloInciso = new _p36_modeloInciso(_p36_slist1[i]);
        _p36_store.add(modeloInciso);
        debug('modeloInciso data=', modeloInciso.data);
    }
    _p36_store.commitChanges();
    debug('_p36_store:',_p36_store);
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p36_panelpri'
        ,renderTo : '_p36_divpri'
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                itemId  : '_p36_form'
                ,title  : 'Edici&oacute;n'
                ,hidden : _p36_itemsEdicion.length==0
                ,items  : _p36_itemsEdicion
            })
            ,Ext.create('Ext.grid.Panel',
            {
                itemId   : '_p36_grid'
                ,title   : 'Incisos'
                ,height  : 300
                ,plugins :
                _p36_itemsEdicion.length==0 ?
                [
                    Ext.create('Ext.grid.plugin.CellEditing',
                    {
                        clicksToEdit  : 1
                        ,errorSummary : false
                        ,listeners    :
                        {
                            validateedit: function(editor, ctx, eOpts) {
                            	_fieldById('_p36_botonConfirmar').disable();
                            	try {
                                    if(_p36_smap1.TIPO_VALIDACION == 'MENOR') {
                                        // Si el nuevo valor es menor o igual al original, mostramos warning:
                                    	debug('comparando', Number(ctx.record.raw[ctx.field]), '<=', Number(ctx.value));
                                        if( Number(ctx.record.raw[ctx.field]) <= Number(ctx.value) ) {
                                            mensajeWarning("El nuevo valor debe ser menor");
                                            _fieldById('_p36_botonConfirmar').enable();
                                            return false;
                                        }
                                    } else if(_p36_smap1.TIPO_VALIDACION == 'MAYOR') {
                                        // Si el nuevo valor es mayor o igual al original, mostramos warning:
                                        debug('comparando', Number(ctx.record.raw[ctx.field]), '>=', Number(ctx.value));
                                        if( Number(ctx.record.raw[ctx.field]) >= Number(ctx.value) ) {
                                            mensajeWarning("El nuevo valor debe ser mayor");
                                            _fieldById('_p36_botonConfirmar').enable();
                                            return false;
                                        }
                                    }
                            	} catch(e) {
                            		debugError("Error al aplicar la validacion de campos", e);
                            	}
                            },
                            beforeedit: function() {
                                _fieldById('_p36_botonConfirmar').disable();
                            },
                            edit: function() {
                                _fieldById('_p36_botonConfirmar').enable();
                            },
                            canceledit: function() {
                                _fieldById('_p36_botonConfirmar').enable();
                            }
                        }
                    })
                ] : []
                ,columns : _p36_gridColumns
                ,store   : _p36_store
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
                                ,itemId     : '_p36_fechaCmp'
                                ,fieldLabel : 'Fecha de efecto'
                                ,value      : _p36_smap1.FEEFECTO
                                ,allowBlank : false
                            }
                            ,{
                                xtype    : 'button'
		                        ,text    : 'Confirmar'
		                        ,itemId  : '_p36_botonConfirmar'
		                        ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
		                        ,handler : function()
		                        {
		                            var panel    = _fieldById('_p36_botonConfirmar');
		                            var callback = function()
		                            {
		                                // Generar objeto para la confirmacion:
		                                var jsonDatosConfirmacion = {
		                                    smap1: {
		                                        'cdtipsup'  : _p36_smap1.cdtipsup
		                                        ,'tstamp'   : _p36_smap1.tstamp
		                                        ,'cdunieco' : _p36_store.getAt(0).get('CDUNIECO')
		                                        ,'cdramo'   : _p36_store.getAt(0).get('CDRAMO')
		                                        ,'estado'   : _p36_store.getAt(0).get('ESTADO')
		                                        ,'nmpoliza' : _p36_store.getAt(0).get('NMPOLIZA')
		                                        ,'feefecto' : Ext.Date.format(_fieldById('_p36_fechaCmp').getValue(),'d/m/Y')
		                                    },
		                                    slist1 : []
		                                };
		                                _p36_store.each(function(record){
		                                    var valores = {};
		                                    
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
		                                            fecha+='/';
		                                            fecha+=value.getMonth()+1<10?
		                                                   (('x'+(value.getMonth()+1)).replace('x','0'))
		                                                   :(value.getMonth()+1);
		                                            fecha+='/';
		                                            fecha+=value.getFullYear();
		                                            value=fecha;
		                                        }
		                                       
		                                        valores[key]=value;
		                                        for(var i in _p39_gridColumnsEditables)
		                                        {
		                                            var col      = _p39_gridColumnsEditables[i];
		                                            var name     = col.editor.name;
		                                            var cdatribu = 90+col.editor.orden;
		                                            debug('name:',name,'cdatribu:',cdatribu);
		                                            debug('metemos:',record.get(name));
		                                            valores['OTVALOR'+cdatribu]=record.get(name);
		                                            debug('metimos:',valores['OTVALOR'+cdatribu]);
		                                            
		                                        }
		                                        //numSerie+=valores['OTVALOR99'];
		                                    }
		                                    jsonDatosConfirmacion.slist1.push(valores);
		                                    
		                                });
		                                
		                                var panelMask = new Ext.LoadMask('_p36_divpri', {msg:"Confirmando..."});
										panelMask.show();
										
										if(!Ext.isEmpty(_p36_flujo))
										{
										    jsonDatosConfirmacion.flujo = _p36_flujo;
										}
										_p36_store.each(function(record) {
											
											try{
											    if(_p36_smap1.CDRAMO==Ramo.ServicioPublico){
											    	debug( record.get('DES_NUMERO_DE_SERIE'));
											    	numSerie=(record.get('DES_NUMERO_DE_SERIE'));
											    }else{
											        debug( record.get('CVE_NUMERO_DE_SERIE'));
	                                                numSerie=(record.get('CVE_NUMERO_DE_SERIE'));
											    }
											}catch(e){
												debug( record.get('CVE_NUMERO_DE_SERIE'));
                                                numSerie=(record.get('CVE_NUMERO_DE_SERIE'));
												debugError(e);
											}
									    });
										debug('jsonDatosConfirmacion****',jsonDatosConfirmacion.slist1['OTVALOR99']);
										//numSerie+=''+(record.get('parametros.pv_otvalor37'))+'|';
										function confirmaEmitir(Confirmacion){
											Ext.Ajax.request(
												{
													url     : _p36_urlConfirmarEndoso
													,jsonData: Confirmacion 
													,success : function(response)
													{
														_p36_store.commitChanges();
														panelMask.hide();
														var json = Ext.decode(response.responseText);
														debug('### confirmar endoso:',json);
														if(json.success)
														{
															mensajeCorrecto('Endoso generado','Endoso generado',function()
																	{
																		var callbackRemesa = function()
																		{
																			marendNavegacion(2);
																		};
																		_generarRemesaClic(
																			true
																			,_p36_smap1.CDUNIECO
																			,_p36_smap1.CDRAMO
																			,_p36_smap1.ESTADO
																			,_p36_smap1.NMPOLIZA
																			,callbackRemesa
																		);
																	});
															
									
														}
														else
														{
															mensajeError(json.respuesta);
														}
													}
													,failure : function(response)
													{
														panelMask.hide();
														errorComunicacion();
													}
												});
										}
										if(_p36_smap1.cdtipsup =='42'){
											debug('_p36_smap1.cdtipsup',_p36_smap1.cdtipsup);
											Ext.Ajax.request({
			                                	url     : _p29_urlObtieneValNumeroSerie,
			                                	params :{
												'smap1.numSerie'  : numSerie
												,'smap1.feini'    : new Date()
												},
												success : function(response){
													var jsonNumSerie=Ext.decode(response.responseText);
								        	    	if(jsonNumSerie.exito!=true){
								        	    		if(!RolSistema.puedeSuscribirAutos(_GLOBAL_CDSISROL)){
								        	    			mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", jsonNumSerie.respuesta);
										        	    } else {
										        	    	centrarVentanaInterna(Ext.MessageBox.confirm(
																'Confirmar'
																,'¿Desea Continuar?'
																,function(btn){
																	if(btn === 'yes') {
																		confirmaEmitir(jsonDatosConfirmacion);
																	} else {
																		_p36_store.each(function(record) {
																	    	debug( record.get('CVE_NUMERO_DE_SERIE'));
																	    	record.set('CVE_NUMERO_DE_SERIE',record.get('DES_NUMERO_DE_SERIE'));
																	    });
																		panelMask.hide();
																		debug('no quiso Continuar');
																	}
																}
															));
										        			mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", jsonNumSerie.respuesta);
										        		}
	       											}else{
	       												confirmaEmitir(jsonDatosConfirmacion);
	       											}
												},
												failure : errorComunicacion
											});
										} else {
											debug('_p36_smap1.cdtipsup',_p36_smap1.cdtipsup);
											confirmaEmitir(jsonDatosConfirmacion);
										}
		                                
		                            };
		                            
		                            var valido = _fieldById('_p36_form').getForm().isValid()&&_fieldById('_p36_formEndoso').getForm().isValid();
		                            if(!valido)
		                            {
		                                datosIncompletos();
		                            }
		                            
		                            if(valido)
		                            {
		                                if(_p36_itemsEdicion.length==0)
		                                {
		                                    callback();
		                                }
		                                else
		                                {
		                                    _p36_store.each(function(record)
		                                    {
		                                        var values=_fieldById('_p36_form').getForm().getValues();
		                                        for(var att in values)
		                                        {
		                                            record.set(att,values[att]);
		                                        }
		                                    });
		                                    callback();
		                                }
		                            }
		                        }
		                    }
                        ]
                    }
                    ,'->'
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
        url      : _p36_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : _p36_smap1.CDUNIECO
            ,'smap1.cdramo'       : _p36_smap1.CDRAMO
            ,'smap1.estado'       : _p36_smap1.ESTADO
            ,'smap1.nmpoliza'     : _p36_smap1.NMPOLIZA
            ,'smap1.cdtipsup'     : _p36_smap1.cdtipsup
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldById('_p36_fechaCmp').setMinValue(json.smap1.FECHA_MINIMA);
                _fieldById('_p36_fechaCmp').setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldById('_p36_fechaCmp').setValue(json.smap1.FECHA_REFERENCIA);
                _fieldById('_p36_fechaCmp').setReadOnly(json.smap1.EDITABLE=='N');
                _fieldById('_p36_fechaCmp').isValid();
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

////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p36_divpri" style="height:330px;border:1px solid #999999;"></div></body>
</html>