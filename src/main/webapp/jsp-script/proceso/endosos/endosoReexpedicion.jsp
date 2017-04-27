<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
///////////////////////
////// variables //////

/*
smap1:
    CDRAMO: "2"
    CDTIPSIT: "SL"
    CDUNIECO: "1006"
    DSCOMENT: ""
    DSTIPSIT: "SALUD VITAL"
    ESTADO: "M"
    FEEMISIO: "22/01/2014"
    FEINIVAL: "22/01/2014"
    NMPOLIEX: "1006213000024000000"
    NMPOLIZA: "24"
    NMSUPLEM: "245668019180000000"
    NSUPLOGI: "1"
    NTRAMITE: "678"
    PRIMA_TOTAL: "12207.37"
*/
//Obtenemos el contenido en formato JSON de la propiedad solicitada:
var _8_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _8_formLectura;
var _8_panelPri;
var _8_panelEndoso;

var _8_tabGruposLinealCols;
var _8_storeGrupos;
var _8_editorPlan;

var _8_urlLoaderLectura = '<s:url namespace="/consultasPoliza" action="consultaDatosPoliza" />';
var _8_urlCargarGrupos = '<s:url namespace="/emision" action="cargarGruposCotizacionReexpedicion" />';
var _8_urlGuardar = '<s:url namespace="/endosos" action="guardarEndosoReexpedicion" />';

_8_smap1.cdunieco = _8_smap1.CDUNIECO;
_8_smap1.cdramo = _8_smap1.CDRAMO;
_8_smap1.estado = _8_smap1.ESTADO;
_8_smap1.nmpoliza = _8_smap1.NMPOLIZA;
_8_smap1.cdtipsit = _8_smap1.CDTIPSIT;

var _TIPOFLOT =  _8_smap1.TIPOFLOT;

debug('_8_smap1:',_8_smap1);

var _p8_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

debug('_p8_flujo:',_p8_flujo);

//
//// variables //////
///////////////////////

