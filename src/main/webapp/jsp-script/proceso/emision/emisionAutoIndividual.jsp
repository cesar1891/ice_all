<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _p29_urlPantallaCliente                = '<s:url namespace="/catalogos"  action="includes/personasLoader"              />';
var _p29_urlCotizacionAutoIndividual       = '<s:url namespace="/emision"    action="cotizacionAutoIndividual"             />';
var _p29_urlCargarDatosComplementarios     = '<s:url namespace="/emision"    action="cargarDatosComplementariosAutoInd"    />';
var _p29_urlCargarTvalosit                 = '<s:url namespace="/emision"    action="cargarValoresSituacion"               />';
var _p29_urlCargarRetroactividadSuplemento = '<s:url namespace="/emision"    action="cargarRetroactividadSuplemento"       />';
var _p29_urlMovimientoMpoliper             = '<s:url namespace="/emision"    action="movimientoMpoliper"                   />';
var _p29_urlGuardar                        = '<s:url namespace="/emision"    action="guardarComplementariosAutoIndividual" />';
var _p29_urlRecotizar                      = '<s:url namespace="/"           action="recotizar"                            />';
var _p29_urlEmitir                         = '<s:url namespace="/"           action="emitir"                               />';
var _p29_urlDocumentosPoliza               = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza"              />';
var _p29_urlRecuperacionSimple             = '<s:url namespace="/emision"    action="recuperacionSimple"                   />';
var _p29_urlCotizacionAutoIndividual       = '<s:url namespace="/emision"    action="cotizacionAutoIndividual"             />';
var _p29_urlDocumentosPolizaClon           = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon"          />';
var _p29_urlObtieneValNumeroSerie          = '<s:url namespace="/emision"    action="obtieneValNumeroSerie"                />';
var urlPantallaBeneficiarios               = '<s:url namespace="/catalogos"  action="includes/pantallaBeneficiarios"       />';
var _p28_urlCargarIdUsu                    = '<s:url namespace="/emision"    action="obtieneIdUsu"        />';
var urlReintentarWS                        = '<s:url namespace="/"        action="reintentaWSautos" />';
var _urlEnviarCorreo                       = '<s:url namespace="/general" action="enviaCorreo"      />';
var _p29_urlRecuperacion                   = '<s:url namespace="/recuperacion"    action="recuperar"                       />';

var url_buscar_empleado                     = '<s:url namespace="/emision"              action="buscarEmpleados"                       />';
var urlCargar                               = '<s:url namespace="/"                     action="cargarDatosComplementarios"  />';
var url_guarda_empleado                     = '<s:url namespace="/emision"                action="guardaEmpleados"  />';
var url_admin_ret                           = '<s:url namespace="/emision"                action="obtieneAdminRet"  />';
var _p29_urlRangoPeriodogracia              = '<s:url namespace="/emision"   action="obtieneRangoPeriodoGraciaAgente"              />';


    


////// urls //////

////// variables //////
var claveUsuarioCaptura = '<s:property value="%{#session['USUARIO'].claveUsuarioCaptura}"/>';
debug("claveUsuarioCaptura= ",claveUsuarioCaptura,".");
var _p29_smap1          = <s:property value="%{convertToJSON('smap1')}" escapeHtml="false"/>;
debug('_p29_smap1:',_p29_smap1);

var _p29_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_p29_flujo:',_p29_flujo);

var _p29_polizaLicenciaTurista  = null
var _p29_polizaAdicionalesItems = null;
var _p29_adicionalesItems       = null;
var _p22_parentCallback         = false;
var _p22_parentCallbackCallback = null;
var _p32_parentCallback         = false;
var _p32_parentCallbackCallback = null;
var _p29_ventanaDocs            = null;
var _p29_validaSeguro           = "N";

var _SWexiper = _p29_smap1.swexiper;

var _paramsRetryWS;
var _mensajeEmail;

var _aplicaCobVida = !Ext.isEmpty(_p29_smap1.AplicaCobVida) && _p29_smap1.AplicaCobVida == "S" ? true : false ;
var _FechaMinEdad  = _p29_smap1.FechaMinEdad;
var _FechaMaxEdad  = _p29_smap1.FechaMaxEdad;

var ramogs;
var poliza;
var _p29_habilitarBotonEmitir  = "S";

var _url_domiciliacion = '<s:text name="portal.agentes.domiciliacion.url" />';
var _URL_IDUSULOGIN = '<s:property value="sigsObtenerDatosPorSucRamPolUrl" />';

var panelDxnItems=[<s:property value="imap.panelDxnItems" />];

var esDXN=false;

////// variables //////

