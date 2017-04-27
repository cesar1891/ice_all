<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cancelar</title>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var marcanurlcata      = '<s:url namespace="/catalogos"       action="obtieneCatalogo" />';
    var marcanurlramos     = '<s:url namespace="/"                action="obtenerRamos" />';
    var marcanStorePolizas;
    var marcanUrlFiltro    = '<s:url namespace="/cancelacion"     action="buscarPolizas" />'
    
    var _feproces = "";
    /*///////////////////*/
    ////// variables //////
    ///////////////////////
    
    ///////////////////////
    ////// funciones //////
    /*///////////////////*/
    function marcanMostrarControlesFiltro(tipo)
    {
    	Ext.getCmp('marcanFilForm').getForm().reset();
    	debug('marcanMostrarControlesFiltro',tipo);
    	if(tipo==1)
    	{
	    	Ext.getCmp('marcanFilUnieco').show();
	        Ext.getCmp('marcanFilRamo').show();
	        Ext.getCmp('marcanFilEstado').show();
	        Ext.getCmp('marcanFilNmpoliza').show();
	        Ext.getCmp('marcanFilNmpoliex').hide();
	        Ext.getCmp('marcanFilAgente').hide();
	        Ext.getCmp('marcanFilFereferen').hide();
    	}
    	else if (tipo==2)
    	{
    		Ext.getCmp('marcanFilUnieco').hide();
            Ext.getCmp('marcanFilRamo').hide();
            Ext.getCmp('marcanFilEstado').hide();
            Ext.getCmp('marcanFilNmpoliza').hide();
            Ext.getCmp('marcanFilNmpoliex').show();
            Ext.getCmp('marcanFilAgente').hide();
            Ext.getCmp('marcanFilFereferen').hide();
    	}
    	else if (tipo==3)
        {
            Ext.getCmp('marcanFilUnieco').show();
            Ext.getCmp('marcanFilRamo').hide();
            Ext.getCmp('marcanFilEstado').hide();
            Ext.getCmp('marcanFilNmpoliza').hide();
            Ext.getCmp('marcanFilNmpoliex').hide();
            Ext.getCmp('marcanFilAgente').show();
            Ext.getCmp('marcanFilFereferen').show();
        }
    }
    
    function validaOperacion(recordOperacion)
    {
    	if(recordOperacion.get('funcion')=='cancelacionunica')
   		{
   		    debug('cancelacion unica');
   		    var nRecordsActivos=0;
   		    var recordActivo;
   		    marcanStorePolizas.each(function(record)
   		    {
   		    	if(record.get('activo')==true)
   		    	{
   		    		nRecordsActivos=nRecordsActivos+1;
   		    		recordActivo=record;
   		    	}
   		    });
   		    if(nRecordsActivos==1)
   		    {
   		    	debug('continuar');
   		    	Ext.getCmp('marcanMenuOperaciones').collapse();
   		    	Ext.getCmp('marcanLoaderFrame').setTitle(recordOperacion.get('texto'));
   		    	debug('recordActivo.getData()',recordActivo.getData());
   		    	Ext.getCmp('marcanLoaderFrame').getLoader().load(
   		    	{
   		    		url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                    	'smap1' : recordActivo.raw
                    }
   		    	});
                //--------------
   		    	/*
                Ext.Msg.show(
                {
                    icon    : Ext.Msg.WARNING,
                    msg     : 'Seleccione una Raz&oacuten de Cancelaci&oacuten de la P&oacuteliza para continuar',
                    buttons : Ext.Msg.OK
                });
                */
                //--------------
   		    }
   		    else
   		    {
   		    	Ext.Msg.show(
                {
                    title   : 'Error',
                    icon    : Ext.Msg.WARNING,
                    msg     : 'Seleccione solo una p&oacute;liza',
                    buttons : Ext.Msg.OK
                });
   		    }
   		}
    	else if(recordOperacion.get('funcion')=='cancelacionauto')
        {
            debug('cancelacion automatica');
            var nRecordsActivos  = 0;
            var recordsActivos   = [];
            var jsonObject       = {};
            jsonObject['slist1'] = [];
            marcanStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nRecordsActivos=nRecordsActivos+1;
                    recordsActivos.push(record);
                    jsonObject['slist1'].push(record.raw);
                }
            });
            debug('records activos: ',recordsActivos);
            debug('json object: ',jsonObject);
            if(nRecordsActivos>0)
            {
                debug('continuar');
                Ext.getCmp('marcanMenuOperaciones').collapse();
                Ext.getCmp('marcanLoaderFrame').setTitle(recordOperacion.get('texto'));
                Ext.getCmp('marcanLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : jsonObject
                });
            }
            else
            {
                Ext.Msg.show(
                {
                    title   : 'Error',
                    icon    : Ext.Msg.WARNING,
                    msg     : 'Seleccione al menos una p&oacute;liza',
                    buttons : Ext.Msg.OK
                });
            }
        }
    }
    /*///////////////////*/
    ////// funciones //////
    ///////////////////////
    