Ext.onReady(function()
{
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
    /////////////////////
    ////// modelos //////
    Ext.define('_8_ModeloPoliza',
    {
    	extend  : 'Ext.data.Model'
    	,fields : [ <s:property value="imap1.fieldsFormLectura" /> ]
    });
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    Ext.define('_8_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_8_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,layout   :
                {
                	type     : 'table'
                	,columns : 2
                }
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [ <s:property value="imap1.itemsDatosEndoso" /> ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_8_FormLectura',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_8_FormLectura initComponent');
            Ext.apply(this,
            {
                title      : 'Datos de la p&oacute;liza'
                ,model     : '_8_ModeloPoliza'
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items     : [ <s:property value="imap1.itemsPanelLectura" /> ]
            });
            this.callParent();
        }
    });
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    _8_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha'
        ,allowBlank : false
        ,value      : new Date()
        ,name       : 'fecha_endoso'
    });
    _8_panelEndoso   = new _8_PanelEndoso();
    _8_formLectura   = new _8_FormLectura();

    
    _fieldByName('cdplan',_8_panelEndoso).hidden = _TIPOFLOT != "F"? false:true;
    _fieldByName('cdplan',_8_panelEndoso).allowBlank = true;
    _fieldByName('dsplan',_8_formLectura).hidden = _TIPOFLOT != "F"? false:true;
    _fieldByName('NEWCDUNIECO',_8_panelEndoso).value  = _8_smap1.CDUNIECO;
    
	var _8_colsBaseColumns =
	[
	    <s:if test='%{getImap1().get("colsBaseColumns")!=null}'>
	        <s:property value="imap1.colsBaseColumns" escapeHtml="false" />
	    </s:if>
	];
	debug('_8_colsBaseColumns:',_8_colsBaseColumns);
	
	//////////////////////////////////////////////////
	
	////// modelos //////
	
	var _8_modeloGrupoFields =
    [
        'letra'
        ,'nombre'
        ,'cdplan'
    ];
    
    var _8_colsBaseFields =
    [
        <s:if test='%{getImap1().get("colsBaseFields")!=null}'>
            <s:property value="imap1.colsBaseFields" escapeHtml="false" />
        </s:if>
    ];
    debug('_8_colsBaseFields:',_8_colsBaseFields);
    

	var fieldName;
    for(var i=0;i<_8_colsBaseFields.length;i++)
    {
    	fieldName = _8_colsBaseFields[i].name;
    	
    	try{
	    	if(!Ext.isEmpty(fieldName) && fieldName.indexOf("parametros.pv_otvalor") >= 0 ){
	    		  _8_colsBaseFields[i].name = fieldName.replace("parametros.pv_otvalor","pv_otvalor");
	    	}
    	}
    	catch(e){
    		debugError('Error en mapeo de columna',e);
    	}
    	
        _8_modeloGrupoFields.push(_8_colsBaseFields[i].name);
    }
    
    debug('_8_modeloGrupoFields:',_8_modeloGrupoFields);
    
	
	Ext.define('_8_modeloGrupo',
    {
        extend  : 'Ext.data.Model'
        ,fields : _8_modeloGrupoFields
    });
    
    
    _8_storeGrupos = Ext.create('Ext.data.Store',
    {
        model : '_8_modeloGrupo'
        ,proxy   : {
		           type: 'ajax',
		           url : _8_urlCargarGrupos,
		            reader: {
		                type: 'json',
		                root: 'slist1'
		            }
		        }
    });
    
    if( _TIPOFLOT == "F"){
		_8_storeGrupos.load({
			params: {
	            'smap1.cdunieco'  : _8_smap1.cdunieco
	            ,'smap1.cdramo'   : _8_smap1.cdramo
	            ,'smap1.estado'   : _8_smap1.estado
	            ,'smap1.nmpoliza' : _8_smap1.nmpoliza
	        },
		    callback: function(records, operation, success){
		    	if(success){
		    		_8_storeGrupos.sort('letra','ASC');
		    	}else{
		    		mensajeError("Error al cargar los grupos.");
		    	}
		    }
		});    	
    }
    
    var _8_editorNombreGrupo=
	{
	    xtype       : 'textfield'
	    ,allowBlank : false
	    ,readOnly   : true
	    ,minLength  : 3
	};
	
	_8_editorPlan = <s:property value="imap1.editorPlanesColumn" />.editor;
	
    
    _8_tabGruposLinealCols =
    [
        {
            header     : 'ID'
            ,dataIndex : 'letra'
            ,width     : 40
        }
        ,{
            header     : 'NOMBRE'
            ,dataIndex : 'nombre'
            ,width     : 150
            ,editor    : _8_editorNombreGrupo
        }
        ,{
            header     : 'PLAN'
            ,dataIndex : 'cdplan'
            ,width     : 100
            ,editor    : _8_editorPlan
            ,renderer  : function(v)
            {
                return rendererColumnasDinamico(v,'cdplan');
            }
        }
    ];
    
    Ext.Array.each(_8_colsBaseColumns,function(col)
    {
    	try{
	    	if(!Ext.isEmpty(col.dataIndex) && col.dataIndex.indexOf("parametros.pv_otvalor") >= 0 ){
	    		col.dataIndex = col.dataIndex.replace("parametros.pv_otvalor","pv_otvalor");
	    	}
	    	if(col.editor && col.editor.name && !Ext.isEmpty(col.editor.name) && col.editor.name.indexOf("parametros.pv_otvalor") >= 0 ){
				col.editor.name = col.editor.name.replace("parametros.pv_otvalor","pv_otvalor");  	
	    	}
    	}
    	catch(e){
    		debugError('Error en mapeo de columna',e);
    	}
    	
    	col.flex = undefined;
    	col.width = 200;
        _8_tabGruposLinealCols.push(col);
    });
    
    var _panelPlanesEndosoCole =  Ext.create('Ext.grid.Panel',
        {
        	 title    : 'Planes Por Grupo'
			,columns : _8_tabGruposLinealCols
            ,store   : _8_storeGrupos
            ,hidden  : _TIPOFLOT != "F"? true:false
            ,height  : 200
            ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
            {
                clicksToEdit  : 1
                ,errorSummary : false
            })
        });
        
    _8_panelPri=Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_8_divPri'
        ,defaults    :
        {
            style : 'margin : 5px;'
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text     : 'Confirmar endoso'
                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                ,handler : _8_confirmar
            }
        ]
        ,items       :
        [
            _8_formLectura
            ,_panelPlanesEndosoCole
            ,_8_panelEndoso
        ]
    });
    ////// contenido //////
    ///////////////////////
    
    ////////////////////
    ////// loader //////
    _setLoading(true,_8_panelPri);
    Ext.Ajax.request(
    {
    	url      : _8_urlLoaderLectura
    	,params  :
    	{
    		'params.cdunieco'  : _8_smap1.CDUNIECO
    		,'params.cdramo'   : _8_smap1.CDRAMO
    		,'params.estado'   : _8_smap1.ESTADO
    		,'params.nmpoliza' : _8_smap1.NMPOLIZA
    	}
    	,success : function(response)
    	{
    		_setLoading(false,_8_panelPri);
    		var json = Ext.decode(response.responseText);
    		debug('respuesta:',json);
    		if(json.success==true)
    		{
    			_8_formLectura.loadRecord(new _8_ModeloPoliza(json.datosPoliza));
    		}
    	}
        ,failure : function()
        {
        	_setLoading(false,_8_panelPri);
        	mensajeError('Error al cargar los datos de la p&oacute;liza');
        }
    });
    ////// loader //////
    ////////////////////
});

