<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
////// urls //////
////// urls //////

////// variables //////
//var json;
var store;
var win;
var mesConUrlDocu                   = '<s:url namespace="/documentos"  action="ventanaDocumentosPoliza"     />';
var mesConUrlComGrupo               = '<s:url namespace="/emision"     action="cotizacionGrupo"             />';
var mesConUrlComGrupo2              = '<s:url namespace="/emision"     action="cotizacionGrupo2"            />';
var _p100_urlComplementoClonacion   = '<s:url namespace="/endosos"     action="subirCensoClonacion"            />';
var _p100_smap1                     = <s:property value = '%{convertToJSON("params")}' escapeHtml="false" />;
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////
var itemsFormulario = [<s:property value="imap1.itemsFormulario" escapeHtml="false" />];
var itemsGrid = [<s:property value="imap1.itemsGrid" escapeHtml="false" />];
// var itemsInsert = [<s:property value="items.itemsInsert" escapeHtml="false" />];
var itemsGridModel = [<s:property value="imap1.itemsGridModel" escapeHtml="false" />];

itemsGrid.push({xtype : 'actioncolumn'
				,width:50
		        ,icon : '${icons}page_copy.png'
		        ,tooltip : 'clonacion total'
		        ,handler : function(itemsGrid, rowIndex)
                    {   
		        	_mask('Procesando...');
		        	  var values = store.getAt(rowIndex).getData();	
  		        	  debug("rec",values);
  		        	  
		            	   Ext.Ajax.request({
                            url     : '<s:url namespace="/endosos" action="generarCopiaCompleta" />'
                            ,params :
                            {
                                'params.cdunieco'    : values.cdunieco
                                ,'params.cdramo'     : json.slist1[rowIndex].cdramo
                                ,'params.cdtipsit'   : values.cdtipsit
                                ,'params.estado' 	 : values.estado
                                ,'params.nmpoliza' 	 : values.nmpoliza
                                ,'params.nmsolici' 	 : values.nmsolici
                                ,'params.ntramite' 	 : values.ntramite
                                ,'params.status' 	 : values.status
                                ,'params.ferecepc' 	 : Ext.Date.format(values.ferecepc,'d/m/Y')
                                ,'params.fecstatus'  : Ext.Date.format(values.fecstatus,'d/m/Y')
                                ,'params.TIPOFLOT'   : json.slist1[rowIndex].tipoflot
                            }
                            ,success : function(response)
                            {
                                _unmask();
                                var json = Ext.decode(response.responseText);
                                debug("json",json);
                                if(json.success==true){
                                    mensajeCorrecto('Tramite',json.mensaje,function(){                                    		
                                    Ext.create('Ext.window.Window',
                                    	    {
                                    	        title        : 'Documentaci&oacute;n'
                                    	        ,modal       : true
                                    	        ,buttonAlign : 'center'
                                    	        ,width       : 600
                                    	        ,height      : 400
                                    	        ,autoScroll  : true
                                    	        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                    	        ,loader      :
                                    	        {
                                    	            url       : mesConUrlDocu
                                    	            ,params   :
                                    	            {
                                    	                'smap1.nmpoliza'  : '0'
                                    	                ,'smap1.cdunieco' : json.params.cdunieco
                                    	                ,'smap1.cdramo'   : json.params.cdramo
                                    	                ,'smap1.estado'   : json.params.estado
                                    	                ,'smap1.nmsuplem' : '0'
                                    	                ,'smap1.ntramite' : json.params.ntramite
                                    	                ,'smap1.nmsolici' : '0'
                                    	                ,'smap1.tipomov'  : '0'
                                    	            }
                                    	            ,scripts  : true
                                    	            ,autoLoad : true
                                    	        }
                                    	        ,buttons : [
                                    	            {
                                    	            	text     : 'continuar'
                                    	            	,icon    : '${icons}accept.png'
                                    	            	,handler : function(){
                                    	            		debug('json.params', json.params);
                                    	            		if(json.params.redireccion == 'S'){
							                                	if(values.cdtipsit == 'MSC')
							                                	{
							                                		debug('entro al if de values cdtipsit MSC');
							                                        Ext.create('Ext.form.Panel').submit(
							                                        {
							                                            url             : mesConUrlComGrupo
							                                            ,standardSubmit : true
							                                            ,params         :
							                                            {
							                                                'smap1.cdunieco'  : json.params.cdunieco
							                                                ,'smap1.cdramo'   : json.params.cdramo
							                                                ,'smap1.cdtipsit' : values.cdtipsit
							                                                ,'smap1.estado'   : 'W'
							                                                ,'smap1.nmpoliza' : json.params.nmpoliza
							                                                ,'smap1.ntramite' : json.params.ntramite
							                                                ,'smap1.cdagente' : values.cdagente
							                                                ,'smap1.status'   : json.params.statusNuevo
							                                                ,'smap1.sincenso' : 'N'
							                                            }
							                                        });
							                                    }
							                                    else
							                                    {
							                                    	debug('entro al if de values cdtipsit');
							                                        Ext.create('Ext.form.Panel').submit(
							                                        {
							                                            url             : mesConUrlComGrupo2
							                                			,standardSubmit : true
							                                			,params         :
							                                            {
							                                				'smap1.cdunieco'  : json.params.cdunieco
							                                				,'smap1.cdramo'   : json.params.cdramo
							                                				,'smap1.cdtipsit' : values.cdtipsit
							                                				,'smap1.estado'   : 'W'
							                                				,'smap1.nmpoliza' : json.params.nmpoliza
							                                				,'smap1.ntramite' : json.params.ntramite
							                                				,'smap1.cdagente' : values.cdagente
							                                				,'smap1.status'   : json.params.statusNuevo
							                                				,'smap1.sincenso' : 'N'
							                                             }
							                                        });
							                                   }
                                    	            		}else{
                                    	            			this.up('window').close();
                                    	            		}
                                    	            	}
                                    	            }
                                    	        ]
                                    	    }).show();
									});
                                }
                                else
                                {
                                    mensajeError('Error al guardar',json.message);
                                }
                            }
                            ,failure : function()
                            {
                                _unmask();
                                errorComunicacion(null,'Error de red al guardar');
                            }
                        });
                    }
			    });
			    
