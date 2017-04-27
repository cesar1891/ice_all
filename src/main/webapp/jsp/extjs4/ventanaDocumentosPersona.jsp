<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
////// variables //////
_p23_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;
debug('_p23_smap1:',_p23_smap1);

var _p23_urlSubirArchivo = '<s:url namespace="/"            action="subirArchivoPersona"      />';
var _p23_UrlUploadPro    = '<s:url namespace="/"            action="subirArchivoMostrarBarra" />';
var _p23_UrlCargar       = '<s:url namespace="/catalogos"   action="cargarDocumentosPersona"  />';
var _p23_urlViewDoc      = '<s:url namespace ="/documentos" action="descargaDocInline" />';
var _p23_urlDownload     = '<s:url namespace ="/documentos" action="descargaDoc"       />';

var _RUTA_DOCUMENTOS_PERSONA = '<s:text name="ruta.documentos.persona" />';

var _p23_StoreDoc;
////// variables //////
Ext.onReady(function()
{
	////// modelos //////
	Ext.define('Documento',
    {
        extend    : 'Ext.data.Model'
        ,fields   :
        [
            'cddocume'
            ,'cdperson'
            ,'dsdocume'
            ,{name:'feinici',type:'date',dateFormat:'d/m/Y'}
            ,'liga'
        ]
    });
	////// modelos //////
	
	////// stores //////
	_p23_StoreDoc=Ext.create('Ext.data.Store',
    {
        model       : 'Documento'
        ,autoLoad   : true
        ,proxy      :
        {
            url          : _p23_UrlCargar
            ,extraParams : 
            {
                'smap1.cdperson' : _p23_smap1.cdperson
            }
            ,type        : 'ajax'
            ,reader      :
            {
                type  : 'json'
                ,root : 'slist1'
            }
        }
    });
	////// stores //////
	
	////// componentes //////
	////// componentes //////
	
	////// contenido //////
	Ext.create('Ext.grid.Panel',
	{
		renderTo : '_p23_maindiv'
		,store   : _p23_StoreDoc
		,height  : 300
		,columns :
		[
			{
			    header     : 'Descripci&oacute;n'
			    ,dataIndex : 'dsdocume'
			    ,flex      : 2
			}
			,{
			    header     : 'Fecha'
			    ,dataIndex : 'feinici'
			    ,flex      : 1
			    ,renderer  : Ext.util.Format.dateRenderer('d M Y')
			}
			,{
			    dataIndex     : 'liga'
			    ,width        : 30
			    ,renderer     : function(value)
			    {
			        debug(value);
			        var res='';
			        var splited=value.split('#_#');//nombre#_#descripcion
			        debug(splited);
			        var nom=splited[0];
			        var desc=splited[1];
			        if(nom&&nom.length>4)
			        {
			            var http=nom.substr(0,4);
			            if(true||http!='http')
			            {
			                res='<img src="${ctx}/resources/fam3icons/icons/eye.png" data-qtip="Abrir en línea" style="cursor:pointer;" />';
			            }
			        }
			        return res;
			    }
			}
			,{
			    dataIndex     : 'liga'
			    ,width        : 30
			    ,renderer     : function(value)
			    {
			        debug(value);
			        var res='';
			        var splited=value.split('#_#');//nombre#_#descripcion
			        debug(splited);
			        var nom=splited[0];
			        var desc=splited[1];
			        if(nom&&nom.length>4)
			        {
			            var http=nom.substr(0,4);
			            if(http!='http')
			            {
			                res='<img src="${ctx}/resources/fam3icons/icons/page_white_put.png" data-qtip="Descargar" style="cursor:pointer;" />';
			            }
			        }
			        return res;
			    }
		    }
	    ]
	    ,dockedItems :
	    [
	        {
	            xtype   : 'toolbar'
	            ,dock   : 'top'
	            ,hidden : true
	            ,items  :
	            [
	                {
	                    xtype    : 'button'
	                    ,text    : 'Agregar'
	                    ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
	                    ,handler : _p23_onAddClick
	                    ,hidden  : true
	                }
	            ]
	        }
	    ]
	    ,listeners:
	    {
	        cellclick : function(grid, td,cellIndex, record, tr,rowIndex, e, eOpts)
	        {
	            debug( cellIndex+'x', rowIndex+'y' , record.raw );
	            if(cellIndex==2)//ver
	            {
	                debug($(td).find('img').length);
	                if($(td).find('img').length>0)//si hay accion
	                {
	                    var nom=record.get('cddocume');
	                    debug(nom);
	                    var salida=false;
	                    if(nom&&nom.length>4)
	                    {
	                        var http=nom.substr(0,4);
	                        if(http=='http')
	                        {
	                            salida=true;
	                            var numRand=Math.floor((Math.random()*100000)+1);
	                            debug('numRand b: ',numRand);
	                            var windowVerDocu=Ext.create('Ext.window.Window',
	                            {
	                                title          : record.get('dsdocume')
	                                ,width         : 700
	                                ,height        : 500
	                                ,collapsible   : true
	                                ,titleCollapse : true
	                                ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                                             +'src="'+nom+'">'
                                                             +'</iframe>'
                                    ,listeners     :
                                    {
                                        resize : function(win,width,height,opt){
                                            debug(width,height);
                                            $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
                                        }
                                    }
                                }).show();
                                windowVerDocu.center();
                            }
                        }
                        if(!salida)
                        {
                            _p23_onViewClick(record);
                        }
                    }
                }
                else if(cellIndex==3)//descargar
                {
                    debug($(td).find('img').length);
                    if($(td).find('img').length>0)//si hay accion
                    {
                        _p23_onDownloadClick(record);
                    }
                }
            }
        }
	});
	////// contenido //////
	
	////// loaders //////
	////// loaders //////
});

