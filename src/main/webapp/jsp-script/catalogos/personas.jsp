<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Comentarios sobre la modificacion de este archivo --%>

<%-- Con el fin de guardar la pantalla unicamente cuando se han realizado cambios a los datos originales de la pantalla. --%>
	 
<%-- Si se agrega un nuevo campo y se le realiza un setValue (cambia de valor) despues de cargar la forma (loadrecord) 
	 hay que ejecutar la funcion resetOriginalValue() al campo, para considerar el valor cargado como valor orignal y no lo tome 
	 como si se hubiera realizado un cambio en los valores de la pantalla, lo cual mandará a guardar toda la informacion 
	 y actualizara el campo del usuario que actualizó los datos por ultima vez, para evitar esto y guardar unicamente cuando hay cambios
	 y el usuario que modifica hay que ejecutar dicha funcion para que identifique ese valor como inicial. --%>
	 
<%-- Tener cuidado al cambiar la funcion _p22_tipoPersonaChange evitar hacer cambios a campos compartidos entre tipos de personas ya que 
	 se pueden perder valores, o campos ocultos si desde el cofigurador de campos se bloquean u ocultan para algun rol. --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Personas</title>
<style>
.status{
	font-size:14px; 
	font-weight: bold;
}
.green-row .x-grid-cell {
        background-color: #28B058 !important;
}
</style>

<script>

/**
 *	Funciones que se ejecutan desde una pantalla padre.
 */

function obtDatLoaderContratante<s:property value="smap1.idPantalla" />(){
	/**
	 *	Declaracion de Funcion que se ejecutara desde fuera de esta pantalla, y se iguala a una funcion interna propia de este numero de pantalla idPantalla
	 *  Se puede llamar sin ID y con un Id cuando son muchas pantallas
	 *  No es necesario declarar esta funcion afuera
	 */
}

function destruirLoaderContratante<s:property value="smap1.idPantalla" />(){
	/**
	 *	Declaracion de Funcion que se ejecutara desde fuera de esta pantalla, y se iguala a una funcion interna propia de este numero de pantalla idPantalla
	 *  Se puede llamar sin ID y con un Id cuando son muchas pantallas
	 *  No es necesario declarar esta funcion afuera
	 */
}

function callbackDocumentoSubidoPersona(){
	/**
	 *	Declaracion de Funcion que se ejecutara desde fuera de esta pantalla, o desde ventana de carga de documentos y se iguala a una funcion interna
	 *  No es necesario declarar esta funcion afuera de este script
	 */
}