itemsGrid.push(
    {
        xtype : 'actioncolumn'
        ,width:50
        ,icon : '${icons}page_white_copy.png'
        ,tooltip : 'clonacion de condiciones'
        ,handler : function(itemsGrid, rowIndex)
        {
            var values = store.getAt(rowIndex).getData();
            debug("rec",values);
            debug("entrando al boton");
			Ext.Ajax.request({
			    url     : '<s:url namespace="/endosos" action="clonarPolizaCenso" />'
			    ,params :
				{
					'params.cdunieco'    : values.cdunieco
					,'params.cdramo'     : json.slist1[rowIndex].cdramo
					,'params.cdtipsit'   : values.cdtipsit
					,'params.estado' 	 : values.estado
					,'params.nmpoliza' 	 : values.nmpoliza
					,'params.nmsolici' 	 : values.nmsolici
					,'params.ntramite' 	 : values.ntramite
					,'params.status' 	 : values.status
					,'params.ferecepc' 	 : Ext.Date.format(values.ferecepc,'d/m/Y')
					,'params.fecstatus'  : Ext.Date.format(values.fecstatus,'d/m/Y')
					,'params.TIPOFLOT'   : json.slist1[rowIndex].tipoflot
				}
				,success : function(response)
				{
					_unmask();
					var json = Ext.decode(response.responseText);
					debug("respondio poliza clonacion censo");
					debug("json",json);
					if(json.success==true){
						/* debug("params",json.params); */
			            centrarVentanaInterna(
			                Ext.create('Ext.window.Window',
			                {
			                    title        : 'Clonaci&oacute;n de tr&aacute;mites'
			                    ,modal       : true
								,itemId      : 'censoWin'
			                    ,buttonAlign : 'center'
			                    ,autoScroll  : true
			                    ,items       : [
			                        Ext.create('Ext.form.Panel',
			                        {
			                            title     : 'Subir censo'
			                            ,url      : _p100_urlComplementoClonacion
			                            ,layout   :
			                            {
			                                type     : 'table'
			                                ,columns : 3
			                            }
			                            ,defaults :
			                            {
			                                style : 'margin:5px;'
			                            }
			                            ,items : [
			                            {
			                                xtype     : 'fieldset'
											,title    : '<span style="font:bold 14px Calibri;">CENSO</span>'
											,defaults : { style : 'margin:5px;' }
											//,hidden : _p21_ntramite&&_p21_smap1.sincenso!='S' ? true : false
											,items    :
											[
/* 			                                            {
			                                                xtype        : 'fieldcontainer'
			                                                ,fieldLabel  : 'Tipo de censo'
			                                                ,defaultType : 'radiofield'
			                                                ,defaults    : { style : 'margin : 5px;' }
			                                                ,layout      : 'hbox'
			                                                ,items       :
			                                                [
			                                                    {
			                                                        boxLabel    : 'Por asegurado'
			                                                        ,name       : 'tipoCenso'
			                                                        ,inputValue : 'solo'
			                                                        ,checked    : true
			                                                    }
			                                                    ,{
			                                                        boxLabel    : 'Agrupado por edad'
			                                                        ,name       : 'tipoCenso'
			                                                        ,inputValue : 'grupo'
			                                                    }
			                                                ]
			                                            } 
			                                            ,*/
			                                {
			                                    xtype       : 'filefield'
												,fieldLabel : 'Censo de asegurados'
												,name       : 'censo'
												,buttonText : 'Examinar...'
												,allowBlank : false
												,buttonOnly : false
												,width      : 450
												,cAccept    : ['xls','xlsx']
												,msgTarget  : 'side'
												,listeners  :
			                                    {
			                                        change : function(me)
			                                        {
			                                            var indexofPeriod = me.getValue().lastIndexOf("."),
			                                            uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
			                                            if (!Ext.Array.contains(this.cAccept, uploadedExtension))
			                                            {
			                                                centrarVentanaInterna(Ext.MessageBox.show(
			                                                {
			                                                                title   : 'Error de tipo de archivo',
			                                                                msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
			                                                                buttons : Ext.Msg.OK,
			                                                                icon    : Ext.Msg.WARNING
			                                                            }));
			                                                            me.reset();
			                                                        }
			                                                    }
			                                    }
			                                }
										    ]
			                            }
			                            ]
										,buttons : [
										{
										    text  : 'Continuar'
					                        ,handler : function(me)
					                        {
					                            debug('>complemento clonacion button click');
					                            debug(values.cdunieco);
					                            debug(json.params);
					                            debug(json.params.cdramo);
					                            debug(values.estado);
					                            debug(values.nmpoliza);
					                            debug(values.cdunieco);
					                            debug(values.cdramo);
					                            debug(json.params.nmpolizaNueva);
					                            debug(values.cdtipsit);					                            
					                            var form = me.up('form');					                            
					                            var params =
					                            {					                                
					                                'params.cduniecoOrig'  : values.cdunieco
					                                ,'params.cdramoOrig'   : json.params.cdramo
					                                ,'params.estadoOrig'   : values.estado
					                                ,'params.nmpolizaOrig' : values.nmpoliza
					                                ,'params.cdunieco'     : values.cdunieco
					                                ,'params.cdramo'       : json.params.cdramo
					                                ,'params.estado'       : 'W'
					                                ,'params.nmpoliza'     : json.params.nmpolizaNueva
					                                ,'params.cdtipsit'     : values.cdtipsit
					                            };					                            
					                            
					                            if(form.isValid())
					                            {
					                                form.setLoading(true);
					                                form.submit(
					                                {
					                                    params   : params
					                                    ,success : function(form2,action)
					                                    {															
 					                                        form.setLoading(false);
					                                        var ck = 'Procesando respuesta al subir complemento';
					                                        try
					                                        {					                                        	
					                                            var json2 = Ext.decode(action.response.responseText);
					                                            debug('### submit:',json2);
					                                            if(json2.success)
					                                            {					                                                
					                                                debug('### json2 exito:',json2);
					                                                var despues = function()
					                                                {
						                                                debug('### despues');
						                                                var numRand      = Math.floor((Math.random() * 100000) + 1);
						                                                var nombreModelo = '_modelo'+numRand;
						                                                var fields  = [];
						                                                var columns = [];
						                                                debug('### var despues:');
						                                                if(Number(json2.params.filasProcesadas)>0)
						                                                {
						                                                    var record = json2.slist1[0];
						                                                    debug('record:',record);
						                                                    for(var att in record)
						                                                    {
						                                                        if(att.substring(0,1)=='_')
						                                                        {
						                                                            var col =
						                                                            {
						                                                                dataIndex : att.substring(att.lastIndexOf('_')+1)
						                                                                ,text     : record[att]
						                                                                ,orden    : ''+att
						                                                            };
						                                                            columns.push(col);
						                                                        }
						                                                        else
						                                                        {
						                                                            fields.push(att);
						                                                        }
						                                                    }
						                                                }
						                                                
						                                                for(var i=0;i<columns.length-1;i++)
						                                                {
						                                                    for(var j=i+1;j<columns.length;j++)
						                                                    {
						                                                        if(columns[i].orden>columns[j].orden)
						                                                        {
						                                                            var aux    = columns[i];
						                                                            columns[i] = columns[j];
						                                                            columns[j] = aux;
						                                                        }
						                                                    }
						                                                }
						                                                
						                                                debug('fields:',fields,'columns:',columns);
						                                                
						                                                Ext.define(nombreModelo,
						                                                {
						                                                    extend  : 'Ext.data.Model'
						                                                    ,fields : fields
						                                                });
						                                                
						                                                var store = Ext.create('Ext.data.Store',
						                                                {
						                                                    model : nombreModelo
						                                                    ,data : json2.slist1
						                                                });
						                                                
						                                                debug('store.getRange():',store.getRange());
						                                                
						                                                centrarVentanaInterna(Ext.create('Ext.window.Window',
						                                                {
						                                                    width     : 600
						                                                    ,height   : 500
																			,itemId   : 'asegWin'
						                                                    ,title    : 'Revisar asegurados del complemento'
						                                                    ,closable : false
						                                                    ,items    :
						                                                    [
						                                                        Ext.create('Ext.panel.Panel',
						                                                        {
						                                                            layout    : 'hbox'
						                                                            ,border   : 0
						                                                            ,defaults : { style : 'margin:5px;' }
						                                                            ,height   : 40
						                                                            ,items    :
						                                                            [
						                                                                {
						                                                                    xtype       : 'displayfield'
						                                                                    ,fieldLabel : 'Filas leidas'
						                                                                    ,value      : json2.params.filasLeidas
						                                                                }
						                                                                ,{
						                                                                    xtype       : 'displayfield'
						                                                                    ,fieldLabel : 'Filas procesadas'
						                                                                    ,value      : json2.params.filasProcesadas
						                                                                }
						                                                                ,{
						                                                                    xtype       : 'displayfield'
						                                                                    ,fieldLabel : 'Filas con error'
						                                                                    ,value      : json2.params.filasErrores
						                                                                }
						                                                                ,{
						                                                                    xtype    : 'button'
						                                                                    ,text    : 'Ver errores'
						                                                                    ,hidden  : Number(json2.params.filasErrores)==0
						                                                                    ,handler : function()
						                                                                    {
						                                                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
						                                                                        {
						                                                                            modal        : true
						                                                                            ,closeAction : 'destroy'
						                                                                            ,title       : 'Errores al procesar censo'
						                                                                            ,width       : 800
						                                                                            ,height      : 500
						                                                                            ,items       :
						                                                                            [
						                                                                                {
						                                                                                    xtype       : 'textarea'
						                                                                                    ,fieldStyle : 'font-family: monospace'
						                                                                                    ,value      : json2.params.erroresCenso
						                                                                                    ,readOnly   : true
						                                                                                    ,width      : 780
						                                                                                    ,height     : 440
						                                                                                }
						                                                                            ]
						                                                                        }).show());
						                                                                    }
						                                                                }
						                                                            ]
						                                                        })
						                                                        ,Ext.create('Ext.grid.Panel',
						                                                        {
						                                                            height      : 350
						                                                            ,columns    : columns
						                                                            ,store      : store
						                                                            ,viewConfig : viewConfigAutoSize
						                                                        })
						                                                    ]
						                                                    ,buttonAlign : 'center'
						                                                    ,buttons     :
						                                                    [
						                                                        {
						                                                            text     : 'Aceptar y continuar'
						                                                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
						                                                            ,handler : function(me)
						                                                            { 
																						_fieldById('censoWin').close();
																						me.up('window').close();
																						debug('boton aceptar y continuar');
						                                                                _mask('Procesando');
						                                                                Ext.Ajax.request(
						                                                                {
                            															    url     : '<s:url namespace="/endosos" action="confirmarClonacionCondiciones" />'
                            																,params :
                            																{
                                														 	    'params.cdunieco'      : values.cdunieco
																								,'params.cdramo'       : json.params.cdramo
																								//,'params.estado'     : json.params.estado
																								,'params.estado'       : 'W'
																								,'params.nmpoliza'     : json.params.nmpolizaNueva
																								,'params.cdtipsit'     : values.cdtipsit
																								,'params.ntramite'     : json.params.ntramite
																								,'params.listlen' 	   : json2.slist1.length
                            																}
                            															,success : function(response)
                            															{
                            															    _unmask();
																							var json3 = Ext.decode(response.responseText);
																							debug("json3",json3);
																							//me.up('asegWin').close();																							
																							if(json3.success==true){
																								mensajeCorrecto('Tramite',json3.mensaje,function(){                                    		
																								Ext.create('Ext.window.Window',
																										{
																											title        : 'Documentaci&oacute;n'
																											,modal       : true
																											,buttonAlign : 'center'
																											,width       : 600
																											,height      : 400
																											,autoScroll  : true
																											,cls         : 'VENTANA_DOCUMENTOS_CLASS'
																											,loader      :
																											{
																												url       : mesConUrlDocu
																												,params   :
																												{
																													'smap1.nmpoliza'  : json.params.nmpolizaNueva
																													,'smap1.cdunieco' : json3.params.cdunieco
																													,'smap1.cdramo'   : json3.params.cdramo
																													,'smap1.estado'   : 'W'
																													,'smap1.nmsuplem' : '0'
																													,'smap1.ntramite' : json3.params.ntramite
																													,'smap1.nmsolici' : '0'
																													,'smap1.tipomov'  : '0'
																												}
																												,scripts  : true
																												,autoLoad : true
																											}
																											,buttons : [
																												{
																													text     : 'continuar'
																													,icon    : '${icons}accept.png'
																													,handler : function(){																																																												
																														if(json3.params.redireccion == 'S'){
																															if(values.cdtipsit == 'MSC')
																															{
																																Ext.create('Ext.form.Panel').submit(
																																{
																																	url             : mesConUrlComGrupo
																																	,standardSubmit : true
																																	,params         :
																																	{
																																		'smap1.cdunieco'  : json3.params.cdunieco
																																		,'smap1.cdramo'   : json3.params.cdramo
																																		,'smap1.cdtipsit' : values.cdtipsit
																																		,'smap1.estado'   : 'W'
																																		,'smap1.nmpoliza' : json.params.nmpolizaNueva
																																		,'smap1.ntramite' : json3.params.ntramite
																																		,'smap1.cdagente' : values.cdagente
																																		,'smap1.status'   : json3.params.statusNuevo
																																		,'smap1.sincenso' : 'N'
																																	}
																																});
																															}
																															else
																															{
																																Ext.create('Ext.form.Panel').submit(
																																{
																																	url             : mesConUrlComGrupo2
																																	,standardSubmit : true
																																	,params         :
																																	{
																																		'smap1.cdunieco'  : json3.params.cdunieco
																																		,'smap1.cdramo'   : json3.params.cdramo
																																		,'smap1.cdtipsit' : values.cdtipsit
																																		,'smap1.estado'   : 'W'
																																		,'smap1.nmpoliza' : json.params.nmpolizaNueva
																																		,'smap1.ntramite' : json3.params.ntramite
																																		,'smap1.cdagente' : values.cdagente
																																		,'smap1.status'   : json3.params.statusNuevo
																																		,'smap1.sincenso' : 'N'
																																	 }
																																});
																														   }
																														}else{
																															this.up('window').close();
																														}
																													}
																												}
																											]
																										}).show();
																								});																								
																							}
																							else
																							{
																								mensajeError('Error al guardar',json3.message);                                    
																							}	
																						}
																						,failure : function()
                            															{
                                															_unmask();
                                															errorComunicacion(null,'Error de red al guardar');
                            															}
																					}); 
						                                                          }
						                                                        }
						                                                        ,{
						                                                            text     : 'Reintentar'
						                                                            ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
						                                                            ,handler : function(me){ me.up('window').destroy(); }
						                                                        }
						                                                    ]
						                                                }).show());																		
						                                            };																	
						                                            despues();
						                                            //me.up('window').close();
						                                            /*_p25_generarTramiteClic(despues,false,false,true);*/
					                                            }
					                                            else
					                                            {
					                                                mensajeError(json2.respuesta);
					                                            }
					                                        }
					                                        catch(e)
					                                        {
																_unmask();
					                                            manejaException(e,ck);
					                                        }
															
					                                    }
					                                    ,failure : function(form2,action)
					                                    {
					                                        _unmask();
															form.setLoading(false);
															//debug('action',action);
															var json2 = Ext.decode(action.response.responseText);
															debug('json2',json2);
															if (Ext.isEmpty(json2.message)){
																errorComunicacion(null,'Error al subir archivo de complemento');
															}
															else{
																mensajeError(json2.message);
															}
					                                        
					                                    }
					                                })
					                            }
					                            else
					                            {
					                                datosIncompletos();
					                            }
					                        }
										}
										]
			                        }
			                        )
			                    ]
			                }).show()
			            );						
					}
					else
					{
						mensajeError('Error al guardar',json.message);                                    
					}
				}
				,failure : function()
				{
					_unmask();
					errorComunicacion(null,'Error de red al guardar');
				}
			});			

        }
    }
);
////// componentes dinamicos //////