////// funciones //////
function _p23_onAddClick(button,e)
{
            var windowAgregarDocu=Ext.create('Ext.window.Window',
            {
                id           : '_p23_WinPopupAddDoc'
                ,title       : 'Agregar documento'//+_p23_InputNmpoliza
                ,closable    : false
                ,modal       : true
                ,width       : 500
                //,height   : 700
                ,bodyPadding : 5
                ,items       :
                [
                    panelSeleccionDocumento= Ext.create('Ext.form.Panel',
                    {
                        border       : 0
                        ,url         : _p23_urlSubirArchivo
                        ,buttonAlign : 'center'
                        ,items       :
                        [
                            {
                                xtype       : 'datefield'
                                ,readOnly   : true
                                ,format     : 'd/m/Y'
                                ,name       : 'smap1.fecha'
                                ,value      : new Date()
                                ,fieldLabel : 'Fecha'
                            }
                            ,{
                                xtype       : 'textfield'
                                ,fieldLabel : 'Descripci&oacute;n'
                                ,name       : 'smap1.descripcion'
                                ,width      : 450
                            }
                            ,{
                                xtype       : 'filefield'
                                ,fieldLabel : 'Documento'
                                ,buttonText : 'Examinar...'
                                ,buttonOnly : false
                                ,width      : 450
                                ,name       : 'file'
                                ,cAccept    : ['jpg','png','gif','zip','pdf','rar','jpeg','doc','docx','xls','xlsx','ppt','pptx']
                                ,listeners  :
                                {
                                    change : function(me)
                                    {
                                        var indexofPeriod = me.getValue().lastIndexOf("."),
                                        uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                                        if (!Ext.Array.contains(this.cAccept, uploadedExtension))
                                        {
                                            Ext.MessageBox.show(
                                            {
                                                title   : 'Error de tipo de archivo',
                                                msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                                buttons : Ext.Msg.OK,
                                                icon    : Ext.Msg.WARNING
                                            });
                                            me.reset();
                                            Ext.getCmp('_p23_botGuaDoc').setDisabled(true);
                                        }
                                        else
                                        {
                                            Ext.getCmp('_p23_botGuaDoc').setDisabled(false);
                                        }
                                    }
                                }
                            }
                            ,Ext.create('Ext.panel.Panel',
                            {
                                html    :'<iframe id="_p23_IframeUploadDoc" name="_p23_IframeUploadDoc"></iframe>'
                                ,hidden : true
                            })
                            ,Ext.create('Ext.panel.Panel',
                            {
                                border  : 0
                                ,html   :'<iframe id="_p23_IframeUploadPro" name="_p23_IframeUploadPro" width="100%" height="30" src="'+_p23_UrlUploadPro+'" frameborder="0"></iframe>'
                                ,hidden : false
                            })
                        ]
                        ,buttons     :
                        [
                            {
                                id        : '_p23_botGuaDoc'
                                ,text     : 'Agregar'
                                ,icon     : '${ctx}/resources/fam3icons/icons/disk.png'
                                ,disabled : true
                                ,handler  : function (button,e)
                                {
                                    debug(button.up().up().getForm().getValues());
                                    button.setDisabled(true);
                                    Ext.getCmp('_p23_BotCanDoc').setDisabled(true);
                                    Ext.create('Ext.form.Panel').submit(
                                    {
                                        url             : _p23_UrlUploadPro
                                        ,standardSubmit : true
                                        ,target         : '_p23_IframeUploadPro'
                                        ,params         :
                                        {
                                            uploadKey : '1'
                                        }
                                    });
                                    button.up().up().getForm().submit(
                                    {
                                        standardSubmit : true
                                        ,target        : '_p23_IframeUploadDoc'
                                        ,params        :
                                        {
                                            'smap1.cdperson'  : _p23_smap1.cdperson
                                        }
                                    });
                                }
                            }
                            ,{
                                id       : '_p23_BotCanDoc'
                                ,text    : 'Cancelar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                ,handler : function (button,e)
                                {
                                    button.up().up().up().destroy();
                                }
                            }
                        ]
                    })
                ]
            }).show();
            centrarVentanaInterna(windowAgregarDocu);
}

function panDocSubido()
{
    Ext.getCmp('_p23_WinPopupAddDoc').destroy();
    _p23_StoreDoc.load();
}

function _p23_onViewClick(record)
{
    var numRand=Math.floor((Math.random()*100000)+1);
    debug('numRand a: ',numRand);
    var windowVerDocu=Ext.create('Ext.window.Window',
    {
        title          : record.get('dsdocume')
        ,width         : 700
        ,height        : 500
        ,collapsible   : true
        ,titleCollapse : true
        ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" style="overflow: hidden; height: 100%;width: 100%; position: absolute;" height="100%" width="100%"'
                +'src="'+_p23_urlViewDoc+'?path='+_RUTA_DOCUMENTOS_PERSONA+'&subfolder='+record.get('cdperson')+'&filename='+record.get('cddocume')+'">'
                +'</iframe>'
        ,listeners     :
        {
             resize : function(win,width,height,opt){
                 debug(width,height);
                 $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
             }
        }
    }).show();
    centrarVentanaInterna(windowVerDocu);
}

function _p23_onDownloadClick(record)
{
    Ext.create('Ext.form.Panel').submit(
    {
        url              : _p23_urlDownload
        , standardSubmit : true
        , target         : '_blank'
        , params         :
        {
        	path      : _RUTA_DOCUMENTOS_PERSONA
            ,subfolder: record.get('cdperson')
            ,filename : record.get('cddocume')
        }
    });
}
////// funciones //////
</script>
<div id="_p23_maindiv" style="height:350px;"></div>