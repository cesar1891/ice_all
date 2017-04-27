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
var _10_smap1 = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false" />;

var _10_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;

var _10_formLectura;
var _10_panelPri;
var _10_panelEndoso;
var _10_fieldFechaEndoso;
var _10_storeAgentes;
var _10_gridAgentes;

var _10_urlGuardar              = '<s:url namespace="/endosos" action="guardarEndosoAgente"       />';
var _10_urlLoadAgentes          = '<s:url namespace="/endosos" action="cargarAgentesEndosoAgente" />';
var _10_urlRecuperacionSimple   = '<s:url namespace="/emision" action="recuperacionSimple"        />';
var _10_urlValidacionSigsAgente = '<s:url namespace="/endosos" action="validacionSigsAgente"      />';

debug('_10_smap1:',_10_smap1);
debug('_10_flujo:',_10_flujo);
////// variables //////
///////////////////////

Ext.onReady(function()
{
	
	// Se aumenta el timeout para todas las peticiones:
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
//	Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
//	Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
//	Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	///////////////////////
	////// overrides //////
	Ext.override(Ext.form.field.ComboBox,
	{
		initComponent : function()
		{
//			debug('Ext.form.field.ComboBox initComponent');
			Ext.apply(this,
			{
				forceSelection : 'true'
			});
			return this.callParent();
		}
	});
	////// overrides //////
	///////////////////////
	
    /////////////////////
    ////// modelos //////
    Ext.define('_10_ModeloAgente',
    {
    	extend  : 'Ext.data.Model'
    	,fields : [ <s:property value="imap1.fieldsModelo" /> ]
    });
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    _10_storeAgentes=Ext.create('Ext.data.Store',
    {
		autoLoad : false
    	,model   : '_10_ModeloAgente'
        ,proxy: {
            type: 'ajax',
            url : _10_urlLoadAgentes,
            reader: {
                type: 'json',
                root: 'slist1'
            }
        }
    });
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    Ext.define('_10_GridAgentes',
    {
    	extend         : 'Ext.grid.Panel'
    	,initComponent : function()
    	{
    		debug('_10_GridAgentes initComponent');
    		Ext.apply(this,
    		{
    			title    : 'Agente(s)'
    			,store   : _10_storeAgentes
    			,columns : [ <s:property value="imap1.columnsGrid" /> ]
    			,tbar:[{
		                text     : 'Cambiar Agente'
		                ,icon    : '${ctx}/resources/fam3icons/icons/group_go.png'
		                ,handler : function(){
		                	if(_10_gridAgentes.getSelectionModel().hasSelection()){
		                		var recordSel = _10_gridAgentes.getSelectionModel().getLastSelected();
		                		
		                		var windowAgente = Ext.create('Ext.window.Window', {
								  title : 'Cambiar Agente ' + recordSel.get('CDAGENTE'),
						          modal:true,
						          width : 350,
						          height : 150,
						          bodyStyle:'padding:5px;',
						          items:[{
						    				xtype: 'displayfield',
						    				fieldLabel: 'Agente actual',
						    				value: recordSel.get('NOMBRE')
						    			},
						    			<s:property value="imap1.comboAgentes" />],
						          buttons:[{
						        	  text: 'Aceptar',
						                 icon:'${ctx}/resources/fam3icons/icons/accept.png',
							                 handler: function() {
							                 	var comboAgente = _fieldByNameDown('NUEVOAGENTE',windowAgente);
							                 	
							                 	if(comboAgente.isValid() && comboAgente.getStore().find('key',comboAgente.getValue()) >= 0 ){
							                 	    var callback = function () {
								                 	    recordSel.set('CDAGENTE',comboAgente.getValue());
								                 	    recordSel.set('NOMBRE',comboAgente.getRawValue());
								                	    if (!recordSel.isModified('CDAGENTE')){
								                		    recordSel.reject();
								                	    }
								                	    windowAgente.close();
								                	};
								                	var mask, ck = 'Validando agente';
								                	try {
								                	    mask = _maskLocal(ck);
								                	    Ext.Ajax.request({
								                	        url     : _10_urlValidacionSigsAgente,
								                	        params  : {
								                	            'params.cdagente' : comboAgente.getValue(),
								                	            'params.cdramo'   : _10_smap1.CDRAMO,
								                	            'params.cdtipsit' : _10_smap1.CDTIPSIT,
								                	            'params.cdtipend' : 'A'
								                	        },
								                	        success : function (response) {
								                	            mask.close();
								                	            var ck = 'Decodificando datos al validar agente';
								                	            try {
								                	                var json = Ext.decode(response.responseText);
								                	                debug('### validar agente:', json, '.');
								                	                if (json.success === true) {
								                	                    callback();
								                	                } else {
								                	                    mensajeError(json.message);
								                	                }
								                	            } catch (e) {
								                	                manejaException(e, ck);
								                	            }
								                	        },
								                	        failure : function () {
								                	            mask.close();
								                	            errorComunicacion(null, 'Error al validar agente');
								                	        }
								                	    });
								                	} catch (e) {
								                	    manejaException(e, ck, mask);
								                	}
							                 	}else{
							                 		comboAgente.reset();
							                 		showMessage("Aviso","Debe seleccionar un agente.", Ext.Msg.OK, Ext.Msg.INFO);
							                 	}
							                 }
						           		},{
						        	  	 text: 'Cancelar',
						                 icon:'${ctx}/resources/fam3icons/icons/cancel.png',
							                 handler: function() {
							                 	windowAgente.close();
							                 }
						           		} ]
						          });
							 	windowAgente.show();
		                	}else{
		                		showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
		                	}
		                	
		                }
		            },'-',{
		                text     : 'Quitar Cambios'
		                ,icon    : '${ctx}/resources/fam3icons/icons/arrow_rotate_anticlockwise.png'
		                ,handler : function(btn,e){
			                	Ext.Msg.show({
		                        title: 'Confirmar acci&oacute;n',
		                        msg   : '&iquest;Esta seguro que desea quitar todos los cambios?',
		                        buttons: Ext.Msg.YESNO,
		                        fn: function(buttonId, text, opt) {
		                            if(buttonId == 'yes') {
		                        		_10_storeAgentes.rejectChanges();    
		                            }
			                    },
		                        animateTarget: btn,
		                        icon: Ext.Msg.QUESTION
		                    });
		                }
		            }
    			]
    		});
    		this.callParent();
    	}
    });
    
    Ext.define('_10_PanelEndoso',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_10_PanelEndoso initComponent');
            Ext.apply(this,
            {
                title     : 'Datos del endoso'
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    _10_fieldFechaEndoso
                ]
            });
            this.callParent();
        }
    });
    
    Ext.define('_10_FormLectura',
    {
        extend         : 'Ext.form.Panel'
        ,initComponent : function()
        {
            debug('_10_FormLectura initComponent');
            Ext.apply(this,
            {
                title      : 'Datos de la p&oacute;liza'
                ,defaults  :
                {
                    style : 'margin : 5px;'
                }
                ,layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,listeners :
                {
                    afterrender : function(me){heredarPanel(me);}
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
    _10_fieldFechaEndoso=Ext.create('Ext.form.field.Date',
    {
        format      : 'd/m/Y'
        ,fieldLabel : 'Fecha de efecto'
        ,allowBlank : false
        ,value      : '<s:property value="smap1.fechaInicioEndoso" />'
        //,readOnly   : true
        ,name       : 'fecha_endoso'
    });
    _10_panelEndoso = new _10_PanelEndoso();
    _10_formLectura = new _10_FormLectura();
    _10_gridAgentes = new _10_GridAgentes();
    
    _10_panelPri=Ext.create('Ext.panel.Panel',
    {
        renderTo     : '_10_divPri'
        ,defaults    :
        {
            style : 'margin : 5px;'
        }
        ,buttonAlign : 'center'
        ,buttons     :
        [
            {
                text      : 'Confirmar endoso'
                ,icon     : '${ctx}/resources/fam3icons/icons/key.png'
                ,handler  : _10_confirmar
            }
        ]
        ,items       :
        [
            _10_formLectura
            ,_10_gridAgentes
            ,_10_panelEndoso
        ]
    });
    ////// contenido //////
    ///////////////////////
    
    ////////////////////
    ////// loader //////
    _setLoading(true,_10_panelPri);
    _10_storeAgentes.load({
		params: {
    		'smap1.cdunieco'  : _10_smap1.CDUNIECO
    		,'smap1.cdramo'   : _10_smap1.CDRAMO
    		,'smap1.estado'   : _10_smap1.ESTADO
    		,'smap1.nmpoliza' : _10_smap1.NMPOLIZA
    		,'smap1.nmsuplem' : _10_smap1.NMSUPLEM
    	},
		callback: function(records, operation, success){
			_setLoading(false,_10_panelPri);
    		if(!success){
    			mensajeError('Error al cargar los Agentes.');
    			//////////////////////////////////
                ////// usa codigo del padre //////
                /*//////////////////////////////*/
                marendNavegacion(4);
                /*//////////////////////////////*/
                ////// usa codigo del padre //////
                //////////////////////////////////
    		}
		}
	});
    
    Ext.Ajax.request(
    {
        url      : _10_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : _10_smap1.CDUNIECO
            ,'smap1.cdramo'       : _10_smap1.CDRAMO
            ,'smap1.estado'       : _10_smap1.ESTADO
            ,'smap1.nmpoliza'     : _10_smap1.NMPOLIZA
            ,'smap1.cdtipsup'     : _10_smap1.cdtipsup
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldByName('fecha_endoso').setMinValue(json.smap1.FECHA_MINIMA);
                _fieldByName('fecha_endoso').setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldByName('fecha_endoso').setReadOnly(json.smap1.EDITABLE=='N');
                _fieldByName('fecha_endoso').isValid();
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
    ////// loader //////
    ////////////////////
});

///////////////////////
////// funciones //////
function _10_confirmar()
{
    debug('_10_confirmar');
    
    var valido=true;
    
    if(valido)
    {
        valido = _10_panelEndoso.isValid();
        if(!valido)
        {
            datosIncompletos();
        }
    }
    
    if(valido)
    {
        /*
        a.cdunieco, a.cdramo, a.estado, a.nmpoliza, a.cdagente, a.nmsuplem, a.status, a.cdtipoag, porredau, a.porparti,nombre,cdsucurs,nmcuadro
        */
		
        var agentes=[];
        _10_storeAgentes.each(function(record){
        	var data = record.getData();
    		if(!Ext.isEmpty(agentes[data.CDAGENTE])){
        		mensajeWarning('Existen agentes repetidos.');
        		valido=false;
        	}
    		agentes[data.CDAGENTE]=data.CDAGENTE;
        });
        
        
        /**
         * SI HAY AGENTES REPETIDOS 
         */
        if(!valido){
        	debug('Existen agentes repetidos.');
        	return;
        }
        
        var slist1=[];
        _10_storeAgentes.getUpdatedRecords().forEach(function(record,index,arr){
        	var data = record.getData();
    		data.CDAGENTEORIG = record.modified.CDAGENTE;
    		slist1.push(data);
        	
        });
        
        if(slist1.length <= 0){
        	mensajeWarning('No hay cambios en los agentes para realizar el endoso.');
        	return;
        }
        
        var json={
            smap1   : _10_smap1
            ,smap2  :
            {
                fecha_endoso : Ext.Date.format(_10_fieldFechaEndoso.getValue(),'d/m/Y')
            }
            ,slist1 : slist1
        }
        
        if(!Ext.isEmpty(_10_flujo))
        {
            json.flujo = _10_flujo;
        }
        
        debug('datos que se enviaran:',json);
        
        var panelMask = new Ext.LoadMask('_10_divPri', {msg:"Confirmando..."});
		panelMask.show();
		
        Ext.Ajax.request(
        {
            url       : _10_urlGuardar
            ,jsonData : json
            ,timeout  : 8*60*1000
            ,success  : function(response)
            {
                panelMask.hide();
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
	                            ,_10_smap1.CDUNIECO
	                            ,_10_smap1.CDRAMO
	                            ,_10_smap1.ESTADO
	                            ,_10_smap1.NMPOLIZA
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
                panelMask.hide();
                errorComunicacion();
            }
        });
    }
};
////// funciones //////
///////////////////////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="_10_divPri" style="height:1000px;"></div>