Ext.onReady(function()
{
 Ext.Ajax.timeout = 1000*60*60; // 1 HORA
 Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
 Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
 Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    ////// modelos //////
     Ext.define('ModeloConvenio',
    {
        extend  : 'Ext.data.Model'
        ,fields : itemsGridModel
      }); 
    ////// modelos //////
    
    ////// stores //////
    store = Ext.create('Ext.data.Store',
    {
        model : 'ModeloConvenio'
    }); 
    ////// stores //////
    
    ////// componentes //////

    ////// componentes //////
    
    ////// contenido //////
    Ext.create('Ext.panel.Panel',
    {
        title     : 'Panel principal'
        ,panelPri : 'S'
        ,renderTo : '_p100_divpri'
        ,id		  : '_p100_formulario'
        ,defaults :
        {
            style : 'margin:5px;'
        }
        ,items    :
        [
		    Ext.create('Ext.form.Panel',
		    {
		        title     : 'Consulta y copia de tramites'
		        ,layout   :
		        {
		            type     : 'table'
		            ,columns : 3
		        }
		        ,defaults :
		        {
		            style : 'margin:5px;'
		        }
		        ,items : itemsFormulario
		        ,buttonAlign : 'center'
		        ,buttons :
		        [
		            {
                        text     : 'BUSCAR'
                        ,icon    : '${icons}find.png'
                        ,handler : function(me)
                        {
                            var values = me.up('form').getValues();
                            _mask('Buscando...');
                            Ext.Ajax.request(
                            {
                                url     : '<s:url namespace="/endosos" action="buscarTramites" />'
                                ,params :
                                {
                                    'smap1.pv_cdunieco_i'    : values.cdunieco
                                    ,'smap1.pv_cdramo_i'     : values.cdramo
                                    ,'smap1.pv_cdtipsit_i'   : values.cdtipsit
                                    ,'smap1.pv_estado_i' 	 : values.estado
                                    ,'smap1.pv_ntramite_i'   : values.nmtramite
                                    ,'smap1.pv_status_i' 	 : values.status
                                    ,'smap1.pv_fedesde_i' 	 : values.fecini
                                    ,'smap1.pv_fehasta_i' 	 : values.fecfin
                                    
                                }
                                ,success : function(response)
                                {
                                    _unmask();
                                    //var json = Ext.decode(response.responseText);
                                    json = Ext.decode(response.responseText);
                                    if(json.success==true)
                                    {
                                    	store.removeAll();
                                        store.add(json.slist1);
                                        //debug('json',json);
                                    }
                                    else
                                    {
                                        mensajeError(json.message);
                                    }
                                }
                                ,failure : function()
                                {
                                    _unmask();
                                    errorComunicacion(null,'error de red al guardar');
                                }
                            });
                        }
                    },
 		            {
		                text     : 'LIMPIAR'
		                ,icon    : '${icons}arrow_refresh.png'
		                ,handler : function()
		                {
		                	this.up('form').getForm().reset();
		                }
		            } 
		        ]
		    })
 		    ,Ext.create('Ext.grid.Panel',
		    {
		        title    : 'Tabla'
		        //,width   : 900
		        ,height  : 200
		        ,store   : store
		        ,columns : itemsGrid
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
////// funciones //////
</script>
</head>
<body>
<div id="_p100_divpri" style="height:600px;border:1px solid #CCCCCC"></div>
</body>
</html>