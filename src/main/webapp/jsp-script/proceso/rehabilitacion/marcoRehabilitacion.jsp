<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rehabilitar</title>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var marrehurlcata      = '<s:url namespace="/catalogos"         action="obtieneCatalogo" />';
    var marrehurlramos     = '<s:url namespace="/"                  action="obtenerRamos" />';
    var marrehStorePolizas;
    var marrehUrlFiltro    = '<s:url namespace="/rehabilitacion"    action="buscarPolizas" />'
    /*///////////////////*/
    ////// variables //////
    ///////////////////////
    
    ///////////////////////
    ////// funciones //////
    /*///////////////////*/
    
    function marRehValidaOperacion(recordOperacion)
    {
        if(recordOperacion.get('funcion')=='rehabilitacionunica')
        {
            debug('rehabilitacion unica');
            var nRecordsActivos=0;
            var recordActivo;
            marrehStorePolizas.each(function(record)
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
                Ext.getCmp('marrehMenuOperaciones').collapse();
                Ext.getCmp('marrehLoaderFrame').setTitle(recordOperacion.get('texto'));
                Ext.getCmp('marrehLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                    	'smap1' : recordActivo.raw
                    }
                });
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
    }
    /*///////////////////*/
    ////// funciones //////
    ///////////////////////
    
Ext.onReady(function()
{
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('MarRehPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            <s:property value="imap.fieldsModelo" />
            ,{
            	name  : 'activo'
            	,type : 'boolean'
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
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    marrehStorePolizas = Ext.create('Ext.data.Store',
    {
        pageSize  : 10
        ,autoLoad : true
        ,model    : 'MarRehPoliza'
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         : []
        }
    });
    
    marrehStoreLigas = Ext.create('Ext.data.Store',
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
                    texto    : 'Rehabilitaci&oacute;n &uacute;nica'
                    ,liga    : '<s:url namespace="/rehabilitacion" action="pantallaRehabilitacionUnica" />'
                    ,funcion : 'rehabilitacionunica'
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
        renderTo  : 'marcoRehabilitacionDiv'
        ,title    : 'Rehabilitaci&oacute;n'
        ,defaults :
        {
            style : 'margin : 5px'
        }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                title          : 'B&uacute;squeda'
                ,id            : 'marrehFilForm'
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
                ]
                ,buttons       :
                [
					{
					    text     : 'Limpiar'
					    ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
					    ,handler : function()
					    {
					        this.up().up().getForm().reset();
					    }
					}
                    ,{
                        text     : 'Buscar'
                        ,id      : 'marrehFilBotGen'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function()
                        {
                        	var form=this.up().up();
                            if(form.isValid())
                            {
                            	form.setLoading(true);
                                Ext.Ajax.request(
                                {
                                	url       : marrehUrlFiltro
                                	,jsonData :
                                	{
                                		'smap1' : this.up().up().getValues()
                                	}
                                    ,success  : function(response)
                                    {
                                    	form.setLoading(false);
                                        var json = Ext.decode(response.responseText);
                                        debug(json);
                                        if(json.success==true&&json.slist1&&json.slist1.length>0)
                                        {
                                            marrehStorePolizas.removeAll();
                                            marrehStorePolizas.add(json.slist1);
                                            debug(marrehStorePolizas);
                                        }
                                        else
                                        {
                                            Ext.Msg.show(
                                            {
                                                title    : 'Sin resultados'
                                                ,msg     : 'No hay resultados'
                                                ,icon    : Ext.Msg.WARNING
                                                ,buttons : Ext.Msg.OK
                                            });
                                        }
                                    }
                                    ,failure : function()
                                    {
                                    	form.setLoading(false);
                                        Ext.Msg.show(
                                        {
                                            title    : 'Error'
                                            ,icon    : Ext.Msg.ERROR
                                            ,msg     : 'Error de comunicaci&oacute;n'
                                            ,buttons : Ext.Msg.OK
                                        });
                                    }
                                });
                            }
                            else
                            {
                                Ext.Msg.show(
                                {
                                    title    : 'Datos imcompletos'
                                    ,icon    : Ext.Msg.WARNING
                                    ,msg     : 'Favor de llenar los campos requeridos'
                                    ,buttons : Ext.Msg.OK
                                });
                            }
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title          : 'P&oacute;lizas canceladas'
                //,width         : 1000
                ,collapsible   : true
                ,titleCollapse : true
                ,height        : 200
                ,store         : marrehStorePolizas
                ,frame         : true
                ,tbar          :
                [
                    {
                        text     : 'Marcar/desmarcar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/table_lightning.png'
                        ,handler : function()
                        {
                            var nRecordsActivos=0;
                            marrehStorePolizas.each(function(record)
                            {
                                if(record.get('activo')==true)
                                {
                                    nRecordsActivos=nRecordsActivos+1;
                                }
                            });
                            marrehStorePolizas.each(function(record)
                            {
                                record.set('activo',nRecordsActivos!=marrehStorePolizas.getCount());
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
                    ,<s:property value="imap.columnsGrid" />
                ]
            })
            ,Ext.create('Ext.panel.Panel',
            {
                id        : 'marrehLoaderFrameParent'
                ,layout   : 'border'
                //,width    : 1000
                ,height   : 1000
                ,border   : 0
                ,items    : 
                [
                    Ext.create('Ext.grid.Panel',
                    {
                        style        : 'margin-right : 5px;'
                        ,id          : 'marrehMenuOperaciones'
                        ,width       : 180
                        ,region      : 'west'
                        ,collapsible : true
                        ,margins     : '0 5 0 0'
                        ,layout      : 'fit'
                        ,frame       : false
                        ,title       : 'Operaciones'
                        ,store       : marrehStoreLigas
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
                                marRehValidaOperacion(record);
                            }
                        }
                    })
                    ,Ext.create('Ext.panel.Panel',
                    {
                        frame      : true
                        ,region    : 'center'
                        ,id        : 'marrehLoaderFrame'
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
    
});
</script>
</head>
<body>
<div id="marcoRehabilitacionDiv" style="height:1500px;"></div>
</body>
</html>