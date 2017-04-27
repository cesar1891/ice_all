<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:if test="false">
<script>
</s:if>
debug('###############################################');
debug('###### scriptMesaAutorizacionEndosos.jsp ######');
debug('###############################################');

///////////////////////
////// variables //////
var _4_urlAutorizarEndoso = '<s:url namespace="/endosos" action="autorizarEndoso" />';
var _4_authEndUrlDoc      = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon" />';

var _4_selectedRecordEndoso;
var _4_windowAutorizarEndoso;
var _4_fieldComentAuthEndoso;
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
function _4_authEndosoDocumentos(grid,rowIndex,colIndex)
{
	var store=grid.getStore();
    var record=store.getAt(rowIndex);
    debug('record seleccionado',record);
    
    Ext.create('Ext.window.Window',
    {
        title        : 'Documentos del tr&aacute;mite '+record.get('ntramite')
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 600
        ,height      : 400
        ,autoScroll  : true
        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
        ,loader      :
        {
            url       : _4_authEndUrlDoc
            ,params   :
            {
                'smap1.nmpoliza'  : record.get('nmpoliza')
                ,'smap1.cdunieco' : record.get('cdunieco')
                ,'smap1.cdramo'   : record.get('cdramo')
                ,'smap1.estado'   : record.get('estado')
                ,'smap1.nmsuplem' : record.get('nmsuplem')
                ,'smap1.ntramite' : record.get('cdsucdoc')
                ,'smap1.nmsolici' : ''
                ,'smap1.tipomov'  : record.get('nombre')
            }
            ,scripts  : true
            ,autoLoad : true
        }
    }).show();
}

function _4_preAutorizarEndoso(grid,rowIndex,colIndex)
{
	var store=grid.getStore();
    var record=store.getAt(rowIndex);
    debug('record',record);
    
    _4_selectedRecordEndoso=record;
    _4_fieldComentAuthEndoso.setValue('');
    _4_windowAutorizarEndoso.show();
}

function _4_autorizarEndoso()
{
	var record=_4_selectedRecordEndoso;
	debug('record',record);
	
	var status=record.get('status');
	
	var valido=true;
	
	if(valido)
	{
		valido=status=='8';
		if(!valido)
		{
			mensajeWarning('El endoso ya est&aacute; confirmado o requiere autorizaci&oacute;n del gerente');
		}
	}
	
	if(valido)
	{
		mcdinGrid.setLoading(true);
		_4_windowAutorizarEndoso.hide();
		Ext.Ajax.request(
		{
			url       : _4_urlAutorizarEndoso
			,params   :
			{
				'smap1.ntramiteemi'  : record.get('parametros.pv_otvalor01')
				,'smap1.ntramiteend' : record.get('ntramite')
	            ,'smap1.cdunieco'    : record.get('cdunieco')
	            ,'smap1.cdramo'      : record.get('cdramo')
	            ,'smap1.estado'      : record.get('estado')
	            ,'smap1.nmpoliza'    : record.get('nmpoliza')
	            ,'smap1.cdtipsit'    : record.get('cdtipsit')
	            ,'smap1.nmsuplem'    : record.get('nmsuplem')
	            ,'smap1.nsuplogi'    : record.get('parametros.pv_otvalor04')
	            ,'smap1.cdtipsup'    : record.get('parametros.pv_otvalor02')
	            ,'smap1.status'      : '9'
	            ,'smap1.fechaEndoso' : Ext.Date.format(record.get('ferecepc'),'d/m/Y')
	            ,'smap1.observacion' : _4_fieldComentAuthEndoso.getValue()
			}
			,success  : function(response)
			{
				mcdinGrid.setLoading(false);
				var json=Ext.decode(response.responseText);
				if(json.success==true)
				{
					Ext.Msg.show(
					{
                        title    : 'Endoso autorizado'
                        ,msg     : 'El endoso ha sido autorizado'
                        ,buttons : Ext.Msg.OK
                        ,fn      : function()
                        {
                        	Ext.create('Ext.form.Panel').submit({standardSubmit:true});
                        }
                    });
				}
				else
				{
					_4_windowAutorizarEndoso.show();
					mensajeError(json.error);
				}
			}
		    ,failure  : function()
		    {
		    	mcdinGrid.setLoading(false);
		    	_4_windowAutorizarEndoso.show();
		    	errorComunicacion();
		    }
		});
	}
	
}
////// funciones //////
///////////////////////

Ext.onReady(function()
{
	/////////////////////////
	////// componentes //////
	_4_fieldComentAuthEndoso=Ext.create('Ext.form.field.TextArea',
	{
		width   : 280
		,height : 160
	});
	
	Ext.define('_4_WindowAutorizarEndoso',
	{
		extend         : 'Ext.window.Window'
		,initComponent : function()
		{
			debug('_4_WindowAutorizarEndoso initComponent');
			Ext.apply(this,
			{
				title        : 'Observaciones'
				,items       : _4_fieldComentAuthEndoso
				,modal       : true
				,buttonAlign : 'center'
				,width       : 300
				,height      : 250
				,closeAction : 'hide'
				,buttons     :
				[
				    {
				    	text     : 'Autorizar'
				    	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
				    	,handler : _4_autorizarEndoso
				    }
				]
			});
			this.callParent();
		}
	});
    ////// componentes //////
	/////////////////////////
	
	///////////////////////
	////// contenido //////
	_4_windowAutorizarEndoso=new _4_WindowAutorizarEndoso();
    ////// contenido //////
    ///////////////////////
});
<s:if test="false">
</script>
</s:if>