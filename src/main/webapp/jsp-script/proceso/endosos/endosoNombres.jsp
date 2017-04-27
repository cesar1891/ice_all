<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var endnomStoreAseg;

var _panelPrincipal;
var _gridAsegurados;

var unaPersonaMoral = false;

var endnomUrlGuardar       = '<s:url namespace="/endosos" action="guardarEndosoNombres" />';
var endnomUrlGuardarSimple = '<s:url namespace="/endosos" action="guardarEndosoNombresSimple" />';
var endnomUrlDoc           = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon" />';

var endnomInput         = [];
endnomInput['cdunieco'] = '<s:property value="smap1.cdunieco" />';
endnomInput['cdramo']   = '<s:property value="smap1.cdramo" />';
endnomInput['cdtipsit'] = '<s:property value="smap1.cdtipsit" />';
endnomInput['estado']   = '<s:property value="smap1.estado" />';
endnomInput['nmpoliza'] = '<s:property value="smap1.nmpoliza" />';
endnomInput['ntramite'] = '<s:property value="smap1.ntramite" />';
endnomInput['simple']   = <s:property value="endosoSimple" />;
endnomInput['fechainicio'] = endnomInput['simple'] ? '<s:property value="mensaje" />' : new Date(); 
debug('endnomInput',endnomInput);

var _endnom_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
    
debug('_endnom_flujo:',_endnom_flujo);
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/

