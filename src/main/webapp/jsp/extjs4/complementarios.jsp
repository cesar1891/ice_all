<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%--<style>
        div.claseTitulo>div.x-panel-header>div.x-header-body>div.x-box-inner>div.x-box-target>div.x-panel-header-text-container>span.x-header-text
        {
            font-size:16px;
            font-weight: bold;
            text-transform: uppercase;
        }
        </style>--%>
<%--////////////////////////////////////
        ////// para el parser de archivos //////
        ////////////////////////////////////--%
        <script>var urlFrameArchivo='<s:url namespace="/" action="subirArchivoMostrarPanel" />';</script>
        <script type="text/javascript" src="${ctx}/resources/extjs4/jQuerySubirArchivosParser.js"></script>
        <%--////////////////////////////////--%>
<!--// para el parser de archivos //////
        /////////////////////////////////////-->
<!--<script src="${ctx}/resources/jsp-script/extjs4/complementarios.js"></script>-->
<script>
            var contexto='${ctx}';
            
            
            var panDatComMap1 = <s:property value="%{convertToJSON('map1')}" escapeHtml="false" />;
            debug('panDatComMap1:',panDatComMap1,'.');
            
            var panDatComFlujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
            debug('panDatComFlujo:',panDatComFlujo,'.');
            var tvalopol = <s:property value="%{convertToJSON('map4')}" escapeHtml="false"/>;
            var inputCdunieco = '<s:property value="cdunieco"         />';
            var inputCdramo   = '<s:property value="cdramo"           />';
            var inputEstado   = '<s:property value="estado"           />';
            var inputNmpoliza = '<s:property value="nmpoliza"         />';
            var inputNtramite = '<s:property value='map1.ntramite'    />';
            var inputCdtipsit = '<s:property value='cdtipsit'         />';
            var sesionDsrol   = '<s:property value="map1.sesiondsrol" />';
            debug("inputNtramite",inputNtramite);
            
            //var urlCargarCatalogos         = '<s:url namespace="/flujocotizacion" action="cargarCatalogos"             />';
            var urlGuardar                   = '<s:url namespace="/"                action="guardarDatosComplementarios" />';
            var urlCargar                    = '<s:url namespace="/"                action="cargarDatosComplementarios"  />';
            var urlCargarCatalogos           = '<s:url namespace="/catalogos"       action="obtieneCatalogo"             />';
            var urlRecotizar                 = '<s:url namespace="/"                action="recotizar"                   />';
            var urlEmitir                    = '<s:url namespace="/"                action="emitir"                      />';
            var urlReintentarWS              = '<s:url namespace="/"                action="reintentaWSautos"            />';
            var panDatComUrlDoc              = '<s:url namespace="/documentos"      action="ventanaDocumentosPoliza"     />';
            var panDatComUrlDoc2             = '<s:url namespace="/documentos"      action="ventanaDocumentosPolizaClon" />';
            var panDatComUrlCotiza           = '<s:url namespace="/"                action="cotizacionVital"             />';
            var datComUrlMCUpdateStatus      = '<s:url namespace="/mesacontrol"     action="actualizarStatusTramite"     />';
            var datComUrlMC                  = '<s:url namespace="/mesacontrol"     action="mcdinamica"                  />';
            var urlPantallaValosit           = '<s:url namespace="/"                action="pantallaValosit"             />';
            var urlPantallaAgentes           = '<s:url namespace="/flujocotizacion" action="principal"                   />';
            var compleUrlViewDoc             = '<s:url namespace ="/documentos"     action="descargaDocInline"           />';
            var compleUrlGuardarCartoRechazo = '<s:url namespace="/"                action="guardarCartaRechazo"         />';
            var compleUrlCotizacion          = '<s:url namespace="/emision"         action="cotizacion"                  />';
            var _urlEnviarCorreo             = '<s:url namespace="/general"         action="enviaCorreo"                 />';
            var _URL_CONSULTA_CLAUSU_DETALLE = '<s:url namespace="/catalogos"       action="consultaClausulaDetalle"     />';
            var _URL_CONSULTA_CLAUSU         = '<s:url namespace="/catalogos"       action="consultaClausulas"           />';
            var _URL_ObtieneValNumeroSerie   = '<s:url namespace="/emision" 		action="obtieneValNumeroSerie"       />';
            var urlEditarAsegurados = '${ctx}<s:property value="map1.urlAsegurados" />?now=${now}';
            var urlServidorReports  = '<s:property value="rutaServidorReports"         />';
            var complerepSrvUsr     = '<s:property value="passServidorReports"         />';
            var _URL_urlCargarTvalosit   = '<s:url namespace="/emision"    action="cargarValoresSituacion"               />';
            var urlPantallaBeneficiarios = '<s:url namespace="/catalogos"  action="includes/pantallaBeneficiarios"       />';
            
            var url_buscar_empleado                  = '<s:url namespace="/emision"    action="buscarEmpleados"                       />';
            var urlCargar                    = '<s:url namespace="/"                action="cargarDatosComplementarios"  />';
            var url_guarda_empleado         = '<s:url namespace="/emision"                action="guardaEmpleados"  />';
            var url_admin_ret                           = '<s:url namespace="/emision"                action="obtieneAdminRet"  />';

            if(!Ext.isEmpty(panDatComFlujo))
            {
                datComUrlMC = _GLOBAL_COMP_URL_MCFLUJO;
                debug('datComUrlMC:',datComUrlMC);
            }
            
            var _panDatCom_numPestaniasIniciales=4;
            var accordion;
            var datComPolizaMaestra;
            
            
            var _NOMBRE_REPORTE_CARATULA; //= '<s:text name="rdf.caratula.previa.nombre" />';
            if(panDatComMap1.SITUACION=='AUTO'){
                _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.auto.nombre" />';
            }else if(inputCdtipsit == 'SL' || inputCdtipsit == 'SN'){
            	_NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.nombre" />';
            }else if(inputCdtipsit == 'GMI'){
            	_NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.gmi.nombre" />';
            }else if(inputCdtipsit == 'MS'){
            	_NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.ms.nombre" />';
            }else if(inputCdtipsit == 'RI'){
            	_NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.ri.nombre" />';
            }else if(inputCdtipsit == 'GMPI'){
                _NOMBRE_REPORTE_CARATULA = '<s:text name="rdf.caratula.previa.gmpi.nombre" />';
            }
            
            if(Ext.isEmpty(_NOMBRE_REPORTE_CARATULA)){
            	debugError('Error, no se tiene nombre de caratula para vista previa con cdtipsit: ', inputCdtipsit);
            }
            
            var fechaMinEmi = Ext.Date.parse('<s:property value="map1.fechamin" />','d/m/Y');
            var fechaMaxEmi = Ext.Date.parse('<s:property value="map1.fechamax" />','d/m/Y');
            debug('fechaMinEmi:',fechaMinEmi);
            debug('fechaMaxEmi:',fechaMaxEmi);
            debug(sesionDsrol);
            
            var _paramsRetryWS;
            var _mensajeEmail;
            
            var _nombreContratante;
            var _numeroPolizaExt;
            
            var panDatComUpdateFerenova = function(field,value)
            {
                try
                {
                	if(!Ext.isEmpty(plazoEnDias) && '|16|5|6|'.lastIndexOf('|'+inputCdramo+'|')!=-1)
                		{
                		  Ext.getCmp('fechaRenovacion').setValue(Ext.Date.add(value, Ext.Date.DAY, plazoEnDias));
                		}
                	else
                		{
                		 Ext.getCmp('fechaRenovacion').setValue(Ext.Date.add(value, Ext.Date.YEAR, 1));
                		}
                }catch(e)
                {
                	debugError(e);
                }
            }
            
            var plazoEnDias;
            function recuperaPlazoDias()
            {
            	if(!Ext.isEmpty(Ext.getCmp('fechaRenovacion')) && !Ext.isEmpty(Ext.getCmp('fechaEfectividad')))
                {
	                var milisDif = Ext.Date.getElapsed(
	                Ext.getCmp('fechaRenovacion').getValue(),Ext.getCmp('fechaEfectividad').getValue()
	                );
	                debug('Valor itemVigencia:',milisDif);
	                plazoEnDias = (milisDif/1000/60/60/24).toFixed(0);
	                debug('plazo_dias:',plazoEnDias);
                }
            }
            
            var pantallaValositParche = false;
            var panDatComAux1         = 0;
            
            if(inputCdramo+'x'=='6x')
            {
                pantallaValositParche = function()
                {
                    debug('>parchando tvalosit');
                    
                    _fieldByName('panel2.feefec').removeListener('change',panDatComUpdateFerenova);
                    
                    _fieldByName('panel2.feefec').addListener('change',function()
                    {
                        if(Ext.isEmpty(_fieldByName('parametros.pv_otvalor20').getValue()))
                        {
                            mensajeWarning('Favor de capturar la vigencia');
                        }
                        else
                        {
                            _fieldByName('panel2.ferenova').setValue(
                                Ext.Date.add(_fieldByName('panel2.feefec').getValue(),Ext.Date.MONTH,_fieldByName('parametros.pv_otvalor20').getValue())
                            );
                        }
                    });
                    
                    _fieldByName('parametros.pv_otvalor20').addListener('select',function()
                    {
                        _fieldByName('panel2.ferenova').setValue(
                            Ext.Date.add(_fieldByName('panel2.feefec').getValue(),Ext.Date.MONTH,_fieldByName('parametros.pv_otvalor20').getValue())
                        );
                        var vigencia=_fieldByName('parametros.pv_otvalor20').getValue();
                        debug('vigencia:',vigencia);
                        if(vigencia-0==3||vigencia-0==6)
                        {
                            _fieldByName('panel2.cdperpag').setValue('12');
                            _fieldByName('panel2.cdperpag').setReadOnly(true);
                        }
                        else
                        {
                            _fieldByName('panel2.cdperpag').setReadOnly(false);
                        }
                    });
                    
                    debug('<parchando tvalosit');
                };
            }
            
            var mantener;
            function expande(indice)
            {
            	try
            	{
		            	var comp;
		            	if(indice==1 && !mantener)
		           		{
                            mantener= true;
		            		comp=Ext.getCmp('formPanel');
		           		}
		            	
		            	else if(indice==2 && !mantener)
		           		{
		           	        mantener= false;
		            	    comp=Ext.getCmp('tabPanelAsegurados');
		           		}
		            	
		            	else if(indice==2 && mantener)
                        {
		            		mantener= false;
		            		comp=Ext.getCmp('formPanel');
                        }
		            	
		            	debug('mantener: '+mantener+' e indice: '+ indice)
		           		window.parent.scrollTo(0,0);
		          		accordion.setActiveTab(comp);
            	}
            	catch(e)
                {
                    debugError(e, 'expande');
                }
          	}
            
var _p29_validaSeguro;
function checarBenef()
 {  
// 	alert('DDD');
	 if(inputCdramo == 16)
	 { 
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
	          var json=Ext.decode(response.responseText);
	          debug('### tvalosit:',json);
	          if(json.exito)
	          {
	                  var _p29_validaSeguro = json.smap1['parametros.pv_seguroVida'];
	                 
	                  if(_p29_validaSeguro == "S")
	                  {    
	                	  var panel = Ext.getCmp('panelBeneficiarioHere');
 	                	  panel.on({
 	                		  afterrender : function(me){
 	                			  me.getLoader().load();
 	                		  }
 	                	  })
	                	  panel.show();
                	  }
	          }
	          else
	          {
	              mensajeError(json.respuesta);
	          }
	      }
	  });
	 }
	 else
		 {
	 debug('No es ramo 16, es '+inputCdramo);
		 }
}


function _datComTurnarSuscripcion()
{
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Observaciones para el suscriptor'
        ,width       : 600
        ,height      : 430
        ,buttonAlign : 'center'
        ,modal       : true
        ,closable    : false
        ,autoScroll  : true
        ,items       :
        [
            {
                id        : 'inputTextareaCommentsToSusFromMC'
                ,width  : 570
                ,height : 300
                ,xtype  : 'textarea'
            }
            ,{
                xtype       : 'radiogroup'
                ,fieldLabel : 'Mostrar al agente'
                ,columns    : 2
                ,width      : 250
                ,style      : 'margin:5px;'
                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                ,items      :
                [
                    {
                        boxLabel    : 'Si'
                        ,itemId     : 'SWAGENTE'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'S'
                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                    }
                    ,{
                        boxLabel    : 'No'
                        ,name       : 'SWAGENTE'
                        ,inputValue : 'N'
                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                    }
                ]
            }
        ]
        ,buttons    :
        [
            {
                text     : 'Turnar'
                ,icon    : '${ctx}/resources/fam3icons/icons/user_go.png'
                ,handler : function()
                {
                    var form=Ext.getCmp('formPanel');
                    var window=this.up().up();
                    if(form.isValid())
                    {
                        window.setLoading(true);
                        form.submit(
                        {
                            params:
                            {
                                'map1.pv_cdunieco' :  inputCdunieco,
                                'map1.pv_cdramo' :    inputCdramo,
                                'map1.pv_estado' :    inputEstado,
                                'map1.pv_nmpoliza' :  inputNmpoliza
                            },
                            success:function()
                            {
                                Ext.Ajax.request(
                                {
                                    url     : datComUrlMCUpdateStatus
                                    ,params :
                                    {
                                        'smap1.ntramite'   : inputNtramite
                                        ,'smap1.status'    : '13'//en suscripcion
                                        ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToSusFromMC').getValue()
                                        ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
                                    }
                                    ,success : function(response)
                                    {
                                        var json=Ext.decode(response.responseText);
                                        if(json.success==true)
                                        {
                                            Ext.create('Ext.form.Panel').submit(
                                            {
                                                url             : datComUrlMC
                                                ,standardSubmit : true
                                                ,params         :
                                                {
                                                    'smap1.gridTitle':'Tareas',
                                                    'smap2.pv_cdtiptra_i':1,
                                                    'smap1.editable':1
                                                }
                                            });
                                        }
                                        else
                                        {
                                            window.setLoading(false);
                                            Ext.Msg.show(
                                            {
                                                title:'Error',
                                                msg: 'Error al turnar a suscripci&oacute;n',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.ERROR
                                            });
                                        }
                                    }
                                    ,failure : function()
                                    {
                                        window.setLoading(false);
                                        errorComunicacion();
                                    }
                                });
                            },
                            failure:function()
                            {
                                window.setLoading(false);
                                errorComunicacion();
                            }
                        });
                    }
                    else
                    {
                        datosIncompletos();
                    }
                }
            }
            ,{
                text  : 'Cancelar'
                ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
                ,handler : function()
                {
                    this.up().up().destroy();
                }
            }
        ]
    }).show());
}