///////////////////////
////// funciones //////
function _8_confirmar()
{
    debug('_8_confirmar');
    
    var valido=true;
    
    if(valido)
    {
        valido=_8_panelEndoso.isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    var motivo = _8_panelEndoso.items.items[1].getValue();
    var coment = _8_panelEndoso.items.items[3].getValue();
    
    if(valido)
    {
    	valido=motivo&&motivo*1>0;
    	if(!valido)
    	{
    		mensajeWarning('El motivo es requerido');
    	}
    }
    
    if(valido)
    {
    	if(motivo==30)//el 30 es el motivo Otros en TRAZREEXP y en TRAZCANC
    	{
    		if(!coment||coment.length==0)
    		{
    			valido = false;
    		}
    	}
    	if(!valido)
    	{
    		mensajeWarning('Si el motivo es "Otros", se debe especificar en el campo comentarios');
    	}
    }
    
    var updateGroups = [];
    var noUpdateGroups = [];
    
    if( _TIPOFLOT == "F"){
    	 _8_storeGrupos.getUpdatedRecords().forEach(function(record,index,arr){
    		updateGroups.push(record.data);
    	});
		
	    debug('Endos Reexpedicion, Grupos a Actualizar: ' , updateGroups);
    }

    if( _TIPOFLOT == "F"){
    	 _8_storeGrupos.each(function(record){
    		if(!record.dirty){
    			noUpdateGroups.push(record.data);
    		}
    	});
		
	    debug('Endos Reexpedicion, Grupos a no Actualizar: ' , noUpdateGroups);
    }
    
    if(valido)
    {
        var json=
        {
            smap1  : _8_smap1
            ,smap2 : _8_panelEndoso.getValues()
            ,smap3 : _8_formLectura.getValues()
            ,slist1: updateGroups
            ,slist2: noUpdateGroups
        }
        
        if(!Ext.isEmpty(_p8_flujo))
        {
            json.flujo = _p8_flujo;
        }
        
        debug('datos que se enviaran:',json);
        _setLoading(true,_8_panelPri);
        Ext.Ajax.request(
        {
            url       : _8_urlGuardar
            ,jsonData : json
            ,success  : function(response)
            {
                _setLoading(false,_8_panelPri);
                json=Ext.decode(response.responseText);
                debug('datos recibidos:',json);
                if(json.success==true)
                {
                    var callbackRemesa = function()
                    {
                        //////////////////////////////////
                        ////// usa codigo del padre //////
                        /*//////////////////////////////*/
                        marendNavegacion(2);
                        /*//////////////////////////////*/
                        ////// usa codigo del padre //////
                        //////////////////////////////////
                    };
                    
                    mensajeCorrecto('Endoso generado',json.mensaje,function()
                    {
                    	
                    	if(json.endosoConfirmado){
                        	_generarRemesaClic(
	                            true
	                            ,_8_smap1.CDUNIECO
	                            ,_8_smap1.CDRAMO
	                            ,_8_smap1.ESTADO
	                            ,_8_smap1.NMPOLIZA
	                            ,callbackRemesa
	                        );
                        }else{
                        	callbackRemesa();
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
                _setLoading(false,_8_panelPri);
                errorComunicacion();
            }
        });
    }
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_8_divPri" style="height:1000px;"></div>