Ext.onReady(function(){
	
/**
 * Se genera un numero unico para generar un Div con nombre unico de la pantalla para poder usarse mas de una instancia en una misma pantalla
 */
var _genDivId  = "_divClientesID" + new Date().getTime();
//document.getElementById("_p22_divpri").setAttribute("id", _genDivId);
Ext.get('_p22_divpri').set({id : _genDivId});


////// overrides Para quitar overrides de paginas padre//////
Ext.override(Ext.form.TextField,
{
    beforeRender:function()
    {
    	Ext.apply(this,
        {
            labelWidth : 100
        });
        return this.callParent();
    }
});

Ext.override(Ext.form.NumberField,
{
    beforeRender:function()
    {
        Ext.apply(this,
        {
            decimalPrecision : 2
        });
        return this.callParent();
    }
});
////// overrides //////


////// variables //////
var _p22_urlObtenerPersonas     = '<s:url namespace="/catalogos"  action="obtenerPersonasPorRFC"              />';
var _p22_urlGuardar             = '<s:url namespace="/catalogos"  action="guardarPantallaPersonas"            />';
var _p22_urlObtenerDomicilio    = '<s:url namespace="/catalogos"  action="obtenerDomicilioPorCdperson"        />';
var _p22_urlObtenerDomicilios   = '<s:url namespace="/catalogos"  action="obtenerDomiciliosPorCdperson"        />';
var _p22_urlTatriperTvaloper    = '<s:url namespace="/catalogos"  action="obtenerTatriperTvaloperPorCdperson" />';
var _p22_urlGuadarTvaloper      = '<s:url namespace="/catalogos"  action="guardarDatosTvaloper"               />';
var _p22_urlPantallaDocumentos  = '<s:url namespace="/catalogos"  action="pantallaDocumentosPersona"          />';
var _p22_urlSubirArchivo        = '<s:url namespace="/"           action="subirArchivoPersona"                />';
var _p22_UrlUploadPro           = '<s:url namespace="/"           action="subirArchivoMostrarBarra"           />';
var _p22_urlViewDoc             = '<s:url namespace="/documentos" action="descargaDocInline"           />';
var _p22_urlCargarNombreArchivo = '<s:url namespace="/catalogos"  action="cargarNombreDocumentoPersona"       />';

var _URL_CARGA_CATALOGO = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';
var _CAT_NACIONALIDAD  = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@NACIONALIDAD"/>';

var _UrlCargaAccionistas   = '<s:url namespace="/catalogos" action="obtieneAccionistas" />';
var _UrlGuardaAccionista   = '<s:url namespace="/catalogos" action="guardaAccionista" />';
var _UrlEliminaAccionistas = '<s:url namespace="/catalogos" action="eliminaAccionistas" />';

var _UrlActualizaStatusPersona = '<s:url namespace="/catalogos" action="actualizaStatusPersona" />';
var _UrlImportaPersonaWS = '<s:url namespace="/catalogos" action="importaPersonaExtWS" />';
var _UrlguardaPersonaWS = '<s:url namespace="/catalogos" action="guardarClienteCompania" />';

var _UrlCargaConfCampos = '<s:url namespace="/catalogos" action="obtieneConfPatallaCli" />';

/* PARA EL LOADER */
var _p22_urlCargarPersonaCdperson = '<s:url namespace="/catalogos" action="obtenerPersonaPorCdperson" />';
/* PARA EL LOADER */

var _RUTA_DOCUMENTOS_PERSONA = '<s:text name="ruta.documentos.persona" />';

var _p22_windowAgregarDocu;

var windowAccionistas = undefined;
var accionistasStore;
var gridAccionistas;
var fieldEstCorp;
var _0_botAceptar;

var _statusDataDocsPersona;

var ultimoValorQueryRFC;


var _DocASubir;

/* PARA LOADER */
var _p22_smap1 = <s:property value='%{convertToJSON("smap1")}' escapeHtml="false" />;
debug('_p22_smap1:',_p22_smap1);


var _RFCsel;
var _RFCnomSel;

var _p22_cdperson = false;
var _p22_tipoPersona;
var _p22_nacionalidad;
var _CDIDEPERsel = '';
var _CDIDEEXTsel = '';
var _esSaludDanios;


var _p22_cdpersonTMP;
var _p22_tipoPersonaTMP;
var _p22_nacionalidadTMP;
var _CDIDEPERselTMP;
var _CDIDEEXTselTMP;
var _esSaludDaniosTMP;

var municipioImportarTMP;
var coloniaImportarTMP;


var _esCargaClienteNvo = false;
var _ocultaBusqueda    = false;
var _cargaCompania;
var _cargaFenacMin;
var _cargaFenacMax;
var _cargaCdPerson;
var _cargaCP;
var _cargaTipoPersona;
var _cargaSucursalEmi;

var _activaCveFamiliar = false;
var _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />;

var _panelTipoPer;

var _nombreComContratante;

var _modoRecuperaDanios;
var _modoSoloEdicion; // no es compatible con _esCargaClienteNvo

var _rolDeAcceso;
var _editaIncompleto;

var _editandoCliente = false;

var _contrantantePrincipal;

var gridDomiciliosCliente;
var storeDomiciliosCliente;

var saveListDom;
var updateListDom;
var deleteListDom;

var _tomarUnDomicilio;
var _domicilioSimple;
var _numDomicilioTomado = undefined;
var _cargaOrdDomicilio;

var _domiciliosNuevos = [];

var _camposAconfigurar = undefined;
var _camposConfigurados = [];

var _RFCduplicado = false;

var _rfcGuardado = 'DUMMYRFCTMP';

var _permiteDuplicarRFC = false;

var _polizaEnEmision = false;

var _modoEdicionDomicilio = false;

if(!Ext.isEmpty(_p22_smap1)){
	
	_cargaCdPerson = _p22_smap1.cdperson;
	
	_esCargaClienteNvo = !Ext.isEmpty(_p22_smap1.esCargaClienteNvo) && _p22_smap1.esCargaClienteNvo == "S" ? true : false ;
	_ocultaBusqueda = !Ext.isEmpty(_p22_smap1.ocultaBusqueda) && _p22_smap1.ocultaBusqueda == "S" ? true : false ;
	_muestraBusqueda= !Ext.isEmpty(_p22_smap1.muestraBusqueda) && _p22_smap1.muestraBusqueda == "S" ? true : false ;
	_cargaCompania  = _p22_smap1.esSaludDanios;	
	_cargaFenacMin = _p22_smap1.cargaFenacMin;
	_cargaFenacMax = _p22_smap1.cargaFenacMax;
	_cargaCP = _p22_smap1.cargaCP;	
	_cargaTipoPersona = _p22_smap1.cargaTipoPersona;
	_cargaSucursalEmi = _p22_smap1.cargaSucursalEmi;
	
	_activaCveFamiliar = !Ext.isEmpty(_p22_smap1.activaCveFamiliar) && _p22_smap1.activaCveFamiliar == "S" ? true : false ;
	_modoRecuperaDanios = !Ext.isEmpty(_p22_smap1.modoRecuperaDanios) && _p22_smap1.modoRecuperaDanios == "S" ? true : false ;
	_modoSoloEdicion = !Ext.isEmpty(_p22_smap1.modoSoloEdicion) && _p22_smap1.modoSoloEdicion == "S" ? true : false ;

	_contrantantePrincipal = !Ext.isEmpty(_p22_smap1.contrantantePrincipal) && _p22_smap1.contrantantePrincipal == "S" ? true : false ;
	
	_rolDeAcceso = _p22_smap1.cveModo;
	_editaIncompleto = (_rolDeAcceso != "EDITAALL")?true:false;
	
	_tomarUnDomicilio = !Ext.isEmpty(_p22_smap1.tomarUnDomicilio) && _p22_smap1.tomarUnDomicilio == "S" ? true : false ;
	_domicilioSimple = !Ext.isEmpty(_p22_smap1.domicilioSimple) && _p22_smap1.domicilioSimple == "S" ? true : false ;
	_polizaEnEmision = !Ext.isEmpty(_p22_smap1.polizaEnEmision) && _p22_smap1.polizaEnEmision == "S" ? true : false ;
	_cargaOrdDomicilio= _p22_smap1.cargaOrdDomicilio;
	_modoEdicionDomicilio = !Ext.isEmpty(_p22_smap1.modoEdicionDomicilio) && _p22_smap1.modoEdicionDomicilio == "S" ? true : false ;
}

if(_polizaEnEmision){
	_tomarUnDomicilio = true;
	_domicilioSimple = true
}

var _URL_urlCargarTvalosit   = '<s:url namespace="/emision"    action="cargarValoresSituacion"    />';
////// variables //////
	
	if(Ext.isEmpty(_p22_smap1)){
		mensajeWarning('No hay parametros de acceso a clientes.');
		return;
	}
	
	// Se aumenta el timeout para todas las peticiones:
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
	Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
	Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	////// modelos //////
	Ext.define('_p22_modeloGrid',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap.gridModelFields" /> ]
	});
	
	////// modelos //////
	
	Ext.define('DomiciliosModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'boolean', name:'SEL', persist: false},
              {type:'string', name:'NMORDDOM'},
              {type:'string', name:'SWACTIVO'},
              {type:'string', name:'CDTIPDOM'},
              {type:'string', name:'DSTIPDOM'},
              {type:'string', name:'CODPOSTAL' },
              {type:'string', name:'CDEDO' },
              {type:'string', name:'ESTADO' },
              {type:'string', name:'CDMUNICI' },
              {type:'string', name:'MUNICIPIO' },
              {type:'string', name:'CDCOLONI' },
              {type:'string', name:'COLONIA' },
              {type:'string', name:'DSDOMICI' },
              {type:'string', name:'NMNUMERO' },
              {type:'string', name:'NMNUMINT' },
              {type:'string', name:'DOMTOM' }
        ]
    });
    // Store
    storeDomiciliosCliente = new Ext.data.Store({
        model: 'DomiciliosModel',
        proxy: {
           type: 'ajax',
           url : _p22_urlObtenerDomicilios,
            reader: {
                type: 'json',
                root: 'slist1'
            }
        }
    });
    
    
    // GRID PARA DOMICILIOS DEL COTRATANTE
    gridDomiciliosCliente = Ext.create('Ext.grid.Panel', {
        width   : 800,
        store   : storeDomiciliosCliente,
        autoScroll:true,
        columns: [
        	{text:'Pol', dataIndex:'SEL', xtype : 'checkcolumn', width:30, sortable:false,hidden: !_tomarUnDomicilio , disabled: (!Ext.isEmpty(_cargaOrdDomicilio) && _polizaEnEmision)
        		,listeners : {
        			checkchange: function (chk, rowIndex, checked, eOpts){
	    		        
        				_numDomicilioTomado = storeDomiciliosCliente.getAt(rowIndex);
//        				gridDomiciliosCliente.getView().addRowCls(_numDomicilioTomado,'green-row');
        				
	            		storeDomiciliosCliente.each(function(record, index){
            				if(index != rowIndex){
								record.set('SEL',false);	      					
            					
            					if(_esSaludDanios == 'S'){
            						mensajeWarning('Domicilio seleccionado para efectos de la p&oacute;liza actual. Guarde los datos de Persona.');
            						try{
            							_contratanteSaved =  false;
            						}catch(e){
            								debug('no existe _contratanteSaved en pantalla padre');
            						}
            					}
//            					gridDomiciliosCliente.getView().removeRowCls(record,'green-row');
	            			}
	            			
	            		});
	            		
        			},
        			beforecheckchange: function (chk, rowIndex, checked, eOpts){
							if(!checked)return false;        			
        			}
        		}
        	},
            {text:'No.', dataIndex:'NMORDDOM', width:30, align:'left', sortable:false},
            {text:'Tipo', dataIndex:'DSTIPDOM', width:100, align:'left', sortable:false},
            {text:'CP.', dataIndex:'CODPOSTAL', width:60, align:'left', sortable:false},
            {text:'Estado', dataIndex:'ESTADO', width:120, align:'left', sortable:false},
            {text:'Municipio', dataIndex:'MUNICIPIO', width:120, align:'left', sortable:false},
            {text:'Colonia', dataIndex:'COLONIA', width:150, align:'left', sortable:false},
            {text:'Domicilio', dataIndex:'DSDOMICI', width:140, align:'left', sortable:false},
            {text:'Activo', dataIndex:'SWACTIVO', width:50, align:'left', sortable:false}
//            {text:'No. Ext', dataIndex:'NMNUMERO', width:60, align:'left', sortable:false}
//            {text:'No. Int', dataIndex:'NMNUMINT', width:60, align:'left', sortable:false}
        ],
        tbar:[{
				text: 'Agregar Domicilio',
				icon: '${ctx}/resources/fam3icons/icons/report_add.png',
				name: 'AgregaDomBtn',
				handler: function(){
					
					if(_domicilioSimple && storeDomiciliosCliente.getCount() > 0){
						mensajeWarning('No se pueden agregar mas domicilios.');
						return;
					}
						
					agregaEditaDomicilio(false,null);
				}
			},{
				text: 'Editar Domicilio',
				icon: '${ctx}/resources/fam3icons/icons/report_edit.png',
				handler: function(){
					
                var model =  gridDomiciliosCliente.getSelectionModel();
                if(model.hasSelection()){
                    var recordSel = model.getLastSelected();
                    agregaEditaDomicilio(true,recordSel);
                }else{
                    showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
                }
            
				}
			},/*'-',{
				text: 'Tomar Domicilio',
				tooltip: 'Tomar Domicilio para efecto de la p&oacute;liza',
				icon: '${ctx}/resources/fam3icons/icons/award_star_gold_2.png',
				name: 'TomaDomBtn',
				hidden: !_tomarUnDomicilio || _domicilioSimple,
				handler: function(){
					
	                var model =  gridDomiciliosCliente.getSelectionModel();
	                if(model.hasSelection()){
	                    var recordSel = model.getLastSelected();
	                    
				    	var confirm = Ext.Msg.show({
	    		            title: 'Confirmar acci&oacute;n',
	    		            msg: '&iquest;Desea tomar este domicilio para efectos de la p&oacute;liza?',
	    		            buttons: Ext.Msg.YESNO,
	    		            fn: function(buttonId, text, opt) {
	    		            	if(buttonId == 'yes') {

	    		            		_numDomicilioTomado = recordSel;
	    		            		
	    		            		storeDomiciliosCliente.each(function(record, index){
	    		            			gridDomiciliosCliente.getView().removeRowCls(record,'green-row');
	    		            		});
	    		            		
	    		            		gridDomiciliosCliente.getView().addRowCls(recordSel,'green-row');
	    		            		gridDomiciliosCliente.focus();
	    		            		
	    		            		mensajeWarning('Domicilio Tomado. Debe guardar los datos de Persona.');
	    		            		
	    		            	}else{
	    		            		gridDomiciliosCliente.focus();
	    		            		return;
	    		            	}
	            			},
	    		            icon: Ext.Msg.QUESTION
	        			});
	                
	                }else{
	                    showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
	                }
            
				}
			},*/'-',{
				text: 'Cambiar Estatus Domicilio',
				icon: '${ctx}/resources/fam3icons/icons/flag_blue.png',
				name: 'EstatusDomBtn',
				hidden: true,
				handler: function(){
					
	                var model =  gridDomiciliosCliente.getSelectionModel();
	                if(model.hasSelection()){
	                    var recordSel = model.getLastSelected();
	                    
	                    if(Ext.isEmpty(recordSel.get("NMORDDOM"))){
	                    	mensajeWarning('No se puede cambiar el estatus de un domicilio que aun no se ha guardado.');
	                    	return;
	                    }
	                    
	                    var estatusActivo = recordSel.get("SWACTIVO") == "N"?false:true; 
	                    
	                    var mensaje = !estatusActivo?'&iquest;Est&aacute; seguro que desea cambiar a estatus activo este domicilio?' 
	                    : '&iquest;Est&aacute; seguro que desea cambiar a estatus inactivo este domicilio?'
	                    
				    	var confirm = Ext.Msg.show({
	    		            title: 'Confirmar acci&oacute;n',
	    		            msg: mensaje,
	    		            buttons: Ext.Msg.YESNO,
	    		            fn: function(buttonId, text, opt) {
	    		            	if(buttonId == 'yes') {
	    		            		recordSel.set("SWACTIVO",estatusActivo?"N":"S");
	    		            	}else{
	    		            		return;
	    		            	}
	            			},
	    		            icon: Ext.Msg.QUESTION
	        			});
	                
	                }else{
	                    showMessage("Aviso","Debe seleccionar un registro", Ext.Msg.OK, Ext.Msg.INFO);
	                }
            
				}
			}]
        
    });
	
	
	////// contenido //////
	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" /> = Ext.create('Ext.panel.Panel',{
		renderTo  : _genDivId
		,defaults : { style : 'margin:5px;' }
	    ,border   : 0
	    ,items    :
	    [
	        Ext.create('Ext.form.Panel',
	        {
	        	 title        : _modoSoloEdicion? "Escriba el RFC de la persona a buscar y de clic en Continuar." : "Escriba y seleccione el RFC de la persona a Editar, &oacute; de clic en el bot&oacute;n Agregar para una nueva."
	        	 ,name      : '_p22_formBusqueda'
	        	 ,hidden      : !Ext.isEmpty(_cargaCdPerson)
	        	 ,defaults    : { style : 'margin:5px;' }
	        	 ,items       : [{
	        	 					xtype      : 'fieldcontainer',
						            fieldLabel : 'Tipo de Compa&ntilde;ia',
						            defaultType: 'radiofield',
						            id        : 'companiaGroupId',
						            border: true,	
									defaults : { style : 'margin:5px;' },
						            layout: 'hbox',
						            items: [
						                {
						                    boxLabel  : 'General de Seguros',
						                    name      : 'smap1.esSalud',
						                    inputValue: 'D',
						                    checked   : true,
						                    id        : 'companiaId',
						                    listeners: {
			        	 						change: function(){
			        	 								var form=_p22_formBusqueda();
			        	 								form.down('[name=smap1.rfc]').reset();
						                        		form.down('[name=smap1.nombre]').reset();
			        	 								form.down('[name=smap1.rfc]').getStore().removeAll();
						                        		form.down('[name=smap1.nombre]').getStore().removeAll();
			        	 						}
			        	 					}
						                }, {
						                    boxLabel  : 'General de Salud',
						                    name      : 'smap1.esSalud',
						                    inputValue: 'S',
						                    checked   : false
						                }
						            ]},{
	        	 				xtype: 'combobox',
								fieldLabel:'B&uacute;squeda por RFC',
								labelWidth: 100,
								width:    800,
								queryParam  : 'smap1.rfc',
								queryMode   : 'remote',
								queryCaching: false,
								allQuery    : 'dummyForAllQuery',
            					minChars    : 9,
								minLength   : 2,
//								queryDelay  : 2000,
								name          : 'smap1.rfc',
					            valueField    : 'CDRFC',
					            displayField  : 'NOMBRE_COMPLETO',
					            autoSelect    : false,
					            hideTrigger   : true,
					            tpl: Ext.create('Ext.XTemplate',
					                    '<tpl for=".">',
					                        '<div class="x-boundlist-item">{CDRFC} - {DSNOMBRE} {DSNOMBRE1} {DSAPELLIDO} {DSAPELLIDO1} - {DIRECCIONCLI}</div>',
					                    '</tpl>'
					            ),
					            enableKeyEvents: true,
					            listeners: {
					            	select: function(comb, records){
					            		_RFCsel = records[0].get('CDRFC');
					            		_p22_cdpersonTMP = records[0].get('CDPERSON');
					            		_p22_tipoPersonaTMP = records[0].get('OTFISJUR');
					            		_p22_nacionalidadTMP = records[0].get('CDNACION');
					            		
					            		_CDIDEPERselTMP = records[0].get('CDIDEPER');
					            		_CDIDEEXTselTMP = records[0].get('CDIDEEXT');
					            		_esSaludDaniosTMP = Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].getGroupValue(); 
					            		
					            		var form=_p22_formBusqueda();
					            		form.down('[name=smap1.nombre]').reset();
					            		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setText(_modoRecuperaDanios?'Continuar y Recuperar Cliente':'Continuar y Editar Cliente');
					            		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).enable();
					            	}, 
					            	keydown: function( com, e, eOpts ){
					            		var form=_p22_formBusqueda();
					            		
					            		if(e.isSpecialKey() || e.isNavKeyPress() || e.getKey() == e.CONTEXT_MENU || e.getKey() == e.HOME || e.getKey() == e.NUM_CENTER 
						            		|| e.getKey() == e.F1 || e.getKey() == e.F2 || e.getKey() == e.F3 || e.getKey() == e.F4 || e.getKey() == e.F5 || e.getKey() == e.F6 
						            		|| e.getKey() == e.F7 || e.getKey() == e.F8 || e.getKey() == e.F9 || e.getKey() == e.F10 || e.getKey() == e.F11 || e.getKey() == e.F12){
						            			if(e.getKey() == e.BACKSPACE){
						            				form.down('[name=smap1.rfc]').getStore().removeAll();
						            			}
					            		}
					            		
					            	},
					            	change: function(me, val, oldVal, eopts){
						    				try{
							    				if('string' == typeof val){
							    					if(String(val.toUpperCase()).indexOf(" -") != -1){
							    						me.setValue(ultimoValorQueryRFC);
							    					}else {
							    						ultimoValorQueryRFC = val.toUpperCase();
							    						me.setValue(ultimoValorQueryRFC);
							    					}
							    				}
						    				}
						    				catch(e){
						    					debug(e);
						    				}
									},
									beforequery: function( queryPlan, eOpts ){
									
										queryPlan.query = Ext.String.trim(queryPlan.query);
										if(String(queryPlan.query).indexOf(" -") != -1){
//											queryPlan.combo.getStore().removeAll();
//											debug("Cambiando el QueryPlan a: ",ultimoValorQueryRFC);
											queryPlan.query = ultimoValorQueryRFC;
//											queryPlan.rawQuery=true;
//											queryPlan.cancel = true;
//											debug("CAncelado!");
//											debug("haciendo query para: ", ultimoValorQueryRFC);
//											queryPlan.combo.doQuery(ultimoValorQueryRFC);
//											queryPlan.combo.setValue(ultimoValorQueryRFC,false);
//											queryPlan.combo.expand();
										}
									}
					            },
					            store         : Ext.create('Ext.data.Store', {
					                model     : '_p22_modeloGrid', 
					                proxy     : {
				                            type        : 'ajax'
				                            ,url        : _p22_urlObtenerPersonas
				                            ,reader     :
				                            {
				                                type  : 'json'
				                                ,root : 'slist1'
				                            }
				                        }
				                        ,listeners: {
				                        	beforeload: function( store, operation, eOpts){
				                        		operation.callback = function(records, op, succ){
				                        			if(succ){
					                        			
					                        			var jsonResponse = Ext.decode(op.response.responseText);

					                        			if(!jsonResponse.exito){
					                        				mensajeError('Error al hacer la consulta, Favor de Reintentar');
					                        				var form=_p22_formBusqueda();
						            						form.down('[name=smap1.rfc]').reset();
					                        				form.down('[name=smap1.nombre]').reset();
					                        			}
					                        			
					                        		}else{
					                        			
					                        				mensajeError('Error al hacer la consulta. Error de comunicaci&oacute;n, favor de reintentar');
					                        				var form=_p22_formBusqueda();
						            						form.down('[name=smap1.rfc]').reset();
					                        				form.down('[name=smap1.nombre]').reset();
					                        		}
				                        		};
				                        		operation.params['smap1.esSalud'] = Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].getGroupValue(); //SALUD o DAÑOS
				                        		operation.params['smap1.validaTienePoliza'] = _esCargaClienteNvo?'S':'N';
				                        		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).disable();
				                        		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].disable();
				                        	},
				                        	load: function(store, records, successful, eOpts){
				                        		var form=_p22_formBusqueda();
				                        		
				                        		form.down('[name=smap1.rfc]').setRawValue(form.down('[name=smap1.rfc]').getValue());
				                        		
				                        		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].enable();
				                        		
				                        	}
				                        }
					            })
								},
								{
								xtype: 'combobox',
								fieldLabel:'B&uacute;squeda por Nombre',
								labelWidth: 100,
								width:    800,
								queryParam  : 'smap1.nombre',
								queryMode   : 'remote',
								queryCaching: false,
								allQuery    : 'dummyForAllQuery',
            					minChars    : 2,
								minLength   : 2,
								name          : 'smap1.nombre',
					            valueField    : 'CDRFC',
					            displayField  : 'NOMBRE_COMPLETO',
					            forceSelection: false,
					            autoSelect    : false,
	                            typeAhead     : false,
					            anyMatch      : false,
					            hideTrigger   : true,
					            tpl: Ext.create('Ext.XTemplate',
					                    '<tpl for=".">',
					                        '<div class="x-boundlist-item">{CDRFC} - {DSNOMBRE} {DSNOMBRE1} {DSAPELLIDO} {DSAPELLIDO1} - {DIRECCIONCLI}</div>',
					                    '</tpl>'
					            ),
					            enableKeyEvents: true,
					            listeners: {
					            	select: function(comb, records){
					            		_RFCnomSel = records[0].get('CDRFC');
					            		_p22_cdpersonTMP = records[0].get('CDPERSON');
					            		_p22_tipoPersonaTMP = records[0].get('OTFISJUR');
					            		_p22_nacionalidadTMP = records[0].get('CDNACION');
					            		
					            		_CDIDEPERselTMP = records[0].get('CDIDEPER');
					            		_CDIDEEXTselTMP = records[0].get('CDIDEEXT');
					            		_esSaludDaniosTMP = Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].getGroupValue(); 
					            		
					            		var form=_p22_formBusqueda();
					            		form.down('[name=smap1.rfc]').reset();
					            		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setText(_modoRecuperaDanios?'Continuar y Recuperar Cliente':'Continuar y Editar Cliente');
					            		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).enable();
					            	}, 
					            	keydown: function( com, e, eOpts ){
					            		var form=_p22_formBusqueda();
					            		
					            		if(e.isSpecialKey() || e.isNavKeyPress() || e.getKey() == e.CONTEXT_MENU || e.getKey() == e.HOME || e.getKey() == e.NUM_CENTER 
						            		|| e.getKey() == e.F1 || e.getKey() == e.F2 || e.getKey() == e.F3 || e.getKey() == e.F4 || e.getKey() == e.F5 || e.getKey() == e.F6 
						            		|| e.getKey() == e.F7 || e.getKey() == e.F8 || e.getKey() == e.F9 || e.getKey() == e.F10 || e.getKey() == e.F11 || e.getKey() == e.F12){
						            			if(e.getKey() == e.BACKSPACE){
						            				form.down('[name=smap1.nombre]').getStore().removeAll();
						            			}
					            		}else{
					            			_RFCnomSel = '';
						            		form.down('[name=smap1.rfc]').reset();
						            		form.down('[name=smap1.nombre]').getStore().removeAll();
					            			
					            			_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setText(_modoSoloEdicion?'Seleccione...' : (_modoRecuperaDanios?'Continuar y Recuperar Cliente':'Continuar y Editar Cliente'));
					            		}
					            	},
					            	change: function(me, val){
						    				try{
							    				if('string' == typeof val){
							    					me.setValue(val.toUpperCase());
							    				}
						    				}
						    				catch(e){
						    					debug(e);
						    				}
									}
					            },
					            store         : Ext.create('Ext.data.Store', {
					                model     : '_p22_modeloGrid',
					                proxy     : {
				                            type        : 'ajax'
				                            ,url        : _p22_urlObtenerPersonas
				                            ,reader     :
				                            {
				                                type  : 'json'
				                                ,root : 'slist1'
				                            }
				                        }
				                        ,listeners: {
				                        	beforeload: function( store, operation, eOpts){
				                        		operation.callback = function(records, op, succ){
				                        			if(succ){
					                        			var jsonResponse = Ext.decode(op.response.responseText);
					                        			if(!jsonResponse.exito){
					                        				mensajeError('Error al hacer la consulta, Favor de Reintentar');	
					                        				var form=_p22_formBusqueda();
						            						form.down('[name=smap1.rfc]').reset();
					                        				form.down('[name=smap1.nombre]').reset();
					                        			}
				                        			}else{
				                        				mensajeError('Error al hacer la consulta. Error de comunicaci&oacute;n, favor de reintentar');	
				                        				var form=_p22_formBusqueda();
					            						form.down('[name=smap1.rfc]').reset();
				                        				form.down('[name=smap1.nombre]').reset();
				                        			}
				                        		};
				                        		operation.params['smap1.esSalud'] = Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].getGroupValue(); //SALUD o DAÑOS
				                        		operation.params['smap1.validaTienePoliza'] = _esCargaClienteNvo?'S':'N';
				                        		_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).disable();
				                        		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].disable();
				                        	},
				                        	load: function(store, records, successful, eOpts){
				                        		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].enable();
				                        		
				                        	}
				                        }
					            })
								},{
										xtype      : 'hidden',
										name       : 'smap1.snombre',
										value      : ' '
									},
									{
										xtype      : 'hidden',
										name       : 'smap1.apat',
										value      : ' '
									},{
										xtype      : 'hidden',
										name       : 'smap1.amat',
										value      : ' '
									}
								]
	        	 ,buttonAlign : 'center'
	        	 ,buttons     :
	        	 [
	        	     {
                         text     : _modoSoloEdicion?'Seleccione...' : (_modoRecuperaDanios?'Continuar y Recuperar Cliente':'Continuar y Editar Cliente')
                         ,xtype   : 'button'
                         ,name  : 'btnContinuarId'
                         ,disabled: true
                         ,icon    : '${ctx}/resources/fam3icons/icons/building_go.png'
                         ,handler : function (){
										var form = _p22_formBusqueda();
										
										var valorRFC = form.down('[name=smap1.rfc]').getValue();
										var valorRFCRaw = form.down('[name=smap1.rfc]').getRawValue();
										
										debug('valorRFC: ',valorRFC);
										debug('valorRFCRaw: ',valorRFCRaw);
										
										var valorNombre = form.down('[name=smap1.nombre]').getValue();
										var valorNombreRaw = form.down('[name=smap1.nombre]').getRawValue();
										
										debug('valorNombre: ',valorNombre);
										debug('valorNombreRaw: ',valorNombreRaw);
										
										/**
										 * PARA PROBLEMA CUANDO AVECES NO SE SELECCIONA CORRECTAMENTE
										 */
										
										if(Ext.isEmpty(valorRFC) && Ext.isEmpty(valorNombre) && Ext.isEmpty(valorRFCRaw) && Ext.isEmpty(valorNombreRaw)){
											mensajeWarning('Llene la informaci&oacute;n solicitada para continuar.');
											return;
										}
										
										if((!Ext.isEmpty(valorRFCRaw) && Ext.isEmpty(valorRFC)) || (!Ext.isEmpty(valorNombreRaw) && Ext.isEmpty(valorNombre))){
											mensajeWarning('Hubo un problema al seleccionar el Cliente. Vuelva a intentarlo.');
											form.down('[name=smap1.rfc]').reset();
								    		form.down('[name=smap1.nombre]').reset();
								    		form.down('[name=smap1.rfc]').getStore().removeAll();
								    		form.down('[name=smap1.nombre]').getStore().removeAll();
								    		return;
										}
										
										debug('valorRFC:',valorRFC);
										debug('valorNombre:',valorNombre);
										debug('_RFCsel:',_RFCsel);
										debug('_RFCnomSel:',_RFCnomSel);
										
										_nombreComContratante = '';
										if(Ext.isEmpty(valorRFC)){
											_nombreComContratante = form.down('[name=smap1.nombre]').getRawValue();
										}else{
											_nombreComContratante = form.down('[name=smap1.rfc]').getRawValue();
										}
										
										if( (!Ext.isEmpty(_RFCsel)&&(_RFCsel == valorRFC)) || ((!Ext.isEmpty(_RFCnomSel))&&(_RFCnomSel == valorNombre))){
											_p22_formDatosGenerales().getForm().reset();
											
											//multidomicilio
											if(storeDomiciliosCliente)storeDomiciliosCliente.removeAll();
											
											_p22_formDatosGenerales().hide();
								    		
								    		//multidomicilio
								    		_p22_formDomicilio().hide();
								    		_p22_principalDatosAdicionales().hide();
								    		
											_p22_cdperson = _p22_cdpersonTMP;
											_p22_tipoPersona = _p22_tipoPersonaTMP;
											_p22_nacionalidad = _p22_nacionalidadTMP;
											_CDIDEPERsel = _CDIDEPERselTMP;
											_CDIDEEXTsel = _CDIDEEXTselTMP;
											_esSaludDanios = _esSaludDaniosTMP;
											municipioImportarTMP = '';
											coloniaImportarTMP = '';
											
								    		//Si el la persona es proveniente de WS, primero se genera la persona y se inserta los datos del WS para luego ser editada
								    		if("1" == _p22_cdperson){
								    			importaPersonaWS( _esSaludDanios , (_esSaludDanios == 'S') ? _CDIDEEXTsel : _CDIDEPERsel );
								    			return;
								    		}
								    		
								    		form.down('[name=smap1.rfc]').reset();
								    		form.down('[name=smap1.nombre]').reset();
								    		form.down('[name=smap1.rfc]').getStore().removeAll();
								    		form.down('[name=smap1.nombre]').getStore().removeAll();
								    		
								    		
											irModoEdicion();
											
										}else if(!Ext.isEmpty(valorRFC)){
											
											mensajeWarning('Seleccione una persona.');
											return;
											
										}else if(!Ext.isEmpty(valorNombre)){
											
											if(!Ext.isEmpty(_nombreComContratante) && !Ext.isEmpty(valorNombre) && _nombreComContratante != valorNombre){
												mensajeWarning('Hubo un problema al seleccionar el Cliente. Vuelva a intentarlo.');
												form.down('[name=smap1.nombre]').reset();
												form.down('[name=smap1.nombre]').getStore().removeAll();
											}else{
												mensajeWarning('Seleccione un cliente de la b&uacute;squeda.');
											}
											
											return;
										}
									}
                     },{
                         text     : 'Agregar Nuevo Cliente'
                         ,xtype   : 'button'
                         ,name    : 'btnAgregarCliId'
                         ,hidden  : _modoSoloEdicion
                         ,icon    : '${ctx}/resources/fam3icons/icons/building_add.png'
                         ,handler : function (){
                         	
                         				var form = _p22_formBusqueda();
										
										form.down('[name=smap1.nombre]').reset();
										form.down('[name=smap1.rfc]').getStore().removeAll();
										
										_p22_formDatosGenerales().getForm().reset();
									
										if(storeDomiciliosCliente)storeDomiciliosCliente.removeAll();
										
										_p22_formDatosGenerales().hide();
										_p22_formDomicilio().hide();
										_p22_principalDatosAdicionales().hide();
										
										_RFCsel = '';
										_p22_cdperson = '';
										_p22_tipoPersona = '';
										_p22_nacionalidad = '';
										_CDIDEPERsel = '';
										_CDIDEEXTsel = '';
										municipioImportarTMP = '';
										coloniaImportarTMP = '';
										
										form.down('[name=smap1.rfc]').reset();
										form.down('[name=smap1.nombre]').reset();
										form.down('[name=smap1.rfc]').getStore().removeAll();
										form.down('[name=smap1.nombre]').getStore().removeAll();
										
										_esSaludDanios = Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].getGroupValue();
										
										irModoAgregar();
                     }
                 }]
	        })
	        ,Ext.create('Ext.form.Panel',
	        	    {
	        	    	title     : 'Datos generales de la Persona'
	        	    	,name   : '_p22_formDatosGenerales'
                        ,border   : 0
	        	    	,defaults : { style : 'margin:5px'}
	        	        ,layout   :
	        	        {
	        	        	type     : 'table'
	        	        	,columns : 3
	        	        }
	        	        ,trackResetOnLoad: true
	        	    	,items    : [ <s:property value="imap.datosGeneralesItems" escapeHtml="false" /> ]
	        	    })
	        	    ,Ext.create('Ext.form.Panel',
                    {
                        title     : 'Domicilios de la Persona'
                        ,name   : '_p22_formDomicilio'
                        ,border   : 0
                        ,defaults : { style : 'margin:5px' }
                        ,layout   :
                        {
                            type     : 'table'
                            ,columns : 3
                        }
                        ,items    : [gridDomiciliosCliente]
                    })
                    ,Ext.create('Ext.form.Panel',
                    {
                        title   : 'Datos adicionales de la Persona'
                    	,name : '_p22_principalDatosAdicionales'
                        ,border   : 0
                        ,buttonAlign: 'center'
                        ,buttons    :
	                    [{
	                            text     : _modoRecuperaDanios?'Guardar datos y Recuperar Persona':'Guardar datos de Persona'
	                            ,name  : '_p22_botonGuardar'
	                            ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
	                            ,handler : function()
	                            {
	                            	if(typeof inputCdramo === 'undefined')
	                            	{
	                            		if(!_p22_formDatosGenerales().isValid()){
                                            mensajeWarning('Favor de verificar los datos generales del cliente.');
                                            return;
                                        }
                                        if(_RFCduplicado){
                                            if(_esCargaClienteNvo || !_permiteDuplicarRFC){
                                                if(_esCargaClienteNvo){
                                                    mensajeWarning('La persona para el RFC ingresado ya existe como cliente. Favor de volver a realizar la cotizaci&oacute;n como cliente existente.');
                                                }else{
                                                    mensajeWarning('El RFC ingresado ya existe registrado para otro Cliente.');
                                                }
                                                return;
                                            }
                                            var confirm = Ext.Msg.show({
                                                title: 'Aviso',
                                                msg: 'El RFC ingresado ya existe registrado para otro Cliente. &iquest;Desea Duplicar la Persona?',
                                                buttons: Ext.Msg.YESNO,
                                                fn: function(buttonId, text, opt) {
                                                    if(buttonId == 'yes') {
                                                        _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);
                                                    }else{
                                                        return;
                                                    }
                                                },
                                                icon: Ext.Msg.QUESTION
                                            });
                                        }else{
                                            
                                            _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);                                   
                                        }
	                            	}
	                            	else
	                            	{
	                            		if(!Ext.isEmpty(inputCdramo))
	                            		{
		                            		if(inputCdramo==16)
		                            		{
				                            	checarBenef(function()
				                            	{
				                            		if(!_p22_formDatosGenerales().isValid()){
				                                        mensajeWarning('Favor de verificar los datos generales del cliente.');
				                                        return;
				                                    }
				                                    if(_RFCduplicado){
				                                        if(_esCargaClienteNvo || !_permiteDuplicarRFC){
				                                            if(_esCargaClienteNvo){
				                                                mensajeWarning('La persona para el RFC ingresado ya existe como cliente. Favor de volver a realizar la cotizaci&oacute;n como cliente existente.');
				                                            }else{
				                                                mensajeWarning('El RFC ingresado ya existe registrado para otro Cliente.');
				                                            }
				                                            return;
				                                        }
				                                        var confirm = Ext.Msg.show({
				                                            title: 'Aviso',
				                                            msg: 'El RFC ingresado ya existe registrado para otro Cliente. &iquest;Desea Duplicar la Persona?',
				                                            buttons: Ext.Msg.YESNO,
				                                            fn: function(buttonId, text, opt) {
				                                                if(buttonId == 'yes') {
				                                                    _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);
				                                                }else{
				                                                    return;
				                                                }
				                                            },
				                                            icon: Ext.Msg.QUESTION
				                                        });
				                                    }else{
				                                        
				                                        _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);                                   
				                                    }
				                            	});
				                           }
		                            		else
		                            		{
		                            			if(!_p22_formDatosGenerales().isValid()){
	                                                mensajeWarning('Favor de verificar los datos generales del cliente.');
	                                                return;
	                                            }
	                                            if(_RFCduplicado){
	                                                if(_esCargaClienteNvo || !_permiteDuplicarRFC){
	                                                    if(_esCargaClienteNvo){
	                                                        mensajeWarning('La persona para el RFC ingresado ya existe como cliente. Favor de volver a realizar la cotizaci&oacute;n como cliente existente.');
	                                                    }else{
	                                                        mensajeWarning('El RFC ingresado ya existe registrado para otro Cliente.');
	                                                    }
	                                                    return;
	                                                }
	                                                var confirm = Ext.Msg.show({
	                                                    title: 'Aviso',
	                                                    msg: 'El RFC ingresado ya existe registrado para otro Cliente. &iquest;Desea Duplicar la Persona?',
	                                                    buttons: Ext.Msg.YESNO,
	                                                    fn: function(buttonId, text, opt) {
	                                                        if(buttonId == 'yes') {
	                                                            _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);
	                                                        }else{
	                                                            return;
	                                                        }
	                                                    },
	                                                    icon: Ext.Msg.QUESTION
	                                                });
	                                            }else{
	                                                
	                                                _p22_guardarClic(_p22_guardarDatosAdicionalesClic,false);                                   
	                                            }
		                            		}
	                            		}	
	                            	}
	                            }
	                    }]
                    })
                    
	    ]
	});
	////// contenido //////
	
	////// loaders //////
	
	_p22_fieldTipoPersona().addListener('change',_p22_tipoPersonaChange);
	_p22_tipoPersonaChange(_p22_fieldTipoPersona(),'F');
	_p22_fieldTipoPersona().forceSelection = true;

	_p22_fieldSexo().forceSelection = true;
	_fieldByName('CDESTCIV',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).forceSelection = true;
	_p22_fieldCumuloPrima().forceSelection = true;
	_fieldByName('CANALING',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).forceSelection = true;
	
	
	_p22_fieldRFC().validator =  function(value){
		if(validarRFC(value,_p22_fieldTipoPersona().getValue(), true)){
			return true;
		}else{
			return 'RFC inv&aacute;lido';
		}
	} 
	
	_p22_fieldRFC().addListener('change',function(me, val){
		try{
			_RFCduplicado = false;
			_fieldByName('parametros.pv_otvalor13',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />,true).setValue(val);
		}catch(e){
			debug('Sin campo de RFC en Art 104 para cumulo seleccionado',e)
		}
		
	});
	
	/**
	 * Si en modo agregar se modifican unos de los campos que forma RFC debe de volverse a calcular
	 */
	_p22_fieldNombre().addListener('change',
		function(){
			if(!_editandoCliente){
	    		_p22_fieldRFC().setValue(''); 
	    	}
		}
	);
	_p22_fieldApat().addListener('change',
		function(){
			if(!_editandoCliente){
	    		_p22_fieldRFC().setValue(''); 
	    	}
		}
	);
	_p22_fieldAmat().addListener('change',
		function(){
			if(!_editandoCliente){
	    		_p22_fieldRFC().setValue(''); 
	    	}
		}
	);

	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).addListener('change',
		function(){
			if(!_editandoCliente){
	    		_p22_fieldRFC().setValue(''); 
	    	}
		}
	);
	
	_fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).addListener('change',_p22_nacionalidadChange);
	_p22_nacionalidadChange(_fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />),'001');
    

	var RFCremoved = _p22_fieldRFC();
	var fieldRFCMoved = Ext.clone(RFCremoved);
	_p22_formDatosGenerales().items.remove(RFCremoved, true);
	
	_p22_formDatosGenerales().add([{
        xtype: 'panel',
        height: 10,
        width:  400,
		border: false,
        colspan : 3,
        layout: 'vbox',
        items:[
            {
                xtype: 'displayfield',
                fieldLabel: ''
            }
        ]
    },fieldRFCMoved,
	{
        xtype: 'button',
        text: 'Generar RFC',
        name : 'GeneraRFCBtn',
        handler: function(){
        	var RFCgen;
        	try{
        		
        		var RFCgen = generaRFC(_p22_fieldTipoPersona().getValue(), _fieldByName('DSNOMBRE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
        			_p22_fieldSegundoNombre().getValue(),  _p22_fieldApat().getValue(), _p22_fieldAmat().getValue(), _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getRawValue())
        		
        		_p22_fieldRFC().setValue(RFCgen);
        	}catch(e){
        		mensajeWarning(e);
        	}
        	
        	if(!Ext.isEmpty(RFCgen) && _p22_fieldRFC().isValid() && _p22_fieldRFC().getValue() != _rfcGuardado){
        		validaExisteCliente(RFCgen);
        	}
        	
        }
    },
    {
        xtype: 'displayfield',
        value: ''//'RFC Incorrectos'
    }]);
	
	////// loaders //////
    
    
    /**
     *	Para disparar factores que determinan los datos Adicionales 
     */
    _p22_fieldCumuloPrima().addListener('select', function(){_p22_guardarClic(_p22_datosAdicionalesClic, true);});
    _p22_fieldTipoPersona().addListener('select', function(){_p22_guardarClic(_p22_datosAdicionalesClic, true);});
    _p22_fielCdNacion().addListener('select',     function(){_p22_guardarClic(_p22_datosAdicionalesClic, true);});
    _p22_fieldResidente().addListener('select',   function(){_p22_guardarClic(_p22_datosAdicionalesClic, true);});
    
    _p22_formDatosGenerales().hide();

    //multidomicilio
    _p22_formDomicilio().hide();
    _p22_principalDatosAdicionales().hide();
    
    function validaExisteCliente(cdRFC){
    	
    	if(Ext.isEmpty(cdRFC)){
    		return;
    	}
    	
    	if(!validarRFC(cdRFC,_p22_fieldTipoPersona().getValue(), true)){
    		mensajeWarning('RFC inv&aacute;lido');
    		return;
    	}
    	
    	Ext.Ajax.request(
	    {
	        url       : _p22_urlObtenerPersonas
	        ,params: {
	    		'smap1.esSalud' :  _esSaludDanios,
	    		'smap1.rfc':  cdRFC,
	    		'smap1.validaTienePoliza':  _esCargaClienteNvo?'S':'N'
	    	}
	        ,success  : function(response)
	        {
	            var json = Ext.decode(response.responseText);
	            debug('response text:',json);
	            if(json.exito){
	            	if(json.smap1.cliEncontrado == "S"){
	            		_RFCduplicado = true;
						mensajeWarning('Ya existe un cliente registrado con este RFC.');        	
	            	}else{
	            		_RFCduplicado = false;
	            	}
	            }
	            else
	            {
	            	mensajeError('Error al validar existencia de RFC. Consulte a soporte.');
	            }
	        }
	        ,failure  : function()
	        {
	            errorComunicacion(null,'Error al validar existencia de RFC. Consulte a soporte.');
	        }
		});
    }
    
    
    _p22_fieldRFC().addListener('blur',function(){
    	debug('... En blur de validar RFC... ', ' _editandoCliente: ',_editandoCliente, ' _rfcGuardado: ',_rfcGuardado);
    	if(_p22_fieldRFC().isValid() && !_editandoCliente && _p22_fieldRFC().getValue() != _rfcGuardado){
    		validaExisteCliente(_p22_fieldRFC().getValue());
    	}
    });
    
    
function irModoAgregar(){
	
	_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).disable();
	_fieldByName('GeneraRFCBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
	_fieldByName('AgregaDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
	
	_permiteDuplicarRFC = false;
	
	municipioImportarTMP = '';
	coloniaImportarTMP = '';
	
	fijarCamposAgregar(_p22_formDatosGenerales());
	
	_editandoCliente = false;
	_numDomicilioTomado = undefined;
	
	_camposAconfigurar = undefined;
	_camposConfigurados = [];
	_domiciliosNuevos = [];
	_RFCduplicado = false;
	_rfcGuardado = 'DUMMYRFCTMP';
	
	
	Ext.Ajax.request(
    {
        url       : _UrlCargaConfCampos
        ,params: {
    		'smap1.esSalud' :  _esSaludDanios,
    		'smap1.cdperson':  _p22_cdperson ==  false?null:_p22_cdperson,
    		'smap1.codigoExterno':  (_esSaludDanios == 'S') ? _CDIDEEXTsel : _CDIDEPERsel,
    		'smap1.modoAgregar':  'S'
    	}
        ,success  : function(response)
        {
        	
            var json = Ext.decode(response.responseText);
            debug('response text:',json);
            if(json.exito){
            	
            	_editandoCliente = false;
            	_camposAconfigurar = json.slist1;
            	
            	debug('Lista de campos a configurar: ', _camposAconfigurar);
            	
            	_p22_formDatosGenerales().show(null,function(){
            		/**
					 * POR SI HAY UNA SUCURSAL A CARGAR EN EL MODO DE AGREGAR
					 */
					if(!Ext.isEmpty(_cargaSucursalEmi) && _esSaludDanios == 'S' && _cargaSucursalEmi >= 1000 ){
						_fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_cargaSucursalEmi);
					}
            		
					_fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue('001');
					_p22_nacionalidadChange(_fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />),'001');
					_fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
					
					_p22_fieldCumuloPrima().setValue('0');
					_p22_fieldCumuloPrima().resetOriginalValue();
					
					if(!Ext.isEmpty(_cargaTipoPersona)){
							_p22_fieldTipoPersona().setValue(_cargaTipoPersona);
							_p22_fieldTipoPersona().setReadOnly(true);
					}
					
					/**
					 * SE CONFIGURAN CAMPOS 
					 */
            		if(_camposAconfigurar){
				    	configuraCampos(_p22_formDatosGenerales());
				    }
				    if(_editaIncompleto){
				    	fijarCamposEditables(_p22_formDatosGenerales(), _editandoCliente);
				    }else{
				    	_fieldByName('EstatusDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).enable();
				    }
				    
				    if(!Ext.isEmpty(_cargaTipoPersona)){
				    	_p22_tipoPersonaChange(_p22_fieldTipoPersona(),_p22_fieldTipoPersona().getValue());
					}
				    
            	});
				                
                //multidomicilio
                _p22_formDomicilio().show();
                
                _p22_principalDatosAdicionales().show();
                
				if(_ocultaBusqueda){
					
					_p22_formBusqueda().hide();
				}
				
				try{
	    			var ventanaMensaje = window.parent;
	    			
	    			if (ventanaMensaje != window.top) {
					  debug('Para postMessage, El parent es el mismo que el top');
					}else{
					  debug('Para postMessage, El parent no es el mismo que el top');
					}
					
					var _codigoDanios = '';
					var _codigoSalud  = '';
					
					try{
						_codigoDanios = _CDIDEPERsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Danios',e);
					}
					
					try{
						_codigoSalud = _CDIDEEXTsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Salud',e);
					}
										
					var objMsg = {
						clienteIce: true,
						modo: 'A',
						cdperson: _p22_cdperson,
						cdideper: _CDIDEPERsel,
						cdideext: _CDIDEEXTsel,
						codigoDanios: _codigoDanios,
						codigoSalud:_codigoSalud
					};
					
					ventanaMensaje.postMessage(objMsg, "*");
					
	    		}catch(e){
	    			debugError('Error en postMessage',e);
	    		}
								    		
	    		/**
	    		 *	Recalcula los componentes de Datos Adicionales 
	    		 */
                _p22_guardarClic(_p22_datosAdicionalesClic, true);
            	
            }
            else
            {
            	mensajeError('Error al recuperar configuraci&oacute;n del cliente. Consulte a soporte.');
            }
        }
        ,failure  : function()
        {
        	loadMaskTabla.hide();
            errorComunicacion(null,'En recuperar configuraci&oacute;n del cliente. Consulte a soporte.');
        }
	});
	
}
    
function irModoEdicion(){
	
	_fieldByName('btnContinuarId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).disable();
	_fieldByName('GeneraRFCBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
	_fieldByName('AgregaDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
	
	_permiteDuplicarRFC = false;
	
	_p22_fieldRFC().setReadOnly(true);
	
	_camposAconfigurar = undefined;
	_camposConfigurados = [];
	_domiciliosNuevos = [];
	_numDomicilioTomado = undefined;
	_RFCduplicado = false;
	_rfcGuardado = 'DUMMYRFCTMP';
	
	Ext.Ajax.request(
	        {
	            url       : _UrlCargaConfCampos
	            ,params: {
            		'smap1.esSalud' :  _esSaludDanios,
            		'smap1.cdperson':  _p22_cdperson ==  false?null:_p22_cdperson,
            		'smap1.codigoExterno':  (_esSaludDanios == 'S') ? _CDIDEEXTsel : _CDIDEPERsel
            	}
	            ,success  : function(response)
	            {
	            	
	                var json = Ext.decode(response.responseText);
	                debug('response text:',json);
	                if(json.exito){
	                	
	                	_editandoCliente = true;
	                	_camposAconfigurar = json.slist1;
	                	
	                	debug('Lista de campos a configurar: ', _camposAconfigurar);
	                	
						if(_modoRecuperaDanios){
							
							if(!Ext.isEmpty(_CDIDEPERsel)){
								try{
									var ventanaMensaje = window.parent;
									
									if (ventanaMensaje != window.top) {
									  debug('Para postMessage, El parent es el mismo que el top');
									}else{
									  debug('Para postMessage, El parent no es el mismo que el top');
									}
									
									var _codigoDanios = '';
									var _codigoSalud  = '';
									
									try{
										_codigoDanios = _CDIDEPERsel.substring(5);
									}catch(e){
										debug('Error al obtener codigo externo de Danios',e);
									}
									
									try{
										_codigoSalud = _CDIDEEXTsel.substring(5);
									}catch(e){
										debug('Error al obtener codigo externo de Salud',e);
									}
									
									var objMsg = {
										clienteIce: true,
										modo: 'R',
										cdperson: _p22_cdperson,
										cdideper: _CDIDEPERsel,
										cdideext: _CDIDEEXTsel,
										codigoDanios: _codigoDanios,
										codigoSalud:_codigoSalud
									};
									
									ventanaMensaje.postMessage(objMsg, "*");
									
									try{
										/**
										 *	PARA ENDOSO DE CAMBIO DE CONTRATANTE 
										 */
					                	if(_p22_recuperaCallback){
					                    	_p22_recuperaCallback();
					                	}
					                }catch(e){
					                	debug('Error en recupera callback',e)
					                }		
					                
									mensajeCorrecto('Aviso','Cliente Recuperado: ' + _codigoDanios);
								}catch(e){
									debugError('Error en postMessage',e);
								}
							}else{
								var loadMaskTabla = new Ext.LoadMask(_genDivId, {msg:"Recuperando Cliente..."});
								loadMaskTabla.show();
								Ext.Ajax.request(
						        {
						            url       : _UrlguardaPersonaWS
						            ,params: {
					            		'smap1.esSalud' :  'D',// solo para Danios en este caso
					            		'smap1.cdperson':  _p22_cdperson
					            	}
						            ,success  : function(response)
						            {
						            	loadMaskTabla.hide();
						            	
						                var json = Ext.decode(response.responseText);
						                debug('response text:',json);
						                if(json.exito){
						                	try{
						                		var codigoExtGen = json.smap1.codigoExternoGen;
												var ventanaMensaje = window.parent;
												
												if (ventanaMensaje != window.top) {
												  debug('Para postMessage, El parent es el mismo que el top');
												}else{
												  debug('Para postMessage, El parent no es el mismo que el top');
												}
												
												var _codigoDanios = '';
												var _codigoSalud  = '';
												
												try{
													_codigoDanios = codigoExtGen.substring(5);
												}catch(e){
													debug('Error al obtener codigo externo de Danios',e);
												}
												
												try{
													_codigoSalud = _CDIDEEXTsel.substring(5);
												}catch(e){
													debug('Error al obtener codigo externo de Salud',e);
												}
												
												var objMsg = {
													clienteIce: true,
													modo: 'R',
													cdperson: _p22_cdperson,
													cdideper: codigoExtGen,
													cdideext: _CDIDEEXTsel,
													codigoDanios: _codigoDanios,
													codigoSalud:_codigoSalud
												};
												
												ventanaMensaje.postMessage(objMsg, "*");
												mensajeCorrecto('Aviso','Cliente Recuperado: ' + _codigoDanios);
											}catch(e){
												debugError('Error en postMessage',e);
											}
						                }
						                else
						                {
						                    mensajeError("Error al recuperar el c&oacute;digo externo del cliente. Datos incompletos.");
						                }
						            }
						            ,failure  : function()
						            {
						            	loadMaskTabla.hide();
						                errorComunicacion(null,'En recuperar c&oacute;digo externo del cliente. Consulte a soporte.');
						            }
								});
							}
							
							return;
						}
						
						if(_p22_cdperson!=false){
							_p22_formDatosGenerales().show(null, function(){
								
								/**
								 * Autosave en true para auto guardado cuando carga la pantalla o cuando cambian los factores de art 140, tvaloper
								 */
							    _p22_loadRecordCdperson(function(){_p22_guardarClic(_p22_datosAdicionalesClic, true);},true);
							
							    setTimeout(function(){
									/**
									 * SE CONFIGURAN CAMPOS se espera porque carguen los combos porque si no deja abiertos los campos
									 * ya que considera que no tienen valor y son obligatorios los desbloquea en automatico 
									 */
				            		if(_camposAconfigurar){
								    	configuraCampos(_p22_formDatosGenerales());
								    }
								    if(_editaIncompleto){
								    	fijarCamposEditables(_p22_formDatosGenerales(), _editandoCliente);
								    }else{
								    	_fieldByName('EstatusDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).enable();
								    }
								    
								    _p22_tipoPersonaChange(_p22_fieldTipoPersona(),_p22_fieldTipoPersona().getValue());
								    
								},600);
								
								
							});
							
							
							//multidomicilio
							_p22_formDomicilio().show();
						    _p22_principalDatosAdicionales().show();
					
						    if(_ocultaBusqueda)
						    {
						    	_p22_formBusqueda().hide();
							}
						    if(_muestraBusqueda){
								
								_p22_formBusqueda().show();
							}
						    
							
						    try{
								var ventanaMensaje = window.parent;
								
								if (ventanaMensaje != window.top) {
								  debug('Para postMessage, El parent es el mismo que el top');
								}else{
								  debug('Para postMessage, El parent no es el mismo que el top');
								}
								
								var _codigoDanios = '';
								var _codigoSalud  = '';
								
								try{
									_codigoDanios = _CDIDEPERsel.substring(5);
								}catch(e){
									debug('Error al obtener codigo externo de Danios',e);
								}
								
								try{
									_codigoSalud = _CDIDEEXTsel.substring(5);
								}catch(e){
									debug('Error al obtener codigo externo de Salud',e);
								}
								
								var objMsg = {
									clienteIce: true,
									modo: 'E',
									cdperson: _p22_cdperson,
									cdideper: _CDIDEPERsel,
									cdideext: _CDIDEEXTsel,
									codigoDanios: _codigoDanios,
									codigoSalud:_codigoSalud
								};
								
								ventanaMensaje.postMessage(objMsg, "*");
								
							}catch(e){
								debugError('Error en postMessage',e);
							}
							
						    
						}else{
							mensajeWarning('Error al cargar datos del cliente guardado.');
						}
	                }
	                else
	                {
	                	mensajeError('Error al recuperar configuraci&oacute;n del cliente. Consulte a soporte.');
	                }
	            }
	            ,failure  : function()
	            {
	            	loadMaskTabla.hide();
	                errorComunicacion(null,'En recuperar configuraci&oacute;n del cliente. Consulte a soporte.');
	            }
			});
			
	
}
    
function importaPersonaWS(esSaludD, codigoCliExt){
    	
    Ext.Ajax.request(
	        {
	            url       : _UrlImportaPersonaWS
	            ,params: {
            		'params.esSalud':  esSaludD,
            		'params.codigoCliExt':  codigoCliExt
            	}
	            ,success  : function(response)
	            {
	                var json = Ext.decode(response.responseText);
	                debug('response text:',json);
	                if(json.exito)
	                {
	                	if(esSaludD == 'S'){
	                		
	                		/**
	                		 * 
	                		 * SE PONE EL CDIDEEXT EN BLANCO POR PETICION DE ARGENIS POR PROBLEMAS DE SUCURSAL CON EL CLIENTE
	                		 * SE MANDAN A CREAR NUEVOS CLIENTES.
	                		 * 
	                		 * Codigo Original:
	                		 * _CDIDEEXTsel = json.params.codigoExterno; 
	                		 * 
	                		 */
	                		
	                		_CDIDEEXTsel = '';
	                		
	                	}else{
	                		_CDIDEPERsel = json.params.codigoExterno;
	                	}
	                	
		                _p22_cdperson = json.params.cdperson;
		                
						var form=_p22_formBusqueda();
						form.down('[name=smap1.rfc]').reset();
						form.down('[name=smap1.nombre]').reset();

						municipioImportarTMP = json.params.municipioImp;
						coloniaImportarTMP = json.params.coloniaImp;
						
						if(!Ext.isEmpty(municipioImportarTMP)){
                    			mensajeInfo("Se ha importado este cliente, sin embargo el municipio '" + municipioImportarTMP + "' no se ha encontrado, favor de seleccionar el municipio correcto en el domicilio importado.");
                    	}
	
						irModoEdicion();
	                }
	                else
	                {
	                    mensajeError("Error al Editar Cliente, vuelva a intentarlo.");
	                }
	            }
	            ,failure  : function()
	            {
	                errorComunicacion(null,'En importar cliente. Consulte a soporte.');
	            }
	});
    
    }
    
    _fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).editable = true;
    _fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).forceSelection = false;
    _fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(true);

    
	/**
	 *	Para Cliente Existente de una pantalla anterior 
	 */
    if(!Ext.isEmpty(_cargaCdPerson)){
    	
    	setTimeout(function(){
			_p22_cdperson = _cargaCdPerson;
//			_CDIDEPERsel = _p22_smap1.cdideper;
//			_CDIDEEXTsel = _p22_smap1.cdideext;
			

			if(!Ext.isEmpty(_cargaCompania)){
				_esSaludDanios = _cargaCompania;
				Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].setValue(_cargaCompania);
				Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].resetOriginalValue();
				
				if('D' == _cargaCompania){
					_fieldByName('DSL_CDIDEEXT',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					_fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();				
				}else if('S' == _cargaCompania){
					_fieldByName('DSL_CDIDEPER',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
				}
			}
				
    		irModoEdicion();
		},1000);
    	
    }else if(_esCargaClienteNvo){
    
    /**
	 *	Para Cliente No existente pero con un Codigo postal y tipo de persona predefinido 
	 */
    	setTimeout(function(){
    		
    		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].hide();
    		
			if(!Ext.isEmpty(_cargaCompania)){
				_esSaludDanios = _cargaCompania;
				Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].setValue(_cargaCompania);
				Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].resetOriginalValue();
				
				if('D' == _cargaCompania){
					_fieldByName('DSL_CDIDEEXT',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					_fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();				
				}else if('S' == _cargaCompania){
					_fieldByName('DSL_CDIDEPER',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
				}
			}
			
			_p22_formDatosGenerales().hide();
    		_p22_formDomicilio().hide();
    		_p22_principalDatosAdicionales().hide();
    		
			var form=_p22_formBusqueda();
			form.down('[name=smap1.rfc]').setFieldLabel('Ingrese el RFC');
			
			_p22_formBusqueda().hide(); 
			
			if(_muestraBusqueda){
				
				_p22_formBusqueda().show();
			}

			irModoAgregar();
			
			
		},1000);
	    
    }else if(!Ext.isEmpty(_cargaCompania)){
    	setTimeout(function(){
    		
    		_esSaludDanios = _cargaCompania;
    		Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaGroupId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].hide();
    		
			Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].setValue(_cargaCompania);
			Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('#companiaId',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1].resetOriginalValue();
			
			if('D' == _cargaCompania){
				_fieldByName('DSL_CDIDEEXT',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
				_fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();				
			}else if('S' == _cargaCompania){
				_fieldByName('DSL_CDIDEPER',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
			}
			
			_p22_formDatosGenerales().hide();
    		
    		//multidomicilio
    		_p22_formDomicilio().hide();
    		_p22_principalDatosAdicionales().hide();
			
		},1000);
    }
    
    if(!Ext.isEmpty(_cargaFenacMin)){
    	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setMinValue(_cargaFenacMin);
    }

    if(!Ext.isEmpty(_cargaFenacMax)){
    	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setMaxValue(_cargaFenacMax);
    }
    
////// funciones //////

function _p22_formBusqueda()
{
    debug('>_p22_formBusqueda<');
	return _fieldByName('_p22_formBusqueda',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_nacionalidadChange(combo,value)
{
    debug('>_p22_nacionalidadChange',value);
    if(value!='001')//extranjero
    {
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = false;
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).validate();
    }
    else//nacional
    {
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = true;
        _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).validate();
    }
    debug('<_p22_nacionalidadChange');
}

function _p22_tipoPersonaChange(combo,value)
{
	/**
	 *	NO FIJAR VALORES PARA CAMPOS QUE COINCIDEN ENTRE LOS TIPOS DE PERSONAS YA QUE BORRARIA INFORMACION 
	 */
    debug('>_p22_tipoPersonaChange',value);
    
    if(!_editandoCliente){
    	_p22_fieldRFC().setValue(''); 
    }
    
    if(value!='F')
    {
        _p22_fieldSegundoNombre().hide();
        _p22_fieldApat().hide();
        _p22_fieldAmat().hide();
        _p22_fieldSexo().hide();
        _fieldByName('CDESTCIV',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();

        _p22_fieldSegundoNombre().setValue('');
        _p22_fieldApat().setValue('');
        _p22_fieldAmat().setValue('');
        _p22_fieldSexo().setValue('');
        _fieldByName('CDESTCIV',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue('');
        
        _fieldByName('DSNOMBRE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setFieldLabel('Raz&oacute;n social*');
        _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setFieldLabel('Fecha de constituci&oacute;n*');
        
        if(value == 'S'){
        	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = true;
        	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
        }else {
        	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = false;
        	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
        }
    }
    else
    {
        _p22_fieldSegundoNombre().show();
        _p22_fieldApat().show();
        _p22_fieldAmat().show();
        _p22_fieldSexo().show();
        _fieldByName('CDESTCIV',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
        
        _fieldByName('DSNOMBRE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setFieldLabel('Nombre*');
        _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setFieldLabel('Fecha de nacimiento*');
        
        _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = false;
    	_fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
    }
    debug('<_p22_tipoPersonaChange');
}

function _p22_fieldNombre()
{
    debug('>_p22_fieldNombre<');
    return _fieldByName('DSNOMBRE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldSegundoNombre()
{
    debug('>_p22_fieldSegundoNombre<');
    return _fieldByName('DSNOMBRE1',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldApat()
{
    debug('>_p22_fieldApat<');
    return _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.down('[name=DSAPELLIDO]');
}

function _p22_fieldAmat()
{
    debug('>_p22_fieldAmat<');
    return _fieldByName('DSAPELLIDO1',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldSexo()
{
    debug('>_p22_fieldSexo<');
    return _fieldByName('OTSEXO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldTipoPersona()
{
    debug('>_p22_fieldTipoPersona<');
    return _fieldByName('OTFISJUR',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldCumuloPrima(){
    return _fieldByName('PTCUMUPR',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fielCdNacion(){
    return _fieldByName('CDNACION',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldResidente(){
    return _fieldByName('RESIDENTE',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_formDatosGenerales()
{
    return _fieldByName('_p22_formDatosGenerales',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

/* PARA EL LOADER */
function _p22_loadRecordCdperson(callbackload,autosave)
{
    debug('>_p22_loadRecordCdperson');
    _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p22_urlCargarPersonaCdperson
        ,params :
        {
            'smap1.cdperson' : _p22_cdperson
        }
        ,success : function(response)
        {
            _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
            var json=Ext.decode(response.responseText);
            if(json.exito)
            {
            	var record = new _p22_modeloGrid(json.smap2);
            	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
            	
            	/**
            	 * Para cargar el cdideper y cdideext segun sea el caso.
            	 */
            	if(!Ext.isEmpty(record.get('CDIDEPER'))){
            		_CDIDEPERsel = record.get('CDIDEPER');
            	}
            	if(!Ext.isEmpty(record.get('CDIDEEXT'))){
            		_CDIDEEXTsel = record.get('CDIDEEXT');
            	}
            	
            	var valTel  = _fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(); 
            	var valTel2 = _fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(); 
            	var valTel3 = _fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(); 
            	var valMail = _fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(); 
            	
			    _p22_formDatosGenerales().loadRecord(record);
			    
			    if(!Ext.isEmpty(valTel) && Ext.isEmpty(_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			    	_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(valTel);
			    }

			    if(!Ext.isEmpty(valTel2) && Ext.isEmpty(_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			    	_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(valTel2);
			    }
			    
			    if(!Ext.isEmpty(valTel3) && Ext.isEmpty(_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			    	_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(valTel3);
			    }
			    
			    if(!Ext.isEmpty(valMail) && Ext.isEmpty(_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			    	_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(valMail);
			    }
			    
			    storeDomiciliosCliente.load({
			    	params: {
			            'smap1.cdperson' : record.get('CDPERSON'),
			            'smap1.AUTOSAVE' : autosave?'S':'N'
			        },
			        callback: function(records, operation, success){
			        	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
			        	
			        	/**
			        	 * TOMAR DOMICILIO 
			        	 */
			        	
			        	if(!Ext.isEmpty(_cargaOrdDomicilio)){
			        		_tomarUnDomicilio = true;
			        		
			        		var indexDomTom = storeDomiciliosCliente.findBy(function(recordIt){
					        		
			        			if(_cargaOrdDomicilio == recordIt.get('NMORDDOM')){
			        				return true;
			        			}else{
			        				return false;
			        			}
			        				
			        		});
					        		
			        		if(indexDomTom >= 0){
			        			_numDomicilioTomado = storeDomiciliosCliente.getAt(indexDomTom);
			        			_numDomicilioTomado.set('SEL',true);
			        		}
			        		
//			        		gridDomiciliosCliente.getView().addRowCls(_numDomicilioTomado,'green-row');
			        	}
			        	
			        	if(success){
			        		if(_domicilioSimple && storeDomiciliosCliente.getCount() > 0){
			        			
			        			var confAgrDom = undefined;
			        			
			        			if(_camposAconfigurar){
			        				var confAgrDom = Ext.Array.findBy(_camposAconfigurar, function(campoConf, index){
			        					if(campoConf.CVECAMPO == "AGREGARDOM"){
			        						return true;
			        					}else{
			        						return false;
			        					}
			        				});
			        			}
			        			
			        			/**
			        			 *	Si es domicilio simple, ya hay uno o mas domicilios, y no hay configuracion del Boton de Agregar Domicilio 
			        			 *	se oculta
			        			 */
			        			if(Ext.isEmpty(confAgrDom)){
			        				_fieldByName('AgregaDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
			        			}
			        		}
			        		
			        		try{
			                	callbackload();
			                }catch(e){
			                	debug("Excepcion al ejecutar callback");
			                }
			        	}else{
			        		if(autosave){
			            		try{
				                	callbackload();
				                }catch(e){
				                	debug("Excepcion al ejecutar callback EN AUTOSAVE");
				                }
			            	}else{
			            		mensajeError(json.respuesta);
			            	}
			        	}
			        }
			    });
			    
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
            errorComunicacion(null,'En cargar datos generales');
        }
    });
    debug('<_p22_loadRecordCdperson');
}
/* PARA EL LOADER */


function _p22_guardarClic(callbackGuardar, autosave)
{
    debug('>_p22_guardarClic');
    var valido = true;
    
    if(valido)
    {
        valido = autosave || _p22_formDatosGenerales().isValid();
        if(!valido)
        {
            mensajeWarning('Favor de verificar los datos generales del cliente.');
        }
    }
    
    if(valido&&_p22_fieldTipoPersona().getValue()=='F')
    {
        valido = autosave || (!Ext.isEmpty(_p22_fieldApat().getValue())
                 &&!Ext.isEmpty(_p22_fieldAmat().getValue())
                 &&!Ext.isEmpty(_p22_fieldSexo().getValue()));
        if(!valido)
        {
            mensajeWarning('Favor de introducir los apellidos y sexo del cliente');
        }
    }
    
    //multidomicilio
    if(valido)
    {
        valido = autosave || (storeDomiciliosCliente.getCount()>0);
        if(!valido)
        {
            mensajeWarning('Favor de verificar los domicilios. Debe existir almenos un domicilio.');
        }
    }
    
    if(valido)
    {
    	var validDomicilio = true;
    	var domFiscalActivo = 0;
    	
    	storeDomiciliosCliente.each(function(record){
    		if(record.get('SWACTIVO') == "S" && record.get('CDTIPDOM') == "4"){
    			domFiscalActivo++;
    		}
    		
			if(Ext.isEmpty(record.get('CODPOSTAL')) || Ext.isEmpty(record.get('CDEDO')) || Ext.isEmpty(record.get('CDMUNICI')) || Ext.isEmpty(record.get('CDCOLONI'))
			|| Ext.isEmpty(record.get('DSDOMICI')) || Ext.isEmpty(record.get('NMNUMERO'))){
				validDomicilio = false;
				return false;
			}    		
    	});
    	
    	valido = autosave || validDomicilio;
    	
    	if(!valido){
			mensajeWarning('Favor de verificar y/o completar todos los datos de domicilios.');
    	}
    	if(domFiscalActivo > 1){
    		valido =  false;
    		mensajeWarning('No puede existir mas de un Domicilio Fiscal.');
    	}
    	
    }
    
    if(! autosave && valido){
    	if(_tomarUnDomicilio){
    		
    		if(_domicilioSimple && storeDomiciliosCliente.getCount()==1){
				_numDomicilioTomado = storeDomiciliosCliente.getAt(0);
				_numDomicilioTomado.set('SEL',true);
			}
    		
    		if(!_numDomicilioTomado){
    			valido = false;
    			mensajeWarning('Debe seleccionar un domicilio para efectos de la p&oacute;liza actual.');
    		}
    	}
    }
    
    if(valido)
    {
        _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
        
        saveListDom = [];
	    updateListDom = [];
	    deleteListDom = [];
	    
    	storeDomiciliosCliente.getRemovedRecords().forEach(function(record,index,arr){
        	deleteListDom.push(record.data);
    	});
        storeDomiciliosCliente.getNewRecords().forEach(function(record,index,arr){
        	saveListDom.push(record.data);
    	});
        storeDomiciliosCliente.getUpdatedRecords().forEach(function(record,index,arr){
    		updateListDom.push(record.data);
    	});
		
	    debug('Domicilios Removed: ' , deleteListDom);
	    debug('Domicilios Added: '   , saveListDom);
	    debug('Domicilios Updated: ' , updateListDom);
	    
		/**
		 * Carga la nueva sucursal si cambia, solo se actualiza en BD si el cliente no esta asociado a una poliza.
		 */
	    
		if(!autosave && !Ext.isEmpty(_cargaSucursalEmi) && _esSaludDanios == 'S' && _cargaSucursalEmi >= 1000 && _fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() != _cargaSucursalEmi){
			_fieldByName('CDSUCEMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_cargaSucursalEmi);
		}
		
		if(!autosave){
			
	        if(!_p22_formDatosAdicionales().isValid()){
            	mensajeWarning('Favor de verificar los datos adicionales del cliente.');
            	return;
        	}
    		
    		/**
    		 *	Se valida si hay cambios en las formas para no enviar la informacion, si hay un cambio en algun dato se envia toda la informacion excepto en los domicilios
    		 *	los domicilios se manejan indepedientes al envio de web service aunque se modifiquen otros datos y no hay cambios en domicilios, no se envian pero si hay un cambio en domicilios se envia todo
    		 */
			
			var cambiosDatosGenerales   = _p22_formDatosGenerales().getForm().isDirty();
			var cambisoDatosDomicilio   = (storeDomiciliosCliente.getModifiedRecords().length > 0);
			var cambiosDatosAdicionales = _p22_formDatosAdicionales().getForm().isDirty();
			var cambiosDatosAccionistas = ((accionistasStore) && (accionistasStore.getModifiedRecords().length > 0 || accionistasStore.getRemovedRecords().length > 0)) ? true : false;
			
    		debug('>>>>>>>>>>>>>>>>>>>>>>>>>>>>   PARA GUARDADO DE CLIENTE   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<');
    		debug('>>>>>>>>>>>>>>>>>> Hay Datos Generales a guardar: ', cambiosDatosGenerales);
    		debug('>>>>>>>>>>>>>>>>>> Hay Datos de Domicilio a guardar: ', cambisoDatosDomicilio);
    		debug('>>>>>>>>>>>>>>>>>> Hay Datos Adicionales a guardar: ', cambiosDatosAdicionales);
    		debug('>>>>>>>>>>>>>>>>>> Hay Datos de Accionistas a guardar: ', cambiosDatosAccionistas);
    		
    		var guardarDatos = cambiosDatosGenerales || cambisoDatosDomicilio || cambiosDatosAdicionales || cambiosDatosAccionistas;
    		
    		if(_esSaludDanios == 'S' && Ext.isEmpty(_CDIDEEXTsel)){
    			debug('>>>>>>>>>>>>>>>>>> Cliente de Salud sin codigo Externo, Se guardaran los datos para crear un nuevo codigo');
    			guardarDatos = true;
    		}else if(_esSaludDanios == 'D' && Ext.isEmpty(_CDIDEPERsel)){
    			debug('>>>>>>>>>>>>>>>>>> Cliente de Danios sin codigo Externo, Se guardaran los datos para crear un nuevo codigo');
    			guardarDatos = true;
    		}
    		
    		debug('>>>>>>>>> :::::::: Guardar Datos: ', guardarDatos);
    		
    		if(!guardarDatos){
				debug('>>>>>>>>> :::::::: No se actualizan los datos, solo se ejecutan callbacks ::::::::');
				var jsonRespDummy = {
					sinGuardado: true,
	    			success: true,
	    			exito  : true,
	    			smap1  : _p22_formDatosGenerales().getValues(),
	                smap2  : _p22_formDomicilio().getValues()
	    		};
	    		
	    		if(_tomarUnDomicilio){
					jsonRespDummy.smap1.CDPOSTAL = _numDomicilioTomado.get('CODPOSTAL');
					jsonRespDummy.smap1.CDEDO    = _numDomicilioTomado.get('CDEDO');
					jsonRespDummy.smap1.CDMUNICI = _numDomicilioTomado.get('CDMUNICI');
					jsonRespDummy.smap1.NMORDDOM = _numDomicilioTomado.get('NMORDDOM');
	    		}
	    		
	    		debug('>>>>>>>>> :::::::: Datos a enviar en callbacks: ', jsonRespDummy);
	    		
	        	try{
                	if(_p22_cdperson!=false && _p22_parentCallback){
                    	_p22_parentCallback(jsonRespDummy);
                	}
                }catch(e){
                	debug('Error',e)
                }

                try{
                	if(_p22_cdperson!=false && _contrantantePrincipal && _callbackContPrincipal){
                    	_callbackContPrincipal(jsonRespDummy);
                	}
                }catch(e){
                	debug('Error',e)
                }
                
                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
                
                try{
	    			var ventanaMensaje = window.parent;
	    			
	    			if (ventanaMensaje != window.top) {
					  debug('Para postMessage, El parent es el mismo que el top');
					}else{
					  debug('Para postMessage, El parent no es el mismo que el top');
					}
					
					var _codigoDanios = '';
					var _codigoSalud  = '';
					
					try{
						_codigoDanios = _CDIDEPERsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Danios',e);
					}
					
					try{
						_codigoSalud = _CDIDEEXTsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Salud',e);
					}
					
					var objMsg = {
						clienteIce: true,
						modo: 'G',
						success : jsonRespDummy.exito,
						cdperson: _p22_cdperson,
						cdideper: _CDIDEPERsel,
						cdideext: _CDIDEEXTsel,
						codigoDanios: _codigoDanios,
						codigoSalud:_codigoSalud
					};
					
					ventanaMensaje.postMessage(objMsg, "*");
					if(_modoRecuperaDanios){
						mensajeInfo('Cliente Guardado y Recuperado: ' + _codigoDanios);
					}
					
	    		}catch(e){
	    			debugError('Error en postMessage',e);
	    		}
	    		
	    		mensajeCorrecto('Aviso','Datos guardados correctamente. No se ralizaron cambios a los datos del Contratante.');
	    		
				return;
    		}
		}
        
		var feIngreso =  _fieldByName('FEINGRESO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue();
		
		if(Ext.isEmpty(feIngreso)){
			_fieldByName('FEINGRESO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(new Date());
		}
		
        Ext.Ajax.request(
        {
            url       : _p22_urlGuardar
            ,jsonData :
            {
                 smap1  : _p22_formDatosGenerales().getValues(),
                 smap2 : _p22_formDomicilio().getValues(),
                 saveList   : saveListDom,
                 deleteList : deleteListDom,
                 updateList : updateListDom,
                 smap3: {
                	'AUTOSAVE': autosave?'S':'N'
                }
            }
            ,success : function(response)
            {
                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('json response:',json);
                if(json.exito)
                {
                    _p22_fieldCdperson().setValue(json.smap1.CDPERSON);
                    _p22_fieldCdperson().resetOriginalValue();
                    
                    _p22_fieldCdperson().validado = _p22_fieldCdperson().validado||Ext.isEmpty(autosave)||autosave==false;
                    _p22_cdperson = json.smap1.CDPERSON;
                    
		
					if(!autosave){
						if(!_editandoCliente){
				        	_rfcGuardado = _p22_fieldRFC().getValue();
						}
						
						storeDomiciliosCliente.load({
					    	params: {
					            'smap1.cdperson' : json.smap1.CDPERSON
					        },
					        callback: function(records, operation, success){
					        	
					        	if(success){
								
					        		debug('saveListDom',saveListDom);
					        		var exitoGuardaDom =  true;
					        		
					        		Ext.Array.forEach(saveListDom,function(domicilio) {
									    
									    storeDomiciliosCliente.each(function(recordIt, index){
						        		
						        			if(    domicilio.CODPOSTAL == recordIt.get('CODPOSTAL')
						        				&& domicilio.CDEDO     == recordIt.get('CDEDO')
						        				&& domicilio.CDMUNICI  == recordIt.get('CDMUNICI')
						        				&& domicilio.CDCOLONI  == recordIt.get('CDCOLONI')
						        				&& domicilio.CDTIPDOM  == recordIt.get('CDTIPDOM')
						        				&& domicilio.SWACTIVO  == recordIt.get('SWACTIVO')
						        				){
						        					/**
						        					 *	Para los domicilios que unicamente fueron agregados, algunos se mandan en modo insert cuando no existen en WS Domicilios 
						        					 */
						        					if(Ext.isEmpty(domicilio.NMORDDOM)){
						        						
														domicilio.NMORDDOM = recordIt.get('NMORDDOM');
						        						_domiciliosNuevos.push(recordIt.get('NMORDDOM'));						        					
						        					}
						        				}	
						        				
						        		});
						        		
						        		if(Ext.isEmpty(domicilio.NMORDDOM)){
						        			mensajeError('Error al Guardar Domicilios del cliente. Consulte a Soporte.');
						        			exitoGuardaDom =  false;
						        		}
									});
									
									if(!exitoGuardaDom) {
										return false;
									}
					        		
					        		if(_tomarUnDomicilio){
					        			
					        			
					        			var indexDomSel = -1;
					        			
					        			if(Ext.isEmpty(_numDomicilioTomado.get('NMORDDOM'))){
					        				
					        				indexDomSel = storeDomiciliosCliente.findBy(function(recordIt){
							        			if(_numDomicilioTomado.get('CODPOSTAL') == recordIt.get('CODPOSTAL')
							        				&& _numDomicilioTomado.get('CDEDO') == recordIt.get('CDEDO')
							        				&& _numDomicilioTomado.get('CDMUNICI') == recordIt.get('CDMUNICI')
							        				&& _numDomicilioTomado.get('CDCOLONI') == recordIt.get('CDCOLONI')
							        				&& _numDomicilioTomado.get('CDTIPDOM') == recordIt.get('CDTIPDOM')
							        				&& _numDomicilioTomado.get('SWACTIVO') == recordIt.get('SWACTIVO')
							        				){
							        				return true;
							        			}else{
							        				return false;
							        			}
						        			});
						        			
					        			}else{
					        				
					        				indexDomSel = storeDomiciliosCliente.findBy(function(recordIt){
							        			if(_numDomicilioTomado.get('NMORDDOM') == recordIt.get('NMORDDOM')){
							        				return true;
							        			}else{
							        				return false;
							        			}
						        			});
						        			
					        			}
					        			
						        		if(indexDomSel >= 0){
						        			_numDomicilioTomado = storeDomiciliosCliente.getAt(indexDomSel);
						        			_numDomicilioTomado.set('SEL',true);
						        		}else{
						        			mensajeError('Error al seleccionar ordinal de domicilio. Consulte a Soporte.');
						        			return false;
						        		}
						        		
						        		json.smap1.CDPOSTAL = _numDomicilioTomado.get('CODPOSTAL');
						        		json.smap1.CDEDO    = _numDomicilioTomado.get('CDEDO');
						        		json.smap1.CDMUNICI = _numDomicilioTomado.get('CDMUNICI');
						        		json.smap1.NMORDDOM = _numDomicilioTomado.get('NMORDDOM');
						        		
						        		_cargaOrdDomicilio = _numDomicilioTomado.get('NMORDDOM');
						        		
//						        		gridDomiciliosCliente.getView().addRowCls(_numDomicilioTomado,'green-row');
					        		}
					        		
					        		try{
					        			/**
					        			 * Ejecuta Datos adicionales para terminar el guardado
					        			 */
					                        callbackGuardar(json);
				                    }catch(ex){
				                    	debug("Excepcion al ejecutar callbackGuardar",ex);
				                    	mensajeCorrecto('Datos de persona guardados',json.respuesta);
				                    }
					                    
					        	}else{
					        		mensajeError('Error al Guardar Domicilios del cliente. Consulte a Soporte.');
					        	}
					        	
					        }
						});
					}else{
						
						 //si es autosave no es necesario ejecutar callback final parent
						/**
			    		 *	Se va a Datos Adicionales Clic, Recalcula los componentes de Datos Adicionales art 140
			    		 */
						try{
                        	callbackGuardar(null);
	                    }catch(e){
	                    	debug("Excepcion al ejecutar callbackGuardar");
	                    	mensajeCorrecto('Datos de persona guardados',json.respuesta);
	                    }
					}

                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function()
            {
                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
                errorComunicacion(null,'En almacenar datos generales');
            }
        });
    }
    
    debug('<_p22_guardarClic');
}

function _p22_formDomicilio()
{
    debug('>_p22_formDomicilio<');
    return _fieldByName('_p22_formDomicilio',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_principalDatosAdicionales()
{
    debug('>_p22_principalDatosAdicionales<');
    return _fieldByName('_p22_principalDatosAdicionales',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldRFC()
{
    debug('>_p22_fieldRFC<');
    return _fieldByName('CDRFC',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldCdperson()
{
    debug('>_p22_fieldCdperson<');
    return _fieldByName('CDPERSON',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_fieldConsecutivo()
{
    debug('>_p22_fieldConsecutivo<');
    return _fieldByName('NMORDDOM',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function recargaTelefonoEmailOriginales(){
	//rellenar al cargar telefono y correo electronico para los valores Originales
	
	try{
		
		///////////   11111111
		debug('Tel en Datos Generales: ' , _fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		
		/**
		 *	Si se recarga y trae un valor de la recarga es el valor original 
		 */
		if(Ext.isEmpty(_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && !Ext.isEmpty(_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
		}
		
		///////////   222222222
		debug('Tel2 en Datos Generales: ' , _fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel2 en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
				
		/**
		 *	Si se recarga y trae un valor de la recarga es el valor original 
		 */
		if(Ext.isEmpty(_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && !Ext.isEmpty(_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
		}
		
		///////////   33333333333
		debug('Tel3 en Datos Generales: ' , _fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel3 en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			
		/**
		 *	Si se recarga y trae un valor de la recarga es el valor original 
		 */
		if(Ext.isEmpty(_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && !Ext.isEmpty(_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
		}
		
	}catch(e){
		debug('Aun no existe algun campo de telefono. ', e);
	}
	
	try{
		debug('Email en Datos Generales: ' , _fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Email en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
				
		/**
		 *	Si se recarga y trae un valor de la recarga es el valor original 
		 */
		if(Ext.isEmpty(_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && !Ext.isEmpty(_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
			_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
		}
		
	}catch(e){
		debug('Aun no existe el campo email. ', e);
	}
}

function recargaTelefonoEmailNuevos(){
	//rellenar al cargar telefono y correo electronico para los valores Nuevos
	
	try{
		
		///////////   11111111
		debug('Tel en Datos Generales: ' , _fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		
		/**
		 *	Si se recarga y no trae ese valor y no tenia valor, se le fija el nuevo valor 
		 */
		if(!Ext.isEmpty(_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && ( Ext.isEmpty(_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) || _fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() != _fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() )){
			_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		}
		
		///////////   222222222
		debug('Tel2 en Datos Generales: ' , _fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel2 en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
				
		/**
		 *	Si se recarga y no trae ese valor y no tenia valor, se le fija el nuevo valor 
		 */
		if(!Ext.isEmpty(_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && ( Ext.isEmpty(_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) || _fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() != _fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() )){
			_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		}

		///////////   33333333333
		debug('Tel3 en Datos Generales: ' , _fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Tel3 en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
			
		/**
		 *	Si se recarga y no trae ese valor y no tenia valor, se le fija el nuevo valor 
		 */
		if(!Ext.isEmpty(_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && ( Ext.isEmpty(_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) || _fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() != _fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() )){
			_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		}
		
	}catch(e){
		debug('Aun no existe algun campo de telefono. ', e);
	}
	
	try{
		debug('Email en Datos Generales: ' , _fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		debug('Email en Datos Adicional: ' , _fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
				
		/**
		 *	Si se recarga y no trae ese valor y no tenia valor, se le fija el nuevo valor 
		 */
		if(!Ext.isEmpty(_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) && ( Ext.isEmpty(_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue()) || _fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() != _fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue() )){
			_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue());
		}
		
	}catch(e){
		debug('Aun no existe el campo email. ', e);
	}
}

function _p22_datosAdicionalesClic()
{
    debug('>_p22_datosAdicionalesClic');
    
    windowAccionistas = undefined;
    
    /** PARA ACTUALIZAR EL NUEVO ESTATUS GENERAL DE LA PERSONA **/
    Ext.Ajax.request(
	        {
	            url       : _UrlActualizaStatusPersona
	            ,params: {
            		'params.pv_cdperson_i':  _p22_fieldCdperson().getValue(),
            		'params.pv_cdrol_i':  '1'
            	}
	            ,success  : function(response)
	            {
	                var json = Ext.decode(response.responseText);
	                debug('response text:',json);
	                if(json.exito)
	                {
	                    debug('Actualizando estatus de Persona: ');
	                    _fieldByName('STATUS',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(json.respuesta);
	                    _fieldByName('STATUS',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
	                    
	                }
	                else
	                {
	                    mensajeError(json.respuesta);
	                }
	            }
	            ,failure  : function()
	            {
	                errorComunicacion(null,'En actualizar estatus persona');
	            }
	});
    
    _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
    
    _p22_principalDatosAdicionales().removeAll();
    
    Ext.Ajax.request(
    {
        url      : _p22_urlTatriperTvaloper
        ,params  : { 'smap1.cdperson' : _p22_fieldCdperson().getValue(), 'smap1.cdrol' : '1' }
        ,success : function(response)
        {
            _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('json response:',json);
            if(json.exito)
            {
                Ext.define('_p22_modeloTatriper',
                {
                    extend  : 'Ext.data.Model'
                    ,fields : Ext.decode(json.smap1.fieldsTatriper.substring("fields:".length))
                });
                
                
                _p22_principalDatosAdicionales().add({
            	    	layout: 'column',
            	    	border: false,
            	    	html:'<span style="font-size:14px; font-weight: bold;">Para que el estatus de la persona sea completo se requiere que los campos con el s&iacute;mbolo: <img src="${ctx}/resources/fam3icons/icons/transmit_error.png" alt="">, sean capturados.</span><br/><br/>'
            	});
                _p22_principalDatosAdicionales().add(
                Ext.create('Ext.form.Panel',
                        {
                            border    : 0
                            ,name   : '_p22_formDatosAdicionales'
//                          ,width    : 570
                            ,defaults : { style : 'margin:5px;' }
                            ,layout   :
                            {
                                type     : 'table'
                                ,columns : 3
                            }
                            ,trackResetOnLoad: true
                            ,items    : Ext.decode(json.smap1.itemsTatriper.substring("items:".length))
                        })
                );
                
				fieldEstCorp = _fieldByLabel('Estructura corporativa', null, true);
				var fieldEstCorpAux = Ext.clone(fieldEstCorp);
				
				if(fieldEstCorp){
					var panelDatAdic = fieldEstCorp.up();
					var indEstCorp = panelDatAdic.items.indexOf(fieldEstCorp);
					
					_p22_formDatosAdicionales().items.remove(fieldEstCorp, true);
					fieldEstCorp = fieldEstCorpAux;
					
					panelDatAdic.insert(indEstCorp,{
						xtype      : 'panel',
						defaults : { style : 'margin:3px' },
						border     :  1,
						items      : [fieldEstCorp,{
                       	 xtype    : 'button'
                         	,text     : 'Ver/Editar Accionistas'
                             ,icon     : '${ctx}/resources/fam3icons/icons/award_star_add.png'
                             ,tooltip  : 'Ver/Editar Accionostas'
                             ,handler  : function(button)
                             {
                                verEditarAccionistas(_p22_fieldCdperson().getValue(), fieldEstCorp.getName().substring(fieldEstCorp.getName().length-2, fieldEstCorp.getName().length), fieldEstCorp.getValue());
                             }
 					}]
					});
					
					fieldEstCorp.addListener('beforeselect',function (combo){
						if(windowAccionistas){
							
							var valorAnterior = combo.getValue();
							
							Ext.Msg.show({
		    		            title: 'Confirmar acci&oacute;n',
		    		            msg  : 'Si cambia la Estructura Coorporativa perder&aacute; la lista de Accionistas guardada. &iquest;Desea continuar?',
		    		            buttons: Ext.Msg.YESNO,
		    		            fn: function(buttonId, text, opt) {
		    		            	if(buttonId == 'yes') {
		    		            		
		    		            		windowAccionistas = undefined;
		    		            		
		    		            		Ext.Ajax.request(
		    		                	        {
		    		                	            url       : _UrlEliminaAccionistas
		    		                	            ,params: {
	    		                	            		'params.pv_cdperson_i':   _p22_fieldCdperson().getValue(),
	    		                    	            	'params.pv_cdatribu_i':  fieldEstCorp.getName().substring(fieldEstCorp.getName().length-2, fieldEstCorp.getName().length)
	    		                	            	}
		    		                	            ,success  : function(response)
		    		                	            {
		    		                	                _p22_formDatosAdicionales().setLoading(false);
		    		                	                var json = Ext.decode(response.responseText);
		    		                	                debug('response text:',json);
		    		                	                if(json.exito)
		    		                	                {
		    		                	                    mensajeCorrecto('Aviso','Datos de Accionistas eliminados correctamente.');
		    		                	                }
		    		                	                else
		    		                	                {
		    		                	                    mensajeError(json.respuesta);
		    		                	                }
		    		                	            }
		    		                	            ,failure  : function()
		    		                	            {
		    		                	                _p22_formDatosAdicionales().setLoading(false);
		    		                	                errorComunicacion(null,'En guardar datos de accionistas');
		    		                	            }
		    		                	});
		    		            	
		    		            		
		    		            	}else{
		    		            		combo.setValue(valorAnterior);
		    		            	}
		            			},
		    		            icon: Ext.Msg.QUESTION
		        			});	
						}else {
							return true;
						}
					});
				}
				
				
				try{
					_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = _fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank;	
					_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = _fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank;	
					_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = _fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank;
					
					if(!_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank && Ext.isEmpty(_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
						_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(false);
					}
					if(!_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank && Ext.isEmpty(_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
						_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(false);
					}
					if(!_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank && Ext.isEmpty(_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
						_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(false);
					}
					
					
				}catch(e){
					debug('No se encontro un elemento de telefono para fijar obligatoriedad', e);
				}
				
				try{
					_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank = _fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank;
					if(!_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).allowBlank && Ext.isEmpty(_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue())){
						_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(false);
					}
				}catch(e){
					debug('No se encontro el elemento de email para fijar obligatoriedad', e);
				}
				
				
				_p22_formDatosAdicionales().items.each(function(item,index,len){
					
                	if(!item.allowBlank){
                		item.allowBlank = true;
                		if(item.getFieldLabel){
                			item.inicialField = item.getFieldLabel();
                			item.setFieldLabel('<span>'+ item.getFieldLabel() +'<img src="${ctx}/resources/fam3icons/icons/transmit_error.png" alt=""></span>');
                		}
                	}else{
                		if(item.getFieldLabel){
                			item.inicialField = item.getFieldLabel();
                		}
                	}
                	
                	if(item.xtype && (item.xtype == 'combo' || item.xtype == 'combobox')){
						item.editable = true;
					}
                	
                });
				
                _p22_formDatosAdicionales().loadRecord(new _p22_modeloTatriper(json.smap2));
                
                var itemsDocumento=Ext.ComponentQuery.query('[tieneDocu]',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
                debug('itemsDocumento:',itemsDocumento);

                
                for(var i=0;i<itemsDocumento.length;i++)
                {
                    itemDocumento=itemsDocumento[i];
                    
                    
                    if('DOC' == itemDocumento.tieneDocu){
//                    	debug('Icono itemDoc: ' , itemDocumento.icon);
	                    itemDocumento.up().add(
	                    		{
	        						xtype      : 'panel',
	        						defaults : { style : 'margin:3px' },
	        						border     :  1,
	        						items      : [
	        						         Ext.clone(itemDocumento),
	        						        {
	        	                       		 xtype    : 'panel'
	        		                        ,layout  : 'hbox'
	        		                        ,border  : 0
	        		                        ,items   :
	        		                        [
	        		                            {
	        		                                xtype       : 'displayfield'
	        		                                ,labelWidth : 180
	        		                                ,fieldLabel : 'Documento digital' + (itemDocumento.allowBlank==false ? '<span style="font-size:10px;">(obligatorio)</span>' : '')
	        		                            }
	        		                            ,{
	        		                                xtype     : 'button'
	        		                                ,name     : itemDocumento.name+'btnId'
	        		                                ,icon     : '${ctx}/resources/fam3icons/icons/arrow_up.png'
	        		                                ,tooltip  : 'Subir nuevo'
	        		                                ,codidocu : itemDocumento.codidocu
	        		                                ,descrip  : itemDocumento.inicialField
	        		                                ,iddocumento: itemDocumento.name
	        		                                ,handler  : function(button)
	        		                                {
	        		                                	_DocASubir = button.iddocumento;
	        		                                    _p22_subirArchivo(_p22_fieldCdperson().getValue(),button.codidocu,button.descrip);
	        		                                }
	        		                            },{
	        		                                xtype     : 'button'
	        		                                ,icon     : '${ctx}/resources/fam3icons/icons/eye.png'
	        		                                ,tooltip  : 'Descargar'
	        		                                ,codidocu : itemDocumento.codidocu
	        		                                ,descrip  : itemDocumento.inicialField
	        		                                ,handler  : function(button)
	        		                                {
	        		                                    _p22_cargarArchivo(_p22_fieldCdperson().getValue(),button.codidocu,button.descrip);
	        		                                }
	        		                            },{
	        		                                xtype: 'imagecomponent'
	        		                                ,docCargado: itemDocumento.name+'ImgDocId'
	        		                                ,src : "CARGADO" == itemDocumento.icon ? '${ctx}/resources/fam3icons/icons/accept.png' : '${ctx}/resources/fam3icons/icons/cancel.png'
	        		                                ,height: 24
	        		                                ,width:  24
	        		                            }
	        		                        ]
	        		                    },{
											xtype: 'tbspacer',        	    	
											height: 15                	
						        	    }]
	        					}
	                    	);
	                }
	                
                }
                
                _p22_formDatosAdicionales().add({
					xtype: 'tbspacer',        	    	
					height: 50                	
        	     });
                _p22_formDatosAdicionales().add({
					xtype: 'tbspacer',        	    	
					height: 50                	
        	     });
                _p22_formDatosAdicionales().add({
					xtype: 'tbspacer',        	    	
					height: 50                	
        	     });
        	     
        	     try{
					_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).vtype = 'email';
					_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).on({
							change: function(me, val){
				    				_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(val);
							}
					});
					
					_fieldByName('parametros.pv_otvalor39',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					
				}catch(e){
					debug('Error en adaptacion de  campo email',e);
				}
				
				try{
					
					_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).on({
						change: function(me, val){
			    				_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(val);
						}
					});
					_fieldByName('parametros.pv_otvalor38',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					
					////////   2222222
					_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).on({
						change: function(me, val){
			    				_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(val);
						}
					});
					_fieldByName('parametros.pv_otvalor51',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					
					////////   3333333
					_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).on({
						change: function(me, val){
			    				_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(val);
						}
					});
					_fieldByName('parametros.pv_otvalor52',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					
				}catch(e){
					debug('Error en adaptacion de un campo de telefono',e);
				}
				
				/**
				 *	Antes de fijar campos con valores originales como bloqueados 
				 */
				recargaTelefonoEmailOriginales();
        	     
        	    if(_camposAconfigurar){
			    	configuraCampos(_p22_formDatosAdicionales());
			    }
			    
			    if(_editaIncompleto){
			    	fijarCamposEditablesAdicionales(_p22_formDatosAdicionales(), _editandoCliente);
			    }
			    
			    /**
				 *	Despues de fijar valores nuevos de campos sin bloquearlos 
				 */
			    recargaTelefonoEmailNuevos();
        	     
				//rfc solo lextura en datos adicionales       	     
        	     try{
        	     	_fieldByName('parametros.pv_otvalor13',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).setValue(_p22_fieldRFC().getValue());
        	     	_fieldByName('parametros.pv_otvalor13',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).resetOriginalValue();
        	     	_fieldByName('parametros.pv_otvalor13',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).setReadOnly(true);
        	     }catch(e){
        	     	debug('Error en adaptacion de  campo Cliente RFC de Generales a Adicionales, no existe el campo',e);
        	     }
        	     
				//cliente VIP solo lextura        	     
        	     try{
        	     	_fieldByName('parametros.pv_otvalor37',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).setReadOnly(true);
        	     }catch(e){
        	     	debug('Error en adaptacion de  campo Cliente VIP, para solo lectura, el campo no existe',e);
        	     }
        	     
        	     if(!_activaCveFamiliar){
			    	
					try{
						//Clave familiar
			    		_fieldByName('parametros.pv_otvalor49', _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).hide();	
					}catch(e){
						debug('Sin campo de Clave Familiar, no se oculta');
					}

					try{
						//Numero socio
			    	_fieldByName('parametros.pv_otvalor50', _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />, true).hide();	
					}catch(e){
						debug('Sin campo de Numero de Socio, no se oculta');
					}
			    }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
            errorComunicacion(null,'En carga conf. datos adicionales');
        }
    });
    
    debug('<_p22_datosAdicionalesClic');
}

function _p22_guardarDatosAdicionalesClic(jsonCli)
{
    debug('>_p22_guardarDatosAdicionalesClic');
    
    var saveList = [];
    var updateList = [];
    var deleteList = [];
    
    if(windowAccionistas){
    	accionistasStore.getRemovedRecords().forEach(function(record,index,arr){
        	deleteList.push(record.data);
    	});
        accionistasStore.getNewRecords().forEach(function(record,index,arr){
    		if(record.dirty) saveList.push(record.data);
    	});
        accionistasStore.getUpdatedRecords().forEach(function(record,index,arr){
    		updateList.push(record.data);
    	});
    }
	
    debug('Accionistas Removed: ' , deleteList);
    debug('Accionistas Added: '   , saveList);
    debug('Accionistas Updated: ' , updateList);
    
    var valido=true;
    
    if(valido)
    {
        valido = _p22_formDatosAdicionales().isValid();
        if(!valido)
        {
            mensajeWarning('Favor de verificar los datos adicionales del cliente.');
        }
    }
    
    if(valido)
    {
        _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
        var jsonData = _p22_formDatosAdicionales().getValues();
        jsonData['cdperson'] = _p22_fieldCdperson().getValue();
        jsonData['cdrol'] = '1';
        jsonData['esSalud'] = _esSaludDanios;
        jsonData['codigoExterno'] = (_esSaludDanios=='S')?_CDIDEEXTsel:_CDIDEPERsel;
        jsonData['codigoExterno2'] = (_esSaludDanios=='S')?_CDIDEPERsel:_CDIDEEXTsel;
        
        Ext.Ajax.request(
        {
            url       : _p22_urlGuadarTvaloper
            ,jsonData :
            {
                smap1 : jsonData,
                smap2  : _p22_formDatosGenerales().getValues(),
                saveList   : saveListDom,
                updateList : updateListDom
            }
            ,success  : function(response)
            {
                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
                var json = Ext.decode(response.responseText);
                debug('response text:',json);
                if(json.exito)
                {
                    mensajeCorrecto('Datos de persona guardados',json.respuesta);
                    
                    
                    	if(_esSaludDanios == 'S'){
                    		_CDIDEEXTsel = json.smap1.codigoExterno;
	                	}else{
							_CDIDEPERsel = json.smap1.codigoExterno;
	                	}
	                	
	                	_p22_loadRecordCdperson(function(){debug('Sin Callback');},true);
                }
                else
                {
                    if(!Ext.isEmpty(_cargaCompania)){
                    	mensajeWarning(json.respuesta);
                    }else{
                    	mensajeError(json.respuesta);	
                    }
                    
                }
                
                if(!Ext.isEmpty(jsonCli)){
	    		
	    			/**
		        	 * SE MUEVEN DE POSICION LOS CALLBACK DE LA PANTALLA PRINCIPAL
		        	 */
		        	try{
                    	if(_p22_cdperson!=false && _p22_parentCallback){
                        	_p22_parentCallback(jsonCli);
                    	}
                    }catch(e){
                    	debug('Error',e)
                    }

                    try{
                    	if(_p22_cdperson!=false && _contrantantePrincipal && _callbackContPrincipal){
                        	_callbackContPrincipal(jsonCli);
                    	}
                    }catch(e){
                    	debug('Error',e)
                    }
	    		}
                
                try{
	    			var ventanaMensaje = window.parent;
	    			
	    			if (ventanaMensaje != window.top) {
					  debug('Para postMessage, El parent es el mismo que el top');
					}else{
					  debug('Para postMessage, El parent no es el mismo que el top');
					}
					
					var _codigoDanios = '';
					var _codigoSalud  = '';
					
					try{
						_codigoDanios = _CDIDEPERsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Danios',e);
					}
					
					try{
						_codigoSalud = _CDIDEEXTsel.substring(5);
					}catch(e){
						debug('Error al obtener codigo externo de Salud',e);
					}
					
					var objMsg = {
						clienteIce: true,
						modo: 'G',
						success : json.exito,
						cdperson: _p22_cdperson,
						cdideper: _CDIDEPERsel,
						cdideext: _CDIDEEXTsel,
						codigoDanios: _codigoDanios,
						codigoSalud:_codigoSalud
					};
					
					ventanaMensaje.postMessage(objMsg, "*");
					if(_modoRecuperaDanios){
						mensajeInfo('Cliente Guardado y Recuperado: ' + _codigoDanios);
					}
					
	    		}catch(e){
	    			debugError('Error en postMessage',e);
	    		}
                
	    		
                /** PARA ACTUALIZAR EL NUEVO ESTATUS GENERAL DE LA PERSONA **/
                Ext.Ajax.request(
            	        {
            	            url       : _UrlActualizaStatusPersona
            	            ,params: {
        	            		'params.pv_cdperson_i':  _p22_fieldCdperson().getValue(),
        	            		'params.pv_cdrol_i':  '1'
        	            	}
            	            ,success  : function(response)
            	            {
            	                var jsonEstatus = Ext.decode(response.responseText);
            	                debug('response text:',jsonEstatus);
            	                if(jsonEstatus.exito)
            	                {
            	                    debug('Actualizando estatus de Persona: ');
            	                    _fieldByName('STATUS',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setValue(jsonEstatus.respuesta);
            	                    _fieldByName('STATUS',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).resetOriginalValue();
            	                    
            	                }
            	                else
            	                {
            	                    debugError('Error en actualiza estatus persona: ',jsonEstatus.respuesta);
            	                }
            	            }
            	            ,failure  : function()
            	            {
            	                debugError('Error en actualiza estatus persona');
            	            }
            	});
            }
            ,failure  : function()
            {
                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
                errorComunicacion(null,'En guardar datos adicionales');
            }
        });
        
    }
    
    if(valido && (deleteList.length > 0 || saveList.length > 0 || updateList.length > 0)){
    	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(true);
    	
    	Ext.Ajax.request(
    	        {
    	            url       : _UrlGuardaAccionista
    	            ,jsonData :
    	            {
    	            	params: {
    	            		'pv_cdperson_i':   _p22_fieldCdperson().getValue(),
        	            	'pv_cdatribu_i':  fieldEstCorp.getName().substring(fieldEstCorp.getName().length-2, fieldEstCorp.getName().length),
        	            	'pv_cdtpesco_i':  fieldEstCorp.getValue()
    	            	},
    	                'saveList'   : saveList,
    	                'deleteList' : deleteList,
    	                'updateList' : updateList
    	            }
    	            ,success  : function(response)
    	            {
    	                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
    	                var json = Ext.decode(response.responseText);
    	                debug('response text:',json);
    	                if(json.exito)
    	                {
    	                	windowAccionistas = undefined;
    	                    debug('Datos de Accionistas guardados correctamente.');
    	                }
    	                else
    	                {
    	                    mensajeError(json.respuesta);
    	                }
    	            }
    	            ,failure  : function()
    	            {
    	                _PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.setLoading(false);
    	                errorComunicacion(null,'En guardar datos de accionistas');
    	            }
    	});
	}
    
    debug('<_p22_guardarDatosAdicionalesClic');
}

function _p22_formDatosAdicionales()
{
    debug('>_p22_formDatosAdicionales<');
    return _fieldByName('_p22_formDatosAdicionales',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />);
}

function _p22_documentosClic()
{
    debug('>_p22_documentosClic');
    var windowDocsPer = Ext.create('Ext.window.Window',
    {
        title        : 'Documentos'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 600
        ,height      : 400
        ,autoScroll  : true
        ,loader      :
        {
            url       : _p22_urlPantallaDocumentos
            ,params   :
            {
                'smap1.cdperson'  : _p22_fieldCdperson().getValue()
            }
            ,scripts  : true
            ,autoLoad : true
        }
    }).show();
    centrarVentanaInterna(windowDocsPer);
    debug('<_p22_documentosClic');
}

function _p22_subirArchivo(cdperson,codidocu,descrip)
{
    debug('>_p22_subirArchivo',cdperson,codidocu,descrip);
    _p22_windowAgregarDocu=Ext.create('Ext.window.Window',
            {
                id           : '_p22_WinPopupAddDoc'
                ,title       : 'Agregar documento'
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
                        ,url         : _p22_urlSubirArchivo
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
                                ,value      : descrip
                                ,readOnly   : true
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
                                        if (false&&!Ext.Array.contains(this.cAccept, uploadedExtension))
                                        {
                                            Ext.MessageBox.show(
                                            {
                                                title   : 'Error de tipo de archivo',
                                                msg     : 'Extensiones permitidas: ' + this.cAccept.join(),
                                                buttons : Ext.Msg.OK,
                                                icon    : Ext.Msg.WARNING
                                            });
                                            me.reset();
                                            Ext.getCmp('_p22_botGuaDoc').setDisabled(true);
                                        }
                                        else
                                        {
                                            Ext.getCmp('_p22_botGuaDoc').setDisabled(false);
                                        }
                                    }
                                }
                            }
                            ,Ext.create('Ext.panel.Panel',
                            {
                                html    :'<iframe id="_p22_IframeUploadDoc" name="_p22_IframeUploadDoc"></iframe>'
                                ,hidden : true
                            })
                            ,Ext.create('Ext.panel.Panel',
                            {
                                border  : 0
                                ,html   :'<iframe id="_p22_IframeUploadPro" name="_p22_IframeUploadPro" width="100%" height="30" src="'+_p22_UrlUploadPro+'" frameborder="0"></iframe>'
                                ,hidden : false
                            })
                        ]
                        ,buttons     :
                        [
                            {
                                id        : '_p22_botGuaDoc'
                                ,text     : 'Agregar'
                                ,icon     : '${ctx}/resources/fam3icons/icons/disk.png'
                                ,disabled : true
                                ,handler  : function (button,e)
                                {
                                    debug(button.up().up().getForm().getValues());
                                    button.setDisabled(true);
                                    Ext.getCmp('_p22_BotCanDoc').setDisabled(true);
                                    Ext.create('Ext.form.Panel').submit(
                                    {
                                        url             : _p22_UrlUploadPro
                                        ,standardSubmit : true
                                        ,target         : '_p22_IframeUploadPro'
                                        ,params         :
                                        {
                                            uploadKey : '1'
                                        }
                                    });
                                    button.up().up().getForm().submit(
                                    {
                                        standardSubmit : true
                                        ,target        : '_p22_IframeUploadDoc'
                                        ,params        :
                                        {
                                            'smap1.cdperson'    : cdperson
                                            ,'smap1.codidocu'   : codidocu
                                            ,'smap1.callbackFn' : 'callbackDocumentoSubidoPersona'
                                        }
                                    });
                                }
                            }
                            ,{
                                id       : '_p22_BotCanDoc'
                                ,text    : 'Cancelar'
                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                ,handler : function (button,e)
                                {
                                    _p22_windowAgregarDocu.destroy();
                                }
                            }
                        ]
                    })
                ]
            }).show();
            centrarVentanaInterna(_p22_windowAgregarDocu);
    debug('<_p22_subirArchivo');
}

function _p22_cargarArchivo(cdperson,codidocu,dsdocume)
{
    debug('>_p22_cargarArchivo',cdperson,codidocu,dsdocume);
    _p22_principalDatosAdicionales().setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p22_urlCargarNombreArchivo
        ,params  :
        {
            'smap1.cdperson'  : cdperson
            ,'smap1.codidocu' : codidocu
        }
        ,success : function(response)
        {
            _p22_principalDatosAdicionales().setLoading(false);
            var json=Ext.decode(response.responseText);
            debug('json response:',json);
            if(json.exito)
            {
                var numRand=Math.floor((Math.random()*100000)+1);
                debug('numRand a: ',numRand);
                var windowVerDocu=Ext.create('Ext.window.Window',
                {
                    title          : dsdocume
                    ,width         : 700
                    ,height        : 500
                    ,collapsible   : true
                    ,titleCollapse : true
                    ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                     +'src="'+_p22_urlViewDoc+'?path='+_RUTA_DOCUMENTOS_PERSONA+'&subfolder='+cdperson+'&filename='+json.smap1.cddocume+'">'
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
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            _p22_principalDatosAdicionales().setLoading(false);
            errorComunicacion(null,'En cargar archivo');
        }
    });
    
    debug('<_p22_cargarArchivo');
}

callbackDocumentoSubidoPersona = function()
{
	
    _p22_windowAgregarDocu.destroy();
    
    var elemento = _fieldByName(_DocASubir,_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />,true);
    
    if(!Ext.isEmpty(elemento.store)){
    	elemento.setValue('S');
    }
    
    (Ext.ComponentQuery.query('[docCargado='+_DocASubir+'ImgDocId]',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />)[Ext.ComponentQuery.query('[docCargado='+_DocASubir+'ImgDocId]',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).length-1]).setSrc('${ctx}/resources/fam3icons/icons/accept.png');
    
};

/**
 * 	PARA ANTES DE IR A MODO AGREGAR 
 */
function fijarCamposAgregar(forma){

	/**
	 * Reinicia los campos a un estado inicial de la pantalla, TODO: guardar esta configuracion de la primer vez que se carga la pantalla 
	 */
	forma.items.each(function(item,index,len){
		
		if(item.xtype && (item.xtype == 'button' || item.xtype == 'displayfield' || item.xtype == 'panel')) return;
		
		if(item.getName && (item.getName() == 'STATUS' || item.getName() == 'DSL_CDIDEPER' || item.getName() == 'DSL_CDIDEEXT' || item.getName() == 'NONGRATA')){
			
			item.setReadOnly(true); 
			
			/**
			 *	Para el modo agregar se fijan los valores originales iniciales en blanco 
			 */
			item.setValue('');
			item.resetOriginalValue();
		}else if(item.getName){
			
			item.setReadOnly(false); 
			
			/**
			 *	Para el modo agregar se fijan los valores originales iniciales en blanco 
			 */
			item.setValue('');
			
			if(item.getName() == 'FEINGRESO'){
				item.setValue(new Date());
			}
			
			item.resetOriginalValue();
		}
		
    });

}

/**
 * 	PARA CONFIGURAR CAMPOS SOLO LECTURA U OCULTO ANTES DE BLOQUEAR CAMPOS NO EDITABLES RESTANTES en la funcion fijarCamposEditables
 */
function configuraCampos(forma){

	try{
		forma.items.each(function(item,index,len){
			
			if(item.xtype && (item.xtype == 'button' || item.xtype == 'displayfield' || item.xtype == 'panel')) return;
			
			if(item.getName && item.getName() == "CODPOSTAL" && !Ext.isEmpty(_cargaCP)) return;
			if(item.getName && item.getName() == "OTFISJUR"  && !Ext.isEmpty(_cargaTipoPersona)) return;
			if(item.getName && item.getName() == "CDSUCEMI"  && !Ext.isEmpty(_cargaSucursalEmi)) return;
			
			Ext.Array.forEach(_camposAconfigurar,function(campoConf) {
				
				if(campoConf.CVECAMPO == "AGREGARDOM"){
					if(campoConf.OCULTO == "S"){
						_fieldByName('AgregaDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					}else{
						_fieldByName('AgregaDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
					}
				}
				
				if(campoConf.CVECAMPO == "ESTATUSDOM"){
					if(campoConf.OCULTO == "S"){
						_fieldByName('EstatusDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).hide();
					}else{
						_fieldByName('EstatusDomBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
					}
				}

				
				if(campoConf.CVECAMPO == "PERMITEDUPLICAR"){
					if(campoConf.OCULTO == "S"){
						_permiteDuplicarRFC = true;
					}else{
						_permiteDuplicarRFC = false;
					}
				}

				
				if(campoConf.CVECAMPO.indexOf("pv_otvalor") >=0 && campoConf.CVECAMPO.indexOf("parametro") < 0){
					campoConf.CVECAMPO = "parametros."+campoConf.CVECAMPO;
				}
				
				if(item.getName && item.getName() == campoConf.CVECAMPO /** TODO: && campoConf.CVECAMPO != 'CDRFC' */
					&& campoConf.CVECAMPO != 'STATUS' && campoConf.CVECAMPO != 'RESIDENTE' && campoConf.CVECAMPO != 'NMORDDOM'){
				
					_camposConfigurados.push(campoConf.CVECAMPO);
					
					if(campoConf.OCULTO == "S"){
			    		item.hide();
			    	}else{
			    		item.show();
			    	}
			    	
			    	if(campoConf.SOLOLECTURA == "S"){
			    		item.setReadOnly(true);	
			    	}else{
			    		if(campoConf.CVECAMPO != 'DSL_CDIDEPER' && campoConf.CVECAMPO != 'DSL_CDIDEEXT'){
			    			item.setReadOnly(false);
			    		}
			    	}
			    	
			    	if(campoConf.CVECAMPO == "CDRFC" && campoConf.SOLOLECTURA == "N"){
			    		_fieldByName('GeneraRFCBtn',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).show();
			    	}
				}					    
		});
    });
	}catch(e){
		mensajeError('Error al configurar campos del cliente. Consulte a Soporte.');
		debugError('Error al configurar campos. Consulte a Soporte.',e);
	}

}


function fijarCamposEditables(forma, modoEdicion){

	forma.items.each(function(item,index,len){
		
		if(item.xtype && (item.xtype == 'button' || item.xtype == 'displayfield' || item.xtype == 'panel')) return;
		
		/**
		 * PARA CAMPOS POR DEFAULT VISIBLES Y FUNCIONALES
		 */
		if(item.isVisible()){
	    	if( ((!item.allowBlank) || item.getName() == 'DSAPELLIDO' || item.getName() == 'DSAPELLIDO1' || item.getName() == 'OTSEXO')
	    	     && Ext.isEmpty(item.getValue()) ){
	    		item.setReadOnly(false); //abre campos obligatorios visibles	
	    	}else{
	    		if(modoEdicion){
	    			if(_camposConfigurados.indexOf(item.getName()) < 0 ){
	    				item.setReadOnly(true); // bloquea demas campos, si no se configuraron previamente
	    			}
	    		}
	    	}		
		
		}else if( ((!item.allowBlank) || item.getName() == 'DSAPELLIDO' || item.getName() == 'DSAPELLIDO1' || item.getName() == 'OTSEXO')
	    	     && Ext.isEmpty(item.getValue()) ){
	    	     	
			item.setReadOnly(false);//abre campos obligatorios invisibles
			item.show(); // muestra campos obligatorios invisibles
		}
		
    });

}

function fijarCamposEditablesAdicionales(forma, modoEdicion){

	forma.items.each(function(item,index,len){
		
		if(item.xtype && (item.xtype == 'button' || item.xtype == 'displayfield' || item.xtype == 'panel')) return;
		
		/**
		 * PARA CAMPOS POR DEFAULT VISIBLES Y FUNCIONALES
		 */
		if(item.isVisible()){
	    	if(item.getName && !Ext.isEmpty(item.getValue()) && _camposConfigurados.indexOf(item.getName()) < 0  ){
	    		item.setReadOnly(true); //bloquea campos con valor previamente no configurados
	    	}
		}else{
			if(item.getName && !Ext.isEmpty(item.getValue())){
				//Bloquea Telefono
	    		if(item.getName() == "parametros.pv_otvalor38" && _camposConfigurados.indexOf('TELEFONO') < 0  ){
	    			_fieldByName('TELEFONO',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(true);
	    		}
	    		//Bloquea Email
	    		if(item.getName() == "parametros.pv_otvalor39" && _camposConfigurados.indexOf('EMAIL') < 0  ){
	    			_fieldByName('EMAIL',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(true);
	    		}
	    		//Bloquea Telefono2
	    		if(item.getName() == "parametros.pv_otvalor51" && _camposConfigurados.indexOf('TELEFONO2') < 0  ){
	    			_fieldByName('TELEFONO2',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(true);
	    		}
	    		//Bloquea Telefono3
	    		if(item.getName() == "parametros.pv_otvalor52" && _camposConfigurados.indexOf('TELEFONO3') < 0  ){
	    			_fieldByName('TELEFONO3',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).setReadOnly(true);
	    		}
			}
		} 	
    });

}

function agregaEditaDomicilio(editMode, recordCargar){
	
	debug('Editar Domicilio para recordCargar',editMode);
	
	if(editMode && recordCargar.get('CODPOSTAL') == '00000' ){
	    recordCargar.set('CODPOSTAL','000000');// Para ver cuando un cdigo postal importado viene en cero.
	}
	
	var windowDomicilio;

	var formDomicilio = Ext.create('Ext.form.Panel',
                    {
                        title     : 'Domicilio de la Persona'
                        ,border   : 0
                        ,defaults : { style : 'margin:5px' }
                        ,layout   :
                        {
                            type     : 'table'
                            ,columns : 2
                        }
                        ,items    : [ <s:property value="imap.itemsDomicilio" escapeHtml="false" /> ]
                        ,buttonAlign : 'center'
                        ,buttons: [{
					        text: 'Aceptar',
					        handler: function() {
					        	
					        	var valido = true;
					        	
						    	valido = (_fieldByName('CDEDO',formDomicilio).getStore().getCount()>0) && (_fieldByName('CDMUNICI',formDomicilio).getStore().getCount()>0) && _fieldByName('CDEDO',formDomicilio).isValid() && _fieldByName('CDMUNICI',formDomicilio).isValid();
						        
						        if(!valido)
						        {
						            mensajeWarning('Favor de verificar el estado y municipio.');
						            return;
						        }
						        
					            if(this.up('form').getForm().isValid()){
					            	
					            	var nuevoRec = this.up('form').getForm().getValues();
					            	if(editMode){
              
					            		recordCargar.set('NMORDDOM',_fieldByName('NMORDDOM',formDomicilio).getValue());
					            		recordCargar.set('CDTIPDOM',_fieldByName('CDTIPDOM',formDomicilio).getValue());
					            		recordCargar.set('DSTIPDOM',_fieldByName('CDTIPDOM',formDomicilio).getRawValue());
					            		recordCargar.set('CODPOSTAL',_fieldByName('CODPOSTAL',formDomicilio).getValue());
					            		recordCargar.set('CDEDO',_fieldByName('CDEDO',formDomicilio).getValue());
					            		recordCargar.set('ESTADO',_fieldByName('CDEDO',formDomicilio).getRawValue());
					            		recordCargar.set('CDMUNICI',_fieldByName('CDMUNICI',formDomicilio).getValue());
					            		recordCargar.set('MUNICIPIO',_fieldByName('CDMUNICI',formDomicilio).getRawValue());
					            		recordCargar.set('CDCOLONI',_fieldByName('CDCOLONI',formDomicilio).getValue());
					            		recordCargar.set('COLONIA',_fieldByName('CDCOLONI',formDomicilio).getRawValue());
					            		recordCargar.set('DSDOMICI',_fieldByName('DSDOMICI',formDomicilio).getValue());
					            		recordCargar.set('NMNUMERO',_fieldByName('NMNUMERO',formDomicilio).getValue());
					            		recordCargar.set('NMNUMINT',_fieldByName('NMNUMINT',formDomicilio).getValue());
					            		
					            		if(!Ext.isEmpty(municipioImportarTMP) && editMode && recordCargar.get('NMORDDOM') == "1"){
					            			municipioImportarTMP = '';
					            		}
					            		if(!Ext.isEmpty(coloniaImportarTMP) && editMode && recordCargar.get('NMORDDOM') == "1"){
					            			coloniaImportarTMP = '';
					            		}
					            		
					            	}else{
						            	nuevoRec.ESTADO = _fieldByName('CDEDO',formDomicilio).getRawValue();
						            	nuevoRec.MUNICIPIO = _fieldByName('CDMUNICI',formDomicilio).getRawValue();
						            	nuevoRec.COLONIA = _fieldByName('CDCOLONI',formDomicilio).getRawValue();
						            	nuevoRec.DSTIPDOM = _fieldByName('CDTIPDOM',formDomicilio).getRawValue();
						            	nuevoRec.SWACTIVO = 'S';
					            	
					            	 	storeDomiciliosCliente.add(new DomiciliosModel(nuevoRec));
					            	 	
					            	 	if(_domicilioSimple && storeDomiciliosCliente.getCount()==1){
											_numDomicilioTomado = storeDomiciliosCliente.getAt(0);
											_numDomicilioTomado.set('SEL',true);
										}
					            	}
					            	
					           		 windowDomicilio.close();
					            }else{
					            	mensajeWarning('Llene la informaci&oacute;n requerida.');	
					            }
					            
					        }
					    }] 
                    });
                    
			//Se carga colonia porque en ocasiones no se carga hasta la segunda vez
            _fieldByName('CDCOLONI',formDomicilio).getStore().load();
            
			windowDomicilio = Ext.create('Ext.window.Window', {
	          title: editMode?'Editar Domicilio':'Agregar Domicilio',
	          modal:true,
	          height : 300,
	          width  : 550,
		      items: [formDomicilio]
	        });
	        
	        /**
	         *	PARA PERMITIR LLENAR VALORES SIN PROBLEMA 
	         */
	        	_fieldByName('CDEDO',formDomicilio).editable = true;
				_fieldByName('CDEDO',formDomicilio).forceSelection = false;
	        	_fieldByName('CDMUNICI',formDomicilio).editable = true;
				_fieldByName('CDMUNICI',formDomicilio).forceSelection = false;
	        	_fieldByName('CDCOLONI',formDomicilio).editable = true;
				_fieldByName('CDCOLONI',formDomicilio).forceSelection = false;
	        	_fieldByName('CDTIPDOM',formDomicilio).editable = true;
				_fieldByName('CDTIPDOM',formDomicilio).forceSelection = false;
				
				/**
				 * Regex para letras numeros, espacios, acentos y guines
				 */
				_fieldByName('NMNUMERO',formDomicilio).regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
				_fieldByName('NMNUMERO',formDomicilio).regexText = 'Solo d&iacute;gitos, letras, espacios y guiones';
			    _fieldByName('NMNUMINT',formDomicilio).regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
			    _fieldByName('NMNUMINT',formDomicilio).regexText = 'Solo d&iacute;gitos, letras, espacios  y guiones';
				
			    
			    _fieldByName('NMORDDOM',formDomicilio).allowBlank = true;
				
				/**
				 *	CUANDO SE SELECCIONA ESCRIBE PARA CREAR UNA COLONIA SE PONE EN MAYUSCULAS
				 */
		        _fieldByName('CDCOLONI',formDomicilio).on({
						change: function(me, val){
			    				try{
				    				if('string' == typeof val){
				    					me.setValue(val.toUpperCase());
				    				}
			    				}
			    				catch(e){
			    					debug(e);
			    				}
						}
				});
				
		        _fieldByName('CDTIPDOM',formDomicilio).on({
					change: function(me, val){
		    				try{
			    				if(val == '26'){
			    					_fieldByName('CDEDO',formDomicilio).setFieldLabel('REGI&Oacute;N');
			    					_fieldByName('CDMUNICI',formDomicilio).setFieldLabel('PA&Iacute;S');
			    				}else{
			    					_fieldByName('CDEDO',formDomicilio).setFieldLabel('ESTADO');
			    					_fieldByName('CDMUNICI',formDomicilio).setFieldLabel('MUNICIPIO');
			    				}
		    				}
		    				catch(e){
		    					debug(e);
		    				}
					}
				});
				
	        windowDomicilio.show(null,function(){
	        	
	        	formDomicilio.setLoading(true);
	
				if(editMode){
		        	formDomicilio.loadRecord(recordCargar);
		        	heredarPanel(formDomicilio);
		        	
		        	setTimeout(function(){
		        		if(_camposAconfigurar){
					    	configuraCampos(formDomicilio);
					    }
			    
		        		if(_editaIncompleto){
		        			/**
		        			 * NO SE BLOQUEAN LOS CAMPOS CON VALORES DE UN DOMICILIO QUE SE ACABA DE AGREGAR
		        			 */
		        			if(!Ext.isEmpty(recordCargar.get('NMORDDOM')) && !Ext.Array.contains(_domiciliosNuevos,recordCargar.get('NMORDDOM'))){
		        				if(!_modoEdicionDomicilio){
		        					fijarCamposEditables(formDomicilio,_editandoCliente);		
		        				}
					    	}
					    }
					    
					    if(!_fieldByName('CODPOSTAL',formDomicilio).isValid()){
					    	_fieldByName('CODPOSTAL',formDomicilio).setReadOnly(false);
					    }
					    if(!_fieldByName('NMNUMERO',formDomicilio).isValid()){
					    	_fieldByName('NMNUMERO',formDomicilio).setReadOnly(false);
					    }
					    if(!_fieldByName('NMNUMINT',formDomicilio).isValid()){
					    	_fieldByName('NMNUMINT',formDomicilio).setReadOnly(false);
					    }

					    
						setTimeout(function(){
							
							/**
							 *	Se hereda la colonia ya que se queda la clave, no cambiar orden o darle el tiempo necesario 
							 */
							_p22_heredarColonia();
							heredarPanel(formDomicilio);

							/**
					     *	PARA SELECCIONAR UN MUNICIPIO NO ENCONTRADO O FIJAR LA COLONIA NO ENCONTRADA PARA CREAR UNA NUEVA 
					     */
					    
						    if(!Ext.isEmpty(municipioImportarTMP) && editMode && recordCargar.get('NMORDDOM') == "1"){
					    		_fieldByName('CDMUNICI',formDomicilio).setReadOnly(false);
					    		_fieldByName('CDMUNICI',formDomicilio).show();
					    		
					    		var mensajeColImport =  ''; 
					    		if(!Ext.isEmpty(coloniaImportarTMP)){
									mensajeColImport = " " + coloniaImportarTMP;
									
									_fieldByName('CDCOLONI',formDomicilio).setReadOnly(false);
					    			_fieldByName('CDCOLONI',formDomicilio).show();
					    			_fieldByName('CDCOLONI',formDomicilio).setValue(coloniaImportarTMP);
					    		}
					    		
						    	mensajeInfo("No se encontr&oacute; el municipio: '" + municipioImportarTMP + "' seleccione el municipio y colonia"+mensajeColImport+" correctos.");
						    }else if(!Ext.isEmpty(coloniaImportarTMP) && editMode && recordCargar.get('NMORDDOM') == "1"){
								_fieldByName('CDCOLONI',formDomicilio).setReadOnly(false);
				    			_fieldByName('CDCOLONI',formDomicilio).show();
				    			_fieldByName('CDCOLONI',formDomicilio).setValue(coloniaImportarTMP);
				    			
						    	mensajeInfo("No se encontr&oacute; la colonia: '" + coloniaImportarTMP + "' seleccione la colonia correcta, &oacute; escriba el nombre de la nueva colonia.");
						    }
					    
							
							/**
							 *	PARA REGRESAR CAMPOS QUE SOLO SE PUEDE SELECCIONAR DE LA LISTA 
							 */
							
							_fieldByName('CDEDO',formDomicilio).forceSelection = true;
							_fieldByName('CDMUNICI',formDomicilio).forceSelection = true;
							_fieldByName('CDTIPDOM',formDomicilio).forceSelection = true;
							
							windowDomicilio.doLayout();
							
							if(!_fieldByName('CODPOSTAL',formDomicilio).readOnly){
					        	_fieldByName('CDEDO',formDomicilio).setReadOnly(false);
					        	_fieldByName('CDMUNICI',formDomicilio).setReadOnly(false);
					        	_fieldByName('CDCOLONI',formDomicilio).setReadOnly(false);
					        }else if(!_fieldByName('CDEDO',formDomicilio).readOnly){
					        	_fieldByName('CDMUNICI',formDomicilio).setReadOnly(false);
					        	_fieldByName('CDCOLONI',formDomicilio).setReadOnly(false);
					        }else if(!_fieldByName('CDMUNICI',formDomicilio).readOnly){
					        	_fieldByName('CDCOLONI',formDomicilio).setReadOnly(false);
					        }
					        
					        setTimeout(function(){
					        	formDomicilio.setLoading(false);
					        },700);
					        
				        },700);
					},700);
		        	
		        }else{
		        	
		        	/**
					 *	PARA REGRESAR CAMPOS QUE SOLO SE PUEDE SELECCIONAR DE LA LISTA 
					 */
					
					_fieldByName('CDEDO',formDomicilio).forceSelection = true;
					_fieldByName('CDMUNICI',formDomicilio).forceSelection = true;
					_fieldByName('CDTIPDOM',formDomicilio).forceSelection = true;
							
		        	if(_esCargaClienteNvo && !Ext.isEmpty(_cargaCP)){
						_fieldByName('CODPOSTAL',formDomicilio).setValue(_cargaCP);
						_fieldByName('CODPOSTAL',formDomicilio).setReadOnly(true);
						
						setTimeout(function(){
							_p22_heredarColonia();
							heredarPanel(formDomicilio);
					    },3000);
					}
					
					    
					formDomicilio.setLoading(false);
		        }	        	
		        
	        });


	function _p22_heredarColonia(callbackload)
	{
	    debug('>_p22_heredarColonia');
	    var comboColonias  =_fieldByName('CDCOLONI',formDomicilio);
	    var comboCodPostal = _fieldByName('CODPOSTAL',formDomicilio);
	    var codigoPostal   = comboCodPostal.getValue();
	    debug('comboColonias:',comboColonias,'comboCodPostal:',comboCodPostal);
	    debug('codigoPostal:',codigoPostal);
	    comboColonias.getStore().load(
	    {
	        params :
	        {
	            'params.cp' : codigoPostal
	        }
	        ,callback : function()
	        {
	            var hay=false;
	            comboColonias.getStore().each(function(record)
	            {
	                if(comboColonias.getValue()==record.get('key'))
	                {
	                    hay=true;
	                }
	            });
	            if(!hay)
	            {
	                comboColonias.setValue('');
	            }
	            
	            try{
	            	callbackload();	
	            }catch(e){
	            	debug("Excepcion al ejecutar callback");
	            }
	            
	        }
	    });
	    debug('<_p22_heredarColonia');
}

_fieldByName('CODPOSTAL',formDomicilio).addListener('blur',function(){
	_fieldByName('CDCOLONI',formDomicilio).setValue('');
	_p22_heredarColonia();
});

}


function verEditarAccionistas(cdperson, cdatribu, cdestructcorp){
	
	
	if(Ext.isEmpty(cdestructcorp)){
		mensajeWarning('Debe seleccionar una Estructura Coorporativa.');
		return;
	}
	
	if(!windowAccionistas){
		Ext.define('modeloAccionistas',{
	        extend  : 'Ext.data.Model'
	        ,fields :
	        [
	            'NMORDINA'
	            ,'DSNOMBRE'
	            ,'CDNACION'
	            ,'PORPARTI'
	        ]
		});
		
		accionistasStore = Ext.create('Ext.data.Store',
			    {
					pageSize : 20,
			        autoLoad : true
			        ,model   : 'modeloAccionistas'
			        ,proxy   :
			        {
			            type         : 'memory'
			            ,enablePaging : true
			            ,reader      : 'json'
			            ,data        : []
			        }
			    });
		
		gridAccionistas = Ext.create('Ext.grid.Panel',
		    {
		    title    : 'Para Editar un Accionista de Doble Clic en la fila deseada.'
		    ,height  : 200
		    ,plugins : Ext.create('Ext.grid.plugin.RowEditing',
		    {
		    	pluginId: 'accionistasRowId',
		        clicksToEdit  : 2,
		        errorSummary : false,
		        listeners: {
		    		beforeedit: function(){
		    			_0_botAceptar.disable();
		    		},
		    		edit: function(){
		    			_0_botAceptar.enable();
		    		},
		    		canceledit: function(){
		    			_0_botAceptar.enable();
		    		}
		    	}
		        
		    })
		    ,tbar     :
		        [
		            {
		                text     : 'Agregar'
		                ,icon    : '${ctx}/resources/fam3icons/icons/add.png'
		                ,handler : function(){
		                	accionistasStore.add(new modeloAccionistas());
		                	gridAccionistas.getPlugin('accionistasRowId').startEdit(accionistasStore.getCount()-1,0);
		                }
		            },{
		                text     : 'Eliminar'
			                ,icon    : '${ctx}/resources/fam3icons/icons/delete.png'
			                ,handler : function(){
			                	accionistasStore.remove(gridAccionistas.getSelectionModel().getSelection());
			                }
			            }
		        ]
		    ,columns :
		    [
		        {
		            header     : 'Accionista'
		            ,dataIndex : 'DSNOMBRE'
		            ,flex      : 1
		            ,editor    :
		            {
		                xtype             : 'textfield'
		                ,allowBlank       : false
		            }
		        }
		        ,{
		            header     : 'Nacionalidad'
		            ,dataIndex : 'CDNACION'
		            ,flex      : 1
		            ,renderer  : function(valor){
		            	return rendererColumnasDinamico(valor,'CDNACION'); 
		            }
		            ,editor    :
		            {
		                xtype         : 'combobox',
		                allowBlank    : false,
		                name          : 'CDNACION',
		                valueField    : 'key',
		                displayField  : 'value',
		                forceSelection: true,
		                typeAhead     : true,
		                anyMatch      : true,
		                store         : Ext.create('Ext.data.Store', {
		                    model     : 'Generic',
		                    autoLoad  : true,
		                    proxy     : {
		                        type        : 'ajax'
		                        ,url        : _URL_CARGA_CATALOGO
		                        ,extraParams: {catalogo:_CAT_NACIONALIDAD}
		                        ,reader     :
		                        {
		                            type  : 'json'
		                            ,root : 'lista'
		                        }
		                    }
		                })
		               
		            }
		        }
		        ,{
		            header     : 'Porcentaje Participaci&oacute;n'
		            ,dataIndex : 'PORPARTI'
		            ,flex      : 1
		            ,editor    :
		            {
		                xtype             : 'numberfield'
		                ,allowBlank       : false
		                ,allowDecimals    : true
		                ,minValue         : 0
		                ,negativeText     : 'No se puede introducir valores negativos.'
		                ,decimalSeparator : '.'
		            }
		        }
		    ]
		    ,store : accionistasStore
		});
		
		
		_0_botAceptar = Ext.create('Ext.Button',{
            text: 'Aceptar',
            icon:'${ctx}/resources/fam3icons/icons/accept.png',
            handler: function() {
           	 windowAccionistas.close();
            }
      	});
		
		windowAccionistas = Ext.create('Ext.window.Window', {
	          title: 'Accionistas',
	          closeAction: 'close',
	          modal:true,
	          closable: false,
	          height : 320,
	          width  : 800,
		      items: [gridAccionistas],
	          bodyStyle:'padding:15px;',
	          buttons:[_0_botAceptar]
	        });
		
			var params = {
				'params.pv_cdperson_i' : cdperson,
				'params.pv_cdatribu_i' : cdatribu,
				'params.pv_cdtpesco_i' : cdestructcorp
			};
			
			cargaStorePaginadoLocal(accionistasStore, _UrlCargaAccionistas, 'slist1', params, function (options, success, response){
	    		if(success){
	                var jsonResponse = Ext.decode(response.responseText);
	                
	                if(!jsonResponse.success) {
	                    showMessage(_MSG_SIN_DATOS, _MSG_BUSQUEDA_SIN_DATOS, Ext.Msg.OK, Ext.Msg.INFO);
	                }
	            }else{
	                showMessage('Error', 'Error al obtener los datos.', Ext.Msg.OK, Ext.Msg.ERROR);
	            }
	    	}, gridAccionistas);
	}
	
			windowAccionistas.show();
			
}
//fin funciones on ready    
    
/**
 * Funcion que recupera la informacion del contratante actual, a ejecutar desde fuera de esta pantalla, iguala una variable declarada desde fuera 
 * Debe estar declarada antes de crear esta pantalla con el id de pantalla correspondiente. 
 */
destruirContLoaderPersona<s:property value="smap1.idPantalla" /> = function(){
	
	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.query().forEach(function(element){
		try{
			element.destroy();
		}catch(e){
			debug('Error al destruir en Pantalla de Clientes',e);
		}
	});
	
	try{
		_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.destroy();	
	}catch(e){
		debug('Error al destruir Panel Principal en Pantalla de Clientes',e);
	}
	
	try{
		windowAccionistas.destroy();	
	}catch(e){
		debug('Error al destruir Window accionistas en Pantalla de Clientes',e);
	}
};

/**
 * Funcion que recupera la informacion del contratante actual, a ejecutar desde fuera de esta pantalla, sin necesidad de declararla desde fuera 
 */

destruirLoaderContratante<s:property value="smap1.idPantalla" /> = function(){
	
	_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.query().forEach(function(element){
		try{
			element.destroy();
		}catch(e){
			debug('Error al destruir en Pantalla de Clientes',e);
		}
	});
	
	try{
		_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />.destroy();	
	}catch(e){
		debug('Error al destruir Panel Principal en Pantalla de Clientes',e);
	}
	
	try{
		windowAccionistas.destroy();	
	}catch(e){
		debug('Error al destruir Window accionistas en Pantalla de Clientes',e);
	}
};

/**
 * Funcion que recupera la informacion del contratante actual, a ejecutar desde fuera de esta pantalla, iguala una variable declarada desde fuera 
 * Debe estar declarada antes de crear esta pantalla con el id de pantalla correspondiente. 
 */

obtieneDatosClienteContratante<s:property value="smap1.idPantalla" /> = function(){
	var datosPersona = {
		cdperson: (_p22_cdperson != false && !Ext.isEmpty(_p22_cdperson))? _p22_cdperson : '',
		fenacimi: _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		sexo:     _p22_fieldSexo().getValue(),
		tipoper:  _p22_fieldTipoPersona().getValue(),
		naciona:  _p22_fielCdNacion().getValue(),
		nombre:   _p22_fieldNombre().getValue(),
		snombre:  _p22_fieldSegundoNombre().getValue(),
		appat:    _p22_fieldApat().getValue(),
		apmat:    _p22_fieldAmat().getValue(),
		rfc:      _p22_fieldRFC().getValue(),
		cdideper: _fieldByName('CDIDEPER',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		cdideext: _fieldByName('CDIDEEXT',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		cdestciv: _fieldByLabel('Estado Civil',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		codpos:   (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CODPOSTAL') : '',
		cdedo:    (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CDEDO') : '',
		cdmunici: (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CDMUNICI') : '',
		nomRecupera: _nombreComContratante
		
	}
	
	return datosPersona;
};

/**
 * Funcion que recupera la informacion del contratante actual, a ejecutar desde fuera de esta pantalla, sin necesidad de declararla desde fuera 
 */

obtDatLoaderContratante<s:property value="smap1.idPantalla" /> = function(){
	
	var datosPersona = {
		cdperson: (_p22_cdperson != false && !Ext.isEmpty(_p22_cdperson))? _p22_cdperson : '',
		fenacimi: _fieldByName('FENACIMI',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		sexo:     _p22_fieldSexo().getValue(),
		tipoper:  _p22_fieldTipoPersona().getValue(),
		naciona:  _p22_fielCdNacion().getValue(),
		nombre:   _p22_fieldNombre().getValue(),
		snombre:  _p22_fieldSegundoNombre().getValue(),
		appat:    _p22_fieldApat().getValue(),
		apmat:    _p22_fieldAmat().getValue(),
		rfc:      _p22_fieldRFC().getValue(),
		cdideper: _fieldByName('CDIDEPER',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		cdideext: _fieldByName('CDIDEEXT',_PanelPrincipalPersonas<s:property value="smap1.idPantalla" />).getValue(),
		codpos:   (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CODPOSTAL') : '',
		cdedo:    (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CDEDO') : '',
		cdmunici: (_tomarUnDomicilio&&_numDomicilioTomado)?_numDomicilioTomado.get('CDMUNICI') : '',
		nomRecupera: _nombreComContratante
	}
	
	return datosPersona;
};

function checarBenef(callback)
{  
	if(typeof inputCdramo === 'undefined')
	{
		callback();
	}
	else(!Ext.isEmpty(inputCdramo) && !Ext.isEmpty(_fieldLikeLabel('Fecha de nacimiento',null,true)) && _p22_fieldTipoPersona().getValue()=='F')
    {
		var fecnam= _fieldLikeLabel('Fecha de nacimiento').getRawValue();debug('Fecha de nacimiento original del contratante:',fecnam);
		     Ext.Ajax.request(
		     {
		         url     : _URL_urlCargarTvalosit
		         ,params :
		         {
		             'smap1.cdunieco'  : inputCdunieco
		             ,'smap1.cdramo'   : inputCdramo
		             ,'smap1.estado'   : inputEstado
		             ,'smap1.nmpoliza' : inputNmpoliza
		             ,'smap1.nmsituac' : '1'
		         }
		         ,success : function(response)
		         {
		             var json=Ext.decode(response.responseText);debug('### tvalosit:',json);
		             if(json.exito)
		             {
		                     var _p22_validaFecha = json.smap1['parametros.pv_otvalor56'];debug('Fecha de nacimiento recien capturada por el contratante: ',_p22_validaFecha);
		                     var _p22_validaSeguro = json.smap1['parametros.pv_seguroVida'];debug('¿Tiene seguro de vida?',_p22_validaSeguro);
		                     if( _p22_validaSeguro=="S" && _p22_validaFecha+'X' != fecnam+'X')
		                     {    
		                    	 mensajeWarning('No se puede amparar la cobertura de Seguro de Vida debido a inconsistencias en la fecha de nacimiento del contratante ingresada en la cotización ('+_p22_validaFecha+') y la asociada al cliente seleccionado ('+fecnam+').\n  Por favor generar una nueva cotización o seleccionar otro cliente.');
		                     }
		                     else
		                     {
		                     	callback();
		                     }
		             }
		             else
		             {
		                 mensajeError(json.respuesta);
		             }
		         }
		     });
    }
};

});


////// funciones //////

//}//fin de if para que no se duplique codigo
</script>
</head>
<body>
<div id="_p22_divpri" style="height : 1400px;"></div>
</body>
</html>