function _p29_guardarComplementario(callback)
{
    var form=Ext.getCmp('formPanel');
    //console.log(form.getValues());
    
    //validacion de cdunieco=1403
    if (!Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO',null,true))){
    	debug('**encontrado en funcion? ', _fieldByLabel('NUMERO DE CONTRATO').value,_fieldByLabel('NUMERO DE CONTRATO',null,true));
     	
     	/* Cuando es un producto valido verifica si es la sucursal correcta y esta vacio,  
     	 * y modifica la obligatoriedad del campo segun la sucursal correcta.
     	 */
        //if (inputCdunieco == 1403 && Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO').value)){
        //if ((inputCdunieco == 1403) && (Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO').value))||(_fieldByLabel('NUMERO DE CONTRATO').value==0)){
        if (inputCdunieco == 1403 && _fieldByLabel('NUMERO DE CONTRATO').value==0){
    		_fieldByLabel('NUMERO DE CONTRATO',null,true).allowBlank = false;
         	_fieldByLabel('NUMERO DE CONTRATO',null,true).regex = /^[a-zA-Z]{3}[-]\d{3}$/;
         	_fieldByLabel('NUMERO DE CONTRATO',null,true).regexText = 'Debe cumplir con el formato /^[a-zA-Z]{3}[-]\d{3}$/';
         	debug('**Numero de contrato puede estar vacio? ',_fieldByLabel('NUMERO DE CONTRATO').allowBlank);
         	debug('**Valor en Numero de contrato',_fieldByLabel('NUMERO DE CONTRATO').value);
        }
    	else{
         	_fieldByLabel('NUMERO DE CONTRATO').allowBlank = true;
        }
    }
    
    if(form.isValid())//Modificación para advertir que no se han llegnado los campos
    {
        form.setLoading(true);
        //dxn
        if( inputCdtipsit!='MC' && inputCdtipsit!='AT' && inputCdtipsit!='GMPI'){
            
            guardaEmpleado();
        }
        form.submit({
            params:{
                'map1.pv_cdunieco' :  inputCdunieco,
                'map1.pv_cdramo' :    inputCdramo,
                'map1.pv_estado' :    inputEstado,
                'map1.pv_nmpoliza' :  inputNmpoliza
            },
            success:function(){
                form.setLoading(false);
                centrarVentanaInterna(Ext.Msg.show({
                    title    : 'Cambios guardados'
                    ,msg     : 'Sus cambios han sido guardados'
                    ,buttons : Ext.Msg.OK
                    ,fn      : function()
                    {
                    	 if(!Ext.isEmpty(callback))
                         {
                             callback();
                         }
                        /*
                        if(!Ext.isEmpty(panDatComFlujo))
                        {
                            var botones = Ext.ComponentQuery.query('[xtype=button][clase=botonFlujo]');
                            debug('botones:',botones);
                            for(var i in botones)
                            {
                                botones[i].show();
                            }
                        }
                        */
                    }
                }));
            },
            failure:function(){
                form.setLoading(false);
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
    }
    else
    {
        Ext.Msg.show({
            title:'Datos incompletos',
            msg: 'Favor de introducir todos los campos requeridos',
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.WARNING
        });
    }
}


function _p29_emitirClicComplementarios()
{
	try
	{
		var me = _fieldById('panDatComBotonRetarificar');
	    if(!Ext.isEmpty(me.mensajeInvalido))
	    {
	        mensajeWarning(me.mensajeInvalido);
	        expande(2);
	        return;
	    }
	    var form=Ext.getCmp('formPanel');
	    debug('contrato',_fieldByLabel('NUMERO DE CONTRATO',null,true));
	    if(form.isValid())
	    {
	        form.setLoading(true);
	        form.submit({
	            params:{
	                'map1.pv_cdunieco' :  inputCdunieco,
	                'map1.pv_cdramo' :    inputCdramo,
	                'map1.pv_estado' :    inputEstado,
	                'map1.pv_nmpoliza' :  inputNmpoliza
	            },
	            success:function(){
	                 //--
	                 Ext.Ajax.request(
	                                {
	                                    url     : urlRecotizar
	                                    ,params :
	                                    {
	                                        'panel1.nmpoliza'    : inputNmpoliza
	                                        ,cdunieco            : inputCdunieco
	                                        ,cdramo              : inputCdramo
	                                        ,cdtipsit            : inputCdtipsit
	                                        ,'panel1.notarifica' : ( Number(inputCdramo)==16 || Number(inputCdramo)==6 || Number(inputCdramo)==5 ) ? 'si' : ''
	                                    }
	                                    ,success : function(response)
	                                    {
	                                        form.setLoading(false);
	                                        var json=Ext.decode(response.responseText);
	                                        //console.log(json);
	                                        /**/
	                                        if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
	                                        {
	                                            centrarVentanaInterna(Ext.Msg.show({
	                                                title:'Verificar datos',
	                                                msg: json.mensajeRespuesta,
	                                                buttons: Ext.Msg.OK,
	                                                icon: Ext.Msg.WARNING
	                                            }));
	                                        }
	                                        else
	                                        {
	                                            var orden=0;
	                                            var parentescoAnterior='werty';
	                                            for(var i=0;i<json.slist1.length;i++)
	                                            {
	                                                if(json.slist1[i].parentesco!=parentescoAnterior)
	                                                {
	                                                    orden++;
	                                                    parentescoAnterior=json.slist1[i].parentesco;
	                                                }
	                                                json.slist1[i].orden_parentesco=orden+'_'+json.slist1[i].parentesco;
	                                            }
	                                            try
	                                            {
	                                            	if(!Ext.isEmpty(_fieldById('panDatComVentanaTarifaFinal',true)))
	                                            		{
	                                            		   _fieldById('panDatComVentanaTarifaFinal',true).destroy();
	                                            		}
	                                            }
	                                            catch(e)
	                                            {
	                                                debugError(e);
	                                            }
	                                            	                                            
	                                            Ext.create('Ext.window.Window',
	                                            {
	                                                title: 'Tarifa final',
	                                                itemId: 'panDatComVentanaTarifaFinal',
	                                                //maxHeight: 400,
	                                                autoScroll:true,
	                                                width: 660,
	                                                height: 400,
	                                                defaults: {
	                                                    width: 650
	                                                },
	                                                modal:false,
	                                                closable:false,
	                                                collapsible:true,
	                                                titleCollapse:true,
	                                                items:[  // Let's put an empty grid in just to illustrate fit layout
	                                                    Ext.create('Ext.grid.Panel',{
	                                                    width : 600
	                                                        ,store:Ext.create('Ext.data.Store',{
	                                                            model:'ModeloDetalleCotizacion',
	                                                            groupField: 'orden_parentesco',
	                                                            sorters: [{
	                                                                sorterFn: function(o1, o2){
	                                                                    if (o1.get('orden') === o2.get('orden'))
	                                                                    {
	                                                                        return 0;
	                                                                    }
	                                                                    return o1.get('orden') < o2.get('orden') ? -1 : 1;
	                                                                }
	                                                            }],
	                                                            proxy: {
	                                                                type: 'memory',
	                                                                reader: 'json'
	                                                            },
	                                                            data:json.slist1
	                                                        }),
	                                                        columns:
	                                                        [
	                                                            {
	                                                                header    : 'Nombre de la cobertura',
	                                                                dataIndex : 'Nombre_garantia',
	                                                                flex      : 2,
	                                                                summaryType: 'count',
	                                                                summaryRenderer: function(value){
	                                                                    return Ext.String.format('Total de {0} cobertura{1}', value, value !== 1 ? 's' : '');
	                                                                }
	                                                            },
	                                                            {
	                                                                header      : 'Importe por cobertura',
	                                                                dataIndex   : 'Importe',
	                                                                flex        : 1,
	                                                                renderer    : Ext.util.Format.usMoney,
	                                                                align       : 'right',
	                                                                summaryType : 'sum'
	                                                            }
	                                                        ],
	                                                        features: [{
	                                                            groupHeaderTpl:
	                                                                [
	                                                                    '{name:this.formatName}',
	                                                                    {
	                                                                        formatName:function(name)
	                                                                        {
	                                                                            return name.split("_")[1];
	                                                                        }
	                                                                    }
	                                                                ],
	                                                            ftype:'groupingsummary',
	                                                            startCollapsed :true
	                                                        }]
	                                                    })
	                                                    ,Ext.create('Ext.toolbar.Toolbar',{
	                                                        buttonAlign:'right'
	                                                        ,items:
	                                                        [
	                                                            '->'
	                                                            ,Ext.create('Ext.form.Label',
	                                                            {
	                                                                style:'color:white;'
	                                                                ,initComponent:function()
	                                                                {
	                                                                    var sum=0;
	                                                                    for(var i=0;i<json.slist1.length;i++)
	                                                                    {
	                                                                        sum+=parseFloat(json.slist1[i].Importe);
	                                                                    }
	                                                                    this.setText('Total: '+Ext.util.Format.usMoney(sum));
	                                                                    this.callParent();
	                                                                }
	                                                            })
	                                                        ]
	                                                    })
	                                                    ,Ext.create('Ext.form.Panel',
	                                                    {
	                                                        layout:
	                                                        {
	                                                            type    : 'table',
	                                                            columns : 5
	                                                        }
	                                                        ,defaults:
	                                                        {
	                                                            style : 'margin:5px;'
	                                                        }
	                                                        ,items:
	                                                        [
	                                                            {
	                                                                xtype : 'textfield'
	                                                                ,id   : 'numerofinalpoliza'
	                                                                ,fieldLabel : 'N&uacute;mero de poliza'
	                                                                ,readOnly   : true
	                                                            }
	                                                            ,{
	                                                                id      : 'botonEmitirPolizaFinal'
	                                                                ,xtype  : 'button'
	                                                                ,text   : 'Emitir'
	                                                                ,hidden : panDatComMap1.SITUACION !== 'AUTO' && ('SUSCRIPTOR' !== sesionDsrol)
	                                                                ,icon   : contexto+'/resources/fam3icons/icons/award_star_gold_3.png'
	                                                                //,disabled : true
	                                                                ,handler:function()
	                                                                {
	                                                                    var me=this;
	                                                                    me.up().up().setLoading(true);
	                                                                    Ext.Ajax.request(
	                                                                    {
	                                                                        url     : urlEmitir
	                                                                        ,params :
	                                                                        {
	                                                                            'panel1.pv_nmpoliza'  : inputNmpoliza
	                                                                            ,'panel1.pv_ntramite' : inputNtramite
	                                                                            ,'panel2.pv_cdramo'   : inputCdramo
	                                                                            ,'panel2.pv_cdunieco' : inputCdunieco
	                                                                            ,'panel2.pv_estado'   : inputEstado
	                                                                            ,'panel2.pv_nmpoliza' : inputNmpoliza
	                                                                            ,'panel2.pv_cdtipsit' : inputCdtipsit
	                                                                            ,'panel2.caseIdRstn'  : _NVL(panDatComMap1.caseIdRstn)
	                                                                        }
	                                                                        ,success:function(response)
	                                                                        {
	                                                                            me.up().up().setLoading(false);
	                                                                            var json=Ext.decode(response.responseText);
	                                                                            debug(json);
	                                                                            if(json.success==true)
		                                                            	    	{
		                                                            	    	    if ((!Ext.isEmpty(panDatComFlujo)) && 'RSTN' === panDatComFlujo.aux) {
		                                                            	    	        mensajeCorrecto(
		                                                            	    	            'P\u00f3liza emitida',
		                                                            	    	            'Se ha emitido la p\u00f3liza ' + json.panel2.nmpoliex,
		                                                            	    	            function () {
		                                                            	    	                _mask();
		                                                            	    	                location.href = _GLOBAL_CONTEXTO + '/jsp-script/general/callback.jsp?nmpoliex=' + json.panel2.nmpoliex;
		                                                            	    	            }
		                                                            	    	        );
		                                                            	    	        return;
		                                                            	    	    }
		                                                            	    	    
		                                                            	    		datComPolizaMaestra=json.panel2.nmpoliza;
		                                                            	    		debug("datComPolizaMaestra",datComPolizaMaestra);
		                                                            	    		
		                                                            	    		_numeroPolizaExt = json.panel2.nmpoliex;
		                                                            	    		if(json.retryRec){
		                                                            	    			
		                                                            	    			Ext.getCmp('botonEmitirPolizaFinal').hide();
			                                                            	    		Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
			                                                            	    		Ext.getCmp('botonImprimirPolizaFinal').setDisabled(false);
			                                                            	    		Ext.getCmp('botonPagar').setDisabled(false);
			                                                            	    		
			                                                            	    		Ext.Msg.show({
		                                                                                    title    :'Aviso'
		                                                                                    ,msg     : 'Error en la emisi&oacute;n. No se pudo emitir la p&oacute;liza.'
		                                                                                    ,buttons : Ext.Msg.OK
		                                                                                    ,icon    : Ext.Msg.WARNING
		                                                                                    ,fn      : function(){
		                                                                                        if(''+json.panel1.necesitaAutorizacion=='S')
		                                                                                        {
		                                                                                            Ext.create('Ext.form.Panel').submit(
		                                                                                            {
		                                                                                                url     : datComUrlMC
		                                                                                                ,params :
		                                                                                                {
		                                                                                                    'smap1.gridTitle'     : 'Tareas',
		                                                                                                    'smap2.pv_cdtiptra_i' : 1,
		                                                                                                    'smap1.editable'      : 1
		                                                                                                }
		                                                                                                ,standardSubmit : true
		                                                                                            });
		                                                                                        }
	                                                                                    		var paramsWS = {
								                                                                        'panel1.pv_nmpoliza'  : inputNmpoliza
								                                                                        ,'panel1.pv_ntramite' : inputNtramite
								                                                                        ,'panel2.pv_cdramo'   : inputCdramo
								                                                                        ,'panel2.pv_cdunieco' : inputCdunieco
								                                                                        ,'panel2.pv_estado'   : inputEstado
								                                                                        ,'panel2.pv_nmpoliza' : inputNmpoliza
								                                                                        ,'panel2.pv_cdtipsit' : inputCdtipsit
								                                                                        ,'nmpoliza'           : json.nmpoliza
								                                                                        ,'nmsuplem'           : json.nmsuplem
								                                                                        ,'cdIdeper'           : json.cdIdeper
								                                                                        ,'nmpolAlt'           : json.nmpolAlt
								                                    	                                ,'sucursalGS'         : json.sucursalGS
								                                    	                                ,'cdRamoGS'           : json.cdRamoGS
								                                    	                                ,'retryRec'           : json.retryRec
								                                                            		};
	                                                                                    		reintentarWSAuto(me.up().up(), paramsWS);
		                                                                                    }
		                                                                                });
			                                                            	    		return;
		                                                            	    		}
		                                                            	    		
		                                                            	    		Ext.getCmp('numerofinalpoliza').setValue(json.panel2.nmpoliex);
		                                                            	    		
		                                                            	    		Ext.getCmp('botonEmitirPolizaFinal').hide();
		                                                            	    		Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
		                                                            	    		Ext.getCmp('botonImprimirPolizaFinal').setDisabled(false);
		                                                            	    		Ext.getCmp('botonPagar').setDisabled(false);
		                                                            	    		
	    																			Ext.getCmp('botonReenvioWS').hide();
	                                                                                
	                                                                                if(panDatComMap1.SITUACION == 'AUTO'){
	                                                                                    _mensajeEmail = json.mensajeEmail;
	                                                                                    //debug("Mensaje Mail: " + _mensajeEmail);
	                                                                                    Ext.getCmp('botonEnvioEmail').enable();
	                                                                                }else {
	                                                                                    Ext.getCmp('botonEnvioEmail').hide();
	                                                                                }
	                                                                                
	                                                                                if(panDatComMap1.SITUACION=='AUTO' && Ext.isEmpty(panDatComFlujo))
	                                                                                {
	                                                                                    Ext.getCmp('venDocVenEmiBotIrCotiza').show();
	                                                                                }
	                                                                                Ext.getCmp('venDocVenEmiBotMesa').show();
	                                                                                Ext.getCmp('venDocVenEmiBotCancelar').setDisabled(true);
	                                                                                if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
	                                                                                {
	                                                                                    var ventanaTmp = Ext.Msg.show({
	                                                                                        title:'Aviso del sistema',
	                                                                                        msg: json.mensajeRespuesta,
	                                                                                        buttons: Ext.Msg.OK,
	                                                                                        icon: Ext.Msg.WARNING,
	                                                                                        fn: function(){
	                                                                                            if(!Ext.isEmpty(json.nmpolAlt)){
	                                                                                                mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
	                                                                                            }
	                                                                                        }
	                                                                                    });
	                                                                                    centrarVentanaInterna(ventanaTmp);
	                                                                                }else { 
	                                                                                    if(!Ext.isEmpty(json.nmpolAlt)){
	                                                                                        mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
	                                                                                    }
	                                                                                }
	                                                                            }
	                                                                            else
	                                                                            {
	                                                                                if(json.retryWS){
	                                                                                    datComPolizaMaestra=json.panel2.nmpoliza;
	                                                                                    debug("datComPolizaMaestra",datComPolizaMaestra);
	                                                                                    
	                                                                                    Ext.getCmp('botonEmitirPolizaFinal').hide();
	                                                                                    Ext.getCmp('botonEmitirPolizaFinalPreview').hide();
	                                                                                    
	                                                                                    if(panDatComMap1.SITUACION=='AUTO' && Ext.isEmpty(panDatComFlujo))
	                                                                                    {
	                                                                                        Ext.getCmp('venDocVenEmiBotIrCotiza').show();
	                                                                                    }
	                                                                                    Ext.getCmp('venDocVenEmiBotMesa').show();
	                                                                                    Ext.getCmp('venDocVenEmiBotCancelar').setDisabled(true);
	                                                                                }
	                                                                                Ext.Msg.show({
	                                                                                    title    :'Aviso'
	                                                                                    ,msg     : json.mensajeRespuesta
	                                                                                    ,buttons : Ext.Msg.OK
	                                                                                    ,icon    : Ext.Msg.WARNING
	                                                                                    ,fn      : function(){
	                                                                                        if(''+json.panel1.necesitaAutorizacion=='S')
	                                                                                        {
	                                                                                            Ext.create('Ext.form.Panel').submit(
	                                                                                            {
	                                                                                                url     : datComUrlMC
	                                                                                                ,params :
	                                                                                                {
	                                                                                                    'smap1.gridTitle'     : 'Tareas',
	                                                                                                    'smap2.pv_cdtiptra_i' : 1,
	                                                                                                    'smap1.editable'      : 1
	                                                                                                }
	                                                                                                ,standardSubmit : true
	                                                                                            });
	                                                                                        }
	                                                                                        if(json.retryWS){
	                                                                                            var paramsWS = {
	                                                                                                    'panel1.pv_nmpoliza'  : inputNmpoliza
	                                                                                                    ,'panel1.pv_ntramite' : inputNtramite
	                                                                                                    ,'panel2.pv_cdramo'   : inputCdramo
	                                                                                                    ,'panel2.pv_cdunieco' : inputCdunieco
	                                                                                                    ,'panel2.pv_estado'   : inputEstado
	                                                                                                    ,'panel2.pv_nmpoliza' : inputNmpoliza
	                                                                                                    ,'panel2.pv_cdtipsit' : inputCdtipsit
	                                                                                                    ,'nmpoliza'           : json.nmpoliza
	                                                                                                    ,'nmsuplem'           : json.nmsuplem
	                                                                                                    ,'cdIdeper'           : json.cdIdeper
	                                                                                                };
	                                                                                            reintentarWSAuto(me.up().up(), paramsWS);
	                                                                                        }
	                                                                                    }
	                                                                                });
	                                                                            }
	                                                                        }
	                                                                        ,failure:function()
	                                                                        {
	                                                                            me.up().up().setLoading(false);
	                                                                            Ext.Msg.show({
	                                                                                title:'Error',
	                                                                                msg: 'Error de comunicaci&oacute;n',
	                                                                                buttons: Ext.Msg.OK,
	                                                                                icon: Ext.Msg.ERROR
	                                                                            });
	                                                                        }
	                                                                    });
	                                                                }
	                                                            }
	                                                            ,{
	                                                                id     : 'botonEnvioEmail'
	                                                                ,xtype : 'button'
	                                                                ,text  : 'Enviar Email'
	                                                                ,icon  : contexto+'/resources/fam3icons/icons/email.png'
	                                                                ,disabled: true
	                                                                ,hidden: (panDatComMap1.SITUACION != 'AUTO') ? true: false
	                                                                ,handler:function()
	                                                                {
	                                                                    Ext.Msg.prompt('Envio de Email', 'Escriba los correos que recibir&aacute;n la documentaci&oacute;n (separados por ;)', 
	                                                                    function(buttonId, text){
	                                                                        if(buttonId == "ok" && !Ext.isEmpty(text)){
	                                                                            
	                                                                            if(Ext.isEmpty(_mensajeEmail)){
	                                                                                mensajeError('Mensaje de Email sin contenido. Consulte a Soporte T&eacute;cnico');
	                                                                                return;
	                                                                            }
	                                                                            
	                                                                            Ext.Ajax.request(
	                                                                                    {
	                                                                                        url : _urlEnviarCorreo,
	                                                                                        params :
	                                                                                        {
	                                                                                            to     : text,
	                                                                                            asunto : 'Documentación de póliza de Autos',
	                                                                                            mensaje: _mensajeEmail,
	                                                                                            html   : true
	                                                                                        },
	                                                                                        callback : function(options,success,response)
	                                                                                        {
	                                                                                            if (success)
	                                                                                            {
	                                                                                                var json = Ext.decode(response.responseText);
	                                                                                                if (json.success == true)
	                                                                                                {
	                                                                                                    Ext.Msg.show(
	                                                                                                    {
	                                                                                                        title    : 'Correo enviado'
	                                                                                                        ,msg     : 'El correo ha sido enviado'
	                                                                                                        ,buttons : Ext.Msg.OK
	                                                                                                        ,fn      : function()
	                                                                                                        {
	                                                                                                            _generarRemesaClic(
	                                                                                                                false
	                                                                                                                ,inputCdunieco
	                                                                                                                ,inputCdramo
	                                                                                                                ,'M'
	                                                                                                                ,datComPolizaMaestra
	                                                                                                                ,function(){}
	                                                                                                                ,'S'
	                                                                                                                );
	                                                                                                        }
	                                                                                                    });
	                                                                                                }
	                                                                                                else
	                                                                                                {
	                                                                                                    mensajeError('Error al enviar el correo');
	                                                                                                }
	                                                                                            }
	                                                                                            else
	                                                                                            {
	                                                                                                errorComunicacion();
	                                                                                            }
	                                                                                        }
	                                                                                    });
	                                                                        
	                                                                        }else {
	                                                                            mensajeWarning('Introduzca al menos una direcci&oacute;n de email');    
	                                                                        }
	                                                                    })
	                                                                }
	                                                            }
	                                                            ,{
	                                                                id     : 'botonReenvioWS'
	                                                                ,xtype : 'button'
	                                                                ,text  : 'Reintentar Emisi&oacute;n'
	                                                                ,icon  : contexto+'/resources/fam3icons/icons/award_star_gold_3.png'
	                                                                ,disabled: true
	                                                                ,hidden: (panDatComMap1.SITUACION != 'AUTO') ? true: false
	                                                                ,handler:function()
	                                                                {
	                                                                    var me=this;
	                                                                    reintentarWSAuto(me.up().up(), _paramsRetryWS);
	                                                                }
	                                                            }
	                                                            ,{
	                                                                xtype    : 'button'
	                                                                ,id      : 'botonEmitirPolizaFinalPreview'
	                                                                ,text    : 'Vista previa'
	                                                                ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
	                                                                ,hidden  : inputCdramo=='6'
	                                                                ,handler : function()
	                                                                {
	                                                                    var me=this;
	                                                                    var urlRequestImpCotiza=urlServidorReports
	                                                                    +'?destype=cache'
	                                                                    +"&desformat=PDF"
	                                                                    +"&report="+_NOMBRE_REPORTE_CARATULA
	                                                                    +"&paramform=no"
	                                                                    +"&userid="+complerepSrvUsr
	                                                                    +"&ACCESSIBLE=YES" //parametro que habilita salida en PDF
	                                                                    +'&p_unieco='+inputCdunieco
	                                                                    +'&p_estado=W'
	                                                                    +'&p_ramo='+inputCdramo
	                                                                    +'&p_poliza='+inputNmpoliza
	                                                                    debug(urlRequestImpCotiza);
	                                                                    var numRand=Math.floor((Math.random()*100000)+1);
	                                                                    debug(numRand);
	                                                                    var windowVerDocu=Ext.create('Ext.window.Window',
	                                                                    {
	                                                                        title          : 'Vista previa'
	                                                                        ,width         : 700
	                                                                        ,height        : 500
	                                                                        ,collapsible   : true
	                                                                        ,titleCollapse : true
	                                                                        ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
	                                                                                         +'src="'+compleUrlViewDoc+"?contentType=application/pdf&url="+encodeURIComponent(urlRequestImpCotiza)+"\">"
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
	                                                            ,{
	                                                                xtype     : 'button'
	                                                                ,id       : 'botonImprimirPolizaFinal'
	                                                                ,text     : 'Imprimir'
	                                                                ,icon     : contexto+'/resources/fam3icons/icons/printer.png'
	                                                                ,disabled : true
	                                                                ,handler  : function(me)
	                                                                {
	                                                                    var callbackRemesa = function()
	                                                                    {
	                                                                    	try
	                                                                    	{
		                                                                        _fieldById('IdvenDocuTramite', true).destroy();
	                                                                    	}
	                                                                    	catch(e){
	                                                                            debugError(e, 'venDocuTramite');
	                                                                        }
	                                                                    	
	                                                                        Ext.create('Ext.window.Window',
	                                                                        {
	                                                                            title        : 'Documentos del tr&aacute;mite '+inputNtramite
	                                                                            ,modal       : true
	                                                                            ,buttonAlign : 'center'
	                                                                            ,width       : 600
	                                                                            ,height      : 400
	                                                                            ,autoScroll  : true
	                                                                            ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
	                                                                            ,loader      :
	                                                                            {
	                                                                                url       : panDatComUrlDoc2
	                                                                                ,params   :
	                                                                                {
	                                                                                    'smap1.nmpoliza'  : datComPolizaMaestra
	                                                                                    ,'smap1.cdunieco' : inputCdunieco
	                                                                                    ,'smap1.cdramo'   : inputCdramo
	                                                                                    ,'smap1.estado'   : 'M'
	                                                                                    ,'smap1.nmsuplem' : '0'
	                                                                                    ,'smap1.ntramite' : inputNtramite
	                                                                                    ,'smap1.nmsolici' : inputNmpoliza
	                                                                                    ,'smap1.tipomov'  : '0'
	                                                                                }
	                                                                                ,scripts  : true
	                                                                                ,autoLoad : true
	                                                                            }
	                                                                        }).show();
	                                                                    };
	                                                                    _generarRemesaClic(
	                                                                        false
	                                                                        ,inputCdunieco
	                                                                        ,inputCdramo
	                                                                        ,'M'
	                                                                        ,datComPolizaMaestra
	                                                                        ,callbackRemesa
	                                                                        );
	                                                                }
	                                                            }
	                                                            ,{
	                                                                xtype     : 'button'
	                                                                ,id       : 'botonPagar'
	                                                                ,text     : 'Pagar'
	                                                                ,icon     : contexto+'/resources/fam3icons/icons/money.png'
	                                                                ,disabled : true
	                                                                ,hidden  : true//TODO: Reemplazar obtDatLoaderContratante por obtieneDatosClienteContratante //inputCdramo!='2'
	                                                                ,handler  : function()
	                                                                {
	                                                                    if(Ext.isEmpty(_numeroPolizaExt)){
	                                                                        mensajeWarning('Error al cargar el n&uacute;mero de p&oacute;liza.');
	                                                                        return;
	                                                                    }
	                                                                        
	                                                                    var nombreCompCont = '';
	                                                                    try{
	                                                                        var datosCont = obtDatLoaderContratante();
	                                                                        debug(datosCont);
	                                                                        nombreCompCont = datosCont.nombre +" "+ datosCont.snombre +" "+ datosCont.appat +" "+ datosCont.apmat;
	                                                                    }
	                                                                    catch(e){
	                                                                        debug('Sin datos de contratante.');
	                                                                        nombreCompCont = '';
	                                                                    }
	                                                                    
	                                                                    var IDventana = window.open("http://www.biosnettcs.com/", "IDventana", "width=610,height=725,top=0,left=190");
	                                                                        var parametrosPago = {
	                                                                                'etiqueta': 'USERADM1',
	                                                                                'etiquetaSys': 'lorant',
	                                                                                'mensajeExito': '16168',
	                                                                                'asegPago' : '906',//qualitas
	                                                                                'contratante' : nombreCompCont,//qualitas
	                                                                                'polext' : _numeroPolizaExt
	                                                                        };
	                                                                        
	                                                                        creaWindowPay("http://www.ibseguros.com/securepayment/Validate.action", parametrosPago, "IDventana");
	                                                                }
	                                                            }
	                                                            ,{
	                                                                xtype    : 'button'
	                                                                ,id      : 'venDocVenEmiBotMesa'
	                                                                ,text    : 'Regresar a mesa de control'
	                                                                ,icon    : '${ctx}/resources/fam3icons/icons/house.png'
	                                                                //,hidden  : panDatComMap1.SITUACION=='AUTO'
	                                                                ,handler : function()
	                                                                {
	                                                                    var me=this;
	                                                                    Ext.create('Ext.form.Panel').submit(
	                                                                    {
	                                                                        standardSubmit : true
	                                                                        ,url           : datComUrlMC
	                                                                        ,params        :
	                                                                        {
	                                                                            'smap1.gridTitle':'Tareas',
	                                                                            'smap2.pv_cdtiptra_i':1,
	                                                                            'smap1.editable':1
	                                                                        }
	                                                                    });
	                                                                }
	                                                            }
	                                                            ,{
	                                                                xtype    : 'button'
	                                                                ,id      : 'venDocVenEmiBotIrCotiza'
	                                                                ,text    : 'Nueva cotizaci&oacute;n'
	                                                                ,icon    : '${ctx}/resources/fam3icons/icons/book_open.png'
	                                                                ,hidden  : panDatComMap1.SITUACION!='AUTO'
	                                                                ,handler : function()
	                                                                {
	                                                                    var me=this;
	                                                                    Ext.create('Ext.form.Panel').submit(
	                                                                    {
	                                                                        standardSubmit : true
	                                                                        ,url           : compleUrlCotizacion
	                                                                        ,params        :
	                                                                        {
	                                                                            'smap1.cdramo'    : inputCdramo
	                                                                            ,'smap1.cdtipsit' : inputCdtipsit
	                                                                        }
	                                                                    });
	                                                                }
	                                                            }
	                                                            ,{
	                                                                xtype    : 'button'
	                                                                ,id      : 'venDocVenEmiBotCancelar'
	                                                                ,text    : 'Cancelar'
	                                                                ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
	                                                                ,handler : function()
	                                                                {
	                                                                    var me=this;
	                                                                    me.up().up().destroy();
	                                                                }
	                                                            }
	                                                        ],
	                                                        listeners : {
	                                                            afterrender : function(me){
	                                                                try{
	                                                                    debug('mensaje agente activo',json.panel1['mensajeAgenteActivo']);
	                                                                    if(!Ext.isEmpty(json.panel1['mensajeAgenteActivo'])){
	                                                                        setTimeout(function(){ 
	                                                                            mensajeWarning(json.panel1['mensajeAgenteActivo']); 
	                                                                        }, 3000);
	                                                                    }
	                                                                }
	                                                                catch(e){
	                                                                    debugError(e);
	                                                                }
	                                                            }
	                                                        }
	                                                    })
	                                                ]
	                                            }).showAt(50,50);
	                                            Ext.getCmp('venDocVenEmiBotMesa').hide();
	                                            Ext.getCmp('venDocVenEmiBotIrCotiza').hide();
	                                            
	                                            if(!Ext.isEmpty(json.respuestaOculta)){
	                                                mensajeWarning(json.respuestaOculta);
	                                            }
	                                            
	                                            /**/
	                                        }
	                                    }
	                                    ,failure : function()
	                                    {
	                                        form.setLoading(false);
	                                        Ext.Msg.show({
	                                            title:'Error',
	                                            msg: 'Error de comunicaci&oacute;n',
	                                            buttons: Ext.Msg.OK,
	                                            icon: Ext.Msg.ERROR
	                                        });
	                                    }
	                                });
	                                //--
	                
	            },
	            failure:function(){
	                form.setLoading(false);
	                Ext.Msg.show({
	                    title:'Error',
	                    msg: 'Error de comunicaci&oacute;n',
	                    buttons: Ext.Msg.OK,
	                    icon: Ext.Msg.ERROR
	                });
	            }
	            
	        });
	    }
	    else
	    {
	        datosIncompletos();
	    }
	}
	catch(e)
    {
        debugError(e,'_p29_emitirClicComplementarios');
    }
}

            Ext.onReady(function(){
            	
            	// Se aumenta el timeout para todas las peticiones:
				Ext.Ajax.timeout = 485000; // 8 min
				Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
				Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
				Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
                
                Ext.define('MiModeloDinamico',{
                    extend:'Ext.data.Model',
                    <s:property value="fields" />
                });
                
                Ext.define('ModeloDetalleCotizacion',{
                    extend:'Ext.data.Model',
                    fields:
                    [
                        {name : 'Codigo_Garantia'},
                        {name : 'Importe',type : 'float'},
                        {name : 'Nombre_garantia'},
                        {name : 'cdtipcon'},
                        {name : 'nmsituac'},
                        {name : 'orden'},
                        {name : 'parentesco'},
                        {name : 'orden_parentesco'}
                    ]
                });
                
                accordion=Ext.create('Ext.tab.Panel',
                {
                	title:'Tr&aacute;mite '+inputNtramite,
                	border:0
                	//renderTo : 'maindiv'
                	/*,layout   :
               		{
                		type           : 'accordion'
                		,animate       : true
                		,titleCollapse : true
               		}*/
                    ,items:
                    [
                    /**/Ext.create('Ext.panel.Panel',
                        {
                            id:'tabPanelValosit'
                            ,title:'Datos de cotizaci&oacute;n'
                            ,cls:'claseTitulo'
                            ,border:0
                            ,loader:
                            {
                                url       : urlPantallaValosit
                                ,params   :
                                {
                                    'smap1.cdunieco'  : inputCdunieco
                                    ,'smap1.cdramo'   : inputCdramo
                                    ,'smap1.estado'   : inputEstado
                                    ,'smap1.nmpoliza' : inputNmpoliza
                                    ,'smap1.cdtipsit' : inputCdtipsit
                                    ,'smap1.agrupado' : 'si'
                                }
                                ,scripts  : true
                                ,autoLoad : true
                            }
                            ,listeners:
                            {
                                /*expand:function( p, eOpts )
                                {
                                    window.parent.scrollTo(0,150+p.y);
                                }*/
                            	activate: function(){
                        			try{
                        		        if(inputCdramo==Ramo.ServicioPublico){
                        		        	Ext.ComponentQuery
                        		        	.query("[fieldLabel*='(FRONTERIZO)'],[fieldLabel*='TIPO DE CAMBIO AL D'],[fieldLabel*='PAQUETE'],[fieldLabel*='NOMBRE CLIENTE'],[fieldLabel*='TIPO PERSONA'],[fieldLabel*='FECHA DE NACIMIENTO DEL CONTRATANTE']")
                        		        	.forEach(function(it){ 
                        		    	    		it.allowBlank=true; 
                        		    	    		it.hide();
                        		        		}
                        		        	);
                        		    	}
                        		    }catch(e){
                        		    	debugError(e);
                        		    }
                        		},
                                afterrender:function(tab)
                                {
                                	debug('afterrender tabPanelValosit');
                                    tab.loader.load();
                                }
                            }
                        })
                        ,Ext.create('Ext.panel.Panel',
                        {
                            id:'tabPanelAsegurados'
                            ,title:inputCdtipsit=='AF'||inputCdtipsit=='PU'?'Editar clientes':'Editar contratante'
                            ,cls:'claseTitulo'
                            ,border:0
                            ,loader:
                            {
                                url     : urlEditarAsegurados
                                ,params :
                                {
                                    'map1.cdunieco'  : inputCdunieco
                                    ,'map1.cdramo'   : inputCdramo
                                    ,'map1.cdtipsit' : inputCdtipsit
                                    ,'map1.estado'   : inputEstado
                                    ,'map1.nmpoliza' : inputNmpoliza
                                    ,'map1.cdpercli' : panDatComMap1.cdpercli
                                    ,'map1.ntramite' : inputNtramite
                                }
                                ,scripts:true
                                ,autoLoad:true
                            }
                            ,listeners:
                            {
                                /*expand:function( p, eOpts )
                                {
                                    window.parent.scrollTo(0,150+p.y);
                                }*/
                                afterrender:function(tab)
                                {
                                	debug('afterrender tabPanelAsegurados');
                                	tab.loader.load();
                                }
                            }
                        })
                        ,Ext.create('Ext.form.Panel',{
                        	title : 'Editar datos complementarios / emitir',
                        	cls:'claseTitulo',
		                    id:'formPanel',//id1
		                    itemId : 'formPanel',
		                    //renderTo:'maindiv',
		                    url:urlGuardar,
		                    buttonAlign:'center',
		                    border:0,
		                    //bodyPadding:5,
		                    items:[
		                        Ext.create('Ext.panel.Panel',{
		                            id:'panelDatosGeneralesPoliza',//id2
		                            title:'Datos generales de la poliza',
		                            style:'margin:5px',
		                            frame:false,
		                            collapsible:true,
		                            titleCollapse:true,
		                            layout: {
		                                type: 'table',
		                                columns: 3
		                            },
		                            items:[
		                                Ext.create('Ext.form.TextField', {
		                                    id:'companiaAseguradora',//id3
		                                    name:'panel1.dsciaaseg',
		                                    fieldLabel: 'Sucursal',
		                                    /*store:Ext.create('Ext.data.Store', {
		                                        model: 'Generic',
		                                        data : [{key:'20',value:'General de seguros'}]
		                                    }),
		                                    queryMode: 'local',
		                                    displayField: 'value',
		                                    valueField: 'key',*/
		                                    readOnly:true,
		                                    style:'margin:5px;'
		                                }),
		                                Ext.create('Ext.form.TextField', {
		                                    id:'agenteVentas',
		                                    name:'panel1.nombreagente',
		                                    fieldLabel: 'Agente',
		                                    /*store:Ext.create('Ext.data.Store', {
		                                        model: 'Generic',
		                                        data : [{key:'20',value:'General de seguros'}]
		                                    }),
		                                    queryMode: 'local',
		                                    displayField: 'value',
		                                    valueField: 'key',
		                                    //value:Ext.create('Generic',{key:'20',value:'General de seguros'}),*/
		                                    readOnly:true,
		                                    style:'margin:5px;'
		                                }),
		                                Ext.create('Ext.form.TextField', {
		                                    id:'producto',//id4
		                                    name:'panel1.dsramo',
		                                    fieldLabel: 'Producto',
		                                    /*store:Ext.create('Ext.data.Store', {
		                                        model: 'Generic',
		                                        data : [{key:'2',value:'Salud Vital'}]
		                                    }),
		                                    queryMode: 'local',
		                                    displayField: 'value',
		                                    valueField: 'key',
		                                    //value:Ext.create('Generic',{key:'2',value:'Salud vital'}),*/
		                                    readOnly:true,
		                                    style:'margin:5px;'
		                                })
		                            ]
		                        }),
		                        Ext.create('Ext.panel.Panel',{
		                            id:'panelDatosGenerales',//id5
		                            title:'Datos generales',
		                            style:'margin:5px',
		                            frame: false,
		                            collapsible:true,
		                            titleCollapse:true,
		                            layout:{
		                                type: 'table',
		                                columns: 3
		                            },
		                            items:[
		                                {
		                                    xtype:'textfield',
		                                    id:'poliza',//id6
		                                    name:'panel2.nmpoliza',
		                                    readOnly:true,
		                                    fieldLabel:'Poliza',
		                                    style:'margin:5px;',
		                                    hidden:true
		                                },
		                                {                        //<<maquillado
		                                	xtype:'textfield',   //<<maquillado  
                                            readOnly:true,       //<<maquillado
                                            fieldLabel:'Poliza', //<<maquillado
                                            value:'0',           //<<maquillado
                                            style:'margin:5px;' //<<maquillado
		                                },                       //<<maquillado
		                                {
		                                    xtype:'combo',
		                                    id:'estadoPoliza',//id8
		                                    name:'panel2.estado',
		                                    fieldLabel:'Estado',
		                                    displayField: 'value',
		                                    valueField: 'key',
		                                    readOnly:true,
		                                    store:Ext.create('Ext.data.Store', {
		                                        model:'Generic',
		                                        autoLoad:true,
		                                        proxy:
		                                        {
		                                            type: 'ajax',
		                                            url:urlCargarCatalogos,
		                                            extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@STATUS_POLIZA"/>'},
		                                            reader:
		                                            {
		                                                type: 'json',
		                                                root: 'lista'
		                                            }
		                                        }
		                                    }),
		                                    editable:false,
		                                    queryMode:'local',
		                                    style:'margin:5px;',
		                                    allowBlank:false
		                                },
		                                {
		                                    xtype:'numberfield',
		                                    id:'solicitud',//id10
		                                    name:'panel2.solici',
		                                    fieldLabel:'Cotizaci&oacute;n',
		                                    style:'margin:5px;',
		                                    allowBlank:false,
		                                    readOnly:true
		                                },
		                                {
		                                    xtype:'datefield',
		                                    id:'fechaSolicitud',//id9
		                                    name:'panel2.fesolici',
		                                    fieldLabel:'Fecha de solicitud',
		                                    allowBlank:false,
		                                    style:'margin:5px;',
		                                    format:'d/m/Y',
		                                    //minValue:fechaMinEmi,
		                                    //maxValue:fechaMaxEmi,
		                                    readOnly:panDatComMap1.SITUACION=='AUTO'
		                                },
		                                {
		                                    xtype:'datefield',
		                                    id:'fechaEfectividad',//id11
		                                    name:'panel2.feefec',
		                                    fieldLabel:'Fecha de inicio de vigencia',
		                                    allowBlank:false,
		                                    style:'margin:5px;',
		                                    format:'d/m/Y',
		                                    listeners:{
		                                        change:panDatComUpdateFerenova
		                                    },
		                                    minValue:fechaMinEmi,
                                            maxValue:fechaMaxEmi
		                                },
		                                {
		                                    xtype:'datefield',
		                                    id:'fechaRenovacion',//id14
		                                    name:'panel2.ferenova',
		                                    fieldLabel:'Fecha de t&eacute;rmino de vigencia',
		                                    allowBlank:false,
		                                    style:'margin:5px;',
		                                    format:'d/m/Y',
		                                    readOnly:true
		                                },
		                                {
		                                    xtype:'combo',
		                                    id:'tipoPoliza',//id12
		                                    name:'panel2.cdtipopol',
		                                    fieldLabel:'Tipo de poliza',
		                                    displayField: 'value',
		                                    valueField: 'key',
		                                    store:Ext.create('Ext.data.Store', {
		                                        model:'Generic',
		                                        autoLoad:true,
		                                        proxy:
		                                        {
		                                            type: 'ajax',
		                                            url:urlCargarCatalogos,
		                                            <s:if test='%{getMap1().get("SITUACION").equals("AUTO")}'>
		                                            extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_POLIZA_AUTO"/>'},
		                                            </s:if>
		                                            <s:else>
		                                            extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_POLIZA"/>'},
		                                            </s:else>
		                                            reader:
		                                            {
		                                                type: 'json',
		                                                root: 'lista'
		                                            }
		                                        }
		                                    }),
		                                    editable:false,
		                                    queryMode:'local',
		                                    style:'margin:5px;',
		                                    allowBlank:false
		                                },
		                                {
		                                    xtype:'combo',
		                                    id:'formaPagoPoliza',//id15
		                                    name:'panel2.cdperpag',
		                                    fieldLabel:'Forma de pago',
		                                    displayField: 'value',
		                                    valueField: 'key',
		                                    store:Ext.create('Ext.data.Store', {
		                                        model:'Generic',
		                                        autoLoad:true,
		                                        proxy:
		                                        {
		                                            type: 'ajax',
		                                            url:urlCargarCatalogos,
		                                            extraParams :
		                                            {
		                                                catalogo           : 'FORMAS_PAGO_POLIZA_POR_RAMO_TIPSIT'
		                                                ,'params.cdramo'   : inputCdramo
		                                                ,'params.cdtipsit' : inputCdtipsit
		                                            },
		                                            reader:
		                                            {
		                                                type: 'json',
		                                                root: 'lista'
		                                            }
		                                        }
		                                    }),
		                                    editable:false,
		                                    queryMode:'local',
		                                    style:'margin:5px;',
		                                    allowBlank:false,
		                                    readOnly : Number(inputCdramo)==16
		                                },
		                                {
                                            xtype:'textfield',
                                            name:'panel2.dsplan',
                                            readOnly:true,
                                            fieldLabel:'Plan',
                                            style:'margin:5px;'
                                        },
		                                {
                                            xtype:'numberfield',
                                            name:'panel2.nmrenova',
                                            allowBlank:false,
                                            maxValue: 99,
        									minValue: 0,
                                            //readOnly:true,
                                            value:0,
                                            fieldLabel:'N&uacute;mero Renovaci&Oacute;n',
                                            style:'margin:5px;'
                                        },
		                                {
                                            xtype:'textfield',
                                            name:'panel2.nmpolant',
                                           // readOnly:true,
                                            fieldLabel:'P&oacute;liza Anterior',
                                            style:'margin:5px;'
                                        }
                                        ,{
                                            xtype           : 'combo'
                                            ,itemId         : '_panDatCom_nmcuadroCmp'
                                            ,fieldLabel     : 'Cuadro de comisiones'
                                            ,name           : 'panel2.nmcuadro'
                                            ,style          : 'margin:5px;'
                                            ,forceSelection : true
                                            ,valueField     : 'key'
                                            ,displayField   : 'value'       
                                            ,editable       : true  
                                            ,queryMode      : 'local'
                                            ,disabled       : panDatComMap1.cambioCuadro!='S'
                                            ,hidden         : panDatComMap1.cambioCuadro!='S'
                                            ,allowBlank     : false
                                            ,store          :
                                            Ext.create('Ext.data.Store',
                                            {
                                                model     : 'Generic'
                                                ,autoLoad : true
                                                ,proxy    :
                                                {
                                                   type         : 'ajax'
                                                   ,url         : urlCargarCatalogos
                                                   ,extraParams :
                                                   {
                                                       catalogo           : 'CUADROS_POR_SITUACION'
                                                       ,'params.cdtipsit' : inputCdtipsit
                                                   }
                                                   ,reader      :
                                                   {
                                                       type  : 'json'
                                                       ,root : 'lista'
                                                   }
                                                }
                                            })
                                        }
		                            ]
		                        })
		                        ,Ext.create('Ext.panel.Panel',{
		                            id:'panelDatosAdicionales',//id16
		                            name:'panelDatosAdicionales',
		                            title:'Datos adicionales',
		                            style:'margin:5px',
		                            collapsible:true,
		                            titleCollapse:true,
		                            autoScroll:true,
		                            
		                            maxHeight:300,
		                            layout:{
		                                type: 'table',
		                                columns: 2
		                            },
		                            <s:property value="items" />
		                            ,listeners:{
		                                beforerender:function(me,op){
		                                    if(Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="ADMINISTRADORA"]').length>0){ 
                                                
		                                        Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="ADMINISTRADORA"]')[0].getStore().filter(
                                                        [
                                                         {filterFn: function(item) {
                                                               
                                                                        return item.get("key") >= 1000 || item.get("key")==-1; 
                                                                     }
                                                         }
                                                        ]);
                                             }
		                                }
		                                ,afterrender:function(me,op){
		                                    
		                                    
		                                        try{
		                                          
		                                            if(inputCdtipsit=='MC' || inputCdtipsit=='AT' || inputCdtipsit=='GMPI'){
                                                       
                                                        return;
                                                    }
		                                                
		                                               try{
			                                                _fieldByLabel('CLAVE DESCUENTO').on({
	                                                            boxready:function(){
	                                                                claveDescuentoDxn(_fieldByLabel('ADMINISTRADORA').getValue()
	                                                                        ,_fieldByLabel('ADMINISTRADORA').getValue()
	                                                                        ,inputCdramo
	                                                                        ,inputCdtipsit);
	                                                            }
	                                                        });
		                                               }catch(e){
		                                                   debug("Error: no se estan filtrando las claves de descuento");
		                                                   debugError(e);
		                                               }
		                                                
		                                                if(panDatComMap1.SITUACION == 'AUTO' ){
			                                                _fieldByLabel('RETENEDORA').fireEvent('blur',_fieldByLabel('RETENEDORA'));
			                                                
			                                                if(!(_fieldByLabel('ADMINISTRADORA').getValue()!=null &&
			                                                        (_fieldByLabel('ADMINISTRADORA').getValue()+'').trim()!='' &&
			                                                        _fieldByLabel('ADMINISTRADORA').getValue()!="-1")){
			                                                    
			                                                    me.hide();
			                                                    return;
			                                                }
			                                                
			                                                if(inputCdtipsit=='MC' || inputCdtipsit=='AT'){
			                                                   
                                                                return;
			                                                }
			                                                
			                                                try{
			                                                   me.setTitle('Datos adicionales descuento por n&oacute;mina');
			                                                }catch(e){
			                                                    debugError(e);
			                                                }
			                                                Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor08"]')[0].setReadOnly(true);
			                                                Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor09"]')[0].setReadOnly(true);
			                                               
			                                                if(Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor03"]').length>0){
			                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor03"]')[0].hide();
			                                                }
			                                                _fieldByLabel('CLAVE DESCUENTO').allowBlank=false;
		                                                    _fieldByLabel('CLAVE DESCUENTO').clearInvalid();
		                                                    _fieldByLabel('CLAVE DESCUENTO').store.load();
		                                                
		                                                    _fieldByLabel('CLAVE EMPLEADO').allowBlank=false;
		                                                    _fieldByLabel('CLAVE EMPLEADO').clearInvalid();
		                                                     _fieldByLabel('NOMBRE EMPLEADO').allowBlank=false;
		                                                    _fieldByLabel('NOMBRE EMPLEADO').clearInvalid();
		                                                    _fieldByLabel('APELLIDO PATERNO').allowBlank=false;
		                                                    _fieldByLabel('APELLIDO PATERNO').clearInvalid();
		                                                    _fieldByLabel('APELLIDO MATERNO').allowBlank=false;
		                                                    _fieldByLabel('APELLIDO MATERNO').clearInvalid();
		                                                    _fieldByLabel('RFC EMPLEADO').allowBlank=false;
		                                                    _fieldByLabel('RFC EMPLEADO').clearInvalid();
		                                                    _fieldByLabel('CURP').allowBlank=false;
			                                                _fieldByLabel('CURP').clearInvalid();
		                                                    
		                                                }
	                                                    _fieldByName('panelDatosAdicionales').insert(0, [{
	                                                        xtype:'button',
	                                                        text:'Buscar Empleado',
	                                                          style: {
	                                                            marginLeft:'10px',
	                                                            marginTop: '10px',
	                                                            marginBottom:'10px'
	                                                        },
	                                                         icon    : '${ctx}/resources/fam3icons/icons/zoom.png',
	                                                         handler:function(){
	                                                             ventanaBusquedaEmpleado();
	                                                         }
	                                                    },
	                                                    {
	                                                        xtype:'button',
	                                                        text:'Limpiar Campos',
	                                                        icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png',
	                                                        style: {
                                                                marginLeft:'10px',
                                                                marginTop: '10px',
                                                                marginBottom:'10px'
                                                            },
	                                                        handler:function(){
	                                                           // alert("limpiar")
	                                                           try{
		                                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CLAVE EMPLEADO"]')[0].setReadOnly(false);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="NOMBRE EMPLEADO"]')[0].setReadOnly(false);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO PATERNO"]')[0].setReadOnly(false);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO MATERNO"]')[0].setReadOnly(false);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="RFC EMPLEADO"]')[0].setReadOnly(false);
	                                                                
		                                                            
		                                                            
		                                                            _fieldByLabel("CLAVE EMPLEADO").setValue("");
		                                                            _fieldByLabel("NOMBRE EMPLEADO").setValue("");
		                                                            _fieldByLabel("APELLIDO PATERNO").setValue("");
		                                                            _fieldByLabel("APELLIDO MATERNO").setValue("");
		                                                            _fieldByLabel("RFC EMPLEADO").setValue("");
		                                                            
		                                                            if(panDatComMap1.SITUACION == 'AUTO' ){
		                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]')[0].setReadOnly(false);
		                                                                _fieldByLabel("CURP").setValue("");
		                                                            }
	                                                           }catch(e){
	                                                               debugError(e)
	                                                           }
	                                                            
	                                                            
	                                                           
	                                                        }
	                                                    }]
	                                                    
	                                                    );
	                                                    
	                                                    try{
	                                                        Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CLAVE EMPLEADO"]')[0].on(
	                                                            {
	                                                                'blur':function(){
	                                                                    
	                                                                    buscarEmpleado(
	                                                                             Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="ADMINISTRADORA"]')[0].getValue()
	                                                                            ,Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="RETENEDORA"]')[0].getValue()
	                                                                            ,_fieldByLabel('CLAVE EMPLEADO').getValue()
	                                                                            );
	                                                                }
	                                                            }        
	                                                    );
	                                                    }catch(e){
	                                                        debugError(e)
	                                                    }
	                                                    if(panDatComMap1.SITUACION == 'AUTO' ){
		                                                    //campos variables
		                                                    _mask("cargando datos");
		                                                    Ext.Ajax.request(
		                                                            {
		                                                               
		                                                                 url     : url_admin_ret 
		                                                                ,params :
		                                                                {
		                                                                    'smap1.administradora' :  _fieldByLabel('ADMINISTRADORA').getValue(),
		                                                                    'smap1.retenedora' :    _fieldByLabel('RETENEDORA').getValue()
		                                                                }
		                                                                ,success : function(response)
		                                                                {
		                                                                    _unmask();
		                                                                    var json2 = Ext.decode(response.responseText);
		                                                                    debug('### Response --- :',json2);
		                                                                    if(json2.slist1==null){
		                                                                        json2.slist1=[]
		                                                                    }
		                                                                    json2.slist1.forEach(function(it,idx,arr){
		                                                                      for(var i=17;i<22;i++)  {
		                                                                          
		                                                                        if((it['OTVALOR'+(i+6)]+'').trim()!='' && it['OTVALOR'+(i+6)]!=undefined && it['OTVALOR'+(i+6)]!=null){
		                                                                            
		                                                                            _fieldByName('panelDatosAdicionales').add({
		                                                                                xtype      :'textfield',
		                                                                                fieldLabel :it['OTVALOR'+(i+6)],
		                                                                                name       :'parametros.pv_otvalor'+i,
		                                                                                value      :tvalopol['parametros.pv_otvalor'+i]
		                                                                            });
		                                                                          
		                                                                            
		                                                                            
		                                                                           
		                                                                        }
		                                                                      }
		                                                                      
		                                                                      
		                                                                    });
		                                                                    
		                                                                }
		                                                                ,failure : function()
		                                                                {
		                                                                    _unmask();
		                                                                    errorComunicacion();
		                                                                }
		                                                            });
	                                                    }
		                                           
		                                            
		                                            
		                                        
		                                        }catch(e){
		                                            debugError(e);
		                                        }
		                                    
		                                }
		                            }
		                        })
								,Ext.create('Ext.panel.Panel',
			                                {
			                                    id        : 'panelBeneficiarioHere'
			                                   ,hidden    : true
			                                   ,itemId    : '_BeneficiarioPanel'
			                                   ,height    : 300
			                                   ,autoScroll: false
			                                   ,loader:
			                                   {
			                                        url      : urlPantallaBeneficiarios
			                                       ,params   :
			                                       {
			                                           'smap1.cdunieco'      : inputCdunieco
			                                           ,'smap1.cdramo'       : inputCdramo
			                                           ,'smap1.estado'       : inputEstado
			                                           ,'smap1.nmpoliza'     : inputNmpoliza
			                                           ,'smap1.nmsuplem'     : '1'
			                                           ,'smap1.nmsituac'     : '0'
			                                           ,'smap1.cdrolPipes'   : '3'
			                                           ,'smap1.cdtipsup'     : '1'
			                                           ,'smap1.ultimaImagen' : 'N'
			                                       }
			                                       ,autoLoad: false
			                                       ,scripts:true
			                                   }
			                               })
		                    ],
		                    buttons:
		                    [
		                        {
		                            text:'Guardar',
		                            icon: contexto+'/resources/fam3icons/icons/accept.png',
		                            handler: function(){ _p29_guardarComplementario(null);}	
		                        },
		                        {
		                            text     : 'Turnar a suscripci&oacute;n',
		                            icon    : '${ctx}/resources/fam3icons/icons/user_go.png',
		                            hidden  : (sesionDsrol!='MESADECONTROL')||!Ext.isEmpty(panDatComFlujo)
		                            ,handler : function() {
		                            	var form = Ext.getCmp('formPanel');
                        	            if (form.isValid()) {
                        	                _datComTurnarSuscripcion();	
                        	            } else {
  		                                    Ext.Msg.show({
  		                                        title:'Datos incompletos',
  		                                        msg: 'Favor de introducir todos los campos requeridos',
  		                                        buttons: Ext.Msg.OK,
  		                                        icon: Ext.Msg.WARNING
  		                                    });
  		                                }
                        	        }
		                        },
		                        <%--
		                        {
		                            text:'Editar asegurados',
		                            hidden:true,
		                            icon: contexto+'/resources/fam3icons/icons/user.png',
		                            handler:function(){
		                                Ext.create('Ext.form.Panel').submit({
		                                    url : urlEditarAsegurados,
		                                    standardSubmit:true,
		                                    params:{
		                                        'map1.cdunieco' :  inputCdunieco,
		                                        'map1.cdramo' :    inputCdramo,
		                                        'map1.estado' :    inputEstado,
		                                        'map1.nmpoliza' :  inputNmpoliza
		                                    }
		                                });
		                            }
		                        },
		                        --%>
		                        {
		                            text:'Editar agentes',
		                            icon: contexto+'/resources/fam3icons/icons/user_gray.png',
		                            disabled:true,
		                            hidden:true
		                        },
		                        {
		                            text:'Editar documentos',
		                            icon: contexto+'/resources/fam3icons/icons/book.png',
		                            disabled:true,
		                            hidden:true
		                        }
		                        ,{
                                    text     : 'Guardar y enviar a revisión médica'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/heart_add.png'
                                    ,hidden  : ((!sesionDsrol)||sesionDsrol!='SUSCRIPTOR')||!Ext.isEmpty(panDatComFlujo)
                                    ,handler:function()
                                    {
                                        var form=Ext.getCmp('formPanel');
                                        //console.log(form.getValues());
                                        if (form.isValid()){
						                                        Ext.create('Ext.window.Window',
						                                        {
						                                        	title        : 'Observaciones para el m&eacute;dico'
						                                        	,width       : 600
						                                        	,height      : 430
						                                        	,buttonAlign : 'center'
						                                        	,modal       : true
						                                        	,closable    : false
						                                        	,autoScroll  : true
						                                        	,items       :
						                                        	[
																		{
																		    id        : 'inputTextareaCommentsToMedico'
																		    ,width  : 570
																		    ,height : 300
																		    ,xtype  : 'textarea'
																		}
																		,{
															                xtype       : 'radiogroup'
															                ,fieldLabel : 'Mostrar al agente'
															                ,columns    : 2
															                ,width      : 250
															                ,style      : 'margin:5px;'
															                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
															                ,items      :
															                [
															                    {
															                        boxLabel    : 'Si'
															                        ,itemId     : 'SWAGENTE'
															                        ,name       : 'SWAGENTE'
															                        ,inputValue : 'S'
															                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
															                    }
															                    ,{
															                        boxLabel    : 'No'
															                        ,name       : 'SWAGENTE'
															                        ,inputValue : 'N'
                                                                                    ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
															                    }
															                ]
															            }
						                                        	]
						                                        	,buttons    :
						                                        	[
						                                        	    {
						                                        	    	text     : 'Guardar y enviar a revisión médica'
						                                                    ,icon    : '${ctx}/resources/fam3icons/icons/heart_add.png'
						                                        	    	,handler : function()
						                                        	    	{                                   	    			
						                                        	    			var window=this.up().up();
						                                                            window.setLoading(true);
						                                                            form.submit({
						                                                                params:{
						                                                                    'map1.pv_cdunieco' :  inputCdunieco,
						                                                                    'map1.pv_cdramo' :    inputCdramo,
						                                                                    'map1.pv_estado' :    inputEstado,
						                                                                    'map1.pv_nmpoliza' :  inputNmpoliza
						                                                                },
						                                                                success:function(){
						                                                                    Ext.Ajax.request
						                                                                    ({
						                                                                        url     : datComUrlMCUpdateStatus
						                                                                        ,params : 
						                                                                        {
						                                                                            'smap1.ntramite' : inputNtramite
						                                                                            ,'smap1.status'  : '1'//en revision medica
						                                                                            ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToMedico').getValue()
						                                                                            ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
						                                                                        }
						                                                                        ,success : function(response)
						                                                                        {
						                                                                            var json=Ext.decode(response.responseText);
						                                                                            if(json.success==true)
						                                                                            {
						                                                                                Ext.create('Ext.form.Panel').submit
						                                                                                ({
						                                                                                    url             : datComUrlMC
						                                                                                    ,params         :
						                                                                                    {
						                                                                                    	'smap1.gridTitle':'Tareas',
						                                                                                    	'smap2.pv_cdtiptra_i':1,
						                                                                                    	'smap1.editable':1
						                                                                                    }
						                                                                                    ,standardSubmit : true
						                                                                                });
						                                                                            }
						                                                                            else
						                                                                            {
						                                                                            	window.setLoading(false);
						                                                                                Ext.Msg.show({
						                                                                                    title:'Error',
						                                                                                    msg: 'Error al enviar a revisi&oacute;n m&eacute;dica',
						                                                                                    buttons: Ext.Msg.OK,
						                                                                                    icon: Ext.Msg.ERROR
						                                                                                });
						                                                                            }
						                                                                        }
						                                                                        ,failure : function()
						                                                                        {
						                                                                            Ext.Msg.show({
						                                                                                title:'Error',
						                                                                                msg: 'Error de comunicaci&oacute;n',
						                                                                                buttons: Ext.Msg.OK,
						                                                                                icon: Ext.Msg.ERROR
						                                                                            });
						                                                                        }
						                                                                    });
						                                                                },
						                                                                failure:function(){
						                                                                	window.setLoading(false);
						                                                                    Ext.Msg.show({
						                                                                        title:'Error',
						                                                                        msg: 'Error de comunicaci&oacute;n',
						                                                                        buttons: Ext.Msg.OK,
						                                                                        icon: Ext.Msg.ERROR
						                                                                    });
						                                                                }
						                                                            });
						                                        	    	}
						                                        	    }
						                                        	    ,{
						                                        	    	text  : 'Cancelar'
						                                        	    	,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
						                                        	    	,handler : function()
						                                        	    	{
						                                        	    		this.up().up().destroy();
						                                        	    	}
						                                        	    }
						                                        	]
						                                        }).show();
				                        } else {
                                            Ext.Msg.show({
                                                title:'Datos incompletos',
                                                msg: 'Favor de introducir todos los campos requeridos',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.WARNING
                                          });
                                        }   
                                     }
                                }
		                        ,{
		                            text     : ['COTIZADOR', 'SUPTECSALUD'].indexOf(sesionDsrol) != -1
		                                           ? 'Cotizar'
		                                           : 'Emitir'
                                    ,itemId  : 'panDatComBotonRetarificar'
                                    ,icon    : contexto+'/resources/fam3icons/icons/key.png'
                                    //,hidden  : ((!sesionDsrol)||sesionDsrol!='SUSCRIPTOR')&&panDatComMap1.SITUACION!='AUTO'
                                    ,hidden  : panDatComMap1.SITUACION !== 'AUTO' && (['SUSCRIPTOR', 'COTIZADOR', 'SUPTECSALUD'].indexOf(sesionDsrol) === -1)
                                    ,handler : function(me)
                                    {
                                    	try
                                    	{
	                                    	_p29_guardarComplementario
	                                    	(
	                                    	  function()
	                                    	  {
	                                    			if(inputCdramo == 16)
	                                                {
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
	                                                                     var json=Ext.decode(response.responseText);
	                                                                     if(json.exito)
	                                                                     {
	                                                                             var _p29_validaSeguro = json.smap1['parametros.pv_seguroVida'];
	                                                                            
	                                                                                        debug('fn:', _p29_validaSeguro);
	                                                                             if(_p29_validaSeguro == "S")
	                                                                             {    
	                                                                                 var suma=0;
	                                                                                 _p32_store.each(function(record)
	                                                                                 {
	                                                                                     if(record.get('mov')+'x'!='-x')
	                                                                                     {
	                                                                                         suma=suma+(record.get('PORBENEF')-0);
	                                                                                     }
	                                                                                 });
	                                                                                 if(suma!=100)
	                                                                                 {  
	                                                                                     mensajeError('La suma de porcentajes de beneficiarios activos es '+suma+', en lugar de 100');
	                                                                                     return
	                                                                                 }
	                                                                                 else
	                                                                                 {
	                                                                                	 try{
	                                                                                     _p32_guardarClic(_p29_emitirClicComplementarios);
	                                                                                	 }
	                                                                                	 catch(e){manejaException(e);}
	                                                                                     
	                                                                                 }
	
	                                                                             }
	                                                                             else
	                                                                             {
	                                                                                 _p29_emitirClicComplementarios();
	                                                                             }
	                                                                     }
	                                                                     else
	                                                                     {
	                                                                         mensajeError(json.respuesta);
	                                                                     }
	                                                                 }
	                                                             });
	                                                }
	                                                else
	                                                {
	                                                    _p29_emitirClicComplementarios();
	                                                }
	                                    	  }
	                                    	);
                                    	}
                                    	catch(e)
                                        {
                                            debugError(e,' panDatComBotonRetarificar handler ');
                                        }
                                    }
                                  }
		                        ,{
                                    text     : 'Guardar y dar Vo. Bo.'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/heart_add.png'
                                    ,hidden  : ((!sesionDsrol)||sesionDsrol!='MEDICO')||!Ext.isEmpty(panDatComFlujo)
                                    ,handler:function()
                                    {
                                    	var form=Ext.getCmp('formPanel');                                         
                                        if(form.isValid()) {           
                                    	
	                                    	Ext.create('Ext.window.Window',
	                                        {
	                                            title        : 'Dictamen para mesa de control'
	                                            ,width       : 600
	                                            ,height      : 430
	                                            ,buttonAlign : 'center'
	                                            ,modal       : true
	                                            ,closable    : false
	                                            ,autoScroll  : true
	                                            ,items       :
	                                            [
													{
													    id        : 'inputTextareaCommentsToMCFromMedico'
												    	,width  : 570
	                                                    ,height : 300
	                                                    ,xtype  : 'textarea'
													}
													,{
										                xtype       : 'radiogroup'
										                ,fieldLabel : 'Mostrar al agente'
										                ,columns    : 2
										                ,width      : 250
										                ,style      : 'margin:5px;'
										                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
										                ,items      :
										                [
										                    {
										                        boxLabel    : 'Si'
										                        ,itemId     : 'SWAGENTE'
										                        ,name       : 'SWAGENTE'
										                        ,inputValue : 'S'
										                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
										                    }
										                    ,{
										                        boxLabel    : 'No'
										                        ,name       : 'SWAGENTE'
										                        ,inputValue : 'N'
                                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
										                    }
										                ]
										            }
	                                            ]
	                                            ,buttons    :
	                                            [
	                                                {
	                                                    text     : 'Guardar y dar Vo. Bo.'
	                                                    ,icon    : '${ctx}/resources/fam3icons/icons/heart_add.png'
	                                                    ,handler : function()
	                                                    {
					                                        var form=Ext.getCmp('formPanel');
					                                        var window=this.up().up();
					                                        //console.log(form.getValues());
					                                        	window.setLoading(true);
					                                            form.submit({
					                                                params:{
					                                                    'map1.pv_cdunieco' :  inputCdunieco,
					                                                    'map1.pv_cdramo' :    inputCdramo,
					                                                    'map1.pv_estado' :    inputEstado,
					                                                    'map1.pv_nmpoliza' :  inputNmpoliza
					                                                },
					                                                success:function(){
					                                                    Ext.Ajax.request
					                                                    ({
					                                                        url     : datComUrlMCUpdateStatus
					                                                        ,params : 
					                                                        {
					                                                            'smap1.ntramite'   : inputNtramite
					                                                            ,'smap1.status'    : '5'//Vo.Bo.Medico
					                                                            ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToMCFromMedico').getValue()
					                                                            ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
					                                                        }
					                                                        ,success : function(response)
					                                                        {
					                                                            var json=Ext.decode(response.responseText);
					                                                            if(json.success==true)
					                                                            {
					                                                                Ext.create('Ext.form.Panel').submit
					                                                                ({
					                                                                    url             : datComUrlMC
					                                                                    ,standardSubmit : true
					                                                                    ,params         :
	                                                                                    {
	                                                                                        'smap1.gridTitle':'Tareas',
	                                                                                        'smap2.pv_cdtiptra_i':1,
	                                                                                        'smap1.editable':1
	                                                                                    }
					                                                                });
					                                                            }
					                                                            else
					                                                            {
					                                                            	window.setLoading(false);
					                                                                Ext.Msg.show({
					                                                                    title:'Error',
					                                                                    msg: 'Error al guardar Vo. Bo.',
					                                                                    buttons: Ext.Msg.OK,
					                                                                    icon: Ext.Msg.ERROR
					                                                                });
					                                                            }
					                                                        }
					                                                        ,failure : function()
					                                                        {
					                                                        	window.setLoading(false);
					                                                            Ext.Msg.show({
					                                                                title:'Error',
					                                                                msg: 'Error de comunicaci&oacute;n',
					                                                                buttons: Ext.Msg.OK,
					                                                                icon: Ext.Msg.ERROR
					                                                            });
					                                                        }
					                                                    });
					                                                },
					                                                failure:function(){
					                                                	window.setLoading(false);
					                                                    Ext.Msg.show({
					                                                        title:'Error',
					                                                        msg: 'Error de comunicaci&oacute;n',
					                                                        buttons: Ext.Msg.OK,
					                                                        icon: Ext.Msg.ERROR
					                                                    });
					                                                }
					                                            });
	                                                    }
	                                                }
	                                                ,
	                                                {
	                                                	text  : 'Cancelar'
	                                                    ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
	                                                    ,handler : function()
	                                                    {
	                                                        this.up().up().destroy();
	                                                    }
	                                                }
	                                            ]
	                                        }).show();
                                        } else {
                                            Ext.Msg.show({
                                                title:'Datos incompletos',
                                                msg: 'Favor de introducir todos los campos requeridos',
                                                buttons: Ext.Msg.OK,
                                                icon: Ext.Msg.WARNING
                                            });
                                        }
                                    }
                                }
		                        ,{
                                    text     : 'Guardar como pendiente de informaci&oacute;n'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/clock.png'
                                    ,hidden  : ((!sesionDsrol)||sesionDsrol!='MEDICO')||!Ext.isEmpty(panDatComFlujo)
                                    ,handler:function()
                                    {
                                    	var form=Ext.getCmp('formPanel');                                         
                                        if(form.isValid()) {                                        	
                                            Ext.create('Ext.window.Window',
                                            {
                                                title        : 'Dictamen para mesa de control'
                                                ,width       : 600
                                                ,height      : 430
                                                ,buttonAlign : 'center'
                                                ,modal       : true
                                                ,closable    : false
                                                ,autoScroll  : true
                                                ,items       :
                                                [
                                                    {
                                                        id        : 'inputTextareaCommentsToMCFromMedico'
                                                        ,width  : 570
                                                        ,height : 300
                                                        ,xtype : 'textarea'
                                                    }
                                                    ,{
										                xtype       : 'radiogroup'
										                ,fieldLabel : 'Mostrar al agente'
										                ,columns    : 2
										                ,width      : 250
										                ,style      : 'margin:5px;'
										                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
										                ,items      :
										                [
										                    {
										                        boxLabel    : 'Si'
										                        ,itemId     : 'SWAGENTE'
										                        ,name       : 'SWAGENTE'
										                        ,inputValue : 'S'
										                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
										                    }
										                    ,{
										                        boxLabel    : 'No'
										                        ,name       : 'SWAGENTE'
										                        ,inputValue : 'N'
                                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
										                    }
										                ]
										            }
                                                ]
                                                ,buttons    :
                                                [
                                                    {
                                                    	text     : 'Guardar como pendiente de informaci&oacute;n'
                                                        ,icon    : '${ctx}/resources/fam3icons/icons/clock.png'
                                                        ,handler : function()
                                                        {
                                                            var form=Ext.getCmp('formPanel');
                                                            var window=this.up().up();
                                                            //console.log(form.getValues());
                                                           
                                                                window.setLoading(true);
                                                                form.submit({
                                                                    params:{
                                                                        'map1.pv_cdunieco' :  inputCdunieco,
                                                                        'map1.pv_cdramo' :    inputCdramo,
                                                                        'map1.pv_estado' :    inputEstado,
                                                                        'map1.pv_nmpoliza' :  inputNmpoliza
                                                                    },
                                                                    success:function(){
                                                                        Ext.Ajax.request
                                                                        ({
                                                                            url     : datComUrlMCUpdateStatus
                                                                            ,params : 
                                                                            {
                                                                                'smap1.ntramite'   : inputNtramite
                                                                                ,'smap1.status'    : '6'//Esperando info
                                                                                ,'smap1.comments' : Ext.getCmp('inputTextareaCommentsToMCFromMedico').getValue()
                                                                                ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue() 
                                                                            }
                                                                            ,success : function(response)
                                                                            {
                                                                                var json=Ext.decode(response.responseText);
                                                                                if(json.success==true)
                                                                                {
                                                                                    Ext.create('Ext.form.Panel').submit
                                                                                    ({
                                                                                        url             : datComUrlMC
                                                                                        ,standardSubmit : true
                                                                                        ,params         :
                                                                                        {
                                                                                            'smap1.gridTitle':'Tareas',
                                                                                            'smap2.pv_cdtiptra_i':1,
                                                                                            'smap1.editable':1
                                                                                        }
                                                                                    });
                                                                                }
                                                                                else
                                                                                {
                                                                                    window.setLoading(false);
                                                                                    Ext.Msg.show({
                                                                                        title:'Error',
                                                                                        msg: 'Error al guardar Vo. Bo.',
                                                                                        buttons: Ext.Msg.OK,
                                                                                        icon: Ext.Msg.ERROR
                                                                                    });
                                                                                }
                                                                            }
                                                                            ,failure : function()
                                                                            {
                                                                                window.setLoading(false);
                                                                                Ext.Msg.show({
                                                                                    title:'Error',
                                                                                    msg: 'Error de comunicaci&oacute;n',
                                                                                    buttons: Ext.Msg.OK,
                                                                                    icon: Ext.Msg.ERROR
                                                                                });
                                                                            }
                                                                        });
                                                                    },
                                                                    failure:function(){
                                                                        window.setLoading(false);
                                                                        Ext.Msg.show({
                                                                            title:'Error',
                                                                            msg: 'Error de comunicaci&oacute;n',
                                                                            buttons: Ext.Msg.OK,
                                                                            icon: Ext.Msg.ERROR
                                                                        });
                                                                    }
                                                                });
                                                            
                                                          
                                                        }
                                                    }
                                                    ,
                                                    {
                                                        text  : 'Cancelar'
                                                        ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
                                                        ,handler : function()
                                                        {
                                                            this.up().up().destroy();
                                                        }
                                                    }
                                                ]
                                            }).show();
                                        } else {                                        	    
                                                Ext.Msg.show({                                                	
                                                    title:'Datos incompletos',
                                                    msg: 'Favor de introducir todos los campos requeridos',
                                                    buttons: Ext.Msg.OK,
                                                    icon: Ext.Msg.WARNING
                                                });
                                          }
                                    }
                                }
		                        ,{
                                    text     : 'Rechazar'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                    ,hidden  : ((!sesionDsrol)||(sesionDsrol!='SUSCRIPTOR'&&sesionDsrol!='MEDICO'))||!Ext.isEmpty(panDatComFlujo)
                                    ,handler:function()
                                    {
                                        var form=Ext.getCmp('formPanel');
                                        var idClausula;
                                        
                                        if(sesionDsrol=='MEDICO'){
                                        	descripcion = 'CARTA RECHAZO MEDICO';
                                        }else{
                                        	descripcion ='CARTA RECHAZO ADMINISTRATIVA';
                                        }
                                        
                                        //Obtengo el valor del ID para obtener el valor de la descripcion
                                        Ext.Ajax.request(
                           				{
                           				    url     : _URL_CONSULTA_CLAUSU
                           				    ,params : 
                           				    {
                           						'params.cdclausu' : null,
												'params.dsclausu' : descripcion
                           				    }
                           				    ,success : function (response)
                           				    {
                           				    	var json=Ext.decode(response.responseText);
                           				    	var claveClausula = json.listaGenerica[0].key;
                                                
                           				    	Ext.Ajax.request(
        										{
        										    url     : _URL_CONSULTA_CLAUSU_DETALLE
        										    ,params : 
        										    {
        										        'params.cdclausu'  : claveClausula
        										    }
        										    ,success : function (response)
        										    {
        										    	var json=Ext.decode(response.responseText);
        										    	txtContenido =json.msgResult;
        										    	
        										    	Ext.create('Ext.window.Window',
   		                                                {
   		                                                    title        : 'Guardar detalle'
   		                                                    ,width       : 600
   		                                                    ,height      : 430
   		                                                    ,buttonAlign : 'center'
   		                                                    ,modal       : true
   		                                                    ,closable    : false
   		                                                    ,autoScroll  : true
   		                                                    ,items       :
   		                                                    [
   		                                                        Ext.create('Ext.form.field.TextArea', {
   		                                                            id        : 'inputTextareaCommentsToRechazo'
   		                                                            ,width  : 570
   		                                                            ,height : 200
   		                                                            ,value  : txtContenido
   		                                                        }),
   		                                                     	Ext.create('Ext.form.field.TextArea', {
		                                                            id        : 'inputTextareaComments'
		                                                            ,width  : 570
		                                                            ,height : 100
		                                                        })
		                                                        ,{
													                xtype       : 'radiogroup'
													                ,fieldLabel : 'Mostrar al agente'
													                ,columns    : 2
													                ,width      : 250
													                ,style      : 'margin:5px;'
													                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
													                ,items      :
													                [
													                    {
													                        boxLabel    : 'Si'
													                        ,itemId     : 'SWAGENTE'
													                        ,name       : 'SWAGENTE'
													                        ,inputValue : 'S'
													                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
													                    }
													                    ,{
													                        boxLabel    : 'No'
													                        ,name       : 'SWAGENTE'
													                        ,inputValue : 'N'
                                                                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
													                    }
													                ]
													            }
   		                                                    ]
   		                                                    ,buttons    :
   		                                                    [
   		                                                        {
   		                                                            text     : 'Rechazar'
   		                                                            ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
   		                                                            ,handler : function()
   		                                                            {
   		                                                                if(true||form.isValid())
   		                                                                {
   		                                                                    var window=this.up().up();
   		                                                                    window.setLoading(true);
   		                                                                    
                                                                            Ext.Ajax.request
                                                                            ({
                                                                                url     : datComUrlMCUpdateStatus
                                                                                ,params : 
                                                                                {
                                                                                    'smap1.ntramite' : inputNtramite
                                                                                    ,'smap1.status'  : '4'//rechazado
                                                                                    ,'smap1.comments' : Ext.getCmp('inputTextareaComments').getValue()
                                                                                    ,'smap1.swagente' : _fieldById('SWAGENTE').getGroupValue()
                                                                                }
                                                                                ,success : function(response)
                                                                                {
                                                                                    var json=Ext.decode(response.responseText);
                                                                                    if(json.success==true)
                                                                                    {
                                                                                        Ext.Ajax.request(
                                                                                        {
                                                                                            url     : compleUrlGuardarCartoRechazo
                                                                                         	,method:'GET'
                                                                                            ,params :
                                                                                            {
                                                                                                'map1.ntramite'  : inputNtramite
                                                                                                ,'map1.comments' : Ext.getCmp('inputTextareaCommentsToRechazo').getValue()
                                                                                                ,'map1.cdsisrol' : sesionDsrol
                                                                                                ,'map1.cdunieco' : inputCdunieco
                                                                                                ,'map1.cdramo'   : inputCdramo
                                                                                                ,'map1.estado'   : inputEstado
                                                                                                ,'map1.nmpoliza' : inputNmpoliza
                                                                                            }
		                                                                                    ,success : function(response)
	   		                                                                                {
		                                                                                    	Ext.create('Ext.form.Panel').submit
		                                                                                        ({
		                                                                                            url             : datComUrlMC
		                                                                                            ,standardSubmit : true
		                                                                                            ,params         :
		                                                                                            {
		                                                                                                'smap1.gridTitle':'Tareas',
		                                                                                                'smap2.pv_cdtiptra_i':1,
		                                                                                                'smap1.editable':1
		                                                                                            }
		                                                                                        });
	   		                                                                                }
 		                                                                                 ,failure : function()
 		                                                                                {
 		                                                                                    Ext.Msg.show({
 		                                                                                        title:'Error',
 		                                                                                        msg: 'Error de comunicaci&oacute;n',
 		                                                                                        buttons: Ext.Msg.OK,
 		                                                                                        icon: Ext.Msg.ERROR
 		                                                                                    });
 		                                                                                }
                                                                                        });
                                                                                    }else{
                                                                                        window.setLoading(false);
                                                                                        Ext.Msg.show({
                                                                                            title:'Error',
                                                                                            msg: 'Error al rechazar',
                                                                                            buttons: Ext.Msg.OK,
                                                                                            icon: Ext.Msg.ERROR
                                                                                        });
                                                                                    }
                                                                                }
                                                                                ,failure : function()
                                                                                {
                                                                                    Ext.Msg.show({
                                                                                        title:'Error',
                                                                                        msg: 'Error de comunicaci&oacute;n',
                                                                                        buttons: Ext.Msg.OK,
                                                                                        icon: Ext.Msg.ERROR
                                                                                    });
                                                                                }
                                                                            });
   		                                                                }
   		                                                                else
   		                                                                {
   		                                                                    Ext.Msg.show({
   		                                                                        title:'Datos incompletos',
   		                                                                        msg: 'Favor de introducir todos los campos requeridos',
   		                                                                        buttons: Ext.Msg.OK,
   		                                                                        icon: Ext.Msg.WARNING
   		                                                                    });
   		                                                                }
   		                                                            }
   		                                                        }
   		                                                        ,{
   		                                                            text  : 'Cancelar'
   		                                                            ,icon : '${ctx}/resources/fam3icons/icons/cancel.png'
   		                                                            ,handler : function()
   		                                                            {
   		                                                                this.up().up().destroy();
   		                                                            }
   		                                                        }
   		                                                    ]
   		                                                }).show();
        										    },
        										    failure : function ()
        										    {
        										        Ext.Msg.show({
        										            title:'Error',
        										            msg: 'Error de comunicaci&oacute;n',
        										            buttons: Ext.Msg.OK,
        										            icon: Ext.Msg.ERROR
        										        });
        										    }
        										});
                           				    },
                           				    failure : function ()
                           				    {
                           				        Ext.Msg.show({
                           				            title:'Error',
                           				            msg: 'Error de comunicaci&oacute;n',
                           				            buttons: Ext.Msg.OK,
                           				            icon: Ext.Msg.ERROR
                           				        });
                           				    }
                           				});

                                    }
                                }
		                    ]
                            ,listeners:
                            {
                            	expand:function( p, eOpts )
                            	{
                            		window.parent.scrollTo(0,150+p.y);
                            	},
                            	afterrender:function(tab)
                                {
                                    debug('afterrender tabPanelAsegurados');
                                    
                                    // Validaciones:
                                    
                                    // Validacion para NUMERO DE CONTRATO en sucursal 1403 ELP
                                    debug('**traeme la cdunieco',inputCdunieco);
                                    
                                    debug('**encontrado? ',_fieldByLabel('NUMERO DE CONTRATO',null,true));
                                    // Verifica si el campo existe en el producto utilizado ELP
                                    if (!Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO',null,true)))
                                    {
                                    	debug('**encontrado? ', _fieldByLabel('NUMERO DE CONTRATO').value,_fieldByLabel('NUMERO DE CONTRATO',null,true));
                                    	
                                    	/* Cuando es un producto valido verifica si es la sucursal correcta y esta vacio,  
                                    	 * y modifica la obligatoriedad del campo segun la sucursal correcta.
                                    	 */
	                                    //if (inputCdunieco == 1403 && Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO').value)){
	                                    //if ((inputCdunieco == 1403) && (Ext.isEmpty(_fieldByLabel('NUMERO DE CONTRATO').value))||(_fieldByLabel('NUMERO DE CONTRATO').value==0)){
	                                    if (inputCdunieco == 1403 && _fieldByLabel('NUMERO DE CONTRATO').value==0){
	                                    	_fieldByLabel('NUMERO DE CONTRATO',null,true).allowBlank = false;
	                                    	_fieldByLabel('NUMERO DE CONTRATO',null,true).regex = /^[a-zA-Z]{3}[-]\d{3}$/;
	                                    	_fieldByLabel('NUMERO DE CONTRATO',null,true).regexText = 'Debe cumplir con el formato /^[a-zA-Z]{3}[-]\d{3}$/';
	                                    	debug('**Numero de contrato puede estar vacio? ',_fieldByLabel('NUMERO DE CONTRATO').allowBlank);
	                                    	debug('**Valor en Numero de contrato',_fieldByLabel('NUMERO DE CONTRATO').value);
	                                    }else{
	                                    	_fieldByLabel('NUMERO DE CONTRATO').allowBlank = true;
	                                    }
                                    }    
                                    //
                                    if(inputCdtipsit=='AF'){
                                    	Ext.Ajax.request({
                                  			url     : _URL_ObtieneValNumeroSerie
                                  			,params :
                                  			{
                                  				'smap1.numSerie'  : _fieldByName('parametros.pv_otvalor03').getValue()
                                  				,'smap1.feini'   : _fieldByName('panel2.feefec').getValue()
                                  			}
                                  			,success : function(response)
                                  			{
                                  				var json=Ext.decode(response.responseText);
                                  				if(json.exito!=true)
                                  				{
                                  					if(!RolSistema.puedeSuscribirAutos(sesionDsrol)/*sesionDsrol!='SUSCRIAUTO'*/){
                                  						//mensajeError(json.respuesta);
                                  						mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", json.respuesta);
                                  						 _fieldById('panDatComBotonRetarificar').setDisabled(true);//Deshabilita el boton
                                  					}else{
                                  						//mensajeWarning(json.respuesta);
                                  						mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", json.respuesta);
                                  						_fieldById('panDatComBotonRetarificar').setDisabled(false);
                                  					}
                                  				}else{
                                  					_fieldById('panDatComBotonRetarificar').setDisabled(false);
                                  				}
                                  			}
                                  			,failure : errorComunicacion
                                  		});
                                    }
                                    
                                    recuperaPlazoDias();
                                }
                            }
		                })
                        ,Ext.create('Ext.panel.Panel',
                        {
                            id:'tabPanelAgentes'
                            ,title:'Agentes'
                            ,cls:'claseTitulo'
                            ,border:0
                            ,loader:
                            {
                                url       : urlPantallaAgentes
                                ,scripts  : true
                                ,autoLoad : true
                            }
                            ,listeners:
                            {
                                afterrender:function(tab)
                                {
                                    debug('afterrender tabPanelAgentes');
                                    tab.loader.load();
                                }
                            }
                        })
                    ]
                    ,listeners:
                    {
                        add :function(panel,tab,pos)
                        {
                            debug('>accordion add pos:',pos);
                            if(pos>_panDatCom_numPestaniasIniciales-1)
                            {
                                tab.border=0;
                                panel.setActiveTab(tab);
                            }
                            debug('<accordion add');
                        }
                    }
                });
                
                Ext.create('Ext.panel.Panel',
                {
                    renderTo  : 'maindiv'
                    ,defaults : { style : 'margin:5px;' }
                    ,border   : 0
                    ,items    :
                    [
                        Ext.create('Ext.panel.Panel',
			            {
			                itemId       : '_datcom_panelFlujo'
			                ,title       : 'ACCIONES'
			                ,hidden      : Ext.isEmpty(panDatComFlujo) || 'RSTN' === panDatComFlujo.aux
			                ,buttonAlign : 'left'
			                ,buttons     : []
			                ,listeners   :
			                {
			                    afterrender : function(me)
			                    {
			                        if(!Ext.isEmpty(panDatComFlujo))
			                        {
			                            _cargarBotonesEntidad(
			                                panDatComFlujo.cdtipflu
			                                ,panDatComFlujo.cdflujomc
			                                ,panDatComFlujo.tipoent
			                                ,panDatComFlujo.claveent
			                                ,panDatComFlujo.webid
			                                ,me.itemId//callback
			                                ,panDatComFlujo.ntramite
			                                ,panDatComFlujo.status
			                                ,panDatComFlujo.cdunieco
			                                ,panDatComFlujo.cdramo
			                                ,panDatComFlujo.estado
			                                ,panDatComFlujo.nmpoliza
			                                ,panDatComFlujo.nmsituac
			                                ,panDatComFlujo.nmsuplem
			                                ,null//callbackDespuesProceso
			                            );
			                        }
			                    }
			                }
			            })
			            ,accordion
                    ]
                });
                
                checarBenef();
                
                /*
                if(!Ext.isEmpty(panDatComFlujo))
                {
                    var formPanel = _fieldById('formPanel');
                    _cargarBotonesEntidad
                    (
                        panDatComFlujo.cdtipflu
                        ,panDatComFlujo.cdflujomc
                        ,panDatComFlujo.tipoent
                        ,panDatComFlujo.claveent
                        ,panDatComFlujo.webid
                        ,function(botones)
                        {
                            debug('botones:',botones);
                            for(var i in botones)
                            {
                                var boton = botones[i];
                                formPanel.down('toolbar').add(boton);
                                boton.clase = 'botonFlujo';
                                boton.hide();
                            }
                        }
                        ,panDatComFlujo.ntramite
                        ,panDatComFlujo.status
                        ,panDatComFlujo.cdunieco
                        ,panDatComFlujo.cdramo
                        ,panDatComFlujo.estado
                        ,panDatComFlujo.nmpoliza
                        ,panDatComFlujo.nmsituac
                        ,panDatComFlujo.nmsuplem
                        ,function(){alert();}
                    );
                }
                */
                
                function creaWindowPay(url, params, tarjet )
			    {
			             var form = document.createElement("form");
			             form.setAttribute("method", "post");
			             form.setAttribute("action", url);
			             form.setAttribute("target", "IDventana");
			  
			             for (var i in params) {
			                 if (params.hasOwnProperty(i)) {
			                     var input = document.createElement('input');
			                     input.type = 'hidden';
			                     input.name = i;
			                     input.value = params[i];
			                     form.appendChild(input);
			                 }
			             }
			             
			             document.body.appendChild(form);
			  
			             //note I am using a post.htm page since I did not want to make double request to the page 
			            //it might have some Page_Load call which might screw things up.
			//             window.open("post.htm", name, windowoption);
			  
			             form.submit();
			  
			             document.body.removeChild(form);
			     }
                
                //funcion para reintentar WS auto
                
                function reintentarWSAuto(loading, params){

                	Ext.Msg.show({
	                   title    :'Confirmaci&oacute;n'
	                   ,msg     : '&iquest;Desea Reenviar los Web Services de Autos?'
	                   ,buttons : Ext.Msg.YESNO
	                   ,icon    : Ext.Msg.QUESTION
	                   ,fn      : function(boton, text, opt){
	                   	if(boton == 'yes'){
	                   		
	                   		loading.setLoading(true);
	                    	
	                    	Ext.Ajax.request(
	                            	{
	                            		url     : urlReintentarWS
	                            		,params :params
	                            	    ,success:function(response)
	                            	    {
	                            	    	loading.setLoading(false);
	                            	    	var json=Ext.decode(response.responseText);
	                            	    	debug(json);
	                            	    	if(json.success==true)
	                            	    	{
	                            	    		mensajeCorrecto('Aviso', 'Ejecuci&oacute;n Correcta de Reintento. P&oacute;liza Emitida: ' + json.nmpolAlt);
	                            	    		
	                            	    		datComPolizaMaestra=json.panel2.nmpoliza;
	                            	    		
	                            	    		Ext.getCmp('numerofinalpoliza').setValue(json.nmpolAlt);
	                            	    		_numeroPolizaExt = json.nmpolAlt;
                                	    		Ext.getCmp('botonImprimirPolizaFinal').setDisabled(false);
                                	    		Ext.getCmp('botonPagar').setDisabled(false);
                                	    		Ext.getCmp('botonReenvioWS').setDisabled(true);
                                	    		Ext.getCmp('botonReenvioWS').hide();
                                	    		
                                	    		_mensajeEmail = json.mensajeEmail;
												Ext.getCmp('botonEnvioEmail').enable();
	                            	    	}
	                            	    	else
	                            	    	{
	                            	    		Ext.Msg.show({
	                                                title    :'Aviso'
	                                                ,msg     : json.mensajeRespuesta
	                                                ,buttons : Ext.Msg.OK
	                                                ,icon    : Ext.Msg.WARNING
	                                                ,fn      : function(){
	                                                	reintentarWSAuto(loading, params);
	                                                }
	                                            });
	                            	    	}
	                            	    }
	                            	    ,failure:function()
	                            	    {
	                            	    	loading.setLoading(false);
	                            	    	Ext.Msg.show({
	                                            title:'Error',
	                                            msg: 'Error de comunicaci&oacute;n',
	                                            buttons: Ext.Msg.OK,
	                                            icon: Ext.Msg.ERROR
	                                            ,fn      : function(){
	                                            	reintentarWSAuto(loading, params);
	                                            }
	                                        });
	                            	    }
	                            	});
	                   	}else{
	                   		_paramsRetryWS = params;
	                   		debug("Habilitando Boton Reenvio WS");
	                   		Ext.getCmp('botonReenvioWS').setDisabled(false);
	                   	}
	                   }
                	});
                	                	
                }
                
                
                function mensajeValidacionNumSerie(titulo,imagenSeccion,txtRespuesta){              	
                	var panelImagen = new Ext.Panel({
                		defaults 	: {
                			style   : 'margin:5px;'
                		},
                		layout: {
                			type: 'hbox'
                			,align: 'center'
                			,pack: 'center'
                		}
                		,border: false
                		,items:[{	        	
                			xtype   : 'image'
                			,src    : contexto+'/images/cotizacionautos/menu_endosos.png'
                			,width: 200
                			,height: 100
                		}]
                	});

                	validacionNumSerie = Ext.create('Ext.window.Window',{
                		title        : titulo
                		,modal       : true
                		,buttonAlign : 'center'
                		,width		 : 520
                		,icon 		 : imagenSeccion
                		,resizable	 : false
                		,height      : 250
                		,items       :[
                			Ext.create('Ext.form.Panel', {
                				id: 'panelClausula'
                				,width		 : 500
                				,height      : 150
                				,bodyPadding: 5
                				,renderTo: Ext.getBody()
                				,defaults 	 : {
                					style : 'margin:5px;'
                				}
                				,border: false
                				,items: [
                				{
                					xtype  : 'label'
                					,text  : txtRespuesta
                					,width : 100
                					,height: 100
                					,style : 'color:red;margin:10px;'
                				}
                				,{
                					border: false
                					,items    :
                						[	panelImagen		]
                				}]
                			})
                		],
                		buttonAlign:'center',
                		buttons: [{
                			text: 'Aceptar',
                			icon: contexto+'/resources/fam3icons/icons/accept.png',
                			buttonAlign : 'center',
                			handler: function() {
                				validacionNumSerie.close();
                			}
                		}]
                	});
                	centrarVentanaInterna(validacionNumSerie.show());
                }
                
                
                //para ver documentos en vivo
                var venDocuTramite=Ext.create('Ext.window.Window',
		        {
                	itemId          : 'IdvenDocuTramite'
		            ,title          : 'Documentos del tr&aacute;mite '+inputNtramite
		            ,closable       : false
		            ,width          : 500
		            ,height         : 300
		            ,autoScroll     : true
		            ,collapsible    : true
		            ,titleCollapse  : true
		            ,startCollapsed : true
		            ,resizable      : false
		            ,cls            : 'VENTANA_DOCUMENTOS_CLASS'
		            ,loader         :
		            {
		                scripts   : true
		                ,autoLoad : true
		                ,url      : panDatComUrlDoc
		                ,params   :
		                {
		                    'smap1.cdunieco'  : inputCdunieco
		                    ,'smap1.cdramo'   : inputCdramo
		                    ,'smap1.estado'   : inputEstado
		                    ,'smap1.nmpoliza' : '0'
		                    ,'smap1.nmsuplem' : '0'
		                    ,'smap1.nmsolici' : ''
		                    ,'smap1.ntramite' : inputNtramite
		                    ,'smap1.tipomov'  : '0'
		                    ,'smap1.lista'    : 'EMISION'
		                }
		            }
		        }).showAt(500,0);
                venDocuTramite.collapse();
                if ((!Ext.isEmpty(panDatComFlujo)) && 'RSTN' === panDatComFlujo.aux) {
                    venDocuTramite.hide();
                }
                //para ver documentos en vivo
                
                //codigo dinamico recuperado de la base de datos
                <s:property value="map1.customCode" escapeHtml="false" />
                
                ///////////////////////////////////////////////
                ////// Cargador de formulario (sin grid) //////
                /*///////////////////////////////////////////*/
                Ext.define('LoaderForm',
                {
                    extend:'MiModeloDinamico',
                    proxy:
                    {
                        extraParams:{
                            cdunieco :  inputCdunieco,
                            cdramo :    inputCdramo,
                            estado :    inputEstado,
                            nmpoliza :  inputNmpoliza
                        },
                        type:'ajax',
                        url:urlCargar,
                        reader:{
                            type:'json'
                        }
                    }
                });

                var loaderForm=Ext.ModelManager.getModel('LoaderForm');
                loaderForm.load(123, {
                    success: function(resp) {
                        //console.log(resp);
                        Ext.getCmp('formPanel').loadRecord(resp);
                        if(inputCdramo+'x'=='1x')
                        {
                            var cuadro20 = function()
                            {
                                if(!_fieldByName('panel2.nmcuadro').isValid())
                                {
                                    _fieldByName('panel2.nmcuadro').setValue('RE20');
                                    _fieldByName('panel2.nmcuadro').isValid();
                                    //alert('reemplazo');
                                }
                                else
                                {
                                    //alert('ya viene');
                                }
                            };
                            if(_fieldByName('panel2.nmcuadro').store.getCount()>0)
                            {
                                //alert('cargado');
                                cuadro20();
                            }
                            else
                            {
                                //alert('no cargado');
                                _fieldByName('panel2.nmcuadro').store.on(
                                {
                                    load : function()
                                    {
                                        cuadro20();
                                    }
                                });
                            }
                        }
                        panDatComAux1=panDatComAux1+1;
                        if(panDatComAux1==2&&inputCdramo+'x'=='6x')
                        {
                            pantallaValositParche();
                        }
                        else
                        {
                            debug('complementarios>todavia no se parcha tvalosit');
                        }
                    },
                    failure:function()
                    {
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Error',
                            icon: Ext.Msg.ERROR,
                            msg: 'Error al cargar',
                            buttons: Ext.Msg.OK
                        }));
                    }
                });
                /*//////////////////////////////////////////////////*/
                ////// Fin de cargador de formulario (sin grid) //////
                //////////////////////////////////////////////////////
                
                <s:if test='%{getActionErrors()!=null&&getActionErrors().size()>0}' >
                var items=[];
                <s:iterator value="actionErrors" var="iError">
                items.push(
                {
                    xtype : 'panel'
                    ,layout   : 'hbox'
                    ,border   : 0
                    ,defaults : { style : 'margin : 5px;' }
                    ,items    :
                    [
                        {
                            xtype   : 'image'
                            ,src    : '${ctx}/resources/fam3icons/icons/error.png'
                            ,width  : 16
                            ,height : 16
                        }
                        ,{
                            xtype  : 'label'
                            ,text  : '<s:property value="iError" escapeHtml="false" />'
                            ,style : 'color:red;margin:5px;'
                        }
                    ]
                });
                </s:iterator>
                centrarVentanaInterna(
                Ext.create('Ext.window.Window',
                {
                    title      : 'Aviso(s)'
                    ,width     : 600
                    ,height    : (items.length*40)+40
                    ,modal     : true
                    ,items     : items
                }).show());
                </s:if>
                
                //Ext.getCmp('formPanel').loadRecord(storeLoader.getAt(0));
            });
            
            function ventanaBusquedaEmpleado(){
            	try{
	                var ventana=Ext.create('Ext.window.Window',
	                        {
	                            title      : 'Recuperar empleado'
	                            ,modal     : true
	                            ,width     : 600
	                            ,height    : 400
	                            ,items     :
	                            [
	                                {
	                                     layout :
	                                     {
	                                         type     : 'table'
	                                         ,columns : 2
	                                     }
	                                    ,defaults : { style : 'margin : 5px;' }
	                                    ,items    :
	                                    [
	                                        {
	                                            xtype       : 'textfield'
	                                            ,name       : 'no_empleado'
	                                            ,fieldLabel : 'NO. EMPLEADO'
	                                            ,minLength  : 0
	                                            ,maxLength  : 30
	                                        }
	                                        ,
	                                        {
	                                            xtype       : 'textfield'
	                                            ,name       : 'rfc'
	                                            ,fieldLabel : 'RFC'
	                                            ,minLength  : 0
	                                            ,maxLength  : 13
	                                        }
	                                        ,
	                                        {
	                                            xtype       : 'textfield'
	                                            ,name       : 'ap_paterno'
	                                            ,fieldLabel : 'APELLIDO PATERNO'
	                                            ,minLength  : 0
	                                            ,maxLength  : 60
	                                        }
	                                        ,
	                                        {
	                                            xtype       : 'textfield'
	                                            ,name       : 'ap_materno'
	                                            ,fieldLabel : 'APELLIDO MATERNO'
	                                            ,minLength  : 0
	                                            ,maxLength  : 60
	                                        }
	                                        ,
	                                        {
	                                            xtype       : 'textfield'
	                                            ,name       : 'nombre'
	                                            ,fieldLabel : 'NOMBRE EMPLEADO'
	                                            ,minLength  : 0
	                                            ,maxLength  : 60
	                                        }
	                                        ,{
	                                            xtype    : 'button'
	                                            ,text    : 'Buscar'
	                                            ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
	                                            ,handler : function(button)
	                                            {
	                                                
                                                   try{ 
	                                                    buscarEmpleado(
	                                                            _fieldByLabel('ADMINISTRADORA').getValue()
	                                                            ,_fieldByLabel('RETENEDORA').getValue()
	                                                            ,_fieldByName('no_empleado').getValue()
	                                                            ,_fieldByName('rfc').getValue()
	                                                            ,_fieldByName('ap_paterno').getValue()
	                                                            ,_fieldByName('ap_materno').getValue()
	                                                            ,_fieldByName('nombre').getValue()
	                                                            );
                                                   }catch(e){
                                                       debugError(e);
                                                   }
	                                            }
	                                        }
	                                    ]
	                                }
	                                ,Ext.create('Ext.grid.Panel',
	                                {
	                                    title    : 'Resultados'
	                                    ,name    : 'gridBuscaEmpleado'
	                                    ,width   : 590
	                                    ,height    : 220
	                                    ,columns :
	                                    [
	                                        {
	                                            xtype    : 'actioncolumn'
	                                            ,width   : 30
	                                            ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
	                                            ,handler : function(view,row,col,item,e,record)
	                                            {
	                                                debug('recuperar cliente handler record:',record);
	//                                              _p28_recordClienteRecuperado=record;
	//                                              nombre.setValue(record.raw.NOMBRECLI);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CLAVE EMPLEADO"]')[0].setValue(record.raw.clave_empleado);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="NOMBRE EMPLEADO"]')[0].setValue(record.raw.nombre);;
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO PATERNO"]')[0].setValue(record.raw.apellido_p);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO MATERNO"]')[0].setValue(record.raw.apellido_m);
	                                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="RFC EMPLEADO"]')[0].setValue(record.raw.rfc);
	                                                                if(Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]').length>0){
	                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]')[0].setValue(record.raw.curp);
	                                                                }
	                                                               

                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CLAVE EMPLEADO"]')[0].setReadOnly(true);
                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="NOMBRE EMPLEADO"]')[0].setReadOnly(true);
                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO PATERNO"]')[0].setReadOnly(true);
                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO MATERNO"]')[0].setReadOnly(true);
                                                                    Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="RFC EMPLEADO"]')[0].setReadOnly(true);
                                                                    if(Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]').length>0){
                                                                        Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]')[0].setReadOnly(true);
                                                                    }
	                                                
	                                                ventana.destroy();
	                                            }
	                                        }
	                                        ,{
	                                            text       : 'CLAVE EMPLEADO'
	                                            ,dataIndex : 'clave_empleado'
	                                            //,width     : 200
	                                        }
	                                        ,{
	                                            text       : 'NOMBRE'
	                                            ,dataIndex : 'nombre'
	                                            ,flex      : 1
	                                        }
	                                        ,{
	                                            text       : 'APELLIDO PATERNO'
	                                            ,dataIndex : 'apellido_p'
	                                            ,flex      : 1
	                                        }
	                                        ,{
	                                            text       : 'APELLIDO MATERNO'
	                                            ,dataIndex : 'apellido_m'
	                                            ,flex      : 1
	                                        }
	                                        ,{
	                                            text       : 'RFC'
	                                            ,dataIndex : 'rfc'
	                                            ,flex      : 1
	                                        }
	                                    ]
	                                    ,store :Ext.create('Ext.data.Store', {
	                                        fields: ['clave_empleado', 'nombre','apellido_p','apellido_m','rfc'],
	                                        data : []
	                                    })
	                                        
	                                        
	                                })
	                            ]
	                            ,listeners :
	                            {
	                                close : function()
	                                {
	                                    //combcl.setValue('S');
	                                }
	                            }
	                        }).show();
	                        centrarVentanaInterna(ventana);
            	}catch(e){
            	    debugError(e);
            	}
            }
            
            
            
            function guardaEmpleado(){
               
                try{
                    ck="preparando datos para enviar";
                    
                    
                    
                    
                    
                    $.ajax({
                        async:false, 
                        cache:false,
                        dataType:"json", 
                        type: 'POST',   
                        url: url_guarda_empleado ,
                        data: {
                            'params.administradora':_fieldByLabel('ADMINISTRADORA').getValue(),
                            'params.retenedora':_fieldByLabel('RETENEDORA').getValue(),
                            'params.clave':_fieldByLabel('CLAVE EMPLEADO').getValue(),
                            'params.nombre':_fieldByLabel('NOMBRE EMPLEADO').getValue(),
                            'params.apaterno':_fieldByLabel('APELLIDO PATERNO').getValue(),
                            'params.amaterno':_fieldByLabel('APELLIDO MATERNO').getValue(),
                            'params.rfc':_fieldByLabel('RFC EMPLEADO').getValue(),
                            'params.clavedescuento':_fieldByLabel('CLAVE DESCUENTO').getValue(),
                            'params.curp':Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor16"]').length==0?'':Ext.ComponentQuery.query('#panelDatosAdicionales [name="parametros.pv_otvalor16"]')[0].getValue()
                           
                        }, 
                        success:  function(respuesta){
                            var ck = 'Decodificando respuesta al recuperar datos para guardar empleado';
                            
                         try
                         {
                             var jsonDatTram =  respuesta// Ext.decode(response.responseText);
                             debug('### jsonDatTram:',jsonDatTram,'.');
                             
                             if(jsonDatTram.success === true)
                             {
                              
                                 //alert("sd")
                                
                             }
                             else
                             {
                                 mensajeError(jsonDatTram.message);
                             }
                         }
                         catch(e)
                         {
                             debugError(e);
                         }
                         
                        },
                        beforeSend:function(){},
                        error:function(objXMLHttpRequest){
                            
                              errorComunicacion(null,'Error al recuperar datos de tr\u00e1mite para revisar renovaci\u00f3n');
                        }
                      });
                    
                }catch(e){
                    debugError(e);
               }
                
            }
            function claveDescuentoDxn(administradora,retenedora,cdramo,cdtipsit){
                try{
                    _fieldByLabel('CLAVE DESCUENTO').store.proxy.extraParams['catalogo']='CLAVE_DESCUENTO_SUBRAMO'
                    _fieldByLabel('CLAVE DESCUENTO').store.proxy.extraParams['params.administradora']=administradora;
                    _fieldByLabel('CLAVE DESCUENTO').store.proxy.extraParams['params.retenedora']=retenedora;
                    _fieldByLabel('CLAVE DESCUENTO').store.proxy.extraParams['params.cdramo']=cdramo;
                    _fieldByLabel('CLAVE DESCUENTO').store.proxy.extraParams['params.cdtipsit']=cdtipsit;
                    _fieldByLabel('RETENEDORA').focus();
                    
                    _fieldByLabel('CLAVE DESCUENTO').getStore().load({
                        callback:function(){
                            
                        }
                    });
                    
                    
                    
                }catch(e){
                    debugError(e)
                }
            }
            
            function buscarEmpleado(administradora,retenedora,ce,rfc,ap,am,nom){
                try{
	                debug('recuperar empleado buscar');
	                
	                _fieldById('panelDatosAdicionales').setLoading(true);
	             
	                Ext.Ajax.request(
                        {
                             url     : url_buscar_empleado 
                            ,params :
                            {
                                'params.administradora'   : administradora
                                ,'params.retenedora'       : retenedora
                                ,'params.clave_empleado'   : ce
                                ,'params.rfc'              : rfc==undefined?"":rfc
                                ,'params.ap_paterno'       : ap==undefined?"":ap
                                ,'params.ap_materno'       : am==undefined?"":am
                                ,'params.nombre'           : nom==undefined?"":nom
                            }
                            ,success : function(response)
                            {
                                _fieldById('panelDatosAdicionales').setLoading(false);
                                var json = Ext.decode(response.responseText);
                                debug('### Response Boton Comprar:',json);
                                if(json.success ==  true)
                                {
                                    var ck = 'Decodificando respuesta al  buscar empleado';
                                    try
                                    {
                                        //alert(json);
                                        debug("### respuesta ",json);
                                        if(json.slist1.length>0){
                                            if(Ext.ComponentQuery.query("[name=gridBuscaEmpleado]").length==0){
                                                ventanaBusquedaEmpleado()
                                            }
                                            _fieldByName("gridBuscaEmpleado").getStore().loadData(json.slist1);
                                            
                                            
                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CLAVE EMPLEADO"]')[0].setReadOnly(true);
                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="NOMBRE EMPLEADO"]')[0].setReadOnly(true);
                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO PATERNO"]')[0].setReadOnly(true);
                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="APELLIDO MATERNO"]')[0].setReadOnly(true);
                                            Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="RFC EMPLEADO"]')[0].setReadOnly(true);
                                            if(Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]').length>0){
                                                Ext.ComponentQuery.query('#panelDatosAdicionales [fieldLabel="CURP"]')[0].setReadOnly(true);
                                            }
                                        }else{
                                            if(Ext.ComponentQuery.query("[name=gridBuscaEmpleado]").length>0){
                                                _fieldByName("gridBuscaEmpleado").getStore().removeAll();
                                            }
                                        }
                                        
                                        
                                    }
                                    catch(e)
                                    {
                                        manejaException(e,ck);
                                    }                    
                                }
                                else
                                {
                                    mensajeError(json.respuesta);
                                }
                            }
                            ,failure : function()
                            {
                                _fieldById('panelDatosAdicionales').setLoading(false);
                                errorComunicacion();
                            }
                        });
                }catch(e){
                    debugError(e);
                }
            }

        <%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
        </script>
</head>
<body>
	<div id="maindiv" style="height: 1500px;"></div>
	<%--////////////////////////////////////
        ////// para el parser de archivos //////
        ////////////////////////////////////--%
        <script>Ext.onReady(afterExtReady);</script>
        <%--////////////////////////////////--%>
	<!--// para el parser de archivos //////
        /////////////////////////////////////-->
</body>
</html>