Ext.onReady(function()
{
	
	// Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*60; // 60 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('Ramo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            "cdramo"
            ,"dsramo"
        ]
    });
    
    Ext.define('MarCanPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {
        	    name        : "FEMISION"
            	,type       : "date"
            	,dateFormat : "d/m/Y"
            }
            ,{
                name        : "FEINICOV"
                ,type       : "date"
                ,dateFormat : "d/m/Y"
            }
            ,{
                name        : "FEFINIV"
                ,type       : "date"
                ,dateFormat : "d/m/Y"
            }
            ,{
                name        : "PRITOTAL"
                ,type       : "float"
            }
            ,"NOMBRE"
            ,"DSRAMO"
            ,"CDRAMO"
            ,"DSTIPSIT"
            ,"CDTIPSIT"
            ,"DSUNIECO"
            ,"CDUNIECO"
            ,"NMPOLIZA"
            ,"NMPOLIEX"
            ,"NMSOLICI"
            ,"ESTADO"
            ,{
            	name  : 'activo'
            	,type : 'boolean'
            }
            ,{
                name        : "FERECIBO"
                ,type       : "date"
                ,dateFormat : "d/m/Y"
            }
        ]
    });
    
    Ext.define('Liga',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            "texto"
            ,"liga"
            ,"funcion"
        ]
    });
    
    Ext.define('MarcanCandidatas',
    {
    	extend  : 'Ext.data.Model'
    	,fields :
    	[
    	    <s:property value="imap.fieldsCandidata" />
    	]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    marcanStorePolizas = Ext.create('Ext.data.Store',
    {
        pageSize  : 10
        ,autoLoad : true
        ,model    : 'MarcanCandidatas'
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         : []
        }
    });
    
    marcanStoreLigas = Ext.create('Ext.data.Store',
    {
        autoLoad  : true
        ,model    : 'Liga'
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         :
            [
                {
                	texto    : 'Cancelaci&oacute;n &uacute;nica'
                	,liga    : '<s:url namespace="/cancelacion"     action="pantallaCancelar" />'
                	,funcion : 'cancelacionunica'
                }
                ,{
                    texto    : 'Cancelaci&oacute;n autom&aacute;tica'
                    ,liga    : '<s:url namespace="/cancelacion"     action="pantallaCancelarAuto" />'
                    ,funcion : 'cancelacionauto'
                }
            ]
        }
    });
    
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*
    Ext.define('Panel1',{
        extend:'Ext.panel.Panel',
        title:'Objeto asegurado',
        layout:{
            type:'column',
            columns:2
        },
        frame:false,
        style:'margin : 5px;',
        collapsible:true,
        titleCollapse:true,
        <s:property value="item2" />
    });
    Ext.define('FormPanel',{
        extend:'Ext.form.Panel',
        renderTo:'maindiv',
        frame:false,
        buttonAlign:'center',
        items:[
            new Panel1()
        ]
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    Ext.create('Ext.panel.Panel',
    {
    	renderTo  : 'marcoCancelacionDiv'
    	,title    : 'Cancelaci&oacute;n'
    	,defaults :
    	{
    		style : 'margin : 5px'
    	}
    	,items    :
    	[
    	    Ext.create('Ext.form.Panel',
    	    {
    	    	title          : 'B&uacute;squeda de p&oacute;lizas candidatas a cancelar'
    	    	,id            : 'marcanFilForm'
    	    	//,width         : 1000
    	    	,url           : marcanUrlFiltro
    	    	,layout        :
    	    	{
    	    		type     : 'table'
    	    		,columns : 3
    	    	}
    	    	,defaults      :
    	    	{
    	    		style : 'margin:5px;'
    	    	}
    	    	,collapsible   : true
    	    	,titleCollapse : true
    	    	,buttonAlign   : 'center'
    	    	,frame         : true
    	    	,items         :
    	    	[
    	    	    <s:property value="imap.itemsFiltro" />
    	    	    /*
    	    	    {
			        	xtype           : 'combo'
			        	,id             : 'marcanFilUnieco'
			            ,fieldLabel     : 'Sucursal'
			            ,name           : 'smap1.pv_cdunieco_i'
			            ,displayField   : 'value'
			            ,valueField     : 'key'
			            ,forceSelection : true
			            ,queryMode      :'local'
			            ,store          : Ext.create('Ext.data.Store',
			            {
			                model     : 'Generic'
			                ,autoLoad : true
			                ,proxy    :
			                {
			                    type         : 'ajax'
			                    ,url         : marcanurlcata
			                    ,extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@MC_SUCURSALES_DOCUMENTO"/>'}
			                    ,reader      :
			                    {
			                        type  : 'json'
			                        ,root : 'lista'
			                    }
			                }
			            })
			            ,listeners      :
			            {
			            	'change' : function()
			            	{
			            		Ext.getCmp('marcanFilRamo').getStore().load(
			            		{
			            			params : {'map1.cdunieco':this.getValue()}
			            		});
			            	}
			            }
			        }
			        ,{
			        	xtype           : 'combo'
                        ,id             : 'marcanFilRamo'
                        ,fieldLabel     : 'Producto'
                        ,name           : 'smap1.pv_cdramo_i'
                        ,valueField     : 'cdramo'
                        ,displayField   : 'dsramo'
                        ,forceSelection : true
                        ,queryMode      :'local'
                        ,store          : Ext.create('Ext.data.Store',
                        {
                            model     : 'Ramo'
                            ,autoLoad : false
                            ,proxy    :
                            {
                                type    : 'ajax'
                                ,url    : marcanurlramos
                                ,reader :
                                {
                                    type  : 'json'
                                    ,root : 'slist1'
                                }
                            }
                        })
                    }
			        ,{
                        xtype           : 'combo'
                        ,id             : 'marcanFilEstado'
                        ,fieldLabel     : 'Estado'
                        ,name           : 'smap1.pv_estado_i'
                        ,displayField   : 'value'
                        ,valueField     : 'key'
                        ,forceSelection : true
                        ,queryMode      :'local'
                        ,store          : Ext.create('Ext.data.Store',
                        {
                            model     : 'Generic'
                            ,autoLoad : true
                            ,proxy    :
                            {
                                type         : 'ajax'
                                ,url         : marcanurlcata
                                ,extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@STATUS_POLIZA"/>'}
                                ,reader      :
                                {
                                    type  : 'json'
                                    ,root : 'lista'
                                }
                            }
                        })
                    }
			        ,{
			        	xtype       : 'numberfield'
			        	,id         : 'marcanFilNmpoliza'
			        	,name       : 'smap1.pv_nmpoliza_i'
			        	,fieldLabel : 'P&oacute;liza/Cotizaci&oacute;n'
			        }
			        ,{
                        xtype       : 'textfield'
                        ,id         : 'marcanFilNmpoliex'
                        ,name       : 'smap1.pv_nmpoliex_i'
                        ,fieldLabel : 'N&uacute;mero de p&oacute;liza'
                    }
			        ,Ext.create('Ext.form.field.ComboBox',
                    {
                        fieldLabel : 'Agente'
                        ,id        : 'marcanFilAgente'
                        ,name      : 'smap1.pv_cdagente_i'
                        ,displayField : 'value'
                        ,valueField   : 'key'
                        ,forceSelection : true
                        ,matchFieldWidth: false
                        ,hideTrigger : true
                        ,minChars  : 3
                        ,queryMode :'remote'
                        ,queryParam: 'params.agente'
                        ,store : Ext.create('Ext.data.Store', {
                            model:'Generic',
                            autoLoad:false,
                            proxy: {
                                type: 'ajax',
                                url : marcanurlcata,
                                extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@AGENTES"/>'},
                                reader: {
                                    type: 'json',
                                    root: 'lista'
                                }
                            }
                        })
                    })
                    ,{
			        	xtype       : 'datefield'
			        	,id         : 'marcanFilFereferen'
			        	,format     : 'd/m/Y'
			        	,fieldLabel : 'Fecha de referencia'
			        	,name       : 'smap1.pv_fereferen_i'
			        	,value      : new Date()
			        }
			        */
    	    	]
    	    	,buttons       :
    	    	[
					/*{
					    text  : 'Tipo de b&uacute;squeda'
					    ,icon : '${ctx}/resources/fam3icons/icons/cog.png'
					    ,menu :
					    {
					        xtype     : 'menu'
					        ,items    :
					        [
					            {
					                text     : 'General'
					                ,handler : function(){marcanMostrarControlesFiltro(1);}
					            }
					            ,{
					                text     : 'Por p&oacute;liza'
					                ,handler : function(){marcanMostrarControlesFiltro(2);}
					            }
					            ,{
                                    text     : 'Por agente'
                                    ,handler : function(){marcanMostrarControlesFiltro(3);}
                                }
					        ]
					    }
					}
    	    		,*/
                    {
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                        ,handler : function()
                        {
                            this.up().up().getForm().reset();
                        }
                    },
	    			{
    	    			text     : 'Buscar'
    	    			,id      : 'marcanFilBotGen'
    	    			,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
    	    			,handler : function()
    	    		    {
    	    				var form1=this.up().up();
    	    				if(form1.isValid())
    	    				{
    	    					form1.setLoading(true);
	    	    				form1.submit(
	    	    				{
	    	    					success  : function(form,action)
	    	    					{
	    	    						_feproces = form1.getValues()['smap1.pv_fechapro_i'];
	    	    						
	    	    						form1.setLoading(false);
	    	    						debug(action);
	    	    						var json = Ext.decode(action.response.responseText);
	    	    						debug(json);
	    	    						if(json.success==true&&json.slist1&&json.slist1.length>0)
	    	    						{
	    	    							marcanStorePolizas.removeAll();
	    	    							marcanStorePolizas.add(json.slist1);
	    	    							Ext.getCmp('marcanBotCheck').handler();
	    	    							debug(marcanStorePolizas);
	    	    						}
	    	    						else
	    	    						{
	    	    							_feproces = "";
	    	    							Ext.Msg.show(
   	                                        {
   	                                            title    : 'Sin resultados'
   	                                            ,msg     : 'No hay resultados'
   	                                            ,icon    : Ext.Msg.WARNING
   	                                            ,buttons : Ext.Msg.OK
   	                                        });
	    	    						}
	    	    						
	    	    						Ext.getCmp('marcanMenuOperaciones').expand();
	    	    		                Ext.getCmp('marcanLoaderFrame').update('');
	    	    					}
	    	    				    ,failure : function(action,response)
	    	    				    {
	    	    				    	_feproces = "";
	    	    				    	form1.setLoading(false);
	    	    				    	var json=Ext.decode(response.response.responseText);
	    	    				    	mensajeError(json.error);
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
    	    	]
    	    })
    	    ,Ext.create('Ext.grid.Panel',
    	    {
    	    	title          : 'P&oacute;lizas candidatas a cancelar'
   	    		//,width         : 1000
    	    	,collapsible   : true
    	    	,titleCollapse : true
    	    	,height        : 350
    	    	,store         : marcanStorePolizas
    	    	,frame         : true
    	    	,tbar          :
    	    	[
    	    	    {
    	    	    	text     : 'Marcar/desmarcar'
    	    	    	,id      : 'marcanBotCheck'
    	    	    	,icon    : '${ctx}/resources/fam3icons/icons/table_lightning.png'
    	    	    	,handler : function()
    	    	    	{
    	    	    		var nRecordsActivos=0;
    	    	            marcanStorePolizas.each(function(record)
    	    	            {
    	    	                if(record.get('activo')==true)
    	    	                {
    	    	                    nRecordsActivos=nRecordsActivos+1;
    	    	                }
    	    	            });
    	    	            marcanStorePolizas.each(function(record)
                            {
                                record.set('activo',nRecordsActivos!=marcanStorePolizas.getCount());
                            });
    	    	    	}
    	    	    }
    	    	]
    	    	,columns       :
    	    	[
    	    	    {
    	    	    	dataIndex     : 'activo'
    	    	    	,xtype        : 'checkcolumn'
    	    	    	,width        : 30
    	    	    	,menuDisabled : true
    	    	    	,sortable     : false
    	    	    }
    	    	    ,<s:property value="imap.columnsCandidata" />
    	    	    /*
    	    	    ,{
    	    	    	header     : "Sucursal"
    	    	    	,dataIndex : "DSUNIECO"
    	    	    	,flex      : 1
    	    	    }
    	    	    ,{
                        header     : "Producto"
                        ,dataIndex : "DSRAMO"
                        ,flex      : 1
                    }
    	    	    ,{
                        header     : "P&oacute;liza"
                        ,dataIndex : "NMPOLIEX"
                        ,flex      : 1
                    }
    	    	    ,{
    	    	    	header     : 'Inicio de vigencia'
    	    	    	,dataIndex : 'FEINICOV'
    	    	    	,xtype     : 'datecolumn'
    	    	    	,format    : 'd M Y'
    	    	    	,flex      : 1
    	    	    }
    	    	    ,{
                        header     : 'Fin de vigencia'
                        ,dataIndex : 'FEFINIV'
                        ,xtype     : 'datecolumn'
                        ,format    : 'd M Y'
                        ,flex      : 1
                    }
    	    	    ,{
                        header     : 'Fecha de recibo'
                        ,dataIndex : 'FERECIBO'
                        ,xtype     : 'datecolumn'
                        ,format    : 'd M Y'
                        ,flex      : 1
                    }
    	    	    ,{
    	    	    	header     : 'Cliente'
    	    	    	,dataIndex : 'NOMBRE'
    	    	    	,flex      : 1
    	    	    }
    	    	    ,{
                        header     : 'Prima total'
                        ,dataIndex : 'PRITOTAL'
                        ,renderer  : Ext.util.Format.usMoney
                        ,flex      : 1
                    }
    	    	    */
    	    	]
    	    })
    	    ,Ext.create('Ext.panel.Panel',
    	    {
    	    	id        : 'marcanLoaderFrameParent'
    	    	,layout   : 'border'
    	    	//,width    : 1000
    	    	,height   : 1000
    	        ,border   : 0
    	    	,items    : 
    	    	[
    	    	    Ext.create('Ext.grid.Panel',
    	    	    {
    	    	    	style        : 'margin-right : 5px;'
    	    	    	,id          : 'marcanMenuOperaciones'
    	    	    	,width       : 180
    	    	    	,region      : 'west'
    	    	    	,collapsible : true
    	    	    	,margins     : '0 5 0 0'
    	    	    	,layout      : 'fit'
    	    	    	,frame       : false
    	    	    	,title       : 'Operaciones'
    	    	    	,store       : marcanStoreLigas
    	    	    	,hideHeaders : true
    	    	    	,columns     :
    	    	    	[
    	    	    	    {
    	    	    	    	dataIndex : 'texto'
    	    	    	    	,flex     : 1
    	    	    	    }
    	    	    	]
    	    	    	,listeners   :
    	    	    	{
                            'cellclick' : function(grid, td, cellIndex, record)
                            {
                            	validaOperacion(record);
                            }
    	    	        }
    	    	    })
    	    	    ,Ext.create('Ext.panel.Panel',
    	    	    {
    	    	    	frame      : true
    	    	    	,region    : 'center'
    	    	    	,id        : 'marcanLoaderFrame'
    	    	    	,loader    :
    	    	    	{
    	    	    		autoLoad: false
    	    	    	}
    	    	    })
    	    	]
    	    })
    	]
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*
    Ext.define('LoaderForm',
    {
        extend:'Modelo1',
        proxy:
        {
            extraParams:{
            },
            type:'ajax',
            url : urlCargar,
            reader:{
                type:'json'
            }
        }
    });

    var loaderForm=Ext.ModelManager.getModel('LoaderForm');
    loaderForm.load(123, {
        success: function(resp) {
            //console.log(resp);
            formPanel.loadRecord(resp);
        },
        failure:function()
        {
            Ext.Msg.show({
                title:'Error',
                icon: Ext.Msg.ERROR,
                msg: 'Error al cargar',
                buttons: Ext.Msg.OK
            });
        }
    });
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
    //marcanMostrarControlesFiltro(1);
    
});
</script>
</head>
<body>
<div id="marcoCancelacionDiv" style="height:1500px;"></div>
</body>
</html>