/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function(){

	/////////////////////
	////// modelos //////
	/*/////////////////*/
    Ext.define('EndNomAsegurado',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            "nmsituac"
            ,"cdrol"
            ,{
                name        : 'fenacimi'
                ,type       : 'date'
                ,dateFormat : 'd/m/Y'
            }
            ,"sexo"
            ,"cdperson"
            ,"nombre"
            ,"segundo_nombre"
            ,"Apellido_Paterno"
            ,"Apellido_Materno"
            ,"cdrfc"
            ,"Parentesco"
            ,"tpersona"
            ,"nacional"
            ,"swexiper"
            ,{
                name  : 'activo'
                ,type : 'boolean'
            }
            ,'nombrecompleto'
            ,'CDUNIECO'
            ,'CDRAMO'
            ,'ESTADO'
            ,'NMPOLIZA'
            ,'NMPOLIEX'
            ,'NSUPLOGI'
            ,'CDTIPSIT'
            ,'CANALING'
            ,'CONDUCTO'
            ,'PTCUMUPR'
            ,'RESIDENCIA'
            ,'cdideper'
        ]
    });
	/*/////////////////*/
	////// modelos //////
	/////////////////////
	
	////////////////////
	////// stores //////
	/*////////////////*/
	endnomStoreAseg = Ext.create('Ext.data.Store',
    {
        pageSize  : 10
        ,autoLoad : true
        ,model    : 'EndNomAsegurado'
        ,proxy    :
        {
            enablePaging : true
            ,reader      : 'json'
            ,type        : 'memory'
            ,data        : <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />
        }
	    ,listeners        :
	    {
	    	load : function ( grid, records, successful, eOpts )
	        {
	    		debug('loaded');
   			    var indices=[];
   			    var contador=0;
   			       			    
   			    if(endnomStoreAseg.getTotalCount() == 1){
	    			var aseg1 = endnomStoreAseg.getAt(0);
	    			if(aseg1.get('tpersona') == 'M'){
	    				unaPersonaMoral = true;
	    				debug('_gridAsegurados: ',_gridAsegurados)
	    				_gridAsegurados.getView().headerCt.child('[dataIndex=nombre]').setText("Raz&oacute;n Social");
	    				_gridAsegurados.getView().headerCt.child('[dataIndex=segundo_nombre]').hide();
	    				_gridAsegurados.getView().headerCt.child('[dataIndex=Apellido_Paterno]').hide();
	    				_gridAsegurados.getView().headerCt.child('[dataIndex=Apellido_Materno]').hide();

	    			}
	    		}
   			    
	    		endnomStoreAseg.each(function(record1)
   			    {
   			        var cdperson=record1.get('cdperson');
   			        debug('verifi',cdperson);
   			        var repetidos=0;
   			        endnomStoreAseg.each(function(record2)
   			        {
   			            if(cdperson==record2.get('cdperson'))
   			            {
   			                repetidos=repetidos+1;
   			            }
   			        });
   			        if(repetidos>1)
   			        {
   			        	debug('quitar');
   			            record1.set('cdperson','@quitar');
   			            indices.push(contador);
   			        }
   			        else
   			        {
   			        	debug('mantener');
   			        }
   			        contador=contador+1;
   			    });
	    		debug(indices);
	    		
	    		for(contador=indices.length-1;contador>=0;contador--)
	    		{
	    			debug('quitando indice','-->'+indices[contador]);
	    			endnomStoreAseg.removeAt(indices[contador]);
	    		}
	        }
	    }
    });
    debug(endnomStoreAseg);
	/*////////////////*/
	////// stores //////
	////////////////////
	
	/////////////////////////
	////// componentes //////
	/*/////////////////////*/
	/*/////////////////////*/
	////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	/*///////////////////*/
    
    
    _gridAsegurados = Ext.create('Ext.grid.Panel',
		    {
		        title     : 'Asegurados seleccionados'
		        ,store    : endnomStoreAseg
		        ,height   : 200
		        ,selType  : 'cellmodel'
		        ,plugins  :
		        [
		            Ext.create('Ext.grid.plugin.CellEditing',
		            {
		                clicksToEdit: 1
		            })
		        ]
		        ,columns  :
		        [
		            {
		                header        : 'Clave'
		                ,dataIndex    : 'cdperson'
		                ,flex         : 1
		                ,menuDisabled : true
		            }
		            ,{
		                header     : 'Nombre'
		                ,dataIndex : 'nombre'
		                ,flex      : 3
		                ,editor    :
		                {
		                    xtype : 'textfield'
		                }
		            }
		            ,{
		                header     : 'Segundo nombre'
		                ,dataIndex : 'segundo_nombre'
		                ,flex      : 3
		                ,editor    :
		                {
		                    xtype : 'textfield'
		                }
		            }
		            ,{
		                header     : 'Apellido paterno'
		                ,dataIndex : 'Apellido_Paterno'
		                ,flex      : 3
		                ,editor    :
		                {
		                    xtype : 'textfield'
		                }
		            }
		            ,{
		                header     : 'Apellido materno'
		                ,dataIndex : 'Apellido_Materno'
		                ,flex      : 3
		                ,editor    :
		                {
		                    xtype : 'textfield'
		                }
		            }
		            ,{
		                header     : 'RFC'
		                ,dataIndex : 'cdrfc'
		                ,flex      : 2
		                ,editor    :
		                {
		                    xtype : 'textfield'
		                }
		            }
		        ]
		    });
    
    
	_panelPrincipal = Ext.create('Ext.panel.Panel',
	{
		border    : 0
		,renderTo : 'endnomDivPri'
		,defaults :
		{
		    style : 'margin : 5px;'
		}
	    ,items    :
	    [
	        
            _gridAsegurados
		    ,Ext.create('Ext.form.Panel',
            {
                title        : 'Informaci&oacute;n del endoso'
                ,buttonAlign : 'center'
                ,layout      :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,defaults    :
                {
                    style : 'margin : 5px;'
                }
                ,items       :
                [
                    {
                        xtype       : 'datefield'
                        ,fieldLabel : 'Fecha de efecto'
                        ,format     : 'd/m/Y'
                        ,value      : endnomInput['fechainicio']
                        ,allowBlank : false
                        ,name       : 'pv_fecha_i'
                        ,readOnly   : endnomInput['simple']
                    }
                ]
                ,buttons     :
                [
                    {
                    	text     : 'Generar endoso'
                    	,icon    : '${ctx}/resources/fam3icons/icons/key.png' 
                    	,handler : function()
                    	{
                    		debug('generar endoso');
                    		var form=this.up().up();
                    		if(form.isValid())
                    		{
                    			debug(endnomStoreAseg.getAt(0));
                    			_setLoading(true,form);
                    			var json={};
                    		    json['omap1']=form.getValues();
                    		    json['omap1']['pv_cdunieco_i'] = endnomInput['cdunieco'];
                    		    json['omap1']['pv_cdramo_i']   = endnomInput['cdramo'];
                    		    json['omap1']['pv_estado_i']   = endnomInput['estado'];
                    		    json['omap1']['pv_nmpoliza_i'] = endnomInput['nmpoliza'];
                    			var slist1=[];
                    			endnomStoreAseg.each(function(record)
                    			{
                    				slist1.push(
                    				{
	                    				cdperson : record.get('cdperson')
	                    			    ,nombre  : record.get('nombre')
	                    			    ,nombre2 : record.get('segundo_nombre')
	                    			    ,apat    : record.get('Apellido_Paterno')
	                    			    ,amat    : record.get('Apellido_Materno')
	                    			    ,rfc     : record.get('cdrfc')
                                        ,cdideper   : record.get('cdideper')
                                        ,tpersona   : record.get('tpersona')
                                        ,sexo       : record.get('sexo')
                                        ,fenacimi   : record.raw.fenacimi
                                        ,nacional   : record.get('nacional')
                                        ,CANALING   : record.get('CANALING')
                                        ,CONDUCTO   : record.get('CONDUCTO')
                                        ,PTCUMUPR   : record.get('PTCUMUPR')
                                        ,RESIDENCIA : record.get('RESIDENCIA')
                                        ,NMSITUAC   : record.get('nmsituac')
                    				});
                    			});
                    			json['slist1']=slist1;
                    			
                    			if(!Ext.isEmpty(_endnom_flujo))
                    			{
                    			    json.flujo = _endnom_flujo;
                    			}
                    			
                    			debug(json);
                    			Ext.Ajax.request(
                    			{
                    				url       : endnomInput['simple'] ? endnomUrlGuardarSimple : endnomUrlGuardar
                    				,jsonData : json
                    				,success  : function(response)
                    			    {
                    					_setLoading(false,form);
                    					json=Ext.decode(response.responseText);
                    					debug(json);
                    					if(json.success==true)
                    					{
                    						//////////////////////////////////
                    						////// usa codigo del padre //////
                    						/*//////////////////////////////*/
                    						var callbackRemesa = function(){ marendNavegacion(2); };
                                            /*//////////////////////////////*/
                                            ////// usa codigo del padre //////
                    						//////////////////////////////////
                    						Ext.Msg.show(
                                            {
                                                title   : 'Endoso generado',
                                                msg     : json.mensaje,
                                                buttons : Ext.Msg.OK
                                                ,fn     : function()
                                                {
                                                	
                                                	if(json.endosoConfirmado){
				                                        _generarRemesaClic(
	                                                        true
	                                                        ,endnomInput['cdunieco']
	                                                        ,endnomInput['cdramo']
	                                                        ,endnomInput['estado']
	                                                        ,endnomInput['nmpoliza']
	                                                        ,callbackRemesa
	                                                    );
						                            }else{
						                            	callbackRemesa();
						                            }
                                                }
                                            });
                    					}
                    					else
                    					{
                    						mensajeError(json.error);
                    					}
                    			    }
                    			    ,failure  : function()
                    			    {
                    			    	_setLoading(false,form);
                    			    	Ext.Msg.show(
                                        {
                                            title   : 'Error',
                                            icon    : Ext.Msg.ERROR,
                                            msg     : 'Error de comunicaci&oacute;n',
                                            buttons : Ext.Msg.OK
                                        });
                    			    }
                    			});
                    		}
                    		else
                    		{
                    			Ext.Msg.show(
                                {
                                    title   : 'Datos imcompletos',
                                    icon    : Ext.Msg.WARNING,
                                    msg     : 'Favor de llenar los campos requeridos',
                                    buttons : Ext.Msg.OK
                                });
                    		}
                    	}
                    }
                    ,{
                    	text     : 'Documentos'
                    	,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                    	,handler : function()
                        {
                    		Ext.create('Ext.window.Window',
                            {
                                title        : 'Documentos del tr&aacute;mite '+endnomInput['ntramite']
                                ,modal       : true
                                ,buttonAlign : 'center'
                                ,width       : 600
                                ,height      : 400
                                ,autoScroll  : true
                                ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                ,loader      :
                                {
                                    url       : endnomUrlDoc
                                    ,params   :
                                    {
                                        'smap1.nmpoliza'  : endnomInput['nmpoliza']
                                        ,'smap1.cdunieco' : endnomInput['cdunieco']
                                        ,'smap1.cdramo'   : endnomInput['cdramo']
                                        ,'smap1.estado'   : endnomInput['estado']
                                        ,'smap1.nmsuplem' : '0'
                                        ,'smap1.ntramite' : endnomInput['ntramite']
                                        ,'smap1.nmsolici' : ''
                                        ,'smap1.tipomov'  : '0'
                                    }
                                    ,scripts  : true
                                    ,autoLoad : true
                                }
                            }).show();
                        }
                    }
                ]
            })
	    ]
	});
	/*///////////////////*/
	////// contenido //////
	///////////////////////
	
	//////////////////////
	////// cargador //////
	/*//////////////////*/
	/*//////////////////*/
	////// cargador //////
	//////////////////////

});
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="endnomDivPri"></div>