Ext.onReady(function()
{   
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 485000; // 8 min
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
    ////// modelos //////
    Ext.define('_p29_polizaModelo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {
                type        : 'date'
                ,dateFormat : 'd/m/Y'
                ,name       : 'feini'
            }
            ,{
                type        : 'date'
                ,dateFormat : 'd/m/Y'
                ,name       : 'fefin'
            }
            ,<s:property value="imap.polizaFields" />
            ,<s:property value="imap.agenteFields" />
            <s:if test='%{getImap().get("polizaAdicionalesFields")!=null}'>
                ,<s:property value="imap.polizaAdicionalesFields" />
            </s:if>
        ]
    });
    
    Ext.define('ModeloDetalleCotizacion',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {name  : 'Codigo_Garantia'}
            ,{name : 'Importe',type : 'float'}
            ,{name : 'Nombre_garantia'}
            ,{name : 'cdtipcon'}
            ,{name : 'nmsituac'}
            ,{name : 'orden'}
            ,{name : 'parentesco'}
            ,{name : 'orden_parentesco'}
        ]
    });
    ////// modelos //////
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    _p29_polizaAdicionalesItems =
    [
        <s:property value="imap.agenteItems" />
        ,{
            xtype       : 'datefield'
            ,format     : 'd/m/Y'
            ,fieldLabel : 'INICIO DE VIGENCIA'
            ,name       : 'feini'
            ,listeners  : { change : _p29_fefinChange }
            ,style      : 'margin:5px;'
        }
        ,{
            xtype       : 'datefield'
            ,format     : 'd/m/Y'
            ,fieldLabel : 'FIN DE VIGENCIA'
            ,name       : 'fefin'
            ,readOnly   : true
            ,style      : 'margin:5px;'
        }
    ];
    <s:if test='%{getImap().get("polizaAdicionalesItems")!=null}'>
        var aux=[<s:property value="imap.polizaAdicionalesItems" />];
        for(var i=0;i<_p29_polizaAdicionalesItems.length;i++)
        {
            aux.push(_p29_polizaAdicionalesItems[i]);
        }
        _p29_polizaAdicionalesItems = aux;
    </s:if>
    <s:if test='%{getImap().get("customRamo6")!=null}'>
	    _p29_polizaAdicionalesItems.push(<s:property value="imap.customRamo6" />);
    </s:if>
    for(var i=0;i<_p29_polizaAdicionalesItems.length;i++)
    {
        _p29_polizaAdicionalesItems[i].labelWidth=295;
    }
    
    _p29_adicionalesItems = [];
    <s:if test='%{getImap().get("adicionalesItems")!=null}'>
        _p29_adicionalesItems = [<s:property value="imap.adicionalesItems" />];
    </s:if>
    
    var _p29_datosGeneralesItems = [<s:property value="imap.polizaItems" />];
    	
    for(var i=0;i<_p29_datosGeneralesItems.length;i++)
    {
        _p29_datosGeneralesItems[i].labelWidth=295;
    }
    ////// componentes //////
    
    ////// contenido //////
    
    
    
    panelDxnItems.splice(0, 0,
            
            {
                  xtype:'button',
                  text:'Buscar Empleado',
                  icon    : '${ctx}/resources/fam3icons/icons/zoom.png',
                  handler:function(){
                      ventanaBusquedaEmpleado();
                  }
              },
              {
                  xtype:'button',
                  text:'Limpiar Campos',
                  icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png',
                  handler:function(){
                      _fieldByName("aux.otvalor10").setReadOnly(false);
                      _fieldByName("aux.otvalor11").setReadOnly(false);
                      _fieldByName("aux.otvalor12").setReadOnly(false);
                      _fieldByName("aux.otvalor13").setReadOnly(false);
                      _fieldByName("aux.otvalor14").setReadOnly(false);
                      _fieldByName("aux.otvalor16").setReadOnly(false);
                      
                      _fieldByName("aux.otvalor10").setValue('');
                      _fieldByName("aux.otvalor11").setValue('');
                      _fieldByName("aux.otvalor12").setValue('');
                      _fieldByName("aux.otvalor13").setValue('');
                      _fieldByName("aux.otvalor14").setValue('');
                      _fieldByName("aux.otvalor16").setValue('');
                  }
              }
              
            
        
         );
    
    
    
    
    
    Ext.create('Ext.panel.Panel',
    {
        itemId    : '_p29_panelpri'
        ,renderTo : '_p29_divpri'
        ,title    : _p29_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto?'Emisi&oacute;n Servicio P&uacute;blico Auto':_p29_smap1.cdtipsit==TipoSituacion.ServicioPublicoMicro?'Emisi&oacute;n Servicio P&uacute;blico Microbus':null
        ,border   : 0
        ,defaults : { style : 'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p29_panelFlujo'
                ,title       : 'ACCIONES'
                ,hidden      : Ext.isEmpty(_p29_flujo)
                ,buttonAlign : 'left'
                ,buttons     : []
                ,listeners   :
                {
                    afterrender : function(me)
                    {
                        if(!Ext.isEmpty(_p29_flujo))
                        {
                            _cargarBotonesEntidad(
                                _p29_flujo.cdtipflu
                                ,_p29_flujo.cdflujomc
                                ,_p29_flujo.tipoent
                                ,_p29_flujo.claveent
                                ,_p29_flujo.webid
                                ,me.itemId//callback
                                ,_p29_flujo.ntramite
                                ,_p29_flujo.status
                                ,_p29_flujo.cdunieco
                                ,_p29_flujo.cdramo
                                ,_p29_flujo.estado
                                ,_p29_flujo.nmpoliza
                                ,_p29_flujo.nmsituac
                                ,_p29_flujo.nmsuplem
                                ,null//callbackDespuesProceso
                            );
                        }
                    }
                }
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p29_polizaForm'
                ,title    : 'DATOS DE P&Oacute;LIZA'
                ,defaults : { style : 'margin:5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                    ,tdAttrs : { valign : 'top' }
                }
                ,items    :
                [
                    {
                        xtype  : 'fieldset'
                        ,title : '<span style="font:bold 14px Calibri;">DATOS GENERALES</span>'
                        ,items : _p29_datosGeneralesItems
                    }
                    ,{
                        xtype  : 'fieldset'
                        ,title : '<span style="font:bold 14px Calibri;">DATOS ADICIONALES</span>'
                        ,items : _p29_polizaAdicionalesItems
                    }
                ]
            })
            ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p29_adicionalesForm'
                ,title    : 'DATOS ADICIONALES DE INCISO'
                ,defaults :
                {
                    style : 'margin:5px;margin-right:160px;'
                }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items    : _p29_adicionalesItems
            })
            ,Ext.create('Ext.panel.Panel',
            {
                itemId      : '_p29_clientePanel'
                ,title      : 'CLIENTE'
                ,height     : 400
                ,autoScroll : true
                ,loader     :
                {
                    url       : _p29_urlPantallaCliente
                    ,scripts  : true
                    ,autoLoad : false
                    ,ajaxOptions: {
                                    method: 'POST'
                             }
                }
            })
            ,Ext.create('Ext.panel.Panel',
            {
                itemId      : '_p29_BeneficiarioPanel'
                ,height     : 300
                ,autoScroll : true
                ,hidden     : true
                ,loader:
                {
                    url : urlPantallaBeneficiarios
                    ,params   :
                    {
                        'smap1.cdunieco'      : _p29_smap1.cdunieco
                        ,'smap1.cdramo'       : _p29_smap1.cdramo
                        ,'smap1.estado'       : _p29_smap1.estado
                        ,'smap1.nmpoliza'     : _p29_smap1.nmpoliza
                        ,'smap1.nmsuplem'     : '1'
                        ,'smap1.nmsituac'     : '0'
                        ,'smap1.cdrolPipes'   : '3'
                        ,'smap1.cdtipsup'     : '1'
                        ,'smap1.ultimaImagen' : 'N'
                    }
                    ,autoLoad:true
                    ,scripts:true
                }
            })
            
             ,Ext.create('Ext.form.Panel',
            {
                itemId    : '_p29_dxnForm'
                ,name     : '_p29_dxnForm'
                ,title    : 'DESCUENTO POR N&Oacute;MINA'
                ,autoScroll : true
                ,defaults : { style : 'margin:5px;' }
                ,layout   :
                {
                    type     : 'table'
                    ,columns : 2
                    ,tdAttrs : { valign : 'top' }
                }
                ,items    :
                
                    panelDxnItems
                    
                ,listeners: {
                    
                    beforerender:function(me){
                        
                        _mask("cargando datos");
                        Ext.Ajax.request(
                                {
                                   
                                     url     : urlCargar 
                                    ,params :
                                    {
                                        cdunieco :  _p29_smap1.cdunieco,
                                        cdramo :    _p29_smap1.cdramo,
                                        estado :    _p29_smap1.estado,
                                        nmpoliza :  _p29_smap1.nmpoliza
                                    }
                                    ,success : function(response)
                                    {
                                        _unmask();
                                        var json = Ext.decode(response.responseText);
                                        debug('### Response :',json);
                                        if(json.success ==  true)
                                        {
                                            var ck = 'Decodificando respuesta ';
                                            try
                                            {
                                                ck="cargando datos";
                                                
                                              
                                                if(
                                                           json.parametros.pv_otvalor08!=null 
                                                        && (json.parametros.pv_otvalor08+'').trim()!=''
                                                        && json.parametros.pv_otvalor08!='-1'
                                                        && _p29_smap1.cdramo!=Ramo.ServicioPublico
                                                  ){
                                                    
                                                        esDXN=true;
                                                        _fieldByName("aux.otvalor08").setValue(json.parametros.pv_otvalor08);
                                                        _fieldByName('aux.otvalor09').heredar(true, function(){
                                                        _fieldByName('aux.otvalor09').setValue(json.parametros.pv_otvalor09);
                                                        claveDescuentoDxn(_fieldByName("aux.otvalor08").getValue(),
                                                                          _fieldByName("aux.otvalor09").getValue(),
                                                                          _p29_smap1.cdramo,
                                                                          _p29_smap1.cdtipsit)
                                                        _fieldByName('aux.otvalor15').heredar(true, function(){
                                                            
                                                            _fieldByName('aux.otvalor15').setValue(json.parametros.pv_otvalor15);
                                                          
                                                        });
                                                    });
                                                    
                                                    // EVENTO PARA VERIFICAR SI YA EXISTE EL EMPLEADO
                                                    
                                                    _fieldByName("aux.otvalor10").on(
                                                            {
                                                                'blur':function(){
                                                                    
                                                                    buscarEmpleado(
                                                                            _fieldByName("aux.otvalor08").getValue()
                                                                            ,_fieldByName("aux.otvalor09").getValue()
                                                                            ,_fieldByName('aux.otvalor10').getValue()
                                                                            );
                                                                }
                                                            }        
                                                    );
                                                    
                                                    
                                                    _fieldByName("aux.otvalor10").setValue(json.parametros.pv_otvalor10==null?"":json.parametros.pv_otvalor10);
                                                    _fieldByName("aux.otvalor11").setValue(json.parametros.pv_otvalor11==null?"":json.parametros.pv_otvalor11);
                                                    _fieldByName("aux.otvalor12").setValue(json.parametros.pv_otvalor12==null?"":json.parametros.pv_otvalor12);
                                                    _fieldByName("aux.otvalor13").setValue(json.parametros.pv_otvalor13==null?"":json.parametros.pv_otvalor13);
                                                    _fieldByName("aux.otvalor14").setValue(json.parametros.pv_otvalor14==null?"":json.parametros.pv_otvalor14);
                                                    _fieldByName("aux.otvalor15").setValue(json.parametros.pv_otvalor15==null?"":json.parametros.pv_otvalor15);
                                                    
                                                    _fieldByName("aux.otvalor16").setValue(json.parametros.pv_otvalor16==null?"":json.parametros.pv_otvalor16);
                                                   
                                                    _fieldByName("aux.otvalor08").setReadOnly(true);
                                                    _fieldByName("aux.otvalor09").setReadOnly(true);
                                                    _fieldByName("aux.otvalor09").focus();
                                                    
                                                    Ext.ComponentQuery.query('#_p29_dxnForm [xtype=textfield]').forEach(function(item,idx,arr){
                                                        
                                                        item.on({
                                                            change:function(){
                                                                try
                                                                {
                                                                    if('string' == typeof item.getValue() && true !== item.sinmayus)
                                                                    {
                                                                        //debug('mayus de '+this.getValue());
                                                                        item.setValue(item.getValue().toUpperCase());
                                                                    }
                                                                }
                                                                catch(e){}
                                                            }
                                                        })
                                                    });
                                                    
                                                    
                                                    _mask("cargando datos");
                                                    Ext.Ajax.request(
                                                            {
                                                               
                                                                 url     : url_admin_ret 
                                                                ,params :
                                                                {
                                                                    'smap1.administradora' :  json.parametros.pv_otvalor08,
                                                                    'smap1.retenedora' :    json.parametros.pv_otvalor09
                                                                }
                                                                ,success : function(response)
                                                                {
                                                                    _unmask();
                                                                    var json2 = Ext.decode(response.responseText);
                                                                    debug('### Response --- :',json2);
                                                                    
                                                                    json2.slist1.forEach(function(it,idx,arr){
                                                                      for(var i=17;i<22;i++)  {
                                                                          
                                                                        if((it['OTVALOR'+(i+6)]+'').trim()!='' && it['OTVALOR'+(i+6)]!=undefined && it['OTVALOR'+(i+6)]!=null){
                                                                            
                                                                            _fieldByName('_p29_dxnForm').add({
                                                                                xtype      :'textfield',
                                                                                fieldLabel :it['OTVALOR'+(i+6)],
                                                                                name       :'aux.otvalor'+i
                                                                            });
                                                                            _fieldByName("aux.otvalor"+i).setValue(json.parametros['pv_otvalor'+i]==null?"":json['parametros.pv_otvalor'+i]);
                                                                            
                                                                           
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
                                
                                                    
                                                    
                                                    
                                                }else{
                                                    esDXN=false;
                                                    me.destroy();
                                                }
                                                
                                                
                                                json.parametros.pv_otvalor08
                                                
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
                                        _unmask();
                                        errorComunicacion();
                                    }
                                });
                    }
                }
                    
                 
            })
            ,Ext.create('Ext.panel.Panel',
            {
                itemId       : '_p29_panelBotones'
                ,border      : 0
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        itemId   : '_p29_botonEmitir'
                        ,text    : 'Emitir'//vils
                        ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                        ,handler :  _p29_emitirClic
                        ,hidden  :  (_p29_habilitarBotonEmitir == 'N')
                    }
                    ,{
                        itemId   : '_p29_botonGuardar'
                        ,text    : 'Guardar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                        ,handler : function(){ _p29_guardar(); }
                    }
                    ,{
                        text     : 'Nueva'
                        ,icon    : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                        ,hidden  : !Ext.isEmpty(_p29_flujo)
                        ,handler : _p29_nuevaClic
                    }
                ]
            })
        ]
    });
    
    _p29_ventanaDocs=Ext.create('Ext.window.Window',
    {
        title           : 'Documentaci&oacute;n'
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
            ,url      : _p29_urlDocumentosPolizaClon
            ,params   :
            {
                'smap1.cdunieco'       : _p29_smap1.cdunieco
                ,'smap1.cdramo'        : _p29_smap1.cdramo
                ,'smap1.estado'        : _p29_smap1.estado
                ,'smap1.nmpoliza'      : ''
                ,'smap1.nmsuplem'      : '0'
                ,'smap1.nmsolici'      : ''
                ,'smap1.ntramite'      : _p29_smap1.ntramite
                ,'smap1.tipomov'       : '0'
                ,'smap1.ocultarRecibo' : ''
            }
        }
    }).showAt(500,0);
    _p29_ventanaDocs.collapse();
    ////// contenido //////
    
    ////// custom //////
    _fieldByLabel('AGENTE').hide();
    _fieldByName('porparti').setMaxValue(99);
    
    if(_p29_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto){
	  //PERMITIMOS EDICION IGUAL QUE EN AF
	    Ext.ComponentQuery
	    .query('[fieldLabel="NUMERO DE SERIE"]')
	    .forEach(function(it,idx){
	        if(_fieldByLabel('TIPO DE UNIDAD').getValue()==TipoUnidad.Fronterizo)
	            it.setReadOnly(!RolSistema.puedeSuscribirAutos(_0_smap1.cdsisrol));
	            it.fireEvent('blur');
	    });
    }
    //codigo dinamico recuperado de la base de datos
    <s:property value="smap1.customCode" escapeHtml="false" />
    
    
    ////// custom //////
    
    ////// loaders //////
    
    bloquearBotonEmitir();
    
    Ext.Ajax.request(
    {
        url     : _p29_urlCargarDatosComplementarios
        ,params :
        {
            'smap1.cdunieco'  : _p29_smap1.cdunieco
            ,'smap1.cdramo'   : _p29_smap1.cdramo
            ,'smap1.estado'   : _p29_smap1.estado
            ,'smap1.nmpoliza' : _p29_smap1.nmpoliza
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### cargar datos complementarios:',json);
            if(json.exito)
            {
                var form   = _fieldById('_p29_polizaForm');
                var record = new _p29_polizaModelo(json.smap1);
                debug('record:',record);
                form.loadRecord(record);
                
                if(json.smap1.agente_sec+'x'!='x')
                {
                    var age2 = _fieldByName('agente_sec');
                    age2.getStore().load(
                    {
                        params :
                        {
                            'params.agente' : json.smap1.agente_sec
                        }
                        ,callback : function()
                        {
                            age2.setValue(age2.findRecord('key',json.smap1.agente_sec));
                        }
                    });
                }
                
                _fieldById('_p29_clientePanel').loader.load(
                {
                    params:
                    {
                        'smap1.cdperson' : json.smap1.cdperson,
                        'smap1.cdideper' : json.smap1.cdideper,
                        'smap1.cdideext' : json.smap1.cdideext,
                        'smap1.esSaludDanios' : 'D',
                        'smap1.polizaEnEmision': 'S',
                        'smap1.esCargaClienteNvo' :(Ext.isEmpty(json.smap1.cdperson)? 'S' : 'N' ),
                        'smap1.cargaCP' : json.smap1.cdpostal,
                        'smap1.cargaTipoPersona' : json.smap1.otfisjur,
                        'smap1.cargaSucursalEmi' : _p29_smap1.cdunieco,
                        'smap1.cargaFenacMin' : _aplicaCobVida?_FechaMinEdad:'',
                        'smap1.cargaFenacMax' : _aplicaCobVida?_FechaMaxEdad:'',
                        'smap1.tomarUnDomicilio' : 'S',
                        'smap1.cargaOrdDomicilio' : json.smap1.nmorddom,
                        'smap1.muestraBusqueda' : _p29_smap1.cdramo == Ramo.ServicioPublico || (_p29_smap1.cdtipsit == TipoSituacion.TuristaLicencia || _p29_smap1.cdtipsit == TipoSituacion.TuristaVehiculo)?'S':'N' // forzamos a que aparezca el campo para buscar personas
                    }
                });
                
                _p22_parentCallback = _p29_personaSaved;
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    
    Ext.Ajax.request(
    {
        url     : _p29_urlCargarTvalosit
        ,params :
        {
            'smap1.cdunieco'  : _p29_smap1.cdunieco
            ,'smap1.cdramo'   : _p29_smap1.cdramo
            ,'smap1.estado'   : _p29_smap1.estado
            ,'smap1.nmpoliza' : _p29_smap1.nmpoliza
            ,'smap1.nmsituac' : '1'
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### tvalosit:',json);
            if(json.exito)
            {
                var form = _fieldById('_p29_adicionalesForm');
                for(var i in json.smap1)
                {
                    var item = _fieldByName(i,form,true);
                    if(item)
                    {
                        item.setValue(json.smap1[i]);
                        if(_p29_smap1.cdramo+'x'=='5x')
                        {
                            if(item.fieldLabel=='CONDUCTOR'&&Ext.isEmpty(json.smap1[i]))
                            {
                                item.setValue(json.smap1['parametros.pv_otvalor15']);
                            }
                            
                            _p29_validaSeguro = json.smap1['parametros.pv_seguroVida'];
                            if(_p29_validaSeguro =="S"){
                                _fieldById('_p29_BeneficiarioPanel').show();
                            }else{
                                _fieldById('_p29_BeneficiarioPanel').hide();
                            }
                        }
                    }
                }

                _p29_loadCallback();
                
                if(_p29_smap1.cdramo==Ramo.ServicioPublico)
                {
                	cargaComentariosRamo6();
                }
              //RANGO PERIODO DE GRACIA
        	    rangoPeriodoGracia()
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    ////// loaders //////
   
    
    if(_p29_smap1.cdtipsit==TipoSituacion.ServicioPublicoAuto){
        var folio  = _fieldByName('parametros.pv_otvalor35');
    }else if(_p29_smap1.cdtipsit==TipoSituacion.ServicioPublicoMicro){
        var folio  = _fieldByName('parametros.pv_otvalor33');
    }else{
        var folio  = _fieldByName('parametros.pv_otvalor37',null,true);
    }
        
	   
	    debug("Valor del Folio --->",folio);
	    if(_p29_smap1.cdtipsit!='TL')
		    folio.on(
		    {
		        'change' : function(comp,val)
		        {
		            debug('folio change val:',val,'dummy');
		        }
		        ,'blur' : function()
		        {
		            debug("Valor 1 -->",!Ext.isEmpty(folio.getValue()));
		            if(!Ext.isEmpty(folio.getValue())){
		                Ext.Ajax.request(
		                {
		                    url     : _p29_urlObtieneValNumeroSerie
		                    ,params :
		                    {
		                        'smap1.numSerie'  : folio.getValue()
		                        ,'smap1.feini'   : _fieldByName('feini').getValue()
		                    }
		                    ,success : function(response)
		                    {
		                        var json=Ext.decode(response.responseText);
		                        if(json.exito!=true)
		                        {
		                            if(!RolSistema.puedeSuscribirAutos(_p29_smap1.cdsisrol))
		                            {
		                                mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", json.respuesta);
		                                _fieldById('_p29_botonEmitir').setDisabled(true);//Deshabilita el boton
		                            }else{
		                                mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", json.respuesta);
		                                _fieldById('_p29_botonEmitir').setDisabled(false);
		                            }
		                        }else{
		                            _fieldById('_p29_botonEmitir').setDisabled(false);
		                        }
		                    }
		                    ,failure : errorComunicacion
		                }); 
		            }else{
		                mensajeError("No se recibio el número de serie");
		            }
		        }
		    });
  
});

////// funciones //////
function _p29_fefinChange(me,newVal,oldVal)
{
    debug('>_p29_fefinChange:',newVal,oldVal,'DUMMY');
    debug('<_p29_fefinChange');
}

function _p29_nuevaClic()
{
    _fieldById('_p29_panelpri').setLoading(true);
    Ext.create('Ext.form.Panel').submit(
    {
        url     : _p29_urlCotizacionAutoIndividual
        ,params :
        {
            'smap1.cdramo'    : _p29_smap1.cdramo
            ,'smap1.cdtipsit' : _p29_smap1.cdtipsit
        }
        ,standardSubmit : true
    });
}

function _p29_loadCallback()
{
    var vigen = _fieldByLabel('VIGENCIA');
    vigen.hide();
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    feini.on(
    {
        change : function(me,val)
        {
            try
            {
//              debug('### obtener retroactividad:',json);
                fefin.setValue(Ext.Date.add(val,Ext.Date.DAY,vigen.getValue()))
            }
            catch(e)
            {
                debug(e);
            }
        }
    });
    
    Ext.Ajax.request(
    {
        url     : _p29_urlCargarRetroactividadSuplemento
        ,params :
        {
            'smap1.cdunieco'  : _p29_smap1.cdunieco
            ,'smap1.cdramo'   : _p29_smap1.cdramo
            ,'smap1.cdtipsup' : 1
            ,'smap1.cdusuari' : _p29_smap1.cdusuari
            ,'smap1.cdtipsit' : _p29_smap1.cdtipsit
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### obtener retroactividad:',json);
            if(json.exito)
            {
                feini.setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,(json.smap1.retroac-0)*-1));
                feini.setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi-0));
                feini.isValid();
                if(Ext.ComponentQuery.query("[name='parametros.pv_otvalor37']").length>0 && _fieldByName('parametros.pv_otvalor37').getValue().length>0){
                    Ext.Ajax.request({
                        url     : _p29_urlObtieneValNumeroSerie
                        ,params : {
                            'smap1.numSerie'  : _fieldByName('parametros.pv_otvalor37').getValue()
                            ,'smap1.feini'   : _fieldByName('feini').getValue()
                        }
                        ,success : function(response){
                            var json=Ext.decode(response.responseText);
                                debug(json);
                                if(json.exito!=true)
                                {
                                    if(!RolSistema.puedeSuscribirAutos(_p29_smap1.cdsisrol))
                                    {
                                        mensajeValidacionNumSerie("Error","${ctx}/resources/fam3icons/icons/exclamation.png", json.respuesta);
                                         alert('Se bloquea boton 2: '+_p29_smap1.cdsisrol);
                                        _fieldById('_p29_botonEmitir').setDisabled(true);//Deshabilita el boton
                                    }else{
                                        mensajeValidacionNumSerie("Aviso","${ctx}/resources/fam3icons/icons/error.png", json.respuesta);
                                        _fieldById('_p29_botonEmitir').setDisabled(false);
                                    }
                                }else{
                                    _fieldById('_p29_botonEmitir').setDisabled(false);
                                }
                        }
                        ,failure : errorComunicacion
                    });
                }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    
    if(_p29_smap1.cdramo==Ramo.ServicioPublico || _p29_smap1.cdramo==Ramo.AutosResidentes)
    {
        Ext.Ajax.request(
        {
            url     : _p29_urlRecuperacionSimple
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_DATOS_VEHICULO_RAMO_5'
                ,'smap1.cdunieco'     : _p29_smap1.cdunieco
                ,'smap1.cdramo'       : _p29_smap1.cdramo
                ,'smap1.estado'       : _p29_smap1.estado
                ,'smap1.nmpoliza'     : _p29_smap1.nmpoliza 
            }
            ,success : function(response)
            {
                var json = Ext.decode(response.responseText);
                debug('### recuperar datos vehiculo ramo 5:',json);
                if(json.exito)
                {
                    var _f1_aux=
                    [
                        {
                            xtype       : 'displayfield'
                            ,fieldLabel : 'VEH&Iacute;CULO'
                            ,value      : json.smap1.datos
                        }
                    ];
                    var form=_fieldById('_p29_adicionalesForm');
                    var anteriores=form.removeAll(false);
                    form.add(
                    {
                        xtype       : 'displayfield'
                        ,fieldLabel : 'VEH&Iacute;CULO'
                        ,value      : json.smap1.datos
                    });
                    for(var i=0;i<anteriores.length;i++)
                    {
                        form.add(anteriores[i]);
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function(){ errorComunicacion(); }
        });
    }
}

function _p29_personaSaved(json)
{
    debug('>_p29_personaSaved');
    Ext.Ajax.request(
    {
        url     : _p29_urlMovimientoMpoliper
        ,params :
        {
            'smap1.cdunieco'  : _p29_smap1.cdunieco
            ,'smap1.cdramo'   : _p29_smap1.cdramo
            ,'smap1.estado'   : _p29_smap1.estado
            ,'smap1.nmpoliza' : _p29_smap1.nmpoliza
            ,'smap1.nmsituac' : '0'
            ,'smap1.cdrol'    : '1'
            ,'smap1.cdperson' : json.smap1.CDPERSON
            ,'smap1.nmsuplem' : '0'
            ,'smap1.status'   : 'V'
            ,'smap1.nmorddom' : json.smap1.NMORDDOM
            ,'smap1.accion'   : 'I'
            ,'smap1.swexiper' : _SWexiper
        }
        ,success : function(response)
        {
            var json=Ext.decode(response.responseText);
            debug('### mpoliper:',json);
            if(json.exito)
            {
                if(!Ext.isEmpty(_p22_parentCallbackCallback))
                {
                    _p22_parentCallbackCallback();
                    _p22_parentCallbackCallback=null;
                }
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    debug('<_p29_personaSaved');
}

function _p29_emitirClic()
{
    debug('>_p29_emitirClic');
    
    _p29_guardar(_p29_mostrarVistaPrevia);
    
    debug('<_p29_emitirClic');
}

function _p29_guardar(callback)
{
    debug('>_p29_guardar');
    _p22_parentCallbackCallback = null;
    var form1  = _fieldById('_p29_polizaForm');
    var form2  = _fieldById('_p29_adicionalesForm');
    if(esDXN){
        var form3  = _fieldById('_p29_dxnForm');
       
    }else{
        var form3={}
        form3.isValid=function(){ return true;}
    }
    
    var valido = form1.isValid()&&form2.isValid()&&form3.isValid();
    if(!valido)
    {
        datosIncompletos();
    }
    
    if(valido)
    {
        valido = _fieldByName('_p22_formBusqueda').isValid() && _fieldByName('_p22_formDatosGenerales').isValid() && _fieldByName('_p22_formDomicilio').isValid() ;
        if(!valido)
        {
            mensajeWarning('Falta registrar un cliente');
        }
    }
    
    if(valido)
    {
        if(esDXN){
        guardaEmpleado();
        }
        _p22_parentCallbackCallback = function()
        {
            var json =
            {
                smap1  : form1.getValues()
                ,smap2 : form2.getValues()
            };
            //para mandar los datos dxn
            if(esDXN){
                try{
                     var  datos=_fieldByName('_p29_dxnForm').getForm().getValues();
                     json.smap1["parametros.pv_otvalor10"]=datos['aux.otvalor10'];
                     json.smap1["parametros.pv_otvalor11"]=datos['aux.otvalor11'];
                     json.smap1["parametros.pv_otvalor12"]=datos['aux.otvalor12'];
                     json.smap1["parametros.pv_otvalor13"]=datos['aux.otvalor13'];
                     json.smap1["parametros.pv_otvalor14"]=datos['aux.otvalor14'];
                     json.smap1["parametros.pv_otvalor15"]=datos['aux.otvalor15'];
                     json.smap1["parametros.pv_otvalor16"]=datos['aux.otvalor16'];
                     //para los datos variables dxn
                     json.smap1["parametros.pv_otvalor17"]=datos['aux.otvalor17'];
                     json.smap1["parametros.pv_otvalor18"]=datos['aux.otvalor18'];
                     json.smap1["parametros.pv_otvalor19"]=datos['aux.otvalor19'];
                     json.smap1["parametros.pv_otvalor20"]=datos['aux.otvalor20'];
                     json.smap1["parametros.pv_otvalor21"]=datos['aux.otvalor21'];
                }catch(e){
                    debugError(e);
                }
            }
                ////////////////////////////
            json.smap1['cdunieco'] = _p29_smap1.cdunieco;
            json.smap1['cdramo']   = _p29_smap1.cdramo;
            json.smap1['estado']   = _p29_smap1.estado;
            json.smap1['ntramite'] = _p29_smap1.ntramite;
            json.smap1['cdagente'] = _fieldByLabel('AGENTE').getValue();
        
            debug('json para guardar:',json);
            var panelPri = _fieldById('_p29_panelpri');
            panelPri.setLoading(true);
            Ext.Ajax.request(
            {
                url       : _p29_urlGuardar
                ,jsonData : json
                ,success  : function(response)
                {
                    panelPri.setLoading(false);
                    var json = Ext.decode(response.responseText);
                    debug('### guardar:',json);
                    if(json.exito)
                    {
                        if(_p29_validaSeguro =="S"){
                            _p32_guardarClic(callback);
                        }else{
                            if(callback){
                                callback();
                            }
                            else{
                                mensajeCorrecto('Datos guardados',json.respuesta);
                            }
                        }
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure  : function()
                {
                    panelPri.setLoading(false);
                    errorComunicacion();
                }
            });
        };
        _fieldByName('_p22_botonGuardar').handler();
    }
    
    debug('<_p29_guardar');
}

function _p29_mostrarVistaPrevia()
{
    debug('>_p29_mostrarVistaPrevia');
    var panelpri = _fieldById('_p29_panelpri');
    panelpri.setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p29_urlRecotizar
        ,params :
        {
            cdunieco             : _p29_smap1.cdunieco
            ,cdramo              : _p29_smap1.cdramo
            ,cdtipsit            : _p29_smap1.cdtipsit
            ,'panel1.nmpoliza'   : _p29_smap1.nmpoliza
            ,'panel1.notarifica' : 'si'
        }
        ,success : function(response)
        {
            panelpri.setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('### retarificar:',json);
            if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
            {
                centrarVentanaInterna(Ext.Msg.show(
                {
                    title    :'Verificar datos'
                    ,msg     : json.mensajeRespuesta
                    ,buttons : Ext.Msg.OK
                    ,icon    : Ext.Msg.WARNING
                }));
            }
            else
            {
                var orden=0;
                var parentescoAnterior='qwerty';
                for(var i=0;i<json.slist1.length;i++)
                {
                    if(json.slist1[i].parentesco!=parentescoAnterior)
                    {
                        orden++;
                        parentescoAnterior=json.slist1[i].parentesco;
                    }
                    json.slist1[i].orden_parentesco=orden+'_'+json.slist1[i].parentesco;
                }
                centrarVentanaInterna(Ext.create('Ext.window.Window',
                {
                    title          : 'Tarifa final'
                    ,autoScroll    : true
                    ,width         : 660
                    ,height        : 400
                    ,defaults      : { width: 650 }
                    ,modal         : true
                    ,closable      : false
                    ,collapsible   : true
                    ,titleCollapse : true
                    ,items         :
                    [
                        Ext.create('Ext.grid.Panel',
                        {
                            width  : 600
                            ,store : Ext.create('Ext.data.Store',
                            {
                                model       : 'ModeloDetalleCotizacion'
                                ,groupField : 'orden_parentesco'
                                ,sorters    :
                                [
                                    {
                                        sorterFn: function(o1, o2)
                                        {
                                            if (o1.get('orden') === o2.get('orden'))
                                            {
                                                return 0;
                                            }
                                            return o1.get('orden') < o2.get('orden') ? -1 : 1;
                                        }
                                    }
                                ]
                                ,proxy :
                                {
                                    type    : 'memory'
                                    ,reader : 'json'
                                }
                                ,data:json.slist1
                            })
                            ,columns :
                            [
                                {
                                    header           : 'Nombre de la cobertura'
                                    ,dataIndex       : 'Nombre_garantia'
                                    ,flex            : 2
                                    ,summaryType     : 'count'
                                    ,summaryRenderer : function(value)
                                    {
                                        return Ext.String.format('Total de {0} cobertura{1}', value, value !== 1 ? 's' : '');
                                    }
                                }
                                ,{
                                    header       : 'Importe por cobertura'
                                    ,dataIndex   : 'Importe'
                                    ,flex        : 1
                                    ,renderer    : Ext.util.Format.usMoney
                                    ,align       : 'right'
                                    ,summaryType : 'sum'
                                }
                            ]
                            ,features :
                            [
                                {
                                    groupHeaderTpl :
                                    [
                                        '{name:this.formatName}'
                                        ,{
                                            formatName:function(name)
                                            {
                                                return name.split("_")[1];
                                            }
                                        }
                                    ]
                                    ,ftype          : 'groupingsummary'
                                    ,startCollapsed : !RolSistema.puedeSuscribirAutos(_p29_smap1.cdsisrol)//_p29_smap1.cdsisrol!='SUSCRIAUTO'
                                    ,listeners      :
                                    {
                                        groupexpand : function(view,node,group)
                                        {
                                            if(!RolSistema.puedeSuscribirAutos(_p29_smap1.cdsisrol))
                                            {
                                                this.collapseAll();
                                            }
                                        }
                                    }
                                }
                            ]
                        })
                        ,Ext.create('Ext.toolbar.Toolbar',
                        {
                            buttonAlign : 'right'
                            ,items      :
                            [
                                '->'
                                ,Ext.create('Ext.form.Label',
                                {
                                    style          : 'color:white;'
                                    ,initComponent : function()
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
                            layout :
                            {
                                type     : 'table'
                                ,columns : 5
                            }
                            ,defaults : { style : 'margin:5px;' }
                            ,items    :
                            [       //Inicio de Items . Ext.form.Panel
                                {
                                    xtype       : 'textfield'
                                    ,itemId     : '_p29_numerofinalpoliza'
                                    ,fieldLabel : 'N&uacute;mero de poliza'
                                    ,readOnly   : true
                                }
                                ,{
                                    itemId   : '_p29_botonEmitirPolizaFinal'
                                    ,xtype   : 'button'
                                    ,text    : 'Emitir'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
                                    ,handler : _p29_emitirFinal
//                                  ,hidden  : _p29_habilitarBotonEmitir == 'N'
                                },{
                                    itemId : 'botonEnvioEmail'
                                    ,xtype : 'button'
                                    ,text  : 'Enviar Email'
                                    ,icon  : '${ctx}//resources/fam3icons/icons/email.png'
                                    ,disabled: true
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
                                                                            ,icon    : 'x-message-box-ok'
                                                                            ,fn      : function()
                                                                            {
                                                                                _generarRemesaClic(
                                                                                    false
                                                                                    ,_p29_smap1.cdunieco
                                                                                    ,_p29_smap1.cdramo
                                                                                    ,'M'
                                                                                    ,_p29_smap1.nmpolizaEmitida
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
                                    itemId : 'botonReenvioWS'
                                    ,xtype : 'button'
                                    ,text  : 'Reintentar Emisi&oacute;n'
                                    ,icon  : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
                                    ,disabled: true
                                    ,handler:function()
                                    {
                                        var me=this;
                                        reintentarWSAuto(me.up().up(), _paramsRetryWS);
                                    }
                                }
                                ,{
                                    xtype     : 'button'
                                    ,itemId   : '_p29_botonDocumentosPolizaEmitida'
                                    ,text     : 'Imprimir'
                                    ,icon     : '${ctx}/resources/fam3icons/icons/printer.png'
                                    ,disabled : true
                                    ,handler  : function(me)
                                    {
                                        var callbackRemesa = function()
                                        {
                                            centrarVentanaInterna(Ext.create('Ext.window.Window',
                                            {
                                                title        : 'Documentos del tr&aacute;mite'
                                                ,modal       : true
                                                ,buttonAlign : 'center'
                                                ,width       : 600
                                                ,height      : 400
                                                ,autoScroll  : true
                                                ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                                ,loader      :
                                                {
                                                    url       : _p29_urlDocumentosPoliza
                                                    ,params   :
                                                    {
                                                        'smap1.nmpoliza'  : _p29_smap1.nmpolizaEmitida
                                                        ,'smap1.cdunieco' : _p29_smap1.cdunieco
                                                        ,'smap1.cdramo'   : _p29_smap1.cdramo
                                                        ,'smap1.estado'   : 'M'
                                                        ,'smap1.nmsuplem' : '0'
                                                        ,'smap1.ntramite' : ''
                                                        ,'smap1.nmsolici' : _p29_smap1.nmpoliza
                                                        ,'smap1.tipomov'  : '0'
                                                    }
                                                    ,scripts  : true
                                                    ,autoLoad : true
                                                }
                                            }).show());
                                        };
                                        _generarRemesaClic(
                                            false
                                            ,_p29_smap1.cdunieco
                                            ,_p29_smap1.cdramo
                                            ,'M'
                                            ,_p29_smap1.nmpolizaEmitida
                                            ,callbackRemesa
                                        );
                                    }
                                }//fin boton impresion
                                ,{
                                    xtype    : 'button'
                                    ,itemId  : '_p29_botonCancelarEmision'
                                    ,text    : 'Cancelar'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                    ,handler : function()
                                    {
                                        var me=this;
                                        me.up().up().destroy();
                                    }
                                }
                                ,{
                                    xtype     : 'button'
                                    ,itemId   : '_p29_botonNueva'
                                    ,disabled : true
                                    ,text     : 'Nueva'
                                    ,hidden   : !Ext.isEmpty(_p29_flujo)
                                    ,icon     : '${ctx}/resources/fam3icons/icons/arrow_refresh.png'
                                    ,handler  : function()
                                    {
                                        Ext.create('Ext.form.Panel').submit(
                                        {
                                            url             : _p29_urlCotizacionAutoIndividual
                                            ,standardSubmit : true
                                            ,params         :
                                            {
                                                'smap1.cdramo'    : _p29_smap1.cdramo
                                                ,'smap1.cdtipsit' : _p29_smap1.cdtipsit
                                            }
                                        });
                                    }
                                }
//====================================--------------------=======================================||
                             ,{
                                      hidden  :  Number(_fieldByName('cdperpag').getValue()) != 1 
                                     ,xtype   : 'button'
                                     ,itemId  : '_p29_botonRedireccionar'
                                     ,text    : 'Cobranza Automatica'
                                     ,icon    : '${ctx}/resources/fam3icons/icons/application_link.png'
                                     ,disabled : true
                                     ,handler : function()
                                     {  
                                         domiciliar();
                                     }
                                 }
                                ,{
                                    itemId   : '_p29_botonMesaControl'
                                    ,xtype   : 'button'
                                    ,text    : 'Mesa de control'
                                    ,icon    : '${ctx}/resources/fam3icons/icons/house.png'
                                    ,hidden  : Ext.isEmpty(_p29_flujo)
                                    ,handler : function(){
                                        _mask('Redireccionando...');
                                        Ext.create('Ext.form.Panel').submit({
                                            standardSubmit : true
                                            ,url           : _GLOBAL_COMP_URL_MCFLUJO
                                            });     
                                        } 
//                                  ,hidden  : _p29_habilitarBotonEmitir == 'N'
                                }
                            
                            ]//Fin Items. Ext.form.Panel
                        })//Fin Ext.form.Panel (Fin ext.create
                    ]//Fin Items. Ext.window.Window
                }).show());//Fin Ext.window.Window, Fin ext.create Ext.window.Window and Show(it), Fin centrarVentanaInterna
            }
        }
        ,failure : function()
        {
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p29_mostrarVistaPrevia');
}

function _p29_emitirFinal(me)
{
    debug('>_p29_emitirFinal');
    var panelpri = me.up().up();
    panelpri.setLoading(true);
    Ext.Ajax.request(
    {
        url      : _p29_urlEmitir
        ,params  :
        {
            'panel1.pv_ntramite'  : _p29_smap1.ntramite
            ,'panel2.pv_cdunieco' : _p29_smap1.cdunieco
            ,'panel2.pv_cdramo'   : _p29_smap1.cdramo
            ,'panel2.pv_estado'   : _p29_smap1.estado
            ,'panel1.pv_nmpoliza' : _p29_smap1.nmpoliza
            ,'panel2.pv_nmpoliza' : _p29_smap1.nmpoliza
            ,'panel2.pv_cdtipsit' : _p29_smap1.cdtipsit
        }
        ,success:function(response)
        {
            panelpri.setLoading(false);
            var json=Ext.decode(response.responseText);
            ramogs = json.cdRamoGS;
            poliza = json.nmpolAlt;
            debug('### emitir:',json);
            if(json.success==true)
            {   //vils 
                _fieldById('_p29_botonRedireccionar').setDisabled(false);
            
                _p29_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                debug("_p29_smap1.nmpolizaEmitida:",_p29_smap1.nmpolizaEmitida);
                
                
                if(json.retryRec){
                    _fieldById('_p29_botonEmitirPolizaFinal').hide();
                    _fieldById('_p29_botonCancelarEmision').setDisabled(true);
                    _fieldById('_p29_botonNueva').setDisabled(false);
                    
                    
                    Ext.Msg.show(
                    {
                        title    :'Aviso'
                        ,msg     : 'Error en la emisi&oacute;n. No se pudo emitir la p&oacute;liza.'
                        ,buttons : Ext.Msg.OK
                        ,icon    : Ext.Msg.WARNING
                        ,fn      : function()
                        {
                                var paramsWS = {
                                     'panel1.pv_nmpoliza' : _p29_smap1.nmpoliza
                                    ,'panel1.pv_ntramite' : _p29_smap1.ntramite
                                    ,'panel2.pv_cdramo'   : _p29_smap1.cdramo
                                    ,'panel2.pv_cdunieco' : _p29_smap1.cdunieco
                                    ,'panel2.pv_estado'   : _p29_smap1.estado
                                    ,'panel2.pv_nmpoliza' : _p29_smap1.nmpoliza
                                    ,'panel2.pv_cdtipsit' : _p29_smap1.cdtipsit
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
                
                _fieldById('_p29_numerofinalpoliza').setValue(json.panel2.nmpoliex);
                _fieldById('_p29_botonEmitirPolizaFinal').setDisabled(true);
                _fieldById('_p29_botonDocumentosPolizaEmitida').setDisabled(false);
                _p29_ventanaDocs.destroy();
                
                _fieldById('botonReenvioWS').hide();
                _mensajeEmail = json.mensajeEmail;
                _fieldById('botonEnvioEmail').enable();
                
                if(Number(_fieldByName('cdperpag').getValue()) == 1)
                {domiciliar();}
                
                _fieldById('_p29_botonCancelarEmision').setDisabled(true);
                _fieldById('_p29_botonNueva').setDisabled(false);
                if(json.mensajeRespuesta&&json.mensajeRespuesta.length>0)
                {
                    var ventanaTmp = Ext.Msg.show(
                    {
                        title    : 'Aviso del sistema'
                        ,msg     : json.mensajeRespuesta
                        ,buttons : Ext.Msg.OK
                        ,icon    : Ext.Msg.WARNING
                        ,fn      : function()
                        {
                            if(!Ext.isEmpty(json.nmpolAlt))
                            {
                                mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
                            }
                        }
                    });
                    centrarVentanaInterna(ventanaTmp);
                }
                else
                {
                    if(!Ext.isEmpty(json.nmpolAlt))
                    {
                        mensajeCorrecto("Aviso","P&oacute;liza Emitida: " + json.nmpolAlt);
                    }
                }
            }
            else
            {
                if(json.retryWS){
                    
                    _p29_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                    debug("_p29_smap1.nmpolizaEmitida, en reintento WS:" , _p29_smap1.nmpolizaEmitida);
                
                    _fieldById('_p29_botonEmitirPolizaFinal').hide();
                    _fieldById('_p29_botonCancelarEmision').setDisabled(true);
                    _fieldById('_p29_botonNueva').setDisabled(false);
                }
                Ext.Msg.show(
                {
                    title    :'Error'
                    ,msg     : Ext.isEmpty(json.mensajeRespuesta)?"Error al emitir":json.mensajeRespuesta
                    ,buttons : Ext.Msg.OK
                    ,icon    : Ext.Msg.ERROR
                    ,fn      : function()
                    {
                        if(json.retryWS)
                        {
                            var paramsWS =
                            {
                                'panel1.pv_nmpoliza'  : _p29_smap1.nmpoliza
                                ,'panel1.pv_ntramite' : _p29_smap1.ntramite
                                ,'panel2.pv_cdramo'   : _p29_smap1.cdramo
                                ,'panel2.pv_cdunieco' : _p29_smap1.cdunieco
                                ,'panel2.pv_estado'   : _p29_smap1.estado
                                ,'panel2.pv_nmpoliza' : _p29_smap1.nmpoliza
                                ,'panel2.pv_cdtipsit' : _p29_smap1.cdtipsit
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
            panelpri.setLoading(false);
            errorComunicacion();
        }
    });
    debug('<_p29_emitirFinal');
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
                                
                                _p29_smap1.nmpolizaEmitida=json.panel2.nmpoliza;
                                
                                _fieldById('_p29_numerofinalpoliza').setValue(json.nmpolAlt);
                                _fieldById('_p29_botonDocumentosPolizaEmitida').setDisabled(false);
                                _p29_ventanaDocs.destroy();
                                _fieldById('botonReenvioWS').setDisabled(true);
                                _fieldById('botonReenvioWS').hide();
                                
                                _mensajeEmail = json.mensajeEmail;
                                _fieldById('botonEnvioEmail').enable();
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
            _fieldById('botonReenvioWS').setDisabled(false);
        }
       }
    });
                        
}

function mensajeValidacionNumSerie(titulo,imagenSeccion,txtRespuesta){
    var panelImagen = new Ext.Panel({
        defaults    : {
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
            ,src    : '${ctx}/images/cotizacionautos/menu_endosos.png'
            ,width: 200
            ,height: 100
        }]
    });

    validacionNumSerie = Ext.create('Ext.window.Window',{
        title        : titulo
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 520
        ,icon        : imagenSeccion
        ,resizable   : false
        ,height      : 250
        ,items       :[
            Ext.create('Ext.form.Panel', {
                id: 'panelClausula'
                ,width       : 500
                ,height      : 150
                ,bodyPadding: 5
                ,renderTo: Ext.getBody()
                ,defaults    : {
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
                        [   panelImagen     ]
                }]
            })
        ],
        buttonAlign:'center',
        buttons: [{
            text: 'Aceptar',
            icon: '${ctx}/resources/fam3icons/icons/accept.png',
            buttonAlign : 'center',
            handler: function() {
                validacionNumSerie.close();
            }
        }]
    });
    centrarVentanaInterna(validacionNumSerie.show());
}

function domiciliar()
{   
    var callbackFunction = function(idUsuario)
    {
    var url = _url_domiciliacion+"?u="+idUsuario+"&suc="+_p29_smap1.cdunieco+"&ram="+ramogs+"&pol="+poliza;
    centrarVentanaInterna(Ext.create('Ext.window.Window',
      {
           title       : 'Datos de Poliza'
          ,modal       : true
          ,buttonAlign : 'center'
          ,width       : 1000
          ,height      : 500
          ,autoScroll  : false
          ,html        :'<iframe width="950" height="500" src="'+url+'"></iframe>'
          
      }).show());
    };
  
    callbackFunction(claveUsuarioCaptura);
}

function obtenerIDUsusigs(callback)
{
    if (Ext.isEmpty(claveUsuarioCaptura)) {
    
        _mask('Cargando Usuario');
        Ext.Ajax.request(
            { 
                url     : _p28_urlCargarIdUsu
                ,params :
                {
                    'smap1.cdusuari' : _p29_smap1.cdusuari
                }
                ,success : function(response)
                {
                    _unmask();
                   var json = Ext.decode(response.responseText);
                   if(json.success == true){
                       
                       claveUsuarioCaptura = json.smap1.idUsu;
                       
                       if(!Ext.isEmpty(callback))
                       {
                           callback();
                       }
                                            }
                   else{console.log(json.respuesta);}
                }
                ,failure  : function(response)
                {   _unmask();
                    console.log(response);
                }
           }
         );
    }
}

function bloquearBotonEmitir() 
{
      Ext.Ajax.request(
            {
                 url     : _p29_urlRecuperacion
                ,params :
                {
                     'params.consulta'     : 'RECUPERAR_ESTADO_BOTON_EMITIR'
                    ,'params.cdtipsit'     : _p29_smap1.cdtipsit
                }
                ,success : function(response)
                {
                    _unmask();
                    var json = Ext.decode(response.responseText);
                    debug('### Response Boton Comprar:',json);
                    if(json.success ==  true)
                    {
                        var ck = 'Decodificando respuesta al recuperar permisos de boton Comprar';
                        try
                        {
                            var json = Ext.decode(response.responseText);
                            debug('### permisos boton comprar:',json);
                            if(json.success==true)
                            {
                                if(json.params.ACTIVAR_BOTON_COMPRAR == 'N')
                                    { mensajeWarning('Sin permisos para Emitir');
                                      _fieldById('_p29_botonEmitir').hide();
                                    }
                                
                                _p29_habilitarBotonEmitir = json.params.ACTIVAR_BOTON_COMPRAR+"";
                            }
                            else
                            {
                                mensajeError(json.message);
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
                    _unmask();
                    errorComunicacion();
                }
            });
        }
        
        
function ventanaBusquedaEmpleado(){
    var ventana=Ext.create('Ext.window.Window',
            {
                title      : 'Recuperar cliente'
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
                                    buscarEmpleado(
                                            _fieldByName("aux.otvalor08").getValue()
                                            ,_fieldByName("aux.otvalor09").getValue()
                                            ,_fieldByName('no_empleado').getValue()
                                            ,_fieldByName('rfc').getValue()
                                            ,_fieldByName('ap_paterno').getValue()
                                            ,_fieldByName('ap_materno').getValue()
                                            ,_fieldByName('nombre').getValue()
                                            );
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
                                    
                                                    _fieldByName("aux.otvalor10").setValue(record.raw.clave_empleado);
                                                    _fieldByName("aux.otvalor11").setValue(record.raw.nombre);
                                                    _fieldByName("aux.otvalor12").setValue(record.raw.apellido_p);
                                                    _fieldByName("aux.otvalor13").setValue(record.raw.apellido_m);
                                                    _fieldByName("aux.otvalor14").setValue(record.raw.rfc);
                                                    _fieldByName("aux.otvalor16").setValue(record.raw.curp);
                                    
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
}

function guardaEmpleado(){
    try{
        ck="preparando datos para enviar";
        var datos=_fieldByName('_p29_dxnForm').getForm().getValues();
        debug("### Datos de empleado:", datos);
        
        $.ajax({
            async:false, 
            cache:false,
            dataType:"json", 
            type: 'POST',   
            url: url_guarda_empleado ,
            data: {
                'params.administradora':datos['aux.otvalor08'],
                'params.retenedora':datos['aux.otvalor09'],
                'params.clave':datos['aux.otvalor10'],
                'params.nombre':datos['aux.otvalor11'],
                'params.apaterno':datos['aux.otvalor12'],
                'params.amaterno':datos['aux.otvalor13'],
                'params.rfc':datos['aux.otvalor14'],
                'params.clavedescuento':datos['aux.otvalor15'],
                'params.curp':datos['aux.otvalor16']
               
            }, 
            success:  function(respuesta){
                var ck = 'Decodificando respuesta al recuperar datos para guardar empleado';
                
             try
             {
                 var jsonDatTram =  respuesta// Ext.decode(response.responseText);
                 debug('### jsonDatTram:',jsonDatTram,'.');
                 
                 if(jsonDatTram.success === true)
                 {
                  
                     //alert()
                    
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
        _fieldByLabel('CLAVE DE DESCUENTO').store.proxy.extraParams['params.administradora']=administradora;
        _fieldByLabel('CLAVE DE DESCUENTO').store.proxy.extraParams['params.retenedora']=retenedora;
        _fieldByLabel('CLAVE DE DESCUENTO').store.proxy.extraParams['params.cdramo']=cdramo;
        _fieldByLabel('CLAVE DE DESCUENTO').store.proxy.extraParams['params.cdtipsit']=cdtipsit;
    }catch(e){
        debugError(e)
    }
}

function buscarEmpleado(administradora,retenedora,ce,rfc,ap,am,nom){
    
    debug('recuperar empleado buscar');
    
    _fieldById('_p29_dxnForm').setLoading(true);
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
                    _fieldById('_p29_dxnForm').setLoading(false);
                    var json = Ext.decode(response.responseText);
                    debug('### Response Boton Comprar:',json);
                    if(json.success ==  true)
                    {
                        var ck = 'Decodificando respuesta buscar empleado';
                        try
                        {
                            //alert(json);
                            debug("### respuesta ",json);
                            
                            if(json.slist1.length>0){
                                if(Ext.ComponentQuery.query("[name=gridBuscaEmpleado]").length==0){
                                    ventanaBusquedaEmpleado()
                                }
                                
                                _fieldByName("gridBuscaEmpleado").getStore().loadData(json.slist1);
                                _fieldByName("aux.otvalor10").setReadOnly(true);
                                _fieldByName("aux.otvalor11").setReadOnly(true);
                                _fieldByName("aux.otvalor12").setReadOnly(true);
                                _fieldByName("aux.otvalor13").setReadOnly(true);
                                _fieldByName("aux.otvalor14").setReadOnly(true);
                                _fieldByName("aux.otvalor16").setReadOnly(true);
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
                    _fieldById('_p29_dxnForm').setLoading(false);
                    errorComunicacion();
                }
            });
    
}

function cargaComentariosRamo6(){
	try{
		Ext.ComponentQuery.query('[name=aux.otvalor08]').forEach(function(it){
			it.store.proxy.extraParams['params.negocio']=_fieldByLabel('NEGOCIO',null,true).getValue();
			it.store.load();
			it.store.on({
                load      : function(store){
                               store.insert(0,{"aux":null,"aux2":null,"aux3":null,"key":null,"value":"(NINGUNO)"})
                            }
});
		});
	}catch(e){
		debugError(e);
	}
}

function rangoPeriodoGracia(){
	
	try{
		Ext.Ajax.request(
			    {
			        url     : _p29_urlRangoPeriodogracia
			        ,params :
			        {
			            
			             'smap1.cdramo'   : _p29_smap1.cdramo
			            ,'smap1.cdtipsit' : _p29_smap1.cdtipsit
			            ,'smap1.cdagente' : _fieldByLabel('AGENTE',null,true).getValue()
			           
			        }
			        ,success : function(response)
			        {
			            var json=Ext.decode(response.responseText);
			            debug('### respuesta periodo gracia:',json);
			            if(json.exito)
			            {
			                if(!Ext.isEmpty(json.slist1)){
			                	json.slist1.forEach(function(it){
			                		_fieldByLabel('PERIODO GRACIA',null,true).getStore().filter(
			                				[
			                					{
			                						filterFn: function(item) {
			                							var val=parseInt(item.get("key"));
			                							it.MINIMO=parseInt(it.MINIMO);
			                							it.MAXIMO=parseInt(it.MAXIMO);
			                							debug("test: ",item.get("key")," - ",it.MINIMO," ",it.MAXIMO," ",item.get("key") >= it.MINIMO," ",item.get("key") <= it.MAXIMO)
			                							return val >= it.MINIMO && val <= it.MAXIMO ; 
			                						}
			                					}
			                				]
			                				);
			                	});
			                }
			            }
			            else
			            {
			                mensajeError(json.respuesta);
			            }
			        }
			        ,failure : errorComunicacion
			    });
	}catch(e){
		debugError(e)
	}
}
////// funciones //////
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
</head>
<body><div id="_p29_divpri" style="height:1500px;"></div></body>
</html>